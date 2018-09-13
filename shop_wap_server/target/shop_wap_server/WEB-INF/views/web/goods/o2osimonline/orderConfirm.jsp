<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/6/7
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>资料填写</title>
    <%--插码相关--%>
    <%--插码相关：test--%>
    <meta name="WT.si_n" content="">
    <meta name="WT.si_x" content="">
    <meta name="WT.si_s" content="">

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css"/>
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet"><!--这个页面选号的弹出框-->

    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/vue.min.js"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
<%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>



</head>
<script type="text/javascript">
    var ctx = ${ctx};
    var cmConfId = "${conf.confId}";
    var deliveryType = ${deliveryType};
    var shopAddress = "${shop.shopPhysicallProvince}${shop.shopPhysicallCity}${shop.shopPhysicallCounty}${shop.shopPhysicallAddress}";
    var slctCity = '${shop.shopPhysicallCity}';//市
    var slctCnty = '${shop.shopPhysicallCounty}';//区
    var typeFlag = '${resultPlan.chnlCodeOut}';
    var cardType = '${resultPlan.cardType}';
</script>
<body>
<div class="container">
    <div class="topbar">
        <a href="javascript:history.back(-1);" class="icon icon-return">返回</a>
        <%--<a href="javascript:;" class="icon icon-close">关闭</a>--%>
        <h3>资料填写</h3>
        <%--<a href="javascript:;" class="icon icon-more">更多</a>--%>
    </div>
    <div class="selected">已选择<span>“${plan.planName}
    <c:if test="${preFee != 0}">
        预存${preFee/100}
    </c:if>
    <c:if test="${cardFee != 0}">
        卡费${cardFee/100}
    </c:if>
        套餐”
    </span></div>

    <div class=" clearfix ">
        <form id="confirmOrderFormSim" action="${ctx}goodsBuy/simH5OnlineSubmitOrder" method="post">
            <input type="hidden" name="planName" value="${plan.planName}"/>
            <input type="hidden" id="confId" name="confId" value="${confId}"/>
            <input type="hidden" id="cardType" name="cardType" value="${resultPlan.cardType}"/>
            <input type="hidden" id="planId" name="planId" value="${plan.planId}"/>
            <input type="hidden" name="recmdCode" value="${recmdCode}"/><%--推荐码--%>
            <input type="hidden" id="preFee" name="preFee" value="${preFee/100}"/>
            <input type="hidden" id="cardFee" name="cardFee" value="${cardFee/100}"/>
            <input type="hidden" id="feature_str" name="feature_str" value="${resultPlan.feature_str}"/>
            <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
            <input type='hidden' value='${deliveryType}' name="deliveryMode.deliveryModeId">
            <input type="hidden" value="${shop.shopId}" name="userGoodsCarList[0].shopId">
            <input type="hidden" value="${shop.shopName}" name="userGoodsCarList[0].shopName">
            <input type="hidden" value="${shop.shopTypeId}" name="userGoodsCarList[0].shopTypeId">
            <input type="hidden" value="2" name="payMode.payModeId">
            <input type="hidden" value="湖南省" name="memberAddress.memberRecipientProvince"><!--湖南省-->

            <!--下面的表单信息如果回调回来有就填充,没有就继续填写-->
            <input  type="hidden" value="${memberRecipientCity}" id="memberRecipientCity" name="memberAddress.memberRecipientCity"><!--长沙市-->
            <input  type="hidden" value="${memberRecipientCounty}" id="memberRecipientCounty" name="memberAddress.memberRecipientCounty"><!--芙蓉区-->
            <dl class="common-list-info border-b">
                <dt><span class="font-red">*</span>收货人姓名</dt>
                <dd><i class="clear-index" style="display: none"></i><input id="regName" type="text" name="orderDetailSim.custName" placeholder="请填写收货人姓名" value="${custName}" onkeyup="regNameKey(this)"></dd>
            </dl>
            <dl class="common-list-info border-b">
                <dt><span class="font-red">*</span>联系电话</dt>
                <dd><i class="clear-index" style="display: none"></i><input type="text" id="contactPhone" onkeyup="phoneKey(this)" name="orderDetailSim.contactPhone" maxlength="11"
                           placeholder="请输入电话号码" value="${contactPhone}"></dd>
            </dl>
            <div class="plb10">
                <div class="bg-gray fs28 p10"><b>请选择号码</b></div>
            </div>
            <dl class="common-list-info border-b-big">
                <dt><span class="font-red">*</span>号码归属</dt>
                <dd><p id="selectCity">${shop.shopPhysicallCity}</p>
                    <input type="hidden" id="cityCode" name="orderDetailSim.cityCode" value="${shop.tDistributionChnl.eparchyCode}"/><!--0731-->
                    <input type="hidden" id="cityCodeNum" name="orderDetailSim.chnlCodeOut" value="${shop.tDistributionChnl.cityCode}"/><!--区县编码A31K-->
                </dd>
            </dl>
            <c:if test="${transactionId == null}">
            <dl class="common-list-info border-b-big">
                <dt><span class="font-red">*</span>选择号码</dt>
                <dd><p id="searchNumberO2O" class="item-select">请选择号码</p>
                    <input type="hidden" id="phone" name="orderDetailSim.phone" value="${phone}">
                </dd>
            </dl>
            </c:if>
            <c:if test="${transactionId != null}">
                <dl class="common-list-info border-b-big">
                    <dt><span class="font-red">*</span>已选号码</dt>
                    <dd><p>${phone}</p>
                        <input type="hidden" name="orderDetailSim.phone" value="${phone}">
                    </dd>
                </dl>
            </c:if>
            <c:if test="${transactionId == null}">
            <dl class="common-list-info border-b-big">
                <dt><span class="font-red">*</span>实名认证</dt>
                <dd><p class="item-select" id="appointmentSubmit">点击进行实名认证</p>
                </dd>
            </dl>
            </c:if>
            <c:if test="${transactionId != null}">
                <dl class="common-list-info border-b-big">
                    <dt><span class="font-red">*</span>实名认证</dt>
                    <dd><p>实名认证通过</p>
                    </dd>
                </dl>
            </c:if>
            <%--物流配送方式--%>
            <div id="needLogis" style="display: none;">
                <div class="">
                    <div class="bg-gray fs28 p10"><b>请填写配送地址</b>
                        <small class="font-red fs18">（仅支持湖南省内配送）</small>
                    </div>
                </div>
                <dl class="common-list-info border-b-big">
                    <dt><span class="font-red">*</span>所在地区</dt>
                    <c:if test="${transactionId == null}">
                    <dd><p class="item-select" id="picker2">请选择区/县</p></dd>
                    </c:if>
                    <c:if test="${transactionId != null}">
                        <dd><p class="item-select" id="picker2">${memberRecipientCity} ${memberRecipientCounty} </p></dd>
                    </c:if>
                    <input type="hidden" id="linkAddress" name="orderDetailSim.linkAddress" value="${linkAddress}"/>
                </dl>
                <dl class="common-list-info border-b-big">
                    <dt><span class="font-red">*</span>详细地址</dt>
                    <input type="text" id="memberRecipientAddress" name="memberAddress.memberRecipientAddress"
                           placeholder="街道/镇+村/小区/写字楼+门牌号" value="${memberRecipientAddress}">
                </dl>
            </div>
            <input type="hidden" name="transactionId" value="${transactionId}" id="transactionId">
            <input type="hidden" name="urlPage" value="o2oOnline">
        </form>

    </div>
    <div class="agree">
        <p>
            <input type="checkbox" id="protocol"/>
            <label for="protocol">我已阅读并同意<a style="font-size: .2rem;" href="javascript:void(0);"
                                            onclick="toggleModal('agree-modal')">《客户入网服务协议》</a></label>
        </p>
    </div>


    <div class="kd-navbar-wrap navbar-wrap-footer ">
        <div class="common-navbar ">
            <div class="navbar-left navbar-left-w">
                <a href="javascript:history.back(-1);" class="btn-footer">上一步</a>
                <a id="verifySub" class="btn-footer btn-footer-blue btnNext">下一步</a>
            </div>

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
            <a href="javascript:void(0)" id="infoConfirmO2O" class="confirm-btn"
               otype="button" otitle="H5号卡销售_${conf.confId}-确定">确定</a>
        </div>
    </div>
