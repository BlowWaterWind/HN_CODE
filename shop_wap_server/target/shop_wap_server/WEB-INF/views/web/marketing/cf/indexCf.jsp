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
    <title>众筹首页</title>

</head>

<body>
<div class="container">
    <input type="hidden" name="status" id="status" value="${cfGoodsConf.status}"/>
    <div class="header-top">
        <a href="javascript:void(0)" id="share" class="btn-org">分享</a>
        <dl>
            <dt><img src="${tfsUrl}/${cfGoodsConf.imgPath}"/></dt>
            <dd>
                <p class="pb10">${cfGoodsConf.goodsTitle}</p>
                <p class="f40 font-gray3 header-txt">众筹${cfGoodsConf.goodsName}只需${cfGoodsConf.singlePrice/100}元起 </p>
            </dd>
        </dl>
        <div class="dz-con">
            <span class="hand" id="like" rel="like"></span>
            <span class="f20">(<b id="likeCount">${cfGoodsConf.popularity ==null ? 0:cfGoodsConf.popularity}</b>)</span>
        </div>
    </div>
    <div class="main">
        <form id="toBuyForm" action="${ctx}/marketing/cf/toBuy" method="post">
            <input type="hidden" name="cfGoodsId" id="cfGoodsId" value="${cfGoodsConf.cfGoodsId}"/>
            <input type="hidden" name="buyPeriod" value="${curPeriod}"/>
            <input type="hidden" name="prizeId" value="${prizeInfId}"/>
            <input type="hidden" name="" value="${cfGoodsConf.popularity}"/>
        <div class="zc-title">${cfGoodsConf.goodsName},${cfGoodsConf.goodsTitle}<span
                class="font-blue">&gt;&gt;&gt;&gt;</span></div>
        <div class="zc-con">
            <div class="zc-list">
                <label>
                    <input type="radio" name="buyType" class="radio" value="0" onchange="changeBuyType(this)" checked="checked" />
                    <div class="zc-txt">
                        <p><span class="zc-tip btn-org"><b
                                class="pr20">¥${cfGoodsConf.singlePrice/100}元</b>抽奖档</span><span><b
                                class="f40 font-org">${orderResult.parPeoNum}</b>位参与者</span>
                        </p>
                        <p class="pt10 pl20">
                            正在进行第<span class="font-blue">${curPeriod}</span>轮众筹。
                            <c:if test="${cfGoodsConf.sucPeriod ne null}">第${orderResult.cfGoodsConf.sucPeriod}轮众筹已成功。</c:if>
                        </p>
                    </div>
                    <div class="gory-con">
                        <ul class="usage-list">
                            <li>
                                <div class="usage-title">
                                    <p>
                                        <span class="f32">¥${orderResult.curPeriodRmb/100}元</span>
                                        <span>已筹款</span>
                                    </p>
                                    <p>
                                        <span class="f32">¥${cfGoodsConf.singlePrice*cfGoodsConf.totalNumber/100}元</span>
                                        <span>目标金额</span>
                                    </p>
                                </div>
                                <div class="us-con">
                                    <div class="us-bar" style="width:<fmt:formatNumber
                                            value="${orderResult.curPeriodRmb*100/(cfGoodsConf.singlePrice*cfGoodsConf.totalNumber)}"
                                            pattern="#.##"/>%"></div>
                                </div>
                                <div class="us-txt">
                                    <p>本轮已完成<span class="font-blue f40">
                                    <fmt:formatNumber value="${orderResult.curPeriodRmb*100/(cfGoodsConf.singlePrice*cfGoodsConf.totalNumber)}"
                                                      pattern="#.##"/>%
                                </span></p>
                                    <a href="javascript:void(0)" class="usage-link font-blue _myLuckNum" data-toggle="modal" data-target="#myLuckNumDiv">查看我的幸运码&gt;</a>
                                    <%--<a href="javascript:void(0)" id="_myLuckNumHid" style="display:none;" data-toggle="modal"--%>
                                       <%--data-target="#myLuckNumDiv"></a>--%>
                                </div>
                            </li>
                        </ul>
                        <div class="usage-txt">
                            <p>每轮众筹累计金额达到目标<span class="font-blue">${cfGoodsConf.singlePrice*cfGoodsConf.totalNumber/100}元</span>，则将抽取一位幸运用户，将获得
                                <span class="font-blue">${cfGoodsConf.goodsName}</span>手机一台。
                            </p>
                            <p></p>
                            <p>不满足目标金额时，该轮众筹失败，众筹资金按原路径退回。</p>
                            <p>共<span class="font-blue">${cfGoodsConf.maxPeriod}台</span>手机，已抽<span
                                    class="font-blue">${sucTs}</span>台，剩余<span
                                    class="font-blue">${cfGoodsConf.maxPeriod - sucTs}</span>台。</p>
                            <a href="javascript:void(0)" class="usage-link font-blue" data-toggle="modal"
                               data-target="#myModal">抽奖规则&gt;</a>
                        </div>
                    </div>
                </label>
            </div>
            <div class="zc-list pt36">
                <label>
                    <input type="radio" name="buyType" class="radio" value="1" onclick="changeBuyType(this)"/>
                    <div class="zc-txt">
                        <p><span class="zc-tip btn-org"><b class="pr20">¥${cfGoodsConf.price/100}元</b>购买档</span></p>
                    </div>
                    <div class="hd-txt">
                        <p class="font-blue f40">${cfGoodsConf.goodsName}手机一台</p>
                        <p class="pt10">点击前往购买即可，${cfGoodsConf.price/100}元优惠购${cfGoodsConf.goodsName}。</p>
                    </div>
                </label>
            </div>
        </div>
            <!--待支付订单 start-->
            <c:if test="${not empty watiPayOrders}">
            <div class="pay-con">
                <span class="zc-tip btn-org">${cfGoodsConf.singlePrice/100}元抽奖档</span>
                <ul class="pay-order">
                    <c:forEach items="${watiPayOrders}" var="order">
                    <li>
                        <div class="pay-list">
                            <p class="font-gray3">${order.goodsName}</p>
                            <p>金额：<span class="font-red">¥<fmt:formatNumber value="${order.payTotalSum/100}" pattern="#.##"/></span></p>
                            <p>订单号：${order.cfOrderId}</p>
                            <p><fmt:formatDate value="${order.buyTime}" pattern="yyyy-MM-dd HH:mm:ss.SSS"/></p>
                        </div>
                        <a href="${ctx}/marketing/cf/toPay?cfOrderId=${order.cfOrderId}" class="btn btn-blue">立即支付</a>
                    </li>
                    </c:forEach>
                </ul>
            </div>
            </c:if>
            <!--待支付订单 end-->
        <!--酬金展示 start-->
        <div class="cj-con">
            <!--参与人数 start-->
            <div class="part-number distance01">
                <p class="font-blue">${orderResult.allPeoNum}</p>
                <p class="f24 font-gray9">参与人数</p>
            </div>
            <!--参与人数 end-->
            <!--已筹金额 start-->
            <div class="part-number distance02">
                <p class="font-blue f40">${orderResult.allPeriodRmb/100}元</p>
                <p class="f24 font-gray9">已筹金额</p>
            </div>
            <!--已筹金额 end-->
            <!--剩余天数 start-->
            <div class="part-number distance03">
                <p class="font-blue">${orderResult.remainDays}</p>
                <p class="f24 font-gray9">剩余天数</p>
            </div>
            <!--剩余天数 end-->
        </div>
        <!--酬金展示 end-->
        <!--中奖公告 start-->
        <div class="prize-info">
            <div class="prize-title">中奖公告</div>
            <ul class="prize-list">
                <c:forEach items="${prizeInfs}" var="pis">
                    <c:if test="${pis.isOpenPrize eq '1'}">
                        <li>
                            <p>第${pis.period}轮：</p>
                            <p>恭喜 “抽奖号”为${pis.prizeNo}的用户</p>
                            <p>（${pis.prizePhoneNum})获得${pis.goodsName}一台。</p>
                            <a  href="${ctx}/marketing/cf/initCalDetail?prizeId=${pis.prizeId}&cfGoodsId=${cfGoodsConf.cfGoodsId}">查看计算详情&gt;</a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>
        <!--中奖公告 end-->
        <div class="ftu-con">
            <a href="javascript:void(0)"><img src="${ctxStatic}/images/marketing/cf/demoimages/ftu.png"/></a>
        </div>


        <div class="ft-btn">
            <button class="btn-org" id="toBuyBtn" onclick="goSubmit('toBuyForm')">前往购买</button>
            <a href="javascript:void(0)" id="_goBuyHid" data-toggle="modal" data-target="#myModal1" style="display:none;"></a>

        <%--<a href="javascript:void(0)" class="btn-org" onclick="goSubmit('toBuyForm')" >前往购买</a>--%>
        </div>
        </form>
    </div>
