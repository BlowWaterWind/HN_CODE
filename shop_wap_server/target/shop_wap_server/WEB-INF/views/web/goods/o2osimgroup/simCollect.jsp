<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/8/9
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关：test--%>
    <meta name="WT.si_n" content="O2O集团售卡_${conf.confId}">
    <meta name="WT.si_x" content="O2O集团售卡_${conf.confId}">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">

    <title>我的收藏</title>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">

    <link href="${ctxStatic}/css/sim/simgroup/o2o.sim.group.css?v=1" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>

    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>


</head>
<body>

<div class="container">

    <div class="common-navbar-wrap">
        <div class="common-navbar ">
            <div class="navbar-left">
                <a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
            </div>
            <h3 class="navbar-title">我的收藏</h3>
        </div>
    </div>

    <div id="header">
        <div id="nav" class="clearfix">
            <img src="${ctxStatic}/images/home.png" alt="" style="width: 15px">
            <a href="">首页</a>
            <a href="${referer}">选号中心</a>
            <a href="#" class="on">我的收藏</a>
        </div>
    </div>


    <div style="margin: 20px;">
        <ul id="newsList" class="content list-phone">
        </ul>
    </div>
</div>
<script type="text/javascript">
    var ctx = "${ctx}";
</script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/simgroup/collectNum.js?v=1"></script>
<script type="text/javascript">
    showCartList(0);
    var clientType = isAppClient();
    if(clientType=="hezhanggui"){
        $(".common-navbar-wrap").show();
    }else if(clientType=="html5" || clientType=="android" || clientType=="ios"){
        //去除头和分享(电脑/安卓/ios浏览器打开不分享但是显示头)
        $(".navbar-right").remove();
    }else{
        //微信打开调用微信的分享,并且不显示头
        $(".common-navbar-wrap").remove();
    }
</script>
</body>
</html>
