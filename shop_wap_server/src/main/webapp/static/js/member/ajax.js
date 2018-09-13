/**
 * AJAX 请求
 */
var ajaxRequest = function(url, callback, formId, data, needAjaxLoadingCanvas) {
	if(needAjaxLoadingCanvas != false) {
		showAjaxLoading();
	}

	var ajaxData = "";
	if(isNull(data)) {
		formId = formId || "fm";
		var form = $("#" + formId);
		if(form.length > 0) {
			ajaxData = form.serialize();
		}
	} else {
		ajaxData = jQuery.param(data, true);
	}
	$.ajax({
		url:url,
		cache:false,
		type:"POST",
		data:ajaxData,
		success:function(data) {
			if(!interceptServerResponseException(data)) {
				callback(data);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			showAlert("请求失败！", "错误");
		},
		complete:function(XMLHttpRequest, textStatus) {
			hideAjaxLoading();
		}
	});
};

/**
 * 显示ajax请求等待响应效果。
 */
function showAjaxLoading() {
	var canvas = $("#AJAX_LOADING_CANVAS");
	if (canvas.length == 0) {
		canvas = createAjaxLoadingCanvas();
	}
	canvas.show();
	canvas.fadeTo("slow", 0.66);
}

/**
 * 创建ajax请求等待响应效果显示幕布。
 * 
 * @returns 显示效果的DIV。
 */
function createAjaxLoadingCanvas() {
	var doc = $(document);
	var canvas = $("<div>");
	canvas.attr("id", "AJAX_LOADING_CANVAS");
	canvas.css({
		position : "absolute",
		left : "0px",
		top : "0px",
		"margin-top" : "0px",
		"z-index" : "888",
		background : "rgb(240, 240, 240)",
		"background-image" : "url(../../img/member/loading.gif)",
		"background-repeat" : "no-repeat",
		"background-position" : "center"
	});
	canvas.width(doc.width());
	canvas.height(doc.height());
	canvas.fadeTo(0, 0.66);
	canvas.hide();

	$("body").append(canvas);
	return canvas;
}

/**
 * 隐藏ajax请求等待响应效果。
 */
function hideAjaxLoading() {
	var canvas = $("#AJAX_LOADING_CANVAS");
	canvas.stop();
	canvas.fadeTo(0, 0.66, function() {
		canvas.hide();
	});
}

/**
 * 拦截服务器返回的异常信息，如果存在异常信息，则向用户展示相关异常描述。
 * @param data 服务器返回的数据
 * @returns 如果服务器返回了异常信息，则返回true，否则返回false。
 */
function interceptServerResponseException(data) {
	var e = data._error;
	if(!isNull(e)) {
		showAlert(e.msg, "错误");
		return true;
	} else {
		return false;
	}
}
