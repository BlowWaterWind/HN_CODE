var nameEl = document.getElementById('picker2');

var first = [];
/* 地市 */
var second = [];
/* 区县 */

var selectedIndex = [0, 0];
/* 默认选中的地区 */
var checked = [0, 0];


function creatList(obj, list) {
    obj.forEach(function (item, index, arr) {
        var temp = new Object();
        temp.text = item.name;
        temp.value = index;
        list.push(temp);
    })
}

/* 已选选项 */
$(function () {
    //请求全量地址信息
    $.ajax({
        type: "POST",
        url: ctx + "simBuy/getSimCityCnty",
        dataType: "json",
        success: function (data) {

            creatList(data, first);

            if (data[selectedIndex[0]].hasOwnProperty('tdEcOrgInfos')) {
                creatList(data[selectedIndex[0]].tdEcOrgInfos, second);
            } else {
                second = [{
                    text: '',
                    value: 0
                }];
            }
        },
        error: function (t) {
            $("#select-number ul").html("很抱歉，暂无符合要求的号码！");
        }
    });

    var picker = new Picker({
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

    $("#picker2").on("click",function(){
        picker.show();
    });


});



