<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--插码相关--%>
	<meta name="WT.si_n" content="KDDJ" />
	<meta name="WT.si_x" content="20" />
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
<%--<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-ims.js"></script>--%>
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
    <input type="hidden" name="phoneId" value="${installPhoneNum}"/>
	<input type="hidden" name="installRealName" value="${installRealName}"/>
	<input type="hidden" name="minCost" value="${minCost}" id="minCost"/>
	<input type="hidden" name="monthCost" id="monthCost"/>
	<input type="hidden" name="bandWidth" id="bandWidth"/>
	<input type="hidden" name="price" id="price"/>
	<input type="hidden" name="goodsSkuId" id="goodsSkuId"/>
	<input type="hidden" name="productId" id="productId"/>
	<input type="hidden" name="packageId" id="packageId"/>
	<input type="hidden" name="eparchyCode"  id="eparchy_Code" value="${eparchy_Code}" />
	<input type="hidden" id="isMbh" name="isMbh" value="${isMbh}"/>
	<input type="hidden" id="isBroadBand" name="isBroadBand" value="${isBroadBand}"/>

	<input type="hidden" id="form1_maxWidth" name="form1_maxWidth"  />
    <input type="hidden" id="form1_freePort" name="form1_freePort"  />
    <input type="hidden" id="form1_eparchyCode" name="eparchyCode"  />
    <input type="hidden" id="form1_coverType" name="form1_coverType"/>
	<input type="hidden" id="install_county" name="install_county"/>

	<input type="hidden" id="form1_communityType" name="form1_communityType">

	<input type="hidden" id="payMode" name="payMode" value="0">
	<input type="hidden" id="payModeName" name="payModeName" value="直接办理">

	<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<dl>
				<dt><img src="${tfsUrl}${consupostnItemList[0].imgUrl}"/></dt>
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
					<c:when test="${minCost eq '88'}">
						<li> <span class="font-gray">宽带速率：</span>
							<div class="sub-text">
								<c:forEach items="${consupostnItemList}" var="broadbandItem">
									<a onclick="getBusiInfo('${broadbandItem.goodsSkuId}','${broadbandItem.bandWidth}','${broadbandItem.price}','${broadbandItem.productId}','${broadbandItem.packageId}','${broadbandItem.discntCode}')" class="bar-btn busi-btn">${broadbandItem.bandWidth}M</a>
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
							<div class="sub-text">
								<c:forEach items="${consupostnItemList}" var="broadbandItem">
									<a onclick="getBusiInfo('${broadbandItem.goodsSkuId}','${broadbandItem.bandWidth}','${broadbandItem.price}','${broadbandItem.productId}','${broadbandItem.packageId}','${broadbandItem.discntCode}')" class="bar-btn busi-btn">${broadbandItem.bandWidth}M<b>${broadbandItem.discntCode}元/月</b></a>
								</c:forEach>
							</div>
						</li>
						<li> <span class="font-gray">电视月费：</span>
							<div class="sub-text">20元/月</div>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
	<div class="wf-list change-new sub-new <c:if test="${isBroadBand == '1'}">hide</c:if>">
		<div class="wf-ait clearfix">
			<ul class="bar-list">
				<li>
					<span class="font-gray">我的宽带账户：</span>
					<div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.accessAcct}</div>
				</li>
			</ul>
		</div>
	</div>

	<div class="wf-list change-new sub-new hide">
		<div class="wf-ait clearfix">
			<ul class="bar-list">
				<li>
					<span class="font-gray">我的互联网电视账户：</span>
					<div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.accessAcct}</div>
				</li>
			</ul>
		</div>
	</div>

	<!--地市选择 start-->
	<c:choose>
    <c:when test="${isBroadBand=='1'}">
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
	</c:when>
    </c:choose>
  	<!--地市选择 end-->

	<!--填写信息 start-->
	<div class="wf-list sub-info sub-new">
      <div class="wf-ait clearfix">
        <ul class="wf-con clearfix">
          <c:choose>
          <c:when test="${isBroadBand == '1'}">
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
          </c:when>
          <c:otherwise>
            <li>
           	  	<span class="font-gray">手&nbsp;&nbsp;&nbsp;&nbsp;机：</span>
           	  	<div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.serialNumber}</div>
          	</li>
		  	<li>
              <span class="font-gray">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
              <div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.custName}</div>
            </li>
            <li>
              <span class="font-gray">身份证：</span>
              <div class="sub-text">${broadbandDetailInfoResult.broadbandDetailInfo.psptId}</div>
            </li>
          </c:otherwise>
          </c:choose>
        </ul>
      </div>

		<div class="wf-ait isBook <c:if test="${isBroadBand == '0'}">hide</c:if>">
			<label class="font-gray">是否预约装机：</label>
			<input type="radio" name="isBookInstall" value="0">否
			<input type="radio" name="isBookInstall" value="1" checked="checked">是
		</div>

	  <!--预约安装 start-->
		<div class="install-con install-time <c:if test="${isBroadBand == '0'}">hide</c:if>">
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
      <!--选择品牌 start-->
      <div class="wf-ait clearfix <c:if test="${isMbh == '0'}">hide</c:if>">
        <div class="wf-tit wf-cit font-gray">牌照方：</div>
        <ul class="wf-con mt10">
        	<li><label><input type="radio" id='wl' name="form1_Mbh"   value="1" disabled="disabled"/><b>未来电视（库存不足）</b><small class="font-gray">央视连续剧、少儿动漫、极光专区、超级回看中的电影。</small></label></li>
         	<li disabled="disabled"><label><input type="radio"   id='mg' name="form1_Mbh" checked="checked" value="0"/><b>芒果TV</b> <small class="font-gray">湖南自制剧、热播电影、综艺、电影片库、动漫等。</small></label></li>
        </ul>
      </div>
      <!--选择品牌 end-->
    </div>
	<%--<div class="wf-list change-new sub-new">--%>
		<%--<div class="wf-ait clearfix">--%>
			<%--<ul class="bar-list">--%>
				<%--<li style="height:26px;line-height:26px;"> --%>
					<%--<span class="font-gray">支付方式：</span>--%>
					<%--<div class="sub-text">--%>
						<%--<a href="javascript:choosePayMode('pay-btn1','2','在线支付')" class="bar-btn pay-btn pay-btn1">在线支付</a>--%>
						<%--&lt;%&ndash;<a href="javascript:choosePayMode('pay-btn2','Q','话费抵扣')" class="bar-btn pay-btn pay-btn2">话费抵扣</a>&ndash;%&gt;--%>
					<%--</div>--%>
				<%--</li>--%>
			<%--</ul>--%>
		<%--</div>--%>
	<%--</div>--%>
		<div class="broad-agre">
			<label><input type="checkbox" id="isRead"/> 我已阅读并同意<a href="javascript:void(0);" data-toggle="modal" data-target="#agreementModal">《宽带入网协议》</a></label>
		</div>
	<div class="broadband-ims"></div>
	</form>
