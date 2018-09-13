<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
					<textarea name="userGoodsCarList[${sStatus.count}${ugsStatus.index}].goodsRemark" class="sp-text">备注：</textarea>
				</c:if>
			</c:forEach>
		</div>
		<div class="sp-pj sp09 dl-tarea text-right">
			<span>共${goodsBuyNumTotal}件商品</span>
			<span>实付款：
				<strong>
					<%--<fmt:formatNumber value="${goodsSalePriceTotal/100}" type="currency"/>--%>
					<fmt:formatNumber value="${carModel.paymentAmount/100}" type="currency"/>
				</strong>
			</span>
		</div>
	</div>
</c:forEach>