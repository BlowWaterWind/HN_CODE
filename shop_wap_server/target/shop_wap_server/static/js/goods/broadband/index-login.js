/**
 * @author zangnc
 * @date 2017-12-3
 * @description wap宽带首页显示登录信息
**/
$(function() {
	$.ajax({
		  url: ctx+"broadband/indexLogin", 
		  data: {},
		  dataType: "json",
		  type : 'post',
		  success: function(e){
			  	if(e.respCode == "0"){
			  		//未登录
			  		showUnLogin();
	  			}else if(e.respCode == "1"){
	  				//已登录显示宽带信息
	  				var loginHtml = '';
	  				loginHtml += '<div class="clearfix kd-new-info">';
	  				loginHtml += '<p class="pull-left">电话号码：<span>'+e.broadbandInfoMap.mobile+'</span></p>';
	  				loginHtml += '<p class="pull-right">宽带账号：<span>'+e.broadbandInfoMap.accessAcct+'</span></p>';
	  				loginHtml += '</div>';
	  				loginHtml += '<div class="clearfix kd-new-info">';
	  				loginHtml += '<p class=" pull-left">当前速率：<span>'+e.broadbandInfoMap.rate+'M</span></p>';
	  				loginHtml += '<p class="pull-right">宽带套餐：<span>'+e.broadbandInfoMap.discntName+'</span></p>';
	  				loginHtml += '</div>';
	  				loginHtml += '<div class="clearfix kd-new-info">';
					loginHtml += '<p class=" pull-left">安装时间：<span>'+e.broadbandInfoMap.startTime+'</span></p>';
					loginHtml += '<p class="pull-right">到期时间：<span>'+e.broadbandInfoMap.endTime+'</span></p>';
	  				loginHtml += '</div>';
	  				//loginHtml += '<div class="clearfix kd-new-info">';
	  				//loginHtml += '<p class=" pull-left">状态：<span>正常</span></p>';
	  				//loginHtml += '</div>';
	  				$(".content").html(loginHtml);
					$("#accessAcct").val(e.broadbandInfoMap.accessAcct);
					//动态加载自助报障
					$(".container").find("li a[data-title='自助排障']").each(function(i,value){
						var url = $(value).attr("href");
						var url2 = url.replace(/busiCodePlaceHolder/g,e.broadbandInfoMap.accessAcct);
						var url3  = url2.replace(/areaCodePlaceHolder/g,"0713");
						$(value).attr("href",url3);
					});
	  			}else if(e.respCode == "3"){
					//显示任我行办理信息
					var loginHtml = '';
					loginHtml += '<div class="kd-entrance">';
					loginHtml += '<p>'+e.product.desc+'</p>';
					loginHtml += '<a href="'+ctx+'/consupostn/freeGift100M?goodsSkuId='+e.product.goodsSkuId+'" class="btn btn-border-blue">立即办理</a>';
					loginHtml += '</div>';
					$(".content").html(loginHtml);
				}
				//else if(e.respCode == "4"){
				//	//显示任我行办理信息
				//	var loginHtml = '';
				//	loginHtml += '<div class="kd-entrance">';
				//	loginHtml += '<p>'+e.product.desc+'</p>';
				//	loginHtml += '<a href="'+ctx+'/consupostn/bigKing?goodsSkuId='+e.product.goodsSkuId+'" class="btn btn-border-blue">立即办理</a>';
				//	loginHtml += '</div>';
				//	$(".content").html(loginHtml);
				//}
		  },
		  error: function(e) {
			  showUnLogin();
		  }
	  });

	$(".container").find("li a[data-title='自助排障']").click(function(){
		if($("#accessAcct").val()==''){
			showAlert("无宽带信息，无法排障！");
			return false;
		}
	});
});

function showUnLogin(){
	var loginHtml = '';
	loginHtml += '<ul class="kd-index-list">';
		loginHtml += '<li>';
			loginHtml += '<div>尊敬的用户，你还未登录，请<a href="'+ctx+'/login/toLogin" class="ml10 font-blue">立即登录</a></div>';
		loginHtml += '</li>';
	loginHtml += '</ul>';
	
	$(".content").html(loginHtml);
}