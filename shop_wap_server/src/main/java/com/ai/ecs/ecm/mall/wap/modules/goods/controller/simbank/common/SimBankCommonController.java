package com.ai.ecs.ecm.mall.wap.modules.goods.controller.simbank.common;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.controller.simbank.BankCommonService;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.OrderPayService;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.recharge.service.RechargeService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSend4AllChanCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.ecsite.modules.bank.entity.CheckPsptCondition;
import com.ai.ecs.ecsite.modules.bank.service.BankInterfaceService;
import com.ai.ecs.goods.api.IBankFamilyService;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfBankFamilyPlan;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.order.OrderBankDepositService;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.utils.LogUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/5/25.
 * 银行相关公共流程
 */
@Controller
@RequestMapping("bankCommon")
public class SimBankCommonController extends BaseController {

    @Value("${afterOrderPayUrl}")
    private String afterOrderPayUrl;

    @Autowired
    private IBankFamilyService bankFamilyService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private BasicInfoQryModifyService basicInfoQryModifyService;

    @Autowired
    private IRecmdMainService recmdMainService;

    @Autowired
    private BankCommonService bankCommonService;

    @Autowired
    private OrderBankDepositService orderBankDepositService;

    @Autowired
    private BankInterfaceService bankInterfaceService;

    @Autowired
    private IPlansService plansService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private IOrderService orderService;

    private static final String ORDER_SUB_NO = "orderSubNo";

    private static final String SIM_SMS_CACHE_KEY = "SIM_PUFA_SMS_VERI_CODE";
    /**
     * 短信验证码有效时间 两分钟有效
     */
    private static final int SIM_SMS_TIME = 2 * 60;

    private static final String TONG_NET = "1";

    private static final String YI_NET = "2";

    private static final String BANK_MOBILE = "mobile";

    private static final String PLAN_NAME_PARAMETER = "planName";


    /**
     * 银行订单类型
     */
    private static final  Map<String,String>  CALLBANK_URL  = new HashMap<>();

    static {
        CALLBANK_URL. put("SPDB","/spdbank/toPayResult");
        CALLBANK_URL. put("CCB","/ccbank/toPayResult");
    }

