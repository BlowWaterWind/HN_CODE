<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link href="${ctxStatic}/css/o2oClient/userbroadcast.css" rel="stylesheet" type="text/css">
    <link href="${ctxStatic}/css/list.css" rel="stylesheet" type="text/css" />
    <link href="${ctxStatic}/css/swiper.min.css" rel="stylesheet" type="text/css" />

    <title>宽带预约</title>
    <script type="text/javascript">

    </script>

</head>

<body>
<div id="bradcastMore" >
    <div class="top container">
        <div class="header"><a class="icon-main-back" href="javascript:void(0);" onclick="window.history.back()"></a>
            <h1>宽带预约</h1>
        </div>
    </div>
    <div class="container ">
        <div class="swiper-container  broadcast-swiper " id="swiper">
            <div class="swiper-wrapper">
                <c:forEach items="${posterList}" var="poster">
                <div class="swiper-slide"><a href="${poster.redirectUrl}"><img src="${poster.picUrl}" /></a></div>
                </c:forEach>
            </div>
            <div class="pagination"></div>
        </div>
    </div>
    <div class=" container bg-white mt10 p10 ">
        <div class="clearfix border-bottom pb10 mt10">
            <div class="pull-left w90"><a class="dis-block" href="#"><img src="${tfsUrl}/${broadbandItem.imgUrl}"/></a> </div>
            <div class="ml100 "  >
                <p class="fs16 lh30">${broadbandItem.broadbandItemName}</p>
                <p class="line-txt"><span>${fns:abbr(broadbandItem.desc,40)}</span></p>
                <p class="font-blue  fs14 lh30">${broadbandItem.price}元/${broadbandItem.term}</p>
            </div>

        </div>
    </div>
    <div class=" container bg-white mt10 ">
        <div class="p10 border-bottom">

            <h3 class="fs16"><i class="index-icon-hot-broadcast  index-icon-hot-broadcast-green"></i>宽带介绍</h3>

        </div>
        <p class="p10 font-9">${broadbandItem.desc}</p>

    </div>
    <div class="container mt10 ">
        <div class="p10 border-bottom bg-white">
            <h3 class="fs16"><i class="index-icon-hot-broadcast"></i>宽带信息</h3>
        </div>
        <div class=" bg-white fs12">
            <form>
                <div class="clearfix">
                    <label class="w60 pull-left text-right lh30 fs12">姓名</label>
                    <div class="ml70 border-bottom lh30 fs12"><input class="w-100per fs12" type="text" placeholder="请输入真实姓名" /></div>
                </div>
                <div class="clearfix">
                    <label class="w60 pull-left text-right lh30 fs12">联系电话</label>
                    <div class="ml70 border-bottom lh30"><input class="w-100per fs12" type="text" placeholder="请输入正确的手机号（必填项）" value="${installPhoneNum}" /></div>
                </div>
                <div class="clearfix">
                    <label class="w60 pull-left text-right lh30 fs12">装机地址</label>
                    <div class="ml70 border-bottom lh30 clearfix">
                        <span class="pull-left fs12">
                            <select class="select-form pull-left ml5 fs12"  id="city" name="city" >
                                <c:forEach items="${cityList}" var="city" varStatus="status">
                                    <c:if test="${fn:contains(city.eparchyCode,eparchy_Code)}">
                                        <option value="${city.eparchyCode}"  orgid="${city.orgId}">${city.orgName}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </span>
                        <select class="select-form pull-left ml5 fs12"  id="county" name="county">
                            <c:forEach items="${countyList}" var="county" varStatus="status">
                                <option value="${county.orgId}"  >${county.orgName}</option>
                            </c:forEach>
                        </select>
                        <div class="pull-left ml5 w90 "><input class="fs12" placeholder="输入小区具体地址" type="text" /></div><span class="iconfont icon-search pull-right  ml10 mt4 " id="searchBtn0" ></span></div>
                </div>

            </form>

        </div>
        <div class="bg-eee mt10 mb10 p10"><a class="btn btn-blue btn-block btnGz"  href="javascript:;">立即预约</a></div>
    </div>
