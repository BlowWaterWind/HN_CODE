<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2017/9/12
  Time: 19:45
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
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <div class="header-form">
            <input type="text" class="form-control" />
            <a href="javascript:void(0)" class="s-icon"></a>
        </div>
        <a href="javascript:void(0)" class="relefresh-btn">搜索</a>
    </div>
</div>
<div class="container">
    <ul class="adress-list-box">
        <li>
            <a href="javascript:void(0)">
                <div class="collect-txt">
                    <p>杨帆小区</p>
                    <span class="channel-gray9 f12">芙蓉区荷花路369号</span>
                </div>
            </a>
        </li>
        <li>
            <a href="javascript:void(0)">
                <div class="collect-txt">
                    <p>杨帆小区</p>
                    <span class="channel-gray9 f12">芙蓉区荷花路369号</span>
                </div>
            </a>
        </li>
    </ul>
    <!--未找到到相关内容 strat-->
    <div class="no-orders mt10" style="display: none;">
        <img src="images/fa_icon.png" />
        <p>未找到到相关内容</p>
    </div>
    <!--未找到到相关内容 end-->
</div>
</body>

</html>