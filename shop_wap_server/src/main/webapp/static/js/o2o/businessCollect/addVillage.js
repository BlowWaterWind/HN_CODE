function qryAddress (parentId,id) {
    $.getJSON(baseProject+'/o2oBusiCollect/qryOrgByParentId', {
            'parentId': parentId
        }, function (e) {
            if (e.respCode == "0")
            {
                $.each(e.data, function (i, item) {
                    $('#'+id).append("<option value="+item.orgId+" zgCode="+item.orgCode+">" + item.orgName+ "</option>");
                });
            } else {
                alert("系统异常，请稍后再试");
            }
        }
    );
}

function qryCountyName(cityCode) {
    $('#countyCode').html("<option value=''>请选择</option>");
    $('#townId').html("");
    $('#changedIllageId').html("");
    if(cityCode==''){
        return;
    }
    $.ajaxSettings.async = false;
    qryAddress(cityCode,'countyCode');
}

function qryTownName (parentId) {
    $('#townId').html("<option value=''>请选择</option>");
    $('#changedIllageId').html("");
    if(parentId==''){
        return;
    }
    $.ajaxSettings.async = false;
    qryAddress(parentId,'townId');
}

function qryChangedIllageName (parentId) {
    $('#changedIllageId').html("<option value=''>请选择</option>");
    if(parentId==''){
        return;
    }
    $.ajaxSettings.async = false;
    qryAddress(parentId,'changedIllageId');
    $("#townName").val($("#townId").find("option:selected").text());
}
function qryChangedIllage(obj){
    $('#changedIllageName').val($(obj).find("option:selected").text());
    $('#zgCode').val($(obj).find("option:selected").attr('zgCode'))
}

var data0 = [
    {'id': '0', 'value': '是'},
    {'id': '1', 'value': '否'}
];
var isDevInstallDom0 = document.querySelector('#isDevInstall0');//hidden
var isDevInstallOpDom0 = document.querySelector('#isDevInstallOp0');//外层
isDevInstallOpDom0.addEventListener('click', function () {
    createIsoSelect(isDevInstallDom0,isDevInstallOpDom0,'isDevInstall0','是否展开预约');
});
var isDevInstallDom1 = document.querySelector('#isDevInstall1');//hidden
var isDevInstallOpDom1 = document.querySelector('#isDevInstallOp1');//外层
isDevInstallOpDom1.addEventListener('click', function () {
    createIsoSelect(isDevInstallDom1,isDevInstallOpDom1,'isDevInstall1','是否展开预约');
});

var isSubmitDemandDom0 = document.querySelector('#isSubmitDemand0');//hidden
var isSubmitDemandOpDom0 = document.querySelector('#isSubmitDemandOp0');//外层
isSubmitDemandOpDom0.addEventListener('click', function () {
    createIsoSelect(isSubmitDemandDom0,isSubmitDemandOpDom0,'isSubmitDemand0','是否提交建设需求');
});
var isSubmitDemandDom1 = document.querySelector('#isSubmitDemand1');//hidden
var isSubmitDemandOpDom1 = document.querySelector('#isSubmitDemandOp1');//外层
isSubmitDemandOpDom1.addEventListener('click', function () {
    createIsoSelect(isSubmitDemandDom1,isSubmitDemandOpDom1,'isSubmitDemand1','是否提交建设需求');
});

var isFinishedDom0 = document.querySelector('#isFinished0');
var isFinishedOpDom0 = document.querySelector('#isFinishedOp0');
isFinishedOpDom0.addEventListener('click', function () {
    createIsoSelect(isFinishedDom0,isFinishedOpDom0,'isFinished0','是否建设完毕');
});
var isFinishedDom1 = document.querySelector('#isFinished1');
var isFinishedOpDom1 = document.querySelector('#isFinishedOp1');
isFinishedOpDom1.addEventListener('click', function () {
    createIsoSelect(isFinishedDom1,isFinishedOpDom1,'isFinished1','是否建设完毕');
});


function createIsoSelect(operaDom0,operaIdDom0,id,title){
    var operaId = operaIdDom0.dataset['id'];
    var operaName = operaIdDom0.dataset['value'];
    var operaSelect = new IosSelect(1,
        [data0],
        {
            container: '.container',
            title: title,
            itemHeight: 50,
            itemShowCount: 3,
            oneLevelId: 0,
            callback: function (selectOneObj) {
                operaDom0.value = selectOneObj.id;
                operaIdDom0.innerHTML = selectOneObj.value;
                operaIdDom0.dataset['id'] = selectOneObj.id;
                operaIdDom0.dataset['value'] = selectOneObj.value;
                $('#'+id).valid();
            }
        });
}

