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
			<h3 class="navbar-title">宽带专区</h3>


		</div>
	</div>
	<div class="kd-tabs">
		<a  name="tabKd" href="${ctx}/broadbandNew/home?type=broadband&cityCode=${cityCode}">办宽带</a>
		<a name="tabTv" class="active" style="border-color: #490475;"  href="${ctx}/broadbandNew/tv?type=tv&cityCode=${cityCode}">看互联网电视</a>
		<a name="tabPro"  href="${ctx}/broadbandNew/terminal?type=terminal&cityCode=${cityCode}">买智能产品</a>
	</div>
	<div class="kd-banner">
		<div class="swiper-container" id="indexswiper">
			<div class="swiper-wrapper">
				<c:if test="${isLogin=='false'||isLogin==false}">
					<div class="swiper-slide"> <a href="${ctx}/login/toLogin"><img src="${ctxStatic}/images/login.png" alt=""></a></div>
				</c:if>
				<c:if test="${oneFloor==null||oneFloor==''}">
					<div class="swiper-slide">
						<img src="${ctxStatic}/demoimages/ba-bg.png" alt="">
						<div class="index-swiper-pz">
						</div>
					</div>
				</c:if>
				<c:forEach var="oneFloor" items="${oneFloor}">
					<div class="swiper-slide"> <a href="${oneFloor.url}"><img src="${tfsUrlSql}${oneFloor.background}" alt=""></a></div>
				</c:forEach>
				<%--<div class="swiper-slide"> <img src="${ctxStatic}/demoimages/ba-bg.png" alt=""></div>--%>
				<%--<div class="swiper-slide"> <img src="${ctxStatic}/demoimages/ba-bg.png" alt=""></div>--%>

			</div>
			<!-- Add Pagination -->
			<div class="swiper-pagination"></div>
		</div>
	</div>

	<div class="kd-tabs-content" id="tabTv">
		<div class="kd-box mb20">

			<div class="box-flex box-icon box-icon-tv">
				<c:forEach var="twoFloor" items="${twoFloor}">
					<a href="">
						<img src="${tfsUrlSql}${twoFloor.background}" alt="">
						<p>${twoFloor.prodName}</p>
					</a>
				</c:forEach>
			</div>
		</div>
		<div>
			<c:if test="${threeFloor!=null&&fn:length(threeFloor)>0}">
				<a href="${threeFloor[0].url}"><img src="${tfsUrlSql}${threeFloor[0].background}" alt=""></a>
			</c:if>
		</div>
		<div class="tv-title">
			<c:if test="${fourFloorTitle!=null}">
				<a href="${fourFloorTitle.url}">${fourFloorTitle.title}&gt;</a>
			</c:if>
			<p>重磅推荐</p>
		</div>
		<ul class="tv-list box-flex  box-flex-wrap">
			<c:forEach var="fourFloor" items="${fourFloor}">
				<li><a href=""><img src="${tfsUrlSql}${fourFloor.background}" alt="">
					<p class="tv-list-title">${fourFloor.prodName}</p>
					<p class="tv-list-subtitle">${fourFloor.shortDesc}</p></a></li>
			</c:forEach>
		</ul>
		<!--广告推荐位置 -->
		<c:if test="${intro!=null}">
			<div class="tvintro" id="tvIntro">
				<div class="div-flex">
					<div class="div-flex-in"><a class="" href="javascript:;" id="closeIntro"></a>
						<a href="${intro.url}" style="position: initial;"><img  src="${tfsUrlSql}${intro.background}" alt=""></a>
					</div>
				</div>
			</div>
		</c:if>
	</div>
	</div>

</div>

</div>
<script type="text/javascript">
	var swiper = new Swiper('#znSwiper', {
		slidesPerView: 'auto',
		spaceBetween: 15,

		observer:true,
		observeParents:true

	});
	var myswiper = new Swiper('#musSweiper', {
		observer:true,
		observeParents:true,
		effect: 'coverflow',
		grabCursor: true,
		slidesPerView: 3,
		initialSlide :1,
		centeredSlides: true,
		slidesPerView: 'auto',
		coverflowEffect: {
			rotate: 30,
			stretch: 20,
			depth: 10,
			modifier: 1,
			slideShadows : true,
		}

	});
	var indexswiper = new Swiper('#indexswiper', {
		slidesPerView: 'auto',
		spaceBetween: 10,
		centeredSlides: true,
		loop : true,
		pagination: {
			el: '.swiper-pagination',
		},
	});

	$('.kd-tabs a').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		var _id=$(this).attr('name');
		$('.kd-tabs-content ').hide();
		$('#'+_id).show();
	})
	$('#closeIntro').click(function(){
		console.log("111");
		$("#tvIntro").hide();
	})

</script>
</body>
</html>