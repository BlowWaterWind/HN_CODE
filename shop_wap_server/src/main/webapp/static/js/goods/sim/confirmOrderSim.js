$(function () {
    var memberAddressId = $(this).find("input[name='memberAddress.memberAddressId']").val();
    //跳转到选择收货地址页面
    $("#chooseReceiptAddressBtn").live("click", function () {
        if (!memberAddressId) {
            memberAddressId = "";
        }
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToChooseReceiptAddress?memberAddressId=" + memberAddressId);
        $("#confirmOrderFormSim").submit();
    });

    //跳转到选择发票页面
    $("#chooseInvoiceBtn").live("click", function () {
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToChooseInvoice");
        $("#confirmOrderFormSim").submit();
    });

    //跳转到查询套餐信息页面
    $("#queryContractBtn").live("click", function () {
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToContractInfo");
        $("#confirmOrderFormSim").submit();
    });

    //跳转到选择配送方式页面
    $("#chooseDeliveryModeBtn").live("click", function () {
        if ($("span[name='shopName']").length > 1) {
            showAlert("多个店铺商品提交订单只能选择物流配送");
            return;
        }
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToChooseDeliveryMode");
        $("#confirmOrderFormSim").submit();
    });

    //跳转到选择收货时间页面
    $("#chooseReceiptTimeBtn").live("click", function () {
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToChooseReceiptTime");
        $("#confirmOrderFormSim").submit();
    });

    //填写入网信息
    $("#networkInfoBtn").live("click", function () {
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToNetworkInfo");
        $("#confirmOrderFormSim").submit();
    });

    //跳转到选择优惠券页面
    $("#chooseCouponBtn").live("click", function () {
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToChooseCoupon");
        $("#confirmOrderFormSim").submit();
    });

    //跳转到填写推荐人页面
    $("#recommenderBtn").live("click", function () {
        $("#confirmOrderFormSim").attr("action", ctx + "goodsBuy/linkToRecommender");
        $("#confirmOrderFormSim").submit();
    });

    //点击已选择区县
    $(".yt-tit").click(function () {
        $(".yt-list").toggle();
    });

    //== 物流配送/到厅在自提 dom变化
    var deliveryModeId = $('#chooseDeliveryModeBtn input[name="deliveryMode.deliveryModeId"]').val();
    var $payModeId = $('#choosePayModeBtn input[name="payMode.payModeId"]');
    var $payModeName = $('#choosePayModeBtn').children('.gm-zj02');
    // 物流配送
    if(deliveryModeId === '1'){
        $payModeId.val('2');
        $payModeName.html('在线支付');
    }
    // //到厅自提
    // else if(deliveryModeId === '2') {
    //     $payModeId.val('3');
    //     $payModeName.html('到厅支付');
    // }

    //选择区县营业厅地址
    $("#orgList li").live("click", function () {
        var $org = $(this);
        var orgId = $org.attr("orgId");
        var orgName = $org.attr("orgName");
        var eparchCode = $("#cityCode").val();

        var $selectedOrg = $("#selectedOrg");
        $selectedOrg.attr("orgId", orgId);
        $selectedOrg.text(orgName);

        $("#orgList").hide();

        var url = ctx + "goodsBuy/queryOrgBusinessHallList";
        var param = {"orgId": orgId, "eparchCode": eparchCode};
        $.post(url, param, function (data) {
            $("#hallList  tr:not(:first)").html("");

            $.each(data, function (i, hall) {
                $("<tr>\n" +
                    "  <td>" + hall.orgName + "</td>\n" +
                    "  <td>" + hall.hallName + "</td>\n" +
                    "  <td>" + hall.hallAddress + "</td>\n" +
                    "</tr>").appendTo($("#hallList"));

            });
        });
    });

    //确认订单
    $("#confirmBtnSim").live("click", function () {
        if (deliveryModeId == undefined || deliveryModeId.length == 0) {
            showAlert("请选择配送方式");
            return;
        }
        if(deliveryModeId === '1' && (memberAddressId == null ||memberAddressId.length == 0)){
            showAlert("请选择配送地址");
            return;
        }
        if ($("input[name='orderDetailSim.regName']").val() == 0) {
            showAlert("请填写入网信息");
            return;
        }
        if(!$("#confirmCB").is(":checked")){
            showAlert("请确保阅读《湖南移动网上商城购前须知协议》");
            return;
        }
        showLoadPop("正在提交...")
        $("#confirmOrderFormSim").submit();
        // $.ajax({
        //     url: ctx + "/goodsBuy/realityVerify",
        //     type: "post",
        //     data: $("#confirmOrderFormSim").serializeObject(),
        //     dataType: "json",
        //     success: function (data) {
        //         if (data.resultCode != "success") {
        //             showAlert('身份证信息不一致，请重新填写');
        //             return;
        //         }
        //         showLoadPop("正在提交...")
        //         $("#confirmOrderFormSimSim").submit();
        //     }
        // });
    });

    $("#confirmModal").click(function () {
        if ($("#myModal").is(":visible")) {
            $("#myModal").hide();
        } else {
            $("#myModal").show();
        }
    });

    $("#comfirm").click(function () {
        $("#myModal").hide();
        $("#confirmCB").attr("checked", true);
    });

    $("#closeForm").click(function () {
        $("#myModal").hide();
        $("#confirmCB").attr("checked", true);
    });

});

/**
 * 将表单元素序列化为JSON对象
 * 基于jQuery serializeArray()
 */
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
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