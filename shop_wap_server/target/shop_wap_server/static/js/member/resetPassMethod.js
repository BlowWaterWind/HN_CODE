$(function() {
	
	$("#answer").click(function() {
		$.ajax({
			type : 'post',
			url : checkUrl,
			cache : false,
			context : $(this),
			dataType : 'text',
			success : function(data) {
				var res=data.replace('\"','').replace('\"','');
				if (res == "success") {
					window.location.href=submitUrl;
				} else {
					showAlert(res);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert("服务中断，请重试");
			}
		});
		
	});

});