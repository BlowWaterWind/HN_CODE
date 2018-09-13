<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/1/31
  Time: 16:01
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
    <META name="WT.si_n" content="">
    <META name="WT.si_s" content="">
    <META name="WT.si_x" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <META name="WT.ac" content="">
    <title>完善入网资料</title>
    <link rel="stylesheet" href="${ctxStatic}/css/wap.o2o.sim.css"/>

    <%--插码相关--%>
    <script src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>



</head>
<body>
<div class="container">
    <div class="navbar Grid -center -middle">
        <h3 class="navbar-title">STEP 2 实名登记</h3>

        <button class="navbar-return"></button>
    </div>
    <form method="post" action="createPreOrder" id="createOrder">
        <div class="base-cells mb-40">
            <div class="base-cell Grid -middle">
                <label class="Cell -4of12">姓名</label>
                <i class="clear-index" style="display: none"></i>
                <input type="text" id="name" name="userName" placeholder="请输入身份证件姓名"
                       class="base-cell-input Cell -fill" onkeyup="nameKey(this)"/>
            </div>
            <div class="base-cell Grid -middle">
                <label class="Cell -4of12">身份证</label>
                <i class="clear-index" style="display: none"></i>
                <input id="cardIdShow" onkeyup="formatCardId(this)" placeholder="请输入身份证号"
                       class="base-cell-input Cell -fill"/>
                <input type="hidden" id="cardId" name="cardId"/>
            </div>
            <div class="base-cell Grid -middle">
                <label class="Cell -4of12">联系电话</label>
                <i class="clear-index" style="display: none"></i>
                <input type="tel" id="contactPhoneShow" onkeyup="onlyNum1(this)" placeholder="请输入联系电话"
                       class="base-cell-input Cell -fill"/>
                <input type="hidden" id="contactPhone" name="contactPhone"/>
            </div>
        </div>
        <input type="hidden" name="confId" value="${confId}"/>
        <input type="hidden" name="planId" value="${planId}"/>
        <input type="hidden" name="reCmdCode" value="${reCmdCode}"/>
        <input type="hidden" name="chanId" value="${CHANID}"/>
        <input type="hidden" name="shopId" value="${shopId}"/>
    </form>
    <div class="Grid -center">
        <button id="submitBtn" class="btn-large btn-blue" otype="button" otitle="实名制预订单提交跳转_">提交</button>
    </div>
</div>

<script src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script src="${ctxStatic}/ap/lib/layer.js"></script>
<script src="${ctxStatic}/js/o2o/simOnline/public.js"></script>
<%--这个页面还是跟在线售卡不一样,在线售卡又要选号又要填地址，因此blur事件之后调用的一证五号的时间可以忽略，而这个填完身份证号码和姓名用户就想点立即提交--%>
<%--如果不返回，用户就觉得点不了。改正为只在提交的时候进行一次实名认证校验--%>


