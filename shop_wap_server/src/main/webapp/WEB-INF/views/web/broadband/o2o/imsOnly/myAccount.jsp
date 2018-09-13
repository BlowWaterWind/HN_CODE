<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>

    <title>宽带信息</title>
</head>
<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带信息</h1>
    </div>
</div>
<div class="container">

    <form action="${ctx}/o2oImsOnly/index" id="form2" name="form2" method="post" >
        <input type="hidden" name="custName" value="${broadband.custName}"/>
        <input type="hidden" name="addrDesc" value="${broadband.addrDesc}"/>
        <input type="hidden" name="addrId" id="addrId" value="${broadband.addrId}"/>
        <input type="hidden"  id="installPhoneNum" name="installPhoneNum"  value="${installPhoneNum}" />
    </form>
    <div class="broadband-con">
        <div class="broadband-list">
            <div class="broadband-title"><span>个人信息</span></div>
            <div class="broadband-info">
                <p>
                    <label>姓&emsp;名：</label><span>${broadband.custName}</span></p>
                <p>
                    <label>身份证：</label><span>${broadband.psptId}</span></p>
            </div>
        </div>
        <div class="broadband-list">
            <div class="broadband-title"><span>宽带信息</span></div>
            <div class="broadband-info">
                <p class="border-line">宽带账号：<span>${broadband.accessAcct}</span></p>
                <ul class="broadband-box">
                    <li>
                        <label class="font-gray">套餐名称：</label><span>${broadband.discntName}</span></li>
                    <%--<li>--%>
                    <%--<label class="font-gray">资&emsp;&emsp;费：</label><span>48元/月</span></li>--%>
                    <li>
                        <label class="font-gray">宽带速率：</label><span>${broadband.rate}M光宽带</span></li>
                    <li>
                        <label class="font-gray">安装时间：</label><span>${broadband.startTime}</span></li>
                    <li>
                        <label class="font-gray">到期时间：</label><span>${broadband.endTime}</span></li>
                    <li>
                        <label class="font-gray">安装地址：</label><span>${broadband.addrDesc}</span></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="broad-btn">
        <a href="javascript:void(0);" class="btn btn-blue" id="addIms">加装固话</a>
    </div>
</div>
<%--<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>--%>
<!--宽带停机 弹框1 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text">
                <p>您好。您当前使用的宽带合约未到期，不可办理停机业务。</p>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a>
            </div>
        </div>
    </div>
</div>
<!--宽带停机 弹框1 end-->

<!--宽带停机 弹框2 end-->

<!--宽带拆机 弹框3 end-->


<!--宽带复机 弹框4 start-->
<div class="modal fade modal-prompt" id="myModal4" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text">
                <p>您好。您当前账户已欠费，请充值缴费后再进行办理！</p>
            </div>
            <div class="dialog-btn"><a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a></div>
        </div>
    </div>
</div>
<!--宽带复机 弹框4 end-->

</body>
<script>
    $("#addIms").click(function(){
        $("#form2").submit();
    });
</script>
</html>
