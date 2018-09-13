<%--
  Created by IntelliJ IDEA.
  User: cc
  查询小区信息
  Date: 2017/9/18
  Time: 20:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>

    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css"/>
    <%--<link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>小区信息收集</h1>
    </div>
</div>

<div class="container bg-gray hy-tab">
    <div class="wf-list tab-con">
        <div class="tab-tit font-red">请点选您所在的地市和区域，然后在查询框内输入您的地址</div>
        <!--地市选择 start-->
        <ul class="change-list">
            <li>
                <label>地&emsp;&emsp;区：</label>
                <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <select id="city" class="set-sd">
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
                    <input type="hidden" id="county"/>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
          <select class="set-sd city-area">
              <%--<option value="岳麓区">岳麓区</option>--%>
          </select>
          </span></div>
            </li>
        </ul>
        <!--地市选择 end-->
    </div>
</div>
<!--搜索地址点击搜索后的div start-->
<div id="div_search" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" id="a_retSearch"></a>
            <div class="header-form">
                <input type="text" id="txt" name="txt" class="form-control form-fr" placeholder="请输入小区、楼盘、写字楼"/>
                <a id="sIcon" class="s-icon"></a>
            </div>
            <a onclick="inputSearch()" class="relefresh-btn" id="searchBtn">搜索</a>
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
            <input type="text" class="form-control" placeholder="请输入小区名" id="inputSearch"/>
            <a href="javascript:void(0)" class="s-icon"></a>
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
                <a href="myInfo" class="btn btn-qblue">我录入过的信息</a>
            </div>
        </div>
    </div>

