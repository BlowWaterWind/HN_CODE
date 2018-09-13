<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 退款模板 -->
<script id="retMoneyWapTemplate" type="text/html">
{{each resList.page.list as val i}}
<li class="cur-li">
<p class="tabcon-cl">
  <span class="pull-left pull-jl">
	 <a href="#"><span class="dd-number">订单号：</span>{{val.orderSubId}}</a>
  </span> 
  <span class="pull-right pull-cl">{{val.orderStatus.orderStatusName}}</span>
</p>
<div class="tabcon-lr tabcon-cl02">
<dl>
  <dt><img src=""></dt>
  <dd>
	<h2>{{val.detailList[0].goodsName}}&nbsp;&nbsp;{{val.detailList[0].goodsFormat}}</h2>
	<span class="tab-zh">退款金额:￥{{val.orderSubPayAmount/100}}</span> 
	<span class="tab-zh">退款时间:{{val.refundTime }}</span> 
	<span class="tab-zh">退款原因:{{val.refundReason}}</span> 
  </dd>
  <div class="dy">
	<p class="tabcon-cl tabcon-cl03">{{val.detailList[0].goodsSkuPrice/100 }}</p>
	<p class="tabcon-cl tabcon-cl03 tab-lr">×{{val.detailList[0].goodsSkuNum }}</p>
  </div>
</dl>
</a>
</div>
</li>
{{/each}}
</script>

<!-- 退货模板 -->
<script id="retGoodWapTemplate" type="text/html">
{{each resList.page.list as val i}}
<li class="cur-li">
<p class="tabcon-cl">
  <span class="pull-left pull-jl">
	 <a href="{{resList.projectUrl}}/afterserviceWap/retGood/listDetail?returnOrderId={{val.returnOrderId}}&&returnOrderDetailId={{val.returnOrderDetailL[0].returnOrderDetailId}}"><span class="dd-number">退货单号：</span>{{val.returnOrderNo}}</a>
  </span> 
  <span class="pull-right pull-cl">{{val.orderStatusName}}</span>
</p>
<div class="tabcon-lr tabcon-cl02">
<dl>
  <dt><img src="{{resList.tfsUrl}}{{val.orderSub.detailList[0].goodsImgUrl}}"></dt>
  <dd>
	<h2>{{val.returnOrderDetailL[0].goodsName}}&nbsp;&nbsp;{{val.returnOrderDetailL[0].goodsFormat}}</h2>
	<span class="tab-zh">退款金额:￥{{val.actualReturnPrice/100}}</span> 
	<span class="tab-zh">退货时间:{{val.applyTime}}</span> 
	<span class="tab-zh">退货原因:{{val.returnReson}}</span> 
	{{if val.returnResonDesc}}
		<span class="tab-zh">原因描述:{{val.returnResonDesc}}</span>
	{{/if}}
  </dd>
  <div class="dy">
	<p class="tabcon-cl tabcon-cl03">{{val.returnOrderDetailL[0].goodsSkuPrice/100 }}</p>
	<p class="tabcon-cl tabcon-cl03 tab-lr">×{{val.returnOrderDetailL[0].goodsSkuNum }}</p>
  </div>
</dl>
</a>
</div>
</li>
{{/each}}
</script>

<!-- 换货模板 -->
<script id="changeGoodWapTemplate" type="text/html">
{{each resList.page.list as val i}}
<li class="cur-li">
<p class="tabcon-cl">
  <span class="pull-left pull-jl">
	 <a href="{{resList.projectUrl}}/afterserviceWap/changeGood/listDetail?changeOrderId={{val.changeOrderId}}&&changeOrderDetailId={{val.changeOrderDetailL[0].changeOrderDetailId}}"><span class="dd-number">换货单号：</span>{{val.changeOrderNo}}</a>
  </span> 
  <span class="pull-right pull-cl">{{val.orderStatusName}}</span>
</p>
<div class="tabcon-lr tabcon-cl02">
<dl>
  <dt><img src="{{resList.tfsUrl}}{{val.orderSub.detailList[0].goodsImgUrl}}"></dt>
  <dd>
	<h2>{{val.changeOrderDetailL[0].goodsName}}&nbsp;&nbsp;{{val.changeOrderDetailL[0].goodsFormat}}</h2>
	<span class="tab-zh">换货时间:{{val.applyTime}}</span> 
	<span class="tab-zh">换货原因:{{val.changeReson}}</span> 
	{{if val.changeResonDesc}}
		<span class="tab-zh">原因描述:{{val.changeResonDesc}}</span> 
	{{/if}}
  </dd>
  <div class="dy">
	<p class="tabcon-cl tabcon-cl03">{{val.changeOrderDetailL[0].goodsSkuPrice/100 }}</p>
	<p class="tabcon-cl tabcon-cl03 tab-lr">×{{val.changeOrderDetailL[0].goodsSkuNum }}</p>
  </div>
</dl>
</a>
</div>
</li>
{{/each}}
</script>

<!-- 售后单列表模板 -->
<script id="aftersaleServiceWapTemplate" type="text/html">
{{each resList.page.list as val i}}
<li class="cur-li">
<p class="tabcon-cl">
  <span class="pull-left pull-jl">
	 <a href="{{resList.projectUrl}}/afterserviceWap/aftersaleService/listDetail?appId={{val.aftersaleApplyId}}"><span class="dd-number">售后单号：</span>{{val.aftersaleApplyNum}}</a>
  </span> 
  <span class="pull-right pull-cl">{{val.aftersaleApplyStatusName}}</span>
</p>
<div class="tabcon-lr tabcon-cl02">
<dl>
  <dt><img src="{{resList.tfsUrl}}{{val.orderSub.detailList[0].goodsImgUrl}}"></dt>
  <dd>
	<h2></h2>
	<span class="tab-zh">售后申请时间:{{val.aftersaleApplyTime}}</span> 
	<span class="tab-zh">售后申请原因:{{val.aftersaleApplyReason}}</span> 
	{{if val.aftersaleApplyDescribe}}
	<span class="tab-zh">申请原因描述:{{val.aftersaleApplyDescribe}}</span>
	{{/if}} 
  </dd>
  <div class="dy">
	<p class="tabcon-cl tabcon-cl03"></p>
	<p class="tabcon-cl tabcon-cl03 tab-lr"></p>
  </div>
</dl>
</a>
</div>
</li>
{{/each}}
</script>
