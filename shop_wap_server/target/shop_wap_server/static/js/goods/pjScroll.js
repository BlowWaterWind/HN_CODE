	var 	basePath = "" ;//$("#basePath").val();
	 var sHeight= (window.innerHeight > 0) ? window.innerHeight : screen.Height; 
	 $('#contentPj').height(sHeight-150);
	var myScroll;
	var pullDownEl, pullDownL;
	var pullUpEl, pullUpL;
	var Downcount = 0, Upcount = 0;
	var loadStep = 2;//加载状态0默认，1显示加载状态，2执行加载数据，只有当为0时才能再次加载，这是防止过快拉动刷新  
	function pullDownAction() {//下拉事件  
		setTimeout(function() {
			var el, li, i;
			/*el = $('#add').html();
			for (i = 0; i < 4; i++) {
				li = $("<li></li>");
				Downcount++;
				li.text('new Add ' + Downcount + " ！");
				el.prepend(li);
			}*/
			ajaxHomeGoods();
			pullDownEl.removeClass('load');
			pullDownL.html('下拉显示更多...');
			pullDownEl['class'] = pullDownEl.attr('class');
			pullDownEl.attr('class', '').hide();
			myScroll.refresh();
			loadStep = 0;
		}, 1000); //1秒  
	}
	function pullUpAction() {//上拉事件
		ajaxHomeGoods();
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
        
		var content1 = document.getElementById("contentPj");
		myScroll = new IScroll(content1, {
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
$(function(){
	$("#loadMyModal7").on("click",function(){
		document.getElementById("myModal7").addEventListener('touchmove', function(e) {
		e.preventDefault();
	    }, false);
	    //document.addEventListener('DOMContentLoaded', loaded, false);
		loaded();
		
	})
	
})
	
	
	function fillTab(){
		$("#currentPage").val(1);
		
		var sort = $("#bySort").val();
		//alert(sort);
		$("#sub-classtmp li a").removeClass();
		$("#"+sort).addClass("active");
	}
	
	function bySubmit(){
		$("#sortForm").submit();
	}
	
	function bySort(val){		
		$("#bySort").val(val);
		$("#sortForm").submit();
	}
	
	function ajaxHomeGoods(oper){
		var param = {"currentPage":$("#currentPage").val(),"bySort":$("#bySort").val(),"floorChnlId":$("#floorChnlId").val(),"sortKey":$("#sortKey").val()};
	
		/*$.ajax({
			type : "GET",
			contentType : "application/json",
			url : basePath+"/home/ajaxHomeGoods.do",
			data :param,
			dataType: "json",
			success : function(jsonData) {
				//alert(jsonData.homeGoodsList);
				var obj = jsonData.homeGoodsList;
				var lihtml = "";
			setTimeout(function() {
				var el, li;
				el = $('#add');
				$(obj).each(function(index) {
					li = $("<li></li>");
	                var val = obj[index];
	                //alert(val.goods_name);
	              var hrefpath =basePath+"/goods/goodsDetail.do?goodsId="+val.goodsId+"&chnlCode="+val.chnnlCode+"&catelog="+val.firstCateCode;
	                lihtml+="<a href='"+hrefpath+"'> <img src='"
								+val.goodsSResUrl
								+"' /> <div style='height:1.5rem;'>"
								+val.goodsName
								+"</div> <div class='h40 mtb10'>"
								+"<div class='price pull-left'>￥"
								+val.maxMarketPrice 
								+"</div> <div class='oldprice pull-right'>参考价："
								+val.maxMarketPrice
								+"</div></div></a>";
					li.html(lihtml);
					el.append(li);	
					lihtml="";					
	            });
		
	            pullUpEl.removeClass('load');
				pullUpL.html('上拉显示更多...');
				pullUpEl['class'] = pullUpEl.attr('class');
				pullUpEl.attr('class', '').hide();
				myScroll.refresh();
				loadStep = 0;
			}, 1000);
	        $("#currentPage").val(parseInt($("#currentPage").val())+1);         
		},
		error : function(jsonData) {
			}
		});*/	
		
		setTimeout(function() {
			var data = {"list":[{"val":"val1"},{"val":"val2"},{"val":"val3"},{"val":"val4"},{"val":"val5"}]}
			$("#add").append(template("pj-listTmpl"),data);
	
            pullUpEl.removeClass('load');
			pullUpL.html('上拉显示更多...');
			pullUpEl['class'] = pullUpEl.attr('class');
			pullUpEl.attr('class', '').hide();
			myScroll.refresh();
			loadStep = 0;
		}, 1000);
		
	}