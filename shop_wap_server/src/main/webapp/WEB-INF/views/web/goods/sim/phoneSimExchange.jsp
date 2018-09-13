<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <%--<meta name="format-detection" content="telephone=no">--%>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>在线售卡H5-我的订单</title>
    <link href="${ctxStatic}/css/sim/wap_list.css" rel="stylesheet">
    <link href="${ctxStatic}/css/dropload/dropload.css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/picker.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/public.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/dropload/dropload.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
        <style type="text/css">
            .detail_div{
                display: flex;
                flex-direction: column;
                text-align: left;
                padding-left: 10px;
                font-size: 20px;
                color: black;
                width: 100%;
                height: auto;
            }
            #phones{
                font-weight: bolder;
            }
            #iccids{
                font-weight: bolder;
            }

        </style>

</head>


<body>
<!-- start container -->
<div class="container bg-gray">
    <div class="topbar">
        <a href="javascript:window.history.back();" class="icon icon-return">返回</a>
       <%-- <a href="javascript:window.history.back();" class="icon icon-close">关闭</a>--%>
        <h3>SIM卡关联查询</h3>
    </div>
    <form:form action="${ctx}recmd/findPhoneSim" method="post" id="form1">
    <input type="hidden" name="statusId" value="${statusId}">
    <input type="hidden" name="memberId" value="${memberId}">
    <!-- end topbar -->
    <div class="tabs">

        <div id="tab-1">
            <c:if test="${memberId==''||memberId==null}">
            <div class="orders-wrap">
                <div class="orders-search">
                    <input type="text" name="psptId" id="psptId" value="${psptId}" placeholder="请输入手机号码或sim卡号进行查询">
                    <input type="button" name="" id="queryOrder" value="查询">
                </div>
            </div>
        </c:if>
        </div>
    </div>
    </form:form>
   <div class="detail_div" id="addTest">
   </div>

    </div>
   </div>

<script>
    $("#queryOrder").click(function () {
        var psptId = $("input[name='psptId']").val();
        if(psptId != ""){
            $.ajax({
                type: 'GET',
                url: "findPhoneSim",//设置请求地址
                dataType: 'json',
                data: {
                    psptId: psptId
                },
                success: function (data) {
                    $('#addTest').html("");
                    var contentHtml = '';
                    if(data.length > 0){
                        contentHtml += "<span>手机号："+"<span id=phones>"+"&nbsp"+data[0].phone+"</span>"+"</span>";
                        if(data[0].iccid == null){
                            contentHtml += "<span>SIM卡号:";
                        }else{
                            contentHtml += "<span>SIM卡号:"+"<span id=iccids>"+"&nbsp"+data[0].iccid+"</span>"+"</span>";
                        }


                    }else{
                        alert('暂未查到相关数据，请核验后重试！');
                    }
                    $('#addTest').prepend(contentHtml);
                }
            });
        }

    });


</script>
</body>

</html>