package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.JsonUtil;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.iis.upp.util.UppCore;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNum;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNumQueryCondition;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.ICommonPropagandaService;
import com.ai.ecs.goods.entity.CommonPropaganda;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.register.IRegisterService;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.sales.entity.CouponInfo;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ai.ecs.ecm.mall.wap.modules.goods.controller.GoodsBuyController.confirmOrderBaseInfo;


/**
 * 优选(靓号) 在线售卡
 *
 * @author xiejq
 * Created by cc on 2018/1/15.
 */
@Controller
@RequestMapping("simBeautifulBuy")
public class SimBeautifulBuyController extends RecmdDefaultController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private NetNumServerService netNumServerService;

    @Autowired
    private ICommonPropagandaService commonPropagandaService;

    @Autowired
    private IMemberLoginService loginService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private IRegisterService registerService;

    @Autowired
    private GoodsService goodsService;


    @Value("${orderPayUrl}")
    private String orderPayUrl;

    /**
     * afterOrderPayUrl=http://172.168.20.75:8091/shop
     */
    @Value("${afterOrderPayUrl}")
    private String afterOrderPayUrl;
    @Value("${discountTime}")
    private String discountTime;
    @Value("${financialTest}")
    private String financialTest;
    @Value("${didiclientId}")
    private String didiclientId;
    @Value("${recmdSimTimeInterval}")
    private String recmdSimTimeInterval;

    private static final String CONF_ID = "confId";

    private static final String CITY_CODE = "cityCode";

    private static final String PRE_FEE = "preFee";

    private static final String CARD_FEE = "cardFee";

    private static final String PLAN_ID = "planId";

    private static final String CHOOSE_NUM = "chooseNum";

    private static final String GUARANTEED_FEE = "guaranteedFee";

    /**
     * 将号码和预存费用保存到redis,用于提交订单的校验
     */
    private static final int SECONDS = 30 * 60;

    @RequestMapping("toSelectPage")
    public String toBeautifulSimSelectPage(TfH5SimConf cond,Model model) {
        if (cond.getConfId() == null || cond.getConfId().length() == 0) {
            throw new RuntimeException("配置参数不能为空");
        }
        List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
        if (h5Confs.isEmpty()) {
            throw new RuntimeException("配置参数不存在");
        }
        TfH5SimConf confResult = h5Confs.get(0);
        if (TfH5SimConf.ConfStatus.SC_OFFLINE.getValue().equals(confResult.getConfStatus())) {
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡销售已经下线！");
        }
        model.addAttribute(CONF_ID,cond.getConfId());
        return "web/goods/simbeauty/selectBeautifulSimOnline";
    }

    /**
     * 根据用户选择的号码,预存费用,保底消费选择套餐
     * 将已经选择的号卡，号码的预存费用，卡费保存到session中,用于多个页面传
     * @param chooseNum
     * @param preFee
     * @param guaranteedFee
     * @param model
     * @return
     */
    @RequestMapping("showPlanInfo")
    public String toShowPlanPage(HttpServletRequest request,String chooseNum, String preFee, String guaranteedFee, Model model) {
        String confId = request.getParameter("confId");
        if(confId == null){
            throw new RuntimeException("页面超时,请重新选择号码!");
        }
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
        TfH5SimConf confResult;
        List<TfPlans> plans;
        if (h5Confs.isEmpty()) {
            throw new RuntimeException("获取配置信息失败");
        }
        confResult = h5Confs.get(0);
        plans = plansService.queryPlanListByH5SimConf(confResult.getPlanJson());
        Iterator<TfPlans> iter = plans.iterator();
        while (iter.hasNext()) {
            TfPlans plan = iter.next();
            if (plan.getPlanFee() > Long.parseLong(guaranteedFee) * 100) {
                iter.remove();
            }
        }
        model.addAttribute("conf", confResult);
        model.addAttribute("plans", plans);
        model.addAttribute(CHOOSE_NUM,chooseNum);
        model.addAttribute(PRE_FEE,preFee);
        model.addAttribute(GUARANTEED_FEE, guaranteedFee);
        return "web/goods/simbeauty/planConfigShow";
    }


    /**
     * 填写用户地址信息,提交订单
     *
     * @param plan
     * @param model
     * @return
     */
    @RequestMapping("beautifulSimH5OnlineToConfirmOrder")
    public String confirmOrderBeautifulSimH5Online(HttpServletRequest request,TfPlans plan, Model model) {
        String planId = String.valueOf(plan.getPlanId());
        String confId = request.getParameter(CONF_ID);
        String chooseNum = request.getParameter(CHOOSE_NUM);
        String preFee = request.getParameter(PRE_FEE);
        String guaranteedFee = request.getParameter("guaranteedFee");
        if ("".equals(planId)) {
            throw new RuntimeException("套餐参数传递错误");
        }
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        List<TfH5SimConf> resultPlans = plansService.queryListCond(cond);
        if (resultPlans == null || resultPlans.isEmpty()) {
            throw new RuntimeException("参数未配置或者网络错误");
        }
        TfH5SimConf resultPlan = resultPlans.get(0);
        JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), planId, TfH5SimConf.JSON_ID);
        Long cardFee = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));//卡金额
        String serviceContractSim = commonPropagandaService.getCommonPropagandaByCode(CommonPropaganda.SIM_ORDER_PRO, CommonParams.CHANNEL_CODE).getCommonPropagandaContent();
        String beautifulContract = commonPropagandaService.getCommonPropagandaByCode(CommonPropaganda.BEAUTIFUL_SIM_ORDER_PRO, "E050").getCommonPropagandaContent();
        model.addAttribute("serviceContractSim", serviceContractSim);
        model.addAttribute("beautifulContract", beautifulContract);
        model.addAttribute("plan", plan);
        //TODO  卡费是从配置中取出来的，预存费,保底消费是从crm那边过来的
        model.addAttribute("cardFee", cardFee);
        model.addAttribute(CHOOSE_NUM, chooseNum);
        model.addAttribute(PRE_FEE, preFee);
        model.addAttribute("guaranteedFee", guaranteedFee);
        // TODO 查询号码的时候返回号码的归属地市信息
        model.addAttribute("cityCode", "0731");
        return "web/goods/simbeauty/confirmOrderSim";
    }

    //todo
    @RequestMapping("getQueryBeautyNumsH5Online")
    @ResponseBody
    public ResponseBean getQueryBeautyNumsH5Online(String cityCode, String minPreFee, String maxPreFee, String section, String guaranteeFee, String tailNum) throws Exception {
        Map<String, Object> paramMap = Maps.newHashMap();
        Session session = UserUtils.getSession();
        session.setAttribute(CITY_CODE, cityCode);
        List<NetNum> numList = Lists.newArrayList();
        ResponseBean resp = new ResponseBean();
        try {
            if (cityCode == null || cityCode.equals("")) {
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "请选择归属地市！");
            }
            NetNumQueryCondition condition = new NetNumQueryCondition();
            paramMap.put("X_CHOICE_TAG", 3); //分支：1-自助选号，3-网上商城选号
            paramMap.put("PARA_VALUE4", 20); //最大数量，默认每次查询出9个号码
            paramMap.put("CODE_TYPE_CODE", 1); //e号码类型：1-普号，2-靓号
            paramMap.put("PARA_VALUE5", 2); //1：号头查询，2：号尾查询，3：模糊查询
            paramMap.put("PARA_VALUE6", "0"); //开始预存,单位为分String 8
            paramMap.put("PARA_VALUE7", "10000000"); //结束预存,单位为分String 8
            paramMap.put("START_SERIAL_NUMBER", tailNum);
            paramMap.put("TRADE_EPARCHY_CODE", cityCode);// 设置号码归属地
            condition.setConditionExtendsMap(paramMap);
            numList = netNumServerService.netNumQuery(condition);
            for (NetNum num : numList) {
                //todo 等待新接口，实际传的是预存金额
                jedisCluster.setex(num.getNum(), SECONDS, num.getGuaranteedFee());
            }
            resp.addSuccess(numList);
        } catch (Exception e) {
            logger.info("号码查询异常" + e);
            splitException(e, "号码查询异常！");
        }
        return resp;
    }


    @RequestMapping("submitOrderToPay")
    public void submitOrderToPay(HttpServletRequest request, UserGoodsCarModel cModelOrigin, HttpServletResponse response) throws Exception {
        Map<String,String> respInnerMap = new HashMap<>();
        respInnerMap.put("type", ""); //特殊业务置换特殊标记，用于js中判断不同情况不同方式向用户展示信息
        ResponseBean resp = new ResponseBean();
        Long planRMB;
        UserGoodsCarModel cModel = UserGoodsCarModel.getSimBaseInfo(cModelOrigin);
        String confId;
        Long preFee;
        Long cardFee;
        confId = request.getParameter(CONF_ID);
        preFee = Long.parseLong( request.getParameter(PRE_FEE));
        cardFee = Long.parseLong(request.getParameter(CARD_FEE));
        String planId = request.getParameter(PLAN_ID);
        String preFeeValidate = jedisCluster.get(cModel.getOrderDetailSim().getPhone());

        TfH5SimConf resultPlan = new TfH5SimConf();
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        List<TfH5SimConf> confList = plansService.queryListCond(cond);
        Long cardFeeValidate = 0L;
        if (confList != null && !confList.isEmpty()) {
            resultPlan = confList.get(0);
            JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), planId, TfH5SimConf.JSON_ID);
            cardFeeValidate = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));
        }
        if (preFee + cardFee != Long.parseLong(preFeeValidate) + cardFeeValidate) {
            throw new RuntimeException("请确认您提交的预存费用是否正确");
        }
        TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();
        String cityCode = orderDetailSim.getCityCode();
        if(isProEnv()) {
            simBusiService.realityVerifyV2(orderDetailSim);
            logger.info("身份证信息验证成功！");
            simBusiService.oneIdFiveNoVerify(orderDetailSim);
            logger.info("一证五号校验成功！");

            orderDetailSim.setPhone(orderDetailSim.getPhone().substring(0, 11));
            PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
            phoneAttributionModel.setSerialNumber(orderDetailSim.getPhone());
            Map phoneResultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
            String message = JSONObject.toJSONString(phoneResultMap, SerializerFeature.WriteMapNullValue);
            logger.info("归属地校验结果返回==> %s", message);
            //接口异常时
            String pasResult = (String) ((Map) ((List) phoneResultMap.get("result")).get(0)).get("AREA_CODE");
            if (pasResult == null) {
                printKaInfo("submitOrderH5OnlineSim归属地校验接口异常：" + JSONObject.toJSONString(phoneResultMap));
            }
            //接口正常且有AREA_CODE字段，则校验失败
            if (pasResult != null && pasResult.length() > 0 && !cityCode.equals(pasResult)) {
                printKaInfo("submitOrderH5OnlineSim归属地不匹配：" + JSONObject.toJSONString(phoneResultMap));
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "对不起，您所选号码和号码归属地不匹配！");
            }
        }
        //== 地址处理，防止同步订单报错
        //取选择地址，并且去掉中间的空格
        String idAddr = orderDetailSim.getLinkAddress();
        if (idAddr.contains(" ")) {
            idAddr = idAddr.replaceAll(" ", "");
        }
        //如果字符个数小于6，则补充字符个数
