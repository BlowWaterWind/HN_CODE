<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/fittingList.js"></script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>配件</h1>
  </div>
</div>

<div class="container container-con white-bg">
  <div class="hyj-pic"> 
  	<span><img src="demoimages/hyj01.png" /></span>
  </div>
  <div id="wrapper" style="top:150px;">
  <div id="scroller">
  <div id="pullDown"> 
  	<span class="pullDownLabel">下拉刷新...</span> 
  </div>
  <ul class="tu-list">
    <c:forEach items="${goodsPage.list}" var="goods" varStatus="gStatus">
	    <li>
	      <div class="pic">
	      	<c:forEach items="${goods.goodsStaticList}" var="goodsStatic" varStatus="gsStatus">
	      		<c:if test="${goodsStatic.goodsStaticDefault == 0}">
			      	<a href="单品页.html"><img src="${goodsStatic.goodsStaticUrl}"></a>
	      		</c:if>
	      	</c:forEach>
	      </div>
	      <p>${goods.goodsName}</p>
	      <span>¥${goods.goodsSalePrice}</span> 
	      <strong>已售${goods.goodsSaleNum}</strong> 
	    </li>
	 </c:forEach>
  </ul>
  <div id="pullUp"> <span class="pullUpLabel">上拉加载更多...</span> </div>
  </div>
  </div>
</div>

<!--底部菜单start-->

<!--底部菜单end-->

<input id="basePath" name="basePath" type="hidden" value="${ctxStatic}"/>
<input id="pageNo" name="pageNo" type="hidden" value="${goodsPage.pageNo}"/>
<input id="pageSize" name="pageSize" type="hidden" value="${goodsPage.pageSize}"/>
<input id="lastPage" name="lastPage" type="hidden" value="${goodsPage.lastPage}"/>
<input id="categoryId" name="categoryId" type="hidden" value="${categoryId}"/>
</body>
</html>