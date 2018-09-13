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
	<!-- 宽带多级地址搜索框 -->
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-addr.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-agreement.js"></script>
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
    <h1>组网送宽带</h1>
  </div>
</div>
<div class="container">
	<form action="confirmOrder" id="form1" name="form1" method="post">
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

	<input type="hidden" id="form1_maxWidth" name="form1_maxWidth"/>
    <input type="hidden" id="form1_freePort" name="form1_freePort"/>
    <input type="hidden" id="form1_eparchyCode" name="eparchyCode"/>
    <input type="hidden" id="form1_coverType" name="form1_coverType"/>

	<input type="hidden" name="form1_communityType" id="form1_communityType">

	<input type="hidden" id="install_county" name="install_county"/>
	
	<input type="hidden" name="memberListParamStr" id="memberListParamStr"/>
	<input type="hidden" name="memberListLength" id="memberListLength"/>
	<input type="hidden" name="memberFlag" id="memberFlag"/>

		<input type="hidden" id="payMode" name="payMode" value="0">
		<input type="hidden" id="payModeName" name="payModeName" value="现金支付">
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
                        <c:forEach items="${consupostnItemList}" var="consupostnItem" varStatus="status" >
					  		<a class="bar-btn busi-btn" onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}');">${consupostnItem.bandWidth}M</a>
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
				<li> <span class="font-gray">初装费：</span>
					<div class="sub-text">${consupostnItemList[0].price}元</div>
				</li>
            </ul>
        </div>
    </div>
    <!--选择套餐 end-->
  	
  	<c:choose>
  		<c:when test="${isBroadBand=='1'}">
  			<!--地市选择 start-->
		<div class="add-con">
			<div class="add-list">
				<label>所在地区：</label>
				<div class="add-box">
					<p>
						<select class="form-control" style="height: 30px;" id="memberRecipientCity"
								name="memberRecipientCity">
						</select>
					</p>
					<p>
						<select class="form-control" style="height: 30px;" id="memberRecipientCounty"
								name="memberRecipientCounty">
						</select>
					</p>
				</div>
			</div>
			<%@ include file="/WEB-INF/views/web/goods/broadband/address.jsp"%>
		</div>
  			<!--地市选择 end-->
  			
  			<div class="wf-list sub-info sub-new">
		    	<div class="wf-ait clearfix">
			        <ul class="wf-con clearfix">
			          	<li>
			           	  <span class="font-gray">手机：</span>
							<div class="sub-text"><input type="text" id="contactPhone" name="contactPhone" value="${installPhoneNum}" class="form-control" placeholder="请输入联系人手机号" /></div>
			          	</li>
					  	<li>
			              <span class="font-gray">姓名：</span>
			              <div class="sub-text"><input type="text" id="installName" name="installName" class="form-control" placeholder="请与手机号码机主姓名保持一致" /></div>
			            </li>
			            <li>
			              <span class="font-gray">身份证：</span>
			              <div class="sub-text"><input type="text" id="idCard" name="idCard" class="form-control" placeholder="请与手机号码机主证件保持一致" /></div>
			            </li>
			        </ul>
		        </div>

				<div class="wf-ait isBook">
					<label class="font-gray">是否预约装机：</label>
					<input type="radio" name="isBookInstall" value="0">否
					<input type="radio" name="isBookInstall" value="1" checked="checked">是
				</div>

				<!--预约安装 start-->
				<div class="install-con install-time">
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
										<option value="上午08:00-12:00">上午08:00-12:00</option>
										<option value="中午12:00-14:00">中午12:00-14:00</option>
										<option value="下午14:00-18:00">下午14:00-18:00</option>
										<option value="晚上18:00-20:00">晚上18:00-20:00</option>
									</select>
								</div>
							</div>
						</li>
					</ul>
				</div>
		  		<!--预约安装 end-->
  		</c:when>
  		<c:otherwise>
  			<div class="wf-list sub-info sub-new">
	  			<div class="wf-list change-new sub-new">
					<div class="wf-ait clearfix">
						<ul class="bar-list">
							<li>
								<span class="font-gray">我的宽带账户：</span>
								<div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.accessAcct}</div>
								<input type="hidden" name="accessAcct" value="${broadbandDetailInfoResult.broadbandDetailInfo.accessAcct}"/>
							</li>
						</ul>
					</div>
				</div>
  		</c:otherwise>
  	</c:choose>
	  
	<c:if test="${isMbh == '1'}">
	    	<!--选择品牌 start-->
	        <div class="wf-ait clearfix">
	            <div class="wf-tit wf-cit font-gray">牌照方：</div>
	            <ul class="wf-con change-view mt10">
	            	<li><label><div class="view-box"><input type="radio" id='wl' name="form1_Mbh" value="1"  onclick="adc(this)"/><b>未来TV</b> </div><small class="font-gray">央视连续剧、少儿动漫、极光专区、超级回看中的电影。</small></label></li>
	            	<li disabled="disabled"><label><div class="view-box"><input type="radio" id='mg' name="form1_Mbh"    value="0" onclick="adc(this)"/><b>芒果TV</b></div><small class="font-gray">湖南自制剧、热播电影、综艺、电影片库、动漫等。</small></label></li>
	            </ul>
	        </div>
	        <!--选择品牌 end-->
        </div>
	</c:if>
	
	<%--<div class="wf-list change-new sub-new">--%>
		<%--<div class="wf-ait clearfix">--%>
			<%--<ul class="bar-list">--%>
				<%--<li style="height:26px;line-height:26px;">--%>
					<%--<span class="font-gray">支付方式：</span>--%>
					<%--<div class="sub-text">--%>
						<%--<a href="javascript:choosePayMode('pay-btn1','2','在线支付')" class="bar-btn pay-btn pay-btn1">在线支付</a>--%>
						<%--<a href="javascript:choosePayMode('pay-btn2','8','先装后付')" class="bar-btn pay-btn pay-btn2">话费抵扣</a>--%>
					<%--</div>--%>
				<%--</li>--%>
			<%--</ul>--%>
		<%--</div>--%>
	<%--</div>--%>
	
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
		  <p>（1）仅限大于等于38元的4G套餐用户办理，办理后不可降档。</p>
		  <p>（2）必须办理魔百和业务。</p>
		  <p>（3）用户必须已组建大于等于3人的非0元档家庭网（未组建必须先组建家庭网，不含家庭网体验用户），即可享受宽带免费用。一个家庭网仅限主卡办理一次。</p>
		  <p>（4）宽带免费优惠期限为两年，优惠到期后按标准资费收取宽带月费（40/50元/月），并下发短信提醒至办理号码。</p>
		  <p>（5）优惠期限内，若主卡套餐降档至38元以下，或者家庭网成员退出、离网或家庭网取消时，宽带免费优惠期限终止，系统直接按标准资费收取宽带月费，同时用户可以办理其他档次消费叠加型。</p>
	  </div>
      <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
    </div>
 </div>
