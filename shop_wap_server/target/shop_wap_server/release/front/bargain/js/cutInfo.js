	var actId = ""; 
	var wxUid = ""; 
$(function() {
	 actId =  getQueryString("actId"); 
	 wxUid =  getQueryString("wxUid"); 
	var totoalCut = "";
	var sale ="";	
	if(actId != null && actId != ""){
	
	$.ajaxDirect({  //查询活动信息
        url: 'bargain/queryCutInfo',
        param:'',
        afterFn:function(data){
        	//alert("queryCutInfo" + data.resultCode);
         	if(data.resultCode=="9"){
        		 popTips(data.resultInfo);        		
        	}else{  		 
        		totoalCut = data.totoalCut;        	
        		$('.price_yuan').html('原价：￥'+data.cost);
        		$('.price_hui').html('特惠价：￥'+data.cutPrice);
        	}       
         	
         	$.ajaxDirect({  //查询砍价信息
                url: 'bargain/queryUserCutInfo',
                param:'actId='+actId,
                afterFn:function(data){
                /*	alert("queryUserCutInfo" + data.cutNumebr);
                	alert("queryUserCutInfo resultCode" + data.resultCode);*/
                 	if(data.resultCode=="9"){
                        //该if为插码
                        if(typeof(dcsPageTrack)=="function"){dcsPageTrack ("WT.si_n","查询当前用户砍价信息",false ,"WT.si_x","查询当前用户砍价信息失败",false,"WT.err_code","系统异常请稍候在尝试",false);}
                 	}else{
                 		var totalAmount = data.totalAmount;
                 		sale = data.sale;                 	 
                 		if(totalAmount == "" || totalAmount == '0.0' || totoalCut == "" || totoalCut == '0.0'){
                 			 $('.proc_val').css({
                 		        'width': '0%'
                 		    });
                 		}else{         		
                 			var rate =Math.round(totalAmount/totoalCut * 100);                      			
                 			//alert("rate =  " +rate) ;
                 			$('.proc_val').css({'width': rate + '%'}); 
                 		}          		
                 		$('.proc_title').html( data.cutNumebr+'    已经砍至：￥<b>'+sale+'</b>');
                        //该if为插码
                        if(typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","查询用户砍价信息",true, "WT.si_x","查询用户砍价信息成功",true);}
                 	} 
                 	
                 	$.ajaxDirect({  //查询砍价明细
                        url: 'bargain/queryUserCutInfoDetail',
                        param:'actId='+actId,
                        afterFn:function(data){
                        	/*alert("queryUserCutInfoDetail resultCode" + data.resultCode);
                        	alert("queryUserCutInfoDetail " + data.resultInfo);*/
                        		if(data.resultCode=="9"){
                        			$('.row_kanlist_con').html(data.resultInfo);
                                    //该if为插码
                                    if(typeof(dcsPageTrack)=="function"){dcsPageTrack ("WT.si_n","查询当前用户砍价明细",false ,"WT.si_x","查询当前用户砍价明细失败或还没有人帮助砍价",false,"WT.err_code","微信进行砍价活动，请保持正确的姿势",false);}
                        		}else{
                        			$('.row_kanlist_con').html(data.resultInfo);
                                    //该if为插码
                                    if(typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","查询用户砍价明细",true, "WT.si_x","查询用户砍价明细成功",true);}
                                }
                        } 		
                	//判断逻辑
                	});	
                } 		 
        	//判断逻辑
        	});
        	
        } 		
	//判断逻辑	    
	}); 
	}	
	if(wxUid != ""){
		$.ajaxDirect({  //异步读取微信redis手机号，避免超时丢失手机号
            url: 'bargain/queryWxUserInfo',
            param:'wxUid='+wxUid,
            afterFn:function(data){
            } 		
    	//判断逻辑
    	});	
	} 
});

//帮助砍价
function helpCut(){
	//任何触发判断是否微信，弹出关注信息！
	//上线修改成if(is_weixin()){
	// alert("非微信弹出关注！"+!is_weixin());
		if(is_weixin()){
			 //砍价操作	
			// alert("helpCut！"+actId);
			// alert("helpCut！"+wxUid); 
			 $.ajaxDirect({
		            url: 'bargain/helpCut',
		            param:'actId='+actId+"&wxUid="+wxUid,
		            afterFn:function(data){            	 
		            	 if(data.resultCode=="1"){
		            		 var totalAmount = data.totalAmount;
		            		 sale = data.sale;
		            		 totoalCut = data.totoalCut;    
		            		 var showInfo = data.showInfo;
		            		 var totalAmount = data.totalAmount; 
		                 		if(totalAmount == "" || totalAmount == '0.0' || totoalCut == "" || totoalCut == '0.0'){
		                 			 
		                 		}else{         		
		                 			var rate =Math.round(totalAmount/totoalCut * 100);      
		                 			$('.proc_val').css({'width': rate + '%'}); 
		                 		}          		
		                 		if(sale !=undefined && sale != null && sale != "" && sale != '0.0')
		                 		$('.proc_title').html( data.cutNumebr+'    已经砍至：￥<b>'+sale+'</b>');		   
		                 		if( showInfo !=undefined && showInfo != null && showInfo != "")
		                 		$('.row_kanlist_con').html(showInfo);
		            		 popTips(data.resultInfo);
		            	 } else  if(data.resultCode=="0"){
		            		 //需要登录
		            		  popLgin();
		            	 }
		            	else{
		            		 popTips(data.resultInfo);		            		 	 
		            	 } 
		            } 		
				//判断逻辑
				});

		}else{
			popGuanzhu();
		} 
}
