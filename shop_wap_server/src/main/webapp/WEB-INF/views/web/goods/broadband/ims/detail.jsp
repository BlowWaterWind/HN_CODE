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
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-addr.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-ims.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/broadband-agreement.js"></script>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
    <style>
    .sf-info-tip{font-size:12px;margin:5px 0 10px 10px;}
    .pinfo li{height:40px;line-height:40px;}
    </style>
</head>

<body>
<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>固话加装</h1>
    </div>
</div>
<!--套餐选择 start-->
<div class="container">
    
    <div class="sf-info-tip">已选择“固话套餐9元档”<br/>根据国家实名制要求，请准确提供身份证信息</div>
    
    <!--个人信息 start-->
    <div class="wf-list sub-info sub-new">
        <div class="wf-ait clearfix">
            <ul class="mt wf-con clearfix pinfo">
                <li> <span class="font-gray">手&nbsp;&nbsp;&nbsp;&nbsp;机：</span>
                    <div class="sub-text">${installPhoneNum}</div>
                </li>
                <li> <span class="font-gray">姓&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
                    <div class="sub-text">
                        <input type="text" class="form-control" placeholder="请与手机号码机主姓名保持一致" />
                    </div>
                </li>
                <li> <span class="font-gray">身份证：</span>
                    <div class="sub-text">
                        <input type="text" class="form-control" placeholder="请与手机号码机主证件保持一致" />
                    </div>
                </li>
            </ul>
        </div>
	</div>
	
	<div class="sf-info-tip">请选择号码</div>
	
    <!--固话套餐 start-->
    <div class="wf-list change-new sub-new">
        <div class="wf-ait clearfix">
            <ul class="bar-list">
                <li> <span class="font-gray">号码归属：</span>
                    <div class="sub-text">
                        <div class="td-fr">
                            <i class="select-arrow"></i>
                            <select class="form-control">
                                <option value="长沙市">长沙市</option>
                            </select>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="entrance-date">
                        <label class="font-gray">选择号码：</label>
                        <span id="selectedImsNum" class="arrow" data-toggle="modal" data-target="#myModal">073188123412</span>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <!--固话套餐 end-->
    
    <div class="sf-info-tip">请确认装机地址<span style="color:red">（宽带固话为统一安装地址）</span></div>
    <div id="broadband_addr_div"></div>
    <!--装机地址 satrt-->
    <!--  
    <ul class="change-list ym-zit ym-list">
        <li>
            <label class="font-gray"><em class="font-red">*</em>装机地址：</label>
            <div class="right-td"> <span class="td-fr"> <i class="select-arrow"></i>
                            <select class="set-sd">
                                    <option value="长沙市">长沙市</option>
                            </select>
                            </span> <span class="td-fr"> <i class="select-arrow"></i>
                            <select class="set-sd">
                                    <option value="长沙市">长沙市</option>
                            </select>
                            </span>
                <div class="zit-box">
                    <input type="text" class="form-control flip" placeholder="详细街道、小区、门牌号" />
                </div>
            </div>
        </li>
    </ul>-->
    <!--装机地址 end-->
    
    <div class="sf-info-tip">安装时间</div>
    
    <!--装机时间 start-->
    <div class="entrance-date">
        <label class="font-gray">装机时间：</label>
        <span class="arrow" data-toggle="modal" data-target="#myModal2">2017.09.06</span>
    </div>
    <!--装机时间 end-->
    <!--协议 start-->
    <div class="broad-agre">
        <label>
            <input type="checkbox" /> 我已阅读并同意《宽带入网协议》</label>
    </div>
    <!--协议 end-->
</div>
<div class="fix-br">
    <div class="affix foot-box">
        <div class="container">
            <a href="javascript:void(0)" class="btn btn-rose btn-block">下一步</a>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
<script>
//切换
$(".sub-broad a").on("click", function() {
    $(this).siblings().removeClass("active");
    $(this).addClass("active");
});
</script>

<!--固话号码选择 弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                <h4 class="modal-title">固话号码</h4>
            </div>
            <div class="fixed-modal">
                <div class="fixed-search">
                    <input type="text" class="form-control" placeholder="生日、幸运数字等" />
                    <a class="search-icon"></a>
                </div>
                
                <ul class="fixed-list">
                    <li>073188123411</li>
                    <li>073188123412</li>
                    <li>073188123413</li>
                    <li>073188123412</li>
                    <li>073188123412</li>
                    <li>073188123412</li>
                    <li>073188123412</li>
                    <li>073188123412</li>
                    <li>073188123412</li>
                    <li>073188123412</li>
                </ul>

                <!--查找无记录 start-->
                <div class="fixed-records" style="display:none;">
                    <p>很抱歉</p>
                    <p>暂无符合要求号码</p>
                </div>
                <!--查找无记录 end-->

                <div class="fixed-change"><a href="javascript:void(0)">换一批</a></div>
            </div>
        </div>
    </div>
</div>
<!--固话号码选择 弹框 end-->
<script type="text/javascript">
$(function(){
	$(".fixed-list li").click(function(){
		$("#selectedImsNum").html($(this).html());
		$(".close").click();
	});
});

</script>

<!--选择时间 弹框 start-->
<div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog broadband-dialog">
        <div class="modal-con">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                <h4 class="modal-title">安装时间</h4>
            </div>
            <ul class="modal-form">
                <li>
                    <span class="rt-title">日期选择：</span>
                    <div class="modal-rt">
                        <select class="form-control">
                            <option>2017.09.06</option>
                            <option>2017.09.06</option>
                            <option>2017.09.06</option>
                            <option>2017.09.06</option>
                        </select>
                    </div>
                </li>
                <li>
                    <span class="rt-title">时间段选择：</span>
                    <div class="modal-rt">
                        <select class="form-control">
                            <option>上午</option>
                            <option>中午</option>
                            <option>下午</option>
                            <option>晚上午</option>
                        </select>
                    </div>
                </li>
            </ul>
            <div class="subform-btn">
                <a href="javascript:void(0)" class="btn btn-green">取消</a>
                <a href="javascript:void(0)" class="btn btn-blue">确定</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>