var ua = navigator.userAgent.toLowerCase();
if(ua.match(/leadeon/i)=="leadeon"){
    //走手厅
    document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc_leadeon.js"><\/script>');
}else{
    document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/sdc9.js"><\/script>');
    document.write('<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"><\/script>');
}
$(function() {
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
    /*选择城市*/
   /* $("#memberRecipientCity").click(function() {
        var pid = $("#memberRecipientCity").find("option:selected").attr("orgid");

        $.ajax({
            type : 'post',
            url : changeUrl,
            cache : false,
            context : $(this),
            dataType : 'json',
            data : {
                pId : pid
            },
            success : function(data) {
                $('#memberRecipientCounty').html('');
                $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
                if(data.flag=='Y'){
                    var  htt = "";
                    $.each(data.orgList,function(i,obj){
                        htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
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

    });*/

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
    $("#memberRecipientCity").bind("change",function(){
        $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
        $("#queryAddress").val("");
        $(".adress-list").html("");
        $("#form1_houseCode").val("");
        $('#queryCommit').attr("disabled","disabled");
        $('#queryCommit').attr("class","btn btn-gray");
    });

    /*展示地址搜索框*/
    $("#address").bind("click",function(){
        $(".panel").toggle();
    });

    /*隐藏地址搜索框*/
    $("#a_retSearch").click(function(){
        $(".panel").toggle();
    });

    /*隐藏地址搜索框*/
    $("#queryCancel").click(function(){
        $(".panel").toggle();
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
                    if (ADDRESS_INFO.length == 0) {
                        $(".adress-list").append("<li>该地址暂无宽带办理信息</li>");
                        return;
                    }
                    $.each(ADDRESS_INFO, function (i, item) {
                        $(".adress-list").append("<li><label><span  coverType='" + item.COVER_TYPE + "' community-code='" + item.COMMUNITY_CODE + "' address-path='" + item.ADDRESS_PATH + "' onclick=searchBuilding2(this) class='adr-xz'><input type='radio' name='radio_address'></span><span coverType='" + item.COVER_TYPE + "' community-code='" + item.COMMUNITY_CODE + "' address-path='" + item.ADDRESS_PATH + "' class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME +  "</span></label></li>");
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
                    if (ADDRESS_INFO.length == 0) {
                        $(".adress-list").html("").append("该地址暂未开通宽带资源办理");
                        return;
                    }
                    $.each(ADDRESS_INFO, function (i, item) {
                        $(".adress-list").append("<li><label><span class='adr-xz'><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME +  "</span></label></li>");
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


    /*选择新装宽带套餐*/
    $("li[name='li_broadbandItem']").bind("click",function(){

        var skuId = $(this).find("input[name='skuId']").val();
        var chooseBandWidth = $(this).find("input[name='chooseBandWidth']").val();
        var price = parseFloat($(this).find("input[name='price']").val());
        var productId = $(this).find("input[name='productId']").val();
        var discntCode = $(this).find("input[name='discntCode']").val();
        var packageId = $(this).find("input[name='packageId']").val();
        var desc1=$(this).find("input[name='desc']").val();
        var maxWidth = $("#form1_maxWidth").val();
        var freePort = $("#form1_freePort").val();
        $("#form1_skuId").val(skuId);
        $("#form1_chooseBandWidth").val(chooseBandWidth);
        $("#submit_productId").val(productId);
        $("#submit_packageId").val(packageId);
        $("#submit_discntCode").val(discntCode);
        var isChooseCat=$("#form1_chooseCat").is(':checked') ;
        $("#span_total").text(price);
        $("#desc1").text(desc1+"<br>(办理后24个月内限制转出)");
       // $("#div_settlement").attr("class","container active-fix");\
        if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","和家庭",1, "WT.si_x","选择产品档次"+chooseBandWidth+"M "+$(this).attr("price")+"元/月",1);}
    });

    $("#form1_chooseCat").bind("click",function(){
        var priceTotal = parseFloat($("#span_total").text());
        $("#span_total").text(priceTotal.toFixed(2));
    });

    /**
     *  确认购买
     */
    $("#a_confirmInstall").bind("click",function(){
    	if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","和家庭宽带",0,"WT.si_x","和家庭宽带办理",0)}
        var countyName = $("#memberRecipientCounty").find("option:selected").text();
        $("#form1_county").val(countyName);
        var isBroadBand = $("input[name='form1_hasBroadband']").val();
        var isMbh = $("input[name='form1_hasMbh']").val();
        if(isBroadBand == '1'){
            if($("#form1_houseCode").val()==""){
                showAlert("请选择安装地址!");
                return ;
            }
            //在没有宽带的情况下光猫必须有
            $("#form1_chooseCat").val("1");
        }
        //在没有魔百和的情况下，机顶盒必须有；
        if(isMbh == '1'){
            $("#form1_Mbh").val($("input[name='boxType']:checked").val()||"0");
        }
        if($("#form1_skuId").val()==""){
            showAlert("请选择宽带产品!");
            return ;
        }
        if(parseInt($("#form1_chooseBandWidth").val())>parseInt($("#form1_maxWidth").val())){
            showAlert("选择的地址暂无"+$("#form1_chooseBandWidth").val()+"此宽带资源,请重新选择!!");
            return ;
        }
        showLoadPop();
        $("#form1").submit();
    });


    /**
     *  确认地址
     */
    $("#a_confirmHeInstall").bind("click",function(){
        $("#form1").submit();
    });

});

/**
 * 查询楼栋
 * @param residence 小区名
 */
function searchBuilding2(obj){

    //选择小区后将小区地址编码赋值给表单中的form1_communityId
    var communityId = $(obj).attr("community-code");
    $("#form1_communityId").val(communityId);

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
                if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].BUILDING_NAME === "") {
                    //$(".adress-list").append("该地址暂未开通宽带办理");
                    showAlert("该地址暂未开通宽带资源办理");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append("<li><label>" +
                        "<span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-xz' onclick=chooseBuilding2(this)><input type='radio' name='radio_address' onclick=chooseBuilding2(this)></span>" +
                        "<span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-text'  onclick=chooseBuilding2(this)>" + item.BUILDING_NAME +  "</span>" +
                        "</label></li>");
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
                if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].BUILDING_NAME == "") {
                    //$(".adress-list").append("该地址暂未开通宽带资源办理");
                    showAlert("该地址暂未开通宽带资源办理");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    //console.log(item);
                    $(".adress-list").append("<li><label>" +
                        "<span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-xz' onclick=chooseLastAddress2(this)><input type='radio' name='radio_address' ></span>" +
                        "<span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-text' onclick=chooseLastAddress2(this)>" + item.ADDRESS_NAME +  "</span>" +
                        "</label></li>");
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

    if(pid){
        $.ajax({
            type : 'post',
            url : changeUrl,
            cache : false,
            context : $(this),
            dataType : 'json',
            data : {
                pId : pid
            },
            success : function(data) {
                $('#memberRecipientCounty').html('');
                $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
                if(data.flag=='Y'){
                    var  htt = "";
                    $.each(data.orgList,function(i,obj){
                        htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
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

/**
 *和家庭办理
 */
function broadbandReservationTrade(obj) {
        window.location.href=ctx + "BroadbandTrade/heBroadbandInstall";
}
window.onload = function () {
    document.getElementById('queryAddress').focus();
}






