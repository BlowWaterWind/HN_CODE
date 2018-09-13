<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>

    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>一单清办理</h1>
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
            <a href="${ctx}o2oBroadFusion/init?busiType=0&secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus04.png" />
                <h4>新和家庭办理</h4>
                <p class="channel-txt">新和家庭办理</p>
            </a>
        </li>

            <li>
                <a href="${ctx}o2oBroadFusion/init?busiType=1&minCost=48&secToken=${secToken}">
                    <img src="${ctxStatic}/images/broadBand/bus01.png" />
                    <h4>48元档消费叠加型</h4>
                    <p class="channel-txt">办套餐，100M宽带电视免费享</p>
                </a>
            </li>


        <li>
            <a href="${ctx}o2oBroadFusion/init?busiType=3&secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus07.png" />
                <h4>组网送宽带</h4>
                <p class="channel-txt"> 组网送宽带</p>
            </a>
        </li>

        <li>
            <a href="${ctx}newO2oNewHeBand/newInit?secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus04.png" />
                <h4>新和家庭办理(代扣费用)</h4>
                <p class="channel-txt">新和家庭办理(代扣费用)</p>
            </a>
        </li>

        <li>
            <a href="${ctx}newO2oNewHeBand/newInit?secToken=${secToken}&busType=reissue">
                <img src="${ctxStatic}/images/broadBand/bus04.png" />
                <h4>和家庭补办宽带(代扣费用)</h4>
                <p class="channel-txt">和家庭补办宽带(代扣费用)</p>
            </a>
        </li>

        <li>
            <a href="${ctx}newO2oConsupostn/newInit?secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus07.png" />
                <h4>组网送宽带(代扣费用)</h4>
                <p class="channel-txt"> 组网送宽带(代扣费用)</p>
            </a>
        </li>

        <li>
            <a href="${ctx}newO2oConsupostn/newInit?busiType=1&minCost=48&secToken=${secToken}">
                <img src="${ctxStatic}/images/broadBand/bus01.png" />
                <h4>48元档消费叠加型(代扣费用)</h4>
                <p class="channel-txt">办套餐，100M宽带电视免费享</p>
            </a>
        </li>

        <li>
            <a href="${ctx}newO2oMbh/newInit">
                <img src="${ctxStatic}/images/broadBand/bus08.png" />
                <h4>魔百和办理(代扣费用)</h4>
                <p class="channel-txt"> 魔百和办理</p>
            </a>
        </li>
    </ul>
</div>
</body>

</html>