<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/main.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/kd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/media-style.css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>账单查询</title>
        <script>
            function changeMonth(obj){
                $(".flex-menu li").each(function(){
                    $(this).removeClass("active");
                });
                $(obj).addClass("active");
                var billDate=$(obj).attr("data-value");
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        async: false,
                        data : {
                            billDate: billDate
                        },
                        url: ctx+"/myBroadband/billQueryAjax",
                        success: function (data) {
                            $("#firstday").text(data.firstday);
                            $("#fee").text(data.fee);
                            $("#lastday").text(data.lastday);
                        },
                        error: function (data) {
                            showAlert("系统错误，请稍后再试");
                        }
                    });
            }
            $(function () {
                //提速
                $('#speedup').on('click', function () {
                    var rate=$("#oldBandWidth").val();
                    if(rate>=100||rate.indexOf("100")>0 ){
                        showAlert("您的宽带已经是最高速率");
                        return;
                    }
                    $("#form1").submit();
                });
            });
        </script>
</head>

<body>
<div class="top container">
    <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
        <h1>账单查询</h1>
    </div>
</div>
<div class="container">
    <ul class="flex-menu">
        <c:forEach items="${queryDateList}" var="date" varStatus="status">
            <c:choose>
                <c:when test="${status.last}">
                    <li class="active" onclick="changeMonth(this)" data-value="<fmt:formatDate value='${date}' pattern='yyyyMM'/>" ><fmt:formatDate value="${date}" pattern="MM月"/></li>
                </c:when>
                <c:otherwise>
                    <li onclick="changeMonth(this)" data-value="<fmt:formatDate value='${date}' pattern='yyyyMM'/>"><fmt:formatDate value="${date}" pattern="MM月"/></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
    <form action="${ctx}/broadbandSpeedUp/confirmAccount" id="form1" name="form1" method="post" >
        <input type="hidden" id="broadbandAccount" name="bandAccount" value="${broadband.accessAcct}"/>
        <input type="hidden" id="oldBandWidth" name="oldBandWidth" value="${broadband.rate}"/>
        <input type="hidden" id="endTime"  name="endTime" value="${broadband.endTime}"/>
        <input type="hidden" name="discntName" value="${broadband.discntName}"/>
        <input type="hidden" name="addrDesc" value="${broadband.addrDesc}"/>
        <input type="hidden" name="startTime" value="${broadband.startTime}"/>

    </form>
    <!--内容区域 start-->
    <div class="inform-info">
        <div class="inform-list">
            <div class="inform-title">套餐信息</div>
            <div class="inform-con">
                <p>套餐名称<span class="t-r">${broadband.discntName}</span></p>
                <p>宽带速率<span class="t-r">${broadband.rate}M</span></p>
                <p>账单开始日<span class="t-r" id="firstday">${firstday}</span></p>
                <p>账单结束日<span class="t-r" id="lastday">${lastday}</span></p>
            </div>
        </div>
        <div class="inform-list">
            <div class="inform-title">套餐费用</div>
            <div class="inform-con">
                <%--<p>套餐保底消费<span class="t-r">48元<b class="f12 font-red">不计入宽带费用</b></span></p>--%>
                <p>宽带费用<span class="t-r" id="fee">${billMaps["FEE2"]==null?0:billMaps["FEE2"]/100}元</span></p>
                <%--<p>互联网电视<span class="t-r">10元</span></p>--%>
            </div>
        </div>
    </div>
    <!--内容区域 end-->
    <div class="broad-btn">
        <a href="javascript:void(0)" class="btn btn-blue" id="speedup">宽带提速</a>
    </div>
</div>
</body>
</html>