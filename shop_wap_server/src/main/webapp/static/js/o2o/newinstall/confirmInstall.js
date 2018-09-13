$(function() {
	$("#phoneName").bind("focus",function(){
		if($(this).val()=="移动用户手机号码"){
			$(this).val("");
		}
	});	
	
	$("#installName").bind("focus",function(){
		if($(this).val()=="手机号所属人"){
			$(this).val("");
		}
	});	
	
	$("#idCard").bind("focus",function(){
		if($(this).val()=="手机号所属身份证号"){
		   $(this).val("");
		}
	});	
	
	
	$("#phoneName").bind("blur",function(){
		var isMobile =/^1(([0-9][0-9]))\d{8}$/;
		if($(this).val()==""){
			$("#span_inputMesage").text("手机号码不能为空");
			$(this).focus();
			return ;
		}
		
		if(!isMobile.test($(this).val())){
			$("#span_inputMesage").text("手机号码格式错误,手机号码为11位数字");
			$(this).focus();
			return ;
	    }
		$("#span_inputMesage").text("手机号、姓名、和身份证必须一致，否则会影响安装。");
	});
	
	$("#installName").bind("blur",function(){
		if($(this).val()==""){
			$("#span_inputMesage").text("姓名不能为空");
			$(this).focus();
			return ;
		}
		$("#span_inputMesage").text("手机号、姓名、和身份证必须一致，否则会影响安装。");
	});	
	
	$("#idCard").bind("blur",function(){
		if($(this).val()==""){
			$("#span_inputMesage").text("身份证不能为空");
			$(this).focus();
			return ;
		}
		var checkIdCardMsg = checkIdcard($(this).val());
		if(checkIdCardMsg !="验证通过!"){
			$("#span_inputMesage").text(checkIdCardMsg);
			return ;
		}
		$("#span_inputMesage").text("手机号、姓名、和身份证必须一致，否则会影响安装。");
		
	});	
	
	/**
	 *  填写阿安装信息的确认
	 */
	$("#a_confirm1").bind("click",function(){
		var isMobile =/^1(([0-9][0-9]))\d{8}$/;
		if($("#phoneName").val()==""){
			$("#span_inputMesage").text("手机号码不能为空");
			return ;
		}
		if(!isMobile.test($("#phoneName").val())){
			$("#span_inputMesage").text("手机号码格式错误,手机号码为11位数字");
			return ;
	    }
		if($("#installName").val()==""){
			$("#span_inputMesage").text("姓名不能为空");
			return ;
		}
		if($("#idCard").val()==""){
			$("#span_inputMesage").text("身份证不能为空");
			return ;
		}
		var checkIdCardMsg = checkIdcard($("#idCard").val());
		if(checkIdCardMsg !="验证通过!"){
			$("#span_inputMesage").text(checkIdCardMsg);
			return ;
		}
		
		$("#span_installName").text($("#installName").val());
		$("#span_idCard").text($("#idCard").val());
		$("#span_phoneNum").text($("#phoneName").val());

		$(".close-btn").click();
	});
	
	
	/*员工推荐*/
	$("#employeNo").bind("focus",function(){
		if($(this).val()=="请输入以36开头的8位人力资源员工编号"){
			$(this).val("");
		}
	});
	
	/*渠道推荐*/
	$("#channelRecoNo").bind("focus",function(){
		if($(this).val()=="请输入渠道推荐人工号"){
			$(this).val("");
		}
		
	});
	
	
	/*推荐人 确定*/
	$("#a_confirm2").bind("click",function(){
		var recommend = $("input[name='radio_recommend']:checked").val();
		if(recommend=="0"){
			$("#p_recommend").text("员工推荐");
			if($("#employeNo").val()=="" || $("#employeNo").val()=="请输入以36开头的8位人力资源员工编号"){
				$("#span_inputMesage2").text("推荐员工编号不能为空");
				return ;
			}
			$("#p_recommendNo").text("推荐员工编号:"+$("#employeNo").val());
		}
		else if(recommend=="1"){
			$("#p_recommend").text("渠道推荐");
			if($("#channelRecoNo").val()==""||$("#channelRecoNo").val()=="请输入渠道推荐人工号"){
				$("#span_inputMesage2").text("渠道推荐人工编号不能为空");
				return ;
			}
			$("#p_recommendNo").text("渠道推荐人工编号:"+$("#channelRecoNo").val());
		}
		
		//关闭弹窗
		$(".close-btn").click();
		
		
	});
	
	/*安装时间 确定*/
	$("#a_confirm3").bind("click",function(){
		$(".close-btn").click();
	});
	
	/**
	 * 单宽带立即安装
	 */
	$("#a_submitInstallOrder").bind("click",function(){
		var phoneName = $("#span_phoneNum").text();
		var installName = $("#span_installName").text();
		var idCard = $("#span_idCard").text();
		if(phoneName==""||phoneName=="移动用户手机号码"){
			showAlert("请输入手机号码！");
			return ;
		}
		var isMobile =/^1(([0-9][0-9]))\d{8}$/;
		if(!isMobile.test(phoneName)){
			$("#span_inputMesage").text("手机号码格式错误,手机号码为11位数字");
			return ;
	    }
		if(installName==""||installName=="手机号所属人"){
			showAlert("请输入姓名！");
			return ;
		}
		if(idCard==""||idCard=="手机号所属身份证号"){
			showAlert("请输入身份证号！");
			return ;
		}
		var checkIdCardMsg = checkIdcard(idCard);
		if(checkIdCardMsg !="验证通过!"){
			showAlert(checkIdCardMsg);
			return ;
		}
		var eparchyCode = $("input[name='form1_eparchyCode']").val()||"";
		if(eparchyCode == ''){
			showAlert("系统错误，地市编码为空！");
			return ;
		}
		//增加了安装地址地市与手机号码地市的校验；
		showLoadPop();
		$.ajax({
			url: ctx + "/broadbandInstall/queryTelephoneAttribution",
			type:"post",
			data: {'telephoneNum':phoneName},
			dataType: "json",
			success: function(data){
				if(data.respCode == '0'){
					var attrbutions = data.result;
					if(typeof (attrbutions[0]["AREA_CODE"]) != 'undefined' && attrbutions[0]["AREA_CODE"] == eparchyCode){
						$("#myModal1").modal();
					}else{
						hideLoadPop();
						showAlert("手机号码归属地与安装归属地不符，请重新输入手机号码或者重新选择宽带安装地址！");
					}
				}else{
					hideLoadPop();
					showAlert(data.respDesc);
				}
			}
		});
	});
	
	/*立即安装*/
	$("#a_submitInstallOrder1").bind("click",function(){
		var installName = $("#span_installName").text();
		var idCard = $("#span_idCard").text();
		var span1_phoneNum=$("#span1_phoneNum").text();
		var h_price=$("#h_price").text();

		if(installName==""||installName=="手机号所属人"){
			showAlert("请输入姓名！");
			return ;
		}
		if(idCard==""||idCard=="手机号所属身份证号"){
			showAlert("请输入身份证号！");
			return ;
		}
		var checkIdCardMsg = checkIdcard(idCard);
		if(checkIdCardMsg !="验证通过!"){
			showAlert(checkIdCardMsg);
			return ;
		}
		var  installTime = $("input[name='radio_installTime']:checked").val();

		var param = $("#form1").serializeObject();
		$.post("submitInstallOrder",param,function(data){
			if(data.code=="0"){
				$("#payForm_orderSubNo").val(data.orderSub.orderSubNo);
				$("#b_orderNo").text(data.orderSub.orderSubNo);
				$("#span_price").text(data.orderSub.orderSubAmount/100);
				$(".share-box").slideDown('fast');
				$(".more-box").addClass("on");
			}
			else {
				showAlert(data.message);
			}

		});
		if(h_price==0||h_price==""){
			window.location.href= ctx + "BroadbandTrade/orderResult";
			return false;
		}

	});
	
	/*支付*/
	$("a[name='a_pay']").bind("click",function(){
		if($("#payForm_orderSubNo").val()==""){
			showAlert("提交订单中,请稍等!");
			return ;
		}
		$("#payForm_eparchyCode").val($("#form1_eparchyCode").val());
		$("#payForm_payPlatform").val($(this).attr("paytype"));
		$("#payForm").submit();
	});
});

