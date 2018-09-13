<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<jsp:useBean id="dateValue" class="java.util.Date"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <!--
     * the head of wap
     *
     * @author: yunzhi li
     * @version: 2016/10/27 10:56
     *           Id
    -->
    <title>一元秒杀</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport"/>
    <!-- css -->
    <!-- css eg -->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/kill/lib/normalize-6.0.0.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/kill/css/wap.css"/>

    <!-- JS -->
    <!-- JS eg -->
    <script type="text/javascript" src="${ctxStatic}/kill/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${ctxStatic}/kill/lib/flexible/flexible.js"></script>
    <script type="text/javascript" src="${ctxStatic}/kill/js/comm.js"></script>
    <script type="text/javascript" src="${ctxStatic}/kill/js/kill.js?version=1"></script>
    <script type="text/javascript" src="${ctxStatic}/js/hebao.js?verson=6"></script>
    <script type="text/javascript">
        var ctxStatic = "${ctxStatic}";
        var ctx = "${ctx}";
        var activityId = '${visualActivityInfo.activityId}';
        var isShare = '${isShare}';
        var isBindCard = "${isBindCard}";
        var myvar = null;
        var demo = null;
        var demo1 = null;
        var demo2 = null;
        function Marquee() {
            if (demo.scrollLeft - demo2.offsetWidth >= 0) {
                demo.scrollLeft -= demo1.offsetWidth;
            } else {
                demo.scrollLeft++;
            }
        }

        $(function(){
            startKillTime(${systemTime}, ${visualActivityInfo.startDate}, ${visualActivityInfo.endDate});
            <c:if test="${fn:length(lFormatUsers) > 0}">
                //文字滚动
                demo = document.getElementById("demo");
                demo1 = document.getElementById("demo1");
                demo2 = document.getElementById("demo2");
                demo2.innerHTML = demo1.innerHTML;
                myvar = setInterval(Marquee, 30);
                demo.onmouseout = function() {
                    myvar = setInterval(Marquee, 30);
                }
                demo.onmouseover = function() {
                    clearInterval(myvar);
                }
            </c:if>
        });
    </script>

</head>

<body>
    <div class="container">
        <img src="${tfsUrl}/${visualActivityInfo.activityUrl}" />
        <a href="javascript:void(0)" class="gz-btn" onclick="toggleModal('tk_modal')">活动规则</a>
        <div class="con">
            <div class="con-title">绑定支付分享至朋友圈即可参与</div>
            <div class="con-veiw">
                <div class="con-list clearfix">
                    <dl>
                        <dt><img src="${tfsUrl}${visualActivityInfo.goodsUrl}" /></dt>
                        <dd>
                            <h4 title="${visualActivityInfo.goodsName}">
                                ${visualActivityInfo.goodsName}
                            </h4>
                            <c:if test="${activityAttrs != null && fn:length(activityAttrs) > 0}">
                                <c:forEach var="item" items="${activityAttrs}">
                                    <span>${item}</span>
                                </c:forEach>
                            </c:if>
                            <p class="font-red">价值:<strong class="f40"><fmt:formatNumber pattern="#0.00" value="${visualActivityInfo.goodsSkuPrice/100}"></fmt:formatNumber></strong>元</p>
                        </dd>
                    </dl>
                    <div class="scroll-con" id="demo">
                        <div class="qimo">
                            <div id="demo1">
                                <ul>
                                    <c:if test="${fn:length(lFormatUsers) > 0}">
                                        <c:forEach var="item" items="${lFormatUsers}">
                                            <li>${item}用户成功秒杀到${visualActivityInfo.goodsName}</li>
                                        </c:forEach>
                                    </c:if>
                                </ul>
                            </div>
                            <div id="demo2"></div>
                        </div>
                    </div>
                    <div class="ms-con">
                        <p class="ms-title">每周四更新秒杀机型，敬请期待... ...</p>
                        <p class="f50 t-center"><span class="font-red">
                            <jsp:setProperty name="dateValue" property="time" value="${visualActivityInfo.startDate}"/>
                            <c:choose>
                                <c:when test="${inToday}">
                                    <fmt:formatDate value="${dateValue}" pattern="HH:mm"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatDate value="${dateValue}"  pattern="MM-dd HH:mm"/>
                                </c:otherwise>
                            </c:choose>
                        </span>点整开抢</p>
                        <p class="f24 pt15 t-center" id="timerContainer">加载中...</p>
                    </div>
                    <div class="ms-btn">
                        <a href="javascript:void(0)" class="btn btn-zs" id="killShareCtrl">分享</a>
                        <a href="javascript:void(0)" class="btn" activityId="${visualActivityInfo.activityId}" id="killStartCtrl">即将开始</a>
                    </div>
                </div>
            </div>
            <div class="con-tabs">
                <table id="table">
                    <tr>
                        <th>秒杀时间</th>
                        <th>机型</th>
                        <th>秒杀价</th>
                        <th>数量</th>
                    </tr>

                    <c:forEach items="${visualActivityList}" var="item">
                        <tr>
                            <jsp:setProperty name="dateValue" property="time" value="${item.startDate}"/>
                            <td><fmt:formatDate value="${dateValue}"  pattern="MM月dd日 HH:mm"/></td>
                            <td><span title="${item.goodsName}">${item.goodsName}</span></td>
                            <td><fmt:formatNumber pattern="#0.00" value="${item.marketPrice/100}"></fmt:formatNumber>元</td>
                            <td>${item.activityStock}台</td>
                        </tr>
                    </c:forEach>

                </table>
            </div>
        </div>
        <div class="silder-con"><a href="http://touch.10086.cn/hd/skin/268632/731_731.html"><img src="${ctxStatic}/kill/images/killbottom.jpg" /></a></div>
    </div>
    <!--弹框 start-->
    <!--活动规则弹框 start-->
