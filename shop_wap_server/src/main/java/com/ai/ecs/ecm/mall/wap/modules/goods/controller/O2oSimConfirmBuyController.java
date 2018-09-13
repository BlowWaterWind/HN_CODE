package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.JsonUtil;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.SimBuyCommonService;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.o2osim.entity.GetPhoneByCardCondition;
import com.ai.ecs.ecsite.modules.o2osim.entity.SerialNumberOut;
import com.ai.ecs.ecsite.modules.o2osim.service.O2oSimService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.ICommonPropagandaService;
import com.ai.ecs.goods.entity.CommonPropaganda;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oPreOrderService;
import com.ai.ecs.o2o.entity.O2oPreOrder;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.busi.IOrderAddonProductService;
import com.ai.ecs.order.api.busi.IOrderAppointmentService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.*;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.modules.goods.controller.GoodsBuyController.confirmOrderBaseInfo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.processThrowableMessage;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogEnterMenthod;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogThrowable;

/**
 * Created by cc on 2018/1/31
 *
 * @author xiejq
 *         用户选号码生成订单
 *
 *
 * 号卡订单:
 * TF_ORDER_DETAIL_SIM的chnlCodeOut必填E050
 * TF_ORDER_SUB_DETAIL 的GoodsFormat 填客户选的号码(Ecop订单查询时显示)
 * TF_ORDER_SUB_DETAIL 的GoodsUrl填confId
 */
