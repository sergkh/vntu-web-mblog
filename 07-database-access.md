Реалізація роботи з базою даних в *Spring*
=====

Традиційною технологією для організації роботи з СУБД на мові *Java* є JDBC (англ. Java DataBase Connectivity — з'єднання з базами даних на Java). Однак існують фреймворки, які дозволяють пов’язувати *Java* об’єкти з таблицями в СУБД й працювати з базою на рівні об’єктної моделі. Одним з таких фреймворків є *Hibernate*. Даний фреймворк забезпечує переведення реляційної моделі в об’єктну й навпаки, таким чином з розробника знімається багато задач роботи з СУБД й все що необхідно зробити це розробити модель додатку на рівні об’єктів та помітити об’єкти спеціальними анотаціями.

Для початку необхідно спроектувати об’єктну модель додатку. 






*Spring* фреймворк дозволяє значно спростити роботу з СУБД й перевести її на рівень роботи з об’єктам


JDBC (англ. Java DataBase Connectivity — з'єднання з базами даних на Java) це стандартний програмний інтерфейс для роботи з СУБД на Java. В JDBC описані основні класи та Java інтерфейси, що описують методи встановлення з’єднання до бази даних, отримання вибірок, оновлення бази та інші. Дані інтерфейси описані в пакеті `java.sql`.

Реалізацію цих інтерфейсів для конкретної СУБД зазвичай предоставляють розробники самої СУБД або ж сторонні розробники. Така реалізація називається JDBC драйвером й представляє собою `*.jar` файл з класами, що реалізують протокол конкретної СУБД.

В даному випадку використовуватиметься СУБД **H2**, це вбудовувана (embedded) база даних написана на Java. Це означає, що вам не доведеться встановлювати й налаштовувати окремо СУБД, а потім знаходити до неї драйвер JDBC, а достатньо скачати сам драйвер `h2-версія.jar` в якому вже й знаходиться СУБД.

Отже скачавши [файл драйверу](http://www.h2database.com/html/download.html) й розмістивши його у папці `WEB-INF/lib` проекту (папка де знаходяться бібліотеки веб проекту) можливо встановити з’єднання до СУБД:

    import java.sql.*;
    
    public class ConnectionManager {
       private static final String DB_URL = "jdbc:h2:mem:univ";
       private static final String USER = "username";
       private static final String PASS = "password";
       
       public Connection getConnection() {
          Connection conn = null;
          Statement stmt = null;
          try {
             Class.forName("org.h2.Driver");
             return DriverManager.getConnection(DB_URL, USER, PASS);
          } catch(Exception e){
             throw new RuntimeException(ex);   
          }
       }
    }


`DB_URL` задає тип СУБД та шлях до бази даних. В даному випадку задається СУБД `h2`, шлях до бази данних - `mem`, що означає, що база даних повинна розміщуватись у пам’яті (й відповідно знищуватись при закритті додатку) та назву бази даних `univ`. 

За назвою СУБД знаходиться відповідний драйвер, який повинен бути попередньо зареєстрованим у менеджері драйверів JDBC, що й фактично робить команда Class.forName("org.h2.Driver") - завантажує класс драйверу й він автоматично реєструється. 

Шлях до бази залежить від драйверу й конкретної СУБД. Наприклад:

`"jdbc:h2:~/univ"` - СУБД h2 - розміщення на диску у файлі ~/univ

`"jdbc:mysql://192.168.1.56/univ"` - СУБД MySql, що знаходиться за адресою 192.168.1.56, назва бази даних `univ`.

Приклад використання з’єднання у об’єктах доступу до бази (DAO):

    public class StudentsDao {
    
      public List<Student> getAll() {
         Connection con = ConnectionManager.getConnection();
         try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
    
            List<Student> students = new ArrayList<Student>();
    
            while(rs.next()) {
    
               long id  = rs.getLong("id");
               String name = rs.getString("name");
               String email = rs.getString("email");
    
               students.add(new Student(id, name, email));
            }
    
            rs.close();
            stmt.close();
    
            return students;
         } catch(Exception ex) {
            throw new RuntimeException(ex);
         } finally {
            con.close();
         }
      }
    
      public void create(Student student) {
          String sql = "INSERT INTO students (name, email, group_id, mark) VALUES (?,?,?,?)";
    
          try (Connection con = ConnectionManager.getConnection();
               PreparedStatement createSt = con.prepareStatement(sql)) {
    
              createSt.setString(1, student.getName());
              createSt.setString(2, student.getEmail());
              createSt.setInt(3, student.getGroupId());
              createSt.setInt(4, student.getMark());
              createSt.executeUpdate();
    
              try(PreparedStatement getIdSt = con.prepareStatement("CALL IDENTITY()")) {
                  ResultSet rs = getIdSt.executeQuery();
                  
                  if(rs.next()) { 
                    student.setId(rs.getLong(1));
                  }
              }
    
          } catch (SQLException e) {
              throw new RuntimeException(e);
          }
      }
    
    }

Пул з’єднань
-----------

TODO

Робота з транзакціями
--------------

Для того, щоб розпочати працювати з транзакціями в JDBC, необхідно просто встановити для з’єднання:

    connection.setAutoCommit(false);

що означає - не виконувати одразу кожен SQL вираз (Statement), а виконати тільки при послідуючому виклику методу

    connection.commit();

або відмінити зміни:

    c.rollback();

Однак часто логіка додатку, яка повинна виконуватись в одній транзакції розміщується у декількох методах об’єкту DAO, або навіть у декількох об’єктах DAO. Наприклад, для того, щоб додати студента в табличку студентів, необхідно виконати наступні дії:

1. Перевірити чи в таблиці груп існує група, вказана у студента.
2. Якщо немає - створити таку групу та отримати її ідентифікатор.
3. Додати студента у таблицю студентів.

В даному випадку створення групи, якщо студент не може бути доданий (наприклад пошта вже зареєстрована) немає змісту - тому необхідно щоб усі зазначені дії виконувались у одній транзакції. При цьому за створення й отримання груп може відповідати клас GroupsDao, а за роботу зі студентами StudentsDao. 

Можливо створення групи без користувача не призведе до серйозних помилок у лозіці додатку, але бувають більш складні випадки, наприклад, з додаванням студента необхідно збільшити лічильники кількості студентів у групи чи факультету - збільшити лічильники й недодати студента може призвести до значно критичніших наслідків.

Зазвичай принято, що такі дії, які є логічним цілим але складаються з декількох запитів заключаються в один метод класу сервісів. Й логічним при цьому є зробити щоб області видимості методу та транзації співпали - тобто відкривати з’єднання з транзакцією на початку методу сервісу та закривати в кінці. При цьому керування з’єднаннями потрібно винести з DAO об’єктів й передавати його ззовні.

Зберігати об’єкт з’єднання в DAO об’єкті не можна, оскільки паралельно в інших потоках можуть паралельно виконуватись ще запити, які не повинні доступатись до даного з’єднання. Тому необхідно обмежити область видимості DAO об’єкту методом сервісу або ж передавати з’єдання необхідно в кожен метод:

    public void create(Connection con, Student student);

Одним з варіантів щоб не засмічувати методи об’єктів DAO ще додатковим з’єднанням, можна скористатись наступним підходом: на початку методу сервісу створити з’єднання та прив’язати його до поточного потоку, для чого можна скористатись класом `ThreadLocal`. Далі з нього можна отримати з’єднання, яке буде унікальним саме для даного потоку:

    public class ConnectionManager {
      private static final ConnectionManager instance = new ConnectionManager(); 
      private final DataSource dataSource = ... ;
      
      private final ThreadLocal<Connection> threadLocalConnections = new ThreadLocal<Connection>(){
        @Override
        protected Connection initialValue() {
              try {
                    Connection c = dataSource.getConnection();
                    c.setAutoCommit(false);
                  return c;
              } catch (Exception e) {
                  throw new RuntimeException(e);
              }
        }
      };
      
      public static ConnectionManager getInstance() {
        return instance;
      }
      
      public Connection currentConnection() {
        return threadLocalConnections.get();
      }
        
      public void commitTransaction() {
        try {
          Connection c = currentConnection();
          c.commit();
          c.close();
          threadLocalConnections.remove();
        } catch (Exception e){
          throw new RuntimeException(e);
        }
      }
    
      public void rollbackTransaction() {
        try {
          Connection c = currentConnection(); 
          c.rollback();
          c.close();
          threadLocalConnections.remove();
        } catch (Exception e){
          throw new RuntimeException(e);
        }
      }
    }


