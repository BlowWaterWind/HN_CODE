<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<meta name="WT.si_n" content="在线选号"/>
<meta name="WT.si_x" content="选号码"/>
<%@ include file="/WEB-INF/views/include/message.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"  />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/num-card.css"  />
<script type="text/javascript" src="${ctxStatic}/js/iscroll-probe.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/qm/insdc_w2.js"></script>
</head>
<body class="drawer drawer-right">
<div class="top container">
  <div class="header sub-title">
    <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>选号码</h1>
  </div>
</div>
<div class="container pd-t45 white-bg">
  <div class="tab-box">
    <ul id="numTypeUl" class="bt-list bk-list">
      <li
      <c:if test="${goodsInfo.numTypeCode == 1}">
          class="on"
      </c:if>
       >
        <a href="javascript:;" numTypeCode="1" queryListpageId="130" queryListpageCode="NUM_LISTPAGE_B2C"  otype="button" otitle="普号" oarea="终端"> <i></i>普号</a>
      </li>
      <li
      <c:if test="${goodsInfo.numTypeCode == 2}">
        class="on"
      </c:if>
      >
        <a href="javascript:;" numTypeCode="2" queryListpageId="150" queryListpageCode="NUM_LISTPAGE_B2C"  otype="button" otitle="靓号" oarea="终端"><i></i>靓号</a>
      </li>
    </ul>
    <div class="sx drawer-toggle drawer-hamberger">
      <a href="javascript:;"  otype="button" otitle="筛选" oarea="终端">筛选</a>
      <span><img src="${ctxStatic}/images/shop-images/icon01.png" /></span>
    </div>
  </div>
  <div class="drawer-main drawer-default">
    <div class="drawer-main-title">筛选</div>
 <form id="queryNumForm" action="${ctx}/goodsQuery/linkToNumList" method="post">
     <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
   <input id="pageNo" name="page.pageNo" type="hidden" value="${netNumList.pageNo}"/>
   <input id="pageSize" name="page.pageSize" type="hidden" value="${netNumList.pageSize}"/>
   <input type="hidden" name="numTypeCode" value="${goodsInfo.numTypeCode}"/><%--号码类型--%>
   <input type="hidden" name="queryListpageId" value="${goodsInfo.queryListpageId}"/>
   <input type="hidden" name="queryListpageCode" value="${goodsInfo.queryListpageCode}"/>
   <div id="masterdiv">
     <div id="scroller">
     <c:set var="valueIndex" value="0"/>
     <c:forEach items="${conditionList}" var="condition" varStatus="cStatus">
       <c:if test="${condition.queryListpageId == goodsInfo.queryListpageId}">
         <div class="vtitle"><em class="v"></em>${condition.conditionName}</div>
         <div class="vcon">
           <ul name="conditionUl" class="vconlist clearfix">
             <input type="hidden" name="queryConditionList[${cStatus.index}].conditionCode" value="${condition.conditionCode}"/>
             <input type="hidden" name="queryConditionList[${cStatus.index}].conditionId" value="${condition.conditionId}"/>
             <c:forEach items="${conditionValueList}" var="conditionValue" varStatus="cvStatus">
               <c:if test="${condition.conditionId == conditionValue.conditionId}">
                
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
                
                 <li <c:if test="${flag == 1}">class="list-bg"</c:if> >
                   <a href="javascript:;"
                      valueName="queryConditionList[${cStatus.index}].queryConditionValueList[${valueIndex}].conditionValue"
                      conditionIdName="queryConditionList[${cStatus.index}].queryConditionValueList[${valueIndex}].conditionId"
                    otype="button" otitle="筛选-${condition.conditionName}-${conditionValue.conditionName}" oarea="终端">${conditionValue.conditionName}</a>
                   <input type="hidden" value="${conditionValue.conditionValue}" <c:if test="${flag == 1}">name="queryConditionList[${cStatus.index}].queryConditionValueList[${valueIndex}].conditionValue"</c:if>/>
                   <input type="hidden" value="${condition.conditionId}" <c:if test="${flag == 1}">name="queryConditionList[${cStatus.index}].queryConditionValueList[${valueIndex}].conditionId"</c:if>/>
                 </li>
                 <c:set var="valueIndex" value="${valueIndex+1}"/>
               </c:if>
             </c:forEach>
           </ul>
           
         </div>
       </c:if>
     </c:forEach>
     <div class="vtitle"><em class="v"></em>尾号搜索</div>
     <div class="vcon" style="display: none;">
       <ul class="vconlist clearfix">
         <li class="vk-search">
           <input type="text" class="form-control" name="numbers[0]" placeholder="尾号查询" onfocus="if (this.value==this.defaultValue) this.value='';" onblur="if (this.value=='') this.value=this.defaultValue;"otype="button" otitle="尾号查询" oarea="终端"/>
           <a href="javascript:;" class="vk-icon" otype="button" otitle="尾号查询" oarea="终端"></a>
         </li>
       </ul>
     </div>
     </div>
   </div>
 </form>

    <div class="drawer-main-btn">
      <a href="javascript:;" class="btn btn-blue btn-sm" id="confirmBtn" otype="button" otitle="筛选-确定" oarea="终端">确定</a>
      <a href="javascript:;" class="btn btn-blue btn-sm" id="clearBtn" otype="button" otitle="筛选-重置" oarea="终端">重置</a>
    </div>
  </div>
  <div class="drawer-overlay-upper"></div>

