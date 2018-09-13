Array.prototype.indexOf = function(val) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == val)
			return i;
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
 * 
 * @param obj
 * @returns {Boolean}
 */
Array.prototype.containsByNocase = function(obj) {
	var i = this.length;
	while (i--) {
		for (var j = 0; j < obj.length; j++) {
			if (obj[j] == this[i]) {
				return true;
			}
		}

	}
	return false;
}

/**
 * 跟据 prodAttrId 和prodAttrValueId 取goodsSkuId
 * 
 * @param currentAttrId
 * @returns
 */
function getGoodsSkuIdByProdAttrIdAndValue(prodAttrId, prodAttrValueId) {
	var goodsSkuArray = new Array();
	for (var i = 0; i < goodsSkuAttrList.length; i++) {
		var goodsSA = goodsSkuAttrList[i];
		if (prodAttrId == goodsSA.prodAttrId
				&& prodAttrValueId == goodsSA.prodAttrValueId) {
			goodsSkuArray.push(goodsSA.goodsSkuId);
		}
	}
	return goodsSkuArray;
}

// 以上代码可公用

/**
 * 根据全选的属性选择goodsSkuId
 */
function getGoodsSkuId() {
	// 获取选中的 goodsSkuAttr
	var $attr_ul = $("ul[id^='attrUl_']");
	var goodsSkuIdArraySet = new Array();
	var $attr_li_on = $attr_ul.eq(0).find(".list-active");
	var prodAttrId = $attr_li_on.find(">a").attr("prodAttrId");
	var prodAttrValueId = $attr_li_on.find(">a").attr("prodAttrValueId");
	var goodsSkuIdArraySet = getGoodsSkuIdByProdAttrIdAndValue(prodAttrId,
			prodAttrValueId);

	for (var i = 1; i < $attr_ul.size(); i++) {
		var $attr_li_on = $attr_ul.eq(i).find(".list-active");
		var prodAttrId = $attr_li_on.find(">a").attr("prodAttrId");
		var prodAttrValueId = $attr_li_on.find(">a").attr("prodAttrValueId");
		var goodsSkuIdArray = getGoodsSkuIdByProdAttrIdAndValue(prodAttrId,
				prodAttrValueId);
		for (var j = 0; j < goodsSkuIdArraySet.length; j++) {
			if (goodsSkuIdArray.indexOf(goodsSkuIdArraySet[j]) == -1) {
				goodsSkuIdArraySet.splice(j, 1);
				j--;
			}
		}
	}
	if (goodsSkuIdArraySet.length >= 1)
		return goodsSkuIdArraySet[0];
	return null;
}

/**
 * 增加了 sku 集筛选后的 联动选择 sku 属性
 * 
 * @param currentAttrId
 */
function LinkageSelection(currentAttrId) {
	var $attrA = $("#attrLi_" + currentAttrId + " a");
	// 已经被置灰的按钮 不能点
	if ($("#attrLi_" + currentAttrId).hasClass("gray")) {
		return;
	}

	// 取sku数据
	var goodsSkuId = $attrA.attr("goodsSkuId");
	var prodAttrId = $attrA.attr("prodAttrId");
	var prodAttrValueId = $attrA.attr("prodAttrValueId");

	// 当前选中
	$("#attrUl_" + prodAttrId + ">li").removeClass("list-active");
	$("#attrLi_" + currentAttrId).addClass("list-active");

	// 取当前属性的 goodsSkuId
	var goodsSkuIdArray = getGoodsSkuIdByProdAttrIdAndValue(prodAttrId,
			prodAttrValueId);

	// 将后面的不能选的sku属性置灰，且置为不能选
	var isLastLi = false;
	for (var i = 0; i < strGoodsSkuProdAttrList.length; i++) {
		var goodsSkuAttr = strGoodsSkuProdAttrList[i];
		if (isLastLi) {
			// ul下的li数组
			var $attr_lis = $("#attrUl_"
					+ strGoodsSkuProdAttrList[i].prodAttrId + ">li");
			// 循环li数组 来判断能不能选
			for (var k = 0; k < $attr_lis.size(); k++) {
				var $attr_li = $attr_lis.eq(k);
				var $attr_li_a = $attr_li.find("a");
				var a_prodAttrId = $attr_li_a.attr("prodAttrId");
				var a_prodAttrValueId = $attr_li_a.attr("prodAttrValueId");
				var a_goodsSkuIdArray = getGoodsSkuIdByProdAttrIdAndValue(
						a_prodAttrId, a_prodAttrValueId);

				// 当前属性不能选
				if (!goodsSkuIdArray.containsByNocase(a_goodsSkuIdArray)) {
					$attr_li.removeClass("list-active");
					$attr_li.addClass("gray");
				}
				// 当前属性能选
				else {
					// 得到当前 ul 下的 已被选中的属性
					var $attr_li_on = $("#attrUl_"
							+ strGoodsSkuProdAttrList[i].prodAttrId
							+ " >li.list-active");
					var $li_on_a = $attr_li_on.find(">a");
					var on_a_prodAttrId = $li_on_a.attr("prodAttrId");
					var on_a_prodAttrValueId = $li_on_a.attr("prodAttrValueId");
					var on_a_goodsSkuIdArray = getGoodsSkuIdByProdAttrIdAndValue(
							on_a_prodAttrId, on_a_prodAttrValueId);
					// 如果已经被选中的属性 与前面选中的属性 互斥
					if (!goodsSkuIdArray.containsByNocase(on_a_goodsSkuIdArray)
							|| $attr_li_on.size() <= 0) {
						$attr_li_on.removeClass("list-active").addClass("gray");
						$attr_li.addClass("list-active");
					}
					$attr_li.removeClass("gray");
				}
			}
		} else {
			// 标记以后循环的 ul 是后面的ul
			if (goodsSkuAttr.prodAttrId == prodAttrId) {
				isLastLi = true;
			}
		}

	}
}

