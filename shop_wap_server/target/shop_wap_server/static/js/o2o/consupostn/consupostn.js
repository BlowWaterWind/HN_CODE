$(function() {
	
//	/*选择城市*/
//	$("#memberRecipientCity").change(function() {
//		var pid = $("#memberRecipientCity").find("option:selected").attr("orgid");
//
//		$.ajax({
//			type : 'post',
//			url : changeUrl,
//			cache : false,
//			context : $(this),
//			dataType : 'json',
//			data : {
//				pId : pid
//			},
//			success : function(data) {
//				$('#memberRecipientCounty').html('');
//				if(data.flag=='Y'){
//					var  htt = "";
//					$.each(data.orgList,function(i,obj){
//						htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
//					});
//					$('#memberRecipientCounty').html(htt);
//
//				}else{
//					showAlert("您查询的区县不存在！");
//				}
//
//			},
//			error : function(XMLHttpRequest, textStatus, errorThrown) {
//				showAlert("您查询的区县不存在！");
//			}
//		});
//
//	});

    // $("#queryAddress").on("focus",function(){
    //     if($(this).val()=="关键字搜索参考：请输入小区或周边标志性建筑"){
    //         $(this).val("");
    //     }
    // });
	
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
	
	/*最终地址查询*/
	$("#queryAddress").bind("keyup",function(){
		var cityName = $("#memberRecipientCity").find("option:selected").text();
		var cityArea = $("#memberRecipientCounty").find("option:selected").text();
		var keyWords = $("#queryAddress").val();
		 if (!$.trim(keyWords)) //空字符串
		 {
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
					if (ADDRESS_INFO.length == 0 && ADDRESS_INFO[0].BUILDING_NAME == "") {
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

	});

	/*最终地址查询*/
	$("#a_search").click(function(){
		var cityName = $("#memberRecipientCity").find("option:selected").text();
		var cityArea = $("#memberRecipientCounty").find("option:selected").text();
		var keyWords = $("#queryAddress").val();
		if (!$.trim(keyWords)) //空字符串
		{
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
					if (ADDRESS_INFO.length == 0 && ADDRESS_INFO[0].BUILDING_NAME == "") {
						$(".adress-list").html("").append("该地址暂未开通宽带资源办理");
						return;
					}
					$.each(ADDRESS_INFO, function (i, item) {
                        $(".adress-list").append("<li><label><span coverType='" + item.COVER_TYPE + "' community-code='" + item.COMMUNITY_CODE + "' address-path='" + item.ADDRESS_PATH + "' class='adr-xz' onclick=searchBuilding2(this)><input type='radio' name='radio_address'></span><span coverType='" + item.COVER_TYPE + "' community-code='" + item.COMMUNITY_CODE + "' address-path='" + item.ADDRESS_PATH + "' class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME +  "</span></label></li>");
					});
					$('#queryCommit').attr("disabled","disabled");
					$('#queryCommit').attr("class","btn btn-gray");
				} else {
					showAlert("系统异常，请稍后再试");
				}
			}
		);

	});

	/*选择最终地址*/
	$("#queryCommit").click(function(){
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
	    	return;
	    }

        /*根据地址类型动态设置预约时间 */
        if(houseCode != null && houseCode != ''){
            generateBookInstallTime(houseCode);
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


	});
	
	$(".add-new li").click(function(){
		$(this).addClass("on");
		$(this).siblings("li").removeClass("on");
		$(".new-content").show();
		$("#operBtn").removeAttr("disabled").removeClass("dg-gray"); 
		
		var goodsSkuId = $(this).find("input[name='goodsSkuId']").val();
		$("#goodsSkuId").val(goodsSkuId);
		
		var chooseBandWidth = $(this).find("input[name='choose_bandwidth']").val();
		var price = parseFloat($(this).find("input[name='choose_price']").val());
		var productId = $(this).find("input[name='choose_productId']").val();
		var packageId = $(this).find("input[name='choose_packageId']").val();
		var discntCode = $(this).find("input[name='choose_discntCode']").val();
		
		var minCost = "";
		
		if(productId == "51080016")minCost = "48"; 
		else if(productId == "51080017")minCost = "48"; 
		else if(productId == "51080018")minCost = "48"; 
		
		var broadbandMonthCost = "";
		if(parseInt(minCost) < 138){
        	if(chooseBandWidth == "20"){
        		broadbandMonthCost = "10";
        	}else if(chooseBandWidth == "50"){
        		broadbandMonthCost = "20";
        	}else if(chooseBandWidth == "100"){
        		broadbandMonthCost = "30";
        	}
        }
		
		$("#priceVal").html(price);
		$("#bandWidthVal").html(chooseBandWidth);
		$("#minCostVal").html(minCost);
		$("#broadbandMonthCostVal").html(broadbandMonthCost);
		
		$("#price").val(price);
		$("#minCost").val(minCost);
		$("#bandwidth").val(chooseBandWidth);
		$("#broadbandMonthCost").val(broadbandMonthCost);
		
		$("#productId").val(productId);
		$("#packageId").val(packageId);
		$("#discntCode").val(discntCode);
	});
	
	
	/*立即安装*/
	$("#a_submitInstallOrder").bind("click",function(){
		if(!$('#isRead').is(':checked')){
			showAlert("请阅读并同意《宽带入网协议》!");
			return;
		}
		
		$(".share-box").slideDown('fast');
    	$(".more-box").addClass("on");
	});
	
	/*支付*/
	$("a[name='a_pay']").bind("click",function(){
		if($("#payForm_orderSubNo").val()==""){
			showAlert("提交订单中,请稍等!");
			return ;
		}
		$("#payForm").submit();
	
	});
});

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
				if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].BUILDING_NAME == "") {
					//$(".adress-list").append("该地址暂未开通宽带办理");
                    showAlert("该地址暂未开通宽带资源办理");
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
					//$(".adress-list").append("该地址暂未开通宽带资源办理");
                    showAlert("该地址暂未开通宽带资源办理");
					return;
				}
				$.each(ADDRESS_INFO, function (i, item) {
					//console.log(item);
					$(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-xz' onclick=chooseLastAddress2(this)><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-text' onclick=chooseLastAddress2(this)>" + item.ADDRESS_NAME +  "</span></label></li>");
				});
			} else {
                showAlert("系统异常，请稍后再试");
			}
		}
	);

}
function chooseLastAddress2(obj){
	$('#queryCommit').removeAttr("disabled");
	$('#queryCommit').attr("class","btn btn-blue");
}

