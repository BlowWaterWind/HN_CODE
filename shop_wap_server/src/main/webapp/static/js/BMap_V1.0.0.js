var bMap = (function(){
    var map;
    var bmapResult = new Object();
    var myIcon = new BMap.Icon(ctxStatic+"/images/location.png", new BMap.Size(25, 25));
   var init = function init(){

       //坐标系
       var coordinate = $("#bmapAddress").attr("data-coord");
       //回调函数
       var callback = $("#bmapAddress").attr("callback");
       // 地图初始化
       map = new BMap.Map("allmap");
       var point = new BMap.Point(112.974366,28.19854);
       map.centerAndZoom(point,12);
       getCurrentPosition(coordinate);
       map.enableScrollWheelZoom(true);   //启用滚轮放大缩小，默认禁用
       map.enableContinuousZoom(true);    //启用地图惯性拖拽，默认禁用
        // 初始化地图， 设置中心点坐标和地图级别
        // 添加带有定位的导航控件
       var navigationControl = new BMap.NavigationControl({
           // 靠左上角位置
           anchor: BMAP_ANCHOR_TOP_LEFT,
           // LARGE类型
           type: BMAP_NAVIGATION_CONTROL_LARGE,
           // 启用显示定位
           enableGeolocation: true
       });
       map.addControl(navigationControl);
       // 添加定位控件
       var geolocationControl = new BMap.GeolocationControl({
           locationIcon:myIcon
       });
       geolocationControl.addEventListener("locationSuccess", function(e){
           // 定位成功事件
           coordinateChange(e.point,coordinate);
           setAddress(e.point);
       });
       geolocationControl.addEventListener("locationError",function(e){
           // 定位失败事件
           alert(e.message);
       });
       map.addControl(geolocationControl);

        // 百度地图API功能
        //单击获取点击的经纬度

       map.addEventListener("click",function(e){
           //返回BD09坐标系坐标
           coordinateChange(e.point,coordinate);
           map.clearOverlays();
           // 初始化地图， 设置中心点坐标和地图级别
           var marker =new BMap.Marker(e.point,{icon:myIcon});
           //var marker=new BMap.Marker(e.point,{icon:myIcon});
           map.addOverlay(marker);
           setAddress(e.point);
       });

        //百度地图搜索智能提示
       var ac = new BMap.Autocomplete({   //建立一个自动完成的对象
           "input" : "suggestId",
           "location" : map
       });

       ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
           var str = "";
           var _value = e.fromitem.value;
           var value = "";
           if (e.fromitem.index > -1) {
               value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
           }
           str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

           value = "";
           if (e.toitem.index > -1) {
               _value = e.toitem.value;
               value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
           }
           str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
           G("searchResultPanel").innerHTML = str;
       });

       var myValue;
       ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
           var _value = e.item.value;
           myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
           G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
           setPlace(myValue,coordinate);
       });
       //确认选择地址
       $(".bmap-comfirm").click(function(){
           if(bmapResult.address == undefined){
               alert("未选择地址");
               return;
           }
           $(".bmap-div").hide();
           $("#bmapAddress").attr("readOnly","readOnly");
           $("#bmapAddress").val(bmapResult.address);
           eval(callback+"(bmapResult)");

       });
       //解决与商城样式冲突
       $("#suggestId").click(function(){
           $(".tangram-suggestion-main").css("z-index","99");
       });
       $(".bmap-cancel").click(function(){
           $(".bmap-div").hide();
           return;
       });
   }
    //获取当前位置信息
    function getCurrentPosition(coordinate){
        //var geolocation = new BMap.Geolocation();
        //geolocation.getCurrentPosition(function(r){
        //    if(this.getStatus() == BMAP_STATUS_SUCCESS){
        //        console.log("r1:",r);
        //        coordinateChange(r.point,"BD09");
        //        //r.point.lng=bmapResult.lng
        //        //r.point.lat=     bmapResult.lat;
        //        map.clearOverlays();
        //        console.log("r2:",r);
        //        var mk=new BMap.Marker(r.point,{icon:myIcon});
        //        //var mk = new BMap.Marker(r.point);
        //        map.addOverlay(mk);
        //        map.panTo(r.point);
        //        setAddress(r.point);
        //    }
        //    else {
        //        alert('failed'+this.getStatus());
        //    }
        //},{enableHighAccuracy: true});
        //console.log("getCurrentPosition");
        //if (navigator.geolocation)
        //{
        //    navigator.geolocation.getCurrentPosition(showPosition);
        //}else{
        //    alert("Geolocation is not supported by this browser.");
        //}
        if (navigator.geolocation){
            navigator.geolocation.getCurrentPosition(showPosition,showError);
        }else{
            alert("浏览器不支持地理定位。");
        }
    }
    function showError(){
        console.log("22222");
    }
    function showPosition(position) {
        alert("position:"+position);
        //得到html5定位结果
        var x = position.coords.longitude;
        var y = position.coords.latitude;

        //由于html5定位的结果是国际标准gps，所以from=1，to=5
        //下面的代码并非实际是这样，这里只是提供一个思路
        BMap.convgps(x, y, 1, 5, function (convRst) {
            var point = new BMap.Point(convRst.x, convRst.y);

            //这个部分和上面的代码是一样的
            var marker = new BMap.Marker(point);
            map.addOverlay(marker);
            map.panTo(point);
        })

    }
    //选择地址，返回经纬度，精确地址
    function setPlace(myValue,coordinate){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            coordinateChange(pp,coordinate);
            setAddress(pp);
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }
    //坐标转换
    function coordinateChange(point,coordinate){
        if(coordinate =='BD09' ){
            bmapResult.lng = point.lng;
            bmapResult.lat = point.lat;
            //返回GCJ02坐标系坐标
        }else if(coordinate =='GCJ02'){
            var bd09togcj02 = coordtransform.bd09togcj02(point.lng, point.lat);
            bmapResult.lng = bd09togcj02[0];
            bmapResult.lat = bd09togcj02[1];
            //返回WGS84坐标系坐标
        }else if(coordinate =='WGS84'){
            var bd09togcj02 = coordtransform.bd09togcj02(point.lng, point.lat);
            var gcj02towgs84 = coordtransform.gcj02towgs84(bd09togcj02[0], bd09togcj02[1]);
            bmapResult.lng = gcj02towgs84[0];
            bmapResult.lat = gcj02towgs84[1];
        }
    }

    /**
     * 设置选择中文地址
     * @param point
     */
    function setAddress(point){
        var geoc = new BMap.Geocoder();
        geoc.getLocation(point, function(rs){
            //addressComponents对象可以获取到详细的地址信息
            var addComp = rs.addressComponents;
            var site = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
            //将对应的HTML元素设置值
            bmapResult.address = site;

        });
    }
    // 百度地图API功能
    function G(id) {
        return document.getElementById(id);
    }


    return {
        init:init
    }
})(this);
//地图控件触发
$("#bmapAddress").click(function () {
    $(".bmap-div").show();
    bMap.init();
});


