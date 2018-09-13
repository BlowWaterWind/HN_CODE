// JavaScript Document
$(document).ready(function(){
  $(".spcs").click(function(){
    $(".model-bg").animate({left:'0px'});
    $(".xzcs").animate({right:'0px'});
    $("body").addClass("open");
  });
  $(".model-bg").click(function(){
    $(".model-bg").animate({left:'100%'});
    $(".xzcs").animate({right:'-100%'});
    $("body").removeClass("open");
  });
  $(".ljgm").click(function(){
    $(".model-bg").animate({left:'0px'});
    $(".xzcs2").animate({left:'0px'});
    $("body").addClass("open");
  });
  $(".xzcs2").click(function(){
    $(".model-bg").animate({left:'100%'});
    $(".xzcs2").animate({left:'100%'});
    $("body").removeClass("open");
  });
  $(".icon-text-list.more").click(function(){
    $(".main-more").slideToggle('fast');
  });
  $(".ewm").click(function(){
    $(".model-bg2").toggle();
    $(".ewm-box").toggle();
  });
  $(".model-bg2").click(function(){
    $(".model-bg2").hide();
    $(".tcbox").hide();
    $(".fx-box").slideUp('fast');
  });
   $(".model-bg2").click(function(){
    $(".ewm-box").hide(); 
  });
  $(".fx").click(function(){
    $(".model-bg2").toggle();
    $(".main-more").hide('fast');
    $(".fx-box").slideDown('fast');
  });
  $(".model-bg2").click(function(){
    $(".model-bg2").hide();
    $(".tcbox").hide();
    $(".fx-box").slideUp('fast');
  });
  $(".cancel").click(function(){
    $(".model-bg2").hide();
    $(".fx-box").slideUp('fast');
  });
  $(".qr").click(function(){
    $(".model-bg2").show();
    $(".tcbox").show();
  });
  $(".wxts").click(function(){
    $(".model-bg2").toggle();
    $(".qrhy_wxtsmm").slideDown('fast');
  });
  $(".model-bg2").click(function(){
    $(".model-bg2").hide();
    $(".qrhy_wxtsmm").slideUp('fast');
	//$(".qrhy_wxtsmm").hide();
  });
  $(".qrhy-text .close").click(function(){
    $(".model-bg2").hide();
    $(".qrhy_wxtsmm").slideUp('fast');
	//$(".qrhy_wxtsmm").hide();
  });
  $(".sx").click(function(){
    $(".model-bg").animate({left:'0px'});
    $(".sx-main").animate({left:'50%'});
    $("body").addClass("open");
  });
  $(".model-bg").click(function(){
    $(".model-bg").animate({left:'100%'});
    $(".sx-main").animate({left:'100%'});
    $("body").removeClass("open");
  });
  $(".close-ex").click(function(){
    $(".model-bg2").toggle();
    $(".ewm-box").toggle();
  });
  $(".bottom-ad .close").click(function(){
    $(".bottom-ad").toggle();
  });
});

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
$(".sum").click(function(){
    $(".sum-box").slideDown('fast');
    $(".more-box").addClass("on");
  });
  $(".more-box").click(function(){
    $(".sum-box").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".cancel").click(function(){
    $(".sum-box").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".ixchange").click(function(){
    $(".sum-sd").slideDown('fast');
    $(".more-box").addClass("on");
  });
  $(".more-box").click(function(){
    $(".sum-sd").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".cancel").click(function(){
    $(".sum-sd").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $("#top").click(function(){ //当点击标签的时候,使用animate在200毫秒的时间内,滚到顶部
	 $("html,body").animate({scrollTop:"0px"},200);
  });
});

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
//网厅
$(document).ready(function(){
  jQuery("#set").click(function(){
	//$("#main").animate({'margin-left': '70%'});
	$(".set-wrap").animate({'left': '0'});
	$(".set-wrap-top").animate({'left': '0'});
	$("body").addClass("open");
	//$(".icon-main-set").addClass("icon-main-set-on");
	}
  )
})
  $(".set-wrap-bg").click(function(){
    $("body").removeClass("open");
	$(".set-wrap").animate({'left': '-100%'});
	$(".set-wrap-top").animate({'left': '-100%'});
  });
$(document).ready(function(){
  jQuery(".lljc").toggle(function(){
	$(".lljc a").animate({'margin-right': '0'});
	}, function () {
	$(".lljc a").animate({'margin-right': '-90px'});
	}
  )
})
//loading
window.onload=function(){
  $("#load").fadeOut('2200');
}

//全站搜索
$(document).ready(function(){
$(".flip").click(function(){
$(".panel").toggle();
});
$(".site-close").click(function(){
$(".panel").toggle();
});
$(".txt-box").click(function(){
$(".fuy").toggle();
});
$(".wx").click(function(){
$(".panel").toggle();
});
});
 //二次确认框
$(document).ready(function(){
  $(".confirmtw").click(function(){
    $(".share-bit").slideDown('fast');
    $(".more-box").addClass("on");
  });
  $(".more-box").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".cancel").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  
});


//获取短信码
var countdown=60; 
function settime(val) { 
if (countdown == 0) { 
val.removeAttribute("disabled"); 
val.value="获取随机短信"; 
countdown = 60; 
} else { 
val.setAttribute("disabled", true); 
val.value="重新发送(" + countdown + ")"; 
countdown--; 
} 
setTimeout(function() { 
settime(val) 
},1000) 
} 

