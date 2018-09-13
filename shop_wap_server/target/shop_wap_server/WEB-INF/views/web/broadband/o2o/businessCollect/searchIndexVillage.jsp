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
        <h1>农村预约营销基本商情录入</h1>
    </div>
</div>

<div class="container bg-gray hy-tab">
    <div class="wf-list tab-con">
        <div class="tab-tit font-red">请先点选地市和区域，然后在查询框内输入行政村名</div>
        <!--地市选择 start-->
        <form name="form1" action="initAddvillageReverse" method="post" id="form1">
            <input type="hidden" name="type" id="type" value="7">
            <input type="hidden" name="searchString" id="searchString">
            <input type="hidden" name="searchType" value="7"/>
            <input type="hidden" name="eparchyName" id="eparchyName"/>
            <input type="hidden" name="countyName" id="countyName" />
            <ul class="change-list">
                <li>
                    <label>地&emsp;&emsp;区：</label>
                    <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
          <select id="city" class="set-sd" name="eparchyCode">
              <c:forEach items="${orgList}" var="org" varStatus="status">
                      <option value="${org.orgId}">${org.orgName}</option>
              </c:forEach>
          </select>
          </span> <span class="td-fr"> <i class="select-arrow"></i>
          <select class="set-sd city-area" name="countyCode" id="countyCode">
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
                <input type="text" id="txt" name="txt" class="form-control form-fr" placeholder="请输入行政村名"/>
                <a id="sIcon" class="s-icon" onclick="queryBu()"></a>
            </div>
            <a onclick="queryBu()" class="relefresh-btn" id="searchBtn">搜索</a>
        </div>
    </div>
    <%--查询结果--%>
    <div class="container">
        <div class="collect-con">
            <ul class="adress-list-box" id="villageUl"></ul>
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
            <input type="text" class="form-control" placeholder="请输入行政村名" id="inputSearch" onclick="inputSearch()"/>
            <a href="javascript:void(0)" class="s-icon" onclick="inputSearch()"></a>
        </div>
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
                <a href="myVillageReserveList" class="btn btn-qblue">我录入过的信息</a>
            </div>
        </div>
    </div>

</div>
<!--尾部 end-->
</body>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/html" id="villageList">
    {{each data as village}}
    <li>
        <a href="javascript:void(0)" class="collect-icon">
            <div class="collect-txt">
                <p>{{village.changedIllageName}}</p>
                    <span class="channel-gray9 f12">
                        {{village.townName}}
                   </span>
            </div>
            <form action="initAddvillageReverse" method="post">
                <input type="hidden" name="recordId" value="{{village.recordId}}"/>
                <input type="hidden" name="query" value="query"/>
                <div class="collect-btn">
                    <input onclick="modify(this)" type="button" class="btn btn-border-blue"
                           value="修改"/>
                    <input onclick="view(this)" type="button" class="btn btn-border-blue"
                           value="查看"/>
                </div>
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
<script type="text/javascript">

    var addressQuery =
    {
        keyWord: "", //关键字
        parentId:'',
        countyCode:'',
        eparchyCode:'',
        /**
         * 查询城市区域
         * @param cityCode 市区域编码
         */
        qryCityArea: function (parentId) {
            $.getJSON('${ctx}/o2oBusiCollect/qryOrgByParentId', {
                        'parentId': parentId
                    }, function (e) {
                        if (e.respCode == "0")
                        {
                            $('.city-area').html("");
                            $.each(e.data, function (i, item) {
                                $('.city-area').append("<option value="+item.orgId+">" + item.orgName+ "</option>");
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
        qryVillageByKeyWords: function (eparchyName, countyName, keyWords) {
            $.getJSON('${ctx}/o2oBusiCollect/qryVillageList', {
                        'eparchyName': encodeURI(eparchyName, "UTF-8"),
                        'countyName': encodeURI(countyName, "UTF-8"),
                        'keyWords': encodeURI(keyWords, "UTF-8")
                    }, function (e) {
                        if (e.respCode == "0") //
                        {
                            if (e.data.length != 0) {
                                $("#villageUl").html(template("villageList", e));
                            }
                            var button = "<div id='userAdd' class='channel-btn'>" +
                                    "<a href='javascript:void(0);' class='broad-btn broad-btn-blue' onclick='collectInfo()' style='line-height: 1rem;'>农村预约营销基本商情录入</a></div>";
                            $(".adress-list-box").append(button);
                        } else {
                            alert("系统异常，请稍后再试");
                        }
                    }
            );
        },
    };
    $(function () {
        addressQuery.qryCityArea($('#city').find("option:selected").val()); //默认初始化加载
        $("#city").live('change', function () {  //市级别选择事件监听
            $("#txt").val("");
            $(".adress-list-box").html("");
            addressQuery.qryCityArea($(this).find("option:selected").val());
        });

        $(".city-area").live('change', function () {  //区域级别选择事件监听
            $("#txt").val("");
            $(".adress-list-box").html("");
        });

        $("#txt").live('input propertychange', function () {  //关键字级别选择事件监听
            $(".adress-list-box").html(""); //清空无效信息
            addressQuery.qryVillageByKeyWords($('#city').find("option:selected").text(),$('#countyCode').find("option:selected").text(),$(this).val());//关键字查询
        });
    });


    function collectInfo(){
        var keyWord=$("#txt").val();
        if($.trim(keyWord)){
            $("#searchString").val($('#city').find("option:selected").text()+";"+$('#countyCode').find("option:selected").text()+";"+keyWord);
        }
        $("#form1").submit();
    }

    /*返回时更新最高记录和历史记录*/
    function updateRecords(type) {
        $.ajax({
            url: "updateRecord",
            data:{type:type},
            type:"get",
            dataType:"json",
            success:function(data){
                if(data.resultCode == "0"){
                    $(".hot-list").html(template("hotList",data));
                    $("#record").html(template("historyList",data));
                }
            }
        });
    }

    function inputSearch(){
        $(".panel").toggle();
        $("#txt").focus();
        $(".adress-list-box").html(""); //清空无效信息
        addressQuery.qryVillageByKeyWords( $('#city').find("option:selected").text(),$('#countyCode').find("option:selected").text(),null);//关键字查询
    }

    /*隐藏地址搜索框*/
    $("#a_retSearch").click(function(){
        $("#txt").val("");
        $(".adress-list-box").html("");
        updateRecords($("#type").val());
        $(".panel").toggle();
    });


    function quickSearch(keyWords){
        var keys = keyWords.split(";");
        $(".panel").toggle();
        $("#txt").focus();
        $("#txt").val(keys[2]);
        $(".adress-list-box").html(""); //清空无效信息
        addressQuery.qryVillageByKeyWords(keys[0],keys[1],keys[2]);//关键字查询
    }

    function queryBu(){
        var keyWords = $("#txt").val();
        $(".adress-list-box").html("");
        addressQuery.qryVillageByKeyWords($('#city').find("option:selected").text(),$('#countyCode').find("option:selected").text(), keyWords);//关键字查询
    }

    /**
     * 清空搜索记录,清空商机推荐输入的搜索记录
     */
    $("#clearRecord").click(function(){
        var type=$("#type").val();
        $.ajax({
            url:"deleteRecord",
            data:{type:type},
            success:function(data){
                $(".seach-records").html("");
            }
        })
    });

    function modify(thisDiv) {
        $(thisDiv).parent().parent().find("input[name='query']").remove();
        $(thisDiv).parent().parent().submit();
    }

    function view(thisDiv) {
        $(thisDiv).parent().parent().submit();
    }
</script>
</html >