//        if (idAddr.length() < 6) {
//            printKaInfo("submitOrderH5OnlineSim配送地址所在地区有误：" + JSONObject.toJSONString(phoneResultMap));
//            throw new RuntimeException(ConstantsBase.MY_EXCEP + "配送地址所在地区有误！");
//        }

        MemberAddress address = cModel.getMemberAddress();
        String regEx = "^[0-9A-Za-z\\u4e00-\\u9fa5]{6,}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(address.getMemberRecipientAddress());
        if (!matcher.matches()) {
            printKaInfo("simH5OnlineSubmitOrder地址不合法:" + JSONObject.toJSONString(address));
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "订单提交失败，收货地址信息不符合规范");
        }
        if (StringUtils.isEmpty(address.getMemberRecipientCity())) {
            printKaInfo("simH5OnlineSubmitOrder地址为空问题监控:" + JSONObject.toJSONString(address));
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "订单提交失败，请重试[地址为空]");
        }
        address.setMemberRecipientName(orderDetailSim.getRegName());
        planRMB = preFee + cardFee;
        String payPlatform = cModel.getPayPlatform();

        MemberLogin memberLogin;
        MemberVo member = UserUtils.getLoginUser(request);
//        if (member == null) {
//            throw new RuntimeException("登录超时");
//        }
        //TODO memberLogin = member.getMemberLogin();
        //== 套餐信息+个性档次信息
        //配置项Id：以后的金额也可能配置在planJson中，所以这里必须通过confId去数据查数据
        //外围渠道编码

        //todo deleting
        memberLogin = new MemberLogin();
        memberLogin.setMemberId(1234567890L);
        memberLogin.setMemberPhone(15608446648L);
        memberLogin.setMemberLogingName("谢江琼");


        orderDetailSim.setChnlCodeOut(resultPlan.getChnlCodeOut());
        //外围子渠道编码
        orderDetailSim.setChnlCodeOutSub1(resultPlan.getChnlCodeOutSub1());
        logger.info("页面获取参数：planId:{}confId:{}",planId,confId);
        //== 子订单信息
        TfOrderSub orderSub = new TfOrderSub();
        //== 用户信息
        TfOrderUserRef userRef = new TfOrderUserRef();
        userRef.setMemberId(memberLogin.getMemberId());
        userRef.setMemberLogingName(memberLogin.getMemberLogingName());
        //归属地市，直接使用收货地址信息
        userRef.setEparchyCode(address.getMemberRecipientCity());
        //归属区县，直接使用收货地址信息
        userRef.setCounty(address.getMemberRecipientCounty());
        Long cp = memberLogin.getMemberPhone();
        if (cp != null) {
            userRef.setContactPhone(cp.toString());
        }
        //用户星级 | 客户类型，为空则为普通用户
        userRef.setMemberCreditClass("0");
        orderSub.setOrderUserRef(userRef);
        //== 支付金额
        cModel.setMemberAddress(address);
        orderSub = confirmOrderBaseInfo(cModel, orderSub);
        orderSub.setOrderSubPayAmount(planRMB);//流程中需要用到，必须设置
        orderSub.setOrderSubAmount(planRMB);//号卡订单SubAmount和SubPayAmount相同

        //== 处理TfOrderDetailSim
        String cityCodeSuffix = cityCode.substring(2);
        TfPlans plans = new TfPlans();
        plans.setPlanId(Long.parseLong(planId));
        plans = plansService.getPlanList(plans).get(0);
        String productId = plans.getProductId().replace("**", cityCodeSuffix);
        String baseSet = plans.getPlanCode().replace("**", cityCodeSuffix);
        orderDetailSim.setSimProductId(productId);
        orderDetailSim.setPreFee(preFee);
        orderDetailSim.setCardFee(cardFee);
        orderDetailSim.setBaseSet(baseSet);//套餐编码
        orderDetailSim.setBaseSetName(plans.getPlanName());//套餐名称
        orderDetailSim.setPhoneType(TfOrderDetailSim.PT_PU_NUM);//普号
        orderDetailSim.setPsptAddr(idAddr);//同步订单时，接口必须传，且有空格和字符个数校验
        //设置供应商
        orderDetailSim.setMemberId(memberLogin.getMemberId());
        orderDetailSim.setCustName(memberLogin.getMemberLogingName());
        //== 子订单详情信息
        TfUserGoodsCar car = cModel.getUserGoodsCarList().get(0);
        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        orderSubDetail.setRootCateId(2L);//产品类别
        orderSubDetail.setShopId(car.getShopId());
        orderSubDetail.setShopName(car.getShopName());
        orderSubDetail.setShopTypeId(car.getShopTypeId());
        orderSubDetail.setGoodsName(car.getGoodsName());// 商品信息
        orderSubDetail.setGoodsImgUrl(car.getGoodsSkuImgUrl());
        orderSubDetail.setGoodsRemark(car.getGoodsRemark());//备注
        orderSubDetail.setGoodsSkuId(car.getGoodsSkuId());
        //orderSubDetail.setGoodsUrl(url.getGoodsUrl()); // 设置商品单品页URL
        //orderSubDetail.setWapGoodsUrl(url.getGoodsUrl());
        orderSubDetail.setSupplierShopId(car.getShopId());// 设置供应商Id和Name
        orderSubDetail.setSupplierShopName(car.getShopName());
        orderSubDetail.setGoodsSkuPrice(planRMB);
        orderSubDetail.setGoodsSkuNum(1L);//每个订单只能一个号码
        orderSubDetail.setProdSkuId(Long.parseLong(orderDetailSim.getPhone()));
        //orderSubDetail.setProdBossCode(goodsInfo.getProdBossCode());//号卡没有这个属性
        //orderSubDetail.setMarketId(marketId);//无活动
        orderSubDetail.setGoodsSkuId(Long.parseLong(orderDetailSim.getPhone()));//号卡订单：号码作为SKUID
        orderSubDetail.setGoodsId(Long.parseLong(orderDetailSim.getPhone()));//号卡订单：号码作为GoodsId
        orderSubDetail.setGoodsFormat(orderDetailSim.getPhone());//号卡订单：号码作为商品规格
        orderSubDetail.setOrderDetailSim(orderDetailSim);
        orderSub.addOrderDetail(orderSubDetail);
        //== 号码第一次选占
        /*TODO 开发时候不预占
        simBusiService.selTempOccupyRes(orderDetailSim);*/
        logger.info("号码选占成功！");
        try {
            //==生成订单
            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
            TfOrderSub orderSubResult = orderSubList.get(0);
            //printKaInfo("submitOrderH5OnlineSim订单生成成功：" + JSONObject.toJSONString(orderSubResult, SerializerFeature.WriteMapNullValue));
            //== 非0元支付
            if (planRMB > 0) {
                String content = toPayOrder(request, cModel, orderSubResult.getOrderSubNo(), memberLogin);
                response.setContentType("text/html;charset=" + CommonParams.PAY_CHARSET.get(payPlatform));
                PrintWriter out = response.getWriter();
                out.print(content);
                out.flush();
                out.close();
            }
            //== 订单生成后处理
            dealAfterNewOrder(request, orderDetailSim, plans, Long.parseLong(orderDetailSim.getPhone()), orderSubResult);
            logger.info("页面获取参数"+planId);
        } catch (Exception e) {
           logger.error("订单生成失败",e);
            printKaError2("submitOrderH5OnlineSim生成订单失败：", request, e);
            logger.info("页面获取参数"+planId);
            resp = ExceptionUtils.dealExceptionRetMsg(resp, e, "订单生成失败");
        }
    }

    @RequestMapping("getQueryBeautyNumsH5OnlineMock")
    @ResponseBody
    public ResponseBean getQueryBeautyNumsH5OnlineMock(String cityCode) {
        Session session = UserUtils.getSession();
        session.setAttribute(CITY_CODE, cityCode);
        jedisCluster.setex("15608446648", SECONDS, "1");
        List<NetNum> numList = Lists.newArrayList();
        for (int i = 0; i < 20; i++) {
            NetNum netNum = new NetNum();
            netNum.setBrand("神州行");
            netNum.setCostNoRule("1");
            netNum.setGuaranteedFee("1000");
            netNum.setNum("15608446648");
            numList.add(netNum);
        }
        ResponseBean resp = new ResponseBean();
        resp.addSuccess(numList);
        return resp;
    }

    private String toPayOrder(HttpServletRequest request, UserGoodsCarModel carModel, String orderNos, MemberLogin memberLogin) throws Exception {
       /*订单合并：子订单关联主订单，用于支付*/
        Map<String, Object> resultMap = orderService.mergeOrder(orderNos);
        TfOrder order = (TfOrder) resultMap.get("order");
        List<TfOrderSub> orderSubList = (List<TfOrderSub>) resultMap.get("orderSubList");
        List<CouponInfo> couponInfoList = carModel.getCouponInfoList();
        String payPlatform = carModel.getPayPlatform();
        //获取商户分润规则
//        String shRule = goodsService.simShRule(orderSubList, couponInfoList, payPlatform);
        Integer orderTypeId = orderSubList.get(0).getOrderTypeId();
        String merchantId = null;
        String key = null;
        String callbackUrl = null;
        String notifyUrl = null;
        String type = null;

        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        JSONObject innerMerchant = goodsService.getInnerMerchant(orderSubList);
        logger.info("innerMerchant:%s", innerMerchant);
        merchantId = innerMerchant.getString("merchantId");
        key = innerMerchant.getString("key");
        // 同步页面回调地址
        callbackUrl = basePath + "/simBeautifulBuy/orderSuccess";
        // 异步页面回调地址
        notifyUrl = afterOrderPayUrl + "/goodsBuy/afterPayOrder";
        type = CommonParams.PAY_INTERFACE_TYPE.get(carModel.getPayPlatform());

        //订单支付金额
       // 测试 String orderPayAmount = goodsService.getOrderPayAmount(request, couponInfoList, orderSubList, order);
        String orderPayAmount = "1";
        String orderDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String productName = GoodsService.getProductName(orderSubList);
        String payType = CommonParams.PAY_TYPE.get(orderTypeId);
        String reserved1 = goodsService.getReserved1(orderSubList);

        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("payOrg", carModel.getPayPlatform());//支付机构：
        //shRule = "分润规则";
//        paramMap.put("shRule", shRule);//分账支付分润规则
        paramMap.put("type", type);//接口类型
        paramMap.put("callbackUrl", callbackUrl);//同步页面返回地址
        paramMap.put("notifyUrl", notifyUrl);//异步接收支付结果地址
        paramMap.put("merchantId", merchantId);//统一支付平台分配的唯一商户编号
        paramMap.put("version", "2.0.0");//版本号：固定为2.0.0
        paramMap.put("characterSet", "UTF-8");//编码方式
        paramMap.put("channelId", "E021");//渠道：暂固定为E021
        paramMap.put("amount", orderPayAmount);//订单金额 单位为分
        paramMap.put("currency", "00");//币种
        paramMap.put("orderDate", orderDate);//订单提交日期
        paramMap.put("merchantOrderId", String.valueOf(order.getOrderId()));//商户订单号
        paramMap.put("merAcDate", orderDate);//商户会计日期，与订单提交日期保持一致
        paramMap.put("productDesc", productName);//商品介绍
        paramMap.put("productName", productName);//商品名称
        paramMap.put("payType", payType);//支付类型
        paramMap.put("period", "30");//有效期数量
        paramMap.put("periodUnit", "00");//有效期单位：00-分，01-小时，02-日，03-月
        paramMap.put("reserved1", reserved1);//有效期单位：00-分，01-小时，02-日，03-月
        paramMap.put("hmac", UppCore.getHmac(paramMap, key, "UTF-8"));//签名数据
        String content = UppCore.sentHttp2(orderPayUrl, paramMap);

        //订单支付信息
        TfOrderPay orderPay = new TfOrderPay();
        orderPay.setOrderId(order.getOrderId());
        orderPay.setOrderPayAmount(order.getOrderPayAmount());
        orderPay.setOrgCode(carModel.getPayPlatform());
//        orderPay.setShRule(shRule);
        orderPay.setHmac(key);
        orderPay.setOrderHarvestExpend("0");
        orderPay.setMerchantId(merchantId);
        orderPay.setOrderPayPerson(memberLogin.getMemberLogingName());
        orderService.saveOrderPay(orderPay);
        return content;
    }

    /**
     * 同步回调,根据chargeflowId查询订单,跳转办理成功/失败的页面
     * @param request
     * @param returnCode
     * @param chargeflowId
     * @return
     */
    @RequestMapping("orderSuccess")
    public String toOrderSuccess(HttpServletRequest request, String returnCode, Long chargeflowId,Model model) {

        model.addAttribute("returnCode",returnCode);
        return "web/goods/simbeauty/orderSuccess";
    }

    /**
     * todo 回调更新订单状态
     * 异步回调,更新订单状态
     * @param request
     */
    @RequestMapping("afterPayOrder")
    public void afterPayOrder(HttpServletRequest request){

    }
}