/**
 * Created by Wandergis on 2015/7/8.
 * 提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换
 */
//UMD魔法代码
// if the module has no dependencies, the above pattern can be simplified to
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define([], factory);
    } else if (typeof module === 'object' && module.exports) {
        // Node. Does not work with strict CommonJS, but
        // only CommonJS-like environments that support module.exports,
        // like Node.
        module.exports = factory();
    } else {
        // Browser globals (root is window)
        root.coordtransform = factory();
    }
}(this, function () {
    //定义一些常量
    var x_PI = 3.14159265358979324 * 3000.0 / 180.0;
    var PI = 3.1415926535897932384626;
    var a = 6378245.0;
    var ee = 0.00669342162296594323;
    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param bd_lon
     * @param bd_lat
     * @returns {*[]}
     */
    var bd09togcj02 = function bd09togcj02(bd_lon, bd_lat) {
        var bd_lon = +bd_lon;
        var bd_lat = +bd_lat;
        var x = bd_lon - 0.0065;
        var y = bd_lat - 0.006;
        var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
        var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
        var gg_lng = z * Math.cos(theta);
        var gg_lat = z * Math.sin(theta);
        return [gg_lng, gg_lat]
    };

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    var gcj02tobd09 = function gcj02tobd09(lng, lat) {
        var lat = +lat;
        var lng = +lng;
        var z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
        var theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
        var bd_lng = z * Math.cos(theta) + 0.0065;
        var bd_lat = z * Math.sin(theta) + 0.006;
        return [bd_lng, bd_lat]
    };

    /**
     * WGS84转GCj02
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    var wgs84togcj02 = function wgs84togcj02(lng, lat) {
        var lat = +lat;
        var lng = +lng;
        if (out_of_china(lng, lat)) {
            return [lng, lat]
        } else {
            var dlat = transformlat(lng - 105.0, lat - 35.0);
            var dlng = transformlng(lng - 105.0, lat - 35.0);
            var radlat = lat / 180.0 * PI;
            var magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            var sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            var mglat = lat + dlat;
            var mglng = lng + dlng;
            return [mglng, mglat]
        }
    };

    /**
     * GCJ02 转换为 WGS84
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    var gcj02towgs84 = function gcj02towgs84(lng, lat) {
        var lat = +lat;
        var lng = +lng;
        if (out_of_china(lng, lat)) {
            return [lng, lat]
        } else {
            var dlat = transformlat(lng - 105.0, lat - 35.0);
            var dlng = transformlng(lng - 105.0, lat - 35.0);
            var radlat = lat / 180.0 * PI;
            var magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            var sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            var mglat = lat + dlat;
            var mglng = lng + dlng;
            return [lng * 2 - mglng, lat * 2 - mglat]
        }
    };

    var transformlat = function transformlat(lng, lat) {
        var lat = +lat;
        var lng = +lng;
        var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret
    };

    var transformlng = function transformlng(lng, lat) {
        var lat = +lat;
        var lng = +lng;
        var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret
    };

    /**
     * 判断是否在国内，不在国内则不做偏移
     * @param lng
     * @param lat
     * @returns {boolean}
     */
    var out_of_china = function out_of_china(lng, lat) {
        var lat = +lat;
        var lng = +lng;
        // 纬度3.86~53.55,经度73.66~135.05
        return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55);
    };

    return {
        bd09togcj02: bd09togcj02,
        gcj02tobd09: gcj02tobd09,
        wgs84togcj02: wgs84togcj02,
        gcj02towgs84: gcj02towgs84
    }
}));
