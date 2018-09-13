<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width"/>
        <meta name="viewport" content="initial-scale=1.0,user-scalable=no"/>
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <Meta name="WT.mobile" content="">
        <Meta name="WT.brand" content="">
        <META name="WT.si_n" content="WZXD_Login">
        <META name="WT.si_x" content="20">

    <title>在线售卡-我要推荐</title>
    <link href="${ctxStatic}/css/sim/wap-simh5onlineV2.css?v=2" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/css.css?v=8" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript">
        var ctx = ${ctx};
    </script>
</head>
<style>
    .daojishi{
        background-color: #2e83ff;
    }

    .stopDaojishi{
        background-color: #999;
    }
</style>

<body>
<%--<div class="container">--%>
    <%--<div class="topbar">--%>
        <%--<a href="javascript:history.back(-1);" class="icon icon-return">返回</a>--%>
        <%--<a href="javascript:history.back(-1);" class="icon icon-close">关闭</a>--%>

    <%--</div>--%>
<div class="box">
    <div class="content">
            <img class="logo" src="${ctxStatic}/css/sim/images/logo.png"/>
            <img class="title1 w1" src="${ctxStatic}/css/sim/images/title.png"/>
            <p class="p1"><a href="${ctx}recmd/getMoreDetail">点我了详情&gt;&gt;</a></p>
                <form id="genRecmdCodeLink" action="${ctx}recmd/genRecmdQrCode" method="post">
                    <input type="hidden" name="recmdBusiParam" value="${orderRecmd.recmdBusiParam}">
                    <input type="hidden" name="recmdActConf.confMainColor" value="${recmdActConf.confMainColor}">
                    <input type="hidden" name="rcdConfId" value="${orderRecmd.rcdConfId}">
                    <div class="input_box">
                        <div class="l_input">
                            <input type="tel" name="recmdPhone" placeholder="请输入湖南移动手机号码" onkeyup="daojishi(this)" onFocus="javascript:if(this.value=='请输入湖南移动手机号码')this.value='';">
                        </div>
                        <div class="l_input">
                            <span class="fl inp2">
                                <input type="tel" placeholder="请输入验证码" onFocus="javascript:if(this.value=='请输入验证码')this.value='';"  name="smsCode">
                            </span>
                            <span class="yzm">
                               <a class="getcode daojishi">获取验证码</a>
                            </span>
                        </div>
                    </div>
                    <input type="button"  class="login_button"/>
                </form>
    </div>
</div>
<%-- 公用弹窗 --%>
<div id="sorry-modal" class="mask-layer">
    <div class="modal small-modal" style="width:18rem">
        <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')" style="width: 1.2rem;
    height: 1.2rem;"></a>
        <div class="modal-content">
            <%--<div class="pt-30"></div>--%>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group" style="font-size:1.0rem">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>


<div id="banli" class="mask-layer">
    <div class="modal small-modal" style="width:18rem">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('banli')" style="width: 1.2rem;
    height: 1.2rem;"></a>
        <div class="modal-content">
            <%--<div class="pt-30"></div>--%>
            <p class="text-center _pomptTxt">对不起，您不符合参与条件。您可成为湖南移动用户后再参加。</p>
        </div>
        <div class="btn-group" style="font-size:1.0rem">
            <a href="${ctx}/simBuy/simH5OnlineToApply?confId=2002" class="confirm-btn">确认</a>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=4"></script>
