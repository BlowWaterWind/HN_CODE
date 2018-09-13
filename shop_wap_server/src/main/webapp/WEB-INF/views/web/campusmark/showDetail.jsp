<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>湖南移动商城</title>
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <meta name="WT.si_n" content="GJJ" />
    <meta name="WT.si_x" content="20" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/swiper.min.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/campusmark/wap.css" />
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/swiper.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/loginForm.js?v=1024"></script>
    <script type="text/javascript" src="${ctxStatic}/js/campusmark/registerForm.js?v=1024"></script>
    <script  type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/securityencode.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>


<body class="bg-white">
<input type="hidden" id="goodsId" name="goodsId" value="${goods.goodsId}"/>
<input type="hidden" id="quan" name="quan" value="${goods.quan}"/>
<input type="hidden" value='${CSRFToken}' id='csrftoken' name="CSRFToken">
<div class="container">
    <img class="img-logo" src="${ctxStatic}/images/campusmark/logo.png" alt="">
    <div class="phone-price">
        <p><b><c:out value="${goods.price-goods.quan}"/></b>元</p>
    </div>
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <div class="swiper-slide"><img src="${myTfsUrl}${goods.imgName}" alt=""></div>
            <div class="swiper-slide"><img src="${myTfsUrl}${goods.imgName}" alt=""></div>

        </div>
        <!-- Add Pagination -->
        <div class="swiper-pagination"></div>
    </div>
    <div class="box-flex">
        <div>
            <p class="box-show-txt">开学价：<span>${goods.price}</span>元</p>
            <p class="box-show-txt">官网价：<span>${goods.marketPrice}</span>元</p>
        </div>
        <div class="count-ren">
            <p>目前搞“机”人数</p>
            <p class="count-ren-txt">共<span>${goods.buyNum}</span>人</p>

        </div>
    </div>
    <div class="box-gray">
        <div class="box-gray-content">
            <div class="phone-info">
                <p class="font-purple">${goods.screenSize}</p>
                <p class="font-purple2">${goods.phoneMemory}</p>
                <p class="font-purple3">${goods.pixel}</p>
                <p class="font-purple">${goods.runMemory}</p>
                <p class="font-purple2">${goods.cpu}</p>
                <p class="font-purple3">${goods.comeTime}</p>
            </div>

        </div>
    </div>

    <div class="common-flex">
        <a class="common-btn" id="btnGetQuan" href="javascript:;"><img src="${ctxStatic}/images/campusmark/btn-lingq.png" alt=""></a>
        <a class="common-btn" id="btnMList" href="javascript:;"><img src="${ctxStatic}/images/campusmark/btn-xuan.png" alt=""></a>
    </div>
    <div class="buy-phone-w popWin">
        <ul>
            <c:forEach items="${mobileList}" var="mobile" varStatus="ugsStatus">
                <c:choose>
                    <c:when test="${mobile.recommended == 0}">
                        <li><a  href="javascript:showDetail(${mobile.goodsId});"><span class="hot-red">${mobile.goodsName}</span></a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a  href="javascript:showDetail(${mobile.goodsId});"><span >${mobile.goodsName}</span></a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ul>
        <a class="btn-buy-phone" href="javasctipt:;"></a>
    </div>

    <!-- 弹出框领券 -->
    <div  class=" common-win popWin getQuan" >
        <img class="img-signal" src="${ctxStatic}/images/campusmark/signal.png" alt="">
        <div class="common-win-head">萌新</div>
        <div class="common-win-body ">
            <p class="lingq-txt1">点我就有机会拿</p>
            <p class="lingq-txt2"><b>${goods.quan}</b>元购机直降券哦~</p>
            <p class="lingq-txt3">*温馨提示：选择机型后直降券才能到账哦。</p>

        </div>
        <div class="common-win-foot"><a class="BtnZd" href="javascript:;"><img id="btnDianWo" class="btn-zd" src="${ctxStatic}/images/campusmark/btn-dianwo.png" alt=""></a></div>

    </div>

    <!-- 用户名登录-弹出框 -->
    <div id="userLogin" class=" common-win popWin signPhone signIn" >
        <img class="img-gift" src="${ctxStatic}/images/campusmark/gift.png" alt="">
        <div class="common-win-head">为确保直降券到账，请填写用户名</div>
        <div class="common-win-body ">
            <div class="form-flex">
                <img class="icon-phone" src="${ctxStatic}/images/campusmark/icon-user.png" alt="">
                <input type="text" id="loginname" name="loginname" class="flex1 input-form" placeholder=" 输 入 用 户 名   ">
            </div>
            <div class="form-flex">
                <img class="icon-phone" src="${ctxStatic}/images/campusmark/icon-key.png" alt="">
                <input type="password" id="loginpass" name="loginpass"  class="flex1 input-form" placeholder=" 请  输 入 密 码 ">
            </div>
            <%--<div class="form-flex2">--%>
                <%--<input type="text" id="v_code" class="input-form3" placeholder=" 请  输 入 验 证 码  ">--%>
                <%--<canvas id="canvas" width="180" height="66"></canvas>--%>
            <%--</div>--%>
            <p class="sg-txt">还没有账户请先<a href="javascript:renderToReg($('#goodsId').val());">注册</a></p>
        </div>
        <div class="common-win-foot"><a class="BtnZd" id="submitBtn" href="javascript:"><img  class="btn-zd" src="${ctxStatic}/images/campusmark/btn-comfrom.png" alt=""></a></div>
    </div>

    <!-- 邮箱注册-弹出框 -->
    <div id="emailReg" class=" common-win popWin signIn" >
        <img class="img-gift" src="${ctxStatic}/images/campusmark/gift.png" alt="">
        <div class="common-win-head">为确保直降券到账，请先注册邮箱</div>
        <div class="common-win-body ">
            <div class="form-flex">
                <img class="icon-phone" src="${ctxStatic}/images/campusmark/icon-user.png" alt="">
                <input type="text" id="member_name" name="member_name"  class="flex1 input-form" placeholder=" 输 入 用 户 名   ">
            </div>
            <div class="form-flex">
                <img class="icon-phone" src="${ctxStatic}/images/campusmark/icon-mail.png" alt="">
                <input type="text" id="member_email" name="member_email" class="flex1 input-form" placeholder=" 请 输 入 邮 箱 ">
            </div>
            <div class="form-flex">
                <img class="icon-phone" src="${ctxStatic}/images/campusmark/icon-key.png" alt="">
                <input type="password" id="member_passwd"  name="member_passwd"  class="flex1 input-form" placeholder=" 请  输 入 密 码 ">
            </div>
            <div class="form-flex">
                <img class="icon-phone" src="${ctxStatic}/images/campusmark/icon-key.png" alt="">
                <input type="password" id="confirm_passwd" name="confirm_passwd"  class="flex1 input-form" placeholder="  请 再 次 输 入 密 码  ">
            </div>
            <div class="form-flex2">
                <input type="tel" id="captcha" name="captcha" class="input-form3" placeholder=" 请  输 入 验 证 码  ">
                <canvas id="canvas" width="180" height="66"></canvas>
            </div>


        </div>
        <div class="common-win-foot"><a class="BtnZd" id="submitRegBtn"  href="javascript:;"><img class="btn-zd" src="${ctxStatic}/images/campusmark/btn-sign.png" alt=""></a></div>
    </div>


    </div>
    <div class="mask"></div>

