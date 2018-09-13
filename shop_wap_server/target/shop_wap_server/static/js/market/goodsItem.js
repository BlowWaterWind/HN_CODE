function getGoodsWater(params){
	var goodsType = $("#goodsTypeUl li.on").find("input[name='goodsType']").val();
    alert("aa");
	$.ajax({
		type:"POST",
		url:baseProject+"goodsInfo/getGoodsWater",
		data:{'params':params,"goodsType":goodsType,"goodsId":$("#goodsId").val()},
		dataType:"json",
		success:function(data){
			 var goodsWater=data.goodsWater;
			 $("#saleNum").html(goodsWater.saleNum);
			 $("#stockNum").html(goodsWater.stockNum);
			 $("#act_stockNum").html(goodsWater.stockNum);
			 if(goodsWater.stockNum==0){
				$("#stockNotice").css("display","block");
			}
			 $("#skuPrice").html("￥"+(goodsWater.goodsSkuPrice/100).toFixed(2));
			 $("input[name='userGoodsCarList[0].goodsSalePrice']").val(goodsWater.goodsSkuPrice);
			 if(goodsWater.goodsDeposit){
				 $("#firstPrice").html("￥"+(goodsWater.goodsDeposit/100).toFixed(2));
				 $("#lastPrice").html("￥"+((goodsWater.goodsSkuPrice-goodsWater.goodsDeposit)/100).toFixed(2));
			 }
			}
		});
}

function checkSelect(){
	setTimeout("checkSelect2()",100);
}

function checkSelect2(){
	var attrs_ul=$("ul[id^='attr_']");
	var params='';
	for(var i=0;i<attrs_ul.size();i++){
		var attrs_li=attrs_ul.eq(i).find(".on");
		if(attrs_li.size()==1){
			params+=attrs_ul.eq(i).attr("attrName")+"="+attrs_li.find('a').html();
			if(attrs_ul.size()>i+1){
				params+="&";
				}
			}else{
				params="";
				break;
		   }
	}

	$("input[name='userGoodsCarList[0].goodsStandardAttr']").val(params);//设置商品属性
	console.log(params);
	getGoodsWater(params);
}

function mycarousel_initCallback(carousel) {
	$("#mycarousel li").mouseover( function() {
		var JQ_img = $("img", this);
		$("#_middleImage").attr("src", JQ_img.attr('src_o')).attr("longdesc", JQ_img.attr('src_b'));
		$(this).siblings().each(function() {
			$("img", this).removeClass().addClass("curr_base");
		});
		JQ_img.addClass("cur_on");
	 });
}

/*选择商品类型*/
var chooseGoodsType = function($obj){
	var goodsType = $obj.find("input[name='goodsType']").val();
	$("input[name='userGoodsCarList[0].goodsType']").val(goodsType);
	if(goodsType == 2){
		$("input[name='applyNum']").attr("readonly","");
		$("#chooseContractBtn").hide();
		$(".favor").addClass("hide");
		$(".divPhoneNum").hide();
		$("#addCartBtn").show();

	}else{
		$("input[name='applyNum']").attr("readonly","true");
		$("input[name='applyNum']").val(1);
		$("#addCartBtn").hide();
		$("#chooseContractBtn").show();
		$("#divPhoneNum").show();
	}
}

