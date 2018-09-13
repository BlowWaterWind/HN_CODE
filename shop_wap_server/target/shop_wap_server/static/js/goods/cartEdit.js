$(function(){
	var goodsCheckedCount = 0;
	
	//删除当前行商品
	$(".confirmtw").each(function(){
		$(this).click(function(){
			//显示二次确认对话框
			/*$(".share-bit").slideDown('fast');
		    $(".more-box").addClass("on");*/
			var goodsNum = $(this).parents('div[type="shopDiv"]').find("li").length;
			if(goodsNum == 1){
				$(this).parents('div[type="shopDiv"]').remove();
			}else{
				$(this).parents("li").remove();
			}
			
			$("#userGoodsCarListForm").submit();
		});
	});
	
	/*移入我的收藏*/
	$("#collectBtn").click(function(){
		$("#userGoodsCarListForm").attr("action","addMyFollow");
		$("#userGoodsCarListForm").submit();
	});
	
	/*删除选中的商品*/
	$("#deleteBtn").click(function(){
		('input[type=checkbox][name!=allCheck]:checked').each(function(){
			if($(this).attr("shopCheck") == "shopCheck"){
				$(this).parents('div[type="shopDiv"]').remove();
			}
			
			if($(this).attr("goodsCheck") == "goodsCheck"){
				$(this).parents("li").remove();
			}
		});
		
		$("#userGoodsCarListForm").submit();
	});
	
	//全选或全不选
	$("#allCheck").click(function(){
		var allCheck = $(this).attr("checked");
		
		if(allCheck == "checked"){
			$("input[type='checkbox']").attr("checked","checked");
		}else{
			$("input[type='checkbox']").removeAttr("checked");
		}
		
		var goodsCheckedCount = $('input[type="checkbox"][goodsCheck="goodsCheck"]:checked').length;
		$("#collectBtn").val("移入我的关注("+goodsCheckedCount+")");
		$("#deleteBtn").val("删除("+goodsCheckedCount+")");
	});
	
	/*店铺全选或全不选*/
	$("input[shopCheck='shopCheck']").each(function(){
		$(this).click(function(){
			var shopCheck = $(this).attr("checked");
			var shopIdName = $(this).attr("shopIdName");
			
			if(shopCheck == "checked"){
				goodsCheckedCount++;
				$('input[shopIdName="'+shopIdName+'"][goodsCheck="goodsCheck"]').attr("checked","checked");
			}else{
				goodsCheckedCount--;
				$('input[shopIdName="'+shopIdName+'"][goodsCheck="goodsCheck"]').removeAttr("checked");
			}
			
			$("#collectBtn").val("移入我的关注("+goodsCheckedCount+")");
			$("#deleteBtn").val("删除("+goodsCheckedCount+")");
			
			$('input[shopIdName="'+shopIdName+'"][goodsCheck="goodsCheck"]').each(function(){
				var skuId = $(this).attr("skuId");
				
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
			
		});
	});
	
	/*商品选择*/
	$("input[goodsCheck='goodsCheck']").each(function(){
		$(this).click(function(){
			var goodsCheck = $(this).attr("checked");
			var shopIdName = $(this).attr("shopIdName");
			
			if(goodsCheck == "checked"){
				goodsCheckedCount++;
			}else{
				goodsCheckedCount--;
				$('input[shopCheck="shopCheck"][shopIdName="'+shopIdName+'"]').removeAttr("checked");
			}
			
			$("#collectBtn").val("移入我的关注("+goodsCheckedCount+")");
			$("#deleteBtn").val("删除("+goodsCheckedCount+")");
			
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
		});
	});
	
	
});