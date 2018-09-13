<!DOCTYPE html>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="com.ai.ecs.ecm.mall.wap.modules.member.vo.FavoriteShopResult" %>
<%@page import="java.util.List" %>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/favoriteShopList.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
List<FavoriteShopResult> favList=(List<FavoriteShopResult>)request.getAttribute("favoriteShopList");
%>
<body>
<div class="top container">
  <div class="header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>收藏的店铺</h1>
    <sys:headInfo whiteTop="1"/>
  </div>
</div>
<div class="dp-list container">
<%
if(favList!=null){
    for(FavoriteShopResult shop:favList){
%>
  <dl>
    <dt><a href="${gUrl}<%=shop.getMemberFollowBusiUrl()%>"><img src="${tfsUrl}<%=shop.getMemberFollowBusiImgUrl()%>"></a></dt>
    <dd> <a href="${gUrl}<%=shop.getMemberFollowBusiUrl()%>" class="tu-con"><%=shop.getMemberFollowBusiName()%></a> <span class="dp-num">
      <h3><%=shop.getGoodsCount()%></h3>
      商品</span> </dd>
    <i class="icon-default02 deleteBtn" data-index=<%=shop.getMemberFollowId() %> data-toggle="modal" data-target="#myModal"></i> <span class="sx-btn">
    </span>
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
