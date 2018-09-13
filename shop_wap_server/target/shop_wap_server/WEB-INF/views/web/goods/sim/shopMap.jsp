<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/7/23
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>店铺导航</title>
    <%--<link href="${ctxStatic}/css/sim/wap_list.css" rel="stylesheet">--%>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type='text/javascript' src='http://api.map.baidu.com/api?v=2.0&amp;ak=C93b5178d7a8ebdb830b9b557abce78b' charset="UTF-8"></script>
    <script type="text/javascript" src="${ctxStatic}/js/baidumap/convertor.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/baidumap/baidumapForShop.js?v=20150906"></script>
    <script type="text/javascript">

        $(function(){
            baidu_map.locate(baidu_map.showPosition);
        });
    </script>
    <style type="text/css">
        .topbar {
            background-color: #fff;
            height: 1.333333rem;
            -webkit-box-align: center;
            -ms-flex-align: center;
            align-items: center;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            margin-bottom: .04rem;
        }
        .topbar .icon-return {
            padding-left: .4rem;
            padding-right: .4rem;
            background: url("${ctxStatic}/css/sim/images/icon-return.png") 50% no-repeat;
            height: 100%;
            background-size: .24rem .426667rem;
            width: 42px;
        }

        .topbar .icon-close {
            padding-left: .133333rem;
            padding-right: .266667rem;
            background: url("${ctxStatic}/css/sim/images/icon-close.png") 50% no-repeat;
            height: 100%;
            background-size: .44rem .44rem;
            width: 42px;
        }

        .topbar h3 {
            font-size: .48rem;
            margin-left: .266667rem;
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
        }

        html{height:100%}
        body{height:100%;margin:0px;padding:0px}
        #container{height:100%}
    </style>
    <style type="text/css">
        #allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
</head>
<body>
<input id='LONGITUDE' type='hidden' value="${LONGITUDE}"/>
<input id='LATITUDE' type='hidden' value="${LATITUDE}"/>
<input id='SHOPNAME' type='hidden' value="${SHOPNAME}"/>
<input id='SHOPADDR' type='hidden' value="${SHOPADDR}"/>
<input id='SHOPID' type='hidden' value="${SHOPID}"/>
<div class="top container">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return"></a>
        <a href="javascript:window.history.back();" class="icon icon-close"></a>
        <h3>路线导航</h3>
    </div>
    <div id="container" ></div>
</div>
<!--<div id="allmap"></div>-->
</body>
</html>
