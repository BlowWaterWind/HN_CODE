<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="WT.si_n" content="服务标准" />
    <meta name="WT.si_x" content="业务详情" />
    <link href="${ctxStatic}/css/main.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/wt-sub.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/kd.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/media-style.css" rel="stylesheet" type="text/css" />
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <title>湖南移动网上营业厅</title>
</head>

<body>

<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>宽带服务</h1>
  </div>
</div>
<div class="container">
   <table cellpadding="0" cellspacing="0" class="servic-tabs">
   	  <tr>
   	  	 <th>服务名称</th>
   	  	 <th>服务标准</th>
   	  </tr>
   	  <tr>
   	  	 <td width="30%">家庭宽带服务时间</td>
   	  	 <td>8：00-20：00（含节假日）</td>
   	  </tr>
   	  <tr>
   	  	 <td>转移机预约时长</td>
   	  	 <td>≤4个工作时</td>
   	  </tr>
   	  <tr>
   	  	 <td>装移机平均时长</td>
   	  	 <td>
   	  	 	 <p>城镇≤48小时</p>
   	  	 	 <p>农村≤72小时</p>
   	  	 </td>
   	  </tr>
   	  <tr>
   	  	 <td>故障申告首次响应时长</td>
   	  	 <td>≤4个工作时</td>
   	  </tr>
   	  <tr>
   	  	 <td>故障维修时长</td>
   	  	 <td>
   	  	 	 <p>城镇≤24小时</p>
   	  	 	 <p>农村≤48小时</p>
   	  	 </td>
   	  </tr>
   	  <tr>
   	  	 <td>投诉处理时长</td>
   	  	 <td>普通投诉不长于48小时；重大投诉不长于8小时；批量投诉不长于24小时；重复投诉不长于24小时；升级投诉不长于7×24小时</td>
   	  </tr>
   </table>
</div>
<!--底部菜单 start-->
<div class="fix-br">
<div class="affix foot-box">
 <ul class="footer-list container">
 	 <li class="active" ><a href="javascript:">服务标准</a></li>
 	 <li><a href="${ctx}broadband/installProcedure">安装流程</a></li>
 	 <li><a href="${ctx}broadband/faultReport">故障申告</a></li>
	</ul>
<!--底部菜单 end-->
</div>
</div>

</body>
</html>