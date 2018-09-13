
Array.prototype.indexOf = function(val) {
for (var i = 0; i < this.length; i++) {
if (this[i] == val) return i;
}
return -1;
};
Array.prototype.removeAll = function() {
	 var i = this.length;  
	    while (i--) {
	    	this.pop();
	    }  
	};
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
	this.splice(index, 1);
	}
	};
/**
 * 判断两个数组是否有交集
 * @param obj
 * @returns {Boolean}
 */
Array.prototype.containsByNocase = function (obj) {  
    var i = this.length;  
    while (i--) {
    	for(var j=0;j<obj.length;j++)
    	{
    		 if (obj[j] ==  this[i]) 
    		 {  
    	        return true;  
    	     }  	
    	}
       
    }  
    return false;  
} 


/**
 * 跟据 prodAttrId 和prodAttrValueId 取goodsSkuId
 * @param currentAttrId
 * @returns
 */
function getGoodsSkuIdByProdAttrIdAndValue(prodAttrId,prodAttrValueId){
	var goodsSkuArray = new Array();
	for(var i=0;i < goodsSkuAttrList.length ;i++)
	{
		var goodsSA = goodsSkuAttrList[i];
		if(prodAttrId == goodsSA.prodAttrId && prodAttrValueId == goodsSA.prodAttrValueId)
		{
			goodsSkuArray.push(goodsSA.goodsSkuId);
		}
	}
	return goodsSkuArray;
}

// 以上代码可公用

/**
 * 根据全选的属性选择goodsSkuId
 */
function getGoodsSkuId()
{
	// 获取选中的 goodsSkuAttr
	var $attr_ul =$("ul[id^='attrUl_']");
	var goodsSkuIdArraySet = new Array();
	var $attr_li_on=$attr_ul.eq(0).find(".list-active");
	var prodAttrId = $attr_li_on.find(">a").attr("prodAttrId");
	var prodAttrValueId = $attr_li_on.find(">a").attr("prodAttrValueId");
	var goodsSkuIdArraySet= getGoodsSkuIdByProdAttrIdAndValue(prodAttrId,prodAttrValueId);
	
	for(var i=1;i<$attr_ul.size();i++)
	{
		var $attr_li_on=$attr_ul.eq(i).find(".list-active");
		var prodAttrId = $attr_li_on.find(">a").attr("prodAttrId");
		var prodAttrValueId = $attr_li_on.find(">a").attr("prodAttrValueId");
		var goodsSkuIdArray = getGoodsSkuIdByProdAttrIdAndValue(prodAttrId,prodAttrValueId);
		for(var j= 0 ;j< goodsSkuIdArraySet.length;j++ )
		{
			if(goodsSkuIdArray.indexOf(goodsSkuIdArraySet[j]) == -1)
			{
				goodsSkuIdArraySet.splice(j,1);
				j--;
			}
		}
	}
	if(goodsSkuIdArraySet.length>=1) return goodsSkuIdArraySet[0];
	return null;
}

/**
 *  增加了 sku 集筛选后的 联动选择 sku 属性
 * @param currentAttrId
 */
