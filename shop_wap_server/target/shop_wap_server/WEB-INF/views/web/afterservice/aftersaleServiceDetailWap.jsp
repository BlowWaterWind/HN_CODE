<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>售后详情</title>
    <link href="<%=request.getContextPath()%>/static/css/main.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/static/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css" />
    <%--<link href="<%=request.getContextPath()%>/static/css/oil.css" rel="stylesheet" type="text/css" />--%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/nav.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/public.js"></script>
    <%--<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/modal.js"></script>--%>
</head>
<body>
<div class="top container">
    <div class="header sub-title"> <a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
        <h1>售后单详情</h1>
    </div>
</div>
<div class="container pd-t45">
    <div class="qr-dl gray-bg">
        <div>
            <p>订单号：${asApp.orderSub.orderSubNo}</p>
            <p>下单时间：<fmt:formatDate value="${asApp.orderSub.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            <p class="font-rose">订单状态：${asApp.orderSub.orderStatus.orderStatusName}</p>
        </div>
        <%--<span class="font-rose rcl">卖家已发货</span>--%>
        <%--<a href="个人中心 - 我的订单 - 查看物流.html" class="btn pull-right btn-sm btn-blue">查看物流</a>--%>
    </div>
    <div class="cur-ul cur-con">
        <div class="cur-li cur-bd">
            <p class="tabcon-cl"> <span class="pull-jl02">商品信息</span> </p>
            <div class="tabcon-cl tab-pic">
                <!-- <a href="#"> -->
                <dl>
                    <dt><img src="${tfsUrl}${asApp.orderSub.detailList[0].goodsImgUrl}" /></dt>
                    <dd>
                        <h2>商品名称：${asApp.orderSub.detailList[0].goodsName}</h2>
                        <span class="tab-zh">${asApp.orderSub.detailList[0].goodsFormat}</span>
                    </dd>
                    <i class="dy">
                        <p class="tabcon-cl tabcon-cl03">${asApp.orderSub.detailList[0].goodsSkuPrice/100}</p>
                        <p class="tabcon-cl tabcon-cl03 tab-lr">×${asApp.orderSub.detailList[0].goodsSkuNum}</p>
                    </i>
                </dl>
                <!-- </a>-->
            </div>
        </div>
    </div>
    <!-- 售后申请信息 -->
    <div class="cur-con">
        <ul class=" box10 white-bg">
            <%--<p class="tabcon-cl"> <span class="pull-jl02">售后单信息</span> </p>--%>
            <div>
                <p>售后单号：${asApp.aftersaleApplyNum}</p>
                <p>申请时间：<fmt:formatDate value="${asApp.aftersaleApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                <p>申请原因：${asApp.aftersaleApplyReason}</p>
                <c:if test="${! empty asApp.aftersaleApplyDescribe}">
                    <p>原因描述：${asApp.aftersaleApplyDescribe}</p>
                </c:if>
                <!-- TODO：上传的图片 -->
            </div>
        </ul>
    </div>
    <!-- 协商记录 -->
    <div class="cur-con">
        <ul class=" box10 white-bg">
            <p class="tabcon-cl"> <span class="pull-jl02">协商记录</span> </p>
            <!-- 申请信息 -->
            <li>
                <c:choose>
                    <c:when test="${!empty  memberLoginName}">
                        <span class="pull-left">${asApp.memberLoginName}:</span>
                    </c:when>
                    <c:otherwise>
                        <span class="pull-left">买家：</span>
                    </c:otherwise>
                </c:choose>
                申请"${asApp.aftersaleApplyTypeName}"服务
                <span class="pull-right"><fmt:formatDate value="${asApp.aftersaleApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
            </li>
            <!-- 管理员审核信息 -->
                <c:if test="${! empty asApp.aftersaleApplyAuditL}">
                <li>
                <c:forEach items="${asApp.aftersaleApplyAuditL}" var="l">
                    <c:choose>
                        <c:when test="${!empty l.operator  }">
                            <span class="pull-left">${l.operator}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="pull-left">管理员</span>
                        </c:otherwise>
                    </c:choose>
                    <span class="pull-right"><fmt:formatDate value="${l.aftersaleApplyAuditTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                    <c:choose>
                        <c:when test="${l.aftersaleApplyAuditResult=='Y'}">
                            <span class="pull-left">审核通过：${l.aftersaleApplyAuditText}（备注：${l.aftersaleApplyAuditRemark}）</span>
                        </c:when>
                        <c:otherwise>
                            <span class="pull-left">审核不通过：${l.aftersaleApplyAuditText}（备注：${l.aftersaleApplyAuditRemark}）</span>
                        </c:otherwise>
                    </c:choose>
                    </div>
                </c:forEach>
                </li>
            </c:if>

            <!-- 卖家处理信息 -->

                <c:if test="${! empty asApp.aftersaleApplyDealL}">
                <li>
                <c:forEach items="${asApp.aftersaleApplyDealL}" var="l">
                   <c:choose>
                        <c:when test="${! empty l.aftersaleApplyDealOperator}">
                            <span class="pull-left">卖家：${l.aftersaleApplyDealOperator}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="pull-left">卖家</span>
                        </c:otherwise>
                    </c:choose>
                    <span class="title fr"><fmt:formatDate value="${l.aftersaleApplyDealTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                    <c:choose>
                       <c:when test="${l.aftersaleApplyDealResult=='Y'}">
                            <span class="pull-left">处理成功：${l.aftersaleApplyDealText}（备注：${l.aftersaleApplyDealRemark}）</span>
                        </c:when>
                        <c:otherwise>
                            <span class="pull-left">处理失败：${l.aftersaleApplyDealText}（备注：${l.aftersaleApplyDealRemark}）</span>
                        </c:otherwise>
                     </c:choose>
                </c:forEach>
                </li>
            </c:if>
        </ul>
    </div>
</div>
<%@ include file="/release/front/navBottom.jsp"%>
<!--退单确认弹出框 begin-->
<div class="share-bit">
    <div class="share-text"> <span>确认删除此订单?</span> <a class="cancel cancel-con">确认</a> </div>
    <div class="share-gb"><a class="cancel cancel-con">取消</a></div>
</div>
<div class="more-box"></div>
<script>
    //二次确认框
    $(document).ready(function(){
        $(".confirmtw").click(function(){
            $(".share-bit").slideDown('fast');
            $(".more-box").addClass("on");
        });
        $(".more-box").click(function(){
            $(".share-bit").slideUp('fast');
            $(".more-box").removeClass("on");
        });
        $(".cancel").click(function(){
            $(".share-bit").slideUp('fast');
            $(".more-box").removeClass("on");
        });

    });
</script>
</body>
</html>
