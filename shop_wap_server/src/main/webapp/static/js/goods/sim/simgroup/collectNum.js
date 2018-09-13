var cityObject = {'0731':'长沙市','0733':'株洲市','0732':'湘潭市','0734':'衡阳市','0739':'邵阳市','0730':'岳阳市',
                   '0736':'常德市','0744':'张家界市','0737':'益阳市','0735':'郴州市','0746':'永州市',
                   '0745':'怀化市','0738':'娄底市','0743':'湘西州市'};

//卡类参数
function callOpCode(){
	var opCode = Request('opCode');
	if(!checkValue('opCodeABn',opCode)){
		return false;
	}
	adValue('opCode',opCode);
	$("#opCodeABn_" + opCode).parent('.swiper-slide').addClass('on').siblings().removeClass('on');
}

function checkValue(name,value){
	
	if(name != null && name != "" && value != null && value != ""){
		var fg = "n";
		$('[id^="'+ name +'_"]').each(function(index,domEle){
		    if(domEle.id==(name + "_" + value)){
		    	fg = "t";

		    }
		});

		if(fg =="n"){
			//输入的值不在被选择范围
			return false;
		}else{
			return true;
		}
	}
	return false;

}

	


	
function setCookieStatic(e, p, m, q, d, a) {
	var h = new Date();
	if (m != null || m != undefined) {
		var b = m * 24 * 60 * 60 * 1000;
		h.setTime(h.getTime() + b)
	}
	var g = ((m == null) ? '' : (';expires=' + h.toGMTString()));
	var c = ((q == null) ? '' : (';path=' + q));
	var n = ((d == null) ? '' : (';domain=' + d));
	var l = ((a == true) ? ';secure' : '');
	document.cookie = e + '=' + escape(p) + g + c + n + l
}

function getCookieStatic(d) {
	var b = document.cookie.split('; ');
	for (var c = 0; c < b.length; c++) {
		var a = b[c].split('=');
		if (escape(d) == a[0]) {
			return unescape(a[1])
		}
	}
	return null
}	
	
//按位筛选号码
function queryByMuNum(){
	var s="0123456789";
	var m="";
	for(var i=0;i<10;i++){
		var val = $("#num_"+i).text();
		if(i==0){//第二位，只能输入3,4,5,7,8,9
			if(val!=""&&val!=3&&val!=4&&val!=5&&val!=7&&val!=8&&val!=9){
				layer.open({
					title: [
					'消息提示',
					'color:#999'],
					content: '<div>号码第二位只能是3,4,5,7,8,9</div>',
					btn: ['<em class="text_blue">确定</em>'],
				});
				return;
			}
		}
		if (val!=""&&s.indexOf(val)<0) {
			layer.open({
				title: [
				'消息提示',
				'color:#999'],
				content: '<div>只能输入数字</div>',
				btn: ['<em class="text_blue">确定</em>'],
			});
			return;
		} else if (val=="") {
			m+="*";
		} else {
			m+=val;
		}
	}
	$("#txtInput").val(m);
	queryCardList(1);
}	
	
