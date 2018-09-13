<%--
  Created by IntelliJ IDEA.
  User: cc
  小区信息,友商营销信息,用户信息录入
  Date: 2017/9/18
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<%@ taglib prefix="BMap" uri="/WEB-INF/tlds/baidu.tld" %>
<!DOCTYPE html>
<html>

<head>

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=2.0&ak=CxWGT8rEbug80GBC0maXVG0xCRioYbal"></script>

    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>

    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/Bmap.css" rel="stylesheet" type="text/css"/>
</head>
<script>
    var infoType = ${infoType};
    var addrType1 = {
        "0": "封闭小区", "1": "开放小区", "2": "写字楼", "3": "酒店", "4": "大型企业办公室", "5": "市场",
        "7": "学校", "8": "宿舍楼", "9": "党政军", "10": "工业园区", "11": "住宅小区", "12": "沿街商铺", "13": "住宅区商铺", "14": "商业楼宇",
        "15": "城中村", "16": "棚户区", "17": "村(除镇外)", "18": "其他", "19": "医院"
    };
    var addrType2 = {"1": "标准小区", "2": "非标准小区"};
    var areaType = {"1": "市城区", "2": "县城区", "3": "农村", "4": "乡镇"};
    var userScene = {"1": "家庭场景", "2": "校园场景", "3": "聚类场景"};
    var blindLevel = {"1":"小区录入","2":"楼栋录入"};
    var buildCoveType = {"0":"塔楼","1":"板楼","2":"独栋","3":"其他"};
    var buildingCoverType = {"0":"FTTH","1":"FTTB","2":"学校FTTB","3":"学校FTTH","4":"学校LAN","5":"合作宽带","6":"农村无线宽带","10":"铁通FTTB","11":"铁通FTTH","12":"铁通ADSL","13":"铁通学校FTTB","14":"铁通学校FTTH",
        "15":"铁通学校LAN","16":"铁通合作宽带","17":"校园AP宽带"}
    var baseProject = "${ctx}";
    var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
        + "wxyz0123456789+/" + "=";
</script>

<style>
    .user-add li label {
        width: 4.6rem;
        text-align: left;
    }
</style>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back"></a>
        <h1>信息录入</h1>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/message.jsp" %>
<form method="post" id="keepCommuntyInfo">
    <input type="hidden" name="communityId" value="${o2oCommunity.communityId}">
    <input type="hidden" name="zoneName" value="${o2oCommunity.zoneName}">
    <input type="hidden" name="addressPath" value="${o2oCommunity.addressPath}">
    <input type="hidden" name="type" id="type">
    <input type="hidden" name="synchronizedType" value="0">
    <input type="hidden" name="pageNoPartner" id="pageNoPartner" value=""/>
    <input type="hidden" name="pageNoUser" id="pageNoUser" value=""/>
    <input type="hidden" name="pageSize" id="pageSize" value="4"/>
