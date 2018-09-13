/*按键查询-查询营销知识信息*/
$("#queryKeyword").bind("keyup", function () {
    var keyWords = $("#queryKeyword").val();
    inputSearch(keyWords);
});
function setSearchCondition(type, condition) {
    $.ajax({
        url: "search",
        data: {type: type, condition: condition},
        dataType: "json",
        success: function (result) {
            if (result.code == '0') {
                var resultHtml = "";
                var resultData = result.data;
                if (resultData != undefined) {
                    for (var i = 0; i < result.data.length; i++) {
                        resultHtml += "<li><a onclick='fetchDetail(this)'><form action='businessRule' method='post'><input type='hidden' name='businessId'value='" + result.data[i].businessId + "'><input type='hidden' name='businessName' value='"+result.data[i].businessName+"'></form></form><img src='" + logoUrl(result.data[i].logoUrl)+ "'/><h4>" +
                            result.data[i].businessName + "</h4><p class='order-txt'>" + result.data[i].businessDesc + "</p><p class='channel-blue pt10'>" +
                            result.data[i].businessName + "元/月</p></form></a></li>";
                    }
                    $("#clickSearch").html("");
                    $("#clickSearch").html(resultHtml);
                    $("#noInfoClick").hide();
                } else {
                    $("#clickSearch").html("");
                    $("#noInfoClick").show();
                }
            } else {
                showAlert(result.message || "系统在忙哦，请稍后再试！");
                //返回结果有误
                $("#noInfoClick").show();
                $("#afterSearch").hide();
            }
        }
    });
}
function inputSearch(keyword) {
    if (!$.trim(keyword)) //空字符串
    {
        return;
    }
    var searchkey = keyword;
    if (searchkey == undefined) {
        searchkey = $("#queryKeyword").val();
    } else {
        $("#queryKeyword").val(searchkey);
        $("#queryKeyword").focus();
    }
    $.ajax({
        url: "keyWordSearch",
        data: {keyWords: searchkey},
        dataType: "json",
        type: "post",
        success: function (result) {
            if (result.code == '0') {
                var resultHtml = "";
                var resultData = result.data;
                if (resultData != undefined) {
                    for (var i = 0; i < result.data.length; i++) {
                        resultHtml += "<li><a onclick='fetchDetail(this)'><form action='businessRule' method='post'><input type='hidden' name='businessId'value='" + result.data[i].businessId + "'><input type='hidden' name='businessName' value='"+result.data[i].businessName+"'></form></form><img src='" + logoUrl(result.data[i].logoUrl)+ "'/><h4>" +
                            result.data[i].businessName + "</h4><p class='order-txt'>" + result.data[i].businessDesc + "</p><p class='channel-blue pt10'>" +
                            result.data[i].businessName + "元/月</p></form></a></li>";
                    }
                    $("#knowledgeInfo").html("");
                    $("#knowledgeInfo").html(resultHtml);
                    $("#knowledgeInfo").show();
                    $("#afterSearch").hide();
                    $("#noInfoClick").hide();//搜索完之后隐藏热门搜索和历史搜索
                    $("#noInfoSearch").hide();
                } else {
                    $("#knowledgeInfo").html("");
                    $("#afterSearch").hide();
                    $("#noInfoSearch").show();
                }
            } else {
                showAlert(result.message || "系统在忙哦，请稍后再试！");
                //返回结果有误
                $("#noInfoClick").show();
                $("#afterSearch").hide();
            }
        }
    });
}

$("#a_retSearch").on("click", function () {
    $("#queryKeyword").val("");
    $("#container").toggle();
    $("#div_search").toggle();
});

$("#search").on("click", function () {
    {
        $("#noInfoSearch").hide();
        //查询热门搜索和本用户的搜索记录
        $.ajax({
            url: "searchRecord",
            data: {},//如何获取用户信息？
            type: "post",
            dataType: "json",
            success: function (data) {
                if (data.resultCode == 0) {
                    var topList = data.topList;
                    var hisList = data.historyList;
                    var topHtml = "";
                    var hisHtml = "";
                    for (var i = 0; i < topList.length; i++) {
                        topHtml += "<a onclick=inputSearch('" + topList[i].searchString + "')>" + topList[i].searchString + "</a>";
                    }
                    for (var j = 0; j < hisList.length; j++) {
                        hisHtml += "<li><a onclick=inputSearch('" + hisList[j].searchString + "')>" + hisList[j].searchString + "</a></li>";
                    }
                    $("#hotRecordList").html(topHtml);
                    $("#hisRecordUl").html(hisHtml);
                }
            }
        });
        $("#afterSearch").show();
        $("#knowledgeInfo").html("");
        $("#queryKeyword").focus();
        $("#knowledgeInfo").hide();
        $("#container").toggle();
        $("#div_search").toggle();
    }
});

$("#queryKeyword").on("click", function () {
    $("#queryKeyword").val("");
    $("#knowledgeInfo").hide();
    $("#afterSearch").show();
    $("#noInfoSearch").hide();
});

/**
 * 清空搜索记录,清空商机推荐输入的搜索记录
 */
$("#clearRecord").click(function () {
    $.ajax({
        url: "deleteRecord",
        success: function () {
            $(".seach-records").html("");
        }
    })
});

function logoUrl(logoUrl) {
    if (logoUrl) {
        return '/res/img/' + logoUrl;
    } else {
        return ctxStatic + '/images/broadBand/bus09.png';
    }
}

function inputQuery() {
    var keyWord = $("#queryKeyword").val();
    inputSearch(keyWord);
}

function fetchDetail(thisDiv){
    $(thisDiv).find("form").submit();
}
