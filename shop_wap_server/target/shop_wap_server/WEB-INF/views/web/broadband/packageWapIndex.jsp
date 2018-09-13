<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带地址查询</h1>
    </div>
</div>
<input type="hidden" name="county" value="${county}" id="county">
<input type="hidden" name="city" value="${city}" id="city">
<input type="hidden" name="communityName" value="${communityName}" id="communityName">
<div class="container">
    <%--<div class="borad-adress">--%>
        <%--<div class="adress-box">--%>
            <%--<div class="adress-silder">--%>
                <%--<span>${city}${county}</span>--%>
            <%--</div>--%>
            <%--<div class="adress-form">--%>
                <%--<input type="text" class="form-control" id="address" placeholder="扬帆小区" />--%>
                <%--<a href="javascript:void(0)" class="s-icon"></a>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <div class="borad-selected">
        <div class="selected-list">
            <a href="javascript:void(0)" class="selected-link collent">
                <img src="${ctxStatic}/images/broadBand/kdtu.jpg" class="selected-img" />
                <div class="selected-title pb5">
                    <p class="selected-txt">${addressPath}</p>
                    <c:if test="${hasData==false}">
                         <span class="channel-blue pl10">未覆盖</span>
                    </c:if>
                    <c:if test="${hasData==true}">
                         <span class="channel-blue pl10">已覆盖</span>
                    </c:if>
                </div>
                <p>交维状态：</p>
                <p>端口资源：</p>
            </a>
            <ul class="selected-sub collent-item">
                <c:if test="${hasData==true}">
                    <c:forEach items="${broadbandPosterList}" var="broadbandItem" varStatus="status">

                        <c:if test="${broadbandItem.status == '1'}">
                            <li>
                            <a href="javascript:void(0)" class="sub-link">
                                <div class="selected-info">
                                    <img src="${broadbandItem.imgUrl}" />
                                    <h4>${broadbandItem.posterName}</h4>
                                    <p class="sub-txt">${broadbandItem.intro}</p>
                                    <span class="channel-blue price">${broadbandItem.feeInfo}</span>
                                </div>
                            <%--<input type="button" class="broad-btn broad-btn-blue" value="立即办理" />--%>
                                <input type="button" class="broad-btn broad-btn-blue" data-href="${broadbandItem.url}" onclick="redirctFun(this)" value="立即办理" />
                            <%--<a class="broad-btn broad-btn-blue" href="${broadbandItem.url}">立即办理</a>--%>
                            </li>
                        </c:if>

                     </c:forEach>

                <c:forEach items="${hebroadbandPosterList}" var="broadbandItem" varStatus="status">
                    <c:if test="${broadbandItem.status == '1'}">
                        <li>
                            <a href="javascript:void(0)" class="sub-link">
                                <div class="selected-info">
                                    <img src="${broadbandItem.imgUrl}" />
                                    <h4>${broadbandItem.posterTitle}</h4>
                                    <p class="sub-txt">${broadbandItem.intro}</p>
                                    <span class="channel-blue price">${broadbandItem.feeInfo}</span>
                                </div>
                                    <%--<input type="button" class="broad-btn broad-btn-blue" value="立即办理" />--%>
                                <input type="button" class="broad-btn broad-btn-blue" data-href="${broadbandItem.url}" onclick="redirctFun(this)" value="立即办理" />
                                <%--<a class="kdrm-list-btn " href="${broadbandItem.url}">立即办理</a>--%>
                        </li>
                    </c:if>
                </c:forEach>

                <c:forEach items="${consupostnPosterList}" var="broadbandItem" varStatus="status">
                    <c:if test="${broadbandItem.status == '1'}">
                        <li>
                            <a href="javascript:void(0)" class="sub-link">
                                <div class="selected-info">
                                    <img src="${broadbandItem.imgUrl}" />
                                    <h4>${broadbandItem.posterTitle}</h4>
                                    <p class="sub-txt">${broadbandItem.intro}</p>
                                    <span class="channel-blue price">${broadbandItem.feeInfo}</span>
                                </div>
                                    <%--<input type="button" class="broad-btn broad-btn-blue" value="立即办理" />--%>
                                <input type="button" class="broad-btn broad-btn-blue" data-href="${broadbandItem.url}" onclick="redirctFun(this)" value="立即办理" />
                        </li>
                    </c:if>
                </c:forEach>
                </c:if>
                </ul>
        </div>
    </div>
</div>

<script>
    $(function() {
        //折叠面板
        $(".collent").click(function() {
            $(".collent-item").not($(this).next()).hide();
//            $(".collent").not($(this).next()).removeClass("item");
            $(this).next().slideToggle(500);
            $(this).toggleClass("item");
        });
//        $("#queryCommunityInfo").click(function(){
//            var county = $("#county").val();
//            var city   = $("#city").val();
//            var communityName = $("#communityName").val();
//            window.location.href=ctx+"o2oBandSource/initSourceIndex?county="+county+"&city="+city+"&communityName="+communityName;
//        });
    });
    function redirctFun(param){
        var href = $(param).attr("data-href");
        window.location.href = href;
    }
</script>
</body>

</html>