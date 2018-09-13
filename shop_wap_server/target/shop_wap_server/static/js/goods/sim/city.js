    // 号码归属选择
    var data1 = [{
        text: '长沙',
        value: '长沙'
    }, {
        text: '株洲',
        value: '株洲'
    }, {
        text: '湘潭',
        value: '湘潭'
    }, {
        text: '衡阳',
        value: '衡阳'
    }, {
        text: '邵阳',
        value: '邵阳'
    }, {
        text: '岳阳',
        value: '岳阳'
    }, {
        text: '常德',
        value: '常德'
    }, {
        text: '张家界',
        value: '张家界'
    }, {
        text: '益阳',
        value: '益阳'
    }, {
        text: '郴州',
        value: '郴州'
    }, {
        text: '永州',
        value: '永州'
    }, {
        text: '怀化',
        value: '怀化'
    }, {
        text: '娄底',
        value: '娄底'
    }, {
        text: '湘西土家族苗族自治州',
        value: '湘西土家族苗族自治州'
    }];

    var picker1El = document.getElementById('picker1');

    var picker1 = new Picker({
        data: [data1]
    });

    picker1.on('picker.select', function(selectedVal, selectedIndex) {
        picker1El.innerText = data1[selectedIndex[0]].text;
    });
    picker1El.addEventListener('click', function() {
        picker1.show();
    });

    // 选择区县
    var data2 = [{
            text: "岳麓区",
            value: "岳麓区"
        },
        {
            text: "芙蓉区",
            value: "芙蓉区"
        },
        {
            text: "天心区",
            value: "天心区"
        },
        {
            text: "开福区",
            value: "开福区"
        },
        {
            text: "雨花区",
            value: "雨花区"
        },
        {
            text: "浏阳市",
            value: "浏阳市"
        },
        {
            text: "长沙县",
            value: "长沙县"
        },
        {
            text: "望城县",
            value: "望城县"
        },
        {
            text: "宁乡县",
            value: "宁乡县"
        }
    ];

    var picker2El = document.getElementById('picker2');

    var picker2 = new Picker({
        data: [data2]
    });

    picker2.on('picker.select', function(selectedVal, selectedIndex) {
        picker2El.innerText = data2[selectedIndex[0]].text;
    });
    picker2El.addEventListener('click', function() {
        picker2.show();
    });

