<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：新和家庭模板,用于和掌柜号段卡--%>
<!DOCTYPE html>
<head>
    <%--<c:set var="mymyTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
        <meta name="format-detection" content="telphone=no, email=no" />
    <%--插码相关--%>
    <meta name="WT.si_n" content="H5号卡销售_${conf.confId}" >
    <meta name="WT.si_x" content="售卡申请_${conf.confId}" >
    <title>在线售卡-${conf.cardTypeName}</title>
        <!-- css -->
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/kill/lib/normalize-6.0.0.min.css" />
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/pure-grids.css" />
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/wap-hjt.css" />
        <!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <%-- 一个居中，三个换行居中的效果 --%>

</head>
<body>
<div class="container">
    <!--swiper start-->
    <div class="swiper-container yxhm-swiper" id="swiper">
        <div class="swiper-wrapper">
            <%--<div class="swiper-slide"><img src="${ctxStatic}/images/beauty-sim/silder.jpg"/></div>--%>
            <div class="swiper-slide"><img src="${myTfsUrl}${conf.titleImg}" /></div>
        </div>
        <!--Pagination-->
        <%--<div class="swiper-pagination"></div>--%>
        <!--Arrows -->
        <%--<div class="swiper-button-next"></div>--%>
        <%--<div class="swiper-button-prev"></div>--%>
    </div>
    <!--swiper end-->
    <!--套餐介绍和档次选择 start-->
    <div class="yxhm-pack">
        <div class="pack-list">
            <span class="pack-title">套餐介绍:</span>
            <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
        </div>
        <div class="pack-txt">${conf.cardTypeName}</div>
        <div class="pack-list">
            <span class="pack-title">套餐名称:</span>
            <ul class="grades-list">
                <c:forEach items="${plans}" var="ps" varStatus="index">
                <li <c:if test="${varStatus.index ==0}">class="active"</c:if>>${confPlansMap[ps.planId]}
                    <input type="hidden" name="planId" value="${ps.planId}">
                    <input type="hidden" name="planName" value="${ps.planName}">
                    <input type="hidden" name="planDesc" value="${ps.planDesc}">
                </li>
                </c:forEach>
            </ul>
            <!--套餐信息 start-->
            <div class="yxhm-info" id="desc"></div>
            <!--套餐信息 end-->
        </div>
    </div>
    <!--套餐介绍和档次选择 end-->
    <!--产品内容介绍 start-->
    <div class="yxhm-intro">
        <!--资费标准 start-->
        <div class="intro-list">
            <img src="${myTfsUrl}${conf.prodDetlImg}">
        </div>
        <!--资费标准 end-->
        <!--产品介绍 start-->
        <div class="intro-list">
            <img src="${myTfsUrl}${conf.feeDetlImg}" id="zfxq">
        </div>
        <%--<img src="${myTfsUrl}${conf.prodDetlImg}">--%>
        <%--<img src="${myTfsUrl}${conf.feeDetlImg}" id="zfxq">--%>
    </div>
    </div>
    <!--底部办理按钮 start-->
    <div class="container yxhm-fixed">
        <div class="yxhm-footer">
            <div class="yxhm-footer-btn" > <a href="javascript:void(0)" class="yxhm-btn yxhm-btn-dblue _goSubmit"  otitle="H5号卡销售_${conf.confId}-立即办理">立即办理&gt;&gt;</a></div>
            <!--按钮灰色class yxhm-btn-dblue 替换为 yxhm-gray -->
        </div>
    </div>

    <form id="applyForm" action="${ctx}o2oSimPreBuy/realNameRegistry" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="reCmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
        <input type="hidden" name="shopId" value="${shopId}"/>
    </form>
</body>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/swiper/swiper.min.css"/>
<script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.min.js"></script>
<script type="text/javascript">
    $(function () {
        //默认对第一个为选中
        $(".grades-list li").first().addClass("active");

        var firstLi = ".active";
        $("#applyForm input[name='planId']").val($(firstLi+" input[name='planId']").val());
        $("#applyForm input[name='planName']").val($(firstLi+" input[name='planName']").val());
        $("#desc").html($(firstLi+" input[name='planDesc']").val());
        //绑定点击事件
        $(".grades-list li").on("click",function () {
            $(".active").removeClass();
            $(this).addClass("active");
            $("#applyForm input[name='planId']").val($(this).find('input[name="planId"]').val());
            $("#applyForm input[name='planName']").val($(this).find('input[name="planName"]').val());
            $("#desc").html($(this).find('input[name="planDesc"]').val());
        });
        //提交
        $('._goSubmit').on("click",function() {
            $("#applyForm").submit();
        });
    });
    var imgUrl="/res/img/${conf.titleImg}";
    var swiper = new Swiper('.swiper-container', {
        slidesPerView: 1,
        spaceBetween: 30,
        loop: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
    });
</script>

</html>