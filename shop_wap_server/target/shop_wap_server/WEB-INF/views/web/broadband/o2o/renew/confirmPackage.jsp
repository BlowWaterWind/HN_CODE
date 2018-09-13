<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script>
        var baseProject = "${ctx}" ;
        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
                + "wxyz0123456789+/" + "=";
    </script>
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
        <!--支付方式-->
        <div class="wf-list change-new sub-new">
            <div class="wf-ait clearfix">
                <ul class="bar-list">
                    <li style="height:30px;line-height:25px;">
                        <span class="font-gray">支付方式：</span>
                        <div class="sub-text">
                            <a href="javascript:void(0);" class="bar-btn active" onclick="choosePayMode('Q','现金支付',this)">现金支付</a>
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
            <div class="fl-right"><a id="payBtn" class="shares">立即办理</a></div>
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
        <input type="hidden" id="phoneNum" name="phoneNum" value="${phoneNum}"/>
        <input type="hidden" id="payPlatform" name="payPlatform"  />
        <input type="hidden" id="eparchyCode" name="eparchyCode" value="${broadbandInfo.eparchyCode}" />
        <input type="hidden" id="broadbandAccount" name="broadbandAccount" value="${broadbandInfo.broadbandAccount}" />
        <input type="hidden" name="staffPwd" id="staffPwd"/>
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
<!--输入密码弹框 start-->
<div class="modal modal-prompt" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog channel-modal">
        <div class="modal-info">
            <h4>提示</h4>
            <div class="modal-txt">
                <p><input type="password" class="form-control" name="password" id="password" placeholder="请输入密码"/></p>
            </div>
            <div class="modal-btn">
                <a href="javascript:void(0);" data-dismiss="modal" id="handleComfirmOrder">确定</a>
                <%--<button id="handleSpeedUp" class="btn btn-rose btn-block" data-dismiss="modal">立即办理</button>--%>
            </div>
        </div>
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
    $(function () {
        //提交订单,选择支付平台支付
        $("#payBtn").on("click",function () {
        	if($("#orderSubNo").val()!='' ){
        		return ;
        	}
        	$("#confirmPayBtn").attr("disabled",true);
            $("#myModal1").modal();
            return ;
        });

        /**
         * 续费订单办理
         */
        $("#handleComfirmOrder").bind("click",function(){
            if($("#password").val()==''){
                showAlert("请输入渠道工号密码！");
                return ;
            }
            var password = encode64($("#password").val());
            $("#staffPwd").val(password);
            var url = "submitOrder";
            var param = {goodsSkuId:$("#goodsSkuId").val(),eparchyCode:$("#eparchyCode").val(),broadbandAccount:$("#broadbandAccount").val(),staffPwd:password};
            showLoadPop();
            $.post(url,param,function (data) {
                if(data.code==0){
                    sendSms(data.orderSub.order_temp_id);
                }else {
                    hideLoadPop();
                    showAlert(data.message);
                }
            });
        });
    });
    //发送短信
    function sendSms(orderId){
        //var newBandWidth=$("#newBandWidth").val();
        $.ajax({
            type : 'post',
            url :  baseProject+'/o2oBusiness/invokeSmsInterface',
            cache : false,
            dataType : 'json',
            data : {
                goodsName : '宽带续费办理',
                serialNumber: $("#phoneNum").val(),
                broadbandType: '8',
                orderId: orderId
            },
            success : function(data) {
                hideLoadPop();
                if (data.X_RESULTCODE=='0') {
                    window.location.href= ctx + "o2oBusiness/smssuccess";
                    return false;
                    //testCallBack();
                } else {
                    showAlert(data.X_RESULTINFO);
                    return false;
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                hideLoadPop();
                showAlert("服务中断，请重试");
                $("#handleSpeedUp").removeClass('dg-gray').addClass('btn-rose').removeAttr("disabled");
                return false;
            }
        });
    }

    function encode64(password) {

        return strEnc(password, keyStr);
    }
    function choosePayMode(payMode,payModeName,param){
        $(".bar-list li a.active").toggleClass("active");
        $(param).addClass("active");
//        $("#payMode").val(payMode);
//        $("#payModeName").val(payModeName);
    }
</script>
</body>
</html>