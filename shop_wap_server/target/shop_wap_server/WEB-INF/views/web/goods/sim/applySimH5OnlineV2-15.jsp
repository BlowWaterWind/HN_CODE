<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：号段卡新和家庭两档--%>
<html>
<head>
    <%--<c:set var="mymyTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关--%>
        <meta name="WT.si_n" content="">
        <meta name="WT.si_x" content="">
        <meta name="WT.si_s" content="">
        <meta name="WT.ac" content="">

    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">

    <title>和掌柜在线售卡-首页</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css"/>
    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet"><!--这个页面选号的弹出框-->

    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <%-- 一个居中，三个换行居中的效果 --%>
    <style>
        .float-icon {
            width: 2.573333rem;
            height: 2.493333rem;
            position: fixed;
            right: 0.266667rem;
            top: 10%;
        }
    </style>
</head>
<body>
<div class="container">
    <img src="${myTfsUrl}${conf.titleImg}">
    <img src="${myTfsUrl}${conf.feeDetlImg}" id="zfxq"><!--展示套餐的资费信息-->
    <p class="index-title index-title-green">请选择业务档次</p>
    <div class="common-select">
        <c:forEach items="${plans}" var="ps">
            <a planId="${ps.planId}" planName="${ps.planName}">${ps.planName}</a>
        </c:forEach>
    </div>
    <div class="p20">
        <a class="btn btn-r w-100per btnNext" step="20">下一步,填写资料</a>
    </div>
    <div class="float-icon">
        <a onclick="shareSim()">
            <img src="${ctxStatic}/images/simo2o/share.png">
        </a>
    </div>

    <form id="applyForm" action="${ctx}o2oSimOnline/simO2OH5OnlineToConfirmOrder" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="recmdCode" id="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
        <input type="hidden" name="channelCityCode" id="channelCityCode">
        <input type="hidden" name="shopId" id="shopId">
        <input type="hidden" name="deliveryType" id="deliveryType">
    </form>
    <%--下面的用于微信分享--%>
    <input type="hidden" value="/res/img/${conf.titleImg}" id="titleImg">
    <input type="hidden" value="" id="recmdUserLink">
    <input type="hidden" value="${conf.cardTypeName}" id="cardTypeName">
    <input type="hidden" value="${conf.planDesc}" id="planDesc">
</div>

<%--选择号码--%>
<div id="choose-delivery" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('choose-delivery')"></a>
        <div class="modal-content">
            <h4>请确认自提点信息</h4>
            <ul class="confirm-list"></ul>
        </div>
        <div class="btn-group chooseType">
            <a data-dismiss="modal" class="modal-btn font-blue" otype="button" otitle="O2O在线售卡_${conf.confId}-自提立即申请"
               deliveryType="1">营业厅自提</a>
            <a data-dismiss="modal" class="modal-btn font-blue" otype="button" otitle="O2O在线售卡_${conf.confId}-送货立即申请"
               deliveryType="2">不自提，送货上门</a>
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


</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=6"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">
    var imgUrl = "/res/img/${conf.titleImg}";
    var ctx = "${ctx}";
    $(function () {
        /*给页面WT.si_n 赋初始值 插码*/
        getConfId("${conf.confId}","${conf.confId}");
        $(".common-select a").on("click", function () {
            $(this).addClass('active');
            $(this).siblings().removeClass('active');
            var planId = $(this).attr('planId');
            var planName = $(this).attr('planName');
            $("#applyForm input[name='planId']").val(planId);
            $("#applyForm input[name='planName']").val(planName);
        });
        //如果可以默认recmdLink 这段代码可以去掉
        $(".common-select").children(":first").click();
        var url = "http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?confId=" + getQueryString("confId") +
            "&recmdCode=" + getQueryString("recmdCode") + "&shopId=" + getQueryString("shopId");
        $("#recmdUserLink").val(url);
        //---
        //提交
        $('.btnNext').on("click", function () {
            var step = $(this).attr("step");
            $.ajax({
                url: ${ctx}+'o2oSimOnline/getShopInfo',
                type: "post",
                dataType: "json",
                data: {recmdCode: $("#recmdCode").val()},
                success: function (data) {
                    if (data.code == '0') {
                        $("#shopId").val(data.data.shopId);
                        var shopAdd = data.data.shopPhysicallProvince + data.data.shopPhysicallCity +
                            data.data.shopPhysicallCounty + data.data.shopPhysicallAddress;
                        var shopTime = "营业时间" + data.data.shopPhysicallOpenTime + "-" + data.data.shopPhysicallCloseTime;
                        var html = '<li>店铺名称:' + data.data.shopName + '</li>' +
                            '<li>店铺地址:' + shopAdd + '</li>' +
                            '<li>营业时间:' + shopTime + '</li>' +
                            '<li>联系电话:' + data.data.servicePhone + '</li>';
                        $(".confirm-list").append(html);
                        toggleModal('choose-delivery');
                        trace(step);//追踪码
                    } else {
                        toggleModal("sorry-modal", data.message, "订单提交失败");
                    }
                },
                error: function () {
                    //TODO 系统异常
                }
            });
        });

        $(".chooseType a").click(function () {
            var deliveryType = $(this).attr('deliveryType');
            $("#deliveryType").val(deliveryType);
            $("#applyForm").submit();
        });

        if (getQueryString("TYPE") == "") {
            $(".float-icon").hide();//分享出去不展示悬浮框,使用微信分享
        }

    });
</script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShare.js?v=201805"></script>
</html>