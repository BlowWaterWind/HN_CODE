<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<link rel="icon" href="${ctxStatic}/images/icon/favicon.ico" type="images/x-icon" />
<link rel="apple-touch-icon-precomposed" href="${ctxStatic}/images/icon/logo-icon.png" type="image/png" />
<!--校园迎新活动公用样式-->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/activityGlobal.css"/>
<!--页面样式-->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/coupon.css">
<!--引入Jquery-->
<script src="${ctxStatic}/js/jquery-1.7.2.min.js" type="text/javascript" charset="utf-8"></script>
<title>购机券使用专区</title>
<body class="bg-yellow">
<!--顶部：主题展示-->
<div class="w-1440">
    <img src="${ctxStatic}/images/coupon-bg-1.png"/>
    <img class="top-img" src="${ctxStatic}/images/coupon-topic.png"/>
</div>
<!--屋顶-->
<div class="w-1440 roof">
    <div class="w-1200">
        <img src="${ctxStatic}/images/coupon-roof.png"/>
    </div>
</div>
<!--手机展示区-->
<div class="w-1440 phone-bg">
    <div class="w-1200 pb-10">
        <div class="container">
            <!--手机列表-->
            <ul class="phone-list clear">
                <c:forEach items="${goodsList}" var="goods">
                    <li class="phone-list__item">
                        <p class="phone-name">${goods.goodsName}</p>
                        <div class="phone-info clear">
                            <c:forEach items="${goods.goodsStaticList}" var="goodsStatic">
                                <c:if test="${goodsStatic.goodsStaticDefault == 0}">
                                    <img class="fl xs-w-40per" width="30%" src="${tfsUrl}${fns:resImageSize(goodsStatic.goodsStaticUrl,"80x100")}" />
                                </c:if>
                            </c:forEach>
                            <div class="fr w-70per xs-w-60per">
                                <p class="price-tle">抢购价</p>
                                <p class="price">${goods.goodsSalePrice/100}</p>
                                <a class="buy-btn" href="">立即购买</a>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="w-70per m-auto mb-10 clear tc">
                <a class="more-phone" href="${ctx}/goodsQuery/linkToGoodsList?categoryId=10000001&queryListpageCode=PHONE_LISTPAGE_B2C&queryListpageId=100">更多机型>></a>
            </div>
            <!--温馨提示-->
            <div class="tips">
                <p class="tips-tle">温馨提示</p>
                <p>&nbsp;&nbsp;</p>
                1.购机券仅限在湖南移动网上商城购买800元以上指定机型时使用;</br>
                2.有效期为30天，10月30日后所有活动购机券失效;</br>
                3.购机券不可拆分，不找现，不予其他优惠券叠加使用。</br>
                4.购机券使用流程：选定手机确认购买后支付方式请选择”和包“，跳　转至和包支付登陆页面，若您未注册和包请先注册，登陆后即可勾选　300元购机电子券直抵现金购买手机。
            </div>
        </div>
    </div>
</div>
</body>
<script src="${ctxStatic}/js/coupon.js" type="text/javascript" charset="utf-8"></script>
</html>