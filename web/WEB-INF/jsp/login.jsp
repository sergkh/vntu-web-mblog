<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp" %>

<div class="centered-login" style="padding-top: 15%;  ">
	    <ul class="nav nav-pills" id="login-or-register">
	    	<li class="active"><a href="#login" data-toggle="tab">Авторизація</a></li>
	        <li class="pull-right"><a href="#register" data-toggle="tab">Реєстрація</a></li>
	    </ul>

        <div class="tab-content">
                
			<div class="tab-pane active in" id="login">
                <!-- Login -->
	            <form action="" method="POST">
                    <div class="control-group">
                        <!-- Username -->
                        <label class="control-label" for="session-login">Логін</label>
                        <div class="controls input-prepend">
                            <span class="add-on"><i class="icon-user"></i></span>
                            <input id="session-login" name="session-login" placeholder="логін чи пошта" class="input-large" required="" type="text">
                            <p id="session-login-error" class="error-block"></p>
                        </div>
                    </div>
                    <div class="control-group">
                        <!-- Password-->
                        <label class="control-label" for="session-password">Пароль</label>
                        <div class="controls input-prepend">
                            <span class="add-on"><i class="icon-lock"></i></span>
                            <input id="session-password" name="session-password" placeholder="*******" class="input-large" required="" type="password">
                            <p id="session-password-error" class="error-block"></p>
                        </div>
                    </div>

                    <button type="submit" name="login-button" class="btn btn-primary">Увійти</button>
				</form>
   	 		</div>
            
            <div class="tab-pane fade" id="register">
	            <!-- Registration -->
	            <form action="" method="POST">
                    <div class="control-group">
                        <!-- Username -->
                        <label class="control-label" for="register-login">Логін</label>
                        <div class="controls input-prepend">
                            <span class="add-on"><i class="icon-user"></i></span>
                            <input id="register-login" name="register-login" placeholder="user123" class="input-large" required="" type="text">
                            <p id="register-login-error" class="error-block"></p>
                        </div>
                    </div>

                    <div class="control-group">
                        <!-- Email -->
                        <label class="control-label" for="register-email">Електронна пошта</label>
                        <div class="controls input-prepend">
                            <span class="add-on"><i class="icon-envelope"></i></span>
                            <input id="register-email" name="register-email" placeholder="user@mail.com" class="input-large" required="" type="text">
                            <p id="register-email-error" class="error-block"></p>
                        </div>
                    </div>

                    <div class="control-group">
                        <!-- Password -->
                        <label class="control-label" for="session-login">Пароль</label>
                        <div class="controls input-prepend">
                            <span class="add-on"><i class="icon-lock"></i></span>
                            <input id="register-password" name="register-password" placeholder="********" class="input-large" required="" type="text">
                            <p id="register-password-error" class="error-block"></p>
                        </div>
                    </div>

                    <button class="btn btn-success">Зареєструватись</button>
	            </form>
			</div>
            
		</div>
</div>

<%@include file="includes/footer.jsp" %>