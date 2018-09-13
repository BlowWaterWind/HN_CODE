/**
 * 加载层
 */
function showLoadPop(str) {
    if(str==undefined || str==''){
       str= "数据加载中，请稍后... ...";
    }
    var  html='<div id="load" class="loading loading-sub"><div class="loading-logo"></div>'+
        '<div class="loader"></div><div id="reload" style="margin-top:-10px;">'+str+'</div></div>';
    $("body").append(html);
    $("#load").show();
    $("#reload").show();
}
/**
 * 隐藏加载层
 */
function hideLoadPop() {
    $("#load").remove();
}