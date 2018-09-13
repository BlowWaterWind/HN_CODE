<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSubDetail" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/member/application.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/commentOrder.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/jquery.raty.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/start.js"></script>
</head>
<%
        TfOrderSubDetail  detail=(TfOrderSubDetail)request.getAttribute("orderSubDetail");
%>
<body>
<div class="top container">
  <div class="header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>评价打分</h1>
  </div>
</div>
<div class=" container ">
  <div class="cur-ul cur-con wl-con">
    <div class="cur-li">
      <div class="tabcon-cl tabcon-cl02 tabcon-cl05 wl-fr">
        <dl>
          <dt><img src="${tfsUrl}<%=detail.getGoodsImgUrl() %>"></dt>
          <dd>
            <h2><%=detail.getGoodsName()%></h2>
            <span class="tab-zh"><%=detail.getGoodsFormat()%></span> </dd>
          <i class="dy">
          <p class="tabcon-cl tabcon-cl03">￥<%=detail.getGoodsSkuPrice()%>.00</p>
          <p class="tabcon-cl tabcon-cl03 tab-lr">×<%=detail.getGoodsSkuNum()%></p>
          </i>
        </dl>
        
      </div>
    </div>
  </div>
  <form class="box-dr login-top" role="form" id="commentForm">
  <div>
    <span class="title">商品评分：</span>
    <div id="xing1" data-score="1" style="float:right;margin-right:70%;"></div>
    <input type="hidden" id="stars-input" name="orderSubId" value="<%=detail.getOrderSubDetailId()%>" size="2" />
</div>
<div >
    <span class="title">店铺态度：</span>
    <div id="xing2" data-score="1"  style="float:right;margin-right:70%;"></div>
</div>
<div >
    <span class="title">物流质量：</span>
    <div id="xing3" data-score="1"  style="float:right;margin-right:70%;"></div>
</div>
  <!--star end-->
  
  <div class="tc-lr01 tc-lr02">
    <div class="tc-radio tc-radio02">
      <textarea class="tc-text" placeholder="说点什么吧！您的评价对我们很有用" name="ratingContain"  id="ratingContain"></textarea>
    </div>
  </div>
    <span class="sp-nm">匿名评价&nbsp;<input type="checkbox" class="check-box" name="isAnonymous" id="isAnonymous" value="Y"/></input></span>
  <a href="##" class="btn btn-blue btn-block center-btn" id="submitBtn">确认评价</a> </div>
</div>
</form>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
</body>
<script type="text/javascript">
var subUrl="<%=request.getContextPath()%>/myOrder/commentOrder.json";
var gotoUrl="<%=request.getContextPath()%>/myOrder/toMyAllOrderList?flag=3";
var baseUrl="<%=request.getContextPath()%>/static";
</script>
</html>
