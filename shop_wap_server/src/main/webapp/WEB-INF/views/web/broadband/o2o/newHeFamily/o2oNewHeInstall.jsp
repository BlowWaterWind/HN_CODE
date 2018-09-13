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
    <!-- 宽带多级地址搜索框 -->
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>
</head>
<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>和家庭详情</h1>
    </div>
</div>
<div class="container">
    <form action="confirmHeInstallDetail" id="form1" name="form1" method="post">
        <input type="hidden" id="form1_skuId" name="form1_skuId" value="${goodsSkuId}"/>
        <input type="hidden" id="form1_goodsName" name="form1_goodsName" value="${form1_goodsName}" />
        <input type="hidden" id="form1_houseCode" name="form1_houseCode"/>
        <input type="hidden" id="form1_addressName" name="form1_addressName"/>
        <input type="hidden" id="form1_addrDesc" name="form1_addrDesc"/>
        <input type="hidden" id="form1_communityType" name="form1_communityType"/>
        <input type="hidden" id="form1_hasBroadband" name="form1_hasBroadband" value="${hasBroadband}"/>
        <input type="hidden" id="form1_hasMbh" name="form1_hasMbh" value="${hasMbh}"/>
        <input type="hidden" id="form1_maxWidth" name="form1_maxWidth"/>
        <input type="hidden" id="form1_freePort" name="form1_freePort"/>
        <input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode" value="${eparchyCode}"/>
        <input type="hidden" id="form1_county" name="form1_county"/>
        <input type="hidden" id="form1_coverType" name="form1_coverType"/>
        <input type="hidden" id="form1_chooseBandWidth" name="form1_chooseBandWidth" value="${chooseBandWidth}"/>
        <input type="hidden" id="form1_productId" name="form1_productId" value="${productId}"/>
        <input type="hidden" id="form1_packageId" name="form1_packageId" value="${packageId}"/>
        <input type="hidden" id="form1_discntCode" name="form1_discntCode" value="${discntCode}"/>
        <input type="hidden" id="form1_chooseCat" name="form1_chooseCat" value="${chooseCat}"/>
        <input type="hidden" id="form1_Mbh" name="form1_Mbh" value="${Mbh}"/>
        <input type="hidden" id="form1_phoneNum" name="form1_phoneNum" value="${form1_phoneNum}"/>
        <input type="hidden" id="form1_custName" name="form1_custName"/>
        <input type="hidden" id="form1_psptId" name="form1_psptId"/>
        <input type="hidden" id="form1_price" name="form1_price" value="${form1_price}"/>
        <input type="hidden" id="bookInstallDateParam" name="bookInstallDate"/>
        <input type="hidden" id="bookInstallTimeParam" name="bookInstallTime"/>
        <input type="hidden" id="form1_giveMbh" name="form1_giveMbh" value="${giveMbh}"/>
        <input type="hidden" id="payMode" name="payMode" value="0">
        <input type="hidden" id="payModeName" name="payModeName" value="现金支付">

    <div class="entry-info hjt-detail">

        <!--已选套餐 start-->
        <div class="entry-list">
            <div class="entry-title">已选套餐</div>
            <div class="detail-con">
                <p>${discntCode}元/月，包含${chooseBandWidth}M宽带+
                    <c:if test="${reissueType=='province'}">国内流量${homeData}</c:if>
                    <c:if test="${reissueType!='province'}">国内不限量${homeData}G不限速</c:if>
                    +国内${homeVoice}分钟语音（
                    <c:if test="${giveMbh!=0}"> ${giveMbh}元/月魔百和</c:if>
                    <c:if test="${giveMbh==0}"> 赠送魔百和</c:if>
                    <%--<c:if test="${giveData!=null&&giveData!=''}"> ${giveData}</c:if>--%>
                    <%--<c:if test="${giveData==null||giveData==''}"> 省内流量不限量</c:if>--%>
                    ）</p>
            </div>
        </div>
        <!--已选套餐 end-->
        <c:if test="${hasBroadband=='1'}">
        <!--用户信息 start-->
        <div class="entry-list">
            <div class="entry-title">用户信息</div>
            <ul class="entry-form">
                <li>
                    <label>姓名</label>
                    <div class="entry-rt">
                        <input type="text" class="entry-box" id="installName" placeholder="请输入身份证件姓名" name="installName"/>
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
                        <input type="text" class="entry-box" id="contactPhone" value="${form1_phoneNum}" name="contactPhone"/>
                        <p class="pt5 font-red hide">输入的手机号有误</p>
                    </div>
                </li>
            </ul>
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
        </div>
        <!--用户信息 end-->

        <!--安装时间 start-->
        <div class="entry-list hjt-date-list">
            <div class="entry-title">安装时间</div>
            <div class="hjt-date">
                <div class="date-list">
                    <span class="date-icon01">是否预约装机时间</span>
                    <div class="date-con">
                        <div class="date-text">
                            <input type="radio" name="isBookInstall" value="1" id="radio01" checked="checked"/>
                            <label for="radio01">是</label>
                        </div>
                        <div class="date-text">
                            <input type="radio" name="isBookInstall" value="0" id="radio02"/>
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
        </c:if>
        <!--安装时间 end-->

        <!--优惠赠送 start-->
        <c:if test="${hasMbh == '1'}">
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
                    <div class="discount-list">
                        <img src="${ctxStatic}/images/broadBand/hjt_tu.png" class="discount-img"/>
                        <div class="discount-info">
                            <p>牌照方</p>
                            <div class="disconunt-box">
                                <p>
                                    <input type="radio" name="boxType"  id='bx'  onclick="adc(this)" value="1"/>
                                    <span>未来电视</span></label>
                                </p>
                                <p>
                                    <input type="radio" name="boxType" id='bw'  onclick="adc(this)" value="0" />
                                    <span>芒果TV</span></label>
                                </p>
                            </div>
                            <p class="font-gray9 discount-txt">湖南自制剧、热播电影、综艺、电影片库、动漫等</p>
                        </div>
                        <span class="font-red">￥0.00</span>
                    </div>
                </div>
            </div>
        </c:if>
        <!--优惠赠送 end-->


        <%--<div class="wf-list change-new sub-new">--%>
            <%--<div class="wf-ait clearfix">--%>
                <%--<ul class="bar-list">--%>
                    <%--<li style="height:26px;line-height:26px;">--%>
                        <%--<span class="font-gray">支付方式：</span>--%>
                        <%--<div class="sub-text">--%>
                            <%--<a href="javascript:choosePayMode('pay-btn1','2','在线支付')" class="bar-btn pay-btn pay-btn1">在线支付</a>--%>
                            <%--<a href="javascript:choosePayMode('pay-btn2','8','先装后付')" class="bar-btn pay-btn pay-btn2">先装后付</a>--%>
                        <%--</div>--%>
                    <%--</li>--%>
                <%--</ul>--%>
            <%--</div>--%>
        <%--</div>--%>
    </div>
    </form>