function LinkageSelection(currentAttrId)
{
	var $attrA = $("#attrLi_"+currentAttrId+" a");
	// 已经被置灰的按钮 不能点
	if($("#attrLi_"+currentAttrId).hasClass("gray"))
	{
		return;
	}
	
	// 取sku数据
	var goodsSkuId = $attrA.attr("goodsSkuId");
	var prodAttrId = $attrA.attr("prodAttrId");
	var prodAttrValueId = $attrA.attr("prodAttrValueId");
	
	// 当前选中
	$("#attrUl_"+prodAttrId+">li").removeClass("list-active");
	$("#attrLi_"+currentAttrId).addClass("list-active");
    
	// 取当前属性的 goodsSkuId
	var goodsSkuIdArray = getGoodsSkuIdByProdAttrIdAndValue(prodAttrId,prodAttrValueId);
	
	// 将后面的不能选的sku属性置灰，且置为不能选
	var isLastLi = false;
	for(var i=0;i<strGoodsSkuProdAttrList.length;i++)
	{
		 var goodsSkuAttr = strGoodsSkuProdAttrList[i];
		 if(isLastLi)
		 {
	        // ul下的li数组
	    	var $attr_lis=$("#attrUl_"+strGoodsSkuProdAttrList[i].prodAttrId+">li");
	    	// 循环li数组 来判断能不能选
	    	for(var k=0;k<$attr_lis.size();k++)
	    	{
	    		var $attr_li = $attr_lis.eq(k);
	    		var $attr_li_a = $attr_li.find("a");
	    		var a_prodAttrId = $attr_li_a.attr("prodAttrId"); 
	    		var a_prodAttrValueId = $attr_li_a.attr("prodAttrValueId"); 
	    		var a_goodsSkuIdArray =  getGoodsSkuIdByProdAttrIdAndValue(a_prodAttrId,a_prodAttrValueId);
	    		
	    		// 当前属性不能选
	    		if(!goodsSkuIdArray.containsByNocase(a_goodsSkuIdArray))
	    		{
	    			$attr_li.removeClass("list-active");
	    			$attr_li.addClass("gray");
	    		}
	    		// 当前属性能选
	    		else{
	    			// 得到当前 ul 下的 已被选中的属性
	    			var $attr_li_on = $("#attrUl_"+strGoodsSkuProdAttrList[i].prodAttrId+" >li.list-active");
	    			var $li_on_a = $attr_li_on.find(">a");
	    			var on_a_prodAttrId =$li_on_a.attr("prodAttrId"); 
		    		var on_a_prodAttrValueId = $li_on_a.attr("prodAttrValueId"); 
		    		var on_a_goodsSkuIdArray =  getGoodsSkuIdByProdAttrIdAndValue(on_a_prodAttrId,on_a_prodAttrValueId);
	    			// 如果已经被选中的属性 与前面选中的属性 互斥
		    		if(!goodsSkuIdArray.containsByNocase(on_a_goodsSkuIdArray) || $attr_li_on.size() <= 0 )
	    			{
		    			$attr_li_on.removeClass("list-active").addClass("gray");
	    				$attr_li.addClass("list-active");
	    			}
	    			$attr_li.removeClass("gray");
	    		}
	    	}
		 }else
	     {
			 // 标记以后循环的  ul 是后面的ul
			 if(goodsSkuAttr.prodAttrId == prodAttrId)
			 {
				 isLastLi = true;
			 }
	     }
			 
	}
}


