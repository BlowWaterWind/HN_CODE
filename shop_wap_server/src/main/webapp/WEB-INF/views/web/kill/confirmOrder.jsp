<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
	<%@include file="/WEB-INF/views/include/head.jsp"%>
	<meta name="WT.si_n" content="购物流程" />
	<meta name="WT.si_x" content="填写订单" />
	<meta name="WT.pn_sku" content="${skuIds}" />   <%--取值产品的id，多个产品用;隔开，必填--%>
	<meta name="WT.tx_s" content="${carModel.paymentAmount / 100}" >  <%--取值订单总价，必填--%>
	<meta name="WT.tx_u" content="1" >  <%--取值手机的数量，必填--%>
	<meta name="format-detection" content="telephone=no">
<title>确认订单</title>
<%@ include file="/WEB-INF/views/include/message.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/transition.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/collapse.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<style>
	.addressInput select{
		width: 200px;
		border-color: #0000FF;
		border-radius: 5px;
	}
	.addressInput input{
		width: 200px;
		background-color: #e8e8e8;
		border-radius: 5px;
	}
</style>
</head>
<body>
<form id="confirmOrderForm" action="${ctx}kill/submitOrder" method="post">
    <input type="hidden" name="marketId" id="marketId" value="${marketId}"/>
	<div class="top container">
	  <div class="header sub-title">
		  <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
	      <h1>确认订单</h1>
	  </div>
	</div>

	<div class="container pd-t45">

	  <%--收货地址start--%>
		  <div class="qr-dl">
			  <a id="chooseReceiptAddressBtn" href="javascript:void(0)">
				  <i class="icon-default05 pull-left"></i>
				  <div class="qr-con">
					  <c:choose>
						  <c:when test="${empty carModel.memberAddress}">
							  <div class="pull-left">新增地址</div>
							  <div class="clear"></div>
						  </c:when>
						  <c:otherwise>
							  <div class="pull-left">收货人：${carModel.memberAddress.memberRecipientName}</div>
							  <div class="pull-right">${carModel.memberAddress.memberRecipientPhone}</div>
							  <div class="clear"></div>
							  <div class="address address-con">
								  收货人地址：
								      ${fn:split(carModel.memberAddress.memberRecipientProvince,":")[1]}
									  ${fn:split(carModel.memberAddress.memberRecipientCity,":")[1]}
									  ${fn:split(carModel.memberAddress.memberRecipientCounty,":")[1]}
									  ${carModel.memberAddress.memberRecipientAddress}
							  </div>
							  <input type="hidden" name="memberAddress.memberAddressId" value="${carModel.memberAddress.memberAddressId}"/>
						  </c:otherwise>
					  </c:choose>
				  </div>
				  <i class="icon-default06 pull-right"></i>
			  </a>
		  </div>
	  <%--收货地址end--%>

	  <div class="cur-ul cur-con">
	   <div class="panel-group panel-classification panel-dy" id="accordion" role="tablist" aria-multiselectable="true" >
		<!-- 店铺列表start -->
		   <c:forEach items="${shopSet}" var="shop" varStatus="sStatus">
			   <div class="cur-li cur-bd">
				   <p class="tabcon-cl">
					   <span name="shopName" class="pull-jl02">商家：${shop.shopName}</span>
				   </p>
				   <div class="tabcon-cl tabcon-cl02 tabcon-cl05">
					   <!--  商品sku列表start -->
					   <c:set var="goodsBuyNumTotal" value="0"/>
					   <c:set var="goodsSalePriceTotal" value="0"/>
					   <c:forEach items="${carModel.userGoodsCarList}" var="userGoodsCar" varStatus="ugsStatus">
						   <c:if test="${userGoodsCar.shopId == shop.shopId}">
							   <dl>
								   <dt><img src="${tfsUrl}${userGoodsCar.goodsSkuImgUrl}" /></dt>
								   <dd>
									   <h2>${userGoodsCar.goodsName}</h2>
									   <c:set var="goodsStandardAttr" value="${fn:replace(userGoodsCar.goodsStandardAttr, '&amp;','，' )}"/>
									   <span class="tab-zh">${fn:replace(goodsStandardAttr, "=","：" )}</span>
								   </dd>
								   <i class="dy">
									   <p class="tabcon-cl tabcon-cl03">
										   <fmt:formatNumber value="${userGoodsCar.goodsSalePrice/100}" type="currency"/>
									   </p>
									   <p class="tabcon-cl tabcon-cl03 tab-lr">×${userGoodsCar.goodsBuyNum}</p>
									   <c:set var="goodsBuyNumTotal" value="${goodsBuyNumTotal + userGoodsCar.goodsBuyNum}"/>
									   <c:set var="goodsSalePriceTotal" value="${goodsSalePriceTotal + (userGoodsCar.goodsSalePrice * userGoodsCar.goodsBuyNum)}"/>
								   </i>
							   </dl>
						   </c:if>
					   </c:forEach>
				   </div>
				   <div class="sp-pj sp09 dl-tarea text-right">
					   <span>共${goodsBuyNumTotal}件商品</span>
						<span>实付款：
							<strong>
								<fmt:formatNumber value="${carModel.paymentAmount/100}" type="currency"/>
							</strong>
						</span>
				   </div>
			   </div>
		   </c:forEach>
	    <!-- 店铺列表end -->
	    <div class="cur-ly cul-lt">

		  <%--配送方式start--%>
	      <div class="sp-pj sp09"> 
	      	<a id="chooseDeliveryModeBtn" href="javascript:void(0);"> 
	      		<span class="gm-sl">配送方式</span>
				<input type="hidden" name="deliveryMode.deliveryModeId" value="1"/>
				<span class="gm-zj gm-zj02">物流配送</span>
				<span class="sp-icon"></span>
	      	</a>
	      </div>
		  <%--配送方式end--%>

		  <%--支付方式start--%>
	      <div class="sp-pj sp09"> 
	      	<a id="choosePayModeBtn" href="###">
				<input type="hidden" name="payMode.payModeId" value="2"/>
				<span class="gm-zj gm-zj02">在线支付</span>
	      		<span class="sp-icon"></span> 
	      	</a> 
	      </div>
		  <%--支付方式end--%>

		  <%--是否开发票start--%>
			  <div class="sp-pj sp09">
				  <a id="chooseInvoiceBtn" href="javascript:void(0)">
					  <span class="gm-sl">是否开发票</span>
					  <input type="hidden" name="isInvoicing" value="0"/>
					  <span class="gm-zj gm-zj02">不开</span>
					  <span class="sp-icon"></span>
				  </a>
			  </div>
		  <%--是否开发票end--%>

			  <div class="sp-pj sp09" id="receiptTimeIdContainer">
				  <a id="chooseReceiptTimeBtn" href="javascript:void(0)">
					  <span class="gm-sl">收货时间段</span>
					  <input type="hidden" name="receiptTime.receiptTimeId" value="2"/>
					  <span class="gm-zj gm-zj02">只工作日送货</span>
					  <span class="sp-icon"></span>
				  </a>
			  </div>

	      <div class="sp-pj sp09 dl-tarea">
	        <textarea name="orderSubRemark" class="sp-text">订单备注：</textarea>
	      </div>
	    </div>

		   <div class="wx-con">
			   <div class="tabcon-cl tab-fd">
				<span class="input-text02 input-text03">
					<input type="checkbox" class="checkbox" id="confirmCB"/>
					<label for="confirmCB" class="box-jl01">我已阅读并同意<a href="javascript:;" id="confirmModal">《湖南移动网上商城购前须知协议》</a></label>
				</span>
				<span class="input-text02"></span>
			   </div>
		   </div>
	   </div>
	  </div>
	</div>

	<div class="fix-br">
		<div class="affix foot-box">
			<div class="container active-fix active-fix02">
				<div class="jc-menu jc-fr">
							<span class="jc-tr">实付款：
								<strong>
									<fmt:formatNumber value="${carModel.paymentAmount/100}" type="currency"/>
								</strong>
							</span>
					<span><a href="javascript:void(0);"><input id="confirmBtn" class="btn btn-rose" type="button" value="确认"></a></span>
				</div>
			</div>
		</div>
	</div>
