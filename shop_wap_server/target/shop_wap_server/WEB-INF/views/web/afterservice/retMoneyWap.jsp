<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page language="java" import="java.util.*"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>湖南移动商城</title>
<link href="<%=request.getContextPath()%>/static/css/main.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/media-style.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/oil.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/nav.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/modal.js"></script>
</head>

<script type="text/javascript">
	$(document).ready(function() {
		$(".more-box").click(function() {
			$(".share-bit").slideUp('fast');
			$(".more-box").removeClass("on");
		});
		$(".cancel").click(function() {
			$(".share-bit").slideUp('fast');
			$(".more-box").removeClass("on");
		});
		$(".radio").click(function(){
			$(".radio").removeClass("active");
			$(this).addClass("active");
			$(".radio").removeAttr("checked");
			$(this).attr("checked", "checked");
		});
	});
	// 验证表单 > 整合表单 > ajax异步图片上传 > 提交表单
	function formSubmit() {
		var juge = retMoneyCheck();
		//表单验证通过
		if(juge===true){
			$(".share-bit").slideDown('fast');
		    $(".more-box").addClass("on");
		}
	}
	
	function retMoneyCheck() {
		var theCheckboxInputs = $('input[name="retMoneyReason"]');
		for (var i = 0; i < theCheckboxInputs.length; i++) {
			if (theCheckboxInputs[i].checked)
				return true;
		}
		$('.radio-list span').remove();
		$('.radio-list').after('<span class="tc-title">*请选择你的退款原因</span>');
		return false;
	}
</script>

<body>
	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>申请退款</h1>
		</div>
	</div>
	<div class="container pd-t45">
		<!--商品 start-->
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
		<!--商品 end-->

		<!--退款说明 start-->
		<div class="sm-list floor floor1">
			<div class="sm-title">
				<span class="text">退款说明</span>
			</div>
			<div class="sm-con">
				<form id="retMoneyForm" action="${pageContext.request.contextPath}/afterserviceWap/retMoney/retMoneyApply" 
					method="post">
					<!-- 订单Id  -->
					<input type="hidden" name="subOrderId" value="${showOrder.orderSubId}" />
					<!-- 订单明细Id -->
					<input type="hidden" name="subOrderDetailId" value="${showOrder.detailList[0].orderSubDetailId}" />
					<!-- 退款金额 -->
					<input type="hidden" name="retMoneyCount" value="${maxretMony/100}"/>
					<div class="sm-sic">
						可退金额 :<b class="cl-red">&nbsp;${maxretMony/100}&nbsp;</b>元
					</div>
					<!-- <div class="sm-sic">退款方式：原路退回</div> -->
					<div class="sm-sic">
						<span class="ty-st">退款原因：</span>
						<div class="tc-radio sm-radio">
							<ul class="radio-list">
								<c:forEach items="${asReasonL}" var="rl">
									<li>
										<label class="tabcon-radio"> 
											<input type="radio" name="retMoneyReason" class="radio" value="${rl.aftersaleReplyReason}">${rl.aftersaleReplyReason}
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--退款end-->
		
		<!--提交按钮 start-->
		<div class="sm-btn" >
			<a class="btn btn-blue btn-2x" onclick="formSubmit()">提 交 </a>
		</div>
		<!--提交按钮 end-->
	</div>

	<!-- 提交成功弹出框 start-->
	<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-top">
			<div class="modal-content">
				<div class="card4g-popup card4g-success">
					<div class="icon">
						<div class="line_short"></div>
						<div class="line_long"></div>
					</div>
					<div class="popup-text ts-fr">
						<p>感谢您提交的退货单，我们将及时为您处理！</p>
						<p>
							退货单：<b class="font-rose">255544477</b>
						</p>
						<p>尊敬的湖南移动集团用户，您的订单已经在处理中，稍后会有短信或经理人联系您。</p>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<!-- 提交成功弹出框 end-->

	<!-- 提交失败弹出框 start-->
	<div class="modal fade modal-prompt" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-top">
			<div class="modal-content">
				<div class="card4g-popup card4g-lose">
					<div class='icon'>
						<div class='icon_box'>
							<div class='line_left'></div>
							<div class='line_right'></div>
						</div>
					</div>
					<div class="popup-text ts-fr">
						<p>退货失败，我们将及时为您处理！</p>
						<p>
							退货单：<b class="font-rose">255544477</b>
						</p>
						<p>尊敬的湖南移动集团用户，您的订单已经在处理中，稍后会有短信或经理人联系您。</p>
					</div>
				</div>
				<div class="sm-btn">
					<a class="btn btn-blue btn-2x">确 认</a>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<!-- 提交失败弹出框 end-->

	<!--退款确认弹出框 begin-->
	<div class="share-bit" >
		<div class="share-text">
			<span>确定提交退款信息?</span> <a class="cancel cancel-con" onclick='$("#retMoneyForm").submit();'>确认</a>
		</div>
		<div class="share-gb">
			<a class="cancel cancel-con">取消</a>
		</div>
	</div>
	<div class="more-box"></div>
	<!--退款确认弹出框 end-->
</body>
</html>
