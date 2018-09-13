<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/4/3
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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
    <meta name="WT.si_x" content="浦发专享号卡">

</head>
<body>
<div class="top-aifix">
    <div class="header">
        <a class="return-con"  href="javascript:history.back(-1);"></a>
        <h4>浦发客户移动专属套餐</h4>
    </div>
</div>
<div class="container">
    <div class="banner"><img src="${ctxStatic}/images/spdbsim/banner0720.png"/></div>
    <!--流程图 start-->
    <div class="tag-box"><img src="${ctxStatic}/images/spdbsim/tag04.png"/></div>
    <!--流程图 end-->
    <!--表单 start-->
    <div class="form-box" id="daojishi">
        <div class="payment-info">
            <div class="payment-con">
                <p>您已成功选择${deposit.planName}，补贴${deposit.planSubsidy}元活动</p>
                <!--支付剩余时间 stat-->
                <div class="pt30">
                    <p>冻结剩余时间</p>
                    <p class="pt10">
                        <span id="minute">${minute}</span>
                        <span>:</span>
                        <span id="second">${second}</span>
                    </p>
                </div>
                <!--支付剩余时间 end-->
            </div>
            <!--支付说明 start-->
            <div class="payment-amount">您选择的套餐活动即将办理成功，<br>请在一小时之内办理保证金冻结。</div>
            <!--支付说明 end-->
        </div>
    </div>
    <form id="freeze" method="post" action="freezeCash">
        <input type="hidden" id="orderSubId" value="${orderSub.orderSubId}">
        <input type="hidden" id="orderSubNo" value="${orderSub.orderSubNo}">
    </form>
    <!--表单 end-->
    <div class="gobal-btn">
        <a class="btn btn-rose w50" data-toggle="modal" id="commitBtn">保证金冻结</a>
        <!--灰色按钮 class btn-rose替换为 btn-gray-->
    </div>
</div>
<script type="text/javascript">
    //保证金冻结保持一小时
    var minute = ${minute};
    var second = ${second};
    var canFreeze = true;
    var interval = setInterval(function () {
        second--;
        if(minute < 0){
            //假如从第一个页面一进来就是小于10的分钟数，要等一分钟才能在前面加上0
            minute = "0" + minute;
        }
        if(second < 0){
            second = "0" + second;
        }
        if (second == 00 && minute == 00) {
            clearInterval(interval);
            canFreeze = false;
            $("#minute").text(minute);
            $("#second").text("0"+second);
            return;
        }
        //当分钟和秒钟都为00时，重新给值
        if (second == 00) {
            second = 59;
            minute--;
            if (minute < 10) minute = "0" + minute;
        }
        //当秒钟为00时，秒数重新给值
        if (second < 10) second = "0" + second;
        $("#minute").text(minute);
        $("#second").text(second);
    }, 1000);
</script>
<!--弹框一 start-->
<div class="modal fade gobal-modal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">您保证冻结失败<br>请重试</p>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">返回</a></div>
        </div>
    </div>
</div>
<!--弹框一 end-->
<!--弹框二 start-->
<div class="modal fade gobal-modal" id="myModal02" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">冻结超时,活动办理失败!<br>请联系10086返销套餐!</p>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">返回</a></div>
        </div>
    </div>
</div>
<!--弹框二 end-->
<!--弹框三 start-->
<div class="modal fade gobal-modal" id="myModal03" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">恭喜您<br>您的保证金冻结成功</p>
            </div>
            <div class="modal-footer"><a href="toPay?orderSubNo=${orderSub.orderSubNo}" class="btn btn-rose w50">去支付</a></div>
        </div>
    </div>
</div>
<!--弹框三 end-->

<!--弹框三 start-->
<div class="modal fade gobal-modal" id="myModal04" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="load-con">
                    <i class="loading" id="loading"></i>
                    <p class="pt10" id="result">正在查询冻结结果</p>
                </div>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<!--弹框三 end-->

<!--弹框三 start-->
<div class="modal fade gobal-modal" id="myModal05" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="load-con">
                    <i class="loading" style="display: none"></i>
                    <p class="pt10">活动办理失败,您可以申请解冻保证金!</p>
                </div>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" onclick="unFreeze()">解冻保证金</a></div>
        </div>
    </div>
