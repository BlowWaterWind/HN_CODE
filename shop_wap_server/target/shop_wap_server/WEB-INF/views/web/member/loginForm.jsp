<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<html>
<head>
  <meta http-equiv="pragma" content="no-cache">
  <!-- HTTP 1.0 -->
  <meta http-equiv="cache-control" content="no-cache">
  <!-- Prevent caching at the proxy server -->
  <meta http-equiv="expires" content="0">
  <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" />
  <meta name="WT.si_n" content="登录" />
<meta name="WT.si_x" content="开始登录" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/tab.js"></script>
<script  type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/securityencode.js" charset="utf-8"></script>
<script src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/loginForm.js?v=1024"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/qm/insdc_w2.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/qm/sdc_wap.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>
<div class="top container">
  <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)"+ class="icon-left"></a>
    <h1>登录</h1>
  </div>
</div>
<div class="container login-bg white-bg"><img src="<%=request.getContextPath()%>/static/images/member/shop-images/logo.png" class="logo">
  <div class="Tab tab-list">
    <div id="box">
      <ul class="TabHead" id="topfloat">
        <li class="on"><a href="javascript:;" otype="button" otitle="手机号登录" oarea="终端">手机号登录</a></li>
         <c:if test="${mobileFlag!=true}">
        <li><a href="javascript:;" otype="button" otitle="用户名登录" oarea="终端">用户名登录</a></li>
        </c:if>
         <c:if test="${mobileFlag==true}">
       <script>
       		showAlert("此业务仅支持手机号登录");
       </script>
        </c:if>
      </ul>
    </div>
    <div class="TabNote">
      <div  class="block" style="display:block;"  id="loginByMobile" >
        <form class="form-horizontal login login-con" role="form" method="post">
          <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
          <div class="form-group"> <span class="login-icon01"></span>
            <input type="tel" name="mobile" id="mobile" value="" class="form-control form-con sjh-jd" autocomplete="off" placeholder="请输入手机号码" otype="button" otitle="输入手机号" oarea="终端"/>
            <input type="hidden" value="" id="isVerify">
          </div>
          <div class="form-group gx-tab">
            <div class="pull-left">
              <label class="box-jl01">
                <input type="checkbox" class="checkbox" name="method" value="fw"  checked="checked" id='fw' onclick="clickCheckBox('fw')"/>
                服务密码</label>
            </div>
            <div class="pull-right">
              <label class="box-jl01"><!-- 灰色样式 “bjl2” -->
                <input type="checkbox" class="checkbox"  name="method" value="dx" id='dx' onclick="clickCheckBox('dx')"/>
                <!-- 禁用属性 disabled="true" --> 
                短信验证码</label>
            </div>
          </div>
          <div class="form-group" id="fwDiv"> <span class="login-icon02"></span>
            <input type="password" id="servicePass" name="servicePass" class="form-control form-con"
                   placeholder="请输入服务密码" otype="button" otitle="服务密码" oarea="终端" autocomplete="off"/>

          </div>
          <div class="form-group hq-yzm" id="fwDivVerify" style="display: none">
            <span class="login-icon03"></span>
            <input type="tel" id="verifyCode" name="verifyCode" placeholder="请输入短信随机码"
                   value="" otype="button" otitle="短信随机码" oarea="终端"/>
            <input  class="sms-bg" id="btnSendVerifyCode" type="button" value="获取随机码"  />
          </div>

          <div class="form-group hq-yzm" id="dxdiv">
            <span class="login-icon03"></span>
            <input type="tel" id="smsCaptcha" name="smsCaptcha" placeholder="请输入短信随机码" value="${sms}"
                   otype="button" otitle="短信随机码" oarea="终端"/>
            <input  class="sms-bg" id="btnSendCode" type="button" value="获取随机码"  />
          </div>
          <div class="form-group" style="display: none" id="captchaDiv">
            <div class="yzm">
              <input type="tel"  id="imageCaptcha" name="imageCaptcha"  class="form-control form-con02" placeholder="输入验证码">
              <a href="JavaScript:void(0);"
              onclick="javascript:document.getElementById('captchaImage2').src='${pageContext.request.contextPath}/login/getCaptchaImage.do?temp=123'+ (new Date().getTime().toString(36)); return false"
              class="change" title="看不清,点击更换验证码">
              <img src="${pageContext.request.contextPath}/login/getCaptchaImage.do" name="captchaImage2" id="captchaImage2" border="0" style="width:120px;height:40px;">
              </a></div>
            <div class="clear"></div>
            <span class="input-cw hide">*请重新输入验证码</span> </div>
        </form>
         <div class="form-group grop-btn"> <a  class="btn btn-blue btn-block submitBtn" href="#"  otype="button" otitle="登录" oarea="终端">登&emsp;&emsp;录</a><a style="float:left;margin-left:10px;color:blue" href="<%=request.getContextPath()%>/register/toRegister"  otype="button" otitle="立即注册" oarea="终端">立即注册</a>
 	        <a style="float:right;margin-right:10px;color:blue" href="<%=UserUtils.wantingServerHost %>wap/static/doBusiness/PwdReset.html" id="findPassBtn"  otype="button" otitle="忘记密码" oarea="终端">忘记密码</a></div>
      </div>
     <div class="none" style="display: none;" id="loginByName">
        <form class="form-horizontal login login-con" role="form">
          <div class="form-group"> <span class="login-icon01"></span>
          <input type="hidden" id="ref" name="ref" class="form-control form-con sjh-jd" value="${ref}"/>
            <input type="text" id="loginname" name="loginname" class="form-control form-con sjh-jd" autocomplete="off" placeholder="请输入用户名" otype="button" otitle="输入用户名" oarea="终端"/>
          </div>
          <div class="form-group"> <span class="login-icon02"></span>
            <input type="password" id="loginpass" name="loginpass" class="form-control form-con"   placeholder="请输入密码" autocomplete="off"/>
          </div>
          <div class="form-group">
            <div class="yzm">
              <input type="tel" id="captcha" name="captcha" class="form-control form-con02" placeholder="输入验证码" />
              <a href="JavaScript:void(0);"
              onclick="javascript:document.getElementById('captchaImage1').src='${pageContext.request.contextPath}/login/getCaptchaImage.do?temp=123'+ (new Date().getTime().toString(36)); return false"
              class="change" title="看不清,点击更换验证码">
              <img src="${pageContext.request.contextPath}/login/getCaptchaImage.do" name="captchaImage1" id="captchaImage1" border="0" style="width:120px;height:40px;">
              </a> </div>
            <div class="clear"></div>
            <span class="input-cw hide">*请重新输入验证码</span> </div>
        </form>
      <div class="form-group grop-btn"> <a class="btn btn-blue btn-block submitBtn" href="#" otype="button" otitle="登录" oarea="终端">登&emsp;&emsp;录</a><a style="float:left;margin-left:10px;color:blue" href="<%=request.getContextPath()%>/register/toRegister" otype="button" otitle="立即注册" oarea="终端">立即注册</a>
 	 <a style="float:right;margin-right:10px;color:blue" href="<%=request.getContextPath()%>/memberSecurity/toResetPassword"  id="findPassBtn"  otype="button" otitle="忘记密码" oarea="终端">忘记密码</a></div>
      </div>
    </div>
  </div>
  <!--第三方登录 --start-->
    <!--
  <div class="sf-login">
    <h4>使用第三方登录：</h4>
    <ul class="sf-box">
       <li class="wx"><a href="<%=request.getContextPath()%>/login/wechatLogin">微信</a></li>
       <li class="xlwb"><a href="<%=request.getContextPath()%>/login/sinaLogin">新浪微博</a></li>
       <li class="qq"><a href="<%=request.getContextPath()%>/login/qqLogin">QQ</a></li>
    </ul>
  </div>
  -->
    <!--第三方登录 --end-->
