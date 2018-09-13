function share(title, shareLink, shareText) {
    var uAgent = navigator.userAgent;
    // 安卓
    if (uAgent.indexOf('Android') > -1 || uAgent.indexOf('Adr') > -1) {
        window.MobileWalletShare.startShare(title, shareLink, shareText);
    }
    // ios
    else if (!!uAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
        window.location.href = "cyberapp://IOSShare#IOSShareTitle=" + title +"#IOSShareURL=" + shareLink +"#IOSShareDetails=" + shareText;
    }
}

function reloadUrl() {
    if (location.href.indexOf('?') > -1) {
        window.location.href=location.href+'&time='+((new Date()).getTime());
    } else {
        window.location.href=location.href+'?time='+((new Date()).getTime());
    }

}

function toHeBaoIndex() {
    var uAgent = navigator.userAgent;
    // 安卓
    if (uAgent.indexOf('Android') > -1 || uAgent.indexOf('Adr') > -1) {
        window.goActivity.closeWebView();
    }
    // ios
    else if (!!uAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
        window.location.href = "activity://HBSY";
    }
}


function goToBindBank() {
    var uAgent = navigator.userAgent;
    // 安卓
    if (uAgent.indexOf('Android') > -1 || uAgent.indexOf('Adr') > -1) {
        window.goActivity.goAddBankCard("");
    }
    // ios
    else if (!!uAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
        window.location.href = "activity://BDYHK";
    }
}

function addToShare(activityId) {
    if (!activityId || "" == activityId) {
        return;
    }
    // 异步处理， 不处理返回结果
    $.ajax({
        url : ctx + "bargainInHeBao/addToShare?datetime=" + new Date().getTime(),
        dataType : "json",
        data : {activityId : activityId},
        type : "post",
        async : true,
        success : function(data) {

        },  error : function() {

        }
    });
}

function shareGetFlow(orderSubId) {
    if (!orderSubId || "" == orderSubId) {
        return;

    }

    $.ajax({
        url : ctx + "bargainInHeBao/shareToFlowInit?datetime=" + new Date().getTime(),
        dataType : "json",
        data : {orderSubId : orderSubId},
        type : "post",
        async : true,
        success : function(data) {
            if (data.FLAG == '1') {
                showAlert(data.MSG);
            } else {
                var type = data.type;
                var phoneName = data.phoneName;
                if (type == '2') {
                    var title = "1元秒杀";
                    var shareText = "天啦噜， 1元秒杀到了" + phoneName + "手机，快来羡慕手速逆天的我吧！";
                    var shareLink = "http://wap.hn.10086.cn/shop/kill/init";
                    sendFlow(orderSubId);
                    share(title, shareLink, shareText);
                } else if(type == '10' || type == '11') {
                    var title = "和包客户端砍价";
                    var shareText = "我已成功砍价并立减购买" + phoneName +"手机！便宜不止一点点，快来参与吧！";
                    var shareLink = "http://wap.hn.10086.cn/shop/bargainInHeBao/init";
                    if (type == '10') {
                        sendFlow(orderSubId);
                    }
                    share(title, shareLink,shareText);
                }
            }
        },
        error : function() {
            showAlert("系统正忙, 请稍后再试");
        }
    });
}

function sendFlow(orderSubId) {
    if (!orderSubId || "" == orderSubId) {
        return;
    }
    $.ajax({
        url : ctx + "bargainInHeBao/sendFlow?datetime=" + new Date().getTime(),
        dataType : "json",
        data : {orderSubId : orderSubId},
        type : "post",
        async : true,
        success : function(data) {
            if (data.FLAG == "1") {
                showAlert(data.MSG);
            } else if (data.FLAG == "0") {
                showAlert("流量赠送成功！！");
            }
        }, error : function() {
            showAlert("系统正忙, 请稍后再试");
        }
    });
}