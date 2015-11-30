Серіалізація форм
----------

При реалізації завантаження сторінок сайту за допомогою AJAX необхідно також змінити поведінку стандартних html форм. Форми завжди переходять на нову сторінку й повністю перевантажують сторінку, що відкидає нас назад до звичайного сайту з перезавантаженням сторінок, більше того - оскільки браузер не передає частину після `#`, то сервер не завжди зможе повернути користувача до сторінки, з якої він відправляв форму. Тому додамо ще невеличкий скрипт, що буде відправляти всі форми використовуючи AJAX запити :

```javascript
$( function() {

	$("form").on("submit", function(evt) {
	  evt.preventDefault();

	  var form = $(this);
	  $.ajax({
	      url: form.arrt('action'),
	      type: form.attr('method') || 'GET',
	      data: form.serialize()
	  }).done(function(result){
	  	 form.find("#result").text('Form posted');
	  });
	});
});
```

Першим аргументом в функції обробнику події стоїть об’єкт, що містить дані про те коли й з яким елементом сталась подія. Виклик функції `evt.preventDefault()` говорить що подія далі не повинна оброблятись браузером й відповідно форма не має відправитись, це є альтернативою `return false;` в функції обробнику. Вираз `form.attr('method') || 'GET'` поверне або метод вказаний у атрибуті `method` елемента форми, або ж `GET` якщо атрибуту немає. Всі дані форми у вигляді javascript об’єкта можна отримати використовуючи функцію jQuery [`serialize()`](http://api.jquery.com/serialize/), наприклад для форми:

```html
	<form action="action.jsp" method="POST">
	    <input type="text" name="email"/>
	    <input type="text" name="name"/>
	    <input type="submit" name="Submit" />
	</form>
```
вона поверне об’єкт виду:
```javascript
	{
		email: "пошта",
		name: "ім’я"
	}
```

XML
-----

Розши́рювана мо́ва розмі́тки (англ. Extensible Markup Language, скорочено XML) — запропонований консорціумом World Wide Web (W3C) стандарт побудови мов розмітки ієрархічно структурованих даних для обміну між різними застосунками, зокрема, через Інтернет.

Приклад документу:
```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<users>
	    <comment>Jani</comment>
	    <user id="1" name="Alex" />
	    <user id="2" name="Alice" />
	    <user id="3" name="Bob" />
	    <user id="4" name="Sam" />
	</users>
```
Приклад роботи з XML документом з Javascript:
```javascript
	$.get('test.xml').done(function(xml) {
		$(xml).find('user').each(function() {
			alert($(this).attr('name'));
		});
		form.find("#result").text('Form posted');
	});
```

JSON
-----

JSON (англ. JavaScript Object Notation - об'єктний запис JavaScript, вимовляється джейсон) — це легкий формат обміну даними між комп'ютерами. JSON базується на тексті, і може бути з легкістю прочитаним людиною. Формат дозволяє описувати об'єкти та інші структури даних. Цей формат головним чином використовується для передачі структурованої інформації через мережу (завдяки процесу, що називають серіалізацією).
Розробив і популяризував формат Дуглас Крокфорд (англ.).
JSON знайшов своє головне призначення у написанні веб-програм, а саме при використанні технології AJAX. JSON виступає як заміна XML під час асинхронної передачі структурованої інформації між клієнтом та сервером. При цьому перевагою JSON перед XML є те, що він дозволяє складні структури в атрибутах, займає менше місця і прямо інтерпретується за допомогою JavaScript в об'єкти.

Приклад JSON документу:
```javascript
	{
	   "firstName": "Петро",
	   "lastName": "Сомін",
	   "age" : 25,
	   "phoneNumbers": [
	       "0(432) 123-1234",
	       "0(432) 123-4567"
	   ]
	}
```
Основні типи у JSON унаслідувані від javascript:

Number
: число, ціле або з плаваючою комою, не обрамлюється в лапки:

String
: Стрічка
(double-quoted Unicode, with backslash escaping)

Boolean
: (true or false)
Array
: (an ordered, comma-separated sequence of values enclosed in square brackets; the values do not need to be of the same type)

Object
: (an unordered, comma-separated collection of key:value pairs enclosed in curly braces, with the ':' character separating the key and the value; the keys must be strings and should be distinct from each other)

null
: (empty)


MIME тип:	application/json
```javascript
{
   "firstName": "Іван",
   "lastName": "Коваленко",
   "address": {
       "streetAddress": "вул. Грушевського 14, кв.101",
       "city": "Київ",
       "postalCode": 21000
   },
   "phoneNumbers": [
       "044 123-1234",
       "050 123-4567"
   ]
}
```

JavaScript eval()
------
Оскільки JSON представляється синтаксично правильним фрагментом коду JavaScript, природним способом розбору JSON-даних в JavaScript-програмі є використання вбудованої в JavaScript функції eval(), яка призначена для обчислення JavaScript-виразів. При цьому підході відпадає необхідність у використанні додаткових парсерів.
Техніка використання eval() робить систему вразливою, якщо джерело JSON-даних, що використовуються, не відноситься до надійних. В якості таких даних може виступати шкідливий JavaScript код для атак за допомогою ін'єкції коду. За допомогою цієї вразливості можливо здійснювати крадіжку даних, підробку автентифікації. Проте, вразливість можна усунути за рахунок використання додаткових засобів перевірки даних на коректність. Наприклад, до виконання eval() отримані від зовнішнього джерела дані можуть перевірятися за допомогою регулярних виразів. У RFC, що визначає JSON[1] пропонується використовувати такий код для перевірки його відповідності формату JSON

Вбудований JSON об’єкт
______

Останні версії веб-браузерів мають вбудовану підтримку JSON і здатні його обробляти без виклику функції eval(), що приводить до 
описаної проблеми. Обробка JSON у такому разі зазвичай здійснюється швидше. 
```javascript
	JSON.stringify(frm.serializeArray());
	JSON.parse('{"user": "name"}')
```
Для отримання JSON з сервера можна використовувати функцію $.getJSON, яка є скороченням до більш загальної функції:
```javascript
	$.ajax({
	  url: url,
	  dataType: 'json',
	  data: data,
	  success: callback
	});
```
Приклад використання:
```javascript
	$.getJSON('ajax/test.json', function(data) {
	  var items = [];
	 
	  $.each(data, function(key, val) {
	    items.push('<li id="' + key + '">' + val + '</li>');
	  });
	 
	  $('<ul/>', {
	    'class': 'my-new-list',
	    html: items.join('')
	  }).appendTo('body');
	});
```

Шаблонізація на Javascript
-------------

HTML:
```html	
	<table id="users-table">
	<thead>
	  <tr>
	      <td>ID</td>
	      <td>Ім’я</td>
	      <td>Пошта</td>
	      <td>Особиста сторінка</td>
	  </tr>
	</thead>
	
	<tbody></tbody>
	</table>

	<script id="user-row-template" type="text/html">
  	  <tr>
	    <td>${id}</td>
	    <td>${name}</td>
	    <td>${email}</td>
	    <td><a href="/users/${id}">Users link</a></td>
	  </tr>
	</script>
```
Javascript:
```javascript	
	function renderTpl(id, obj) {
	  var tpl = $(id).text();
	  return tpl.replace(/\$\{(.+)\}/g, function(match, contents) {
	    return obj[contents] || '';
	  });
	}
	
	var users = [
	  {
	    id : 1,
	    name : 'Anna',
	    email : 'anna@yahoo.com'
	  },
	  {
  	    id : 2,
	    name : 'Arsen',
	    email : 'arsen@yandex.ru'
	  }
	];
	
	jQuery.each(users, function(i, val) {
	  $('#users-table>tbody').append(renderTpl('#user-row-template', val));    
	});
```

Приклад можна спробувати на [jsfiddle](http://jsfiddle.net/6CM6B/)
