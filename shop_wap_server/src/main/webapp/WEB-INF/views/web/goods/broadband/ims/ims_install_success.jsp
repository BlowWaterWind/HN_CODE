<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
	<div class="top container">
        <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
            <h1>我的订单</h1>
        </div>
    </div>
    <div class="container">
        <div class="order-install order-detail">
            <div class="install-title">
                <p>安装成功</p>
                <p><img src="images/kd/cl_icon.png" /></p>
            </div>
            <div class="install-con mt20">
                <div class="install-con-title">
                    <p class="font-gray">2016-04-07 09:09:17</p>
                    <p>订单编号：8771251</p>
                </div>
                <div class="install-adress">
                    <div class="install-info">
                        <p class="adress-number"><span>联系人：陈换</span><span>13873882888</span></p>
                        <p>安装地址：长沙市 芙蓉区 东屯渡街道 扬帆小区E28栋2单元222</p>
                    </div>
                </div>
                <dl class="detail-list">
                    <dt><img src="demoimages/kdtu.png" /></dt>
                    <dd>
                        <p>消费叠加型保底消费xx元/月50M宽带</p>
                        <p>消费叠加型保底消费xx元/月50M宽带</p>
                        <a href="javascript:void(0)">更多详情</a>
                    </dd>
                </dl>
                <dl class="detail-list last">
                    <dt><img src="demoimages/kdtu.png" /></dt>
                    <dd>
                        <p>消费叠加型保底消费xx元/月50M宽带</p>
                        <p>消费叠加型保底消费xx元/月50M宽带</p>
                        <a href="javascript:void(0)">更多详情</a>
                    </dd>
                </dl>
                <!--支付金额 start-->
                <div class="order-amount">金额：XX元/月<span class="ml20">已支付</span></div>
                <!--支付金额 end-->
            </div>
        </div>
    </div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>