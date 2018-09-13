<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>


    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带办理</h1>
    </div>
</div>
<div class="container">
    <ul class="channel-list">
        <%--<li>--%>
            <%--<a href="${ctx}o2oHeBand/init">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus04.png" />--%>
                <%--<h4>和家庭办理</h4>--%>
                <%--<p class="channel-txt">和家庭办理</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <li>
            <a href="${ctx}o2oNewHeBand/init?secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus04.png" />
                <h4>新和家庭办理</h4>
                <p class="channel-txt">新和家庭办理</p>
            </a>
        </li>

        <%--<li>--%>
            <%--<a href="${ctx}newO2oNewHeBand/newInit?secToken=${secToken}">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus04.png" />--%>
                <%--<h4>新和家庭办理(代扣费用)</h4>--%>
                <%--<p class="channel-txt">新和家庭办理(代扣费用)</p>--%>
            <%--</a>--%>
        <%--</li>--%>

        <li>
            <a href="${ctx}o2oNewHeBand/init?secToken=${secToken}&busType=reissue">
                <img src="${ctxStatic}/images/broadBand/bus04.png" />
                <h4>和家庭补办宽带</h4>
                <p class="channel-txt">和家庭补办宽带</p>
            </a>
        </li>
        <c:if test="${openCity eq 'LY'}">
            <li>
                <a href="${ctx}o2oNewHeBand/init?secToken=${secToken}&openCity=${openCity}">
                    <img src="${ctxStatic}/images/broadBand/bus04.png" />
                    <h4>浏阳新和家庭办理</h4>
                    <p class="channel-txt">浏阳新和家庭办理</p>
                </a>
            </li>
            <li>
                <a href="${ctx}o2oNewHeBand/init?secToken=${secToken}&busType=reissue&openCity=${openCity}">
                    <img src="${ctxStatic}/images/broadBand/bus04.png" />
                    <h4>浏阳和家庭补办宽带</h4>
                    <p class="channel-txt">浏阳和家庭补办宽带</p>
                </a>
            </li>
        </c:if>
        <%--<li>--%>
            <%--<a href="${ctx}newO2oNewHeBand/newInit?secToken=${secToken}&busType=reissue">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus04.png" />--%>
                <%--<h4>和家庭补办宽带(代扣费用)</h4>--%>
                <%--<p class="channel-txt">和家庭补办宽带(代扣费用)</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <li>
            <a href="${ctx}o2oBandBooking/linktoBookInstall?secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus04.png" />
                <h4>宽带预约代办</h4>
                <p class="channel-txt">宽带预约代办</p>
            </a>
        </li>
            <%--<li>--%>
                <%--<a href="${ctx}o2oMarketing/init?posterName=NEW_HE_HALF&secToken=${secToken}">--%>
                    <%--<img src="${ctxStatic}/images/broadBand/bus04.png" />--%>
                    <%--<h4>新和家庭半年包</h4>--%>
                    <%--<p class="channel-txt">新和家庭半年包办理</p>--%>
                <%--</a>--%>
            <%--</li>--%>
            <%--<li>--%>
                <%--<a href="${ctx}o2oMarketing/init?posterName=TV_YEAR&secToken=${secToken}">--%>
                    <%--<img src="${ctxStatic}/images/broadBand/bus04.png" />--%>
                    <%--<h4>宽带电视年包</h4>--%>
                    <%--<p class="channel-txt">宽带电视年包办理</p>--%>
                <%--</a>--%>
            <%--</li>--%>
        <li>
            <a href="${ctx}o2oconsupostn/init?secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus07.png" />
                <h4>组网送宽带</h4>
                <p class="channel-txt"> 组网送宽带</p>
            </a>
        </li>

        <%--<li>--%>
            <%--<a href="${ctx}newO2oConsupostn/newInit?secToken=${secToken}">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus07.png" />--%>
                <%--<h4>组网送宽带(代扣费用)</h4>--%>
                <%--<p class="channel-txt"> 组网送宽带(代扣费用)</p>--%>
            <%--</a>--%>
        <%--</li>--%>

        <li>
            <a href="${ctx}o2oconsupostn/init?busiType=1&minCost=48&secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus01.png" />
                <h4>48元档消费叠加型</h4>
                <p class="channel-txt">办套餐，100M宽带电视免费享</p>
            </a>
        </li>

        <%--<li>--%>
            <%--<a href="${ctx}newO2oConsupostn/newInit?busiType=1&minCost=48&secToken=${secToken}">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus01.png" />--%>
                <%--<h4>48元档消费叠加型(代扣费用)</h4>--%>
                <%--<p class="channel-txt">办套餐，100M宽带电视免费享</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="${ctx}o2oconsupostn/init?busiType=1&minCost=88">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus01.png" />--%>
                <%--<h4>88元档消费叠加型</h4>--%>
                <%--<p class="channel-txt">办套餐，100M宽带免费用</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <li>
            <a href="${ctx}o2oconsupostn/init?busiType=1&minCost=88&secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus01.png" />
                <h4>88元档消费叠加型</h4>
                <p class="channel-txt">办套餐，100M宽带免费用</p>
            </a>
        </li>
        <%--<li>--%>
            <%--<a href="${ctx}o2oconsupostn/init?busiType=2&minCost=98">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus02.png" />--%>
                <%--<h4>98元档消费叠加型</h4>--%>
                <%--<p class="channel-txt">办套餐，100M宽带免费用</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="${ctx}o2oconsupostn/init?busiType=2&minCost=158">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus02.png" />--%>
                <%--<h4>158元档消费叠加型</h4>--%>
                <%--<p class="channel-txt">办套餐，100M宽带电视免费享</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <li>
            <a href="${ctx}o2ombh/init">
                <img src="${ctxStatic}/images/broadBand/bus08.png" />
                <h4>魔百和办理</h4>
                <p class="channel-txt"> 魔百和办理</p>
            </a>
        </li>

        <%--<li>--%>
            <%--<a href="${ctx}newO2oMbh/newInit">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus08.png" />--%>
                <%--<h4>魔百和办理(代扣费用)</h4>--%>
                <%--<p class="channel-txt"> 魔百和办理</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="${ctx}/o2obroadbandInstall/init">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus02.png" />--%>
                <%--<h4>单宽带办理</h4>--%>
                <%--<p class="channel-txt">办套餐，100M宽带电视免费享</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="${ctx}o2oSpeedUp/toSpeedUp">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus03.png" />--%>
                <%--<h4>宽带提速</h4>--%>
                <%--<p class="channel-txt">免费升移动光网</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="${ctx}o2oRenew/renewIndex">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus04.png" />--%>
                <%--<h4>宽带续费</h4>--%>
                <%--<p class="channel-txt">100M年包续费限时5折</p>--%>
            <%--</a>--%>
        <%--</li>--%>

        <li>
            <a href="${ctx}o2oBandSource/address">
                <img src="${ctxStatic}/images/broadBand/bus04.png" />
                <h4>宽带资源查询</h4>
                <p class="channel-txt">宽带资源查询</p>
            </a>
        </li>
        <li>
            <a href="${ctx}o2oMyBroadband/queryAccount">
                <img src="${ctxStatic}/images/broadBand/bus05.png" />
                <h4>宽带帐号查询</h4>
                <p class="channel-txt">我的宽带-宽带帐号查询</p>
            </a>
        </li>
        <li>
            <a href="${ctx}o2oOrder/queryOrderList">
                <img src="${ctxStatic}/images/broadBand/bus05.png" />
                <h4>宽带订单管理</h4>
                <p class="channel-txt">我的宽带-宽带订单查询</p>
            </a>
        </li>
        <li>
            <a href="${ctx}infoCollect/search">
                <img src="${ctxStatic}/images/broadBand/bus06.png" />
                <h4>小区信息收集</h4>
                <p class="channel-txt">小区信息收集</p>
            </a>
        </li>

        <li>
            <a href="${ctx}o2oBussinessRecommend/init">
                <img src="${ctxStatic}/images/broadBand/bus08.png" />
                <h4>商机推荐</h4>
                <p class="channel-txt"> 商机推荐</p>
            </a>
        </li>
        <%--<li>--%>
            <%--<a href="${ctx}o2oImsOnly/init">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus08.png" />--%>
                <%--<h4>固话办理</h4>--%>
                <%--<p class="channel-txt">固话办理</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="${ctx}o2oAndEyeCloud/init">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus08.png" />--%>
                <%--<h4>和目云存储办理</h4>--%>
                <%--<p class="channel-txt"> 和目云存储办理</p>--%>
            <%--</a>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="${ctx}businessKnowledge/list">--%>
                <%--<img src="${ctxStatic}/images/broadBand/bus08.png" />--%>
                <%--<h4>营销知识查询</h4>--%>
                <%--<p class="channel-txt"> 营销知识查询</p>--%>
            <%--</a>--%>
        <%--</li>--%>
            <li>
                <a href="${ctx}o2oBusiCollect/search?type=6">
                    <img src="${ctxStatic}/images/broadBand/bus06.png"/>
                    <h4>小区竞争情况搜集</h4>
                    <p class="channel-txt">小区竞争情况搜集</p>
                </a>
            </li>
            <li>
                <a href="${ctx}o2oBusiCollect/searchVillage">
                    <img src="${ctxStatic}/images/broadBand/bus06.png" />
                    <h4>农村预约营销基本商情搜集</h4>
                    <p class="channel-txt">农村预约营销基本商情搜集</p>
                </a>
            </li>
            <li>
                <a href="${ctx}o2oBusiCollect/search?type=5">
                    <img src="${ctxStatic}/images/broadBand/bus06.png" />
                    <h4>“建营装维”第三方合作资源搜集</h4>
                    <p class="channel-txt">全省有线宽带“建营装维”第三方合作资源搜集</p>
                </a>
            </li>
    </ul>
</div>
</body>

</html>