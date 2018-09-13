<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
    <%@include file="/WEB-INF/views/include/taglib.jsp"%>
    <script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp" %>
    <!-- css -->
    <link href="${ctxStatic}/kill/lib/normalize-6.0.0.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/marketing/cf/wap.css" rel="stylesheet" type="text/css"/>
    <%--sdc9.js--%>
    <!-- js：jquery/insdc_w2.js在head.jsp中有；modal.js在message.jsp中有 -->
    <script type="text/javascript" src="${ctxStatic}/cut/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/cut/js/comm.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>

    <script type="text/javascript" >
        var ctx="${ctx}",gUrl="${gUrl}",sUrl="${sUrl}",tfsUrl="${tfsUrl}";
    </script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <title>众筹下单</title>
</head>

<body>
<div class="container">
    <form id="toPayForm"  action="${ctx}/marketing/cf/toPay" method="post">
        <input type="hidden" name="singlePrice" value="${cfGoodsConf.singlePrice}"/>
        <input type="hidden" name="cfGoodsId" value="${cfGoodsConf.cfGoodsId}"/>
        <input type="hidden" name="maxAllowNum" id="maxAllowNum" value="${maxAllowNum}"/><!-- 允许的最大份数 -->
        <input type="hidden" name="prizeId" value="${prizeInf.prizeId}"/>
    <div class="sub-con">
        <!--流程步骤 start-->
        <ul class="bar-step">
            <li class="active">1.提交订单<em></em><i></i></li>
            <li>2.选择支付方式<em></em><i></i></li>
            <li>3.支付成功</li>
        </ul>
        <!--流程步骤 end-->
        <ul class="sub-amount">
            <li>
                <label>金额</label>
                <div class="amount-list">¥<span class="f40 font-blue w200" id="buyNumRmb">${cfGoodsConf.singlePrice/100}</span>元</div>
            </li>
            <li>
                <label>数量</label>
                <div class="amount-list">
                    <input type="button" value="-" class="min"/>
                    <!-- 购买的份数 -->
                    <input class="text_box form-box" name="buyNumber" type="text" value="1"/>
                    <input type="button" value="+" class="add"/>
                    <span class="f24 amout-sm">（您最多能购买${maxAllowNum}份）</span>
                </div>
                <p class="font-gray9 f24 pl80 pt10">购买数量越多，中奖机会越大哦！</p>
            </li>
        </ul>
        <div class="sub-btn">
            <%--<a href="javascript:void(0)" class="btn btn50 btn-blue _goPay">立即支付</a>--%>
            <button class="btn btn50 btn-blue _goPay">立即支付</button>
            <a href="javascript:void(0)" id="_goPayHid" data-toggle="modal" data-target="#myModal"></a>
        </div>
        <!--活动规则 start-->
        <div class="rule-con">
            <p class="font-blue">活动规则：</p>
            <p>1、活动时间：<fmt:formatDate value="${cfGoodsConf.startTime}" pattern="yyyy年MM月dd日"/>-<fmt:formatDate
                    value="${cfGoodsConf.endTime}" pattern="yyyy年MM月dd日"/>单轮众筹时间以${cfGoodsConf.cycleDay}天为期限。</p>
            <p>
                2、每支付${cfGoodsConf.singlePrice/100}元，即可获得一个“幸运号码”（非手机号码），幸运号码设置为10000+X，X为客户支付成功顺位数。每位客户最多购买${cfGoodsConf.singleTotalBuyNumber}份众筹，即最多支付${cfGoodsConf.singleTotalBuyNumber*cfGoodsConf.singlePrice/100}元。支付金额越多，获得的幸运号码越多，中奖概率越大。本活动仅支持和包支付方式付款（不可使用电子券）；</p>
            <p>3、幸运号码产生规则：</p>
            <p>a.每轮众筹成功后（即众筹满${cfGoodsConf.price/100}元），将公示截止该时间点本站全部商品的最后100个参与时间（取本省商城数据）；</p>
            <p>b.将这100个时间的数值进行求和得出数值A（每个时间按时、分、秒、毫秒的顺序组合，如15:23:45:342则为152345342），将数字A除以众筹金额${cfGoodsConf.price/100}得到的余数，此余数+10000得到最终幸运号码（非手机号码），拥有该幸运号码者，直接获得众筹商品。</p>
            <p>如15:23:45:342则为152345342），将数字A除以众筹金额6800得到的余数，此余数+10000得到最终幸运号码（非手机号码），拥有该幸运号码者，直接获得手机。</p>
            <p>4、幸运号码产生后后，将以短信方式告知用户中奖信息，并在活动页面进行公告。10086将于客户中奖后3个工作日内联系中奖用户收货地址信息，并联系终端公司发货。</p>
            <p>用户30日之内未确认收货信息则视为自动放弃本次中奖机会；</p>
            <p>5、若在${cfGoodsConf.cycleDay}天内本轮筹金额未达到${cfGoodsConf.price/100}元，则本轮众筹失败，并开启新一轮众筹。失败轮的众筹资金将在活动结束后7个工作日内，按照原支付路径退还;</p>
            <p>6、若客户订单金额大于当期众筹金额，或客户支付后出现延迟到账，导致客户无法正常获得抽奖号，则该客户该次支付的众筹金额将在7个工作日内按原支付路径退还。</p>
        </div>
        <!--活动规则 end-->
    </div>
    </form>
