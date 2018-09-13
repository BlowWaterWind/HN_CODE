<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/4/3
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="UTF-8">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<head>
    <title>浦发客户移动专属套餐</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <meta name="format-detection" content="telephone=no, email=no"/>
    <%--插码相关--%>
    <meta name="WT.si_n" content="浦发专享号卡">
    <meta name="WT.si_x" content="浦发专享号卡">

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

</head>
<body>
<div class="top-aifix">
    <div class="header">
        <a class="return-con" href="javascript:history.back(-1);"></a>
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
                <%--<div class="pt30" id="time">--%>
                    <%--<p>支付剩余时间</p>--%>
                    <%--<p class="pt10">--%>
                        <%--<span id="minute">${minute}</span>--%>
                        <%--<span>:</span>--%>
                        <%--<span id="second">${second}</span>--%>
                    <%--</p>--%>
                <%--</div>--%>
                <p id="errorInfo" style="display: none"></p>
                <!--支付剩余时间 end-->
            </div>
            <!--支付说明 start-->
            <div class="payment-amount" id="successPrompt" style="display: none">您选择的套餐活动已经办理成功，<br>中国移动和您在一起!</div>
            <div class="payment-amount" id="failPrompt" style="display: none;">具体请咨询10086。<br>支付金额将在1到15个工作日退回原账户。</div>
            <!--支付说明 end-->
        </div>
    </div>

</div>
<script type="text/javascript">
</script>
<!--弹框一 start-->
<div class="modal fade gobal-modal" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">您保证冻结失败<br>请重试</p>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">结束</a></div>
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
                <p class="t-c">活动办理失败<br>请24小时后再试</p>
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
                <div class="load-con">
                    <i class="loading"></i>
                    <p class="pt10">正在查询支付结果</p>
                </div>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">结束</a></div>
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
                    <i class="loading"></i>
                    <p class="pt10">正在活动办理结果</p>
                </div>
            </div>
            <div class="modal-footer"><a class="btn btn-rose w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<!--弹框三 end-->

<input type="hidden" id="orderSubId" value="${orderSub.orderSubId}">


</body>
<script type="text/javascript">
    $(function () {
        if(${fail}){
            //支付失败
            $("#myModal01").modal('toggle');
        }else{
            //支付成功,查询是否办理成功
            $("#myModal03").modal('toggle');
            checkStatus();
        }
    });

    function checkStatus(){
        $.ajax({
            url:${ctx}+'/bankCommon/currentTask',
            data:{orderSubId:$("#orderSubId").val()},
            dataType:"json",
            type:"post",
            success:function (data) {
                $("#myModal03").modal('hide');
                $("#myModal04").modal('hide');
                if(data.code == '0'){
                    //异步回调失败,查询支付成功,正在办理业务
                    $("#errorInfo").hide();
                    $("#successPrompt").show();
                }else if(data.code == '-2'){
                    //查询支付中心支付失败,重新支付
                    var destUrl="${ctx}spdbank/toPay?orderSubNo="+data.data.orderSubNo;
                    toggleModal('myModal',data.message,'重新支付',destUrl);
                }else if(data.code == '-1'){
                    //支付回调变慢,查询支付结果成功,等待支付任务完成
                    $("#myModal04").modal('toggle');
                    checkStatus();
                } else{
                    //系统异常或者是重复执行任务
                    toggleModal('myModal',data.message);
                }
            }
        });
    }

</script>
</html>