    @ResponseBody
    @RequestMapping("checkNum")
    public ResponseBean checkQualification(TfOrderBankDeposit deposit, String bankType) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        List<TfOrderBankDeposit> result = orderBankDepositService.queryOrderBankDepositByIdNo(deposit);
        if (!result.isEmpty()) {
            TfOrderBankDeposit orderDeposit = result.get(0);
            TfOrderSub orderParam = new TfOrderSub();
            orderParam.setOrderSubId(orderDeposit.getOrderSubId());
            TfOrderSub tfOrderSub = orderQueryService.queryBaseOrder(orderParam);
            return checkExistOrder(tfOrderSub, orderDeposit, responseBean);
        }
        try {
            int checkResult = checkHNNum(deposit.getPsptId(), deposit.getSerialNumber(), bankType);
            if (checkResult == 3) {
                //名下没有湖南移动的号码,从配置文件中取要跳转的在线售卡的页面
                Map<String, String> map = new HashMap<>(16);
                map.put("destUrl", bankCommonService.getBankRecmdLink(bankType));
                responseBean.addSuccess("-2", "您的名下没有湖南移动的号码!", map);
                return responseBean;
            } else if (checkResult == 4) {
                //输入的号码不是湖南移动的号码
                responseBean.addSuccess("-3", "请您输入您的湖南移动的号码进行办理!", null);
                return responseBean;
            } else if (checkResult == 0) {
                responseBean.addSuccess("-4", "接口调用失败,请稍后再试", null);
                return responseBean;
            }
            return bankCommonService.newOrder(bankType, deposit, checkResult);
        } catch (Exception e) {
            logger.error("检查号码出错",e);
            responseBean.addSuccess("-4", e.getMessage(), null);
            return responseBean;
        }
    }

    @ResponseBody
    @RequestMapping("smsSend")
    public ResponseBean<Map<String, Object>> sendSms(HttpServletRequest request) throws Exception {
        ResponseBean<Map<String, Object>> responseBean = new ResponseBean<>();
        SmsSend4AllChanCondition condition = new SmsSend4AllChanCondition();
        String phone = "";
        phone = request.getParameter(BANK_MOBILE).trim();
        Session session = UserUtils.getSession();
        String phoneSess = (String)session.getAttribute(phone);
        String codeLength = request.getParameter("codeLength");
        if(phoneSess == null){
            responseBean.addError("-1", "会话超时!");
        }
        if (StringUtils.isBlank(phone) || !phone.equals(phoneSess)) {
            responseBean.addError("-1", "手机号码不正确!");
        }
        condition.setSERIAL_NUMBER(phone);
        condition.setCodeLength(Integer.valueOf(codeLength));
        condition.setCacheKey(SIM_SMS_CACHE_KEY + phone);
        condition.setCacheSeconds(SIM_SMS_TIME);
        condition.setNOTICE_CONTENT("尊敬的用户，您的短信随机验证码是:{$}。中国移动不会以任何方式向您索取该密码,请勿告知他人");
        //基本类型new对象初始化为0,不显式设置输入错误的次数就是0(也即不允许输入错误,输入错误就需要重新发送)
        condition.setErrorTimes(3);
        Map<String, Object> map = smsSendService.sendSms4AllChan(condition);
        responseBean.addSuccess(map);
        return responseBean;
    }


    @ResponseBody
    @RequestMapping("validateCaptcha")
    public ResponseBean validateCaptcha(HttpServletRequest request) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        String captchaCode = request.getParameter("captchaCode");
        if (JedisClusterUtils.get(request.getSession().getId().toString() + "VERIFY_CODE") == null) {
            responseBean.addError("-1", "验证码失效,请重新获取!");
            return responseBean;
        } else {
            String captcha = JedisClusterUtils.get(request.getSession().getId().toString() + "VERIFY_CODE");
            if (!captchaCode.equalsIgnoreCase(captcha)) {
                responseBean.addError("-1", "验证码错误!");
                return responseBean;
            }
        }
        return responseBean;
    }

    @ResponseBody
    @RequestMapping("confirmOffer")
    public ResponseBean confirmOffer(HttpServletRequest request) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        SmsSend4AllChanCondition condition = new SmsSend4AllChanCondition();
        String phone = request.getParameter(BANK_MOBILE).trim();
        String smsCode = request.getParameter("smsCode");
        String orderSubNo = request.getParameter(ORDER_SUB_NO);
        condition.setSERIAL_NUMBER(phone);
        condition.setCodeValue(smsCode);
        condition.setCacheKey(SIM_SMS_CACHE_KEY + phone);
        condition.setCacheSeconds(SIM_SMS_TIME);
        Map<String, Object> smsResult = smsSendService.CheckSms4AllChan(condition);
        if ("-1".equals(smsResult.get("X_RESULTCODE").toString())) {
            responseBean.addError("-1", smsResult.get("message").toString());
            return responseBean;
        }
        String bankPlanId = request.getParameter("bankPlanId");
        TfBankFamilyPlan plan = bankFamilyService.selectById(Short.valueOf(bankPlanId));
        TfOrderSub orderParam = new TfOrderSub();
        orderParam.setOrderSubNo(orderSubNo);
        TfOrderSub tfOrderSub = orderQueryService.queryComplexOrder(orderParam);
        tfOrderSub.setOrderPayAmount(plan.getContractPrice() * 100);//更新订单的支付价格
        tfOrderSub.setOrderSubPayAmount(plan.getContractPrice() * 100);
        Map<String, Object> map = new HashMap<>();
        //用于展示
        map.put("payAmount", tfOrderSub.getOrderSubPayAmount());
        responseBean.addSuccess(map);
        // SpdbPlansTransactProcess.java中更新订单状态
        try {
            bankCommonService.completeOrder(tfOrderSub, 1, OrderConstant.STATUS_SPDB_PLANS_BL);
        } catch (Exception e) {
            logger.error("completeOrder:", e);
            LogUtils.printKaError(CommonParams.KA_LOG, "completeOrder-订单流转异常:", e);
            responseBean.addError("-1", e.getMessage());
        }
        //观察任务是否完成 浦发完成后跳到冻结 建行完成后跳到支付
        TfOrderSub orderSubAfter = orderQueryService.queryBaseOrder(orderParam);
        if(OrderConstant.STATUS_SPDB_END == (orderSubAfter.getOrderStatusId()) || OrderConstant.STATUS_SPDB_PLANS_BL == orderSubAfter.getOrderStatusId()
                || OrderConstant.STATUS_BANK_FAIL == orderSubAfter.getOrderStatusId()){
            responseBean.addError("-1",orderSubAfter.getExceptionCause());
        }else{
            responseBean.addSuccess();
        }
        return responseBean;
    }


    /**
     * 生成订单,发送请求
     *
     * @param request
     * @param orderNos
     * @return
     * @throws Exception
     */
    @RequestMapping("/payOrder")
    @ResponseBody
    public ResponseBean payOrder(HttpServletRequest request, String orderNos,String bankType) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        String radioCodeType = request.getParameter("radiocode");
        String payPlatform = CommonParams.PAY_ENUM_PAYPLATFORM.get(radioCodeType);
        if (payPlatform == null) {
            responseBean.addError("-1", "支付平台选择错误");
            return responseBean;
        }
        try {
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
            String callbackUrl = basePath + CALLBANK_URL.get(bankType);
            String notifyUrl = afterOrderPayUrl +"/bankCommon/afterPayOrder";
            responseBean = orderPayService.payOrder(orderNos, callbackUrl, notifyUrl, payPlatform);
        } catch (Exception e) {
            logger.error("订单支付异常：", e);
            throw new Exception("订单支付异常：" + e.getMessage());
        }
        return responseBean;
    }

    /**
     * 回调参数包含
     * 返回举例:
     * returnCode = 0000 status = SUCCESS 支付成功 message = SUCCESS
     * type = SHGWDirectPayToPayOrg
     * org_code = ACCOUNT_SHIPOS 支付机构
     * organization_payNo = payLogId
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping("/afterPayOrder")
    public void afterPayOrder(HttpServletRequest request, String returnCode, String status, String message, String merchantId,
                              String payDate, String type, String payNo, String organization_payNo,
                              String org_code, String accountDate, Long orderId, Long amount, String version,
    String organization_result_desc,String result_Pay_Type) throws Exception {
        logger.info("支付异步回调参数[{}]", request.getParameterMap());
        orderPayService.afterPayOrder(request, returnCode, status, message, merchantId,
                payDate, type, payNo, organization_payNo,
                org_code, orderId, version, organization_result_desc, result_Pay_Type);
    }

    @ResponseBody
    @RequestMapping("currentTask")
    public ResponseBean currentTask(HttpServletRequest request, TfOrderSub orderParam) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderParam);
        if (OrderConstant.STATUS_PAY == orderSub.getOrderStatusId()) {
            //异步回调失败,查询支付状态
            logger.info("查询支付平台订单支付状态，orderSubId：" + orderParam.getOrderSubId());
            TfOrderPay payParam = new TfOrderPay();
            payParam.setOrderId(orderSub.getOrderId());
            try {
                TfOrderPay orderPay = orderService.checkPayFromBoss(orderSub);
                if (orderPay != null && "SUCCESS".equals(orderPay.getPayState())) {
                    String payDate = DateUtils.formatDate(orderPay.getSettleDate(),"yyyyMMdd");
//                    orderPayService.afterPayOrderOperation( "0000", "SUCCESS", orderPay.getMessage(), orderPay.getMerchantId(),
//                            payDate, orderPay.getType(), orderPay.getPayNo(), null, orderPay.getOrgCode(), null, orderPay.getOrderId(),
//                            orderPay.getOrderPayAmount(), orderPay.getVersion());
                    responseBean.addError("-1", "活动正在办理中，请稍后查询!");
                } else {
                    //支付失败
                    Map<String, String> map = new HashMap<>();
                    map.put("orderSubNo", orderSub.getOrderSubNo());
                    orderService.updateOrderPay(orderPay);
                    responseBean.addSuccess("-2", "支付失败,请重新支付!", map);
                }
            } catch (Exception e) {
                responseBean.addError("-5", "订单处理失败!!");
                logger.error("失败", e);
            }
        } else if (OrderConstant.STATUS_ARCHIVE == orderSub.getOrderStatusId()) {
            //活动办理成功
            Map<String,String> map = new HashMap<>();
            String destUrl = DictUtil.getDictValue("","RETURN_PAGE_CCB","http://wap.hn.10086.cn/shop");
            String label= DictUtil.getDictLabel("","RETURN_PAGE_CCB","回到首页");
            map.put("destUrl",destUrl);
            map.put("label",label);
            responseBean.addSuccess("0",map);
        } else if(OrderConstant.STATUS_BANK_FAIL == orderSub.getOrderStatusId()){
            responseBean.addError("-4", "支付超时!!");
        }else{
            responseBean.addError("-5", "系统异常!!");
        }
        return responseBean;
    }


    private ResponseBean checkExistOrder(TfOrderSub tfOrderSub, TfOrderBankDeposit orderDeposit, ResponseBean responseBean) {
        int orderStatusId = tfOrderSub.getOrderStatusId();
        if (orderStatusId == OrderConstant.STATUS_SPDB_PLANS_SELECT) {
            //跳转到选套餐的页面
            String phoneNumber = orderDeposit.getSerialNumber();
            Map<String, String> map = new HashMap<>();
            map.put("type", orderDeposit.getPlanLevel());
            JedisClusterUtils.set("CHECK_RESULT" + phoneNumber, orderDeposit.getPlanLevel(), 3600);
            map.put(ORDER_SUB_NO, tfOrderSub.getOrderSubNo());
            map.put("phone", phoneNumber);
            responseBean.addSuccess("2", "您已有订单,去选套餐", map);
        } else if (orderStatusId == OrderConstant.STATUS_SPDB_PLANS_BL) {
            //跳转到套餐确认的页面
            Map<String, Object> map = new HashMap<>();
            map.put("planId", orderDeposit.getPlanId());
            map.put(PLAN_NAME_PARAMETER, orderDeposit.getPlanName());
            map.put("planSubsidy", orderDeposit.getPlanSubsidy());
            map.put(ORDER_SUB_NO, tfOrderSub.getOrderSubNo());
            responseBean.addSuccess("3", "您已有订单,去确认套餐", map);
        } else if (orderStatusId == OrderConstant.STATUS_PAY) {
            //去支付
            Map<String, String> map = new HashMap<>();
            map.put(ORDER_SUB_NO, tfOrderSub.getOrderSubNo());
            responseBean.addSuccess("4", "您已有订单,去支付", map);
        } else if (orderStatusId == OrderConstant.STATUS_SPDB_UNFREEZE) {
            //活动办理失败,需要解冻保证金
            Map<String, String> map = new HashMap<>();
            map.put("orderSubId", String.valueOf(tfOrderSub.getOrderSubId()));
            responseBean.addSuccess("8", "您的活动办理失败,去解冻保证金", map);

        } else if (orderStatusId == OrderConstant.STATUS_SPDB_DJ) {
            //跳转冻结页面
            Map<String, String> map = new HashMap<>();
            map.put(ORDER_SUB_NO, tfOrderSub.getOrderSubNo());
            responseBean.addSuccess("5", "您已有订单,去冻结", map);
        } else if (orderStatusId == 12) {
            //该号码已经办理成功
            responseBean.addSuccess("6", "您的号码已经成功办理过该活动!", null);
        } else if (orderStatusId == OrderConstant.STATUS_SPDB_FAIL || orderStatusId == OrderConstant.STATUS_BANK_FAIL ) {
            responseBean.addError("9",  tfOrderSub.getExceptionCause());
        }else  {
            responseBean.addError("-1", "您存在未完成的订单!");
        }
        return responseBean;
    }

    /**
     * 3 非湖南移动号码 纯异网客户
     * 2 异网客户,办理业务 优惠等级高 type = 2
     * 1 六个月以上的同网客户 优惠等级低 type = 1
     * 4 输入的号码不是湖南移动的号码,不能办理优惠活动
     *
     * @param idNo
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    private int checkHNNum(String idNo, String phoneNumber, String bankType) throws Exception {

        TfOrderDetailSim simParam = new TfOrderDetailSim();
        simParam.setPsptId(idNo);
        simParam.setPhone(phoneNumber);
        int checkResult;
        Map map = new HashMap();
        String simConfId = DictUtil.getDictValue("BANK_RECMD",bankType,"1001");
        map.put("confId", simConfId);
        map.put("phone", phoneNumber);
        map.put("recmdPhone", CommonParams.RECMD_PHONE.get(bankType));
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(simConfId);
        TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
        map.put("rcdConfId", resultPlan.getRcdConfId());
        //第一步:看号码是不是电渠打标的号码(六个月内BankCommonService在网厅下单的号码)
        if (recmdMainService.checkSixMonthRecmdPhone(map) > 0) {
            checkResult = 2;//异网客户
        } else {
            //查询输入的号码是否是湖南移动的号码,是,则是同网客户,办理优惠级别低的活动
            BasicInfoCondition biCond = new BasicInfoCondition();
            biCond.setSerialNumber(phoneNumber);
            biCond.setxGetMode("0");
            //默认值没有权限，所以显式赋值
            biCond.setStaffId("ITFWAPNN");
            biCond.setTradeDepartPassword("225071");//225071
            Map<String, Object> userInfo = null;
            logger.info("====用户资料条件===>{}",JSONObject.toJSONString(biCond, SerializerFeature.WriteMapNullValue));
            userInfo = basicInfoQryModifyService.queryUserBasicInfo(biCond);
            CheckPsptCondition condition = new CheckPsptCondition();
            condition.setSerialNumber(phoneNumber);
            condition.setPsptId(idNo);
            if (MapUtils.isNotEmpty(userInfo) && HNanConstant.SUCCESS.equals(userInfo.get("respCode"))){
                logger.info("====用户资料返回===>{}",JSONObject.toJSONString(userInfo, SerializerFeature.WriteMapNullValue));
                JSONObject phoneAttributionJSON = JSONObject.parseArray(userInfo.get("result").toString()).getJSONObject(0);
                String phoneNumberResultcode = phoneAttributionJSON.getString("X_RESULTCODE");
                //不是湖南移动的号码
                if (!"0".equals(phoneNumberResultcode)) {
                    //1移动 2异网(纯异网)
                    checkResult = checkHunan(condition);
                } else {
                    //是湖南移动的号码
                    checkResult = 1;
                }
            }else{
               //获得三户信息提示没有路由信息,说明不是输入的不是湖南移动的号码
                checkResult = checkHunan(condition);
            }
        }
        JedisClusterUtils.set("CHECK_RESULT" + phoneNumber, String.valueOf(checkResult), 3600);
        return checkResult;
    }


    /**
     * 测试环境 42端口7001;192.192.1.164:7001 对应现场140环境
     * 测试工号:ITFWC000 ai1234
     * 接口返回 : 1 移动 2 异网
     * @param condition
     * @return
     * @throws Exception
     */
    private int checkHunan(CheckPsptCondition condition) throws Exception{
        condition.setStaffId("ITFWPMAL");
        condition.setTradeDepartPassword("ai1234");
        String bossCheck = bankInterfaceService.checkPspt(condition);
        if (TONG_NET.equals(bossCheck)) {
            //不是湖南移动的号码,但有湖南移动的号码,提示输入客户名下的湖南移动的号码
             return 4;
        } else if (YI_NET.equals(bossCheck)) {
             return 3;
        } else {
            //接口抛出异常(eg.外围接入系统员工权限校验失败!)
            throw new RuntimeException(bossCheck);
        }

    }

}