var city = [{
  "name": "长沙",
  "sub": [{
    "name": "岳麓区"
  }, {
    "name": "芙蓉区"
  }, {
    "name": "天心区"
  }, {
    "name": "开福区"
  }, {
    "name": "雨花区"
  }, {
    "name": "浏阳市"
  }, {
    "name": "长沙县"
  }, {
    "name": "望城县"
  }, {
    "name": "宁乡县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "株洲",
  "sub": [{
    "name": "天元区"
  }, {
    "name": "荷塘区"
  }, {
    "name": "芦淞区"
  }, {
    "name": "石峰区"
  }, {
    "name": "醴陵市"
  }, {
    "name": "株洲县"
  }, {
    "name": "炎陵县"
  }, {
    "name": "茶陵县"
  }, {
    "name": "攸县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "湘潭",
  "sub": [{
    "name": "岳塘区"
  }, {
    "name": "雨湖区"
  }, {
    "name": "湘乡市"
  }, {
    "name": "韶山市"
  }, {
    "name": "湘潭县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "衡阳",
  "sub": [{
    "name": "雁峰区"
  }, {
    "name": "珠晖区"
  }, {
    "name": "石鼓区"
  }, {
    "name": "蒸湘区"
  }, {
    "name": "南岳区"
  }, {
    "name": "耒阳市"
  }, {
    "name": "常宁市"
  }, {
    "name": "衡阳县"
  }, {
    "name": "衡东县"
  }, {
    "name": "衡山县"
  }, {
    "name": "衡南县"
  }, {
    "name": "祁东县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "邵阳",
  "sub": [{
    "name": "双清区"
  }, {
    "name": "大祥区"
  }, {
    "name": "北塔区"
  }, {
    "name": "武冈市"
  }, {
    "name": "邵东县"
  }, {
    "name": "洞口县"
  }, {
    "name": "新邵县"
  }, {
    "name": "绥宁县"
  }, {
    "name": "新宁县"
  }, {
    "name": "邵阳县"
  }, {
    "name": "隆回县"
  }, {
    "name": "城步苗族自治县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "岳阳",
  "sub": [{
    "name": "岳阳楼区"
  }, {
    "name": "云溪区"
  }, {
    "name": "君山区"
  }, {
    "name": "临湘市"
  }, {
    "name": "汨罗市"
  }, {
    "name": "岳阳县"
  }, {
    "name": "湘阴县"
  }, {
    "name": "平江县"
  }, {
    "name": "华容县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "常德",
  "sub": [{
    "name": "武陵区"
  }, {
    "name": "鼎城区"
  }, {
    "name": "津市市"
  }, {
    "name": "澧县"
  }, {
    "name": "临澧县"
  }, {
    "name": "桃源县"
  }, {
    "name": "汉寿县"
  }, {
    "name": "安乡县"
  }, {
    "name": "石门县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "张家界",
  "sub": [{
    "name": "永定区"
  }, {
    "name": "武陵源区"
  }, {
    "name": "慈利县"
  }, {
    "name": "桑植县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "益阳",
  "sub": [{
    "name": "赫山区"
  }, {
    "name": "资阳区"
  }, {
    "name": "沅江市"
  }, {
    "name": "桃江县"
  }, {
    "name": "南县"
  }, {
    "name": "安化县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "郴州",
  "sub": [{
    "name": "北湖区"
  }, {
    "name": "苏仙区"
  }, {
    "name": "资兴市"
  }, {
    "name": "宜章县"
  }, {
    "name": "汝城县"
  }, {
    "name": "安仁县"
  }, {
    "name": "嘉禾县"
  }, {
    "name": "临武县"
  }, {
    "name": "桂东县"
  }, {
    "name": "永兴县"
  }, {
    "name": "桂阳县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "永州",
  "sub": [{
    "name": "冷水滩区"
  }, {
    "name": "零陵区"
  }, {
    "name": "祁阳县"
  }, {
    "name": "蓝山县"
  }, {
    "name": "宁远县"
  }, {
    "name": "新田县"
  }, {
    "name": "东安县"
  }, {
    "name": "江永县"
  }, {
    "name": "道县"
  }, {
    "name": "双牌县"
  }, {
    "name": "江华瑶族自治县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "怀化",
  "sub": [{
    "name": "鹤城区"
  }, {
    "name": "洪江市"
  }, {
    "name": "会同县"
  }, {
    "name": "沅陵县"
  }, {
    "name": "辰溪县"
  }, {
    "name": "溆浦县"
  }, {
    "name": "中方县"
  }, {
    "name": "新晃侗族自治县"
  }, {
    "name": "芷江侗族自治县"
  }, {
    "name": "通道侗族自治县"
  }, {
    "name": "靖州苗族侗族自治县"
  }, {
    "name": "麻阳苗族自治县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "娄底",
  "sub": [{
    "name": "娄星区"
  }, {
    "name": "冷水江市"
  }, {
    "name": "涟源市"
  }, {
    "name": "新化县"
  }, {
    "name": "双峰县"
  }, {
    "name": "其他"
  }],
  "type": 0
}, {
  "name": "湘西土家族苗族自治州",
  "sub": [{
    "name": "吉首市"
  }, {
    "name": "古丈县"
  }, {
    "name": "龙山县"
  }, {
    "name": "永顺县"
  }, {
    "name": "凤凰县"
  }, {
    "name": "泸溪县"
  }, {
    "name": "保靖县"
  }, {
    "name": "花垣县"
  }, {
    "name": "其他"
  }],
  "type": 0
}];


var nameEl = document.getElementById('picker2');

var first = []; /* 地市 */
var second = []; /* 区县 */

var selectedIndex = [0, 0]; /* 默认选中的地区 */

var checked = [0, 0]; /* 已选选项 */

function creatList(obj, list) {
  obj.forEach(function(item, index, arr) {
    var temp = new Object();
    temp.text = item.name;
    temp.value = index;
    list.push(temp);
  })
}

creatList(city, first);

if (city[selectedIndex[0]].hasOwnProperty('sub')) {
  creatList(city[selectedIndex[0]].sub, second);
} else {
  second = [{
    text: '',
    value: 0
  }];
}

var picker = new Picker({
  data: [first, second],
  selectedIndex: selectedIndex
});

picker.on('picker.select', function(selectedVal, selectedIndex) {
  var text1 = first[selectedIndex[0]].text;
  var text2 = second[selectedIndex[1]].text;
  nameEl.innerText = text1 + ' ' + text2 + ' ';
});

picker.on('picker.change', function(index, selectedIndex) {
  if (index === 0) {
    firstChange();
  }

  function firstChange() {
    second = [];
    checked[0] = selectedIndex;
    var firstCity = city[selectedIndex];
    if (firstCity.hasOwnProperty('sub')) {
      creatList(firstCity.sub, second);

      var secondCity = city[selectedIndex].sub[0]
      if (secondCity.hasOwnProperty('sub')) {
        creatList(secondCity.sub);
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

nameEl.addEventListener('click', function() {
  picker.show();
});