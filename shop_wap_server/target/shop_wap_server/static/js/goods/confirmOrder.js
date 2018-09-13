$(function(){
    //跳转到选择收货地址页面
    $("#chooseReceiptAddressBtn").live("click",function(){
        var memberAddressId = $(this).find("input[name='memberAddress.memberAddressId']").val();
        if(!memberAddressId) {
            memberAddressId = "";
        }
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToChooseReceiptAddress?memberAddressId="+memberAddressId);
        $("#confirmOrderForm").submit();
    })

    //跳转到选择发票页面
    $("#chooseInvoiceBtn").live("click",function(){
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToChooseInvoice");
        $("#confirmOrderForm").submit();
    });

    //跳转到选择支付方式页面
    $("#choosePayModeBtn").live("click",function(){
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToChoosePayMode");
        $("#confirmOrderForm").submit();
    });

    //跳转到查询套餐信息页面
    $("#queryContractBtn").live("click",function () {
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToContractInfo");
        $("#confirmOrderForm").submit();
    });

    //跳转到选择配送方式页面
    $("#chooseDeliveryModeBtn").live("click",function(){
         if($("span[name='shopName']").length > 1){
            showAlert("多个店铺商品提交订单只能选择物流配送");
            return;
         }

         $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToChooseDeliveryMode");
         $("#confirmOrderForm").submit();
     });

    //跳转到选择收货时间页面
    $("#chooseReceiptTimeBtn").live("click",function(){
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToChooseReceiptTime");
        $("#confirmOrderForm").submit();
    });

    //填写入网信息
    $("#networkInfoBtn").live("click",function(){
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToNetworkInfo");
        $("#confirmOrderForm").submit();
    });

    //跳转到选择优惠券页面
    $("#chooseCouponBtn").live("click",function () {
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToChooseCoupon");
        $("#confirmOrderForm").submit();
    });

    //跳转到填写推荐人页面
    $("#recommenderBtn").live("click",function(){
        $("#confirmOrderForm").attr("action",ctx+"goodsBuy/linkToRecommender");
        $("#confirmOrderForm").submit();
    });

    //点击已选择区县
    $(".yt-tit").click(function(){
        $(".yt-list").toggle();
    });

    //选择区县
    $("#orgList li").live("click",function(){
        var $org = $(this);
        var orgId = $org.attr("orgId");
        var orgName = $org.attr("orgName");
        var eparchCode = $("#cityCode").val();

        var $selectedOrg = $("#selectedOrg");
        $selectedOrg.attr("orgId",orgId);
        $selectedOrg.text(orgName);

        $("#orgList").hide();
        
        var url = ctx + "goodsBuy/queryOrgBusinessHallList";
        var param = {"orgId":orgId,"eparchCode":eparchCode};
        $.post(url,param,function(data){
            $("#hallList  tr:not(:first)").html("");
            
            $.each(data,function(i,hall){
                $("<tr>\n" +
                  "  <td>"+hall.orgName+"</td>\n" +
                  "  <td>"+hall.hallName+"</td>\n" +
                  "  <td>"+hall.hallAddress+"</td>\n" +
                  "</tr>").appendTo($("#hallList"));

            });
        });
    });

    //确认订单
    $("#confirmBtn").live("click",function(){
        var memberAddressId = $("input[name='memberAddress.memberAddressId']").val();
        //手机或配件
        if(rootCategoryId == 10000001 || rootCategoryId ==5){
            if(_shopId == 100000002099){//85店铺
                //收货地址信息校验
                if($.trim($("#memName").val())==''||$.trim($("#phNumber").val())==''||$.trim($("#detailAddress").val())==''
                    || $.trim($("#memZip").val())==''){
                    showAlert('收货信息不可为空');
                    return;
                }
                $("#memberRecipientName").val($("#memName").val());
                $("#memberRecipientPhone").val($("#phNumber").val());
                $("#memberRecipientProvince").val($("#address1").val());
                $("#memberRecipientCity").val($("#address2").val());
                $("#memberRecipientCounty").val($("#address3").val());
                $("#memberRecipientAddress").val($("#detailAddress").val());
                $("#memberRecipientEmail").val($("#memEmail").val());
                $("#memberRecipientZip").val($("#memZip").val());
                $("#confirmOrderForm").attr("action",ctx+"goodsBuy/submit10085Order");
            }else{
                if(memberAddressId==undefined || memberAddressId.length == 0){
                    showAlert("请新增或选择收货地址");
                    return;
                }
            }

            //配件不需要确认协议
            if(rootCategoryId != 5) {
                if (!$("#confirmCB").is(":checked")) {
                    showAlert("请阅读并同意《湖南移动网上商城购前须知协议》");
                    return;
                }
            }

            showLoadPop("正在提交...")
            $("#confirmOrderForm").submit();
        }

        //号卡
        if(rootCategoryId == 2){
            var  deliveryModeId = $("input[name='deliveryMode.deliveryModeId']").val();
            //物流配送
            if(deliveryModeId == 1){
                if(memberAddressId==undefined||memberAddressId.length == 0){
                    showAlert("请新增或选择收货地址");
                    return;
                }
            }

            if($("input[name='orderDetailSim.regName']").val() == 0){
                showAlert("请填写入网信息");
                return;
            }

            $.ajax({
                url:ctx + "/goodsBuy/realityVerify",
                type: "post",
                data: $("#confirmOrderForm").serializeObject(),
                dataType: "json",
                success: function (data) {
                    if(data.resultCode != "success"){
                        showAlert('身份证信息不一致，请重新填写');
                        return;
                    }
                    showLoadPop("正在提交...")
                    $("#confirmOrderForm").submit();
                }
            });
        }


    });

    $("#confirmModal").click(function(){
        if ($("#myModal").is(":visible")) {
            $("#myModal").hide();
        } else {
            $("#myModal").show();
        }
    });

    $("#comfirm").click(function(){
        $("#myModal").hide();
        $("#confirmCB").attr("checked", true);
    });

    $("#closeForm").click(function(){
        $("#myModal").hide();
        $("#confirmCB").attr("checked", true);
    });

});

/**
 * 将表单元素序列化为JSON对象
 * 基于jQuery serializeArray()
 */
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