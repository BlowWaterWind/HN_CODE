<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
    <style>
    	.tib-sm{ padding:6px 0 0 30px; }
    </style>
</head>

<body>
<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>固话加装</h1>
    </div>
</div>
<!--套餐选择 start-->
<div class="container">
    <div class="broadband-con">
        <div class="broadband-list">
            <div class="broadband-title"><span>宽带信息</span></div>
            <div class="broadband-info">
                <p class="border-line">宽带账号：<span>${broadbandDetailInfoResult.broadbandDetailInfo.accessAcct}</span></p>
                <ul class="broadband-box">
                    <li>
                        <label class="font-gray">套餐名称：</label><span>${broadbandDetailInfoResult.broadbandDetailInfo.discntName}</span></li>
                    <li>
                        <label class="font-gray">合约期限：</label><span>${broadbandDetailInfoResult.broadbandDetailInfo.newProductsInfo[0].start_date.substring(0,10)} ~ ${broadbandDetailInfoResult.broadbandDetailInfo.endTime.substring(0,10)}</span></li>
                    <li>
                        <label class="font-gray">安装地址：</label><span>${broadbandDetailInfoResult.broadbandDetailInfo.addrDesc}</span></li>
                    <li>
                        <label class="font-gray">装机人：</label><span>${broadbandDetailInfoResult.broadbandDetailInfo.custName}</span></li>
                    <li>
                        <label class="font-gray">联系电话：</label><span>${broadbandDetailInfoResult.broadbandDetailInfo.serialNumber}</span></li>
                </ul>
            </div>
        </div>
        <div class="broadband-list">
            <div class="broadband-title"><span>固话套餐选择</span></div>
        </div>
    </div>
    
    <!--固话套餐 start-->
    <div class="wf-list change-new sub-new">
        <div class="wf-ait clearfix">
            <ul class="bar-list">
                <li class="border-line"> 
                    <div class="sub-broad" style="height:60px;">
                    	<c:forEach items="${imsList}" var="imsItem" varStatus="status">
				  			<a href="javascript:getImsEle('${imsItem.imsItemName}','${imsItem.explain}')" class="bar-btn">${imsItem.imsItemName}</a>
				  		</c:forEach>
                    </div>
                    <div class="tib-sm">
                        <p>1、包含网内长市通话分钟数2000分钟</p>
                        <p>2、套外、网间通话均按0.1元/分钟收取</p>
                        <p>3、港澳台、国际长途按标准资费收取（默认不开通）</p>
                    </div>
                </li>
                <li> <span class="font-gray">优惠价：</span>
                    <div class="sub-text">1元/月<strong class="f12 font-red">首次办理，1元/月，优惠期12个月</strong></div>
                </li>
            </ul>
        </div>
    </div>
    <!--固话套餐 end-->
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div class="container">
            <a href="javascript:onSubmit()" class="btn btn-rose btn-block">下一步</a>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
<form method="post" action="detail">
	<input id="ims_element_id" type="hidden">
</form>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/modal.js"></script>
<script>
//切换
$(".sub-broad a").on("click", function() {
    $(this).siblings().removeClass("active");
    $(this).addClass("active");
});

function getImsEle(tip){
	$(".tib-sm").html(tip);
}

function onSubmit(){
	
}
</script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>