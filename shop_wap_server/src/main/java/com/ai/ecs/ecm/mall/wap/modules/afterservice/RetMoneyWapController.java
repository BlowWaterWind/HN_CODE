package com.ai.ecs.ecm.mall.wap.modules.afterservice;

import com.ai.ecs.afterservice.api.IAftersaleServerService;
import com.ai.ecs.afterservice.constant.AftersaleTypeConstant;
import com.ai.ecs.afterservice.entity.AftersaleReplyReason;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.config.Global;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderRefund;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("afterserviceWap/retMoney")
public class RetMoneyWapController extends BaseController {
    private final String pagePath = "web/afterservice/";

    @Autowired
    private IAftersaleServerService aftersaleService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IOrderService orderService;

    @Value("${ecop.host}")
    private String ecopHost;

    /**
     * 跳转到申请退款页面
     */
    @RequestMapping(value = "retMoneyUI")
    public String retMoneyUI(HttpServletRequest request, Map<String, Object> retMap) {
        long subOrderId = Long.parseLong(request.getParameter("subOrderId"));
        long subOrderDetailId = Long.parseLong(request.getParameter("subOrderDetailId"));
        List<AftersaleReplyReason> asReasonL = new ArrayList<AftersaleReplyReason>();
        TfOrderSub showOrder = new TfOrderSub();

        TfOrderSub orderSub = new TfOrderSub();
        orderSub.setOrderSubId(subOrderId);
        try {
            // 退款原因列表
            asReasonL = aftersaleService.selectAllListByTypeId(AftersaleTypeConstant.AFTERSALE_TYPE_RETURNMONEY);
            showOrder = orderQueryService.queryComplexOrder(orderSub, Variables.ACT_GROUP_MEMBER);
            List<TfOrderSubDetail> subOrderDetailList = showOrder.getDetailList();
            //== 查看是否已经退款；求可退金额 ==//
            for (TfOrderSubDetail detl : subOrderDetailList) {
                if (subOrderDetailId == detl.getOrderSubDetailId()) {
                    showOrder.getDetailList().removeAll(showOrder.getDetailList());
                    showOrder.getDetailList().add(detl);
                    // 支付总金额
                    long l1 = showOrder.getOrderSubPayAmount();
                    // sku价格*退款的sku数目
                    long l2 = detl.getGoodsSkuPrice() * detl.getGoodsSkuNum();
                    long maxretMony = l1 < l2 ? l1 : l2;
                    // 退款金额
                    retMap.put("maxretMony", maxretMony);
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "申请失败，退款异常");
            return pagePath + "retMoneyWapBack";
        }
        retMap.put("showOrder", showOrder);
        retMap.put("asReasonL", asReasonL);
        return pagePath + "retMoneyWap";
    }

    /**
     * 退款申请 以商品规格为单位进行退货，不能整个订单进行退款
     */
    @RequestMapping(value = "retMoneyApply", method = RequestMethod.POST)
    public String retMoneyApply(HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== retMoneyApply：进入退款申请 ==========");
        String retMoneyReason = request.getParameter("retMoneyReason");
        // 页面退款金额
        float retMoneyF = Float.parseFloat(request.getParameter("retMoneyCount"));
        // 退款存库金额
        long retMoneyL = (long)(retMoneyF * 100);
        long subOrderId = Long.parseLong(request.getParameter("subOrderId"));
        retMap.put("asServerName", "retMoney");
        retMap.put("retMoneyCount", retMoneyF);
        retMap.put("retMoneyReason", retMoneyReason);

        TfOrderSub orderSub = new TfOrderSub();
        orderSub.setOrderSubId(subOrderId);

        // 退款金额后台验证
        try {
            TfOrderSub queriedSubOrder = orderQueryService.queryComplexOrder(orderSub);
            retMap.put("queriedSubOrder", queriedSubOrder);
            List<TfOrderSubDetail> subOrderDetailList = queriedSubOrder.getDetailList();
            // 子订单支付总金额
            long l1 = queriedSubOrder.getOrderSubPayAmount();
            long l2 = 0L;
            for (TfOrderSubDetail detl : subOrderDetailList) {
                // sku价格*退款的sku数目
                l2 += detl.getGoodsSkuPrice() * detl.getGoodsSkuNum();
            }
            long maxretMony = l1 < l2 ? l1 : l2;
            retMap.put("maxretMony", maxretMony);
            //可以退款
            if (retMoneyL <= maxretMony) {
                //数据是否在我这是否存库
                //orderSub.setOrderStatusId(OrderConstant.STATUS_REFUND);
                //orderSub.setRefundReason(retMoneyReason);
                //orderSub.setRefundTime(new Date());
                //orderService.updateOrder(orderSub);

                TfOrderRefund orderRefund = new TfOrderRefund();
                orderRefund.setOrderId(queriedSubOrder.getOrderId());
                orderRefund.setOrderSubId(queriedSubOrder.getOrderSubId());
                orderRefund.setOrderSubNo(queriedSubOrder.getOrderSubNo());
                orderRefund.setRefundReason(retMoneyReason);
                orderService.saveOrderRefund(orderRefund);
            }
            //无法退款
            else {
                retMap.put("errorMsg", "申请失败，您填写的退款总金额高于支付总金额");
                return pagePath + "retMoneyWapBack";
            }
            logger.info("retMoneyApply："+JSONObject.toJSONString(queriedSubOrder,SerializerFeature.WriteMapNullValue));
            retMap.put("sucMsg", "进入退款流程，正在退款……");
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "对不起，您的退款申请失败，请确保退款符合条件");
        }
        return pagePath + "retMoneyWapBack";
    }

