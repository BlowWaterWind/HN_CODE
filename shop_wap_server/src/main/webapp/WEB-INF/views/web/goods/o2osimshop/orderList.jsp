<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/6/7
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>O2O在线售卡能力</title>
    <meta name="WT.si_n" content="O2O在线售卡能力_订单列表">
    <meta name="WT.si_x" content="O2O在线售卡能力_订单列表">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>

    <link href="${ctxStatic}/css/sim/wap-simh5online.css?v=10" rel="stylesheet"><!--这个页面选号的弹出框-->
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/sim/sim-h5o2oonline.css"/>

    <link href="${ctxStatic}/css/sim/sim-loading.css?v=1" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/jquery.select.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/dropload/dropload.min.js"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
</head>
<body class="bg-gray2">
<div class="container">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return">返回</a>
        <a href="javascript:window.history.back();" class="icon icon-close">关闭</a>
        <a href="javascript:window.location.reload();" class="refresh"></a>
        <h3>订单查询页</h3>
    </div>

    <div class=" ">
        <div class="input-search"><a class="search-btn" id="queryOrder"></a><input id="searchKey" type="search"
                                                                                   placeholder="输入用户手机号查询订单"></div>
        <div class="bg-white p20 tc select-list clearfix">
            <select id="select1">
                <option value="0">全部类型</option>
                <option value="1">店铺自取</option>
                <option value="2">快递送货</option>
            </select>
            <select id="select2">
                <option value="0">全部状态</option>
                <option value="1">待写卡</option>
                <option value="2">待处理</option>
                <option value="3">已超时</option>
                <option value="4">办理成功</option>
                <option value="5">办理失败</option>
            </select>
        </div>
        <div class="orders" id="orderContent"></div><!--返回的数据放在这里-->
    </div>

    <!-- 公共加载弹窗 -->
    <div id="loding-modal" class="mask-layer">
        <div class="modal min-modal">
            <div class="modal-content">
                <div class="modal-con">
                    <div class="dropload-load">
                        <div class="loading"></div>
                    </div>
                    <p class="text-center _promptSorry"></p>
                    <p class="text-center _pomptTxt"></p>
                    <div class="pb-20"></div>
                </div>
            </div>
        </div>
    </div>

    <%-- 公用弹窗 --%>
    <div id="sorry-modal" class="mask-layer">
        <div class="modal small-modal">
            <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')"></a>
            <div class="modal-content">
                <div class="pt-30"></div>
                <p class="text-center _pomptTxt"></p>
            </div>
            <div class="btn-group">
                <a href="orderlist" class="confirm-btn">刷新页面</a>
            </div>
        </div>
    </div>

    <!-- 公共加载弹窗 -->
    <div id="loding-modal" class="mask-layer">
        <div class="modal min-modal">
            <div class="modal-content">
                <div class="modal-con">
                    <div class="dropload-load">
                        <div class="loading"></div>
                    </div>
                    <p class="text-center _promptSorry"></p>
                    <p class="text-center _pomptTxt"></p>
                    <div class="pb-20"></div>
                </div>
            </div>
        </div>
    </div>

