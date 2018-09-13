package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Base64;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.Exceptions;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.JsonUtil;
import com.ai.ecs.common.web.Servlets;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.common.security.FormAuthenticationFilter;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.SimBuyCommonService;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.CSRFTokenUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.RealNameMsDesPlus;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNum;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNumQueryCondition;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.sim.entity.SynKeyRefreshCond;
import com.ai.ecs.ecsite.modules.sim.entity.SynSimOrder2OnlineCond;
import com.ai.ecs.ecsite.modules.sim.service.ISynKeyRefreshService;
import com.ai.ecs.ecsite.modules.sim.service.ISynSimOrder2OnlineService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.ICommonPropagandaService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.CommonPropaganda;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.TfPlans;
import com.ai.ecs.goods.entity.TfPlansCodeRef;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.IWtjkzqUsrDetailService;
import com.ai.ecs.member.api.register.IRegisterService;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.merchant.company.ICompanyUserService;
import com.ai.ecs.merchant.entity.O2oAccountInfo;
import com.ai.ecs.merchant.entity.TdEcOrgInfo;
import com.ai.ecs.merchant.shop.ShopManage;
import com.ai.ecs.merchant.shop.service.IShopInfoService;
import com.ai.ecs.order.api.IOrderConfigService;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.ITfSimOrderLogService;
import com.ai.ecs.order.api.busi.IOrderAppointmentService;
import com.ai.ecs.order.api.busi.ISimBusiService;
import com.ai.ecs.order.api.recmd.IFlowCouponsGiveService;
import com.ai.ecs.order.api.recmd.IOrderNewBusiService;
import com.ai.ecs.order.api.recmd.IRecmdMainService;
import com.ai.ecs.order.constant.ExceptionOrderCode;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderAppointment;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.TfOrderLog;
import com.ai.ecs.order.entity.TfOrderPushKey;
import com.ai.ecs.order.entity.TfOrderRecipient;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.ai.ecs.order.entity.TfSimOrderLog;
import com.ai.ecs.order.entity.recmd.TfCouponsConf;
import com.ai.ecs.order.entity.recmd.TfFlowcouponsGive;
import com.ai.ecs.order.entity.recmd.TfOrderChnlInfo;
import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import com.ai.ecs.order.entity.recmd.TfOrderRecmdRef;
import com.ai.ecs.order.entity.recmd.TfOrderSimBusi;
import com.ai.ecs.order.entity.recmd.TfOrderSimBusiConf;
import com.ai.ecs.order.entity.recmd.TfRecmdActConf;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.ecs.utils.LogUtils;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ai.ecs.ecm.mall.wap.modules.goods.controller.GoodsBuyController.confirmOrderBaseInfo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.objectToMap;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.processThrowableMessage;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogConditionAndReturn;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogEnterMenthod;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogExitMenthod;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogThrowable;

/**
 * H5号卡销售
 * Created by hewei on 2017/9/1/001.
 * GoodsBuyContoller内容太多，分布一部分到这里来
 */
