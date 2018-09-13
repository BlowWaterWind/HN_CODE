package com.ai.ecs.ecm.mall.wap.modules.goods.service;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecsite.modules.recharge.entity.DqBuildSeqCondition;
import com.ai.ecs.ecsite.modules.recharge.entity.DqPaySuccFuncCondition;
import com.ai.ecs.ecsite.modules.recharge.service.RechargeService;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.util.UppCore;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ai.iis.upp.util.UppCore.getHashMapParam;
import static com.ai.iis.upp.util.UppCore.sentHttp2;

/**
 * Created by cc on 2018/4/28.
 * 商城支付接入统一支付平台支付
 */
@Service
public class OrderPayService {


    @Value("${afterOrderPayUrl}")
    private String afterOrderPayUrl;

    @Value("${orderPayUrl}")
    private String orderPayUrl;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private IUppHtmlValidataService validataService;


    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 统一支付第二步: 向支付中心发起支付请求
     *
     * @param orderPay
     * @param callBackUrl
     * @param notifyUrl
     * @return
     * @throws Exception
     */
    public Map<String, String> confirmPayInfo(TfOrderPay orderPay,
                                              String callBackUrl, String notifyUrl,TfOrderSub orderSub) throws Exception {
        String merchantId = CommonParams.PAY_ENUM_MERCHANT.get(orderPay.getOrgCode());

        String orderDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("callbackUrl", callBackUrl);//同步页面返回地址
        paramMap.put("notifyUrl", notifyUrl);//异步接收支付结果地址
        paramMap.put("payOrg", orderPay.getOrgCode());//支付机构
        paramMap.put("type", OrderConstant.PayOrgInterfaceType.getInterfaceType(orderPay.getOrgCode().replace("-", "_")));//接口类型
        paramMap.put("version", "2.0.0");//版本号：固定为2.0.0
        paramMap.put("characterSet", "UTF-8");//编码方式
        paramMap.put("channelId", OrderConstant.PayChannelCode.getPayChannel(CommonParams.CHANNEL_CODE));
        paramMap.put("amount", String.valueOf(orderPay.getOrderPayAmount()));//订单金额 金额单位为分
        paramMap.put("currency", "00");//币种
        paramMap.put("orderDate", orderDate);//订单提交日期
        paramMap.put("merchantId", merchantId);//统一支付平台分配的唯一商户编号
        paramMap.put("merchantOrderId", String.valueOf(orderPay.getOrderId()));//商户订单号,对应T_EZF_FLOW的T_EZF_FLOW_OUTERID
        paramMap.put("OrderNo", orderPay.getPayNumber());//支付流水;对应
        paramMap.put("merAcDate", orderDate);//商户会计日期，与订单提交日期保持一致
        paramMap.put("productDesc", orderPay.getProductDesc());//商品介绍
        paramMap.put("productName", orderPay.getProductName());//商品名称
        paramMap.put("payType", orderPay.getPayType());// 支付类型：1-充值缴费;2-终端类订单;3-宽带订单支付;4-号卡;5-一体化平台;6-流量
        paramMap.put("Gift", String.valueOf(orderPay.getGift()));//支付类型
        paramMap.put("productId", orderPay.getProductId());//产品名称
        paramMap.put("period", "30");//有效期数量
        paramMap.put("IDType", "01");////中国移动用户标识类型 01：表示11位手机号 02：邮箱 03：固话 04：宽带  05：物联网号码
        paramMap.put("authorizeMode", "WAP"); // 推荐用户进行确认的方式 :推荐用户进行确认的方式  WEB/WAP
        paramMap.put("IvrMobile", orderPay.getIvrMobile());
        paramMap.put("mobile", orderPay.getIvrMobile());//二阶段支付用于用户标识
        paramMap.put("periodUnit", "00");//有效期单位：00-分，01-小时，02-日，03-月
        paramMap.put("MerActivityID", orderPay.getMerActivityID());
        paramMap.put("PaymentLimit", orderPay.getPaymentLimit());
        paramMap.put("ProductURL", orderPay.getProductURL());
        paramMap.put("reserved1", orderSub.getOrderCityCode());//保留字段(二阶段支付收银台作为REGION_ID入参)
        paramMap.put("reserved2", orderPay.getPayType());// 业务类型;1-充值缴费;2-终端类订单;3-宽带订单支付;4-号卡;5-一体化平台;6-流量
        paramMap.put("hmac", UppCore.getHmac(paramMap,CommonParams.PAY_ENUM_MERCHANT_KEY.get(orderPay.getOrgCode()) , "UTF-8"));//签名数据
        paramMap.put("requestPayUrl","http://10.13.11.27:8090/upay/payOrder.html");
        return paramMap;
    }

