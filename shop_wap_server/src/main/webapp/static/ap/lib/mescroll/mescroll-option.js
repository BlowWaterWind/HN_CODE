/*
 *下拉刷新和上拉加载js
 * 

2.拷贝以下布局结构
<div id="mescroll" class="mescroll"> // id可修改,但class不能改;另外mescroll的height: 100%,所以父布局要有高度,否则无法触发上拉加载.
	//滑动区域的内容,如:<ul>列表数据</ul> ...
</div>

3.创建MeScroll对象,内部已默认开启下拉刷新
var mescroll = initMeScroll("mescroll", {
//	down:{
//		auto: true, //是否在初始化完毕之后自动执行下拉回调callback; 默认true
//		callback: function() {
//			mescroll.resetUpScroll();//下拉刷新的回调,默认重置上拉加载列表为第一页
//		}
//	},
	up: {
//		auto: true, //是否在初始化时以上拉加载的方式自动加载第一页数据; 默认true
		callback: getListData, //上拉回调,此处可简写; 相当于 callback: function (page) { getListData(page); }
	}
});

function getListData(page){
	$.ajax({
        type: 'GET',
        url: 'xxxxxx?num='+page.num+"&size="+page.size,
        dataType: 'json',
        success: function(data){
        	//联网成功的回调,隐藏下拉刷新和上拉加载的状态;(参数:当前页数据的总数)
			mescroll.endSuccess(data.length);//如果传了参数,mescroll会自动判断列表若无任何数据,则提示空;列表无下一页数据,则提示无更多数据;如果不传参数则仅隐藏加载中的状态
			//设置列表数据
			//setListData(data);
        },
        error: function(data){
        	//联网失败的回调,隐藏下拉刷新和上拉加载的状态;
	        mescroll.endErr();
        }
    });
}

其他常用方法:
1.主动触发下拉刷新 mescroll.triggerDownScroll();
2.主动触发上拉加载 mescroll.triggerUpScroll();
3.重置列表 mescroll.resetUpScroll();
4.滚动列表到指定位置 mescroll.scrollTo(y); (y=0回到列表顶部;如需滚动到列表底部,可设置y很大的值,比如y=99999);
5.获取下拉刷新的配置 mescroll.optDown;
6.获取上拉加载的配置 mescroll.optUp;
7.锁定下拉刷新 mescroll.lockDownScroll(isLock); (isLock=ture,null锁定;isLock=false解锁)
8.锁定上拉加载 mescroll.lockUpScroll(isLock); (isLock=ture,null锁定;isLock=false解锁)
**/