@Controller
@RequestMapping("simBuy")
public class SimBuyController extends RecmdDefaultController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IMemberAddressService memberAddressService;
    @Autowired
    private IPlansService plansService;
    @Autowired
    private IGoodsManageService goodsManageService;
    @Autowired
    private ISimBusiService simBusiService;
    @Autowired
    private IMemberLoginService loginService;
    @Autowired
    private IRegisterService registerService;
    @Autowired
    private NetNumServerService netNumServerService;
    @Autowired
    private ShopManage shopManage;
    @Autowired
    private ICommonPropagandaService commonPropagandaService;
    @Autowired
    private IOrderQueryService orderQueryService;
    @Autowired
    private ISynSimOrder2OnlineService synSimOrder2OnlineService;
    @Autowired
    private PhoneAttributionService phoneAttributionService;
    @Autowired
    private BasicInfoQryModifyService basicInfoQryModifyService;
    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private IRecmdMainService recmdMainService;
    @Autowired
    private IFlowCouponsGiveService flowCouponsGiveService;
    @Autowired
    private ITfSimOrderLogService orderLogService;
    @Autowired
    private IOrderConfigService orderConfigService;
    @Autowired
    private IOrderNewBusiService orderNewBusiService;
    @Autowired
    private IWtjkzqUsrDetailService wtjkzqUsrDetailService;

    @Autowired
    private IShopInfoService shopInfoService;

    @Autowired
    private SimBuyCommonService simBuyCommonService;

    @Autowired
    private ISynKeyRefreshService synKeyRefreshService;

    @Autowired
    private IOrderAppointmentService orderAppointmentService;

    @Autowired
    private ICompanyUserService companyUserService;

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef"
            + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";


    @Value("${orderPayUrl}")
    String orderPayUrl;
    @Value("${afterOrderPayUrl}")
    String afterOrderPayUrl;
    @Value("${discountTime}")
    String discountTime;
    @Value("${financialTest}")
    String financialTest;
    @Value("${didiclientId}")
    String didiclientId;
    @Value("${recmdSimTimeInterval}")
    String recmdSimTimeInterval;

    @Value("${realNameAuthPath}")
    String realNameAuthPath;
    @Value("${realNameCallBackUrlPath}")
    String realNameCallBackUrlPath;


    private static final String CHNL_E050 = "E050";
    private static final String URL_PAGE_ONLINE = "online";
    private static final String URL_PAGE_O2O = "o2oOnline";
    private static final String URL_PAGE_GROUP = "o2oGroup";

    private int month;
    private final static String O2O_GROUP = "18";


    /**
     * 跳转h5在线售卡申请页面
     */
    @RequestMapping("/simH5OnlineToApply")
    public String simh5OnlineToApply(TfH5SimConf cond, Model model, HttpServletRequest request) throws Exception {
        TfH5SimConf confResult = new TfH5SimConf();
        String streamNo = createStreamNo();
        List<TfPlans> plans = Lists.newArrayList();
        writerFlowLogEnterMenthod(streamNo, "", "", this.getClass().getName(),
                "simh5OnlineToApply", request.getParameterMap(), "跳转h5在线售卡申请页面", request);
        try {
            if (cond.getConfId() == null || cond.getConfId().length() == 0) {
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "套餐信息配置参数错误！");
            }
            List<TfH5SimConf> h5Confs = plansService.queryListCond(cond);

            if (h5Confs.size() == 0) {
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡配置信息不存在！");
            }
            confResult = h5Confs.get(0);

            if (confResult != null) {
                //标志为和掌柜配置套餐
                String flag = "1";
                if (flag.equals(confResult.getExtContent2()) && CHNL_E050.equals(confResult.getChnlCodeOut())) {
                    MemberVo member = UserUtils.getLoginUser(request);
                    List<Map<String, String>> planList = shopInfoService.getShopSimPlans(member.getShopInfo().getShopId(), "SIM_" + confResult.getConfId());
                    plans = plansService.queryPlanListByH5SimConfOnO2o(planList);
                } else {
                    plans = plansService.queryPlanListByH5SimConf(confResult.getPlanJson());
                }
            }
            if (confResult.getConfStatus().equals(TfH5SimConf.ConfStatus.SC_OFFLINE.getValue())) {
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "该号卡销售已经下线！");
            }
            //套餐信息
            Map confPlansMap = Maps.newConcurrentMap();
            for (TfPlans p : plans) {
                JSONObject object = JsonUtil.queryMapInfoById(confResult.getPlanJson(), p.getPlanId() + "", TfH5SimConf.JSON_ID);
                confPlansMap.put(p.getPlanId(), object.getString(TfH5SimConf.JSON_PLANSNAME));
            }
            //== 是否参与推荐活动
            String rcdConfId = confResult.getRcdConfId();
            String rstFlag = "";
            if (!StringUtils.isEmpty(rcdConfId)) {
                TfRecmdActConf recmdActConf = recmdMainService.getRecmdActConf(rcdConfId);
                rstFlag = isAllowRecmdV2(recmdActConf);
            }
            //O2O集团需求特殊处理
            if (O2O_GROUP.equals(confResult.getPageCode())) {
                String recmdCode2 = Base64.decode(cond.getRecmdCode().getBytes());//解码 -> rcd_conf_id
                Long recmdId = Long.valueOf(recmdCode2);//推荐ID
                TfOrderRecmd recmd = recmdMainService.getOrderRecmd(new TfOrderRecmd(recmdId));
                O2oAccountInfo accountInfo = companyUserService.queryo2oAccountInfo(recmd.getRecmdPhone());
                model.addAttribute("cityCode", recmd.getCityCode());
                model.addAttribute("departId", accountInfo.getDepartId());
            }
            model.addAttribute("templateId", DictUtil.getDictValue(confResult.getCardType(), "O2O_qyyx", "2018080801"));
            model.addAttribute("isAllowRecmd", rstFlag);
            model.addAttribute("confPlansMap", confPlansMap);
            model.addAttribute("conf", confResult);
            model.addAttribute("cond", cond);
            model.addAttribute("plans", plans);
            model.addAttribute("CHANID", request.getParameter("CHANID"));
            model.addAttribute("shopId", request.getParameter("shopId") == null || request.getParameter("shopId").equals("") ? "1" : request.getParameter("shopId"));
            Session session = UserUtils.getSession();
            session.setAttribute("shopId", request.getParameter("shopId") == null || request.getParameter("shopId").equals("") ? "1" : request.getParameter("shopId"));

        } catch (Exception e) {
            e.printStackTrace();
            printKaError("号卡销售提交信息错误", e);
            splitException(e, "号卡销售提交信息错误！");
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "simh5OnlineToApply", null, Exceptions.getStackTraceAsString(e));
        } finally {

            writerFlowLogExitMenthod(streamNo, "", "", this.getClass().getName(), "simh5OnlineToApply", objectToMap(plans), "跳转h5在线售卡申请页面");
        }
        return "web/goods/sim/applySimH5OnlineV2-" + confResult.getPageCode();
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
            writerFlowLogEnterMenthod(streamNo,"",cModel.getOrderDetailSim().getPhone(),getClass().getName(),
                    "payAppointment",request.getParameterMap(),"预约下单",request);
            TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();
            validatePhone(streamNo, orderDetailSim, orderDetailSim.getCityCode());
            //== 号码第一次选占
            String urlPage = request.getParameter("urlPage");
            if(URL_PAGE_O2O.equals(urlPage)){
                orderDetailSim.setTerminalType("XYYX");
            }
            if(URL_PAGE_GROUP.equals(urlPage)){
                String recmdCode2 = Base64.decode(request.getParameter("recmdCode").getBytes());//解码 -> rcd_conf_id
                Long recmdId = Long.valueOf(recmdCode2);//推荐ID
                TfOrderRecmd recmd = recmdMainService.getOrderRecmd(new TfOrderRecmd(recmdId));
                O2oAccountInfo accountInfo = companyUserService.queryo2oAccountInfo(recmd.getRecmdPhone());
                if(!"00310".equals(accountInfo.getDepartId())){
                    orderDetailSim.setTerminalType("XYYX");
                }
            }
            simBusiService.selTempOccupyRes(orderDetailSim);

            String callBackUrl = "";
            if (URL_PAGE_ONLINE.equals(urlPage)) {
                callBackUrl = realNameCallBackUrlPath + "/simBuy/simH5OnlineToConfirmOrder?transactionId=";
            }
            if (URL_PAGE_O2O.equals(urlPage)) {
                callBackUrl = realNameCallBackUrlPath + "/o2oSimOnline/simO2OH5OnlineToConfirmOrder?transactionId=";
            }
            if (URL_PAGE_GROUP.equals(urlPage)) {
                callBackUrl = realNameCallBackUrlPath + "/simWholenet/toConfirm?transactionId=";
            }

            String requestStr = simBuyCommonService.preordainCheck(orderDetailSim.getPhone(), reqParam, callBackUrl,streamNo);


            resp.addSuccess("成功", realNameAuthPath + requestStr);
            logger.info(JSONObject.toJSONString(resp));
        }catch (Exception e){
            writerFlowLogThrowable(streamNo, "", cModel.getOrderDetailSim().getPhone(), this.getClass().getName(),
                    "simBuy-payAppointment-error", null, "预约发起失败:" + processThrowableMessage(e));
            resp = ExceptionUtils.dealExceptionRetMsg(resp, e, "预约发起失败");
        }

        return resp;
    }



    /**
     * 验签秘钥更新接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "SynkeyRefresh")
    @ResponseBody
    public ResponseBean SynkeyRefresh(HttpServletRequest request,HttpServletResponse response, UserGoodsCarModel cModel) {
        String streamNo = createStreamNo();
        ResponseBean resp=new ResponseBean();
        try{
            String pwd=request.getParameter("pwd");
            if(!"6c663bfc6ece0610861d8cd36cc81ca3".equals(pwd)){
                resp.addError("pwd error");
                return resp;
            }
            Map<String, String> param = UppCore.getHashMapParam(request.getParameterMap());
            String reqParam = JSONObject.toJSONString(param);

            String busiCode = "RSA_KEY_FRESH";//业务编码
            String targetCode = "1085";//落地方编码
            String sourceCode = "731009";//请求源编码
            String version = "1.0";//报文版本


            /**
             * 验签秘钥更新接口
             */
            SynKeyRefreshCond cond = new SynKeyRefreshCond();
            cond.setBusiCode(busiCode);
            cond.setSourceCode(sourceCode);
            cond.setTargetCode(targetCode);
            cond.setVersion(version);
            Map<String, Object> map = new HashMap<>();
            map = synKeyRefreshService.synKeyRefresh(cond);

            String returnCode = "";
            String returnMessage;
            String privateKey="";
            if (map != null) {
                Map map0 = (Map) map.get("result");
                if (map0 != null && map0.size() > 0) {
                    returnCode = String.valueOf(map0.get("returnCode"));
                    returnMessage = String.valueOf(map0.get("returnMessage"));
                    privateKey = String.valueOf(map0.get("privateKey"));
                }
            }

            TfOrderPushKey orderPushkeyCond = new TfOrderPushKey();
            String keyType = "1";//密钥类型(1:签名2:加密)
            orderPushkeyCond.setBusiCode(busiCode);
            orderPushkeyCond.setSourceCode(sourceCode);
            orderPushkeyCond.setTargetCode(targetCode);
            orderPushkeyCond.setVersion(version);

            orderPushkeyCond.setKey(privateKey);
            orderPushkeyCond.setKeyType(keyType);

            TfOrderPushKey orderPushKey = orderAppointmentService.selectByKeyType(keyType);
            String pushKey = orderPushKey.getKey();
            boolean flag = true;

            /**
             * 如果pushKey不为空把之前的签名密钥更新为无效后，再插入签名密钥数据。
             */

            if ("0000".equals(returnCode)) {
                if (!(pushKey == null || ("").equals(pushKey))) {
                    orderAppointmentService.updateByValidity(keyType);
                }
                orderPushkeyCond.setValidity("0");
                orderPushkeyCond.setTransactionId(orderAppointmentService.seqTransactionId()+"");
                orderPushkeyCond.setPushKeyId(orderAppointmentService.seqTransactionId()+"");
                flag = orderAppointmentService.insertOrderPushKey(orderPushkeyCond);
            }


        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(),
                    "SynkeyRefresh", null, "验签秘钥更新发起失败:" + processThrowableMessage(e));
            resp = ExceptionUtils.dealExceptionRetMsg(resp, e, "验签秘钥更新发起失败");
        }
        return resp;


    }

    /**
     * 跳转信息填写页面
     */
    @RefreshCSRFToken
    @RequestMapping("simH5OnlineToConfirmOrder")
    public String confirmOrderSimH5Online(HttpServletRequest request, TfH5SimConf conf, TfPlans plan, Model model, String transactionId) throws Exception {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", this.getClass().getName(),
                "confirmOrderSimH5Online", request.getParameterMap(), "跳转信息填写页面", request);
        String chanId = request.getParameter("CHANID");
        JSONObject param = new JSONObject();
        TfOrderAppointment orderAppointment = new TfOrderAppointment();
        if (StringUtils.isNotEmpty(transactionId)) {
            //查询预约信息
            orderAppointment = orderAppointmentService.getOrderAppointmentById(transactionId);
            if (orderAppointment != null && StringUtils.isNotEmpty(orderAppointment.getRequestParam())) {
                simBuyCommonService.orderTimeout(orderAppointment);
                param = JSONObject.parseObject(orderAppointment.getRequestParam());
                conf = JSONObject.toJavaObject(param, TfH5SimConf.class);
                plan.setPlanId(param.getLong("planId"));
                plan.setPlanName(param.getString("planName"));
                model.addAttribute("regName", orderAppointment.getCustName());
                model.addAttribute("psptId", orderAppointment.getCustCertNo());
                if (StringUtils.isEmpty(orderAppointment.getCustCertNo())) {
                    writerFlowLogThrowable(streamNo, "", "", "", getClass().getName(),
                            null, String.format("身份证信息为空[%s]", transactionId));
                    throw new RuntimeException(ConstantsBase.MY_EXCEP + "身份验证信息为空，请重新认证！");
                }
            } else {
                writerFlowLogThrowable(streamNo, "", "", "", getClass().getName(),
                        null, String.format("预约订单参数据为空，请重新预约[%s]", transactionId));
                throw new RuntimeException(ConstantsBase.MY_EXCEP + "身份验证信息处理异常，请重新认证！");
            }
        }
        //取出预存话费和实际金额
        String planId = String.valueOf(plan.getPlanId());
        TfH5SimConf cond = new TfH5SimConf();
        cond.setConfId(conf.getConfId());
        TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
        conf.setCardType(resultPlan.getCardType());
        //身份验证
        if (StringUtils.isNotEmpty(transactionId)) {
            TfOrderDetailSim orderDetailSim = new TfOrderDetailSim();
            orderDetailSim.setRegName(orderAppointment.getCustName());
            orderDetailSim.setPsptId(orderAppointment.getCustCertNo());
            orderDetailSim.setRegType("1");
            validateCardId(orderDetailSim, resultPlan, param.getString("orderDetailSim.cityCode"), param.getString("feature_str"));
        }
        //== 依据planId获取该档次个性信息
        JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), planId, TfH5SimConf.JSON_ID);
        Long preFee = Long.parseLong(object.getString(TfH5SimConf.JSON_PREFEE));//预存金额
        Long cardFee = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));//卡金额
        //协议
        String serviceContract = commonPropagandaService.getCommonPropagandaByCode(CommonPropaganda.SIM_ORDER_PRO, CommonParams.CHANNEL_CODE).getCommonPropagandaContent();
        model.addAttribute("serviceContract", serviceContract);

        //判断用户是否登录
        MemberVo member = UserUtils.getLoginUser(request);
        logger.info("-----判断用户是否登录-----");
        //o2o渠道版不取当前登录信息
        if (member == null || "E050".equals(chanId)) {
            logger.info("-----用户未登录-----");
            model.addAttribute("preFee", preFee);
            model.addAttribute("cardFee", cardFee);
            model.addAttribute("conf", conf);
            model.addAttribute("plan", plan);
            model.addAttribute("serviceContract", serviceContract);
            writerFlowLogConditionAndReturn(streamNo, "", "", this.getClass().getName(), "simh5OnlineToApply", objectToMap(plan), "跳转信息填写页面", request.getParameterMap(), "member == null || \"E050\".equals(chanId)");
        } else {
            logger.info("-----用户已登录-----");
            //获取已登录用户的联系方式和地址
            MemberAddress memberAddress = new MemberAddress();
            String contactPhone = null; //联系人电话
            String city = null;
            String county = null;
            String memberRecipientAddress = null;
            Long id = member.getMemberLogin().getMemberId();
            memberAddress.setMemberId(id);
            MemberAddress md = memberAddressService.selectAddrY(memberAddress);//查询默认地址
            if (md != null && !(" ".equals(md))) {
                contactPhone = md.getMemberRecipientPhone();
                city = md.getMemberRecipientCity();
                memberRecipientAddress = md.getMemberRecipientAddress();
                String[] citys = city.split(":");
                city = citys[1].toString();
                county = md.getMemberRecipientCounty();
                String[] countys = county.split(":");
                county = countys[1].toString();
            } else {
                List<MemberAddress> md2 = memberAddressService.selectAddr(memberAddress);//未设置默认地址时查询第一个地址
                if (!md2.isEmpty()) {
                    contactPhone = md2.get(0).getMemberRecipientPhone();
                    city = md2.get(0).getMemberRecipientCity();
                    memberRecipientAddress = md2.get(0).getMemberRecipientAddress();
                    String[] citys = city.split(":");
                    city = citys[1].toString();
                    county = md2.get(0).getMemberRecipientCounty();
                    String[] countys = county.split(":");
                    county = countys[1].toString();
                }

            }
            model.addAttribute("memberRecipientCity", city);
            model.addAttribute("memberRecipientAddress", memberRecipientAddress);
            model.addAttribute("memberRecipientCounty", county);
            model.addAttribute("contactPhone", contactPhone);
            model.addAttribute("preFee", preFee);
            model.addAttribute("cardFee", cardFee);
            model.addAttribute("conf", conf);
            model.addAttribute("plan", plan);
            model.addAttribute("CHANID", chanId);
            writerFlowLogExitMenthod(streamNo, "", "", this.getClass().getName(), "simh5OnlineToApply", objectToMap(plan), "跳转信息填写页面");
        }
        model.addAttribute("transactionId", transactionId);
        //从预约回调过来时
        if (StringUtils.isNotEmpty(transactionId)) {
            model.addAttribute("CHANID", param.getString("CHANID"));
            model.addAttribute("memberRecipientCity", param.getString("memberAddress.memberRecipientCity"));
            model.addAttribute("memberRecipientAddress", param.getString("memberAddress.memberRecipientAddress"));
            model.addAttribute("memberRecipientCounty", param.getString("memberAddress.memberRecipientCounty"));
            model.addAttribute("contactPhone", param.getString("orderDetailSim.contactPhone"));
            model.addAttribute("phone", param.getString("orderDetailSim.phone"));
            model.addAttribute("cityName", param.getString("cityName"));

        }
        if (TfH5SimConf.CHNL_CODE_DIDI.equals(resultPlan.getChnlCodeOut())) {
            return "web/goods/sim/confirmOrderSimH5Online-didi";
        }
        return "web/goods/sim/confirmOrderSimH5Online";
    }

    /**
     * 其它渠道接入h5在线售卡
     */
    @VerifyCSRFToken
    @RequestMapping("/simH5OnlineSubmitOrder")
    @ResponseBody
    public Object submitOrderH5OnlineSim(HttpServletRequest request, UserGoodsCarModel cModel, Model model) {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", this.getClass().getName(),
                "simH5OnlineSubmitOrder", request.getParameterMap(), "sim订单提交", request);

        String transactionId = request.getParameter("transactionId");
        JSONObject param;
        try {
            cModel = simBuyCommonService.getCallbackAppointment(transactionId, streamNo, cModel);
            param = cModel.getParams();
        } catch (EcsException e) {
            writerFlowLogThrowable(streamNo, "", "", "", getClass().getName(), "", processThrowableMessage(e));
            throw new RuntimeException("身份验证信息处理异常，请重新认证！");//ConstantsBase.MY_EXCEP要和ExceptionUtils.dealExceptionRetMsg配合使用
        }

        Map respInnerMap = new HashMap();
        respInnerMap.put("type", ""); //特殊业务置换特殊标记，用于js中判断不同情况不同方式向用户展示信息
        ResponseBean resp = new ResponseBean();
        Session session = UserUtils.getSession();
        //放入号卡固定信息
        cModel = UserGoodsCarModel.getSimBaseInfo(cModel);
        Long planRMB;//支付金额
        Long preFee;//预存金额
        Long cardFee;//卡费金额
        MemberLogin memberLogin;//会员信息
        String planId = param.getString("planId");
        TfOrderDetailSim orderDetailSim = cModel.getOrderDetailSim();

        //== 判断是否已经登陆，如果没有登陆，则依据申请信息生成匿名用户
        MemberVo member = UserUtils.getLoginUser(request);
        TfPlans plans = new TfPlans();
        try {
            /*int ipPhoneNumOrders = simBusiService.getIpPhoneNumOrders(Servlets.getRemortIP(request));
            printKaInfo("simH5OnlineSubmitOrder_" + ipPhoneNumOrders + "  IP:" + Servlets.getRemortIP(request));
            if (ipPhoneNumOrders > Integer.parseInt(DictUtil.getDictValue("ipPhoneNumOrders", "ipPhoneNumOrders", "10"))) {
                printKaInfo("选号次数过多:" + Servlets.getRemortIP(request) + "---" + ipPhoneNumOrders);
                throw new RuntimeException("选号异常，请稍候再试！");
            }*/
            MemberAddress address = validateMemberAddress(cModel, streamNo);

            //== 区县查询地市，弥补插件不足
            //String cityTmp = address.getMemberRecipientCity();
            //String cntyTmp = address.getMemberRecipientCounty();
            address.setMemberRecipientName(orderDetailSim.getRegName());
            //用户没有登陆
            if (member == null) {
                memberLogin = simBuyCommonService.anonymousLogin(orderDetailSim, address);
            } else {
                memberLogin = member.getMemberLogin();
            }

            //== 套餐信息+个性档次信息
            //配置项Id：以后的金额也可能配置在planJson中，所以这里必须通过confId去数据查数据
            TfH5SimConf resultPlan = null;
            String confId = param.getString("confId");//TODO 空判断
            String is95 = DictUtil.getDictValue(confId, "IS_FRESHMAN", "0");
            if ("1".equals(is95) && !checkIsGraduate(orderDetailSim.getPsptId())) {//不是95到05之间
                throw  new Exception(ConstantsBase.MY_EXCEP + "本活动仅限95后校园用户办理！");
            }
            try {
                //== 配置项信息
                TfH5SimConf cond = new TfH5SimConf();
                cond.setConfId(confId);
                resultPlan = plansService.queryListCond(cond).get(0);
                //== 依据planId获取该档次个性信息
                JSONObject object = JsonUtil.queryMapInfoById(resultPlan.getPlanJson(), planId, TfH5SimConf.JSON_ID);
                preFee = Long.parseLong(object.getString(TfH5SimConf.JSON_PREFEE));//预存金额
                cardFee = Long.parseLong(object.getString(TfH5SimConf.JSON_CARDFEE));//卡金额
                planRMB = preFee + cardFee;
                orderDetailSim.setChnlCodeOut(resultPlan.getChnlCodeOut());//外围渠道编码
                orderDetailSim.setChnlCodeOutSub1(resultPlan.getChnlCodeOutSub1());//外围子渠道编码
                logger.info("页面获取参数：planId:{},confId:{}", planId, confId);
            } catch (Exception e) {
                e.printStackTrace();
                printKaError2("submitOrderH5OnlineSim订单提交失败，提交参数有误：", request, e);
                writerFlowLogThrowable(streamNo, "", memberLogin.getMemberLogingName(), this.getClass().getName(),
                        "submitOrderH5OnlineSim", null, processThrowableMessage(e));
                throw new Exception(ConstantsBase.MY_EXCEP + "订单提交失败，提交参数有误");
            }
            //== 子订单信息
            TfOrderSub orderSub = new TfOrderSub();
            TfOrderUserRef userRef = new TfOrderUserRef();//== 用户信息
            userRef.setMemberId(memberLogin.getMemberId());
            userRef.setMemberLogingName(memberLogin.getMemberLogingName());
            userRef.setEparchyCode(address.getMemberRecipientCity());//归属地市，直接使用收货地址信息
            userRef.setCounty(address.getMemberRecipientCounty());//归属区县，直接使用收货地址信息
            Long cp = memberLogin.getMemberPhone();
            if (cp != null) {
                userRef.setContactPhone(cp.toString());
            }
            userRef.setMemberCreditClass("0");//用户星级 | 客户类型，为空则为普通用户
            orderSub.setOrderUserRef(userRef);

            //== 支付金额
            cModel.setMemberAddress(address);
            orderSub = confirmOrderBaseInfo(cModel, orderSub);
            orderSub.setOrderSubPayAmount(planRMB);//流程中需要用到，必须设置
            orderSub.setOrderSubAmount(planRMB);//号卡订单SubAmount和SubPayAmount相同

            //== 处理TfOrderDetailSim
            //处理号码，car中就不应该出现这种形式：15273162604_1,15273162604_1
            long phone = Long.parseLong(orderDetailSim.getPhone().substring(0, 11));
            orderDetailSim.setPhone(orderDetailSim.getPhone().substring(0, 11));
            //处理baseSet和simProductId
            String cityCode = orderDetailSim.getCityCode();
            //== 归属地校验
            Map phoneResultMap = validatePhone(streamNo, orderDetailSim, cityCode);
            String cityCodeSuffix = cityCode.substring(2);
            plans.setPlanId(Long.parseLong(planId));
            plans = plansService.getPlanList(plans).get(0);
            String productId = plans.getProductId().replace("**", cityCodeSuffix);
            String baseSet = plans.getPlanCode().replace("**", cityCodeSuffix);
            orderDetailSim.setSimProductId(productId);
            orderDetailSim.setPreFee(preFee);//预存金额
            orderDetailSim.setCardFee(cardFee);//卡金额
            orderDetailSim.setBaseSet(baseSet);//套餐编码
            orderDetailSim.setBaseSetName(plans.getPlanName());//套餐名称
            orderDetailSim.setPhoneType(TfOrderDetailSim.PT_PU_NUM);//普号

            String idAddr = validateAddr(streamNo, orderDetailSim, phoneResultMap);


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
            orderSubDetail.setGoodsUrl(confId); // 设置商品单品页URL
            //orderSubDetail.setWapGoodsUrl(url.getGoodsUrl());
            orderSubDetail.setSupplierShopId(car.getShopId());// 设置供应商Id和Name
            orderSubDetail.setSupplierShopName(car.getShopName());
            orderSubDetail.setGoodsSkuPrice(planRMB);
            orderSubDetail.setGoodsSkuNum(1L);//每个订单只能一个号码
            orderSubDetail.setProdSkuId(Long.parseLong(planId));//将号卡配置信息作为prodSkuId保存
            //orderSubDetail.setProdBossCode(goodsInfo.getProdBossCode());//号卡没有这个属性
            //orderSubDetail.setMarketId(marketId);//无活动
            orderSubDetail.setGoodsSkuId(Long.parseLong(planId));//保存套餐的planId
            orderSubDetail.setGoodsId(phone);//号卡订单：号码作为GoodsId
            orderSubDetail.setGoodsFormat(confId);//号卡订单：confId保存在goodsFormat中
            //==信息校验（生产必须放开，测试环境因为统一工号无权限可以注释掉）
            String feature_str = param.getString("feature_str");
            validateCardId(orderDetailSim, resultPlan, cityCode, feature_str);
            orderSubDetail.setOrderDetailSim(orderDetailSim);
            orderSub.addOrderDetail(orderSubDetail);
            //== 号码第一次选占
            //simBusiService.selTempOccupyRes(orderDetailSim);
            //logger.info("号码选占成功！");
            //==生成订单
            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
            TfOrderSub orderSubResult = orderSubList.get(0);

            simBuyCommonService.updateOrderAppointment(transactionId, orderSubResult);

            printKaInfo("submitOrderH5OnlineSim订单生成成功：" + JSONObject.toJSONString(orderSubResult, SerializerFeature.WriteMapNullValue));
            session.setAttribute("ORDER_RESULT", orderSubResult);
            //== 是否保存推荐信息
            String recmdCode = param.getString("recmdCode");
            model.addAttribute("recmdCode", recmdCode);

            printKaInfo("submitOrderH5OnlineSim_recmdCode：" + recmdCode + "：" + JSONObject.toJSONString(orderSubResult));
            if (!StringUtils.isEmpty(recmdCode)) {
                saveSimRecmdInfo(recmdCode, orderSubResult, orderDetailSim.getPhone(), orderDetailSim.getContactPhone(), resultPlan, request);
            }
            writerFlowLogExitMenthod(streamNo, orderSub.getOrderSubNo(), memberLogin.getMemberLogingName(), this.getClass().getName(), "submitOrderH5OnlineSim", objectToMap(orderSub), "sim订单提交成功");
            //== 滴滴卡返回信息特殊处理
            if ("0731".equals(cityCode) && TfH5SimConf.CHNL_CODE_DIDI.equals(resultPlan.getChnlCodeOut())) {
                respInnerMap.put("type", "DIDI"); //前台标识滴滴号卡，以方便不同的处理方式
                respInnerMap.put("orderNo", orderSubResult.getOrderSubNo());
                respInnerMap.put("confId", confId);
                respInnerMap.put("feature_str", feature_str);
            }
            //判断卡的类型,如果是大王卡信息，特殊处理
            String cardType = param.getString("cardType");
            if ("3".equals(cardType)) {
                respInnerMap.put("cardType", cardType);
                respInnerMap.put("confId", confId);
                respInnerMap.put("recmdCode", recmdCode);
                respInnerMap.put("cityCodeSuffix", cityCodeSuffix);
            }
            //== 非0元支付
            if (planRMB > 0) {
                //跳转到支付页面
                respInnerMap.put("type", "toPay");
                respInnerMap.put("orderNo", orderSubResult.getOrderSubNo());
                respInnerMap.put("confId", confId);
                respInnerMap.put("planId", planId);
                respInnerMap.put("selectPhone", orderDetailSim.getPhone());
                //大王卡需要支付特殊处理
                JedisClusterUtils.set(orderSub.getOrderSubNo() + "recmdCode", recmdCode, 3600);
                JedisClusterUtils.set(orderSub.getOrderSubNo() + "planId", planId, 3600);
                resp.addSuccess(respInnerMap);
                return resp;

            }
            //== 订单生成后处理
            dealAfterNewOrder(request, orderDetailSim, plans, phone, orderSubResult);
            resp.addSuccess(respInnerMap);
            request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));
        } catch (Exception e) {
            e.printStackTrace();
            printKaError2("submitOrderH5OnlineSim生成订单失败：", request, e);
            writerFlowLogThrowable(streamNo, "", "", this.getClass().getName(), "submitOrderH5OnlineSim", null, "订单生成失败:" + processThrowableMessage(e));
            resp = ExceptionUtils.dealExceptionRetMsg(resp, e, "订单生成失败");
        }
        return resp;
    }


    /**
     * 身份验证
     *
     * @param orderDetailSim
     * @param resultPlan
     * @param cityCode
     * @param feature_str
     * @throws Exception
     */

    private void validateCardId(TfOrderDetailSim orderDetailSim, TfH5SimConf resultPlan, String cityCode, String feature_str) throws Exception {
        if (isProEnv()) {
            simBusiService.realityVerifyV2(orderDetailSim); //== 身份证信息校验
            logger.info("身份证信息验证成功！");
            simBusiService.oneIdFiveNoVerify(orderDetailSim);//== 一证五号 v2.0 校验
            logger.info("一证五号校验成功！");
            //== 归属地为长沙，并且是滴滴号卡才进行滴滴司机认证
            if ("0731".equals(cityCode) && TfH5SimConf.CHNL_CODE_DIDI.equals(resultPlan.getChnlCodeOut())) {
                //orderDetailSim.setSynOnliResuIssuc(TfOrderDetailSim.TEST_SYN);//正式上生产必须去掉
                simBusiService.didiDriverVerify(orderDetailSim, didiclientId, feature_str);
                logger.info("滴滴司机身份校验成功！");
            }
        }
    }

    private String validateAddr(String streamNo, TfOrderDetailSim orderDetailSim, Map phoneResultMap) {
        //== 地址处理，防止同步订单报错
        //取选择地址，并且去掉中间的空格
        String idAddr = orderDetailSim.getLinkAddress();
        if (idAddr.contains(" ")) {
            idAddr = idAddr.replaceAll(" ", "");
        }
        //如果字符个数小于6，则补充字符个数
        if (idAddr.length() < 6) {
            printKaInfo("submitOrderH5OnlineSim配送地址所在地区有误：" + JSONObject.toJSONString(phoneResultMap));
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(),
                    "submitOrderH5OnlineSim", phoneResultMap, "配送地址所在地区有误！");
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "配送地址所在地区有误！");
        }
        return idAddr;
    }

    /**
     * 号码归属校验
     *
     * @param streamNo
     * @param orderDetailSim
     * @param cityCode
     * @return
     * @throws Exception
     */
    private Map validatePhone(String streamNo, TfOrderDetailSim orderDetailSim, String cityCode) throws Exception {
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(orderDetailSim.getPhone());
        Map phoneResultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        logger.info("归属地校验结果返回==>" + JSONObject.toJSONString(phoneResultMap, SerializerFeature.WriteMapNullValue));
        //接口异常时
        String pasResult = (String) ((Map) ((List) phoneResultMap.get("result")).get(0)).get("AREA_CODE");
        if (pasResult == null) {
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(),
                    "submitOrderH5OnlineSim", phoneResultMap, "归属地校验接口异常");
            printKaInfo("submitOrderH5OnlineSim归属地校验接口异常：" + JSONObject.toJSONString(phoneResultMap));
        }
        //接口正常且有AREA_CODE字段，则校验失败
        if (pasResult != null && pasResult.length() > 0 && !cityCode.equals(pasResult)) {
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(),
                    "submitOrderH5OnlineSim", phoneResultMap, "所选号码和号码归属地不匹配！");
            printKaInfo("submitOrderH5OnlineSim归属地不匹配：" + JSONObject.toJSONString(phoneResultMap));
            throw new Exception(ConstantsBase.MY_EXCEP + "对不起，您所选号码和号码归属地不匹配！");
        }
        return phoneResultMap;
    }

    /**
     * 收货地址校验
     *
     * @param cModel
     * @param streamNo
     * @return
     */
    private MemberAddress validateMemberAddress(UserGoodsCarModel cModel, String streamNo) {
        //== 地址数据校验
        MemberAddress address = cModel.getMemberAddress();
        String regEx = "^[0-9A-Za-z\\u4e00-\\u9fa5]{6,}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(address.getMemberRecipientAddress());
        if (!matcher.matches()) {
            printKaInfo("simH5OnlineSubmitOrder地址不合法:" + JSONObject.toJSONString(address));
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(), "submitOrderH5OnlineSim", address, "地址不合法");
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "订单提交失败，收货地址信息不符合规范");
        }
        if (StringUtils.isEmpty(address.getMemberRecipientCity())) {
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(), "submitOrderH5OnlineSim", address, "地址为空");
            printKaInfo("simH5OnlineSubmitOrder地址为空问题监控:" + JSONObject.toJSONString(address));
            throw new RuntimeException(ConstantsBase.MY_EXCEP + "订单提交失败，请重试[地址为空]");
        }
        return address;
    }


    /**
     * 跳转滴滴号卡的结果页面
     */
    @RequestMapping("toDidiSuccess")
    public Object toDidiSuccess(String orderNo, Model model) {
        model.addAttribute("orderNo", orderNo);
        return "web/goods/sim/didiSimSuccess";
    }


    /**
     * 异步请求地址数据
     */
    @RequestMapping("getSimAddress")
    @ResponseBody
    public Object getSimAddress() {
        List<ThirdLevelAddress> addresses = memberAddressService.getChildrensByPId(CommonParams.CITY_HN_PID);
        List<Map> ls = Lists.newArrayList();
        for (ThirdLevelAddress address : addresses) {
            Map map = new HashMap();
            map.put("text", address.getOrgName());
            map.put("value", address.getEparchyCode());
            ls.add(map);
        }
        return ls;
    }

    /**
     * 异步请求全量地址
     */
    @RequestMapping("getSimCityCnty")
    @ResponseBody
    public Object getSimCityCnty() {
        return shopManage.queryCityCntyInfo(null);
    }

    /**
     * 异步请求号码
     */
    @RequestMapping("getQueryNumsH5Online")
    @ResponseBody
    public Object getQueryNumsH5Online(String number, String cityCode, TfH5SimConf conf) throws Exception {
        Map<String, Object> paramMap = Maps.newHashMap();
        List<NetNum> numList = Lists.newArrayList();
        ResponseBean resp = new ResponseBean();
        try {
            if (cityCode == null || cityCode.equals("")) {
                throw new Exception(ConstantsBase.MY_EXCEP + "请选择归属地市！");
            }
            NetNumQueryCondition condition = new NetNumQueryCondition();
            paramMap.put("X_CHOICE_TAG", 3);//分支：1-自助选号，3-网上商城选号 TODO
            paramMap.put("PARA_VALUE4", 100);//最大数量，默认每次查询出9个号码 fixme
            paramMap.put("CODE_TYPE_CODE", 1);//号码类型：1-普号，2-靓号
            paramMap.put("PARA_VALUE5", 2);//1：号头查询，2：号尾查询，3：模糊查询 TODO
            paramMap.put("START_SERIAL_NUMBER", number);
            paramMap.put("TRADE_EPARCHY_CODE", cityCode);// 设置号码归属地
            condition.setConditionExtendsMap(paramMap);
            //取外围渠道工号配置
            List<TfH5SimConf> list = plansService.queryListCond(conf);
            conf = list.get(0);
            if (StringUtils.isNotEmpty(conf.getChnlCodeOut())) {
                TfOrderChnlInfo orderChnlInfo = recmdMainService.getOrderChnlInfo(conf.getChnlCodeOut());
                if (null != orderChnlInfo && StringUtils.isNotEmpty(orderChnlInfo.getStaffId())
                        && StringUtils.isNotEmpty(orderChnlInfo.getTradeDepartPassword())) {
                    condition.setStaffId(orderChnlInfo.getStaffId());
                    condition.setTradeDepartPassword(orderChnlInfo.getTradeDepartPassword());
                }
            }
            numList = netNumServerService.netNumQuery(condition);
            resp.addSuccess(numList);
        } catch (Exception e) {
            e.printStackTrace();
            writerFlowLogThrowable(createStreamNo(), "", "", this.getClass().getName(), "getQueryNumsH5Online", paramMap, "号码查询异常:" + processThrowableMessage(e));
            splitException(e, "号码查询异常！");
        }
        return resp;
    }

    /**
     * 同步订单测试
     */
    @RequestMapping("testSynSimToOnline")
    @ResponseBody
    public String testSynSimToOnline() throws Exception {
        logger.info("======> 进入同步号卡订单：SynOrderDetail2Deliver");
        TfOrderSub orderCond = new TfOrderSub();
        Page<TfOrderSub> orderSubPage = new Page<>();
        Date now = new Date();
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(now);
        //calendar.add(calendar.DATE, -1); //1天以前的订单
        //Date beforeTime = calendar.getTime();

        //== 查询所有SYN_ONLI_RESU_ISSUC为-1的号卡订单的orderSubNo
        orderCond.setPage(new Page<TfOrderSub>(1, 1));//每3分钟150条
        orderCond.setOrderTypeId(OrderConstant.TYPE_SIM);//仅限于号卡订单
        orderCond.setOrderTypeIds(OrderConstant.TYPE_SIM + "");//必须要有Service中依据这个有区分
        TfOrderDetailSim simCond = new TfOrderDetailSim();
        simCond.setSynOnliResuIssuc(TfOrderDetailSim.MUST_SYN);//所有需要同步但未同步订单 + 同步失败的订单
        //orderCond.setStartOrderTime(beforeTime);
        //orderCond.setEndOrderTime(now);
        orderCond.setDetailSim(simCond);
        //TODO 是否需要排除的订单渠道
        /*orderCond.setExcludeChannelCodes(Arrays.asList(new String[]{
                OrderConstant.CHANNEL_B2B, OrderConstant.CHANNEL_BLOC, OrderConstant.CHANNEL_chinaNet,
                OrderConstant.CHANNEL_JD,OrderConstant.CHANNEL_JD2}));*/
        orderCond.setOrderChannelCodeList(Arrays.asList(new String[]{
                OrderConstant.CHANNEL_BLOC, OrderConstant.CHANNEL_chinaNet, OrderConstant.CHANNEL_TMALL,
                OrderConstant.CHANNEL_JD, OrderConstant.CHANNEL_JD2, OrderConstant.CHANNEL_SHOPWAP}));
        //orderCond.setOrderChannelCode(OrderConstant.CHANNEL_chinaNet);
        //订单具体信息
        logger.info("订单查询条件：" + JSONObject.toJSONString(orderCond, SerializerFeature.WriteNullStringAsEmpty));
        orderSubPage = orderQueryService.queryComplexOrderPage(orderCond);
        logger.info("查询到的订单量：" + orderSubPage.getList().size());

        //TODO 逆序，先执行完最靠前的数据（是否有必要）
        //TODO 如果两个线程同是获取锁将会怎样？
        long suc = 0;//记录成功条数
        for (TfOrderSub orderSub : orderSubPage.getList()) {
            logger.info("------> 同步订单开始");
            SynSimOrder2OnlineCond cond = new SynSimOrder2OnlineCond();
            Map<String, Object> map = new HashMap<>();
            try {
                List channels = Arrays.asList(new String[]{OrderConstant.CHANNEL_chinaNet, OrderConstant.CHANNEL_JD,
                        OrderConstant.CHANNEL_TMALL, OrderConstant.CHANNEL_JD2});
                TfOrderDetailSim sim = orderSub.getDetailSim();
                TfOrderRecipient recipient = orderSub.getRecipient();
                cond.setSourceCode("731009"); //黄濯说暂时写死，依据在线公司要求再改
                cond.setTargetCode("1085");
                String time = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
                String transactionID = "731" + time + (int) ((Math.random() * 9 + 1) * 100000);
                cond.setTransactionID(transactionID);
                Map<String, Object> reqMap = new HashMap();
                reqMap.put("busiSeq", orderSub.getOrderSubNo());//订单流水/订单号码
                reqMap.put("svcNum", sim.getPhone());//开户号码
                if (channels.contains(orderSub.getOrderChannelCode())) {
                    reqMap.put("cityCode", "0" + sim.getCityCode());//地市编码
                } else {
                    reqMap.put("cityCode", sim.getCityCode());//地市编码
                }
                TdEcOrgInfo info = new TdEcOrgInfo();
                if (channels.contains(orderSub.getOrderChannelCode())) {
                    info.setEparchCode("0" + sim.getCityCode());
                } else {
                    info.setEparchCode(sim.getCityCode());
                }
                reqMap.put("cityName", shopManage.getOrgName(info));
                reqMap.put("custName", sim.getRegName());
                reqMap.put("custCertNo", sim.getPsptId()); //随意写的，看能力平台那边是否有校验
                reqMap.put("telephone", sim.getContactPhone());//客户话

                TfPlansCodeRef codeRef = new TfPlansCodeRef();
                //集团订单
                if (channels.contains(orderSub.getOrderChannelCode())) {
                    TfPlansCodeRef param = new TfPlansCodeRef();
                    //查集团对应编码
                    param.setPlansCodeType(TfPlansCodeRef.DQ_GROUP_REF);
                    param.setRefCode(sim.getBaseSet());
                    param = plansService.queryListCond(param).get(0);

                    String cityCodeSuffix = sim.getCityCode().substring(1);
                    String productId = param.getRefProductId().replace("**", cityCodeSuffix);
                    String baseSet = param.getRefPlansId().replace("**", cityCodeSuffix);
                    codeRef.setPlansCodeType(TfPlansCodeRef.DQ_ONLINE_REF);
                    codeRef.setRefProductId(productId);
                    codeRef.setRefPlansId(baseSet);
                    codeRef = plansService.queryListCond(codeRef).get(0);
                    //== 号码第一次选占
                    sim.setTerminalType(OrderConstant.O2O_TYPE_XYYX);
                    simBusiService.selTempOccupyRes(sim);
                    //二次选占
                    Map result = simBusiService.occupySimNumber(sim.getPhone(), sim.getPsptId(), sim);
                    if (!OrderConstant.IF_SUCCESS_CODE.equals((String) result.get(OrderConstant.IF_RESULT_CODE))) {
                        throw new RuntimeException(String.format("二次选占失败：号码[%s],psptid[%s],%s", sim.getPhone(), sim.getPsptId(),
                                result.get(OrderConstant.IF_RESULT_INFO)));
                    }
                    boolean flag = simBusiService.blocThirdSimSync(sim, orderSub);
                    if (!flag) {
                        throw new EcsException(ExceptionOrderCode.ECS_ERROR, String.format("订单同步失败【%s】", orderSub.getOrderSubId()));
                    }

                } else {
                    codeRef.setPlansCodeType(TfPlansCodeRef.DQ_ONLINE_REF);
                    codeRef.setRefProductId(sim.getSimProductId());
                    codeRef.setRefPlansId(sim.getBaseSet());
                    codeRef = plansService.queryListCond(codeRef).get(0);
                }
                reqMap.put("productCode", codeRef.getRefCode());
                reqMap.put("productName", codeRef.getRefProductName());
                reqMap.put("productDesc", codeRef.getRefProductName());//最多200，该处截取181个字符
                reqMap.put("priceFee", sim.getPreFee() + "");//收费金额：0元或其它金额
                reqMap.put("priceDesc", "preFee_" + sim.getPreFee() + "|" + "cardFee_" + sim.getCardFee());
                reqMap.put("deliveryCity", recipient.getOrderRecipientCity());//收货所在地市
                reqMap.put("deliveryAddress", recipient.getOrderRecipientCounty() + recipient.getOrderRecipientAddress());//收货详细地址
                reqMap.put("saleChannle", "E007");
                //取外部渠道工号配置
                if (StringUtils.isNotEmpty(sim.getChnlCodeOut())) {
                    TfOrderChnlInfo orderChnlInfo = recmdMainService.getOrderChnlInfo(sim.getChnlCodeOut());
                    if (null != orderChnlInfo && StringUtils.isNotEmpty(orderChnlInfo.getStaffId())
                            && StringUtils.isNotEmpty(orderChnlInfo.getTradeDepartPassword())) {
                        cond.setStaffId(orderChnlInfo.getStaffId());
                        cond.setTradeDepartPassword(orderChnlInfo.getTradeDepartPassword());
                    }
                    reqMap.put("saleChannle", sim.getChnlCodeOut());
                }
                //用它传新业务编码list,对于选多种新业务的情况,用竖线"|"分隔即可.
                List<TfOrderSimBusi> simBusiList = orderSub.getOrderSimBusiList();
                if (!Collections3.isEmpty(simBusiList)) {
                    reqMap.put("offCode", (Collections3.extractToString(simBusiList, "discountsCode", "|")));
                }

                cond.setReqInfo(reqMap);
                logger.info("同步的订单信息：" + JSONObject.toJSONString(cond, SerializerFeature.WriteMapNullValue));
                map = synSimOrder2OnlineService.syn2Online(cond);
                logger.info("返回结果信息：" + JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue));

                //更新返回结果状态
                if (map != null) {
                    Map map0 = (Map) map.get("result");
                    if (map0 != null && map0.size() > 0) {
                        String respCode = String.valueOf(map0.get("returnCode"));
                        String returnMessage = String.valueOf(map0.get("returnMessage"));
                        String isSuc = String.valueOf(map0.get("isSuc"));
                        TfOrderDetailSim updateSim = new TfOrderDetailSim();
                        updateSim.setSynOnliResuCode(respCode);
                        updateSim.setSynOnliResuMsg(returnMessage);
                        updateSim.setSynOnliResuIssuc(isSuc);
                        updateSim.setOrderSubDetailId(orderSub.getDetailList().get(0).getOrderSubDetailId());
                        orderService.updateDetailSimByPrimaryKey(updateSim);
                        suc++;
                        logger.info("更新返回信息成功：" + orderSub.getDetailList().get(0).getOrderSubDetailId());
                    }
                }
                TfOrderLog log = new TfOrderLog();
                log.setOrderLogMethod("SynOrderDetail2Deliver#Req_Resp_Suc");
                log.setOrderLogMsec(1L);//该条记录被循环了多少次 结合日志 达到监控目的
                log.setOrderLogArgs(JSONObject.toJSONString(cond, SerializerFeature.WriteMapNullValue));
                log.setOrderLogReturn(JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue));
                orderService.saveOrderLog(log);
                completeOrder(orderSub);
            } catch (Exception e) {
                String msg1 = "{查询条件:" + JSONObject.toJSONString(orderCond) + "},{查询条数:" + JSONObject.toJSONString(orderSubPage.getList().size()) + "}";
                String msg2 = "{请求内容:" + JSONObject.toJSONString(cond) + "},{返回结果:" + JSONObject.toJSONString(map) + "}";
                orderService.saveOrderLog("SynOrderDetail2Deliver#Exception", msg1, msg2, suc + ":" + e.getMessage());//输出+返回
                e.printStackTrace();
                return "{\"异常\":" + e.getMessage() + "},{" + msg1 + "," + msg2 + "}";
            }
        }
        long time = ((new Date()).getTime() - now.getTime()) / 1000;
        TfOrderLog log1 = new TfOrderLog();
        log1.setOrderLogMethod("SynOrderDetail2Deliver#Size_Time");
        log1.setOrderLogArgs(orderSubPage.getList().size() + "");
        log1.setOrderLogMsec(suc);//该条记录被循环了多少次 结合日志 达到监控目的
        log1.setOrderLogReturn(time / 60 + ":" + time % 60);
        orderService.saveOrderLog(log1);
        return "{条数:" + JSONObject.toJSONString(orderSubPage.getList().size()) + "}," +
                "{\"成功条数\":" + suc + "},{\"消耗时间\":" + time / 60 + "分" + time % 60 + "秒}";
    }

    private void completeOrder(TfOrderSub orderSub) {
        try {
            OrderProcessParam processParam = new OrderProcessParam();
            orderSub.setProcessParam(null);
            processParam.putFlash(Variables.ORDER_SUB, orderSub);
            processParam.setAct(1);
            if (orderSub.getOrderPayAmount() > 0) {
                //审核
                processParam.setAction("audit");
            } else {
                //备货
                processParam.setAction("delivery");
            }
            processParam.setOrderStatusId(orderSub.getOrderStatusId());
            processParam.setBusinessId(orderSub.getOrderSubNo());
            orderService.completeByAdmin(processParam);
            if (orderSub.getOrderPayAmount() > 0) {
                processParam.setAction("delivery");
                orderService.completeByAdmin(processParam);
            }
        } catch (Exception e) {
            logger.error("completeOrder:", e);
            LogUtils.printKaError(CommonParams.KA_LOG, "completeOrder-订单流转异常:", e);
        }
    }


    /**
     * 请求ip对应地市信息
     */
    @RequestMapping("getDefaultAddressByIp")
    @ResponseBody
    public String getDefaultAddress(String ip, HttpServletRequest request) {
        String rip = FormAuthenticationFilter.getRemoteAddr(request);
        logger.info("remoteAddr:" + rip);
        //String urlStrIp = "http://www.taobao.com/help/getip.php";
        //String retStr = openServiceRequest(urlStrIp);
        //int leftIndex = retStr.indexOf("{ip:");
        //int lastIndex = retStr.indexOf("\"})");
        //String ip = retStr.substring(leftIndex+5,lastIndex);
        logger.info("获取得到的IP：" + ip);
        if (!rip.equals(ip)) {
            printKaInfo("getDefaultAddressByIp存在两个IP不同的情况：getRemoteAddr:" + rip + ",ip:" + ip);
        }
        //String urlStrAdrs = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
        //String retStr = openServiceRequest(urlStrAdrs);
        //String ipCityCode = ((Map) com.alibaba.fastjson.JSONObject.parseObject(retStr).get("data")).get("city_id").toString();
        return "";
    }

    /**
     * 第三方开放服务请求
     */
    public String openServiceRequest(String urlStr) {
        String retStr = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            // 设置连接属性
            httpConn.setConnectTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            // 获取相应码
            int respCode = httpConn.getResponseCode();
            if (respCode == 200) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                // 将输入流转移到内存输出流中
                try {
                    while ((len = httpConn.getInputStream().read(buffer, 0, buffer.length)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    retStr = new String(out.toByteArray()); // 将内存流转换为字符串
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != out) {
                        out.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }

    /**
     * 订单生成后处理
     */
    @Override
    public void dealAfterNewOrder(HttpServletRequest request, TfOrderDetailSim orderDetailSim, TfPlans plans, long phone, TfOrderSub orderSubResult) {
        try {
            //== 发短信
            //该联系方式在移动这边是否有用户信息
            //因增加新业务，延时同步，短信发送移到订单同步成功后再发送
            /*Map<String, Object> map = new HashMap<String, Object>();
            BasicInfoCondition biCond = new BasicInfoCondition();
            biCond.setSerialNumber(orderDetailSim.getContactPhone());
            biCond.setxGetMode("0");
            //默认值没有权限，所以显式赋值
            biCond.setStaffId("ITFWAPNN");
            biCond.setTradeDepartPassword("225071");
            logger.info("====用户资料条件===>" + JSONObject.toJSONString(biCond, SerializerFeature.WriteMapNullValue));
            Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(biCond);
            logger.info("====用户资料返回===>" + JSONObject.toJSONString(userInfo, SerializerFeature.WriteMapNullValue));
            printKaInfo("submitOrderH5OnlineSim查询是否为湖南移动号码：" + JSONObject.toJSONString(userInfo));
            JSONObject phoneAttributionJSON = JSONObject.parseArray(userInfo.get("result").toString()).getJSONObject(0);
            String phoneNumberResultcode = phoneAttributionJSON.getString("X_RESULTCODE");
            if ("0".equals(phoneNumberResultcode)) {
                //发送短信
                SmsSendCondition smsSendCond = new SmsSendCondition();
                smsSendCond.setSerialNumber(orderDetailSim.getContactPhone());
                String noticeContent = "尊敬的客户，您已成功申请湖南移动" + plans.getPlanName() +
                        "卡，号码为" + orderDetailSim.getPhone() +
                        "，订单号为" + orderSubResult.getOrderSubNo() +
                        "。我们正在备货，您可关注“湖南移动微厅”查询订单详情。【中国移动】。";
                smsSendCond.setNoticeContent(noticeContent);
                logger.info("====短信请求===>" + JSONObject.toJSONString(smsSendCond, SerializerFeature.WriteMapNullValue));
                Map smsSendRetMap = smsSendService.sendSms(smsSendCond);
                logger.info("====短信返回===>" + JSONObject.toJSONString(smsSendRetMap, SerializerFeature.WriteMapNullValue));
                printKaInfo(phone + "号卡订单生成成功短信接口内容" + JSONObject.toJSONString(smsSendRetMap));
            }*/


        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printKaError(CommonParams.KA_LOG, "submitOrderH5OnlineSim发送短信失败", e);
            writerFlowLogThrowable(createStreamNo(), orderSubResult.getOrderSubNo(), "", getClass().getName(), "dealAfterNewOrder",
                    orderSubResult, "submitOrderH5OnlineSim发送短信失败:" + processThrowableMessage(e));
        }

        try {
            //== 根据用户IP地址记录选号次数
            simBusiService.setIpPhoneNumOrders(Servlets.getRemortIP(request));
        } catch (Exception e) {
            LogUtils.printKaError(CommonParams.KA_LOG, "IP地址记录选号次数异常", e);
            e.printStackTrace();
        }
    }

    /**
     * 保存号卡推荐信息
     * recmdCode：推荐人推荐数据的RecmdId的base64编码
     * phone：所选号码
     */
    private void saveSimRecmdInfo(String recmdCode, TfOrderSub orderSub, String phone, String contactPhone, TfH5SimConf resultPlan, HttpServletRequest request) {
        String streamNo = createStreamNo();
        logger.info("==进入保存推荐信息==>推荐码:" + recmdCode + ";所选号码:" + phone + ";联系电话:" + contactPhone + ";orderSub:" + JSONObject.toJSONString(orderSub));
        writerFlowLogEnterMenthod(streamNo, "", phone, getClass().getName(), "saveSimRecmdInfo",
                orderSub, "保存号卡推荐信息", request);
        try {
            String recmdCode2 = Base64.decode(recmdCode.getBytes());//解码 -> rcd_conf_id
            Long recmdId = Long.valueOf(recmdCode2);//推荐ID

            //== 保存推荐号卡业务信息
            TfOrderRecmdRef orderRecmdRef = new TfOrderRecmdRef();
            TfOrderRecmd recmd = recmdMainService.getOrderRecmd(new TfOrderRecmd(recmdId));
            orderRecmdRef.setRecmdId(recmdId);
            orderRecmdRef.setRcdedRefOrdSubId(orderSub.getOrderSubId());
            //被推荐关联子订单ID
            orderRecmdRef.setRcdedRefBusiFlag1(orderSub.getOrderSubNo());//订单号
            orderRecmdRef.setRcdedRefBusiFlag2(phone);
            logger.info("==saveSimRecmdInfo保存推荐关联业务信息==>" + JSONObject.toJSONString(orderRecmdRef));
            printKaInfo("==saveSimRecmdInfo保存推荐关联业务信息==>" + JSONObject.toJSONString(orderRecmdRef));
            recmdMainService.saveOrderRecmdRef(orderRecmdRef);
            logger.info("==saveSimRecmdInfo保存推荐关联业务信息成功！");
            //o2o渠道另外处理

            if (TfFlowcouponsGive.CHNL_CODE_E050.equals(resultPlan.getChnlCodeOut())) {
                logger.info("E050");
                //99541463 优惠编码
                //激活后送 job
                return;
            } else {
                //== 时间过期能正常下单,不做赠送
                //TfOrderRecmd recmdRst = recmdMainService.getOrderRecmd(new TfOrderRecmd(recmdId));
                //不在允许的时间范围内
                logger.info("允许的时间范围内：" + timeRange(DictUtil.getDictValue("FLOW_GIVE", "BEGIN_DATE", "2018-04-01"),
                        DictUtil.getDictValue("FLOW_GIVE", "END_DATE", "2018-07-01")));
                if (timeRange(DictUtil.getDictValue("FLOW_GIVE", "BEGIN_DATE", "2018-04-01"),
                        DictUtil.getDictValue("FLOW_GIVE", "END_DATE", "2018-07-01"))) {

                    //判断是否为养卡用户
                    if (wtjkzqUsrDetailService.queryRpYktlmx(recmd.getRecmdPhone())) {
                        printKaInfo(recmd.getRecmdPhone() + "====>该号码为经分养卡用户，不再送流量！");
                        return;
                    }
                    printKaInfo(recmd.getRecmdPhone() + "====>该号码不是养卡！");

                    //先判断当月该用户是否已经赠送过
                    //被推荐人身份证号码 > 推荐人：无论什么业务（大王卡/不限量）：都只送一次
                    //条件：当月/推荐赠送/身份证号码
                    Date beignTime = DateUtils.parseDate(DateUtils.getDate("yyyy-MM"));//当月
                    TfFlowcouponsGive flowCouponCond = new TfFlowcouponsGive();
                    flowCouponCond.setCouponsId(TfCouponsConf.COPONS_ID_FLOW_SIM_RECMD_BOSS_200); //推荐赠送 todo
                    flowCouponCond.setStorageBeginTime(beignTime);//当月
                    flowCouponCond.setPsptId(orderSub.getDetailList().get(0).getOrderDetailSim().getPsptId());//身份证
                    int resultRecmd = flowCouponsGiveService.recmdCountOnePsptId(flowCouponCond).size();
                    printKaInfo(phone + "====>该号码当月已推荐下单" + resultRecmd + "次，大于1次，不再赠关流量！");
                    if (resultRecmd < 1) {
                        sendFlow(orderSub, contactPhone, recmd, beignTime);
                    }
                }
            }
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo, orderSub.getOrderSubNo(), "", getClass().getName(), "saveSimRecmdInfo",
                    "", processThrowableMessage(e));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            printKaError("saveSimRecmdInfo", e);
            TfSimOrderLog log = new TfSimOrderLog();
            log.setLogType("saveSimRecmdInfo#Error");
            log.setReqFlag1(orderSub.getOrderSubId() + "");
            log.setReqFlag2(phone);
            log.setReqArgs(sw.toString().substring(0, 2000));//报错内容
            orderLogService.addLog(log);
        }
    }

    /**
     * 流量赠送，调用boss接口
     *
     * @param orderSub
     * @param contactPhone
     * @param recmd
     * @param beignTime
     */
    private void sendFlow(final TfOrderSub orderSub, final String contactPhone, final TfOrderRecmd recmd, final Date beignTime) {
        //流量编码
        final String M500 = "99541463";//
        //== 保存信息,流量赠送
        TfFlowcouponsGive cond = new TfFlowcouponsGive();
        cond.setSerialNumber(recmd.getRecmdPhone());//推荐人的号码
        cond.setStorageBeginTime(beignTime);
        cond.setCouponsId(TfCouponsConf.COPONS_ID_FLOW_SIM_RECMD_BOSS_200);
        Page page = new Page<>(1, 10);
        cond.setPage(page);
        //注意：所有数据条目，而不只算推荐成功的
        Page<TfFlowcouponsGive> flowPage = flowCouponsGiveService.findPage(cond);
        printKaInfo("==流量赠送次数==>" + recmd.getRecmdPhone() + "  " + flowPage.getCount());
        if (flowPage.getCount() < TfFlowcouponsGive.FLOW_RECMD_MAX_VAL) {
            TfFlowcouponsGive flowcouponsGive = new TfFlowcouponsGive();
            try {
                flowcouponsGive.setCouponsId(TfCouponsConf.COPONS_ID_FLOW_SIM_RECMD_BOSS_200);
                flowcouponsGive.setOrderSubNo(orderSub.getOrderSubNo());
                flowcouponsGive.setSerialNumber(recmd.getRecmdPhone());//推荐人的号码
                flowcouponsGive.setRefBusiFlag1(orderSub.getOrderSubId() + "");
                flowcouponsGive.setSmsParam(contactPhone);
                flowcouponsGive.setGiveStatus(TfFlowcouponsGive.FLOW_COPON_STATUS_NO_GIVE);
                flowcouponsGive.setResultInfo(TfFlowcouponsGive.FLOW_COPON_INFO_NO_GIVE);
                printKaInfo("==saveSimRecmdInfo保存推荐送流量券信息==>" + JSONObject.toJSONString(flowcouponsGive));
                flowCouponsGiveService.saveFlowcouponsGiveInWap(flowcouponsGive);

            } catch (Exception e) {
                printKaError("==流量赠送入库失败==>" + recmd.getRecmdPhone(), e);
            }
        }

    }


    /**
     * 赠送后发送短信
     * extSms：短信模板
     * phone：需要发送短信的号码
     * smsParam：短信模板需要的参数
     * 字段配置格式：标记||短信内容
     */
    private void giveFlowSms(String extSms, String phone, String smsParam) {
        try {
            if (!com.ai.ecs.common.utils.StringUtils.isEmpty(extSms)) {
                String[] param = smsParam.split(TfFlowcouponsGive.SMS_PARAM_SPLIT);
                for (int i = 0; i < param.length; i++) {
                    extSms = extSms.replace("${arg" + i + "}", param[i]);//渲染短信模板
                }
                SmsSendCondition smsSendCond = new SmsSendCondition();
                smsSendCond.setSerialNumber(phone);
                smsSendCond.setNoticeContent(extSms);
                printKaInfo("====giveFlowSms短信请求===>" + phone +
                        JSONObject.toJSONString(smsSendCond, SerializerFeature.WriteMapNullValue));
                Map smsSendRetMap = smsSendService.sendSms(smsSendCond);
                printKaInfo("====giveFlowSms短信返回===>" + phone
                        + JSONObject.toJSONString(smsSendRetMap, SerializerFeature.WriteMapNullValue));
            }
        } catch (Exception e) {
            printKaError(phone + " 流量赠送短信发送失败！", e);
        }
    }

    /**
     * 大王卡办理成功跳转页面
     */
    @RequestMapping("toKingCardSuccess")
    public String toKingCardSuccess(String confId, String recmdCode, String cityCodeSuffix, Model model) {
        model.addAttribute("confId", confId);
        model.addAttribute("recmdCode", recmdCode);
        model.addAttribute("cityCodeSuffix", cityCodeSuffix);
        model.addAttribute("simBusiConfs", simBuyCommonService.getBusiConfig());
        return "web/goods/sim/recmd/orderSuccess";
    }


    /**
     * 大王卡用户办理新业务
     */
    @RequestMapping("simBusiOnline")
    @ResponseBody
    public Map<String, String> simBusiOnlineSubmit(String str, Model model, HttpServletRequest request, HttpServletResponse response) {
        String resp = "";
        Map<String, String> map = new HashMap<String, String>();
        str = request.getParameter("busi");
        //获取orderSubId
        Session session = UserUtils.getSession();
        TfOrderSub orderSub = (TfOrderSub) session.getAttribute("ORDER_RESULT");
        TfOrderSimBusi simBusi = new TfOrderSimBusi();
        //获取所选业务
        String[] busi = str.trim().split(",");
        //判断是否是10分钟以内的订单
        Date date = new Date();
        if (orderSub != null) {
            if (date.getTime() - orderSub.getOrderTime().getTime() > 600000 && busi.length > 0) {
                resp = "等待超时，无法办理业务！";
            }
        }
        //没有选择推荐业务
        if ("".equals(str.trim())) {
            resp = "success";
        }
        //判断该订单是否办理了业务，避免重复提交
        if (orderSub == null && busi.length > 0) {
            resp = "您已提交预约办理，请勿重复提交！";
        }
        //若没有超时，session不为空，且选择了推荐业务，则办理推荐业务
        if (orderSub != null && date.getTime() - orderSub.getOrderTime().getTime() < 600000 && busi.length > 0) {//600000
            //判断该订单是否办理了业务，避免重复提交
            simBusi.setOrderSubId(orderSub.getOrderSubId());
            List<TfOrderSimBusi> orderSimBusis = orderNewBusiService.querySimBusi(simBusi);
//            if(orderSimBusis.size() != 0 ){
//                resp = "您已提交预约办理，请勿重复提交！";
//            }
            if (orderSimBusis.size() == 0 && busi.length > 0) {
                String code = request.getParameter("cityCodeSuffix");
                for (int i = 0; i < busi.length; i++) {
                    if (!"".equals(busi[i])) {
                        Long confId = Long.parseLong(busi[i]);
                        TfOrderSimBusi newBusi = new TfOrderSimBusi();
                        newBusi.setOrderSubId(simBusi.getOrderSubId());
                        //查询该业务的配置信息
                        TfOrderSimBusiConf conf = orderNewBusiService.queryBusiConfList(new TfOrderSimBusiConf(confId)).get(0);
                        String discountsCode = conf.getDiscountsCode();
                        if (conf.getDiscountsCode().indexOf("**") != -1) {//优惠编码归属地市
                            discountsCode = conf.getDiscountsCode().replace("**", code);
                        }
                        newBusi.setDiscountsCode(discountsCode);
                        newBusi.setBusiName(conf.getBusiName());
                        newBusi.setPackageId(conf.getPackageId());
                        orderNewBusiService.saveSimBusi(newBusi);
                    }
                }
                resp = "success";
                session.removeAttribute("ORDER_RESULT");//清除session
            }
        }
        map.put("message", resp);
        model.addAttribute("confId", request.getParameter("confId"));
        model.addAttribute("recmdCode", request.getParameter("recmdCode"));
        map.put("confId", request.getParameter("confId"));
        map.put("recmdCode", request.getParameter("recmdCode"));
        return map;
    }

    private boolean checkIsGraduate(String num) throws ParseException {
        if (num.length() == 18) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date age = sdf.parse(num.substring(6, 14));
            Date downAge = sdf.parse("19941231");
            return age.after(downAge);
        }
        return false;
    }

}
