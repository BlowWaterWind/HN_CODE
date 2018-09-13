<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.ai.ecs.order.entity.TfOrderSub" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <META name="WT.si_n" content="">
    <META name="WT.si_x" content="22">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <META name="WT.ac" content="">
    <title>大王卡办理成功</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/swiper.css">
    <link href="${ctxStatic}/css/sim/wap-sim-newbusi.css" rel="stylesheet">
    <% TfOrderSub orderSub = (TfOrderSub) session.getAttribute("ORDER");%>
    <style type="text/css">
        #msgTxt {
            background-color: #fff;
            padding: 60px;
            position: absolute;
            top: 50%;
            left: 50%;
            border-radius: .106667rem;
            transform: translate(-50%,-50%);
        }
        .cloud{
            text-align: center;
            width: 250px;
        }
        .cloud p{
            font-size: 14px;
            font-weight: bolder;
            height: 60px;
            line-height: 60px;
            text-align: center;
        }
        .to_btn{
            width:100px;
            height:45px;
            font-size: 20px;
        }
    </style>
</head>

<body>
    <!-- start container -->
    <div class="container">
        <form id="formSubmit" action="" method="post">
            <%--<input type="hidden" value="${order.orderSubId}"/>--%>
            <input id ="confId" name="confId" type="hidden" value="${confId}"/>
            <input id="recmdCode" name="recmdCode" type="hidden" value="${recmdCode}"/>
            <input id="cityCodeSuffix" name="cityCodeSuffix" type="hidden" value="${cityCodeSuffix}">
        </form>
        <!-- start 办理成功-->
        <div class="success-modal">
            <div class="timecount"><span class="timecount-txt">倒计时</span><span id="minute">10</span><span>:</span><span id="second">00</span></div>
            <div class="success-modal-hd">
                <h3>您已成功订购号卡！</h3>
                <p>请关注“湖南移动微厅”查询更多详情</p>
                <span>（可在“我的订单”中查看详情）</span>
            </div>
            <div class="tips-flag">移动大王卡专属优惠 首月0元体验咪咕业务</div>
            <div class="check-list">
                <c:forEach items="${simBusiConfs}" var="conf">
                    <label class="check-label">
                        <div class="check-label-hd">
                            <img src="${ctxStatic}/images/simNewBusi/images/${conf.busiImg}">
                            <p>${conf.price}</p>
                            <h3>${conf.priceDesc}</h3>
                        </div>
                        <div class="check-label-bd">
                            <div>
                                <h3>${conf.busiName}</h3>
                                <p>${conf.busiDesc}</p>
                            </div>
                            <%--<a href="javascript:;" onclick="toggleModal('mgVideoModal')">点击查看详情》</a>--%>
                            <c:if test="${conf.confId eq 10001}">
                                <a href="javascript:;" onclick="toggleModal('mgVideoModal')">点击查看详情》</a>
                            </c:if>
                            <c:if test="${conf.confId eq 10002}">
                                <a href="javascript:;" onclick="toggleModal('mgReadModal')">点击查看详情》</a>
                            </c:if>
                            <c:if test="${conf.confId eq 10003}">
                                <a href="javascript:;" onclick="toggleModal('mgMusicModal')">点击查看详情》</a>
                            </c:if>
                        </div>
                        <div class="check-label-ft">
                            <input type="checkbox" class="check" name="checkbox1" value=${conf.confId} checked>
                            <i class="icon-checked"></i>
                        </div>
                    </label>
                </c:forEach>
            </div>
            <div class="Grid -center">
                <button id="sure-button" type="button" class="success-modal-btn" otype="button" otitle="大王卡新业务选择" >确定</button>
            </div>
        </div>
        <!-- end 办理成功-->
        <!-- start 咪咕音乐弹窗弹窗 -->
        <div id="mgMusicModal" class="mask-layer">
            <div class="modal mg-modal mg-music">
                <a href="javascript:;" class="modal-close-01" onclick="toggleModal('mgMusicModal')"></a>
                <div class="mg-txt">
                    <div class="number-cir number-cir-pink">1</div>
                    <p><strong>咪咕彩铃权益包，</strong><b>8元/月</b>。涵盖了彩铃功能与彩铃包月两项业务，开通彩铃权益包之后，彩铃、振铃免费无限量下载，点击下载咪咕音乐APP在彩铃专区DIY彩铃不限量，炫酷彩铃想换就换！</p>
                </div>
                <div class="mg-txt">
                    <div class="number-cir number-cir-pink">2</div>
                    <p><strong>优惠政策 </strong>订购期间，首次开通第一个月免费，第二、三个月送8元话费，每个月再赠送500M省内通用流量，连续赠送3个月。每月还可在咪咕音乐APP领取1G咪咕音乐定向流量。</p>
                </div>
                <div class="mg-qrcode">
                    <img src="${ctxStatic}/images/simNewBusi/images/mg-music-qrcode.png">
                    <p>首次扫码下载咪咕音乐APP次月赠送100M省内通用流量</p>
                </div>
            </div>
        </div>
        <!-- end 咪咕音乐弹窗弹窗 -->
        <!-- start 咪咕视频弹窗弹窗 -->
        <div id="mgVideoModal" class="mask-layer">
            <div class="modal mg-modal mg-video">
                <a href="javascript:;" class="modal-close-01" onclick="toggleModal('mgVideoModal')"></a>
                <div class="mg-txt">
                    <div class="number-cir number-cir-yellow">1</div>
                    <p><strong>咪咕视频钻石会员，</strong><b>15元/月</b>。全球影视资源库VIP会员免费观看，院线大片抢先看，美国大片、欧洲、亚洲等经典影片，还有电视剧、热门综艺、记录片、演唱会、体育直播等内容。VIP会员可使用7天超长直播回看！专享TV端2K/4K高品质画质、4屏环绕立体声音效。定期推出会员专属活动，电影周边好礼，明星亲笔签名照、电影场景物品，VIP会员专享。</p>
                </div>
                <div class="mg-txt">
                    <div class="number-cir number-cir-yellow">2</div>
                    <p><strong>优惠政策 </strong>订购期间，首次开通第一个月免费，第二个月送15元话费，每月获赠5G咪咕视频定向流量，连续赠送6个月。</p>
                </div>
                <div class="mg-qrcode">
                    <img src="${ctxStatic}/images/simNewBusi/images/mg-video-qrcode.png">
                    <p>首次扫码下载咪咕视频APP次月赠送200M省内通用流量</p>
                </div>
            </div>
        </div>
        <!-- end 咪咕视频弹窗弹窗 -->
        <!-- start 咪咕阅读弹窗 -->
        <div id="mgReadModal" class="mask-layer">
            <div class="modal mg-modal mg-read">
                <a href="javascript:;" class="modal-close-01" onclick="toggleModal('mgReadModal')"></a>
                <div class="mg-txt">
                    <div class="number-cir number-cir-blue">1</div>
                    <p><strong>咪咕阅读至尊全站升级包，</strong><b>10元/月</b>。全站50万图书、漫画、杂志内容（不含境外原版分类内容），尊享1.4倍成长值加速，并可获得额外成长等级，咪咕阅读APP内全站阅读页无广告，全站包会员尊享VIP身份点亮，将在会员首页、个人中心、图书评论等页面展示，福利会员参加“悦读咖”活动可享名家优先签名特权。</p>
                </div>
                <div class="mg-txt">
                    <div class="number-cir number-cir-blue">2</div>
                    <p><strong>优惠政策 </strong>订购期间，首次开通第一个月免费，第二个月送10元话费，连续赠送6个月的300M/月省内通用流量，每月还可在咪咕阅读APP领取1G咪咕阅读定向流量。</p>
                </div>
                <div class="mg-qrcode">
                    <img src="${ctxStatic}/images/simNewBusi/images/mg-read-qrcode.png">
                    <p>首次扫码下载咪咕阅读APP次月赠送200M省内通用流量</p>
                </div>
            </div>
        </div>
        <!-- end 咪咕阅读弹窗 -->
        <!--start 办理提示弹框-->
        <div id="mgMessage" class="mask-layer" >
            <a href="javascript:;" onclick="toggleModal('mgMessage')"></a>
            <div id="msgTxt">
                <div class="cloud">
                    <p></p>
                    <button  class="success-modal-btn to_btn" type="button">确定</button>
                </div>
            </div>
        </div>
        <input type="hidden" id="tempString" name="tempString"/>
        <!--end 办理提示弹框-->
    </div>

    <script type="text/javascript" src="${ctxStatic}/js/busi/lib/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/busi/lib/jquery-2.2.4.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/busi/lib/swiper.jquery.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/busi/lib/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <!-- 倒计时10分钟 -->
    <script type="text/javascript">
        try{
            /*给页面WT.si_n 赋值*/
            getConfId3("2002");
            /**
             * 获取cookie中的参数
             */
            var wtAc = $.cookie("WT.ac");
            if($.cookie("WT.ac")!=undefined) {
                document.getElementsByTagName('meta')['WT.ac'].content = wtAc;
            }
        }catch (e){
            console.log(e);
        }
    var minute = 9;
    var second = 59;
    var count = setInterval(function() {
        second--;
        if (second == 00 && minute == 00) {
           clearInterval(count);
        };
        if (second == 00 && minute != 00) {
            second = 59;
            if (minute < 10) minute = "0" + minute;
            minute--;
        };

        if (second < 10) second = "0" + second;
        $("#minute").text(minute);
        $("#second").text(second);
    }, 1000);
    </script>
    <!-- 倒计时10分钟 -->
    <!--点击确定按钮，提交数据-->
    <script type="text/javascript">
        $(function () {
            var confId =$("#confId").val();
            var recmdCode = $("#recmdCode").val();
            var cityCodeSuffix = $("#cityCodeSuffix").val()
            $("#sure-button").click(function () {
                var checkBox = "";
                $("input[name='checkbox1']:checked").each(function (i) {
//                    checkBox[i] =$(this).val();
                    checkBox = checkBox+ ","+$(this).val();
                })
                $("#tempString").val(checkBox.substr(1,checkBox.length));
                var str = $("#tempString").val();
                //将参数传递到后台
                $.ajax({
                    type:"POST",
                    url:"${ctx}simBuy/simBusiOnline",
                    dataType:"json",
                    data:{busi:str,confId:confId,recmdCode:recmdCode,cityCodeSuffix:cityCodeSuffix},
                    error:function (request){
                        alert("请求错误！");
                    },
                    success:function (data) {
//                        alert(data.message+","+data.confId+","+data.recmdCode);
                        //根据返回信息判断跳转办理页面还是弹窗
                        if("success" === data.message){
                            window.location.href="${ctx}/simBuy/simH5OnlineToApply?confId="+data.confId+"&recmdCode="+data.recmdCode;
                            <%--window.location.href="${ctx}/simBuy/simH5OnlineToApply?confId="+data.confId+"&recmdCode=34354";测试--%>
                        }else{
                            $("#msgTxt p").html(data.message);
                            toggleModal('mgMessage');
                        }
                    }
                })
            })
            //弹窗点击确定跳转到办理页
            $(".to_btn").click(function () {
                try{
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n"," HK_2002 ",
                                "WT.si_x","22"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n"," HK_2002",true,
                                    "WT.si_x","22",true);
                        }
                    }
                }catch (e){
                    console.log(e);
                }
                window.location.href ="${ctx}/simBuy/simH5OnlineToApply?confId=${confId}&recmdCode=${recmdCode}";
                <%--window.location.href ="${ctx}/simBuy/simH5OnlineToApply?confId=${confId}&recmdCode=343434";测试--%>
            })
        })
    </script>
</body>

</html>