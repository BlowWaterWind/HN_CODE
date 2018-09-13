<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <title>湖南移动网上营业厅</title>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
</head>
<body>
<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>帐号查询</h1>
    </div>
</div>
<div class="container bg-gray hy-tab">
    <form id="broadBandQueryForm" action="${ctx}/o2oSpeedUp/queryAccount" method="post">
        <div class="share-content rearch-con">
            <span class="cx-tit">请查询需要提速的宽带:</span>
            <div class="con-changae">
                <label><input type="radio" name="numType"  checked="checked" />手机号码查询</label>
            </div>
            <div><input id="serialNumber" type="text" name="serialNumber" class="form-control" /></div>
            <div class="kdcenter-renew-phone-search">
                <a id="queryBtn" class="btn btn-bd btn-icon btn-block" href="javascript:;">查询</a>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
    $(function () {

        /*查询*/
        $("#queryBtn").on("click",function () {
            if($.trim($("#serialNumber").val()).length == 0){
                return;
            }
            $("#broadBandQueryForm").submit();
        });


    });
</script>
</body>
</html>