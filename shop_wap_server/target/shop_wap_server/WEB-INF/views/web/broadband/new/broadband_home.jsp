<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--<meta name="WT.si_n" content="wap商城宽带首页" />
    <meta name="WT.si_x" content="wap商城宽带首页" />--%>
	<%--插码相关--%>
	<meta name="WT.si_n" content="KD_SY" />
	<meta name="WT.si_x" content="20" />
	<Meta name="WT.mobile" content="">
	<Meta name="WT.brand" content="">

	<%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>
	<link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/list_oil.js"></script> 
	<script type="text/javascript" src="${ctxStatic}/js/swiper.min.js"></script> 
	<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/index-login.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
    <%--<script type="text/javascript" src="${ctxStatic}/js/qm/sdc9.js"></script>--%>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>宽带专区首页</title>
    <script type="text/javascript">
		var  ctx = '${ctx}';
		$(function(){
			var bussinessRecommend = '${bussinessRecommendJson}';
			if(bussinessRecommend!=''){
				$("#myModal1").modal();
			}
		})

	</script>
</head>

<body>
	<div class="top container">
		<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
		    <h1>宽带专区</h1>
		    <!-- 如果没有登录，请加hide样式隐藏 -->
<%-- 		   	<a href="#" class="header-kd-right"><img src="${ctxStatic}/images/kd/item.png" /></a> --%>
		</div>
	</div>
	<!-- swiper Begin -->
	<div class="swiper-container container kd-banner" id="swiper-index01">
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
	

			<!-- floor1 Begin -->
			<div class="container floor floor1  floor8">
				<input name="accessAcct" hidden id="accessAcct">
				<div class="content"> </div>
			</div>
			<!-- floor1 End --> 
	<!-- floor2 Begin -->
	<div class="container kd-common mt10 white-bg">
		<h2><a class="pull-right" href="#"></a>极光宽带</h2>
		<ul class="kd-new-list clearfix">
			<c:forEach items="${newInstallInfo}" var="broadbandItem" varStatus="status" >
				<c:if test="${broadbandItem.posId == '1-1'}">
					<li><a  class="kd-new-list-text01" href="${broadbandItem.url}" otitle="新和家庭办理" otype="button">${broadbandItem.posterTitle}</a>
					   <a href="${broadbandItem.url}" otitle="新和家庭办理" otype="button"><img class="kd-new-list-img01" src="${broadbandItem.imgUrl}" /></a>
					</li>            		
				</c:if>
			</c:forEach>
			<li>
				<c:forEach items="${newInstallInfo}" var="broadbandItem" varStatus="status" >
					<c:if test="${broadbandItem.posId == '1-2'}">
						<div class="kd-new-list-2"><a href="${broadbandItem.url}"><img class="kd-new-list-img02"  src="${broadbandItem.imgUrl}"/></a>
						<a  class="kd-new-list-text02" href="${broadbandItem.url}">${broadbandItem.posterTitle}</a></div>        		
					</c:if>
					<c:if test="${broadbandItem.posId == '1-3'}">
						<div class="kd-new-list-2"><a href="${broadbandItem.url}"><img class="kd-new-list-img02"  src="${broadbandItem.imgUrl}"/></a>
						<a  class="kd-new-list-text02" href="${broadbandItem.url}">${broadbandItem.posterTitle}</a></div>    		
					</c:if>
				</c:forEach>
			</li>            		
		</ul>
	</div>
	<!-- floor2 End --> 
	
	<!--数字家庭 start-->
     <div class="container kd-common mt10 white-bg">
        <h2><a class="pull-right" href="digital-family">更多</a>数字家庭</h2>
        <ul class="kd-new-list-com kd-new-list-com-4 clearfix">
            <c:forEach items="${digitalFamilyInfo}" var="broadbandItem" varStatus="status" >
            	<li><a href="${broadbandItem.url}"><img src="${broadbandItem.imgUrl}"/></a><a href="#">${broadbandItem.posterTitle}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!--数字家庭 end-->

	<!-- floor3 Begin -->
	<div class="container kd-common mt10 white-bg">
		<h2>宽带服务</h2>
		<ul class="kd-new-list-com kd-new-list-com-4 clearfix">
			<c:forEach items="${broadbandServiceInfo}" var="broadbandItem" varStatus="status" >
				<c:choose>
					<c:when test="${broadbandItem.posId == '3-4'}">
						<li><a href="${broadbandItem.url}${broadbandInfoMap.accessAcct}" data-title="${broadbandItem.posterTitle}"><img src="${broadbandItem.imgUrl}"/></a><a href="${broadbandItem.url}${broadbandInfoMap.accessAcct}" data-title="${broadbandItem.posterTitle}">${broadbandItem.posterTitle}</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${broadbandItem.url}" data-title="${broadbandItem.posterTitle}"><img src="${broadbandItem.imgUrl}" /></a><a href="${broadbandItem.url}" data-title="${broadbandItem.posterTitle}">${broadbandItem.posterTitle}</a></li>
					</c:otherwise>
				</c:choose>
				<%--<li><a href="${broadbandItem.url}"><img src="${broadbandItem.imgUrl}"/></a><a href="${broadbandItem.url}">${broadbandItem.posterTitle}</a></li>--%>
			</c:forEach>
		</ul>
	</div>
	<!--输入密码弹框 start-->
	<div class="modal modal-prompt" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog channel-modal">
			<div class="modal-info">
				<h4>业务推荐</h4>
				<div class="modal-txt">
					<div class="wf-list change-info sub-new">
						<div class="wf-ait clearfix">
							<dl>
								<dt style="margin-top: 5px;"><img src="${ctxStatic}/images/kd/broad_info.jpg"/></dt>
								<dd>
									<c:if test="${bussinessRecommend!=null}">
										<h5>${bussinessRecommend.bussinessName}</h5>
										<p>${bussinessRecommend.bussinessFee}元/月</p>
										<p>${bussinessRecommend.bussinessDesc}</p>
										<%--<p>${bussinessRecommend.bussinessHref}</p>--%>
									</c:if>
								</dd>
							</dl>
						</div>
					</div>				</div>
				<div class="modal-btn">
					<a href="javascript:void(0);" data-href="${bussinessRecommend.bussinessHref}" data-dismiss="modal" onclick="redirctFun(this)">立即办理</a>
					<%--<button id="handleSpeedUp" class="btn btn-rose btn-block" data-dismiss="modal">立即办理</button>--%>
				</div>
			</div>
		</div>
	</div>
	<script>//轮播JS

	  var mySwiper = new Swiper('#swiper-index01',{
	    pagination: '#swiper-index01 .pagination',
		loop:true,
		grabCursor: true,
		autoplay:3000,
		autoplayDisableOnInteraction:false,
	  });
	function redirctFun(param){
		var href = $(param).attr("data-href");
//		var phone = $("#phone").val();
		window.location.href = ctx+href;
	}
	</script>
</body>
</html>