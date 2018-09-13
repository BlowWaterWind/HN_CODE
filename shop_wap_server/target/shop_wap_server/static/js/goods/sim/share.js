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



$(function () {
    if (getQueryString("CHANID") == "E050") {
        if($("#o2oDiv").text()==''){
            $('body').append('<div class="app-fix"   onclick = "shareSim()"><span class="app-icon"></span>分享</div>');
        }else {
            $("#o2oDiv").show();
            $("#mallDiv").hide();
        }
        //去掉我要推荐
        $(".float-icon").hide();
        $("#tjlabel").remove();
        $(".Grid").addClass("king-card-more").removeClass("Grid -around");
    }
    if(getQueryString("TYPE") == "O2O"){//O2O在线售卡的方式使用悬浮分享的方式
        $(".float-icon").show();
        $(".app-fix").remove();

    }
});
/**
 * 调用o2o渠道分享组件
 */
function shareSim() {
    var cardTypeName=$("#cardTypeName").val();
    var desc="";
    if(cardTypeName=='移动大王卡'){
        desc="0元领卡，首月免费体验";
    }else if(cardTypeName=='新和家庭套餐'){
        desc="0元领卡，全国流量不限量"
    }else{
        desc=$("#cardDesc").val();
    }
    var url="http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?confId="+getQueryString("confId")+
        "&recmdCode="+getQueryString("recmdCode")+"&shopId="+getQueryString("shopId");
    /*判断新老ios版本*/
    var isFlag = isNewIos();
    if(isFlag){
        window.webkit.messageHandlers.shareAllView.postMessage({'text':cardTypeName,'summary':desc,'url':url.replace(/(^\s*)|(\s*$)/g, ''),'iconUrl':("http://wap.hn.10086.cn" + imgUrl).replace(/(^\s*)|(\s*$)/g, '')});
    }else{
        o2oApp.openShareView(cardTypeName, desc, url.replace(/(^\s*)|(\s*$)/g, ''),
            ("http://wap.hn.10086.cn" + imgUrl).replace(/(^\s*)|(\s*$)/g, ''));
    }
}

/**
 * 调用o2o渠道分享组件
 * 原来js不能函数重载,只会执行下面一个
 */
function shareSimHaoduanka(cardTypeName,url) {
    var desc="";
    var imgUrl = "/shop/static/images/simpay/pay_7.png";
    if(cardTypeName=='移动大王卡'){
        desc="0元领卡，首月免费体验";
    }else if(cardTypeName=='新和家庭套餐'){
        desc="0元领卡，全国流量不限量"
    }else{
        desc=cardTypeName;
    }
    /*判断新老ios版本*/
    var isFlag = isNewIos();
    if(isFlag){
        window.webkit.messageHandlers.shareAllView.postMessage({'text':cardTypeName,'summary':desc,'url':url.replace(/(^\s*)|(\s*$)/g, ''),'iconUrl':("http://wap.hn.10086.cn" + imgUrl).replace(/(^\s*)|(\s*$)/g, '')});
    }else{
        o2oApp.openShareView(cardTypeName, desc, url.replace(/(^\s*)|(\s*$)/g, ''),
            ("http://wap.hn.10086.cn" + imgUrl).replace(/(^\s*)|(\s*$)/g, ''));
    }
}

function shareGroupSim(){
    var cardTypeName=$("#cardTypeName").val();
    var desc="";
    if(cardTypeName=='移动大王卡'){
        desc="0元领卡，首月免费体验";
    }else if(cardTypeName=='新和家庭套餐'){
        desc="0元领卡，全国流量不限量"
    }else{
        desc=cardTypeName;
    }
    /**
     * http://localhost:8084/shop/simBuy/simH5OnlineToApply?Prov=731&ChanID=0BFB3
     * &RecEmpID=A1KDGK01&recmdCode=NzcwMDc0NDU3NjAyNTM5NTI=&confId=E007QV5Q39&CHANID=E050&shopId=100004140086
     * @type {string}
     */
    var url="http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?Prov="+getQueryString("Prov")+
        "&ChanID="+getQueryString("ChanID")+"&RecEmpID="+getQueryString("RecEmpID") +"&recmdCode=" +getQueryString("recmdCode")+
        "&confId="+getQueryString("confId");
    console.log(url);
    /*判断新老ios版本*/
    var isFlag = isNewIos();
    if(isFlag){
        window.webkit.messageHandlers.shareAllView.postMessage({'text':cardTypeName,'summary':desc,'url':url.replace(/(^\s*)|(\s*$)/g, ''),'iconUrl':("http://wap.hn.10086.cn" + imgUrl).replace(/(^\s*)|(\s*$)/g, '')});
    }else{
        o2oApp.openShareView(cardTypeName, desc, url.replace(/(^\s*)|(\s*$)/g, ''),
            ("http://wap.hn.10086.cn" + imgUrl).replace(/(^\s*)|(\s*$)/g, ''));
    }
}
/**
 * 调用和掌柜分享
 */
function batchShare() {
    var linkUrl="http://wap.hn.10086.cn/shop/simBuy/simH5OnlineToApply?confId="+getQueryString("confId")+"&recmdCode="+getQueryString("recmdCode");
    window.location.href="http://wap.hn.10086.cn/ecsmc-static/static/sp/html/sms-create.html?templateId=2018040101&linkUrl="+encodeURI(linkUrl);
}
/**
 * 获取url中的参数
 * @param nameg
 * @returns {null}
 */
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return "";
}

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