var itemIndex = 1;
var partnertabEnd = false;
var usertabEnd = false;

/*友商营销信息分页查询*/
var droploadConfig = {
    scrollArea: window,
    domUp: {
        domClass: 'dropload-up',
        domRefresh: '<div class="dropload-refresh">↑上拉刷新</div>',
        domUpdate: '<div class="dropload-update">↑释放更新</div>',
        domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
    },
    domDown: {
        domClass: 'dropload-down',
        domRefresh: '<div class="dropload-refresh">↓下拉加载更多</div>',
        domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
        domNoData: '<div class="dropload-noData">已经到底了...</div>'
    },
    loadUpFn: function (me) {
        var loadUp = 'loadUp';
        fetchInfo(itemIndex, me, loadUp);
    },
    loadDownFn: function (me) {
        var loadDown = 'loadDown';
        fetchInfo(itemIndex, me, loadDown);
    }
};

var dropload;
$(function () {
    $(".tab-menu li").click(function () {
        var $this = $(this);
        itemIndex = $this.index()+1;
        $this.addClass('active').siblings().removeClass('active');
        if (itemIndex == '1') {
            $("#partnerInfoList").show();
            $("#partnerAdd").show();
            $("#userInfos").hide();
            $("#userAdd").hide();
            fetchPartner();
        }
        else if (itemIndex == '2') {
            $("#partnerInfoList").hide();
            $("#partnerAdd").hide();
            $("#userInfos").show();
            $("#userAdd").show();
            fetchUser();
            // dropload.resetload();
            $("#userInfos").html("");
            var me = dropload;
            fetchInfo(itemIndex,me,'loadDown');
        }
    });
    dropload = $(".content").dropload(droploadConfig);
});


function fetchPartner() {
    //partnertabEnd = true 数据加载完
    if (!partnertabEnd) {
        dropload.unlock();//智能锁定上次拉动的方向
        dropload.noData(false);
    } else {
        dropload.lock('down');
        dropload.noData();
    }
}

function fetchUser() {
    if (!usertabEnd) {
        // 解锁
        dropload.unlock();
        dropload.noData(false);
    } else {
        // 锁定
        dropload.lock('down');
        dropload.noData();
    }
}

$(function () {
    $("#insertBtn").click(function () {
        $("form :input ").trigger('blur');
        var flag = 0;
        $("div[id$=Error]").each(function (i, index) {
            if ($(this).css("display") == "block") {
                flag = 1;
            }
        });
        var params = $("#communityInfo").serialize();
        if (flag == 0) {
            //发送ajax请求保存用户信息
            $.ajax({
                url: "saveCommunityInfo",
                type: "post",
                data: params,
                dataType: "json",
                success: function (data) {
                    if (data.code == "0") {
                        $("#insert").hide();
                        $("#update").show();
                    }
                }
            });
        }
    });
});

$("#updateBtn").click(function () {
    $("form :input ").trigger('blur');
    var flag = 0;
    $("div[id$=Error]").each(function (i, index) {
        if ($(this).css("display") == "block") {
            flag = 1;
        }
    });
    var params = $("#communityInfo").serialize();
    if (flag == 0) {
        //发送ajax请求保存用户信息
        $.ajax({
            url: "updateCommunityInfo",
            type: "post",
            data: params,
            dataType: "json",
            success: function (data) {
                if (data.code == "0") {
                    $("#updateBtn").removeClass('broad-btn-blue').addClass('broad-btn-gray');
                }
            }
        });
    }
});


function initAddPartnerInfo() {
    var $keepInfo = $("#keepCommuntyInfo");
    $keepInfo.attr("action", "initAddPartnerInfo");
    $keepInfo.submit();
}

function initAddUserInfo() {
    var $keepInfo = $("#keepCommuntyInfo");
    $keepInfo.attr("action", "initAddUserInfo");
    $keepInfo.submit();
}