</form>
<div class="container">
    <c:if test="${infoType!='1'}">
        <div class="order-detail">
            <div class="adress-info">
                <div class="adress-txt">
                    <p class="adress-number">${o2oCommunityInfoBak.zoneName}</p>
                    <p class="channel-gray9 f12">${o2oCommunityInfoBak.addressPath}</p>
                </div>
            </div>
        </div>
    </c:if>
    <!--小区地址 start-->
    <div class="tabs broad-tabs mt10">
        <ul class="tab-menu">
            <li class="active">小区信息</li>
            <li>友商营销信息</li>
            <%--<li>用户信息</li>--%>
        </ul>
        <div id="tab-1">
            <form method="post" action="saveCommunityInfo" id="communityInfo">
                <div class="container bg-gray hy-tab">
                    <div class="wf-list tab-con">
                        <div class="tab-tit font-red">请点选您所在的地市和区域，然后在查询框内输入您的地址</div>
                        <!--地市选择 start-->
                        <ul class="change-list">
                            <li>
                                <label>地&emsp;&emsp;区：</label>
                                <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <input type="hidden" name="eparchyName" id="eparchyName" value="${o2oCommunity.eparchyName}"/>
          <input type="hidden" name="eparchy" id="eparchy" value="${o2oCommunity.eparchy}"/>
          <%--<input type="hidden" name="latitude" id="latitude">--%>
          <%--<input type="hidden" name="longitude" id="longitude">--%>
          <select id="eparchySelect" class="set-sd" name="routeEparchyCode">
              <%--<option value="长沙市">长沙市</option>--%>
              <c:forEach items="${cityNames}" var="cityName">
                  <option value="${cityName.key}">${cityName.value}</option>
              </c:forEach>
          </select>
          <input type="hidden" name="cityName" id="cityName" value="${o2oCommunity.cityName}"/>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
          <select id="city" class="set-sd city-area" name="city">
              <%--<option value="岳麓区">岳麓区</option>--%>
          </select>
          </span></div>
                            </li>
                            <li>
                                <label>街&emsp;&emsp;道：</label>
                                <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <input type="hidden" name="streetName" id="streetName" value="${o2oCommunity.streetName}"/>
          <select id="street" class="set-sd" name="street">
          </select>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
           <input type="hidden" name="roadName" id="roadName" value="${o2oCommunity.roadName}"/>
          <select id="road" class="set-sd road-area" name="road">
              <%--<option value="岳麓区">岳麓区</option>--%>
          </select>
          </span></div>
                            </li>
                        </ul>
                        <!--地市选择 end-->
                    </div>
                </div>
                <input type="hidden" name="communityId" value="${o2oCommunity.communityId}">
                <input type="hidden" name="communityCode" id="communityCode" value="${o2oCommunityInfoBak.communityCode}">
                <input type="hidden" name="addressPath" value="${o2oCommunity.addressPath}">
                <input type="hidden" name="synchronizedType" value="0">
                <ul class="user-add">
                    <%--<li>--%>
                        <%--<label>百度地图:</label>--%>
                        <%--<div class="user-form">--%>
                            <%--<BMap:map callback="callbackMap" coordinate="WGS84" bmapStyle="width: 80%;"/>--%>
                        <%--</div>--%>
                    <%--</li>--%>
                    <li>
                        <label>地址类型1:</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control" name="baAddrType" id="operaId"
                                   value="${o2oCommunity.baAddrType}"/>
                            <span id="opera" class="opera">未选择</span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>地址类型2:</label>
                        <div class="user-form">
                            <input type="hidden" name="baCellType" id="baCellType" value="${o2oCommunity.baCellType}">
                            <span id="bacell" class="opera">未选择</span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>城乡类型</label>
                        <div class="user-form" id="areaType">
                            <input type="hidden" name="baType" id="areaTypeInput" value="${o2oCommunity.baType}">
                            <span id="areaDom" class="opera">未选择</span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>用户场景:</label>
                        <div class="user-form" id="USER_SCENE">
                            <input type="hidden" name="baUserScene" id="USERSCENE" value="${o2oCommunity.baUserScene}">
                            <span id="scene" class="opera">
                                <c:if test="${o2oCommunity.baUserScene == null}">
                                    未选择
                                </c:if>
                                <c:if test="${o2oCommunity.baUserScene != null}">
                                    ${o2oCommunity.baUserScene}
                                </c:if>
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>小区名称</label>
                        <div class="user-form">
                            <input id="zoneName" name="zoneName" type="text" class="form-control"
                                   placeholder="客户精准的住宅" value="${o2oCommunity.zoneName}"/>
                        </div>
                    </li>
                        <c:if test="${infoType!='1'}">
                            <li>
                                <label>楼栋名称</label>
                                <div class="user-form">
                                    <input id="buildinfName" name="buildinfName" type="text" class="form-control" maxlength="40"
                                           placeholder="客户精准的楼栋名称" value="${o2oCommunity.buildinfName}"/>
                                </div>
                            </li>
                            <li>
                                <label>楼栋类型</label>
                                <div class="user-form">
                                    <input type="hidden" name="buildingType" id="buildingType" value="${o2oCommunity.buildingType}">
                                    <span id="bType" class="opera">未选择</span>
                                    <span class="arrow"></span>
                                </div>
                            </li>
                            <li>
                                <label>单元</label>
                                <div class="user-form">
                                    <input id="unitName" name="unitName" type="number" class="form-control" maxlength="40"
                                           placeholder="客户精准的单元" value="${o2oCommunity.unitName}"/>
                                </div>
                            </li>
                            <li>
                                <label>房号</label>
                                <div class="user-form">
                                    <input id="roomNumber" name="roomNumber" type="text" class="form-control" maxlength="40"
                                           placeholder="客户精准的房号" value="${o2oCommunity.roomNumber}"/>
                                </div>
                            </li>
                            <li>
                                <label>楼栋覆盖类型</label>
                                <div class="user-form">
                                    <input type="hidden" name="buildingCoverType" id="buildingCoverType" value="${o2oCommunity.buildingCoverType}">
                                    <span id="bCoverType" class="opera">未选择</span>
                                    <span class="arrow"></span>
                                </div>
                            </li>
                        </c:if>
                    <li>
                        <label>交维时间</label>
                        <div class="user-form" id="selectDelivery">
                            <c:if test="${o2oCommunity.baBuiltYear != null}">
                                <input type="hidden" name="baBuiltYear" id="baBuiltYear"
                                       value="${o2oCommunity.baBuiltYear}">
                                <div class="form-control" data-year="" data-month="" data-date="" id="deliveryDate">
                                    <fmt:formatDate value="${o2oCommunity.baBuiltYear}" pattern="yyyy年MM月dd日"/>
                                </div>
                            </c:if>
                            <c:if test="${o2oCommunity.baBuiltYear == null}">
                                <input type="hidden" name="baBuiltYear" id="baBuiltYear" value="">
                                <div class="form-control" data-year="" data-month="" data-date="" id="deliveryDate">
                                    未选择
                                </div>
                            </c:if>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>楼宇数量</label>
                        <div class="user-form">
                            <input id="buildingNum" name="baBuildingCnt" type="number" class="form-control"
                                   placeholder="输入数量（幢）" value="${o2oCommunity.baBuildingCnt}"/>
                        </div>
                    </li>
                    <li>
                        <label>用户数</label>
                        <div class="user-form">
                            <input id="baFmyCnt" name="baFmyCnt" type="number" class="form-control" maxlength="8"
                                   placeholder="输入用户数量" value="${o2oCommunity.baFmyCnt}"/>
                        </div>
                    </li>
                    <li>
                        <label>反馈者姓名</label>
                        <div class="user-form">
                            <input id="FEEDBACK_PERSON" name="feedbackPerson" type="text" class="form-control"
                                   placeholder="输入反馈者姓名" value="${o2oCommunity.feedbackPerson}"/>
                        </div>
                    </li>
                    <li>
                        <label>反馈者号码</label>
                        <div class="user-form">
                            <input id="FEEDBACK_MOBILE" name="feedbackMobile" type="text" class="form-control"
                                   placeholder="输入反馈者手机号码" value="${o2oCommunity.feedbackMobile}"/>
                        </div>
                    </li>
                </ul>
                <%--<input type="hidden" name="staffPwd" id="staffPwd"/>--%>
                <div id="buildingNumError" class="error-tip" style="display: none">请输入楼宇数量</div>
                <div id="zoneNameError" class="error-tip" style="display: none">请输入小区名称</div>
                <c:if test="${infoType == '2'}">
                <div class="channel-btn"><a href="javascript:void(0)" class="broad-btn broad-btn-gray"
                                            style="padding: 0px;">同步CRM的信息无法修改</a></div>
                </c:if>
                <c:if test="${infoType == '1'||infoType == '0'}">
                    <div class="channel-btn"><a href="javascript:void(0)" class="broad-btn broad-btn-blue"
                                                id="insertBtn" style="padding: 0px;">确认提交</a></div>
                </c:if>
                <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
            </form>
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
    <%--两个tab的按钮--%>
    <div id="partnerAdd" class="channel-btn channel-footer"><a onclick="initAddPartnerInfo()"
                                                               class="broad-btn broad-btn-blue" style="padding: 0px;">新增友商信息</a>
    </div>
    <%--<div id="userAdd" class="channel-btn channel-footer"><a onclick="initAddUserInfo()"--%>
                                                            <%--class="broad-btn broad-btn-blue" style="padding: 0px;">新增用户信息</a>--%>
    <%--</div>--%>
