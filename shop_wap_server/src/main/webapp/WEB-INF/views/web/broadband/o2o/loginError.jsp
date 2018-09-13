<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>湖南移动网上营业厅</title>
    <%
        String result =URLDecoder.decode(request.getParameter("value"),"UTF-8");
        System.out.print(result);
    %>
</head>
<body>

<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <span class="suess-img"><img src="${ctxStatic}/images/error.png"></span><!--办理失败替换error.png-->
        <div class="suess-list" style="text-align: center;">

            <p class="suess-tit font-blue"><%=result%> !</p>
            <%--<p class="suess-tit font-blue">登录校验失败，请重新登录!${value}</p>--%>
        </div>
        <%--<div class="tj-btn"><a class="btn btn-blue" href="${ctx}">返回首页</a> <a class="btn btn-blue" href="${ctx}/broadband/broadbandHome">返回宽带专区首页</a></div>--%>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>