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
	<form action="broadbandInstall" id="form1" name="form1" method="post">
	<!--套餐信息 start-->
	<div class="wf-list change-info sub-new">
		<div class="wf-ait clearfix">
			<dl>
				<dt><img src="${ctxStatic}/demoimages/k01.png"/></dt>
				<dd>
					<h4>宽带新装</h4>
					<p></p>
				</dd>
			</dl>
		</div>
	</div>
	<!--套餐信息 end-->

	<!--填写信息 start-->
	<div class="wf-list sub-info sub-new">
      <div class="wf-ait clearfix">
        <ul class="wf-con clearfix">
          	<li>
           	  <span class="font-gray" style="line-height:35px;">手机：</span>
           	  <div class="sub-text"><input type="text" id="installPhoneNum" name="installPhoneNum" value="" class="form-control" placeholder="请输入手机号码" /></div>
          	</li>
        </ul>
      </div>
   </div>
   </form>
</div>

<div class="fix-br">
	<div class="affix foot-box new-foot">
		<div class="container active-fix">
			<div class="fl-right"><a id="operBtn">下一步</a></div>
		</div>
	</div>
</div>

<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript">
	$(function(){
		var isMobile =/^1(([0-9][0-9]))\d{8}$/;
		$("#operBtn").click(function(){
			if($("#installPhoneNum").val() == ""){
				showAlert("请输入办理用户手机号码！");
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