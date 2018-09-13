<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="WT.si_n" content="和家庭" />
    <meta name="WT.si_x" content="确定安装信息" />
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/wt-sub.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/oil.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/goods/broadband/confirmHeInstall.js"></script>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
</head>

<body>

<%--<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>确定安装信息</h1>
    </div>
</div>--%>
<c:set value="确定安装信息" var="titleName" ></c:set>
<%@ include file="/WEB-INF/views/include/broadBandHead.jsp"%>

<form id="form1" name="form1" method="post">
    <input type="hidden" id="form1_skuId" name="form1_skuId"  value="${broadbandItem.goodsSkuId}"/>
    <input type="hidden" id="form1_addressName" name="form1_addressName"  value="${addressName}"/>
    <input type="hidden" id="form1_houseCode" name="form1_houseCode"  value="${houseCode}"/>
    <input type="hidden" id="form1_maxWidth" name="form1_maxWidth"  value="${maxWidth}"/>
    <input type="hidden" id="form1_freePort" name="form1_freePort"  value="${freePort}"/>
    <input type="hidden" id="form1_eparchyCode" name="form1_eparchyCode"  value="${eparchyCode}"/>
    <input type="hidden" id="form1_county" name="form1_county"  value="${county}" />
    <input type="hidden" id="form1_coverType" name="form1_coverType"  value="${coverType}"/>
    <input type="hidden" id="form1_chooseCat" name="form1_chooseCat"  value="${chooseCat}"/>
    <input type="hidden" id="form1_chooseBandWidth" name="form1_chooseBandWidth"  value="${chooseBandWidth}"/>
    <input type="hidden" id="form1_phoneNum" name="form1_phoneNum" value="${installPhoneNum}" />
    <input type="hidden" id="form1_productId" name="form1_productId" value="${productId}" />
    <input type="hidden" id="form1_packageId" name="form1_packageId" value="${packageId}" />
    <input type="hidden" id="form1_discntCode" name="form1_discntCode" value="${discntCode}" />
    <input type="hidden" id="form1_Mbh" name="form1_Mbh" value="${Mbh}" />
    <input type="hidden" id="form1_hasBroadband" name="form1_hasBroadband" value="${hasBroadband}" />
    <input type="hidden" id="form1_hasMbh" name="form1_hasMbh" value="${hasMbh}" />
    <input type="hidden" id="form1_addrDesc" name="form1_addrDesc" value="${addrDesc}" />
    <input type="hidden" id="form1_communityType" name="form1_communityType" value="${communityType}">
    <%--<input type="hidden" id="payMode" name="payMode" value="${payMode}" />--%>
    <%--<input type="hidden" id="payModeName" name="payModeName" value="${payModeName}" />--%>

    <div class="container bg-gray hy-tab">
        <div class="wf-list sub-info">
            <div class="wf-ait clearfix">
                <div class="wf-tit wf-cit font-gray">安装地址：</div>
                <div class="wf-con">
                    <p>${addressName}</p>
                </div>
            </div>
            <c:choose>
                <c:when test="${hasBroadband == '0'}">
                    <div class="wf-ait clearfix">
                        <div class="wf-tit wf-cit font-gray">用户信息</div>
                        <div class="wf-con">
                            <p>姓名：<span id="span_installName">${custName}</span></p>
                            <p>身份证：<span id="span_idCard">${psptId}</span></p>
                            <p>手机：<span id="span1_phoneNum">${installPhoneNum}</span></p>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="wf-ait clearfix sum">
                        <div class="wf-tit wf-cit font-gray">用户信息</div>
                        <div class="wf-con">
                            <p><span style="color:#f95657">*</span>&nbsp;姓名：<span id="span_installName">手机号所属人</span></p>
                            <p><span style="color:#f95657">*</span>&nbsp;身份证：<span id="span_idCard">手机号所属身份证号</span></p>
                            <p><span style="color:#f95657">*</span>&nbsp;手机：<span id="span1_phoneNum">${installPhoneNum}</span></p>
                            <span class="wf-icon"></span>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:if test="${hasBroadband == '1'}">
                <!--<div class="wf-ait clearfix ixchange">
                    <div class="wf-tit wf-cit font-gray">推荐人</div>
                    <div class="wf-con">
                        <p id="p_recommend">渠道推荐</p>
                        <p id="p_recommendNo">BOSS工号：</p>
                        <span class="wf-icon"></span>
                    </div>
                </div>-->

                <div class="wf-ait isBook">
                    <label class="font-gray">是否预约装机：</label>
                    <input type="radio" name="isBookInstall" value="0">否
                    <input type="radio" name="isBookInstall" value="1" checked="checked">是
                </div>

                <div class="wf-ait clearfix install-time">
                    <div class="wf-tit wf-cit font-gray">安装时间</div>
                    <div class="wf-con">
                        <ul class="share-date change-list install-list">
                            <li>
                                <label>预约安装时间：</label>
                                <div class="right-td">
                                    <div class="td-fr"><i class="select-arrow"></i>
                                        <select id="bookInstallDate" name="bookInstallDate" class="form-control">
                                            <option value="">---请选择---</option>
                                            <c:forEach items="${bookInstallDateList}" var="bookDate">
                                                <option value="${bookDate}">${bookDate}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <label>安装日期时段：</label>
                                <div class="right-td">
                                    <div class="td-fr"><i class="select-arrow"></i>
                                        <select id="bookInstallTime" name="bookInstallTime" class="form-control">
                                            <option value="">---请选择---</option>
                                        </select>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </c:if>
            <div class="wf-ait clearfix">
                <div class="wf-tit wf-cit">商品详情</div>
                <div class="wf-con">
                    <table cellpadding="0" cellspacing="0" class="wf-tabs">
                        <tr>
                            <th>商品名称</th>
                            <th>数量</th>
                            <th>商品单价</th>
                        </tr>
                        <c:if test="${hasBroadband == '1'}">
                            <tr>
                                <td>宽带光猫</td>
                                <td>1</td>
                                <td>¥0.00</td>
                            </tr>
                            <tr>
                                <td>${chooseBandWidth}M移动光宽带终身免费<b class="font-rose">（赠送）</b></td>
                                <td>1</td>
                                <td>¥0.00</td>
                            </tr>
                        </c:if>
                        <c:if test="${hasMbh == '1'}">
                            <tr>
                                <td>互联网电视两年<b class="font-rose">（赠送）</b></td>
                                <td>1</td>
                                <td>¥0.00</td>
                            </tr>
                            <tr>
                                <td>高清机顶盒</td>
                                <td>1</td>
                                <td>¥0.00</td>
                            </tr>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="wf-list change-new sub-new">
        <div class="wf-ait clearfix">
            <ul class="bar-list">
                <li style="height:30px;line-height:25px;">
                    <span class="font-gray">支付方式：</span>
                    <div class="sub-text">
                        <a href="javascript:void(0);" class="bar-btn active" onclick="choosePayMode('0','在线支付',this)">在线支付</a>
                        <%--<a href="javascript:void(0);" class="bar-btn" onclick="choosePayMode('Q','先装后付',this)">先装后付</a>--%>
                        <a href="javascript:void(0);" class="bar-btn" onclick="choosePayMode('6','余额支付',this)">余额支付</a>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="fix-br">
        <div class="affix foot-box">
            <div class="container active-fix">
                <div class="fl-left">
                    <p class="p1">应付总额：<b id="h_price" class="yq-jb font-rose">
                        0
                    </b>
                    </p>
                </div>
                <div class="fl-right"><a class="share11" id="a_submitInstallOrder1">立即支付</a></div>
                <!--当用户不能点击时在a class加dg-gray-->
            </div>
        </div>
    </div>
    <!--入网信息弹框 start-->
    <div class="sum-box share-sum"> <a class="cancel close-btn"></a> <span class="share-title">入网信息</span>
        <div class="share-content sum-con">
            <ul class="share-con">
                <li>
                    <label>手&emsp;机：</label>
                    <div class="right-td"><input type="text" class="form-control" id="phoneName" name="phoneName" value="${installPhoneNum}" readonly="readonly" otitle="手机号码" otype="button"/></div>
                </li>
                <li>
                    <label>姓&emsp;名：</label>
                    <div class="right-td">
                        <input type="text" class="form-control" id="installName" name="installName" otitle="手机号所属人" otype="button"
                            <c:choose>
                                <c:when test="${hasBroadband == '0'}">
                                    value="${custName}"
                                </c:when>
                                <c:otherwise>
                                    value="手机号所属人"
                                </c:otherwise>
                            </c:choose>
                         />
                    </div>
                </li>
                <li>
                    <label>身份证：</label>
                    <div class="right-td"><input type="text" class="form-control" id="idCard" name="idCard" otitle="手机号所属身份证号" otype="button"
                            <c:choose>
                                <c:when test="${hasBroadband == '0'}">
                                    value="${psptId}"
                                </c:when>
                                <c:otherwise>
                                    value="手机号所属身份证号"
                                </c:otherwise>
                            </c:choose>
                    /></div>
                </li>
            </ul>
            <!--提示信息 start-->
            <div class="ts-info font-red">提示：<span id="span_inputMesage">手机号、姓名、和身份证必须一致，否则会影响安装。</span></div>
            <!--提示信息 end-->
            <div class="zp-btn"><a class="btn btn-blue btn-block" id="a_confirm1" otitle="用户信息确认" otype="button">确认</a></div>
        </div>
    </div>
    <!--入网信息弹框 end-->
    <!--推荐人弹框 start-->
    <div class="sum-sd share-sum"> <a class="cancel close-btn"></a> <span class="share-title">推荐人</span>
        <div class="share-content sum-con">
            <ul class="share-con">
                <li>
                    <label><span class="pull-left">员工推荐</span><span class="pull-right"><input type="radio" name="radio_recommend" class="sub-radio" onclick="selType('tjr')" value="0" otitle="员工推荐" otype="button"/></span></label>
                    <span id="table1" class="shat-box" style="display:none"><input type="text" class="form-control" id="employeNo" name="employeNo" value="请输入以36开头的8位人力资源员工编号" otitle="员工推荐推荐人工号" otype="button"/></span>
                </li>
                <li>
                    <label><span class="pull-left">渠道推荐</span><span class="pull-right"><input type="radio" name="radio_recommend" class="sub-radio" onclick="selType('area')" value="1" otitle="渠道推荐" otype="button"/></span></label>
                    <span id="table2" class="shat-box" style="display:none"><input type="text" class="form-control" id="channelRecoNo" name="channelRecoNo" value="请输入渠道推荐人工号" otitle="渠道推荐推荐人信息" otype="button"/></span>
                </li>
            </ul>
            <!--提示信息 start-->
            <div class="ts-info font-red">提示：<span id="span_inputMesage2">请输入推荐信息。</span></div>
            <!--提示信息 end-->
            <div class="zp-btn"><a class="btn btn-blue btn-block" id="a_confirm2" otitle="渠道推荐确认" otype="button">确认</a></div>
        </div>
    </div>
    <!--推荐人弹框 end-->
    <!--安装时间弹框 start-->
    <div class="ibox-date share-sum"> <a class="cancel close-btn"></a> <span class="share-title">安装时间</span>
        <div class="share-content sum-con">
            <ul class="share-con">
                <li>
                    <label>
                        <span class="pull-left">休息日安装</span><span class="pull-right"><input type="radio" name="radio_installTime" class="sub-radio" value="0"/></span></label>
                </li>
                <li>
                    <label><span class="pull-left">工作日安装</span><span class="pull-right"><input type="radio" name="radio_installTime" class="sub-radio" value="1"/></span></label>
                </li>
                <li>
                    <label><span class="pull-left">休息工作日均可安装</span><span class="pull-right"><input type="radio" name="radio_installTime" class="sub-radio" value="2" /></span></label>
                </li>
            </ul>
            <div class="zp-btn"><a class="btn btn-blue btn-block" id="a_confirm3">确认</a></div>
        </div>
    </div>
    <!--安装时间弹框 end-->
