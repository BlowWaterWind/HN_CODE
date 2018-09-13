<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KD_ZDD" />
    <meta name="WT.si_x" content="20" />
    <META name="WT.ac" content="">
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
    <%@include file="/WEB-INF/views/include/taglib.jsp"%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta http-equiv="Cache-Control" content="max-age=3600;must-revalidate" />
    <meta name="keywords" content="中国移动网上商城,手机,值得买,可靠,质量好,移动合约机,买手机送话费,4G手机,手机最新报价,苹果,华为,魅族" />
    <meta name="description" content="中国移动网上商城,提供最新最全的移动合约机、4G手机,买手机送话费,安全可靠,100%正品保证,让您足不出户,享受便捷移动服务！" />
    <script src="${ctxStatic}/js/jquery/jquery-2.1.4.min.js?v=625" type="text/javascript"></script>
    <script src="${ctxStatic}/js/jquery/jquery-migrate-1.2.1.min.js"></script>
        <script type="text/javascript" >
        var ctx="${ctx}",gUrl="${gUrl}",sUrl="${sUrl}",tfsUrl="${tfsUrl}",ctxStatic = "${ctxStatic}",contextPath = "${contextPath}";
    </script>

    <%@ include file="/WEB-INF/views/include/messageNew.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/broadBand/wap-order-new.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/js/datepicker/datepicker.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/swiper/swiper.min.css" rel="stylesheet" type="text/css"/>
        <%--插码js引入--%>
        <script type="text/javascript" src="${ctxStatic}/js/qm/webtrends.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/o2o/broadbandInsertCode.js"></script>
        <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
        <%--插码meta标签的定义--%>
        <meta name="WT.mobile" content="" />
        <meta name="WT.brand" content="" />
        <meta name="WT.si_n" content="WDDD_DDCX" />
        <meta name="WT.si_x" content="20" />


</head>
<body>

<div class="container">
    <%--<ul class="tabmenu">--%>
        <%--<li class="tabmenu-item" id="all"><a href="${ctx}/broadbandOrder/queryOrderList?flag=all">全部订单</a></li>--%>
        <%--<li class="tabmenu-item" id="willPay"><a href="${ctx}/broadbandOrder/queryPayOrderList?flag=willPay">支付相关订单</a></li>--%>
    <%--</ul>--%>
    <form action="${ctx}/broadbandOrder/queryOrderList" id="form1" name="form1" method="post">
        <div class="search-time"><i class="icon-datetime"></i>
            <div class="search-time_con">
                <input type="text" value="${startTime}" onclick="orderDateTime('startDateTime')" id="startDateTime" name="startTime"/>
                <div class="text">至</div>
                <input type="text" value="${endTime}" onclick="orderDateTime('endDateTime')" id="endDateTime" name="endTime"/>
            </div>
        </div>
        <input type="hidden" name="flag" value="${flag}" id="flag"/>
    </form>
    <form id="detailForm" name="detailForm" action="${ctx}/broadbandOrder/newOrderDetail" modelAttribute="broadbandOrder" method="post">
        <input type="hidden" name="offerName" id="offerName"/>
        <input type="hidden" name="tradeStatus" id="tradeStatus"/>
        <input type="hidden" name="tradeId" id="detailTradeId"/>
        <input type="hidden" name="serialNumber" id="detailSerialNumber"/>
        <input type="hidden" name="tradeTypeCode" id="tradeTypeCode"/>
        <input type="hidden" name="tradeType" id="tradeType"/>
        <input type="hidden" name="tradeFee" id="tradeFee"/>
        <input type="hidden" name="createDate" id="createDate"/>
        <input type="hidden" name="accNbr" id="accNbr"/>
        <input type="hidden" name="eparchyCode" id="eparchyCode"/>
        <input type="hidden" name="preInstallDate" id="preInstallDate"/>
        <input type="hidden" name="tradeOfferNum" id="tradeOfferNum"/>
    </form>

    <form id="queryWorkSheetForm" name="detailForm" action="${ctx}/broadbandOrder/queryWorkSheet" modelAttribute="broadbandOrder" method="post">
        <input type="hidden" name="tradeId" id="sheetDetailTradeId"/>
        <input type="hidden" name="accNbr" id="sheetAccNbr"/>
        <input type="hidden" name="eparchyCode" id="sheetEparchyCode"/>
        <input type="hidden" name="serialNumber" id="sheetSerialNumber"/>
        <input type="hidden" name="offerName" id="sheetOfferName"/>
        <input type="hidden" name="tradeTypeCode" id="sheetTradeTypeCode"/>
        <input type="hidden" name="addrDesc" id="sheetAddrDesc"/>
    </form>

    <form>
        <input type="hidden" name="tradeId" id="modifyTimeTradeId"/>
        <input type="hidden" name="installTime" id="modifyTimeInstallTime"/>
        <input type="hidden" name="serialNumber" id="modifyTimeSerialNumber"/>
        <input type="hidden" name="bandAccount" id="modifyTimeBandAccount"/>
        <input type="hidden" name="cityCode" id="modifyTimeCityCode"/>
        <input type="hidden" name="bookDate" id="bookDate"/>
        <input type="hidden" name="bookSession" id="bookSession"/>
    </form>

    <!--分享初始数据-->
    <input type="hidden"  id="recmdUserLink"> <!-- 分享链接 -->
    <input type="hidden"  id="recmdPhone">
    <input type="hidden"  id="cardTypeName">  <!-- 分享标题 -->
    <input type="hidden"  id="planDesc">    <!-- 分享描述 -->
    <input type="hidden"  id="titleImg">   <!-- 分享标题 -->
    <input type="hidden"  id="shareTradeId">   <!-- 订单ID -->