<div id="tk_modal" class="mask-layer">
    <div class="modal">
        <a href="javascript:void(0)" class="btn-close" onclick="toggleModal('tk_modal')"></a>
        <div class="modal_content">
            <h4>活动规则</h4>
            <div class="modal-con">
                <p>1.本活动仅限湖南移动客户参与；</p>
                <p>2.终端秒杀需要求客户在网1年以上，即2016年4月1日前入网；</p>
                <p>3.成功秒杀的客户必须在30分钟之内完成付款，否则秒杀机会将失效；</p>
                <p>4.用户参与秒杀必须通过和包绑卡才能参与秒杀；</p>
                <p>5.用户参与秒杀必须分享本次活动至朋友圈才能参与秒杀；</p>
                <%--<p>6.用户成功秒杀并支付后，晒单可获赠50M省内流量；</p>--%>
                <p>6.购买订单可登陆和包“湖南移动厅-购机-我的订单”查询。</p>
            </div>
        </div>
    </div>
</div>
<!--活动规则弹框 end-->

    <!--温馨提示一弹框 start-->
    <div id="tk_modal1" class="mask-layer">
        <div class="modal">
            <a href="javascript:void(0)" class="btn-close" onclick="toggleModal('tk_modal1')"></a>
            <div class="modal_content">
                <h4>温馨提示</h4>
                <div class="modal-con">
                    <p>您访问的秒杀活动信息异常~更多精彩活动 请至活动专区。
                    </p>
                </div>
                <div class="tk-btn">
                    <a href="javascript:void(0)" class="btn btn-ls" onclick="toggleModal('tk_modal1')">取消</a>
                    <a href="javascript:void(0)" class="btn btn-red" onclick="toHeBaoIndex();">立即前往</a>
                </div>
            </div>
        </div>
    </div>

<!--温馨提示一弹框 start-->
    <div id="tk_modal4" class="mask-layer">
        <div class="modal">
            <a href="javascript:void(0)"></a>
            <div class="modal_content">
                <h4>温馨提示</h4>
                <div class="modal-con">
                    <p>您访问页面前面人山人海， 请稍后再试！！
                    </p>
                </div>
                <div class="tk-btn">
                    <a href="javascript:void(0)" class="btn btn-center btn-red" onclick="toggleModal('tk_modal4')">确认</a>
                </div>
            </div>
        </div>
    </div>
    <!--温馨提示一弹框 end-->


    <!--温馨提示二弹框 start-->
    <div id="tk_modal16" class="mask-layer">
        <div class="modal">
            <a href="javascript:void(0)" class="btn-close" onclick="reloadUrl();"></a>
            <div class="modal_content">
                <h4>温馨提示</h4>
                <div class="modal-con">
                    <p>将确认您的绑卡状态，请重新进入活动页面参与活动吧！</p>
                </div>
                <div class="tk-btn">
                    <a href="javascript:void(0)" class="btn btn-center btn-red" onclick="reloadUrl();">确认</a>
                </div>
            </div>
        </div>
    </div>

