<%@ page import="org.apache.shiro.session.Session" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel" %>
<%@ page import="com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils" %>
<%@ page import="com.ai.ecs.merchant.shop.pojo.Shop" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.ai.ecs.order.entity.TfOrderSub" %>
<%@ page import="com.ai.ecs.order.constant.OrderConstant" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        Session shrioSession = UserUtils.getSession();
        UserGoodsCarModel carModel = (UserGoodsCarModel)shrioSession.getAttribute("carModel");
        Set<Shop> shopSet = (Set<Shop>)shrioSession.getAttribute("shopSet");
        Long orderSubAmount = (Long)shrioSession.getAttribute("orderSubAmount");
        List<TfOrderSub> orderSubList = (List<TfOrderSub>)shrioSession.getAttribute("orderSubList");
        Long orderId = orderSubList.get(0).getOrderId();
    %>
<%@include file="/WEB-INF/views/include/head.jsp"%>
    <meta name="WT.si_n" content="购物流程" />
    <meta name="WT.si_x" content="订单生成" />
    <meta name="WT.pn_sku" content="${skuIds}" />   <%--取值产品的id，多台手机用;隔开，必填--%>
    <meta name="WT.tx_s" content="${orderSubAmount}" >  <%--取值订单总价，必填--%>
    <meta name="WT.tx_u" content="${goodsNumCount}" > <%-- 取值手机的数量，必填--%>
    <meta name="WT.tx_i" content="${orderNos}" >  <%--取值订单号，必填--%>
    <%@ include file="/WEB-INF/views/include/message.jsp"%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/list.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/load.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/qm/insdc_w2.js"></script>

    <script type="text/javascript">
    /**
     * 将表单元素序列化为JSON对象
     * 基于jQuery serializeArray()
     */
    $.fn.serializeObject = function() {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name] ];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

	$(function(){
        /*设置选择优惠券链接*/
        $("a[name='chooseCoupon']").each(function () {
            var orderSubIds = [];
            var orderSubId = $(this).parents("div[name='shopDiv']").find("input[name='orderSubId']").val();
            orderSubIds.push(orderSubId);
            orderSubIds = orderSubIds.join(",");

            var href = $(this).attr("href");
            $(this).attr("href",href + "&orderSubIds="+orderSubIds);
        });

        /*确认支付*/
		$("#confirmPayBtn").click(function(){
            var $payPlatform = $('input:radio[name="payPlatform"]:checked');
            var url = ctx + "/goodsBuy/queryPayAccount";
            var param = $("#choosePayPlatform").serializeObject();
            if ($payPlatform.val() == 'FinancialPackage') {
                if(!$("#confirmCB").attr("checked")) {
                    showAlert('请阅读<<和包理财活动协议>>');
                } else {
                    showLoadPop("正在加载...")
                    $("#choosePayPlatform").submit();
                }
            } else {
                $.post(url, param, function (data) {
                    if (data.returnCode == "fail") {
                        showAlert(data.returnInfo + $payPlatform.siblings("span").text());
                        return;
                    }

                    showLoadPop("正在加载...")
                    $("#choosePayPlatform").submit();
                });
            }
		});


        $("#confirmModal").click(function(){
            if ($("#myModal").is(":visible")) {
                $("#myModal").hide();
            } else {
                $("#myModal").show();
            }
        });

        $("#comfirm").click(function(){
            $("#myModal").hide();
            $("#confirmCB").attr("checked", true);
        });

        $("#closeForm").click(function(){
            $("#myModal").hide();
            $("#confirmCB").attr("checked", true);
        });


    });
</script>
</head>
<body>
<div class="top container">
    <div class="header sub-title"><a onclick="window.history.back()" href="javascript:void(0)" class="icon-left"></a>
        <c:choose>
            <c:when test="${carModel.payMode.payModeId != 2}">
                <h1>订单提交成功</h1>
            </c:when>
            <c:otherwise>
                <h1>选择支付平台</h1>
            </c:otherwise>
        </c:choose>
        
    </div>
