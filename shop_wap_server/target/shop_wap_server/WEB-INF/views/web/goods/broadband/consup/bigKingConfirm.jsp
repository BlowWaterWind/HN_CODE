<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
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
<form id="form1" action="submitGiftOrder" method="post">
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
    <input type="hidden" name="communityType" value="${communityType}"/>
    <input type="hidden" name="form1_coverType" value="${form1_coverType}"/>
    <input type="hidden" name="form1_maxWidth" value="${form1_maxWidth}"/>
    <input type="hidden" name="form1_freePort" value="${form1_freePort}"/>
    
	<input type="hidden" name="eparchyCode" value="${eparchyCode}"/>
	
	<input type="hidden" name="form1_Mbh" value="${form1_Mbh}"/>
	
	<input type="hidden" name="bookInstallDate" value="${bookInstallDate}"/>
	<input type="hidden" name="bookInstallTime" value="${bookInstallTime}"/>
	
	<input type="hidden" name="price" value="${price}"/>
	<input type="hidden" name="minCost" value="${minCost}"/>
	<input type="hidden" name="monthCost" value="${monthCost}"/>
	<input type="hidden" name="bandWidth" value="${bandWidth}"/>
	
	<input type="hidden" name="memberListStr" value="${memberListStr}"/>
</form>
<!--套餐选择 start-->
<div class="container">
  <div class="new-poplist clearfix">
  	<table cellpadding="0" cellspacing="0" class="new-tabs">
  	  <tr>
  	  	<td class="tr">活动名称：</td>
  	  		<td class="tl">大王卡套餐${bandWidth}M宽带</td>
  	  </tr>
  	  <tr>
  	  	<td class="tr">联系电话：</td>
  	  	<td class="tl" id="td_phoneNum">${phoneId}</td>
  	  </tr>	
		  	  <tr>
		  	  	<td class="tr">装机地址：</td>
		  	  	<td class="tl">${address}</td>
		  	  </tr>	
		  	  <tr>
		  	  	<td class="tr">装机时间：</td>
		  	  	<td class="tl">${bookInstallDate}
					<c:choose>
						<c:when test="${bookInstallTime == '0'}">
							08:00:00-12:00:00
								</c:when>
						<c:when test="${bookInstallTime == '3'}">
							12:00:00-14:00:00
								</c:when>
						<c:when test="${bookInstallTime == '4'}">
							14:00:00-18:00:00
								</c:when>
						<c:when test="${bookInstallTime == '5'}">
							18:00:00-20:00:00
								</c:when>
					</c:choose>
				</td>
		  	  </tr>

	  	  <tr>
	  	  	<td class="tr">电视牌照方：</td>
	  	  	<td class="tl">${form1_Mbh == "0"?"芒果TV":"未来电视"}</td>
	  	  </tr>	

  	  <tr>
  	  	<td class="tr">宽带月费：</td>
  	  	<td class="tl">${monthCost}元/天</td>
  	  </tr>	
  	  <tr>
  	  	<td class="tr">电视月费：</td>
  	  	<td class="tl">20元/月</td>
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
	$("#submitInstallOrder").click(function(){
		showAlert("提交订单中,请稍等!");
		$("#form1").submit();
	});
});
</script>
</body>
</html>