</div>

<div class="fix-br">
	<div class="affix foot-box new-foot">
		<div class="container active-fix">
			<div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#myModal">温馨提示</a></div>
			<div class="fl-right"><a id="operBtn">下一步</a></div>
		</div>
	</div>
</div>

<!--温馨提示弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog new-modal">
    <div class="modal-dow">
      <h4>温馨提示</h4>
      <div class="modal-text">
		  <c:choose>
			  <c:when test="${minCost eq '88'}">
				  <p> 1、保底时间24个月，承诺保底期内不降挡或取消保底，24个月后按30元/月宽带功能费；</p>
				  <p>2、魔百和标准资费为20元/月，从手机账户上扣取；</p>
				  <p> 3、保底套餐当月办理，当月生效，当月按天扣费并分摊，宽带和魔百和免办理当月月功能费，不计入整体优惠期；</p>
				  <p>4、手机号码停机，宽带、电视停机（宽带电视延迟3天停机，3天内手机复机，宽带电视不停机），手机号码复机，宽带、电视立即复机，手机销号或停机120天以上，同步取消宽带、电视和手机号码的绑定关系，终止宽带、电视。</p>
			  </c:when>
			  <c:otherwise>
				  <p>（1）办理号码承诺保底消费不低于48元/月，保底时间为12个月，承诺期内不允许降档、取消保底。</p>
				  <%--<p>（2）互联网高清电视第一年10元/月，从第13个月起按20元/月从手机账户上扣取。</p>--%>
				  <2>（3）老用户宽带到期前参与本活动时，宽带的余额次月自动转入捆绑手机号码的现金存折（不可清退），可用于抵扣手机套餐费用。套餐、宽带当月生效，办理当月按天收取保底费用，当月宽带月功能费、互联网电视月功能费，不计入整体优惠期。</2>
			  </c:otherwise>
		  </c:choose>
      </div>
      <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
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
				showAlert("请选择消费叠加型套餐档次");
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
			    	showAlert("请选择地址！");
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
		    showAlert("提交订单中,请稍等!");
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
    
    function getBusiInfo(goodsSkuId,bandWidth,price,productId,packageId,discntCode){
    	$("#goodsSkuId").val(goodsSkuId);
		$("#productId").val(productId);
		$("#packageId").val(packageId);
		$("#monthCost").val(discntCode);
		$("#price").val(price);
		$("#bandWidth").val(bandWidth);
		if($("#minCost").val()=='88') {
			$("#origPrice1").html("原价：" + discntCode + "元/月");
			$("#origPrice2").html("优惠24个月，第三年按" + discntCode + "元/月收取");
		}
	}
</script>

<!--搜索地址 end-->
<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
</body>
</html>