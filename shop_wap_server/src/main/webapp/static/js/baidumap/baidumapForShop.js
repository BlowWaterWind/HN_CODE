/**
 * Created by lufm on 2016/4/14.
 */
//模块定义
function isWeiXin(){
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    }else{
        return false;
    }
}
function isInstalled(reqUrl,downLoadUrl){
	try{
		var ifr = document.createElement('iframe');
		ifr.src=reqUrl;
		ifr.style.display = 'none';
		document.body.appendChild(ifr);
		setTimeout(function(){
			document.body.removeChild(ifr);
			window.location=downLoadUrl;//如果超时就跳转到app下载页
		},3000);
	}catch (e) {
		// TODO: handle exception
		
	}
}
//获取所在城市,去掉“市”
function tirmCity(cityName) {
	if(cityName != null && typeof(cityName) != "undefined"){
		cityName = cityName.replace('市','');
	}
	return cityName;
}
//获取所在城市地址
function getCityUrl(la,lo) {
	// 配置Baidu Geocoding API
	var url = "http://api.map.baidu.com/geocoder/v2/?ak=C93b5178d7a8ebdb830b9b557abce78b"
			+ "&callback=renderReverse"
			+ "&location="
			+ la
			+ ","
			+ lo
			+ "&output=json" + "&pois=0";
	return url;
}
//调用app由掌厅拉取百度地图app
function callToBaiduMapApp(url){
    var u = navigator.userAgent;
    var isApp = u.indexOf('GxMobileEhallAppMessenger')>-1;
	if(isApp){//判断是否是app嵌套
		try{
			var isAndroid = u.indexOf('Android')>-1;
			if(isAndroid){
				window.js_invoke.goBaiduMapsApp(url);
				return;
			} else {
				location.href = 'JSProtocol::goBaiduMapsApp'+url; //ios调用
				return;
			}
		} catch(e){
			throw '系统繁忙，请稍候重试！';
		}
    } else {
        
    }
}

