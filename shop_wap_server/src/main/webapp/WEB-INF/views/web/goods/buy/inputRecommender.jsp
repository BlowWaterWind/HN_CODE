<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Session shrioSession = UserUtils.getSession();
	UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
%>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<script type="text/javascript">
	$(function(){
		$("#confirmBtn").click(function(){
            var moduleTypeId =  $('input[name="moduleTypeId"]').val();
            if(moduleTypeId =="sim"){
                $("#recommenderForm").attr("action",ctx + "goodsBuy/linkToConfirmOrderSim");
            }
			$("#recommenderForm").submit();
		});
	});
</script>
</head>
<body>
<div class="top container">
  <div class="header sub-title">
	<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>推荐人信息</h1>
  </div>
  <div class="more-box"></div>
</div>

<form id="recommenderForm" action="linkToConfirmOrder" method="post">
	<%-- 跳转模块Id --%>
	<input type="hidden" name="moduleTypeId" value="${moduleTypeId}"/>
	<div id="outer" class="container">
	  <div class="wl-xy">
	    <ul class="order-info4 order-fr">
	      <li>
	        <label>推荐人工号：</label>
	        <div class="right-td right-ar">
	          <input type="text" class="form-control form-fr" name="recommendContact.recommendContactNo" value="${carModel.recommendContact.recommendContactNo}">
	        </div>
	      </li>
	      <li>
	        <label>推荐人姓名：</label>
	        <div class="right-td right-ar">
	          <input type="text" class="form-control form-fr" name="recommendContact.recommendContactName" value="${carModel.recommendContact.recommendContactName}">
	        </div>
	      </li>
	      <li>
	        <label>联系电话：</label>
	        <div class="right-td right-ar">
	          <input type="text" class="form-control form-fr" name="recommendContact.recommendContactPhone" value="${carModel.recommendContact.recommendContactPhone}">
	        </div>
	      </li>
	    </ul>
	  </div>
	  <a id="confirmBtn" href="javascript:void(0)" class="btn btn-blue btn-block center-btn">确 定</a>
	</div>
</form>
</body>
</html>