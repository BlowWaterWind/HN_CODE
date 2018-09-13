var delParent;
var defaults = {
    fileType: ["jpg", "png", "bmp", "jpeg"], // 上传文件的类型
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
                var $li = $("<li class='up-li loading'>");
                imgContainer.prepend($li);

                var $img0 = $("<img class='close-upimg'>").on("click", function (event) {
                    event.preventDefault();
                    event.stopPropagation();
                    $(".works-mask").show();
                    delParent = $(this).parent();
                });
                $img0.attr("src", ctxStatic + "/images/broadBand/img_close.png").appendTo($li);
                var $img = $("<img class='up-img up-opcity'>");
                $img.attr("src", d.filePath);
                $img.appendTo($li);
                var $p = $("<p class='img-name-p'>");
                $p.html(d.fileName).appendTo($li);
                var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
                $input.attr("value", data[i].fileName);
                $input.appendTo($li);
            }
        }
        $("input[type='file']").change(function () {
            fileUp(fileElementId);
        })
    } else {
        // showAlert(data[0]["info"]);
    }
}

function errCallback(data, status, e) {
    // showAlert(data[0]["info"]);
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
    if (fileList.length > 4 || totalNum > 4) {
        alert("上传图片数目不可以超过4个，请重新选择"); //一次选择上传超过5个 或者是已经上传和这次上传的到的总数也不可以超过5个
    } else if (numUp < 4) {
        fileList = validateUp(fileList);
        var upload_url = "ajaxImageUpload";
        goodsFileUpload(upload_url, idFile, sucCallback, errCallback);
    }
    setTimeout(function () {
        $(".up-li").removeClass("loading");
        $(".up-img").removeClass("up-opcity");
    }, 350);
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

var imgNames = ["imageUrlA", "imageUrlB", "imageUrlC", "imageUrlD"];
function confirmSubmit() {
    var photoLi = $(".z_photo").find("li");
    var inputHtml = "";
    var i = 0;
    photoLi.each(function (index) {
        var fileName = $(this).find("input[name='taglocation']").val();
        inputHtml += "<input type='hidden' name='" + imgNames[i] + "' value='" + fileName + "'/>";
        i++;
    });
    $(".z_photo").append(inputHtml);
    var params = $("#partnerInfoForm").serializeObject();
    var communityId = $("input[name='communityId']").val();
    var synchronizedType = $("#synchronizedType").val();
    showLoadPop();
    $.ajax({
        data: params,
        url:"addPartnerInfo",
        type: "post",
        dataType: "json",
        success: function (data) {
            if (data.code == '0') {
                hideLoadPop();
                showAlert(data.message, function () {
                    //跳转到列表页面;同步类型 0电渠侧录入 1CRM同步
                    if(synchronizedType == '0'){
                        window.location.href = ctx+"/bandInfoCollect/initAdd?communityId="+communityId+"&infoType=2&tab=tab2";
                    }else{
                        window.location.href = ctx+"/bandInfoCollect/initAddPartnerOrUser?communityId="+communityId+"&type=1&tab=tab2";
                    }
                });
            } else {
                hideLoadPop();
                showAlert(data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            hideLoadPop();
            showAlert("添加友商信息失败!");
        }
    });

}