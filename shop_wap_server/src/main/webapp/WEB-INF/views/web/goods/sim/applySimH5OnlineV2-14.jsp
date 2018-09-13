<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：校园营销，号段卡--%>
<html lang="zh-CN">

<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关：test--%>
    <%--<meta name="WT.si_n" content="H5号卡销售_${conf.confId}" >
    <meta name="WT.si_x" content="售卡申请_${conf.confId}" >--%>
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <META name="WT.si_n" content="">
    <META name="WT.si_s" content="">
    <META name="WT.si_x" content="">
    <META name="WT.ac" content="">
    <Meta itemprop="name" content="${conf.cardTypeName}">
    <Meta itemprop="description" content="${conf.planSecondDesc}">
    <Meta itemprop="image" name="image" content="">

    <title>号段卡-首页</title>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>大王卡</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/swiper/swiper.css">
    <link href="${ctxStatic}/css/sim/wapnew.css?v=1" rel="stylesheet">

    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <style>
        .local-color {
            color: ${conf.styleColor};
        }

        .local-backcolor {
            background: ${conf.styleColor};
        }

        /* 个性化样式 */
        ${conf.extContent1}

        .app-fix {
            position: fixed;
            bottom: 2px;
            left: 12px;
            z-index: 9999;
            font-size: 12px;
        }

        .app-fix .app-icon {
            display: block;
            background: url(../static/images/fx_icon.png) no-repeat center;
            background-size: 100%;
            width: 16px;
            height: 16px;
            margin: 0 auto;
        }

        .float-icon {
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
    <input type="hidden" value="${conf.planSecondDesc}" id="planDesc"><!--号卡描述-->
    <input type="hidden" value="" id="titleImg"><!--分享顶部图片-->
    <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}"><!--号卡名称-->
    <div class="btn-wrap" id="mallDiv">
        <button class="btn btn-gold local-backcolor _goSubmit" step="20">立即办卡</button>
    </div>

    <div class="pd10 hk-list" id="o2oDiv" style="display: none">
        <div class="title">
            <span class="text">业务办理</span>
        </div>
        <p class="pd10">方式1：
            <button class="btn btn-gold local-backcolor _goSubmit" step="20">立即办卡</button>
        </p>

        <%--<p class="pd10">方式2：对多个号码进行业务办理请点击 <a href="##" onclick="batchShare();" class="font-blue" style="padding-right:10px"  otitle="批量办理" otype="button" oarea="批量办理">批量办理</a></p>--%>
        <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="##" onclick="shareSim();" otitle="o2oel分享" otype="button"
                                        oarea="o2oel分享">
            <img src="${ctxStatic}/images/o2osim/fx-icon.png" style="width:0.8rem;margin-left:0.2rem;"></a>
        </p>
        <p class="pd10">方式3：群发短信推荐用户办理<a id="smsBtn" href="##" onclick="smsDirect();" otitle="o2oSms分享" otype="button"
                                         oarea="o2oSms分享">
            <img src="${ctxStatic}/images/o2osim/sms1.png" style="width:0.8rem;margin-left:0.2rem;"></a>
        </p>
    </div>

    <div class="pd-intr">
        <img src="${myTfsUrl}${conf.prodDetlImg}">
        <%--<img src="${myTfsUrl}${conf.feeDetlImg}" >--%>
    </div>
    <!-- end pd-intr -->

    <div id="sorry-modal" class="mask-layer">
        <div class="modal tips-modal">
            <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close"
               onclick="toggleModal('sorry-modal')"></a>
            <div class="modal-content">
                <div class="pt-30"></div>
                <p class="text-center _pomptTxt"></p>
            </div>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-1')">取&nbsp;&nbsp;消</button>
                <button>确&nbsp;&nbsp;认</button>
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
    <input type="hidden" name="planId" value="${plans[0].planId}"/>
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

    var imgUrl = "/res/img/${conf.titleImg}";
    var ctx = "${ctx}";
    $(function () {
        /*微信QQ分享参数*/
        try {
            var img = "${conf.titleImg}";
            var suffix = img.split('.')[1];
            var path = "/res/img/" + "${conf.titleImg}" + "_100x100." + suffix;
            $("#titleImg").val(path);
        } catch (e) {
            console.log(e);
        }
        /*给页面WT.si_n 赋初始值*/
        getConfId2("${conf.confId}", "");
        /*拿取url中的WT.at存入cookie*/
        getToCookie();
        //提交
        $('._goSubmit').on("click", function () {
            var step = $(this).attr("step");
            trace(step);//引入插码,在js中try catch了
            $("#applyForm").submit();
        });
    });
</script>
</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=62536"></script>
<script type="text/javascript">
    var templateId = '${templateId}';
    var tken = $.cookie('token');
    var secToken = $.cookie('secToken');
    //跳转到sms-create.html页面后token丢失,故重新设置一下token cookie
    $.cookie('secToken', secToken, {"domain": "wap.hn.10086.cn", "path": "/"});
    $.cookie('token', tken, {"domain": "wap.hn.10086.cn", "path": "/"});
    $.cookie('userToken', tken, {"domain": "wap.hn.10086.cn", "path": "/"});
    var url = "http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?confId=" + getQueryString("confId") +
        "&recmdCode=" + getQueryString("recmdCode") + "&shopId=" + getQueryString("shopId");

    function smsDirect() {
        window.location.href = "http://wap.hn.10086.cn/ecsmc-static/static/sp/html/sms-create.html?templateId=" +
            templateId + "&linkUrl=" + url;
    }
</script>
<%--微信QQ分享--%>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShareCommon.js?v=201805"></script>
</html>