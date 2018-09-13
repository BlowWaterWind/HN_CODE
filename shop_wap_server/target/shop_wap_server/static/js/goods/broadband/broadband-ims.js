$(function() {
    //$(".broadband-ims").addClass("hide");
    $("#imsListUl li").click(function(){
		$("#ims_sn").html($(this).html());
		$(".close").click();
	});
    init();
});

function init(){
	var imsHtml = '';
	imsHtml += '<div class="wf-list change-new sub-new">';
		imsHtml += '<div class="wf-ait clearfix">';
			imsHtml += '<a href="javascript:void(0)" class="install-btn" id="open">+加装固话</a>';
			imsHtml += '<ul id="imsComboUl" class="bar-list install-content txt" style="display:none;">';		
				imsHtml += '<a href="javascript:void(0)" class="install-btn" id="close">-取消固话</a>';
			imsHtml += '</ul>';
		imsHtml += '</div>';
	imsHtml += '</div>';
	
	$(".broadband-ims").html(imsHtml);
	
	getImsCombo();
	initImsChooseDialog();
}

function getImsCombo(){
	$.ajax({
	  url: ctx+"ims/getImsInfo", 
	  data: {product_id:$("#productId").val()},
	  dataType: "json",
	  type : 'post',
	  success: function(e){
		  	var comboInfo = '';
		  	if(e.respCode == "0"){
		  		comboInfo = '';
		  		comboInfo += '<li> <span class="font-gray">固话套餐：</span>';
		  		comboInfo += '<div class="sub-text sub-broad">';
				for(var i=1;i<=e.imsList.length;i++){
					if(i == 1){
						comboInfo += '<a href="javascript:chooseIms(\''+e.imsList[i-1].imsItemName+'\',\''+e.imsList[i-1].imsItemFee+'\',\''+e.imsList[i-1].imsItemProductId+'\',\''+e.imsList[i-1].imsItemPackageId+'\',\''+e.imsList[i-1].elementId+'\',\''+e.imsList[i-1].expireDate+'\',\''+e.imsList[i-1].imsEparchyCode+'\',\''+e.imsList[i-1].explain+'\')" class="bar-btn active">'+e.imsList[i-1].imsItemFee+'元/月</a>';
					}else{
						comboInfo += '<a href="javascript:chooseIms(\''+e.imsList[i-1].imsItemName+'\',\''+e.imsList[i-1].imsItemFee+'\',\''+e.imsList[i-1].imsItemProductId+'\',\''+e.imsList[i-1].imsItemPackageId+'\',\''+e.imsList[i-1].elementId+'\',\''+e.imsList[i-1].expireDate+'\',\''+e.imsList[i-1].imsEparchyCode+'\',\''+e.imsList[i-1].explain+'\')" class="bar-btn">'+e.imsList[i-1].imsItemFee+'元/月</a>';
					}
				}
				comboInfo += '</div>';
				comboInfo += '<div id="imsIntroDiv" class="tib-sm">';
				
				comboInfo += '</div>';
				comboInfo += '</li>';
				
				comboInfo += '<li> <span class="font-gray">优惠价：</span>';
					comboInfo += '<div class="sub-text">1元/月<strong class="f12 font-red"> 首次办理，0元/月，优惠期3个月，第一年1元/月</strong></div>';
				comboInfo += '</li>';
				
				comboInfo += '<li> <span class="font-gray">号码归属：</span>';
					comboInfo += '<div class="sub-text">';
						comboInfo += '<div class="td-fr">';
							comboInfo += '<i class="select-arrow"></i>';
							comboInfo += '<select class="form-control">';
								comboInfo += '<option value="长沙市">长沙市</option>';
							comboInfo += '</select>';
						comboInfo += '</div>';
					comboInfo += '</div>';
				comboInfo += '</li>';
				
				comboInfo += '<li>';
					comboInfo += '<div class="entrance-date">';
						comboInfo += '<label class="font-gray">固话号码：</label>';
						comboInfo += '<span id="ims_sn" class="arrow" data-toggle="modal" data-target="#myModal"></span>';
					comboInfo += '</div>';
				comboInfo += '</li>';
				$("#imsComboDiv").append(comboInfo);
  			}
	  },
	  error: function(e) {
		showAlert("固话套餐查询出错！");
	  }
	});
}

function chooseIms(imsItemName,imsItemFee,imsItemProductId,imsItemPackageId,elementId,expireDate,imsEparchyCode,explain){
//	  $("#imsProductId").val(imsItemProductId);
//	  $("#imsPackageId").val(imsItemPackageId);
//	  $("#imsOptDiscnt").val(elementId);
//	  $("#imsOptDiscntEnddate").val(expireDate);
//	  $("#imsEparchyCode").val(imsEparchyCode);
//	  $("#imsIntroDiv").html(explain);
}

function initImsChooseDialog(){
	var imsChooseDia = '';
	imsChooseDia += '<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">';
	imsChooseDia += '<div class="modal-dialog broadband-dialog">';
	imsChooseDia += '    <div class="modal-con">';
	imsChooseDia += '        <div class="modal-header">';
	imsChooseDia += '            <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>';
	imsChooseDia += '            <h4 class="modal-title">固话号码</h4>';
	imsChooseDia += '        </div>';
	imsChooseDia += '        <div class="fixed-modal">';
	imsChooseDia += '            <div class="fixed-search">';
	imsChooseDia += '                <input id="searchImsNum" type="text" class="form-control" placeholder="生日、幸运数字等" />';
	imsChooseDia += '                <a class="search-icon" href="javascript:searchImsNum()"></a>';
	imsChooseDia += '            </div>';
	imsChooseDia += '            <ul id="imsListUl" class="fixed-list">';
	imsChooseDia += '                <li>073188123412</li>';
	imsChooseDia += '                <li>073188123412</li>';
	imsChooseDia += '            </ul>';
	imsChooseDia += '            <!--查找无记录 start-->';
	imsChooseDia += '            <div class="fixed-records" style="display:none;">';
	imsChooseDia += '                <p>很抱歉</p>';
	imsChooseDia += '                <p>暂无符合要求号码</p>';
	imsChooseDia += '            </div>';
	imsChooseDia += '            <!--查找无记录 end-->';
	imsChooseDia += '            <div class="fixed-change"><a href="javascript:void(0)">换一批</a></div>';
	imsChooseDia += '        </div>';
	imsChooseDia += '    </div>';
	imsChooseDia += '</div>';
	imsChooseDia += '</div>';
	$("body").append(agreementHtml);
}

function searchImsNum(){
	$.ajax({
		  url: ctx+"ims/searchImsNum", 
		  data: {searchImsSn:$("#searchImsNum").val(),eparchyCode:$("#imsEparchyCode").val()},
		  dataType: "json",
		  type : 'post',
		  success: function(e){
			  	if(e.respCode == "0"){
			  		
	  			}else{
	  				$(".fixed-records").removeClass("hide");
	  			}
		  },
		  error: function(e) {
			  $(".fixed-records").removeClass("hide");
		  }
	  });
}