<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KD_SDD" />
    <meta name="WT.si_x" content="20" />
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/order/date01.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/order/date02.js"></script>

    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>

</head>
<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>我的订单</h1>
    </div>
</div>
<div class="container">
    <div class="tabs broad-tabs" id="content">
    <form action="${ctx}/broadbandOrder/queryOrderList" id="form1" name="form1" method="post">
        <ul class="tab-menu">
            <li id="all"><a href="${ctx}/broadbandOrder/queryOrderList?flag=all">全部</a></li>
            <li id="willPay"><a href="${ctx}/broadbandOrder/queryPayOrderList?flag=willPay">支付相关订单<span class="order-tip">${payNum}</span></a></li>
        </ul>
        <input type="hidden" name="flag" value="${flag}" id="flag"/>
        <input type="hidden" name="startTime" id="startTime" value="${startTime}"/>
        <input type="hidden" name="endTime" id="endTime" value="${endTime}"/>

        <!--订单日期选择 start-->
        <section class="order-date-new">
            <div class="flow-date">
                <label>查询时间：</label>
                <div class="flow-box">
                    <div class="form-date" id="Date01">请选择开始日期</div>
                    <span class="font-gray9">-</span>
                    <div class="form-date" id="Date02">请选择结束日期</div>
                </div>
            </div>
            <div class="section-btn"><a href="javascript:void(0)" class="btn btn-blue" id="query">查询</a></div>
        </section>
        <!--订单日期选择 end-->
    </form>
    <form id="detailForm" name="detailForm" action="${ctx}/broadbandOrder/orderDetail" modelAttribute="broadbandOrder" method="post">
        <input type="hidden" name="offerName" id="offerName"/>
        <input type="hidden" name="tradeStatus" id="tradeStatus"/>
        <input type="hidden" name="tradeId" id="detailTradeId"/>
        <input type="hidden" name="serialNumber" id="detailSerialNumber"/>
        <input type="hidden" name="tradeTypeCode" id="tradeTypeCode"/>
        <input type="hidden" name="tradeFee" id="tradeFee"/>
        <input type="hidden" name="createDate" id="createDate"/>
        <input type="hidden" name="accNbr" id="accNbr"/>
        <input type="hidden" name="eparchyCode" id="eparchyCode"/>
        <input type="hidden" name="preInstallDate" id="preInstallDate"/>
        <input type="hidden" name="tradeOfferNum" id="tradeOfferNum"/>
    </form>
<c:if test="${not empty broadbandOrderList}">
<div class="order-detail">
<c:forEach items="${broadbandOrderList}" var="broadbandOrder">
        <div class="order-list">
            <div class="order-title">
                <p class="font-gray">${broadbandOrder.createDate}</p>
                <p>订单编号：${broadbandOrder.tradeId}</p>
            </div>
            <dl class="detail-list last">
                <dt><img src="${ctxStatic}/demoimages/kdtu.png" /></dt>
                <dd>
                    <h4>${broadbandOrder.tradeType}</h4>
                    <p>${broadbandOrder.offerName}</p>
                    <%--<a href="javascript:void(0)">更多详情</a>--%>
                    <a href="javascript:void(0);" style="font-weight: bold;" onclick="detail('${broadbandOrder.tradeId}','${broadbandOrder.serialNumber}','${broadbandOrder.tradeStatus}','${broadbandOrder.offerName}','${broadbandOrder.tradeTypeCode}','${broadbandOrder.tradeFee}','${broadbandOrder.tradeOfferNum}','${broadbandOrder.createDate}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}','${broadbandOrder.preInstallDate}')">更多详情</a>
                </dd>
            </dl>
            <!--支付金额 start-->
            <div class="order-amount">总额：¥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/><span class="ml20">${broadbandOrder.tradeStatus}</span></div>
            <!--支付金额 end-->
            <div class="order-btn neworder-btn">
                <c:if test="${broadbandOrder.tradeTypeCode eq '1002'}">
                    <c:if test="${broadbandOrder.tradeStatus ne '系统完工'}">
                        <a href="javascript:void(0)" class="btn btn-border-red" onclick="remind('${broadbandOrder.tradeId}','${broadbandOrder.eparchyCode}','${broadbandOrder.accNbr}')">催单</a>
                    </c:if>
                    <c:if test="${not empty broadbandOrder.preInstallDate and broadbandOrder.tradeStatus ne '系统完工'}">
                        <a href="javascript:void(0)" class="btn btn-border-red" onclick="toModifyTime('${broadbandOrder.tradeId}','${broadbandOrder.preInstallDate}','${broadbandOrder.serialNumber}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}')">更改上门安装时间</a>
                    </c:if>
                    <c:if test="${broadbandOrder.tradeStatus eq '系统完工'}">
                        <a href="javascript:void(0)" class="btn btn-border-red" onclick="toComment('${broadbandOrder.tradeId}')">评价</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </c:forEach>
    </div>
