Servlets
---------

Java Servlet API — API описаний в стандартах J2EE для створення динамічного контенту веб-додатків. Сервлети це аналог технологій PHP, CGI і ASP.NET для Java.

Інтерфейси та класи для написання сервлетів описані в пакетах javax.servlet і javax.servlet.http. Всі сервлети повинні реалізовувати інтерфейс Servlet, який визначає методи життєвого циклу сервлету. Але для реалізації обробки HTTP ми будемо наслідуватись від абстрактного класу HttpServlet, який забезпечує методи обробки HTTP запитів, такі як doGet і doPost.

    package ua.vntu.test;

    import java.io.*;
    import javax.servlet.*;
    import javax.servlet.annotation.*;
    import javax.servlet.http.*;

    @WebServlet("/hello")
    public class HelloWorld extends HttpServlet {

        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            PrintWriter out = response.getWriter();
            out.println("Hello World");
        }
    }

Отже основними методами, які необхідно перевизначити є:

`void doGet(HttpServletRequest req, HttpServletResponse resp)`
: якщо сервлет підтримує HTTP `GET` запити.  

`void doPost(HttpServletRequest req, HttpServletResponse resp)`
: для запитів HTTP `POST`

`void doPut(HttpServletRequest req, HttpServletResponse resp)` 
: для HTTP `PUT` запитів

`void doDelete(HttpServletRequest req, HttpServletResponse resp)` 
: для HTTP запитів `DELETE`  

`void init(ServletConfig config)`
: викликається при старті - може використовуватись для налаштування сервлета та створення ресурсів.  
`void destroy()`
: викликається при знищенні сервлета, може використовуватись для знищення ресурсів. 

В більшості сервлет контейнерів URL складається з адреси серверу, директорії додатку (оскільки їх може бути декілька на одному сервері) та власне частині URL, яка вказує який сервлет буде обробляти даний запит. 

Для вказання URL, який буде обробляти даний сервлет необхідно створювати файл дескриптор додатку - `web.xml` й там вказувати необхідні налаштування, однак починаючи з версії 3.0 це можна зробити простіше - лише вказавши анотацію `@WebServlet("/hello")`, яка означає, що сервлет прийматиме запити за URL `http://servername.com/app_name/hello`. 

Об’єкт `HttpServletRequest` має наступні методи, які ми будемо використовувати:

`String getContextPath()`
: повертає частину URI, яка визначає контекст запиту.

`String getHeader(String name)`
: повертає заголовок пакету з назвою name. Не чутливий до регістру.

`String getMethod()`
: повертає стрічку - метод запиту, один з `"GET"`, `"POST"`, `"PUT"`, `"DELETE"`, `"HEAD"`, `"TRACE"` або `"OPTION"`.

`String getParameter(String name)`
: повертає значення параметру скрипта з заданим ім’ям.

`String getPathInfo()`
: повертає частину URI без параметрів, яка слідує за шляхом сервлету.

`String getRemoteAddr()`
: поветає адресу з якої надходив запит.

Методи об’єкту `HttpServletResponse`:

`void addHeader(String name, String value)`
: додає у відповідь заголовок з іменем name і значенням value.

`void sendError(int sc, String msg)`
: відправляє у відповідь код HTTP помилки (наприклад 500 - внутрішня помилка сервера) та додає до відповіді повідомлення яке буде відображено в браузері користувача.

`void sendRedirect(String location)`
: відправляє на браузер відповідь, що необхідно запросити іншу сторінку з вказаним URI.

`PrintWriter getWriter()`
: отримати об’єкт типу PrintWriter для запису відповіді.

`void setCharacterEncoding(String charset)`
: встановити кодування відповіді.

`void setContentType(String type)`
: встановити MIME тип відповіді.

Cтруктура директорій веб додатку
------
Типова структура веб додатку в *Eclipse*:

    /src                - java файли 
    /WebContent         
    +- WEB-INF
        +- web.xml      - дескриптор додатку
        +- lib          - додаткові бібліотеки
    +- index.jsp        - jsp
    +- static/          - директорія для статичних ресурсів
        +- img/         - зображення
        +- js/          - javascript-файли
        +- css/         - файли стилів

