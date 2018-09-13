<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
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

    <!-- 搜索地址框placeholder处理 -->
    <style>
        /* WebKit browsers */
        input:focus::-webkit-input-placeholder { color:transparent; }
        /* Mozilla Firefox 4 to 18 */
        input:focus:-moz-placeholder { color:transparent; }
        /* Mozilla Firefox 19+ */
        input:focus::-moz-placeholder { color:transparent; }
        /* Internet Explorer 10+ */
        input:focus:-ms-input-placeholder { color:transparent; }
    </style>

    <script type="text/javascript">
        var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>输入办理号码</h1>
    </div>
</div>
<div class="container">
    <form action="heInstall" id="form1" name="form1" method="post">
        <input type="hidden" name="shopId" id="shopId" value="${shopId}"/>
        <input type="hidden" name="staffId" id="staffId" value="${staffId}"/>
        <!--套餐信息 start-->
        <div class="wf-list change-info sub-new">
            <div class="wf-ait clearfix">
                <dl>
                    <dt><img src="${ctxStatic}/images/kd/broad_info.jpg"/></dt>
                    <dd>
                        <br>
                        <p>和家庭套餐办理</p>
                    </dd>
                </dl>
            </div>
        </div>
        <!--套餐信息 end-->

        <!--填写信息 start-->
        <div class="wf-list sub-info sub-new">
            <div class="wf-ait clearfix">
                    <p class="pd10">方式1：
                        <input type="text" id="installPhoneNum" name="installPhoneNum" value="${installPhoneNum}" style="width:80%" class="form-control form-con sjh-jd" placeholder="请输入手机号码"/>
                    </p>
                <%--<p class="pd10">方式2：对多个号码进行业务办理请点击<a id="batchBtn" href="javascript:void(0);" class="font-blue" style="padding-right:10px">批量办理</a></p>--%>
                <p class="pd10">方式2：分享给用户自行办理<a id="shareBtn" href="javascript:void(0);">
                    <img src="${ctxStatic}/images/fx-icon.png" style="width: 30px;"></a>
                </p>
                <%--<ul class="wf-con clearfix">--%>
                    <%--<li>--%>
                        <%--<span class="font-gray" style="line-height:35px;">手&nbsp;&nbsp;&nbsp;&nbsp;机：</span>--%>
                        <%--<div class="sub-text"><input type="text" id="installPhoneNum" name="installPhoneNum" value="${installPhoneNum}" class="form-control" placeholder="请输入手机号码" /></div>--%>
                    <%--</li>--%>
                <%--</ul>--%>
            </div>
        </div>
    </form>
</div>

<div class="fix-br">
    <div class="affix foot-box new-foot">
        <div class="container active-fix">
            <div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#myModal"></a></div>
            <div class="fl-right"><a id="operBtn">下一步</a></div>
        </div>
    </div>
</div>

<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
    $(function(){
        var isMobile = /^1((3[4-9])||(5[0-2])||(5[7-9])||(8[2-4])||(8[7-8])||(47))\d{8}$/g;
        $("#operBtn").click(function(){
            if($("#installPhoneNum").val() == ""){
                showAlert("请办理用户手机号码！");
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

<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
</body>
</html>