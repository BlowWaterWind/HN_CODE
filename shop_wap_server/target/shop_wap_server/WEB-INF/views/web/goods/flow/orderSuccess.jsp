<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=path%>/static/css/main.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/css/wt-sub.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/css/media-style.css" rel="stylesheet" type="text/css" />
<title>湖南移动商城</title>
<script type="text/javascript">

</script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
   <h1>${orderSub.goodsName }</h1>
  </div>
</div>
<div class="container white-bg">
  <div class="dg-suess">
    <span class="suess-icon"></span>
    <div class="suess-text">
      <span class="suess-ft cl-cs">订购成功！</span>
      <span>稍后会以短信形式提醒您开通的业务</span>
    </div>
  </div>
  <!--订单 start-->
  <div class="dg-con">
    <div class="dg-text"><span>订单号码：${orderSub.orderNos }</span><span><fmt:formatDate value="${orderSub.orderTime}"  pattern="yyyy-MM-dd HH:mm:ss" /></span></div>
    <ul class="dg-fg">
      <li>
        <span class="fd-hm">订购号码</span>
        <span class="fd-phone">${orderSub.memberPhone }</span>
      </li>
      <li>
        <span class="fd-hm">订购业务</span>
        <span class="fd-phone">${orderSub.GoodsFormat }</span>
      </li>
      <li>
        <span class="fd-hm">生效时间</span>
        <span class="fd-phone">立即生效</span>
      </li>
    </ul>
  </div>
  <!--订单 end-->
<div class="tj-btn"> <a class="btn btn-green" href="${ctx }myOrder/toMyAllOrderList">查看订单</a> <a class="btn btn-blue" href="${ctx }">继续购物</a> </div>
</div>
<!--底部菜单start-->
<!--底部菜单start-->
  <%@include file="/release/front/navBottom.jsp" %>
<!--底部菜单end-->
<!--底部菜单end--> 
</body>
</html>
