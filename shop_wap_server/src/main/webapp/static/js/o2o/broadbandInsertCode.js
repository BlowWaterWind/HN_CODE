/**
 * Created by 罗松 on 2018/8/24.
 */
/**
 * 获取url中的参数
 * @param name
 * @returns {null}
 */
function getQueryString(name) {
    try{
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        /*search是一个可以查询的属性，可以查询？之后的部分*/
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
    }catch (e){
        console.log(e);
    }
}
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
$(function(){
    try{
        var mobie = document.getElementsByTagName('meta')['WT.mobile'].content;
        var si_x = document.getElementsByTagName('meta')['WT.si_x'].content;
        if(mobie == undefined || mobie == null || mobie == ""){
            if(si_x == 1 || si_x == 20){
                document.getElementsByTagName('meta')['WT.si_x'].content = 1;
            }
        }else{
            if(si_x == 1 || si_x == 20){
                document.getElementsByTagName('meta')['WT.si_x'].content = 20;
            }
        }
    }catch (e){
        console.log(e);
    }
});
/*判读是否为浏览页面，不是则给WT.ac赋值*/
$(function(){
    try{
        var si_x = document.getElementsByTagName('meta')['WT.si_x'].content;
        if(si_x != undefined && si_x != 1 && si_x != 20){
            /**
             * 获取cookie中的参数
             */
            var wtAc = $.cookie("WT.ac");
            if($.cookie("WT.ac")!=undefined) {
                /*如果是浏览页面，则给WT.ac赋值*/
                document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
            }
        }else{
            /*如果不是游览页面，那么直接放cookie中*/
            getToCookie();
        }
    }catch (e){
        console.log(e);
    }
});

/*给新和家庭办理，代扣预存款的WT.si.n赋值*/
function setO2oNewHeBandMetaValue(skuId,goodsId){
    try{
        if(skuId == undefined || skuId == ""){
            document.getElementsByTagName('meta')['WT.si_n'].content = "XHJTDKYCK_"+goodsId;
        }else if(goodsId == undefined || goodsId == ""){
            document.getElementsByTagName('meta')['WT.si_n'].content = "XHJTDKYCK_"+skuId;
        }else{
            document.getElementsByTagName('meta')['WT.si_n'].content = "XHJTDKYCK_"+skuId+"_"+goodsId;
        }

    }catch(e) {
        console.log(e);
    }
}

/*给魔百和办理，代扣预存款的WT.si.n赋值*/
function setO2oMbhMetaValue(){
    try{
        var goodsSkuId = $("#goodsSkuId").val();
        if(goodsSkuId != "" && goodsSkuId != undefined){
            document.getElementsByTagName('meta')['WT.si_n'].content = "MBHDKYCK_"+goodsSkuId;
        }else{
            document.getElementsByTagName('meta')['WT.si_n'].content = "MBHDKYCK";
        }
    }catch (e){
        console.log(e);
    }
}

/*给组网送宽带，代扣预存款的WT.si.n赋值*/
function setO2oConsupostnMetaValue(){
    try{
        var goodsSkuId = $("#goodsSkuId").val();
        if(goodsSkuId != "" && goodsSkuId != undefined){
            document.getElementsByTagName('meta')['WT.si_n'].content = "ZWSKDDKYCK_"+goodsSkuId;
        }else{
            document.getElementsByTagName('meta')['WT.si_n'].content = "ZWSKDDKYCK";
        }
    }catch (e){
        console.log(e);
    }
}