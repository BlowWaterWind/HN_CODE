<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KD_ZDD" />
    <meta name="WT.si_x" content="20" />
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <%--<link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">--%>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/public.js"></script>
    <%--<script src="${ctxStatic}/js/dropload/dropload.min.js" type="text/javascript"></script>--%>
    <script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>

    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>


</head>
<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带订单</h1>
    </div>
</div>
<div class="container">
    <div class="tabs broad-tabs" id="content">
            <ul class="tab-menu">
                <li id="all"><a href="${ctx}/broadbandOrder/queryOrderList?flag=all">全部</a></li>
                <li id="willPay"><a href="${ctx}/broadbandOrder/queryPayOrderList?flag=willPay">支付相关订单<span class="order-tip">${payNum}</span></a></li>
            </ul>
            <input type="hidden" name="flag" value="${flag}" id="flag"/>
            <input type="hidden" name="startTime" id="startTime" value="${startTime}"/>
            <input type="hidden" name="endTime" id="endTime" value="${endTime}"/>
            <input type="hidden" name="pageNo" id="pageNo" value="0" />
            <input type="hidden" name="pageSize" id="pageSize" value="8" />
            <div class="order-detail" id="pageDiv">

        <c:forEach items="${broadbandOrderList}" var="broadbandOrder">
                <div class="order-list">
                    <div class="order-title">
                        <p class="font-gray"></p>
                        <p>订单编号：${broadbandOrder.orderSubNo}</p>
                    </div>
                    <dl class="detail-list last">
                        <dt><img src="${ctxStatic}/demoimages/kdtu.png" /></dt>
                        <dd>
                            <h4>${broadbandOrder.orderType.orderTypeName}</h4>
                            <p>${broadbandOrder.detailList[0].goodsName}</p>
                            <p><fmt:formatDate value="${broadbandOrder.orderTime}" pattern="yyyy-MM-dd hh:mm:ss"/></p>
                            <%--<a href="javascript:void(0)">更多详情</a>--%>
                            <%--<a href="javascript:void(0);" style="font-weight: bold;" onclick="detail('${broadbandOrder.tradeId}','${broadbandOrder.serialNumber}','${broadbandOrder.tradeStatus}','${broadbandOrder.offerName}','${broadbandOrder.tradeTypeCode}','${broadbandOrder.tradeFee}','${broadbandOrder.tradeOfferNum}','${broadbandOrder.createDate}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}','${broadbandOrder.preInstallDate}')">更多详情</a>--%>
                        </dd>
                    </dl>
                    <!--支付金额 start-->
                    <div class="order-amount">总额：¥${broadbandOrder.orderSubPayAmount/100}元<span class="ml20">${broadbandOrder.orderStatus.orderStatusName}</span></div>
                    <!--支付金额 end-->
                    <div class="order-btn neworder-btn">
                        <c:if test="${'0' eq broadbandOrder.orderSubPayStatus and '12' ne broadbandOrder.orderStatus.orderStatusId}">
                            <%--<a href="javascript:void(0)" class="btn btn-border-red" onclick="orderPay('${broadbandOrder.orderSubId}')">支付</a>--%>
                        </c:if>
                        <c:if test="${'1' eq broadbandOrder.orderSubPayStatus }">
                            <a href="${ctx}/broadbandOrder/payDetail?orderId=${broadbandOrder.orderId}" class="btn btn-border-red">支付明细</a>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
            </div>

    </div>
</div>
<%--<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.tabslet.min.js"></script>--%>
<script type="text/javascript">
    var ctx="${ctx}";
    var ctxStatic="${ctxStatic}";
    var tfsUrl="${tfsUrl}";
</script>
<script>
    $(function() {
        var flag = $("#flag").val();
        $("#"+flag).addClass("active");
    });

    function orderPay(orderSubId){
        window.location.href=ctx+'/broadbandOrder/payList?orderSubId='+orderSubId;
    }
</script>
</body>
</html>
