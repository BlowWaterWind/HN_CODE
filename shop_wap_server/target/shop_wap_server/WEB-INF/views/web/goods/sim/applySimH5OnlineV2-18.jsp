<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--页面说明：O2O集团在线售卡--%>
<html lang="zh-CN">

<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关：test--%>
    <meta name="WT.si_n" content="">
    <meta name="WT.si_x" content="">
    <meta name="WT.si_s" content="">
    <META name="WT.ac" content="">

    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">

    <title>选号中心</title>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/mescroll/mescroll.min.css"/>
    <link href="${ctxStatic}/css/sim/simgroup/o2o.sim.group.css?v=1" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>


    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>

    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/jquery.select.min.js"></script>


    <script type="text/javascript">
        var ctx = "${ctx}";
        var confId = "${conf.confId}";
    </script>


    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>

</head>

<body class="bg-white">
<div class="container">
    <div class="common-navbar-wrap">
        <div class="common-navbar ">
            <div class="navbar-left">
                <a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
            </div>
            <h3 class="navbar-title">选号中心</h3>
            <div class="navbar-right">
                <a onclick="shareGroupSim()" class="head-share">分享</a>
            </div>
        </div>
    </div>

    <div class="common-content">
        <div class="common-content-fixed">
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide">
                        <img src="${myTfsUrl}${conf.slctedImg}" alt="">
                    </div>
                    <div class="swiper-slide">
                        <img src="${myTfsUrl}${conf.slctedNoImg}" alt="">
                    </div>

                </div>
                <!-- 如果需要分页器 -->
                <div class="swiper-pagination"></div>
            </div>
            <div class="index-4f">
                <a href="#">
                    <img src="${ctxStatic}/images/index-card.png" alt="">
                    <p>号卡激活</p>
                </a>
                <a href="#">
                    <img src="${ctxStatic}/images/index-sub.png" alt="">
                    <p>副卡绑定</p>
                </a>
                <a href="">
                    <img src="${ctxStatic}/images/index-fav.png" alt="">
                    <p>我的收藏</p>
                </a>
                <a href="#">
                    <img src="${ctxStatic}/images/index-order.png" alt="">
                    <p>我的订单</p>
                </a>
            </div>

            <img class="index-lianhao" src="${ctxStatic}/images/15.jpg" alt="">
            <div class="Chnumber flex-h mt20 mb20 ">
                <p class="index-f-w">1</p>

                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>

                <p class="index-f-w">-</p>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <p class="index-f-w">-</p>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <li class="li_ipt"><input type="tel" maxlength="1" class="input-f-index trinput"></li>
                <a class="btn btn-middle btn-border btn-border-blue" onclick="getNumByPosition()">按位筛选</a>
                <a class="btn btn-middle btn-lingtblue" onclick="clearPosition()">清空</a>


            </div>
            <div class="ChoseNum">
                <div class="clearfix">
                    <a class="btn btn-middle btn-lingtblue  w-30 pull-right  mr20" onclick="anotherNumber()">换一批</a>
                    <ul class="Choseder">
                        <li class="Chodertitle">
                            <h2>按号段
                                <em class="li_arrow"></em>
                            </h2>
                        </li>
                    </ul>
                </div>
                <div class="ChoseList hide">
                    <div class="Listshow hide" style="display: none;">
                        <div class="ListAddr ListBrand clearfix">
                            <div class="ListAll">
                                <div class="otherTit unlimited clearfix">
                                    <a href="javascript:adValue('');" class="curr" val="" otitle="不限"
                                       otype="button">不限</a>
                                    <a href="javascript:adValue('134');" id="numPeriodABn_134" class=""
                                       val="134">134</a>
                                    <a href="javascript:adValue('135');" id="numPeriodABn_135" class=""
                                       val="135">135</a>
                                    <a href="javascript:adValue('136');" id="numPeriodABn_136" class=""
                                       val="136">136</a>
                                    <a href="javascript:adValue('137');" id="numPeriodABn_137" class=""
                                       val="137">137</a>
                                    <a href="javascript:adValue('138');" id="numPeriodABn_138" class=""
                                       val="138">138</a>
                                    <a href="javascript:adValue('139');" id="numPeriodABn_139" class=""
                                       val="139">139</a>
                                    <a href="javascript:adValue('150');" id="numPeriodABn_150" class=""
                                       val="150">150</a>
                                    <a href="javascript:adValue('151');" id="numPeriodABn_151" class=""
                                       val="151">151</a>
                                    <a href="javascript:adValue('152');" id="numPeriodABn_152" class=""
                                       val="152">152</a>
                                    <a href="javascript:adValue('157');" id="numPeriodABn_157" class=""
                                       val="157">157</a>
                                    <a href="javascript:adValue('158');" id="numPeriodABn_158" class=""
                                       val="158">158</a>
                                    <a href="javascript:adValue('159');" id="numPeriodABn_159" class=""
                                       val="159">159</a>
                                    <a href="javascript:adValue('182');" id="numPeriodABn_182" class=""
                                       val="182">182</a>
                                    <a href="javascript:adValue('183');" id="numPeriodABn_183" class=""
                                       val="183">183</a>
                                    <a href="javascript:adValue('188');" id="numPeriodABn_188" class=""
                                       val="188">188</a>
                                </div>
                            </div>
                        </div>
                        <div class="boxbg"></div>
                    </div>

                </div>
            </div>
        </div>
        <div id="mescroll" class="mescroll">
            <ul id="newsList" class="content list-phone">
            </ul>
        </div>
    </div>
