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
    var url = ctx + "o2oOrder/queryOrderListByPage";
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
                    var str1 ="<div class='no-data' style='display: none;> <img src='" + ctxStatic + "images/no-data.png' alt='无数据'/> <div class='desc'>暂无记录哦</div> </div>";
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

//再次派单
function dispatchAgain(orderId){
    $.ajax({
        type : 'post',
        url : ctx+'/o2oOrder/dispatchAgain',
        dataType : 'json',
        cache : false,
        data : {
            orderId : orderId
        },
        success : function(data) {
            toggleModal("noticeSuccessId",data.respDesc);
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            toggleModal("noticeFailId","服务中断，请重试");
        }
    });
}

//取消订单
function cancelOrder(orderId){
    $.ajax({
        type : 'post',
        url : ctx+'/o2oOrder/cancelOrder',
        cache : false,
        dataType : 'json',
        data : {
            orderId : orderId
        },
        success : function(data) {
            // 插码相关
            try{
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",'取消订单',
                            "WT.si_x","99"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",'取消订单',true,
                            "WT.si_x","99",true);
                    }
                }
            }catch (e){
                console.log(e);
            }
            toggleModal("noticeSuccessId",data.respDesc);
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            // 插码相关
            try{
                var sin = document.getElementsByTagName('meta')['WT.si_n'].content;
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n",'取消订单',
                            "WT.si_x","-99"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n",'取消订单',true,
                            "WT.si_x","-99",true);
                    }
                }
            }catch (e){
                console.log(e);
            }
            toggleModal("noticeFailId","服务中断，请重试");
        }
    });
}

//再次发送短信
function sendSms(goodsName,serialNumber,typeId,orderTempId){
    $.ajax({
        type : 'post',
        url :  ctx+'/o2oBusiness/invokeSmsInterface',
        cache : false,
        dataType : 'json',
        data : {
            goodsName : goodsName,
            serialNumber: serialNumber,
            broadbandType: typeId,
            orderId: orderTempId
        },
        success : function(data) {
            if (data.X_RESULTCODE=='0') {
                toggleModal('noticeSuccessId',"短信发送成功");
            } else {
                toggleModal("noticeFailId",data.X_RESULTINFO);
                return false;
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            toggleModal("noticeFailId","服务中断，请重试");
            return false;
        }
    });
}

function changeTab(obj){
    $(".tab-menu li").each(function(){
        $(this).removeClass("active");
    });
    $(obj).addClass("active");
}
//催单
function remind(orderId){
    $.ajax({
        type : 'post',
        url : ctx+'/o2oOrder/remind',
        dataType : 'json',
        cache : false,
        data : {
            orderId : orderId
        },
        success : function(data) {
            showAlert(data.respDesc);
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            showAlert("服务中断，请重试");
        }
    });
}
//修改预约时间
function modifyTime(orderId){
    //根据订单原始预约时间计算预约起止时间
    var bookSession = $("#bookSession").val();
    var bookDate = $("#bookDate").val();
    $.ajax({
        type : 'post',
        url : baseProject+'/broadbandOrder/submitModify',
        cache : false,
        dataType : 'json',
        data : {bookDate:bookDate,orderId:orderId,bookSession:bookSession},
        success : function(data) {
            $(".box2").fadeToggle();
            $(".backMask").fadeToggle();
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            $(".box2").fadeToggle();
            $(".backMask").fadeToggle();
            alert("服务中断，请重试");
        }
    });
    $('#myModal').modal('show');
    $("#orderId").val(orderId);
}

function submitModify(){
    var modityTime=$("#modifyTime").val();
    var orderId=$("#orderId").val();
    $.ajax({
        type : 'post',
        url : baseProject+'/broadbandOrder/submitModify',
        cache : false,
        dataType : 'json',
        data : {modifyTime:modityTime,orderSubId:orderId},
        success : function(data) {
            $(".box2").fadeToggle();
            $(".backMask").fadeToggle();
            alert(data.resultDesc);
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            $(".box2").fadeToggle();
            $(".backMask").fadeToggle();
            alert("服务中断，请重试");
        }
    });
}
