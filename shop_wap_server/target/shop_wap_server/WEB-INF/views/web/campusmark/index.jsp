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
    <title>校园购机节</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
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

    <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>


<body>
<div class="container">
    <a class="index-rule" href="javascript:;"><span>活动规则</span></a>
    <img src="${ctxStatic}/images/campusmark/buy-phpne_01.jpg" alt="">
    <img src="${ctxStatic}/images/campusmark/buy-phpne_02.jpg" alt="">
    <img src="${ctxStatic}/images/campusmark/buy-phpne_03.jpg" alt="">
    <div class="buy-phone">
        <a class="my-lott" href="javascript:;"></a>
        <a class="phone-more" href="javascript:;"></a>
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
    <div  class="rule-win common-win popWin">
        <div class="common-win-head">活动规则</div>
        <div class="common-win-body">
            <img class="act-txt" src="${ctxStatic}/images/campusmark/act-txt.png" alt="">
            <div class="rule-txt-bg">

                <p class="rule-txt-title">一、活动时间：</p>
                <p>2018年8月21日-10月31日</p>
                <p class="rule-txt-title">二、活动内容：</p>
                <p>购机节期间内购买指定16款手机，享直降优惠。</p>
                <table class="common-table">
                    <tr>
                        <th>套餐档次</th>
                        <th>手机售价</th>
                        <th>预存话费<br>送直降券</th>
                    </tr>
                    <tr>
                        <td class="td-nowrap">38元及以上</td>
                        <td class="td-nowrap">满500元</td>
                        <td>预存50元得<br>50元直降</td>
                    </tr>
                    <tr>
                        <td class="td-nowrap">48元及以上</td>
                        <td class="td-nowrap">满700元</td>
                        <td>预存100元得<br>100元直降</td>
                    </tr>
                    <tr>
                        <td class="td-nowrap">78元及以上</td>
                        <td class="td-nowrap">满1400元</td>
                        <td>预存200元得<br>200元直降</td>
                    </tr>
                </table>
                <p class="rule-txt-title">三、活动规则：</p>
                <p>1.享受购机直降必须预存同等金额话费，存一得二（如存50元，得100元、50元直降+50元自由话费）。<br>
                    2.本次活动仅限1995年及之后出生的客户参与。<br>
                    3. 线下购机时需出示身份证及直降券领取页面或截图。</p>
                <p class="rule-txt-title">四、直降券使用规则：</p>
                <p>1、直降券有效期为2018年8月21日-X月X日。
                    2、每个手机号最多可领3张，购机时一个手机号仅限使用一张。<br>
                    3、购买手机的订单发生退货或退款时，赠送的直降券将会失效。<br>
                    4、直降券金额仅限购买活动指定手机，不可提现、不可开具发票。</p>
            </div>

        </div>
        <div class="common-win-foot"><a class="BtnZd" href="javascript:;"><img class="btn-zd" src="${ctxStatic}/images/campusmark/btn-zd.png" alt=""></a></div>

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

    //弹出注册框
    function renderToReg(goodsId) {
        $('.getQuan,.signIn').hide();
        $('#emailReg').show();
        //window.location.href = ${ctx}+'campusmark/showDetailEmail?goodsId='+goodsId;
    }

    $(function(){

        $('.phone-more').click(function(){
            $('.mask,.buy-phone-w').show();
        })
        $('.btn-buy-phone').click(function(){
            $('.mask,.buy-phone-w').hide();
        })

        $('.BtnZd').click(function(){
            $('.mask,.rule-win').hide();
        })
        $('.index-rule').click(function(){
            $('.mask,.rule-win').show();
        })
        $('.mask').click(function(){
            $('.mask,.popWin').hide();
        })
        $('.buy-phone-w li a').click(function(){
//            $('.mask,.buy-phone-w').hide();
//            window.location.href = "indexShow.html";
        })


        $('.my-lott').click(function(){
            $.ajax({
                type:'post',
                contentType: 'application/json',
                url:${ctx}+'campusmark/isLoginIndex',
                data:JSON.stringify({"isLogin":"false"}),
                success:function(data)
                {
                    if(data.isLogin == "0"){// 已经登录
                        window.location.href = ${ctx}+'campusmark/toMyCoupons';
                    }else{//未登录，跳转到登录页面
                        $('.mask,.signPhone').show();
                    }
                },
                dataType:'json'
            });
        })


    });
    
    function showDetail(goodsId) {
        $('.mask,.buy-phone-w').hide();
        window.location.href = ${ctx}+'campusmark/showDetail?goodsId='+goodsId;
    }
</script>
</body>


</html>