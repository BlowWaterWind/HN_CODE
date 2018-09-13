<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
    <!-- 宽带多级地址搜索框 -->
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>和家庭详情</h1>
    </div>
</div>
<div class="container">
    <form action="toInstall" id="form1" name="form1" method="post">
        <input type="hidden" name="bandWidth" id="bandWidth"/>
        <input type="hidden" name="goodsSkuId" id="goodsSkuId"/>
        <input type="hidden" name="goodsName" id="goodsName"/>
        <input type="hidden" name="price" id="price"/>
        <input type="hidden" name="productId" id="productId"/>
        <input type="hidden" id="busiType" name="busiType" value="${busiType}"/>
        <input type="hidden" name="isMbh" id="isMbh" value="${isMbh}"/>
        <input type="hidden" name="isBroadband" id="isBroadBand" value="${isBroadBand}"/>
        <input type="hidden" id="installPhoneNum" name="installPhoneNum" value="${installPhoneNum}"/>
        <input type="hidden" id="homeData" name="homeData"/>
        <input type="hidden" id="homeVoice" name="homeVoice"/>
        <input type="hidden" id="giveData" name="giveData"/>
        <input type="hidden" id="giveMbh" name="giveMbh"/>
        <input type="hidden" id="discntCode" name="discntCode" />
    </form>
    <!--套餐详情 start-->
    <div class="hjt-list">
        <div class="hjt-title">套餐详情</div>
        <div class="hjt-process"><span>可办理</span><span class="process-blue">已选套餐</span><span class="process-gray">不可办理</span></div>
        <table class="hjt-table">
            <thead>
            <tr>
                <th colspan="7" class="th-blue">新版和家庭套餐资费</th>
            </tr>
            <tr>
                <th>价格</th>
                <th>国内流量</th>
                <th>国内语音<br>(分钟)</th>
                <th>宽带</th>
                <th>魔百和</th>
                <%--<th>赠送<br>省内流量</th>--%>
                <th class="font-red">套餐选择</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${broadItemList}" var="broadbandItem">
                <tr>
                    <td class="tr-blue" title="price">${broadbandItem.discntCode}</td>
                    <td class="tr-blue" title="homeData">不限量（${broadbandItem.homeData}G不限速）</td>
                    <td class="tr-blue" title="homeVoice">${broadbandItem.homeVoice}</td>
                    <td class="tr-blue" title="bandWidth">${broadbandItem.bandWidth}M</td>
                    <td class="tr-blue" title="mbh">
                        <c:if test="${broadbandItem.mbh!=0}"> ${broadbandItem.mbh}元/月</c:if>
                        <c:if test="${broadbandItem.mbh==0}"> 免费赠送</c:if>
                    </td>
                        <%--<td class="tr-blue" title="giveData">--%>
                        <%--<c:if test="${broadbandItem.giveData!=null&&broadbandItem.giveData!=''}"> ${broadbandItem.giveData}</c:if>--%>
                        <%--<c:if test="${broadbandItem.giveData==null||broadbandItem.giveData==''}"> 流量不限量</c:if>--%>
                        <%--</td>--%>
                    <td class="tr-white"><input type="radio" name="radio" /></td>
                    <input type="hidden" id="skuId" name="skuId" value="${broadbandItem.goodsSkuId}"/>
                    <input type="hidden" id="goodsId" name="goodsId" value="${broadbandItem.goodsId}"/>
                    <input type="hidden" id="chooseBandWidth" name="chooseBandWidth" value="${broadbandItem.bandWidth}"/>
                    <input type="hidden" id="item_price" name="item_price" value="${broadbandItem.price}"/>
                    <input type="hidden" name="item_productId" value="${broadbandItem.productId}"/>
                        <%--<c:if test="${hasPackage==false}">--%>
                        <%--</c:if>--%>
                        <%--<c:if test="${hasPackage!=false}">--%>
                    <input type="hidden" name="item_discntCode" value="${broadbandItem.discntCode}"/>
                        <%--</c:if>--%>

                    <input type="hidden" name="desc" value="${broadbandItem.desc}"/>
                    <input type="hidden" name="mbh" value="${broadbandItem.mbh}"/>
                    <input type="hidden" name="broadbandItemName" value="${broadbandItem.broadbandItemName}"/>
                    <input type="hidden" name="item_giveData" value="${broadbandItem.giveData}"/>
                    <input type="hidden" name="item_homeVoice" value="${broadbandItem.homeVoice}"/>
                    <input type="hidden" name="item_homeData" value="${broadbandItem.homeData}"/>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <!--当前套餐余额 start-->
        <div class="hjt-balance">
            <div class="balance-info">
                <c:if test="${hasPackage==true}">
                    <p><span>已办理套餐</span></p>
                </c:if>
                <c:if test="${hasPackage!=true}">
                    <p><span>未办理套餐</span></p>
                </c:if>
                <%--<p>当前余额：<span>${balance}元</span></p>--%>
                <p>套餐档次：￥${curentPackage}元/月,${packageName}</p>
            </div>
            <p class="balance-tip" id="warnDesc"></p>
        </div>
        <!--当前套餐余额 end-->
    </div>
    <!--套餐详情 end-->


    <!--业务规则 start-->
    <div class="hjt-list">
        <div class="hjt-title">业务规则</div>
        <div class="hjt-rules">
            <div class="rules-list">
                <p>（1）WAP不限量套餐、存在共享关系的用户不能办理。</p>
                <p>（2）和家庭套餐用户12个月内不能转出</p>
                <p>（3）当月套内国内流量使用达到不限速流量后，网速降至1Mbps，超过100GB后网速降至128Kbps。</p>
                <p>（4）办理任意档次，可免费获赠50M-100M宽带，办理129元档次及以上可免费获赠魔百和电视。</p>
                <p>（5）未尽事宜，保留最终解释权。</p>
            </div>
        </div>
    </div>
    <!--业务规则 end-->

