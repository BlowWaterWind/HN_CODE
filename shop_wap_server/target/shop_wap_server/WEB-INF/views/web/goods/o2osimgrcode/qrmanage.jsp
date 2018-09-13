<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/8/14
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

    <title>二维码</title>
    <link href="${ctxStatic}/css/sim/simgroup/o2o.sim.group.css?v=1" rel="stylesheet">


    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/swiper/swiper.jquery.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/modal/modal.js"></script>

    <link href="${ctxStatic}/css/sim/wap-simcommon.css?v=1" rel="stylesheet"><!--这个页面选号的弹出框-->
    <c:set var="myTfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>
</head>
<body class="bg-white">
<div class="container">
    <div class="common-navbar-wrap">
        <div class="common-navbar ">
            <%--<div class="navbar-left">--%>
                <%--<a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>--%>
            <%--</div>--%>
            <h3 class="navbar-title">二维码</h3>
        </div>
    </div>
    <div id="hasQr" <c:if test="${hasQr == false}">
        style="display: none"
    </c:if>>
        <div><img class="ma-w" src="${myTfsUrl}${orderRecmd.recmdUserQrcode}" alt=""></div>
        <div class="ma-txt">
            <p><span class="ma-txt-name">省份：</span><span>湖南省</span></p>
            <p><span class="ma-txt-name">员工归属：</span><span>${orderRecmd.chanId}</span></p>
            <p><span class="ma-txt-name">员工标识：</span><span>${orderRecmd.recEmpID}</span></p>
        </div>
        <div class="tc mt20"><a class="btn btn-blue w200 tc " onclick="deleteQr('${orderRecmd.recmdId}')">删除</a></div>
    </div>
    <div id="genQr" <c:if test="${hasQr == true}">
        style="display: none;"
    </c:if>>
        <div class="tc mt20" style="margin-top: 6rem;"><a class="btn btn-blue w200 tc " onclick="genQr('${confId}')">生成推荐码</a></div>
    </div>
</div>


<%-- 公用弹窗 --%>
<div id="sorry-modal" class="mask-layer">
    <div class="modal small-modal">
        <a id="sorry-modal-a" href="javascript:void(0)" class="modal-close" onclick="toggleModal('sorry-modal')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group">
            <a href="javascript:void(0)" class="confirm-btn">确认</a>
        </div>
    </div>
</div>

<!-- 信息确认弹窗 -->
<div id="delete-toggle" class="mask-layer">
    <div class="modal large-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('delete-toggle')"></a>
        <div class="modal-content">
            <div class="pt-30"></div>
            <p class="text-center _pomptTxt"></p>
        </div>
        <div class="btn-group both-btn">
            <a href="javascript:void(0)" id="infoEdit" class="confirm-btn">取消</a>
            <a href="javascript:void(0)" onclick="confirmDelete('${orderRecmd.recmdId}')" class="confirm-btn"
               otype="button" otitle="O2O号码绑卡_${conf.confId}-确定">确定</a>
        </div>
    </div>
</div>

</body>
<script type="text/javascript" src="${ctxStatic}/js/goods/sim/common-sim.js?v=6"></script>
<script type="text/javascript">

    function deleteQr() {
        toggleModal("delete-toggle", "确认删除推荐二维码吗？");
    }

    function confirmDelete(recmdId) {
        $.ajax({
            url: 'deleteQr',
            data: {recmdId: recmdId},
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data.code == '0') {
                    $("#hasQr").hide();
                    $("#genQr").show();
                } else {
                    toggleModal("sorry-modal", "删除失败,请重试!");
                }
            },
            error: function () {
                toggleModal("sorry-modal", "删除失败,请重试!");
            }
        });
    }

    function genQr(confId) {
        $.ajax({
            url: 'genQr',
            data: {confId: confId},
            dataType: "json",
            type: "post",
            success: function (data) {
                if (data.code == '0') {
                    window.location.reload();
                } else {
                    toggleModal("sorry-modal", "删除失败,请重试!");
                }
            },
            error: function () {
                toggleModal("sorry-modal", "删除失败,请重试!");
            }
        });
    }

</script>

</html>
