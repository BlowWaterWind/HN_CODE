<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>湖南移动商城</title>
	<%@include file="/WEB-INF/views/include/head.jsp"%>
	
	<link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>订单结果</h1>
		</div>
	</div>
	<div class="container pd-t45 white-bg">
		<div class="card4g-popup card4g-success cz-cg">
			<span class="fr-cl">
				<img src="${ctxStatic}/images/shop-images/icon_default.png" />
			</span>
			<span class="cl-red">${sucMsg}</span>
			<div class="card-list mesg-text">
				<p>订单号：<a style="color:#0000FF" href="${ctx}/myOrder/toOrderDetail?orderId=${orderVo.orderSubId}">${orderVo.orderSubNo}</a></p>
				<p>号码信息：<strong>${orderVo.detailList[0].goodsId}</strong>
				<p>绑定套餐：${orderVo.detailList[0].orderDetailSim.baseSetName}</p>
				<p>号卡金额：￥<strong>${orderVo.detailList[0].orderDetailSim.cardFee/100}</strong>元</p>
				<p>预存金额：￥<strong>${orderVo.detailList[0].orderDetailSim.preFee/100}</strong>元</p>
			</div>
		</div>
		<div class="sub-bnt">
			<a href="${ctx}/myOrder/toMyAllOrderList" class="btn btn-blue btn-box">查看我的订单</a>
			<a href="${ctx}" class="btn btn-blue btn-box">返回商城首页</a>
		</div>
	</div>
</body>
</html>