// 合约选择弹出框
function queryAgreementInfo(agreeTerm,commitMonthlyBegin,commitMonthlyEnd){
	// 获取裸机价格的值
	var goodsSalePrice = $("input[name='userGoodsCarList[0].goodsSalePrice']").val();
	goodsSalePrice = ( goodsSalePrice / 100 ).toFixed(2);
	$.ajax({
		type:'post',
		url:baseProject+'goodsInfo/queryAgreementList',
		data:{"agreeTerm":agreeTerm,"commitMonthlyBegin":commitMonthlyBegin,"commitMonthlyEnd":commitMonthlyEnd},
		success:function(data)
		{
			for(var i=0;i<data.length;i++)
			{
				var tatolReturn = ((data[i].refundMonthly * (data[i].resHoldMonth-1))/100+(data[i].refundLastMonth/100)).toFixed(2);
				data[i] = $.extend(data[i],{"phonePrice":goodsSalePrice,"tatolReturn":tatolReturn});
			}
			console.log(JSON.stringify(data));
			// 渲染1
			var dataSet = {"agreementLeveList":data};
			$("#agreementLeveList").html(template("agreementLeveListTmpl",dataSet));

			// 渲染2
			var dataExtendSet = {"agreementPriceList":data};
			$("#agreementPriceList").html(template("agreementPriceListTmpl",dataExtendSet));

			var refundMonthly = new Number($("#agreementLeveList li").eq(0).attr("refundMonthly"));
			var resHoldMonth = new Number($("#agreementLeveList li").eq(0).attr("resHoldMonth"));
			var refundLastMonth = new Number($("#agreementLeveList li").eq(0).attr("refundLastMonth"));
			var tatolReturn = ( ( ( refundMonthly * ( resHoldMonth-1 ) ) + refundLastMonth )/100).toFixed(2);


			// 优惠计算公式
			$("#prepaidMoney").html(tatolReturn+"元");
			$("#phoneMoney").html($("#agreementLeveList li").eq(0).attr("phonePrice")+"元");
			$("#payMoney").html($("#agreementLeveList li").eq(0).attr("phonePrice")+"元");
			$("#saveMoney").html(tatolReturn+"元");
			$("#needPayMoney").html(tatolReturn+"元");

			// 选择数据暂时表格
			$("#tdPhonePrice").html($("#agreementLeveList li").eq(0).attr("phonePrice")+"元");
			$("#tdImgPhonePrice  span").html($("#agreementLeveList li").eq(0).attr("phonePrice")+"元");
			$("#tdTatolReturn").html( tatolReturn+"元");
			$("#tdResHoldMonth").html($("#agreementLeveList li").eq(0).attr("resHoldMonth")+"个月");
			$("#tdCommitMonthly").html( ($("#agreementLeveList li").eq(0).attr("commitMonthly")/100).toFixed(2)+"元");

			// 提交数据填充
			$("input[name='orderDetailContract.condId']").val($('#agreementLeveList li').eq(0).attr('condId'));
			$("input[name='orderDetailContract.agreeName']").val($('#agreementLeveList li').eq(0).attr('agreeName'));
			$("input[name='orderDetailContract.agreeShortName']").val($('#agreementLeveList li').eq(0).attr('agreeShortName'));
			$("input[name='orderDetailContract.schemeType']").val($('#agreementLeveList li').eq(0).attr('schemeType'));
			$("input[name='orderDetailContract.agreeTerm']").val($('#agreementLeveList li').eq(0).attr('schemeType'));
			$("input[name='orderDetailContract.commitMonthly']").val($('#agreementLeveList li').eq(0).attr('commitMonthly'));
			$("input[name='orderDetailContract.period']").val($('#agreementLeveList li').eq(0).attr('period'));
			$("input[name='orderDetailContract.refundMonthly']").val($('#agreementLeveList li').eq(0).attr('refundMonthly'));
			$("input[name='orderDetailContract.refund1stMonth']").val($('#agreementLeveList li').eq(0).attr('refundMonthly'));
			$("input[name='orderDetailContract.refundLastMonth']").val($('#agreementLeveList li').eq(0).attr('refundLastMonth'));
			$("input[name='orderDetailContract.notes']").val($('#agreementLeveList li').eq(0).attr('notes'));
			$("input[name='orderDetailContract.startDate']").val($('#agreementLeveList li').eq(0).attr('startDate'));
			$("input[name='orderDetailContract.endDate']").val($('#agreementLeveList li').eq(0).attr('endDate'));
			$("input[name='orderDetailContract.resHoldMonth']").val($('#agreementLeveList li').eq(0).attr('resHoldMonth'));
			$("input[name='orderDetailContract.subsidiesProprotion']").val($('#agreementLeveList li').eq(0).attr('subsidiesProprotion'));
			$("input[name='orderDetailContract.depositRatio']").val($('#agreementLeveList li').eq(0).attr('depositRatio'));
			$("input[name='orderDetailContract.presentProprotion']").val($('#agreementLeveList li').eq(0).attr('presentProprotion'));


			$(".favor").removeClass("hide");
			$(".divPhoneNum").show();

			// 选择套餐联动
			$("#agreementLeveList li").on("click",function(){
				refundMonthly = new Number($(this).attr("refundMonthly"));
				resHoldMonth = new Number($(this).attr("resHoldMonth"));
				refundLastMonth = new Number($(this).attr("refundLastMonth"));
				tatolReturn = ( ( ( refundMonthly * ( resHoldMonth-1 ) ) + refundLastMonth )/100).toFixed(2);
				// 优惠计算公式
				$("#prepaidMoney").html(tatolReturn+"元");
				$("#phoneMoney").html($(this).attr("phonePrice")+"元");
				$("#payMoney").html($(this).attr("phonePrice")+"元");
				$("#saveMoney").html(tatolReturn+"元");
				$("#needPayMoney").html($(this).attr("phonePrice")+"元");

				// 选择数据暂时表格
				$("#tdPhonePrice").html($(this).attr("phonePrice")+"元");
				$("#tdImgPhonePrice span").html($(this).attr("phonePrice")+"元");
				$("#tdTatolReturn").html( tatolReturn+"元");
				$("#tdResHoldMonth").html($(this).attr("resHoldMonth")+"个月");
				$("#tdCommitMonthly").html(($(this).attr("commitMonthly")/100).toFixed(2)+"元");

				// 提交数据填充
				$("input[name='orderDetailContract.conId']").val($(this).attr('condId'));
				$("input[name='orderDetailContract.agreeName']").val($(this).attr('agreeName'));
				$("input[name='orderDetailContract.agreeShortName']").val($(this).attr('agreeShortName'));
				$("input[name='orderDetailContract.schemeType']").val($(this).attr('schemeType'));
				$("input[name='orderDetailContract.agreeTerm']").val($(this).attr('agreeTerm'));
				$("input[name='orderDetailContract.commitMonthly']").val($(this).attr('commitMonthly'));
				$("input[name='orderDetailContract.period']").val($(this).attr('period'));
				$("input[name='orderDetailContract.refundMonthly']").val($(this).attr('refundMonthly'));
				$("input[name='orderDetailContract.refund1stMonth']").val($(this).attr('refundMonthly'));
				$("input[name='orderDetailContract.refundLastMonth']").val($(this).attr('refundLastMonth'));
				$("input[name='orderDetailContract.notes']").val($(this).attr('notes'));
				$("input[name='orderDetailContract.startDate']").val($(this).attr('startDate'));
				$("input[name='orderDetailContract.endDate']").val($(this).attr('endDate'));
				$("input[name='orderDetailContract.resHoldMonth']").val($(this).attr('resHoldMonth'));
				$("input[name='orderDetailContract.subsidiesProprotion']").val($(this).attr('subsidiesProprotion'));
				$("input[name='orderDetailContract.depositRatio']").val($(this).attr('depositRatio'));
				$("input[name='orderDetailContract.presentProprotion']").val($(this).attr('presentProprotion'));


				$(".favor").removeClass("hide");
				$(".divPhoneNum").show();

			});

		},
		dataType:'json'
	});

}

