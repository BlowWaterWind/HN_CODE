//提示弹框
function toggleModal(id, prompt1, prompt2) {
    var thisId = "#" + id;
    if ($(thisId)) {
        if (prompt1 == null || prompt1.length == 0)
            prompt1 = prompt2;
            $(thisId + " ._pomptTxt").html(prompt1);
        $("#" + id).toggle();
    }
}



$(function () {
    //关闭确认弹窗，适用于所有弹窗
    $(".confirm-btn").on("click", function () {
        $(this).closest(".mask-layer").hide();
    });
    //确定信息 提交订单
    $("#updatePackage").on("click", function () {
        $.ajax({
            url: ctx+"simUp/toUpgradePackage",
            type: "post",
            data:"",
            dataType: "json",
            success: function (data) {
                console.log(data);
                $("#loding-modal").hide();
                if (data.code === "0") {
                    toggleModal("tips-modal-3");
                }else{
                    if(data.message === "-1") {
                        toggleModal("tips-modal-1");
                    }else if (data.message === "-2") {
                            toggleModal("tips-modal-2");
                        }else if (data.message === "-4") {
                            toggleModal("tips-modal-6");
                        }else{
                        toggleModal("tips-modal-5");
                    }

                }
            },
            error: function () {
                console.log("....");
            }
        });
    });
    $("#sureApply3").on("click",function () {
        toggleModal("loding-modal", "", "正在提交...");//打开加载框
        $.ajax({
            url: ctx+"simUp/confirmUpgradePackage",
            type: "post",
            data:"",
            dataType: "json",
            success: function (data) {
                console.log(data);
                $("#loding-modal").hide();
                if (data.code === "0") {
                    $("#tips-modal-3").closest(".mask-layer").hide();
                    toggleModal("tips-modal-4");
                }else {
                    $("#tips-modal-3").closest(".mask-layer").hide();
                    if(data.message === "-5") {
                        toggleModal("tips-modal-7");
                    }else{
                        toggleModal("tips-modal-5");
                    }

                }
            },
            error: function () {
                $("#tips-modal-3").closest(".mask-layer").hide();
                toggleModal("tips-modal-5");
            }
        });
    });

});