<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">

<title>首页</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
<!-- css -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/cut/lib/normalize-5.0.0.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/cut/css/wap.css"/>
<!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/cut/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/cut/lib/jquery.vticker-min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/cut/js/comm.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/cut/js/cut.js?version=2"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js?version=1"></script>
    <script type="text/javascript" src="${ctxStatic}/js/hebao.js?version=2"></script>
    <script type="text/javascript">
        var ctxStatic = "${ctxStatic}";
        var ctx = "${ctx}";
        //var isShare = "${isShare}";
        var isBindCard = "${isBindCard}";
        var actCode = '${activityGoodsPara.actCode}';
        $(function(){
            $("#startCutCtrl").bind("click", funJoinCut);
            /*$("#shareCtrl").bind("click", function() {
                fnShareConfirm();
            });*/

          /*  if ("true" != isShare) {
                showErrorDialog("11");
            } else*/
            if ("true" != isBindCard) {
                showErrorDialog("2");
            }
        });

        function showGoodsDetail(code) {
            var $tip = $("#tip_" + code);
            if ($tip.attr("init") != "true") {
                var $imgs = $tip.find(".popup-product-img img");
                var len = $imgs.length;
                for(var i = 0; i < len; i++) {
                    var $img = $($imgs.get(i));
                    $img.attr("src", $img.attr("destsrc"));
                }
                $tip.attr("init", "true");
            }
            showTipDialog(code);
        }
    </script>

</head>
<body>
<div class="containter hbbargain-bg">
    <a class="btn-rule" href="javascript:void(0);" onclick="showTipDialog('1');">活动规则</a>
    <div class="hbbargain-shop">
        <p class="hbbargain-shop-time">活动时间：<b>
            <c:set var="nowDate" value="<%=System.currentTimeMillis()%>"></c:set>
            <c:choose>
                <c:when test="${nowDate - activityGoodsPara.startTime.time < 0}">
                    <fmt:formatDate value="${activityGoodsPara.startTime}" pattern="MM.dd"></fmt:formatDate>-
                </c:when>
                <c:otherwise>
                    即日起-
                </c:otherwise>
            </c:choose>
            <fmt:formatDate value="${activityGoodsPara.endTime}" pattern="MM.dd"></fmt:formatDate></b></p>
        <p class="hbbargain-shop-txt01">${activityGoodsPara.actTitle}</p>
        <p class="hbbargain-shop-txt02">${activityGoodsPara.rsrv3}</p>
        <img  class="hbbargain-shop-img"  onclick="showGoodsDetail('${activityGoodsPara.actCode}');"  src="${ctxStatic}/${activityGoodsPara.rsrv4}" />
    </div>
    <div class="tc"><a class="hbbargain-btn" href="javascript:void(0);" id="startCutCtrl" bargainCode="${activityGoodsPara.actCode}">我要发起砍价</a> </div>
    <ul class="hbbargain-progress  clearfix">
        <li><span class="hbbargain-progress-btn">发起<br/>活动</span></li>
        <li><span class="hbbargain-progress-btn">喊朋友<br/>来砍价</span></li>
        <li><span class="hbbargain-progress-btn">砍到<br/>优惠价</span></li>
        <li><span class="hbbargain-progress-btn">优惠<br/>购机</span></li>
    </ul>
    <div class="bargin02bg">优惠购机随心挑</div>
    <ul class="hbbargain-list-phone clearfix">

        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">
            <img src="${ctxStatic}/cut/images/bargain/phone01.png"/> </a>
            <div>
                <a class="hbbargain-list-text01"
                       href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">中国移动A3</a>
                <a class="hbbargain-list-text02"
                   href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">1G+8G<br/>双卡双待<br/>5.0英寸高清屏</a>
                <a class="hbbargain-list-text03"
                   href="http://touch.10086.cn/goods/731_731_1042578_1036440.html">优惠价：￥<b>499</b>元</a></div>
        </li>

        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282"><img src="${ctxStatic}/cut/images/bargain/phone02.png"/> </a>
            <div>
                <a class="hbbargain-list-text01" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282">畅享7Plus</a>
                <a class="hbbargain-list-text02" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282">性能强劲<br/>4000Ah大电池<br/>.5英寸高大屏</a>
                <a class="hbbargain-list-text03" href="http://touch.10086.cn/goods/731_738_1043282_1037272.html?wt.ac=ad_search_res_731_1043282">优惠价：￥<b>1499</b>元</a></div>
        </li>
        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html"><img src="${ctxStatic}/cut/images/bargain/phone03.png"/> </a>
            <div>
                <a class="hbbargain-list-text01" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html">魅蓝U20</a>
                <a class="hbbargain-list-text02" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html">2G + 16G<br/>5.5英寸高清屏<br/>1300万像素摄像头 指纹识别</a>
                <a class="hbbargain-list-text03" href="http://touch.10086.cn/goods/731_731_1040016_1033776.html">优惠价：￥<b>799</b>元</a></div>
        </li>
        <li><a class="hbbargain-list-img" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html"><img src="${ctxStatic}/cut/images/bargain/phone04.png"/> </a>
            <div><a class="hbbargain-list-text01" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html">畅享6S</a>
                <a class="hbbargain-list-text02" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html">3G + 32G<br/>轻薄金属机身<br/>1300万像素摄像头</a>
                <a class="hbbargain-list-text03" href="http://touch.10086.cn/goods/731_731_1040877_1035191.html">优惠价：￥<b>1299</b>元</a></div>
        </li>
    </ul>
</div>

<%--<div class="bargin-popup popup-product-more" id="tip_${activityGoodsPara.actCode}">
    <a class="popup-close" href="javascript:void(0);"><img  src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="popup-product-img">
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif"  destsrc="${ctxStatic}/cut/images/bargain/honorv9_01.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_02.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_03.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_04.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_05.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_06.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_07.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_08.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_09.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_10.jpg" />
        <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/honorv9_11.jpg" />
    </div>
</div>--%>

<c:if test="${fn:length(activityGoodsPara.remark) > 0}">
<div class="bargin-popup popup-product-more" id="tip_${activityGoodsPara.actCode}">
    <a class="popup-close" href="javascript:void(0);"><img  src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="popup-product-img">

        <c:set var="imgs" value="${fn:split(activityGoodsPara.remark, '|')}"></c:set>

        <c:forEach var="img" items="${imgs}">
            <img src="${ctxStatic}/cut/images/bargain/loading4.gif" destsrc="${ctxStatic}/cut/images/bargain/${img}" />
        </c:forEach>
    </div>
</div>
</c:if>

<jsp:include page="showDaillog.jsp"></jsp:include>

</body>
</html>
