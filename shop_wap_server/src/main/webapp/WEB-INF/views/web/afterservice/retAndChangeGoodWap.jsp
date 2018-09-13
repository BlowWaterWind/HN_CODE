<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="imgUploadViewTemplate.jsp"%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>

<link href="<%=request.getContextPath()%>/static/css/main.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/media-style.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/nav.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/modal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/public.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/changetab.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/aftersale/uploadPreview.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/aftersale/aftersale.common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/aftersale/retAndChangeGoodWap.js"></script>


<script type="text/javascript">
	//二次确认框
	$(document).ready(function() {
		$(".more-box").click(function() {
			$(".share-bit").slideUp('fast');
			$(".more-box").removeClass("on");
		});
		$(".cancel").click(function() {
			$(".share-bit").slideUp('fast');
			$(".more-box").removeClass("on");
		});
	});
	
	//允许退货的最大数量
	var canServerNum = '${allowRetNum}'*1; 
	//var canServerNum = 10;
	//子订单详情中商品数量 
	var initSkuNum = '${showOrder.detailList[0].goodsSkuNum}'*1;
	//sku价格
	var skuPrice = '${showOrder.detailList[0].goodsSkuPrice/100}'*1;
	//子订单总支付金额
	var payPrice = '${showOrder.orderSubPayAmount/100}'*1;
</script>

</head>
<body>
	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>退换货服务</h1>
		</div>
	</div>

	<div class="container pd-t45">
		<!--商品信息 start-->
		<div class="cur-ul cur-con">
			<div class="cur-li cur-bd cur-ft">
				<div class="tabcon-cl tab-pic">
					<a href="#">
						<dl>
							<dt>
								<img src="${tfsUrl}${showOrder.detailList[0].goodsImgUrl}">
							</dt>
							<dd>
								<h2>${showOrder.detailList[0].goodsName}</h2>
								<span class="tab-zh">
									<em>${showOrder.detailList[0].goodsFormat }</em>
								</span>
								<!--<c:if test="${!empty showOrder.orderSubDiscountAmount}"></c:if>-->
								<span class="tab-zh">优惠金额：￥${showOrder.orderSubDiscountAmount==0?0:showOrder.orderSubDiscountAmount/100}元</span>
								<span class="tab-zh">支付金额：￥${showOrder.orderSubPayAmount/100}元</span>
							</dd>
							<i class="dy">
								<p class="tabcon-cl tabcon-cl03">￥${showOrder.detailList[0].goodsSkuPrice/100}元</p>
								<p class="tabcon-cl tabcon-cl03 tab-lr">×${showOrder.detailList[0].goodsSkuNum}</p>
							</i>
						</dl>
					</a>
				</div>
			</div>
		</div>
		<!--商品信息 end-->

		<!--服务类型 start-->
		<div class="fw-lx mt10">
			<h4>服务类型</h4>
			<ul class="fw-list" id="mytabs">
				<li onclick="selectDiv('retGood')">退货</li>
				<li onclick="selectDiv('changeGood')">换货</li>
			</ul>
			<div class="tab-content fw-tab">
				<!-- 退货 -->
				<div class="tab-pane" id="retGoodDiv">
					<form id="retGoodForm" action="${pageContext.request.contextPath}/afterserviceWap/retGood/retGoodApply" method="post"
						enctype="multipart/form-data" onsubmit="return retGoodCheck();">
						<!-- 订单Id  -->
						<input type="hidden" name="orderSubId" value="${showOrder.orderSubId}" />
						<!-- 订单明细Id -->
						<input type="hidden" name="orderSubDetailId" value="${showOrder.detailList[0].orderSubDetailId}" />

						<!--退货说明 start-->
						<div class="sm-list floor floor1">
							<div class="sm-title">
								<span class="text">退货说明</span>
							</div>
							<div class="sm-con">
								<div class="sm-sic">
									<label>退款金额：</label>
									<b class="cl-red">￥${initPrice/100}</b>元
									<input type="hidden"  name="returnPrice" value="${initPrice/100}"/>
								</div>
								<div class="sm-sic">
									<label class="sm-nubmer">退货数量：</label>
									<div id="numCag">
										<input class="min" name="minus" type="button" value="-" />
										<input class="text_box" name="retGoodNum" type="text" value="1" />
										<input class="min" name="add" type="button" value="+" />
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">退货原因：</span>
									<div class="tc-radio sm-radio">
										<ul class="radio-list"></ul>
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">退货备注：</span>
									<div class="sm-fr">
										<textarea class="ts-kuang" name="retGoodExplain" id="retGoodExplain">死机、黑屏…</textarea>
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">上传图片：</span>
									<span class="uploader-btn" id="imgPreViewRetg">
										<a class="btn" onclick='addImg("Retg")'>选择图片</a>
									</span>
									<p class="owl-js">最多上传3张，图片大小不超过3M，支持GIF、JPG、PNG、BMP格式</p>
								</div>
							</div>
						</div>
						<div class="sm-btn">
							<input class="btn btn-blue btn-2x" type="submit" />
						</div>
					</form>
				</div>

				<!-- hw换货 -->
				<div class="tab-pane" id="changeGoodDiv">
					<!--换货说明 strat-->
					<form id="changeGoodForm" action="${pageContext.request.contextPath}/afterserviceWap/changeGood/changeGoodApply" method="post"
						enctype="multipart/form-data" onsubmit="return changeGoodCheck();">
						<!-- 订单Id  -->
						<input type="hidden" name="orderSubId" value="${showOrder.orderSubId}" />
						<!-- 订单明细Id -->
						<input type="hidden" name="orderSubDetailId" value="${showOrder.detailList[0].orderSubDetailId}" />	
						
						<div class="sm-list floor floor1">
							<div class="sm-title">
								<span class="text">换货说明</span>
							</div>
							<div class="sm-con">
								<div class="sm-sic">
									<label class="sm-nubmer">换货数量：</label>
									<div id="numCag1">
										<input class="min" name="minus" type="button" value="-" />
										<input class="text_box" name="changeGoodNum" type="text" value="1" />
										<input class="min" name="add" type="button" value="+" />
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">换货原因：</span>
									<div class="tc-radio sm-radio">
										<ul class="radio-list"></ul>
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">换货备注：</span>
									<div class="sm-fr">
										<textarea class="ts-kuang" id="changeGoodExplain" name="changeGoodExplain">死机、黑屏…</textarea>
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">上传图片：</span>
									<span class="uploader-btn" id="imgPreViewCag">
										<a class="btn" onclick='addImg("Cag")'>选择图片</a>
									</span>
									<p class="owl-js">最多上传3张，图片大小不超过3M，支持GIF、JPG、PNG、BMP格式</p>
								</div>								
							</div>
						</div>
						<div class="sm-btn">
							<input class="btn btn-blue btn-2x" type="submit" />
						</div>
					</form>
					<!--换货说明 end-->
				</div>
			</div>
		</div>
		<!--服务类型 end-->
	</div>
</body>
</html>
