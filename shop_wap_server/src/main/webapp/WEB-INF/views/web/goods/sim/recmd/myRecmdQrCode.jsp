<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width"/>
    <meta name="viewport" content="initial-scale=1.0,user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <META name="WT.si_n" content="WZXD">
    <META name="WT.si_x" content="21">
    <Meta name="WT.ac" content="">
    <title>在线售卡H5-我要推荐</title>
    <link href="${ctxStatic}/css/sim/css.css?v=9" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/sim/base.css?v=1" rel="stylesheet" />
    <link href="${ctxStatic}/css/sim/wap-simh5onlineV2.css?v=2" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/clipboard.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=4"></script>
    <script type="text/javascript">
        var ctx = ${ctx};
    </script>
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>

</head>
<body>

    <div class="box">
        <div class="content">
            <input type="hidden" value="${orderRecmd.recmdUserLink}" id="recmdUserLink">
            <input type="hidden"  value="${orderRecmd.recmdPhone}" id="recmdPhone">
            <input type="hidden" value="${h5SimConf.cardTypeName}" id="cardTypeName">
            <input type="hidden" value="${h5SimConf.planDesc}" id="planDesc">
            <input type="hidden" value="/res/img/${h5SimConf.titleImg}" id="titleImg">
            <img class="title3 w1" src="${ctxStatic}/css/sim/images/title3.png"/>
            <p class="p2"><a href="${ctx}recmd/getMoreDetail">了解王者行动&gt;&gt;</a></p>
            <c:if test="${true==weixin}">
            <div class="wdtj">
                <c:set var="confId" value="${h5SimConf.confId}"></c:set>
                <div class="car_box" id="cat_box1" >
                    <a href="#" class="colors" id="draw1"
                       cardTypeName="${h5SimConf0.cardTypeName}" titleImg="${h5SimConf0.titleImg}"
                       recmdUserLink="${orderRecmd0.recmdUserLink}" qrcode="${orderRecmd0.recmdUserQrcode}"
                       planDesc="${h5SimConf0.planDesc}">
                        <img src="${ctxStatic}/css/sim/images/yddwk.png"></a><a href="#" class="draw">
                    <span class="text2">了解资费详情&gt;&gt;</span></a>
                </div>
                <div class="car_box" id="cat_box2" >
                    <a href="#" class="" id="draw2" cardTypeName="${h5SimConf1.cardTypeName}"  titleImg="${h5SimConf1.titleImg}"
                       recmdUserLink="${orderRecmd1.recmdUserLink}" qrcode="${orderRecmd1.recmdUserQrcode}"
                       planDesc="${h5SimConf1.planDesc}">
                        <img src="${ctxStatic}/css/sim/images/xhjt.png"></a><a href="#" class="draw3">
                    <span class="text2">了解资费详情&gt;&gt;</span></a>
                </div>
            </div>
            </c:if>

            <div class="wdzs">
                <div class="ewm_box">
                  <img src="${myTfsUrl}${orderRecmd.recmdUserQrcode}" class="qr-img" >
                </div>

                <div class="but_box">
                    <div id="mcover" onClick="document.getElementById('mcover').style.display='';"><img src="${ctxStatic}/css/sim/images/arrow02.png"></div>
                    <div class="btn2"><a href="#" id="share" title="" onclick="javascript:share();" ><img src="${ctxStatic}/css/sim/images/button04.png"/></a></div>
                </div>
            </div>

            <a href="${ctx}/recmd/myInvitationRecord"><img class="button2" src="${ctxStatic}/css/sim/images/button02.png"  style="margin-left:100px;" ></a>
        </div>
    </div>


        <!-- 弹出层 -->
        <div class="hide-body">
            <div class="close-window">
                <!-- 关闭窗口，也就是触发关闭div的事件-->
                <a href="javascript:;" onClick="colose('0')" title="关闭" class="close"><img src="${ctxStatic}/css/sim/images/close.png" alt=""></a>
            </div>
            <!-- 中间主体显示div 可以增加其他的样式-->
            <div class="tswz">
                <div class="title1"><img src="${ctxStatic}/css/sim/images/l_title5.png"></div>
                <div class="table1"><img src="${ctxStatic}/css/sim/images/table2.gif"></div>
            </div>
            <div class="clear"></div>
            <!-- 底部的div -->

        </div>
        <div class="hide-body3">
            <div class="close-window">
                <!-- 关闭窗口，也就是触发关闭div的事件-->
                <a href="javascript:;" onClick="colose3('0')" title="关闭" class="close"><img src="${ctxStatic}/css/sim/images/close.png" alt=""></a>

            </div>
            <!-- 中间主体显示div 可以增加其他的样式-->
            <div class="tswz">
                <div class="title1"><img src="${ctxStatic}/css/sim/images/l_title4.png"></div>
                <div class="table1"><img src="${ctxStatic}/css/sim/images/table1.gif"></div>
            </div>
            <div class="clear"></div>
            <!-- 底部的div -->
        </div>


        <div class="body-color"></div>
        <script>
            <%--${h5SimConf.confId}--%>
            jQuery(document).ready(function($) {

                $('.draw').click(function(){ //jquery的点击事件
                    var mas=1;
                    $('.body-color').fadeIn(100);//全局变得黑的效果，具体的div就是theme-popover-mask这个
                    $('.hide-body').slideDown(200);//将隐藏的窗口div显示出来

                })
            })
            function colose(){
                // alert(i);
                $('.body-color').css("display","none");
                $('.hide-body').css("display","none");//将显示的窗口隐藏起来
            }
        </script>
    <script>
        $('.button2').click(function(){
            try{
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.event","WZXD_CKJL"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.event","WZXD_CKJL",true);
                    }
                }

            }catch (e){
                console.log(e);
            }
        });
        jQuery(document).ready(function($) {

            $('.draw3').click(function(){ //jquery的点击事件
                var mas=1;
                $('.body-color').fadeIn(100);//全局变得黑的效果，具体的div就是theme-popover-mask这个
                $('.hide-body3').slideDown(200);//将隐藏的窗口div显示出来

            })

            $('#draw1').click(function(){
                try{
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.event","WZXD_DWK"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.event","WZXD_DWK",true);
                        }
                    }
                }catch (e){
                    console.log(e);
                }
                $('#draw1').attr('class','colors');
                $('#draw2').attr('class','');
                $(".qr-img").attr('src','${myTfsUrl}'+$(this).attr("qrcode"));
                $("#recmdUserLink").val($(this).attr("recmdUserLink"));
                $("#cardTypeName").val($(this).attr("cardTypeName"));
                $("#titleImg").val("/res/img/"+$(this).attr("titleImg"));
                $("#planDesc").val($(this).attr("planDesc"));
                wxReady();
            })
            $('#draw2').click(function(){
                try{
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.event","WZXD_XHJT"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.event","WZXD_ XHJT",true);
                        }
                    }

                }catch (e){
                    console.log(e);
                }
                $('#draw1').attr('class','');
                $('#draw2').attr('class','colors');
                $(".qr-img").attr('src','${myTfsUrl}'+$(this).attr("qrcode"));
                $("#recmdUserLink").val($(this).attr("recmdUserLink"));
                $("#cardTypeName").val($(this).attr("cardTypeName"));
                $("#titleImg").val("/res/img/"+$(this).attr("titleImg"));
                $("#planDesc").val($(this).attr("planDesc"));
                wxReady();
            })

            $("#draw1").click();
            if($.cookie("WT.ac")!=undefined) {
                document.getElementsByTagName('meta')['WT.ac'].content = $.cookie("WT.ac");
            }

        });
        function colose3(){
            // alert(i);
            $('.body-color').css("display","none");
            $('.hide-body3').css("display","none");//将显示的窗口隐藏起来
        }

    </script>

    <!-- start share-modal -->
    <div id="share-modal" class="mask-layer">
        <img src="${ctxStatic}/css/sim/images/arrow02.png">
    <%--</div>--%>




<%-- 公用弹窗 --%>
        <%-- 公用弹窗 --%>
        <div id="sorry-modal" class="mask-layer">
            <div class="modal small-modal" style="width:18rem">
                <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')"></a>
                <div class="modal-content">
                    <%--<div class="pt-30"></div>--%>
                    <p class="text-center _pomptTxt"></p>
                </div>
                <div class="btn-group" style="font-size:1.0rem">
                    <a href="javascript:void(0)" class="confirm-btn">确认</a>
                </div>
            </div>
        </div>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShare.js?v=201805"></script>
</body>
</html>