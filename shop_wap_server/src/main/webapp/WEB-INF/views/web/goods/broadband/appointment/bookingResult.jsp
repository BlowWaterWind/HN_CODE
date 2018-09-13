<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="WT.si_n" content="和家庭或单宽带" />
    <c:choose> 
    <c:when test="${resultMap.respCode=='0'}">
        <meta name="WT.si_x" content="申请成功" />
    </c:when>
    <c:otherwise>
        <meta name="WT.si_x" content="申请失败" />
        <meta name="WT.err_code" content="${resultMap.respDesc}" />
    </c:otherwise>
    </c:choose>
    <meta name="WT.pn_sku" content="业务详情" />
    <meta name="WT.pn" content="单宽带或和家庭" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>预约安装</h1>
    </div>
</div>--%>
<c:set value="预约安装" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <c:choose>
        		<c:when test="${resultMap.respCode=='0'}">
        		<span class="suess-img"><img src="${ctxStatic}/images/suess.png"></span>
        		</c:when>
        		<c:otherwise>
        			<span class="suess-img"><img src="${ctxStatic}/images/error.png"></span> 
        		</c:otherwise>
        	</c:choose>
		<div class="suess-list text-center">
		<c:choose>
			<c:when test="${resultMap.respCode=='0'}">
				你的信息提交成功，24小时内客户会确认安装信息，请您保持手机畅通。
			</c:when>
			<c:otherwise>
				${resultMap.respDesc} 预约失败。
			</c:otherwise>
		</c:choose>
		</div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>