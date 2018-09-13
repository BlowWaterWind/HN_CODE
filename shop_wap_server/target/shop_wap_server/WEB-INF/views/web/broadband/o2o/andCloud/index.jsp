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
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
    <script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
	<!-- 提示弹框 -->
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
	<script type="text/javascript">
    	var  ctx = '${ctx}';
        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
                + "wxyz0123456789+/" + "=";
    </script>
</head>

<body>
    <form action="${ctx}/o2oAndEyeCloud/comfirmOrder" id="form1">
        <input type="hidden" name="DURATION_VAL" id="DURATION_VAL">
        <input type="hidden" name="PACKAGE_VAL" id="PACKAGE_VAL">
        <input type="hidden" name="installPhoneNum" value="${installPhoneNum}">
        <input type="hidden" name="staffPassword" id="staffPassword">
    </form>
	<div class="top container">
	  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
	    <h1>和目云存储</h1>
	  </div>
	</div>
	<!--套餐选择 start-->
    <div class="container">
        <!--套餐信息 start-->
        <div class="wf-list change-info sub-new">
            <div class="wf-ait clearfix">
                <dl>
                    <dt><img src="demoimages/kdtu.png" /></dt>
                    <dd>
                        <h4>和目云存储</h4>
                        <p>套餐内容：高清远程观看、双向语音对讲、消息推送报警、24小时不间断云存储</p>
                    </dd>
                </dl>
            </div>
        </div>
        <!--套餐信息 end-->
        <!--选择套餐 start-->
        <ul class="hm-con">
            <li>
                <span class="hm-tit">存储容量：</span>
                <ul class="hm-list change" id="duration">
                    <li class="active" data-attr="7">7天</li>
                    <li data-attr="30">30天</li>
                </ul>
            </li>
            <li>
                <span class="hm-tit">套餐资费：</span>
                <ul class="hm-list change" id="package">
                    <li class="active" data-attr="MONTH">10元/月</li>
                    <li  data-attr="YEAR">88元/年</li>
                </ul>
            </li>
        </ul>
        <!--选择套餐 end-->
        <!--产品介绍 start-->
        <div class="hm-cpjs">
            <p class="cpjs-tit">产品介绍</p>
            <div class="cpjs-con">
                <p>产品介绍</p>
            </div>
        </div>
        <!--产品介绍 end-->
    </div>
    <div class="fix-br">
        <div class="affix foot-box new-foot">
            <div class="container active-fix">
                <div class="fl-left">
                    <p class="ft-total">资费：<span class="font-red" id="fee"></span></p>
                </div>
                <div class="fl-right"><a href="javascript:void(0);" data-toggle="modal" data-target="#myModal2">立即办理</a></div>
                <!--当用户不能点击时在a class加dg-gray-->
            </div>
        </div>
    </div>
    <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="js/modal.js"></script>
    <script>
    $("#duration li").on("click", function() {
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
        console.log($(this).html());
    });
    $("#package li").on("click", function() {
        $(this).siblings().removeClass("active");
        $(this).addClass("active");
        $("#fee").html($(this).html());
    });
    </script>
    <!--输入密码弹框 start-->
    <div class="modal modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog channel-modal">
            <div class="modal-info">
                <h4>提示</h4>
                <div class="modal-txt">
                    <p><input type="password" class="form-control" name="password" id="password" placeholder="请输入密码"/></p>
                </div>
                <div class="modal-btn">
                    <a href="javascript:void(0);" data-dismiss="modal" id="comfirm">确定</a>
                    <%--<button id="handleSpeedUp" class="btn btn-rose btn-block" data-dismiss="modal">立即办理</button>--%>
                </div>
            </div>
        </div>
    </div>
    <!--提交办理弹框 end-->
</body>
<script>
    $("#comfirm").click(function(){
        if($("#password").val()==''){
            showAlert("请输入渠道工号密码！");
            return ;
        }
        var password = encode64($("#password").val());
        $("#staffPassword").val(password);
        $("#DURATION_VAL").val($("#duration li.active").attr("data-attr"));
        $("#PACKAGE_VAL").val($("#package li.active").attr("data-attr"));
        $("#form1").submit();
    });
    function encode64(password) {

        return strEnc(password, keyStr);
    }
</script>
</html>