<%@ page import="edu.vntu.mblog.services.UsersService" %>
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
                    <th>Дозволи</th>
                    <th></th>
                </tr>
            </thead>

            <tbody id="users-table">
              <c:forEach var="user" items="${users}">
                <tr data-user="${user.id}">
                  <td>
                      <c:choose>
                          <c:when test="${msg.authorAvatar != null}">
                              <img class="img-avatar-small img-circle" alt="User avatar"
                                   src="${pageContext.request.contextPath}/static/img/avatars/${msg.authorAvatar}">
                          </c:when>
                          <c:when test="${msg.authorAvatar == null}">
                              <img class="img-avatar-small img-circle" alt="User avatar" src="http://placehold.it/128x128">
                          </c:when>
                      </c:choose>
                      <br/>
                      <c:out value="${user.login}"/>
                  </td>
                  <td><c:out value="${user.email}"/></td>
                  <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${user.creationDate}"/></td>
                  <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${user.blockDate}"/></td>

                  <td class="permissions">
                    <!-- c:out value=" $ { user . permissions }"/ -->
                    <c:forEach var="perm" items="${user.permissionsNames}">
                        <span class="badge"><c:out value="${perm}"/></span>
                    </c:forEach>

                  </td>

                  <td class="user-manage-buttons">
                      <div class="btn-group">
                          <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                              <i class="icon-pencil"></i>
                              <span class="caret"></span>
                          </a>
                          <ul class="dropdown-menu">
                              <c:choose>
                                <c:when test="${user.blockDate == null}">
                                    <li><a href="#"
                                           onclick="return manageUser(this, ${user.id});" data-action="block">
                                        Заблокувати
                                        </a>
                                    </li>
                                </c:when>
                              <c:otherwise>
                                  <li>
                                    <a href="#" onclick="return manageUser(this, ${user.id});" data-action="unblock">
                                      Розблокувати
                                    </a>
                                  </li>
                              </c:otherwise>
                              </c:choose>

                              <auth:hasPermission permissions="MODERATE_POSTS" user="${user}">
                                  <li><a href="#" class="permissions-link"
                                         data-permission="MODERATE_POSTS" data-action="removePermission">
                                      <i class="icon-remove"></i> MODERATE_POSTS
                                      </a>
                                  </li>
                              </auth:hasPermission>

                              <auth:hasPermission permissions="MODERATE_POSTS" user="${user}" invert="true">
                                  <li><a href="#" class="permissions-link"
                                         data-permission="MODERATE_POSTS" data-action="addPermission">
                                      <i class="icon-plus"></i> MODERATE_POSTS
                                      </a>
                                  </li>
                              </auth:hasPermission>

                              <auth:hasPermission permissions="MANAGE_USERS" user="${user}">
                                  <li><a href="#" class="permissions-link"
                                         data-permission="MANAGE_USERS" data-action="removePermission">
                                      <i class="icon-remove"></i> MANAGE_USERS
                                      </a>
                                  </li>
                              </auth:hasPermission>

                              <auth:hasPermission permissions="MANAGE_USERS" user="${user}" invert="true">
                                  <li><a href="#" class="permissions-link"
                                         data-permission="MANAGE_USERS" data-action="addPermission">
                                      <i class="icon-plus"></i> MANAGE_USERS
                                  </a>
                                  </li>
                              </auth:hasPermission>
                          </ul>
                      </div>
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