//按照电话号码进行用户的信息
function telSearch(serialNumber){
    if(serialNumber == undefined){
        serialNumber = $("#telSearch").val();
    }else{
        $(".panel").toggle();
        $("#telSearch").val(serialNumber);
    }
    var obj = /^1[3|4|5|7|8]\d{9}$/;
    if(!obj.test(serialNumber)){
        alert("输入的手机号码不正确");
        return;
    }
    $.ajax({
       data:{searchString:serialNumber,searchType:"2"},
        type:"post",
        url:"telSearch",
        dataType:"json",
        success:function(result){
           if(result.code == '0'){
               if( result.data!=undefined){
                   $("#noInfo").hide();
                   $("#personalInfo").html(template("personal",result));
               }else{
                   $("#noInfo").show();
               }
           }else{
               showAlert(result.message);
           }
        }
    });
}
/*展示地址搜索框*/
$("#inputSearch").bind("click",function(){
    $(".panel").toggle();
    $("#queryAddress").focus();
    $("#telSearch").focus();
});
/*隐藏地址搜索框*/
$("#a_retSearch").click(function(){
    $("#telSearch").val("");
    $("#queryAddress").val("");
    $(".adress-list").html("");
    $("#personalInfo").html("");
    $(".panel").toggle();
});
function quickSearch(keyWords){
    $(".panel").toggle();
    $("#queryAddress").focus();
    $("#telSearch").focus();
    $("#queryAddress").val(keyWords);
    queryAddress(keyWords);
}
/*按键查询-最终地址查询*/
$("#queryAddress").bind("keyup",function(){
    var keyWords = $("#queryAddress").val();
    queryAddress(keyWords);
});

/*点了搜索icon后查询搜索*/
$("#sIcon").click(function(){
    var keyWords = $("#queryAddress").val();
    queryAddress(keyWords);
});

/*点了搜索后查询搜索*/
$("#searchBtn").click(function(){
    var keyWords = $("#queryAddress").val();
    queryAddress(keyWords);
});

/**
 * 清空搜索记录,清空商机推荐输入的搜索记录
 */
$("#clearRecord").click(function(){
    $.ajax({
        url:ctx+"businessPromotion/deleteSearch",
        success:function(data){
            $(".seach-records").html("");
        }
    })
});

$("#clearRecordSearch").click(function(){
    $.ajax({
        url:ctx+"businessPromotion/deleteRecordSearch",
        success:function(data){
            $(".seach-records").html("");
        }
    })
});

/*根据关键字查询电渠侧保存的小区名称*/
function queryAddress(keyWords){
    if (!$.trim(keyWords)) //空字符串
    {
        return;
    }
    var cityCode = $("#cityCode").val();
    $.ajax({
        url: 'qryAddressCommunityName',
        data:{keyWords:keyWords,cityCode:cityCode},
        dataType:"json",
        type:"post",
        success: function (e) {
            if (e.code == '0') //接口请求成功
            {
                if (e.data != undefined) {
                    $("#noInfo").hide();
                    $("#knowledgeInfo").show();
                    $(".adress-list").html("");
                    var ADDRESS_INFO = e.data;
                    $.each(ADDRESS_INFO, function (i, item) {
                        $(".adress-list").append(" <li>  <img src='"+ctxStatic+"/images/broadBand/kdtu.jpg'/><a href='fetchCustomerInfo?communityId=" + item.communityId + "'><div class='collect-txt'>" +
                            "<p>" + item.addressPath + "</p><span class='channel-gray9 f12' address-path=" + item.addressPath + " " +
                            "address-community=" + item.communityName + " community-id='" + item.communityId + "'onclick=searchPotentialUser(this)>" +
                            "" + item.communityName + "</span></div></li>");
                    });
                } else {
                    $("#noInfo").show();
                    $("#knowledgeInfo").hide();
                }
            } else {
                alert("系统异常，请稍后再试");
            }
        }
    });
}
/*查询楼栋*/
/**
 * 查询楼栋
 * @param residence 小区名
 */
function searchPotentialUser(obj){
    var COMMUNITY_NAME = $(obj).attr("community-id");
}

function inputSearch(){
    queryAddress($("#queryAddress").val());
}