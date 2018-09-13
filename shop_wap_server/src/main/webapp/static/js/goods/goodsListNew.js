
var sHeight= (window.innerHeight > 0) ? window.innerHeight : screen.Height;
$('#content').height(sHeight-90);
$('#masterdiv').height(sHeight - 183);
$('#masterdiv').css("margin-top","93px");
var myScroll;
var pullDownEl, pullDownL;
var pullUpEl, pullUpL;
var saleNumSort="";
var salePriceSort="";
var Downcount = 0, Upcount = 0;
var loadStep = 0;//加载状态0默认，1显示加载状态，2执行加载数据，只有当为0时才能再次加载，这是防止过快拉动刷新  
function pullDownAction() {//下拉事件  
    setTimeout(function() {
        // $('div.tu-list').html('');
        searchGoods("pullDown",saleNumSort,salePriceSort);
        pullDownEl.removeClass('load');
        pullDownL.html('下拉显示更多...');
        pullDownEl['class'] = pullDownEl.attr('class');
        pullDownEl.attr('class', '').hide();
        myScroll.refresh();
        loadStep = 0;
    }, 1000); //1秒  
}
function pullUpAction() {//上拉事件
    var lastPage = $("#lastPage").val();
    if(lastPage == "true"){
        $('div.pullUpLabel').html('已到最后一页...');

        return;
    }
    searchGoods("pullUp",saleNumSort,salePriceSort);
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
document.addEventListener('DOMContentLoaded', loaded, false);

function search(){
    $("#salePriceSortI").attr("class","sort-btn-up");
    $("#saleNumSortI").attr("class","sort-btn-up");
    $("#liActive").attr("class","active");
    // 搜索清除筛选条件
    $("#clearItemsBtn").click();
    searchGoods("search");
}
function searchGoods(pullType,saleNumSort,salePriceSort){
    var url;
    var pageNo = $("#pageNo").val();
    if(pullType == "pullUp"){
        pageNo++;
    }else if(pullType=="search"){
        $("#goodsListDiv").empty();
        pageNo=1;
    }
    
    var flag = checkConditionFlag();
    if (flag == 0) {
    	url = ctx + "/goodsQuery/searchGoods";
    	var param = {};
        param["CSRFToken"] = $("#csrftoken").val();
        param["keyWord"] = $("#searchKeyInput").val();
        param["categoryId"] = $("#categoryId").val();
        param["queryListpageCode"] = $("#queryListpageCode").val();
        param["page.pageNo"] = pageNo;
        param["page.pageSize"] = $("#pageSize").val();
        param["goodsSaleNumOrderBy"] = saleNumSort;
        param["goodsSalePriceOrderBy"] = salePriceSort;
        
        queryGoodsByPage(pullType, url, param);
    } else if (flag == 1) {
    	url = ctx + "/goodsQuery/linkToGoodsListByPage";
    	var param = $("#searchGoodsForm").serializeObject();
    	param["page.pageNo"] = pageNo;
        param["page.pageSize"] = $("#pageSize").val();
        param["goodsSaleNumOrderBy"] = saleNumSort;
        param["goodsSalePriceOrderBy"] = salePriceSort;
        
        queryGoodsByPage(pullType, url, param);
    }

    
}
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

$(function(){
	
	handlePrice();
	
    $("#saleNumSortI_a").click(function(){
        var saleNumClass;


        salePriceSort = "";

        if( $("#saleNumSortI").attr("class") == "sort-btn-up"){
            //降序
            saleNumSort = "DESC";
            saleNumClass = "sort-btn-down";
        }

        if( $("#saleNumSortI").attr("class") == "sort-btn-down"){
            //升序
            saleNumSort = "ASC";
            saleNumClass = "sort-btn-up";
        }
        $("#liActive").attr("class","");
        $("#saleNumSortI").attr("class",saleNumClass);
        flag = 0;
        searchGoods("search",saleNumSort,salePriceSort);
    });

    $("#salePriceSortI_a").click(function(){
        var saleNumClass;

        saleNumSort = "";

        if($("#salePriceSortI").attr("class") == "sort-btn-up"){
            //降序
            salePriceSort = "DESC";
            salePriceClass = "sort-btn-down";
        }

        if($("#salePriceSortI").attr("class") == "sort-btn-down"){
            //升序
            salePriceSort = "ASC";
            salePriceClass = "sort-btn-up";
        }
        $("#liActive").attr("class","");
        $("#salePriceSortI").attr("class",salePriceClass);
        flag = 0;
        searchGoods("search",saleNumSort,salePriceSort);
    });

    $(".conditionValue").each(function(){
        $(this).live("click",function(){
            var liClass = $(this).attr("class");
            var aClass = $(this).find("a").attr("class");

            if(liClass.indexOf("select") >-1){
                $(this).removeClass("select");
            }else{
                $(this).addClass("select");
                
                //$(this).siblings().removeClass("select");
            }

            if(aClass == "list-bg"){
                $(this).find("a").removeClass("list-bg");
            }else{
                $(this).find("a").addClass("list-bg");
                //$(this).siblings().find("a").removeClass("list-bg");
            }
        });
    });

    $("#confirmBtn").click(function(){
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
        
        // 新增价格区间条件
        if ($("#minPrice").val() && $("#maxPrice").val()) {
        	var valueName = $("#price").find("input[valueName]").attr("valueName");
            var endValueName =$("#price").find("input[endValueName]").attr("endValueName");
            var conditionIdName = $("#price").find("input[conditionIdName]").attr("conditionIdName");
        	$("#price").find("input[valueName]").val($("#minPrice").val() * 100).attr("name", valueName);
        	$("#price").find("input[endValueName]").val($("#maxPrice").val() * 100).attr("name", endValueName);
        	$("#price").find("input[conditionIdName]").attr("name", conditionIdName);
        } else if ($("#minPrice").val() || $("#maxPrice").val()) {
        	//showAlert("请填写完整的价格区间!");
        	return;
        }
        try{
        if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","购机",false,"WT.si_x","条件筛选",false,"WT.select",$("#searchGoodsForm").serializeObject(),false);}  
        }catch(e){
        	
        }
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

        // 清除价格区间
        $("#minPrice").val("");
        $("#maxPrice").val("");
        $("#price").find("input:gt(1)").removeAttr("name");
    });
    
    $("#minPrice").blur(function(){
		var reg = /^[0-9]*$/;
		var value = $(this).val();
		if (!reg.test(value)) {
			//showAlert("请输入整数！");
			$(this).val("");
			return;
		} else {
			var maxVal = $("#maxPrice").val();
			if (maxVal) {
				if (value >= maxVal) {
					//showAlert("最低价不能高于最高价!");
					$(this).val("");
					return;
				}
			}
		}
	});
	
    $("#maxPrice").blur(function(){
    	var reg = /^[0-9]*$/;
		var value = $(this).val();
		if (!reg.test(value)) {
			//showAlert("请输入整数！");
			$(this).val("");
			return;
		} else {
			var minVal = $("#minPrice").val();
			if (minVal) {
				if (value <= minVal) {
					//showAlert("最高价不能低于最低价!");
					$(this).val("");
					return;
				}
			}
		}
	});
    search();
});

/**
 * 分页
 * @param url
 * @param param
 */
function queryGoodsByPage(pullType, url, param) {
	$.post(url, param, function(data){
        if(pullType == "pullDown"){
            $("#goodsListDiv").empty();
        }

        for(var i=0;i<data.list.length;i++){
            var goods = data.list[i];

            goodsStaticList=goods.goodsStaticList;
            imgurl="";
            for(var j=0;j<goodsStaticList.length;j++){
                if(goodsStaticList[j].goodsStaticDefault==0){
                    imgurl=tfsUrl+resImageSize(goodsStaticList[j].goodsStaticUrl,"80x100");
                }
            }

            var $dl = $("<dl>\n" +
                "  <dt><a href=\""+gUrl+goods.goodsUrl+"\"><img src=\""+imgurl+"\" /></a></dt>\n" +
                "  <dd>\n" +
                "    <a href=\""+gUrl+goods.goodsUrl+"\" class=\"tu-con\">"+goods.goodsName+"</a>\n" +
                "    <span class=\"price-bt\"><strong>￥"+goods.minGoodsSalePrice/100+"</strong></span>\n" +
                "    <div class=\"tu-fr\">\n" +
                "      <span class=\"tu-cl\">已售出"+goods.goodsSaleNum+"台</span>\n" +
                "    </div>\n" +
                "  </dd>\n" +
                "</dl>");
            $("#goodsListDiv").append($dl);

            $("#pageNo").val(data.pageNo);
            $("#pageSize").val(data.pageSize);
            var pageNo = parseInt(data.pageNo);
            var pageSize = parseInt(data.pageSize);

            var count = parseInt(data.count);
            if(pageNo*pageSize>=count){
                $("#lastPage").val('true');
            }else{
                $("#lastPage").val('false');
            }
            pullUpEl.removeClass('load');
            pullUpL.html('上拉显示更多...');
            pullUpEl['class'] = pullUpEl.attr('class');
            pullUpEl.attr('class', '').hide();
            myScroll.refresh();
            loadStep = 0;
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

/**
 * 判断是否经过条件筛选
 */
function checkConditionFlag() {
	var flag = 0;
	if ($("#maxPrice").val() && $("#minPrice").val()) {
		flag = 1;
	} else {
		 $(".conditionValue").each(function(){
	         if($(this).attr("class").indexOf("select") > -1) {
	             flag = 1;
	             return false;
	         }
	     });
    }
	
	return flag;
}

/**
 * 处理价格区间数据
 */
function handlePrice(){
	// 清除价格区间为0的情况
	var minPrc = $("#minPrice").attr("alt");
	if (minPrc) {
		$("#minPrice").val(parseInt(minPrc / 100));
	}
	
	var maxPrc = $("#maxPrice").attr("alt");
	if (maxPrc) {
		$("#maxPrice").val(parseInt(maxPrc / 100));
	}
	
	if ($("#minPrice").val() && $("#maxPrice").val()) {
    	var valueName = $("#price").find("input[valueName]").attr("valueName");
        var endValueName =$("#price").find("input[endValueName]").attr("endValueName");
        var conditionIdName = $("#price").find("input[conditionIdName]").attr("conditionIdName");
    	$("#price").find("input[valueName]").val(minPrc).attr("name", valueName);
    	$("#price").find("input[endValueName]").val(maxPrc).attr("name", endValueName);
    	$("#price").find("input[conditionIdName]").attr("name", conditionIdName);
    }
}