function initMeScroll(mescrollId, options) {

	var htmlContent = '<div class="downwarp-unload"><p class="downwarp-arrow"></p><p class="downwarp-tip">下拉刷新</p></div>';
	htmlContent += '<div class="downwarp-onload"><p class="downwarp-progress"></p><p class="downwarp-tip">加载中</p></div>';

	var myOption={
		down:{
			offset: 60, //触发刷新的距离
			htmlContent: htmlContent, //布局内容
			inited: function(mescroll, downwarp) {
				//初始化完毕的回调,可缓存dom
				mescroll.downOnLoadDom = downwarp.getElementsByClassName("downwarp-onload")[0];
				mescroll.downUnloadDom = downwarp.getElementsByClassName("downwarp-unload")[0];
				mescroll.downTipDom = downwarp.getElementsByClassName("downwarp-tip")[0];
				mescroll.downArrowDom = downwarp.getElementsByClassName("downwarp-arrow")[0];
			},
			inOffset: function(mescroll) {
				//进入指定距离范围内那一刻的回调
				mescroll.downOnLoadDom.style.display="none";
				mescroll.downUnloadDom.style.display="block";
				mescroll.downTipDom.innerText="下拉刷新";
				mescroll.downArrowDom.style.webkitTransform = "rotate(0deg)";
				mescroll.downArrowDom.style.transform = "rotate(0deg)";
			},
			outOffset: function(mescroll) {
				//下拉超过指定距离那一刻的回调
				mescroll.downTipDom.innerText="释放更新";
				mescroll.downArrowDom.style.webkitTransform = "rotate(-180deg)";
				mescroll.downArrowDom.style.transform = "rotate(-180deg)";
			},

			showLoading: function(mescroll) {
				//触发下拉刷新的回调
				mescroll.downOnLoadDom.style.display="block";
				mescroll.downUnloadDom.style.display="none";
				mescroll.downTipDom.innerText="下拉刷新";
				mescroll.downArrowDom.style.webkitTransform = "rotate(0deg)";
				mescroll.downArrowDom.style.transform = "rotate(0deg)";
			}
		},
		up:{
			toTop: {
				src: "option/mescroll-totop.png" //回到顶部按钮的图片路径
			}
		}
	}
	//加入自定义的默认配置
	options=MeScroll.extend(options,myOption);
	//创建MeScroll对象
	return new MeScroll(mescrollId,options);
}
$(function(){
    //创建MeScroll对象
    var mescroll = initMeScroll("mescroll", {
        down:{
            auto:true,//是否在初始化完毕之后自动执行下拉回调callback; 默认true
            callback: downCallback, 
        },
        up: {
            auto:true,
            isBoth: true, 
            isBounce: false, 
            callback: upCallback, 
           
        }
    });
    
    /*下拉刷新的回调 */
    function downCallback(){
       
        getListDataFromNet(0, 1, function(data){
            
            mescroll.endSuccess();
      
            setListData(data, false);
       
            $("#downloadTip").css("top","44px");
            setTimeout(function () {
                $("#downloadTip").css("top","10px");
            },2000);
        }, function(){
        
            mescroll.endErr();
        });
    }
    
    /*上拉加载的回调 page = {num:1, size:10}; num:当前页 从1开始, size:每页数据条数 */
    function upCallback(page){
        //联网加载数据
        getListDataFromNet(page.num, page.size, function(curPageData){
            
            console.log("page.num="+page.num+", page.size="+page.size+", curPageData.length="+curPageData.length);
            
            //方法一(推荐): 后台接口有返回列表的总页数 totalPage
            //mescroll.endByPage(curPageData.length, totalPage); //必传参数(当前页的数据个数, 总页数)
            
            //方法二(推荐): 后台接口有返回列表的总数据量 totalSize
            //mescroll.endBySize(curPageData.length, totalSize); //必传参数(当前页的数据个数, 总数据量)
            
            //方法三(推荐): 您有其他方式知道是否有下一页 hasNext
            //mescroll.endSuccess(curPageData.length, hasNext); //必传参数(当前页的数据个数, 是否有下一页true/false)
            

            mescroll.endSuccess(curPageData.length);
            
     
            setListData(curPageData, true);
        }, function(){
            
            mescroll.endErr();
        });
    }
    
    /*设置列表数据*/
    function setListData(curPageData, isAppend) {
        var listDom=document.getElementById("newsList");
        for (var i = 0; i < curPageData.length; i++) {
            var newObj=curPageData[i];
          
                var  str = '<p class="list-phone-txt">';
                     str+= '<span>'+newObj.phoneNum01+'</span>';
                      str+='<span>' +newObj.phoneNum02+ '</span>'
                      str+='<span class="font-red">' +newObj.phoneNum03+ '</span></p>'
                       str+= '<p class="list-phone-txt2 text-center">'+newObj.title+'</p>'     
                        str+='<p class="flex1">'+newObj.card+'</p>' 
                         str+= '<a class="btn btn-middle btn-border btn-border-blue " href="javascript:;">购买</a> <a class="btn btn-middle btn-border btn-border-rose" href="javascript;">收藏</a>'  
                            
                           
                           
                       
            
            var liDom=document.createElement("li");
            liDom.innerHTML=str;
            
            if (isAppend) {
                listDom.appendChild(liDom);//加在列表的后面,上拉加载
            } else{
                listDom.insertBefore(liDom, listDom.firstChild);//加在列表的前面,下拉刷新
            }
        }
    }
    
    /*联网加载列表数据
     在您的实际项目中,请参考官方写法: http://www.mescroll.com/api.html#tagUpCallback
     请忽略getListDataFromNet的逻辑,这里仅仅是在本地模拟分页数据,本地演示用
     实际项目以您服务器接口返回的数据为准,无需本地处理分页.
     * */
    var downIndex=0;
    function getListDataFromNet(pageNum,pageSize,successCallback,errorCallback) {
        //todo 一次返回7 或 8个号码 模拟分页



        //延时一秒,模拟联网
        setTimeout(function () {
            try{
                var newArr=[];
                if(pageNum==0){
                    //此处模拟下拉刷新返回的数据
                    downIndex++;
                    var newObj={phoneNum01:178,phoneNum02:0657,phoneNum03:7820,title:"茂名",card:"万能副卡"};
                    newArr.push(newObj);
                }else{
                    //此处模拟上拉加载返回的数据
                    for (var i = 0; i < pageSize; i++) {
                        var upIndex=(pageNum-1)*pageSize+i+1;
                        var newObj={phoneNum01:178,phoneNum02:0657,phoneNum03:7820,title:"茂名",card:"万能副卡"};
                        newArr.push(newObj);
                    }
                }
                //联网成功的回调
                successCallback&&successCallback(newArr);
            }catch(e){
                //联网失败的回调
                errorCallback&&errorCallback();
            }
        },2000)
    }
    
});