var baidu_map = {
     name:"百度地图"  //定义模块变量
    ,EARTH_RADIUS : 6378137.0    //单位M
    ,PI : Math.PI
    ,location_x:0
    ,location_y:0
    ,locate_flag : "false"
    ,show_alert : "true"
    ,isAndroid:(navigator.userAgent.indexOf('Android') > -1)
    ,isWeixin:(navigator.userAgent.toLowerCase().indexOf('micromessenger') > -1)
    ,isIos:(navigator.userAgent.toLowerCase().indexOf('iphone') > -1 || navigator.userAgent.toLowerCase().indexOf('ipad') > -1)
    ,isApp:(navigator.userAgent.indexOf('GxMobileEhallAppMessenger') > -1)
    ,callbackfun:function(){}
    ,getRad : function(d){
        return d * this.PI / 180.0;
    }
    ,init:function(){
        //初始化
    }
    ,help:function(){
        //alert(this.name);
    }
    ,test:function(){
        //
        if(!baidu_map.isIos){
            alert("is not ios");
            //
        }
        else{
            alert("iphone or ipad");
            document.location = 'JSProtocol::getLocation';
            //此处调用
        }
    }
    ,showPosition:function(position){
        var x0 = position.point.lng;
        var y0 = position.point.lat;
       // alert("showPosition x0:" + x0 + " y0:" + y0);  //debug
        var gpsPoint = new BMap.Point(x0,y0);
        //真实经纬度转成百度坐标
        BMap.Convertor.translate(gpsPoint,0,function(point0){
        	var lo = $('#LONGITUDE').val();
	        var la = $('#LATITUDE').val();
	        var name = $('#SHOPNAME').val();
	        var addr = $('#SHOPADDR').val();
	        var shopid = $('#SHOPID').val();
	        var x = point0.lng;
	        var y = point0.lat;
	        if(baidu_map.isApp && checkVersion()){// 炎黄接口返回的定位坐标已转换为百度坐标,此处直接引用
	        	x=x0;
	        	y=y0;
	        }
	        var dis = baidu_map.getGreatCircleDistance(la,lo ,y ,x);
	        var map = new BMap.Map("container");
	        var point = new BMap.Point(lo, la);
	        map.centerAndZoom(point, 18);
	        //var origin_region = getPositionCity(y,x);
	        //var destination_region = getPositionCity(la,lo);
	        // 配置Baidu Geocoding API
			var url1 = getCityUrl(y,x);
			$.ajax({
				type : "GET",
				dataType : "jsonp",
				url : url1,
				success : function(json1) {
					if (json1 != null && typeof(json1) != "undefined" && json1.status == "0") {
						var city1=tirmCity(json1.result.addressComponent.city);// 获取到起点地市
						// 配置Baidu Geocoding API
						var url2 = getCityUrl(la,lo);
						$.ajax({
							type : "GET",
							dataType : "jsonp",
							url : url2,
							success : function(json2) {
								if (json2 != null && typeof(json2) != "undefined" && json2.status == "0") {
									var city2=tirmCity(json2.result.addressComponent.city);// 获取到终点地市
									var urlParams = 
							        	"?origin=latlng:"+y+","+x+"|当前位置&destination=latlng:"+la+","+lo+"|"+name+"&mode=driving&origin_region="+city1+"&destination_region="+city2+"&output=html&src=yourCompanyName|yourAppName";
							        // 目前支持微信，ios版浏览器
							        var	dhUrl = "http://api.map.baidu.com/direction"+urlParams;
							        // android版系统浏览器调用
							        if (baidu_map.isAndroid && !isWeiXin() &&!baidu_map.isApp){
							        	//dhUrl = "bdapp://map/direction"+urlParams;
							        	var browser_url = "bdapp://map/direction"+urlParams;
							        	dhUrl = "javascript:isInstalled(\""+browser_url+"\",\""+dhUrl+"\");"
							        	
							        }
							        if(baidu_map.isApp){// app在4.11版本以上可用
							        	if(checkVersion()){
									        // 掌厅android
									        if(baidu_map.isAndroid){
									        	var url = "intent://map/direction" + urlParams + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
									        	dhUrl = "javascript:callToBaiduMapApp(\""+url+"\");";
									        }
									        // 掌厅ios
									        if(baidu_map.isIos){
									        	var ios_url = "baidumap://map/direction"+urlParams;
									        	dhUrl = "javascript:callToBaiduMapApp(\""+ios_url+"\");";
									        }
							        	}else{
							        		dhUrl = "javascript:baidu_map.locate(baidu_map.showPath);";
							        	}
							        }
							      //  alert(dhUrl);
							        var sContent ="<div width='200px'><div style='margin-top:15px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'><b>"
							            +name
							            +"</b><br/><img src='/shop/static/images/locate.jpg' style='width:38px;display:inline-block;vertical-align: middle;' />"+ Number(dis/1000).toFixed(2) +"KM  "+addr+"</div>"
							            +"<div>"
							            +"<a href='"+dhUrl+"' target='_blank'><img style='margin-left:20px;width:45px;height:45px;vertical-align:middle;' src='/shop/static/images/path.png'><div style='width:45%;display:inline;color:#666666;'>店铺导航</div></a></div>"
							            +"</div>";
							        //var infoWindow = new BMap.InfoWindow(sContent,{InfoWindowOptions:{enableCloseOnClick:false}});  // 创建信息窗口对象
							        var infoWindow = new BMap.InfoWindow(sContent,{enableCloseOnClick:false});  // 创建信息窗口对象
							        map.openInfoWindow(infoWindow,point); //开启信息窗口
								}
							}
						});
					}
				}
			});
         }); 
    }
    ,showPath:function(position){
        var x0 = position.coords.longitude;
        var y0 = position.coords.latitude;
        //alert("showPath x0:" + x0 + " y0:" + y0);  //debug
        var gpsPoint0 = new BMap.Point(x0,y0);
        //真实经纬度转成百度坐标
        BMap.Convertor.translate(gpsPoint0,0,function(point0){
        	var x = point0.lng;
	        var y = point0.lat;
	        var lo = $('#LONGITUDE').val();
	        var la = $('#LATITUDE').val();
       		var name = $('#SHOPNAME').val();
	
	        var map = new BMap.Map("container");
	        var p1 = new BMap.Point(lo,la);
	        var p2 = new BMap.Point(x,y);
	
	        map.centerAndZoom(p1, 11);
	        var marker = new BMap.Label(name);
	        marker.setPosition(p1);
	        map.addOverlay(marker);
	        
	        var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});
			driving.search(p1, p2);
	
	         driving.setMarkersSetCallback(function(){
	             $(".BMap_Marker").hide();
	         });
        });    
    }
    ,locate:function(callback){
            //app调用炎黄接口，其他走html5
            if(baidu_map.isApp){
                baidu_map.callbackfun = callback;
                //ios和安卓分别调用炎黄的接口
                if(baidu_map.isIos && checkVersion()){
                	//alert("炎黄IOS定位");
                	document.location = 'JSProtocol::getLocation';
            	}
            	else if(checkVersion()){ //大于4.11新版本才有接口
            		//alert("炎黄android定位");
	            	window.js_invoke.getLocation();
            	}
            	else{
            		//alert("炎黄app其他定位");
    	            if (navigator.geolocation) {
    	            	var config = {
    		                enableHighAccuracy: true,//是否启用高精确度模
    		                timeout: 5000,
    		                maximumAge: 30000
                		};
                    //	navigator.geolocation.getCurrentPosition(callback, this.showError,config);
                        var geolocation = new BMap.Geolocation();
                        geolocation.getCurrentPosition(callback, this.showError,config);
    				}
                  	else{
    	              	alert("浏览器不支持html5定位接口");
                  	}            		
            	}
            }
            else{ //其他走html5定位接口
            	//alert("其他定位");
				if (navigator.geolocation) {
	            	var config = {
		                enableHighAccuracy: true,//是否启用高精确度模
		                timeout: 5000,
		                maximumAge: 30000
            		};
          			//navigator.geolocation.getCurrentPosition(callback, this.showError, config);
                    var geolocation = new BMap.Geolocation();
                    geolocation.getCurrentPosition(callback, this.showError,config);
      			}
      			else{
	      			alert("浏览器不支持html5定位接口");
      			}
            }
    }
    ,getLocation: function () {
            //满足是IOS版APP才调用JSProtocol
            if(baidu_map.isApp){
                baidu_map.callbackfun = baidu_map.setLocation;
                //ios和安卓分别调用炎黄的接口
                if(baidu_map.isIos&&checkVersion()){
                	document.location = 'JSProtocol::getLocation';
            	}
            	else if(checkVersion()){ //大于4.11新版本才有接口
	            	window.js_invoke.getLocation();
            	}
            	else{
    	            if (navigator.geolocation) {
    	            	var config = {
    		                enableHighAccuracy: true,//是否启用高精确度模
    		                timeout: 5000,
    		                maximumAge: 30000
                		};
                        var geolocation = new BMap.Geolocation();
                    	geolocation.getCurrentPosition(this.setLocation, this.showError,config);
    				}
                  	else{
    	              	alert("浏览器不支持html5定位接口");
                  	}            		
            	}
            }
            else{
	            if (navigator.geolocation) {
	            	var config = {
		                enableHighAccuracy: true,//是否启用高精确度模
		                timeout: 5000,
		                maximumAge: 30000
            		};
					//BMap定位方法
                        var geolocation = new BMap.Geolocation();
						geolocation.getCurrentPosition(this.setLocation, this.showError,config);

				}
              	else{
	              	alert("浏览器不支持html5定位接口");
              	}
            }
    }
    //设置当前坐标
    ,setLocation:function(position){
    	// ==========真实经纬度转成百度坐标 start ==========
		//alert(this.getStatus());
        if(this.getStatus() == BMAP_STATUS_SUCCESS) {
            var x0 = position.point.lng;
            var y0 = position.point.lat;
           // alert("setLocation x0:" + x0 + " y0:" + y0);  //debug
            var callbackName = 'cbk_' + Math.round(Math.random() * 10000);    //随机函数名
            var xyUrl = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=" + x0 + "&y=" + y0 + "&callback=BMap.Convertor." + callbackName;
            var head = document.getElementsByTagName('head')[0];
            var script = document.createElement('script');
            script.type = 'text/javascript';
            script.src = xyUrl;
            //借鉴了jQuery的script跨域方法
            script.onload = script.onreadystatechange = function () {
                if ((!this.readyState || this.readyState === "loaded" || this.readyState === "complete")) {
                    callback && callback();
                    // Handle memory leak in IE
                    script.onload = script.onreadystatechange = null;
                    if (head && script.parentNode) {
                        head.removeChild(script);
                    }
                }
            };
            // Use insertBefore instead of appendChild  to circumvent an IE6 bug.
            head.insertBefore(script, head.firstChild);

            BMap.Convertor[callbackName] = function (xyResult) {
                delete BMap.Convertor[callbackName];    //调用完需要删除改函数
                var point = new BMap.Point(xyResult.x, xyResult.y);
                $("#LONGITUDE").val(point.lng);
                $("#LATITUDE").val(point.lat);
                //载入商品列表，显示商品
                // if(typeof(setOrder) != "undefined"){
                //     setOrder('disOrder');
                // }
            }
        }
	    // ==========真实经纬度转成百度坐标 end ==========
    }
    ,showError:function (error) {
        // 获取发生错误，展示首页内容
    	if(baidu_map.show_alert == "true"){
    		alert("湖南移动商城已被禁止权限：读取位置信息。可在手机设置-权限管理重新授权。");
    		baidu_map.show_alert = "false";
    	}
		
		/*定位错误统一弹框
        switch (error.code) {
            case "3" :   //安卓版app定位禁止的时候错误码是3
                alert("广西移动商城已被禁止权限：读取位置信息。可在手机设置-权限管理重新授权。");
                break;
            case error.PERMISSION_DENIED :
                alert("广西移动商城已被禁止权限：读取位置信息。可在手机设置-权限管理重新授权。");
                // x.innerHTML = "User denied the request for
                // Geolocation.[用户拒绝请求地理定位]"
                break;
            case error.POSITION_UNAVAILABLE :
                //alert("定位失败,位置信息是不可用");
                // x.innerHTML = "Location information is unavailable.[位置信息是不可用]"
                break;
            case error.TIMEOUT :
                //alert("定位失败,请求获取用户位置超时");
                // x.innerHTML = "The request to get user location timed
                // out.[请求获取用户位置超时]"
                break;
            case error.UNKNOWN_ERROR :
                //alert("定位失败,定位系统失效");
                // x.innerHTML = "An unknown error occurred.[未知错误]"
                break;
        }*/
        //定位失败载入商品列表
        // if(typeof(setOrder) != "undefined"){
        //     setOrder('');
        // }
    }
    ,getGreatCircleDistance :function(lat1,lng1,lat2,lng2){
        var radLat1 = this.getRad(lat1);
        var radLat2 = this.getRad(lat2);

        var a = radLat1 - radLat2;
        var b = this.getRad(lng1) - this.getRad(lng2);

        var s = 2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s*this.EARTH_RADIUS;
        s = Math.round(s*10000)/10000.0;

        return s;

    }
    ,quickSort: function(array,macher) {
        //比较函数，如果未定义则设问默认
        if(macher == undefined){
            macher = this.compareTo;
        }
        var i = 0;
        var j = array.length - 1;
        var Sort = function(i, j) {

            // 结束条件
            if (i == j) {
                return
            };

            var key = array[i];
            var stepi = i; // 记录开始位置
            var stepj = j; // 记录结束位置

            while (j > i) {
                // j <<-------------- 向前查找
                if (macher(array[j],key)>=0) {
                    j--;
                } else {
                    array[i] = array[j]
                    //i++ ------------>>向后查找
                    while (j > ++i) {
                        if ( macher(array[i],key)>0 ) {
                            array[j] = array[i];
                            break;
                        }
                    }
                }
            }
            // 如果第一个取出的 key 是最小的数
            if (stepi == i) {
                Sort(++i, stepj);
                return;
            }
            // 最后一个空位留给 key
            array[i] = key;
            // 递归
            Sort(stepi, i);
            Sort(j, stepj);
        }
        Sort(i, j);
        return array;
    }
    ,compareTo:function(a,b){
        if(a > b){
            return 1;
        }
        else if(a == b){
            return 0;
        }
        else{
            return -1;
        }
    }
}
baidu_map.init();

