<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-addr-ims.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<form action="imsNewDetail" id="form1" name="form1" method="post">
	<input type="hidden" id="eparchyCode" name="eparchyCode"  value="${eparchyCode}" />

	<input type="hidden" name="goodsSkuId" id="goodsSkuId" />
	<input type="hidden" id="monthCost" name="monthCost"  />
    <input type="hidden" id="bandWidth" name="bandWidth"  />
    <input type="hidden" id="price" name="price"  />
    <input type="hidden" id="productId" name="productId"  />
    <input type="hidden" id="packageId" name="packageId"  />
    
    <input type="hidden" id="chooseKdInfo" name="chooseKdInfo"  />
    <input type="hidden" id="chooseImsInfo" name="chooseImsInfo"  />
    
    <input type="hidden" id="imsProductId" name="imsProductId"  />
    <input type="hidden" id="imsPackageId" name="imsPackageId"  />
    <input type="hidden" id="imsOptDiscnt" name="imsOptDiscnt"  />
    <input type="hidden" id="imsOptDiscntEnddate" name="imsOptDiscntEnddate"  />
    <input type="hidden" id="imsEparchyCode" name="imsEparchyCode"  />
	
	
	<input type="hidden" id="form1_maxWidth" name="form1_maxWidth"  />
    <input type="hidden" id="form1_freePort" name="form1_freePort"  />
    <input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode"  />
    <input type="hidden" id="form1_coverType" name="form1_coverType" />
	<input type="hidden" id="install_county" name="install_county" />
	<input type="hidden" id="form1_communityType" name="form1_communityType" />
	
	<input type="hidden" id="tvType" name="tvType" value="1"/>
	<input type="hidden" id="tvboxSaleType" name="tvboxSaleType" value="1"/>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>选择套餐</h1>
    </div>
