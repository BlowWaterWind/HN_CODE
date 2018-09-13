<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>

<!--弹出层-->
<button id="alertMsgInfoBT" type="button" class="buy" data-toggle="modal" data-target="#alertMsgInfo" aria-hidden="true"
        style="display:none;"></button>
<div class="modal fade  modal-prompt" id="alertMsgInfo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modle-center">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only" id="btnClose">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <div class="pop_670_cont" id="alertMsgInfo_content"></div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" class="btn btn-blue btn-2x" onclick="hideMe();">确  定</a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<script type="text/javascript">

    function __innnerMessageShow() {
        if ($("#alertMsgInfo").is(":hidden")) {
            $("#alertMsgInfo").modal("show");
        }
    }

    function __innnerErrorMessageShow() {
        if ($("#alertMsgInfoError").is(":hidden")) {
            $("#alertMsgInfoError").modal("show");
        }
    }

    /**
     * 提示消息
     * @param obj
     */
    function showAlert(obj,callback) {
        $("#alertMsgInfo_content").html(obj);
        __innnerMessageShow();
        if (typeof callback === "function") {
            setTimeout(callback, 2000);
        }
    }

    function hideMe() {
        $("#alertMsgInfo_content").html("");
        $("#btnClose").click();
    }

</script>