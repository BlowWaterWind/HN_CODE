<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
     <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadbandInstall.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
     <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>消费叠加型套餐选择</h1>
    </div>
</div>--%>
<c:set value="消费叠加型套餐选择" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<div class="container bg-gray ">
    <form action="confirmInstall" id="form1" name="form1" method="post">
    <input type="hidden" id="form1_skuId" name="form1_skuId"  />
    <input type="hidden" id="form1_houseCode" name="form1_houseCode"  />
    <input type="hidden" id="form1_addressName" name="form1_addressName"  />
    <input type="hidden" id="form1_packageId" name="form1_packageId"  />
    <input type="hidden" id="form1_productId" name="form1_productId"  />
    <input type="hidden" id="form1_discntCode" name="form1_discntCode"  />
    <input type="hidden" id="form1_hasMbh" name="form1_hasMbh"   />
    <input type="hidden" id="form1_hasBroadband" name="form1_hasBroadband"  />
    <input type="hidden" id="form1_maxWidth" name="form1_maxWidth"  />
    <input type="hidden" id="form1_freePort" name="form1_freePort"  />
    <input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode"  />
    <input type="hidden" id="form1_coverType" name="form1_coverType"  />
	<input type="hidden" id="form1_chooseBandWidth" name="form1_chooseBandWidth"  />

    
    <div class="wf-list tab-con">
        <!--选择套餐 start-->
        <div class="change-tc">
            <h2 class="renew-title">请选择新装套餐</h2>
            <ul class="add_oil select-renew clearfix" >
            	<c:forEach items="${broadbandItemList}" var="broadbandItem">
	            	<li name="li_broadbandItem">
	            		<input type="hidden" id="skuId" name="skuId" value="${broadbandItem.goodsSkuId}"/>
	            		<input type="hidden" id="goodsId" name="goodsId" value="${broadbandItem.goodsId}"/>
	            		<input type="hidden" id="chooseBandWidth" name="chooseBandWidth" value="${broadbandItem.bandWidth}"/>
	            		<input type="hidden" id="price" name="price" value="${broadbandItem.price}"/>
	            		${broadbandItem.bandWidth}M
	                    <p  class="font-gray">${broadbandItem.price}元/${broadbandItem.term}</p>
	                    <s></s>
	                    <c:if test="${not empty broadbandItem.labelName}">
	                    	<span class="tip tip-rose">${broadbandItem.labelName}</span>
	                    </c:if>
	                </li>
            	</c:forEach>
            </ul>
        </div>
        <!--选择套餐 end-->
    </div>
    </form>
</div>
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div id="div_settlement" class="container active-fix kd-disabled">
            <div class="fl-left">
                <p class="p1">应付总额：<b class="cl-rose" id="b_total">￥<span id="span_total">0.00</span></b></p>
            </div>
            <div class="fl-right"><a href="javascript:;" id="a_confirmInstall">立即订购</a></div>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
<!--搜索地址 start-->
<div id="div_searchAddress" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header site-full sub-title"><a class="icon-left site-close" id="a_retSearch" ></a>
            <div class="top-search list-search fit-ipnut">
                <input type="text" id="queryAddress" name="queryAddress" class="form-control form-fr" placeholder="请输入地址"/>
                <a href="javascript:;" class="icon-search"  id="a_search" ></a></div>
        </div>
    </div>
    <!--没资源 start-->
    <div class="mzy-con hide" id="div_message"><!--没资源时去掉hide-->
        <img src="${ctxStatic}/images/error.png" />
        <div class="mzy-text">
            <p>尊敬的用户，非常抱歉！</p>
            <p>您所在区域宽带资源还在建设中，</p>
            <p>有宽带资源后，我们会第一时间通过短信告知您！</p>
        </div>
    </div>
    <!--没资源 end-->
    <!--展示地址信息 start-->
    <ul class="container adress-list">
    </ul>
    <!--展示地址信息 end-->
    <div class="fix-br container fix-top fix-fb">
        <div class="affix container foot-menu">
            <div class="container form-group tj-btn"> <a class="btn btn-blue" href="##" id="queryCommit">确定</a> <a id="queryCancel" class="btn btn-gray site-close" href="##">取消</a> </div>
            <!--预约安装 start-->
            <!--预约安装 start-->
        </div>
    </div>
</div>
<!--搜索地址 end-->
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
</body>
</html>