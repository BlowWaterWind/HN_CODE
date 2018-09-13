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
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>

    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <script>
        var baseProject = "${ctx}" ;
        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
                + "wxyz0123456789+/" + "=";
        $(function () {
            //停机
            $('#toSuspend').on('click', function () {
                var date=$("#endTime").val();
                //解决ios不兼容问题
                if(date.length>0){
                    date=date.replace(/-/g,"/");
                }
                var curDate=Date.parse(new Date());
                if(curDate < Date.parse(new Date(date))){
                    $('#myModal').modal('show');
                    return;
                }
                $('#myModal2').modal('show');
            });

            //停机
            $('#suspend').on('click', function () {
                $('#myModal7').modal('show');
                $("#status").val("0");
            });

            //复机
            $('#restart').on('click', function () {
                $('#myModal7').modal('show');
                $("#status").val("1");
            });

            //复机
            $('#toRestart').on('click', function () {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    async: false,
                    data:{serialNumber : $("#serialNumber").val()},
                    url: ctx+"/o2oMyBroadband/feeDet",
                    success: function (data) {
                        if(data.respCode== '0'){
                            $('#myModal5').modal('show');
                        }else{
                            $('#myModal4').modal('show');
                        }
                    },
                    error: function (data) {
                        $('#myModal6').modal('show');
                    }
                });
            });

            //提速
            $('#speedup').on('click', function () {
                var rate=$("#oldBandWidth").val();
                if(rate>=100||rate.indexOf("100")>0 ){
                    showAlert("您的宽带已经是最高速率");
                    return;
                }
               $("#form1").submit();
            });

            //移机页面
            $('#move').on('click', function () {
                $("#form2").submit();
            });
        });

        function updUserStatus(){
            var bandAccount=$("#broadbandAccount").val();
            var serialNumber=$("#serialNumber").val();
            var status=$("#status").val();
            var password = encode64($("#password").val());
            $.ajax({
                type: "POST",
                dataType: "json",
                async: false,
                data : {bandAccount : bandAccount,
                    status : status,
                    serialNumber :serialNumber,
                    password:password
                },
                url: ctx+"/o2oMyBroadband/updUserStatus",
                success: function (data) {
                    window.location.href = ctx+"/o2oMyBroadband/result?respCode="+data.respCode;
                },
                error: function (data) {
                    $('#myModal6').modal('show');
                }
            });
        }
        function encode64(password) {

            return strEnc(password, keyStr);
        }


    </script>
    <title>我的宽带</title>
</head>
<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>我的宽带</h1>
    </div>
</div>
<div class="container">
    <form action="${ctx}/o2oSpeedUp/confirmAccount" id="form1" name="form1" method="post" >
        <input type="hidden" id="broadbandAccount" name="bandAccount" value="${broadband.accessAcct}"/>
        <input type="hidden" id="oldBandWidth" name="oldBandWidth" value="${broadband.rate}"/>
        <input type="hidden" name="serialNumber" id="serialNumber" value="${serialNumber}"/>
        <input type="hidden" id="endTime"  name="endTime" value="${broadband.endTime}"/>
        <input type="hidden" id="status" name="status"/>
    </form>
    <form action="${ctx}/o2oMyBroadband/toMove" id="form2" name="form2" method="post" >
        <input type="hidden"  name="accessAcct" value="${broadband.accessAcct}"/>
        <input type="hidden"  name="endTime" value="${broadband.endTime}"/>
        <input type="hidden" name="discntName" value="${broadband.discntName}"/>
        <input type="hidden" name="addrDesc" value="${broadband.addrDesc}"/>
        <input type="hidden" name="startTime" value="${broadband.startTime}"/>
        <input type="hidden" name="addrId" id="addrId" value="${broadband.addrId}"/>
        <input type="hidden" name="serialNumber" value="${serialNumber}"/>
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
        <a href="javascript:void(0)" class="btn btn-blue" id="speedup">立即提速</a>
    </div>
    <div class="broadband-foot">
        <a href="javascript:void(0);" id="move">宽带移机</a>
        <c:choose>
            <c:when test="${broadband.userStateCode eq '0'}">
                <a href="javascript:void(0)" data-toggle="modal"  id="toSuspend">宽带停机</a>
            </c:when>
            <c:otherwise>
                <a href="javascript:void(0)" data-toggle="modal"  id="toRestart">宽带复机</a>
            </c:otherwise>
        </c:choose>
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

<!--宽带停机 弹框2 start-->
<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text">
                <p>您好。您目前正在办理宽带停机业务，请确认是否办理。</p>
                <p>资费：<span class="font-red">5元/月</span></p>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal" id="suspend">确认办理</a>
            </div>
        </div>
    </div>
</div>
<!--宽带停机 弹框2 end-->

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

<!--宽带复机 弹框5 start-->
<div class="modal fade modal-prompt" id="myModal5" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text">
                <p>您好。您目前正在办理宽带复机业务，办理后即日生效，请确认办理！</p>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal" id="restart">确认办理</a>
            </div>
        </div>
    </div>
</div>
<!--宽带复机 弹框5 end-->

<div class="modal fade modal-prompt" id="myModal6" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text">
                <p>系统繁忙，请稍后再试！</p>
            </div>
            <div class="dialog-btn"><a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a></div>
        </div>
    </div>
</div>

<!--输入密码弹框 start-->
<div class="modal modal-prompt" id="myModal7" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog channel-modal">
        <div class="modal-info">
            <h4>请输入密码</h4>
            <div class="modal-txt">
                <p><input type="password" class="form-control" name="password" id="password" placeholder="请输入密码"/></p>
            </div>
            <div class="modal-btn">
                <a href="javascript:void(0);" data-dismiss="modal" onclick="updUserStatus()">确定</a>
            </div>
        </div>
    </div>
</div>
<!--输入密码弹框 end-->
</body>
</html>