</div>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<%--<script type="text/javascript" src="${ctxStatic}/js/BMap.js"></script>--%>
<script type="text/javascript" src="${ctxStatic}/js/dropload/dropload.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/comm.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/infoCollect/infoCollect.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
<script>
    $(function () {
        //tabs
        var id = '${tabType}';
        if (id == "a2") {
            $($(".tab-menu li")[1]).click();
        } else if (id == "a3") {
            $($(".tab-menu li")[2]).click();
        } else {
            $($(".tab-menu li")[0]).click()
        }
    });
</script>
<%--fetch partnerInfo--%>
<script type="text/html" id="partnerInfo">
    {{each list as partnerInfo}}
    <div class="marke-info">
        <p>{{partnerInfo.partnersName}}<span class="pl10">{{partnerInfo.communityName}}</span></p>
        <p><label class="channel-gray9">宽带覆盖情况：</label>{{partnerInfo.coverInfo}}</p>
        <p><label class="channel-gray9">是否独家进驻：</label>{{partnerInfo.activityAddress}}</p>
        <p><label class="channel-gray9">宽带客户数量：</label>{{partnerInfo.userNum}}</p>
        <p><label class="channel-gray9">宽带营销及回挖政策：</label>{{partnerInfo.stealBackPolicy}}</p>
        <p class="channel-gray9">现场活动图片：</p>
        <ul class="marke-list">
            {{if partnerInfo.imageUrlA}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlA)}}"/></li>
            {{/if}}
            {{if partnerInfo.imageUrlB}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlB)}}"/></li>
            {{/if}}
            {{if partnerInfo.imageUrlC}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlC)}}"/></li>
            {{/if}}
            {{if partnerInfo.imageUrlD}}
            <li><img src="{{logoUrl(partnerInfo.imageUrlD)}}"/></li>
            {{/if}}
        </ul>
    </div>
    {{/each}}
