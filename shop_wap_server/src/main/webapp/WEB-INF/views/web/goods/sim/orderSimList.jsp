<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <%--<meta name="format-detection" content="telephone=no">--%>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>在线售卡H5-我的订单</title>
    <link href="${ctxStatic}/css/sim/wap_list.css" rel="stylesheet">
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/js/comm.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/dropload/dropload.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>


</head>


<body>
<!-- start container -->
<div class="container bg-gray">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return">返回</a>
        <a href="javascript:window.history.back();" class="icon icon-close">关闭</a>
        <h3>我的订单</h3>
    </div>
    <form:form action="${ctx}myOrder/querySimOrderList" method="post" id="form1">
        <input type="hidden" name="statusId" value="${statusId}">
        <input type="hidden" name="memberId" value="${memberId}">
        <!-- end topbar -->
        <div class="tabs">
            <ul class="tabs-bar">
                <li <c:if test="${statusId==''||statusId==null}"> class="active" </c:if>><a
                        href="${ctx}myOrder/querySimOrderList?psptId=${psptId}">全部</a></li>
                <li <c:if test="${statusId==6}"> class="active" </c:if> ><a
                        href="${ctx}myOrder/querySimOrderList?statusId=6&psptId=${psptId}">待发货</a></li>
                <li <c:if test="${statusId==7}"> class="active" </c:if> ><a
                        href="${ctx}myOrder/querySimOrderList?statusId=7&psptId=${psptId}">待收货</a></li>
                <li <c:if test="${statusId==12}"> class="active" </c:if> ><a
                        href="${ctx}myOrder/querySimOrderList?statusId=12&psptId=${psptId}">已收货</a></li>
            </ul>
            <div id="tab-1">
                <%--<c:if test="${memberId==''||memberId==null}">--%>
                    <div class="orders-wrap" style="position: relative">
                        <div class="orders-search">
                            <input type="text" name="psptId" id="psptId" value="${psptId}" onkeyup="psptKey(this)" placeholder="输入身份证或订单号或SIM号查询">
                            <i class="clear-index" style="display: none"></i>
                            <input type="button" name="" id="queryOrder" value="查询">
                        </div>
                    </div>
                <%--</c:if>--%>
                <c:if test="${memberId!=null||psptId!=null}">
                    <%--<div class="ajax-content">
                        <!-- start orders-lists -->
                        <div class="orders-lists" id="orderContent">
                        </div>
                    </div>--%>
                    <div class="orders" id="orderContent">
                    </div>
                </c:if>
            </div>
        </div>
    </form:form>
</div>
<!-- end container -->
<!-- start modal -->
<div id="orders-tips" class="mask-layer">
    <div class="modal small-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('orders-tips')"></a>
        <div class="modal-content">
            <div class="modal-con">
                <div class="pt-50"></div>
                <p class="text-center">您当前没有订单信息，
                    <br>请点击确定前往申请号卡！</p>
            </div>
            <div class="modal-btn-wrap">
                <a href="javascript:void(0)" class="modal-btn">确定</a>
            </div>
        </div>
    </div>
</div>
<!-- end modal -->

<!-- 信息确认弹窗 -->
<div id="info-confirm" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('info-confirm')"></a>
        <div class="modal-content">
            <h4>您确认要取消订单吗?</h4>
            <ul class="confirm-list"></ul>
        </div>
        <div class="btn-group both-btn">
            <a onclick="toggleModal('info-confirm')" id="infoEdit" class="confirm-btn">取消</a>
            <a onclick="cancelOrder(this)" id="infoConfirm" class="confirm-btn"
               otype="button" otitle="号段卡销售订单取消_">确定</a>
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
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>

<%--取消失败弹出框--%>


