$(function() {

	

	$(".deleteBtn").click(function() {
		var followId=$(this).data("index");
		$.ajax({
  			type : 'post',
  			url : delBtnUrl,
  			cache : false,
  			context : $(this),
  			dataType : 'text',
  			data : {
  				followId:followId
  			},
  			success : function(data) {
  				data=data.replace("\"","").replace("\"","");
  				if (data == "success") {
  					showAlert('取消成功！',reload);
  				} else {
  					showAlert(data);
  				}
  			},
  			error : function(XMLHttpRequest, textStatus, errorThrown) {
  				showAlert("服务中断，请重试");
  			}
  		});

	});
	

});