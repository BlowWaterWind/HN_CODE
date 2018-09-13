$(function() {
	initAddrDiv();//初始化地址选择栏
	initAddrSearchDiv();//初始化搜索地址页面
	
	$("#queryAddress").on("focus",function(){
        if($(this).val()=="关键字搜索参考：请输入小区或周边标志性建筑"){
            $(this).val("");
        }
    });
	
	$("#queryAddress").bind("blur",function(){
		$(this).keyup();
	});
	
	$("#memberRecipientCounty").bind("change",function(){
        $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
        $("#queryAddress").val("");
        $(".adress-list").html("");
        $("#form1_houseCode").val("");
        $('#queryCommit').attr("disabled","disabled");
        $('#queryCommit').attr("class","btn btn-gray");
    });
});

function initAddrDiv(){
	$("#broadband_addr_div").html("");
	
	$.getJSON(ctx+'consupostn/getAreaInfo', {}, function (e) 
	{
		var cityList = e.cityList;	
		var popHtml = '';
		popHtml += '<ul class="change-list ym-zit ym-list">';
		popHtml += '<li>';
		popHtml += '<label class="font-gray"><em class="font-red">*</em>装机地址：</label>';
		popHtml += '<div class="right-td">';
		popHtml += '<span class="td-fr">';
		popHtml += '<i class="select-arrow"></i>';
		popHtml += '<select class="set-sd" id="memberRecipientCity" name="memberRecipientCity">';
		
		var cityOrgId = '';
		
		for(var i=0;i<cityList.length;i++){
			if(cityList[i].eparchyCode == $("#eparchy_Code").val()){
				cityOrgId = cityList[i].eparchyCode;
				popHtml += '<option value="'+cityList[i].eparchyCode+'" orgid="'+cityList[i].orgId+'">'+cityList[i].orgName+'</option>';
				break;
			}
		}
		
		popHtml += '</select>';
		popHtml += '</span>';
		popHtml += '<span class="td-fr"> <i class="select-arrow"></i>';
		popHtml += '<select class="set-sd" id="memberRecipientCounty" name="memberRecipientCounty" onchange="memberRecipientCounty_onChange()">';
		
		$.ajax({
			 url: ctx+"broadband/getCountyFromBoss",
			 data: {cityCode : cityOrgId},
			  dataType: "json",
			  type : 'post',
			  success: function(data){
				 $('#memberRecipientCounty').html('');
				 if(data.flag=='Y'){
					 $.each(data.orgList,function(i,obj){
						 popHtml+="<option value='"+obj.CITY_ID+"'>"+obj.CITY_NAME+"</option>";
	                 });
					 
					 popHtml += '';
					 popHtml += '</select>';
					 popHtml += '</span>';
					
					 popHtml += '<div class="zit-box">';
					 popHtml += '<input type="hidden" id="houseCode" name="houseCode"  />';
					 popHtml += '<input type="text" id="address" name="address" class="form-control flip" placeholder="详细街道、小区、门牌号" />';
					 popHtml += '</div></div>';
					 popHtml += '</li></ul>';
					
					 $("#broadband_addr_div").html(popHtml);
					 $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
					 $(".flip").click(function(){$(".panel").toggle();});
				 }
				 else {
					 showAlert("无区县信息!");
				 }
			  },
			  error: function(e) {
				  showAlert("您查询的区县不存在！");
			  }
		});
	});
}

