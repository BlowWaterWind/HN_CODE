<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--已废弃不用--%>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>在线售卡-首页</title>
    <link href="${ctxStatic}/css/sim/wap-simh5online.css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
</head>
<body>

<div class="container bg-02">
    <div class="header"></div>
    <div class="king-card">
        <h3>移动大王卡</h3>
        <p>(9元/月)</p>
        <span>原价：18元／月</span>
    </div>
    <div class="btn-wrap">
        <button class="btn btn-gold _goSubmit">立即申请</button>
    </div>
    <div class="king-card-more">
        <a href="#zfxq">更多资费详情 </a>
    </div>
    <form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
        <input type="hidden" name="confId" value="${conf.confId}"/>
        <c:forEach items="${plans}" var="ps">
        <input type="hidden" name="planId" value="${ps.planId}" />
        <input type="hidden" name="planName" value="${ps.planName}" />
        </c:forEach>
    </form>

    <div class="pd-intr-02">
        <div class="title">
            <h3>产品介绍</h3>
        </div>
        <div class="intr">
            <div class="intr-img intr-img-04">
                <img src="${ctxStatic}/images/goods/sim/h5online/img-04.png">
            </div>
            <div class="intr-text">
                <h1>首月免费体验</h1>
                <p>实名激活号卡，首月免费体验，享套
                    <br>餐费全免（全省限量5万户）</p>
            </div>
        </div>
        <div class="intr">
            <div class="intr-text intr-text-02">
                <h1>首月每天送</h1>
                <p>实名激活号卡，当月每天送
                    <br>500M国内流量（最高可获15G）</p>
            </div>
            <div class="intr-img intr-img-05">
                <img src="${ctxStatic}/images/goods/sim/h5online/img-05.png">
            </div>
        </div>
        <div class="intr">
            <div class="intr-img intr-img-06">
                <img src="${ctxStatic}/images/goods/sim/h5online/img-06.png">
            </div>
            <div class="intr-text">
                <h1>线上购卡5折优惠</h1>
                <p>购卡当月起，一年以内月费5折享
                    <br>套餐费优惠至9元/月（原价18元/月） </p>
            </div>
        </div>
        <div class="intr">
            <div class="intr-text intr-text-02">
                <h1>充值有优惠</h1>
                <p>首次充值存50元得60元自由话费</br>
                    （赠送的10元话费72小时内到账）；</br>
                    下载和包支付客户端充值再送50元话费券。
                </p>
            </div>
            <div class="intr-img intr-img-07">
                <img src="${ctxStatic}/images/goods/sim/h5online/img-07.png">
            </div>
        </div>
    </div>
    <!-- end pd-intr -->
    <div class="title">
        <h3 id="zfxq">资费详情</h3>
    </div>
    <div class="plr-24">
        <table class="yellow-table">
            <thead>
            <tr>
                <th rowspan="2">套餐档次</th>
                <th colspan="4">语音</th>
                <th colspan="3">流量</th>
            </tr>
            <tr>
                <th>赠送国内主叫</th>
                <th>来电显示</th>
                <th>国内被叫</th>
                <th>套外国内主叫</th>
                <th>国内流量</th>
                <th>套外国内流量</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>18元</td>
                <td>100分钟</td>
                <td>免费</td>
                <td>免费</td>
                <td>0.19元／分钟</td>
                <td>100M</td>
                <td>0.29元／M</td>
            </tr>
            </tbody>
        </table>
        <div class="tips">
            <p>移动王卡月基本费18元/月，赠送国内主叫100分钟，</br>
                赠来电显示，国内被叫免费，国内主叫语音超出套餐</br>
                后为0.19元/分钟；包含省流量1天1元1G，不用不花</br>
                钱，国内流量100M，套外国内流量0.29元/M。</p>
        </div>
    </div>
    <div class="city-bg"></div>
</div>
<script type="text/javascript">
    $(function () {
        $('._goSubmit').on("click",function() {
            $("#applyForm").submit();
        });
    });
</script>

</body>
</html>