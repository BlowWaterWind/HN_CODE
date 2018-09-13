<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>小区竞争情况录入</h1>
    </div>
</div>

<div class="container bg-gray hy-tab">
    <div class="wf-list tab-con">
        <div class="tab-tit font-red">请先点选地市和区域，然后在查询框内输入地址关键字</div>
        <!--地市选择 start-->
        <form action="initAddComuCompetition" name="form1" id="form1" method="post">
            <input type="hidden" name="type" id="type" value="${type}">
            <input type="hidden" name="communityId" id="communityId" />
            <input type="hidden" name="communityName" id="communityName"/>
            <input type="hidden" name="addressPath" id="addressPath" />
            <input type="hidden" name="searchString" id="searchString">
            <input type="hidden" name="searchType" value="${type}"/>
            <%--<input type="hidden" name="eparchyCode" />--%>
            <input type="hidden" name="eparchyName" id="eparchyName"/>
            <input type="hidden" name="countyCode" id="countyCode" />
            <%--<input type="hidden" name="countyName" />--%>
        <ul class="change-list">
            <li>
                <label>地&emsp;&emsp;区：</label>
                <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <select id="city" name="eparchyCode" class="set-sd">
              <%--<option value="长沙市">长沙市</option>--%>
              <option value="0731">长沙</option>
              <option value="0733">株洲</option>
              <option value="0732">湘潭</option>
              <option value="0734">衡阳</option>
              <option value="0739">邵阳</option>
              <option value="0730">岳阳</option>
              <option value="0736">常德</option>
              <option value="0744">张家界</option>
              <option value="0737">益阳</option>
              <option value="0735">郴州</option>
              <option value="0746">永州</option>
              <option value="0745">怀化</option>
              <option value="0738">娄底</option>
              <option value="0743">湘西州</option>
          </select>
                    <%--<input type="hidden" id="county"/>--%>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
          <select class="set-sd city-area" name="countyName">
              <%--<option value="岳麓区">岳麓区</option>--%>
          </select>
          </span></div>
            </li>
        </ul>
        </form>
        <!--地市选择 end-->
    </div>
</div>
<!--搜索地址点击搜索后的div start-->
<div id="div_search" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" id="a_retSearch"></a>
            <div class="header-form">
                <input type="text" id="txt" name="txt" class="form-control form-fr" placeholder="请输入小区、楼盘、写字楼"/>
                <a id="sIcon" class="s-icon" onclick="queryBu()"></a>
            </div>
            <a onclick="queryBu()" class="relefresh-btn" id="searchBtn">搜索</a>
        </div>
    </div>
    <%--查询结果--%>
    <div class="container">
        <div class="collect-con">
            <ul class="adress-list-box adress-list" id="knowledgeInfo"></ul>
        </div>
    </div>
    <%--查询结果结束--%>

    <!--未找到到相关内容 strat-->
    <div class="no-orders mt10" style="display: none;" id="noInfo">
        <img src="${ctxStatic}/images/fa_icon.png"/>
        <p>未找到到相关内容</p>
    </div>
    <!--未找到到相关内容 end-->
</div>
<%--搜索查询结束--%>

