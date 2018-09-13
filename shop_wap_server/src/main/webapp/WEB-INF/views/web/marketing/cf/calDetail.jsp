<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
    <title>计算详情</title>
</head>
<body>
<div class="container">
    <div class="header-top">
        <dl>
            <dt><img src="${tfsUrl}/${cfGoodsConf.imgPath}" /></dt>
            <dd>
                <p class="pb10">${cfGoodsConf.goodsTitle}</p>
                <p class="f40 font-gray3 header-txt">众筹${cfGoodsConf.goodsName}只需${cfGoodsConf.singlePrice/100}元起</p>
            </dd>
        </dl>
        <div class="dz-con">
            <a href="javascript:void(0)" class="font-gray9">查看宝贝详情&gt;</a>
        </div>
    </div>
    <div class="main">
        <div class="zcjs-con">
            <div class="zcjs-list">
                <div class="zcjs-title">[第${prizeInf.period}轮众筹]揭晓结果：恭喜<span class="font-red">${prizeInf.prizePhoneNum}</span>获得${cfGoodsConf.goodsName}一台</div>
                <div class="zcjs-photo">
                    <div class="zcjs-tx">
                        <dl>
                            <dt>
                                <span class="start"></span>
                            <div class="tx-con">
                                <img src="${ctxStatic}/images/marketing/cf/tx.png" />
                                <p class="zj-txt">获奖者</p>
                            </div>
                            </dt>
                            <dd>
                                <p><span class="font-org f32">${prizeInf.prizePhoneNum}</span><a href="javascript:void(0)" id="allWinNo" class="f24 ml10 font-gray9" data-toggle="modal" data-target="#myModal">查看TA所有幸运码</a></p>
                                <div class="zcjs-txt">
                                    <div class="zcjs-lt">
                                        <p>Ta参与</p>
                                        <p class="f20 font-org"><fmt:parseNumber integerOnly="true" value="${myAllPayAmount/100}" />人次</p>
                                    </div>
                                    <div class="zcjs-rt">
                                        <p>众筹成功时间:<fmt:formatDate value="${prizeInf.prizeTime}" pattern="yyyy-MM-dd HH:mm:ss.SSS"/></p>
                                        <p>T A参与时间:<fmt:formatDate value="${myOrder.buyTime}" pattern="yyyy-MM-dd HH:mm:ss.SSS"/></p>
                                    </div>
                                </div>
                            </dd>
                        </dl>
                        <div class="js-resulte">
                            <div class="resulte-title">计算结果：</div>
                            <div class="resulte-con">
                                <p class="f36 font-fblue">(${prizeInf.prizeCalNumber}&nbsp;&nbsp;%&nbsp;&nbsp;<fmt:parseNumber integerOnly="true" value="${cfGoodsConf.totalNumber*cfGoodsConf.singlePrice/100}" />)+ 10001 = ${prizeInf.prizeNo}</p>
                                <p class="f20"><span>以下一百条时间之和</span><span>取余</span><span>众筹金额</span><span>固定值</span><span>幸运码</span></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--计算结果 start-->
            <div class="zcjs-list zjs-list">
                <span class="zcjs-tip">计算结果</span>
                <div class="zjs-con">
                    <div class="zjs-gz">
                        <p class="zjs-title">计算规则</p>
                        <div class="zjs-txt">
                            <p><span class="f36 font-fblue">幸运码</span>=<b class="font-fblue pr20">（</b></p>
                            <p class="f24">数值A<span>众筹金额</span></p>
                            <p><b class="font-fblue pl20">）</b><strong class="font-fblue f24">的余数</strong>+10001</p>
                        </div>
                    </div>
                    <ul class="sm-list">
                        <li>
                            <span class="broad">1</span>
                            <p>数值A为商城所有商品最后100条购买时间求和（时间按时、分、 秒、毫秒的顺序组合，如17：54：23.123，则为175423123）；</p>
                        </li>
                        <li>
                            <span class="broad">2</span>
                            <p>数值A除以该宝贝总需人次，取余数<span class="font-gray9">（余数：指整数除法中被除数 未被除尽部分，例如：13除以5，商数为2，余数为3）</span>；</p>
                        </li>
                        <li>
                            <span class="broad">3</span>
                            <p>余数加10001得到最终幸运码。</p>
                        </li>
                    </ul>
                </div>
            </div>
            <!--计算结果 end-->
            <div class="zcjs-list zsjs-date">
                <div class="zsjs-date-title">截止该轮众筹成功时间【<fmt:formatDate value="${prizeInf.prizeTime}" pattern="yyyy-MM-dd HH:mm:ss.SSS"/>】， 商城所有商品的最后100条购买时间记录</div>
                <div class="zsjs-date-con">
                    <table class="zsjs-tabs">
                        <thead>
                        <tr>
                            <th>时间</th>
                            <th>手机号</th>
                            <th>商品</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${cfOrderList}" var="order">
                        <tr>
                            <td><span class="date"><fmt:formatDate value="${order.buyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></td>
                            <td>${order.userPhone}</td>
                            <td><span class="mc-txt">${cfGoodsConf.goodsName}</span></td>
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!--尾部 start-->
        <div class="zcjs-foot">
            <dl>
                <dt>计算结果</dt>
                <dd>
                    <ul class="foot-list">
                        <li>
                            <span class="broad">1</span>
                            <p>求和：<span class="font-fblue">${prizeInf.prizeCalNumber}</span>（上面100条购买记录的取值相加）</p>
                        </li>
                        <li>
                            <span class="broad">2</span>
                            <p>求余：${prizeInf.prizeCalNumber} %<fmt:parseNumber integerOnly="true" value="${cfGoodsConf.totalNumber*cfGoodsConf.singlePrice/100}" />（众筹金额）=<span class="font-fblue"><fmt:parseNumber integerOnly="true" value="${prizeInf.prizeCalNumber%(cfGoodsConf.totalNumber*cfGoodsConf.singlePrice)/100}" /></span>（余数）</p>
                        </li>
                        <li>
                            <span class="broad">3</span>
                            <p><fmt:parseNumber integerOnly="true" value="${prizeInf.prizeCalNumber%(cfGoodsConf.totalNumber*cfGoodsConf.singlePrice)/100}" />(余数)+10001=<span class="font-fblue">${prizeInf.prizeNo}</span></p>
                        </li>
                    </ul>
                    <div class="ft-btn"><span>幸运码：${prizeInf.prizeNo}</span></div>
                </dd>
            </dl>
        </div>
        <!--尾部 end-->
    </div>