</div>
<!--弹框三 end-->

</body>
<script type="text/javascript">
    var toChaxun = false;
    var toFreeze = true;
    $("#commitBtn").click(function(){
        if(!canFreeze){
           $("#myModal02").modal('toggle');
           return;
        }else if($(this).attr("disabled")=="disabled"){
            return;
        }else if(toFreeze){
            $.ajax({
               url:'freezeCash',
                type:"post",
                dataType:"json",
                data:{orderSubId:$("#orderSubId").val()},
                success:function(data){
                   if(data.code == '0'){
                       window.location.href = data.message;
                   }else{
                       toggleModal('myModal',data.message);
                   }
                },
                error:function() {
                    toggleModal('myModal','系统异常');
                }
            });
            dcsPageTrack("WT.si_n", "浦发银行活动办理", 0, "WT.si_x", "保证金冻结", 0);//插码使用
        }else{
            checkFreezeStatus();
        }
    });

    var depositId;
    $(function(){
       if(${retry}){
           //从01跳转过来发过一次请求,如果发过一次冻结请求,查询冻结结果
           checkStatus();
       }
    });
    function checkStatus(){
        if(${success}){
            //冻结成功
            $("#minute").text("00");
            $("#second").text("00");
            clearInterval(interval);
            $("#myModal03").modal('toggle');
        }else{
            //超时,任务结束
            if(${timeout}){
                canFreeze = false;
                clearInterval(interval);
                $("#minute").text("00");
                $("#second").text("00");
                $("#myModal02").modal('toggle');
                return;
            }else if(${unFreeze}){
                //解冻保证金
                clearInterval(interval);
                $("#myModal05 p").html('活动办理失败,失败原因:'+'${reason}'+'您可以申请解冻保证金!');
                $("#myModal05").modal('toggle');
            }else{
                //clearInterval(interval); 重新发起冻结请求
                minute = ${minute};
                second = ${second};
                $("#minute").text(minute);
                $("#second").text(second);
                depositId  = $("#orderSubId").val();
                checkFreezeStatus();
            }
        }
    }


    function checkFreezeStatus(){
        $("#myModal04").modal('show');
        $.ajax({
            url:'checkFreezeStatus',
            data:{depositId:depositId},
            type:"post",
            dataType:"json",
            success:function(data){
                $("#loading").hide();
                if(data.code == "0"){
                    //去支付
                    $("#commitBtn").attr("disabled",true);
                    $("#loading").hide();
                    $("#result").html(data.message);
                    clearInterval(interval);
                    $("myModal04 a").removeAttr('data-dismiss');
                    $("myModal04 a").html("去支付");
                    var dest = "${ctx}spdbank/toPay?orderSubNo="+data1.data.orderSubNo;
                    $("myModal04 a").attr('href', dest);
                    $("#myModal04").modal('show');
                }else if(data.code == '-1'){
                    toChaxun = false;
                    toFreeze = true;
                    //保证金冻结失败,重新发起冻结保证金
                    $("#result").html(data.message);
                    $("#commitBtn").html("冻结保证金")
                }else{
                    $("#myModal04").modal('hide');
                    toggleModal('myModal',data.message);
                }
            },
            error:function(){
                toggleModal('myModal','系统异常!');
            }

        });
    }

    function unFreeze() {
        $("#myModal05 i").show();
        $.ajax({
            url:'unFreeze',
            data:{orderSubNo:$("#orderSubNo").val()},
            dataType:"json",
            type:"post",
            success:function(data){
                $("#myModal05 i").hide();
                if(data.code == '0'){
                    //解冻成功
                    $("#myModal05 p").html(data.message);
                }else{
                    $("#myModal05 p").html(data.message);
                }
            }
        });
        try{
            dcsPageTrack("WT.si_n", "浦发银行活动办理", 0, "WT.si_x", "保证金解冻", 0);//插码使用
        }catch (e) {
            console.log("插码失败");
        }
    }
</script>
</html>
