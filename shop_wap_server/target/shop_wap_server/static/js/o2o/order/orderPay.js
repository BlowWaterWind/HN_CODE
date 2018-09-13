var droploadConfig = {
    scrollArea: window,
    domUp : {
        domClass   : 'dropload-up',
        domRefresh : '<div class="dropload-refresh">↑上拉刷新</div>',
        domUpdate  : '<div class="dropload-update">↑释放更新</div>',
        domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
    },
    domDown : {
        domClass   : 'dropload-down',
        domRefresh : '<div class="dropload-refresh">↓下拉加载更多</div>',
        domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
        domNoData  : '<div class="dropload-noData">已经到底了...</div>'
    },
    loadUpFn : function(me){
        window.location.reload();
    },
    loadDownFn: function (me) {
        searchOrder(me);
    }
};

/**
 * 下拉刷新订单数据
 * @param pullType
 */
function searchOrder(me) {
    var url = ctx + "o2oOrder/queryOrderPayListByPage";
    var pageNo = $("#pageNo").val();
    pageNo++;
    var pageSize = $("#pageSize").val();
    var flag = $("#flag").val();
    var param = {'pageNo': pageNo, 'pageSize': pageSize, 'flag': flag};
    $.ajax({
        url: url,
        type : "post",
        dataType : "json",
        data: param,
        success: function (data) {
            if (data && data.list!=null &&data.list.length>0) {
                var html = template('tempOrderList', data);
                var $ul=$(html);
                $("#pageDiv .dropload-down").before($ul);
                $("#pageNo").val(data.pageNo);

                if(data.list.length < pageSize || pageNo == data.totalPage){
                    // 再往下已经没有数据
                    // 锁定
                    me.lock();
                    // 显示无数据
                    me.noData();
                }
            } else {
                if(data.pageNo==1&&data.list==null){
                    var str1 ="<div class='no-orders' style='display: none;'> <img src='" + ctxStatic + "images/fa_icon.png' /> <p>暂未找到相关订单</p> </div>";
                    var $li = $(str1);
                    $("#pageDiv .dropload-down").before($li);
                }
                // 锁定
                me.lock();
                // 无数据
                me.noData();
            }
        // 每次数据加载完，必须重置
            me.resetload();
        },
        error: function () {
            me.lock();
            me.noData();
            me.resetload();
        }
    });
}

function orderPay(orderId){
    if(window.Webtrends){
        Webtrends.multiTrack({
            argsa: ["WT.event","WDDD_DDZF"],
            delayTime: 100
        })
    }else{
        if(typeof(dcsPageTrack)=="function"){
            dcsPageTrack ("WT.event","WDDD_DDZF",true);
        }
    }
    window.location.href=ctx+'/o2oOrder/payList?orderId='+orderId;
}
function resetOrder(orderId){
    $.ajax({
        url:ctx+'/o2oOrder/bossSyn?orderTempId='+orderId,
        method:"GET",
        success:function(data){
            if(data.respCode == '0'){
                // 插码相关
                try{
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n",'BOSS同步订单',
                                "WT.si_x","99"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n",'BOSS同步订单',true,
                                "WT.si_x","99",true);
                        }
                    }
                }catch (e){
                    console.log(e);
                }
                $("#pageDiv").dropload(droploadConfig);
            }else{
                // 插码相关
                try{
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n",'BOSS同步订单',
                                "WT.si_x","-99"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n",'BOSS同步订单',true,
                                "WT.si_x","-99",true);
                        }
                    }
                }catch (e){
                    console.log(e);
                }
                showAlert(data.respMsg||'BOSS同步失败！');
            }
        },
        error:function(data){
            showAlert("BOSS同步失败！");
        }
    })
    //window.location.href=ctx+'/o2oOrder/bossSyn?orderTempId='+orderId;
}



function changeTab(obj){
    $(".tab-menu li").each(function(){
        $(this).removeClass("active");
    });
    $(obj).addClass("active");
}


