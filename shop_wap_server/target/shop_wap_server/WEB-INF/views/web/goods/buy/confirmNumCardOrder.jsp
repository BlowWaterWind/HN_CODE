<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/transition.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/collapse.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/changetab.js"></script>
<script type="text/javascript">
	$(function(){
		//跳转到选择配送方式页面
		$("#chooseDeliveryModeBtn").click(function(){
			$("#confirmOrderForm").attr("action","linkToChooseDeliveryMode");
			$("#confirmOrderForm").submit();
		});
		
		//跳转到选择支付方式页面
		$("#choosePayModeBtn").click(function(){
			$("#confirmOrderForm").attr("action","linkToChoosePayMode");
			$("#confirmOrderForm").submit();
		});
		
		//填写入网信息
		$("#networkInfoBtn").click(function(){
			$("#confirmOrderForm").attr("action","linkToNetworkInfo");
			$("#confirmOrderForm").submit();
		});
		
		//跳转到填写推荐人页面
		$("#recommenderBtn").click(function(){
			$("#confirmOrderForm").attr("action","linkToRecommender");
			$("#confirmOrderForm").submit();
		});
		
		//提交订单
		$("#confirmBtn").click(function(){
			$("#confirmOrderForm").submit();
		});
	});
</script>
</head>
<body>
<div class="top container top-other">
  <div class="header sub-title">
  	<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>确认订单</h1>
  </div>
</div>

