<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/public.js"></script>
</head>

<script type="text/javascript">
	
	function retGoodCheck(){
		if(true){
			alert();
			return false;
		}
		return true;
	}
	
</script>
<body>
	<input type="hidden" id="serverType" name="serverType" value="${serverType}" />
	<input type="hidden" id="serverName" name="serverName"  value="${serverName}" />
	<input type="hidden" id="asServerTypeId" name="asServerTypeId" value="${asServerTypeId}"/>
	<!-- 订单号 -->
	
	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>申请退货</h1>
		</div>
	</div>
	<div class="container pd-t45">
		<!--商品 statt-->
		<div class="cur-ul sq-list">
			<div class="cur-li cur-sd">
				<p class="tabcon-cl tab-ux">
					<span class="pull-left pull-jl"><a href="订单详情.html">订单号：12015100723493012335</a></span> <span class="pull-right pull-cl">已付款</span>
				</p>
				<div class="tabcon-lr tabcon-cl02">
					<dl>
						<dt>
							<img src="demoimages/12.jpg" />
						</dt>
						<dd>
							<span><b>酷派8105</b></span> <span><b>16G</b>&nbsp;<b>白色</b></span> <span>x<b>1</b></span> <span class="sq-jg cl-red">￥699</span>
						</dd>
					</dl>
					<dl>
						<dt>
							<img src="demoimages/12.jpg" />
						</dt>
						<dd>
							<span><b>酷派8105</b></span> <span><b>16G</b>&nbsp;<b>白色</b></span> <span>x<b>1</b></span> <span class="sq-jg cl-red">￥699</span>
						</dd>
					</dl>
				</div>
				<p class="tabcon-cl tabcon-cl02 tabcon-cl04">
					<span class="pull-right pull-gd"><strong>共1件商品</strong><b><em>实付:</em>￥10.00</b></span>
				</p>
			</div>
		</div>
		<!--商品 end-->
		
		<!--退货说明 strat-->
		<div class="sm-list floor floor1">
			<div class="sm-title">
				<span class="text">退货说明：</span>
			</div>
			<div class="sm-con">
				<div class="sm-sic">
					<span class="ty-st">退货原因：</span>
					<div class="tc-radio sm-radio">
						<ul class="radio-list">
							<c:forEach items="${asReasonL}" var="rl">
								<li><label class="tabcon-radio"> 
								<input type="radio" name="sex" class="radio">${rl.aftersaleReplyReason}</label></li>
							</c:forEach>
						</ul>
						<span class="tc-title">*请选择你的退货原因</span>
					</div>
				</div>
				<div class="sm-sic">
					<span class="ty-st">退货说明：</span>
					<div class="sm-fr">
						<textarea class="ts-kuang">不能超过501字</textarea>
					</div>
				</div>
				<div class="sm-sic">
					<span>上传凭证：</span> 
					<div id="demo" class="demo">
					</div>
					
					<span class="owl-js"><i class="sm-icon"></i>图片大小不超过3M，支持GIF,JPG,PNG,BMP格式</span>
				</div>
			</div>
		</div>
		<!--退货说明 end-->
		
		<!--提交按钮 start-->
		<div class="sm-btn">
			<a class="btn btn-blue btn-2x confirmtw">提 交 </a> <a class="btn btn-blue btn-2x hide" data-toggle="modal" data-target="#myModal2">提 交 </a>
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
						<p>换货失败，我们将及时为您处理！</p>
						<p>
							换货单：<b class="font-rose">255544477</b>
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
	
	<!--退单确认弹出框 begin-->
	<div class="share-bit">
		<div class="share-text">
			<span>确定提交售后服务单信息?</span> <a class="cancel cancel-con">确认</a>
		</div>
		<div class="share-gb">
			<a class="cancel cancel-con">取消</a>
		</div>
	</div>
	<div class="more-box"></div>

	<script>
		//二次确认框
		$(document).ready(function() {
			$(".confirmtw").click(function() {
				$(".share-bit").slideDown('fast');
				$(".more-box").addClass("on");
			});
			$(".more-box").click(function() {
				$(".share-bit").slideUp('fast');
				$(".more-box").removeClass("on");
			});
			$(".cancel").click(function() {
				$(".share-bit").slideUp('fast');
				$(".more-box").removeClass("on");
			});
		});
	</script>
</body>
</html>
