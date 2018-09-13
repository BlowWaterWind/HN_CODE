/**
 * the common js of the project
 *
 * @author: xxxx
 * @version: 2016/10/27 10:51
 *           $Id$
 */
$(function () {
    // bang success
    var bangCount = 0;
    $(".bt_bang").click(function () {
        // 逻辑根据具体情况修改
        if (bangCount > 0) {
            $('.aiui-modal.modal_bang_fail').fadeIn();
        } else {
            $('.aiui-modal.modal_bang_success').fadeIn();
            bangCount++;
        }
        return false;
    });

    // rule
    $('.bt_rule').click(function () {
        $('.aiui-modal.modal_rule').fadeIn();
        return false;
    });

    // han
    $('.bt_han').click(function () {
        $('.aiui-modal.modal_han').fadeIn();
        // $('.aiui-modal.modal_han').show();
        return false;
    });

    // // share
    // $('.aiui-modal.modal_share').show();
    // $('.aiui-modal.modal_share').click(function () {
    //     $(this).fadeOut();
    // });

    // phone info
    $('.product_link').click(function () {
        $('.aiui-modal.modal_info').fadeIn();
        return false;
    });
    // $('.aiui-modal.modal_info').show();

    // close modal
    $('.aiui-modal .modal-close').click(function () {
        $(this).parents('.aiui-modal').fadeOut();
        return false;
    });

	var InterValObj; // timer变量，控制时间
	var count = 60; // 间隔函数，1秒执行
	var curCount;// 当前剩余秒数
	// timer处理函数
	function SetRemainTime() {
		if (curCount == 0) {
			window.clearInterval(InterValObj);// 停止计时器
			$("#btnSendCode").removeAttr("disabled");// 启用按钮
			$("#btnSendCode").val("重新发送验证码");
		} else {
			curCount--;
			$("#btnSendCode").val("请在" + curCount + "秒内输入验证码");
		}
	}

    $(".get_sms").click(function() {
        if ($("#mobile").val() == "") {
            showAlert("请输入手机号！".fontcolor("red"));
            $("#mobile").focus();
            return false;
        }
        var reg = /^1\d{10}$/;
        if (!reg.test($("#mobile").val() )) {
            showAlert("请输入正确的手机号！".fontcolor("red"));
            $("#mobile").focus();
            return false;
        }
        // $.ajax({
        //     type : 'post',
        //     url : "${ctx}/checkmobile/extract",
        //     cache : false,
        //     context : $(this),
        //     dataType : 'text',
        //     data : {
        //         mobile : $("#mobile").val()
        //     },
        //     success : function(data) {
        //         if (data.X_RESULTCODE==0) {
        //             showAlert(data.X_RESULTINFO);
        //         } else {
        //             showAlert("请输入湖南移动手机号！");
        //             $("#mobile").focus();
        //             return false;
        //         }
        //     },
        //     error : function(XMLHttpRequest, textStatus, errorThrown) {
        //         showAlert("服务中断，请重试");
        //     }
        // });
        curCount = count;
        // var sendSmsUrl="${ctx}/bargainSms/sendBgRandomCode";
        // 设置button效果，开始计时
        $("#btnSendCode").attr("disabled", "true");
        $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");
        var tips;
        InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次
        $.ajax({
            type : 'post',
            url : "/shop/bargainSms/sendBgRandomCode",
            cache : false,
            context : $(this),
            // dataType : 'json',
            data : {
                mobile : $("#mobile").val()
            },
            success : function(data) {
                var a = data.X_RESULTINFO.toString();
                if (data.X_RESULTCODE==0) {
                    showAlert(a.fontcolor("red"));
                } else {
                    showAlert(a.fontcolor("red"));
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试".fontcolor("red"));
            }
        });
    });
    var flag="";
    $("#form_mobile").click(function () {
        var phone=$("#mobile").val();
        var code = $("#code").val();
         if(phone==null|| phone==""){
             showAlert("请输入手机号".fontcolor("red"));
             return false;
        }
        if(code==null||code==""){
            showAlert("请输入验证码".fontcolor("red"));
            return false
        }
        else{
            if(checkPhone('mobile')){
                $.ajax({
                    type : 'post',
                    url : '/shop/bargainSms/sendBgRandomCode?smsCode='+ flag,
                    cache : false,
                    context : $(this),
                    dataType : 'text',
                    data : {
                        phoneNo : $("#mobile").val(),
                        smsCode : $("#code").val(),
                    },
                    success : function(data) {
                        if (data.X_RESULTCODE==0) {
                            //如果成功则分享页面出现
                            $('.aiui-modal.modal_share').show();
                            $('.aiui-modal.modal_share').click(function () {
                                $(this).fadeOut();
                            });
                        } else {
                            var a=data.X_RESULTINFO.toString();
                            showAlert(a.fontcolor("red"));
                            return;
                        }
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                        showAlert("服务中断，请重试".fontcolor("red"));
                        return;
                    }
                });
            }
        }
    });

});
//校验是否为移动手机号
function checkPhone(t){
    var tel = $("#"+t).val();
    var reg = /^1(3[4-9]|5[012789]|8[23478]|4[7]|7[8])\d{8}$/;
    if (reg.test(tel)) {
        return true;
    }else{
        showAlert("请输入11位中国湖南移动手机号码".fontcolor("red"));
        return;
    };
}
// /**
//  * 提示消息
//  * @param obj
//  */
// function showAlert(obj,callback) {
//     $("#alertMsgInfo_content").html(obj);
//     __innnerMessageShow();
//     if (typeof callback === "function") {
//         setTimeout(callback, 2000);
//     }
// }

//ajax 返回 结果
function shareBargain(object) {
    //调用 微信接口
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
            title : '我要砍价！', // 分享标题
            desc : "砍价等你来砍！", // 分享描述
            link : object.url,
            imgUrl : '', // 分享图标
            success: function () {
                if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","分享",true);}
                // 用户确认分享后执行的回调函数
                $.ajaxDirect({
                    url: ' ',  //分享执行的回调函数
                    param:"activityid="+activityid,
                    afterFn:function(data){
                        if(data.result=='0'){
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动成功",true);}
                        }else{
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动失败",true, "WT.err_code","发起活动失败",false);}
                        }
                    }
                });
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
        // 获取“分享给朋友”按钮点击状态及自定义分享内容接口
        wx.onMenuShareAppMessage({
            title : '我要砍价', // 分享标题
            desc : "砍价等你来砍！", // 分享描述
            link : object.url,
            imgUrl : '', // 分享图标
            type : 'link', // 分享类型,music、video或link，不填默认为link
            success: function () {
                if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","分享",true);}
                // 用户确认分享后执行的回调函数
                $.ajaxDirect({
                    url: '',
                    param:"activityid="+activityid,
                    afterFn:function(data){
                        if(data.result=='0'){
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动成功",true);}
                        }else{
                            if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","砍价",true, "WT.si_x","发起活动失败",true, "WT.err_code","发起活动失败",false);}
                        }
                    }
                });
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
//              console.log("shibai分享");
            }
        });
    });
}