<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page session="false"%>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>湖南移动</title>
	<style type="text/css">
		*{ margin:0; padding:0;}
		body {margin: 0px; padding:0;font-family:"Microsoft Yahei"; background:#fff;}
		.error-wrap {max-width:720px; width:100%; margin:0 auto;padding:35px 0 0 0}
		.error-wrap .error-logo{ padding-bottom:55px;}
		.error-wrap .error-con{ padding-left:75px;}
		.error-img { width:200px; height:200px;height:auto;display:block;float:left;}
		.error-btnBox a {display:block; width:120px; height:38px; overflow:hidden;line-height:38px; color:#fff; text-decoration:none; margin-left:auto; margin-right:auto; font-size:15px;background-color:#0085d0; background-size:100%; text-align:center; float:left;}
		.error-wrap .error-text{ color:#888; font-size:18px;font-weight:bold; margin-left:70px; float:left; overflow:hidden; padding-top:13px;}
		.error-wrap .error-text .error-btnBox{ padding-top:25px;}
		.error-wrap .error-text p{ padding:3px 0;}
		.fs{ font-size:14px;}
		b{ font-weight:normal;}
		.font-gray{ color:#999;}
		.mb10{ margin-bottom:10px; display:block;}
		@media(max-width:1000px){
			.error-logo{ display:none;}
			.error-wrap .error-con{ padding:0 !important;}
			.error-img{ width:40%; float:inherit; margin:0 auto;}
			.error-wrap .error-text{ float:inherit !important; text-align:left; padding:30px 25px 0 45px;margin:0 !important}
			.error-btnBox a{ float:inherit !important}
		}
	</style>
</head>
<body>
<div class="error-wrap">
	<div class="error-logo"><img src="${ctxStatic}/images/mon_logo.jpg" /></div>
	<div class="error-con">
		<img class="error-img" src="${ctxStatic}/images/404.jpg" alt=" ">
		<!--<img class="error-img" src="images/503.jpg" alt=" ">-->
	</div>
	<div class="error-text">
		<!--<p>糟糕，404了<br />抱歉您所访问的页面不存在</p>-->
		<p class="mb10">抱歉！页面无法访问…</p>
		<p class="fs font-gray">可能因为：</p>
		<p class="fs">网址有错误&gt;<b class="font-gray">请检查地址是否完整或存在多余字符</b></p>
		<p class="fs">网址已失效&gt;<b class="font-gray">可能页面已删除</b></p>
		<div class="error-btnBox"><a href="javascript:void(0)" onclick="window.history.back();" >立即返回</a></div>
	</div>
</div>
</body>
</html>
