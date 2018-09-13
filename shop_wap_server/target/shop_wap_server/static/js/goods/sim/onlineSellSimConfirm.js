//增加对身份证号码和宽带密码的加密
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
    + "wxyz0123456789+/" + "=";
function encode64(str) {
    return strEnc(str, keyStr);
}



//提示弹框
function toggleModal(id, prompt1, prompt2) {
    var thisId = "#" + id;
    if ($(thisId)) {
        if (prompt1 == null || prompt1.length == 0)
            prompt1 = prompt2;
        if (typeFlag === 'E0DD' && prompt1 != null && prompt1.indexOf("，") > 0) {
            var strs = new Array();
            strs = prompt1.split("，"); //字符分割
            $(thisId + " ._pomptTxt").html('<p>' + strs[0] + '</p><p>' + strs[1] + '</p>');
        } else {
            $(thisId + " ._pomptTxt").html(prompt1);
        }
        $("#" + id).toggle();
    }
}

//联动地址
var cityCnty = [];
var picker;
function creatList(obj, list) {
    obj.forEach(function (item, index, arr) {
        var temp = new Object();
        temp.text = item.orgName;
        temp.value = index;
        list.push(temp);
    });
}

//查询号码，可以尝试整合 TODO
function setSearchNum(cityCode, number) {
    if (typeFlag === 'E0DD') {
        searchNumDidi(cityCode, number);
    } else {
        searchNumCommon(cityCode, number);
    }
}

function searchNumCommon(cityCode, number) {
    $("#select-number ul").empty();
    var loading = '<div class="m-load2" ><div class="line">' +
        '<div></div> <div></div><div></div><div></div><div></div><div></div>' +
        '</div><div class="circlebg"></div></div>';
    $("#select-number ul").append(loading);
    $.ajax({
        type: "POST",
        url: ctx + "simBuy/getQueryNumsH5Online",
        dataType: "json",
        data: {
            cityCode: cityCode,
            number: number,
            confId: $("#confId").val()//查询字符串
        },
        success: function (data1) {
            $("#select-number ul").empty();
            if (data1.code != 0 || (data1.code === '0' && data1.data == null)) {
                $("#select-number-ul").append("<p class='text-center'>很抱歉，暂无符合要求的号码！</p>");
                return;
            }
            var html = ""
            var j = data1.data.length > 10 ? 10 : data1.data.length;
            for (var i = 0; i < j; i++) {
                html += "<li>" + data1.data[i].num + "</li>"
            }
            $("#select-number ul").html(html);
        },
        error: function (data1) {
            $("#select-number ul").empty();
            $("#select-number-ul").append("<p class='text-center'>很抱歉，暂无符合要求的号码！</p>");
        }
    });
}

function searchNumDidi(cityCode, number) {
    $(".search-list").empty();
    var loading = '<div class="m-load2" ><div class="line">' +
        '<div></div><div></div><div></div><div></div><div></div><div></div>' +
        '</div><div class="circlebg"></div></div>';
    $(".search-list").append(loading);
    $.ajax({
        type: "POST",
        url: ctx + "simBuy/getQueryNumsH5Online",
        dataType: "json",
        data: {
            cityCode: cityCode,
            number: number,//查询字符串
            confId: $("#confId").val()
        },
        success: function (data1) {
            $(".search-list").empty();
            if (data1.code != '0' || (data1.code === '0' && data1.data == null)) {
                $(".search-list").append('<div class="search-sorry"><p>抱歉</br>暂无匹配号码</p></div>');
                return;
            }
            var html = '<ul id="select-number-ul">';
            var charCount = number.length;
            var j = data1.data.length > 10 ? 10 : data1.data.length;
            for (var i = 0; i < j; i++) {
                html += "<li>" + data1.data[i].num.substring(0, 11 - charCount)
                    + "<span>" + data1.data[i].num.substring(11 - charCount, 11) + "</span></li>"
            }
            html += '</ul>'
            $(".search-list").html(html);
        },
        error: function (data1) {
            $(".search-list").empty();
            $(".search-list").html('<div class="search-sorry"><p>抱歉</br>暂无匹配号码</p></div>');
        }
    });
    var searchVal = $("#number").val();
    if (searchVal.length > 0) {
        $("#numberBtn").hide();
        $("#numberBtnClean").show();
    }
}

