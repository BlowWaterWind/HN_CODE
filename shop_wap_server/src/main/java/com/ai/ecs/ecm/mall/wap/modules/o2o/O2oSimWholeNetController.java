package com.ai.ecs.ecm.mall.wap.modules.o2o;


import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.common.utils.Exceptions;
import com.ai.ecs.common.utils.JsonUtil;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.SimBuyCommonService;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.ecop.o2o.entity.TfH5EntranceConf;
import com.ai.ecs.ecop.o2o.service.H5EntranceConfService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNum;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import com.ai.ecs.ecsite.modules.o2ogroupsim.entity.GetEmptyCardInfoByPukCondition;
import com.ai.ecs.ecsite.modules.o2ogroupsim.entity.NetNumQueryPlusCondition;
import com.ai.ecs.ecsite.modules.o2ogroupsim.service.O2oSimGroupService;
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
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.busi.IOrderAppointmentService;
import com.ai.ecs.order.api.busi.ISimBusiService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

@Controller
@RequestMapping("simWholenet")
public class O2oSimWholeNetController extends BaseController {

    @Autowired
    private ISimBusiService simBusiService;

    @Autowired
    private IPlansService plansService;

    @Autowired
    private NetNumServerService netNumServerService;

    @Autowired
    private ICommonPropagandaService commonPropagandaService;

    @Autowired
    private IRecmdMainService recmdMainService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private SimBuyCommonService simBuyCommonService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private H5EntranceConfService h5EntranceConfService;

    @Autowired
    private O2oSimGroupService o2oSimGroupService;

    @Autowired
    private IShopInfoService shopInfoService;

    @Autowired
    private IOrderAppointmentService orderAppointmentService;

    private static final Map<String, String> CITY_NAME_REF = new HashMap<>();

    static {
        CITY_NAME_REF.put("0731", "长沙市");
        CITY_NAME_REF.put("0733", "株洲市");
        CITY_NAME_REF.put("0732", "湘潭市");
        CITY_NAME_REF.put("0734", "衡阳市");
        CITY_NAME_REF.put("0739", "邵阳市");
        CITY_NAME_REF.put("0730", "岳阳市");
        CITY_NAME_REF.put("0736", "常德市");
        CITY_NAME_REF.put("0744", "张家界市");
        CITY_NAME_REF.put("0737", "益阳市");
        CITY_NAME_REF.put("0735", "郴州市");
        CITY_NAME_REF.put("0746", "永州市");
        CITY_NAME_REF.put("0745", "怀化市");
        CITY_NAME_REF.put("0738", "娄底市");
        CITY_NAME_REF.put("0743", "湘西州市");
    }

    @RequestMapping("getH5FastEntrance")
    @ResponseBody
    public ResponseBean getH5FastEntrance() {
        ResponseBean responseBean = new ResponseBean();
        try {
            List<TfH5EntranceConf> confs = h5EntranceConfService.queryAllEffectiveEntrance();
            responseBean.addSuccess(confs);
        } catch (Exception e) {
            logger.error("查询配置失败", e);
            responseBean.addError("-1", "查询快捷入口失败!");
        }
        return responseBean;
    }

    /**
     * ajax异步请求号码
     *
     * @param departId
     * @param cityCode
     * @param start
     * @param startNumber
     * @param number
     * @return
     */
    @RequestMapping("groupNetNumQuery")
    @ResponseBody
    public ResponseBean simchoose(String departId, String cityCode, String start, String startNumber, String number) {
        ResponseBean responseBean = new ResponseBean();
        NetNumQueryPlusCondition condition = new NetNumQueryPlusCondition();
        condition.setDepartId(departId);//设置部门ID
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("X_CHOICE_TAG", 3);//分支：1-自助选号，3-网上商城选号
        paramMap.put("PARA_VALUE4", 20);//最大数量，默认每次查询出9个号码
        paramMap.put("CODE_TYPE_CODE", 1);//号码类型：1-普号，2-靓号
        paramMap.put("USER_EPARCHY_CODE", cityCode);
        if (StringUtils.isNotEmpty(start)) {
            paramMap.put("PARA_VALUE5", start);//1：号头查询，2：号尾查询，3：模糊查询
            paramMap.put("START_SERIAL_NUMBER", startNumber);
        }
        if (StringUtils.isNotEmpty(number)) {
            paramMap.put("SERIAL_NUMBER", number);
        }
        paramMap.put("TRADE_EPARCHY_CODE", cityCode);// 设置号码归属地
        condition.setConditionExtendsMap(paramMap);
        logger.info("=====hewei==>" + JSONObject.toJSONString(condition, SerializerFeature.WriteMapNullValue));
        try {
            List<NetNum> numList = netNumServerService.netNumQueryForWholeNet(condition);
            responseBean.addSuccess(numList);
        } catch (Exception e) {
            logger.error("");
        }
        return responseBean;
    }


