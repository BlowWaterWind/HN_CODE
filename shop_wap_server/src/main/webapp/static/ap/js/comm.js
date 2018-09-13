//tabs
// $(function() {
//     $('.tabs').tabslet();
// });

/**
 * 浏览器打开：mozilla/5.0 (macintosh; intel mac os x 10_13_1) applewebkit/537.36 (khtml, like gecko) chrome/67.0.3396.99 safari/537.36
 * 微信打开：mozilla/5.0 (iphone; cpu iphone os 10_2 like mac os x) applewebkit/602.3.12 (khtml, like gecko) mobile/14c92 safari/601.1 wechatdevtools/1.02.1806120 micromessenger/6.5.7 language/zh_cn webview/15323288886018708 webdebugger port/16720
 * @type {string}
 */
var ua = window.navigator.userAgent.toLowerCase();//客户端信息

/**
 * 判断是否是客户端类型
 * return iso,android,weixin,html5
 */


/**
 * IOS打开和掌柜
 * Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)
 * Mobile/15F79 TJMobileChannel/TJMobileChannel AppName/TJCMClient Channel/iOS BuildVersion/3
 *
 * //TODO android打开和掌柜
 *
 * IPHONE打开
 * Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38
 * (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1
 *
 * 浏览器打开
 * Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36
 * @returns {string}
 */
function isAppClient(){
    if (/appname/.test(ua)) {
        return "hezhanggui";
    }else if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return "weixin";
    }else if (/iphone|ipad|ipod/.test(ua)) {// IOS
        return "ios";
    }else if(ua.match(/android/i) == "android") { //ANDROID
        return "android";
    }else{
        return "html5";
    }
}

