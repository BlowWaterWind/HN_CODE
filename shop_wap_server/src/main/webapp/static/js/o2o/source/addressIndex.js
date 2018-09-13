$(function(){
    document.onkeydown=function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if(e && e.keyCode==13){ // enter 键
            alert("111");
            //要做的事情
        }
    };

    /*展示地址搜索框*/
    $("#address").bind("click",function(){
        $(".panel").toggle();
        $("#queryAddress").focus();
    });
    /*隐藏地址搜索框*/
    $("#a_retSearch").click(function(){
        $("#queryAddress").val("");
//        $(".adress-list").html("");
        $(".panel").toggle();
    });

    /*最终地址查询*/
    $("#queryAddress").bind("keyup",function(){
        var keyWords = $("#queryAddress").val();
        queryAddress(keyWords);

    });
    /*搜索*/
    $("#searchBtn").click(function(){
        var keyWords = $("#queryAddress").val();
        queryAddress(keyWords);
    });
    /**
     * 清空搜索记录
     */
    $("#clearRecord").click(function(){
        $.ajax({
            url:ctx+"o2oBandSource/deleteRecord",
            data:{
                searchType:"1"
            },
            success:function(data){
                $(".seach-records").html("");
            }
        })
    })

});
function quickSearch(keyWords){
    $(".panel").toggle();
    $("#queryAddress").val(keyWords);
    $("#queryAddress").focus();
    queryAddress(keyWords);
}
/*地址查询*/
function queryAddress(keyWords){
    var cityName = $("#city").val();
    var cityArea = $("#county").val();

    if (!$.trim(keyWords)) //空字符串
    {
        return;
    }
    if(keyWords==''){
         keyWords = $("#queryAddress").val();
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
                    $(".adress-list").append("<li> <div class='no-orders' ><img src='"+ctxStatic+"/images/fa_icon.png' /><p>未找到到相关内容</p></div></li>");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append(" <li><a href='javascript:void(0)'><div class='collect-txt' address-path="+item.ADDRESS_PATH+ " community_code=" +item.COMMUNITY_CODE+" address-community="+item.COMMUNITY_NAME+" onclick=searchBuilding2(this)><p>"+item.COMMUNITY_NAME+"</p><span class='channel-gray9 f12' >"+item.ADDRESS_PATH +"</span></div></li>")
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
    var COMMUNITY_NAME = $(obj).attr("address-community");
    var community_code=$(obj).attr("community_code");
    var eparchyCode = $("#cityCode").val();
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
                    $(".adress-list").append("<li> <div class='no-orders' ><img src='"+ctxStatic+"/images/fa_icon.png' /><p>未找到到相关内容</p></div></li>");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append("<li><label><a href='javascript:void(0)'><div class='collect-txt' coverType=" + item.COVER_TYPE  + " buildingCode="+item.BUILDING_CODE+" community_code="+community_code + " address-path-building=" + item.ADDRESS_PATH + " address-community="+COMMUNITY_NAME+" onclick=chooseBuilding2(this)>" +
                        "<span class='channel-gray9 f12' >"+item.BUILDING_NAME +"</span></div></li>" );

                });
                /*保存搜索记录*/
                $.ajax({
                    url:ctx+'o2oBandSource/saveSearch',
                    type:"POST",
                    data:{
                        searchType:"1",
                        searchString:COMMUNITY_NAME
                    },
                    success:function(data){
                        console.log("搜索记录保存成功！");
                    }
                });
            }
        }
    );

}

/**
 * 选择楼栋，查询单元
 * @param obj
 * @returns
 */
function chooseBuilding2(obj) {
    var ADDRESS_PATH = $(obj).attr("address-path-building");
    var eparchyCode = $("#cityCode").val();
    var communityCode = $(obj).attr("community_code");
    var buildingCode = $(obj).attr("buildingCode");
    var COMMUNITY_NAME = $(obj).attr("address-community");
    $.getJSON(ctx + 'bandResourceQuery/qryAddressUnit', {
            'eparchyCode': eparchyCode,
            'buildingCode': buildingCode,
            'comunityCode': communityCode
        }, function (e) {
            if (e.respDesc == "OK!") //接口请求成功
            {
                $(".adress-list").html("");
                var ADDRESS_INFO = e.result[0].ADDRESS_UNIT_INFO;
                if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].UNIT_NAME == "") {
                                        //$(".adress-list").append("该地址暂未开通宽带资源办理");
                    $(".adress-list").append("<li> <div class='no-orders' ><img src='"+ctxStatic+"/images/fa_icon.png' /><p>未找到到相关内容</p></div></li>");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append("<li><label><a href='javascript:void(0)'>" +
                        "<div class='collect-txt' coverType=" + item.COVER_TYPE + " unitCode=" + item.UNIT_CODE  + " community_code="+communityCode+ " address-path="+item.ADDRESS_NAME+" address-community="+COMMUNITY_NAME+" onclick=chooseUnit(this)>" +
                        "<span class='channel-gray9 f12' >"+item.UNIT_NAME +"</span></div></li>" );
                });
            } else {
                showAlert("系统异常，请稍后再试");
            }
        }
    );

}
/**
 * 选择单元,查询楼层
 * @param obj
 * @returns
 */
