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
