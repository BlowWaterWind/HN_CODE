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
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/setMemberSecurity.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<body>
<div class="top container">
  <div class="header sub-title"> 
    <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>设置密码保护</h1>
     <sys:headInfo/>
  </div>
</div>
<form class="form-horizontal login login-con" role="form" id="questionForm">
    <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
<div class="container white-bg box10">
    <ul class="order-info4 order-fr">
      <li>
        <label>密保问题：</label>
        <div class="right-td form-input-div address-select"><i class="select-arrow"></i>
        <input type="hidden" name="memberId" id="memberId" value=${memberId} >
        <select id="pwdQuestionNameSec" name="pwdQuestionName" class="form-control">
        <option value="">--请选择--</option>
        <c:forEach items="${pwdList}" var="page">
        <option value=${page.pwdQuestionName}>${page.pwdQuestionName}</option>
        </c:forEach>
         <option value="">其他</option>
        </select>
        </div>
      </li>
      <li id="pwdQuestionNameInLi" style="display:none">
      <label></label>
      <div class="right-td form-input-div address-select" >
      <input  id="pwdQuestionNameIn" name="pwdQuestionName" class="form-control" />
      </div>
    </li>
        
      <li>
      <label>密保答案：</label>
      <div class="right-td form-input-div address-select">
       <input type="text" name="pwdQuestionAnswer" id="pwdQuestionAnswer"  class="form-control"/>
      </div>
    </li>
    </ul>
  <a id="submitBtn"  class="btn btn-blue btn-block center-btn">确 定</a>
</div>
</form>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
<script type="text/javascript">
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
+ "wxyz0123456789+/" + "=";
var submitUrl="<%=request.getContextPath()%>/memberSecurity/setMemberSecurity.json";
</script>
</body>
</html>
