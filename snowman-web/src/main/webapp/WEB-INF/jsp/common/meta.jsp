<%--
  Created by IntelliJ IDEA.
  User: XUGANG
  Date: 2017/2/20
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<%--<% String path = request.getContextPath(); String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; %>--%>
<c:set var="defaultPageSize" value="10"/>
<c:set var="defaultPageList" value="[10,20,30]"/>
<!-- 全局 -->
<script>
    var SNOWMAN_PATH = "${basePath}";
</script>