<div class="container">
    <div class="collect-con">
        <div class="new-adress">
            <input type="text" class="form-control" placeholder="请输入小区名" id="inputSearch" onclick="inputSearch()"/>
            <a href="javascript:void(0)" class="s-icon" onclick="inputSearch()"></a>
        </div>
        <ul class="adress-list-box" id="ulList">
        </ul>
        <!--热门搜索 start-->
        <div class="hot-adress">
            <p class="adress-title">热门搜索</p>
            <div class="hot-list">
                <c:forEach items="${topList}" var="topList">
                    <a href="javascript:void(0)"
                       onclick="quickSearch('${topList.searchString}');">${topList.searchStringBak}</a>
                </c:forEach>
            </div>
        </div>
        <!--热门搜索 end-->
        <!--搜索记录 start-->
        <div id="searchRecord">
            <div class="seach-records">
                <p class="adress-title">搜索记录</p>
                <ul class="recorads-list" id="record">
                    <c:forEach items="${historyList}" var="historyList">
                        <li><a href="javascript:void(0)"
                               onclick="quickSearch('${historyList.searchString}');">${historyList.searchStringBak}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <!--搜索记录 end-->
            <!--清空 start-->
            <div class="empty-delete">
                <a href="javascript:void(0)" class="delete-icon" id="clearRecord">清空搜索记录</a>
            </div>
            <!--未找到到相关内容 strat-->
            <div class="no-orders" style="display:none;">
                <img src="images/fa_icon.png"/>
                <p>未找到到相关内容</p>
            </div>
            <!--未找到到相关内容 end-->
        </div>
    </div>
    <!--尾部 start-->
    <div class="container channel-aifix">
        <div class="channel-footer">
            <div class="channel-link">
                <a href="myCompetitionList" class="btn btn-qblue">我录入过的信息</a>
            </div>
        </div>
    </div>

</div>
<!--尾部 end-->
</body>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<%--<script type="text/javascript" src="${ctxStatic}/js/o2o/infoCollect/infoSearch.js"></script>--%>
<script type="text/html" id="communityInfo">
    {{each ADDRESS_INFO as info}}
    <li>
        <a onclick="collectInfo('{{info.COMMUNITY_CODE}}','{{info.COMMUNITY_NAME}}','{{info.ADDRESS_PATH}}')" class="collect-icon">
            <div class="collect-txt">
                <p>{{info.COMMUNITY_NAME}}</p>
                <span class="channel-gray9 f12">{{info.ADDRESS_PATH}}</span>
            </div>
                        <div class="collect-btn">
                            <div class="btn btn-border-blue btnClick" style="font-size:10px;">信息录入</div>
                        </div>
        </a>
    </li>
    {{/each}}
</script>
<script type="text/html" id="hotList">
    {{each topList as info}}
    <a href="javascript:void(0)"
       onclick="quickSearch('{{ info.searchString }}');">{{ info.searchStringBak }}</a>
    {{/each}}
</script>
<script type="text/html" id="historyList">
    {{each historyList as info}}
    <li>
        <a href="javascript:void(0)"
           onclick="quickSearch('{{ info.searchString }}');">{{ info.searchStringBak }}</a></li>
    {{/each}}
</script>
<script type="text/javascript">
    var addressQuery =
    {
        cityName: "长沙", //市
        cityCode: "0731", //市区域编码
        cityArea: "天心", //区
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
            $.getJSON('${ctx}/bandResourceQuery/qryAddressCityName', {
                        'cityCode': cityCode || '0731'
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            $('.city-area').html("");
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            $.each(ADDRESS_INFO, function (i, item) {
                                $('.city-area').append("<option>" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },


        /**
         * 通过关键字查询最终地址
         *
         * @param cityName 市
         * @param cityArea 区
         * @param keyWords 查询关键字
         */
        qryLastAddressByKeyWords: function (cityName, cityArea, keyWords) {
            if (!$.trim(keyWords)) //空字符串
            {
                return;
            }
            $.getJSON('${ctx}/bandResourceQuery/qryAddressCommunityName', {
                        'cityName': encodeURI(cityName, 'UTF-8'),
                        'cityArea': encodeURI(cityArea, 'UTF-8'),
                        'keyWords': encodeURI(keyWords, "UTF-8")
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            if (ADDRESS_INFO.length == 0) {
                                var button = "<div id='userAdd' class='channel-btn'>" +
                                        "<a href='javascript:void(0);' onclick='collectInfo(null,null,null)' class='broad-btn broad-btn-blue' style='line-height: 1rem;'>小区竞争情况录入</a></div>";
                                $(".adress-list").html("").append("<span style='padding: 80px'>该地址暂未开通宽带资源</span>");
                                $(".adress-list").append(button);
                                return;
                            }
                            $(".adress-list").html(template("communityInfo", e.result[0]));
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },
    };

</script>
<script type="text/javascript" src="${ctxStatic}/js/o2o/businessCollect/addressQuery.js"></script>
</html >