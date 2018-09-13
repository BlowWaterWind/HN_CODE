<!DOCTYPE html>
<%@page import="com.ai.ecs.order.constant.OrderConstant"%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils"%>
<%@ page import="com.ai.ecs.member.entity.MemberVo"%>
<%@ page import="com.ai.ecs.order.entity.TfOrderSub" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSubDetail" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderRecipient" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.constants.OrderStatusConstant"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css" />
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
TfOrderSub  orderSub=(TfOrderSub)request.getAttribute("orderSub");
if(orderSub==null){
    orderSub=new TfOrderSub();
}
List<TfOrderSubDetail> detailList=new ArrayList<TfOrderSubDetail>();
if(orderSub!=null){
    detailList=orderSub.getDetailList();
}
StringBuilder addrStr=new StringBuilder();
if(orderSub.getRecipient()!=null&&orderSub.getOrderType().getOrderTypeId()!=OrderConstant.TYPE_SIM){
    TfOrderRecipient  addrObj=orderSub.getRecipient();
    addrStr.append(addrObj.getOrderRecipientName()).append(",")
    .append(addrObj.getOrderRecipientPhone()).append(",").append(addrObj.getOrderRecipientProvince())
    .append(addrObj.getOrderRecipientCity()).append(addrObj.getOrderRecipientCounty()).append(addrObj.getOrderRecipientAddress());
}
%>
<body>
<div class="top container">
  <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>查看物流</h1>
    <a href="#" class="icon-right icon-lr02"></a> <a href="<%=request.getContextPath()%>/goods/linkToUserGoodsCarList" class="icon-top-cart icon-cart02"></a>
  </div>
     <sys:headInfo/>
</div>
<div id="outer" class="container">
  <div class="container">
    <div class="wl-kt">
      <dl>
        <dd>
          <h2>${orderSub.logisticsCompany} </h2>
          <span>运单编号：${orderSub.logisticsNum} </span>  </dd>
      </dl>
    </div>
    <div class="cur-ul cur-con wl-con">
      <div class="cur-li">
        <div class="tabcon-cl tabcon-cl02 tabcon-cl05 wl-fr">
        <c:forEach items="${orderSub.detailList}" var="item" varStatus="iter">
        <dl>
        <dt>
 			 <c:if test="${item.goodsImgUrl!=null}">
          		 <img  src="${tfsUrl}${item.goodsImgUrl}" /> 
         	</c:if>
          	 <c:if test="${item.goodsImgUrl==null}">
           		<img src="${ctxStatic}/images/default.jpg" />
        		</c:if>
		</dt>
        <dd>
          <h2> ${item.goodsName}</h2>
          <span class="tab-zh"> ${item.goodsFormat}</span> </dd>
        <i class="dy">
        <p class="tabcon-cl tabcon-cl03">￥${item.goodsSkuPrice/100}</p>
        <p class="tabcon-cl tabcon-cl03 tab-lr">× ${item.goodsSkuNum}</p>
        </i>
      </dl>
          </c:forEach>
        </div>
      </div>
    </div>
    <div class="wl-box">
      <div class="cul-li">
        <p class="tabcon-cl"> <span class="pull-jl02">物流跟踪</span> </p>
      
        <c:forEach items="${logisticList}" var="logistic" varStatus="iter">
        <div class="wl-ad <c:if test="${iter.count==fn:length(logisticList)}">  wl-green </c:if> ">
          <i class="wl-icon active"></i>
          <div class="wl-mes"><span>${logistic.scantype}&nbsp;&nbsp;${logistic.remark}${logistic.acceptaddress}${logistic.opcode}</span> <small>${logistic.time}</small></div>
        </div>
        </c:forEach>   
        
      </div>
    </div>
    <div class="white-bg white-con"></div>
  </div>
</div>

<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
</body>
</html>
