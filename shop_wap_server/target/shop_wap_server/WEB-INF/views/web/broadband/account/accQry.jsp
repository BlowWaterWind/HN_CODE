<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%--插码相关--%>
    <meta name="WT.si_n" content="KD_CX" />
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
                <li class="on"><a href="javascript:void(0);">账号查询</a></li>
                <li style="border:none;"><a href="${ctx}broadbandAccount/accReset">密码修改/重置</a></li>
            </ul>
        </div>
        <div class="TabNote">
            <div class="block tab-cz" id="qryDiv">
                <div class="xg-pssword mt10">
                    <ul class="password-con">
                        <li>
                            <label>手机号码：</label>
                            <div class="right-td">
                                <input type="text" id="mobileNum" class="form-control form-fr" placeholder="请输入手机号码" />
                                <p class="cw-ts font-red hide">请输入正确的手机号码</p>
                            </div>
                        </li>
                        <li>
                            <label>验证码：</label>
                            <div class="right-td">
                                <input type="text" id="valiCode" class="form-control form-fr form-sd" placeholder="请输入验证码">
                                <input type="button" value="点击获取验证码" class="td-btn" id="getSmsCodeBtn">
                            </div>
                        </li>
                    </ul>
                    <!--查询按钮 start-->
                    <div class="form-group btn-box">
                        <a class="btn btn-blue btn-block" onclick="accountQry()">查询</a>
                    </div>
                    <!--查询按钮 end-->
                </div>
            </div>
            
            <div class="block tab-cz hide" id="qryResultDiv">
				<div class="xg-pssword mt10">
					<div class="pad-box">
						<table cellpadding="0" cellspacing="0" class="password-tabs">
							<tbody>
								<tr>
									<td class="pad-tit">宽带账号</td>
									<td id="accessAcct"></td>
								</tr>
								<tr>
									<td class="pad-tit">宽带速率</td>
									<td id="rate"></td>
								</tr>
								<tr>
									<td class="pad-tit">魔百和账户</td>
									<td id="mbh"></td>
								</tr>
								<tr>
									<td class="pad-tit">宽带优惠时间</td>
									<td id="yxDate"></td>
								</tr>
							</tbody>
						</table>
					</div>
					
					<!--查询按钮 start-->
<%-- 					<div class="pword-btn tj-btn"> <a class="btn btn-blue" href="##">宽带提速</a> <a class="btn btn-blue" href="##">宽带续费</a> </div> --%>
					<div class="return-btn"><a href="/shop/broadband/broadbandHome" class="btn btn-border-blue">返回宽带首页</a></div>
					<!--查询按钮 end--> 
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