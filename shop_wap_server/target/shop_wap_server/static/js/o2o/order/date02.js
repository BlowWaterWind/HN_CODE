$(function() {
var showDateDom02 = $('#Date02');
        // 初始化时间
    var now = new Date();
    var nowYear = now.getFullYear();
    var nowMonth = now.getMonth() + 1;
    var nowDate = now.getDate();
    var endTime=$("#endTime").val();
    if(endTime!=null&&endTime.length>0){
        var dateData=endTime.split('-');
        showDateDom02.attr('data-year', dateData[0]);
        showDateDom02.attr('data-month', dateData[1]);
        showDateDom02.attr('data-date', dateData[2]);
        $("#endTime").val(endTime);
    }else{
        showDateDom02.attr('data-year', nowYear);
        showDateDom02.attr('data-month', nowMonth);
        showDateDom02.attr('data-date', nowDate);
        $("#endTime").val(nowYear+'-'+nowMonth+'-'+nowDate);
    }
    showDateDom02.html(showDateDom02.attr('data-year') + '年 ' + showDateDom02.attr('data-month') + '月 ' + showDateDom02.attr('data-date')+'日');

        //
        //showDateDom02.attr('data-year', nowYear);
        //showDateDom02.attr('data-month', nowMonth);
        //showDateDom02.attr('data-date', nowDate);
        // 数据初始化
        function formatYear(nowYear) {
            var arr = [];
            for (var i = nowYear - 1; i <= nowYear ; i++) {
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
        var yearData = function(callback) {
            callback(formatYear(nowYear))
        }
        var monthData = function(year, callback) {
            callback(formatMonth());
        };
        var dateData = function(year, month, callback) {
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
        showDateDom02.bind('click', function() {
            var oneLevelId = showDateDom02.attr('data-year');
            var twoLevelId = showDateDom02.attr('data-month');
            var threeLevelId = showDateDom02.attr('data-date');
            var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
                itemHeight: 35,
                showLoading: true,
                relation: [1, 1],
                oneLevelId: oneLevelId,
                twoLevelId: twoLevelId,
                threeLevelId: threeLevelId,
                callback: function(selectOneObj, selectTwoObj, selectThreeObj) {
                    showDateDom02.attr('data-year', selectOneObj.id);
                    showDateDom02.attr('data-month', selectTwoObj.id);
                    showDateDom02.attr('data-date', selectThreeObj.id);
                    showDateDom02.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
                    $("#endTime").val(selectOneObj.id+'-'+selectTwoObj.id+'-'+selectThreeObj.id);
                }
            });
        });
});