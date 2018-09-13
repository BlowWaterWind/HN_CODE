<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>老用户专区</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-5.0.0.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/swiper/swiper.min.css" />
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/css/wap.css" />
	
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
	<script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
	<div class="container">
        <img src="${ctxStatic}/ap/images/top.jpg" />
        <div class="broadband-tit"><span class="broad-icon"></span>精选套餐</div>
        <!--tabs start-->
        <div class="tabs">
            <ul class="tab-menu clearfix">
                <li class="active"><a href="#tab-1">0元续费</a></li>
                <li><a href="#tab-2">包年续费</a></li>
            </ul>
            <div id='tab-1'>
                <div class="broad-con clearfix">
                    <dl>
                        <dt><img src="${ctxStatic}/ap/images/pro_tu01.png" /></dt>
                        <dd>
                            <h4>158元消费保底套餐</h4>
                            <p>100M光纤宽带免费用，4K高 清互联网电视10元/月，线上办理还送光猫、机顶盒。</p>
                            <a href="/shop/broadband/linktoBookInstall" class="btn btn-blue" >立即办理</a>
                            <!-- data-toggle="modal" data-target="#myModal" -->
                        </dd>
                    </dl>
                    <dl>
                        <dt><img src="${ctxStatic}/ap/images/pro_tu02.png" /></dt>
                        <dd>
                            <h4>168元和家庭套餐</h4>
                            <p>100M光纤宽带、4K高清互联网电视免费 用，套餐内包含2G流量、2000分钟免费 通话时长，线上办理还送光猫、机顶盒。</p>
                            <a href="/shop/BroadbandTrade/heBroadbandInstall" class="btn btn-blue">立即办理</a>
                        </dd>
                    </dl>
                </div>
            </div>
            <div id='tab-2'>
                <div class="broad-con renewals-list clearfix">
                    <dl>
                        <dt>
                            <img src="${ctxStatic}/ap/images/ktu.png" />
                            <p>宽带续费</p>
                        </dt>
                        <dd>
                            <h4><span class="renew-lblue">100M</span>宽带续费享<span class="renew-dblue">5折</span></h4>
                            <%--<a href="/shop/broadband/linkToRenew" class="btn btn-blue">立即办理</a>--%>
                            <a href="javascript:void(0)" class="btn disabled">敬请期待</a>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
        <!--tabs end-->
        <div class="broadband-tit ywbl-list"><span class="broad-icon"></span>办理流程</div>
        <ul class="bus-list clearfix">
            <li class="col col-12-3">
                <a href="javascript:void(0)">
                    <img src="${ctxStatic}/ap/images/bus_icon01.png" />
                    <p>选择宽带</p>
                </a>
            </li>
            <li class="col col-12-3">
                <a href="javascript:void(0)">
                    <img src="${ctxStatic}/ap/images/bus_icon02.png" />
                    <p>信息填写</p>
                </a>
            </li>
            <li class="col col-12-3">
                <a href="javascript:void(0)">
                    <img src="${ctxStatic}/ap/images/bus_icon03.png" />
                    <p>确认下单</p>
                </a>
            </li>
            <li class="col col-12-3">
                <a href="javascript:void(0)">
                    <img src="${ctxStatic}/ap/images/bus_icon04.png" />
                    <p>上门安装</p>
                </a>
            </li>
        </ul>
    </div>
    <!--弹框 start -->
    <div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog new-modal">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <div class="modal-dow">
            <h4>温馨提示</h4>
            <div class="modal-text">
                <p>您的家庭网成员合计消费XXX元</p>
                <p class="font-dred">（月底结算时成员合计消费不足158元，不足部分从您的号码扣费补足差额）</p>
            </div>
            <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
        </div>
    </div>
</div>
</body>
</html>