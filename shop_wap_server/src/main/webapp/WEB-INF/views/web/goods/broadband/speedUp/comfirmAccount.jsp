<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <title>宽带提速</title>
</head>
<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>确定套餐</h1>
    </div>
</div>
<div class="container bg-gray hy-tab">
    <div class="wf-list sub-info sub-tc">
        <div class="wf-ait clearfix">
            <div class="wf-tit">宽带账号：389999877</div>
            <div class="wf-con">
                <p class="font-gray">套餐名称：<span class="font-3">20M宽带/1年</span></p>
                <p class="font-gray">
                    <span class="sus-tit">合约期限：</span>
                    <span class="sus-text font-3">2015年03月28日~2016年04月04日</span>
                </p>
                <p class="font-gray">
                    <span class="sus-tit">安装地址：</span>
                    <span class="sus-text font-3">长沙市芙蓉区人民东路329号湘域熙25号</span>
                </p>
                <p class="font-gray">装  机  人：<span class="font-3">张三</span></p>
                <p class="font-gray">身  份 证：<span class="font-3">430703198801016688</span></p>
                <p class="font-gray">手　　机：<span class="font-3">13888999000</span></p>
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
                        <td>20M</td>
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
            <div class="fl-left"><a href="javascript:void(0);" data-toggle="modal" data-target="#myModal">温馨提示</a></div>
            <div class="fl-right"><a href="javascript:void(0);">立即办理</a></div>
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
            <div class="modal-btn"><a href="javascript:void(0);" data-dismiss="modal">确定</a></div>
        </div>
    </div>
</div>
<!--温馨提示弹框 end-->
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/rem.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
</body>
</html>
