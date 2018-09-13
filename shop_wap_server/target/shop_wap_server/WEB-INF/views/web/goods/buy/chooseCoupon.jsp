<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
	<%
		Session shrioSession = UserUtils.getSession();
		UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
	%>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<link href="${ctxStatic}/css/man-center.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
	<script type="text/javascript">
		$(function(){
			/**
			 *  选择优惠券
			 * */
			$("input[type='checkbox']").click(function(){
				var checkboxName = $(this).attr("name");
				if($(this).is(":checked")){
					$("input[type='checkbox'][name='"+checkboxName+"']").removeAttr("checked");
					$(this).attr("checked","checked");
				}
			});

			/**
			 * 确定
			 */
			$("#confirmBtn").click(function () {
				$("input[type='checkbox']:checked").each(function(i,coupon){
					 var shopId = $(coupon).attr("shopId");
					 var couponId = $(coupon).attr("couponId");
					 var couponValue = $(coupon).attr("couponValue");

					 var $couponForm = $("#couponForm");
					 $("<input type='hidden' name='couponInfoList["+i+"].shopId' value='"+shopId+"'/>").appendTo($couponForm);
					 $("<input type='hidden' name='couponInfoList["+i+"].couponId' value='"+couponId+"'/>").appendTo($couponForm);
					 $("<input type='hidden' name='couponInfoList["+i+"].couponValue' value='"+couponValue+"'/>").appendTo($couponForm);
				});

				$("#couponForm").submit();
			});

		});
	</script>
</head>
<body>
<div class="top container">
	<div class="container header sub-title">
		<a onclick="window.history.back()" href="###" class="icon-left"></a>
		<h1>选择优惠券</h1>
	</div>
</div>
<form id="couponForm" action="${ctx}/goodsBuy/linkToConfirmOrder" method="post">
</form>
<div class="container">
	<ul class="s-l s-gl pd-t45 yhjlist">
		<c:forEach items="${couponInfoList}" var="coupon" varStatus="cStatus">
			<li>
				<label class="yhj-wsh">
					<div class="cur-fr pull-left">
						<input type="checkbox" name="${coupon.shopId}" shopId="${coupon.shopId}" couponId="${coupon.couponId}" couponValue="${coupon.couponValue}"
						  <c:forEach items="${carModel.couponInfoList}" var="couponInfo">
							  <c:if test="${couponInfo.couponId == coupon.couponId}">checked="checked"</c:if>
						  </c:forEach>
						/>
					</div>
					<div class="name">
						<div class="pull-left">${coupon.couponTypeName}</div>
						<div class="address">
							使用期限：<fmt:formatDate value="${coupon.couponBatchStarttime}"/> -
							<fmt:formatDate value="${coupon.couponBatchEndtime}"/>
						</div>
					</div>
					<div class="yhjbg">
					<span class="money">
						<fmt:formatNumber value="${coupon.couponValue/100}" type="currency"/>
					</span><br/>
						满${coupon.couponUseLowestValue/100}元使用
					</div>
				</label>
			</li>
		</c:forEach>
	</ul>
	<div class="tj-btn btn-box">
		<a class="btn btn-green" href="javascript:window.history.back();">取消</a>
		<a id="confirmBtn" class="btn btn-blue" href="###">确定</a>
	</div>
</div>
</body>
</html>