</div>
<!--尾部 start-->
<div class="fix-br new-fixed">
    <div class="affix foot-box">
        <div class="container active-fix">
            <div class="fl-left">
                <p class="ft-total"><span class="font-gray6" id="span_total">20元/月</span></p>
            </div>
            <div class="fl-right"><a href="javascript:void(0);" class="" id="submitOrder">
                立即订购
            </a></div>
            <!--当按钮为灰色时 a class 加 btn-gray-->
        </div>
    </div>
</div>
<!--尾部 end-->
<%--<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>--%>
<%--<script type="text/javascript" src="js/rem.js"></script>--%>
<script type="text/javascript">
    $(document).ready(function(){
        //当前套餐档期确定能赠送流量的档次
        var package_code = parseInt('${package_code}');
        var hasPackage = '${hasPackage}';
        $("tbody>tr").click(function(){
            $(this).find(":radio").attr("checked",true)
                    .end().find("td").addClass("selected")
                    .end().siblings().find("td").removeClass("selected");
//            if(!$(this).find("td[title='giveData']").hasClass("tr-disabled")){
//                $(this).find("td[title='giveData']").addClass("selected").end().siblings().find("td[title='giveData']").removeClass("selected");
//            }
            var skuId = $(this).find("input[name='skuId']").val();
            var chooseBandWidth = $(this).find("input[name='chooseBandWidth']").val();
            var price = parseFloat($(this).find("input[name='item_price']").val());
            var mbh = parseFloat($(this).find("input[name='mbh']").val());
            var giveData = $(this).find("input[name='item_giveData']").val();
            var homeVoice = parseFloat($(this).find("input[name='item_homeVoice']").val());
            var homeData = $(this).find("input[name='item_homeData']").val();
            var productId = $(this).find("input[name='item_productId']").val();
            var discntCode = parseFloat($(this).find("input[name='item_discntCode']").val());
            var desc1=$(this).find("input[name='desc']").val();
            var broadbandItemName=$(this).find("input[name='broadbandItemName']").val();
            $("#goodsSkuId").val(skuId);
            $("#bandWidth").val(chooseBandWidth);
            $("#productId").val(productId);
            $("#discntCode").val(discntCode);
            $("#goodsName").val(broadbandItemName);
            $("#price").val(price);
            //$("#span_total").html(price+"元");
            if(package_code==productId){
                $("#span_total").html(mbh+"元/月");
                $("#submitOrder").html("补办宽带")
            }else {
                $("#span_total").html((discntCode+mbh)+"元/月");
                $("#submitOrder").html("立即订购")
            }
            $("#homeData").val(homeData);
            $("#homeVoice").val(homeVoice);

            $("#giveMbh").val(mbh);
        });

        $("#submitOrder").click(function(){
            if($("#productId").val()==''){
                showAlert("请选择档次!");
                return;
            }
            $("#form1").submit();
        })
        if(hasPackage=='true') {
            changeClass(package_code);
        }

    });
    function changeClass(limit){
        $.each($("tbody>tr>input[name='item_productId']"),function(i,v){
            var productId  = $(this).val();
            if(productId==limit){
                $(this).parent().click();
            }
        });
    }
</script>
</body>

</html>