

var sys_second=0;
var sys_second_end=0;
//剩余天
var rday = 0;
//剩余小时
var rhour = 0;
//剩余分钟
var rminutes = 0;
//剩余秒
var rseconds = 0;
//剩余毫秒
var rmillSeconds = 0;
	function getGoodsActivity1() {
		$.ajax({
			url :"/act/info/get?activityId="+activityId+"&v="+new Date().getTime(),
			success : function(data) {
		 	//var	data='{"activityStock":"88","status":"0","isLogin":"true","serialNumber":"13111111111","svtime":"1480595456336","startDate":"1480595436336","endDate":"1480595466336"}'
				data = JSON.parse(data);
				if (data.status=="0") {
					 if(data.isLogin=="false"){
						 var href = window.location.href;
						window.location.href="/shop/login/toLogin?specialRef="+href;
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
						$("#statusBtn").html("已结束");
						$("#statusBtn").show();
						$("#secondsKillBtn").hide();

					} //秒杀中
					else if (svtimeNum >= startDateNum && svtimeNum <= endDateNum)
					{
						$("#statusBtn").hide();
						$("#secondsKillBtn").show();
						$(".djs-fj").html("距秒杀开始还有:0天0小时0分0秒");
					}
					else
					{
						$("#statusBtn").show();
						$("#secondsKillBtn").hide();
						$("#statusBtn").html("等待秒杀");
						//剩余时间
						var remainTime = startDateNum - svtimeNum;
						rday = Math.floor(remainTime/1000/60/60/24);
						rhour = Math.floor(remainTime/1000/60/60);
						rminutes = Math.floor((remainTime - (rhour*60*60*1000))/1000/60);
						rseconds = Math.floor((remainTime - (rhour*60*60*1000) - (rminutes*60*1000))/1000);
						rmillSeconds =  remainTime - (rhour*60*60*1000) - (rminutes*60*1000) - (rseconds*1000);

						$(".djs-fj").html("距秒杀开始还有:"+rday+"天"+rhour+"小时"+rminutes+"分"+rseconds+"秒");
						//剩余时间倒计时
						window.setTimeout(changeTime, 1000 + rmillSeconds);
					}
					
					$("#kkNum").html(data.activityStock);
				
				}

			}
		})
	}
	function changeTime(){

		//秒杀可以开始
		if (rday == 0 && rhour == 0 && rminutes == 0 && rseconds == 0)
		{
			$("#statusBtn").hide();
			$("#secondsKillBtn").show();
			$(".djs-fj").html("距秒杀开始还有:0天0小时0分0秒");
			return;
		}

		//rseconds大于0时隔30秒矫正一次时间
		if (rseconds>0 && rseconds%30==0)
		{
			getGoodsActivity1();
			return;
		}

		if (rseconds == 0)
		{
			rseconds = 59;

			if (rminutes == 0)
			{
				rminutes = 59;

				if (rhour == 0)
				{
					rhour = 23;
					if(rday == 0){
						rday = 0;
					}
					else
					{
						rday = rday -1;
					}
				}
				else
				{
					rhour = rhour -1;
				}
			}
			else
			{
				rminutes = rminutes -1;
			}
		}
		else
		{
			rseconds = rseconds - 1;
		}

		$(".djs-fj").html("距秒杀开始还有:"+rday+"天"+rhour+"小时"+rminutes+"分"+rseconds+"秒");
		//剩余时间倒计时
		window.setTimeout(changeTime, 1000);
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
	var attrs_ul=$("ul[id^='attrUl_']");
	var color = attrs_ul.attr("attrName")+"="+$("li[id^='attrLi_']").find('a').html().trim();
	var attrId = $("li[id^='attrLi_']").find('a').attr("prodAttrId");
	$("input[name='userGoodsCarList[0].goodsStandardAttr']").val(color);//设置商品属性
	$("input[name='userGoodsCarList[0].goodsStandardAttrId']").val(attrId+"="+attrId);//设置商品属性
	getGoodsActivity1();

	var secondsKillFunction = function(){
		showLoadPop("正在秒杀中...");
				$.ajax({
					url: "/act/seckill?activityId="+activityId+"&v="+new Date().getTime(),
					success: function (data) {
	//	var data='{"status":"5","message":"sfsf"}';
						data = JSON.parse(data); 
						if(data.status=="5"){
							hideLoadPop();
							$("#goodsBuyForm").submit();
						}else if(data.status=="1"){
							hideLoadPop();
							windows.location.href=baseProject+"login/toLogin";
						}else if(data.status=="2"){
							hideLoadPop();
							showAlert(data.message);
						}else if(data.status=="3"||data.status=="0"){
							hideLoadPop();
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