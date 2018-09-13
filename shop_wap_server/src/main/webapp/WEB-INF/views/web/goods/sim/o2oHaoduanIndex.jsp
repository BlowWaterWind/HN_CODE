<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/7/25
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>号段卡分享</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>

    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=6"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>
    <style type="text/css">
        li{
            position: relative;
        }
        .sms-icon{
            width: 1.5rem;
            height: 1.5rem;
            background-image: url("${ctxStatic}/images/sms.png");
            background-size: contain;
            background-repeat: no-repeat;
            position: absolute;
            right: 1rem;
            top:1rem;
        }

        .share-icon{
            width: 1.5rem;
            height: 1.5rem;
            background-image: url("${ctxStatic}/images/sharehaoduan.png");
            background-size: contain;
            background-repeat: no-repeat;
            position: absolute;
            right: 2.9rem;
            top:1rem;
        }
    </style>
</head>
<body>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>号段卡分享</h1>
    </div>
</div>
<div class="container">
    <ul class="channel-list">
        <c:forEach items="${recmds}" var="index">
        <li style="margin-top: 10px">
            <span>
                <img src="${ctxStatic}/images/simpay/pay_${index.cardType}.png" />
                <h4>${index.cardTypeName}</h4>
                <p class="channel-txt">${index.planDesc}</p>
            </span>
            <i class="sms-icon" onclick="smsDirect('${index.recmdUserLink}','${index.templateId}')"></i>
            <i class="share-icon" onclick="shareSimHaoduanka('${index.cardTypeName}','${index.recmdUserLink}')"></i>
        </li>
        </c:forEach>
    </ul>
</div>
</body>

</body>
<script type="text/javascript">
    var tken = $.cookie('token');
    var secToken = $.cookie('secToken');
    //跳转到sms-create.html页面后token丢失,故重新设置一下token cookie
    $.cookie('secToken', secToken, {"domain":"wap.hn.10086.cn","path":"/"});

    $.cookie('token', tken, {"domain":"wap.hn.10086.cn","path":"/"});
    $.cookie('userToken', tken, {"domain":"wap.hn.10086.cn","path":"/"});
    function smsDirect(linkUrl,templateId) {
        window.location.href = "http://wap.hn.10086.cn/ecsmc-static/static/sp/html/sms-create.html?templateId="+
            templateId+"&linkUrl="+linkUrl;
    }
</script>
</html>
