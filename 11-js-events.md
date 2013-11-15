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

> Ще одним поширеним підходом до роботи зі структурованими документами є SAX - підхід, при якому документ представляється як потік даних - аналізатор послідовно розбираючи документ генерує повідомлення, які містять елементи документу, які він розібрав. Зазвичай такі парсери менш зручні у використанні, однак при правильній реалізації потребують значно менше пам’яті.

В javascript браузер будує й оновлює DOM-дерево автоматично, тому змінюючи DOM-елемент з javascript, наприклад змінюючи значення атрибуту `width` - браузер одразу відобразить зміни на сторінці - змінить довжину елементу. Представлено DOM-дерево поточного документу через передвизначений об’єкт `document`. У нього зазвичай використовують 2 піделементи: `document.documentElement` - який відповідає кореню документу, тобто тегу `<html>` та `document.body`, що відповідає тегу `<body>`. 

Кожен елемент має посилання на масив дочірніх елементів:

        var childNodes = document.body.children;
 
        for(var i = 0; i < childNodes.length; i++) {
            alert(childNodes[i])
        }

У кожного з елементів масив `childNodes[i]` є список його дочірніх елементів й так далі. Однак шукати елемент таким чином не дуже зручно, тому існують додаткові функції пошуку елементу в дереві:

	var planets = document.getElementById('planets');
	var firstSpan = planets.getElementsByTagName('span')[0];
	var elements = document.querySelectorAll('#planets>span');

Перший вираз знайде елемент за ідентифікатором, `firstSpan` буде відповідати першому тегу `<span>` всередині тегу `planets` (однак кине виключення, якщо жодного тегу `<span>` не всередині `planets` не буде знайдено). Третій варіант демонструє пошук елементу за CSS селектором.

Коли елемент, який необхідно змінити знайдено, можливо над ним виконувати певні дії, наприклад нехай є HTML код:

	<div id="error-text">old text</div>

Можна переглянути та змінити вміст елементу:

	var errorDiv = document.getElementById('error-text');
	alert(errorDiv.innerHTML ); // покаже 'old text'
	errorDiv.innerHTML = 'OK';  // змінить вміст на 'OK'

Можливо доступатись не тільки до вмісту тегу, а й його атрибутів:

`element.hasAttribute(name)`
: повертає true, якщо у елементу встановлено атрибут з іменем `name`:

		var hasId = errorDiv.hasAttribute('id'); // = true

`element.getAttribute(name)`
: повертає значення атрибуту, якщо встановлено, у вигляді тексту.

		var id = errorDiv.getAttribute('id'); // 'error-text'

`element.setAttribute(name, value)`
: встановлює нове значення `value` атрибуту з іменем `name`. Встановимо клас `success` знайденому елементу:
	
		errorDiv.setArribute('class', 'success');

`element.removeAttribute(name)`
: видаляє атрибут з іменем `name`. Щоб видалити атрибут `class`:

		errorDiv.removeArribute('class');

Для деяких атрибутів існують скорочення, наприклад для тегів `<input>`:

	input.value == input.getAttribute('value'); // значення елементу вводу
	input.checked == input.getAttribute('checked'); // значення чекбоксу

Для того щоб додати елемент в документ, необхідно його створити, для чого є відповідний метод у документа:

	var li = document.createElement('li');
	li.innerHtml = 'Новий елемент';
	document.getElementById('items-list').appendChild(li);

Також додаткові функції для додавання елементів:

`elem.cloneNode(deepCopy)`
: клонує елемент, якщо `deepCopy=true`, то клонуються також всі піделементи даного, якщо ж `deepCopy=false`, то клонується тільки даний елемент без дочірніх.

`parentElem.appendChild(elem)`
: додає елемент `elem` до `parentElem`.

`parentElem.insertBefore(elem, nextSibling)`
: додає елемент `elem` до `parentElem` перед елементом `nextSibling`.

`parentElem.removeChild(elem)`
: видаляє елемент `elem` з батьківського `parentElem`.

`parentElem.replaceChild(elem, currentElem)`
: заміняє елемент `currentElem` елементом `elem` у батьківському елементі `parentElem`.

Бібліотека jQuery
--------

Підключення:

<script type="text/javascript" src="jquery.js"></script>

Або через CDN:

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

Найкраще jQuery описана в книзі Эрла Каслдайна та Крейга Шаркі - "JQuery Novice to Ninja"

Або ж у [Антона Шевчука](http://anton.shevchuk.name/jquery-book/)
