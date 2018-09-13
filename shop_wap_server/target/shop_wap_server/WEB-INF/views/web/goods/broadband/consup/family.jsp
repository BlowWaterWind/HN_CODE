<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--插码相关--%>
	<meta name="WT.si_n" content="KDDJ" />
	<meta name="WT.si_x" content="20" />
	<META name="WT.ac" content="">
	<Meta name="WT.mobile" content="">
	<Meta name="WT.brand" content="">
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
	<%--插码相关--%>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
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
    <h1>消费叠加型</h1>
  </div>
</div>
<div class="container">
	<form action="confirmOrder" id="form1" name="form1" method="post">
    <input type="hidden" id="phoneId" name="phoneId" value="${installPhoneNum}"/>
	<input type="hidden" name="installRealName" value="${installRealName}"/>
	<input type="hidden" name="minCost" value="${minCost}" id="minCost"/>
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
    
	<input type="hidden" id="install_county" name="install_county"/>
	
	<input type="hidden" name="memberListParamStr" id="memberListParamStr"/>
	<input type="hidden" name="memberListLength" id="memberListLength"/>
	<input type="hidden" name="memberFlag" id="memberFlag"/>

	<input type="hidden" id="form1_communityType" name="form1_communityType">
	
	<input type="hidden" id="payMode" name="payMode" value="0">
	<input type="hidden" id="payModeName" name="payModeName" value="直接办理">
	
	<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<dl>
				<c:choose>
					<c:when test="${minCost eq '98'}">
						<dt><img src="${tfsUrl}${consupostnItemList[0].imgUrl}"/></dt>
					</c:when>
					<c:otherwise>
						<dt><img src="${ctxStatic}/images/kd/family-logo.jpg"/></dt>
					</c:otherwise>
				</c:choose>
				<dd>
					<h4>${minCost}元保底套餐</h4>
					<p>${consupostnItemList[0].desc}</p>
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
						<c:forEach items="${consupostnItemList}" var="consupostnItem" varStatus="status" >
							<a class="bar-btn busi-btn" onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}');">${consupostnItem.bandWidth}M</a>
						</c:forEach>
					</div>
				</li>
					<li> <span class="font-gray">优惠价：</span>
						<div class="sub-text">0元/月
							<del class="font-gray9 ml10" id="origPrice1">原价：${consupostnItemList[0].discntCode}元/月</del>
							<p class="f12 font-red" id="origPrice2">优惠24个月，第三年按${consupostnItemList[0].discntCode}元/月收取</p>
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
								<c:forEach items="${consupostnItemList}" var="consupostnItem" varStatus="status" >
									<a class="bar-btn busi-btn" onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}');">${consupostnItem.bandWidth}M</a>
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
			           	  <span class="font-gray">手&nbsp;&nbsp;&nbsp;&nbsp;机：</span>
							<div class="sub-text"><input type="text" id="contactPhone" name="contactPhone" value="${installPhoneNum}" class="form-control" placeholder="请输入联系人手机号" /></div>
			          	</li>
					  	<li>
			              <span class="font-gray">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
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
	            	<li><label><div class="view-box"><input type="radio" id='wl'  name="form1_Mbh" disabled="disabled" value="1"/><b>未来TV(库存不足)</b> </div><small class="font-gray">央视连续剧、少儿动漫、极光专区、超级回看中的电影。</small></label></li>
	            	<li disabled="disabled"><label><div class="view-box"><input   type="radio" id='mg' checked="checked" name="form1_Mbh"  value="0"/><b>芒果TV</b> </div><small class="font-gray">湖南自制剧、热播电影、综艺、电影片库、动漫等。</small></label></li>
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
						<%--&lt;%&ndash;<a href="javascript:choosePayMode('pay-btn2','Q','话费抵扣')" class="bar-btn pay-btn pay-btn2">话费抵扣</a>&ndash;%&gt;--%>
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
		/*插码*/
		$(document).ready(function(){
			getGoodsId3();
		});
        // 若不选择预约装机，则隐藏预约时间与日期，否则显示
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
			/*插码*/
			try{
				var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
				if(window.Webtrends){
					Webtrends.multiTrack({
						argsa: ["WT.si_n",sin,
							"WT.si_x","21"],
						delayTime: 100
					})
				}else{
					if(typeof(dcsPageTrack)=="function"){
						dcsPageTrack ("WT.si_n",sin,true,
								"WT.si_x","21",true);
					}
				}
			}catch (e){
				console.log(e);
			}

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
		    	
		    	if($.trim(installName)==""){
			    	showAlert("请输入装机人姓名！");
			        return ;
			    }
				if($.trim(contactPhone)==""){
					showAlert("请输入联系人手机号码！");
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
    
    function getBroadbandInfo(bandWidth,discntCode,price,productId,packageId,goodsSkuId){
		  $("#discountCost").html(price);
		  $("#orig").html("原价："+discntCode+"元/月");
		  $("#monthCost").val(price);
		  $("#bandWidth").val(bandWidth);
		  $("#price").val(price);
		  $("#goodsSkuId").val(goodsSkuId);
		  $("#productId").val(productId);
		  $("#packageId").val(packageId);
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