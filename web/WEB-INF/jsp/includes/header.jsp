<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MBlog</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/main.css" media="screen"/>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.js"></script>

</head>
<body>

<div class="navbar navbar-inverse navbar-static-top">
    <div class="navbar-inner">
        <ul class="nav">
            <li><a href="#">Головна</a></li>
            <li class="divider-vertical"></li>
            <li>
                <a href="#">Модерація</a>
            </li>
            <li class="divider-vertical"></li>
            <li class="active">
                <a href="#">Користувачі</a>
            </li>
        </ul>

        <ul class="nav pull-right">
            <li class="divider-vertical"></li>
            <li><a href="#">user</a></li>
            <li><button type="button" class="btn btn-small pull-right">Вихід</button></li>
        </ul>

    </div>
</div>