</div>
<!--弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
        <div class="modal-con">
            <p class="pb20">${prizeInf.prizePhoneNum}总共参与<fmt:parseNumber integerOnly="true" value="${myAllPayAmount/100}" />人次：</p>
            <ul class="modal-list">
                <c:forEach items="${myOrderList}" var ="myOrder">
                <li>
                    <p><span class="mr20"><fmt:formatDate value="${myOrder.buyTime}" pattern="yyyy-MM-dd HH:mm:ss.SSS"/></span>第${prizeInf.period}轮众筹：</p>
                    <p class="f32">${myOrder.winNumber}</p>
                </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<!--弹框 end-->
</body>
<script type="text/javascript">
    $(function () {
    $('#allWinNo').on('click', function () {
        var userId=$().val();
        $.ajax({
            type: "POST",
            dataType: "json",
            data : {userId : userId},
            async: false,
            url: ctx+"/marketing/cf/lookNo",
            success: function (data) {
                var rs = $.parseJSON(data).resp;
                if (rs.code == "0000") {
                    var data1 = $.parseJSON(rs.data);
                    if (data1 == null || data1 == '') {
                        $('#myLuckNum').html('<p><span class="mr20">您还没有该商品的众筹订单。</span></p>');
                    } else {
                        $('#myLuckNum').empty();
                        $('#myLuckNum').append('<li><p>恭喜你获得以下幸运号：</p>');
                        $.each(data1, function (i, item) {
                            $('#myLuckNum').append('<p><span class="mr20">'+item.buyTime+'</span>第' +
                                    + item.buyPeriod + '轮众筹：</p><p class="f32">' + item.winNumber + '</p>');
                        });
                        $('#myLuckNum').append('</li>');
                        $('#_myLuckNumHid').click();
                    }
                } else {
                    $('#myLuckNum').html('<p><span class="mr20">'+rs.message+'</span></p>');
                }
            },
            error: function (data) {
                    $('#myLuckNum').html('<p><span class="mr20">网络错误</span></p>');
            }
        });
    });
    });

</script>
</html>
