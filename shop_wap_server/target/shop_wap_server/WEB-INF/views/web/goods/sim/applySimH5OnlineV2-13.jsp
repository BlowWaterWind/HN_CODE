<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：号段卡新和家庭两档 E050RSENSD--%>
<html>
<head>
    <%--<c:set var="mymyTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关--%>
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <META name="WT.si_n" content="">
    <META name="WT.si_s" content="">
    <META name="WT.si_x" content="">
    <META name="WT.ac" content="">
    <title>号段卡-首页</title>
    <link href="${ctxStatic}/css/sim/wap-simh5onlineV2.css?v=10" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <%--插码相关--%>
        <%--插码相关--%>
        <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
        <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <%-- 一个居中，三个换行居中的效果 --%>
    <style>
        .tcard-list {
            justify-content: space-around
        }

        .taocan .taocan-foot:after {
            content: "";
            width: 0;
            height: 0;
            border-style: solid;
            border-width: .16rem 0 .16rem .213333rem;
            border-color: transparent transparent transparent${conf.styleColor};
        }

        .taocan .taocan-foot .right-flag {
            content: "";
            width: 0;
            height: 0;
            border-style: solid;
            border-width: .16rem .213333rem .16rem 0;
            border-color: transparent ${conf.styleColor} transparent transparent;
        }

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
        }
    </style>
</head>
<body>
<div class="container bg-01">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}">
    </div>
    <div class="taocan">
        <div class="taocan-top">
            <h3>${conf.planDesc}</h3>
            <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
        </div>
        <div class="taocan-body">
            <ul class="tcard-list">
                <c:forEach items="${plans}" var="ps">
                    <%--<c:set var="info" value="${fn:split(ps.planName,\"套餐\")}"/>--%>
                    <li>
                        <div>
                                <%--<h3>${ps.planName}</h3>--%>
                                <%-- 名称信息 --%>
                            <h3>${confPlansMap[ps.planId]}</h3>
                                <%-- 资费信息：两种情况 --%>
                            <c:if test="${ps.otherBusiness != null}">
                                <p> ${ps.otherBusiness}元/月</p>
                            </c:if>
                            <c:if test="${ps.otherBusiness == null}">
                                <p><fmt:formatNumber value="${ps.planFee/100}" pattern="#" type="number"/>元/月</p>
                            </c:if>
                            <input type="hidden" name="planId" value="${ps.planId}">
                            <input type="hidden" name="planName" value="${ps.planName}">
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="btn-wrap">
                <button class="btn btn-blue _goSubmit" style="background-color:${conf.styleColor};" step="20">立即申请</button>
            </div>
        </div>
        <div class="taocan-foot">
            <%--<c:if test="${isAllowRecmd == '1'}">
                <div class="right-flag"></div>
                &nbsp;&nbsp;
                <a href="${ctx}recmd/toGenSimRecmdLink?recmdBusiParam=${conf.confId}&rcdConfId=${conf.rcdConfId}"
                       class="recommend-link" style="margin-right:5.33333rem;">我要推荐
                </a>
            </c:if>--%>
            <a href="#zfxq">更多资费详情</a>
        </div>
        <%--<c:if test="${isAllowRecmd == '1'}">
           <div style="color: red;padding-left: 10px;" id="tjlabel">推荐送流量，最高可获赠15G</div>
        </c:if>--%>
    </div>
    <img src="${myTfsUrl}${conf.prodDetlImg}">
    <img src="${myTfsUrl}${conf.feeDetlImg}" id="zfxq">
    <form id="applyForm" action="${ctx}o2oSimPreBuy/toValidateSim" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="reCmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
        <input type="hidden" name="shopId" value="${shopId}"/>
    </form>
</div>

</body>
<script type="text/javascript">
    $(function () {
        /*拿取url中的WT.at存入cookie*/
        getToCookie();
        //还未选套餐先默认为confId
        getConfId("${conf.confId}","${conf.confId}");

        var slctImg = '${myTfsUrl}${conf.slctedImg}';
        var slctNoImg = '${myTfsUrl}${conf.slctedNoImg}';
        var slctNoImgHtml = "<img src=" + slctNoImg + ">";
        //默认对第一个为选中
        var firstLi = ".tcard-list li:first";
        $(".tcard-list li").append(slctNoImgHtml);
        $(firstLi).find("img").attr("src", slctImg);
        $("#applyForm input[name='planId']").val($(firstLi + " input[name='planId']").val());
        $("#applyForm input[name='planName']").val($(firstLi + " input[name='planName']").val());
        //绑定点击事件
        $(".tcard-list li").on("click", function () {
            $(".tcard-list li").find('img').attr("src", slctNoImg);
            $(this).find('img').attr("src", slctImg);
            $("#applyForm input[name='planId']").val($(this).find('input[name="planId"]').val());
            $("#applyForm input[name='planName']").val($(this).find('input[name="planName"]').val());
        });
        //提交;
        $('._goSubmit').on("click", function () {
            var planId = $("#applyForm input[name='planId']").val();
            var step = $(this).attr("step");
            quote("${conf.confId}",planId);
            trace(step);
            $("#applyForm").submit();
        });
    });
    var imgUrl = "/res/img/${conf.titleImg}";
</script>
</html>