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
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/js/iOSselect/iosSelect.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/datePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/checkIdCard.js"></script>
    <!-- 宽带多级地址搜索框 -->
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>
</head>
<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>套餐详情</h1>
    </div>
</div>
<div class="container">
    <form action="confirmInstall" id="form1" name="form1" method="post">
        <input type="hidden" id="goodsSkuId" name="goodsSkuId" value="${broadbandFusionBean.goodsSkuId}"/>
        <input type="hidden" id="goodsName" name="goodsName" value="${broadbandFusionBean.goodsName}" />
        <input type="hidden" id="addressName" name="addressName"/>
        <input type="hidden" id="addrDesc" name="addrDesc"/>
        <input type="hidden" id="communityType" name="communityType"/>
        <input type="hidden" id="isBroadband" name="isBroadband" value="${broadbandFusionBean.isBroadband}"/>
        <input type="hidden" id="isMbh" name="isMbh" value="${broadbandFusionBean.isMbh}"/>
        <input type="hidden" id="maxWidth" name="maxWidth"/>
        <input type="hidden" id="freePort" name="freePort"/>
        <input type="hidden" id="eparchyCode" name="eparchyCode" value="${broadbandFusionBean.eparchyCode}"/>
        <input type="hidden" id="county" name="county"/>
        <input type="hidden" id="countyName" name="countyName"/>
        <input type="hidden" id="coverType" name="coverType"/>
        <input type="hidden" id="bandWidth" name="bandWidth" value="${broadbandFusionBean.bandWidth}"/>
        <input type="hidden" id="productId" name="productId" value="${broadbandFusionBean.productId}"/>
        <input type="hidden" id="discntCode" name="discntCode" value="${broadbandFusionBean.discntCode}"/>
        <input type="hidden" id="chooseCat" name="chooseCat" value="${broadbandFusionBean.chooseCat}"/>
        <input type="hidden" id="custName" name="custName"/>
        <input type="hidden" id="price" name="price" value="${broadbandFusionBean.price}"/>
        <input type="hidden" id="bookInstallDateParam" name="bookInstallDate"/>
        <input type="hidden" id="bookInstallTimeParam" name="bookInstallTime"/>
        <input type="hidden" id="payMode" name="payMode" value="0">
        <input type="hidden" id="payModeName" name="payModeName" value="直接办理">
        <input type="hidden" id="minCost" name="minCost" value="${broadbandFusionBean.minCost}"/>
        <input type="hidden" id="monthCost" name="monthCost" value="${broadbandFusionBean.monthCost}"/>
        <input type="hidden" id="busiType" name="busiType" value="${broadbandFusionBean.busiType}"/>
        <input type="hidden" name="memberListStr" id="memberListStr" value="${broadbandFusionBean.memberListStr}"/>
        <input type="hidden" name="giveMbh" id="giveMbh" value="${broadbandFusionBean.giveMbh}"/>
        <div class="entry-info hjt-detail">

            <!--已选套餐 start-->
            <div class="entry-list">
                <div class="entry-title">已选套餐</div>
                <div class="detail-con">
                    <c:choose>
                        <c:when test="${broadbandFusionBean.busiType eq '0'}">
                            <p>${broadbandFusionBean.discntCode}元/月，包含${broadbandFusionBean.bandWidth}M宽带+国内不限量${broadbandFusionBean.homeData}G不限速+国内${broadbandFusionBean.homeVoice}分钟语音（
                                <c:if test="${broadbandFusionBean.giveMbh!=0}"> ${broadbandFusionBean.giveMbh}元/月魔百和</c:if>
                                <c:if test="${broadbandFusionBean.giveMbh==0}"> 赠送魔百和</c:if>
                                ）</p>
                        </c:when>
                        <c:when test="${broadbandFusionBean.busiType eq '1' or broadbandFusionBean.busiType eq '2'}">
                            <p>${broadbandFusionBean.minCost}元保底套餐，宽带速率：${broadbandFusionBean.bandWidth}M</p>
                        </c:when>
                        <c:when test="${broadbandFusionBean.busiType eq '3'}">
                            <p>组家庭网送宽带，套餐内容：100M光纤宽带免费用，4K高清互联网电视20元/月，光猫、机顶盒费用全免。</p>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            <!--已选套餐 end-->
            <c:if test="${broadbandFusionBean.isBroadband == '1'}">
            <!--用户信息 start-->
            <div class="entry-list">
                <div class="entry-title">用户信息</div>
                <ul class="entry-form">
                    <li>
                        <label>姓名</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" id="contact" placeholder="请输入姓名" name="contact"/>
                        </div>
                    </li>
                    <li>
                        <label>身份证号</label>
                        <div class="entry-rt">
                            <input type="text" id="idCard" class="entry-box" placeholder="请输入身份证号" name="idCard"/>
                        </div>
                    </li>
                    <li>
                        <label>手机号码</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" id="serialNumber" value="${serialNumber}" name="serialNumber" readonly/>
                            <p class="pt5 font-red hide">输入的手机号有误</p>
                        </div>
                    </li>
                    <li>
                        <label>联系号码</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" id="contactPhone" placeholder="不填默认为本人联系号码" name="contactPhone"/>
                        </div>
                    </li>
                </ul>
                <%--地址选择--%>
                <div class="add-con">
                    <div class="add-list">
                        <label>所在地区：</label>
                        <div class="add-box">
                            <p>
                                <select class="form-control" style="height: 30px;" id="memberRecipientCity"
                                        name="memberRecipientCity">
                                    <c:forEach items="${cityList}" var="city" varStatus="status">
                                        <c:if test="${fn:contains(city.eparchyCode,eparchyCode)}">
                                            <option value="${city.eparchyCode}" orgid="${city.orgId}" otitle="地市选择"
                                                    otype="button">${city.orgName}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </p>
                            <p>
                                <select class="form-control" style="height: 30px;" id="memberRecipientCounty"
                                        name="memberRecipientCounty">
                                    <c:forEach items="${countyList}" var="county" varStatus="status">
                                        <option value="${county.orgId}" otitle="区县选择"
                                                otype="button">${county.orgName}</option>
                                    </c:forEach>
                                </select>
                            </p>
                        </div>
                    </div>
                    <%@ include file="/WEB-INF/views/web/goods/broadband/address.jsp"%>
                </div>
            <!--用户信息 end-->
