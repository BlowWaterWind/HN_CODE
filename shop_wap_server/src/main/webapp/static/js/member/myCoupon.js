$(function() {
	  var $li = $('#tab li');
      var $ul = $('#content ul');
                  
      $li.click(function(){
		var status=$(this).data("index");
		window.location.href=changeTabUrl+"?status="+status;

	});
	
      $("#delBatchBtn").click(function() {
    	 var indexs = "";
  		$("input[name=isDelete]:checked").each(function(index) {
  			indexs+=$(this).val()+",";
  		});
  		if(indexs.length==0){
  			alert("请勾选需要删除的优惠券！");
  			return false;
  		}
  		$.ajax({
  			type : 'post',
  			url : delBtnUrl,
  			cache : false,
  			context : $(this),
  			dataType : 'text',
  			data : {
  				couponId:indexs
  			},
  			success : function(data) {
  				var res=data.replace('\"','').replace('\"','');
  				if (res == "success") {
  					showAlert('删除成功！',reload);
  				} else {
  					showAlert(res);
  				}
  			},
  			error : function(XMLHttpRequest, textStatus, errorThrown) {
  				showAlert("服务中断，请重试");
  			}
  		});
  		
  	});
      
      function reload(){
  		window.location.reload();
  	}
  	
	

});