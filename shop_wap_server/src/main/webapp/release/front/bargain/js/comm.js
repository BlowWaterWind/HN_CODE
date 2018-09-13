/**
 * the common js of the project
 *
 * @author: xxxx
 * @version: 2016/10/27 10:51
 *           $Id$
 */
$(function () {   	
    var detailInd;
    $('.list_item').click(function () {
        detailInd = layer.open({
            type: 1,
            title: false,
            closeBtn: 0, //不显示关闭按钮
            anim: 2,
            shadeClose: true, //开启遮罩关闭
            shade: [0.6, '#000'],
            maxWidth: 0,
            resize: false,
            content: $('.popup_con.p_detail_con').html()
        });

        $('.p_detail .p_close').click(function () {
            layer.close(detailInd);
            return false;
        });

        return false;
    });

    var detailInd;
    $('.main_prod').click(function () {
        detailInd = layer.open({
            type: 1,
            title: false,
            closeBtn: 0, //不显示关闭按钮
            anim: 2,
            shadeClose: true, //开启遮罩关闭
            shade: [0.6, '#000'],
            maxWidth: 0,
            resize: false,
            content: $('.popup_con.p_detail_con')
        });

        $('.p_rule .p_close').click(function () {
            layer.close(ruleInd);
            return false;
        });

        return false;
    }); 
    
    var ruleInd;
    $('.rule_link').click(function () {
        ruleInd = layer.open({
            type: 1,
            title: false,
            closeBtn: 0, //不显示关闭按钮
            anim: 2,
            shadeClose: true, //开启遮罩关闭
            shade: [0.6, '#000'],
            maxWidth: 0,
            resize: false,
            content: $('.popup_con.p_rule_con')
        });

        $('.p_rule .p_close').click(function () {
            layer.close(ruleInd);
            return false;
        });

        return false;
    });
});

//弹出信息提示框
function popTips(msg) {
    $(".p_tips_con .p_content > p").html(msg);
    var tipsInd = layer.open({
        type: 1,
        title: false,
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        shade: [0.6, '#000'],
        maxWidth: 0,
        resize: false,
        content: $('.popup_con.p_tips_con').html()
    });

    $('.p_tips .bt, .p_tips .p_close').click(function () {
        layer.close(tipsInd);
        return false;
    });
    return false;
}
 
 //弹出短信登录
function popLgin(){
	  var loginInd = layer.open({
	        type: 1,
	        title: false,
	        closeBtn: 0, //不显示关闭按钮
	        anim: 2,
	        shadeClose: true, //开启遮罩关闭
	        shade: [0.6, '#000'],
	        maxWidth: 0,
	        resize: false,
	        //content: $('.popup_con.p_login_con').html()
	        content: $('.popup_con.p_login_con')
	    });

	    $('.p_login .p_close').click(function () {
	        layer.close(loginInd);
	        return false;
	    });
}

//弹出关注公众号
function popGuanzhu(){
	 var guanzhuInd = layer.open({
	        type: 1,
	        title: false,
	        closeBtn: 0, //不显示关闭按钮
	        anim: 2,
	        shadeClose: true, //开启遮罩关闭
	        shade: [0.6, '#000'],
	        maxWidth: 0,
	        resize: false,
	       // content: $('.popup_con.p_guanzhu_con').html()
	        content: $('.popup_con.p_guanzhu_con')
	    });

	    $('.p_guanzhu .p_close').click(function () {
	        layer.close(guanzhuInd);
	        return false;
	    }); 
}
 
//弹出分享提示
function popShare(){
	var shareWxInd = layer.open({
        type: 1,
        title: false,
        closeBtn: 0, //不显示关闭按钮
        anim: 2,
        shadeClose: true, //开启遮罩关闭
        shade: [0.6, '#000'],
        maxWidth: 0,
        offset: 't',
        resize: false,
        //content: $('.popup_con.p_sharewx_con').html()
         content: $('.popup_con.p_sharewx_con')
    });

    $('.p_sharewx .p_close').click(function () {
        layer.close(shareWxInd);
        return false;
    }); 
}

//是否微信
function is_weixin() {
	  var ua = navigator.userAgent.toLowerCase();
	  if (ua.match(/MicroMessenger/i) == "micromessenger") {
		  return true;
	  } else {
		  return false;
	  }
} 