/**
 * 选择城市
 */
$(function() {
    var pid = $("#memberRecipientCity").find("option").attr("orgid");
	var eparchyCode_V = $("#memberRecipientCity").val();
    if(eparchyCode_V){
        $.ajax({
            type : 'post',
            url : countyUrl,
            cache : false,
            context : $(this),
            dataType : 'json',
            data : {
				cityCode : eparchyCode_V
            },
            success : function(data) {
                $('#memberRecipientCounty').html('');
                $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
                if(data.flag=='Y'){
                    var  htt = "";
                    $.each(data.orgList,function(i,obj){
                        //htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
						htt+="<option value='"+obj.CITY_ID+"'>"+obj.CITY_NAME+"</option>";
                    });
                    $('#memberRecipientCounty').html(htt);

                }else{
                    showAlert("您查询的区县不存在！");
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("您查询的区县不存在！");
            }
        });
    }
})

function onSubmit(){
	if($("#address").val() == ""){
		showAlert("请输入详细地址！");
		return;
	}
	
	if($("#installName").val() == ""){
		showAlert("请输入安装人姓名！");
		return;
	}
	
	if($("#idCard").val() == ""){
		showAlert("请输入安装人身份证号码！");
		return;
	}
	
	var checkIdCardMsg = checkIdcard($("#form1_IdCard").val());
	if(checkIdCardMsg !="验证通过!"){
		showAlert(checkIdCardMsg);
		return ;
	}
	
	showLoadPop("订单生成中，请稍后...");
	
	$("#form2").submit();
}

window.onload = function () {
	document.getElementById('queryAddress').focus();
}

//验证身份证函数
function checkIdcard(idcard){
	idcard = idcard.toString();
	var Errors=new Array("验证通过!","身份证号码位数不对!","身份证号码出生日期超出范围或含有非法字符!","身份证号码校验错误!","身份证地区非法!");
	//var Errors=new Array(true,false,false,false,false);
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
	var idcard,Y,JYM;
	var S,M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	//地区检验
	if(area[parseInt(idcard.substr(0,2))]==null) return Errors[4];
	//身份号码位数及格式检验
	switch(idcard.length){
		case 15:
			return Errors[0];
			break;
		case 18:
			//18 位身份号码检测
			//出生日期的合法性检查
			//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
			//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
			//if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0 )) {
			//    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
			//} else {
			//    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
			//}
			//if (ereg.test(idcard)) {//测试出生日期的合法性
			//计算校验位
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
				+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
				+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
				+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
				+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
				+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
				+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
				+ parseInt(idcard_array[7]) * 1
				+ parseInt(idcard_array[8]) * 6
				+ parseInt(idcard_array[9]) * 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1);//判断校验位
			if (M == idcard_array[17]) return Errors[0]; //检测ID的校验位
			else return Errors[3];
			//}
			//else return Errors[2];
			break;
		default:
			return Errors[1];
			break;
	}
}

