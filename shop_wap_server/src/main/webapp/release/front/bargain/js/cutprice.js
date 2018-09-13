//(function(doc, win) {
//	var docEl = doc.documentElement,
//	resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize', 
//	recalc = function() {
//		  var b = docEl.clientWidth;
//          if (!b) return;
//		      b= b>=750?750 : b;
//		 var arem=b/16;
//		  docEl.style.fontSize =arem+ 'px';
//    };
//	if (!doc.addEventListener) {return;}
//	win.addEventListener(resizeEvt, recalc, false);
//	doc.addEventListener('DOMContentLoaded', recalc, false);

//})(document, window);

//发起砍价
function joinCut(){
	//任何触发判断是否微信，弹出关注信息！
	//上线修改成if(is_weixin()){
	//alert("非微信弹出关注！"+!is_weixin());
	if(is_weixin()){		
		$.ajaxDirect({
            url: 'bargain/joinCut',
            param:'',
            afterFn:function(data){            	 
            	// alert("非微信弹出关注！"+data.resultCode);
            	// alert("非微信弹出关注！"+data.resultInfo);
            	 if(data.resultCode=="0"){
            		 //需要登录
            		  popLgin();
            	 }
            	 else if(data.resultCode=="9"){
            		 //系统异常提示
                     popTips(data.resultInfo);
                     //该if为插码
                     if(typeof(dcsPageTrack)=="function"){dcsPageTrack ("WT.si_n","发起砍价",false ,"WT.si_x","发起砍价失败",false,"WT.err_code","系统异常请稍候在尝试",false);}
                 }else{
            		//生成砍价，或者已经有砍价 --上线地址要修改
					 //该if为插码
                	 if(null !=data && data.actId !=undefined){
                		 if(typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","生成砍价",true, "WT.si_x","生成砍价成功",true);}
                	 	 window.location.href="http://wap.hn.10086.cn/shop/release/front/bargain/share.html?actId="+data.actId;
                   //  window.location.href="http://localhost:8080/shop_wap_server/release/front/bargain/share.html?actId="+data.actId; 
                	  }
                	 else{
                			 popTips("系统异常请稍后再尝试!请走微信公众号“湖南移动和你在一起”参与活动");
                	 	}
            	 } 
            } 		
		//判断逻辑
		});
	
	}else{
		popGuanzhu();
		// console.log("非微信弹出关注！");
		//window.location.href="http://localhost:8080/shop_wap_server/release/front/bargain/share.html";
	} 
}



//砍价求帮助
function reqHelp(){
	//任何触发判断是否微信，弹出关注信息！
		if(is_weixin()){
			 //弹出分享指示
			popShare();
			//微信分享给用户
           ///shareWx();
		}else{
			popGuanzhu();
			//window.location.href="http://localhost:8080/shop_wap_server/release/front/bargain/share.html";
		} 
}
