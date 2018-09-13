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

    <title>下单成功</title>
</head>

<body>
<div class="container">
    <div class="sub-con">
        <!--流程步骤 start-->
        <ul class="bar-step">
            <li>1.提交订单<em></em><i></i></li>
            <li>2.选择支付方式<em></em><i></i></li>
            <li class="active">3.支付成功</li>
        </ul>
        <!--流程步骤 end-->
        <div class="zfcg-con">
            <c:choose>
                <c:when test="${returnCode eq '0000'}">
                    <p>✔︎支付成功，</p>
                    <p>您的幸运码将在稍后送达，</p>
                    <p>请返回首页查看。</p>
                </c:when>
                <c:otherwise>
                    <p>抱歉，支付失败！</p>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="sub-btn">
            <a href="${ctx}/marketing/cf/initCfIndex?cfGoodsId=${order.cfGoodsId}" class="btn btn50 btn-blue">返回首页</a>
        </div>
    </div>
</div>
</body>

</html>
