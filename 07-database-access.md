Реалізація роботи з базою даних в *Spring*
=====

Традиційною технологією для організації роботи з СУБД на мові *Java* є JDBC (англ. Java DataBase Connectivity — з'єднання з базами даних на Java). Однак існують фреймворки, які дозволяють пов’язувати *Java* об’єкти з таблицями в СУБД й працювати з базою на рівні об’єктної моделі, такі фреймворки називаються ORM (Object Relational Mapping – об’єктно реляційний мапінг). Одним з таких фреймворків є *Hibernate*. 

Даний фреймворк забезпечує переведення реляційної моделі в об’єктну й навпаки, таким чином з розробника знімається багато проблем роботи з СУБД — все що необхідно зробити це розробити модель додатку на рівні об’єктів та помітити об’єкти спеціальними анотаціями. Додатковим плюсом використання *Hibernate* є те, що оскільки ми не працюємо з базою даних напряму, додаток можна без змін переналаштувати на практично будь-яку реляційну СУБД. 

Для початку необхідно спроектувати саму об’єктну модель додатку. В нашому випадку, щоб не ускладнювати додаток, модель буде складатись лише з 2-х об’єктів:

![Об’єктна модель сайту](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/model.png)

Користувач має список власних постів та інших користувачів, на яких він підписаний. Відношення між користувачем і постом — один до багатьох, у користувача може бути багато постів, однак у поста буде лише один автор. Відношення між користувачами та тих, на кого вони підписані — багато до багатьох. 

Далі необхідно описати дані моделі в коді та помітити спеціальними анотаціями, які вкажуть *Hibernate* яким чином необхідно відображати об’єкти та їх зв’язки на таблиці бази даних.

Помістимо моделі в окремий пакет `labs.models`. Код користувача:

```java
package labs.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class User {
  
  @Id
  @GeneratedValue
  private Long id;
  
  @NotBlank
  @Size(min = 1, max = 512)
  @Column(unique = true)
  private String login;
  
  @NotBlank
  @Size(min = 1, max = 512)
  @Column(unique = true)
  private String email;
  
  @NotBlank
  @Size(min = 1, max = 100)
  private String passHash;
  
  @OneToMany(mappedBy = "author")
  private List<Post> posts = new ArrayList<>();
  
  @ManyToMany
  private Set<User> subscriptions = new HashSet<>();

  public User() {}

  get/set методи
}
```

Сам клас помічається анотацією `@Entity`, яка вказує, що клас повинен зберігатись у БД. *Hibernate* автоматично підбере імена табличок і полів за їх назвами (в даному випадку табличка `User`), однак їх можна вказати явно за допомогою додаткових анотацій `@Table("назва таблички")` та `@Column(name="назва колонки")`. За допомогою анотації `@Column` також можна вказати, що значення колонки повинне бути унікальним, як зроблено для `login` та `email`, при цьому в СУБД буде автоматично створено унікальний індекс по цим колонкам і буде викинуто виключення при спробі додати ще одного користувача з існуючим логіном чи поштою.

Клас моделі повинен обов’язково мати контруктор без параметрів та мати поле, яке буде первинним ключом в БД, це поле помічається як `@Id`, в прикладі це окреме поле `id`, яке додатково помічено анотацію `@GeneratedValue`, яка вказує *Hibernate*, що він має бути відповідальним за генерацію ідентифікаторів для нових записів. Тобто при додаванні нового запису ми не встановлюємо ідентифікатор запису — бібліотека сама встановить його.

Додатково бажано, але не обов’язково, можна вказати полям деякі обмеження, наприклад: `@NotNull` – не дасть зберегти об’єкт, якщо анотоване поле дорівнює `null`, `@NotBlank` — те саме, але не дозволяє також пустих стрічок `""`, для стрічок можна обмежити їхню довжину знизу й зверху: `@Size(min = 1, max = 100)`. 

Перед тим як розглянути відношення, подивимось на об’єкт `Post`:

```java
@Entity
@Table(name = "posts")
public class Post {
  
  @Id
  @GeneratedValue
  private Long id;
  
  @NotNull
  @ManyToOne
  private User author;
    
  @NotBlank
  @Size(min = 1, max = 2048)
  private String text;
    
  @NotNull
  private Date createdAt;

  public Post() {}

  get/set методи
}
```

Як видно для простих полів анотації дуже схожі. Тепер можна розглянути саму складну частину — відношення між об’єктами. *Hibernate* підтримує фактично всі види відношень: `@OneToOne`, `@OneToMany`, `@ManyToOne` та `@ManyToMany`. Розглянемо їх кожне детальніше:

`@OneToOne`
-----
Описує відношення 1 до 1-го. Таке відношення не часто застосовується, однак іноді логічно поділити табличку на кілька з меншою кількістю колонок. Наприклад припустимо, що необхідно зберігати багато персональних даних користувача (адресу, телефон, місто, дата народження), однак ця інформація буде використовуватись досить рідко лише на одній зі сторінок налаштування профіля. Тоді логічно винести ці данні в окрему табличку й не дістававати їх кожен раз. В коді це буде виглядати приблизно так:

```java
public class User {
    @OneToOne(mappedBy = "user")
    private Details status;
    ...
}

public class Details {
    @OneToOne
    private User user;
    ...
}
```

Параметр `mappedBy = "user"` в анотації користувача вказує, що для того щоб знайти відповідний об’єкт `Details` необхідно звернутись до таблички `details` де знайти поле `user`. Тобто фактично в БД це виглядатиме так: табличка `details` міститиме поле `user_id` (*Hibernate* автоматично додасть id до імені), по якому можна буде знайти відповідну стрічку з деталями користувача.

`@OneToMany` та `@ManyToOne`
-----




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


