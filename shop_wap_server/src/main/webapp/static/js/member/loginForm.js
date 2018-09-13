$(function() {
	function encode64(password) {

		return strEnc(password, keyStr);
	}

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
			$("#btnSendCode").removeAttr("disabled");// 启用按钮
			$("#btnSendCode").val("重新获取");
		} else {
			curCount--;
			$("#btnSendCode").val("重新获取(" + curCount + "秒)");
		}
	}

	$("input").focus(function() {
		$(this).parent().find('.input-text').addClass('hide');
	});

	$("input").focus(function() {
		$(this).parent().find('.input-text').addClass('hide');
	});

	$("input").blur(function() {
		if ($(this).val() == "") {
			$(this).parent().find('.input-text').removeClass('hide');
		}
	});

	$("#btnSendCode").click(function() {
		if ($("#mobile").val() == "") {
			showAlert("请输入手机号！")
			$("#mobile").focus();
			return false;
		}
		 var reg = /^1\d{10}$/;
		 if (!reg.test($("#mobile").val() )) {
			 showAlert("请输入正确的手机号！")
				$("#mobile").focus();
			 return false;
		 }
		curCount = count;
		// 设置button效果，开始计时
		$("#btnSendCode").attr("disabled", "true");
		$("#btnSendCode").val("重新获取(" + curCount + "秒)");
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
				if (data == "success") {

				} else {
					showAlert(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert("服务中断，请重试");
			}
		});
	});


	$(".submitBtn")
			.click(
					function() {
						var isLoginByName = document
								.getElementById("loginByName").style.display;
						var loginname = encode64($("#loginname").val()); // 对数据加密
						var loginpass = encode64($("#loginpass").val());
						var mobile = encode64($("#mobile").val());
						var servicePass = encode64($("#servicePass").val());
						if (isLoginByName == "block" || isLoginByName == "") {
							if ($("#loginname").val() == "") {
								showAlert("会员名称不能为空");
								return;
							}
							if ($("#loginpass").val() == "") {
								showAlert("密码不能为空");
								return;
							}
							if ($("#captcha").val().length == 0) {
								showAlert("验证码不能为空");
								return;
							}
							showLoadPop();
							$
									.ajax({
										type : 'post',
										url : loginByNameUrl,
										cache : false,
										context : $(this),
										dataType : 'json',
										timeout: 30000,
										data : {
											loginname : loginname,
											password : loginpass,
											captcha : $("#captcha").val(),
											CSRFToken:$("#csrftoken").val()
										},
										success : function(data) {
											if (data.msg == "success") {
												window.location.href = gotoMemberCenterUrl;
												try {
													if (typeof (dcsPageTrack) == "function") {
														dcsPageTrack('WT.ti',
																'登录成功', false,
																'WT.si_x',
																'登录成功', false,
																'WT.si_n',
																'登录', false)
													}
													document.cookie = "WT.rh_user=user="
														+ data.username
														+";expires="+new Date(new Date().getTime()+63072000000).toGMTString()+"; path=/; domain=.hn.10086.cn";
											
												} catch (e) {
													console.log("登录成功嵌码失败");
												}
											} else {
												$("#captchaImage1").attr("src",ctx+"/login/getCaptchaImage.do?s="+new Date())
												hideLoadPop();
												if(data.msg){
												showAlert(data.msg);
												loginFailToWebtreeds(data.msg);
                                                }else {
                                                    showAlert(data);
												}
											}
										},
										error : function(XMLHttpRequest,
												textStatus, errorThrown) {
											hideLoadPop();
											window.location.href=gotoMemberCenterUrl;
											loginFailToWebtreeds("登录失败");
										}
									});

						} else {
							var subUrl = "";
							var isMobile = /^1\d{10}$/;
							if ($("input[name='method']:checked").val() == "fw") {
								subUrl = loginByServiceUrl;
								if ($("#mobile").val() == "") {
									showAlert("手机号不能为空");
									return;
								}
								if (!isMobile.test($("#mobile").val())) {
									showAlert("手机号格式有误！");
									return;
								}
								if ($("#servicePass").val() == "") {
									showAlert("服务密码不能为空");
									return;
								}
								if ($("#isVerify").val=='-1' && $("#verifyCode").val().length == 0) {
									showAlert("短信随机码不能为空");
									return;
								}
								showLoadPop();
								$.ajax({
											type : 'post',
											url : subUrl,
											cache : false,
											context : $(this),
											timeout: 30000,
											dataType : 'json',
											data : {
												mobile : mobile,
												servicePass : servicePass,
                                                verifyCode : $("#verifyCode").val()
                                                ,CSRFToken:$("#csrftoken").val()
											},
											success : function(data) {
												if (data.msg == "success") {
													if (data.ref == "" || data.ref == null) {
														var url = data.goToGroupCookieUrl + "channelID=" +  data.channelID +
															"&Artifact=" + data.Artifact + "&backUrl=" + encodeURI(gotoMemberCenterUrl)
															+"&TransactionID=" + data.TransactionID;
														window.location.href = url;
													} else {
														window.location.href = data.ref;
													}
													try{
													if (typeof (dcsPageTrack) == "function") {
														dcsPageTrack('WT.ti',
																'登录成功', false,
																'WT.si_x',
																'登录成功', false,
																'WT.si_n',
																'登录', false)
													}
													document.cookie = "WT.rh_user=mob="
															+ data.mobile
															+ ":city="
															+ data.cityCode
															+ ";expires="+new Date(new Date().getTime()+63072000000).toGMTString()+"; path=/; domain=.hn.10086.cn";
													} 
													catch (e) 
													{ 
														console.log("登录成功嵌码失败");
													}
												} else {
													hideLoadPop();
													showAlert(data.msg);
                                                    needVerifyCode();
                                                    loginFailToWebtreeds(data.msg);
												}
											},
											error : function(XMLHttpRequest,
													textStatus, errorThrown) {
												hideLoadPop();
												showAlert("服务中断，请重试");
                                                needVerifyCode();
												loginFailToWebtreeds("登录失败，服务中断");
											}
										});
							} else {
								subUrl = loginBySmsUrl;
								if ($("#mobile").val() == "") {
									showAlert("手机号不能为空");
									return;
								}
								if (!isMobile.test($("#mobile").val())) {
									showAlert("手机号格式有误！");
									return;
								}
								if ($("#smsCaptcha").val() == "") {
									showAlert("短信验证码不能为空");
									return;
								}
								if ($("#isVerify").val()=='-1' && $("#imageCaptcha").val().length == 0) {
									showAlert("验证码不能为空");
									return;
								}
								showLoadPop();
								$.ajax({
											type : 'post',
											url : subUrl,
											cache : false,
											context : $(this),
											timeout: 30000,
											dataType : 'json',
											data : {
												mobile : mobile,
												smsCaptcha : encode64($("#smsCaptcha").val()),
												imageCaptcha : $("#imageCaptcha").val()
                                                ,CSRFToken:$("#csrftoken").val()
											},
											success : function(data) {
												if (data.msg == "success") {
													if (data.ref == "" | data.ref == null) {
														
														var url = data.goToGroupCookieUrl + "channelID=" +  data.channelID +
															"&Artifact=" + data.Artifact + "&backUrl=" + encodeURI(gotoMemberCenterUrl)
															+"&TransactionID=" + data.TransactionID;
														window.location.href = url;
													} else {
														window.location.href = data.ref;
													}
													try{
													if (typeof (dcsPageTrack) == "function") {
														dcsPageTrack('WT.ti',
																'登录成功', false,
																'WT.si_x',
																'登录成功', false,
																'WT.si_n',
																'登录', false)
													}
													document.cookie = "WT.rh_user=mob="
														+ data.mobile
														+ ":city="
														+ data.cityCode
														+ ";expires="+new Date(new Date().getTime()+63072000000).toGMTString()+"; path=/; domain=.hn.10086.cn";
											
													} 
													catch (e) 
													{ 
														console.log("登录成功嵌码失败");
													}
													
												} else {
													hideLoadPop();
													showAlert(data.msg);
                                                    needVerifyCode();
													loginFailToWebtreeds(data.msg);
												}
											},
											error : function(XMLHttpRequest,
													textStatus, errorThrown) {
												hideLoadPop();
												showAlert("服务中断，请重试");
                                                needVerifyCode();
												loginFailToWebtreeds("登录失败，服务中断");
											}
										});
							}

						}
					});

	$("#findPassBtn").click(
		function() {
			var isLoginByName = document
					.getElementById("loginByName").style.display;
			if (isLoginByName == "block") {
				window.location.href = findPassByNameUrl;
			} else {
				window.location.href = findPassByMobileUrl;
			}
		});

	/**
	 * 登录失败插码
	 */
	function loginFailToWebtreeds(errorMsg){
		try {
			//增加登录失败的插码
			if (typeof(dcsPageTrack)=="function"){
				dcsPageTrack("WT.ti","登录失败", false, "WT.si_x","登录失败", false, "WT.si_n","登录", false, "WT.err_code",errorMsg, true);
			}
		}catch(e){}
	}

    $("#btnSendVerifyCode").click(function() {
        if ($("#mobile").val() == "") {
            showAlert("请输入手机号！")
            $("#mobile").focus();
            return false;
        }
        var reg = /^1\d{10}$/;
        if (!reg.test($("#mobile").val() )) {
            showAlert("请输入正确的手机号！")
            $("#mobile").focus();
            return false;
        }
        curCount = count;
        // 设置button效果，开始计时
        $("#btnSendVerifyCode").attr("disabled", "true");
        $("#btnSendVerifyCode").val("重新获取(" + curCount + "秒)");
        InterValObj = window.setInterval(setRemainTimeSendVerify, 1000); // 启动计时器，1秒执行一次
        $.ajax({
            type : 'post',
            url : sendSmsCodeUrl,
            cache : false,
            context : $(this),
            dataType : 'json',
            data : {
                mobile : $("#mobile").val()
            },
            success : function(data) {
                if (data == "success") {

                } else {
                    window.clearInterval(InterValObj);// 停止计时器
                    $("#btnSendVerifyCode").removeAttr("disabled");// 启用按钮
                    $("#btnSendVerifyCode").val("重新获取");
                    showAlert(data);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                window.clearInterval(InterValObj);// 停止计时器
                $("#btnSendVerifyCode").removeAttr("disabled");// 启用按钮
                $("#btnSendVerifyCode").val("重新获取");
                showAlert("获取失败，请重试！");
            }
        });
    });
// timer处理函数
    function setRemainTimeSendVerify() {
        if (curCount == 0) {
            window.clearInterval(InterValObj);// 停止计时器
            $("#btnSendVerifyCode").removeAttr("disabled");// 启用按钮
            $("#btnSendVerifyCode").val("重新获取");
        } else {
            curCount--;
            $("#btnSendVerifyCode").val("重新获取(" + curCount + "秒)");
        }
    }
    //服务密码登录密码输入校验
    $("#mobile").blur(function(){
		needVerifyCode();
    });
    needVerifyCode();
});

//校验是否需要输入验证码
function needVerifyCode() {
    var isMobile = /^1\d{10}$/;
    if(isMobile.test($("#mobile").val())) {
        var params = {};
        params.loginType = $("input[name='method']:checked").val();
        params.mobile = $("#mobile").val();
        var timestamp = new Date().getTime();
        params.timestamp = timestamp;
        $.ajax({
            type: "post",
            url: ctx + '/login/needVerifyCode',
            data: params,
            success: function (data) {
                if (params.loginType == "fw") {
                    if (data.code == "-1") {
                        $("#fwDivVerify").show();
                    } else {
                        $("#fwDivVerify").hide();
                    }
                } else {
                    if (data.code == "-1") {
                        $("#captchaDiv").show();
                    } else {
                        $("#captchaDiv").hide();
                    }
                }
                $("#isVerify").val(data.code);
            }
        });
    }

}