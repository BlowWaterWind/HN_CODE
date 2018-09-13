<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
</head>

<body>
	<input type="hidden" id="loginPhoneNum" name="loginPhoneNum" value="${installPhoneNum}" />
	
	<c:set value="宽带专区" var="titleName" ></c:set>
	<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
	
	<!-- swiper Begin -->
	<div class="swiper-container swiper-banner container" id="swiper-index01">
	  <div class="swiper-wrapper">
	  	<c:forEach items="${posterList}" var="poster" varStatus="status" >
		    <div class="swiper-slide">
		    	<a href="${poster.redirectUrl}"><img otitle="${poster.picName}" otype="ad" oarea="${poster.picName}" src="${poster.picUrl}"/></a>
		    </div>
	    </c:forEach>
	  </div>
	  <div class="pagination"></div>
	</div>
	<!-- swiper End --> 
	
	<%--<div class="container floor floor1 floor8"> <a href="#" class="title"> <span class="text">宽带专区</span> </a>
	  <div class="content"> <a href="网厅-宽带专区-宽带新装.html" class="left-floor"><img src="demoimages/kd01.png" /></a> <a href="网厅-宽带专区-宽带续费列表页.html" class="up-floor"><img  src="demoimages/kd02.png" /></a> <a href="网厅-宽带专区-宽带提速列表页.html" class="up-floor"><img src="demoimages/kd03.png" /></a> </div>
	</div>--%>
	
	<div class="container floor floor1 floor8"> 
	  <a href="#" class="title"> <span class="text">宽带办理</span> </a>
	  <div class="content floor-line">
	       <%--<a  onclick="javascript:;"  class="box-lt "> <img src="${ctxStatic}/demoimages/fy001.png"  otitle="0元宽带" otype="ad" oarea="宽带预约宽带办理"/></a>--%>
	    <a  onclick="broadbandReservationTrade(this)"  class="box-lt "> <img src="${ctxStatic}/demoimages/fy001.png" otitle="0元宽带" otype="ad" oarea="宽带预约宽带办理"/></a> 
	    <div class="box-rt">
	        <a href="${ctx}consupostn/init?busiType=1&minCost=48"><img src="${ctxStatic}/demoimages/fy10.png"  otitle="宽带包年" otype="ad" oarea="宽带预约宽带办理"/></a>
	        <div class="box-dw">
	            <a href="javascript:;" class="box-line" ><img  src="${ctxStatic}/demoimages/fy03.png"  otitle="老客户续费" otype="ad" oarea="宽带预约宽带办理"/></a>
<%--<a href="${ctx}/broadband/linkToRenew" class="box-line" ><img  src="${ctxStatic}/demoimages/fy03.png"  otitle="老客户续费" otype="ad" oarea="宽带预约宽带办理"/></a> --%>
<a ><img src="${ctxStatic}/demoimages/fy04.png"  otitle="老客户提速" otype="ad" oarea="宽带预约宽带办理"/></a>
</div>
</div>
</div>
</div>

<!--资源查询 start-->
<div class="container zy-rearch mt10 "><a href="${ctx}/bandResourceQuery/toResourceQueryPage"><img src="${ctxStatic}/demoimages/xf.png"  otitle="资源查询" otype="button" oarea="宽带专区"/></a></div>
<!--资源查询 end-->
<!-- floor3 Begin -->
<div class="container floor floor1 floor8"> <a href="#" class="title"> <span class="text">贴心服务</span> </a>
<div class="content">
<ul class="add_oil kd-w-3 kd-w-4" >
<%-- <li><a href="${ctx}broadband/linktoBookInstall" otitle="宽带预约" otype="button" oarea="宽带专区贴心服务">宽带预约</a></li> --%>
<%-- <li><a href="${ctx}broadband/ap_lyh" otitle="老用户专区" otype="button" oarea="宽带专区贴心服务">老用户专区</a></li> --%>
<li><a href="${ctx}broadbandAccount/accQry" otitle="账号查询" otype="button" oarea="宽带专区贴心服务">账号查询</a></li>
<li><a href="${ctx}broadbandAccount/accReset" otitle="密码修改" otype="button" oarea="宽带专区贴心服务">密码修改</a></li>   
<li><a href="${ctx}broadband/serviceStandard" otitle="宽带服务" otype="button" oarea="宽带专区贴心服务">宽带服务</a></li>
<li><a href="${ctx}broadband/broadSelfHelp" otitle="自助排障" otype="button" oarea="宽带专区贴心服务">自助排障</a></li>
</ul>
</div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/list_oil.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/swiper.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadbandInstall.js"></script>
<script>//轮播JS
var mySwiper = new Swiper('#swiper-index01',{
pagination: '#swiper-index01 .pagination',
loop:true,
grabCursor: true,
autoplay:3000,
autoplayDisableOnInteraction:false
});
</script>
</body>
</html>