Java-сервери зазвичай працюють з файлами формату `*.war` (Web ARchive – веб архів). Це звичайний *zip* файл, який містить вміст папки `WebContent` й всі скомпільовані Java файли та додаткові описові файли програми. При збиранні додатку й експорті в `war` *IDE* автоматично розмістить всі файли згідно вимог й додасть необхідні описові файли. Також для збирання проектів часто застосовуються допоміжні утиліти, такі як: *ant*, *maven*, *sbt*, *gradle* та інші. 

При розміщенні файлу, наприклад, `app.war` в папку `webapps` сервера він автоматично розпаковується й запускається сервером за шляхом http://localhost:8080/app/. Якщо сервер не запущений, то розгортання відбудеться при запуску.

2.4 Схема роботи сервера, проксі. TODO

JSP
--------

`JSP` (Java Server Pages) — технологія, що дозволяє веб-розробникам динамічно генерувати `HTML`, `XML` та інші веб-сторінки. JSP дозволяє додати динамічну складову прямо в HTML, добто JSP складається зі звичайного коду HTML, а динамічна частина коду додається за допомогою спеціальних тегів.

Рекомендоване розширення `JSP` файлу - `.jsp`. Сторінки може бути скомпонованою з декількох файлів-фрагментів й для таких фрагментів рекомендується використовувати розширення `.jspf`.

Роглянемо приклад `JSP` сторінки:

    <%@ page contentType="text/html; charset=UTF-8" %>
    <%@ page import="java.util.Date" %>
    <%-- комментар до сторінки --%>
    <html>
      <head><title>Localized Dates</title></head>
      <body>
         Час на сервері: <%=new Date()%>
      </body>
    </html>

Директива `<%@page contentType="..." %>` встановлює `Content-Type`, який буде повертати дана сторінка, для html підходить text/html, також додатково можна встановити кодування сторінки: `charset=UTF-8` 

Директива `<%@page import="..." %>` імпортує клас `Date` з пакету `java.util` - клас який ми використовуємо для видедення поточної дати та часу. Можливі атрибути директиви page:

    import="пакет.class"
    contentType="MIME-Type"
    isThreadSafe="true|false
    session="true|false"
    buffer="размерkb|none"
    autoflush="true|false"
    extends="пакет.class"
    info="сообщение"
    errorPage="url"
    isErrorPage="true|false"
    language="java"

Тег `<%= код %>` виконає код, розміщений в дужках і результат виконання виведе на місці тегу. Отже в наведеному прикладі буде створено новий об’єкт `Date` (який в конструкторі автоматично встановить дату та час на поточні) й виведено його у вихідний потік. При цьому об’єкт `Date` автоматично перетвориться до стрічки (фактично неявно буде викликано метод `toString`). Тобто приклад еквівалентний:

    out.println(new Date());

або

    out.println(new Date().toString());

Тег `<% %>` - називається скриплетом, дозволяє вміщувати java код в сторінку, наприклад:

    <% for(int i = 0; i < 100; i++) { %>
        <p>Я завжди буду закривати парні теги.</p>
    <% } %>

Даний код виведе 100 параграфів тексту.

`JSP` сторінка скомпілюється в сервлет, в якому весь html код буде розміщено в методі doGet та обрамлено out.println(). Є можливість розмістити код в класі сервлету, поза методом doGet, для чого використовується тег <%! код %>.

`<jsp:include page="відносний URL" flush="true"/>` - вставляє вказаний файл (зазвичай jsp або html) на місці даного тегу.

`<jsp:forward page="відносний URL"/>` - передає запит вказаній сторінці

Для спрощення роботи в JSP є ряд наперед визначених змінних, які можна використовувати:
    
`HttpServletRequest request`
: об’єкт запиту

`HttpServletResponse response`
: об’єкт відповіді.

`HttpSession session`
: сессія користувача

`PrintWriter out`
: буферизований потік виведення, використовується для відправки даних клієнту. 

`ServletContext application`
: контекст додатку

`ServletConfig config`
: об’єкт з налаштуваннями сервлету.

Дані змінні можна вільно використовувати в скриплетах, наприклад:

    Ім’я вашого хоста: <%= request.getRemoteHost() %>

Або:

    <% 
        String queryData = request.getQueryString();
        out.println("Дані запиту: " + queryData); 
    %>

