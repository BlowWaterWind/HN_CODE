<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/8/9
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>订单填写</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <META name="WT.si_n" content="${confId}_${plan.planName}">
    <META name="WT.si_x" content="21">
    <META name="WT.ac" content="${plan.planId}_${plan.planName}">

    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">

    <link href="${ctxStatic}/css/sim/simgroup/o2o.sim.group.css?v=1" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/wap-simcommon.css?v=1" rel="stylesheet"><!--这个页面选号的弹出框-->


    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.jquery.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <!---->
    <script type="text/javascript" src="${ctxStatic}/js/wb/public.js?v=1"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>

    <script type="text/javascript">
        var ctx = "${ctx}";
        var simCardPrefix = "${simCardPrefix}";
    </script>
</head>
<style type="text/css">
    input {
        width: 100%;
        flex: 1;
    }
</style>

<body>
<div class="container">
    <div class="common-navbar-wrap">
        <div class="common-navbar ">
            <div class="navbar-left">
                <a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
            </div>
            <h3 class="navbar-title">填写订单</h3>
        </div>
    </div>

    <div class="box-list-order ">
        <ul>
            <li>
                <span>您已选择：</span>
                <span class="font-rose">${mobile}</span>
            </li>
            <li>
                <span>所属城市：</span>
                <span class="font-rose">${cityName}</span>
            </li>

            <li>
                <span>已选套餐：</span>
                <span class="font-rose">${plan.planName}</span>
            </li>
            <li>
                <span>已选方案：</span>
                <span class="font-rose">${plan.planDesc}</span>
            </li>

        </ul>
    </div>
    <div class="bg-white">
        <div class="index-title">
            <p>用户信息</p>
        </div>
        <form id="applyForm">
            <div class="pl20">
                <c:if test="${transactionId == null}">
                <div class="border-b pt10 pb10 lh50" style="display: flex;position: relative;">
                    <label class="form-label">身份验证： </label>
                    <i class="clear-index" style="display: none"></i>
                    <div id="verityCard"><p class="fs22" style="margin-top: 3px">点击进行实名认证</p>
                    <i class="arrow arrow-right"></i></div>
                </div>
                </c:if>
                <c:if test="${transactionId != null}">
                    <div class="border-b pt10 pb10 lh50" style="display: flex;position: relative;">
                        <label class="form-label">身份验证： </label>
                        <i class="clear-index" style="display: none"></i>
                        <p class="fs22" style="margin-top: 3px">身份验证成功</p>
                        <i class="arrow arrow-right"></i>
                    </div>
                </c:if>
                <div class="border-b pt10 pb10 lh50" style="display: flex">
                    <label class="form-label">联系号码： </label>
                    <i class="clear-index" style="display: none"></i>
                    <input class="fs22" type="tel" placeholder="请填写联系号码" name="orderDetailSim.contactPhone"
                           id="contactPhone" onkeyup="phoneKey(this)" value="${contactPhone}"/>
                </div>
                <div class="border-b pt10 pb10 lh50" style="display: flex">
                    <label class="form-label">sim卡号： </label>
                    <input class="fs22" type="text" value="${simCardPrefix}" readonly style="width:2rem;flex:none;"/>
                    <p class="index-f-w">-</p>
                    <i class="clear-index" style="display: none"></i>
                    <input class="fs22" type="text" placeholder="请输入sim卡号后十位" id="simcardno" onkeyup="onlyNum1(this)" value="${iccid}"/>
                    <input type="hidden" name="orderDetailSim.iccid" id="simCard"/>
                </div>
                <div class="border-b pt10 pb10 lh50" style="display: flex">
                    <label class="form-label">PUK码： </label>
                    <i class="clear-index" style="display: none"></i>
                    <input class="fs22" type="text" name="orderDetailSim.onlineBakSn" placeholder="请输入puk码" id="puk"
                           onkeyup="pukKey(this)" value="${onlineBakSn}"/>
                </div>
            </div>
            <input type="hidden" name="confId" value="${confId}"/>
            <input type="hidden" name="recmdCode" id="recmdCode" value="${recmdCode}"/><%--推荐码--%>
            <input type="hidden" name="CHANID" value="${CHANID}"/>
            <input type="hidden" name="orderDetailSim.cityCode" value="${cityCode}" id="cityCode"/>
            <input type="hidden" name="planId" value="${planId}"/>
            <input type="hidden" value="${mobile}" name="orderDetailSim.phone" id="phone">
            <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
            <input type="hidden" value="5" name="userGoodsCarList[0].shopTypeId">
            <input type="hidden" value="2" name="payMode.payModeId">
            <input type="hidden" value="湖南省" name="memberAddress.memberRecipientProvince"><!--湖南省-->
            <input type="hidden" value="" id="memberRecipientCity" name="memberAddress.memberRecipientCity"><!--长沙市-->
            <input type="hidden" value="" id="memberRecipientCounty" name="memberAddress.memberRecipientCounty">
            <input type="hidden" name="transactionId" value="${transactionId}" id="transactionId">
            <input type="hidden" value="o2oGroup" name="urlPage">
            <!--芙蓉区-->
        </form>

    </div>
    <div class="p20">
        <input type="checkbox" class="regular-radio" id="checkbox1"><label for="checkbox1">
        我已阅读并同意<a class="font-blue btnXieyi" onclick="toggleModal('agree-modal')">《客户入网服务协议》</a>
    </label>

    </div>
    <div class="common-footer-wrap">
        <div class="common-footer-fix">
            <a class="footer-btn-blue " onclick="submitOrder()">订单提交</a>
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

