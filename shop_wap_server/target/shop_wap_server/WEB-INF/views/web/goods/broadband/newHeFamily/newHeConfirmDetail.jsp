<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--插码--%>
	<META name="WT.si_n" content="">
	<META name="WT.si_x" content="22">
	<META name="WT.ac" content="">
	<Meta name="WT.mobile" content="">
	<Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
	<link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
	<script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script><link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<%--插码--%>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
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
<form id="form1" action="submitGiftOrder" method="post">
	<input type="hidden" id="form1_skuId" name="form1_skuId"  value="${goodsSkuId}"/>
	<input type="hidden" id="form1_goodsName" name="form1_goodsName"  value="${goodsName}"/>
	<input type="hidden" id="form1_addressName" name="form1_addressName"  value="${addressName}"/>
	<input type="hidden" id="form1_houseCode" name="form1_houseCode"  value="${houseCode}"/>
	<input type="hidden" id="form1_maxWidth" name="form1_maxWidth"  value="${maxWidth}"/>
	<input type="hidden" id="form1_freePort" name="form1_freePort"  value="${freePort}"/>
	<input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode"  value="${eparchyCode}"/>
	<input type="hidden" id="form1_county" name="form1_county"  value="${county}" />
	<input type="hidden" id="form1_coverType" name="form1_coverType"  value="${coverType}"/>
	<input type="hidden" id="form1_chooseCat" name="form1_chooseCat"  value="${chooseCat}"/>
	<input type="hidden" id="form1_chooseBandWidth" name="form1_chooseBandWidth"  value="${chooseBandWidth}"/>
	<input type="hidden" id="form1_productId" name="form1_productId" value="${productId}" />
	<input type="hidden" id="form1_packageId" name="form1_packageId" value="${packageId}" />
	<input type="hidden" id="form1_discntCode" name="form1_discntCode" value="${discntCode}" />
	<input type="hidden" id="form1_Mbh" name="form1_Mbh" value="${form1_Mbh}" />
	<input type="hidden" id="form1_hasBroadband" name="form1_hasBroadband" value="${hasBroadband}" />
	<input type="hidden" id="form1_hasMbh" name="form1_hasMbh" value="${hasMbh}" />
	<input type="hidden" id="form1_communityType" name="form1_communityType" value="${COMMUNITY_TYPE}" />
	<input type="hidden" id="bookInstallDate" name="bookInstallDate" value="${bookInstallDate}" />
	<input type="hidden" id="bookInstallTime" name="bookInstallTime" value="${bookInstallTime}" />
	<input type="hidden" id="form1_phoneNum" name="form1_phoneNum" value="${form1_phoneNum}" />
	<input type="hidden" id="installName" name="installName" value="${installName}" />
	<input type="hidden" id="idCard" name="idCard" value="${idCard}" />
	<input type="hidden" name="staffPwd" id="staffPwd"/>
	<input type="hidden" id="payMode" name="payMode" value="${payMode}">
	<input type="hidden" id="payModeName" name="payModeName" value="${payModeName}">
	<input type="hidden" id="contactPhone" name="contactPhone" value="${contactPhone}">
	<input type="hidden" id="isBookInstall" name="isBookInstall" value="${isBookInstall}">

