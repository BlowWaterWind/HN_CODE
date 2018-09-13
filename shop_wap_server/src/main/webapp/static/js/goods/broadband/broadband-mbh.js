$(function() {
	initMbhHtml();
});

function initMbhHtml(){
	var rvHtml = '';
	$.ajax({
		  url: ctx+"ims/getLoginUser", 
		  data: {},
		  dataType: "json",
		  type : 'post',
		  success: function(e){
			  	if(e.respCode == "0"){
			  		rvHtml += '<div class="wf-ait clearfix">';
						rvHtml += '<ul class="wf-con clearfix">';
							rvHtml += '<li>';		
								rvHtml += '<span class="font-gray">手&nbsp;&nbsp;&nbsp;&nbsp;机：</span>';
								rvHtml += '<div id="installPhoneNumDiv" class="sub-text">'+e.installPhoneNum+'</div>';
							rvHtml += '</li>';
							rvHtml += '<li>';		
								rvHtml += '<span class="font-gray">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span>';
								rvHtml += '<div class="sub-text"><input type="text" id="installName" name="installName" class="form-control" placeholder="请与手机号码机主姓名保持一致" /></div>';
							rvHtml += '</li>';
							rvHtml += '<li>';		
								rvHtml += '<span class="font-gray">身份证：</span>';
								rvHtml += '<div class="sub-text"><input type="text" id="idCard" name="idCard" class="form-control" placeholder="请与手机号码机主证件保持一致" /></div>';
							rvHtml += '</li>';
						rvHtml += '</ul>';
					rvHtml += '</div>';
					
					$(".broadband-userinfo").html(imsHtml);
					
					$("#installName").blur(function(){
						realityVerify();
					});
					
					$("#idCard").blur(function(){
						realityVerify();  
					});
	  			}else{
	  				
	  			}
		  },
		  error: function(e) {
			  
		  }
	});
}

/**
 * 实名验证 */
function realityVerify(){
	if($("#installName").val() != "" && $("#idCard").val() != ""){
		$.ajax({
			  url: ctx+"ims/realityVerify", 
			  data: {cardId:$("#idCard").val(),installName:$("#installName").val(),installPhoneNum:$("#installPhoneNumDiv").html()},
			  dataType: "json",
			  type : 'post',
			  success: function(e){
				  	if(e.respCode == "0"){
				  		
		  			}else{
		  				
		  			}
			  },
			  error: function(e) {
				  
			  }
		});
	}
}