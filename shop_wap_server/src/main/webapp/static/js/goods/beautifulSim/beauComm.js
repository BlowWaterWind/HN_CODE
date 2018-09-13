
$(function () {
    //筛选
    $('.city').on('click', function () {
        $('.city-con').fadeToggle();
        $('.grade-con').hide();
        $('.consum-con').hide();
        $(".consum").removeClass("active");
        $(".grade").removeClass("active");
        $(".city").toggleClass("active");
    });
    $('.select01 li').on('click', function () {
        var $li = $(this); //选中当前DOM元素
        $('.city').text($li.attr('data-value'));
        $li.addClass('active').siblings().removeClass('active');
        $('.city-con').hide();
        $(".city").addClass("up");
        $(".city").removeClass("active");
        $("input[name='cityCode']").val($li.attr('value'));
        searchBeautifulNum();
    });
    $('.grade').on('click', function () {
        $('.grade-con').fadeToggle();
        $('.city-con').hide();
        $('.consum-con').hide();
        $(".city").removeClass("active");
        $(".consum").removeClass("active");
        $(".grade").toggleClass("active");
    });
    $('.consum').on('click', function () {
        $('.consum-con').fadeToggle();
        $('.city-con').hide();
        $('.grade-con').hide();
        $(".city").removeClass("active");
        $(".grade").removeClass("active");
        $(".consum").toggleClass("active");
    });
    //切换:预存档次的百分之几的切换
    $(".change li").on("click", function () {
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
        $("input[name='section']").val($(this).attr('value'));
        searchBeautifulNum();
    });
    //弹框
    $(".cancel").click(function () {
        $(".zffs-fixed").slideUp('fast');
        $(".mask-box").removeClass("on");
    });

    $($('.select01 li')[1]).click()
});

//点击页面的mask,将已经弹出来的框框隐藏，将mask隐藏
$(".mask-box").click(function(){
    $(".zffs-fixed").slideUp('fast');
    $(".mask-box").toggle();
});