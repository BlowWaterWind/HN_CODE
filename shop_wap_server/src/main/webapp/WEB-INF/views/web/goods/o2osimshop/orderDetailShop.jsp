<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/6/7
  Time: 17:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>O2O售卡-订单详情</title>
    <meta name="WT.si_n" content="O2O在线售卡能力_订单详情店铺">
    <meta name="WT.si_x" content="O2O在线售卡能力_订单详情店铺">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>


    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css"/>
    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet"><!--这个页面选号的弹出框-->

    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>

</head>
<body class="bg-gray2">
<div class="container">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return">返回</a>
        <a href="javascript:window.history.back();" class="icon icon-close">关闭</a>
        <a href="javascript:window.location.reload();" class="refresh"></a>
        <h3>订单详情</h3>
    </div>


    <div class="mt20">

        <div class="list-order list-order-flex">
            <div class="list-order-img"><img src="${ctxStatic}/images/simpay/pay_${conf.cardType}.png" alt=""></div>
            <div class="pl20 ">
                <p class="font-red">${orderSub.orderPayAmount/100}元</p>
            </div>
        </div>
    </div>
    <div class="mt20 bg-white p20 font-6 ">订单号：<span>${orderSub.orderSubNo}</span></div>
    <div class="mt20 bg-white p20 font-6 ">下单时间：<span><fmt:formatDate value='${orderSub.orderTime}'
                                                                      pattern='yyyy-MM-dd HH:mm:ss'/></span></div>

    <c:if test="${orderSub.orderAddressId == 1}"><!--到厅自取-->
    <div class="mt20 bg-white p20 font-6">取货方式：&nbsp;&nbsp;<span>到店自取</span></div>
    <div class="mt20 bg-white p20 font-6">店铺地址：&nbsp;&nbsp;<span>${orderSub.hallAddress}</span></div>
    <div class="mt20 bg-white p20 font-6"><a class="phone-bg font6"
                                             href="tel:${orderSub.valetStaffId}">到店联系电话:<span>${orderSub.valetStaffId}</span></a>
    </div>
    </c:if>

    <c:if test="${orderSub.orderAddressId == 2}"><!--送货上门或邮寄-->
    <div class="mt20 bg-white p20 font-6">取货方式：&nbsp;&nbsp;<span>快递邮寄</span></div>
    <div class="mt20 bg-white p20 font-6">送货地址：&nbsp;&nbsp;<span>${orderSub.recipient.orderRecipientProvince}
       ${orderSub.recipient.orderRecipientCity}${orderSub.recipient.orderRecipientCounty}${orderSub.recipient.orderRecipientAddress}
        </span></div>
    <div class="mt20 bg-white p20 font-6">收件人：&nbsp;&nbsp;<span>${orderSub.recipient.orderRecipientName}</span></div>
    <c:if test="${orderSub.logisticsCompany != null}">
        <div class="mt20 bg-white p20 font-6">物流公司：&nbsp;&nbsp;<span>${orderSub.logisticsCompany}</span></div>
    </c:if>
    <c:if test="${orderSub.logisticsNum != null}">
        <div class="mt20 bg-white p20 font-6" style="display: flex">
            <span>物流编号：&nbsp;&nbsp;</span>
            <input type="text" class="copy-bg font6" onclick="copylogisticsNum(this)" value="${orderSub.logisticsNum}" readonly></input>
        </div>
    </c:if>
    <div class="mt20 bg-white p20 font-6"><a class="phone-bg font6"
                                             href="tel:${orderSub.recipient.orderRecipientPhone}">联系电话:&nbsp;&nbsp;<span>${orderSub.recipient.orderRecipientPhone}</span></a>
    </div>
    </c:if>

    <div class="mt20 bg-white p20 font-6">
        状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span> ${orderSub.orderStatus.orderStatusName}</span></div>
    <c:if test="${orderSub.orderStatusId == 76}">
        <div class="mt160 tc">
            <a href="${ctx}/o2oSimOper/writesim?orderSubNo=${orderSub.orderSubNo}"
               class="btn btn-r btn-border w-50per btnQ">立即绑卡</a>
        </div>

    </c:if>
    <c:if test="${orderSub.orderStatusId == 2}">
        <c:if test="${orderSub.exceptionCause != null}">
            <div class="mt20 bg-white p20 font-6">同步失败原因：&nbsp;&nbsp;<span>${orderSub.exceptionCause}</span></div>
        </c:if>
        <div class="mt160 tc">
            <a onclick="toggleModal('confirm-toggle', '${orderSub.exceptionCause}');"
               class="btn btn-r btn-border w-50per btnQ">确认订单</a>
        </div>
    </c:if>

    <c:if test="${orderSub.orderStatusId == 79}">
        <c:if test="${orderSub.exceptionCause != null}">
            <div class="mt20 bg-white p20 font-6">同步失败原因：&nbsp;&nbsp;<span>${orderSub.exceptionCause}</span></div>
        </c:if>
    </c:if>
    <c:if test="${orderSub.orderStatusId == 7}">
        <div class="mt160 tc">
            <a onclick="processOrder('received')" otype="button" otitle="O2O在线售卡_订单扭转_${orderSub.orderSubNo}-确认签收"
               class="btn btn-r btn-border w-50per btnQ">确认签收</a>
        </div>
    </c:if>
</div>

<!-- 信息确认弹窗 -->
<div id="confirm-toggle" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('confirm-toggle')"></a>
        <div class="modal-content">
            <p>${orderSub.exceptionCause}</p>
            <ul class="confirm-list"></ul>
        </div>
        <div class="btn-group both-btn">
            <a onclick="processOrder('cancel')" id="infoEdit" otitle="O2O在线售卡_订单扭转_${orderSub.orderSubNo}-取消订单"
               class="confirm-btn">取消订单</a>
            <a onclick="processOrder('continueSim')" id="infoConfirmO2O"
               otitle="O2O在线售卡_订单扭转_${orderSub.orderSubNo}-继续同步" class="confirm-btn">重新同步</a>
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
            <a href="orderlist" class="confirm-btn">我知道了</a>
        </div>
    </div>
</div>


<%-- 公用弹窗 --%>
<div id="copy-toggle" class="mask-layer">
    <div class="modal small-modal">
        <a id="copy-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('copy-toggle')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group">
            <a onclick="javascript:void(0)" class="confirm-btn">我知道了</a>
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

<script type="text/javascript">
    var orderSubNo = "${orderSub.orderSubNo}";

    function processOrder(process) {
        $("#sorry-modal").hide();
        $("#confirm-modal").hide();
        toggleModal("loding-modal", "", "正在处理...");//打开加载框
        $.ajax({
            url: "${ctx}/o2oSimOper/process",
            dataType: "json",
            type: "post",
            data: {orderSubNo: orderSubNo, process: process},
            success: function (data) {
                $("#loding-modal").hide();
                if (data.code == '0') {
                    toggleModal("sorry-modal", data.message);
                } else {
                    toggleModal("sorry-modal", data.message);
                }

            },
            error: function () {
                $("#loding-modal").hide();
                toggleModal("sorry-modal", "", "订单处理失败");
            }
        });
    }

    function copylogisticsNum(obj) {
        var url2 = $(obj);
        url2.select(); // 选择对象
        document.execCommand("Copy"); // 执行浏览器复制命令
        toggleModal("copy-toggle", "物流编号已经复制!");
    }

</script>
</body>
</html>
