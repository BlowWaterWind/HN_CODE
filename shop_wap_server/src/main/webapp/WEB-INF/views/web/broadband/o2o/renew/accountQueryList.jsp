<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    
    <c:if test="${!empty message}">
    	<script type="text/javascript">
    		showAlert('${message}');	
    	</script>
    </c:if>
</head>

<body>
<%--<div class="top container">--%>
    <%--<div class="header sub-title">--%>
        <%--<a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>--%>
        <%--<h1>宽带帐号</h1>--%>
    <%--</div>--%>
<%--</div>--%>
<c:set value="宽带帐号" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<div class="container bg-gray hy-tab">
    <div class="wf-list">
        <div class="wf-ait clearfix">
            <div class="wf-tit">
                <span class="pull-left">宽带账号：${sessionScope.broadbandInfo.broadbandAccount}</span>
                <span class="pull-right font-red">${sessionScope.broadbandInfo.remainingDays}天后过期</span>
            </div>
            <div class="wf-con kd-renew-con">
                <p class="font-gray">套餐类型：<span class="font-3">${sessionScope.broadbandInfo.prodName}</span></p>
                <p class="font-gray">
                    <span class="sus-tit">合同期限：</span>
                    <span class="sus-text font-3">
                        ${sessionScope.broadbandInfo.startTime}~${sessionScope.broadbandInfo.endTime}
                    </span>
                </p>
                <p class="font-gray">
                    <span class="sus-tit">安装地址：</span>
                    <span class="sus-text font-3">${sessionScope.broadbandInfo.installAddress}</span>
                </p>
                <p class="font-gray">姓　　名：<span class="font-3">${sessionScope.broadbandInfo.custName}</span></p>
            </div>
            <div class="zp-btn wf-btn">
                <a id="renewBtn" class="btn btn-blue btn-block" href="javascript:;">立即续费</a>
            </div>
        </div>
    </div>
</div>
<form id="renewForm" action="linkToBroadBandRenew" method="post">
    <input type="hidden" name="broadbandAccount" value="${sessionScope.broadbandInfo.broadbandAccount}"/>
    <input type="hidden" name="cityCode" value="${sessionScope.broadbandInfo.eparchyCode}"/>
    <input type="hidden" name="phoneNum" value="${phoneName}"/>
</form>

<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
    $(function () {
        //续费
        $("#renewBtn").on("click",function () {
            $("#renewForm").submit();
        });
    });

</script>
</body>
</html>