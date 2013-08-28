<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="auth"  uri="http://vntu.edu.ua/jsp/taglib/permchecker" %>

<%@include file="includes/header.jsp" %>
<%@include file="includes/navigation.jsp" %>

<!-- User information -->
<div class="container">
	<div class="row-fluid">
		<div class="span12 well">
			<div class="span2">
				<a href="#" >
					<img src="http://placehold.it/128x128" alt="">
				</a>
			</div>
			<div class="span8">
				<h2><c:out value="${user}"/></h2><br/>
				<span class="badge badge-warning">${userStat.posts} повідомлення(нь)</span> 
				<span class=" badge badge-info">${userStat.followers}  підписчик(ів)</span> 
				<span class=" badge badge-info">${userStat.following} підписок(а)</span> 
			</div>
			<div class="span2">
			
				<c:choose>
				    <c:when test="${subscribed}">
				    	<form action="${pageContext.request.contextPath}/subscribtions/${user}" method="DELETE">
							<button type="submit" class="btn btn-warning pull-right"> <i class="icon-star-empty"></i> Відписатись</button>
						</form>
				    </c:when>
			
				    <c:when test="${!subscribed && sessionScope.login}">
		    			<form action="${pageContext.request.contextPath}/subscribtions/${user}" method="POST">
		        			<button type="submit" class="btn btn-success pull-right"> <i class="icon-star"></i> Підписатись</button>
	        			</form>
				    </c:when>
				</c:choose>
				
      			<auth:unregistered>
	       			<a href="${pageContext.request.contextPath}/" class="btn btn-success pull-right"> 
    	   				<i class="icon-user"></i> Увійти
       				</a>
      			</auth:unregistered>

			</div>
		</div>
	</div>
 	
 	<auth:hasPermission permissions="USER">
	 	<div class="row-fluid">
	 		
	 		<form id="post-message-form" action="/messages/" method="post">
	 			<fieldset>
					<textarea name="text" class="input-block-level" rows="3" placeholder="Напишіть повідомлення..."></textarea>
	 				
	 				<button type="submit" class="btn">Написати</button>
	 			</fieldset>
	 		</form>
	 	</div>
 	</auth:hasPermission>
 	
 	<!-- Message feed -->
	<div class="row-fluid">
	
		<c:forEach var="msg" items="${posts}">
		    <div class="row-fluid">
		    	<div class="span1 text-center">
		    		<a href="${pageContext.request.contextPath}/users/${msg.authorLogin}/">
		    			<img src="http://placehold.it/42x42" class="img-circle">
	    			</a>
		    	</div>
	  		    <div class="span9"><c:out value="${msg.text}"/></div>
			    <div class="span2 timebadge">
			    	<span class="badge pull-right">
			    		<i class="icon-time"></i> <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${msg.date}"/>
			    	</span>
			    </div>
	    	    
		    </div>
		    <hr/>
	    
	    </c:forEach>
	    
	</div><!-- .row-fluid -->
	
</div><!-- .container -->

<%@include file="includes/footer.jsp" %>