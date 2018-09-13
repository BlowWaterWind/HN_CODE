<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/js/iOSselect/iosSelect.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/oil.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/js/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
    <script src="${ctxStatic}/js/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
</head>
<script>
    var baseProject = "${ctx}";
    $(document).ready(function() {
            $("#form0").validate({
                ignore: [],
                submitHandler: function(form){
                    showLoadPop();
                    submitRecord('form0');
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

        $("#form1").validate({
            ignore: [],
            submitHandler: function(form){
                showLoadPop();
                submitRecord('form1');
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
        if($("#isDevInstall0").val()){
            $("#isDevInstallOp0").html(data['${o2oVillageReserve0.isDevInstall}']);
        }
        if($("#isDevInstall1").val()){
            $("#isDevInstallOp1").html(data['${o2oVillageReserve1.isDevInstall}']);
        }

        if($("#isSubmitDemand0").val()){
            $("#isSubmitDemandOp0").html(data['${o2oVillageReserve0.isSubmitDemand}']);
        }
        if($("#isSubmitDemand1").val()){
            $("#isSubmitDemandOp1").html(data['${o2oVillageReserve1.isSubmitDemand}']);
        }

        if($("#isFinished0").val()){
            $("#isFinishedOp0").html(data['${o2oVillageReserve0.isFinished}']);
        }
        if($("#isFinished1").val()){
            $("#isFinishedOp1").html(data['${o2oVillageReserve1.isFinished}']);
        }
        if('query'=='${query}'){
            $("input").each(function (index) {
                $(this).attr('readonly','true');
            });
        }
        var type='${type}';
        if(type==null||type==''){
            type='0';
        }
        $("#tab-"+type).show();
        $('#menu-'+type).addClass("active");
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
        <h1>信息录入</h1>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/message.jsp" %>
<div class="container">
    <!--小区地址 start-->
    <div class="tabs broad-tabs mt10">
        <ul class="tab-menu">
            <li id="menu-0" onclick="changeTab('0')">清单范围内</li>
            <li id="menu-1" onclick="changeTab('1')">清单范围外</li>
        </ul>
        <div id="tab-0" style="display: none;">
            <form method="post" action="addVillageReverse" id="form0">
                <input type="hidden" name="recordId" value="${o2oVillageReserve0.recordId}">
                <input type="hidden" id="isNew" name="isNew" value="${o2oVillageReserve0.isNew}">
                <input type="hidden" name="recordType" value="0">
                <input type="hidden" name="zgCode" id="zgCode" value="${o2oVillageReserve0.zgCode}">
                <input type="hidden" name="townName" id="townName" value="${o2oVillageReserve0.townName}">
                <input type="hidden" name="changedIllageName" id="changedIllageName" value="${o2oVillageReserve0.changedIllageName}">
                <input type="hidden" name="eparchyName" id="eparchyName" value="${o2oVillageReserve0.eparchyName}">
                <input type="hidden" name="countyName" id="countyName" value="${o2oVillageReserve0.countyName}">
                <c:if test="${o2oVillageReserve0.isNew eq '0' or o2oVillageReserve0.isNew == null}">
                <div class="container bg-gray hy-tab">
                    <div class="wf-list tab-con">
                        <div class="tab-tit font-red">请依次点选地市和区域和乡镇、行政村</div>
                        <!--地市选择 start-->
                        <ul class="change-list">
                            <li>
                                <label>地&emsp;&emsp;区：</label>
                                <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
                                    <select onchange="qryCountyName(this.value)" class="set-sd" name="eparchyCode" id="eparchyCode">
                                      <option >请选择</option>
                                      <c:forEach items="${orgList}" var="org" varStatus="status">
                                          <option value="${org.orgId}">${org.orgName}</option>
                                      </c:forEach>
                                    </select>
                                  </span> <span class="td-fr"> <i class="select-arrow"></i>
                                  <select onchange="qryTownName(this.value)" class="set-sd city-area" name="countyCode" id="countyCode">
                                      <%--<option value="岳麓区">岳麓区</option>--%>
                                  </select>
                                  </span></div>
                            </li>
                            <li>
                                <label>乡&emsp;&emsp;镇：</label>
                                 <div class="right-td"> <span class="td-fr"><i class="select-arrow"></i>
                              <select class="set-sd" name="townId" onchange="qryChangedIllageName(this.value)" id="townId">
                              </select>
                              </span> <span class="td-fr"> <i class="select-arrow"></i>
                              <select  class="set-sd road-area" name="changedIllageId" onchange="qryChangedIllage(this)" id="changedIllageId">
                              </select>
                            </span></div>
                            </li>
                        </ul>
                        <!--地市选择 end-->
                    </div>
                </div>
                </c:if>
                <ul class="user-add">
                    <c:if test="${query eq 'query' or o2oVillageReserve0.isNew eq '1'}">
                        <input type="hidden" name="eparchyCode"  value="${o2oVillageReserve0.eparchyCode}">
                        <input type="hidden" name="countyCode"  value="${o2oVillageReserve0.countyCode}">
                    <li>
                        <label>市州名:</label>
                        <div class="user-form">
                            <input  type="text" class="form-control" maxlength="40"
                                    placeholder="输入市州名" value="${o2oVillageReserve0.eparchyName}" readonly="readonly"/>
                        </div>
                    </li>
                    <li>
                        <label>区县名:</label>
                        <div class="user-form">
                            <input  type="text" class="form-control" maxlength="40"
                                    placeholder="输入区县名" value="${o2oVillageReserve0.countyName}" readonly="readonly"/>
                        </div>
                    </li>
                    <li>
                        <label>乡镇名</label>
                        <div class="user-form">
                            <input  type="text" class="form-control" maxlength="40"
                                    placeholder="输入乡镇名" value="${o2oVillageReserve0.townName}" readonly="readonly"/>
                        </div>
                    </li>
                    <li>
                        <label>调整后行政村名:</label>
                        <div class="user-form">
                            <input  type="text" class="form-control" maxlength="40"
                                    placeholder="输入调整后行政村名" value="${o2oVillageReserve0.changedIllageName}" readonly="readonly"/>
                        </div>
                    </li>
                    </c:if>
                    <li>
                    <label>人口总数</label>
                        <div class="user-form">
                            <input  name="peopleTotalNum" type="text" class="form-control" maxlength="8"
                                   placeholder="输入人口总数" value="${o2oVillageReserve0.peopleTotalNum}"/>
                        </div>
                    </li>
                    <li>
                        <label>家庭总户数</label>
                        <div class="user-form">
                            <input  name="familyTotalNum" type="text" class="form-control" maxlength="8"
                                   placeholder="输入家庭总户数" value="${o2oVillageReserve0.familyTotalNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>是否展开预约</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control required" name="isDevInstall" id="isDevInstall0"
                                   value="${o2oVillageReserve0.isDevInstall}"/>
                                    <span id="isDevInstallOp0" class="opera">
                                   未选择
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>预约用户数</label>
                        <div class="user-form">
                            <input  name="installNum" type="text" class="form-control required" maxlength="15"
                                   placeholder="输入预约用户数" value="${o2oVillageReserve0.installNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>是否提交建设需求</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control required" name="isSubmitDemand" id="isSubmitDemand0"
                                   value="${o2oVillageReserve0.isSubmitDemand}"/>
                                    <span id="isSubmitDemandOp0" class="opera">
                                   未选择
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>提交需求时间</label>
                        <div class="user-form">
                            <c:if test="${o2oVillageReserve0.submitDemandTime != null}">
                                <input type="hidden" name="submitDemandTime" id="submitDemandTime0" class="required" value="<fmt:formatDate value="${o2oVillageReserve0.submitDemandTime}" pattern="yyyy-MM-dd"/>">
                                <div class="form-control" data-year="" data-month="" data-date="" id="submitDemandDate0">
                                    <fmt:formatDate value="${o2oVillageReserve0.submitDemandTime}" pattern="yyyy年MM月dd日"/>
                                </div>
                            </c:if>
                            <c:if test="${o2oVillageReserve0.submitDemandTime == null}">
                                <input type="hidden" name="submitDemandTime" id="submitDemandTime0" value="" class="required">
                                <div class="form-control" data-year="" data-month="" data-date="" id="submitDemandDate0">
                                    未选择
                                </div>
                            </c:if>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>已预约未提交建设需求的原因</label>
                        <div class="user-form">
                            <input  name="reason" type="text" class="form-control" maxlength="15"
                                   placeholder="输入已预约未提交建设需求的原因" value="${o2oVillageReserve0.reason}"/>
                        </div>
                    </li>
                    <li>
                        <label>是否建设完毕</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control" name="isFinished" id="isFinished0"
                                   value="${o2oVillageReserve0.isFinished}"/>
                                    <span id="isFinishedOp0" class="opera">
                                   未选择
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>建设完毕时间</label>
                        <div class="user-form">
                            <c:if test="${o2oVillageReserve0.finishedTime != null}">
                                <input type="hidden" name="finishedTime" id="finishedTime0" value="<fmt:formatDate value="${o2oVillageReserve0.finishedTime}" pattern="yyyy-MM-dd"/>">
                                <div class="form-control" data-year="" data-month="" data-date="" id="finishedDate0">
                                    <fmt:formatDate value="${o2oVillageReserve0.finishedTime}" pattern="yyyy年MM月dd日"/>
                                </div>
                            </c:if>
                            <c:if test="${o2oVillageReserve0.finishedTime == null}">
                                <input type="hidden" name="finishedTime" id="finishedTime0" value="">
                                <div class="form-control" data-year="" data-month="" data-date="" id="finishedDate0">
                                    未选择
                                </div>
                            </c:if>
                            <span class="arrow"></span>
                        </div>
                    </li>
                </ul>
                    <div class="channel-btn">
                        <c:if test="${query ne 'query'}">
                            <a href="javascript:void(0)" class="broad-btn broad-btn-blue"
                               id="insertBtn0" style="padding: 0px;">确认提交</a>
                        </c:if>
                    </div>
                <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
            </form>
        </div>
        <div id="tab-1" style="display:none">
            <form method="post" action="addVillageReverse" id="form1">
                <input type="hidden" name="recordId" value="${o2oVillageReserve1.recordId}">
                <input type="hidden" name="isNew" value="${o2oVillageReserve1.isNew}">
                <input type="hidden" name="recordType" value="1">
                <ul class="user-add">
                    <li>
                        <label>市州名:</label>
                        <div class="user-form">
                            <input  name="eparchyName" type="text" class="form-control" maxlength="40"
                                   placeholder="输入市州名" value="${o2oVillageReserve1.eparchyName}"/>
                        </div>
                    </li>
                    <li>
                        <label>区县名:</label>
                        <div class="user-form">
                            <input  name="countyName" type="text" class="form-control" maxlength="40"
                                   placeholder="输入区县名" value="${o2oVillageReserve1.countyName}"/>
                        </div>
                    </li>
                    <li>
                        <label>乡镇名</label>
                        <div class="user-form">
                            <input  name="townName" type="text" class="form-control" maxlength="40"
                                   placeholder="输入乡镇名" value="${o2oVillageReserve1.townName}"/>
                        </div>
                    </li>
                    <li>
                        <label>调整后行政村名:</label>
                        <div class="user-form">
                            <input  name="changedIllageName" type="text" class="form-control" maxlength="40"
                                   placeholder="输入调整后行政村名" value="${o2oVillageReserve1.changedIllageName}"/>
                        </div>
                    </li>
                    <li>
                        <label>资管编码</label>
                        <div class="user-form">
                            <input name="zgCode" type="text" class="form-control" maxlength="40"
                                   placeholder="输入资管编码" value="${o2oVillageReserve1.zgCode}"/>
                        </div>
                    </li>
                    <li>
                        <label>人口总数</label>
                        <div class="user-form">
                            <input id="peopleTotalNum" name="peopleTotalNum" type="text" class="form-control" maxlength="8"
                                   placeholder="输入人口总数" value="${o2oVillageReserve1.peopleTotalNum}"/>
                        </div>
                    </li>
                    <li>
                        <label>家庭总户数</label>
                        <div class="user-form">
                            <input id="familyTotalNum" name="familyTotalNum" type="text" class="form-control" maxlength="8"
                                   placeholder="输入家庭总户数" value="${o2oVillageReserve1.familyTotalNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>是否展开预约</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control required" name="isDevInstall" id="isDevInstall1"
                                   value="${o2oVillageReserve1.isDevInstall}"/>
                                    <span id="isDevInstallOp1" class="opera">
                                   未选择
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>预约用户数</label>
                        <div class="user-form">
                            <input id="installNum" name="installNum" type="text" class="form-control required" maxlength="15"
                                   placeholder="输入预约用户数" value="${o2oVillageReserve1.installNum}"/>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>是否提交建设需求</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control required" name="isSubmitDemand" id="isSubmitDemand1"
                                   value="${o2oVillageReserve1.isSubmitDemand}"/>
                                    <span id="isSubmitDemandOp1" class="opera">
                                   未选择
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <span style="color:#f95657">*</span>
                        <label>提交需求时间</label>
                        <div class="user-form">
                            <c:if test="${o2oVillageReserve1.submitDemandTime != null}">
                                <input type="hidden" name="submitDemandTime" id="submitDemandTime1" class="required" value="<fmt:formatDate value="${o2oVillageReserve1.submitDemandTime}" pattern="yyyy-MM-dd"/>">
                                <div class="form-control" data-year="" data-month="" data-date="" id="submitDemandDate1">
                                    <fmt:formatDate value="${o2oVillageReserve1.submitDemandTime}" pattern="yyyy年MM月dd日"/>
                                </div>
                            </c:if>
                            <c:if test="${o2oVillageReserve1.submitDemandTime == null}">
                                <input type="hidden" name="submitDemandTime" id="submitDemandTime1" value="" class="required">
                                <div class="form-control" data-year="" data-month="" data-date="" id="submitDemandDate1">
                                    未选择
                                </div>
                            </c:if>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>已预约未提交建设需求的原因</label>
                        <div class="user-form">
                            <input id="reason" name="reason" type="text" class="form-control" maxlength="15"
                                   placeholder="输入已预约未提交建设需求的原因" value="${o2oVillageReserve1.reason}"/>
                        </div>
                    </li>
                    <li>
                        <label>是否建设完毕</label>
                        <div class="user-form">
                            <input type="hidden" class="form-control" name="isFinished" id="isFinished1"
                                   value="${o2oVillageReserve1.isFinished}"/>
                                    <span id="isFinishedOp1" class="opera">
                                   未选择
                            </span>
                            <span class="arrow"></span>
                        </div>
                    </li>
                    <li>
                        <label>建设完毕时间</label>
                        <div class="user-form">
                            <c:if test="${o2oVillageReserve1.finishedTime != null}">
                                <input type="hidden" name="finishedTime" id="finishedTime1" value="<fmt:formatDate value="${o2oVillageReserve1.finishedTime}" pattern="yyyy-MM-dd"/>">
                                <div class="form-control" data-year="" data-month="" data-date="" id="finishedDate1">
                                    <fmt:formatDate value="${o2oVillageReserve1.finishedTime}" pattern="yyyy年MM月dd日"/>
                                </div>
                            </c:if>
                            <c:if test="${o2oVillageReserve1.finishedTime == null}">
                                <input type="hidden" name="finishedTime" id="finishedTime1" value="">
                                <div class="form-control" data-year="" data-month="" data-date="" id="finishedDate1">
                                    未选择
                                </div>
                            </c:if>
                            <span class="arrow"></span>
                        </div>
                    </li>
                </ul>
                <div class="channel-btn">
                    <c:if test="${query ne 'query'}">
                        <a href="javascript:void(0)" class="broad-btn broad-btn-blue" id="insertBtn1" style="padding: 0px;">确认提交</a>
                    </c:if>
                </div>
                <!--按钮变灰class broad-btn-blue换成broad-btn-gray-->
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iscroll.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/iOSselect/iosSelect.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/o2o/businessCollect/addVillage.js"></script>
</body>
</html>