<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：校园促销--%>
<html>
<head>
    <%--<c:set var="mymyTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关--%>
        <meta name="WT.si_n" content="">
        <meta name="WT.si_x" content="">
        <meta name="WT.si_s" content="">
    <title>校园促销-首页</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css"/>
    <link href="${ctxStatic}/css/sim/sim-school.css" rel="stylesheet"><!--这个页面选号的弹出框-->

    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <%-- 一个居中，三个换行居中的效果 --%>
    <style>
        .main .link-btn {
            position: absolute;
            left: 50%;
            -webkit-transform: translateX(-50%);
            transform: translateX(-50%);
            top: .266667rem;
            display: block;
            text-indent: -9999px;
            width: 7.92rem;
            height: 1.146667rem;
            background-image: url("${myTfsUrl}${conf.slctedImg}");
            background-size: contain;
            background-repeat: no-repeat;
        }
    </style>
</head>
<body>
<!-- start container -->
<div class="container">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}" alt="校促实名制卡">
    </div>
    <div class="main">
        <img src="${myTfsUrl}${conf.feeDetlImg}" alt="校促实名制卡">
        <a onclick="javascript:$('#applyForm').submit(); return;" class="link-btn">点击开卡</a>
        <div class="tips"><!--套餐详情,配置时按分号隔开-->
            <c:forEach items="${confDesc}" var="item">
            <p>${item}</p>
            </c:forEach>
        </div>
    </div>
    <form id="applyForm" action="${ctx}o2oSimPreBuy/realNameRegistry" method="post">
        <input type="hidden" name="confId" value="${confId}"/>
        <input type="hidden" name="recmdCode" id="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
        <input type="hidden" name="channelCityCode" id="channelCityCode">
        <input type="hidden" name="shopId" id="shopId" value="${shopId}">
        <input type="hidden" name="deliveryType" id="deliveryType">
    </form>
</div>

</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=6"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">
</script>
</html>