    /**
     * 统一支付第一步:保存支付信息
     * 浦发
     *
     * @param orderSubNo
     * @param payPlatform
     * @param OrderNo
     * @return
     */
    public TfOrderPay processOrderPay(String orderSubNo, String payPlatform, String OrderNo) {
        /*订单合并：子订单关联主订单，用于支付*/
        Map<String, Object> resultMap = orderService.mergeOrder(orderSubNo);
        TfOrder order = (TfOrder) resultMap.get("order");
        String merchantId = CommonParams.PAY_ENUM_MERCHANT.get(payPlatform);
        List<TfOrderSub> orderSubList = (List<TfOrderSub>) resultMap.get("orderSubList");
        //订单支付金额
        String orderPayAmount = String.valueOf(order.getOrderPayAmount());
        Integer orderTypeId = orderSubList.get(0).getOrderTypeId();
        List<TfOrderSubDetail> details = orderSubList.get(0).getDetailList();
        //用户标识传的是用户购买的号码;
        StringBuilder phone = new StringBuilder();
        for (TfOrderSubDetail detail : details) {
            phone.append(detail.getGoodsId());
        }
        String productName = GoodsService.getProductName(orderSubList);
        TfOrderPay orderPay = new TfOrderPay();
        orderPay.setOrderId(order.getOrderId());
        //orderPayAmount = "1";//todo 测试代码记得去掉
        orderPay.setOrderPayAmount(Long.parseLong(orderPayAmount));//支付前述
        orderPay.setOrgCode(payPlatform);//支付机构
        orderPay.setMerchantId(merchantId);
        orderPay.setHmac(CommonParams.PAY_ENUM_MERCHANT_KEY.get(orderPay.getOrgCode()));
        orderPay.setOrderHarvestExpend("0");
        orderPay.setOrderPayTime(new Date());
        orderPay.setGift(0L);//统一支付必填字段
        orderPay.setOrderHarvestExpend("0");
        orderPay.setiDType("01");//中国移动用户标识类型 01：表示11位手机号 02：邮箱 03：固话 04：宽带  05：物联网号码
        orderPay.setMerActivityID("");
        orderPay.setPaymentLimit("");
        orderPay.setIvrMobile(phone.toString());
        orderPay.setOrderPayPerson(phone.toString());
        orderPay.setOrderChannelCode(CommonParams.CHANNEL_CODE);
        orderPay.setPayChannel(CommonParams.CHANNEL_CODE);
        orderPay.setProductId(phone.toString());
        orderPay.setProductName(productName);
        orderPay.setProductDesc(productName);
        orderPay.setProductURL("http://wap.hn.10086.cn/shop/");
        orderPay.setPayNumber(OrderNo);//一级支付流水
        orderPay.setPayNo(OrderNo);//退款时取这个字段作为refund(OrderPayBusiness.java#saveRefund)表的payNo,传到支付中心作为EzfFlowInterid查询支付记录
        orderPay.setVersion("2.0.0");
        // 支付类型：1-充值缴费;2-终端类订单;3-宽带订单支付;4-号卡;5-一体化平台;6-流量
        orderPay.setPayType(CommonParams.PAY_TYPE.get(orderTypeId));
        orderPay.setType(OrderConstant.PayOrgInterfaceType.getInterfaceType(payPlatform.replace("-", "_")));
        orderService.mergeOrderPay(orderPay);//更新支付流水号
        return orderPay;
    }