function getGoodsWater(goodsSkuId, params) {
	var goodsType = $("input[name='userGoodsCarList[0].goodsType']").val();
	removeMessage();
	$.ajax({
		type : "POST",
		url : baseProject + "goodsInfo/getGoodsWater",
		data : {
			'params' : params,
			"goodsId" : $("#goodsId").val(),
			"goodsSkuId" : goodsSkuId,
			"goodsType" : goodsType
		},
		dataType : "json",
		success : function(data) {
			var goodsWater = data.goodsWater;
			var marketInfo = data.marketInfo;
			if (!goodsWater.goodsSalePrice) {
				$("#goodsPrice_2").html("抱歉，暂无报价！");
				$("#goodsPrice").html("抱歉，暂无报价！");
                $("#stockNum").html(0);
				$("#buyBtn").removeClass("btn-rose").addClass("btn-gray");
				if (typeof(dcsPageTrack)=="function"){
					dcsPageTrack("WT.rh_info","缺货",true);
				}

				$("#addCartBtn").removeClass("btn-blue").addClass("btn-gray");
				return;
			}
			// 参加活动能够的商品不能加入购物车
			if (marketInfo) {
				$("#addCartBtn").hide();
				goodsMaxNum = marketInfo.goodsMaxNum;
			}

			$("#buyBtn").removeClass("btn-gray").addClass("btn-rose");
			if (typeof(dcsPageTrack)=="function"){
				dcsPageTrack("WT.rh_info","有货",true);
			}
			$("#addCartBtn").removeClass("btn-gray").addClass("btn-blue");

			$("#stockNum").html(goodsWater.stockNum);
			$("#stockNum").attr("valNum", goodsWater.stockNum);
			if (goodsWater.stockNum == 0) {
				$("#addCartBtn").removeClass("btn-blue").addClass("btn-gray");
				$("#buyBtn").removeClass("btn-rose").addClass("btn-gray");
				if (typeof(dcsPageTrack)=="function"){
					dcsPageTrack("WT.rh_info","缺货",true);
				}
			}
			if (goodsWater.goodsSkuId) {
				$("#goodsPrice_2").html(
						"￥" + (goodsWater.goodsSalePrice * 0.01).toFixed(2));
				$("#goodsPrice +small").html("元");
				$("#goodsPrice").html(
						"￥" + (goodsWater.goodsSalePrice * 0.01).toFixed(2));
				$("#killPrice").html(
						"￥" + (goodsWater.goodsSkuPrice * 0.01).toFixed(2));// 秒杀价
				$("#stockNum_2").html(goodsWater.stockNum);
				$("input[name='userGoodsCarList[0].goodsSalePrice']").val(
						goodsWater.goodsSalePrice);
				$("#numberSelectDiv").show();

				//设置集团尊享价
				var goodsGroupPrice = goodsWater.goodsGroupPrice;
				if(goodsGroupPrice){
					if($("#goodsGroupPriceDesc").length==0)
					$("#goodsPriceDesc").after($("<div class=\"sp-jg\" id=\"goodsGroupPriceDesc\">\n" +
												 "  <span>集团尊享价：</span>\n" +
												 "  <strong id=\"goodsGroupPrice\">" + goodsGroupPrice + "</strong>\n" +
												 "  <small>元</small>\n" +
												 "</div>\n" +
												 "<div class=\"sp-jg\" id=\"goodsPriceDesc\">\n" +
												 "  <span>手机号码：</span>\n" +
												 "  <input id=\"mobilePhoneNo\" maxlength='11' type=\"text\" class=\"sr-sk\"/>\n" +
												 "  <a id=\"verifyBtn\" href=\"javascript:;\" class=\"btn btn-blue\">验证</a>\n" +
												 "</div>"));

					//设置只能输入数字
					$("#mobilePhoneNo").keyup(function(){  //keyup事件处理
						$(this).val($(this).val().replace(/\D|^0/g,''));
					}).bind("paste",function(){  //CTR+V事件处理
						$(this).val($(this).val().replace(/\D|^0/g,''));
					}).css("ime-mode", "disabled");  //CSS设置输入法不可用

					$("#verifyBtn").live("click",function(){
						var mobilePhoneNo = $.trim($("#mobilePhoneNo").val());
						if(mobilePhoneNo.length == 0){
							showAlert("手机号码不能为空！");
							return;
						}

						//验证手机号码
						var partrn = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
						if(!partrn.test(mobilePhoneNo)){
							showAlert("请输入正确的手机号码！");
							return;
						}

						//判断输入的手机号码是否为目标客户
						var url = baseProject + "/goodsBuy/verifyIs3HighUser";
						var param = {"mobilePhoneNo":mobilePhoneNo};
						$.post(url,param,function (data) {
							if(data.is3HighUser+"" == 'true'){
								showAlert("输入的手机号码是目标客户，能享受集团尊享价！");
							}else{
								showAlert("输入的手机号码不是目标客户，不能享受集团尊享价！");
							}
						});

					});
					if(goodsType=='2'){
						//是集团手机，再进行判断用户登录者是否符合购买此手机的条件,1是否是三高用户，2是否实名认证，3是否办理了合适套餐
						is3higher($("#goodsId").val(),goodsSkuId,goodsGroupPrice);//价格使用集团尊享价格
					}
				}
			}
			$("#ratingCount").html(data.userRatingCount);
			if (goodsWater.state != 4) {
				$("#buyBtn").hide();
				$("#addCartBtn").hide();
				if (typeof(dcsPageTrack)=="function"){
					dcsPageTrack("WT.rh_info","不能购买",true);
				}
			} else {
				$("#buyBtn").removeClass('hide');
				$("#addCartBtn").removeClass('hide');
				if (typeof(dcsPageTrack)=="function"){
					dcsPageTrack("WT.rh_info","可以购买",true);
				}
			}

			if(goodsType == 1){//选择合约机
				$("#addCartBtn").addClass("hide");//隐藏加入购物车按钮
				$("#phoneNumDiv").removeClass("hide");

				var contractId = $("input[name='orderDetailContract.contractId']").val();
				if(contractId.length > 0){
					$("#table_select_contact_info").removeClass("hide");
				}
			}else if(goodsType == 2){//选择裸机
				$("#addCartBtn").removeClass("hide");//显示加入购物车按钮
				$("#phoneNumDiv").addClass("hide");
				$("#table_select_contact_info").addClass("hide");
			}

		}
	});
	
	
	/*$.ajax({
		type : "POST",
		url : baseProject + "goodsBuy/queryRedBagPrice",
		data : {
			"goodsId" : $("#goodsId").val()
		},
		dataType : "json",
		success : function(data) {
			if(data!=null){
				$("#redBag").show();
				$("#redBagNum").html(data);
			}
		}
	});*/
	
}

