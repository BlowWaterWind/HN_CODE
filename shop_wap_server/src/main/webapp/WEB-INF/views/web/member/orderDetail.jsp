<!DOCTYPE html>
<%@page import="com.ai.ecs.order.constant.OrderConstant"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.ArrayUtils"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderRecipient" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSub" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSubDetail" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.constants.OrderStatusConstant"%>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils"%>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.constants.PayWayConstant"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/public.js"></script>
<script src="<%=request.getContextPath()%>/static/js/timecountDown.js" type="text/javascript" charset="utf-8"></script> 
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
TfOrderSub  orderview=(TfOrderSub)request.getAttribute("orderview");
StringBuilder addrStr=new StringBuilder();
if(orderview.getRecipient()!=null&&orderview.getOrderType().getOrderTypeId()!=OrderConstant.TYPE_SIM){
    TfOrderRecipient  addrObj=orderview.getRecipient();
    if(StringUtils.isNotEmpty(addrObj.getOrderRecipientProvince())){
    if(addrObj.getOrderRecipientProvince()!=null&&addrObj.getOrderRecipientProvince()!=""){
        addrObj.setOrderRecipientProvince(ArrayUtils.getLast(addrObj.getOrderRecipientProvince().split(":")));
    }
    if(addrObj.getOrderRecipientCity()!=null&&addrObj.getOrderRecipientCity()!=""){
        addrObj.setOrderRecipientCity(ArrayUtils.getLast(addrObj.getOrderRecipientCity().split(":")));
    }
    if(addrObj.getOrderRecipientCounty()!=null&&addrObj.getOrderRecipientCounty()!=""){
        addrObj.setOrderRecipientCounty(ArrayUtils.getLast(addrObj.getOrderRecipientCounty().split(":")));
    }
    addrStr.append(addrObj.getOrderRecipientName()).append(",")
    .append(addrObj.getOrderRecipientPhone()).append(",").append(addrObj.getOrderRecipientProvince())
    .append(addrObj.getOrderRecipientCity()).append(addrObj.getOrderRecipientCounty()).append(addrObj.getOrderRecipientAddress());
    }
}
%>
<body>
<div class="top container">
  <div class="header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>订单详情</h1>
   <sys:headInfo/>
  </div>
