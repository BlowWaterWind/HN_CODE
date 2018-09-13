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
<form action="imsNewSubmit" id="form1" name="form1" method="post">
	<input type="hidden" id="eparchyCode" name="eparchyCode"  value="${eparchyCode}" />

	<input type="hidden" name="goodsSkuId" id="goodsSkuId" value="${goodsSkuId}"/>
	<input type="hidden" id="monthCost" name="monthCost"  value="${monthCost}"/>
    <input type="hidden" id="bandWidth" name="bandWidth"  value="${bandWidth}"/>
    <input type="hidden" id="price" name="price" value="${price}" />
    <input type="hidden" id="productId" name="productId" value="${productId}" />
    <input type="hidden" id="packageId" name="packageId" value="${packageId}" />
    
    <input type="hidden" id="imsProductId" name="imsProductId" value="${imsProductId}" />
    <input type="hidden" id="imsPackageId" name="imsPackageId" value="${imsPackageId}" />
    <input type="hidden" id="imsOptDiscnt" name="imsOptDiscnt" value="${imsOptDiscnt}" />
    <input type="hidden" id="imsOptDiscntEnddate" name="imsOptDiscntEnddate" value="${imsOptDiscntEnddate}" />
    <input type="hidden" id="imsEparchyCode" name="imsEparchyCode" value="${imsEparchyCode}" />
	
	<input type="hidden" id="form1_maxWidth" name="form1_maxWidth" value="${form1_maxWidth}" />
    <input type="hidden" id="form1_freePort" name="form1_freePort" value="${form1_freePort}" />
    <input type="hidden" id="form1_eparchyCode" name="eparchyCode" value="${eparchyCode}" />
    <input type="hidden" id="form1_coverType" name="form1_coverType" value="${form1_coverType}" />
	<input type="hidden" id="install_county" name="install_county" value="${install_county}" />
	<input type="hidden" id="houseCode" name="houseCode" value="${houseCode}" />
	<input type="hidden" id="address" name="address" value="${address}" />
	
	<input type="hidden" name="isBookInstall" id="isBookInstall"/>
	<input type="hidden" name="bookInstallDate"/>
	<input type="hidden" name="bookInstallTime"/>
	<input type="hidden" id="form1_communityType" name="form1_communityType" value="${form1_communityType}" />
	
	<input type="hidden" id="tvType" name="tvType" value="${tvType}"/>
	<input type="hidden" id="tvboxSaleType" name="tvboxSaleType" value="${tvboxSaleType}"/>
	
	<div class="top container">
        <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
            <h1>选择套餐</h1>
        </div>
    </div>
    <div class="container">
        <div class="entry-info">
            <div class="entry-list">
                <div class="entry-title">
                    <p>已选择“<span class="font-blue">${chooseKdInfo}</span> + <span class="font-blue">${chooseImsInfo}</span> ”</p>
                    <p>根据国家实名制要求，请准确提供身份证信息</p>
                </div>
                <ul class="entry-form">
                    <li>
                        <label>客户姓名</label>
                        <div class="entry-rt">
                            <input type="text" id="installName" name="installName" class="entry-box" placeholder="请输入身份证件姓名" />
                        	<p class="pt5 font-red hide">输入的姓名有误</p>
                        </div>
                    </li>
                    <li>
                        <label>身份证号码</label>
                        <div class="entry-rt">
                            <input type="text" id="idCard" name="idCard" class="entry-box" placeholder="请输入身份证号" maxlength="18"/>
                            <p class="pt5 font-red hide">输入的身份证号有误</p>
                        </div>
                    </li>
                    <li>
                        <label>业务受理号码</label>
                        <div class="entry-rt">
                            <input type="text" class="entry-box" id="installNum" name="installNum" value="${installPhoneNum}" readonly="readonly"/>
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
                            <input type="text" class="entry-box" value="${cityName}" readonly="readonly"/>
                            <!-- <i class="arrow"></i>  -->
                        </div>
                    </li>
                    <li>
                        <label>选择号码</label>
                        <div class="entry-rt" data-toggle="modal" data-target="#myModal">
                            <input id="ims_sn" type="text" class="entry-box" placeholder="" />
                            <i class="arrow"></i>
                        </div>
                    </li>
                </ul>
            </div>
            <!--选择号码 end-->
            <!--安装时间 start-->
            <div class="entry-list">
                <div class="entry-title">安装时间</div>
                <ul class="entry-form">
                    <li>
                        <label>是否预约</label>
                        <div class="entry-rt">
                            <input type="text" data-toggle="modal" data-target="#myModal4" class="entry-box" value="否" />
                            <i class="arrow"></i>
                        </div>
                    </li>
                    <li id="chooseTimeLi">
                        <label>选择时间</label>
                        <div class="entry-rt" data-toggle="modal" data-target="#myModal2">
                            <input id="appointTime" type="text" class="entry-box" placeholder="请选择预约时间" />
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
            <a href="javascript:void(0)" class="btn btn-gray" data-toggle="modal" data-target="#myModal3">立即办理</a>
            <!--灰色按钮class btn-blue 变为btn-gray-->
        </div>
        <!--立即办理按钮 end-->
    </div>

    <!--号码选择 弹框 start-->
    <div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog broadband-dialog">
            <div class="modal-con">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                	<h4 class="modal-title">固话号码</h4>
            	</div>
                <div class="fixed-modal">
                    <div class="fixed-search">
                        <input id="searchImsNum" type="text" class="form-control" placeholder="生日、幸运数字等" />
                        <a class="search-icon" href="javascript:searchImsNum()"></a>
                    </div>
                    
                    <ul id="imsListUl" class="fixed-list">
                        <li>073188123411</li>
                        <li>073188123412</li>
                        <li>073188123413</li>
                        <li>073188123414</li>
                    </ul>

                    <!--查找无记录 start-->
                    <div class="fixed-records hide">
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
    
    <!--是否预约 弹框 start-->
    <div class="modal fade modal-prompt" id="myModal4" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog broadband-dialog">
            <div class="modal-con">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                	<h4 class="modal-title">是否预约</h4>
            	</div>
                <div class="fixed-modal">
                    <ul id="isYyUl" class="fixed-list">
                        <li>
                        	<div class="broad-agre agreement-ckbox">
                        	<input id="chk_yes" type="checkbox" value="1"/><label for="chk_yes">是</label></div>
                        </li>
                        <li>
                        	<div class="broad-agre agreement-ckbox">
                        	<input id="chk_no" type="checkbox" value="0"/><label for="chk_no">否</label></div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!--是否预约 弹框 end-->

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
                            <select id="bookInstallDate" class="form-control"></select>
                        </div>
                    </li>
                    <li>
                        <span class="rt-title">时间段选择：</span>
                        <div class="modal-rt">
                            <select id="bookInstallTime" class="form-control"></select>
                        </div>
                    </li>
                </ul>
                <div class="subform-btn">
                    <a href="javascript:$('.close').click();" class="btn btn-green">取消</a>
                    <a href="javascript:setAppointTime()" class="btn btn-blue">确定</a>
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
    </form>
    <script>
    	$(function(){
    		$("#installName").change(function(){
    			if($("#installName").val() != ""){
    				$("#installName").next().addClass("hide");
    			}else{
    				$("#installName").next().removeClass("hide");
    			}
    		});
    		
    		$("#idCard").change(function(){
    			var reg = /(^\d{15}$)|(^\d{18}(\d|X)$)/;
    			if($("#idCard").val() == "" || reg.test($("#idCard").val()) === false){
    				$("#idCard").next().addClass("hide");
    			}else{
    				$("#idCard").next().removeClass("hide");
    			}
    		});
    		
    		$("#imsListUl li").click(function(){
    			$("#ims_sn").val($(this).html());
    			$(".close").click();
    		});
    		
    		/*根据地址类型动态设置预约时间 */
    	    if($("#houseCode").val() != null && $("#houseCode").val() != ''){
    	        generateBookInstallTime($("#houseCode").val());
    	    }
    		
    		$("#isYyUl li #chk_yes").click(function(){
    			$("#chooseTimeLi").removeClass("hide");
    			$(".close").click();
    		});
    		
    		$("#isYyUl li #chk_no").click(function(){
    			$("#chooseTimeLi").addClass("hide");
    			$(".close").click();
    		});
    	});
    
    	function searchImsNum(){
    		$.ajax({
    			  url: ctx+"ims/searchImsNum", 
    			  data: {searchImsSn:$("#searchImsNum").val(),eparchyCode:$("#eparchyCode").val()},
    			  dataType: "json",
    			  type : 'post',
    			  success: function(e){
    				  	if(e.respCode == "0"){
    				  		
    		  			}else{
    		  				$(".fixed-records").removeClass("hide");
    		  			}
    			  },
    			  error: function(e) {
    				  $(".fixed-records").removeClass("hide");
    			  }
    		  });
    	}
    	
    	/**
    	 * 根据地址编码动态生成预约时间
    	 * @param obj
    	 */
    	function generateBookInstallTime(communityCode) {
    	    var url = ctx + "bandResourceQuery/queryAddressAttr";
    	    var data = {"addressCode": communityCode};
    	    $.getJSON(url, data, function (e) {
    	        // 防止重复添加option
    	        $("#bookInstallDate").empty();
    	        $("#bookInstallTime").empty();
    	        $("#bookInstallDate").append("<option value=''>---请选择---</option>");
    	        $("#bookInstallTime").append("<option value=''>---请选择---</option>");

    	        if (e.respCode != 0) {//调用接口失败！
    	            return ;
    	        } else {
    	            //给form1_communityType赋值
    	            $("#form1_communityType").val(e.result[0].COMMUNITY_TYPE);

    	            // 农村预约装机时间在72小时后，城市预约装机时间在48小时后； 01 为农村编码
    	            // var offTime = (e.COMMUNITY_TYPE == "01" ? (3 * 24 * 60 * 60 * 1000) : (2 * 24 * 60 * 60 * 1000));
    	            var offTime ;
    	            if(e.result[0].COMMUNITY_TYPE == "01"){
    	                offTime = 3 * 24 * 60 * 60 * 1000;
    	            }else if(e.result[0].COMMUNITY_TYPE == "02" || e.result[0].COMMUNITY_TYPE == "03" || e.result[0].COMMUNITY_TYPE =="04"){
    	                offTime = 2 * 24 * 60 * 60 * 1000;
    	            }else{
    	                alert("根据该地址查询地址信息异常!");
    	                return ;
    	            }

    	            //获取当前时间
    	            var currentTime = new Date();
    	            //能预约的最早时间
    	            var bookStartTime = new Date(currentTime.getTime() + offTime);

    	            // 初始化预约时间
    	            initBookTime(bookStartTime);

    	            //动态添加预约装机日期
    	            var dateOptHtml = "";
    	            var bookStartTimeMS = bookStartTime.getTime();
    	            for (var i = 1; i <= 4; i++) {
    	                dateOptHtml = "<option value='" + bookStartTime.toLocaleDateString() + "'>" + bookStartTime.toLocaleDateString() + "</option>";
    	                bookStartTimeMS += 1 * 24 * 60 * 60 * 1000;
    	                bookStartTime = new Date(bookStartTimeMS);
    	                $("#bookInstallDate").append(dateOptHtml);
    	            }


    	        }
    	    });

    	    // 当预约日期改变时动态改变时间
    	    $("#bookInstallDate").change(function () {
    	        $("#bookInstallTime").empty();
    	        var bookStartTime = $("#bookInstallDate").find("option:eq('1')").val();
    	        if ($("#bookInstallDate").val() == bookStartTime) {
    	            var currentTime = new Date();
    	            var currentTime = currentTime.getHours() + ":" + currentTime.getMinutes() + ":" + currentTime.getSeconds();
    	            $("#bookInstallTime").append("<option value=''>---请选择---</option>");
    	            initBookTime(new Date(bookStartTime + " " + currentTime));
    	        } else {
    	            $("#bookInstallTime").append("<option value=''>---请选择---</option>");
    	            var bookTimePeriod = ["上午08:00-12:00", "中午12:00-14:00", "下午14:00-18:00", "晚上18:00-20:00"];
    	            for (var i = 0; i < bookTimePeriod.length; i++) {
    	                $("#bookInstallTime").append("<option value='" + bookTimePeriod[i] + "'>" + bookTimePeriod[i] + "</option>");
    	            }
    	        }
    	    });
    	}


    	//初始化预约时间
    	function initBookTime(bookStartTime) {
    	    var bookTimePeriod = ["上午08:00-12:00", "中午12:00-14:00", "下午14:00-18:00", "晚上18:00-20:00"];
    	    var bookTimePoint = [new Date(bookStartTime.toLocaleDateString() + " 12:00"),
    	        new Date(bookStartTime.toLocaleDateString() + " 14:00"),
    	        new Date(bookStartTime.toLocaleDateString() + " 18:00"),
    	        new Date(bookStartTime.toLocaleDateString() + " 20:00")];

    	    var timePeriodCount;
    	    if (bookStartTime < bookTimePoint[0]) {
    	        timePeriodCount = 4;
    	    } else if (bookStartTime < bookTimePoint[1]) {
    	        timePeriodCount = 3;
    	    } else if (bookStartTime < bookTimePoint[2]) {
    	        timePeriodCount = 2;
    	    } else if (bookStartTime < bookTimePoint[3]) {
    	        timePeriodCount = 1;
    	    } else {
    	        timePeriodCount = 0;
    	    }
    	    for (var i = bookTimePeriod.length - timePeriodCount; i < bookTimePeriod.length; i++) {
    	        $("#bookInstallTime").append("<option value='" + bookTimePeriod[i] + "'>" + bookTimePeriod[i] + "</option>")
    	    }
    	}
    	
    	function setAppointTime(){
    		var appointTimeStr = $("#bookInstallDate").find("option:selected").text() + " " + $("#bookInstallTime").find("option:selected").text()
    		$("#appointTime").val(appointTimeStr);
    		$(".close").click();
    	}
    </script>
</body>
</html>