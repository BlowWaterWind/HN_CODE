<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/taglib.jsp" %>
    <script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js" type="text/javascript"></script>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="${ctxStatic}/css/broadBand/wap.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/ap/lib/normalize-6.0.0.min.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/layer.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/comm.js"></script>
    <script type="text/javascript" >
        var ctx="${ctx}",gUrl="${gUrl}",sUrl="${sUrl}",tfsUrl="${tfsUrl}",ctxStatic = "${ctxStatic}",contextPath = "${contextPath}";
    </script>
    <!--
   * the head of wap
   *
   * @author: yunzhi li
   * @version: 2016/10/27 10:56
   *           Id
  -->

    <%--插码js引入--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/webtrends.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/broadbandInsertCode.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <%--插码meta标签的定义--%>
    <meta name="WT.mobile" content="" />
    <meta name="WT.brand" content="" />
    <meta name="WT.si_n" content="WDDD_ZFJG" />
    <meta name="WT.si_x" content="99" />

    <title>支付结果</title>
</head>
<body>

<div class="cont c_pay">
    <input type="hidden" name="orderId" id="orderId" value="${orderId}"/>
    <div class="r r_rs">
        <div class="c c-12-12">
            <div class="loading">
                <div class="clock seconds">
                    <div class="left">
                        <div></div>
                    </div>
                    <div class="right">
                        <div></div>
                    </div>
                    <div class="progress"><span>0</span></div>
                </div>
                <p>支付结果检查中，请稍候</p>
            </div>

            <div class="rs" id="success">
                <div class="rs_icon rs_success"></div>
                <h2>支付成功</h2>
                <p>您已经支付成功</p>
                <a href="${ctx}/broadbandOrder/queryOrderList">返回订单列表</a>
            </div>

            <div class="rs" id="fail">
                <div class="rs_icon rs_fail"></div>
                <h2>支付失败</h2>
                <p>您支付失败</p>
                <a href="${ctx}/broadbandOrder/queryOrderList">返回重新支付</a>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function() {
        checkPay();
        function checkPay(){
            payTimer =  setTimeout(function () {
                var f = checkPayState();
                console.info('------f-----' + f);
                if (f) {
                    checkPay();
                }else{
                  clearTimeout(payTimer);
                }
            }, 5000);
        }
    });

    function checkPayState() {
        console.info('------开始查询-----');
        var flag = true;
        $.ajax({
            type: 'post',
            url: ctx + '/broadbandOrder/checkPayState',
            cache: false,
            async: false,
            dataType: 'json',
            data: {
                orderId: $("#orderId").val()
            },
            success: function (data) {
                if (data.returnCode == '0000' && data.status == 'SUCCESS') {//请求成功支付成功
                    document.getElementsByTagName('meta')['WT.si_x'].content = "99";
                    clearInterval(loaderInt);
                    flag = false;
                    $(".loading").hide();
                    $("#success").show();
                    $("#fail").hide();
                } else if (data.status == 'FAILED') {//支付失败
                    document.getElementsByTagName('meta')['WT.si_x'].content = "-99";
                    clearInterval(loaderInt);
                    flag = false;
                    $(".loading").hide();
                    $("#success").hide();
                    $("#fail").show();
                }
            }
        });
        return flag;
    }
</script>
<script type="text/javascript">
    var payTimer=null;
    // 计时 如： 10 秒
    var timer = 15;
    $(".progress").html('<span>' + timer + '</span>')
    var halfTimer = timer / 2;
    var curTimer  = 0;
    var loaderInt = setInterval(function () {
        if (curTimer < timer) {
            curTimer += 1;
            showLoading(curTimer, halfTimer);
        }
        else {
            clearInterval(loaderInt);
            clearTimeout(payTimer);
            $(".rs").show();
            $(".loading").hide();
            $("#success").hide();
            $("#fail").show();
        }
    }, 1000);

    /**
     * 显示加载进度
     * @param num
     * @param half
     */
    function showLoading(num, half) {
        var $seconds    = $(".seconds");
        var $lProgress  = $seconds.find(".left").children('div');
        var $rProgress  = $seconds.find(".right").children('div');
        var rDeg        = num > half ? half : num;
        var lDeg        = num > half ? num - half : 0;
        var progressNum = half * 2 - num;
        $(".progress").html('<span>' + progressNum + '</span>')

        $lProgress.css({
            "transform": "rotateZ(" + (360 / (2 * half) * lDeg - 180) + "deg)"
        });
        $rProgress.css({
            "transform": "rotateZ(" + (360 / (2 * half) * rDeg - 180) + "deg)"
        });
    }
</script>
</body>
</html>