    //@RequestMapping(value = "retMoneyList1", method = RequestMethod.POST)
    //@ResponseBody
    //public String retMoneyList1(HttpServletRequest request, HttpServletResponse response,
    //        Map<String, Object> retMap) {
    //    Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
    //    // 测试数据
    //    TfOrderSub subOrder = new TfOrderSub();
    //    MemberInfo memInfo = new MemberInfo();
    //    memInfo.setMemberId(memberId);
    //    // subOrder.setMemberInfo(memInfo);
    //    // 查询条件：订单状态组
    //    List<Integer> orderStatusIds = new ArrayList<>();
    //    // 其它订单状态待补充
    //    orderStatusIds.add(OrderConstant.STATUS_REFUND);
    //    orderStatusIds.add(OrderConstant.STATUS_REFUND_END);
    //    subOrder.setOrderStatusIds(orderStatusIds);
    //
    //    Page<TfOrderSub> subOrderPage = new Page<TfOrderSub>();
    //    // subOrderPage.setPageSize((getPageSize(request, response,3)));
    //    if (subOrder.getPage() == null) {
    //        Page<TfOrderSub> page = new Page<TfOrderSub>();
    //        subOrder.setPage(page);
    //    }
    //    subOrder.getPage().setPageNo(getPageNo(request, response));
    //    subOrder.getPage().setPageSize(getPageSize(request, response, 3));
    //    try {
    //        subOrderPage = orderQueryService.queryComplexOrderPage(subOrder);
    //    }
    //    catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    List<TfOrderSub> orderviewL = subOrderPage.getList();
    //    Map<String, Object> respOrderMap = new HashMap<String, Object>();
    //
    //    // 服务类型
    //    respOrderMap.put("serverType", "retMoney");
    //    // 没有列表信息时
    //    if (orderviewL.size() == 0) {
    //        respOrderMap.put("retMsg", Global.NO);
    //    }
    //    // 存在列表信息时
    //    else {
    //        respOrderMap.put("retMsg", Global.YES);
    //        // 退款信息
    //        respOrderMap.put("page", subOrderPage);
    //    }
    //    String orderviewLJson = JSON
    //            .toJSONString(respOrderMap, SerializerFeature.WriteMapNullValue);
    //    logger.info(orderviewLJson);
    //    return orderviewLJson;
    //}

