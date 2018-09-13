<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>预约安装</h1>
    </div>
</div>--%>
<c:set value="预约安装" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <span class="suess-img"><img src="${ctxStatic}/images/suess.png"></span><!--办理失败替换error.png-->
        <div class="suess-list text-center">
            你的信息提交成功，24小时内客户会<br>确认安装信息，请您保持手机畅通。
        </div>
    </div>
</div>
<script type="text/javascript"  src="js/rem.js"></script>
</body>
</html>
