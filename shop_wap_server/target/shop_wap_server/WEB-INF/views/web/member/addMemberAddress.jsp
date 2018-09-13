<!DOCTYPE html>
<%@page import="java.security.SecureRandom"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.member.entity.ThirdLevelAddress" %>
<%@ page import="java.util.List" %>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.ArrayUtils"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>

<link href="<%=request.getContextPath()%>/static/css/member/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/addMemberAddress.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
  List<ThirdLevelAddress> addrList=(List<ThirdLevelAddress>)request.getAttribute("addrParent");
%>
<%
SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
long seq=random.nextLong();
String randomstr=""+seq;
session.setAttribute("random_session",randomstr);
%>
<body>
<div class="top container">
  <div class="header sub-title"> 
    <!--<a class="icon-left icon-home icon2"></a>--><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>新增地址</h1>
  <sys:headInfo/>
  </div>
</div>
<div class="container white-bg box10">
  <form class="form-horizontal registration add-address" role="form" id="addrForm" method="post" action="<%=request.getContextPath()%>/memberAddress/addMemberAddress">
      <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
      <div class="form-group">
      <label for="inputEmail3" class="control-label">收货人姓名:</label>
      <div id="memberRecipientNameDiv" class="form-input-div "><!-- 输入错误时加class input-fr 将输入框变成红色 -->
      	<input type="hidden" name="random_form" value=<%=randomstr%>></input>
        <input type="text" class="form-control" name="memberRecipientName"  id="memberRecipientName"  otype="button" otitle="收货人姓名" oarea="终端"/>
      </div>
      <span class="font-default" id="recName"></span> </div>
    <div class="form-group">
      <label for="inputEmail3" class="control-label">联系电话:</label>
      <div class="form-input-div" id="memberRecipientPhoneDiv">
        <input type="tel" class="form-control" name="memberRecipientPhone"  id="memberRecipientPhone"  otype="button" otitle="联系电话" oarea="终端"/>
      </div>
      <span class="font-default" id="memberRecipientPhoneSpan"></span> </div>

    <div class="form-group">
      <label for="inputEmail3" class="control-label">收货地址:</label>
      <div class="form-input-div address-select" id="regionDiv">
  <select id="memberRecipientProvince" name="memberRecipientProvince"
      data-category_level="0"  otype="button" otitle="收货地址-省" oarea="终端">
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
  data-category_level="0"  otype="button" otitle="收货地址-市" oarea="终端">
  <option value="">请选择</option>
</select>
<select id="memberRecipientCounty" name="memberRecipientCounty"
data-category_level="0"  otype="button" otitle="收货地址-县/区" oarea="终端">
<option value="">请选择</option>
</select>
      </div>
      <span class="font-default" id="regionSpan"></span> </div>
    <div class="form-group">
      <label for="inputEmail3" class="control-label">详细地址：</label>
      <div class="form-input-div" id="memberRecipientAddressDiv">
        <input type="text" class="form-control" placeholder="请输入详细地址" name="memberRecipientAddress" id="memberRecipientAddress"  otype="button" otitle="详细地址" oarea="终端"/>
      </div>
      <span class="font-default" id="memberRecipientAddressSpan"></span> </div>
    <div class="form-group btn-box"> <a id="submitBtn" class="btn btn-blue" href="#"  otype="button" otitle="提交" oarea="终端">提　交</a> </div>
  </form>
</div>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
<script type="text/javascript">
var changeUrl="<%=request.getContextPath()%>/memberAddress/getChildrenByPid";
var submitUrl="<%=request.getContextPath()%>/memberAddress/addMemberAddress";
var gotoUrl="<%=request.getContextPath()%>/memberAddress/toMemberAddressList";
</script>
</body>
</html>
