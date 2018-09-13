<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<%--插码相关--%>
	<meta name="WT.si_n" content="KD_MBH" />
	<meta name="WT.si_x" content="20" />
	<Meta name="WT.mobile" content="">
	<Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<%--插码相关--%>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>
	<script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
	<div class="top container">
		<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
	    	<h1>魔百和</h1>
		</div>
	</div>
	
	<div class="container">
  		<a href="javascript:void(0)"><img src="${ctxStatic}/images/goods/broadband/mbhBanner.png" /></a>
   		<div class="wf-list">
	      	<!--选择套餐 start-->
	      	<div class="change-tc mb-change mb-list"> 
				<h2 class="renew-title">魔百和套餐</h2>

					<ul class="add_oil select-renew clearfix">
						<c:forEach items="${mbhItemList}" var="mbhItem" varStatus="status" >
				  			<li>${mbhItem.broadbandItemName}<s></s></li>
				  		</c:forEach>
					</ul>
			</div>
			<!--选择套餐 end--> 
					
			<!--高清魔百和 stat-->
	      	<div class="change-tc mb-change mb-list"> 
				<h2 class="renew-title">高清魔百和</h2>
					<ul class="gq-list clearfix">
							<li>
						   <img src="${ctxStatic}/images/goods/broadband/mbhTv.jpg" />
						   <p><span class="font-gray3">芒果TV：</span>湖南卫视优质点播资源，芒果自制节目及其他热门电影，电视剧，综艺等</p>
						 </li>
							<li>
								 <img src="${ctxStatic}/images/goods/broadband/mbhFur.jpg" />
								 <p><span class="font-gray3">未来电视：</span>央视优质点播资源，独有电视看点及超长回看，更有腾讯专区等特色板块</p>
							</li>
					</ul>
			</div>
			<!--高清魔百和 end--> 
   		</div>
	</div>
	
	<div class="fix-br">
	  	<div class="affix foot-box new-foot">
		    <div class="container active-fix">
	      		<div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#"></a></div>
	      		<div class="fl-right"><a href="javascript:onSubmit()">立即办理</a></div>
	      		<!--当用户不能点击时在a class加dg-gray--> 
		    </div>
	  	</div>
	</div>
	
	<!--温馨提示弹框 start-->
	<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog new-modal">
	    <div class="modal-dow">
	      <h4>温馨提示</h4>	
	      <div class="modal-text">
	       	<p>1、魔百和年包使用到期后恢复为标准资费10元/月，直接从用户手机账户收取。用户也可选择退订电视服务，或者续订礼包产品。</p>
	        <p>2、续订魔百和电视将在次月生效。</p>
	        <p>3、对于新装、续费裸宽用户可办理魔百和电视业务，魔百和电视费用将于手机合并捆绑后付费。</p>
	        <p>4、若前期已办理魔百和电视且正处于按月收费状态，则办理魔百和年包后原魔百和计费规则取消，次月魔百和年包生效且同时变更为魔百和年包计费规则。</p>
	      </div>
	      <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
	    </div>
	 	</div>
	</div>
	<!--温馨提示弹框 end-->
	<form id="form1" method="post" action="detail">
		<input type="hidden" id="phoneId" name="phoneId" value="${phoneId}" />
	</form>
	<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
		  $(".mb").click(function(){
		  $(".mb-change").toggle();
		  });
		});
		
		function onSubmit(){
			/*插码*/
			try{
				var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
				if(window.Webtrends){
					Webtrends.multiTrack({
						argsa: ["WT.si_n",sin,
							"WT.si_x","21"],
						delayTime: 100
					})
				}else{
					if(typeof(dcsPageTrack)=="function"){
						dcsPageTrack ("WT.si_n",sin,true,
								"WT.si_x","21",true);
					}
				}
			}catch (e){
				console.log(e);
			}
			$("#form1").submit();
		}
	</script>
</body>
</html>