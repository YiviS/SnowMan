<%--
  Created by IntelliJ IDEA.
  User: XUGANG
  Date: 2017/2/20
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
<html lang="en" class="no-js">
<head>
    <title>登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">


</head>
<body>
<div class="page-container">
    <h1>Login</h1>
    <form action="${snowman}/login" method="post">
        <input type="text" name="username" class="username" placeholder="Username">
        <input type="password" name="password" class="password" placeholder="Password">
        <input type="checkbox" name="rememberMe" value="" style="display: none">
        <button type="submit">Sign me in</button>
        <div class="error"><span>+</span></div>
    </form>
    <div class="connect">
        <p>Or connect with:</p>
        <p>
            <a class="facebook" href=""></a>
            <a class="twitter" href=""></a>
        </p>
    </div>
    <div align="center">Copyright © 2017 <a href="https://github.com/Yivis" target="_blank" style="text-decoration: none" title="Yivis">github.com/Yivis</a></div>
</div>

<!-- Javascript -->
<script src="${snowman}/plugins/jquery/jquery-1.8.2.min.js"></script>
<script src="${snowman}/plugins/supersized/supersized.3.2.7.min.js"></script>
<script src="${snowman}/plugins/supersized/supersized-init.js"></script>
<script src="${snowman}/static/js/login/scripts.js"></script>
<!-- CSS -->
<link rel='stylesheet' href='${snowman}/plugins/googleapis/fonts/fonts.css'>
<link rel="stylesheet" href="${snowman}/static/css/login/reset.css">
<link rel="stylesheet" href="${snowman}/static/css/login/supersized.css">
<link rel="stylesheet" href="${snowman}/static/css/login/style.css">
</body>
</html>
