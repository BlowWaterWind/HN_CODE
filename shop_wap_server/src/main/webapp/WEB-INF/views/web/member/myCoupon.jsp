<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.constants.CouponStatusConstant" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ai.ecs.sales.entity.CouponInfo"%>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/public.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/myCoupon.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
    List<CouponInfo> couponList=(List<CouponInfo>)request.getAttribute("couponList");
    Integer status=(Integer)request.getAttribute("status");
%>
<body>
<div class="top container">
  <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>商家优惠券</h1>
     <a class="list-bt" id="delBatchBtn">删除</a> 

  </div>
</div>
<div class="container">
  <div class="yhjuan">
    <ul id="tab" class="tabList list-con affix container">
      <li data-index="0" id="allCoupon"<%if(status==0){%> class="current"<%}%>><span class="text">全部优惠券</span></li>
      <li data-index="1" id="normalCoupon" <%if(status==1){%> class="current"<%}%>><span class="text">未使用券</span></li>
      <li data-index="2" id="unUsedCoupon" <%if(status==2){%> class="current"<%}%>><span class="text">已失效券</span></li>
    </ul>
  </div>
  <ul class="s-l s-gl yhjlist">
  <%
      if(couponList!=null){
          for(CouponInfo coupon:couponList){
  %>
    <li>
      <%if(coupon.getCouponStatusId()==10||coupon.getCouponStatusId()==11){%>
      <label class="yhj-wsh yhjuang-sx">
      <%}else{%>
      <label class="yhj-wsh">
       <div class="cur-fr pull-left" >
        <input type="checkbox" class="deletebox" name="isDelete" value="<%=coupon.getCouponId() %>"/>
        </input>
      </div>
      
      <%}%>
      <div class="name">
        <div class="pull-left ">全场通用</div>
        <div class="address">使用期限：<%=DateUtils.formatDateHMS(coupon.getCouponBatchStarttime())%>- <%=DateUtils.formatDateHMS(coupon.getCouponBatchEndtime())%></div>
      </div>
      <%if(coupon.getCouponStatusId()==10){%>
      <div class="yhjbg disabled dis-cl">
      <div class="sd-td"></div>
      <%}else if(coupon.getCouponStatusId()==11){%>
      <div class="yhjbg disabled dis-cl">
      <%}else{%>
      <div class="yhjbg ">
      <%}%>
      <span class="money">￥<%=coupon.getCouponValue()%> </span><br/>满200使用</div>
      </label>
    </li>
  <%
      }}
  %>  
   
  </ul>
</div>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>

<script type="text/javascript">

var changeTabUrl="<%=request.getContextPath()%>/memberCoupon/myCoupon";
var delBtnUrl="<%=request.getContextPath()%>/memberCoupon/deleteCoupon";
</script>
</body>
</html>
