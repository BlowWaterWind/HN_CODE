<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>湖南移动网上营业厅</title>
    <style>
        b {
            font-weight: bold
        }
        .divMargin{
            margin: 20px;
        }
    </style>
</head>
<body>

<div class="container bg-gray hy-tab">
    <div class="suess-con">
        <div class="suess-list" style="text-align: left;">
            <H1>用户信息</H1>
            <div class="divMargin">
                <b>memberId:</b>${memberVo.memberLogin.memberId}<br>
                <b>memberLogingName:</b>${memberVo.memberLogin.memberLogingName}<br>
                <b>memberPassword:</b>${memberVo.memberLogin.memberPassword}<br>
                <b>memberPhone:</b>${memberVo.memberLogin.memberPhone}<br>
                <b>memberTypeId:</b>${memberVo.memberLogin.memberTypeId}<br>
                <b>memberStatusId:</b>${memberVo.memberLogin.memberStatusId}<br>
            </div>
        </div>
        <div class="suess-list" style="text-align: left;">
            <H1>渠道工号信息</H1>
            <div class="divMargin">
                <b>provinceCode:</b>${memberVo.channelInfo.provinceCode}<br>
                <b>tradeDepartPasswd:</b>${memberVo.channelInfo.tradeDepartPasswd}<br>
                <b>tradeStaffId:</b>${memberVo.channelInfo.tradeStaffId}<br>
                <b>tradeEparchyCode:</b>${memberVo.channelInfo.tradeEparchyCode}<br>
                <b>tradeCityCode:</b>${memberVo.channelInfo.tradeCityCode}<br>
                <b>tradeDepartId:</b>${memberVo.channelInfo.tradeDepartId}<br>
                <b>routeEparchyCode:</b>${memberVo.channelInfo.routeEparchyCode}<br>
                <b>tradeTerminalId:</b>${memberVo.channelInfo.tradeTerminalId}<br>
                <b>inModeCode:</b>${memberVo.channelInfo.inModeCode}<br>
            </div>
        </div>
        <div class="suess-list" style="text-align: left;">
            <H1>店铺信息</H1>
            <div class="divMargin">
                <b>shopName:</b>${memberVo.shopInfo.shopName}<br>
                <b>shopId:</b>${memberVo.shopInfo.shopId}<br>
            </div>
        </div>
        <div class="suess-list" style="text-align: left;">
            <H1>商城用户信息</H1>
            <div class="divMargin">
                <b>memberId:</b>${member.memberId}<br>
                <b>memberRealname:</b>${member.memberRealname}<br>
                <b>memberNickname:</b>${member.memberNickname}<br>
                <b>memberQq:</b>${member.memberQq}<br>
                <b>memberRegiterTime:</b>${member.memberRegiterTime}<br>
                <b>memberWechat:</b>${member.memberWechat}<br>
                <b>memberSex:</b>${member.memberSex}<br>
                <b>memberAddress:</b>${member.memberAddress}<br>
                <b>memberProvince:</b>${member.memberProvince}<br>
                <b>memberCity:</b>${member.memberCity}<br>
                <b>memberCounty:</b>${member.memberCounty}<br>
                <b>memberHeadimage:</b>${member.memberHeadimage}<br>
                <b>memberFavorite:</b>${member.memberFavorite}<br>
            </div>
        </div>
        <a href="http://test.asinfo.com:8055">测试地址</a>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
</body>
</html>
