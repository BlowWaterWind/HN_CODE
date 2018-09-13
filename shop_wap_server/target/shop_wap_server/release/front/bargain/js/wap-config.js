//渲染模板
function renderData(data,artId,contentId){
	if(artId && contentId){
		var html = template(artId, data);
		$("#"+contentId).html(html);
	}
}

//显示层
function shareBoxShow(popboxId)
{
	var popbox = document.getElementById(popboxId);
	if(popbox&&popbox.style.display!="block"){
//		shareMaskShow();
		popbox.style.display = "block";
	}
}
//隐藏层
function shareBoxHide(popboxId)
{
	var popbox = document.getElementById(popboxId);
//	shareMaskHide();
	if(popbox)
	popbox.style.display="none";
}

//隐藏遮罩
function shareMaskHide()
{
 // var mask = document.getElementById("shareto_mask");
 // if(mask){
//	  document.body.removeChild(mask);
  //}
}
//显示遮罩
function shareMaskShow()
{	
	//var mask = document.createElement("div");
    //mask.className="shareto_mask";
    //mask.id="shareto_mask";
    //mask.setAttribute("style","display:block;background:none repeat scroll 0 0 #666;height:100%;left:0;opacity:0.5;filter:alpha(opacity=50);position:fixed;top:0;width:100%;z-index:99");
    //document.body.appendChild(mask);
}


//是否支持sessionStorage，解决Safari浏览器无痕浏览模式下报错问题，以及暂未发现的其他浏览器可能存在的此类问题。
var isSupportSessionStorage = (function () {
	try {
	       sessionStorage.setItem("test", "test");
	       sessionStorage.removeItem("test");
	       return true;
	} catch (e) {
	       return false;
	}
})();

$(document).ready(function(){
	//对于每一条内容：
	 $('.detail').each(function(){
	  //内容
	  var html=$(this).html();
	  //查看完整内容
	  $(this).html('<span class="short">'+html.substring(0,20)+'...</span><span class="all">'+html+'</span><a class="more">(更多)</a>');
	 });
	 //查看完整内容的超链接：
	 $('.detail .more').each(function(){
	  //如果点击该链接则：
	  $(this).click(function(){
	   //把不需要显示的内容隐藏，需要显示的内容展开。
	   $(this).parent().children('.all,.short').toggle();
	   //替换超链接的文字
	   if($(this).html()=='(更多)')$(this).html('(收起)');
	   else $(this).html('(更多)');
	  });
	 });
});

function loadjscssfile(filename,filetype){

    if(filetype == "js"){
        var fileref = document.createElement('script');
        fileref.setAttribute("type","text/javascript");
        fileref.setAttribute("src",filename);
    }else if(filetype == "css"){
    
        var fileref = document.createElement('link');
        fileref.setAttribute("rel","stylesheet");
        fileref.setAttribute("type","text/css");
        fileref.setAttribute("href",filename);
    }
   if(typeof fileref != "undefined"){
        document.getElementsByTagName("head")[0].appendChild(fileref);
    }
    
}

function removejscssfile(filename, filetype){ 
	var targetelement=(filetype=="js")? "script" : (filetype=="css")? "link" : "none"; 
	var targetattr=(filetype=="js")? "src" : (filetype=="css")? "href" : "none"; 
	var allsuspects=document.getElementsByTagName(targetelement); 
	for (var i=allsuspects.length; i>=0; i--){ 
		if (allsuspects[i] && allsuspects[i].getAttribute(targetattr)!=null && allsuspects[i].getAttribute(targetattr).indexOf(filename)!=-1){ 
			allsuspects[i].parentNode.removeChild(allsuspects[i]); 
		}
	} 
}

// 图片延迟加载
/*$(function() {
    $("img.lazy").show().lazyload();
    $("img.lazy").lazyload({ 
        effect: "fadeIn"
    });
});*/

//获取URL中的传参
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null) return r[2]; return null;
}

//根据元素值判空
function valIsNotEmpty(obj)
{
	if(null != obj && '' != obj && undefined != obj && typeof(obj) != "undefined")
	{
		if('' != $.trim(obj))
			return true;
		else 
			return false;
	}
	else{
		return false;
	}
}
//根据元素值判空
function valIsEmpty(obj)
{
	if(null != obj && '' != obj && undefined != obj && typeof(obj) != "undefined")
	{
		if('' != $.trim(obj))
			return false;
		else 
			return true;
	}
	else{
		return true;
	}
}
//初步判断是否是手机号码
function isMobileNumber(mobiles) {
	if(valIsEmpty(mobiles)){
		return false;
	} else {
		var partten = /^\d{11}$/;
		if(partten.test(mobiles)){
			 return true;
		} else {
			return false;
		}
	}
}

function checkNumber(telNumId){
	var telNum = $("#"+telNumId).val();
	telNum=replaceSpace(telNum);
	
	var length = telNum.length;
	var showTelNum="";
	
	if (length <= 3){
		showTelNum = telNum;
	}
	
	if (length > 3){
		showTelNum=telNum.substr(0,3) + " " + telNum.substr(3,length - 3 > 4 ? 4 : length - 3);
	}
	
	if (length > 7){
		showTelNum += " " + telNum.substr(7);
	}
	
	$("#" + telNumId).val(showTelNum);
}

function replaceSpace(strValue){
	
	return strValue.replace(/\s+/g,"");
}