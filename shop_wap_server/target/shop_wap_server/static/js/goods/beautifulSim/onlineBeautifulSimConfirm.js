//提示弹框
/*function toggleModal(id, prompt1, prompt2) {
 var thisId = "#" + id;
 if ($(thisId)) {
 $("#" + id).toggle();
 $(".mask-box").toggle();
 }
 }*/
var psptVerify = true;
$("#regName").on("blur", function () {
    var psptId = $.trim($("#psptId").val());
    var regName = $.trim($("#regName").val());
    if (isContainSpace(regName)) {
        toggleModalForBeauty("sorry-modal", "姓名中不能含有空格！");
        return;
    }
    if (psptId.length != 0) {
        psptId = encode64(psptId);
        $.ajax({
            url: ctx + "/goodsBuy/realityVerifyV2",
            type: "post",
            data: {
                "orderDetailSim.psptId": $("#psptId").val(),
                "orderDetailSim.regName": $("#regName").val()
            },
            dataType: "json",
            success: function (data) {
                if (data.resultCode == "fail") {
                    toggleModal("sorry-modal", data.resultDesc, "实名制或一证五号校验未通过");
                    psptVerify = false;
                    return;
                }
                psptVerify = true;
            },
            error: function (data) {
                toggleModal("sorry-modal", data.resultDesc, "实名制或一证五号校验未通过");
                psptVerify = false;
            }
        });
    }
});

/*点击mask的事件*/
$(".mask-box").click(function(){
    $(".zffs-fixed").slideUp('fast');
    $(".mask-box").toggle();
});

//== 身份证号码 异步校验
$("#psptId").on("blur", function () {
    var psptId = $.trim($("#psptId").val());
    if (psptId.length == 0) {
        toggleModalForBeauty("sorry-modal", "请填写身份证号");
        return;
    }
    $.ajax({
        url: ctx + "/goodsBuy/realityVerifyV2",
        type: "post",
        data: {
            "orderDetailSim.psptId": $("#psptId").val(),
            "orderDetailSim.regName": $("#regName").val()
        },
        dataType: "json",
        success: function (data) {
            if (data.resultCode == "fail") {
                toggleModalForBeauty("sorry-modal", data.resultDesc);
                psptVerify = false;
                return;
            }
            psptVerify = true;
        },
        error: function (data) {
            toggleModal("sorry-modal", data.resultDesc, "实名制或一证五号校验未通过");
            psptVerify = false;
        }
    });
});

function verifySubmit() {
    if ($("#regName").val().length == 0) {
        toggleModalForBeauty("sorry-modal", "请输入姓名");
        return;
    }
    if ($("#psptId").val().length == 0) {
        toggleModalForBeauty("sorry-modal", "请输入身份证号码");
        return;
    }
    if (!psptVerify) {
        toggleModalForBeauty("sorry-modal", "实名制或一证五号校验未通过");
        return;
    }
    //== 联系电话校验
    var $phone = $("#contactPhone").val();
    var regPhone = /^1[34578]\d{9}$/;
    if ($phone.length == 0) {
        toggleModalForBeauty("sorry-modal", "请输入电话号码");
        return;
    }
    if (!regPhone.test($phone)) {
        toggleModalForBeauty("sorry-modal", "联系电话不正确");
        return;
    }
    if ($("#cityCode").val().length == 0) {
        toggleModalForBeauty("sorry-modal", "请选择号码归属地");
        return;
    }
    if ($("#phone").val().length == 0) {
        toggleModalForBeauty("sorry-modal", "请选择号码");
        return;
    }
    if ($("#picker").text() == "请选择区/县") {
        toggleModalForBeauty("sorry-modal", "请选择区/县");
        return;
    }
    //== 地址校验
    var cicns = $("#picker").text().split(" ");
    $("#memberRecipientCity").val(cicns[0]);
    $("#memberRecipientCounty").val(cicns[1]);
    $("#linkAddress").val("湖南省" + $("#picker").text());
    if ($("#memberRecipientAddress").val().length == 0) {
        toggleModalForBeauty("sorry-modal", "请输入详细地址");
        return;
    }
    var regDetailAddress = /^[0-9A-Za-z\u4e00-\u9fa5]*$/;
    if (!regDetailAddress.test($("#memberRecipientAddress").val())) {
        toggleModalForBeauty("sorry-modal", "请输入合法的地址，收货地址只能含有中文/字母/数字/非全角字符");
        return;
    }
    if (/^[0-9]+$/.test($("#memberRecipientAddress").val())) {
        toggleModalForBeauty("sorry-modal", "请输入合法的地址，收货地址不能是纯数字");
        return;
    }
    if ($("#memberRecipientAddress").val().length <= 5) {
        toggleModalForBeauty("sorry-modal", "请输入合法的地址，收货地址必须大于5个字符");
        return;
    }
    if (!$("#ckbox01").is(":checked")) {
        toggleModalForBeauty("sorry-modal", "请认真阅读客户入网服务协议！");
        return;
    }
    if (!$("#ckbox02").is(":checked")) {
        toggleModalForBeauty("sorry-modal", "请认真阅读优选号码服务协议！");
        return;
    }
    var sum = Number($("#preFee").val()) + Number($("#cardFee").val());
    $(".zffs-total span").html(sum);

    $(".zffs-fixed").slideDown('fast');
    $(".mask-box").addClass("on");
    $(".mask-box").toggle();
}

function submitOrderAndPay(){
    $("input[name='payPlatform']").val("WAPIPOS_SHIPOS");
    $("#submitOrderToPay").serializeObject();
    $("#submitOrderToPay").submit();
}




