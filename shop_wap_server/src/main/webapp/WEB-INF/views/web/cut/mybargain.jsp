<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
<title>喊朋友来砍价</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
<!-- css -->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/cut/lib/normalize-5.0.0.css"/>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/cut/css/wap.css"/>

<!-- JS -->
<script type="text/javascript" src="${ctxStatic}/cut/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/cut/lib/jquery.vticker-min.js"></script>
<script type="text/javascript" src="${ctxStatic}/cut/js/comm.js"></script>
<script type="text/javascript" src="${ctxStatic}/cut/js/cut.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/hebao.js"></script>
    <script type="text/javascript">
        var ctxStatic = "${ctxStatic}";
        var ctx = "${ctx}";
        $(function(){
            $("#callHelp").bind("click", function() {
                var title = "和包客户端砍价";
                var shareContext = "我正在和包客户端参与呼朋唤友来砍价，求砍，求狠狠砍！";
                var url = "http://wap.hn.10086.cn/shop/bargainInHeBao/initHelpCut?id=${activityCutPrice.id}";
                share(title, url, shareContext);
            });
        });
    </script>
</head>
<body>
<div class="containter hbbargain-bg">
    <a class="btn-rule" href="javascript:void(0);" onclick="showTipDialog('1');">活动规则</a>
    <div class="hbbargain-shop">
        <p class="hbbargain-shop-time">活动时间：<b>即日起-<fmt:formatDate value="${activityGoodsPara.endTime}" pattern="MM.dd"></fmt:formatDate></b></p>
        <p class="hbbargain-shop-txt01">${activityGoodsPara.actTitle}</p>
        <p class="hbbargain-shop-txt02">${activityGoodsPara.rsrv3}</p>
        <img  class="hbbargain-shop-img"   src="${ctxStatic}/${activityGoodsPara.rsrv4}" />
    </div>
    <div class="bargin03">
        <p>${activityCutPrice.formatActNumber}：现价 ￥
            <span id="newPrice">
                <fmt:formatNumber value="${curSalePrice}" pattern="0"></fmt:formatNumber>
            </span></p>
        <div class="bargin-price-progress">
            <div class="bargin-price-progress-in" style="width:${cutPercentage}%;"></div>
        </div>
        <p class="bargin-price-2">
            <span class="bargin-favorite-price">特惠价￥<span id="endPrice">${activityGoodsPara.salePrice}</span></span>
            <span>原价￥<span id="beginPrice">${activityGoodsPara.costPrice}</span></span>
        </p>
        <div class="">
            <c:set var="nowDate" value="<%=System.currentTimeMillis()%>"></c:set>
            <c:choose>
                <c:when test="${activityCutPrice.status == '0'}">
                    <a class="bargin03-btn" href="javascript:void(0);" id="callHelp">喊朋友来砍价</a>
                </c:when>
                <c:when test="${activityCutPrice.status == '1' && (nowDate - activityGoodsPara.endTime.time < 0)}">
                    <a class="bargin03-btn"  href="${ctx}/bargainInHeBao/initGoOrder?bargainCode=${bargainCode}">立即购买手机</a>
                </c:when>
                <c:when test="${activityCutPrice.status == '1' && (nowDate - activityGoodsPara.endTime.time > 0)} ">
                    <a class="bargin03-btn" href="javascript:void(0);">已过期</a>
                </c:when>
                <c:otherwise>
                    <a class="bargin03-btn" href="javascript:void(0);">已购买</a>
                </c:otherwise>
            </c:choose>


        </div></div>
    <div class="bargin04bg clearfix">
        <span class="span-p span-p-txt">购机使用规则：</span>
        <span class="span-p span-p-txt2"><span class="span-p-w">用户手机号</span>砍价金额</span></div>
    <dl class="my-bargain clearfix">
        <dd>
            <p class="my-bargain-txt"><span>1.</span>点击“立即购买手机”后，订单有效期为1小时，1小时内不付款系统将自动取消订单</p>
        </dd>
        <dd>
            <div class="my-content-scroll">
                <div id="my-scroll" class="top-scroll">
                    <ul>
                        <c:choose>
                            <c:when test="${empty activityCutPrice.listActivityCutPriceDetail}">
                                <li><span class="span-p" style="width:100%">还没有好友为你砍价</span></li>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${activityCutPrice.listActivityCutPriceDetail}" var="item">
                                    <li><span class="span-p">${item.formatCutNumber}</span>
                                        <fmt:formatNumber value="${item.cutAmount}" pattern="0.00"></fmt:formatNumber>元</li>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                    </ul>
                </div>
            </div>
        </dd>
    </dl>

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
<jsp:include page="showDaillog.jsp"></jsp:include>
</body>
</html>
