<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <meta name="WT.si_n" content="宽带新装" />
     <c:choose>
        <c:when test="${returnCode=='0000'}">
             <meta name="WT.si_x" content="支付成功" />
        </c:when>
        <c:otherwise>
        	 <meta name="WT.si_x" content="支付失败" />
        	 <meta name="WT.err_code" content="支付失败" />
        </c:otherwise>
     </c:choose>
    <meta name="WT.pn" content="单宽带或和家庭宽带" />
    <meta name="WT.tx_s" content="${orderSub.orderSubAmount/100}" />
    <meta name="WT.tx_i" content="${orderSub.orderSubId}" />
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>
        	<c:choose>
        		<c:when test="${returnCode=='0000'}">
        			付款成功
        		</c:when>
        		<c:otherwise>
        			付款失败
        		</c:otherwise>
        	</c:choose>
        	
        </h1>
    </div>
</div>
<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <span class="suess-img"><img src="${ctxStatic}/images/suess.png"></span><!--办理失败替换error.png-->
        <div class="suess-list">
            <c:choose>
        		<c:when test="${returnCode=='0000'}">
        			<p class="suess-tit font-blue">您的业务办理成功，感谢您的购买!</p>
		            <p>订单编号：<b class="font-rose">${orderSub.orderSubId}</b></p>
		            <p>已付金额：<b class="font-rose">￥${orderSub.orderSubAmount/100}</b></p>
        		</c:when>
        		<c:otherwise>
        			<p class="suess-tit font-blue">支付失败!</p>
        		</c:otherwise>
        	</c:choose>
            
        </div>
        <div class="tj-btn"><a class="btn btn-blue" href="${ctx}">返回首页</a> </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>