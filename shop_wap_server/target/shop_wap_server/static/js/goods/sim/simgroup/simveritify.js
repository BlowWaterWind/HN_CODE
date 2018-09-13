//增加对身份证号码和宽带密码的加密
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
    + "wxyz0123456789+/" + "=";
function encode64(str) {
    return strEnc(str, keyStr);
}
//== 身份证号码 异步校验
$("#psptId").on("blur", function () {
    var psptId = $.trim($("#psptId").val());
    if (psptId.length == 0) {
        toggleModal("sorry-modal", "请填写身份证号");
        return;
    }
    if(psptId.length <18){
        return;
    }
    psptId = encode64(psptId);
    $.ajax({
        url: ctx + "/goodsBuy/realityVerifyV2",
        type: "post",
        data: {
            "orderDetailSim.psptId": psptId,
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
});
$("#regName").on("blur", function () {
    var psptId = $.trim($("#psptId").val());
    var regName = $.trim($("#regName").val());
    if (isContainSpace(regName)) {
        toggleModal("sorry-modal", "姓名中不能含有空格！");
        return;
    }
    if (psptId.length != 0) {
        psptId = encode64(psptId);
        $.ajax({
            url: ctx + "/goodsBuy/realityVerifyV2",
            type: "post",
            data: {
                "orderDetailSim.psptId": psptId,
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