</form>
<!--立即支付弹框 start-->
<div class="share-box share-sum"> <a class="cancel close-btn"></a> <span class="share-title">付款详情</span>
    <form id="payForm" name="payForm" method="post" action="payOrder">
        <input type="hidden" id="payForm_orderSubNo" name="payForm_orderSubNo" />
        <input type="hidden" id="payForm_payPlatform" name="payForm_payPlatform" />
        <input type="hidden" id="payForm_eparchyCode" name="payForm_eparchyCode" />
    </form>
    <div class="share-content">
        <p>订　单：<b class="cl-red" id="b_orderNo"></b></p>
        <p>支付金额：￥<span id="span_price">1000.00</span></p>
    </div>
    <div class="share-cz">
        <a href="#" class="cx-menu-item" name="a_pay" paytype="WAPIPOS_SHIPOS">
            <span class="item-img"><img src="${ctxStatic}/images/wt-images/hb_icon.png" /></span>
            <div class="cx-text">
                <p class="yw-title">和包支付</p>
                <p class="font-red ml100 lh40">*首次开通和包快捷完成付款最高送20元电子券<span class="m-bar"></span></p>
            </div>
            <span class="jt-icon"></span>
        </a>

          <a href="#" class="cx-menu-item" name="a_pay" paytype="WAPALIPAY">
            <span class="item-img"><img src="${ctxStatic}/images/alipay.jpg" /></span>
            <div class="cx-text">
               <div class="cx-text">
                <p class="yw-title">支付宝支付</p>
                <p class="font-red ml100 lh40">*首次开通和包快捷完成付款最高送20元电子券<span class="m-bar"></span></p>
            </div>
            </div>

            <span class="jt-icon"></span>
        </a>
    </div>
</div>

<div class="more-box"></div>
<script type="text/javascript" src="${ctxStatic}/js/nav.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/public.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tab.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script>
    $(function () {
        // 若不选择预约装机，则隐藏预约时间与日期，否则显示
        $('input:radio[name="isBookInstall"]').change(function(){
            if($(this).val() == "0"){
                $(".install-time").hide();
            }else{
                $(".install-time").show();
            }
        });
    });

    function selType(val){
        if(val=="tjr"){
            table1.style.display="block";
            table2.style.display="none";
        }else if(val=="area"){
            table1.style.display="none";
            table2.style.display="block";
        }
    }
    function choosePayMode(payMode,payModeName,param){
        $(".bar-list li a.active").toggleClass("active");
        $(param).addClass("active");
//        $("#payMode").val(payMode);
//        $("#payModeName").val(payModeName);
    }
</script>
</body>
</html>