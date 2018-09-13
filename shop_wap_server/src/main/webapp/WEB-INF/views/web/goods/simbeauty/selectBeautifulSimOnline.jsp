<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/1/17
  Time: 17:54
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>首页</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-5.0.0.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/pure-grids.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/css/wap.css"/>
    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">

    <!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
    <%--插码相关--%>
    <%--<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>--%>
    <script type="text/javascript">
        var ctx = ${ctx};
    </script>
</head>
<body>
<%--保存用户的选择信息--%>
<input type="hidden" name="cityCode">
<input type="hidden" name="section">
<input type="hidden" name="guaranteeFee">

<form method="post" action="showPlanInfo" id="showPlanInfo">
    <input type="hidden" name="chooseNum">
    <input type="hidden" name="preFee">
    <input type="hidden" name="guaranteedFee">
</form>

<div class="container">
    <div class="yxhm-ggtu"><img src="${ctxStatic}/images/beauty-sim/750x210.png"/></div>
    <div class="yxhm-select">
        <ul class="yxhm-list">
            <li>
                <a href="javascript:void(0)" class="city">地市选择</a>
            </li>
            <li>
                <a href="javascript:void(0)" class="grade">预存档次</a>
            </li>
            <li>
                <a href="javascript:void(0)" class="consum">最低消费</a>
            </li>
        </ul>
        <!--地市选择 start-->
        <div class="city-con yxhm-mask" style="display:none">
            <ul class="city-list select01">
                <li class="active" data-value="全部">全部</li>
                <li data-value="长沙" value="0731">长沙</li>
                <li data-value="株洲" value="0733">株洲</li>
                <li data-value="湘潭" value="0732">湘潭</li>
                <li data-value="衡阳" value="0734">衡阳</li>
                <li data-value="娄底" value="0738">娄底</li>
                <li data-value="郴州" value="0735">郴州</li>
                <li data-value="怀化" value="0745">怀化</li>
                <li data-value="常德" value="0736">常德</li>
                <li data-value="邵阳" value="0739">邵阳</li>
                <li data-value="岳阳" value="0739">岳阳</li>
                <li data-value="张家界" value="0744">张家界</li>
                <li data-value="益阳" value="0737">益阳</li>
                <li data-value="永州" value="0746">永州</li>
                <li data-value="湘西土家族苗族自治州" value="0743">湘西土家族苗族自治州</li>
            </ul>
        </div>
        <!--地市选择 end-->
        <!--预存档次 start-->
        <div class="grade-con yxhm-mask" style="display:none">
            <div class="grade-box">
                <div class="grade-form">
                    <div class="grade-form-box"><input id="minPreFee" type="text" name="minPreFee" class="form-box-input" placeholder="最低价"/></div>
                    <div class="grade-line"></div>
                    <div class="grade-form-box"><input id="maxPreFee" type="text" name="maxPreFee" class="form-box-input" placeholder="最高价"/></div>
                </div>
                <ul class="grade-list change">
                    <li class="active" value="1">
                        50元/300MB
                        <span>10%选择</span>
                    </li>
                    <li  value="2">
                        50元/300MB
                        <span>60%选择</span>
                    </li>
                    <li  value="3">
                        50元/300MB
                        <span>30%选择</span>
                    </li>
                </ul>
            </div>
        </div>
        <!--预存档次 end-->
        <!--最低消费 start-->
        <div class="consum-con yxhm-mask" style="display:none">
            <ul class="consum-list change">
                <li class="active">保底100元</li>
                <li>保底50元</li>
                <li>保底300元</li>
                <li>保底400元</li>
                <li>保底500元</li>
            </ul>
        </div>
        <!--最低消费 end-->
    </div>
    <!--搜索 start-->
    <div class="yxhm-search">
        <div class="yxhm-search-box">
            <input type="text" placeholder=" 请至少输入两位数字" id="phone" class="form-box-input"/>
        </div>
        <a href="javascript:void(0)" onclick="searchBeautifulNum()" class="yxhm-btn yxhm-btn-blue" id="searchBtn">搜索</a>
    </div>
    <!--搜索 end-->
    <!--号码列表 start-->
    <ul class="yxhm-number-list change pure-g" id="phonePool">
        <li class="active pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number" id="phone">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
        <li class="pure-u-1-2">
            <a href="javascript:void(0)">
                <span class="yxhm-number">18812344321</span>
                <p class="yxhm-txt">预存XX元 保底消费XX元/月</p>
            </a>
        </li>
    </ul>
    <!--号码列表 end-->
    <div class="yxhm-change"><a onclick="searchBeautifulNum()" class="yxhm-btn yxhm-btn-blue">换一批号码</a></div>
</div>
<%--向下滑动时固定栏目--%>
<script type="text/javascript" src="${ctxStatic}/ap/lib/stickUp.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/beautifulSim/beauComm.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/beautifulSim/selectBeautifulSimOnline.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript">
    jQuery(function ($) {
        $(document).ready(function () {
            $('.yxhm-select').stickUp();
        });
    });
</script>
</body>
</html>
