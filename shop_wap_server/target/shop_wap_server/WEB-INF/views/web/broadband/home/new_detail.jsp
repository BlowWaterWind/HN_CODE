<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_x" content="wap商城宽带首页" />
	<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wap.css"/>
	<script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/swiper.min.4.2.2.js"></script>
	<script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
    <title>宽带专区首页</title>
</head>

<body>
<div class="container">
	<div class="kd-navbar-wrap">
		<div class="kd-navbar ">
			<div class="navbar-left">
				<a href="javascript:history.back();" class="arrow-left"></a>
			</div>
			<h3 class="navbar-title">${broadbandConf.prodName}</h3>


		</div>
	</div>

	<div class="common-img">
		<c:if test="${broadbandConf.imgDetail1!=null&&broadbandConf.imgDetail1!=''}">
			<img src="${tfsUrlSql}${broadbandConf.imgDetail1}" alt="">
		</c:if>
		<c:if test="${broadbandConf.imgDetail2!=null&&broadbandConf.imgDetail2!=''}">
			<img src="${tfsUrlSql}${broadbandConf.imgDetail2}" alt="">
		</c:if>
		<c:if test="${broadbandConf.imgDetail3!=null&&broadbandConf.imgDetail3!=''}">
			<img src="${tfsUrlSql}${broadbandConf.imgDetail3}" alt="">
		</c:if>
		<c:if test="${broadbandConf.imgDetail4!=null&&broadbandConf.imgDetail4!=''}">
			<img src="${tfsUrlSql}${broadbandConf.imgDetail4}" alt="">
		</c:if>
	</div>

	<div class="kd-navbar-wrap navbar-wrap-footer ">
		<div class="kd-navbar ">
			<a class="btn-bl" href="${broadbandConf.url}?productName=${broadbandConf.prodName}">立即办理</a>
		</div>
	</div>
</div>
</body>
</html>