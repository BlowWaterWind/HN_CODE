<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KD_TS" />
    <meta name="WT.si_x" content="21" />
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadbandSpeedUp.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <%--<script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>--%>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
    <script>
        var baseProject = "${ctx}" ;
    </script>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带提速</h1>
    </div>
</div>
<div class="container bg-gray hy-tab">
    <div class="wf-list tab-con">
        <!--宽带提速 start-->
        <div class="wf-ait clearfix">
            <form id="speedUpForm" method="post"
                  action="${ctx}/broadbandSpeedUp/comfirmLevel">
                <input type="hidden"  name="bandAccount" id="bandAccount" value="${speedupRecord.bandAccount}"/>
                <input type="hidden" name="newBandWidth" id="newBandWidth" value=""/>
                <input type="hidden" name="payAmount" id="payAmount" value=""/>
                <input type="hidden" name="productInfo" value="${speedupRecord.productInfo}"/>
                <input type="hidden" name="startTime" value='<fmt:formatDate value="${speedupRecord.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
                <input type="hidden" name="endTime" value='<fmt:formatDate value="${speedupRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
                <input type="hidden" name="custName" value="${speedupRecord.custName}"/>
                <input type="hidden" name="address" value="${speedupRecord.address}"/>
                <input type="hidden" id="serialNumber" name="serialNumber" value="${speedupRecord.serialNumber}"/>
                <input type="hidden" name="psptId" value="${speedupRecord.psptId}"/>
                <input type="hidden" name="oldBandWidth" value="${speedupRecord.oldBandWidth}"/>

            <div class="wf-tit"><span class="pull-left">宽带账号：${speedupRecord.bandAccount}</span><span class="pull-right font-red">可提速</span></div>
            <div class="wf-con">
                <p class="font-gray">套餐名称：<span class="font-3">${speedupRecord.productInfo}</span></p>
                <p class="font-gray">合约期限：<span class="font-3"><fmt:formatDate value="${speedupRecord.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>~ <fmt:formatDate value="${speedupRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
                <p class="font-gray">安装地址：<span class="font-3">${speedupRecord.address}</span></p>
                <p class="font-gray">装机人：<span class="font-3">${speedupRecord.custName}</span></p>
                <p class="font-gray">联系电话：<span class="ts-box">
										<input type="text" class="form-control" placeholder="关联您的手机，方便用手机号登陆管理" id="mobile" value="${speedupRecord.serialNumber}" otitle="绑定手机"  otype="button" oarea="宽带提速"/>
										<button type="button"  class="ts-btn" id="getValiCode" otitle="获取验证码"  otype="button" oarea="宽带提速">获取短信验证码</button>
										<input type="text"  id="valiCode" class="form-control dx-box" placeholder="请输入验证码" otitle="验证码"  otype="button" oarea="宽带提速" />
										</span>
                </p>
            </div>
                </form>
        </div>
        <!--宽带提速 end-->
        <!--选择套餐 start-->
        <div class="change-tc">
            <!--续费金额 start-->
            <h2 class="renew-title">请选择提速套餐</h2>
            <ul class="add_oil select-renew clearfix" >
                <c:forEach items="${speedupRecord.rateList}" var="rate" varStatus="status">
                    <c:choose>
                    <c:when test="${status.index eq 0}">
                            <li class="on" onclick="chooseRate(this)" data="${rate}">${rate}
                        <script type="text/javascript">
                            var speedTemp = '${rate}';
                            var priceTemp="${speedupRecord.rateMap[rate]}";
                            $("#newBandWidth").val(speedTemp);
                            $("#payAmount").val(priceTemp);
                        </script>
                     </c:when>
                    <c:otherwise>
                        <li onclick="chooseRate(this)" data="${rate}">${rate}
                    </c:otherwise>
                    </c:choose>
                                <p  class="font-gray">${speedupRecord.rateMap[rate]/100}元/月</p>
                                <s></s> </li>
                    <input type="hidden" name="rate${rate}" value="${speedupRecord.rateMap[rate]}"/>
                </c:forEach>
            </ul>
            <!--充值金额 end-->
            <div class="renew-content">
                <ul class="clearfix">
                    <li class="clearfix bg-grayf2">
                        <p class="font-red">续费升级，提前享受光速带宽！</p>
                    </li>
                </ul>
            </div>
            <!--选择套餐 end-->
        </div>
    </div>
</div>
<div class="fix-br">
    <div class="affix foot-box afix-btn">
        <div class="container active-fix">
            <%--<a href="javascript:void(0);" class="btn btn-rose btn-block" id="speedUp">立即提速</a>--%>
            <button type="button"  class="btn btn-rose btn-block" id="speedUp" >立即提速</button>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript"  src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>
