$(function () {

    /**
     * 选择月消费金额
     */
    $("#planFeeUL li").on("click",function () {
        $(this).find("a").addClass("list-bg");
        $(this).siblings("li a").removeClass("list-bg");

        var planFeeBegin = $(this).find("input[name='planFeeBegin']").val();
        var planFeeEnd = $(this).find("input[name='planFeeEnd']").val();

        $("#planForm").find("input[name='planFeeBegin']").val(planFeeBegin);
        $("#planForm").find("input[name='planFeeEnd']").val(planFeeBegin);
    });

    /**
     * 选择月通话时长
     */
    $("#callTimeUL li").on("click",function () {
        $(this).find("a").addClass("list-bg");
        $(this).siblings("li a").removeClass("list-bg");

        var callTimeBegin = $(this).find("input[name='callTimeBegin']").val();
        var callTimeEnd = $(this).find("input[name='callTimeEnd']").val();

        $("#planForm").find("input[name='callTimeBegin']").val(callTimeBegin);
        $("#planForm").find("input[name='callTimeEnd']").val(callTimeEnd);
    });

    /**
     * 选择月上网流量
     */
    $("#internetFlowUL li").on("click",function () {
        $(this).find("a").addClass("list-bg");
        $(this).siblings("li a").removeClass("list-bg");

        var internetFlowBegin = $(this).find("input[name='internetFlowBegin']").val();
        var internetFlowEnd = $(this).find("input[name='internetFlowEnd']").val();

        $("#planForm").find("input[name='internetFlowBegin']").val(internetFlowBegin);
        $("#planForm").find("input[name='internetFlowEnd']").val(internetFlowEnd);
    });

    /**
     * 确定
     */
    $("#confirmBtn").on("click",function () {
        //关闭查询条件弹出层
        $(".drawer-overlay-upper").hide();
        $(".drawer-main").animate({right:'-100%'});
        $("body").removeClass('open');
        
        $("#planForm").submit();
    });

    /**
     * 清除选项
     */
    $("#clearBtn").on("click",function () {
        $("#masterdiv a[name='any']").addClass("list-bg");
        $("#masterdiv a[name!='any']").removeClass("list-bg");

    });

    /**
     * 选择套餐
     */
    $("#planListUl li").on("click",function () {
        $(this).find("form[name='planForm']").submit();
    });

});