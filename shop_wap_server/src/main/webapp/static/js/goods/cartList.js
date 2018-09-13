var flag = 0;
$(function(){
	var goodsSalePriceTotal = 0;

    $("*[elementType='cartEdit']").hide();

	/*购物车编辑和完成*/
	$("#editOrSave").live("click",function () {
		if($(this).text() == "编辑"){
			$(this).text("完成");
			$("*[elementType='cartList']").hide();
			$("a[name='deleteBtn']").show();
			$("#editFooterDiv").show();
			$(".sm-number").show();
			$(".tb-jg").hide();
			$(".tb-number").hide();
			$(".tabcon-fr").addClass("tb-sm03");
			
			setDelNum();
		}else{
			$(this).text("编辑");
			$("*[elementType='cartList']").show();
			$("a[name='deleteBtn']").hide();
			$("#editFooterDiv").hide();
			$(".sm-number").hide();
			$(".tabcon-fr").removeClass("tb-sm03");
			$(".tb-jg").show();
			$(".tb-number").show();
		}
	});

	/**
	 * 更新购物车
	 * @param $obj
     */
	var updateCart = function(){
		var $goodsBuyNum = $(this).siblings("input[name$='goodsBuyNum']");
		var goodsBuyNum = parseInt($goodsBuyNum.val());

		if($(this).hasClass("add")){
			goodsBuyNum++;
			if(goodsBuyNum > 100){
				showAlert("购买数量不能大于100");
				return;
			}
		}

		if($(this).hasClass("min")){
			goodsBuyNum--;
			if(goodsBuyNum < 1){
				showAlert("购买数量不能小于1");
				return;
			}
		}

		var $goodsItemLi = $(this).parents("li[name='goodsItemLi']");
		var goodsSkuId = $goodsItemLi.find("input[name$='goodsSkuId']").val();
		var param = {"goodsSkuId":goodsSkuId,"goodsBuyNum":goodsBuyNum};
		var url = ctx + "/cart/updateCart";

		$.post(url,param,function (data) {
			if(data == "OK"){
				// 修改完成页展示数量
				$goodsItemLi.find("span[class='tb-number']").text("x" + goodsBuyNum);
				var goodsTotalPrice = 0;
				$('input[type="checkbox"][goodsCheck="goodsCheck"]:checked').each(function(){
					var goodsSalePrice = $goodsItemLi.find("input[name$='goodsSalePrice']").val();
					goodsSalePrice = parseFloat(goodsSalePrice)/100;

					goodsTotalPrice += goodsSalePrice * goodsBuyNum;
				});

				$goodsBuyNum.val(goodsBuyNum);
				$("#goodsSalePriceTotal").text("￥" + goodsTotalPrice.toFixed(2));
			}
		});

	}

	/*减少商品购买量*/
	$(".min").live("click",updateCart);
	
	/*增加商品购买量*/
	$(".add").live("click",updateCart);
	
	/*商品数量改变事件*/
	$(".text_box").bind("blur",function(){
		var goodsBuyNum = parseInt($(this).val());
		if(goodsBuyNum >100 ){
			$(this).val(100);
			showAlert("最多只能购买100件哦！");
		}
	});

	//删除当前行商品
	$("li").find("i").click(function(){
		$(".share-bit").slideDown('fast');
		$(".more-box").addClass("on");
		$(this).attr("alt", "del");
		flag = 0;
	});

	/*删除选中的商品*/
	$("#deleteBtn").click(function(){
		$(".share-bit").slideDown('fast');
		$(".more-box").addClass("on");
		flag = 1;
	});

	/*移入我的收藏*/
	$("#collectBtn").click(function(){
		
		$.ajax({
			type:'POST',
			url:ctx + "goodsQuery/addMyFollow",
			data: $("#userGoodsCarListForm").serialize(),
			success:function(data){
				if(data == "0"){
					showAlert("加入我的关注成功!");
				} else {
					showAlert("加入我的关注失败!");
				}
			}
		});
	});

	/*全选*/
	$("#allCheck").click(function(){
		if($(this).is(":checked")){
			goodsSalePriceTotal = 0;
			$("input[type='checkbox']").attr("checked","checked");
		}else{
			$("input[type='checkbox']").removeAttr("checked");
		}
		
		$('input[type="checkbox"][goodsCheck="goodsCheck"]').each(function(){
			var skuId = $(this).attr("skuId");
			// 解决数据重复时计算不对
			var obj = $(this).parents("li[name='goodsItemLi']");
			var goodsSalePrice = $.trim(obj.find('span[skuId="'+skuId+'"]').text()).substring(1);
			var goodBuyNum = obj.find('input[name$="goodsBuyNum"][skuId="'+skuId+'"]').val();
			goodsSalePrice = parseFloat(goodsSalePrice) * 100;
			goodBuyNum = parseInt(goodBuyNum);

			if($(this).is(":checked")){
				goodsSalePriceTotal += goodsSalePrice * goodBuyNum;
				$(this).next().val("Y");
			}else{
				goodsSalePriceTotal = goodsSalePriceTotal * 100 - goodsSalePrice * goodBuyNum;
				$(this).next().val("N");
			}
			
			$("#goodsSalePriceTotal").text("￥" + (goodsSalePriceTotal/100).toFixed(2));
		});

		var goodsCheckedCount = $('input[type="checkbox"][goodsCheck="goodsCheck"]:checked').length;
		$("#collectBtn").val("移入我的关注("+goodsCheckedCount+")");
		$("#deleteBtn").val("删除("+goodsCheckedCount+")");
	});
	
	/*店铺选择*/
	$("input[shopCheck='shopCheck']").click(function(){
		var shopCheck = $(this).attr("checked");
		var shopIdName = $(this).attr("shopIdName");

		if(shopCheck == "checked"){
			$('input[shopIdName="'+shopIdName+'"][goodsCheck="goodsCheck"]').attr("checked","checked");
		}else{
			$('input[shopIdName="'+shopIdName+'"][goodsCheck="goodsCheck"]').removeAttr("checked");
		}

		$('input[shopIdName="'+shopIdName+'"][goodsCheck="goodsCheck"]').each(function(){
			var skuId = $(this).attr("skuId");
			var obj = $(this).parents("li[name='goodsItemLi']");
			var goodsSalePrice = $.trim(obj.find('span[skuId="'+skuId+'"]').text()).substring(1);
			var goodBuyNum = obj.find('input[name$="goodsBuyNum"][skuId="'+skuId+'"]').val();
			goodsSalePrice = parseFloat(goodsSalePrice);
			goodBuyNum = parseInt(goodBuyNum);

			var goodsCheck = $(this).attr("checked");

			if(goodsCheck != "checked"){
				goodsSalePriceTotal = goodsSalePriceTotal - goodsSalePrice * goodBuyNum;
				$(this).next().val("N");
			}else{
				goodsSalePriceTotal += goodsSalePrice * goodBuyNum;
				$(this).next().val("Y");
			}

			$("#goodsSalePriceTotal").text("￥"+goodsSalePriceTotal.toFixed(2));

			var goodsIsAllCheck = true;
			$('input[type="checkbox"][goodsCheck="goodsCheck"]').each(function(){
				if(!$(this).is(":checked")){
					goodsIsAllCheck = false;
				}
			});

			if(goodsIsAllCheck){
				$("#allCheck").attr("checked","checked");
			}else{
				$("#allCheck").removeAttr("checked");
			}

		});
		
		setDelNum();
	});
	
	/*商品选择*/
	$("input[goodsCheck='goodsCheck']").click(function(){
		var skuId = $(this).attr("skuId");
		var obj = $(this).parents("li[name='goodsItemLi']");
		var goodsSalePrice = $.trim(obj.find('span[skuId="'+skuId+'"]').text()).substring(1);
		var goodBuyNum = obj.find('input[name$="goodsBuyNum"][skuId="'+skuId+'"]').val();
		goodsSalePrice = parseFloat(goodsSalePrice);
		goodBuyNum = parseInt(goodBuyNum);

		var goodsCheck = $(this).attr("checked");
		var shopIdName = $(this).attr("shopIdName");

		if(goodsCheck != "checked"){
			goodsSalePriceTotal = goodsSalePriceTotal - goodsSalePrice * goodBuyNum;

			$('input[shopCheck="shopCheck"][shopIdName="'+shopIdName+'"]').removeAttr("checked");

			$(this).next().val("N");
		}else{
			goodsSalePriceTotal += goodsSalePrice * goodBuyNum;

			$(this).next().val("Y");
		}

		$("#goodsSalePriceTotal").text("￥"+goodsSalePriceTotal.toFixed(2));

		var shopGoodsIsAllCheck = true;
		$('input[goodsCheck="goodsCheck"][shopIdName="'+shopIdName+'"]').each(function(){
			if(!$(this).is(":checked")){
				shopGoodsIsAllCheck = false;
			}
		});
		if(shopGoodsIsAllCheck){
			$('input[shopCheck="shopCheck"][shopIdName="'+shopIdName+'"]').attr("checked","checked");
		}else{
			$('input[shopCheck="shopCheck"][shopIdName="'+shopIdName+'"]').removeAttr("checked");
		}

		var goodsIsAllCheck = true;
		$('input[type="checkbox"][goodsCheck="goodsCheck"]').each(function(){
			if(!$(this).is(":checked")){
				goodsIsAllCheck = false;
			}
		});
		if(goodsIsAllCheck){
			$("#allCheck").attr("checked","checked");
		}else{
			$("#allCheck").removeAttr("checked");
		}
		
        setDelNum();
	});
	
	/*结算*/
	$("#billingBtn").click(function(){
		var goodsItem = $("input[name$=goodsBuyNum]");
		var modified = false;
		$.each(goodsItem,function(index,val){
			if($(val).val()<=0){
                modified = true;
			}
		});
		if(modified){
            showAlert("您加入购物车的商品数量存在负数,请核实后再提交订单");
            return;
		}
		if($("input[goodsCheck='goodsCheck']:checked").length > 0){
			$("#userGoodsCarListForm").attr("action",ctx +"/goodsBuy/linkToConfirmOrder");
			$("#userGoodsCarListForm").submit();
		}		
	});
    
	$("#delBtn").click(function(){
		if (flag == 0) {
			var obj = $("li").find("i[alt='del']");
			var $shopDiv = obj.parents("div[type='shopDiv']");
			var $goodsItemLi = obj.parents("li[name='goodsItemLi']");
			var goodsSkuId = $goodsItemLi.find("input[name$='goodsSkuId']").val();

			$.ajax({
				type:'POST',
				url:ctx + "/cart/deleteCart",
				data:{"goodsSkuIds[]":[goodsSkuId]},
				success:function(data){
					if(data == "success"){
						var goodsNum = $shopDiv.find("li").length;
						if(goodsNum == 1){
							$shopDiv.remove();
						}else{
							$goodsItemLi.remove();
						}
						
						setDelNum();
					}
				}
			});
		} else if (flag == 1) {
			var goodsSkuIds = [];
			var $checkedGoods = $("input[type='checkbox'][goodsCheck='goodsCheck']:checked");
			$checkedGoods.each(function(){
				var goodsSkuId = $(this).parents("li[name='goodsItemLi']").find("input[type='hidden'][name$='goodsSkuId']").val();
				goodsSkuIds.push(goodsSkuId);
			});

			$.ajax({
				type:'POST',
				url:ctx + "/cart/deleteCart",
				data:{"goodsSkuIds[]":goodsSkuIds},
				success:function(data){
					if(data == "success"){
						$checkedGoods.each(function(){
							var $currentShopDiv = $(this).parents("div[type='shopDiv']");
							var $goodsItemLi = $currentShopDiv.find("li[name='goodsItemLi']");
                            var size = $currentShopDiv.find("li input[type='checkbox']:checked").size();
                            var len = $goodsItemLi.length;
							if(len == 1 || len == size){
								$currentShopDiv.remove();
							}else{
								$goodsItemLi.remove();
							}

						});
						
						setDelNum();
					}
				}
			});
		}
	});

    $(".cancel").click(function(){
        $(".share-bit").slideUp('fast');
		$(".more-box").removeClass("on");
		$("i[alt='del']").removeAttr("alt");
	});
    
    /**
     * 重置数据
     */
    function setDelNum(){
    	var size = $("li input[type='checkbox']:checked").size();
		$("#collectBtn").val("移入我的关注(" + size + ")");
		$("#deleteBtn").val("删除(" + size + ")");
    }
});