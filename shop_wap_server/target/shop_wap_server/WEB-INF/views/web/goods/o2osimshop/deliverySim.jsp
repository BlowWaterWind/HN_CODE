<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/6/7
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<html>
<head>
    <title>O2O售卡-发货</title>
    <meta name="WT.si_n" content="号段卡发货" >
    <meta name="WT.si_x" content="号段卡发货" >
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
        <h3>O2O在线售卡-发货</h3>
    </div>

    <div class=" clearfix ">
        <form id="deliverySim">
            <div class="mt20 bg-white p20 font-6 ">订单手机号码：<span>${orderSub.phoneNumber}</span></div>
            <div class="mt20 bg-white p20 font-6 ">收货地址：<span>${orderSub.recipient.orderRecipientProvince}
                ${orderSub.recipient.orderRecipientCity}${orderSub.recipient.orderRecipientCounty}${orderSub.recipient.orderRecipientAddress}</span></div>
            <div class="mt20 bg-white p20 font-6 ">收货人：<span>${orderSub.recipient.orderRecipientName}</span></div>
            <div class="mt20 bg-white p20 font-6 ">收货电话：<span>${orderSub.recipient.orderRecipientPhone}</span></div>

            <dl class="common-list-info border-t mt160 border-b1">
                <dt>物流公司：</dt>
                <dd><i class="clear-index" style="margin-top: 5px;display: none"></i><input id="logisticsCompany" name="logisticsCompany" onkeyup="logisticsCompanyKey(this)" class="input-com input-border" type="text" placeholder="请输入快递公司名称"></dd>
            </dl>
            <dl class="common-list-info border-t mt160 border-b1" style="margin-top:10px">
                <dt>快递编号：</dt>
                <dd><i class="clear-index" style="margin-top: 5px;display: none"></i><input id="logisticsNum" name="logisticsNum" onkeyup="logisticsNumKey(this)" class="input-com input-border" type="text" placeholder="请输入快递编号"></dd>
            </dl>
            <input type="hidden" name="orderSubNo" value="${orderSubNo}">
            <input type="hidden" name="process" value="conDelivery">
            <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
        </form>

    </div>
    <div class="mt160 tc">
        <a onclick="deliverySim()" class="btn btn-r btn-border w-50per btnQ">立即发货</a>
    </div>
</div>

<!-- 找不到sim卡号提示弹窗 -->
<div id="prompt-toggle" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('prompt-toggle')"></a>
        <div class="modal-content">
            <div class="sim-img"><img src="${ctxStatic}/images/o2osim/card-info.png" style="height:200px;width: 200px;margin-left:52px;"/></div>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">确定</a>
        </div>
    </div>
</div>


<!-- 信息确认弹窗 -->
<div id="after-bind" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('after-bind')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group both-btn">
            <a href="javascript:void(0)" id="infoEdit" class="confirm-btn">重新绑卡</a>
            <a href="javascript:void(0)" id="infoConfirmO2O" class="confirm-btn"
               otype="button" otitle="O2O号码发货_${conf.confId}-确定">确定</a>
        </div>
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
            <a href="${ctx}/o2oSimOper/orderlist" class="confirm-btn">返回订单首页</a>
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


<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">

    function logisticsCompanyKey(obj) {
        if($(obj).val() != ''){
            $(obj).prev().show();
        }else{
            $(obj).prev().hide();
        }
    }

    function logisticsNumKey(obj) {
        if($(obj).val() != ''){
            $(obj).prev().show();
        }else{
            $(obj).prev().hide();
        }
    }
    function deliverySim(){
        var logisticsCompany = $("#logisticsCompany").val();
        if(logisticsCompany == ""){
            toggleModal("sorry-modal", "请输入快递公司名称!");
            return;
        }
        var logisticsCompany = $("#logisticsCompany").val();
        if(logisticsCompany == ""){
            toggleModal("sorry-modal", "请输入快递编号!");
            return;
        }
        toggleModal("loding-modal", "", "正在提交...");//打开加载框
        var params = $("#deliverySim").serializeObject();
        $.ajax({
           url:"${ctx}/o2oSimOper/process",
           dataType:"json",
           data:params,
           type:"post",
            success:function(data){
                $("#loding-modal").hide();
                if(data.code == '0'){
                    toggleModal("success-modal", "", "保存发货单号成功");
                    //流程页面嵌码 WT.si_n(流程名称) WT.si_x(步骤名称)
                    dcsPageTrack("WT.si_n", "O2O在线售卡", 0, "WT.si_x", "发货成功", 0);//插码使用
                }else{
                    toggleModal("sorry-modal", data.message, "保存发货单号失败");
                    //流程页面嵌码 WT.si_n(流程名称) WT.si_x(步骤名称)
                    dcsPageTrack("WT.si_n", "O2O在线售卡", 0, "WT.si_x", "保存发货单号失败", 0);//插码使用
                }
            },
            error:function(){
                $("#loding-modal").hide();
                toggleModal("sorry-modal", "", "系统异常");
                //流程页面嵌码 WT.si_n(流程名称) WT.si_x(步骤名称)
                dcsPageTrack("WT.si_n", "O2O在线售卡", 0, "WT.si_x", "保存发货单号失败", 0);//插码使用
            }
        });
    }

    $(".clear-index").click(function(){
        $(this).next().val("");//清空表单内容
        $(this).hide();
    });
</script>
</body>
</html>
