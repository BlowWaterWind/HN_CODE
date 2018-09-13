<!DOCTYPE html>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="com.ai.ecs.member.entity.MemberSecurity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Cache-Control" content="no-cache">
  <meta http-equiv="Expires" content="0">
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/member/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/checkMemberSecurity.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>
<div class="top container">
  <div class="header sub-title"> 
    <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>密保验证</h1>
  </div>
</div>
<form class="form-horizontal login login-con" role="form" id="userForm">
  <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
<div class="container white-bg box10">
    <ul class="order-info4 order-fr">
      <li>
        <label> 手机号码</label>
        <div class="right-td form-input-div address-select">
        <input id="mobile" name="mobile" type="tel" value="" class="form-control"/>
        </div>
      </li>
    </ul>
      <ul class="order-info4 order-fr">
      <li>
        <label> 验证码：</label>
        <div class="right-td form-input-div address-select">
        <input id="captcha" name="captcha" class="input-form" type="text" maxlength="6" placeholder="6位数字" data-widget-cid="widget-1" data-explain="" />
        <button id="getCodeBtn" class="h30" data-widget-cid="widget-2">点此免费获取</button>
        </div>
      </li>
       <li>
      <a href="<%=request.getContextPath()%>/memberSecurity/toResetPassword"  class=" font-blue">重新选择找回密码方式</a>
      </li>
    </ul>
  <a id="findByPhoneBtn"  class="btn btn-blue btn-block center-btn">确 定</a>
</div>
</form>
<script type="text/javascript">
var setUserNameUrl="<%=request.getContextPath()%>/memberSecurity/setUserName";
var sendSmsUrl="<%=request.getContextPath()%>/memberSecurity/sendSms";
var gotoUrl="<%=request.getContextPath()%>/memberSecurity/toResetPass";
var checkSmsUrl="<%=request.getContextPath()%>/memberSecurity/checkSms";
</script>
</body>
</html>
