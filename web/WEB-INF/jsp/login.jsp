<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp" %>

<div class="" id="loginModal">
	<div class="modal-header">
    	 <h3>Вітаємо на нашому сайті!</h3>
    </div>
    <div class="modal-body">
	    <ul class="nav nav-tabs" id="login-or-register">
	    	<li class="active"><a href="#login" data-toggle="tab">Авторизація</a></li>
	        <li><a href="#register" data-toggle="tab">Реєстрація</a></li>
	    </ul>
	    <div class="tab-content">
                
			<div class="tab-pane active in" id="login">
                <!-- Login -->
	            <form class="well span4" action='' method="POST">
                	<fieldset>
		                <legend>Вхід</legend>
			            <div class="control-group">
			            	<!-- Username -->
			                <label class="control-label" for="session-login">Логін</label>
			                <div class="controls input-prepend">
			                    <span class="add-on"><i class="icon-user"></i></span>
			                    <input id="session-login" name="session-login" placeholder="user@mail.com" class="input-large" required="" type="text">
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
			            <!-- Remember-me-->
			            <label class="checkbox" for="remember-me">
     						<input type="checkbox" id="remember-me"> Запам'ятати мене?
    					</label><br/>
			            
			            <div class="control-group text-center">
			            	<!-- Button -->
			                <button type="submit" name="login-button" class="btn btn-primary">Увійти</button>
			            </div>
					</fieldset>
				</form>
   	 		</div>
            
            <div class="tab-pane fade" id="register">
	            <!-- Registration -->
	            <form id="tab">
	                    <label>Логін</label>
	                    <input type="text" value="" class="input-xlarge">
	                    <span class="required" style="color: red;">*</span>
	                    <label>Пароль</label>
	                    <input type="text" value="" class="input-xlarge">
	                    <span class="required" style="color: red;">*</span>
	                    <label>Пароль (повторно)</label>
	                    <input type="text" value="" class="input-xlarge">
	                    <span class="required" style="color: red;">*</span>
	                    <label>Email</label>
	                    <input type="text" value="" class="input-xlarge">
	                    <span class="required" style="color: red;">*</span>
	                    <label>Коротко про себе</label>
	                    <textarea rows="3" class="input-xlarge">
	                    </textarea>
	                    <p style="font-size: smaller;">Поля, помічені "<span class="required" style="color: red;">*</span>", обов'язкові для заповнення.</p>
	                    <div>
	                      <button class="btn btn-primary">Зареєструватись</button>
	                    </div>
	            </form>
			</div>
            
		
    </div>
</div>
</div>

<%@include file="includes/footer.jsp" %>