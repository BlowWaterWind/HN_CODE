var myScroll, pullDownEl, pullDownOffset, pullUpEl, pullUpOffset, generatedCount = 0;

//初始化绑定iScroll控件
document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);
document.addEventListener('DOMContentLoaded', loaded, false);

/**
 * 下拉刷新
 */
function pullDownAction() {
	setTimeout(function() { 
		searchGoods("pullDown"); 
	}, 1000);
}

/**
 * 上拉滚动加载翻页
 */
function pullUpAction() {
	setTimeout(function() { 
		var lastPage = $("#lastPage").val();
		if(lastPage == "true"){
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '已到最后一页...';
			return;
		}
		
		searchGoods("pullUp");
		
	}, 1000);
}
var goodsStaticList;
var imgurl="";
//查询商品
function searchGoods(pullType,saleNumSort,salePriceSort){
	var url = ctx + "/goodsQuery/searchGoods";
	var pageNo = $("#pageNo").val()+1;
	if(pullType == "pullUp"){
		pageNo++;
	}
	
	var param = {};
	param["keyWord"] = $("#keyWord").val();
	param["categoryId"] = $("#categoryId").val();
	param["page.pageNo"] = pageNo;
	param["page.pageSize"] = $("#pageSize").val();
	param["goodsSaleNumOrderBy"] = saleNumSort;
	param["goodsSalePriceOrderBy"] = salePriceSort;

	$.post(url,param,function(data){
		if(pullType == "pullDown"){
			$("#goodsListDiv").empty();
		}
		
		for(var i=0;i<data.list.length;i++){
			var goods = data.list[i];

			 goodsStaticList=goods.goodsStaticList;
			 imgurl="";
			for(var j=0;j<goodsStaticList.length;j++){
				if(goodsStaticList[j].goodsStaticDefault==0){
					imgurl=tfsUrl+goodsStaticList[j].goodsStaticUrl;
				}
			}

			var $dl = $("<dl>\n" +
					    "  <dt><a href=\""+gUrl+goods.goodsUrl+"\"><img src=\""+imgurl+"\" /></a></dt>\n" +
					    "  <dd>\n" + 
					    "    <a href=\""+gUrl+goods.goodsUrl+"\" class=\"tu-con\">"+goods.goodsName+"</a>\n" +
					    "    <span class=\"price-bt\"><strong>￥"+goods.minGoodsSalePrice+"</strong></span>\n" +
					    "    <div class=\"tu-fr\">\n" + 
					    "      <span class=\"tu-cl\">已售出"+goods.goodsSaleNum+"台</span>\n" + 
					    "    </div>\n" + 
					    "  </dd>\n" + 
					    "</dl>");
		    $("#goodsListDiv").append($dl);
		}
	});
	
	myScroll.refresh();
}

/**
 * 初始化iScroll控件
 */
function loaded() {
	pullDownEl = document.getElementById('pullDown');
	pullDownOffset = pullDownEl.offsetHeight;
	pullUpEl = document.getElementById('pullUp');
	pullUpOffset = pullUpEl.offsetHeight;

	myScroll = new iScroll(
			'wrapper',
			{
				scrollbarClass : 'myScrollbar', /* 重要样式 */
				useTransition : false, /* 此属性不知用意，本人从true改为false */
				topOffset : pullDownOffset,
				onRefresh : function() {
					if (pullDownEl.className.match('loading')) {
						pullDownEl.className = '';
						pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
					} else if (pullUpEl.className.match('loading')) {
						pullUpEl.className = '';
						pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
					}
				},
				onScrollMove : function() {
					if (this.y > 5 && !pullDownEl.className.match('flip')) {
						pullDownEl.className = 'flip';
						pullDownEl.querySelector('.pullDownLabel').innerHTML = '松手开始更新...';
						this.minScrollY = 0;
					} else if (this.y < 5 && pullDownEl.className.match('flip')) {
						pullDownEl.className = '';
						pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
						this.minScrollY = -pullDownOffset;
					} else if (this.y < (this.maxScrollY - 5)
							&& !pullUpEl.className.match('flip')) {
						pullUpEl.className = 'flip';
						pullUpEl.querySelector('.pullUpLabel').innerHTML = '松手开始更新...';
						this.maxScrollY = this.maxScrollY;
					} else if (this.y > (this.maxScrollY + 5)
							&& pullUpEl.className.match('flip')) {
						pullUpEl.className = '';
						pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
						this.maxScrollY = pullUpOffset;
					}
				},
				onScrollEnd : function() {
					if (pullDownEl.className.match('flip')) {
						pullDownEl.className = 'loading';
						pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';
						pullDownAction(); // Execute custom function (ajax
											// call?)
					} else if (pullUpEl.className.match('flip')) {
						pullUpEl.className = 'loading';
						pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';
						pullUpAction(); // Execute custom function (ajax call?)
					}
				}
			});

	setTimeout(function() {
		document.getElementById('wrapper').style.left = '0';
	}, 800);
}

