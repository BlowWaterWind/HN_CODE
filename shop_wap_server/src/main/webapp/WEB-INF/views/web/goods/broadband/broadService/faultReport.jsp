<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="WT.si_n" content="故障申告" />
    <meta name="WT.si_x" content="业务详情" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
</head>

<body>

<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>宽带服务</h1>
  </div>
</div>
<div class="container">
   <div class="lc-content">
   	 <img src="${ctxStatic}/images/broadService/lctu03.jpg" />
   </div>
</div>
<!--底部菜单 start-->
<div class="fix-br">
<div class="affix foot-box">
 <ul class="footer-list container">
 	 <li><a href="${ctx}broadband/serviceStandard">服务标准</a></li>
 	 <li><a href="${ctx}broadband/installProcedure">安装流程</a></li>
 	 <li class="active"><a href="javascript:">故障申告</a></li>
	</ul>
<!--底部菜单 end-->
</div>
</div>
<script>
	$(function (){
    $('.footer-list li').click(function (){
      $('.active').removeClass('active');
      $(this).addClass("active");
   });
})
</script>

</body>
</html>