//var chooseCamera;
var cropImage;
var imgData;
var clipContent;
var clipAction;
var showContent;
var showImg;
var targetImgCamera;
var container;//原先界面的元素，需要酌情修改

initPage();

function initPage() {
    initParams();
    initListeners();
    initImgClip();
}

function initParams() {
    //数据插入
    var html = '<div class="clip-content"><div class="upload-container choose-camera hidden"><div class="upload-pretty button-three-dimen"><input type="file" id="targetImgCamera" capture="camera">手机拍摄</div></div><div class="img-clip"></div><nav class="clip-action nav-bar nav-bar-tab hidden"><a class="tab-item" id="btn-reload"><span class="mui-icon mui-icon-arrowleft tab-icon"></span><span class="tab-label hidden">取消</span></a><a class="tab-item " id="btn-rotate-anticlockwise"><span class="mui-icon mui-icon-refreshempty tab-icon rotate90"></span><span class="tab-label hidden">逆时针旋转</span></a><a class="tab-item " id="btn-rotate-clockwise"><span class="mui-icon mui-icon-refreshempty tab-icon"></span>        <span class="tab-label hidden">顺时针旋转</span>        </a>        <a class="tab-item " id="btn-maxrect">        <span class="mui-icon mui-icon-navigate tab-icon"></span>        <span class="tab-label hidden">最大选择</span></a><a class="tab-item" id="btn-verify"><span class="mui-icon mui-icon-checkmarkempty tab-icon"></span>        <span class="tab-label hidden">确定</span>        </a>        </nav>        </div>        <div class="show-content hidden">        <div class="img-wrap">        <img class="show-img" data-preview-src="" data-preview-group="2"></img>        </div>        <nav class="nav-bar nav-bar-tab">        <a class="tab-item" id="btn-back">        <span class="mui-icon mui-icon-arrowleft tab-icon"></span>        <span class="tab-label hidden">取消</span>        </a>        <a class="tab-item" id="btn-detail">        <span class="mui-icon mui-icon-more-filled tab-icon"></span>        <span class="tab-label hidden">详情</span>        </a>        <a class="tab-item" id="btn-save">        <span class="mui-icon mui-icon-checkmarkempty tab-icon"></span><span class="tab-label hidden">确定</span></a></nav></div>';
    $('#clip').html(html);//需要有一个承载div
    targetImgCamera = $('#targetImgCamera');
    //chooseCamera = document.querySelector('.choose-camera');
    clipContent = $('.clip-content');
    clipAction = $('.clip-action');
    showContent = $('.show-content');
    showImg = $('.show-img');
    container = $('.container');
}

function initImgClip() {
    new FileInput({
        container: '#targetImgCamera',
        isMulti: false,
        type: 'Camera',
        success: function(b64, file, detail) {
            // console.log("选择:" + b64);
            console.log("fileName:" + file.name);
            loadImg(b64);
        },
        error: function(error) {
            console.error(error);
        }
    });
}

function loadImg(b64) {
    changeImgClipShow(true);

    var img = new Image();
    img.src = b64;

    img.onload = function() {
        EXIF.getData(img, function() {
            var orientation = EXIF.getTag(this, 'Orientation');

            cropImage && cropImage.destroy();
            cropImage = new ImageClip({
                container: '.img-clip',
                img,
                // 0代表按下才显示，1恒显示，-1不显示
                sizeTipsStyle: 0,
                isSmooth:true,
                // 为1一般是屏幕像素x2这个宽高
                // 最终的大小为：屏幕像素*屏幕像素比（手机中一般为2）*compressScaleRatio
                compressScaleRatio: 1,
                // iphone中是否继续放大：x*iphonforceWidtheFixedRatio
                // 最好compressScaleRatio*iphoneFixedRatio不要超过2
                iphoneFixedRatio: 1.8,
                // 减去顶部间距，底部bar,以及显示间距
                maxCssHeight: window.innerHeight - 100 - 50 - 20,
                // 放大镜捕获的图像半径
                captureRadius: 100,
                // 是否采用原图像素（不会压缩）
                isUseOriginSize: true,
                // 增加最大宽度，增加后最大不会超过这个宽度
                maxWidth: 0,
                // 是否固定框高，优先级最大，设置后其余所有系数都无用直接使用这个固定的宽，高度自适应
                forceWidth: 0,
                // 同上，但是一般不建议设置，因为很可能会改变宽高比导致拉升，特殊场景下使用
                forceHeight: 0,
                // 压缩质量
                quality: 0.99,
                mime: 'image/jpeg',
            });

            // 6代表图片需要顺时针修复（默认逆时针处理了，所以需要顺过来修复）
            switch (orientation) {
                case 6:
                    cropImage.rotate(true);
                    break;
                default:
                    break;
            }

        });
    };
}

function resizeShowImg(b64) {
    var img = new Image();

    img.src = b64;
    img.onload = showImgOnload;
}

function showImgOnload() {
    // 必须用一个新的图片加载，否则如果只用showImg的话永远都是第1张
    // margin的话由于有样式，所以自动控制了
    var width = this.width;
    var height = this.height;
    var wPerH = width / height;
    var MAX_WIDTH = Math.min(window.innerWidth, width);
    var MAX_HEIGHT = Math.min(window.innerHeight - 50 - 100, height);
    var legalWidth = MAX_WIDTH;
    var legalHeight = legalWidth / wPerH;

    if (MAX_WIDTH && legalWidth > MAX_WIDTH) {
        legalWidth = MAX_WIDTH;
        legalHeight = legalWidth / wPerH;
    }
    if (MAX_HEIGHT && legalHeight > MAX_HEIGHT) {
        legalHeight = MAX_HEIGHT;
        legalWidth = legalHeight * wPerH;
    }

    var marginTop = (window.innerHeight - 50 - legalHeight) / 2;

    showImg.css('marginTop',marginTop);
    showImg.css('width',legalWidth);
    showImg.css('height',legalHeight);
}

