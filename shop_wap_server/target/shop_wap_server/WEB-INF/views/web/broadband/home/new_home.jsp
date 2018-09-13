<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_x" content="wap商城宽带首页" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wap.css"/>
	<%--<link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />--%>
	<%--<link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />--%>
	<script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/swiper.min.4.2.2.js"></script>
	<script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
    <title>宽带专区首页</title>
	<style>
		@media (min-width: 640px) {
			.modal {
				width: 640px;
				margin-left: auto;
				margin-right: auto
			}
		}
		.modal {
			position: fixed;
			top: 0;
			right: 0;
			bottom: 0;
			left: 0;
			z-index: 9999;
			display: none;
			overflow: hidden;
			-webkit-overflow-scrolling: touch;
			outline: 0;
		}
		.channel-modal {
			position: absolute;
			width: 80%;
			max-width: 640px;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%) !important;
			margin: 0 auto;
		}
		.modal-dialog {
			position: relative;
			width: auto;
			margin: 10px;
			z-index: 7;
		}
		.channel-modal .modal-info {
			background-color: #fff;
			border-radius: 4px;
			overflow: hidden;
		}
		.channel-modal .modal-info .modal-txt {
			padding: 0 10px;
			line-height: 1.7em;
			margin: 10px 0;
			max-height: 360px;
			overflow: hidden;
			overflow-y: auto;
		}
		.modal-backdrop.in, .loading-box.in {
			filter: alpha(opacity=50);
			opacity: .5 !important;
		}
		.modal-backdrop {
			z-index: 6;
		}
		.modal-backdrop, .loading-box {
			position: fixed;
			top: 0;
			right: 0;
			bottom: 0;
			left: 0;
			background-color: #000;
		}
		.modal-open .modal {
			overflow-x: hidden;
			overflow-y: auto;
		}
	</style>
</head>
<script>
	var ctx = ${ctx};
</script>
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
		<a  class="active" style="border-color: #490475;" name="tabKd" href="${ctx}/broadbandNew/home?type=broadband&cityCode=${cityCode}">办宽带</a>
		<a name="tabTv"   href="${ctx}/broadbandNew/tv?type=tv&cityCode=${cityCode}">看互联网电视</a>
		<a name="tabPro"  href="${ctx}/broadbandNew/terminal?type=terminal&cityCode=${cityCode}">买智能产品</a>
	</div>


	<div class="kd-tabs-content " id="tabKd">
		<div class="kd-banner">
			<div class="swiper-container" id="indexswiper">
				<div class="swiper-wrapper">
					<c:if test="${isLogin=='true'||isLogin==true}">
						<div class="swiper-slide">
							<img src="${ctxStatic}/demoimages/ba-bg.png" alt="">
							<div class="index-swiper-pz">
								<p>${CITY_NAME}</p>
								<ul  class="index-swiper-txt" >
									<li class="index-swiper-txt-big"><span>--${result.gprsBalance}</span>GB</li>
									<li class="index-swiper-txt-big"><span>--${result.voiceBalance}</span>分钟</li>
									<li>宽带账户<span>--${bandResult.broadbandInfoMap.accessAcct}</span></li>
									<li>总量<span>--${result.gprsTotal}</span>GB</li>
									<li>总量<span>--${result.voiceResv}</span>分钟</li>
									<li>宽带到期<span>--${bandResult.broadbandInfoMap.endTime}</span></li>
									<li>已用<span>--${result.gprsUse}</span>GB</li>
									<li>已用<span>--${result.voiceUse}</span>分钟</li>
								</ul>
							</div>
						</div>
						<c:forEach var="oneFloor" items="${oneFloor}">
							<div class="swiper-slide"> <a href="${ctx}/broadbandNew/detail?confId=${oneFloor.confId}"><img src="${tfsUrlSql}${oneFloor.background}" alt=""></a></div>
						</c:forEach>
					</c:if>
					<c:if test="${isLogin=='false'||isLogin==false}">
						<div class="swiper-slide"> <a href="${ctx}/login/toLogin"><img src="${ctxStatic}/images/login.png" alt=""></a></div>
					</c:if>

				</div>
				<!-- Add Pagination -->
				<div class="swiper-pagination"></div>
			</div>
		</div>
		<div class="kd-box">
			<a class="kd-box-area" href="javascript:;" id="citySelect">
				${CITY_NAME}&gt;</a>
			<p class="kd-box-title"><span>
			<c:if test="${twoFloorTitle!=null}">
				${twoFloorTitle.title}
			</c:if>
			</span></p>
			<c:if test="${twoFloor!=null&&fn:length(twoFloor)>0}">
				<a href="${ctx}/broadbandNew/detail?confId=${twoFloor[0].confId}"><img src="${tfsUrlSql}${twoFloor[0].background}" alt=""></a>
			</c:if>
		</div>
		<div class="kd-box">
			<p class="kd-box-title"><span>
				<c:if test="${threeFloorTitle!=null}">
					${threeFloorTitle.title}
				</c:if>
			</span></p>
			<div class="box-flex">
				<c:forEach var="threeFloor" items="${threeFloor}" end="2">
					<div class="box-box"> <a class="box-shadows" href="${ctx}/broadbandNew/detail?confId=${threeFloor.confId}">
						<p class="box-box-title">${threeFloor.title}</p>
						<p class="box-box-subtitle">${threeFloor.prodName}</p>
						<div class="box-box-bg">
							<p class="txt1"><span>${threeFloor.shortDesc}</span></p>
							<p class="txt2">${threeFloor.completeDesc}</p>
						</div>
					</a></div>
				</c:forEach>
			</div>
		</div>
		<div class="kd-box mb20">
			<p class="kd-box-title"><span>
			<c:if test="${fourFloorTitle!=null}">
				${fourFloorTitle.title}
			</c:if>
			</span></p>
			<div class="box-flex box-icon">
				<c:forEach var="fourFloor" items="${fourFloor}" end="3">
					<a href="${fourFloor.url}">
						<img src="${tfsUrlSql}${fourFloor.background}" alt="">
						<p>${fourFloor.title}</p>
					</a>
				</c:forEach>
			</div>
		</div>
		<!--广告推荐位置 -->
		<c:if test="${intro!=null}">
			<div class="tvintro" id="tvIntro">
				<div class="div-flex">
					<div class="div-flex-in"><a class="" href="javascript:;" id="closeIntro"></a>
						<a href="${ctx}/broadbandNew/detail?confId=${intro.confId}" style="position: initial;"><img  src="${tfsUrlSql}${intro.background}" alt=""></a>
					</div>
				</div>
			</div>
		</c:if>
	</div>

	</div>
	<div class="modal modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="width: 100%;" aria-hidden="true">
		<div class="modal-dialog channel-modal">
			<div class="modal-info">
				<h4 style="border-bottom: 1px solid black">区域选择</h4>
				<div class="modal-txt">
					<ul>
						<c:forEach items="${areaList}" var="areaList" varStatus="status" >
							<li onclick="selectCity('${areaList.value}')" style="text-align: center;border-bottom: 1px dashed black;">${areaList.label}</li>
						</c:forEach>
					</ul>
				</div>
			</div>
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
		$("#tvIntro").hide();
	});
	function selectCity(cityCode){
		window.location.href=ctx+"/broadbandNew/home?type=broadband&cityCode="+cityCode;
	}
	$(function(){
		$("#citySelect").click(function(){
			var isLogin = '${isLogin}';
			if(!isLogin||isLogin=='false'){
				$("#myModal2").show();
				$("#myModal2").modal();
			}
		});

	});


</script>
</body>
</html>