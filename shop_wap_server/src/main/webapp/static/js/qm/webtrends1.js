var _tag;window.webtrendsAsyncInit=function(){var a=new Webtrends.dcs().init({domain:window.location.protocol.indexOf("https:")==0 ? "www.hn.10086.cn:7099" : "www.hn.10086.cn:7098",dcsid:"v1/dcs5w0txb10000wocrvqy1nqm_6n1p",timezone:8,i18n:true,dcsdelay:1000,fpcdom:'.10086.cn',fpc:"WT_FPCN",plugins:{}});a.WT.branch="pc";var b="";var c=window.parent.document.getElementsByTagName("meta");for(i=0;i<c.length;i++){if(c[i].getAttribute("name")=="WT.mobile"){b=c[i].getAttribute("content")}}if(!b){b=document.cookie.replace(/(?:(?:^|.*;\s*)mobile\s*\=\s*([^;]*).*$)|^.*$/,"$1");}if(b){if(is_mobile(b)){a.WT.mobile=b;document.cookie="mobile="+encode_mobile(b)+";path=/;domain="+window.location.host.split(":")[0];}else{if(is_mobile(decode_mobile(b))){a.WT.mobile=decode_mobile(b);document.cookie="mobile="+b+";path=/;domain="+window.location.host.split(":")[0];}}}; if(document.cookie.replace(/(?:(?:^|.*;\s*)WT.rh_user\s*\=\s*([^;]*).*$)|^.*$/,"$1")){b= document.cookie.replace(/(?:(?:^|.*;\s*)WT.rh_user\s*\=\s*([^;]*).*$)|^.*$/,"$1").split(":")[0].split("=")[1];if(is_mobile(b)){a.WT.mobile=b;document.cookie="mobile="+encode_mobile(b)+";path=/;domain="+window.location.host.split(":")[0];}else if(is_mobile(decode_mobile(b))){a.WT.mobile=decode_mobile(b);document.cookie="mobile="+b+";path=/;domain="+window.location.host.split(":")[0];}}
if(document.cookie.replace(/(?:(?:^|.*;\s*)WT.rh_login\s*\=\s*([^;]*).*$)|^.*$/,"$1")){a.WT.logintype=document.cookie.replace(/(?:(?:^|.*;\s*)WT.rh_login\s*\=\s*([^;]*).*$)|^.*$/,"$1").split("=")[1];}a.track()};
function is_mobile(a){var b=/^(\+[0-9]{2,}-?)?1(3[0-9]|5[0-35-9]|8[0-9]|4[57]|7[678])[0-9]{8}$/;return b.test(a)}function get_a_random(){var b=new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");return String(b[parseInt(Math.random()*(15+1),10)])}function pre_fix_integer(a,b){return(Array(b).join(0)+a).slice(-b)}function encode_mobile(f){var h="abcdef";var f=String(f);f=f.substring(0,2)+get_a_random()+get_a_random()+f.substring(2,5)+get_a_random()+get_a_random()+f.substring(5,8)+get_a_random()+f.substring(8,11);var d=String(parseInt("0x"+String(f.substring(0,4)))^h);var c=String(parseInt("0x"+String(f.substring(4,8)))^h);var b=String(parseInt("0x"+String(f.substring(8,12)))^h);var a=String(parseInt("0x"+String(f.substring(12,16)))^h);return b+"-"+a+"-"+d+"-"+c}function decode_mobile(h){var f="abcdef";h=h.split("-");var b=pre_fix_integer(Number(h[0]^f).toString(16),4);var a=pre_fix_integer(Number(h[1]^f).toString(16),4);var d=pre_fix_integer(Number(h[2]^f).toString(16),4);var c=pre_fix_integer(Number(h[3]^f).toString(16),4);var j=d+c+b+a;return j.substring(0,2)+j.substring(4,7)+j.substring(9,12)+j.substring(13,17)}if(!window._tag){function _wt(){}_wt.prototype.trackEvent=function(){};_wt.prototype.E=function(a,c){var b=a.target||a.srcElement;while(b.tagName&&(b.tagName.toLowerCase()!=c.toLowerCase())){b=b.parentElement||b.parentNode;b=b||{}}return b};_wt.prototype.P=function(b){var a=b.clientX;var f=b.clientY;$j=(document.documentElement!=undefined&&document.documentElement.clientHeight!=0)?document.documentElement:document.body;var d=window.pageXOffset==undefined?$j.scrollLeft:window.pageXOffset;var c=window.pageYOffset==undefined?$j.scrollTop:window.pageYOffset;return(a+d)+"x"+(f+c)};_wt.prototype.N=function(a){var k="";var h="";var f=["div","table"];var d=f.length;var c,j,b;for(c=0;c<d;c++){b=f[c];if(b.length){j=this.E(a,b);k=(j.getAttribute&&j.getAttribute("id"))?j.getAttribute("id"):"";h=j.className||"";if(k.length||h.length){break}}}return k.length?k:h};Function.prototype.wtbind=function(c){var b=this;var a=function(){return b.apply(c,arguments)};return a};_wt.prototype.dcsMultiTrack=function(){Webtrends.multiTrack({argsa:arguments,delayTime:100})};_tag=new _wt()}var l=void 0;(function(c,h,b,a){function u(d){if(d){if(!d.forEach){d.forEach=function(f,n){for(var m=n||window,k=0,j=this.length;k<j;++k){f.call(m,this[k],k,this)}}}if(!d.filter){d.filter=function(j,p){for(var o=p||window,n=[],k=0,m=this.length;k<m;++k){j.call(o,this[k],k,this)&&n.push(this[k])}return n}}if(!d.indexOf){d.indexOf=function(f){for(var j=0;j<this.length;++j){if(this[j]===f){return j}}return -1}}}return d}if(!c.Ra){var t={f:{},xa:0,e:{},addEventListener:c.addEventListener?function(f,d,j){f.addEventListener&&f.addEventListener(d,j,!1)}:function(f,d,j){f.attachEvent&&f.attachEvent("on"+d,j,!1)},g:{},version:"10.4.21",j:{},Fa:!1,m:25,H:function(){if(a.search){t.j=t.S(a.search)}if(c.webtrendsAsyncInit&&!c.webtrendsAsyncInit.hasRun){c.webtrendsAsyncInit(),c.webtrendsAsyncInit.hasRun=!0}t.addEventListener(c,"load",function(){t.Fa=!0})},o:function(d){return Object.prototype.toString.call(d)==="[object Function]"},Ia:function(f){var d=[],j;for(j in f){f.hasOwnProperty(j)&&f[j]!=""&&f[j]!=l&&typeof f[j]!="function"&&d.push({k:j,v:f[j]})}return d},extend:function(f,d,j){for(key in d){if(j||typeof f[key]==="undefined"){f[key]=d[key]}}return f},find:function(d){if(!t.Y){t.Y=t.Da()}return t.Y(d)},Da:function(){var f=/MSIE (\d+)/.exec(b.userAgent),f=f?f[1]:99;if(h.querySelectorAll&&h.body&&f>8){var d=h.body;return function(j){return d.querySelectorAll(j)}}if(c.jQuery){return c.jQuery.find}if(c.Sizzle){return c.Sizzle}if(c.YAHOO&&YAHOO.aa&&YAHOO.aa.ba){return YAHOO.aa.ba.bb}if("qwery" in c){return qwery}c.YUI&&YUI().eb("node",function(j){return j.all});return h.querySelectorAll?(d=h.body)?function(j){return d.querySelectorAll(j)}:function(){return[]}:function(){return[]}},S:function(k){var k=k.split(/[&?]/g),j={};try{for(var v=0,p=k.length;v<p;++v){var o=k[v].match(/^([^=]+)(?:=([\s\S]*))?/);if(o&&o[1]){var m=decodeURIComponent(o[1]);j[m]?(j[m]=[j[m]],j[m].push(decodeURIComponent(o[2]))):j[m]=decodeURIComponent(o[2])}}}catch(n){this.D.push(n),this.B(n)}return j},Ga:function(f,d,j){arguments.length<2&&(d=!0);s=h.createElement("script");s.type="text/javascript";s.async=d;s.src=f;s2=h.getElementsByTagName("script")[0];s2.parentNode.insertBefore(s,s2)},O:function(f,d){var j=f.target||f.srcElement;for(typeof d=="string"&&(d[d]=1);j&&j.tagName&&!d[j.tagName.toUpperCase()];){j=j.parentElement||j.parentNode}return j},K:function(d){return typeof encodeURIComponent=="function"?encodeURIComponent(d):escape(d)},Ha:function(f){for(var d in t.f){t.f[d].L(f)}return !1},s:function(f,d,j){d||(d="collect");j?t.t("transform."+d,f,j):t.t("transform."+d,f);return this},t:function(f,d,m){function j(n,o){t.g[f][n.i]||(t.g[f][n.i]=u([]));t.g[f][n.i].push(o)}if(f&&d&&f!=""&&t.o(d)){f==="wtmouseup"&&(f="wtmouse");if(f==="wtmouse"&&!t.U){var k=/MSIE (\d+)/.exec(b.userAgent);t.addEventListener(h,(k?k[1]:99)>=8?"mousedown":"mouseup",function(n){if(!n){n=window.event}t.ia(f,{event:n})});t.U=!0}t.g[f]||(t.g[f]={});if(m){j(m,d)}else{for(dcsid in t.f){j(t.f[dcsid],d)}}}},ia:function(f,d){for(dcsid in t.f){t.fireEvent(f,t.f[dcsid],d)}},ca:function(j,f,m,k){if(typeof f==="function"){return f.onetime?(m.push(f),!0):(f(j,k),!1)}},fireEvent:function(f,d,m){var j=u([]);if(t.g[f]&&t.g[f][d.i]){f=t.g[f][d.i];if(!f.length){return}for(var k=f.length-1;k>=0;k--){t.ca(d,f[k],j,m)&&f.pop()}}j.forEach(function(n){n(d,m)})}},r=t.fireEvent,q=t.t;t.b=function(){this.Q=c.RegExp?/dcs(uri)|(ref)|(aut)|(met)|(sta)|(sip)|(pro)|(byt)|(dat)|(p3p)|(cfg)|(redirect)|(cip)/i:"";this.X={};this.d=this.WT={};this.h=this.DCS={};this.l=this.DCSext={};this.i=this.dcssID="dcsobj_"+t.xa++;this.images=this.images=[];this.D=this.errors=[];this.a=this.FPCConfig={};this.c=this.TPCConfig={};this.Xa={};this.images=[];this.ab=[];this.Sa=[];this.Qa=!1;this.F=this.R="";this.U=!1;return this};t.b.prototype={H:function(f){function d(k,m){return f.hasOwnProperty(k)?f[k]:m}function j(m,k,n){return !m?n:m.hasOwnProperty(k)?m[k]:n}this.Ua=f;this.B=d("errorlogger",function(){});this.wa=this.dcsid=f.dcsid;this.q=this.queue=d("queue",[]);this.domain=this.domain=d("domain",".mysite.cn");this.Oa=this.timezone=d("timezone",-8);this.enabled=this.enabled=d("enabled",!0);this.G=this.i18n=d("i18n",!0);this.X=c.RegExp?this.G?{"%25":/\%/g,"%26":/\&/g,"%23":/\#/g}:{"%09":/\t/g,"%20":/ /g,"%23":/\#/g,"%26":/\&/g,"%2B":/\+/g,"%3F":/\?/g,"%5C":/\\/g,"%22":/\"/g,"%7F":/\x7F/g,"%A0":/\xA0/g}:"";if(f.metanames){this.T=u(f.metanames.toLowerCase().split(","))}this.r=this.vtid=d("vtid",l);this.V=d("paidsearchparams","gclid");this.Na=this.splitvalue=d("splitvalue","");t.m=f.dcsdelay||t.m;this.ya=this.delayAll=d("delayAll",!1);this.W=this.preserve=d("preserve",!0);this.a.enabled=this.FPCConfig.enabled=j(f.FPCConfig,"enabled",!0);this.a.domain=this.FPCConfig.domain=j(f.FPCConfig,"domain",d("fpcdom",""));this.a.name=this.FPCConfig.name=j(f.FPCConfig,"name",d("fpc","WT_FPC"));this.a.n=this.FPCConfig.expiry=j(f.FPCConfig,"expires",d("cookieexpires",63113851500));this.a.n=this.a.n<63113851500?this.a.n:63113851500;this.a.Aa=new Date(this.getTime()+this.a.n);this.a.Ma=this.a.n===0;this.c.enabled=this.TPCConfig.enabled=j(f.TPCConfig,"enabled",!d("disablecookie",!0));this.c.u=this.TPCConfig.cfgType=j(f.TPCConfig,"cfgType",!this.c.enabled?"1":"");if(f.cookieTypes){if(f.cookieTypes.toLowerCase()==="none"){this.a.enabled=!1,this.c.enabled=!1,this.c.u="1"}else{if(f.cookieTypes.toLowerCase()==="firstpartyonly"){this.a.enabled=!0,this.c.enabled=!1,this.c.u="1"}else{if(f.cookieTypes.toLowerCase()==="all"){this.a.enabled=!0,this.c.enabled=!0,this.c.u=j(f.TPCConfig,"cfgType","")}}}}this.Ya=this.fpc=this.a.name;this.Za=this.fpcdom=this.a.domain;this.Wa=this.cookieExp=this.a.n;d("privateFlag",!1)||(t.f[this.i]=this);t.e[this.domain]||(t.e[this.domain]="");!d("privateFlag",!1)&&this.a.enabled?this.oa(this.i):this.I();return this},pa:function(d){if(typeof d!="undefined"){!t.e[this.domain]&&d.gTempWtId&&(t.e[this.domain]=d.gTempWtId),this.F=d.gTempWtId,!t.e[this.domain]&&d.gWtId&&(t.e[this.domain]=d.gWtId),this.R=d.gWtAccountRollup}this.I()},oa:function(d){if(h.cookie.indexOf(this.a.name+"=")==-1&&h.cookie.indexOf("WTLOPTOUT=")==-1&&this.c.enabled){return this.enabled&&t.Ga("//"+this.domain+"/"+this.wa+"/wtid.js?callback=Webtrends.dcss."+d+".dcsGetIdCallback",!0),!1}this.I();return !0},I:function(){if(!this.da){r("onready",this),this.Ea(),this.La(),this.da=!0}},Ea:function(){for(var d=0;d<this.q.length;d++){this.N(this.q[d])}this.q=[]},La:function(){var d=this;this.q.push=function(f){d.N(f)}},s:function(f,d){t.s(f,d,this)},ga:function(f,d){var k=this,j=t.extend({domEvent:"click",callback:l,argsa:[],args:{},delayTime:l,transform:l,filter:l,actionElems:{A:1,INPUT:1,BUTTON:1},finish:l},d,!0);q("wtmouse",function(m,n){k.ha(k,f,t.extend(n,j,!0))},k);return this},Z:function(j,f,m,k){f.element=m;if(k==="form"||k==="input"||k==="button"){f.domEvent="submit"}j.J(f)},ha:function(f,d,n){var j=t.find;if(j&&n.event&&n.actionElems){var m=t.O(n.event,n.actionElems),k=m.tagName?m.tagName.toLowerCase():"";if(d.toUpperCase() in n.actionElems&&n.actionElems[k.toUpperCase()]){return this.Z(f,n,m,k)}if((d=j(d))&&m&&d&&d.length){for(j=0;j<d.length;j++){if(d[j]===m){this.Z(f,n,m,k);break}}}}},C:function(f,d){var j=u(h.cookie.split("; ")).filter(function(k){return k.indexOf(f+"=")!=-1})[0];if(!j||j.length<f.length+1){return !1}u(j.split(f+"=")[1].split(":")).forEach(function(k){k=k.split("=");d[k[0]]=k[1]});return !0},ta:function(f,d,k){var j=[],d=t.Ia(d);u(d).forEach(function(m){j.push(m.k+"="+m.v)});j=j.sort().join(":");h.cookie=f+"="+j+k},qa:function(j,f,n,m){var k={};return this.C(j,k)?f==k.id&&n==k.lv&&m==k.ss?0:3:2},na:function(){var d={};this.C(this.a.name,d);return d},ma:function(){if(h.cookie.indexOf("WTLOPTOUT=")==-1){if(this.a.enabled){var x=this.d,w=this.a.name,v=new Date,n=v.getTimezoneOffset()*60000+this.Oa*3600000;v.setTime(v.getTime()+n);var p=new Date(v.getTime());x.co_f=x.vtid=x.vtvs=x.vt_f=x.vt_f_a=x.vt_f_s=x.vt_f_d=x.vt_f_tlh=x.vt_f_tlv="";var o={};if(this.C(w,o)){var m=o.id,f=parseInt(o.lv),d=parseInt(o.ss);if(m==null||m=="null"||isNaN(f)||isNaN(d)){return}x.co_f=m;m=new Date(f);x.vt_f_tlh=Math.floor((m.getTime()-n)/1000);p.setTime(d);if(v.getTime()>m.getTime()+1800000||v.getTime()>p.getTime()+28800000){x.vt_f_tlv=Math.floor((p.getTime()-n)/1000),p.setTime(v.getTime()),x.vt_f_s="1"}if(v.getDate()!=m.getDate()||v.getMonth()!=m.getMonth()||v.getFullYear()!=m.getFullYear()){x.vt_f_d="1"}}else{if(this.F.length){x.co_f=t.e[this.domain].length?t.e[this.domain]:this.F,x.vt_f="1"}else{if(t.e[this.domain].length){x.co_f=t.e[this.domain]}else{x.co_f="2";d=v.getTime().toString();for(m=2;m<=32-d.length;m++){x.co_f+=Math.floor(Math.random()*16).toString(16)}x.co_f+=d;x.vt_f="1"}}this.R.length==0&&(x.vt_f_a="1");x.vt_f_s=x.vt_f_d="1";x.vt_f_tlh=x.vt_f_tlv="0"}x.co_f=escape(x.co_f);x.vtid=typeof this.r=="undefined"?x.co_f:this.r||"";x.vtvs=(p.getTime()-n).toString();n=(this.a.Ma?"":"; expires="+this.a.Aa.toGMTString())+"; path=/"+(this.a.domain!=""?"; domain="+this.a.domain:"");v=v.getTime().toString();p=p.getTime().toString();o.id=x.co_f;o.lv=v;o.ss=p;this.ta(w,o,n);w=this.qa(w,x.co_f,v,p);if(w!=0){x.co_f=x.vtvs=x.vt_f_s=x.vt_f_d=x.vt_f_tlh=x.vt_f_tlv="",typeof this.r=="undefined"&&(x.vtid=""),x.vt_f=x.vt_f_a=w}}else{this.d.vt_f="4",this.d.vtid=this.r?this.r:""}}},Pa:function(){try{var f;arguments&&arguments.length>1?f={argsa:Array.prototype.slice.call(arguments)}:arguments.length===1&&(f=arguments[0]);typeof f==="undefined"&&(f={element:l,event:l,Ta:[]});typeof f.argsa==="undefined"&&(f.argsa=[]);this.P("collect",f);return this}catch(d){this.D.push(d),this.B(d)}},L:function(d){d&&d.length>1&&(d={argsa:Array.prototype.slice.call(arguments)});this.J(d)},J:function(d){try{typeof d==="undefined"&&(d={});this.P("multitrack",d);return !1}catch(f){this.D.push(f),this.B(f)}},ja:function(){this.h={};this.d={};this.l={};arguments.length%2==0&&this.z(arguments)},z:function(f){if(f){for(var d=0,j=f.length;d<j;d+=2){f[d].indexOf("WT.")==0?this.d[f[d].substring(3)]=f[d+1]:f[d].indexOf("DCS.")==0?this.h[f[d].substring(4)]=f[d+1]:f[d].indexOf("DCSext.")==0&&(this.l[f[d].substring(7)]=f[d+1])}}},ua:function(j){var f,n;if(this.W){this.p=[];for(var m=0,k=j.length;m<k;m+=2){n=j[m],n.indexOf("WT.")==0?(f=n.substring(3),this.p.push(n,this.d[f]||"")):n.indexOf("DCS.")==0?(f=n.substring(4),this.p.push(n,this.h[f]||"")):n.indexOf("DCSext.")==0&&(f=n.substring(7),this.p.push(n,this.l[f]||""))}}},sa:function(){if(this.W){this.z(this.p),this.p=[]}},va:function(){var f=new Date,d=this.d,n=this.h;d.tz=parseInt(f.getTimezoneOffset()/60*-1)||"0";d.bh=f.getHours()||"0";d.ul=b.appName=="Netscape"?b.language:b.fb;if(typeof screen=="object"){d.cd=b.appName=="Netscape"?screen.pixelDepth:screen.colorDepth,d.sr=screen.width+"x"+screen.height}typeof b.javaEnabled()=="boolean"&&(d.jo=b.javaEnabled()?"Yes":"No");h.title&&(d.ti=c.RegExp?h.title.replace(RegExp("^"+a.protocol+"//"+a.hostname+"\\s-\\s"),""):h.title);d.js="Yes";d.ct="unknown";if(h.body&&h.body.addBehavior){try{h.body.addBehavior("#default#clientCaps"),d.ct=h.body.Va||"unknown",h.body.addBehavior("#default#homePage"),d.hp=h.body.$a(location.href)?"1":"0"}catch(j){this.B(j)}}var m=0,k=0;if(typeof c.innerWidth=="number"){m=c.innerWidth,k=c.innerHeight}else{if(h.documentElement&&(h.documentElement.clientWidth||h.documentElement.clientHeight)){m=h.documentElement.clientWidth,k=h.documentElement.clientHeight}else{if(h.body&&(h.body.clientWidth||h.body.clientHeight)){m=h.body.clientWidth,k=h.body.clientHeight}}}d.bs=m+"x"+k;this.G&&(d.le=typeof h.defaultCharset=="string"?h.defaultCharset:typeof h.characterSet=="string"?h.characterSet:"unknown");d.tv=t.version;d.sp=this.Na;d.dl="0";if(t.j&&t.j.Ba){d.fb_ref=t.j.Ba}if(t.j&&t.j.Ca){d.fb_source=t.j.Ca}d.ssl=a.protocol.indexOf("https:")==0?"1":"0";n.dcsdat=f.getTime();n.dcssip=a.hostname;n.dcsuri=a.pathname;d.es=a.href;if(a.search){n.dcsqry=a.search}if(n.dcsqry){f=n.dcsqry.toLowerCase();m=this.V.length?this.V.toLowerCase().split(","):[];for(k=0;k<m.length;k++){if(f.indexOf(m[k]+"=")!=-1){d.srch="1";break}}}if(h.referrer!=""&&h.referrer!="-"&&!(b.appName=="Microsoft Internet Explorer"&&parseInt(b.appVersion)<4)){n.dcsref=h.referrer}n.dcscfg=this.c.u},la:function(f,d){if(d!=""){if(f===null||f===l){return""}var f=f.toString(),j;for(j in d){d[j] instanceof RegExp&&(f=f.replace(d[j],j))}return f}else{return escape(f)}},w:function(k,d){if(this.G&&this.Q!=""&&!this.Q.test(k)){if(k=="dcsqry"){for(var w="",n=d.substring(1).split("&"),v=0;v<n.length;v++){var o=n[v],p=o.indexOf("=");if(p!=-1){var m=o.substring(0,p),o=o.substring(p+1);v!=0&&(w+="&");w+=m+"="+t.K(o)}}d=d.substring(0,1)+w}else{d=t.K(d)}}return"&"+k+"="+this.la(d,this.X)},ka:function(f,d){if(h.images){var k=new Image;this.images.push(k);if(true){var j=!1;if(t.o(d.callback)){e=d.callback}else{e=function(n,m){}}g=this;k.onload=function(){if(!j){return j=!0,e(g,d),!0}};se=c.setTimeout(function(){if(!j){k.removeAttribute("src");return j=!0,e(g,d),!0}},t.m)}k.onload=function(){if(!j){clearTimeout(se);return j=!0,e(g,d),!0}};k.src=f}},ra:function(){var j;h.documentElement?j=h.getElementsByTagName("meta"):h.all&&(j=h.all.cb("meta"));if(typeof j!="undefined"){for(var f=j.length,n=0;n<f;n++){var m=j.item(n).name,k=j.item(n).content;j.item(n);m.length>0&&(m=m.toLowerCase(),
(m.toUpperCase().indexOf("WT.")==0&&((m.indexOf("mobile")>-1&&(is_mobile(k)||is_mobile(decode_mobile(k))))||m.indexOf("mobile")<-1))?this.d[m.substring(3)]=k:m.toUpperCase().indexOf("DCSEXT.")==0?this.l[m.substring(7)]=k:m.toUpperCase().indexOf("DCS.")==0?this.h[m.substring(4)]=k:this.T&&this.T.indexOf(m)!=-1&&(this.l["meta_"+m]=k))}}},M:function(j){if(h.cookie.indexOf("WTLOPTOUT=")==-1){var f=this.d,p=this.h,o=this.l,n=this.i18n,m="http"+(a.protocol.indexOf("https:")==0?"s":"")+"://"+this.domain+(this.dcsid==""?"":"/"+this.dcsid)+"/dcs.gif?";n&&(f.dep="");for(var k in p){p[k]!=""&&p[k]!=l&&typeof p[k]!="function"&&(m+=this.w(k,p[k]))}for(k in f){f[k]!=""&&f[k]!=l&&typeof f[k]!="function"&&(m+=this.w("WT."+k,f[k]))}for(k in o){if(o[k]!=""&&o[k]!=l&&typeof o[k]!="function"){n&&(f.dep=f.dep.length==0?k:f.dep+";"+k),m+=this.w(k,o[k])}}n&&f.dep.length>0&&(m+=this.w("WT.dep",f.dep));m.length>2048&&b.userAgent.indexOf("MSIE")>=0&&(m=m.substring(0,2040)+"&WT.tu=1");
var tmp=document.cookie.replace(/(?:(?:^|.*;\s*)WT.rh_user\s*\=\s*([^;]*).*$)|^.*$/,"$1").split(":")[0].split("=")[1];if(m.indexOf("WT.mobile")>-1){var tmpold=m.split("WT.mobile=")[1].split("&")[0];if(tmp && tmpold!=tmp){m=m.replace("WT.mobile="+tmpold,"WT.mobile="+tmp)}}else if(tmp){m+="&WT.mobile="+tmp};var tmp=document.cookie.replace(/(?:(?:^|.*;\s*)WT.rh_login\s*\=\s*([^;]*).*$)|^.*$/,"$1").split("=")[1];if(m.indexOf("WT.logintype")>-1){var tmpold=m.split("WT.logintype=")[1].split("&")[0];if(tmp && tmpold!=tmp){m=m.replace("WT.logintype="+tmpold,"WT.logintype="+tmp)}}else if(tmp){m+="&WT.logintype="+tmp};this.ka(m,j);this.d.ad=""}},Ja:function(){this.va();this.ra();this.Ka=!0},getTime:function(){return(new Date).getTime()},za:0,$:function(f){for(var d=this.getTime();this.getTime()-d<f;){this.za++}},P:function(f,d){f||(f="collect");this.q.push({action:f,message:d})},N:function(j){if(this.enabled){var d="action_"+j.action,m=j.message;this.Ka||this.Ja();m.event&&!m.element&&(m.element=t.O(m.event,{A:1}));if(!t.o(m.filter)||!m.filter(this,m)){if(m.args){m.argsa=m.argsa||[];for(var k in m.args){m.argsa.push(k,m.args[k])}}m.element&&m.element.getAttribute&&m.element.getAttribute("data-wtmt")&&(m.argsa=m.argsa.concat(m.element.getAttribute("data-wtmt").split(",")));r("transform."+j.action,this,m);r("transform.all",this,m);m.transform&&t.o(m.transform)&&m.transform(this,m);this.ma();if(t.o(this[d])){this[d](m)}r("finish."+j.action,this,m);r("finish.all",this,m);m.finish&&t.o(m.finish)&&m.finish(this,m)}}},fa:function(f){var d=f&&f.argsa&&f.argsa.length%2==0;d&&(this.ua(f.argsa),this.z(f.argsa));this.h.dcsdat=this.getTime();this.M(f);d&&this.sa()},ea:function(d){d&&d.argsa&&d.argsa.length%2==0&&this.z(d.argsa);this.M(d)}};t.b.prototype.action_multitrack=t.b.prototype.fa;t.b.prototype.action_collect=t.b.prototype.ea;c.Webtrends=t;c.WebTrends=t;c.WT=c.Webtrends;t.multiTrack=t.Ha;t.dcs=t.b;t.dcss=t.f;t.addTransform=t.s;t.bindEvent=t.t;t.getQryParams=t.S;t.dcsdelay=t.m;t.find=t.find;t.b.prototype.init=t.b.prototype.H;t.b.prototype.dcsMultiTrack=t.b.prototype.L;t.b.prototype.track=t.b.prototype.Pa;t.b.prototype.addSelector=t.b.prototype.ga;t.b.prototype.dcsGetIdCallback=t.b.prototype.pa;t.b.prototype.dcsCleanUp=t.b.prototype.ja;t.b.prototype.dcsGetFPC=t.b.prototype.na;t.b.prototype.addTransform=t.b.prototype.s;t.H()}})(window,window.document,window.navigator,window.location);