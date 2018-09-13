<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/5/18
  Time: 15:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>支付页面</title>
    <link href="${ctxStatic}/css/sim/wap-simh5pay.css" rel="stylesheet">
</head>

<body>
<!-- start container -->
<div class="container bg-gray">
    <div class="topbar">
        <a href="javascript:history.back(-1);" class="icon icon-return">返回</a>
        <a href="javascript:history.back()" class="icon icon-close">关闭</a>
        <h3>订单支付</h3>
    </div>
    <!-- end topbar -->
    <div class="cells mb-24">
        <div class="cell">
            <div class="cell-top">
                <h3>订单信息</h3>
            </div>
            <ul class="cell-list">
                <li>
                    <div class="pay-info">
                        <div class="pay-info-thumb">
                            <img src="${ctxStatic}/images/simpay/pay_${conf.cardType}.png">
                        </div>
                        <div class="pay-info-bd">
                            <div>
                                <h3>${selectPhone}</h3>
                                <p>${conf.planDesc}</p>
                            </div>
                            <div>
                                <span>数量:x1</span>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <label>订单编号</label>
                    <p>${orderNos}</p>
                </li>
                <li>
                    <label>付款金额</label>
                    <p><span class="txt-red">￥${planRMB/100}</span></p>
                </li>
            </ul>
        </div>
        <%--无需选择支付通道--%>
        <div class="cell">
            <div class="cell-top">
                <h3>选择支付通道</h3>
            </div>
            <ul class="cell-list chose-pay">
                <li class="pay-list" value="4">
                    <div class="pay-list-thumb">
                        <img src="${ctxStatic}/images/beauty-sim/hebao.png" alt="和包支付">
                    </div>
                    <span>和包支付</span>
                </li>
                <li class="pay-list"  value="1">
                    <div class="pay-list-thumb">
                        <img src="${ctxStatic}/images/spdbsim/icon-zfb.png" alt="支付宝支付">
                    </div>
                    <span>支付宝支付</span>
                </li>
                <li class="pay-list"  value="2">
                    <div class="pay-list-thumb">
                        <img src="${ctxStatic}/images/spdbsim/wx_icon.png" alt="微信支付">
                    </div>
                    <span>微信支付</span>
                </li>
                <li class="pay-list"  value="3">
                    <div class="pay-list-thumb">
                        <img src="${ctxStatic}/images/spdbsim/i_unionpay.png" alt="银联支付">
                    </div>
                    <span>银联支付</span>
                </li>
                <li class="pay-list"  value="5">
                    <div class="pay-list-thumb">
                        <img src="${ctxStatic}/images/spdbsim/cashier.png" alt="银联支付">
                    </div>
                    <span>收银台支付</span>
                </li>
            </ul>
        </div>
    </div>
    <button id="submitBtn" type="button" class="tj-btn">确认支付￥${planRMB/100} 元</button>
</div>

    <input type="hidden" name="radiocode" id="radiocode">
    <input type="hidden" name="orderNos" id="orderNos" value="${orderNos}">
<%--支付信息模板注入--%>
<div id="subFormDivId">
    <script type="text/html" id="subFormTplId" >
        <form id="formid" method="post" action="{{requestPayUrl}}">
            <input type="hidden" name="callbackUrl" value="{{callbackUrl}}"/>
            <input type="hidden" name="notifyUrl" value="{{notifyUrl}}"/>
            <input type="hidden" name="merchantId" value="{{merchantId}}"/>
            <input type="hidden" name="type" value="{{type}}"/>
            <input type="hidden" name="version" value="{{version}}"/>
            <input type="hidden" name="channelId" value="{{channelId}}"/>
            <input type="hidden" name="payType" value="{{payType}}"/>
            <input type="hidden" name="period" value="{{period}}"/>
            <input type="hidden" name="periodUnit" value="{{periodUnit}}"/>
            <input type="hidden" name="characterSet" value="{{characterSet}}"/>
            <input type="hidden" name="currency" value="{{currency}}"/>
            <input type="hidden" name="amount" value="{{amount}}"/>
            <input type="hidden" name="orderDate" value="{{orderDate}}"/>
            <input type="hidden" name="merchantOrderId" value="{{merchantOrderId}}"/>
            <input type="hidden" name="OrderNo" value="{{OrderNo}}"/>
            <input type="hidden" name="merAcDate" value="{{merAcDate}}"/>
            <input type="hidden" name="productDesc" value="{{productDesc}}"/>
            <input type="hidden" name="productId" value="{{productId}}"/>
            <input type="hidden" name="productName" value="{{productName}}"/>
            <input type="hidden" name="reserved1" value="{{reserved1}}"/>
            <input type="hidden" name="reserved2" value="{{reserved2}}"/>
            <input type="hidden" name="payOrg" value="{{payOrg}}"/>
            <input type="hidden" name="authorizeMode" value="{{authorizeMode}}"/>
            <input type="hidden" name="mobile" value="{{mobile}}"/>
            <input type="hidden" name="Gift" value="{{Gift}}"/>
            <input type="hidden" name="MerActivityID" value="{{MerActivityID}}"/>
            <input type="hidden" name="PaymentLimit" value="{{PaymentLimit}}"/>
            <input type="hidden" name="ProductURL" value="{{ProductURL}}"/>
            <input type="hidden" name="IDType" value="{{IDType}}"/>
            <input type="hidden" name="IvrMobile" value="{{IvrMobile}}"/>
            <input type="hidden" name="ProductType" value="{{ProductType}}"/>
            <input type="hidden" name="hmac" value="{{hmac}}"/>
        </form>
    </script>
</div>

<%-- 公用弹窗 --%>
<div id="sorry-modal" class="mask-layer">
    <div class="modal small-modal">
        <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>


<!-- end container -->
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">
    $(function() {
        $('.chose-pay').find('.pay-list').bind('click', function() {
            var radiocode = $(this).attr('value');
            $("#radiocode").val(radiocode);
            $(this).addClass('active').siblings().removeClass('active');
        });
    });

    $("#submitBtn").click(function(){
        var radiocode = $("#radiocode").val();
        $.ajax({
            url:'payOrder',
            dataType:'json',
            type:'post',
            data:{orderNos:$("#orderNos").val(),radiocode:radiocode},
            success:function(data){
                if(data.code == '0'){
                    //注入订单信息发起请求
                    $("#subFormDivId").html(template("subFormTplId",data.data));
                    $("#formid").submit();
                }else if(data.code == '-1' || data.code == '-2'){
                    //-1选择平台错误 -2获取支付流水失败
                    toggleModal('sorry-modal',data.message);
                }else{
                    //支付失败
                    var url = ${ctx}+ '/afterFreeze?orderSubId='+data.data.orderSubId;
                    toggleModal('sorry-modal',data.message,'去解冻保证金',url);
                }
            },
            error:function(){
                toggleModal('sorry-modal','系统异常');
            }
        });
    })
</script>
</body>

</html>
