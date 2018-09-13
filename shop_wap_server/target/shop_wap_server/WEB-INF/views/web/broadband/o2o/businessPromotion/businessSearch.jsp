<%--
  Created by IntelliJ IDEA.
  User: cc
  3:商机查询
  Date: 2017/9/12
  Time: 19:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>商机查询</h1>
    </div>
</div>
<!--搜索地址点击搜索后的div start-->
<div id="div_search" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" id="a_retSearch"></a>
            <div class="header-form">
                <input type="text" id="telSearch" name="queryAddress" class="form-control" placeholder="输入要查询用户的电话号码"
                       autofocus="autofocus"/>
                <a id="sIcon" class="s-icon"></a>
            </div>
            <a onclick="telSearch()" class="relefresh-btn" id="searchBtn">搜索</a>
        </div>
    </div>
    <%--查询结果--%>
    <div id="personalInfo"></div>
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
    <div class="collect-con">
        <div class="collect-top">
            <input type="text" class="form-control" placeholder="输入客户的手机号进行查询" id="inputSearch"/>
            <a onclick="telSearch()" class="s-icon"></a>
        </div>
    </div>
    <!--热门搜索 start-->
    <div class="hot-adress">
        <p class="adress-title">热门搜索</p>
        <div class="hot-list">
            <c:forEach items="${topList}" var="topList">
                <a href="javascript:void(0)" onclick="telSearch('${topList.searchString}');">${topList.searchString}</a>
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
                    <li><a href="javascript:void(0)" onclick="telSearch('${searchList.searchString}');">${searchList.searchString}</a></li>
                </c:forEach>
            </ul>
        </div>
        <!--搜索记录 end-->
        <!--清空 start-->
        <div class="empty-delete">
            <a href="javascript:void(0)" class="delete-icon" id="clearRecordSearch">清空搜索记录</a>
        </div>
    </div>
</div>
</body>
</html>
<script type="text/html" id="personal">
    {{each data as info}}
    <div class="personal-con">
        <div class="personal-silder">
            <p class="pb5">{{ info.userName}}</p>
            <span>{{ info.serialNumber}}</span>
        </div>
        <div class="personal-right">
            <a href="sms:{{ info.serialNumber }}" class="msg-icon pb5">发送短信</a>
            <a href="tel:{{ info.serialNumber }}" class="phone-icon">联系客户</a>
        </div>
    </div>
    <div class="channel-entry">
        <!--近三个月消费 start-->
        <div class="entry-list">
            <div class="entry-title b-color01">近三个月消费</div>
            <div class="entry-con">
                {{ if info.recentCost != null}}
                {{info.recentCost}}
                {{else if info.recentCost == null}}
                暂无数据
                {{else}}
                {{ /if }}
            </div>
        </div>
        <!--近三个月消费 end-->
        <!--宽带套餐 start-->
        <div class="entry-list">
            <div class="entry-title b-color02">宽带套餐</div>
            <div class="entry-con">
                {{ if info.broadbandPackage != null}}
                {{info.broadbandPackage}}
                {{else if info.broadbandPackage == null}}
                暂无数据
                {{else}}
                {{ /if }}
            </div>
        </div>
        <!--宽带套餐 end-->
        <!--家庭增值业务 start-->
        <div class="entry-list">
            <div class="entry-title b-color03">家庭增值业务</div>
            <div class="entry-con">
                {{ if info.familyAddedService != null}}
                {{info.familyAddedService}}
                {{else if info.familyAddedService == null}}
                暂无数据
                {{else}}
                {{ /if }}
            </div>
        </div>
        <!--家庭增值业务 end-->
    </div>
    {{/each}}
</script>
<script type="text/javascript" src="${ctxStatic}/js/o2o/bussiness/bussinessPromotion.js"></script>