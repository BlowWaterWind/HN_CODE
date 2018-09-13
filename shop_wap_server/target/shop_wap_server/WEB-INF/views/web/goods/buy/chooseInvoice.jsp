<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="org.apache.shiro.session.Session" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	Session shrioSession = UserUtils.getSession();
	UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
%>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<title>选择是否开发票</title>
<%@ include file="/WEB-INF/views/include/message.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
<script type="text/javascript">
	$(function(){

		//选择是否开发票
		$("input[name='isInvoicing']").click(function(){
			if($(this).val() == "1"){//开发票
				$("#invoicingTitleLi").show();
			}else{
				$("#invoicingTitleLi").hide();
			}

		});

		//确定
		$("#confirmBtn").click(function () {
			var isInvoicing = $("input[name='isInvoicing']:checked").val();
			var invoicingTitle = $.trim($("#invoicingTitle").val());
			var moduleTypeId = $("input[name='moduleTypeId']").val();
			if(isInvoicing == "1" && invoicingTitle.length == 0) {//开发票
				showAlert("请填写发票抬头！");
				return ;
			}
            if(moduleTypeId =="sim"){
                $("#chooseInvoicingForm").attr("action",ctx + "goodsBuy/linkToConfirmOrderSim");
            }

			$("#chooseInvoicingForm").submit();
		});
		
	});
</script>
</head>
<body>
<div class="top container">
  <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>发票</h1>
  </div>
</div>

<form id="chooseInvoicingForm" action="${ctx}goodsBuy/linkToConfirmOrder" method="post">
	<%-- 跳转模块Id --%>
	<input type="hidden" name="moduleTypeId" value="${moduleTypeId}"/>
	<div class="container pd-t45 white-bg">
	  <ul class="cur-ul choose-list">
	    <li>
	      <label>
	      <div class="tabcon-cl">
	        <input name="isInvoicing" type="radio" value="0"
					<c:if test="${carModel.isInvoicing == 0}">
						 checked="checked"
					</c:if>
			/>
	        <span class="lab-jl">不开</span>
	      </div>
	      </label>
	    </li>
		<li>
		  <label>
			  <div class="tabcon-cl">
				  <input name="isInvoicing" type="radio" value="1"
						  <c:if test="${carModel.isInvoicing == 1}">
							   checked="checked"
						  </c:if>
				  />
				  <span class="lab-jl">开</span>
			  </div>
		  </label>
		</li>
	    <li id="invoicingTitleLi" <c:if test="${carModel.isInvoicing == 0}"> style="display: none" </c:if> >
		  <div class="tabcon-cl">
			  <input id="invoicingTitle" type="text" name="invoicingTitle" class="form-control fr-dt01" placeholder="填写发票抬头" value="${carModel.invoicingTitle}"/>
		  </div>
	    </li>
	  </ul>
	</div>

	<div class="container zp-btn">
		<a id="confirmBtn" class="btn btn-blue btn-block" href="javascript:;">确定</a>
	</div>
</form>

<!--底部菜单start-->

<!--底部菜单end-->
</body>
</html>