<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>商机业务推荐</h1>
    </div>
</div>
<div class="container">
    <input name="phone" hidden="hidden" id="phone" value="${phone}">
    <div class="borad-selected">
        <div class="selected-list">
            <ul class="selected-sub collent-item">
                    <c:forEach items="${list}" var="broadbandItem" varStatus="status">

                            <li>
                            <a href="javascript:void(0)" class="sub-link">
                                <div class="selected-info">
                                    <img src="${ctxStatic}/images/kd/broad_info.jpg" />
                                    <h4>${broadbandItem.bussinessName}</h4>
                                    <p class="sub-txt">${broadbandItem.bussinessDesc}</p>
                                    <span class="channel-blue price">${broadbandItem.bussinessFee}元/月</span>
                                </div>
                            <%--<input type="button" class="broad-btn broad-btn-blue" value="立即办理" />--%>
                                <input type="button" class="broad-btn broad-btn-blue" data-href="${broadbandItem.bussinessHref}" onclick="redirctFun(this)" value="立即办理" />
                            <%--<a class="broad-btn broad-btn-blue" href="${broadbandItem.url}">立即办理</a>--%>
                            </li>

                     </c:forEach>

                </ul>
        </div>
    </div>
</div>

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
<script type="text/javascript" src="${ctxStatic}/js/o2o/source/addressIndex.js" charset="utf-8"></script>
<script>
    $(function() {
        //折叠面板
        $(".collent").click(function() {
            $(".collent-item").not($(this).next()).hide();
//            $(".collent").not($(this).next()).removeClass("item");
            $(this).next().slideToggle(500);
            $(this).toggleClass("item");
        });
    });
    function redirctFun(param){
        var href = $(param).attr("data-href");
        var phone = $("#phone").val();
        window.location.href = ctx+href+phone;
    }
</script>
</body>

</html>