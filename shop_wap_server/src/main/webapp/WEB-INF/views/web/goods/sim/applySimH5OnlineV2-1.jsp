<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：不限量模板--%>
<html>
<head>
    <%--<c:set var="mymyTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>--%>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <%--插码相关--%>
    <%--meta name="WT.si_n" content="H5号卡销售_${conf.confId}" >
    <meta name="WT.si_x" content="售卡申请_${conf.confId}" >--%>


    <META name="WT.si_n" content="">
    <META name="WT.si_s" content="">
    <META name="WT.si_x" content="">
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <Meta itemprop="name" content="${conf.cardTypeName}">
    <Meta itemprop="description" content="${conf.planSecondDesc}">
    <Meta itemprop="image" name="image" content="">

        <title>在线售卡-首页</title>
    <link href="${ctxStatic}/css/sim/wap-simh5onlineV2.css?v=10" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>

    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
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
            border-color: transparent transparent transparent ${conf.styleColor};
        }
        .taocan .taocan-foot .right-flag{
            content: "";
            width: 0;
            height: 0;
            border-style: solid;
            border-width: .16rem .213333rem .16rem 0;
            border-color: transparent ${conf.styleColor} transparent transparent;
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
<div class="container bg-01">
    <div class="header">
        <img src="${myTfsUrl}${conf.titleImg}">
    </div>
    <div class="taocan">
        <div class="taocan-top">
            <h3>${conf.planDesc}</h3>
            <input type="hidden" id="cardTypeName" value="${conf.cardTypeName}"><!--号卡名称-->
            <input type="hidden" value="${conf.planSecondDesc}" id="planDesc"><!--号卡描述-->
            <input type="hidden" value="" id="titleImg"><!--分享顶部图片-->
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
                            <p> <fmt:formatNumber value="${ps.planFee/100}" pattern="#" type="number"/>元/月</p>
                        </c:if>
                        <input type="hidden" name="planId" value="${ps.planId}">
                        <input type="hidden" name="planName" value="${ps.planName}">
                    </div>
                </li>
                </c:forEach>
            </ul>
            <div class="pd10 hk-list" id="o2oDiv" style="display: none">
                <div class="title">
                    <span class="text">业务办理</span>
                </div>
                <p class="pd10">方式1：
                    <a href="javascript:void(0)" class="btn btn-blue _goSubmit" style="background-color:${conf.styleColor};text-align:center"  otitle="H5号卡销售_${conf.confId}-立即办理">立即办理</a>
                </p>

                <%--<p class="pd10">方式2：对多个号码进行业务办理请点击 <a href="##" onclick="batchShare();" class="font-blue" style="padding-right:10px"  otitle="批量办理" otype="button" oarea="批量办理">批量办理</a></p>--%>
                <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="##" onclick="shareSim();" otitle="o2oel分享" otype="button" oarea="o2oel分享">
                    <img src="${ctxStatic}/images/o2osim/fx-icon.png" style="width:0.8rem;margin-left:0.2rem;"></a>
                </p>
            </div>
            <div class="btn-wrap" id="mallDiv">
                <button class="btn btn-blue _goSubmit" style="background-color:${conf.styleColor};" otype="button" otitle="H5号卡销售_${conf.confId}-立即申请">立即申请</button>
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
    <form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <input type="hidden" name="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
        <input type="hidden" name="planId"/>
        <input type="hidden" name="planName"/>
        <input type="hidden" name="CHANID" value="${CHANID}"/>
    </form>
</div>
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
        /*拿取url中的WT.at存入cookie*/
        getToCookie();
        /*微信QQ分享参数*/
        try{
            /**
             * 获取cookie中的参数
             */
            var wtAc = $.cookie("WT.ac");
            if($.cookie("WT.ac")!=undefined) {
                document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
            }
            var img = "${conf.titleImg}";
            var suffix = img.split('.')[1];
            var path = "/res/img/"+"${conf.titleImg}"+"_100x100."+suffix;
            $("#titleImg").val(path);
            document.getElementsByTagName('meta')['image'].content = path;
        }catch (e){
            console.log(e);
        }


        /*给页面WT.si_n 赋初始值*/
        $(document).ready(function(){
            getConfId1(this,"${conf.confId}");
        });
        var slctImg = '${myTfsUrl}${conf.slctedImg}';
        var slctNoImg = '${myTfsUrl}${conf.slctedNoImg}';
        var slctNoImgHtml = "<img src="+slctNoImg+">";
        //默认对第一个为选中
        var firstLi = ".tcard-list li:first";
        $(".tcard-list li").append(slctNoImgHtml);
        $(firstLi).find("img").attr("src",slctImg);
        $("#applyForm input[name='planId']").val($(firstLi+" input[name='planId']").val());
        $("#applyForm input[name='planName']").val($(firstLi+" input[name='planName']").val());
        //绑定点击事件
        $(".tcard-list li").on("click",function () {
            $("#cardTypeName").val($(this).attr("cardTypeName"));
            $(".tcard-list li").find('img').attr("src",slctNoImg);
            $(this).find('img').attr("src",slctImg);
            $("#applyForm input[name='planId']").val($(this).find('input[name="planId"]').val());
            $("#applyForm input[name='planName']").val($(this).find('input[name="planName"]').val());

            /*给页面WT.si_n 赋值*/
            try{
                getConfId1(this,"${conf.confId}");
            }catch (e){
                console.log(e);
            }

        });
        //提交
        $('._goSubmit').on("click",function() {
            try{
                quote("${conf.confId}","${plans[0].planId}");
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                var sis = document.getElementsByTagName('meta')['WT.si_s'].content;
                Webtrends.multiTrack({
                    argsa: ["WT.si_n",sin,
                        "WT.si_s",sis,
                        "WT.si_x","20"],
                    delayTime: 100
                })

            }catch (e){
                console.log(e);
            }
            $("#applyForm").submit();
        });
    });

    var imgUrl="/res/img/${conf.titleImg}";
</script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=6"></script>
<%--微信QQ分享--%>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShareCommon.js?v=201805"></script>
</html>