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
<title>湖南移动商城-店铺内商品列表</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/main.css"  />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/media-style.css"  />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/css/man-center.css"  />
<script type="text/javascript" src="<%=path%>/static/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/static/js/artTemplate/dist/template.js"></script>
<style>
   .list-search {
    margin-right: 80px !important;
    }
</style>


<script type="text/javascript">
    var imageServerPath = '${tfsUrl}';
    var sortType = '${sortType}';
    var sortCloum = '${sortClounm}';
    var baseProject = "<%=path%>/"; 
   $(function() {
	   //loaded();
	   if(sortCloum==''||sortCloum==null||sortCloum=='shopid'){
		   $('#sortClounm').val('');
		   $('a.noSort').attr('class','noSort active');
	   }else{
		   $('#sortClounm').val(sortCloum);
		   $('#sortType').val(sortType);
		   if(sortType=='DESC'){
			   $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-down');
		   }else{
			   $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-up');
		   }
		   
	   }
	   $("#content").css("background-image","url('')");
	});
    function querySort(sortCloum,obj){
    	$('#goodsName').val($('#serachKey').val());
    	if(sortCloum=='noSort'){//默认排序
    		 $('#sortType').val('');
    		 $('#sortClounm').val('');
    	}else if(sortCloum=='goodsSalePriceOrderBy'){
    		var cc = $('a.'+sortCloum ).attr('class');
    		if(cc=="goodsSalePriceOrderBy"){
    			 $('#sortType').val('ASC');
    			 $('#sortClounm').val(sortCloum);
    			 $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-up');
    		}else if(cc=="goodsSalePriceOrderBy active sort-btn-down"){
    			$('#sortType').val('ASC');
   			    $('#sortClounm').val(sortCloum);
   			 $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-up');
    		}else if(cc=="goodsSalePriceOrderBy active sort-btn-up"){
    			$('#sortType').val('DESC');
   			    $('#sortClounm').val(sortCloum);
   			 $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-down');
    		}
    	}else if(sortCloum=='goodsSaleNumOrderBy'){
    		var cc = $('a.'+sortCloum).attr('class');
    		if(cc=="goodsSaleNumOrderBy"){
    			 $('#sortType').val('ASC');
    			 $('#sortClounm').val(sortCloum);
    			 $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-up');
    		}else if(cc=="goodsSaleNumOrderBy active sort-btn-down"){
    			$('#sortType').val('ASC');
   			    $('#sortClounm').val(sortCloum);
   			 $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-up');
    		}else if(cc=="goodsSaleNumOrderBy active sort-btn-up"){
    			$('#sortType').val('DESC');
   			    $('#sortClounm').val(sortCloum);
   			 $('a.'+sortCloum).attr('class',sortCloum+' active sort-btn-down');
    		}
    	}
    	$('#shopGoodsListForm').submit();
    	
    }
    function searchShopGoods(){
    	$('#sortType').val('');
		$('#sortClounm').val('');
		$('#goodsName').val($('#serachKey').val());
    	$('#shopGoodsListForm').submit();
    }
</script>
</head>
<body class="drawer drawer-right">
            <form id="shopGoodsListForm" action="shopGoodsList">
                <input id="sortType" name="sortType" type="hidden" />
                <input id="sortClounm" name="sortClounm" type="hidden" />
                <input id="goodsName"   name="goodsName" type="hidden"/>
                <input id="shopId" name="shopId" type="hidden" value="${shopId}"/>
            </form>
			<div class="top container">
			 <div class="header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			    <div class="top-search list-search">
				  <input type="text"  id="serachKey" value="${goodsName}" class="form-control" placeholder="搜索店铺内商品" />
				  <a href="javascript:searchShopGoods();" class="icon-search"></a>
				</div>
			    <a href="#" class="icon-right"></a>
			    <%@include file="/WEB-INF/views/include/headInfo.jsp" %>
			    <!--  <ul class="nav-slide">
				    <li class="nav-arrow"></li>
				    <li><a   href="网厅-index.html"><i class="icon-nav-wt-home"></i>网上营业厅</a></li>
				    <li><a href="首页.html"><i class="icon-nav-home"></i>购机首页</a></li>
				    <li><a href="号卡-首页.html"><i class="icon-nav-lh"></i>靓号</a></li>
				    <li><a href="分类搜索.html"><i class="icon-nav-ss"></i>搜索</a></li>
				    <li><a href="个人中心.html"><i class="icon-nav-wo"></i>个人中心</a></li>
			    </ul>  
			    <div class="modal-bg"></div>-->
			  </div>
			</div>
			<div class="container white-bg">
			  <div class="tab-list" id="tab-content" style="display:block">
			  <ul class="xl-list xl-con">
			    <li>
			       <a href="javascript:querySort('goodsSaleNumOrderBy',this);" class="goodsSaleNumOrderBy">销量<i></i>
			       </a>
			    </li>
			    <li><a href="javascript:querySort('goodsSalePriceOrderBy',this);" class="goodsSalePriceOrderBy">价格<i></i></a></li>
			  </ul>
			  </div>
			 </div>
			  <!-- floor1 Begin -->
			  <div class="container container-con" >
			  <div id="content" >
			   <div id="scroller">
				     <div id="pullDown" class="" style="display:block;">
				       <div class="pullDownIcon"></div>
				       <div class="pullDownLabel">下拉刷新</div>
				     </div>
				    <div class="container floor goodsDiv" style="display:none">
					 <div class="dp-con">
					   <!--店铺位置 start-->
					   <div class="dp-dt">
					     <a href="">
					     <div class="dp-pic"><img src="" /></div>
					     <div class="dp-text">
					      <h4 class="goodsPrice"></h4>
					      <span class="goodsName"></span>
					      <div class="gz-number">
					         <span>销量：<b class="saleNum"></b></span></span>
					      </div>
					     </div>
					      <span class="dp-icon"></span>
					     <!--<span class="dp-wz">&nbsp;</span> -->
					   </a>
					   </div>
					   <!--店铺位置 end-->
					  </div>
					</div>
					 
					 <div class="tu-list" > 
					 <c:if test="${empty page.list}">
					        <div class="text-center" >暂无数据</div>

					 </c:if>
					
					<c:forEach items="${page.list}" var="goods" varStatus="status">
					
					
					<div class="container floor goodsDiv">
					 <div class="dp-con">
					   <!--店铺位置 start-->
					   <div class="dp-dt">
					     <a href="<%=path%>/goods/${goods.goodsUrl}">
					     <div class="dp-pic"><img src="${tfsUrl}${goods.goodsStaticUrl}" /></div>
					     <div class="dp-text">
					      <h4 class="goodsPrice"><fmt:formatNumber value="${goods.minGoodsSalePrice/100}" type="currency" pattern="￥#0.00"/></h4>
					      <span class="goodsName">${goods.goodsName}</span>
					      <div class="gz-number">
					         <span>销量：<b class="saleNum">${goods.goodsSaleNum}</b></span></span>
					      </div>
					     </div>
					      <span class="dp-icon"></span>
					     <!--<span class="dp-wz">&nbsp;</span> -->
					   </a>
					   </div>
					   <!--店铺位置 end-->
					  </div>
					</div>
					<!-- floor1 End -->
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
			    
			</div>
			<%@include file="/WEB-INF/views/include/navBottom.jsp" %>
<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
<input id="lastPage" name="lastPage" type="hidden" value="${page.lastPage}"/>
<script type="text/javascript" src="<%=basePath%>/static/js/iscroll-probe.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/shop/shopGoodsList.js"></script>
</body>

</html>