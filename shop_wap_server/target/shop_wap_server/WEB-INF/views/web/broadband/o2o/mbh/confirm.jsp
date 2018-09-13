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
    <%@ include file="/WEB-INF/views/include/message.jsp"%><!-- 提示弹框 -->
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
<form method="post" id="confirmForm">
	<input type="hidden" id="phoneId" name="phoneId" value="${phoneId}"/>
	<input type="hidden" id="goodsSkuId" name="goodsSkuId" value="${goodsSkuId}"/>
	<input type="hidden" id="accessAcct" name="accessAcct" value="${accessAcct}"/>
	<input type="hidden" id="tvProductId" name="tvProductId" value="${tvProductId}"/>
	<input type="hidden" id="reqPackageId" name="reqPackageId" value="${reqPackageId}"/>
	<input type="hidden" id="reqElementId" name="reqElementId" value="${reqElementId}"/>
	<input type="hidden" id="giftElementId" name="giftElementId" value="${giftElementId}"/>
	<input type="hidden" name="staffPwd" id="staffPwd"/>
    <input type="hidden" id="payMode" name="payMode" value="${payMode}">
    <input type="hidden" id="payModeName" name="payModeName" value="${payModeName}">
</form>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>确定套餐</h1>
  </div>
</div>
<div class="container bg-gray hy-tab">
   <div class="wf-list sub-info sub-tc">
      <div class="wf-ait clearfix">
        <div class="wf-tit">宽带账号：${accessAcct}</div>
        <div class="wf-con">
          <p class="font-gray">套餐名称：<span class="font-3">${discntName}</span></p>
          <p class="font-gray">
            <span class="sus-tit">合约期限：</span>
            <span class="sus-text font-3">${startTime} ~ ${endTime}</span>
          </p>
          <p class="font-gray">
             <span class="sus-tit">安装地址：</span>
             <span class="sus-text font-3">${addrName}</span>
          </p>
          <p class="font-gray">装  机  人：<span class="font-3">${custName}</span></p>
          <p class="font-gray">身  份 证：<span class="font-3">${psptId}</span></p>
          <p class="font-gray">手　　机：<span class="font-3">${phoneId}</span></p>
        </div>
      </div>
      <div class="wf-ait clearfix">
        <div class="wf-tit wf-cit">商品详情</div>
        <div class="wf-con">
          <table cellpadding="0" cellspacing="0" class="wf-tabs">
             <tr>
               <th>商品名称</th>
               <th>数量</th>
               <th>商品单价</th>
             </tr>
             <tr>
               <td>魔百和${term}包</td>
               <td>1</td>
               <td>${price}元</td>
             </tr>
             <tr>
               <td>
                   <c:if test="${tvProductId=='20142000'}">
                    芒果TV
                    </c:if>
                   <c:if test="${tvProductId=='20141000'}">
                      未来TV
                   </c:if>
               </td>
               <td>1</td>
               <td>0元</td>
             </tr>
          </table>
        </div> 
      </div>
   </div>
</div>
<div class="fix-br">
  <div class="affix foot-box new-foot">
    <div class="container active-fix">
        <div class="fl-left">
            <p class="p1">应付总额：<b id="mbhPrice" class="cl-rose">${price}</b>元</p>
        </div>
        <div class="fl-right"><a class="share11" id="a_submitInstallOrder" data-dismiss="modal" data-toggle="modal" otitle="魔百和在线办理"  otype="button" oarea="魔百和在线办理" data-target="#myModal1">立即办理</a></div>
        <!--当用户不能点击时在a class加dg-gray-->
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
<form method="post" id="smsForm">
	<input type="hidden" id="goodsName" name="goodsName"/>
	<input type="hidden" id="serialNumber" name="serialNumber"/>
	<input type="hidden" id="broadbandType" name="broadbandType"/>
	<input type="hidden" id="orderId" name="orderId"/>
</form>
<!--输入密码弹框 end-->
<script type="text/javascript">
$(function(){
    $("#handleBusi").bind("click",function(){
    	var password = encode64($("#password").val());
		$("#staffPwd").val(password);
		$.ajax({
            type : 'post',
            url  : 'nopaysubmit',
            cache : false,
            context : $(this),
            dataType : 'json',
            data : $("#confirmForm").serializeObject(),
            success : function(data) {
            	if(data.code=="0"){
                	$("#goodsName").val(data.goodsName);
                	$("#serialNumber").val(data.serialNumber);
                	$("#broadbandType").val(data.broadbandType);
                	$("#orderId").val(data.orderId);
                	alert("短信发送成功！");                		
                	sendSms();
                }else{
                	showAlert(data.respDesc);
                    return false;
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

/**
 * 将表单元素序列化为JSON对象
 * 基于jQuery serializeArray()
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name] ];
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