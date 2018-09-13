$(function () {
    addressQuery.qryCityArea(addressQuery.cityCode); //默认初始化加载

    $("#city").live('change', function () {  //市级别选择事件监听
        $("#txt").val("");
        $(".adress-list").html("");
        addressQuery.cityCode = $(this).val();
        addressQuery.cityName = $(this).find("option:selected").text();
        addressQuery.qryCityArea(addressQuery.cityCode);
        addressQuery.cityArea = $('.city-area').find("option:selected").text();
    });

    $(".city-area").live('change', function () {  //区域级别选择事件监听
        $("#txt").val("");
        $(".adress-list").html("");
        addressQuery.cityArea = $(this).val();
        console.log(addressQuery.cityCode);
    });

    $("#txt").live('input propertychange', function () {  //关键字级别选择事件监听
        addressQuery.keyWord = $(this).val();
        addressQuery.cityArea = $(".city-area").val();
        $(".adress-list").html(""); //清空无效信息
        addressQuery.qryLastAddressByKeyWords(addressQuery.cityName, addressQuery.cityArea, addressQuery.keyWord);//关键字查询
        console.log(addressQuery.keyWord);
    });

});


function collectInfo(communityCode,communityName,addressPath){
    $("#communityId").val(communityCode);
    $("#communityName").val(communityName);
    $("#addressPath").val(addressPath);
    var cityName = $("#city").find("option:selected").text();
    var areaName = $(".city-area").find("option:selected").text();
//        var cityCode=$("#city").find("option:selected").val();
    var countyCode=$(".city-area").find("option:selected").val();
    $("#eparchyName").val(cityName);
    if(communityName!=null){
        $("#searchString").val(cityName+";"+areaName+";"+communityName);
    }
    $("#form1").submit();
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

function inputSearch(){
    $(".panel").toggle();
    $("#txt").focus();
}
/*隐藏地址搜索框*/
$("#a_retSearch").click(function(){
    $("#txt").val("");
    $(".adress-list").html("");
    updateRecords($("#type").val());
    $(".panel").toggle();
});

function quickSearch(keyWords){
    var keys = keyWords.split(";");
    $(".panel").toggle();
    $("#txt").focus();
    $("#txt").val(keys[2]);
    $(".adress-list").html(""); //清空无效信息
    $("#city option").each(function (){
        if($.trim($(this).text())==$.trim(keys[0])){
            $(this).attr('selected',true);
        }
    });
    $.ajaxSettings.async = false;
    addressQuery.qryCityArea($("#city").val());
    $(".city-area option").each(function (){
        if($.trim($(this).text())==$.trim(keys[1])){
            $(this).attr('selected',true);
        }
    });
    addressQuery.qryLastAddressByKeyWords(keys[0],keys[1],keys[2]);//关键字查询
}

function queryBu(){
    var keyWords = $("#txt").val();
    addressQuery.cityArea =  $(".city-area").find("option:selected").text();
    addressQuery.cityName = $('#city').find("option:selected").text();
    $(".adress-list").html("");
    addressQuery.qryLastAddressByKeyWords(addressQuery.cityName, addressQuery.cityArea, keyWords);//关键字查询
}

/**
 * 清空搜索记录,清空商机推荐输入的搜索记录
 */
$("#clearRecord").click(function(){
    var type=$("#type").val();
    $.ajax({
        url:"deleteRecord",
        data:{type:type},
        success:function(data){
            $(".seach-records").html("");
        }
    })
});