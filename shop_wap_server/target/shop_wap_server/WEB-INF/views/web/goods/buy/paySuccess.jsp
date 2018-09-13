<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSub" %>
<html>
<%
TfOrderSub orderSub=(TfOrderSub)request.getAttribute("orderSub");
%>
<head>
  <%@include file="/WEB-INF/views/include/head.jsp"%>
  <meta name="WT.si_n" content="购物流程" />
<meta name="WT.si_x" content="支付完成" />
<meta name="WT.pn_sku" content="${skuIds}" />  <!--  取值产品的id，多台手机用;隔开，必填 -->
<meta name="WT.tx_s" content="${orderAmount/100}" > <!--  取值订单总价，必填 -->
<meta name="WT.tx_u" content="${goodsBuyNum}" >  <!-- 取值手机的数量，必填 -->
<meta name="WT.tx_i" content="${orderSubNos}" >  <!-- 取值订单号，必填 -->
<meta name="WT.paytype" content="${orderSub.payMode.payModeName}" > 
<meta name="WT.tx_id" content="<%=DateUtils.formatDate(orderSub.getOrderTime()) %>" >  
<meta name="WT.tx_it" content="<%=DateUtils.formatHms(orderSub.getOrderTime()) %>" >  
  
<link rel="stylesheet" type="text/css" href="css/list.css"/>
<link rel="stylesheet" type="text/css" href="css/num-card.css"/>
<script type="text/javascript" src="${ctxStatic}/js/filter.js"></script>
</head>
<body>
<div class="top container">
  <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>支付成功</h1>
  </div>
</div>
<div class="container pd-t45 white-bg">
  <div class="gray-bd">
    <div class="payment">
      <div class="pull-left"><img src="${ctxStatic}/images/shop-images/logo_bg.png" width="40"></div>
      您的订单支付成功！<br>
      感谢您选择湖南移动商城！ </div>
    <div class="tj-btn"> 
    	<a class="btn btn-green" href="${ctx}/myOrder/toMyAllOrderList">查看订单</a>
    	<a class="btn btn-blue" href="${ctx}">继续购物</a>
    </div>
  </div>
</div>
</body>
</html>