В JSP можна також використовувати вбудовану скриптову мову виразів (EL - Expression Language), яка дозволяє отримати доступ до Java компонентів з JSP.  Наприклад, щоб отримати доступ до об`єкта необхідно додати вираз `${name}`, для доступу до змінної цього об’єкту `${name.foo.bar}`.

В мові виразів можна скористатись одним з наперед визначених об’єктів:

`pageContext`
: Контекст сторінки, який надає доступ наступних змінних:

    `servletContext`
    : Контекст сервлету, з якого можна отримати налаштування сервлету
    
    `session`
    : сесія користувача
    
    `request`
    : Об’єкт запиту
    
    `response`
    : Об’єкт відповіді

Додатково є ряд об’єктів для швидкого доступу:

`param`
: надає доступ до параметрів запиту, наприклад `${param.username}` поверне значення параметру `username`.

`paramValues`
: аналогічний об’єкт, але повертає масив значень.

`header`
: надає доступ до заголовків

`headerValues`
: аналогічний об’єкт, але повертає масив значень для однієї назви заголовка.

`cookie`
: містить значення всіх cookies.

`initParam`
: параметри ініціалізації скрипта.

Останніми розглянемо об’єкти, які відносяться до різних областей видимості (scopes): 

`pageScope`
: містить змінні, які існують тільки для данної JSP сторінки.

`requestScope`
: містить змінні, які існують тільки під час обробки конктретного запиту, має більшу область видимості ніж pageScope, 
оскільки запит можуть обробляти декілька сторінок.

`sessionScope`
: містить змінні з сессії користувача.

`applicationScope`
: містить змінні прив’язані до додатку, існують весь час, поки працює додаток.

Приклад використання:

    <% pageContext.setAttribute("foo", "bar"); %>
 
 Далі можна доступатись до поля:

    From page scope: ${pageScope.foo} <br/>
    Or Simply: ${foo} <br/>

MVC
----
MVC (Model-View-Controller - Модель-Представлення-Контроллер) - підхід до проектування додатку, в якому відокремлюються 3 сутності дані програми - модель, код для відображення цих даних - представлення та контроллер - код, який відповідає за оновлення моделі відповідно до змін зроблених користувачем. Такий підхід дозволяє вільно модифікувати кожен з компонентів незалежно з мінімальним впливом на інші та значно спростити кожен з компонентів.

Наприклад, нам необхідно відобразити список користувачів системи. Тоді моделлю будуть наші дані, які нам необхідно відобразити тобто список користувачів об’єктів `User`. Представленням виступатиме JSP сторінка, яка буде цей список відображати користувачу. А, відповідно, контроллером - сервлети який отримуватиме список користувачів з бази даних чи іншого джерела та передавати його представленню. Отже, нехай об’єкти користувачів матимуть вигляд:

    public class User {
        private String login;
        private String email;

        public User(String login, String email) {
            this.login = login;
            this.email = email;
        }

        public String getLogin(){
            return login; 
        }
        
        public String getEmail(){
            return email; 
        }
    }

Список користувачів для простоти поки буде отримуватись зі звичайного списку. В реальній системі - список, швидше за все, буде отримуватись з бази. Зазвичай у веб додатках додатково виділяють шар доступу до бази даних (DAO - Data Access Object) та шар сервісів, тобто класів які безпосередньо містять саму логіку додатку. В нашому випадку шар DAO можна опустити, необхідним буде тільки сервіс: 

    public class UsersService {
        private List<User> users = new ArrayList<User>();

        public UsersService() {
            users.addAll(Arrays.asList(
                new User("test", "test@mail.com"),
                new User("admin", "admin@mail.com"),
                new User("user", "user@mail.com")
            ));
        }

        public void register(User u) {
            users.add(u);
        }

        public List<User> getUsers() {
            return users;
        }
    }

Основна функція контроллера - отримати користувачів та передати їх на відображення:
    
    ...
    private UsersService srvc = new UsersService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("model", srvc.getUsers());

        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/view.jsp");
        view.forward(req, resp);
    }
    ...

Для передачі даних у JSP використали метод `req.setAttribute()` який дозволяє прив’язати будь-які дані до запиту (ці дані не передаються на сторону клієнту). Для передачі керування на JSP можна скористатись методом `forward` який передасть управління на JSP сторінку, при чому на відміну від `redirect` все відбуватиметься всередині серверу й користувачу повератиметься тільки дані сторінки. 

Код відображення даних - `view.jsp`:
    
    <%@ page import="java.util.List" %>
    <%
        List<User> users = (List<User>) request.getAttribute("model");
        for(User u: users) {
    %>
        ${u.login}
    <%    
        }
    %>


JSTL (JavaServer Pages Standard Tag Library) - у перекладі з англійської «стандартна бібліотека тегів JSP». Вона розширює специфікацію JSP, додаючи бібліотеку JSP тегів для загальних потреб, таких як розбір XML даних, умовна обробка, створення циклів і підтримка інтернаціоналізації. JSTL - кінцевий результат JSR 52, розробленого в рамках JCP (Процесу Java співтовариства). JSTL є альтернативою прямих вставок Java коду - скріплетів. 

Підхід з використанням стандартизованого набору тегів кращий, оскільки в цьому разі код легше підтримувати і простіше відокремлювати бізнес-логіку від логіки відображення. Але найголовнішим фактором зараз є той, що зазвичай верстають сайт люди, які не розуміють й не повинні розуміти java код, тому для них простіше якщо вся логіка відображення буде описуватись у вигляді тегів.

Скачати бібліотеку JSTL можна за посиланням: http://tomcat.apache.org/taglibs/ після скачування 2 jar файли (jstl-1.2.1.jar та jstl-api-1.2.1.jar) необхідно покласти в папку WEB-INF/lib.

JSTL дозволяє будувати основні структурні блоки, такі як: ітерації та умови, має теги для роботи з XML-документами, інтернаціоналізації, теги для роботи безпосередньо з SQL, а також забезпечує основу для інтеграції існуючих користувацьких тегів з тегами JSTL.

JSTL теги можна класифікувати відповідно до їх функцій:

1. Основні теги
2. Теги форматування
3. SQL теги
4. XML-теги
5. функції JSTL

Для підключення основних тегів JSTL на початку документу треба вказати:

    <%@taglib prefix="c" uri="http://java.sun.com/jsp/core" %>

Розглянемо приклади найчастіше використовуваних тегів, що містяться у бібліотеці.

**Виведення даних з екрануванням симвлолів**

    <c:out value="${var}"/>
    
дозволяє екранувати всі символи змінної що передається у атрибут `value`, на відміну від просто EL виразу `${var}` - який виведе всі символи як є. 

Таким способом краще за все виводити всі строкові дані, які яким-небудь чином були отримані від користувача або зовнішньої системи (наприклад, стороннього сайту), оскільки користувач може або зламати верстку, встановити наприклад логін як `<h1>Сергій</h1>` або ж вставити зловмисний скрипт, наприклад: `Сергій<script>stoleCookie()</script>`, який буде виконуватись на сторінці користувача який саме переглядає напис з логіном й таким чином красти сессії користувачів.

**Встановлення змінної**
    
    <c:set var="salary" value="${2000*2}"/>

не настільки популярний тег, але дозволяє встановити на сторінці локальної змінної щоб потім використати її в умовних блоках, циклах чи ж просто при виведенні.

**Умовний блок**

    <c:if test="${salary > 2000}">
        <p>My salary is: <c:out value="${salary}"/><p>
    </c:if>

Для реалізації if-else необхідно використовувати тег `choose` (аналог `switch-case`)

    <c:choose>
        <c:when test="${salary <= 0}">
           Salary is very low to survive.
        </c:when>
        <c:when test="${salary > 1000}">
            Salary is very good.
        </c:when>
        <c:otherwise>
            No comment sir...
        </c:otherwise>
    </c:choose>

**Цикл**

    <c:forEach var="i" begin="1" end="5">
        Item <c:out value="${i}"/><p>
    </c:forEach>

Цикл `for-each` виглядає так:

    <c:forTokens var="name" items="${namesList}">
        <c:out value="${name}"/><p>
    </c:forTokens>

де змінна `namesList` має бути колекцією строкових змінних, наприклад `List<String>` або `String[]`. Цей самий цикл з заданням елементів:

    <c:forTokens var="name" items="Zara,nuha,roshy" delims="," >
        <c:out value="${name}"/><p>
    </c:forTokens>

Більш детальний опис та інші теги можна знайти на сайті: http://www.tutorialspoint.com/jsp/jsp_standard_tag_library.htm

Створення власного тегу
-------

Іноді стандартної бібліотеки тегів не вистачає й буває необхідно створити свій власний тег. Щоб зробити це необхідно описати його в файлі бібліотеки тегів `.tld` та створити код, який перетворюватиме тег у HTML код.

Наприклад, буває необхідно показувати деякі елементи сторінки тільки для користувачів, які мають певну роль чи право доступу - немає сенсу показувати звичайному користувачу посилання на сторінку адміністрування чи модерації. Нехай існує об’єкт опису користувача User, у якого є поле `List<String> permissions` - яке містить список наявних прав у користувача. Створимо тег, який буде відображати свій вміст тільки якщо у користувача є право вказане в параметрах тегу:

Для початку створимо файл опису бібліотеки тегів - PermissionsTag.tld, який необхідно помістити в папку `WEB-INF` проекту. Файл може містити опис кількох тегів або функцій, однак в даному випадку тег буде лише один.

	<taglib>
		<tlibversion>1.0</tlibversion>
		<jspversion>1.1</jspversion>
		<shortname>permchecker</shortname>
		<info>Taglib for authorization</info>
		<uri>http://vntu.edu.ua/jsp/taglib/permchecker</uri>

		<tag>
		    <name>hasPermission</name>
		    <tagclass>edu.vntu.mblog.web.tags.PermissionsTagHandler</tagclass>
		    
		    <attribute>
		    	<name>permission</name>
		    	<required>true</required>
		    	<rtexprvalue>true</rtexprvalue>
		  	</attribute>
		  	
		 	<attribute>
		    	<name>user</name>
		    	<required>false</required>
		    	<rtexprvalue>true</rtexprvalue>
		  	</attribute>
		</tag>

	</taglib> 

В заголовку файлу вказані версія бібліотеки - `tlibversion`, версія JSP починаючи з якої може використовуватись бібліотека - `jspversion`, коротке ім’я та опис й головне `uri` - ідентифікатор ресурсу за яким цей файл буде підключатись у JSP - варто зауважити що цей URI може бути будь-яким і не обов’язково існувати (звичайно URL `http://vntu.edu.ua/jsp/taglib/permchecker` нікуди не приведе - такої сторінки не існує, однак це не заважає використовувати його як ідентифікатор для бібліотеки тегів).