</div>
<form id="applyForm" action="${ctx}simWholenet/toConfrim" method="post">
    <input type="hidden" name="confId" value="${conf.confId}"/>
    <input type="hidden" name="recmdCode" id="recmdCode" value="${cond.recmdCode}"/><%--推荐码--%>
    <input type="hidden" name="planId"/>
    <input type="hidden" name="planName"/>
    <input type="hidden" name="CHANID" value="${CHANID}"/>
    <input type="hidden" name="channelCityCode" value="${cityCode}" id="cityCode">
    <input type="hidden" name="departId" value="${departId}">
</form>


<%--下面的用于微信分享--%>
<input type="hidden" value="/res/img/${conf.titleImg}" id="titleImg">
<input type="hidden" value="${conf.cardTypeName}" id="cardTypeName">
<input type="hidden" value="${conf.planDesc}" id="planDesc">
</body>


<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<%--插码相关--%>
<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
<script type="text/javascript" src="${ctxStatic}/ap/js/jquery_cookie.js?v=6"></script>

<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=62536"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/simgroup/collectNum.js?v=1"></script>

<script type="text/javascript" src="${ctxStatic}/js/swiper.min.js"></script>

<%--微信分享--%>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShare.js?v=201805"></script>


<script type="text/javascript">
    var idx = null;
    var imgUrl = "/shop/static/images/simpay/pay_" + '${conf.cardType}' + ".png";
    var departId = "${departId}";//作为选号的部门ID
    var haoDuan = "";
    var position = ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'];//按位筛选拼接字符串
    var Page = {
        init: function () {
            this.Platform.init();
            this.Buttons.init();
            this.InputBoxEvents.init();

        },
        InputBoxEvents: {
            init: function () {
                this.whenClick();
                this.whenFocus();
                this.whenBlur();
                this.whenKeyUp();
            },
            whenClick: function () {
                $(".Chnumber").on("click", ".trinput", function () {
                    $(this).addClass("active");
                    $(".trinput:not(.active)").addClass("hidden");
                });
            },
            whenFocus: function () {
                $(".Chnumber").on("focus", ".trinput", function () {
                    idx = $(".trinput").index(this);
                    console.log(idx);
                    if (Page.Platform.type == "IOS") {
                        $(this).val("");
                        position[idx] = '_';
                    }
                    $(this).select();
                });
            },
            whenBlur: function () {
                $(".Chnumber").on("blur", ".trinput", function () {
                    //向表单中填充值
                    console.log("blur" + idx);
                });
            },
            whenKeyUp: function () {
                $(".Chnumber").on("keyup", ".trinput", function () {
                    //fill value
                    var val = $.trim($(this).val().replace(/\D/g, ""));
                    $(this).val(val);
                    if (val == "") {
                        position[idx] = '_';
                        return;
                    }
                    position[idx] = val;
                    //光标移向下一个input
                    if (idx == 10) {
                        return;
                    } else {//在和掌柜中不会自动移向下个输入框,
                        $(".Chnumber").children("li").eq(++idx).children().focus();
                    }


                });
            }
        },
        Buttons: {
            init: function () {
                this.btnClear();
            },
            btnClear: function () {
                $(".Numclear").on("click", function () {
                    $(".fainput").text("");//进入页面时,所有的
                    $(".trinput").val("");
                });
            }
        },
        Platform: {
            type: null,
            init: function () {
                if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) { //判断iPhone|iPad|iPod|iOS
                    this.type = "IOS";
                } else if (/(Android)/i.test(navigator.userAgent)) {  //判断Android
                    this.type = "Android";
                } else { //pc
                    this.type = "PC";
                }
            }
        }
    };

    Page.init();


    $(function () {
        var clientType = isAppClient();
        if (clientType == "hezhanggui") {
            $(".common-navbar-wrap").show();
        } else if (clientType == "html5" || clientType == "android" || clientType == "ios") {
            //去除头和分享(电脑/安卓/ios浏览器打开不分享但是显示头)
            $(".navbar-right").remove();
        } else {
            //微信打开调用微信的分享,并且不显示头
            $(".common-navbar-wrap").remove();
            $("#mescroll").css('top', '7.9rem')
        }

        if ($("#psptId").val() != '') {
            $("#psptId").next().show();
        }
        /*给页面WT.si_n 赋初始值插码*/
        getConfId("${conf.confId}","${conf.confId}");

        new Swiper('.swiper-container', {
            autoplay: true,
            pagination: {
                el: '.swiper-pagination'
            },
        });
        $.ajax({
            url: ctx + 'simWholenet/getH5FastEntrance',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if (data.code == '0') {
                    var html = '';
                    for (var i = 0; i < data.data.length; i++) {
                        html += '<a href="' + data.data[i].confUrl + '"><img src="${myTfsUrl}' + data.data[i].confImg + '"><p>' + data.data[i].confName + '</p></a>';
                    }
                    $(".index-4f").html(html);
                }
            }
        });

        $('.common-select a').click(function () {
            $(this).addClass('active').siblings().removeClass('active');
        });
        //进入页面调用一次选号接口
        searchNumber();
    });

    function anotherNumber() {
        if (haoDuan == '') {
            searchNumber("", "", "");
        } else {
            searchNumber(1, haoDuan, "");
        }
    }

    function getNumByPosition() {
        console.log("按位筛选" + 1 + position.join(""));
        searchNumber("", "", 1 + position.join(""));
    }

    function clearPosition() {
        $(".Chnumber li input").val("");
        position = ['_', '_', '_', '_', '_', '_', '_', '_', '_', '_'];
    }
</script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/share.js?v=6"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/simgroup/choNumber.js?v=1"></script>

</html>