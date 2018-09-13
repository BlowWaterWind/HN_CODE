package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.common.utils.Exceptions;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.JsonUtil;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.SimBuyCommonService;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.CSRFTokenUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.onlinesim.entity.GetPhoneForO2OonlineCondition;
import com.ai.ecs.ecsite.modules.onlinesim.service.OnlineSimService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.ICommonPropagandaService;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.CommonPropaganda;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.merchant.shop.pojo.Shop;
import com.ai.ecs.merchant.shop.service.IShopInfoService;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.busi.IOrderAppointmentService;
import com.ai.ecs.order.api.busi.ISimBusiService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * Created by cc on 2018/6/6.
 * O2O在线售卡能力
 * 日志打印:printKaError2("createOrder：", request, e); shopwaplog中查看日志
 * writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "
 * simO2OH5OnlineToConfirmOrder", null, Exceptions.getStackTraceAsString(e)); 在shopflowlog中查看日志
 */
@Controller
@RequestMapping("o2oSimOnline")
public class O2oSimOnlineController extends BaseController {

    @Autowired
    private IShopInfoService shopInfoService;

    @Autowired
    private IRecmdMainService recmdMainService;

    @Autowired
    private ISimBusiService simBusiService;

    @Autowired
    private SimBuyCommonService simBuyCommonService;

    @Autowired
    private IPlansService plansService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private ICommonPropagandaService commonPropagandaService;

    @Autowired
    private OnlineSimService onlineSimService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private IOrderAppointmentService orderAppointmentService;



    /**
     * 物流或送货上门
     */
    private static final Integer LOGISTRY = 2;

