<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!--规则效果预留-->
<div  class="bargin-popup popup-rule" id="tip_1">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-title">活动规则</div>
    <div class="bargin-popup-content">
        <p class="bargin-rule-txt">
            1）本活动仅限湖南移动客户参与；<br/>
            2）活动发起客户必须绑定银行卡，且每位用户仅限购买一台砍价终端；<br/>
            3）每位用户只能发起一次砍价终端活动，每位砍价人员只能帮一位好友砍价一次；<br/>
            4）好友帮砍需输入客户湖南移动手机号码，获取短信验证码并正确填入方可成功砍价；<br/>
            5）购买订单可登陆和包“湖南移动厅-购机-我的订单”查询。
        </p>
    </div>
</div>

<!--电话确认预留-->
<div  class="bargin-popup bargin-popup-sign" id="tip_2">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png"/></a>
    <div class="bargin-popup-content">
        <div class="mt10">
            <label for="phoneNum">手机号:</label>
            <input type="number" id="phoneNum" placeholder="请输入湖南移动号码"/>
        </div>
        <div class="mt10">
            <label for="phoneNum">验证码:</label>
            <input type="number" id="smsCode"  class="w1" />
            <a  class="bargin-ver-btn" href="javascript:void(0);" id="getSmsCtrl">点击获取</a>
        </div>
        <div class="mt10 bargin-popup-sign-info" id="errorMsgContainer">砍价活动限湖南移动客户!</div>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" id="confirmHelpCutCtrl">确认</a> </div>
    </div>
</div>


<!--帮砍页面预留-->
<div  class="bargin-popup bargin-popup-help" id="tip_3">
    <a class="popup-close" onclick="reloadUrl();" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png"/></a>
    <div class="bargin-popup-content">
        <div class="bargin-txt"><p id="helpCutAmount">0</p></div>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" onclick="reloadUrl();">确认</a></div>
    </div>
</div>

<!--帮砍页面预留-->
<div  class="bargin-popup bargin-popup-help" id="tip_4">
    <a class="popup-close" onclick="reloadUrl();" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png"/></a>
    <div class="bargin-popup-content">
        <div class="bargin-txt03">将确认您的绑卡状态，请重新进入活动页面参与活动吧！</div>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" onclick="reloadUrl();">确认</a></div>
    </div>
</div>


<!--帮砍页面-已帮卡预留-->
<div  class="bargin-popup bargin-popup-helpover" id="error_4">
    <a class="popup-close" href="javascript:;"><img    src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <div class="bargin-txt02"><img src="${ctxStatic}/cut/images/bargain/txt02.png" /></div>
        <div class="mt10 tc"><a class="bargin03-btn popup-close-btn" href="javascript:void(0);">确认</a> </div>
    </div>
</div>

<!--帮砍页面-无需帮卡预留-->
<div  class="bargin-popup bargin-popup-nohelp" id="error_3">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <div class="bargin-txt03"><img src="${ctxStatic}/cut/images/bargain/txt03.png" /></div>
        <div class="mt10 tc"><a class="bargin03-btn popup-close-btn" href="javascript:void(0);">确认</a> </div>
    </div>
</div>

<!--帮砍页面-需帮卡预留-->
<div  class="bargin-popup bargin-popup-bindcard" id="error_2">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png"/></a>
    <div class="bargin-popup-content">
        <div class="bargin-txt04"><img src="${ctxStatic}/cut/images/bargain/txt04.png" /></div>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" onclick="hideErrorDialog('2');showTipDialog('4');goToBindBank();">立即绑卡</a></div>
    </div>
</div>

<!--活动结束预留-->
<div  class="bargin-popup bargin-popup-over" id="error_1">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>本期砍价活动未开始或已结束~<br/>更多精彩活动请移步至和包首页。</p>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" onclick="toHeBaoIndex();">立即前往</a> </div>
    </div>
</div>

<!--活动结束预留-->
<div  class="bargin-popup bargin-popup-over" id="error_5">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>你已经发起过砍价活动~</p>
        <div class="mt10 tc"><a class="bargin03-btn" href="${ctx}/bargainInHeBao/initMyCut?bargainCode=${activityGoodsPara.actCode}">查看我的砍价活动</a> </div>
    </div>
</div>

<!--活动结束预留-->
<div  class="bargin-popup bargin-popup-over" id="error_6">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>系统繁忙~<br/>请稍后重试。</p>
        <div class="mt10 tc"><a class="bargin03-btn popup-close-btn" href="javascript:void(0);">确认</a> </div>
    </div>
</div>

<!--活动结束预留-->
<div  class="bargin-popup bargin-popup-over" id="error_7">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>本期砍价活动已结束~<br/>更多精彩活动请移步至和包首页。</p>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" onclick="toHeBaoIndex();">确认</a> </div>
    </div>
</div>

<!--活动结束预留-->
<div  class="bargin-popup bargin-popup-over" id="error_8">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>本期砍价活动已到达上限~<br/>更多精彩活动请移步至和包首页。</p>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" onclick="toHeBaoIndex();">立即前往</a> </div>
    </div>
</div>

<!--活动结束预留-->
<div  class="bargin-popup bargin-popup-over" id="error_9">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>自己发起过的砍价活动， 不能自己砍价~</p>
        <div class="mt10 tc"><a class="bargin03-btn popup-close-btn" href="javascript:void(0);">确认</a> </div>
    </div>
</div>

<div  class="bargin-popup bargin-popup-over" id="error_10">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>你只能帮一位好友砍价一次~</p>
        <div class="mt10 tc"><a class="bargin03-btn popup-close-btn" href="javascript:void(0);">确认</a> </div>
    </div>
</div>

<div  class="bargin-popup bargin-popup-over" id="error_11">
    <a class="popup-close" href="javascript:void(0);"><img src="${ctxStatic}/cut/images/bargain/close.png" /></a>
    <div class="bargin-popup-content">
        <p>亲爱的用户你好，<br>参与本次活动请先分享至朋友圈哦！~</p>
        <div class="mt10 tc"><a class="bargin03-btn" href="javascript:void(0);" id="shareCtrl">分享</a> </div>
    </div>
</div>