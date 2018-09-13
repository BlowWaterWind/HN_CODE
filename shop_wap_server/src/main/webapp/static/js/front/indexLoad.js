$(function(){
    try {
		//商城首页增加头部显示,根据url参数中的isShowHead值进行判断是否显示
		var isShowHead = getQueryString("isShowHead");
		if(isShowHead != null && isShowHead.toString() == '1')
		{
            showHeadAndHideBottom();

			//将isShowHead 保存至cookie，以便其他页面能够使用
			document.cookie = "isShowHead=1;path=/";
		}else{
			// 判断cookie中是否有isShowHead
			var isShowHeadVal = getCookie("isShowHead");
            if(isShowHeadVal != null && isShowHeadVal == "1"){
                showHeadAndHideBottom();
            }
		}
	} catch(e) {}
	// 登录判断
	var url = baseProject + "indexLoad/isLogin";
	var params = {'isLogin':"false"};
	$.post(url,params,function(data){
		if(data.isLogin == "0" )
		{
			$("#index_login").attr("href",baseProject+"login/logout");
			$("#index_login").html("<i class='icon-zc'></i> 退出");
			$("#sp_login").html("退出");
		}
	})
	
	$("#sp_login").on('click',function(){
		var  style = $("#ul_sp_login").attr("style");
		if(style == "display:block;" )
		{
			style = $("#ul_sp_login").attr("style","display:none;");
		}else{
			style = $("#ul_sp_login").attr("style","display:block;");
		}
		
	});
	$(window).scroll(function(){
		style = $("#ul_sp_login").attr("style","display:none;")
		style = $("#ul_sp_side").attr("style","display:none;")
		style = $("#navigationRightList").attr("style","display:none;")
	})

    notice();
	
});

/**
 * 获取url中的参数
 * @param name
 * @returns {null}
 */
function getQueryString(name) {
	var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]);
	}
	return null;
}

/**
 * 根据name的值获取cookie
 * @param name
 * @returns {null}
 */
function getCookie(name) {
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg)){
        return unescape(arr[2]);
	}
    else{
        return null;
	}
}

/**
 * 显示头部，隐藏底部菜单栏
 */
function showHeadAndHideBottom() {
    //添加头部
    var divObj = document.createElement("div");
    divObj.setAttribute("class", "top container")//添加样式
    divObj.innerHTML = "<div class=\"header sub-title\"><a class=\"icon-left\" href=\"javascript:void(0);\" onclick=\"window.history.back()\"></a><h1>购机</h1></div>";
    var first = document.body.firstChild;//得到页面的第一个元素
    document.body.insertBefore(divObj, first);//在得到的第一个元素之前插入

    //隐藏底部菜单栏
    $('.footer-wrap').hide();
}
/**
 * 公告
 */
function notice() {
	var html='<div class="pf-tc">'+
        '<div class="dt-mask"></div>'+
        '<div class="container dt-list">'+
        '<a href="#" class="dt-close" onclick="$(\'.pf-tc\').addClass(\'hide\')"><img src="/shop/static/images/wt-images/close_btn.png" /></a>'+
        '<h2 style="font-size:0.55rem;">公告</h2>'+
        '<div class="dt-text">'+
        '<p class="dt-2em" style="font-size:0.35rem;text-indent:0rem">尊敬的客户：</p>'+
    '<p class="dt-2em" style="font-size:0.35rem;">为提高服务质量，我公司将于3月22日22：00至23日7：00进行系统升级工作，' +
	'期间将影响您查询和办理业务，建议您在3月23日7：00以后进行操作，由此给您带来的不便敬请谅解。</p>'+
    '<p class="dt-sm" style="font-size:0.35rem;">中国移动通信集团湖南有限公司 </p>'+
        '<p class="dt-sm" style="font-size:0.35rem;">2018年3月22日 </p>'+
    '</div>'+
    '<div class="footerbg"></div>'+
        '</div>'+
        '</div>';
	// $(".footer-wrap").append(html);
}