<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="org.apache.shiro.session.Session" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Session shrioSession = UserUtils.getSession();
    UserGoodsCarModel carModel = (UserGoodsCarModel) shrioSession.getAttribute("carModel");
%>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/num-card.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/goods/ajaxfileupload.js"></script>
    <script type="text/javascript">
        $(function () {
            //确认验证
            $("#confirmBtn").click(function () {
                if ($.trim($("#regName").val()).length == 0) {
                    showAlert("请填写姓名");
                    return;
                }
                var psptId = $.trim($("#psptId").val());
                if (psptId.length == 0) {
                    showAlert("请填写身份证号");
                    return;
                }
                var psptAddr = $("#psptAddr").val();
                if ($.trim(psptAddr).length == 0) {
                    showAlert("请填写地址");
                    return;
                }
                var contactPhone = $('#contactPhone').val();
                var deliveryModeId = $('input[name="deliveryModeId"]').val();
                var regPhone  = /^1[34578]\d{9}$/;
                if (deliveryModeId === '2' && !regPhone.test(contactPhone)) {
                    showAlert("请输入正确手机号码");
                    return;
                };
                //== 证件校验
                if (chkHalf(psptAddr)) {
                    showAlert("证件地址中不能包含全角字符，请重新填写");
                    return;
                }
                if (/^[0-9]*$/.test(psptAddr)) {
                    showAlert("证件地址不能是纯数字，请重新填写");
                    return;
                }
                //psptAddr.match(/[\u4E00-\u9FA5]/g).length < 4
                if (chkAddrLen(psptAddr)) {
                    showAlert("证件地址必须大于等于4个汉字，请重新填写");
                    return;
                }

                if (!isIdCard(psptId)) {
                    //$("#psptId").val("");
                    showAlert("身份证号码校验错误!请重新输入");
                    return;
                }
                //一月5次号卡订单校验
                $.ajax({
                    url: ctx + "/goodsBuy/simOrderCountVerify",
                    type: "post",
                    data: {"psptId": psptId},
                    dataType: "json",
                    success: function (data) {
                        if (data.resultCode == "fail") {
                            showAlert("此身份证号码在一个月内已提交超过5次号卡订单，暂时不能提交号卡订单");
                            return;
                        }
                    }
                });
                $.ajax({
                    url: ctx + "/goodsBuy/realityVerifyV2",
                    type: "post",
                    data: $("#networkInfoForm").serialize(),
                    dataType: "json",
                    success: function (data) {
                        if (data.resultCode == "fail") {
                            showAlert(data.resultDesc);
                            return;
                        }
                        $("#networkInfoForm").submit();
                    }
                });

            });
            // 校验入网信息日期格式
            $("input[alt='date']").on("blur", function () {
                var dateRegExp = /^(\d{4})\-(\d{2})\-(\d{2})$/;
                if (!dateRegExp.test($(this).val())) {
                    $(this).val("");
                    showAlert('请填写正确的日期格式(yyyy-MM-dd)');
                    return;
                }
            });

        });

        /**
         * 校验是否包含全角字符
         */
        var chkHalf = function (str) {
            for (var i = 0; i < str.length; i++) {
                var strCode = str.charCodeAt(i);
                if ((strCode > 65248) || (strCode == 12288)) {
                    return true;
                }
            }
            return false;
        };

        /**
         * 检验长度
         */
        var chkAddrLen = function(str){
            if (str == null) return 0;
            if (typeof str != "string"){
                str += "";
            }
            if(str.replace(/[^\x00-\xff]/g,"01").length < 12){
                return true;
            }
            return false;
        }

        /*校验是否为身份证号码*/
        function isIdCard(idcard) {
            var area = {
                11: "北京",
                12: "天津",
                13: "河北",
                14: "山西",
                15: "内蒙古",
                21: "辽宁",
                22: "吉林",
                23: "黑龙江",
                31: "上海",
                32: "江苏",
                33: "浙江",
                34: "安徽",
                35: "福建",
                36: "江西",
                37: "山东",
                41: "河南",
                42: "湖北",
                43: "湖南",
                44: "广东",
                45: "广西",
                46: "海南",
                50: "重庆",
                51: "四川",
                52: "贵州",
                53: "云南",
                54: "西藏",
                61: "陕西",
                62: "甘肃",
                63: "青海",
                64: "宁夏",
                65: "xinjiang",
                71: "台湾",
                81: "香港",
                82: "澳门",
                91: "国外"
            }
            var idcard, Y, JYM;
            var S, M;
            var idcard_array = new Array();
            idcard_array = idcard.split("");
            if (area[parseInt(idcard.substr(0, 2))] == null)
                return false;
            switch (idcard.length) {
                case 15 :
                    if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
                        || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
                            .substr(6, 2)) + 1900)
                        % 4 == 0)) {
                        ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
                    } else {
                        ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
                    }
                    if (ereg.test(idcard))
                        return true;
                    else
                        return false;
                    break;
                case 18 :
                    if (parseInt(idcard.substr(6, 4)) % 4 == 0
                        || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
                            .substr(6, 4))
                        % 4 == 0)) {
                        ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
                    } else {
                        ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
                    }
                    if (ereg.test(idcard)) {
                        S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10]))
                            * 7
                            + (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
                            * 9
                            + (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
                            * 10
                            + (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
                            * 5
                            + (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
                            * 8
                            + (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
                            * 4
                            + (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
                            * 2 + parseInt(idcard_array[7]) * 1
                            + parseInt(idcard_array[8]) * 6
                            + parseInt(idcard_array[9]) * 3;
                        Y = S % 11;
                        M = "F";
                        JYM = "10X98765432";
                        M = JYM.substr(Y, 1);
                        if (M == idcard_array[17])
                            return true;
                        else
                            return false;
                    } else
                        return false;
                    break;
                default :
                    return false;
                    break;
            }
        }
    </script>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
        <h1>填写入网信息</h1>
    </div>
</div>


<form id="networkInfoForm" action="${ctx}goodsBuy/linkToConfirmOrderSim" method="post">
    <input type="hidden" name="orderDetailSim.phone" value="${carModel.orderDetailSim.phone}"/>
    <input type="hidden" name="orderDetailSim.simProductId" value="${carModel.orderDetailSim.simProductId}"/>
    <input type="hidden" name="orderDetailSim.baseSet" value="${carModel.orderDetailSim.baseSet}"/>
    <input type="hidden" name="orderDetailSim.baseSetName" value="${carModel.orderDetailSim.baseSetName}"/>
    <input type="hidden" name="orderDetailSim.preFee" value="${carModel.orderDetailSim.preFee}"/>
    <input type="hidden" name="orderDetailSim.cityCode" value="${carModel.orderDetailSim.cityCode}"/>
    <input id="imgServerPath" type="hidden" value="${imgServerPath}"/>
    <input type="hidden" name="deliveryModeId" value="${carModel.deliveryMode.deliveryModeId}"/>
    <div class="container pd-t45 gray-hs">
        <div class="rw-xy">
            <div class="tx-xy">
                <h4><s></s><b>填写入网信息</b></h4>
                <span>根据国家工信部《电话用户真实身份信息登记规定》要求，用户通过运营商办理新入网业务需进行实名制登记。湖南移动将保证此身份证照片仅用于本次入网使用。</span>
                <ul class="order-info4 order-ft">
                    <li>
                        <label><b>*</b>姓名：</label>
                        <div class="right-td">
                            <input id="regName" type="text" name="orderDetailSim.regName"
                                   value="${carModel.orderDetailSim.regName}" class="form-control form-fr"
                                   placeholder="请填写与上传证件照片一致的姓名"/>
                        </div>
                    </li>
                    <li>
                        <label><b>*</b>身份证号：</label>
                        <div class="right-td">
                            <input id="psptId" type="text" name="orderDetailSim.psptId"
                                   value="${carModel.orderDetailSim.psptId}" class="form-control form-fr"
                                   placeholder="请填写与上传证件照片一致的身份证号码" maxlength="18"/>
                        </div>
                    </li>
                    <li>
                        <label><b>*</b>地址：</label>
                        <div class="right-td">
                            <input id="psptAddr" type="text" name="orderDetailSim.psptAddr"
                                   value="${carModel.orderDetailSim.psptAddr}" class="form-control form-fr"
                                   placeholder="请填写与上传证件照片一致的证件地址"/>
                        </div>
                    </li>
                    <c:if test="${carModel.deliveryMode.deliveryModeId == 2}">
                    <li>
                        <label><b>*</b>联系方式：</label>
                        <div class="right-td">
                            <input id="contactPhone" type="text" name="orderDetailSim.contactPhone"
                                   value="${carModel.orderDetailSim.contactPhone}" class="form-control form-fr"
                                   placeholder="请填写可以联系到您的电话号码"/>
                        </div>
                    </li>
                    </c:if>
                </ul>
            </div>
        </div>
        <div class="tj-btn">
            <a onclick="window.history.back()" class="btn btn-green" href="javascript:void(0)">取消</a>
            <a id="confirmBtn" class="btn btn-blue" href="javascript:void(0)">确定</a>
        </div>
    </div>
</form>
</body>
</html>