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
	<!-- 宽带多级地址搜索框 -->
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-addr-forfree.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-agreement.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/iOSselect/iosSelect.css"/>
	<script type="text/javascript" src="${ctxStatic}/js/datePicker.js"></script>
<%-- 	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/consupostn.js"></script> --%>
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>

	<!-- 搜索地址框placeholder处理 -->
	<style>
		/* WebKit browsers */
		input:focus::-webkit-input-placeholder { color:transparent; }
		/* Mozilla Firefox 4 to 18 */
		input:focus:-moz-placeholder { color:transparent; }
		/* Mozilla Firefox 19+ */
		input:focus::-moz-placeholder { color:transparent; }
		/* Internet Explorer 10+ */
		input:focus:-ms-input-placeholder { color:transparent; }
		
		.broad-agre {color: #000000;}
		.broad-agre a{color: #0085d0;}
	</style>

	<script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>确认套餐</h1>
  </div>
</div>
<div class="container">
	<form action="confirmKingOrder" id="form1" name="form1" method="post">
    <input type="hidden" id="phoneId" name="phoneId" value="${installPhoneNum}"/>
	<input type="hidden" name="installRealName" value="${installRealName}"/>
	<input type="hidden" name="minCost" value="0"/>
	<input type="hidden" name="monthCost" id="monthCost"/>
	<input type="hidden" name="bandWidth" id="bandWidth"/>
	<input type="hidden" name="price" id="price"/>
	<input type="hidden" name="goodsSkuId" id="goodsSkuId"/>
	<input type="hidden" name="productId" id="productId"/>
	<input type="hidden" name="packageId" id="packageId"/>
	<input type="hidden" name="eparchyCode" id="eparchy_Code" value="${eparchy_Code}" />
	<input type="hidden" name="isMbh" id="isMbh" value="${isMbh}"/>
	<input type="hidden" name="isBroadBand" id="isBroadBand" value="${isBroadBand}"/>
	<input type="hidden" id="communityType" name="communityType"/>

		<input type="hidden" id="form1_maxWidth" name="form1_maxWidth"/>
    <input type="hidden" id="form1_freePort" name="form1_freePort"/>
    <input type="hidden" id="form1_eparchyCode" name="eparchyCode"/>
    <input type="hidden" id="form1_coverType" name="form1_coverType"/>
    
	<input type="hidden" id="install_county" name="install_county"/>
	
	<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<dl>
				<dt><img src="${ctxStatic}/images/kd/familynet-logo.jpg"/></dt>
				<dd>
					<h4>${goodsTemp.broadbandItemName}</h4>
					<p>${goodsTemp.desc}</p>
				</dd>
			</dl>
		</div>
	</div>
	<!--套餐信息 end-->

	<!--选择套餐 start-->
    <div class="wf-list change-new sub-new">
        <div class="wf-ait clearfix">
            <ul class="bar-list">
                <li> <span class="font-gray">宽带速率：</span>
                    <div class="sub-text sub-broad">
                        <c:forEach items="${consupostnItemList}" var="consupostnItem" varStatus="status" >
					  		<a class="bar-btn" href="javascript:getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}');">${consupostnItem.bandWidth}M</a>
					  	</c:forEach>
                    </div>
                </li>
                <li> <span class="font-gray">优惠价：</span>
                    <div class="sub-text font-red"><span id="discountCost">1</span>元/天
                        <del class="font-gray9 f12" id="orig">原价：1元/天</del>
                    </div>
                </li>

            </ul>
        </div>
    </div>
    <!--选择套餐 end-->
  	
  	<%--<c:choose>--%>
  		<%--<c:when test="${isBroadBand=='1'}">--%>

  			
  			<div class="wf-list sub-info sub-new">
					<div class="wf-ait clearfix">
						<div class="wf-tit wf-cit mb10"><span class="font-gray">电视月费：</span>20元/月</div>
						<div class="wf-tit wf-cit font-gray">电视牌照方：</div>
						<ul class="wf-con change-view mt10">
							<li><label><div class="view-box"><input type="radio" id='wl' name="form1_Mbh" value="1" checked="checked"/><b>未来TV</b> </div><small class="font-gray">央视连续剧、少儿动漫、极光专区、超级回看中的电影。</small></label></li>
							<li disabled="disabled"><label><div class="view-box"><input disabled="disabled" type="radio" id='mg' name="form1_Mbh"  value="0"/><b>芒果TV</b><br/>　（库存不足）</div><small class="font-gray">湖南自制剧、热播电影、综艺、电影片库、动漫等。</small></label></li>
						</ul>
					</div>
				</div>
					<!--地市选择 start-->
					<div id="broadband_addr_div"></div>
					<!--地市选择 end-->
				<!--预约安装 start-->
		<div class="wf-list sub-info sub-new">
				<div class="install-con">
					<h2 class="install-title">安装时间</h2>
					<ul class="change-list install-list">
						<li>
							<label>预约安装时间：</label>
							<div class="right-td">
								<div class="user-form" id="selectDate">
									<input type="hidden" name="bookInstallDate" id="bookInstallDate">
									<div class="form-control" data-year="" data-month="" data-date="" id="showDate">未选择</div>
									<span class="arrow"></span>
								</div>
							</div>
						</li>
						<li>
							<label>安装日期时段：</label>
							<div class="right-td">
								<div class="td-fr"><i class="select-arrow"></i>
									<select id="bookInstallTime" name="bookInstallTime" class="form-control">
										<option value="">---请选择---</option>
										<option value="0">08:00:00-12:00:00</option>
										<option value="1">12:00:00-14:00:00</option>
										<option value="2">14:00:00-18:00:00</option>
										<option value="3">18:00:00-20:00:00</option>
									</select>
								</div>
							</div>
						</li>
					</ul>
				</div>
        </div>
	</form>
	
	<!--协议 start-->
    <div class="broad-agre">
        <label><input type="checkbox" id="isRead"/> 我已阅读并同意<a href="javascript:void(0);" data-toggle="modal" data-target="#agreementModal">《宽带入网协议》</a></label>
    </div>
</div>

<div class="fix-br">
	<div class="affix foot-box new-foot">
		<div class="container active-fix">
			<div class="fl-left">
				<p class="ft-total">合计：<span class="font-red">20元/月</span></p>
			</div>
			<div class="fl-right"><a href="javascript:void(0);" id="operBtn">提交办理</a></div>
			<!--当用户不能点击时在a class加dg-gray-->
		</div>
	</div>
</div>


<!--提交办理弹框 start-->
<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog broadband-dialog">
		<div class="modal-con">
			<div class="modal-text">
				<p>您将办理大王卡100M宽带，请确认办理。</p>
			</div>
			<div class="dialog-btn">
				<a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
				<a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a>
			</div>
		</div>
	</div>
</div>
<!--提交办理弹框 end-->

<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
	$(function(){
		$(".bar-btn").click(function(){
			$(".bar-btn").removeClass("active");
			$(this).addClass("active");

			$("#goodsSkuId").val($(this).parent().find("#choose_skuId").val());
			$("#productId").val($(this).parent().find("#choose_productId").val());
			$("#packageId").val($(this).parent().find("#choose_packageId").val());
			$("#monthCost").val($(this).parent().find("#choose_discntCode").val());
			$("#price").val($(this).parent().find("#choose_price").val());
			$("#bandWidth").val($(this).parent().find("#choose_bandwidth").val());
		});

		$("#operBtn").click(function(){
//			$("#myModal2").modal('show');
			var flag = false;
			$(".bar-btn").each(function(){
		    	if($(this).attr("class").indexOf("active") != -1){
		    		flag = true;
		    	}
			});
			if(!flag){
				showAlert("请选择大王卡宽带套餐档次");
				return;
			}
			

			var houseCode = $("#houseCode").val();
		    var installName = $("#installName").val();
		    var idCard = $("#idCard").val();
		    var isBroadBand = $("#isBroadBand").val();
		    var isMbh = $("#isMbh").val();
			var bookInstallDate  = $("#bookInstallDate").val();
			var bookInstallTime = $("#bookInstallTime").val();

//		    if(isBroadBand == "1"){
		    	if($.trim(houseCode)==""){
			    	showAlert("请选择装机地址！");
			        return ;
			    }
		    	
		    	if($.trim(bookInstallDate)==""){
			    	showAlert("请选择装机日期！");
			        return ;
			    }
//
		    	if($.trim(bookInstallTime)==""){
			    	showAlert("请选择装机时间！");
			        return ;
			    }
//		    }
		    
		    if(!$('#isRead').is(':checked')){
		    	showAlert("请阅读并同意《宽带入网协议》!");
		  		return;
		    }
		    
			$("#form1").submit();
		});
	});
</script>

<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
    
    function getBroadbandInfo(bandWidth,discntCode,price,productId,packageId,goodsSkuId){
		  $("#discountCost").html(discntCode);
		  $("#orig").html("原价："+discntCode+"元/天");
		  
		  $("#monthCost").val(discntCode);
		  $("#bandWidth").val(bandWidth);
		  $("#price").val(discntCode);
		  $("#goodsSkuId").val(goodsSkuId);
		  $("#productId").val(productId);
		  $("#packageId").val(packageId);
		  
//		  $.ajax({
//			  url: ctx+"consupostn/getFamilyNetMemberInfo",
//			  data: {phoneId : $("#phoneId").val(),productId:$("#productId").val(),discntCode:$("#packageId").val()},
//			  dataType: "json",
//			  type : 'post',
//			  success: function(e){
//				  if(e.resCode == "0"){
//		  				$("#memberListParamStr").val(e.memberListParamStr);
//		  				$("#memberListLength").val(e.memberListLength);
//		  				$("#memberFlag").val(e.memberFlag);
//		  			}
//			  },
//			  error: function(e) {
//				  showAlert("家庭网查询出错！");
//			  }
//		  });
	  }
</script>
</body>
</html>