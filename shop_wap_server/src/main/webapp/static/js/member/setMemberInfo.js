var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
    + "wxyz0123456789+/" + "=";
function encode64(str) {
    return strEnc(str, keyStr);
}
$(function() {

	$("#memberProvince").change(function() {
		$.ajax({
			type : 'post',
			url : changeUrl,
			cache : false,
			context : $(this),
			dataType : 'json',
			data : {
				pId : $("#memberProvince").val()
			},
			success : function(data) {
					$('#memberCity').html('');
		            	  var  htt = "<option value='' selected>请选择</option>";
		            	$.each(data.orgList,function(i,obj){
		            		htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
		            	});
		            	$('#memberCity').html(htt);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});

	});
	
	$("#memberCity").change(function() {
		$.ajax({
			type : 'post',
			url : changeUrl,
			cache : false,
			context : $(this),
			dataType : 'json',
			data : {
				pId : $("#memberCity").val()
			},
			success : function(data) {
					$('#memberCounty').html('');
		            	  var  htt = "<option value=''>请选择</option>";
		            	$.each(data.orgList,function(i,obj){
		            		htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
		            	});
		            	$('#memberCounty').html(htt);
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});

	});
	
	$("#submitBtn").click(function() {
		var CheckPass = /^[\u4e00-\u9fa5a\w]{0,30}$/;
		  //获取昵称字节大小
		  var nickNameV = $("input[name='memberNickname']").val();
		  var bytesCountOfnickName = 0;
		  for (var i = 0; i < nickNameV.length; i++)
		  {
			  var c = nickNameV.charAt(i);
			  if (/^[\u0000-\u00ff]$/.test(c)) //匹配双字节
			  {
				  bytesCountOfnickName += 1;
			  }
			  else
			  {
				  bytesCountOfnickName += 2;
			  }
		  }

		  if (bytesCountOfnickName==0) {
		        showAlert("昵称不能为空");
		        return;
		    }
		 	if(bytesCountOfnickName>50){
				showAlert("昵称过长");
				return;
		 	}
		    // if ($("#memberPhone").val()=="") {
				// showAlert("请绑定手机号");
		    //     return;
		    // }
		    //不能在隐藏表单中放入从服务器上取得的电话号码
		    var digitreg=/^[0-9]*$/;
		    // 如果qq号中不包含*则为假 &&后面的条件不会被判断
		    if($("#memberQq").val().indexOf("*")== -1 && $("#memberQq").val()!=null&&!digitreg.test($("#memberQq").val()) )
		    {
				showAlert('请输入有效的QQ号码！');
		        return;
		    }
		    //真实姓名长度控制
		    var realNameV = $("#memberRealname").val();
			var bytesCountOfrealName = 0;
			for (var i = 0; i < realNameV.length; i++)
			{
				var c = realNameV.charAt(i);
				if (/^[\u0000-\u00ff]$/.test(c)) //匹配双字节
				{
					bytesCountOfrealName += 1;
				}
				else
				{
					bytesCountOfrealName += 2;
				}
			}
		    if(bytesCountOfrealName==0)
		    {
				showAlert('真实姓名不能为空！');
		        return; 
		    }
		    if(bytesCountOfrealName>50){
				showAlert('真实姓名过长！');
				return;
			}
		    if($("#memberProvince").val()=="") 
		    {
				showAlert('请选择地区-省！');
		        return; 
		    }
		    if($("#memberCity").val()=="") 
		    {
				showAlert('请选择地区-市！');
		        return; 
		    }
		    if($("#memberCounty").val()=="") 
		    {
				showAlert('请选择地区-区！');
		        return; 
		    }
		    if($("#memberAddress").val()=="") 
		    {
				showAlert('地址不能为空！');
		        return; 
		    }


		    $("#memberRealnameHidden").val(encode64($("#memberRealname").val()));
			$("#memberEmailHidden").val(encode64($("#memberEmail").val()));
            $("#memberAddressHidden").val(encode64($("#memberAddress").val()));
            if($("#memberPhone").val()!=""){
            	//更新了绑定的手机号码,加密传输
                $("#memberPhone").val(encode64($("#memberPhone").val()));
			}
        if($("#memberQq").val().indexOf('*')==-1){
            $("#memberQqHidden").val(encode64($("#memberQq").val()));
        }else{
            $("#memberQqHidden").val($("#memberQq").val());
        }
		$.ajax({
			type : 'post',
			url : subUrl,
			cache : true,
			context : $(this),
			dataType : 'json',
			data : $("#setForm").serialize(),
			success : function(data) {
				if (data == "success") {
					showAlert('设置成功！',reload);
					
				} else {
					alert(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert('服务中断，请重试');
			}
		});
		
	});
	
	function reload(){
		window.location.reload();
	}
	
	var InterValObj; // timer变量，控制时间
	var count = 60; // 间隔函数，1秒执行
	var curCount;// 当前剩余秒数
	
	// timer处理函数
	function SetRemainTime() {
		if (curCount == 0) {
			window.clearInterval(InterValObj);// 停止计时器
			$("#getMessage").removeAttr("disabled");// 启用按钮
			$("#getMessage").val("重新发送验证码");
		} else {
			curCount--;
			$("#getMessage").val("请在" + curCount + "秒内输入验证码");
		}
	}

	
	$("#getMessage").click(function() {
		$("#mobileSpan").html("");
		var phone=$("#phone").val();
		if(phone==null||phone==""){
			$("#mobileSpan").html("请输入验证手机号！");
			$("#phone").focus();
			return;
		}
		curCount = count;
		// 设置button效果，开始计时
		$("#getMessage").attr("disabled", "true");
		$("#getMessage").val("请在" + curCount + "秒内输入验证码");
		InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次
		$.ajax({
			type : 'post',
			url : sendMsgUrl,
			cache : false,
			context : $(this),
			dataType : 'text',
			data : {
				mobile : phone
			},
			success : function(data) {
				if (data == "success") {

				} else {
					$("#myModal").modal('toggle');
					showAlert(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	});
	
	$("#confirmBtn").click(function() {
		$("#mobileSpan").html("");
		var phone=$("#phone").val();
		var smscaptcha=$("#smscaptcha").val();
		if(phone==null||phone==""){
			$("#mobileSpan").html("请输入验证手机号！");
			$("#phone").focus();
			return;
		}
		if(smscaptcha==null||smscaptcha==""){
			$("#mobileSpan").html("请输入手机验证码！");
			$("#smscaptcha").focus();
			return;
		}
		$.ajax({
			type : 'post',
			url : checkMsgUrl,
			cache : false,
			context : $(this),
			dataType : 'text',
			data : {
				mobile : phone,
				smsCaptcha : smscaptcha
			},
			success : function(data) {
				//if (data == "success") {
					$("#memberPhone").val(phone);
					$("#memberPhonespan").html($("#phone").val());
					$("#memberPhoneBox").html(phone)
					$("#myModal").hide();
				//} else {
				//	$("#mobileSpan").html(data);
				//}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	});
	
	$("#phone").click(function() {
		if($("#phone").val()=="输入手机号码"){
		$("#phone").val("");
		}
	});
	
	$("#smscaptcha").click(function() {
		if($("#smscaptcha").val()=="验证码"){
		$("#smscaptcha").val("");
		}
	});

});