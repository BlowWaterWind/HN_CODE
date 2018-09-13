<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/list_oil.js"></script> 
	<script type="text/javascript" src="${ctxStatic}/js/swiper.min.js"></script> 
	<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>首页</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
	<div class="top container">
		<div class="header"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
	    	<h1>宽带预约</h1>  
	  	</div>
	</div>
	
	<div class="container ">
		<div class="swiper-container  broadcast-swiper" id="swiper">
		    <div class="swiper-wrapper">
		    	<c:forEach items="${carouselList}" var="carousel" varStatus="status" >
					<div class="swiper-slide"><a href="${carousel.url}"><img src="${carousel.imgUrl}" /></a></div>
				</c:forEach>
		    </div>
		    <div class="pagination"></div>
		</div>
	</div>
	
	<div class=" container bg-white mt10 ">
	    <div class="p10 border-bottom">
	       <a class="font-9 fs14 pull-right" href="#">更多&gt;&gt;</a>
	        <h3 class="fs16"><i class="index-icon-hot-broadcast"></i>热门活动</h3>
	    </div>
	    <ul class="common-list common-list-3 text-center mt10 clearfix">
	    	<c:forEach items="${hotActivityList}" var="hotActivity" varStatus="status" >
		    	<li class="pb10 plr5">
			    	<a class="dis-block" href="#"><img src="${hotActivity.imgUrl}" /></a>
			    	<p class="fs16 text-center lh30">${hotActivity.posterTitle}</p>
			    	<p class="font-blue text-center fs14 lh30">${hotActivity.feeInfo}</p>
			    	<a class="btn btn-blue w-80per" href="${hotActivity.url}">${hotActivity.btnName}</a>
			    </li>
			</c:forEach>
	    </ul>
	</div>
	
	<div class=" container bg-white mt10 ">
	    <div class="p10 border-bottom">
	        <h3 class="fs16"><i class="index-icon-hot-broadcast"></i>热销商品</h3>
	    </div>
	    
	    <div class="p10"> 
	    	<c:forEach items="${hotGoodsList}" var="hotGoods" varStatus="status" >
		    	<div class="clearfix border-bottom pb10 mt10">
				   	<div class="pull-left w90"><a class="dis-block" href="${hotGoods.url}"><img src="${hotGoods.imgUrl}"/></a> </div>
				    <div class="pull-right">
				   		<a class="btn btn-blue plr10 mt30" href="${hotActivity.url}">${hotActivity.btnName}</a>
				   	</div>
				   	<div class="ml100">
					   	<p class="fs16 lh30">${hotActivity.posterTitle}</p>
					   	<p class="line-txt">${hotActivity.intro}</p>
					    <p class="font-blue  fs14 lh30">${hotActivity.feeInfo}</p>
				   	</div>
			   	</div>
			</c:forEach>
	   	</div>
	</div>
	
	<script>//轮播JS
	  var mySwiper = new Swiper('#swiper',{
	    pagination: '.pagination',
		loop:true,
		grabCursor: true,
		autoplay:3000,
		autoplayDisableOnInteraction:false,
	  });
	</script> 
</body>
</html>