<%--<script type="text/javascript">--%>
<%--$('.tabs').tabslet();--%>
<%--</script>--%>
<script>
    var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
        + "wxyz0123456789+/" + "=";
    var pageNo = 0;
    var droploadConfig = {
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
            if($("#psptId").val() == ""){
                window.location.reload();
            }else{
                window.location.href = "${ctx}/myOrder/querySimOrderList?psptId="+$("#psptId").val();
            }
        },
        loadDownFn: function (me) {
            loadData(me);
        }
    };


    $(function () {

        var clientType = isAppClient();
        if(clientType=="html5"){
            $(".topbar").show();
        }else{
            $(".topbar").remove();
        }
        if( $("#psptId").val() != ''){
            $("#psptId").next().show();
        }

        var memberId = $("input[name='memberId']").val();
        var psptId = $("input[name='psptId']").val();
        var statusId = $("input[name='statusId']").val();
        $("#queryOrder").click(function () {
            //点击时刷新了页面
            $("#form1").submit();
        });
        $("#orderContent").dropload(droploadConfig);
    });


    function loadData(me) {
        //$('.orders-lists').html('');
        // 页数
        // 每页展示5个
        var pageSize = 10;
        var psptId = $("input[name='psptId']").val();
        if(psptId == undefined || psptId == ''){
            return;
        }
        var psptId = encode64($("input[name='psptId']").val());
        var statusId = $("input[name='statusId']").val();
        var memberId = $("input[name='memberId']").val();

        // 拼接HTML
        pageNo++;
        var contentHtml = '';
        $.ajax({
            type: 'GET',
            url: "${ctx}myOrder/querySimOrderData",//设置请求地址
            dataType: 'json',
            data: {
                psptId: psptId,
                statusId: statusId,
                pageNo: pageNo,
                pageSize: pageSize
            },
            success: function (data) {

                var arrLen = data.length;
                if (data.list != null && data.list.length > 0) {
                    pageNo = data.pageNo;
                    var contentHtml = "";
                    var reg = /套餐(\S*)元/;
                    $.each(data.list, function (i, val) {
                        var orderTime = new Date(val.orderTime);
                        var dateTime = orderTime.getFullYear() + "-" + (orderTime.getMonth() + 1) + "-" + orderTime.getDate();
                        /*var cardPrice = val.orderSubPayAmount / 100;
                         if (reg.test(val.detailSim.baseSetName)) {
                         cardPrice = val.detailSim.baseSetName.match(reg)[1];
                         }*/
                        if (val.orderTypeId == '2') {//实际号卡订单
                            var head = '<div class="orders-list"><div class="orders-list-top"><p>订单编号：<span>';
                            var body = '<div class="orders-list-body"><ul><li class="text-lblue">';
                            var orderTime = new Date(val.orderTime);
                            var dateTime = orderTime.getFullYear() + "-" + (orderTime.getMonth() + 1) + "-" + orderTime.getDate();
                            contentHtml += head;
                            contentHtml += val.orderSubNo;
                            contentHtml += '</span></p>';
                            contentHtml += '<span>' + dateTime + '</span></div>';
                            contentHtml += body;
                            contentHtml += '<p>' + val.detailSim.baseSetName + '</p> <span>' + val.orderStatus.orderStatusName + '</span></li>';
                            contentHtml += ' </ul></div><div class="orders-list-foot"><a href="${ctx}myOrder/querySimOrderDetail?orderSubNo=' + val.orderSubNo + '">订单详情</a></div></div>';

                        } else {//预约订单
                            var head = '<div class="orders-list"><div class="orders-list-top"><p>预约订单<span>';
                            var body = '<div class="orders-list-body"><ul><li class="text-lblue">';
                            var orderTime = new Date(val.orderTime);
                            var dateTime = orderTime.getFullYear() + "-" + (orderTime.getMonth() + 1) + "-" + orderTime.getDate();

                            contentHtml += head;
                            contentHtml += '</span></p>';
                            contentHtml += '<span>' + dateTime + '</span></div>';
                            contentHtml += body;
                            var overDue = new Date().getTime() - val.orderTime > 24 * 60 * 60 * 1000 ? 0 : 1;

                            // 未超时的create状态的订单
                            if (overDue == '1' && val.orderStatusId == '0') {
                                contentHtml += '<p>' + val.detailSim.baseSetName + '</p><span>未选号订单</span></li>';
                                contentHtml += ' </ul></div><div class="orders-list-foot"><a href="${ctx}simConfirm/toSelectPage?orderPreId=' + val.orderSubNo + '">去选号</a>' +
                                    '<a onclick="showCancelModel(this)" value="' + val.orderSubNo + '">取消订单</a>' +
                                    '</div></div>';
                            }
                            // else if (val.orderStatusId == '0') {
                            //     contentHtml += '<p>' + val.detailSim.baseSetName + '</p><span>订单已超时</span></li>';
                            //     contentHtml += '</ul></div><div class="orders-list-foot">' +
                            //         '<a onclick="showCancelModel(this)" value="' + val.orderSubNo + '">取消订单</a>' +
                            //         '</div></div>';
                            // }
                            else if (val.orderStatusId == '1') {
                                contentHtml += '<p>' + val.detailSim.baseSetName + '</p><span>订单已完成</span></li>';
                                contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                            } else if (val.orderStatusId == '3') {
                                contentHtml += '<p>' + val.detailSim.baseSetName + '</p><span>订单已取消</span></li>';
                                contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                            } else if (val.orderStatusId == '2') {
                                contentHtml += '<p>' + val.detailSim.baseSetName + '</p><span>订单失败</span></li>';
                                contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                            } else {
                                contentHtml += '<p>' + val.detailSim.baseSetName + '</p><span>订单超时</span></li>';
                                contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                            }
                        }

                    });
                    if (data.list.length <= 10 && pageNo == data.totalPage) {
                        me.lock();
                        me.noData();
                    }
                    if (data.pageNo == 1 && data.list.length == 0) {
                        contentHtml = '<div class="orders-search-error"><p>很抱歉，没有找到相关订单记录！</p></div>';

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
    function encode64(password) {
        return strEnc(password, keyStr);
    }
    function queryMakeCard() {
        var psptId = encode64($("input[name='psptId']").val());
        $.ajax({
            url: "${ctx}myOrder/queryMakeCardOrderData",
            type: "post",
            data: {psptId: psptId},
            dataType: "json",
            success: function (data) {
                if (data.data) {
                    var head = '<div class="orders-list"><div class="orders-list-top"><p>预约订单<span>';
                    var body = '<div class="orders-list-body"><ul><li class="text-lblue">';
                    var contentHtml = "";
                    $.each(data.data, function (i, val) {
                        var orderTime = new Date(val.createTime);
                        var dateTime = orderTime.getFullYear() + "-" + (orderTime.getMonth() + 1) + "-" + orderTime.getDate();

                        contentHtml += head;
                        if (val.orderSubNo != undefined && val.orderSubNo != '') {
                            contentHtml += "编号：" + val.orderSubNo;
                        }
                        contentHtml += '</span></p>';
                        contentHtml += '<span>' + dateTime + '</span></div>';
                        contentHtml += body;

                        // 未超时的create状态的订单
                        if (val.overDue == '1' && (val.orderSubNo == undefined || val.orderSubNo == '') && val.status == '0') {
                            contentHtml += '<p>' + val.planName + '</p><span>未选号订单</span></li>';
                            contentHtml += ' </ul></div><div class="orders-list-foot"><a href="${ctx}simConfirm/toSelectPage?orderPreId=' + val.orderPreId + '">去选号</a>' +
                                '<a onclick="showCancelModel(this)" value="' + val.orderPreId + '">取消订单</a>' +
                                '</div></div>';
                        } else if (val.status == '0') {
                            contentHtml += '<p>' + val.planName + '</p><span>订单已超时</span></li>';
                            contentHtml += '</ul></div><div class="orders-list-foot">' +
                                '<a onclick="showCancelModel(this)" value="' + val.orderPreId + '">取消订单</a>' +
                                '</div></div>';
                        } else if (val.status == '1') {
                            contentHtml += '<p>' + val.planName + '</p><span>订单已完成</span></li>';
                            contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                        } else if (val.status == '3') {
                            contentHtml += '<p>' + val.planName + '</p><span>订单已取消</span></li>';
                            contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                        } else if (val.status == '2') {
                            contentHtml += '<p>' + val.planName + '</p><span>订单失败</span></li>';
                            contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                        } else {
                            contentHtml += '<p>' + val.planName + '</p><span>订单超时</span></li>';
                            contentHtml += '</ul></div><div class="orders-list-foot"></div></div>';
                        }
                    });
                    $('#orderPre').before(contentHtml);
                }
            }

        });
    }

    function showCancelModel(obj) {
        var orderPreId = $(obj).attr("value");
        $("#infoConfirm").attr("otitle", "号段卡销售订单取消_" + orderPreId);
        toggleModal('info-confirm');
    }

    function cancelOrder(obj) {
        var otitle = $(obj).attr("otitle");
        var orderPreId = otitle.substring(otitle.indexOf('_') + 1);
        $.ajax({
            data: {"orderPreId": orderPreId},
            url: ${ctx}+"simConfirm/deleteOrder",
            dataType: "json",
            success: function (data) {
                if (data.code == '0') {
                    window.location.reload();
                    dcsPageTrack("WT.si_n", "号段卡订单取消" + data.message, 0, "WT.si_x", "预订单下单成功", 0);//插码使用
                } else {
                    toggleModal('sorry-modal', data.message);
                }
            },
            error: function () {
                dcsPageTrack("WT.si_n", "号段卡订单取消失败", 0, "WT.si_x", "预订单下单失败", 0);//插码使用
            }
        })
    }

    function psptKey(a){
        var value = $("#psptId").val();
        if( value != ""){
            $(a).next().show();
        }else{
            $(a).next().hide();
        }
    }


    $(".clear-index").click(function(){
        $(this).prev().val("");//清空表单内容
        $(this).hide();
    });
</script>
</body>

</html>