</script>
<script type="text/html" id="userInfo">
    {{each list as userInfo}}
    <li>
        <div class="marke-con">
            <p class="marke-txt"><span>{{userInfo.userName}}</span><span>{{userInfo.cardId}}</span><span>{{userInfo.serialNumber}}</span>
            </p>
            <p class="channel-gray9 f12">{{userInfo.broadbandProvider}} {{userInfo.broadbandPrice}}/年
                {{timeConver(userInfo.endTime,"yyyy-MM-dd")}}</p>
        </div>
    </li>
    {{/each}}
</script>
<script type="text/javascript">
    var data1 = [];
    <c:forEach items="${addrType}" var="item">
    var jsonObj = {};
    jsonObj.id = "${item.value}";
    jsonObj.value = "${item.label}";
    data1.push(jsonObj);
    </c:forEach>
    var operaDom = document.querySelector('#opera');
    var operaIdDom = document.querySelector('#operaId');
    operaDom.addEventListener('click', function () {
        var operaId = operaDom.dataset['id'];
        var operaName = operaDom.dataset['value'];

        var operaSelect = new IosSelect(1,
            [data1],
            {
                container: '.container',
                title: '地址类型1',
                itemHeight: 50,
                itemShowCount: 3,
                oneLevelId: operaId,
                callback: function (selectOneObj) {
                    operaIdDom.value = selectOneObj.id;
                    operaDom.innerHTML = selectOneObj.value;
                    operaDom.dataset['id'] = selectOneObj.id;
                    operaDom.dataset['value'] = selectOneObj.value;
                }
            });
    });


    var data2 = [];
    <c:forEach items="${cellType}" var="item">
    var jsonObj = {};
    jsonObj.id = "${item.value}";
    jsonObj.value = "${item.label}";
    data2.push(jsonObj);
    </c:forEach>
    var baCellDom = document.querySelector('#bacell');
    var baCellIdDom = document.querySelector('#baCellType');
    baCellDom.addEventListener('click', function () {
        var baCellId = baCellDom.dataset['id'];
        var operaName = baCellDom.dataset['value'];

        var operaSelect = new IosSelect(1,
            [data2],
            {
                container: '.container',
                title: '地址类型2',
                itemHeight: 50,
                itemShowCount: 3,
                oneLevelId: baCellId,
                callback: function (selectOneObj) {
                    baCellIdDom.value = selectOneObj.value;
                    baCellDom.innerHTML = selectOneObj.value;
                    baCellDom.dataset['id'] = selectOneObj.id;
                    baCellDom.dataset['value'] = selectOneObj.value;
                }
            });
    });

    var data3 = [];
    <c:forEach items="${areaType}" var="item">
    var jsonObj = {};
    jsonObj.id = "${item.value}";
    jsonObj.value = "${item.label}";
    data3.push(jsonObj);
    </c:forEach>
    var areaDom = document.querySelector('#areaDom');
    var areaIdDom = document.querySelector('#areaTypeInput');
    areaDom.addEventListener('click', function () {
        var areaId = areaDom.dataset['id'];
        var operaName = areaDom.dataset['value'];

        var operaSelect = new IosSelect(1,
            [data3],
            {
                container: '.container',
                title: '城乡类型',
                itemHeight: 50,
                itemShowCount: 3,
                oneLevelId: areaId,
                callback: function (selectOneObj) {
                    areaIdDom.value = selectOneObj.value;
                    areaDom.innerHTML = selectOneObj.value;
                    areaDom.dataset['id'] = selectOneObj.id;
                    areaDom.dataset['value'] = selectOneObj.value;
                }
            });
    });

    var data4 = [];
    <c:forEach items="${userScene}" var="item">
    var jsonObj = {};
    jsonObj.id = "${item.value}";
    jsonObj.value = "${item.label}";
    data4.push(jsonObj);
    </c:forEach>
    var sceneDom = document.querySelector('#scene');
    var sceneIdDom = document.querySelector('#USERSCENE');
    sceneDom.addEventListener('click', function () {
        var sceneId = sceneDom.dataset['id'];
        var operaName = sceneDom.dataset['value'];

        var operaSelect = new IosSelect(1,
            [data4],
            {
                container: '.container',
                title: '用户场景',
                itemHeight: 50,
                itemShowCount: 3,
                oneLevelId: sceneId,
                callback: function (selectOneObj) {
                    sceneIdDom.value = selectOneObj.value;
                    sceneDom.innerHTML = selectOneObj.value;
                    sceneDom.dataset['id'] = selectOneObj.id;
                    sceneDom.dataset['value'] = selectOneObj.value;
                }
            });
    });
    //如果是录入楼栋才需要加载
    if(infoType=='0'){
        var data5 = [];
        <c:forEach items="${buildType}" var="item">
        var jsonObj = {};
        jsonObj.id = "${item.value}";
        jsonObj.value = "${item.label}";
        data5.push(jsonObj);
        </c:forEach>
        var blindDom = document.querySelector('#bType');
        var blindIdDom = document.querySelector('#buildingType');
        blindDom.addEventListener('click', function () {
            var areaId = blindDom.dataset['id'];
            var operaName = blindDom.dataset['value'];

            var operaSelect = new IosSelect(1,
                    [data5],
                    {
                        container: '.container',
                        title: '楼栋类型',
                        itemHeight: 50,
                        itemShowCount: 3,
                        oneLevelId: areaId,
                        callback: function (selectOneObj) {
                            blindIdDom.value = selectOneObj.id;
                            blindDom.innerHTML = selectOneObj.value;
                            blindDom.dataset['id'] = selectOneObj.id;
                            blindDom.dataset['value'] = selectOneObj.value;
                        }
                    });
        });

        var data6 = [];
        <c:forEach items="${buildCoverType}" var="item">
        var jsonObj = {};
        jsonObj.id = "${item.value}";
        jsonObj.value = "${item.label}";
        data6.push(jsonObj);
        </c:forEach>
        var coverDom = document.querySelector('#bCoverType');
        var coverIdDom = document.querySelector('#buildingCoverType');
        coverDom.addEventListener('click', function () {
            var areaId = coverDom.dataset['id'];
            var operaName = coverDom.dataset['value'];

            var operaSelect = new IosSelect(1,
                    [data6],
                    {
                        container: '.container',
                        title: '楼栋覆盖类型',
                        itemHeight: 50,
                        itemShowCount: 3,
                        oneLevelId: areaId,
                        callback: function (selectOneObj) {
                            coverIdDom.value = selectOneObj.id;
                            coverDom.innerHTML = selectOneObj.value;
                            coverDom.dataset['id'] = selectOneObj.id;
                            coverDom.dataset['value'] = selectOneObj.value;
                        }
                    });
        });
    }
    $(function () {
        //tabs
        var id = '${tabType}';
        if (id == "tab2") {
            $($(".tab-menu li")[1]).click();
        } else if (id == "tab3") {
            $($(".tab-menu li")[2]).click();
        } else {
            $($(".tab-menu li")[0]).click()
        }
    });
    var addressQuery =
        {
            cityName: "长沙", //市
            cityCode: "0731", //市区域编码
            city: "233199472",
            cityArea: "天心", //区
            street: "-133509970",
            streetName: "金洲国际城街道办事处",
            road: "-133509974",
            roadName: "金湖路",
            communityAddressName: "", //小区
            buildingName: "", //楼栋
            keyWord: "", //关键字
            lastAddress: "", //最终地址
            maxWidth: "", //最大带宽

            /**
             * 查询城市区域
             * @param cityCode 市区域编码
             */
            qryCityArea: function (cityCode) {
                $('.city-area').html("<option value=''>请选择</option>");
                $('#street').html("");
                $('#road').html("");
                if(cityCode==''){
                    return;
                }
                $.ajaxSettings.async = false;
                $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': cityCode || '0731'
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
//                            $('.city-area').html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('.city-area').append("<option value=" + item.CITY_ID + ">" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
                );
            },

            /**
             * 查询街道区域
             * @param cityCode 市区域编码
             */
            qryStreetArea: function (street) {
                $('#street').html("<option value=''>请选择</option>");
                $('#road').html("");
                if(street==''){
                    return;
                }
                $.ajaxSettings.async = false;
                $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': street || '0731'
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
//                            $('#street').html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('#street').append("<option value=" + item.CITY_ID + ">" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
                );
            },
            /**
             * 查询路巷名称
             * @param cityCode 市区域编码
             */
            qryRoadArea: function (road) {
                $('#road').html("");
                if(road==''){
                    return;
                }
                $.ajaxSettings.async = false;
                $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': road
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
//                            $('#road').html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('#road').append("<option value=" + item.CITY_ID + ">" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
                );
            },

            //TODO 调用根据区查询街道的接口
            //todo 调用查询路的接口
        };

    $(function () {
        if (infoType == '2') {
            //注入小区的信息
            $("#opera").html(addrType1['${o2oCommunity.baAddrType}']);
            $("#bacell").html('${o2oCommunity.baCellType}');
            $("#areaDom").html('${o2oCommunity.baType}');
            $("#scene").html('${o2oCommunity.baUserScene}');
            $("#bType").html(buildCoveType['${o2oCommunity.buildingType}']);
            $("#bCoverType").html(buildingCoverType['${o2oCommunity.buildingCoverType}']);
            //注入地址信息
            $("#eparchySelect").find("option[value='${o2oCommunity.eparchy}']").attr("selected", true);
            addressQuery.cityCode = '${o2oCommunity.eparchy}';
            addressQuery.city = '${o2oCommunity.city}';
            addressQuery.street = '${o2oCommunity.street}';
            addressQuery.qryCityArea(addressQuery.cityCode);
            addressQuery.qryStreetArea(addressQuery.city);
            addressQuery.qryRoadArea(addressQuery.street);
            $("#city").find("option[value='${o2oCommunity.city}']").attr("selected", true);
            $("#street").find("option[value='${o2oCommunity.street}']").attr("selected", true);
            $("#road").find("option[value='${o2oCommunity.road}']").attr("selected", true);
        } else {
            addressQuery.qryCityArea(addressQuery.cityCode); //默认初始化加载
        }
        $("#eparchySelect").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityCode = $(this).val();
            addressQuery.cityName = $(this).find("option:selected").text();
            addressQuery.qryCityArea(addressQuery.cityCode);
        });

        $("#city").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.city = $(this).val();
            addressQuery.cityArea = $(this).find("option:selected").text();
            addressQuery.qryStreetArea(addressQuery.city);
        });
        $("#street").live('change', function () {
            addressQuery.street = $(this).val();
            addressQuery.streetName = $(this).find("option:selected").text();
            addressQuery.qryRoadArea(addressQuery.street)
        });
        $("#txt").live('input propertychange', function () {  //关键字级别选择事件监听
            addressQuery.keyWord = $(this).val();
            addressQuery.cityArea = $(".city-area").val();
            $(".adress-list").html(""); //清空无效信息
            addressQuery.qryLastAddress(addressQuery.cityName, addressQuery.cityArea, addressQuery.keyWord);//关键字查询
        });

        $("#queryCommit").click(function () {  //地址信息选中事件监听
            var address = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").text();
            var houseCode = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("last-address-building");
            var coverType = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("coverType");

            showLoadPop("正在建设中...");
        });
    });

    /**
     * 查询楼栋
     * @param residence 小区名
     */
    function searchBuilding2(obj) {
        $(obj).addClass('cur').siblings().removeClass('cur');
        var residence = $(obj).attr("address-path");
        $("#residential").val(residence);

        $.getJSON(ctx + 'bandResourceQuery/qryAddressBuildingName', {
                'communityAddressName': encodeURI(residence, "UTF-8")
            }, function (e) {
                if (e.respDesc == "OK!") //接口请求成功
                {
                    $(".adress-list").html("");
                    var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                    if (ADDRESS_INFO.length == 0) {
                        $(".adress-list").append("该地址暂未开通宽带办理");
                        return;
                    }
                    $.each(ADDRESS_INFO, function (i, item) {
                        $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-xz' onclick=chooseBuilding2(this)>" +
                            "<input type='radio' name='radio_address'></span>" +
                            "<span coverType=" + item.COVER_TYPE + " address-path-building=" + item.ADDRESS_PATH + " class='adr-text'  onclick=chooseBuilding2(this)>" + item.BUILDING_NAME + "</span>" +
                            "</label></li>");
                    });
                }
            }
        );
    }
    /*
     * 选择楼栋
     * @param obj
     * @returns
     */
    function chooseBuilding2(obj) {
        var ADDRESS_PATH = $(obj).attr("address-path-building");
        $.getJSON(ctx + 'bandResourceQuery/qryAddressName', {
                'addressKeyString': encodeURI(ADDRESS_PATH, "UTF-8")
            }, function (e) {
                if (e.respDesc == "OK!") //接口请求成功
                {
                    $(".adress-list").html("");
                    var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                    if (ADDRESS_INFO.length == 0) {
                        $(".adress-list").append("该地址暂未开通宽带资源办理");
                        return;
                    }
                    $.each(ADDRESS_INFO, function (i, item) {
                        //console.log(item);
                        $(".adress-list").append("<li><label><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-xz' onclick=chooseLastAddress2(this)><input type='radio' name='radio_address'></span><span coverType=" + item.COVER_TYPE + " last-address-building=" + item.HOUSE_CODE + " class='adr-text' onclick=chooseLastAddress2(this)>" + item.ADDRESS_NAME + "</span></label></li>");
                    });
                } else {
                    alert("系统异常，请稍后再试");
                }
            }
        );

    }
    function chooseLastAddress2(obj) {
        $('#queryCommit').removeAttr("disabled");
        $('#queryCommit').attr("class", "btn btn-blue");
    }
    var selectDateDom = $('#selectDate');
    var showDateDom = $('#showDate');
    var selectDelivery = $("#selectDelivery");
    var deliveryDate = $("#deliveryDate");
    // 初始化时间
    var now = new Date();
    var nowYear = now.getFullYear();
    var nowMonth = now.getMonth() + 1;
    var nowDate = now.getDate();
    showDateDom.attr('data-year', nowYear);
    showDateDom.attr('data-month', nowMonth);
    showDateDom.attr('data-date', nowDate);
    deliveryDate.attr('data-year', nowYear);
    deliveryDate.attr('data-month', nowMonth);
    deliveryDate.attr('data-date', nowDate);
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
    selectDelivery.bind('click', function () {
        var oneLevelId = deliveryDate.attr('data-year');
        var twoLevelId = deliveryDate.attr('data-month');
        var threeLevelId = deliveryDate.attr('data-date');
        var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
            title: '日期选择',
            itemHeight: 35,
            relation: [1, 1],
            oneLevelId: oneLevelId,
            twoLevelId: twoLevelId,
            threeLevelId: threeLevelId,
            showLoading: true,
            callback: function (selectOneObj, selectTwoObj, selectThreeObj) {
                deliveryDate.attr('data-year', selectOneObj.id);
                deliveryDate.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
                $("#baBuiltYear").val(selectOneObj.id + "-" + selectTwoObj.id + "-" + selectThreeObj.id);
            }
        });
    });
    function callbackMap(result){
        console.log("result:",result);
        $("#longitude").val(result.lng);
        $("#latitude").val(result.lat);
    }

</script>
</body>
</html>