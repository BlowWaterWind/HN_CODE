<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>订单详情</h1>
    </div>
</div>
<div class="container">
    <div class="order-detail">
        <!--地址信息 start-->
        <div class="adress-info">
            <div class="adress-txt">
                <p class="adress-number"><span>联系人：${broadbandOrder.custName}</span><span>${broadbandOrder.serialNumber}</span></p>
                <p>安装地址：${broadbandOrder.addrDesc}</p>
            </div>
        </div>
        <!--地址信息 end-->
        <!--店铺信息 start-->
        <div class="shop-info mt10">
            <div class="shop-title">
                <a href="javascript:void(0);">
                    <span class="shop-icon">${shopName}</span><span class="channel-blue">${broadbandOrder.tradeStatus}</span>
                </a>
            </div>
            <!--产品说明 start-->
            <div class="shop-gobal">
                <a href="javascript:void(0)">
                    <%--<img src="images/bus09.png" />--%>
                    <h4>${broadbandOrder.offerName}</h4>
                    <p class="shop-txt">&nbsp;</p>
                    <div class="shop-bottom"><span class="price"><fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/>元</span><span>x${broadbandOrder.tradeOfferNum}</span></div>
                </a>
            </div>
            <!--产品说明 end-->
            <!--价格明细 start-->
            <div class="price-info">
                <%--<p>宽带总价<span>￥98.00</span></p>--%>
                <%--<p>安装费用<span>￥0.00</span></p>--%>
                <%--<p>光猫价格<span>￥98.00</span></p>--%>
                <%--<p>机顶盒价格<span>￥0.00</span></p>--%>
                <p class="price-sum">订单总价<span>￥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/>元</span></p>
            </div>
            <!--价格明细 end-->
            <!--完成订单时间 start-->
            <div class="des-date">
                <p>订单编号：<span>${broadbandOrder.tradeId}</span></p>
                <c:if test="${not empty broadbandOrder.createDate}">
                    <p>创建时间：<span>${broadbandOrder.createDate}</span></p>
                </c:if>
                <c:if test="${not empty broadbandOrder.preInstallDate}">
                    <p>预约时间：<span>${broadbandOrder.preInstallDate}</span></p>
                </c:if>
                <c:if test="${not empty broadbandOrder.recvDate}">
                    <p>接单时间：<span>${broadbandOrder.recvDate}</span></p>
                </c:if>
                <c:if test="${not empty broadbandOrder.finishDate}">
                    <p>接单时间：<span>${broadbandOrder.finishDate}</span></p>
                </c:if>
            </div>
            <!--完成订单时间 end-->
        </div>
        <!--店铺信息 end-->

        <!--撤单透明化 start-->
        <c:if test="${not empty retList}">
            <div class="logist-info mt10">
                <h3 style="margin-bottom: 15px;">撤单记录</h3>
                <ul class="step-list">
                    <c:forEach items="${retList}" var="ret" varStatus="status">
                        <c:choose>
                            <c:when test="${status.first}">
                                <li class="log">
                            </c:when>
                            <c:otherwise>
                                <li>
                            </c:otherwise>
                        </c:choose>
                        <div class="step-info">
                            <p>撤单原因：${ret.CANCEL_REASON}</p>
                            <p>撤单状态：${ret.STATIC_STATUS}</p>
                            <p>装维电话：${ret.MAINTAIN_NAME}</p>
                            <p>创建时间：${ret.ACCEPT_DATE}</p>
                            <p>处理时间：${ret.UPDATE_TIME}</p>
                        </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <!--撤单透明化 end-->

        <!--日志 start-->
        <c:if test="${not empty logList}">
        <div class="logist-info mt10">
                <h3 style="margin-bottom: 15px;">宽带日志</h3>
            <ul class="step-list">
                <c:forEach items="${logList}" var="log" varStatus="status">
                    <c:choose>
                        <c:when test="${status.first}">
                            <li class="log">
                        </c:when>
                        <c:otherwise>
                            <li>
                        </c:otherwise>
                    </c:choose>
                    <div class="step-info">
                        <p>${log.ACCEPT_TAG}</p>
                        <span>${log.ACCEPT_TIME}</span>
                    </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
        </c:if>
        <!--日志 end-->

        <!--物流信息 start-->
        <c:if test="${not empty tradeInfoList}">
        <div class="logist-info mt10">
                <h3 style="margin-bottom: 15px;">安装进度</h3>
            <ul class="step-list">
                <c:forEach items="${tradeInfoList}" var="tradeInfo" varStatus="status">
                    <c:choose>
                        <c:when test="${status.first}">
                            <li class="active">
                        </c:when>
                        <c:otherwise>
                            <li>
                        </c:otherwise>
                    </c:choose>
                        <div class="step-info">
                            <p>${tradeInfo.name}，状态:${tradeInfo.stauts}，操作人:${tradeInfo.handleName}<a href="tel:${tradeInfo.phone}">${tradeInfo.phone}</a></p>
                            <span>${tradeInfo.endTime}</span>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
        </c:if>
        <!--物流信息 end-->
    </div>
</div>
<!--尾部 start-->
<div class="container channel-aifix">
    <div class="channel-footer">
        <div class="order-link">
            <a href="tel:${broadbandOrder.serialNumber}" class="btn-border">联系客户</a>
        </div>
    </div>
</div>
<!--尾部 end-->
</body>
</html>
