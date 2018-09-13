$(function(){
    var selectDateDom = $('#selectDate');
    var showDateDom = $('#showDate');
    // 初始化时间
    var now = new Date();
    var tomorrow = new Date(now);
    tomorrow.setDate(now.getDate() + 1);
    var nowYear = tomorrow.getFullYear();
    var nowMonth = tomorrow.getMonth() + 1;
    var nowDate = tomorrow.getDate();
    var futureDate =  new Date(now);
    tomorrow.setDate(now.getDate() + 45);
    var furtureYear = tomorrow.getFullYear();
    var furtureMonth= tomorrow.getMonth() + 1;
    var furtureDay = tomorrow.getDate();
    showDateDom.attr('data-year', nowYear);
    showDateDom.attr('data-month', nowMonth);
    showDateDom.attr('data-date', nowDate);
    // 数据初始化
    function formatYear(nowYear) {
        var arr = [];
        for (var i = nowYear; i <= furtureYear; i++) {
            arr.push({
                id: i + '',
                value: i + '年'
            });
        }
        formatMonth();
        return arr;
    }

    function formatMonth(startMonth,endMonth) {
        var arr = [];

        for (var i = startMonth; i <= endMonth; i++) {
            arr.push({
                id: i + '',
                value: i + '月'
            });
        }
        return arr;
    }

    function formatDate(startDay,count) {
        var arr = [];
        for (var i = startDay; i <= count; i++) {
            arr.push({
                id: i + '',
                value: i + '日'
            });
        }
        return arr;
    }
    var yearData = function(callback) {
        callback(formatYear(nowYear))
    }
    var monthData = function(year, callback) {
        var startMonth = 1;
        var endMonth = 12;
        if(year == nowYear){
            startMonth = nowMonth;
        }
        if(year == furtureYear){
            endMonth = furtureMonth;
        }
        callback(formatMonth(startMonth,endMonth));
    };
    var dateData = function(year, month, callback) {
        var startDay = 1;
        var endDay;
        if(year == nowYear&&month ==nowMonth){
            startDay = nowDate;
        }

        if (/^(1|3|5|7|8|10|12)$/.test(month)) {
            endDay = 31;
            if(year == furtureYear&&month ==furtureMonth){
                endDay = furtureDay;
            }
            callback(formatDate(startDay,endDay));
        } else if (/^(4|6|9|11)$/.test(month)) {
            endDay = 30;
            if(year == furtureYear&&month ==furtureMonth){
                endDay = furtureDay;
            }
            callback(formatDate(startDay,endDay));
        } else if (/^2$/.test(month)) {
            if (year % 4 === 0 && year % 100 !== 0 || year % 400 === 0) {
                endDay = 29;
                if(year == furtureYear&&month ==furtureMonth){
                    endDay = furtureDay;
                }
                callback(formatDate(startDay,endDay));
            } else {
                endDay = 28;
                if(year == furtureYear&&month ==furtureMonth){
                    endDay = furtureDay;
                }
                callback(formatDate(startDay,endDay));
            }
        } else {
            throw new Error('month is illegal');
        }
    };
    selectDateDom.bind('click', function() {
        var oneLevelId = showDateDom.attr('data-year');
        var twoLevelId = showDateDom.attr('data-month');
        var threeLevelId = showDateDom.attr('data-date');
        var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
            title: '日期选择',
            itemHeight: 35,
            relation: [1, 1],
            oneLevelId: oneLevelId,
            twoLevelId: twoLevelId,
            threeLevelId: threeLevelId,
            showLoading: true,
            callback: function(selectOneObj, selectTwoObj, selectThreeObj) {
                showDateDom.attr('data-year', selectOneObj.id);
                showDateDom.attr('data-month', selectTwoObj.id);
                showDateDom.attr('data-date', selectThreeObj.id);
                showDateDom.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
                dateValid(selectOneObj.id,selectTwoObj.id,selectThreeObj.id);
            }
        });
    });

    function dateValid(Year,Month,Day){
        if(Month.length<2){
            Month='0'+Month;
        }
        if(Day.length<2){
            Day='0'+Day;
        }
        var dateTime = Year+'-'+Month+'-'+Day;
        $("#bookInstallDate").val(dateTime);

    }
});