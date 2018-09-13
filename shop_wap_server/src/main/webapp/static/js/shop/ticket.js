$(function(){

	/**
	 * 请求优惠券信息
	 */
	var url = baseProject + "shopInfo/queryTicketList";
	var params = {"shopId":currentShopId,"channelCode":channelCode};
	$.post(url,params,function(data){
		
		if(data)
		{
			for(var i=0;i<data.length;i++)
			{
				data[i].couponBatchValue =  (data[i].couponBatchValue/100).toFixed(2);	
				data[i].couponUserLowestValue =  (data[i].couponUserLowestValue/100).toFixed(2);	
			}			
			var dataSet = {"list":data};
			$("#ticketList").html(template('ticketListTmpl',dataSet));
		}
		else
	    {
			$("#ticketList").html("<li>抱歉，本店暂无优惠卷</li>");
	    }
	});	
	
	/**
	 * 领取优惠券
	 */
	$("#reciveCoupon").on("click",function(){
		var reciveCouponUrl = baseProject + "shopInfo/reciveTicket";
		var reciveCouponParams = {"couponBatchDetailId":$(this).attr("couponBatchDetailId")};
		$.post(reciveCouponUrl,reciveCouponParams,function(data){
			if(data.flag == "0")
			{
				window.location.href = baseProject+"login/tologin";
			}else{
				alert(data.resultMessage);
			}
		});
	});
	
	
});