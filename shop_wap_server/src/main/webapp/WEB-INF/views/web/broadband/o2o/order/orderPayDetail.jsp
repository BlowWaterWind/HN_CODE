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
        <h1>支付详情</h1>
    </div>
</div>
<div class="container">
    <div class="order-detail">
        <!--店铺信息 start-->
        <div class="shop-info mt10">
            <div class="shop-title">
                <a href="javascript:void(0);">
                    <span class="shop-icon">${orderSub.shopName}</span><span class="channel-blue">${orderSub.orderStatus.orderStatusName}</span>
                </a>
            </div>
            <!--产品说明 start-->
            <div class="shop-gobal">
                <a href="javascript:void(0)">
                    <%--<img src="images/bus09.png" />--%>
                    <h4>${orderSub.goodsName}</h4>
                    <p class="shop-txt">&nbsp;</p>
                    <div class="shop-bottom"><span class="price"><fmt:formatNumber value="${orderSub.orderSubPayAmount/100}" pattern="#.##"/>元</span><span>x1</span></div>
                </a>
            </div>
            <!--产品说明 end-->
            <!--完成订单时间 start-->
            <div class="des-date">
                <p>支付流水号：<span>${orderPay.payNumber}</span></p>
                    <p>订单编号：<span>${orderPay.orderId}</span></p>
                    <p>支付时间：<span><fmt:formatDate value="${orderPay.orderPayTime}" pattern="yyyy-MM-dd hh:mm:ss"/></span></p>
                    <p>结算时间：<span><fmt:formatDate value="${orderPay.settleDate}" pattern="yyyy-MM-dd"/></span></p>
                    <p>支付方式：<span>
                    ${orderPay.orgName}
                    </span></p>
                    <p>支付金额：<span><fmt:formatNumber value="${orderPay.orderPayAmount/100}" pattern="#.##"/>元</span></p>
                    <p>实际支付金额：<span><fmt:formatNumber value="${orderPay.orderPayAmount/100}" pattern="#.##"/>元</span></p>
                    <p>支付币种：<span>人民币</span></p>
                    <p>支付状态：<span>
                        <c:choose>
                            <c:when test="${'SUCCESS' eq orderPay.payState}">
                                成功
                            </c:when>
                            <c:when test="${'FAILED' eq orderPay.payState}">
                                失败
                            </c:when>
                            <c:otherwise>
                                未支付
                            </c:otherwise>
                        </c:choose>
                    </span></p>
            </div>
            <!--完成订单时间 end-->
        </div>
        <!--店铺信息 end-->

    </div>
</div>
</body>
</html>
