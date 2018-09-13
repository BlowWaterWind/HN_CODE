<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
<link href="css/swiper.min.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link href="css/wt-sub.css" rel="stylesheet" type="text/css" />
<link href="css/oil.css" rel="stylesheet" type="text/css">
<link href="css/kd.css" rel="stylesheet" type="text/css" />
<link href="css/media-style.css" rel="stylesheet" type="text/css" />
<title>湖南移动网上营业厅</title>
</head>

<body>
<div class="top container">
  <div class="header sub-title"><a class="icon-left" href="javascript:void(0);" onclick="window.history.back()"></a>
    <h1>密码</h1>
  </div>
</div>
<div class="container bg-gray hy-tab">
   <div class="Tab">
    <div id="box">
      <ul class="TabHead nav-tabs kd-tabs" id="topfloat">
        <li class="on"><a href="javascript:;">修改密码</a></li>
        <li style="border:none;"><a href="javascript:;">密码重置</a></li>
      </ul>
    </div>
    <div class="TabNote">
      <div class="block tab-cz" style="display:block;"> 
       <div class="xg-pssword mt10">
    <ul class="password-con">
      <li>
        <label>宽带账号：</label>
        <div class="right-td">
          <span class="td-fr"><i class="select-arrow"></i>
          <select name="city" class="form-control f-c-xg">
            <option value="湖南省">湖南省</option>
          </select>
          </span>
        </div>
      </li>
      <li>
        <label>&emsp;证件号：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="请输入您的身份证号" />
        </div>
      </li>
      <li>
        <label>&emsp;原密码：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="请输入当前密码" />
         <span class="cw-ts font-red">两次新密码不一致，请重新输入</span> </div>
      </li>
      <li>
        <label>&emsp;新密码：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="请设置8位以上密码" />
         <span class="cw-ts font-red">两次新密码不一致，请重新输入</span> </div>
      </li>
      <li>
        <label>确认密码：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="请再次输入新密码">
        </div>
      </li>
      <!--提交按钮 start-->
      <div class="form-group btn-box"> <a class="btn btn-blue btn-block" href="#">确  定</a> </div>
      <!--提交按钮 end-->
    </ul>
  </div>
      </div>
      <div class="none" style="display: none;"> 
       <div class="xg-pssword mt10">
    <ul class="password-con">
      <li>
        <label>宽带账号：</label>
        <div class="right-td">
          <span class="td-fr"><i class="select-arrow"></i>
          <select name="city" class="form-control f-c-xg">
            <option value="湖南省">湖南省</option>
          </select>
          </span>
        </div>
      </li>
      <li>
        <label>&emsp;手机号：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="13699880000" />
        </div>
      </li>
      <li>
        <label>&emsp;验证码：</label>
        <div class="right-td">
          <input type="text" class="form-control form-fr form-sd" placeholder="请输入手机号码">
          <input type="button" value="发送验证码" class="td-btn" id="getCode" onclick="getVerificationCode(1);">
        </div>
      </li>
      <li>
        <label>&emsp;证件号：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="********" />
        </div>
      </li>
      <li>
        <label>新密码：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="请再次输入新密码">
        </div>
      </li>
      <li>
        <label>确认密码：</label>
        <div class="right-td">
          <input type="password" class="form-control form-fr" placeholder="请再次输入新密码">
        </div>
      </li>
      <!--提交按钮 start-->
      <div class="form-group btn-box"> <a class="btn btn-blue btn-block" href="#">确  定</a> </div>
      <!--提交按钮 end-->
    </ul>
  </div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/nav.js"></script> 
<script type="text/javascript" src="js/public.js"></script>
<script type="text/javascript"  src="js/tab.js"></script>
<script type="text/javascript" src="js/modal.js"></script>
</body>
</html>
