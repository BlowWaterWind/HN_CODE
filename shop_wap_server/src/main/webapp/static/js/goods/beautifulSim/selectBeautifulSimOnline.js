$("#minPreFee").blur(function(){
    searchBeautifulNum();
});

$("#maxPreFee").blur(function(){
    searchBeautifulNum();
});


function searchBeautifulNum() {
    $("#phonePool").empty();
    var loading = '<div class="m-load2" ><div class="line">' +
        '<div></div> <div></div><div></div><div></div><div></div><div></div>' +
        '</div><div class="circlebg"></div></div>';
    $("#phonePool").html(loading);
    var cityCode = $("input[name='cityCode']").val();
    var minPreFee = $("input[name='minPreFee']").val();
    var maxPreFee = $("input[name='maxPreFee']").val();
    var section = $("input[name='section']").val();
    var guaranteeFee = $("input[name='guaranteeFee']").val();
    var tailNum = $("#phone").val();
    $.ajax({
        type: "POST",
        url: ctx + "simBeautifulBuy/getQueryBeautyNumsH5OnlineMock",
        dataType: "json",
        data: {
            cityCode: cityCode,
            minPreFee: minPreFee,
            maxPreFee: maxPreFee,
            section: section,
            guaranteeFee: guaranteeFee,
            tailNum: tailNum //查询字符串
        },
        success: function (data1) {
            $(".search-list").empty();
            if (data1.code != '0' || (data1.code === '0' && data1.data == null)) {
                $(".search-list").append('<div class="search-sorry"><p>抱歉</br>暂无匹配号码</p></div>');
                return;
            }
            var html = '<ul id="select-number-ul">';
            var charCount = tailNum.length;
            var j = data1.data.length > 20 ? 20 : data1.data.length;
            for (var i = 0; i < j; i++) {
                html += "<li class='pure-u-1-2'><a onclick='chooseNumber(this)'><span class='yxhm-number'>"
                    + data1.data[i].num.substring(0, 11 - charCount)
                    + "<span>" + data1.data[i].num.substring(11 - charCount, 11) + "</span></span>" +
                    "<p class='yxhm-txt' preFee='" + data1.data[i].guaranteedFee + "'"+
                    "guaranteedFee=" + data1.data[i].costNoRule + ">" +
                    "预存" + data1.data[i].guaranteedFee + "元 保底消费" + data1.data[i].costNoRule + "元/月</p></a></li>"
            }
            html += '</ul>';
            $("#phonePool").html(html);
        },
        error: function (data1) {
            $("#phonePool").empty();
            $("#phonePool").html('<div class="search-sorry"><p>抱歉</br>暂无匹配号码</p></div>');
        }
    });
}

function chooseNumber(obj) {
    $("input[name='chooseNum']").val($(obj).find('span').text());
    $("input[name='preFee']").val($(obj).find('p').attr('preFee'));
    $("input[name='guaranteedFee']").val($(obj).find('p').attr('guaranteedFee'));
    $("#showPlanInfo").submit();
}