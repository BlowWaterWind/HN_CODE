<%--
  Created by IntelliJ IDEA.
  User: cc
  录入
  Date: 2017/9/18
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<%@ include file="/WEB-INF/views/include/message.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css" />

</head> <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" href="search"></a>
        <%--如果添加信息未成功，返回到搜索页面，如果添加成功，自动跳转到原信息收集页面--%>
        <h1>新增用户</h1>
    </div>
</div>
<div class="container">
    <form method="post" action="addUserInfo" id="addUserInfo">
        <input type="hidden" name="communityId" value="${o2oCommunityUserInfo.communityId}">
        <c:if test="${o2oCommunityInfoBak==null}">
            <input type="hidden" name="communityCode" value="${o2oCommunityUserInfo.communityId}">
            <input type="hidden" name="eparchy" value="${eparchy}">
        </c:if>
        <c:if test="${o2oCommunityInfoBak!=null}">
            <input type="hidden" name="communityCode" value="${o2oCommunityInfoBak.communityCode}">
            <input type="hidden" name="eparchy" value="${o2oCommunityInfoBak.eparchy}">
        </c:if>
        <input type="hidden" name="communityName" value="${o2oCommunityUserInfo.communityName}">
        <input type="hidden" name="addressPath" value="${o2oCommunityUserInfo.addressPath}">
        <input type="hidden" id="synchronizedType" value="${synchronizedType}">
    <ul class="user-add">
        <li>
            <label>用户姓名</label>
            <div class="user-form">
                <input id="userName" name="userName" type="text" class="form-control" autocomplete="off" placeholder="输入客户名称" />
            </div>
        </li>
        <li>
            <label>用户号码</label>
            <div class="user-form">
                <input id="serialNumber" name="serialNumber"  type="text" class="form-control" placeholder="客户对应的手机号码" />
            </div>
        </li>
        <li>
            <label>身份证号</label>
            <div class="user-form">
                <input id="cardId" name="cardId" type="text" class="form-control" placeholder="18位数身份证号码" />
            </div>
        </li>
        <li>
            <label>所在小区</label>
            <div class="user-form">
                <input id="addrDetail" name="addrDetail"  type="text" class="form-control" placeholder="${o2oCommunityUserInfo.addressPath}" />
            </div>
        </li>
        <li>
            <label>宽带运营商</label>
            <div class="user-form" >
                <input type="hidden" class="form-control" name="broadbandProvider" id="operaId" value="">
                <span id="opera" class="opera">未选择</span>
                <span class="arrow"></span>
            </div>
        </li>
        <li>
            <label>宽带资费</label>
            <div class="user-form">
                <input id="broadbandPrice" name="broadbandPrice" type="number"  maxlength="10"  oninput="if(value.length>10)value=value.slice(0,10)" class="form-control" placeholder="宽带的总费用" />
            </div>
        </li>
        <li>
            <label>宽带到期时间</label>
            <div class="user-form" id="selectDate">
                <input type="hidden" name="endTime"id="endTime">
                <div class="form-control" data-year="" data-month="" data-date="" id="showDate" >未选择</div>
                <span class="arrow"></span>
            </div>
        </li>
        <li>
            <label>用户类型</label>
            <div class="user-form">
                <input type="hidden" name="userType"id="userType">
                <span class="opera" id="user" >未选择</span>
                <span class="arrow"></span>
            </div>
        </li>
        <li>
            <label>近三个月消费</label>
            <div class="user-form">
                <input id="recentCost" name="recentCost" type="text"  maxlength="50"  class="form-control" placeholder="近三个月消费" />
            </div>
        </li>
        <li>
            <label>宽带套餐</label>
            <div class="user-form">
                <input id="broadbandPackage" name="broadbandPackage" type="text"  maxlength="50"  class="form-control" placeholder="宽带套餐" />
            </div>
        </li>
        <li>
            <label>家庭增值业务</label>
            <div class="user-form">
                <input id="familyAddedService" name="familyAddedService" type="text"  maxlength="50"  class="form-control" placeholder="家庭增值业务" />
            </div>
        </li>

    </ul>
    </form>
    <div id="userNameError" class="error-tip" style="display: none">请输入正确的用户名</div>
    <div id="serialNumberError" class="error-tip" style="display: none">请输入正确的11位电话号码</div>
    <div id="cardIdError" class="error-tip" style="display: none">请输入18位身份证号码</div>
    <div id="broadbandPriceError" class="error-tip" style="display: none">请输入宽带的价格/元</div>
    <div class="channel-btn"><a href="javascript:void(0)" class="broad-btn broad-btn-blue" id="userInfoBtn">确认提交</a></div>
    <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
