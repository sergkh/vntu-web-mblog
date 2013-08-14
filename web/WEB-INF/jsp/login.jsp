<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="includes/header.jsp" %>
<div class="abs-center">
    <form class="form well span4">
            <legend>Вхід</legend>

            <div class="control-group">
                <label class="control-label" for="session-login">Логін</label>
                <div class="controls input-prepend">
                    <span class="add-on"><i class="icon-user"></i></span>
                    <input id="session-login" name="session-login" placeholder="user@mail.com" class="input-large" required="" type="text">
                    <p id="session-login-error" class="error-block"></p>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="session-password">Пароль</label>
                <div class="controls input-prepend">
                    <span class="add-on"><i class="icon-lock"></i></span>
                    <input id="session-password" name="session-password" placeholder="*******" class="input-large" required="" type="password">
                    <p id="session-password-error" class="error-block"></p>
                </div>
            </div>

            <div class="control-group text-center">
                <button type="submit" name="login-button" class="btn btn-primary">Увійти</button>
            </div>
    </form>
</div>

<%@include file="includes/footer.jsp" %>