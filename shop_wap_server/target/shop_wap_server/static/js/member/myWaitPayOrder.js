$(function() {

/*$(".orderDetailBtn").click(function() {
	var subOrderId =	$(this).data("index");
	window.location.href=orderDetailUrl+"?orderId="+subOrderId;

});*/

$(document.body).on('click','.orderDetailBtn',function(){
	var subOrderId =	$(this).data("index");
	window.location.href=orderDetailUrl+"?orderId="+subOrderId;
});


$(".groupOrderDetailBtn").click(function() {
	var subOrderId =	$(this).data("index");
	window.location.href=orderDetailUrl+"?type=1&&orderId="+subOrderId;

});

$(".commentOrderBtn").click(function() {
	var subOrderId =	$(this).data("index");
	window.location.href=commentOrderUrl+"?orderDetailId="+subOrderId;

});

$(".deleteOrderBtn").click(function() {
	var subOrderId =	$(this).data("index");
	window.location.href=deleteOrderUrl+"?orderId="+subOrderId;

});


$(".logisticBtn").click(function() {
	var subOrderId =	$(this).data("index");
	var logisticsNum =	$(this).data("logisticsNum");
	window.location.href=logisticUrl+"?orderId="+subOrderId+"&logisticsNum="+logisticsNum;

});

$("a.sendGoodsWarnBtn").click(function() {
	var orderSubId=$(this).data("index");
	$.ajax({
		type : 'post',
		url : sendGoodsWarnUrl,
		cache : false,
		context : $(this),
		dataType : 'text',
		data : {
			orderSubId:orderSubId
		},
		success : function(data) {
			var res=data.replace('\"','').replace('\"','');
			if(res!="success"){
				showAlert(res);
			}else{
				showAlert("提醒成功！");
				window.location.reload();
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			showAlert("请求失败");
		}
	});

});

$(".backGoodsBtn").click(function() {
	var subOrderId =	$(this).data("index");
	window.location.href=backGoodsUrl+"?orderId="+subOrderId;
});

$("#waitPay").click(function() {
	window.location.href=waitPayUrl;
});

$("#waitSend").click(function() {
	window.location.href=waitSendGoodsUrl;
});

$("#waitReceive").click(function() {
	window.location.href=waitReceiveGoodsUrl;

});

$("#waitComment").click(function() {
	window.location.href=waitCommentUrl;

});


$("#paidBtn").click(function() {
	var subOrderId =	$(this).data("index");
	window.location.href=payUrl+"?subOrderIds="+subOrderId;

});

$("#cancelBtn").click(function() {
	var subOrderId =	$(this).data("index");
		$.post(cancelUrl,{subOrderId:subOrderId}, function(data) {
			if(data!="success"){
				showAlert(data);
			}else{
				showAlert("删除成功！");
				window.location.href=gotoUrl;
			}
		});

});

});