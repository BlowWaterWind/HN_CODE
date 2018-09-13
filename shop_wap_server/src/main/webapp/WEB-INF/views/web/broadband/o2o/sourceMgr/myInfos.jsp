<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2017/9/25
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>录入过的信息</h1>
    </div>
</div>
<div class="container">
    <div class="collect-con">
        <c:if test="${fn:length(myInfos) > 0}">
            <ul class="adress-list-box">
                <c:forEach items="${myInfos}" var="item">
                    <li>
                        <a href="javascript:void(0)" class="collect-icon">
                            <div class="collect-txt">
                                <p>${item.zoneName}</p>
                                <span class="channel-gray9 f12">${item.addressPath}</span>
                            </div>
                            <c:if test="${item.synchronizedType == '0'}">
                            <form action="initAdd" method="post">
                                <input type="hidden" name="infoType" value="2"/>
                                <input type="hidden" name="communityId" value="${item.communityId}"/>
                                <input type="hidden" name="communityName" value="${item.zoneName}"/>
                                <input type="hidden" name="addressPath" value="${item.addressPath}"/>
                                <div class="collect-btn">
                                    <input onclick="collectInfo(this)" type="button" class="btn btn-border-blue"
                                           value="信息修改"/>
                                </div>
                            </form>
                            </c:if>
                            <c:if test="${item.synchronizedType == '1'}">
                                <form action="initAddPartnerOrUser" method="post">
                                    <input type="hidden" name="communityId" value="${item.communityId}"/>
                                    <input type="hidden" name="communityName" value="${item.zoneName}"/>
                                    <input type="hidden" name="addressPath" value="${item.addressPath}"/>
                                    <div class="collect-btn">
                                        <input onclick="collectInfo(this)" type="button" class="btn btn-border-blue"
                                               value="信息修改"/>
                                    </div>
                                </form>
                            </c:if>

                        </a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
        <!--未找到到相关内容 strat-->
        <c:if test="${fn:length(myInfos) == 0}">
            <div class="no-orders" style="display:block;">
                <img src="${ctxStatic}/images/fa_icon.png"/>
                <p>未找到到相关内容</p>
            </div>
        </c:if>
        <!--未找到到相关内容 end-->
    </div>
</div>
</body>
<script type="text/javascript">
    function collectInfo(thisDiv) {
        $(thisDiv).parent().parent().submit();
    }
</script>

</html>