//刷新验证码
function flushImg(obj) {
	obj.src=ctxPath + "/card/cardOrder/imageCode.jsps?id="+Math.floor(Math.random()*100) ;
}
//添加删除我的收藏--列表页
function dmlCollect(obj,num,city,recmdCode,confId,cardName) {
	if ($(obj).hasClass('btn-border-rose')) {//加入收藏
	        $(obj).attr("otitle","取消收藏_"+num);
        $(obj).removeClass('btn-border-rose').addClass("btn-border-gray");
		addCart(num,city,recmdCode,confId,cardName);
	} else {
		$(obj).attr("otitle","加入收藏_"+num);
        $(obj).removeClass('btn-border-gray').addClass("btn-border-rose");
		delCart(num);
	}
}
//添加删除我的收藏--号码详情页
function dmlCollect_detail(obj,num,city,brand,price,sid) {
	$(obj).toggleClass('disCheck');
	if ($(obj).hasClass('disCheck')) {
		$(obj).attr("otitle","取消收藏_"+num);
		$(obj).text('已经收藏');
		addCart(num,city,brand,price,sid);
	} else {
		$(obj).attr("otitle","加入收藏_"+num);
		$(obj).text('加入收藏');
		delCart(num);
	}
}
//显示收藏列表
function showCartList(t){
	var cartItem=evalJSON('['+getCookieStatic('cartItem')+']');	//把cookie中的字符串转换为json	
	//cartItem.sort(compare("price",t));	
	var html="";
	if(cartItem!=""){		 
		for(var i=0;i<cartItem.length;i++){
			var cityName = cityObject[cartItem[i].city];
            html += '<li><p class="list-phone-txt"><span>'+cartItem[i].num+'</span></p>' +
                '<p class="list-phone-txt2 text-center">'+cityName+'</p>' +
                '<p class="flex1">'+cartItem[i].cardName+'</p><a class="btn btn-middle btn-border btn-border-blue" onclick="toBuy(\''+ cartItem[i].num +'\',\''+ cartItem[i].recmdCode+'\',\''+cartItem[i].confId+'\',\''+cartItem[i].city+'\')">购买</a><a ' +
                'class="btn btn-middle btn-border btn-border-gray" onclick="dmlCollect(this,\''+cartItem[i].num +'\',\''+cartItem[i].city +'\',\''+cartItem[i].recmdCode +'\',\''+cartItem[i].confId+'\',\''+cartItem[i].cardName+'\')">收藏</a></li>';
		}
	 }
	 $('#newsList').html(html);
}
//添加收藏
function addCart(num,city,recmdCode,confId,cardName){
	console.log(num+" addCart");
	var cartItem="";				
	if(getCookieStatic('cartItem')==null){
		cartItem='{num:"'+num+'",city:"'+city+'",recmdCode:"'+recmdCode+'",confId:"'+confId+'",cardName:"'+cardName+'"}';
		setCookieStatic('cartItem',cartItem,8760,'/');	//保存cookie，名称为 cartItem,时间为 1年
	}else{		
		var cartItem=evalJSON('['+getCookieStatic('cartItem')+']');
		if(getCookieStatic('cartItem').indexOf(num)==-1){	//判断cookie中是否已存在该号码，如果不存在则
			cartItem='{num:"'+num+'",city:"'+city+'",recmdCode:"'+recmdCode+'",confId:"'+confId+'",cardName:"'+cardName+'"},'+getCookieStatic('cartItem');
			setCookieStatic('cartItem',cartItem,8760,'/');	//保存cookie，名称为 cartItem,时间为 1年	
		}
	}		
}
//按号码删除收藏
function delCart(num){
	var cartItem=evalJSON('['+getCookieStatic('cartItem')+']');	//把cookie中的字符串转换为json
	console.log(num+" 删除购物车");
	for(var i in cartItem){  
		if (cartItem[i].num==num) {
			cartItem.splice(i,1);
			cartItem=strJSON(cartItem).replace('[','').replace(']','');	//把JSON转换为字符串，并替换字符串中的[]
			setCookieStatic('cartItem',cartItem,8760,'/');	//保存cookie，名称为 cartItem,时间为 1年
			break;
		}
	}
}
//按索引位置删除收藏
function delCartByIdx(i){
	var cartItem=evalJSON('['+getCookieStatic('cartItem')+']');	//把cookie中的字符串转换为json	
	if(cartItem.length==1){		
		setCookieStatic('cartItem','',0,'/');	//如果为最后一条，即清空cookie
	}else{
		cartItem.splice(i,1);	//删除指定元素
		cartItem=strJSON(cartItem).replace('[','').replace(']','');	//把JSON转换为字符串，并替换字符串中的[]
		setCookieStatic('cartItem',cartItem,8760,'/');	//保存cookie，名称为 cartItem,时间为 1年
	}
	showCartList(0);
}

function evalJSON(strJson) {
	return eval('(' + strJson + ')')
}

function strJSON(b) {
    var a = []; (function(e) {
        var c = true;
        if (e instanceof Array) {
            c = false
        } else {
            if (typeof e != 'object') {
                if (typeof e == 'string') {
                    a.push('"' + e + '"')
                } else {
                    a.push(e)
                }
                return
            }
        }
        a.push(c ? '{': '[');
        for (var d in e) {
            if (e.hasOwnProperty(d) && d != 'prototype') {
                if (c) {
                    a.push(d + ':')
                }
                arguments.callee(e[d]);
                a.push(',')
            }
        }
        a.push(c ? '}': ']')
    })(b);
    return a.slice(0).join('').replace(/,\}/g, '}').replace(/,\]/g, ']')
}

function Request(key) {
	var retValue = (window.location.search.match(new RegExp("(?:^\\?|&)" + key + "=(.*?)(?=&|$)")) || ['', null])[1];
	return (retValue == null ? "" : retValue);
}	
	
//加载更多
function loadmore() {
	var pageSize = $("#pageSize").val();
	var totalCount = $("#totalCount").val();
	var pageNo = $("#pageNo").val();
	if(pageNo * pageSize >= totalCount){
		return false;
	} else {
		queryCardList(++pageNo);
		return true;
	} 
}

function toBuy(num,recmdCode,confId,city) {
	trace("20");//事件码20
	window.location.href= ctx +"/simWholenet/simDetail?mobile="+num+"&recmdCode="+recmdCode+"&confId="+confId+"&city="+city;
}


	
function winpop(r){
    layer.open({
		title: ['消息提示','color:#999'],
		content: '<div>'+r+'</div>',
		btn: ['<em class="text_blue">确定</em>'],
	});
}
	
	
	
	
	
	
	
