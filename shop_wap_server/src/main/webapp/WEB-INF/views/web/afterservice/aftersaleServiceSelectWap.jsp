<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/aftersale/uploadPreview.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/aftersale/aftersale.common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/aftersale/aftersaleServiceWap.js"></script>


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
		//$('.giftResn').click()
	});


</script>
</head>
<body>
	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>售后服务</h1>
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
				<li onclick="selectDiv('repair')">申请维修</li>
				<li onclick="selectDiv('resendReceipt')">发票补寄</li>
				<li onclick="selectDiv('giftProblem')">赠品问题</li>
				<li onclick="selectDiv('complaint')">申请投诉</li>
			</ul>

			<div class="tab-content fw-tab">
				<!-- 申请维修 -->
				<div class="tab-pane" id="repairDiv">
					<form id="repairForm" action="${pageContext.request.contextPath}/afterserviceWap/aftersaleService/repairApply" method="post"
						enctype="multipart/form-data" onsubmit="return repairCheck();">
						<!-- 订单Id  -->
						<input type="hidden" name="orderSubId" value="${showOrder.orderSubId}" />
						<!-- 订单明细Id -->
						<input type="hidden" name="orderSubDetailId" value="${showOrder.detailList[0].orderSubDetailId}" />
						<!-- 订单号 -->
						<input type="hidden" name="orderSubNo" value="${showOrder.orderSubNo}" />
						
						<!--退货说明 start-->
						<div class="sm-list floor floor1">
							<div class="sm-title">
								<span class="text">申请维修</span>
							</div>
							<div class="sm-con">
								<div class="sm-sic">
									<span class="ty-st">维修原因：</span>
									<div class="tc-radio sm-radio">
										<ul class="radio-list"></ul>
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">上传图片：</span>
									<span class="uploader-btn" id="imgPreViewRep">
										<a class="btn" onclick='addImg("Rep")'>选择图片</a>
									</span>
									<p class="owl-js">最多上传3张，图片大小不超过3M，支持GIF、JPG、PNG、BMP格式</p>
								</div>
								<div class="sm-sic">
									<span class="ty-st">维修备注：</span>
									<div class="sm-fr">
										<textarea class="ts-kuang" id="repairExplain" name="repairExplain">死机、黑屏…</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="sm-btn">
							<input class="btn btn-blue btn-2x" type="submit" value="提交申请" />
						</div>
					</form>
				</div>

				<!-- hw补寄发票 -->
				<div class="tab-pane" id="resendReceiptDiv">
					<form id="resendReceiptForm" action="${pageContext.request.contextPath}/afterserviceWap/aftersaleService/resendReceiptApply"
						enctype="multipart/form-data" method="post" onsubmit="return resendReceiptCheck();">
						<!-- 订单Id  -->
						<input type="hidden" name="orderSubId" value="${showOrder.orderSubId}" />
						<!-- 订单明细Id -->
						<input type="hidden" name="orderSubDetailId" value="${showOrder.detailList[0].orderSubDetailId}" />
						<!-- 订单号 -->
						<input type="hidden" name="orderSubNo" value="${showOrder.orderSubNo}" />
						
						<div class="sm-list floor floor1">
							<div class="sm-title">
								<span class="text">补寄发票</span>
							</div>
							<div class="sm-con">
								<div class="sm-sic">
									<span class="ty-st">补寄原因：</span>
									<div class="tc-radio sm-radio">
										<ul class="radio-list"></ul>
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">上传图片：</span>
									<span class="uploader-btn" id="imgPreViewResr">
										<a class="btn" onclick='addImg("Resr")'>选择图片</a>
									</span>
									<p class="owl-js">最多上传3张，图片大小不超过3M，支持GIF、JPG、PNG、BMP格式</p>
								</div>
								<div class="sm-sic">
									<span class="ty-st">补寄说明：</span>
									<div class="sm-fr">
										<textarea class="ts-kuang" id="resendReceiptExplain" name="resendReceiptExplain">死机、黑屏…</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="sm-btn">
							<input class="btn btn-blue btn-2x" type="submit" value="提交申请" />
						</div>
					</form>
				</div>
				<!--补寄 end-->

				<!-- 赠品问题 -->
				<div class="tab-pane" id="giftProblemDiv">
					<form id="giftProblemForm" action="${pageContext.request.contextPath}/afterserviceWap/aftersaleService/giftProblemApply" method="post"
						enctype="multipart/form-data" onsubmit="return giftProblemCheck();">
						<input type="hidden" name="orderSubId" value="${showOrder.orderSubId}" />
						<input type="hidden" name="orderSubDetailId" value="${showOrder.detailList[0].orderSubDetailId}" />
						<input type="hidden" name="orderSubNo" value="${showOrder.orderSubNo}" />

						<div class="sm-list floor floor1">
							<div class="sm-title">
								<span class="text">赠品问题</span>
							</div>
							<div class="sm-con">
								<div class="sm-sic">
									<span class="ty-st" >赠品类型：</span>
									<div class="tc-radio sm-radio">
										<ul class="radio-list giftTypeRadio">
											<li><label class='tabcon-radio'>
												<input type='radio' name='giftType' class='radio' value='9'/>实物赠品
											</label></li>
											<li><label class='tabcon-radio'>
												<input type='radio' name='giftType' class='radio' value='7'/>流量优惠
											</label></li>
											<li><label class='tabcon-radio'>
												<input type='radio' name='giftType' class='radio' value='6'/>话费优惠
											</label></li>
										</ul>
									</div>
								</div>
								<!-- 申请原因 -->
								<div class="sm-sic giftResn">

								</div>
								<div class="sm-sic">
									<span class="ty-st">上传图片：</span>
									<span class="uploader-btn" id="imgPreViewGifp">
										<a class="btn" onclick='addImg("Gifp")'>选择图片</a>
									</span>
									<p class="owl-js">最多上传3张，图片大小不超过3M，支持GIF、JPG、PNG、BMP格式</p>
								</div>
								<div class="sm-sic">
									<span class="ty-st">赠品备注：</span>
									<div class="sm-fr">
										<textarea class="ts-kuang" id="giftProblemExplain" name="giftProblemExplain">死机、黑屏…</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="sm-btn">
							<input class="btn btn-blue btn-2x" type="submit" value="提交申请" />
						</div>
					</form>
				</div>

				<!-- hw投诉管理 -->
				<div class="tab-pane" id="complaintDiv">
					<form id="complaintForm" action="${pageContext.request.contextPath}/afterserviceWap/aftersaleService/complaintApply" method="post"
						enctype="multipart/form-data" onsubmit="return complaintCheck();">
						<!-- 订单Id  -->
						<input type="hidden" name="orderSubId" value="${showOrder.orderSubId}" />
						<!-- 订单明细Id -->
						<input type="hidden" name="orderSubDetailId" value="${showOrder.detailList[0].orderSubDetailId}" />

						<!--退货说明 start-->
						<div class="sm-list floor floor1">
							<div class="sm-title">
								<span class="text">投诉说明</span>
							</div>
							<div class="sm-con">
								<div class="sm-sic">
									<span class="ty-st">退款原因：</span>
									<div class="tc-radio sm-radio">
										<ul class="radio-list"></ul>
									</div>
								</div>
								<div class="sm-sic">
									<span class="ty-st">上传图片：</span>
									<span class="uploader-btn" id="imgPreViewComp">
										<a class="btn" onclick='addImg("Comp")'>选择图片</a>
									</span>
									<p class="owl-js">最多上传3张，图片大小不超过3M，支持GIF、JPG、PNG、BMP格式</p>
								</div>
								<div class="sm-sic">
									<span class="ty-st">投诉备注：</span>
									<div class="sm-fr">
										<textarea class="ts-kuang" id="complaintExplain" name="complaintExplain">死机、黑屏…</textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="sm-btn">
							<input class="btn btn-blue btn-2x" type="submit" value="提交申请" />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	</div>
	<!--服务类型 end-->
	</div>
</body>
</html>
