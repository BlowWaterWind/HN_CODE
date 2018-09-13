<%--
  Created by IntelliJ IDEA.
  User: cc
  Date: 2018/1/31
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
    <title>开卡激活</title>
    <link rel="stylesheet" href="${ctxStatic}/css/wap.o2o.sim.css"/>
    <%--插码相关--%>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>
</head>
<body>
<div class="container">
    <div class="return-msg">
        <div class="return-msg-icon Grid -center"><img src="${ctxStatic}/images/o2osim/icon-success.png"/></div>
        <h3>下单成功</h3>
        <div class="return-tips">
            <p>恭喜您，下单成功，感谢您对中国移动的支持，请及时认证激活，以方便使用！</p>
        </div>
    </div>
    <div class="Grid -center">
        <button class="btn-large btn-blue">我要认证激活</button>
    </div>
</div>
<script src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
<script src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
<script src="${ctxStatic}/ap/lib/layer.js"></script>
<script src="${ctxStatic}/js/o2o/simOnline/public.js"></script>
<script type="text/javascript">
    $(".btn-blue").click(function(){
       //todo 带着sim卡号跳转到激活页面
        window.location.href = "https://smrz.realnameonline.cn:20106/rnmsol/realnameactive/realNameActivateM.html?UID=29873390F7B74EF4827C14F5A68C86A1&CHANID=E008&WT.ch_id=wh01&WT.mc_id=HNRZSMRZ170900I0001";

        //window.location.href = "http://url.cn/53HsOXv";
    });
</script>
</body>
</html>
