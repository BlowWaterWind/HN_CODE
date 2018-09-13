<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/share.js"></script>

    <!-- 宽带多级地址搜索框 -->
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
        var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>输入号码办理</h1>
    </div>
</div>
<!--套餐选择 start-->
<div class="container">
    <form action="${ctx}/${broadbandPoster.url}" id="form1" name="form1" method="post">
        <input type="hidden" name="url" value="${broadbandPoster.url}"/>
        <input type="hidden" name="shopId" id="shopId" value="${shopId}"/>
        <input type="hidden" name="staffId" id="staffId" value="${staffId}"/>
        <input type="hidden" name="secToken" id="secToken" value="${secToken}"/>

        <!--套餐信息 start-->
    <div class="wf-list change-info sub-new">
        <div class="wf-ait clearfix">
            <dl>
                <dt>
                    <c:choose>
                        <c:when test="${not empty broadbandPoster.imgUrl}">
                            <img src="${tfsUrl}${broadbandPoster.imgUrl}"/>
                            <input type="hidden" name="imgUrl" value="${broadbandPoster.imgUrl}"/>
                        </c:when>
                    </c:choose>
                </dt>
                <dd>
                    <h4>${broadbandPoster.posterTitle}</h4>
                    <input type="hidden" name="posterTitle" value="${broadbandPoster.posterTitle}"/>
                    <p class="font-gray6">${broadbandPoster.intro}</p>
                    <input type="hidden" name="intro" value="${broadbandPoster.intro}"/>
                </dd>
            </dl>
        </div>
    </div>
    <!--套餐信息 end-->
    <!--输入手机号码 start-->
    <div class="enter-number">
        <div class="enter-list">
            <p class="pd10">方式1：
                <input type="text" id="installPhoneNum" name="installPhoneNum" value="${installPhoneNum}" style="width:80%"  class="form-control form-con sjh-jd" placeholder="请输入手机号码" />
            </p>
            <c:if test="${resultMap.respCode eq '-1'}">
                <div class="font-red" style="padding-left:62px;">${resultMap.respDesc}</div>
            </c:if>
             <%--<p class="pd10">方式2：对多个号码进行业务办理请点击<a id="batchBtn" href="javascript:void(0);" class="font-blue" style="padding-right:10px">批量办理</a></p>--%>
            <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="javascript:void(0);">
                <img src="${ctxStatic}/images/fx-icon.png" style="width: 30px;"></a>
            </p>

            <%--<div class="enter-box">--%>
                <%--<span class="enter-title">手机号码：</span>--%>
                <%--&lt;%&ndash;<input type="text" id="installPhoneNum" name="installPhoneNum" value="${installPhoneNum}" class="form-control" placeholder="请输入手机号码" />&ndash;%&gt;--%>
            <%--</div>--%>
            <%--<c:if test="${resultMap.respCode eq '-1'}">--%>
                <%--<p class="pt10 font-red enter-tip">*${resultMap.respDesc}</p>--%>
            <%--</c:if>--%>
        </div>
    </div>
    <!--输入手机号码 end-->
    </form>
</div>
<!--尾部 start-->
<div class="fix-br">
    <div class="affix foot-box install-foot">
        <div class="container">
            <a href="javascript:void(0)" class="btn btn-block btn-red" id="operBtn">下一步</a>
            <!--当按钮为灰色时class btn-blue 替换成 btn-gray-->
        </div>
    </div>
</div>
<!--尾部 end-->

<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
    $(function(){
        var isMobile = /^1((3[4-9])||(5[0-2])||(5[7-9])||(8[2-4])||(8[7-8])||(47))\d{8}$/g;
        $("#operBtn").click(function(){
            if($("#installPhoneNum").val() == ""){
                showAlert("请先输入办理用户手机号码！");
                return;
            }
            if(!isMobile.test($("#installPhoneNum").val())){
                showAlert("手机号码格式错误,手机号码为11位数字");
                return ;
            }
            $("#form1").submit();
        });
    });
</script>
</body>
</html>