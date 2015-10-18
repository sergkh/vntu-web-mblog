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
  private String password;
  
  @OneToMany(mappedBy = "author")
  private List<Post> posts = new ArrayList<>();
  
  @ManyToMany
  private Set<User> subscriptions = new HashSet<>();

  public User() {}

  get/set методи
}
```

Сам клас помічається анотацією `@Entity`, яка вказує, що клас повинен зберігатись у БД. *Hibernate* автоматично підбере імена табличок і полів за їх назвами (в даному випадку табличка `user`), однак їх можна вказати явно за допомогою додаткових анотацій `@Table("назва таблички")` та `@Column(name="назва колонки")`. За допомогою анотації `@Column` також можна вказати, що значення колонки повинне бути унікальним, як зроблено для `login` та `email`, при цьому в СУБД буде автоматично створено унікальний індекс по цим колонкам і буде викинуто виключення при спробі додати ще одного користувача з існуючим логіном чи поштою.

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

Як видно для простих полів анотації дуже схожі. Тепер можна розглянути саму складну частину — відношення між об’єктами. *Hibernate* підтримує всі види відношень: `@OneToOne`, `@OneToMany`, `@ManyToOne` та `@ManyToMany`. Розглянемо кожне з них детальніше:

`@OneToOne`
-----
Описує відношення 1 до 1-го. Таке відношення не часто застосовується, однак іноді логічно поділити табличку на кілька з меншою кількістю колонок. Наприклад припустимо, що необхідно зберігати багато персональних даних користувача (адресу, телефон, місто, дата народження), однак ця інформація буде використовуватись досить рідко лише на одній зі сторінок налаштування профіля. Тоді логічно винести ці данні в окрему табличку й не дістававати їх кожен раз. В коді це буде виглядати приблизно так:

```java
@Entity
public class User {
    @OneToOne(mappedBy = "user")
    private Details details;
    ...
}

@Entity
public class Details {
    @OneToOne
    private User user;
    ...
}
```

Параметр `mappedBy = "user"` в анотації користувача вказує, що для того щоб знайти відповідний об’єкт `Details` необхідно звернутись до таблички `details` де знайти поле `user`. Тобто фактично в БД це виглядатиме так: табличка `details` міститиме поле `user_id` (*Hibernate* автоматично додасть id до імені), по якому можна буде знайти відповідну стрічку з деталями користувача.Якщо ж перемістити `mappedBy` в клас `Details` й вказати `@OneToOne(mappedBy = "details")`, тоді навпаки в табличку користувача буде додано поле `details_id`.

`@OneToMany` та `@ManyToOne`
-----

Це взаємодоповнюючі анотації, які описують відношення один до багатьох й зворотнє багато до одного. Прикладом такого відношення можуть бути пости чи коментарі користувача: один користувач може мати багато постів/коментарів (OneToMany), а пост чи коментар може мати лише одного автора (ManyToOne). В коді це буде виглядати так само як і у попередньому випадку, однак тепер параметр `mappedBy` може бути лише на стороні користувача, так кожен пост буде мати параметр в БД `author_id`, який вказуватиме на автора. Зворотній ж варіант неможливий, оскільки користувач має багато постів.

```java
@Entity 
public class User {
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();
    ...
}

@Entity 
public class Post {
    @ManyToOne
    private User author;
    ...
}
```

Зазвичай для набору постів використовують тип список — `List`, або ж набір — `Set`. Різниця між ними така, що `Set` допоможе уникнути дуплікатів, тобто якщо записати пост в `Set` кілька разів, збережеться тільки одне з відношень.

`@ManyToMany`
------
Прикладом відношення багато до багатьох можуть бути групи користувачів: користувач може належати до більш ніж однієї групи й група може мати багато користувачів. Відношення багато до багатьох може бути описане тільки за допомогою додаткової таблички, яка буде містити ідентифікатори обох сторін відношення. Hibernate сам створить таку табличку з назвою `user_groups` або `group_users` в залежності від параметру `mappedBy`. Для того, щоб вказати назву таблички вручну існує анотація `@JoinTable`, однак тут вона розглядатись не буде. Приклад коду:

```java
@Entity 
public class User {
    @OneToMany()
    private Set<Group> groups = new HashSet<>();
    ...
}

