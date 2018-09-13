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
            <h1>选择套餐</h1>
        </div>
    </div>
    <div class="container">
        <div class="entry-info">
            <div class="entry-list">
                <div class="entry-title">
                    <p>已选择“<span class="font-blue">98元和家庭宽带套餐+9元固话套餐</span>”</p>
                    <p>根据国家实名制要求，请准确提供身份证信息</p>
                </div>
                <ul class="entry-form">
                    <li>
                        <label>姓名</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" placeholder="请输入身份证件姓名" />
                        </div>
                    </li>
                    <li>
                        <label>身份证</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" placeholder="请输入身份证号" />
                        </div>
                    </li>
                    <li>
                        <label>联系电话</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" placeholder="请先输入手机号码" />
                            <p class="pt5 font-red">输入的手机号有误</p>
                        </div>
                    </li>
                </ul>
            </div>
            <!--选择号码 start-->
            <div class="entry-list">
                <div class="entry-title">请选择号码</div>
                <ul class="entry-form">
                    <li>
                        <label>号码归属</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" placeholder="长沙" />
                            <i class="arrow"></i>
                        </div>
                    </li>
                    <li>
                        <label>选择号码</label>
                        <div class="entry-rt" data-toggle="modal" data-target="#myModal">
                            <input type="text" class="entry-box" placeholder="073188123412" />
                            <i class="arrow"></i>
                        </div>
                    </li>
                </ul>
            </div>
            <!--选择号码 end-->
            <!--请确认装机地址 start-->
            <div class="entry-list">
                <div class="entry-title">请确认装机地址<span class="font-red">（宽带固话为统一安装地址）</span></div>
                <div class="add-con">
                    <div class="add-list">
                        <label>所在地区：</label>
                        <div class="add-box">
                            <p>
                                <select class="form-control">
                                    <option>长沙市</option>
                                </select>
                            </p>
                            <p>
                                <select class="form-control">
                                    <option>长沙县</option>
                                </select>
                            </p>
                        </div>
                    </div>
                    <div class="add-list"><input type="text" class="form-control" placeholder="人民东路329号湘域熙岸4栋A单元1层201"></div>
                </div>
            </div>
            <!--请确认装机地址 end-->
            <!--安装时间 start-->
            <div class="entry-list">
                <div class="entry-title">安装时间</div>
                <ul class="entry-form">
                    <li>
                        <label>选择时间</label>
                        <div class="entry-rt" data-toggle="modal" data-target="#myModal2">
                            <input type="text" class="entry-box" placeholder="2017.09.06" />
                            <i class="arrow"></i>
                        </div>
                    </li>
                </ul>
            </div>
            <!--安装时间 end-->
            <!--协议 start-->
            <div class="broad-agre agreement-ckbox">
                <input type="checkbox" id="ckbox" />
                <label for="ckbox">我已阅读并同意《客户入网服务协议》</label>
            </div>
            <!--协议 end-->
        </div>
        <!--立即办理按钮 start-->
        <div class="broad-btn">
            <a href="javascript:void(0)" class="btn btn-blue" data-toggle="modal" data-target="#myModal3">立即办理</a>
            <!--灰色按钮class btn-blue 变为btn-gray-->
        </div>
        <!--立即办理按钮 end-->
    </div>

    <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="js/modal.js"></script>

    <!--号码选择 弹框 start-->
    <div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog broadband-dialog">
            <div class="modal-con">
                <div class="fixed-modal">
                    <div class="fixed-search">
                        <input type="text" class="form-control" placeholder="生日、幸运数字等" />
                        <a class="search-icon"></a>
                    </div>
                    
                    <ul class="fixed-list">
                        <li>073188123412</li>
                        <li>073188123412</li>
                        <li>073188123412</li>
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
    <!--号码选择 弹框 end-->

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
    <!--选择时间 弹框 end-->

    <!--提示 弹框 start-->
    <div class="modal fade modal-prompt" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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