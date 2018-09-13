<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/7/5
  Time: 09:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<html>
<head>
    <title>O2O在线售卡能力</title>
    <meta name="WT.si_n" content="号段卡退款" >
    <meta name="WT.si_x" content="号段卡退款" >
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css" />
    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet"><!--这个页面选号的弹出框-->
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">

    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
</head>
<script>
    var orderSubNo = "${orderSubNo}";
</script>
<body class="bg-gray2">
<div class="container">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return">返回</a>
        <a href="javascript:window.history.back();" class="icon icon-close">关闭</a>
        <h3>号码退款</h3>
    </div>

    <div class=" clearfix ">
        <form id="simRefundApply">
            <div class="mt20 bg-white p20 font-6 ">订单手机号码：<span>${orderSub.phoneNumber}</span></div>
            <div class="mt20 bg-white p20 font-6 ">套餐：<span>${orderSub.detailSim.baseSetName}</span></div>
            <div class="mt20 bg-white p20 font-6 ">订单失败原因：<span>${orderSub.exceptionCause}</span></div>

            <dl class="common-list-info border-t mt160 border-b1">
                <dt>退款原因：</dt>
                <dd><input class="input-com input-border" type="textarea"  name="retMoneyReason" placeholder="请输入退款原因"></dd>
            </dl>
            <input type="hidden" name="orderSubNo" value="${orderSub.orderSubNo}">
            <%--<input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">--%>
        </form>

    </div>
    <div class="mt160 tc">
        <a onclick="refundApply()" class="btn btn-r btn-border w-50per btnQ">申请退款</a>
        <a href="process??orderSubNo=${orderSub.orderSubNo}" class="btn btn-r btn-border w-50per btnQ">继续同步</a>
    </div>
</div>

<%-- 公用弹窗 --%>
<div id="sorry-modal" class="mask-layer">
    <div class="modal small-modal">
        <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>

<%-- 公用弹窗 --%>
<div id="success-modal" class="mask-layer">
    <div class="modal small-modal">
        <a  href="javascript:void(0)" class="modal-close" onclick="toggleModal('success-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group">
            <a href="${ctx}/o2oSimOnline/getOrderDetail?orderSubNo=${orderSubNo}" class="confirm-btn">返回订单详情页</a>
        </div>
    </div>
</div>


<!-- 公共加载弹窗 -->
<div id="loding-modal" class="mask-layer">
    <div class="modal min-modal">
        <div class="modal-content">
            <div class="modal-con">
                <div class="dropload-load">
                    <div class="loading"></div>
                </div>
                <p class="text-center _promptSorry"></p>
                <p class="text-center _pomptTxt"></p>
                <div class="pb-20"></div>
            </div>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">
    var ctx = '${ctx}';
    function refundApply() {
        //发起退款申请
        toggleModal("loding-modal", "", "正在发起退款...");//打开加载框
        $.ajax({
            url: ctx + "simpay/retMoneyApply",
            type: "post",
            data: $("#simRefundApply").serializeObject(),
            dataType: "json",
            success: function (data) {
                $("#loding-modal").hide();
                if (data.code === "0") {
                    toggleModal("success-modal",data.message);
                }else{
                    toggleModal("sorry-modal", data.message);
                }
            },
            error:function(){
                $("#loding-modal").hide();
                toggleModal("sorry-modal", "", "退款提交失败");
            }
        });
    }


</script>
</html>
