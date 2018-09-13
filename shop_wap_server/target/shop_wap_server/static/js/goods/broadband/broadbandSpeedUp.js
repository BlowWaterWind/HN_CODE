var ua = navigator.userAgent.toLowerCase();
if(ua.match(/leadeon/i)=="leadeon"){
    //走手厅
    document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc_leadeon.js"><\/script>');
}else{
    document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc9.js"><\/script>');
    document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"><\/script>');
}
$(function() {
	
	var WTjson=WTjson||[];
	WTjson["WT.branch"]="hn";
	var ua = navigator.userAgent.toLowerCase();
	var st = '';
	if(ua.match(/leadeon/i)=="leadeon"){
		//手厅插码
		WTjson["WT.plat"]="app";
		leadeon.init=function(){
			//获取用户信息
			leadeon.getUserInfo({
				debug:false,
				success:function(res){
					//保存用户信息
					if(!!res.cid){
						WTjson["WT.cid"]=res.cid;
					}
					
					if(!!res.phoneNumber){
						WTjson["WT.mobile"]=res.phoneNumber;
					}
				},
				error:function(res1){
					
				}
			});
		}
	}else{
		WTjson["WT.plat"]="touch";
	}
	
    var InterValObj; // timer变量，控制时间
    var count = 60; // 间隔函数，1秒执行
    var curCount;// 当前剩余秒数
// timer处理函数
    function SetRemainTime() {
        if (curCount == 0) {
            window.clearInterval(InterValObj);// 停止计时器
            $("#getValiCode").removeAttr("disabled");// 启用按钮
            $("#getValiCode").html("重新发送验证码");
        } else {
            curCount--;
            $("#getValiCode").html("请在" + curCount + "秒内输入验证码");
        }
    }

    //发送短信验证码
    $("#getValiCode").click(function() {
    	if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","宽带提速",0,"WT.si_x","宽带提速短信验证码发送",0)}
        var isMobile = /^1\d{10}$/;

        if ($("#mobile").val() == "") {
            showAlert("手机号不能为空");
            return;
        }
        if (!isMobile.test($("#mobile").val())) {
            showAlert("手机号格式有误！");
            return;
        }
        curCount = count;
        // 设置button效果，开始计时
        $("#getValiCode").attr("disabled", "true");
        $("#getValiCode").html("请在" + curCount + "秒内输入验证码");
        InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次
        $.ajax({
            type : 'post',
            url : baseProject+'/broadbandSpeedUp/sendSms',
            cache : false,
            context : $(this),
            dataType : 'text',
            data : {
                mobile : $("#mobile").val()
            },
            success : function(data) {
                var res=data.replace('\"','').replace('\"','');
                if (res != "success") {
                    showAlert(data);
                    $("#getValiCode").removeAttr("disabled");// 启用按钮
                    $("#getValiCode").val("重新发送验证码");
                    if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","宽带提速",0,"WT.si_x","宽带提速短信验证码发送成功",0)}
                }else{
                	if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","宽带提速",0,"WT.si_x","宽带提速短信验证码发送失败",0)}e
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
                $("#getValiCode").removeAttr("disabled");// 启用按钮
                $("#getValiCode").val("重新发送验证码");
            }
        });
    });

    $("#speedUp").click(function() {
        /*插码*/
        try{
            var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
            if(window.Webtrends){
                Webtrends.multiTrack({
                    argsa: ["WT.si_n",sin,
                        "WT.si_x","22"],
                    delayTime: 100
                })
            }else{
                if(typeof(dcsPageTrack)=="function"){
                    dcsPageTrack ("WT.si_n",sin,true,
                        "WT.si_x","22",true);
                }
            }
        }catch (e){
            console.log(e);
        }
    	if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","宽带提速",0,"WT.si_x","宽带提速办理校验",0)}
    	
        if ($("#mobile").val().length == 0) {
            showAlert("手机号不能为空！");
            return false;
        }
        if ($("#valiCode").val().length == 0) {
            showAlert("验证码不能为空！");
            return false;
        }
        $("#speedUp").removeClass('btn-rose').addClass('dg-gray').attr("disabled", "true");
        //校验验证码，校验是否能提速
        $.ajax({
            type : 'post',
            url :  baseProject+'/broadbandSpeedUp/checkSpeedUp',
            cache : false,
            dataType : 'json',
            data : {
                mobile : $("#mobile").val(),
                smsCaptcha : $("#valiCode").val(),
                bandAccount: $("#bandAccount").val(),
                newBandWidth: $("#newBandWidth").val(),
                payAmount: $("#payAmount").val()
            },
            success : function(data) {
                if (data.respCode=='0') {
                	if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","宽带提速",0,"WT.si_x","宽带提速办理校验成功",0)}
                    $("#speedUpForm").submit();
                } else {
                	if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","宽带提速",0,"WT.si_x","宽带提速办理校验失败",0)}
                    showAlert(data.respDesc);
                    $("#speedUp").removeClass('dg-gray').addClass('btn-rose').removeAttr("disabled");
                    return false;
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
                $("#speedUp").removeClass('dg-gray').addClass('btn-rose').removeAttr("disabled");
                return false;
            }
        });
    });

    $("#handleSpeedUp").click(function() {
        $("#handleSpeedUp").addClass('dg-gray').attr("disabled", "true");
        $("#handleForm").submit();
    });

});

//确认选择宽带帐号
function comfirAccount(account,rate){
    $("#broadbandAccount").val(account);
    $("#oldBandWidth").val(rate);
    if(rate>=100||rate.indexOf("100")>0 ){
        showAlert("您的宽带已经是最高速率");
        return;
    }
    $("#form1").submit();
}

//选择宽带速率
function chooseRate(obj){
    var rate=$(obj).attr("data");
    var rateStr="rate"+rate;
    var price=$("input[name="+rateStr+"]").val();
    $("#newBandWidth").val(rate);
    $("#payAmount").val(price);
}



