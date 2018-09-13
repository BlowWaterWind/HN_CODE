<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<c:set value="确定套餐" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<%--<div class="top container">--%>
    <%--<div class="header sub-title">--%>
        <%--<a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>--%>
        <%--<h1>确定套餐</h1>--%>
    <%--</div>--%>
<%--</div>--%>
<div class="container bg-gray hy-tab">
    <div class="wf-list sub-info sub-tc">
        <div class="wf-ait clearfix">
            <div class="wf-tit">
            	宽带账号：${broadbandInfo.broadbandAccount}
            </div>
	        <div class="wf-con">
	          <p class="font-gray">套餐类型：<span class="font-3">${broadbandInfo.prodName}</span></p>
	          <p class="font-gray">
	            <span class="sus-tit">合同期限：</span>
	            <span class="sus-text font-3"> ${broadbandInfo.startTime}~${broadbandInfo.endTime}</span>
	          </p>
	          <p class="font-gray">
	             <span class="sus-tit">安装地址：</span>
	             <span class="sus-text font-3">${broadbandInfo.installAddress}</span>
	          </p>
	          <p class="font-gray">姓　　名：<span class="font-3">${broadbandInfo.custName}</span></p>
	        </div>
        </div>
        <div class="wf-ait clearfix">
            <div class="wf-tit wf-cit">套餐详情</div>
            <div class="wf-con">
                <table cellpadding="0" cellspacing="0" class="wf-tabs">
                    <tr>
                        <th>商品名称</th>
                        <th>数量</th>
                        <th>商品单价</th>
                    </tr>
                    <tr>
                        <td>
                        	<input type="hidden" id="goodsSkuId" name="goodsSkuId" value="${broadbandInfo.goodsSkuId}"/>
                        	${broadbandItem.broadbandItemName}
                        </td>
                        <td>1</td>
                        <td>
                            <fmt:formatNumber value="${broadbandInfo.price/100}" type="currency"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="wf-list change-new sub-new">
            <div class="wf-ait clearfix">
                <ul class="bar-list">
                    <li style="height:30px;line-height:25px;">
                        <span class="font-gray">支付方式：</span>
                        <div class="sub-text">
                            <a href="javascript:void(0);" class="bar-btn active" onclick="choosePayMode('0','在线支付',this)">在线支付</a>
                            <a href="javascript:void(0);" class="bar-btn" onclick="choosePayMode('6','余额支付',this)">余额支付</a>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div class="container active-fix">
            <div class="fl-left">
                <p class="p1 fl-con">应付总额：
                    <b class="cl-rose">
                        <fmt:formatNumber value="${broadbandInfo.price/100}" type="currency"/>
                    </b>
                </p>
            </div>
            <div class="fl-right"><a id="payBtn" class="shares">立即支付</a></div>
        </div>
    </div>
</div>
<!--立即支付弹框 start-->
<div class="share-box share-sum">
    <a class="cancel close-btn"></a>
    <span class="share-title">付款详情</span>
    <div class="share-content">
        <p>订单号：<b id="b_orderSubNo" class="cl-red"></b></p>
        <p>支付金额：<span id="orderSubPayAmount"></span></p>
    </div>
    <div class="share-cz">
       <form id="broadbandForm" action="submitOrder" method="post">
        <input type="hidden" id="broadbandType" name="broadbandType" value="${broadbandType}"/>
        <input type="hidden" id="orderSubNo" name="orderSubNo" />
        <input type="hidden" id="payPlatform" name="payPlatform"  />
        <input type="hidden" id="eparchyCode" name="eparchyCode" value="${broadbandInfo.eparchyCode}" />
        <input type="hidden" id="broadbandAccount" name="broadbandAccount" value="${broadbandInfo.broadbandAccount}" />
        <input type="hidden" id="payMode" name="payMode" value="${payMode}" />
        <input type="hidden" id="payModeName" name="payModeName" value="${payModeName}" />
        <a href="javascript:;" class="cx-menu-item">
            <span class="item-img"><img src="${ctxStatic}/images/wt-images/hb_icon.png" /></span>
            <div class="cx-text" onclick="payOrder('WAPIPOS_SHIPOS')">
            	<!--
            	<input type="radio" name="payPlatform" class="radio" value="WAPIPOS_SHIPOS"/>
            	-->
                <p class="yw-title">和包支付</p>
                <p class="font-red ml100 lh40">*首次开通和包快捷完成付款最高送20元电子券</p>
            </div>
            <span class="jt-icon"></span>
        </a>
        
       </form>
    </div>
</div>
<div class="more-box"></div>


<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript">
	//去支付
	function payOrder(payPlatform){
		$("#payPlatform").val(payPlatform);
		$("#broadbandForm").attr("action","payOrder");
		$("#broadbandForm").submit();
	}
    function choosePayMode(payMode,payModeName,param){
        $(".bar-list li a.active").toggleClass("active");
        $(param).addClass("active");
        $("#payMode").val(payMode);
        $("#payModeName").val(payModeName);
    }
    $(function () {
        //提交订单,选择支付平台支付
        $("#payBtn").on("click",function () {
        	if($("#orderSubNo").val()!='' ){
        		return ;
        	}
        	var payMode =  $("#payMode").val();
        	$("#confirmPayBtn").attr("disabled",true);
            if(payMode=='6'){
                hideLoadPop();
                setTimeout(function(){
                    window.location.href=ctx+"broadband/tempResult";
                },1500);
                return;
            }
            var url = "submitOrder";
            var param = {goodsSkuId:$("#goodsSkuId").val(),eparchyCode:$("#eparchyCode").val(),broadbandAccount:$("#broadbandAccount").val()};
            showLoadPop();
            $.post(url,param,function (data) {
                hideLoadPop();

            	if(data.code==0){
            		var orderSubPayAmount = "￥" + (Number(data.orderSub["orderSubPayAmount"])/100).toFixed(2);
                    $("#orderSubPayAmount").text(orderSubPayAmount);
                    $("#b_orderSubNo").text(data.orderSub["orderSubNo"]);
                    $("#orderSubNo").val(data.orderSub["orderSubNo"]);
                    $("#confirmPayBtn").attr("disabled",false);
                    $(".share-box").slideDown('fast');
                    $(".nav-slide").slideUp('fast');
                    $(".more-box").addClass("on");
            	}else {
            		showAlert(data.message);
            	}
            });
        });
    });
</script>
</body>
</html>