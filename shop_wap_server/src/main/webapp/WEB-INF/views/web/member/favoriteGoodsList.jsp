<!DOCTYPE html>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.List" %>
<%@page import="com.ai.ecs.ecm.mall.wap.modules.member.vo.FavoriteGoodsResult" %>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/man-center.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/favoriteGoodsList.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%  
List<FavoriteGoodsResult> favoriteList=(List<FavoriteGoodsResult>)request.getAttribute("favoriteList");

%>
<body>
<div class="top container">
  <div class="container header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>收藏的商品</h1>
    <sys:headInfo whiteTop="1"/>
  </div>
</div>
<div class="tu-list ct-list container">
<%
if(favoriteList!=null){
    for(FavoriteGoodsResult goods:favoriteList){
%>
  <dl>
    <dt><a href="${gUrl}<%=goods.getMemberFollowBusiUrl()%>"><img src="${tfsUrl}<%=goods.getMemberFollowBusiImgUrl()%>"></a></dt>
    <dd> <a href="${gUrl}<%=goods.getMemberFollowBusiUrl()%>" class="tu-con"><%=goods.getMemberFollowBusiName()%></a> <span>￥<%=(double) (Math.round(goods.getPrice()*10000/100)/10000.0)%></span> </dd>
    <i class="icon-default02 deleteBtn" data-index=<%=goods.getMemberFollowId() %>></i>
  </dl>
  <%
    }
  }
%> 
</div>
<script>
var delBtnUrl="<%=request.getContextPath()%>/memberFavorite/cancelFav";
</script>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
</body>
</html>
