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
    });
    /*隐藏地址搜索框*/
    $("#a_retSearch").click(function(){
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
                    $("#div_message").removeClass("hide");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    //$(".adress-list").append("<li><label><span  coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " onclick=searchBuilding2(this) class='adr-xz'><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " address-path=" + item.ADDRESS_PATH + " class='adr-text'  onclick=searchBuilding2(this)>" + item.COMMUNITY_NAME +  "</span></label></li>");
                    $(".adress-list").append(" <li><a href='javascript:void(0)'><div class='collect-txt'><p>"+item.COMMUNITY_NAME+"</p><span class='channel-gray9 f12' address-path="+item.ADDRESS_PATH+" address-community="+item.COMMUNITY_NAME+" onclick=searchBuilding2(this)>"+item.ADDRESS_PATH +"</span></div></li>")
                });
                if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","单宽带或者和家庭",1, "WT.si_x","选择地址",1);}
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
    $("#residential").val(residence);
    $.getJSON(ctx+'bandResourceQuery/qryAddressBuildingName', {
            'communityAddressName': encodeURI(residence,"UTF-8")
        }, function (e) {
            if (e.respDesc == "OK!") //接口请求成功
            {
                $(".adress-list").html("");
                var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                if (ADDRESS_INFO.length == 1 && ADDRESS_INFO[0].BUILDING_NAME === "") {
                    $("#div_message").removeClass("hide");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append("<li><label><a href='javascript:void(0)'><div class='collect-txt' coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " onclick=chooseBuilding2(this)>" +
                        "<span class='channel-gray9 f12' >"+item.BUILDING_NAME +"</span></div></li>" );

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
                    $("#div_message").removeClass("hide");
                    return;
                }
                $.each(ADDRESS_INFO, function (i, item) {
                    $(".adress-list").append("<li><label><a href='javascript:void(0)'><div class='collect-txt' coverType=" + item.COVER_TYPE + " address-path-building=" + item.HOUSE_CODE + " onclick=chooseLastAddress2(this)>" +
                        "<span class='channel-gray9 f12' >"+item.ADDRESS_NAME +"</span></div></li>" );
                });
            } else {
                showAlert("系统异常，请稍后再试");
            }
        }
    );

}

function chooseLastAddress2(obj){
    $("#returnMain").click();

    //$('#queryCommit').removeAttr("disabled");
    //$('#queryCommit').attr("class","btn btn-blue");

}