</div>
<!-- 输出金额不合法提示 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
        <div class="modal-con">
            <div class="modal-faile">
                <p>抱歉，您输入的支持金额</p>
                <p>大于所剩的金额份数。请重新输入。</p>
            </div>
            <div class="modal-btn">
                <a href="javascript:void(0)" data-dismiss="modal" class="btn btn35 btn-white">我知道了</a></a>
            </div>
        </div>
    </div>
</div>
<!--弹框 end-->
</body>
<script type="text/javascript">
    $(function () {
        var msg = '${message}';
        if(msg.length != 0){
            $('.modal-faile').html('<p>'+msg+'</p>');
            $("._goPay").removeClass('btn-blue').addClass('btn-gray').attr("disabled", "true");
            $('#_goPayHid').click();

            //$("#getValiCode").removeAttr("disabled");// 启用按钮

        }

        $('._goPay').on("click", function () {
            //== 提交表单
            var singlePrice = parseInt($('input[name="singlePrice"]').val());//单份价格
            var maxAllowNum = parseInt($('input[name="maxAllowNum"]').val());
            var userBuyNum = parseInt($('input[name="buyNumber"]').val());
            //金额联动
            if(!/\D/.test(userBuyNum)){
                if (maxAllowNum < userBuyNum) {
                    $('.modal-faile').html('<p>抱歉，您输入的支持金额</p> <p>大于所剩的金额份数。请重新输入。</p>');
                    $('#_goPayHid').click();
                    return false;
                }
            }else{
                $('.modal-faile').html('请输入大于0的数值!</p>');
                $('#_goPayHid').click();
                return false;
            }
            $('#toPayForm').submit();
        });

        $('input[name="buyNumber"]').on("change", function () {
            var singlePrice = parseInt($('input[name="singlePrice"]').val());//单份价格
            var maxAllowNum = parseInt($('input[name="maxAllowNum"]').val());
            var userBuyNum = parseInt($('input[name="buyNumber"]').val());
            var thisVal = $(this).val();//输入数值
            //金额联动
            if(!/\D/.test(thisVal)){
                if (maxAllowNum < userBuyNum) {
                    $('.modal-faile').html('<p>抱歉，您输入的支持金额</p> <p>大于所剩的金额份数。请重新输入。</p>');
                    $('#_goPayHid').click();
                    return false;
                }
                $('#buyNumRmb').text(parseInt(thisVal) * singlePrice/100);
            }else{
                $('.modal-faile').html('请输入大于0的数值!</p>');
                $('#_goPayHid').click();            }
        });
        //加减按钮绑定事件
        $(".add").click(function() {
            var singlePrice = parseInt($('input[name="singlePrice"]').val());//单份价格
            var t = $(this).parent().find('input[class*=text_box]');
            if(parseInt(t.val())+1>$("#maxAllowNum").val()){
                $('#_goPayHid').click();
                return;
            }
            t.val(parseInt(t.val()) + 1);
            $('#buyNumRmb').text(parseInt( t.val()) * singlePrice/100);
        });
        $(".min").click(function() {
            var singlePrice = parseInt($('input[name="singlePrice"]').val());//单份价格
            var t = $(this).parent().find('input[class*=text_box]');
            t.val(parseInt(t.val()) - 1);
            if (parseInt(t.val()) < 0) {
                t.val(0);
            }
            $('#buyNumRmb').text(parseInt( t.val()) * singlePrice/100);
        });
    });
</script>

</html>