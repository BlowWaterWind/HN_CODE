
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <%--插码相关--%>
   <%--<meta name="WT.si_n" content="H5号卡销售_${conf.confId}">
    <meta name="WT.si_x" content="售卡资料填写_${conf.confId}">--%>

    <META name="WT.si_n" content="">
    <META name="WT.si_x" content="21">
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">


    <title>滴滴至尊卡</title>
    <link href="${ctxStatic}/css/sim/wap-simonlineV2-didi.css?v=2" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/sim-loading.css?v=2" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/didisim-loading.css?v=2" rel="stylesheet">
    <link href="${ctxStatic}/css/sim/dropload/dropload.css?v=3" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/css/sim/dropload/dropload.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
<%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/insertCode.js?v=7"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript">
        var ctx = ${ctx};
        var cmConfId = ${conf.confId};
    </script>
    <style>
        #toast
        {
            position: fixed;
            top: 200px;
            left: 50%;
            width: 130px;
            margin-left: -80px;
            border: 1px solid #383838;
            background-color: #383838;
            padding: 10px 0 ;
            text-align:center;
            opacity: .9;
            border-radius:5px;
            margin-top: 80px;
            -webkit-transition: opacity 0.5s ease-out ;
            -moz-transition: opacity 0.5s ease-out;
            -ms-transition: opacity 0.5s ease-out;
            -o-transition: opacity 0.5s ease-out;
            transition: opacity 0.5s ease-out;

        }
    </style>
</head>

<body>
<!-- start container -->
<div class="container bg-gray" >
    <!-- end topbar -->
    <form id="confirmOrderFormSim" action="${ctx}goodsBuy/simH5OnlineSubmitOrder" method="post">
        <input type="hidden" id="confId" name="confId" value="${conf.confId}"/>
        <input type="hidden" id="chnlCodeOut" name="chnlCodeOut" value="${conf.chnlCodeOut}"/>
        <input type="hidden" id="planId" name="planId" value="${plan.planId}"/>
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
                    <div class="cell-row">
                        <p>已选择<span class="text-orange">“${plan.planName}”</span>
                        <%--</p>--%>
                        <%--<p>根据国家实名制要求，请准确提供身份证信息</p>--%>
                    </div>
                </div>
                <div class="cell-top">
                    <p>请选择号码</p>
                </div>
                <ul class="cell-list">
                    <li>
                        <label>号码归属</label>
                        <%--<input type="text" id="picker1" name="phone" placeholder="请选择号码归属" readonly="ture" class="text-right">--%>
                        <p id="picker1" align="right">请选择号码归属</p>
                        <input type="hidden" id="cityCode" name="orderDetailSim.cityCode"/>
                        <input type="hidden" id="cityName" name="cityName"/>
                    </li>
                    <li id="searchNumber">
                        <label>选择号码</label>
                        <c:choose>
                            <c:when test="${phone!=null}">
                                <p align="right">${phone}</p>
                            </c:when>
                            <c:otherwise>
                                <p align="right"></p>
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
                    <div class="cell-row">
                        <p>根据国家实名制要求，请准确提供身份证信息</p>
                    </div>
                </div>
            </div>

            <div class="cell">
                <div class="cell-top">
                    <p>请填写配送地址</p>
                    <span class="text-orange">(仅支持湖南省内配送)</span>
                </div>
                <ul class="cell-list">
                    <li>
                        <label>姓名</label>
                        <label type="" >${regName}</label>
                    </li>
                    <li>
                        <label>联系电话</label>
                        <input type="text" id="contactPhone" name="orderDetailSim.contactPhone" maxlength="11"
                               placeholder="请输入联系电话" class="text-right">
                    </li>
                    <li>
                        <label>所在地区</label>
                        <c:if test="${memberRecipientCity != null && memberRecipientCity != ''}">
                            <p id="picker2">${memberRecipientCity}&nbsp;${memberRecipientCounty}</p>
                        </c:if>
                        <c:if test="${memberRecipientCity == null || memberRecipientCity == ''}">
                            <p id="picker2" align="right">请选择区/县</p>
                        </c:if>
                        <input type="hidden" id="linkAddress" name="orderDetailSim.linkAddress"
                               value="${memberRecipientCity}${memberRecipientCounty}"/>
                        <i class="caret"></i>
                    </li>
                    <li>
                        <input type="text" id="memberRecipientAddress" name="memberAddress.memberRecipientAddress"
                               placeholder="街道/镇+村/小区/写字楼+门牌号" value="${memberRecipientAddress}">
                        <input type="hidden" id="memberRecipientCity" name="memberAddress.memberRecipientCity"
                               value="${memberRecipientCity}">
                        <input type="hidden" id="memberRecipientCounty" name="memberAddress.memberRecipientCounty"
                               value="${memberRecipientCounty}">
                    </li>
                </ul>
            </div>
            <div class="agree">
                <p>
                    <input type="checkbox" id="protocol"/>
                    <label for="protocol">我已阅读并同意<a href="javascript:void(0);" onclick="toggleModal('agree-modal')"
                                                    class="text-orange">《客户入网服务协议》</a></label>
                </p>
            </div>
            <div style="margin-top:20px">
                <input type="button" id="verifySubmit" value="立即提交" class="btn-orange" otype="button"
                       otitle="H5号卡销售_${conf.confId}-立即提交" disabled="disabled">
                <%--<input type="button" name="" value="立即提交" class="btn-orange" onclick="toggleModal('submit-modal')">--%>
                <!-- 灰色按钮请加 disabled=“disabled”属性 -->
            </div>
        </div>
    </form>
