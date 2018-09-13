<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
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
$(function(){
	var sucMsg=promptMsg='';
	var msgCode = '${msgCode}';
	var promptMsg = '${promptMsg}';
	var asServerName = '${flage}';
	var appTypeId = '${asApp.aftersaleApplyTypeId}';
	if(asServerName==='aftersaleService'){
		switch(appTypeId){
			case '10':
				asServerName='complaint';
				break;
			case '9':
				asServerName='giftProblem';
				break;
			case '4':
				asServerName='repair';
				break;
			default:
				asServerName='resendReceipt';
				break;
		}
	}
	if (msgCode ==="-1") {
		var sucId = "#ero"+asServerName;
		$(sucId).show();
	}
})
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

		<!-- 退款失败-->
		<div class="card4g-popup card4g-success cz-cg" id="eroretMoney" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${promptMsg}</span>
			<div class="card-list">
				<p>所属订单：${orderSubNo}</p>
				<p>退款原因：${repairReason }</p>
				<p>详细说明：${repairExplain}</p>
			</div>
		</div>

		<!-- 退货失败-->
		<div class="card4g-popup card4g-success cz-cg" id="eroretGood" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${promptMsg}</span>
			<div class="card-list">
				<p>所属订单：${retOrder.orderSub.orderSubNo}</p>
				<p>退货单号：${retOrder.returnOrderNo}</p>
				<p>申请时间：<fmt:formatDate value="${retOrder.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p>退货状态：${retOrder.orderStatusName}</p>
				<p>退货原因：${retOrder.returnReson }</p>
				<c:if test="${!empty retOrder.returnResonDesc}">
				<p>详细说明：${retOrder.returnResonDesc}</p>
				</c:if>
			</div>
		</div>

		<div class="card4g-popup card4g-success cz-cg" id="eroretGood1" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${promptMsg}</span>
			<c:forEach items="${retOrder.returnOrderDetailL}" var="l">
			<div class="card-list">
				<p>所属订单：${l.orderSub.orderSubNo}</p>
				<p>退货单号：${l.returnOrderNo}</p>
				<p>申请时间：<fmt:formatDate value="${l.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p>退货状态：${l.orderStatusName}</p>
				<p>退货原因：${l.returnReson }</p>
				<c:if test="${!empty l.returnResonDesc}">
					<p>详细说明：${l.returnResonDesc}</p>
				</c:if>
			</div>
			</c:forEach>
		</div>

		<!-- 换货失败-->
		<div class="card4g-popup card4g-success cz-cg" id="erochangeGood" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${promptMsg}</span>
			<div class="card-list">
				<p>所属订单：${changeOrder.orderSub.orderSubNo}</p>
				<p>换货单号：${changeOrder.changeOrderNo}</p>
				<p>申请时间：<fmt:formatDate value="${changeOrder.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p>换货状态：${changeOrder.orderStatusName}</p>
				<p>换货原因：${changeOrder.changeReson}</p>
				<c:if test="${!empty changeOrder.changeResonDesc}">
				<p>原因说明：${changeOrder.changeResonDesc}</p>
				</c:if>
			</div>
		</div>

		<!-- 维修失败-->
		<div class="card4g-popup card4g-success cz-cg" id="erorepair" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${promptMsg}</span>
			<div class="card-list">
				<p>所属订单：${asApp.orderSubNo}</p>
				<p>售后单号：${asApp.aftersaleApplyNum}</p>
				<p>售后类型：申请维修</p>
				<p>申请时间：<fmt:formatDate value="${asApp.aftersaleApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p>售后状态：${asApp.aftersaleApplyStatusName}</p>
				<p>维修原因：${asApp.aftersaleApplyReason }</p>
				<c:if test="${!empty asApp.aftersaleApplyDescribe}">
				<p>原因说明：${asApp.aftersaleApplyDescribe}</p>
				</c:if>
			</div>
		</div>

		<!-- 补寄失败-->
		<div class="card4g-popup card4g-success cz-cg" id="eroresendReceipt" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<!--办理失败icon_st.png-->
			<span class="cl-red">${promptMsg}</span>
			<div class="card-list">
				<p>所属订单：${asApp.orderSubNo}</p>
				<p>售后单号：${asApp.aftersaleApplyNum}</p>
				<p>申请时间：<fmt:formatDate value="${asApp.aftersaleApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p>售后类型：申请发票补寄</p>
				<p>售后状态：${asApp.aftersaleApplyStatusName}</p>
				<p>补寄原因：${asApp.aftersaleApplyReason }</p>
				<c:if test="${!empty asApp.aftersaleApplyDescribe}">
					<p>原因说明：${asApp.aftersaleApplyDescribe}</p>
				</c:if>
			</div>
		</div>
		<!-- 赠品失败-->
		<div class="card4g-popup card4g-success cz-cg" id="erogiftProblem" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<span class="cl-red">${promptMsg }</span>
			<div class="card-list">
				<p>所属订单：${asApp.orderSubNo}</p>
				<p>售后单号：${asApp.aftersaleApplyNum}</p>
				<p>申请时间：<fmt:formatDate value="${asApp.aftersaleApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p>售后类型：赠品问题</p>
				<p>售后状态：${asApp.aftersaleApplyStatusName}</p>
				<p>赠品问题原因：${asApp.aftersaleApplyReason }</p>
				<c:if test="${!empty asApp.aftersaleApplyDescribe}">
					<p>原因说明：${asApp.aftersaleApplyDescribe}</p>
				</c:if>
			</div>
		</div>
		
		<!-- 投诉失败-->
		<div class="card4g-popup card4g-success cz-cg" id="erocomplaint" style="display:none">
			<span class="fr-cl">
				<img src="<%=request.getContextPath()%>/static/images/shop-images/icon_st.png" />
			</span>
			<span class="cl-red">${promptMsg }</span>
			<div class="card-list">
				<p>所属订单：${asApp.orderSubNo}</p>
				<p>售后单号：${asApp.aftersaleApplyNum}</p>
				<p>申请时间：<fmt:formatDate value="${asApp.aftersaleApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p>售后类型：申请投诉</p>
				<p>售后状态：${asApp.aftersaleApplyStatusName}</p>
				<p>投诉原因：${asApp.aftersaleApplyReason }</p>
				<c:if test="${!empty asApp.aftersaleApplyDescribe}">
					<p>原因说明：${asApp.aftersaleApplyDescribe}</p>
				</c:if>
			</div>
		</div>

		<div class="sub-bnt">
			<a href="<%=request.getContextPath()%>" class="btn btn-block btn-blue">返 回商城首页</a>
		</div>
	</div>
</body>
</html>
