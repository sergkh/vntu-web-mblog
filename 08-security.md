Методи ідентифікації користувача
------

Протокол HTTP побудований таким чином, що він не зберігає стану між запитами. Й це є як його перевагою (серверу не потрібно відслідковувати стан, тому він може потребувати менше ресурсів) так і недоліком, тому що залишається відкритою проблема ідентифікації користувача. Тобто потрібен механізм, який допоміг би зв’язати різні HTTP запити й сказати що вони відносяться до однієї сесії роботи користувача. Наприклад, в інтернет магазині потрібно знати що запит замовлення товару відправив той самий користувач, який 5 хвилин назад додав мікрохвильову піч у корзину, а не інший який просто натиснув кнопку замовлення. 

> Варіант просто завести відповідну змінну "корзина", наприклад, в сервісі чи контролері буде прекрасно працювати під час розробки - оскільки тільки ви, як розробник, будете замовляти товари. Однак, як тільки користувачів стане більше, оскільки вони працюватимуть паралельно, можливі різні варіанти, коли один користувач буде замовляти товари іншого і перетирати чужі замовлення. Тому такий підхід краще відкинути одразу.

Перше, що спадає на думку - визначати користувача по IP адресі, однак відношення між користувачем та IP не є одним до одного - можливий варіант коли декілька користувачів (або навіть достатньо багато) може працювати під однією IP адресою - коли вони знаходяться за NAT, або ж навпаки один користувач може мати динамічну IP адресу, яка буде змінюватись під час перегляду сторінок сайту. Тому цей варіант не підходить. Однак можна виділити такі підходи:

1. Додавати параметр в кожен URL та в форми.
2. Додавати певний параметр до кожного HTTP пакету.

Перший варіант досить очевидний — при першому заході користувача, або ж після логіну додавати до всіх URL на сайті певний параметр — ідентифікатор сесії, який можна зв’язати з користувачем. При цьому ідентифікатор сесії має бути достатньо великим й випадковим, тому що, якщо взяти, наприклад, просто номер сесії підряд чи логін/ідентифікатор користувача:

	http://secure.somesite.com/userspage?sessionId=1567

зловмиснику досить просто буде підібрати ідентифікатор просто перебираючи номери чи логіни підряд, або ж хитріше - отримавши ідентифікатор своєї сесії додати чи відняти від нього 1 - отримаєш сесію попереднього або наступного користувача. Тому ідентифікатором сесії обирають достатньо довгий та випадковий набір символів, який не пов’язаний з користувачем:

	http://secure.somesite.com/userspage?sessionId=903hfw74n6lMsEfyodG8r

Однак варіант не дуже зручний як для користувача так і для розробників, оскільки потрібно не забути всюди додати параметр сесії в URL або ж як параметр форм. Також він має суттєвий недолік — ідентифікатор сесії користувача можна досить легко підглянути, а користувач може випадково передати доступ до свого акаунта випадково передавши URL на сайт комусь із знайомих.

Тому зазвичай для ідентифікації користувача використовуються другий варіант, який реалізується за допомогою *Cookies*, і тільки якщо вони вимкнені в браузері, іноді прибігають до першого варіанту.

Робота з *Cookies*
------

*Cookie* - це фрагмент даних, який встановлюється веб-сервером (зрідка на клієнтській стороні) і зберігається браузером на комп'ютері користувача. Браузер при доступі до сайту додає цей фрагмент даних до кожного HTTP пакету. При цьому особливістю *Cookie* є те, що вони передаються тільки при доступі до того домену, для якого вони були встановлені, тобто *Cookie* встановлена сайтом *facebook.com* буде передаватись тільки при доступі до сторінок цього сайту й ніякого іншого. 

*Cookie* дозволяють зберігати дані між запитами на стороні користувача, й на практиці зазвичай використовуються для:

1. Аутентифікації користувача;
2. Зберігання персональних переваг і налаштувань користувача;
3. Відстеження сеансу користувача;
4. Ведення статистики по користувачам.

Розповсюджений сценарій використання — створення *Cookie* з даними користувача та його сесії взаємодії з сайтом після логіну. Таким чином ідентифікація всіх наступних дій користувача за допомогою цієї *Cookie*. При натисненні кнопки "Вихід" або ж по проходженню певного періода чи закритті вікна браузера — *Cookie* знищується. Однак досить багато сайтів створюють *Cookie* одразу при заході на сайт й таким чином можуть запам’ятовувати налаштування користувача чи ідентифікувати його у майбутньому. 

Дані *Cookie* це параметр та його значення, й таких *Cookie* може бути скільки завгодно. Для того, щоб встановити *Cookie* сервер передає браузеру спеціальний заголовок HTTP пакету:

	Set-Cookie: name=value; expires=date; path=/; domain=.example.org.

