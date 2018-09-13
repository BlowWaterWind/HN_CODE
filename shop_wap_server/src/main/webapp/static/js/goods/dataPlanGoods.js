/**
 * 
 */
var _loginInfo = {'isLogin':'1'};
var curWwwPath = window.document.location.href;  //当前访问路径
$(function(){
    $("#load").hide();
	$('.sp-button li').bind('click', function(){ 
	    $(this).addClass('list-active').siblings().removeClass('list-active'); 
	});
	$(".more-box").bind('click',function(){
		$(".sum-box").slideUp('fast');
		$(this).removeClass("on");
	});
	
	var planInfoLi=$(".list-active");
	
	$("#goodsPrice").html("￥" + (planInfoLi.attr("planPrice")*0.01).toFixed(2));
	var business = $('a',planInfoLi).html();
	$("#businesstype").html(business);
	 /**
     * 判断该用户是否登录,如果登录判断该用户是否可以办理该业务
     */
	 $.ajax({
	        type:'post',
	        contentType: 'application/json',
	        url:baseProject+'indexLoad/isLogin',
	        data:JSON.stringify({"isLogin":"false"}),
	        success:function(data)
	        {
		         if(data.isLogin == "0")// 已经登录
		         {
		        	 if(data.isMobileLogin == "true"){
		        		 ShowPlanInfo(planInfoLi);
		        	 }else if(data.isMobileLogin == "false"){
		        			
		        	}
		         }
	        },
	        dataType:'json'
	        });
	
	getLoginInfo(afterGetLoginInfo);

	/**
	 * 商品关注
	 */
	$("#attention").on("click",function () {
		var goodsId = $("#goodsId").val();
		var attentionType = 0;
		if($(this).hasClass('active')){
			attentionType = 1;
		}else{
			attentionType = 0;
		}
		$(this).toggleClass("active");
		$.ajax({
			url:baseProject + "goodsQuery/addOrDelGoodsAttention",
			type:"post",
			data:{"goodsId" : goodsId,"attentionType" : attentionType},
			dataType:"json",
			success:function (data) {
				if(!data.isSuccess){
					$("#attention").toggleClass("active");
					showAlert(data.msg);
				}
			}
		});
	});

	var reg = new RegExp("(^|&)marketId=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	var marketId = "";
	if(r!=null){
		$("#marketId").val( unescape(r[2]));
	}
	
});

/**
 * 取得用户的登录信息
 * @param afterGetLoginInfo  回调方法
 */
function getLoginInfo(afterGetLoginInfo) {
	$.ajax({
		url : baseProject + "indexLoad/isLogin",
		type :"post",
		dataType :"json",
		success:function (data) {
			_loginInfo = data;
			afterGetLoginInfo(data);
		},
		error : function () {

		}
	});
}

/**
 * 取得用户登录信息后执行的方法;
 */
function afterGetLoginInfo(data) {
	if(_loginInfo.isLogin == '1'){//没有登录
		if($("#attention").hasClass('active')){
			$("#attention").removeClass("active");
		}
	}else{
		queryUserIsAttentionGoods();
	}
}
/**
 * 查询用户是否已关注此商品
 * return true:已关注;
 * false:没有关注;
 */
function queryUserIsAttentionGoods() {
	var goodsId = $("#goodsId").val();
	var isAttention = false;
	$.ajax({
		url:baseProject + "goodsQuery/queryGoodsIsAttention",
		type:"post",
		data:{goodsId : goodsId},
		dataType:"text",
		success:function (data) {
			if(data=="false"){
				if($("#attention").hasClass('active')){
					$("#attention").removeClass('active');
				}
				
			}else{
				if(!$("#attention").hasClass('active')){
					$("#attention").addClass('active');
				}
			}
		}
	});
}

function ShowPlanInfo(obj){
	 
	$("#goodsPrice").html("￥"+($(obj).attr("planPrice")*0.01).toFixed(2));
	//$("#planDesc").html($(obj).attr("plan_desc"));
	var elementId = $(obj).attr("ELEMENT_ID");
	var business = $('a',$(obj)).html();
	$("#businesstype").html(business);
	
	$.ajax({
		type:"POST",
		url:baseProject + "flow/queryFlowInfos",
		data:{'elementId':elementId},
		dataType:"json",
		success:function(data){
			 if(data.ProductFlag=='true'){
					if (data.OpenFlag=='true'|| data.BookFlag=='true'){
					   //已办理,取消办理
						$('#handle a').bind("click");
						$("#handle a").addClass(function(){return "btn-rose"});
					   $("#handle a").html("取消办理");
					   $("#handle").attr("flag","cancelBusi");
					}else if(data.OpenFlag=='false'&& data.BookFlag=='false'){
					//立即办理
						$('#handle a').bind("click");
						$("#handle a").addClass(function(){return "btn-rose"});
					   $("#handle a").html("立即办理");
					   $("#handle").attr("flag",function(){return "openBusi"});
					   
					}
				}else if(data.ProductFlag=='false'){
					//无法办理
					$("#handle a").html("无法办理");
					$("#handle a").removeClass(function(){return "btn-rose"});
					$('#handle a').unbind("click");
				}
		}
	});
}

function placeOrder(){
    var planInfoLi=$(".list-active");
	
	
	var busiType = $("#handle").attr("flag");
	$("#busiType2").val(busiType);
	$("#goodsSkuId2").val(planInfoLi.attr("skuid"));
	$("#goodsSkuPrice2").val(planInfoLi.attr("planPrice"));
	$("#goodsFormat2").val($('a',planInfoLi).html());
	$("#elementId2").val(planInfoLi.attr("ELEMENT_ID"));
	if(planInfoLi.attr("PRODUCT_ID") != null){
		$("#productId2").val(planInfoLi.attr("PRODUCT_ID"));
	}
	var url = baseProject+$("#gotoSuccessPage").attr('action');
	$("#submitOrder").attr('action',url);
	$("#submitOrder").submit();
}

/**
 * 购买订单提交
 */
function buyFlow(){
	

	$(".sum-box").slideUp('fast');
	$("#load").show();
	$(".more-box").unbind("click");
	var planInfoLi=$(".list-active");
	var busiType = $("#handle").attr("flag");
	$("#busiType1").val(busiType);
	
	$("#elementId1").val(planInfoLi.attr("ELEMENT_ID"));
	$("#productId1").val(planInfoLi.attr("PRODUCT_ID"));
	$("#goodsSkuId").val(planInfoLi.attr("skuid"));
	$("#goodsSkuPrice1").val(planInfoLi.attr("planPrice")*0.01);
	
	//订单 
	$("#busiType2").val(busiType);
	$("#goodsSkuId2").val(planInfoLi.attr("skuid"));
	$("#goodsSkuPrice2").val(planInfoLi.attr("planPrice"));
	$("#goodsFormat2").val($('a',planInfoLi).html());
	$("#elementId2").val(planInfoLi.attr("ELEMENT_ID"));
	if(planInfoLi.attr("PRODUCT_ID") != null){
		$("#productId2").val(planInfoLi.attr("PRODUCT_ID"));
	}
	
	var submitGoodInfo = $("#submitGoodInfo");
	var url = "";
	if($("#marketId").val()==""){
		url = baseProject+$("#submitGoodInfo").attr('action');
		$("#submitGoodInfo").attr('action',url);
		$.post(submitGoodInfo.attr('action'), submitGoodInfo
									.serializeArray(), function (data){
			                    
			                    if (data.X_RESULTCODE == "0") {
									$("#orderNos").val(data.orderNos);
									var url = baseProject+$("#gotoSuccessPage").attr('action');
									$("#gotoSuccessPage").attr('action',url);
									$("#gotoSuccessPage").submit();
									// placeOrder();
								} else if(data.X_RESULTCODE == "-1"){
									$("#respDesc").val(data.X_RESULTINFO);
									var url = baseProject+$("#errorReason").attr('action');
									$("#errorReason").attr('action',url);
									$("#errorReason").submit();
								}else{
									
								}
							});
	}
	else {
		url = baseProject+"act/seckill?activityID="+marketId;
		$.ajax({
	        type : "POST",
	        url : url,
	        data : {
	            "marketId": marketId
	        },
	        dataType : "json",
	        success : function(data) {
	        	 if (data.status == '1') {
	            	window.location.href=baseProject + "login/toLogin?mobileFlag=true&specialRef="+curWwwPath;   
	            }
	            else {
	            	showAlert(data.message);
	            }

	        }
	    });
		
		
		
	}
	
	
	
}


function confirmPay(){
	 buyFlow();
//	 $.ajax({
//	        type:'post',
//	        contentType: 'application/json',
//	        url:baseProject+'indexLoad/isLogin',
//	        data:JSON.stringify({"isLogin":"false"}),
//	        success:function(data)
//	        {
//		         if(data.isLogin == "0" )// 已经登录
//		         {
//		        	 if(data.isMobileLogin == "true"){
//		        		 buyFlow();
//		        	 }else if(data.isMobileLogin == "false"){
//		        		 window.location.href=baseProject + "login/toLogin?mobileFlag=true&specialRef="+curWwwPath;    	
//		             }
//		         }else{
//		        	 window.location.href=baseProject + "login/toLogin?mobileFlag=true&specialRef="+curWwwPath;
//		         }
//	        },
//	        dataType:'json'
//	        });
}