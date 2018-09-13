<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils"%>
<%@ page import="com.ai.ecs.member.entity.MemberLogin"%>
<%@ page import="org.apache.shiro.session.Session"%>
<%@ page import="com.ai.ecs.member.entity.MemberVo"%>
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
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/updatePass.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
MemberVo member=UserUtils.getLoginUser(request);
MemberLogin memberLogin=(MemberLogin)member.getMemberLogin();
if(memberLogin==null){
    memberLogin=new MemberLogin();
}

%>
<body>
<div class="top container">
  <div class="header sub-title"> 
    <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>修改密码</h1>
   <sys:headInfo/>
  </div>
</div>
<form class="form-horizontal login login-con" role="form" id="resetForm">
    <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
<div class="container white-bg box10">
    <ul class="order-info4 order-fr">
      <li>
        <label>用户名：</label>
        <div class="right-td form-input-div address-select">
        <input type="hidden" name="memberId" id="memberId" value=<%=memberLogin.getMemberId()%>>
        <input type="text" disabled="true" name="memberLogingName" id="memberLogingName" value="<%=memberLogin.getMemberLogingName()%>">
        </div>
      </li>
    <li>
      <label>原密码：</label>
      <div class="right-td form-input-div address-select">
      <input type="password" name="oldPass" id="oldPass" value="">
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
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
<script type="text/javascript">
var submitUrl="<%=request.getContextPath()%>/memberInfo/updatePass.json";
var gotoUrl="<%=request.getContextPath()%>/memberInfo/toSetMemberInfo";
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
+ "wxyz0123456789+/" + "=";
</script>
</body>
</html>
