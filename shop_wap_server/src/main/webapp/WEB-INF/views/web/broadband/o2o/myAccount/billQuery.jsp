<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>账单查询</title>
    <script type="text/javascript">
        var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>账单查询</h1>
    </div>
</div>
<div class="container">
    <ul class="flex-menu">
        <li>3月</li>
        <li>4月</li>
        <li>5月</li>
        <li>6月</li>
        <li>7月</li>
        <li class="active">当月</li>
    </ul>
    <!--内容区域 start-->
    <div class="inform-info">
        <div class="inform-list">
            <div class="inform-title">套餐信息</div>
            <div class="inform-con">
                <p>套餐名称<span class="t-r">48元消费叠加型</span></p>
                <p>宽带速率<span class="t-r">50M</span></p>
                <p>账单开始日<span class="t-r">8月1日</span></p>
                <p>账单结束日<span class="t-r">8月31日</span></p>
            </div>
        </div>
        <div class="inform-list">
            <div class="inform-title">套餐费用</div>
            <div class="inform-con">
                <p>套餐保底消费<span class="t-r">48元<b class="f12 font-red">不计入宽带费用</b></span></p>
                <p>宽带费用<span class="t-r">20元</span></p>
                <p>互联网电视<span class="t-r">10元</span></p>
            </div>
        </div>
    </div>
    <!--内容区域 end-->
    <div class="broad-btn">
        <a href="javascript:void(0)" class="btn btn-blue">宽带提速</a>
    </div>
</div>
</body>
</html>