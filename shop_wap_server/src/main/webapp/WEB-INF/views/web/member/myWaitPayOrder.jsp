<!DOCTYPE html>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.constants.OrderStatusConstant"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSub" %>
<%@ page import="com.ai.ecs.entity.base.Page" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSubDetail" %>
<%@ page import="com.ai.ecs.order.constant.OrderConstant" %>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/changetab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/public.js"></script>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/iscroll.js"></script> --%>
<script type="text/javascript" src="${ctxStatic}/js/iscroll-probe.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/myWaitPayOrder.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/hebao.js?version=11"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
    Page<TfOrderSub> orderPage=(Page<TfOrderSub>)request.getAttribute("orderviewList");
    Boolean heBaoLogin = (Boolean)request.getAttribute("heBaoLogin");
    if (heBaoLogin == null) {
        heBaoLogin = false;
    }
    List<TfOrderSub> orderViewList= null;
    if(orderPage!=null){    
        orderViewList=orderPage.getList();
       if(orderViewList==null){
           orderViewList=new ArrayList<TfOrderSub>();
       }
    }else{
        orderPage=new  Page<TfOrderSub>();
    }
%>
<body>
<div class="top container">
  <div class="header sub-title"><!--<a class="icon-left icon-home icon2"></a>--><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>我的订单</h1>
    <sys:headInfo/>
  </div>
</div>
<div id="outer" class="container">
  <ul id="tab" class="tabList list-con container">
    <li id="waitPay" class="current"><span class="text">全部订单<span class="badge">${orderCount.waitPayCount}</span></span></li>
    <li id="waitSend"><span class="text">待发货<span class="badge">${orderCount.waitSendCount}</span></span></li>
    <li id="waitReceive"><span class="text">待收货<span class="badge">${orderCount.waitReceiveCount}</span></span></li>
     <%-- <li id="waitReceive"><span class="text"><a href="${ctx}recmd/phoneSimExchange">号卡查询</a></span></li>--%>
    <%--<li id="waitComment"><span class="text">待评价<span class="badge">${orderCount.waitCommentCount}</span></span></li>--%>
  </ul>