    /**
     * 根据支付异步回调的结果通知BOSS
     * 统一支付回调以后:回调成功后将结果回传给BOSS
     * SERIAL_NUMBER 银行相关SERIAL_NUMBER 保存在deposit中
     * 在线售卡支付SERIAL_NUMBER 保存在TF_ORDER_DETAIL_SIM中
     */
    public void syncBossPayResult(TfOrderPay tfOrderPay, TfOrderSub orderSub) throws Exception {
        List<TfOrderSubDetailBusiParam> paramList = orderQueryService.queryBusiParamByOrderSub(orderSub.getOrderSubId());
        Map<String, Object> paramMap = new HashMap<>();
        for (TfOrderSubDetailBusiParam tfOrderSubDetailBusiParam : paramList) {
            paramMap.put(tfOrderSubDetailBusiParam.getSkuBusiParamName(),
                    tfOrderSubDetailBusiParam.getSkuBusiParamValue());
        }
        String tradeId = (String) paramMap.get("TRADE_ID");
        String eparchyCode = (String) paramMap.get("ROUTE_EPARCHY_CODE");
        DqPaySuccFuncCondition dqPaySuccFuncCondition = new DqPaySuccFuncCondition();
        dqPaySuccFuncCondition.setAMOUNT(String.valueOf(tfOrderPay.getOrderPayAmount()));
        dqPaySuccFuncCondition.setTRADE_ID(tradeId);
        dqPaySuccFuncCondition.setROUTE_EPARCHY_CODE(eparchyCode);
        dqPaySuccFuncCondition.setSERIAL_NUMBER(orderSub.getPhoneNumber());
        String payMoneyCode = "";
        String org_code = tfOrderPay.getOrgCode();
        if (StringUtils.isNotBlank(org_code) && org_code.lastIndexOf("ALIPAY") != -1) {
            payMoneyCode = "A";
        }
        if (StringUtils.isNotBlank(org_code) && org_code.lastIndexOf("WEIXIN") != -1) {
            payMoneyCode = "W";
        }
        if (StringUtils.isNotBlank(org_code) && org_code.lastIndexOf("UNIONPAY") != -1) {
            payMoneyCode = "U";
        }
        if (StringUtils.isNotBlank(org_code) && org_code.lastIndexOf("CMPAY") != -1) {
            payMoneyCode = "C";
        }
        dqPaySuccFuncCondition.setPAY_MONEY_CODE(payMoneyCode);
        //预受理的支付流水
        dqPaySuccFuncCondition.setPEER_ORDER_ID(tfOrderPay.getPayNumber());
        for (int i = 0; i <= 2; i++) {
            Map resultDataMap = rechargeService.paySuccFunc(dqPaySuccFuncCondition);
            LoggerFactory.getLogger("webDbLog").info("异步回调结果" + resultDataMap);
            tfOrderPay.setSynCode((String) resultDataMap.get("respCode"));
            tfOrderPay.setSynDesc((String) resultDataMap.get("respDesc"));
            if (null != resultDataMap && "0".equals(resultDataMap.get("respCode"))) {
                break;
            } else {
                Thread.sleep(3000);
            }
        }
    }


