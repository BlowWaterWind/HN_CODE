<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadbandInstall.js"></script>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<%--<div class="top container">--%>
    <%--<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>--%>
        <%--<h1>资源详情</h1>--%>
    <%--</div>--%>
<%--</div>--%>
<c:set value="资源详情" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<form action="${ctx}/broadbandInstall/confirmInstall" id="form1" name="form1" method="post">
    <input type="hidden" id="form1_skuId" name="form1_skuId"  />
    <input type="hidden" id="form1_houseCode" name="form1_houseCode" value="${houseCode}" />
    <input type="hidden" id="form1_addressName" name="form1_addressName" value="${lastAddress}" />
    <input type="hidden" id="form1_packageId" name="form1_packageId"  />
    <input type="hidden" id="form1_productId" name="form1_productId"  />
    <input type="hidden" id="form1_discntCode" name="form1_discntCode"  />
    <input type="hidden" id="form1_hasMbh" name="form1_hasMbh"   />
    <input type="hidden" id="form1_hasBroadband" name="form1_hasBroadband"  />
    <input type="hidden" id="form1_maxWidth" name="form1_maxWidth"  value="${maxWidth}"/>
    <input type="hidden" id="form1_freePort" name="form1_freePort"   value="${freePort}"/>
    <input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode" value="${EPARCHY_CODE}" />
    <input type="hidden" id="form1_coverType" name="form1_coverType" value="${coverType}" />
    <input type="hidden" id="form1_chooseBandWidth" name="form1_chooseBandWidth"  />
</form>

<div class="container bg-gray hy-tab">
    <div class="wf-list tab-con">
        <!--续费号码 start-->
        <div class="wf-ait clearfix">
            <div class="wf-adress">
                <span class="adr-tit">详情地址：</span>
                <div class="adr-fit">${lastAddress}</div>
            </div>
        </div>
        <!--续费号码 end-->
        <!--选择套餐 start-->
        <div class="change-tc">
            <!--续费金额 start-->
            <h2 class="renew-title">请选择推荐套餐</h2>
            <ul class="add_oil select-renew clearfix">
                <c:forEach items="${broadbandItemList}" var="broadbandItem">
                    <li name="li_broadbandItem">
                        <input type="hidden" id="skuId" name="skuId" value="${broadbandItem.goodsSkuId}"/>
                        <input type="hidden" id="goodsId" name="goodsId" value="${broadbandItem.goodsId}"/>
                        <input type="hidden" id="chooseBandWidth" name="chooseBandWidth" value="${broadbandItem.bandWidth}"/>
                        <input type="hidden" id="price" name="price" value="${broadbandItem.price}"/>
                        <input type="hidden" name="desc" value="${broadbandItem.desc}"/>
                            ${broadbandItem.bandWidth}M
                        <p  class="font-gray">${broadbandItem.price}元/${broadbandItem.term}</p>
                        <s></s>
                        <c:if test="${not empty broadbandItem.labelName}">
                            <span class="tip tip-rose">${broadbandItem.labelName}</span>
                        </c:if>
                    </li>
                </c:forEach>
               <%-- <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>
                <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>
                <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>
                <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>
                <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>
                <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>
                <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>
                <li>20M
                    <p class="font-gray">750元/1年</p>
                    <s></s></li>--%>
            </ul>
            <!--充值金额 end-->

            <!--选择套餐 end-->
        </div>
    </div>
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div id="div_settlement" class="container active-fix kd-disabled">
            <div class="fl-left">
                <p class="p1">应付总额：<b class="cl-rose" id="b_total">￥<span id="span_total">0.00</span></b></p>
            </div>
            <%--<div class="fl-right"><a href="javascript:;" id="a_confirmInstall">立即订购</a></div>--%>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
</body>
</html>
