<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：大王卡类型模板--%>
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
    <link href="${ctxStatic}/css/sim/wap.css?v=11" rel="stylesheet">
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
        .float-icon{
            width: 2.573333rem;
            height: 2.493333rem;
            position: fixed;
            right: 0.266667rem;
            top: 10%;
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
    <div class="btn-wrap" id="mallDiv">
        <button class="btn btn-gold local-backcolor _goSubmit" otype="button" otitle="H5号卡销售_${conf.confId}-立即申请">立即申请</button>
    </div>

    <div class="pd10 hk-list" id="o2oDiv" style="display: none">
        <div class="title">
            <span class="text">业务办理</span>
        </div>
        <p class="pd10">方式1：
            <button class="btn btn-gold local-backcolor _goSubmit" otype="button" otitle="H5号卡销售_${conf.confId}-立即申请">立即申请</button>
        </p>

        <%--<p class="pd10">方式2：对多个号码进行业务办理请点击<a href="##" onclick="batchShare();" class="font-blue" style="padding-right:10px"  otitle="批量办理" otype="button" oarea="批量办理">批量办理</a></p>--%>
        <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="##" onclick="shareSim();" otitle="o2oel分享" otype="button" oarea="o2oel分享">
            <img src="${ctxStatic}/images/o2osim/fx-icon.png" style="width:0.8rem;margin-left:0.2rem;"></a>
        </p>
    </div>

     <%--<c:if test="${isAllowRecmd == '1'}">
         <div style="color: red;padding-left: 10px;" id="tjlabel">推荐送流量，最高可获赠15G</div>
     </c:if>--%>
    <img src="${myTfsUrl}${conf.prodDetlImg}">
    <img src="${myTfsUrl}${conf.feeDetlImg}" id="zfxq">
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

    <div id="tips-modal-1" class="mask-layer">
        <div class="modal tips-modal">
            <p>您好！您当前为非移动大王卡品牌，</p>
            <p>请办理移动大王卡激活后再申请。</p>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-1')">取&nbsp;&nbsp;消</button>
                <%--<button id="sureApply" otitle="H5号卡销售_${conf.confId}-立即申请 >确&nbsp;&nbsp;认</button>--%>
                <button otype="button" otitle="H5号卡销售_${conf.confId}-确认" id="sureApply1">确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
    <div id="tips-modal-2" class="mask-layer">
        <div class="modal tips-modal">
            <p>您好!您当前可用话费余额不足200元，</p>
            <p>不满足升级条件，请充值后再办理。</p>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-2')">取&nbsp;&nbsp;消</button>
                <button otype="button" id="sureApply2">确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
    <div id="tips-modal-3" class="mask-layer">
        <div class="modal tips-modal">
            <p>您正在升级1元1天不限量优惠，</p>
            <p>申请立即生效，有效期为6个月。</p>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-3')">取&nbsp;&nbsp;消</button>
                <button otype="button" id="sureApply3">确&nbsp;&nbsp;认</button>
            </div>
        </div>
    </div>
    <div id="tips-modal-4" class="mask-layer">
        <div class="modal tips-modal">
            <p>办理成功！</p>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-4')" >关&nbsp;&nbsp;闭</button>
            </div>
        </div>
    </div>
    <div id="tips-modal-5" class="mask-layer">
        <div class="modal tips-modal">
            <p>办理失败！</p>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-5')">关&nbsp;&nbsp;闭</button>
            </div>
        </div>
    </div>
    <div id="tips-modal-7" class="mask-layer">
        <div class="modal tips-modal">
            <p>已办理，无需重复办理!</p>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-7')">关&nbsp;&nbsp;闭</button>
            </div>
        </div>
    </div>
    <div id="tips-modal-6" class="mask-layer">
        <div class="modal tips-modal">
            <p>请用手机号码登录！</p>
            <div class="Grid -center tips-modal-btn">
                <button onclick="toggleModal('tips-modal-6')">关&nbsp;&nbsp;闭</button>
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

<%--<script type="text/javascript" src="${ctxStatic}/js/goods/sim/updatePackage.js?v=4"></script>--%>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=5"></script>
<script type="text/javascript" charset="utf-8" src="${ctxStatic}/js/ajaxSetup.js"></script>

</html>