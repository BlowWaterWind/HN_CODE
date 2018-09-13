package com.ai.ecs.ecm.mall.wap.modules.goods.controller.simbank;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.config.Global;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.OrderPayService;
import com.ai.ecs.ecm.mall.wap.platform.utils.ThreeDes;
import com.ai.ecs.order.OrderBankDepositService;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.ecs.order.payquery.FreezeService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogEnterMenthod;


/**
 * Created by cc on 2018/3/13.
 * 浦发移动合作需求
 */

@Controller
@RequestMapping("/spdbank")
public class SimSpdbBuyController extends BaseController {

    /**
     * 冻结超时时间 1小时
     */
    private static final int DEPOSIT_DURATION = 60 * 60 * 1000;

    private static final String ORDER_SUB_NO = "orderSubNo";

    private static final String PUFA_BAIL_MONEY = "1000";

    private static final String PUFA_BANK = "SPDB";

    @Value("${afterOrderPayUrl}")
    private String afterOrderPayUrl;

    @Value("${orderPayUrl}")
    private String orderPayUrl;

    @Value("${orderNotify}")
    private String orderNotify;

    @Value("${simConfId}")
    private String simConfId;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderQueryService orderQueryService;


    @Autowired
    private OrderBankDepositService orderBankDepositService;


    @Autowired
    private FreezeService freezeService;

    @Autowired
    private BankCommonService bankCommonService;

    @RequestMapping("toCheckNum")
    public String toCheckNum() {
        return "web/goods/simspdb/checkNum_01";
    }

