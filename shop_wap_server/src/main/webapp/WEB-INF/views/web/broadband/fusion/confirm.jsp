<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
	<link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
	<link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<%@ include file="/WEB-INF/views/include/message.jsp"%>
	<title>湖南移动网上营业厅</title>
	<script>
		var baseProject = "${ctx}" ;
	</script>
</head>

<body>
<div class="top container">
	<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
		<h1>确定套餐</h1>
	</div>
</div>
<form id="form1" action="submitOrder" method="post">
	<input type="hidden" id="goodsSkuId" name="goodsSkuId" value="${broadbandFusionBean.goodsSkuId}"/>
	<input type="hidden" id="goodsName" name="goodsName" value="${broadbandFusionBean.goodsName}" />
	<input type="hidden" id="houseCode" name="houseCode" value="${broadbandFusionBean.houseCode}"/>
	<input type="hidden" id="addressName" name="addressName" value="${broadbandFusionBean.addressName}"/>
	<input type="hidden" id="addrDesc" name="addrDesc" value="${broadbandFusionBean.addrDesc }"/>
	<input type="hidden" id="communityType" name="communityType" value="${broadbandFusionBean.communityType}"/>
	<input type="hidden" id="isBroadband" name="isBroadband" value="${broadbandFusionBean.isBroadband}"/>
	<input type="hidden" id="isMbh" name="isMbh" value="${broadbandFusionBean.isMbh}"/>
	<input type="hidden" id="maxWidth" name="maxWidth" value="${broadbandFusionBean.maxWidth}"/>
	<input type="hidden" id="freePort" name="freePort" value="${broadbandFusionBean.freePort}"/>
	<input type="hidden" id="eparchyCode" name="eparchyCode" value="${broadbandFusionBean.eparchyCode}"/>
	<input type="hidden" id="county" name="county" value="${broadbandFusionBean.county}"/>
	<input type="hidden" id="countyName" name="countyName" value="${broadbandFusionBean.countyName}"/>
	<input type="hidden" id="coverType" name="coverType" value="${broadbandFusionBean.coverType}"/>
	<input type="hidden" id="bandWidth" name="bandWidth" value="${broadbandFusionBean.bandWidth}"/>
	<input type="hidden" id="productId" name="productId" value="${broadbandFusionBean.productId}"/>
	<input type="hidden" id="discntCode" name="discntCode" value="${broadbandFusionBean.discntCode}"/>
	<input type="hidden" id="chooseCat" name="chooseCat" value="${broadbandFusionBean.chooseCat}"/>
	<input type="hidden" id="mbh" name="mbh" value="${broadbandFusionBean.mbh}"/>
	<input type="hidden" id="price" name="price" value="${broadbandFusionBean.price}"/>
	<input type="hidden" id="bookInstallDateParam" name="bookInstallDate" value="${broadbandFusionBean.bookInstallDate}"/>
	<input type="hidden" id="bookInstallTimeParam" name="bookInstallTime" value="${broadbandFusionBean.bookInstallTime}"/>
	<input type="hidden" id="payMode" name="payMode"  value="${broadbandFusionBean.payMode}">
	<input type="hidden" id="payModeName" name="payModeName" value="${broadbandFusionBean.payModeName}">
	<input type="hidden" id="contact" name="contact" value="${broadbandFusionBean.contact}">
	<input type="hidden" id="contactPhone" name="contactPhone" value="${broadbandFusionBean.contactPhone}">
	<input type="hidden" id="moreMagicTabShow" name="moreMagicTabShow" value="${broadbandFusionBean.moreMagicTabShow}">
	<input type="hidden" id="iptvTabShow" name="iptvTabShow" value="${broadbandFusionBean.iptvTabShow}">
	<input type="hidden" id="imsTabShow" name="imsTabShow" value="${broadbandFusionBean.imsTabShow}">
	<input type="hidden" id="bookYes" name="bookYes" value="${broadbandFusionBean.bookYes}">
	<input type="hidden" id="memberListStr" name="memberListStr" value="${broadbandFusionBean.memberListStr}"/>
	<input type="hidden" id="giveMbh" name="giveMbh" value="${broadbandFusionBean.giveMbh}"/>
	<input type="hidden" id="busiType" name="busiType" value="${broadbandFusionBean.busiType}"/>
	<input type="hidden" id="minCost" name="minCost" value="${broadbandFusionBean.minCost}"/>
	<input type="hidden" id="monthCost" name="monthCost" value="${broadbandFusionBean.monthCost}"/>
	<input type="hidden" id="imsPhone" name="imsPhone" value="${broadbandFusionBean.imsPhone}"/>
	<input type="hidden"   name="imsProductId" value="${broadbandFusionBean.imsProductId}"/>
	<input type="hidden"  name="imsPackageId" value="${broadbandFusionBean.imsPackageId}"/>
	<input type="hidden"   name="imsElementId" value="${broadbandFusionBean.imsElementId}"/>
	<input type="hidden"  name="imsLevel" value="${broadbandFusionBean.imsLevel}"/>
