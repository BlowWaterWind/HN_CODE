<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_n" content="固话加装" />
    <meta name="WT.si_x" content="填写地址" />
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/kd.js"></script>

    <!-- 宽带新装相关js -->
    <%--<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/heBroadbandInstall.js"></script>--%>
    <!-- 搜索地址框placeholder处理 -->
    <style>
        /* WebKit browsers */
        input:focus::-webkit-input-placeholder { color:transparent; }
        /* Mozilla Firefox 4 to 18 */
        input:focus:-moz-placeholder { color:transparent; }
        /* Mozilla Firefox 19+ */
        input:focus::-moz-placeholder { color:transparent; }
        /* Internet Explorer 10+ */
        input:focus:-ms-input-placeholder { color:transparent; }
    </style>

    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
        var  ctx = '${ctx}';
    </script>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>和家庭</h1>
    </div>
</div>--%>
<c:set value="套餐选择" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<div class="container bg-gray hy-tab">
    <form action="imsChoose" id="form1" name="form1" method="post">
        <input type="hidden" id="form1_skuId" name="form1_skuId"  />
        <input type="hidden"  id="form1_productId" name="form1_productId"  />
        <input type="hidden"  id="form1_packageId" name="form1_packageId"  />
        <input type="hidden"  id="form1_goodsId" name="form1_goodsId"  />
        <input type="hidden"  id="form1_goodsName" name="form1_goodsName"  />
        <input type="hidden"  id="form1_level" name="form1_level"  />
        <input type="hidden"  id="form1_custName" name="form1_custName" value="${custName}"/>
        <input type="hidden"  id="form1_addressName" name="form1_addressName" value="${addrDesc}"/>
        <input type="hidden"  id="form1_houseCode" name="form1_houseCode" value="${addrId}"/>
        <input type="hidden"  id="installPhoneNum" name="installPhoneNum"  value="${installPhoneNum}" />

    </form>
    <div class="wf-list tab-con">
    <!--选择套餐 start-->
    <div class="change-tc">
        <!--宽带新装金额 start-->
        <h2 class="renew-title">请选择新装套餐</h2>

        <ul class="add_oil select-renew clearfix" >
            <c:forEach items="${heBroadbandItemList}" var="broadbandItem" varStatus="status">
                <li name="li_broadbandItem" price="${broadbandItem.price}">${broadbandItem.productLevel}元/月
                    <p class="font-gray">1元加装固话</p>
                    <s></s>
                    <input type="hidden" id="skuId" name="skuId" value="${broadbandItem.goodsSkuId}"/>
                    <input type="hidden" id="goodsId" name="goodsId" value="${broadbandItem.goodsId}"/>
                    <input type="hidden" name="productId" value="${broadbandItem.productId}"/>
                    <input type="hidden" name="packageId" value="${broadbandItem.packageId}"/>
                    <input type="hidden" name="productLevel" value="${broadbandItem.productLevel}"/>
                    <input type="hidden" name="desc" value="${broadbandItem.desc}"/>
                    <input type="hidden" name="goodsName" value="${broadbandItem.broadbandItemName}"/>
                    <c:if test="${not empty broadbandItem.labelName}">
                        <span class="tip tip-rose">${broadbandItem.broadbandItemName}</span>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
        <!--充值金额 end-->
        <div class="renew-content nofiexd">
            <ul class="ren-tit clearfix">
                <li class="clearfix bg-grayf2">
                    <div class="cp-con"><span class="renew-tip">含</span><span id="desc1" class="renew-tip2"></span></div>
                </li>
            </ul>
        </div>
        <!--选择套餐 end-->
        </div>
    </div>
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div id="div_settlement" class="container active-fix kd-disabled">
            <div class="fl-left">
                <p class="p1">应付总额：<b class="cl-rose" id="b_total">￥<span id="span_total1">0.00</span></b></p>
            </div>
            <div class="fl-right"><a href="javascript:;" id="a_confirmInstall" otitle="立即订购" otype="button">立即订购</a></div>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>

<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
<script>
    /*选择新装宽带套餐*/
    $("li[name='li_broadbandItem']").bind("click",function(){

        var skuId = $(this).find("input[name='skuId']").val();
        var price = parseFloat($(this).find("input[name='price']").val());
        var productId = $(this).find("input[name='productId']").val();
        var packageId = $(this).find("input[name='packageId']").val();
        var productLevel = $(this).find("input[name='productLevel']").val();
        var goodsName = $(this).find("input[name='goodsName']").val();
        var desc1=$(this).find("input[name='desc']").val();
        var goodsId=$(this).find("input[name='goodsId']").val();
        $("#form1_skuId").val(skuId);
        $("#form1_productId").val(productId);
        $("#form1_level").val(productLevel);
        $("#form1_packageId").val(packageId);
        $("#form1_goodsName").val(goodsName);
        $("#form1_goodsId").val(goodsId);
        $("#span_total").text(price);
        $("#desc1").text(desc1);
        // $("#div_settlement").attr("class","container active-fix");\
//        if (typeof(dcsPageTrack)=="function"){ dcsPageTrack ("WT.si_n","和家庭",1, "WT.si_x","选择产品档次"+chooseBandWidth+"M "+$(this).attr("price")+"元/月",1);}
    });

    /**
     *  确认购买
     */
    $("#a_confirmInstall").bind("click",function(){
//        if (typeof(dcsPageTrack)=="function"){dcsPageTrack("WT.si_n","和家庭宽带",0,"WT.si_x","和家庭宽带办理",0)}
        if($("#form1_skuId").val()==""){
            showAlert("请选择固话产品!");
            return ;
        }
        showLoadPop();
        $("#form1").submit();
    });


</script>
</body>
</html>