</c:if>

</div>
    <!--暂无订单 strat-->
    <c:if test="${ empty broadbandOrderList}">
        <div class="no-orders">
            <img src="${ctxStatic}/images/fa_icon.png" />
            <p>暂未找到相关订单</p>
        </div>
    </c:if>
    <!--暂无订单 end-->
</div>
<!--评价 弹框 start-->
<div class="modal fade modal-prompt" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form id="comment_form" modelAttribute="question"  method="post" action="${ctx}/broadbandOrder/submitComment">
        <input type="hidden" name="orderId" id="orderId" />
        <div class="modal-dialog qdkd-dialog">
            <div class="qdkd-header">
                <h4>评价问题</h4>
                <button data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-con">
                <div class="qdkd-list">

                    <ul class="add-box">
                        <c:forEach items="${questionList}" var="question" varStatus="status">
                            <input type="hidden" name="commentResults[${status.index}].questionId" value="${question.questionId}"/>
                            <li>
                                <p>问题${status.index+1}：${question.questionTitle}</p>
                                <div class="add-radio">
                                    <c:forEach items="${question.options}" var="option" varStatus="oStatus" >
                                        <label><input type="radio" name="commentResults[${status.index}].resultContent" value="${option}"/>${option}</label>
                                    </c:forEach>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="dialog-btn">
                    <a href="javascript:void(0);" class="font-blue" onclick="comment()" id="comment">提交</a>
                    <a href="javascript:void(0);" class="font-blue" id="cancel">返回</a>
                </div>
            </div>
        </div>
    </form>
</div>
<!--评价 弹框 end-->

<!--催单 弹框 start-->
<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog order-dialog">
        <div class="order-header">
            <h4>提示</h4>
            <button data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-con">
            <div class="order-txt">
                <p>催单成功！</p>
                <p>装维人员将尽快与您联系，请保持电话畅通</p>
            </div>
            <div class="dialog-btn">
                <a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a>
            </div>
        </div>
    </div>
</div>
<!--催单 弹框 end-->
<!--选择时间 弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                <h4 class="modal-title">改约时间</h4>
            </div>
            <input type="hidden" id="tradeId" name="tradeId"/>
            <input type="hidden" id="serialNumber" name="serialNumber"/>
            <input type="hidden" id="bandAccount" name="bandAccount"/>

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
                <a href="javascript:void(0)" class="font-blue" data-dismiss="modal" onclick="modifyTime()">确定</a>
            </div>
        </div>
    </div>
</div>
<!--选择时间 弹框 end-->