</div>
<div id="bradcastSearch"  style="display: none" >

    <div class="top container">
        <div class="header"><a class="icon-main-back" href="javascript:void(0);" id="returnMain"></a>
            <a href="javascript:void(0)" class="broadcast-searchbtn" id="searchBtn">搜索</a>
            <div class="broadcast-search  "> <a href="#"><span class="iconfont icon-search"></span></a>
                <a class="" href="#"><span class="iconfont icon-colse"></span></a>
                <input type="search" id="queryAddress" name="queryAddress" class="form-control" placeholder="输入具体的查询地址">
            </div>

        </div>
    </div>
    <div class="container p10 hide" id="div_message">

        <img class="no-search" src="${ctxStatic}/images/broadBand/o2oClient/noorder.png" />
        <p class="font-9 text-center mt30">未找到到相关内容</p>
    </div>
    <ul class="container">
        <ul class="adress-list">
        </ul>
    </ul>
</div>
<!-- 电话弹窗口 -->
<div class="modal modal-broadcast fade" id="telModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">

            <div class="modal-body ">
                <div class="gz-list">
                    <p class="text-center fs16 lh45">提示</p>
                    <p>联系电话未输入，请您先填写完毕联系电话，方便您后续的宽带办理！</p>

                </div>
            </div>
            <div class="modal-footer">
                <a  class="sigle font-blue" data-dismiss="modal">我知道了</a>

            </div>

        </div>
    </div>
</div>
<!-- 未完成预约弹出窗口 -->
<div class="modal modal-broadcast fade" id="NotreserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">

            <div class="modal-body ">
                <div class="gz-list">
                    <p class="text-center fs16 lh45">预约未完成</p>
                    <p>您的预约订单未完成，是否保存此预约订单</p>

                </div>
            </div>
            <div class="modal-footer">
                <a  href="#" data-dismiss="modal">取消</a>
                <a  class=" font-blue" >前往查看</a>

            </div>

        </div>
    </div>
</div>
<!-- 已预约弹出窗口 -->
<div class="modal modal-broadcast fade" id="reservaModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">

            <div class="modal-body ">
                <div class="gz-list">
                    <p class="text-center fs16 lh45">提示</p>
                    <p>您已预约过该宽带套餐，在订单中心可以查看该笔订单！</p>

                </div>
            </div>
            <div class="modal-footer">
                <a  href="#" data-dismiss="modal">取消</a>
                <a  class=" font-blue" >前往查看</a>

            </div>

        </div>
    </div>
</div>
<!-- 未包保弹出窗口 -->
<div class="modal modal-broadcast fade" id="NonguarantModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">

            <div class="modal-body ">
                <div class="gz-list">
                    <p class="text-center fs16 lh45">提示</p>
                    <p>为了方便你的业务办理，请先关注一个为你专属服务的店铺吧！</p>

                </div>
            </div>
            <div class="modal-footer">
                <a  href="#" data-dismiss="modal">跳过</a>
                <a  class=" font-blue" >前往选择</a>

            </div>

        </div>
    </div>
</div>
<!-- 未包保弹出窗口2 -->
<div class="modal modal-broadcast fade" id="NonguarantModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">

            <div class="modal-body ">
                <div class="gz-list">
                    <p class="text-center fs16 lh45">提示</p>
                    <p>为你服务的店铺暂未有办理该业务权限，请选择一个为你服务的店铺</p>

                </div>
            </div>
            <div class="modal-footer">
                <a data-dismiss="modal" href="#" >跳过</a>
                <a  class=" font-blue" >前往选择</a>

            </div>

        </div>
    </div>
</div>
<!-- 未包保弹出窗口2 -->
<div class="modal modal-broadcast fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">

            <div class="modal-body ">
                <div class="gz-list">
                    <p class="text-center fs16 lh45">预约成功</p>
                    <p>恭喜你成功预约该宽带套餐，系统正在为你派单中，请耐心等待~</p>

                </div>
            </div>
            <div class="modal-footer">
                <a  class="sigle font-blue" data-dismiss="modal">确认</a>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/o2o/addressIndex.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/swiper.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/modal.js"></script>
<script>//轮播JS
var mySwiper = new Swiper('#swiper',{
    pagination: '.pagination',
    loop:true,
    grabCursor: true,
    autoplay:3000,
    autoplayDisableOnInteraction:false,

})
$(function(){
    $("#searchBtn0").click(function(){
        $("#bradcastMore").hide();
        $("#bradcastSearch").show();
    });
    $("#returnMain").click(function(){
        $("#bradcastMore").show();
        $("#bradcastSearch").hide();
    });
    $(".btnGz").click(function(){ $('#myModal').modal('show'); });
})

</script>
</body>
</html>
