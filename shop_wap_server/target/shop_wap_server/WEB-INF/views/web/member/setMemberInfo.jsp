<!DOCTYPE html>
<%@page import="java.util.List" %>
<%@page import="com.ai.ecs.member.entity.ThirdLevelAddress" %>
<%@page import="com.ai.ecs.ecm.mall.wap.platform.utils.HtmlUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ai.ecs.member.entity.MemberInfo" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UtilString" %>
<%@ page import="com.ai.ecs.member.entity.MemberVo" %>
<%@ page import="com.ai.ecs.member.entity.MemberLogin" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>湖南移动商城</title>
    <link href="<%=request.getContextPath()%>/static/css/man-center.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/static/css/list.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/static/css/num-card.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/static/js/member/select_default_value.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/js/member/setMemberInfo.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/member/securityencode.js" charset="utf-8"></script>

    <%@ include file="/WEB-INF/views/include/message.jsp" %>
</head>
<%
    MemberVo member = (MemberVo) request.getAttribute("member");
    if (member == null) {
        member = new MemberVo();
    }
    MemberInfo memberInfo = member.getMemberInfo();
    if (memberInfo == null) {
        memberInfo = new MemberInfo();
    }
    MemberLogin memberLogin = member.getMemberLogin();
    if (memberLogin == null) {
        memberLogin = new MemberLogin();
    }

    List<ThirdLevelAddress> addrList = (List<ThirdLevelAddress>) request.getAttribute("addrParent");
    List<ThirdLevelAddress> cityList = (List<ThirdLevelAddress>) request.getAttribute("citys");
    List<ThirdLevelAddress> countyList = (List<ThirdLevelAddress>) request.getAttribute("countys");
%>
<body>
<div class="top container">
    <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
        <h1>个人信息</h1>
        <sys:headInfo/>
    </div>
