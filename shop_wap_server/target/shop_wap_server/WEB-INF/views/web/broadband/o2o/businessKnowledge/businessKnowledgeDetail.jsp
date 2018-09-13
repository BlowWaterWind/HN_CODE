<%--
  Created by IntelliJ IDEA.
  User: cc
  1:营销芝士详情
  Date: 2017/9/12
  Time: 19:37
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
    <link href="${ctxStatic}/css/broadBand/pure-grids.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.min.js"></script>
</head>

<%@ include file="/WEB-INF/views/include/message.jsp" %>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>营销知识详情</h1>
    </div>
</div>
<!-- 业务介绍、资费说明、温馨提示开始 -->
<input type="hidden" id="businessId" value="${businessId}">
<div class="tabs broad-tabs mt10">
    <ul class="tab-menu">
        <li class="active" onclick="fetchProductInfo('1')"><a href="#tab-1">业务规则</a></li>
        <li><a href="#tab-2" onclick="fetchProductInfo('2')">注意事项</a></li>
        <li><a href="#tab-3" onclick="fetchProductInfo('3')">其他信息</a></li>
    </ul>
    <div id="tab-1">
        <div id="ruleInfo" class="info-detail">
            <c:forEach items="${o2oBusinessRules}" var="item" varStatus="status">
                <p>(${status.index+1})${item.ruleContent}</p>
            </c:forEach>
        </div>
        <!--未找到到相关内容 strat-->
        <div class="no-orders mt10" style="display: none;" id="noRuleInfo">
            <img src="${ctxStatic}/images/fa_icon.png"/>
            <p>未找到到相关内容</p>
        </div>
        <!--未找到到相关内容 end-->
    </div>
    <div id="tab-2">
        <div id="attentionInfo"></div>
        <!--未找到到相关内容 strat-->
        <div class="no-orders mt10" style="display: none;" id="noAttentionInfo">
            <img src="${ctxStatic}/images/fa_icon.png"/>
            <p>未找到到相关内容</p>
        </div>
        <!--未找到到相关内容 end-->
    </div>
    <div id="tab-3">
        <div id="otherInfo"></div>
        <!--未找到到相关内容 strat-->
        <div class="no-orders mt10" style="display: none;" id="noOtherInfo">
            <img src="${ctxStatic}/images/fa_icon.png"/>
            <p>未找到到相关内容</p>
        </div>
        <!--未找到到相关内容 end-->
    </div>
</div>
<!-- 业务介绍、资费说明、温馨提示结束 -->
</body>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.tabslet.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
<script type="text/html" id="divQA">
    <div class="market-answer">
        {{each data as info}}
        <div class="answer-list">
            <p>{{$index+1}} . {{info.questionContent}}</p>
            <p class="answer-txt">答：{{info.answerContent}}</p>
        </div>
        {{/each}}
    </div>
</script>
<script>
    $(function () {
        //tabs
        $('.tabs').tabslet();
        fetchProductInfo('1');
    });

    function fetchProductInfo(type) {
        var businessId = $("#businessId").val();
        $.ajax({
            url: "businessDetail",
            type: "post",
            data: {type:type,businessId:businessId},
            success: function (result) {
                if (result.code == '0') {
                    var data = result.data;
                    if (data != undefined) {
                        var resultHtml = "";
                        if (type == '1') {
                            for(var i=0;i<data.length;i++){
                                resultHtml += "<p>("+(i+1)+")"+data[i].ruleContent+"</p>";
                            }
                            $("#ruleInfo").html("");
                            $("#ruleInfo").html(resultHtml);
                            $("#noRuleInfo").hide();
                        }else if (type == '2') {
                            for(var i=0;i<data.length;i++){
                                resultHtml += "<p>("+(i+1)+")"+data[i].ruleContent+"</p>";
                            }
                            $("#attentionInfo").html("");
                            $("#attentionInfo").html(resultHtml);
                            $("#noAttentionInfo").hide();
                        } else {
                            $("#otherInfo").html("");
                            $("#otherInfo").html(template('divQA',result))
                            $("#noOtherInfo").hide();
                        }
                    } else {
                        //无数据
                        if (type == '1') {
                            $("#noRuleInfo").show();
                        }else if(type == '2'){
                            $("#noAttentionInfo").show();
                        }else{
                            $("#noOtherInfo").show();
                        }
                    }
                } else {
                    //返回出错
                    showAlert(result.message || "系统在忙哦，请稍后再试！");
                }
            }
        });
    }


</script>


</html>
