<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Session shrioSession = UserUtils.getSession();
    UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
%>
<html>
<head>
    <%@ include file="/WEB-INF/views/include/head.jsp"%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/num-card.css"/>
</head>

<body>
    <div class="top container">
        <div class="header sub-title">
            <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
            <h1>合约套餐信息</h1>
        </div>
    </div>

    <div class="container pd-t45 gray-hs">
        <div class="rw-xy">
            <div class="tx-xy">
                <ul class="order-info4 order-ft">
                    <li>
                        <label>办理手机号码：</label>
                        <div class="right-td">
                            <span>${carModel.orderDetailContract.serialNumber}</span>
                        </div>
                    </li>
                    <li>
                        <label>合约：</label>
                        <div class="right-td">
                            <span>${carModel.orderDetailContract.contractName}</span>
                        </div>
                    </li>
                    <li>
                        <label>套餐：</label>
                        <div class="right-td">
                            <span>${carModel.orderDetailContract.elementName}</span>
                        </div>
                    </li>
                    <li>
                        <label>合约期限：</label>
                        <div class="right-td">
                            <span>${carModel.orderDetailContract.promisionDuration}个月</span>
                        </div>
                    </li>
                    <li>
                        <label>月保底消费：</label>
                        <div class="right-td">
                            <span>${carModel.orderDetailContract.sumLimitPrice / 100}元</span>
                        </div>
                    </li>
                    <li>
                        <label>赠送费：</label>
                        <div class="right-td">
                            <span>${carModel.orderDetailContract.depositFee / 100}元</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="tj-btn">
            <a class="btn btn-blue" href="javascript:void(0)" onclick="window.history.back()">确定</a>
        </div>
    </div>
</body>
</html>