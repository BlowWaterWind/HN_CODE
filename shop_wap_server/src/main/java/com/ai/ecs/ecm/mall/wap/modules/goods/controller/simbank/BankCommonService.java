package com.ai.ecs.ecm.mall.wap.modules.goods.controller.simbank;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.goods.api.IBankFamilyService;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfBankFamilyPlan;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.ecs.utils.LogUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cc on 2018/5/29.
 * 放浦发和建行在进入页面前的操作
 */
@Service
public class BankCommonService extends BaseController{

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IBankFamilyService bankFamilyService;

    @Autowired
    private IPlansService plansService;

    @Autowired
    private IRecmdMainService recmdMainService;

    @Autowired
    @Value("${recmdSimQrcodeDomain}")
    private String recmdSimQrcodeDomain;

    @Autowired
    private SmsSendService smsSendService;

    /**
     * 支付超时时间 15分钟
     */
    private static final int PAY_DURATION = 60 * 15 * 1000;

    private static final String ORDER_SUB_NO = "orderSubNo";

    private static final String PLAN_NAME_PARAMETER = "planName";
    private static final String PHONE_MOBILE = "mobile";


    /**
     * @param request 请求
     * @param model 视图
     * @param bankType 银行类型
     * @return
     */
    public void toChooseOffer(HttpServletRequest request, Model model, String bankType){
        //异网客户 type= 2 同网客户 type = 1
        String planType = request.getParameter("type");
        String phoneNumber = request.getParameter("phone");
        String typeRedis = JedisClusterUtils.get("CHECK_RESULT"+phoneNumber);
        if(!planType.equals(typeRedis)){
            throw new RuntimeException("套餐办理等级出错,请确认后再试！");
        }
        String orderSubNo = request.getParameter(ORDER_SUB_NO);
        TfBankFamilyPlan cond = new TfBankFamilyPlan();
        cond.setBankType(bankType);
        cond.setPlanType(planType);
        List<TfBankFamilyPlan> familyPlans = bankFamilyService.selectAllPlans(cond);
        model.addAttribute("plans", familyPlans);
        model.addAttribute(ORDER_SUB_NO, orderSubNo);
    }

