<!DOCTYPE html>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ai.ecs.member.entity.UserBroadbrand" %>
<%@ page import="org.springframework.util.CollectionUtils" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
 <%@include file="/WEB-INF/views/include/head.jsp" %>
<link href="<%=request.getContextPath()%>/static/css/swiper.min.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/wt-sub.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/oil.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/static/css/kd.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/nav.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/public.js"></script> 
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/tab.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/modal.js"></script>
<title>湖南移动网上营业厅</title>
</head>
<% 
	List<UserBroadbrand> brandlist = (List<UserBroadbrand>)request.getAttribute("brandList");
%>
<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>我的宽带</h1>
  </div>
</div>
<div class="container bg-gray hy-tab">
  <div class="Tab">
    <div id="box">
      <ul class="TabHead nav-tabs" id="topfloat">
        <li class="on"><a href="javascript:;">宽带账号</a></li>
        <li><a href="<%=request.getContextPath() %>/userBroadBrand/toMyAllOrderList ">我的订单</a></li>
        <li style="border:none;"><a href="javascript:;">现金券</a></li>
      </ul>
    </div>
    <div class="TabNote mt10">
      <div class="block" style="display:block;">
        <div class="wf-list sub-info">
        
        <%if(!CollectionUtils.isEmpty(brandlist)){ 
        	for(UserBroadbrand brand:brandlist){
        %>
    <div class="wf-ait clearfix">
      <div class="wf-tit wf-zt"> <a href="<%=request.getContextPath()%>/userBroadBrand/toMyBroadBrandDetail?bandId=<%=brand.getUserBroadbandId() %>">
        <div class="wf-zm"><span class="pull-left">宽带账号：<%=brand.getBroadbandAccount() %></span><span class="pull-right font-red"><%=brand.getEndTime()==null?new Date():brand.getEndTime() %>过期</span></div>
        <div class="wf-hy">
          <span class="sus-tit">合 约 期：</span>
          <span class="sus-text"><%=brand.getStartTime()==null?"":brand.getStartTime() %>~<%=brand.getEndTime()==null?"":brand.getEndTime() %></span>
        </div>
        <span class="wf-icon"></span> </a> </div>
      <div class="wf-con kd-renew-con">
        <ul class="ft-renew clearfix">
          <li><span class="pull-left"><%=brand.getBroadbandSpeed() %>M宽带</span><span class="pull-right"><%=brand.getBroadbandContract() %>年</span></li>
        </ul>
        <div class="renew-btn"><a href="网厅-宽带专区-密码修改.html" class="btn-new">修改密码</a><a href="<%=request.getContextPath() %>/broadband/linkToRenew" class="btn-new">续费</a></div>
      </div>
    </div>
 <% }} %>
 
  </div>
 </div>
    
    </div>
  </div>
</div>

</body>
</html>
