<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KDWD" />
    <meta name="WT.si_x" content="20" />
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <%--<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>--%>


    <script>
        var baseProject = "${ctx}" ;
    </script>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>我的宽带</h1>
    </div>
</div>
<div class="container">
    <div class="broadband-con">
        <div class="broadband-txt">
            <p>尊敬的用户</p>
            <p class="pt10">当前账号未办理宽带业务，请先办理宽带业务</p>
        </div>
    </div>
    <div class="broad-btn">
        <a  href="${ctx}/broadband/new-install" class="btn btn-blue">我要办宽带</a>
    </div>
</div>

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript"  src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<%--插码相关--%>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>

</body>
</html>
