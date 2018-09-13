<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/taglib.jsp"%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta http-equiv="Cache-Control" content="max-age=3600;must-revalidate" />
    <meta name="keywords" content="中国移动网上商城,手机,值得买,可靠,质量好,移动合约机,买手机送话费,4G手机,手机最新报价,苹果,华为,魅族" />
    <meta name="description" content="中国移动网上商城,提供最新最全的移动合约机、4G手机,买手机送话费,安全可靠,100%正品保证,让您足不出户,享受便捷移动服务！" />
    <script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js?v=625" type="text/javascript"></script>
    <script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
    <script type="text/javascript" >
        var ctx="${ctx}",gUrl="${gUrl}",sUrl="${sUrl}",tfsUrl="${tfsUrl}",ctxStatic = "${ctxStatic}",contextPath = "${contextPath}";
    </script>

    <%@ include file="/WEB-INF/views/include/messageNew.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/broadBand/wap-order-new.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/js/swiper/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">

    <%--插码js引入--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/webtrends.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/broadbandInsertCode.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <%--插码meta标签的定义--%>
    <meta name="WT.mobile" content="" />
    <meta name="WT.brand" content="" />
    <meta name="WT.si_n" content="WDDD_LSDD" />
    <meta name="WT.si_x" content="20" />


</head>
<body>

<div class="container">
    <ul class="tabmenu">
        <li class="tabmenu-item" id="all"><a href="${ctx}/o2oOrder/queryOrderList?flag=all">全部订单</a></li>
        <li class="tabmenu-item" id="unfinished"><a href="${ctx}/o2oOrder/queryOrderList?flag=unfinished&phone=${phone}">未完成</a></li>
        <li class="tabmenu-item" id="finished"><a href="${ctx}/o2oOrder/queryOrderList?flag=finished&phone=${phone}">已完成</a></li>
        <li class="tabmenu-item" id="unstart"><div class="red-point">${unstartNumber}</div><a href="${ctx}/o2oOrder/queryOrderTempList?flag=unstart&unfinishedNumber=${unfinishedNumber}&finishedNumber=${finishedNumber}&phone=${phone}">未回复</a></li>
        <li class="tabmenu-item" id="willPay"><div class="red-point">${payNum}</div><a href="${ctx}/o2oOrder/queryOrderPayTempList?flag=willPay&unfinishedNumber=${unfinishedNumber}&finishedNumber=${finishedNumber}&phone=${phone}">待支付</a></li>
    </ul>
    <input type="hidden" name="pageNo" id="pageNo" value="0" />
    <input type="hidden" name="flag" value="${flag}" id="flag"/>
    <input type="hidden" name="pageSize" id="pageSize" value="8" />
        <div class="lists" id="pageDiv">

        </div>


</div>


<script type="text/javascript" src="${ctxStatic}/js/busi/lib/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/swiper/swiper.jquery.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-extend.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/datetime-picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/busi/public.js"></script>
<script src="${ctxStatic}/js/dropload/dropload.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
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
        <div class="list order-list">
            <div class="order-list_hd">
                <div class="left"><i class="icon-shop"></i>
                    <h3 class="shop-name">手机号码:{{o2oTempOrder.contact_phone}}</h3><i class="caret-right"></i>
                </div>
                <div class="right"><span class="txt-red">{{o2oTempOrder.status_name }}</span></div>
            </div>
            <div class="order-list_bd">
                <div class="thumb"><img src="${ctxStatic}/demoimages/kdtu.png" alt="宽带"/></div>
                <div class="con">
                    <div class="row">
                        <div class="title">{{o2oTempOrder.goods_name}}</div>
                        <div class="price">¥{{o2oTempOrder.goods_pay_price / 100}}元</div>
                    </div>
                    <div class="row">
                        <div class="desc">套餐分类：{{o2oTempOrder.goods_name}}</div>
                        <div class="number">x1</div>
                    </div>
                    <div class="row">
                        <div class="desc"></div>
                        <div class="number" style="width:100%">
                            {{if o2oTempOrder.differTime =='-1'}}
                            剩余时间：已超时
                            {{else}}
                            剩余时间：{{o2oTempOrder.differTime}}
                            {{/if}}
                        </div>
                    </div>
                </div>
            </div>
            <div class="total">共1件商品 合计：<span class="price">¥{{o2oTempOrder.goods_pay_price / 100}}</span>（含初装费￥0.00）</div>
            <div class="btn-wrap Grid -right">
                {{if o2oTempOrder.order_status == 0 || o2oTempOrder.order_status == 2 && o2oTempOrder.sms_send_times <3}}
                <button href="javascript:void(0)" class="btn btn-default" style="background-color: #fff;" onclick='cancelOrder("{{o2oTempOrder.order_temp_id}}")'>取消订单</button>
                <button href="javascript:void(0)" class="btn btn-default" style="background-color: #fff;" id="sendSms" onclick="sendSms('{{o2oTempOrder.goods_name}}','{{o2oTempOrder.contact_phone}}','{{o2oTempOrder.order_type_id}}','{{o2oTempOrder.order_temp_id}}')">再次发送短信</button>
                {{/if}}
            </div>
        </div>
    {{/each}}
</script>
<script type="text/javascript" src="${ctxStatic}/js/o2o/order/order.js"></script>
</body>
</html>
