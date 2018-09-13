<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <title>湖南移动网上商城</title>
    <meta name="WT.si_n" content="在线选号">
    <meta name="WT.si_x" content="套餐详情	">
    <meta name="WT.pn_sku" content="${plans.phoneNum}"/>
    <%--取值选定的手机号码--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/num-card.css"/>
    <style>
        table{
            table-layout: fixed;
        }
    </style>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
        <h1>产品详情</h1>
    </div>
</div>
<div class="container pd-t45 white-bg">
    <div class="nc-pro-title">
        <h2>${simplePlansName}</h2>
    </div>
    <div class="nc-tancan-title">
        <h2>${plan.planFee/100}元档</h2>
    </div>
    <table class="nc-table">
        <%-- 先渲染标题，再渲染内容 --%>
        <c:forEach items="${plansConfs}" var="pcs">
            <tr>
                <td <c:if test="${pcs.valueLabel eq 'colspan'}">colspan="2" class="text-center"</c:if>>${pcs.keyLabel}</td>
                <c:if test="${pcs.valueLabel ne 'colspan'}">
                    <td id="${pcs.key}"></td>
                </c:if>
            </tr>
        </c:forEach>
    </table>
    <form id="dealPlanDetail">
        <input type='hidden' value='${CSRFToken}' id='csrftoken2' name="CSRFToken">
        <input type="hidden" name="planId" value="${plan.planId}"/>
        <input type="hidden" name="brandCode" value="${plan.brandCode}"/>
        <input type="hidden" name="planName" value="${plan.planName}"/>
        <input type="hidden" name="planCode" value="${plan.planCode}"/>
        <input type="hidden" name="productId" value="${plan.productId}"/>
        <input type="hidden" name="planFee" value="${plan.planFee}"/>
        <input type="hidden" name="planDesc" value="${plan.planDesc}"/>
        <input type="hidden" name="remark" value="${plan.remark}"/>
        <input type="hidden" name="keyword" value="${plan.keyword}"/>
        <input type="hidden" name="callTime" value="${plan.callTime}"/>
        <input type="hidden" name="internetFlow" value="${plan.internetFlow}"/>
        <input type="hidden" name="planType" value="${plan.planType}"/>
        <input type="hidden" name="calledRange" value="${plan.calledRange}"/>
        <input type="hidden" name="getBusiness" value="${plan.getBusiness}"/>
        <input type="hidden" name="callOut" value="${plan.callOut}"/>
        <input type="hidden" name="dataOut" value="${plan.dataOut}"/>
        <input type="hidden" name="smsOut" value="${plan.smsOut}"/>
        <input type="hidden" name="smsBusiA" value="${plan.smsBusiA}"/>
        <input type="hidden" name="smsBusiB" value="${plan.smsBusiB}"/>
        <input type="hidden" name="smsBusiC" value="${plan.smsBusiC}"/>
        <input type="hidden" name="planImgUrl" value="${plan.planImgUrl}"/>

        <input type="hidden" name="phoneNum" value="${plan.phoneNum}"/>
        <input type="hidden" name="preFee" value="${plan.preFee}"/>
        <input type="hidden" name="cityCode" value="${cityCode}"/>
    </form>
    <div class="info info-sd">
        <p>月租${plan.planFee/100}元，包含国内数据流量${plan.internetFlow}(2、3、4G网通用)，本地接听免费</p>
    </div>
</div>
<!--所选号码信息悬浮-->
<div class="fix-br fix-bt">
    <div class="affix foot-box bulid-fix">
        <div class="container sdd-btn">
            <div class="xh-mesg">
                <div class="mesg-text">所选靓号：<strong>${fn:substring(plan.phoneNum, 0, 11)}</strong></div>
                <div class="mesg-text">归属地市：<strong>${fns:getDictLabel(cityCode, 'CITY_CODE_CHECKBOXES', '长沙')}</strong>
                </div>
                <div class="mesg-text">所选产品：${plan.planName}</div>
                <div class="mesg-text">
                    <p>
                        支付金额：预存话费<strong>${plan.preFee}</strong>+卡费<strong>0.00</strong>=<strong>${plan.preFee}元</strong>
                    </p>
                    <p class="mesg-cl">亲，提交订单后，请在30分钟内完成支付哦，超出后订单将自动取消。该商品不支持七天无理由退换货。</p>
                </div>
            </div>
            <a id="confirmBtn" href="javascript:$('#planForm').submit();" class="btn btn-blue">确认，去下单</a>
            <%--<a href="#" class="btn btn-gray">敬请期待！！！</a>--%>
        </div>
    </div>
</div>

<%--其它公用：套餐详情 -> 确认订单 --%>
<form id="planForm" action="${ctx}/goodsBuy/linkToConfirmOrderSim" method="post">
    <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
    <input type="hidden" name="userGoodsCarList[0].shopId" value="1"/>
    <input type="hidden" name="userGoodsCarList[0].shopTypeId" value="6"/>
    <input type="hidden" name="userGoodsCarList[0].shopName" value="湖南省移动公司"/>
    <input type="hidden" name="userGoodsCarList[0].goodsShortDesc" value="号码"/>
    <input type="hidden" name="userGoodsCarList[0].goodsName" value="号码"/>
    <input type="hidden" name="userGoodsCarList[0].goodsType" value="2"/>
    <input type="hidden" name="userGoodsCarList[0].goodsBuyNum" value="1"/>
    <input type="hidden" name="userGoodsCarList[0].categoryId" value="2"/>
    <input type="hidden" name="userGoodsCarList[0].isChecked" value="Y"/>

    <input type="hidden" name="orderDetailSim.phone" value="${plan.phoneNum}"/>
    <input type="hidden" name="orderDetailSim.preFee" value="${plan.preFee}"/>
    <input type="hidden" name="orderDetailSim.simProductId" value="${plan.productId}"/>
    <input type="hidden" name="orderDetailSim.baseSet" value="${plan.planCode}"/>
    <input type="hidden" name="orderDetailSim.baseSetName" value="${plan.planName}"/>
    <input type="hidden" name="orderDetailSim.cityCode" value="${cityCode}"/>
</form>
<script type="text/javascript">
    $(function(){
        $("#planNameTd").text($("#dealPlanDetail input[name='planName']").val());
        $("#planFeeTd").text($("#dealPlanDetail input[name='planFee']").val()/100+"元");
        $("#callTimeTd").text($("#dealPlanDetail input[name='callTime']").val()+"分钟");
        $("#internetFlowTd").text($("#dealPlanDetail input[name='internetFlow']").val());
        $("#callOutTd").text($("#dealPlanDetail input[name='callOut']").val());
        $("#dataOutTd").text($("#dealPlanDetail input[name='dataOut']").val());
        $("#smsOutTd").text($("#dealPlanDetail input[name='smsOut']").val());
        $("#calledRangeTd").text($("#dealPlanDetail input[name='calledRange']").val());
        $("#smsBusiATd").text($("#dealPlanDetail input[name='smsBusiA']").val());
        $("#smsBusiBTd").text($("#dealPlanDetail input[name='smsBusiB']").val());
        $("#smsBusiCTd").text($("#dealPlanDetail input[name='smsBusiC']").val());
    });
</script>
</body>
</html>