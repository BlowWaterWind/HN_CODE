<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城-重置密码选择方式</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/member/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/resetPassMethod.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>
<div class="top container">
  <div class="header sub-title"> 
    <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>密码重置</h1>
  </div>
</div>
<div class="container white-bg">
  <div class="sp-pj sp09"> <a id="byPhone" href="<%=request.getContextPath()%>/memberSecurity/findPassByPhone"> <span class="gm-sl">验证手机</span> <span class="sp-icon"></span></a> </div>
  <div class="sp-pj sp09"> <a id="answer" href="<%=request.getContextPath()%>/memberSecurity/toSetUserName"> <span class="gm-sl">回答安全保护问题</span><span class="sp-icon"></span></a> </div>
</div>
<script type="text/javascript">
var submitUrl="<%=request.getContextPath()%>/memberSecurity/toSetMemberSecurity";
</script>
</body>
</html>