<script type="text/javascript">
    //== 短信验证码倒计时
    var wait = 60;
    var verify = true;
    function time(o) {
        if (wait == 0) {
            o.removeAttribute("disabled");
            $(o).html("获取验证码");
            daojishi();
            wait = 60;
        } else {
            o.setAttribute("disabled", true);
            $(o).html("重新发送(" + wait + ")");
            wait--;
            setTimeout(function () {
                time(o)
            }, 1000)
        }
    }
    function genSmsVerifyCode(phone) {
        $.ajax({
            url: ctx + "/recmd/genSmsVerifyCode",
            type: "post",
            data: {
                "recmdPhone": phone
            },
            dataType: "json",
            success: function (data) {
                console.log(data);
                if (data.code != "0") {
                    toggleModal("sorry-modal", data.message);
                } else {
                    $(".login_button").removeAttr("disabled");
                }
            },
            error: function () {
                toggleModal("sorry-modal", data.message);
            }
        });
    }
    function isGenedQrcode(rcdConfId, phone,ts) {
        var recmdBusiParam = $("input[name='recmdBusiParam']").val();
        var url;
        var url1=ctx+"recmd/isGenedQrcodeWei";
        var url2 = ctx + "/recmd/isGenedQrcode";
        if(recmdBusiParam === null || recmdBusiParam == ""){//判断是否从微厅跳转
            url = url1;
        }else{
            url = url2;
        }

        $.ajax({
            url:url,
            type: "post",
            data: {
                "rcdConfId": rcdConfId,
                "phone": phone,
                "confId":recmdBusiParam
            },
            dataType: "json",
            success: function (data) {
                console.log(data);
                if (data.code === "0") {
                     var dirUrl;
                    $("input[name='recmdPhone']").val(data.message);
                    if(url==url1){
                        dirUrl=ctx + "/recmd/dir2MyQrcodeWei";
                    }else{
                        dirUrl=ctx + "/recmd/dir2MyQrcode";
                    }

                    $("#genRecmdCodeLink").attr("action", dirUrl);

                    $("#genRecmdCodeLink").submit();
                } else if(data.code==="1"){
                    $("input[name='recmdPhone']").val(data.message);
                    time(ts);
                    genSmsVerifyCode(data.message);
                }else if(data.code==="-2"){
                    daojishi();
                    $(".getcode").html("重新获取")
                    $("#banli").toggle();
                }else{
                    daojishi();
                    $(".getcode").html("重新获取")
                    toggleModal("sorry-modal","获取失败,请稍后再试!");
                }
            },
            error: function () {
                return false;
            }
        });
    }
    var $phone = "";
    var $rcdConfId = $("input[name='rcdConfId']").val();
    {
        $(".getcode").on("click", function () {
            if($(this).attr('disabled') == 'disabled'){
                return;
            }
            $(this).removeClass('daojishi');
            $(this).addClass('stopDaojishi');
            $phone = $("input[name='recmdPhone']").val();
            if ($phone.length == 0) {
                toggleModal("sorry-modal", "请输入电话号码或员工编号！");
                return;
            }
            isGenedQrcode($rcdConfId, $phone, this);
        });
        $(".detail").on("click", function () {
            $.ajax({
                url: ctx + '/recmd/getMoreDetail',
                type: "post",
                data: {},
                dataType: "json",
                success: function (data) {
                },
                error: function (data) {
                }
            });
        });
        $(".login_button").on("click", function () {
            /*插码*/
            try {
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n","WZXD_Login",
                            "WT.si_x","21"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n","WZXD_Login",true,
                                "WT.si_x","21",true);
                    }
                }

            }catch(e){
                console.log(e)
            }

            var recmdBusiParam = $("input[name='recmdBusiParam']").val();
            if ($phone.length == 0) {
                toggleModal("sorry-modal", "请获取验证码！");
                return;
            }
            if (!verify) {
                toggleModal("sorry-modal", "对不起，您的信息填写不正确！");
                return;
            }
            var $smsCode = $("input[name='smsCode']").val();
            if ($smsCode.length == 0) {
                toggleModal("sorry-modal", "请输入6位短信验证码！");
                return;
            }
            if ($smsCode.length != 6) {
                toggleModal("sorry-modal", "验证码不符合规格！");
                return;
            }
            if (recmdBusiParam == null || recmdBusiParam == '') {
                try {
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n","WZXD_Login",
                                "WT.si_x","99"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n","WZXD_Login",true,
                                    "WT.si_x","99",true);
                        }
                    }
                }catch(e){
                    console.log(e)
                }
                $("#genRecmdCodeLink").attr("action", ctx + "/recmd/genRecmdQrCodeWei");
            } else {
                try {
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n","WZXD_Login",
                                "WT.si_x","-99"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n","WZXD_Login",true,
                                    "WT.si_x","-99",true);
                        }
                    }
                }catch(e){
                    console.log(e)
                }
                $("#genRecmdCodeLink").attr("action", ctx + "/recmd/genRecmdQrCode");
            }


            $("#genRecmdCodeLink").submit();
        })
    };

    //点击号码输入框时,倒计时的样式显示
    function daojishi(){
        $(".getcode").addClass('daojishi');
        $(".getcode").removeClass('stopDaojishi');
    }

    try{
        $.cookie("WT.ac", getQueryString("WT.ac"));
    }catch (e){
        console.log(e);
    }

</script>
</html>