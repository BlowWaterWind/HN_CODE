$(function(){
    if ("true" != isShare) {
        toggleModal("tk_modal7");
    } else if("true" != isBindCard) {
        toggleModal("tk_modal8");
    }

    $("#shareCancelCtrl").bind("click", fnShareCancel);
    $("#shareConfirmCtrl").bind("click", fnShareConfirm);
    $("#killShareCtrl").bind("click", function() {
        var title = "一元秒杀";
        var shareContext = "我正在和包客户端参与1元秒杀爆款机型，比比谁手快！";
        var url = "http://wap.hn.10086.cn/shop/kill/init";
        share(title, url, shareContext);
        addToShare(activityId);
    });
});

function startKillTime(systemTime, startTime, endTime) {
    var startDiff = 0,  endDiff = 0;
    if (systemTime < startTime) {
        startDiff = Math.floor((startTime - systemTime) / 1000);
        endDiff = Math.floor((endTime - startTime) / 1000);
    } else if (systemTime < endTime) {
        endDiff = Math.floor((endTime - systemTime) / 1000);
    }
    setKillTime(startDiff, endDiff, $("#timerContainer"), $("#killStartCtrl"));
}


function setKillTime(startDiff, endDiff, $Timer, $killStartCtrl) {

    if (startDiff > 0) {
        $Timer.html("即将开始：");
        if(!$killStartCtrl.hasClass("btn-gray")) {
            $killStartCtrl.removeClass("btn-red").addClass("btn-gray").html("即将开始");
            $killStartCtrl.unbind("click");
        }
        setTime(startDiff, $Timer);
        setTimeout(function(){
            setKillTime(startDiff - 1, endDiff, $Timer, $killStartCtrl);
        }, 1000);
    } else if (endDiff > 0) {
        $Timer.html("即将结束：");
        setTime(endDiff, $Timer);
        if(!$killStartCtrl.hasClass("btn-red")) {
            $killStartCtrl.removeClass("btn-gray").addClass("btn-red").html("秒杀");
            $killStartCtrl.bind("click", fnKillStart);
        }
        setTimeout(function(){
            setKillTime(startDiff, endDiff - 1, $Timer, $killStartCtrl);
        }, 1000);
    } else {
        $Timer.html("活动已结束");
        $killStartCtrl.removeClass("btn-red").addClass("btn-gray").html("已结束");
        $killStartCtrl.unbind("click");
    }
}

function fnKillStart() {
    var oThis = $(this);
    var activityId = oThis.attr("activityId");
    oThis.unbind("click");
    if (activityId != '') {
        oThis.html("秒杀中...");
        $.ajax({
            url : ctx + "kill/getKillQualification?datetime=" + new Date().getTime(),
            dataType : "json",
            data : {activityId : oThis.attr("activityId")},
            type : "post",
            async : true,
            success : function(data) {
                if (data.FLAG == '0') {
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack("WT.si_n","获取杀资格",true, "WT.si_x","获取杀资格成功",true);
                    }
                    window.location.href = ctx + "/kill/initKillConfirmOrder?activityId=" + oThis.attr("activityId");
                } else {
                    oThis.bind("click", fnKillStart);
                    oThis.html("秒杀");
                    if(typeof(dcsPageTrack)=="function") {
                        dcsPageTrack ("WT.si_n","获取杀资格",false ,"WT.si_x","获取杀资格失败",false,"WT.err_code","错误码（"+ data.FLAG +"）",false);
                    }
                    toggleModal("tk_modal" + data.FLAG);
                }
            },
            error : function() {
                $oThis.bind("click", fnKillStart);
                oThis.html("秒杀");
            }
        });
    }
}

function setTime(intDiff, $Timer) {
    var day = 0, hour = 0, minute = 0, second = 0;
    day = Math.floor(intDiff / (60 * 60 * 24));
    hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
    minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
    second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);

    $Timer.append('<span><s></s>' + (day <= 9 ?  "0" + day : day + '') + "</span>天");
    $Timer.append('<span><s></s>' + (hour <= 9 ? "0" + hour : hour + '') + "</span>时");
    $Timer.append('<span><s></s>' + (minute <= 9 ? "0" + minute : minute + '') + "</span>分");
    $Timer.append('<span><s></s>' + (second <= 9 ? "0" + second : second + '') + "</span>秒");
}


function fnShareConfirm() {
    var title = "一元秒杀";
    var shareContext = "我正在和包客户端参与1元秒杀爆款机型，比比谁手快！";
    var url = "http://wap.hn.10086.cn/shop/kill/init";
    share(title, url, shareContext);
    addToShare(activityId);
    toggleModal('tk_modal7');
    if ("true" != isBindCard) {
        toggleModal("tk_modal8");
    }
}

function fnShareCancel() {

}