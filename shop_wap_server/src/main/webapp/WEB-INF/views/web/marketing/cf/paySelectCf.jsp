<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>


<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <%@include file="/WEB-INF/views/include/taglib.jsp"%>
    <script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp" %>
    <!-- css -->
    <link href="${ctxStatic}/kill/lib/normalize-6.0.0.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/marketing/cf/wap.css" rel="stylesheet" type="text/css"/>
    <%--sdc9.js--%>
    <!-- js：jquery/insdc_w2.js在head.jsp中有；modal.js在message.jsp中有 -->
    <script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/cut/js/comm.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>

    <script type="text/javascript" >
        var ctx="${ctx}",gUrl="${gUrl}",sUrl="${sUrl}",tfsUrl="${tfsUrl}";
    </script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>

    <title>支付选择</title>
</head>

<body>
    <div class="container">
        <form id="toPayForm" action="${ctx}/marketing/cf/payOrder" method="post">
            <input type="hidden" name="cfOrderId" value="${order.cfOrderId}"/>
        <div class="sub-con">
            <!--流程步骤 start-->
            <ul class="bar-step">
                <li>1.提交订单<em></em><i></i></li>
                <li class="active">2.选择支付方式<em></em><i></i></li>
                <li>3.支付成功</li>
            </ul>
            <!--流程步骤 end-->
            <div class="zf-con">
                <h4 class="f32">请选择支付方式</h4>
                <div class="radio">
                    <input type="radio" id="radio-1" name="payPlatform" class="radio" value="WAPIPOS_SHIPOS" checked="checked"/>
                    <label for="radio-1" class="radio-label">
                        <div class="radio-list">
                            <p>和包支付</p>
                            <p class="f24">支持和包现金账户、商城购机电子券、及多家银行及机构的储蓄卡、信用卡的网上付款<span>请在30分钟内完成支付</span></p>
                        </div>
                    </label>
                </div>
            </div>
            <div class="sub-btn">
                <a href="javascript:void(0)" id="toPay" class="btn btn50 btn-blue">前往支付</a>
            </div>
        </div>
        </form>
    </div>
</body>
<script type="text/javascript">
    $(function () {
        $('#toPay').on("click", function () {
            $('#toPayForm').submit();
        });
    });
</script>
</html>