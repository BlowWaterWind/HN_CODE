<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="org.apache.shiro.session.Session" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
	Session shrioSession = UserUtils.getSession();
	UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
%>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/man-center.css"/>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css" />
<script type="text/javascript">
	$(function(){
		$("li").click(function(){
			$("li").removeClass("active");
			$(this).addClass("active");
			var memberAddressId = $(this).find("input[name='memberAddressId']").val();
			var memberRecipientName = $(this).find("input[name='memberRecipientName']").val();
			var memberRecipientPhone = $(this).find("input[name='memberRecipientPhone']").val();
			var memberIsDefault = $(this).find("input[name='memberIsDefault']").val();
			var memberRecipientProvince = $(this).find("input[name='memberRecipientProvince']").val();
			var memberRecipientCity = $(this).find("input[name='memberRecipientCity']").val();
			var memberRecipientCounty = $(this).find("input[name='memberRecipientCounty']").val();
			var memberRecipientAddress = $(this).find("input[name='memberRecipientAddress']").val();

			$("#memberAddressId").val(memberAddressId);
			$("#memberRecipientName").val(memberRecipientName);
			$("#memberRecipientPhone").val(memberRecipientPhone);
			$("#memberIsDefault").val(memberIsDefault);
			$("#memberRecipientProvince").val(memberRecipientProvince);
			$("#memberRecipientCity").val(memberRecipientCity);
			$("#memberRecipientCounty").val(memberRecipientCounty);
			$("#memberRecipientAddress").val(memberRecipientAddress);

			if($('input[name="moduleTypeId"]').val()==='sim'){
                $("#chooseReceiveAddressForm").attr("action",ctx + "/goodsBuy/linkToConfirmOrderSim");
			}
            $("#chooseReceiveAddressForm").submit();
        });
	});
</script>
</head>
<body>
<form id="chooseReceiveAddressForm" action="${ctx}/goodsBuy/linkToConfirmOrder" method="post">
	<input type="hidden" name="moduleTypeId" value="${moduleTypeId}"/>
	<%--收货地址--%>
	<input type="hidden" name="memberAddress.memberAddressId" id="memberAddressId" value="${carModel.memberAddress.memberAddressId}"/>
	<input type="hidden" name="memberAddress.memberRecipientName" id="memberRecipientName" value="${carModel.memberAddress.memberRecipientName}"/>
	<input type="hidden" name="memberAddress.memberRecipientPhone" id="memberRecipientPhone" value="${carModel.memberAddress.memberRecipientPhone}"/>
	<input type="hidden" name="memberAddress.memberIsDefault" id="memberIsDefault" value="${carModel.memberAddress.memberIsDefault}"/>
	<input type="hidden" name="memberAddress.memberRecipientProvince" id="memberRecipientProvince" value="${carModel.memberAddress.memberRecipientProvince}"/>
	<input type="hidden" name="memberAddress.memberRecipientCity" id="memberRecipientCity" value="${carModel.memberAddress.memberRecipientCity}"/>
	<input type="hidden" name="memberAddress.memberRecipientCounty" id="memberRecipientCounty" value="${carModel.memberAddress.memberRecipientCounty}"/>
	<input type="hidden" name="memberAddress.memberRecipientAddress" id="memberRecipientAddress" value="${carModel.memberAddress.memberRecipientAddress}"/>
</form>
<div class="top container">
	<div class="header sub-title">
		<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
		<h1>选择收货地址</h1>
		<a href="${ctx}memberAddress/toMemberAddressList" class="list-bt">管理</a>
	</div>
</div>
<div class="container pd-t45">
	<ul class="s-l">
	  <c:forEach items="${memberAddressList}" var="memberAddress" varStatus="maStatus">
		<li
			<c:if test="${memberAddress.memberAddressId == memberAddressId}">
				class="active"
			</c:if>
			>
			<input type="hidden" name="memberAddressId" value="${memberAddress.memberAddressId}"/>
			<input type="hidden" name="memberRecipientName" value="${memberAddress.memberRecipientName}"/>
			<input type="hidden" name="memberRecipientPhone" value="${memberAddress.memberRecipientPhone}"/>
			<input type="hidden" name="memberIsDefault" value="${memberAddress.memberIsDefault}"/>
			<input type="hidden" name="memberRecipientProvince" value="${memberAddress.memberRecipientProvince}"/>
			<input type="hidden" name="memberRecipientCity" value="${memberAddress.memberRecipientCity}"/>
			<input type="hidden" name="memberRecipientCounty" value="${memberAddress.memberRecipientCounty}"/>
			<input type="hidden" name="memberRecipientAddress" value="${memberAddress.memberRecipientAddress}"/>
			<div class="name">
				<div class="pull-left">${memberAddress.memberRecipientName}</div>
				<div class="pull-right phone-jl">${memberAddress.memberRecipientPhone}</div>
			</div>
			<div class="address">
				<span class="font-default">
					<c:if test="${memberAddress.memberIsDefault == 'Y'}">
						[默认]
					</c:if>
				</span>
				${fn:split(memberAddress.memberRecipientProvince,":")[1]}
				${fn:split(memberAddress.memberRecipientCity,":")[1]}
				${fn:split(memberAddress.memberRecipientCounty,":")[1]}
				${memberAddress.memberRecipientAddress}
			</div><i class="icon-default"></i>
		</li>
	  </c:forEach>
	</ul>
	<div class="mt10">
		<a href="${ctx}memberAddress/toAddMemberAddress" class="add-dress">新增地址</a>
	</div>
</div>
</body>
</html>