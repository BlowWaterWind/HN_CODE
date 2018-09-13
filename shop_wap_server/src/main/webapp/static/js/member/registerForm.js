$(function() {
	
	$("input").focus(function(){
		$(this).parent().find('.input-text').addClass('hide');
	});
	
	$("input").blur(function(){
		if($(this).val()==""){
			$(this).parent().find('.input-text').removeClass('hide');
			}
	});
	
	$("#member_name").blur(function(){
		if($("#member_name").val()==""){
			$("#name_text").removeClass("hide");
			$("#memberName").addClass("input-fr");
			return;
		}else{
			$("#name_text").addClass("hide");
			$("#memberName").removeClass("input-fr");
		}
		/*$.ajax({
			type:'get',
			url :  checkUserUrl,
			cache:false,
			context: $(this),
			dataType : 'text',
			data : {member_name:$("#member_name").val()},
			 success:function(data) {    
				 if(data=="该用户名已存在"){
					 $("#name_text").removeClass("hide");
					 $("#member_name").focus();
				 }
	             },       
	             error : function(XMLHttpRequest, textStatus, errorThrown) {       
	                  showAlert("用户名校验失败，请重试");  
	             }  
		});*/
	});
	
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



	$("#submitBtn").click(
			function() {
				var Regx = /^[A-Za-z0-9]*$/;
				if ($("#member_name").val()=="") {
					showAlert("会员名称不能为空");
					return;
				}
				var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				if (!Regx.test($("#member_name").val())&&filter.test($("#member_name").val())) {
					showAlert("会员名称为包含A-Z的字母，0-9数字，不允许有特殊符号");
					return;
				}
				var pattenEmail = new RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/);
				if (!pattenEmail.test($("#member_email").val())) {
					showAlert("请输入正确的邮箱");
					return;
				}
				if ($("#member_passwd").val()=="") {
					showAlert("密码不能为空");
					return;
				}
				 if($("#member_passwd").val().length<8||$("#member_passwd").val().length>20){
		         	 showAlert("密码长度为8-20位");
		              return;
		         }
			    if (!CheckStr($("#member_passwd").val())) {
			            showAlert("密码需要包含A-Z的字母，0-9数字,特殊符号");
			            return;
			    }
				
				if ($("#confirm_passwd").val() != $("#member_passwd").val()) {
					showAlert("两次密码输入不一致！");
					return;
				}
				if ($("#captcha").val().length == 0) {
					showAlert("验证码不能为空");
					return;
				}
				var password = encode64($("#member_passwd").val());
				$("#loading").show();
				$.ajax({
					type:'post',
					url :  submitUrl,
					cache:false,
					context: $(this),
					dataType : 'json',
					data : {member_email:$("#member_email").val(),member_name:$("#member_name").val(),member_passwd:password,captcha:$("#captcha").val(),CSRFToken:$("#csrftoken").val()},
					 success:function(data) { 
						 $("#loading").hide();
							var res=data.replace('\"','').replace('\"',''); 
			                if(data =="注册成功" ){       
			                    showAlert("注册成功！",gotoUrl);  
			                      
			                }else{       
			                    showAlert(data);       
			                }       
			             },       
			             error : function(XMLHttpRequest, textStatus, errorThrown) {       
			            	 $("#loading").hide();
			                  showAlert("服务中断，请重试"); 
			             }  
				});

			});
	
	function gotoUrl(){
		  window.location.href=loginUrl;  
	}

});