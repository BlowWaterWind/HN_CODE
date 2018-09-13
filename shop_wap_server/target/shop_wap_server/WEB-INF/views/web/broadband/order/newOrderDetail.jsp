<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
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
    <meta name="WT.si_n" content="WDDD_CKXQ" />
    <meta name="WT.si_x" content="20" />
</head>
<body>
<div class="container">
    <div class="lists">
        <div class="list order-info_wrap">
            <div class="order-info"><i class="icon-time"></i>
                <div class="order-info_bd">
                    <p>${broadbandOrder.tradeStatus}</p>
                    <div class="time">${broadbandOrder.createDate}</div>
                </div>
                <div class="order-info_ft">
                    <c:if test="${broadbandOrder.tradeTypeCode eq '1002'}">
                    <div class="mr-20" onclick="queryWorkSheet('${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}','${broadbandOrder.tradeId}','${broadbandOrder.serialNumber}');">查看进度详情</div><i class="caret-right"></i>
                    </c:if>
                    </div>
            </div>
            <div class="order-info"><i class="icon-address"></i>
                <div class="order-info_bd">
                    <div class="Grid -between mb-10">
                        <p>联络人：${broadbandOrder.custName}</p>
                        <p>${broadbandOrder.serialNumber}</p>
                    </div>
                    <div class="txt-deep-gray">安装地址：${broadbandOrder.addrDesc}</div>
                    <br>
                    <%--<c:if test="${broadbandComment!=null}">--%>
                        <%--<ul class="master-stars">--%>
                            <%--<c:forEach begin="1" end="${broadbandComment.resultScore}">--%>
                                <%--<li class="master-star"></li>--%>
                            <%--</c:forEach>--%>
                        <%--</ul>--%>
                    <%--</c:if>--%>

                </div>
            </div>
        </div>

        <div class="list order-list">
            <div class="order-list_hd">
                <div class="left"><i class="icon-shop"></i>
                    <h3 class="shop-name">湖南移动营业厅</h3><i class="caret-right"></i>
                </div>
            </div>
            <div class="order-list_bd">
                <div class="thumb"><img src="${ctxStatic}/demoimages/kdtu.png" alt="宽带"/></div>
                <div class="con">
                    <div class="row">
                        <div class="title">${broadbandOrder.offerName}</div>
                        <div class="price">￥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/></div>
                    </div>
                    <div class="row">
                        <div class="desc">套餐分类：${broadbandOrder.tradeType}</div>
                        <div class="number">x${broadbandOrder.tradeOfferNum}</div>
                    </div>
                </div>
            </div>
            <div class="total">共1件商品 合计：<span class="price">￥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/>元</span>（含初装费￥0.00）</div>
        </div>

        <div class="list">
            <div class="order-total">
                <h3>订单总价</h3>
                <div class="txt-red">￥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/></div>
            </div>
            <div class="order-detail">
                <div class="order-detail_item">
                    <div class="txt-deep-gray">初装费</div><span>￥0.00</span>
                </div>
                <div class="order-detail_item">
                    <div class="txt-deep-gray">产品总价</div><span>￥<fmt:formatNumber value="${broadbandOrder.tradeFee/100}" pattern="#.##"/></span>
                </div>
            </div>
        </div>

        <div class="list">
            <div class="order-detail">
                <div class="order-detail_item">
                    <div class="txt-deep-gray">订单编号</div><span>${broadbandOrder.tradeId}</span>
                </div>
                <div class="order-detail_item">
                    <div class="txt-deep-gray">订单提交时间</div><span>${broadbandOrder.createDate}</span>
                </div>
            </div>
        </div>



    </div>
    <%--<div class="divider">
        <div class="text">为你推荐</div>
    </div>
    <div class="hot">
        <a href="javascript:;" class="hot-item"><img src="images/hot-img-01.jpg" alt="一卡多号"/>
            <div class="title">一卡多号</div>
            <div class="desc">一心不能多用，一卡可以多用</div>
        </a>
        <a href="javascript:;" class="hot-item"><img src="images/hot-img-02.jpg" alt="咪咕视频"/>
            <div class="title">咪咕视频</div>
            <div class="desc">视频VIP会员 0元看大片</div>
        </a>
    </div>--%>

</div>

