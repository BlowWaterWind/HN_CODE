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
	<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />
    <script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <title>湖南移动网上营业厅</title>
     <script type="text/javascript">
    	var  ctx = '${ctx}';
    	
        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
                + "wxyz0123456789+/" + "=";
    </script>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>确定套餐</h1>
  </div>
</div>
<form id="form1" action="submitOrder" method="post">
	<input type="hidden" id="phoneId" name="phoneId" value="${phoneId}"/>
	<input type="hidden" name="installName" value="${installName}"/>
	<input type="hidden" name="idCard" value="${idCard}"/>
	
	<input type="hidden" name="productId" value="${productId}"/>
	<input type="hidden" name="packageId" value="${packageId}"/>
	<input type="hidden" name="goodsSkuId" value="${goodsSkuId}"/>
	
	<input type="hidden" name="isMbh" value="${isMbh}"/>
	<input type="hidden" name="isBroadBand" value="${isBroadBand}"/>

	<input type="hidden" name="houseCode" value="${houseCode}"/>
    <input type="hidden" name="address" value="${address}"/>
    <input type="hidden" name="form1_coverType" value="${form1_coverType}"/>
    <input type="hidden" name="form1_maxWidth" value="${form1_maxWidth}"/>
    <input type="hidden" name="form1_freePort" value="${form1_freePort}"/>
    
	<input type="hidden" name="eparchyCode" value="${eparchyCode}"/>
	
	<input type="hidden" name="form1_Mbh" value="${form1_Mbh}"/>
	
	<input type="hidden" name="isBookInstall" value="${isBookInstall}"/>
	<input type="hidden" name="bookInstallDate" value="${bookInstallDate}"/>
	<input type="hidden" name="bookInstallTime" value="${bookInstallTime}"/>
	<input type="hidden" name="form1_communityType" value="${form1_communityType}">
	
	<input type="hidden" name="price" value="${price}"/>
	<input type="hidden" id="minCost" name="minCost" value="${minCost}"/>
	<input type="hidden" name="monthCost" value="${monthCost}"/>
	<input type="hidden" name="bandWidth" value="${bandWidth}"/>
	
	<input type="hidden" name="memberListStr" value="${memberListStr}"/>
	<input type="hidden" name="staffPwd" id="staffPwd"/>
	<input type="hidden" id="payMode" name="payMode" value="${payMode}">
	<input type="hidden" id="payModeName" name="payModeName" value="${payModeName}">
	<input type="hidden" id="contactPhone" name="contactPhone" value="${contactPhone}">
</form>
<!--套餐选择 start-->
<div class="container">
  <div class="new-poplist clearfix">
  	<table cellpadding="0" cellspacing="0" class="new-tabs">
  	  <tr>
  	  	<td class="tr">活动名称：</td>
  	  	<c:choose>
  	  		<c:when test="${minCost == '0'}">
  	  		<td class="tl">组家庭网送${bandWidth}M宽带</td>
  	  		</c:when>
  	  		<c:otherwise>
  	  		<td class="tl">${minCost}元消费保底套餐</td>
  	  		</c:otherwise>
  	  	</c:choose>
  	  </tr>	
  	  <tr>
  	  	<td class="tr">联系电话：</td>
  	  	<td class="tl" id="td_phoneNum">${contactPhone}</td>
  	  </tr>	
  	  <c:choose>
  	  	<c:when test="${isBroadBand == '1'}">
			<c:if test="${minCost ne '88' and minCost ne '98'}">
	  	  	<tr>
		  	  	<td class="tr">用户名称：</td>
		  	  	<td class="tl">${installName}</td>
		  	  </tr>	
		  	  <tr>
		  	  	<td class="tr">身份证号码：</td>
		  	  	<td class="tl" id="td_idCard">${idCard}</td>
		  	  </tr>
			</c:if>
			<tr>
		  	  	<td class="tr">装机地址：</td>
		  	  	<td class="tl">${address}</td>
		  	  </tr>
			<c:if test="${minCost ne '88' and minCost ne '98'}">
			<tr>
		  	  	<td class="tr">装机时间：</td>
		  	  	<td class="tl">${bookInstallDate} ${bookInstallTime=="1"?"上午":"下午"}</td>
		  	  </tr>
			</c:if>
  	  	</c:when>
  	  	<c:otherwise>
  	  		<tr>
	  	  		<td class="tr">宽带账户：</td>
	  	  		<td class="tl">${accessAcct}</td>
	  	  	</tr>
  	  	</c:otherwise>
  	  </c:choose>
  	  
  	  <c:if test="${isMbh == '1'}">
	  	  <tr>
	  	  	<td class="tr">电视牌照方：</td>
	  	  	<td class="tl">
				<c:if test="${form1_Mbh == '0'}">
					芒果TV
				</c:if>
				<c:if test="${form1_Mbh == '1'}">
					未来电视
				</c:if>
	  	  	</td>
	  	  </tr>	
  	  </c:if>
		<c:choose>
		<c:when test="${minCost eq '88' or minCost eq '98'}">
			<tr>
				<td class="tr">宽带月费：</td>
				<td class="tl">0元/月</td>
			</tr>
			<tr>
				<td class="tr">电视月费：</td>
				<td class="tl">
					<c:if test="${form1_Mbh!=null}">
						20元/月
					</c:if></td>
			</tr>
			<tr>
				<td class="tr">保底消费：</td>
				<td class="tl">${minCost}元/月</td>
			</tr>
			<%--<tr>--%>
				<%--<td class="tr">设备费用：</td>--%>
				<%--<td class="tl">--%>
					<%--<p>10元/月</p>--%>
					<%--<p class="font-red">收取10个月，包含光猫与机顶盒</p>--%>
				<%--</td>--%>
			<%--</tr>--%>
			<tr>
				<td class="tr">合计费用：</td>
				<td class="tl">
					<c:if test="${form1_Mbh!=null}">
						${minCost+20}元/月
					</c:if>
					<c:if test="${form1_Mbh==null}">
						${minCost}元/月
					</c:if>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
  	  <tr>
  	  	<td class="tr">宽带月费：</td>
  	  	<td class="tl">${monthCost}元/月</td>
  	  </tr>	
  	  <tr>
  	  	<td class="tr">电视月费：</td>
  	  	<td class="tl">
			<c:if test="${form1_Mbh!=null}">
				20元/月
			</c:if></td>
  	  </tr>
		</c:otherwise>
		</c:choose>
		<tr>
			<td class="tr">初装费：</td>
			<td class="tl">${price}元</td>
		</tr>
  	  <tr>
  	  	<td class="tr">支付方式：</td>
  	  	<td class="tl">${payModeName}</td>
  	  </tr>
  	</table>
  </div>
