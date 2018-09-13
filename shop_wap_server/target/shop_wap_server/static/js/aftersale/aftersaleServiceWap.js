	/**
	 * 表单验证
	 */
	//前端表单校验
	function repairCheck() {
		flage = verify('repair')==true?true:false;
		if(flage==false){
			$('#repairForm .radio-list+span').remove();	
			$('#repairForm .radio-list').after('<span class="tc-title">*请选择你的维修原因</span>');
			return false;
		}
		if ($("#repairExplain").val().length > 500) {
			$('.rm').remove();
			$("#repairForm .sm-con").append('<span class="rm" style="color:red">原因说明应少于500字</span>');
			return false;
		}
		return true;
	}
	
	function resendReceiptCheck() {
		flage = verify('resendReceipt')==true?true:false;
		if(flage==false){
			$('#resendReceiptForm .radio-list+span').remove();	
			$('#resendReceiptForm .radio-list').after('<span class="tc-title">*请选择你的补寄发票原因</span>');
			return false;
		}
		if ($("#repairExplain").val().length > 500) {
			$('.rm').remove();
			$("#resendReceiptForm .sm-con").append('<span class="rm" style="color:red">原因说明应少于500字</span>');
			return false;
		}
		return true;
	}
	
	function giftProblemCheck() {
		//赠品类型
		typeFlage = verifyEx('input[name="giftType"]')==true?true:false;
		if(typeFlage==false){
			$('.giftTypeRadio+span').remove();
			$('.giftTypeRadio').after('<span class="tc-title">*请选择你购买商品的赠品类型</span>');
			return false;
		}
		//赠品原因
		flage = verify('giftProblem')==true?true:false;
		if(flage==false){
			$('.giftResn .radio-list+span').remove();
			$('.giftResn .radio-list').after('<span class="tc-title">*请选择你的赠品问题申请原因</span>');
			return false;
		}
		//赠品说明
		if ($("#giftProblemExplain").val().length > 500) {
			$('.rm').remove();
			$("#giftProblemForm .sm-con").append('<span class="rm" style="color:red">原因说明应少于500字</span>');
			return false;
		}
		return true;
	}

	function complaintCheck() {
		flage = verify('complaint')==true?true:false;
		if(flage==false){
			$('#complaintForm .radio-list+span').remove();	
			$('#complaintForm .radio-list').after('<span class="tc-title">*请选择你的投诉问题申请原因</span>');
			return false;
		}
		if ($("#complaintExplain").val().length > 500) {
			$('.rm').remove();
			$("#complaintForm .sm-con").append('<span class="rm" style="color:red">原因说明应少于500字</span>');
			return false;
		}
		return true;
	}

	/**
	 * 选择服务div
	 */
	function selectDiv(serverName) {
		$(".tab-pane").removeClass("active");
		if (serverName === "repair") {
			$("#repairDiv").addClass("active")
			initData("4", serverName);
		}
		if (serverName === "resendReceipt") {
			$("#resendReceiptDiv").addClass("active");
			initData("5", serverName);
		}
		if (serverName === "giftProblem") {
			$("#giftProblemDiv").addClass("active");
			//initData("9", serverName);
		}
		if (serverName === "complaint") {
			$("#complaintDiv").addClass("active");
			initData("10", serverName);
		}
	}

	/**
	 * 初始化div
	 */
	function initDivStyle(asServerTypeId, serverName, result) {
		var liE = "";
		if (asServerTypeId === "4") {
			$("#repairDiv ul.radio-list").empty();
			for (var i = 0; i < result.length; i++) {
				liE = $("<li><label class='tabcon-radio'><input type='radio' name='repairReason' class='radio' value='"+result[i].aftersaleReplyReason+"'/>"
						+ result[i].aftersaleReplyReason + "</label></li>");
				$("#repairDiv ul.radio-list").append(liE);
			}
			radioActiveDeal();
		} else if (asServerTypeId == "5") {
			$("#resendReceiptDiv ul.radio-list").empty();
			for (var i = 0; i < result.length; i++) {
				liE = $("<li><label class='tabcon-radio'><input type='radio' name='resendReceiptReason' class='radio' value='"+result[i].aftersaleReplyReason+"'/>"
						+ result[i].aftersaleReplyReason + "</label></li>");
				$("#resendReceiptDiv ul.radio-list").append(liE);
			}
			radioActiveDeal();
		}else if (asServerTypeId == "10") {
			$("#complaintDiv ul.radio-list").empty();
			for (var i = 0; i < result.length; i++) {
				liE = $("<li><label class='tabcon-radio'><input type='radio' name='complaintReason' class='radio'  value='"+result[i].aftersaleReplyReason+"'/>"
						+ result[i].aftersaleReplyReason + "</label></li>");
				$("#complaintDiv ul.radio-list").append(liE);
			}
			radioActiveDeal();
		}
		//赠品问题特殊处理
		else {
			$("#giftProblemDiv .giftResn").empty();
			liE = '<div class="sm-sic giftResn"><span class="ty-st">赠品原因：</span><div class="tc-radio sm-radio"><ul class="radio-list">';
			for (var i = 0; i < result.length; i++) {
				liE = liE+"<li><label class='tabcon-radio'><input type='radio' name='giftProblemReason'  class='radio' value='"+result[i].aftersaleReplyReason+"'/>"
					+ result[i].aftersaleReplyReason + "</label></li>";
			}
			liE = liE+'</ul> </div> </div>'
			$("#giftProblemDiv .giftResn").append(liE);
			$(".giftResn .radio").click(function(){
				$(".giftResn .radio").removeClass("active");
				$(this).addClass("active");
				$(".giftResn .radio").removeAttr("checked");
				$(this).attr("checked", "checked");
			});
		}
	}
	function radioActiveDeal(){
		//注册点击事件
		$(".radio").click(function(){
			$(".radio").removeClass("active");
			$(this).addClass("active");
			$(".radio").removeAttr("checked");
			$(this).attr("checked", "checked");
		});
	}
	function radioGiftDeal(){

	}
	//赠品问题类型单选按钮
	$(function(){
		$('.giftTypeRadio .radio').on('click',function(){
			var slctor = '.giftTypeRadio .radio';
			$(slctor).removeClass("active");
			$(this).addClass("active");
			$(slctor).removeAttr("checked");
			$(this).attr("checked", "checked");
			var typeId = $(this).val();
			initData(typeId,"giftProblem");
		});
	});
