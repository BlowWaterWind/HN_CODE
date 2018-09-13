$(function() {

    function draw(show_num) {
        var canvas_width=$('#canvas').width();
        var canvas_height=$('#canvas').height();
        var canvas = document.getElementById("canvas");//获取到canvas的对象，演员
        var context = canvas.getContext("2d");//获取到canvas画图的环境，演员表演的舞台
        canvas.width = canvas_width;
        canvas.height = canvas_height;
        var sCode = "A,B,C,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
        var aCode = sCode.split(",");
        var aLength = aCode.length;//获取到数组的长度

        for (var i = 0; i <= 3; i++) {
            var j = Math.floor(Math.random() * aLength);//获取到随机的索引值
            var deg = Math.random() * 30 * Math.PI / 180;//产生0~30之间的随机弧度
            var txt = aCode[j];//得到随机的一个内容
            show_num[i] = txt.toLowerCase();
            var x = 0 + i * 20;//文字在canvas上的x坐标
            var y = 20 + Math.random() * 8;//文字在canvas上的y坐标
            context.font =canvas_height/1.6 + 'px SimHei';

            context.translate(x, y);
            context.rotate(deg);

            context.fillStyle = randomColor();
            context.fillText(txt, 0, 0);

            context.rotate(-deg);
            context.translate(-x, -y);
        }
        for (var i = 0; i <= 3; i++) { //验证码上显示线条
            context.strokeStyle = randomColor();
            context.beginPath();
            context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.stroke();
        }

    }


    function randomColor() {//得到随机的颜色值
        var r = Math.floor(Math.random() * 256);
        var g = Math.floor(Math.random() * 256);
        var b = Math.floor(Math.random() * 256);
        return "rgb(" + r + "," + g + "," + b + ")";
    }

    var show_num = [];
    draw(show_num);

    $("#canvas").on('click',function(){
        draw(show_num);
    })

    $("input").focus(function(){
        $(this).parent().find('.input-text').addClass('hide');
    });

    $("input").blur(function(){
        if($(this).val()==""){
            $(this).parent().find('.input-text').removeClass('hide');
        }
    });

    $("#member_name").blur(function(){
        if($("#member_name").val()==""){
            $("#name_text").removeClass("hide");
            $("#memberName").addClass("input-fr");
            return;
        }else{
            $("#name_text").addClass("hide");
            $("#memberName").removeClass("input-fr");
        }
    });

    function encode64(password) {

        return strEnc(password,keyStr);
    }

    function CheckStr(v_pw){
        var patrn_shuzi=/^[0-9]$/;
        var patrn_zimu=/^[a-z]|[A-Z]$/;
        var patrn_teshu=/^[^A-Za-z0-9]$/;
        var flag=0;
        var s = v_pw.split('');

        for(var i=0;i<s.length;i++){
            if (patrn_shuzi.exec(s[i])) {
                flag = flag+1;
                break;
            }
        }
        for(var i=0;i<s.length;i++){
            if (patrn_zimu.exec(s[i])) {
                flag = flag+1;
                break;
            }
        }
        for(var i=0;i<s.length;i++){
            if (patrn_teshu.exec(s[i])) {
                flag = flag+1;
                break;
            }
        }
        if (flag != 3) {
            return false;
        }
        return true;
    }



    $("#submitRegBtn").click(
        function() {

            /*插码*/
            try {
                if(window.Webtrends){
                    Webtrends.multiTrack({
                        argsa: ["WT.si_n","GJJ_Reg",
                            "WT.si_x","21"],
                        delayTime: 100
                    })
                }else{
                    if(typeof(dcsPageTrack)=="function"){
                        dcsPageTrack ("WT.si_n","GJJ_Reg",true,
                            "WT.si_x","21",true);
                    }
                }
            }catch(e){
                console.log(e)
            }

            var Regx = /^[A-Za-z0-9]*$/;
            if ($("#member_name").val()=="") {
                showAlert("用户名不能为空");
                draw(show_num);
                return;
            }
            var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
            if (!Regx.test($("#member_name").val())&&filter.test($("#member_name").val())) {
                showAlert("用户名为包含A-Z的字母，0-9数字，不允许有特殊符号");
                draw(show_num);
                return;
            }
            var pattenEmail = new RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/);
            if (!pattenEmail.test($("#member_email").val())) {
                showAlert("请输入正确的邮箱");
                draw(show_num);
                return;
            }
            if ($("#member_passwd").val()=="") {
                showAlert("密码不能为空");
                draw(show_num);
                return;
            }
            if($("#member_passwd").val().length<8||$("#member_passwd").val().length>20){
                showAlert("密码长度为8-20位");
                draw(show_num);
                return;
            }
            if (!CheckStr($("#member_passwd").val())) {
                showAlert("密码需要包含A-Z的字母，0-9数字，特殊符号");
                draw(show_num);
                return;
            }

            if ($("#confirm_passwd").val() != $("#member_passwd").val()) {
                showAlert("两次密码输入不一致！");
                draw(show_num);
                return;
            }
            if ($("#captcha").val().length == 0) {
                showAlert("验证码不能为空");
                draw(show_num);
                return;
            }

            var vcode = $("#captcha").val();//输入的验证码
            var num = show_num.join("");//生成的验证码

            if(vcode != num){
                showAlert("验证码不正确");
                draw(show_num);
                return;
            }

            var password = encode64($("#member_passwd").val());
            //$("#loading").show();
            $.ajax({
                type:'post',
                url :  submitUrl,
                cache:false,
                context: $(this),
                dataType : 'json',
                data : {member_email:$("#member_email").val(),member_name:$("#member_name").val(),member_passwd:password,captcha:$("#captcha").val(),CSRFToken:$("#csrftoken").val()},
                success:function(data) {
                    //$("#loading").hide();
                    //var res=data.replace('\"','').replace('\"','');
                    if(data =="注册成功" ){

                        try {
                            if(window.Webtrends){
                                Webtrends.multiTrack({
                                    argsa: ["WT.si_n","GJJ_Reg",
                                        "WT.si_x","99"],
                                    delayTime: 100
                                })
                            }else{
                                if(typeof(dcsPageTrack)=="function"){
                                    dcsPageTrack ("WT.si_n","GJJ_Reg",true,
                                        "WT.si_x","99",true);
                                }
                            }
                        }catch(e){
                            console.log(e)
                        }

                        showAlert("注册成功，请领券!");
                        $('.mask,.signIn').hide();
                    }else{

                        try {
                            if(window.Webtrends){
                                Webtrends.multiTrack({
                                    argsa: ["WT.si_n","GJJ_Reg",
                                        "WT.si_x","-99"],
                                    delayTime: 100
                                })
                            }else{
                                if(typeof(dcsPageTrack)=="function"){
                                    dcsPageTrack ("WT.si_n","GJJ_Reg",true,
                                        "WT.si_x","-99",true);
                                }
                            }
                        }catch(e){
                            console.log(e)
                        }

                        draw(show_num);
                        showAlert(data);
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    //$("#loading").hide();
                    draw(show_num);
                    showAlert("服务中断，请重试");
                }
            });

        });

    function gotoUrl(){
        window.location.href=loginUrl;
    }

});