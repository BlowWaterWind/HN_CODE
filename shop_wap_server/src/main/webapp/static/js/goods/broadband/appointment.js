$(function() {



	$("#a_apply").click(function(){

		var phoneNum = $("#phoneNum").val();
		var contacts = $("#contacts").val();
		var city = $("#eparchySelect").val();
		var address = $("#addressDetail").val();
		var houseCode = $("#houseCode").val();
		var road = $("#road").val();
		$("#eparchyCode").val(city);
		var cityText = $("#eparchySelect").find("option:selected").text();
		var epachyText = $("#city").find("option:selected").text();
		var streetText = $("#street").find("option:selected").text();
		var roadText = $("#road").find("option:selected").text();
		if($.trim(phoneNum)==""){
			 showAlert("请输入手机号码！");
			 $("#phoneNum").focus();
			 return ;
		}
		var isMobile =  /^1([0-9][0-9])\d{8}$/g;
		if(!isMobile.test($("#phoneNum").val())){
			 showAlert("手机号码格式不正确！");
			 $("#phoneNum").focus();
			 return ;
	    }
        if($.trim(road) == ''){
			showAlert("请选择街道！");
			$("#contacts").focus();
			return ;
		}
		if($.trim(contacts)==""){
			showAlert("请输入联系人！");
			$("#contacts").focus();
			return ;
		}
		if($.trim(address)=="" || address=="详细街道、小区、门牌号"){
			showAlert("请输入地址！");
			return ;
		}
		var address = cityText+','+epachyText+','+streetText+','+roadText+$("#addressDetail").val();
		$("#address").val(address);
		$("#form1").attr("action","broadBandBook");
		showLoadPop("正在提交...");
		
		$("#form1").submit();
		
		
	});
	
	
	$("#phoneNum").on("blur",function(){
		var  isFw = $("#fw").is(":checked");
		if(isFw){
			//查询用户是否有宽带和魔百和
			var url = ctx + "/broadband/heFamilyCheck";
			var phoneNum = $("#phoneNum").val();
			var productId = $("#submit_productId").val();
			var city = $("#memberRecipientCity").val();
			if(phoneNum=="" || productId==""){
				return ;
			}
			var isMobile =/^1(([0-9][0-9]))\d{8}$/g;
			if(!isMobile.test($("#phoneNum").val())){
				 return ;
		    }
			
			var params = {phoneNum:phoneNum,productId:productId,city:city};
			  $.post(url,params,function(data){
				  if(data.respCode=="0"){
					  if(data.isBroadBand=="1"){
						  $("#bw").attr("checked","checked");
						  $("#submit_hasBroadband").val("0");
					  }
					  else {
						  $("#bx").attr("checked","checked");
						  $("#submit_hasBroadband").val("1");
					  }
					  if(data.isMbh=="1"){
						  $("#xw").attr("checked","checked");
						  $("#submit_hasMbh").val("0");
					  }
					  else {
						  $("#mx").attr("checked","checked");
						  $("#submit_hasMbh").val("1");
					  }
				  
				  }
				  else {
					  showAlert(data.respDesc);
				  }
				 
			  });
	}
		
		
		
		
		
		
	});
	
	 $(".select-renew li").click(function(){
		 var sHeight=$('.renew-content').height(),sHeight2=$('.hy-tab').height(); 
		 var  x= $(this).offset().top;
		 var desc =$(this).children("input[name='desc']").val();
		 var productId = $(this).children("input[name='productId']").val();
		 var packageId = $(this).children("input[name='packageId']").val();
		 var discntCode = $(this).children("input[name='discntCode']").val();
	     $("#submit_productId").val(productId);
		 $("#submit_packageId").val(packageId);
		 $("#submit_discntCode").val(discntCode);
		 
		 $("#p_desc").html(desc);
			 if($(this).hasClass("on")){
				
			     $('.hy-tab').height( sHeight2-sHeight);
				  $(".select-renew li").css("marginTop",5);
				 $(this).parent().parent().find('.renew-content').slideUp();
				  $(this).removeClass('on');
				  $('.active-fix').addClass('kd-disabled');
			 } else
			 {    
			       var _index=  $(this).index(),
				      s= Math.ceil(parseInt( _index+1)/3),
					  _lenth= $(".select-renew li").length;
					  m=s*3;
					
					 if(m<=_lenth)
					 {
				    for(var i=m;i<=m+2;i++) 
					{
					 $(".select-renew li:eq("+i+")").css("marginTop",sHeight);
					}
					 }
			       $(".select-renew li").removeClass('on');
				  $(this).addClass('on');
				  $('.hy-tab').height( sHeight2+sHeight);
			     $(this).parent().parent().find('.renew-content').css("top",x+15);
			    $(this).parent().parent().find('.renew-content').slideDown();
				 
				 $('.active-fix').removeClass('kd-disabled');
				 
			 }

	 });
	
	
	
	
	
	
	
});

/**
 * 选中和家庭宽带
 * @param obj
 */
function chooseHeBroadband(obj){
	$(obj).addClass('on').siblings().removeClass('on');
	var desc =$(obj).children("input[name='desc']").val();
	var productId = $(obj).children("input[name='productId']").val();
	var packageId = $(obj).children("input[name='packageId']").val();
	var discntCode = $(obj).children("input[name='discntCode']").val();
	
	$("#submit_productId").val(productId);
	$("#submit_packageId").val(packageId);
	$("#submit_discntCode").val(discntCode);
	
	
	$("#p_desc").html(desc);
	$(".renew-content").attr("style",'top: 127px; overflow: hidden; display: block');
	$(".renew-content").show();
}


/**
 * 选中宽带
 * @param obj
 */
function chooseBroadband(obj){
	$(obj).addClass('on').siblings().removeClass('on');
}