    public ResponseBean getPaySequence() throws Exception {
        ResponseBean responseBean = new ResponseBean();
        //支付流水
        String OrderNo = null;
        DqBuildSeqCondition dqBuildSeqCondition = new DqBuildSeqCondition();
//        dqBuildSeqCondition.setStaffId("SUPERUSR");
//        dqBuildSeqCondition.setTradeDepartPassword("ai1234");
        Map resultDataOrderNoMap = rechargeService.getOrderNo(dqBuildSeqCondition);
        if (null != resultDataOrderNoMap && "0".equals(resultDataOrderNoMap.get("respCode"))) {
            Map OrderNoData = (Map) ((List) resultDataOrderNoMap.get("result")).get(0);
            OrderNo = (String) OrderNoData.get("SEQ");
            if (org.apache.commons.lang3.StringUtils.isBlank(OrderNo)) {
                responseBean.addError("-2", "获取流水号失败！" + resultDataOrderNoMap.get("respDesc").toString());
            } else {
                responseBean.addSuccess("成功", OrderNo);
            }
            return responseBean;
        } else {
            responseBean.addError("-2", "获取流水号失败！" + resultDataOrderNoMap.get("respDesc").toString());
            return responseBean;
        }
    }

    public ResponseBean payOrder(String orderNos, String callbackUrl, String notifyUrl,
                                 String payPlatform) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        TfOrderSub param = new TfOrderSub();
        param.setOrderSubNo(orderNos);
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(param);
        //查询是否已经支付成功
        try {
            TfOrderPay orderPay = orderService.checkPayFromBoss(orderSub);
            if (orderPay != null && "SUCCESS".equals(orderPay.getPayState()) && orderPay.getPayLogId() == null) {
                //查询的时候payLogId为空,说明没有回调
                String payDate = com.ai.ecs.common.utils.DateUtils.formatDate(orderPay.getSettleDate(),"yyyyMMdd");
                afterPayOrderOperation("0000", "SUCCESS", orderPay.getMessage(),
                        payDate, orderPay.getPayNo(), orderPay.getOrgCode(), String.valueOf(orderPay.getOrderId()),orderPay.getOrderId()
                        , orderPay.getVersion(),orderPay.getSynDesc(),orderPay.getPayChannel());
                throw new Exception("该订单已支付成功，请不要重复支付！");
            }else if(orderPay != null && "SUCCESS".equals(orderPay.getPayState()) && orderPay.getPayLogId() != null){
                throw new Exception("该订单已支付成功，请不要重复支付！");
            }
            //支付失败或者未支付都重新支付 获取一级支付流水
            DqBuildSeqCondition dqBuildSeqCondition = new DqBuildSeqCondition();
            String OrderNo = rechargeService.getOrderNoAnalysis(dqBuildSeqCondition);
            TfOrderPay tfOrderPay = processOrderPay(orderNos, payPlatform, OrderNo);
            Map map = confirmPayInfo(tfOrderPay, callbackUrl, notifyUrl,orderSub);
            responseBean.addSuccess(map);
        } catch (Exception e) {
            logger.error("订单支付异常：", e);
            if(e instanceof EcsException){
                EcsException e1 = (EcsException)e;
                responseBean.addError("-1",e1.getFriendlyDesc());
            }else{
                throw new Exception("订单支付异常：" + e.getMessage());
            }
        }
        return responseBean;
    }


    /**
     *    "returnCode" -> "0000"
     *    "status" -> "SUCCESS"
     *    "message" -> "SUCCESS"
     *    "orderDate" -> "20180528120000"
     *    "type" -> "GWDirectToPayOrg"
     *    "organization_payNo" ->
     *    "payNo" -> "07312018052817183741352800004583"
     *    "org_code" -> "CMPAY-WAP_CMCCPAYH5"
     *    "orderDate" -> "20180528120000"
     *    "version" -> "2.0.0"
     *    "amount" -> "1"
     *    "merchantId" -> "888073157379240"
     *    "payDate" -> "20180528"
     *    "reserved1" -> orderSub.getOrderCityCode()
     *    "reserved2" -> orderPay.getPayType()
     *    "orderId" -> "68961647748911104"
     *    由支付中心/dev/upp/upp-parent-hunan/uppHtml/src/main/java/com/ai/iis/upp/html/service/impl/CommonServiceImpl.java#syncPayNotify方法通知
     *    回传了17个字段
     * @param request
     * @param returnCode
     * @param status
     * @param message
     * @param merchantId
     * @param payDate
     * @param type
     * @param payNo
     * @param organization_payNo
     * @param org_code
     * @param orderId
     * @param version
     * @param organization_result_desc
     * @param result_Pay_Type
     * @throws Exception
     */
    public void afterPayOrder(HttpServletRequest request, String returnCode, String status, String message, String merchantId,
                              String payDate, String type, String payNo, String organization_payNo,
                              String org_code, Long orderId, String version,String organization_result_desc,String result_Pay_Type) throws Exception {
        logger.info("接入统一支付异步回调开始执行");
        Map<String, String> paramMap = getHashMapParam(request.getParameterMap());
        String responseHmac = paramMap.remove("hmac");
        String hmac1 = UppCore.getHmac(paramMap,CommonParams.PAY_ENUM_MERCHANT_KEY.get(org_code), "UTF-8");
        if (!hmac1.equals(responseHmac)) {
            throw new Exception("签名验证未通过");
        }
        MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
        Map<String, String> payParamMap = getHashMapParam(request.getParameterMap());
        if (!validataService.valHmac(payParamMap, merChantBean)) {
            throw new Exception("签名验证未通过");
        }
        afterPayOrderOperation(returnCode, status, message, payDate, payNo,type, organization_payNo, orderId, version,organization_result_desc,result_Pay_Type);
    }

    public  void afterPayOrderOperation(String returnCode, String status, String message,
                                        String payDate, String payNo,String type,String organization_payNo,Long orderId, String version,String organization_result_desc,String result_Pay_Type) throws Exception {
        //订单支付信息
        TfOrderPay orderPay = new TfOrderPay();
        orderPay.setOrderId(orderId);
        orderPay = orderQueryService.queryOrderPay(orderPay);
        orderPay.setMessage(message);
        orderPay.setOrderHarvestExpend("0");
        if (payDate.length() == 8) {
            orderPay.setSettleDate(DateUtils.parseDate(payDate, "yyyyMMdd"));
        } else {
            orderPay.setSettleDate(DateUtils.parseDate(payDate, "yyyyMMddHHmmss"));
        }
        orderPay.setPayLogId(organization_payNo);//订单ID
        orderPay.setPayNo(payNo);//一级支付流水
        orderPay.setPayState(status);//接口返回的枚举值
        orderPay.setSynDesc(organization_result_desc);
        orderPay.setPayChannel(result_Pay_Type);//AP,WX,CM,UN(支付宝，微信，和包，银联)
        orderPay.setReturnCode(returnCode);
        orderPay.setType(type);
        orderPay.setVersion(version);
        TfOrderSub orderSubParam = new TfOrderSub();
        orderSubParam.setOrderId(orderId);
        //同步订单 告诉BOSS支付成功需要实际办理业务 支付任务完成时流程中调用OrderPayProcess继承的方法里调用订单同步的BOSS接口

        if ("0000".equals(returnCode) && "SUCCESS".equals(status)) {
            OrderProcessParam param = new OrderProcessParam();
            //orderId对应completeByOrderId的方法
            param.setBusinessId(String.valueOf(orderId));
            param.setOrderStatusId(OrderConstant.STATUS_PAY);
            param.setAct(1);
            param.put(Variables.ORDER_PAY, orderPay);
            orderService.completeByOrderId(param);//订单流转
        } else {
            OrderProcessParam param = new OrderProcessParam();
            param.setBusinessId(String.valueOf(orderId));
            param.setOrderStatusId(OrderConstant.STATUS_PAY);
            param.setAct(0);
            param.put(Variables.ORDER_PAY, orderPay);
            orderService.completeByOrderId(param);//订单流转
        }

    }
}

