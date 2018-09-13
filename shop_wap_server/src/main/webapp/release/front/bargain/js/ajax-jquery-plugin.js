!(function($){
	var _ctxurl = "/shop";
	var _checkMethod,_ctxurl,_defaultEff;
	var _checkMethod = new Object();
	var _effect=[];
	var _defaultFn=[];
	var chanId = getQueryString("CHANID");
	var serialNumber = getQueryString("serialNumber");
	var uid = getQueryString("UID");
	var staffCode = getQueryString("staffCode");
	var QRCodeType = getQueryString("QRCodeType");
	var sourceChannelId = getQueryString("sourceChannelId");
	var sourceShopId = getQueryString("sourceShopId");
	var sourceStaffId = getQueryString("sourceStaffId");
	var pageName;
	var locationSrc = window.location.href.replace(
			/(http|https|ftp):\/\/[\s\S]*?(?=\/)/g, "").split("#")[0];
	var index = locationSrc.lastIndexOf("/");
	var pageName = locationSrc.substring(index + 1);
	if (pageName.indexOf("?") >= 0) {
		pageName = pageName.substring(0, pageName.indexOf("?"));
	}
	if(pageName==null || pageName==""){
		pageName = "Index.html";
	}
	function parseUrlParams(string){
		if(!string||!string.length){return"";}
		string=string.replace(/&amp;/g,"&");
		string=string.replace(/\n/g,"\\n");
		return string;
	}
	function buildAsyncUrl(url,params) {
		if(!url){ return null; }
		var inUrl= _ctxurl;
		inUrl+="/"+url;
		inUrl+="?"+parseUrlParams(params);
		return inUrl;
	}
	
	function getCookie(name)
	{
	    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	 
	    if(arr=document.cookie.match(reg))
	 
	        return unescape(arr[2]);
	    else
	        return null;
	}
	
	//获取URL中的传参
	function getQueryString(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r!=null) return r[2]; return "";
	}
	
	//获取URL中的传参
	function getUrlParam(name,url) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
	    var r = url.match(reg);
	    if (r!=null) return r[2]; return "";
	}
	
	function handleResponse(data,params){
		
		//前置校验结果判断，并渲染内容区
		if(params.needCheck==undefined||params.needCheck){
			if(data==undefined || data==null){
				return;
			}
			var checkResult = handleCheck(data,params);
			if(!checkResult){
				if( chanId  == "E051"   ||  chanId  == "E050"){
					o2oApp.callLogin();
				}else{
//					window.location.href = _globalVar_relativePath+"/login/Login.html";
					location.href="https://login.10086.cn/SSOCheck.action?channelID=10731&backUrl=" + location.href;
				}
				return;
			}
		}
		renderData(data,params.artId,params.contentId);
		for(var i in _defaultFn){
			if(typeof _defaultFn[i] == "function"){
				_defaultFn[i].call(this,data,params);
			}
		}

		if(typeof(params.afterFn)=="function"){
			params.afterFn.call(this,data);
		}else if(params.afterFn instanceof Array){
			for(var i in params.afterFn){
				if(typeof(params.afterFn[i])=="function")
				params.afterFn[i].call(this,data);
			}
		}
	}
	
	function handleCheck(data,contentId){
		//var checkInfo = data["checkInfo"];
		var flag = true;
		var checkTag = data["CHECK_TAG"];
		//校验未通过
		if(checkTag && checkTag !='' && checkTag != 0){
			flag=false;
		}
		return flag;
	}
	
	function addLoadingEffect(params){
		if(params.loadingType){
			var id = parseInt(params.loadingType);
			if(typeof _effect[id] == "function"){
				params=_effect[id].call(this,params);
			}
		}else{
			if(typeof _defaultEff == "function"){
				params=_defaultEff.call(this,params);
			}
		}
		return params;
	}
	

	$.extend({
		setCtxUrl:function(url){
			_ctxurl=url;
		},
		addEffect:function(effFuc,def){
			_effect.push(effFuc);
			if(def){
				_defaultEff = effFuc;
			}
		},
		addCheckMethod:function(checkTag,checkMethod){
			_checkMethod[checkTag]=checkMethod;
		},
		addDefaultFn:function(fn){
			_defaultFn.push(fn);
		},
		ajaxDirect:function(params){
			if(pageName!=null && pageName!=""){
				params.param = params.param+"&pageName="+ pageName;
			}
			
			if( params.param.indexOf('serialNumber=')==-1 && serialNumber!=null && serialNumber!=""){
				params.param = params.param+"&serialNumber="+ serialNumber;
			}
			if(chanId!=null && chanId!=""){
				params.param = params.param+"&CHANID="+ chanId;
			}
			if(uid!=null && uid!=""){
				params.param = params.param+"&UID="+ uid;
			}
			if(staffCode!=null && staffCode!=""){
				params.param = params.param+"&staffCode="+ staffCode;
			}
			if(QRCodeType!=null && QRCodeType!=""){
				params.param = params.param+"&QRCodeType="+ QRCodeType;
			}
			if(sourceChannelId!=null && sourceChannelId!=""){
				params.param = params.param+"&sourceChannelId="+ sourceChannelId;
			}
			if(sourceShopId!=null && sourceShopId!=""){
				params.param = params.param+"&sourceShopId="+ sourceShopId;
			}
			if(sourceStaffId!=null && sourceStaffId!=""){
				params.param = params.param+"&sourceStaffId="+ sourceStaffId;
			}
			var requrl = buildAsyncUrl(params.url,params.param);
			requrl=requrl+"&ajaxSubmitType=post";
			var tokenId = Math.random();
			requrl+="&ajax_randomcode="+tokenId;
			document.cookie=tokenId+"=0;path=/;";
			params.goodsName = getUrlParam("goodsName",requrl);
			params.url=encodeURI(requrl);
			params=addLoadingEffect(params);
			if(!params.dataType){ params.dataType="json"; }
			params.success=function(data,textStatus, jqXHR){
				var exp = new Date();
                exp.setTime(exp.getTime() - 1);
                var cval=getCookie(tokenId);
                if(cval!=null){
                	document.cookie= tokenId + "="+cval+";expires="+exp.toGMTString()+";path=/;";
                }
                
				if(data){
					if(pageName!="olympic.html"){
						var err_code = "";
						var operType = "";
						var goodsName = "";
						var paramsStr = "";
						var product = "";
						if(params.param){
							paramsStr = params.param;
							var paramsArr = paramsStr.split("&");
							for (var i = 0; i < paramsArr.length; i++) {
								var paramArr = paramsArr[i].split("=");
								if ("operType".toLowerCase() == paramArr[0].toLowerCase()) {
									if("QUERY"==paramArr[1]){//查询 ： QUERY
										operType = "查询";
									}
									else if("OPER"==paramArr[1]){//办理 ： OPER
										operType = "办理";
									}
									else if("UPDATE"==paramArr[1]){//更改 ： UPDATE
										operType = "更改";
									}
									else if("CANCEL"==paramArr[1]){//取消 ： CANCEL
										operType = "取消";
									}
									else if("LOGIN"==paramArr[1]){//登陆 ： LOGIN
										operType = "登录";
									}
								}
								if ("goodsName" == paramArr[0]) {
									goodsName = paramArr[1];
								}
								if(goodsName=="我的移动"){
									goodsName="话费余额;套餐余量";
								}
							}
						}
						if(params.url){
							var urlStr = params.url;
							if(urlStr.indexOf("SPServe/handleServe") > 0 || urlStr.indexOf("flowServeBusi/handleFlowBusi") > 0 || urlStr.indexOf("basicServer/handleServe") > 0){
								$(".deploy .RadioBox ul li").each(function(){
									if($(this).attr("class")==="on"){
										product = $(this).text();
									}
								});
								if(!valIsNotEmpty(product)){
									product = $("#busiFee").html();
								}
							}
							if(urlStr.indexOf("pickAndPay/handleFlowBusi") > 0){
								product = $('#voiceCallPick').html() + "；" + $('#netFlowPick').html();
								wtsi_n = "自选套餐";
							}
						}
						if("flowZone.html"===pageName){
							if(goodsName===""){
								wtsi_n="流量管家-流量包月套餐";
							}else{
								wtsi_n="流量管家-"+goodsName;
							}
						}
						if (data.X_RESULTCODE && valIsNotEmpty(operType) && valIsNotEmpty(goodsName)) {
							//wtsi_n=wtsi_n?wtsi_n+";"+goodsName:goodsName;
//						if(wtsi_nMap[pageName]){
//							wtsi_n=wtsi_nMap[pageName]+";"+goodsName;
//						}
							var result = data.X_RESULTCODE == 0 ? "99"
									: "-99";
							if (result == "-99") {
								err_code=data.X_RESULTINFO;
								if(operType=="登录"){
									if (typeof(dcsPageTrack)=="function"){dcsPageTrack('WT.ti','登录失败', false, 'WT.si_x','登录失败', false, 'WT.si_n','登录', false, "WT.err_code", err_code, false) }
								}else{
									if(operType=="查询"){
										if (typeof (dcsPageTrack) == "function") {dcsPageTrack("WT.si_n", goodsName, true, "WT.si_x", product.trim()+operType.trim() + '失败', true, "WT.err_code", err_code, false);}
									}else{
										if (typeof (dcsPageTrack) == "function") {dcsPageTrack("WT.si_n", wtsi_n, true, "WT.si_x", product.trim()+operType.trim() + '失败', true, "WT.err_code", err_code, false);}
									}
								}
							} else {
								if(operType == "登录" && goodsName.indexOf("登录") > 0){
									if(data.serialNumber && data.eparchyCode){
										document.cookie="WT.rh_user= mob="+data.serialNumber+":city="+data.eparchyCode+"; path=/; domain=.hn.10086.cn; expires="+ new Date(new Date().getTime() + 63072000000).toGMTString();
									}
								}
								if(operType=="登录"){
									if (typeof(dcsPageTrack)=="function"){dcsPageTrack('WT.ti','登录成功', false, 'WT.si_x','登录成功', false, 'WT.si_n','登录', false) }
								}else{
									if(operType=="查询"){
										if (typeof (dcsPageTrack) == "function") {dcsPageTrack("WT.si_n", goodsName, true, "WT.si_x", product.trim()+operType.trim() + '成功', true);}
									}else{
										if (typeof (dcsPageTrack) == "function") {dcsPageTrack("WT.si_n", wtsi_n, true, "WT.si_x", product.trim()+operType.trim() + '成功', true);}
									}
								}
							}
						}
					}
				}
				handleResponse(data,params);
			}; 
			params.contentType="application/x-www-form-urlencoded; charset=utf-8";
			$.ajax(params);
			return true;
		},
		ajaxSubmit:function(params){
			var requrl = buildAsyncUrl(params.url,params.param);
			requrl=requrl+"&ajaxSubmitType=post";
			requrl+="&ajax_randomcode="+Math.random();
			params.url=encodeURI(requrl);
			params=addLoadingEffect(params);
			var formParam = $("#"+params.formId).serialize();
			params.data = formParam;
			if(!params.dataType){ params.dataType="json"; }
			params.success=function(data,textStatus, jqXHR){
				handleResponse(data,params);
			};
			params.type = "POST";
			params.contentType="application/x-www-form-urlencoded; charset=utf-8";
			$.ajax(params);
			return true;
		},	
		ajaxOperate:function(params){
			params.handlerBusiResult = true ;
			$.ajaxSubmit(params);
		}
	});
})(jQuery);

