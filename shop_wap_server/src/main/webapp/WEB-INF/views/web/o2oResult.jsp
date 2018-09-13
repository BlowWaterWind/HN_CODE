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
        <h1>办理成功</h1>
        <a href="#" class="icon-right icon-lr02"></a>
        <div class="modal-bg"></div>
    </div>
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
                    <p class="font-blue">业务受理失败，${resultMap.respDesc},请致电咨询10086！！</p>
                </c:otherwise>
            </c:choose>
        </div>
        <!--订购成功提示语 end-->
    </div>

    <div class="tj-btn tj-box"><a class="btn btn-dl btn-block" href="javascript:void(0);" onclick="window.history.back()">返回</a></div>

</div>
</body>
</html>
