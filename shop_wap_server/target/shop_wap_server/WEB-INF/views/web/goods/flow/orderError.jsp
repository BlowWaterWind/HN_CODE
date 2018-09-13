<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<link href="<%=path%>/static/css/main.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/css/wt-sub.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/static/css/media-style.css" rel="stylesheet" type="text/css" />
<title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
   <h1>${goodsName }</h1>
  </div>
</div>
<div class="container white-bg">
  <div class="dg-suess">
    <span class="error-icon"></span>
    <div class="suess-text">
      <span class="suess-ft cl-cs">订购失败！</span>
      <span>${respDesc }</span>
    </div>
  </div>
  <div class="zp-btn dg-btn"><a class="btn btn-cl btn-block" href="javascript:void(0);" onclick="window.history.back()">返回上一步，继续办理</a></div>
</div>
</body>
</html>