</div>
<div class="container pd-t45 white-bg">
    <div class="alert alert-top alert-success">
        <div class="cur-ul cur-con">
            <c:set var="paymentAmount" value="0" />
            <c:forEach items="${shopSet}" var="shop" varStatus="sStatus">
                <div name="shopDiv" class="cur-li cur-bd">
                    <p class="tabcon-cl"> <span class="pull-jl02">商家${sStatus.count}：${shop.shopName}</span> </p>
                    <div class="tabcon-cl tabcon-cl02 tabcon-cl05">
                        <c:set var="shopPayAmount" value="0" />
                        <c:forEach items="${orderSubList}" var="orderSub" varStatus="oStatus">
                            <c:if test="${orderSub.shopId == shop.shopId}">
                                <p>订单号：${orderSub.orderSubNo}<b class="font-red pull-right" style="text"><fmt:formatNumber value="${orderSub.orderSubAmount/100}" type="currency"/></b></p>
                                <input type="hidden" name="orderSubId" value="${orderSub.orderSubId}"/>
                                <c:set var="shopPayAmount" value="${shopPayAmount + orderSubPayAmount}"/>
                            </c:if>
                        </c:forEach>
                        <div class="sp-info sp03 sp-top sd-info" style="display: none" <c:if test="${carModel.payMode.payModeId != 2 || carModel.rootCategoryId == 2}">style="display: none" </c:if> >
                            <a name="chooseCoupon" href="${ctx}/goodsBuy/linkToChooseCoupon?shopId=${shop.shopId}">
                                <i class="sd-icon01 sd-icon02"></i>
                                <div class="pull-left pj-lt01">优惠券</div>
                                <div class="pull-right pj-right">
                                    <c:forEach items="${couponInfoList}" var="coupon">
                                        <c:if test="${coupon.shopId == shop.shopId}">
                                            <span class="pj-text">已选${coupon.couponValue/100}元优惠券</span>
                                        </c:if>
                                    </c:forEach>
                                </div>
                                <i class="sp-icon sp-icon04"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <c:set var="paymentAmount" value="${orderSubPayAmount + shopPayAmount}"/>
            </c:forEach>
        </div>
    </div>
    <% if(orderSubList.size()==1&&orderSubList.get(0).getShopId()==100000002099l) {%>
    10085商品
    <% }else if(orderSubList.size()==1&& "Y".equals(orderSubList.get(0).getIsFinancialPackage())) {%>
        <form id="choosePayPlatform" action="${ctx}goodsBuy/payFinancialPackage" method="post">
            <input type="hidden" name="orderNos" value="${orderNos}"/>
            <ul class="cur-ul choose-list">
                <li>
                    <label>
                        <div class="tabcon-cl">
                            <input type="radio" name="payPlatform" class="radio" value="FinancialPackage" checked="checked"/>
                            <span class="lab-jl">和包理财</span>
                        </div>
                    </label>
                </li>
            </ul>
        </form>
    <%} else {%>
        <form id="choosePayPlatform" action="${ctx}goodsBuy/payOrder" method="post" <c:if test="${carModel.payMode.payModeId != 2}">style="display: none" </c:if> >
        <input type="hidden" name="orderNos" value="${orderNos}"/>
        <c:forEach items="${shopSet}" var="shop" varStatus="sStatus">
            <input type="hidden" name="shopList[${sStatus.index}].shopId" value="${shop.shopId}"/>
            <input type="hidden" name="shopList[${sStatus.index}].shopName" value="${shop.shopName}"/>
        </c:forEach>
        <ul class="cur-ul choose-list">
            <li>
                <label>
                    <div class="tabcon-cl">
                        <input type="radio" name="payPlatform" class="radio" value="WAPIPOS_SHIPOS" checked="checked"/>
                        <span class="lab-jl">和包支付</span>
                    </div>
                </label>
            </li>
            <%--<li>
                <label>
                    <div class="tabcon-cl">
                        <input type="radio" name="payPlatform" class="radio" value="WAPALIPAY"/>
                        <span class="lab-jl">支付宝支付</span>
                    </div>
                </label>
            </li>--%>
        </ul>
        </form>
    <%
        }
    %>


    <% if(orderSubList.size()==1 && "Y".equals(orderSubList.get(0).getIsFinancialPackage())) {%>
        <div class="wx-con">
            <div class="tabcon-cl tab-fd">
				<span class="input-text02 input-text03">
					<input type="checkbox" class="checkbox" id="confirmCB"/>
					<label for="confirmCB" class="box-jl01">我已阅读并同意<a href="javascript:;" id="confirmModal">《和包理财活动协议》</a></label>
				</span>
                <span class="input-text02"></span>
            </div>
        </div>
    <%}%>

    <% if(orderSubList.size()==1 && (OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_2.getValue().equals(orderSubList.get(0).getPromotionTypeId())
            || OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_10.getValue().equals(orderSubList.get(0).getPromotionTypeId())
            || OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_11.getValue().equals(orderSubList.get(0).getPromotionTypeId())
            )) {
        OrderConstant.PROMOTION_TYPE promotionType = OrderConstant.PROMOTION_TYPE.getPromotionType(orderSubList.get(0).getPromotionTypeId());
    %>
        <div class="wx-con">
            <div class="tabcon-cl tab-fd">
                <span class="input-text02 input-text03" style="color:red">
                    恭喜您！<%=promotionType.getName() %>下单成功，请于<%=promotionType.getOrderOverTime() / 60 %>分钟以内付款完毕。
                </span>
                <span class="input-text02"></span>
            </div>
        </div>
    <%}%>

