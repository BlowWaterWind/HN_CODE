<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!--成功 弹框提示-->
<div id="noticeSuccessId" class="modal-wrap noticeSuccessId">
    <div class="mask-layer"></div>
    <div class="base-modal">
        <div class="animate slideInDown">
            <div onclick="toggleModal('noticeSuccessId')" class="modal-close"></div>
            <div class="modal-content">
                <div class="return-img"><img src="${ctxStatic}/images/icon-success.png" alt="成功"/></div>
                <div class="title"> </div>
                <div class="desc"> </div>
            </div>
            <div class="modal-ft Grid -right"><a href="javascript:;" onclick="toggleModal('noticeSuccessId')" class="cancel">确定</a></div>
        </div>
    </div>
</div>
<!--成功弹框提示 end-->

<!--失败 弹框提示-->
<div id="noticeFailId" class="modal-wrap noticeFailId">
    <div class="mask-layer"></div>
    <div class="base-modal">
        <div class="animate slideInDown">
            <div onclick="toggleModal('noticeFailId')" class="modal-close"></div>
            <div class="modal-content">
                <div class="return-img"><img src="${ctxStatic}/images/icon-error.png" alt="失败"/></div>
                <div class="title"> </div>
                <div class="desc"> </div>
            </div>
            <div class="modal-ft Grid -right"><a href="javascript:;" onclick="toggleModal('noticeFailId')" class="cancel">确定</a></div>
        </div>
    </div>
</div>
<!--失败弹框提示 end-->