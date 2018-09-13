<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/5/31
  Time: 11:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <title>建行客户移动专属套餐</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <meta name="format-detection" content="telephone=no, email=no" />
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-6.0.0.min.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/wap.ccb.sim.css" />

    <!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/simbank/comm.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>

    <%--插码相关--%>
    <meta name="WT.si_n" content="浦发专享号卡">
    <meta name="WT.si_x" content="支付">

</head>

<body>
<div class="top-aifix">
    <div class="header">
        <a class="return-con" href="javascript:void(0)" onclick="window.history.back()"></a>
        <h4>建行客户移动专属套餐</h4>
    </div>
</div>
<div class="container">
    <div class="banner"><img src="${ctxStatic}/images/simccb/banner.png" /></div>
    <!--流程图 start-->
    <div class="tag-box"><img src="${ctxStatic}/images/simccb/tag03.png" /></div>
    <!--流程图 end-->
    <form id="choosePlatform" action="payOrder" method="post">
        <input type="hidden" name="radiocode">
        <input type="hidden" name="orderNos" id="orderNos" value="${orderSub.orderSubNo}">
        <!--表单 start-->
        <div class="form-box">
            <div class="payment-info">
                <div class="payment-con">
                    <p>您已成功选择${deposit.planName}套餐，补贴${deposit.planSubsidy}元活动</p>
                    <!--支付剩余时间 stat-->
                    <div class="pt30">
                        <p>支付剩余时间</p>
                        <p class="pt10">
                            <span id="minute">${minute}</span>
                            <span>:</span>
                            <span id="second">${second}</span>
                        </p>
                    </div>
                    <!--支付剩余时间 end-->
                </div>
                <!--支付金额 start-->
                <div class="payment-amount">支付金额<span class="ml20 font-gray">￥${orderSub.orderSubPayAmount/100}</span>
                </div>
                <!--支付金额 end-->
            </div>
        </div>
    </form>
    <!--表单 end-->
    <!--支付方式 start-->
    <div>
        <div class="form-box mt20">
            <label class="choose-list" for="checkbox1">
                <div class="choose-pic"><img src="${ctxStatic}/images/spdbsim/wx_icon.png"/></div>
                <div class="choose-txt">
                    <p>微信支付</p>
                    <p class="f20 font-gray">推荐安装微信5.0及以上版本使用</p>
                </div>
                <div class="choose-box">
                    <input type="checkbox" id="checkbox1" class="choose-checkbox" value="2"/>
                </div>
            </label>
        </div>
        <%--和包支付--%>
        <div class="form-box mt20">
            <label class="choose-list" for="checkbox2">
                <div class="choose-pic"><img src="${ctxStatic}/images/beauty-sim/hebao.png"/></div>
                <div class="choose-txt">
                    <p>和包支付</p>
                    <p class="f20 font-gray">移动自有快捷支付</p>
                </div>
                <div class="choose-box">
                    <input type="checkbox" id="checkbox2" value="4" class="choose-checkbox"/>
                </div>
            </label>
        </div>

        <div class="form-box mt20">
            <label class="choose-list" for="checkbox3">
                <div class="choose-pic"><img src="${ctxStatic}/images/spdbsim/icon-zfb.png"/></div>
                <div class="choose-txt">
                    <p>支付宝支付</p>
                    <p class="f20 font-gray">支付宝支付</p>
                </div>
                <div class="choose-box">
                    <input type="checkbox" id="checkbox3" class="choose-checkbox" value="1"/>
                </div>
            </label>
        </div>

        <div class="form-box mt20">
            <label class="choose-list" for="checkbox4">
                <div class="choose-pic"><img src="${ctxStatic}/images/spdbsim/i_unionpay.png"/></div>
                <div class="choose-txt">
                    <p>银联支付</p>
                    <p class="f20 font-gray">银联支付</p>
                </div>
                <div class="choose-box">
                    <input type="checkbox" id="checkbox4" class="choose-checkbox" value="3"/>
                </div>
            </label>
        </div>
    </div>
    <!--支付方式 end-->
    <div class="gobal-btn">
        <a class="btn btn-blue w50" data-toggle="modal" id="submitBtn">确认支付￥${orderSub.orderSubPayAmount/100}</a>
        <!--灰色按钮 class btn-blue替换为 btn-gray-->
    </div>
</div>
<script type="text/javascript">
    var minute = "${minute}";
    var second = "${second}";
    var canPay = true;
    var timeout =  setInterval(function() {
        if(minute < 0){
            //假如从第一个页面一进来就是小于10的分钟数，要等一分钟才能在前面加上0
            minute = "0" + minute;
        }
        if(second <= 0){
            second = "0" + second;
        }
        if (second == 00 && minute == 00) {
            clearInterval(timeout);
            canPay = false;
            $("#minute").text(minute);
            $("#second").text("0" + second);
            return;
        }; //当分钟和秒钟都为00时，重新给值
        if (second == 00) {
            second = 59;
            minute--;
            if (minute < 10) minute = "0" + minute;
        }; //当秒钟为00时，秒数重新给值
        second--;
        if (second < 10) second = "0" + second;
        $("#minute").text(minute);
        $("#second").text(second);
    }, 1000);
    $(function () {
        if (minute == "00" && second == "00") {
            clearInterval(timeout);
            canPay = false;
        }
    });
