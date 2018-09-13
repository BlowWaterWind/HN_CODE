<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/include/taglib.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城-店铺列表</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/main.css"  />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/media-style.css"  />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/man-center.css"  />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/list.css"  />
<script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/nav.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/artTemplate/dist/template.js"></script>
<script type="text/javascript">
var path = '<%=path%>';
var baseProject = "<%=path%>/";
/**
 * 收藏店铺
 */
function saveMemberFlow(shopId,shopUrl,shopName,shopLogo){
	$.ajax({
		type : "POST",
		url : path + "/shop/saveMemberFlow",
		data : {
			shopId : shopId,
			shopUrl : shopUrl,
			shopName:shopName,
			shopLogo:shopLogo
		},
		dataType : "json",
		success : function(data) {
				if(data.flag=='noLogin'){
					window.location.href="<%=basePath%>/login/toLogin";
				}else{
					alert(data.info);
				}
			
		}
	});
}


</script>
</head>
<body>
<div class="top container">
  <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>店铺描述</h1>
    <a href="#" class="icon-right"></a> 
    <%@include file="/release/front/navRight.jsp" %>
    <!-- <ul class="nav-slide">
      <li class="nav-arrow"></li>
				    <li><a   href="网厅-index.html"><i class="icon-nav-wt-home"></i>网上营业厅</a></li>
				    <li><a href="首页.html"><i class="icon-nav-home"></i>购机首页</a></li>
				    <li><a href="号卡-首页.html"><i class="icon-nav-lh"></i>靓号</a></li>
				    <li><a href="分类搜索.html"><i class="icon-nav-ss"></i>搜索</a></li>
				    <li><a href="个人中心.html"><i class="icon-nav-wo"></i>个人中心</a></li>
    </ul> -->
    <div class="modal-bg"></div>
    
  </div>
</div>
<div class="container"> 
  <!--头部导航 start-->
  <div class="center-top dp-sy">
    <div class="mask"></div>
    <img src="<%=basePath%>/static/images/shop-images/mt.png" class="pl-img" />
    <div class="usr-img"><span class="tu-st"><img src="${imageServerPath}${shop.shopLogo}"></span>
      <a href="店铺描述.html" class="usr-wcon">
        <span class="usr-name">${shop.shopName}</span>
        <span class="usr-adress">${shop.shopPhysicallProvince}${shop.shopPhysicallCity}${shop.shopPhysicallCounty}${shop.shopPhysicallAddress}</span>
        <div class="gz-rs"><span class="usr-sc">${shop.collectionNum}人关注</span></div>
        
      </a>
     <div class="usr-btn"><a href="javascript:saveMemberFlow('${shop.shopId}','${shop.shopHomepageUrl}','${shop.shopName}','${shop.shopLogo}');" class="btn-sc btn-brt"><i class="heart-icon"></i><strong>收藏</strong></a></div>
    </div>
  </div>
  <!--头部导航 end-->
  <!-- 店铺描述 start -->
  <div class="mes-row">
    <p><span class="mes-th">好评率：</span>
       <c:choose>
           <c:when test="${goodRate=='暂无评分'}">
                ${goodRate}
           </c:when>
           <c:otherwise>
                ${goodRate}%
           </c:otherwise>
       </c:choose>
    </p>
    <p><span class="mes-th">所在地：</span>${shop.shopPhysicallProvince}${shop.shopPhysicallCity}</p>
    <p><span class="mes-th">开店时间：</span><fmt:formatDate value="${shop.shopTime}" pattern="yyyy年MM月dd日 "/></p>
  </div>
  
  <div class="mes-row shop-con">
     <h4>商家简介</h4>
     <div class="shop-info">
       ${shop.shopShortDesc} 
     </div>
  </div>
  <!--商家简介 end-->
  <!--商家信息 start-->
  <div class="mes-row shop-con">
    <h4>商家信息</h4>
    <div class="sj-info sj-address">
       <div class="sj-list"><span class="power-address">商家地址：${shop.shopPhysicallProvince}${shop.shopPhysicallCity}${shop.shopPhysicallCounty}${shop.shopPhysicallAddress}</span></div>
       <div class="sj-list"><span>营业时间：${shop.shopPhysicallOpenTime}-${shop.shopPhysicallCloseTime}</span></div>
       <div class="sj-list sj-con"><a href="#">店铺名称：${shop.shopName}</a></div>
       <div class="sj-list sj-con"><a href="tel:${shop.servicePhone}">服务电话：${shop.servicePhone}<i class="icon-nav-dh"></i></a></div>
    </div>
  </div>
  <!--商家信息 end-->
  <!-- 店铺二维码 start -->
  <div class="mes-row text-center">
    <div class="erm"><img src="${tfsUrl}${shop.shopQrcodeImgUrl}" alt=" " /></div>
    <p>店铺二维码手机扫一扫</p>
  </div>
  <!-- 店铺二维码 end -->
</div>
<%@include file="/release/front/navBottom.jsp" %>
<a href="#top" target="_self" id="top"></a>
</body>
</html>
