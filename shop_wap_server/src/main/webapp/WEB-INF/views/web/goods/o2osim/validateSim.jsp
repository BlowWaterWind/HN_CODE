<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/1/31
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="WT.si_n" content="">
    <meta name="WT.si_x" content="23">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <META name="WT.ac" content="">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
    <title>下单</title>
    <link rel="stylesheet" href="${ctxStatic}/css/wap.o2o.sim.css"/>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <%--&lt;%&ndash;图片识别&ndash;%&gt;--%>
    <%--<link rel="stylesheet" href="${ctxStatic}/ocr/mui/mui.css" />--%>
    <%--<link rel="stylesheet" href="${ctxStatic}/ocr/common/common.css" />--%>
    <%--<link rel="stylesheet" href="${ctxStatic}/ocr/image-clip.min.css" />--%>
    <%--<link rel="stylesheet" href="${ctxStatic}/ocr/common/clip.css" />--%>
</head>
<body>
<div id="clip"></div>
<input type="hidden" id="orderPreId" value="${o2oPreOrder.orderPreId}">
<div class="container">
    <div class="return-msg">
        <div class="return-msg-icon Grid -center"><img src="${ctxStatic}/images/o2osim/icon-success.png"/></div>
        <h3>验证成功 请勿关闭</h3>
        <p class="txt-center">请将收到的SIM卡片与订单进行绑定</p>
    </div>
    <form id="continue" method="post">
        <input type="hidden" name="">
        <div class="base-cells bor-cells">
            <div class="base-cell Grid -middle">
                <label class="Cell -3of12">SIM卡号:</label>
                <i class="clear-index" style="display: none"></i>
                <input id="simCardNo" type="text" value="" maxlength="24" onkeyup="onlyNum1(this)"
                       class="base-cell-input Cell -fill"/>
            </div>
        </div>
    </form>
    <div class="find-number Grid -right"><span id="findCardNmber">找不到在哪?</span></div>
    <div class="return-btn Grid -center">
        <%--<button id="scan"class="btn-default btn-blue">扫码识别</button>--%>
        <button id="continueBtn" onclick="" class="btn-default btn-blue">继续选号</button>
        <%--<button id="closeBtn" class="btn-default btn-blue">绑定后关闭</button>--%>
    </div>
    <div class="layer-modal layer-modal-1">
        <button onclick="layer.closeAll();" class="icon-close"></button>
        <div class="sim-img"><img src="${ctxStatic}/images/o2osim/card-info.png"/></div>
    </div>
    <form method="post" id="validateSim">
        <input type="hidden" name="confId" value="${confId}"/>
        <input type="hidden" name="reCmdCode" value="${recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId" value="${planId}"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="simCardNo" id="simCardValue">
        <input type="hidden" name="chanId" value="${CHANID}"/>
        <input type="hidden" name="shopId" value="${shopId}"/>
    </form>
