<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.member.vo.WangTingInfoResult" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta name="format-detection" content="telephone=no">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/man-center.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/rem.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/circle.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>
<div class="center-top container"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
  
  <%if(request.getAttribute("memberId")!=null){%>
<a href="<%=request.getContextPath()%>/login/logout" class="recot">注销</a>
<%}else{%>
<a href="<%=request.getContextPath()%>/login/toLogin" class="recot">登录</a>
<%}%>
<sys:headInfo cartTop="2"/>
  <div class="usr-img"> <img src="<%=request.getContextPath()%>/static/images/member/shop-images/mt_user.png" class="pl-img" />
    <dl>
      <!--<dt><img src="images/shop-images/user_pic.png"></dt>-->
      <dd>
        <p>${nickName}</p>
        <p>您好，${mobile} 欢迎来到湖南移动商城！</p>
      </dd>
    </dl>
  </div>
  <ul class="container menu urers-menu clearfix">
  <li><a href="<%=request.getContextPath()%>/myOrder/toMyAllOrderList"><i class="icon-center-pay"></i>全部订单<span class="badge">${orderCount.waitPayCount}</span></a></li>
  <li><a href="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=1"><i class="icon-center-car"></i>待发货<span class="badge">${orderCount.waitSendCount}</span></a></li>
  <li><a href="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=2"><i class="icon-center-box"></i>待收货<span class="badge">${orderCount.waitReceiveCount}</span></a></li>
  <%--<li><a href="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=3"><i class="icon-center-talk"></i>待评价<span class="badge">${orderCount.waitCommentCount}</span></a></li>--%>
  </ul>
</div>
<div class="center-body container">
  <ul class="grid-9 clearfix">
    <li><a href="<%=request.getContextPath()%>/myOrder/toMyAllOrderList"><i class="icon-center-ddlist"></i>我的订单</a></li>
	<li><a href="<%=request.getContextPath()%>/myOrder/toMyGroupOrderList"><i class="icon-center-shop"></i>集团订单</a></li>
    <li><a href="<%=request.getContextPath()%>/memberFavorite/toFavoriteGoods"><i class="icon-center-love"></i>我的关注</a></li>
    <li><a href="<%=request.getContextPath()%>/memberCoupon/myCoupon"><i class="icon-center-juan"></i>我的优惠券</a></li>
    <li><a href="<%=request.getContextPath()%>/integration/toIntegrationSelect"><i class="icon-center-said"></i>我的积分</a></li>
    <li><a href="<%=request.getContextPath()%>/memberAddress/toMemberAddressList"><i class="icon-center-adds"></i>地址管理</a></li>
    <li><a href="<%=request.getContextPath()%>/memberInfo/toSetMemberInfo"><i class="icon-center-usr"></i>个人信息</a></li>
    <li><a href="<%=request.getContextPath()%>/afterserviceWap/aftersaleService/toAftersaleServiceCenter"><i class="icon-center-fw"></i>售后服务</a></li>
  </ul>
 <%--     <c:if test="${flag=='true'}">
  <div class="urer-center-title">移动业务</div>
  <div class="mobile-bg-content">
    <div class="fl plans-bg">
      <dl>
        <dt>
          <div class="circle-blue">
            <div class="circle-wrap">
              <div class="hold hold1">
                <div class="pie pie1" style="transform: rotate(<%=HtmlUtils.defaultNumber(wtInfo.getVoiceBalanceWeight())%>deg);"></div>
              </div>
              <div class="hold hold2">
                <div class="pie pie2"></div>
              </div>
              <div class="bg"></div>
              <div class="circle"><span><small>剩余</small><b><%=HtmlUtils.defaultNumber(wtInfo.getVoiceBalanceWeight())%>%</b><em><%=HtmlUtils.defaultNumber(wtInfo.getVoiceBalance())%></em>Min</span></div>
            </div>
          </div>
        </dt>
        <dd> <span class="font-gray">通话时长</span> <span><%=HtmlUtils.defaultNumber(wtInfo.getVoiceResv())%>小时</span> </dd>
      </dl>
    </div>
    <div class="fl plans-bg">
      <dl>
        <dt>
          <div class="circle-green ">
            <div class="circle-wrap">
              <div class="hold hold1">
                <div class="pie pie1" style="transform: rotate(<%=HtmlUtils.defaultNumber(wtInfo.getGprsBalanceWeight())%>deg);"></div>
              </div>
              <div class="hold hold2">
                <div class="pie pie2"></div>
              </div>
              <div class="bg"></div>
              <div class="circle"><span><small>剩余</small><b><%=HtmlUtils.defaultNumber(wtInfo.getGprsBalanceWeight())%>%</b><em><%=HtmlUtils.defaultNumber(wtInfo.getGprsBalance())%></em>MB</span></div>
            </div>
          </div>
        </dt>
        <dd> <span class="font-gray">流量</span> <span><%=HtmlUtils.defaultNumber(wtInfo.getGprsTotal())%>MB</span> </dd>
      </dl>
    </div>
    <div class="fl plans-bg">
      <dl>
        <dt>
          <div class="circle-rose ">
            <div class="circle-wrap">
              <div class="hold hold1">
                <div class="pie pie1" style="transform: rotate(<%=HtmlUtils.defaultNumber(wtInfo.getFeeBalanceWeight())%>deg);"></div>
              </div>
              <div class="hold hold2">
                <div class="pie pie2"></div>
              </div>
              <div class="bg"></div>
              <div class="circle"><span><small>剩余</small><b><%=HtmlUtils.defaultNumber(wtInfo.getFeeBalanceWeight())%>%</b><em><%=HtmlUtils.defaultNumber(wtInfo.getFeeBalance())%></em>元</span></div>
            </div>
          </div>
        </dt>
        <dd> <span class="font-gray">话费</span> <span><%=HtmlUtils.defaultNumber(wtInfo.getRealFee())%>元</span> </dd>
      </dl>
    </div>
  </div>
 
  <ul class="grid-9 clearfix">
    <li><a href="<%=UserUtils.wantingServerHost%>doBusiness/spOperation.html"><i class="icon-center-ddlist"></i>我的账户</a></li>
    <li><a href="<%=UserUtils.wantingServerHost%>doBusiness/dataFlowQuery.html"><i class="icon-center-shop"></i>我的话费</a></li>
    <li><a href="<%=UserUtils.wantingServerHost%>doBusiness/monthBillQuery.html"><i class="icon-center-love"></i>我的账单</a></li>
    <li><a href="<%=UserUtils.wantingServerHost%>doBusiness/dataFlowQuery.html"><i class="icon-center-juan"></i>我的套餐余量</a></li>
    <li><a href="<%=UserUtils.wantingServerHost%>doBusiness/detailBillQuery.html"><i class="icon-center-said"></i>我的详单</a></li>
  </ul>
  </c:if> --%>
</div>
<!--底部菜单start-->
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
<!--底部菜单end-->
<script>
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
</script>
</body>
</html>