$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/*返回进行分页*/
function fetchInfo(type, me, dir) {
    if (dir == 'loadUp') {
        $("input[name='pageNo']").val("0");
        if (type == '1') {
            //友商信息下拉刷新页面,解除锁定
            $("#pageNoPartner").val("0");
            var $partnerInfoList = $("#partnerInfoList");
            $partnerInfoList.html("");
            partnertabEnd = false;
        } else {
            $("#pageNoUser").val("0");
            //用户信息下拉刷新
            $("#userInfos").html("");
            usertabEnd = false;
        }
    }
    $("#type").val(type);
    var pageNo;
    if (type == '1') {
        pageNo = Number($("#pageNoPartner").val()) + 1;
        $("#pageNoPartner").val(pageNo);
    } else {
        pageNo = Number($("#pageNoUser").val()) + 1;
        $("#pageNoUser").val(pageNo)
    }
    var params = $("#keepCommuntyInfo").serializeObject();
    $.ajax({
        url: "fetchInfo",
        data: params,
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.code == 0) {
                var arr = result.data.list;
                if (arr != undefined) {
                    //设置当前页和总页数
                    template.helper("logoUrl", logoUrl);
                    if (result.message == "1") {
                        $("#pageNoPartner").val(result.data.pageNo);
                        var html = template('partnerInfo', result.data);
                        $("#partnerInfoList").append(html);
                        if (dir == 'loadUp') {
                            me.noData(false);
                        }
                        if (arr.length <= result.data.pageSize && pageNo * result.data.pageSize >= result.data.count) {
                            // 再往下已经没有数据
                            // 锁定
                            // 显示无数据
                            me.noData();
                            partnertabEnd = true;
                        }
                        me.resetload();
                        if (dir == 'loadUp') {
                            me.unlock();
                        }
                        if (partnertabEnd) {
                            me.lock('down');
                        }
                    } else {
                        $("#pageNoUser").val(result.data.pageNo);
                        template.helper("timeConver", timeConver);
                        var html = template('userInfo', result.data);
                        // $("#userInfos dropload-down").before(html);
                        $("#userInfos").append(html);
                        if (dir == 'loadUp') {
                            me.noData(false);
                        }
                        if (arr.length <= result.data.pageSize && pageNo * result.data.pageSize >= result.data.count) {
                            // 再往下已经没有数据
                            // 锁定
                            // 显示无数据
                            me.noData();
                            usertabEnd = true;
                        }
                        me.resetload();
                        if (dir == 'loadUp') {
                            me.unlock();
                        }
                        if (usertabEnd) {
                            me.lock('down');
                        }
                    }
                }else{
                    // 显示无数据
                    me.noData();
                    me.resetload();
                    me.lock('down');//向上拉加载无数据锁定下方，向下拉刷新数据无数据还是锁定下方
                }
            } else {
                // 锁定
                me.lock();
                // 显示无数据
                me.noData();
                me.resetload();
                showAlert(result.message || "系统在忙哦，请稍后再试！");
            }
            // 每次数据加载完，必须重置
            // me.resetload();
            // if(dir == 'loadUp' ){
            //     dropload.noData(false);
            //     dropload.unlock();
            // }

        },
        error: function () {
            showAlert("系统在忙哦，请稍后再试！");
            // 锁定
            me.lock();
            // 显示无数据
            me.noData();
            // 每次数据加载完，必须重置
            me.resetload();
        }
    });
}

function logoUrl(logoUrl) {
    if (logoUrl) {
        return '/res/img/' + logoUrl;
    } else {
        return ctxStatic + '/images/cardhui.png';
    }
}

/**partnerInfoList
 * "yyyy-MM-dd HH:mm:ss"
 * @param longTime,date.getTime()
 * @param fmt
 * @returns {string}
 */
function timeConver(longTime, fmt) {
    if (longTime) {
        var date = new Date(longTime);
        return (date.pattern(fmt ? fmt : "yyyy-MM-dd HH:mm:ss"));
    } else {
        return "";
    }
}
