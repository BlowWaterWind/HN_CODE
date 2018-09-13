<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script id="navigationRightListTmpl" type="text/html">
  {{each childCatalogList as catalog i}}
      <li><a   href="http://10.154.73.87:82/{{catalog.catalogUrl}}"><i class="{{catalog.catalogCss}}"></i>{{catalog.catalogName}}</a></li>
   {{/each}}
</script>
 <a class="icon-right"></a>

 <ul class="nav-slide" id="navigationRightList">
		    <li class="nav-arrow"></li>
		    <li><a   href="http://10.154.73.87:18910"><i class="icon-nav-wt-home"></i>首页</a></li>
		    <li><a href="/shop-wap/release/front/wap/market/index.html"><i class="icon-nav-home"></i>最新活动</a></li>
		    <li><a href="/shop-wap/memberInfo/toMemberCenter"><i class="icon-nav-wo"></i>个人中心</a></li>
		
		  </ul>
  <div class="modal-bg"></div><!-- 别漏掉了这个div，必需跟着nav-slide -->
