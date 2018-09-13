
$(function(){
	
	/**
	 * 请求店铺中的相关信息：好评率、店铺商品数量
	 * 
	 * 方法功能：以后关于店铺的相关信息都加在该方法中，减少网络访问量
	 */
	var url = baseProject+"shop/shopIntroduction"
	var params = {"shopId":currentShopId}
	$.post(url,params,function(data){
		// 店铺商品数量
		if(data.shopGoodsCount)
		{
			$("#shopGoodsCount").html(data.shopGoodsCount);
		}
	});
	
	  /**
	   *搜索店铺的联系方式：qq
	   */
	  $.post(baseProject+"shop/getShopOnlineServiceList",{"shopId":currentShopId},function(data){
		  if(data[0])
		  {
	       var connection = "http://wpa.qq.com/msgrd?v=3&uin="+ data[0].onlineServiceNumber+"&site=qq&menu=yes";
	       $("#connectionStation").attr("href",connection);
	       $("#connectionSaleMen").attr("href",connection);
		  }
	  });
	
	
	$("#collectShop").on("click",function(){
		$this = $(this)
		var shopName = $this.attr("shopName");
		var shopUrl = $this.attr("shopUrl");
		var shopId = $this.attr("shopId");
		var shopLogo = $this.attr("shopLogo");
		var params = {"shopName":shopName,"shopUrl":shopUrl,"shopId":shopId,"shopLogo":shopLogo};
		var url = baseProject + "shop/saveMemberFlow"
		$.post(url,params,function(data){
			if(data.flag == 'noLogin')
			{
				window.location.href = baseProject + "login/toLogin";
			}else{
				setTimeout("$('#myModal').css('display','none');",2000);
			}
		});
		
	});
	  
	  
})