</div>

<!--阅读协议 begin-->
<div class="modal fade modal-hy in" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content modal-content">
            <div class="top container">
                <div class="header sub-title">
                    <a data-dismiss="modal" aria-label="Close" class="icon-left" id="closeForm"></a>
                    <h1>和包理财活动协议</h1>
                </div>
            </div>
            <div class="container hy-content">
                <div class="hy-con">
                    <p>1.参与本活动需确保在和聚宝1或和聚宝2号理财产品中有足够的资金，否则请先进行第一步，点击购买理财；</p>
                    <p>2.业务办理成功后即将客户存入和聚宝中对应的理财金购买定期，投资期限为12个月，理财到期后7个工作日内返回本金至和钱包账户（继续享受活期收益）；</p>
                    <p>3.本金默认回到和聚宝和钱包，可登陆和包客户端，点击“和聚宝－－我的资产和钱包－－转出提现至银行卡”选择回款即可提取使用；</p>
                    <p>4.若业务办理失败，则客户投资金将继续存放和聚宝活期产品中，享受和聚宝活期收益，客户可在和钱包中随时提取；</p>
                    <p>5. “购理财，全年免费用套餐”业务的协议有效期均为一年。理财产品购买成功后，封闭期一年，中途不可退款，不可转让；</p>
                    <p>6.协议期满前您需保证买理财绑定的银行卡正常且办理业务的手机账户不欠费。若银行卡丢失，请前往银行补办银行卡，并联系和包客服（拨打10086转和包二线客服）变更银行卡信息；目前和聚宝支持的借记卡为：工商、农业、中国银行、建设、招商、浦发、中信、光大、民生、华夏、平安、渤海、江西；</p>
                    <p>7.本活动产品收益率为本金定期理财产品收益加湖南移动业务补贴折算收益率；</p>
                    <p>8.理财收益只能用于“用套餐”“续宽带”“拿手机”三个活动三选一，参加活动后，收益将直接抵扣相应业务的产品资费，中途不可取消，收益不予返回；</p>
                    <p>9.已成功参与“用套餐”活动的理财收益不可用于抵扣“续宽带”或“拿手机”费用，如果还需要参与“续宽带”或“拿手机”，则需另存一笔理财费用进行办理；“续宽带”、“拿手机”亦然。</p>
                    <p>免费用套餐：</p>
                    <p>1.“免费用套餐”优惠期间，不可变更及降档套餐，若办理套餐升档，升档差额部分由用户自行承担;</p>
                    <p>2.变更基础资费套餐至本套餐次月生效，加量优惠也次月生效；</p>
                    <p>3.赠送流量直接送至客户手机账户，仅当月有效，不可转赠；</p>
                    <p>4.享受流量赠送的客户当月不能取消或者降档套餐。</p>
                    <p>免费续宽带：</p>
                    <p>本活动优惠仅限已办理移动宽带且宽带账户已绑定移动手机的客户办理续费宽带才可享受。</p>
                    <p>免费拿手机：</p>
                    <p>参与本活动购买手机的客户不可选择退货。</p>
                    <p>套餐免费用推荐奖励规则：</p>
                    <p>1.推荐人与被推荐人不可为同一人，以手机号对应身份证号为唯一判断标准；</p>
                    <p>2.推荐绑定关系以移动套餐业务办理成功时最近一次填写的推荐人号码为准，填写推荐人信息仅当日有效。</p>
                    <p>3.推荐人成功推荐一名用户参与本活动，推荐人可获得40元现金奖励，被推荐人可获得15元现金奖励。多推多得，奖品有限，赠完为止；</p>
                    <p>4.本次活动奖励在被推荐人成功办理移动套餐后5个工作日内发放至推荐人、被推荐人和聚宝账户中，推荐人需确定已开通和聚宝账户，否则将无法获得奖励。</p>
                    <p>备注：推荐人可为异网用户，未办理此套餐用户也可为推荐人</p>
                    <div class="bot-btn"><a shape="rect" class="btn btn-blue btn-block center-btn" aria-label="Close" data-dismiss="modal" id="comfirm"  otype="button" otitle="确定" oarea="合包理财">确 定</a></div>
                </div>
            </div>
        </div>
    </div>
