<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="BMap" uri="/WEB-INF/tlds/baidu.tld" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<%@ include file="/WEB-INF/views/include/message.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/broadBand/pure-grids.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/imgup/imgup.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/js/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
    <script src="${ctxStatic}/js/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/ajaxfileupload.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/comm.js"></script>
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.9&key=cf224bf60876b17eccf9b667790c1576&plugin=AMap.Autocomplete,AMap.PlaceSearch"></script>
    <%--<script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>--%>
    <link href="${ctxStatic}/css/Bmap.css" rel="stylesheet" type="text/css"/>
</head>
<script>
    var baseProject = "${ctx}";
    function confirmSubmit() {
        $(".z_photo").find("input[name^='imageUrls']").remove();
        var photoLi = $(".z_photo").find("li");
        var inputHtml = "";
        photoLi.each(function (index) {
            var fileName = $(this).find("input[name='taglocation']").val();
            inputHtml += "<input type='hidden' name='imageUrls[" + index + "]' value='" + fileName + "'/>";
        });
        $(".z_photo").append(inputHtml);
        showLoadPop();
        $.ajax({
            url : "${ctx}/o2oBusiCollect/addCommuCompetition",
            type: "post",
            dataType: "json",
            data: $("#form1").serialize(),
            success : function(data) {
                hideLoadPop();
                if(data.respCode=='0'){
                    showAlert(data.respDesc, function () {
                        window.location.href=baseProject+"o2oBusiCollect/search?type=6";
                    });
                }else{
                    showAlert(data.respDesc);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                hideLoadPop();
                showAlert("系统错误!");
            }
        });
    }

    $(document).ready(function() {
        $("#form1").validate({
            ignore: [],
            submitHandler: function(form){
//                showLoadPop();
//                form.submit();
                confirmSubmit();
            },
            errorContainer: "#messageBox",
            errorPlacement: function(error, element) {
                $("#messageBox").text("输入有误，请先更正。");
                if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                    error.appendTo(element.parent().parent());
                } else {
                    error.insertAfter(element);
                }
            }
        });
        var data = {'0': '是', '1': '否'};
        var hasElevator=$("#hasElevator").val();
        if(hasElevator){
            $("#hasElevatorOp").html(data['${communityCompetition.hasElevator}']);
        }
        if($("#advPlaced").val()){
            $("#advPlacedOp").html(data['${communityCompetition.advPlaced}']);
        }
        if($("#isAnnex").val()){
            $("#isAnnexOp").html(data['${communityCompetition.isAnnex}']);
        }
        if('${communityCompetition.posterPlace}'){
            var poster='${communityCompetition.posterPlace}'.split(",");
            for(var i=0;i<poster.length;i++){
                $("input:checkbox[value="+poster[i]+"]").attr('checked','true');
            }
        }

        if('query'=='${query}'){
            $("input").each(function (index) {
                $(this).attr('readonly','true');
            });
        }
    });
</script>

<style>
    .user-add li label {
        width: 4.6rem;
        text-align: left;
    }
</style>
<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>小区竞争情况录入</h1>
    </div>
