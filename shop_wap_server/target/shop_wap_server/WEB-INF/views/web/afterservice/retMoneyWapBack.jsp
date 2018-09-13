<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>

<link href="<%=request.getContextPath()%>/static/css/main.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/media-style.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/oil.css" rel="stylesheet" type="text/css" />


<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	window.onload = function() {
		var asServerName = "${asServerName}";
		initData(asServerName);
	}

	function initData(asServerName) {
		var sucMsg = "${sucMsg}";
		var errorMsg = "${errorMsg}";
		//显示成功信息
		if (sucMsg.length > 0 && errorMsg === "") {
			var sucId = "#suc" + asServerName;
			$(sucId).show();
		} else if (errorMsg.length > 0) {
			var eroId = "#ero" + asServerName;
			$(eroId).show();
		}
	}
</script>

</head>
<body>
	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>申请结果</h1>
		</div>
	</div>
	<!-- 退款申请失败 -->
	<div class="container pd-t45 white-bg">
		<!-- 退款成功 -->
		<div class="card4g-popup card4g-success cz-cg" id="sucretMoney" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_default.png" />
			</span>
			<span class="cl-red">${sucMsg}</span>
			<div class="card-list">
				<p>订单号：<a style="color:#0000FF" href="<%=request.getContextPath()%>/myOrder/toOrderDetail?orderId=${queriedSubOrder.orderSubId}">${queriedSubOrder.orderSubNo}</a></p>
				<p>退款原因：${retMoneyReason }</p>
				<p>退款金额：${orderSub.orderSubPayAmount }</p>
			</div>
		</div>
		<!-- 退款失败-->
		<div class="card4g-popup card4g-success cz-cg" id="eroretMoney" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${errorMsg}</span>
			<div class="card-list">
				<p>订单号：<a style="color:#0000FF" href="<%=request.getContextPath()%>/myOrder/toOrderDetail?orderId=${queriedSubOrder.orderSubId}">${queriedSubOrder.orderSubNo}</a></p>
				<p>退款原因：${retMoneyReason }</p>
				<p>可退最大金额：${maxretMony/100}元</p>
			</div>
		</div>
		<div class="sub-bnt">
			<a href="<%=request.getContextPath()%>" class="btn btn-block btn-blue">返回商城首页</a>
		</div>
	</div>
</body>
</html>
