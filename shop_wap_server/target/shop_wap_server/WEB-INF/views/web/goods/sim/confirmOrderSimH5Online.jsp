<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关--%>
   <%-- <meta name="WT.si_n" content="H5号卡销售_${conf.confId}">
    <meta name="WT.si_x" content="售卡资料填写_${conf.confId}">--%>

    <META name="WT.si_n" content="">
    <META name="WT.si_s" content="">
    <META name="WT.si_x" content="20">
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <title>在线售卡-信息填写</title>
    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/dropload/dropload.css?v=3" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/css/sim/dropload/dropload.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript">
        var ctx = ${ctx};
        var cmConfId = "${conf.confId}";
    </script>
    <style>
        #keleyivisitorip{display: none;}
    </style>
<body>
<div class="container bg-gray">
    <div class="topbar">
        <a href="javascript:history.back(-1);" class="icon icon-return">返回</a>
        <%--<a href="javascript:;" class="icon icon-close">关闭</a>--%>
        <h3>资料填写</h3>
        <%--<a href="javascript:;" class="icon icon-more">更多</a>--%>
    </div>
    <div class="selected">已选择<span>“${plan.planName}
    <c:if test="${preFee != 0}">
        预存${preFee/100}
    </c:if>
    <c:if test="${cardFee != 0}">
        卡费${cardFee/100}
    </c:if>
        套餐”
    </span></div>
    <form id="confirmOrderFormSim" action="${ctx}goodsBuy/simH5OnlineSubmitOrder" method="post">
        <input type="hidden" id="confId" name="confId" value="${conf.confId}"/>
        <input type="hidden" id="cardType" name="cardType" value="${conf.cardType}"/>
        <input type="hidden" id="chnlCodeOut" name="chnlCodeOut" value="${conf.chnlCodeOut}"/>
        <input type="hidden" id="planId" name="planId" value="${plan.planId}"/>
        <input type="hidden" name="recmdCode" value="${conf.recmdCode}"/><%--推荐码--%>
        <input type="hidden" id="preFee" name="preFee" value="${preFee/100}"/>
        <input type="hidden" id="cardFee" name="cardFee" value="${cardFee/100}"/>
        <input type="hidden" id="feature_str" name="feature_str" value="${conf.feature_str}"/>
        <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
        <input type='hidden' value='${transactionId}' id='transactionId' name="transactionId">
        <input type='hidden' value='${plan.planName}' id='planName' name="planName">
        <input type='hidden' value='online' name="urlPage">
        <div class="cells">
            <div class="cell">
                <div class="cell-top">
                    <h3>请选择号码</h3>
                </div>
                <ul class="cell-list">
                    <li id="phoneCityCode">
                        <label class="must">号码归属</label>

                        <c:choose>
                            <c:when test="${cityName!=null}">
                                <p id="picker1">${cityName}</p>
                            </c:when>
                            <c:otherwise>
                                <p id="picker1">请选择号码归属</p>
                            </c:otherwise>
                        </c:choose>
                        <input type="hidden" id="cityCode" name="orderDetailSim.cityCode"/>
                        <input type="hidden" id="cityName" name="cityName"/>
                        <i class="caret"></i>
                    </li>
                    <li id="searchNumber">
                        <label class="must">选择号码</label>
                        <c:choose>
                            <c:when test="${phone!=null}">
                                <p id="phone">${phone}</p>
                            </c:when>
                            <c:otherwise>
                                <p>请选择号码</p>
                            </c:otherwise>
                        </c:choose>
                        <input type="hidden" id="phone" name="orderDetailSim.phone" value="${phone}">
                        <i class="caret"></i>
                    </li>
                    <li id="yySubmit">
                        <label class="must">身份验证</label>
                        <c:choose>
                            <c:when test="${transactionId!=null && transactionId!=''}">
                                <p style="">已完成身份验证</p>
                            </c:when>
                            <c:otherwise>
                                <p style="">点击进行身份认证</p>
                            </c:otherwise>
                        </c:choose>

                        <i class="caret"></i>
                    </li>
                </ul>
            </div>
            <div class="cell">
                <div class="cell-top">
                    <h3>根据国家实名制要求，请提供个人身份证信息</h3>
                </div>
            </div>

            <div class="cell">
                <div class="cell-top">
                    <h3>请填写配送地址</h3>
                    <%--<span>(全国配送，港澳台、新疆、西藏除外)</span>--%>
                    <span>(仅支持湖南省内配送)</span>
                </div>
                <ul class="cell-list">
                    <li>
                        <label class="must">收货人姓名</label>
                        <p id="regName">${regName}</p>
                    </li>
                    <li>
                        <label class="must">联系电话</label>
                        <input type="text" id="contactPhone" name="orderDetailSim.contactPhone" maxlength="11" placeholder="请输入电话号码" value="${contactPhone}">
                    </li>
                    <li>
                        <label class="must">所在地区</label>
                        <c:if test="${memberRecipientCity != null && memberRecipientCity != ''}" >
                            <p id="picker2">${memberRecipientCity}&nbsp;${memberRecipientCounty}</p>
                        </c:if>
                        <c:if test="${memberRecipientCity == null || memberRecipientCity == ''}">
                            <p id="picker2">请选择区/县</p>
                        </c:if>
                        <input type="hidden" id="linkAddress" name="orderDetailSim.linkAddress" value="${memberRecipientCity}${memberRecipientCounty}"/>
                        <i class="caret"></i>
                    </li>
                    <li>
                        <input type="text" id="memberRecipientAddress" name="memberAddress.memberRecipientAddress" placeholder="街道/镇+村/小区/写字楼+门牌号" value="${memberRecipientAddress}">
                        <input type="hidden" id="memberRecipientCity" name="memberAddress.memberRecipientCity" value="${memberRecipientCity}">
                        <input type="hidden" id="memberRecipientCounty" name="memberAddress.memberRecipientCounty" value="${memberRecipientCounty}">
                    </li>
                </ul>
            </div>

            <div class="agree">
                <p>
                    <input type="checkbox" id="protocol"/>
                    <label for="protocol">我已阅读并同意<a href="javascript:void(0);" onclick="toggleModal('agree-modal')">《客户入网服务协议》</a></label>
                </p>
            </div>
           <%-- <div class="cell">
                <ul class="cell-list">
                    <li>
                        <label class="must">推荐人信息</label>
                    </li>
                    <li>
                        <label class="must">渠道信息</label>
                    </li>
                </ul>
            </div>--%>
            <input type="button" id="verifySubmit" value="立即提交" class="tj-btn" otype="button" otitle="H5号卡销售_${conf.confId}-立即提交">

            <p class="fill-tips">请保持联系号码畅通，我们可能随时与您联系，联系电话无人接听或恶意下单时，将不予发货，请知悉，谢谢！</p>
        </div>
    </form>
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