$(function() {
	chooseGoodsType($("#goodsTypeUl li.on"));

	checkSelect();

	$("#mycarousel").jcarousel({
		initCallback : mycarousel_initCallback
	});

	$(".jqzoom").jqueryzoom({
		xzoom : 370,
		yzoom : 350,
		offset : 10,
		position : "right",
		preload : 1,
		lens : 1
	});

	/*减少商品购买数量*/
	$("a[name='applyNum_operate_minus']").on("click",function(){
		var goodsType = $("#goodsTypeUl li.on input[name='goodsType']").val();
		if(goodsType == '1'){
			return ;
		}
		var $applyNum = $("input[name='applyNum']");
		var goodsBuyNum = $applyNum.val();
		if(goodsBuyNum > 1){
			$applyNum.val(--goodsBuyNum);
			$("input[name='userGoodsCarList[0].goodsBuyNum']").val(goodsBuyNum);
		}
	});

	/*增加商品购买数量*/
	$("a[name='applyNum_operate_add']").on("click",function(){
		var goodsType = $("#goodsTypeUl li.on input[name='goodsType']").val();
		if(goodsType == '1'){
			return ;
		}
		var $applyNum = $("input[name='applyNum']");
		var goodsBuyNum = $applyNum.val();
		if(goodsBuyNum < 101){
			$applyNum.val(++goodsBuyNum);
			$("input[name='userGoodsCarList[0].goodsBuyNum']").val(goodsBuyNum);
		}
	});

	/*选择购买方式*/
	$("#goodsTypeUl li").on("click",function(){
		chooseGoodsType($(this));
		checkSelect();
	});

	/*立即购买*/
	$("#buyBtn").on("click",function(){
			/*checkLogin();
			var inputPhoneNum =  $("#inputPhoneNum").val();
			var goodsType = $("#goodsTypeUl li.on input[name='goodsType']").val();
			var PATTERN_CHINAMOBILE = /^1(3[4-9]|5[012789]|8[23478]|4[7]|7[8])\d{8}$/;
			if((typeof(inputPhoneNum) == 'undefined' || inputPhoneNum =='') && goodsType == '1'){
				alert("请输入正确的手机号码！");
				return ;
			}
			$("input[name='orderDetailContract.phone']").val($("#inputPhoneNum").val());*/
			if($("#agreeBtn").is(":checked")){
				$("#goodsBuyForm").attr("action",baseProject+"goodsBuy/linkToConfirmOrder");
				$("#goodsBuyForm").submit();
			}else{

			}
	});


	/*加入购物车*/
	$("#addCartBtn").on("click",function(){

		if($("#agreeBtn").is(":checked")){
			$("#goodsBuyForm").attr("action",baseProject+"cart/addCart");
			$("#goodsBuyForm").submit();
		}
	});

	// 隐藏号码输入框 和 合约选择弹出框
	$(".favor").addClass("hide");
	$(".divPhoneNum").hide();

	$('.pop_btn').on('click',function() {
		// 请求后台参数
		// 1、请求合约数据
		queryAgreementInfo(3,2800,5800);
		$(".backMask").show();
		$(".addpop").show();
	});

	// 点击详情 进入
	$('.pop_btn_detail').on('click',function() {
		// 请求后台参数
		// 1、请求合约数据
		$(".backMask").show();
		$(".addpop").show();
	});

	//选择合约期
	$("#agreementTermChoice li a").on('click',function(){
		var agreeTerm = $(this).attr("agreeTerm");
		var commitMonthlyBegin = $(this).attr("commitMonthlyBegin");
		var commitMonthlyEnd = $(this).attr("commitMonthlyEnd");
		queryAgreementInfo(agreeTerm,commitMonthlyBegin,commitMonthlyEnd);
	});

	// 点击确定按钮关闭 弹出框和遮罩
	$(".addpop .button-sure").on("click",function(){
		$(".backMask").hide();
		$(".addpop").hide();
	});

});
	
	

