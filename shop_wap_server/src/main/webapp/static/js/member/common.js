String.prototype.format = function(value) {
	var result = this;
	if(this.indexOf("{") == -1 || this.indexOf("}") == -1) {
		return String(this);
	}
	if(typeof value == "undefined" || value == null || value == "null") {
		value = "";
	}
	var type = typeof value;
	if("string" === type || "number" === type || "boolean" == type) {
		value = String(value);
		result = this.replace(/\{0\}/g, value);
	} else if (value instanceof Array) {
		jQuery.each(value, function(i, v) {
			var regText = "\\{" + String(i) + "\\}";
			var regex = new RegExp(regText, "g");
			if(typeof v == "undefined" || v == null|| v == "null") {
				v = "";
			}
			result =  result.replace(regex, v);
		});
	} else {
		for(var key in value) {
			var v = value[key];
			if(typeof v == "undefined" || v == null || v == "null") {
				v = "";
			}
			if("string" === (typeof v) || "number" === (typeof v) || "boolean" === (typeof v)) {
				var regText = "\\{" + String(key) + "\\}";
				var regex = new RegExp(regText, "g");
				result = result.replace(regex, v);
			}
		}
	}
	return result;
};

Date.prototype.format = function(format){
	var o = {
	"M+" : this.getMonth()+1, // month
	"d+" : this.getDate(), // day
	"h+" : this.getHours(), // hour
	"m+" : this.getMinutes(), // minute
	"s+" : this.getSeconds(), // second
	"q+" : Math.floor((this.getMonth()+3)/3), // quarter
	"S" : this.getMilliseconds() // millisecond
	};
	
	format = format || "yyyy-MM-dd";
	if(/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	}

	for(var k in o) {
		if(new RegExp("("+ k +")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		}
	}
	return format; 
};
/**
 * 是否闰年
 * */
Date.prototype.isLeapYear = function(){
	var year = this.getFullYear();
	return (year%4 === 0) && (year%100 !==0) || (year%400 === 0);
};

/**
 * 得到当月有多少天
 * */
Date.prototype.getMaxMonthDay = function(){
	switch(this.getMonth()+1){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
				return 31;
		case 2:
				return this.isLeapYear() ? 29 :28;
		default :
				return 30;
	};
};

Date.prototype.getDayOfYear = function(){
	var simpleFormatDate = new Date(this.format().replace(/(-|\\)/g,"/"))- new Date(this.getFullYear()+"/01/01");
	return simpleFormatDate/(1000*3600*24);
};

Date.prototype.addDays = function(num){
	var returnDate = new Date(this.valueOf()+(1000*3600*24*num));
	return returnDate;
};

function validateForm(fm){
	var count = 0;
	$("#"+fm).find("[required='required']").each(function(){
		var $this = $(this);
		var offset = $this.offset();
		if(!$this.val() && count < 3){
			if(count === 0){
				window.scrollTo(0,offset.top);
			}
			count++;
			$this.popover({
				content:"不能为空",
				placement:"bottom",
				trigger:"manual",
				delay:{
					"show":600,
					"hide":600
				}
			}).popover("show");
			window.setTimeout(function(){
				$this.popover("hide");
			},1000);
		}
		if(count > 3){
			return false;
		}
	});
	return count === 0;
}

//DMS 导出表单提交，由于现在没有找到jquery的解决办法，现在先通过W3C的标准实现
function dmsExportSubmit(url,form){
	var formId = form||"fm";
	var currDate = new Date();
	var url2 = (url+(url.lastIndexOf("?")!=-1?"&xxx="+currDate.getMilliseconds():"?xxx="+currDate.getMilliseconds()));
	var formElement = document.getElementById(formId);
	if(!formElement){
		formElement = document.createElement("form");
		document.body.appendChild(formElement); //IE下需要加入到DOM中
	}
	formElement.action = (url2);
	formElement.submit();
}