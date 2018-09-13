// JavaScript Document
$(document).ready(function(){
  $(".drawer-toggle.drawer-hamberger").click(function(){
    $(".drawer-overlay-upper").show();
    $(".drawer-main").animate({right:'0px'});
    $("body").addClass('open');
    loadedCondition();
  });
  $(".drawer-overlay-upper").click(function(){
    $(".drawer-overlay-upper").hide();
    $(".drawer-main").animate({right:'-100%'});
    $("body").removeClass('open');
    loaded();
  });
  $(".drawer-main-title-qx").click(function(){
    $(".drawer-overlay-upper").hide();
    $(".drawer-main").animate({right:'-100%'});
    $("body").removeClass('open');
    loaded();
  });
  $(".xzcp-default .btn.cancel").click(function(){
    $(".drawer-overlay-upper").hide();
    $(".drawer-main").animate({right:'-100%'});
    $("body").removeClass('open');
  });
});
$(function(){
	//菜单隐藏展开
	var tabs_i=0
	$('.vtitle').click(function(){
		
		var _self = $(this);
		var j = $('.vtitle').index(_self);
		if ($('.vcon').eq(j).is(":visible")) {
			$('.vcon').eq(j).slideUp();
			$('em',_self).removeClass('v02').addClass('v01');
		} else {
			
			$('.vtitle em').each(function(e){
				if(e == j){
					$('em',_self).removeClass('v01').addClass('v02');
				}else{
					$(this).removeClass('v02').addClass('v01');
				}
			});
			$('.vcon').slideUp().eq(j).slideDown();
		}
	});
})