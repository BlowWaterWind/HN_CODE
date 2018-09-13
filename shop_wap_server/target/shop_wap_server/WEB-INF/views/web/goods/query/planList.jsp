<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <title>湖南移动网上商城</title>
    <meta name="WT.si_n" content="在线选号">
    <meta name="WT.si_x" content="选套餐">
    <meta name="WT.pn_sku" content="${plans.phoneNum}" />   <%--取值选定的手机号码--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/num-card.css"/>
</head>

<body class="drawer drawer-right">
<div class="top container">
    <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
        <h1>套餐选择</h1>
        <%--<a href="号卡-选择产品.html" class="icon-img-list"></a>--%>
    </div>
</div>
<div class="container pd-t45 white-bg container-con">
    <div class="bt-list bt-list-white" style="display:none;">
        <ul >
            <li><a href="#" class="" >默认</a></li>
            <li><a href="#" class="list-hr sort-btn-up"><!--由高往低排序class则改成sort-btn-down-->销量<i></i></a></li>
            <li><a href="#">人气<i></i></a></li>
            <li >
                <div class="sx drawer-toggle drawer-hamberger">
                    <a href="#" >筛选</a>
                    <span><img src="${ctxStatic}/images/shop-images/icon01.png" /></span>
                </div>
            </li>
        </ul>
    </div>
    <div class="drawer-main drawer-default">
        <div class="drawer-main-title"><a href="#" class="drawer-main-title-qx">取消</a>筛选<a href="#" class="drawer-main-title-qd">确定</a></div>
        <div id="masterdiv">
            <div class="vtitle"><em class="v"></em>月消费金额</div>
            <div class="vcon">
                <ul class="vconlist clearfix">
                    <li><a href="javascript:;" class="list-bg">不限</a></li>
                    <li><a href="javascript:;">50元以下</a></li>
                    <li><a href="javascript:;">50元-99元</a></li>
                    <li><a href="javascript:;">100元-199元</a></li>
                    <li><a href="javascript:;">200元-299元</a></li>
                </ul>
            </div>
            <div class="vtitle"><em class="v v02"></em>月通话时长</div>
            <div class="vcon"  style="display: none;">
                <ul class="vconlist clearfix">
                    <li class="select"><a href="javascript:;" class="list-bg">30分钟</a></li>
                    <li><a href="javascript:;">30分钟</a></li>
                    <li><a href="javascript:;">30分钟</a></li>
                </ul>
            </div>
            <div class="vtitle"><em class="v"></em>月上网流量</div>
            <div class="vcon" style="display: none;">
                <ul class="vconlist clearfix">
                    <li><a href="javascript:;">0M</a></li>
                    <li><a href="javascript:;">0M</a></li>
                    <li><a href="javascript:;">0M</a></li>
                </ul>
            </div>
            <div class="vtitle"><em class="v"></em>产品价格</div>
            <div class="vcon" style="display: none;">
                <ul class="vconlist clearfix">
                    <li><a href="javascript:;">1000元以下</a></li>
                    <li><a href="javascript:;">1000元以下</a></li>
                    <li><a href="javascript:;">1000元以下</a></li>
                </ul>
            </div>
            <br />
        </div>
        <div class="drawer-main-btn"><a href="#" class="btn btn-bd btn-sm">清除选项</a></div>
    </div>
    <div class="drawer-overlay-upper"></div>
</div>
<div class="container container-con">
    <div id="content">
        <div id="scroller">
            <div id="pullDown" class="" style="display:block;">
                <div class="pullDownIcon"></div>
                <div class="pullDownLabel">下拉刷新</div>
            </div>
             <div class="tab-er tab-fbs white-bg">
            <ul id="planListUl" class="tu-list hk-list">
                <c:forEach items="${plansPage}" var="plan">
                    <li>
                        <form name="planForm" action="${ctx}goodsQuery/linkToPlanDetail" method="post">
                            <input type='hidden' value='${CSRFToken}' id='csrftoken' name="CSRFToken">
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

                            <input type="hidden" name="phoneNum" value="${plans.phoneNum}"/>
                            <input type="hidden" name="preFee" value="${plans.preFee}"/>
                            <input type="hidden" name="cityCode" value="${cityCode}"/>
                        </form>
                        <%--<div class="pic">
                            <a href="javascript:;">
                                <img src="${tfsUrl}${plan.planImgUrl}"/>
                            </a>
                        </div>--%>
                        <p>${plan.planName}</p>
                        <div class="mesg-text">国内主叫：<strong>${plan.callTime}分钟</strong></div>
                        <div class="mesg-text">国内流量：<strong>${plan.internetFlow}/月</strong></div>
                        <%--<div class="mesg-text">通话资费：<strong>${plan.callOut}</strong></div>--%>
                        <%--<div class="mesg-text">流量资费：<strong>${plan.dataOut}</strong></div>--%>
                    </li>
                </c:forEach>
            </ul>
            </div>
            <div id="pullUp" class="ub ub-pc c-gra load" style="display: block;">
                <div class="pullUpIcon"></div>
                <div class="pullUpLabel">加载中...</div>
            </div>
        </div>
        <div class="iScrollVerticalScrollbar iScrollLoneScrollbar"></div>
    </div>
</div>
<!--所选号码信息悬浮-->
<div class="fix-br fix-bt">
    <div class="affix foot-box bulid-fix">
        <div class="container sdd-btn">
            <div class="xh-mesg">
                <div class="mesg-text">所选靓号：<strong>${fn:substring(plans.phoneNum, 0, 11)}</strong></div>
                <div class="mesg-text">归属地市：<strong>${fns:getDictLabel(cityCode, 'CITY_CODE_CHECKBOXES', '长沙')}</strong></div>
                <div class="mesg-text">
                    <p>支付金额：预存话费<strong>${plans.preFee}</strong>+卡费<strong>0.00</strong>=<strong>${plans.preFee}元</strong></p>
                    <p class="mesg-cl">亲，提交订单后，请在30分钟内完成支付哦，超出后订单将自动取消。该商品不支持七天无理由退换货。</p>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${ctxStatic}/js/filter.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iscroll-probe.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/pagelist.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/packageList.js"></script>
</body>
</html>
