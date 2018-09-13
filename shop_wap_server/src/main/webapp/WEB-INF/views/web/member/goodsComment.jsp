<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.member.vo.UserRatingResult"%>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils"%>
<%@ page import="java.util.List" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="<%=request.getContextPath()%>/static/css/member/application.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/static/css/member/list.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/start.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/commentOrder.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/jquery.raty.min.js"></script>
   <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
List<UserRatingResult> ratingList=(List<UserRatingResult>)request.getAttribute("ratingList");

%>
<body>
<div class="top container">
  <div class="header sub-title"><!--<a class="icon-left icon-home icon2"></a>--><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
    <h1>商品评价</h1>
     <sys:headInfo/>
  </div>
</div>
<div class="container white-bg">
  <div class="good-detail sift-mg">
    <div class="new-goods-details">
      <div class="new-gd-introduce">
        <div class="new-fl w56 new-mg-r12"> <span class="new-span-block"> <span class="new-txt36">99</span> <span class="new-txt-sign"></span></span> <span class="new-span-block new-mg-t5">好评度</span> </div>
        <div class="new-gd-txt3"> 
        <span class="new-span-block"><span>好评</span><span class="new-txtb8" >（99%）</span>
        <span class="new-gd-bar new-mg-l12"><span style="width: 99%;"></span></span></span>
        <span class="new-span-block"><span>中评</span><span class="new-txtb8">（1%）</span>
        <span class="new-gd-bar new-mg-l12"><span style="width: 1%;"></span></span></span> 
        <span class="new-span-block"><span>差评</span><span class="new-txtb8">（0%）</span>
        <span class="new-gd-bar new-mg-l12"><span style="width: 0%;"></span></span></span> </div>
      </div>
    </div>
  </div>
  <div id="outer" class="tab-ct">
    <ul id="tab" class="tab-lr">
      <li class="current"><span>好评</span><strong>43623</strong></li>
      <li><span>中评</span><strong>1550</strong></li>
      <li style="border:none;"><span>差评</span><strong>940</strong></li>
    </ul>
    <div id="content" class="tab-dr tab-dl">
      <div class="pj-list">
        <ul>
        <%if(ratingList!=null){
            for(UserRatingResult res:ratingList){
            %>
          <li>
            <h4>心得：<%=res.getRatingContain()%></h4>
            <span class="pull-left mu-star"> <div id="xing1" data-score="1" style="float:right;margin-right:70%;"></div></span><span class="pull-right tab-cl"><span><%=res.getMemberName()%>…</span><strong><%=DateUtils.formatDateHMS(res.getRatingTime())%></strong></span> </li>
         <%}}%>
        </ul>
        <div class="jz-list"><a href="#">加载更多</a></div>
      </div>
      
    </div>
  </div>
</div>

<script>
    $(document).ready(function(){
            var $li = $('#tab li');
            var $ul = $('#content .pj-list');
                        
            $li.click(function(){
                var $this = $(this);
                var $t = $this.index();
                $li.removeClass();
                $this.addClass('current');
                $ul.css('display','none');
                $ul.eq($t).css('display','block');
            });
    });
</script>
<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>
</body>
</html>
