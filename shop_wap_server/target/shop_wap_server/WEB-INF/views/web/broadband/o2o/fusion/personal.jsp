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
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/iOSselect/iosSelect.css"/>
	<script type="text/javascript" src="${ctxStatic}/js/datePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-agreement.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>

	<script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>消费叠加型</h1>
  </div>
</div>
<div class="container">
	<form action="toInstall" id="form1" name="form1" method="post">
	<input type="hidden" name="minCost" value="${minCost}" id="minCost"/>
	<input type="hidden" name="monthCost" id="monthCost"/>
	<input type="hidden" name="bandWidth" id="bandWidth"/>
	<input type="hidden" name="price" id="price"/>
	<input type="hidden" name="goodsSkuId" id="goodsSkuId"/>
	<input type="hidden" name="goodsName" id="goodsName"/>
	<input type="hidden" name="productId" id="productId"/>
	<input type="hidden" id="isMbh" name="isMbh" value="${isMbh}"/>
	<input type="hidden" id="isBroadBand" name="isBroadband" value="${isBroadBand}"/>
	<input type="hidden" id="busiType" name="busiType" value="${busiType}"/>
	<input type="hidden" id="installPhoneNum" name="serialNumber" value="${installPhoneNum}"/>
	<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<dl>
				<dt><img src="${tfsUrl}${broadItemList[0].imgUrl}"/></dt>
				<dd>
					<h4>${minCost}元保底套餐</h4>
					<p>${broadItemList[0].desc}</p>
				</dd>
			</dl>
		</div>
	</div>
	<!--套餐信息 end-->

	<!--选择套餐 start-->
	<div class="wf-list change-new sub-new">
		<div class="wf-ait clearfix">
			<ul class="bar-list">
				<c:choose>
					<c:when test="${minCost eq '88'}">
						<li> <span class="font-gray">宽带速率：</span>
							<div class="sub-text">
								<c:forEach items="${broadItemList}" var="broadbandItem">
									<a onclick="getBusiInfo('${broadbandItem.goodsSkuId}','${broadbandItem.bandWidth}','${broadbandItem.price}','${broadbandItem.productId}','${broadbandItem.broadbandItemName}','${broadbandItem.discntCode}')" class="bar-btn busi-btn">${broadbandItem.bandWidth}M</a>
								</c:forEach>
							</div>
						</li>
						<li> <span class="font-gray">优惠价：</span>
							<div class="sub-text">0元/月
								<del class="font-gray9 ml10" id="origPrice1">原价：${broadItemList[0].discntCode}元/月</del>
								<p class="f12 font-red" id="origPrice2">优惠24个月，第三年按${broadItemList[0].discntCode}元/月收取</p>
							</div>
						</li>
						<li> <span class="font-gray">电视月费：</span>
							<div class="sub-text">20元/月</div>
						</li>
						<%--<li> <span class="font-gray">设备费用：</span>--%>
							<%--<div class="sub-text">10元/月--%>
								<%--<p class="f12 font-red">收取10个月，包含光猫与机顶盒费用</p>--%>
							<%--</div>--
						<%--</li>--%>
					</c:when>
					<c:otherwise>
						<li> <span class="font-gray">宽带月费：</span>
							<div class="sub-text">
								<c:forEach items="${broadItemList}" var="broadbandItem">
									<a onclick="getBusiInfo('${broadbandItem.goodsSkuId}','${broadbandItem.bandWidth}','${broadbandItem.price}','${broadbandItem.productId}','${broadbandItem.broadbandItemName}','${broadbandItem.discntCode}')" class="bar-btn busi-btn">${broadbandItem.bandWidth}M<b>${broadbandItem.discntCode}元/月</b></a>
								</c:forEach>
							</div>
						</li>
						<li> <span class="font-gray">电视月费：</span>
							<div class="sub-text">20元/月</div>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
	<div class="wf-list change-new sub-new <c:if test="${isBroadBand == '1'}">hide</c:if>">
		<div class="wf-ait clearfix">
			<ul class="bar-list">
				<li>
					<span class="font-gray">我的宽带账户：</span>
					<div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.accessAcct}</div>
				</li>
			</ul>
		</div>
	</div>

	<div class="wf-list change-new sub-new hide">
		<div class="wf-ait clearfix">
			<ul class="bar-list">
				<li>
					<span class="font-gray">我的互联网电视账户：</span>
					<div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.accessAcct}</div>
				</li>
			</ul>
		</div>
	</div>

		<!--业务规则 start-->
		<div class="hjt-list">
			<div class="hjt-title">业务规则</div>
			<div class="hjt-rules">
				<div class="rules-list">
					<c:choose>
						<c:when test="${minCost eq '88'}">
							<p> 1、保底时间24个月，承诺保底期内不降挡或取消保底，24个月后按30元/月宽带功能费；</p>
							<p>2、魔百和标准资费为20元/月，从手机账户上扣取；</p>
							<p> 3、保底套餐当月办理，当月生效，当月按天扣费并分摊，宽带和魔百和免办理当月月功能费，不计入整体优惠期；</p>
							<p>4、手机号码停机，宽带、电视停机（宽带电视延迟3天停机，3天内手机复机，宽带电视不停机），手机号码复机，宽带、电视立即复机，手机销号或停机120天以上，同步取消宽带、电视和手机号码的绑定关系，终止宽带、电视。</p>
						</c:when>
						<c:otherwise>
							<p>（1）保底时间12个月，承诺保底期间内，客户不允许降档或取消保底，消费保底与其他保底活动不互斥，就高不就低原则。</p>
							<p>（2）互联网高清电视第一年10元/月，从第13个月起按20元/月从手机账户上扣取。</p>
							<p>（3）老用户宽带到期前参与本活动时，宽带的余额次月自动转入捆绑手机号码的现金存折（不可清退），可用于抵扣手机套餐费用。套餐、宽带当月生效，办理当月按天收取保底费用，当月宽带月功能费、互联网电视月功能费，不计入整体优惠期。</p>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<!--业务规则 end-->

		<div class="broad-agre">
			<label><input type="checkbox" id="isRead"/> 我已阅读并同意<a href="javascript:void(0);" data-toggle="modal" data-target="#agreementModal">《宽带入网协议》</a></label>
		</div>
	</form>