function changeImgClipShow(isClip) {

    console.log("changeImgClipShow->" +isClip )
    if (isClip) {
        //chooseCamera.classList.add('hidden');
        clipContent.removeClass('hidden');
        clipAction.removeClass('hidden');
        container.addClass('hidden');
    } else {
        //chooseCamera.classList.remove('hidden');
        clipAction.addClass('hidden');
        container.removeClass('hidden');
        // 需要改变input，否则下一次无法change
        targetImgCamera.value = '';
    }
}

function initListeners() {
    $('#clip').on('click','#btn-reload', function() {
        cropImage && cropImage.destroy();
        changeImgClipShow(false);
    });
    $('#clip').on('click', '#btn-back',function() {
        changeContent(false);
    });
    $('#clip').on('click','#btn-save', function() {
        // downloadFile(imgData);
        //上传图片识别
        showContent.addClass('hidden');
        clipContent.addClass('hidden');
        container.removeClass('hidden');
        targetImgCamera.value = '';
        console.log(imgData);
        //获取图片数据
        var pic = imgData.replace(/^data:image\/(png|jpg|jpeg);base64,/, "");
        //tips('上传成功');
        layer.open({
            type: 2,
            shadeClose: false
        });
        $.ajax({
            type: 'POST',
            url: ctx+'o2oSimPreBuy/ocr',
            data: {"imageData" : pic },
            dataType: 'json',
            success: function (msg) {
               layer.closeAll();
               if(msg.code == 0){
                   $("#simCardNo").val(msg.message);
               }else{
                   layer.open({
                       content: msg.message,
                       btn: '我知道了'
                   });
               }
            }
            ,error: function () {
                layer.open({
                    content: '网络错误',
                    btn: '我知道了'
                });
                return;
            }
        });
});
    $('#clip').on('click', '#btn-detail',function() {
        showImgDataLen(imgData);
    });

    $('#clip').on('click', '#btn-maxrect',function() {
        if (!cropImage) {
            tips('请选择图片');
            return;
        }
        cropImage.resetClipRect();
    });

    $('#clip').on('click','#btn-rotate-anticlockwise', function() {
        if (!cropImage) {
            tips('请选择图片');
            return;
        }
        cropImage.rotate(false);
    });

    $('#clip').on('click', '#btn-rotate-clockwise',function() {
        if (!cropImage) {
            tips('请选择图片');
            return;
        }
        cropImage.rotate(true);
    });

    $('#clip').on('click','#btn-verify', function() {
        if (!cropImage) {
            tips('请选择图片');
            return;
        }
        cropImage.clip(false);
        imgData = cropImage.getClipImgData();
        recognizeImg(function() {
            changeContent(true);
        }, function(error) {
            tips(JSON.stringify(error), true);
        });

    });
}

function showImgDataLen(imgData) {
    var len = imgData.length;
    var sizeStr = len + 'B';

    if (len > 1024 * 1024) {
        sizeStr = (Math.round(len / (1024 * 1024))).toString() + 'MB';
    } else if (len > 1024) {
        sizeStr = (Math.round(len / 1024)).toString() + 'KB';
    }

    tips('处理后大小：' + sizeStr);
}

function tips(msg, isAlert) {
    if (isAlert) {
        alert(msg);
    } else {
        toast(msg);
    }
}

function toast(message) {
    var CLASS_ACTIVE = 'mui-active';
    var duration = 2000;
    var toastDiv = document.createElement('div');

    toastDiv.classList.add('mui-toast-container');
    toastDiv.innerHTML = `<div class="mui-toast-message">${message}</div>`;
    toastDiv.addEventListener('webkitTransitionEnd', () => {
        if (!toastDiv.classList.contains(CLASS_ACTIVE)) {
        toastDiv.parentNode.removeChild(toastDiv);
        toastDiv = null;
    }
});
    // 点击则自动消失
    toastDiv.addEventListener('click', () => {
        toastDiv.parentNode.removeChild(toastDiv);
    toastDiv = null;
});
    document.body.appendChild(toastDiv);
    toastDiv.classList.add(CLASS_ACTIVE);
    setTimeout(function() {
        toastDiv && toastDiv.classList.remove(CLASS_ACTIVE);
    }, duration);
}

function changeContent(isShowContent) {
    console.log("changeContent->" +isShowContent )
    if (isShowContent) {
        showContent.removeClass('hidden');
        clipContent.addClass('hidden');
        resizeShowImg(imgData);
        showImg.attr('src',imgData);

    } else {
        showContent.addClass('hidden');
        clipContent.removeClass('hidden');
    }
}

function b64ToBlob(urlData) {
    var arr = urlData.split(',');
    var mime = arr[0].match(/:(.*?);/)[1] || 'image/png';
    // 去掉url的头，并转化为byte
    var bytes = window.atob(arr[1]);

    // 处理异常,将ascii码小于0的转换为大于0
    var ab = new ArrayBuffer(bytes.length);
    // 生成视图（直接针对内存）：8位无符号整数，长度1个字节
    var ia = new Uint8Array(ab);
    for (var i = 0; i < bytes.length; i++) {
        ia[i] = bytes.charCodeAt(i);
    }

    return new Blob([ab], {
        type: mime
    });
}

function downloadFile(content) {
    // Convert image to 'octet-stream' (Just a download, really)
    var imageObj = content.replace("image/jpeg", "image/octet-stream");
    window.location.href = imageObj;
}

function recognizeImg(success, error) {
    // 里面正常有：裁边，摆正，梯形矫正，锐化等算法操作
    success();
}

function upload(success, error) {
    success();
}