</form>
<!--套餐选择 start-->
<div class="container">
  <div class="new-poplist clearfix">
  	<table cellpadding="0" cellspacing="0" class="new-tabs">
  	  <tr>
  	  	<td class="tr">活动名称：</td>
  	  		<td class="tl">${goodsName}</td>
  	  </tr>
  	  <tr>
  	  	<td class="tr">联系人：</td>
  	  	<td class="tl" id="td_installName">${installName}</td>
  	  </tr>	
  	  <tr>
  	  	<td class="tr">联系电话：</td>
  	  	<td class="tl" id="td_phoneNum">${contactPhone}</td>
  	  </tr>
		  	  <tr>
		  	  	<td class="tr">装机地址：</td>
		  	  	<td class="tl">${addressName}</td>
		  	  </tr>	
		  	  <tr>
		  	  	<td class="tr">装机时间：</td>
		  	  	<td class="tl">${bookInstallDate}
					<c:choose>
						<c:when test="${bookInstallTime == '0'}">
							08:00:00-12:00:00
								</c:when>
						<c:when test="${bookInstallTime == '1'}">
							12:00:00-14:00:00
								</c:when>
						<c:when test="${bookInstallTime == '2'}">
							14:00:00-18:00:00
								</c:when>
						<c:when test="${bookInstallTime == '3'}">
							18:00:00-20:00:00
								</c:when>
					</c:choose>
				</td>
		  	  </tr>

	  	  <tr>
	  	  	<td class="tr">电视牌照方：</td>
	  	  	<td class="tl">
				<c:if test="${form1_Mbh == '0'}">
					芒果TV
				</c:if>
				<c:if test="${form1_Mbh == '1'}">
					未来电视
				</c:if>
			</td>
	  	  </tr>	

  	  <tr>
  	  	<td class="tr">宽带月费：</td>
  	  	<td class="tl">${discntCode}元/月</td>
  	  </tr>	
  	  <tr>
  	  	<td class="tr">电视月费：</td>
  	  	<td class="tl">
			<c:if test="${form1_giveMbh==0&&form1_Mbh!=null}">
				免费赠送
			</c:if>
			<c:if test="${form1_giveMbh!=0&&form1_Mbh!=null}">
				20元/月
			</c:if>
		</td>
  	  </tr>	
  	  <%--<tr>--%>
  	  	<%--<td class="tr">安装费：</td>--%>
  	  	<%--<td class="tl">--%>
			<%--${form1_price}元--%>
		<%--</td>--%>
  	  <%--</tr>--%>
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
$(function() {
	/*插码*/
	getGoodsId("${goodsSkuId}","");
	$("#submitInstallOrder").click(function () {
		/*插码*/
		try{
			var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
			if(window.Webtrends){
				Webtrends.multiTrack({
					argsa: ["WT.si_n",sin,
						"WT.si_x","23"],
					delayTime: 100
				})
			}else{
				if(typeof(dcsPageTrack)=="function"){
					dcsPageTrack ("WT.si_n",sin,true,
							"WT.si_x","23",true);
				}
			}

		}catch (e){
			console.log(e);
		}
		$("#myModal1").modal();
	});
	/**
	 * 订单确认
	 */
	$("#submitInstallOrder").bind("click", function () {

		var param = $("#form1").serializeObject();
		showLoadPop();
		$.post(ctx + "/newHeBand/submitTempOrder", param, function (data) {
			if (data.code == "0") {
				/*插码*/
				try{
					var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
					if(window.Webtrends){
						Webtrends.multiTrack({
							argsa: ["WT.si_n",sin,
								"WT.si_x","99"],
							delayTime: 100
						})
					}else{
						if(typeof(dcsPageTrack)=="function"){
							dcsPageTrack ("WT.si_n",sin,true,
									"WT.si_x","99",true);
						}
					}

				}catch (e){
					console.log(e);
				}
				var orderSub=data.orderSub;
				if(orderSub.orderSubPayAmount>0&&orderSub.payModeId==2){
					//没有直接取orderSub的id是为了解决精度丢失问题
					window.location.href= ctx+'/broadbandOrder/payList?orderSubId='+data.orderSubId;
				}else{
					window.location.href= ctx + "BroadbandTrade/orderResult";
				}
			}
			else {
				/*插码*/
				try{
					var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
					if(window.Webtrends){
						Webtrends.multiTrack({
							argsa: ["WT.si_n",sin,
								"WT.si_x","-99"],
							delayTime: 100
						})
					}else{
						if(typeof(dcsPageTrack)=="function"){
							dcsPageTrack ("WT.si_n",sin,true,
									"WT.si_x","-99",true);
						}
					}

				}catch (e){
					console.log(e);
				}
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