function getGoodsWater(goodsSkuId,params){
	var goodsType = $("input[name='userGoodsCarList[0].goodsType']").val();
	var reg = new RegExp("(^|&)marketId=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    var marketId = "";
    if(r!=null){
   	 marketId = unescape(r[2]);
    }
	
	$.ajax({
        type:"POST",
        url:baseProject+"goodsInfo/getGoodsWater",
        data:{'params':params,"goodsId":$("#goodsId").val(),"goodsSkuId":goodsSkuId,"goodsType":goodsType,"marketId":marketId},
        dataType:"json",
        success:function(data){
            var goodsWater=data.goodsWater;
			 var marketInfo = data.marketInfo;
            
			if(!goodsWater.goodsSalePrice)
			{
					/*for(var i=0;i < his_select_prodAttrValueIdArray.length;i++)
					{
						$("#"+his_select_prodAttrValueIdArray[i]).siblings("li").removeClass("on");
						$("#"+his_select_prodAttrValueIdArray[i]).addClass("on");
						
					}*/
                $("#goodsPrice_2").html("抱歉，暂无报价！");
                $("#goodsPrice").html("抱歉，暂无报价！");
                $("#buyBtn").removeClass("btn-rose").addClass("btn-gray");
                $("#addCartBtn").removeClass("btn-blue").addClass("btn-gray");
                return ;
			}
          
			 // 参加活动能够的商品不能加入购物车
			if(marketInfo){
				$("#addCartBtn").hide();

			}
			
            $("#buyBtn").removeClass("btn-gray").addClass("btn-rose");
            $("#addCartBtn").removeClass("btn-gray").addClass("btn-blue");
            
            $("#stockNum").html(goodsWater.stockNum);
            $("#stockNum").attr("valNum",goodsWater.stockNum);
            if(goodsWater.stockNum == 0)
            {
                $("#addCartBtn").removeClass("btn-blue").addClass("btn-gray");
                $("#buyBtn").removeClass("btn-rose").addClass("btn-gray");
            }
            if(goodsWater.goodsSkuId){
                $("input[name='userGoodsCarList[0].goodsSalePrice']").val(goodsWater.goodsSalePrice);
                $("#goodsPrice_2").html("￥"+(goodsWater.goodsSalePrice*0.01).toFixed(2));
                $("#goodsPrice").html("￥"+(data.shopGoodsPrice*0.01).toFixed(2));
                if(data.status == '1'){
                    $("#killPrice").html("￥"+(data.goodsWater.goodsSkuPrice*0.01).toFixed(2));//秒杀价
                   // $("#deposit_num").html("￥"+goodsWater.firstPrice.toFixed(2));//预售价
                }
                $("#stockNum_2").html(goodsWater.stockNum);
                $("#numberSelectDiv").show();
            }
            
            if(goodsWater.state!=4){
				$("#buyBtn").hide();
				$("#addCartBtn").hide();
		    }
            else {
            	$("#buyBtn").removeClass('hide');
				$("#addCartBtn").removeClass('hide');

            }
        }
    });
}
var his_select_prodAttrValueIdArray = new Array();
function checkSelect(currentAttrId){
	// 点击第一个属性
	if(!currentAttrId)
	{
			var $attr_lis=$("#attrUl_"+strGoodsSkuProdAttrList[0].prodAttrId+">li");
	    	var $attr_li_a = $attr_lis.eq(0).find(">a");
	    	$attr_li_a.click();
	}
	else
	{
		/*// 保存选则的属性历史
		var $his_attr_ul =$("ul[id^='attrUl_']");
		his_select_prodAttrValueIdArray.removeAll();
		for(var i=0;i<$his_attr_ul.size();i++)
		{
			
			var $his_attr_li_on=$his_attr_ul.eq(i).find(".on");
			his_select_prodAttrValueIdArray.push($his_attr_li_on.attr("id"));
		}*/
		
		LinkageSelection(currentAttrId);
		// 获取选中的 goodsSkuAttr
		var $attr_ul =$("ul[id^='attrUl_']");
		var params='';
		var paramsName='';
		var selectAttrLength = $("ul[id^='attrUl_'] .list-active");
		if(selectAttrLength < strGoodsSkuAttrDistinctListList.length)
		{
            $("#goodsPrice +small").html("");
            $("#goodsPrice").html("抱歉，暂无报价");
            $("#goodsPrice2").html("抱歉，暂无报价");
            $("#buyBtn").removeClass("btn-rose").addClass("btn-gray");
            $("#addCartBtn").removeClass("btn-blue").addClass("btn-gray");

            return ;
		}

        $("#buyBtn").removeClass("btn-gray").addClass("btn-rose");
        $("#addCartBtn").removeClass("btn-gray").addClass("btn-blue");
		
		for(var i=0;i<$attr_ul.size();i++)
		{
			var $attr_li_on=$attr_ul.eq(i).find(".list-active");
			if($attr_li_on.size()==1)
			{
				params+=$attr_li_on.find(">a").attr("prodAttrId")+"="+$attr_li_on.find('>a').attr("prodAttrValueId");
				if(i < $attr_ul.size()-1)
				{
					params+="&";
				}
			}else{
				params="";
				break;
			}
		}

		var skuAttrValues = [];
		for(var i=0;i<$attr_ul.size();i++)
		{
			var $attr_li_on=$attr_ul.eq(i).find(".list-active");
			if($attr_li_on.size()==1)
			{
				var skuAttrValue = $.trim($attr_li_on.find('>a').text());
				skuAttrValues.push('"' + skuAttrValue + '"');
				paramsName+=$attr_ul.eq(i).attr("attrName")+"="+skuAttrValues;
				if(i < $attr_ul.size()-1)
				{
					paramsName+="&";
				}
			}else{
				paramsName="";
				break;
			}
		}
	    // 通过属性集获取goodsSkuId
		var goodsSkuId = getGoodsSkuId();
		$("input[name='userGoodsCarList[0].goodsSkuId']").val(goodsSkuId);//设置商品SKUID
		$("input[name='userGoodsCarList[0].goodsStandardAttrId']").val(params);//设置商品属性ID
		$("input[name='userGoodsCarList[0].goodsStandardAttr']").val(paramsName);//设置商品属性名称
		$("#chooseGoodsAttr").text("已选：" + skuAttrValues.join(""));
		getGoodsWater(goodsSkuId,params);
	}
		
}


var goodsMaxNum = null; // 活动最大限购数量
function phoneGMNumber(obj) {
    if (obj.value == "") {
        showAlert("尊敬的客户：您好！购买数量不能为空！");
        $('#phoneGMNum').val(1);
        $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(1));
        return;
    }
    var numbx = $('#phoneGMNum').val().replace(/^\s+|\s+$/g, "");
    var reg = /^[0]{1}$|^[1-9]+[0-9]*$/;
    if (!reg.test(numbx)) {
        showAlert("尊敬的客户：您好！购买数量必须为正整数！");
        $('#phoneGMNum').val(1);
        $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(1));
        return;
    }
    if (obj == 'add') {
    	if(null == goodsMaxNum || parseInt($("#stockNum_2").html()) < goodsMaxNum)
		{
			if(parseInt($("#stockNum_2").html())<(parseInt(numbx))){
				 showAlert("尊敬的客户：您好！购买数量不能超过商品库存数！");
				 $('#phoneGMNum').val(1);
				 $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(1));
			return;
			}
		}
		else
		{
			if( goodsMaxNum <(parseInt(numbx)+1)){
				 showAlert("尊敬的客户：您好！购买数量不能超过商品最大限购数量："+goodsMaxNum);
				 $('#phoneGMNum').val(goodsMaxNum);
				 $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(goodsMaxNum));
			return;
			}
		}
        $('#phoneGMNum').val(numbx * 1 + 1 );
        $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(numbx) + 1);
    } else if (obj == 'del') {
        if (parseInt(numbx) > 1) {
            $('#phoneGMNum').val(numbx * 1 - 1 );
            $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(numbx) - 1);
            return;
        }
    } else {
        if (isNaN(numbx)||parseInt(numbx)<1) {
            showAlert("尊敬的客户：您好，购买数量只能输入大于0数字！");
            $('#phoneGMNum').val(1);
            $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(1));
            return;
        } else {
        	if(null == goodsMaxNum || parseInt($("#stockNum_2").html()) < goodsMaxNum)
    		{
    			if(parseInt($("#stockNum_2").html())<(parseInt(numbx))){
    				 showAlert("尊敬的客户：您好！购买数量不能超过商品库存数！");
    				 $('#phoneGMNum').val(1);
    				 $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(1));
    			return;
    			}
    		}
    		else
    		{
    			if( goodsMaxNum <(parseInt(numbx)+1)){
    				 showAlert("尊敬的客户：您好！购买数量不能超过商品最大限购数量："+goodsMaxNum);
    				 $('#phoneGMNum').val(goodsMaxNum);
    				 $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(goodsMaxNum));
    			return;
    			}
    		}
            $('#phoneGMNum').val(numbx);
            $("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(numbx));
            return;
        }
    }
}

