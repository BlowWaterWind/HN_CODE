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
    <title>O2O在线售卡能力</title>
    <%--插码相关--%>
    <meta name="WT.si_n" content="O2O在线售卡客户订单查询">
    <meta name="WT.si_x" content="O2O在线售卡客户订单查询">
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
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
</head>
<body class="bg-gray2">
<div class="container bg-gray">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return">返回</a>
        <a href="javascript:window.history.back();" class="icon icon-close">关闭</a>
        <h3>我的订单</h3>
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
    <div class="mt20 bg-white p20 font-6 ">下单时间：<span><fmt:formatDate value='${orderSub.orderTime}' pattern='yyyy-MM-dd HH:mm:ss' /></span></div>
    <div class="mt20 bg-white p20 font-6 ">选择号码：<span>${orderSub.detailSim.phone}</span></div>
    <div class="mt20 bg-white p20 font-6 ">套餐名称：<span>${orderSub.detailSim.baseSetName}</span></div>


    <c:if test="${orderSub.orderAddressId == 1}"><!--到厅自取-->
        <div class="mt20 bg-white p20 font-6">取货方式：&nbsp;&nbsp;<span>到店自取</span></div>
        <div class="mt20 bg-white p20 font-6">店铺地址：&nbsp;&nbsp;<span>${orderSub.hallAddress}</span></div>
        <div class="mt20 bg-white p20 font-6"><a class="phone-bg font6" href="tel:${orderSub.valetStaffId}">到店联系电话:<span>${orderSub.valetStaffId}</span></a></div>
    </c:if>

    <c:if test="${orderSub.orderAddressId == 2}"><!--送货上门或邮寄-->
        <div class="mt20 bg-white p20 font-6">取货方式：&nbsp;&nbsp;<span>快递邮寄</span></div>
        <div class="mt20 bg-white p20 font-6">送货地址：&nbsp;&nbsp;<span>${orderSub.recipient.orderRecipientProvince}
       ${orderSub.recipient.orderRecipientCity}${orderSub.recipient.orderRecipientCounty}${orderSub.recipient.orderRecipientAddress}
        </span></div>
         <div class="mt20 bg-white p20 font-6">收件人：&nbsp;&nbsp;<span>${orderSub.recipient.orderRecipientName}</span></div>
        <div class="mt20 bg-white p20 font-6"><a class="phone-bg font6" href="tel:${orderSub.valetStaffId}">店铺联系电话：&nbsp;&nbsp;<span>${orderSub.valetStaffId}</span></a></div>
    </c:if>

    <div class="mt20 bg-white p20 font-6">状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span> ${orderSub.orderStatus.orderStatusName}</span></div>
</div>

<!-- 信息确认弹窗 -->
<div id="sorry-toggle" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-toggle')"></a>
        <div class="modal-content">
            <h4>信息确认</h4>
            <ul class="confirm-list"></ul>
        </div>
        <div class="btn-group both-btn">
            <a href="${ctx}/o2oSimOnline/process?orderSubNo=${orderSub.orderSubNo}&process=cancel" id="infoEdit" class="confirm-btn">取消订单</a>
            <a href="${ctx}/o2oSimOnline/process?orderSubNo=${orderSub.orderSubNo}&process=continue" id="infoConfirmO2O" class="confirm-btn">重新同步</a>
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
<script type="text/javascript">
    var clientType = isAppClient();
    if(clientType == 'weixin') {
        //微信打开调用微信的分享,并且不显示头
        $(".topbar").remove();
    }
</script>
</html>
