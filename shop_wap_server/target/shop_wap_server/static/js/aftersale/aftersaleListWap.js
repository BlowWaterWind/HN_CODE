	var sHeight = (window.innerHeight > 0) ? window.innerHeight : screen.Height;
	$('#content').height(sHeight-90);
	var myScroll;
	var pullDownEl, pullDownL;
	var pullUpEl, pullUpL;
	var Downcount = 0, Upcount = 0;
	var loadStep = 0;// 加载状态0默认，1显示加载状态，2执行加载数据，只有当为0时才能再次加载，这是防止过快拉动刷新
	var tfsUrl = $("#tfsUrl").val();
	var gUrl=$("#gUrl").val();

	function pullDownAction() {//下拉事件
		setTimeout(function() {
			$('div.tu-list').html('');
			ajaxMorePage('y');
			pullDownEl.removeClass('load');
			pullDownL.html('下拉显示更多...');
			pullDownEl['class'] = pullDownEl.attr('class');
			pullDownEl.attr('class', '').hide();
			myScroll.refresh();
			loadStep = 0;
		}, 1000); // 1秒
	}
	// 上拉事件
	function pullUpAction() {
		var lastPage = $("#lastPage").val();
		if (lastPage === "true") {
			pullUpEl.find('.pullUpLabel').html('已到最后一页');
			return;
		}
		ajaxMorePage();
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

		myScroll = new IScroll('#content', {
			probeType : 2,// probeType：1对性能没有影响。在滚动事件被触发时，滚动轴是不是忙着做它的东西。probeType：2总执行滚动，除了势头，反弹过程中的事件。这类似于原生的onscroll事件。probeType：3发出的滚动事件与到的像素精度。注意，滚动被迫requestAnimationFrame（即：useTransition：假）。
			scrollbars : true,// 有滚动条
			mouseWheel : true,// 允许滑轮滚动
			topOffset : 40,
			fadeScrollbars : true,// 滚动时显示滚动条，默认影藏，并且是淡出淡入效果
			bounce : true,// 边界反弹
			interactiveScrollbars : true,// 滚动条可以拖动
			shrinkScrollbars : 'scale',// 当滚动边界之外的滚动条是由少量的收缩。'clip' or 'scale'.
			click : true,// 允许点击事件
			keyBindings : true,// 允许使用按键控制
			momentum : true
		// 允许有惯性滑动
		});
		// 滚动时
		myScroll.on('scroll', function() {
			if (loadStep == 0 && !pullDownEl.attr('class').match('flip|load')
					&& !pullUpEl.attr('class').match('flip|load')) {
				if (this.y > 5) {
					// 下拉刷新效果
					pullDownEl.attr('class', pullUpEl['class'])
					pullDownEl.show();
					myScroll.refresh();
					pullDownEl.addClass('flip');
					pullDownL.html('准备刷新...');
					loadStep = 1;
				} else if (this.y < (this.maxScrollY - 5)) {
					// 上拉刷新效果
					pullUpEl.attr('class', pullUpEl['class'])
					pullUpEl.show();
					myScroll.refresh();
					pullUpEl.addClass('flip');
					pullUpL.html('准备刷新...');
					loadStep = 1;
				}
			}
		});
		// 滚动完毕触发事件
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
	document.addEventListener('DOMContentLoaded', loaded, false);
	
	/**
	 * 异步得到分页数据
	 */
	function ajaxMorePage(reflesh) {
		//获取当前服务类型
		var serverName = $("#tab li.current").attr("id");
		var url = "../"+serverName+"/"+serverName+"List";
		var pageNo = $("#pageNo").val();
		var pageSize = $("#pageSize").val();
		//如果是下拉，则页码在此基础上+1
		pageNo++;
		//如果是是上拉，则是第一页
		if (reflesh == 'y') {
			pageNo = 1;
		}
		$.ajax({
			type : "post",
			url : url,
			data : {
				"pageNo": pageNo,
				"pageSize":pageSize,
				"tfsUrl":tfsUrl
			},
			dataType : "json",
			success : function(result) {
				// 滑动分页公用内容
				setTimeout(function() {
					var data = {
						"resList" : result
					};
					// 得到数据进行处理
					var html = template(serverName+'WapTemplate', data);
					$('ul.aftersalelist').append(html);

					//== 页码数据保存 ==//
					//alert(result.page.pageNo);
					$("#pageNo").val(result.page.pageNo);
					$("#pageSize").val(result.page.pageSize);
					var pageNo = parseInt(result.page.pageNo);
					var pageSize = parseInt(result.page.pageSize);
					//判断是否是最后一页，每次的pageSize必须相同
					var count = parseInt(result.page.count);
					if (pageNo * pageSize >= count) {
						$("#lastPage").val('true');
					} else {
						$("#lastPage").val('false');
					}
					pullUpEl.removeClass('load');
					pullUpL.html('上拉显示更多...');
					pullUpEl['class'] = pullUpEl.attr('class');
					pullUpEl.attr('class', '').hide();
					myScroll.refresh();
					loadStep = 0;
				}, 1000);
			},
			error : function(msg) {
				alert("错误："+msg);
			}
		});
	}

	/**
	 * 跳转后首次请求数据
	 */
	function changeTab(serverName) {
		$.ajax({
			type : "post",
			url : "../" + serverName + "/" + serverName + "List",
			data : {
				pageNo : 1,
				pageSize : 5,
				tfsUrl:tfsUrl
			},
			dataType : "json",
			success : function(result) {
				// 滑动分页公用内容
				setTimeout(function() {
					//console.log(result);
					var data = {
						"resList" : result
					};
					$('ul.aftersalelist').empty();
					if (result.retMsg === "0") {
						$("#content").hide();
						$("#noDataReminder").show();
					} else if (result.retMsg === "1") {
						$("#noDataReminder").hide();
						$("#content").show();
						$('ul.aftersalelist').append(template(serverName + 'WapTemplate', data));
						
						//分页参数赋值
						$("#pageNo")[0].value = result.page.pageNo;
						$("#pageSize")[0].value = result.page.pageSize;
						//== 如果count<=pageSize，则lastPage=false，否则true ==//
						if(result.page.count>result.page.pageSize){
							$("#lastPage").val("false");
							//$("#pullUp").html("");
						}else{
							$("#lastPage").val("true");
							//$("#pullUp").html("加载中");
						}
					}
					pullUpEl.removeClass('load');
					pullUpL.html('上拉显示更多...');
					pullUpEl['class'] = pullUpEl.attr('class');
					pullUpEl.attr('class', '').hide();
					myScroll.refresh();
					loadStep = 0;
				}, 1000);
			},
			error : function(data) {
				alert("error：" + data);
			}
		});
	}

	//跳转进来，默认查看退款信息
	$(function() {
		changeTab('retMoney');
		//注册点击事件，修改样式
		$("span.text").click(function() {
			//alert($(this).parent().attr("id"));
			$("#tab li").removeClass("current")
			$(this).parent().addClass("current");
		});
	});
	
	
	
	
	