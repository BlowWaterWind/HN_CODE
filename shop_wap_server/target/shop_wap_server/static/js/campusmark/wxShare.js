$(function() {
    shareWx();

});

function share() {
    try{
        if(window.Webtrends){
            Webtrends.multiTrack({
                argsa: ["WT.event","GJJ_FX"],
                delayTime: 100
            })
        }else{
            if(typeof(dcsPageTrack)=="function"){
                dcsPageTrack ("WT.event","gjj_FX",true);
            }
        }

    }catch (e){
        console.log(e);
    }
    if(is_weixn()){
        // toggleModal('share-modal')
        $("#mcover").show();
        wxtrends(21)
    }else{
        //Mozilla/5.0 (Linux; Android 7.0; MI 5 Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/59.0.3071.125 Mobile Safari/537.36 X-Tingyun-Id/p35OnrDoP8k;c=2;r=2000707370;u=8f16d5c286d7507bd5d0c1561700a1d2::E43DAD8FB1CD015E hebao/7.1.45 NetType/wifi
        //Mozilla/5.0 (iPhone; CPU iPhone OS 11_2_1 like Mac OS X) AppleWebKit/604.4.7 (KHTML, like Gecko) Mobile/15C153 Hebao/7.0.3 NetType/4G
        var uAgent = navigator.userAgent;
        if(uAgent.indexOf("Hebao")>-1 || uAgent.indexOf("hebao")>-1){
            shareHeBao($("#cardTypeName").val(),$("#recmdUserLink").val(),$("#cardTypeName").val())
        }else{
            // toggleModal("sorry-modal", "请在微信里打开分享！");
            alert( "请在微信里打开分享！");
        }
    }
}

function shareWx(){
    if(is_weixn()) {
        $.ajax({
            url: '/shop/recmd/ticketSignature',
            data : {
                "url":window.location.href
            },
            type :"post",
            success: function (data) {
                    shareFriend(data);

            }
        });
    }
}
function is_weixn(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i)=="micromessenger") {
        return true;
    } else {
        return false;
    }
}

//微信接口调用config
    function shareFriend(data)
    {
        appid = data.appId;
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: data.appId, // 必填，公众号的唯一标识
            timestamp: data.timeStamp, // 必填，生成签名的时间戳
            nonceStr: data.nonceStr, // 必填，生成签名的随机串
            signature: data.signature,// 必填，签名，见附录1
            jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage','onMenuShareQQ','onMenuShareQZone'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wxReady();
    }

    wx.error(function(res){
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
    // if(typeof(dcsPageTrack)=="function"){dcsPageTrack ("WT.si_n","发红包",false ,"WT.si_x","普通流量红包发送失败或随机流量红包发送失败",false,"WT.err_code","签名过期,微信接口验证失败",false);}
    console.log("微信接口验证失败:"+res.errMsg);
    // $('#share-modal').hide();
    //toggleModal("sorry-modal", "微信接口验证失败:"+res.errMsg);
    });

function wxReady() {
    var grabUrl = $("#recmdUserLink").val();
    var img = $("#titleImg").val();

    var cardTypeName;
    var adom = $('#share1');
    var title = adom.attr('title');
    cardTypeName =  $("#planDesc").val();

    var desc = "";
    var timelineTitle = cardTypeName;

    wx.ready(function () {
        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，
        // 所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，
        // 则可以直接调用，不需要放在ready函数中。
        //分享到朋友圈
        wx.onMenuShareTimeline({
            title: timelineTitle, // 分享标题
            link: grabUrl, // 分享链接
            imgUrl: img, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#share-modal').hide();
                // toggleModal("sorry-modal", "朋友圈分享成功");
                alert("朋友圈分享成功");
                wxtrends(-99)
                shareSuccess();
            },
            cancel: function () {
                $('#share-modal').hide();
                wxtrends(-99)
                // 用户取消分享后执行的回调函数
                // toggleModal("sorry-modal", "朋友圈取消分享");
            }
        });
        //分享给朋友
        wx.onMenuShareAppMessage({
            title: cardTypeName, // 分享标题
            desc: desc, // 分享描述
            link: grabUrl, // 分享链接
            imgUrl: img, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#share-modal').hide();
                alert("朋友分享成功");
                wxtrends(99);
                shareSuccess();
            },
            cancel: function () {
                $('#share-modal').hide();
                // 用户取消分享后执行的回调函数
                // toggleModal("sorry-modal", "朋友取消分享");
                wxtrends(-99)
            }
        });
        //分享QQ
        wx.onMenuShareQQ({
            title: cardTypeName, // 分享标题
            desc: desc, // 分享描述
            link: grabUrl, // 分享链接
            imgUrl: img, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#share-modal').hide();
                alert("QQ分享成功");
                wxtrends(99);
                shareSuccess();
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                // toggleModal("sorry-modal", "QQ取消分享");
                wxtrends(-99)
            }
        });
//分享到QQ朋友圈朋友圈
        wx.onMenuShareQZone({
            title: cardTypeName, // 分享标题
            desc: desc, // 分享描述
            link: grabUrl, // 分享链接
            imgUrl: img, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#share-modal').hide();
                alert("朋友分享成功");
                wxtrends(99)
            },
            cancel: function () {
                $('#share-modal').hide();
                // 用户取消分享后执行的回调函数
                // toggleModal("sorry-modal", "朋友取消分享");
                wxtrends(-99)
            }
        });
    });
}
/**
 * 和包分享
 * @param title
 * @param shareLink
 * @param shareText
 */
function shareHeBao(title, shareLink, shareText) {
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

function wxtrends(six) {
    try {

        if(window.Webtrends) Webtrends.multiTrack({
            argsa: ["WT.si_n",document.getElementsByTagName('meta')['WT.si_n'].content ,"WT.si_x",six],
            delayTime: 100
        });

    }catch (e) {
        console.log(e)
    }

}