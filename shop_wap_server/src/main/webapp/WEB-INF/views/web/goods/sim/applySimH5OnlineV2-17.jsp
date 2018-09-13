<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：校园促销--%>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关--%>
    <meta name="WT.si_n" content="">
    <meta name="WT.si_x" content="">
    <meta name="WT.si_s" content="">

    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <title>校园促销-首页</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css"/>
    <link href="${ctxStatic}/css/sim/sim-school.css" rel="stylesheet"><!--这个页面选号的弹出框-->
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/wap-simcommon.css?v=1" rel="stylesheet"><!--这个页面选号的弹出框-->


    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>

    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
</head>
<body class="bg-event">

<div class="container">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}" alt="校园网上选号">
        <div class="event-time">活动时间：即日起——2018年9月30日</div>
    </div>
    <div class="pd10 hk-list">
        <div class="title div-need-hide" style="display: none">
            <span class="text">业务办理</span>
        </div>
        <form id="applyForm">
            <div class="user-info-wrap" style="display: block">

                <ul class="user-info">
                    <li class="user-info-item">
                        <div class="label">待激活手机号码</div>
                        <div class="input-group">
                            <i class="clear-index" style="display: none"></i>
                            <c:if test="${transactionId == null}">
                                <input type="text" placeholder="手机号码" class="input" name="orderDetailSim.phone"
                                       id="validatePhone" onkeyup="phoneKey(this)">
                            </c:if>
                            <c:if test="${transactionId != null}">
                                <input type="text" class="input" name="orderDetailSim.phone" value="${phone}" readonly>
                            </c:if>
                            <p class="error">手机号码不能为空</p>
                        </div>
                    <li class="user-info-item">
                        <div class="label">SIM卡后六位</div>
                        <div class="input-group">
                            <i class="clear-index" style="display: none"></i>
                            <c:if test="${transactionId == null}">
                                <input type="text" placeholder="请输入SIM卡后六位" class="input" name="orderDetailSim.iccid"
                                       id="iccid" onkeyup="iccidKey(this)" value="${iccid}"/>
                            </c:if>
                            <c:if test="${transactionId != null}">
                                <input type="text" class="input" name="orderDetailSim.iccid" value="${iccid}" readonly>
                            </c:if>
                            <p class="error">SIM卡后六位不能为空</p>
                        </div>
                    </li>
                    <li class="user-info-item">
                        <div class="label">身份验证</div>
                        <div class="input-group">
                            <c:if test="${transactionId == null}">
                                <p id="veritify" class="input">点击进行身份验证</p>
                                <i class="arrow arrow-right"></i>
                            </c:if>
                            <c:if test="${transactionId != null}">
                                <p id="veritify" class="input">点击进行身份验证</p>
                            </c:if>
                            <p class="error">身份证号码有误</p>
                        </div>
                    </li>
                </ul>
                <div class="name-tips">根据国家实名制要求，请提供准确身份证信息</div>

                <a onclick="infomationConfirm()" class="link-btn-small">点击开卡</a>
            </div>
            <input type="hidden" name="confId" value="${confId}"/>
            <input type="hidden" name="recmdCode" id="recmdCode" value="${recmdCode}"/><%--从request中取推荐码--%>
            <input type="hidden" name="CHANID" value="${CHANID}"/>
            <input type="hidden" value="${shopId}" name="shopId">
            <input type="hidden" name="transactionId" value="${transactionId}" id="transactionId">
        </form>
        <%--<p class="pd10">方式2：对多个号码进行业务办理请点击 <a href="##" onclick="batchShare();" class="font-blue" style="padding-right:10px"  otitle="批量办理" otype="button" oarea="批量办理">批量办理</a></p>--%>
        <p class="pd10 div-need-hide" style="display: none">方式2：分享给用户自行办理<a id="shareBtn" href="##" onclick="shareSim();" otitle="o2oel分享" otype="button" oarea="o2oel分享">
            <img src="${ctxStatic}/images/o2osim/fx-icon.png" style="width:0.8rem;margin-left:0.2rem;"></a>
        </p>
        <p class="pd10 div-need-hide" style="display: none">方式3：群发短信推荐用户办理<a id="smsBtn" href="##" onclick="smsDirect();" otitle="o2oSms分享" otype="button" oarea="o2oSms分享">
            <img src="${ctxStatic}/images/o2osim/sms1.png" style="width:0.8rem;margin-left:0.2rem;"></a>
        </p>
    </div>
    <input type="hidden" name="regName" value="${regName}">
    <input type="hidden" name="cardTypeName" value="${conf.cardTypeName}">
    <input type="hidden" name="cardTypeDesc" value="${conf.cardTypeName}">
    <%--下面的用于微信分享--%>
    <input type="hidden" value="/res/img/${conf.titleImg}" id="titleImg">
    <input type="hidden" value="" id="recmdUserLink">
    <input type="hidden" value="${conf.cardTypeName}" id="cardTypeName">
    <input type="hidden" value="${conf.planDesc}" id="planDesc">
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

<!-- 订购成功弹窗 -->
<div id="orders-success" class="mask-layer">
    <div class="modal small-modal">
        <a href="javascript:void(0)" onclick="toggleModal('orders-success')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-blue text-center">您已成功订购号卡!</p>
            <p class="text-blue text-center fz-24">请关注“湖南移动微厅"查询更多详情</p>
            <p class="text-gray text-center fz-24">(可在”我的订单“中查看详情)</p>
            <div class="modal-btn-wrap">
                <a href="https://smz.cmcc-cs.cn:30026/edcreg-web/videorealname/realnameActive/realNameActivateM.html?"
                   class="modal-btn">去激活</a>
            </div>
        </div>
    </div>
