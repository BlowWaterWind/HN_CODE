<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadbandSpeedUp.js"></script>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带提速</h1>
        <a href="javascript:;" class="icon-right icon-add"></a>
    </div>
</div>
<div class="container bg-gray hy-tab">
    <div class="wf-list">
        <form action="${ctx}/broadbandSpeedUp/confirmAccount" id="form1" name="form1" method="post" >
            <input type="hidden" id="broadbandAccount" name="bandAccount" value=""/>
            <input type="hidden" id="oldBandWidth" name="oldBandWidth" value=""/>
            <input type="hidden" name="serialNumber" value="${serialNumber}"/>
<c:forEach items="${speedUpList}" var="record" varStatus="status">
    <div class="wf-ait clearfix">
        <div class="wf-tit"><span class="pull-left">宽带账号：${record.bandAccount}</span><c:if test="${record.oldBandWidth lt 100}"><span class="pull-right font-red">可提速</span></c:if></div>
        <div class="wf-con">
            <p class="font-gray">套餐名称：<span class="font-3">${record.productInfo}</span></p>
            <p class="font-gray">合约期限：<span class="font-3"><fmt:formatDate value="${record.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>~ <fmt:formatDate value="${record.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
            <p class="font-gray">安装地址：<span class="font-3">${record.address}</span></p>
        </div>
        <div class="zp-btn wf-btn"><a class="btn btn-blue btn-block" href="javascript:comfirAccount('${record.bandAccount}','${record.oldBandWidth}');" otitle="立即提速"  otype="button" oarea="宽带提速">立即提速</a> </div>
    </div>
</c:forEach>
            </form>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript"  src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>
