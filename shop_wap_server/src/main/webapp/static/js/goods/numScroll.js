var sHeight= (window.innerHeight > 0) ? window.innerHeight : screen.Height;
$('#content').height(sHeight - 90);
$('#masterdiv').height(sHeight - 90);
var myScroll;
var pullDownEl, pullDownL;
var pullUpEl, pullUpL;
var Downcount = 0, Upcount = 0;
var loadStep = 0;//加载状态0默认，1显示加载状态，2执行加载数据，只有当为0时才能再次加载，这是防止过快拉动刷新  

var flag = false;
function pullDownAction() {//下拉事件  
    setTimeout(function() {
        $('div.tu-list').html('');
        queryNum("pullDown");

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
    queryNum("pullUp");
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
 * 下拉查询数据
 * @param pullType
 */
function queryNum(pullType) {
	
	if ($("#numListUl li").length == 0) {
		return;
	} else {
		var pageNo = $("#pageNo").val();
		if (pullType == 'pullUp') {
			pageNo++;
			$("#pageNo").val(pageNo);
		}
	}
	
	var url = ctx + "/goodsQuery/linkToNumByPage";
	$.post(
			url,
			$("#queryNumForm").serializeObject(),
			function(data){
				if (pullType == 'pullDown') {
					$("#numListUl").empty();
				}
				
				if (data) {
					for (var i = 0; i < data.list.length; i++) {
						var netNum = data.list[i];
						var $li = "<li>\n" +
						          "<h4>" + netNum.num.substring(0, 7) + "<b>" + netNum.num.substring(7) + "</b></h4>" +
						          "<div class='hk-tit'>" + 
						          "<span>预存话费：<strong>100.00</strong></span>" + // TODO先默认设为10000分
						          "<span>地市：<strong>" + netNum.cityCode +"</strong></span>" +
						          "</div>" +
						          "<input type='hidden' name='netNum[" + i + "].num' value='" + netNum.num + "'/>" +
						          "<input type='hidden' name='netNum[" + i + "].guaranteedFee' value='" + 10000 + "'/>" + //TODO 先默认为100 netNum.guaranteedFee
						          "</li>";
						
						$("#numListUl").append($li);
						
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

/**
 * 条件滑动
 */
function loadedCondition() {
    myScroll = new IScroll("#masterdiv", {
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
    
    //滚动完毕  
    myScroll.on('scrollEnd', function() {
    	// 滚动结束去除空白区域
    	myScroll.refresh();
    });
}