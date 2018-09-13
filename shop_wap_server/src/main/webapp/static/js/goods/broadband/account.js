//增加对身份证号码和宽带密码的加密
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
    + "wxyz0123456789+/" + "=";
function encode64(str) {
    return strEnc(str, keyStr);
}
$(function(){
	$("#getSmsCodeBtn").click(function(){
        var phoneValidate = phoneValidateFun($("#mobileNum").val());
        if(phoneValidate == "1"){
            $(".cw-ts").removeClass("hide");
            return;
        }
		$(".cw-ts").addClass("hide");
		$.ajax({
            type : 'post',
            url : ctx+'broadbandAccount/sendSms',
            cache : false,
            dataType : 'text',
            data : {
                mobile : $("#mobileNum").val()
            },
            success : function(data) {
            	var res=data.replace('\"','').replace('\"','');
                if (res == "success") {
                	showAlert("您的验证码发送成功，请注意查收！");
                	$("#getSmsCodeBtn").attr("disabled","disabled");
                	send(61);
                } else {
                    showAlert(data);
            		$("#getSmsCodeBtn").val("获取验证码");
            		$("#getSmsCodeBtn").removeAttr("disabled");
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
                $("#getSmsCodeBtn").val("获取验证码");
        		$("#getSmsCodeBtn").removeAttr("disabled");
            }
        });
	});
});

function send(initSec){	
	initSec--;
	if(initSec==0) {
		$("#getSmsCodeBtn").val("获取验证码");
		$("#getSmsCodeBtn").removeAttr("disabled");
		return;
	}
	$("#getSmsCodeBtn").val("重新获取（"+initSec+"）");
	setTimeout("send("+initSec+");",1000);
}

function accountQry(){
    try{
        var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
        if(window.Webtrends){
            Webtrends.multiTrack({
                argsa: ["WT.si_n",sin,
                    "WT.si_x","21"],
                delayTime: 100
            })
        }else{
            if(typeof(dcsPageTrack)=="function"){
                dcsPageTrack ("WT.si_n",sin,true,
                    "WT.si_x","21",true);
            }
        }
    }catch (e){
        console.log(e);
    }
    var phoneValidate = phoneValidateFun($("#mobileNum").val());
    if(phoneValidate == "1"){
        showAlert("请输入正确的手机号码！");
        return;
    }
	if ($("#valiCode").val() == "") {
        showAlert("验证码不能为空！");
        return;
    }
	showLoadPop("正在查询...");
	$.ajax({
        type : 'post',
        url : ctx+'broadbandAccount/accountQry',
        cache : false,
        dataType : 'json',
        data : {
            mobile : $("#mobileNum").val(),
            smsCaptcha : $("#valiCode").val()
        },
        success : function(data) {
            /*插码*/
            try{
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",sin,
                            "WT.si_x","99"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",sin,true,
                            "WT.si_x","99",true);
                    }
                }
            }catch (e){
                console.log(e);
            }
        	var res=data.respCode.replace('\"','').replace('\"','');
            if(res == "0"){
            	$("#qryDiv").addClass("hide");
            	$("#qryResultDiv").removeClass("hide");
            	$("#accessAcct").html(data.accessAcct);
            	$("#rate").html(data.rate + "M");
            	if(data.mbh.length > 0){
            		$("#mbh").html("有魔百和");
            	}else{
            		$("#mbh").html("无魔百和");
            	}
                $("#yxDate").html(data.startTime + "~" + data.endTime);
            }else{
            	$("#qryDiv").addClass("hide");
            	$("#errorDiv").removeClass("hide");
            	$("#errorInfo").html(data.respDesc);
            }
            
            hideLoadPop();
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            /*插码*/
            try{
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",sin,
                            "WT.si_x","-99"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",sin,true,
                            "WT.si_x","-99",true);
                    }
                }
            }catch (e){
                console.log(e);
            }
        	hideLoadPop();
        	$("#errorInfo").html("服务中断，请重试！");
        	$("#errorDiv").removeClass("hide");
            $("#qryDiv").addClass("hide");
        }
    });
}

