function validateLogin(el) {
	var login = $(el);
	var loginText = el.value;
	var loginBlock = login.parent(); // <div class="form-group">
	
	$.get("/exists", { login: loginText }).done(function (result) {
		
		if (result.exists) {
			loginBlock.find('#loginHelpBlock').removeClass('hidden');
			loginBlock.removeClass('has-success').addClass('has-error');
		} else {
			loginBlock.find('#loginHelpBlock').addClass('hidden');
			loginBlock.removeClass('has-error').addClass('has-success');
		}
		
		// збереження результату перевірки, щоб надалі заборонити відправку форми
		$(el).data('unoccupied', !result.exists); 
	});
}

function validateRegistration(form) {
	var hasErrors = false;
	var errorText = '';
	
	var login = form['login'].value;
	if (login.length < 6) {
		errorText += 'Login must have at least 6 symbols<br>';
		hasErrors = true;
	}
	
	var loginUnoccupied = $(form['login']).data('unoccupied');
	
	if (!loginUnoccupied) {		
		errorText += 'Login is occupied<br>';
		hasErrors = true;
	}
	
	var email = form['email'].value;
	if (!email.match(/[a-z][a-z\._-]*@[a-z]+.[a-z]{2,4}/i)) {
		errorText += 'Email is not valid<br>';
		hasErrors = true;
	}
	
	var password = form['pass'].value;
	if(password.length < 6) {
		errorText += 'Password is too short<br>';
		hasErrors = true;
	}
	
	var age = parseInt(form['age'].value);
	if (isNaN(age) || age < 16) {
		errorText += 'You have to be at least 16 y.o.<br>';
		hasErrors = true;
	}
	
	// div with errors
	var errorsDiv = form.firstElementChild; 
	errorsDiv.innerHTML = errorText;
	errorsDiv.className = hasErrors ? 'alert alert-danger' : 'alert alert-danger hidden';
	return !hasErrors;
}

