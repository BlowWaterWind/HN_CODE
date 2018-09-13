<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/js/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
    <script src="${ctxStatic}/js/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
</head>
<script>
    var baseProject = "${ctx}";
    $(document).ready(function() {
        $("#form1").validate({
            submitHandler: function(form){
//                showLoadPop();
//                form.submit();
                showLoadPop();
                $.ajax({
                    url : "${ctx}/o2oBusiCollect/addCooResource",
                    type : "post",
                    dataType : "json",
                    data: $("#form1").serialize(),
                    success : function(data) {
                        hideLoadPop();
                        if(data.respCode=='0'){
                            showAlert(data.respDesc, function () {
                                window.location.href=baseProject+"o2oBusiCollect/search?type=5";
                            });
                        }else{
                            showAlert(data.respDesc);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        hideLoadPop();
                        showAlert("系统错误!");
                    }
                });
            },
            errorContainer: "#messageBox",
            errorPlacement: function(error, element) {
                $("#messageBox").text("输入有误，请先更正。");
                if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                    error.appendTo(element.parent().parent());
                } else {
                    error.insertAfter(element);
                }
            }
        });
        if('query'=='${query}'){
            $("input").each(function (index) {
                $(this).attr('readonly','true');
            });
        }
    });
</script>

<style>
    .user-add li label {
        width: 4.6rem;
        text-align: left;
    }
</style>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>“建营装维”第三方合作资源录入</h1>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/message.jsp" %>
<form method="post" name="form1" action="addCooResource" id="form1">
    <input type="hidden" name="communityName" value="${o2oCooResource.communityName}"/>
    <input type="hidden" name="communityId" value="${o2oCooResource.communityId}"/>
    <input type="hidden" name="addressPath" value="${o2oCooResource.addressPath}"/>
    <input type="hidden" name="eparchyCode" value="${o2oCooResource.eparchyCode}"/>
    <input type="hidden" name="eparchyName" value="${o2oCooResource.eparchyName}"/>
    <input type="hidden" name="countyCode" value="${o2oCooResource.countyCode}"/>
    <input type="hidden" name="countyName" value="${o2oCooResource.countyName}"/>
    <input type="hidden" name="isNew" value="${o2oCooResource.isNew}"/>
    <input type="hidden" name="recordId" value="${o2oCooResource.recordId}"/>
    <div class="container">
    <!--小区地址 start-->
    <div class="order-detail">
        <div class="adress-info">
            <div class="adress-txt">
                <p class="adress-number">${o2oCooResource.communityName}</p>
                <p class="channel-gray9 f12">${o2oCooResource.addressPath}</p>
            </div>
        </div>
    </div>
    <!--小区地址 start-->
    <div class="tabs broad-tabs mt10">
        <div id="tab-1">
                <ul class="user-add">
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>合作项目</label>
                        <div class="user-form">
                            <input id="cooperationName" name="cooperationName" type="text" class="form-control required"
                                   placeholder="输入合作项目" value="${o2oCooResource.cooperationName}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>合作类型</label>
                        <div class="user-form">
                            <input type="text" name="cooperationType" id="cooperationType" class="form-control required" value="${o2oCooResource.cooperationType}" placeholder="输入合作类型">
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>合同起始时间</label>
                        <div class="user-form" id="conStartTimeDiv">
                            <c:if test="${o2oCooResource.conStartTime != null}">
                                <input type="hidden" name="conStartTime" id="conStartTime" value="<fmt:formatDate value="${o2oCooResource.conStartTime}" pattern="yyyy-MM-dd"/>">
                                <div class="form-control" data-year="" data-month="" data-date="" id="conStartDate">
                                    <fmt:formatDate value="${o2oCooResource.conStartTime}" pattern="yyyy年MM月dd日"/>
                                </div>
                            </c:if>
                            <c:if test="${o2oCooResource.conStartTime == null}">
                                <input type="hidden" name="conStartTime" id="conStartTime" value="">
                                <div class="form-control" data-year="" data-month="" data-date="" id="conStartDate">
                                    未选择
                                </div>
                            </c:if>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>合同截止时间:</label>
                        <div class="user-form" id="conEndTimeDiv">
                            <c:if test="${o2oCooResource.conEndTime != null}">
                                <input type="hidden" name="conEndTime" id="conEndTime"
                                       value="<fmt:formatDate value="${o2oCooResource.conEndTime}" pattern="yyyy-MM-dd"/>">
                                <div class="form-control" data-year="" data-month="" data-date="" id="conEndDate">
                                    <fmt:formatDate value="${o2oCooResource.conEndTime}" pattern="yyyy年MM月dd日"/>
                                </div>
                            </c:if>
                            <c:if test="${o2oCooResource.conEndTime == null}">
                                <input type="hidden" name="conEndTime" id="conEndTime" value="">
                                <div class="form-control" data-year="" data-month="" data-date="" id="conEndDate">
                                    未选择
                                </div>
                            </c:if>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>合作端口数</label>
                        <div class="user-form">
                            <input id="portNum" name="portNum" type="text" class="form-control required number"
                                   placeholder="输入合作端口数" value="${o2oCooResource.portNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>覆盖家庭户数</label>
                        <div class="user-form">
                            <input id="coverNum" name="coverNum" type="text" class="form-control required number"
                                   placeholder="输入覆盖家庭户数" value="${o2oCooResource.coverNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>已发展用户数</label>
                        <div class="user-form">
                            <input id="devPeopleNum" name="devPeopleNum" type="text" class="form-control required number"
                                   placeholder="输入已发展用户数" value="${o2oCooResource.devPeopleNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>分成标准</label>
                        <div class="user-form">
                            <input id="rsrv1" name="rsrv1" type="text" class="form-control required"
                                   placeholder="输入分成标准（百分制）" value="${o2oCooResource.rsrv1}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>合同金额</label>
                        <div class="user-form">
                            <input id="conFee" name="conFee" type="text" class="form-control required number"
                                   placeholder="输入合同金额 单位元" value="<fmt:formatNumber pattern="#.##" value="${o2oCooResource.conFee}"/>"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>累计结算金额</label>
                        <div class="user-form">
                            <input id="balanceFee" name="balanceFee" type="text" class="form-control required number"
                                   placeholder="输入累计结算金额 单位元" value="<fmt:formatNumber pattern="#.##" value="${o2oCooResource.balanceFee}"/>"/>
                        </div>
                    </li>
                </ul>
                    <div class="channel-btn">
                        <c:if test="${query ne 'query'}">
                        <a href="javascript:void(0)" class="broad-btn broad-btn-blue" id="insertBtn" style="padding: 0px;">确认提交</a>
                        </c:if>
                    </div>
                <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
        </div>
        <div class="content">
            <div id="partnerInfoList">
            </div>
            <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
            <ul class="marke-user" id="userInfos">
            </ul>
            <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
        </div>
    </div>