</div>
<!-- end container -->

<!-- 信息确认弹窗 -->
<div id="info-confirm" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('info-confirm')"></a>
        <div class="modal-content">
            <h4>信息确认</h4>
            <ul class="confirm-list"></ul>
        </div>
        <div class="btn-group both-btn">
            <a href="javascript:void(0)" id="infoEdit" class="confirm-btn">编辑</a>
            <a href="javascript:void(0)" id="infoConfirm" class="confirm-btn"
               otype="button" otitle="H5号卡销售_${conf.confId}-确定">确定</a>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">
    var ctx = ${ctx};
    var transactionId = '${transactionId}';
    var imgUrl = "/res/img/${conf.titleImg}";
    $(function () {
        if (getQueryString("CHANID") == "E050"){
            $(".div-need-hide").show();
        }
    });

    function phoneKey(obj) {
        if ($("#validatePhone").val() != "") {
            $(obj).prev().show();
        } else {
            $(obj).prev().hide();
        }
        $("#validatePhone").val($.trim($("#validatePhone").val()));
    }

    function iccidKey(obj){
        if ($("#iccid").val() != "") {
            $(obj).prev().show();
        } else {
            $(obj).prev().hide();
        }
        $("#iccid").val($.trim($("#iccid").val()));
    }

    $(".clear-index").click(function(){
        $(this).next().val("");//清空表单内容
        $(this).hide();
    });

    $("#veritify").click(function () {
        if ($("#validatePhone").val().length == 0) {
            toggleModal("sorry-modal", "请输入待激活的号码");
            return;
        }
        toggleModal("loding-modal", "", "正在跳转...");//打开加载框
        //点击跳转到在线公司的页面
        var params = $("#applyForm").serializeObject();
        $.ajax({
            url: ctx + 'schoolSim/payAppointment',
            data: params,
            dataType: "json",
            type: "post",
            success: function (data) {
                $("#loding-modal").hide();
                if (data.code === "0") {
                    ajaxSuccess();
                    window.location.href = data.data;
                } else {
                    if (data == '页面过期，请刷新页面') {
                        toggleModal("sorry-modal", "页面过期，请刷新页面");
                    } else {
                        toggleModal("sorry-modal", data.message);
                    }
                    ajaxFail();
                }

            },
            error: function () {
                toggleModal("sorry-modal", "身份认证提交失败");
                ajaxFail();
            }
        });
    });

    function infomationConfirm(){
        if ($("#transactionId").val().length == 0) {
            toggleModal("sorry-modal", "请先进行身份验证");
            return;
        }
        if($("#iccid").val()==''){
            toggleModal("sorry-modal", "请输入SIM卡号后6位");
            return;
        }
        if ($("#validatePhone").val().length == 0) {
            toggleModal("sorry-modal", "请输入待激活的号码");
            return;
        }
        var confirmInfo = "<li>姓名：" + $("#regName").val() + "</li>" +
            // "<li>身份证：" + $("#psptId").val() + "</li>" +
            "<li>待激活的号码：" + $("#validatePhone").val() + "</li>" +
            "<li>SIM卡号后六位：" + $("#iccid").val() + "</li>";
        $(".confirm-list").append(confirmInfo);
        toggleModal("info-confirm");
    }

    $("#infoConfirm").click(function(){
        if (transactionId == '') {
            toggleModal("sorry-modal", "请先进行实名制激活");
            return;
        }
        toggleModal("loding-modal", "", "正在提交...");//打开加载框
        $.ajax({
            url: ctx + "schoolSim/createSchoolSimOrder",
            type: "post",
            data: $("#applyForm").serializeObject(),
            dataType: "json",
            success: function (data) {
                $("#loding-modal").hide();
                if (data.code === "0") {
                    toggleModal("orders-success");//弹出结果框
                    ajaxSuccess();
                } else {
                    if (data == '页面过期，请刷新页面') {
                        toggleModal("sorry-modal", "", data.message, "订单提交失败");
                    } else {
                        toggleModal("sorry-modal", "", data.message, "订单提交失败");
                    }
                    ajaxFail();
                }
            },
            error: function () {
                $("#loding-modal").hide();
                toggleModal("sorry-modal", "", "订单提交失败");
                ajaxFail();
            }
        });
    });

    var templateId = '${templateId}';
    var tken = $.cookie('token');
    var secToken = $.cookie('secToken');
    //跳转到sms-create.html页面后token丢失,故重新设置一下token cookie
    $.cookie('secToken', secToken, {"domain":"wap.hn.10086.cn","path":"/"});
    $.cookie('token', tken, {"domain":"wap.hn.10086.cn","path":"/"});
    $.cookie('userToken', tken, {"domain":"wap.hn.10086.cn","path":"/"});
    var url="http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?confId="+getQueryString("confId")+
        "&recmdCode="+getQueryString("recmdCode")+"&shopId="+getQueryString("shopId");
    function smsDirect() {
        window.location.href = "http://wap.hn.10086.cn/ecsmc-static/static/sp/html/sms-create.html?templateId="+
            templateId+"&linkUrl="+url;
    }

    /**
     * 调用o2o渠道分享组件
     */
    function shareSim() {
        var cardTypeName='移动校园卡';
        var desc='Pick移动校园卡,青春从此不限量';
        var url="http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?confId="+getQueryString("confId")+
            "&recmdCode="+getQueryString("recmdCode")+"&shopId="+getQueryString("shopId");
        o2oApp.openShareView(cardTypeName, desc, url.replace(/(^\s*)|(\s*$)/g, ''),
            ("http://wap.hn.10086.cn" + imgUrl).replace(/(^\s*)|(\s*$)/g, ''));

    }



</script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShare.js?v=201805"></script>
</html>