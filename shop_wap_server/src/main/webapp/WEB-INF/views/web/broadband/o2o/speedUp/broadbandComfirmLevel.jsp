<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css" />

    <script  type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/o2o/speedUp/broadbandSpeedUp.js"></script>
    <title>湖南移动网上营业厅</title>
    <script>
        var baseProject = "${ctx}" ;
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
<div class="container bg-gray hy-tab">
    <form id="handleForm" method="post" action="${ctx}/o2oSpeedUp/handleSpeedUp">
        <input type="hidden"  name="bandAccount" id="bandAccount" value="${speedupRecord.bandAccount}"/>
        <input type="hidden" name="newBandWidth" id="newBandWidth" value="${speedupRecord.newBandWidth}"/>
        <input type="hidden" name="payAmount" id="payAmount" value="${speedupRecord.payAmount}"/>
        <input type="hidden" name="serialNumber" id="serialNumber" value="${speedupRecord.serialNumber}"/>
        <input type="hidden" name="productInfo" id="productInfo" value="${speedupRecord.productInfo}"/>
        <input type="hidden" name="startTime" value='<fmt:formatDate value="${speedupRecord.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
        <input type="hidden" name="endTime" value='<fmt:formatDate value="${speedupRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
        <input type="hidden" name="custName" id="custName" value="${speedupRecord.custName}"/>
        <input type="hidden" name="address" id="address" value="${speedupRecord.address}"/>
        <input type="hidden" name="psptId" id="psptId" value="${speedupRecord.psptId}"/>
        <input type="hidden" name="oldBandWidth" value="${speedupRecord.oldBandWidth}"/>
        <input type="hidden" name="staffPwd" id="staffPwd"/>
        <div class="wf-list sub-info sub-tc">
        <div class="wf-ait clearfix">
            <div class="wf-tit">宽带账号：${speedupRecord.bandAccount}</div>
            <div class="wf-con">
                <p class="font-gray">套餐名称：<span class="font-3">${speedupRecord.productInfo}</span></p>
                <p class="font-gray">
                    <span class="sus-tit">合约期限：</span>
                    <span class="sus-text font-3"><fmt:formatDate value="${speedupRecord.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>~ <fmt:formatDate value="${speedupRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                </p>
                <p class="font-gray">
                    <span class="sus-tit">安装地址：</span>
                    <span class="sus-text font-3">${speedupRecord.address}</span>
                </p>
                <p class="font-gray">装  机  人：<span class="font-3">${speedupRecord.custName}</span></p>
                <p class="font-gray">身  份 证：<span class="font-3">${speedupRecord.psptId}</span></p>
                <p class="font-gray">手　　机：<span class="font-3">${speedupRecord.serialNumber}</span></p>
            </div>
        </div>
        <div class="wf-ait clearfix">
            <div class="wf-tit wf-cit">商品详情</div>
            <div class="wf-con">
                <table cellpadding="0" cellspacing="0" class="wf-tabs">
                    <tr>
                        <th>商品名称</th>
                        <th>速率</th>
                        <th>金额</th>
                    </tr>
                    <tr>
                        <td>宽带提速</td>
                        <td>${speedupRecord.newBandWidth}</td>
                        <td>${speedupRecord.payAmount/100}元</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
        </form>
</div>
<div class="fix-br">
    <div class="affix foot-box new-foot">
        <div class="container active-fix">
            <div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#myModal" otitle="温馨提示"  otype="button" oarea="宽带提速">温馨提示</a></div>
            <div class="fl-right">
                <%--<a href="javascript:void(0)" data-toggle="modal" data-target="#myModal1" id="toSpeedup">立即办理</a>--%>
                <button id="toSpeedup" class="btn btn-rose btn-block" data-toggle="modal" data-target="#myModal1">立即办理</button>
            </div>
            <!--当用户不能点击时在a class加dg-gray-->
        </div>
    </div>
</div>
<!--温馨提示弹框 start-->
<div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog new-modal">
        <div class="modal-dow">
            <h4>温馨提示</h4>
            <div class="modal-text">
                <p>1、用户办理宽带提速包需与手机进行合账付费。</p>
                <p>2、用户办理提速包后立即生效，提速立即生效，合账付费当月生效，当月即扣除提速月费，用户宽带到期后，提速包同时失效。</p>
                <p>3、提速月费从手机账户余额中每月扣取提速费用。</p>
                <p>4、如果客户选择跨档次提速，则后台自动叠加跨档次资费。</p>
                <p>5、用户成功办理宽带提速包后，移动将下发短信告知用户办理情况。</p>
            </div>
            <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal" otitle="确认提速"  otype="button" oarea="宽带提速">确定</a></div>
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
                <a href="javascript:void(0);" data-dismiss="modal" id="handleSpeedUp">确定</a>
                <%--<button id="handleSpeedUp" class="btn btn-rose btn-block" data-dismiss="modal">立即办理</button>--%>
            </div>
        </div>
    </div>
</div>
<!--输入密码弹框 end-->

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript"  src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>