<c:if test="${not empty broadbandOrderList or not empty broadPayOrderList}">
<div class="lists">
<c:forEach items="${broadbandOrderList}" var="broadbandOrder">
    <div class="list order-list">
        <div class="order-list_hd">
            <div class="left"><i class="icon-shop"></i>
                <h3 class="shop-name">湖南移动营业厅</h3><i class="caret-right"></i>
            </div>
            <div class="right"><span class="txt-red">${broadbandOrder.tradeStatus}</span></div>
        </div>
        <div class="order-list_bd" onclick="detail('${broadbandOrder.tradeId}','${broadbandOrder.serialNumber}','${broadbandOrder.tradeStatus}','${broadbandOrder.offerName}','${broadbandOrder.tradeTypeCode}','${broadbandOrder.tradeFee}','${broadbandOrder.tradeOfferNum}','${broadbandOrder.createDate}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}','${broadbandOrder.preInstallDate}','${broadbandOrder.tradeType}')">
            <div class="thumb"><img src="${ctxStatic}/demoimages/kdtu.png" alt="宽带"/></div>
            <div class="con">
                <div class="row">
                    <div class="title">${broadbandOrder.offerName}</div>
                    <div class="price">¥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/></div>
                </div>
                <div class="row">
                    <div class="desc">套餐分类：${broadbandOrder.tradeType}</div>
                    <div class="number">x1</div>
                </div>
            </div>
        </div>
        <div class="total">共1件商品 合计：<span class="price">¥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/></span>（含初装费￥0.00）</div>
        <div class="btn-wrap Grid -right">
            <button onclick="initShare('${broadbandOrder.tradeId}','${broadbandOrder.offerName}','${broadbandOrder.tradeType}','${ctxStatic}/demoimages/kdtu.png','');" style="background-color: #fff;"  class="btn btn-default">分享订单</button>
            <c:if test="${broadbandOrder.tradeTypeCode eq '1002'}">
                <button type="button" class="btn btn-default" style="background-color: #fff;" onclick="queryWorkSheet('${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}','${broadbandOrder.tradeId}','${broadbandOrder.serialNumber}','${broadbandOrder.offerName}','${broadbandOrder.tradeTypeCode}');">安装进度</button>
                <c:if test="${broadbandOrder.tradeStatus ne '系统完工'}">
                    <button href="javascript:void(0)" class="btn btn-default" style="background-color: #fff;" onclick="remind('${broadbandOrder.tradeId}','${broadbandOrder.eparchyCode}','${broadbandOrder.accNbr}')">催单</button>
                </c:if>
                <c:if test="${not empty broadbandOrder.preInstallDate and broadbandOrder.tradeStatus ne '系统完工'}">
                    <button href="javascript:void(0)" class="btn btn-default btn-timePicker" style="background-color: #fff;" onclick="toPreModifyTime('${broadbandOrder.tradeId}','${broadbandOrder.preInstallDate}','${broadbandOrder.serialNumber}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}')">更改预约</button>
                </c:if>
                <c:if test="${broadbandOrder.tradeStatus eq '系统完工'}">
                    <button class="btn btn-default " style="background-color: #fff;" onclick="comment('ratingModal','${broadbandOrder.tradeId}')">评价</button>
                </c:if>
            </c:if>
        </div>
    </div>

    </c:forEach>

    <c:forEach items="${broadPayOrderList}" var="payOrder">
        <div class="list order-list">
            <div class="order-list_hd">
                <div class="left"><i class="icon-shop"></i>
                    <h3 class="shop-name">湖南移动营业厅</h3><i class="caret-right"></i>
                </div>
                <div class="right"><span class="txt-red">${payOrder.orderStatus.orderStatusName}</span></div>
            </div>
            <div class="order-list_bd">
                <div class="thumb"><img src="${ctxStatic}/demoimages/kdtu.png" alt="宽带"/></div>
                <div class="con">
                    <div class="row">
                        <div class="title">${payOrder.detailList[0].goodsName}</div>
                        <div class="price">¥<fmt:formatNumber value="${payOrder.orderSubPayAmount/100}" pattern="#.##"/></div>
                    </div>
                    <div class="row">
                        <div class="desc">套餐分类：${payOrder.orderType.orderTypeName}</div>
                        <div class="number">x1</div>
                    </div>
                </div>
            </div>
            <div class="total">共1件商品 合计：<span class="price">¥<fmt:formatNumber value="${payOrder.orderSubPayAmount/100}" pattern="#.##"/></span></div>
            <div class="btn-wrap Grid -right">
                <c:if test="${'0' eq payOrder.orderSubPayStatus and '12' ne payOrder.orderStatus.orderStatusId}">
                </c:if>
                <c:if test="${'1' eq payOrder.orderSubPayStatus }">
                    <a href="${ctx}/broadbandOrder/payDetail?orderId=${payOrder.orderId}" class="btn btn-default">支付明细</a>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>
