<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
	<%
		Session shrioSession = UserUtils.getSession();
		UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
	%>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/message.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
<script type="text/javascript">
	$(function(){
		//如果是到厅自提，则显示自提网点，否则隐藏
		/*if($(".radio.active").val() == 2){
			$(".wl-xy.wl-xy02").show();
		}else{
			$(".wl-xy.wl-xy02").hide();
		}*/

		$("input[name='deliveryMode']").click(function () {
			$(".radio.active").removeClass("active");

			if(!$(this).hasClass("active")){
				$(this).addClass("active");
			}

			//如果是到厅自提，则显示自提网点，否则隐藏
			/*if($(this).val() == 2){
				$(".wl-xy.wl-xy02").show();
			}else{
				$(".wl-xy.wl-xy02").hide();
			}*/
		});

		/**
		 * 选择地址 确定按钮
		 */
		$("#confirmBtn").click(function(){
			var $deliveryMode = $("input[name='deliveryMode']:checked");
			var moduleTypeId =  $('input[name="moduleTypeId"]').val();
			if ($deliveryMode.length == 0) {
				showAlert("请选择一种配送方式");
			} else {
				$("#deliveryModeId").val($deliveryMode.val());
				$("#deliveryModeName").val($deliveryMode.next().text());
				$("#hallAddress").val($("#hallAddressSP").text());
               	if(moduleTypeId =="sim"){
                    $("#chooseDeliVeryModeForm").attr("action",ctx + "goodsBuy/linkToConfirmOrderSim");
				}
				$("#chooseDeliVeryModeForm").submit();
			}
		});

	});
</script>
</head>
<body>
<div class="top container">
	<div class="header sub-title">
		<a onclick="window.history.back()" href="javascript:void(0)"
			class="icon-left"></a>
		<h1>配送方式</h1>
	</div>
</div>

<form id="chooseDeliVeryModeForm" action="${ctx}goodsBuy/linkToConfirmOrder" method="post">
	<%-- 跳转模块Id --%>
	<input type="hidden" name="moduleTypeId" value="${moduleTypeId}"/>
	<%--配送方式--%>
	<input type="hidden" name="deliveryMode.deliveryModeId" id="deliveryModeId" value="${carModel.deliveryMode.deliveryModeId}"/>
	<input type="hidden" name="deliveryMode.deliveryModeName" id="deliveryModeName" value="${carModel.deliveryMode.deliveryModeName}"/>
	<input type="hidden" name="hallAddress" id="hallAddress" value="${carModel.hallAddress}"/>
</form>

<div class="container pd-t45 white-bg">
	<div class="cur-ul choose-list">
		<c:forEach items="${deliveryModeList}" var="deliveryMode" varStatus="dmStatus">
			<c:choose>
				<%--号卡的配送方式--%>
				<c:when test="${carModel.rootCategoryId == 2 }">
					<label class="tabcon-cl">
					<c:choose>
						<c:when test="${deliveryMode.deliveryModeId == carModel.deliveryMode.deliveryModeId}">
							<input type="radio" name="deliveryMode" class="radio active" checked="checked" value="${deliveryMode.deliveryModeId}" />
						</c:when>
						<c:when test="${(empty carModel.deliveryMode.deliveryModeId) && dmStatus.index == 0}">
							<input type="radio" name="deliveryMode" class="radio active" checked="checked" value="${deliveryMode.deliveryModeId}" />
						</c:when>
						<c:otherwise>
							<input type="radio" name="deliveryMode" class="radio" value="${deliveryMode.deliveryModeId}" />
						</c:otherwise>
					</c:choose>
					<span class="box-jl">${deliveryMode.deliveryModeName}</span>
					</label>
				</c:when>
				<c:when test="${deliveryMode.deliveryModeId!=2 && (carModel.rootCategoryId == 10000001 || carModel.rootCategoryId == 5)}">
				<%--<c:when test="${carModel.rootCategoryId == 10000001 || carModel.rootCategoryId == 5}">--%>
					<label class="tabcon-cl">
					<c:choose>
						<c:when test="${deliveryMode.deliveryModeId == carModel.deliveryMode.deliveryModeId}">
							<input type="radio" name="deliveryMode" class="radio active" checked="checked" value="${deliveryMode.deliveryModeId}" />
						</c:when>
						<c:when test="${(empty carModel.deliveryMode.deliveryModeId) && dmStatus.index == 0}">
							<input type="radio" name="deliveryMode" class="radio active" checked="checked" value="${deliveryMode.deliveryModeId}" />
						</c:when>
						<c:otherwise>
							<input type="radio" name="deliveryMode" class="radio" value="${deliveryMode.deliveryModeId}" />
						</c:otherwise>
					</c:choose>
					<span class="box-jl">${deliveryMode.deliveryModeName}</span>
					</label>
				</c:when>
			</c:choose>
		</c:forEach>
	</div>
</div>
<div class="fix-br">
	<div class="affix foot-menu">
		<div class="container form-group tj-btn">
			<a class="btn btn-green" onclick="window.history.back()" href="javascript:void(0)">取消</a>
			<a id="confirmBtn" class="btn btn-blue" href="javascript:void(0);">确定</a>
		</div>
	</div>
</div>
</body>
</html>