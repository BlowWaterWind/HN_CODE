<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<html>
<head>
    <meta charset="UTF-8">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <meta name="WT.si_n" content="GJJ" />
    <meta name="WT.si_x" content="20" />
    <title>湖南移动商城</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/swiper.min.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/wap.css" />
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/swiper.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>



<body class="bg-yellow">
<div class="container">
    <input type="hidden" id="isLogin" name="isLogin" value="${returnMap.isLogin}"/>
    <img class="card-center" src="${ctxStatic}/images/campusmark/card-center.png" alt="">

    <c:forEach items="${myCoupons}" var="cp" varStatus="cpStatus">

            <c:if test="${cp.price == 50}">
                <a href="javascript:;" id="quan_${cp.price}" class="card-center-bg">
                <p class="card-center-txt">满500<b>减50元</b></p>
                <p class="card-center-txt2">购机时预存50元话费可享受此优惠</p>
                    <a href="javascript:;"  id="show_${cp.price}" class="quan-win">
                    <img  class="red-quan" src="${ctxStatic}/images/campusmark/50-red.png" alt="">
                    <p>${cp.goodsSn}</p>
                </a>
                </a>
            </c:if>
            <c:if test="${cp.price == 100}">
                <a href="javascript:;" id="quan_${cp.price}" class="card-center-bg">
                <p class="card-center-txt">满700<b>减100元</b></p>
                <p class="card-center-txt2">购机时预存100元话费可享受此优惠</p>
                    <a href="javascript:;" id="show_${cp.price}" class="quan-win" >
                    <img  class="red-quan" src="${ctxStatic}/images/campusmark/100-red.png" alt="">
                    <p>${cp.goodsSn}</p>
                </a>
                </a>
            </c:if>
            <c:if test="${cp.price == 200}">
                <a href="javascript:;" id="quan_${cp.price}" class="card-center-bg">
                <p class="card-center-txt">满1400<b>减200元</b></p>
                <p class="card-center-txt2">购机时预存200元话费可享受此优惠</p>
                <a href="javascript:;" id="show_${cp.price}" class="quan-win">
                    <img  class="red-quan" src="${ctxStatic}/images/campusmark/200-red.png" alt="">
                    <p>${cp.goodsSn}</p>
                </a>
                </a>
            </c:if>

    </c:forEach>


    <div class="warm-info">
        <p>温馨提示：</p>
        <p>1.至营业厅购机时需出示此页面；<br>
            2.本次活动仅限1995年及之后出生的客户
            参与（购机时请出示身份证）</p>
    </div>


    <div class="common-flex card-center-flex">
        <a class="common-btn-big2" id="" href="${ctx}campusmark/shareToFriends?quan=${quan}"><img src="${ctxStatic}/images/campusmark/btn-sharefirnd2.png" alt=""></a>

    </div>

    <%--<a href="javascript:;" class="quan-win">--%>
        <%--<img  class="red-quan" src="${ctxStatic}/images/campusmark/50-red.png" alt="">--%>
        <%--<p>C201808171512345</p>--%>
    <%--</a>--%>


</div>
<div class="mask"></div>

<script>
    $(function(){


        $("#quan_50").click(function(){
            $('.mask').show();
            $('#show_50').show();
        })

        $("#quan_100").click(function(){
            $('.mask').show();
            $('#show_100').show();
        })

        $("#quan_200").click(function(){
            $('.mask').show();
            $('#show_200').show();
        })

        if($('#isLogin').val()==1){
            showAlert('请登录后查看我的券');
        }

//        $('.card-center-bg').click(function(){
////            $('.mask,.quan-win').show();
//                $('.mask').show();
//                $('#quan_100').show();
//            })
//        }


        $('.mask').click(function(){
            $('.mask,.quan-win').hide();
        })
    })


</script>
</body>


</html>