<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KDZW" />
        <meta name="WT.si_x" content="22" />
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">

    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%--<%@ include file="/WEB-INF/views/include/message.jsp"%>--%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="${ctxStatic}/css/broadBand/wap.css" rel="stylesheet" type="text/css" />
    <%--<link href="${ctxStatic}/ap/lib/normalize-5.0.0.css" rel="stylesheet" type="text/css" />--%>
    <script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/comm.js"></script>
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/public.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>

        <!--
       * the head of wap
       *
       * @author: yunzhi li
       * @version: 2016/10/27 10:56
       *           Id
      -->
    <title>支付方式</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
</head>
<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>支付</h1>
    </div>
</div>
<div class="cont c_pay">
    <form action="${ctx}/broadbandOrder/payOrder" method="post" id="form1">
        <input type="hidden" name="orderSubId" id="orderSubId" value="${orderSub.orderSubId}">
        <input type="hidden" name="orderSubNo" id="orderSubNo" value="${orderSub.orderSubNo}">

        <input type="hidden" name="payPlatform" id="payPlatform">
    </form>
    <div class="r r_info">
        <div class="c c-12-12">
            <h2>订单信息</h2>
            <dl>
                <dt>订单编号</dt>
                <dd id="tel">${orderSub.orderSubNo}</dd>
                <dt>手机号码</dt>
                <dd id="tel">${orderSub.phoneNumber}</dd>
                <dt>套餐名称</dt>
                <dd id="packageName">${orderSubDetail.goodsName}</dd>
                <%--<dt>预存档次</dt>--%>
                <%--<dd id="yeah">x年</dd>--%>
                <dt>支付金额</dt>
                <dd class="price_now" id="amount">￥${orderSub.orderSubPayAmount/100}元</dd>
                <%--<dt>享受折扣</dt>--%>
                <%--<dd class="price_now" id="discount">0折</dd>--%>
            </dl>
        </div>
    </div>

    <div class="r r_payment">
        <div class="c c-12-12">
            <h2>选择支付方式</h2>
            <ul>
                <li class="pay_item p_hebao active" data-attr="CMPAY-WAP_CMCCPAYH5" radiocode="4">
                    <div class="item">和包支付</div>
                    <div class="checkbox"></div>
                </li>
                <li class="pay_item p_alipay" data-attr="ALIPAY-WAP_CMCCPAYH5" radiocode="1">
                    <div class="item">支付宝支付</div>
                    <div class="checkbox" ></div>
                </li>
                <li class="pay_item p_unionpay" data-attr="UNIONPAY-WAP_CMCCPAYH5" radiocode="3">
                    <div class="item">银联支付</div>
                    <div class="checkbox"></div>
                </li>
                <li class="pay_item p_wepay" data-attr="WEIXIN-WAP_CMCCPAYH5" radiocode="2">
                    <div class="item">微信支付</div>
                    <div class="checkbox"></div>
                </li>
            </ul>
        </div>
    </div>

    <div class="r r_pay">
        <div class="c c-12-12">
            <a class="bt_pay" id="zfid">确认支付${orderSub.orderSubPayAmount/100}元</a>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $("#payPlatform").val("CMPAY-WAP_CMCCPAYH5");
        var $payItem = $(".pay_item");
        $payItem.click(function () {
            $payItem.removeClass('active');
            $(this).addClass('active');
            $("#payPlatform").val($(this).attr("data-attr"));

        });

        $("#zfid").click(function(){
            /*插码*/
            try{
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",sin,
                            "WT.si_x","23"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",sin,true,
                                "WT.si_x","23",true);
                    }
                }
            }catch (e){
                console.log(e);
            }
            $("#form1").submit();
        })
    });
</script>
</body>
</html>

</body>
</html>
