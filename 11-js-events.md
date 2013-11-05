Обробка подій в Javascript
--------

Більшість дій, які програмуються за допомогою JavaScript у веб додатках, це обробка різноманітних подій від користувача та браузера. Події можуть генеруватись елементами 

DOM -події, які ініціюються елементами DOM . Наприклад , подія click відбувається при кліці на елементі , а подія mouseover - коли покажчик миші з'являється над елементом ,
Події вікна . Наприклад подія resize - при зміні розміру вікна браузера ,
Інші події , наприклад load , readystatechange . Вони використовуються , скажімо , в технології AJAX.
Саме DOM- події пов'язують дії , що відбуваються в документі , з кодом JavaScript , тим самим забезпечуючи динамічний веб -інтерфейс.

Події можна додавати через атрибути елементів. Розглянемо приклад додавання функцій для валідації форми:

	<html>
	<body>
		<form id="order-form" action="/order" method="POST" onsubmit="return validate(this);"> 
			Email:
			<input type="text" name="email" onchange="emailCheck(this)">
			How many apples?
			<input type="text" name="quantity" onchange="rangeCheck(this, 1, 100)">
			<input type="submit" value="Order"/>
		</form>
	</body>

	<script type="text/javascript">
		function emailCheck(elem) {
			var text = elem.value;
			var regEx = /[a-zA-Z][\w\.]*@[\w\.]{2,}/;

			if(!regEx.test(text)) {
				alert("Email is not valid");
				return false;
			}

			return true;
		}

		function rangeCheck(elem, min, max) {
			var v = parseInt(elem.value);

			if(isNaN(v)) {
				alert(elem.name + " is not a number");
				return false;
			}

		   	if (v < min) {
		      alert(elem.name + " has to be greater than " + min);
		      return false;
		   	}

		   if (v > max) {
		      alert(elem.name + " has to be less than " + max);
		      return false;
		   }

		   return true;
		}

		function validate(formEl) {
		   return emailCheck(formEl.email) & rangeCheck(formEl.quantity, 1, 200);
		}

	</script>
	</html>

Додавання подій з Javascript
----

Можливо також додавати події до елементів безпосередньо з javascript. Для цього необхідно знайти елемент до якого необхідно додати обробник та встановити його:

	var formElem = document.getElementById("order-form"); // знаходимо форму за ідентифікатором

	formElem.onsubmit = function() { 
		return emailCheck(formEl.email) & rangeCheck(formEl.quantity, 1, 200); 
	};

`document` це об’єкт браузера, що представляє собою об’єктну модель HTML-документу (DOM - Document Object Model), тобто містить об’єкти всіх тегів, що знаходяться на сторінці, їх атрибутів, вмісту і т.д. Роботу з DOM більш детально буде розглянуто далі.

В даного коді не врахована одна особливість роботи браузера - javascript код виконується по мірі завантаження сторінки, не очікуючи завантаження інших елементів. Тобто якщо документ буде мати наступну структуру:


	<script> попередній скрипт </script>

	...

	<form id="order-form"> форма </form>

То можлива ситуація, що браузер почав виконувати скрипт, ще не отримавши елемент `form` від серверу, тоді код `document.getElementById("order-form")` не знайде необхідного елементу. Найбільш правильним підходом буде помістити даний код у функцію, що викликатиметься, коли весь документ буде завантажено. Така подія є у елементу `<body>`:

	<body onload="documentReady()">

Тепер помістивши код у функцію `documentReady()`:

	function documentReady() {
		/// попередній код
	}

Після чого код відпрацьовуватиме вірно.

Список найбільш використовуваних подій
--------

Для всього документу:

`onload`
: викликається коли браузер завантажив усі елементи документу.

		<body onload="documentLoaded()">

	Javascript варіант:

		window.onbeforeunload=function(){ ... };


`onbeforeunload`
: викликається перед закриттям вікна, може не працювати по різному в різних браузерах:
	
		<body onbeforeunload="return confirm('O RLY?');">

`onresize`
: Викликається коли розмір вікна браузера змінився:

		<body onresize="alert('Size changed')">


Для елементів:

`onclick`
: клік на елементі

		<div onclick="divClicked(this)">

`ondblclick`
: подвійний клік на елементі

`onmouseover`
: переміщення мишки над елементом

`onkeydown`
: натиснення клавіші клавіатури


Для форм:

`onblur`
: при втраті фокусу введення з елементу

		<input type="text" onblur="validateValue()">	

`onchange`
: при втраті фокусу введення з елементу, якщо значення його змінилось 

`onfocus`
: при отриманні фокусу введення

`onsubmit`
: при відправці форми

		<form onsubmit="return validateForm(this);">


Робота з DOM
----------