    /**
     * 申请退款信息列表
     * @param request
     * @param response
     * @param retMap
     * @return
     */
    @RequestMapping(value = "retMoneyList", method = RequestMethod.POST)
    @ResponseBody
    public String retMoneyList(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> retMap) {
        logger.info("========== wap端：退款列表信息 ==========");
        String tfsUrl = request.getParameter("tfsUrl");
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        // 用户数据
        TfOrderSub subOrder = new TfOrderSub();
        TfOrderUserRef ref = new TfOrderUserRef();
        ref.setMemberId(memberId);
        subOrder.setOrderUserRef(ref);
        // 查询条件：订单状态组
        List<Integer> orderStatusIds = new ArrayList<>();
        // 其它订单状态待补充 FIXME
        orderStatusIds.add(OrderConstant.STATUS_REFUND);
        orderStatusIds.add(OrderConstant.STATUS_REFUND_END);
        orderStatusIds.add(OrderConstant.STATUS_REFUND_CALLBACK);
        orderStatusIds.add(OrderConstant.STATUS_REFUND_FAIL);

        subOrder.setOrderStatusIds(orderStatusIds);
        //待转Json的Map
        Map<String, Object> respOrderMap = new HashMap<String, Object>();
        Page<TfOrderSub> subOrderPage = new Page<TfOrderSub>();
        if (subOrder.getPage() == null) {
            Page<TfOrderSub> page = new Page<TfOrderSub>();
            subOrder.setPage(page);
        }
        subOrder.getPage().setPageNo(getPageNo(request, response));
        subOrder.getPage().setPageSize(getPageSize(request, response, 5));
        try {
            subOrderPage = orderQueryService.queryComplexOrderPage(subOrder, Variables.ACT_GROUP_MEMBER);
            List<TfOrderSub> orderviewL = subOrderPage.getList();
            // 服务类型
            respOrderMap.put("serverType", "retMoney");
            if (orderviewL.size() == 0) {
                respOrderMap.put("retMsg", Global.NO);
            }
            else {
                respOrderMap.put("retMsg", Global.YES);
                respOrderMap.put("page", subOrderPage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            respOrderMap.put("retMsg", Global.NO);
        }
        retMap.put("page", subOrderPage);
        respOrderMap.put("tfsUrl",tfsUrl);
        String retjson = JSON.toJSONString(respOrderMap, SerializerFeature.WriteMapNullValue);
        logger.info("retMoneyList："+ JSONObject.toJSONString(retjson,SerializerFeature.WriteMapNullValue));
        logger.info(retjson);
        return retjson;
    }

    /**
     * 滑动分页：异步请求分页数据
     */
    @RequestMapping(value = "reqMyRetMoneyListAjax", method = RequestMethod.POST)
    @ResponseBody
    public String reqMyRetMoneyListAjax(HttpServletRequest request, HttpServletResponse response) {
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        TfOrderSub subOrder = new TfOrderSub();
        MemberInfo memInfo = new MemberInfo();
        memInfo.setMemberId(memberId);

        // 待转json的Map
        Map<String, Object> respOrderMap = new HashMap<String, Object>();

        // 查询条件：订单状态组
        List<Integer> orderStatusIds = new ArrayList<>();
        // 其它订单状态待补充
        orderStatusIds.add(OrderConstant.STATUS_REFUND);
        orderStatusIds.add(OrderConstant.STATUS_REFUND_END);
        subOrder.setOrderStatusIds(orderStatusIds);

        Page<TfOrderSub> subOrderPage = new Page<>();
        if (subOrder.getPage() == null) {
            Page<TfOrderSub> page = new Page<TfOrderSub>();
            subOrder.setPage(page);
        }
        subOrder.getPage().setPageNo(getPageNo(request, response));
        subOrder.getPage().setPageSize(getPageSize(request, response, 10));
        try {
            subOrderPage = orderQueryService.queryComplexOrderPage(subOrder);
            List<TfOrderSub> orderviewL = subOrderPage.getList();
            // 服务类型
            respOrderMap.put("serverType", "retMoney");
            // 没有列表信息时
            if (orderviewL.size() == 0) {
                respOrderMap.put("retMsg", Global.NO);
            }
            // 存在列表信息时
            else {
                respOrderMap.put("retMsg", Global.YES);
                respOrderMap.put("page", subOrderPage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            respOrderMap.put("retMsg", Global.NO);
        }

        String orderviewLJson = JSON.toJSONString(respOrderMap, SerializerFeature.WriteMapNullValue);
        //== 返回json数据处理 ==//
        //orderviewLJson = orderviewLJson.replace("[]", "[{}]");
        return orderviewLJson;
    }
}
