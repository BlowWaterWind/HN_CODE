<%--
  Created by IntelliJ IDEA.
  User: cc
  友商营销信息,用户信息录入
  Date: 2017/9/18
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<!DOCTYPE html>
<html>

<head>

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>

    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css"/>
</head>

<style>
    .user-add li label {
        width: 4.6rem;
        text-align: left;
    }
</style>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="search"></a>
        <h1>信息录入</h1>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/message.jsp" %>
<form method="post" id="keepCommuntyInfo">
    <input type="hidden" name="communityId" value="${o2oCommunityInfoBak.communityId}">
    <input type="hidden" name="communityName" value="${o2oCommunityInfoBak.communityName}">
    <input type="hidden" name="addressPath" value="${o2oCommunityInfoBak.addressPath}">
    <input type="hidden" name="eparchy" value="${o2oCommunityInfoBak.eparchy}">
    <input type="hidden" name="synchronizedType" value="1">
    <input type="hidden" name="type" id="type">
    <input type="hidden" name="pageNoPartner" id="pageNoPartner" value=""/>
    <input type="hidden" name="pageNoUser" id="pageNoUser" value=""/>
    <input type="hidden" name="pageSize" id="pageSize" value="4"/>
</form>
<div class="container">
    <!--小区地址 start-->
    <div class="order-detail">
        <div class="adress-info">
            <div class="adress-txt">
                <p class="adress-number">${o2oCommunityInfoBak.communityName}</p>
                <p class="channel-gray9 f12">${o2oCommunityInfoBak.addressPath}</p>
            </div>
        </div>
    </div>
    <!--小区地址 start-->
    <div class="tabs broad-tabs mt10">
        <ul class="tab-menu">
            <li class="active">友商营销信息</li>
            <li>用户信息</li>
        </ul>
        <div class="content">
            <div id="partnerInfoList">
            </div>
            <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
            <ul class="marke-user" id="userInfos">
            </ul>
            <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
        </div>
    </div>
    <%--两个tab的按钮--%>
    <div id="partnerAdd" class="channel-btn channel-footer" style="padding:0px;"><a onclick="initAddPartnerInfo()"
                                                               class="broad-btn broad-btn-blue" style="padding: 0px;">新增友商信息</a>
    </div>
    <div id="userAdd" class="channel-btn channel-footer" style="padding:0px;"><a onclick="initAddUserInfo()"
                                                            class="broad-btn broad-btn-blue" style="padding: 0px;">新增用户信息</a>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/dropload/dropload.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/comm.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/infoCollect/partnerUserCollect.js"></script>
<script>
    $(function () {
        //tabs
        var id = '${tabType}';
        if (id == "tab3") {
            $($(".tab-menu li")[1]).click();
        } else {
            $($(".tab-menu li")[0]).click();
        }
    });
</script>
<%--fetch partnerInfo--%>
<script type="text/html" id="partnerInfo">
    {{each list as partnerInfo}}
    <div class="marke-info">
        <p>{{partnerInfo.partnersName}}<span class="pl10">{{partnerInfo.communityName}}</span></p>
        <p><label class="channel-gray9">宽带覆盖情况：</label>{{partnerInfo.coverInfo}}</p>
        <p><label class="channel-gray9">是否独家进驻：</label>{{partnerInfo.activityAddress}}</p>
        <p><label class="channel-gray9">宽带客户数量：</label>{{partnerInfo.userNum}}</p>
        <p><label class="channel-gray9">宽带营销及回挖政策：</label>{{partnerInfo.stealBackPolicy}}</p>
        <p class="channel-gray9">现场活动图片：</p>
        <ul class="marke-list">
            {{if partnerInfo.imageUrlA}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlA)}}"/></li>
            {{/if}}
            {{if partnerInfo.imageUrlB}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlB)}}"/></li>
            {{/if}}
            {{if partnerInfo.imageUrlC}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlC)}}"/></li>
            {{/if}}
            {{if partnerInfo.imageUrlD}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlD)}}"/></li>
            {{/if}}
        </ul>
    </div>
    {{/each}}
</script>
<script type="text/html" id="userInfo">
    {{each list as userInfo}}
    <li>
        <div class="marke-con">
            <p class="marke-txt"><span>{{userInfo.userName}}</span><span>{{userInfo.cardId}}</span><span>{{userInfo.serialNumber}}</span>
            </p>
            <p class="channel-gray9 f12">{{userInfo.broadbandProvider}} {{userInfo.broadbandPrice}}/年
                {{timeConver(userInfo.endTime,"yyyy-MM-dd")}}</p>
        </div>
    </li>
    {{/each}}
</script>
</body>
</html>