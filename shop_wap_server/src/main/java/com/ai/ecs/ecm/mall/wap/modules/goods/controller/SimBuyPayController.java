package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Exceptions;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.JsonUtil;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.OrderPayService;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.SimBuyCommonService;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.TfOrderRefund;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogThrowable;

/**
 * Created by cc on 2018/6/21.
 * 在线售卡支付处理类
 */
@Controller
@RequestMapping("simpay")
public class SimBuyPayController extends BaseController {

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IPlansService plansService;

    @Autowired
    private SimBuyCommonService simBuyCommonService;

    @Autowired
    private IOrderService orderService;


    /**
     * 跳转到支付页面
     *
     * @return
     */
    @RequestMapping("toPay")
    public String payOrder(String orderNo, String confId, String planId, String selectPhone, Model model) {
        //TODO
//        Date date = orderService.getTaskCreateDate(orderNo, "SimOrderProcess_1");
//        if (date == null) {
//            throw new RuntimeException("您的订单已超时,请重新下单!");
//        }
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
        //== 依据planId获取该档次个性信息
        JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), planId, TfH5SimConf.JSON_ID);
        Long preFee = Long.parseLong(object.getString(TfH5SimConf.JSON_PREFEE));//预存金额
        Long cardFee = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));//卡金额
        Long planRMB = preFee + cardFee;//仅用于展示,实际支付的钱在合并订单的时候计算
        model.addAttribute("orderNos", orderNo);
        model.addAttribute("conf", resultPlan);
        model.addAttribute("planRMB", planRMB);
        model.addAttribute("selectPhone", selectPhone);
        return "web/goods/sim/confirmPaySim";
    }

    @RequestMapping("payOrder")
    @ResponseBody
    public ResponseBean payOrder(HttpServletRequest request, String orderNos) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        String radioCodeType = request.getParameter("radiocode");
        String payPlatform = CommonParams.PAY_ENUM_PAYPLATFORM.get(radioCodeType);
        if (payPlatform == null) {
            responseBean.addError("-1", "支付平台选择错误");
            return responseBean;
        }
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String callbackUrl = basePath + "/simpay/toPayResult";
        //String notifyUrl = afterOrderPayUrl + "/simBuy/afterPayOrder"; http://a505433b.ngrok.io
        //灰度地址
        String notifyUrl = "http://10.13.11.27:8084/shop/simpay/afterPayOrder";//这是支付中心回调我们的
        return orderPayService.payOrder(orderNos, callbackUrl, notifyUrl, payPlatform);
    }

    @RequestMapping("afterPayOrder")
    public void afterPayOrder(HttpServletRequest request, String returnCode, String status, String message, String merchantId,
                              String payDate, String type, String payNo, String organization_payNo,
                              String org_code, String accountDate, Long orderId, Long amount, String version,
                                String organization_result_desc,String result_Pay_Type) throws Exception {
        orderPayService.afterPayOrder(request, returnCode, status, message, merchantId,
                payDate, type, payNo, organization_payNo,
                org_code, orderId, version, organization_result_desc,result_Pay_Type);
    }

    /**
     * @param model
     * @param returnCode
     * @param chargeflowId
     * @return
     */
    @RequestMapping("toPayResult")
    public String toPayResult(Model model, String returnCode, Long chargeflowId) {
        TfOrderSub orderSub = new TfOrderSub();
        orderSub.setOrderId(chargeflowId);
        List<TfOrderSub> orderSubList = orderQueryService.queryBaseOrderList(orderSub);
        if (orderSubList.isEmpty()) {
            throw new RuntimeException("订单信息不存在");
        }
        TfOrderSub tfOrderSub = orderSubList.get(0);//订单信息
        TfOrderSubDetail detailParam = new TfOrderSubDetail();
        detailParam.setOrderSubId(tfOrderSub.getOrderSubId());
        List<TfOrderSubDetail> details = orderQueryService.queryOrderDetailList(detailParam);
        if (details.isEmpty()) {
            throw new RuntimeException("订单信息不存在");
        }
        TfOrderSubDetail detail = details.get(0);
        TfOrderDetailSim simParam = new TfOrderDetailSim();
        simParam.setOrderSubId(tfOrderSub.getOrderSubId());
        List<TfOrderDetailSim> detailSims = orderQueryService.queryOrderDetailSimList(simParam);
        TfOrderDetailSim detailSim = detailSims.get(0);
        //获取号码的配置信息
        if ("0000".equals(returnCode)) {
            String confId = String.valueOf(detail.getProdSkuId());
            TfH5SimConf cond = new TfH5SimConf();
            cond.setConfId(confId);
            TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
            //跳转到didi页面
            if ("E0DD".equals(resultPlan.getChnlCodeOut())) {
                model.addAttribute("orderNo", tfOrderSub.getOrderSubNo());
                return "web/goods/sim/didiSimSuccess";
            }
            //跳转到大王卡的页面
            if ("3".equals(resultPlan.getCardType())) {
                //取出recmdCode信息,在tf_order_detail_sim取出cityCodeSuffix 信息
                String recmdCode = JedisClusterUtils.get(tfOrderSub.getOrderSubNo() + "recmdCode");
                model.addAttribute("confId", confId);
                model.addAttribute("recmdCode", recmdCode);
                model.addAttribute("cityCodeSuffix", detailSim.getCityCode().substring(2));
                model.addAttribute("simBusiConfs", simBuyCommonService.getBusiConfig());
                return "web/goods/sim/recmd/orderSuccess";
            }
            //其他情况跳转到信息填写页面注入已经填好的信息,显示在微厅可以查询订单的提示信息
            return "web/goods/sim/confirmAfterPayOrderSuccess";
        } else {
            /*支付失败跳转到我的订单页面,
            可以重新进行支付 拼接支付参数
            orderSubNo,confId(orderSubDetail的productSkuId),planId(orderSubDetail的goodskuId),selectPhone*/
            model.addAttribute("tfOrderSub", tfOrderSub);
            model.addAttribute("orderDetail",detail);
            model.addAttribute("selectPhone",detailSim.getPhone());
            return "web/goods/sim/confirmAfterPayOrderFail";
        }
    }



    @RequestMapping("retSimUI")
    public String retSimUI(TfOrderSub subParam,Model model){
        TfOrderSub orderView = orderQueryService.queryComplexOrder(
                subParam, Variables.ACT_GROUP_MEMBER);
        model.addAttribute("orderSub",orderView);
        return "web/goods/o2osimshop/orderSimRefund";
    }


    /**
     * 退款申请 以商品规格为单位进行退货，不能整个订单进行退款
     */
    @RequestMapping(value = "retMoneyApply", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean retMoneyApply(TfOrderSub subParam,String retMoneyReason){
        String streamNo = createStreamNo();
        TfOrderSub queriedSubOrder = orderQueryService.queryComplexOrder(
                subParam, Variables.ACT_GROUP_MEMBER);
        ResponseBean responseBean = new ResponseBean();
        try{
            //保存退款单据
            TfOrderRefund orderRefund = new TfOrderRefund();
            orderRefund.setOrderId(queriedSubOrder.getOrderId());
            orderRefund.setOrderSubId(queriedSubOrder.getOrderSubId());
            orderRefund.setOrderSubNo(queriedSubOrder.getOrderSubNo());
            orderRefund.setRefundReason(retMoneyReason);
            orderService.saveOrderRefund(orderRefund);
            responseBean.addSuccess("退款成功");
        }catch (Exception e){
            logger.error("发起退款失败",e);
            if(e instanceof EcsException){
                EcsException e1 = (EcsException)e;
                responseBean.addError("-1",e1.getFriendlyDesc());
            }
            writerFlowLogThrowable(streamNo, subParam.getOrderSubNo(), "", this.getClass().getName(), "retMoneyApply", null, Exceptions.getStackTraceAsString(e));
            responseBean = ExceptionUtils.dealExceptionRetMsg(responseBean, e, "退款失败");

        }
        return responseBean;
    }


}
