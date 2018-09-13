<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KD_CZ" />
    <meta name="WT.si_x" content="20" />
    <Meta name="WT.mobile" content="">
    <Meta name="WT.brand" content="">
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
	<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/account.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w3.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/insertCodeKdWap.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
    	var  ctx = '${ctx}';
    </script>
</head>

<body>
<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>账号及密码查询</h1>
    </div>
</div>
<div class="container"><img src="${ctxStatic}/demoimages/pw.jpg" /></div>
<div class="container">
    <div class="Tab kd-password">
        <div id="box">
            <ul class="nav-tabs">
                <li><a href="${ctx}broadbandAccount/accQry">账号查询</a></li>
                <li style="border:none;" class="on"><a href="javascript:void(0);">密码修改/重置</a></li>
            </ul>
        </div>
        <div class="TabNote">
            <div class="block tab-cz" id="resetDiv">
                <div class="xg-pssword mt10">
                    <ul class="password-con">
                        <li>
                            <label>宽带账号：</label>
                            <div class="right-td">
                                <input id="accessAcct" type="text" class="form-control form-fr" placeholder="请输入宽带账号" />
                            </div>
                        </li>
                        <li>
                            <label>身份证：</label>
                            <div class="right-td">
                                <input id="psptId" type="text" class="form-control form-fr" placeholder="请输入身份证" />
                            </div>
                        </li>
                        <li>
                            <label>手机号码：</label>
                            <div class="right-td">
                                <input id="mobileNum" type="text" class="form-control form-fr" placeholder="请输入手机号码" />
                                <p class="cw-ts font-red hide">请输入正确的手机号码</p>
                            </div>
                        </li>
                        <li>
                            <label>验证码：</label>
                            <div class="right-td">
                                <input id="valiCode" type="text" class="form-control form-fr form-sd" placeholder="请输入验证码">
                                <input type="button" value="点击获取验证码" class="td-btn" id="getSmsCodeBtn">
                            </div>
                        </li>
                        <li>
                            <label>新密码：</label>
                            <div class="right-td">
                                <input id="newPwd" type="text" class="form-control form-fr" placeholder="请输入新密码" />
                            </div>
                        </li>
                        <li>
                            <label>确认密码：</label>
                            <div class="right-td">
                                <input id="confirm_newPwd" type="text" class="form-control form-fr" placeholder="请输入确认密码" />
                            </div>
                        </li>
                    </ul>
                    <!--重置按钮 start-->
                    <div class="form-group btn-box"> <a class="btn btn-blue btn-block" onclick="accountReset()" href="javascript:void(0);">重置</a> </div>
                    <!--重置按钮 end-->
                </div>
            </div>
            
            <div class="block tab-cz hide" id="resetResultDiv">
            	<div class="pd-succes">
					  <div class="pd-img pull-left"><img src="${ctxStatic}/images/succss-icon.png" /></div>
					  <div class="pd-text">密码修改成功！</div>
					  <div class="return-btn"><a href="/shop/broadband/broadbandHome" class="btn btn-border-blue">返回宽带首页</a></div>
				</div>
            </div>
            
            <div class="block tab-cz hide" id="errorDiv">
            	<div class="pd-succes">
					  <div class="pd-img pull-left"><img src="${ctxStatic}/images/error.png" /></div>
					  <div class="pd-text" id="errorInfo"></div>
					  <div class="return-btn"><a href="/shop/broadband/broadbandHome" class="btn btn-border-blue">返回宽带首页</a></div>
				</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>