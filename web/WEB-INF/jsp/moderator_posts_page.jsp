<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="auth"  uri="http://vntu.edu.ua/jsp/taglib/permchecker" %>

<%@include file="includes/header.jsp" %>
<%@include file="includes/navigation.jsp" %>


<div class="container">
		
 	<!-- Messages -->
	<div class="row-fluid">
	
		<c:forEach var="msg" items="${posts}">

				    <div class="row-fluid">
				    	<div class="span1 text-center">
				    		<a href="${pageContext.request.contextPath}/users/${msg.authorLogin}/">
                                <c:choose>
                                    <c:when test="${msg.authorAvatar != null}">
                                        <img class="img-avatar-small img-circle" alt="User avatar"
                                             src="${pageContext.request.contextPath}/static/img/avatars/${msg.authorAvatar}">
                                    </c:when>
                                    <c:when test="${msg.authorAvatar == null}">
                                        <img class="img-avatar-small img-circle" alt="User avatar" src="http://placehold.it/128x128">
                                    </c:when>
                                </c:choose>

                                ${msg.authorLogin}
			    			</a>
				    	</div>
			  		    <div class="span9"><c:out value="${msg.text}"/></div>

                        <div class="span2 timebadge">
					    	<span class="badge pull-right">
					    		<i class="icon-time"></i> <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${msg.date}"/>
					    	</span>
					    </div>

				    	<form action="${pageContext.request.contextPath}/moderator" method="POST">
                            <input type="hidden" name="action" value="disablePost">
                            <input type="hidden" name="postId" value="${msg.id}">
							<button type="submit" class="btn btn-danger btn-mini pull-right"> <i class="icon-remove"></i> Заборонити</button>
						</form>
				  
				   
		    			<form action="${pageContext.request.contextPath}/moderator" method="POST">
                            <input type="hidden" name="action" value="confirmPost">
                            <input type="hidden" name="postId" value="${msg.id}">
		        			<button type="submit" class="btn btn-success btn-mini pull-right"> <i class="icon-ok"></i> Підтвердити</button>
	        			</form>
				       
			    	    
				    </div>
				    <hr/>

	    </c:forEach>
	    
	</div><!-- .row-fluid -->
	
</div><!-- .container -->

<%@include file="includes/footer.jsp" %>