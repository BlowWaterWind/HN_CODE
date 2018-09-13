<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/filter.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/static/js/goods/smsList.js"></script>
<title>湖南移动商城</title>
</head>
<body class="drawer drawer-right">
<div class="top container">
  <div class="header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>短信</h1>
    <a href="#" class="icon-right"></a><a href="购物车.html" class="icon-top-cart"></a>
    <ul class="nav-slide">
      <li class="nav-arrow"></li>
      <li><a href="首页.html"><i class="icon-nav-home"></i>首页</a></li>
      <li><a href="所有商品.html"><i class="icon-nav-ss"></i>所有商品</a></li>
      <li><a href="个人中心.html"><i class="icon-nav-wo"></i>个人中心</a></li>
    </ul>
    <div class="modal-bg"></div>
  </div>
</div>
<div class="container">
</div>
<div class="container white-bg container-con">
  <div class="tab-box">
    <ul class="bt-list bt-fr">
      <li class="active"><a href="#">排序</a></li>
      <li class="on"><a href="#"><i></i>有库存</a></li>
    </ul>
    <div class="sx drawer-toggle drawer-hamberger"><a href="#" >筛选</a><span><img  src="${ctxStatic}/static/css/goods/images/shop-images/icon01.png" /></span></div>
  </div>
  <div class="tab-list" id="tab-content">
    <ul class="tab-fr" style="display:block">
      <li class="active"><a href="#">默认</a></li>
      <li><a href="#">销量<i id="saleNumSortI" class="sort-btn-up"></i></a></li>
      <li><a href="#">价格<i id="salePriceSortI" class="sort-btn-up"></i></a></li>
    </ul>
  </div>
  <div class="drawer-main drawer-default">
    <div class="drawer-main-title">
    	<a href="#" class="drawer-main-title-qx">取消</a>筛选
    	<a href="#" class="drawer-main-title-qd">确定</a>
    </div>
    <div id="masterdiv">
        <form id="searchGoodsForm" action="goodsQuery/linkToGoodsList" method="post">
        	<input id="keyWord" name="keyWord" type="hidden" value="${goodsInfo.keyWord}"/>
		    <c:forEach items="${conditionList}" var="condition" varStatus="cStatus">
		    <!-- 类目暂时固定为手机 -->
		     <c:if test="${condition.categoryId == 72000000 && condition.channelCode == 'E006'}">
		      <div class="vtitle"><em class="v v02"></em>${condition.conditionName}</div>
		      <input name="queryConditionList[${cStatus.index}].conditionId" value="${condition.conditionId}" type="hidden"/>
		      <input name="queryConditionList[${cStatus.index}].conditionCode" value="${condition.conditionCode}" type="hidden"/>
		      <div class="vcon"  style="display: none;">
		        <ul class="vconlist clearfix">
		          <c:forEach items="${conditionValueList}" var="conditionValue" varStatus="cvStatus">
		              <c:if test="${conditionValue.conditionId == condition.conditionId}">
				          <li class="conditionValue">
				          	<a
				          	 valueName="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionValue"
				          	 endValueName="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionEndValue"
				          	 conditionIdName="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionId"
				          	>${conditionValue.conditionName}</a>
				          	<input class="value" type="hidden" value="${conditionValue.conditionValue}"/>
				          	<input class="endValue" type="hidden" value="${conditionValue.conditionEndValue}"/>
				          	<input class="conditionId" type="hidden" value="${condition.conditionId}" />
				          </li>
		              </c:if>
		          </c:forEach>
		        </ul>
		      </div>
		     </c:if>
		    </c:forEach>
        </form>   
      <br />
    </div>
    <div class="drawer-main-btn"><a href="#" class="btn btn-bd btn-sm">清除选项</a></div>
  </div>
  <div class="drawer-overlay-upper"></div>
  
  <div id="wrapper" style="top:76px;">
    <div id="scroller">
      <div id="pullDown"> <span class="pullDownLabel">下拉刷新...</span> </div>
      <div id="goodsListDiv" class="tu-list">
      	<c:forEach items="${goodsPage.list}" var="goods" varStatus="gStatus">
	        <dl>
	          <dt>
	          	<a href="单品页.html">
	          		<c:forEach items="${goods.goodsStaticList}" var="goodsStatic" varStatus="gsStatus">
			      		<c:if test="${goodsStatic.goodsStaticDefault == 0}">
					      	<img src="${goodsStatic.goodsStaticUrl}">
			      		</c:if>
			      	</c:forEach>
	          	</a>
	          </dt>
	          <dd>
	          	<a href="单品页.html" class="tu-con">${goods.goodsName}</a>
	          	<span class="price-bt"><strong>￥${goods.goodsSalePrice}</strong></span>
	            <div class="tu-fr"> 
	            	<span class="tu-cl">已售出${goods.goodsSaleNum}台</span> 
	            	<span class="dp-wz">800M</span>
	            </div>
	          </dd>
	        </dl>
        </c:forEach>
      </div>
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
</body>
</html>