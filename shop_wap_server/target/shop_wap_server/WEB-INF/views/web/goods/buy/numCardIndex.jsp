<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>

<link href="<%=basePath%>static/css/swiper.min.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>static/css/main.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>static/css/media-style.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>static/css/list.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>static/css/num-card.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/static/js/jquery/jquery-1.9.1.min.js"></script> 
<title>号卡首页</title>
</head>
<body>
<!-- loading start -->
<!-- loading start -->
<div id="load" class="loading">
  <div id="reload">数据加载中，请稍后... ...</div>
  <span class="text">中国移动（湖南）</span>
 </div>
<script>
var baseProject = "<%=basePath%>";
//loading
window.onload=function(){
  $("#load").fadeOut('2200');
}
</script>
<!-- loading end -->
<!-- top Begin -->
<div class="top container">
  <div class="header">
  <%@include file="/release/front/head.jsp" %>
   <%@include file="/release/front/search.jsp" %>
  </div>
</div>
<!-- top End --> 
<!-- swiper Begin -->
 <%@include file="/release/front/nav.jsp" %>
<script type="text/javascript" src="<%=basePath%>/static/js/nav.js"></script> 
<script type="text/javascript" src="<%=basePath%>/static/js/rem.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/swiper.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/artTemplate/dist/template.js"></script>
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
<!-- floor1 Begin -->
<script id="numCardTmpl" type="text/html">
{{each NET_NUM_LIST as val i}}
    <li> <a  href="号卡-选择产品2.html">
      <p class=" font-green">{{val.num1}}{{val.num2}}<b>{{val.num3}}</b></p>
      <p class="font-gray">所属地:</p>
      <p class="font-gray2"></p>
      </a> </li>
{{/each}}
</script>
  <script>
    $(function(){
        var data = {"PARA_VALUE5" :"1" ,"CODE_TYPE_CODE" :"2" ,
                           "PARA_VALUE4":"12" ,"X_CHOICE_TAG" :"3" ,
                           "CITY_CODE":"0731",
                           "SERVICE_NAME":"DQ_HQ_HNAN_NetNumQuery"
                   };
              
    })
    </script>
<div class="container floor floor2"> <a href="号卡-选择号码.html" class="title"> <span class="text">靓号选购</span> <span class="more">更多</span> </a>
  <ul class="tu-list hk-ft" id="numCardList">
   <c:forEach items="${planList}" var="item">  
	   <li> <a  href="号卡-选择产品2.html">
	      <p class=" font-green">${item.num}</p>
	      <p class="font-gray2">预存:<b>${item.guaranteedFee/100}元</b></p>
	      </a> </li>
     </c:forEach>
    <li> <a  href="号卡-选择产品2.html">
      <p class=" font-green">1528964<b>0445</b></p>
      <p class="font-gray">所属地:长沙市</p>
      <p class="font-gray2">预存:<b>50元</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最低:<b>0元</b>/月</p>
      </a> </li>
    <li> <a href="号卡-选择产品2.html">
      <p class=" font-green">1528964<b>0445</b></p>
      <p class="font-gray">所属地:长沙市</p>
      <p class="font-gray2">预存:<b>50元</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最低:<b>0元</b>/月</p>
      </a> </li>
    <li> <a href="号卡-选择产品2.html">
      <p class=" font-green">1528964<b>0445</b></p>
      <p class="font-gray">所属地:长沙市</p>
      <p class="font-gray2">预存:<b>50元</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最低:<b>0元</b>/月</p>
      </a> </li>
    <li> <a href="号卡-选择产品2.html">
      <p class=" font-green">1528964<b>0445</b></p>
      <p class="font-gray">所属地:长沙市</p>
      <p class="font-gray2">预存:<b>50元</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最低:<b>0元</b>/月</p>
      </a> </li>
  </ul>
</div>
<!-- floor1 End --> 
<!-- floor2 Begin -->
<div class="container floor floor5"> <a href="#" class="title"> <span class="text">4G套餐</span> <span class="more more-red">更多</span> </a>
  <ul class="tu-list">
  <c:forEach items="${planList}" var="item">  
      <li>
	      <div class="pic"><a href="单品页 - 套餐.html"><img  src="<%=basePath%>/static/${item.planImgUrl}"></a></div>
	      <p><a href="单品页 - 套餐.html">${item.planName}</a></p>
      </li>
  </c:forEach>
   <!--  <li>
      <div class="pic"><a href="单品页 - 套餐.html"><img  src="demoimages/hk-product1.png"></a></div>
      <p><a href="单品页 - 套餐.html">流量升级8元档</a></p>
    </li>
    <li>
      <div class="pic"><a href="单品页 - 套餐.html"><img  src="demoimages/hk-product2.png"></a></div>
      <p><a href="单品页 - 套餐.html">4G飞享套餐58元档</a></p>
    </li>
    <li>
      <div class="pic"><a href="号卡-选择号码.html"><img  src="demoimages/hk-product3.png"></a></div>
      <p><a href="号卡-选择号码.html">长话套餐13元档</a></p>
    </li>
    <li>
      <div class="pic"><a href="号卡-选择号码.html"><img  src="demoimages/hk-product4.png"></a></div>
      <p><a href="号卡-选择号码.html">本地套餐9元档</a></p>
    </li> -->
  </ul>
</div>
<!-- floor2 End --> 
<!-- floor3 Begin -->
<!--<div class="container floor floor6"> <a href="#" class="title"> <span class="text">4G换卡</span> </a>
  <div class="content"> <a href="#"><img  src="demoimages/hk-4gzq.png" /></a> </div>
</div>-->
<!-- floor3 End --> 
<!-- floor4 Begin -->
<div class="container floor floor3"> <a href="#" class="title"> <span class="text">活动专区</span> </a>
  <div class="content"> <a href="#" class="left-floor"><img src="demoimages/gj_main04.png" /></a> <a href="#" class="right-floor"><img src="demoimages/gj_main06.png" /></a> </div>
</div>
<!-- floor4 End --> 
<!-- floor5 Begin -->
 <%@include file="/release/front/footer.jsp" %>
<!-- floor5 End --> 
<!--底部菜单start-->
  <%@include file="/release/front/navBottom.jsp" %>
<!--底部菜单end-->

</body>
</html>