</div>
<div class="fix-br">
	<div class="affix foot-box">
	<div class="container active-fix">
		<div class="fl-left">
			<p class="p1"><span class="cl-rose">　　　　　　　　　　　　　　　　
			</span></p>
		</div>
		<div class="fl-right"><a href="javascript:void(0);" data-dismiss="modal" data-toggle="modal" otitle="确认消费叠加型办理"  otype="button" oarea="消费叠加型" data-target="#myModal1">提交订单</a></div>
	</div>
	</div>
</div>
<!--输入密码弹框 start-->
<div class="modal modal-prompt" id="myModal1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog channel-modal">
        <div class="modal-info">
            <h4>请输入密码</h4>
            <div class="modal-txt">
                <p><input type="password" class="form-control" name="password" id="password" placeholder="请输入密码"/></p>
            </div>
            <div class="modal-btn">
                <a href="javascript:void(0);" data-dismiss="modal" id="handleBusi">确定</a>
            </div>
        </div>
    </div>
</div>
<!--输入密码弹框 end-->
<form method="post" id="smsForm">
	<input type="hidden" id="goodsName" name="goodsName"/>
	<input type="hidden" id="serialNumber" name="serialNumber"/>
	<input type="hidden" id="broadbandType" name="broadbandType"/>
	<input type="hidden" id="orderId" name="orderId"/>
</form>
<script type="text/javascript">
$(function(){
	$("#handleBusi").click(function(){
		var password = encode64($("#password").val());
		$("#staffPwd").val(password);
		var param = $("#form1").serializeObject();
		showLoadPop();
		$.ajax({
            type : 'post',
            url  : 'submitOrder',
            cache : false,
            context : $(this),
            dataType : 'json',
            data : $("#form1").serializeObject(),
            success : function(data) {
				if(data.X_RESULTCODE=='0'){
					hideLoadPop();
					window.location.href= ctx + "o2oBusiness/smssuccess";
				}else {
					hideLoadPop();
					if(data.message!=''||data.X_RESULTINFO!=''){
						showAlert(data.message||data.X_RESULTINFO);
					}else{
						showAlert("业务办理失败！");
					}
				}
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                showAlert("服务中断，请重试");
            }
        });
	});
});

function sendSms(){
	$.ajax({
        type : 'post',
        url :  ctx+'/o2oBusiness/invokeSmsInterface',
        cache : false,
        dataType : 'json',
        data : $("#smsForm").serializeObject(),
        success : function(data) {
            if (data.X_RESULTCODE=='0') {
            	window.location.href = ctx+ "/o2oBusiness/smssuccess";
            } else {
            	showAlert("短信发送失败！");
                showAlert(data.X_RESULTINFO);
                return false;
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlert("服务中断，请重试");
            return false;
        }
    });
}

$.fn.serializeObject = function()
{
   var o = {};
   var a = this.serializeArray();
   $.each(a, function() {
       if (o[this.name]) {
           if (!o[this.name].push) {
               o[this.name] = [o[this.name]];
           }
           o[this.name].push(this.value || '');
       } else {
           o[this.name] = this.value || '';
       }
   });
   return o;
};

function encode64(password) {

    return strEnc(password, keyStr);
}
</script>
</body>
</html>