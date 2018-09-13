//弹窗操作
//layer.js插件地址 http://layer.layui.com/mobile/

/**
 * 将表单元素序列化为JSON对象
 * 基于jQuery serializeArray()
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


$(function () {
    var modal1 = $('.layer-modal-1').html();
    $('#findCardNmber').bind('click', function () {
        layer.open({
            type: 1,
            content: modal1
        });

    });
});


function toggleModal(modal) {
    if ($("#" + modal)) {
        $("#" + modal).toggle();
    }
}