<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
    <script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>

    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>
    <script>
        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
                + "wxyz0123456789+/" + "=";
        var ctx=${ctx};
    </script>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>办理详情</h1>
    </div>
</div>
<div class="container">
    <form action="submitTempOrder" id="form1" name="form1" method="post">
        <input type="hidden" id="goodsSkuId" name="goodsSkuId"  />
        <input type="hidden" id="goodsId" name="goodsId"/>
        <input type="hidden" id="goodsName" name="goodsName"/>
        <input type="hidden" id="productId" name="productId"/>
        <input type="hidden" id="packageId" name="packageId"/>
        <input type="hidden" id="price" name="goodsPrice"/>
        <input type="hidden" id="serialNumber" name="serialNumber" value="${installPhoneNum}"/>
        <input type="hidden" id="cityCode" name="cityCode" value="${eparchy_Code}"/>
        <input type="hidden" id="recordType" name="recordType" value="${type}"/>
        <input type="hidden" id="staffPwd" name="staffPwd" />
    </form>
    <!--套餐详情 start-->
    <div class="hjt-list">
        <div class="hjt-title">套餐详情</div>
        <div class="hjt-process"><span>可办理</span><span class="process-blue">已选套餐</span><span class="process-gray">不可办理</span></div>
        <table class="hjt-table">
            <thead>
            <tr>
                <th colspan="7" class="th-blue">宽带电视年包资费</th>
            </tr>
            <tr>
                <th>标准价（元）</th>
                <th>产品内容</th>
                <th>折扣促销价（元）</th>
                <th class="font-red">套餐选择</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${heBroadbandItemList}" var="broadbandItem">
                <tr>
                    <td class="tr-blue" title="standarPrice">${broadbandItem.standarPrice}</td>
                    <td class="tr-blue" title="productDesc">${broadbandItem.productDesc}</td>
                    <td class="tr-blue" title="disaccountPrice"><fmt:formatNumber value="${broadbandItem.price/100}" type="number"  /></td>
                    <td class="tr-white">
                        <input type="radio" name="radio" />
                    </td>
                    <input type="hidden" name="skuId" value="${broadbandItem.goodsSkuId}"/>
                    <input type="hidden" name="goods_Id" value="${broadbandItem.goodsId}"/>
                    <input type="hidden" name="price_form" value="${broadbandItem.price}"/>
                    <input type="hidden" name="product_Id" value="${broadbandItem.productId}"/>
                    <input type="hidden" name="package_Id" value="${broadbandItem.packageId}"/>
                    <input type="hidden" name="goods_name" value="${broadbandItem.broadbandItemName}"/>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <!--当前套餐余额 start-->
        <div class="hjt-balance">
            <div class="balance-info">
                <%--<p>存费年包到期时间:2018-1-1</p>--%>
                <p>套餐档次:${curentPackage}元/月</p>
            </div>
        </div>
        <!--当前套餐余额 end-->
    </div>
    <!--套餐详情 end-->
    <!--业务规则 start-->
    <div class="hjt-list">
        <div class="hjt-title">业务规则</div>
        <div class="hjt-rules">
            <div class="rules-list">
                <%--<p>1、年包到期后30元/月、电视20/元月，到期后可制成办理宽带及电视优惠活动；</p>--%>
                <%--<p>2、宽带与手机号码绑定，同开同停；</p>--%>
            </div>
        </div>
    </div>
    <!--业务规则 end-->
</div>
<!--尾部 start-->
<div class="fix-br hjt-fixed">
    <div class="affix foot-box">
        <div class="container active-fix">
            <div class="fl-left">
                <p class="ft-total">价格：<span class="font-gray6" id="span_total">￥0元</span></p>
                <p class="zfs-txt">支付方式:<span class="font-gray6">余额抵扣</span></p>
            </div>
            <div class="fl-right"><a href="javascript:void(0);" class="btn-red" id="submitOrder">立即办理</a></div>
            <!--当按钮为灰色时 a class 加 btn-gray-->
        </div>
    </div>
</div>
<!--尾部 end-->


<!--输入密码弹框 start-->
<div class="modal modal-prompt" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog channel-modal">
        <div class="modal-info">
            <h4>请输入密码</h4>
            <div class="modal-txt">
                <p><input type="password" class="form-control" name="password" id="password" placeholder="请输入密码"/></p>
            </div>
            <div class="modal-btn">
                <a href="javascript:void(0);" data-dismiss="modal" id="handleCommit">确定</a>
            </div>
        </div>
    </div>
</div>
<!--输入密码弹框 end-->

<script type="text/javascript">
    $(document).ready(function() {
        $("tbody>tr").click(function () {
            $(this).find(":radio").attr("checked", true)
                    .end().find("td:not([title='disaccountPrice'])").addClass("selected")
                    .end().siblings().find("td").removeClass("selected");
            if (!$(this).find("td[title='disaccountPrice']").hasClass("tr-disabled")) {
                $(this).find("td[title='disaccountPrice']").addClass("selected").end().siblings().find("td[title='disaccountPrice']").removeClass("selected");
            }
            var skuId = $(this).find("input[name='skuId']").val();
            var goods_Id = $(this).find("input[name='goods_Id']").val();
            var price = parseFloat($(this).find("input[name='price_form']").val());
            var productId = $(this).find("input[name='product_Id']").val();
            var packageId = $(this).find("input[name='package_Id']").val();
            var goodsName = $(this).find("input[name='goods_name']").val();
            $("#goodsSkuId").val(skuId);
            $("#goodsId").val(goods_Id);
            $("#goodsName").val(goodsName);
            $("#packageId").val(packageId);
            $("#productId").val(productId);
            $("#price").val(price);
            $("#span_total").html("￥" + price/100+ "元");
        });

        $("#submitOrder").click(function () {
            if ($("#goodsSkuId").val() == '') {
                showAlert("请选择套餐!");
                return;
            }
            $('#myModal1').modal('show');
        })

        $("#handleCommit").click(function() {
            $("#submitOrder").addClass('btn-gray');
            var password = encode64($("#password").val());
            $("#staffPwd").val(password);
            $.ajax({
                type : 'post',
                url :  ctx+'/o2oMarketing/submitTempOrder',
                cache : false,
                async : false,
                dataType : 'json',
                data :$("#form1").serialize(),
                success : function(data) {
                    if (data.respCode=='0'&& data.orderId !=null) {
                        alert("短信发送成功！");
                        sendSms(data.orderId);
                    } else {
                        showAlert(data.respDesc);
                        $("#submitOrder").removeClass('btn-gray').addClass('btn-red');
                        return false;
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    showAlert("服务中断，请重试");
                    $("#submitOrder").removeClass('btn-gray').addClass('btn-red');
                    return false;
                }
            });

        });
    });

    //发送短信
    function sendSms(orderId){
        $.ajax({
            type : 'post',
            url :  ctx+'/o2oBusiness/invokeSmsInterface',
            cache : false,
            async : false,
            dataType : 'json',
            data : {
                goodsName : $("#goodsName").val(),
                serialNumber: $("#serialNumber").val(),
                broadbandType: '23',
                orderId: orderId
            },
            success : function(data) {
                if (data.X_RESULTCODE=='0') {
                    window.location.href = ctx+ "/o2oBusiness/smssuccess";
                } else {
                    showAlert(data.X_RESULTINFO);
                    return false;
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
                $("#submitOrder").removeClass('btn-gray').addClass('btn-red');
                return false;
            }
        });
    }
    function encode64(password) {
        return strEnc(password, keyStr);
    }
</script>
</body>
</html>