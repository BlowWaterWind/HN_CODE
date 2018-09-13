/**
 * Created by Administrator on 2017/3/13.
 */
$(function() {

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
        $("#speedUp").removeClass('btn-rose').addClass('dg-gray').attr("disabled", "true");
        $("#speedUpForm").submit();
    });

    $("#handleSpeedUp").click(function() {
        $("#toSpeedup").addClass('dg-gray').attr("disabled", "true");
        var password = encode64($("#password").val());
        $("#staffPwd").val(password);
        //校验是否能提速
        $.ajax({
            type : 'post',
            url :  baseProject+'/o2oSpeedUp/checkSpeedUp',
            cache : false,
            dataType : 'json',
                data :$("#handleForm").serialize(),
            success : function(data) {
                if (data.respCode=='0'&& data.orderId !=null) {
                    sendSms(data.orderId);
                } else {
                    showAlert(data.respDesc);
                    $("#toSpeedup").removeClass('dg-gray').addClass('btn-rose').removeAttr("disabled");
                    return false;
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
                $("#toSpeedup").removeClass('dg-gray').addClass('btn-rose').removeAttr("disabled");
                return false;
            }
        });

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
//发送短信
function sendSms(orderId){
    var newBandWidth=$("#newBandWidth").val();
    $.ajax({
        type : 'post',
        url :  baseProject+'/o2oBusiness/invokeSmsInterface',
        cache : false,
        dataType : 'json',
        data : {
            goodsName : '宽带提速'+newBandWidth,
            serialNumber: $("#serialNumber").val(),
            broadbandType: '19',
            orderId: orderId
        },
        success : function(data) {
            if (data.X_RESULTCODE=='0') {
                //showAlert("订单提交成功，请等待客户短信回复");
                window.location.href = baseProject+ "/o2oBusiness/smssuccess";
            } else {
                showAlert(data.X_RESULTINFO);
                return false;
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlert("服务中断，请重试");
            $("#handleSpeedUp").removeClass('dg-gray').addClass('btn-rose').removeAttr("disabled");
            return false;
        }
    });
}

function testCallBack(){
    $.ajax({
        type : 'post',
        url :  baseProject+'/o2oBusiness/initFlow',
        cache : false,
        dataType : 'json',
        data : {
            content : '是',
            serialnumber: $("#serialNumber").val(),
            numCode: '100019'
        },
        success : function(data) {
           //alert(data);
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlert("服务中断，请重试");
        }
    });

}

function encode64(password) {

    return strEnc(password, keyStr);
}