</div>
  

<script>
    $(function () {
        $("#dxdiv").hide();
    })
    function clickCheckBox(obj) {
        if ("fw" == obj) {
            $("#fw").attr("checked", 'true');
            $("#dx").removeAttr("checked");
            $("#dxdiv").hide();
            $("#fwDiv").show();
            $("#fwDivVerify").hide();
            $("#captchaDiv").hide();
            $("#verifyCode").val("");

        } else if ("dx" == obj) {
            $("#fw").removeAttr("checked");
            $("#dx").attr("checked", 'true');
            $("#dxdiv").show();
            $("#fwDiv").hide();
            $("#fwDivVerify").hide();
            $("#captchaDiv").hide();
            $("#smsCaptcha").val("");
        }
        needVerifyCode();
    }
    
    var loginByNameUrl="<%=request.getContextPath()%>/login/loginByName";
    var loginByServiceUrl="<%=request.getContextPath()%>/login/loginByService";
    var loginBySmsUrl="<%=request.getContextPath()%>/login/loginBySms";
    var sendSmsUrl="<%=request.getContextPath()%>/login/sendSms";
    var sendSmsCodeUrl="<%=request.getContextPath()%>/login/sendSmsCode";
    var gotoMemberCenterUrl="http://wap.hn.10086.cn<%=request.getContextPath()%>/memberInfo/toRefUrl";
    var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";
</script>
</body>
</html>