<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.member.entity.ThirdLevelAddress" %>
<%@ page import="com.ai.ecs.member.entity.MemberAddress" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils"%>
<%@ page import="org.apache.commons.lang3.StringUtils"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.ArrayUtils"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/member/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/select_default_value.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/updateMemberAddress.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
  List<ThirdLevelAddress> addrList=(List<ThirdLevelAddress>)request.getAttribute("addrParent");
List<ThirdLevelAddress> cityList=(List<ThirdLevelAddress>)request.getAttribute("citys");
List<ThirdLevelAddress> countyList=(List<ThirdLevelAddress>)request.getAttribute("countys");
   MemberAddress addr=(MemberAddress)request.getAttribute("memberAddress");
%>
<body>
<div class="top container">
  <div class="header sub-title"> 
    <!--<a class="icon-left icon-homeaddrForm icon2"></a>--><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>修改地址</h1>
  </div>
</div>
<div class="container white-bg box10">
  <form class="form-horizontal registration add-address" role="form" id="addrForm" method="post">
      <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
    <div class="form-group">
      <label for="inputEmail3" class="control-label">收货人姓名:</label>
      <div id="memberRecipientNameDiv" class="form-input-div "><!-- 输入错误时加class input-fr 将输入框变成红色 -->
        <input type="hidden" name="memberAddressId" value="${memberAddress.memberAddressId}" />
        <input type="hidden" name="memberIsDefault" value="${memberAddress.memberIsDefault}" />
          <input type="text" class="form-control"  id="memberRecipientName" value="${memberAddress.memberRecipientName}"  otype="button" otitle="收货人姓名" oarea="终端"/>
          <input type="hidden" class="form-control" name="memberRecipientName"  id="memberRecipientNameHidden" value="${memberAddress.memberRecipientName}"  otype="button" otitle="收货人姓名" oarea="终端"/>
      </div>
      <span class="font-default" id="recName"></span> </div>
    <div class="form-group">
      <label for="inputEmail3" class="control-label">联系电话:</label>
      <div class="form-input-div" id="memberRecipientPhoneDiv">
          <input type="text" class="form-control"  id="memberRecipientPhone" value="${memberAddress.memberRecipientPhone}"  otype="button" otitle="收货人手机" oarea="终端"/>
          <input type="hidden" class="form-control" name="memberRecipientPhone"  id="memberRecipientPhoneHidden" value="${memberAddress.memberRecipientPhone}"  otype="button" otitle="收货人手机" oarea="终端"/>
      </div>
      <span class="font-default" id="memberRecipientPhoneSpan"></span> </div>

    <div class="form-group">
      <label for="inputEmail3" class="control-label">收货地址:</label>
      <div class="form-input-div address-select" id="regionDiv">
  <select id="memberRecipientProvince" name="memberRecipientProvince" 
          data-default_value="${memberAddress.memberRecipientProvince}"  otype="button" otitle="收货地址-省" oarea="终端">
      <option value="">请选择</option>
      <%
          if (addrList!=null) {
              ThirdLevelAddress cate0 = null;
              for (int i = 0, count = addrList.size(); i < count; ++i) {
                  cate0 = addrList.get(i);
      %>
      <option value="<%=cate0.getOrgId()%>"><%=HtmlUtils.defaultString(cate0.getOrgName())%></option>
      <%
          }
          }
      %>
  </select>
  <select id="memberRecipientCity" name="memberRecipientCity"
          data-default_value="${memberAddress.memberRecipientCity}"  otype="button" otitle="收货地址-市" oarea="终端">
  <option value="">请选择</option>
  <%
  if (cityList!=null) {
      ThirdLevelAddress cate0 = null;
      for (int i = 0, count = cityList.size(); i < count; ++i) {
          cate0 = cityList.get(i);
  %>
    <option value="<%=cate0.getOrgId()%>"><%=HtmlUtils.defaultString(cate0.getOrgName())%></option>
<%
      }
  }
%>
</select>
<select id="memberRecipientCounty" name="memberRecipientCounty"
data-default_value="${memberAddress.memberRecipientCounty}"  otype="button" otitle="收货地址-县" oarea="终端">
<option value="">请选择</option>
<%
if (countyList!=null) {
    ThirdLevelAddress cate0 = null;
    for (int i = 0, count = countyList.size(); i < count; ++i) {
        cate0 = countyList.get(i);
%>
    <option value="<%=cate0.getOrgId()%>"><%=HtmlUtils.defaultString(cate0.getOrgName())%></option>
    <%
        }
    }
    %>
</select>
      </div>
      <span class="font-default" id="regionSpan"></span> </div>
    <div class="form-group">
      <label for="inputEmail3" class="control-label">详细地址：</label>
      <div class="form-input-div" id="memberRecipientAddressDiv">
          <input type="text" class="form-control" placeholder="请输入详细地址" id="memberRecipientAddress" value="${memberAddress.memberRecipientAddress}"  otype="button" otitle="收货地址-详细地址" oarea="终端"/>

          <input type="hidden" class="form-control" placeholder="请输入详细地址" name="memberRecipientAddress" id="memberRecipientAddressHidden" value="${memberAddress.memberRecipientAddress}"  otype="button" otitle="收货地址-详细地址" oarea="终端"/>
      </div>
      <span class="font-default" id="memberRecipientAddressSpan"></span> </div>
    <div class="form-group btn-box"> <a id="submitBtn" class="btn btn-blue" href="#"  otype="button" otitle="提交" oarea="终端">提　交</a> </div>
  </form>
</div>
<script type="text/javascript">
var changeUrl="<%=request.getContextPath()%>/memberAddress/getChildrenByPid";
var submitUrl="<%=request.getContextPath()%>/memberAddress/updateMemberAddress";
var gotoUrl="<%=request.getContextPath()%>/memberAddress/toMemberAddressList";
</script>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
</body>
</html>