</c:if>

    <!--暂无订单 strat-->
    <c:if test="${ empty broadbandOrderList}">
        <div class="no-data"><img src="${ctxStatic}/images/no-data.png" alt="无数据"/>
            <div class="desc">暂无记录哦</div>
        </div>
        <%--<div class="no-orders">
            <img src="${ctxStatic}/images/fa_icon.png" />
            <p>暂未找到相关订单</p>
        </div>--%>
    </c:if>
    <!--暂无订单 end-->
</div>

<!--评价弹框-->
<div id="ratingModal" class="modal-wrap">
    <div class="mask-layer"></div>
    <div class="bottom-modal">
        <div class="animate slideInUp">
            <div class="toolbar"><a href="javascript:;" onclick="toggleModal('ratingModal')" class="cancel">取消</a><a href="javascript:submitNewComment();" class="submit">完成</a></div>
            <div class="modal-content">
                <form action="" method="post">
                    <div class="rating">
                        <input type="radio" name="rating" id="star1" value="5"/>
                        <label for="star1" class="rating-star"></label>
                        <input type="radio" name="rating" id="star2" value="4"/>
                        <label for="star2" class="rating-star"></label>
                        <input type="radio" name="rating" id="star3" value="3"/>
                        <label for="star3" class="rating-star"></label>
                        <input type="radio" name="rating" id="star4" value="2"/>
                        <label for="star4" class="rating-star"></label>
                        <input type="radio" name="rating" id="star5" value="1"/>
                        <label for="star5" class="rating-star"></label>
                    </div>
                </form>
                <div class="rating-divider">
                    <div class="text">请为我们打分</div>
                </div>
                <%--<div class="tips">评价五星，并将订单链接分享可获赠100M流量哦~</div>--%>
            </div>
        </div>
    </div>
</div>
<!--评价弹框 end-->