</form>

<!--阅读协议 begin-->
<div class="modal fade modal-hy in" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
	<div class="modal-content modal-content">
	  <div class="top container">
		<div class="header sub-title">
		  <a data-dismiss="modal" aria-label="Close" class="icon-left" id="closeForm"></a>
		  <h1>阅读协议</h1>
		</div>
	  </div>
	  <div class="container hy-content">
		 <div class="hy-con">
		 <h1>湖南移动网上商城购前须知协议</h1>
		 <p><strong>一、 下单须知</strong></p>
		 <p>您的订单信息是湖南移动网上商城（以下简称商城）处理订单及商家配送的依据，订单生成后您的订单信息将无法更改。故请您在下订单时仔细确认所购商品的名称、价格、数量、型号、联系地址、电话、收货人等信息。如因您的订单信息有误或您临时更改寄送地址造成订单取消或无法送达，商城将不承担责任。</p>
		 <p><strong>二、 购买裸机须知</strong></p>
		 <p>纯裸机：支持湖南移动客户及注册客户购买，购买数量不限（特殊活动除外，具体以活动页面说明为准）。</p>
		 <p><strong>三、购买合约机须知</strong></p>
		 <p><strong>（一）一个湖南移动号码仅能购买一台合约机。</strong></p>
		 <p>（二）正常情况下，您选购的合约方案将会在48小时内完成办理，下单号码赠送的话费自合约方案完成办理的当月起赠送，承诺的最低消费于次月生效。若遇特殊情况，您在月末三天申请的订单可能次月办理成功，合约方案可下一个月生效，如9月27日-30日购机，赠送的话费10月起赠送，承诺的最低消费11月起生效。</p>
		 <p>（三）其余未尽事宜以商城活动页面以及合约机购机协议为准。</p>
		 <p><strong>四、 物流配送</strong></p>
		 <p>（一）目前，商城订单由商家签订的物流公司负责配送。</p>
		 <p>（二）配送方式：</p>
		 <p>1.物流配送上门；</p>
		 <p>2.到店/厅自提（仅部分商家支持）；</p>
		 <p>具体请以订购界面为准；</p>
		 <p><strong>（三）配送范围：目前仅支持湖南区内物流配送上门服务。</strong></p>
		 <p>（四）配送费用：湖南区内免费配送。</p>
		 <p>（五）配送时限：商城订单一般5个工作日内可以送到，具体时间以您当地的派送情况为准。若遇不可抗因素，收货时间可能有所延误，特殊时期（如周末、法定节假日）需要购买的，以商家店铺说明为准。</p>
		 <p><strong>五、 商品签收</strong></p>
		 <p>（一）物流配送人员送货上门时，请您准备本人身份证原件，如不是您本人签收，请委托代签人持您本人身份证复印件、代签人身份证原件进行签收。物流配送人员在核对您的身份情况无误后，商品将交您当场检验，若您确认商品无误后，请在配送单签收信息处签名确认。商品签收注意事项如下：</p>
		 <p>1.收货时，首先请您确认外包装封装完好，无拆动痕迹，未被损坏、拆包，确认无误后再签收。</p>
		 <br/>
		 <br/>
		<div class="bot-btn"><a shape="rect" class="btn btn-blue btn-block center-btn" aria-label="Close" data-dismiss="modal" id="comfirm"  otype="button" otitle="确定" oarea="终端">确 定</a></div>
	  </div>
	</div>
  </div>
 </div>
</div>
<!--阅读协议 end-->

</body>
</html>