$(function () {
    //关闭确认弹窗，适用于所有弹窗
    $(".confirm-btn").on("click", function () {
        $(this).closest(".mask-layer").hide();
    });
    $("#number").on("keyup", function () {
        $("#numberBtn").show();
        $("#numberBtnClean").hide();
    });
    $("#numberBtnClean").on("click", function () {
        $("#numberBtn").show();
        $("#number").val('');
        $("#numberBtnClean").hide();
    });
    $("#protocol").on("click", function () {
        if ($("#protocol").is(":checked")) {
            $("#verifySubmit").removeAttr("disabled");
        } else {
            $("#verifySubmit").attr("disabled", "disabled");
        }

    });
    if (typeFlag === 'E0DD') {
        $('#picker1').text("长沙市");
        $("#cityCode").val("0731");
    } else {
        //号码归属地市
        $.ajax({
            type: "POST",
            url: ctx + "simBuy/getSimAddress",
            dataType: "json",
            success: function (data) {
                var picker1El = $('#picker1');
                var cityCode = $("#cityCode");
                console.log(data);
                var picker1 = new Picker({data: [data]});
                picker1.on('picker.select', function (selectedVal, selectedIndex) {
                    var slctedCityCode = $("#cityCode").val();
                    if (slctedCityCode != data[selectedIndex[0]].value) {
                        $("#phone").val('');
                        $('#searchNumber p').html('请重新选择号码');
                    }
                    picker1El.html(data[selectedIndex[0]].text);
                    $("#cityName").val(data[selectedIndex[0]].text);
                    cityCode.val(data[selectedIndex[0]].value);
                });
                picker1El.on("click", function () {
                    picker1.show();
                });
            }
        });
    }
    //查询号码
    $("#searchNumber").on("click", function () {
        var number = $("#number").val();
        var cityCode = $('#cityCode').val(); //地市编码
        if (cityCode == null || cityCode == "") {
            toggleModal("sorry-modal", "请选择号码归属地！");
            return;
        }
        toggleModal('select-number');
        setSearchNum(cityCode, number);
    });
    //换一批/搜索
    $("#cagBatch,#numberBtn").on("click", function () {
        var cityCode = $('#cityCode').val(); //地市编码
        var number = $("#number").val();
        if (cityCode == null || cityCode == "") {
            toggleModal("sorry-modal", "请选择号码归属地！");
            return;
        }
        setSearchNum(cityCode, number);
    });
    $(".search-list").on("click", "li", function () {
        $("#phone").val($(this).text());
        $("#select-number").hide();
        $("#searchNumber p").html($(this).text());
        $("#searchNumberO2O").html($(this).text());
    });
    //联动地址第一次加载数据
    $.ajax({
        type: "POST",
        url: ctx + "simBuy/getSimCityCnty",
        dataType: "json",
        success: function (data) {
            console.log(data);
            cityCnty = data;
            var nameEl = document.getElementById('picker2');
            var first = [];
            var second = [];
            //默认选中地区
            var slctCityInd = 0, slctCntyInd = 0;
            if (slctCity.length > 0) {
                for (ind = 0; ; ind++) {
                    if (data[ind].orgName == slctCity) {
                        slctCityInd = ind;
                        for (ind2 = 0; ; ind2++) {
                            if (data[ind].tdEcOrgInfos[ind2].orgName == slctCnty) {
                                slctCntyInd = ind2;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            var selectedIndex = [slctCityInd, slctCntyInd];
            /* 默认选中的地区 */
            var checked = [0, 0];
            /* 已选选项 */
            creatList(data, first);
            if (data[selectedIndex[0]].hasOwnProperty('tdEcOrgInfos')) {
                creatList(data[selectedIndex[0]].tdEcOrgInfos, second);
            } else {
                second = [{
                    text: '',
                    value: 0
                }];
            }
            picker = new Picker({
                data: [first, second],
                selectedIndex: selectedIndex
            });
            picker.on('picker.select', function (selectedVal, selectedIndex) {
                var text1 = first[selectedIndex[0]].text;
                var text2 = second[selectedIndex[1]].text;
                nameEl.innerText = text1 + ' ' + text2 + ' ';
            });
            picker.on('picker.change', function (index, selectedIndex) {
                if (index === 0) {
                    firstChange();
                }
                function firstChange() {
                    second = [];
                    checked[0] = selectedIndex;
                    var firstCity = data[selectedIndex];
                    if (firstCity.hasOwnProperty('tdEcOrgInfos')) {
                        creatList(firstCity.tdEcOrgInfos, second);
                        var secondCity = data[selectedIndex].tdEcOrgInfos[0]
                        if (secondCity.hasOwnProperty('tdEcOrgInfos')) {
                            creatList(secondCity.tdEcOrgInfos);
                        }
                    } else {
                        second = [{
                            text: '',
                            value: 0
                        }];
                        checked[1] = 0;
                    }
                    picker.refillColumn(1, second);
                }
            });
            //picker.show();
        }
    });
    //联动地市
    $("#picker2").on("click", function () {
        if (cityCnty.length == 0) {
            //TODO
        } else {
            picker.show();
        }
    });

    if($("#transactionId").val().length>0){
        $("#picker1").unbind();
        $("#searchNumber").unbind();
        $("#yySubmit").unbind();
    }

    //== 身份证号码 异步校验
    /*$("#psptId").on("blur", function () {
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
    });*/
    /*$("#regName").on("blur", function () {
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
    });*/
    //编辑
    $("#infoEdit").click(function(){
        try {
            var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
            var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                Webtrends.multiTrack({
                    argsa: ["WT.si_n",wtc,
                        "WT.si_s",wts,
                        "WT.si_x","-22"],
                    delayTime: 100
                })

        }catch(e){
            console.log(e)
        }
    });
    //提交校验 显示信息
    $("#verifySubmit").on("click", function () {

        try {
            var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
            var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                Webtrends.multiTrack({
                    argsa: ["WT.si_n",wtc,
                        "WT.si_s",wts,
                        "WT.si_x","21"],
                    delayTime: 100
                })
        }catch(e){
            console.log(e)

        }
        /*
        if ($("#regName").val().length == 0) {
            toggleModal("sorry-modal", "请输入姓名");
            return;
        }
        if ($("#psptId").val().length == 0) {
            toggleModal("sorry-modal", "请输入身份证号码");
            return;
        }
        if (!psptVerify) {
            toggleModal("sorry-modal", "实名制或一证五号校验未通过");
            return;
        }*/
        if ($("#transactionId").val().length == 0) {
            toggleModal("sorry-modal", "请先进行身份验证");
            return;
        }
        //== 联系电话校验
        var $phone = $("#contactPhone").val();
        var regPhone = /^1[345789]\d{9}$/;
        if ($phone.length == 0) {
            toggleModal("sorry-modal", "请输入电话号码");
            return;
        }
        if (!regPhone.test($phone)) {
            toggleModal("sorry-modal", "联系电话不正确");
            return;
        }
        /*if ($("#cityCode").val().length == 0) {
            toggleModal("sorry-modal", "请选择号码归属地");
            return;
        }
        if ($("#phone").val().length == 0) {
            toggleModal("sorry-modal", "请选择号码");
            return;
        }*/
        if ($("#picker2").text() == "请选择区/县") {
            toggleModal("sorry-modal", "请选择区/县");
            return;
        }
        //== 地址校验
        var cicns = $("#picker2").text().split(" ")
        $("#memberRecipientCity").val(cicns[0]);
        $("#memberRecipientCounty").val(cicns[1]);
        $("#linkAddress").val("湖南省" + $("#picker2").text());
        if ($("#memberRecipientAddress").val().length == 0) {
            toggleModal("sorry-modal", "请输入详细地址");
            return;
        }
        var regDetailAddress = /^[0-9A-Za-z\u4e00-\u9fa5]*$/;
        if (!regDetailAddress.test($("#memberRecipientAddress").val())) {
            toggleModal("sorry-modal", "请输入合法的地址，收货地址只能含有中文/字母/数字/非全角字符");
            return;
        }
        if (/^[0-9]+$/.test($("#memberRecipientAddress").val())) {
            toggleModal("sorry-modal", "请输入合法的地址，收货地址不能是纯数字");
            return;
        }
        if ($("#memberRecipientAddress").val().length <= 5) {
            toggleModal("sorry-modal", "请输入合法的地址，收货地址必须大于5个字符");
            return;
        }
        if (!$("#protocol").is(":checked")) {
            toggleModal("sorry-modal", "请认真阅读服务协议！");
            return;
        }
        $(".confirm-list").empty();
        if (typeFlag == 'E0DD') {


            var confirmInfo = "<li>姓名：" + $("#regName").text() + "</li>" +
                // "<li>身份证：" + $("#psptId").val() + "</li>" +
                "<li>联系电话：" + $("#contactPhone").val() + "</li>" +
                "<li>所选号码：" + $("#phone").val() + "</li>" +
                "<li>号码归属：" + $('#picker1').text() + "</li>" +
                "<li>配送地址：" + $("#linkAddress").val() + $("#memberRecipientAddress").val() + "</li>";
        } else {
            var confirmInfo = "<li>尊敬的" + $("#regName").text() + "(先生/女士)：</li>" +
                // "<li>身份证号码：" + $("#psptId").val() + "</li>" +
                "<li>联系方式：" + $("#contactPhone").val() + "</li>" +
                "<li>将办理湖南省" + $('#picker1').text() + $("#phone").val() + "(号卡)</li>" +
                "<li>配送至：湖南省" + $("#linkAddress").val() + $("#memberRecipientAddress").val() + "</li>";
        }
        var sum = Number($("#preFee").val()) + Number($("#cardFee").val());
        if (sum > 0) {
            confirmInfo += "<li>支付金额:" + sum + "元</li>";
        }
        $(".confirm-list").append(confirmInfo);
        toggleModal("info-confirm");

    });

    //确定信息 提交订单
    $("#infoConfirm").on("click", function () {

        try {
            var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
            var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                Webtrends.multiTrack({
                    argsa: ["WT.si_n",wtc,
                        "WT.si_s",wts,
                        "WT.si_x","22"],
                    delayTime: 100
                })


        }catch(e){
            console.log(e)

        }
        $("#info-confirm").hide();//关闭信息框
        if (typeFlag == 'E0DD') {
            toggleModal("loding-modal", "", "加载中...");//打开加载框
        } else {
            toggleModal("loding-modal", "", "正在提交...");//打开加载框
        }
        // var psptIdEncode = encode64($("#psptId").val());
        // $("#psptId").val(psptIdEncode);这样在页面上展示加密后的身份证号码
        // $("input[name='orderDetailSim.psptId']").val(psptIdEncode);
        $.ajax({
            url: ctx + "/simBuy/simH5OnlineSubmitOrder",
            type: "post",
            data: $("#confirmOrderFormSim").serializeObject(),
            dataType: "json",
            success: function (data) {
                console.log(data);
                $("#loding-modal").hide();
                if (data.code === "0") {
                   // dcsPageTrack("WT.si_n", "H5号卡销售_" + cmConfId, 0, "WT.si_x", "申请成功", 0);//插码使用

                    try {
                            var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                            var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                            Webtrends.multiTrack({
                                argsa: ["WT.si_n",wtc,
                                    "WT.si_n",wts,
                                    "WT.si_x","99"],
                                delayTime: 100
                            })


                    }catch(e){
                        console.log(e)

                    }


                    if(data.data.type == 'toPay'){
                        window.location.href = ctx + "/simpay/toPay?orderNo=" + data.data.orderNo + "&confId=" + data.data.confId + "&planId=" + data.data.planId + "&selectPhone=" +data.data.selectPhone ;
                    }
                    //滴滴跳转结果页面，而不是弹框
                    else if (typeFlag == 'E0DD') {
                        window.location.href = ctx + "/simBuy/toDidiSuccess?orderNo=" + data.data.orderNo + "&confId=" + data.data.confId + "&feature_str=" + data.data.feature_str;
                    } else if (cardType == '3') {//大王卡跳转页面，而不是弹框
                        window.location.href = ctx + "/simBuy/toKingCardSuccess?confId=" + data.data.confId + "&recmdCode=" + data.data.recmdCode + "&cityCodeSuffix=" + data.data.cityCodeSuffix;
                    } else {
                        toggleModal("orders-success");//弹出结果框
                    }
                    dcsPageTrack("WT.si_n", "H5号卡销售_" + cmConfId, 0, "WT.si_x", "申请成功", 0);//插码使用,防止插码关闭js报错导致后面的不能执行
                } else {
                    if (data == '页面过期，请刷新页面') {
                        toggleModal("sorry-modal", "", data, "订单提交失败");
                    } else {
                        toggleModal("sorry-modal", "", data.message, "订单提交失败");
                    }
                    //dcsPageTrack("WT.si_n", "H5号卡销售_" + cmConfId, 0, "WT.si_x", "申请失败", 0, "WT.err_code ", data.message, 0)

                    try {
                            var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                            var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                            Webtrends.multiTrack({
                                argsa: ["WT.si_n",wtc,
                                    "WT.si_s",wts,
                                    "WT.si_x","-99"],
                                delayTime: 100
                            })

                    }catch(e){
                        console.log(e)

                    }
                }
            },
            error: function () {
                $("#loding-modal").hide();
                toggleModal("sorry-modal", "", "订单提交失败");
                //dcsPageTrack("WT.si_n", "H5号卡销售_" + cmConfId, 0, "WT.si_x", "申请失败", 0, "WT.err_code ", "申请异常", 0)
            }
        });
    });
});

$("#yySubmit").on("click", function () {
    if ($("#cityCode").val().length == 0) {
        toggleModal("sorry-modal", "请选择号码归属地");
        return;
    }
    if ($("#phone").val().length == 0) {
        toggleModal("sorry-modal", "请选择号码");
        return;
    }

    if (typeFlag == 'E0DD') {
        toggleModal("loding-modal", "", "加载中...");//打开加载框
    } else {
        toggleModal("loding-modal", "", "正在提交...");//打开加载框
    }

    $.ajax({
        url: ctx + "simBuy/payAppointment",
        type: "post",
        data: $("#confirmOrderFormSim").serializeObject(),
        dataType: "json",
        success: function (data) {
            $("#loding-modal").hide();
            if (data.code === "0") {
                // dcsPageTrack("WT.si_n", "H5号卡销售_" + cmConfId, 0, "WT.si_x", "申请成功", 0);//插码使用
                var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                try {
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n", wtc,
                                "WT.si_s", wts,
                                wtx, "99"],
                            delayTime: 100
                        })

                } catch (e) {
                    console.log(e)
                }
                window.location.href = data.data ;
            }else {
                if (data == '页面过期，请刷新页面') {
                    toggleModal("sorry-modal", "", data, "身份认证提交失败");
                } else {
                    toggleModal("sorry-modal", "", data.message, "身份认证提交失败");
                }

                try {
                        var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                        var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n", wtc,
                                "WT.si_s", wts,
                                wtx, "-99"],
                            delayTime: 100
                        })

                } catch (e) {
                    console.log(e)
                }
            }

        },
        error: function () {
            $("#loding-modal").hide();
            toggleModal("sorry-modal", "", "身份认证提交失败");

            try {
                    var wtc = document.getElementsByTagName('meta')['WT.si_n'].content;
                    var wts = document.getElementsByTagName('meta')['WT.si_s'].content;
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",wtc,
                            "WT.si_s",wts,
                            wtx,"-99"],
                        delayTime: 100
                    })

            }catch(e){
                console.log(e)

            }
        }
    });
});