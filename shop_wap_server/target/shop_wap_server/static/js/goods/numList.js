$(function () {
    getIpv6Address();
    /**
     * 选择普号或靓号
     */
    $("#numTypeUl li").on("click",function(){
        $(this).addClass("on");
        $(this).siblings("li").removeClass("on");

        var numTypeCode = $(this).find("a").attr("numTypeCode");
        var queryListpageId = $(this).find("a").attr("queryListpageId");
        var queryListpageCode = $(this).find("a").attr("queryListpageCode");
        $("input[name='numTypeCode']").val(numTypeCode);
        $("input[name='queryListpageId']").val(queryListpageId);
        $("input[name='queryListpageCode']").val(queryListpageCode);

        $("#queryNumForm").submit();
    });

    /**
     * 选择查询条件
     */
    $("ul[name='conditionUl'] li").live("click",function(){
        $(this).addClass("list-bg");
        $(this).siblings("li").removeClass("list-bg");

        var valueName = $(this).find("a").attr("valueName");
        var conditionIdName = $(this).find("a").attr("conditionIdName");
        $(this).find("input[type='hidden']").eq(0).attr("name",valueName);
        $(this).find("input[type='hidden']").eq(1).attr("name",conditionIdName);

        $(this).siblings("li").find("input").removeAttr("name");
    });

    /**
     * 确定
     */
    $("#confirmBtn").on("click",function () {
        //关闭查询条件弹出层
        $(".drawer-overlay-upper").hide();
        $(".drawer-main").animate({right:'-100%'});
        $("body").removeClass('open');
        try{
            if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","选号",false,"WT.si_x","条件筛选",false,"WT.select", $("#queryNumForm").serializeObject(),false);}  
            }catch(e){
            	
            }
        $("#queryNumForm").submit();
    });
    
    /**
     * 清除选项
     */
    $("#clearBtn").on("click",function(){
        var $li = $("ul[name='conditionUl'] li");
        $li.removeClass("list-bg");
        $li.find("input").removeAttr("name");
        // 重置默认选中长沙
        selectCity();
    });

    /**
     * 确认，选套餐
     */
    $("#choosePlanBtn").live("click",function(){
        var numTypeCode = $("input[name='numTypeCode']").val();
        if(numTypeCode == 2){
            showAlert("靓号暂不开放购买");
            return;
        }

        var $li = $("#numListUl li.active");
        var num = $li.find("input[name$='num']").val();
        var costNoRule = $li.find("input[name$='costNoRule']").val();
        var cityCode = $li.find("input[name$='cityCode']").val();
        var guaranteedFee = $li.find("input[name$='guaranteedFee']").val();
        guaranteedFee = parseInt(guaranteedFee) * 100;
        $("input[name='phoneNum']").val(num);
        $("input[name='preFee']").val(costNoRule);
        $("input[name='planFee']").val(guaranteedFee);
        $("input[name='cityCode']").val(cityCode);
        $("#choosePlanForm").submit();
    });
    
    // 查询号码
    $(".vk-icon").live("click", function(){
    	//关闭查询条件弹出层
        $(".drawer-overlay-upper").hide();
        $(".drawer-main").animate({right:'-100%'});
        $("body").removeClass('open');

        $("#queryNumForm").submit();
    });
    
    $("#numListUl > li").live("click", function(){
    	if ($(this).hasClass("active")) {
    		return;
    	} else {
    		$(this).addClass("active");
    		$(this).siblings().removeClass("active");
    	}
    });
});

/**
 * 默认地市选中长沙
 */
function selectCity() {
	var master = $("#masterdiv").find("div[class='vcon']").eq(0);
	master.find("ul[name='conditionUl'] li").each(function(){
		var name = $(this).find("a").text();
		if (name == '长沙') {
			$(this).addClass("list-bg");
	        $(this).siblings("li").removeClass("list-bg");

	        var valueName = $(this).find("a").attr("valueName");
	        var conditionIdName = $(this).find("a").attr("conditionIdName");
	        $(this).find("input[type='hidden']").eq(0).attr("name",valueName);
	        $(this).find("input[type='hidden']").eq(1).attr("name",conditionIdName);

	        $(this).siblings("li input").removeAttr("name");
		}
	});
}
/**
 * 获取ipv6地址
 */
function getIpv6Address() {

    $.ajax({

        url: ctx + "/GetIp/getIPv6",
        type: "post",
        data: {},
        dataType: "json",
        success: function (data) {
            if (data.message.indexOf(":") >= 0) {
                $(".icon-left").after("<img src='../static/demoimages/ipv6.png' style='position: absolute;left:3.35rem;top:5px;height: 35px;'/>");//
            }
        },
        error: function () {

        }

    });
}