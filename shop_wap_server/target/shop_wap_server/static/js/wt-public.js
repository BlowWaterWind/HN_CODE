// JavaScript Document
$(document).ready(function(){
  jQuery("#set").toggle(function(){
	$("#main").animate({'margin-left': '70%'});
	$(".set-wrap").animate({'left': '0'});
	$(".set-wrap-top").animate({'left': '0'});
	$("body").addClass("open");
	$(".icon-main-set").addClass("icon-main-set-on");
	}, function () {
	$("#main").animate({'margin-left': '0'});
	$("body").removeClass("open");
	$(".icon-main-set").removeClass("icon-main-set-on");
	$(".set-wrap").animate({'left': '-100%'});
	$(".set-wrap-top").animate({'left': '-100%'});
	}
  )
})
$(document).ready(function(){
  jQuery(".lljc").toggle(function(){
	$(".lljc a").animate({'margin-right': '0'});
	}, function () {
	$(".lljc a").animate({'margin-right': '-90px'});
	}
  )
})

//分享
$(document).ready(function(){
  $(".share").click(function(){
    $(".share-box").slideDown('fast');
    $(".nav-slide").slideUp('fast');
    $(".more-box").addClass("on");
  });
  $(".more-box").click(function(){
	$(".nav-slide").slideUp('fast');
    $(".share-box").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".cancel").click(function(){
    $(".share-box").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".yhq").click(function(){
    $(".yhq-box").show(300).delay(1800).hide(300);
  });

  $("#top").click(function(){ //当点击标签的时候,使用animate在200毫秒的时间内,滚到顶部
	 $("html,body").animate({scrollTop:"0px"},200);
  });
});
//rem
    (function (doc, win) {
          var docEl = doc.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
            recalc = function () {
              var b = docEl.clientWidth;
              if (!b) return;
			      b= b>=640?640 : b;
			 var arem=b/16;
              docEl.style.fontSize =arem+ 'px';
            };

          if (!doc.addEventListener) return;
          win.addEventListener(resizeEvt, recalc, false);
          doc.addEventListener('DOMContentLoaded', recalc, false);
        })(document, window);
		//页面加载完以后
	document.onreadystatechange = subSomething;
	function subSomething(){
		if(document.readyState == "complete"){
			$(".u-Loading").hide(function(){
				$("#join-hui-content").removeClass('hide');
			});
		}
	}





//返回顶部
$(window).scroll(function(){  //只要窗口滚动,就触发下面代码 
	var scrollt = document.documentElement.scrollTop + document.body.scrollTop; //获取滚动后的高度 
	if( scrollt >200 ){  //判断滚动后高度超过200px,就显示  
		$("#top").fadeIn(400); //淡出     
	}else{      
		$("#top").stop().fadeOut(400); //如果返回或者没有超过,就淡入.必须加上stop()停止之前动画,否则会出现闪动   
	}
});

function renameImageSize(path,size){
    try {
        var  lastIndex = path.lastIndexOf(".");
        if(lastIndex>0) {
            // 获得文件后缀名
            var tmpName = path.substring(lastIndex,path.length);
            var tmpPath = path.substring(0, lastIndex);
            path = tmpPath + "_" + size + tmpName;
        }
    }catch (e){}
    return path;
}

function openQQApp(qqNum,aId){
	try {
	  var ua = navigator.userAgent;
	  var url = "";
	  if(ua.match(/iPhone|iPod/i) != null){
		  url="mqq://im/chat?chat_type=wpa&uin="+qqNum+"&version=1&src_type=web"; 
	  }else if(ua.match(/Android/i) != null){
		  url="mqqwpa://im/chat?chat_type=wpa&uin="+qqNum;
	  }else{
		  url="http://wpa.qq.com/msgrd?v=3&uin="+qqNum+"&site=qq&menu=yes";
	  }
	  $("#"+aId).attr("href",url);$("#"+aId+"Span").click(); 
	 }catch (e){}
}
 