</div>
<script src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
<script src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script src="${ctxStatic}/ap/lib/layer.js"></script>
<script src="${ctxStatic}/js/o2o/simOnline/public.js"></script>
<%--<script type="text/javascript" src="${ctxStatic}/ocr/common/fileinput.js"></script>--%>
<%--<script type="text/javascript" src="${ctxStatic}/ocr/common/exif.js"></script>--%>
<%--<script type="text/javascript" src="${ctxStatic}/ocr/image-clip.min.js"></script>--%>
<%--<script type="text/javascript" src="${ctxStatic}/ocr/ocr.js"></script>--%>
</body>
<script type="text/javascript">

    try {
        /*给页面WT.si_n 赋值*/
        getConfId3("${o2oPreOrder.confId}");
        var wtAc = $.cookie("WT.ac");
        if ($.cookie("WT.ac") != undefined) {
            document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
        }
    } catch (e) {
        console.log(e);
    }
    var ctx = ${ctx};

    function onlyNum1(a) {
        onlyNum2(a);
    }

    function onlyNum2(a) {
        var simCardNo = getSimNum(a.value), len = simCardNo.length;
        $("#simCardValue").val(simCardNo);
        if (simCardNo != "") {
            $(a).prev().show();
        } else {
            $(a).prev().hide();
        }

        var se, vlen;
        if (a.setSelectionRange) {
            se = a.selectionEnd;
            vlen = se === a.value.length;
        }
        if (21 > len) {
            a.value = formatSimCardNo(simCardNo);
            if (20 === len) {
                a.blur();
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

    function checkSimCardNo(simCardNo) {
        var simReg = /^[0-9A-Za-z]{20}$/;
        if (simReg.test(simCardNo)) {
            return true;
        }
        return false;
    }

    //发送ajax请求验证sim卡号是否合法
    function getSimCardNo(simCardNo, type) {
        layer.open({
            type: 2,
            shadeClose: false
        });
        var params = $("#validateSim").serializeObject();
        $.ajax({
            url: ctx + 'o2oSimPreBuy/validateSim',
            data:params,
            type: "post",
            dataType: "json",
            sync: true,
            success: function (data) {
                if (data.code == '0' && type == 1) {
                    window.location.href = "${ctx}simConfirm/toSelectPage?orderPreId=" + data.data.orderPreId;
                    // layer.closeAll();不关闭直接跳转
                } else if (data.code == '0' && type == 2) {
                    layer.closeAll();
                    var confId = $("#confId").val();
                    var recmdCode = $("#recmdCode").val();
                    var CHANID = $("#CHANID").val();
                    var shopId = $("#shopId").val();
                    layer.open({
                        content: '绑定成功,请在号卡订单中心继续选号操作!',
                        btn: '确定',
                        yes: function () {
                            window.location.href = "${ctx}simBuy/simH5OnlineToApply?confId=" + confId + "&recmdCode=" + recmdCode + "&CHANID=" + CHANID + "&shopId=" + shopId;
                        }
                    });
                    try {
                        var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                        var wtx = document.getElementsByTagName('meta')['WT.si_x'].content;
                        if (window.Webtrends) {
                            Webtrends.multiTrack({
                                argsa: ["WT.si_n", wtc,
                                    wtx, "99"],
                                delayTime: 100
                            })
                        } else {
                            if (typeof(dcsPageTrack) == "function") {
                                dcsPageTrack("WT.si_n", wtc, true,
                                    wtx, "99", true);
                            }
                        }

                        // dcsPageTrack("WT.si_n", "实名制验证_" + data.data.cardId + "输入", 0, "WT.si_x", "预订单下单成功", 0);//插码使用
                    } catch (e) {
                        console.log(e);
                    }
                } else {
                    layer.closeAll();
                    message = data.message;
                    layer.open({
                        content: message,
                        btn: '我知道了'
                    });
                    try {
                        var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                        var wtx = document.getElementsByTagName('meta')['WT.si_x'].content;
                        if (window.Webtrends) {
                            Webtrends.multiTrack({
                                argsa: ["WT.si_n", wtc,
                                    wtx, "-99"],
                                delayTime: 100
                            })
                        } else {
                            if (typeof(dcsPageTrack) == "function") {
                                dcsPageTrack("WT.si_n", wtc, true,
                                    wtx, "-99", true);
                            }
                        }

                        // dcsPageTrack("WT.si_n", "实名制验证_" + data.code + "输入", 0, "WT.si_x", "预订单下单失败", 0);//插码使用
                    } catch (e) {
                        console.log(e);
                    }
                }
            },
            error: function () {
                layer.open({
                    content: '网络错误',
                    btn: '我知道了'
                });
                return;
            }
        });

    }
    $('#scan').click(function(){
        $('#targetImgCamera').click();
    });
    $("#continueBtn").click(function () {
        var simCardNo = getSimNum($("#simCardNo").val());
        if (!checkSimCardNo(simCardNo)) {
            layer.open({
                content: '请先输入正确的sim卡号',
                btn: '我知道了'
            });
            return;
        }
        $("#simCardValue").val(simCardNo);
        getSimCardNo(simCardNo, 1);
    });

    //点击后关闭 todo 要有confId和planId
    $("#closeBtn").click(function () {
        var simCardNo = getSimNum($("#simCardNo").val());
        if (!checkSimCardNo(simCardNo)) {
            layer.open({
                shadeClose: false,
                content: '请先输入正确的sim卡号',
                btn: '我知道了'
            });
            return;
        }
        //询问框
        layer.open({
            content: '绑定关闭后,您可以在一天内前往湖南移动微厅->业务办理->办卡和实名认证->订单查询中查询选号!'
            , btn: ['我要关闭', '不我要现在选号']
            , yes: function (index) {
                getSimCardNo(simCardNo, 2);
                layer.close(index);
            }
        });
    });

    function getSimNum(a) {
        var simCardNo = $.trim(a);
        simCardNo = simCardNo.replace(/[\s]/g, "");
        return simCardNo;
    }

    $(".clear-index").click(function () {
        $(this).next().val("");//清空表单内容
        $(this).hide();
    });

</script>
</html>
