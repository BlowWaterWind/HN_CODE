<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Session shrioSession = UserUtils.getSession();
    UserGoodsCarModel carModel = (UserGoodsCarModel) shrioSession.getAttribute("carModel");
%>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="WT.si_n" content="购物流程"/>
    <meta name="WT.si_x" content="填写订单"/>
    <meta name="WT.pn_sku" content="${skuIds}"/>
    <%--取值产品的id，多个产品用;隔开，必填--%>
    <meta name="WT.tx_s" content="${carModel.paymentAmount/100}">
    <%--取值订单总价，必填--%>
    <meta name="WT.tx_u" content="${goodsNumCount}">
    <%--取值手机的数量，必填--%>
    <meta name="format-detection" content="telephone=no">
    <title>确认订单</title>
    <%@ include file="/WEB-INF/views/include/message.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/num-card.css"/>

    <script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/transition.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/collapse.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/confirmOrderSim.js?v=3"></script>
    <script type="text/javascript">
        var rootCategoryId = ${carModel.rootCategoryId};
    </script>
    <style>
        .addressInput select {
            width: 200px;
            border-color: #0000FF;
            border-radius: 5px;
        }

        .addressInput input {
            width: 200px;
            background-color: #e8e8e8;
            border-radius: 5px;
        }
    </style>
