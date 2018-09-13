<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/1/17
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>优选号码-套餐选择</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-6.0.0.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/pure-grids.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/css/wap.css"/>

    <!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
</head>
<body>
<div class="container">
    <!--swiper start-->
    <div class="swiper-container yxhm-swiper" id="swiper">
        <div class="swiper-wrapper">
            <div class="swiper-slide"><img src="${ctxStatic}/images/beauty-sim/silder.jpg"/></div>
            <div class="swiper-slide"><img src="${ctxStatic}/images/beauty-sim/silder.jpg"/></div>
            <div class="swiper-slide"><img src="${ctxStatic}/images/beauty-sim/silder.jpg"/></div>
        </div>
        <!--Pagination-->
        <div class="swiper-pagination"></div>
        <!--Arrows -->
        <div class="swiper-button-next"></div>
        <div class="swiper-button-prev"></div>
    </div>
    <!--swiper end-->
    <!--套餐介绍和档次选择 start-->
    <div class="yxhm-pack">
        <div class="pack-list">
            <span class="pack-title">套餐介绍:</span>
            <div class="pack-txt">${conf.planDesc}</div>
        </div>
        <div class="pack-list">
            <span class="pack-title">套餐名称:</span>
            <ul class="grades-list">
                <c:forEach items="${plans}" var="plan" varStatus="status">
                    <c:if test="${status.index == 0}">
                        <li class="active" value="${plan.planId}">${plan.planName}</li>
                    </c:if>
                    <c:if test="${status.index != 0}">
                    <li value="${plan.planId}">${plan.planName}</li>
                    </c:if>
                </c:forEach>
                <%--<li>129元档</li>--%>
            </ul>
            <!--套餐信息 start-->
            <div class="yxhm-info">套餐包含：1GB国内流量、1000分钟国内语音、100M宽带、加1元赠2000分钟固话通话。</div>
            <!--套餐信息 end-->
        </div>
    </div>
    <!--套餐介绍和档次选择 end-->
    <!--产品内容介绍 start-->
        <div class="yxhm-intro">
            <!--资费标准 start-->
            <div class="intro-list">
                <img src="${ctxStatic}/images/beauty-sim/tu01.png" />
            </div>
            <!--资费标准 end-->
            <!--产品介绍 start-->
            <div class="intro-list">
                <img src="${ctxStatic}/images/beauty-sim/tu02.png" />
            </div>
            <!--产品介绍 end-->
            <!--温馨提示 start-->
            <div class="yxhm-wxts">温馨提示：宽带、魔百和电视、固话请前往本地营业网点办理，收费标准以实际办 理为准。套餐12个月内不能转出。套餐赠送流量优先级量，套餐内国内流量可以结 转至次月，赠送流量不结转、不转赠。</div>
            <!--温馨提示 end-->
        </div>
        <!--产品内容介绍 end-->
</div>
<!--底部办理按钮 start-->
<div class="container yxhm-fixed">
    <div class="yxhm-footer">
        <div class="yxhm-footer-btn"><a onclick="choosePlan()" class="yxhm-btn yxhm-btn-dblue">立即办理&gt;&gt;</a></div>
        <!--按钮灰色class yxhm-btn-dblue 替换为 yxhm-gray -->
    </div>
</div>
<form id="submitPlan" method="post" action="beautifulSimH5OnlineToConfirmOrder">
    <input type="hidden" name="planId">
    <input type="hidden" name="planName">
</form>
<!--底部办理按钮 end-->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/swiper/swiper.min.css"/>
<script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.min.js"></script>
<script>
    var swiper = new Swiper('.swiper-container', {
        slidesPerView: 1,
        spaceBetween: 30,
        loop: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev'
        }
    });

    function choosePlan() {
        var planId ='';
        var planName = '';
        $(".grades-list li").each(function(){
            if($(this).hasClass('active')){
                planId = $(this).attr('value');
                planName = $(this).text();
                return;
            }
        });
        $("#submitPlan input[name='planId']").val(planId);
        $("#submitPlan input[name='planName']").val(planName);
        $("#submitPlan").submit();
    }

    $(".grades-list li").click(function () {
        $(this).addClass('active');
        $(this).siblings().removeClass('active');
    });
</script>
</body>
</html>
