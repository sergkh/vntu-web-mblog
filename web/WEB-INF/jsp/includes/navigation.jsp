<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="auth"  uri="http://vntu.edu.ua/jsp/taglib/permchecker" %>

<div class="navbar navbar-inverse navbar-static-top">
    <div class="navbar-inner">
        <ul class="nav">
       		<auth:hasPermission permissions="USER">
   				<li>
   					<a href="${pageContext.request.contextPath}/users/${sessionScope.user.login}"> <i class="icon-home"></i> Моя сторінка</a>
   				</li>
            	<li class="divider-vertical"></li>
			</auth:hasPermission>
        
            <auth:hasPermission permissions="MODERATE_POSTS">
	            <li>
	                <a href="${pageContext.request.contextPath}/moderator"> <i class="icon-eye-open"></i> Модерація</a>
	            </li>
	            <li class="divider-vertical"></li>
            </auth:hasPermission>
            
            <auth:hasPermission permissions="MANAGE_USERS">
	            <li>
	                <a href="${pageContext.request.contextPath}/admin"> <i class="icon-th-list"></i> Користувачі</a>
	            </li>
            </auth:hasPermission>
        </ul>

        <ul class="nav pull-right">
            <li class="divider-vertical"></li>
            <li><a href="${pageContext.request.contextPath}/users/${sessionScope.user.login}">${sessionScope.user.login}</a></li>
            
            <auth:hasPermission permissions="USER">
            	<li>
            		<form action="${pageContext.request.contextPath}/logout" method="POST">
            			<button type="submit" class="btn btn-small pull-right">Вихід</button>
           			</form>
          		</li>
            </auth:hasPermission>
            
            <auth:unregistered>
            	<a href="${pageContext.request.contextPath}" class="btn btn-small pull-right">Реєстрація / Вхід</a>
            </auth:unregistered>
        </ul>

    </div>
</div>