</div>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">
    var pageNo = 0;
    var orderType = 0;
    var orderStatus = 0;
    var ctxStatic = "${ctxStatic}";
    var ctx = "${ctx}";

    refreshOrderList = function refreshOrderList(){
        pageNo = 0;
        $("#orderContent").empty();
        $("#orderContent").dropload({
            scrollArea: window,
            domUp: {
                domClass: 'dropload-up',
                domRefresh: '<div class="dropload-refresh">↓下拉刷新</div>',
                domUpdate: '<div class="dropload-update">↑释放更新</div>',
                domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
            },
            domDown: {
                domClass: 'dropload-down',
                domRefresh: '<div class="dropload-refresh">↑上拉加载更多</div>',
                domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
                domNoData: '<div class="dropload-noData">已经到底了...</div>'
            },
            loadUpFn: function (me) {
                pageNo = 0;
                window.location.reload();
            },
            loadDownFn: function (me) {
                loadData(me);
            }
        });
    };

    $(function () {
        $('select').niceSelect();
        $("#queryOrder").click(refreshOrderList)
        refreshOrderList();
    });

    function loadData(me) {
        //$('.orders-lists').html('');
        // 页数
        // 每页展示5个
        var pageSize = 10;
        // 拼接HTML
        pageNo++;
        var contentHtml = '';
        $.ajax({
            type: 'GET',
            url: "${ctx}o2oSimOper/getOrderList",//设置请求地址
            dataType: 'json',
            data: {
                orderType: orderType,
                orderStatus: orderStatus,
                searchKey: $("#searchKey").val(),
                pageNo: pageNo,
                pageSize: pageSize
            },
            success: function (data) {
                if (data.list != null && data.list.length > 0) {
                    pageNo = data.pageNo;
                    var contentHtml = "";
                    var reg = /套餐(\S*)元/;
                    $.each(data.list, function (i, val) {
                        var orderTime = new Date(val.orderTime);
                        var dateTime = orderTime.getFullYear() + "-" + (orderTime.getMonth() + 1) + "-" + orderTime.getDate();
                        contentHtml += '<div class="mt20"><div class="list-order"><p class="font-red pull-right">'+val.orderStatus.orderStatusName+'</p>'
                            + '<p>' + val.detailSim.baseSetName;
                        if (val.orderAddressId == '1') {
                            contentHtml += '(自取)</p>';
                        } else {
                            contentHtml += '(邮寄)</p>';
                        }
                        contentHtml += '</div><div class="list-order list-order-flex"><div class="list-order-img">' +
                            '<img src=' + ctxStatic + '/images/simpay/pay_' + val.detailSim.cardType + '.png></div>' +
                            '<div class="pl20 "><p>用户手机号：<span>' + val.detailSim.contactPhone + '</span></p>' +
                            '<p>下单时间：<span>' + dateTime + '</span></p></div></div>' +
                            '<div class="list-order tr border-b-gray">';
                        if (val.orderStatusId == '2') {
                            //去确认,查看同步失败的原因
                            contentHtml += '<a href="getOrderDetail?orderSubNo=' + val.orderSubNo + '" class="btn btn-r btn-border btn-middle">去确认</a>'
                        } else if (val.orderStatusId == '4') {
                            //确认备货
                            contentHtml += '<a onclick="process(this)" value='+ val.orderSubNo +' operation="conDelivery" class="btn btn-r btn-border btn-middle">确认备货</a>'
                        } else if (val.orderStatusId == '6') {
                            //确认发货
                            contentHtml += '<a  href="deliverySim?orderSubNo=' + val.orderSubNo + '"  class="btn btn-r btn-border btn-middle">去发货</a>'
                        } else if (val.orderStatusId == '76') {
                            //去写卡
                            contentHtml += '<a href="writesim?orderSubNo=' + val.orderSubNo + '" class="btn btn-r btn-border btn-middle">去写卡</a>'
                        } else if(val.orderStatusId == '7'){//收货
                            contentHtml += '<a onclick="process(this)" value='+ val.orderSubNo +' operation="received" class="btn btn-r btn-border btn-middle">确认收货</a>'
                        }else{
                            contentHtml += '';
                        }
                        contentHtml += '<a href="getOrderDetail?orderSubNo=' + val.orderSubNo + '" class="btn btn-r btn-border btn-middle ml20">查看详情</a></div></div>'

                        if (data.pageNo == 1 && data.list.length == 0) {
                            contentHtml = '<div class="orders-search-error"><p>很抱歉，没有找到相关订单记录！</p></div>';

                        }
                    });
                    if (data.list.length <= 10 && pageNo >= data.totalPage) {
                        me.lock();
                        me.noData();
                    }
                    // 如果没有数据
                } else {
                    // 锁定
                    me.lock();
                    // 无数据
                    me.noData();
                }
                // 为了测试，延迟1秒加载
                setTimeout(function () {
                    // 插入数据到页面，放到最后面
                    $('.dropload-down').before(contentHtml);
                    // 每次数据插入，必须重置
                    me.resetload();
                }, 1000);
            },
            error: function (xhr, type) {
                console.log('Ajax 错误!');
                // 即使加载出错，也得重置
//                        me.resetload();
            }
        });
    }

    $("#select1").change(function () {
        orderType = $(this).find('option:selected').val();
        refreshOrderList();
    });
    $("#select2").change(function () {
        orderStatus = $(this).find('option:selected').val();
        refreshOrderList();
    });

    function process(object) {
        var orderSubNo = $(object).attr('value');
        var oper = $(object).attr('operation');
        toggleModal("loding-modal", "", "正在处理...");//打开加载框
        $.ajax({
            url: ctx + "o2oSimOper/process",
            type: "post",
            data: {orderSubNo:orderSubNo,process:oper},
            dataType: "json",
            success: function (data) {
                $("#loding-modal").hide();
                if(data.code == '0'){
                    toggleModal("sorry-modal", "", data.message, "订单处理成功");
                }else{
                    toggleModal("sorry-modal", "", data.message, "订单处理失败");
                }
            },
            error:function(){
                $("#loding-modal").hide();
                toggleModal("sorry-modal", "", "系统异常", "订单处理失败");
            }
        });
    }

</script>
</body>
</html>
