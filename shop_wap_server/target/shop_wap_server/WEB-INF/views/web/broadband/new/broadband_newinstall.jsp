<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
        <%@include file="/WEB-INF/views/include/head.jsp" %>
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css" />
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css" />
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css" />
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css" />
        <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
        <%@ include file="/WEB-INF/views/include/message.jsp" %>
        <title>湖南移动网上营业厅</title>
        <script type="text/javascript">var ctx = '${ctx}';</script>
    </head>
    <body onload="document.getElementById('test').focus()">
        <div class="top container">
            <div class="header sub-title">
            <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
            <h1>${pageTitle}</h1></div>
        </div>
        <div class="container ">
            <div>
                <c:forEach items="${broadbandPosterList}" var="broadbandItem" varStatus="status">
                <c:if test="${broadbandItem.status == '1'}">
                <dl class="kdrm-list clearfix">
                    <dt>
                        <a href="#"><img src="${broadbandItem.imgUrl}" /></a>
                    </dt>
                    <dd>
                        <p class="kdrm-list-txt01">${broadbandItem.posterTitle}</p>
                        <p class="kdrm-list-txt02">${broadbandItem.intro}</p>
                        <div class="clearfix">
                            <p class="kdrm-list-txt">${broadbandItem.feeInfo}</p>
                            <a class="kdrm-list-btn " href="${broadbandItem.url}">立即办理</a>
                        </div>
                    </dd>
                </dl>
                </c:if>
                </c:forEach>
            </div>
        </div>
    </body>
</html>