</form>
<!--套餐选择 start-->
<div class="container">
	<div class="new-poplist clearfix">
		<table cellpadding="0" cellspacing="0" class="new-tabs">
			<tr>
				<td class="tr">活动名称：</td>
				<td class="tl">${broadbandFusionBean.goodsName}</td>
			</tr>
			<c:if test="${broadbandFusionBean.isBroadband == '1'}">
			<tr>
				<td class="tr">联系人：</td>
				<td class="tl" id="td_installName">${broadbandFusionBean.contact}</td>
			</tr>
			<tr>
				<td class="tr">联系电话：</td>
				<td class="tl" id="td_phoneNum">${broadbandFusionBean.contactPhone}</td>
			</tr>
			<tr>
				<td class="tr">装机地址：</td>
				<td class="tl">${broadbandFusionBean.addressName}</td>
			</tr>
			<tr>
				<td class="tr">装机时间：</td>
				<td class="tl">${broadbandFusionBean.bookInstallDate}
					<c:choose>
						<c:when test="${broadbandFusionBean.bookInstallTime == '0'}">
							08:00:00-12:00:00
						</c:when>
						<c:when test="${broadbandFusionBean.bookInstallTime == '1'}">
							12:00:00-14:00:00
						</c:when>
						<c:when test="${broadbandFusionBean.bookInstallTime == '2'}">
							14:00:00-18:00:00
						</c:when>
						<c:when test="${broadbandFusionBean.bookInstallTime == '3'}">
							18:00:00-20:00:00
						</c:when>
					</c:choose>
				</td>
			</tr>
			<c:if test="${broadbandFusionBean.imsPhone !=null && broadbandFusionBean.imsPhone !=''}">
				<tr>
					<td class="tr">固话号码：</td>
					<td class="tl">${broadbandFusionBean.imsPhone}</td>
				</tr>
			</c:if>
			<tr>
				<td class="tr">电视牌照方：</td>
				<td class="tl">
					<c:if test="${fn:contains(broadbandFusionBean.mbh,'20142000')}">
						芒果TV&nbsp;&nbsp;
					</c:if>
					<c:if test="${fn:contains(broadbandFusionBean.mbh,'20141000')}">
						未来电视&nbsp;&nbsp;
					</c:if>
				</td>
			</tr>
			<c:choose>
				<c:when test="${broadbandFusionBean.minCost eq '88' or broadbandFusionBean.minCost eq '98'}">
					<tr>
						<td class="tr">宽带月费：</td>
						<td class="tl">0元/月</td>
					</tr>
					<tr>
						<td class="tr">电视月费：</td>
						<td class="tl">20元/月</td>
					</tr>
					<tr>
						<td class="tr">保底消费：</td>
						<td class="tl">${broadbandFusionBean.minCost}元/月</td>
					</tr>
				</c:when>
				<c:when test="${broadbandFusionBean.busiType eq '0'}">
					<tr>
						<td class="tr">宽带月费：</td>
						<td class="tl">${broadbandFusionBean.discntCode}元/月</td>
					</tr>
					<tr>
						<td class="tr">电视月费：</td>
						<td class="tl">
							<c:if test="${broadbandFusionBean.giveMbh==0}">
								免费赠送
							</c:if>
							<c:if test="${broadbandFusionBean.giveMbh!=0}">
								20元/月
							</c:if>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td class="tr">宽带月费：</td>
						<td class="tl">${broadbandFusionBean.monthCost}元/月</td>
					</tr>
					<tr>
						<td class="tr">电视月费：</td>
						<td class="tl">20元/月</td>
					</tr>
				</c:otherwise>
			</c:choose>
			<%--<tr>--%>
				<%--<td class="tr">安装费：</td>--%>
				<%--<td class="tl">--%>
					<%--${broadbandFusionBean.price}元--%>
				<%--</td>--%>
			<%--</tr>--%>
		</c:if>
		</table>
	</div>
</div>
<div class="fix-br">
	<div class="affix foot-box">
		<div class="container active-fix">
			<div class="fl-left">
				<p class="p1"><span class="cl-rose">　　　　　　　　　　　　　　　　
			</span></p>
			</div>
			<div class="fl-right"><a id="submitInstallOrder">提交订单</a></div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		$("#submitInstallOrder").click(function(){
			$("#myModal1").modal();
		});
		/**
		 * 订单确认
		 */
		$("#submitInstallOrder").bind("click", function () {
			var param = $("#form1").serializeObject();
			showLoadPop();
			$.post(ctx + "/broadbandFusion/submitOrder", param, function (data) {
				if (data.code == "0") {
					var orderSub=data.orderSub;
					if(orderSub.orderSubPayAmount>0&&orderSub.payModeId==2){
						//没有直接取orderSub的id是为了解决精度丢失问题
						window.location.href= ctx+'/broadbandOrder/payList?orderSubId='+data.orderSubId;
					}else{
						window.location.href= ctx + "BroadbandTrade/orderResult";
					}
				}
				else {
					hideLoadPop();
					if (data.message != '') {
						showAlert(data.message);
					} else {
						showAlert("业务办理失败！");
					}
				}
			});
		});

	});
	/**
	 * 将表单元素序列化为JSON对象
	 * 基于jQuery serializeArray()
	 */
	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};

</script>
</body>
</html>