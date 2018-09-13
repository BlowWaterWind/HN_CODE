<!DOCTYPE html>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSub" %>
<%@ page import="com.ai.ecs.entity.base.Page" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSubDetail" %>
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
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/iscroll.js"></script>  --%>
<script type="text/javascript" src="${ctxStatic}/js/iscroll-probe.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/myWaitPayOrder.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
    Page<TfOrderSub> orderPage=(Page<TfOrderSub>)request.getAttribute("orderviewList");
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
  <li id="waitPay"><span class="text">全部订单<span class="badge">${orderCount.waitPayCount}</span></span></li>
  <li id="waitSend"><span class="text">待发货<span class="badge">${orderCount.waitSendCount}</span></span></li>
  <li id="waitReceive"><span class="text">待收货<span class="badge">${orderCount.waitReceiveCount}</span></span></li>
  <%--<li id="waitComment"  class="current"><span class="text">待评价<span class="badge">${orderCount.waitCommentCount}</span></span></li>--%>
  </ul>
</div>
<div class="container container-con">  
  <div id="content">
   <div id="scroller">
       <div id="pullDown" class="" style="display:block;">
           <div class="pullDownIcon"></div>
           <div class="pullDownLabel">下拉刷新</div>
       </div>
       
     <div class="tabCon tabcon-list">
    <div class="cur-sd" id="pageDiv">
    <%
    if(orderViewList!=null){
        for(TfOrderSub orderView:orderViewList){
    %>
      <ul style="display:block;" class="cur-ul">
        <li class="cur-li">
          <p class="tabcon-cl"><!-- <span class="tabcon-icon"><input type="checkbox" /></span> --><span class="pull-left pull-jl">
          <a class="orderDetailBtn" href="#" data-index="<%=orderView.getOrderSubId()%>"><span class="dd-number">订单号：</span><%=orderView.getOrderSubNo()%></a></span> 
          <span class="pull-right pull-cl"><%=orderView.getOrderStatus().getOrderStatusName()%></span></p>
          <%
          if(orderView.getDetailList()!=null){
          for(TfOrderSubDetail subDetail:orderView.getDetailList()){
          %>
          
          <div class="tabcon-lr tabcon-cl02">
           <a class="orderDetailBtn" href="#" data-index="<%=subDetail.getOrderSubId()%>">
            <dl>
             <dt><a href="${gUrl}<%=subDetail.getGoodsUrl()%>">
               <%if(StringUtils.isNotEmpty(subDetail.getGoodsUrl())){ %>
            <img src="${tfsUrl}<%=subDetail.getGoodsImgUrl()%>" />
            <%}else{ %>
             <img src="${ctxStatic}/images/default.jpg" />
            <%} %>
            </a></dt>
              <dd>
                <h2><a href="${gUrl}<%=subDetail.getGoodsUrl()%>"><%=HtmlUtils.defaultString(subDetail.getGoodsName())%></a></h2>
                <span class="tab-zh"><%=subDetail.getGoodsFormat()%></span>
                  <%
                      if(orderView.getShopId()==100000002099l){
                  %>
                  <p><font color="red"> 此订单为10085订单</font></p>
                  <%
                      }
                  %>
              </dd>
              <div class="dy">
                <p class="tabcon-cl tabcon-cl03"><%=subDetail.getGoodsSkuPrice()/100%>.00</p>
                <p class="tabcon-cl tabcon-cl03 tab-lr">×<%=subDetail.getGoodsSkuNum()%></p>
              </div>
            </dl>
            </a>
          </div>
          <p class="tabcon-cl tabcon-cl02 tabcon-cl04"></p>
          <p><a  data-index="<%=subDetail.getOrderSubDetailId()%>" class="pull-right btn btn-bd btn-sm commentOrderBtn">订单评价</a></p>

            <% if(orderView.getShopId()==100000002099l&&orderView.getOrderTypeId()!=7){ %>
            <p> <a class="btn btn-sm btn-bd pull-right logisticBtn" href="<%=request.getContextPath()%>/myOrder/to10085LogisticDetail?orderId=<%=orderView.getOrderSubId()%>&dsOrderId=<%=orderView.getThirdPartyId()%>" >查看物流</a></p>
            <%}else if(orderView.getOrderTypeId()!=7){%>
            <p><a  data-index="<%=subDetail.getOrderSubId()%>" data-logisticsNum="<%=orderView.getLogisticsNum() %>" class="pull-right btn btn-bd btn-sm mr10 logisticBtn">查看物流</a></p>
            <%}%>
          <%
          }}
          %>
          	 <span class="sm-date pull-right font-gray"><%=DateUtils.formatDateHMS(orderView.getOrderTime()) %></span>
            <span class="pull-left pull-gd"><strong>共<%=orderView.getDetailList()==null?0:orderView.getDetailList().size()%>件商品</strong><b><em>实付:</em>￥<%=orderView.getOrderSubPayAmount()/100.0%></b></span> </p>
         
        </li>
    </ul>
    <%  }}%>
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

<script>
var basePath="<%=request.getContextPath()%>";
var payUrl="<%=request.getContextPath()%>/myOrder/payOrder";
var cancelUrl="<%=request.getContextPath()%>/myOrder/cancelOrder.json";
var gotoUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=3";
var waitPayUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList";
var waitSendGoodsUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=1";
var waitReceiveGoodsUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=2";
var waitCommentUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=3";
var logisticUrl="<%=request.getContextPath()%>/myOrder/toLogistics";
var backGoodsUrl="<%=request.getContextPath()%>/myOrder/toRefund";
var deleteOrderUrl="<%=request.getContextPath()%>/myOrder/deleteOrder.json";
var commentOrderUrl="<%=request.getContextPath()%>/myOrder/toCommentOrder";
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
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/waitGoodsOrderScroll.js"></script>
</body>
</html>