</div>
</form>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/comm.js"></script>
<script type="text/javascript">
    var conEndDate = $("#conEndDate");
    var conStartDate = $("#conStartDate");
    // 初始化时间
    var now = new Date();
    var nowYear = now.getFullYear();
    var nowMonth = now.getMonth() + 1;
    var nowDate = now.getDate();
    conEndDate.attr('data-year', nowYear);
    conEndDate.attr('data-month', nowMonth);
    conEndDate.attr('data-date', nowDate);
    conStartDate.attr('data-year', nowYear);
    conStartDate.attr('data-month', nowMonth);
    conStartDate.attr('data-date', nowDate);
    // 数据初始化
    function formatYear(nowYear) {
        var arr = [];
        for (var i = nowYear - 5; i <= nowYear + 5; i++) {
            arr.push({
                id: i + '',
                value: i + '年'
            });
        }
        return arr;
    }

    function formatMonth() {
        var arr = [];
        for (var i = 1; i <= 12; i++) {
            arr.push({
                id: i + '',
                value: i + '月'
            });
        }
        return arr;
    }

    function formatDate(count) {
        var arr = [];
        for (var i = 1; i <= count; i++) {
            arr.push({
                id: i + '',
                value: i + '日'
            });
        }
        return arr;
    }
    var yearData = function (callback) {
        callback(formatYear(nowYear))
    }
    var monthData = function (year, callback) {
        callback(formatMonth());
    };
    var dateData = function (year, month, callback) {
        if (/^(1|3|5|7|8|10|12)$/.test(month)) {
            callback(formatDate(31));
        } else if (/^(4|6|9|11)$/.test(month)) {
            callback(formatDate(30));
        } else if (/^2$/.test(month)) {
            if (year % 4 === 0 && year % 100 !== 0 || year % 400 === 0) {
                callback(formatDate(29));
            } else {
                callback(formatDate(28));
            }
        } else {
            throw new Error('month is illegal');
        }
    };
    conStartDate.bind('click', function () {
        var oneLevelId = conStartDate.attr('data-year');
        var twoLevelId = conStartDate.attr('data-month');
        var threeLevelId = conStartDate.attr('data-date');
        var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
            title: '日期选择',
            itemHeight: 35,
            relation: [1, 1],
            oneLevelId: oneLevelId,
            twoLevelId: twoLevelId,
            threeLevelId: threeLevelId,
            showLoading: true,
            callback: function (selectOneObj, selectTwoObj, selectThreeObj) {
                conStartDate.attr('data-year', selectOneObj.id);
                conStartDate.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
                $("#conStartTime").val(selectOneObj.id + "-" + selectTwoObj.id + "-" + selectThreeObj.id);
            }
        });
    });


    conEndDate.bind('click', function () {
        var oneLevelId = conEndDate.attr('data-year');
        var twoLevelId = conEndDate.attr('data-month');
        var threeLevelId = conEndDate.attr('data-date');
        var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
            title: '日期选择',
            itemHeight: 35,
            relation: [1, 1],
            oneLevelId: oneLevelId,
            twoLevelId: twoLevelId,
            threeLevelId: threeLevelId,
            showLoading: true,
            callback: function (selectOneObj, selectTwoObj, selectThreeObj) {
                conEndDate.attr('data-year', selectOneObj.id);
                conEndDate.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
                $("#conEndTime").val(selectOneObj.id + "-" + selectTwoObj.id + "-" + selectThreeObj.id);
            }
        });
    });

    $("#insertBtn").click(function(){
        $("#form1").submit();
    });
</script>
</body>
</html>