</div>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/infoCollect/userInfo.js"></script>
<script type="text/javascript">
    var selectDateDom = $('#selectDate');
    var showDateDom = $('#showDate');
    // 初始化时间
    var now = new Date();
    var nowYear = now.getFullYear();
    var nowMonth = now.getMonth() + 1;
    var nowDate = now.getDate();
    showDateDom.attr('data-year', nowYear);
    showDateDom.attr('data-month', nowMonth);
    showDateDom.attr('data-date', nowDate);
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
    var yearData = function(callback) {
        callback(formatYear(nowYear))
    }
    var monthData = function(year, callback) {
        callback(formatMonth());
    };
    var dateData = function(year, month, callback) {
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
    selectDateDom.bind('click', function() {
        var oneLevelId = showDateDom.attr('data-year');
        var twoLevelId = showDateDom.attr('data-month');
        var threeLevelId = showDateDom.attr('data-date');
        var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
            title: '日期选择',
            itemHeight: 35,
            relation: [1, 1],
            oneLevelId: oneLevelId,
            twoLevelId: twoLevelId,
            threeLevelId: threeLevelId,
            showLoading: true,
            callback: function(selectOneObj, selectTwoObj, selectThreeObj) {
                showDateDom.attr('data-year', selectOneObj.id);
                showDateDom.attr('data-month', selectTwoObj.id);
                showDateDom.attr('data-date', selectThreeObj.id);
                showDateDom.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
                $("#endTime").val(selectOneObj.id+"-"+selectTwoObj.id+"-"+selectThreeObj.id);
            }
        });
    });
    var userType = [
        {'id': '1', 'value': '新增客户'},
        {'id': '2', 'value': '友商客户'},
        {'id': '3', 'value': '价值提升客户'},
        {'id': '4', 'value': '流失预警客户'}
    ];
    var userDom = document.querySelector('#user');
    var userIdDom = document.querySelector('#userType');//表单
    userDom.addEventListener('click', function () {
        var operaId = operaDom.dataset['id'];
        var operaName = operaDom.dataset['value'];

        var operaSelect = new IosSelect(1,
            [userType],
            {
                container: '.container',
                title: '用户类型',
                itemHeight: 50,
                itemShowCount: 3,
                oneLevelId: operaId,
                callback: function (selectOneObj) {
                    userIdDom.value = selectOneObj.id;
                    userDom.innerHTML = selectOneObj.value;
                    userDom.dataset['id'] = selectOneObj.id;
                    userDom.dataset['value'] = selectOneObj.value;
                }
            });
    });

    var data = [
        {'id': '10001', 'value': '中国移动'},
        {'id': '10002', 'value': '中国联通'},
        {'id': '10003', 'value': '中国电信'}
    ];
    var operaDom = document.querySelector('#opera');
    var operaIdDom = document.querySelector('#operaId');
    operaDom.addEventListener('click', function () {
        var operaId = operaDom.dataset['id'];
        var operaName = operaDom.dataset['value'];

        var operaSelect = new IosSelect(1,
            [data],
            {
                container: '.container',
                title: '宽带运营商',
                itemHeight: 50,
                itemShowCount: 3,
                oneLevelId: operaId,
                callback: function (selectOneObj) {
                    operaIdDom.value = selectOneObj.value;
                    operaDom.innerHTML = selectOneObj.value;
                    operaDom.dataset['id'] = selectOneObj.id;
                    operaDom.dataset['value'] = selectOneObj.value;
                }
            });
    });
</script>
</body>

</html>