</div>
            <!--安装时间 start-->
            <div class="entry-list hjt-date-list">
                <div class="entry-title">安装时间</div>
                <div class="hjt-date">
                    <div class="date-list">
                        <span class="date-icon01">是否预约装机时间</span>
                        <div class="date-con">
                            <div class="date-text">
                                <input type="radio" name="bookYes" value="1" id="radio01" checked="checked"/>
                                <label for="radio01">是</label>
                            </div>
                            <div class="date-text">
                                <input type="radio" name="bookYes" value="2" id="radio02"/>
                                <label for="radio02">否</label>
                            </div>
                        </div>
                    </div>
                    <div class="date-list date-list02" id="dateSelect">
                        <span class="date-icon02">预约安装时间时段</span>
                        <div class="date-rt" data-toggle="modal" >
                            <span id="dateTime"></span>
                            <i class="arrow"></i>
                        </div>
                    </div>

                </div>
            </div>
            <!--安装时间 end-->

            <!--优惠赠送 start-->
            <%--<c:if test="${isMbh == '1'}">--%>
                <div class="entry-list">
                    <div class="entry-title">优惠赠送</div>
                    <div class="hjt-discount">
                        <div class="discount-list">
                            <img src="${ctxStatic}/images/broadBand/hjt_tu.png" class="discount-img"/>
                            <div class="discount-info">
                                <p>宽带光猫
                                    <del class="font-gray9 ml20">￥100.00</del>
                                </p>
                                <p class="font-gray9 discount-txt">移动专用光猫，稳定、极速上网</p>
                            </div>
                            <span class="font-red">￥0.00</span>
                        </div>
                        <div class="discount-list">
                            <img src="${ctxStatic}/images/broadBand/hjt_tu.png" class="discount-img"/>
                            <div class="discount-info">
                                <p>高清机顶盒
                                    <del class="font-gray9 ml20">￥100.00</del>
                                </p>
                                <p class="font-gray9 discount-txt">互联网电视专用，享受只能高清电视节目</p>
                            </div>
                            <span class="font-red">￥0.00</span>
                        </div>
                        <%@include file="/WEB-INF/views/web/broadband/comm/addMoremagict.jsp" %>
                    </div>
                </div>
            <%--</c:if>--%>
            <!--优惠赠送 end-->

            <%@include file="/WEB-INF/views/web/broadband/comm/addIms.jsp" %>

            <%--<div class="wf-list change-new sub-new">--%>
            <%--<div class="wf-ait clearfix">--%>
            <%--<ul class="bar-list">--%>
            <%--<li style="height:26px;line-height:26px;">--%>
            <%--<span class="font-gray">支付方式：</span>--%>
            <%--<div class="sub-text">--%>
            <%--<a href="javascript:choosePayMode('pay-btn1','2','在线支付')"--%>
            <%--class="bar-btn pay-btn pay-btn1">在线支付</a>--%>
            <%--</div>--%>
            <%--</li>--%>
            <%--</ul>--%>
            <%--</div>--%>
            <%--</div>--%>
    </c:if>
        </div>
    </form>
