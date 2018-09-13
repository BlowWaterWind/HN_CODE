<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--底部菜单start-->
<div class="footer-wrap">
  <nav class="footer-nav"><!-- 当前所属页面请在相应的按钮上加上class active -->
      <a href="${ctx}" class="app-link shop">移动商城</a>
      <a href="<%=UserUtils.wantingServerHost%>/wap" class="app-link recharge">掌厅首页</a>
      <a href="${ctx}goodsQuery/linkToGoodsList?categoryId=10000001&queryListpageCode=PHONE_LISTPAGE_B2C&queryListpageId=100" class="app-link shop active">选机中心</a>
      <a href="${ctx}cart/linkToCartList" class="app-link shopping-cart">购物车</a>
      <a href="${ctx}memberInfo/toMemberCenter" class="app-link user-center">个人中心</a>
  </nav>
   
</div>
<script type="text/javascript">
    $(function(){
        var curWwwPath = window.document.location.href;
        var $item = $(".footer-nav >a");
        var isActive = false;
        for(var i=0;i<$item.length;i++)
        {
            var aHref = $item.eq(i).attr("href");
            if(curWwwPath.lastIndexOf(aHref)>-1)
            {
                $item.eq(i).addClass("active");
                isActive = true;
            }else{
                $item.eq(i).removeClass("active");
            }
        }
        if(!isActive)
        {
            $item.eq(0).addClass("active");
        }
        var val = getCookie("isShowHead");
        // 判断cookie中是否有isShowHead参数，若有，则隐藏底部菜单栏
        if(val !=null && val == "1"){
            $(".footer-wrap").hide();
        }
    });

    // 根据名字读取cookie
    function getCookie(name)
    {
        var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg)){
            return unescape(arr[2]);
        }else{
            return null;
        }
    }
    </script>

<!--底部菜单end-->
     <script type="text/javascript" charset="utf-8" src="${ctxStatic}/js/ajaxSetup.js"></script>