<script>
    $(function() {
        var flag = $("#flag").val();
        console.info(flag);
        $("#"+flag).addClass("active");
        $("#query").click(function() {
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
            $("#form1").submit();
        });
        $("#cancel").click(function() {
            $('#myModal1').modal('hide');
        });
    });

    function remind(tradeId,eparchyCode,serialNumber){
        $.ajax({
            type : 'post',
            url : ctx+'/broadbandOrder/remind',
            dataType : 'json',
            cache : false,
            data : {
                tradeId : tradeId,cityCode:eparchyCode,serialNumber:serialNumber
            },
            success : function(data) {
                if(data.respCode=='0'){
                    $('#myModal2').modal('show');
                }else{
                    showAlert(data.respDesc);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
            }
        });
    }

    //修改预约时间
    function modifyTime(){
        var bookSession = $("#bookSession").val();
        var bookDate = $("#bookDate").val();
        var tradeId= $("#tradeId").val();
        var serialNumber=$("#serialNumber").val();
        var bandAccount=$("#bandAccount").val();
        $.ajax({
            type : 'post',
            url : ctx+'/broadbandOrder/modifyInstallTime',
            cache : false,
            dataType : 'json',
            data : {bookDate:bookDate,tradeId:tradeId,bookSession:bookSession,serialNumber:serialNumber,bandAccount:bandAccount},
            success : function(data) {
                if(data.respCode=='0'){
                    showAlert("修改成功");
                }else{
                    showAlert(data.respDesc);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
            }
        });
    }

    function toModifyTime(tradeId,installTime,serialNumber,bandAccount,cityCode){
        if(installTime ==null ||installTime==''){
            showAlert("非预约单，不支持改约！");
            return;
        }
        $("#tradeId").val(tradeId);
        $("#serialNumber").val(serialNumber);
        $("#bandAccount").val(bandAccount);
        //判断预约时间是否大于当前时间48/72小时
        var curDate=Date.parse(new Date());
        var installDate=installTime.replace(/-/g,"/");
        if(curDate > Date.parse(new Date(installDate))){//判断预约时间-当前时间>
            showAlert("预约时间已过，不支持改约！");
            return;
        }
        $.ajax({
            type : 'post',
            url : ctx+'/broadbandOrder/queryAddr',
            cache : false,
            dataType : 'json',
            data : {installTime:installTime,cityCode:cityCode,tradeId:tradeId},
            success : function(data) {
                if(data.respCode == '0' && data.bookInstallDateList.length>0){
                    $("#bookDate").empty();
                    $.each(data.bookInstallDateList, function (i, item) {
                        $("#bookDate").append( "<option value='"+item+"'>"+item+"</option>");
                    });
                    $('#myModal').modal('show');
                }else{
                    showAlert(data.respDesc);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
            }
        });
    }

    function toComment(orderId){
        $("#orderId").val(orderId);
        $.ajax({
            type : 'post',
            url : ctx+'broadbandOrder/toComment',
            cache : false,
            dataType : 'json',
            data : {orderId:orderId},
            success : function(data) {
                if(data.list!=null &&data.list.length>0){
                    $.each(data.list, function (i, item) {
                        $("input[value='"+item.resultContent+"']").attr("checked",'checked');
                    });
                    $("#comment").hide();
                }else{
                    $("#comment").show();
                }
                $('#myModal1').modal('show');
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
            }
        });
    }

    function comment(){
        var flag=true;
        $(".add-box li").each(function(i){
            if($(this).find("input[type='radio']:checked").length<1) {
                alert('请填写完整');
                flag=false;
                return false;
            }
        });
        if(!flag){
            return ;
        }
        $.ajax({
            type : 'post',
            url : ctx+'/broadbandOrder/submitComment',
            cache : false,
            dataType : 'json',
            data : $("#comment_form").serialize(),
            success : function(data) {
                $('#myModal1').modal('hide');
                showAlert(data.respDesc);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
            }
        });
    }
    function detail(tradeId,serialNumber,tradeStatus,offerName,tradeTypeCode,tradeFee,tradeOfferNum,createDate,accNbr,eparchyCode,preInstallDate){
        $("#detailTradeId").val(tradeId);
        $("#detailSerialNumber").val(serialNumber);
        $("#tradeStatus").val(tradeStatus);
        $("#offerName").val(offerName);
        $("#tradeTypeCode").val(tradeTypeCode);
        $("#tradeFee").val(tradeFee);
        $("#tradeOfferNum").val(tradeOfferNum);
        $("#createDate").val(createDate);
        $("#accNbr").val(accNbr);
        $("#eparchyCode").val(eparchyCode);
        $("#preInstallDate").val(preInstallDate);
        $("#detailForm").submit();
    }

</script>
</body>
</html>
