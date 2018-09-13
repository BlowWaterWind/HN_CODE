$(function() {
    var delParent;
    var defaults = {
        fileType: ["jpg", "png", "bmp", "jpeg"], // 上传文件的类型
        fileSize: 1024 * 1024 * 5 // 上传文件的大小 50KB
    };
    /*点击图片的文本框*/
    $(".file").change(function() {
        var idFile = $(this).attr("id");
        var file = document.getElementById(idFile);
        var imgContainer = $(this).parents(".z_photo"); //存放图片的父亲元素
        var fileList = file.files; //获取的图片文件
        console.log(fileList + "======filelist=====");
        var input = $(this).parent(); //文本框的父亲元素
        var imgArr = [];
        //遍历得到的图片文件
        var numUp = imgContainer.find(".up-li").length;
        var totalNum = numUp + fileList.length; //总的数量
        if (fileList.length > 6 || totalNum > 6) {
            alert("上传图片数目不可以超过5个，请重新选择"); //一次选择上传超过5个 或者是已经上传和这次上传的到的总数也不可以超过5个
        } else if (numUp < 6) {
            fileList = validateUp(fileList);
            for (var i = 0; i < fileList.length; i++) {
                var imgUrl = window.URL.createObjectURL(fileList[i]);
                imgArr.push(imgUrl);
                var $li = $("<li class='up-li loading'>");
                imgContainer.prepend($li);

                var $img0 = $("<img class='close-upimg'>").on("click", function(event) {
                    event.preventDefault();
                    event.stopPropagation();
                    $(".works-mask").show();
                    delParent = $(this).parent();
                });
                $img0.attr("src", "images/img_close.png").appendTo($li);
                var $img = $("<img class='up-img up-opcity'>");
                $img.attr("src", imgArr[i]);
                $img.appendTo($li);
                var $p = $("<p class='img-name-p'>");
                $p.html(fileList[i].name).appendTo($li);
                var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
                $input.appendTo($li);
                var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
                $input2.appendTo($li);

            }
        }
        setTimeout(function() {
            $(".up-li").removeClass("loading");
            $(".up-img").removeClass("up-opcity");
        }, 350);
        numUp = imgContainer.find(".up-li").length;
        if (numUp >= 6) {
            $(this).parent().hide();
        }
        //input内容清空
        $(this).val("");
    });



    $(".z_photo").delegate(".close-upimg", "click", function() {
        $(".works-mask").show();
        delParent = $(this).parent();
    });

    $(".wsdel-ok").click(function() {
        $(".works-mask").hide();
        var numUp = delParent.siblings().length;
        if (numUp <= 6) {
            delParent.parent().find(".z_file").show();
        }
        delParent.remove();

    });

    $(".wsdel-no").click(function() {
        $(".works-mask").hide();
    });

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























})