function checkSelect(currentAttrId) {
	// 点击第一个属性
	if (!currentAttrId) {
		var $attr_lis = $("#attrUl_" + strGoodsSkuProdAttrList[0].prodAttrId
				+ ">li");
		var $attr_li_a = $attr_lis.eq(0).find(">a");
		$attr_li_a.click();
	} else {
		LinkageSelection(currentAttrId);
		// 获取选中的 goodsSkuAttr
		var $attr_ul = $("ul[id^='attrUl_']");
		var params = '';
		var paramsName = '';
		var selectAttrLength = $("ul[id^='attrUl_'] .list-active");
		if (selectAttrLength < strGoodsSkuAttrDistinctListList.length) {
			$("#goodsPrice +small").html("");
			$("#goodsPrice").html("抱歉，暂无报价");
			$("#goodsPrice2").html("抱歉，暂无报价");
			return;
		}

		$("#buyBtn").removeClass("btn-gray").addClass("btn-rose");
		try{
		if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.rh_info","有货",true);}
		}catch(e){
			
		}
		$("#addCartBtn").removeClass("btn-gray").addClass("btn-blue");

		for (var i = 0; i < $attr_ul.size(); i++) {
			var $attr_li_on = $attr_ul.eq(i).find(".list-active");
			if ($attr_li_on.size() == 1) {
				params += $attr_li_on.find(">a").attr("prodAttrId") + "="
						+ $attr_li_on.find('>a').attr("prodAttrValueId");
				if (i < $attr_ul.size() - 1) {
					params += "&";
				}
			} else {
				params = "";
				break;
			}
		}

		var skuAttrValues = [];
		for (var i = 0; i < $attr_ul.size(); i++) {
			var $attr_li_on = $attr_ul.eq(i).find(".list-active");
			if ($attr_li_on.size() == 1) {
				var skuAttrValue = $.trim($attr_li_on.find('>a').text());
				skuAttrValues.push('"' + skuAttrValue + '"');
				paramsName += $attr_ul.eq(i).attr("attrName") + "="
						+ skuAttrValue;
				if (i < $attr_ul.size() - 1) {
					paramsName += "&";
				}
			} else {
				paramsName = "";
				break;
			}
		}
		// 通过属性集获取goodsSkuId
		var goodsSkuId = getGoodsSkuId();
		$("input[name='userGoodsCarList[0].goodsSkuId']").val(goodsSkuId);// 设置商品SKUID
		$("input[name='userGoodsCarList[0].goodsStandardAttrId']").val(params);// 设置商品属性ID
		$("input[name='userGoodsCarList[0].goodsStandardAttr']").val(paramsName);// 设置商品属性名称
		$("#chooseGoodsAttr").text("已选：" + skuAttrValues.join(""));
		getGoodsWater(goodsSkuId, params);
	}

}

function selectPhoneType() {
	// 获取选中的 goodsSkuAttr
	var $attr_ul = $("ul[id^='attrUl_']");
	var params = '';
	var paramsName = '';
	for (var i = 0; i < $attr_ul.size(); i++) {
		var $attr_li_on = $attr_ul.eq(i).find(".list-active");
		if ($attr_li_on.size() == 1) {
			params += $attr_li_on.find(">a").attr("prodAttrId") + "="
					+ $attr_li_on.find('>a').attr("prodAttrValueId");
			if (i < $attr_ul.size() - 1) {
				params += "&";
			}
		} else {
			params = "";
			break;
		}
	}
	for (var i = 0; i < $attr_ul.size(); i++) {
		var $attr_li_on = $attr_ul.eq(i).find(".list-active");
		if ($attr_li_on.size() == 1) {
			paramsName += $attr_ul.eq(i).attr("attrName") + "="
					+ $attr_li_on.find('>a').html();
			if (i < $attr_ul.size() - 1) {
				paramsName += "&";
			}
		} else {
			paramsName = "";
			break;
		}
	}
	// 通过属性集获取goodsSkuId
	var goodsSkuId = getGoodsSkuId();
	$("input[name='userGoodsCarList[0].goodsSkuId']").val(goodsSkuId);// 设置商品SKUID
	$("input[name='userGoodsCarList[0].goodsStandardAttrId']").val(params);// 设置商品属性ID
	$("input[name='userGoodsCarList[0].goodsStandardAttr']").val(paramsName);// 设置商品属性名称
	getGoodsWater(goodsSkuId, params);
}

/**
 * 输入数字限制 , 以及最大限购数量
 * 
 * @param event
 */
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
		if (null == goodsMaxNum
				|| parseInt($("#stockNum_2").html()) < goodsMaxNum) {
			if (parseInt($("#stockNum_2").html()) < (parseInt(numbx))) {
				showAlert("尊敬的客户：您好！购买数量不能超过商品库存数！");
				$('#phoneGMNum').val(1);
				$("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(1));
				return;
			}
		} else {
			if (goodsMaxNum < (parseInt(numbx) + 1)) {
				showAlert("尊敬的客户：您好！购买数量不能超过商品最大限购数量：" + goodsMaxNum);
				$('#phoneGMNum').val(goodsMaxNum);
				$("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(goodsMaxNum));
				return;
			}
		}

		var goodType = $("input[name='userGoodsCarList[0].goodsType']").val();
		if(goodType == 1 && (numbx+1) > 1){
			showAlert("尊敬的客户：您好！合约机最大限购数量为：1");
			$('#phoneGMNum').val(1);
			$("input[name='userGoodsCarList[0].goodsBuyNum']").val(1);
			return;
		}

		$('#phoneGMNum').val(numbx * 1 + 1);
		$("input[name='userGoodsCarList[0].goodsBuyNum']").val(
				parseInt(numbx) + 1);
	} else if (obj == 'del') {
		if (parseInt(numbx) > 1) {
			$('#phoneGMNum').val(numbx * 1 - 1);
			$("input[name='userGoodsCarList[0].goodsBuyNum']").val(
					parseInt(numbx) - 1);
			return;
		}
	} else {
		if (isNaN(numbx) || parseInt(numbx) < 1) {
			showAlert("尊敬的客户：您好，购买数量只能输入大于0数字！");
			$('#phoneGMNum').val(1);
			$("input[name='userGoodsCarList[0].goodsBuyNum']").val(parseInt(1));
			return;
		} else {
			if (null == goodsMaxNum
					|| parseInt($("#stockNum_2").html()) < goodsMaxNum) {
				if (parseInt($("#stockNum_2").html()) < (parseInt(numbx))) {
					showAlert("尊敬的客户：您好！购买数量不能超过商品库存数！");
					$('#phoneGMNum').val(1);
					$("input[name='userGoodsCarList[0].goodsBuyNum']").val(
							parseInt(1));
					return;
				}
			} else {
				if (goodsMaxNum < (parseInt(numbx) + 1)) {
					showAlert("尊敬的客户：您好！购买数量不能超过商品最大限购数量：" + goodsMaxNum);
					$('#phoneGMNum').val(goodsMaxNum);
					$("input[name='userGoodsCarList[0].goodsBuyNum']").val(
							parseInt(goodsMaxNum));
					return;
				}
			}
			$('#phoneGMNum').val(numbx);
			$("input[name='userGoodsCarList[0].goodsBuyNum']").val(numbx);
			return;
		}
	}

}