    @RequestMapping("simDetail")
    public String simDetail(String confId, String mobile, Model model,String city) {
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);
        TfH5SimConf confResult = h5Confs.get(0);
        List<TfPlans> plans = plansService.queryPlanListByH5SimConf(confResult.getPlanJson());
        model.addAttribute("conf", h5Confs.get(0));
        model.addAttribute("plans", plans);
        model.addAttribute("mobile", mobile);
        model.addAttribute("cityName", CITY_NAME_REF.get(city));
        return "web/goods/o2osimgroup/simDetail";
    }

    @RequestMapping("toConfirm")
    @RefreshCSRFToken
    public String toConfrimOrder(HttpServletRequest request,Model model,String transactionId) {
        String streamNo = createStreamNo();
        TfOrderAppointment orderAppointment;
        JSONObject param = new JSONObject();
        String mobile = request.getParameter("mobile");
        String confId = request.getParameter("confId");
        String planId = request.getParameter("planId");
        String recmdCode = request.getParameter("recmdCode");
        String cityCode = request.getParameter("cityCode");
        if(StringUtils.isNotEmpty(transactionId)){
            //查询预约信息
            orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
            if (orderAppointment != null && com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
                simBuyCommonService.orderTimeout(orderAppointment);
                param = JSONObject.parseObject(orderAppointment.getRequestParam());
                mobile = param.getString("orderDetailSim.phone");
                confId = param.getString("confId");
                planId = param.getString("planId");
                recmdCode = param.getString("recmdCode");
                String CSRFToken = param.getString("CSRFToken");
                model.addAttribute("planId",planId);
                model.addAttribute("CSRFToken",CSRFToken);
                model.addAttribute("transactionId",transactionId);
                model.addAttribute("phone",param.getString("orderDetailSim.phone"));
                if (com.ai.ecs.common.utils.StringUtils.isEmpty(orderAppointment.getCustCertNo())) {
                    writerFlowLogThrowable(streamNo, "", "", "", getClass().getName(),
                            null, String.format("身份证信息为空[%s]", transactionId));
                    throw new RuntimeException("身份验证信息为空，请重新认证！");
                }
            } else {
                writerFlowLogThrowable(streamNo, "", "", "", getClass().getName(),
                        null, String.format("预约订单参数据为空，请重新预约[%s]", transactionId));
                throw new RuntimeException("身份验证信息处理异常，请重新认证！");
            }
        }
        String serviceContract = commonPropagandaService.getCommonPropagandaByCode(CommonPropaganda.SIM_ORDER_PRO, CommonParams.CHANNEL_CODE).getCommonPropagandaContent();
        String simCardPrefix = DictUtil.getDictValue("O2O_GROUP", "SIM_CARD_PREFIX", "1314520818");
        if(StringUtils.isNotEmpty(transactionId)){
            //页面填写的信息要填充进去
            model.addAttribute("contactPhone", param.getString("orderDetailSim.contactPhone"));
            model.addAttribute("iccid", StringUtils.isNotEmpty(param.getString("orderDetailSim.iccid"))?param.getString("orderDetailSim.iccid").substring(10):"");
            model.addAttribute("cityCode", param.getString("orderDetailSim.cityCode"));
            model.addAttribute("onlineBakSn", param.getString("orderDetailSim.onlineBakSn"));
            model.addAttribute("CHANID", param.getString("CHANID"));
            model.addAttribute("memberRecipientCounty", param.getString("memberAddress.memberRecipientCounty"));
        }
        //依据planId获取该档次个性信息
        TfPlans cond = new TfPlans();
        cond.setPlanId(Long.parseLong(planId));
        List<TfPlans> plans = plansService.getPlanList(cond);
        model.addAttribute("mobile", mobile);
        model.addAttribute("plan", plans.get(0));
        model.addAttribute("confId", confId);
        model.addAttribute("recmdCode", recmdCode);
        model.addAttribute("cityName", CITY_NAME_REF.get(cityCode));
        model.addAttribute("serviceContract", serviceContract);
        model.addAttribute("simCardPrefix", simCardPrefix);
        return "web/goods/o2osimgroup/simConfirmOrder";
    }


    @RequestMapping("submitOrder")
    @VerifyCSRFToken
    @ResponseBody
    public ResponseBean submitOrder(HttpServletRequest request, UserGoodsCarModel cModel,String transactionId){
        String streamNo = createStreamNo();
        ResponseBean responseBean = new ResponseBean();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "O2oGroupSimOnline", request.getParameterMap(), "请求参数", request);
        JSONObject param;
        TfOrderAppointment orderAppointment=null;
        try {
            //查询预约信息
            orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
            if (orderAppointment != null && com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
                simBuyCommonService.orderTimeout(orderAppointment);
                param = JSONObject.parseObject(orderAppointment.getRequestParam());
                cModel=simBuyCommonService.disposeOrder(param,streamNo,orderAppointment,cModel);
            } else {
                writerFlowLogThrowable(streamNo,"","","",getClass().getName(),
                        null,String.format("预约订单参数据为空，请重新预约[%s]",transactionId));
                throw new RuntimeException("身份验证信息为空，请重新认证！");
            }
        }catch (EcsException e){
            writerFlowLogThrowable(streamNo,"","","",getClass().getName(),orderAppointment,processThrowableMessage(e));
            throw new RuntimeException("身份验证信息处理异常，请重新认证！");
        }
        try {
            String planId = request.getParameter("planId");
            String confId = request.getParameter("confId");
            String recmdCode = request.getParameter("recmdCode");
            String recmdCode2 = Base64.decode(recmdCode.getBytes());//解码 -> rcd_conf_id
            Long recmdId = Long.valueOf(recmdCode2);//推荐ID
            TfOrderRecmd recmd = recmdMainService.getOrderRecmd(new TfOrderRecmd(recmdId));
            TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();
            orderDetailSim.setRegType("1");//设置证件类型
            orderDetailSim.setTransactionId(transactionId);
            MemberVo member = UserUtils.getLoginUser(request);
            GetEmptyCardInfoByPukCondition condition = new GetEmptyCardInfoByPukCondition();
            condition.setICC_ID(orderDetailSim.getIccid());
            condition.setPUK1(orderDetailSim.getOnlineBakSn());
            JSONObject mapPuk = o2oSimGroupService.getEmptyCardInfoByPuk(condition);
            orderDetailSim.setOrderSubNoAct(mapPuk.getString("DONE_CODE"));//只要不抛出异常就是校验成功随便去个值保存
            if (isProEnv()) {
                simBusiService.oneIdFiveNoVerify(orderDetailSim);
                logger.info("一证五号校验成功！");
            }
            PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
            phoneAttributionModel.setSerialNumber(orderDetailSim.getPhone());
            String cityCode = phoneAttributionService.queryPhoneAttributionAnalysis(phoneAttributionModel);
            //接口异常时 java.lang.ClassCastException: com.alibaba.fastjson.JSONObject cannot be cast to java.util.List
            if(!orderDetailSim.getCityCode().equals(cityCode)){
                throw new Exception(ConstantsBase.MY_EXCEP+"对不起，您所选号码和号码归属地不匹配！");
            }
            //装载
            TfOrderSub tfOrderSub = equipOrderSub(cModel, planId, confId, recmd, member);
            List<TfOrderSub> orderSubList = orderService.newOrder(tfOrderSub);
            TfOrderSub orderSub = orderSubList.get(0);
            simBuyCommonService.saveSimRecmdInfo(recmdCode, orderSub);
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
            request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));//刷新token防重复提交
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "createO2oGroupOrder", null, Exceptions.getStackTraceAsString(e));
            if (e instanceof EcsException) {
                EcsException e1 = (EcsException) e;
                logger.error("接口调用异常", e1);
                responseBean.addError("-1", e1.getFriendlyDesc());
            } else {
                responseBean = ExceptionUtils.dealExceptionRetMsg(responseBean, e, "订单生成失败");
                logger.error("生成订单失败", e);
            }
        }
        return responseBean;
    }

    @RequestMapping("simCollect")
    public String simCollect(HttpServletRequest request, Model model) {
        String referer = request.getHeader("Referer");
        model.addAttribute("referer", referer);
        return "web/goods/o2osimgroup/simCollect";
    }


    private TfOrderSub equipOrderSub(UserGoodsCarModel cModel, String planId, String confId, TfOrderRecmd recmd, MemberVo member) throws Exception {
        TfOrderSub orderSub = new TfOrderSub();
        TfOrderDetailSim detailSim = cModel.getOrderDetailSim();
        TfUserGoodsCar userGoodsCar = cModel.getUserGoodsCarList().get(0);
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
        Shop shop = shopInfoService.getShopIdAndName(recmd.getRecmdPhone());
        orderSub.setShopId(shop.getShopId());//
        orderSub.setShopName(shop.getShopName());
        orderSub.setValetStaffId(recmd.getRecEmpID());//和掌柜员工编号
        orderSub.setDeliveryModeId(1);//一般售卡1备货环节之后进入发货环节(没有缴费上账环节)
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(confId);
        TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
        JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), planId, TfH5SimConf.JSON_ID);
        Long preFee = Long.parseLong(object.getString(TfH5SimConf.JSON_PREFEE));//预存金额
        Long cardFee = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));//卡金额
        Long planRMB = preFee + cardFee;
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
        detailSim.setSynOnliResuIssuc("O2O");//走CreateSimDelegate的方法(集团售卡是第一版号段卡的修改版不要预占)这个要预占要预占
        detailSim.setSynOnliResuMsg("o2oonline");
        detailSim.setCardType(resultPlan.getCardType());
        detailSim.setTerminalType("O2O_GROUP");//判断非网上选号号码池第二次预占
        detailSim.setChnlCodeOut("E050");

        cModel = UserGoodsCarModel.getSimBaseInfo(cModel);
        MemberAddress address = cModel.getMemberAddress();
        address.setMemberRecipientName(detailSim.getRegName());

        detailSim.setPsptAddr(address.getMemberRecipientAddress());

        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        orderSubDetail.setRootCateId(2L);//产品类别(javaBean中定义非空)

        userRef.setEparchyCode(address.getMemberRecipientCity());//归属地市，直接使用收货地址信息
        userRef.setCounty(address.getMemberRecipientCounty());//归属区县，直接使用收货地址信息
        userRef.setContactPhone(detailSim.getPhone());
        userRef.setMemberCreditClass("0"); //用户星级 | 客户类型，为空则为普通用户

        orderSubDetail.setShopId(shop.getShopId());//(javaBean中定义非空但是该字段不会保存如表)
        orderSubDetail.setShopName(shop.getShopName());//(javaBean中定义非空但是该字段不会保存如表)
        orderSubDetail.setShopTypeId(5);//(javaBean中定义非空但是该字段不会保存如表)

        orderSubDetail.setGoodsName("O2O集团售卡订单");// 商品信息(javaBean中定义非空)
        orderSubDetail.setGoodsImgUrl("www.wap.hn.10086.cn/shop");//默认商城的首页
        orderSubDetail.setGoodsRemark(userGoodsCar.getGoodsRemark());//备注
        orderSubDetail.setGoodsSkuId(userGoodsCar.getGoodsSkuId());
        orderSubDetail.setGoodsSkuPrice(planRMB);//在生成订单是自动设置tfOrderSub的支付金额(javaBean中定义非空)
        orderSubDetail.setGoodsSkuNum(1L);//每个订单只能一个号码(javaBean中定义非空 订单生成过程中用skuNum * skuPrice)
        orderSubDetail.setGoodsSkuId(Long.parseLong(planId));//保存套餐的planId(javaBean中定义非空)
        orderSubDetail.setGoodsId(Long.parseLong(detailSim.getPhone()));//号卡订单:号码作为GoodsId(javaBean中定义非空)
        orderSubDetail.setGoodsFormat(detailSim.getPhone());//ecop展示时用这个字段
        orderSubDetail.setGoodsUrl(confId);//号卡订单:confId保存在goodsFormat中(javaBean中定义非空)
        orderSubDetail.setOrderDetailSim(detailSim);//装配sim订单信息
        orderSub.addOrderDetail(orderSubDetail);//装配订单详情信息
        orderSub.setOrderUserRef(userRef);//装配用户信息
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
        orderSub.setOrderChannelCode("E050");//渠道编码,供和掌柜查询(javaBean中定义渠道非空)
        orderSub.setPayModeId(cModel.getPayMode().getPayModeId());//支付方式
        return orderSub;
    }


}
