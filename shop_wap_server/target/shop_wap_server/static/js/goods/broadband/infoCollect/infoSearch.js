/*展示地址搜索框*/
$("#inputSearch").bind("click",function(){
    $(".panel").toggle();
    $("#txt").focus();
});
/*隐藏地址搜索框*/
$("#a_retSearch").click(function(){
    $("#txt").val("");
    $(".adress-list").html("");
    updateRecords("4");
    $(".panel").toggle();
});
function quickSearch(keyWords){
    var keys = keyWords.split(";");
    $(".panel").toggle();
    $("#txt").focus();
    $("#txt").val(keys[2]);
    queryAddress(keys[2],keys[0],keys[1]);
}
/*按键查询-最终地址查询*/
$("#queryAddress").bind("keyup",function() {
    var keyWords = $("#queryAddress").val();
    queryAddress(keyWords);
});
/*点了搜索icon后查询搜索*/
$("#sIcon").click(function(){
    var keyWords = $("#queryAddress").val();
    queryAddress(keyWords);
});

/**
 * 清空搜索记录,清空商机推荐输入的搜索记录
 */
$("#clearRecord").click(function(){
    $.ajax({
        url:"deleteRecord",
        success:function(data){
            $(".seach-records").html("");
        }
    })
});

/*要看商机推荐支持什么地址范围的查询*/

function collectInfo(thisDiv){
    $("#queryAddress").val("");
    var eparchy = $("#city").val();
    var cityName = $("#city").find("option:selected").text();
    var areaName = $(".city-area").find("option:selected").text();
    var addressName = $(thisDiv).find("input[name='communityName']").val();
    $(thisDiv).find("input[name='eparchy']").val(eparchy);
    $(thisDiv).find("input[name='searchString']").val(cityName+";"+areaName+";"+addressName);
    $(thisDiv).find("form").submit();
}
/*返回时更新最高记录和历史记录*/
function updateRecords(type) {
    $.ajax({
        url: "updateRecord",
        data:{type:type},
        type:"get",
        dataType:"json",
        success:function(data){
            if(data.resultCode == "0"){
                $(".hot-list").html(template("hotList",data));
                $("#record").html(template("historyList",data));

            }
        }
    });
}

