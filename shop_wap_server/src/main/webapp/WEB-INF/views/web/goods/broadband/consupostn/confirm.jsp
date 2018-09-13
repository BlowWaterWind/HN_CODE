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
   
<%--<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>确定套餐</h1>
  </div>
</div>--%>
    <c:set value="确定套餐" var="titleName" ></c:set>
    <%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<!--套餐选择 start-->
<div class="container">
  <!--已登录并且开通宽带 start-->
  <ul class="new-login hide">
 	<li>
 	  <div class="login-list">宽带账号：<span></span></div>
 	  <div class="login-list">宽带地址：<span></span></div>		
 	</li>
  </ul>
  <!--已登录 end-->
  <!--地市选择 start-->
  <ul class="change-list">
      <li>
          <label>地&emsp;&emsp;区：</label>
          <div class="right-td"> 
          	<span class="td-fr">
	          	<i class="select-arrow"></i>
	   		    <select class="set-sd" id="memberRecipientCity" name="memberRecipientCity">
	      		<c:forEach items="${cityList}" var="city" varStatus="status">
	      			<option value="${city.eparchyCode}"  orgid="${city.orgId}">${city.orgName}</option>
	      		</c:forEach>
	  			</select>
    		</span> 
      		<span class="td-fr"> <i class="select-arrow"></i>
		      	<select class="set-sd"  id="memberRecipientCounty" name="memberRecipientCounty">
			     	<c:forEach items="${countyList}" var="county" varStatus="status">
			     		<option value="${county.orgId}"  >${county.orgName}</option>
			     	</c:forEach>
		      	</select>
      		</span> 
   	  	  </div>
      </li>
      
      <li style="border:none;">
      	  <label>详细地址：</label>
	      <div class="zit-box">
	      	<input type="hidden"  id="houseCode" name="houseCode"  />
	      	<input type="text" id="address" name="address" class="form-control flip" value="详细街道、小区、门牌号" />
	      </div>
      </li>
  </ul>
  <!--地市选择 end-->
  <!--填写信息 start-->
  <div class="wf-list sub-info sub-new">
      <div class="wf-ait clearfix">
      
        <ul class="wf-con">
          <c:choose>
          <c:when test="${isBroadBand == '1'}">
          	<li>
           	  <span class="font-gray">手&nbsp;&nbsp;&nbsp;&nbsp;机：</span>
           	  <div class="sub-text">${phoneId}</div>
          	</li>
		  	<li>
              <span class="font-gray">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
              <div class="sub-text"><input type="text" class="form-control" placeholder="请与手机号码机主姓名保持一致" /></div>
            </li>
            <li>
              <span class="font-gray">身份证：</span>
              <div class="sub-text"><input type="text" class="form-control" placeholder="请与手机号码机主证件保持一致" /></div>
            </li>
          </c:when>
          <c:otherwise>
            <li>
           	  	<span class="font-gray">手&nbsp;&nbsp;&nbsp;&nbsp;机：</span>
           	  	<div class="sub-text">${phoneId}</div>
          	</li>
		  	<li>
              <span class="font-gray">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
              <div class="sub-text">${installPhoneNum}</div>
            </li>
            <li>
              <span class="font-gray">身份证：</span>
              <div class="sub-text">${psptId}</div>
            </li>
          </c:otherwise>
          </c:choose>
        </ul>
      </div>
      <!--选择品牌 start-->
      <div class="wf-ait clearfix">
        <div class="wf-tit wf-cit font-gray">互联网电视品牌：</div>
        <ul class="wf-con mt10">
         <li><label><input type="radio" name="radio" id='mg' name="form1_Mbh" checked="checked" value="0"/><b>芒果TV</b><small class="font-gray">湖南卫视所有电视栏目高清视频点播</small></label></li>
         <li><label><input type="radio" name="radio" id='wl' name="form1_Mbh" value="1"/><b>未来TV</b><small class="font-gray">中国网络电视台强大的媒体资源库</small></label></li>
        </ul>
      </div>
      <!--选择品牌 end-->
   </div>
   <!--填写信息 end-->
</div>
<div class="fix-br">
	<div class="affix foot-box">
	<div class="container active-fix">
		<div class="fl-left">
			<p class="p1">应付金额：<span class="cl-rose">${price}元</span></p>
			<p class="p1">保底消费：<span class="cl-rose">${minCost}元</span></p>
		</div>
		<div class="fl-right"><a class="javascript:void(0);">立即办理</a></div>
		<!--当用户不能点击时在a class加dg-gray-->
	</div>
	</div>
</div>
</form>
</body>
</html>