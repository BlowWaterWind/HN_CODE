/**
 * Created by tongcheng on 2018/8/20.
 */
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
               document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
           }
       }else{
           /*如果是浏览页面，则给WT.ac赋值*/
           getToCookie();
       }
   }catch (e){
       console.log(e);
   }
});
/*为WT.si_n赋值goodsId和skuId*/
function getGoodsId(skuId,goodsId){
    try{
        if(skuId == undefined || skuId == ""){
            document.getElementsByTagName('meta')['WT.si_n'].content = "XHJT_"+goodsId;
        }else if(goodsId == undefined || goodsId == ""){
            document.getElementsByTagName('meta')['WT.si_n'].content = "XHJT_"+skuId;
        }else{
            document.getElementsByTagName('meta')['WT.si_n'].content = "XHJT_"+skuId+"_"+goodsId;
        }

    }catch(e) {
        console.log(e);
    }
}

/*为WT.si_n赋值(魔百和)*/
function getGoodsId1(){
    try{
        var goodsSkuId = $("#goodsSkuId").val();
        if(goodsSkuId != "" && goodsSkuId != undefined){
            document.getElementsByTagName('meta')['WT.si_n'].content = "KDMBHBL_"+goodsSkuId;
        }else{
            document.getElementsByTagName('meta')['WT.si_n'].content = "KDMBHBL";
        }
    }catch (e){
        console.log(e);
    }

}



/*为WT.si_n赋值(组网松宽带)*/
function getGoodsId2(){
    try{
        var goodsSkuId = $("#goodsSkuId").val();
        if(goodsSkuId != "" && goodsSkuId != undefined){
            document.getElementsByTagName('meta')['WT.si_n'].content = "KDZW_"+goodsSkuId;
        }else{
            document.getElementsByTagName('meta')['WT.si_n'].content = "KDZW";
        }
    }catch (e){
        console.log(e);
    }

}

/*为WT.si_n赋值(宽带叠加)*/
function getGoodsId3(){
    try{
        var goodsSkuId = $("#goodsSkuId").val();
        if(goodsSkuId != "" && goodsSkuId != undefined){
            document.getElementsByTagName('meta')['WT.si_n'].content = "KDDJ_"+goodsSkuId;
        }else{
            document.getElementsByTagName('meta')['WT.si_n'].content = "KDDJ";
        }
    }catch (e){
        console.log(e);
    }

}