var voices;//套餐数据
/**
 * 根据选择的合约查询套餐、计划消费计算结果
 */
function queryPackagesBySelectedContract(){
	var contractId = $("#contractUl li.list-active input[name='contractId']").val();
	var eparchyCode = $("input[name='orderDetailContract.eparchyCode']").val();
	var phoneNumber = $("#phoneNumber").val();
	var paramData = {"serialNumber":phoneNumber,"contractId":contractId,"inEparchyCode":eparchyCode};

	$("#modalLoad").show();
	$.ajax({
		   url:baseProject + "goodsInfo/queryContractPackages",
		   type: "post",
		   data: paramData,
		   dataType: "json",
		   success: function (data) {
			   if(data.resultCode == "fail"){
				   showAlert(data.resultInfo);
				   return;
			   }

			   voices = data.result.VOICES;
			   $("#monthUl").html(template("contractMonths",{"contractMonths":data.result.CONTRACT_MONTHS}));
			   $("#voiceUl").html(template("voices",{"voices":data.result.VOICES}));
			   $("#elementUl").html(template("elements",{"elements":voices[0].VOICE_DISCNTS}));

			   //设置折扣
			   $("input[name='orderDetailContract.discount']").val(data.result.CONTRACT_INFO.RETURN_RATE);

			   //查询计划消费计算结果
			   var contractMonth = data.result.CONTRACT_MONTHS[0].ATTR_FIELD_CODE;
			   var consume = data.result.VOICES[0].VOICE_DISCNTS[0].PRICE;
               var productTypeCode=$("input[name='orderDetailContract.productTypeCode']").val();
			   var packageParam = {"contractMonth":contractMonth,"consume":consume,"contractId":contractId,"productTypeCode":productTypeCode,
				   "eparchyCode":eparchyCode,"serialNumber":phoneNumber};
			   queryContractSaleConCal(packageParam);
		   }
	   });
}

/**
 * 查询计划消费计算结果
 * @param packageParam
 */
function queryContractSaleConCal(packageParam) {
	$("#modalLoad").show();
	$.ajax({
		url:baseProject + "goodsInfo/queryContractSaleConCal",
		type: "post",
		data: packageParam,
		dataType: "json",
		success: function (data) {
			$("#modalLoad").hide();
			if(data.resultCode == "fail"){
				showAlert(data.resultInfo);
				return;
			}

			var giftFee = data.result.GIFT_FEE;
			var operFee = data.result.OPER_FEE;
			var depositFee = data.result.DEPOSIT_FEE;
			var fee1 = data.result.FEE1;
			var fee2 = data.result.FEE2;
			var fee3 = data.result.FEE3;
			var fee4 = data.result.FEE4;

			$("#giftFeeTd").text(giftFee / 100  + "元");
			$("#fee1Td").text(fee1 / 100  + "元");
			$("#fee2Td").text(fee2 / 100  + "元");

			$("input[name='orderDetailContract.depositFee']").val(giftFee);//赠送费（单位：分）
			$("input[name='orderDetailContract.operFee']").val(operFee);//购机款（单位：分）
			$("input[name='orderDetailContract.advancePay']").val(depositFee);//预存款（单位：分）
			$("input[name='orderDetailContract.fee1']").val(fee1);//除末月外每月赠送额（单位：分）
			$("input[name='orderDetailContract.fee2']").val(fee2);//末月赠送额（单位：分）
			$("input[name='orderDetailContract.fee3']").val(fee3);//除末月外每月返还额（单位：分）
			$("input[name='orderDetailContract.fee4']").val(fee4);//末月返还额（单位：分）

			$("#contractResultDiv").removeClass("hide");
		}
	});

    var exist= $("#elementUl li.active").find("input[name='exist']").val();
    if(exist=='TRUE'){
        $("#confirmContractBtn").html("已办理");
        $("#confirmContractBtn").removeClass("btn-rose").addClass("button-gray");
        $("#confirmContractBtn").removeAttr("data-dismiss");
    }else{
        $("#confirmContractBtn").html("确定");
        $("#confirmContractBtn").attr("data-dismiss","modal");
        $("#confirmContractBtn").removeClass("button-gray").addClass("btn-rose");

    }
}

/**
 * 合约计划用户资格查询
 * @param contractSaleCheckParam
 */
function contractSaleCheck() {
	var contractId = $("input[name='orderDetailContract.contractId']").val();//合约ID
	var serialNumber = $("input[name='orderDetailContract.serialNumber']").val();//办理手机号码
	var inEparchyCode = $("input[name='orderDetailContract.eparchyCode']").val();//保底消费金额（单位：分）
	var sumLimitPrice = $("input[name='orderDetailContract.sumLimitPrice']").val();//保底消费金额（单位：分）
	var promisionDuration = $("input[name='orderDetailContract.promisionDuration']").val();//合约时长
	var depositFee = $("input[name='orderDetailContract.depositFee']").val();//赠送费
	var advancePay = $("input[name='orderDetailContract.advancePay']").val();//预存款
	var operFee = $("input[name='orderDetailContract.operFee']").val();//购机款
	var productId = $("input[name='orderDetailContract.productId']").val();//产品ID
	var elementId = $("input[name='orderDetailContract.elementId']").val();//元素ID
	var contractSaleCheckParam = {"inEparchyCode":inEparchyCode,
								"serialNumber":serialNumber,
								"contractId":contractId,
								"sumLimitPrice":sumLimitPrice,
								"promisionDuration":promisionDuration,
								"depositFee":depositFee,
								"advancePay":advancePay,
								"operFee":operFee,
								"productStr":[{"PRODUCT_ID":productId,"ELEMENT_ID":elementId}]};

	var url = baseProject + "goodsInfo/contractSaleCheck";

	showLoadPop("数据加载中，请稍候……");
	$.ajax({
	        url: url,
	        type: "post",
	        data: contractSaleCheckParam,
	        dataType: "json",
	        success: function (data) {
	        	
	        	//没有担保号码
				//弹出担保号码输入
				if(data.resultCode == "nosureno"){ 
					hideLoadPop();
					popSureno();
					return;
				}
	        	
					if(data.resultCode == "fail"){
						hideLoadPop();
						showAlert(data.resultInfo);
						return;
					}

					$("#goodsBuyForm").attr("action",baseProject + "goodsBuy/linkToConfirmOrder");
					$("#goodsBuyForm").submit();
	        }
	});
}

