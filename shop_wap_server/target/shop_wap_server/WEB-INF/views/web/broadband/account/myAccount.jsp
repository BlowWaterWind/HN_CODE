<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KDWD" />
    <meta name="WT.si_x" content="20" />
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp"%>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>

    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
    <script>
        $(function () {
            //停机
            $('#suspend').on('click', function () {
                var date=$("#endTime").val();
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

            //复机
            $('#restart').on('click', function () {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    async: false,
                    url: ctx+"/myBroadband/feeDet",
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
                /*插码*/
                try{
                    var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n",sin,
                                "WT.si_x","21"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n",sin,true,
                                    "WT.si_x","21",true);
                        }
                    }
                }catch (e){
                    console.log(e);
                }
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

            //拆机
            $('#destroy').on('click', function () {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    async: false,
                    data : {
                        serialNumber : $("#broadbandAccount").val(),
                        bookDate: $("#bookDate").val(),
                        bookSession: $("#bookSession").val()
                    },
                    url: ctx+"/myBroadband/destroy",
                    success: function (data) {
                        window.location.href = ctx+"/myBroadband/result?respCode="+data.respCode
                    },
                    error: function (data) {
                        $('#myModal6').modal('show');
                    }
                });
            });

            //拆机
            $('#toDestory').on('click', function () {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    async: false,
                    data : {
                        addrId: $("#addrId").val(),
                    },
                    url: ctx+"/myBroadband/queryAddr",
                    success: function (data) {
                        if(data.respCode == '0' && data.bookInstallDateList.length>0){
                            $("#bookDate").empty();
                            $.each(data.bookInstallDateList, function (i, item) {
                                $("#bookDate").append( "<option value='"+item+"'>"+item+"</option>");
                            });
                        }
                    },
                    error: function (data) {

                    }
                });
            });
        });
        function updUserStatus(status){
            var bandAccount=$("#broadbandAccount").val();
            $.ajax({
                type: "POST",
                dataType: "json",
                async: false,
                data : {bandAccount : bandAccount,
                    status : status
                },
                url: ctx+"/myBroadband/updUserStatus",
                success: function (data) {
                    window.location.href = ctx+"/myBroadband/result?respCode="+data.respCode;
                },
                error: function (data) {
                    $('#myModal6').modal('show');
                }
            });
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
    <form action="${ctx}/broadbandSpeedUp/confirmAccount" id="form1" name="form1" method="post" >
        <input type="hidden" id="broadbandAccount" name="bandAccount" value="${broadband.accessAcct}"/>
        <input type="hidden" id="oldBandWidth" name="oldBandWidth" value="${broadband.rate}"/>
        <input type="hidden" id="endTime"  name="endTime" value="${broadband.endTime}"/>
        <%--<input type="status" id="status" name="status" value="${broadband.userStateCode}"/>--%>
        <input type="hidden" name="discntName" value="${broadband.discntName}"/>
        <input type="hidden" name="addrDesc" value="${broadband.addrDesc}"/>
        <input type="hidden" name="startTime" value="${broadband.startTime}"/>

    </form>

    <form action="${ctx}/myBroadband/toMove" id="form2" name="form2" method="post" >
        <input type="hidden"  name="accessAcct" value="${broadband.accessAcct}"/>
        <input type="hidden"  name="endTime" value="${broadband.endTime}"/>
        <input type="hidden" name="discntName" value="${broadband.discntName}"/>
        <input type="hidden" name="addrDesc" value="${broadband.addrDesc}"/>
        <input type="hidden" name="startTime" value="${broadband.startTime}"/>
        <input type="hidden" name="addrId" id="addrId" value="${broadband.addrId}"/>
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
        <a href="javascript:void(0);" class="btn btn-blue" id="speedup">立即提速</a>
    </div>
    <div class="broadband-foot">
        <a href="javascript:void(0);" id="move">宽带移机</a>
        <c:choose>
            <c:when test="${broadband.userStateCode eq '0'}">
                <a href="javascript:void(0)" data-toggle="modal"  id="suspend">宽带停机</a>
            </c:when>
            <c:otherwise>
                <a href="javascript:void(0)" data-toggle="modal"  id="restart">宽带复机</a>
            </c:otherwise>
        </c:choose>
        <a href="javascript:void(0)" data-toggle="modal" data-target="#myModal7" id="toDestory">宽带拆机</a>
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
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal" onclick="updUserStatus('0')">确认办理</a>
            </div>
        </div>
    </div>
</div>
<!--宽带停机 弹框2 end-->

<!--宽带拆机 弹框3 start-->
<div class="modal fade modal-prompt" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-text">
                <p>您好。您正在申请移动宽带拆机业务，请选择拆机原因。</p>
                <ul class="dialog-list">
                    <li>
                        <label><input type="radio" name="radio" />移机地址无资源覆盖</label>
                    </li>
                    <li>
                        <label><input type="radio" name="radio" />宽带业务存在严重质量问题</label>
                    </li>
                </ul>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认申请</a>
            </div>
        </div>
    </div>
</div>
<!--宽带拆机 弹框3 end-->

<!--选择时间 弹框 start-->
<div class="modal fade modal-prompt" id="myModal7" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                <h4 class="modal-title">预约时间</h4>
            </div>
            <ul class="modal-form">
                <li>
                    <span class="rt-title">日期选择：</span>
                    <div class="modal-rt">
                        <select class="form-control" name="bookDate" id="bookDate">
                            <c:forEach items="${bookInstallDateList}" var="bookDate">
                                <option value="${bookDate}">${bookDate}</option>
                            </c:forEach>
                        </select>
                    </div>
                </li>
                <li>
                    <span class="rt-title">时间段选择：</span>
                    <div class="modal-rt">
                        <select class="form-control" name="bookSession" id="bookSession">
                            <option value="0">上午8：00-12：00</option>
                            <option value="1">中午12：00-14：00</option>
                            <option value="2">下午14：00-18：00</option>
                            <option value="3">晚上18：00-20：00</option>
                        </select>
                    </div>
                </li>
            </ul>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal" id="destroy">确认申请</a>
            </div>
        </div>
    </div>
</div>

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
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal" onclick="updUserStatus('1')">确认办理</a>
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
</body>
</html>