@Entity 
public class Group {
    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();
    ...
}
```

В даному випадку створиться табличка `user_groups` з двома колонками `users_id`, `groups_id`.

З’єднання з СУБД
-----
Як було вказано вище за роботу з СУБД в Java відповідає інтерфейс JDBC. JDBC це набір класів в основному інтерфейсів в пакеті `java.sql`, що описують методи встановлення з’єднання до бази даних, отримання вибірок, оновлення бази та інші. А реалізацію цих інтерфейсів для конкретної СУБД зазвичай предоставляють розробники самої СУБД або ж сторонні розробники. Така реалізація називається JDBC драйвером й представляє собою `*.jar` файл з класами, що реалізують протокол конкретної СУБД.

При створенні проекту ми вказували, що проект буде працювати з СУБД, тому IDE автоматично скачала СУБД **H2** й налаштовано з’єднання до неї. **H2** це вбудовувана (embedded) база даних написана на Java, це означає, що сам драйвер вже містить саму СУБД й вам не доведеться встановлювати й налаштовувати окрему програму СУБД, а потім знаходити до неї JDBC-драйвер. При цьому за замовчуванням база налаштована працювати в режимі тільки в пам’яті (in-memory), це означає, що база даних буде створюватись при старті програми й знищуватись при зупинці.

Це дуже зручний варіант при розробці, коли як модель бази даних так і дані міняються дуже швидко й не тре піклуватись про зберігання чи оновлення старих даних. Однак якщо потім необхідно буде перенести додаток для роботи з більш серйозними СУБД це можна досить легко зробити. Для цього необхідно зробити 2 речі:

1. Додати драйвер СУБД до бібліотек проекту, що можна зробити клікнувши на файлі `pom.xml` й знайшовши ім’я в пошуку (зазвичай імена й поточні версії драйверу можна знайти на сайті СУБД):

![Вікно додавання залежності](https://github.com/sergkh/vntu-web-mblog/raw/lections/img/mysql-driver-add.png)

2. Тепер необхідно вказати драйвер, URL з’єднання з СУБД та логін з паролем в `application.properties` . URL має формат jdbc:СУБД:адреса-сервера/назва-бази (хоча може відрізнятись для різних СУБД). Наприклад для MySQL налаштування можуть мати вигляд:

```
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.1.56/univ
spring.datasource.username=dbuser
spring.datasource.password=dbpass
```

СУБД **H2** також має можливість зберігати дані на диску, для чого необхідно тільки змінити URL, наприклад в папці `~/labs`:

```
spring.datasource.url=jdbc:h2:~/labs"
```

Генерація таблиць бази даних
-----

Перед початком роботи з СУБД необхідно мати створену структуру табличок в які будуть заноситись дані. Однак існує можливість автоматично створювати структуру бази даних з описаної об’єктної моделі при старті додатку. За генерацію таблиць відповідає бібліотека *Hibernate*. При цьому налаштування за замовчуванням дозволяють отримати автоматичну генерацію табличок бази даних з описаних моделей автоматично не докладаючи жодних зусиль:

```
spring.jpa.hibernate.ddl-auto=create-drop
```

Що означає, що при старті додатку база буде знищуватись й створюватись заново. Очевидно, що цей варіант не підходить, якщо ми хочемо щоб дані залишались після перезапуску додатку, тому після створення бази його можна замінити на `validate`, який не робитиме змін в базі даних, однак не дасть запустити додаток, якщо ви зміните класи й забудете змінити схему бази даних. Інші варіанти: `create` - створити базу даних, якщо її немає, `update` – спробувати оновити таблички відповідно до змін в класах, та `none` – не робити нічого.

Об’єкти доступу до бази даних
------

Тепер можна замінити всі закодовані вручну списки з об’єктами моделі на отримання та запис їх з бази даних. Для цього необхідно описати об’єкти, які б дозволяли записати створені раніше моделі в базу чи прочитати їх.

Для початку визначимось з основними методами, які нам будуть необхідні для роботи: 

— для користувачів: необхідно буде його зберегти при реєстрації, отримати за логіном чи ідентифікатором та отримати список користувачів, які будуть пропонуватись користувачу для підписування на них. 

– для постів: отримання за ідентифікатором, збереження, отримання списку постів
для конкретного користувача (всіх на кого він підписаний та його).

Якщо описати це за допомогою Java-інтерфейсу в окремому пакеті `labs.repositories` це виглядатиме так:

```java

