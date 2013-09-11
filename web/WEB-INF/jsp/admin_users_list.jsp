<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="auth"  uri="http://vntu.edu.ua/jsp/taglib/permchecker" %>

<%@include file="includes/header.jsp" %>
<%@include file="includes/navigation.jsp" %>

<div class="container">
	<div class="row-fluid">
        <table class="table table-condensed table-hover">
            <thead>
                <tr>
                    <th>Логін</th>
                    <th>Пошта</th>
                    <th>Дата реєстрації</th>
                    <th>Дата блокування</th>
                    <th style="width: 120px;">Стан</th>
                </tr>
            </thead>

            <tbody>
              <c:forEach var="user" items="${users}">
                <tr>
                  <td class="text-center">
                      <c:choose>
                          <c:when test="${msg.authorAvatar != null}">
                              <img class="img-avatar-small img-circle" alt="User avatar"
                                   src="${pageContext.request.contextPath}/static/img/avatars/${msg.authorAvatar}">
                          </c:when>
                          <c:when test="${msg.authorAvatar == null}">
                              <img class="img-avatar-small img-circle" alt="User avatar" src="http://placehold.it/128x128">
                          </c:when>
                      </c:choose>
                      <c:out value="${user.login}"/>
                  </td>
                  <td><c:out value="${user.email}"/></td>
                  <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${user.creationDate}"/></td>
                  <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${user.blockDate}"/></td>

                  <!--td>
                    <c:choose>
                      <c:when test="${user.blockDate == null}">
                          <form action="${pageContext.request.contextPath}/admin" method="POST">
                              <input type="hidden" name="action" value="disableUser">
                              <input type="hidden" name="userId" value="${user.id}">
                              <button type="submit" class="btn btn-success btn-mini"><i class="icon-remove"></i> Заблокувати</button>
                          </form>
                      </c:when>
                      <c:otherwise>
                          <form action="${pageContext.request.contextPath}/admin" method="POST">
                              <input type="hidden" name="action" value="enableUser">
                              <input type="hidden" name="userId" value="${user.id}">
                              <button type="submit" class="btn btn-danger btn-mini"><i class="icon-ok"></i> Розблокувати</button>
                          </form>

                      </c:otherwise>
                    </c:choose>
                  </td-->

                  <td>
                      <div class="btn-group" data-toggle="buttons-radio">
                          <button class="btn btn-mini">Заблокований</button>
                          <button class="btn btn-mini">Користувач</button>
                          <button class="btn btn-mini">Модератор</button>
                          <button class="btn btn-mini">Адмін</button>
                      </div>

                    <!-- button type="button" class="btn btn-mini"><i class="icon-arrow-up"></i> В модератори</button -->
                  </td>

                </tr>
              </c:forEach>
            </tbody>
        </table>
    </div>
    
    <!--div class="pagination text-center">
        <ul>
            <li><a href="#">Prev</a></li>
            <li class="active"><a href="#">1</a></li>
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">Next</a></li>
        </ul>
    </div -->
</div>



<%@include file="includes/footer.jsp" %>