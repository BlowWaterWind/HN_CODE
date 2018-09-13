<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/6/7
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>下单成功</title>
    <%--插码相关--%>
    <meta name="WT.si_n" content="下单成功">
    <meta name="WT.si_x" content="下单成功">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />


    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css" />
    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet">

    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
<%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
</head>
<script type="text/javascript">
    var ctx = ${ctx};
</script>

<body >
<div class="container">
    <div class="common-navbar-wrap">
        <div class="common-navbar ">
            <div class="navbar-left">
                <a href="javascript:history.back();" class="arrow-left"><span>&lt;</span> Back</a>
            </div>
            <h3 class="navbar-title">订单结果</h3>


        </div>
    </div>

    <div class=" clearfix ">
        <div class="info-img"><img src="${ctxStatic}/images/simo2o/img-succss.png" alt=""></div>
        <p class="tc">号卡购买成功</p>
        <%--自取--%>
        <div class="mt160 tc">
            <a href="${ctx}myOrder/querySimOrderList?psptId=${detailSim.psptId}" class="btn btn-border btn-r">查看订单</a>
            <a href="http://wap.hn.10086.cn/shop/" class="btn ml40 btn-border btn-r">回到首页</a>
        </div>
    </div>
</div>
</body>
</html>
