<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
        var  ctx = '${ctx}';
    </script>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>账户宽带信息</h1>
    </div>
</div>--%>
<c:set value="账户宽带信息" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<div class="container bg-gray hy-tab">
    <div class="wf-list sub-info">
        <c:choose>
            <c:when test="${broadbandDetailInfoResult.resultCode == '0'}">
                <div class="wf-ait clearfix">
                    <div class="wf-tit wf-cit font-gray">安装地址：</div>
                    <div class="wf-con">
                        <p>${broadbandDetailInfoResult.broadbandDetailInfo.addrDesc}</p>
                    </div>
                </div>
                <div class="wf-ait clearfix">
                    <div class="wf-tit wf-cit font-gray">用户信息</div>
                    <div class="wf-con">
                        <p>姓名：${broadbandDetailInfoResult.broadbandDetailInfo.custName}</p>
                        <p>身份证：${broadbandDetailInfoResult.broadbandDetailInfo.psptId}</p>
                        <p>手机：${broadbandDetailInfoResult.broadbandDetailInfo.serialNumber}</p>
                    </div>
                </div>
                <c:if test="${fn:length(broadbandDetailInfoResult.broadbandDetailInfo.mbhDetailInfos)>0}">
                    <div class="wf-ait clearfix">
                        <div class="wf-tit wf-cit font-gray">魔百和</div>
                        <div class="wf-con">
                            <p>魔百和账户：${broadbandDetailInfoResult.broadbandDetailInfo.mbhDetailInfos[0].serialNumber}</p>
                        </div>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="wf-ait clearfix">
                    <div class="wf-con">
                        <p>无法查询到宽带信息,请稍后重试! </p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div class="container active-fix">
            <a href="${ctx}/newHeBand/heInstall?hasAddress=Y&installPhoneNum=${installPhoneNum}" class="btn btn-rose">使用该地址</a>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
</body>
</html>
