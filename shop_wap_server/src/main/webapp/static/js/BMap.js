var map, geolocation, coordinate;
var bMap = (function () {
    console.log("init");

    var bmapResult = new Object();
    //var myIcon = new BMap.Icon(ctxStatic+"/images/location.png", new BMap.Size(25, 25));
    var init = function () {
        var url = 'https://cache.amap.com/lbs/static/addToolbar.js';
        var jsapi = document.createElement('script');
        jsapi.charset = 'utf-8';
        jsapi.src = url;
        document.head.appendChild(jsapi);
        //坐标系
        coordinate = $("#bmapAddress").attr("data-coord");
        //回调函数
        var callback = $("#bmapAddress").attr("callback");
        // 地图初始化
        map = new AMap.Map('allmap', {
            resizeEnable: true
        });
        map.on('click', function (e) {
            mapMark(e.lnglat.getLng(), e.lnglat.getLat());
        });
        map.plugin('AMap.Geolocation', function () {
            geolocation = new AMap.Geolocation({
                enableHighAccuracy: true,//是否使用高精度定位，默认:true
                timeout: 10000,          //超过10秒后停止定位，默认：无穷大
                buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
                zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
                buttonPosition: 'RB',
                //noGeoLocation: 3,
                useNative:true,
                //noIpLocate:3,
                showMarker: false
            });
            map.addControl(geolocation);
            geolocation.getCurrentPosition();
            AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
            AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
        });
//输入提示
        var autoOptions = {
            input: "suggestId"
        };
        var auto = new AMap.Autocomplete(autoOptions);
        var placeSearch = new AMap.PlaceSearch({
            map: map,
            pageSize: 1,
            showCover: false
        });  //构造地点查询类
        AMap.event.addListener(auto, "select", select);//注册监听，当选中某条记录时会触发
        function select(e) {
            placeSearch.setCity(e.poi.adcode);
            placeSearch.search(e.poi.name, function (status, result) {
                var address = result.poiList.pois[0].cityname + result.poiList.pois[0].adname + result.poiList.pois[0].name;
                mapMark(result.poiList.pois[0].location.lng, result.poiList.pois[0].location.lat, address);
                <!-- map.clearMap(); -->
            });  //关键字查询查询

        }

        //确认选择地址
        $(".bmap-comfirm").click(function () {
            if (bmapResult.address == undefined) {
                alert("未选择地址");
                return;
            }
            $(".bmap-div").hide();
            $("#bmapAddress").attr("readOnly", "readOnly");
            $("#bmapAddress").val(bmapResult.address);
            eval(callback + "(bmapResult)");

        });
        //解决与商城样式冲突
        $("#suggestId").click(function () {
            $(".tangram-suggestion-main").css("z-index", "99");
        });
        $(".bmap-cancel").click(function () {
            $(".bmap-div").hide();
            return;
        });
    }

    //解析定位结果
    function onComplete(data) {
        console.log("data:", data);
        mapMark(data.position.lng, data.position.lat, data.formattedAddress);
    }

    function mapMark(lng, lat, address) {
        map.clearMap();
        new AMap.Marker({
            map: map,
            position: [lng, lat],
            icon: new AMap.Icon({
                size: new AMap.Size(40, 50),  //图标大小
                image: ctxStatic + "/images/location.png"
                //imageOffset: new AMap.Pixel(0, -60)
            })
        })
        coordinateChange(lng, lat, coordinate);
        if (address != null && address.length > 0) {
            bmapResult.address = address;
        }
    }

    //解析定位错误信息
    function onError(data) {
        //alert(JSON.stringify(data));
        document.getElementById('tip').innerHTML = '定位失败';
    }

    //坐标转换
    function coordinateChange(lng, lat, coordinate) {
        if (coordinate == 'BD09') {
            var bd09togcj02 = coordtransform.gcj02tobd09(lng, lat);
            bmapResult.lng = bd09togcj02[0];
            bmapResult.lat = bd09togcj02[1];
            //返回GCJ02坐标系坐标
        } else if (coordinate == 'GCJ02') {
            var bd09togcj02 = coordtransform.bd09togcj02(point.lng, point.lat);
            bmapResult.lng = lng;
            bmapResult.lat = lat;
            //返回WGS84坐标系坐标
        } else if (coordinate == 'WGS84') {
            var gcj02towgs84 = coordtransform.gcj02towgs84(lng, lat);
            bmapResult.lng = gcj02towgs84[0];
            bmapResult.lat = gcj02towgs84[1];
        }
    }


    return {
        init: init
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