package labs.repositories;

import java.util.List;
import labs.models.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  User findByLogin(String login);
  List<User> findFirst10ByIdNotIn(List<Long> users);
}
```

Інтерфейс наслідується від інтерфейсу `CrudRepository<User, Long>` який містить основні методи — збереження об’єкту, отримання списку всіх об’єктів чи конкретного за ідентифікатором. При цьому параметрами інтерфейсу вказується клас моделі `User` та тип ідентифікатора `Long`. 

Метод `findByLogin` має знаходити користувача за логіном, `findFirst10ByIdNotIn` має отримувати список ідентифікаторів користувачів, на яких поточний користувач підписаний та повертати 10 користувачів, які до цього списку не входять.

Інтерфейс доступу для постів:

```java
package labs.repositories;

import java.util.List;
import java.util.Set;

import labs.models.Post;
import labs.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
    public Page<Post> findByAuthorInOrderByCreatedAtDesc(Set<User> users, Pageable pageable);
}

```

Метод `findByAuthorInOrderByCreatedAtDesc` має повертати список постів відсортований по даті по спаданню, автор яких знаходиться в переданому списку користувачів, при цьому має бути можливість розбивати список постів по сторінках, щоб відображати не більше, наприклад, 10 постів на сторінку. Об’єкт `Pageable` вказуватиме кількість об’єктів на сторінці та номер сторінки, який необхідно повернути. Повертатиме метод об’єкт `Page`, який містить як список постів для поточної сторінки так само як і загальну кількість сторінок.

Власне це й усе, що необхідно було зробити — реалізацію цього інтерфейсу буде автоматично згенеровано фреймворком *Spring*. Яким ж чином фреймворк знає, яким чином працює метод? Це вказано в назві методу та його аргументах, якщо метод називається `findByName` це означає що необхідно знайти об’єкт чи список об’єктів за значенням атрибуту `name`, метод має мати перший аргумент, який вказуватиме ім’я й повинен мати такий самий тип як і `name` у об’єкта.

Зазвичай класи репозиторії відображають тільки роботу з СУБД — в більшості випадків один метод репозиторію це один SQL запит, а поєднуються декілька запитів в одну дію в сервісах, де використовуючи різні репозиторії та різні їх методи формується логіка роботи додатку. 

Для того щоб отримати доступ до згенерованого репозиторію `UserRepository` необхідно в сервісі створити змінну з відповідним типом й поставити анотацію `@Autowired`. Самі ж класи сервісів необхідно помітити анотацією `@Service`, тоді їх аналогічним чином можна буде вставляти в контролери. Сервіс користувачів з реалізацією реєстрації, отримання списку рекомендованих користувачів та підписки на них матиме вигляд:

```java
package labs.services;

