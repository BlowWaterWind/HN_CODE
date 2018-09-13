<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_n" content="老客户续费" />
    <meta name="WT.si_x" content="业务详情" />
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>湖南移动网上营业厅</title>
</head>

<body>
<%--<div class="top container">
    <div class="header sub-title">
        <a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>宽带续费</h1>
        <a href="javascript:;" class="icon-right icon-add"></a>
    </div>
</div>--%>
<c:set value="宽带续费" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>
<c:choose>
    <c:when test="${empty userBroadbrandList}">
        <!--当前无绑定账户start-->
        <div class="container container-con">
            <div class="card4g-popup card4g-success">
                <span class="fr-cl">
                    <a href="javascript:;" class="addKdphpne">
                        <img src="${ctxStatic}/images/wt-images/add-icon-blue.png">
                    </a>
                </span>
                <span>尊敬的用户，<br/>您当前无绑定的账号，请先绑定需要续费的账号。</span>
            </div>
        </div>
        <!--当前无绑定账户 end-->
    </c:when>
    <c:otherwise>
        <div class="container bg-gray hy-tab">
            <div class="wf-list">
                <c:forEach items="${userBroadbrandList}" var="broadbandDetailInfo">
                    <div class="wf-ait clearfix">
                       <div class="wf-list">
		     			 <div class="wf-ait clearfix">
			       			 <div class="wf-tit"><span class="pull-left">宽带账号： }</span></div>
			       			 <div class="wf-con kd-renew-con">
						        <p class="font-gray">套餐名称：<span class="font-3">${broadbandDetailInfo.discntName}</span></p>
						        <p class="font-gray">
					           	 	<span class="sus-tit">到期时间：</span>
					            	<span class="sus-text font-3">${broadbandDetailInfo.endTime}</span>
			          			</p>
                                 <p class="font-gray">
                                     <span class="sus-tit">宽带速率：</span>
                                     <span class="sus-text font-3">${broadbandDetailInfo.rate}M</span>
                                 </p>
					             <p class="font-gray">
					             	<span class="sus-tit">安装地址：</span>
					             	<span class="sus-text font-3">${broadbandDetailInfo.addrDesc}</span>
					            </p>
			         			<p class="font-gray">姓　　名：<span class="font-3">${broadbandDetailInfo.custName}</span></p>
                                <%--<div class="rk-con">
                                    <label class="font-gray">关联手机：</label>
                                      <div class="kd-zm"><input type="text" id="txt_phoneNum" name="txt_phoneNum" class="form-control" value="${userBroadbrand.phoneNum}" /></div>
                                </div>--%>
			        		</div>
			      		    <div class="zp-btn wf-btn"><a class="btn btn-blue btn-block"  onclick="broadBandRenew('${broadbandDetailInfo.accessAcct}','${eparchyCode}')" >立即续费</a> </div>
		     	 	  </div>
				   </div>
				</div>
                </c:forEach>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<!--绑定宽带账户弹框 start-->
<div class="mask"></div>
<div class="share-box share-sum share-box-top" >
    <div class="share-content">
        <div class="con-changae">
            <label><input type="radio" name="numType" value="idNum" checked="checked" />宽带账号查询</label>
        </div>
        <div><input id="num" type="text" name="num" class="form-control" /></div>
        <div class="change-city">
            <label>选择地市：</label>
            <div class="change-box">
                <span class="td-fr"><i class="select-arrow"></i>
                     <select id="cityCode" class="form-control">
                        <c:forEach items="${cityList}" var="city">
                            <option value="${city.eparchyCode}">${city.orgName}</option>
                        </c:forEach>
                     </select>
                </span>
            </div>
        </div>
        <div class="text-center kd-renew-phone-search">
            <a id="searchBtn" class="btn btn-bd kdicon-search" href="javascript:;">搜索</a>
            <a class="btn btn-bd btnCancel" href="javascript:;">取消</a>
        </div>
    </div>
    <div class="kd-renew-phone-info">
        <div class="kd-renew-phone-info-title">宽带信息</div>
        <div class="kd-renew-con">
            <p>宽带账号：<span id="broadbandAccount"></span></p>
            <p>套餐名称：<span id="prodName"></span></p>
            <p>
                <span class="sus-tit">有效时间：</span>
                <span id="validity" class="sus-text font-red"></span>
            </p>
            <p>安  装 人：<span id="custName"></span></p>
            <p>
                <span class="sus-tit">地　　址：</span>
                <span class="sus-text" id="installAddress"></span>
            </p>
            <p align="center">
            	 <span id="message" class="sus-text font-red"></span>
            </p>
        </div>
        <div class="text-center kd-btn">
            <a id="bindBtn" class="btn btn-blue" href="javascript:;" onclick="broadbandBind()">立即绑定</a>
            <a class="btn btn-gray reset-cx" href="javascript:;">重新查询</a>
        </div>
    </div>
</div>

<form id="renewForm" action="bindBrodbandAccount" method="post">
    <input type="hidden" name="broadbandAccount"/>
    <input type="hidden" name="prodName"/>
    <input type="hidden" name="startTime"/>
    <input type="hidden" name="endTime"/>
    <input type="hidden" name="custName"/>
    <input type="hidden" name="installAddress"/>
    <input type="hidden" name="broadbandSpeed"/>
    <input type="hidden" name="broadbandContract"/>
    <input type="hidden" name="cityCode"/>
    <input type="hidden" name="phoneNum"/>
</form>

<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/broadband/renew.js"></script>
<%@ include file="/WEB-INF/views/include/message.jsp"%>

<script type="text/javascript">
    $(function () {
        //搜索
        $("#searchBtn").on("click",function(){
            var num = $("#num").val();
            if(num.length == 0){
                return;
            }

            var numType = $("input[name='numType']").val();
            var cityCode = $("#cityCode").val();

            var param = {"numType":numType,"num":num,"cityCode":cityCode};
            var url = "queryBroadbandInfo";

            $.post(url,param,function(data){
                $("#message").text("");
                $("#broadbandAccount").text(data["broadbandAccount"]);
                $("#prodName").text(data["prodName"]);
                $("#validity").text(data["startTime"] + "~" + data["endTime"]);
                $("#custName").text(data["custName"]);
                $("#installAddress").text(data["installAddress"]);

                $("#renewForm input[name='broadbandAccount']").val(data["broadbandAccount"]);
                $("#renewForm input[name='prodName']").val(data["prodName"]);
                $("#renewForm input[name='startTime']").val(data["startTime"]);
                $("#renewForm input[name='endTime']").val(data["endTime"]);
                $("#renewForm input[name='custName']").val(data["custName"]);
                $("#renewForm input[name='installAddress']").val(data["installAddress"]);
                $("#renewForm input[name='broadbandSpeed']").val(data["broadbandSpeed"]);
                $("#renewForm input[name='broadbandContract']").val(data["broadbandContract"]);
                $("#renewForm input[name='cityCode']").val(cityCode);
            });

        });
    });
</script>
</body>
</html>