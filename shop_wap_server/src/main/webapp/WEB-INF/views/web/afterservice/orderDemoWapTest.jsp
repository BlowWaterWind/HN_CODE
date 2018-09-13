<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/nav.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/modal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/public.js"></script>
<script type="text/javascript">
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

	function submitDeal(serverName) {
		$("#div" + serverName + " form").submit();
	}
</script>

<!-- 测试数据和链接 -->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
//测试数据
String subOrderId = "179885627209154560";
//String subOrderDetailId = "179885627216494592";
String subOrderDetailId = "179885627216494593";
String retMoneyUrl = basePath+"/afterservice/retMoney/retMoneyUI?subOrderId="+subOrderId+"&&subOrderDetailId="+subOrderDetailId;
String retAndChangeGoodUrl = basePath+"/afterservice/retGood/retAndChangeGoodUI?subOrderId="+subOrderId+"&&subOrderDetailId="+subOrderDetailId;
String aftersaleServiceUrl = basePath+"/afterservice/aftersaleServer/toAftersaleServiceUI?subOrderId="+subOrderId+"&&subOrderDetailId="+subOrderDetailId;
%>


</head>
<body>

	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>订单详情</h1>
		</div>
	</div>
	<div class="container pd-t45">
		<div class="qr-dl gray-bg">
			<div>
				<p>订单号：2009090909</p>
				<p>成交时间：2015-02-09 09:29:05</p>
				<p class="font-rose">订单状态：已签收</p>
			</div>
			<span class="font-rose rcl">卖家已发货</span>
			<a href="个人中心 - 我的订单 - 查看物流.html" class="btn pull-right btn-sm btn-blue">查看物流</a>
		</div>
		<div class="cur-ul cur-con">
			<!-- 示例：退款start -->
			<div class="cur-li cur-bd" id="divretMoney">
				<form action="${pageContext.request.contextPath}/afterserviceWap/retMoney/retMoneyUI" method="post">
					<input name="serverName" type="hidden" value="retMoney" />
					<input name="subOrderId" type="hidden" value="<%=subOrderId %>" />
					<input name="subOrderDetailId" type="hidden" value="<%=subOrderDetailId%>" />
				</form>
				<p class="tabcon-cl">
					<span class="pull-jl02">商品信息</span>
				</p>
				<div class="tabcon-cl tab-pic">
					<a href="#">
						<dl>
							<dt>
								<img src="demoimages/4.jpg" />
							</dt>
							<dd>
								<h2>华为荣耀4 64位高通骁龙处理器+4G高速网络</h2>
								<span class="tab-zh">
									颜色：白色<em>机身内存：6G</em>
								</span>
								<span class="tab-zh">活动：裸机</span>
							</dd>
							<i class="dy">
								<p class="tabcon-cl tabcon-cl03">￥3600.00</p>
								<p class="tabcon-cl tabcon-cl03 tab-lr">×1</p>
							</i>
						</dl>
					</a>
				</div>
				<div class="dd-xbtn">
					<a onclick="javascript:submitDeal('retMoney');">
						<button class="pull-left btn btn-bd btn-sm">退款</button>
					</a>
				</div>
			</div>
			<!-- 示例：退款end -->

			<!-- 示例：退货/换货start -->
			<div class="cur-li cur-bd">
				<form action="${pageContext.request.contextPath}/afterservice/retAndChangeGood/retAndChangeGoodUI" method="post">
					<input name="subOrderId" type="hidden" value="<%=subOrderId %>" />
					<input name="subOrderDetailId" type="hidden" value="<%=subOrderDetailId%>" />
					<p class="tabcon-cl">
						<span class="pull-jl02">商品信息</span>
					</p>
					<div class="tabcon-cl tab-pic">
						<a href="#">
							<dl>
								<dt>
									<img src="demoimages/4.jpg" />
								</dt>
								<dd>
									<h2>华为荣耀4 64位高通骁龙处理器+4G高速网络</h2>
									<span class="tab-zh">
										颜色：白色<em>机身内存：6G</em>
									</span>
									<span class="tab-zh">活动：裸机</span>
								</dd>
								<i class="dy">
									<p class="tabcon-cl tabcon-cl03">￥3600.00</p>
									<p class="tabcon-cl tabcon-cl03 tab-lr">×1</p>
								</i>
							</dl>
						</a>
					</div>
					<div class="dd-xbtn">
						<a href="#">
							<!-- <button class="pull-left btn btn-bd btn-sm">退货/换货</button> -->
							<input type="submit" class="pull-left btn btn-bd btn-sm" value="退货/换货" />
						</a>
					</div>
				</form>
			</div>
			<!-- 示例：退货/换货end -->

			<!-- 示例：申请售后start -->
			<div class="cur-li cur-bd">
				<form action="${pageContext.request.contextPath}/afterserviceWap/aftersaleService/toAftersaleServiceUI" method="post">
					<input name="subOrderId" type="hidden" value="<%=subOrderId %>" />
					<input name="subOrderDetailId" type="hidden" value="<%=subOrderDetailId%>" />
					<p class="tabcon-cl">
						<span class="pull-jl02">商品信息</span>
					</p>
					<div class="tabcon-cl tab-pic">
						<!-- TODO:订单详情 -->
						<a href="#">
							<dl>
								<dt>
									<img src="demoimages/4.jpg" />
								</dt>
								<dd>
									<h2>华为荣耀4 64位高通骁龙处理器+4G高速网络</h2>
									<span class="tab-zh">
										颜色：白色<em>机身内存：6G</em>
									</span>
									<span class="tab-zh">活动：裸机</span>
								</dd>
								<i class="dy">
									<p class="tabcon-cl tabcon-cl03">￥3600.00</p>
									<p class="tabcon-cl tabcon-cl03 tab-lr">×1</p>
								</i>
							</dl>
						</a>
					</div>
					<div class="dd-xbtn">
						<a href="#">
							<!-- <button class="pull-left btn btn-bd btn-sm">退货/换货</button> -->
							<input type="submit" class="pull-left btn btn-bd btn-sm" value="申请售后" />
						</a>
					</div>
				</form>
			</div>
			<!-- 示例：申请售后end -->

		</div>
		<div class="cur-con">
			<div class="qr-dl">
				<a href="#">
					<div class="pull-left">收货人：张三张三</div>
					<div class="pull-right">13988888888</div>
					<div class="clear"></div>
					<div class="address address-con">收货人地址：这里显示默认地址，如需要更改地址，点击这个区域就可以进行地址的相关操作</div>
				</a>
			</div>
		</div>
		<div class="cur-con">
			<ul class=" box10 white-bg">
				<li>
					<span class="pull-left">付款方式：</span>
					<span class="pull-right">在线支付</span>
				</li>
				<li>
					<span class="pull-left">商品金额：</span>
					<strong class="pull-right font-rose">￥3600.00</strong>
				</li>
				<li>
					<span class="pull-left">应支付金额：</span>
					<strong class="pull-right font-rose">￥3600.00</strong>
				</li>
			</ul>
		</div>
	</div>
	<div class="footer-wrap">
		<div class="affix footer-nav">
			<nav class="container">
				<a href="#" class="app-link home active">首页</a>
				<!-- 当前所属页面请在相应的按钮上加上class active -->
				<a href="所有商品.html" class="app-link category-search">所有商品</a>
				<a href="首选店铺-未首选或未登录.html" class="app-link shop-home">首选店铺</a>
				<a href="个人中心.html" class="app-link personal-center">个人中心</a>
				<a href="#" class="app-link shopping-cart">购物车</a>
			</nav>
		</div>
	</div>
	<!--退单确认弹出框 begin-->
	<div class="share-bit">
		<div class="share-text">
			<span>确认删除此订单?</span>
			<a class="cancel cancel-con">确认</a>
		</div>
		<div class="share-gb">
			<a class="cancel cancel-con">取消</a>
		</div>
	</div>
	<div class="more-box"></div>
</body>
</html>
