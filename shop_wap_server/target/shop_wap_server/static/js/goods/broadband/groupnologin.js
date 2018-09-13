var serialNumber = "";//号码
var url = getQueryString("url");
//1 登录判断
$(function() {
    try {
        MobilePhoneApp.isLogin(getLoginFalg);
    } catch(e) {
        if (url != null && url != ''){
            window.location.href = url;
        }
    }

});

//2 获取用户信息
var getLoginFalg = function(falg) {
    if (!falg) {
        //没有登录
        MobilePhoneApp.showLogin();
    } else {
        //获取用户信息
        MobilePhoneApp.getUserInfo(loadNum);
    }
}


var loadNum = function(phone) {
    serialNumber = phone;
    MobilePhoneApp.encryptString(serialNumber, validateNewFc_afterEncrypt);
}

function validateNewFc_afterEncrypt(str) {
    //此时调用ajax请求，str为号码密文
    $.ajax({
        url :ctx+'/GroupNoLogin/initPage',
        type:"post",
        data:{
            "serialNumber":str
        },
        dataType: "json",
        success: function (data) {
            window.location.href = url;
        },
        error:function(data){
        }
    });


}
/**
 * 获取url中的参数
 * @param name
 * @returns {null}
 */
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}