function accountReset(){
    /*插码*/
    try{
        var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
        if(window.Webtrends){
            Webtrends.multiTrack({
                argsa: ["WT.si_n",sin,
                    "WT.si_x","21"],
                delayTime: 100
            })
        }else{
            if(typeof(dcsPageTrack)=="function"){
                dcsPageTrack ("WT.si_n",sin,true,
                    "WT.si_x","21",true);
            }
        }
    }catch (e){
        console.log(e);
    }
	if ($("#accessAcct").val() == "") {
        showAlert("宽带账号不能为空！");
        return;
    }
    if ($("#psptId").val() == "") {
        showAlert("身份证号码不能为空！");
        return;
    }
    //身份证校验
    var checkIdCardMsg = checkIdcard($("#psptId").val());
    if(checkIdCardMsg !="验证通过!"){
        showAlert(checkIdCardMsg);
        return ;
    }
    var phoneValidate = phoneValidateFun($("#mobileNum").val());
    if(phoneValidate == "1"){
        showAlert("请输入正确的手机号码！");
        return;
    }
    if ($("#valiCode").val() == "") {
        showAlert("验证码不能为空！");
        return;
    }
	if ($("#newPwd").val() == "") {
        showAlert("新密码不能为空！");
        return;
    }
	if ($("#confirm_newPwd").val() == "") {
        showAlert("确认新密码不能为空！");
        return;
    }
	if ($("#confirm_newPwd").val() != $("#newPwd").val()) {
        showAlert("两次输入密码必须一致！");
        return;
    }
	showLoadPop("正在重置...");
	$.ajax({
        type : 'post',
        url : ctx+'broadbandAccount/accountReset',
        cache : false,
        dataType : 'json',
        data : {
            mobile : $("#mobileNum").val(),
            smsCaptcha : $("#valiCode").val(),
            psptId : encode64($("#psptId").val()),
            accessAcct : $("#accessAcct").val(),
            newPwd : encode64($("#newPwd").val()),
            confirm_newPwd : encode64($("#confirm_newPwd").val())
        },
        success : function(data) {
            /*插码*/
            try{
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",sin,
                            "WT.si_x","99"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",sin,true,
                            "WT.si_x","99",true);
                    }
                }
            }catch (e){
                console.log(e);
            }
            if(data.respCode == "0"){
            	$("#resetDiv").addClass("hide");
            	$("#resetResultDiv").removeClass("hide");
            }else{
            	$("#errorInfo").html(data.respDesc);
            	$("#errorDiv").removeClass("hide");
                $("#resetDiv").addClass("hide");
            }
            hideLoadPop();
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            /*插码*/
            try{
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",sin,
                            "WT.si_x","-99"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",sin,true,
                            "WT.si_x","-99",true);
                    }
                }
            }catch (e){
                console.log(e);
            }
        	$("#errorInfo").html("服务中断，请重试！");
        	$("#errorDiv").removeClass("hide");
            $("#resetDiv").addClass("hide");
            hideLoadPop();
        }
    });
}

/**
 * 验证身份证函数
 */
function checkIdcard(idcard){
    idcard = idcard.toString();
    var Errors=new Array("验证通过!","身份证号码位数不对!","身份证号码出生日期超出范围或含有非法字符!","身份证号码校验错误!","身份证地区非法!");
    //var Errors=new Array(true,false,false,false,false);
    var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
    var idcard,Y,JYM;
    var S,M;
    var idcard_array = new Array();
    idcard_array = idcard.split("");
    //地区检验
    if(area[parseInt(idcard.substr(0,2))]==null) return Errors[4];
    //身份号码位数及格式检验
    switch(idcard.length){
        case 15:
            return Errors[0];
            break;
        case 18:
            //18 位身份号码检测
            //出生日期的合法性检查
            //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
            //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
            //if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0 )) {
            //    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
            //} else {
            //    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
            //}
            //if (ereg.test(idcard)) {//测试出生日期的合法性
            //计算校验位
            S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
                + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
                + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
                + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
                + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
                + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
                + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
                + parseInt(idcard_array[7]) * 1
                + parseInt(idcard_array[8]) * 6
                + parseInt(idcard_array[9]) * 3;
            Y = S % 11;
            M = "F";
            JYM = "10X98765432";
            M = JYM.substr(Y, 1);//判断校验位
            if (M == idcard_array[17]) return Errors[0]; //检测ID的校验位
            else return Errors[3];
            //}
            //else return Errors[2];
            break;
        default:
            return Errors[1];
            break;
    }
}

/**
 * 中国移动手机号码校验
 * @param mobileNum
 * @returns {*}
 */
function phoneValidateFun(mobileNum) {
    //134-139 150-152 157-159 182-184 187 188 147
    var reg = /^1((3[4-9])||(5[0-2])||(5[7-9])||(8[2-4])||(8[7-8])||(47))\d{8}$/g;
    if (mobileNum == "" || mobileNum.length != 11) {
        return "1";
    }else if (!reg.test($("#mobileNum").val())) {
        return "1";
    }
    return "0";
}