<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
<title>分享来砍价</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
<!-- css -->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/cut/lib/normalize-5.0.0.css"/>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/cut/css/wap.css"/>

<!-- JS -->
<script type="text/javascript" src="${ctxStatic}/cut/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/cut/lib/jquery.vticker-min.js"></script>
<script type="text/javascript" src="${ctxStatic}/cut/js/comm.js"></script>

</head>
<body>
<div class="containter hbbargain-bg">
    <div class="hbbargain-shop hbbargain-share clearfix">
        <p class="hbbargain-shop-time">活动时间：<b>即日起-<fmt:formatDate value="${activityGoodsPara.endTime}" pattern="MM.dd"></fmt:formatDate></b></p>
        <img class="bargin-succ-img" src="${ctxStatic}/cut/images/bargain/bargin-succ.png" />
        <div class="share-txt">
            <p class="share-txt01">我已砍价购机成功，<br/>小伙伴们一起来参与吧！</p>
            <p class="share-txt02">机型：${activityGoodsPara.actTitle}<br/>砍后价格：${activityGoodsPara.salePrice}元</p>
        </div>
        <img  class="share-img"   src="${ctxStatic}/${activityGoodsPara.rsrv5}" />
    </div>
    <div class="tc"><a class="hbbargain-btn" href="javascript:void(0);">点我发起砍价</a> </div>
    <ul class="hbbargain-progress  clearfix">
        <li><span class="hbbargain-progress-btn">发起<br/>活动</span></li>
        <li><span class="hbbargain-progress-btn">喊朋友<br/>来砍价</span></li>
        <li><span class="hbbargain-progress-btn">砍到<br/>优惠价</span></li>
        <li><span class="hbbargain-progress-btn">优惠<br/>购机</span></li>
    </ul>
    <div class="bargin02bg">优惠购机随心挑</div>
    <ul class="hbbargain-list-phone clearfix">
        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">
            <img src="${ctxStatic}/cut/images/bargain/phone01.png"/> </a>
            <div>
                <a class="hbbargain-list-text01"
                   href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">中国移动A3</a>
                <a class="hbbargain-list-text02"
                   href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">1G+8G<br/>双卡双待<br/>5.0英寸高清屏</a>
                <a class="hbbargain-list-text03"
                   href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">优惠价：￥<b>499</b>元</a></div>
        </li>

        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282"><img src="${ctxStatic}/cut/images/bargain/phone02.png"/> </a>
            <div>
                <a class="hbbargain-list-text01" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282">畅享7Plus</a>
                <a class="hbbargain-list-text02" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282">性能强劲<br/>4000Ah大电池<br/>.5英寸高大屏</a>
                <a class="hbbargain-list-text03" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282">优惠价：￥<b>1499</b>元</a></div>
        </li>
        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html"><img src="${ctxStatic}/cut/images/bargain/phone03.png"/> </a>
            <div>
                <a class="hbbargain-list-text01" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html">魅蓝U20</a>
                <a class="hbbargain-list-text02" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html">2G + 16G<br/>5.5英寸高清屏<br/>1300万像素摄像头 指纹识别</a>
                <a class="hbbargain-list-text03" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html">优惠价：￥<b>799</b>元</a></div>
        </li>
        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html"><img src="${ctxStatic}/cut/images/bargain/phone04.png"/> </a>
            <div><a class="hbbargain-list-text01" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html">畅享6S</a>
                <a class="hbbargain-list-text02" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html">3G + 32G<br/>轻薄金属机身<br/>1300万像素摄像头</a>
                <a class="hbbargain-list-text03" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html">优惠价：￥<b>1299</b>元</a></div>
        </li>
    </ul>
</div>
</body>
</html>