</div>


<!-- 查看规则 弹框-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
        <div class="modal-con">
            <h4>幸运号码产生规则</h4>
            <div class="modal-txt">
                <p>
                    截止该轮众筹成功时间，商城所有商品的最后100条交易时间（时、分、秒、毫秒）记录之和，除以众筹金额${cfGoodsConf.singlePrice*cfGoodsConf.totalNumber/100}元，所得余数再加10001即为最终中奖号码。若客户本轮众筹的“幸运号（幸运号为按支付时间顺序的编号）”与最终中奖号码完全一致，则可以获得本轮众筹的大奖${cfGoodsConf.goodsName}一台。</p>
                <p>例如：</p>
                <div class="modal-cale">
                    <p>计算结果= (12349440 % <fmt:parseNumber integerOnly="true" value="${cfGoodsConf.totalNumber*cfGoodsConf.singlePrice/100}" />) + 10001 = <fmt:parseNumber integerOnly="true" value="${12349440%(cfGoodsConf.totalNumber*cfGoodsConf.singlePrice)/100+10001}" /></p>
                    <p class="moadl-small">
                        <span>最后100条商品购买时间之和</span><span>取余</span><span>众筹金额</span><span>固定值</span><span>中奖号码</span></p>
                </div>
                <p>则若客户该轮众筹的幸运号为10001，则可获得${cfGoodsConf.goodsName}一台。</p>
            </div>
            <div class="modal-btn">
                <a href="javascript:void(0)" data-dismiss="modal" class="btn btn35 btn-white">我知道了</a></a>
            </div>
        </div>
    </div>