</div>


<c:choose>
    <c:when test="${carModel.payMode.payModeId == 2}">
        <div class="fix-br fix-top">
		    <div class="affix foot-menu">
		        <div class="container form-group tj-btn">
		            <div class="affix-xd">
		                <p>总金额：<b class="font-rose"><fmt:formatNumber value="${orderSubPayAmount/100}" type="currency"/></b>元</p>
		                <p class="font-gray">支付方式：${carModel.payMode.payModeName}</p>
		            </div>
		            <a href="${ctx}" class="btn btn-green">稍后支付</a>
                    <% if(orderSubList.size()==1&&orderSubList.get(0).getShopId()==100000002099l) {%>
                        <a class="btn btn-blue" href="http://www.10085.cn/web85/page/zyzxpay/wap_order.html?orderId=<%=orderSubList.get(0).getThirdPartyId()%>"  otype="button" otitle="确认支付" oarea="终端">去85平台支付</a>
                    <%}else{%>
                        <a id="confirmPayBtn" class="btn btn-blue" href="javascript:void(0)"  otype="button" otitle="确认支付" oarea="终端">确认支付</a>
                    <%}%>
                </div>
		    </div>
		</div>
    </c:when>
    <c:otherwise>
        <div class="fix-br fix-top">
		    <div class="affix foot-menu">
		        <div class="container form-group tj-btn">
		            <a href="${ctx}" class="btn btn-green">继续购物</a>
		            <a id="confirmPayBtn" class="btn btn-blue" href="${ctx}myOrder/toMyAllOrderList"  otype="button" otitle="查看订单" oarea="终端">查看订单</a>
		        </div>
		    </div>
		</div>
    </c:otherwise>
</c:choose>


</body>
</html>