<form id="confirmOrderForm" action="goodsBuy/submitNumCardOrder" method="post">
	<c:forEach items="${carModel.userGoodsCarList}" var="car" varStatus="ugsStatus">
 		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsCarId" value="${userGoodsCar.goodsCarId}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsSkuUrl" value="${userGoodsCar.goodsSkuUrl}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].isAction" value="${userGoodsCar.isAction}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsSkuImgUrl" value="${userGoodsCar.goodsSkuImgUrl}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsShortDesc" value="${userGoodsCar.goodsShortDesc}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsStandardAttr" value="${userGoodsCar.goodsStandardAttr}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsSalePrice" value="${userGoodsCar.goodsSalePrice}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsMarketPrice" value="${userGoodsCar.goodsMarketPrice}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsSkuId" value="${userGoodsCar.goodsSkuId}"/>
  		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].userId" value="${userGoodsCar.userId}"/>
      	<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].memberId" value="${userGoodsCar.memberId}"/>
      	<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsName" value="${userGoodsCar.goodsName}"/>
		<input type="hidden" name="userGoodsCarList[${ugsStatus.index}].goodsBuyNum" value="${userGoodsCar.goodsBuyNum}"/>
	</c:forEach>
	<%--配送方式，自提网点--%>
	<input type="hidden" name="hallAddress" id="hallAddress" value="${carModel.hallAddress}"/>
	<%--推荐人--%>
	<c:if test="${!empty carModel.recommendContact}">
		<input type="hidden" name="recommendContact.recommendContactNo" value="${carModel.recommendContact.recommendContactNo}"/>
		<input type="hidden" name="recommendContact.recommendContactName" value="${carModel.recommendContact.recommendContactName}"/>
		<input type="hidden" name="recommendContact.recommendContactPhone" value="${carModel.recommendContact.recommendContactPhone}"/>
		<input type="hidden" name="recommendContact.recommendContactCity" value="${carModel.recommendContact.recommendContactCity}"/>
		<input type="hidden" name="recommendContact.recommendContactConty" value="${carModel.recommendContact.recommendContactConty}"/>
		<input type="hidden" name="recommendContact.recommendContactAddress" value="${carModel.recommendContact.recommendContactAddress}"/>
	</c:if>
	<%--入网信息--%>
	<input type="hidden" name="baseSet" value="${carModel.baseSet}"/>
	<input type="hidden" name="baseSetName" value="${carModel.baseSetName}"/>
	<input type="hidden" name="cardFee" value="${carModel.cardFee}"/>
	<input type="hidden" name="preFee" value="${carModel.preFee}"/>
	<input type="hidden" name="phone" value="${carModel.phone}"/>
	<input type="hidden" name="regName" value="${carModel.regName}"/>
	<input type="hidden" name="regType" value="${carModel.regType}"/>
	<input type="hidden" name="regNbr" value="${carModel.regNbr}"/>
	<input type="hidden" name="regAddress" value="${carModel.regAddress}"/>
	<c:forEach items="${carModel.simAttList}" var="simAttr" varStatus="saStatus">
		<input name="simAttList[${saStatus.index}].attName" type="hidden" attType="${simAttr.attName}"/>
		<input name="simAttList[${saStatus.index}].attPath" type="hidden" attType="${simAttr.attPath}"/>
		<input name="simAttList[${saStatus.index}].attType" type="hidden" value="${simAttr.attType}"/>
		<input name="simAttList[${saStatus.index}].attSeq" type="hidden" value="${simAttr.attSeq}"/>
	</c:forEach>
	<%--推荐人信息--%>
	<input type="hidden" name="recommendContact.recommendContactNo" value="${carModel.recommendContact.recommendContactNo}"/>
	<input type="hidden" name="recommendContact.recommendContactName" value="${carModel.recommendContact.recommendContactName}"/>
	<input type="hidden" name="recommendContact.recommendContactPhone" value="${carModel.recommendContact.recommendContactPhone}"/>
	<input type="hidden" name="recommendContact.recommendContactCity" value="${carModel.recommendContact.recommendContactCity}"/>
	<input type="hidden" name="recommendContact.recommendContactConty" value="${carModel.recommendContact.recommendContactConty}"/>
	<input type="hidden" name="recommendContact.recommendContactAddress" value="${carModel.recommendContact.recommendContactAddress}"/>

	<div class="container gray-hs hk-qrdd">
	  <div class="cur-ly cul-lt">
	    <div class="sp-pj sp09"> 
	    	<a id="chooseDeliveryModeBtn" href="javascript:void(0);"> 
	      		<span class="gm-sl">配送方式</span>
	      		<span class="gm-zj gm-zj02">到厅自提</span>
	      		<span class="sp-icon"></span>
	      	</a>
	    </div>
	    <div class="sp-pj sp09"> 
	   		<a id="choosePayModeBtn" href="javascript:void(0);"> 
	      		<span class="gm-sl">支付方式</span>
	      		<c:choose>
	      			<c:when test="${empty carModel.payMode}">
						<input type="hidden" name="payMode.payModeId" value="2"/>
						<span class="gm-zj gm-zj02">在线支付</span>
	      			</c:when>
	      			<c:otherwise>
						<input type="hidden" name="payMode.payModeId" value="${carModel.payMode.payModeId}"/>
						<span class="gm-zj gm-zj02">${carModel.payMode.payModeName}</span>
					</c:otherwise>
	      		</c:choose> 
	      		<span class="sp-icon"></span> 
		     </a> 
	   	</div>
	    <div class="sp-pj sp09"> 
	    	<a id="networkInfoBtn" href="javascript:void(0);"> 
	    		<span class="gm-sl">填写入网信息</span>
	    		<span class="sp-icon"></span> 
	    	</a> 
	    </div>
	    <div class="sp-pj sp09"> 
	    	<a id="recommenderBtn" href="javascript:void(0);"> 
	    		<span class="gm-sl">填写推荐人信息</span>
	    		<span class="sp-icon"></span> 
	    	</a> 
	    </div>
	  </div>
	  <div class="sp-tjr">
	    <div class="tabcon-cl tab-fd">
	      <div class="input-text02 input-text03">
	        <input type="checkbox" class="checkbox">
	        </input>
	        <label class="box-jl01">我已阅读并同意<strong>《湖南移动选号入网协议》</strong></label>
	      </div>
	    </div>
	  </div>
	</div>
	<!--所选号码信息悬浮-->
	<div class="fix-br fix-bt">
	  <div class="affix foot-box bulid-fix">
	    <div class="container sdd-btn">
	      <div class="xh-mesg">
	        <div class="mesg-text">所选靓号：<strong>${carModel.phone}</strong></div>
	        <div class="mesg-text">所选产品：${carModel.baseSetName}</div>
	        <div class="mesg-text">
	          <p>支付金额：预存话费<strong>${carModel.preFee}</strong>+卡费<strong>${carModel.cardFee}</strong>=<strong>${carModel.preFee + carModel.cardFee}元</strong></p>
	          <p class="mesg-cl">亲，提交订单后，请在30分钟内完成支付哦，超出后订单将自动取消。该商品不支持七天无理由退换货。</p>
	        </div>
	      </div>
	      <a id="confirmBtn" href="javascript:void(0);" class="btn btn-blue"  otype="button" otitle="确认，提交订单" oarea="终端">确认，提交订单</a> </div>
	  </div>
	</div>
</form>
<!--底部菜单start-->

<!--底部菜单end-->
</body>
</html>