<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <meta name="WT.si_n" content="宽带提速办理" />
    <meta name="WT.tx_s" content="${record.payAmount/100}" >  <%--取值订单总价，必填--%>
    <meta name="WT.tx_u" content="1" ><%--  取值数量，必填--%>
    <meta name="WT.tx_i" content="${record.recordId}" > <%-- 取值订单号，必填--%>
    <c:choose>
        <c:when test="${record.respCode=='0'}">
            <meta name="WT.si_x" content="办理成功" />
        </c:when>
        <c:otherwise>
            <meta name="WT.si_x" content="办理失败" />
            <meta name="WT.err_code" content="办理失败" />
        </c:otherwise>
    </c:choose>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>
            <c:choose>
                <c:when test="${record.respCode=='0'}">
                    付款成功
                </c:when>
                <c:otherwise>
                    付款失败
                </c:otherwise>
            </c:choose>
        </h1>
    </div>
</div>
<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <c:choose>
            <c:when test="${record.respCode=='0'}">
                <span class="suess-img"><img src="${ctxStatic}/images/suess.png"></span>
            </c:when>
            <c:otherwise>
                <span class="suess-img"><img src="${ctxStatic}/images/error.png"></span>
            </c:otherwise>
        </c:choose>
        <div class="suess-list">
            <c:choose>
                <c:when test="${record.respCode=='0'}">
                    <p class="text-center suess-tit font-blue">提速成功申请提交成功，等待客户回复短信确认</p>
                </c:when>
                <c:otherwise>
                    <p class="text-center suess-tit font-blue">您的订单提交失败，请您重试，或致电客服10086!</p>
                </c:otherwise>
            </c:choose>
            <p>订单编号：<b class="font-rose">${order.order_temp_id}</b></p>
            <p>价&nbsp;&nbsp;格：<b class="font-rose">￥${order.goods_pay_price/100}元/月</b></p>
            <p>产品名称：${record.productInfo}</p>
            <p>合约期限：<b class="font-rose"><fmt:formatDate value="${record.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>~ <fmt:formatDate value="${record.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></b></p>
            <p>用户姓名：${record.custName}</p>
            <p>手&nbsp;&nbsp;机：${record.serialNumber}</p>
            <p>宽带地址：${record.address}</p>
        </div>
        <div class="tj-btn">
            <%--<a class="btn btn-green" href="${ctx}/myOrder/toMyAllOrderList">查看订单</a>--%>
            <!--付款失败 class btn-rose--><a class="btn btn-blue" href="${ctx}/broadband/broadbandHome">返回宽带首页</a> </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>