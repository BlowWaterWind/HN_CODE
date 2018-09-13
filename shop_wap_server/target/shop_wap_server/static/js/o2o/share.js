var imgUrl="/res/img/T1MFKTBX_T1R4cSCrK.png";
var token = getCookie('token');
var appVersion = getCookie('appVersion');
var APP_TOKEN = null!=token?token:'';
var APP_VERSION = null !=appVersion?appVersion:'';
/*ios新版本与ios老版本的判断*/
function isNewIos(){
    var isFlag = IOS();
    if(isFlag){
        return isIOSApp();
    }
}
//判断是否是ios终端
function IOS()
{
    var u = navigator.userAgent;
    return !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
}
//IOS框架升级版本1.1.2
function isIOSApp(){
    var isIOS = IOS();
    if(isIOS && APP_VERSION == '1.1.2'){
        return true;
    }else{
        return false;
    }
}
$(function(){
    $("#shareBtn").click(function(){
        var shopId=$("#shopId").val();
        var url="http://wap.hn.10086.cn/ecsmc-static/static/cp/html/businessDetail.html?goodsId=2018032220289168&goodsType=0&h5Flag=1&sourceShopId="+shopId;
        var isFlag = isNewIos();
        if(isFlag){
            window.webkit.messageHandlers.shareAllView.postMessage({'text':"新和家庭套餐",'summary':"全家流量不限量 宽带电视免费用",'url':url.replace(/(^\s*)|(\s*$)/g,''),'iconUrl':("http://wap.hn.10086.cn"+imgUrl).replace(/(^\s*)|(\s*$)/g,'')});
        }else{
            o2oApp.openShareView("新和家庭套餐", "全家流量不限量 宽带电视免费用", url.replace(/(^\s*)|(\s*$)/g,''),
                ("http://wap.hn.10086.cn"+imgUrl).replace(/(^\s*)|(\s*$)/g,'')) ;
        }

    })

    $("#batchBtn").click(function(){
    	//alert(document.cookie);
        var shopId=$("#shopId").val();
        //var token = getCookie("token");
        //var userToken=getCookie("userToken");
        var secToken=$("#secToken").val();
        var url="http://wap.hn.10086.cn/ecsmc-static/static/cp/html/businessDetail.html?goodsId=2018032220289168&goodsType=0&h5Flag=1&sourceShopId="+shopId;
        window.location.href="http://wap.hn.10086.cn/ecsmc-static/static/sp/html/sms-create.html?secToken="+secToken+"&templateId=2018040102&linkUrl="+encodeURI(url);
    })
});

// 根据名字读取cookie
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg)){
        return unescape(arr[2]);
    }else{
        return null;
    }
}