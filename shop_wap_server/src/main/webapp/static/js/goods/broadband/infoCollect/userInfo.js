$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/*用户信息添加校验*/
$(function () {
    $("form input").blur(function () {
        if ($(this).is('#userName')) {
            $("#userNameError").hide();
            if (this.value == "") {
                $("#userNameError").show();
            }
        }
        //验证用户号码
        if ($(this).is('#serialNumber')) {
            $("#serialNumberError").hide();
            if (this.value == "" || !(/^1[3|4|5|7|8]\d{9}$/.test(this.value))) {
                $("#serialNumberError").show()
            }
        }
        //验证身份证号码
        if ($(this).is('#cardId')) {
            $("#cardIdError").hide();
            if (this.value == "" || !(/\d{18}/).test(this.value)) {
                $("#cardIdError").show()
            }
        }
        //验证宽带价格
        if ($(this).is('#broadbandPrice')) {
            $("#broadbandPriceError").hide();
            if (this.value == "") {
                $("#broadbandPriceError").show()
            }
        }
    }).keyup(function () {
        $(this).triggerHandler("blur");
    }).focus(function () {
        $(this).triggerHandler("blur");
    });



    $("#userInfoBtn").click(function () {
        $("#clerkAdd").attr("disabled", true);
        $("form :input ").trigger('blur');
        var flag = 0;
        $("div[id$=Error]").each(function(i,index){
            if($(this).css("display")=="block"){
                flag =1;
                return;
            }
        });
        if(flag == 0){
            var params = $("#addUserInfo").serializeObject();
            var communityId = $("input[name='communityId']").val();
            var synchronizedType = $("#synchronizedType").val();
            showLoadPop();
            $.ajax({
                data: params,
                url:"addUserInfo",
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.code == '0') {
                        hideLoadPop();
                        showAlert(data.message, function () {
                            //跳转到列表页面;同步类型 0电渠侧录入 1CRM同步
                            if(synchronizedType == '0'){
                                window.location.href = ctx+"/bandInfoCollect/initAdd?communityId="+communityId+"&infoType=2&tab=tab3";
                            }else{
                                window.location.href = ctx+"/bandInfoCollect/initAddPartnerOrUser?communityId="+communityId+"&type=1&tab=tab3";
                            }
                        });
                    } else {
                        hideLoadPop();
                        showAlert(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    hideLoadPop();
                    showAlert("添加用户信息失败!");
                }
            });
        }
    });
});