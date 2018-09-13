<%--
  Created by IntelliJ IDEA.
  User: cc
  2:潜在目标客户
  Date: 2017/9/12
  Time: 19:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>潜在目标客户</h1>
    </div>
</div>
<input type="hidden" id="communityId" value="${o2oCommunityInfoBak.communityId}">
<div class="container">
    <!--小区地址 start-->
    <div class="order-detail">
        <div class="adress-info">
            <div class="adress-txt">
                <p class="adress-number">${o2oCommunityInfoBak.addressPath}</p>
                <%--<p class="channel-gray9 f12">{buildingName}</p>--%>
            </div>
        </div>
    </div>
    <!--小区地址 start-->
    <div class="tabs broad-tabs opport-con mt10">
        <ul class="tab-menu">
            <li><a href="#tab-1" onclick="customerFilter(1)">新增客户</a></li>
            <li><a href="#tab-2" onclick="customerFilter(2)">友商客户</a></li>
            <li><a href="#tab-3" onclick="customerFilter(3)">提升客户</a></li>
            <li><a href="#tab-4" onclick="customerFilter(4)">预警客户</a></li>
        </ul>
        <div id="tab-1">
        </div>
        <div id="tab-2">
        </div>
        <div id="tab-3">
        </div>
        <div id="tab-4">
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>

<script>
    $(function () {
        //tabs
        $('.tabs').tabslet();
        customerFilter('1');

    });
</script>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/javascript">
    //新增客户，友商客户，提升客户，预警客户的过滤
    function customerFilter(customerType){
        var communityId = $("#communityId").val();
        $.ajax({
            url:"filterCustomer",
            data:{type:customerType,communityId:communityId},
            type:"post",
            dataType:"json",
            success:function(result){
                var noDataHtml = "<div class='no-orders mt10' style='display: block;'><img src='${ctxStatic}/images/fa_icon.png'/><p>未找到到相关内容</p></div>"
                $("#tab-"+customerType).html("");
                if(result.data!=undefined){
                    $("#tab-"+customerType).append(template("customerInfo",result));
                }else{

                    $("#tab-"+customerType).append(noDataHtml);
                }
            }
        });

    }
</script>
<script type="text/html" id="customerInfo">
    {{each data as info}}
    <div class="opport-list">
        <!--个人信息 start-->
        <div class="personal-con">
            <div class="personal-silder">
                <p class="pb5">{{info.userName}}</p>
                <span>{{info.serialNumber}}</span>
            </div>
            <div class="personal-right">
                <a href="sms:{{ info.serialNumber }}"  class="msg-icon pb5">发送短信</a>
                <a href="tel:{{ info.serialNumber }}" class="phone-icon">联系客户</a>
            </div>
        </div>
        <!--个人信息 end-->
        <!--信息内容展示 start-->
        <div class="opport-info">
            <p>消费情况：<span>
                {{ if info.recentCost != null}}
                {{info.recentCost}}
                {{else if info.recentCost == null}}
                暂无数据
                {{else}}
                {{ /if }}
            </span></p>
            <p>宽带套餐：<span>
                {{ if info.broadbandPackage != null}}
                {{info.broadbandPackage}}
                {{else if info.broadbandPackage == null}}
                暂无数据
                {{else}}
                {{ /if }}
            </span></p>
            <p>家庭增值业务：<span>
                {{ if info.familyAddedService != null}}
                {{info.familyAddedService}}
                {{else if info.familyAddedService == null}}
                暂无数据
                {{else}}
                {{ /if }}
            </span></p>
        </div>
        <!--信息内容展示 end-->
    </div>
    {{ /each }}
</script>
</body>

</html>