<!-- 公共加载弹窗 -->
<div id="loding-modal" class="mask-layer">
    <div class="modal min-modal">
        <div class="modal-content">
            <div class="modal-con">
                <div class="dropload-load">
                    <div class="loading"></div>
                </div>
                <p class="text-center _promptSorry"></p>
                <p class="text-center _pomptTxt"></p>
                <div class="pb-20"></div>
            </div>
        </div>
    </div>
</div>

<%--选择号码--%>
<div id="select-number" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('select-number')"></a>
        <div class="modal-content">
            <div class="modal-con">
                <div class="search-con">
                    <div class="search-bar">
                        <input type="text" id="number" name="number" placeholder="生日、幸运数字等" class="search-input">
                        <input type="button" id="numberBtn" name="search-bar-btn" class="search-bar-btn">
                    </div>
                    <ul id="select-number-ul" class="search-list">
                    </ul>
                </div>
            </div>
        </div>
        <a href="javascript:void(0)" id="cagBatch" class="full-btn">换一批</a>
    </div>
</div>

<%-- 用户协议弹窗 --%>
<div id="agree-modal" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('agree-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <h4 class="pt-30">客户入网服务协议</h4>
            <div class="agree-wrap">${serviceContract}</div>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>

<!-- 信息确认弹窗 -->
<div id="info-confirm" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('info-confirm')"></a>
        <div class="modal-content">
            <h4>信息确认</h4>
            <ul class="confirm-list"></ul>
        </div>
        <div class="btn-group both-btn">
            <a href="javascript:void(0)" id="infoEdit" class="confirm-btn">编辑</a>
            <a href="javascript:void(0)" id="infoConfirm" class="confirm-btn"
               otype="button" otitle="H5号卡销售_${conf.confId}-确定">确定</a>
        </div>
    </div>
</div>

<!-- 订购成功弹窗 -->
<div id="orders-success" class="mask-layer">
    <div class="modal small-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('orders-success')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-blue text-center">您已成功订购号卡!</p>
            <p class="text-blue text-center fz-24">请关注“湖南移动微厅"查询更多详情</p>
            <p class="text-gray text-center fz-24">(可在”我的订单“中查看详情)</p>
            <div class="modal-btn-wrap">
                <a href="${ctx}/simBuy/simH5OnlineToApply?confId=${conf.confId}&recmdCode=${conf.recmdCode}&CHANID=${CHANID}" class="modal-btn">确定</a>
            </div>
        </div>
    </div>
</div>

<%--支付提交 --%>
<div id="submit-modal" class="mask-layer">
    <div class="full-modal">
        <div class="modal-wrap">
            <div class="modal-top">
                <h3>我的订单</h3>
                <a href="javascript:void(0)" class="icon-close" onclick="toggleModal('submit-modal')"></a>
            </div>
            <ul class="submit-list">
            </ul>
            <a href="${ctx}/simBuy/simH5OnlineSubmitOrder" class="modal-foot">
                <img src="${ctxStatic}/images/goods/sim/h5online/hb-pay.png">
                <div class="order-text">
                    <h3>我的订单</h3>
                    <span>移动自有快捷支付</span>
                </div>
                <i class="caret-01"></i>
            </a>
        </div>
    </div>
</div>
<span id="keleyivisitorip"></span>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript">

    try{
        /*给页面WT.si_n 赋值*/
        getConfId2("${conf.confId}","${plan.planId}");
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
    var psptVerify = false;
    var slctCity =  '${memberRecipientCity}';
    var slctCnty =  '${memberRecipientCounty}';
    var typeFlag = '${conf.chnlCodeOut}';
    var cardType = '${conf.cardType}'
</script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/IP2City.js?v=11"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/onlineSellSimConfirm.js?v=331363952"></script>
</body>
</html>