var _loginInfo = {
	'isLogin' : '1'
};

function getShopInfo() {
	var shopId = $('input[name="userGoodsCarList[0].shopId"]').val();
	if (shopId != undefined && shopId != "") {
		$.ajax({
			type: "POST",
			url: baseProject + "goodsInfo/getShopDetail",
			data: {'shopId': shopId},
			dataType: "json",
			success: function (data) {
				var shopDetail = data.shop;
				$('input[name="userGoodsCarList[0].shopName"]').val(shopDetail.shopInfo.shopName);
				$('.sp-phone').html("客服电话："+shopDetail.shopInfo.servicePhone);
			}
		});
	}else{
		alert(11)
	}
}

$(function() {
	checkSelect();
	/*重新获取商户店铺信息 */
	getShopInfo();
	/* 确定商品规格属性 */
	$("#confirmAttrBtn").on("click", function() {
		var goodType = $("input[name='userGoodsCarList[0].goodsType']").val();
		var phoneGMNum = $("#phoneGMNum").val();
		if(goodType == 1 && phoneGMNum > 1){
			showAlert("尊敬的客户：您好！合约机最大限购数量为：1");
			$('#phoneGMNum').val(1);
			$("input[name='userGoodsCarList[0].goodsBuyNum']").val(1);
			return;
		}

		$(".model-bg").click();
		var goodsStandardAttr = $("input[name='userGoodsCarList[0].goodsStandardAttr']").val();
		goodsStandardAttr = goodsStandardAttr.replaceAll("=", "：").replaceAll("&", "，");
		$("#chooseGoodsAttr").text("已选择 " + goodsStandardAttr);
	});

	/**
	 * 购买数量失去焦点事件
	 */
	$("#phoneGMNum").blur(function() {
		phoneGMNumber(this);
	});

	/**
	 * 点击【查询合约套餐】按钮，查询合约计划列表
	 */
	$("#chooseContractBtn").on("click",function(){
		if($("#contractUl li").length > 0){
			return;
		}

		var phoneNumber = $.trim($("#phoneNumber").val());
		$("#modalLoad").show();
		$.ajax({
			url:baseProject + "goodsInfo/queryContractProducts",
			type: "post",
			data: {"phoneNumber":phoneNumber},
			data: {"phoneNumber":phoneNumber,"goodsId":$("#goodsId").val()},
			dataType: "json",
			success: function (data) {
				$("#modalLoad").hide();
				if(data.resultCode == "fail"){
					showAlert(data.resultInfo);
					$("#returnContractBtn").click();
					return;
				}
				$("input[name='orderDetailContract.eparchyCode']").val(data.eparchCode);
				$("#contractUl").html(template("contractProducts",{"contractProducts":data.contractProducts}));
                $("input[name='orderDetailContract.productTypeCode']").val(data.productTypeCode);
				queryPackagesBySelectedContract();
			}
		});
	});

	/**
	 * 选择合约，查询套餐信息
	 */
	$("#contractUl").on("click","li",function(){
		$(this).addClass('list-active').siblings().removeClass('list-active');

		var contractId = $(this).find("input[name='contractId']").val();
		var eparchyCode = $("input[name='orderDetailContract.eparchyCode']").val();
		var phoneNumber = $("#phoneNumber").val();
		var paramData = {"serialNumber":phoneNumber,"contractId":contractId,"inEparchyCode":eparchyCode};

		$("#modalLoad").show();
		$.ajax({
			url:baseProject + "goodsInfo/queryContractPackages",
			type: "post",
			data: paramData,
			dataType: "json",
			success: function (data) {
				if(data.resultCode == "fail"){
					showAlert(data.resultInfo);
					return;
				}

				voices = data.result.VOICES;
				$("#monthUl").html(template("contractMonths",{"contractMonths":data.result.CONTRACT_MONTHS}));
				$("#voiceUl").html(template("voices",{"voices":data.result.VOICES}));
				$("#elementUl").html(template("elements",{"elements":voices[0].VOICE_DISCNTS}));

				//设置折扣
				$("input[name='orderDetailContract.discount']").val(data.result.CONTRACT_INFO.RETURN_RATE);

				//查询计划消费计算结果
				var contractMonth = data.result.CONTRACT_MONTHS[0].ATTR_FIELD_CODE;
				var consume = data.result.VOICES[0].VOICE_DISCNTS[0].PRICE;
				var packageParam = {"contractMonth":contractMonth,"consume":consume,"contractId":contractId,"productTypeCode":"25","eparchyCode":eparchyCode,"serialNumber":phoneNumber};
				queryContractSaleConCal(packageParam);
			}
		});
	});

	/**
	 * 选择合约时长
	 */
	$("#monthUl").on("click","li",function(){
		$(this).addClass('list-active').siblings().removeClass('list-active');

		//查询计划消费计算结果
		var contractId = $("#contractUl li.list-active").find("input[name='contractId']").val();
		var eparchyCode = $("input[name='orderDetailContract.eparchyCode']").val();
		var phoneNumber = $("#phoneNumber").val();
		var contractMonth = $(this).find("input[name='contractMonth']").val();
		var consume = $("#elementUl li.active").find("input[name='price']").val();
        var productTypeCode=$("input[name='orderDetailContract.productTypeCode']").val();
		var packageParam = {"contractMonth":contractMonth,"consume":consume,"contractId":contractId,"productTypeCode":productTypeCode,
			"eparchyCode":eparchyCode,"serialNumber":phoneNumber};
		queryContractSaleConCal(packageParam);
	});

	/**
	 * 选择套餐
	 */
	$("#voiceUl").on("click","li",function(){
		$(this).addClass('list-active').siblings().removeClass('list-active');

		//根据选择的套餐过滤套餐档次
		var selectedProductId = $(this).find("input[name='productId']").val();
		$.each(voices,function(i,voice){
			if(voice.PRODUCT_ID == selectedProductId){
				$("#elementUl").html(template("elements",{"elements":voice.VOICE_DISCNTS}));
			}
		});

		var contractId = $("#contractUl li.list-active").find("input[name='contractId']").val();
		var eparchyCode = $("input[name='orderDetailContract.eparchyCode']").val();
		var phoneNumber = $("#phoneNumber").val();
		var contractMonth = $("#monthUl li.list-active").find("input[name='contractMonth']").val();
		var consume = $("#elementUl li.active").find("input[name='price']").val();
        var productTypeCode=$("input[name='orderDetailContract.productTypeCode']").val();
		var packageParam = {"contractMonth":contractMonth,"consume":consume,"contractId":contractId,"productTypeCode":productTypeCode,
			"eparchyCode":eparchyCode,"serialNumber":phoneNumber};
		queryContractSaleConCal(packageParam);
	});

	/**
	 * 选择套餐档次
	 */
	$("#elementUl").on("click","li",function(){
		$(this).addClass('active').siblings().removeClass('active');

		var contractId = $("#contractUl li.list-active").find("input[name='contractId']").val();
		var eparchyCode = $("input[name='orderDetailContract.eparchyCode']").val();
		var phoneNumber = $("#phoneNumber").val();
		var contractMonth = $("#monthUl li.list-active").find("input[name='contractMonth']").val();
		var consume = $("#elementUl li.active").find("input[name='price']").val();
        var productTypeCode=$("input[name='orderDetailContract.productTypeCode']").val();
		var packageParam = {"contractMonth":contractMonth,"consume":consume,"contractId":contractId,"productTypeCode":productTypeCode,
			"eparchyCode":eparchyCode,"serialNumber":phoneNumber};
		queryContractSaleConCal(packageParam);
	});

	/**
	 * 确认合约套餐
	 */
	$("#confirmContractBtn").on("click",function () {
		if($(this).attr("data-dismiss")!='modal'){
			return;
		}
		$("#tdGiftFee").text($("#giftFeeTd").text());
		$("#tdFee1").text($("#fee1Td").text());
		$("#tdFee2").text($("#fee2Td").text());
		$("#tdConsume").text($("#elementUl li.active a").text());
		$("#tdContractMonth").text($("#monthUl li.list-active a").text());

		var phoneNumber = $("#phoneNumber").val();
		var selectedContract = $("#contractUl li.list-active");
		var contractId = selectedContract.find("input[name='contractId']").val();
		var contractName = selectedContract.find("a").text();

		var selectedProduct = $("#voiceUl li.list-active");
		var productId = selectedProduct.find("input[name='productId']").val();
		var productName = selectedProduct.find("a").text();

		var selectedElement = $("#elementUl li.active");
		var consume = selectedElement.find("input[name='price']").val();
		var packageId = selectedElement.find("input[name='packageId']").val();
		var elementId = selectedElement.find("input[name='elementId']").val();
		var elementName = selectedElement.find("input[name='elementName']").val();

		var contractMonth = $("#monthUl li.list-active input[name='contractMonth']").val();

		$("input[name='orderDetailContract.serialNumber']").val(phoneNumber);//办理手机号码
		$("input[name='orderDetailContract.contractId']").val(contractId);//合约ID
		$("input[name='orderDetailContract.contractName']").val(contractName);//合约名称
		$("input[name='orderDetailContract.sumLimitPrice']").val(consume);//保底消费金额（单位：分）
		$("input[name='orderDetailContract.productId']").val(productId);//产品ID
		$("input[name='orderDetailContract.productName']").val(productName);//产品名称
		$("input[name='orderDetailContract.packageId']").val(packageId);//包ID
		$("input[name='orderDetailContract.elementId']").val(elementId);//元素ID
		$("input[name='orderDetailContract.elementName']").val(elementName);//元素名称
		$("input[name='orderDetailContract.promisionDuration']").val(contractMonth);//合约时长（单位：月）
		// $("input[name='orderDetailContract.productTypeCode']").val("25");//合约类型：25 - 收入合约

		$("#table_select_contact_info").removeClass("hide");
	});

	/**
	 * 立即购买
	 */
	$("#buyBtn").on("click",function() {
			var stockNum = new Number($("#stockNum").attr("valNum"));
			if (stockNum <= 0 || $(this).hasClass("btn-gray")) {
				return;
			}
			//加入验证集团用户购机
		    if($("#groupCheckRadio").length>0){//存在节点
				if(!$("#groupCheckRadio").is(":checked")){
					showAlert("享受购机优惠，须承诺购机当月起连续6个月内不取消或降低套餐档次（可办理升档）。如同意该项优惠购机条款，请确认勾选条款后，再进行购买。");
					return;
				}
			}
			var goodsBuyNum = $("input[name='userGoodsCarList[0].goodsBuyNum']").val();
			if (goodsBuyNum > stockNum) {
				showAlert("购买数量不能超过库存数量");
				return;
			}

			var goodsAttr = $("input[name='userGoodsCarList[0].goodsStandardAttr']").val();
			if (goodsAttr.length == 0) {
				showAlert("请选择商品规格");
				return;
			}

			var goodsType = $("input[name='userGoodsCarList[0].goodsType']").val();
			if (goodsType == 1) {// 合约机
				var serialNumber = $("#phoneNumber").val();
				$("input[name='orderDetailContract.serialNumber']").val(serialNumber);//办理手机号码
				var contractId = $("input[name='orderDetailContract.contractId']").val();//合约ID

				if(serialNumber.length == 0){
					showAlert("请输入办理手机号码");
					return;
				}
				if(contractId.length ==0){
					showAlert("请选择合约套餐");
					return;
				}

				contractSaleCheck();
			} else if (goodsType == 2) {// 裸机
				if($("#marketId").val()==""){
					$("#goodsBuyForm").attr("action",baseProject + "goodsBuy/linkToConfirmOrder");
				}
				else {
					$("#goodsBuyForm").attr("action",baseProject + "act/seckill");
				}
				$("#goodsBuyForm").submit();
			}
	});

	/**
	 * 加入购物车
	 */
	$("#addCartBtn").on("click",function() {
		var stockNum = new Number($("#stockNum").attr("valNum"));
		if (stockNum <= 0 || $(this).hasClass("btn-gray")) {
			return;
		}
		var choosedAttr = $(
				"input[name='userGoodsCarList[0].goodsStandardAttr']")
				.val();
		if (choosedAttr.length == 0) {
			showAlert("请选择商品规格");
			return;
		}

		var url = baseProject + "cart/addCart";
		var param = $("#goodsBuyForm").serializeObject();
		$.post(url, param, function(data) {
			if (data == "OK") {
				showAlert("商品已成功加入购物车！");
				loadShopCateGoodsNum();
			}else{
                showAlert(data);
			}
		});
	});

	/**
	 * 选择购买方式
	 */
	$("#goodsTypeUl").on("click","li",function(){
		$(this).addClass('list-active').siblings().removeClass('list-active');
		var goodsType = $(this).find("a").attr("goodsType");
		$("input[name='userGoodsCarList[0].goodsType']").val(goodsType);
		selectPhoneType();
	});

	getLoginInfo(afterGetLoginInfo);

	/**
	 * 商品关注
	 */
	$("#attention").on("click", function() {
		var obj = $(this);
		var url = baseProject + "indexLoad/isLogin";
		var params = {
			'isLogin' : "false"
		};
		$.post(url, params, function(data) {
			if (data.isLogin == "0") {
				var goodsId = $("#goodsId").val();
				var attentionType = 0;
				if (obj.hasClass('active')) {
					attentionType = 1;
				} else {
					attentionType = 0;
				}
				obj.toggleClass("active");
				$.ajax({
					url : baseProject + "goodsBuy/addOrDelGoodsAttention",
					type : "post",
					data : {
						"goodsId" : goodsId,
						"attentionType" : attentionType
					},
					dataType : "json",
					success : function(data) {
						if (!data.isSuccess) {
							$("#attention").toggleClass("active");
							showAlert(data.msg);
						}
					}
				});
			} else {
				showAlert("对不起，您还未登录，无法关注此商品，请去登录！");
				$("div[class='modal-footer'] > a").unbind();
				$("div[class='modal-footer'] > a").bind("click", function() {
					$("#alertMsgInfo_content").html("");
					$("#btnClose").click();

					var goodsId = $("#goodsId").val();
					var attentionType = 0;

					$.ajax({
						url : baseProject + "goodsBuy/addOrDelGoodsAttention",
						type : "post",
						data : {
							"goodsId" : goodsId,
							"attentionType" : attentionType
						},
						dataType : "json",
						success : function(data) {
							if (!data.isSuccess) {
								showAlert(data.msg);
							} else {
								$("#attention").addClass("active");
							}
						}
					});
				});
			}
		});

	});

	// resImg();
});

