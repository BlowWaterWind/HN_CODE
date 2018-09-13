<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/iOSselect/iosSelect.css"/>
	<script type="text/javascript" src="${ctxStatic}/js/datePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-agreement.js"></script>
	<%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>


	<script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>消费叠加型</h1>
  </div>
</div>
<div class="container">
	<form action="toInstall" id="form1" name="form1" method="post">
    <input type="hidden" id="phoneId" name="phoneId" value="${installPhoneNum}"/>
	<input type="hidden" name="minCost" value="${minCost}" id="minCost"/>
	<input type="hidden" name="monthCost" id="monthCost"/>
	<input type="hidden" name="bandWidth" id="bandWidth"/>
	<input type="hidden" name="price" id="price"/>
	<input type="hidden" name="goodsSkuId" id="goodsSkuId"/>
	<input type="hidden" name="goodsName" id="goodsName"/>
	<input type="hidden" name="productId" id="productId"/>
	<input type="hidden" name="packageId" id="packageId"/>
	<input type="hidden" name="eparchyCode" id="eparchy_Code" value="${eparchyCode}" />
	<input type="hidden" name="isMbh" id="isMbh" value="${isMbh}"/>
	<input type="hidden" name="isBroadband" id="isBroadBand" value="${isBroadBand}"/>
	<input type="hidden" id="busiType" name="busiType" value="${busiType}"/>

	<input type="hidden" name="memberListStr" id="memberListParamStr"/>
	<input type="hidden" name="memberListLength" id="memberListLength"/>
	<input type="hidden" name="memberFlag" id="memberFlag"/>

	<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<dl>
				<c:choose>
					<c:when test="${minCost eq '98'}">
						<dt><img src="${tfsUrl}${broadItemList[0].imgUrl}"/></dt>
					</c:when>
					<c:otherwise>
						<dt><img src="${ctxStatic}/images/kd/family-logo.jpg"/></dt>
					</c:otherwise>
				</c:choose>
				<dd>
					<h4>${minCost}元保底套餐</h4>
					<p>${broadItemList[0].desc}</p>
				</dd>
			</dl>
		</div>
	</div>
	<!--套餐信息 end-->

	<!--选择套餐 start-->
    <div class="wf-list change-new sub-new">
        <div class="wf-ait clearfix">
            <ul class="bar-list">
				<c:choose>
				<c:when test="${minCost eq '98'}">
				<li> <span class="font-gray">宽带速率：</span>
					<div class="sub-text">
						<c:forEach items="${broadItemList}" var="consupostnItem" varStatus="status" >
							<a class="bar-btn busi-btn" onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}','${consupostnItem.broadbandItemName}');">${consupostnItem.bandWidth}M</a>
						</c:forEach>
					</div>
				</li>
					<li> <span class="font-gray">优惠价：</span>
						<div class="sub-text">0元/月
							<del class="font-gray9 ml10" id="origPrice1">原价：${broadItemList[0].discntCode}元/月</del>
							<p class="f12 font-red" id="origPrice2">优惠24个月，第三年按${broadItemList[0].discntCode}元/月收取</p>
						</div>
					</li>
					<li> <span class="font-gray">电视月费：</span>
						<div class="sub-text">20元/月</div>
					</li>
					<%--<li> <span class="font-gray">设备费用：</span>--%>
						<%--<div class="sub-text">10元/月--%>
							<%--<p class="f12 font-red">收取10个月，包含光猫与机顶盒费用</p>--%>
						<%--</div>--%>
					<%--</li>--%>
					</c:when>
					<c:otherwise>
						<li> <span class="font-gray">宽带月费：</span>
							<div class="sub-text sub-broad">
								<c:forEach items="${broadItemList}" var="consupostnItem" varStatus="status" >
									<a class="bar-btn busi-btn" onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}','${consupostnItem.broadbandItemName}');">${consupostnItem.bandWidth}M</a>
								</c:forEach>
							</div>
						</li>
						<li> <span class="font-gray">优惠价：</span>
							<div class="sub-text font-red"><span id="discountCost">0</span>元/月</div>
						</li>
						<li> <span class="font-gray">电视月费：</span>
							<div class="sub-text">20元/月</div>
						</li>
						<li> <span class="font-gray">保底消费：</span>
							<div class="sub-text">${minCost}元/月</div>
						</li>
					</c:otherwise>
				</c:choose>
            </ul>
        </div>
    </div>
    <!--选择套餐 end-->
	</form>
	
	<!--协议 start-->
    <div class="broad-agre">
        <label><input type="checkbox" id="isRead"/> 我已阅读并同意<a href="javascript:void(0);" data-toggle="modal" data-target="#agreementModal">《宽带入网协议》</a></label>
    </div>
</div>

<div class="fix-br">
	<div class="affix foot-box new-foot">
		<div class="container active-fix">
			<div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#tipsModal">温馨提示</a></div>
			<div class="fl-right"><a id="operBtn">下一步</a></div>
			<a id="chooseMember" class="hide" data-toggle="modal" data-target="#myModal2"></a>
		</div>
	</div>
</div>