function initAddrSearchDiv(){
	var addrSearchHtml = '';
	addrSearchHtml += '<div id="div_searchAddress" class="site-search panel" style="display:none">';
	addrSearchHtml += '    <div class="top container">';
	addrSearchHtml += '        <div class="header site-full sub-title"><a class="icon-left site-close" id="a_retSearch"></a>';
	addrSearchHtml += '            <div class="top-search list-search fit-ipnut">';
	addrSearchHtml += '                <input type="text" id="queryAddress" name="queryAddress" onkeyup="queryLastAddress()" class="form-control form-fr" placeholder="关键字搜索参考：请输入小区名或周边标志性建筑" autofocus="autofocus" />';
	addrSearchHtml += '                <a href="javascript:;" class="icon-search"  id="a_search" ></a></div>';
	addrSearchHtml += '        </div>';
	addrSearchHtml += '    </div>';
	addrSearchHtml += '    <div class="mzy-con hide" id="div_message">';
	addrSearchHtml += '        <img src="${ctxStatic}/images/error.png" />';
	addrSearchHtml += '        <div class="mzy-text">';
	addrSearchHtml += '            <p>尊敬的用户，非常抱歉！</p>';
	addrSearchHtml += '            <p>您所在区域宽带资源还在建设中，</p>';
	addrSearchHtml += '            <p>有宽带资源后，我们会第一时间通过短信告知您！</p>';
	addrSearchHtml += '        </div>';
	addrSearchHtml += '    </div>';
	addrSearchHtml += '    <ul class="container adress-list"></ul>';
	addrSearchHtml += '    <div class="fix-br container fix-top fix-fb">';
	addrSearchHtml += '        <div class="affix container foot-menu">';
	addrSearchHtml += '            <div class="container form-group tj-btn"> <a  href="##" id="queryCommit" onclick="chooseDetailAddr()" class="btn btn-gray" disabled="disabled">确定</a> <a id="queryCancel" class="btn btn-gray site-close">取消</a> </div>';
	addrSearchHtml += '        </div>';
	addrSearchHtml += '    </div>';
	addrSearchHtml += '</div>';
	$("body").append(addrSearchHtml);
	$(".site-close").click(function(){$(".panel").toggle();});
}

