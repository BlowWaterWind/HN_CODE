<%--
    description：滴滴卡类型模板，有且只能卖两个套餐
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <%--<c:set var="tfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关--%>
    <meta name="WT.si_n" content="H5号卡销售_${conf.confId}">
    <meta name="WT.si_x" content="售卡申请_${conf.confId}">
    <title>滴滴至尊卡</title>
        <link href="${ctxStatic}/css/sim/wap-simonlineV2-didi.css?v=2" rel="stylesheet">

    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
     <style>
        ${conf.extContent1}
     </style>
</head>
<body>
<div class="container bg-white">
    <div class="pack-banner">
        <c:if test="${conf.titleImg == null}">
            <img src="${ctxStatic}/css/sim/images/king-banner.png">
        </c:if>
        <c:if test="${conf.titleImg != null}">
            <img src="${myTfsUrl}${conf.titleImg}" >
       </c:if>
    </div>
    <div class="sale-pack" style="margin-top:-10px">
        <div class="sale-pack-top">
            <hN3>${conf.planDesc}</hN3>
        </div>
        <div class="tabs">
            <ul class="tab-menu">
                <c:forEach items="${plans}" var="ps">
                    <li>
                        <a>
                            <p>${confPlansMap[ps.planId]}</p>
                            <h1><fmt:formatNumber value="${ps.planFee/100}" pattern="#" type="number"/></h1>
                            <input type="hidden" name="planId" value="${ps.planId}">
                            <input type="hidden" name="planName" value="${ps.planName}">
                        </a>
                    </li>
                </c:forEach>
            </ul>
            <div id="tab-1">
                <div class="pack-info">
                    <c:forEach items="${plans}" var="ps">
                        <div class="pack-info-top planId${ps.planId}" style="display: none">
                            <div><strong>${ps.internetFlow}</strong>
                                <p>国内流量</p>
                            </div>
                            <div>+</div>
                            <div><strong>${ps.callTime}分钟</strong>
                                <p>省内主叫</p>
                            </div>
                        </div>
                    </c:forEach>
                    <ul class="pack-info-list">
                        <li>
                            <p>国内被叫免费,来电显示免费;</p>
                        </li>
                        <li>
                            <p>套餐外国内主叫0.19元/分钟、国内流量0.29元/M;</p>
                        </li>
                        <li>
                            <p>套餐内流量不结转、不清退、不共享。</p>
                        </li>

                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="pt-35 pb-20" style="margin-top:-10px">
        <button class="btn-orange _goSubmit" otype="button" otitle="H5号卡销售_${conf.confId}-立即申请">立即申请</button>
    </div>
    <form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="feature_str" value="${cond.feature_str}"/>
        <input type="hidden" name="chnlCodeOut" value="${conf.chnlCodeOut}"/>
    </form>
</div>
</body>
<script type="text/javascript">
    //    $(function () {
    //        $('.tabs').tabslet({
    //            active: 2
    //        });
    //    });
    $(function () {
        //默认对第一个为选中
        var firstLi = ".tab-menu li:first";
        $(firstLi).attr("class", "active");
        $("#applyForm input[name='planId']").val($(firstLi + " input[name='planId']").val());
        $("#applyForm input[name='planName']").val($(firstLi + " input[name='planName']").val());
        $(".pack-info-top:first").show();
        //绑定点击事件
        $(".tab-menu li").on("click", function () {
            $(".tab-menu li").attr("class", '');
            $(this).attr("class", 'active');
            var thisPlanId = $(this).find('input[name="planId"]').val();
            $(".pack-info-top").hide();
            $(".planId"+thisPlanId).show();
            $("#applyForm input[name='planId']").val(thisPlanId);
            $("#applyForm input[name='planName']").val($(this).find('input[name="planName"]').val());
        });
        //提交
        $('._goSubmit').on("click", function () {
            $("#applyForm").submit();
        });
    });
</script>
</html>