</div>

<!-- 查看我的幸运号码 弹框 -->
<div class="modal fade modal-prompt" id="myLuckNumDiv">
    <div class="modal-dialog">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
        <div class="modal-con">
            <ul class="modal-list" id="myLuckNum">

            </ul>
            <div class="modal-btn">
                <a href="javascript:void(0)" data-dismiss="modal" class="btn btn35 btn-white">我知道了</a></a>
            </div>
        </div>
    </div>
</div>

<!-- 输出金额不合法提示 start-->
<div class="modal fade modal-prompt" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
        <div class="modal-con">
            <div class="modal-faile">
                <p></p>
            </div>
            <div class="modal-btn">
                <a href="javascript:void(0)" data-dismiss="modal" class="btn btn35 btn-white">我知道了</a></a>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    var msg = '${message}';
    $('.hand').on("click", function () {
        var A = $(this).attr("id");
        var B = A.split("like");
        var messageID = B[1];
        var C = parseInt($("#likeCount" + messageID).html());
        var D = $(this).attr("rel");
        if (D === 'like') {
            $("#likeCount" + messageID).html(C + 1);
            $(this).addClass("heartAnimation").attr("rel", "unlike");
            commitLike('like');
        } else {
            $("#likeCount" + messageID).html(C - 1);
            $(this).removeClass("heartAnimation").attr("rel", "like");
            commitLike('unlike');
        }
    });

    function goSubmit(id){
        //如果选择的是众筹档，只有状态为进行中才能前往购买
        var buyType=$("input[name='buyType']:checked").val();
        var status=$("#status").val();
        if('0'==buyType && '1'!=status){
            $('.modal-faile').html('<p>众筹档暂未上架！</p>');
            $('#_goBuyHid').click();
            return;
        }
        $('#'+id).submit();
    }

    function changeBuyType(obj){
        var buyType=$(obj).val();
        var status=$("#status").val();
        if('0'==buyType ){
            if( '1'!=status || msg.length != 0){
                $("#toBuyBtn").removeClass('btn-org').addClass('btn-gray').attr("disabled", "true");
                return;
            }
        }
        $("#toBuyBtn").removeClass('btn-gray').addClass('btn-org').removeAttr("disabled");
    }

    function share(title, shareLink, shareText) {
        var uAgent = navigator.userAgent;
        // 安卓
        if (uAgent.indexOf('Android') > -1 || uAgent.indexOf('Adr') > -1) {
            window.MobileWalletShare.startShare(title, shareLink, shareText);
        }
        // ios
        else if (!!uAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
            window.location.href = "cyberapp://IOSShare#IOSShareTitle=" + title +"#IOSShareURL=" + shareLink +"#IOSShareDetails=" + shareText;
        }
    }

    function commitLike(flag){
        var cfGoodsId=$("#cfGoodsId").val();
        $.ajax({
            type: "POST",
            dataType: "json",
            async: false,
            data : {cfGoodsId : cfGoodsId,flag:flag},
            url: ctx+"/marketing/cf/like",
            success: function (data) {
            },
            error: function (data) {
            }
        });
    }
    $(function () {
        /**
         * 提示语监控
         */
        if(msg.length != 0){
            $('.modal-faile').html('<p>'+msg+'</p>');
            $("#toBuyBtn").removeClass('btn-org').addClass('btn-gray').attr("disabled", "true");
            $('#_goBuyHid').click();
        }
        /**
         * 查看 我的幸运号码 弹窗
         */
        $('._myLuckNum').on('click', function () {
            var cfGoodsId=$("#cfGoodsId").val();
            $.ajax({
                type: "POST",
                dataType: "json",
                async: false,
                data : {cfGoodsId : cfGoodsId},
                url: ctx+"/marketing/cf/lookNo",
                success: function (data) {
//                    var rs = $.parseJSON(data).resp;
                    var rs=data.resp;
                    if (rs.code == "0000" || rs.code=='0') {
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
                    if (data.status == 401) {
                        $('#myLuckNum').html('<p><span class="mr20">请先登录</span></p>');
                    } else
                        $('#myLuckNum').html('<p><span class="mr20">网络错误</span></p>');
                }
            });
        });

        $("#share").on("click", function() {
            var title = "1元众筹";
            var shareContext = "我正在和包客户端参与1元起众筹${cfGoodsConf.goodsName}…离众筹成功只差你的一臂之力~";
            var url = "http://wap.hn.10086.cn/shop/marketing/cf/initCfIndex?cfGoodsId=${cfGoodsConf.cfGoodsId}";
            share(title, url, shareContext);
        });
    });
</script>
</html>