</div>
<!--尾部 end-->
</body>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/o2o/infoCollect/infoSearch.js"></script>
<script type="text/html" id="communityInfo">
    {{each ADDRESS_INFO as info}}
    <li>
        <a onclick="collectInfo(this)" class="collect-icon">
            <div class="collect-txt">
                <p>{{info.COMMUNITY_NAME}}</p>
                <span class="channel-gray9 f12">{{info.ADDRESS_PATH}}</span>
            </div>
            <form action="initAdd" method="post">
                <div class="collect-btn">
                    <div class="btn btn-border-blue btnClick" style="font-size:10px;">楼栋及友商录入</div>
                </div>
                <input type="hidden" name="communityId" value="{{info.COMMUNITY_CODE}}"/>
                <input type="hidden" name="communityCode" value="{{info.COMMUNITY_CODE}}"/>
                <input type="hidden" name="zoneName" value="{{info.COMMUNITY_NAME}}"/>
                <input type="hidden" name="addressPath" value="{{info.ADDRESS_PATH}}"/>
                <input type="hidden" name="searchString" value="{{info.COMMUNITY_NAME}}">
                <input type="hidden" name="searchType" value="4"/>
                <input type="hidden" name="eparchy"/>
                <input type="hidden" name="city"/>
                <input type="hidden" name="infoType" value="0"/>
            </form>
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
<%--fetch partnerInfo--%>
<script type="text/html" id="partnerInfo">
    {{each list as partnerInfo}}
    <div class="marke-info">
        <p>{{partnerInfo.partnersName}}<span class="pl10">{{partnerInfo.communityName}}</span></p>
        <p><label class="channel-gray9">宽带覆盖情况：</label>{{partnerInfo.coverInfo}}</p>
        <p><label class="channel-gray9">宽带客户数量：</label>{{partnerInfo.userNum}}</p>
        <p><label class="channel-gray9">宽带营销及回挖政策：</label>{{partnerInfo.stealBackNum}}</p>
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
                                $('.city-area').append("<option value='"+item.CITY_ID+"'>" + item.CITY_NAME + "</option>");
                            });
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
                );
            },

            /**
             * 查询最终地址
             * 通过参数个数进行重载
             */
            qryLastAddress: function () {
                if (arguments.length == 1) {
                    addressQuery.qryLastAddressByPath(arguments[0]);
                }
                if (arguments.length == 3) {
                    addressQuery.qryLastAddressByKeyWords(arguments[0], arguments[1], arguments[2]);
                }
            },

            /**
             * 通过楼栋查询最终地址
             * @param ADDRESS_PATH 楼栋名称
             */
            qryLastAddressByPath: function (ADDRESS_PATH) {
                $.getJSON('${ctx}/bandResourceQuery/qryAddressName', {
                        'addressKeyString': ADDRESS_PATH
                    }, function (e) {
                        if (e.respDesc == "OK!") //接口请求成功
                        {
                            var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                            if (ADDRESS_INFO.length == 0) {
//                                $("#lastAddress").append("该地址暂未开通宽带资源办理");
                                return;
                            }
                            $.each(ADDRESS_INFO, function (i, item) {
                                console.log(item);
//                                $("#lastAddress").append("<li last-address-building=" + item.HOUSE_CODE + " coverType=" + item.COVER_TYPE + ">" + item.ADDRESS_NAME + "</li>");
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
                if (!$.trim(addressQuery.keyWord)) //空字符串
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
                                    "<a href='initAdd?infoType=1' class='broad-btn broad-btn-blue' style='line-height: 1rem;'>盲点地址录入</a></div>";
                                $(".adress-list").html("").append("<span style='padding: 80px'>该地址暂未开通宽带资源办理</span>");
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
    $(function () {
        addressQuery.qryCityArea(addressQuery.cityCode); //默认初始化加载

        $("#city").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityCode = $(this).val();
            addressQuery.cityName = $(this).find("option:selected").text();
            addressQuery.qryCityArea(addressQuery.cityCode);
            addressQuery.cityArea = $('.city-area').find("option:selected").text();
        });

        $(".city-area").live('change', function () {  //区域级别选择事件监听
            $("#txt").val("");
            $(".adress-list").html("");
            addressQuery.cityArea = $(this).val();
            console.log(addressQuery.cityCode);
        });

        $("#txt").live('input propertychange', function () {  //关键字级别选择事件监听
            addressQuery.keyWord = $(this).val();
            addressQuery.cityArea = $(".city-area").find("option:selected").text();
            $(".adress-list").html(""); //清空无效信息
            addressQuery.qryLastAddress(addressQuery.cityName, addressQuery.cityArea, addressQuery.keyWord);//关键字查询
            console.log(addressQuery.keyWord);
        });

        $("#queryCommit").click(function () {  //地址信息选中事件监听
            var address = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").text();
            var houseCode = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("last-address-building");
            var coverType = $('input[name="radio_address"]:checked ').parent().nextAll("span[class='adr-text']").attr("coverType");

            showLoadPop("正在建设中...");
        });
    });

    /*查询小区信息*/
    function queryAddress(keyWords,cityName,cityArea){
        if(cityName ==undefined || cityArea == undefined){
            cityName = $("#city").find("option:selected").text();
            cityArea = $(".city-area").find("option:selected").text();
        }
        if (!$.trim(keyWords)) //空字符串
        {
            return;
        }
        if(keyWords==''){
            keyWords = $("#queryAddress").val();
        }
        $.getJSON('${ctx}/bandResourceQuery/qryAddressCommunityName', {
                'cityName': encodeURI(cityName,"UTF-8"),
                'cityArea': encodeURI(cityArea,"UTF-8"),
                'keyWords': encodeURI(keyWords,"UTF-8")
            }, function (e) {
                if (e.respDesc == "OK!") //接口请求成功
                {
                    var ADDRESS_INFO = e.result[0].ADDRESS_INFO;
                    if (ADDRESS_INFO.length == 0) {
                        var button = "<div id='userAdd' class='channel-btn' style='padding: 0px'>" +
                            "<a onclick='initAddUserInfo()' class='broad-btn broad-btn-blue' style='line-height: 1rem;'>盲点地址录入</a></div>";
                        $(".adress-list").html("").append("<span style='padding: 80px'>该地址暂未开通宽带资源办理</span>");
                        $(".adress-list").append(button);
                        return;
                    }
                    $(".adress-list").html(template("communityInfo",e.result[0]))
                } else {
                    alert("系统异常，请稍后再试");
                }
            }
        );
    }
</script>
</html >