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
    	
    	var ua = navigator.userAgent.toLowerCase();
    	if(ua.match(/leadeon/i)=="leadeon"){
            //走手厅
	            document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc_leadeon.js"><\/script>');
	      }else{
	            document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc9.js"><\/script>');
	            document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"><\/script>');
	      }
    </script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>组网送宽带</h1>
  </div>
</div>
<div class="container">
	<form action="toInstall" id="form1" name="form1" method="post">
    <input type="hidden" id="phoneId" name="phoneId" value="${installPhoneNum}"/>
	<input type="hidden" name="minCost" value="0"/>
	<input type="hidden" name="monthCost" id="monthCost" value="0"/>
	<input type="hidden" name="bandWidth" id="bandWidth"/>
	<input type="hidden" name="price" id="price"/>
	<input type="hidden" name="goodsSkuId" id="goodsSkuId"/>
	<input type="hidden" name="goodsName" id="goodsName"/>
	<input type="hidden" name="productId" id="productId"/>
	<input type="hidden" name="packageId" id="packageId"/>
	<input type="hidden" name="isMbh" id="isMbh" value="${isMbh}"/>
	<input type="hidden" name="isBroadband" id="isBroadBand" value="${isBroadBand}"/>
	<input type="hidden" name="eparchyCode" id="eparchy_Code" value="${eparchyCode}" />
	<input type="hidden" id="busiType" name="busiType" value="${busiType}"/>

	<input type="hidden" name="memberListStr" id="memberListParamStr"/>
	<input type="hidden" name="memberListLength" id="memberListLength"/>
	<input type="hidden" name="memberFlag" id="memberFlag"/>
	<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<dl>
				<dt><img src="${ctxStatic}/images/kd/familynet-logo.jpg"/></dt>
				<dd>
					<h4>组家庭网送宽带</h4>
					<p>套餐内容：100M光纤宽带免费用，4K高清互联网电视20元/月，光猫、机顶盒费用全免。</p>
				</dd>
			</dl>
		</div>
	</div>
	<!--套餐信息 end-->

	<!--选择套餐 start-->
    <div class="wf-list change-new sub-new">
        <div class="wf-ait clearfix">
            <ul class="bar-list">
                <li> <span class="font-gray">宽带月费：</span>
                    <div class="sub-text sub-broad">
                        <c:forEach items="${broadItemList}" var="consupostnItem" varStatus="status" >
					  		<a class="bar-btn busi-btn" onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}','${consupostnItem.broadbandItemName}');">${consupostnItem.bandWidth}M</a>
					  	</c:forEach>
                    </div>
                </li>
                <li> <span class="font-gray">优惠价：</span>
                    <div class="sub-text font-red"><span id="discountCost">0</span>元/月
                        <del class="font-gray9 f12" id="orig">原价：0元/月</del>
                        <p class="f12">第三年按原价收取宽带月费</p>
                    </div>
                </li>
                <li> <span class="font-gray">电视月费：</span>
                    <div class="sub-text">20元/月</div>
                </li>
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
		</div>
	</div>
</div>

<!--温馨提示弹框 start-->
<div class="modal fade modal-prompt" id="tipsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog new-modal">
    <div class="modal-dow">
      <h4>温馨提示</h4>
      <div class="modal-text">
    	<p>（1）仅限4G套餐用户办理。</p>
		<p>（2）必须办理魔百和业务。</p>
		<p>（3）用户必须已组建大于等于3人的非0元档家庭网（未组建必须先组建家庭网，不含家庭网体验用户），即可享受宽带免费用。一个家庭网仅限主卡办理一次。</p>
		<p>（4）宽带免费优惠期限为两年，优惠到期后按标准资费收取宽带月费（40/50/100/300/600元/月），并下发短信提醒至办理号码。</p>
		<p>（5）优惠期限内，若家庭网成员退出、离网或家庭网取消时，宽带免费优惠期限终止，系统直接按标准资费收取宽带月费，同时用户可以办理其他档次消费叠加型。</p>
      </div>
      <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
    </div>
 </div>
</div>
<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
	$(function(){
		var WTjson=WTjson||[];
		WTjson["WT.branch"]="hn";
		var ua = navigator.userAgent.toLowerCase();
		var st = '';
		if(ua.match(/leadeon/i)=="leadeon"){
			//手厅插码
			WTjson["WT.plat"]="app";
			leadeon.init=function(){
				//获取用户信息
				leadeon.getUserInfo({
					debug:false,
					success:function(res){
						//保存用户信息
						if(!!res.cid){
							WTjson["WT.cid"]=res.cid;
						}
						
						if(!!res.phoneNumber){
							WTjson["WT.mobile"]=res.phoneNumber;
						}
					},
					error:function(res1){
						
					}
				});
			}
		}else{
			WTjson["WT.plat"]="touch";
		}
		


		$(".busi-btn").click(function(){
			$(".busi-btn").removeClass("active");
			$(this).addClass("active");
		});

		$("#operBtn").click(function(){
			if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","组网送宽带",0,"WT.si_x","组网送宽带办理",0)}
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
			  	showAlert("您尚未办理家庭网业务！");
			  	return;
		  	}
			  
		  	if($("#memberListLength").val() < 2){
			  	showAlert("您所在家庭网成员数<3人，加入更多家庭网成员即可享受该优惠！");
			  	return;
		  	}
			  
		  	if($("#memberFlag").val() != "1"){
			  	showAlert("您所在家庭网非主卡用户，需成为家庭网主卡用户才能办理该业务！");
			  	return;
		  	}

		    if(!$('#isRead').is(':checked')){
		    	showAlert("请阅读并同意《宽带入网协议》!");
		  		return;
		    }
		    
			$("#form1").submit();
		});
		
		$(".bar-list li:first div a:first").click();
	});
	

</script>

<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
    
    function getBroadbandInfo(bandWidth,discntCode,price,productId,packageId,goodsSkuId,goodsName){
		if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","组网送宽带",0,"WT.si_x","组网送宽带选择产品档次"+bandWidth+"M "+price,0)}

		  //$("#discountCost").html(discntCode);
		  $("#orig").html("原价："+discntCode+"元/月");
		  
		  //$("#monthCost").val(price);
		  $("#bandWidth").val(bandWidth);
		  $("#price").val(price);
		  $("#goodsSkuId").val(goodsSkuId);
		  $("#productId").val(productId);
		  $("#packageId").val(packageId);
		 $("#goodsName").val(goodsName);

		$.ajax({
			  url: ctx+"consupostn/getFamilyNetMemberInfo", 
			  data: {phoneId : $("#phoneId").val(),productId:$("#productId").val(),discntCode:$("#packageId").val()},
			  dataType: "json",
			  type : 'post',
			  success: function(e){
				  if(e.resCode == "0"){
					    if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","组网送宽带",0,"WT.si_x","查询家庭网成员成功",0)}
					  	if(e.memberListParamStr == null || e.memberListParamStr == "null"){
					  		$("#memberListLength").val(0);
					  	}else{
					  		$("#memberListLength").val(e.memberListLength);
					  	}
		  				$("#memberListParamStr").val(e.memberListParamStr);
		  				
		  				$("#memberFlag").val(e.memberFlag);
		  			}else{
		  				if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","组网送宽带",0,"WT.si_x","查询家庭网成员失败",0)}
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