<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.member.entity.MemberAddress" %>
<%@ page import="java.util.List" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no">
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>

<link href="<%=request.getContextPath()%>/static/css/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberAddressList.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
List<MemberAddress> addressList=(List<MemberAddress>)request.getAttribute("addressList");
%>
<body>
<div class="top container">
  <div class="header sub-title"> <a href="<%=request.getContextPath()%>/memberAddress/toAddMemberAddress" class=" list-bt">新增</a> 
    <!--<a class="icon-left icon-home icon2"></a>--><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>地址管理</h1>
  </div>
  <div class="more-box"></div>
</div>
<div id="outer" class="container">
  <ul class="s-l s-gl">
  <%
      if(addressList!=null){
          for(MemberAddress address:addressList){
              StringBuilder addr=new StringBuilder();
              String addString="";
              if(null!=address.getMemberRecipientAddress() && !"".equals(address.getMemberRecipientAddress())){
                  int lenth=address.getMemberRecipientAddress().length();
                  addString=address.getMemberRecipientAddress().replace(address.getMemberRecipientAddress().substring(1, lenth-1), "****");
              }
              addr.append(address.getMemberRecipientProvince())
              .append(address.getMemberRecipientCity())
              .append(address.getMemberRecipientCounty())
              .append(addString);
              String name="";
              if(null!=address.getMemberRecipientName() && !"".equals(address.getMemberRecipientName())){
                  name=address.getMemberRecipientName().replace(address.getMemberRecipientName().substring(1), "****");
              }
  %>
    <li class="active">
      <div class="name">
        <div class="pull-left font-primary"><%=name%></div>
        <div class="pull-right"><%=address.getMemberRecipientPhone()%></div>
      </div>
      <div class="address"><%=addr.toString()%></div>
      <div class="management">
          <% if (("Y").equals(address.getMemberIsDefault())){%>
              <a class="pull-left font-primary" data-index="<%=address.getMemberAddressId()%>">
                  <i class="icon-mrdz pull-left"></i>默认地址
              </a>
          <%} else {%>
              <a class="pull-left font-muted" data-index="<%=address.getMemberAddressId()%>">
                  <i class="icon-mrdz pull-left"></i>设为默认
              </a>
          <%}%>
      <a class="pull-right tb-delete"  data-index="<%=address.getMemberAddressId()%>">
          <i class="icon-delete"></i>删除</a>
      </a>
      <a class="pull-right tb-edit"  data-index="<%=address.getMemberAddressId()%>">
          <i class="icon-edit"></i>编辑</a>
      </a>
      </div>
    </li>
  <%
          }
        }
  %>  
    
  </ul>
</div>

<!--二次确认弹框 start-->
<div class="share-bit">
  <div class="share-text"> <span>确认删除此地址?</span> <a class="cancel cancel-con" id="delBtn">确认</a> </div>
  <div class="share-gb"><a class="cancel cancel-con">取消</a></div>
</div>
<div class="more-box"></div>
<!--二次确认弹框 end-->
<script type="text/javascript">
var updateUrl="<%=request.getContextPath()%>/memberAddress/toUpdateMemberAddress";
var delUrl="<%=request.getContextPath()%>/memberAddress/deleteMemberAddress";
var setDefUrl="<%=request.getContextPath()%>/memberAddress/setDefAddr";
</script>
</body>
</html>
