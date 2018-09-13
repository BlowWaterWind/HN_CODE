<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_n" content="和家庭" />
    <meta name="WT.si_x" content="填写地址" />
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>网速测试</h1>
    </div>
</div>
<div class="container">
    <div class="speed-con">
        <div class="speed-title">
            <p>运营商：<span>湖南移动</span></p>
            <p>归属地：<span>湖南长沙</span></p>
        </div>
        <div class="speed-ball">
            <div class="ball-txt" style="display:none;">准备测速</div>
            <!--测试结果展示1 start-->
            <div class="ball-txt pt20" style="display:none;">
                <p class="f32">12<span class="f14">ms</span></p>
                <p class="f14 pt5 font-gray8">平均延时</p>
            </div>
            <!--测试结果展示1 end-->
            <!--测试结果展示2 start-->
            <div class="ball-txt pt20" style="display:none;">
                <p class="f32">20<span class="f14">MB/s</span></p>
                <p class="f14 pt5 font-gray8">平均下载速度</p>
            </div>
            <!--测试结果展示2 end-->
            <!--测试结果展示3 start-->
            <div class="ball-txt pt20" style="display:block;">
                <p class="f32">12<span class="f14">MB/s</span></p>
                <p class="f14 pt5 font-gray8">平均上传速度</p>
            </div>
            <!--测试结果展示3 end-->
        </div>
        <ul class="speed-info">
            <li class="speed-list speed-icon01">
                <p>0ms</p>
                <span>准备测速</span>
            </li>
            <li class="speed-list speed-icon02">
                <p>0MB/s</p>
                <span>下载速度</span>
            </li>
            <li class="speed-list speed-icon03">
                <p>0MB/s</p>
                <span>上传速度</span>
            </li>
        </ul>
        <div class="broad-btn">
            <a href="javascript:void(0)" class="btn btn-blue">准备测速</a>
        </div>
    </div>
</div>
</body>

</html>