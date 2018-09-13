$(function() {
	
	function encode64(password) {

		return strEnc(password,keyStr);
	}
	
	  function CheckStr(v_pw){
		   var patrn_shuzi=/^[0-9]$/; 
		   var patrn_zimu=/^[a-z]|[A-Z]$/;
		   var patrn_teshu=/^[^A-Za-z0-9]$/;
		   var flag=0;
		   var s = v_pw.split('');
		       
		   for(var i=0;i<s.length;i++){
		     if (patrn_shuzi.exec(s[i])) {
		        flag = flag+1;
		        break;
		     }
		   }
		   for(var i=0;i<s.length;i++){
		     if (patrn_zimu.exec(s[i])) {
		       flag = flag+1;
		        break;
		     }
		   }
		   for(var i=0;i<s.length;i++){
		     if (patrn_teshu.exec(s[i])) {
		        flag = flag+1;
		        break;
		     }
		   }
		   if (flag != 3) {
		   return false;
		   }
		   return true;
		   }
 
 
    
	
	$("#submitBtn").click(function() {
		if ($("#memberPassword").val()=="") {
			showAlert("新密码不能为空");
			return;
		}
		  if($("#memberPassword").val().length<8||$("#memberPassword").val().length>20){
	         	 showAlert("密码长度为8-20位");
	              return;
	         }
	    if (!CheckStr($("#memberPassword").val())) {
	            showAlert("密码需要包含A-Z的字母，0-9数字,特殊符号");
	            return;
	    }
		if ($("#confirmPass").val() != $("#memberPassword").val()) {
			showAlert("两次密码输入不一致！");
			return;
		}
		var password= encode64($("#memberPassword").val());
		$.ajax({
			type : 'post',
			url : submitUrl,
			cache : false,
			context : $(this),
			dataType : 'text',
			data : {
				memberId:$("#memberId").val(),
				memberLogingName:$("#memberLogingName").val(),
				memberPassword:password
                ,CSRFToken:$("#csrftoken").val()
			},
			success : function(data) {
				var res=data.replace('\"','').replace('\"','');
				if (res == "success") {
					showAlert("重置成功！",gotoFunc);
					
				} else {
					showAlert(res);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert("服务中断，请重试");
			}
		});
		
	});
	
	function gotoFunc(){
		window.location.href=gotoUrl;
	}
	

});