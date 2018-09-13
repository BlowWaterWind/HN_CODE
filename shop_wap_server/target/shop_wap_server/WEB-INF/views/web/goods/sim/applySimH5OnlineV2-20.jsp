<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：号段卡新和家庭两档--%>
<html>
<head>
    <%--<c:set var="mymyTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <%--插码相关--%>
    <%--<meta name="WT.si_n" content="H5号卡销售_${conf.confId}">--%>
    <%--<meta name="WT.si_x" content="售卡申请_${conf.confId}">--%>
        <%--插码--%>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <meta name="format-detection" content="telephone=no, email=no"/>
    <META name="WT.si_n" content="">
    <META name="WT.si_s" content="">
    <META name="WT.ac" content="">
    <META name="WT.si_x" content="20">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <title>全能视频王卡-首页</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/normalize-6.0.0.min.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/swipervideo.css?v=1"/>
    <link href="${ctxStatic}/css/sim/wap-simvideo.css?v=8" rel="stylesheet">

        <script type="text/javascript" src="${ctxStatic}/ap/lib/swiper.min.js"></script>
    <%--插码相关--%>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <style>
        .float-icon{
            width: 2.573333rem;
            height: 2.493333rem;
            position: fixed;
            right: 0.266667rem;
            top: 10%;
            z-index: 1000;
        }
    </style>

</head>
<body>
<div class="container">
    <div class="header-wrap">
        <div class="header-info">
            <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
            <!--swiper start-->
            <div class="swiper-container index-swiper" id="indexSwiper">
                <div class="swiper-wrapper">
                    <c:forEach items="${plans}" var="ps">
                    <div class="swiper-slide">
                        <a href="javascript:;">
                            <div class="swiper-info">
                                <h3 class="f32">资费：<span class="swiper-blue">${ps.planFee/100}元/月</span></h3>
                                <div class="swiper-txt">
                                    <p>${ps.planDesc}</p>
                                </div>
                            </div>
                        </a>
                        <input type="hidden" name="planId" value="${ps.planId}">
                        <input type="hidden" name="planName" value="${ps.planName}">
                        <input type="hidden" name="planDesc" value="${ps.planDesc}">
                    </div>
                    </c:forEach>
                </div>
            </div>
            <!--swiper end-->
            <div class="header-btn">
                <a class="btn-link" id="_goSubmit" step="20">立即办理</a>

            </div>
        </div>
    </div>
    <div class="pd10 hk-list" id="o2oDiv" style="display: none">
        <div class="title">
            <span class="text">业务办理</span>
        </div>
        <p class="pd10">方式1：
           点击上方"立即办理"按钮，立即申请
        </p>

        <%--<p class="pd10">方式2：对多个号码进行业务办理请点击<a href="##" onclick="batchShare();" class="font-blue" style="padding-right:10px"  otitle="批量办理" otype="button" oarea="批量办理">批量办理</a></p>--%>
        <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="##" onclick="shareSim();" otitle="o2oel分享" otype="button" oarea="o2oel分享">
            <img src="${ctxStatic}/images/o2osim/fx-icon.png" style=width:0.8rem;margin-left:0.2rem;></a>
        </p>
    </div>
    <c:if test="${isAllowRecmd == '1'}">
        <div class="float-icon">
            <a href="${ctx}recmd/toGenSimRecmdLink?recmdBusiParam=${conf.confId}&rcdConfId=${conf.rcdConfId}">
                <%--<img src="${ctxStatic}/images/goods/sim/share.png?f=0509">--%>
            </a>
        </div>
    </c:if>
    <!--内容部分 start-->
    <div class="almighty-wrap">
        <img src="${ctxStatic}/css/sim/images/videosimimages/detail01.jpg" />
        <img src="${ctxStatic}/css/sim/images/videosimimages/detail02.jpg" />
        <img src="${ctxStatic}/css/sim/images/videosimimages/detail03.jpg" />
    </div>
    <!--内容部分 end-->
</div>
<form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
    <input type="hidden" name="confId" value="${conf.confId}"/>
    <input type="hidden" name="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
    <input type="hidden" name="planId"/>
    <input type="hidden" name="planName"/>
    <input type="hidden" name="CHANID" value="${CHANID}"/>
</form>
<!--插码相关-->
<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.jquery.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>


<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=1"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
<script type="text/javascript">
    $(function () {
        /*给页面WT.si_n 赋初始值*/
        getConfId("${conf.confId}","${conf.confId}");
        /*拿取url中的WT.at存入cookie*/
        getToCookie();
        //提交
        $('#_goSubmit').on("click", function () {
            try{
                var planId = $(".swiper-slide-active input[name='planId']").val();
                quote("${conf.confId}",planId);
                var step = $("#_goSubmit").attr("step");
                trace(step);
            }catch (e){
                console.log(e);
            }
            $("#applyForm input[name='planId']").val($(".swiper-slide-active input[name='planId']").val());
            $("#applyForm input[name='planName']").val($(".swiper-slide-active input[name='planName']").val());
            $("#applyForm").submit();
        });
        var swiper = new Swiper('#indexSwiper', {
            initialSlide: 1,
            effect: 'coverflow',
            centeredSlides: true,
            slidesPerView: 'auto',
            coverflow: {
                rotate: 0,
                stretch: 112,
                depth: 100,
                modifier: 2,
                slideShadows: true
            }
        });
    });
</script>
</body>
</html>