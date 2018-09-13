package com.ai.ecs.ecm.mall.wap.modules.market;

import com.ai.ecs.activity.ActivityCutConstant;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.HeBaoController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.VisualActivityInfo;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.sys.entity.User;
import com.ai.ecs.goods.api.IGoodsStaticService;
import com.ai.ecs.goods.entity.goods.OrderGoodsMerge;
import com.ai.ecs.goods.entity.goods.TfGoodsSkuAttr;
import com.ai.ecs.goods.entity.goods.TfGoodsSkuValue;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.constant.OrderConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/5/12.
 */
@Controller
@RequestMapping("kill")
public class KillController extends HeBaoController {

    @Autowired
    private KillRedisManage killRedisManage;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private BasicInfoQryModifyService basicInfoQryModifyService;
    @Autowired
    private IGoodsStaticService goodsStaticService;

    // 2016年4月1日前入网用户才有入网资格
    private static Date netBeforeDate = null;
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 3, 1, 0, 0, 0);
        netBeforeDate = calendar.getTime();
    }


    @RequestMapping("/init")
    public String initPage(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        // 1. 如果访问时， 带活动id. 以活动id为准
        String activityId = request.getParameter("activityId");
        List<VisualActivityInfo> visualActivityInfos = null;
        visualActivityInfos = getVisualActivityList();
        if (StringUtils.isEmpty(activityId)) {
            // 获取redis缓存中存在的秒杀活动
            if (visualActivityInfos == null || visualActivityInfos.isEmpty()) {
                 logger.error("活动信息不存在");
                 throw new Exception("您访问的活动不存在");
            } else {
                // 当前系统的相对秒数
                long curSystemTime = System.currentTimeMillis();
                activityId = getActivityId(visualActivityInfos, curSystemTime);
            }
        }

        if (StringUtils.isEmpty(activityId)) {
            logger.error("活动信息不存在!");
            throw new Exception("您访问的活动不存在!");
        }


        // 当前活动信息
        String activityInfoStrs = jedisCluster.get("ACTIVITY_DATA_" + activityId);
        if (activityInfoStrs != null) {
            try {
                VisualActivityInfo visualActivityInfo = JSONObject.parseObject(activityInfoStrs, VisualActivityInfo.class);
                String sActivityAttrs = visualActivityInfo.getActivityDesc();
                if (sActivityAttrs != null && sActivityAttrs.length() > 0) {
                    model.addAttribute("activityAttrs", sActivityAttrs.split("\r\n"));
                }


                model.addAttribute("isBindCard", isBindCard());
                model.addAttribute("isShare", isShare(activityId, getPhone(request)));
                model.addAttribute("visualActivityInfo", visualActivityInfo);
                model.addAttribute("systemTime", System.currentTimeMillis());
                model.addAttribute("visualActivityList", visualActivityInfos);
                Date startDate = new Date(visualActivityInfo.getStartDate());
                model.addAttribute("inToday", inToday(startDate));

                List<String> lBuyUsers = jedisCluster.lrange("ACTIVITY_CHECK_ONE_" + activityId, 0, -1);
                List<String> lFormatUsers = null;
                if (lBuyUsers != null && !lBuyUsers.isEmpty()) {
                    lFormatUsers = new ArrayList<String>();
                    for (String phone : lBuyUsers) {
                        lFormatUsers.add(ActivityCutConstant.formatPhone(phone));
                    }
                    model.addAttribute("lFormatUsers", lFormatUsers);
                }
            } catch (Exception e) {
                logger.error("活动信息不存在!！");
                throw new Exception("您访问的活动不存在!！");
            }
        }
        return "web/kill/index";
    }


    /**
     * // 活动都是以开始时间降序排列， 并活动开始期间没有重叠为基础计算。
     // 1. 2017.05.26 00:00:00 2017.05.27 23:59:59
     // 2. 2017.05.07 00:00:00 2017.05.08 23:59:59
     // 3. 2017.04.21 00:00:00 2017.04.22 23:59:59
     * @param visualActivityInfos
     * @param curSystemTime
     * @return
     */
    public static String getActivityId(List<VisualActivityInfo> visualActivityInfos, long curSystemTime) {
        int len = visualActivityInfos.size();
        String activityId = null;
        if (len == 1) { // 如果只有一个活动
            activityId = visualActivityInfos.get(0).getActivityId();
        } else {
            for (int i = len - 1; i >= 0;  i--) {
                VisualActivityInfo visualActivityInfo = visualActivityInfos.get(i);
                long startDate = visualActivityInfo.getStartDate();
                long endDate = visualActivityInfo.getEndDate();

                // 当前时间小于第一个活动开始时间， 返回当前活动。
                if (curSystemTime <= startDate) {
                    return visualActivityInfo.getActivityId();
                }

                // 当前时间大于最后一个活动开始时间时， 返回最后一个活动。
                if (curSystemTime >= startDate && i == 0) {
                    return visualActivityInfo.getActivityId();
                }

                // 在活动期间内， 返回当前活动。, 并延时5分钟， 展示当前活动。
                if (curSystemTime >= startDate && curSystemTime <= (endDate + 5 * 60 * 1000)) {
                    return visualActivityInfo.getActivityId();
                }
            }
        }
        return activityId;
    }

    /**
     * 获取秒杀列表
     * @return
     */
    private List<VisualActivityInfo> getVisualActivityList() {
        try {
            String activityInfosStr = this.jedisCluster.get("KILL_QUERYLIST_CACHE");
            if (activityInfosStr != null && activityInfosStr.length() > 0) {
                List<VisualActivityInfo> list = JSONObject.parseArray(activityInfosStr, VisualActivityInfo.class);
                return list;
            }
        } catch (Exception e) {
            logger.error("获取秒杀列表失败。", e);
        }
        return null;
    }

    private boolean isBindCard() {
        Session session = UserUtils.getSession();
        String loginBaseKey = "LOGIN_BASEINFO_" + session.getId();
        String json = JedisClusterUtils.get(loginBaseKey);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getBoolean("bindCard") == null ? false : jsonObject.getBoolean("bindCard").booleanValue();
    }

    private boolean inToday(Date startDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        Calendar totalCalendar = Calendar.getInstance();
        totalCalendar.setTime(new Date());

        if (startCalendar.get(Calendar.YEAR) == totalCalendar.get(Calendar.YEAR) &&
            startCalendar.get(Calendar.MONTH) == totalCalendar.get(Calendar.MONTH) &&
            startCalendar.get(Calendar.DAY_OF_MONTH) == totalCalendar.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            return false;
        }
    }

    public String getPhone(HttpServletRequest request) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        String phone = memberVo.getMemberLogin().getMemberPhone() + "";
        return phone;
    }

    public static final String ERROR_1 = "1";  // 请求参数不合法
    //public static final String ERROR_2 = "2";  // 系统异常
    //public static final String ERROR_3 = "3";  // 活动信息不合法
    public static final String ERROR_4 = "4";  // 请求过大
    public static final String ERROR_5 = "5";  // 用户入网时间没有符合条件
    public static final String ERROR_6 = "6";  // 手太慢, 库存已空
    public static final String ERROR_7 = "7";  // 需要分享
    public static final String ERROR_8 = "8";  // 未绑卡
    public static final String ERROR_9 = "9";  // 已购买不能再次购买
    public static final long MAX_ACTIVE_NUM = 120L;


    @ResponseBody
    @RequestMapping("/getKillQualification")
    public JSONObject getKillQualification(HttpServletRequest request) throws Exception {
        JSONObject returnJson = new JSONObject();
        Long now = System.currentTimeMillis();

        String phone = getPhone(request);
        String activityId =  request.getParameter("activityId");

        // 请求参数验证
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(activityId)) {
            returnJson.put("FLAG", ERROR_1);
            return returnJson;
        }

        if (!isShare(activityId, phone)) {
            returnJson.put("FLAG", ERROR_7);
            return returnJson;
        }

        // 没有绑卡
        if (!isBindCard()) {
            returnJson.put("FLAG", ERROR_8);
            return returnJson;
        }


        String key = "KILL_" + activityId + "_" + phone;
        boolean lockedFlag = false;
        try {
            lockedFlag = killRedisManage.tryLock(key);   // 给单个用户设置一把全局锁， 防止一个用户重复发送请求
            // 当没有获取个人redis锁， 直接返回
            if (!lockedFlag) {
                returnJson.put("FLAG", ERROR_1);
                return returnJson;
            }

            // 从redis中获取用户的活动信息
            VisualActivityInfo visualActivityInfo = this.getActivity(activityId);

            // 如果没有获取到活动信息时， 直接返回
            if (visualActivityInfo == null) {
                returnJson.put("FLAG", ERROR_1);
                return returnJson;
            }

            // 验证活动的有效信
            if (visualActivityInfo.getStartDate() > now) {
                returnJson.put("FLAG", ERROR_1);
                return returnJson;
            } else if (visualActivityInfo.getEndDate() < now) {
                returnJson.put("FLAG", ERROR_1);
                return returnJson;
            } else if (visualActivityInfo.getActivityStock() <= 0) { // 查看库存数量
                returnJson.put("FLAG", ERROR_6);
                return returnJson;
            }

            Session session = UserUtils.getSession();
            Boolean getQualiSuccess = (Boolean)session.getAttribute("hasQualiFlag");
            // 已经获取得了资格, 直接返回
            if (Boolean.TRUE.equals(getQualiSuccess)) {
                returnJson.put("FLAG", "0");
                return returnJson;
            }

            // 获取活动资格
            Long activityMaxQuali = visualActivityInfo.getActivityMaxQuali();   // 最多的资格数量
            if (activityMaxQuali == null) {
                activityMaxQuali = MAX_ACTIVE_NUM;
            }

            if (activityMaxQuali >= MAX_ACTIVE_NUM) {
                activityMaxQuali = MAX_ACTIVE_NUM;
            }

            String alreadyBuyKey = "ACTIVITY_CHECK_ONE" + phone;
            String value = jedisCluster.get(alreadyBuyKey);
            // 7 对于6个月内秒杀成功的用户在后台对其进行限制不能再秒杀成功
            if (!StringUtils.isEmpty(value)) {
                returnJson.put("FLAG", ERROR_9);

                return returnJson;
            }

            boolean lockedQualiFlag = false;   // 是否获取了资格
            String qualiKey = "KILL_QUALI" + "_" + activityId;

            String killQualiNumKey = "KILL_QUALI_NUM_" + activityId;
            try {
                lockedQualiFlag = killRedisManage.tryLock(qualiKey, 3, TimeUnit.SECONDS);  // 一个活动的全局锁
                if (lockedQualiFlag) {
                    long lQualiNum = 0L;
                    // 如果之前增加过资格， redis中的值不为空
                    String qualiNum = jedisCluster.get(killQualiNumKey);
                    if (qualiNum != null && qualiNum.length() > 0) {
                        lQualiNum = Long.parseLong(qualiNum);
                    }
                    if (activityMaxQuali <= lQualiNum) {  // 资格已满
                        logger.info("资格已满");
                    } else {
                        jedisCluster.incr(killQualiNumKey);   // 资格数量加1
                        getQualiSuccess = true;
                    }
                }
            } finally {
                if (lockedQualiFlag) {
                    killRedisManage.unLock(qualiKey);
                }
            }

            // 如果没有获取资格， 直接返回
            if (!Boolean.TRUE.equals(getQualiSuccess)) {
                returnJson.put("FLAG", ERROR_4);
                return returnJson;
            }

            // 6. 本次终端秒杀需要求客户在网1年以上，即2016年4月1日前入网
            Date userInNetDate = getUserInNetDate(phone);
            // 如果获取用户入网时间为空时， 或晚于2016年4月1入网的用户， 移除秒杀资格
            if (userInNetDate == null || netBeforeDate.before(userInNetDate)) {
                jedisCluster.decr(killQualiNumKey);   // 资格减1
                returnJson.put("FLAG", ERROR_5);
                return returnJson;
            } else {
                // 获取了秒杀资格
                session.setAttribute("hasQualiFlag", Boolean.TRUE);
                returnJson.put("FLAG", "0");
                return returnJson;
            }
        } catch (Exception e) {
            logger.error("获取秒杀资格失败", e);
            returnJson.put("FLAG", ERROR_1);
            return returnJson;
        } finally {
            if (lockedFlag) {
                killRedisManage.unLock(key);
            }
        }
    }

    /**
     * 秒杀订单提交页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/initKillConfirmOrder")
    public String initKillConfirmOrder(HttpServletRequest request) throws Exception {
        long now = System.currentTimeMillis();
        String activityId = request.getParameter("activityId");
        String phone = this.getPhone(request);

        // 请求参数验证
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(activityId)) {
            throw new Exception("请求参数错误！");
        }

        Long lActivityId = Long.parseLong(activityId);
        // 从redis中获取用户的活动信息
        VisualActivityInfo visualActivityInfo = this.getActivity(activityId);

        // 如果没有获取到活动信息时， 直接返回
        if (visualActivityInfo == null) {
            throw new Exception("请求参数错误！");
        }

        if(visualActivityInfo.getActivityStock() <= 0) {
            throw new Exception("手慢啦~宝贝被抢光啦~更多精彩活动请至活动专区");
        }

        Boolean falg = (Boolean)UserUtils.getSession().getAttribute("hasQualiFlag");
        if (!Boolean.TRUE.equals(falg)) {
            throw new Exception("请求出错， 请从秒杀页面进入");
        }

        OrderGoodsMerge orderGoodsMerge = queryDBOrderGoodsMerge(visualActivityInfo.getGoodsSkuId());

        UserGoodsCarModel userGoodsCarModel = new UserGoodsCarModel();
        userGoodsCarModel.setMarketId(lActivityId);
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
        tfUserGoodsCar.setMarketId(lActivityId);
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

        return "forward:/goodsBuy/linkToConfirmOrder?marketId=" + lActivityId;
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



    public VisualActivityInfo getActivity(String activityId) {
        String activityJson = jedisCluster.get("ACTIVITY_DATA_" + activityId);
        VisualActivityInfo activityInfo = null;
        if (activityJson != null && activityJson.length() > 0) {
            activityInfo = JSONObject.parseObject(activityJson, VisualActivityInfo.class);
        }
        return activityInfo;
    }

    public Date getUserInNetDate(String phone) {
        BasicInfoCondition bCondition = new BasicInfoCondition();
        bCondition.setSerialNumber(phone);
        bCondition.setxGetMode("0");
        bCondition.setStaffId("ITFWTNNN");
        bCondition.setTradeDepartPassword("909880");
        String inDate = null;
        Date dDate = null;
        try {
            // 查询用户的基础信息
            Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(bCondition);
            if (MapUtils.isNotEmpty(userInfo) && HNanConstant.SUCCESS.equals(userInfo.get("respCode"))) {
                List<Map<String, Object>> userList = (List<Map<String, Object>>)userInfo.get("result");
                if (userList != null && !userList.isEmpty()) {
                    Map<String, Object> userBasicInfo = userList.get(0);
                    inDate = MapUtils.getString(userBasicInfo, "IN_DATE");
                }
            }
        } catch (Exception e) {
            logger.error("查询用户信息失败", e);
        }

        // 解析时间
        if (inDate != null && inDate.length() >0 ) {
            try {
                dDate = DateUtils.parseDate(inDate, "yyyy-MM-dd hh:mm:ss");
            } catch (Exception e) {
                logger.error("解析时间报错", e);
            }
        }
        return dDate;
    }
}
