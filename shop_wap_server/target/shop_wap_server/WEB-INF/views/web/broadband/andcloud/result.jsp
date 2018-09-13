<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <c:choose>
        <c:when test="${resultMap.respCode == '0'}">
            <h1>办理成功</h1>
        </c:when>
        <c:otherwise>
            <h1>办理失败</h1>
        </c:otherwise>
    </c:choose>
</div>
<div class="container white-bg">
    <!--订购成功 start-->
    <div class="suess-dan card-suess">
        <!--订购成功提示语 start-->
        <div class="dan-tit">
            <c:choose>
                <c:when test="${resultMap.respCode == '0'}">
                    <span class="fr-cl"><img src="${ctxStatic}/images/suess.png" /></span>
                    <p class="font-blue">业务受理成功！！</p>
                </c:when>
                <c:otherwise>
                    <span class="fr-cl"><img src="${ctxStatic}/images/error.png" /></span>
                    <p class="font-blue">${resultMap.respDesc}，请致电咨询10086！！</p>
                </c:otherwise>
            </c:choose>
        </div>
        <!--订购成功提示语 end-->
    </div>

    <div class="tj-btn tj-box"><a class="btn btn-dl btn-block" href="javascript:void(0);" onclick="window.history.back()">返回</a></div>

</div>
</body>
</html>
