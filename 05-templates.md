HTML-шаблони
---------
В даному розділі основну увагу буде приділено побудові *HTML* сторінок додатку на основі шаблонів, принципу функціонування шаблонів в *Spring* та чому, власне, вони побудовані саме таким чином. 

Для початку необхідно визначитись які задачі вирішують шаблони на стороні сервера й які інші підходи можна використати для побудови сторінок. 

.. задача .. Самим простим, на перший погляд, підходом для побудови сторінки може бути генерація коду *HTML* безпосередньо в коді програми, коли теги являються звичайними стрічками й розбавляються змінними отриманими з інших джерел:

    TO BE DONE: додати приклад жахливого коду з циклом


https://ru.wikipedia.org/wiki/Шаблонизатор


В якості двигуна для побудови *HTML* сторінок з набору різних шаблонів

Розглянемо структуру директорій створеного веб-додатку:

TO BE DONE: directory-structure.png

`src/main/java`
: в даній директорії містяться весь основний код програми на мові *Java*. Всі створені `*.java` файли містяться в пакеті `labs`.

`src/main/resources`
: дана директорія містить файл `application.properties` з основними налаштуваннями додатку, папку `static/` для зберігання статичних ресурсів додатку, а саме *CSS* та *Javascript* файли, а також папку `templates/` — де зберігатимуться *HTML*-шаблони, на основі яких будуть будуватись сторінки веб-додатку.

`src/test/java`
: директорія розрахована на так звані Unit-тести — невеликі підпрограми, які перевіряють правильність роботи окремих компонентів системи та взаємодії цих компонентів. Зазвичай для написання тестів використовують популярні бібліотеки такі, як *JUnit*, *TestNG* та інші. Використовуватись не буде.

`pom.xml`
: даний файл є конфігурацією консольної утиліти *Maven*, яка може використовуватись для автоматизованої компіляції, тестування та зборки проекту. Результати роботи *Maven* будуть поміщатись в діректорію `target/`.



Thymeleaf is a Java library. It is an XML / XHTML / HTML5 template engine (extensible to other formats) that can work both in web and non-web environments. It is better suited for serving XHTML/HTML5 at the view layer of web applications, but it can process any XML file even in offline environments.

It provides an optional module for integration with Spring MVC, so that you can use it as a complete substitute of JSP in your applications made with this technology, even with HTML5.

The main goal of Thymeleaf is to provide an elegant and well-formed way of creating templates. Its Standard and SpringStandard dialects allow you to create powerful natural templates, that can be correctly displayed by browsers and therefore work also as static prototypes. You can also extend Thymeleaf by developing your own dialects.

Cтруктура директорій веб додатку
------
Розглянемо структура директорій створеного веб додатку:


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

