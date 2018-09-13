<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<%@include file="/WEB-INF/views/include/taglib.jsp" %>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
		 <META name="WT.oss" content="${goodsInfo.keyWord}">
		<META name="WT.oss_r" content="${goodsPage.count}">  
		<META name="WT.oss_pt" content="${goodsPage.pageNo}">
	
	<link href="${ctxStatic}/css/list.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/filter.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/changetab.js"></script>
	<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/qm/insdc_w2.js"></script>
	<%@ include file="/WEB-INF/views/include/message.jsp"%>
<title>湖南移动商城</title>
</head>
<body class="drawer drawer-right">
<div class="top container">
  <div class="header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
	  <div class="top-search list-search trbar-sm">
		  <input type="text"  id="searchKeyInput" name="keyWord" value="${goodsInfo.keyWord}" class="form-control" placeholder="搜索商品"  otype="button" otitle="输入关键字" oarea="终端"/>
		  <a href="javascript:search();" class="icon-search" otype="button" otitle="搜索" oarea="终端"></a>
	  </div>
	</div>
</div>

<div class="container white-bg">
  <div class="tab-box">
    <ul class="bt-list bt-fr">
      <li class="active"><a href="#" >排序</a></li>
      <%--<li class="on"><!--点选有库存时，给li加上class="on",不要加active--><a href="#"><i></i>有库存</a></li>--%>
    </ul>
    <div <c:if test="${empty goodsInfo.queryListpageCode}">style="display: none" </c:if> class="sx drawer-toggle drawer-hamberger">
		<a href="#" >筛选</a>
		<span>
			<img  src="${ctxStatic}/images/shop-images/icon01.png" />
		</span>
	</div>
  </div>
  <div class="tab-list" id="tab-content">
    <ul class="tab-fr" style="display:block">
      <li id="liActive" class="active"><a href="javascript:search()" otype="button" otitle="排序-默认" oarea="终端">默认</a></li>
      <li><a href="#" id="saleNumSortI_a" otype="button" otitle="排序-销量" oarea="终端">销量<i id="saleNumSortI" class="sort-btn-up"></i></a></li>
      <li><a href="#" id="salePriceSortI_a" otype="button" otitle="排序-价格" oarea="终端">价格<i id="salePriceSortI" class="sort-btn-up"></i></a></li>
    </ul>
  </div>
  <div class="drawer-main drawer-default">
    <div class="drawer-main-title">筛选
    	<!-- <a href="#" class="drawer-main-title-qx">取消</a>
    	<a href="#" class="drawer-main-title-qd">确定</a> -->
    </div>
    <!-- 价格区间开始 -->
    <div class="money-list">
      <div class="mon-tit">价格区间</div>
      <div class="money-con">
       <c:forEach items="${goodsInfo.queryConditionList}" var="queryInfo" varStatus="status">
           <c:if test="${120 == queryInfo.conditionId}">
               <c:forEach items="${queryInfo.queryConditionValueList}" var="queryValueList">
                   <c:if test="${!empty queryValueList.conditionValue && !empty queryValueList.conditionValue}">
                       <c:set var="minPrice" value="${queryValueList.conditionValue}"/>
                       <c:set var="maxPrice" value="${queryValueList.conditionEndValue}"/>
                   </c:if>
               </c:forEach>
           </c:if>
        </c:forEach>
        <div class="money-zd">
            <input type="tel" placeholder="最低价" class="form-control" id="minPrice" alt="${minPrice}" otype="button" otitle="筛选-价格区间-最低价：${minPrice} " oarea="终端"/>
        </div>
        <div class="money-line">-</div>
        <div class="money-zd">
            <input type="tel" placeholder="最高价" class="form-control" id="maxPrice" alt="${maxPrice}"  otype="button" otitle="筛选-价格区间-最高价：${maxPrice} " oarea="终端"/>
        </div>
      </div>
    </div>
    <!-- 价格区间结束 -->
    <div id="masterdiv">
        <div id="scroller">
        <form id="searchGoodsForm" action="linkToGoodsList" method="post">
			<input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
        	<input name="categoryId" id="categoryId" type="hidden" value="${goodsInfo.categoryId}"/>
        	<input name="queryListpageCode" id="queryListpageCode" type="hidden" value="${goodsInfo.queryListpageCode}"/>
        	<set>
        	<c:set var="valueIndex" value="0"/>
		    <c:forEach items="${queryConditionList}" var="condition" varStatus="cStatus">
		     <c:set var="valueIndex" value="${valueIndex+1}"/>
		     <c:if test="${condition.categoryId == goodsInfo.categoryId}">
		      <div class="vtitle"><em class="v v02"></em>${condition.conditionName}</div>
		      <input name="queryConditionList[${cStatus.index}].conditionId" value="${condition.conditionId}" type="hidden"/>
		      <input name="queryConditionList[${cStatus.index}].conditionCode" value="${condition.conditionCode}" type="hidden"/>
		      <div class="vcon"  style="display: none;">
		        <ul class="vconlist clearfix">
				  <c:choose>
					  <c:when test="${condition.conditionSourceTable == 'TF_BRAND'}">
						  <c:forEach items="${brandList}" var="brand" varStatus="brandStatus">
							  <!-- 反选开始 -->
							  <c:set var="flag" value="0"/>
							  <c:forEach items="${goodsInfo.queryConditionList}" var="queryInfo" varStatus="status">
								  <c:if test="${condition.conditionId == queryInfo.conditionId}">
									  <c:forEach items="${queryInfo.queryConditionValueList}" var="queryValueList">
										  <c:if test="${queryValueList.conditionValue == brand.brandId}">
											  <c:set var="flag" value="1"/>
										  </c:if>
									  </c:forEach>
								  </c:if>
							  </c:forEach>
							  <!-- 反选结束 -->

							  <li
								  <c:choose>
									  <c:when test="${flag == 1}">class="conditionValue select"</c:when>
									  <c:otherwise>class="conditionValue"</c:otherwise>
								  </c:choose>
							  >
								  <a
									  valueName="queryConditionList[${cStatus.index}].queryConditionValueList[${brandStatus.index}].conditionValue"
									  conditionIdName="queryConditionList[${cStatus.index}].queryConditionValueList[${brandStatus.index}].conditionId"
									  <c:if test="${flag == 1}">class="list-bg"</c:if>
								   otype="button" otitle="筛选-手机品牌-${brand.brandName}" oarea="终端">${brand.brandName}</a>
								  <input class="value" type="hidden" value="${brand.brandId}"
										 <c:if test="${flag == 1}">name="queryConditionList[${cStatus.index}].queryConditionValueList[${brandStatus.index}].conditionValue"</c:if>/>
								  <input class="conditionId" type="hidden" value="${condition.conditionId}"
										 <c:if test="${flag == 1}">name="queryConditionList[${cStatus.index}].queryConditionValueList[${brandStatus.index}].conditionId"</c:if> />
							  </li>
						  </c:forEach>
					  </c:when>
					  <c:otherwise>
						  <c:forEach items="${queryConditionValueList}" var="conditionValue" varStatus="cvStatus">
							  <c:if test="${conditionValue.conditionId == condition.conditionId}">
								  <!-- 反选开始 -->
								  <c:set var="flag" value="0"/>
								  <c:forEach items="${goodsInfo.queryConditionList}" var="queryInfo" varStatus="status">
									  <c:if test="${condition.conditionId == queryInfo.conditionId}">
										  <c:forEach items="${queryInfo.queryConditionValueList}" var="queryValueList">
											  <c:if test="${queryValueList.conditionValue == conditionValue.conditionValue}">
												  <c:set var="flag" value="1"/>
											  </c:if>
										  </c:forEach>
									  </c:if>
								  </c:forEach>
								  <!-- 反选结束 -->

								  <li
										  <c:choose>
											  <c:when test="${flag == 1}">class="conditionValue select"</c:when>
											  <c:otherwise>class="conditionValue"</c:otherwise>
										  </c:choose>
								  >
									  <a
											  valueName="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionValue"
											  endValueName="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionEndValue"
											  conditionIdName="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionId"
											  <c:if test="${flag == 1}">class="list-bg"</c:if>
									  otype="button" otitle="筛选-${condition.conditionName}-${conditionValue.conditionName}" oarea="终端" >${conditionValue.conditionName}</a>
									  <input class="value" type="hidden" value="${conditionValue.conditionValue}"
											 <c:if test="${flag == 1}">name="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionValue"</c:if>/>
									  <input class="endValue" type="hidden" value="${conditionValue.conditionEndValue}"
											  <c:if test="${flag == 1}"> name="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionEndValue"</c:if> />
									  <input class="conditionId" type="hidden" value="${condition.conditionId}"
											 <c:if test="${flag == 1}">name="queryConditionList[${cStatus.index}].queryConditionValueList[${cvStatus.index}].conditionId"</c:if> />
								  </li>
							  </c:if>
						  </c:forEach>
					  </c:otherwise>
				  </c:choose>
		        </ul>
		      </div>
		     </c:if>
		    </c:forEach>
		    <div style="display:none" id="price">
		        <input name="queryConditionList[${valueIndex}].conditionId" value="120" type="hidden"/>
		        <input name="queryConditionList[${valueIndex}].conditionCode" value="COND_PRICE_CODE" type="hidden"/>
		        <input class="value" type="hidden" valueName="queryConditionList[${valueIndex}].queryConditionValueList[${valueIndex}].conditionValue"/>
		        <input class="endValue" type="hidden" endValueName="queryConditionList[${valueIndex}].queryConditionValueList[${valueIndex}].conditionEndValue"/>
		        <input class="conditionId" type="hidden" conditionIdName="queryConditionList[${valueIndex}].queryConditionValueList[${valueIndex}].conditionId" value="120"/>
		    </div>
        </form>   
      <br />
    </div>
    </div>
    <div class="drawer-main-btn">
    	<!-- <a href="#" id="clearItemsBtn" class="btn btn-bd btn-sm">清除选项</a> -->
    	<a href="javascript:;" class="btn btn-blue btn-sm" id="confirmBtn"  otype="button" otitle="筛选-确认" oarea="终端" >确定</a>
        <a href="javascript:;" class="btn btn-blue btn-sm" id="clearItemsBtn"  otype="button" otitle="筛选-重置" oarea="终端" >重置</a>
    </div>
  </div>
  <div class="drawer-overlay-upper"></div>
