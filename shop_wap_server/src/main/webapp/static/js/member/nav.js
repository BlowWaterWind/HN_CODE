// JavaScript Document
$(document).ready(function(){
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
