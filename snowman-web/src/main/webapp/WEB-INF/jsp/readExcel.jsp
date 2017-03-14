<%--
  Created by IntelliJ IDEA.
  User: XUGUANG
  Date: 2017/3/14
  Time: 11:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
<html>
<head>
    <title>readExcel</title>
</head>
<body>
<form action="${basePath}/user/readExcel" enctype="multipart/form-data" method="post">
    <div style="margin: 30px;"></div>
    <div><input id="excel_file" type="file" name="excelFile" size="50"/></div>
    <input type="submit" value="submit">
</form>
</body>
</html>
