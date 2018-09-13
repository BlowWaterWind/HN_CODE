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

//提示弹框
var toggleModal = function (id, content, btn, dest) {
    var thisId = "#" + id;
    if ($(thisId)) {
        $(thisId + " p").html(content);
    }
    if (dest != undefined) {
        $(thisId + " a").removeAttr('data-dismiss');
        $(thisId + " a").html(btn);

        $(thisId + " a").attr('href', dest);
    } else {
        if ($(thisId + " a").attr('data-dismiss') == undefined) {
            $(thisId + " a").attr('data-dismiss',"modal");
        }
    }

    $(thisId).modal('toggle');
};