DOM (англ. Document Object Model - Об'єктна модель документа) — програмний інтерфейс для роботи зі структурованими документами. Використовується DOM, як правило, для XML-подібних документів, куди входить і HTML, однак поширений й для інших видів документів, так багато бібліотек для роботи з word/excel файлами теж реалізують DOM.

DOM бібліотеки після розбору документу будують дерево об’єктів, де коренем є сам документ, у якого зазвичай є один піделемент - кореневий елемент документу, й у нього є список дочірніх піделементів, які вже представляють HTML-теги сторінки, у кожного піделементу також є список дочірніх елементів. У кожного з елементів DOM визначено набір методів та атрибутів, які дозволяють керувати цим елементом - змінювати атрибути тегу, вміст чи навіть переносити елемент по дереву. 

В javascript браузер будує й оновлює DOM-дерево автоматично, тому змінюючи DOM-елемент з javascript, наприклад змінюючи значення атрибуту `width` - браузер одразу відобразить зміни на сторінці - змінить довжину елементу. Представлено DOM-дерево поточного документу через передвизначений об’єкт `document`. У нього зазвичай використовують 2 піделементи: 

> Ще одним поширеним підходом до роботи зі структурованими документами є SAX - підхід, при якому під час 



Після аналізу структурованого документа, будується його представлення у вигляді дерева. Дерево, в моделі DOM, складається із множини зв'язних вузлів (Node) різних типів. Як правило, розрізняють вузли наступних типів: `document.documentElement` - який відповідає кореню документу, тобто тегу `<html>` та `document.body`, що відповідає тегу `<body>`. У них можна 

Документ (Document)
: корінь дерева, представляє цілий документ.

Фрагмент документа (DocumentFragment)
: вузол, який є корнем піддерева основного документа.

Елемент (Element)
: представляє окремий елемент HTML або XML документа.

Атрибут (Attr)
: представляє атрибут елемента.

Текст (Text)
: представляє текстові дані, які містяться в елементі або атрибуті.


The root is the DOM always document.documentElement. This special property will give access to the topmost HTML tag.

Another starting point can be the document.body, which represents the BODY tag.

Both entry points are valid. But document.body can be null.

For example, you access document.body from an inline script in the HEAD, prepare to see null instead. That’s natural, because there is no BODY yet.


An element keeps references to children in childNodes array-like property.
All nodes are referenced, including whitespace ones (excepts IE<9).

	function go() {
        var childNodes = document.body.childNodes
 
        for(var i=0; i<childNodes.length; i++) {
            alert(childNodes[i])
        }
    }

children
Sometimes we need to browse only element nodes, skipping text nodes. That’s what the children property is for.

It contains all element nodes. Check out the same example as above, but with children instead of childNodes.

	function go() {
	    var children = document.body.children
	    for(var i=0; i<children.length; i++) {
	        alert(children[i])
	    }
	}

	var body = document.body

	alert(body.firstChild === body.childNodes[0])
	alert(body.lastChild === body.childNodes[body.childNodes.length-1])


	alert( document.body.innerHTML ); // read current contents
	document.body.innerHTML = 'Yaaahooo!' // replace contents

Для деяких елементів innerHTML є тільки для читання.

elem.hasAttribute(name) - checks if the attribute exists
elem.getAttribute(name) - gets an attribute value
elem.setAttribute(name, value) - sets an attribute
elem.removeAttribute(name) - removes an attribute

input.value == input.getAttribute('value') - значення елементу вводу
input.checked == input.getAttribute('checked') - значення чекбоксу

var div = document.createElement('div');

elem.cloneNode(true)
Clones an element deeply, with all descendants.

elem.cloneNode(false)
Clones an element only, with attributes, but without children.

parentElem.appendChild(elem)
	Appends elem to the children of parentElem.

parentElem.insertBefore(elem, nextSibling)
	Inserts elem into the children of parentElem before the element nextSibling.

parentElem.removeChild(elem)
	Remove the elem from the children of parentElem.

parentElem.replaceChild(elem, currentElem)
	Replace the child element of parentElem, referenced by currentElem with the elem.

Пошук елементів


var elem = document.getElementById('planets')
var firstSpan = elem.getElementsByTagName('span')[0];

var elements = document.querySelectorAll('#planets > span')


Бібліотека jQuery
--------

<script type="text/javascript" src="jquery.js"></script>

Або через CDN:

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>


Найкраще jQuery описана в книзі Эрла Каслдайна та Крейга Шаркі - "JQuery Novice to Ninja"

Антон Шевчук http://anton.shevchuk.name/jquery-book/

$('#test') //знаходимо елемент з id="test" 
    .text('Клікни по мені')  //встановлюємо текст елемента рівним "Клікни по мені"
    .addClass('myAlert')     //додаємо клас "myAlert"
    .css('color','red')      //встановлюємо колір тексту червоним
    .attr('alert','Привіт, світе!') // додаємо атрибут "alert" із значенням "Привіт, світе!"
    .bind(                       // додаємо в обробник події click функцію, яка відкриє модальне
        'click',                 // вікно із текстом, що вказаний в атрибуті "alert" ("Привіт, світе!")
        function(){alert($(this).attr('alert'))}
    );


