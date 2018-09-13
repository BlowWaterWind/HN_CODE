/**
 * Created by udbac on 2018/7/11 11:00.
 */
var _rhtag, _tag;
window.webtrendsAsyncInit=function(){
    var dcs=new Webtrends.dcs().init({
        domain:window.location.protocol.indexOf("https:")==0 ? "www.hn.10086.cn:7099" : "www.hn.10086.cn:7098",
        dcsid:"v1/dcs5w0txb10000wocrvqy1nqm_6n1p",
        fpcdom:".10086.cn",
        fpc:"WT_FPCN",
        timezone:8,
        i18n:true,
        dcsdelay: 1000,
        enabled:false,
        plugins:{}
    });
    //dcs.WT.branch="hunan";
    var mobile ="";
    var metas = window.parent.document.getElementsByTagName("meta");
    for(i=0;i<metas.length;i++)
    {
        if(metas[i].getAttribute("name")=="WT.mobile")
            mobile = metas[i].getAttribute("content");
    }
    if(mobile){
        document.cookie = "mobile="+encode_mobile(mobile)+";path=/;domain="+window.location.host.split(':')[0];
    }else{
        mobile = document.cookie.replace(/(?:(?:^|.*;\s*)mobile\s*\=\s*([^;]*).*$)|^.*$/, "$1");
        if(mobile){
            if(is_mobile(mobile)){
                mobile = mobile;
            }else if( is_mobile(decode_mobile(mobile))){
                mobile = decode_mobile(mobile);
            }
        }
    }
    if(mobile!='' && mobile!=undefined && is_mobile(mobile)){
        dcs.WT.mobile = encode_mobile(mobile);
    }
    dcs.track();
    dcs.addSelector("input,a,button", {
        transform: function (dcsObject, multiTrackObject) {
            var e = multiTrackObject.element || {};
            var evt = multiTrackObject.event || {};
            var t, j;
            if (!e.type) {
                t=e.id + (e.name?("_"+e.name):"")+ (e.className?("_"+e.className):"")+(e.pathname?((e.pathname.indexOf("/")!=0)?"/"+e.pathname:e.pathname):"/");
                j = "Link";
            } else {
                // var metas = window.parent.document.getElementsByTagName("meta");
                // var si_n,si_x="";
                // for(i=0;i<metas.length;i++)
                // {
                // 	if(metas[i].getAttribute("name")=="WT.si_n")
                // 		si_n = metas[i].getAttribute("content");
                // 	if(metas[i].getAttribute("name")=="WT.si_x")
                // 		si_x = metas[i].getAttribute("content");
                // }
                // t = document.title +(si_n?"_"+si_n:"")+(si_x?"_"+si_x:"");
                // if(!t)
                t = e.id + (e.name?("_"+e.name):"") + (e.className?("_"+e.className):"")+ (e.value?("_"+e.value):"");
                j = e.type;
            };
            multiTrackObject.argsa = [];
            multiTrackObject.argsa.push("DCS.dcsuri", "/event.gif", "WT.event", t, "WT.obj", j);
        },
        delayTime: 100
    });
};

