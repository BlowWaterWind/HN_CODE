<%--
  Created by IntelliJ IDEA.
  User: fengrh
  Date: 2016/5/23
  Time: 20:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>

<script src="${ctxStatic}/js/load.js"></script>

<html>
<head>
    <title>Title</title>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>
<input type="button" value="提示" onclick="showAlert('提示内容')">
<input type="button" value="提示" onclick="showAlert('提示内容2')">
<input type="button" value="加载1" onclick="showLoadPop('提示内容2')">
<input type="button" value="加载2" onclick="showLoadPop()">
<script>
    function testCall() {
         showAlert("回调。。。");
    }
</script>


</body>
</html>
