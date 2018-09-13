<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：新和家庭模板--%>
<html>
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
        <style>
            .local-color{
                color:${conf.styleColor};
            }
            .local-backcolor{
                background:${conf.styleColor};
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
            .float-icon{
                width: 2.573333rem;
                height: 2.493333rem;
                position: fixed;
                right: 0.266667rem;
                top: 10%;
            }
            .pd10{padding:.133333rem}
            .hk-list{font-size:.373333rem;background-color: #fff;}.hk-list p{display:-webkit-box;display:-ms-flexbox;display:flex;align-items:center}.hk-list .form-control{border:1px solid #dedede;height:.933333rem;line-height:.933333rem;font-size:.373333rem;padding-left:.266667rem}
            .pd10 .title {
                border-bottom: 1px solid #dedede;
                margin: 0 !important;
            }
            .pd10 .title {
                width: 100%;
                height: 36px;
                line-height: 36px;
                margin-left: 10px;
                font-size: 14px;
                text-shadow: none;
            }
            .pd10 .title .text {
                border-left: 5px solid #47bfff;
                padding-left: 8px;
            }
            .pd10 .title span {
                margin-left: 10px;
                font-weight: bold;
            }
        </style>
</head>
<body>
<div class="container">
    <!--swiper start-->
    <div class="swiper-container yxhm-swiper" id="swiper">
        <div class="swiper-wrapper">
            <div class="swiper-slide"><img src="${myTfsUrl}${conf.titleImg}" /></div>
        </div>
        <!--Pagination-->
        <div class="swiper-pagination"></div>
        <!--Arrows -->
        <%--<div class="swiper-button-next"></div>--%>
        <%--<div class="swiper-button-prev"></div>--%>
    </div>
    <!--swiper end-->
    <!--套餐介绍和档次选择 start-->
    <div class="yxhm-pack">
        <div class="pack-list">
            <span class="pack-title">套餐介绍:</span>
            <div class="pack-txt">${conf.cardTypeName}</div>
            <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
        </div>
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

    <div class="pd10 hk-list" id="o2oDiv" style="display: none">
        <div class="title">
            <span class="text">业务办理</span>
        </div>
        <p class="pd10">方式1：
            <a href="javascript:void(0)" style="padding: 0 1.2rem;" class="yxhm-btn yxhm-btn-dblue _goSubmit"  otitle="H5号卡销售_${conf.confId}-立即办理">立即办理</a>
        </p>

        <%--<p class="pd10">方式2：对多个号码进行业务办理请点击 <a href="##" onclick="batchShare();" class="font-blue" style="padding-right:10px"  otitle="批量办理" otype="button" oarea="批量办理">批量办理</a></p>--%>
        <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="##" onclick="shareSim();" otitle="o2oel分享" otype="button" oarea="o2oel分享">
            <img src="${ctxStatic}/images/o2osim/fx-icon.png" style="width:0.8rem;margin-left:0.2rem;"></a>
        </p>
    </div>
    <!--产品内容介绍 start-->
    <div class="yxhm-intro">
        <img src="${myTfsUrl}${conf.prodDetlImg}">
        <img src="${myTfsUrl}${conf.feeDetlImg}" id="zfxq">
    </div>
    </div>
    <!--底部办理按钮 start-->
    <div class="container yxhm-fixed" id="mallDiv">
        <div class="yxhm-footer">
            <div class="yxhm-footer-btn" > <a href="javascript:void(0)" class="yxhm-btn yxhm-btn-dblue _goSubmit"  otitle="H5号卡销售_${conf.confId}-立即办理">立即办理&gt;&gt;</a></div>
            <!--按钮灰色class yxhm-btn-dblue 替换为 yxhm-gray -->
        </div>
    </div>

    <form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
    </form>
<c:if test="${isAllowRecmd == '1'}">
    <div class="float-icon">
        <a href="${ctx}recmd/toGenSimRecmdLink?recmdBusiParam=${conf.confId}&rcdConfId=${conf.rcdConfId}">
            <img src="${ctxStatic}/images/goods/sim/share.png?f=0509">
        </a>
    </div>
</c:if>
</body>
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

<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=6"></script>
</html>