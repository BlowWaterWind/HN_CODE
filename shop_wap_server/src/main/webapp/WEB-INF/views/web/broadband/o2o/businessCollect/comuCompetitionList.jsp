<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>湖南移动商城</title>
    <link href="${ctxStatic}/css/broadBand/channel.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">
    <script src="${ctxStatic}/js/dropload/dropload.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/artTemplate/dist/template.js"></script>
</head>

<body>
<div class="top container">
    <div class="header border-bottom"><a class="icon-main-back" href="javascript:void(0);"
                                         onclick="window.history.back()"></a>
        <h1>录入过的信息</h1>
    </div>
</div>
<div class="container">
    <input type="hidden" name="pageNo" id="pageNo" value="0" />
    <input type="hidden" name="pageSize" id="pageSize" value="3" />
    <div class="collect-con" id="comuCompetitionList">
    </div>
</div>
</body>
<script type="text/html" id="competition">
    <ul class="adress-list-box">
        {{each list as competition}}
            <li>
                <a href="javascript:void(0)" class="collect-icon">
                    <div class="collect-txt">
                        <p>{{competition.communityName}}</p>
                                <span class="channel-gray9 f12">
                                {{if competition.street  }}
                                    {{competition.street}}
                                {{/if}}
                                {{if competition.addressPath}}
                                     {{competition.addressPath}}
                                {{/if}}
                                </span>
                    </div>
                    <form action="initAddComuCompetition" method="post">
                        <input type="hidden" name="recordId" value="{{competition.recordId}}"/>
                        <input type="hidden" name="query" value="query"/>
                        <div class="collect-btn">
                            <input onclick="modify(this)" type="button" class="btn btn-border-blue"
                                   value="修改"/>
                            <input onclick="view(this)" type="button" class="btn btn-border-blue"
                                   value="查看"/>
                        </div>
                    </form>
                </a>
            </li>
        {{/each}}
    </ul>
</script>
<script type="text/javascript">
    function modify(thisDiv) {
        $(thisDiv).parent().parent().find("input[name='query']").remove();
        $(thisDiv).parent().parent().submit();
    }

    function view(thisDiv) {
        $(thisDiv).parent().parent().submit();
    }
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
            searchList(me);
        }
    };

    /**
     * 下拉刷新订单数据
     * @param pullType
     */
    function searchList(me) {
        var url = ctx + "o2oBusiCollect/queryComputitionListByPage";
        var pageNo = $("#pageNo").val();
        pageNo++;
        var pageSize = $("#pageSize").val();
        var param = {'pageNo': pageNo, 'pageSize': pageSize};
        $.ajax({
            url: url,
            type : "post",
            dataType : "json",
            data: param,
            success: function (data) {
                if (data && data.list!=null &&data.list.length>0) {

                    var html = template('competition', data);
                    var $ul=$(html);
                    $("#comuCompetitionList .dropload-down").before($ul);
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
                        var str1 ="<div class='no-orders'> <img src='" + ctxStatic + "/images/fa_icon.png' /> <p>未找到相关内容</p> </div>";
                        var $li = $(str1);
                        $("#comuCompetitionList .dropload-down").before($li);
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
    $(function() {
        $("#comuCompetitionList").dropload(droploadConfig);
    });
</script>

</html>