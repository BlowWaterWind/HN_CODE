// JavaScript Document
$(document).ready(function(){
  getIpv6Address();//ip地址判断
  $(".icon-right").click(function(){
    $(".top .nav-slide").slideToggle(20);
    $(".center-top .nav-slide").slideToggle(20);
    $(".modal-bg").toggle();
  });
  $(".modal-bg").click(function(){
    $(".top .nav-slide").slideUp(20);
    $(".center-top .nav-slide").slideUp(20);
    $(".modal-bg").hide();
	
  });
  $(".nav-arrow").click(function(){
    $(".top .nav-slide").slideUp(20);
    $(".center-top .nav-slide").slideUp(20);
    $(".modal-bg").hide();
  });
  
});
/**
 * 获取ipv6地址
 */
function getIpv6Address(){
try {
    $.ajax({
        url:  "/shop/GetIp/getIPv6",
        type: "post",
        data: {},
        dataType: "json",
        success: function (data) {
            if (data.message.indexOf(".") >= 0) {
                $(".clearfix").after("<p style='text-align: center;margin-top: 0.4rem;color: #666;font-size: 14px;'>您的IPv4地址为：" + data.message + "</p>");
            } else {
                if (data.message.indexOf(":") >= 0) {
                    $(".header a img").after("<img src='/shop/static/images/shop-images/wap_ipv6.png' style='float: left; margin-top: 9px; margin-left: 7.8px;height: 30px;'>");
                    $(".top-sp .sp-tit").css("left", "1.1rem");
                    $(".form-control").css("width", "90%");
                    $(".clearfix").after("<div><p  style='text-align: center;margin-top: 0.4rem;color: #666;font-size: 14px;'>您的IPv6地址为：" + data.message + "</p></div>");
                }
            }
        },
        error: function () {

        }

    });
  }catch (e) {
      console.log(e);
  }
}