</div>


<div class="container container-con">
  <div id="content">
    <div id="scroller">
      <div id="pullDown" class="" style="display:block;">
        <div class="pullDownIcon"></div>
        <div class="pullDownLabel">下拉刷新</div>
      </div>
      <div class="tab-er tab-fbs white-bg">
          <ul id="numListUl" class="tu-list sp-button02">
			      <c:if test="${empty netNumList.list}">
			        <span>${simPrompt}</span>
			      </c:if>

			      <c:forEach items="${netNumList.list}" var="netNum" varStatus="nStatus">
			        <li <c:if test="${nStatus.index == 0}">class="active"</c:if>>
			          <h4>
			              ${fn:substring(netNum.num,0,3)}
			              ${fn:substring(netNum.num,3,7)}
			            <b>${fn:substring(netNum.num,7,11)}</b>
			          </h4>
			          <div class="hk-tit">
                          <c:set var="costNoRule" value="0"/>
                          <c:choose>
                              <c:when test="${empty netNum.costNoRule || netNum.costNoRule == '0' || netNum.costNoRule == 0}">
                                  <c:set var="costNoRule" value="${simOrderPayNum}"/>
                              </c:when>
                              <c:otherwise>
                                  <c:set var="costNoRule" value="${netNum.costNoRule}"/>
                              </c:otherwise>
                          </c:choose>
			              <span>预存话费：<strong><fmt:formatNumber value="${costNoRule}" type="currency"/></strong></span>
			              <span>最低消费：<strong><fmt:formatNumber value="${netNum.guaranteedFee}" type="currency"/></strong></span>
			              <span>地市：<strong>${fns:getDictLabel(netNum.cityCode, 'CITY_CODE_CHECKBOXES', '长沙')}</strong></span>
			          </div>
			          <input type="hidden" name="netNum[${nStatus.index}].num" value="${netNum.num}_${goodsInfo.numTypeCode}"/>
			          <input type="hidden" name="netNum[${nStatus.index}].costNoRule" value="${costNoRule}"/>
			          <input type="hidden" name="netNum[${nStatus.index}].guaranteedFee" value="${netNum.guaranteedFee}"/>
			          <input type="hidden" name="netNum[${nStatus.index}].cityCode" value="${netNum.cityCode}"/>
			        </li>
			      </c:forEach>
            </ul>
      </div>
      <div id="pullUp" class="ub ub-pc c-gra load" style="display: block;">
        <div class="pullUpIcon"></div>
        <div class="pullUpLabel">加载中...</div>
      </div>
    </div>
    <div class="iScrollVerticalScrollbar iScrollLoneScrollbar"></div>
  </div>
</div>



<form id="choosePlanForm" action="${ctx}goodsQuery/linkToPlanList" method="post">
    <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
  <input type="hidden" name="phoneNum"/>
  <input type="hidden" name="preFee"/>
  <input type="hidden" name="planFee"/>
  <input type="hidden" name="cityCode"/>
</form>
<div class="fix-br">
  <div class="affix foot-box bulid-fix">
    <div class="container sdd-btn">
      <a id="choosePlanBtn" href="javascript:;" class="btn btn-blue" otype="button" otitle="确认">确认</a>
    </div>
  </div>
</div>
<input id="lastPage" name="page.lastPage" type="hidden" value="${netNumList.lastPage}"/>
<script type="text/javascript" src="${ctxStatic}/js/filter.js" ></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js" ></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/changetab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/numList.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/numScroll.js"></script>
</body>
</html>