/**
 * 取得用户的登录信息
 * 
 * @param afterGetLoginInfo
 *            回调方法
 */
function getLoginInfo(afterGetLoginInfo) {
	$.ajax({
		url : baseProject + "indexLoad/isLogin",
		type : "post",
		dataType : "json",
		success : function(data) {
			_loginInfo = data;
			afterGetLoginInfo(data);
		},
		error : function() {

		}
	});
}

/**
 * 取得用户登录信息后执行的方法;
 */
function afterGetLoginInfo(data) {
	if (_loginInfo.isLogin == '1') {// 没有登录
		if ($("#attention").hasClass('active')) {
			$("#attention").removeClass("active");
		}
	} else {
		queryUserIsAttentionGoods();
	}
}
/**
 * 查询用户是否已关注此商品 return true:已关注; false:没有关注;
 */
function queryUserIsAttentionGoods() {
	var goodsId = $("#goodsId").val();
	var isAttention = false;
	$.ajax({
		url : baseProject + "goodsQuery/queryGoodsIsAttention",
		type : "post",
		data : {
			goodsId : goodsId
		},
		dataType : "text",
		success : function(data) {
			if (data == "true") {
				if (!$("#attention").hasClass('active')) {
					$("#attention").addClass('active');
				}
			} else {
				if ($("#attention").hasClass('active')) {
					$("#attention").removeClass('active');
				}
			}
		}
	});
}

