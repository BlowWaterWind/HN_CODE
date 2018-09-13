<%--
  Created by IntelliJ IDEA.
  User: cc
  1:营销芝士管理
  Date: 2017/9/12
  Time: 19:37
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
    <link href="${ctxStatic}/css/broadBand/pure-grids.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.min.js"></script>
</head>

<%@ include file="/WEB-INF/views/include/message.jsp" %>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>营销知识查询</h1>
    </div>
</div>
<!--搜索地址点击搜索后的div start-->
<div id="div_search" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" id="a_retSearch"></a>
            <div class="header-form">
                <input type="text" id="queryKeyword" name="queryAddress" class="form-control" placeholder="请输入要查询的营销信息"
                       autofocus="autofocus"/>
                <a onclick="inputQuery()" class="s-icon"></a>
            </div>
            <a onclick="inputQuery()" class="relefresh-btn">搜索</a>
        </div>
    </div>

    <div id="afterSearch">
        <div class="hot-adress">
            <p class="adress-title">热门搜索</p>
            <div class="hot-list" id="hotRecordList">
            </div>
        </div>
        <div class="seach-records">
            <p class="adress-title">搜索记录</p>
            <ul class="recorads-list" id="hisRecordUl">
            </ul>
        </div>
        <!--清空 start-->
        <div class="empty-delete">
            <a href="javascript:void(0)" class="delete-icon" id="clearRecord">清空搜索记录</a>
        </div>
    </div>
    <%--查询结果--%>
    <ul class="channel-order" id="knowledgeInfo"></ul>
    <%--查询结果结束--%>

    <!--未找到到相关内容 strat-->
    <div class="no-orders mt10" style="display: none;" id="noInfoSearch">
        <img src="${ctxStatic}/images/fa_icon.png"/>
        <p>未找到到相关内容</p>
    </div>
    <!--未找到到相关内容 end-->
</div>
<%--搜索查询结束--%>

<%--使用tab键进行搜索的div--%>
<div class="container" id="container">
    <div class="borad-adress">
        <div class="adress-box">
            <div class="adress-silder">
                <span id="cityVar">${cityName}</span>
            </div>
            <div class="adress-form">
                <input id="search" type="text" class="form-control" placeholder="营销知识查询"/>
                <a href="javascript:void(0)" class="s-icon"></a>
            </div>
        </div>
        <!--促销活动选择 start-->
        <div class="promotions-con">
            <div class="promotions-list pure-g">
                <a href="javascript:void(0)" class="pure-u-1-2 tariff active" value="1">资费信息</a>
                <a href="javascript:void(0)" class="pure-u-1-2 mot" value="0">促销活动</a>
            </div>
            <!--资费信息 start-->
            <ul class="promotions-info select01" style="display:;">
                <li onclick="setSearchCondition('1','0')"><a>全部资费</a></li>
                <li onclick="setSearchCondition('1','1')"><a>宽带资费</a></li>
                <li onclick="setSearchCondition('1','2')"><a>产品资费</a></li>
                <li onclick="setSearchCondition('1','3')"><a>魔百和</a></li>
            </ul>
            <!--资费信息 end-->
            <!--促销活动 start-->
            <ul class="promotions-info select02" style="display: none;">
                <li onclick="setSearchCondition('2','0')"><a>全部活动</a></li>
                <li onclick="setSearchCondition('2','1')"><a>存送优惠</a></li>
                <li onclick="setSearchCondition('2','2')"><a>终端促销</a></li>
                <li onclick="setSearchCondition('2','3')"><a>业务促销</a></li>
            </ul>
            <!--促销活动 end-->
        </div>
        <!--促销活动选择 end-->
        <!--活动列表 start-->
        <ul class="channel-order" id="clickSearch">
        </ul>
        <!--活动列表 end-->
        <!--未找到到相关内容 strat-->
        <div class="no-orders mt10" style="display: none;" id="noInfoClick">
            <img src="${ctxStatic}/images/fa_icon.png"/>
            <p>未找到到相关内容</p>
        </div>
        <!--未找到到相关内容 end-->
    </div>
</div>
<%--结束--%>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script src="${ctxStatic}/js/o2o/bussiness/bussinessKnowledge.js" type="text/javascript"></script>
<script>
    $(function () {
        //筛选
        $('.tariff').on('click', function () {
            $('.select01').fadeToggle();
            $('.select02').hide();
            $(".mot").removeClass("active");
            $(".tariff").toggleClass("active");

        });
        $('.select01 li').on('click', function () {
            var $li = $(this);
            $li.addClass('active').siblings().removeClass('active');
            $(".select01").removeClass("active");
        });
        $('.mot').on('click', function () {
            $('.select02').fadeToggle();
            $('.select01').hide();
            $(".tariff").removeClass("active");
            $(".mot").toggleClass("active");
        });
        $('.select02 li').on('click', function () {
            var $li = $(this);
            $li.addClass('active').siblings().removeClass('active');
            $(".select02").removeClass("active");
        });
    });

    $($('.select01 li')[0]).click();

</script>
</body>

</html>
