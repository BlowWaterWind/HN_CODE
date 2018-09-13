<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/1/17
  Time: 19:49
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>优选号码-填写页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <meta name="format-detection" content="telphone=no, email=no"/>
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/normalize-6.0.0.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/pure-grids.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/css/wap.css"/>
    <%--<link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet">--%>

    <!-- JS -->
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
    <script type="text/javascript">
        var ctx = ${ctx};
    </script>
</head>
<body>


<div class="container">

    <div class="yxhm-fill">
        <!--套餐信息展示 start-->
        <div class="fill-list">
            <ul class="fill-info">
                <li>
                    <label>所选号码：</label>
                    <div class="fill-txt font-red">${chooseNum}</div>
                </li>
                <li>
                    <label>所选套餐：</label>
                    <div class="fill-txt font-red">${plan.planName}</div>
                </li>
            </ul>
        </div>
        <!--套餐信息展示 end-->
        <!--填写身份信息 start-->
        <form id="submitOrderToPay" method="post" action="submitOrderToPay">
            <%--支付方式--%>
            <input type="hidden" name="payPlatform">
            <%--由上个页面保存得来--%>
            <input type="hidden" id="cityCode" value="${cityCode}" name="orderDetailSim.cityCode">
            <input type="hidden" value="${cardFee}" name="cardFee" id="cardFee">
            <input type="hidden" value="${preFee}" name="preFee" id="preFee">
            <input type="hidden" id="phone" value="${chooseNum}" name="orderDetailSim.phone">
            <input type="hidden" id="planId" value="${plan.planId}" name="planId">
            <input type="hidden" id="planId" value="${confId}" name="confId">
            <input type="hidden" id="linkAddress" name="orderDetailSim.linkAddress">
            <div class="fill-list">
                <div class="fill-title">根据国家实名制要求，请准确提供身份证信息</div>
                <ul class="fill-info fill-border">
                    <li><label>姓名</label><div class="fill-txt"><input id="regName" name="orderDetailSim.regName" type="text" class="form-box-input" placeholder="请输入身份证件姓名"/></div>
                    </li>
                    <li><label>身份证</label><div class="fill-txt"><input id="psptId" name="orderDetailSim.psptId" type="text" class="form-box-input" placeholder="请输入身份证号"/></div>
                    </li>
                    <li><label>联系电话</label><div class="fill-txt"><input id="contactPhone" name="orderDetailSim.contactPhone" type="text" class="form-box-input" placeholder="请输入联系电话"/></div>
                    </li>
                </ul>
            </div>
            <!--填写身份信息 end-->
            <!--填写地址 start-->
            <div class="fill-list">
                <div class="fill-title">请填写配送地址<span class="font-red">（仅支持湖南省内配送）</span></div>
                <ul class="fill-info fill-border">
                    <li>
                        <label>所在地区</label>
                        <div class="fill-txt">
                            <span class="fill-arrow" id="picker">请选择区/县</span>
                        </div>
                    </li>
                    <li><input id="memberRecipientAddress" name="memberAddress.memberRecipientAddress" type="text" placeholder="街道/镇+村/小区/写字楼+门牌号" class="form-box-input"/>
                        <input type="hidden" id="memberRecipientCity" name="memberAddress.memberRecipientCity" value="${memberRecipientCity}">
                        <input type="hidden" id="memberRecipientCounty" name="memberAddress.memberRecipientCounty" value="${memberRecipientCounty}">
                    </li>
                </ul>
            </div>
        </form>
        <!--填写地址 end-->
        <div class="yxhm-agreement">
            <div class="agreement-list">
                <input name="checkbox" type="checkbox" id="ckbox01" class="radio"/>
                <label class="agreement-txt" for="ckbox01">我已阅读并同意<a class="font-blue" data-toggle="modal"
                                                                     data-target="#myModal3">《客户入网服务协议》</a></label>
            </div>
            <div class="agreement-list">
                <input name="checkbox" type="checkbox" id="ckbox02" class="radio"/>
                <label class="agreement-txt" for="ckbox02">我已阅读并同意<a class="font-blue" data-toggle="modal"
                                                                     data-target="#myModal4">《优选号码服务协议》</a></label>
            </div>
        </div>
        <!--确认办理按钮 start-->
        <div class="yxhm-footer-btn"><a class="yxhm-btn yxhm-btn-blue box" onclick="verifySubmit()">确认办理</a></div>
        <!--确认办理按钮 end-->
    </div>
</div>

<link rel="stylesheet" type="text/css" href="${ctxStatic}/ap/lib/picker/picker.css"/>
<script type="text/javascript" src="${ctxStatic}/ap/lib/picker/picker.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/ap/lib/picker/city.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/beautifulSim/onlineBeautifulSimConfirm.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<!--统一弹框 start-->
<div class="modal fade modal-prompt" id="sorry-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog yxhm-dialog">
        <div class="modal-con">
            <div class="modal-text _pomptTxt">
                <p>您的身份证信息填写错误<br>请重新输入</p>
            </div>
            <div class="yxhm-dialog-btn">
                <a onclick="toggleModalForBeauty('sorry-modal')" data-dismiss="modal" class="yxhm-link">我知道了</a>
            </div>
        </div>
    </div>
</div>

<%--服务协议弹出框--%>
<div class="modal fade modal-prompt" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog yxhm-dialog rwxy-dialog">
        <div class="modal-con">
            <h4>客户入网服务协议</h4>
            <div class="modal-text">
                <p>${serviceContractSim}</p>
            </div>
            <div class="yxhm-dialog-btn">
                <a href="javascript:void(0);" data-dismiss="modal" class="yxhm-link">我知道了</a>
            </div>
        </div>
    </div>
</div>
<div>
<%--服务协议弹出框--%>
<div class="modal fade modal-prompt" id="myModal4" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog yxhm-dialog rwxy-dialog">
        <div class="modal-con">
            <h4>客户入网服务协议</h4>
            <div class="modal-text">
                <p>${beautifulContract}</p>
            </div>
            <div class="yxhm-dialog-btn">
                <a href="javascript:void(0);" data-dismiss="modal" class="yxhm-link">我知道了</a>
            </div>
        </div>
    </div>
</div>
</div>

<!--选择支付方式 start-->
<div class="mask-box"></div>
<div class="zffs-fixed">
    <div class="container zffs-con">
        <div class="zffs-title">
            <h4>付款详情</h4>
            <a href="javascript:void(0)" class="zffs-btn cancel"></a>
        </div>
        <!--支付金额 start-->
        <div class="zffs-total">支付金额：<span class="font-red">XX.XX</span>元</div>
        <!--支付金额 end-->
        <ul class="zffs-list">
            <li>
                <a onclick="submitOrderAndPay()">
                    <img src="${ctxStatic}/images/beauty-sim/hebao.png"/>
                    <div class="zfss-txt">
                        和包支付
                        <span>移动自有快捷支付</span>
                    </div>
                </a>
            </li>
            <%--<li>
                <a href="javascript:void(0)">
                    <img src="${ctxStatic}/images/beauty-sim/wx.png"/>
                    <div class="zfss-txt">微信支付</div>
                </a>
            </li>
            <li>
                <a href="javascript:void(0)">
                    <img src="${ctxStatic}/images/beauty-sim/zfb.png"/>
                    <div class="zfss-txt">支付宝支付</div>
                </a>
            </li>--%>
        </ul>
    </div>
</div>
<!--选择支付方式 end-->
</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/beautifulSim/beauComm.js"></script>
</html>