</head>
<body>
<form id="confirmOrderFormSim" action="${ctx}goodsBuy/submitOrderSim" method="post">
    <%--otid：orderTypeId，--%>
    <input type="hidden" name="moduleTypeId" id="moduleTypeId" value="sim"/>
    <%--<input type="hidden" name="marketId" id="marketId" value="${marketId}"/>--%>
    <div class="top container">
        <div class="header sub-title">
            <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
            <h1>确认订单</h1>
        </div>
    </div>

    <div class="container pd-t45">
        <%--收货地址：物流配送需要填写配送地址--%>
        <c:if test="${carModel.deliveryMode.deliveryModeId == 1}">
            <%@include file="../buy/receiptAddress.jsp" %>
        </c:if>

        <div class="cur-ul cur-con">
            <div class="panel-group panel-classification panel-dy" id="accordion" role="tablist"
                 aria-multiselectable="true">
                <!-- 店铺列表 -->
                <c:if test="${carModel.rootCategoryId == 10000001 || carModel.rootCategoryId == 5}">
                    <%@include file="../buy/goodsList.jsp" %>
                </c:if>

                <div class="cur-ly cul-lt">
                    <%--合约信息start--%>
                    <c:if test="${!empty carModel.orderDetailContract && carModel.userGoodsCarList[0].goodsType == 1}">
                    <div class="sp-pj sp09">
                        <a id="queryContractBtn" href="javascript:void(0);">
                            <span class="gm-sl">合约套餐信息</span>
                            <span class="gm-zj gm-zj02">${carModel.orderDetailContract.elementName}</span>
                            <span class="sp-icon"></span>
                            <input type="hidden" name="orderDetailContract.serialNumber" value="${carModel.orderDetailContract.serialNumber}"/>
                            <input type="hidden" name="orderDetailContract.eparchyCode" value="${carModel.orderDetailContract.eparchyCode}"/>
                            <input type="hidden" name="orderDetailContract.contractId" value="${carModel.orderDetailContract.contractId}"/>
                            <input type="hidden" name="orderDetailContract.contractName" value="${carModel.orderDetailContract.contractName}"/>
                            <input type="hidden" name="orderDetailContract.sumLimitPrice" value="${carModel.orderDetailContract.sumLimitPrice}"/>
                            <input type="hidden" name="orderDetailContract.discount" value="${carModel.orderDetailContract.discount}"/>
                            <input type="hidden" name="orderDetailContract.operFee" value="${carModel.orderDetailContract.operFee}"/>
                            <input type="hidden" name="orderDetailContract.productId" value="${carModel.orderDetailContract.productId}"/>
                            <input type="hidden" name="orderDetailContract.productName" value="${carModel.orderDetailContract.productName}"/>
                            <input type="hidden" name="orderDetailContract.packageId" value="${carModel.orderDetailContract.packageId}"/>
                            <input type="hidden" name="orderDetailContract.elementId" value="${carModel.orderDetailContract.elementId}"/>
                            <input type="hidden" name="orderDetailContract.elementName" value="${carModel.orderDetailContract.elementName}"/>
                            <input type="hidden" name="orderDetailContract.promisionDuration" value="${carModel.orderDetailContract.promisionDuration}"/>
                            <input type="hidden" name="orderDetailContract.depositFee" value="${carModel.orderDetailContract.depositFee}"/>
                            <input type="hidden" name="orderDetailContract.advancePay" value="${carModel.orderDetailContract.advancePay}"/>
                            <input type="hidden" name="orderDetailContract.productTypeCode" value="${carModel.orderDetailContract.productTypeCode}"/>
                            <input type="hidden" name="orderDetailContract.fee1" value="${carModel.orderDetailContract.fee1}"/>
                            <input type="hidden" name="orderDetailContract.fee2" value="${carModel.orderDetailContract.fee2}"/>
                            <input type="hidden" name="orderDetailContract.fee3" value="${carModel.orderDetailContract.fee3}"/>
                            <input type="hidden" name="orderDetailContract.fee4" value="${carModel.orderDetailContract.fee4}"/>
                        </a>
                    </div>
                    </c:if>
                    <%--合约信息end--%>

                    <%--配送方式--%>
                    <div class="sp-pj sp09">
                        <a id="chooseDeliveryModeBtn" href="javascript:void(0);">
                            <span class="gm-sl">配送方式</span>
                                <input type="hidden" name="deliveryMode.deliveryModeId" value="${carModel.deliveryMode.deliveryModeId}"/>
                                <span class="gm-zj gm-zj02">${carModel.deliveryMode.deliveryModeName}</span>
                            <span class="sp-icon"></span>
                        </a>
                    </div>

                    <%--支付方式：无需选择，依据配送方式确定--%>
                    <div class="sp-pj sp09">
                        <a id="choosePayModeBtn" href="javascript:void(0);">
                            <span class="gm-sl">支付方式</span>
                            <%--<c:forEach items="${payModeList}" var="pl">--%>
                            <input type="hidden" name="payMode.payModeId"/>
                            <span class="gm-zj gm-zj02"></span>
                            <%--</c:forEach>--%>
                            <%--<span class="sp-icon"></span>--%>
                        </a>
                    </div>

                    <%--填写入网信息--%>
                    <c:if test="${carModel.rootCategoryId == 2}">
                        <%@include file="../buy/networkInfo.jsp" %>
                    </c:if>

                    <%--是否开发票--%>
                    <%@include file="../buy/invoicing.jsp" %>

                    <div class="sp-pj sp09 dl-tarea">
                        <textarea name="orderSubRemark" class="sp-text">订单备注：</textarea>
                    </div>
                </div>

                <%-- 填写推荐人信息 --%>
                <%@include file="../buy/recommender.jsp" %>

                <%-- 协议--%>
                <div class="wx-con">
                    <div class="tabcon-cl tab-fd">
				<span class="input-text02 input-text03">
					<input type="checkbox" class="checkbox" id="confirmCB"/>
					<label for="confirmCB" class="box-jl01">我已阅读并同意<a href="javascript:;" id="confirmModal">《中国移动通信移动电话客户入网服务协议》</a></label>
				</span>
                        <span class="input-text02"></span>
                    </div>
                </div>

            <%-- 到厅自提：营业厅信息 --%>
                <c:if test="${carModel.deliveryMode.deliveryModeId == 2}">
                    <!--自营厅 start-->
                    <div class="zy-con">
                        <div class="yt-select">
                            <label class="zy-tit font-red"><em>*</em>区县：</label>
                            <div class="zy-list">
                                <div class="yt-tit">
                                    <span id="selectedOrg" orgId="">全部</span>
                                    <i class="arrow"></i>
                                </div>
                                <input id="cityCode" type="hidden" value="${cityCode}"/>
                                <ul id="orgList" class="yt-list" style="display: none;">
                                    <c:forEach items="${orgList}" var="org">
                                        <li orgId="${org.orgId}">${org.orgName}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <table id="hallList" cellpadding="0" cellspacing="0" class="zy-tabs">
                            <thead>
                            <tr>
                                <th>区县</th>
                                <th>营业厅名称</th>
                                <th>营业厅地址</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${orgBusinessHallList}" var="hall">
                                <tr>
                                    <td>${hall.orgName}</td>
                                    <td>${hall.hallName}</td>
                                    <td>${hall.hallAddress}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <!--自营厅 end-->
                </c:if>
            </div>
        </div>
    </div>

    <!--所选号码信息悬浮-->
    <div class="fix-br fix-bt">
        <div class="affix foot-box bulid-fix">
            <div class="container sdd-btn">
                <div class="xh-mesg">
                    <div class="mesg-text">
                        分享套餐：<strong>${fn:substring(carModel.orderDetailSim.phone, 0, 11)}</strong></div>
                    <div class="mesg-text">
                        归属地市：<strong>${fns:getDictLabel(carModel.orderDetailSim.cityCode, 'CITY_CODE_CHECKBOXES', '长沙')}</strong>
                    </div>
                    <div class="mesg-text">所选产品：${carModel.orderDetailSim.baseSetName}</div>
                    <div class="mesg-text">
                        <p>
                            支付金额：预存话费<strong>${carModel.orderDetailSim.preFee}</strong>+卡费<strong>0.00</strong>=<strong>${carModel.orderDetailSim.preFee}元</strong>
                        </p>
                        <p class="mesg-cl">亲，该商品不支持七天无理由退换货。</p>
                    </div>
                </div>
                <a id="confirmBtnSim" href="javascript:;" class="btn btn-blue" otype="button" otitle="确认，提交订单"
                   oarea="终端">确认，提交订单</a></div>
        </div>
    </div>
</form>
<!--阅读协议 begin-->
<div class="modal fade modal-hy in" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content modal-content">
            <div class="top container">
                <div class="header sub-title">
                    <a data-dismiss="modal" aria-label="Close" class="icon-left" id="closeForm"></a>
                    <h1>阅读协议</h1>
                </div>
            </div>
            <div class="container hy-content">
                <div class="hy-con">
                    <h1>中国移动通信移动电话客户入网服务协议</h1>
                    ${serviceContract}
                    <br/><br/>
                    <div class="bot-btn">
                        <a shape="rect" class="btn btn-blue btn-block center-btn" aria-label="Close"
                           data-dismiss="modal" id="comfirm" otype="button" otitle="确定" oarea="终端">确定</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>