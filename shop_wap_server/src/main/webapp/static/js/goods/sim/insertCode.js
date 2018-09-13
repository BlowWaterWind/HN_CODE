/**
 * Created by tongcheng on 2018/8/13.
 */


/*赋值WT.mobile、WT.brand meta标签*/
$(function(){
    try{
        var mob = $.cookie("mob");
        var brand = $.cookie("brand");
        if(mob != undefined){
            document.getElementsByTagName('meta')['WT.mobile'].content = mob;
        }
        if(brand != undefined){
            document.getElementsByTagName('meta')['WT.brand'].content = brand;
        }

    }catch (e){
        console.log(e);
    }
});
/*判读是否登录给WT.si_x meta标签赋值*/
/*$(function(){
    try{
        var mobie = document.getElementsByTagName('meta')['WT.mobile'].content;
        var si_x = document.getElementsByTagName('meta')['WT.si_x'].content;
        if(mobie == "undefined" || mobie == null || mobie == ""){
            if(si_x == "1" || si_x == 20){
                document.getElementsByTagName('meta')['WT.si_x'].content = 1;
            }
        }else{
            if(si_x == "1" || si_x == 20){
                document.getElementsByTagName('meta')['WT.si_x'].content = 20;
            }
        }
    }catch (e){
        console.log(e);
    }
});*/
/*大王卡浏览页面*/
function quote(confId,planId){
    try{
        document.getElementsByTagName('meta')['WT.si_n'].content = "HK_"+confId;
        document.getElementsByTagName('meta')['WT.si_s'].content = planId;
    }catch (e){
        console.log(e);
    }
}

/**
 * 获取url中的参数
 * @param name
 * @returns {null}
 */
function getQueryString(name) {
    try{
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return "";
    }catch (e){
        console.log(e);
    }
}
/*拿取url中的WT.at存入cookie*/
function getToCookie(){
    try{
        $.cookie("WT.ac", getQueryString("WT.ac"));
        /**
         * 获取cookie中的参数
         */
        var wtAc = $.cookie("WT.ac");
        if ($.cookie("WT.ac") != undefined) {
            document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
        }
    }catch (e){
        console.log(e);
    }
}

/*给页面WT.si_n 赋初始值 模板1（大王卡）*/
function getConfId(confId,planId){
    try{
        document.getElementsByTagName('meta')['WT.si_n'].content = "HK_"+confId;
        document.getElementsByTagName('meta')['WT.si_s'].content = planId;
    }catch (e){
        console.log(e);
    }
}

/*给页面WT.si_n 赋初始值  模板2(爱奇艺卡)*/
function getConfId1(obj,confid){
    try{
        var planId = $(obj).find('input[name="planId"]').val()
        document.getElementsByTagName('meta')['WT.si_n'].content = "HK_"+confid;
        document.getElementsByTagName('meta')['WT.si_s'].content = planId;
    }catch (e){
        console.log(e);
    }
}


/*给页面WT.si_n 赋初始值 二级模板*/
function getConfId2(confid,planId){
    try{
        document.getElementsByTagName('meta')['WT.si_n'].content = "HK_"+confid;
        document.getElementsByTagName('meta')['WT.si_s'].content = planId;
    }catch (e){
        console.log(e);
    }
}

/*给页面WT.si_n 赋初始值 模板 页面只有confid*/
function getConfId3(confid){
    try{
        document.getElementsByTagName('meta')['WT.si_n'].content = "HK_"+confid;
    }catch (e){
        console.log(e);
    }
}

function ajaxSuccess(){
    var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
    var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
    var wtx = document.getElementsByTagName('meta')['WT.si_x'].content;
    try {
        if (window.Webtrends) {
            Webtrends.multiTrack({
                argsa: ["WT.si_n", wtc,
                    "WT.si_s", wts,
                    wtx, "99"],
                delayTime: 100
            })
        } else {
            if (typeof(dcsPageTrack) == "function") {
                dcsPageTrack("WT.si_n", wtc, true,
                    wtx, "99", true);
            }
        }

    } catch (e) {
        console.log(e)
    }
}

function ajaxFail() {
    var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
    var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
    var wtx = document.getElementsByTagName('meta')['WT.si_x'].content;
    try {
        if (window.Webtrends) {
            Webtrends.multiTrack({
                argsa: ["WT.si_n", wtc,
                    "WT.si_s", wts,
                    wtx, "-99"],
                delayTime: 100
            })
        } else {
            if (typeof(dcsPageTrack) == "function") {
                dcsPageTrack("WT.si_n", wtc, true,
                    wtx, "-99", true);
            }
        }
    } catch (e) {
        console.log(e)
    }
}


/*追踪码*/
function  trace(step){
    try{
        var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
        var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
        Webtrends.multiTrack({
            argsa: ["WT.si_n",sin,
                "WT.si_s",wts,
                "WT.si_x",step],
            delayTime: 100
        })
    }catch (e){
        console.log(e);
    }
}