</div>

<!-- end container -->

<!-- 信息填写通用弹窗 -->
<div id="sorry-modal" class="mask-layer">
    <div class="modal small-modal">
        <a id="sorry-modal-a" href="javascript:void(0)" onclick="toggleModal('sorry-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _promptSorry"></p>
            <p class="text-center _pomptTxt">暂无符合要求的号码</p>
            <%--<p class="text-center">请填写配送地址!</p>--%>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">我知道了</a>
            <%--<a href="javascript:void(0)" class="confirm-btn" onclick="toggleModal('fill-tips')">我知道了</a>--%>
        </div>
    </div>
</div>
<!-- 信息填写通用弹窗 -->

<!-- 信息确认弹窗 -->
<div id="info-confirm" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)"  onclick="toggleModal('info-confirm')"></a>
        <div class="modal-content">
            <h4>信息确认</h4>
            <ul class="confirm-list"></ul>
        </div>
        <div class="btn-group both-btn">
            <a href="javascript:void(0)" id="infoEdit" class="confirm-btn">取消</a>
            <a href="javascript:void(0)" id="infoConfirm" class="confirm-btn"
               otype="button" otitle="H5号卡销售_${conf.confId}-确定">确认</a>
        </div>
    </div>
</div>

<!-- 订购成功弹窗 -->
<div id="orders-success" class="mask-layer">
    <div class="modal small-modal">
        <a href="javascript:void(0)"  onclick="toggleModal('orders-success')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-blue text-center">您已成功订购号卡!</p>
            <p class="text-blue text-center fz-24">请关注“湖南移动微厅"查询更多详情</p>
            <p class="text-gray text-center fz-24">(可在”我的订单“中查看详情)</p>
            <div class="modal-btn-wrap">
                <a href="${ctx}/simBuy/simH5OnlineToApply?confId=${conf.confId}" class="modal-btn">确定</a>
            </div>
        </div>
    </div>
</div>

<!-- 选择号码弹窗 -->
<div id="select-number" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('select-number')"></a>
        <div class="search-con">
            <div class="search-bar">
                <input type="text" id="number" name="number" placeholder="生日、幸运数字等" class="search-input text-orange">
                <input type="button" id="numberBtn" class="search-bar-btn" value="搜索" style="display: none">
                <input type="button" id="numberBtnClean" class="clear-btn" style="display: none">
            </div>
            <div class="search-list">
            </div>
        </div>
        <a href="javascript:void(0)" id="cagBatch" class="full-btn">换一批</a>
    </div>
</div>
<!-- 选择号码弹窗 -->

<!-- 用户协议弹窗 -->
<div id="agree-modal" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)"  onclick="toggleModal('agree-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <h4>中国移动客户入网服务协议</h4>
            <div class="agree-wrap">${serviceContract}</div>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn" onclick="toggleModal('agree-modal')">确定</a>
        </div>
    </div>
</div>

<!-- 公共加载弹窗 -->
<div id="loding-modal" class="mask-layer">
    <div id="toast">
    <div class='m-load1' style='float: left;margin-left: 15px'>
        <div class='line'> <div></div> <div></div><div></div><div></div><div></div><div></div> </div>
        <div class='circlebg'></div></div>
      <p style='float: right;margin-right: 20px;margin-top:3px;color:#D5D5D9;'>加载中...</p>
    </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=11"></script>
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
    var slctCity = '${memberRecipientCity}';
    var slctCnty = '${memberRecipientCounty}';
    var typeFlag = '${conf.chnlCodeOut}';

</script>

<script type="text/javascript" src="${ctxStatic}/js/goods/sim/onlineSellSimConfirm.js?v=231"></script>
</body>
</html>