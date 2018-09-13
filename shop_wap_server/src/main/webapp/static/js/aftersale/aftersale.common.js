//图片预览处理
var imgIndex = 0;
function addImg(idName) {
	imgIndex = imgIndex + 1;
	var data = {
		'index' : imgIndex
	};
	//使模板刷在Form里面
	$("#imgPreView" + idName).append(template("uploadImgTmpl", data));
	var id = "#up" + imgIndex;
	$(id).click();
	$(id).uploadPreview({
		Img : "ImgPr" + imgIndex,
		Width : 40,
		Height : 40
	});
}
function deleteImg(imgIndex) {
	$("#ImgPr" + imgIndex).remove();
	$("#up" + imgIndex).remove();
}

//后台异步请求服务原因数据
function initData(asServerTypeId, serverName) {
	$.ajax({
		type : "post",
		url : "initData",
		data : {
			"asServerTypeId" : asServerTypeId
		},
		dataType : "json",
		error : function(data) {
			alert("数据异常");
		},
		success : function(result) {
			initDivStyle(asServerTypeId, serverName, result);
		}
	});
}

//wap端单选按钮验证
function verify(serverName) {
	var theCheckboxInputs = $('input[name='+serverName+'Reason]');
	for (var i = 0; i < theCheckboxInputs.length; i++) {
		if (theCheckboxInputs[i].checked)
			return true;
	}
	return false;
}

//扩展：wap端单选按钮验证
function verifyEx(slctor) {
	var theCheckboxInputs = $(slctor);
	for (var i = 0; i < theCheckboxInputs.length; i++) {
		if (theCheckboxInputs[i].checked)
			return true;
	}
	return false;
}