</div>
<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
	$(function(){

        // 若选择不预约装机，则隐藏预约时间与日期，否则显示
        $('input:radio[name="isBookInstall"]').change(function () {
            if ($(this).val() == "0") {
                $(".install-time").hide();
            } else {
                $(".install-time").show();
            }
        });

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
		    	showAlert("您尚未办理家庭网业务，不能办理此业务");
		    	return;
		  	}
			  
		  	if($("#memberListLength").val() < 2){
			  	showAlert("您所在家庭网成员数<3人，不能办理此业务");
			  	return;
		  	}
			  
		  	if($("#memberFlag").val() != "1"){
			  	showAlert("您所在家庭网非主卡用户，需成为家庭网主卡用户才能办理该业务。！");
			  	return;
		  	}

			var houseCode = $("#houseCode").val();
		    var installName = $("#installName").val();
		    var idCard = $("#idCard").val();
		    var isBroadBand = $("#isBroadBand").val();
		    var isMbh = $("#isMbh").val();
			var contactPhone = $("#contactPhone").val();
		    if(isBroadBand == "1"){

                var isBookInstall = $('input:radio[name="isBookInstall"]:checked').val();
                var bookInstallDate = $("#bookInstallDate").val();
                var bookInstallTime = $("#bookInstallTime").val();

                if(isBookInstall == '1' ){
                    if(bookInstallDate == "" || bookInstallTime == ""){
                        showAlert("请选择预约装机时间！");
                        return;
                    }
                }

                if($.trim(houseCode)==""){
			    	showAlert("请选择装机地址！");
			        return ;
			    }
				if($.trim(contactPhone)==""){
					showAlert("请输入联系人手机号码！");
					return ;
				}
		    	if($.trim(installName)==""){
			    	showAlert("请输入装机人姓名！");
			        return ;
			    }
		    	
		    	if($.trim(idCard)==""){
			    	showAlert("请输入装机人身份证号码！");
			        return ;
			    }
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
    
    function getBroadbandInfo(bandWidth,discntCode,price,productId,packageId,goodsSkuId){
		  $("#discountCost").html(price);
		  $("#orig").html("原价："+discntCode+"元/月");
		  
		  $("#monthCost").val(price);
		  $("#bandWidth").val(bandWidth);
		  $("#price").val(price);
		  $("#goodsSkuId").val(goodsSkuId);
		  $("#productId").val(productId);
		  $("#packageId").val(packageId);
		  
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
		  			}
			  },
			  error: function(e) {
				  showAlert("家庭网查询出错！");
			  }
		  });
	  }
    
    function choosePayMode(obj,payMode,payModeName){
    	$(".pay-btn").removeClass("active");
		$("." + obj).addClass("active");
		
    	
    	$("#payMode").val(payMode);
    	$("#payModeName").val(payModeName);
    }
	function  adc(param){
		if($(param).attr('previousValue') == 'checked'){
			$(param).attr("checked",false);
			$(param).attr('previousValue', false);
		} else {
			$(param).attr('previousValue', 'checked');
		}
	}
</script>
</body>
</html>