<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/4/3
  Time: 09:56
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<head>
    <title>浦发客户移动专属套餐</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <meta name="format-detection" content="telephone=no, email=no" />
    <%--插码相关--%>
    <meta name="WT.si_n" content="浦发专享号卡">
    <meta name="WT.si_x" content="浦发专享号卡">
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-6.0.0.min.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/wap.spdb.sim.css" />

    <meta name="WT.si_n" content="浦发活动办理" >
    <meta name="WT.si_x" content="号码校验" >


    <!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/simbank/comm.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>


</head>
<body>

<div class="top-aifix">
    <div class="header">
        <a class="return-con"  href="javascript:history.back(-1);"></a>
        <h4>浦发客户移动专属套餐</h4>
    </div>
</div>
<div class="container">
    <div class="banner"><img src="${ctxStatic}/images/spdbsim/banner0720.png" /></div>
    <!--表单 start-->
    <form method="post" action="createPreOrder" id="createOrder">
    <div class="form-box mt-12">
        <ul class="form-list">
            <li>
                <span>手机号</span>
                <div class="form-rt"><i class="clear-index" style="display: none"></i>
                <input type="tel" id="contactPhoneShow" onkeyup="onlyNum1(this)"  class="form-input" placeholder="请输入手机号码" /></div>
                <input type="hidden" id="contactPhone" name="serialNumber">
            </li>
            <li>
                <span>身份证号</span>
                <div class="form-rt"> <i class="clear-index" style="display: none"></i>
                <input id="cardIdShow" onkeyup="formatCardId(this)" class="form-input" placeholder="请输入身份证号码" /></div>
                <input type="hidden" id="cardId" name="psptId">
            </li>
            <li>
                <span>姓名</span>
                <div class="form-rt"> <i class="clear-index" style="display: none"></i>
                <input type="text" id="userName" name="custName" class="form-input" onkeyup="nameKey(this)" placeholder="请输入姓名" /></div>

            </li>
            <li>
                <span>推荐人工号</span>
                <div class="form-rt"> <i class="clear-index" style="display: none"></i>
                <input type="text" name="managerId" id="managerId" class="form-input" onkeyup="staffKey(this)" placeholder="请输入推荐人工号" /></div>
            </li>
        </ul>
    </div>
        <input type="hidden" name="bankType" value="SPDB">
    </form>
    <!--表单 end-->
    <!--协议 start-->
    <div class="agreement-box">
        <input type="checkbox" id="ckbox" class="checkbox" />
        <label for="ckbox">本人同意<a class="font-blue" data-toggle="modal">《个人信息授权书》</a></label>
    </div>
    <!--协议 end-->
    <div class="gobal-btn">
        <a class="btn btn-rose w50" data-toggle="modal" id="submitBtn">资格查验</a>
        <!--灰色按钮 class btn-rose替换为 btn-gray-->
    </div>
</div>
<!--弹框 start-->
<div class="modal fade gobal-modal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c" id="content">系统异常,请稍后再试!<br></p>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<!--弹框 end-->

<!--协议弹框 start -->
<div class="modal fade gobal-modal" id="myModal02" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">个人信息授权书</div>
            <div class="modal-body">
                <p>个人信息授权书个人信息授权书</p>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">确定</a></div>
        </div>
    </div>
