<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/broadBand/wap-order-new.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <%--插码js引入--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/webtrends.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/broadbandInsertCode.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <%--插码meta标签的定义--%>
    <meta name="WT.mobile" content="" />
    <meta name="WT.brand" content="" />
    <meta name="WT.si_n" content="WDDD_CKANJD" />
    <meta name="WT.si_x" content="20" />
</head>
<body>
<div class="container">
    <div class="lists">
        <div class="list">
            <div class="order-info">
                <div class="kd-thumb"><img src="${ctxStatic}/demoimages/kdtu.png" alt="宽带"/></div>
                <div class="order-info_bd">
                    <div class="fz-28">${broadbandOrder.offerName}</div>
                </div>
            </div>
        </div>
        <c:if test="${not empty handleName}">
            <div class="list">
                <div class="order-info">
                    <div class="icon-user"></div>
                    <div class="order-info_bd">
                        <div class="mb-10">装机师傅：${handleName}  <span class="txt-blue">${handlePhone} </span></div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty tradeInfoList}">
        <div class="list">
            <ul class="express-step">
                <li class="express-step_item">
                    <div class="icon-house"></div>
                    <div class="con">
                        <div class="txt-deep-gray">安装地： 
                            <c:choose>
                                <c:when test="${not empty broadbandOrder.addrDesc}">
                                    ${broadbandOrder.addrDesc}
                                </c:when>
                                <c:otherwise>
                                    ********
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </li>
                <c:forEach items="${tradeInfoList}" var="tradeInfo" varStatus="status">
                    <li class="express-step_item">
                    <c:choose>
                        <c:when test="${status.first}">
                            <div class="express-point active"></div>
                            <div class="con">
                                <div>${tradeInfo.name}，状态:${tradeInfo.stauts}，操作人:${tradeInfo.handleName}<a href="tel:${tradeInfo.phone}">${tradeInfo.phone}</div>
                                <div class="fz-22 txt-deep-gray">${tradeInfo.beginTime}</div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="express-point"></div>
                            <div class="con">
                                <div class="txt-deep-gray">${tradeInfo.name}，状态:${tradeInfo.stauts}，操作人:${tradeInfo.handleName}<a href="tel:${tradeInfo.phone}">${tradeInfo.phone}</div>
                                <div class="fz-22 txt-deep-gray">${tradeInfo.beginTime}</div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    </li>
                </c:forEach>
            </ul>
        </div>
        </c:if>
    </div>

</div>

<script type="text/javascript" src="${ctxStatic}/js/busi/lib/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/busi/lib/swiper.jquery.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-extend.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/datetime-picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/busi/public.js"></script>

<script>
    var swiper = new Swiper('.my-swiper', {
        pagination: '.swiper-pagination',
        autoHeight: true,
        paginationClickable: true
    });
</script>

</body>
</html>
