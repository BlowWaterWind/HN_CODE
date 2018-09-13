function callbackB(data){
    console.info("----------------"+JSON.stringify(data));
    $("#longitude").val(data.lng);
    $("#latitude").val(data.lat);
    $("#adderss").val(data.address);
}
var data0 = [
    {'id': '0', 'value': '是'},
    {'id': '1', 'value': '否'}
];
var operaDom0 = document.querySelector('#hasElevator');//hidden
var operaIdDom0 = document.querySelector('#hasElevatorOp');//外层
operaIdDom0.addEventListener('click', function () {
    var val=$("#hasElevator").val();
    var operaId = operaIdDom0.dataset['id'];
    var operaName = operaIdDom0.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data0],
        {
            container: '.container',
            title: '是否有电梯',
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: 0,
            callback: function (selectOneObj) {
                operaDom0.value = selectOneObj.id;
                operaIdDom0.innerHTML = selectOneObj.value;
                operaIdDom0.dataset['id'] = selectOneObj.id;
                operaIdDom0.dataset['value'] = selectOneObj.value;
                $('#hasElevator').valid();
            }
        });
});

var data1 = [
    {'id': 'FTTB', 'value': 'FTTB'},
    {'id': 'FTTH', 'value': 'FTTH'},
    {'id': 'ADSL', 'value': 'ADSL'},
    {'id': '其他', 'value': '其他'}
];
var operaDom1 = document.querySelector('#portTypeOp');
var operaIdDom1 = document.querySelector('#portType');
operaDom1.addEventListener('click', function () {
    var operaId = operaDom1.dataset['id'];
    var operaName = operaDom1.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data1],
        {
            container: '.container',
            title: '移动线路接入模式（端口类型）',
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: operaId,
            callback: function (selectOneObj) {
                operaIdDom1.value = selectOneObj.id;
                operaDom1.innerHTML = selectOneObj.value;
                operaDom1.dataset['id'] = selectOneObj.id;
                operaDom1.dataset['value'] = selectOneObj.value;
                $('#portType').valid();
            }
        });
});
var data2 = [
    {'id': '开放式小区', 'value': '开放式小区'},
    {'id': '封闭式小区', 'value': '封闭式小区'},
    {'id': '专业市场', 'value': '专业市场'},
    {'id': '商务楼宇', 'value': '商务楼宇'},
    {'id': '沿街商铺', 'value': '沿街商铺'},
    {'id': '高校', 'value': '高校'},
    {'id': '中小学幼儿园', 'value': '中小学幼儿园'},
    {'id': '酒店', 'value': '酒店'},
    {'id': '厂园宿舍', 'value': '厂园宿舍'},
    {'id': '其他', 'value': '其他'}
];
var operaDom2 = document.querySelector('#communityTypeOp');
var operaIdDom2 = document.querySelector('#communityType');
operaDom2.addEventListener('click', function () {
    var operaId = operaDom2.dataset['id'];
    var operaName = operaDom2.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data2],
        {
            container: '.container',
            title: '小区类型',
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: operaId,
            callback: function (selectOneObj) {
                operaIdDom2.value = selectOneObj.id;
                operaDom2.innerHTML = selectOneObj.value;
                operaDom2.dataset['id'] = selectOneObj.id;
                operaDom2.dataset['value'] = selectOneObj.value;
                $("#communityType").valid();
            }
        });
});
var data3 = [
    {'id': 'FTTB', 'value': 'FTTB'},
    {'id': 'FTTH', 'value': 'FTTH'},
    {'id': 'ADSL', 'value': 'ADSL'},
    {'id': '其他', 'value': '其他'}
];
var operaDom3 = document.querySelector('#competitionPortType');//hidden
var operaIdDom3 = document.querySelector('#competitionPortTypeOp');//外层
operaIdDom3.addEventListener('click', function () {
    var operaId = operaIdDom3.dataset['id'];
    var operaName = operaIdDom3.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data3],
        {
            container: '.container',
            title: '小区内宽带运营商及竞争对手线路接入模式（端口类型）',
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: operaId,
            callback: function (selectOneObj) {
                operaDom3.value = selectOneObj.id;
                operaIdDom3.innerHTML = selectOneObj.value;
                operaIdDom3.dataset['id'] = selectOneObj.id;
                operaIdDom3.dataset['value'] = selectOneObj.value;
                $("#competitionPortType").valid();
            }
        });
});
var data4 = [
    {'id': '无', 'value': '无'},
    {'id': '正常', 'value': '正常'},
    {'id': '一般', 'value': '一般'},
    {'id': '困难', 'value': '困难'}
];
var operaDom4 = document.querySelector('#installDifficulty');//hidden
var operaIdDom4 = document.querySelector('#installDifficultyOp');//外层
operaIdDom4.addEventListener('click', function () {
    var operaId = operaIdDom4.dataset['id'];
    var operaName = operaIdDom4.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data4],
        {
            container: '.container',
            title: '装机难度',
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: operaId,
            callback: function (selectOneObj) {
                operaDom4.value = selectOneObj.id;
                operaIdDom4.innerHTML = selectOneObj.value;
                operaIdDom4.dataset['id'] = selectOneObj.id;
                operaIdDom4.dataset['value'] = selectOneObj.value;
            }
        });
});

