<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/1/31
  Time: 16:08
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
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">

    <title>选号开通</title>
    <link rel="stylesheet" href="${ctxStatic}/css/wap.o2o.sim.css?v=710"/>
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">

</head>
<style type="text/css">
    .font-big{
        font-size: 0.46875rem;
    }
</style>
<body>
<form id="createOrder" method="post">
<input type="hidden" id="orderPreId" name="orderPreId" value="${o2oPreOrder.orderPreId}"/>
<input id="chosePhone" type="hidden" name="orderDetailSim.phone"  value="${phone}"/>
<input type="hidden" id="contactPhone" name="contactPhone" value="${contactPhone}"/>
<input id="cityCode" type="hidden" name="orderDetailSim.cityCode" value="${o2oPreOrder.simEparchyCode}"/>
<input type="hidden" name="urlPage" value="haoduanka"/>
<input type="hidden" name="transactionId" value="${transactionId}"/>
<input type="hidden" name="confId" value="${o2oPreOrder.confId}"/>
</form>
<%--插码相关--%>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
<div class="container">
    <div class="navbar Grid -center -middle">
        <c:choose>
            <c:when test="${o2oPreOrder.planName!=null && o2oPreOrder.planName!=''}">
                <h3 class="navbar-title">STEP 4 选择号码</h3>
            </c:when>
            <c:otherwise>
                <h3 class="navbar-title">STEP 3 选择号码</h3>
            </c:otherwise>
        </c:choose>

        <%--<button class="navbar-return"></button>--%>
    </div>
    <div class="base-cells no-line">
        <c:if test="${o2oPreOrder.planName!=null && o2oPreOrder.planName!=''}">
            <div class="base-cell Grid -middle">
                <p>您已选择“<strong class="blue-txt">${o2oPreOrder.planName}</strong>”套餐</p>
            </div>
        </c:if>
        <div class="base-cell bg-gray Grid -middle">
            <p >归属地&nbsp;&nbsp;${provinceName}&nbsp;&nbsp;${eparchyName}
            <c:if test="${cityName != null}">
                &nbsp;&nbsp;${cityName}
            </c:if>
            </p>
        </div>
        <div class="base-cell Grid -middle">
            <label class="Cell -4of12">联系电话</label>
            <i class="clear-index" style="display: none"></i>
            <input type="tel" id="contactPhoneShow" onkeyup="onlyNum1(this)" placeholder="请输入联系电话"
                   class="base-cell-input Cell -fill" value="${o2oPreOrder.contactPhone}" <c:if test="${transactionId != null}">readonly</c:if>/>
        </div>
        <c:if test="${transactionId != null}">
            <div class="base-cell Grid -middle">
                <label class="Cell -4of12"><span>您已选择</span></label><div class="Cell -fill font-big"><span>${phone}</span></div><i class="arrow arrow-right"></i>
            </div>
        </c:if>
        <c:if test="${transactionId == null}">
            <div id="choseNumber" class="base-cell Grid -middle">
                <label class="Cell -4of12" id="chooseNum"><span id="numPrompt">选择号码</span></label><div class="Cell -fill"><span id="numChosed">请选择号码</span></div><i class="arrow arrow-right"></i>
            </div>
        </c:if>
        <div class="base-cell bg-gray Grid -middle" style="color:red;float: right">
            <button class="copyBtn"  id="copyPhone" data-clipboard-text="">复制号码</button>
        </div>
        <div class="base-cell bg-gray Grid -middle" style="color:red;">
            温馨提示：请复制您选择的号码，并在下一步的激活环节粘贴。
        </div>
        <c:if test="${transactionId == null}">
            <div class="base-cell Grid -middle">
                <label class="Cell -4of12"><span>身份验证</span></label><span type="text" class="Cell -fill toValidate">请准备个人身份证件,点击认证激活</span><i class="arrow arrow-right toValidate"></i>
            </div>
        </c:if>
        <c:if test="${transactionId != null}">
            <div class="base-cell Grid -middle">
                <label class="Cell -4of12" ><span>身份验证</span></label><span type="text" class="Cell -fill">您的身份校验成功</span><i class="arrow arrow-right"></i>
            </div>
        </c:if>
        <div class="base-cell bg-gray Grid -middle">
            <label class="label-checkbox"><input type="checkbox" onchange="changeStatus()" class="checkbox">我已阅读并同意</label><span id="propaganda" class="blue-txt" style="font-size: 0.40625rem">《客户入网服务协议》</span>
        </div>
    </div>
    <div class="Grid -center">
        <button id="submitBtn" class="btn-large btn-blue">提交</button>
    </div>
    <div class="Grid -center">
        <p class="tips">请保持联系号码畅通，我们可随时与您联系。</p>
    </div>
    <div class="layer-modal layer-modal-2">
        <div class="chosenum Grid -center">
            <div class="search-box Grid -middle">
                <input type="text" id="tailNum" value="" placeholder="请输入不少于3位数字"/>
                <button class="icon-search" onclick="fetchSimBindedNum()">搜索</button>
            </div>
            <ul class="num-list Grid">
            </ul>
        </div>
    </div>

    <%--展示服务协议--%>
    <div id="contentPropaganda" style="display: none">
        <div class="agree-text">
        ${serviceContract}
        </div>
    </div>
