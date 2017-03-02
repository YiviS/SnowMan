<%--
  Created by IntelliJ IDEA.
  User: XUG
  Date: 2017/2/14
  Time: 17:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
<html>
<head>
    <title>Hello World</title>
</head>
<body>
<h1>Hello World</h1>
登录成功：管理中心<br/>
当前用户：${username}<br/>
<li><a href="${basePath}/logout" title="退出登录">退出登录</a></li>
</body>
</html>