<div class="footbtn-wrap">
    <div class="footbtn">
        <c:if test="${broadbandComment==null || broadbandComment.resultScore==0}">
        <a href="javascript:void(0)" class="btn btn-default" style="background-color: #fff;" onclick="comment('ratingModal','${broadbandOrder.tradeId}');">评价</a>
        </c:if>
        <c:if test="${broadbandOrder.tradeTypeCode eq '1002'}">
            <a href="javascript:;" class="btn btn-default" style="background-color: #fff;" onclick="initShare('${broadbandOrder.tradeId}','${broadbandOrder.offerName}','${broadbandOrder.tradeType}','${ctxStatic}/demoimages/kdtu.png','')">分享订单</a>
            <c:if test="${broadbandOrder.tradeStatus ne '系统完工'}">
                <a href="javascript:void(0)" class="btn btn-default" style="background-color: #fff;" onclick="remind('${broadbandOrder.tradeId}','${broadbandOrder.eparchyCode}','${broadbandOrder.accNbr}')">催单</a>
            </c:if>
            <c:if test="${not empty broadbandOrder.preInstallDate and broadbandOrder.tradeStatus ne '系统完工'}">
                <a href="javascript:void(0)" class="btn btn-default btn-timePicker" style="background-color: #fff;" onclick="toPreModifyTime('${broadbandOrder.tradeId}','${broadbandOrder.preInstallDate}','${broadbandOrder.serialNumber}','${broadbandOrder.accNbr}','${broadbandOrder.eparchyCode}')">更改预约</a>
            </c:if>
        </c:if>
    </div>
</div>

<form id="queryWorkSheetForm" name="detailForm" action="${ctx}/broadbandOrder/queryWorkSheet" modelAttribute="broadbandOrder" method="post">
    <input type="hidden" name="tradeId" id="detailTradeId"/>
    <input type="hidden" name="accNbr" id="accNbr"/>
    <input type="hidden" name="eparchyCode" id="eparchyCode"/>
    <input type="hidden" name="serialNumber" id="serialNumber" value="${broadbandOrder.serialNumber}"/>
    <input type="hidden" name="offerName" id="offerName" value="${broadbandOrder.offerName}"/>
    <input type="hidden" name="tradeTypeCode" id="tradeTypeCode" value="${broadbandOrder.tradeTypeCode}"/>
    <input type="hidden" name="addrDesc" id="addrDesc" value="${broadbandOrder.addrDesc}"/>
</form>

<form id="detailForm" name="detailForm" action="${ctx}/broadbandOrder/newOrderDetail" modelAttribute="broadbandOrder" method="post">
    <input type="hidden" name="offerName" id="offerName" value="${broadbandOrder.offerName}"/>
    <input type="hidden" name="tradeStatus" id="tradeStatus" value="${broadbandOrder.tradeStatus}"/>
    <input type="hidden" name="tradeId" id="detailTradeId" value="${broadbandOrder.tradeId}"/>
    <input type="hidden" name="serialNumber" id="detailSerialNumber" value="${broadbandOrder.serialNumber}"/>
    <input type="hidden" name="tradeTypeCode" id="tradeTypeCode" value="${broadbandOrder.tradeTypeCode}"/>
    <input type="hidden" name="tradeType" id="tradeType" value="${broadbandOrder.tradeType}"/>
    <input type="hidden" name="tradeFee" id="tradeFee" value="${broadbandOrder.tradeFee}"/>
    <input type="hidden" name="createDate" id="createDate" value="${broadbandOrder.createDate}"/>
    <input type="hidden" name="accNbr" id="accNbr" value="${broadbandOrder.accNbr}"/>
    <input type="hidden" name="eparchyCode" id="eparchyCode" value="${broadbandOrder.eparchyCode}"/>
    <input type="hidden" name="preInstallDate" id="preInstallDate" value="${broadbandOrder.preInstallDate}"/>
    <input type="hidden" name="tradeOfferNum" id="tradeOfferNum" value="${broadbandOrder.tradeOfferNum}"/>
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
                <div class="tips">评价五星，并将订单链接分享可获赠100M流量哦~</div>
            </div>
        </div>
    </div>
</div>
<!--评价弹框 end-->

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

<script type="text/javascript">

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
        var tradeId= $("#modifyTimeTradeId").val();
        var serialNumber=$("#modifyTimeSerialNumber").val();
        var bandAccount=$("#bandAccount").val();
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
     * 更改预约
     * @param tradeId
     * @param installTime
     * @param serialNumber
     * @param bandAccount
     * @param cityCode
     */
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

    /**
     * 查询安装进度查询
     * @param accNbr
     * @param eparchyCode
     * @param tradeId
     */
    function queryWorkSheet(accNbr,eparchyCode,tradeId,serialNumber){
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
        $("#detailTradeId").val(tradeId);
        $("#accNbr").val(accNbr);
        $("#eparchyCode").val(eparchyCode);
        $("#serialNumber").val(serialNumber);
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
     * 修改预约时间预备参数
     * @param tradeId
     * @param installTime
     * @param serialNumber
     * @param bandAccount
     * @param cityCode
     */
    function toPreModifyTime(tradeId,installTime,serialNumber,bandAccount,cityCode){
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
        $("input:radio[name='rating']").each(function(index,element){
            if($(this).is(":checked")){
                resultScore = $(this).val();
            }
        })
        var url = "${ctx}/broadbandOrder/submitNewComment";
        $.post(url,{tradeId:thisTradeId,resultScore:resultScore},function(data){
            var respCode = data.respCode;
            var six="-99";
            if(respCode=="0"){
                six="99";
                toggleModal('noticeSuccessId',"评价成功");
                $("#detailForm").submit();
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


</script>
</body>
</html>
