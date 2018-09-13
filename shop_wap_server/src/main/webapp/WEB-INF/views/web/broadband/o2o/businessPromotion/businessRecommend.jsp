<%--
  Created by IntelliJ IDEA.
  User: cc
  1:
  Date: 2017/9/12
  Time: 19:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>商机推荐</h1>
        <a href="search" class="relefresh-btn channel-gray3">商机查询</a>
    </div>
</div>
<input type="hidden" name="county" value="${county}" id="county">
<input type="hidden" name="city" value="${city}" id="city">
<input type="hidden" name="cityCode" value="${cityCode}" id="cityCode">
<form id="searchForm" method="post" action="fetchCustomerInfo" accept-charset="UTF-8">
    <input type="hidden" name="addressParam" id="addressParam"/>
    <input type="hidden" name="addressName" id="addressName">
</form>
<!--搜索地址点击搜索后的div start-->
<div id="div_search" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" id="a_retSearch"></a>
            <div class="header-form">
                <input type="text" id="queryAddress" name="queryAddress" class="form-control" placeholder="输入要查询的具体地址"
                       autofocus="autofocus"/>
                <a id="sIcon" class="s-icon"></a>
            </div>
            <a onclick="inputSearch()" class="relefresh-btn" id="searchBtn">搜索</a>
        </div>
    </div>
    <%--查询结果--%>
    <div class="container">
    <ul class="adress-list-box adress-list channel-order" id="knowledgeInfo"></ul>
    </div>
    <%--查询结果结束--%>

    <!--未找到到相关内容 strat-->
    <div class="no-orders mt10" style="display: none;" id="noInfo">
        <img src="${ctxStatic}/images/fa_icon.png"/>
        <p>未找到到相关内容</p>
    </div>
    <!--未找到到相关内容 end-->
</div>
<%--搜索查询结束--%>

<div class="container">
    <div class="borad-adress">
        <div class="adress-box">
            <div class="adress-silder">
                <span id="cityVar">${cityName}</span>
            </div>
            <div class="adress-form">
                <input type="text" class="form-control" placeholder="输入要查询的具体地址" id="inputSearch"/>
                <a href="javascript:void(0)" class="s-icon"></a>
            </div>
        </div>
        <!--热门搜索 start-->
        <div class="hot-adress">
            <p class="adress-title">热门搜索</p>
            <div class="hot-list">
                <c:forEach items="${topList}" var="topList">
                    <a href="javascript:void(0)" onclick="quickSearch('${topList.searchString}');">${topList.searchString}</a>
                </c:forEach>
            </div>
        </div>
        <!--热门搜索 end-->
        <!--搜索记录 start-->
        <div id="searchRecord">
            <div class="seach-records">
                <p class="adress-title">搜索记录</p>
                <ul class="recorads-list" id="record">
                    <c:forEach items="${searchList}" var="searchList">
                        <li><a href="javascript:void(0)" onclick="quickSearch('${searchList.searchString}');">${searchList.searchString}</a></li>
                    </c:forEach>
                </ul>
            </div>
            <!--搜索记录 end-->
            <!--清空 start-->
            <div class="empty-delete">
                <a href="javascript:void(0)" class="delete-icon" id="clearRecord">清空搜索记录</a>
            </div>
        </div>
    </div>
</div>
<script src="${ctxStatic}/js/o2o/bussiness/bussinessPromotion.js" type="text/javascript"></script>
</body>

</html>