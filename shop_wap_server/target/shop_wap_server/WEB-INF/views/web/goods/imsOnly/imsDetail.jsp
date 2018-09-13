<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_n" content="固话加装" />
    <meta name="WT.si_x" content="填写地址" />
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/kd.js"></script>

    <!-- 宽带新装相关js -->
    <%--<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/heBroadbandInstall.js"></script>--%>
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

    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <title>湖南移动网上营业厅</title>
    <script type="text/javascript">
        var  ctx = '${ctx}';
    </script>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>和家庭</h1>
    </div>
</div>--%>
<c:set value="确认套餐" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<div class="container bg-gray hy-tab">
    <form action="comfirmOrder" id="form1" name="form1" method="post">
        <input type="hidden" id="form1_skuId" name="form1_skuId"  value="${form1_skuId}" />
        <input type="hidden"  id="form1_productId" name="form1_productId" value="${form1_productId}" />
        <input type="hidden"  id="form1_packageId" name="form1_packageId" value="${form1_packageId}" />
        <input type="hidden"  id="form1_goodsName" name="form1_goodsName" value="${form1_goodsName}" />
        <input type="hidden"  id="form1_level" name="form1_level"  value="${form1_level}"/>
        <input type="hidden"  id="form1_addressName" name="form1_addressName" value="${form1_addressName}"/>
        <input type="hidden"  id="form1_houseCode" name="form1_houseCode" value="${form1_houseCode}"/>
        <input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode" value="${eparchy_Code}"/>
        <input type="hidden" id="form1_custName" name="form1_custName"/>
        <input type="hidden" id="form1_psptId" name="form1_psptId"/>
        <input type="hidden" id="imsPhone" name="imsPhone"/>
        <input type="hidden" id="installPhoneNum" name="installPhoneNum" value="${installPhoneNum}"/>
    </form>
    <%--<div class="top container">--%>
        <%--<div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>--%>
            <%--<h1>确定套餐</h1>--%>
        <%--</div>--%>
    <%--</div>--%>
    <div class="container">
        <div class="entry-info">
            <!--请确认装机地址 start-->
            <div class="entry-list">
            <!--选择号码 start-->
            <div class="entry-list">
                <div class="entry-title">请选择号码</div>
                <ul class="entry-form">
                    <li>
                        <label>号码归属</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box"  id="cityPlace" readonly value="${cityName}"/>
                            <%--<i class="arrow"></i>--%>
                        </div>
                    </li>
                    <li>
                        <label>选择号码</label>
                        <div class="entry-rt" data-toggle="modal" data-target="#myModal">
                            <input type="text" class="entry-box" readonly placeholder="请选择固话号码" id="chooseNumber" />
                            <i class="arrow"></i>
                        </div>
                    </li>
                    <li>
                        <label>姓名</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" placeholder="请输入身份证件姓名" id="installName"/>
                        </div>
                    </li>
                    <li>
                        <label>身份证</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" placeholder="请输入身份证号"  id="idCard"/>
                        </div>
                    </li>
                </ul>
            </div>
            <!--选择号码 end-->

            <!--请确认装机地址 end-->


            <!--安装时间 start-->
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
            <a href="javascript:void(0)" class="btn btn-blue" data-toggle="modal" data-target="#myModal3" id="orderComfirm">立即办理</a>
            <!--灰色按钮class btn-blue 变为btn-gray-->
        </div>
        <!--立即办理按钮 end-->
    </div>
    <!--号码选择 弹框 start-->
    <div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog broadband-dialog" style="margin-left: 0px;height: 404px;">
            <div class="modal-con">
                <div class="fixed-modal">
                    <div class="fixed-search">
                        <input type="text" class="form-control" placeholder="生日、幸运数字等" id="imsKey" />
                        <a class="search-icon" id="search"></a>
                    </div>
                    <ul class="fixed-list" id="numberContent" style="height: 195px;">
                    </ul>
                    <!--查找无记录 start-->
                    <div class="fixed-records" style="display:none;">
                        <p>很抱歉</p>
                        <p>暂无符合要求号码</p>
                    </div>
                    <!--查找无记录 end-->
                    <div class="fixed-change"><a href="javascript:void(0)" id="changeIms">换一批</a></div>
                </div>
            </div>
        </div>
    </div>
    <!--号码选择 弹框 end-->