</script>
<!--弹框 start-->
<div class="modal fade gobal-modal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">活动办理失败，<br>您已参加其他营销活动，<br>具体请咨询10086<br>支付金额将在1到15个工作日<br>退回原支付账户</p>
            </div>
            <div class="modal-footer"><a class="btn btn-blue w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<div class="modal fade gobal-modal" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">恭喜您！<br>活动已经成功办理</p>
            </div>
            <div class="modal-footer"><a class="btn btn-blue w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<!--弹框 end-->
<%--支付信息模板注入--%>
<div id="subFormDivId">
    <script type="text/html" id="subFormTplId" >
        <form id="formid" method="post" action="{{requestPayUrl}}">
            <input type="hidden" name="callbackUrl" value="{{callbackUrl}}"/>
            <input type="hidden" name="notifyUrl" value="{{notifyUrl}}"/>
            <input type="hidden" name="merchantId" value="{{merchantId}}"/>
            <input type="hidden" name="type" value="{{type}}"/>
            <input type="hidden" name="version" value="{{version}}"/>
            <input type="hidden" name="channelId" value="{{channelId}}"/>
            <input type="hidden" name="payType" value="{{payType}}"/>
            <input type="hidden" name="period" value="{{period}}"/>
            <input type="hidden" name="periodUnit" value="{{periodUnit}}"/>
            <input type="hidden" name="characterSet" value="{{characterSet}}"/>
            <input type="hidden" name="currency" value="{{currency}}"/>
            <input type="hidden" name="amount" value="{{amount}}"/>
            <input type="hidden" name="orderDate" value="{{orderDate}}"/>
            <input type="hidden" name="merchantOrderId" value="{{merchantOrderId}}"/>
            <input type="hidden" name="OrderNo" value="{{OrderNo}}"/>
            <input type="hidden" name="merAcDate" value="{{merAcDate}}"/>
            <input type="hidden" name="productDesc" value="{{productDesc}}"/>
            <input type="hidden" name="productId" value="{{productId}}"/>
            <input type="hidden" name="productName" value="{{productName}}"/>
            <input type="hidden" name="reserved1" value="{{reserved1}}"/>
            <input type="hidden" name="reserved2" value="{{reserved2}}"/>
            <input type="hidden" name="payOrg" value="{{payOrg}}"/>
            <input type="hidden" name="authorizeMode" value="{{authorizeMode}}"/>
            <input type="hidden" name="mobile" value="{{mobile}}"/>
            <input type="hidden" name="Gift" value="{{Gift}}"/>
            <input type="hidden" name="MerActivityID" value="{{MerActivityID}}"/>
            <input type="hidden" name="PaymentLimit" value="{{PaymentLimit}}"/>
            <input type="hidden" name="ProductURL" value="{{ProductURL}}"/>
            <input type="hidden" name="IDType" value="{{IDType}}"/>
            <input type="hidden" name="IvrMobile" value="{{IvrMobile}}"/>
            <input type="hidden" name="ProductType" value="{{ProductType}}"/>
            <input type="hidden" name="hmac" value="{{hmac}}"/>
        </form>
    </script>
</div>
</body>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript">
    $("#submitBtn").click(function () {
        if (!canPay) {
            toggleModal("myModal","支付超时,优惠活动未办理成功,请联系10086返销套餐!");
            return;
        }
        var radiocode = $("input[type='checkbox']:checked").attr('value');
        if(radiocode == undefined){
            toggleModal("myModal","请先选择支付方式!");
            return;
        }
        $("input[name='radiocode']").val(radiocode);
        //$("#choosePlatform").submit();
        $.ajax({
            url:${ctx}+'/bankCommon/payOrder',
            dataType:'json',
            type:'post',
            data:{orderNos:$("#orderNos").val(),radiocode:radiocode,bankType:"CCB"},
            success:function(data){
                if(data.code == '0'){
                    //注入订单信息发起请求
                    $("#subFormDivId").html(template("subFormTplId",data.data));
                    $("#formid").submit();
                }else if(data.code == '-1' || data.code == '-2'){
                    //-1选择平台错误 -2获取支付流水失败
                    toggleModal('myModal',data.message);
                }else{
                    toggleModal('myModal',data.message);
                }
            },
            error:function(){
                toggleModal('myModal','系统异常');
            }
        });
        try{
            dcsPageTrack("WT.si_n", "浦发银行活动办理", 0, "WT.si_x", "订单支付", 0);//插码使用
        }catch (e) {
            console.log("插码失败")
        }
    });

    $(".choose-list").click(function () {
        $(this).parent().siblings().find("input").removeAttr("checked");
    });
    $(function () {
        if(${fail}){
            $("#myModal01").modal('toggle');
        }else{
            //支付成功,查询是否办理成功
            $("#myModal04").modal('toggle');
        }
    })


</script>
</html>
