<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--插码相关--%>
	<meta name="WT.si_n" content="KD_MBHBL" />
	<meta name="WT.si_x" content="21" />
	<META name="WT.ac" content="">
	<Meta name="WT.mobile" content="">
	<Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<%--插码相关--%>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>
	<script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
	<div class="top container">
		<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
	   		<h1>魔百和</h1>
	  	</div>
	</div>
	<div class="container">
		<div class="wf-list sub-info sub-tc">
	    	<div class="wf-ait clearfix">
		        <div class="wf-tit">
			        <label>
						<span class="pull-left">宽带账号：${broadbandDetailInfo.accessAcct}</span>
					    <span class="pull-right"><input type="checkbox" class="mb" /></span>
					</label>
				</div>
		        <div class="wf-con">
		          	<p class="font-gray">套餐名称：<span class="font-3">${broadbandDetailInfo.discntName}</span></p>
		          	<p class="font-gray">
			            <span class="sus-tit">合约期限：</span>
			            <span class="sus-text font-3">${broadbandDetailInfo.endTime}</span>
		          	</p>
		          	<p class="font-gray">
		             	<span class="sus-tit">安装地址：</span>
		             	<span class="sus-text font-3">${broadbandDetailInfo.addrName}</span>
		          	</p>
		        </div>
	      	</div>
	      	<form method ="post" id="mbhForm" action="confirm">
				<input type="hidden" name="phoneId" value="${phoneId}"/>
				<input type="hidden" name="price" id="price"/>
				<input type="hidden" name="accessAcct" id="accessAcct" value="${broadbandDetailInfo.accessAcct}"/>
				<input type="hidden" name="goodsSkuId" id="goodsSkuId"/>
				<input type="hidden" name="tvProductId" id="tvProductId"/>
				<input type="hidden" name="reqPackageId" id="reqPackageId"/>
				<input type="hidden" name="reqElementId" id="reqElementId"/>
				<input type="hidden" name="giftElementId" id="chooseGiftElementId"/>
				<input type="hidden" name="term" id="term"/>
				<input type="hidden" name="custName" id="custName" value="${broadbandDetailInfo.custName}"/>
				<input type="hidden" name="psptId" id="psptId" value="${broadbandDetailInfo.psptId}"/>
				<input type="hidden" name="addrName" id="addrName" value="${broadbandDetailInfo.addrName}"/>
				<input type="hidden" name="discntName" id="discntName" value="${broadbandDetailInfo.discntName}"/>
				<input type="hidden" name="startTime" id="startTime" value="${broadbandDetailInfo.startTime}"/>
				<input type="hidden" name="endTime" id="endTime" value="${broadbandDetailInfo.endTime}"/>
				<input type="hidden" id="payMode" name="payMode">
				<input type="hidden" id="payModeName" name="payModeName">
	      	<!--选择套餐 start-->
	      	<div class="change-tc mb-change mb-list" style="display:none"> 
				<h2 class="renew-title">请选择魔百和套餐</h2>
				<ul class="add_oil select-renew clearfix">
					<c:forEach items="${mbhItemList}" var="mbhItem" varStatus="status" >
				  		<li onclick="getChooseMbhLevelName('${mbhItem.heProductId}','${mbhItem.price}','${mbhItem.term}')">${mbhItem.broadbandItemName}<s></s></li>
				  	</c:forEach>
				</ul>
				<div class="renew-content nofiexd">
					<ul class="clearfix">
						<li class="clearfix bg-grayf2">
							<p class="font-red">高清魔百和<span id="termSpan"></span></p>
							<div class="mb-text">
								<span class="">牌照方：</span>
							  	<div class="mb-rt"></div>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<!--选择套餐 end--> 
			<!--预约安装 start-->
			<div class="install-con mb-change yy-con" style="display:none">
				<h2 class="install-title">安装时间</h2>
				<ul class="change-list install-list">
					<li>
						<label>预约安装时间：</label>
						<div class="right-td">
							<div class="td-fr"><i class="select-arrow"></i>
								<select id="bookInstallDate" name="bookInstallDate" class="form-control">
									<option value="">---请选择---</option>
									<c:forEach items="${bookInstallDateList}" var="bookDate">
										<option value="${bookDate}">${bookDate}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</li>
					<li>
						<label>安装日期时段：</label>
						<div class="right-td">
							<div class="td-fr"><i class="select-arrow"></i>
								<select id="bookInstallTime" name="bookInstallTime" class="form-control">
									<option value="">---请选择---</option>
									<option value="1">上午</option>
									<option value="2">下午</option>
								</select>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<!--预约安装 end-->
			
			<div class="wf-list change-new sub-new">
				<div class="wf-ait clearfix">
					<ul class="bar-list">
						<li style="height:26px;line-height:26px;"> 
							<span class="font-gray">支付方式：</span>
							<div class="sub-text">
								<a href="javascript:choosePayMode('pay-btn1','0','在线支付')" class="bar-btn pay-btn pay-btn1 active">在线支付</a>
								<a href="javascript:choosePayMode('pay-btn2','Q','话费抵扣')" class="bar-btn pay-btn pay-btn2">话费抵扣</a>
							</div>
						</li>
					</ul>
				</div>
			</div>
			</form>		
	   	</div>
	</div>

	<div class="fix-br">
		<div class="affix foot-box new-foot">
		    <div class="container active-fix">
		    	<div class="fl-left"><a></a></div>
		      	<div class="fl-right"><a href="javascript:onSubmit();">下一步</a></div>
		      	<!--当用户不能点击时在a class加dg-gray--> 
		  	</div>
		</div>
	</div>
	
	<!--温馨提示弹框 start-->
	<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog new-modal">
	    <div class="modal-dow">
	      <h4>温馨提示</h4>	
	      <div class="modal-text">
	       	<p>1、魔百和年包使用到期后恢复为标准资费20元/月，直接从用户手机账户收取。用户也可选择退订电视服务，或者续订礼包产品。</p>
	        <p>2、续订魔百和电视将在次月生效。</p>
	        <p>3、对于新装、续费裸宽用户可办理魔百和电视业务，魔百和电视费用将于手机合并捆绑后付费。</p>
	        <p>4、若前期已办理魔百和电视且正处于按月收费状态，则办理魔百和年包后原魔百和计费规则取消，次月魔百和年包生效且同时变更为魔百和年包计费规则。</p>
	      </div>
	      <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
	    </div>
	 </div>
	</div>
	<!--温馨提示弹框 end-->
	<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function(){
		/*插码*/
		getGoodsId1();
		$(".mb").click(function(){
			$(".mb-change").toggle();
		});
	});
	
	function getChooseMbhLevelName(chooseGiftElementId,price,term){
		$("#chooseGiftElementId").val(chooseGiftElementId);
		$("#price").val(price);
		$("#termSpan").html(term);
		$.ajax({
			  url: ctx+"mbh/getTvProduct", 
			  data: {'chooseGiftElementId':chooseGiftElementId},
			  dataType: "json",
			  type : 'post',
			  success: function(e){
				  $("#term").val(e.abledChooseMbhItemList[0].term);
				  var tvProductHtml = '';
				  for(var i=0;i<e.abledChooseMbhItemList.length;i++){
					  tvProductHtml += '<label>';
					  if(e.abledChooseMbhItemList[i].broadbandItemName == "芒果TV"){
						  tvProductHtml += '<span class="pull-left" disabled="disabled">';
						  tvProductHtml += '<input type="radio" class="cbox" id="mg" name="form1_Mbh"  onclick="getChooseTvProduct(\''+e.abledChooseMbhItemList[i].goodsSkuId+'\',\''+e.abledChooseMbhItemList[i].productId+'\',\''+e.abledChooseMbhItemList[i].packageId+'\',\''+e.abledChooseMbhItemList[i].discntCode+'\',\''+e.abledChooseMbhItemList[i].discntCode+'\')" value="0"/>';
						  tvProductHtml += e.abledChooseMbhItemList[i].broadbandItemName
					  }else if(e.abledChooseMbhItemList[i].broadbandItemName == "未来电视"){
						  tvProductHtml += '<span class="pull-left" disabled="disabled">';
						  tvProductHtml += '<input type="radio" class="cbox" id="wl" name="form1_Mbh" disabled="disabled" onclick="getChooseTvProduct(\''+e.abledChooseMbhItemList[i].goodsSkuId+'\',\''+e.abledChooseMbhItemList[i].productId+'\',\''+e.abledChooseMbhItemList[i].packageId+'\',\''+e.abledChooseMbhItemList[i].discntCode+'\',\''+e.abledChooseMbhItemList[i].discntCode+'\')"  value="1"/>';
						  tvProductHtml += e.abledChooseMbhItemList[i].broadbandItemName+ "<br/>　（库存不足）";
					  }
					  
					  tvProductHtml += '</span>';
					  tvProductHtml += '<span class="font-gray">'+e.abledChooseMbhItemList[i].desc+'</span>';
					  tvProductHtml += '</label>';
				  }
				  $(".mb-rt").html(tvProductHtml);
			  },
			  error: function(e) {
				  showAlert("系统错误，请稍后重试！");
			  }
		});
	}
	
	function onSubmit(){
		/*插码*/
		try{
			var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
			if(window.Webtrends){
				Webtrends.multiTrack({
					argsa: ["WT.si_n",sin,
						"WT.si_x","22"],
					delayTime: 100
				})
			}else{
				if(typeof(dcsPageTrack)=="function"){
					dcsPageTrack ("WT.si_n",sin,true,
							"WT.si_x","22",true);
				}
			}
		}catch (e){
			console.log(e);
		}

		if($("#chooseGiftElementId").val() == ""){
			showAlert("请选择魔百和套餐！");
			return;
		}
		
		if($("#goodsSkuId").val() == ""){
			showAlert("请选择电视牌照方！");
			return;
		}
		
		$("#mbhForm").submit();
	}
	
	function getChooseTvProduct(goodsSkuId,productId,packageId,elementId){
		  $("#goodsSkuId").val(goodsSkuId);
		  $("#tvProductId").val(productId);
		  $("#reqPackageId").val(packageId);
		  $("#reqElementId").val(elementId);
	  }
	
	function showTips(){
		showAlert("1、魔百和年包使用到期后恢复为标准资费20元/月，直接从用户手机账户收取。用户也可选择退订电视服务，或者续订礼包产品。<br/>2、续订魔百和电视将在次月生效。<br/>3、对于新装、续费裸宽用户可办理魔百和电视业务，魔百和电视费用将于手机合并捆绑后付费。<br/>4、若前期已办理魔百和电视且正处于按月收费状态，则办理魔百和年包后原魔百和计费规则取消，次月魔百和年包生效且同时变更为魔百和年包计费规则。");
	}
	
	
	function choosePayMode(obj,payMode,payModeName){
    	$(".pay-btn").removeClass("active");
		$("." + obj).addClass("active");
    }
	</script>
</body>
</html>