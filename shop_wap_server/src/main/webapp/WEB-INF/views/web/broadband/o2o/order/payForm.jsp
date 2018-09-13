
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
<form id="form1" method="post" action="${requestPayUrl}">
    <input type="hidden" name="callbackUrl" value="${callbackUrl}"/>
    <input type="hidden" name="notifyUrl" value="${notifyUrl}"/>
    <input type="hidden" name="merchantId" value="${merchantId}"/>
    <input type="hidden" name="type" value="${type}"/>
    <input type="hidden" name="version" value="${version}"/>
    <input type="hidden" name="channelId" value="${channelId}"/>
    <input type="hidden" name="payType" value="${payType}"/>
    <input type="hidden" name="period" value="${period}"/>
    <input type="hidden" name="periodUnit" value="${periodUnit}"/>
    <input type="hidden" name="amount" value="${amount}"/>
    <input type="hidden" name="orderDate" value="${orderDate}"/>
    <input type="hidden" name="merchantOrderId" value="${merchantOrderId}"/>
    <input type="hidden" name="OrderNo" value="${OrderNo}"/>
    <input type="hidden" name="merAcDate" value="${merAcDate}"/>
    <input type="hidden" name="productDesc" value="${productDesc}"/>
    <input type="hidden" name="productId" value="${productId}"/>
    <input type="hidden" name="productName" value="${productName}"/>
    <input type="hidden" name="reserved1" value="${reserved1}"/>
    <input type="hidden" name="reserved2" value="${reserved2}"/>
    <input type="hidden" name="payOrg" value="${payOrg}"/>
    <input type="hidden" name="authorizeMode" value="${authorizeMode}"/>
    <input type="hidden" name="mobile" value="${ mobile}"/>
    <input type="hidden" name="Gift" value="${ Gift}"/>
    <input type="hidden" name="MerActivityID" value="${ MerActivityID}"/>
    <input type="hidden" name="PaymentLimit" value="${ PaymentLimit}"/>
    <input type="hidden" name="ProductURL" value="${ ProductURL}"/>
    <input type="hidden" name="IDType" value="${ IDType}"/>
    <input type="hidden" name="IvrMobile" value="${ IvrMobile}"/>
    <input type="hidden" name="ProductType" value="${ProductType}"/>
    <input type="hidden" name="hmac" value="${hmac}"/>
</form>
<script type="text/javascript">
    window.onload=function(){
            $("#form1").submit();
            }
</script>
</body>
</html>