<div id="cdSuccessModal" class="modal-wrap">
    <div class="mask-layer"></div>
    <div class="base-modal">
        <div class="animate slideInDown">
            <div onclick="toggleModal('cdSuccessModal')" class="modal-close"></div>
            <div class="modal-content">
                <div class="return-img"><img src="${ctxStatic}/images/icon-success.png" alt="成功"/></div>
                <div class="title">催单成功！</div>
                <div class="desc">装维人员将尽快与您联系，请保持电话畅通哦~</div>
            </div>
            <div class="modal-ft Grid -between"><a href="javascript:;" onclick="toggleModal('cdSuccessModal')" class="cancel">确定</a></div>
        </div>
    </div>
</div>
<!--催单 弹框 end-->

<!-- 分享弹框 -->
<div id="mcover" class="modal-wrap">
    <div class="mask-layer"></div><img src="${ctxStatic}/images/share-arrow.png" alt="分享" class="share-arrow"/><img src="${ctxStatic}/images/share-txt.png" alt="分享" class="share-txt"/>
</div>
<!-- 分享弹框 end-->



<script type="text/javascript" src="${ctxStatic}/js/busi/lib/flexible.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/swiper/swiper.jquery.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-extend.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/datetime-picker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/busi/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/datepicker/datepicker.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/wxShare.js?v=2018081602"></script>

