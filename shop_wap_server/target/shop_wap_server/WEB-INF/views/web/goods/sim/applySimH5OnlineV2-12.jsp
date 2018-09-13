<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：新大王卡类型模板-O2O渠道线下推广模板，号段卡;第一版本的号段卡要选套餐,不分享;confId-2002--%>
<html lang="zh-CN">

<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <%--插码相关：test--%>
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <META name="WT.si_n" content="">
    <META name="WT.si_s" content="">
    <META name="WT.si_x" content="">
    <META name="WT.ac" content="">
    <Meta itemprop="name" content="${conf.cardTypeName}">
    <Meta itemprop="description" content="${conf.planSecondDesc}">
    <title>号段卡-首页</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>大王卡</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/swiper/swiper.css">
    <link href="${ctxStatic}/css/sim/wapnew.css?v=1" rel="stylesheet">

    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
        <style>
            .local-backcolor{
                background:${conf.styleColor};
            }
            /* 个性化样式 */
            ${conf.extContent1}
        </style>
</head>

<body>
<!-- start container -->
<div class="container">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}">
    </div>
    <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
    <!-- Swiper -->
    <div class="swiper-container index-swiper">
        <div class="swiper-wrapper">
            <c:forEach items="${plans}" var="ps">
                <c:set var="info" value="${fn:split(ps.planName,\"套餐\")}"/>
                <div class="swiper-slide">
                    <div class="king-card">
                        <img src="${myTfsUrl}${conf.slctedNoImg}" alt="大王卡">
                        <div class="king-card-text">
                            <h3>${confPlansMap[ps.planId]}</h3>
                            <p>(<fmt:formatNumber value="${ps.planFee/100}" pattern="#" type="number"/>元/月)</p>
                        </div>
                    </div>
                    <input type="hidden" name="planId" value="${ps.planId}">
                    <input type="hidden" name="planName" value="${ps.planName}">
                    <input type="hidden" name="planDesc" value="${ps.planDesc}">
                </div>
            </c:forEach>

        </div>
        <!-- Add Arrows -->
        <div class="swiper-button-prev"></div>
        <div class="swiper-button-next"></div>
    </div>
    <div class="btn-wrap" id="mallDiv">
        <button class="btn btn-gold local-backcolor _goSubmit" step="20">立即办卡</button>
    </div>
    <!-- end taocan -->
    <div class="pd-intr">
        <img src="${myTfsUrl}${conf.prodDetlImg}">
        <img src="${myTfsUrl}${conf.feeDetlImg}" >
    </div>
    <!-- end pd-intr -->

    <div id="sorry-modal" class="mask-layer">
        <div class="modal tips-modal">
            <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')"></a>
            <div class="modal-content">
                <div class="pt-30"></div>
                <p class="text-center _pomptTxt"></p>
            </div>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-1')">取&nbsp;&nbsp;消</button>
                <button >确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>

    <!-- 公共加载弹窗 -->
    <div id="loding-modal" class="mask-layer">
        <div class="modal min-modal">
            <div class="modal-content">
                <div class="modal-con">
                    <div class="dropload-load">
                        <div class="loading"></div>
                    </div>
                    <p class="text-center _promptSorry"></p>
                    <p class="text-center _pomptTxt"></p>
                    <div class="pb-20"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- end container -->

<form id="applyForm" action="${ctx}o2oSimPreBuy/toValidateSim" method="post">
    <input type="hidden" name="confId" value="${conf.confId}"/>
    <input type="hidden" name="reCmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
    <input type="hidden" name="planId"/>
    <input type="hidden" name="planName"/>
    <input type="hidden" name="CHANID" value="${CHANID}"/>
    <input type="hidden" name="shopId" value="${shopId}"/>
</form>


<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.jquery.js"></script>
<%--插码相关--%>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
<script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>
<script type="text/javascript">
    var imgUrl="/res/img/${conf.titleImg}";
    var ctx="${ctx}";
    $(function () {
        /*拿取url中的WT.at存入cookie*/
        getToCookie();
        //还未选套餐先默认为confId
        getConfId("${conf.confId}","${conf.confId}");
        //提交
        $('._goSubmit').on("click",function() {
            var planId =$(".swiper-slide-active input[name='planId']").val();
            var step = $(this).attr("step");
            quote("${conf.confId}",planId);
            trace(step);
            $("#applyForm input[name='planId']").val(planId);
            $("#applyForm input[name='planName']").val($(".swiper-slide-active input[name='planName']").val());
            $("#applyForm").submit();
        });

        var swiper = new Swiper('.index-swiper', {
            initialSlide :1,
            effect: 'coverflow',
            slidesPerView: 3,
            grabCursor: true,
            centeredSlides: true,
            slidesPerView: 'auto',
            nextButton: '.swiper-button-next',
            prevButton: '.swiper-button-prev',
            coverflow: {
                rotate: 0,
                stretch: 25,
                depth: 60,
                modifier: 4,
                slideShadows : false
            }
        });

    });
</script>
</body>

</html>