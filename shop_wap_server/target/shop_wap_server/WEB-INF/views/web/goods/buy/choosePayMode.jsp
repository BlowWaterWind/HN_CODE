<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<%
		Session shrioSession = UserUtils.getSession();
		UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
	%>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
<script type="text/javascript">
	$(function(){
		$("input[name='payMode']").click(function(){
			var payModeId = $(this).val();
			var payModeName = $(this).siblings("span[name='payModeName']").text();

			$("#payModeId").val(payModeId);
			$("#payModeName").val(payModeName);

			$("#choosePayModeForm").submit();
		});
		
	});
</script>
</head>
<body>
<div class="top container">
  <div class="header sub-title">
	  <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
      <h1>支付方式</h1>
  </div>
</div>
<form id="choosePayModeForm" action="${ctx}goodsBuy/linkToConfirmOrder" method="post">
	<%--支付方式--%>
	<input type="hidden" name="payMode.payModeId" id="payModeId" value="${carModel.payMode.payModeId}"/>
	<input type="hidden" name="payMode.payModeName" id="payModeName" value="${carModel.payMode.payModeName}"/>
</form>
<div class="container pd-t45 white-bg">
  <ul class="cur-ul choose-list">
	<c:forEach items="${payModeList}" var="payMode" varStatus="pmStatus">

		<c:choose>
			<%--是否支付货到付款--%>
			<c:when test="${payMode.payModeId==1}">
				<c:if test="${cashOndelivery=='Y'}">
					<li>
					<label>
					<div class="tabcon-cl">
					<c:choose>
					<c:when test="${payMode.payModeId == carModel.payMode.payModeId}">
						<input type="radio" name="payMode" checked="checked" value="${payMode.payModeId}"/>
					</c:when>
					<c:when test="${(empty carModel.payMode.payModeId) && payMode.payModeId == 2}">
						<input type="radio" name="payMode" checked="checked" value="${payMode.payModeId}"/>
					</c:when>
					<c:otherwise>
						<input type="radio" name="payMode" value="${payMode.payModeId}"/>
					</c:otherwise>
				</c:choose>
				<span name="payModeName" class="lab-jl">${payMode.payModeName}</span>
					</div>
					</label>
					</li>
				</c:if>
			</c:when>
			<%--是否支持到厅自取--%>
			<c:when test="${payMode.payModeId==3 && carModel.rootCategoryId == 2}">
				<c:if test="${busiHallOnTake=='Y'}">
					<li>
					<label>
					<div class="tabcon-cl">
					<c:choose>
						<c:when test="${payMode.payModeId == carModel.payMode.payModeId}">
							<input type="radio" name="payMode" checked="checked" value="${payMode.payModeId}"/>
						</c:when>
						<c:when test="${(empty carModel.payMode.payModeId) && payMode.payModeId == 2}">
							<input type="radio" name="payMode" checked="checked" value="${payMode.payModeId}"/>
						</c:when>
						<c:otherwise>
							<input type="radio" name="payMode" value="${payMode.payModeId}"/>
						</c:otherwise>
					</c:choose>
					<span name="payModeName" class="lab-jl">${payMode.payModeName}</span>
					</div>
					</label>
					</li>
				</c:if>
			</c:when>
			<c:when test="${payMode.payModeId==2 }">
				<li>
				<label>
				<div class="tabcon-cl">
				<c:choose>
					<c:when test="${payMode.payModeId == carModel.payMode.payModeId}">
						<input type="radio" name="payMode" checked="checked" value="${payMode.payModeId}"/>
					</c:when>
					<c:when test="${(empty carModel.payMode.payModeId) && payMode.payModeId == 2}">
						<input type="radio" name="payMode" checked="checked" value="${payMode.payModeId}"/>
					</c:when>
					<c:otherwise>
						<input type="radio" name="payMode" value="${payMode.payModeId}"/>
					</c:otherwise>
				</c:choose>
				<span name="payModeName" class="lab-jl">${payMode.payModeName}</span>
				</div>
				</label>
				</li>
			</c:when>
		</c:choose>



	</c:forEach>
  </ul>
</div>


<!--底部菜单start-->

<!--底部菜单end-->
</body>
</html>