</div>
<div class="container">
    <div class="fixed-package broadband-con">
        <!--宽带套餐选择 start-->
        <div class="broadband-list">
            <div class="broadband-title"><span>宽带套餐选择</span></div>
            <div class="broadband-info tabs">
                <ul class="broadband-menu clearfix">
                    <li class="active"><a href="#tab-1">组网送宽带</a></li>
                    <li><a href="#tab-2">和家庭</a></li>
                    <li><a href="#tab-3">消费叠加型</a></li>
                </ul>
                <div id="tab-1">
                    <div class="broadband-content">
                        <ul class="broadband-veiw change clearfix">
                            <c:forEach items="${familyItemList}" var="consupostnItem" varStatus="status" >
						  		<li onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}','${consupostnItem.broadbandItemName}');">
						  		${consupostnItem.price}元/月
						  		<span>${consupostnItem.bandWidth}M宽带0元享</span></li>
						  	</c:forEach>
                        </ul>
                    </div>
                </div>
                <div id="tab-2">
                    <div class="broadband-content">
                        <ul class="broadband-veiw change clearfix">
                            <c:forEach items="${heBroadbandItemList}" var="broadbandItem" varStatus="status">
                            	<li onclick="getBroadbandInfo('${broadbandItem.bandWidth}','${broadbandItem.discntCode}','${broadbandItem.price}','${broadbandItem.productId}','${broadbandItem.packageId}','${broadbandItem.goodsSkuId}','${broadbandItem.broadbandItemName}');">
	                                ${broadbandItem.price}元/月
	                                <span>${broadbandItem.bandWidth}M宽带0元享</span>
	                            </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
                <div id="tab-3">
                    <div class="broadband-content">
                        <ul class="broadband-veiw change clearfix">
                            <c:forEach items="${consupostnItemList}" var="consupostnItem" varStatus="status" >
						  		<li onclick="getBroadbandInfo('${consupostnItem.bandWidth}','${consupostnItem.discntCode}','${consupostnItem.price}','${consupostnItem.productId}','${consupostnItem.packageId}','${consupostnItem.goodsSkuId}','${consupostnItem.broadbandItemName}');">
						  		保底${consupostnItem.price}元/月
						  		<span>${consupostnItem.bandWidth}M宽带0元享</span></li>
						  	</c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <!--宽带套餐选择 end-->
        <!--固话套餐选择 start-->
        <div class="broadband-list">
            <div class="broadband-title"><span>固话套餐选择</span></div>
            <div class="broadband-info tabs" id="imsComboDiv">
                
            </div>
        </div>
        <!--固话套餐选择 end-->
        <!--电视牌照方 start-->
        <div class="broadband-list">
            <div class="broadband-title"><span>牌照方</span></div>
            <div class="broadband-info tabs">
                <ul class="broadband-menu broadband-menu02 clearfix">
                    <li onclick="chooseTv('0')" class="active"><a href="#tab-1">芒果TV</a></li>
                    <li onclick="chooseTv('1')"><a href="#tab-2">未来TV</a></li>
                </ul>
                <div id="tab-1">
                    <div class="broadband-content">
                        <div class="broadband-txt">
                            <p class="t-c">湖南卫视优质点播资源，芒果自制节目及其他热门电影，电视剧，综艺等...</p>
                        </div>
                    </div>
                </div>
                <div id="tab-2">
                    <div class="broadband-content">
                    		<p class="t-c">央视连续剧、少儿动漫、极光专区、超级回看中的电影...</p></div>
                </div>
            </div>
        </div>
        <!--电视牌照方 end-->
        <!--安装地址选择 start-->
        <div id="broadband_addr_div" class="broadband-list"></div>
        <!--安装地址选择 end-->
        <!--套餐内容 start-->
        <div class="broadband-list tc-content">
            <div class="broadband-title"><span>套餐内容</span></div>
            <ul class="tcxq-list">
                <li>
                    <p>宽带资费</p>
                    <p id="kd_name_p">-</p>
                    <p id="kd_fee_p" class="font-red">xx元</p>
                </li>
                <li>
                    <p>固话资费</p>
                    <p id="ims_name_p">-</p>
                    <p id="ims_fee_p" class="font-red">xx元</p>
                </li>
                <li>
                    <p>互联网电视</p>
                    <p id="tv_name_p">芒果TV</p>
                    <p class="font-red">20元/月</p>
                </li>
            </ul>
        </div>
        <!--套餐内容 end-->
    </div>
</div>
<!--尾部 start-->
<div class="fix-br new-fixed">
    <div class="affix foot-box">
        <div class="container active-fix">
            <div class="fl-left">
                <p class="ft-total">合计：<span id="sum" class="font-gray6">20</span>元/月</p>
            </div>
            <div class="fl-right"><a id="submitBtn" class="dg-gray" href="javascript:onNextStep()">下一步</a></div>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
</form>
<!--尾部 end-->
<script>
var kdFee,imsFee; 
$(function() {
    //tabs
    $('.tabs').tabslet();
    //切换
    $(".change li").on("click", function() {
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
    });
    
    $(".broadband-veiw:first li:first").click();
});

