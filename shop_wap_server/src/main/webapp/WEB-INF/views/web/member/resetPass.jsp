<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/member/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/login.css" rel="stylesheet" type="text/css" />
<script  type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/securityencode.js" charset="utf-8"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/resetPass.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>
<div class="top container">
  <div class="header sub-title"> 
    <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>设置密码保护</h1>
  </div>
</div>
<form class="form-horizontal login login-con" role="form" id="resetForm">
    <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
<div class="container white-bg box10">
    <ul class="order-info4 order-fr">
      <li>
        <label>用户名：</label>
        <div class="right-td form-input-div address-select">
        <input type="hidden" name="memberId" id="memberId" value=${memberId} />
        <input type="text" disabled="true" name="memberLogingName" id="memberLogingName" value=${loginName} />
        </div>
      </li>
    <li>
    <label>新密码：</label>
    <div class="right-td form-input-div address-select">
    <input type="password" name="memberPassword" id="memberPassword" value="">
    </div>
    </li>
    <li>
    <label>确认新密码：</label>
    <div class="right-td form-input-div address-select">
    <input type="password" name="confirmPass" id="confirmPass" value="">
    </div>
    </li>
    </ul>
  <a id="submitBtn"  class="btn btn-blue btn-block center-btn">确 定</a>
</div>
</form>
<script type="text/javascript">
var submitUrl="<%=request.getContextPath()%>/memberSecurity/resetPass.json";
var gotoUrl="<%=request.getContextPath()%>/login/toLogin";
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
+ "wxyz0123456789+/" + "=";
</script>
</body>
</html>
