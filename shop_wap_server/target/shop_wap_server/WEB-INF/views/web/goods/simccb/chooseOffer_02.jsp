<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/5/31
  Time: 10:50
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

    <meta name="WT.si_n" content="建行活动办理" >
    <meta name="WT.si_x" content="选择套餐" >
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
    <div class="tag-box"><img src="${ctxStatic}/images/simccb/tag01.png" /></div>
    <!--流程图 end-->
    <form id="chooseOffer" method="post" action="toConfirmOffer">
        <input type="hidden" name="planId">
        <input type="hidden" name="planName">
        <input type="hidden" name="planSubsidy">
        <input type="hidden" name="orderSubNo" value="${orderSubNo}">
        <input type="hidden" name="type" value="${type}">
        <input type="hidden" name="id">
    </form>
    <!--表单 start-->
    <div class="form-box">
        <!--表单 start-->
        <div class="form-box">
            <ul class="form-list form-radio-list">
                <c:forEach items="${plans}" var="plan" varStatus="status">
                    <li>
                        <div class="radio">
                            <input id="radio-${status.index}" name="radio" type="radio" />
                            <label for="radio-${status.index}" class="radio-label" planName="${plan.planName}" subsidy="${plan.subsidyPrice}" id="${plan.id}">${plan.planName}，补贴${plan.subsidyPrice}元活动</label>
                        </div>
                        <input type="hidden" name="planId" value="${plan.planId}">
                    </li>
                </c:forEach>
            </ul>
        </div>
        <!--表单 end-->
    </div>
    <!--表单 end-->
    <div class="gobal-btn">
        <a class="btn btn-blue w50 " id="submitBtn">下一步</a>
        <!--灰色按钮 class btn-rose替换为 btn-gray-->
    </div>
</div>
</body>
<script type="text/javascript">
    $("#submitBtn").click(function(){
        var planId = $("input[name='radio']:checked").parent().siblings('input').val();
        var planName =  $("input[name='radio']:checked").siblings('label').attr('planName');
        var subsidy =  $("input[name='radio']:checked").siblings('label').attr('subsidy');
        var id =  $("input[name='radio']:checked").siblings('label').attr('id');
        $("input[name='planId']").val(planId);
        $("input[name='planName']").val(planName);
        $("input[name='planSubsidy']").val(subsidy);
        $("input[name='id']").val(id);
        try{
            dcsPageTrack("WT.si_n", "建设银行活动办理", 0, "WT.si_x", "套餐选择", 0);//插码使用
        }catch (e) {
            console.log("插码失败");
        }
        $("#chooseOffer").submit();

    });
</script>
</html>