/**
 * 根据小区地址编码动态生成预约时间
 * @param obj
 */
function generateBookInstallTime(communityCode) {
    var url = ctx + "bandResourceQuery/queryAddressAttr";
    var data = {"addressCode": communityCode};
    $.getJSON(url, data, function (e) {
        // 防止重复添加option
        //$("#bookInstallDate").empty();
        $("#bookInstallTime").empty();
        //$("#bookInstallDate").append("<option value=''>---请选择---</option>");
        $("#bookInstallTime").append("<option value=''>---请选择---</option>");
        
        if (e.respCode != 0) {//调用接口失败！
            alert("系统异常！请稍后再试！");
        } else {
            //给form1_communityType赋值
            $("#form1_communityType").val(e.result[0].COMMUNITY_TYPE);

            // 农村预约装机时间在72小时后，城市预约装机时间在48小时后； 01 为农村编码
            // var offTime = (e.COMMUNITY_TYPE == "01" ? (3 * 24 * 60 * 60 * 1000) : (2 * 24 * 60 * 60 * 1000));
            //if(e.result[0].COMMUNITY_TYPE == "01"){
            //    offTime = 3 * 24 * 60 * 60 * 1000;
            //}else if(e.result[0].COMMUNITY_TYPE == "02" || e.result[0].COMMUNITY_TYPE == "03" || e.result[0].COMMUNITY_TYPE =="04"){
            //    offTime = 2 * 24 * 60 * 60 * 1000;
            //}else{
            //    alert("根据该地址查询地址信息异常!");
            //    return ;
            //}
            //获取当前时间
            //var currentTime = new Date();
            ////能预约的最早时间
            //var bookStartTime = new Date();

            // 初始化预约时间
            initBookTime();

            //动态添加预约装机日期
            //var dateOptHtml = "";
            //var bookStartTimeMS = bookStartTime.getTime();
            //for (var i = 1; i <= 4; i++) {
            //    dateOptHtml = "<option value='" + bookStartTime.toLocaleDateString() + "'>" + bookStartTime.toLocaleDateString() + "</option>";
            //    bookStartTimeMS += 1 * 24 * 60 * 60 * 1000;
            //    bookStartTime = new Date(bookStartTimeMS);
            //    $("#bookInstallDate").append(dateOptHtml);
            //}


        }
    });

    // 当预约日期改变时动态改变时间
    //$("#bookInstallDate").change(function () {
    //    $("#bookInstallTime").empty();
    //    var bookStartTime = $("#bookInstallDate").find("option:eq('1')").val();
    //    if ($("#bookInstallDate").val() == bookStartTime) {
    //        var currentTime = new Date();
    //        var currentTime = currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
    //        $("#bookInstallTime").append("<option value=''>---请选择---</option>");
    //        initBookTime(new Date(bookStartTime + " " + currentTime));
    //    } else {
    //        $("#bookInstallTime").append("<option value=''>---请选择---</option>");
    //        var bookTimePeriod = ["上午08:00-12:00", "中午12:00-14:00", "下午14:00-18:00", "晚上18:00-20:00"];
    //        for (var i = 0; i < bookTimePeriod.length; i++) {
    //            $("#bookInstallTime").append("<option value='" + bookTimePeriod[i] + "'>" + bookTimePeriod[i] + "</option>");
    //        }
    //    }
    //});
}


//初始化预约时间
function initBookTime() {
    var bookTimePeriod = ["上午08:00-12:00", "中午12:00-14:00", "下午14:00-18:00", "晚上18:00-20:00"];
    //var bookTimePoint = [new Date(bookStartTime.toLocaleDateString() + " 12:00"),
    //    new Date(bookStartTime.toLocaleDateString() + " 14:00"),
    //    new Date(bookStartTime.toLocaleDateString() + " 18:00"),
    //    new Date(bookStartTime.toLocaleDateString() + " 20:00")];

    //var timePeriodCount;
    //if (bookStartTime < bookTimePoint[0]) {
    //    timePeriodCount = 4;
    //} else if (bookStartTime < bookTimePoint[1]) {
    //    timePeriodCount = 3;
    //} else if (bookStartTime < bookTimePoint[2]) {
    //    timePeriodCount = 2;
    //} else if (bookStartTime < bookTimePoint[3]) {
    //    timePeriodCount = 1;
    //} else {
    //    timePeriodCount = 0;
    //}
    for (var i = 0; i < bookTimePeriod.length; i++) {
        $("#bookInstallTime").append("<option value='" + bookTimePeriod[i] + "'>" + bookTimePeriod[i] + "</option>")
    }
}