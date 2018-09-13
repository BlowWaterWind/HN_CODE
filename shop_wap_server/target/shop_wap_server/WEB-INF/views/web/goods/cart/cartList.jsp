<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
int goodsCout = 0;
%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<meta name="WT.si_n" content="购物流程" />
	<meta name="WT.si_x" content="购物车" />
	<meta name="WT.pn_sku" content="${skuIds}" />   <%--取值产品的id，多台手机用;隔开，必填--%>
	<meta name="WT.tx_s" content="${cartGoodsNum}" >  <%--取值订单总价，必填--%>
	<meta name="WT.tx_u" content="${cartGoodsAmount}" >  <%--取值手机的数量，必填--%>
	<%@ include file="/WEB-INF/views/include/message.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
<script type="text/javascript" src="${ctxStatic}/js/goods/cartList.js"></script>

</head>
<body>
<div class="top container">
	<div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
		<h1>购物车</h1>
		<sys:headInfo cartTop="1"/>
	</div>
</div>


<form id="userGoodsCarListForm" action="${ctx}/cart/saveCart" method="post">
<div class="container pd-t45">
  <!-- 购物车列表Start -->
	  <c:forEach items="${shopSet}" var="shop" varStatus="sStatus">
		  <div type="shopDiv" class="cur-ul cur-list cur-cart">
		    <div class="cur-li cur-lr">
		      <div class="tabcon-cl">
		        <input type="checkbox" shopCheck="shopCheck" shopIdName="${shop.shopId}${shop.shopName}"/>
		        <label class="box-jl">${shop.shopName}</label>
		        <input type="hidden" value="${shop.shopId}"/>
		      </div>
		      <ul nam="goodsListUl">
		       <c:forEach items="${userGoodsCarList}" var="userGoodsCar" varStatus="ugsStatus">
		          <c:if test="${shop.shopId == userGoodsCar.shopId}">
					  <li name="goodsItemLi">
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsSkuUrl" value="${userGoodsCar.goodsSkuUrl}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsSkuImgUrl" value="${userGoodsCar.goodsSkuImgUrl}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsShortDesc" value="${userGoodsCar.goodsShortDesc}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].shopId" value="${userGoodsCar.shopId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].shopTypeId" value="${userGoodsCar.shopTypeId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].shopName" value="${userGoodsCar.shopName}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsStandardAttr" value="${userGoodsCar.goodsStandardAttr}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsStandardAttrId" value="${userGoodsCar.goodsStandardAttrId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsSalePrice" value="${userGoodsCar.goodsSalePrice}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsMarketPrice" value="${userGoodsCar.goodsMarketPrice}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsSkuId" value="${userGoodsCar.goodsSkuId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].categoryId" value="${userGoodsCar.categoryId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].userId" value="${userGoodsCar.userId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].memberId" value="${userGoodsCar.memberId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsId" value="${userGoodsCar.goodsId}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsName" value="${userGoodsCar.goodsName}"/>
						  <input type="hidden" name="userGoodsCarList[<%=goodsCout%>].goodsType" value="${userGoodsCar.goodsType}"/>
						  <div class="cur-fr pull-left">
			             	<input type="checkbox" goodsCheck="goodsCheck" shopIdName="${shop.shopId}${shop.shopName}" skuId="${userGoodsCar.goodsSkuId}"/>
			          	 	<input type="hidden" name="userGoodsCarList[<%=goodsCout%>].isChecked"/>
						  </div>
			          <div class="tabcon-cl tabcon-cl02 tabcon-fr">
			            <dl>
			              <dt><a href="${ctx}goods/${userGoodsCar.goodsSkuUrl}"><img src="${tfsUrl}${userGoodsCar.goodsSkuImgUrl}" /></a></dt>
			              <dd><h2><a href="${ctx}goods/${userGoodsCar.goodsSkuUrl}" onmouseover="$(this).attr('style', 'color:red')" onmouseout="$(this).removeAttr('style')">${userGoodsCar.goodsName}</a></h2>
			                <div class="tab-zh">
								<c:set var="goodsStandardAttr" value="${fn:replace(userGoodsCar.goodsStandardAttr, '&amp;','，' )}"/>
			                	<span>${fn:replace(goodsStandardAttr, "=","：" )}</span>
			                	<span class="tb-jg" skuId="${userGoodsCar.goodsSkuId}">￥${userGoodsCar.goodsSalePrice/100}</span>
                                <span class="tb-number">×${userGoodsCar.goodsBuyNum}</span>
			                </div>
			                <div class="sm-number" id="jr" style="display:none;">
                                <input class="min" name="" type="button" value="-" />
			                    <input class="text_box" name="userGoodsCarList[<%=goodsCout%>].goodsBuyNum" type="text" value="${userGoodsCar.goodsBuyNum}" skuId="${userGoodsCar.goodsSkuId}"/>
			                    <input class="add" name="" type="button" value="+" />
                            </div>
			              </dd>
			              <i class="icon-default02 default-fr"></i>
			            </dl>
			          </div>
			        </li>
					<%goodsCout++; %>
		          </c:if>
		       </c:forEach>
		      </ul>
		    </div>
		  </div>
	  </c:forEach>
  </div>
</form>
<!-- 购物车列表End -->

<div class="fix-br">
	<div class="affix foot-box">
		<div class="container active-fix active-fix02">
		<span class="pull-left active-fr">
			<input id="allCheck" type="checkbox"/>
			<label for="allCheck" class="radio-lc">全选</label>
		</span>
		<div class="jc-menu pull-right" elementType="cartList">
			  <span class=" pull-left">实付款：<strong id=goodsSalePriceTotal>￥0.00</strong><br>
			  <em>不含运费</em></span>
			<input id="billingBtn" class="btn btn-rose" type="button" value="去结算">
		</div>
		<div id="editFooterDiv" class="jc-menu jc-con pull-right" style="display: none">
			<!-- <input id="collectBtn" class="btn btn-blue" type="button" value="移入我的关注(0)" /> -->
			<input id="deleteBtn" class="btn btn-rose" type="button" value="删除(0)" />
		</div>
	  </div>
	</div>
</div>


<!--二次确认弹框 start-->
<div class="share-bit">
  <div class="share-text"> <span>确认删除此商品?</span> <a class="cancel cancel-con" id="delBtn">确认</a> </div>
  <div class="share-gb"><a class="cancel cancel-con">取消</a></div>
</div>
<div class="more-box"></div>
<!--二次确认弹框 end-->

</body>
</html>