<script>
    var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
        + "wxyz0123456789+/" + "=";

    var loginByNameUrl="<%=request.getContextPath()%>/campusmark/loginByName";
    var submitUrl="<%=request.getContextPath()%>/campusmark/doRegister";
    var loginUrl="<%=request.getContextPath()%>/campusmark/showDetail?goodsId="+$("#goodsId").val();

    function getNum(a) {
        var num = $.trim(a);
        num = num.replace(/[\s]/g, "");
        return num;
    }

    function encode64(password) {
        return strEnc(password, keyStr);
    }



    function showDetail(goodsId) {
        $('.mask,.buy-phone-w').hide();
        window.location.href = ${ctx}+'campusmark/showDetail?goodsId='+goodsId;
    }

    //弹出注册框
    function renderToReg(goodsId) {
        $('.getQuan,.signIn').hide();
        $('#emailReg').show();
        //window.location.href = ${ctx}+'campusmark/showDetailEmail?goodsId='+goodsId;
    }
</script>

<script>

    var swiper = new Swiper('.swiper-container', {
        pagination: {
            el: '.swiper-pagination',
        },
    });
    $(function(){
        // $('.mask,.signIn').show();
        $('#btnGetQuan').click(function(){
            //$('.mask,.getQuan').show();
            $('.mask,.getQuan').show();
        })
        //"点我"判断是否已经登录
        $('#btnDianWo').click(function(){
            $('.getQuan').hide();
            $.ajax({
                type:'post',
                contentType: 'application/json',
                url:${ctx}+'campusmark/isLogin',
                data:JSON.stringify({"isLogin":"false","goodsId":$('#goodsId').val(),"quan":$('#quan').val()}),
                success:function(data)
                {
                    if(data.isLogin == "0"){// 已经登录
                        //showAlert('已登录');
                        $('.mask').hide();
                        if(data.result == "0"){
                            window.location.href = ${ctx}+'campusmark/toReceiveSuccess?quan='+$('#quan').val();
                        }else if(data.result == "1"){
                            showAlert('您已领取过此券!');
                        }else{
                            showAlert('领取失败，请重试!');
                        }
                    }else if(data.isLogin == "1"){//未登录，跳转到登录页面
                        //showAlert('未登录');
                        $('.mask,.signPhone').show();
                    }else{
                        $('.mask').hide();
                        showAlert('领取失败，请重试！');
                    }
                },
                dataType:'json'
            });
        })
        $('.btn-buy-phone').click(function(){
            $('.mask,.buy-phone-w').hide();
        })
        $('.index-rule').click(function(){
            $('.mask,.rule-win').show();
        })
        $('#btnMList').click(function(){
            $('.mask,.buy-phone-w').show();
        })
        $('.mask').click(function(){
            $('.mask,.popWin').hide();
        })

    });
</script>
</body>


</html>