$(function(){
	$("#saleNumSortI").click(function(){
		var saleNumSort;
		var salePriceSort;
		var saleNumClass;

		var salePriceSortClass = $("#salePriceSortI").attr("class");
		if(salePriceSortClass == "sort-btn-up"){
			salePriceSort = "ASC";
		}

		if(salePriceSortClass == "sort-btn-down"){
			salePriceSort = "DESC";
		}

		if($(this).attr("class") == "sort-btn-up"){
			//降序
			saleNumSort = "DESC";
			saleNumClass = "sort-btn-down";
		}

		if($(this).attr("class") == "sort-btn-down"){
			//升序
			saleNumSort = "ASC";
			saleNumClass = "sort-btn-up";
		}

		$(this).attr("class",saleNumClass);
		searchGoods("pullDown",saleNumSort,salePriceSort);
	});

	$("#salePriceSortI").click(function(){
		var saleNumSort;
		var salePriceSort;
		var salePriceClass;

		var saleNumSortClass = $("#saleNumSortI").attr("class");
		if(saleNumSortClass == "sort-btn-up"){
			saleNumSort = "ASC";
		}

		if(saleNumSortClass == "sort-btn-down"){
			saleNumSort = "DESC";
		}

		if($(this).attr("class") == "sort-btn-up"){
			//降序
			salePriceSort = "DESC";
			salePriceClass = "sort-btn-down";
		}

		if($(this).attr("class") == "sort-btn-down"){
			//升序
			salesPriceSort = "ASC";
			salePriceClass = "sort-btn-up";
		}

		$(this).attr("class",salePriceClass);
		searchGoods("pullUp",salePriceSort);
	});

	$(".conditionValue").each(function(){
		$(this).live("click",function(){
			var liClass = $(this).attr("class");
			var aClass = $(this).find("a").attr("class");

			if(liClass.indexOf("select") >-1){
				$(this).removeClass("select");
			}else{
				$(this).addClass("select");
			}

			if(aClass == "list-bg"){
				$(this).find("a").removeClass("list-bg");
			}else{
				$(this).find("a").addClass("list-bg");
			}
		});
	});

	$(".drawer-main-title-qd").click(function(){
		$(".conditionValue").each(function(){
			if($(this).attr("class").indexOf("select") > -1){
				var valueName = $(this).find("a").attr("valueName");
				var endValueName = $(this).find("a").attr("endValueName");
				var conditionIdName = $(this).find("a").attr("conditionIdName");

				$(this).find(".value").attr("name",valueName);
				$(this).find(".endValue").attr("name",endValueName);
				$(this).find(".conditionId").attr("name",conditionIdName);
			}else{
				$(this).find("input").removeAttr("name");
			}
		});

		$("#searchGoodsForm").submit();
	});

	//清除选项
	$("#clearItemsBtn").click(function(){
		$("#masterdiv").find(".select").each(function(){
			$(this).removeClass("select");
		});

		$("#masterdiv").find(".list-bg").each(function(){
			$(this).removeClass("list-bg");
		});


	});
});