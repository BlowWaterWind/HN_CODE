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
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
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
<div class="container bg-gray hy-tab">
   <div class="wf-list sub-info sub-tc">
      <div class="wf-ait clearfix">
        <div class="wf-tit">宽带账号：${accessAcct}</div>
        <div class="wf-con">
          <p class="font-gray">套餐名称：<span class="font-3">${discntName}</span></p>
          <p class="font-gray">
            <span class="sus-tit">合约期限：</span>
            <span class="sus-text font-3">${startTime} ~ ${endTime}</span>
          </p>
          <p class="font-gray">
             <span class="sus-tit">安装地址：</span>
             <span class="sus-text font-3">${addrName}</span>
          </p>
          <p class="font-gray">装  机  人：<span class="font-3">${custName}</span></p>
          <p class="font-gray">身  份 证：<span class="font-3">${psptId}</span></p>
          <p class="font-gray">手　　机：<span class="font-3">${phoneId}</span></p>
          <%-- <p class="font-gray">支付方式：<span class="font-3">${payModeName}</span></p> --%>
        </div>
      </div>
      <div class="wf-ait clearfix">
        <div class="wf-tit wf-cit">商品详情</div>
        <div class="wf-con">
          <table cellpadding="0" cellspacing="0" class="wf-tabs">
             <tr>
               <th>商品名称</th>
               <th>数量</th>
               <th>商品单价</th>
             </tr>
             <tr>
               <td>魔百和${term}包</td>
               <td>1</td>
               <td>${price}元</td>
             </tr>
             <tr>
               <td>  <c:if test="${tvProductId=='20142000'}">
                   芒果TV
               </c:if>
                   <c:if test="${tvProductId=='20141000'}">
                       未来TV
                   </c:if></td>
               <td>1</td>
               <td>0元</td>
             </tr>
          </table>
        </div> 
      </div>
   </div>
</div>
<div class="fix-br">
  <div class="affix foot-box new-foot">
    <div class="container active-fix">
        <div class="fl-left">
            <p class="p1">应付总额：<b id="mbhPrice" class="cl-rose">${price}</b>元</p>
        </div>
        <div class="fl-right"><a class="share11" id="a_submitInstallOrder">${price == '0'?'立即办理':'立即支付'}</a></div>
        <!--当用户不能点击时在a class加dg-gray-->
    </div>
  </div>
</div>
<!--立即支付弹框 start-->
<div class="share-box share-sum"> <a class="cancel close-btn"></a> <span class="share-title">付款详情</span>
  	<form method="post" id="confirmForm" action="nopaysubmit">
		<input type="hidden" id="phoneId" name="phoneId" value="${phoneId}"/>
		<input type="hidden" id="goodsSkuId" name="goodsSkuId" value="${goodsSkuId}"/>
		<input type="hidden" id="accessAcct" name="accessAcct" value="${accessAcct}"/>
		<input type="hidden" id="tvProductId" name="tvProductId" value="${tvProductId}"/>
		<input type="hidden" id="reqPackageId" name="reqPackageId" value="${reqPackageId}"/>
		<input type="hidden" id="reqElementId" name="reqElementId" value="${reqElementId}"/>
		<input type="hidden" id="giftElementId" name="giftElementId" value="${giftElementId}"/>
		<input type="hidden" name="payMode" value=""/>
  	</form>
    <form id="payForm" name="payForm" method="post" action="pay">
    	<input type="hidden" id="payForm_orderSubNo" name="payForm_orderSubNo" />
		<input type="hidden" id="payForm_payPlatform" name="payForm_payPlatform" />  
		<input type="hidden" id="payForm_eparchyCode" name="payForm_eparchyCode" />  
    </form>
    <div class="share-content">
        <p>订　单：<b class="cl-red" id="b_orderNo"></b></p>
        <p>支付金额：￥<span id="span_price">0</span></p>
    </div>
    <div class="share-cz">
        <a href="#" class="cx-menu-item" name="a_pay" paytype="WAPIPOS_SHIPOS">
            <span class="item-img"><img src="${ctxStatic}/images/wt-images/hb_icon.png" /></span>
            <div class="cx-text">
                <p class="yw-title">和包支付</p>
            </div>
             
            <span class="jt-icon"></span>
        </a>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript">
$(function(){
    $("#a_submitInstallOrder").bind("click",function(){
//         var param = $("#confirmForm").serializeObject();
//         var mbhprice = $("#mbhPrice").html();
//         $.ajax({
// 			  url: ctx+"/mbh/submit", 
// 			  data: param,
// 			  dataType: "json",
// 			  type : 'post',
// 			  success: function(data){
// 				  if(data.code=="0"){
// 		                $("#payForm_orderSubNo").val(data.orderSub.orderSubNo);
// 		                $("#b_orderNo").text(data.orderSub.orderSubNo);
// 		                $("#span_price").text(data.orderSub.orderSubAmount/100);
// 		                $(".share-box").slideDown('fast');
// 		                if(mbhprice==0||mbhprice==""){
// 		                    window.location.href= ctx + "BroadbandTrade/orderResult";
// 		                    return false;
// 		                }
// 		            }
// 		            else {
// 		                showAlert(data.message);
// 		            }
// 			  },
// 			  error: function(e) {
// 				  showAlert("系统错误，请稍后重试！");
// 			  }
// 		});

    	showAlert("提交订单中,请稍等!");
    	$("#confirmForm").submit();
    });

    /*支付*/
    $("a[name='a_pay']").bind("click",function(){
        if($("#payForm_orderSubNo").val()==""){
            showAlert("提交订单中,请稍等!");
            return ;
        }
        $("#payForm_eparchyCode").val($("#form1_eparchyCode").val());
        $("#payForm_payPlatform").val($(this).attr("paytype"));
        $("#payForm").submit();

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