<!DOCTYPE html>
<html lang="zh-CN">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>支付成功</title>
    <link href="${ctxStatic}/css/sim/wap-simh5paysuccess.css" rel="stylesheet">
</head>

<body>
<!-- start container -->
<div class="container bg-gray">
    <div class="topbar">
        <a href="javascript:history.back(-1);" class="icon icon-return">返回</a>
        <a href="javascript:history.back(-1);" class="icon icon-close">关闭</a>
        <h3>订单支付</h3>
    </div>
    <!-- end topbar -->
    <div class="pay-return">
        <div class="pay-return-thumb">
            <img src="${ctxStatic}/images/goods/sim/h5online/dispose-error.png" alt="支付失败">
        </div>
        <h3>支付失败</h3>
        <p>您的订单支付失败,请重新支付!</p>
        <a href="${ctx}/simBuy/toPay?orderNo=${tfOrderSub.orderSubNo}&confId=${orderDetail.prodSkuId}&planId=${orderDetail.goodsSkuId}&selectPhone=${selectPhone}">重新支付</a>
    </div>
</div>
<!-- end container -->
<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
</body>

</html>