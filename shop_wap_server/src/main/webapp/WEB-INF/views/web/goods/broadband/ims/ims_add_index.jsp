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
                <div class="broadband-list">
                    <div class="broadband-title"><span>宽带套餐信息</span></div>
                    <div class="broadband-info">
                        <p class="border-title">宽带账号：<span>389999877</span></p>
                        <ul class="broadband-box">
                            <li>
                                <label class="font-gray">套餐名称：</label><span>48元消费叠加型</span></li>
                            <li>
                                <label class="font-gray">使用期限：</label><span>2017-01-01 12:00:00~2050-12-31 23:59:59</span></li>
                            <li>
                                <label class="font-gray">安装地址：</label><span>********</span></li>
                            <li>
                                <label class="font-gray">装 机 人：</label><span>张三</span></li>
                            <li>
                                <label class="font-gray">联系电话：</label><span>13812344321</span></li>
                        </ul>
                    </div>
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
            <a href="javascript:void(0)" class="btn btn-blue" data-toggle="modal" data-target="#myModal">立即办理</a>
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
                <div class="modal-text t-c">
                    <p>尊敬的用户您好</p>
                    <p>您将为13812344321办理<br>98元和家庭套餐<br>+9元档固话套餐</p>
                </div>
                <div class="dialog-btn">
                    <a href="javascript:void(0);" class="font-gray" data-dismiss="modal">取消</a>
                    <a href="javascript:void(0);" class="font-blue" data-dismiss="modal">确认</a>
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