$(function() {
	$.fn.raty.defaults.path = baseUrl+'/images/member';
	var i=0;
	   $('#xing1').raty({
		   goodsRatingScore: function() {
			   i=i+1;
		     return $(this).attr('data-score');

		   }

		 });
	   $('#xing2').raty({
		   shopRatingScore: function() {
			   i=i+1;

		     return $(this).attr('data-score');

		   }

		 });
	   $('#xing3').raty({
		   logisticsRatingScore: function() {
			   i=i+1;

		     return $(this).attr('data-score');

		   }

		 });

	$("#submitBtn").click(function() {

	/*	if($("#stars-input").val()==""){
			showAlert("商品未评分");
			return;
		}
		if($("#stars2-input").val()==""){
			showAlert("店铺态度未评分");
			return;
		}
		if($("#stars3-input").val()==""){
			showAlert("物流质量未评分");
			return;
		}
		if($("#ratingContain").val()==""){
			showAlert("评价内容不能为空");
			return;
		}*/
		
		$.ajax({
			type : "POST", // 用POST方式传输
			dataType : "text", // 数据格式:JSON
			url : subUrl, // 目标地址
			data :$("#commentForm").serialize(),
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			},
			success : function(msg) {
				msg=msg.replace("\"","").replace("\"","");
				if(msg=="新增评价成功"){
					showAlert("评价成功！",gotoFunc);
					
				}else{
					showAlert(msg);
				}
			}
		
		});
	});
	function gotoFunc(){
		window.location.href=gotoUrl;
	}

});