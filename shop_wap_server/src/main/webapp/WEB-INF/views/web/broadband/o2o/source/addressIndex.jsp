<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <style>
        div.modal-txt li{
            border-bottom: 1px dashed #dedede;
            /* padding: 6px 10px; */
            color: #666;
        }
        div.modal-txt li:hover{
            color: #999;
            background-color: #d2e0e4;
        }
        div.modal-txt ul{
            text-align: center;
        }
    </style>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带地址查询</h1>
        <%--<a href="${ctx}infoCollect/search" class="relefresh-btn channel-gray3">小区信息</a>--%>
    </div>
</div>
<input type="hidden" name="county" value="${county}" id="county">
<input type="hidden" name="city" value="${city}" id="city">
<input type="hidden" name="cityCode" value="${cityCode}" id="cityCode">
<!--搜索地址 start-->
<div id="div_searchAddress" class="site-search panel" style="display:none">
    <div class="top container">
        <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" id="a_retSearch"></a>
            <div class="header-form">
                <input type="text" id="queryAddress" name="queryAddress" class="form-control" placeholder="关键字搜索参考：请输入小区或周边标志性建筑" autofocus="autofocus" />
                <a href="javascript:void(0)" class="s-icon"></a>
            </div>
            <a href="javascript:void(0)" class="relefresh-btn" id="searchBtn">搜索</a>
        </div>
    </div>
    <!--没资源 start-->
    <div class="mzy-con hide" id="div_message"><!--没资源时去掉hide-->
        <img src="${ctxStatic}/images/error.png" />
        <div class="mzy-text">
            <p>尊敬的用户，非常抱歉！</p>
            <p>您所在区域宽带资源还在建设中，</p>
            <p>有宽带资源后，我们会第一时间通过短信告知您！</p>
        </div>
    </div>
    <!--没资源 end-->
    <!--展示地址信息 start-->
    <ul class="container">
        <ul class="adress-list">
            </ul>
    </ul>
    <!--展示地址信息 end-->
    <%--<div class="fix-br container fix-top fix-fb">--%>
        <%--<div class="affix container foot-menu">--%>
            <%--<div class="container form-group tj-btn"> <a  href="##" id="queryCommit" class="btn btn-gray" disabled="disabled">确定</a> <a id="queryCancel" class="btn btn-gray site-close" href="##">取消</a> </div>--%>
            <%--<!--预约安装 start-->--%>
            <%--<!--预约安装 start-->--%>
        <%--</div>--%>
    <%--</div>--%>
</div>
<div class="container">
    <div class="borad-adress">
        <div class="adress-box">
            <div class="adress-silder" <c:if test="${countyList!=null}">id="citySelect"</c:if>>
                <span id="cityVar">${city}</span><span id="countyVar">${county}</span>
            </div>
            <div class="adress-form">
                <input type="text" class="form-control" id="address" placeholder="输入要查询的具体地址" />
                <a href="javascript:void(0)" class="s-icon"></a>
            </div>
        </div>
        <!--热门搜索 start-->
        <div class="hot-adress">
            <p class="adress-title">热门搜索</p>
            <div class="hot-list">
                <c:forEach items="${topList}" var="topList">
                    <a href="javascript:void(0)" onclick="quickSearch('${topList.searchString}');">${topList.searchString}</a>
                </c:forEach>
                <%--<a href="javascript:void(0)">杨帆小</a><a href="javascript:void(0)">王府花</a><a href="javascript:void(0)">船指太平洋</a><a href="javascript:void(0)">农科金苹果服饰家园</a><a href="javascript:void(0)">一起来飞车GO</a>--%>
            </div>
        </div>
        <!--热门搜索 end-->
        <!--搜索记录 start-->
        <div class="seach-records">
            <p class="adress-title">搜索记录</p>
            <ul class="recorads-list">
                <c:forEach items="${searchList}" var="searchList">
                    <li><a href="javascript:void(0)" onclick="quickSearch('${searchList.searchString}');">${searchList.searchString}</a></li>
                </c:forEach>
                <%--<li><a href="javascript:void(0)">王府花园</a></li>--%>
                <%--<li><a href="javascript:void(0)">扬帆小区E28栋</a></li>--%>
                <%--<li><a href="javascript:void(0)">景泰家园</a></li>--%>
            </ul>
        </div>
        <!--搜索记录 end-->
    </div>
    <!--清空 start-->
    <div class="empty-delete">
        <a href="javascript:void(0)" class="delete-icon" id="clearRecord">清空搜索记录</a>
    </div>
    <div class="modal modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog channel-modal">
            <div class="modal-info">
                <h4>区域选择</h4>
                <div class="modal-txt">
                    <ul>
                        <c:forEach items="${countyList}" var="county" varStatus="status" >
                            <%--<option value="${county.orgId}" otitle="区县选择" otype="button">${county.orgName}</option>--%>
                            <li onclick="selectCity('${county.CITY_NAME}')">${county.CITY_NAME}</li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${ctxStatic}/js/o2o/source/addressIndex.js"></script>
<script>
    $(function(){
       $("#citySelect").click(function(){
           $("#myModal2").show();
           $("#myModal2").modal();
       })
    });
    function selectCity(cityName){
        $("#countyVar").text(cityName);
        $("#county").val(cityName);
        $("#myModal2").hide();
    }
</script>
</html>