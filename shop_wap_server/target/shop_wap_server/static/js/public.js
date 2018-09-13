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
  $(".czbd").click(function(){
    $(".model-bg").animate({left:'0px'});
    $(".czsd").animate({right:'0px'});
    $("body").addClass("open");
  });
  $(".model-bg").click(function(){
    $(".model-bg").animate({left:'100%'});
    $(".czsd").animate({right:'-100%'});
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
  /*$(".more-box").click(function(){
	$(".nav-slide").slideUp('fast');
    $(".share-box").slideUp('fast');
    $(".more-box").removeClass("on");
  });*/
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
 /* $(".more-box").click(function(){
    $(".sum-box").slideUp('fast');
    $(".more-box").removeClass("on");
  });*/
  $(".cancel").click(function(){
    $(".sum-box").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".ixchange").click(function(){
    $(".sum-sd").slideDown('fast');
    $(".more-box").addClass("on");
  });
  /*$(".more-box").click(function(){
    $(".sum-sd").slideUp('fast');
    $(".more-box").removeClass("on");
  });*/
  $(".cancel").click(function(){
    $(".sum-sd").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".ibox").click(function(){
    $(".ibox-date").slideDown('fast');
    $(".more-box").addClass("on");
  });
  /*$(".more-box").click(function(){
    $(".ibox-date").slideUp('fast');
    $(".more-box").removeClass("on");
  });*/
  $(".cancel").click(function(){
    $(".ibox-date").slideUp('fast');
    $(".more-box").removeClass("on");
  });
  $(".iyq").click(function(){
    $(".iyq-date").slideDown('fast');
    $(".more-box").addClass("on");
  });
  $(".cancel").click(function(){
    $(".iyq-date").slideUp('fast');
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

/**
 *
 * @param path 图片文件名
 * @param size  尺寸 100x100
 * @returns {*}
 */
function resImageSize(path,size){
    try {
        var  lastIndex = path.lastIndexOf(".");
        if(lastIndex>0) {
            // 获得文件后缀
            var tmpName = path.substring(lastIndex,path.length);
            path = path + "_" + size + tmpName;
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
$(".wx").click(function(){
$(".panel").toggle();
});
});
//文字滚动
function AutoScroll(obj){
$(obj).find("ul:first").animate({
marginTop:"-20px"
},500,function(){
$(this).css({marginTop:"0px"}).find("li:first").appendTo(this);
});
}
$(document).ready(function(){
setInterval('AutoScroll("#gtdown")',4000);
});
 //二次确认框
$(document).ready(function(){
  $(".confirmtw").click(function(){
    $(".share-bit").slideDown('fast');
    $(".more-box").addClass("on");
  });
  /*$(".more-box").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-box").removeClass("on");
  });*/
  $(".cancel").click(function(){
    $(".share-bit").slideUp('fast');
    $(".more-box").removeClass("on");
  });
});

 //宽带专区--请选择续费套餐
+function () {
    var sHeight=$('.renew-content').height(),
        sHeight2=$('.hy-tab').height();

    $(".select-renew li").click(function(){
        var  x= $(this).offset().top;
        if($(this).hasClass("on")){

            $('.hy-tab').height( sHeight2-sHeight);
            $(this).parent().parent().find('.renew-content').slideUp();
            $(this).removeClass('on');
            $('.active-fix').addClass('kd-disabled');
        } else
        {

            $(".select-renew li").removeClass('on');
            $(this).addClass('on');
            $('.hy-tab').height( sHeight2+sHeight);
            $(this).parent().parent().find('.renew-content').css("top",x+15);
            $(this).parent().parent().find('.renew-content').slideDown();

            $('.active-fix').removeClass('kd-disabled');

        }
    })


} ();
  
   $(".kdicon-search").click(function(){
	   $('.share-content').hide();
	   $('.kd-renew-phone-info').slideDown();
   })
   
   $(".addKdphpne,.icon-add").click(function(){
	   $('.mask').show();
	   $('.share-box').slideDown();
   })
   $(".reset-cx").click(function(){
	   $('.kd-renew-phone-info').hide();
	   $('.share-content').slideDown();
   })
   $(".btnCancel").click(function(){
	   $('.mask').hide();
	   $('.share-box').slideUp();
   })
 //获取验证码处理
var g_code_cutdown = 3; /*获取验证码倒计时*/
var g_timeOut; /*获取验证码倒计时*/
//获取验证码
//tag:去后台调用获取验证码的接口，返回结果值1;表示成功 非1表示失败
//sendNumber：发送验证码的手机号
function getVerificationCode(tag) {
	var tips;
	if (tag == 1) {
		cutDown();
		tips = "验证码以发送至手机上，5分钟内有效！";
	} else {
		$("#getCode").val("再次获取");
		tips = "oh,no~~验证码发送失败！"
	}
}
//获取验证码倒计时
function cutDown() {
		$("#getCode").attr("disabled", "disabled");
		$("#getCode").val(g_code_cutdown + "S后再次获取");
		g_code_cutdown--;
		if (eval(g_code_cutdown) == eval(0)) {
			$("#getCode").removeAttr("disabled");
			$("#getCode").val("获取验证码");
			window.clearTimeout(g_timeOut);
			g_code_cutdown = 3;
		} else {
			g_timeOut = window.setTimeout("cutDown()", 1000);
		}
}

//验证身份证函数
function checkIdcard(idcard) {
    idcard = idcard.toString();
    var Errors = new Array("验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!", "身份证地区非法!");
    //var Errors=new Array(true,false,false,false,false);
    var area = {
        11: "北京",
        12: "天津",
        13: "河北",
        14: "山西",
        15: "内蒙古",
        21: "辽宁",
        22: "吉林",
        23: "黑龙江",
        31: "上海",
        32: "江苏",
        33: "浙江",
        34: "安徽",
        35: "福建",
        36: "江西",
        37: "山东",
        41: "河南",
        42: "湖北",
        43: "湖南",
        44: "广东",
        45: "广西",
        46: "海南",
        50: "重庆",
        51: "四川",
        52: "贵州",
        53: "云南",
        54: "西藏",
        61: "陕西",
        62: "甘肃",
        63: "青海",
        64: "宁夏",
        65: "新疆",
        71: "台湾",
        81: "香港",
        82: "澳门",
        91: "国外"
    }
    var idcard, Y, JYM;
    var S, M;
    var idcard_array = new Array();
    idcard_array = idcard.split("");
    //地区检验
    if (area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];
    //身份号码位数及格式检验
    switch (idcard.length) {
        case 15:
            return Errors[0];
            break;
        case 18:
            //18 位身份号码检测
            //出生日期的合法性检查
            //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
            //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
            //if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0 )) {
            //    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
            //} else {
            //    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
            //}
            //if (ereg.test(idcard)) {//测试出生日期的合法性
                //计算校验位
            S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
                + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
                + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
                + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
                + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
                + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
                + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
                + parseInt(idcard_array[7]) * 1
                + parseInt(idcard_array[8]) * 6
                + parseInt(idcard_array[9]) * 3;
            Y = S % 11;
            M = "F";
            JYM = "10X98765432";
            M = JYM.substr(Y, 1);//判断校验位
            if (M == idcard_array[17]) return Errors[0]; //检测ID的校验位
            else return Errors[3];
            //}
            //else return Errors[2];
            break;
        default:
            return Errors[1];
            break;
    }
}