</div>
<form class="form-horizontal login login-con" role="form" id="setForm">
    <div id="outer" class="container">
        <div class="wl-xy">
            <ul class="order-info4 order-fr">
                <li>
                    <label><span style="color:red">*</span>用户帐号：</label>
                    <div class="right-td">
                        <input type="hidden" name="memberId" value="${memberId}"/>
                        <input type="text" class="form-control form-fr" disabled="disabled" name="memberLogingName"
                               value="<%=UtilString.defaultString(memberLogin.getMemberLogingName())%>"/>
                    </div>
                </li>
                <li>
                    <label><span style="color:red">*</span>用户昵称：</label>
                    <div class="right-td">
                        <input type="text" class="form-control form-fr" maxlength="20" name="memberNickname"
                               value="<%=UtilString.defaultString(memberInfo.getMemberNickname())%>"/>
                    </div>
                </li>
                <li>
                    <label><span style="color:red">*</span>性别：</label>
                    <div class="right-td">
                        <label class="order-xb"><input type="radio" class="radio-icon" id="memberSex" name="memberSex"
                                                       value="boy"
                                                       <%if(memberInfo.getMemberSex()==null||"boy".equals(memberInfo.getMemberSex())){%>checked<%}%>/>男</label>
                        <label class="order-xb"><input type="radio" class="radio-icon" id="memberSex" name="memberSex"
                                                       value="girl"
                                                       <%if("girl".equals(memberInfo.getMemberSex())){%>checked<%}%> />女</label>
                    </div>
                </li>
                <li>
                    <label><span style="color:red">*</span>邮箱：</label>
                    <div class="right-td">
                        <input type="text" class="form-control form-fr" maxlength="20" id="memberEmail"
                               value="<%=UtilString.defaultString(memberLogin.getMemberEmail())%>"/>
                        <input type="hidden" class="form-control form-fr" maxlength="20" id="memberEmailHidden"
                              name="memberEmail" value="<%=UtilString.defaultString(memberLogin.getMemberEmail())%>"/>
                    </div>
                </li>
                <li>
                    <label><span style="color:red">*</span>真实姓名：</label>
                    <div class="right-td">
                        <input type="text" class="form-control form-fr" maxlength="20" id="memberRealname"
                               value="<%=UtilString.defaultString(memberInfo.getMemberRealname())%>"/>
                        <input type="hidden" class="form-control form-fr" maxlength="20" id="memberRealnameHidden"
                               name="memberRealname"
                               value="<%=UtilString.defaultString(memberInfo.getMemberRealname())%>"/>
                    </div>
                </li>
                <li>
                    <label><span style="color:red">*</span>联系电话：</label>
                    <div class="right-td">
                        <%if (memberLogin.getMemberPhone() != null) {%>
                        <div class="disbaled-box" id="memberPhoneBox">${blurryPhone }
                        </div>
                        <span id="memberPhonespan">${blurryPhone }</span>
                        <input type="hidden" class="form-control form-fr form-yx" id="memberPhone" name="memberPhone"
                               value=""/>
                        <input type="button" id="addMobileBtn" class="yx-btn" data-toggle="modal" data-target="#myModal"
                               value="修改手机绑定" style="float:right"/>
                        <%} else {%>
                        <input type="button" id="addMobileBtn" class="bd-btn" data-toggle="modal" data-target="#myModal"
                               value="绑定手机" style="float:right"/>
                        <%}%>
                    </div>
                </li>
                <li>
                    <label><span style="color:red">*</span>联系地址：</label>
                    <div class="right-td"> <span class="td-fr"> <i class="select-arrow"></i>
      <select id="memberProvince" name="memberProvince" class="set-ar01"
              data-default_value="<%=memberInfo.getMemberProvince()==null?"":memberInfo.getMemberProvince()%>">
            <option value="">请选择</option>
                <%
                    if (addrList != null) {
                        ThirdLevelAddress cate0 = null;
                        for (int i = 0, count = addrList.size(); i < count; ++i) {
                            cate0 = addrList.get(i);
                %>
                <option value="<%=cate0.getOrgId()%>"><%=HtmlUtils.defaultString(cate0.getOrgName())%></option>
                <%
                        }
                    }
                %>
                </select>
                 </span> <span class="td-fr"> <i class="select-arrow"></i>
                <select id="memberCity" name="memberCity" class="set-ar01"
                        data-default_value="<%=memberInfo.getMemberCity()==null?"":memberInfo.getMemberCity()%>">
                <option value="">请选择</option>
                <%
                    if (cityList != null) {
                        ThirdLevelAddress cate0 = null;
                        for (int i = 0, count = cityList.size(); i < count; ++i) {
                            cate0 = cityList.get(i);
                %>
                <option value="<%=cate0.getOrgId()%>"><%=HtmlUtils.defaultString(cate0.getOrgName())%></option>
                <%
                        }
                    }
                %>
                </select>
                 </span> <span class="td-fr"> <i class="select-arrow"></i>
                <select id="memberCounty" name="memberCounty" class="set-ar01"
                        data-default_value="<%=memberInfo.getMemberCounty()==null?"":memberInfo.getMemberCounty()%>">
                <option value="">请选择</option>
                <%
                    if (countyList != null) {
                        ThirdLevelAddress cate0 = null;
                        for (int i = 0, count = countyList.size(); i < count; ++i) {
                            cate0 = countyList.get(i);
                %>
                <option value="<%=cate0.getOrgId()%>"><%=HtmlUtils.defaultString(cate0.getOrgName())%></option>
                <%
                        }
                    }
                %>
                </select>
                   </span></div>
                    <div class="td-ir">
                        <input type="text" class="form-control form-fr" placeholder="详细地址" id="memberAddress"
                               value="<%=UtilString.defaultString(memberInfo.getMemberAddress())%>"/>
                        <input type="hidden" class="form-control form-fr" placeholder="详细地址" id="memberAddressHidden"
                               name="memberAddress"
                               value="<%=UtilString.defaultString(memberInfo.getMemberAddress())%>"/>
                    </div>
                </li>
                <li>
                    <label><span style="color:red">*</span>QQ：</label>
                    <div class="right-td">
                        <input type="text" class="form-control form-fr" id="memberQq"
                               value="<%=memberInfo.getMemberQq()==null?"":memberInfo.getMemberQq().toString().replace(memberInfo.getMemberQq().toString().substring(2,5),"****")%>"/>
                        <input type="hidden" class="form-control form-fr" id="memberQqHidden" name="memberQq"/>
                    </div>
                </li>
            </ul>
        </div>
        <div class="list-menu center-input"><a href="<%=request.getContextPath()%>/memberInfo/toUpdatePass"
                                               class="list-menu-item list-line">修改登录密码<span class="jt-icon"></span></a>
        </div>
        <%--<div class="list-menu center-input"><a href="<%=request.getContextPath()%>/memberSecurity/saveMemberSecurity"
                                               class="list-menu-item list-line">设置会员密保<span class="jt-icon"></span></a>
        </div>--%>

    </div>
    <div class="fix-br">
        <div class="affix foot-menu">
            <div class="container tj-btn">
                <a id="submitBtn" class="btn btn-blue btn-block center-btn"> 保存</a>
            </div>
        </div>
    </div>

    <!--绑定手机弹出框 start-->
    <div class="modal fade modal-prompt" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-fr">
            <div class="bl-tk">
                <div class="bl-list bl-fr">
                    <p><input type="text" id="phone" name="phone" class="form-control form2x"
                              placeholder="请输入手机号"/><input id="getMessage" type="button" value="获取验证码" class="tk-btn"/>
                    </p>
                    <p><span style="color:red" id="mobileSpan"></span></p>
                    <p><input type="text" id="smscaptcha" name="smscaptcha" class="form-control"
                              placeholder="请输入短信验证码"/></p>
                </div>
                <div class="tj-btn tj-box"><a id="confirmBtn" class="btn btn-blue">确定</a><a class="btn btn-blue"
                                                                                            data-dismiss="modal">取消</a>
                </div>
            </div>
        </div>
    </div>
    <!--绑定手机弹出框 end-->
    <!--邮箱修改弹出框 start-->
    <div class="modal fade modal-prompt" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-fr">
            <div class="bl-tk">
                <div class="bl-list bl-fr">
                    <p><input type="text" value="请输入有效的邮箱" class="form-control"/></p>`
                </div>
                <div class="tj-btn tj-box"><a class="btn btn-blue" data-dismiss="modal">确定</a></div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    var sendMsgUrl = "<%=request.getContextPath()%>/memberInfo/sendSms";
    var checkMsgUrl = "<%=request.getContextPath()%>/memberInfo/checkSms";
    var subUrl = "<%=request.getContextPath()%>/memberInfo/setMemberInfo";
    var changeUrl = "<%=request.getContextPath()%>/memberAddress/getChildrenByPid";
</script>
</body>
</html>
