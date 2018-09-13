<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
    <style>
    	.tib-sm{ padding:6px 0 0 30px; }
    </style>
</head>

<body>
	<div class="top container">
        <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
            <h1>确定套餐</h1>
        </div>
    </div>
    <div class="container">
        <div class="form-broad">
            <!--搜索 start-->
            <div class="form-react">
                <p>
                    <input type="text" class="form-control" placeholder="输入要办理业务的手机号码进行查询" />
                    <a href="javascript:void(0)" class="search-icon "></a>
                </p>
                <a href="javascript:void(0)" class="react-btn font-blue">查询</a>
            </div>
            <!--搜索 end-->
            <!--固话套餐 start-->
            <div class="fixed-package broadband-con">
                <!--宽带套餐信息 start-->
                <div class="broadband-list current-info">
                   <p>固话号码：<span>073112344321</span></p>
                   <p>当前套餐：<span>家庭固话9元档</span></p>
                </div>
                <!--宽带套餐信息 end-->
                <!--固话套餐选择 start-->
                <div class="broadband-list broadband-gray">
                    <div class="broadband-title"><span>固话套餐选择</span></div>
                    <div class="broadband-info tabs">
                        <ul class="broadband-menu clearfix">
                            <li class="active"><a href="#tab-1">9元/月</a></li>
                            <li><a href="#tab-2">15元/月</a></li>
                            <li><a href="#tab-3">30元/月</a></li>
                        </ul>
                        <div id="tab-1">
                            <div class="broadband-content">
                                <div class="broadband-txt">
                                    <p class="t-c">1、包含网内长市通话分钟数2000分钟</p>
                                    <p>优惠价：1元/月<span class="font-red f12 pl30">首次办理，1元/月，优惠期12个月</span></p>
                                </div>
                            </div>
                        </div>
                        <div id="tab-2">
                            <div class="broadband-content">2</div>
                        </div>
                        <div id="tab-3">
                            <div class="broadband-content">3</div>
                        </div>
                    </div>
                </div>
                <!--固话套餐选择 end-->
            </div>
            <!--固话套餐 end-->
        </div>
        <!--立即办理按钮 start-->
        <div class="broad-btn">
            <a href="javascript:void(0)" class="btn btn-blue" data-toggle="modal" data-target="#myModal">确认办理</a>
        </div>
        <!--立即办理按钮 end-->
    </div>
    <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="js/modal.js"></script>
    <script type="text/javascript" src="js/jquery.tabslet.min.js"></script>
    <script>
    $(function() {
        //tabs
        $('.tabs').tabslet();
        //切换
        $(".change li").on("click", function() {
            $(this).siblings().removeClass("active");
            $(this).addClass("active");
        });
    });
    </script>

    <!--提示 弹框 start-->
    <div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog broadband-dialog">
            <div class="modal-con">
                <div class="modal-text">
                    <p>业务名称：<span>固话业务9元档</span></p>
                    <p>开通状态：<span>未开通</span></p>
                    <p>资费：<span>1元/月(优惠12个月)</span></p>
                    <p>生效方式：<span>当月（账期），下月（账期）生效</span></p>
                </div>
                <div class="dialog-btn">
                    <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                    <a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认办理</a>
                </div>
            </div>
        </div>
    </div>
    <!--提示 弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>