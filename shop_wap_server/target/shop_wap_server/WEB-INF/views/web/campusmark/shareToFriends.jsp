<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<html class="bg-yellow">
<head>
    <meta charset="UTF-8">
    <title>湖南移动商城</title>
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <meta name="WT.si_n" content="GJJ" />
    <meta name="WT.si_x" content="20" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/swiper.min.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/wap.css" />
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/swiper.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
</head>


<body class="bg-yellow">
<div class="container">
    <div class="share01">
        <p>嗨~朋友，我参加了移动校园购机节，<b>赢了<span>${quan}</span>元</b>直降券，墙裂安利你哦！</p>
    </div>
    <div class="ma-bg">

        <p>扫码参与 赢购机直降优惠</p>
        <%--<img src="${myTfsUrl}${orderRecmd.recmdUserQrcode}" class="qr-img" >--%>
        <div class="ma-bg-box"><img src="${myTfsUrl}${qrCodePath}" alt=""></div>


    </div>



    <div class="common-flex">
        <%--<a class="common-btn-big2" id="" href="${ctx}campusmark/shareToFriendsNoSave?quan=${quan}"><img src="${ctxStatic}/images/campusmark/btn-sharefirnd2.png" alt=""></a>--%>
        <a class="common-btn-big2" id="btnSare" href="javascript:;"><img src="${ctxStatic}/images/campusmark/btn-sharefirnd2.png" alt=""></a>
    </div>
    <p class="share-info">长按图片保存到相册</p>

    <div class=""></div>

</div>

<%--下面的用于微信分享--%>
<input type="hidden" value="http://wap.hn.10086.cn/res/img/T1DNKTB5ET1R4cSCrK.png" id="titleImg">
<input type="hidden" value="http://wap.hn.10086.cn/shop/campusmark/toIndex" id="recmdUserLink">
<input type="hidden" value="" id="cardTypeName">
<input type="hidden" value="2018校园购机节" id="planDesc">
</div>
<div class="mask"></div>

<div class=" js-popup2 weixin-tip" >
    <div class="container-box"><img src="${ctxStatic}/images/campusmark/fx.png" /></div>
</div>
<script type="text/javascript">
    $(window).on("load", function() {
        var winHeight = $(window).height();

        function is_weixin() {
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                return true;
            } else {
                return false;
            }
        }
        var isWeixin = is_weixin();


        if (isWeixin) {
            $(".weixin-tip").css("height", winHeight);
            $("#btnSare").bind('click', function() {
                $(".weixin-tip").show();
            });


        }
    })
</script>


</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/campusmark/wxShare.js?v=201808"></script>
</html>
