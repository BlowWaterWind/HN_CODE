<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：百度卡/头条卡模板 不选套餐默认套餐--%>
<html lang="zh-CN">

<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <%--插码相关：test--%>
    <meta name="WT.si_n" content="">
    <meta name="WT.si_x" content="">
    <meta name="WT.si_s" content="">
    <META name="WT.ac" content="">
    <%--插码--%>
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">



    <title>在线售卡-首页</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>大王卡</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/swiper/swiper.css">
    <link href="${ctxStatic}/css/sim/wapnew.css?v=2" rel="stylesheet">

    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
        <style>
            .local-backcolor{
                background:${conf.styleColor};
                margin-top: 10px;
            }
            /* 个性化样式 */
            ${conf.extContent1}

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
<!-- start container -->
<div class="container">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}">
    </div>
    <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
    <input type="hidden" id="cardDesc" value="${conf.planDesc}">
    <!-- Swiper -->
    <div class="btn-wrap" id="mallDiv">
        <button class="btn btn-gold local-backcolor _goSubmit" step="20">立即申请</button>
    </div>
    <div class="pd10 hk-list" id="o2oDiv" style="display: none">
        <div class="title">
            <span class="text">业务办理</span>
        </div>
        <p class="pd10">方式1：
            <button class="btn btn-gold local-backcolor _goSubmit" step="20">立即申请</button>
        </p>

        <%--<p class="pd10">方式2：对多个号码进行业务办理请点击<a href="##" onclick="batchShare();" class="font-blue" style="padding-right:10px"  otitle="批量办理" otype="button" oarea="批量办理">批量办理</a></p>--%>
        <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="##" onclick="shareSim();" otitle="o2oel分享" otype="button" oarea="o2oel分享">
            <img src="${ctxStatic}/images/o2osim/fx-icon.png" style="width:0.8rem;margin-left:0.2rem;"></a>
        </p>
    </div>
    <!-- end taocan -->
    <div class="pd-intr">
        <img src="${myTfsUrl}${conf.prodDetlImg}">
        <img src="${myTfsUrl}${conf.feeDetlImg}" >
    </div>
    <!-- end pd-intr -->

    <c:if test="${isAllowRecmd == '1'}">
        <div class="float-icon">
            <a href="${ctx}recmd/toGenSimRecmdLink?recmdBusiParam=${conf.confId}&rcdConfId=${conf.rcdConfId}">
                <img src="${ctxStatic}/images/goods/sim/share.png?f=0509">
            </a>
        </div>
    </c:if>

</div>
<!-- end container -->

<form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
    <input type="hidden" name="confId" value="${conf.confId}"/>
    <input type="hidden" name="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
    <input type="hidden" name="planId" value="${plans[0].planId}"/>
    <input type="hidden" name="planName" value="${plans[0].planName}"/>
    <input type="hidden" name="CHANID" value="${CHANID}"/>
</form>

<%--下面的用于微信分享--%>
<input type="hidden" value="/res/img/${conf.titleImg}" id="titleImg">
<input type="hidden" value="${conf.cardTypeName}" id="cardTypeName">
<input type="hidden" value="${conf.planSecondDesc}" id="planDesc">


<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.jquery.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>

<%--微信分享--%>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShareCommon.js?v=201805"></script>

<%--插码相关--%>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=6"></script>
<%--插码--%>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
<script type="text/javascript">
    var imgUrl="/res/img/${conf.titleImg}";
    var ctx="${ctx}";
    $(function () {
        /*给页面WT.si_n 赋初始值*/
        var planId = $("input[name='planId']").val();
        getConfId("${conf.confId}",planId);
        //提交
        $('._goSubmit').on("click",function() {
            var planId = $("input[name='planId']").val();
            var step = $(this).attr("step");
            quote("${conf.confId}",planId);
            trace(step);
            $("#applyForm").submit();
        });
    });
</script>
</body>

</html>