function getGoodsActivity() {
	 var reg = new RegExp("(^|&)marketId=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     var marketId = "";
     if(r!=null){
    	 marketId = unescape(r[2]);
     }
	
	$.ajax({
        type : "POST",
        url : baseProject+"goodsInfo/getGoodsMarket",
        data : {
            "goodsId" : $("#goodsId").val(),
            "marketId": marketId
        },
        dataType : "json",
        success : function(data) {
            var marketType = "";
            if (data.status == '1') {
                if (data.marketType == 1) {
                    marketType = "团购";
                } else if (data.marketType == 2) {
                    marketType = "秒杀";
                    $("#killPrice").html("￥" + (data.marketPrice*0.01).toFixed(2));
                    $("#delPrice").html($("#goodsPrice").html());
                    $("#kkNum").html("剩余"+data.stockNum+"件");
                    $("#num_notice").html("已抢"+((data.saleNum*100)/(data.saleNum+data.stockNum)).toFixed(0)+"%");
                    $("#percent_notice").css("width",((data.saleNum*100)/(data.saleNum+data.stockNum)).toFixed(2)+"%");
                    if(data.to_start>0){
                        $("#mark_date").html("距开始");
                        $(".time-djs").addClass("bg-gray");
						$("#buyBtn").hide();
						$("#addCartBtn").hide();
                        timer(data.to_start/1000);
                    }else if(data.to_end>0){
                        $("#mark_date").html("距结束");
                        timer(data.to_end/1000);
                    }else{
                        $(".time-djs").addClass("bg-gray");
                        $(".date-time").html("活动已结束");
						$("#buyBtn").hide();
						$("#addCartBtn").hide();
                    }

                    $(".time-djs").css("display","block");
                } else if (data.marketType == 3) {
                    marketType = "预售";
                    $("#deposit_num").html("￥"+data.firstPrice.toFixed(2));
                    $("#deposit").css("display","block");
                }
                
                if(data.stockNum==0){
                	$("#buyBtn").hide();
                }
                
                $("#goodsPrice_2").html("￥" + (data.marketPrice*0.01).toFixed(2));
                $("#goodsNameDesc").html("【" + marketType + "】"+ $("#goodsNameDesc").html());
                $("#stockNum").html(data.stockNum);
                $("#stockNum_2").html(data.stockNum);
            }

        }
    })
}

var _loginInfo = {'isLogin':'1'};
$(function () {
	
	checkSelect();
	
    getGoodsActivity();

    

    /**
     * 确认
     */
    $("#confirmBtn").on("click",function () {
        var choosedAttr = $("input[name='userGoodsCarList[0].goodsStandardAttr']").val();
        choosedAttr = choosedAttr.replaceAll("=","：")
        $("#chooseAttrDiv").text("已选 " + choosedAttr);
        $(".xzcs").attr("style","right: -100%;");
        $(".model-bg").attr("style","left: 100%;");
    });

    /**
     * 购买数量失去焦点事件
     */
    $("#phoneGMNum").blur(function(){
        var goodsBuyNum = $(this).val();
        $("input[name='userGoodsCarList[0].goodsBuyNum']").val(goodsBuyNum);
    });

    /**
     * 立即购买
     */
    $("#buyBtn").on("click",function () {
        var stockNum = new Number($("#stockNum").attr("valNum"));
     	if( stockNum <= 0 || $(this).hasClass("btn-gray"))
     	{
     	   return;
     	}

        var goodsBuyNum = $("input[name='userGoodsCarList[0].goodsBuyNum']").val();
        if(goodsBuyNum > stockNum){
            showAlert("购买数量不能超过库存数量");
            return;
        }

        var choosedAttr = $("input[name='userGoodsCarList[0].goodsStandardAttr']").val();
        if(choosedAttr.length == 0){
            showAlert("请选择商品规格");
            return;
        }

		var reg = new RegExp("(^|&)marketId=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		var marketId = "";
		if(r!=null){
			marketId = unescape(r[2]);
		}
		if(marketId==""){
			$("#goodsBuyForm").attr("action",baseProject+"goodsBuy/linkToConfirmOrder");
		}
		else {
			$("#goodsBuyForm").attr("action",baseProject+"/act/seckill");
			$("input[name='userGoodsCarList[0].marketId']").val(marketId);	
			$("#activityID").val(marketId);	
		}
		
		$("#goodsBuyForm").submit();
    });

    /**
     * 加入购物车
     */
    $("#addCartBtn").on("click",function () {
    	var stockNum = new Number($("#stockNum").attr("valNum"));
     	if( stockNum <= 0 || $(this).hasClass("btn-gray"))
     	{
     	   return;
     	}
        var choosedAttr = $("input[name='userGoodsCarList[0].goodsStandardAttr']").val();
        if(choosedAttr.length == 0){
            showAlert("请选择商品规格");
            return;
        }

        var url = baseProject + "cart/addCart";
        var param = $("#goodsBuyForm").serializeObject();
        $.post(url,param,function (data) {
            if(data == "OK"){
                showAlert("商品已成功加入购物车！");
				loadShopCateGoodsNum();
            }
        });
    });

    getLoginInfo(afterGetLoginInfo);

    /**
     * 商品关注
     */
    $("#attention").on("click",function () {
        var goodsId = $("#goodsId").val();
        var attentionType = 0;
        if($(this).hasClass('active')){
            attentionType = 1;
        }else{
            attentionType = 0;
        }
        $(this).toggleClass("active");
        $.ajax({
            url:baseProject + "goodsQuery/addOrDelGoodsAttention",
            type:"post",
            data:{"goodsId" : goodsId,"attentionType" : attentionType},
            dataType:"json",
            success:function (data) {
                if(!data.isSuccess){
                    $("#attention").toggleClass("active");
                    showAlert(data.msg);
                }
            }
        });
    });

});


/**
 * 取得用户的登录信息
 * @param afterGetLoginInfo  回调方法
 */
function getLoginInfo(afterGetLoginInfo) {
    $.ajax({
        url : baseProject + "indexLoad/isLogin",
        type :"post",
        dataType :"json",
        success:function (data) {
            _loginInfo = data;
            afterGetLoginInfo(data);
        },
        error : function () {

        }
    });
}

/**
 * 取得用户登录信息后执行的方法;
 */
function afterGetLoginInfo(data) {
    if(_loginInfo.isLogin == '1'){//没有登录
        if($("#attention").hasClass('active')){
            $("#attention").removeClass("active");
        }
    }else{
        queryUserIsAttentionGoods();
    }
}
/**
 * 查询用户是否已关注此商品
 * return true:已关注;
 * false:没有关注;
 */
function queryUserIsAttentionGoods() {
    var goodsId = $("#goodsId").val();
    var isAttention = false;
    $.ajax({
        url:baseProject + "goodsQuery/queryGoodsIsAttention",
        type:"post",
        data:{goodsId : goodsId},
        dataType:"text",
        success:function (data) {
            if(data){
                if(!$("#attention").hasClass('active')){
                    $("#attention").addClass('active');
                }
            }else{
                if($("#attention").hasClass('active')){
                    $("#attention").removeClass('active');
                }
            }
        }
    });
}
/**
 * 替换所有
 * @param str1
 * @param str2
 * @returns {string}
 */
String.prototype.replaceAll = function (str1,str2){
    var str = this;
    var result = str.replace(eval("/"+str1+"/gi"),str2);
    return result;
}

/**
 * 将表单元素序列化为JSON对象
 * 基于jQuery serializeArray()
 * @returns {{}}
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