Після чого браузер в кожному наступному запиті буде передавати отримані дані також у заголовку:

	Cookie: name=value

> Максимальний розмір *Cookie* — 4093 байти, однак враховуючи, що вона буде додаватись до кожного пакету необхідно намагатись мінімізувати розмір даних, що передаються у *Cookie* - оскільки це може значно збільшити трафік між клієнтом та сервером. 

Обов’язковими для *Cookie* є пара name=value, однак розглянемо інші можливі атрибути:

	Set-Cookie: lang=ua; expires=Fri, 31 Dec 2013 10:59:59 GMT; path=/; domain=.mydomain.net; Secure; HttpOnly; 

`Expires`
: вказує час, після якого *Cookie* буде автоматично видалено. Якщо цей атрибут не вказаний, то *Cookie* зберігається до закриття браузера.

`Max-Age`
: другий варіант вказання часу життя *Cookie* - час в секундах скільки буде дійсна *Cookie*.

`Domain`
: визначає домен, для якого *Cookie* є дійсним. Доступ до *Cookie* завжди обмежений доменом з якого він був встановлений, що є основою безпеки для *Cookie*, й якщо не вказати цього явно - буде використаний домен сайту який встановлював *Cookie*. Явно вказувати домен необхідно, якщо потрібно дозволити доступ до *Cookie* з піддоменів. Завжди починається з крапки.

`Path`
: визначає частину URL, для яких видима *Cookie*.

`Secure`
: наказує передавати *Cookie* тільки по безпечному з’єднанню, зазвичай https.

`HttpOnly`
: вказує, що значення *Cookie* неможливо отримати з javascript через об’єкт document.cookie. Дозволяє додатково захистити *Cookie* від крадіжки. 

`Version=1`
: - версія.

Щоб програмно записати *Cookie* необхідно додати параметр типу `HttpServletResponse` в контроллер:

```java
@RequestMapping("/cookie-set")
public String createCookie(HttpServletResponse response) {

	Cookie cookie = new Cookie("user", "login");
	cookie.setHttpOnly(true);
	cookie.setMaxAge(20*60); // 20 хвилин

    response.addCookie(new Cookie("foo", "bar"));
}
```

Прочитати дещо простіше:

```java
@RequestMapping("/cookie-read")
public String readCookie(@CookieValue(value = "user", defaultValue = "unknown") String loginCookieValue) {
 
    //..
}
```

Переглянути всі *Cookie*, які зберігає браузер зазвичай можна в консолі розробника:

![Cookie сайту Google.com](https://github.com/sergkh/vntu-web-mblog/raw/lections/img/cookies-browser-view.png)

До недоліків *Cookie* можна віднести те, що дані постійно передаються з кожним запитом, що збільшує трафік між клієнтом та сервером й значно обмежує обсяги даних, які можна зберігати в *Cookie*. Другим недоліком є те, що *Cookie* не повинні містити ніяких критичних даних тому, що дані з них можуть бути легко прочитані будь-ким й підроблені. Якщо зловмисник отримає доступ до комп’ютера користувача (або ж доступ до трафіку) він може легко скопіювати значення *Cookie* й підставити його в свої запити. Щоб боротись з останнім недоліком *Сookie* часто шифруються або підписуються закритим ключем й значно обмежуються в терміні дії (іноді до 15-20 хвилин).

Сессія користувача
------------

Сессії - це засіб, що суміщує в собі згенерований ідентифікатор сесії роботи користувача в *Cookie* (або параметри в URL, якщо *Cookie* заборонені) й дані на сервері пов’язані з сесією користувача. Сессії це спроба подолати недоліки *Cookie* – в *Cookie* зберігається тільки відносно невеликий ідентифікатор сесії, який повинен бути випадковим й досить довгим (зазвичай від 32 символів), а всі критичні дані зберігаються на сервері й пов’язуються з даним ідентифікатором сесії. 

Сесія в *Java* сервлетах виглядає аналогічно до об’єкту `Model`, де можна зв’язати будь-який об’єкт зі строковим ідентифікатором. Щоб використовувати сесії достатньо додати в метод контролера параметр з типом `HttpSession`, а *Spring* сам знайде сесію за *Cookie*, або ж створить нову (разом з *Cookie*). Наприклад, логін користувача можна реалізувати наступним чином:

```java
@RequestMapping(value = "/login", method = RequestMethod.POST)
public String login(String login, String password, HttpSession session) {
	Long userId = userService.authenticateUser(login, password);
	
	if(userId != null) { // null, якщо логін чи пароль не вірний
		session.setAttribute("userId", userId);
		return "redirect:home";
	} else {
		return "redirect:login";
	}
}
```

А потім щоб отримати ідентифікатор користувача, що виконує запит можна скористатись заданим значенням:

```java
@RequestMapping(value = "/purchase", method = RequestMethod.POST)
public String purchase(Long productId, HttpSession session) {
	Long currentUserId = (Long) session.getAttribute("userId");
	
	if(userId == null) {
		return "redirect:login"
	}

	purchaseService.purchase(userId, productId);

	return "redirect:home";
}
```

Для того щоб знищити сесію, наприклад, при виході користувача з сайту, можна це зробити вручну:

```java
@RequestMapping("/logout", method = RequestMethod.POST)
public String logout(HttpSession session) {
	session.invalidate();
	return "redirect:login";
}
```

Або ж сесія буде знищена автоматично якщо не буде використовуватись певний час. Можна також видалити тільки певне значення:

```java
	session.removeValue("userId"); 
```

Дані з сесії можна безпосередньо використовувати в шаблонах *Thymeleaf*:

```html
<span th:text="${session.userId}">Користувач</span>
```

Сесії зберігаються в пам’яті на веб сервері, тому в них є низка переваг: в сесіях можна зберігати критичні дані, всеодно вони не передаються на сторону клієнта й залишаються на сервері. Також сесії часто використовують для швидкого доступу до даних користувача — наприлад крім ідентифікатору можна зберігати логін, права доступу користувача, щоб не діставати ці дані постійно з СУБД. В сесіях можна зберігати проміжні дані форм, наприклад якщо реєстрація займає кілька сторінок — логічно зберігати проміжні сторінки в сесії й записувати користувача в базу даних, тільки після певного кроку (реєстрації на кілька сторінок багато користувачів не проходить до кінця). 

Також виходячи з попереднього твердження все ж таки потрібно намагатись обмежувати кількість об’єктів в сесії, оскільки пам’ять сервера, все ж таки не безкінечна й, при великій кількості користувачів, може закінчитись. 

До недоліку сессій варто віднести те, що до використання сесій сервер фактично не мав ніякого стану: всі дані, які користувачі вносили одразу зберігались в базу даних, а всі які читались брались з БД. Така архітектура дозволяла легко підключати скільки завгодно таких серверів до СУБД й розподіляти навантаження між ними будь-яким чином, оскільки кожен запит на кожному з серверів виконувався б однаково. З використанням сесій ця перевага втрачається — якщо користувач залогіниться на одному з серверів, його сесія буде створена тільки на цьому сервері. Також при перезавантаженні всі сесії користувачів будуть втрачені й їм доведеться логінитись знову.

Існують наступні вирішення перелічених проблем:
 — *Tomcat* можливо налаштувати зберігати дані сесій на диск при перезавантаженні.
 - Кілька серверів *Tomcat* можна налаштувати періодично обмінюватись даними сесій між собою, однак при такому варіанті кожен з серверів буде мати всі сесії всіх користувачів, що обмежує кількість одночасних користувачів пам’ятю кожного з серверів.
 — Можна прив’язувати всі запити з однієї IP адреси до одного з серверів.
 — Можна обмежитись використанням тільки *Cookie*, або ж самому генерувати ідентифікатор сесії й зберігати дані сесії в СУБД.

Spring Security
------
Задача логіну й реєстрації користувача настільки часто зустрічається, що бібліотека *Spring* не може не містити засобів для реалізації цього функціоналу. Існує спеціальна бібліотека *Spring Security*, яка дозволяє в одному класі налаштувати права, необхідні для перегляду того чи іншого URL. Якщо користувач не має доступу до сторінки його автоматично буде перекинуто на сторінку логіну. 

Для початку роботи з бібліотекою, необхідно:

1. Видалити раніше додану стрічку `security.basic.enabled=false` в файлі конфігурації.

2. Необхідно мати клас реалізацію інтерфейсу `UserDetails` з яким вміє працювати *Spring Security*, для цього найбільше підходить уже існуючий клас `User`, до якого, окрім існуючих, необхідно буде додати декілька методів:

```java
@Entity
public class User implements UserDetails {
	...

	public String getUsername() {
		return getLogin();
	}
	
	public String getPassword() {
		return password;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("USER");
	}

	public boolean isAccountNonExpired() { return true; }
	public boolean isAccountNonLocked() { return true; }
	public boolean isCredentialsNonExpired() { return true; }
	public boolean isEnabled() { return true; }	
}
```

Значення більшість методів зрозуміла: `getUsername()` та `getPassword()` повертають логін та пароль користувача, які будуть використовуватись при авторизації користувача. 

Метод `getAuthorities()` повертає список ролей користувача. Ролі при цьому можуть бути описані довільними стрічками (наприклад ADMIN,MANAGER,ACCOUNTANT), які далі можна вказувати в анотаціях, щоб обмежети доступ до конкретного URL тільки визначеним ролям. Наприклад, форма керування користувачами повинна бути доступна тільки адміністраторам, а не всім користувачам. Зберігати ролі можна в окремій табличці й пов’язувати з користувачем відношенням багато до багатьох. В даному випадку ролей не передбачено, тому просто вважається, що кожен зареєстрований користувач має роль USER.

Інші методи передбачені для блокування профілю адміністратором або для обмеження часу дії профілю. В даному випадку цей функціонал не потрібен, тому всі методи повертають `true`. 

3. Необхідно реалізувати сервіс який буде повертати профіль користувача за логіном, або викидати виключення `UsernameNotFoundException`, якщо користувача не знайдено.

```java

@Service
public class UserSecurityService implements UserDetailsService {
	
	@Autowired
	private UserRepository usersRepo;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = usersRepo.findByLogin(username);

		if(user == null) {
			throw new UsernameNotFoundException("User with login " + username + " was not found");
		}
		
		return user;
	}
}
```

4. Створити клас з спеціальними анотаціями, що налаштує *Spring Security*, назвемо його `SecurityConfig`:

```java
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // вимкнути csrf захист

        http.authorizeRequests()
            .antMatchers("/", "/register").permitAll() // дозволити анонімним користувачам заходити на '/' та /register 
            .anyRequest().authenticated(); // всі інші запити потребують аутентифікації
            
        http.formLogin()
            .loginPage("/login")  // URL логіну
            .usernameParameter("login") // назва параметру з логіном на формі
            .permitAll() // дозволити всім заходити на форму логіну
            .and()
        .logout()
            .permitAll(); 
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder()); 
    }
}
```

Розглянемо конфігурацію детальніше:

Стрічка `http.csrf().disable();` вимикає захист від CSRF атак, щоб не ускладнювати додаток. А основне налаштування відбувається в наступній стрічці де вказується список URL `"/"` та `"/register"` (шаблони типу `"/reg*"` також підтримуються) та команда `permitAll()`, що означає дозволити заходити на ці URL всім користувачам, а далі `anyRequest().authenticated()` — вимагати ауторизуватись для всіх інших URL. Завжди необхідно дозволяти доступ до URL реєстрації та логіну всім користувачам, інакше вони просто не зможуть увійти до системи. 

Наступний блок налаштовує роботу форми логіну: вказується POST URL на який буде відправлятись форма логіну `.loginPage("/login")`, далі вказується як на формі буде називатись параметр username, оскільки в нас він називається `login`,  а не `username`, як очікує *Spring Security*. Далі дається доступ до форми логіну та виходу всім користувачам. 

У другому методі конфігурації вказується сервіс, створений раніше, за допомогою якого бібліотека знаходитиме користувача для авторизації, та метод, за допомогою якого шифрується пароль користувача (оскільки це впливає на перевірку).

Це все, що необхідно було налаштувати. Тепер кожна сторінка окрім `"/"` чи `"/register"` переправлятиме на форму логіну, якщо користувач ще не авторизований. Для того щоб отримати поточного користувача й перевіряти чи доступ є анонімним можна скористатись статичними методами, які для зручності можна додати в клас `User`:

```java
public class User implements UserDetails {
	...

	public static User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static Long getCurrentUserId() {
		return getCurrentUser().getId();
	}
	
	public static boolean isAnonymous() {
		return "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
```

Тепер створення та отримання постів для поточного користувача матиме вигляд:

```java
@Service
public class PostsService {

	@Autowired
	private UserRepository usersRepo;
	
	@Autowired
	private PostRepository postsRepo;

	@Transactional
	public Page<Post> getPosts(int page, int pageSize) {
		User currentUser = usersRepo.findOne(User.getCurrentUserId());

		return postsRepo.findByAuthorInOrderByCreatedAtDesc(
			currentUser.getSubscriptions(), new PageRequest(page-1, pageSize));
	}

	@Transactional
	public void addPost(String text) {
		User currentUser = usersRepo.findOne(User.getCurrentUserId());

		postsRepo.save(new Post(currentUser, text, new Date()));
	}
}
```

Можливо також одразу отриматиоб’єкт `User` з сесії, однак він створений в іншій транзакції, тому безпосередньо його використовувати не можна — необхідно за ідентифікатором чи логіном отримати користувача з бази. 

Код для даного етапу розробки можна знайти за [посиланням](https://github.com/sergkh/vntu-web-mblog/tree/3a95e443be411a9e923aee02a1c9b620c85f2e34).
