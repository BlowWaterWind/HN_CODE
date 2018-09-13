<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
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
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/order/date01.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/order/date02.js"></script>
</head>
<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带订单</h1>
    </div>
</div>
<div class="container">
<div class="tabs broad-tabs">
        <ul class="tab-menu">
            <li id="all"><a href="${ctx}/o2oOrder/queryOrderList?flag=all&phone=${phone}">全部</a></li>
            <li id="unfinished"><a href="${ctx}/o2oOrder/queryOrderList?flag=unfinished&phone=${phone}">未完成</a></li>
            <li id="finished"><a href="${ctx}/o2oOrder/queryOrderList?flag=finished&phone=${phone}">已完成</a></li>
            <li id="unstart"><a href="${ctx}/o2oOrder/queryOrderTempList?flag=unstart&unfinishedNumber=${unfinishedNumber}&finishedNumber=${finishedNumber}&phone=${phone}">未回复<span class="order-tip">${unstartNumber}</span></a></li>
            <li id="willPay"><a href="${ctx}/o2oOrder/queryOrderPayTempList?flag=willPay&unfinishedNumber=${unfinishedNumber}&finishedNumber=${finishedNumber}&phone=${phone}">待支付<span class="order-tip">${payNum}</span></a></li>
        </ul>
    <form action="${ctx}/o2oOrder/queryOrderList" id="form1" name="form1" method="post">
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
                <div class="flow-date">
                    <label>查询号码：</label>
                    <div class="flow-box">
                        <input id="phone" type="text" name="phone" class="form-control" value="${phone}" />
                    </div>
                </div>
                <div class="section-btn"><a href="javascript:void(0)" class="btn btn-blue" id="query">查询</a></div>
            </section>
            <!--订单日期选择 end-->
        </form>
    <c:if test="${not empty broadbandOrderList}">
    <form id="detailForm" name="detailForm" action="${ctx}/o2oOrder/orderDetail" modelAttribute="broadbandOrder" method="post">
            <input type="hidden" name="offerName" id="offerName"/>
            <input type="hidden" name="tradeStatus" id="tradeStatus"/>
            <input type="hidden" name="tradeId" id="detailTradeId"/>
            <input type="hidden" name="serialNumber" id="detailSerialNumber"/>
            <input type="hidden" name="tradeTypeCode" id="tradeTypeCode"/>
            <input type="hidden" name="tradeFee" id="tradeFee"/>
            <input type="hidden" name="tradeOfferNum" id="tradeOfferNum"/>
            <input type="hidden" name="createDate" id="createDate"/>
            <input type="hidden" name="accNbr" id="accNbr"/>
            <input type="hidden" name="eparchyCode" id="eparchyCode"/>
            <input type="hidden" name="preInstallDate" id="preInstallDate"/>
        </form>
        <div id="tab-1">
            <div class="broad-order">
                <!--宽带订单 start-->
                        <c:forEach items="${broadbandOrderList}" var="broadbandOrder">
                            <div class="broad-list">
                                <div class="broad-title">
                                    <a href="javascript:void(0)">
                                        <span class="shop-icon">手机号码：${broadbandOrder.serialNumber}</span>
                                        <span class="channel-blue">${broadbandOrder.tradeStatus}</span>
                                    </a>
                                </div>
                                <div class="shop-gobal">
                                    <a href="javascript:void(0)">
                                            <%--<img src="images/bus09.png" />--%>
                                        <h4>${broadbandOrder.offerName}</h4>
                                        <p class="shop-txt">${broadbandOrder.tradeType}</p>
                                        <div class="shop-bottom"><span class="price"><fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/>元</span><span class="f12"></span></div>
                                    </a>
                                </div>
                                <div class="broad-sum">
                                    <p>共${broadbandOrder.tradeOfferNum}件商品</p>
                                    <p>合计：<span class="channel-blue">￥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/></span>（含安装费￥0.00）</p>
                                </div>
                                <div class="broad-btn">
                                    <a href="javascript:void(0);" class="broad-link broad-link-blue" onclick="detail('${broadbandOrder.tradeId}','${broadbandOrder.serialNumber}','${broadbandOrder.tradeStatus}','${broadbandOrder.offerName}','${broadbandOrder.tradeTypeCode}','${broadbandOrder.tradeFee}','${broadbandOrder.tradeOfferNum}','${broadbandOrder.createDate}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}','${broadbandOrder.preInstallDate}')">查看订单</a>
                                    <c:if test="${broadbandOrder.tradeTypeCode eq '1002' and broadbandOrder.tradeStatus ne '系统完工'}">
                                        <a href="javascript:void(0);" class="broad-link broad-link-blue"  onclick="remind('${broadbandOrder.tradeId}','${broadbandOrder.eparchyCode}','${broadbandOrder.accNbr}')">催单</a>
                                        <c:if test="${not empty broadbandOrder.preInstallDate}">
                                            <a href="javascript:void(0);" class="broad-link broad-link-blue" onclick="toModifyTime('${broadbandOrder.tradeId}','${broadbandOrder.preInstallDate}','${broadbandOrder.serialNumber}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}')">修改预约时间</a>
                                        </c:if>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
            </div>
        </div>
    </c:if>

</div>
    <!--暂无订单 strat-->
    <c:if test="${empty broadbandOrderList}">
        <div class="no-orders">
            <img src="${ctxStatic}/images/fa_icon.png" />
            <p>暂未找到相关订单</p>
        </div>
    </c:if>
    <!--暂无订单 end-->
</div>

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
        //tabs
//        $('.tabs').tabslet();
        var flag = $("#flag").val();
        $("#"+flag).addClass("active");

        $("#query").click(function() {
//            var startDate=new Date($("#startTime").val().replace(/-/g,"/"));
//            var endDate=new Date($("#endTime").val().replace(/-/g,"/"));
//            var differTime=endDate.getTime()-startDate.getTime()  //时间差的毫秒数
//            console.info(differTime);
//            if(differTime>24*3600*1000*7){
//                showAlert("查询时间跨度不能超过7天");
//                return;
//            }
            $("#form1").submit();
        });
    });

    function changeTab(obj){
        $(".tab-menu li").each(function(){
            $(this).removeClass("active");
        });
        $(obj).addClass("active");
    }

    function remind(tradeId,eparchyCode,serialNumber){
        $.ajax({
            type : 'post',
            url : ctx+'/o2oOrder/remind',
            dataType : 'json',
            cache : false,
            data : {
                tradeId : tradeId,cityCode:eparchyCode,serialNumber:serialNumber
            },
            success : function(data) {
                showAlert(data.respDesc);
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
            url : ctx+'/o2oOrder/modifyInstallTime',
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
            url : ctx+'/o2oOrder/queryAddr',
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
