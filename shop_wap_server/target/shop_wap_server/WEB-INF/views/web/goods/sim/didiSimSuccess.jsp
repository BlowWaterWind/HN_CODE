
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>滴滴至尊卡</title>
    <link href="${ctxStatic}/css/sim/wap-simonlineV2-didi.css?v=2" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.js"></script>
</head>
<body style="font-size: 12px;">
<div class="container bg-white">
    <div class="success-wrap">
        <div class="success-icon"></div>
        <div class="success-text">
            <h3>您已成功办理滴滴至尊卡</h3>
            <p class="text-orange">您的手机卡将于3个工作日内寄出
                <br>请您在即日起30天之内激活使用</p>
        </div>
    </div>
    <!-- end success-wrap -->
    <!-- start orders-info -->
    <div class="orders-info">
        <div class="orders-info-text">
            <dl>
                <dt>订单号：</dt>
                <dd>${orderNo}</dd>
            </dl>
            <dl>
                <dt>订单查询：</dt>
                <dd>关注微信公众号<span class="text-orange">湖南移动微厅</span>，使用订单号或身份证号登录查询订单详情</dd>
            </dl>
        </div>
    </div>
    <!-- end orders-info -->
    <div class="fixed-wrap">
        <div class="fixed">
            <a href="${ctx}/simBuy/simH5OnlineToApply?confId=${confId}&feature_str=${feature_str}" class="btn-orange" style="height: 50px;" >完成</a>
        </div>
    </div>
</div>
<%--<script type="text/javascript" src="lib/jquery-2.2.4.js"></script>--%>
</body>
</html>