function getBroadbandInfo(bandWidth,discntCode,price,productId,packageId,goodsSkuId,broadbandItemName){
	  $(".broadband-veiw li").each(function(){
		  $(this).removeClass("active");
	  });
	
	  $("#monthCost").val(price);
	  $("#bandWidth").val(bandWidth);
	  $("#price").val(price);
	  $("#goodsSkuId").val(goodsSkuId);
	  $("#productId").val(productId);
	  $("#packageId").val(packageId);
	  
	  $("#kd_name_p").html(broadbandItemName);
	  $("#chooseKdInfo").val(broadbandItemName);
	  $("#kd_fee_p").html(price+"元/月");
	  kdFee = price;
	  setSum();
	  $("#submitBtn").removeClass("dg-gray");
	  $.ajax({
		  url: ctx+"ims/getImsInfo", 
		  data: {product_id:productId},
		  dataType: "json",
		  type : 'post',
		  success: function(e){
			  	if(e.respCode == "0"){
			  		$("#imsComboDiv").html("");
			  		var comboItem = '<ul class="broadband-menu clearfix" id="imsComboUl">';
			  		var comboInfo = '';
					for(var i=1;i<=e.imsList.length;i++){
						if(i == 1){
							comboItem += '<li class="active" onclick="chooseIms(\'tab-'+i+'\',\''+e.imsList[i-1].imsItemName+'\',\''+e.imsList[i-1].imsItemFee+'\',\''+e.imsList[i-1].imsItemProductId+'\',\''+e.imsList[i-1].imsItemPackageId+'\',\''+e.imsList[i-1].elementId+'\',\''+e.imsList[i-1].expireDate+'\',\''+e.imsList[i-1].imsEparchyCode+'\')"><a href="#tab-'+i+'">'+e.imsList[i-1].imsItemName+'<br/>'+e.imsList[i-1].imsItemFee+'元/月</a></li>';
							comboInfo += '<div class="content-div" id="tab-'+i+'">';
						}else{
							comboItem += '<li onclick="chooseIms(\'tab-'+i+'\',\''+e.imsList[i-1].imsItemName+'\',\''+e.imsList[i-1].imsItemFee+'\',\''+e.imsList[i-1].imsItemProductId+'\',\''+e.imsList[i-1].imsItemPackageId+'\',\''+e.imsList[i-1].elementId+'\',\''+e.imsList[i-1].expireDate+'\',\''+e.imsList[i-1].imsEparchyCode+'\')"><a href="#tab-'+i+'">'+e.imsList[i-1].imsItemName+'<br/>'+e.imsList[i-1].imsItemFee+'元/月</a></li>';
							comboInfo += '<div class="content-div hide" id="tab-'+i+'">';
						}
							comboInfo += '<div class="broadband-content">';
								comboInfo += '<div class="broadband-txt">';
									comboInfo += e.imsList[i-1].explain;
									comboInfo += '<p>优惠内容：'+e.imsList[i-1].elementName+'</p>';
								comboInfo += '</div>';
							comboInfo += '</div>';
						comboInfo += '</div>';
					}
					comboItem += '</ul>';
					$("#imsComboDiv").html(comboItem);
					$("#imsComboDiv").append(comboInfo);
					$('#imsComboDiv').tabslet();
					$("#imsComboUl li").on("click", function() {
				        $(this).siblings().removeClass("active");
				        $(this).addClass("active");
				    });
					$("#imsComboUl li:first").click();
	  			}else{
	  				
	  			}
		  },
		  error: function(e) {
			  showAlert("固话套餐查询出错！");
		  }
	  });
  }
  
  function chooseIms(divId,imsItemName,imsItemFee,imsItemProductId,imsItemPackageId,elementId,expireDate,imsEparchyCode){
	  $(".content-div").each(function(){
  		  $(this).addClass("hide");
  	  });
	  $("#imsComboDiv " + "#" + divId).removeClass("hide");
	  
	  $("#ims_name_p").html(imsItemName + "套餐");
	  $("#ims_fee_p").html(imsItemFee + "元/月");
	  $("#chooseImsInfo").val(imsItemFee + "元" + imsItemName + "套餐");
	  
	  $("#imsProductId").val(imsItemProductId);
	  $("#imsPackageId").val(imsItemPackageId);
	  $("#imsOptDiscnt").val(elementId);
	  $("#imsOptDiscntEnddate").val(expireDate);
	  $("#imsEparchyCode").val(imsEparchyCode);
	  imsFee = imsItemFee;
	  setSum();
  }
  
  function chooseTv(tvType){
	  if(tvType == "1"){
		  $("#tv_name_p").html("芒果TV");
	  }else{
		  $("#tv_name_p").html("未来TV");
	  }
	  
	  $("#tvType").val(tvType)
  }
  
  function setSum(){
	  var sumFee = parseInt(kdFee) + parseInt(imsFee) + 20;
	  $("#sum").html(sumFee);
  }
  
  function onNextStep(){
	  $("#form1").submit();
  }
</script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>
</body>
</html>