function chooseUnit(obj) {
    var eparchyCode = $("#cityCode").val();
    var communityCode = $(obj).attr("community_code");
    var unitCode = $(obj).attr("unitCode");
    var COMMUNITY_NAME = $(obj).attr("address-community");
//        var unitCode = '-109585671';
    $.getJSON(ctx + 'bandResourceQuery/qryAddressFloor', {
            'eparchyCode': eparchyCode,
            'unitCode': unitCode,
            'comunityCode': communityCode
        }, function (e) {
            if (e.respDesc == "OK!") //接口请求成功
            {
                $(".adress-list").html("");
                var ADDRESS_INFO = e.result[0].ADDRESS_FLOOR_INFO;
                if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].FLOOR_NAME == "") {
                    //$(".adress-list").append("该地址暂未开通宽带资源办理");
                    $(".adress-list").append("<li> <div class='no-orders' ><img src='"+ctxStatic+"/images/fa_icon.png' /><p>未找到到相关内容</p></div></li>");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append("<li><label><a href='javascript:void(0)'><div class='collect-txt' coverType=" + item.COVER_TYPE+" floorCode=" + item.FLOOR_CODE  +  " community_code="+communityCode + " address-path-building=" + item.ADDRESS_PATH + " address-community="+COMMUNITY_NAME+" onclick=chooseFloor(this)>" +
                        "<span class='channel-gray9 f12' >"+item.FLOOR_NAME +"</span></div></li>" );

                });
            } else {
                showAlert("系统异常，请稍后再试");
            }
        }
    );

}

/**
 * 选择楼层,查询房号
 * @param obj
 * @returns
 */
function chooseFloor(obj) {
    var eparchyCode = $("#cityCode").val();
    var communityCode = $(obj).attr("community_code");
    var floorCode = $(obj).attr("floorCode");
    var COMMUNITY_NAME = $(obj).attr("address-community");
//        var floorCode = '-109585672';
    $.getJSON(ctx + 'bandResourceQuery/qryAddressHouseCode', {
            'eparchyCode': eparchyCode,
            'floorCode': floorCode,
            'comunityCode': communityCode
        }, function (e) {
            if (e.respDesc == "OK!") //接口请求成功
            {
                $(".adress-list").html("");
                var ADDRESS_INFO = e.result[0].ADDRESS_HOUSE_INFO;
                if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].HOUSE_NAME == "") {
                    $(".adress-list").append("<li> <div class='no-orders' ><img src='"+ctxStatic+"/images/fa_icon.png' /><p>未找到到相关内容</p></div></li>");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append("<li><label><a href='javascript:void(0)'>" +
                        "<div class='collect-txt' coverType=" + item.COVER_TYPE + " address-path-building=" + item.HOUSE_CODE + " address-path="+item.ADDRESS_NAME+" address-community="+COMMUNITY_NAME+" onclick=chooseLastAddress2(this)>" +
                        "<span class='channel-gray9 f12' >"+item.ADDRESS_NAME +"</span></div></li>" );

                });
            } else {
                showAlert("系统异常，请稍后再试");
            }
        }
    );

}
///**
// * 选择楼栋
// * @param obj
// * @returns
// */
//function chooseBuilding2(obj){
//    var ADDRESS_PATH = $(obj).attr("address-path-building");
//    var COMMUNITY_NAME = $(obj).attr("address-community");
//    var communityCode = $(obj).attr("community_code");
//    $.getJSON(ctx+'bandResourceQuery/qryAddressName', {
//            'addressKeyString':  encodeURI(ADDRESS_PATH,"UTF-8")
//        }, function (e) {
//            if (e.respDesc == "OK!") //接口请求成功
//            {
//                $(".adress-list").html("");
//                var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
//                if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].BUILDING_NAME == "") {
//                    //$(".adress-list").append("该地址暂未开通宽带资源办理");
//                    $(".adress-list").append("<li> <div class='no-orders' ><img src='"+ctxStatic+"/images/fa_icon.png' /><p>未找到到相关内容</p></div></li>");
//                    return;
//                }
//                $.each(ADDRESS_INFO, function (i, item) {
//                    $(".adress-list").append("<li><label><a href='javascript:void(0)'>" +
//                        "<div class='collect-txt' coverType=" + item.COVER_TYPE + " address-path-building=" + item.HOUSE_CODE + " address-path="+item.ADDRESS_NAME+" address-community="+COMMUNITY_NAME+" onclick=chooseLastAddress2(this)>" +
//                        "<span class='channel-gray9 f12' >"+item.ADDRESS_NAME +"</span></div></li>" );
//                });
//            } else {
//                showAlert("系统异常，请稍后再试");
//            }
//        }
//    );
//
//}

function chooseLastAddress2(obj){
    var ADDRESS_PATH = $(obj).attr("address-path");
    var COMMUNITY_NAME = $(obj).attr("address-community");
    var cityName = encodeURI(encodeURI($("#city").val(),"UTF-8"));
    var countyName = encodeURI(encodeURI($("#county").val(),"UTF-8"));
    window.location.href=ctx+"o2oBandSource/queryPackage?addressPath="+encodeURI(encodeURI(ADDRESS_PATH,"UTF-8"))+"&communityName="+encodeURI(encodeURI(COMMUNITY_NAME,"UTF-8"))+"&city="+cityName+"&county="+countyName;
    //$('#queryCommit').removeAttr("disabled");
    //$('#queryCommit').attr("class","btn btn-blue");
}
