$(function() {
	
	var InterValObj; // timer变量，控制时间
	var count = 60; // 间隔函数，1秒执行
	var curCount;// 当前剩余秒数

	function sendMessage() {

		// 向后台发送处理数据
		$.ajax({
			type : "POST", // 用POST方式传输
			dataType : "text", // 数据格式:JSON
			url : 'Login.ashx', // 目标地址
			data : "dealType=" + dealType + "&uid=" + uid + "&code=" + code,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			},
			success : function(msg) {
			}
		});
	}

	// timer处理函数
	function SetRemainTime() {
		if (curCount == 0) {
			window.clearInterval(InterValObj);// 停止计时器
			$("#getCodeBtn").removeAttr("disabled");// 启用按钮
			$("#getCodeBtn").html("重新发送验证码");
		} else {
			curCount--;
			$("#getCodeBtn").html("请在" + curCount + "秒内输入验证码");
		}
	}

	$("#getCodeBtn").click(function() {
		var isMobile = /^1\d{10}$/;

		if ($("#mobile").val() == "") {
			showAlert("手机号不能为空");
			return;
		}
		if (!isMobile.test($("#mobile").val())) {
			showAlert("手机号格式有误！");
			return;
		}
		curCount = count;
		// 设置button效果，开始计时
		$("#getCodeBtn").attr("disabled", "true");
		$("#getCodeBtn").html("请在" + curCount + "秒内输入验证码");
		InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次
		$.ajax({
			type : 'post',
			url : sendSmsUrl,
			cache : false,
			context : $(this),
			dataType : 'text',
			data : {
				mobile : $("#mobile").val()
			},
			success : function(data) {
				var res=data.replace("\"","").replace("\"","");
				if (res == "success") {

				} else {
					showAlert(res);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert("服务中断，请重试");
			}
		});
	});
	
	
	$("#setNameBtn").click(function() {
		if ($("#username").val().length == 0) {
			showAlert("用户名不能为空！");
			return;
		}else {
            var loginname = $("#username").val();
            $("#userForm").submit();
        }
	});
	
	$("#findByPhoneBtn").click(function() {
		if ($("#mobile").val().length == 0) {
			showAlert("手机号不能为空！");
			return;
		}
		if ($("#captcha").val().length == 0) {
			showAlert("验证码不能为空！");
			return;
		}
		$.ajax({
			type : 'post',
			url : checkSmsUrl,
			cache : false,
			context : $(this),
			dataType : 'text',
			data : {
				mobile : $("#mobile").val(),
				smsCaptcha : $("#captcha").val()
                ,CSRFToken:$("#csrftoken").val()
			},
			success : function(data) {
				var res=data.replace('\"','').replace('\"','');
				if (res == "success") {
					window.location.href=gotoUrl+"?mobile="+$("#mobile").val();
				} else {
					showAlert(res);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert("服务中断，请重试");
			}
		});
	});

	function encode64(password) {
		
		return strEnc(password,keyStr);
	}
	
	$("#submitBtn").click(function() {
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
				pwdQuestionName : $("#pwdQuestionName").val(),
				pwdQuestionAnswer :pwdQuestionAnswer
			},
			success : function(data) {
				var res=data.replace('\"','').replace('\"','');
				if (res == "success") {
					window.location.href=gotoUrl+"?memberId="+$("#memberId").val();
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