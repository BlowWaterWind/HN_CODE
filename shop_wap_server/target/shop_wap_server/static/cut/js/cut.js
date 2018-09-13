$(function(){
    var html = "<div class='bargin-mask'></div>";
    html += "<div class='bargin-popup popup-loading'>";
    html += "<img class='loading-img' src='" + ctxStatic + "/cut/images/bargain/loading4.gif'/></div>";
    $("body").append(html);


});

function showMask() {
    $(".bargin-mask").show();
    $(".popup-loading").show();
}

function hideMask() {
    $(".bargin-mask").hide();
    $(".popup-loading").hide();
}

function funJoinCut() {
    var $oThis = $(this);
    if ("true" != isBindCard) {
        showErrorDialog("2");
        return;
    }
    
    $oThis.unbind("click");
    showMask();
    $.ajax({
        url : ctx + "bargainInHeBao/joinCut?datetime=" + new Date().getTime(),
        dataType : "json",
        data : {bargainCode : $oThis.attr("bargainCode")},
        type : "post",
        async : true,
        success : function(data) {
            hideMask();
            $oThis.bind("click", funJoinCut);
            if (data.success) {
                if(typeof(dcsPageTrack)=="function"){
                    dcsPageTrack("WT.si_n","生成砍价",true, "WT.si_x","生成砍价成功",true);
                }
                window.location.href = ctx + "/bargainInHeBao/initMyCut?bargainCode=" + $oThis.attr("bargainCode");
            } else {
                if(typeof(dcsPageTrack)=="function") {
                    dcsPageTrack ("WT.si_n","发起砍价",false ,"WT.si_x","发起砍价失败",false,"WT.err_code","错误码（"+ data.errorCode +"）",false);
                }
                showErrorDialog(data.errorCode);
            }
        },
        error : function() {
            hideMask();
            $oThis.bind("click", funJoinCut);
        }
    });
}

function fnHelpCutCtrl() {
    showTipDialog("2");
}

function fnMyJoinCutCtrl() {
    window.location.href = ctx + "bargainInHeBao/init";
}

function fnSendRandomCode() {
    var $phoneNum = $("#phoneNum");
    if (validationPhone($phoneNum)) {
        $("#errorMsgContainer").html("");

        ajaxSendRandomCode($phoneNum.val());
    } else {
        $("#errorMsgContainer").html("请输入正确湖南移动号码!!");
    }
}

function ajaxSendRandomCode(mobile) {
    hideTipDialog("2");
    showMask();
    $.ajax({
        url : ctx + "bargainInHeBao/sendRandomCode?datetime=" + new Date().getTime(),
        dataType : "json",
        data : {mobile : mobile},
        type : "post",
        async : true,
        success : function(data) {
            hideMask();
            showTipDialog("2");  // 弹出短息验证码层
            if (data.X_RESULTCODE == "0") {
                $("#errorMsgContainer").html("短信已成功发送！");
                $("#getSmsCtrl").unbind("click");
                nextSendMsg(data.TIME);  // 构建倒计时， 重发短息验证提示
            } else {  // 发送验证码时， 报错时前台提示信息
                $("#errorMsgContainer").html(data.X_RESULTINFO);
            }
        },
        error : function() {
            hideMask();
            showTipDialog("2");
            $("#errorMsgContainer").html("服务异常， 请稍后再试。");
        }
    });
}

function fnConfirmHelpCutCtrl() {
    var mobile = $("#phoneNum").val();
    var smsCode = $("#smsCode").val();
    var cutPriceId = $("#cutPriceId").val();
    if (!validationPhone($("#phoneNum"))) {
        $("#errorMsgContainer").html("请输入正确湖南移动号码!!");
        return;
    }

    if (smsCode.length != 6) {
        $("#errorMsgContainer").html("请输入正确验证码!!");
        return;
    }

    hideTipDialog("2");
    showMask();
    $.ajax({
        url : ctx + "bargainInHeBao/helpCut?datetime=" + new Date().getTime(),
        dataType : "json",
        data : {mobile : mobile, randomCode : smsCode, cutPriceId : cutPriceId},
        type : "post",
        async : true,
        success : function(data) {
            hideMask();
            if (data.X_RESULTCODE == "0") {  // 砍价成功， 弹出砍价结果层, 设置砍价结果
                $("#helpCutAmount").html(data.cutAmount);
                showTipDialog("3");
            } else if (data.X_RESULTCODE == "-1") {  // 验证码错误
                $("#errorMsgContainer").html(data.X_RESULTINFO);
                showTipDialog("2");  // 弹出验证码层， 并设置错误信息
            } else {
                showErrorDialog(data.X_RESULTCODE); // 弹出业务报错层
            }
        },
        error : function() {
            hideMask();
            showTipDialog("2");
            $("#errorMsgContainer").html("服务异常， 请稍后再试。");
        }
    });
}

function nextSendMsg(time) {
    if (time <= 0) {
        $("#getSmsCtrl").html("点击获取");
        $("#getSmsCtrl").bind("click", fnSendRandomCode);
    } else {
        $("#getSmsCtrl").html("重试（" + time +"）");
        setTimeout(function () {
            nextSendMsg(time - 1);
        }, 1000);
    }
}

function validationPhone($input) {
    var sRechargePhone = $input.val().replace(/\s+/g,"");
    $input.val(sRechargePhone);
    if (sRechargePhone == '') {
        return false;
    }

    var regPhone  = /^1[34578]\d{9}$/;
    if (!regPhone.test(sRechargePhone)) {
        return false;
    } else {
        return true;
    }
}


function fnShareConfirm() {
    var title = "和包客户端砍价";
    var shareContext = "我正在和包客户端参与呼朋唤友来砍价，求砍，求狠狠砍！";
    var url = "http://wap.hn.10086.cn/shop/bargainInHeBao/init";
    addToShare(actCode);
    share(title, url, shareContext);
    hideErrorDialog("11");
    if ("true" != isBindCard) {
        showErrorDialog("2");
    }
}


function showErrorDialog(errorCode) {
    if (errorCode && errorCode != "") {
        var $errorContainer = $("#error_" + errorCode);
        if ($errorContainer.length != 0) {
            $(".bargin-mask").show();
            $errorContainer.show();
        }
    }
}


function hideErrorDialog(errorCode) {
    if (errorCode && errorCode != "") {
        var $errorContainer = $("#error_" + errorCode);
        if ($errorContainer.length != 0) {
            $(".bargin-mask").hide();
            $errorContainer.hide();
        }
    }
}

function showTipDialog(tipCode) {
    if (tipCode && tipCode != "") {
        var $tipContainer = $("#tip_" + tipCode);
        if ($tipContainer.length != 0) {
            $(".bargin-mask").show();
            $tipContainer.show();
        }
    }
}

function hideTipDialog(tipCode) {
    if (tipCode && tipCode != "") {
        var $tipContainer = $("#tip_" + tipCode);
        if ($tipContainer.length != 0) {
            $(".bargin-mask").hide();
            $tipContainer.hide();
        }
    }
}