</div>
<!--尾部 start-->
<div class="fix-br new-fixed">
    <div class="affix foot-box">
        <div class="container active-fix">
            <div class="fl-left">
                <p class="ft-total">宽带速率：<span class="font-gray6">${broadbandFusionBean.bandWidth}M</span></p>
            </div>
            <div class="fl-right"><a href="javascript:void(0);" id="orderComfirm" class="">立即办理</a>
            </div>
            <!--当按钮为灰色时 a class 加 btn-gray-->
        </div>
    </div>
</div>
<!--尾部 end-->



<!--选择时间 弹框 start-->
<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                <h4 class="modal-title">安装时间</h4>
            </div>
            <ul class="modal-form">
                <li>
                    <span class="rt-title">日期选择：</span>
                    <div class="modal-rt">
                        <div class="user-form" id="selectDate">
                            <input type="hidden" name="bookInstallDate" id="bookInstallDate">
                            <div class="form-control" data-year="" data-month="" data-date="" id="showDate">未选择</div>
                            <span class="arrow"></span>
                        </div>
                    </div>
                </li>
                <li>
                    <span class="rt-title">时间段选择：</span>
                    <div class="modal-rt">
                        <select class="form-control" id="bookInstallTime" name="bookInstallTime">
                            <option value="">---请选择---</option>
                            <option value="0">08:00:00-12:00:00</option>
                            <option value="1">12:00:00-14:00:00</option>
                            <option value="2">14:00:00-18:00:00</option>
                            <option value="3">18:00:00-20:00:00</option>
                        </select>
                    </div>
                </li>
            </ul>
            <div class="subform-btn">
                <a href="javascript:void(0)" class="btn btn-green" id="dateCancel">取消</a>
                <a href="javascript:void(0)" class="btn btn-blue" id="dateComfirm">确定</a>
            </div>
        </div>
    </div>
</div>
<!--选择时间 弹框 end-->