<script type="text/javascript">
    try{
        /*给页面WT.si_n 赋值*/
        var confId = $("input[name='confId']").val();
        var planId = $("input[name='planId']").val();
        getConfId2(confId,planId);
        var wtAc = $.cookie("WT.ac");
        if($.cookie("WT.ac")!=undefined) {
            document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
        }
    }catch (e){
        console.log(e);
    }

    var ctx = ${ctx};
    //增加对身份证号码和宽带密码的加密
    var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
        + "wxyz0123456789+/" + "=";
    function encode64(str) {
        return strEnc(str, keyStr);
    }

    $(function(){
        //从上个页面返回时如果有文字显示关闭
        if($("#name").val()!=""){
            $("#name").prev().show();
        }
        if($("#cardIdShow").val()!=""){
            $("#cardIdShow").prev().show();
            var psptId = getNum($.trim($("#cardIdShow").val()));
            if(psptId.length == 18){
                $("#cardIdShow").blur()
            }
        }
        if($("#contactPhoneShow").val()!=""){
            $("#contactPhoneShow").prev().show();
        }
        $("#submitBtn").attr("disabled",false);
        $("#submitBtn").html("提交");//
    });
    function nameKey(obj){
        if($("#name").val() != ""){
            $(obj).prev().show();
        }else{
            $(obj).prev().hide();
        }
        $("#name").val($.trim($("#name").val()));

    }

    $("#name").blur(function () {
        var regName = $("#name").val();
        var reg = /\s/;
        if (reg.test(regName)) {
            $("#name").val(regName.replace(/\s+/, ""));
            layer.open({
                shadeClose: false,
                content: '姓名中不能包含空格</br>请重新输入',
                btn: '我知道了'
            });
            return;
        }
    });


    function onlyNum1(a) {
        onlyNum2(a);
    }

        function onlyNum2(a) {
            var num = getNum(a.value), len = num.length;
            if(num != ""){
                $(a).prev().show();
            }else{
                $(a).prev().hide();
            }

            phoneVerify = false;
            var se, vlen;
            if (a.setSelectionRange) {
                se = a.selectionEnd;
                vlen = se === a.value.length;
            }
            if (12 > len) {
                a.value = formatMobile(num);
                /*if (num.length == 11) {
                    if (checkPhoneNum(num) == false) {
                        layer.open({
                            content: '请输入湖南省移动的号码</br>请重新输入',
                            btn: '我知道了'
                        });
                        phoneVerify = false;
                    }else{
                        a.blur();
                    }
                    return;
                }*/
            } else {
                a.value = formatMobile(num.substr(0, 11));
                a.blur();
            }
            se && setTimeout(function () {
                vlen && (se = a.value.length);
                a.setSelectionRange(se, se)
            }, 0)
        }

        function getNum(a) {
            var num = $.trim(a);
            num = num.replace(/[\s]/g, "");
            return num;
        }

        function formatMobile(a) {
            var num = (a + "").split(""), str = "";
            num.forEach(function (a, num) {
                (3 === num || 7 === num) && (str += " ");
                str += a;
            });
            return str;
        }

        function formatCardId(a) {
            var value = getNum(a.value);
            if( value != ""){
                $(a).prev().show();
            }else{
                $(a).prev().hide();
            }

            var se,vlen;
            if(a.setSelectionRange) {
                se = a.selectionEnd;
                vlen = se === a.value.length;
            }
            a.value = formatCardNo(value);
            if (getNum(a.value).length == 18) {
                a.blur();
                return;
            }
            //这段代码的作用是控制光标的位置不能在删除的时候跑到后面去
            se && setTimeout(function(){
                vlen && (se = a.value.length);
                a.setSelectionRange(se,se)
            },0)
        }

    function formatCardNo(a){
        var num = (a + "").split(""),str = "";
        num.forEach(function (a,num){
            (6 === num || 14 ===num) && (str +=" ");
            str += a;
        });
        return str;
    }

        $("#submitBtn").click(function () {
            var regName = $("#name").val();
            var reg = /\s/;
            if (regName == '') {
                layer.open({
                    shadeClose: false,
                    content: '请输入姓名',
                    btn: '我知道了'
                });
                return;
            }
            if (getNum($.trim($("#cardIdShow").val())) == "") {
                layer.open({
                    shadeClose: false,
                    content: '请输入身份证号码',
                    btn: '我知道了'
                });
                return;
            }
            if (reg.test(regName)) {
                layer.open({
                    shadeClose: false,
                    content: '姓名中不能包含空格</br>请重新输入',
                    btn: '我知道了'
                });
                return;
            }
            var cardId = encode64(getNum($("#cardIdShow").val()));
            var contactPhone = getNum($("#contactPhoneShow").val());
            $("#cardId").val(cardId);
            $("#contactPhone").val(contactPhone);
            $("#submitBtn").attr("otitle","﻿实名制预订单提交跳转_"+cardId);
            var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
            try {
                Webtrends.multiTrack({
                    argsa: [
                        "WT.si_n",wtc,
                        "WT.si_x","20"
                    ],
                    delayTime: 100
                })

            }catch(e){
                console.log(e)

            }


            var params = $("#createOrder").serializeObject();
            $("#submitBtn").attr('disabled', true);
            $("#submitBtn").html("正在提交中....");
            $.ajax({
                url: 'createPreOrder',
                data: params,
                dataType: "json",
                type: "post",
                success: function (data1) {
                    if (data1.code == 0) {
                        var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                        try {
                            if(window.Webtrends){
                                Webtrends.multiTrack({
                                    argsa: ["WT.si_n",wtc,
                                        "WT.si_x","99"],
                                    delayTime: 100
                                })
                            }else{
                                if(typeof(dcsPageTrack)=="function"){
                                    dcsPageTrack ("WT.si_n",wtc,true,
                                            "WT.si_x","99",true);
                                }
                            }

                        }catch(e){
                            console.log(e)

                        }
                        $("#submitBtn").html("提交");
                        $("#submitBtn").attr('disabled', false);

                        window.location.href = "${ctx}o2oSimPreBuy/toValidateSim";
                    } else {
                        var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                        try {
                            if(window.Webtrends){
                                Webtrends.multiTrack({
                                    argsa: ["WT.si_n",wtc,
                                        "WT.si_x","-99"],
                                    delayTime: 100
                                })
                            }else{
                                if(typeof(dcsPageTrack)=="function"){
                                    dcsPageTrack ("WT.si_n",wtc,true,
                                            "WT.si_x","-99",true);
                                }
                            }

                        }catch(e){
                            console.log(e)

                        }

                        var message = data1.message == undefined ? "生成订单失败" : data1.message;
                        message += '</br>请重新输入';
                        layer.open({
                            shadeClose: false,
                            content: message,
                            btn: '我知道了'
                        });
                        $("#submitBtn").html("提交");
                        $("#submitBtn").attr('disabled', false);
                        return;
                    }
                   /* try{
                        //dcsPageTrack("WT.si_n", "实名制验证_" + cardId + "输入", 0, "WT.si_x", "页面输入", 0);//插码使用
                    }catch (e) {
                        console.log("插码失败");
                    }*/
                },
                error:function(){
                    layer.open({
                        content: '系统异常',
                        btn: '我知道了'
                    });
                    //允许再次提交
                    $("#submitBtn").attr("disabled",false);
                    $("#submitBtn").html("提交");
                    return;
                }
            });

        });

    $(".navbar-return").click(function(){
        window.history.back();
    });

    $(".clear-index").click(function(){
        $(this).next().val("");//清空表单内容
        $(this).hide();
    });


</script>
</body>
</html>