import java.util.*;
import javax.annotation.PostConstruct;
import labs.models.User;
import labs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
  
  @Autowired
  private UserRepository usersRepo;
  
  public void register(String login, String email, String pass) {
    String passHash = new BCryptPasswordEncoder().encode(pass);
    User u = new User(login, email.toLowerCase(), passHash);
    u.getSubscriptions().add(u); // підпишемо користувача на самого себе
    usersRepo.save(u);
  }

  public void subscribe(String login) {
    User currentUser = usersRepo.findOne(1L);
    User u = usersRepo.findByLogin(login);
    currentUser.getSubscriptions().add(u);
  }

  public List<User> getSubscribeRecommendations() {
    User currentUser = usersRepo.findOne(1L);
    // перетворює список користувачів на список їх ідентифікаторів
    List<Long> ignoreIds = new ArrayList<>();
    for(User u : currentUser.getSubscriptions()) {
      ignoreIds.add(u.getId());
    }
    return usersRepo.findFirst10ByIdNotIn(ignoreIds);
  }  
}
```

При реєстрації користувача перед збереженням паролю користувача гарним тоном є перетворення його односторонньою функцією хешування й зберігання хешу замість паролю — при логіні можна перевіряти хеші введеного та паролю з бази, а це значно підвищить безпеку вашого додатку. Якщо хтось отримає несанкціонований доступ до бази даних й скопіює паролі паролі користувачів їх не можливо буде використати для логіну в реальній системі. Крім того багато користувачів мають однакові паролі для різних сайтів, тому зберігання паролю у відкритому вигляді також наражає на небезпеку й сторонні сайти.

Поки система авторизації не реалізована й ми не знаємо під яким користувачем зараз виконується запит в якості поточного користувача можна взяти першого зареєстрованого користувача, у нього буде `id = 1`. А для того щоб впевнитись що такий користувач точно буде можна створити його при старті сервісу. Це не можна зробити в конструкторі, оскільки *Spring* спочатку створює об’єкт `UsersService`, а тільки потім ініціює всі `@Autowired` змінні, але існує спеціальна анотація для методу, яка вказує, що його необхідно викликати, коли ініціалізація завершиться, створимо в ній нового користувача:

```java
public class UsersService {
  ... 
  @PostConstruct
  public void createAdminUser() {
    register("admin", "admin@mail.com", "qwerty");
  }
  ...
}
```

Сервіс постів також буде тимчасово опиратись на першого створеного користувача:

```java
package labs.services;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import labs.models.*;
import labs.repositories.*;

@Service
public class PostsService {
  @Autowired
  private UserRepository usersRepo;
  @Autowired
  private PostRepository postsRepo;

  public Page<Post> getPosts(int page, int pageSize) {
    User currentUser = usersRepo.findOne(1L);
    return postsRepo.findByAuthorInOrderByCreatedAtDesc(
        currentUser.getSubscriptions(),
        new PageRequest(page-1, pageSize) // spring рахує сторінки з нуля
    );
  }

  public void addPost(String text) {
    User currentUser = usersRepo.findOne(1L);
    postsRepo.save(new Post(currentUser, text, new Date()));
  }
}
```

Для того щоб використати сервіси в контроллері їх також можна додати з анотацією `@Autowired`. Додатково це дозволяє мати лише один об’єкт сервісів навіть якщо вони вставляються в декілька контролерів.

```java
@Controller
public class IndexController {
  
  @Autowired
  private PostsService postsService;
  
  @Autowired
  private UsersService usersService;
  
  @RequestMapping("/")
  public String index(Model model) { return "index"; }
  
  @RequestMapping("/home")
  public String home(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
    Page<Post> postsPage = postsService.getPosts(page, 5);
    model.addAttribute("posts", postsPage.getContent());
    model.addAttribute("users", usersService.getSubscribeRecommendations());
    model.addAttribute("pagesCount", postsPage.getTotalPages());
    model.addAttribute("currentPage", page);
    return "home";
  }
  
  @RequestMapping(value = "/post", method = RequestMethod.POST)
  public String createPost(@RequestParam("text") String postText) {
    postsService.addPost(postText);
    return "redirect:home";
  }
  
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String register(@RequestParam("login") String login, 
      @RequestParam("email") String email, 
      @RequestParam("pass") String pass) { 
    usersService.register(login, email, pass);
    return "redirect:home";
  }
  
