
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- swiper Begin -->
<div class="swiper-container swiper-banner container" id="swiper-index01">
  <div class="swiper-wrapper" id="swiper-wrapper">
    <div class="swiper-slide"><a href="${ctx}/index.html"><img  src="http://15.15.20.221:7500/v1/tfs//T1XRETB4_T1RXrhCrK.jpg" /></a></div>
    <div class="swiper-slide"><a href="${ctx}/index.html"><img  src="http://15.15.20.221:7500/v1/tfs//T1XyETB4_T1RXrhCrK.jpg" /></a></div>
  </div>
  <div class="pagination"></div>
</div>
<script>//轮播JS
  var mySwiper = new Swiper('#swiper-index01',{
    pagination: '#swiper-index01 .pagination',
	loop:true,
	grabCursor: true,
	autoplay:3000,
	autoplayDisableOnInteraction:false,
	
  })
</script>
<!-- swiper End -->