</div>
<div class="container pd-t45">
  <div class="qr-dl gray-bg">
    <div>
      <p>订单号：${orderview.orderSubNo}</p>
      <p>成交时间：<%=DateUtils.formatDateHMS(orderview.getOrderTime())%></p>
      <p class="font-rose">订单状态：<span ><%=orderview.getOrderStatus().getOrderStatusName()%></span></p>
    </div>
      <% if(orderview.getShopId()==100000002099l){ %>
      <a id="showLogistic" href="<%=request.getContextPath()%>/myOrder/to10085LogisticDetail?orderId=<%=orderview.getOrderSubId()%>&dsOrderId=<%=orderview.getThirdPartyId()%>" class="btn pull-right btn-sm btn-blue">查看物流</a> </div>
    <%if(orderview.getOrderStatusId()==12||orderview.getOrderStatusId()==15){%>
        <p><a class="btn  btn-red   dis-block mb10  w80 mlr tc " href="<%=request.getContextPath()%>/myOrder/refund10085Apply?orderSubId=<%=orderview.getOrderSubId()%>" target="_self">申请退款</a></p>
    <%}%>
    <%}else if(orderview.getLogisticsNum()!=null){%>
      <a id="showLogistic" href="<%=request.getContextPath()%>/myOrder/toLogistics?orderId=<%=orderview.getOrderSubId()%>&logisticsNum=<%=orderview.getLogisticsNum()%>" class="btn pull-right btn-sm btn-blue">查看物流</a> </div>
    <%}%>
     <div class="cur-ul cur-con">
    <div class="cur-li cur-bd">
      <p class="tabcon-cl"> <span class="pull-jl02">商品信息</span> </p>
      <%
          if(orderview.getDetailList()!=null){
           for(TfOrderSubDetail detail:orderview.getDetailList()){   
      %>
      <div class="tabcon-cl tab-pic"> <a href="${ctx}goods/<%=detail.getGoodsUrl()%>">
        <dl>
          <dt><%if(StringUtils.isNotEmpty(detail.getGoodsUrl())){ %>
          		<img src="${tfsUrl}<%=detail.getGoodsImgUrl()%>" />
          		 <%}else{ %>
           		<img src="${ctxStatic}/images/default.jpg" />
         		<%} %></dt>
          <dd>
            <h2><a href="${gUrl}<%=detail.getGoodsUrl()%>"><%=HtmlUtils.defaultString(detail.getGoodsName())%></a></h2>
            <span class="tab-zh"><%=detail.getGoodsFormat()%></span> </dd>
          <i class="dy">
          <p class="tabcon-cl tabcon-cl03">￥<%=detail.getGoodsSkuPrice()/100%></p>
          <p class="tabcon-cl tabcon-cl03 tab-lr">×<%=detail.getGoodsSkuNum()%></p>
          
          </i>
           <c:forEach items="<%=orderview.getActionMap()%>" var="map">
               <% if(orderview.getShopId()!=100000002099l&&orderview.getOrderTypeId()!=7){ %>
          <p><a class="pull-left btn btn-bd btn-sm mr10" href="<%=request.getContextPath()%>/myOrder/process?orderSubId=<%=orderview.getOrderSubId()%>&subOrderDetailId=<%=detail.getOrderSubDetailId()%>&action=${map.value.id}&act=${map.value.act}" target="_self">${map.value.name}</a></p>
               <%}else if(orderview.getOrderTypeId()!=7){%>
               <c:if test="${map.value.name=='支付'}">
                   <a class="pull-left btn btn-bd btn-sm mr10" href="http://www.10085.cn/web85/page/zyzxpay/wap_order.html?orderId=<%=orderview.getThirdPartyId()%>" target="_self">支付</a>
               </c:if>
               <%}%>
           </c:forEach>
           <% if(orderview.getOrderStatusId()==OrderStatusConstant.WAITCOMMENT.getValue()||orderview.getOrderStatusId()==OrderStatusConstant.FINISHED.getValue()){ 
           %>
            <% if(orderview.getShopId()!=100000002099l&&orderview.getOrderTypeId()!=7){ %>
        <p><a href="<%=request.getContextPath()%>/myOrder/process?action=aftersale&act=1&orderSubId=<%=orderview.getOrderSubId()%>&subOrderDetailId=<%=detail.getOrderSubDetailId()%>"  class="pull-right btn btn-bd btn-sm mr10" >申请售后</a></p>
            <%}%>
        <%}%>
           <% if(orderview.getOrderStatusId()==OrderStatusConstant.CANCELORDER.getValue()){ %>
         	已取消
         <%}%>
        </dl>
        </a> 
      </div>
        <%
           }}
      %>
    </div>
  </div>
  <%
  TfOrderRecipient orderRecipient=orderview.getRecipient();
      if(orderRecipient.getOrderRecipientAddress()!=null){
         StringBuilder addr=new StringBuilder();
         addr.append(orderRecipient.getOrderRecipientProvince()!=null?orderRecipient.getOrderRecipientProvince():"")
                 .append(orderRecipient.getOrderRecipientCity()!=null?orderRecipient.getOrderRecipientCity():"")
                 .append(orderRecipient.getOrderRecipientCounty()!=null?orderRecipient.getOrderRecipientCounty():"");
  %>
  <div class="cur-con">
    <div class="qr-dl"> <a href="#">
      <div class="pull-left">收货人：${blurryName }</div>
      <div class="pull-right">${blurryPhone }</div>
      <div class="clear"></div>
      <div class="address address-con">收货人地址：<%=addr.toString()%>${blurryAddr }</div>
      </a> </div>
  </div>
  <%
      }
  %>
  <div class="cur-con">
    <ul class=" box10 white-bg">
      <li> <span class="pull-left">付款方式：</span><span class="pull-right"><%=PayWayConstant.getByValue(Integer.parseInt(orderview.getPayModeId()+"")).getDescription()%></span></li>
      <li> <span class="pull-left">商品金额：</span><strong class="pull-right font-rose">￥${orderview.orderSubAmount/100}</strong></li>
      <li><span class="pull-left">应支付金额：</span><strong class="pull-right font-rose">￥${orderview.orderSubPayAmount/100}</strong></li>
    </ul>
  </div>
</div>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
<!--退单确认弹出框 begin-->
<div class="share-bit">
  <div class="share-text"> <span>确认删除此订单?</span> <a class="cancel cancel-con">确认</a> </div>
  <div class="share-gb"><a class="cancel cancel-con">取消</a></div>
</div>
<div class="more-box"></div>
<script>
  //二次确认框
$(document).ready(function(){
  $(".confirmtw").click(function(){
    $(".share-bit").slideDown('fast');
    $(".more-box").addClass("on");
  });
  $(".more-box").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".cancel").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  
});
</script>
</body>
</html>