</div>

<div class="fix-br">
	<div class="affix foot-box new-foot">
		<div class="container active-fix">
			<div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#myModal">温馨提示</a></div>
			<div class="fl-right"><a id="operBtn">下一步</a></div>
		</div>
	</div>
</div>
<!--温馨提示弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog new-modal">
		<div class="modal-dow">
			<h4>温馨提示</h4>
			<div class="modal-text">
				<c:choose>
					<c:when test="${minCost eq '88'}">
						<p> 1、保底时间24个月，承诺保底期内不降挡或取消保底，24个月后按30元/月宽带功能费；</p>
						<p>2、魔百和标准资费为20元/月，从手机账户上扣取；</p>
						<p> 3、保底套餐当月办理，当月生效，当月按天扣费并分摊，宽带和魔百和免办理当月月功能费，不计入整体优惠期；</p>
						<p>4、手机号码停机，宽带、电视停机（宽带电视延迟3天停机，3天内手机复机，宽带电视不停机），手机号码复机，宽带、电视立即复机，手机销号或停机120天以上，同步取消宽带、电视和手机号码的绑定关系，终止宽带、电视。</p>
					</c:when>
					<c:otherwise>
						<p>（1）办理号码承诺保底消费不低于48元/月，保底时间为12个月，承诺期内不允许降档、取消保底。</p>
						<p>（2）互联网高清电视第一年10元/月，从第13个月起按20元/月从手机账户上扣取。</p>
						<p>（3）老用户宽带到期前参与本活动时，宽带的余额次月自动转入捆绑手机号码的现金存折（不可清退），可用于抵扣手机套餐费用。套餐、宽带当月生效，办理当月按天收取保底费用，当月宽带月功能费、互联网电视月功能费，不计入整体优惠期。</p>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
		</div>
	</div>
</div>
<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
	$(function () {

		$(".busi-btn").click(function(){
			$(".busi-btn").removeClass("active");
			$(this).addClass("active");
		});

		$("#operBtn").click(function(){
			if($("#goodsSkuId").val()==''){
				showAlert("请选择套餐!");
				return;
			}
			if(!$('#isRead').is(':checked')){
				showAlert("请阅读并同意《宽带入网协议》!");
				return;
			}
			$("#form1").submit();
		})

		$(".bar-list li:first div a:first").click();
	});

	function getBusiInfo(goodsSkuId,bandWidth,price,productId,goodsName,discntCode){
		$("#goodsSkuId").val(goodsSkuId);
		$("#goodsName").val(goodsName);
		$("#productId").val(productId);
		$("#monthCost").val(discntCode);
		$("#price").val(price);
		$("#bandWidth").val(bandWidth);
		if($("#minCost").val()=='88') {
			$("#origPrice1").html("原价：" + discntCode + "元/月");
			$("#origPrice2").html("优惠24个月，第三年按" + discntCode + "元/月收取");
		}
	}
</script>

</body>
</html>