<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
	    <h1>退换货服务</h1>
	  </div>
	</div>
	
	<!-- 进入退换货申请页面失败提示 -->
	<div id="eroretAndChangeGood" style="display:none" class="container pd-t45 white-bg">
	  <div class="card4g-popup card4g-success cz-cg">
	     <span class="fr-cl"><img src="${pageContext.request.contextPath }/static/images/shop-images/icon_st.png" /></span>
	     <span class="cl-red">${errorMsg}</span>
	  </div>
	  <div class="btn btn-block btn-blue"><a href="${pageContext.request.contextPath }">返回商城首页</a></div>
	</div>
	
	
	<!-- 退货申请失败 -->
	<div id="eroretGood" style="display:none" class="container pd-t45 white-bg">
	  <div class="card4g-popup card4g-success cz-cg">
	     <span class="fr-cl"><img src="${pageContext.request.contextPath }/static/images/shop-images/icon_st.png" /></span>
	     <span class="cl-red">${errorMsg}</span>
	     <div class="card-list">
	       <c:if test="${! empty  subOrderNo}">
	       		<p>所属订单号：${subOrderNo}</p>
	       </c:if>
	        <c:if test="${! empty retOrderDetail}">
	       		<p><b>退货商品：</b>${retOrderDetail.goodsName}&nbsp;&nbsp;${retOrderDetail.goodsFormat}</p>
	       </c:if>
	       <c:if test="${!empty retOrderDetail.goodsSkuNum}">
	       		<p><b>退货数量：</b>${retOrderDetail.goodsSkuNum}</p>
	       </c:if>
	        <c:if test="${! empty retOrder.returnPrice }">
	       		<p><b>退款金额：</b><font class="cl-red">￥${retOrder.returnPrice}</font></p>
	       </c:if>
	       <c:if test="${! empty retOrder.returnPrice }">
	      	 <p><b>退货原因：</b>${retOrder.returnReson}</p>
	       </c:if>
			<c:if test="${! empty retOrder.returnResonDesc}">
				<p><b>退货原因说明：</b>${retOrder.returnResonDesc}</p>
			</c:if>			
	     </div>
	  </div>
	  <div class="sub-bnt">
	  	<a class="btn btn-block btn-blue" href="${pageContext.request.contextPath }">返回商城首页</a>
	  </div>
	</div>
	
	<!-- 退货申请成功 -->
	<div id="sucretGood" style="display: none" class="container pd-t45 white-bg">
	 <div class="card4g-popup card4g-success cz-cg">
	     <span class="fr-cl"><img src="${pageContext.request.contextPath }/static/images/icon_default.png" /></span><!--办理失败icon_st.png-->
	     <span class="cl-red">${sucMsg}</span>
	     <div class="card-list">
	      	<p><b>退货商品：</b>${retOrderDetail.goodsName}&nbsp;&nbsp;${retOrderDetail.goodsFormat}</p>
			<p><b>所属订单号：</b>${subOrderNo}</p>
			<p><b>退货订单号：</b>${retOrder.returnOrderNo}</p>
			<p><b>退货数量：</b>${retOrderDetail.goodsSkuNum}</p>
			<p><b>退款金额：</b><font class="cl-red">￥${retOrder.returnPrice/100}</font></p>
			<p><b>退货原因：</b>${retOrder.returnReson}</p>
			<c:if test="${! empty retOrder.returnResonDesc}">
				<p><b>退货原因说明：</b>${retOrder.returnResonDesc}</p>
			</c:if>
	     </div>
	  </div>
	  <div class="sub-bnt">
	  	<a class="btn btn-block btn-blue" href="${pageContext.request.contextPath }">返回商城首页</a>
	  </div>
	</div>

	<!-- 换货申请失败 -->
	<div id="erochangeGood" style="display: none" class="container pd-t45 white-bg">
	 <div class="card4g-popup card4g-success cz-cg">
	     <span class="fr-cl"><img src="${pageContext.request.contextPath }/static/images/shop-images/icon_st.png" /></span><!--办理失败icon_st.png-->
	     <span class="cl-red">${errorMsg}</span>
	     <div class="card-list">
	     	<c:if test="${!empty changeOrderDetail.goodsName}">
	     		<p><b>换货商品：</b>${changeOrderDetail.goodsName}&nbsp;${changeOrderDetail.goodsFormat}</p>
	     	</c:if>
			<p><b>所属订单号：</b>${subOrderNo}</p>
			<p><b>换货数量：</b>${changeOrderDetail.goodsSkuNum}</p>
			<p><b>换货原因：</b>${changeOrder.changeReson}</p>
			<c:if test="${! empty changeOrder.changeResonDesc}">
				<p><b>换货原因说明：</b>${changeOrder.changeResonDesc}</p>
			</c:if>
	     </div>
	  </div>
	  <div class="sub-bnt">
	  	<a class="btn btn-block btn-blue" href="${pageContext.request.contextPath }">返回商城首页</a>
	  </div>
	</div>
	
	<!-- 换货申请成功 -->
	<div id="succhangeGood" style="display: none" class="container pd-t45 white-bg">
	 <div class="card4g-popup card4g-success cz-cg">
	     <span class="fr-cl"><img src="${pageContext.request.contextPath }/static/images/icon_default.png" /></span>
	     <span class="cl-red">${sucMsg}</span>
	     <div class="card-list">
			<p><b>换货商品：</b>${changeOrderDetail.goodsName}&nbsp;&nbsp;${changeOrderDetail.goodsFormat}</p>
			<p><b>所属订单号：</b>${subOrderNo}</p>
			<p><b>换货单号：</b>${changeOrder.changeOrderNo}</p>
			<p><b>换货数量：</b>${changeOrderDetail.goodsSkuNum}</p>
			<p><b>换货原因：</b>${changeOrder.changeReson}</p>
			<c:if test="${! empty changeOrder.changeResonDesc}">
				<p><b>换货原因说明：</b>${changeOrder.changeResonDesc}</p>
			</c:if>
	     </div>
	  </div>
	  <div class="sub-bnt">
	  	<a class="btn btn-block btn-blue" href="${pageContext.request.contextPath }">返回商城首页</a>
	  </div>
	</div>
</body>
</html>
