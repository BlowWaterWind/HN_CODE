$(function() {
       var showDateDom = $('#Date01');
        // 初始化时间
    var now = new Date();
    var nowYear = now.getFullYear();
    var nowMonth = now.getMonth() + 1;
    var nowDate = now.getDate();
        var startTime=$("#startTime").val();
        if(startTime!=null&&startTime.length>0){
            var dateData=startTime.split('-');
            showDateDom.attr('data-year', dateData[0]);
            showDateDom.attr('data-month', dateData[1]);
            showDateDom.attr('data-date', dateData[2]);
            $("#startTime").val(startTime);
        }else{
            showDateDom.attr('data-year', nowYear);
            showDateDom.attr('data-month', nowMonth);
            showDateDom.attr('data-date', nowDate);
            $("#startTime").val(nowYear+'-'+nowMonth+'-'+nowDate);
        }
        showDateDom.html(showDateDom.attr('data-year') + '年 ' + showDateDom.attr('data-month') + '月 ' + showDateDom.attr('data-date')+'日');

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
        showDateDom.bind('click', function() {
            var oneLevelId = showDateDom.attr('data-year');
            var twoLevelId = showDateDom.attr('data-month');
            var threeLevelId = showDateDom.attr('data-date');
            var iosSelect = new IosSelect(3, [yearData, monthData, dateData], {
                itemHeight: 35,
                showLoading: true,
                relation: [1, 1],
                oneLevelId: oneLevelId,
                twoLevelId: twoLevelId,
                threeLevelId: threeLevelId,
                callback: function(selectOneObj, selectTwoObj, selectThreeObj) {
                    showDateDom.attr('data-year', selectOneObj.id);
                    showDateDom.attr('data-month', selectTwoObj.id);
                    showDateDom.attr('data-date', selectThreeObj.id);
                    showDateDom.html(selectOneObj.value + ' ' + selectTwoObj.value + ' ' + selectThreeObj.value);
                    $("#startTime").val(selectOneObj.id+'-'+selectTwoObj.id+'-'+selectThreeObj.id);
                }
            });
        });

});