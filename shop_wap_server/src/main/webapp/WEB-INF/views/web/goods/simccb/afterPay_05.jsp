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
    <meta name="WT.si_n" content="建行专享号卡">
    <meta name="WT.si_x" content="支付返回页面">

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
                </div>
            </div>
        </div>
    </form>
    <!--支付方式 end-->
    <div class="gobal-btn">
        <a class="btn btn-blue w50" data-toggle="modal" id="submitBtn">确认支付￥${orderSub.orderSubPayAmount/100}</a>
        <!--灰色按钮 class btn-blue替换为 btn-gray-->
    </div>
    <!--表单 end-->
</div>
<!--弹框三 end-->
<input type="hidden" id="orderSubId" value="${orderSub.orderSubId}">


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
            <div class="modal-footer"><a class="btn btn-blue w50" data-dismiss="modal">查询中...</a></div>
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
            <div class="modal-footer"><a class="btn btn-blue w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<!--弹框三 end-->

<div class="modal fade gobal-modal" data-backdrop="static" id="myModal02" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="t-c">恭喜您！<br>活动已经成功办理</p>
            </div>
            <div class="modal-footer"><a class="btn btn-blue w50">结束</a></div>
        </div>
    </div>
</div>
<!--弹框 end-->


<!--弹框三 start-->
<div class="modal fade gobal-modal" data-backdrop="static" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="load-con">
                    <p class="pt10"></p>
                </div>
            </div>
            <div class="modal-footer"><a class="btn btn-blue w50" data-dismiss="modal">结束</a></div>
        </div>
    </div>
</div>
<!--弹框三 end-->

</body>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
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
                    toggleModal('myModal',"恭喜您!活动办理成功!",data.data.label,data.data.destUrl);
                }else if(data.code == '-2'){
                    //查询支付中心支付失败,重新支付
                    var destUrl="${ctx}ccbank/toPay?orderSubNo="+data.data.orderSubNo;
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
