$(function(){
	// 登录判断
	var url = baseProject + "wb/isLogin";
	$.post(url,JSON.stringify({}),function(data){
		// 已登录
		if(data.isLogin == 0){
			
		}else{
		//	window.location.href = baseProject + "login/toLogin";
		}
	});
	
	
});