$(function() {
	function encode64(password) {
		

		return strEnc(password,keyStr);
	}
	
	$("#pwdQuestionNameSec").change(function() {
		var options=$("#pwdQuestionNameSec option:selected").text();  //获取选中的项
		if(options=="其他"){
			$("#pwdQuestionNameInLi").show();
		}else{
			$("#pwdQuestionNameIn").val("");
			$("#pwdQuestionNameInLi").hide();
		}

	});

	$("#submitBtn").click(function() {
		var pwdQuestionName =$("#pwdQuestionNameSec").val();
		if(pwdQuestionName==""){
			pwdQuestionName==$("#pwdQuestionNameIn").val();
		}
		if (pwdQuestionName=="") {
			showAlert("密保问题不能为空！");
			return;
		}
		if ($("#pwdQuestionAnswer").val().length == 0) {
			showAlert("密保答案不能为空！");
			return;
		}
		var pwdQuestionAnswer = encode64($("#pwdQuestionAnswer").val());
		$.ajax({
			type : 'post',
			url : submitUrl,
			cache : false,
			context : $(this),
			dataType : 'text',
			data : {
				memberId : $("#memberId").val(),
				pwdQuestionName : pwdQuestionName,
				pwdQuestionAnswer : pwdQuestionAnswer
                ,CSRFToken:$("#csrftoken").val()
			},
			success : function(data) {
				data=data.replace("\"","").replace("\"","");
				if (data == "success") {
					showAlert("设置成功！");
					window.location.reload();
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