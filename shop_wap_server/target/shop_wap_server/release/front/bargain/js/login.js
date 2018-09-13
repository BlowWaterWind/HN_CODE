//g_code_cutdown：倒计时秒数
var g_code_cutdown = 60;
var g_timeOut; /*获取验证码倒计时*/
var doSendRandomCode ="0";
 function smsMessage(){

        if ($(".mobileg").val() == "")
        {
            popTips("请输入手机号！");
            $(".mobileg").focus();
            return false;
        }
        var reg = /^1\d{10}$/;
        if (!reg.test($(".mobileg").val() ))
        {
            popTips("请输入正确的手机号！");
            $(".mobileg").focus();
            return false;
        }
        if (doSendRandomCode == "0") {
            $("#btnSendCode").addClass("disabled");
            doSendRandomCode="1";
            $.ajax({
                type: 'post',
                url: "/shop/bargain/sendBgRandomCode",
                cache: false,
                context: $(this),
                data: {
                    mobile: $(".mobileg").val()
                },
                success: function (data) {
                    var a = data.X_RESULTINFO;
                    if (data.X_RESULTCODE == 0) {
                        cutDown();
                        popTips("短信下发成功，请注意收取！");
                    } else {
                        $("#btnSendCode").val("再次获取");
                        popTips(a);
                    }
                    setTimeout(releaseSendRandom(),500);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    doSendRandomCode ="0";
                    $("#btnSendCode").removeClass("disabled");
                    popTips("服务中断，请重试");
                }
            });
        }
        return false;
    }

    var flag=" ";
    function loginPhone() {
        var phone = $(".mobileg").val();
        var code = $(".codeg").val();
        if (phone == null || phone == "") {
            popTips("请输入手机号");
            return false;
        }
        if (code == null || code == "") {
            popTips("请输入验证码");
            return false
        }
        if (checkPhone('mobileg')) {
            $.ajax({
                type: 'post',
                url: '/shop/bargain/checkBarginCode',
                cache: false,
                context: $(this),
                // dataType : 'text',
                data: {
                    phoneNo: $(".mobileg").val(),
                    smsCode: $(".codeg").val(),
                    flag: "1",
                },
                success: function (data) {
                    if (data.X_RESULTCODE == 0) {
                        //如果成功则分享页面出现
                    	$('.p_login_con').hide();
                    	$('.layui-layer-shade').hide();
                    } else {
                        var a = data.X_RESULTINFO;
                        popTips(a);
                        return;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    popTips("服务中断，请重试");
                    return;
                }
            });
        }
        return false;
    }

//获取验证码倒计时
function cutDown() {

    $(".get_sms").addClass("disabled");
    $(".get_sms").html(g_code_cutdown + "S后获取");
    g_code_cutdown--;
    if (eval(g_code_cutdown) == eval(0)) {
        $(".get_sms").removeClass("disabled");
        $(".get_sms").html("获取验证码");
        window.clearTimeout(g_timeOut);
        g_code_cutdown = 5;
    } else {
        g_timeOut = window.setTimeout("cutDown()", 1000);
    }

}
//校验是否为移动手机号
function checkPhone(t){
    var tel = $("."+t).val();
    var reg = /^1(3[4-9]|5[012789]|8[23478]|4[7]|7[8])\d{8}$/;
    if (reg.test(tel)) {
        return true;
    }else{
        popTips("请输入11位中国湖南移动手机号码");
        return;
    };
}

function releaseSendRandom(){
    doSendRandomCode ="0";
    $("#btnSendCode").removeClass("disabled");
}


$(window).on("load",function(){
	 var isWeixin = is_weixin();	 	 
	 if(isWeixin){
		  $.ajaxDirect({
		        url: 'share/ticketSignature',
		        //需要分享的url
		        param:'url='+location.href.split('&')[0],
		        afterFn:function(data){
		            shareBargain(data);
		        }
		    });
	 }
})
 
//ajax 返回 结果
function shareBargain(object) {
	//alert("---shareBargain---");
    //调用 微信接口
	 var url = "http://wap.hn.10086.cn/weixin/BargainActivity/initPage?key=process&actId="+ getQueryString("actId");
	 url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf6e74a02d659d984&redirect_uri="+encodeURIComponent(url)+"&response_type=code&scope=snsapi_base&state=1";
    wx.config({
        debug : false,
        appId : object.appId,
        timestamp : object.timeStamp,
        nonceStr : object.nonceStr,
        signature : object.signature,
        jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage']// 使用JS-SDK的什么功能
    });

    wx.ready(function() {
        // 获取“分享到朋友圈”按钮点击状态及自定义分享内容接口
        wx.onMenuShareTimeline({
            title : '金立手机从1299到700，只差你那一刀。', // 分享标题
            desc : "砍价抢红包抵扣购机款，等你来砍！", // 分享描述
            link :url ,
            imgUrl : 'http://wap.hn.10086.cn/shop/release/front/bargain/images/cutprice.jpg', // 分享图标http://wap.hn.10086.cn/
            success: function () {
                if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","分享",true);}
                /*// 用户确认分享后执行的回调函数
                $.ajaxDirect({
                    url: ' ',  //分享执行的回调函数
                    param:"actId="+actId,
                    afterFn:function(data){
                        if(data.result=='0'){
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动成功",true);}
                        }else{
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动失败",true, "WT.err_code","发起活动失败",false);}
                        }
                    }
                });*/
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
        // 获取“分享给朋友”按钮点击状态及自定义分享内容接口
        wx.onMenuShareAppMessage({
            title : '金立手机从1299到700，只差你那一刀。', // 分享标题
            desc : "砍价抢红包抵扣购机款，等你来砍！", // 分享描述
            link : url,
            imgUrl : 'http://wap.hn.10086.cn/shop/release/front/bargain/images/cutprice.jpg', // 分享图标
            type : 'link', // 分享类型,music、video或link，不填默认为link
            success: function () {
                if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","分享",true);}
                // 用户确认分享后执行的回调函数
              /*  $.ajaxDirect({
                    //用户分享后执行的回调函数
                    url: '',
                    param:"actId="+actId,
                    afterFn:function(data){
                        if(data.result=='0'){
                            //该if为插码
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动成功",true);}
                        }else{
                            //该if为插码
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动失败",true, "WT.err_code","发起活动失败",false);}
                        }
                	///}
                });*/
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
//              console.log("shibai分享");
            }
        });
    });
}