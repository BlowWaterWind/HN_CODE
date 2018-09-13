package com.ai.ecs.ecm.mall.wap.modules.Bargain;

import com.ai.ecs.activity.ActivityCutConstant;
import com.ai.ecs.activity.CutException;
import com.ai.ecs.activity.api.IActivityTargetNumberService;
import com.ai.ecs.activity.entity.ActivityCutPrice;
import com.ai.ecs.activity.entity.ActivityCutPriceDetail;
import com.ai.ecs.activity.entity.ActivityGoodsPara;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.JsonUtil;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.HeBaoController;
import com.ai.ecs.ecm.mall.wap.modules.goods.controller.FinancialProcess;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.VisualActivityInfo;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSend4AllChanCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.goods.api.IGoodsStaticService;
import com.ai.ecs.goods.entity.goods.OrderGoodsMerge;
import com.ai.ecs.goods.entity.goods.TfGoodsSkuAttr;
import com.ai.ecs.goods.entity.goods.TfGoodsSkuValue;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/5.
 */
@Controller
@RequestMapping(value="/bargainInHeBao")
public class BargainHeBaoController extends HeBaoController {


    @Autowired
    private IOrderQueryService orderQueryService;
    @Autowired
    private IActivityTargetNumberService activityTargetNumberService;
    @Autowired
    BasicInfoQryModifyService  basicInfoQryModifyService;
    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private IGoodsStaticService goodsStaticService;
    @Autowired
    private FinancialProcess financialProcess;

    private static Pattern PHONE_PATTERN = Pattern.compile("^1[34578]\\d{9}$");
    private static int SEND_MSG_TIME = 300;

    protected Logger logger = LoggerFactory.getLogger(BargainHeBaoController.class);


    private boolean isBindCard() {
        Session session = UserUtils.getSession();
        String loginBaseKey = "LOGIN_BASEINFO_" + session.getId();
        String json = JedisClusterUtils.get(loginBaseKey);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getBoolean("bindCard") == null ? false : jsonObject.getBoolean("bindCard").booleanValue();
    }

    private boolean isHebaoLogin() {
        Session session = UserUtils.getSession();
        String loginBaseKey = "LOGIN_BASEINFO_" + session.getId();
        String json = JedisClusterUtils.get(loginBaseKey);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getBoolean("loginFromHeApp") == null ? false : jsonObject.getBoolean("loginFromHeApp").booleanValue();
    }

    @RequestMapping("shareToFlowInit")
    @ResponseBody
    public JSONObject shareToFlow(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String orderSubId = request.getParameter("orderSubId");
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(orderSubId)) {
            jsonObject.put("FLAG", 1);
            jsonObject.put("MSG", "订单号不能为空！");
            return jsonObject;
        }

