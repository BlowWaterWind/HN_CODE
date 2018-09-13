<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/8/9
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <%--插码相关：test--%>
    <meta name="WT.si_n" content="">
    <meta name="WT.si_x" content="">
    <meta name="WT.si_s" content="">

    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <title>号码详情</title>
    <link href="${ctxStatic}/css/sim/simgroup/o2o.sim.group.css?v=1" rel="stylesheet">


    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.jquery.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>

    <link href="${ctxStatic}/css/sim/wap-simcommon.css?v=1" rel="stylesheet"><!--这个页面选号的弹出框-->
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>

    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>


</head>
<body>
<div class="container">
    <div class="common-navbar-wrap">
        <div class="common-navbar ">
            <div class="navbar-left">
                <a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
            </div>
            <h3 class="navbar-title">号码详情</h3>
        </div>
    </div>

    <p class="p10 bg-white fs28">${conf.cardTypeName}，<span class="font-rose fs28">${conf.planDesc}</span></p>
    <div class=" p10">
        <div class="pl20 ">
            <p>已选号码：<span>${mobile}</span><span class="font-gray">（${cityName}）</span></p>
        </div>
    </div>
    <div class="bg-white">
        <div class="pl20">
            <div class="flex-h pt10 lh50">
                <label class="fs28">卡类型：</label>
                <div class="common-select flex1 ">
                    <a class="active">${conf.cardTypeName}</a>
                </div>
            </div>
            <p class="card-p-info">免费配送SIM卡，自行进行线上开户激活</p>
            <div class="flex-h pt10 lh50">
                <label class="fs28"><label class="fs28">套餐选择:</label>
                    <div class="common-select flex1" id="plans">
                        <c:forEach items="${plans}" var="plan">
                            <a value="${plan.planId}">${plan.planName}</a>
                        </c:forEach>
                    </div>
                </label>
            </div>
        </div>
        <div class="common-tabcontent">
            <div id="ProIntro">
                <img src="${myTfsUrl}${conf.prodDetlImg}">
                <img src="${myTfsUrl}${conf.feeDetlImg}">
            </div>
        </div>
    </div>
    <form method="post" id="applyForm" action="toConfirm">
        <input type="hidden" name="planId" id="planId" value="${plans[0].planId}">
        <input type="hidden" name="mobile" id="phone" value="${mobile}">
        <input type="hidden" name="cityCode" value="${city}">
        <input type="hidden" name="recmdCode" id="recmdCode" value="${recmdCode}">
        <input type="hidden" name="confId" id="confId" value="${confId}">
        <input type="hidden" value="${conf.cardTypeName}" id="cardTypeName">

    </form>
    <div class="common-footer-wrap">
        <div class="common-footer-fix">
            <a class="footer-btn-rose" id="collect">收藏</a>
            <a class="footer-btn-blue" id="toBuy" step="20">马上购买</a>
        </div>

    </div>
</div>

<%-- 公用弹窗 --%>
<div id="sorry-modal" class="mask-layer">
    <div class="modal small-modal">
        <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/simgroup/collectNum.js?v=1"></script>
<script type="text/javascript" src="${ctxStatic}/js/wb/public.js?v=1"></script>
<script type="text/javascript">
        $(function () {
            //页面初始化planId需要选择,直接填confId
            getConfId("${conf.confId}","${conf.confId}");

            var clientType = isAppClient();
            if(clientType=="hezhanggui"){
                $(".common-navbar-wrap").show();
            }else if(clientType=="html5" || clientType=="android" || clientType=="ios"){
                //去除头和分享(电脑/安卓/ios浏览器打开不分享但是显示头)
                $(".navbar-right").remove();
            }else{
                //微信打开调用微信的分享,并且不显示头
                $(".common-navbar-wrap").remove();
            }

            var cartItem = getCookieStatic('cartItem');
            if(cartItem != null && cartItem.indexOf(GetQueryString("mobile"))!=-1){
                $("#collect").removeClass("footer-btn-blue").addClass("footer-btn-gray");
                $("#collect").html("取消收藏");
            }


            $("#collect").click(function(){
                var num = GetQueryString("mobile");
                var city = GetQueryString("city");
                var recmdCode = GetQueryString("recmdCode");
                var confId = GetQueryString("confId");
                var cardTypeName = $("#cardTypeName").val();
                if ($(this).hasClass('footer-btn-rose')) {//加入收藏
                    $(this).attr("otitle","取消收藏_"+num);
                    //获取链接中的参数
                    $(this).removeClass('footer-btn-rose').addClass("footer-btn-gray");
                    addCart(num,city,recmdCode,confId,cardTypeName);
                    $(this).html("取消收藏");
                } else {
                    $(this).attr("otitle","加入收藏_"+num);
                    $(this).removeClass('footer-btn-gray').addClass("footer-btn-rose");
                    delCart(num);
                    $(this).html("加入收藏");
                }
            });


            //默认选中第一个套餐
            $("#plans").children(":first").addClass('active');

            $('.common-select a').click(function () {
                var planId = $(this).attr("value");
                $("#planId").val(planId);
                $(this).addClass('active').siblings().removeClass('active');
            });
            $('.btnXieyi').bind('click', function () {
                $('#gz-modal').modal();

            });
        });


    //提交
    $("#toBuy").click(function(){
        //点击购买插码 21 点击办理
        var step = $(this).attr("step");
        trace(step);
        $("#applyForm").submit();
    });
</script>
</html>