</div>
<!--尾部 start-->
<div class="fix-br new-fixed">
    <div class="affix foot-box">
        <div class="container active-fix">
            <div class="fl-left">
                <p class="ft-total">初装费：¥<span class="font-gray6">${form1_price}元</span></p>
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
                    <%--<div class="modal-rt">--%>
                        <%--<select class="form-control" id="bookInstallDate" name="bookInstallDate">--%>
                        <%--</select>--%>
                    <%--</div>--%>
                    <div class="user-form" id="selectDate">
                        <input type="hidden" name="bookInstallDate" id="bookInstallDate">
                        <div class="form-control" data-year="" data-month="" data-date="" id="showDate">未选择</div>
                        <span class="arrow"></span>
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
    var countyUrl="<%=request.getContextPath()%>/broadband/getCountyFromBoss";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
    $(function () {
        //切换
        $(".sub-broad a").on("click", function () {
            $(this).siblings().removeClass("active");
            $(this).addClass("active");
        });
        //展开隐藏
        $("#open").click(function () {
            $(this).hide();
            $(".txt").show();
        });
        $("#close").click(function () {
            $(".txt").hide();
            $("#open").show();
        });
        $('input:radio[name="isBookInstall"]').change(function () {
            if ($(this).val() == "0") {
                $("#bookInstallDateParam").val("");
                $("#bookInstallTimeParam").val("");
                $("#bookInstallDate").val("");
                $("#bookInstallTime").val("");
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
            var houseCode = $("#form1_houseCode").val();
            if(houseCode.length==0){
                showAlert("请先选择详细地址!");
                return;
            }
            $("#myModal2").modal('show');
        });

        $("#orderComfirm").click(function(){
            var isBroadBand = $("input[name='form1_hasBroadband']").val();
            var contactPhone = $("#contactPhone").val();
            var isMbh = $("input[name='form1_hasMbh']").val();
            if(isMbh == '1'){
                $("#form1_Mbh").val($("input[name='boxType']:checked").val());
            }
            //存量转入
            if(isBroadBand == '0'){
                $("#form1").submit();
                return;
            }

            var messege = "";
            var pattern = /^[\u4E00-\u9FA5]{1,8}$/;
            var isMobile =/^1(([0-9][0-9]))\d{8}$/;
            if ($("#installName").val() == ""||!pattern.test($("#installName").val())) {
               messege+="姓名不能为空且长度不大于8!<br>"
            }
            var checkIdCardMsg = checkIdcard($("#idCard").val());
            if(checkIdCardMsg !="验证通过!"){
                messege+="身份证验证不通过！<br>"
            }
            if(!isMobile.test(contactPhone)){
                messege+="手机号码格式错误,手机号码为11位数字!<br>";
            }
            if($("#form1_houseCode").val()==""){
                messege+="请选择详细地址!<br>";
            }
            var bookInstallDate = $("#bookInstallDate").val();
            var bookInstallTime = $("#bookInstallTime").val();

            var isBookInstall = $('input:radio[name="isBookInstall"]:checked').val();
            if(isBookInstall=='1'){
                if(bookInstallDate==""){
                    messege+="请选择安装日期！<br>";
                }
                if(bookInstallTime==""){
                    messege+="请选择安装时间！<br>";
                }
            }
            if(messege.length>0){
                showAlert(messege);
                return;
            }

            if(isBroadBand == '1'){
                //在没有宽带的情况下光猫必须有
                $("#form1_chooseCat").val("1");
            }

            $("#form1_custName").val($("#installName").val());
            $("#form1_psptId").val($("#idCard").val());
            $("#form1").submit();
        });
        $("#memberRecipientCounty").bind("change",function(){
            $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
            $("#queryAddress").val("");
            $(".adress-list").html("");
            $("#form1_houseCode").val("");
            $('#queryCommit').attr("disabled","disabled");
            $('#queryCommit').attr("class","btn btn-gray");
        });
        $("#memberRecipientCity").bind("change",function(){
            $("#address").val("关键字搜索参考：请输入小区或周边标志性建筑");
            $("#queryAddress").val("");
            $(".adress-list").html("");
            $("#form1_houseCode").val("");
            $('#queryCommit').attr("disabled","disabled");
            $('#queryCommit').attr("class","btn btn-gray");
        });


    });
    function  adc(param){
        if($(param).attr('previousValue') == 'checked'){
            $(param).attr("checked",false);
            $(param).attr('previousValue', false);
        } else {
            $(param).attr('previousValue', 'checked');
        }
    }
    function choosePayMode(obj,payMode,payModeName){
        $(".pay-btn").removeClass("active");
        $("." + obj).addClass("active");


        $("#payMode").val(payMode);
        $("#payModeName").val(payModeName);
    }
    /**
     * 选择城市
     */
    $(function() {
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
    })

    function addDate(date, days) {
        if (days == undefined || days == '') {
            days = 1;
        }
        var date = new Date(date);
        date.setDate(date.getDate() + days);
        var month = date.getMonth() + 1;
        var day = date.getDate();
        return date.getFullYear() + '-' + getFormatDate(month) + '-' + getFormatDate(day);
    }

    // 日期月份/天的显示，如果是1位数，则在前面加上'0'
    function getFormatDate(arg) {
        if (arg == undefined || arg == '') {
            return '';
        }

        var re = arg + '';
        if (re.length < 2) {
            re = '0' + re;
        }

        return re;
    }
    //验证身份证函数
    function checkIdcard(idcard) {
        idcard = idcard.toString();
        var Errors = new Array("验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!", "身份证地区非法!");
        //var Errors=new Array(true,false,false,false,false);
        var area = {
            11: "北京",
            12: "天津",
            13: "河北",
            14: "山西",
            15: "内蒙古",
            21: "辽宁",
            22: "吉林",
            23: "黑龙江",
            31: "上海",
            32: "江苏",
            33: "浙江",
            34: "安徽",
            35: "福建",
            36: "江西",
            37: "山东",
            41: "河南",
            42: "湖北",
            43: "湖南",
            44: "广东",
            45: "广西",
            46: "海南",
            50: "重庆",
            51: "四川",
            52: "贵州",
            53: "云南",
            54: "西藏",
            61: "陕西",
            62: "甘肃",
            63: "青海",
            64: "宁夏",
            65: "新疆",
            71: "台湾",
            81: "香港",
            82: "澳门",
            91: "国外"
        }
        var idcard, Y, JYM;
        var S, M;
        var idcard_array = new Array();
        idcard_array = idcard.split("");
        //地区检验
        if (area[parseInt(idcard.substr(0, 2))] == null) return Errors[4];
        //身份号码位数及格式检验
        switch (idcard.length) {
            case 15:
                return Errors[0];
                break;
            case 18:
                //18 位身份号码检测
                //出生日期的合法性检查
                //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
                //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
                //if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0 )) {
                //    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
                //} else {
                //    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
                //}
                //if (ereg.test(idcard)) {//测试出生日期的合法性
                //计算校验位
                S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
                        + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
                        + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
                        + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
                        + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
                        + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
                        + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
                        + parseInt(idcard_array[7]) * 1
                        + parseInt(idcard_array[8]) * 6
                        + parseInt(idcard_array[9]) * 3;
                Y = S % 11;
                M = "F";
                JYM = "10X98765432";
                M = JYM.substr(Y, 1);//判断校验位
                if (M == idcard_array[17]) return Errors[0]; //检测ID的校验位
                else return Errors[3];
                //}
                //else return Errors[2];
                break;
            default:
                return Errors[1];
                break;
        }
    }
</script>

</html>