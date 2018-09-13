<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--已废弃不用--%>
<html>
<head>
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>在线售卡-首页</title>
    <link href="${ctxStatic}/css/sim/wap-simh5online.css" rel="stylesheet">
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery-2.2.4.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/jquery.tabslet.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ap/lib/flexible/flexible.js"></script>
</head>
<body>
<!-- start container -->
<div class="container bg-01">
    <div class="header"></div>
    <div class="taocan">
        <div class="taocan-top">
            <h3>${conf.planDesc}</h3>
        </div>
        <div class="taocan-body">
            <ul class="tcard-list">
                <%--tcard-gray：表示灰色背景，tcard-blue表示蓝色背景--%>
                <c:forEach items="${plans}" var="ps">
                    <c:set var="info" value="${fn:split(ps.planName,\"套餐\")}"/>
                    <li class="tcard-gray">
                        <h3>${info[0]}套餐</h3>
                        <p>${info[1]}</p>
                        <input type="hidden" name="planId" value="${ps.planId}">
                        <input type="hidden" name="planName" value="${ps.planName}">
                    </li>
                </c:forEach>
            </ul>
            <div class="btn-wrap">
                <button class="btn btn-blue _goSubmit">立即申请</button>
            </div>
        </div>
        <div class="taocan-foot">
            <a href="#zfxq">更多资费详情</a>
        </div>
        <form id="applyForm" action="simH5OnlineToConfirmOrder" method="post">
            <input type="hidden" name="confId" value="${conf.confId}"/>
            <input type="hidden" name="planId"/>
            <input type="hidden" name="planName"/>
        </form>
    </div>

    <%-- 套餐内容介绍 --%>
    <div class="pd-intr">
        <div class="ribbon">
            <h3>产品介绍</h3>
        </div>
        <div class="intr">
            <div class="intr-img intr-img-01">
                <img src="${ctxStatic}/images/goods/sim/h5online/img-01.png">
            </div>
            <div class="intr-text">
                <h1>国内流量不限量</h1>
                <p>随身移动WiFi,随时高效办公
                    <br>微信、QQ、视频、音乐，尽情嗨爽。</p>
            </div>
        </div>
        <div class="intr">
            <div class="intr-text intr-text-02">
                <h1>每月加送1G国内流量</h1>
                <p>连续12个月每月加送1G国内流量
                    <br>商务出差、驴友人士必备。</p>
            </div>
            <div class="intr-img intr-img-02">
                <img src="${ctxStatic}/images/goods/sim/h5online/img-02.png">
            </div>
        </div>
        <div class="intr">
            <div class="intr-img intr-img-03">
                <img src="${ctxStatic}/images/goods/sim/h5online/img-03.png">
            </div>
            <div class="intr-text">
                <h1>存50得110</h1>
                <p>首次充值存50元得60元自由话费
                    <br>（赠送的10元话费72小时内到账）；
                    <br>下载和包支付客户端充值再送50元话费券。</p>
            </div>
        </div>
    </div>
    <div class="zf-info">
        <p id="zfxq"/>
        <div class="ribbon">
            <h3>资费详情</h3>
        </div>
        <table class="blue-table">
            <thead>
            <tr>
                <th rowspan="2">不限量卡</th>
                <th colspan="3">流量</th>
                <th colspan="4">语音</th>
                <th colspan="3">其他</th>
            </tr>
            <tr>
                <th>国内流量</th>
                <th>套外国内流量</th>
                <th>国内主叫
                    <br>(分钟)
                </th>
                <th>来电显示</th>
                <th>国内被叫</th>
                <th>套外国内主叫</th>
                <th>宽带</th>
                <th>添加副卡</th>
                <th>+1元享固话(分钟)</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>98元</td>
                <td>500M</td>
                <td>0.29元/M</td>
                <td>100+400
                    <br>(赠送1年)
                </td>
                <td>免费</td>
                <td>免费</td>
                <td>0.19元/分钟</td>
                <td>100M</td>
                <td>1张</td>
                <td>2000</td>
            </tr>
            <tr>
                <td>198元</td>
                <td>20G</td>
                <td>不限量</td>
                <td>0.29元/M</td>
                <td>1500</td>
                <td>免费</td>
                <td>免费</td>
                <td>0.19元/分钟</td>
                <td>100M</td>
                <td>2张</td>
                <td>2000</td>
            </tr>
            </tbody>
        </table>
        <p class="fz-20">温馨提示：您办理套餐后即可获得宽带、副卡、固话的优惠权益，请前往就近营业厅开通。</p>
        <div class="zf-info-text">
            <div class="zf-title">
                <h3>98元档位套餐</h3>
            </div>
            <p>包含100分钟国内主叫，赠送400分钟通话时长，优惠期12个月，国内被叫免费，国内主叫语音超出套餐后为0.19元/分钟；包含500M全国流量，流量当月有效，未使用完的流量不可结转到次月使用，客户全月总通用流量达到50G后网速将降至不高于1Mbps，当月全部流量使用达到100GB后，停止上网功能，次月自动恢复。</p>
            <div class="zf-title">
                <h3>198元档位套餐</h3>
            </div>
            <p>包含1500分钟国内主叫，国内被叫免费，国内主叫语音超出套餐后为0.19元/分钟；包含20G全国流量，流量当月有效，未使用完的流量不可结转到次月使用，客户全月总通用流量达到50G后网速将降至不高于1Mbps，当月全部流量使用达到100GB后，停止上网功能，次月自动恢复。</p>
        </div>
    </div>
</div>
<!-- end container -->
<!-- start modal -->
<div id="orders-info" class="mask-layer">
    <div class="modal small-modal">
        <a href="javascript:void(0)" class="modal-close" onclick="toggleModal('orders-info')"></a>
        <div class="modal-content">
            <div class="modal-con">
                <div class="pt-50"></div>
                <p class="text-center">您当前没有订单信息，
                    <br>请点击确定前往申请号卡吧！</p>
            </div>
            <div class="modal-btn-wrap">
                <a href="javascript:void(0)" class="modal-btn">确定</a>
            </div>
        </div>
    </div>
</div>
<!-- end modal -->
</body>
<script type="text/javascript">
    $(function () {
       //默认对第一个为选中
        var firstLi = ".tcard-list li:first ";
        $(firstLi).attr("class","tcard-blue");
        $("#applyForm input[name='planId']").val($(firstLi+"input[name='planId']").val());
        $("#applyForm input[name='planName']").val($(firstLi+"input[name='planName']").val());
        //绑定点击事件
        $(".tcard-list li").on("click",function () {
            $(".tcard-list li").attr("class","tcard-gray");
            $(this).attr("class","tcard-blue");
            $("#applyForm input[name='planId']").val($(this).find('input[name="planId"]').val());
            $("#applyForm input[name='planName']").val($(this).find('input[name="planName"]').val());
        });
        //提交
        $('._goSubmit').on("click",function() {
           $("#applyForm").submit();
        });
    });
</script>
</html>