/*最终地址查询*/
function queryLastAddress(){
	var cityName = $("#memberRecipientCity").find("option:selected").text();
	var cityArea = $("#memberRecipientCounty").find("option:selected").text();
	var keyWords = $("#queryAddress").val();
	if (!$.trim(keyWords)){ //空字符串
		return;
	}
	$.getJSON(ctx+'bandResourceQuery/qryAddressCommunityName', {
			'cityName': encodeURI(cityName,"UTF-8"),
			'cityArea': encodeURI(cityArea,"UTF-8"),
			'keyWords': encodeURI(keyWords,"UTF-8")
	}, function (e) {
			if (e.respDesc == "OK!") //接口请求成功
			{
				$(".adress-list").html("");
				var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
				if (ADDRESS_INFO.length == 0) {
					$(".adress-list").append("<li>该地址暂无宽带办理信息</li>");
					return;
				}
				$.each(ADDRESS_INFO, function (i, item) {
					$(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-xz' onclick=searchBuilding2(this)><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME +  "</span></label></li>");
				});
				if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","单宽带或者和家庭",1, "WT.si_x","选择地址",1);}
				$('#queryCommit').attr("disabled","disabled");
				$('#queryCommit').attr("class","btn btn-gray");
			} else {
				showAlert("系统异常，请稍后再试");
			}
		}
	);
}

/**
 * 查询楼栋
 * @param residence 小区名
 */
function searchBuilding2(obj){
	$(obj).addClass('cur').siblings().removeClass('cur');
	var residence = $(obj).attr("address-path");
	$("#residential").val(residence);

	$.getJSON(ctx+'bandResourceQuery/qryAddressBuildingName', {
			'communityAddressName': encodeURI(residence,"UTF-8")
		}, function (e) {
			if (e.respDesc == "OK!") //接口请求成功
			{
				$(".adress-list").html("");
				var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
				if (ADDRESS_INFO.length == 0) {
					$(".adress-list").append("该地址暂未开通宽带办理");
					return;
				}
				$.each(ADDRESS_INFO, function (i, item) {
					$(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-xz' onclick=chooseBuilding2(this)><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-text'  onclick=chooseBuilding2(this)>" + item.BUILDING_NAME +  "</span></label></li>");
				});
			}
		}
	);
}

/**
 * 选择楼栋
 * @param obj
 * @returns
 */
function chooseBuilding2(obj){
	var ADDRESS_PATH = $(obj).attr("address-path-building");
	$.getJSON(ctx+'bandResourceQuery/qryAddressName', {
			'addressKeyString':  encodeURI(ADDRESS_PATH,"UTF-8")
		}, function (e) {
			if (e.respDesc == "OK!") //接口请求成功
			{
				$(".adress-list").html("");
				var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
				if (ADDRESS_INFO.length == 0) {
					$(".adress-list").append("该地址暂未开通宽带资源办理");
					return;
				}
				$.each(ADDRESS_INFO, function (i, item) {
					//console.log(item);
					$(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-xz' onclick=chooseLastAddress2(this)><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-text' onclick=chooseLastAddress2(this)>" + item.ADDRESS_NAME +  "</span></label></li>");
				});
			} else {
				alert("系统异常，请稍后再试");
			}
		}
	);
}

function chooseLastAddress2(obj){
	$('#queryCommit').removeAttr("disabled");
	$('#queryCommit').attr("class","btn btn-blue");
}

/* 选择最终详细地址 */
function chooseDetailAddr(){
	var address = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").text();
	var houseCode = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("last-address-building");
	var coverType = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("coverType");
	
	if(coverType == "学校FTTB" || coverType == "学校LAN" || coverType == "合作宽带" || coverType == "农村无线宽带"
    	|| coverType == "铁通FTTH" || coverType == "铁通FTTB" || coverType == "铁通学校FTTH" || coverType == "铁通ADSL"
    		|| coverType == "铁通学校FTTH" || coverType == "铁通学校FTTB" || coverType == "铁通学校LAN" || coverType == "铁通合作宽带"
    			|| coverType == "校园AP宽带"){
		$("#form1_eparchyCode").val($("#memberRecipientCity").val());
		$("#form1_addressName").val(address);
		$("#form1_houseCode").val(houseCode);
		$("#form1_coverType").val(coverType);
		$("#address").val(address);
		$("#houseCode").val(houseCode);
		$(".panel").toggle();
		$.getJSON(ctx+'consupostn/queryAddress', {
			addrId:houseCode
		},function(data){
			$("#communityType").val(data.COMMUNITY_TYPE);
			$.each(data.bookInstallDateList,function(index,val){
				$("#bookInstallDate").append("<option value='"+val+"'>"+val+"</option>");
			});
		});
    	return;
    }
	
	showLoadPop("正在查询资源...");
	//检查能装多少M的宽带
	$.getJSON(ctx+'bandResourceQuery/coverInfo', {
			'addressCode': houseCode,
			'coverType': encodeURI(coverType,"UTF-8")
		}, function (e) {
			if (e.result)  //接口请求成功
			{
				var freePort = e.result[0].FREE_PORT_NUM;
				var maxWidth = e.result[0].MAX_WIDTH;
				if (freePort && maxWidth && freePort > 0 && maxWidth > 0) {  //可以办理宽带
					$("#form1_maxWidth").val(maxWidth);
					$("#form1_freePort").val(freePort);
					$("#form1_eparchyCode").val($("#memberRecipientCity").val());
					$("#form1_addressName").val(address);
					$("#form1_houseCode").val(houseCode);
					$("#form1_coverType").val(coverType);
					$("#address").val(address);
					$("#houseCode").val(houseCode);
					var countyName = $("#memberRecipientCounty").find("option:selected").text();
					$("#install_county").val(countyName);
					$(".panel").toggle();
					$.getJSON(ctx+'consupostn/queryAddress', {
						addrId:houseCode
					},function(data){
						$("#communityType").val(data.COMMUNITY_TYPE);
						$.each(data.bookInstallDateList,function(index,val){
							$("#bookInstallDate").append("<option value='"+val+"'>"+val+"</option>");
						});
					});
				}
				else {
					showAlert("选择的地址暂无宽带资源,请重新选择!");
				}
			} else {
				alert("系统异常，请稍后再试");
			}
			hideLoadPop();
		}
	);
}

function memberRecipientCounty_onChange(){
	$("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
    $("#queryAddress").val("");
    $(".adress-list").html("");
    $("#form1_houseCode").val("");
    $('#queryCommit').attr("disabled","disabled");
    $('#queryCommit').attr("class","btn btn-gray");
}