  @RequestMapping(value="/subscribe", method = RequestMethod.POST)
  public String subscribe(@RequestParam("login") String login) {
    usersService.subscribe(login);
    return "redirect:home";
  }
  
}
```

Для відображення сторінок можна скористатись кодом:

```html
        <ul class="pagination" th:if="${pagesCount} > 1">
        <li th:each="page: ${#numbers.sequence(1, pagesCount)}" th:class="${page} == ${currentPage} ? 'active'">
          <a href="#" th:href="@{/home(page=${page})}">
            <span th:text="${page}"></span>
          </a>
        </li>
    </ul>
```

Тут створюється звичайний для *Java* цикл `for` від 1 до `pagesCount` й змінюється клас сторінки на `active`, якщо вона зараз є активною. Список користувачів, на яких можна підписатись матиме вигляд:

```html
      <div class="row">
        <ul class="list-inline">
        <li class="text-center" th:each="user: ${users}">
          <span th:text="${user.login}">Користувач</span><br/>
          <form action="/subscribe" method="POST">
            <input type="hidden" name="login" th:value="${user.login}"/>
              <button class="btn btn-info btn-sm">Підписатись</button>
            </form>
        </li>
      </ul>
      </div>
```

Робота з транзакціями
--------------

Для того, щоб об’єднати певний набір дій у транзакцію, необхідно лише скористатись анотацією `@Transactional`. Дана анотація ставиться перед визначенням методу, зазвичай це метод сервісу, рідше контроллера. 

В наведеному вище коді кожен з методів обох сервісів має виконуватись в транзакції. Наприклад для користувачів:

```java
@Service
public class UsersService {
  
  @Autowired
  private UserRepository usersRepo;

  @Transactional  
  @PostConstruct
  public void createAdminUser() { ... }

  @Transactional
  public void register(String login, String email, String pass) { ... }
  
  @Transactional
  public void subscribe(String login) { ... }

  @Transactional
  public List<User> getSubscribeRecommendations() { ... }  
}
```

Можливо також вказати, що транзакція буде тільки читати дані, а не буде нічого записувати в БД за допомогою параметру `@Transactional(readOnly=true)`, це дозволить *Spring* дещо зменшити час на створення транзакції. 

При цьому варто зазначити, що транзакції реалізуються наступним чином: насправді в контролер через `@Autowired` вставляється не сам сервіс `UsersService`, а згенерований проксі клас, який відкриває транзакцію перед викликом кожного методу сервісу й закриває після його виконання. Якщо в методі виникає виключення (Exception), то транзакція автоматично відкатується.

![Робота проксі класу](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/tx-proxy.png)

Очевидно при такому підході транзакції будуть створюватись тільки при виклику методів сервісу з контроллера, а при виклику власних методів в сервісі транзакція створюватись не буде, саме тому метод `createAdminUser()` теж має анотацію `@Transactional` навіть якщо `register` теж позначено як транзакційний.

Індекси
-----------

Для того, щоб стоврити індекси на певних колонках таблиць необхідно вказати назви цих колонок в анотації `@Table` перед назвою класу. Наприклад, в нашому випадку користувачі досить часто будуть діставатись по логіну (при вході на сайт), тому для нього варто створити індекс. Поле `email` не буде так часто використовуватись, але воно як і `login` має бути унікальним, тому для кожного з них необхідно створити по унікальному індексу, які задаються наступним чином:

```java
@Entity
@Table(indexes = {
  @Index(columnList="login", unique = true), 
  @Index(columnList="email", unique = true)
})
public class User { ... }
```

Робочий код для даного етапу розробки можна знайти за [посиланням](https://github.com/sergkh/vntu-web-mblog/tree/1b0d04aaca04a13c88f84c824e3ccf5f66c3b055).

