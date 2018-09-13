<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--插码相关--%>
	<meta name="WT.si_n" content="KDZW" />
	<meta name="WT.si_x" content="21" />
	<META name="WT.ac" content="">
	<Meta name="WT.mobile" content="">
	<Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-agreement.js"></script>
	<style>
		.broad-agre {color: #000000;}
		.broad-agre a{color: #0085d0;}
	</style>
	<%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
	<%--插码相关--%>
	<%--<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>--%>
	<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>

		<script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>确定套餐</h1>
  </div>
</div>
<form id="form1" action="submitOrder" method="post">
	<input type="hidden" name="phoneId" value="${phoneId}"/>
	<input type="hidden" name="installName" value="${installName}"/>
	<input type="hidden" name="idCard" value="${idCard}"/>
	
	<input type="hidden" name="productId" value="${productId}"/>
	<input type="hidden" name="packageId" value="${packageId}"/>
	<input type="hidden" name="goodsSkuId" value="${goodsSkuId}"/>
	
	<input type="hidden" name="isMbh" value="${isMbh}"/>
	<input type="hidden" name="isBroadBand" value="${isBroadBand}"/>

	<input type="hidden" name="houseCode" value="${houseCode}"/>
    <input type="hidden" name="address" value="${address}"/>
    <input type="hidden" name="install_county" value="${install_county}" />
    <input type="hidden" name="form1_coverType" value="${form1_coverType}"/>
    <input type="hidden" name="form1_maxWidth" value="${form1_maxWidth}"/>
    <input type="hidden" name="form1_freePort" value="${form1_freePort}"/>
    
	<input type="hidden" name="eparchyCode" value="${eparchyCode}"/>
	
	<input type="hidden" name="form1_Mbh" value="${form1_Mbh}"/>

	<input type="hidden" name="isBookInstall" value="${isBookInstall}"/>
	<input type="hidden" name="bookInstallDate" value="${bookInstallDate}"/>
	<input type="hidden" name="bookInstallTime" value="${bookInstallTime}"/>
	<input type="hidden" name="form1_communityType" value="${form1_communityType}">
	
	<input type="hidden" name="minCost" value="${minCost}"/>
	<input type="hidden" name="monthCost" value="${monthCost}"/>
	<input type="hidden" name="bandWidth" value="${bandWidth}"/>
	
	<input type="hidden" name="memberListStr" value="${memberListStr}"/>

	<input type="hidden" id="payMode" name="payMode" value="${payMode}">
	<input type="hidden" id="payModeName" name="payModeName" value="${payModeName}"></form>
<!--套餐选择 start-->
<div class="container">
  <div class="new-poplist clearfix">
  	<table cellpadding="0" cellspacing="0" class="new-tabs">
  	  <tr>
  	  	<td class="tr">活动名称：</td>
  	  	<c:choose>
  	  		<c:when test="${minCost == '0'}">
  	  		<td class="tl">组家庭网送${bandWidth}M宽带</td>
  	  		</c:when>
  	  		<c:otherwise>
  	  		<td class="tl">${minCost}元消费保底套餐</td>
  	  		</c:otherwise>
  	  	</c:choose>
  	  </tr>	
  	  <tr>
  	  	<td class="tr">联系电话：</td>
  	  	<td class="tl" id="td_phoneNum">${phoneId}</td>
  	  </tr>	
  	  <c:choose>
  	  	<c:when test="${isBroadBand == '1'}">
			<c:if test="${minCost ne '88' and minCost ne '98'}">
				<tr>
					<td class="tr">用户名称：</td>
					<td class="tl">${installName}</td>
				</tr>
				<tr>
					<td class="tr">身份证号码：</td>
					<td class="tl" id="td_idCard">${idCard}</td>
				</tr>
			</c:if>
		  	  <tr>
		  	  	<td class="tr">装机地址：</td>
		  	  	<td class="tl">${address}</td>
		  	  </tr>
			<c:if test="${minCost ne '88' and minCost ne '98'}">
			<tr>
		  	  	<td class="tr">装机时间：</td>
		  	  	<td class="tl"><c:choose>
					<c:when test="${isBookInstall == '1'}">
						${bookInstallDate} ${bookInstallTime}
					</c:when>
					<c:otherwise>
						尽快安装
					</c:otherwise>
				</c:choose></td>
		  	  </tr>
			</c:if>
  	  	</c:when>
  	  	<c:otherwise>
  	  		<tr>
	  	  		<td class="tr">宽带账户：</td>
	  	  		<td class="tl">${accessAcct}</td>
	  	  	</tr>
  	  	</c:otherwise>
  	  </c:choose>
  	  
  	  <c:if test="${isMbh == '1'}">
	  	  <tr>
	  	  	<td class="tr">电视牌照方：</td>
	  	  	<td class="tl">${form1_Mbh == "0"?"芒果TV":"未来电视"}</td>
	  	  </tr>	
  	  </c:if>
		<c:choose>
			<c:when test="${minCost eq '88' or minCost eq '98'}">
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
					<td class="tl">${minCost}元/月</td>
				</tr>
				<%--<tr>--%>
					<%--<td class="tr">设备费用：</td>--%>
					<%--<td class="tl">--%>
						<%--<p>10元/月</p>--%>
						<%--<p class="font-red">收取10个月，包含光猫与机顶盒</p>--%>
					<%--</td>--%>
				<%--</tr>--%>
				<tr>
					<td class="tr">合计费用：</td>
					<td class="tl">${minCost+20}元/月</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="tr">宽带月费：</td>
					<td class="tl">${monthCost}元/月</td>
				</tr>
				<tr>
					<td class="tr">电视月费：</td>
					<td class="tl">20元/月</td>
				</tr>
			</c:otherwise>
		</c:choose>
		<tr>
			<td class="tr">初装费：</td>
			<td class="tl">${price}元</td>
		</tr>
  	  <tr>
  	  	<td class="tr">支付方式：</td>
  	  	<td class="tl">${payModeName}</td>
  	  </tr>
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
	/*插码*/
	$(document).ready(function(){
		getGoodsId2();
	});
	$("#submitInstallOrder").click(function(){
		/*插码*/
		try{
			var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
			if(window.Webtrends){
				Webtrends.multiTrack({
					argsa: ["WT.si_n",sin,
						"WT.si_x","22"],
					delayTime: 100
				})
			}else{
				if(typeof(dcsPageTrack)=="function"){
					dcsPageTrack ("WT.si_n",sin,true,
							"WT.si_x","22",true);
				}
			}
		}catch (e){
			console.log(e);
		}
		showAlert("提交订单中,请稍等!");
		$("#form1").submit();
	});
});
</script>
</body>
</html>