<!--温馨提示二弹框 start-->
<div id="tk_modal6" class="mask-layer">
    <div class="modal">
        <a href="javascript:void(0)" class="btn-close" onclick="toggleModal('tk_modal6')"></a>
        <div class="modal_content">
            <h4>温馨提示</h4>
            <div class="modal-con">
                <p>手慢啦~，宝贝被抢光啦~更多精彩活动 请至活动专区。</p>
            </div>
            <div class="tk-btn">
                <a href="javascript:void(0)" class="btn btn-ls" onclick="toggleModal('tk_modal6')">取消</a>
                <a href="javascript:void(0)" class="btn btn-red" onclick="toHeBaoIndex();">立即前往</a>
            </div>
        </div>
    </div>
</div>
<!--温馨提示二弹框 end-->

<!--温馨提示三弹框 start-->
<div id="tk_modal7" class="mask-layer">
    <div class="modal">
        <a href="javascript:void(0)" class="btn-close" onclick="toggleModal('tk_modal7')"></a>
        <div class="modal_content">
            <h4>温馨提示</h4>
            <div class="modal-con">
                <p>本次活动需分享后才能参与秒杀活动哦！</p>
            </div>
            <div class="tk-btn">
                <a href="javascript:void(0)" class="btn btn-ls" onclick="toHeBaoIndex();" id="shareCancelCtrl">取消</a>
                <a href="javascript:void(0)" class="btn btn-red" id="shareConfirmCtrl">立即分享</a>
            </div>
        </div>
    </div>
</div>
<!--温馨提示三弹框 end-->

<!--温馨提示四弹框 start-->
<div id="tk_modal8" class="mask-layer">
    <div class="modal">
        <a href="javascript:void(0)" class="btn-close" onclick="toggleModal('tk_modal8')"></a>
        <div class="modal_content">
            <h4>温馨提示</h4>
            <div class="modal-con">
                <p>参加本次活动需进行和包绑卡，请先绑卡 再来参与哦！</p>
            </div>
            <div class="tk-btn">
                <a href="javascript:void(0)" class="btn btn-ls" onclick="toHeBaoIndex();">取消</a>
                <a href="javascript:void(0)" class="btn btn-red" onclick="toggleModal('tk_modal8');toggleModal('tk_modal16');goToBindBank();">立即绑卡</a>
            </div>
        </div>
    </div>
</div>
<!--温馨提示四弹框 end-->

<!--温馨提示五弹框 start-->
<div id="tk_modal5" class="mask-layer">
    <div class="modal">
        <a href="javascript:void(0)" class="btn-close" onclick="toggleModal('tk_modal5')"></a>
        <div class="modal_content">
            <h4>温馨提示</h4>
            <div class="modal-con">
                <p>您在网年限不满1年哦，还不能参与本次活动。</p>
            </div>
            <div class="tk-btn">
                <a href="javascript:void(0)" onclick="toHeBaoIndex();" class="btn btn-center btn-red">确定</a>
            </div>
        </div>
    </div>
</div>
<!--温馨提示五弹框 end-->

    <!--温馨提示一弹框 start-->
    <div id="tk_modal9" class="mask-layer">
        <div class="modal">
            <a href="javascript:void(0)"></a>
            <div class="modal_content">
                <h4>温馨提示</h4>
                <div class="modal-con">
                    <p>不能贪心哦，您已参加过本次秒杀活动啦，更多精彩活动请至活动专区。
                    </p>
                </div>
                <div class="tk-btn">
                    <a href="javascript:void(0)" class="btn btn-ls" onclick="toggleModal('tk_modal9')">取消</a>
                    <a href="javascript:void(0)" class="btn btn-red" onclick="toHeBaoIndex();">确认</a>
                </div>
            </div>
        </div>
    </div>
    <!--温馨提示一弹框 end-->

</body>

</html>