    /**
     * 根据推荐连接获得店铺的物理地址
     * 将店铺的shopId保存在页面
     *
     * @param recmdCode
     * @return
     */
    @RequestMapping("getShopInfo")
    @ResponseBody
    public ResponseBean getShopInfo(HttpServletRequest request,String recmdCode) {
        ResponseBean responseBean = new ResponseBean();
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "根据推荐code查询", recmdCode, "预下单", request);
        String recmdCode2 = Base64.decode(recmdCode.getBytes());//解码 -> rcd_conf_id
        Long recmdId = Long.valueOf(recmdCode2);//推荐ID
        try {
            Shop shopInfo = (Shop)JedisClusterUtils.getObject("O2O_COMPANY_SHOP_INFO"+recmdCode);
            if(shopInfo == null){
                TfOrderRecmd recmd = recmdMainService.getOrderRecmd(new TfOrderRecmd(recmdId));
                shopInfo = shopInfoService.getShopInfoByLoginName(recmd.getRecmdPhone());
            }
            //再延长超时时间为1小时
            JedisClusterUtils.setObject("O2O_COMPANY_SHOP_INFO"+recmdCode,shopInfo,60*60);
            if (shopInfo == null) {
                responseBean.addError("无对应的店铺信息!");
            } else {
                responseBean.addSuccess(shopInfo);
            }
        } catch (Exception e) {
            if (e instanceof EcsException) {
                EcsException e1 = (EcsException) e;
                logger.error("店铺数据异常"+recmdCode, e1);
                responseBean.addError("-1", e1.getFriendlyDesc());
            } else {
                logger.error("查询店铺"+recmdCode, e);
                responseBean.addError("-1", "查询店铺信息异常,请稍后再试");
            }
        }
        return responseBean;
    }


    /**
     *       页面15上传参数:
     *       planId,planName 保存在TfPlans plan
     *       channelCityCode,shopId 保存在Shop shop
     *       CHANID deliveryMode confId recmdCode从request中获取
     * @param request
     * @param plan
     * @param model
     * @param transactionId
     * @return
     */
    @RequestMapping("simO2OH5OnlineToConfirmOrder")
    @RefreshCSRFToken
    public String simO2OH5OnlineToConfirmOrder(HttpServletRequest request,TfPlans plan,Model model,String transactionId) {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),"simO2OH5OnlineToConfirmOrder",request.getParameterMap(),"实名制入参",request);
        request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));

        String deliveryType = request.getParameter("deliveryType");
        String confId = request.getParameter("confId");
        String recmdCode = request.getParameter("recmdCode");
        JSONObject param=new JSONObject();
        TfOrderAppointment orderAppointment;
        if(StringUtils.isNotEmpty(transactionId)){
            //查询预约信息
            orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
            if (orderAppointment != null && StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
                simBuyCommonService.orderTimeout(orderAppointment);
                param = JSONObject.parseObject(orderAppointment.getRequestParam());
                plan.setPlanId(param.getLong("planId"));
                plan.setPlanName(param.getString("planName"));
                deliveryType = param.getString("deliveryMode.deliveryModeId");
                confId = param.getString("confId");
                recmdCode = param.getString("recmdCode");
                model.addAttribute("regName",orderAppointment.getCustName());
                model.addAttribute("transactionId",transactionId);
                if(StringUtils.isEmpty(orderAppointment.getCustCertNo())){
                    writerFlowLogThrowable(streamNo,"","","",getClass().getName(),
                            null,String.format("身份证信息为空[%s]",transactionId));
                    throw new RuntimeException("身份验证信息为空，请重新认证！");
                }
            } else {
                writerFlowLogThrowable(streamNo,"","","",getClass().getName(),
                        null,String.format("预约订单参数据为空，请重新预约[%s]",transactionId));
                throw new RuntimeException("身份验证信息处理异常，请重新认证！");
            }
        }
        TfH5SimConf conf = new TfH5SimConf();
        conf.setConfId(confId);
        TfH5SimConf resultPlan = plansService.queryListCond(conf).get(0);
        JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), String.valueOf(plan.getPlanId()), TfH5SimConf.JSON_ID);
        Long preFee = Long.parseLong(object.getString(TfH5SimConf.JSON_PREFEE));//预存金额
        Long cardFee = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));//卡金额
        //协议
        String serviceContract = commonPropagandaService.getCommonPropagandaByCode(CommonPropaganda.SIM_ORDER_PRO, CommonParams.CHANNEL_CODE).getCommonPropagandaContent();
        try {
            Shop shopInfoRedis = (Shop)JedisClusterUtils.getObject("O2O_COMPANY_SHOP_INFO"+recmdCode);
            if (shopInfoRedis == null) {
                throw new RuntimeException("对不起,您的页面已超时,请重新选择套餐下单!");
            }
            //担心放在对象中会丢参数
            model.addAttribute("confId", confId);
            model.addAttribute("resultPlan", resultPlan);
            model.addAttribute("plan", plan);
            model.addAttribute("shop", shopInfoRedis);
            model.addAttribute("deliveryType", deliveryType);
            model.addAttribute("serviceContract", serviceContract);
            model.addAttribute("preFee", preFee);
            model.addAttribute("cardFee", cardFee);
            //从预约回调过来时
            if(StringUtils.isNotEmpty(transactionId)){
                model.addAttribute("CHANID",param.getString("CHANID"));
                model.addAttribute("memberRecipientCity", param.getString("memberAddress.memberRecipientCity"));
                model.addAttribute("memberRecipientAddress", param.getString("memberAddress.memberRecipientAddress"));
                model.addAttribute("memberRecipientCounty", param.getString("memberAddress.memberRecipientCounty"));
                model.addAttribute("custName", param.getString("orderDetailSim.custName"));
                model.addAttribute("contactPhone", param.getString("orderDetailSim.contactPhone"));
                model.addAttribute("phone", param.getString("orderDetailSim.phone"));
                model.addAttribute("linkAddress", param.getString("orderDetailSim.linkAddress"));
                model.addAttribute("cityName", param.getString("cityName"));

            }
        } catch (Exception e) {
            logger.error("跳转到信息填写页面失败", e);
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "simO2OH5OnlineToConfirmOrder", null, Exceptions.getStackTraceAsString(e));

        }
        return "web/goods/o2osimonline/orderConfirm";
    }


    /**
     * 选号入参;cityCode和x_tag 1到厅自取;2送货上门
     * 不让用户篡改cityCode从session中取,又担心session失效(放redis里面去吧)
     * 从前台传cityCode和物流方式,提交订单的时候;做号码归属地的校验
     *
     * @param number
     * @param xTag
     * @return
     */
    @RequestMapping("o2ONetNumQuery")
    @ResponseBody
    public ResponseBean o2ONetNumQuery(String number, String xTag,String cityCode,String eparchyCode) {
        GetPhoneForO2OonlineCondition condition = new GetPhoneForO2OonlineCondition();
        ResponseBean responseBean = new ResponseBean();
        condition.setCITY_CODE(cityCode);
        condition.setTailNum(number);
        condition.setX_TAG(xTag);
        condition.setEparchyCodeFCust(eparchyCode);
//        condition.setStaffId("ITFCC000");
//        condition.setTradeDepartPassword("ai1234");
        try {
            List<Map<String, String>> nums = onlineSimService.netNumQueryForO2O(condition);
            responseBean.addSuccess(nums);
        } catch (Exception e) {
            if(e instanceof EcsException){
                EcsException e1 = (EcsException) e;
                logger.error("查询号码异常", e1);
                responseBean.addError("-1", e1.getFriendlyDesc());
            }else{
                logger.error("选号接口失败", e);
                responseBean.addError("-1", "选号接口失败");
            }
        }
        return responseBean;
    }




    /**
     * 由店员推荐出去下单,下单信息保存在tf_order_recmd_ref
     *
     * @param request
     * @param cModel  cModel包含的信息
     *                orderDetailSim:contactPhone,linkAddress(省,市,区)regName,psptId,phone,cityCode
     *                memberAddress:memberRecipientAddress(收货地址)
     *                deliveryMode:deliveryModeId(1,2)
     *                payMode:payModeId(2在线支付)
     *                userGoodsCarList[0] shopId,shopName
     * @param request
     * @Param cModel
     * @return
     */
    @RequestMapping("createO2oOnlineOrder")
    @ResponseBody
    @VerifyCSRFToken
    public ResponseBean createO2oOnlineOrder(HttpServletRequest request, UserGoodsCarModel cModel) throws Exception {
        String streamNo = createStreamNo();
        ResponseBean responseBean = new ResponseBean();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "o2o在线售卡", request.getParameterMap(), "预下单", request);
        String transactionId=request.getParameter("transactionId");
        JSONObject param;
        try {
            cModel = simBuyCommonService.getCallbackAppointment(transactionId,streamNo,cModel);
            param = cModel.getParams();
        }catch (EcsException e){
            writerFlowLogThrowable(streamNo,"","","",getClass().getName(),"",processThrowableMessage(e));
            throw new RuntimeException("身份验证信息处理异常，请重新认证！");//ConstantsBase.MY_EXCEP要和ExceptionUtils.dealExceptionRetMsg配合使用
        }
        //1到厅自取 2物流方式
        Integer deliveryMode = cModel.getDeliveryMode().getDeliveryModeId();
        String planId = param.getString("planId");
        String confId = param.getString("confId");
        String recmdCode = param.getString("recmdCode");
        String recmdCode2 = Base64.decode(recmdCode.getBytes());//解码 -> rcd_conf_id
        Long recmdId = Long.valueOf(recmdCode2);//推荐ID
        TfOrderRecmd recmd = recmdMainService.getOrderRecmd(new TfOrderRecmd(recmdId));
        TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();
        orderDetailSim.setRegType("1");
        MemberVo member = UserUtils.getLoginUser(request);
        Shop shopInfoRedis = (Shop)JedisClusterUtils.getObject("O2O_COMPANY_SHOP_INFO"+recmdCode);
        if(shopInfoRedis == null){
            throw new Exception("对不起,您的页面已超时,请重新选择套餐下单!");
        }
        if(!orderDetailSim.getChnlCodeOut().equals(shopInfoRedis.gettDistributionChnl().getCityCode())){
            throw new Exception("对不起,您的区县归属和您的店铺不一致,请确认后下单！");
        }
        if (LOGISTRY.equals(deliveryMode)) {
            //物流方式校验地址
            MemberAddress address = cModel.getMemberAddress();
            String regEx = "^[0-9A-Za-z\\u4e00-\\u9fa5]{6,}$";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(address.getMemberRecipientAddress());
            if (!matcher.matches()) {
                printKaInfo("simH5OnlineSubmitOrder地址不合法:" + JSONObject.toJSONString(address));
                writerFlowLogThrowable(streamNo, "", "", getClass().getName(), "submitOrderH5OnlineSim", address, "地址不合法");
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "订单提交失败，收货地址信息不符合规范");
            }
            orderDetailSim.setPsptAddr(address.getMemberRecipientAddress());//同步订单时，接口必须传，且有空格和字符个数校验
        }else{
            orderDetailSim.setPsptAddr("湖南省长沙市到厅取默认地址");//同步订单时，接口必须传，且有空格和字符个数校验
        }
        if (isProEnv()) {
            simBusiService.oneIdFiveNoVerify(orderDetailSim);
            logger.info("一证五号校验成功！");
        }
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel(); //查询归属地
        phoneAttributionModel.setSerialNumber(orderDetailSim.getPhone());
        String cityCode = phoneAttributionService.queryPhoneAttributionAnalysis(phoneAttributionModel);
        if (!shopInfoRedis.gettDistributionChnl().getEparchyCode().equals(cityCode)) {
            throw new Exception("对不起，您所选号码和该店铺的归属地不匹配！");
        }
        //装载
        try {
            TfOrderSub tfOrderSub = equipOrderSub(cModel, planId, confId, recmd.getRecmdPhone(), deliveryMode, member, shopInfoRedis);
            List<TfOrderSub> orderSubList = orderService.newOrder(tfOrderSub);
            TfOrderSub orderSub = orderSubList.get(0);
            simBuyCommonService.saveSimRecmdInfo(recmdCode, orderSub);
            TfOrderSub subParam = new TfOrderSub();
            subParam.setOrderSubNo(orderSub.getOrderSubNo());
            TfOrderSub orderView = orderQueryService.queryComplexOrder(
                    subParam, Variables.ACT_GROUP_MEMBER);
            if(orderView.getOrderStatusId() == OrderConstant.STATUS_AUDIT){
                //订单同步失败
                responseBean.addError("-1",orderView.getExceptionCause());
                return responseBean;
            }
            Map map = new HashMap<>();
            map.put("orderSubNo", orderSub.getOrderSubNo());
            if (orderSub.getOrderSubPayAmount() > 0) {
                map.put("type", "toPay");
                map.put("confId", confId);
                map.put("planId", planId);
                map.put("selectPhone", tfOrderSub.getPhoneNumber());
            } else {
                map.put("type", "toSuccess");
            }
            responseBean.addSuccess(map);
        } catch (Exception e) {
            responseBean = ExceptionUtils.dealExceptionRetMsg(responseBean, e, "订单生成失败");
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "createO2oOnlineOrder", null, Exceptions.getStackTraceAsString(e));
            logger.error("生成订单失败", e);
        }
        return responseBean;
    }


    private TfOrderSub equipOrderSub(UserGoodsCarModel cModel, String planId, String confId, String shopLoginName, Integer deliveryMode, MemberVo member, Shop shop) throws Exception {
        TfOrderSub orderSub = new TfOrderSub();
        TfOrderDetailSim detailSim = cModel.getOrderDetailSim();
        TfUserGoodsCar userGoodsCar = cModel.getUserGoodsCarList().get(0);
        MemberAddress address = cModel.getMemberAddress();
        address.setMemberRecipientName(detailSim.getRegName());
        orderSub.setOrderTypeId(OrderConstant.TYPE_SIM);
        //用户购买的号码保存在PHONE_NUMBER中,用于支付成功后的入参
        orderSub.setPhoneNumber(detailSim.getPhone().substring(0, 11));
        MemberLogin memberLogin;
        TfOrderUserRef userRef = new TfOrderUserRef();
        if (member == null) {//未登录
            detailSim.setIsCreateUser("RE");//分享办理
            detailSim.setMemberId(1234567890L);
            detailSim.setCustName(detailSim.getRegName());
            userRef.setMemberId(1234567890L);
            userRef.setMemberLogingName(detailSim.getContactPhone()); //归属地市，直接使用收货地址信息
        } else {
            detailSim.setIsCreateUser("AP");
            memberLogin = member.getMemberLogin();
            detailSim.setMemberId(memberLogin.getMemberId());
            detailSim.setCustName(memberLogin.getMemberLogingName());
            userRef.setMemberId(memberLogin.getMemberId());
            userRef.setMemberLogingName(memberLogin.getMemberLogingName()); //归属地市，直接使用收货地址信息
        }
        userRef.setEparchyCode(address.getMemberRecipientCity());//归属地市，直接使用收货地址信息
        userRef.setCounty(address.getMemberRecipientCounty());//归属区县，直接使用收货地址信息
        userRef.setContactPhone(detailSim.getPhone());
        userRef.setMemberCreditClass("0"); //用户星级 | 客户类型，为空则为普通用户
        orderSub.setShopId(userGoodsCar.getShopId());//从获取的
        orderSub.setShopName(userGoodsCar.getShopName());
        orderSub.setValetStaffId(shopLoginName);//和掌柜店员登录手机号码
        orderSub.setDeliveryModeId(4);//号段卡3(订单同步后结束) O2O在线售卡4(订单同步后到写卡环节)
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
        JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), planId, TfH5SimConf.JSON_ID);
        Long preFee = Long.parseLong(object.getString(TfH5SimConf.JSON_PREFEE));//预存金额
        Long cardFee = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));//卡金额
        Long planRMB = preFee + cardFee;
       detailSim.setChnlCodeOut("EO5O");//外围渠道编码不设置外围渠道编码取order默认的工号和密码;但是统计报表需要
