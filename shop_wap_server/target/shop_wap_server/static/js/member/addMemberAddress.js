$(function() {

	$("#memberRecipientProvince").change(function() {
		$.ajax({
			type : 'post',
			url : changeUrl,
			cache : false,
			context : $(this),
			dataType : 'json',
			data : {
				pId : $("#memberRecipientProvince").val()
			},
			success : function(data) {
					$('#memberRecipientCity').html('');
		            if(data.flag=='Y'){
		            	  var  htt = " <option value=''>请选择</option>";
		            	$.each(data.orgList,function(i,obj){
		            		htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
		            	});
		            	$('#memberRecipientCity').html(htt);
		            }else{
		            	showAlert("您查询的区县不存在！");
		            }
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert("您查询的区县不存在！");
			}
		});

	});
	
	$("#memberRecipientCity").change(function() {
		$.ajax({
			type : 'post',
			url : changeUrl,
			cache : false,
			context : $(this),
			dataType : 'json',
			data : {
				pId : $("#memberRecipientCity").val()
			},
			success : function(data) {
					$('#memberRecipientCounty').html('');
		            if(data.flag=='Y'){
		            	  var  htt = " <option value=''>请选择</option>";
		            	$.each(data.orgList,function(i,obj){
		            		htt+="<option value='"+obj.orgId+"'>"+obj.orgName+"</option>";
		            	});
		            	$('#memberRecipientCounty').html(htt);
		            }else{
		            	showAlert("您查询的区县不存在！");
		            }
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				showAlert("您查询的区县不存在！");
			}
		});

	});
	
	$("#submitBtn").click(function() {
		 var isMobile = /^1\d{10}$/;
		if($("#memberRecipientName")==""){
			$("#memberRecipientNameDiv").addClass("input-fr");
			$("#recName").html("*收货人不能为空");
			$("#memberRecipientName").focus();
			return;
		}else{
			$("#memberRecipientNameDiv").removeClass("input-fr");
			$("#recName").html("");
		}
		if($("#memberRecipientPhone")==""){
			$("#memberRecipientPhoneDiv").addClass("input-fr");
			$("#memberRecipientPhoneSpan").html("*手机号不能为空");
			$("#memberRecipientPhone").focus();
			return;
		}else{
			$("#memberRecipientPhoneDiv").removeClass("input-fr");
			$("#memberRecipientPhoneSpan").html("");
		}
		if(!isMobile.test($("#memberRecipientPhone").val())){
			$("#memberRecipientPhoneDiv").addClass("input-fr");
			$("#memberRecipientPhoneSpan").html("*手机号格式有误");
			$("#memberRecipientPhone").focus();
			return;
		}else{
			$("#memberRecipientPhoneDiv").removeClass("input-fr");
			$("#memberRecipientPhoneSpan").html("");
		}
		if($("#memberRecipientCounty")==""){
			$("#regionDiv").addClass("input-fr");
			$("#regionSpan").html("*请选择省市地区");
			$("#memberRecipientCounty").focus();
			return;
		}else{
			$("#regionDiv").removeClass("input-fr");
			$("#regionSpan").html("");
		}
		if($("#memberRecipientAddress")==""){
			$("#memberRecipientAddressDiv").addClass("input-fr");
			$("#memberRecipientAddressSpan").html("*详细地址不能为空");
			$("#memberRecipientAddress").focus();
			return;
		}else{
			$("#memberRecipientAddressDiv").removeClass("input-fr");
			$("#memberRecipientAddressSpan").html("");
		}
		$.post(submitUrl,$("#addrForm").serialize(), function(data) {

			if(data=="success"){
				showAlert("添加成功！",gotofuc);
			}else{
				showAlert(data);
			}
			});

	});
	
	function gotofuc(){
		window.location.href=gotoUrl;
	}
});