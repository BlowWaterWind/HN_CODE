/**
 * 查询宽带
 */
function queryAccount(){
	 if($.trim($("#num").val()).length == 0){
     	showAlert("请输入宽带账号进行查询！");
         return;
     }
     $("#broadBandQueryForm").submit();
}

/**
 * 绑定宽带
 */
function broadbandBind(){
	if($.trim($("#broadbandAccount").text()).length == 0){
		$("#message").text("没有查询到宽带账号，不能进行绑定！");
		return;
	}
	var param = $("#renewForm").serializeObject();
	var url = "bindBrodbandAccount";
	$.post(url,param,function(data){
        if(data.code=="0"){
        	$("#message").text("绑定成功!");
        }
        else {
        	$("#message").text(data.message);
        }
		
     });	
}


/**
 * 宽带续费(已登录)
 * @param sn
 * @param cityCode
 */
function broadBandRenew(broadbandAccount,cityCode){
	showLoadPop();
	$("#renewForm input[name='broadbandAccount']").val(broadbandAccount);
    $("#renewForm input[name='cityCode']").val(cityCode);
    $("#renewForm input[name='phoneNum']").val($("#txt_phoneNum").val());
    $("#renewForm").attr("action","linkToBroadBandRenew");
    $("#renewForm").submit();
}





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