String.prototype.replaceAll = function(str1, str2) {
	var str = this;
	var result = str.replace(eval("/" + str1 + "/gi"), str2);
	return result;
}

/**
 * 将表单元素序列化为JSON对象 基于jQuery serializeArray()
 */
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

function resImg() {
	try {
		var $img = $("#prodDesc img");
		var path = "";
		$img.each(function() {
			path = $(this).attr("src");
			if (path.indexOf("http") < 0) {
				$(this).attr("src", resImageSize(path, "415x500"));
				$(this).attr("style", "");
			}
		})
	} catch (e) {

	}
}
/**
 *根据是否是集团购机活动，
 * 3高用户购买此手机时，需要绑定套餐6个月
 * 1.首先判断此商品是不是符合集团专区的商品
 * 2.如果不是，则结束流程
 * 3.如果是，则需要让用户登录再进入该界面
 * 4.登录用户如果不是实名制，则提示用户非实名制用户，不能参与活动，但是可以购买
 * 5.如果是实名制，则判断是否是3高用户，如果是则必须订购套餐方可购买。
 * 6.如果不是3高用户，则不受此限制
 * */
function is3higher(goodsId,goodsSkuId,goodsPrice){
	$.ajax({
		type:"POST",
		url:baseProject+"is3hGoodsBuy/isGroupProduct3hBuy",
		data:{'goodsPrice':goodsPrice,"goodsId":goodsId,'goodsSkuId':goodsSkuId},
		//dataType:"json",
		success:function(data) {
			if(data=='-1'){//查询出错、或者非3高用户正常购买商品
				showAddCart();
				removeMessage();
				canbuy();
			}else if(data=='0'){//3高用户购买集团商品（已经实名认证）
				//可以购买，显示信息
				hiddenAddCart();
				canbuy();
				addMessage();
			}else if(data=='1'){//没有登录
				//提示登录
				if(confirm("此商品为集团购机专区商品，请先登录再进行购买")){
					window.location.href=baseProject+"login/toLogin";
				}else{
					window.location.href=baseProject+"login/toLogin";
				}
			}else if(data=='2'){//3高用户非实名购买集团专区，提示未实名，不可购买
				//未实名，不可购买
				addMessage();
				hiddenAddCart();
				cannotbuy();
				showAlert("尊敬的客户：参与优惠购机活动客户必须为实名制登记客户，您当前还未进行实名制认证无法参与该活动，感谢您的关注！");
			}else if(data=='3'){//3高实名用户购买，但是套餐不合理
				//套餐不合理，引导去订购套餐
				addMessage();
				hiddenAddCart();
				cannotbuy();
				if(goodsPrice>50000){
					showAlert("尊敬的客户：参与优惠购机活动客户必须办理'28元及以上4G套餐'或'180元及以上流量半年包、年包'或'办理30元及以上流量月套餐'，您当前无法参与该活动！点击链接<a href='http://www.hn.10086.cn/newservice/static/doBusiness/busiSecondPage.html'>【网上营业厅-业务办理】</a>");
				}else{
					showAlert("尊敬的客户：参与优惠购机活动客户必须办理'需办理18元及以上4G套餐’或‘办理20元及以上流量月套餐'，您当前无法参与该活动！点击链接<a href='http://www.hn.10086.cn/newservice/static/doBusiness/busiSecondPage.html'>【网上营业厅-业务办理】</a>");
				}

			}else if(data=='-2'){//已经购买过集团手机，不可再进行购买
				//套餐不合理，引导去订购套餐
				addMessage();
				hiddenAddCart();
				cannotbuy();
				showAlert("尊敬的客户,您在6个月之内购买过集团优惠手机，不可以再次购买，感谢您的关注！");
			}
		}
	});
}
function addMessage(){
	if($("#warninfo").length==0&&$("#goodsGroupPriceDesc").length>0)
		$("#goodsGroupPriceDesc").after('<p class="font-red" id="warninfo">温馨提示：参与优惠购机活动的客户必须实名制登记，且办理\'28及以上4G套餐\'、\'30元及以上流量月套餐\'；并承诺购机当月起连续6个月内不取消或降低套餐档次（可办理升档）</p>'
			+'<div id="groupCheckRadioDiv"><input type="radio" id="groupCheckRadio"/>同意并承诺购机当月起连续6个月不取消或降低4G套餐档次（可办理升档）</div>');
}
function removeMessage(){
	if($("#warninfo").length>0){
		$("#warninfo").remove();
		$("#groupCheckRadioDiv").remove();
	}
}
function cannotbuy(){
	$("#buyBtn").removeClass("btn-rose").addClass("btn-gray");
}
function canbuy(){
	$("#buyBtn").removeClass("btn-gray").addClass("btn-rose");
}
function hiddenAddCart(){
	$("#addCartBtn").hide();
}
function showAddCart(){
	$("#addCartBtn").show();
}