</div>

<%--选择号码--%>
<div id="select-number" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('select-number')"></a>
        <div class="modal-content">
            <div class="modal-con">
                <div class="search-con">
                    <div class="search-bar">
                        <input type="text" id="number" name="number" placeholder="生日、幸运数字等" class="search-input">
                        <input type="button" id="numberBtnO2O" name="search-bar-btn" class="search-bar-btn">
                    </div>
                    <ul id="select-number-ul" class="search-list">
                    </ul>
                </div>
            </div>
        </div>
        <a href="javascript:void(0)" id="cagBatchO2O" class="full-btn">换一批</a>
    </div>
</div>


<%-- 用户协议弹窗 --%>
<div id="agree-modal" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('agree-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <h4 class="pt-30">客户入网服务协议</h4>
            <div class="agree-wrap">${serviceContract}</div>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>


</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/onlineSellSimConfirm.js?v=133"></script>
<script type="text/javascript">
    var transactionId = '${transactionId}';

    $(function(){
        if(deliveryType === 2){
            $("#needLogis").show();
        }else{
            $("#picker2").text("${shop.shopName}");
            $("#memberRecipientAddress").val("${shop.shopName}")
        }
        var clientType = isAppClient();
        if(clientType == 'weixin') {
            //微信打开调用微信的分享,并且不显示头
            $(".topbar").remove();
        }
    });

    $("#cagBatchO2O,#numberBtnO2O").on("click", function () {
        searchNumber();
    });

    $("#searchNumberO2O").click(function(){
        toggleModal('select-number');
        searchNumber();
    });

    function searchNumber(){
        $("#select-number ul").empty();
        var loading = '<div class="m-load2" ><div class="line">' +
            '<div></div> <div></div><div></div><div></div><div></div><div></div>' +
            '</div><div class="circlebg"></div></div>';
        var number = $("#number").val();
        $("#select-number ul").append(loading);
        $.ajax({
            type: "POST",
            url: ctx + "o2oSimOnline/o2ONetNumQuery",
            dataType: "json",
            data: {
                xTag : deliveryType,
                eparchyCode : $("#cityCode").val(),
                cityCode : $("#cityCodeNum").val(),
                number: number
            },
            success: function (data1) {
                $("#select-number ul").empty();
                if (data1.code != 0) {
                    $("#select-number-ul").append("<p class='text-center'>"+ data1.message+"</p>");
                    return;
                }else if(data1.code === '0' && data1.data == null){
                    $("#select-number-ul").append("<p class='text-center'>很抱歉，暂无符合要求的号码！</p>");
                }
                var html = "";
                var j = data1.data.length > 10 ? 10 : data1.data.length;
                for (var i = 0; i < j; i++) {
                    html += "<li>" + data1.data[i].SERIAL_NUMBER + "</li>"
                }
                $("#select-number ul").html(html);
            },
            error: function () {
                $("#select-number ul").empty();
                $("#select-number-ul").append("<p class='text-center'>很抱歉，暂无符合要求的号码！</p>");
            }
        });
    }

    //确定信息 提交订单
    $("#infoConfirmO2O").on("click", function () {
        $("#info-confirm").hide();//关闭信息框
        toggleModal("loding-modal", "", "正在提交...");//打开加载框
        $.ajax({
            url: ctx + "o2oSimOnline/createO2oOnlineOrder",
            type: "post",
            data: $("#confirmOrderFormSim").serializeObject(),
            dataType: "json",
            success: function (data) {
                $("#loding-modal").hide();
                if (data.code === "0") {
                    if (data.data.type == 'toPay') {
                        window.location.href = ctx + "simpay/toPay?orderNo=" + data.data.orderSubNo + "&confId=" + data.data.confId + "&planId=" + data.data.planId + "&selectPhone=" + data.data.selectPhone;
                    }
                    //跳转到成功页面
                    else {
                        window.location.href = ctx + "o2oSimOnline/orderSuccess?orderSubNo=" + data.data.orderSubNo;
                    }
                   ajaxSuccess();
                } else {
                    if (data == '页面过期，请刷新页面') {
                        toggleModal("sorry-modal", "页面过期，请刷新页面", data, "订单提交失败");
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


    //提交校验 显示信息
    $("#verifySub").on("click", function () {
        //== 联系电话校验
        var $phone = $("#contactPhone").val();
        var regPhone = /^1[34578]\d{9}$/;

        if (!regPhone.test($phone)) {
            toggleModal("sorry-modal", "联系电话不正确");
            return;
        }

        if(deliveryType == 2){
            //邮寄需要校验地址
            if ($("#picker2").text() == "请选择区/县") {
                toggleModal("sorry-modal", "请选择区/县");
                return;
            }

            //== 地址校验
            var cicns = $("#picker2").text().split(" ");
            $("#memberRecipientCity").val(cicns[0]);
            $("#memberRecipientCounty").val(cicns[1]);
            $("#linkAddress").val("湖南省" + $("#picker2").text());
            if ($("#memberRecipientAddress").val().length == 0) {
                toggleModal("sorry-modal", "请输入详细地址");
                return;
            }
            var regDetailAddress = /^[0-9A-Za-z\u4e00-\u9fa5]*$/;
            if (!regDetailAddress.test($("#memberRecipientAddress").val())) {
                toggleModal("sorry-modal", "请输入合法的地址，收货地址只能含有中文/字母/数字/非全角字符");
                return;
            }
            if (/^[0-9]+$/.test($("#memberRecipientAddress").val())) {
                toggleModal("sorry-modal", "请输入合法的地址，收货地址不能是纯数字");
                return;
            }
            if ($("#memberRecipientAddress").val().length <= 5) {
                toggleModal("sorry-modal", "请输入合法的地址，收货地址必须大于5个字符");
                return;
            }
        }else{
            //到厅自取
            $("#memberRecipientCity").val("${shop.shopPhysicallCity}");
            $("#memberRecipientCounty").val("${shop.shopPhysicallCounty}");
        }
        if (!$("#protocol").is(":checked")) {
            toggleModal("sorry-modal", "请认真阅读服务协议！");
            return;
        }
        if (transactionId == '') {
            toggleModal("sorry-modal", "请先进行实名制认证");
            return;
        }
        $(".confirm-list").empty();
        var phone = $('input[name="orderDetailSim.phone"]').val();
        if(deliveryType == 2) {
            var confirmInfo = "<li>尊敬的" + $("#regName").val() + "(先生/女士)：</li>" +
                // "<li>身份证号码：" + $("#psptId").val() + "</li>" +
                "<li>联系方式：" + $("#contactPhone").val() + "</li>" +
                "<li>将办理湖南省" + $("#selectCity").html() +phone + "(号卡)</li>" +
                "<li>配送至：" + $("#linkAddress").val() + $("#memberRecipientAddress").val() + "</li>";
        }else{
            //到店取地址
            var confirmInfo = "<li>尊敬的" + $("#regName").val() + "(先生/女士)：</li>" +
                // "<li>身份证号码：" + $("#psptId").val() + "</li>" +
                "<li>联系方式：" + $("#contactPhone").val() + "</li>" +
                "<li>将办理湖南省" + $("#selectCity").html() + phone + "(号卡)</li>" +
                "<li>到店取地址：" + shopAddress + "</li>";
        }
        var sum = Number($("#preFee").val()) + Number($("#cardFee").val());
        if (sum > 0) {
            confirmInfo += "<li>支付金额:" + sum + "元</li>";
        }
        $(".confirm-list").append(confirmInfo);
        toggleModal("info-confirm");
    });

    function regNameKey(obj) {
        if($("#regName").val() != ""){
            $(obj).prev().show();
        }else{
            $(obj).prev().hide();
        }
        $("#regName").val($.trim($("#regName").val()));
    }

    function phoneKey(obj){
        if($("#contactPhone").val() != ""){
            $(obj).prev().show();
        }else{
            $(obj).prev().hide();
        }
        $("#contactPhone").val($.trim($("#contactPhone").val()));
    }

   $(".clear-index").click(function(){
      $(this).next().val("");//清空表单内容
      $(this).hide();
   });


    function copyUrl2() {
         var Url2=document.getElementById("regName");
         Url2.select(); // 选择对象
         document.execCommand("Copy"); // 执行浏览器复制命令
         alert("已复制好，可贴粘。");
        }

    $("#appointmentSubmit").on("click", function () {
        if ($("#cityCode").val().length == 0) {
            toggleModal("sorry-modal", "请选择号码归属地");
            return;
        }
        if ($("#phone").val().length == 0) {
            toggleModal("sorry-modal", "请选择号码");
            return;
        }
        if(deliveryType == 2){
            var cicns = $("#picker2").text().split(" ");
            $("#memberRecipientCity").val(cicns[0]);
            $("#memberRecipientCounty").val(cicns[1]);
            $("#linkAddress").val("湖南省" + $("#picker2").text());
        }else{
            //到厅自取
            $("#memberRecipientCity").val("${shop.shopPhysicallCity}");
            $("#memberRecipientCounty").val("${shop.shopPhysicallCounty}");
        }
        toggleModal("loding-modal", "", "正在提交...");//打开加载框
        $.ajax({
            url: ctx + "simBuy/payAppointment",
            type: "post",
            data: $("#confirmOrderFormSim").serializeObject(),
            dataType: "json",
            success: function (data) {
                $("#loding-modal").hide();
                if (data.code === "0") {
                    ajaxSuccess();
                    window.location.href = data.data ;
                }else {
                    if (data == '页面过期，请刷新页面') {
                        toggleModal("sorry-modal", "", data, "身份认证提交失败");
                    } else {
                        toggleModal("sorry-modal", "", data.message, "身份认证提交失败");
                    }
                    ajaxFail();
                }
            },
            error: function () {
                $("#loding-modal").hide();
                toggleModal("sorry-modal", "", "身份认证提交失败");
                ajaxFail();
            }
        });
    });

</script>
</html>