</div>
<form method="post" name="form1" action="addCommuCompetition" id="form1" modelAttribute="communityCompetition">

    <input type="hidden" name="eparchyCode" value="${communityCompetition.eparchyCode}" />
    <input type="hidden" name="eparchyName" value="${communityCompetition.eparchyName}"/>
    <input type="hidden" name="countyName" value="${communityCompetition.countyName}"/>
    <input type="hidden" name="isNew" value="${communityCompetition.isNew}"/>
    <input type="hidden" name="recordId" value="${communityCompetition.recordId}"/>
    <input type="hidden" name="recordStaff" id="recordStaff" value="${communityCompetition.recordStaff}"/>
    <input type="hidden" name="longitude" id="longitude" value="${communityCompetition.longitude}"/>
    <input type="hidden" name="latitude" id="latitude" value="${communityCompetition.latitude}"/>
    <input type="hidden" name="adderss" id="adderss" value="${communityCompetition.adderss}"/>
    <input type="hidden" name="addressPath" value="${communityCompetition.addressPath}"/>

    <div class="container">
        <!--小区地址 start-->
        <%--已覆盖新增 或修改--%>
        <c:if test="${flag ne '0'}">
            <div class="order-detail">
                <div class="adress-info">
                    <div class="adress-txt">
                        <p class="adress-number">${communityCompetition.communityName}</p>
                        <p class="channel-gray9 f12">${communityCompetition.addressPath}</p>
                        <input type="hidden" name="communityName" value="${communityCompetition.communityName}"/>
                        <input type="hidden" name="communityId" value="${communityCompetition.communityId}"/>
                    </div>
                </div>
            </div>
        </c:if>
        <!--小区地址 start-->
        <div class="tabs broad-tabs mt10">
            <div id="tab-1">
                <ul class="user-add">
                    <li>
                        <label>百度地址</label>
                        <div class="user-form">
                            <c:if test="${query ne 'query'}">
                                <BMap:map callback="callbackB" coordinate="BD09"  bmapClass="form-control"/>
                            </c:if>
                            <c:if test="${query eq 'query'}">
                                <input  type="text" class="form-control" readonly="readonly" value="${communityCompetition.adderss}"/>
                            </c:if>
                        </div>
                    </li>
                    <%--未覆盖 新增或修改--%>
                    <c:if test="${flag eq '0'}">
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>小区资管名称</label>
                        <div class="user-form">
                            <input id="communityName" name="communityName" type="text" class="form-control required"
                                   placeholder="输入小区资管名称" value="${communityCompetition.communityName}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>街道/乡、镇</label>
                        <div class="user-form">
                            <input id="street" name="street" type="text" class="form-control required"
                                   placeholder="输入街道/乡、镇" value="${communityCompetition.street}"/>
                        </div>
                    </li>
                    </c:if>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>小区端口数</label>
                        <div class="user-form">
                            <input id="portNum" name="portNum" type="text" class="form-control required number"
                                   placeholder="输入小区端口数" value="${communityCompetition.portNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>覆盖九级地址数</label>
                        <div class="user-form">
                            <input type="text" name="cover9thAddrNum" id="cover9thAddrNum" class="form-control required number" value="${communityCompetition.cover9thAddrNum}" placeholder="输入覆盖九级地址数">
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>小区类型</label>
                        <div class="user-form">
                            <div class="user-form">
                                <input type="hidden" class="form-control required" name="communityType" id="communityType"
                                       value="${communityCompetition.communityType}"/>
                                <span id="communityTypeOp" class="opera">
                                 <c:if test="${communityCompetition.communityType == null}">
                                     未选择
                                 </c:if>
                                <c:if test="${communityCompetition.communityType != null}">
                                    ${communityCompetition.communityType}
                                </c:if>
                                </span>
                                <span class="arrow"></span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>是否有电梯</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control required" name="hasElevator" id="hasElevator"
                                   value="${communityCompetition.hasElevator}"/>
                                    <span id="hasElevatorOp" class="opera">
                                   未选择
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>楼栋数</label>
                        <div class="user-form">
                            <input id="buildingNum" name="buildingNum" type="text" class="form-control required number"
                                   placeholder="输入楼栋数" value="${communityCompetition.buildingNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>单元数</label>
                        <div class="user-form">
                            <input id="unitNum" name="unitNum" type="text" class="form-control required number"
                                   placeholder="输入单元数" value="${communityCompetition.unitNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>小区户数</label>
                        <div class="user-form">
                            <input id="householdsNum" name="householdsNum" type="text" class="form-control required number"
                                   placeholder="输入小区户数" value="${communityCompetition.householdsNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>入住户数</label>
                        <div class="user-form">
                            <input id="residentNum" name="residentNum" type="text" class="form-control required number"
                                   placeholder="输入入住户数" value="${communityCompetition.residentNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>所属物业</label>
                        <div class="user-form">
                            <input id="property" name="property" type="text" class="form-control required"
                                   placeholder="输入入所属物业" value="${communityCompetition.property}"/>
                        </div>
                    </li>
                    <li>
                        <label>物业费</label>
                        <div class="user-form">
                            <input id="propertyCosts" name="propertyCosts" type="text" class="form-control"
                                   placeholder="输入物业费（元/m*）" value="${communityCompetition.propertyCosts}"/>
                        </div>
                    </li>
                    <li>
                        <label>物业联系人姓名</label>
                        <div class="user-form">
                            <input id="propertyContact" name="propertyContact" type="text" class="form-control"
                                   placeholder="输入物业联系人姓名" value="${communityCompetition.propertyContact}"/>
                        </div>
                    </li>
                    <li>
                        <label>物业联系人电话</label>
                        <div class="user-form">
                            <input id="propertyPhone" name="propertyPhone" type="text" class="form-control"
                                   placeholder="输入物业联系人电话" value="${communityCompetition.propertyPhone}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>移动线路接入模式</label>
                        <div class="user-form">
                            <div class="user-form">
                                <input type="hidden" class="form-control required" name="portType" id="portType"
                                       value="${communityCompetition.portType}"/>
                                <span id="portTypeOp" class="opera">
                                 <c:if test="${communityCompetition.portType == null}">
                                     未选择
                                 </c:if>
                                <c:if test="${communityCompetition.portType != null}">
                                    ${communityCompetition.portType}
                                </c:if>
                                </span>
                                <span class="arrow"></span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>小区内宽带运营商及竞争对手线路接入模式</label>
                        <div class="user-form">
                            <div class="user-form">
                                <input type="hidden" class="form-control required" name="competitionPortType" id="competitionPortType"
                                       value="${communityCompetition.competitionPortType}"/>
                                <span id="competitionPortTypeOp" class="opera">
                                 <c:if test="${communityCompetition.competitionPortType == null}">
                                     未选择
                                 </c:if>
                                <c:if test="${communityCompetition.competitionPortType != null}">
                                    ${communityCompetition.competitionPortType}
                                </c:if>
                                </span>
                                <span class="arrow"></span>
                            </div>
                        </div>
                    </li>

                    <li>
                        <label>装机难度</label>
                        <div class="user-form">
                            <div class="user-form">
                                <input type="hidden" class="form-control" name="installDifficulty" id="installDifficulty"
                                       value="${communityCompetition.installDifficulty}"/>
                                <span id="installDifficultyOp" class="opera">
                                   <c:if test="${communityCompetition.installDifficulty == null}">
                                       未选择
                                   </c:if>
                                <c:if test="${communityCompetition.installDifficulty != null}">
                                    ${communityCompetition.installDifficulty}
                                </c:if>
                                </span>
                                <span class="arrow"></span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <label>装机难度备注</label>
                        <div class="user-form">
                            <input id="installDifNote" name="installDifNote" type="text" class="form-control"
                                   placeholder="输入装机难度备注" value="${communityCompetition.installDifNote}"/>
                        </div>
                    </li>
                    <li>
                        <label>信息采集问题备注</label>
                        <div class="user-form">
                            <input id="infoCollectNote" name="infoCollectNote" type="text" class="form-control"
                                   placeholder="输入信息采集问题备注" value="${communityCompetition.infoCollectNote}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>广告是否到位</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control required" name="advPlaced" id="advPlaced"
                                   value="${communityCompetition.advPlaced}"/>
                            <span id="advPlacedOp" class="opera">未选择</span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>物料内容</label>
                        <div class="user-form">
                            <input id="materialsContent" name="materialsContent" type="text" class="form-control required"
                                   placeholder="输入物料内容" value="${communityCompetition.materialsContent}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>固定广告位</label>
                        <div class="user-form">
                            <%--<form:checkbox path="posterPlace" class="check-box required" label="电梯广告" value="电梯广告"/>--%>
                            <%--<form:checkbox path="posterPlace" class="check-box required" label="小区宣传栏" value="小区宣传栏"/>--%>
                            <input type="checkbox" name="posterPlace" class="check-box required" value="电梯广告"/>电梯广告
                            <input type="checkbox" name="posterPlace" class="check-box required" value="小区宣传栏"/>小区宣传栏
                        </div>
                    </li>
                    <li>
                        <label>广告公司名称</label>
                        <div class="user-form">
                            <input id="advCompany" name="advCompany" type="text" class="form-control"
                                   placeholder="输入广告公司名称" value="${communityCompetition.advCompany}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>录入联系号码</label>
                        <div class="user-form">
                            <input id="recordPhone" name="recordPhone" type="text" class="form-control required"
                                   placeholder="输入录入联系号码" value="${communityCompetition.recordPhone}"/>
                        </div>
                    </li>
                    <li>
                        <label>备注</label>
                        <div class="user-form">
                            <input id="note" name="note" type="text" class="form-control"
                                   placeholder="备注" value="${communityCompetition.note}"/>
                        </div>
                    </li>
                    <li>
                        <label>是否附件</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control " name="isAnnex" id="isAnnex"
                                   value="${communityCompetition.isAnnex}"/>
                            <span id="isAnnexOp" class="opera">未选择</span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                </ul>
                <!--图片上传 start-->
                <div class="imgup-wrap">
                    <p class="imgup-title">图片上传：</p>
                    <div class="list_img">
                        <div class="z_photo">
                                <c:forEach items="${communityCompetition.imageUrls}" var="item">
                                <li class="up-li">
                                    <img class="close-upimg" src="${ctxStatic}/images/broadBand/img_close.png">
                                    <img class="up-img" src="${tfsUrl}${item}">
                                    <p class="img-name-p">${item}</p>
                                    <input id="taglocation" name="taglocation" value="${item}" type="hidden"></li>
                                </c:forEach>
                            <c:if test="${query ne 'query'}">
                            <div class="z_file fl">
                                <span class="add-img">+</span>
                                <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple />
                            </div>
                            </c:if>
                        </div>
                        <div class="imgup-mask works-mask">
                            <div class="mask-content">
                                <p>确认删除？</p>
                                <p class="del-p channel-gray9">您确定要删除图片吗？</p>
                                <p class="check-p pure-g"><span class="pure-u-1-2 wsdel-no">取消</span><span class="pure-u-1-2 wsdel-ok">确定</span></p>
                            </div>
                        </div>
                    </div>
                </div>
                <!--图片上传 end-->
                <div class="channel-btn">
                    <c:if test="${query ne 'query'}">
                        <a href="javascript:void(0)" class="broad-btn broad-btn-blue"
                           id="insertBtn" style="padding: 0px;">确认提交</a>
                        </c:if>
                </div>
                <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
            </div>
            <div class="content">
                <div id="partnerInfoList">
                </div>
                <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
                <ul class="marke-user" id="userInfos">
                </ul>
                <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
            </div>
        </div>
    </div>
</form>
<script type="text/javascript" src="${ctxStatic}/js/o2o/businessCollect/addCompetition.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/BMap.js"></script>

</body>
</html>