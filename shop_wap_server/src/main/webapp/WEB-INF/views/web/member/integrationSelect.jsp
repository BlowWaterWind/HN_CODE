<!DOCTYPE html>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.member.entity.MemberLogin" %>
<%@ page import="java.util.List" %>
<%@ page import="com.ai.ecs.integral.entity.IntegrayDetail" %>
<%@ page import="com.ai.ecs.integral.entity.IntegrayAccount" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>湖南移动商城</title>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>

<link  href="<%=request.getContextPath()%>/static/css/member/wt-sub.css" rel="stylesheet" type="text/css" />
<link  href="<%=request.getContextPath()%>/static/css/member/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="<%=request.getContextPath()%>/static/js/member/tab.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/iscroll.js" ></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/date.js" ></script>
<script type="text/javascript">
$(function(){
    $('#beginTime1').date();
    $('#beginTime2').date();
});
</script>
  <%@ include file="/WEB-INF/views/include/message.jsp"%>
</head>
<%
MemberLogin member=(MemberLogin)request.getAttribute("member");
if(member==null){
    member=new MemberLogin();
}
IntegrayAccount integrayAccount=(IntegrayAccount)request.getAttribute("integrayAccount");
if(integrayAccount==null){
    integrayAccount=new IntegrayAccount();
}
List<IntegrayDetail> integrayList=(List<IntegrayDetail>)request.getAttribute("integraylist");
%>
<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left"   href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>积分查询</h1>
    <sys:headInfo/>
  </div>
</div>
<div class="container">
  <div class="yw-con mt10"> 
    <!--客户信息 start-->
    <div class="kf-info"><img src="<%=request.getContextPath()%>/static/images/member/wt-images/user_pic.png" class="user-img" />
      <div class="user-login">
        <p><%=HtmlUtils.defaultString(member.getMemberLogingName())%></p>
        <div class="kf-adress">${blurryPhone}</div>
      </div>
      <span class="dq-jf">当前积分：<%=HtmlUtils.defaultNumber(integrayAccount.getMemberTotal())%></span> </div>
    <!--客户信息 end--> 
    <!--条件查询 start-->
   <!--  <div class="kf-reach">
      <div class="cx-date"><label>日期：</label>
        <div class="kf-date"><input id="beginTime1" class="kbtn" value="2016-02-15" /><input id="beginTime2" class="kbtn" value="2016-02-19" /></div>
        <input type="submit" value="查询" class="cx-btn" />
      </div>
      <div id="datePlugin"></div>
    </div> -->
    <!--条件查询 end--> 
    <!--积分查询结果 start-->
    <div class="Tab">
      <div id="box" class="cx-con">
        <ul class="TabHead jf-list jf-tc" id="topfloat">
          <li class="on"><a href="javascript:;">总额查询</a></li>
          <li><a href="javascript:;">明细查询</a></li>
        </ul>
      </div>
      <div class="TabNote">
      <!--总额查询-->
        <div class="none" style="display:block;">
        当前可用积分总额：<%=HtmlUtils.defaultNumber(integrayAccount.getMemberTotal())%>
        </div>
        <!--明细查询-->
        <div class="block" style="display:none;">
          <table cellpadding="0" cellspacing="0" class="kf-table">
            <tr>
              <th>使用截止时间</th>
              <th>使用的积分值</th>
              <th>使用情况</th>
            </tr>
            <%
            if(integrayList!=null){
                for(IntegrayDetail detail:integrayList){
            %>
            <tr>
              <td><%=HtmlUtils.defaultDate(detail.getIntegrayDetailEndTiem())%></td>
              <td><%=HtmlUtils.defaultNumber(detail.getIntegrayDetailAmount())%></td>
              <td><%=HtmlUtils.defaultString(detail.getIntegrayDetailDesc())%></td>
            </tr>
            <%
            }}
            %>
          </table>
        </div>
      </div>
    </div>
    <!--积分查询结果 end--> 
  </div>
</div>

<%@ include file="../../include/navBottom.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/memberCenterBottom.js"></script>

</body>
</html>