        try {
            if (!isHebaoLogin()) {
                jsonObject.put("FLAG", 1);
                jsonObject.put("MSG", "只能在和包客户端中才能分享！！");
                return jsonObject;
            }

            TfOrderSub tfOrderSubCond = new TfOrderSub();
            TfOrderUserRef tfOrderUserRef = new TfOrderUserRef();
            tfOrderUserRef.setMemberId(UserUtils.getLoginUser(request).getMemberInfo().getMemberId());
            tfOrderSubCond.setOrderSubId(Long.parseLong(orderSubId));
            tfOrderSubCond.setOrderUserRef(tfOrderUserRef);
            TfOrderSub orderInfo = orderQueryService.queryBaseOrder(tfOrderSubCond);


            // 如果没有生成订单，或订单不为活动订单时，
            if (orderInfo == null || StringUtils.isEmpty(orderInfo.getPromotionId())) {
                jsonObject.put("FLAG", 1);
                jsonObject.put("MSG", "您的订单不能赚送流量");
                return jsonObject;
            }

            TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
            orderSubDetail.setOrderSubId(Long.parseLong(orderSubId));
            List<TfOrderSubDetail> list = orderQueryService.queryOrderDetailList(orderSubDetail);
            if (list == null || list.isEmpty()) {
                jsonObject.put("FLAG", 1);
                jsonObject.put("MSG", "您的订单不能赚送流量");
                return jsonObject;

            }
            // 类型
            jsonObject.put("FLAG", 0);
            jsonObject.put("type", orderInfo.getPromotionTypeId());
            jsonObject.put("phoneName", list.get(0).getGoodsName());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理失败", e);
            jsonObject.put("FLAG", 1);
            jsonObject.put("MSG", "系统正忙, 请稍后再试！！");
            return jsonObject;
        }
    }

    @RequestMapping("sendFlow")
    @ResponseBody
    public JSONObject sendFlow(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String orderSubId = request.getParameter("orderSubId");
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(orderSubId)) {
            jsonObject.put("FLAG", 1);
            jsonObject.put("MSG", "订单号不能为空！");
            return jsonObject;
        }

        try {
            if (!isHebaoLogin()) {
                jsonObject.put("FLAG", 1);
                jsonObject.put("MSG", "只能在和包客户端中才能分享！！");
                return jsonObject;
            }


            long lOrderSubId = Long.parseLong(orderSubId);
            TfOrderSub tfOrderSubCond = new TfOrderSub();
            TfOrderUserRef tfOrderUserRef = new TfOrderUserRef();
            tfOrderUserRef.setMemberId(UserUtils.getLoginUser(request).getMemberInfo().getMemberId());
            tfOrderSubCond.setOrderSubId(lOrderSubId);
            tfOrderSubCond.setOrderUserRef(tfOrderUserRef);

            TfOrderSub orderInfo = orderQueryService.queryBaseOrder(tfOrderSubCond);
            if (orderInfo != null && !StringUtils.isEmpty(orderInfo.getPromotionId())) {
                if ("2".equals(orderInfo.getPromotionTypeId()) || "10".equals(orderInfo.getPromotionTypeId())) {
                    boolean flag = activityTargetNumberService.addShareFlow(lOrderSubId);
                    if (flag) {
                        // 暂时不赠送流量， 屏蔽入口
                        boolean isFlag = false; //financialProcess.sendFlowPackage(getPhone(request), "1706060019", "170606001901", orderInfo.getOrderSubNo(), request.getRemoteAddr());
                        if (isFlag) {
                            activityTargetNumberService.updateShareFlow(lOrderSubId);
                            jsonObject.put("FLAG", 0);
                            jsonObject.put("MSG", "流量赠送成功！！");
                        } else {
                            jsonObject.put("FLAG", 1);
                            jsonObject.put("MSG", "流量赠送失败！！");
                        }
                    } else {
                        jsonObject.put("FLAG", 1);
                        jsonObject.put("MSG", "流量已赠送！");
                    }
                }
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理失败", e);
            jsonObject.put("FLAG", 1);
            jsonObject.put("MSG", "系统正忙, 请稍后再试！！");
            return jsonObject;
        }
    }

    @RequestMapping("addToShare")
    @ResponseBody
    public JSONObject addToShare(HttpServletRequest request) throws Exception {
        String activityId = request.getParameter("activityId");
        return super.addToShare(activityId, getPhone(request));
    }

    /**
     * 进入和包初始化方法
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping("init")
    public String init(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        String bargainCode = request.getParameter("bargainCode");
        // 查询离当前时间较近的活动
        ActivityGoodsPara activityGoodsParaInfo = null;
        if (StringUtils.isBlank(bargainCode)) {
            ActivityGoodsPara activityGoodsPara = new ActivityGoodsPara();
            activityGoodsPara.setCrmCode("0601");
            List<ActivityGoodsPara> activityGoodsParas = activityTargetNumberService.queryActivityGoodsParaListForBuy(activityGoodsPara);
            // 获取砍价信息离当前时间比较近的活动。
            if (activityGoodsParas != null && !activityGoodsParas.isEmpty()) {
                activityGoodsParaInfo = getActivityGoodsPara(activityGoodsParas, System.currentTimeMillis());
            }
        } else {
            activityGoodsParaInfo = activityTargetNumberService.getActivityGoodsParaInfo(bargainCode);
        }


        if (activityGoodsParaInfo != null) {
            model.addAttribute("activityGoodsPara", activityGoodsParaInfo);
            model.addAttribute("isBindCard", isBindCard());
            // model.addAttribute("isShare", isShare(activityGoodsParaInfo.getActCode(), this.getPhone(request)));
            return "web/cut/index";
        } else {
            throw new Exception("砍价活动信息不存在！");
        }
    }

    /**
     * // 活动都是以开始时间降序排列， 并活动开始期间没有重叠为基础计算。
     // 1. 2017.05.26 00:00:00 2017.05.27 23:59:59
     // 2. 2017.05.07 00:00:00 2017.05.08 23:59:59
     // 3. 2017.04.21 00:00:00 2017.04.22 23:59:59
     * @param activityGoodsParas
     * @param curSystemTime
     * @return
     */
    public static ActivityGoodsPara getActivityGoodsPara(List<ActivityGoodsPara> activityGoodsParas, long curSystemTime) {
        int len = activityGoodsParas.size();
        if (len == 1) { // 如果只有一个活动
            return activityGoodsParas.get(0);
        } else {
            for (int i = len - 1; i >= 0;  i--) {
                ActivityGoodsPara activityGoodsPara = activityGoodsParas.get(i);
                long startDate = activityGoodsPara.getStartTime().getTime();
                long endDate = activityGoodsPara.getEndTime().getTime();

                // 当前时间小于第一个活动开始时间， 返回当前活动。
                if (curSystemTime <= startDate) {
                    return activityGoodsPara;
                }

                // 当前时间大于最后一个活动开始时间时， 返回最后一个活动。
                if (curSystemTime >= startDate && i == 0) {
                    return activityGoodsPara;
                }

                // 在活动期间内， 返回当前活动。, 并延时5分钟， 展示当前活动。
                if (curSystemTime >= startDate && curSystemTime <= (endDate + 5 * 60 * 1000)) {
                    return activityGoodsPara;
                }
            }
        }
        return null;
    }

    public String getPhone(HttpServletRequest request) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        String phone = memberVo.getMemberLogin().getMemberPhone() + "";
        return phone;
    }


    @RequestMapping("joinCut")
    @ResponseBody
    public JSONObject joinCut(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            String bargainCode = request.getParameter("bargainCode");
            if (StringUtils.isBlank(bargainCode)) {
                throw new Exception("参数不正确！！");
            }

            String phone = getPhone(request);
            /*if (!isShare(bargainCode, phone)) {
                CutException cutException = new CutException("需要分享到朋友圈");
                cutException.setCode(ActivityCutConstant.EXCEPTION_INFO_CARD_NEED_SHARE);
                throw cutException;
            }*/

            if (!isBindCard()) {
                CutException cutException = new CutException("需要绑卡");
                cutException.setCode(ActivityCutConstant.EXCEPTION_INFO_NEED_CARD);
                throw cutException;
            }


            ActivityCutPrice activityCutPrice = activityTargetNumberService.joniCut(bargainCode, phone);
            jsonObject.put("success", true);
            jsonObject.put("activityCutPrice", JSON.toJSONString(activityCutPrice));
        } catch (Exception e) {
            jsonObject.put("success", false);
            if(e instanceof CutException) {
                jsonObject.put("errorCode", ((CutException)e).getCode());
            } else {
                jsonObject.put("errorCode", ActivityCutConstant.EXCEPTION_INFO_SYSTEM_ERROR);
            }
            logger.error("joinCut", e);
        }
        return jsonObject;
    }

    @RequestMapping("initMyCut")
    public String initMyCut(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String bargainCode = request.getParameter("bargainCode");
        String phone = this.getPhone(request);
        if (!StringUtils.isBlank(bargainCode)) {
            // 查询活动信息
            ActivityGoodsPara activityGoodsPara = activityTargetNumberService.getActivityGoodsParaInfo(bargainCode);
            if (activityGoodsPara != null) {
                 // 查询用户发布的活动信息
                ActivityCutPrice activityCutPrice = activityTargetNumberService.queryActivityCutPriceByCutCodeAndPhone(bargainCode, phone);
                if (activityCutPrice != null) {
                    BigDecimal partDis = new BigDecimal(activityCutPrice.getTotalAmount());  // 已砍价拆扣金额
                    BigDecimal totalDis = new BigDecimal(activityGoodsPara.getDisMount());   // 总的拆扣金额
                    double orgTotalPrice = Double.parseDouble(activityGoodsPara.getCostPrice());  // 商品原价
                    double curTotalPrice = orgTotalPrice - activityCutPrice.getTotalAmount();  // 现价

                    BigDecimal result = null;
                    if (partDis.compareTo(totalDis) < 0) {
                        result = partDis.divide(totalDis, 10, RoundingMode.FLOOR);
                        result = result.multiply(new BigDecimal(100));
                    } else {
                        result = new BigDecimal(100);
                    }

                    result.setScale(2, RoundingMode.FLOOR);
                    model.addAttribute("curSalePrice", curTotalPrice);
                    model.addAttribute("cutPercentage", result.toString());
                    model.addAttribute("activityCutPrice", activityCutPrice);
                }
                model.addAttribute("activityGoodsPara", activityGoodsPara);
                return "web/cut/mybargain";
            } else {
                throw new Exception("访问出错");
            }
        } else {
            throw new Exception("访问出错");
        }
    }

    @RequestMapping("initHelpCut")
    public String initHelpCut(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String id = request.getParameter("id");
        if (!StringUtils.isBlank(id)) {
            // 查询砍价信息
            ActivityCutPrice activityCutPrice = activityTargetNumberService.queryActivityCutPriceById(id);
            if (activityCutPrice != null) {
                // 查询活动信息
                ActivityGoodsPara activityGoodsPara = activityTargetNumberService.getActivityGoodsParaInfo(activityCutPrice.getActCode());
                BigDecimal partDis = new BigDecimal(activityCutPrice.getTotalAmount());  // 已砍价拆扣金额
                BigDecimal totalDis = new BigDecimal(activityGoodsPara.getDisMount());   // 总的拆扣金额
                double orgTotalPrice = Double.parseDouble(activityGoodsPara.getCostPrice());  // 商品原价
                double curTotalPrice = orgTotalPrice - activityCutPrice.getTotalAmount();  // 现价

                BigDecimal result = null;
                if (partDis.compareTo(totalDis) < 0) {
                    result = partDis.divide(totalDis, 10, RoundingMode.FLOOR);
                    result = result.multiply(new BigDecimal(100));
                } else {
                    result = new BigDecimal(100);
                }

                result.setScale(2, RoundingMode.FLOOR);
                model.addAttribute("curSalePrice", curTotalPrice);
                model.addAttribute("cutPercentage", result.toString());
                model.addAttribute("activityCutPrice", activityCutPrice);
                model.addAttribute("activityGoodsPara", activityGoodsPara);
                return "web/cut/otherbargain";
            } else {
                throw new Exception("访问出错");
            }
        } else {
            throw new Exception("访问出错");
        }
    }

    @RequestMapping(value = "sendRandomCode")
    @ResponseBody
    public Map<String, Object> sendRandomCode(HttpServletRequest request, String mobile) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        if (mobile == null || mobile.length() != 11 || !PHONE_PATTERN.matcher(mobile).matches()) { // 手机号码格式不正确
            result.put("X_RESULTINFO", "请输入正确湖南移动号码！");
            result.put("X_RESULTCODE", "-1");
            return result;
        }

        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(mobile);
        //Map<String, Object> resultMap = null;
        try {
            logger.debug("验证号码开始mobile:" + mobile);

            BasicInfoCondition bCondition = new BasicInfoCondition();
            bCondition.setSerialNumber(mobile);
            bCondition.setxGetMode("0");
            bCondition.setStaffId("ITFWTNNN");
            bCondition.setTradeDepartPassword("909880");
            Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(bCondition);
            JSONObject phoneAttributionJSON = JSONObject.parseArray(userInfo.get("result").toString()).getJSONObject(0);
            //logger.info(JSON.toJSONString(userInfo,true));
            String phoneNumberResultcode = phoneAttributionJSON.getString("X_RESULTCODE");
            if(!"0".equals(phoneNumberResultcode)) {
                result.put("X_RESULTINFO", "非湖南移动用户不能参与砍价活动！");
                result.put("X_RESULTCODE", "-1");
                return result;
            }
            logger.debug("验证号码结束mobile:" + mobile);
        } catch (Exception e) {
            logger.error("查询手机号码归属地出错", e);
            result.put("X_RESULTINFO", "获取号码信息异常， 请稍后再试");
            result.put("X_RESULTCODE", "-1");
            return result;
        }

        // 当PROVINCE_CODE为731时， 为湖南省号码
       /* if (resultMap != null && "731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))) {
            logger.debug(mobile + "此号码为湖南省号码");
        } else {
            logger.error("resultMap:" + resultMap);
            result.put("X_RESULTINFO", "请输入正确湖南移动号码！");
            result.put("X_RESULTCODE", "-1");
            return result;
        }
*/
        try {
            if (jedisCluster.exists("BargainSmsSimMaxBomb_" + mobile)) {  //判断5分钟内是否发送过短信
                result.put("X_RESULTINFO", "您的短信密码已发送到手机，5分钟内不重复发送");
                result.put("X_RESULTCODE", "-1");
                return result;
            }

            SmsSend4AllChanCondition smsSend4AllChanCondition = new SmsSend4AllChanCondition();
            smsSend4AllChanCondition.setSERIAL_NUMBER(mobile);
            smsSend4AllChanCondition.setCacheKey(mobile + "BargainSms_SMS_WEB");
            //允许用户输入错误三次
            smsSend4AllChanCondition.setErrorTimes(3);
            smsSend4AllChanCondition.setCodeLength(6);
            smsSend4AllChanCondition.setNOTICE_CONTENT("湖南移动提醒您：本次参加砍价活动获得的验证码为：{$}, 本验证码5分钟内有效。");
            result = smsSendService.sendSms4AllChan(smsSend4AllChanCondition);
            //result = new HashMap<String, Object>();
            result.put("X_RESULTCODE", "0");
            if (MapUtils.isNotEmpty(result) && "0".equals(result.get("X_RESULTCODE"))) {
                jedisCluster.setex("BargainSmsSimMaxBomb_" + mobile, SEND_MSG_TIME, "1");
                String code = JedisClusterUtils.get(mobile + "BargainSms_SMS_WEB");
                logger.error("本次发送的短息验证码${}，手机号码{}", code, mobile);
                result.put("TIME", SEND_MSG_TIME);
            }
            return result;
        } catch (Exception e) {
            logger.error("sendRandomCode", e);
            result.put("X_RESULTINFO", "发送短息失败， 请稍后再试");
            result.put("X_RESULTCODE", "-1");
            return result;
        }
    }

    @RequestMapping(value = "helpCut")
    @ResponseBody
    public JSONObject helpCut(HttpServletRequest request, String mobile, String randomCode, String cutPriceId) throws Exception {
        JSONObject result = new JSONObject();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(randomCode) || StringUtils.isEmpty(cutPriceId)) {
            result.put("X_RESULTCODE", "-1");
            result.put("X_RESULTINFO", "参数错误！");
            return result;
        }

        SmsSend4AllChanCondition smsSend4AllChanCondition = new SmsSend4AllChanCondition();
        smsSend4AllChanCondition.setSERIAL_NUMBER(mobile);
        smsSend4AllChanCondition.setCodeValue(randomCode);
        smsSend4AllChanCondition.setCacheKey(mobile + "BargainSms_SMS_WEB");
        try {
            Map<String, Object> smsResult = smsSendService.CheckSms4AllChan(smsSend4AllChanCondition);
            //Map<String, Object> smsResult = new HashMap<String, Object>();
            //smsResult.put("X_RESULTCODE","0");
            String isSuccess = MapUtils.getString(smsResult, "X_RESULTCODE");
            if (!"0".equals(isSuccess)) {
                result.put("X_RESULTCODE", "-1");
                String message = MapUtils.getString(smsResult, "message");
                if ("短信密码错误".equals(message)) {
                    message = "您输入短信验证码错误， 请重新输入！";
                } else {
                    message = "您输入的验证码已过期， 请点击获取按钮重新操作！";
                }
                result.put("X_RESULTINFO", message);
                return result;
            }
        } catch (Exception e) {
            result.put("X_RESULTCODE", "-1");
            result.put("X_RESULTINFO", "系统繁忙， 请稍后重试！");
            return result;
        }

        try {
            ActivityCutPriceDetail activityCutPriceDetail = activityTargetNumberService.helpCut(cutPriceId, mobile);
            result.put("X_RESULTCODE", "0");
            result.put("cutAmount", activityCutPriceDetail.getCutAmount());
        } catch (Exception e) {
            if(e instanceof CutException) {
                result.put("X_RESULTCODE", ((CutException)e).getCode());
            } else {
                result.put("X_RESULTCODE", ActivityCutConstant.EXCEPTION_INFO_SYSTEM_ERROR);
            }
            logger.error("joinCut", e);
        }
        return result;
    }


    @RequestMapping("iniShareCut")
    public String iniShareCut(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String bargainCode = request.getParameter("bargainCode");
        String phone = this.getPhone(request);
        if (!StringUtils.isBlank(bargainCode)) {
            // 查询活动信息
            ActivityGoodsPara activityGoodsPara = activityTargetNumberService.getActivityGoodsParaInfo(bargainCode);
            if (activityGoodsPara != null) {
                // 查询用户发布的活动信息
                ActivityCutPrice activityCutPrice = activityTargetNumberService.queryActivityCutPriceByCutCodeAndPhone(bargainCode, phone);
                if (activityCutPrice != null) {
                    BigDecimal partDis = new BigDecimal(activityCutPrice.getTotalAmount());  // 已砍价拆扣金额
                    BigDecimal totalDis = new BigDecimal(activityGoodsPara.getDisMount());   // 总的拆扣金额
                    double orgTotalPrice = Double.parseDouble(activityGoodsPara.getCostPrice());  // 商品原价
                    double curTotalPrice = orgTotalPrice - activityCutPrice.getTotalAmount();  // 现价

                    BigDecimal result = null;
                    if (partDis.compareTo(totalDis) < 0) {
                        result = partDis.divide(totalDis, 10, RoundingMode.FLOOR);
                        result = result.multiply(new BigDecimal(100));
                    } else {
                        result = new BigDecimal(100);
                    }

                    result.setScale(2, RoundingMode.FLOOR);
                    model.addAttribute("curSalePrice", curTotalPrice);
                    model.addAttribute("cutPercentage", result.toString());
                    model.addAttribute("activityCutPrice", activityCutPrice);
                }
                model.addAttribute("activityGoodsPara", activityGoodsPara);
                return "web/cut/share";
            } else {
                throw new Exception("访问出错");
            }
        } else {
            throw new Exception("访问出错");
        }
    }

    @RequestMapping("initGoOrder")
    public String initGoOrder(HttpServletRequest request, HttpServletResponse response, Model model, String bargainCode) throws Exception {
        String phone = this.getPhone(request);
        if (!StringUtils.isBlank(bargainCode)) {
            ActivityGoodsPara activityGoodsPara = activityTargetNumberService.getActivityGoodsParaInfo(bargainCode);
            if (activityGoodsPara != null) {
                Date now = new Date();
                if (now.after(activityGoodsPara.getEndTime())) {
                    throw new Exception("活动已过期");
                }


                // 查询用户发布的活动信息
                ActivityCutPrice activityCutPrice = activityTargetNumberService.queryActivityCutPriceByCutCodeAndPhone(bargainCode, phone);
                if (activityCutPrice == null) {
                    throw new Exception("您的砍价信息不存在");
                }

                if (!"1".equals(activityCutPrice.getStatus())) {
                    throw new Exception("您的砍价信息状态不正确");
                }


                String skuId = activityGoodsPara.getSkuCode();

                OrderGoodsMerge orderGoodsMerge = queryDBOrderGoodsMerge(skuId);


                UserGoodsCarModel userGoodsCarModel = new UserGoodsCarModel();
                List<TfUserGoodsCar> userGoodsCarList = new ArrayList<TfUserGoodsCar>();
                TfUserGoodsCar tfUserGoodsCar = new TfUserGoodsCar();
                userGoodsCarList.add(tfUserGoodsCar);

                tfUserGoodsCar.setShopId(orderGoodsMerge.getDbShopGoodsRef().getShopId());
                tfUserGoodsCar.setShopName(orderGoodsMerge.getDbShopGoodsRef().getShopName());
                tfUserGoodsCar.setShopTypeId(orderGoodsMerge.getDbShopGoodsRef().getCompanyTypeId() == null ? 6 : orderGoodsMerge.getDbShopGoodsRef().getCompanyTypeId().intValue());
                tfUserGoodsCar.setGoodsId(orderGoodsMerge.getDbTfGoodsInfo().getGoodsId());
                tfUserGoodsCar.setGoodsName(orderGoodsMerge.getDbTfGoodsInfo().getGoodsName());
                tfUserGoodsCar.setGoodsShortDesc(orderGoodsMerge.getDbTfGoodsInfo().getGoodsShortDesc());
                tfUserGoodsCar.setCategoryId(orderGoodsMerge.getDbTfGoodsInfo().getCategoryId());
                tfUserGoodsCar.setProdBossCode(orderGoodsMerge.getDbTfGoodsInfo().getProdBossCode());
                tfUserGoodsCar.setGoodsSkuId(orderGoodsMerge.getDbTfGoodsSkuValue().getGoodsSkuId());
                tfUserGoodsCar.setGoodsType(Long.parseLong(orderGoodsMerge.getDbTfGoodsSkuValue().getGoodsType()));

                tfUserGoodsCar.setGoodsBuyNum(1L);
                tfUserGoodsCar.setIsChecked("Y");
                tfUserGoodsCar.setGoodsSalePrice(orderGoodsMerge.getDbTfGoodsSkuValue().getGoodsSalePrice());
                tfUserGoodsCar.setGoodsSkuUrl(orderGoodsMerge.getDbTfGoodsStatic().getGoodsStaticUrl());

                List<TfGoodsSkuAttr> goodsSkuAttrs = orderGoodsMerge.getDbTfGoodsSkuAttrList();
                String goodsStandFormat = "";
                String goodsStandId = "";
                for (int i = 0, len = goodsSkuAttrs.size(); i < len; i++) {
                    TfGoodsSkuAttr tfGoodsSkuAttr = goodsSkuAttrs.get(i);
                    goodsStandFormat += tfGoodsSkuAttr.getGoodsAttrName() + "=" + tfGoodsSkuAttr.getGoodsAttrValue();
                    goodsStandId += tfGoodsSkuAttr.getProdAttrId() + "=" + tfGoodsSkuAttr.getProdAttrValueId();
                    if (i != len - 1) {
                        goodsStandFormat += "&";
                        goodsStandId += "&";
                    }
                }
                tfUserGoodsCar.setGoodsStandardAttr(goodsStandFormat);
                tfUserGoodsCar.setGoodsStandardAttrId(goodsStandId);


                userGoodsCarModel.setUserGoodsCarList(userGoodsCarList);
                request.setAttribute("carModel", userGoodsCarModel);

                //Session session = UserUtils.getSession();
                //session.setAttribute("carModel", userGoodsCarModel);

               return "forward:/goodsBuy/linkToConfirmOrder";
            } else {
                throw new Exception("访问出错");
            }
        } else {
            throw new Exception("访问出错");
        }
    }

    /**
     * 查询sku信息
     * @param skuId
     * @return
     * @throws Exception
     */
    public OrderGoodsMerge queryDBOrderGoodsMerge(String skuId) throws Exception {
        Long lSkuId = Long.parseLong(skuId);
        TfGoodsSkuValue queryGoodsInfo = new TfGoodsSkuValue();
        queryGoodsInfo.setGoodsSkuId(lSkuId);
        queryGoodsInfo.setChnlCode(OrderConstant.CHANNEL_SHOPWAP);
        queryGoodsInfo.setGoodsType("2");
        try {
            OrderGoodsMerge orderGoodsMerge  = goodsStaticService.selectOrderSkuValueInfo(queryGoodsInfo);
            if (orderGoodsMerge == null) {
                logger.error("没有查询sku相关信息【" + skuId + "】");
                throw new Exception("没有查询到您订购的商品信息！");
            } else {
                return orderGoodsMerge;
            }
        } catch (Exception e) {
            logger.error("查询sku信息失败", e);
            throw new Exception("您请求的服务正忙， 请稍后再试");
        }
    }

}
