$(document).ready(function() {
    $('.Chodertitle').click(function (event) {
        var _height = document.body.scrollHeight - 260;
        $('.ChoseList').show();
        //$(this).index() 返回这个参数在同辈中的索引
        $('.ChoseList .Listshow').eq($(this).index()).slideToggle().siblings().hide();
        $('.ChoseList .Listshow').eq($(this).index()).children('.boxbg').css('height', _height).show();
        $(this).toggleClass('curr').siblings().removeClass('curr');
    });
});

$('.qrynumbtn').click(function(event) {
    $(this).parents('.Listshow').slideUp();
    $('.ChoseList').slideToggle().siblings('.boxbg').hide();
});

$('.ListAddr>a,.unlimited>a,.proList_ul li,.orderSex').click(function(event) {
    $(this).addClass('curr').siblings().removeClass('curr');
    $(".Listshow").hide();
});


/**
 * 按号段查询
 * @param val
 */
function adValue(val) {
    haoDuan = val;
    searchNumber(1,haoDuan,"")
}

function searchNumber(start,startNumber,positionNumber){
    $("#mescroll ul").empty();
    var loading = '<div class="m-load2" ><div class="line">' +
        '<div></div> <div></div><div></div><div></div><div></div><div></div>' +
        '</div><div class="circlebg"></div></div>';
    $("#mescroll ul").append(loading);
    var cityCode = $("#cityCode").val();
    $.ajax({
        type: "POST",
        url: ctx + "simWholenet/groupNetNumQuery",
        dataType: "json",
        data: {
            departId:departId,
            cityCode : cityCode,
            start: start,
            startNumber:startNumber,
            number: positionNumber
        },
        success: function (data1) {
            $("#mescroll ul").empty();
            if (data1.code != 0) {
                $("#mescroll ul").html(ulHtml);
                $("#mescroll ul").append("<p class='text-center'>"+ data1.message+"</p>");
                return;
            }else if(data1.code === '0' && data1.data == null){
                 $("#mescroll ul").append("<p class='text-center'>很抱歉，暂无符合要求的号码！</p>");
                 return;
            }
            var html = "";
            var recmdCode = $("#recmdCode").val();
            data1.recmdCode = recmdCode;
            data1.confId = confId;
            var j = data1.data.length;
            var cardName = $("#cardTypeName").val();
            for (var i = 0; i < j; i++) {
                var btnColor;
                var cartItem = getCookieStatic('cartItem');
                if(cartItem != null && cartItem.indexOf(data1.data[i].num)==-1){
                    //不存在
                    btnColor = 'btn-border-rose';
                }else if(cartItem == null){
                    btnColor = 'btn-border-rose';
                }else{
                    btnColor = 'btn-border-gray';
                }
                html += '<li><p class="list-phone-txt"><span>'+data1.data[i].num+'</span></p>' +
                    '<p class="list-phone-txt2 text-center">'+cityObject[data1.data[i].cityCode]+'</p>' +
                    '<p class="flex1">'+cardName+'</p><a class="btn btn-middle btn-border btn-border-blue" onclick="toBuy(\''+ data1.data[i].num +'\',\''+ data1.recmdCode+'\',\''+data1.confId+'\',\''+data1.data[i].cityCode+'\')">购买</a><a ' +
                    'class="btn btn-middle btn-border '+btnColor+'" onclick="dmlCollect(this,\''+data1.data[i].num +'\',\''+data1.data[i].cityCode +'\',\''+data1.recmdCode +'\',\''+data1.confId+'\',\''+cardName+'\')">收藏</a></li>';
            }
            $("#mescroll ul").html(html);
        },
        error: function () {
            $("#mescroll ul").empty();
            $("#select-number-ul").append("<p class='text-center'>很抱歉，暂无符合要求的号码！</p>");
        }
    });
}
