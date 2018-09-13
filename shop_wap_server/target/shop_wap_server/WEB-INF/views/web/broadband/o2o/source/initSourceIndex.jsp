<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
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