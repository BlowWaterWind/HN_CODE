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
		$("input[name='receiptTime']").click(function () {
			var receiptTimeId = $(this).val();
			var receiptTimeName = $(this).next().text();

			$("#receiptTimeId").val(receiptTimeId);
			$("#receiptTimeName").val(receiptTimeName);

			$("#chooseReceiptTimeForm").submit();
		});
		
		$("#confirmBtn").click(function () {
			var $receiptTime = $("input[name='receiptTime']:checked");
			var receiptTimeId = $receiptTime.val();
			var receiptTimeName = $receiptTime.next().text();

			$("#receiptTimeId").val(receiptTimeId);
			$("#receiptTimeName").val(receiptTimeName);

			$("#chooseReceiptTimeForm").submit();
		});
		
	});
</script>
</head>
<body>
<div class="top container">
  <div class="header sub-title">
    <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>收货时间段</h1>
  </div>
</div>
<form id="chooseReceiptTimeForm" action="${ctx}goodsBuy/linkToConfirmOrder">
	<%--收货时间段--%>
	<input type="hidden" name="receiptTime.receiptTimeId" id="receiptTimeId" value="${carModel.receiptTime.receiptTimeId}"/>
	<input type="hidden" name="receiptTime.receiptTimeName" id="receiptTimeName" value="${carModel.receiptTime.receiptTimeName}"/>
</form>
<div class="container pd-t45 white-bg">
  <ul class="cur-ul choose-list">
	<c:forEach items="${receiptTimeList}" var="receiptTime" varStatus="rtStatus">
		<li>
		  <label>
		  <div class="tabcon-cl">
			<c:choose>
				<c:when test="${receiptTime.receiptTimeId == carModel.receiptTime.receiptTimeId}">
					<input type="radio" name="receiptTime" class="radio" checked="checked" value="${receiptTime.receiptTimeId}"/>
				</c:when>
				<c:when test="${(empty carModel.receiptTime.receiptTimeId) && receiptTime.receiptTimeId == 1}">
					<input type="radio" name="receiptTime" class="radio" checked="checked" value="${receiptTime.receiptTimeId}"/>
				</c:when>
				<c:otherwise>
					<input type="radio" name="receiptTime" class="radio" value="${receiptTime.receiptTimeId}"/>
				</c:otherwise>
			</c:choose>
			<span class="lab-jl">${receiptTime.receiptTimeName}</span>
		  </div>
		  </label>
		</li>
	</c:forEach>
  </ul>
</div>

<!--底部菜单start-->

<!--底部菜单end-->
</body>
</html>