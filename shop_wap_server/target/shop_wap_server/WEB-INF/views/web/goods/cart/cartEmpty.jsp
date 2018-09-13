<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <title>湖南移动商城</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
</head>
<body>
<div class="top container">
    <div class="header sub-title">
        <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
        <h1>购物车</h1>
        <sys:headInfo cartTop="1"/>
    </div>
</div>
<div class="container empty-cart pd-t45">
    购物车空空如也，赶紧去选购吧<br>
  <!--   或者登录查看您的购物车<br> -->
    <a class="btn btn-rose" href="${ctx}">去逛逛</a>
   <%--  <a class="btn btn-blue" href="${ctx}/login/toLogin">去登录</a> --%>
</div>
<!--底部菜单start-->
<%@include file="/release/front/navBottom.jsp"%>
<!--底部菜单end-->
</body>
</html>