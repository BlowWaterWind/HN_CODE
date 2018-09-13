

var sys_second=0;
var sys_second_end=0;
var activityId=100024795323;



	function getGoodsActivity1() {
		$.ajax({
			url :"/act/info/get?activityId="+activityId+"&v="+new Date().getTime(),
			success : function(data) {
		 	//var	data='{"activityStock":"88","status":"0","isLogin":"true","serialNumber":"13111111111","svtime":"1480595456336","startDate":"1480595436336","endDate":"1480595466336"}'
				data = JSON.parse(data); 
		 	
				if (data.status=="0") {
					 if(data.isLogin=="false"){
						window.location.href="/shop/login/toLogin?specialRef=http://wap.hn.10086.cn/act/page/shop20161111/index.html";
						// $("#loginBackMask").show();
						 //$("#loginAddpop").show();
					}
					 $("#secondsKillBtn").show();
					//系统当前时间
					var svtimeNum = parseInt(data.svtime);
					//活动开始时间
					var startDateNum = parseInt(data.startDate);
					//活动结束时间
					var endDateNum = parseInt(data.endDate);
					
					$("#b_time").html(UnixToDate(startDateNum));
					$("#e_time").html(UnixToDate(endDateNum));
					
					if (svtimeNum > endDateNum)
					{
						$("#statusBtn").removeClass("hide");
						$("#secondsKillBtn").hide();
						
					} //秒杀中
					else if (svtimeNum >= startDateNum && svtimeNum <= endDateNum)
					{
						$("#statusBtn").hide();
						$("#secondsKillBtn").removeClass("hide");
					}
					else
					{
						$("#statusBtn").removeClass("hide");
						$("#secondsKillBtn").hide();
						$("#statusBtn").html("等待秒杀");
						
					}
					
					$("#kkNum").html(data.activityStock);
				
				}

			}
		})
	}
	 
	/**              
     * 时间戳转换日期              
     * @param <int> unixTime    待时间戳(秒)              
     * @param <bool> isFull    返回完整时间(Y-m-d 或者 Y-m-d H:i:s)              
     * @param <int>  timeZone   时区              
     */
	  function UnixToDate(unixTime) {
         var time = new Date(unixTime);
         var ymdhis = "";
         ymdhis += time.getUTCFullYear() + "-";
         ymdhis += (time.getUTCMonth()+1) + "-";
         ymdhis += time.getUTCDate();
             ymdhis += " " + time.getHours() + ":";
             ymdhis += time.getMinutes() + ":";
             ymdhis += time.getSeconds();
         return ymdhis;
     }






$(function(){
	getGoodsActivity1();

	var secondsKillFunction = function(){

				$.ajax({
					url: "/act/seckill?activityId="+activityId+"&v="+new Date().getTime(),
					success: function (data) {
	//	var data='{"status":"5","message":"sfsf"}';
						data = JSON.parse(data); 
						if(data.status=="5"){
							
							$("#goodsBuyForm").submit();
						}else if(data.status=="1"){
							windows.location.href=baseProject+"login/toLogin";
						}else if(data.status=="2"){
							showAlert(data.message);
						}else if(data.status=="3"||data.status=="0"){
							showAlert(data.message);
						}
					}
				});
	}  
	
	/*立即秒杀*/
	$("#secondsKillBtn").on("click",function(){
		secondsKillFunction();
	});


	function getRootPath() {
		// 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
		var curWwwPath = window.document.location.href;
		// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
		var pathName = window.document.location.pathname;
		var pos = curWwwPath.indexOf(pathName);
		// 获取主机地址，如： http://localhost:8083
		var localhostPaht = curWwwPath.substring(0, pos);
		// 获取带"/"的项目名，如：/uimcardprj
		var projectName = pathName
				.substring(0, pathName.substr(1).indexOf('/') + 1);
		return (localhostPaht);
	}

	

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