var data5 = [
    {'id': '0', 'value': '是'},
    {'id': '1', 'value': '否'}
];
var operaDom5 = document.querySelector('#advPlaced');//hidden
var operaIdDom5 = document.querySelector('#advPlacedOp');//外层
operaIdDom5.addEventListener('click', function () {
    var operaId = operaIdDom5.dataset['id'];
    var operaName = operaIdDom5.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data5],
        {
            container: '.container',
            title: '广告是否到位',
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: operaId,
            callback: function (selectOneObj) {
                operaDom5.value = selectOneObj.id;
                operaIdDom5.innerHTML = selectOneObj.value;
                operaIdDom5.dataset['id'] = selectOneObj.id;
                operaIdDom5.dataset['value'] = selectOneObj.value;
                $("#advPlaced").valid();
            }
        });
});

var data6 = [
    {'id': '0', 'value': '是'},
    {'id': '1', 'value': '否'}
];
var operaDom6 = document.querySelector('#isAnnex');//hidden
var operaIdDom6 = document.querySelector('#isAnnexOp');//外层
operaIdDom6.addEventListener('click', function () {
    var operaId = operaIdDom6.dataset['id'];
    var operaName = operaIdDom6.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data6],
        {
            container: '.container',
            title: '是否附件',
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: operaId,
            callback: function (selectOneObj) {
                operaDom6.value = selectOneObj.id;
                operaIdDom6.innerHTML = selectOneObj.value;
                operaIdDom6.dataset['id'] = selectOneObj.id;
                operaIdDom6.dataset['value'] = selectOneObj.value;
            }
        });
});

$("#insertBtn").click(function(){
    $("#form1").submit();
});

var delParent;
var defaults = {
    fileType: ["jpg", "png", "bmp", "jpeg","JPG", "PNG", "BMP", "JPEG"], // 上传文件的类型
    fileSize: 1024 * 1024 * 5 // 上传文件的大小 50KB
};
$(function () {
    /*点击图片的文本框*/
    $("input[type='file']").change(function () {
        var idFile = $(this).attr("id");
        fileUp(idFile);
    });

    $(".wsdel-no").click(function () {
        $(".works-mask").hide();
    });
})
/*********************文件上传部分 start*********************/
function goodsFileUpload(url, fileElementId, sucCallback, errCallback) {
    $.ajaxFileUpload({
        url: url,//用于文件上传的服务器端请求地址
        secureuri: false,//一般设置为false
        fileElementId: fileElementId,//文件上传空间的id属性  <input type="file" id="file" name="file" />
        dataType: "json",//返回值类型 一般设置为json
        //服务器成功响应处理函数
        success: function (data, status) {
            sucCallback(data, status, fileElementId);
        },
        error: function (data, status, e) {//服务器响应失败处理函数
            errCallback(data, status, e);
        }
    });
}


