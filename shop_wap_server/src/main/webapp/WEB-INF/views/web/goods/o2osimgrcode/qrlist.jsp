<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/8/14
  Time: 17:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>我的二维码</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
</head>
<style type="text/css">
    li{
        position: relative;
    }
    .qr-detail{
        width: 1.5rem;
        height: 1.5rem;
        background-image: url("${ctxStatic}/images/qrcode.png");
        background-size: contain;
        background-repeat: no-repeat;
        position: absolute;
        right: 1rem;
        top:0.6rem;
    }

    .channel-list li {
        padding-bottom: 10px;
        border-bottom: 1px solid #e9e9e9;
    }

    .detail{
        text-align: right;
        font-size: 10px;
        line-height: 100%;
        padding-right: 10px;
    }
</style>
<body>
<div class="top container">
    <div class="header border-bottom">
        <%--<a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>--%>
        <h1>我的二维码</h1>
    </div>
</div>
<div class="container">
    <ul class="channel-list">
        <c:forEach items="${confs}" var="index">
            <li style="margin-top: 10px">
            <span>
                <img src="${ctxStatic}/images/simpay/pay_${index.cardType}.png" />
                <h4>${index.cardTypeName}</h4>
                <p class="channel-txt">${index.planDesc}</p>
            </span>
                <i class="qr-detail" onclick="qrdetail('${index.confId}')"></i>
                <p class="detail">查看详情</p>
            </li>
        </c:forEach>
    </ul>
</div>
</body>
<script type="text/javascript">
    var ctx = '${ctx}';
    function qrdetail(confId) {
        window.location.href= ctx +"qrcodeOper/qrDetail?confId="+confId;
    }
</script>
</html>
