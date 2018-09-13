var sHeight= (window.innerHeight > 0) ? window.innerHeight : screen.Height;
$('#content').height(sHeight - 90);
var myScroll;
var pullDownEl, pullDownL;
var pullUpEl, pullUpL;
var Downcount = 0, Upcount = 0;
var loadStep = 0;//加载状态0默认，1显示加载状态，2执行加载数据，只有当为0时才能再次加载，这是防止过快拉动刷新  

function pullDownAction() {//下拉事件  
    setTimeout(function() {
        $('div.tu-list').html('');
        searchOrder("pullDown");

        pullDownEl.removeClass('load');
        pullDownL.html('下拉显示更多...');
        pullDownEl['class'] = pullDownEl.attr('class');
        pullDownEl.attr('class', '').hide();
        myScroll.refresh();
        loadStep = 0;
    }, 1000);  //1秒  
}
function pullUpAction() {//上拉事件
    var lastPage = $("#lastPage").val();
    if(lastPage == "true"){
        $('div.pullUpLabel').html('已到最后一页...');
        return;
    } 
    searchOrder("pullUp");
}

function loaded() {
    pullDownEl = $('#pullDown');
    pullDownL = pullDownEl.find('.pullDownLabel');
    pullDownEl['class'] = pullDownEl.attr('class');
    pullDownEl.attr('class', '').hide();

    pullUpEl = $('#pullUp');
    pullUpL = pullUpEl.find('.pullUpLabel');
    pullUpEl['class'] = pullUpEl.attr('class');
    pullUpEl.attr('class', '').hide();

    myScroll = new IScroll("#content", {
        probeType : 2,//probeType：1对性能没有影响。在滚动事件被触发时，滚动轴是不是忙着做它的东西。probeType：2总执行滚动，除了势头，反弹过程中的事件。这类似于原生的onscroll事件。probeType：3发出的滚动事件与到的像素精度。注意，滚动被迫requestAnimationFrame（即：useTransition：假）。  
        scrollbars : true,//有滚动条  
        mouseWheel : true,//允许滑轮滚动  
        fadeScrollbars : true,//滚动时显示滚动条，默认影藏，并且是淡出淡入效果  
        bounce : true,//边界反弹  
        interactiveScrollbars : true,//滚动条可以拖动  
        shrinkScrollbars : 'scale',// 当滚动边界之外的滚动条是由少量的收缩。'clip' or 'scale'.  
        click : true,// 允许点击事件  
        keyBindings : true,//允许使用按键控制  
        momentum : true
        // 允许有惯性滑动  
    });
    //滚动时  
    myScroll.on('scroll', function() {
        if (loadStep == 0
            && !pullDownEl.attr('class').match('flip|load')
            && !pullUpEl.attr('class').match('flip|load')) {
            if (this.y > 5) {
                //下拉刷新效果  
                pullDownEl.attr('class', pullUpEl['class'])
                pullDownEl.show();
                myScroll.refresh();
                pullDownEl.addClass('flip');
                pullDownL.html('准备刷新...');
                loadStep = 1;
            } else if (this.y < (this.maxScrollY - 5)) {
                //上拉刷新效果  
                pullUpEl.attr('class', pullUpEl['class'])
                pullUpEl.show();
                myScroll.refresh();
                pullUpEl.addClass('flip');
                pullUpL.html('准备刷新...');
                loadStep = 1;
            }
        }
    });
    //滚动完毕  
    myScroll.on('scrollEnd', function() {
        if (loadStep == 1) {
            if (pullUpEl.attr('class').match('flip|load')) {
                pullUpEl.removeClass('flip').addClass('load');
                pullUpL.html('加载中...');
                loadStep = 2;

                pullUpAction();
            } else if (pullDownEl.attr('class').match('flip|load')) {
                pullDownEl.removeClass('flip').addClass('load');
                pullDownL.html('加载中...');
                loadStep = 2;
                pullDownAction();
            }
        }
    });
}

document.addEventListener('touchmove', function(e) {
    e.preventDefault();
}, false);
document.addEventListener('DOMContentLoaded', function(){
	setTimeout(loaded, 200);
}, false);

/**
 * 下拉刷新订单数据
 * @param pullType
 */