/**
 * 将表单元素序列化为JSON对象
 * 基于jQuery serializeArray()
 */
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};




//验证身份证函数
function checkIdcard(idcard){
	idcard = idcard.toString();
	var Errors=new Array("验证通过!","身份证号码位数不对!","身份证号码出生日期超出范围或含有非法字符!","身份证号码校验错误!","身份证地区非法!");
	//var Errors=new Array(true,false,false,false,false);
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
	var idcard,Y,JYM;
	var S,M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	//地区检验
	if(area[parseInt(idcard.substr(0,2))]==null) return Errors[4];
	//身份号码位数及格式检验
	switch(idcard.length){
		case 15:
			return Errors[0];
			break;
		case 18:
			//18 位身份号码检测
			//出生日期的合法性检查
			//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
			//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
			//if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0 )) {
			//    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
			//} else {
			//    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
			//}
			//if (ereg.test(idcard)) {//测试出生日期的合法性
			//计算校验位
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
				+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
				+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
				+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
				+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
				+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
				+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
				+ parseInt(idcard_array[7]) * 1
				+ parseInt(idcard_array[8]) * 6
				+ parseInt(idcard_array[9]) * 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1);//判断校验位
			if (M == idcard_array[17]) return Errors[0]; //检测ID的校验位
			else return Errors[3];
			//}
			//else return Errors[2];
			break;
		default:
			return Errors[1];
			break;
	}
} 