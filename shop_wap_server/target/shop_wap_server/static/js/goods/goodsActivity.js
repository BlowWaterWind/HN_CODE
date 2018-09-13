// 商品活动查询
	$(function() {
		getGoodsActivity();
		
		
	})

	function getGoodsActivity() {
		 var reg = new RegExp("(^|&)marketId=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     var marketId = "";
	     if(r!=null){
	    	 marketId = unescape(r[2]);
	     }
	     $("#marketId").val(marketId);
	     $("#activityID").val(marketId);
		$.ajax({
			type : "POST",
			url : baseProject+"goodsInfo/getGoodsMarket",
			data : {
				"goodsId" : $("#goodsId").val(),
				"marketId": marketId
			},
			dataType : "json",
			success : function(data) {
				var marketType = "";
				if (data.status == '1') {
					
					if (data.marketType == 1) {
						marketType = "团购";
					} /*else if (data.marketType == 2) {
						marketType = "秒杀";
						$("#killPrice").html("￥" + (data.marketPrice*0.01).toFixed(2));
						$("#delPrice").html($("#goodsPrice").html());
						$("#kkNum").html("已售："+data.saleNum+"件,"+"剩余："+data.stockNum+"件");
						$("#num_notice").html("已抢"+((data.saleNum*100)/(data.saleNum+data.stockNum)).toFixed(0)+"%");
						$("#percent_notice").css("width",((data.saleNum*100)/(data.saleNum+data.stockNum)).toFixed(2)+"%");
						if(data.to_start>0){
							$("#mark_date").html("距开始");
							$(".time-djs").addClass("bg-gray");
							$("#handle").hide();
							timer(data.to_start/1000);
						}else if(data.to_end>0){
							$("#mark_date").html("距结束");
							timer(data.to_end/1000);
						}else{
							$(".time-djs").addClass("bg-gray");
							$(".date-time").html("活动已结束");
							$("#handle").hide();
						}
						
						$(".time-djs").css("display","block");
						
						$("#a_handle").text("立即秒杀");
						
					}*/ else if (data.marketType == 3) {
						
						marketType = "预售";
						if(data.firstPrice && data.firstPrice!='' && data.firstPrice!='undefined' && data.firstPrice!=0)
						{
							$("#deposit_num").html("￥"+(data.firstPrice*0.01).toFixed(2));
							$("#deposit").css("display","block");
						}else{
							$("#deposit").css("display","none");
						}
					}
					
					if(data.stockNum==0){
						$("#handle").hide();
					}
					//$("#goodsPrice").html("￥" + (data.marketPrice*0.01).toFixed(2));
					//$("#goodsPrice_2").html("￥" + (data.marketPrice*0.01).toFixed(2));
					$("#goodsNameDesc").html("【" + marketType + "】"+ $("#goodsNameDesc").html());
					$("#stockNum").html(data.stockNum);
	                $("#stockNum_2").html(data.stockNum);
				}

			}
		})
	}