/**
 * 提示消息
 * @type 1 成功样式 2失败样式
 * @param obj
 */
function popSureno() {
	$('#surenoPop').modal("show");
}

function sendSmsCode(){	
	  // $("#btnSendCodeSureno").addClass("disabled");
	 var mobile = $.trim($("#sureno").val());
	  if (mobile == "")
    { 
		  $("#alertSureno_content").html("请输入手机号！");      
        return false;
    }
    var reg = /^1\d{10}$/;
    if (!reg.test(mobile))
    {       
        $("#alertSureno_content").html("请输入正确的手机号！");     
        return false;
    }
    
    var serialNumber = $("input[name='orderDetailContract.serialNumber']").val();//手机号码
    if(mobile==serialNumber){
  	  $("#alertSureno_content").html("担保号码不能是自己购机的号码。");     
        return false; 
        }      
    var url = baseProject + "goodsInfo/sendRandomCode";
    $('#btnSendCodeSureno').attr('disabled',"true");
    $.ajax({
		url: url,
		type: "post",
		data:   {'mobile':mobile},
		//dataType: "json",
		success: function (data) {
			if(data.resultCode == "fail"){  				
			  $("#alertSureno_content").html(data.resultInfo);       			 
			  $('#btnSendCodeSureno').removeAttr('disabled',"true");
 			}else{
			  $("#alertSureno_content").html(data.resultInfo);
			} 
		}
	}); 
    return false;
}

function checkSmsCode(){
	// $("#surenoPopBtn").addClass("disabled");	
	 var phone = $.trim($("#sureno").val());
   var code = $.trim($("#surenoCode").val());
   if (phone == null || phone == "") {
  	 $("#alertSureno_content").html("请输入手机号");
       return false;
   }
   if (code == null || code == "") {
  	 $("#alertSureno_content").html("请输入验证码");
       return false
   }
   
   var serialNumber = $("input[name='orderDetailContract.serialNumber']").val();//手机号码
   if(phone==serialNumber){
 	  $("#alertSureno_content").html("担保号码不能是购机的号码。");     
       return false; 
       }    
   
   var url = baseProject+ "goodsInfo/checkCode";
   if (checkPhone(phone)) {
   $('#surenoPopBtn').attr('disabled',"true");
   $.ajax({
		url: url,
		type: "post",
		data:   {'phoneNo': phone,'smsCode':code, 'flag': '1'},
		//dataType: "json",
		success: function (data) {
			if(data.resultCode == "fail"){  				
			  $("#alertSureno_content").html(data.resultInfo);  	 
			 $('#btnSendCodeSureno').removeAttr('disabled',"true");
			  $("#surenoPopBtn").removeAttr("disabled");
			}else{
			  $("#alertSureno_content").html(data.resultInfo);
			 $(".backMask").hide();
			 $("#surenoPop").hide();
			} 
		}
	 });
   } 
   return false;
}

//校验是否为移动手机号
function checkPhone(t){
//var tel = $("."+t).val();
var reg = /^1(3[4-9]|5[012789]|8[23478]|4[7]|7[8])\d{8}$/;
if (reg.test(t)) {
    return true;
}else{
	  $("#alertSureno_content").html("请输入11位中国湖南移动手机号码！");     
    return;
};
}

function trim(str){ //删除左右两端的空格
  return str.replace(/(^s*)|(s*$)/g, "");
}

$(function(){
	// 判断cookie中是否有isShowHead
	var isShowHeadVal = getCookie("isShowHead");
	if(isShowHeadVal != null && isShowHeadVal == "1"){
		hideshareandserver();
	}
});
/**
 * 根据name的值获取cookie
 * @param name
 * @returns {null}
 */
function getCookie(name) {
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)){
		return unescape(arr[2]);
	}
	else{
		return null;
	}
}
/**
 * 隐藏分享和客服按钮
 */
function hideshareandserver() {

	$('.ac-link04').hide();
	$('.ac-link01').hide();
}