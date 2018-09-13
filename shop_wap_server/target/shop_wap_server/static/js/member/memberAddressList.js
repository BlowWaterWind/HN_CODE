$(function() {

	$("a.tb-edit").click(function() {
		var addrId=$(this).data("index");
		window.location.href=updateUrl+"?addrId="+addrId;

	});
	

	$("a.tb-delete").click(function() {
		$(".share-bit").slideDown('fast');
		$(".more-box").addClass("on");
		$(this).attr("alt", "del");
	});
	
	$("a.font-muted").click(function() {
		var addrId=$(this).data("index");
		console.log(addrId)
		$.post(setDefUrl,{addrId:addrId}, function(data) {
			if(data!="success"){
				showAlert(data);
			}else{
				window.location.reload();
			}
		});

	});
    
	$("#delBtn").click(function(){
		var addrId = $("a[alt='del']").data("index");
		var flag = false;
		$("a[alt='del']").siblings().each(function(){
			if ($(this).hasClass("font-primary")) {
				flag = true;
				return false;
			}
		});
		
		$.post(delUrl,{addrId:addrId}, function(data) {
			if(data!="success"){
				showAlert(data);
			}else{
				
				if (flag) {
					showAlert("请选择默认地址!");
					$("div[class='modal-footer'] > a").unbind();
					$("div[class='modal-footer'] > a").bind("click", function(){
						$("#alertMsgInfo_content").html("");
				        $("#btnClose").click();
				        
				        window.location.reload();
					});
				} else {
					window.location.reload();
				}
			}
		});
	});
	
    $(".cancel").click(function(){
        $(".share-bit").slideUp('fast');
		$(".more-box").removeClass("on");
		$("a[alt='del']").removeAttr("alt");
	});

});