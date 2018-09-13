<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width"/>
    <meta name="viewport" content="initial-scale=1.0,user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <title>在线售卡-我要推荐</title>
    <link href="${ctxStatic}/css/sim/base.css?v=2" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/wap-simh5onlineV2.css?v=2" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/css.css?v=8" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>

    <script type="text/javascript">
        var ctx = ${ctx};
    </script>
</head>

<body>
<div class="box">
    <div class="content">
        <img class="title2 w2" src="${ctxStatic}/css/sim/images/title2.png"/>
        <div class="yqjl_box">
            <div class="title">待充值记录<span class="fc_rose">${charge}个</span><a  onclick="noticeSms()"><img src="${ctxStatic}/css/sim/images/button05.png"></a></div>
            <div class="left_nav">
                <ul class="list">
                    <c:set var="size" value="${size}"></c:set>
                    <c:if test="${size==0}">
                       <li  style="margin-left:70px";><div><span style="color:lightgrey";>暂无邀请记录</span></div></li>
                    </c:if>
                    <c:forEach  items="${orderRecmdRefList}" var="recmd">
                        <li>
                            <c:set var="orderFirstCharge" value="${recmd.orderFirstCharge}"></c:set>
                            <c:set var="electStatus" value="${recmd.electStatus}"></c:set>
                            <div class="fl w3">${recmd.phone}</div>
                            <div>
                                <span class="ico">
                                <img src="${ctxStatic}/css/sim/images/finish.png">下单
                                </span>
                                <span class="mt">
                                <img src="${ctxStatic}/css/sim/images/dashed.png">
                                </span>
                                <c:choose>
                                    <c:when test="${empty recmd.actStatus}">
                                        <span class="ico">
                                            <img src="${ctxStatic}/css/sim/images/lock.png" >激活
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="ico">
                                            <img src="${ctxStatic}/css/sim/images/finish.png">激活
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                                <span class="mt"><img src="${ctxStatic}/css/sim/images/dashed.png"></span>

                                <c:choose>
                                    <c:when test="${empty orderFirstCharge}">
                                        <span class="ico">
                                            <img src="${ctxStatic}/css/sim/images/lock.png" >首充
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="ico">
                                            <img src="${ctxStatic}/css/sim/images/finish.png">首充
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                                <span class="mt"><img src="${ctxStatic}/css/sim/images/dashed.png"></span>

                                <c:choose>
                                    <c:when test="${electStatus!=1}">
                                         <span class="ico">
                                             <img src="${ctxStatic}/css/sim/images/lock.png">
                                             <span style="margin-left:-30%;">领取和包券</span>
                                         </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="ico">
                                            <img src="${ctxStatic}/css/sim/images/finish.png">
                                              <span style="margin-left:-30%;">领取和包券</span>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </li>
                    </c:forEach>

                </ul>
                <div class="more"><span>展开</span></div>
                <div class="clear"></div>
            </div>
        </div>
    </div>
</div>

<%-- 公用弹窗 --%>
<div id="sorry-modal" class="mask-layer">
    <div class="modal small-modal" style="width:18rem">
        <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')" style="width: 1.2rem;
    height: 1.2rem;"></a>
        <div class="modal-content">
            <%--<div class="pt-30"></div>--%>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group" style="font-size:1.0rem">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>


<div id="toLogin" class="mask-layer">
    <div class="modal small-modal" style="width:18rem">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('toLogin')" style="width: 1.2rem;
    height: 1.2rem;"></a>
        <div class="modal-content">
            <%--<div class="pt-30"></div>--%>
            <p class="text-center _pomptTxt">登录超时,请重新的登录</p>
        </div>
        <div class="btn-group" style="font-size:1.0rem">
            <a href="${ctx}/recmd/toGenSimRecmdLinkWei" class="confirm-btn">前往登录</a>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=4"></script>
<script src="${ctxStatic}/js/goods/sim/theme.js"></script>
<script>
    function noticeSms() {
        $.ajax({
            url: ctx + "/recmd/noticeSms",
            type: "post",
            dataType: "json",
            success: function (data) {
                console.log(data);
                if (data.code == "0") {
                    toggleModal("sorry-modal", data.message);
                } else if(data.code == '-3'){
                    //登录超时
                    $("#toLogin").toggle();
                }else{
                    toggleModal("sorry-modal", data.message);
                }
            },
            error: function () {
                toggleModal("sorry-modal", data.message);
            }
        });
    }
</script>
</body>
</html>