<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<html>
<head>
    <meta charset="UTF-8">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <meta name="WT.si_n" content="GJJ" />
    <meta name="WT.si_x" content="20" />
    <title>湖南移动商城</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/swiper.min.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/wap.css" />
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/swiper.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>


<body class="bg-yellow">
<div class="container">
    <div class="lot-bg">
        <img class="cons-img" src="${ctxStatic}/images/campusmark/cons.png" alt="">
        <img  class="common-price" src="${ctxStatic}/images/campusmark/${imgName}" alt="">
    </div>


    <div class="common-flex">
        <a class="common-btn" id="btnGetQuan" href="${ctx}campusmark/toMyCoupons"><img src="${ctxStatic}/images/campusmark/btn-seequan.png" alt=""></a>
        <a class="common-btn" href="${ctx}campusmark/toIndex"><img src="${ctxStatic}/images/campusmark/btn-continue.png" alt=""></a>

    </div>
    <div class="common-flex">
        <a class="common-btn-big" id="" href="${ctx}campusmark/shareToFriends?quan=${quan}"><img src="${ctxStatic}/images/campusmark/btn-sharefirnd.png" alt=""></a>

    </div>




</div>
<div class="mask"></div>


</body>


</html>