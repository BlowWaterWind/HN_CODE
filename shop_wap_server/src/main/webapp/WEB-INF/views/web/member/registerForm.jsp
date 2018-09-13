<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <!-- HTTP 1.0 -->
    <meta http-equiv="cache-control" content="no-cache">
    <!-- Prevent caching at the proxy server -->
    <meta http-equiv="expires" content="0">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/login.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/oil.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/jquery-ui.1.11.1.min.js"></script>
<script  type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/securityencode.js" charset="utf-8"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/registerForm.js?v=2"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>

<div class="top container">
<div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
  <h1>用户注册</h1>
   <sys:headInfo cartTop="4"/>
</div>
</div>

<div class="container pd-t45 ">
<div class="load-container load8" id="loading"  style="display:none">
<div class="container con-br">
  <span>提交中</span>
  <div class="loader"></div> 
</div>
</div>
<form class="box-dr login-top" role="form"  method="post">
    <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
  <div class="form-group">
    <div id="memberName" class="form-input-div"><!-- 输入错误时加class input-fr 将输入框变成红色 -->
      <label  class="input-text">请输入用户名</label><!-- 点击input后label增加class "hide" -->
      <input type="text" id="member_name" name="member_name" class="form-control">
    </div>
    <span id="name_text" class="font-default hide">*该用户名已存在</span>
  </div>
  <div class="form-group">
    <div class="form-input-div">
      <label class="input-text">请输入邮箱</label><!-- 点击input后label增加class "hide" -->
      <input type="text"  id="member_email" name="member_email" class="form-control">
    </div>
    <span id="email_text" class="font-default hide">*请输入常用的邮箱，用于找回密码</span>
  </div>
  <div class="form-group">
    <div class="form-input-div">
      <label class="input-text">请输入密码</label><!-- 点击input后label增加class "hide" -->
      <input type="password" id="member_passwd"  name="member_passwd" class="form-control" AUTOCOMPLETE="off">
    </div>
    <span id="passwd_text" class="font-default hide">*密码长度为8到20位,包含A-Z的字母，0-9数字,特殊符号</span>
  </div>
  <div class="form-group">
    <div class="form-input-div">
      <label class="input-text">请再次输入密码</label><!-- 点击input后label增加class "hide" -->
      <input type="password"  id="confirm_passwd" name="confirm_passwd" class="form-control" AUTOCOMPLETE="off">
    </div>
    <span id="cpass_text" class="font-default hide">*密码与上述不一致</span>
  </div>
  <div class="form-group">
   <div class="form-input-div yzm">
      <label class="input-text">请输入验证码</label><!-- 点击input后label增加class "hide" -->
      <input type="tel" class="form-control"  id="captcha" name="captcha" >
      <a href="JavaScript:void(0);"
      onclick="javascript:document.getElementById('captchaImage').src='${pageContext.request.contextPath}/register/getCaptchaImage.do?temp=123'+ (new Date().getTime().toString(36)); return false"
      class="change" title="看不清,点击更换验证码">
      <img src="${pageContext.request.contextPath}/register/getCaptchaImage.do" name="captchaImage" id="captchaImage" border="0" style="width:120px;height:40px;">
      </a></div>
    <span class="font-default hide">*验证码输入不正确</span>
  </div>
  <div class="form-group btn-box">
    <a id="submitBtn" class="btn btn-blue btn-block" href="#">注　册</a>
  </div>
</form>
</div>

<footer class="text-center container">
<%--<a href="#" class="active">触屏版</a> | <a href="#">电脑版</a>--%>
</footer>
</body>
<script type="text/javascript">
var submitUrl="<%=request.getContextPath()%>/register/doRegister";
var checkUserUrl="<%=request.getContextPath()%>/register/checkUserName";
var loginUrl="<%=request.getContextPath()%>/release/front/index.html";
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
+ "wxyz0123456789+/" + "=";
</script>
</html>