function sucCallback(data, status, fileElementId) {
    var file = document.getElementById(fileElementId);
    var imgContainer = $(file).parents(".z_photo"); //存放图片的父亲元素
    if (data.length > 0) {
        //多文件上传
        for (var i = 0; i < data.length; i++) {
            if (('Y' == data[i].flag || 'y' == data[i].flag)) {
                var d = data[i];
                var $li = $("<li class='up-li'>");
                imgContainer.prepend($li);

                var $img0 = $("<img class='close-upimg'>").on("click", function (event) {
                    event.preventDefault();
                    event.stopPropagation();
                    $(".works-mask").show();
                    delParent = $(this).parent();
                });
                $img0.attr("src", ctxStatic + "/images/broadBand/img_close.png").appendTo($li);
                var $img = $("<img class='up-img'>");
                $img.attr("src", d.filePath);
                $img.appendTo($li);
                var $p = $("<p class='img-name-p'>");
                $p.html(d.fileName).appendTo($li);
                var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
                $input.attr("value", data[i].fileName);
                $input.appendTo($li);
            }else{
                showAlert(data[i]["info"]);
            }
        }
        $("input[type='file']").change(function () {
            fileUp(fileElementId);
        })
    } else {
         showAlert('文件上传失败！');
    }
}

function errCallback(data, status, e) {
    showAlert('文件上传失败！');
}

function fileUp(idFile) {
    var file = document.getElementById(idFile);
    var imgContainer = $(file).parents(".z_photo"); //存放图片的父亲元素
    var fileList = file.files; //获取的图片文件
    console.log(fileList + "======filelist=====");
    var input = $(this).parent(); //文本框的父亲元素
    var imgArr = [];
    //遍历得到的图片文件
    var numUp = imgContainer.find(".up-li").length;
    var totalNum = numUp + fileList.length; //总的数量
    if (fileList.length > 3 || totalNum > 3) {
        alert("上传图片数目不可以超过3个，请重新选择"); //一次选择上传超过3个 或者是已经上传和这次上传的到的总数也不可以超过3个
    } else if (numUp < 3) {
        fileList = validateUp(fileList);
        var upload_url = "ajaxImageUpload";
        goodsFileUpload(upload_url, idFile, sucCallback, errCallback);
    }
    numUp = imgContainer.find(".up-li").length;
    if (numUp >= 4) {
        $(file).parent().hide();
    }
    //input内容清空
    $(file).val("");
}

function validateUp(files) {
    var arrFiles = []; //替换的文件数组
    for (var i = 0, file; file = files[i]; i++) {
        //获取文件上传的后缀名
        var newStr = file.name.split("").reverse().join("");
        if (newStr.split(".")[0] != null) {
            var type = newStr.split(".")[0].split("").reverse().join("");
            console.log(type + "===type===");
            if (jQuery.inArray(type, defaults.fileType) > -1) {
                // 类型符合，可以上传
                if (file.size >= defaults.fileSize) {
                    alert(file.size);
                    alert('您这个"' + file.name + '"文件大小过大');
                } else {
                    // 在这里需要判断当前所有文件中
                    arrFiles.push(file);
                }
            } else {
                alert('您这个"' + file.name + '"上传类型不符合');
            }
        } else {
            alert('您这个"' + file.name + '"没有类型, 无法识别');
        }
    }
    return arrFiles;
}


$(".z_photo").delegate(".close-upimg", "click", function () {
    $(".works-mask").show();
    delParent = $(this).parent();
});

$(".wsdel-ok").click(function () {
    $(".works-mask").hide();
    var numUp = delParent.siblings().length;
    if (numUp <= 4) {
        delParent.parent().find(".z_file").show();
    }
    delParent.remove();
});

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