var submitDemandDate0 = $("#submitDemandDate0");
var finishedDate0 = $("#finishedDate0");
var submitDemandDate1 = $("#submitDemandDate1");
var finishedDate1 = $("#finishedDate1");
// 初始化时间
var now = new Date();
var nowYear = now.getFullYear();
var nowMonth = now.getMonth() + 1;
var nowDate = now.getDate();
function init(obj){
    obj.attr('data-year', nowYear);
    obj.attr('data-month', nowMonth);
    obj.attr('data-date', nowDate);
}
init(submitDemandDate0);
init(submitDemandDate1);
init(finishedDate0);
init(finishedDate1);
// 数据初始化
function formatYear(nowYear) {
    var arr = [];
    for (var i = nowYear - 5; i <= nowYear + 5; i++) {
        arr.push({
            id: i + '',
            value: i + '年'
        });
    }
    return arr;
}

function formatMonth() {
    var arr = [];
    for (var i = 1; i <= 12; i++) {
        arr.push({
            id: i + '',
            value: i + '月'
        });
    }
    return arr;
}

function formatDate(count) {
    var arr = [];
    for (var i = 1; i <= count; i++) {
        arr.push({
            id: i + '',
            value: i + '日'
        });
    }
    return arr;
}
var yearData = function (callback) {
    callback(formatYear(nowYear))
}
var monthData = function (year, callback) {
    callback(formatMonth());
};
var dateData = function (year, month, callback) {
    if (/^(1|3|5|7|8|10|12)$/.test(month)) {
        callback(formatDate(31));
    } else if (/^(4|6|9|11)$/.test(month)) {
        callback(formatDate(30));
    } else if (/^2$/.test(month)) {
        if (year % 4 === 0 && year % 100 !== 0 || year % 400 === 0) {
            callback(formatDate(29));
        } else {
            callback(formatDate(28));
        }
    } else {
        throw new Error('month is illegal');
    }
};

finishedDate0.bind('click', function () {
    var finishedTime0=$('#finishedTime0');
    createDateSelect(finishedDate0,finishedTime0);
});
finishedDate1.bind('click', function () {
    var finishedTime1=$('#finishedTime1');
    createDateSelect(finishedDate1,finishedTime1);
});

function createDateSelect(obj,objVal){
    var oneLevelId = obj.attr('data-year');
    var twoLevelId = obj.attr('data-month');
    var threeLevelId = obj.attr('data-date');
    var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
        title: '日期选择',
        itemHeight: 35,
        relation: [1, 1],
        oneLevelId: oneLevelId,
        twoLevelId: twoLevelId,
        threeLevelId: threeLevelId,
        showLoading: true,
        callback: function (selectOneObj, selectTwoObj, selectThreeObj) {
            obj.attr('data-year', selectOneObj.id);
            obj.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
            objVal.val(selectOneObj.id + "-" + selectTwoObj.id + "-" + selectThreeObj.id);
            objVal.valid();
        }
    });
}

submitDemandDate0.bind('click', function () {
    var submitDemandTime0=$('#submitDemandTime0');
    createDateSelect(submitDemandDate0,submitDemandTime0);
});
submitDemandDate1.bind('click', function () {
    var submitDemandTime1=$('#submitDemandTime1');
    createDateSelect(submitDemandDate1,submitDemandTime1);
});

$("#insertBtn0").click(function(){
    if($("#isNew").val()!='1'){
        $("#eparchyName").val($("#eparchyCode").find("option:selected").text());
        $("#countyName").val($("#countyCode").find("option:selected").text());
    }
    $("#form0").submit();
});

$("#insertBtn1").click(function(){
    $("#form1").submit();
});

function changeTab(id){
    $(".tab-menu li").each(function(){
        $(this).removeClass("active");
    });
    $(".broad-tabs").children('div').each(function(){
        $(this).hide();
    });
    $("#menu-"+id).addClass("active");
    $("#tab-"+id).show();
}

function submitRecord(form){
    $.ajax({
        url : baseProject+"/o2oBusiCollect/addVillageReverse",
        type : "post",
        dataType : "json",
        data: $('#'+form).serialize(),
        success : function(data) {
            hideLoadPop();
            if(data.respCode=='0'){
                showAlert(data.respDesc, function () {
                    window.location.href=baseProject+"o2oBusiCollect/searchVillage";
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
