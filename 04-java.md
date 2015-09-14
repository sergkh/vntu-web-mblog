Опис платформи
-----

*Java* (Джава) — об'єктно-орієнтована мова програмування з *C*-подібним синтаксисом. Мову й платформу *Java* вперше опубліковано у 1995 році компанією Sun Microsystems (наразі придбана компанією Oracle). 

В основу платформи покладено принцип: код написаний і скомпільований один раз, запускається всюди. Даний принцип реалізовується за рахунок того, що Java програми компілюються не у проміжний код — байткод, який інтерпретується віртуальною машиною (*VM* – Virtual Machine) написаної для конкретної платформи.

Синтаксис *Java* дуже близький до синтаксису мови *C++*, однак в деяких речах написання коду на *Java* відрізняється. Однією з значний відмінностей є те, що в платформі *Java* широко використовується автоматичне керування пам’яттю, яке називається збирач сміття (*GC* – Garbage Collector). Такий підхід дозволяє не піклуватись про видалення об’єктів, які створюються під час роботи програми — збирач сміття видалить їх автоматично, коли виявить, що вони більше не використовуються. На противагу в *C++* необхідно завжди викликати `delete` для об’єктів створених операцією `new`.

Ще однією відмінністю від *С*/*C++* є те, що код та дані обов’язково повинні міститись в класах, описати функцію поза класом не можна. Тому головна функція `main` повинна також знаходитись в певному класі й виглядатиме на *Java* так:

```java
    package lections;

    public class Test {
        public void static main(String[] args) {
            System.out.println("Test");
        }
    }
```

Навіть стандартна функція виведення `println` знаходиться в статичному полі `out` (типу `PrintWriter`) класу `System`.

Класи можливо опціонально групувати за призначенням у пакети (*packages*). В прикладі клас знаходиться в пакеті `lections`. Компілятор *Java* вимагає щоб клас `Test` знаходився в однойменному файлі `Test.java` (регістр також має значення) й щоб пакети відображались на структуру папок, тобто шлях до файлу має бути: `lections/Test.java`

Налаштування засобів розробки та створення проекту
----

Для початку роботи з *Java* необхідно встановити інструменти розробника *Java Development Kit* – *JDK* та середовище розробки. Остання версія *JDK* на момент написання — 8-ма й може бути знайдена на сайті **Oracle**, однак усі приклади повинні працювати також й на новіших версіях компілятора та віртуальної машини.

В процесі розробки буде широко використовуватись популярний *Java* фреймворк – *Spring*, який має власне середовище розробки — *Spring Tool Suite — STS*. Дане середовище базується на IDE (*Eclipse*)[http://eclipse.org] з набором плагінів й одразу налаштовано для роботи, як з *Spring* фреймворком, так і зі звичайними *Java* проектами. Завантажити *STS* можна за посиланням (https://spring.io/tools/sts/)[https://spring.io/tools/sts/]. Встановлення середовище не потребує — достатньо просто його розпакувати в будь-яку папку.

В процесі вивченя буде створено додаток для мікроблогінгу, по аналогії з (*Twitter*)[http://twitter.com], який дозволятиме кожному користувачу зареєструватись та залишити повідомлення, яке обмежене в розмірі. Користувач також матиме можливість підписатись на повідомлення інших користувачів.

Для створення нового проекту використаємо майстер «Spring Starter Project», що знаходиться в меню *File → New → Spring Starter Project*: 

![Меню створення проекту](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/sts-create.png)

В майстрі створення проекту можна обрати довільне ім’я проекту, групи й артефакту. Необхідно змінити тип пакування на більш підходящий для веб проектів — War (Web ARchive). Обов’язковими залежностями для проекту є: Web, Thymeleaf, який буде використовуватись для побудови HTML сторінок, H2 та JSA – для організації роботи з базою даних й Security — модуль який забезпечить аутентифікацію та обмеження прав доступу користувачів.

![Налаштування проекту](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/sts-settings.png)

В результаті буде отримано згенерований проект, який матиме вигляд:

![Створений проект](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/sts-newly-created.png)

Згенерований файл LabsApplication.java містить однойменний головний клас LabsApplication з методом main, який використовуватиметься для запуску додатку.Однак перед запуском серверу необхідно створити сторінку, яку можна буде відобразити користувачу. В папці `src/main/resources/templates` створимо файл `index.html` з довільним HTML документом (наприклад з першої лекції). 

Для того, щоб відобразити створений файл у браузері, необхідно також створити новий клас — контроллер, який буде вказувати для якого URL буде відображатись вказаний HTML-документ й надалі буде також використовуватись для отримання даних з СУБД. Для цього можна скористатись діалогом створення нового класу, що знаходиться у головному меню *File → New → Class*:

![Майстер створення нового класу](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/sts-new-class.png)

Зміст класу наведено нижче. В ньому створюється публічний клас `IndexController` помічений аннотацією `@Controller`, яка означає що він призначений для обробки запитів браузера. В класі є всього один публічний метод index() помічений анотацією `@RequestMapping("/home")`, яка вказує що даний метод буде викликатись при переході браузера на URL `http://localhost:8080/home`. Метод повертає стрічку `"index"` — назву шаблону, з папки `templates`, який необхідно відобразити користувачу (*Spring* автоматично додасть розширення .html до назви). Ні назва класу, ні назва методу, насправді ніякої ролі (окрім хіба що інформативної) не відіграють й можуть бути довільними — тільки анотації вказують бібліотеці, що даний клас оброблятиме запити користувача й які *URL* будуть оброблятись.

```java
    package labs;

    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    public class IndexController {
        
        @RequestMapping("/home")
        public String index() {
            return "index";
        }
    }
```

Тепер можна спробувати запустити створений проект, для чого необхідно викликати контекстне меню на файлі `LabsApplication.java` й обрати пункт *Run As → Spring Boot App* або *Run As → Java Application*:

![Меню запуску](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/sts-first-run.png)

Тепер можна перейти в браузері на сторінку `http://localhost:8080/home`:

![Результат роботи з вікном логіну](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/sts-first-run-result.png)

Однак браузер видасть діалог логіну — це відбувається тому, що при створенні проекту було обрано *Security* як одну із залежностей. *Spring* при наявності *Security* автоматично заборонить вхід всім незареєстрованим користувачам. Для того, щоб тимчасово вимкнути цю функціональність необхідно додати в `application.properties` наступне налаштування:

    security.basic.enabled=false

Тепер можна запустити проект ще раз й побачити кінцевий результат:

![Результат роботи](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/sts-final-result.png)

>  Якщо при запуску виникає помилка, яка почиається з виразу: 
>  java.net.BindException: Address already in use 
>  — це означає, що порт 8080, на якому за замовчуванням запускається сервер вже зайнятий. Це може бути результатом попереднього невдалого запуску або він використовується сторонньою програмою. В останньому випадку можна змінити порт в файлі конфігурації:
>  server.port=9090

Також, щоб пришвидшити розробку можна додати наступні опції в `application.conf`:

    spring.thymeleaf.mode=HTML5
    spring.thymeleaf.cache=false

Перша опція змушує *Spring* застосовувати більш поблажливішу до помилок у HTML-документах перевірку, однак фреймворк всеодно буде видавати помилку при проблемах з *HTML*. Друга опція вимкне кеш для HTML-документів, що дозволить бачити зміни в них не перезавантажуючи додаток, а просто перезавантаживши сторінку в браузері.

В результаті створено готовий додаток, який, поки що, тільки відображає звичайну статичну сторінку. Однак, насправді, цей простий проект вже побудовано на базі потужного веб-серверу *Apache Tomcat*, який автоматично запускається при старті додатку. Також проект має всі необхідні бібліотеки для побудови динамічного сайту, організації роботи з базою даних, реалізації логіну, авторизації та прав доступу.

> При запуску продукту для широкого загалу проект зазвичай збирають у так званий `war`-файл (Web Archive – веб архів який є, насправді, звичайним `zip`—архівом з певною структурою папок й містить скомпільовані файли й всі необхідні ресурси для додатку). Даний файл потім завантажують на зовнішній *Tomcat* сервер або на хмарну платформу, наприклад *Jelastic* або *Google App Engine*. А замість вбудованої бази даних використовують зовнішню. 

Розглянемо структуру створеного додатку:

![Структура папок додатку](https://raw.githubusercontent.com/sergkh/vntu-web-mblog/lections/img/directory-structure.png)

`src/main/java`
: в даній директорії містяться весь основний код програми на мові *Java*. Всі створені `*.java` файли містяться в пакеті `labs`.

`src/main/resources`
: дана директорія містить файл `application.properties` з основними налаштуваннями додатку, папку `static/` для зберігання статичних ресурсів додатку, а саме *CSS* та *Javascript* файли, а також папку `templates/` — де зберігатимуться *HTML*-шаблони, на основі яких будуть будуватись сторінки веб-додатку.

`src/test/java`
: директорія розрахована на так звані Unit-тести — невеликі підпрограми, які перевіряють правильність роботи окремих компонентів системи та взаємодії цих компонентів. Зазвичай для написання тестів використовують популярні бібліотеки такі, як *JUnit*, *TestNG* та інші. Використовуватись не буде.

`pom.xml`
: даний файл є конфігурацією консольної утиліти *Maven*, яка може використовуватись для автоматизованої компіляції, тестування та зборки проекту. Результати роботи *Maven* будуть поміщатись в діректорію `target/`.

Переглянути код проекту можна за [посиланням](https://github.com/sergkh/vntu-web-mblog/tree/81ef319d39f1cb62258720d62097c20b2f87725a).

Наслідування
-----

Наслідування дозволяє класу успадкувати методи та властивості батьківського класу. Для того, щоб вказати що певний клас наслідується від іншого в *Java* використовується ключове слово `extends`. Особливістю мови є те, що для наслідування можна вказати лише один клас (на відміну від *C++*, де підтримується множинне наслідування). Наприклад, у новоствореному проекті клас `ServletInitializer`, який відповідає за налаштування проекту наслідується від класу `SpringBootServletInitializer`:

```java
    public class ServletInitializer extends SpringBootServletInitializer {
        ...
    }
```

Насправді навіть якщо не вказувати батьківській клас всі класи наслідуються від типу `Object`. Це відповідає принципам ООП, що всі дані є об’єктами.

В похідний клас можна додавати нові методи чи властивості або перевизначати старі. Всі функції в *Java* є віртуальними, тому не потрібно вирішувати чи робити функцію віртуальною чи ні, як власне і немає необхідності у ключовому слові `virtual`.

Основні типи
-----

Розглянемо основні прості типи змінних, які надає мова *Java*:

Назва   | Розмір   | Допустимі значення                         | Клас
--------|----------|--------------------------------------------|-----------
boolean |  —       | true/false                                 | Boolean
byte    |  8 біт   | -128 до 127                                | Byte
char    |  16 біт  | 0 — 65535 (\u0000' - \uffff')              | Character
short   |  16 біт  | -32768 — 32767                             | Short
int		|  32 біта | -2147483648 — 2147483647                   | Integer
long	|  64 біта | -9223372036854775808 — 9223372036854775807 | Long
float	|  32 біта | 1,17549435e-38 — 3,4028235e+38             | Float
double	|  64 біта | 4,9e-324 — 1,7976931348623157e+308         | Double

В табличці є додаткова колонка з класами, що відповідають кожному з примітивних типів. Насправді у *Java* примітивні типи не є об’єктами й не можуть бути приведені до типу `Object`. Тому було для кожного примітивного типу було створено аналоги у вигляді класів, при чому компілятор може автоматично перетворювати примітивний тип в клас і навпаки, що називається boxing та unboxing відповідно:

```java
    int k = 10;
    Integer objK = k;
    Long objB = 5;
    long b = objB;
```

Перевагою простих типів є те, що вони займають значно менше пам’яті. Перевагою класів є, як зазначено можливість приведення їх до базового типу Object, де необхідно або ж можливість вказати `null` замість значення, щоб відзначити, що значення змінній ще не присвоєно. Наприклад:

```java
    Object n = 10; // буде неявно перетворено до Integer
    Long notInitialized = null;
```

Також функції перетворення стрічки в число та навпаки зберігаються в зазначених класах:

```java
    int inputNumber = Integer.parseInt("1000"); // 1000
    boolean check = Boolean.parseBoolean("false"); // false
    long inputBigNumber = Long.parseLong("-1"); // -1L

    String output = Integer.toString(10000);
```

Модифікатори доступу
-----

Колекції: List та Map
-----

Виключення
-----

Робота зі стрічками
-----

Приклад JavaBean
-----