<script>
    $(function() {
        var flag = $("#flag").val();
        console.info(flag);
        $("#"+flag).addClass("active");
        $("#query").click(function() {
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
                var six="-99"
                if(data.respCode=='0'){
                    six="99";
                    toggleModal('noticeSuccessId',"催单成功！","装维人员将尽快与您联系，请保持电话畅通哦~");
                }else{
                    toggleModal("noticeFailId",data.respDesc);
                }
                // 插码相关
                try{
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n",'订单催单',
                                "WT.si_x",six],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n",'订单催单',true,
                                    "WT.si_x",six,true);
                        }
                    }
                }catch (e){
                    console.log(e);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                toggleModal("noticeFailId","服务中断，请重试");
            }
        });
    }

    //修改预约时间
    function modifyTime(){
        var bookSession = $("#bookSession").val();
        if(bookSession!=null){
            if(bookSession=="8~12点"){
                bookSession="0";
            }
            if(bookSession=="12~14点"){
                bookSession="1";
            }
            if(bookSession=="14~18点"){
                bookSession="2";
            }
            if(bookSession=="18~20点"){
                bookSession="3";
            }
        }
        var bookDate = $("#bookDate").val();
        if(bookDate ==null ||bookDate==''||bookSession ==null ||bookSession==''){
            toggleModal("noticeFailId","请选择预约时间！");
            return;
        }
        var tradeId= $("#modifyTimeTradeId").val();
        var serialNumber=$("#modifyTimeSerialNumber").val();
        var bandAccount=$("#modifyTimeBandAccount").val();
        var installTime = $("#modifyTimeInstallTime").val();

        if(installTime ==null ||installTime==''){
            toggleModal("noticeFailId","非预约单，不支持改约！");
            return;
        }
        //判断预约时间是否大于当前时间48/72小时
        var curDate=Date.parse(new Date());
        var installDate=installTime.replace(/-/g,"/");
        if(curDate > Date.parse(new Date(installDate))){//判断预约时间-当前时间>
            toggleModal("noticeFailId","预约时间已过，不支持改约！");
            return;
        }

        $.ajax({
            type : 'post',
            url : ctx+'/broadbandOrder/modifyInstallTime',
            cache : false,
            dataType : 'json',
            data : {bookDate:bookDate,tradeId:tradeId,bookSession:bookSession,serialNumber:serialNumber,bandAccount:bandAccount},
            success : function(data) {
                var six="-99";
                if(data.respCode=='0'){
                    six="99";
                    toggleModal('noticeSuccessId',"修改成功");
                }else{
                    toggleModal("noticeFailId",data.respDesc);
                }
                // 插码相关
                try{
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n",'修改预约时间',
                                "WT.si_x",six],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n",'修改预约时间',true,
                                    "WT.si_x",six,true);
                        }
                    }
                }catch (e){
                    console.log(e);
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                toggleModal("noticeFailId","服务中断，请重试");
            }
        });
    }

    /**
     * 修改预约时间预备参数
     * @param tradeId
     * @param installTime
     * @param serialNumber
     * @param bandAccount
     * @param cityCode
     */
    function toPreModifyTime(tradeId,installTime,serialNumber,bandAccount,cityCode){
        // 插码相关
        try{
            if(window.Webtrends){
                Webtrends.multiTrack({
                    argsa: ["WT.si_n",'修改预约时间',
                        "WT.si_x","21"],
                    delayTime: 100
                })
            }else{
                if(typeof(dcsPageTrack)=="function"){
                    dcsPageTrack ("WT.si_n",'修改预约时间',true,
                            "WT.si_x","21",true);
                }
            }
        }catch (e){
            console.log(e);
        }
        $("#modifyTimeTradeId").val(tradeId);
        $("#modifyTimeInstallTime").val(installTime);
        $("#modifyTimeSerialNumber").val(serialNumber);
        $("#modifyTimeBandAccount").val(bandAccount);
        $("#modifyTimeCityCode").val(cityCode);
    }

    var thisTradeId = "";
    function comment(showId,tradeId){
        // 插码相关
        try{
            if(window.Webtrends){
                Webtrends.multiTrack({
                    argsa: ["WT.si_n",'订单评价',
                        "WT.si_x","21"],
                    delayTime: 100
                })
            }else{
                if(typeof(dcsPageTrack)=="function"){
                    dcsPageTrack ("WT.si_n",'订单评价',true,
                            "WT.si_x","21",true);
                }
            }
        }catch (e){
            console.log(e);
        }
        toggleModal(showId);
        thisTradeId = tradeId;
        var url = "${ctx}/broadbandOrder/toComment";
        $(".rating input[type='radio']").each(function(index,element){
            $(this).attr("checked",false);
        })
        $.post(url,{orderId:tradeId},function(data){
            if(data.broadbandComment!=null){
                console.info(data.broadbandComment.resultScore);
                $(".rating input[type='radio']").each(function(index,element){
                    if($(this).val()==data.broadbandComment.resultScore){
                        $(this).click();
                        $(this).next().click();
                    }
                })
            }
        })
    }

    function submitNewComment(){
        toggleModal("ratingModal");
        var resultScore = 0;
        $(".rating input[type='radio']").each(function(index,element){
            if($(this).is(":checked")){
                resultScore =$(this).val();
            }
        })
        var url = "${ctx}/broadbandOrder/submitNewComment";
        $.post(url,{tradeId:tradeId,resultScore:resultScore},function(data){
            var respCode = data.respCode;
            var six="-99";
            if(respCode=="0"){
                six="99";
                toggleModal('noticeSuccessId',"评价成功");
            }else{
                toggleModal("noticeFailId",data.respDesc);
            }
            // 插码相关
            try{
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",'订单评价',
                            "WT.si_x",six],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",'订单评价',true,
                                "WT.si_x",six,true);
                    }
                }
            }catch (e){
                console.log(e);
            }
        })
    }

    function detail(tradeId,serialNumber,tradeStatus,offerName,tradeTypeCode,tradeFee,tradeOfferNum,createDate,accNbr,eparchyCode,preInstallDate,tradeType){
        if(window.Webtrends){
            Webtrends.multiTrack({
                argsa: ["WT.event","WDDD_CKXQ"],
                delayTime: 100
            })
        }else{
            if(typeof(dcsPageTrack)=="function"){
                dcsPageTrack ("WT.event","WDDD_CKXQ",true);
            }
        }
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
        $("#tradeType").val(tradeType);
        $("#detailForm").submit();
    }

    $(".btn-timePicker").datetimePicker({
        title:"请选择预约时间",
        yearSplit: '年',
        monthSplit: '月',
        dateSplit: '日',
        years: [${years}],//限定年
        monthes: ['01','02', '03', '04','05', '06', '07','08', '09', '10','11', '12'],//限定月分
        toolbarTemplate:"<div class=\"toolbar\"><a href=\"javascript:;\" class=\"cancel close-picker\">取消</a><a href=\"javascript:modifyTime();\" class=\"submit close-picker\">确认</a></div>",
        times: function () {
            return [
                {
                    values: ['8~12点', '12~14点','14~18点','18~20点']
                }
            ];
        },
        value:'${defaultValue}',//设置默认值
        onChange: function (picker, values, displayValues) {
            var bookDate = values[0]+"-"+values[1]+"-"+values[2];
            var bookSession = values[3];
            $("#bookSession").val(bookSession);
            $("#bookDate").val(bookDate);
        },
        onClose:function(picker){

            var values = picker.value;
            var bookDate = values[0]+"-"+values[1]+"-"+values[2];
            var bookSession = values[3];
            $("#bookSession").val(bookSession);
            $("#bookDate").val(bookDate);
        }
    });



    function orderDateTime(e) {
        var nowValue = document.getElementById(e);
        new DatePicker({
            "type": "3",//0年, 1年月, 2月日, 3年月日
            "title": '请选择日期',//标题(可选)
            "maxYear": "",//最大年份（可选）
            "minYear": "",//最小年份（可选）
            "separator": "-",//分割符(可选)
            "defaultValue": nowValue.value,//默认值（可选）
            "callBack": function (val) {
                //回调函数（val为选中的日期）
                nowValue.value = val;
                var startDateTime = $("#startDateTime").val();
                var endDateTime = $("#endDateTime").val();
                if(compareDate(startDateTime,endDateTime)){
                    $("#form1").submit();
                }

            }
        });
    }

    //比较日前大小
     function compareDate(checkStartDate, checkEndDate) {
        var arys1= new Array();
        var arys2= new Array();
        if(checkStartDate != null && checkEndDate != null) {
            arys1=checkStartDate.split('-');
            var sdate=new Date(arys1[0],parseInt(arys1[1]-1),arys1[2]);
            arys2=checkEndDate.split('-');
            var edate=new Date(arys2[0],parseInt(arys2[1]-1),arys2[2]);
            if(sdate > edate) {
                toggleModal("noticeFailId","开始时间必须小于结束时间！");
                return false;
            }  else {
                return true;
            }
        }
    }

    /**
     * 查询安装进度查询
     * @param accNbr
     * @param eparchyCode
     * @param tradeId
     */
    function queryWorkSheet(accNbr,eparchyCode,tradeId,serialNumber,offerName,tradeTypeCode){
        if(window.Webtrends){
            Webtrends.multiTrack({
                argsa: ["WT.event","WDDD_CKANJD"],
                delayTime: 100
            })
        }else{
            if(typeof(dcsPageTrack)=="function"){
                dcsPageTrack ("WT.event","WDDD_CKANJD",true);
            }
        }
        $("#sheetOfferName").val(offerName);
        $("#sheetDetailTradeId").val(tradeId);
        $("#sheetAccNbr").val(accNbr);
        $("#sheetEparchyCode").val(eparchyCode);
        $("#sheetSerialNumber").val(serialNumber);
        $("#sheetTradeTypeCode").val(tradeTypeCode);
        $("#queryWorkSheetForm").submit();
    }

    /**
     * 初始化分享数据
     **/
    function initShare(tradeId,offerName,tradeType,img){
        var recmdUserLink = "http://wap.hn.10086.cn/shop/broadband/broadbandHome";
        if(tradeType=="宽带移机"){
            recmdUserLink="http://wap.hn.10086.cn/shop/myBroadband/myAccount";
        }
        if(tradeType=="宽带开户"){
            recmdUserLink="http://wap.hn.10086.cn/shop/broadband/new-install";
        }
        if(tradeType=="和家庭宽带订单"){
            recmdUserLink="http://wap.hn.10086.cn/shop/newHeBand/heInstall";
        }
        $("#recmdUserLink").val(recmdUserLink);
        $("#cardTypeName").val(tradeType);
        $("#planDesc").val(offerName);
        $("#titleImg").val(img);
        $("#shareTradeId").val(tradeId);
        wxReady();
        share();
    }
    /**
     * 分享成功处理
     */
    function shareSuccess(){
        var url="${ctx}/broadbandOrder/shareSubmit";
        var tradeId = $("#shareTradeId").val();
        $.post(url,{tradeId:tradeId},function(data){
            if(data.respCode=='0'){
                toggleModal('noticeSuccessId',"分享成功");
            }else{
                toggleModal("noticeFailId",data.respDesc);
            }
        })
    }

</script>
</body>
</html>