    /**
     * 防止客户自行修改可以办理的等级
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("toChooseOffer")
    public String toChooseOffer(HttpServletRequest request, Model model) {
        bankCommonService.toChooseOffer(request, model, PUFA_BANK);
        return "web/goods/simspdb/chooseOffer_02";
    }


    /**
     * 跳转到填写短信验证码的页面
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("toConfirmOffer")
    public String toConfirmOffer(HttpServletRequest request, Model model) throws Exception {
        bankCommonService.toConfirmOffer(request, model);
        return "web/goods/simspdb/validatePay_03";
    }

    @RequestMapping("/toPay")
    public String toPay(HttpServletRequest request, Model model) {
        String orderSubNo = request.getParameter(ORDER_SUB_NO);
        String fail = request.getParameter("fail");
        bankCommonService.toPay(orderSubNo, fail, model);
        return "web/goods/simspdb/toPay_05";
    }


    /**
     * 支付同步回调接口
     * 成功跳冻结保证金页面
     *
     * @param model
     * @param returnCode
     * @param chargeflowId
     * @return
     */
    @RequestMapping("toPayResult")
    public String toPayResult(Model model, String returnCode, Long chargeflowId) throws Exception {
        TfOrderSub orderSubParam = new TfOrderSub();
        orderSubParam.setOrderId(chargeflowId);
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderSubParam);
        TfOrderBankDeposit deposit = orderSub.getOrderSpdBankDeposit();
        model.addAttribute("deposit", deposit);
        model.addAttribute("orderSub", orderSub);
        if ("0000".equals(returnCode)) {
            model.addAttribute("minute", "00");
            model.addAttribute("second", "00");
            //冻结保证金页面 第一次进入retry=false
            model.addAttribute("fail", false);
            return "web/goods/simspdb/afterPay_06";
        } else {
            //支付失败跳转到原支付页面
            return "redirect:" + "toPay?orderSubNo=" + orderSub.getOrderSubNo() + "&fail=fail";
        }
    }


    @RequestMapping("actiStatus")
    @ResponseBody
    public ResponseBean actiStatus(TfOrderSub orderParam) {
        ResponseBean responseBean = new ResponseBean();
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderParam);
        if (OrderConstant.STATUS_SPDB_DJ == orderSub.getOrderStatusId()) {
            responseBean.addSuccess();
        } else {
            responseBean.addError("-1", "" + orderSub.getExceptionCause());
        }
        return responseBean;
    }

    @ResponseBody
    @RequestMapping("/freezeCash")
    public ResponseBean freezeCash(HttpServletRequest request, TfOrderSub orderParam) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderParam);
        if (orderSub == null) {
            responseBean.addError("-1", "没有相应的订单!");
        }
        TfOrderBankDeposit deposit = orderSub.getOrderSpdBankDeposit();
        if (orderSub.getOrderSpdBankDeposit() == null) {
            responseBean.addError("-1", "系统异常!");
        }
        if (OrderConstant.STATUS_SPDB_DJ != orderSub.getOrderStatusId()) {
            responseBean.addError("-1", orderSub.getExceptionCause());
        }
        String paramUrl = sendHttp(request, orderSub, deposit);
        responseBean.addSuccess(paramUrl);
        return responseBean;
    }

    /**
     * 已有订单重新跳转到冻结页面
     * 或者从短信验证之后跳转到这个页面
     *
     * @param orderParam
     * @param model
     * @return
     */
    @RequestMapping("/toFreeze")
    public String toFreeze(HttpServletRequest request, TfOrderSub orderParam, Model model) {
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderParam);
        TfOrderBankDeposit deposit = orderSub.getOrderSpdBankDeposit();
        model.addAttribute("deposit", deposit);
        model.addAttribute("orderSub", orderSub);
        model.addAttribute("success", false);
        model.addAttribute("unFreeze", false);
        Date date = orderService.getTaskCreateDate(orderSub.getOrderSubNo(), "spdbBlockProcess_74");
        if (date == null) {
            throw new RuntimeException("当前任务不在保证金冻结节点");
        }
        if ("POST".equals(request.getMethod())) {
            confirmDepositInfo(deposit);
        }
        long timeInterval = System.currentTimeMillis() - date.getTime();
        //
        long timeLeft = (DEPOSIT_DURATION - timeInterval) / 1000;
        if (timeLeft > 0) {
            long minute = timeLeft / 60;
            long second = timeLeft % 60;
            model.addAttribute("timeout", false);
            model.addAttribute("minute", minute);
            model.addAttribute("second", second);
        } else {
            model.addAttribute("timeout", true);
            model.addAttribute("minute", "00");
            model.addAttribute("second", "00");
        }
        if (deposit.getTryTimes() > 1) {
            model.addAttribute("retry", true);
        } else {
            model.addAttribute("retry", false);
        }
        return "web/goods/simspdb/freezeMoney_04";
    }


    /**
     * 1. 查询冻结保证金剩余时间
     * 2. ajax查询是否冻结成功
     * 从历史任务中查询冻结保证金任务的创建时间 > 保留时间,判定保证金冻结失败
     * 从历史任务中查询冻结保证金任务的创建时间 < 保留时间,继续倒计时
     * retry = true 发起查询冻结状态
     * unFreeze = true 弹出弹框
     * @param orderParam
     * @param model
     * @return
     */
    @RequestMapping("/afterFreeze")
    public String afterFreeze(TfOrderSub orderParam, Model model) {
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderParam);
        TfOrderBankDeposit deposit = orderSub.getOrderSpdBankDeposit();
        String taskDefKey = orderService.getCurrentTaskDef(orderSub.getOrderSubNo());
        model.addAttribute("success", false);
        model.addAttribute("unFreeze", false);
        if (deposit.getTryTimes() > 0) {
            model.addAttribute("retry", true);
        } else {
            model.addAttribute("retry", false);
        }
        model.addAttribute("timeout", false);
        model.addAttribute("minute", "00");
        model.addAttribute("second", "00");
        if ("spdbPayProcess_1".equals(taskDefKey)) {
            //任务完成,进入支付任务
            model.addAttribute("success", true);
        } else if ("spdbBlockProcess_74".equals(taskDefKey)) {
            //任务未完成
            Date date = orderService.getTaskCreateDate(orderSub.getOrderSubNo(), "spdbBlockProcess_74");
            long timeInterval = System.currentTimeMillis() - date.getTime();
            long timeLeft = (DEPOSIT_DURATION - timeInterval) / 1000;
            if (timeLeft > 0) {
                long minute = timeLeft / 60;
                long second = timeLeft % 60;
                model.addAttribute("minute", minute);
                model.addAttribute("second", second);
            } else {
                //任务超时
                model.addAttribute("timeout", true);
            }
            model.addAttribute("retry", true);
        } else if ("spdbBlockProcess_77".equals(taskDefKey)) {
            //任务办理失败,进入解冻保证金任务
            model.addAttribute("reason",orderSub.getExceptionCause());
            model.addAttribute("unFreeze", true);

        }
        model.addAttribute("deposit", deposit);
        model.addAttribute("orderSub", orderSub);
        return "web/goods/simspdb/freezeMoney_04";
    }

    @ResponseBody
    @RequestMapping("checkFreezeStatus")
    public ResponseBean checkFreezeStatus(String depositId) {
        ResponseBean responseBean = new ResponseBean();
        if ("".equals(depositId)) {
            responseBean.addError("-1", "不存在此订单号");
            return responseBean;
        }
        Long depositNum = Long.parseLong(depositId);
        TfOrderBankDeposit depositParam = orderBankDepositService.selectByOrderSubId(depositNum);
        if (depositParam.getBailStatus() == null) {
            //冻结未回调
            try {
                //查询接口
                TfOrderBankDeposit deposit = freezeService.queryFreeze(depositParam);
                checkFreezzeStatus(responseBean, deposit);
            } catch (Exception e) {
                responseBean.addError("-3", e.getMessage());

            }
        } else {
            //冻结已经回调,不需要查询浦发接口
            checkFreezzeStatus(responseBean, depositParam);
        }
        return responseBean;
    }

    private ResponseBean checkFreezzeStatus(ResponseBean responseBean, TfOrderBankDeposit deposit) {
        //冻结已经回调,不需要查询浦发接口
        if ("0".equals(deposit.getBailStatus())) {
            Map map = new HashMap<>();
            map.put("orderSubNo", deposit.getPledgeNo().substring(3));
            responseBean.addSuccess("恭喜您,您的活动已经办理成功!");
        } else{
            responseBean.addError("-1", "订单冻结失败,请重新发起冻结");
        }
        return responseBean;
    }

    @RequestMapping("unFreeze")
    @ResponseBody
    public ResponseBean unFreeze(String orderSubNo) {
        ResponseBean responseBean = new ResponseBean();
        String taskDefKey = orderService.getCurrentTaskDef(orderSubNo);
        if (!"spdbBlockProcess_77".equals(taskDefKey)) {
            responseBean.addError("-1", "您当前的订单不在解冻保证金的节点,请确认后再试!");
        }
        TfOrderSub orderParam = new TfOrderSub();
        orderParam.setOrderSubNo(orderSubNo);
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderParam);
        try {
            JSONObject map = freezeService.upgradeBail(orderSub.getOrderSpdBankDeposit());
            if ("AAAAAAA".equals(map.getString("returnCode"))) {
                responseBean.addSuccess("解冻成功,您可以尝试中国移动其他业务!");
            } else {
                responseBean.addError("-1", map.getString("returnMsg"));
            }
        } catch (Exception e) {
            logger.error("解冻失败", e);
            responseBean.addError("-1", e.getMessage());
        }
        return responseBean;
    }

    private String sendHttp(HttpServletRequest request, TfOrderSub orderSub, TfOrderBankDeposit deposit)
            throws Exception {
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String parameter = "ValidTime=" + System.currentTimeMillis()
                + "&OrderId=" + deposit.getPledgeNo()
                + "&BuyerInfo=" + deposit.getCustName()
                + "&BuyModel=" + deposit.getPlanName()
                + "&FreeZeTime=" + deposit.getFreeZeTime()
                + "&AmountFrozen=" + deposit.getBailMoney()
                + "&CtfId=" + deposit.getPsptId()
                + "&BeginDate=" + deposit.getDongjieDate()
                + "&EndDate=" + deposit.getEndDate()
                + "&FreezeDate=" + deposit.getFreezeDate()
                + "&url=" + basePath + "/spdbank/afterFreeze?orderSubId=" + orderSub.getOrderSubId();
        String parameterString = ThreeDes.desEncryptAndUrlEncode(parameter, ThreeDes.DES_PRIVATE_KEY);
        logger.info("des加密前参数{}", parameter);
        String encrpt = URLDecoder.decode(parameterString, "UTF-8");//传入时用
        String signData = ThreeDes.sign(encrpt.getBytes(), ThreeDes.MD5_PRIVATE_KEY);
        //http://etest.spdb.com.cn/wap/H5HouseGreet.hl
        String url = "https://wap.spdb.com.cn/wap/H5HouseGreet.hl";
        //String url = "http://etest.spdb.com.cn/wap/H5HouseGreet.hl";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("ParameterString", parameterString);
        paramMap.put("SignData", signData);
        String requestParam = url + "?H5Channel=109052" + "&SignData=" + signData + "&ParameterString=" + parameterString;
        logger.info("--------请求url-----{}", requestParam);
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", this.getClass().getName(),
                "sendHttpspdb", parameterString+"$"+ThreeDes.DES_PRIVATE_KEY+"$"+ThreeDes.MD5_PRIVATE_KEY, "发送请求", request);
        return requestParam;
    }

    private void confirmDepositInfo(TfOrderBankDeposit deposit) {
        //金额不是分,不管什么套餐都是冻结1000元
        deposit.setBailMoney(PUFA_BAIL_MONEY);
        //3-购机保证金
        deposit.setBailLockType("3");
        deposit.setIdType("1");
        String beginDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, +1);
        String endDate = DateFormatUtils.format(calendar.getTime(), "yyyyMMdd");
        calendar.add(Calendar.MONTH, +1);
        String freezeDate = DateFormatUtils.format(calendar.getTime(), "yyyyMMdd");
        /**
         * 代码不会敲疯狂写注释
         * FreeZeTime 枚举值 {103:3个月,106:6个月,001:1年,002:2年,003:3年,005:5年}
         * BeginDate 起始日期
         * EndDate 结束起始
         * FreezeDate 冻结到期日
         * 浦发银行:保证金开立时,BeginDate~EndDate是定期存款的时间,到了时间还不能取款,要看FreezeDate的时间才能取款
         * 这个需求统一冻结1000元，冻结到期日是自定义的先向后延长一个月
         */
        deposit.setFreeZeTime("001");
        deposit.setDongjieDate(beginDate);
        deposit.setBeginDate(beginDate);
        deposit.setEndDate(endDate);
        deposit.setFreezeDate(freezeDate);
        deposit.setStaffId(Global.getConfig("staffId"));
        deposit.setTradeDepartPassword(Global.getConfig("tradeDepartPassword"));
        orderBankDepositService.updateDeposit(deposit);
    }


    /**
     * 当支付异步回调很慢,查询得到支付成功,开启新线程完成支付节点任务
     */
    public class CompleteTask extends Thread {
        private OrderProcessParam orderProcessParam;

        public CompleteTask(String name, OrderProcessParam orderProcessParam) {
            super(name);
            this.orderProcessParam = orderProcessParam;
        }

        @Override
        public void run() {
            orderService.completeByOrderId(orderProcessParam);//订单流转
        }
    }


}