function getLocationIOS(longitude, latitude){
    //alert("--经度--"+ longitude + "--纬度--"+ latitude);
    //longitude == kCLErrorDomain  当系统禁用定位功能的时候提示用户
    if(longitude == "kCLErrorDomain"){
    	
    	if(baidu_map.show_alert == "true"){
    		alert("湖南移动商城已被禁止权限：读取位置信息。可在手机设置-权限管理重新授权。");
    		baidu_map.show_alert = "false";
    	}
    	
        // if(typeof(setOrder) != "undefined"){
        //     setOrder('');
        // }
        return;
    }
    //end
    var pos  = {};
    pos.coords = {};
    pos.coords.longitude = longitude;
    pos.coords.latitude = latitude;

    if(longitude == 0 || latitude == 0){  //定位失败的时候返回0
        //alert("定位失败，载入无排序默认商品列表");
        //定位失败载入商品列表
        // if(typeof(setOrder) != "undefined"){
        //     setOrder('');
        // }
    }
    else{
        baidu_map.callbackfun(pos);
    }
}

/**
 * app版本在4.11以上可用
 * @returns {Boolean}
 */
function checkVersion(){
	// 版本号：如果是ios则返回ios@4.10,如果是android则返回android@4.10
	var cliver = "android@4.11";
	try{
		cliver = window.js_invoke.getCliver();
	} catch(e){
		cliver = "android@4.11";
	}
    //判断app是否是V4.11以后版本
	var cliverf = cliver.substring(cliver.indexOf("@")+1);
    if(parseFloat(cliverf)>4.11){
		return true;
    } else {
        return false;
    }
}
