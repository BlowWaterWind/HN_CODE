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
</head>

<body>
<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <span class="suess-img"><img src="${ctxStatic}/images/suess.png"></span><!--办理失败替换error.png-->
        <div class="suess-list" style="text-align:center">
            <p class="suess-tit font-blue">您的短信发送成功！</p>
            <p><b class="font-rose">30分钟内</b>用户回复有效</p>
        <!-- <div class="tj-btn"><a class="btn btn-blue" href="${ctx}/broadband/broadbandHome">返回宽带首页</a> </div> -->
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>