    public void toConfirmOffer(HttpServletRequest request,Model model) throws Exception{
        Long planId = Long.parseLong(request.getParameter("planId"));
        String planType = request.getParameter("type");
        String id = request.getParameter("id");
        String orderSubNo = request.getParameter(ORDER_SUB_NO);
        TfOrderSub param = new TfOrderSub();
        param.setOrderSubNo(orderSubNo);
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(param);
        if ("GET".equals(request.getMethod())) {
            String planName = URLDecoder.decode(request.getParameter(PLAN_NAME_PARAMETER), "utf-8");
            request.setAttribute(PLAN_NAME_PARAMETER, planName);
            model.addAttribute("bankPlanId",orderSub.getOrderSpdBankDeposit().getBankPlanId());
        }else{
            model.addAttribute("bankPlanId",id);
        }
        Session session = UserUtils.getSession();
        session.setAttribute(orderSub.getOrderSpdBankDeposit().getSerialNumber(),orderSub.getOrderSpdBankDeposit().getSerialNumber());
        model.addAttribute(ORDER_SUB_NO, orderSubNo);
        model.addAttribute(PHONE_MOBILE, orderSub.getOrderSpdBankDeposit().getSerialNumber());
        //选择套餐任务完成,SpdbPlansChooseProcess.java更新套餐信息和订单信息
        if (!"GET".equals(request.getMethod())) {
            try {
                TfOrderSub orderSubUpdate = toConfirmOffer(planId,orderSub,planType,id);
                completeOrder(orderSubUpdate, 1, OrderConstant.STATUS_SPDB_PLANS_SELECT);
            } catch (Exception e) {
                logger.error("completeOrder:", e);
                LogUtils.printKaError(CommonParams.KA_LOG, "completeOrder-订单流转异常:", e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }


    private TfOrderSub toConfirmOffer(Long planId,TfOrderSub orderSub,String planType,String id) throws Exception{
        TfPlans planParam = new TfPlans();
        planParam.setPlanId(planId);
        List<TfPlans> plans = plansService.getPlanList(planParam);
        TfPlans plan = plans.get(0);
        TfBankFamilyPlan bankPlan = bankFamilyService.selectById(Short.valueOf(id));
        //订单支付金额,套餐优惠价格,单位是分
        TfOrderBankDeposit deposit = orderSub.getOrderSpdBankDeposit();
        deposit.setPlanLevel(planType);
        deposit.setPlanId(plan.getPlanId());
        deposit.setPlanName(bankPlan.getPlanName());//使用
        deposit.setPlanSubsidy(bankPlan.getSubsidyPrice().toString());
        deposit.setContractPrice(bankPlan.getContractPrice() * 100);
        deposit.setBankPlanId(id);
        //在SpdbPlansTransactProcess保存新的deposit信息
        deposit.setProductId(bankPlan.getProductId());
        deposit.setPackageId(bankPlan.getPackageId());
        deposit.setDiscountId(bankPlan.getDiscountId());
        orderSub.setOrderSpdBankDeposit(deposit);
        return orderSub;
    }


    /**
     * 根据银行类型生成推荐链接
     * @parapm bankType
     * @return
     */
    public String getBankRecmdLink(String bankType){
        TfOrderRecmd orderRecmd = new TfOrderRecmd();
        orderRecmd.setRecmdPhone(CommonParams.RECMD_PHONE.get(bankType));
        String simConfId = DictUtil.getDictValue("BANK_RECMD",bankType,"1001");
        orderRecmd.setConfId(simConfId);
        List<TfOrderRecmd> recmds = recmdMainService.getPage(orderRecmd).getList();
        if(recmds.isEmpty()){
            //生成链接,进入在线售卡页面时推荐码保存在TfH5SimConf的recmdCode VO字段保存在页面,再进入确认订单的页面
            TfH5SimConf cond = new TfH5SimConf();
            cond.setConfId(simConfId);
            List<TfH5SimConf> lists = plansService.queryListCond(cond);
            if(lists.isEmpty()){
                throw new RuntimeException("配置号卡不存在!");
            }
            Long recmdId = recmdMainService.getIdWorkId();
            String recmdCode = Base64.encode((recmdId + "").getBytes());
            String simSellLink = recmdSimQrcodeDomain + "/simBuy/simH5OnlineToApply?confId=" + simConfId + "&recmdCode=" + recmdCode;
            orderRecmd.setRecmdPhone(CommonParams.RECMD_PHONE.get(bankType));
            orderRecmd.setRecmdId(recmdId);
            orderRecmd.setRecmdUserLink(simSellLink);
            orderRecmd.setConfId(simConfId);
            orderRecmd.setRcdConfId(lists.get(0).getRcdConfId());
            recmdMainService.saveOrderRecmd(orderRecmd);
            return simSellLink;
        }else{
            return recmds.get(0).getRecmdUserLink();
        }
    }


    public void toPay(String orderSubNo,String fail,Model model){
        if ("fail".equals(fail)) {
            model.addAttribute("fail", true);
        } else {
            model.addAttribute("fail", false);
        }
        TfOrderSub orderSubParam = new TfOrderSub();
        Date date = orderService.getTaskCreateDate(orderSubNo, "spdbPayProcess_1");
        if (date == null) {
            throw new RuntimeException("当前任务不在支付环节");
        }
        long timeInterval = System.currentTimeMillis() - date.getTime();
        long timeLeft = (PAY_DURATION - timeInterval) / 1000;
        if (timeLeft > 0) {
            long minute = timeLeft / 60;
            long second = timeLeft % 60;
            model.addAttribute("minute", minute);
            model.addAttribute("second", second);
        } else {
            model.addAttribute("minute", "00");
            model.addAttribute("second", "00");
        }
        orderSubParam.setOrderSubNo(orderSubNo);
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderSubParam);
        model.addAttribute("orderSub", orderSub);
        model.addAttribute("deposit", orderSub.getOrderSpdBankDeposit());
    }

    /**
     *
     * @param bankType
     * @param deposit
     * @return
     */
    public ResponseBean newOrder(String bankType, TfOrderBankDeposit deposit, int checkResult){
        ResponseBean responseBean = new ResponseBean();
        TfOrderSub plainOrder = new TfOrderSub();
        TfOrderUserRef userRef = new TfOrderUserRef();//== 用户信息
        userRef.setMemberId(123456890L);
        userRef.setMemberLogingName("银行套餐虚拟用户");
        userRef.setEparchyCode(deposit.getEparchyCode());
        TfOrderSub orderSub = confirmOrderSub(plainOrder,bankType);
        orderSub.setOrderUserRef(userRef);
        orderSub.setOrderTypeId(OrderConstant.TYPE_BANK_DEPOSIT);
        orderSub.setNeedFreeze(CommonParams.NEED_FREEZE.get(bankType));
        //用于支付成功后通知支付成功的入参
        orderSub.setPhoneNumber(deposit.getSerialNumber());
        deposit.setBankType(bankType);
        deposit.setPlanLevel(String.valueOf(checkResult));
        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        TfOrderSubDetail subDetail = comfirmSubDetailInfo(orderSubDetail, deposit.getSerialNumber(),bankType);
        orderSub.addOrderDetail(subDetail);
        orderSub.setOrderSpdBankDeposit(deposit);
        Map<String, Object> map = new HashMap<>();
        try {
            //==生成订单
            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
            TfOrderSub orderSubResult = orderSubList.get(0);
            map.put(ORDER_SUB_NO, orderSubResult.getOrderSubNo());
            map.put("phone",deposit.getSerialNumber());
            map.put("type", checkResult);
            printKaInfo("submitOrderH5OnlineSim订单生成成功：" + JSONObject.toJSONString(orderSubResult, SerializerFeature.WriteMapNullValue));
            responseBean.addSuccess(map);
        } catch (Exception e) {
            logger.error("submitOrderH5OnlineSim生成订单失败", e);
            responseBean.addError("-1", "系统异常,生成订单失败!");
            return responseBean;
        }
        return responseBean;
    }


    /**
     *
     * @param orderSubDetail
     * @param phoneNumber
     * @return
     */
    protected TfOrderSubDetail comfirmSubDetailInfo(TfOrderSubDetail orderSubDetail, String  phoneNumber,String bankType) {
        Long phone = Long.parseLong(phoneNumber);
        orderSubDetail.setRootCateId(2L);//产品类别
        orderSubDetail.setShopId(1L);
        orderSubDetail.setShopName("湖南省移动公司");
        orderSubDetail.setShopTypeId(6);
        orderSubDetail.setGoodsId(phone);//号卡订单：号码作为GoodsId
        if("SPDB".equals(bankType)){
            orderSubDetail.setGoodsName("浦发银行活动办理");// 商品信息
            orderSubDetail.setGoodsUrl("http://wap.hn.10086.cn/shop/spdbank/toCheckNum");
            orderSubDetail.setGoodsRemark("浦发银行活动办理");//备注
        }else{
            orderSubDetail.setGoodsName("建设银行活动办理");// 商品信息
            orderSubDetail.setGoodsUrl("http://wap.hn.10086.cn/shop/ccbank/toCheckNum");
            orderSubDetail.setGoodsRemark("建设银行活动办理");//备注
        }
        orderSubDetail.setGoodsSkuId(phone);//号卡订单：号码作为SKUID
        orderSubDetail.setGoodsSkuPrice(1L);//合约套餐的价格,暂定位1,在短信确认后更新TF_ORDER_SUB和TF_ORDER_SUB_DETAIL的值
        orderSubDetail.setGoodsSkuNum(1L);//每个订单只能一个号码
        orderSubDetail.setProdSkuId(phone);
        orderSubDetail.setGoodsFormat(phoneNumber);//号卡订单：号码作为商品规格
        return orderSubDetail;
    }

    protected TfOrderSub confirmOrderSub(TfOrderSub orderSub,String bankType) {
        //设置订单信息
        orderSub.setOrderSubRemark(bankType+"银行号卡优惠套餐办理");//用户备注
        orderSub.setShopId(100000002068L); //省公司
        orderSub.setShopTypeId(OrderConstant.SHOP_TYPE_PROVINCE);
        orderSub.setOrderChannelCode(CommonParams.CHANNEL_CODE);//渠道编码，WAP
        orderSub.setPayModeId(OrderConstant.PAY_WAY_ONLINE);//支付方式
        orderSub.setDeliveryModeId(OrderConstant.DELIVERY_NO);//配送方式
        return orderSub;
    }

    public void completeOrder(TfOrderSub orderSub, Integer act, Integer orderStatusId) throws Exception {
        OrderProcessParam processParam = new OrderProcessParam();
        orderSub.setProcessParam(null);
        processParam.putFlash(Variables.ORDER_SUB, orderSub);
        processParam.setAct(act);
        processParam.setOrderStatusId(orderStatusId);
        processParam.setBusinessId(orderSub.getOrderSubNo());
        orderService.completeByAdmin(processParam);
    }

    public void completeOrder(TfOrderSub orderSub, Integer act, Integer orderStatusId,OrderProcessParam param) throws Exception {
        orderSub.setProcessParam(null);
        param.putFlash(Variables.ORDER_SUB, orderSub);
        param.setAct(act);
        param.setOrderStatusId(orderStatusId);
        param.setBusinessId(orderSub.getOrderSubNo());
        orderService.completeByAdmin(param);
    }

    /**
     * 银行订单类型
     */
    private static final  Map<String,String>  BANK_CONTENT  = new HashMap<>();

    static {
        BANK_CONTENT. put("SPDB","尊敬的客户，您已经办理浦发银行%s元和家庭套餐%s元中国移动专属合约活动，该活动办理后次月生效" +
                "有效期12个月。该活动一次性缴纳%s元，分12个月返还。另每月赠送%s元，共计赠送12个月。");
        BANK_CONTENT. put("CCB","尊敬的客户，您已经办理建设银行%s元和家庭套餐%s元中国移动专属合约活动，该活动办理后次月生效，" +
                "有效期12个月。该活动一次性缴纳%s元，分12个月返还。另每月赠送%s元，共计赠送12个月。");
    }

    /**
     * 支付成功后发送短信
     * @param bankType
     * @param phoneNumber
     * @param bankPlanId
     */
    public void paySuccessSendMessage(String bankType,String phoneNumber,short bankPlanId){
        SmsSendCondition smsSendCond = new SmsSendCondition();
        smsSendCond.setSerialNumber(phoneNumber);
        TfBankFamilyPlan plan = bankFamilyService.selectById(bankPlanId);
        String noticeTemplate = BANK_CONTENT.get(bankType);
        String noticeContent = String.format(noticeTemplate,plan.getPlanLevel(),plan.getPlanFee(),plan.getContractPrice());
        smsSendCond.setNoticeContent(noticeContent);
        try{
            Map smsSendRetMap = smsSendService.sendSms(smsSendCond);
            logger.info("====短信返回===>{}",JSONObject.toJSONString(smsSendRetMap, SerializerFeature.WriteMapNullValue));
            printKaInfo(phoneNumber + "号卡订单生成成功短信接口内容" + JSONObject.toJSONString(smsSendRetMap));
        }catch (Exception e){
            logger.error("短信下发失败",phoneNumber,e);
        }

    }
}