function searchOrder(pullType){
	var url = basePath + "/myOrder/queryMyOrderByPage";
	var pageNo = $("#pageNo").val();
	var pageSize = $("#pageSize").val();
	var flag = $("#flag").val();
	if(pullType == "pullUp"){
		pageNo++;
		$("#pageNo").val(pageNo);
	}
	
	var param = {'pageNo':pageNo, 'pageSize':pageSize,'flag':flag};
	
	$.post(
			url, 
			param, 
			function(data) {
				if(pullType == "pullDown") {
					$("#pageDiv").empty();
				}
				
				if (data) {
					for(var i = 0; i < data.list.length; i++) {
						var orderView = data.list[i];
						
						var str0 = "<ul style='display:block;' class='cur-ul'>" +
					    "<li class='cur-li'>" +
					    "<p class='tabcon-cl'><!--<span class='tabcon-icon'><input type='checkbox'/></span>--><span class='pull-left pull-jl'>" +
					    " <a class='orderDetailBtn' href='javascript:;' data-index='" + orderView.orderSubId + "'><span class='dd-number'>订单号：</span>"+ orderView.orderSubNo + "</a></span>" +
					    "<span class='pull-right pull-cl'>" + orderView.orderStatus.orderStatusName + "</span></p>";
						var flg = false;
						var str1 = "";
						if (orderView.detailList) {
							flg = true;
							for (var j = 0; j < orderView.detailList.length; j++) {
								var order = orderView.detailList[j];
								
								// 判断是否有图片
								var imageStr;
								if (order.goodsImgUrl) {
									imageStr = tfsUrl + order.goodsImgUrl;
								} else {
									imageStr = ctx + "static/images/default.jpg";
								}
								
								var s = "<div class='tabcon-lr tabcon-cl02'>" +
								"<a class='orderDetailBtn' href='javascript:;' data-index='" + order.orderSubId + "'>" + 
								"<dl>" + 
								"<dt><a href='" + gUrl + order.goodsUrl + "'><img src='" + imageStr + "'/></a></dt>" +
								"<dd>" + 
								"<h2><a href='" + gUrl + order.goodsUrl + "'>" + order.goodsName + "</a></h2>" + 
								"<span class='tab-zh'>" + order.goodsFormat + "</span>";
								if(orderView.shopId==100000002099) {
									s+=" <p><font color='red'> 此订单为10085订单</font></p>";
								}
								s+="</dd>" +
								"<div class='dy'>" + 
								"<p class='tabcon-cl tabcon-cl03'>" + Math.round(order.goodsSkuPrice * 10000 / 100) / 10000.0 + "</p>" +
								"<p class='tabcon-cl tabcon-cl03 tab-lr'>×" + order.goodsSkuNum + "</p>" +
								"</div>" +
								"</dl>" +
								"</a>" +
								"</div>";
								
								var s0 = "<p class='tabcon-cl tabcon-cl02 tabcon-cl04'/>";
								
								var s1 = "";
								if (flag != 3) {
									for (var key in orderView.actionMap) {
										var s2="";
										<!--判断是否是85商品 -->
										if(orderView.shopId!=100000002099){
											s2 = "<p>" +
												"<a class='pull-right btn btn-bd btn-sm' href='"
												+ ctx + "myOrder/process?orderSubId=" + orderView.orderSubId + "&subOrderDetailId=" + order.orderSubDetailId + "&action=" + orderView.actionMap[key].id + "&act=" + orderView.actionMap[key].act +
												"' target='_self'>" + orderView.actionMap[key].name + "</a>" +
												"</p>";
										}else{
											if(orderView.actionMap[key].name=='支付'){
												s2 = "<p>" +
													"<a class='pull-right btn btn-bd btn-sm' href='http://www.10085.cn/web85/page/zyzxpay/wap_order.html?orderId="+orderView.thirdPartyId+"' target='_self'>" + orderView.actionMap[key].name + "</a>" +
													"</p>";
											}
										}
										s1 += s2;
									}
								} else {
									s1 = "<p><a  data-index='" + order.orderSubDetailId + "' class='pull-right btn btn-bd btn-sm commentOrderBtn'>订单评价</a></p>" ;
									<!--判断是否是85商品 -->
									if(orderView.shopId!=100000002099){
										s1+="<p><a  data-index='" + orderView.orderSubId + "'  data-logisticsNum='"+ orderView.logisticsNum  +"' class='pull-right btn btn-bd btn-sm mr10 logisticBtn'>查看物流</a></p>";
									}else{
										s1+= "<a class='btn btn-sm btn-bd pull-right logisticBtn' href='"+ctx+"myOrder/to10085LogisticDetail?orderId="+orderView.orderSubId+"&dsOrderId="+orderView.thirdPartyId+"'>查看物流</a>";
									}

								}
								
								
							}
							
							str1 += (s + s0 + s1);
						}
						
						var str2;
						if (flag == 2) {
							<!--判断是否是85商品 -->
							if(orderView.shopId!=100000002099){
								str2 = "<button class='btn btn-sm btn-bd pull-right logisticBtn'  data-logisticsNum='"+ orderView.logisticsNum  +"' data-index=" + orderView.logisticsNum + " >查看物流</button>";
							}else{
								str2 = "<a class='btn btn-sm btn-bd pull-right logisticBtn' href='"+ctx+"myOrder/to10085LogisticDetail?orderId="+orderView.orderSubId+"&dsOrderId="+orderView.thirdPartyId+"'>查看物流</a>";
							}

						} else if (flag == 1) {
							str2 = "<a href='#' class='pull-right btn btn-bd btn-sm mr10 sendGoodsWarnBtn' data-index='" + orderView.orderSubId + "'>发货提醒</a>";
						} else if (flag == 3) {
							str2 = "";
						}
						
						var count = 0;
						if (flg) {
							count = orderView.detailList.length;
						}
						
						var orderTime = new Date();
					    orderTime.setTime(orderView.orderTime);
						str1 = str1 + str2 + 
						       "<span class='sm-date pull-right font-gray'>"+ orderTime.format("yyyy-MM-dd hh:mm:ss") +"</span>" +
						       "<span class='pull-left pull-gd'><strong>共" + count + "件商品</strong><b><em>实付:</em>￥" + (Math.round(orderView.orderSubPayAmount * 10000 / 100) / 10000.0) + "</b></span></p>" +
						       "</li></ul>";
						//console.log(str1);
						var $li = $(str0 + str1);
						
					    $("#pageDiv").append($li);
					    
					    pullUpEl.removeClass('load');
				        pullUpL.html('上拉显示更多...');
				        pullUpEl['class'] = pullUpEl.attr('class');
				        pullUpEl.attr('class', '').hide();
				        myScroll.refresh();
				        loadStep = 0;
					}
					
					$("#pageNo").val(data.pageNo);
					var pageNo = parseInt(data.pageNo);
			        var pageSize = parseInt(data.pageSize);
			
			        var count = parseInt(data.count);
			        if(pageNo * pageSize >= count) {
			            $("#lastPage").val('true');
			        } else {
			            $("#lastPage").val('false');
			        }
				}
				
				
	});
	
	myScroll.refresh();
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

Date.prototype.format = function(format) { 
	var o = { 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S" : this.getMilliseconds() //millisecond 
	} 

	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
} 