function is_mobile(mobile){
    var reg=/^(\+[0-9]{2,}-?)?1(3[0-9]|5[0-35-9]|8[0-9]|4[57]|7[678])[0-9]{8}$/;
    return reg.test(mobile);
}
function get_a_random(){
    var a=new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');
    return String(a[parseInt(Math.random()*(15+1),10)]);
}
function pre_fix_integer(num, n) {
    return (Array(n).join(0) + num).slice(-n);
}
function encode_mobile(mobile){
    var key='abcdef';
    var mobile=String(mobile);
    mobile=mobile.substring(0,2)+get_a_random()+get_a_random()+mobile.substring(2,5)+get_a_random()+get_a_random()+mobile.substring(5,8)+get_a_random()+mobile.substring(8,11);
    var m1=String(parseInt('0x'+String(mobile.substring(0,4)))^key);
    var m2=String(parseInt('0x'+String(mobile.substring(4,8)))^key);
    var m3=String(parseInt('0x'+String(mobile.substring(8,12)))^key);
    var m4=String(parseInt('0x'+String(mobile.substring(12,16)))^key);
    return m3+'-'+m4+'-'+m1+'-'+m2;
}
function decode_mobile(str){
    var key='abcdef';
    str=str.split("-");
    var m3=pre_fix_integer(Number(str[0]^key).toString(16),4);
    var m4=pre_fix_integer(Number(str[1]^key).toString(16),4);
    var m1=pre_fix_integer(Number(str[2]^key).toString(16),4);
    var m2=pre_fix_integer(Number(str[3]^key).toString(16),4);
    var m5=m1+m2+m3+m4;
    return m5.substring(0,2)+m5.substring(4,7)+m5.substring(9,12)+m5.substring(13,17);;
}
if (!window._tag) {
    function _wt() {
    };
    _wt.prototype.trackEvent = function () {
    };
    _wt.prototype.E = function ($h, $i) {
        var e = $h.target || $h.srcElement;
        while (e.tagName && (e.tagName.toLowerCase() != $i.toLowerCase())) {
            e = e.parentElement || e.parentNode;
            e = e || {};
        }
        ;
        return e;
    };
    _wt.prototype.P = function ($h) {
        var x = $h.clientX;
        var y = $h.clientY;
        $j = (document.documentElement != undefined && document.documentElement.clientHeight != 0) ? document.documentElement : document.body;
        var $k = window.pageXOffset == undefined ? $j.scrollLeft : window.pageXOffset;
        var $l = window.pageYOffset == undefined ? $j.scrollTop : window.pageYOffset;
        return (x + $k) + "x" + (y + $l);
    };
    _wt.prototype.N = function ($h) {
        var id = "";
        var $m = "";
        var $c = ["div", "table"];
        var $n = $c.length;
        var i, e, $o;
        for (i = 0; i < $n; i++) {
            $o = $c[i];
            if ($o.length) {
                e = this.E($h, $o);
                id = (e.getAttribute && e.getAttribute("id")) ? e.getAttribute("id") : "";
                $m = e.className || "";
                if (id.length || $m.length)break;
            }
        }
        ;
        return id.length ? id : $m;
    };
    Function.prototype.wtbind = function ($p) {
        var $q = this;
        var $r = function () {
            return $q.apply($p, arguments);
        };
        return $r;
    };
    _wt.prototype.dcsMultiTrack = function () {
        Webtrends.multiTrack({"argsa": arguments, delayTime: 100});
    }
    _rhtag = _tag = new _wt();
    _wt.prototype.dcsCollect = function () {

    }
}
var n=void 0;
(function(i,j,k,l){function m(a){if(a){if(!a.forEach)a.forEach=function(a,c){for(var e=c||window,d=0,g=this.length;d<g;++d)a.call(e,this[d],d,this)};if(!a.filter)a.filter=function(a,c){for(var e=c||window,d=[],g=0,h=this.length;g<h;++g)a.call(e,this[g],g,this)&&d.push(this[g]);return d};if(!a.indexOf)a.indexOf=function(a){for(var c=0;c<this.length;++c)if(this[c]===a)return c;return-1}}return a}if(!i.Ba||!i.Ba.zb){var d={zb:!0,e:{},plugins:{},hb:0,f:{},addEventListener:i.addEventListener?function(a,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     b,c){a.addEventListener&&a.addEventListener(b,c,!1)}:function(a,b,c){a.attachEvent&&a.attachEvent("on"+b,c,!1)},h:{},version:"10.4.2",i:{},pb:!1,t:500,$:function(){if(/MSIE ([0-9]{1,}[.0-9]{0,})/.exec(k.userAgent)!=null)d.da=parseFloat(RegExp.$1);if(l.search)d.i=d.pa(l.search);if(i.webtrendsAsyncInit&&!i.webtrendsAsyncInit.hasRun)i.webtrendsAsyncInit(),i.webtrendsAsyncInit.hasRun=!0;d.addEventListener(i,"load",function(){d.pb=!0})},g:function(a){return Object.prototype.toString.call(a)==="[object Function]"},
    qb:function(a){var b=[],c;for(c in a)a.hasOwnProperty(c)&&a[c]!=""&&a[c]!=n&&typeof a[c]!="function"&&b.push({k:c,v:a[c]});return b},extend:function(a,b,c){for(var e in b)if(c||typeof a[e]==="undefined")a[e]=b[e];return a},find:function(a){if(!d.wa)d.wa=d.nb();return d.wa(a)},nb:function(){var a=/MSIE (\d+)/.exec(k.userAgent),a=a?a[1]:99;if(j.querySelectorAll&&j.body&&a>8){var b=j.body;return function(a){return b.querySelectorAll(a)}}if(i.jQuery)return i.jQuery.find;if(i.Sizzle)return i.Sizzle;if(i.YAHOO&&
        YAHOO.za&&YAHOO.za.Aa)return YAHOO.za.Aa.Nb;if("qwery"in i)return qwery;i.YUI&&YUI().Pb("node",function(a){return a.all});return j.querySelectorAll?(b=j.body)?function(a){return b.querySelectorAll(a)}:function(){return[]}:function(){return[]}},pa:function(a){var a=a.split(/[&?]/g),b={};try{for(var c=0,e=a.length;c<e;++c){var d=a[c].match(/^([^=]+)(?:=([\s\S]*))?/);if(d&&d[1]){var g="";try{g=decodeURIComponent(d[1])}catch(h){try{g=unescape(d[1])}catch(j){g=d[1]}}var i="";try{i=decodeURIComponent(d[2])}catch(k){try{i=
        unescape(d[2])}catch(l){i=d[2]}}b[g]?(b[g]=[b[g]],b[g].push(i)):b[g]=i}}}catch(m){}return b},aa:function(a,b,c){arguments.length<2&&(b=!0);var e=j.createElement("script");e.type="text/javascript";e.async=b;e.src=a;var d=j.getElementsByTagName("script")[0];d.parentNode.insertBefore(e,d)},V:function(a,b){var c=a.target||a.srcElement;if(typeof b=="string"){var e=b,b={};b[e.toUpperCase()]=1}for(;c&&c.tagName&&!b[c.tagName.toUpperCase()];)c=c.parentElement||c.parentNode;return c},fa:function(a){return typeof encodeURIComponent==
    "function"?encodeURIComponent(a):escape(a)},sa:function(a){var o=a;if(typeof(arguments[0])=="string"){o = {'argsa': Array.prototype.slice.call(arguments)};}for(var b in d.e)d.e[b].ha(o);return!1},Q:function(a,b,c){b||(b="collect");c?d.D("transform."+b,a,c):d.D("transform."+b,a);return this},D:function(a,b,c){function e(b,c){d.h[a][b.n]||(d.h[a][b.n]=m([]));d.h[a][b.n].push(c)}if(a&&b&&a!=""&&d.g(b)){a==="wtmouseup"&&(a="wtmouse");if(a==="wtmouse"&&!d.ta){var f=/MSIE (\d+)/.exec(k.userAgent);d.addEventListener(j,(f?f[1]:99)>=8?"mousedown":"mouseup",function(b){if(!b)b=window.event;d.Pa(a,{event:b})});
        d.ta=!0}d.h[a]||(d.h[a]={});if(c)e(c,b);else for(var g in d.e)e(d.e[g],b)}},Pa:function(a,b){for(var c in d.e)d.fireEvent(a,d.e[c],b)},Ca:function(a,b,c,e){if(typeof b==="function")return b.onetime?(c.push(b),!0):(b(a,e),!1)},fireEvent:function(a,b,c){var e=m([]);if(d.h[a]&&d.h[a][b.n]){a=d.h[a][b.n];if(!a.length)return;for(var f=a.length-1;f>=0;f--)d.Ca(b,a[f],e,c)&&a.pop()}e.forEach(function(a){a(b,c)})},ca:function(a,b){var c=!1,e;for(e in d.e){var f=d.e[e];a in f.plugins&&(c=!0,f.ca(a,b))}c||
    b({Mb:!0})},T:function(a,b){for(var c=j.cookie.split("; "),e=[],d=0,g=0,h=a.length,p=c.length,g=0;g<p;g++){var i=c[g];i.substring(0,h+1)==a+"="&&(e[d++]=i)}c=e.length;if(c>0){d=0;if(c>1&&a==b){p=new Date(0);for(g=0;g<c;g++)i=new Date(parseInt(this.Wa(e[g],"lv"))),i>p&&(p.setTime(i.getTime()),d=g)}return unescape(e[d].substring(h+1))}else return null},Wa:function(a,b,c){a=a.split(c||":");for(c=0;c<a.length;c++){var e=a[c].split("=");if(b==e[0])return e[1]}return null}},q=d.fireEvent,r=d.D;d.b=function(){this.na=
    i.RegExp?/dcs(uri)|(ref)|(aut)|(met)|(sta)|(sip)|(pro)|(byt)|(dat)|(p3p)|(cfg)|(redirect)|(cip)/i:"";this.va={};this.plugins=this.plugins={};this.d=this.WT={};this.j=this.DCS={};this.q=this.DCSext={};this.n=this.dcssID="dcsobj_"+d.hb++;this.images=this.images=[];this.ma=this.errors=[];this.a=this.FPCConfig={};this.c=this.TPCConfig={};this.images=[];this.w=[];this.Eb=[];this.l=[];this.N=[];this.P="";this.ba=this.p=0;this.X=this.oa="";this.ta=!1;return this};d.b.prototype={$:function(a){function b(b,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         c){return a.hasOwnProperty(b)?a[b]:c}function c(a,b,c){return!a?c:a.hasOwnProperty(b)?a[b]:c}this.Gb=a;this.I=b("errorlogger",function(){});this.gb=this.dcsid=a.dcsid;this.L=this.queue=b("queue",[]);this.domain=this.domain=b("domain","statse.webtrendslive.com");this.Bb=this.timezone=b("timezone",-8);this.enabled=this.enabled=b("enabled",!0);this.Z=this.i18n=b("i18n",!0);this.va=i.RegExp?this.Z?{"%25":/\%/g,"%26":/\&/g,"%23":/\#/g}:{"%09":/\t/g,"%20":/ /g,"%23":/\#/g,"%26":/\&/g,"%2B":/\+/g,"%3F":/\?/g,
    "%5C":/\\/g,"%22":/\"/g,"%7F":/\x7F/g,"%A0":/\xA0/g}:"";if(a.metanames)this.ra=m(a.metanames.toLowerCase().split(","));i.webtrendsAsyncLoad&&typeof i.webtrendsAsyncLoad==="function"&&!b("privateFlag",!1)&&d.D("onready",window.webtrendsAsyncLoad,this);this.M=this.vtid=b("vtid",n);this.ua=b("paidsearchparams","gclid");this.yb=this.splitvalue=b("splitvalue","");d.t=a.dcsdelay||d.t;this.ib=this.delayAll=b("delayAll",!1);this.K=this.preserve=b("preserve",!0);this.w=m(b("navigationtag","div,table").toLowerCase().split(","));
    this.l=b("onsitedoms","");if(!d.g(this.l.test))this.l=m(this.l.toLowerCase().split(",")),this.l.forEach(function(a,b,c){c[b]=a.replace(/^\s*/,"").replace(/\s*$/,"")});this.N=m(b("downloadtypes","xls,doc,pdf,txt,csv,zip,docx,xlsx,rar,gzip").toLowerCase().split(","));this.N.forEach(function(a,b,c){c[b]=a.replace(/^\s*/,"").replace(/\s*$/,"")});if(b("adimpressions",!1))this.P=b("adsparam","WT.ac");this.a.enabled=this.FPCConfig.enabled=c(a.FPCConfig,"enabled",!0);this.a.domain=this.FPCConfig.domain=c(a.FPCConfig,
        "domain",b("fpcdom",""));this.a.name=this.FPCConfig.name=c(a.FPCConfig,"name",b("fpc","WT_FPC"));this.a.u=this.FPCConfig.expiry=c(a.FPCConfig,"expires",b("cookieexpires",63113851500));this.a.u=this.a.u<63113851500?this.a.u:63113851500;this.a.kb=new Date(this.getTime()+this.a.u);this.a.wb=this.a.u===0;this.c.enabled=this.TPCConfig.enabled=c(a.TPCConfig,"enabled",!b("disablecookie",!1));this.c.R=this.TPCConfig.cfgType=c(a.TPCConfig,"cfgType",!this.c.enabled?"1":"");if(a.cookieTypes)if(a.cookieTypes.toLowerCase()===
        "none")this.a.enabled=!1,this.c.enabled=!1,this.c.R="1";else if(a.cookieTypes.toLowerCase()==="firstpartyonly")this.a.enabled=!0,this.c.enabled=!1,this.c.R="1";else if(a.cookieTypes.toLowerCase()==="all")this.a.enabled=!0,this.c.enabled=!0,this.c.R=c(a.TPCConfig,"cfgType","");this.Jb=this.fpc=this.a.name;this.Kb=this.fpcdom=this.a.domain;this.Ib=this.cookieExp=this.a.u;m(b("pageEvents",[])).forEach(function(b){this.Db=a[b.toLowerCase()]=!0});b("offsite",!1)&&this.Ma();b("download",!1)&&this.Ka();
    b("anchor",!1)&&this.Ja();b("javascript",!1)&&this.La();b("rightclick",!1)&&this.Na();b("privateFlag",!1)||(d.e[this.n]=this);this.plugins=a.plugins||{};this.Fa();d.f[this.domain]||(d.f[this.domain]="");!b("privateFlag",!1)&&this.a.enabled&&this.Ya(this.n);this.F();return this},Fa:function(){for(var a in this.plugins){var b=this.plugins[a];if(!d.plugins[a]){d.plugins[a]=b;var c=b.src;d.g(c)?i.setTimeout(function(a){return function(){a()}}(c),1):d.aa(c,!1)}if(!b.async){var e=this;b.loaded=!1;this.p++;
    b.Qb&&this.ba++;b.timeout&&i.setTimeout(function(a){return function(){if(!a.loaded)a.Ab=!0,e.p--,e.F()}}(b),b.timeout)}}},Za:function(a){if(typeof a!="undefined")!d.f[this.domain]&&a.gTempWtId&&(d.f[this.domain]=a.gTempWtId),this.X=a.gTempWtId,!d.f[this.domain]&&a.gWtId&&(d.f[this.domain]=a.gWtId),this.oa=a.gWtAccountRollup;this.p--;this.F()},Ya:function(a){return j.cookie.indexOf(this.a.name+"=")==-1&&j.cookie.indexOf("WTLOPTOUT=")==-1&&this.c.enabled?!1:!0;},ca:function(a,b){var c=this.plugins[a];if(c)d.g(b)&&(this.tb()?b(this,c):r("pluginsLoaded",function(a,b,c){function d(){a(b,c)}d.onetime=!0;return d}(b,this,c),this)),c.loaded=!0,!c.async&&!c.Ab&&this.p--;this.F()},vb:function(){this.ba--;this.F()},tb:function(){return this.p<=0},F:function(){if(this.p<=0){if(!this.Ea)q("pluginsLoaded",this),this.Ea=!0;this.ba<=0&&this.xb()}},xb:function(){if(!this.Ga)q("onready",this),
    this.ob(),this.ub(),this.Ga=!0},ob:function(){for(var a=0;a<this.L.length;a++)this.ka(this.L[a]);this.L=[]},ub:function(){var a=this;this.L.push=function(b){a.ka(b)}},Q:function(a,b){d.Q(a,b,this)},r:function(a,b){var c=this,e=d.extend({domEvent:"click",callback:n,argsa:[],args:{},delayTime:n,transform:n,filter:n,actionElems:{A:1,INPUT:1,BUTTON:1},finish:n},b,!0);r("wtmouse",function(b,g){c.Oa(c,a,d.extend(g,e,!0))},c);return this},xa:function(a,b,c,e){b.element=c;if(e==="form"||e==="input"||e==="button")b.domEvent=
    "submit";a.ea(b)},Oa:function(a,b,c){var e=d.find;if(e&&c.event&&c.actionElems){var f=d.V(c.event,c.actionElems),g=f.tagName?f.tagName.toLowerCase():"";if(b.toUpperCase()in c.actionElems&&b.toUpperCase()===g.toUpperCase())return this.xa(a,c,f,g);if((b=e(b))&&f&&b&&b.length)for(e=0;e<b.length;e++)if(b[e]===f){this.xa(a,c,f,g);break}}},W:function(a,b){var c=m(j.cookie.split("; ")).filter(function(b){return b.indexOf(a+"=")!=-1})[0];if(!c||c.length<a.length+1)return!1;m(c.split(a+"=")[1].split(":")).forEach(function(a){a=
    a.split("=");b[a[0]]=a[1]});return!0},T:function(a){return d.T(a,this.a.name)},cb:function(a,b,c){var e=[],b=d.qb(b);m(b).forEach(function(a){e.push(a.k+"="+a.v)});e=e.sort().join(":");j.cookie=a+"="+e+c},Ta:function(a,b,c){a+="=";a+="; expires=expires=Thu, 01 Jan 1970 00:00:01 GMT";a+="; path="+b;a+=c?";domain="+c:"";document.cookie=a},$a:function(a,b,c,d){var f={};return this.W(a,f)?b==f.id&&c==f.lv&&d==f.ss?0:3:2},Xa:function(){var a={};this.W(this.a.name,a);return a},Va:function(){if(j.cookie.indexOf("WTLOPTOUT=")==
    -1)if(this.d.ce=!this.a.enabled&&!this.c.enabled?"0":this.a.enabled&&!this.c.enabled?"1":"2",this.a.enabled){var a=this.d,b=this.a.name,c=new Date,e=c.getTimezoneOffset()*6E4+this.Bb*36E5;c.setTime(c.getTime()+e);var f=new Date(c.getTime());a.co_f=a.vtid=a.vtvs=a.vt_f=a.vt_f_a=a.vt_f_s=a.vt_f_d=a.vt_f_tlh=a.vt_f_tlv="";var g={};if(this.W(b,g)){var h=g.id,p=parseInt(g.lv),i=parseInt(g.ss);if(h==null||h=="null"||isNaN(p)||isNaN(i))return;a.co_f=h;h=new Date(p);a.vt_f_tlh=Math.floor((h.getTime()-e)/
    1E3);f.setTime(i);if(c.getTime()>h.getTime()+18E5||c.getTime()>f.getTime()+288E5)a.vt_f_tlv=Math.floor((f.getTime()-e)/1E3),f.setTime(c.getTime()),a.vt_f_s="1";if(c.getDate()!=h.getDate()||c.getMonth()!=h.getMonth()||c.getFullYear()!=h.getFullYear())a.vt_f_d="1"}else{if(this.X.length)a.co_f=d.f[this.domain].length?d.f[this.domain]:this.X,a.vt_f="1";else if(d.f[this.domain].length)a.co_f=d.f[this.domain];else{a.co_f="2";i=c.getTime().toString();for(h=2;h<=32-i.length;h++)a.co_f+=Math.floor(Math.random()*
    16).toString(16);a.co_f+=i;a.vt_f="1"}this.oa.length==0&&(a.vt_f_a="1");a.vt_f_s=a.vt_f_d="1";a.vt_f_tlh=a.vt_f_tlv="0"}a.co_f=escape(a.co_f);a.vtid=typeof this.M=="undefined"?a.co_f:this.M||"";a.vtvs=(f.getTime()-e).toString();e=(this.a.wb?"":"; expires="+this.a.kb.toGMTString())+"; path=/"+(this.a.domain!=""?"; domain="+this.a.domain:"");c=c.getTime().toString();f=f.getTime().toString();g.id=a.co_f;g.lv=c;g.ss=f;this.cb(b,g,e);b=this.$a(b,a.co_f,c,f);if(b!=0)a.co_f=a.vtvs=a.vt_f_s=a.vt_f_d=a.vt_f_tlh=
    a.vt_f_tlv="",typeof this.M=="undefined"&&(a.vtid=""),a.vt_f=a.vt_f_a=b}else this.d.vtid=this.M?this.M:"",this.Ta(this.a.name,"/",this.a.domain)},Cb:function(){try{var a;arguments&&arguments.length>1?a={argsa:Array.prototype.slice.call(arguments)}:arguments.length===1&&(a=arguments[0]);typeof a==="undefined"&&(a={element:n,event:n,Fb:[]});typeof a.argsa==="undefined"&&(a.argsa=[]);this.la("collect",a);return this}catch(b){this.ma.push(b),this.I(b)}},ha:function(a){a&&a.length>1&&(a={argsa:Array.prototype.slice.call(arguments)});
    this.ea(a)},ea:function(a){try{typeof a==="undefined"&&(a={});this.la("multitrack",a);if(a.delayTime){var b=Number(a.delayTime);this.ya(isNaN(b)?200:b)}else this.ib&&this.ya(200);return!1}catch(c){this.ma.push(c),this.I(c)}},Ra:function(){this.j={};this.d={};this.q={};arguments.length%2==0&&this.U(arguments)},U:function(a){if(a)for(var b=0,c=a.length;b<c;b+=2)a[b].indexOf("WT.")==0?this.d[a[b].substring(3)]=a[b+1]:a[b].indexOf("DCS.")==0?this.j[a[b].substring(4)]=a[b+1]:a[b].indexOf("DCSext.")==0&&
(this.q[a[b].substring(7)]=a[b+1])},eb:function(a){var b,c;if(this.K){this.C=[];for(var d=0,f=a.length;d<f;d+=2)c=a[d],c.indexOf("WT.")==0?(b=c.substring(3),this.C.push(c,this.d[b]||"")):c.indexOf("DCS.")==0?(b=c.substring(4),this.C.push(c,this.j[b]||"")):c.indexOf("DCSext.")==0&&(b=c.substring(7),this.C.push(c,this.q[b]||""))}},bb:function(){if(this.K)this.U(this.C),this.C=[]},fb:function(){var a=new Date,b=this,c=this.d,e=this.j;c.tz=parseInt(a.getTimezoneOffset()/60*-1)||"0";c.bh=a.getHours()||
    "0";c.ul=k.language||k.userLanguage;if(typeof screen=="object")c.cd=k.appName=="Netscape"?screen.pixelDepth:screen.colorDepth,c.sr=screen.width+"x"+screen.height;typeof k.javaEnabled()=="boolean"&&(c.jo=k.javaEnabled()?"Yes":"No");j.title&&(c.ti=i.RegExp?j.title.replace(RegExp("^"+l.protocol+"//"+l.hostname+"\\s-\\s"),""):j.title);c.js="Yes";c.jv=function(){var a=navigator.userAgent.toLowerCase(),b=parseInt(navigator.appVersion),c=a.indexOf("mac")!=-1,d=a.indexOf("firefox")!=-1,e=a.indexOf("firefox/0.")!=
    -1,f=a.indexOf("firefox/1.0")!=-1,g=a.indexOf("firefox/1.5")!=-1,h=a.indexOf("firefox/2.0")!=-1,j=!d&&a.indexOf("mozilla")!=-1&&a.indexOf("compatible")==-1,i=a.indexOf("msie")!=-1&&a.indexOf("opera")==-1,k=i&&b==4&&a.indexOf("msie 4")!=-1,i=i&&!k,l=a.indexOf("opera 5")!=-1||a.indexOf("opera/5")!=-1,m=a.indexOf("opera 6")!=-1||a.indexOf("opera/6")!=-1,a=a.indexOf("opera")!=-1&&!l&&!m,o="1.1";d&&!e&&!f&!g&!h?o="1.8":h?o="1.7":g?o="1.6":e||f||j&&b>=5||a?o="1.5":c&&i||m?o="1.4":i||j&&b==4||l?o="1.3":
k&&(o="1.2");return o}();c.ct="unknown";if(j.body&&j.body.addBehavior)try{j.body.addBehavior("#default#clientCaps"),c.ct=j.body.Hb||"unknown",j.body.addBehavior("#default#homePage"),c.hp=j.body.Lb(location.href)?"1":"0"}catch(f){b.I(f)}var g=0,h=0;if(typeof i.innerWidth=="number")g=i.innerWidth,h=i.innerHeight;else if(j.documentElement&&(j.documentElement.clientWidth||j.documentElement.clientHeight))g=j.documentElement.clientWidth,h=j.documentElement.clientHeight;else if(j.body&&(j.body.clientWidth||
    j.body.clientHeight))g=j.body.clientWidth,h=j.body.clientHeight;c.bs=g+"x"+h;c.fv=function(){var a;if(i.ActiveXObject)for(a=15;a>0;a--)try{return new ActiveXObject("ShockwaveFlash.ShockwaveFlash."+a),a+".0"}catch(c){b.I(c)}else if(k.plugins&&k.plugins.length)for(a=0;a<k.plugins.length;a++)if(k.plugins[a].name.indexOf("Shockwave Flash")!=-1)return k.plugins[a].description.split(" ")[2];return"Not enabled"}();c.slv=function(){var a="Not enabled";try{k.userAgent.indexOf("MSIE")!=-1?new ActiveXObject("AgControl.AgControl")&&
(a="Unknown"):k.plugins["Silverlight Plug-In"]&&(a="Unknown")}catch(c){b.I(c)}if(a!="Not enabled"){var d,e,f;if(typeof Silverlight=="object"&&typeof Silverlight.qa=="function"){for(d=9;d>0;d--){e=d;if(Silverlight.qa(e+".0"))break;if(a==e)break}for(d=9;d>=0;d--){f=e+"."+d;if(Silverlight.qa(f)){a=f;break}if(a==f)break}}}return a}();this.Z&&(c.le=typeof j.defaultCharset=="string"?j.defaultCharset:typeof j.characterSet=="string"?j.characterSet:"unknown");c.tv=d.version;c.sp=this.yb;c.dl="0";if(d.i&&d.i.lb)c.fb_ref=
    d.i.lb;if(d.i&&d.i.mb)c.fb_source=d.i.mb;c.ssl=l.protocol.indexOf("https:")==0?"1":"0";e.dcsdat=a.getTime();e.dcssip=l.hostname;e.dcsuri=l.pathname;c.es=l.href;if(l.search)e.dcsqry=(l.search);if(e.dcsqry){a=e.dcsqry.toLowerCase();g=this.ua.length?this.ua.toLowerCase().split(","):[];for(h=0;h<g.length;h++)if(a.indexOf(g[h]+"=")!=-1){c.srch="1";break}}if(j.referrer!=""&&j.referrer!="-"&&!(k.appName=="Microsoft Internet Explorer"&&parseInt(k.appVersion)<4))e.dcsref=j.referrer;e.dcscfg=this.c.R},
    Ua:function(a,b){if(b!=""){if(a===null||a===n)return"";var a=a.toString(),c;for(c in b)b[c]instanceof RegExp&&(a=a.replace(b[c],c));return a}else return escape(a)},S:function(a,b){if(this.Z&&this.na!=""&&!this.na.test(a))if(a=="dcsqry"){for(var c="",e=b.substring(1).split("&"),f=0;f<e.length;f++){var g=e[f],h=g.indexOf("=");if(h!=-1){var i=g.substring(0,h),g=g.substring(h+1);f!=0&&(c+="&");c+=i+"="+d.fa(g)}}b=b.substring(0,1)+c}else b=d.fa(b);return"&"+a+"="+this.Ua(b,this.va)},Sa:function(a,b){if(j.images){var c=
        new Image;this.images.push(c);if(true){var e=!1;if(d.g(b.callback))f=b.callback;else f=function(a,b){};g=this;c.onload=function(){if(!e)return e=!0,f(g,b),!0};i.setTimeout(function(){if(!e){c.removeAttribute("src");return e=!0,f(g,b),!0}},d.t)}c.src=a}},ab:function(){var a;j.documentElement?a=j.getElementsByTagName("meta"):j.all&&(a=j.all.Ob("meta"));if(typeof a!="undefined")for(var b=a.length,c=0;c<b;c++){var d=a.item(c).name,f=a.item(c).content;a.item(c);d.length>0&&(d=d.toLowerCase(),d.toUpperCase().indexOf("WT.")==0?this.d[d.substring(3)]=
        f:d.toUpperCase().indexOf("DCSEXT.")==0?this.q[d.substring(7)]=f:d.toUpperCase().indexOf("DCS.")==0?this.j[d.substring(4)]=f:this.ra&&this.ra.indexOf(d)!=-1&&(this.q["meta_"+d]=f))}},ia:function(a){if(j.cookie.indexOf("WTLOPTOUT=")==-1){var b=this.d,c=this.j,e=this.q,f=this.i18n,g="http"+(l.protocol.indexOf("https:")==0?"s":"")+"://"+this.domain+(this.dcsid==""?"":"/"+this.dcsid)+"/events.svc?";f&&(b.dep="");for(var h in c)c[h]!=""&&c[h]!=n&&typeof c[h]!="function"&&(g+=this.S(h,c[h]));for(h in b)b[h]!=
    ""&&b[h]!=n&&typeof b[h]!="function"&&(g+=this.S("WT."+h,b[h]));for(h in e)if(e[h]!=""&&e[h]!=n&&typeof e[h]!="function")f&&(b.dep=b.dep.length==0?h:b.dep+";"+h),g+=this.S(h,e[h]);f&&b.dep.length>0&&(g+=this.S("WT.dep",b.dep));d.da&&d.da<9&&g.length>2048&&(g=g.substring(0,2040)+"&WT.tu=1");
        if ("undefined" != typeof WTjson)
            for (var _a in WTjson)
                if(_a.length && _a.length > 0 && (0 == _a.toUpperCase().indexOf("WT.")||0 == _a.toUpperCase().indexOf("DCSEXT.") ||0 == _a.toUpperCase().indexOf("DCS.")) )
                    g+="&"+(0 == _a.toUpperCase().indexOf("WT.")?_a:(0 == _a.toUpperCase().indexOf("DCSEXT.")?("WT."+_a.subString[7]):(0 == _a.toUpperCase().indexOf("DCS.")?("WT."+_a.subString[4]):"")))+"="+ WTjson[_a] ;
        this.Sa(g,a);this.d.ad=""}},rb:function(){this.fb();this.ab();this.P&&this.P.length>0&&this.Qa();this.sb=!0},getTime:function(){return(new Date).getTime()},jb:0,ya:function(a){for(var b=this.getTime();this.getTime()-
    b<a;)this.jb++},la:function(a,b){a||(a="collect");this.L.push({action:a,message:b})},ka:function(a){var b="action_"+a.action,c=a.message;this.sb||this.rb();c.event&&!c.element&&(c.element=d.V(c.event,{A:1}));if(!d.g(c.filter)||!c.filter(this,c)){if(c.args){c.argsa=c.argsa||[];for(var e in c.args)c.argsa.push(e,c.args[e])}c.element&&c.element.getAttribute&&c.element.getAttribute("data-wtmt")&&(c.argsa=c.argsa.concat(c.element.getAttribute("data-wtmt").split(",")));q("transform."+a.action,this,c);q("transform.all",
        this,c);c.transform&&d.g(c.transform)&&c.transform(this,c);if(this.enabled){this.Va();if(d.g(this[b]))this[b](c);q("finish."+a.action,this,c);q("finish.all",this,c);c.finish&&d.g(c.finish)&&c.finish(this,c)}}},Ia:function(a){var b=a&&a.argsa&&a.argsa.length%2==0;b&&(this.eb(a.argsa),this.U(a.argsa));this.j.dcsdat=this.getTime();this.ia(a);b&&this.bb()},Ha:function(a){a&&a.argsa&&a.argsa.length%2==0&&this.U(a.argsa);this.ia(a)},J:function(a){var b=document.createElement("a");b.href=a.href;a={};a.H=
        b.hostname?b.hostname.split(":")[0]:window.location.hostname;a.o=b.pathname?b.pathname.indexOf("/")!=0?"/"+b.pathname:b.pathname:"/";a.m=b.search?b.search.substring(b.search.indexOf("?")+1,b.search.length):"";a.G=i.location;return a},ga:function(a,b){if(a.length>0){a=a.toLowerCase();if(a==window.location.hostname.toLowerCase())return!0;if(d.g(b.test))return b.test(a);else if(b.length>0)for(var c=b.length,e=0;e<c;e++)if(a==b[e])return!0}return!1},ja:function(a,b){for(var c=a.toLowerCase().substring(a.lastIndexOf(".")+
        1,a.length),d=b.length,f=0;f<d;f++)if(c==b[f])return!0;return!1},s:function(a,b){var c="",e="",f=b.length,g,h;for(g=0;g<f;g++)if(h=b[g],h.length&&(e=d.V(a,h),c=e.getAttribute&&e.getAttribute("id")?e.getAttribute("id"):"",e=e.className||"",c.length||e.length))break;return c.length?c:e},Y:function(a,b,c){var e=j.all?b.innerText:b.text,a=d.V(a,{IMG:1}),f;if(a&&a.alt)f=a.alt;else if(e)f=e;else if(b.innerHTML)f=b.innerHTML;return f?f:c},B:function(){if(!this.K)this.Da=this.K=!0},z:function(){this.Da=this.K=
        !1},O:function(a){var b=!1;if(!a)a=window.event;a.which?b=a.which==3:a.button&&(b=a.button==2);return b},Ma:function(){this.r("a",{filter:function(a,b){var c=b.element||{},d=b.event||{};return c.hostname&&!a.ga(c.hostname,a.l)&&!a.O(d)?!1:!0},transform:function(a,b){var c=b.event||{},d=b.element||{};a.B(b);d=a.J(d);b.argsa.push("DCS.dcssip",d.H,"DCS.dcsuri",d.o,"DCS.dcsqry",d.m,"DCS.dcsref",d.G,"WT.ti","Offsite:"+d.H+d.o+(d.m.length?"?"+d.m:""),"WT.dl","24","WT.nv",a.s(c,a.w))},finish:function(a){a.z()}})},
    Ja:function(){this.r("a",{filter:function(a,b){var c=b.element||{},d=b.event||{};return a.ga(c.hostname,a.l)&&c.hash&&c.hash!=""&&c.hash!="#"&&!a.O(d)?!1:!0},transform:function(a,b){var c=b.event||{},d=b.element||{};a.B(b);d=a.J(d);b.argsa.push("DCS.dcssip",d.H,"DCS.dcsuri",escape(d.o+b.element.hash),"DCS.dcsqry",d.m,"DCS.dcsref",d.G,"WT.ti",escape("Anchor:"+b.element.hash),"WT.nv",a.s(c,a.w),"WT.dl","21")},finish:function(a){a.z()}})},Ka:function(){this.r("a",{filter:function(a,b){var c=b.event||
        {};return a.ja((b.element||{}).pathname,a.N)&&!a.O(c)?!1:!0},transform:function(a,b){var c=b.event||{},d=b.element||{};a.B(b);var f=a.J(d),d=a.Y(c,d,f.o);b.argsa.push("DCS.dcssip",f.H,"DCS.dcsuri",f.o,"DCS.dcsqry",f.m,"DCS.dcsref",f.G,"WT.ti","Download:"+d,"WT.nv",a.s(c,a.w),"WT.dl","20")},finish:function(a){a.z()}})},Na:function(){this.r("a",{filter:function(a,b){var c=b.event||{};return a.ja((b.element||{}).pathname,a.N)&&a.O(c)?!1:!0},transform:function(a,b){var c=b.event||{},d=b.element||{};a.B(b);
        var f=a.J(d),d=a.Y(c,d,f.o);b.argsa.push("DCS.dcssip",f.H,"DCS.dcsuri",f.o,"DCS.dcsqry",f.m,"DCS.dcsref",f.G,"WT.ti","RightClick:"+d,"WT.nv",a.s(c,a.w),"WT.dl","25")},finish:function(a){a.z()}})},La:function(){this.r("a",{filter:function(a,b){var c=b.element||{};return c.href&&c.protocol&&c.protocol.toLowerCase()==="javascript:"?!1:!0},transform:function(a,b){var c=b.event||{},d=b.element||{};a.B(b);var f=a.J(d);b.argsa.push("DCS.dcssip",i.location.hostname,"DCS.dcsuri",d.href,"DCS.dcsqry",f.m,"DCS.dcsref",
        f.G,"WT.ti","JavaScript:"+(d.innerHTML?d.innerHTML:""),"WT.dl","22","WT.nv",a.s(c,a.w))},finish:function(a){a.z()}})},Qa:function(){if(j.links){var a=this.P+"=",b=a.length,a=RegExp(a,"i"),c=j.links.length,d=end=-1,f=urlp=value="",g,f=j.URL+"",d=f.search(a);d!=-1&&(end=f.indexOf("&",d),urlp=f.substring(d,end!=-1?end:f.length),g=RegExp(urlp+"(&|#)","i"));for(var h=0;h<c;h++)if(j.links[h].href&&(f=j.links[h].href+"",urlp.length>0&&(f=f.replace(g,"$1")),d=f.search(a),d!=-1))d+=b,end=f.indexOf("&",d),
        value=f.substring(d,end!=-1?end:f.length),this.d.ad=this.d.ad?this.d.ad+";"+value:value}}};d.b.prototype.action_multitrack=d.b.prototype.Ia;d.b.prototype.action_collect=d.b.prototype.Ha;i.dcsMultiTrack=function(){for(var a=[],b=0;b<arguments.length;b++)a[b]=arguments[b];d.sa({argsa:a})};i.Webtrends=d;i.WebTrends=d;d.multiTrack=d.sa;d.dcs=d.b;d.dcss=d.e;d.addTransform=d.Q;d.bindEvent=d.D;d.getQryParams=d.pa;d.qryparams=d.i;d.dcsdelay=d.t;d.find=d.find;d.loadJS=d.aa;d.registerPlugin=d.ca;d.registerPluginCallback=
    d.vb;d.dcsGetCookie=d.T;d.b.prototype.init=d.b.prototype.$;d.b.prototype.dcsMultiTrack=d.b.prototype.ha;d.b.prototype.track=d.b.prototype.Cb;d.b.prototype.addSelector=d.b.prototype.r;d.b.prototype.dcsGetIdCallback=d.b.prototype.Za;d.b.prototype.dcsCleanUp=d.b.prototype.Ra;d.b.prototype.dcsGetFPC=d.b.prototype.Xa;d.b.prototype.addTransform=d.b.prototype.Q;d.b.prototype.dcsGetCookie=d.b.prototype.T;d.b.prototype.dcsNavigation=d.b.prototype.s;d.b.prototype.getTTL=d.b.prototype.Y;d.$()}})(window,window.document,window.navigator,window.location);
