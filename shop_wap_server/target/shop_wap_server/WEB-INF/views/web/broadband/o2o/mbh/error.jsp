<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
	<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
   		<h1>魔百和</h1>
  	</div>
</div>
<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <span class="suess-img"><img src="${ctxStatic}/images/error.png"></span>
        <div class="suess-list"><p class="suess-tit font-blue">${errorInfo}</p></div>
	</div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>