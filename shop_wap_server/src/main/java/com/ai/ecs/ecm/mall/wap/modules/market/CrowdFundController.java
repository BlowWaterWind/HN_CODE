package com.ai.ecs.ecm.mall.wap.modules.market;

import com.ai.ecs.cf.api.ICFManageService;
import com.ai.ecs.cf.api.ICFUserService;
import com.ai.ecs.cf.constant.CFConstant;
import com.ai.ecs.cf.entity.*;
import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderPay;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.common.CommonParams.ORDER_TYPE_MERCHANT;

/**
 * Created by hyc on 2017/7/19/019.
 * 众筹控制器
 */
@Controller
@RequestMapping("marketing/cf")
public class CrowdFundController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(CrowdFundController.class);

    private static String basePath = "web/marketing/cf/";

    @Autowired
    private ICFUserService cfUserService;
    @Autowired
    private ICFManageService cfManageService;
    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    ICompanyAcctService companyAcctService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    IGoodsManageService goodsManageService;

    @Autowired
    IOrderService orderService;

    @Value("${afterOrderPayUrl}")
    String afterOrderPayUrl;

    @Value("${orderPayUrl}")
    String orderPayUrl;

    @Autowired
    IUppHtmlValidataService validataService;

    private String imageServerPath = new PropertiesLoader("tfs-config.properties").getProperty("tfs.req.url");

    @RequestMapping("/initCfIndex")
    public Object initCfIndex(Long cfGoodsId, Model model, HttpServletRequest request) throws Exception {
        CFGoodsConf cfGoodsConf = null;
        CFOrder orderCond = null;

        if (!isHebaoLogin()) {
            throw new Exception("请先登录和包！");
        }
        if (!isBindCard()) {
            throw new Exception("和包绑卡用户才能参与！");
        }
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            throw new Exception("请先登录！");
        }
        if (member.getMemberLogin().getMemberPhone() == null) {
            throw new Exception("手机号码不能为空！");
        }
        //== 众筹商品信息
        cfGoodsConf = cfManageService.findCfConfigById(cfGoodsId);
        if (cfGoodsConf == null) {
            throw new Exception("商品不存在！");
        }
        if (cfGoodsConf.getStatus() != CFConstant.GOODS_STATUS_RUNNING) {
            throw new Exception("该商品众筹活动未上架！");
        }

        if (cfGoodsConf.getEndTime().before(new Date())) {
            throw new Exception("该商品众筹已经结束！");
        }


        //== 现在是多少期众筹信息/初始化
        //fixme 众筹商品信息一旦发布且众筹活动已经开始，则不能修改，只能下架或者重新配置，否则逻辑混乱，里面逻辑是否可以理清楚 待确认
        //redis当前期标识为空或者当前期与数据库中的最近一期不同，说明当前期需要初始化
        String curPeriodRedis = jedisCluster.get(CFConstant.CURPERIOD_CFGOODS_ID + cfGoodsId);
        int intval = DateUtils.getDateBias(cfGoodsConf.getStartTime(), new Date());
        //现在是第n期 （如果所有轮已经结束，但需要查看往期情况怎么办）
        Long curPeriod = intval / cfGoodsConf.getCycleDay() + 1;
        if (curPeriod > cfGoodsConf.getMaxPeriod()) {
            throw new Exception("该商品众筹已经结束！");
        }
        //redis失效或者redis故障引起逻辑混乱
        if (curPeriodRedis == null || curPeriod != cfGoodsConf.getCurPeriod()) {
            //为空表示以前某期已经过期，且数据库中没有最新的众筹期数信息，该次为该轮第一次访问，加锁插入
            if (!cfUserService.initCfPrize(cfGoodsConf, curPeriod)) {
                throw new Exception("该期众筹商品正在初始化设置，请稍后再试！！");
            }
        }
        //我的待支付订单
        orderCond = new CFOrder();
        orderCond.setCfGoodsId(cfGoodsId);
        orderCond.setUserId(member.getMemberLogin().getMemberId() + "");//任何登陆用户都可以参与
        orderCond.setPayStaus(CFConstant.PAY_STAUTS_NONE);
        orderCond.setBuyPeriod(curPeriod);
        List<CFOrder> orders = cfManageService.findCfOrdersByCond(orderCond).getList();
        model.addAttribute("watiPayOrders", orders);
        model.addAttribute("curPeriod", curPeriod);


        //== 所有轮众筹信息
        TfCfPrizeInf prizeCond = new TfCfPrizeInf();
        prizeCond.setGoodsId(cfGoodsConf.getCfGoodsId());
        List<TfCfPrizeInf> prizeInfs = cfManageService.findPrizes(prizeCond).getList();
        TfCfPrizeInf newestprizeCond = prizeInfs.get(0);
        if (!CFConstant.PRIZE_STATUS_RUNNING.equals(newestprizeCond.getStatus())) {
            model.addAttribute("message", "本轮众筹活动结束！");
        }
        model.addAttribute("prizeInfId", newestprizeCond.getPrizeId());
        //== 该商品的订单信息
        orderCond = new CFOrder();
        orderCond.setCfGoodsId(cfGoodsId);
        orderCond.setPayStaus(CFConstant.PAY_STAUTS_ALEADY);
        CFOrder orderTemp = cfManageService.findRmbByCond(orderCond);
        Page<CFOrder> page = cfManageService.findCfOrdersByCond(orderCond);

        CFOrder orderResult = new CFOrder();
        orderResult.setAllPeriodRmb(orderTemp == null ? 0 : orderTemp.getCurPeriodRmb());//所有期已筹总金额
        orderResult.setAllPeoNum((long) page.getList().size());//所有期总参与人数
        orderCond.setBuyPeriod(newestprizeCond.getPeriod().longValue());
        orderTemp = cfManageService.findRmbByCond(orderCond);
        page = cfManageService.findCfOrdersByCond(orderCond);
        orderResult.setBuyPeriod(newestprizeCond.getPeriod().longValue());//现在期数
        orderResult.setCurPeriodRmb(orderTemp == null ? 0 : orderTemp.getCurPeriodRmb());//该期已筹多少元
        orderResult.setParPeoNum((long) page.getList().size());//该期参与人数

        //==该商品最近一期众筹信息(中奖信息)
        int sucTs = 0;//已成功被筹台数
        if (prizeInfs.size() > 0) {
            orderResult.setRemainDays(DateUtils.getDateBias(new Date(), prizeInfs.get(0).getEndTime()));//该期剩余天数
            //循环处理号码
            for (TfCfPrizeInf prizeInf : prizeInfs) {
                if (CFConstant.ZC_SUC.equals(prizeInf.getIsOpenPrize())) {
                    String s = prizeInf.getPrizePhoneNum().substring(0, 3) + "****" + prizeInf.getPrizePhoneNum().substring(7, 11);
                    prizeInf.setPrizePhoneNum(s);
                    sucTs += 1;
                }
            }
        }


        model.addAttribute("sucTs", sucTs);
        model.addAttribute("orderResult", orderResult);
        model.addAttribute("prizeInfs", prizeInfs);
        model.addAttribute("page", page);

        model.addAttribute("cfGoodsConf", cfGoodsConf);
        return basePath + "indexCf";
    }

    /**
     * 查看我的众筹号码
     */
    @RequestMapping("/lookNo")
    @ResponseBody
    public Object lookNo(Long cfGoodsId, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        //查询该用户是否已经登陆
        MemberVo member = UserUtils.getLoginUser(request);
        ResponseBean resp = new ResponseBean();
        try {
            if (member != null) {
                CFOrder orderCond = new CFOrder();
                orderCond.setCfGoodsId(cfGoodsId);
                orderCond.setUserId(member.getMemberLogin().getMemberId() + "");//任何登陆用户都可以参与
                orderCond.setPayStaus(CFConstant.PAY_STAUTS_ALEADY);
                List<CFOrder> orders = cfManageService.findCfOrdersByCond(orderCond).getList();
                resp.addSuccess(JSONObject.toJSONString(orders, SerializerFeature.WriteMapNullValue));
            } else {
                resp.addError("请先登陆！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("resp", resp);
        return map;
    }

    /**
     * 跳转购买选择页面，或者直接购买档单品页
     */
    @RequestMapping(value = "/toBuy", method = RequestMethod.POST)
    public String toBuy(CFOrder order, HttpServletRequest request, Model model, String buyType) throws Exception {
        //查询用户是否已经登陆
        MemberVo member = UserUtils.getLoginUser(request);

        if (!isHebaoLogin()) {
            throw new Exception("请先登录和包！");
        }
        if (!isBindCard()) {
            throw new Exception("和包绑卡用户才能参与！");
        }
        CFGoodsConf cfGoodsConf = cfManageService.findCfConfigById(order.getCfGoodsId());
        TfCfPrizeInf prizeInf = cfManageService.selectPrizeInfByPrimaryKey(order.getPrizeId());
        LoggerFactory.getLogger("webDbLog").info("toBuy cfGoodsConf:" + net.sf.json.JSONObject.fromObject(cfGoodsConf).toString());
        LoggerFactory.getLogger("webDbLog").info("toBuy prizeInf:" + net.sf.json.JSONObject.fromObject(prizeInf).toString());

        //==选中的是"抽奖档"还是"购买档"
        if (CFConstant.CF_BUY_TYPE_NORMAL.equals(buyType)) {
            //跳转购买档地址 TODO
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("containGoodsStaticInfo", true);
            map.put("containShopGoodsChannelRef", true);
            map.put("goodsSkuId", cfGoodsConf.getGoodsSkuId());
            map.put("chnlCode", "E007");
            List<TfGoodsInfo> goods = goodsManageService.queryGoodsInfoByCds(map);
            if (CollectionUtils.isNotEmpty(goods)) {
                try {
                    if (goods.size() > 0) {
                        if (CollectionUtils.isNotEmpty(goods.get(0).getTfShopGoodsChannelRefList()) && StringUtils.isNotEmpty(goods.get(0).getTfShopGoodsChannelRefList().get(0).getGoodsUrl())) {
                            return "redirect:" + adminPath + "/goods/" + goods.get(0).getTfShopGoodsChannelRefList().get(0).getGoodsUrl();
                        }
                    }
                } catch (Exception e) {
                    logger.error("填充商品路径错误：", e);
                }
            }
            throw new Exception("该商品暂不支持原价档购买!");
        } else {
            if (cfGoodsConf == null || prizeInf == null) {
                throw new Exception("众筹商品或众筹信息不存在！");
            }
            //==查询用户该期是否已经购买最大份数
            CFOrderCond cfOrderCond = new CFOrderCond();
            cfOrderCond.setUserId(member.getMemberLogin().getMemberId() + "");
            cfOrderCond.setCfGoodsId(order.getCfGoodsId());
            cfOrderCond.setBuyPeriod(cfGoodsConf.getCurPeriod());
//                cfOrderCond.setPayStaus(CFConstant.PAY_STAUTS_ALEADY);
            cfOrderCond.setStatusList(Arrays.asList(new Integer[]{CFConstant.ORDER_STATUS_CREATE}));

            //当前用户已经购买的份数

            Long userNum = cfManageService.selctByUserPreOrderTotalNum(cfOrderCond);
            LoggerFactory.getLogger("webDbLog").info("toBuy 用户已经购买数:" + userNum);
            //用户允许购买份数1 = 单人可以买的份数 - 已经买了的份数
            Long maxAllowNum = cfGoodsConf.getSingleTotalBuyNumber() - userNum;
            if (maxAllowNum < 1) {
                model.addAttribute("cfGoodsConf", cfGoodsConf);
                model.addAttribute("maxAllowNum", maxAllowNum);
                model.addAttribute("message", "该轮众筹您已经购买了单人允许的最大份数！");
                return basePath + "buyCf";
            }
//                order.setPayStaus(CFConstant.PAY_STAUTS_ALEADY);
            //用户允许购买份数2 = 总份数 - 已经购买的份数
//            CFOrder or = cfManageService.findRmbByCond(order);//已经支付的订单
            cfOrderCond.setUserId(null);
            Long totalNumPay = cfManageService.selctByUserPreOrderTotalNum(cfOrderCond);
            LoggerFactory.getLogger("webDbLog").info("toBuy 所有用户已经购买数:" + totalNumPay);

            long tmpAllowNum = cfGoodsConf.getTotalNumber() - totalNumPay;

            if (tmpAllowNum < maxAllowNum) {
                maxAllowNum = tmpAllowNum;
                if (maxAllowNum < 1) {
                    model.addAttribute("message", "您订购的众筹商品存库不足");
                    model.addAttribute("prizeInf", prizeInf);
                    model.addAttribute("cfGoodsConf", cfGoodsConf);
                    model.addAttribute("maxAllowNum", maxAllowNum);
                    return basePath + "buyCf";
                }
            }
            model.addAttribute("prizeInf", prizeInf);
            model.addAttribute("cfGoodsConf", cfGoodsConf);
            model.addAttribute("maxAllowNum", maxAllowNum);
        }

        return basePath + "buyCf";
    }

    /**
     * 生成订单，跳转支付方式选择页面
     */
    @RequestMapping(value = "/toPay")
    public String toPay(CFOrder order, HttpServletRequest request, Model model) throws Exception {
        if (!isHebaoLogin()) {
            throw new Exception("请先登录和包！");
        }
        if (!isBindCard()) {
            throw new Exception("和包绑卡用户才能参与！");
        }
        //查询用户是否已经登陆
        MemberVo member = UserUtils.getLoginUser(request);
        if (order.getCfOrderId() == null) {
            order.setUserId(member.getMemberLogin().getMemberId() + "");
            CFCreateOrderParam param = new CFCreateOrderParam();
            param.setUserId(member.getMemberLogin().getMemberId());
            param.setCfGoodsId(order.getCfGoodsId());
            param.setBuyNumber(order.getBuyNumber());
            param.setPhone(member.getMemberLogin().getMemberPhone());
            param.setUserIp(getRemoteAddr(request));
            param.setPrizeId(order.getPrizeId());
            LoggerFactory.getLogger("webDbLog").info("toPay createCFOrder param:" + net.sf.json.JSONObject.fromObject(param).toString());

            order = cfUserService.createCFOrder(param);
        }

        //查询用户该期购买量，是否已经超过最大份额
//            Long singleTotalNumber = cfGoodsConf.getSingleTotalBuyNumber();
//        if (cfCreateOrderParam.getBuyNumber().longValue() > singleTotalNumber.longValue()) {
//            throw new Exception("您订购众筹商品不能大于" + singleTotalNumber + "个");
//        }
//        if(cfCreateOrderParam.getBuyNumber().longValue()>cfGoodsConf.getTotalNumber()-prizeInf.getPrizeNumber()){
//            throw new Exception("您订购众筹商品不能大于" + (cfGoodsConf.getTotalNumber()-prizeInf.getPrizeNumber()) + "个");
//        }

        //查询份数是否已经超过最大份额

        //是否是最后一个人

        //生成订单

        //
        LoggerFactory.getLogger("webDbLog").info("toPay createCFOrder result:" + net.sf.json.JSONObject.fromObject(order).toString());
        model.addAttribute("order", order);
        return basePath + "paySelectCf";
    }


    /**
     * 支付
     *
     * @param order
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/payOrder")
    public void payOrder(CFOrder order, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        if (!isHebaoLogin()) {
            throw new Exception("请先登录和包！");
        }
        if (!isBindCard()) {
            throw new Exception("和包绑卡用户才能参与！");
        }
        order = cfManageService.selectByPrimaryKey(order.getCfOrderId());
        LoggerFactory.getLogger("webDbLog").info("payOrder order:" + net.sf.json.JSONObject.fromObject(order).toString());

        CFGoodsConf cfGoodsConf = cfManageService.findCfConfigById(order.getCfGoodsId());
        LoggerFactory.getLogger("webDbLog").info("payOrder cfGoodsConf:" + net.sf.json.JSONObject.fromObject(cfGoodsConf).toString());

        String payPlatform = request.getParameter("payPlatform");
        if (StringUtils.isBlank(payPlatform)) {
            payPlatform = CFConstant.CF_PAY_PLAT_FORM;
        }
        Short payPlat = CommonParams.PAY_PLATFORM.get(payPlatform);
        if (payPlat == null) {
            payPlat = 2;
        }
            /* 分润规则 */
        String shRule = "";
        //店铺为移动平台店铺且订单类型为裸机或配件
        if ("1".equals(order.getShopId())) {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("goodsId", cfGoodsConf.getGoodsId());
            List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoByCds(paramMap);
            Long supplierShopId = goodsList.get(0).getShopGoodsRef().getSupplierShopId();
            LoggerFactory.getLogger("webDbLog").info("payOrder supplierShopId:" + supplierShopId);

            //获取供货商商户分润账户
            CompanyAcctInfo companyAcctInfo = companyAcctService.getAcctByShopIdAndType(supplierShopId, payPlat);

            if (companyAcctInfo == null) {
                logger.error(String.format("供货商%s未配置账户", supplierShopId));
                throw new Exception("该店铺暂不支持该支付方式，推荐您使用和包完成支付。");
            }
            shRule = companyAcctInfo.getAccountNum() + "^" + order.getPayTotalSum() + "^众筹";
        } else {
            //获取供货商商户分润账户
            CompanyAcctInfo companyAcctInfo = companyAcctService.getAcctByShopIdAndType(cfGoodsConf.getShopId(), payPlat);
            if (companyAcctInfo == null) {
                logger.error(String.format("%s未配置账户", cfGoodsConf.getShopName()));
                throw new Exception("该店铺暂不支持该支付方式，推荐您使用和包完成支付。");
            }
            shRule = companyAcctInfo.getAccountNum() + "^" + order.getPayTotalSum() + "^众筹";
        }
        LoggerFactory.getLogger("webDbLog").info("payOrder shRule:" + shRule);

        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();

        JSONObject innerMerchant = ORDER_TYPE_MERCHANT.get(OrderConstant.TYPE_BARE);
        LoggerFactory.getLogger("webDbLog").info("payOrder innerMerchant:" + innerMerchant);

        String merchantId = innerMerchant.getString("merchantId");
        String key = innerMerchant.getString("key");
        String callbackUrl = basePath + "/marketing/cf/linkToPayResult";
        String notifyUrl = afterOrderPayUrl + "/marketing/cf/afterPayOrder";
        String type = CommonParams.PAY_INTERFACE_TYPE.get(payPlatform);


        String orderDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String payType = CommonParams.PAY_TYPE.get(OrderConstant.TYPE_BARE);
        String orderPayAmount = String.valueOf(order.getPayTotalSum());
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("payOrg", payPlatform);//支付机构：
        paramMap.put("shRule", shRule);//分账支付分润规则
        paramMap.put("type", type);//接口类型
        paramMap.put("callbackUrl", callbackUrl);//同步页面返回地址
        paramMap.put("notifyUrl", notifyUrl);//异步接收支付结果地址
        paramMap.put("merchantId", merchantId);//统一支付平台分配的唯一商户编号
        paramMap.put("version", "2.0.0");//版本号：固定为2.0.0
        paramMap.put("characterSet", "UTF-8");//编码方式
        paramMap.put("channelId", "E021");//渠道：暂固定为E021
        paramMap.put("amount", orderPayAmount);//订单金额
        paramMap.put("currency", "00");//币种
        paramMap.put("orderDate", orderDate);//订单提交日期
        paramMap.put("merchantOrderId", String.valueOf(order.getCfOrderId()));//商户订单号
        paramMap.put("merAcDate", orderDate);//商户会计日期，与订单提交日期保持一致
        paramMap.put("productDesc", cfGoodsConf.getGoodsName());//商品介绍
        paramMap.put("productName", cfGoodsConf.getGoodsName());//商品名称
        paramMap.put("payType", payType);//支付类型
        paramMap.put("period", "30");//有效期数量
        paramMap.put("periodUnit", "00");//有效期单位：00-分，01-小时，02-日，03-月
        paramMap.put("reserved1", order.getPayTotalSum() / 100 + "元");//有效期单位：00-分，01-小时，02-日，03-月
        paramMap.put("hmac", UppCore.getHmac(paramMap, key, "UTF-8"));//签名数据
        LoggerFactory.getLogger("webDbLog").info("payOrder paramMap:" + paramMap);

        String content = UppCore.sentHttp2(orderPayUrl, paramMap);
//            订单支付信息
        TfOrderPay orderPay = new TfOrderPay();
        orderPay.setOrderId(order.getCfOrderId());
        orderPay.setOrderPayAmount(Long.parseLong(orderPayAmount));
        orderPay.setOrgCode(payPlatform);
        orderPay.setShRule(shRule);
        orderPay.setHmac(key);
        orderPay.setOrderHarvestExpend("0");
        orderPay.setMerchantId(merchantId);
        MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
        orderPay.setOrderPayPerson(memberLogin.getMemberLogingName());
        orderService.saveOrderPay(orderPay);


        response.setContentType("text/html;charset=" + CommonParams.PAY_CHARSET.get(payPlatform));
        PrintWriter out = response.getWriter();
        out.print(content);
        out.flush();
        out.close();
//            return "redirect:"+adminPath + "/marketing/cf/afterPayOrder?returnCode=0000&message=ok&type=SHGWDirectPayToPayOrg&version=2.0.0&amount="+orderPayAmount+"&payDate=20170825170338&orderId="+order.getCfOrderId()+"&payNo="+order.getCfOrderId();


//        } catch (Exception e) {
//            logger.error("众筹支付订单异常:" + e.getMessage());
//            e.printStackTrace();
//            throw new Exception("众筹支付订单异常：" + e.getMessage());
//        }
    }

    /**
     * 跳转到订单支付结果页面，供支付中心调用（同步）
     *
     * @param model
     * @param returnCode
     * @param chargeflowId
     * @return
     */
    @RequestMapping("/linkToPayResult")
    public String linkToAlipayResult(Model model, String returnCode, Long chargeflowId) {
        LoggerFactory.getLogger("webDbLog").info("linkToPayResult returnCode:" + returnCode);
        LoggerFactory.getLogger("webDbLog").info("linkToPayResult chargeflowId:" + chargeflowId);
        try {
            CFOrder order = cfManageService.selectByPrimaryKey(chargeflowId);
            model.addAttribute("order", order);
            model.addAttribute("returnCode", returnCode);
        } catch (Exception e) {
            LoggerFactory.getLogger("webDbLog").info("linkToPayResult 支付同步回调失败，异常信息:" + e);
            logger.error("支付同步回调失败，异常信息:" + e);
        }
        return basePath + "paySuccess";
    }

    /**
     * 订单支付后的操作处理，供支付中心调用（异步）
     *
     * @param merchantId
     * @param returnCode
     * @param message
     * @param type
     * @param version
     * @param amount
     * @param orderId
     * @param payDate
     * @param status
     * @param payNo
     * @param org_code
     * @param organization_payNo
     */
    @RequestMapping("/afterPayOrder")
    public void afterPayOrder(HttpServletRequest request, String merchantId, String returnCode, String message, String type,
                              String version, Long amount, Long orderId, String payDate,
                              String status, String payNo, String org_code,
                              String organization_payNo) {
        LoggerFactory.getLogger("webDbLog").info("afterPayOrder 支付异步回调参数[{}]:" + net.sf.json.JSONObject.fromObject(request.getParameterMap()).toString());
        try {
            MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
            Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
            if (!validataService.valHmac(payParamMap, merChantBean)) {
                throw new Exception("签名验证未通过");
            }

            cfUserService.payCFOrder(orderId, payNo, org.apache.commons.lang3.time.DateUtils.parseDate(payDate, "yyyyMMddHHmmss"));
            //订单支付信息
            TfOrderPay orderPay = new TfOrderPay();
//            orderPay.setMerchantId(merchantId);
            orderPay.setMessage(message);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setOrderId(orderId);
            orderPay.setOrderPayAmount(amount);
            orderPay.setOrderPayTime(org.apache.commons.lang3.time.DateUtils.parseDate(payDate, "yyyyMMddHHmmss"));
            orderPay.setOrgCode(org_code);
            orderPay.setPayLogId(organization_payNo);
            orderPay.setPayNo(payNo);
            orderPay.setPayState(status);
            orderPay.setReturnCode(returnCode);
            orderPay.setType(type);
            orderPay.setVersion(version);
            orderPay.setHmac(null); // hmac改存支付签名的key值，在支付时设置好，不在此处做更改
//            orderPay.setMerchantId(null); // 在支付时设置好，不在此处做更改
            orderService.updateOrderPay(orderPay);
        } catch (Exception e) {
            LoggerFactory.getLogger("webDbLog").info("afterPayOrder 支付异步回调失败，异常信息:" + e);
        }
//        return "redirect:"+adminPath + "/marketing/cf/linkToPayResult?returnCode="+returnCode+"&chargeflowId="+orderId;
    }

    /**
     * 查看计算详情
     *
     * @param prizeId
     * @param model
     * @return
     */
    @RequestMapping("/initCalDetail")
    public Object calDetail(Long cfGoodsId, Long prizeId, Model model) {
        TfCfPrizeInf prizeInf = new TfCfPrizeInf();
        prizeInf.setPrizeId(prizeId);
        prizeInf = cfManageService.getPrizeInfByCon(prizeInf);
        CFGoodsConf cfGoodsConf = null;
        CFOrder orderCond = new CFOrder();

        try {
            //== 众筹商品信息
            cfGoodsConf = cfManageService.findCfConfigById(cfGoodsId);
            if (cfGoodsConf == null) {
                model.addAttribute("message", "该商品不存在！");
                return basePath + "indexCf";
            }
            //中奖者本轮的订单信息
            orderCond.setCfGoodsId(cfGoodsId);
            orderCond.setBuyPeriod(cfGoodsConf.getCurPeriod());
            orderCond.setPayStaus(CFConstant.PAY_STAUTS_ALEADY);
            orderCond.setUserPhone(prizeInf.getPrizePhoneNum());
            List<CFOrder> orders = cfManageService.findCfOrdersByCond(orderCond).getList();
            int myAllPayAmount = 0;
            for (CFOrder order : orders) {
                String s = order.getUserPhone().substring(0, 3) + "****" + order.getUserPhone().substring(7, 11);
                order.setUserPhone(s);
                myAllPayAmount += order.getPayTotalSum();
                if (order.getCfOrderId().equals(prizeInf.getPrizeOrderId())) {
                    model.addAttribute("myOrder", order);
                }
            }
            model.addAttribute("myOrderList", orders);
            model.addAttribute("myAllPayAmount", myAllPayAmount);


            // 查询最后支付N条记录
            List<CFOrder> cfOrderList = cfManageService.selectPaySuccessLastRow(cfGoodsConf.getCfGoodsId());
            for (CFOrder order : cfOrderList) {
                String s = order.getUserPhone().substring(0, 3) + "****" + order.getUserPhone().substring(7, 11);
                order.setUserPhone(s);
            }
            //== 所有轮众筹信息
            String userPhone = prizeInf.getPrizePhoneNum().substring(0, 3) + "****" + prizeInf.getPrizePhoneNum().substring(7, 11);
            prizeInf.setPrizePhoneNum(userPhone);
            model.addAttribute("cfGoodsConf", cfGoodsConf);
            model.addAttribute("prizeInf", prizeInf);
            model.addAttribute("cfOrderList", cfOrderList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("cfGoodsConf", cfGoodsConf);
        return basePath + "calDetail";
    }

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

    /**
     * 获得用户远程地址
     */
    private static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (com.ai.ecs.common.utils.StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (com.ai.ecs.common.utils.StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (com.ai.ecs.common.utils.StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 点赞
     */
    @RequestMapping("/like")
    @ResponseBody
    public Object like(Long cfGoodsId, String flag, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        ResponseBean resp = new ResponseBean();
        try {
            CFGoodsConf cfGoodsConf = cfManageService.findCfConfigById(cfGoodsId);
            Long popularity = cfGoodsConf.getPopularity() == null ? 0 : cfGoodsConf.getPopularity();
            if ("like".equals(flag)) {
                popularity += 1;
            } else if ("unlike".equals(flag)) {
                popularity -= 1;
            }
            cfGoodsConf.setPopularity(popularity);
            cfManageService.modCfConfig(cfGoodsConf);
            resp.addSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("resp", resp);
        return map;
    }
}