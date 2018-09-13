<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <title>湖南移动网上营业厅</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/renew.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<c:if test="${!empty message}">
    <script type="text/javascript">
        showAlert('${message}');
    </script>
</c:if>
<body>
<%--<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>帐号查询</h1>
    </div>
</div>--%>
<c:set value="帐号查询" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<div class="container bg-gray hy-tab">
    <form id="broadBandQueryForm" action="linkToAccountQueryList" method="post">
        <div class="share-content rearch-con">
            <span class="cx-tit">请查询需要续费的宽带:</span>
            <div class="con-changae">
                <label><input type="radio" name="numType" value="broadBandAccount" checked="checked"/>宽带账号查询</label>
            </div>
            <div><input id="num" type="text" name="num" class="form-control" placeholder="请输入手机号" /></div>
            <div class="change-city">
                <label>选择地市：</label>
                <div class="change-box">
                <span class="td-fr"><i class="select-arrow"></i>
                     <select id="cityCode" name="cityCode" class="form-control">
                        <c:forEach items="${cityList}" var="city">
                            <option value="${city.eparchyCode}">${city.orgName}</option>
                        </c:forEach>
                     </select>
                </span>
                </div>
            </div>
            <div class="kdcenter-renew-phone-search">
                <a id="queryBtn" class="btn btn-bd btn-icon btn-block" href="javascript:;"  onclick="queryAccount()">查询</a>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">

</script>
</body>
</html>