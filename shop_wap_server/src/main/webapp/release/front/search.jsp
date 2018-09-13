<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- search Begin -->
       <!-- 模版 -->
        <script id="wapNavTmpl" type="text/html">
            <li><a  href="{{navigationUrl}}"><i class="{{styleClass}}"></i>{{navigationName}}</a></li>
        </script>
        <script>
            $(function(){
            var data = {};
               $.ajax({
	         type:'post',
	         contentType:'application/json',
	         url:'/shop-wap//qureryNavigationData',
	         data:JSON.stringify(data),
	         success:function(data)
	         {
	            // 渲染 
	            $("#rightNavigation").append(template('wapNavTmpl',data));
	         },
	         dataType:'json'
	         });
	         
	           $("#keyWordsSel").change(function(){
            		  $("#top-search").attr("action",$(this).find("option:selected").attr("formUrl"));
            		  $("#searchKeyInput").attr("name",$(this).find("option:selected").attr("inputName"));
                 });
                 
                 $("#icon-search").bind("click",function(){
     			  $("#top-search").submit();
     		    })
            });
        </script>
<!-- search Begin -->
        	<form class="top-search" id="top-search" action="/shop-wap/goodsQuery/linkToGoodsList" method="post">
	        <i class="select-arrow"></i>
	        <select id="keyWordsSel">
	            <option value="1"  formUrl="/shop-wap/goodsQuery/linkToGoodsList" inputName="keyWord">商品</option>
	            <option value="2" formUrl="/shop-wap/shopList" inputName="shopName">店铺</option>
	            </select>
	        <input type="text" id="searchKeyInput" name="keyWord" class="form-control" placeholder="搜索商品、店铺">
	        <a href="javascript:void(0)" class="icon-search" id="icon-search"></a> 
        </form>
      <a class="icon-right"></a> <a href="购物车.html" class="icon-top-cart"></a>
<script id="navigationRightListTmpl" type="text/html">
  {{each childCatalogList as catalog i}}
      <li><a   href="http://10.154.73.87:82/{{catalog.catalogUrl}}"><i class="{{catalog.catalogCss}}"></i>{{catalog.catalogName}}</a></li>
   {{/each}}
</script>
<script>
var navright_wap_domain_projectpath = "http://10.154.73.87:82" + "/";
var navright_baseProjectPath_wap = "/shop-wap/";
$(function(){
var url = baseProject + "indexLoad/getRightNavigation"
var params={"catalogCode":"YCCDQE004"};
  $.post(url,params,function(data){
      $("#navigationRightList").append(template("navigationRightListTmpl",data))
  })

})
</script>
  <a class="icon-right"></a> <a href="/shop-wap//cart/linckCartList" class="icon-top-cart"></a>
 </div>
 <ul class="nav-slide" id="navigationRightList">
		    <li class="nav-arrow"></li>
		   
		  </ul>
  <div class="modal-bg"></div><!-- 别漏掉了这个div，必需跟着nav-slide -->
<!-- search End -->
