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
<%--<input type="hidden" id="form1_orderSub" name="form1_orderSub" value="${orderNo}" />--%>
<%--<input type="hidden" id="form1_orderSubDetail" name="form1_orderSubDetail" value="${goodsName}" />--%>
<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <span class="suess-img"><img src="${ctxStatic}/images/suess.png"></span><!--办理失败替换error.png-->
        <div class="suess-list" style="padding-left: 50px;">
            <p class="suess-tit font-blue">您的业务办理成功，感谢您的购买!</p>
            <p><b class="font-rose">48小时内</b>客服会确认安装信息，请您保持手机畅通。</p>
        <%--<div class="tj-btn"><a class="btn btn-green" href="${ctx}/myOrder/toMyAllOrderList">订单详情</a><!--付款失败 class btn-rose--><a class="btn btn-blue" href="${ctx}/broadband/broadbandHome">返回宽带首页</a> </div>--%>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>