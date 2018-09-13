<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/4/3
  Time: 09:59
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html lang="zh-CN">
<head>
    <title>浦发客户移动专属套餐</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <meta name="format-detection" content="telephone=no, email=no"/>

    <!-- css -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-6.0.0.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/wap.spdb.sim.css"/>

    <!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/simbank/comm.js"></script>

    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <%--插码相关--%>
    <meta name="WT.si_n" content="浦发专享号卡">
    <meta name="WT.si_x" content="短信确认套餐">

</head>
<body>

<div class="top-aifix">
    <div class="header">
        <a class="return-con" href="javascript:history.back(-1);"></a>
        <h4>浦发客户移动专属套餐</h4>
    </div>
</div>
<div class="container">
    <input type="hidden" id="planId" value="${planId}">
    <input type="hidden" id="orderSubNo" value="${orderSubNo}">
    <div class="banner"><img src="${ctxStatic}/images/spdbsim/banner0720.png"/></div>
    <!--流程图 start-->
    <div class="tag-box"><img src="${ctxStatic}/images/spdbsim/tag02.png"/></div>
    <!--流程图 end-->
    <!--表单 start-->
    <div class="form-box">
        <div class="code-box">
            <div class="code-box-title">您已成功选择${planName}套餐，补贴${planSubsidy}元活动</div>
            <ul class="code-box-list">
                <li>
                    <span>手机号码</span>
                    <div class="code-box-rt">
                        <input type="text" class="form-input" id="mobile" value="${mobile}" readonly/>
                        <button class="code-btn code ml20">获取验证码</button>
                    </div>
                </li>
                <li>
                    <span>短信验证码</span>
                    <div class="code-box-rt">
                        <input type="text" id="smsCode" class="form-input" placeholder="请输入短信验证码"/>
                    </div>
                </li>
                <li>
                    <span>验证码</span>
                    <div class="code-box-rt">
                        <input type="text" class="form-input" onkeyup="validateCaptcha(this)"/>
                        <a href="JavaScript:void(0);"
                           onclick="javascript:document.getElementById('captchaImage').src='${pageContext.request.contextPath}/common/getCaptchaImage.do?temp=123'+ (new Date().getTime().toString(36)); captchaCheck = false; return false"
                           class="change" title="看不清,点击更换验证码">
                            <img src="${pageContext.request.contextPath}/common/getCaptchaImage.do" name="captchaImage"
                                 id="captchaImage" border="0" style="width:75px;height:20px;">
                        </a>
                        <%--<input type="image" class="code-img ml20" src="${ctxStatic}/images/spdbsim/code.png" width="90" height="40" />--%>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <!--表单 end-->
    <div class="gobal-btn">
        <a class="btn btn-rose w50" data-toggle="modal" id="submitBtn">下一步</a>
        <!--灰色按钮 class btn-rose替换为 btn-gray-->
    </div>
</div>
<!--弹框 start-->
<div class="modal fade gobal-modal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">套餐办理失败<br>详情请咨询10086</p>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<!--弹框 end-->

<div class="modal fade gobal-modal" data-backdrop="static" id="myModal04" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="load-con">
                    <i class="loading" id="loading"></i>
                    <p class="pt10" id="result">活动正在办理,请稍后!</p>
                </div>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50">结束</a></div>
        </div>
    </div>
</div>

</body>
<form id="toFreeze" method="post" action="toFreeze">
    <input type="hidden" name="orderSubNo" value="${orderSubNo}">
    <input type="hidden" id="payAmount" name="payAmount">
</form>
<script type="text/javascript">
    var ctx = ${ctx};
    var captchaCheck = false;

    $(function () {
        //验证码倒计时
        var code = $(".code");
        var validCode = true;
        code.click(function () {
            $.ajax({
                type: 'post',
                url: ${ctx}+'/bankCommon/smsSend',
                cache: false,
                context: $(this),
                dataType: 'json',
                data: {
                    mobile: $("#mobile").val(),
                    codeLength : 6
                },
                success: function (data) {
                    if (data.code == '0') {
                        if (data.data.X_RESULTCODE == '-1') {
                            toggleModal('myModal', data.data.X_RESULTINFO);
                        } else {
                            //开始倒计时
                            var time = 120; //倒计时时间

                            if (validCode) {
                                validCode = false;
                                var t = setInterval(function () {
                                    time--;
                                    code.text(time + "秒");
                                    if (time == 0) {
                                        clearInterval(t);
                                        code.text("重新获取");
                                        validCode = true;
                                    }
                                }, 1000);
                            }
                        }
                    } else {
                        toggleModal('myModal', data.message);
                    }
                },
                error: function () {
                    toggleModal('myModal', '系统异常<br>请稍后再试!');
                }
            });
        });
    });

    function validateCaptcha(obj){
        var captcha = $(obj).val();
        if(captcha.length < 4){
            return;
        }
        $.ajax({
            type: 'post',
            url: ${ctx}+'/bankCommon/validateCaptcha',
            cache: false,
            dataType: 'json',
            data: {
                captchaCode: captcha
            },
            success:function(data){
                if(data.code == '0'){
                    //验证成功
                    captchaCheck = true;
                }else{
                    //验证不成功(输入错误或者验证码超时)时,发起请求获得新的验证码-->参考商城
                    toggleModal('myModal', data.message);
                    $("#captchaImage").click();

                }
            },
            error:function () {
                toggleModal('myModal', data.message);
            }
        });
    }

    $("#submitBtn").click(function(){
        var smsCode = $("#smsCode").val();
        if(smsCode == ""){
            toggleModal('myModal', '请填写短信验证码!');
            return;
        }
        if(!captchaCheck){
            toggleModal('myModal', '请填写正确的验证码!');
            return;
        }
        $("#myModal04").modal('toggle');
        $.ajax({
            type: 'post',
            url: ${ctx}+'/bankCommon/confirmOffer',
            dataType: 'json',
            data: {
                mobile:$("#mobile").val(),
                smsCode :smsCode,
                bankPlanId:"${bankPlanId}",
                orderSubNo:"${orderSubNo}"
            },
            success:function (data) {
                if(data.code == '0'){
                    $("#payAmount").val(data.data.payAmount / 100);
                    $("#toFreeze").submit();
                }else if(data.code == '-1'){
                    //支付还没有回调
                    $("#myModal04").modal('toggle');
                    if(data.message == undefined){
                        data.msessage = '系统异常';
                    }
                    toggleModal('myModal', data.message);
                }else if(data.code == '-3'){

                }
            },
            error:function(){
                toggleModal('myModal', '系统异常');
            }

        });
        try{
            dcsPageTrack("WT.si_n", "浦发银行活动办理", 0, "WT.si_x", "短信确认", 0);//插码使用
        }catch (e) {
            console.log("插码失败");
        }
    });



</script>
</html>
