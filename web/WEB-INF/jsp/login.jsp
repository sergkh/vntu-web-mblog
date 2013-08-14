<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp" %>
<div class="container-fluid">

    <form class="form well span4">
            <legend>Вхід</legend>

            <div class="control-group">
                <label class="control-label" for="session-login">Логін</label>
                <div class="controls input-prepend">
                    <span class="add-on"><i class="icon-user"></i></span>
                    <input id="session-login" name="session-login" placeholder="user@mail.com" class="input-medium" required="" type="text">
                    <p id="session-login-error" class="error-block"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="session-password">Пароль</label>
                <div class="controls input-prepend">
                    <span class="add-on"><i class="icon-lock"></i></span>
                    <input id="session-password" name="session-password" placeholder="*******" class="input-medium" required="" type="password">
                    <p id="session-password-error" class="error-block"></p>
                </div>
            </div>

            <div class="control-group">
                <button type="submit" name="login-button" class="btn btn-primary">Увійти</button>
            </div>
    </form>

</div>

    <!-- form id="loginForm" class="form well login-form">
        <div class="control-group">
            <label class="control-label" for="login">Логин:</label>
            <div class="controls<div class="controls input-prepend">">
                <span class="add-on"><i class="icon-user"></i></span>
                <input id="login" name="login" class="username span3" type="text" required="required">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="password">Пароль:</label>
            <div class="controls input-prepend">
                <span class="add-on"><i class="icon-lock"></i></span>
                <input id="pass" name="password" class="password span3" type="password" required="required">
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary" name="Submit">Вход</button>
        </div>
    </form-->



<%@include file="includes/footer.jsp" %>