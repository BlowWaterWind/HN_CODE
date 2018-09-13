<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <meta name="WT.si_n" content="宽带提速" />
    <meta name="WT.si_x" content="无宽带帐号" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script>
        var baseProject = "${ctx}" ;
    </script>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>账户选择</h1>
        <%--<a href="javascript:;" class="icon-right icon-add"></a>--%>
    </div>
</div>
<!--当前无绑定账户 start-->
<div class="container container-con">
    <div class="card4g-popup kd-content">
        <p>尊敬的用户，<br/>您当前账号未办理宽带业务，请先办理宽带业务</p>
    </div>
    <div class="zp-btn kd-box"><a class="btn btn-blue btn-block" href="${ctx}/broadband/broadbandHome">返回</a></div>
</div>
<!--当前无绑定账户 end-->

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript"  src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>
