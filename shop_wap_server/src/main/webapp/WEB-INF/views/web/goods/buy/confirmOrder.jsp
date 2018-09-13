<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Session shrioSession = UserUtils.getSession();
	UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
%>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
	<meta name="WT.si_n" content="购物流程" />
	<meta name="WT.si_x" content="填写订单" />
	<meta name="WT.pn_sku" content="${skuIds}" />   <%--取值产品的id，多个产品用;隔开，必填--%>
	<meta name="WT.tx_s" content="${carModel.paymentAmount/100}" >  <%--取值订单总价，必填--%>
	<meta name="WT.tx_u" content="${goodsNumCount}" >  <%--取值手机的数量，必填--%>
	<meta name="format-detection" content="telephone=no">
<title>确认订单</title>
<%@ include file="/WEB-INF/views/include/message.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/transition.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/collapse.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
	<%--插码相关--%>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
<script type="text/javascript">
	var rootCategoryId = ${carModel.rootCategoryId};
	var _shopId = ${carModel.shopIdList[0]};
</script>
<script type="text/javascript" src="${ctxStatic}/js/goods/confirmOrder.js"></script>
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
<form id="confirmOrderForm" action="${ctx}goodsBuy/submitOrder" method="post">
<input type="hidden" name="marketId" id="marketId" value="${marketId}"/>
	<div class="top container">
	  <div class="header sub-title">
		  <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
	      <h1>确认订单</h1>
	  </div>
	</div>

	<div class="container pd-t45">
	  <%--收货地址start--%>
		<%-- 根据商品店铺进行选择收货地址方式，85店铺需要自己填写地址--%>
		  <c:if test="${carModel.shopIdList[0]==100000002099}">
			  <%@include file="receipt10085Address.jsp"%>
		  </c:if>
		  <c:if test="${carModel.shopIdList[0]!=100000002099 && carModel.rootCategoryId != 2}">
			  <%@include file="receiptAddress.jsp"%>
		  </c:if>
	  <%--收货地址end--%>
	  <div class="cur-ul cur-con">
	   <div class="panel-group panel-classification panel-dy" id="accordion" role="tablist" aria-multiselectable="true" >
		<!-- 店铺列表start -->
		   <c:if test="${carModel.rootCategoryId == 10000001 || carModel.rootCategoryId == 5}">
			   <%@include file="goodsList.jsp"%>
		   </c:if>
	    <!-- 店铺列表end -->
	    <div class="cur-ly cul-lt">
		  <%--合约信息start--%>
			<c:if test="${!empty carModel.orderDetailContract && carModel.userGoodsCarList[0].goodsType == 1}">
				<div class="sp-pj sp09">
					<a id="queryContractBtn" href="javascript:void(0);">
						<span class="gm-sl">合约套餐信息</span>
						<span class="gm-zj gm-zj02">
								${carModel.orderDetailContract.elementName}
						</span>
						<span class="sp-icon"></span>
						<input type="hidden" name="orderDetailContract.serialNumber" value="${carModel.orderDetailContract.serialNumber}"/>
						<input type="hidden" name="orderDetailContract.eparchyCode" value="${carModel.orderDetailContract.eparchyCode}"/>
						<input type="hidden" name="orderDetailContract.contractId" value="${carModel.orderDetailContract.contractId}"/>
						<input type="hidden" name="orderDetailContract.contractName" value="${carModel.orderDetailContract.contractName}"/>
						<input type="hidden" name="orderDetailContract.sumLimitPrice" value="${carModel.orderDetailContract.sumLimitPrice}"/>
						<input type="hidden" name="orderDetailContract.discount" value="${carModel.orderDetailContract.discount}"/>
						<input type="hidden" name="orderDetailContract.operFee" value="${carModel.orderDetailContract.operFee}"/>
						<input type="hidden" name="orderDetailContract.productId" value="${carModel.orderDetailContract.productId}"/>
						<input type="hidden" name="orderDetailContract.productName" value="${carModel.orderDetailContract.productName}"/>
						<input type="hidden" name="orderDetailContract.packageId" value="${carModel.orderDetailContract.packageId}"/>
						<input type="hidden" name="orderDetailContract.elementId" value="${carModel.orderDetailContract.elementId}"/>
						<input type="hidden" name="orderDetailContract.elementName" value="${carModel.orderDetailContract.elementName}"/>
						<input type="hidden" name="orderDetailContract.promisionDuration" value="${carModel.orderDetailContract.promisionDuration}"/>
						<input type="hidden" name="orderDetailContract.depositFee" value="${carModel.orderDetailContract.depositFee}"/>
						<input type="hidden" name="orderDetailContract.advancePay" value="${carModel.orderDetailContract.advancePay}"/>
						<input type="hidden" name="orderDetailContract.productTypeCode" value="${carModel.orderDetailContract.productTypeCode}"/>
						<input type="hidden" name="orderDetailContract.fee1" value="${carModel.orderDetailContract.fee1}"/>
						<input type="hidden" name="orderDetailContract.fee2" value="${carModel.orderDetailContract.fee2}"/>
						<input type="hidden" name="orderDetailContract.fee3" value="${carModel.orderDetailContract.fee3}"/>
						<input type="hidden" name="orderDetailContract.fee4" value="${carModel.orderDetailContract.fee4}"/>
						
					</a>
				</div>
			</c:if>
		  <%--合约信息end--%>

		  <%--配送方式start--%>
	      <div class="sp-pj sp09"> 
	      	<a id="chooseDeliveryModeBtn" href="javascript:void(0);"> 
	      		<span class="gm-sl">配送方式</span>
				<%--号卡订单的配送方式暂时只显示到厅自提--%>
				<c:if test="${carModel.rootCategoryId == 2 || carModel.rootCategoryId==0}">
					<c:choose>
						<c:when test="${empty carModel.deliveryMode}">
							<input type="hidden" name="deliveryMode.deliveryModeId" value="2"/>
							<span class="gm-zj gm-zj02">到厅自提</span>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="deliveryMode.deliveryModeId" value="${carModel.deliveryMode.deliveryModeId}"/>
							<span class="gm-zj gm-zj02">${carModel.deliveryMode.deliveryModeName}</span>
						</c:otherwise>
					</c:choose>
				</c:if>

				<c:if test="${carModel.rootCategoryId == 10000001 || carModel.rootCategoryId == 5}">
					<c:choose>
						<c:when test="${empty carModel.deliveryMode}">
							<input type="hidden" name="deliveryMode.deliveryModeId" value="1"/>
							<span class="gm-zj gm-zj02">物流配送</span>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="deliveryMode.deliveryModeId" value="${carModel.deliveryMode.deliveryModeId}"/>
							<span class="gm-zj gm-zj02">${carModel.deliveryMode.deliveryModeName}</span>
						</c:otherwise>
					</c:choose>
				</c:if>
				<span class="sp-icon"></span>
	      	</a>
	      </div>
		  <%--配送方式end--%>

		  <%--支付方式start--%>
	      <div class="sp-pj sp09"> 
	      	<a id="choosePayModeBtn" href="###">
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
		  <%--支付方式end--%>

		  <%--填写入网信息start--%>
			  <c:if test="${carModel.rootCategoryId == 2}">
				  <%@include file="networkInfo.jsp"%>
			  </c:if>
		  <%--填写入网信息end--%>

		  <%--是否开发票start--%>
			  <%@include file="invoicing.jsp"%>
		  <%--是否开发票end--%>

			  <c:choose>
			  	<c:when test="${empty carModel.deliveryMode}">
					<c:if test="${carModel.rootCategoryId == 10000001 || carModel.rootCategoryId == 5}">
				  		<%@include file="receiptTime.jsp"%>
					</c:if>
			   </c:when>
				  <c:when test="${carModel.deliveryMode.deliveryModeId == 1}">
					  <%@include file="receiptTime.jsp"%>
				  </c:when>
			  </c:choose>


		  <%--选择优惠券start--%>
			  <div class="sp-info sp03 sp-top sd-info" style="display: none" <c:if test="${carModel.rootCategoryId == 2}">style="display: none" </c:if> >
				  <a id="chooseCouponBtn" href="###">
					  <i class="sd-icon01 sd-icon02"></i>
					  <div class="pull-left pj-lt01">优惠券</div>
					  <div class="pull-right pj-right">
						   <span class="pj-text" style="color: #ff0000">
							   <c:if test="${!empty carModel.couponInfoList}">
								   <c:set var="couponValueCount" value="0"/>
								   <c:forEach items="${carModel.couponInfoList}" var="coupon" varStatus="couponStatus">
									   <c:set var="couponValueCount" value="${couponValueCount + coupon.couponValue}"/>
									   <input type="hidden" name='couponInfoList["+${couponStatus.index}+"].shopId' value="${coupon.shopId}"/>
									   <input type="hidden" name='couponInfoList["+${couponStatus.index}+"].couponId' value="${coupon.couponId}"/>
									   <input type="hidden" name='couponInfoList["+${couponStatus.index}+"].couponValue' value="${coupon.couponValue}"/>
								   </c:forEach>
								   -<fmt:formatNumber value="${couponValueCount/100}" type="currency"/>
							   </c:if>
						   </span>
					  </div>
					  <i class="sp-icon sp-icon04"></i>
				  </a>
			  </div>
		  <%--选择优惠券end--%>

	      <div class="sp-pj sp09 dl-tarea">
	        <textarea name="orderSubRemark" class="sp-text">订单备注：</textarea>
	      </div>
	    </div>

		<%--填写推荐人信息start--%>
		   <%@include file="recommender.jsp"%>
		<%--填写推荐人信息end--%>

	   <c:if test="${carModel.rootCategoryId == 10000001}">
		   <div class="wx-con">
			   <div class="tabcon-cl tab-fd">
				<span class="input-text02 input-text03">
					<input type="checkbox" class="checkbox" id="confirmCB"/>
					<label for="confirmCB" class="box-jl01">我已阅读并同意<a href="javascript:;" id="confirmModal">《湖南移动网上商城购前须知协议》</a></label>
				</span>
				<span class="input-text02">
					湖南移动“购手机办业务优惠”活动正在进行中，购买手机并且成功办理任意新业务，即可享受该业务连续三个月等额话费赠送的优惠&nbsp;&nbsp;
					<a href="http://wap.hn.10086.cn/wap/static/activity/migu/totalbusiness/index.html" style="color:red;">点击办理</a>
				</span>
			   </div>
		   </div>
	   </c:if>

	   <c:if test="${carModel.rootCategoryId == 2}" >
		   <!--自营厅 start-->
		   <div class="zy-con">
			   <div class="yt-select">
				   <label class="zy-tit font-red"><em>*</em>区县：</label>
				   <div class="zy-list">
					   <div class="yt-tit">
						   <span id="selectedOrg" orgId="">全部</span>
						   <i class="arrow"></i>
					   </div>
					   <input id="cityCode" type="hidden" value="${cityCode}"/>
					   <ul id="orgList" class="yt-list" style="display: none;">
						   <c:forEach items="${orgList}" var="org">
							   <li orgId="${org.orgId}">${org.orgName}</li>
						   </c:forEach>
					   </ul>
				   </div>
			   </div>
			   <table id="hallList" cellpadding="0" cellspacing="0" class="zy-tabs">
				   <thead>
					   <tr>
						   <th>区县</th>
						   <th>营业厅名称</th>
						   <th>营业厅地址</th>
					   </tr>
				   </thead>
				   <tbody>
				   <c:forEach items="${orgBusinessHallList}" var="hall">
					   <tr>
						   <td>${hall.orgName}</td>
						   <td>${hall.hallName}</td>
						   <td>${hall.hallAddress}</td>
					   </tr>
				   </c:forEach>
				   </tbody>
			   </table>
		   </div>
		   <!--自营厅 end-->
	   </c:if>

	   </div>
	  </div>
	</div>

	<c:choose>
		<c:when test="${carModel.rootCategoryId == 10000001 || carModel.rootCategoryId == 5}">
			<div class="fix-br">
				<div class="affix foot-box">
					<div class="container active-fix active-fix02">
						<div class="jc-menu jc-fr">
							<span class="jc-tr">实付款：
								<strong>
									<fmt:formatNumber value="${carModel.paymentAmount/100}" type="currency"/>
								</strong>
							</span>
							<span><a href="javascript:void(0);"><input id="confirmBtn" class="btn btn-rose" type="button" otype="button" otitle="商品订单提交跳转_终端" oarea="终端" value="确认"></a></span>
						</div>
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<!--所选号码信息悬浮-->
			<div class="fix-br fix-bt">
				<div class="affix foot-box bulid-fix">
					<div class="container sdd-btn">
						<div class="xh-mesg">
							<div class="mesg-text">所选靓号：<strong>${fn:substring(carModel.orderDetailSim.phone, 0, 11)}</strong></div>
							<div class="mesg-text">归属地市：<strong>${fns:getDictLabel(carModel.orderDetailSim.cityCode, 'CITY_CODE_CHECKBOXES', '长沙')}</strong></div>
							<div class="mesg-text">所选产品：${carModel.orderDetailSim.baseSetName}</div>
							<div class="mesg-text">
								<p>支付金额：预存话费<strong>${carModel.orderDetailSim.preFee}</strong>+卡费<strong>0.00</strong>=<strong>${carModel.orderDetailSim.preFee}元</strong></p>
								<p class="mesg-cl">亲，提交订单后，请在30分钟内完成支付哦，超出后订单将自动取消。该商品不支持七天无理由退换货。</p>
							</div>
						</div>
						<a id="confirmBtn" href="javascript:;" class="btn btn-blue"  otype="button" otitle="确认，提交订单" oarea="终端">确认，提交订单</a> </div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
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