$(function() {

    $("#submitBtn")
        .click(
            function() {

                /*插码*/
                try {
                    if(window.Webtrends){
                        Webtrends.multiTrack({
                            argsa: ["WT.si_n","GJJ_Login",
                                "WT.si_x","21"],
                            delayTime: 100
                        })
                    }else{
                        if(typeof(dcsPageTrack)=="function"){
                            dcsPageTrack ("WT.si_n","GJJ_Login",true,
                                "WT.si_x","21",true);
                        }
                    }
                }catch(e){
                    console.log(e)
                }


                var loginname = encode64($("#loginname").val()); // 对数据加密
                var loginpass = encode64($("#loginpass").val());
                if ($("#loginname").val() == "") {
                    showAlert("用户名称不能为空");
                    return;
                }
                if ($("#loginpass").val() == "") {
                    showAlert("密码不能为空");
                    return;
                }
                $.ajax({
                    type : 'post',
                    url : loginByNameUrl,
                    cache : false,
                    context : $(this),
                    dataType : 'json',
                    timeout: 30000,
                    data : {
                        loginname : loginname,
                        password : loginpass,
                        //captcha : $("#captcha").val(),
                        CSRFToken:$("#csrftoken").val()
                    },
                    success : function(data) {
                        if (data.msg == "success") {

                            try {
                                if(window.Webtrends){
                                    Webtrends.multiTrack({
                                        argsa: ["WT.si_n","GJJ_Login",
                                            "WT.si_x","99"],
                                        delayTime: 100
                                    })
                                }else{
                                    if(typeof(dcsPageTrack)=="function"){
                                        dcsPageTrack ("WT.si_n","GJJ_Login",true,
                                            "WT.si_x","99",true);
                                    }
                                }
                            }catch(e){
                                console.log(e)
                            }

                            showAlert('登录成功');
                            //关闭弹出框
                            $('#userLogin').hide();
                            $('.mask').hide();

                        } else {

                            try {
                                if(window.Webtrends){
                                    Webtrends.multiTrack({
                                        argsa: ["WT.si_n","GJJ_Login",
                                            "WT.si_x","-99"],
                                        delayTime: 100
                                    })
                                }else{
                                    if(typeof(dcsPageTrack)=="function"){
                                        dcsPageTrack ("WT.si_n","GJJ_Login",true,
                                            "WT.si_x","-99",true);
                                    }
                                }
                            }catch(e){
                                console.log(e)
                            }

                            if(data.msg){
                                //showAlert(data.msg);
                                showAlert(data.msg);
                                loginFailToWebtreeds(data.msg);
                            }else {
                                //showAlert(data);
                                showAlert(data);
                            }
                        }
                    },
                    error : function(XMLHttpRequest,
                                     textStatus, errorThrown) {
                        // hideLoadPop();
                        window.location.href=gotoMemberCenterUrl;
                        loginFailToWebtreeds("登录失败");
                    }
                });

            });


    /**
     * 登录失败插码
     */
    function loginFailToWebtreeds(errorMsg){
        try {
            //增加登录失败的插码
            if (typeof(dcsPageTrack)=="function"){
                dcsPageTrack("WT.ti","登录失败", false, "WT.si_x","登录失败", false, "WT.si_n","登录", false, "WT.err_code",errorMsg, true);
            }
        }catch(e){}
    }

});