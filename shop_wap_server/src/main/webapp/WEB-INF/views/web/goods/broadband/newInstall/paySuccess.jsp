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
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>付款成功</h1>
    </div>
</div>
<c:set value="确定套餐" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <span class="suess-img"><img src="images/suess.png"></span><!--办理失败替换error.png-->
        <div class="suess-list">
            <p class="suess-tit font-blue">您的业务办理成功，感谢您的购买!</p>
            <p><b class="font-rose">24小时内</b>客服会确认安装信息，请您保持手机畅通。</p>
            <p>订单编号：<b class="font-rose">102928277662</b></p>
            <p>已付金额：<b class="font-rose">￥650.00</b></p>
            <p>套餐名称：50M宽带/1年</p>
            <p>
                <span class="sus-tit">合同期限：</span>
                <b class="sus-text font-rose">2015年03月28日~2016年04月04日</b>
            </p>
            <p>装 机 人：张三</p>
            <p>手　　机：<b class="font-rose">13888998788</b></p>
            <p>
                <span class="sus-tit">安装地址：</span>
                <b class="sus-text">湖南省长沙市芙蓉区人名东路329号湘域熙岸4栋A单元1层201</b>
            </p>
            <p>预约时间：休息日安装</p>
        </div>
        <div class="tj-btn"><a class="btn btn-green" href="##">订单详情</a><!--付款失败 class btn-rose--><a class="btn btn-blue" href="##">返回宽带首页</a> </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>