//        detailSim.setChnlCodeOutSub1(resultPlan.getChnlCodeOutSub1());//外围子渠道编码
        logger.info("页面获取参数：planId:{},confId {}", planId, confId);

        TfPlans planCond = new TfPlans();
        planCond.setPlanId(Long.parseLong(planId));
        List<TfPlans> plans = plansService.getPlanList(planCond);
        TfPlans plan = plans.get(0);
        String cityCode = detailSim.getCityCode();
        String cityCodeSuffix = cityCode.substring(2);
        String productId = plan.getProductId().replace("**", cityCodeSuffix);
        String baseSet = plan.getPlanCode().replace("**", cityCodeSuffix);
        detailSim.setSimProductId(productId);
        detailSim.setBaseSet(baseSet);//套餐编码
        detailSim.setBaseSetName(plan.getPlanName());//套餐名称
        detailSim.setPhoneType(TfOrderDetailSim.PT_PU_NUM);//普号
        detailSim.setPreFee(preFee);
        detailSim.setCardFee(cardFee);
        detailSim.setSynOnliResuIssuc("onli");
        detailSim.setSynOnliResuMsg("o2oonline");
        detailSim.setCardType(resultPlan.getCardType());
        detailSim.setTerminalType("XYYX");
        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        orderSubDetail.setRootCateId(2L);//产品类别(javaBean中定义非空)

        orderSubDetail.setShopId(userGoodsCar.getShopId());//(javaBean中定义非空)
        orderSubDetail.setShopName(userGoodsCar.getShopName());//(javaBean中定义非空)
        orderSubDetail.setShopTypeId(userGoodsCar.getShopTypeId());//(javaBean中定义非空)

        orderSubDetail.setGoodsName("O2O在线号卡订单");// 商品信息(javaBean中定义非空)
        orderSubDetail.setGoodsImgUrl("www.wap.hn.10086.cn/shop");//默认商城的首页
        orderSubDetail.setGoodsRemark(userGoodsCar.getGoodsRemark());//备注
        orderSubDetail.setGoodsSkuId(userGoodsCar.getGoodsSkuId());
        orderSubDetail.setGoodsSkuPrice(planRMB);//在生成订单是自动设置tfOrderSub的支付金额(javaBean中定义非空)
        orderSubDetail.setGoodsSkuNum(1L);//每个订单只能一个号码(javaBean中定义非空 订单生成过程中用skuNum * skuPrice)
        orderSubDetail.setGoodsSkuId(Long.parseLong(planId));//保存套餐的planId(javaBean中定义非空)
        orderSubDetail.setGoodsId(Long.parseLong(detailSim.getPhone()));//号卡订单:号码作为GoodsId(javaBean中定义非空)
        orderSubDetail.setGoodsFormat(detailSim.getPhone());
        orderSubDetail.setGoodsUrl(confId);//号卡订单:confId保存在goodsFormat中(javaBean中定义非空)
        orderSubDetail.setOrderDetailSim(detailSim);//装配sim订单信息
        orderSub.addOrderDetail(orderSubDetail);//装配订单详情信息
        orderSub.setOrderUserRef(userRef);//装配用户信息
        orderSub.setOrderAddressId(deliveryMode.longValue());
        //装配物流信息
        TfOrderRecipient receipt = new TfOrderRecipient();
        receipt.setOrderRecipientProvince(address.getMemberRecipientProvince());
        receipt.setOrderRecipientCity(address.getMemberRecipientCity());
        receipt.setOrderRecipientCounty(address.getMemberRecipientCounty());
        receipt.setOrderRecipientAddress(address.getMemberRecipientAddress());
        //如果收货人电话为空，则取号卡订单详情表中的联系电话，用于网选厅取和h5匿名在线售卡
        String endContactPhone = detailSim.getContactPhone();
        receipt.setOrderRecipientPhone(endContactPhone);
        //如果没有收货人，则用身份证的名字
        receipt.setOrderRecipientName(detailSim.getRegName());
        orderSub.setRecipient(receipt);
        String hallAddress = shop.getShopPhysicallProvince() + shop.getShopPhysicallCity() +
                shop.getShopPhysicallCounty() + shop.getShopPhysicallAddress();
        orderSub.setHallAddress(hallAddress);//自提网点
        orderSub.setOrderChannelCode("E050");//渠道编码,供和掌柜查询(javaBean中定义渠道非空)
        orderSub.setPayModeId(cModel.getPayMode().getPayModeId());//支付方式
        return orderSub;
    }

    @RequestMapping("orderSuccess")
    public String toOrderSuccess(String orderSubNo, Model model) {
        TfOrderSub orderSubParam = new TfOrderSub();
        orderSubParam.setOrderSubNo(orderSubNo);
        TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderSubParam);
        model.addAttribute("orderSub", orderSub);
        model.addAttribute("detailSim", orderSub.getDetailSim());
        return "web/goods/o2osimonline/orderSuccess";
    }
}