</div>
<script src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/clipboard.min.js?v=12"></script>
<script src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script src="${ctxStatic}/ap/lib/layer.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
<script src="${ctxStatic}/js/o2o/simOnline/public.js"></script>
<script type="text/javascript">
    var ctx = '${ctx}';
    $(function () {
        try{
            var confId = $("input[name='confId']").val();
            getConfId2(confId,"");
            /**
             * 获取cookie中的参数
             */
            var wtAc = $.cookie("WT.ac");
            if($.cookie("WT.ac")!=undefined) {
                document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
            }
        }catch (e){
            console.log(e);
        }
        initSimNum();
        var modal2 = $('.layer-modal-2').html();
        $('#choseNumber').bind('click', function () {
            layer.open({
                type: 1,
                content: modal2,
                btn: '换一批',
                yes: fetchSimBindedNum
            });
            fetchSimBindedNum();
        });
    });
    $("#propaganda").click(function(){
        var content = $("#contentPropaganda").html();
        layer.open({
            type: 4,
            content:content
        });
    });

    var phoneStatus = false;
    var agreeStatus = false;
    var transactionId = '${transactionId}';
    function initSimNum(){
        var cardId = $("#cardId").val();
        var simCardNo = $("#simCardNo").val();
        var cityCode = $("#cityCode").val();
        $(".num-list").empty();
        //getSimNum(cardId,simCardNo,cityCode,'',false);//初始化数据
        $("#submitBtn").attr("disabled",true);
    }
    function chooseNum(a) {
        var chooseNum = $(a).attr('mobile');
        var cityCode = $(a).attr('cityCode');
        $("#numPrompt").html("您已选择");
        $("#numChosed").html(chooseNum);
        $("#numChosed").addClass('font-big');
        $("#chosePhone").val(chooseNum);
        $("#cityCode").val(cityCode);
        $("#copyPhone").attr("data-clipboard-text",chooseNum);
        phoneStatus = true;
        if(phoneStatus && agreeStatus){
            $("#submitBtn").attr("disabled",false);
        }
        layer.closeAll();
    }

    function changeStatus(){
        agreeStatus = !agreeStatus;
        if(transactionId != ''){
            phoneStatus = true;
        }
        if(phoneStatus && agreeStatus){
            $("#submitBtn").attr("disabled",false);
        }else{
            $("#submitBtn").attr("disabled",true);
        }
    }

    fetchSimBindedNum = function fetchSimBindedNum() {
        $(".num-list").empty();
        var tailNum = $(".layui-m-layer #tailNum").val();
        if(tailNum.length != "" && tailNum.length < 3){
            $(".num-list").append('<div class="search-sorry"><p>抱歉</br>请输入不少于3位数字!</p></div>');
            return;
        }
        var loading = '<div class="m-load2" ><div class="line">' +
            '<div></div> <div></div><div></div><div></div><div></div><div></div>' +
            '</div><div class="circlebg"></div></div>';
        $(".num-list").html(loading);
        var cityCode = $("#cityCode").val();
        getSimNum(cityCode,tailNum,true);
    };

    function getSimNum(cityCode,tailNum,sysFlag){
        $.ajax({
            url: 'getQueryNumsH5Online',
            //url: 'getQueryBeautyNumsH5OnlineMock',
            data: {orderPreId:$("#orderPreId").val(), tailNum: tailNum},
            dataType: "json",
            type: "post",
            async:sysFlag,
            success: function (data1) {
                $(".num-list").empty();
                var data = data1.data;
                if (data1.code != '0') {
                    layer.closeAll();
                    var message = data1.message;
                    layer.open({
                        content:message
                    });
                    return;
                }else if((data1.code === '0' && data1.data == null)){
                    $(".num-list").append('<div class="search-sorry"><p>抱歉</br>暂无匹配号码</p></div>');
                    return;
                }
                var j = data1.data.length > 10 ? 10 : data1.data.length;
                var charCount = 0;
                if(tailNum != undefined) {
                    charCount = tailNum.length;
                }
                var html = "";
                for (var i = 0; i < j; i++) {
                    var length = data[i].serialNumber.length;
                    var prefix = length - charCount;
                    var suffixNum = data[i].serialNumber.substring(prefix, length);
                    if(charCount == 0){
                        //有输入参数查询
                        html += '<span class="Cell -6of12" onclick="chooseNum(this)" mobile="'+data[i].serialNumber+'" cityCode="'+data[i].eparchyCode+'">' +data[i].serialNumber.substring(0, prefix) + '<span>'+ suffixNum + '</span></span>';
                    }else{
                        if(suffixNum == tailNum){
                            html += '<span class="Cell -6of12" onclick="chooseNum(this)" mobile="'+data[i].serialNumber+'" cityCode="'+data[i].eparchyCode+'">' + data[i].serialNumber.substring(0, prefix) + '<span>' + suffixNum + '</span></span>';
                        }
                    }
                }
                if(html == ""){
                    $(".num-list").append('<div class="search-sorry"><p>抱歉</br>暂无匹配号码</p></div>');
                }else{
                    $(".num-list").html(html);
                }
            }
        });
    }


    //todo
    $(".toValidate").click(function () {
        if( $("#chosePhone").val() == ""){
            layer.open({
                shadeClose: false,
                content:'请先选择号码',
                btn: '我知道了'
            });
            return;
        }
        if($("#contactPhone").val() == ""){
            layer.open({
                shadeClose: false,
                content:'请输入联系号码',
                btn: '我知道了'
            });
            return;
        }


        var params = $("#createOrder").serializeObject();
        $.ajax({
            url:'payAppointment',
            data:params,
            dataType:"json",
            type:"post",
            success: function (data) {
                if (data.code === "0") {
                    window.location.href = data.data ;
                }else {
                    if (data == '页面过期，请刷新页面') {
                        layer.open({
                            shadeClose: false,
                            content: '页面过期，请刷新页面',
                            btn: '我知道了'
                        });
                        return;
                    } else {
                        message = data.messsage != undefined ? data.messsage : "身份认证提交失败";
                        layer.open({
                            shadeClose: false,
                            content: message,
                            btn: '我知道了'
                        });
                        return;
                    }
                }

            },
            error: function () {
                layer.open({
                    shadeClose: false,
                    content: '身份认证提交失败',
                    btn: '我知道了'
                });
                return;
            }
        })
    });

    $(".btn-large").click(function(){

        if(!$("input[type='checkbox']").is(':checked')){
            layer.open({
                content:'请先阅读服务协议'
            });
            return;
        }
        if(transactionId == ''){
            layer.open({
                content:'请先进行实名认证'
            });
            return;
        }
        var params = $("#createOrder").serializeObject();
        layer.open({
            type : 2,
            shadeClose : false
        });
        $.ajax({
            url:'createOrder',
            data:params,
            dataType:"json",
            type:"post",
            success:function(data){
                if(data.code == 0){
                    // layer.close();不关闭窗口跳转
                    window.location.href = "${ctx}simConfirm/orderSuccess?thirdSubId="+data.data.thirdSubId;
                    ajaxSuccess();
                }else{
                    var message = data.message;
                    layer.closeAll();
                    layer.open({
                        type:0,
                        content:message
                    });
                    ajaxFail();
                }
            },
            error:function(){
                layer.open({
                    content:'系统异常,请稍后再试!'
                });
                ajaxFail();
            }

        })
    });

    //复制
    var phone = $("#chosePhone").val();
    $("#copyPhone").attr("data-clipboard-text",phone);
    var btn = document.getElementById('copyPhone');
    var clipboard = new Clipboard(btn);
    clipboard.on('success', function (e) {
        layer.open({
            shadeClose: false,
            content: '复制成功',
            btn: '我知道了'
        });
    });
    clipboard.on('error', function (e) {
        console.log(e);
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
        $("#contactPhone").val(num);

        phoneVerify = false;
        var se, vlen;
        if (a.setSelectionRange) {
            se = a.selectionEnd;
            vlen = se === a.value.length;
        }
        if (12 > len) {
            // a.value = formatMobile(num);
        } else {
            // a.value = formatMobile(num.substr(0, 11));
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

</script>
</body>
</html>
