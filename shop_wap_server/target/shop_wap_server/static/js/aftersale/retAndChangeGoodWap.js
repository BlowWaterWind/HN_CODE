	/**
	 * 退货：加减控件控制 
	 */
	//数量框内赋值
	function changeNumVal(index){
		$(".text_box").attr("value",$(".text_box")[index].value);
	}
	//同步处理价格,判断可退最大金额值
	function retPriceVal(){
		var tempPrice = $(".text_box").val()*skuPrice;
		var maxret = payPrice > tempPrice?tempPrice:payPrice;
		//显示
		$(".cl-red").html('￥'+maxret);
		$('input[name="returnPrice"]').attr("value",maxret); 
	}
	//数量加减控件注册事件
	$(function() {
		//退货+
		$('#retGoodForm input[value="+"]').click(function() { 
			var textVal = $(".text_box")[0].value * 1;
			if (textVal === canServerNum*1||textVal > canServerNum) {
				$(".numRmd").remove();
				$("#numCag").after('<lable style="color:red" class="numRmd">&nbsp;*您允许退货的最大数量为：'+canServerNum+'</lable>');
			} else {
				$(".text_box")[0].value = textVal + 1;
				changeNumVal(0);
			}
			retPriceVal();
		});
		//退货-
		$('#retGoodForm input[value="-"]').click(function() {
			var textVal = $(".text_box")[0].value * 1;
			if (textVal === 1 || textVal < 1) {
				$(".numRmd").remove();
				$("#numCag").append('<lable style="color:red" class="numRmd">&nbsp;数量不能小于1</label>');
				$(".text_box")[0].value = 1;
			} else {
				$(".text_box")[0].value = textVal-1;
				changeNumVal(0);
			}
			retPriceVal();
		});
		//鼠标离开时判断输入类型、输入数量值
		$("#retGoodForm .text_box").blur(function() {
			var textVal = $(".text_box")[0].value * 1;
			if (!(/^(\+|-)?\d+$/.test(textVal))) {
				//alert("请输入正确的数量值");
				$(".numRmd").remove();
				$("#numCag").append('<lable style="color:red" class="numRmd">&nbsp;*请输入正确的数量值</lable>');
				$(".text_box")[0].value = 1;
			}
			if (textVal === 1 || textVal < 1) {
				$(".numRmd").remove();
				$("#numCag").append('<label style="color:red">数量至少必须大于0</label>');
				$(".text_box")[0].value = 1;
			}
			if (textVal > canServerNum) {
				$(".numRmd").remove();
				$("#numCag").append('<lable style="color:red" class="numRmd">&nbsp;*您允许退货的最大数量为：'+canServerNum+'</lable>');
				$(".text_box")[0].value = 1;
			}
			changeNumVal(0);
			retPriceVal();
		});
	});
	
	/**
	 * 换货：数量加减控件注册事件
	 */
	$(function() {
		//换货+
		$('#changeGoodForm input[value="+"]').click(function() { 
			var textValSelect = "#changeGoodForm .text_box";
			var textVal = $(textValSelect).val() * 1;
			if (textVal === initSkuNum||textVal > initSkuNum) {
				$(".numRmd").remove();
				$("#numCag1").after('<lable style="color:red" class="numRmd">&nbsp;*您允许换货的最大数量为：'+initSkuNum+'</lable>');
			} else {
				$(textValSelect)[0].value = textVal + 1;
				changeNumVal(1);
			}
		});
		//换货-
		$('#changeGoodForm input[value="-"]').click(function() {
			var textValSelect = "#changeGoodForm .text_box";
			var textVal = $(textValSelect).val() * 1;
			if (textVal === 1 || textVal < 1) {
				$(".numRmd").remove();
				$("#numCag1").append('<lable style="color:red" class="numRmd">&nbsp;数量不能小于1</label>');
				$(textValSelect)[0].value = 1;
			} else {
				$(textValSelect)[0].value = textVal-1;
				changeNumVal(1);
			}
		});
		//鼠标离开时判断输入类型、输入数量值
		$("#changeGoodForm .text_box").blur(function() {
			var textValSelect = "#changeGoodForm .text_box";
			var textVal = $(textValSelect).val() * 1;
			if (!(/^(\+|-)?\d+$/.test(textVal))) {
				//alert("请输入正确的数量值");
				$(".numRmd").remove();
				$("#numCag1").append('<lable style="color:red" class="numRmd">&nbsp;*请输入正确的数量值</lable>');
				$(textValSelect)[0].value = 1;
			}
			if (textVal === 1 || textVal < 1) {
				$(".numRmd").remove();
				$("#numCag1").append('<label style="color:red">数量至少必须大于0</label>');
				$(textValSelect)[0].value = 1;
			}
			if (textVal > canServerNum) {
				$(".numRmd").remove();
				$("#numCag1").append('<lable style="color:red" class="numRmd">&nbsp;*您允许换货的最大数量为：'+initSkuNum+'</lable>');
				$(textValSelect)[0].value = 1;
			}
			changeNumVal(1);
		});
	});
	
	
	/**
	 * 显示的div控制：aftersale.common.js必须加载在前面
	 */
	function selectDiv(serverName) {
		if (serverName === "retGood") {
			$("#changeGoodDiv").removeClass("active");
			$("#retGoodDiv").addClass("active")
			initData("2", serverName);
		}
		if (serverName === "changeGood") {
			$("#retGoodDiv").removeClass("active")
			$("#changeGoodDiv").addClass("active");
			initData("3", serverName);
		}
	}
	function initDivStyle(asServerTypeId, serverName, result) {
		var liE = "";
		if (asServerTypeId === "2") {
			$("#retGoodDiv ul.radio-list").empty();
			for (var i = 0; i < result.length; i++) {
				liE = $("<li><label class='tabcon-radio'><input type='radio' name='retGoodReason' class='radio' value='"+result[i].aftersaleReplyReason+"'/>"
						+ result[i].aftersaleReplyReason + "</label></li>");
				$("#retGoodDiv ul.radio-list").append(liE);
			}

		} else if (asServerTypeId == "3") {
			$("#changeGoodDiv ul.radio-list").empty();
			for (var i = 0; i < result.length; i++) {
				liE = $("<li><label class='tabcon-radio'><input type='radio' name='changeGoodReason' class='radio' value='"+result[i].aftersaleReplyReason+"'/>"
						+ result[i].aftersaleReplyReason + "</label></li>");
				$("#changeGoodDiv ul.radio-list").append(liE);
			}
		}
		$(".radio").click(function(){
			$(".radio").removeClass("active");
			$(this).addClass("active");
			$(".radio").removeAttr("checked");
			$(this).attr("checked", "checked");
		});
	}	
	
	/**
	 * 表单验证
	 */
	function changeGoodCheck() {
		flage = verify('changeGood')==true?true:false;
		if(flage==false){
			$('#changeGoodForm .radio-list+span').remove();	
			$('#changeGoodForm .radio-list').after('<span class="tc-title">*请选择你的换货原因</span>');
			return false;
		}
		if ($("#changeGoodExplain").val().length > 500) {
			$('.rm').remove();
			$("#changeGoodForm .sm-con").append('<span class="rm" style="color:red">原因说明应少于500字</span>');
			return false;
		}
		return true;
	}
	
	function retGoodCheck() {
		var flage = verify('retGood')==true?true:false;
		if(flage===false){
			$('#retGoodForm .radio-list+span').remove();	
			$('#retGoodForm .radio-list').after('<span class="tc-title">*请选择你的退货原因</span>');
			return false;
		}	
		if ($("#retGoodExplain").val().length > 500) {
			$('.rm').remove();
			$("#retGoodForm .sm-con").append('<span class="rm" style="color:red">原因说明应少于500字</span>');
			return false;
		}
		return true;
	}
	