</div>

<!--搜索地址 end-->
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript">
    var changeUrl="<%=request.getContextPath()%>/broadband/getChildrenByPid";
    var submitUrl="<%=request.getContextPath()%>/broadband/broadBandBook";
    var gotoSuccessUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallSuccess";
    var gotoErrorUrl="<%=request.getContextPath()%>/broadband/gotoBookInstallError";
</script>
<script>
    /**
     * 号码搜索
     * */
    $("#search").click(function(){
        var key = $("#imsKey").val();
        var eparchCode = $("#form1_eparchyCode").val();
        searchIms(key,eparchCode);
    });
    /**
     * 换一批号码
     * */
    $("#changeIms").click(function(){
        var key = $("#imsKey").val();
        var eparchCode = $("#form1_eparchyCode").val();
        searchIms(key,eparchCode);
    })
    /**
     * 号码池初始化
     * */
    $("#chooseNumber").click(function(){
        var key = $("#imsKey").val();
        var eparchCode = $("#form1_eparchyCode").val();
        $("#myModal").modal();
        searchIms(key,eparchCode);

    });
    function searchIms(key,eparchyCode){
        $.ajax({
            type:'get',
            url : "${ctx}/imsOnly/queryImsNumbers",
            cache : false,
            context : $(this),
            dataType : 'json',
            data : {
                key : key,
                eparchCode:eparchyCode
            },
            success : function(data) {
                console.log("data",data);
                var html = "";
                $.each(data,function(index,value){
                    if(value.SERIAL_NUMBER!=''&&value.SERIAL_NUMBER!=undefined){
                        html += "<li>"+value.SERIAL_NUMBER+"</li>"
                    }
                });
                $("#numberContent").html(html);
                $("#numberContent li").bind("click",function(){
                    var imsPhone = $(this).html();
                    $("#imsPhone").val(imsPhone);
                    $("#chooseNumber").val(imsPhone);
                    $("#myModal").hide();
                });
//                var html = "<li>"+value.SERIAL_NUMBER+"</li>"
            },
            error :function(){
                showAlert("号码池查询失败!");
            }
        });
    }

//    /**选择号码**/
//    $("#numberContent li").click(function(){
//
//    });


    $("#orderComfirm").click(function(){
        var messege = "";
        var pattern = /^[\u4E00-\u9FA5]{1,8}$/;
        if($("#imsPhone").val()==''){
            messege+="请选择号码!<br>";
        }
        if ($("#installName").val() == ""||!pattern.test($("#installName").val())) {
            messege+="姓名不能为空且长度不大于8!<br>"
        }
        var checkIdCardMsg = checkIdcard($("#idCard").val());
        if(checkIdCardMsg !="验证通过!"){
            messege+="身份证验证不通过！<br>"
        }
        if($("#form1_houseCode").val()==""){
            messege+="请选择详细地址!<br>";
        }
//        var bookInstallDate = $("#bookInstallDate").val();
//        var bookInstallTime = $("#bookInstallTime").val();
//
//        if(bookInstallDate==""){
//            messege+="请选择安装日期！<br>";
//        }
//        if(bookInstallTime==""){
//            messege+="请选择安装时间！<br>";
//        }
        if(!$("#ckbox").is(":checked")){
            messege+="未同意客户入网协议！<br>";
        }
        if(messege.length>0){
            showAlert(messege);
            return;
        }
        $("#form1_custName").val($("#installName").val());
        $("#form1_psptId").val($("#idCard").val());
        $("#form1").submit();
    });
</script>
</body>
</html>