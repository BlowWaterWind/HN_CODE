//弹窗禁用滚动条
var bodyEl = document.body;
var top = 0;
$.stopBodyScroll = function (isFixed) {
	if (isFixed) {
		top = window.scrollY;
		bodyEl.style.position = 'fixed';
		// bodyEl.style.top = -top + 'px';
		bodyEl.style.top = 0;
		bodyEl.style.left = 0;
		bodyEl.style.right = 0;
		bodyEl.style.bottom = 0;
	} else {
		bodyEl.style.position = '';
		bodyEl.style.top = '';
		bodyEl.style.left = '';
		bodyEl.style.right = '';
		bodyEl.style.bottom = '';
		window.scrollTo(0, top); // 回到原先的top
	}
}

//modal
function toggleModal(modal) {
    if ($("#" + modal)) {
        $("#" + modal).toggle();
    }
    if ($("#" + modal).is(":hidden")) {
        $.stopBodyScroll(false);
    } else {
        $.stopBodyScroll(true);
    }
}

/**
 * 弹框的方法
 * @param modal 弹框ID
 * @param title 弹框的标题
 * @param desc  弹框的说明
 */
function toggleModal(modal,title,desc) {
    if ($("#" + modal)) {
        $("#" + modal).toggle();
    }
    if(title!=null && title!=""){
        $("."+modal).find(".title").html(title);
	}
    if(desc!=null && desc!=""){
        $("."+modal).find(".desc").html(desc);
    }

    if ($("#" + modal).is(":hidden")) {
        $.stopBodyScroll(false);
    } else {
        $.stopBodyScroll(true);
    }
}

$(function () {
	// 遮罩层绑定关闭弹窗
	$('.mask-layer').bind('click', function () {
		$(this).parent('.modal-wrap').toggle();
		$.stopBodyScroll(false);
	});
	// 评分
	$('.rating-star').bind('click', function (index) {
		var labelFor = $(this).prop("for");
		var labelForVal = $("#" + labelFor).val();
		console.log("您给的评分：" + labelForVal);
	});
});

