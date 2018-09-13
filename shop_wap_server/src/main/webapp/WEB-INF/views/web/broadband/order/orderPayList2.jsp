<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KDZF" />
    <meta name="WT.si_x" content="20" />
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/taglib.jsp"%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta http-equiv="Cache-Control" content="max-age=3600;must-revalidate" />
    <meta name="keywords" content="中国移动网上商城,手机,值得买,可靠,质量好,移动合约机,买手机送话费,4G手机,手机最新报价,苹果,华为,魅族" />
    <meta name="description" content="中国移动网上商城,提供最新最全的移动合约机、4G手机,买手机送话费,安全可靠,100%正品保证,让您足不出户,享受便捷移动服务！" />
    <script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js?v=625" type="text/javascript"></script>
    <script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
        <%--插码相关--%>
        <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>

        <script type="text/javascript" >
        var ctx="${ctx}",gUrl="${gUrl}",sUrl="${sUrl}",tfsUrl="${tfsUrl}",ctxStatic = "${ctxStatic}",contextPath = "${contextPath}";
    </script>

    <%@ include file="/WEB-INF/views/include/messageNew.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/broadBand/wap-order-new.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/js/datepicker/datepicker.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/swiper/swiper.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<div class="container">
    <input type="hidden" name="flag" value="${flag}" id="flag"/>
    <ul class="tabmenu">
        <li class="tabmenu-item" id="all"><a href="${ctx}/broadbandOrder/queryOrderList?flag=all">全部订单</a></li>
        <li class="tabmenu-item" id="willPay"><a href="${ctx}/broadbandOrder/queryPayOrderList?flag=willPay">支付相关订单</a></li>
    </ul>

    <div class="lists">
    <c:forEach items="${broadbandOrderList}" var="broadbandOrder">
        <div class="list order-list">
            <div class="order-list_hd">
                <div class="left"><i class="icon-shop"></i>
                    <h3 class="shop-name">湖南移动营业厅</h3><i class="caret-right"></i>
                </div>
                <div class="right"><span class="txt-red">${broadbandOrder.orderStatus.orderStatusName}</span></div>
            </div>
            <div class="order-list_bd">
                <div class="thumb"><img src="${ctxStatic}/demoimages/kdtu.png" alt="宽带"/></div>
                <div class="con">
                    <div class="row">
                        <div class="title">${broadbandOrder.detailList[0].goodsName}</div>
                        <div class="price">¥<fmt:formatNumber value="${broadbandOrder.orderSubPayAmount/100}" pattern="#.##"/></div>
                    </div>
                    <div class="row">
                        <div class="desc">套餐分类：${broadbandOrder.orderType.orderTypeName}</div>
                        <div class="number">x1</div>
                    </div>
                </div>
            </div>
            <div class="total">共1件商品 合计：<span class="price">¥<fmt:formatNumber value="${broadbandOrder.orderSubPayAmount/100}" pattern="#.##"/></span></div>
            <div class="btn-wrap Grid -right">
                <c:if test="${'0' eq broadbandOrder.orderSubPayStatus and '12' ne broadbandOrder.orderStatus.orderStatusId}">
                </c:if>
                <c:if test="${'1' eq broadbandOrder.orderSubPayStatus }">
                    <a href="${ctx}/broadbandOrder/payDetail?orderId=${broadbandOrder.orderId}" class="btn btn-default">支付明细</a>
                </c:if>
            </div>
        </div>
    </c:forEach>

    <!--暂无订单 strat-->
    <c:if test="${ empty broadbandOrderList}">
        <div class="no-data"><img src="${ctxStatic}/images/no-data.png" alt="无数据"/>
            <div class="desc">暂无记录哦</div>
        </div>
        <%--<div class="no-orders">
            <img src="${ctxStatic}/images/fa_icon.png" />
            <p>暂未找到相关订单</p>
        </div>--%>
    </c:if>
    <!--暂无订单 end-->
    </div>
</div>

<script type="text/javascript" src="${ctxStatic}/js/busi/lib/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/swiper/swiper.jquery.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-extend.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/datetime-picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/busi/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/datepicker/datepicker.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShare.js?v=201805"></script>

<script type="text/javascript">
    var ctx="${ctx}";
    var ctxStatic="${ctxStatic}";
    var tfsUrl="${tfsUrl}";
</script>
<script>
    $(function() {
        var flag = $("#flag").val();
        $("#"+flag).addClass("active");
    });

    function orderPay(orderSubId){
        window.location.href=ctx+'/broadbandOrder/payList?orderSubId='+orderSubId;
    }
</script>
</body>
</html>
