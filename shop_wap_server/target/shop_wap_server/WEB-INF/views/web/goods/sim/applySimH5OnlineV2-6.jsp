<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：新飞享--%>
<html>
<head>
    <%--<c:set var="mymyTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <%--插码相关--%>
    <meta name="WT.si_n" content="H5号卡销售_${conf.confId}" >
    <meta name="WT.si_x" content="售卡申请_${conf.confId}" >
    <title>在线售卡-${conf.cardTypeName}</title>
    <link href="${ctxStatic}/css/sim/wap-newfx.css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <%-- 一个居中，三个换行居中的效果 --%>
    <style>
        .tcard-list {
            justify-content: space-around
        }
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
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}">
    </div>
    <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
        <div class="package">
            <!-- start package-list -->
            <ul class="Grid -between package-list">
                <c:forEach items="${plans}" var="ps" varStatus="index">
                    <li <c:if test="${varStatus.index ==0}">class="active"</c:if>>${confPlansMap[ps.planId]}
                        <input type="hidden" name="planId" value="${ps.planId}">
                        <input type="hidden" name="planName" value="${ps.planName}">
                    </li>
                </c:forEach>
            </ul>
            <!-- end package-list -->
            <p>友情提示：请选择一档，即可享受该档次套餐对应优惠; 订购卡品免费配送到家。 </p>
            <div class="Grid -center package-btn-wrap">
                <button class="btn-bus _goSubmit" style="background-color:${conf.styleColor};" otype="button" otitle="H5号卡销售_${conf.confId}-立即办理">立即办理<span>&nbsp;&gt;&gt;</span></button>
            </div>
        </div>
        <!-- end package -->
        <div class="pd-intr">
            <img src="${myTfsUrl}${conf.prodDetlImg}">
            <img src="${myTfsUrl}${conf.feeDetlImg}" id="zfxq">
        </div>
    </div>
    <form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
    </form>
</body>
<script type="text/javascript">
    $(function () {
        //默认对第一个为选中
        $(".package-list li").first().addClass("active");
        var firstLi = ".active";
        $("#applyForm input[name='planId']").val($(firstLi+" input[name='planId']").val());
        $("#applyForm input[name='planName']").val($(firstLi+" input[name='planName']").val());
        //绑定点击事件
        $(".package-list li").on("click",function () {
            $(".active").removeClass();
            $(this).addClass("active");
            $("#applyForm input[name='planId']").val($(this).find('input[name="planId"]').val());
            $("#applyForm input[name='planName']").val($(this).find('input[name="planName"]').val());
        });
        //提交
        $('._goSubmit').on("click",function() {
            $("#applyForm").submit();
        });
    });
    var imgUrl="/res/img/${conf.titleImg}";
</script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=3"></script>
</html>