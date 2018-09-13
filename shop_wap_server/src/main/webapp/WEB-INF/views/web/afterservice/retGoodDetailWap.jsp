<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>退货详情</title>
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
        <h1>退货单详情</h1>
    </div>
</div>
<div class="container pd-t45">
    <div class="qr-dl gray-bg">
        <div>
            <p>订单号：${retOrder.orderSub.orderSubNo}</p>
            <p>下单时间：<fmt:formatDate value="${retOrder.orderSub.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
            <p class="font-rose">订单状态：${retOrder.orderSub.orderStatus.orderStatusName}</p>
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
                    <dt><img src="${tfsUrl}${retOrder.orderSub.detailList[0].goodsImgUrl}" /></dt>
                    <dd>
                        <h2>商品名称：${retOrder.orderSub.detailList[0].goodsName}</h2>
                        <span class="tab-zh">${retOrder.orderSub.detailList[0].goodsFormat}</span></dd>
                    <i class="dy">
                        <p class="tabcon-cl tabcon-cl03">${retOrder.orderSub.detailList[0].goodsSkuPrice/100}</p>
                        <p class="tabcon-cl tabcon-cl03 tab-lr">×${retOrder.orderSub.detailList[0].goodsSkuNum}</p>
                    </i>
                </dl>
                <!-- </a>-->
               </div>
           </div>
       </div>
        <!-- 退货信息 -->
        <div class="cur-con">
            <ul class=" box10 white-bg">
                <%--<p class="tabcon-cl"> <span class="pull-jl02">退货信息</span> </p>--%>
                <div>
                    <p>退货单号：${retOrder.returnOrderNo}</p>
                    <p>退货原因：${retOrder.returnReson}</p>
                    <p>申请时间：<fmt:formatDate value="${changeOrder.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                    <c:if test="${! empty retOrder.returnResonDesc}">
                    <p>原因描述：${retOrder.returnResonDesc}</p>
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
                    <c:when test="${! empty retOrder.proxyApplyPerson}">
                       <span class="pull-left">${retOrder.proxyApplyPerson}：代理申请退货服务</span>
                    </c:when>
                    <c:otherwise>
                        <span class="pull-left">${retOrder.applyLoginName}：申请退货服务</span>
                    </c:otherwise>
                </c:choose>
                    <span class="pull-right"><fmt:formatDate value="${retOrder.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                </li>

                <!-- 处理信息 -->
                <c:forEach items="${retOrder.retOrderActLogL}" var="l">
                <c:if test="${! empty l.operator}">
                <li>
                    <span class="pull-left">${l.operator}：${l.actName}</span>
                    <span class="pull-right"><fmt:formatDate value="${l.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                </li>
                </c:if>
                </c:forEach>
            </ul>
        </div>
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
