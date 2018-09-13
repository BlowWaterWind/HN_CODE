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
<link href="<%=request.getContextPath()%>/static/css/oil.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-1.8.2.min.js"></script>

<script type="text/javascript">
	window.onload = function() {
		var asServerName = "${asServerName}";
		var sucMsg = "${sucMsg}";
		var errorMsg = "${errorMsg}";
		//显示成功信息
		if (sucMsg.length > 0 && errorMsg ==="") {
			var sucId = "#suc"+asServerName;
			$(sucId).show();
		} else if (errorMsg.length > 0) {
			var eroId = "#ero"+asServerName;
			$(eroId).show();
		}
	}
</script>

</head>
<body>
	<div class="top container">
		<div class="header sub-title">
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>售后服务</h1>
		</div>
	</div>
	<div class="container pd-t45 white-bg">
		<!-- 维修成功 -->
		<div class="card4g-popup card4g-success cz-cg" id="sucrepair" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_default.png" />
			</span>
			<span class="cl-red">${sucMsg}</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>售后单号：${appNo}</p>
				<p>维修原因：${repairReason }</p>
				<p>详细说明：${repairExplain}</p>
			</div>
		</div>
		<!-- 维修失败-->
		<div class="card4g-popup card4g-success cz-cg" id="erorepair" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${errorMsg}</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>维修原因：${repairReason }</p>
				<p>详细说明：${repairExplain}</p>
			</div>
		</div>
		
		<!-- 补寄成功 -->
		<div class="card4g-popup card4g-success cz-cg" id="sucresendReceipt" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_default.png" />
			</span>
			<span class="cl-red">${sucMsg}</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>售后单号：${appNo}</p>
				<p>补寄原因：${resendReceiptReason }</p>
				<p>补寄说明：${resendReceiptExplain}</p>
			</div>
		</div>
		<!-- 补寄失败-->
		<div class="card4g-popup card4g-success cz-cg" id="eroresendReceipt" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${errorMsg}</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>补寄原因：${resendReceiptReason }</p>
				<p>补寄说明：${resendReceiptExplain}</p>
			</div>
		</div>
		<!-- 赠品成功 -->
		<div class="card4g-popup card4g-success cz-cg" id="sucgiftProblem" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_default.png" />
			</span>
			<span class="cl-red">${sucMsg}</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>赠品申请单号：${appNo}</p>
				<p>赠品申请原因：${giftProblemReason }</p>
				<p>赠品申请详细说明：${giftProblemExplain}</p>
			</div>
		</div>
		<!-- 赠品失败-->
		<div class="card4g-popup card4g-success cz-cg" id="erogiftProblem" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<span class="cl-red">${errorMsg }</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>赠品申请原因：${giftProblemReason }</p>
				<p>赠品申请详细说明：${giftProblemExplain}</p>
			</div>
		</div>
		
		<!-- 投诉成功 -->
		<div class="card4g-popup card4g-success cz-cg" id="succomplaint" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_default.png" />
			</span>
			<span class="cl-red">${sucMsg}</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>投诉申请单号：${appNo}</p>
				<p>投诉申请原因：${complaintReason }</p>
				<p>投诉内容：${complaintExplain}</p>
			</div>
		</div>
		<!-- 赠品失败-->
		<div class="card4g-popup card4g-success cz-cg" id="erocomplaint" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<span class="cl-red">${errorMsg }</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>投诉申请原因：${complaintReason }</p>
				<p>投诉内容：${complaintExplain}</p>
			</div>
		</div>
		<div class="sub-bnt">
			<a href="<%=request.getContextPath()%>" class="btn btn-block btn-blue">返 回商城首页</a>
		</div>
	</div>
</body>
</html>