@Controller
@RequestMapping("simConfirm")
public class O2oSimConfirmBuyController extends RecmdDefaultController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private O2oPreOrderService o2oPreOrderService;

    @Autowired
    private ICommonPropagandaService commonPropagandaService;

    @Autowired
    private O2oSimService o2oSimInterfaceService;

    @Autowired
    private IOrderAddonProductService orderAddonProductService;

    @Autowired
    private IOrderAppointmentService orderAppointmentService;

    @Autowired
    private SimBuyCommonService simBuyCommonService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;


    private static final String ORDER_COMPLETED = "1";

    private static final String ORDER_FAILED = "2";

    private static final String ORDER_CREATE = "0";

    private static final String ORDER_USER_CANCEL = "3";

    private static final String CREATE_PROMPT = "您存在24小时未选号的预订单,您可以在湖南移动微厅->办卡和实名认证->订单查询直接进行选号";


    @Value("${realNameAuthPath}")
    private String realNameAuthPath;

    @Value("${realNameCallBackUrlPath}")
    private String realNameCallBackUrlPath;

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

    @RequestMapping("toSelectPage")
    public String toSelectPage(O2oPreOrder cond, Model model, String transactionId) {
        String streamNo = createStreamNo();
        TfOrderAppointment orderAppointment;
        JSONObject param;
        if (StringUtils.isNotEmpty(transactionId)) {
            //查询预约信息
            orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
            if (orderAppointment != null && com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
                simBuyCommonService.orderTimeout(orderAppointment);
                param = JSONObject.parseObject(orderAppointment.getRequestParam());
                cond.setOrderPreId(param.getString("orderPreId"));
                cond.setContactPhone(param.getString("contactPhone"));
                cond.setUserName(orderAppointment.getCustName());
                cond.setCardId(orderAppointment.getCustCertNo());
                o2oPreOrderService.updatePreOrder(cond);
                model.addAttribute("contactPhone",param.getString("contactPhone"));
                model.addAttribute("transactionId",transactionId);
                model.addAttribute("phone",param.getString("orderDetailSim.phone"));
                model.addAttribute("confId",param.getString("confId"));
                if (StringUtils.isEmpty(orderAppointment.getCustCertNo())) {
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
        cond.setStatus(ORDER_CREATE);
        List<O2oPreOrder> o2oPreOrders = o2oPreOrderService.selectCreateOrFail(cond);
        if (o2oPreOrders.isEmpty()) {
            throw new RuntimeException("您的预约订单已24小时过期或者不存在此订单号,请确认后再试!");
        }
        O2oPreOrder o2oPreOrder = o2oPreOrderService.selectByPrimaryKey(cond.getOrderPreId());
        String status = o2oPreOrder.getStatus();
        if (ORDER_COMPLETED.equals(status)) {
            throw new RuntimeException("您的预约订单完成选号,请确认后再试!");
        }
        //协议
        String serviceContract = commonPropagandaService.getCommonPropagandaByCode(CommonPropaganda.SIM_ORDER_PRO, CommonParams.CHANNEL_CODE).getCommonPropagandaContent();
        String simCardNo = o2oPreOrder.getSimCardNo();
        model.addAttribute("o2oPreOrder", o2oPreOrder);
        model.addAttribute("serviceContract", serviceContract);
        model.addAttribute("simCardNo", simCardNo);
        model.addAttribute("provinceName", "湖南省");
        model.addAttribute("eparchyName", CITY_NAME_REF.get(o2oPreOrder.getSimEparchyCode()));
        model.addAttribute("cityName", o2oPreOrder.getCityName());
        return "web/goods/o2osim/chooseNum";
    }


    /**
     * 异步请求号码
     * 根据sim卡卡号查询号码
     */
    @RequestMapping("getQueryNumsH5Online")
    @ResponseBody
    public Object getQueryNumsH5Online(String orderPreId, String tailNum) throws Exception {
        ResponseBean resp = new ResponseBean();
        try {
            O2oPreOrder o2oPreOrder = o2oPreOrderService.selectByPrimaryKey(orderPreId);
            TfH5SimConf simConf = new TfH5SimConf();
            simConf.setConfId(o2oPreOrder.getConfId());
            List<TfH5SimConf> confList = plansService.queryListCond(simConf);
            GetPhoneByCardCondition condition = new GetPhoneByCardCondition();
            if (OrderConstant.O2O_TYPE_XYYX.equals(confList.get(0).getExtContent2())) {
                condition.setNewHaoduanka("Y");
            }
            condition.setSimCardNo(o2oPreOrder.getSimCardNo());
            if ("".equals(tailNum)) {
                condition.setSerialNumber(null);
            } else {
                condition.setSerialNumber(tailNum);
            }
            Set<SerialNumberOut> serialList = o2oSimInterfaceService.getPhoneByCard(condition, tailNum);
            if (serialList == null || serialList.isEmpty()) {
                resp.addError("-1", "对不起您输入的sim卡号无对应的号码");
            } else {
                resp.addSuccess(serialList);
            }
        } catch (Exception e) {
            if(e instanceof EcsException){
                EcsException e1 = (EcsException)e;
                resp.addError("-1",e1.getFriendlyDesc());
            }else{
                splitException(e, e.getMessage());
            }
            logger.error("根据simCardNo查询号码失败", e);
        }
        return resp;
    }

    /**
     * 预约采集信息验签接口
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "payAppointment")
    @ResponseBody
    public ResponseBean payAppointment(HttpServletRequest request, HttpServletResponse response, UserGoodsCarModel cModel) {
        String streamNo = createStreamNo();
        ResponseBean resp = new ResponseBean();
        try {
            Map<String, String> param = UppCore.getHashMapParam(request.getParameterMap());
            String reqParam = JSONObject.toJSONString(param);
            writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(),
                    "payAppointment", request.getParameterMap(), "预约下单", request);
            TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();
            String confId = param.get("confId");
            TfH5SimConf resultPlan;
            TfH5SimConf simConf = new TfH5SimConf();
            simConf.setConfId(confId);
            List<TfH5SimConf> confList = plansService.queryListCond(simConf);
            resultPlan = confList.get(0);
            PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
            phoneAttributionModel.setSerialNumber(orderDetailSim.getPhone());
            String cityCodeNew = phoneAttributionService.queryPhoneAttributionAnalysis(phoneAttributionModel);
            if(param.get("orderDetailSim.cityCode").equals(cityCodeNew)){
                resp.addError("-1","号码与sim卡归属不一致,请确认后再试!");
            }
            //号码第一次选占
            if (OrderConstant.O2O_TYPE_XYYX.equals(resultPlan.getExtContent2())) { //标识为校园营销
                orderDetailSim.setTerminalType(OrderConstant.O2O_TYPE_XYYX); //存放校园营销标记
                simBusiService.selTempOccupyRes(orderDetailSim); //号码第一次选占，校园营 销号段卡只做第一次选占
            }
            String callBackUrl = "";
            callBackUrl = realNameCallBackUrlPath + "/simConfirm/toSelectPage?transactionId=";
            String requestStr = simBuyCommonService.preordainCheck(orderDetailSim.getPhone(), reqParam, callBackUrl,streamNo);
            resp.addSuccess("成功", realNameAuthPath + requestStr);
            logger.info(JSONObject.toJSONString(resp));
        } catch (Exception e) {
            logger.error("出错",e);
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(),
                    "payAppointment-error", null, "预约发起失败:" + processThrowableMessage(e));
            resp = ExceptionUtils.dealExceptionRetMsg(resp, e, "预约发起失败");
        }

        return resp;
    }


    /**
     * 1. 空指针异常的e.getMessage为NULL,信息在DefaultHandlerMethodExceptionResolver中装配传给页面,会过滤掉null的值。data.message就为undefined了
     * 2. 对于自定的异常 throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡销售已经下线！"); 配合使用
     * ExceptionUtils.dealExceptionRetMsg(responseBean, e, "订单生成失败"); 方法里的substring操作容易再次抛异常
     *
     * @param request
     * @param cModelOrigin
     * @return
     */
    @RequestMapping("/createOrder")
    @ResponseBody
    public ResponseBean createOrder(HttpServletRequest request, UserGoodsCarModel cModelOrigin) {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(), "createOrder", request.getParameterMap(), "下单", request);
        ResponseBean responseBean = new ResponseBean();
        String orderPreId = request.getParameter("orderPreId");
        O2oPreOrder o2oPreOrder = o2oPreOrderService.selectByPrimaryKey(orderPreId);
        String transactionId=request.getParameter("transactionId");
        JSONObject param;
        TfOrderAppointment orderAppointment=null;
        try {
            //查询预约信息
            orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
            if (orderAppointment != null && com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
                simBuyCommonService.orderTimeout(orderAppointment);
                param = JSONObject.parseObject(orderAppointment.getRequestParam());
                cModelOrigin=simBuyCommonService.disposeOrder(param,streamNo,orderAppointment,cModelOrigin);
            } else {
                writerFlowLogThrowable(streamNo,"","","",getClass().getName(),
                        null,String.format("预约订单参数据为空，请重新预约[%s]",transactionId));
                throw new RuntimeException("身份验证信息为空，请重新认证！");
            }
        }catch (EcsException e){
            writerFlowLogThrowable(streamNo,"","","",getClass().getName(),orderAppointment,processThrowableMessage(e));
            throw new RuntimeException("身份验证信息处理异常，请重新认证！");
        }

        MemberVo member = UserUtils.getLoginUser(request);
        //放入号卡固定信息
        TfOrderDetailSim orderDetailSim = cModelOrigin.getOrderDetailSim();
        //标识为O2O
        orderDetailSim.setSynOnliResuIssuc(OrderConstant.ONLI_RESU_ISSUC_O2O);
        UserGoodsCarModel cModel = UserGoodsCarModel.getSimBaseInfo(cModelOrigin);
        if (!o2oPreOrder.getSimEparchyCode().equals(orderDetailSim.getCityCode())) {
            throw new RuntimeException("您所选的sim卡归属地与号码不一致,请确认后再试!");
        }
        orderDetailSim.setPsptId(o2oPreOrder.getCardId());
        try {
            checkCardId(o2oPreOrder.getConfId(),orderDetailSim.getPsptId());
            if (isProEnv()) {
                //从在线公司回来的时候判断一证五号
                simBusiService.oneIdFiveNoVerify(orderDetailSim);
                logger.info("一证五号校验成功！");
            }
            TfOrderSub orderSub = equipOrderSub(cModel,o2oPreOrder,member);

            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
            TfOrderSub orderSubResult = orderSubList.get(0);
            o2oPreOrder.setOrderSubNo(orderSubResult.getOrderSubNo());
            o2oPreOrder.setStatus(ORDER_COMPLETED);
            o2oPreOrderService.updatePreOrder(o2oPreOrder);
            if (o2oPreOrder.getReCmdCode() != null) {
                simBuyCommonService.saveSimRecmdInfo(o2oPreOrder.getReCmdCode(), orderSubResult);
            }
            Map<String, String> map = new HashMap<>();
            if (orderSubResult.getOrderSubPayAmount() > 0) {
                map.put("flag", "toPay");
                map.put("cardId", o2oPreOrder.getCardId());
                map.put("thirdSubId", orderSubResult.getOrderSubNo());
            } else {
                map.put("flag", "unPay");
                map.put("cardId", o2oPreOrder.getCardId());
                map.put("thirdSubId", orderSubResult.getOrderSubNo());
            }
            responseBean.addSuccess(map);
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, "", "",
                    getClass().getName(), "createOrder", "订单生成失败", processThrowableMessage(e));
            printKaError2("createOrder：", request, e);
            if(e instanceof EcsException){
                EcsException e1 = (EcsException)e;
                responseBean.addError("-1",e1.getFriendlyDesc());
            }else{
                o2oPreOrder.setStatus(ORDER_FAILED);
                o2oPreOrderService.updatePreOrder(o2oPreOrder);
                logger.error("生成订单失败", e);
                responseBean = ExceptionUtils.dealExceptionRetMsg(responseBean, e, "订单生成失败");
            }
        }
        return responseBean;
    }



    @RequestMapping("orderSuccess")
    public String orderSuccess(String thirdSubId, Model model) {
        O2oPreOrder cond = new O2oPreOrder();
        cond.setOrderSubNo(thirdSubId);
        List<O2oPreOrder> o2oPreOrders = o2oPreOrderService.getPreOrder(cond);
        if (o2oPreOrders.isEmpty()) {
            throw new RuntimeException("无对应的订单编号,请确认后再试!");
        }
        model.addAttribute("o2oPreOrder", o2oPreOrders.get(0));
        return "web/goods/o2osim/orderSuccess";
    }


    @RequestMapping("deleteOrder")
    @ResponseBody
    public ResponseBean deleteOrder(O2oPreOrder o2oPreOrder) {
        ResponseBean responseBean = new ResponseBean();
        o2oPreOrder.setStatus(ORDER_USER_CANCEL);
        o2oPreOrder.setUpdateDate(new Date());
        int num = o2oPreOrderService.cancelOrder(o2oPreOrder);
        if (num > 0) {
            responseBean.addSuccess(o2oPreOrder.getOrderPreId());
        } else {
            responseBean.addError("-1", "订单取消失败,请稍后再试!");
        }
        return responseBean;
    }

    private TfOrderSub equipOrderSub(UserGoodsCarModel cModel,O2oPreOrder o2oPreOrder,MemberVo member) throws Exception{
        TfOrderSub orderSub = new TfOrderSub();
        TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();
        orderSub.setPhoneNumber(orderDetailSim.getPhone().substring(0, 11));
        orderSub.setOrderTypeId(OrderConstant.TYPE_SIM);
        MemberLogin memberLogin;
        TfOrderUserRef userRef = new TfOrderUserRef();
        if (member == null) {//未登录
            orderDetailSim.setIsCreateUser("RE");//分享办理
            orderDetailSim.setMemberId(1234567890L);
            orderDetailSim.setCustName(orderDetailSim.getRegName());
            userRef.setMemberId(1234567890L);
            userRef.setMemberLogingName(orderDetailSim.getContactPhone()); //归属地市，直接使用收货地址信息
        } else {
            orderDetailSim.setIsCreateUser("AP");
            memberLogin = member.getMemberLogin();
            orderDetailSim.setMemberId(memberLogin.getMemberId());
            orderDetailSim.setCustName(memberLogin.getMemberLogingName());
            userRef.setMemberId(memberLogin.getMemberId());
            userRef.setMemberLogingName(memberLogin.getMemberLogingName()); //归属地市，直接使用收货地址信息
        }

        //归属地市，直接使用收货地址信息
        userRef.setEparchyCode(orderDetailSim.getCityCode());
        //归属区县，直接使用收货地址信息
        userRef.setCounty("岳麓区");
        userRef.setContactPhone(orderDetailSim.getContactPhone());
        //用户星级 | 客户类型，为空则为普通用户
        userRef.setMemberCreditClass("0");
        orderSub.setOrderUserRef(userRef);
        orderSub.setThirdPartyId(o2oPreOrder.getOrderPreId());
        orderSub.setShopId(o2oPreOrder.getShopId() == null ? 1L : o2oPreOrder.getShopId());
        //== 支付金额
        orderSub = confirmOrderBaseInfo(cModel, orderSub);
        Long preFee = 0L;
        Long cardFee = 0L;
        Long planRMB = 0L;
        TfH5SimConf resultPlan;
        TfH5SimConf simConf = new TfH5SimConf();
        simConf.setConfId(o2oPreOrder.getConfId());
        List<TfH5SimConf> confList = plansService.queryListCond(simConf);
        resultPlan = confList.get(0);
        if (!confList.isEmpty() && null != o2oPreOrder.getPlanId()) {
            JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), String.valueOf(o2oPreOrder.getPlanId()), TfH5SimConf.JSON_ID);
            preFee = Long.parseLong(object.getString("preFee"));
            cardFee = Long.parseLong(object.getString("cardFee"));
            planRMB = preFee + cardFee;
        }
        orderSub.setOrderSubPayAmount(planRMB);//流程中需要用到，必须设置
        orderSub.setOrderSubAmount(planRMB);//号卡订单SubAmount和SubPayAmount相同
        TfPlans planCond = new TfPlans(); //== 处理TfOrderDetailSim
        TfPlans plan;
        if (!OrderConstant.O2O_TYPE_XYYX.equals(resultPlan.getExtContent2())) {//根据校园营销的类型
            planCond.setPlanId(o2oPreOrder.getPlanId());
            List<TfPlans> plans = plansService.getPlanList(planCond);
            if (plans.isEmpty()) {
                throw new RuntimeException("网络异常");
            }
            plan = plans.get(0);
            String cityCode = orderDetailSim.getCityCode();
            String cityCodeSuffix = cityCode.substring(2);
            String productId = plan.getProductId().replace("**", cityCodeSuffix);
            String baseSet = plan.getPlanCode().replace("**", cityCodeSuffix);
            orderDetailSim.setSimProductId(productId);
            orderDetailSim.setBaseSet(baseSet);//套餐编码
            orderDetailSim.setBaseSetName(plan.getPlanName()); //套餐名称
        } else {
            orderDetailSim.setBaseSet(o2oPreOrder.getPlanCode());
            orderDetailSim.setBaseSetName(o2oPreOrder.getPlanName());
            plan = new TfPlans();
            plan.setPlanName(o2oPreOrder.getPlanName());
        }

        orderDetailSim.setIccid(o2oPreOrder.getSimCardNo());
        orderDetailSim.setPreFee(preFee);
        orderDetailSim.setCardFee(cardFee);
        orderDetailSim.setPhoneType(TfOrderDetailSim.PT_PU_NUM);//普号
        orderDetailSim.setPsptAddr("湖南省长沙市岳麓区");
        orderDetailSim.setContactPhone(o2oPreOrder.getContactPhone());
        TfUserGoodsCar car = cModel.getUserGoodsCarList().get(0);//子订单详情信息
        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        orderSubDetail.setRootCateId(2L); //产品类别
        orderDetailSim.setChnlCodeOut("E050");
        orderSubDetail.setShopId(o2oPreOrder.getShopId());
        orderSubDetail.setShopName(car.getShopName());
        orderSubDetail.setShopTypeId(car.getShopTypeId());
        orderSubDetail.setGoodsName(car.getGoodsName()); // 商品信息
        orderSubDetail.setGoodsImgUrl(car.getGoodsSkuImgUrl());
        orderSubDetail.setGoodsRemark(car.getGoodsRemark());  //备注
        orderSubDetail.setGoodsSkuId(car.getGoodsSkuId());
        orderSubDetail.setGoodsSkuPrice(planRMB);
        orderSubDetail.setGoodsSkuNum(1L); //每个订单只能一个号码
        orderSubDetail.setProdSkuId(Long.parseLong(orderDetailSim.getPhone()));
        orderSubDetail.setGoodsSkuId(Long.parseLong(orderDetailSim.getPhone()));  //号卡订单：号码作为SKUID
        orderSubDetail.setGoodsId(Long.parseLong(orderDetailSim.getPhone())); //号卡订单：号码作为GoodsId
        orderSubDetail.setGoodsFormat(orderDetailSim.getPhone()); //号卡订单：号码作为商品规格
        orderSubDetail.setGoodsUrl(o2oPreOrder.getConfId());
        orderSubDetail.setOrderDetailSim(orderDetailSim);
        orderSub.addOrderDetail(orderSubDetail);
        if (OrderConstant.O2O_TYPE_XYYX.equals(resultPlan.getExtContent2())) { //标识为校园营销
            orderDetailSim.setTerminalType(OrderConstant.O2O_TYPE_XYYX); //存放校园营销标记
        }
        return orderSub;
    }

    private void checkCardId(String confId,String cardId) throws EcsException{
        O2oPreOrder orderParam = new O2oPreOrder();
        orderParam.setCardId(cardId);
        orderParam.setStatus(ORDER_COMPLETED);
        List<O2oPreOrder> o2oPreCompeleted = o2oPreOrderService.selectCreateOrFail(orderParam);
        if(o2oPreCompeleted.size() > 3){
            throw new EcsException(-1,"同一身份证号30天内最多申请三张");
        }


        String isFreshMan = DictUtil.getDictValue(confId, "IS_FRESHMAN", "0");
        try{
            if ("1".equals(isFreshMan) && !checkIsGraduate(cardId)) {//不是95到05之间
                throw  new EcsException(-1, "该优惠只适合于95到05之间的身份证办理哟!");
            }
        }catch (ParseException e){
            throw new EcsException(-1,"解析异常!");
        }

    }

    private boolean checkIsGraduate(String num) throws ParseException {
        if (num.length() == 18) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date age = sdf.parse(num.substring(6, 14));
            Date downAge = sdf.parse("19941231");
            Date upAge = sdf.parse("20051231");
            return age.after(downAge) && age.before(upAge);
        }
        return false;
    }

    @RequestMapping("testAndmu")
    public void testAndmu() {
        orderAddonProductService.buyAndmuGiveCloud();
    }
}
