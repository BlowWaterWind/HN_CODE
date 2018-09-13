<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>我的订单详情</title>
    <link href="${ctxStatic}/css/sim/wap.css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <link href="${ctxStatic}/css/sim/wap_list.css" rel="stylesheet">
</head>
<script>
    $(function () {
        var clientType = isAppClient();
        if (clientType == "html5") {
            $(".topbar").show();
        } else {
            $(".topbar").remove();
        }
    });
</script>
<body>
<!-- start container -->
<div class="container bg-gray">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return">返回</a>
        <a href="javascript:window.history.back();" class="icon icon-close">关闭</a>
        <h3>我的订单</h3>
    </div>
    <!-- end topbar -->
    <div class="orders-wrap">
        <ul class="order-list">
            <li>
                <div class="order-list-left">订单编号</div>
                <div class="order-list-right"><p class="item">${result.orderSubNo}</p></div>
            </li>
            <li>
                <div class="order-list-left">套餐名称</div>
                <div class="order-list-right"><p class="item">${result.detailSim.baseSetName}</p></div>
            </li>
            <li>
                <div class="order-list-left">物流公司</div>
                <div class="order-list-right"><p class="item">${result.logisticsCompany}</p></div>
            </li>
            <li>
                <div class="order-list-left">物流单号</div>
                <div class="order-list-right"><p class="item">${result.logisticsNum}</p></div>
            </li>
            <li>
                <div class="order-list-left">所选号码</div>
                <div class="order-list-right"><p class="item">${result.detailSim.phone}</p></div>
            </li>
            <c:if test="${imsi != null}">
                <li>
                    <div class="order-list-left">IMSI码</div>
                    <div class="order-list-right"><p class="item">${imsi}</p></div>
                </li>
            </c:if>
            <c:if test="${result.detailSim.iccid != null}">
                <li>
                    <div class="order-list-left">SIM卡号</div>
                    <div class="order-list-right"><p class="item">${result.detailSim.iccid}</p></div>
                </li>
            </c:if>
            <c:if test="${fn:length(result.orderSimBusiList) > 0}">
                <li>
                    <div class="order-list-left">已订购业务</div>
                    <div class="order-list-right">
                        <c:forEach items="${result.orderSimBusiList}" var="index">
                            <p class="item">${index.busiName}</p>
                        </c:forEach>
                    </div>
                </li>
            </c:if>
        </ul>
    </div>
</div>
</body>

</html>