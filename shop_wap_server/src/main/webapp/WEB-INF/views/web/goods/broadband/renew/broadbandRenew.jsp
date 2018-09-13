<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带续费</h1>
    </div>
</div>--%>
<c:set value="宽带续费" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<div class="container bg-gray hy-tab">
    <div class="wf-list tab-con">
        <!--续费号码 start-->
        <div class="wf-ait clearfix">
            <div class="wf-tit">
                <span class="pull-left">宽带账号：${broadbandInfo.broadbandAccount}</span>
                <span class="pull-right font-red">${broadbandInfo.remainingDays}天后过期</span>
            </div>
            <div class="wf-con">
                <p class="font-gray">套餐类型：<span class="font-3">${broadbandInfo.prodName}</span></p>
                <p class="font-gray">
                    <span class="sus-tit">合同期限：</span>
                    <span class="sus-text font-3">
                        ${broadbandInfo.startTime}~${broadbandInfo.endTime}
                    </span>
                </p>
                <p class="font-gray">
                    <span class="sus-tit">安装地址：</span>
                    <span class="sus-text font-3">${broadbandInfo.installAddress}</span>
                </p>
            </div>
        </div>
        <!--续费号码 end-->
        <!--选择套餐 start-->
        <div class="change-tc">
            <!--续费金额 start-->
            <h2 class="renew-title">请选择续费套餐</h2>
            <ul id="broadbandListUl" class="add_oil select-renew clearfix">
                <c:forEach items="${broadbandItemList}" var="broadbandItem">
                    <c:choose>
                           <c:when test="${broadbandItem.goodsSkuId == '40000136020'}">
                                  <li>
                                      ${broadbandItem.bandWidth}/M
                                      <input type="hidden" name="speed" value="${broadbandItem.bandWidth}"/>
                                     <p class="font-gray">
                                         ${broadbandItem.price}元/${broadbandItem.term}
                                     </p>
                                     <input type="hidden" name="skuId" value="${broadbandItem.goodsSkuId}"/>
                                     <input type="hidden" name="price" value="${broadbandItem.price}"/>
                                        <s></s><span class="c-tip"></span>
                                 </li>
                           </c:when>
                           <c:otherwise>
                                 <li>
                                      ${broadbandItem.bandWidth}/M
                                      <input type="hidden" name="speed" value="${broadbandItem.bandWidth}"/>
                                     <p class="font-gray">
                                         ${broadbandItem.price}元/${broadbandItem.term}
                                     </p>
                                     <input type="hidden" name="skuId" value="${broadbandItem.goodsSkuId}"/>
                                     <input type="hidden" name="price" value="${broadbandItem.price}"/>
                                        <s></s>
                                 </li>
                           </c:otherwise>
                    </c:choose>
                </c:forEach>
            </ul>
            <!--续费金额 end-->
            <!--  
            <div class="renew-content">
                <ul class="clearfix">
                    <li class="clearfix bg-grayf2">
                        <p class="font-red">续费升级，提前享受光速带宽！</p>
                    </li>
                </ul>
            </div>
            -->
            <!--选择套餐 end-->
        </div>
    </div>
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div class="container active-fix kd-disabled">
            <div class="fl-left">
                <p class="p1 fl-con">应付总额：
                    <b id="priceText" class="cl-rose">￥0.00</b>
                </p>
            </div>
            <div class="fl-right">
                <a id="renewBtn" href="javascript:;">立即续费</a>
            </div>
        </div>
    </div>
</div>

<!--绑定宽带账户弹框 start-->
<div class="mask"></div>
<div class="share-box share-sum share-box-top" >
    <div class="share-content">
        <div class="con-changae">
            <label><input type="radio" name="radio" />宽带账号查询</label>
        </div>
        <div><input type="text"  class="form-control" /></div>
        <div class="text-center kd-renew-phone-search">
            <a class="btn btn-bd kdicon-search" href="javascript:;">搜索</a>
            <a class="btn btn-bd btnCancel" href="javascript:;">取消</a>
        </div>
    </div>
    <div class="kd-renew-phone-info">
        <div class="kd-renew-phone-info-title">宽带信息</div>
        <div class="kd-renew-con">
            <p>宽带账号：</p>
            <p>套餐名称：<span>20M宽带/1年</span></p>
            <p>
                <span class="sus-tit">有效时间：</span>
                <span class="sus-text font-red">~</span>
            </p>
            <p>安  装 人：</p>
            <p>
                <span class="sus-tit">地　　址：</span>
                <span class="sus-text"></span>
            </p>
            <div class="rk-con">
            </div>
        </div>
        <div class="text-center kd-btn">
            <a class="btn btn-blue" href="javascript:;">立即绑定</a>
            <a class="btn btn-gray reset-cx" href="javascript:;">重新查询</a>
        </div>
    </div>
</div>
							 
<form id="renewForm" action="linkToConfirmPackage" method="post">
    <input type="hidden" id="productId" name="productId"/>
    <input type="hidden" id="packageId" name="packageId"/>
    <input type="hidden" id="price" name="price"/>
    <input type="hidden" id="goodsSkuId" name="goodsSkuId"/>
    <input type="hidden" id="broadbandAccount" name="broadbandAccount" value="${broadbandInfo.broadbandAccount}"/>
    <input type="hidden" id="startTime" name="startTime" value="${broadbandInfo.startTime}"/>
    <input type="hidden" id="endTime" name="endTime" value="${broadbandInfo.endTime}"/>
    <input type="hidden" id="prodName" name="prodName" value="${broadbandInfo.prodName}"/>
    <input type="hidden" id="custName" name="custName" value="${broadbandInfo.custName}"/>
    <input type="hidden" id="broadbandSpeed" name="broadbandSpeed" value="${broadbandInfo.broadbandSpeed}"/>
    <input type="hidden" id="broadbandContract" name="broadbandContract" value="${broadbandInfo.broadbandContract}"/>
    <input type="hidden" id="installAddress" name="installAddress" value="${broadbandInfo.installAddress}"/>
    <input type="hidden" id="eparchyCode" name="eparchyCode" value="${broadbandInfo.eparchyCode}"/>
    
    
</form>

<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
    $(function () {

        //选择宽带套餐
        $("#broadbandListUl li").on("click",function () {
            var productId = $(this).find("input[name='productId']").val();
            var packageId = $(this).find("input[name='packageId']").val();
            var price = Number($(this).find("input[name='price']").val());
            var speed = Number($(this).find("input[name='speed']").val());
            var skuId = $(this).find("input[name='skuId']").val();

            $("#productId").val(productId);
            $("#packageId").val(packageId);
            $("#price").val(price*100);
            $("#goodsSkuId").val(skuId);
            $("#priceText").text("￥" + price.toFixed(2));
        });

        //立即续费
        $("#renewBtn").on("click",function(){
            showLoadPop();
            if($("#broadbandListUl li.on").length == 0){
                return;
            }
            $("#renewForm").submit();
        });

    });

</script>
</body>
</html>