<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<%--<script type="text/javascript" src="${ctxStatic}/js/goods/sim/simgroup/simveritify.js?v=1"></script>--%>
<script type="text/javascript">
    var transactionId = '${transactionId}';
    $("#simCard").val('${simCardPrefix}${iccid}');
    $("#simcard")
    $(function () {
        var clientType = isAppClient();
        if (clientType == "hezhanggui") {
            $(".common-navbar-wrap").show();
        } else if (clientType == "html5" || clientType == "android" || clientType == "ios") {
            //去除头和分享(电脑/安卓/ios浏览器打开不分享但是显示头)
            $(".navbar-right").remove();
        } else {
            //微信打开调用微信的分享,并且不显示头
            $(".common-navbar-wrap").remove();
        }
    });

    $("#verityCard").click(function () {
        if ($("#cityCode").val().length == 0) {
            toggleModal("sorry-modal", "请选择号码归属地");
            return;
        }
        toggleModal("loding-modal", "", "正在跳转...");//打开加载框

        $.ajax({
            url: ctx + "simBuy/payAppointment",
            type: "post",
            data: $("#applyForm").serializeObject(),
            dataType: "json",
            success: function (data) {
                $("#loding-modal").hide();
                if (data.code === "0") {
                    ajaxSuccess();
                    window.location.href = data.data;
                } else {
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

    function pukKey(obj) {
        if ($("#puk").val() != "") {
            $(obj).prev().show();
        } else {
            $(obj).prev().hide();
        }
        $("#puk").val($.trim($("#puk").val()));
    }

    function phoneKey(obj) {
        if ($("#contactPhone").val() != "") {
            $(obj).prev().show();
        } else {
            $(obj).prev().hide();
        }
        $("#contactPhone").val($.trim($("#contactPhone").val()));
    }

    $(".clear-index").click(function () {
        $(this).next().val("");//清空表单内容
        $(this).hide();
    });

    function onlyNum1(a) {
        var num = getSimNum(a.value)
        if (num != "") {
            $(a).prev().show();
        } else {
            $(a).prev().hide();
        }
        if (num.length == 10) {
            $(a).blur();//blur之后执行onlineSellSimConfirm.js里面的方法
        }
        onlyNum2(a);
    }

    function onlyNum2(a) {
        var simCardNo = getSimNum(a.value), len = simCardNo.length;
        var se, vlen;
        if (a.setSelectionRange) {
            se = a.selectionEnd;
            vlen = se === a.value.length;
        }
        if (11 > len) {
            a.value = formatSimCardNo(simCardNo);
            if (10 === len) {
                a.blur();
                $("#simCard").val(simCardPrefix + simCardNo)
                return;
            }
        } else {
            a.value = formatSimCardNo(simCardNo.substr(0, 20));
            a.blur();
        }
        se && setTimeout(function () {
            vlen && (se = a.value.length);
            a.setSelectionRange(se, se)
        }, 0)

    }

    function formatSimCardNo(a) {
        var num = (a + "").split(""), str = "";
        num.forEach(function (a, num) {
            (5 === num || 10 === num || 15 === num) && (str += " ");
            str += a;
        });
        return str;
    }

    function getSimNum(a) {
        var simCardNo = $.trim(a);
        simCardNo = simCardNo.replace(/[\s]/g, "");
        return simCardNo;
    }

    function checkSimCardNo(simCardNo) {
        var simReg = /^[0-9A-Za-z]{20}$/;
        if (simReg.test(simCardNo)) {
            return true;
        }
        return false;
    }

    function submitOrder() {
        if (transactionId == '') {
            toggleModal("sorry-modal", "请先进行实名认证!");
            return;
        }

        if (!$("input[type='checkbox']").is(':checked')) {
            toggleModal("sorry-modal", "请仔细阅读客户入网服务协议!");
            return;
        }
        //== 联系电话校验
        var $phone = $("#contactPhone").val();
        var regPhone = /^1[34578]\d{9}$/;
        if ($phone.length == 0) {
            toggleModal("sorry-modal", "请输入电话号码");
            return;
        }
        if (!regPhone.test($phone)) {
            toggleModal("sorry-modal", "联系电话不正确");
            return;
        }
        if (!checkSimCardNo($("#simCard").val())) {
            toggleModal("sorry-modal", "请输入正确的20位的sim卡号");
            return;
        }
        toggleModal("loding-modal", "", "正在提交...");//打开加载框
        $.ajax({
            url: "submitOrder",
            type: "post",
            data: $("#applyForm").serializeObject(),
            dataType: "json",
            success: function (data) {
                var planName = '${confId}_${planId}_${planName}';
                $("#loding-modal").hide();
                if (data.code === "0") {
                    toggleModal("orders-success");//弹出结果框
                    ajaxSuccess();
                } else {
                    if (data == '页面过期，请刷新页面') {
                        toggleModal("sorry-modal", "", data, "订单提交失败");
                    } else {
                        toggleModal("sorry-modal", "", data.message, "订单提交失败");
                    }
                    ajaxFail();
                }
            },
            error:function(){
                toggleModal("sorry-modal", "", data, "提交异常");
                ajaxFail();
            }
        });
    }
</script>

</body>


</html>