</div>
<!--协议弹框 end -->
<script type="text/javascript">
    var ctx = ${ctx};
    var phoneVerify = false;

    function nameKey(obj){
        if($("#userName").val() != ""){
            $(obj).prev().show();
        }else{
            $(obj).prev().hide();
        }
        $("#userName").val($.trim($("#userName").val()));

    }

    function staffKey(obj) {
        if($("#managerId").val() != ""){
            $(obj).prev().show();
        }else{
            $(obj).prev().hide();
        }
        $("#managerId").val($.trim($("#managerId").val()));
    }

    function onlyNum1(a) {
        var value = getNum(a.value);
        if( value != ""){
            $(a).prev().show();
        }else{
            $(a).prev().hide();
        }
        onlyNum2(a);
    }

    function onlyNum2(a) {
        var num = getNum(a.value), len = num.length;
        phoneVerify = false;
        var se, vlen;
        if (a.setSelectionRange) {
            se = a.selectionEnd;
            vlen = se === a.value.length;
        }
        if (12 > len) {
            a.value = formatMobile(num);
            if (num.length == 11) {
                if (checkPhoneNum(num) == false) {
                   toggleModal('myModal','请输入正确的手机号码');
                    phoneVerify = false;
                }else{
                    a.blur();
                }
                return;
            }
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

    function checkPhoneNum(phoneId) {
        var regPhone = /^1\d{10}/;
        if (regPhone.test(phoneId)) {
            phoneVerify = true;
        }
        return phoneVerify;
    }

    function formatMobile(a) {
        var num = (a + "").split(""), str = "";
        num.forEach(function (a, num) {
            (3 === num || 7 === num) && (str += " ");
            str += a;
        });
        return str;
    }

    //
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
        var cardId = (value + "").split(""), str = "";
        cardId.forEach(function (value, num) {
            (6 === num || 14 === num) && (str += " ");
            str += value;
        });
        a.value = str;
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

    $("#submitBtn").click(function () {
        var regName = $("#userName").val();
        var reg = /\s/;
        if(regName == ""){
            toggleModal('myModal','请输入姓名');
            return;
        }
        if (reg.test(regName)) {
            toggleModal('myModal','您输入的姓名含有空格<br>请重新输入');
            return;
        }
        if(!$("#ckbox").prop("checked")){
            toggleModal('myModal','请先同意<br>个人信息授权书');
            return;
        }
        var cardId = getNum($("#cardIdShow").val());
        var contactPhone = getNum($("#contactPhoneShow").val());
        $("#cardId").val(cardId);
        $("#contactPhone").val(contactPhone);
        $("#submitBtn").attr("otitle","﻿浦发实名提交跳转_"+cardId);
        var params = $("#createOrder").serializeObject();
        $.ajax({
            url: ${ctx}+'bankCommon/checkNum',
            data: params,
            dataType: "json",
            type: "post",
            success: function (data1) {
                //纯异网客户
                if (data1.code == '0' || data1.code == '2') {
                    //异网客户 message= 2 同网客户 message = 1
                    window.location.href = "${ctx}spdbank/toChooseOffer?type="+data1.data.type+"&orderSubNo="+data1.data.orderSubNo+"&phone="+data1.data.phone;
                    //名下已经有湖南移动的号码
                }else if(data1.code == '3'){
                    //去确认套餐
                    var destUrl="${ctx}spdbank/toConfirmOffer?planId="+ data1.data.planId+
                            "&planName="+encodeURIComponent(encodeURIComponent(data1.data.planName))+
                            "&planSubsidy="+data1.data.planSubsidy+
                            "&orderSubNo="+data1.data.orderSubNo;
                    toggleModal('myModal',data1.message,'前往办理',destUrl);
                }else if(data1.code == '4'){
                    //去支付
                    var destUrl="${ctx}spdbank/toPay?orderSubNo="+data1.data.orderSubNo;
                    toggleModal('myModal',data1.message,'前往办理',destUrl);
                }else if(data1.code == '5'){
                    //去冻结
                    var destUrl="${ctx}spdbank/toFreeze?orderSubNo="+data1.data.orderSubNo;
                    toggleModal('myModal',data1.message,'前往办理',destUrl);
                }else if(data1.code == '-2'){
                    //非湖南移动号码,通过浦发的推荐链接办理
                    toggleModal('myModal',data1.message,'前往办理',data1.data.destUrl);
                }else if(data1.code=='-3'){
                    //您名下已经有湖南移动的号码,但是输入的不是它的移动号码
                    toggleModal('myModal',data1.message,'结束');
                } else if(data1.code=='6'){
                    //该号码已经办理过次业务
                    toggleModal('myModal',data1.message);
                }else if(data1.code=='8'){
                    //去解冻
                    var destUrl="${ctx}spdbank/afterFreeze?orderSubId="+data1.data.orderSubId;
                    toggleModal('myModal',data1.message,'前往解冻',destUrl);
                }else if(data1.code=='-4'){
                    //接口调用出错
                    toggleModal('myModal',data1.message,'结束');
                }else if(data1.code == '9'){
                    toggleModal('myModal',data1.message,'结束');
                }
                //流程页面嵌码 WT.si_n(流程名称) WT.si_x(步骤名称)
                try{
                    dcsPageTrack("WT.si_n", "浦发优惠套餐办理", 0, "WT.si_x", "号码检查", 0);//插码使用
                }catch (e) {
                    console.log("插码失败");
                }
            },
            error:function(){
                return;
            }
        });

    });

    $(".clear-index").click(function(){
        $(this).next().val("");//清空表单内容
        $(this).hide();
    });



</script>


</body>
</html>