</div>
  <div id="container" class="container container-con" >
    <div id="content">
		<div id="scroller">
			<div id="pullDown" class="" style="display:block;">
				<div class="pullDownIcon"></div>
				<div class="pullDownLabel">下拉刷新</div>
			</div>
      <div id="goodsListDiv" class="tu-list">
      	<c:forEach items="${goodsPage.list}" var="goods" varStatus="gStatus">
	        <dl>
	          <dt>
	          	<a href="${gUrl}${goods.goodsUrl}">
	          		<c:forEach items="${goods.goodsStaticList}" var="goodsStatic" varStatus="gsStatus">
			      		<c:if test="${goodsStatic.goodsStaticDefault == 0}">
					      	<img src="${tfsUrl}${fns:resImageSize(goodsStatic.goodsStaticUrl,"80x100")}">
			      		</c:if>
			      	</c:forEach>
	          	</a>
	          </dt>
	          <dd>
	          	<a href="${gUrl}${goods.goodsUrl}" class="tu-con">${goods.goodsName}</a>
	          	<span class="price-bt"><strong><fmt:formatNumber value="${goods.minGoodsSalePrice/100}" type="currency"/></strong></span>
	            <div class="tu-fr"> 
	            	<span class="tu-cl">已售出${goods.goodsSaleNum}台</span> 
	            </div>
	          </dd>
	        </dl>
        </c:forEach>
      </div>
		<div id="pullUp" class="ub ub-pc c-gra load" style="display: block;">
			<div class="pullUpIcon"></div>
			<div class="pullUpLabel">加载中...</div>
		</div>
    </div>
		<div class="iScrollVerticalScrollbar iScrollLoneScrollbar"></div>
</div>
</div>

<!--底部菜单start-->

<!--底部菜单end-->
<input id="basePath" name="basePath" type="hidden" value="${ctxStatic}"/>
<input id="pageNo" name="pageNo" type="hidden" value="${goodsPage.pageNo}"/>
<input id="pageSize" name="pageSize" type="hidden" value="10"/>
<input id="lastPage" name="lastPage" type="hidden" value="${goodsPage.lastPage}"/>
<script type="text/javascript" src="${ctxStatic}/js/iscroll-probe.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/goodsListNew.js"></script>
</body>
</html>