<!--温馨提示弹框 start-->
<div class="modal fade modal-prompt" id="tipsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog new-modal">
    <div class="modal-dow">
      <h4>温馨提示</h4>
      <div class="modal-text">
		  <c:choose>
		  <c:when test="${minCost eq '98'}">
			  <p> 1、保底时间24个月，承诺保底期内不降挡或取消保底，24个月后按20元/月宽带功能费；</p>
			  <p> 2、魔百和标准资费为20元/月，从手机账户上扣取；</p>
			  <p> 3、保底套餐当月办理，当月生效，当月按天扣费并分摊，宽带和魔百和免办理当月月功能费，不计入整体优惠期；</p>
			  <p> 4、手机号码停机，宽带、电视停机（宽带电视延迟3天停机，3天内手机复机，宽带电视不停机），手机号码复机，宽带、电视立即复机，手机销号或停机120天以上，同步取消宽带、电视和手机号码的绑定关系，终止宽带、电视。</p>
			  <p> 5、办理成员人数需达到4人及以上，不区分主副卡。同一集团客户只能参与一次本次活动</p>
		  </c:when>
			  <c:otherwise>
				  <p>（1）仅限家庭网用户办理。</p>
				  <p>（2）同一家庭网成员只能办理一次。</p>
				  <p>（3）惠期12个月，12个月内不能转出，到期之后按标准资费收取月费。</p>
				  <p>（4）家庭网成员合计消费承诺达到158元/月，如果不足约定金额，系统将自动从主卡手机帐号扣费补足差额。</p>
			  </c:otherwise>
		  </c:choose>
	  </div>
      <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
    </div>
 </div>
</div>
<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog new-modal broad-modal">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <div class="modal-dow">
           <h4>提示</h4>
            <div class="modal-text">
                <p>请选择您要加入保底的家庭网成员：</p>
                <ul id="memberListUl" class="modal-form-box"></ul>
            </div>
            <div class="modal-btn"><a id="confirmBtn" data-dismiss="modal">确认</a></div>
        </div>
    </div>
</div>

<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
    $(function () {


		$(".busi-btn").click(function(){
			$(".busi-btn").removeClass("active");
			$(this).addClass("active");
		});

		$("#operBtn").click(function(){
			var flag = false;
			$(".busi-btn").each(function(){
		    	if($(this).attr("class").indexOf("active") != -1){
		    		flag = true;
		    	}
			});
			if(!flag){
				showAlert("请选择组家庭网送宽带套餐档次");
				return;
			}
			

		    if($("#memberListLength").val() == "0"){
			  	showAlert("您尚未办理家庭网业务，赶紧去办理吧！");
			  	return;
		  	}

		    if(!$('#isRead').is(':checked')){
		    	showAlert("请阅读并同意《宽带入网协议》!");
		  		return;
		    }
		    
		    $("#chooseMember").click();
		});
		
		$("#confirmBtn").click(function(){
			var chooseMemberList = '';
			  
			$("#memberListUl input[type='radio']:checked").each(function () {
				chooseMemberList += "|" + $(this).val();
			});
			  
			$("#memberListParamStr").val(chooseMemberList.substr(1,chooseMemberList.length));
			
			$("#form1").submit();
		});
		
		$(".bar-list li:first div a:first").click();
	});
    
    function choosePayMode(obj,payMode,payModeName){
    	$(".pay-btn").removeClass("active");
		$("." + obj).addClass("active");
		
    	
    	$("#payMode").val(payMode);
    	$("#payModeName").val(payModeName);
    }
</script>

<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
    
    function getBroadbandInfo(bandWidth,discntCode,price,productId,packageId,goodsSkuId,goodsName){
		  $("#discountCost").html(discntCode);
		  $("#orig").html("原价："+discntCode+"元/月");
		  $("#monthCost").val(discntCode);
		  $("#bandWidth").val(bandWidth);
		  $("#price").val(price);
		  $("#goodsSkuId").val(goodsSkuId);
		  $("#productId").val(productId);
		  $("#packageId").val(packageId);
		  $("#goodsName").val(goodsName);
		if($("#minCost").val()=='98') {
				$("#origPrice1").html("原价：" + discntCode + "元/月");
				$("#origPrice2").html("优惠24个月，第三年按" + discntCode + "元/月收取");
			}
		  $.ajax({
			  url: ctx+"consupostn/getFamilyNetMemberInfo", 
			  data: {phoneId : $("#phoneId").val(),productId:$("#productId").val(),discntCode:$("#packageId").val()},
			  dataType: "json",
			  type : 'post',
			  success: function(e){
				  if(e.resCode == "0"){
		  				$("#memberListParamStr").val(e.memberListParamStr);
		  				if(e.memberListParamStr == null || e.memberListParamStr == "null"){
					  		$("#memberListLength").val(0);
					  	}else{
					  		$("#memberListLength").val(e.memberListLength);
					  	}
		  				$("#memberFlag").val(e.memberFlag);
		  				
		  				var memberListParamStr = e.memberListParamStr;
					    var memberArr = memberListParamStr.split('|');
					    var tempHtml = '';
						  
						for(var i=0;i<memberArr.length;i++){
							  tempHtml += '<li>';
							  tempHtml += '<label>';
				  		      tempHtml += '<input type="radio" value="'+memberArr[i]+'"/>';
				  		      tempHtml += '<span> '+memberArr[i]+'</span>';
				  		      tempHtml += '</label>';
				  		      tempHtml += '</li>';
						}
						  
			  		    $("#memberListUl").html(tempHtml);
		  			}
			  },
			  error: function(e) {
				  showAlert("家庭网查询出错！");
			  }
	    });
	  }
</script>
</body>
</html>