Далі йде опис тегу, тут визначені тільки деякі з можливих елементів: 

`name` 
: ім’я за яким будуть звертатись до тегу

`tagclass`
: клас, який описує як перетворювати вміст тегу в HTML/

`attribute`
: набір властивостей атрибутів тегу

	`name`
	: назва атрибуту

	`required`
	: вказує що атрибут є обов’язковим

	`rtexprvalue`
	: вказує що у якості значення можна вказувати не тільки стрічку, але й значення EL: ${user.login}

Вбудовування на сторінку такого тегу матиме вигляд:

	<%@taglib prefix="auth" uri="http://vntu.edu.ua/jsp/taglib/permchecker" %>

	<auth:hasPermission user="${user}" permission="CREATE_POSTS">
		Код специфічний для користувача, що може створювати сторінки.
	</auth:hasPermission>

Префікс при цьому не має жодного значення й може бути будь-яким, головним тут є `uri` бібліотеки тегів. Яким чином отримати поточного користувача буде розглянуто пізніше, поки що вважатимемо що він певним чином був занесений у атрибут `user`.
Тепер опишемо реалізацію классу самого тегу PermissionsTagHandler. Значення атрибутів будуть встановлені за допомогою однойменних setXX методів

	package edu.vntu.mblog.web.tags;

	import edu.vntu.mblog.domain.User;
	import edu.vntu.mblog.web.SessionConstants;

	import javax.servlet.http.HttpSession;
	import javax.servlet.jsp.JspException;
	import javax.servlet.jsp.tagext.TagSupport;

	public class PermissionsTagHandler extends TagSupport {
		
		private String permission;
		private User user;

		public PermissionsTagHandler() {}

		public void setPermission(String permission) {
			this.permissions = permission;
		}
		
		public void setUser(User user) {
			this.user = user;
		}

	    @Override
		public int doStartTag() throws JspException {
			boolean result = user.getPermissions().contains(permissions);
			return result ? EVAL_BODY_INCLUDE : SKIP_BODY;
		}
	}

Головна логіка тегу знаходиться в перевизначеному методі `doStartTag()` (аннотація @Override підказує що метод є перевизначеним). Даний метод викликається на початку тегу й повертає одну з декількох можливих констант: 

`EVAL_BODY_INCLUDE`
: весь вміст тегу повинен бути оброблений та відображений користувачу, аналогічно до тегу `if`

`SKIP_BODY`
: весь вміст тегу необхідно пропустити
