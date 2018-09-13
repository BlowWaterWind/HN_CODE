<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：O2O渠道线下推广模板，号段卡--%>
<html>
<head>
    <%--<c:set var="tfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <%--插码相关：test--%>
    <meta name="WT.si_n" content="H5号卡销售_${conf.confId}" >
    <meta name="WT.si_x" content="售卡申请_${conf.confId}" >
    <title>在线售卡-首页</title>
    <link href="${ctxStatic}/css/sim/wap-simh5onlineV2.css?v=10" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/wap-sim-recmd.css?v=10" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/wap.css?v=10" rel="stylesheet">
        <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">
        <link href="${ctxStatic}/css/sim/dropload/dropload.css?v=3" rel="stylesheet">
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
    </style>
</head>
<body>
<div class="container bg-02">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}">
    </div>
    <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}">
    <c:forEach items="${plans}" var="ps">
    <c:set var="info" value="${fn:split(ps.planName,\"套餐\")}"/>
    <div class="king-card">
        <img src="${myTfsUrl}${conf.slctedNoImg}">
        <div class="king-card-text">
            <h3>${confPlansMap[ps.planId]}</h3>
            <%--<h3>${info[0]}</h3>--%>
            <c:if test="${ps.otherBusiness != null}">
                <p> ${ps.otherBusiness}元/月</p>
                <span>原价：<fmt:formatNumber value="${ps.planFee/100}" pattern="#" type="number"/>元/月</span>
            </c:if>
            <c:if test="${ps.otherBusiness == null}">
                <p><fmt:formatNumber value="${ps.planFee/100}" pattern="#" type="number"/>元/月</p>
            </c:if>
            <input type="hidden" name="planId" value="${ps.planId}">
            <input type="hidden" name="planName" value="${ps.planName}">
        </div>
    </div>
    </c:forEach>
    <div class="btn-wrap">
        <button class="btn btn-gold local-backcolor _goSubmit" otype="button" otitle="H5号卡销售_${conf.confId}-立即申请">立即办卡</button>
    </div>


    <img src="${myTfsUrl}${conf.prodDetlImg}">
    <img src="${myTfsUrl}${conf.feeDetlImg}">
    <form id="applyForm" action="${ctx}o2oSimPreBuy/realNameRegistry" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="reCmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
        <input type="hidden" name="shopId" value="${shopId}"/>
    </form>
</div>


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
</body>
<script type="text/javascript">
    $(function () {
        var slctImg = '${myTfsUrl}${conf.slctedImg}';
        var slctNoImg = '${myTfsUrl}${conf.slctedNoImg}';
        //默认对第一个为选中
        var firstLi = ".king-card:first";
        $(firstLi).find("img").attr("src",slctImg);
        $("#applyForm input[name='planId']").val($(firstLi+" input[name='planId']").val());
        $("#applyForm input[name='planName']").val($(firstLi+" input[name='planName']").val());
        //绑定点击事件
        $(".container .king-card").on("click",function () {
            $(".king-card img").attr("src",slctNoImg);
            $(this).find("img").attr("src",slctImg);
            $("#applyForm input[name='planId']").val($(this).find('input[name="planId"]').val());
            $("#applyForm input[name='planName']").val($(this).find('input[name="planName"]').val());
        });
        //提交
        $('._goSubmit').on("click",function() {
            $("#applyForm").submit();
        });
        //确认1
        $('#sureApply1').on("click",function() {
            $("#applyForm").submit();
        });
        //确认2
        $('#sureApply2').on("click",function() {
            window.location.href = "http://dx.10086.cn/UYjYbeq";
        });

    });
    var imgUrl="/res/img/${conf.titleImg}";
    var ctx="${ctx}";
</script>

<script type="text/javascript" src="${ctxStatic}/js/goods/sim/updatePackage.js?v=3"></script>

</html>