</body>
<script>
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var countyUrl="<%=request.getContextPath()%>/broadband/getCountyFromBoss";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
    $(function () {
        var eparchyCode_V = $("#memberRecipientCity").val();

        if(eparchyCode_V){
            $.ajax({
                type : 'post',
                url : countyUrl,
                cache : false,
                context : $(this),
                dataType : 'json',
                data : {
                    cityCode : eparchyCode_V
                },
                success : function(data) {
                    $('#memberRecipientCounty').html('');
                    $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
                    if(data.flag=='Y'){
                        var  htt = "";
                        $.each(data.orgList,function(i,obj){
                            htt+="<option value='"+obj.CITY_ID+"'>"+obj.CITY_NAME+"</option>";
                        });
                        $('#memberRecipientCounty').html(htt);

                    }else{
                        showAlert("您查询的区县不存在！");
                    }

                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    showAlert("您查询的区县不存在！");
                }
            });
        }
        $('input:radio[name="bookYes"]').change(function () {
            if ($(this).val() == "2") {
                $("#bookInstallDate").val("");
                $("#bookInstallTime").val("");
                $("#bookInstallDateParam").val("");
                $("#bookInstallTimeParam").val("");
                $("#dateTime").html("");
                $("#dateSelect").hide();
            } else {
                $("#dateSelect").show();
            }
        });

        $("#dateCancel").click(function(){
            $("#myModal2").modal('hide');
        });
        $("#dateComfirm").click(function(){
            var bookInstallDate = $("#bookInstallDate").val();
//            var bookInstallTime = $("#bookInstallTime").val();
            var bookInstallTime =  $("#bookInstallTime").find("option:selected").text();
            $("#bookInstallDateParam").val(bookInstallDate);
            $("#bookInstallTimeParam").val( $("#bookInstallTime").val());
            $("#dateTime").html(bookInstallDate+" "+bookInstallTime);
            $("#myModal2").modal('hide');
        });
        $("#dateSelect").click(function(){
            $("#bookInstallDate").html("");
            $("#bookInstallTime").val("");
            $("#dateTime").html("");
            $("#myModal2").modal('show');
        })

        $("#memberRecipientCounty").bind("change",function(){
            $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
            $("#queryAddress").val("");
            $(".adress-list").html("");
            $("#houseCode").val("");
            $('#queryCommit').attr("disabled","disabled");
            $('#queryCommit').attr("class","btn btn-gray");
        });
        $("#memberRecipientCity").bind("change",function(){
            $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
            $("#queryAddress").val("");
            $(".adress-list").html("");
            $("#houseCode").val("");
            $('#queryCommit').attr("disabled","disabled");
            $('#queryCommit').attr("class","btn btn-gray");
        });

        $("#orderComfirm").click(function(){
            var isMbh = $("#isMbh").val();
            var isBroadBand = $("#isBroadband").val();

            if(isBroadBand == '0'){
                $("#form1").submit();
                return;
            }
            var messege = "";
            var pattern = /^[\u4E00-\u9FA5]{1,8}$/;
            if ($("#contact").val() == ""||!pattern.test($("#contact").val())) {
                messege+="姓名不能为空且长度不大于8!<br>"
            }
            var checkIdCardMsg = checkIdcard($("#idCard").val());
            if(checkIdCardMsg !="验证通过!"){
                messege+="身份证验证不通过！<br>"
            }
            if($("#houseCode").val()==""){
                messege+="请选择详细地址!<br>";
            }
            var bookInstallDate = $("#bookInstallDate").val();
            var bookInstallTime = $("#bookInstallTime").val();

            var bookYes = $('input:radio[name="bookYes"]:checked').val();
            if(bookYes=='1'){
                if(bookInstallDate==""){
                    messege+="请选择安装日期！<br>";
                }
                if(bookInstallTime==""){
                    messege+="请选择安装时间！<br>";
                }
            }
            if($("#imsTabShow").val()=='true'){
                if(!$("#ims_elementId").val()||!$("#ims_productId").val()||!$("#ims_packageId").val()){
                    messege+="请选择固话元素！<br>";
                }
                if(!$("#imsPhone").val()){
                    messege+="请选择固话号码！<br>";
                }
            }
            if($("#moreMagicTabShow").val()=='true'){
                if($("input[name='mbh']").length<1){
                    messege+="请选择魔百和！<br>";
                }
            }
            if(messege.length>0){
                showAlert(messege);
                return;
            }
            $("#form1").submit();
        });

    });



    function choosePayMode(obj,payMode,payModeName){
        $(".pay-btn").removeClass("active");
        $("." + obj).addClass("active");


        $("#payMode").val(payMode);
        $("#payModeName").val(payModeName);
    }

</script>

</html>