</div>
<div class="container container-con">  
  <div id="content" >
  <div id="scroller">
       <div id="pullDown" class="" style="display:block;">
           <div class="pullDownIcon"></div>
           <div class="pullDownLabel">下拉刷新</div>
       </div>
       
       <div class="tabCon tabcon-list tab-fdd">
      <div class="cur-sd"  id="pageDiv">
       <%
       if(orderViewList!=null){
          for(TfOrderSub orderView:orderViewList){
       %>
           <ul style="display:block;" class="cur-ul">
               <li class="cur-li">
                   <p class="tabcon-cl">
                       <!-- <span class="tabcon-icon">
                           <input type="checkbox" />
                       </span> -->
	                   <span class="pull-left pull-jl">
	                       <a class="orderDetailBtn" href="#" data-index="<%=orderView.getOrderSubId()%>">
	                           <span class="dd-number">订单号：</span><%=orderView.getOrderSubNo()%>
	                       </a>
	                   </span> 
                       <span class="pull-right pull-cl"><%=orderView.getOrderStatus().getOrderStatusName()%></span>
                   </p>
                   <%
                   if(orderView.getDetailList()!=null){
                       for(TfOrderSubDetail subDetail:orderView.getDetailList()){
                   %>
          
                   <div class="tabcon-lr tabcon-cl02">
                       <a class="orderDetailBtn" href="#" data-index="<%=subDetail.getOrderSubId()%>">
	                       <dl>
	                           <dt>
	                               <a href="${gUrl}<%=subDetail.getGoodsUrl()%>">
  									<%if(StringUtils.isNotEmpty(subDetail.getGoodsUrl())){ %>
          							  <img src="${tfsUrl}<%=subDetail.getGoodsImgUrl()%>" />
          							  <%}else{ %>
           							  <img src="${ctxStatic}/images/default.jpg" />
         							   <%} %>
            
									</a>
	                           </dt>
	                           <dd>
	                               <h2>
	                                   <a href="${gUrl}<%=subDetail.getGoodsUrl()%>"><%=HtmlUtils.defaultString(subDetail.getGoodsName())%></a>
	                               </h2>
	                               <span class="tab-zh"><%=subDetail.getGoodsFormat()%></span>
                                   <%if(orderView.getShopId()==100000002099l){%>
                                   <p><font color="red"> 此订单为10085订单</font></p>
                                   <%}%>
	                           </dd>
	                           <div class="dy">
	                               <p class="tabcon-cl tabcon-cl03"><%=(double) (Math.round(subDetail.getGoodsSkuPrice()*10000/100)/10000.0)%></p>
	                               <p class="tabcon-cl tabcon-cl03 tab-lr">×<%=subDetail.getGoodsSkuNum()%></p> 
	                           </div>
                               <%if(null==orderView.getOldOrderNum()&&orderView.getOrderTypeId()!=7){ %>
	                           <c:forEach items="<%=orderView.getActionMap()%>" var="map">
	                               <p>
                                       <% if(orderView.getShopId()!=100000002099l){
                                            if((orderView.getOrderTypeId()==6 || orderView.getOrderTypeId()==8) && orderView.getOrderStatusId() == 12) {
                                       %>
                                            &nbsp;
                                       <% } else {%>
                                            <a class="pull-right btn btn-bd btn-sm" href="<%=request.getContextPath()%>/myOrder/process?orderSubId=<%=orderView.getOrderSubId()%>&subOrderDetailId=<%=subDetail.getOrderSubDetailId()%>&action=${map.value.id}&act=${map.value.act}" target="_self">${map.value.name}</a>
                                       <%}%>
                                       <%} else  {%>
                                           <c:if test="${map.value.name=='支付'}">
                                               <a class="pull-right btn btn-bd btn-sm" href="http://www.10085.cn/web85/page/zyzxpay/wap_order.html?orderId=<%=orderView.getThirdPartyId()%>" target="_self">支付</a>
                                           </c:if>
                                       <%}%>

                                   </p>
	                           </c:forEach>

                               <%
                                    if (heBaoLogin) {
                                    if (orderView.getPromotionId() != null && !"".equals(orderView.getPromotionId())) {  // 活动订单
                                    if (OrderConstant.PAY_STATUS_ALREADY.equals(orderView.getOrderSubPayStatus())) { // 已支付
                               %>
                                    <p><a class="pull-right btn btn-bd btn-sm" href="javascript:void(0);" onclick="shareGetFlow('<%=orderView.getOrderSubId()%>');">
                                        <%
                                            if(OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_11.getValue().equals(orderView.getPromotionId())) {
                                        %>
                                            分享
                                        <%
                                            } else {
                                        %>
                                            分享获取流量
                                        <%
                                            }
                                        %>


                                    </a></p>
                               <% }}} %>

	          
	                           <% 
	                               if(orderView.getOrderStatusId()==OrderStatusConstant.WAITCOMMENT.getValue()||orderView.getOrderStatusId()==OrderStatusConstant.FINISHED.getValue()){ 
	                           %>
                                   <% if(orderView.getShopId()!=100000002099l && null!=orderView.getOrderTypeId() && orderView.getOrderTypeId()!=6
                                           && orderView.getOrderTypeId()!=8 && !"Y".equals(orderView.getIsFinancialPackage()) && orderView.getOrderTypeId()!=16 && orderView.getOrderTypeId()!=17){ %>
                                       <p>
                                           <a href="<%=request.getContextPath()%>/myOrder/process?action=aftersale&act=1&orderSubId=<%=orderView.getOrderSubId()%>&subOrderDetailId=<%=subDetail.getOrderSubDetailId()%>"  class="pull-right btn btn-bd btn-sm mr10" >申请售后</a>
                                       </p>
                                   <%}%>
	                           <%}%>
	                           <% if(orderView.getOrderStatusId()==OrderStatusConstant.CANCELORDER.getValue()){ %>
	         	                                                 已取消
	                           <%}%>
                               <%}%>
	                       </dl>
            
                       </a>
            
                   </div>
                   <p class="tabcon-cl tabcon-cl02 tabcon-cl04">

                   <%
                       }}
                   %>
                   	 <span class="sm-date pull-right font-gray"><%=DateUtils.formatDateHMS(orderView.getOrderTime()) %></span>
                   <span class="pull-left pull-gd"><strong>共<%=orderView.getDetailList()==null?0:orderView.getDetailList().size()%>件商品</strong><b><em>实付:</em>￥<%=(double) (Math.round(orderView.getOrderSubPayAmount()*10000/100)/10000.0)%></b></span> 
                   </p>
               </li>
           </ul>
       <%}}%>
       </div>
 </div> 
   
	    <div id="pullUp" class="ub ub-pc c-gra load" style="display: block;">
	        <div class="pullUpIcon"></div>
	        <div class="pullUpLabel">加载中...</div>
	    </div> 
    
    </div>
    </div>
</div> 
<!--底部菜单start-->
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>

<!--退单确认弹出框 begin-->
<div class="share-bit">
  <div class="share-text"> <span>确认删除此订单?</span> <a class="cancel cancel-con">确认</a> </div>
  <div class="share-gb"><a  class="cancel cancel-con">取消</a></div>
</div>
<div class="more-confirm"></div>
<input type="hidden" name="flag" id="flag" value="${flag}" />
<input type="hidden" name="pageNo" id="pageNo" value="${orderviewList.pageNo}" />
<input type="hidden" name="pageSize" id="pageSize" value="${orderviewList.pageSize}" />
<input id="lastPage" name="page.lastPage" type="hidden" value="${orderviewList.lastPage}"/>
<script type="text/javascript">
var basePath="<%=request.getContextPath()%>";
var payUrl="<%=request.getContextPath()%>/myOrder/payOrder";
var cancelUrl="<%=request.getContextPath()%>/myOrder/cancelOrder.json";
var gotoUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList";
var waitPayUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList";
var waitSendGoodsUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=1";
var waitReceiveGoodsUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=2";
var waitCommentUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=3";
var logisticUrl="<%=request.getContextPath()%>/myOrder/toLogistics";
var backGoodsUrl="<%=request.getContextPath()%>/myOrder/toRefund";
var orderDetailUrl="<%=request.getContextPath()%>/myOrder/toOrderDetail";
var myOrderUrl="<%=request.getContextPath()%>/myOrder/searchOrderPage";

  //二次确认框
$(document).ready(function(){
  $(".confirmtw").click(function(){
    $(".share-bit").slideDown('fast');
    $(".more-confirm").addClass("on");
  });
  $(".more-confirm").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-confirm").removeClass("on");
  });
  $(".cancel").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-confirm").removeClass("on");
  });
  
});
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/orderScroll.js"></script>
</body>
</html>
