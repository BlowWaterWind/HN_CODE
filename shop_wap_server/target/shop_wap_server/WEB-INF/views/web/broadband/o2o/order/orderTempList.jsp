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
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/public.js"></script>
    <script src="${ctxStatic}/js/dropload/dropload.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>

</head>
<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带订单</h1>
    </div>
</div>
<div class="container">

            <div class="tabs broad-tabs" id="content">
                <ul class="tab-menu">
                    <li id="all"><a href="${ctx}/o2oOrder/queryOrderList?flag=all&phone=${phone}">全部</a></li>
                    <li id="unfinished"><a href="${ctx}/o2oOrder/queryOrderList?flag=unfinished&phone=${phone}">未完成</a></li>
                    <li id="finished"><a href="${ctx}/o2oOrder/queryOrderList?flag=finished&phone=${phone}">已完成</a></li>
                    <li id="unstart"><a href="${ctx}/o2oOrder/queryOrderTempList?flag=unstart&unfinishedNumber=${unfinishedNumber}&finishedNumber=${finishedNumber}&phone=${phone}">未回复<span class="order-tip">${unstartNumber}</span></a></li>
                    <li id="willPay"><a href="${ctx}/o2oOrder/queryOrderPayTempList?flag=willPay&unfinishedNumber=${unfinishedNumber}&finishedNumber=${finishedNumber}&phone=${phone}">支付<span class="order-tip">${payNum}</span></a></li>
                </ul>
                <input type="hidden" name="pageNo" id="pageNo" value="0" />
                <input type="hidden" name="flag" value="${flag}" id="flag"/>
                <input type="hidden" name="pageSize" id="pageSize" value="8" />
                <div  id="scroller">

                    <div class="broad-order" id="pageDiv">



                        <!--宽带订单 start-->


                        <!--宽带订单 end-->
                    </div>

                </div>


            </div>

</div>
<%--<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.tabslet.min.js"></script>--%>
<script type="text/javascript">
    var ctx="${ctx}";
    var ctxStatic="${ctxStatic}";
    var tfsUrl="${tfsUrl}";
</script>
<script>
    $(function() {
        //tabs
//        $('.tabs').tabslet();
        var flag = $("#flag").val();
        $("#"+flag).addClass("active");
        $("#pageDiv").dropload(droploadConfig);

    });


</script>
<script type="text/html" id="tempOrderList">
        {{each list as o2oTempOrder}}
        <div class='broad-list'>
        <div class='broad-title'>
            <a href='javascript:void(0)'><span class='shop-icon'>手机号码:{{o2oTempOrder.contact_phone}}</span>
                <span class='channel-blue'> {{o2oTempOrder.status_name }}</span>
            </a>
        </div>
            <div class='shop-gobal'>
                <a href='javascript:void(0)'>
                    {{if o2oTempOrder.goodsImageUrl && o2oTempOrder.goodsImageUrl.length>0}}

                    <img src='${tfsUrl}{{o2oTempOrder.goodsImageUrl}}'/>
                    {{/if}}
                    <h4>{{o2oTempOrder.goods_name}}</h4>
                    <p class='shop-txt'>&nbsp;
                        {{if o2oTempOrder.desc && o2oTempOrder.desc.length>0}}
                        {{o2oTempOrder.desc}}
                        {{/if}}
                    </p>
                    <div class='shop-bottom'>
                        <span class='price'>
                             {{o2oTempOrder.goods_pay_price / 100}}元
                        </span>
                    {{if o2oTempOrder.differTime =='-1'}}
                        <span class='f12'>剩余时间：已超时</span>
                        {{else}}
                        <span class='f12'>剩余时间：{{o2oTempOrder.differTime}}</span>
                        {{/if}}
                    </div>
                </a>
            </div>
            <div class='broad-sum'>
                <p>共1件商品</p>
                <p>合计：<span class='channel-blue'>￥{{o2oTempOrder.goods_pay_price / 100}}</span></p>
            </div>
            <div class='broad-btn'>
                {{if o2oTempOrder.order_status == 0 || o2oTempOrder.order_status == 2 && o2oTempOrder.sms_send_times <3}}
                <a href='javascript:void(0)' class='broad-link broad-link-blue'
                   onclick='cancelOrder("{{o2oTempOrder.order_temp_id}}")'>取消订单</a>
                <a href='javascript:void(0)' class='broad-link broad-link-blue'
                   onclick="sendSms('{{o2oTempOrder.goods_name}}','{{o2oTempOrder.contact_phone}}','{{o2oTempOrder.order_type_id}}','{{o2oTempOrder.order_temp_id}}')">再次发送短信</a>
                {{/if}}
            </div>
        </div>
        {{/each}}
</script>
<script type="text/javascript" src="${ctxStatic}/js/o2o/order/order.js"></script>
</body>
</html>
