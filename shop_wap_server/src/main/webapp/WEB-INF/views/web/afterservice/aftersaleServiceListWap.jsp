<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="aftersaleTemplateWap.jsp"%>

<%
    String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城-售后信息</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/main.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/media-style.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/static/css/man-center.css" />

<script type="text/javascript" src="<%=basePath%>/static/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/nav.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
</head>
<body class="drawer drawer-right">
	<!-- 顶部元素 -->
	<div class="top container">
		<div class="header sub-title">
			<a class="icon-left icon-home icon2"></a>
			<a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
			<h1>售后服务中心</h1>
			<a href="#" class="icon-right icon-lr02"></a>
			<form>
				<input class="tip-search-input tip-con tip-white" placeholder="搜索订单号" type="text" value="" name="search" id="search">
			</form>
		</div>
		<ul class="nav-slide">
			<li class="nav-arrow"></li>
			<li><a href="${pageContext.request.contextPath}">
					<i class="icon-nav-home"></i>首页
				</a>
			</li>
			<li><a href="${pageContext.request.contextPath}/memberInfo/toMemberCenter">
					<i class="icon-nav-wo"></i>个人中心
				</a>
			</li>
		</ul>
		<div class="modal-bg"></div>
	</div>
	</div>
	<div id="outer" class="container">
		<ul id="tab" class="tabList list-con affix container">
			<li id="retMoney" class="current"><span class="text" onclick="changeTab('retMoney')">我的退款</span></li>
			<li id="retGood"><span class="text" onclick="changeTab('retGood')">我的退货</span></li>
			<li id="changeGood"><span class="text" onclick="changeTab('changeGood')">我的换货</span></li>
			<li id="aftersaleService"><span class="text" onclick="changeTab('aftersaleService')">其它服务</span></li>
		</ul>
	</div>
	<div class="container white-bg">
		<div class="tab-list" id="tab-content" style="display: block">
			<ul class="xl-list xl-con">
				<li><a href="javascript:querySort('noSort',this);" class="noSort">默认</a></li>
				<li><a href="javascript:querySort('collectionNum',this);" class="collectionNum">
						人气<i></i>
					</a></li>
			</ul>
		</div>
	</div>
	<!-- 列表数据start -->
	<div class="container container-con">
		<div id="content">
			<div id="scroller">
				<!-- 上提示框 -->
				<div id="pullDown" class="" style="display: block;">
					<div class="pullDownIcon"></div>
					<div class="pullDownLabel"></div>
				</div>
				<!-- 数据域 -->
				<div class="tabCon tabcon-list">
					<div class="cur-sd">
						<ul class="cur-ul aftersalelist"></ul>
					</div>
				</div>
				<!-- 下提示框 -->
				<div id="pullUp">
					<span class="pullUpLabel"></span>
				</div>
			</div>
			<!-- 滚动条 -->
			<div class="iScrollVerticalScrollbar iScrollLoneScrollbar"></div>
		</div>
	</div>

	<input id="pageNo" name="pageNo" type="hidden" value="" />
	<input id="pageSize" name="pageSize" type="hidden" value="" />
	<input id="lastPage" name="lastPage" type="hidden" value="" />
	<input id="tfsUrl" name="tfsUrl" type="hidden" value="${tfsUrl}"/>
	<input id="gUrl" name="gUrl" type="hidden" value="${gUrl}"/>

	<!-- 暂无数据提示框 -->
	<div class="container container-con" id="noDataReminder" style="display: none">
		<div class="empty-box02" style="margin-top: 45px;">
			<p>尊敬的用户，您暂时没有该项售后服务的数据信息。</p>
		</div>
	</div>

	<%@ include file="/release/front/navBottom.jsp"%>
 	<script type="text/javascript" src="<%=basePath%>/static/js/iscroll-probe.js"></script>
	<script type="text/javascript" src="<%=basePath%>/static/js/aftersale/aftersaleListWap.js"></script>
</body>
</html>