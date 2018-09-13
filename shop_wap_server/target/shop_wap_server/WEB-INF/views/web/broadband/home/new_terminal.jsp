<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_x" content="wap商城宽带首页" />
	<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
    <%--<%@include file="/WEB-INF/views/include/head.jsp" %>--%>
    <%--<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <%--<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wap.css"/>
    <%--<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>--%>
	<%--<link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />--%>
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
		<a name="tabTv"  href="${ctx}/broadbandNew/tv?type=tv&cityCode=${cityCode}">看互联网电视</a>
		<a name="tabPro" style="border-color: #490475;"  class="active"  href="${ctx}/broadbandNew/terminal?type=terminal&cityCode=${cityCode}">买智能产品</a>
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

	<div class="kd-tabs-content " id="tabPro">
		<div class="kd-box kd-box-rn">
			<p class="kd-box-title"><span>
			<c:if test="${twoFloorTitle!=null}">
				${twoFloorTitle.title}
			</c:if>
			</span></p>
			<div class="">
				<ul class="box-flex home-list">
					<c:forEach var="twoFloor" items="${twoFloor}">
						<li><a href="${twoFloor.url}">
							<img src="${tfsUrlSql}${twoFloor.background}" alt="">
							<p class="home-list-txt">${twoFloor.prodName}</p>
							<p class="home-list-price">${twoFloor.shortDesc}</p>

						</a>
							<a  class="home-list-btn" style="width: 1.6rem;" href="${twoFloor.url}">立即购买</a></li>
					</c:forEach>
					<li><a href="">
						<p class="home-list-more">...</p>
						<p class="home-list-moretxt">更多机型</p>


					</a>
					</li>
				</ul>

			</div>

		</div>

		<div class="swiper-container zn-swiper" id="znSwiper">
			<div class="swiper-wrapper">
				<div class="swiper-slide">
					<div class="kd-box kd-box-zn">
					<p class="kd-box-title"><span>
					<c:if test="${threeFloorTitle!=null}">
						${threeFloorTitle.title}
					</c:if>
					</span></p>
					<div>
						<c:forEach var="threeFloor" items="${threeFloor}">
							<div class="zn-left">
								<a href="">
									<div><img class="zn-left-img" src="${tfsUrlSql}${threeFloor.background}" alt="">
										<p class="zn-left-txt">${threeFloor.prodName}</p>
										<p class="zn-left-price">${threeFloor.shortDesc}</p></div>
								</a>
								<a  class="zn-list-btn" style="width: 1.7rem;" href="${threeFloor.url}">立即购买</a>
							</div>
						</c:forEach>
						<div class="zn-right">
							<c:forEach var="threeXFloor" items="${threeXFloor}">
								<div class="zn-right-box">
									<a class="zn-right-img"><img  src="${tfsUrlSql}${threeXFloor.background}" alt=""></a>
									<div class="zn-right-r"><a href="">
										<p class="zn-right-txt">${threeXFloor.prodName}</p>
										<p class="zn-left-price zn-left-price-r">${threeXFloor.shortDesc}</p></a>
										<a  class="zn-list-btn zn-list-btn-r" style="width: 1.7rem;" href="${threeXFloor.url}">立即购买</a>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
				</div>

			</div>

		</div>
		<div class="kd-box ">
			<p class="kd-box-title"><span>
				<c:if test="${fourFloorTitle!=null}">
						${fourFloorTitle.title}
				</c:if>
			</span></p>

		</div>
		<div class="swiper-container music-swiper" id="musSweiper">
			<div class="swiper-wrapper">
				<c:forEach var="fourFloor" items="${fourFloor}">
					<div class="swiper-slide" ><a href="">
						<div class="swiper-img-bg"><img class="swiper-img" src="${tfsUrlSql}${fourFloor.background}" alt=""></div>

						<p class="swiper-txt">${fourFloor.prodName} </p>
						<p class="swiper-price">${fourFloor.shortDesc}</p>

					</a> <a class="swiper-btn" href="">立即购买</a></div>
				</c:forEach>
			</div>


		</div>
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
	});
	$('#closeIntro').click(function(){
		$("#tvIntro").hide();
	});

</script>
</body>
</html>