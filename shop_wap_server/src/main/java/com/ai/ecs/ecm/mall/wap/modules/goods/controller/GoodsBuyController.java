package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.activity.api.IActivityTargetNumberService;
import com.ai.ecs.activity.entity.ActivityCutPrice;
import com.ai.ecs.activity.entity.ActivityGoodsPara;
import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.common.web.Servlets;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.VisualActivityInfo;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.ecsite.modules.contract.entity.TerminalCampOnCondition;
import com.ai.ecs.ecsite.modules.contract.service.ContractSaleService;
import com.ai.ecs.ecsite.modules.contractSale.entity.ContractSaleCheckCondition;
import com.ai.ecs.ecsite.modules.contractSale.service.ContractSaleCheckService;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNum;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNumQueryCondition;
import com.ai.ecs.ecsite.modules.netNumServer.entity.SelTempOccupyResCondition;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.ICommonPropagandaService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.api.IGoodsQueryService;
import com.ai.ecs.goods.api.IGoodsSkuService;
import com.ai.ecs.goods.api.IGoodsSkuValueService;
import com.ai.ecs.goods.api.IGoodsStaticService;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.api.IUserGoodsCarService;
import com.ai.ecs.goods.api.inter.IStockInterfaceLogService;
import com.ai.ecs.goods.entity.CommonPropaganda;
import com.ai.ecs.goods.entity.TfCategory;
import com.ai.ecs.goods.entity.goods.GoodsWater;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsSku;
import com.ai.ecs.goods.entity.goods.TfGoodsSkuValue;
import com.ai.ecs.goods.entity.goods.TfGoodsSkuValueExt;
import com.ai.ecs.goods.entity.goods.TfGoodsStatic;
import com.ai.ecs.goods.entity.goods.TfShopGoodsChannelRef;
import com.ai.ecs.goods.entity.goods.TfShopGoodsRef;
import com.ai.ecs.goods.entity.goods.TfStockUpdateLog;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberFavoriteService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.IMemberMarketSignInService;
import com.ai.ecs.member.api.IPhonenumSelectService;
import com.ai.ecs.member.api.register.IRegisterService;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.member.entity.MemberFollow;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.TfMemberMarketSignIn;
import com.ai.ecs.member.entity.TfPhonenumSelect;
import com.ai.ecs.merchant.busiHall.IOrgBusinessHallService;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.merchant.entity.TdEcOrgInfo;
import com.ai.ecs.merchant.entity.TdOrgBusinessHall;
import com.ai.ecs.merchant.shop.pojo.Shop;
import com.ai.ecs.order.api.IOrder10085UpdateService;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.api.busi.ISimBusiService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfDeliveryMode;
import com.ai.ecs.order.entity.TfOrder;
import com.ai.ecs.order.entity.TfOrderDetailContract;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.ai.ecs.order.entity.TfOrderPay;
import com.ai.ecs.order.entity.TfOrderReceiptTime;
import com.ai.ecs.order.entity.TfOrderRecipient;
import com.ai.ecs.order.entity.TfOrderRecommendContact;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubCoupon;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.ai.ecs.order.entity.TfPayMode;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.ecs.sales.api.ICouponService;
import com.ai.ecs.sales.api.IMarketService;
import com.ai.ecs.sales.entity.CouponInfo;
import com.ai.ecs.terminalrisk.api.ITerminalRiskOrderService;
import com.ai.ecs.terminalrisk.entity.TerminalRiskOrderQueryCond;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.service.IPayBankService;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * 商品购买控制器
 * 包含提交订单、支付订单
 */
@Controller
@RequestMapping("goodsBuy")
public class GoodsBuyController extends BaseController {

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef"
            + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";

    @Autowired
    IGoodsQueryService goodsQueryService;
    @Autowired
    IOrderService orderService;
    @Autowired
    IMemberAddressService memberAddressService;
    @Autowired
    IOrderQueryService orderQueryService;
    @Autowired
    IPlansService plansService;
    @Autowired
    ICouponService couponService;
    @Autowired
    IMarketService marketService;
    @Autowired
    IGoodsStaticService goodsStaticService;
    @Autowired
    IGoodsSkuService goodsSkuService;
    @Autowired
    IPayBankService payBankService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ICommonPropagandaService commonPropagandaService;
    @Autowired
    ContractSaleCheckService contractSaleCheckService;
    @Autowired
    NetNumServerService netNumServerService;
    @Autowired
    IUserGoodsCarService userGoodsCarService;
    @Autowired
    IPhonenumSelectService phonenumSelectService;
    @Autowired
    IMemberFavoriteService memberFavoriteService;
    @Autowired
    IGoodsManageService goodsManageService;
    @Autowired
    ICompanyAcctService companyAcctService;
    @Autowired
    IOrgBusinessHallService hallService;
    @Autowired
    IGoodsSkuValueService goodsSkuValueService;
    @Autowired
    IMemberMarketSignInService signInService;
    @Autowired
    IUppHtmlValidataService validataService;
    @Autowired
    ContractSaleService contractSaleService;
    @Autowired
    IOrder10085UpdateService order10085UpdateService;
    @Autowired
    ITerminalRiskOrderService terminalRiskOrderService;
    @Autowired
    IStockInterfaceLogService stockInterfaceLogService;

    @Autowired
    IActivityTargetNumberService activityTargetNumberService;
    @Autowired
    ISimBusiService simBusiService;
    @Autowired
    IMemberLoginService loginService;
    @Autowired
    IRegisterService registerService;
    @Autowired
    private FinancialProcess financialProcess;
    @Autowired
    private JedisCluster jedisCluster;

    @Value("${orderPayUrl}")
    String orderPayUrl;
    @Value("${afterOrderPayUrl}")
    String afterOrderPayUrl;
    @Value("${discountTime}")
    String discountTime;
    @Value("${financialTest}")
    String financialTest;
    @Value("${simOrderPayNum}")
    String simOrderPayNum; //号卡金额配置

    private static String IMG_SERVER_PATH = new PropertiesLoader("tfs-config.properties").getProperty("tfs.req.url");
    // 集团购机比较时间
    private static String compareTime = new PropertiesLoader("mall.properties").getProperty("compareTime");

    /**
     * 获取选中的购物车商品
     */
    public List<TfUserGoodsCar> getCheckedCars(List<TfUserGoodsCar> cars) {
        List<TfUserGoodsCar> newCars = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(cars)) {
            for (TfUserGoodsCar car : cars) {
                if ("Y".equals(car.getIsChecked())) {
                    newCars.add(car);
                }
            }
        }
        return newCars;
    }

    /**
     * 查询红包
     */
    @RequestMapping("/queryRedBagPrice")
    @ResponseBody
    public Double queryRedBagPrice(HttpServletRequest request, Long goodsId) {
        try {
            Long actNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
            ActivityCutPrice price = activityTargetNumberService.queryActivityCutByGoodsIdAndActNumber(goodsId + "", actNumber + "");
            if (price != null && "1".equals(price.getStatus())) {
                return price.getDiscount();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }


    /**
     * 查询自营厅
     *
     * @param org
     * @return
     */
    @RequestMapping("/queryOrgBusinessHallList")
    @ResponseBody
    public List<TdOrgBusinessHall> queryOrgBusinessHallList(TdEcOrgInfo org,HttpServletRequest request) {
        List<TdOrgBusinessHall> orgBusinessHallList = Lists.newArrayList();
        String streamNo= LogUtils.createStreamNo();
        try {
            writerFlowLogEnterMenthod(streamNo,"","",this.getClass().getName(),
                    "queryOrgBusinessHallList",request.getParameterMap(),"查询自营厅",request);
            orgBusinessHallList = hallService.queryOrgBusinessHallList(org);
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                    "queryOrgBusinessHallList",request.getParameterMap(),objectToMap(orgBusinessHallList),"查询自营厅");
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"queryOrgBusinessHallList",
                    null,processThrowableMessage(e));
            e.printStackTrace();
        }

        return orgBusinessHallList;
    }

    /**
     * 跳转号卡订单确认页面
     * 资费名称/所选号码/店铺信息/归属地址/支付金额（后台判断）
     */
    @VerifyCSRFToken
    @RequestMapping("/linkToConfirmOrderSim")
    public String linkToConfirmOrderSim(HttpServletRequest request, UserGoodsCarModel carModel, Model model) throws Exception {
        String streamNo= LogUtils.createStreamNo();

        try {
            //== 用户信息
            Session session = UserUtils.getSession();
            MemberVo member = UserUtils.getLoginUser(request);
            MemberLogin memberLogin = member.getMemberLogin();
            Long memberId = memberLogin.getMemberId();
            writerFlowLogEnterMenthod(streamNo,"",memberLogin.getMemberLogingName(),this.getClass().getName(),
                    "linkToConfirmOrderSim",request.getParameterMap(),"跳转号卡订单确认页面",request);
            //== 购物车信息处理
            // 当从单品页过来时，carList不为空， 当从订单页面选择如： “支付方式”时， carList为空。
            List<TfUserGoodsCar> carList = carModel.getUserGoodsCarList();
            // 容器内的跳转时， 转入的对象
            if (carList == null && request.getAttribute("carModel") != null) {
                carModel = (UserGoodsCarModel) request.getAttribute("carModel");
                if (carModel != null) {
                    carList = carModel.getUserGoodsCarList();
                }
            }

            //== 分情况处理业务逻辑，里面具体逻辑有待整理
            if (carList == null)
                carListNullDeal(request, carModel, model, session, memberLogin);
            else {
                carListNotNullDeal(request, carModel, model, false, session, memberLogin, memberId, carList, null);
            }
            // 服务协议
            String serviceContract = commonPropagandaService.getCommonPropagandaByCode(CommonPropaganda.SIM_ORDER_PRO, CommonParams.CHANNEL_CODE).getCommonPropagandaContent();
            model.addAttribute("serviceContract", serviceContract);
            writerFlowLogExitMenthod(streamNo,"",memberLogin.getMemberLogingName(),getClass().getName(),"linkToConfirmOrderSim",objectToMap(carModel),"跳转号卡订单确认页面");
            //购买须知
            //String noticeContract = commonPropagandaService.getCommonPropagandaByCode("PRE_PURCHASE_NOTICE", CommonParams.CHANNEL_CODE).getCommonPropagandaContent();
            //model.addAttribute("noticeContract", noticeContract);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转到确认订单页面失败，异常[{}]", e);
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"linkToConfirmOrderSim",
                    objectToMap(carModel),"跳转到确认订单页面失败:"+processThrowableMessage(e));
            throw new Exception("跳转到确认订单页面失败: " + e.getMessage());
        }
        return "web/goods/sim/confirmOrderSim";
    }


    /**
     * 跳转到确认订单页面
     * 将购物车信息放入session之前,严格控制购买数量不能为负数,goodsId和goodsSkuId匹配
     */
    @RequestMapping("/linkToConfirmOrder")
    public String linkToConfirmOrder(HttpServletRequest request, UserGoodsCarModel carModel, Model model) throws Exception {
        boolean cutBool = false; //砍价的标识
        String streamNo=createStreamNo();
        try {
            //== 用户信息
            Session session = UserUtils.getSession();
            MemberVo member = UserUtils.getLoginUser(request);
            MemberLogin memberLogin = member.getMemberLogin();
            Long memberId = memberLogin.getMemberId();
            writerFlowLogEnterMenthod(streamNo,"",memberLogin.getMemberLogingName(),this.getClass().getName(),"linkToConfirmOrder",
                    request.getParameterMap(),"跳转到确认订单页面",request);
            //== 购物车信息处理
            // 当从单品页过来时，carList不为空， 当从订单页面选择如： “支付方式”时， carList为空。
            List<TfUserGoodsCar> carList = carModel.getUserGoodsCarList();
            // 容器内的跳转时， 转入的对象
            if (carList == null && request.getAttribute("carModel") != null) {
                carModel = (UserGoodsCarModel) request.getAttribute("carModel");
                if (carModel != null) {
                    carList = carModel.getUserGoodsCarList();
                }
            }

            //== 营销资格判断
            Long marketId = carModel.getMarketId();
            if (marketId != null) {
                Boolean hasQualiFlag = (Boolean) session.getAttribute("hasQualiFlag");
                if (!Boolean.TRUE.equals(hasQualiFlag)) {
                    throw new Exception("您没有获取秒杀资格， 请重新抡购");
                }
            }

            //== 分情况处理业务逻辑，里面具体逻辑有待整理 😢 😜
            if (carList == null)
                carListNullDeal(request, carModel, model, session, memberLogin);
            else {
                carListNotNullDeal(request, carModel, model, cutBool, session, memberLogin, memberId, carList, marketId);
            }
            writerFlowLogExitMenthod(streamNo,"",memberLogin.getMemberLogingName(),this.getClass().getName(),"linkToConfirmOrder",
                    objectToMap(carModel),"跳转到确认订单页面");
        } catch (Exception e) {
            logger.error("跳转到确认订单页面失败，异常[{}]", e);
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"linkToConfirmOrderSim",
                    objectToMap(carModel),"跳转到确认订单页面失败:"+processThrowableMessage(e));
            throw new Exception("跳转到确认订单页面失败: " + e.getMessage());
        }
        return "web/goods/buy/confirmOrder";
    }

    /**
     * @param request
     * @param carModel
     * @param model
     * @param cutBool     砍价标识
     * @param session
     * @param memberLogin
     * @param memberId
     * @param carList
     * @param marketId    如果参加活动的活动Id
     * @throws Exception
     * @desc 分解：carList不为null处理逻辑
     */
    private void carListNotNullDeal(HttpServletRequest request, UserGoodsCarModel carModel, Model model, boolean cutBool, Session session, MemberLogin memberLogin, Long memberId, List<TfUserGoodsCar> carList, Long marketId) throws Exception {
        //获取选中的购物车商品
        List<TfUserGoodsCar> cars = goodsService.getCheckedCars(carList);
        carModel.setUserGoodsCarList(cars);
        // 处理理财商品
        processFinancialPackageOrder(cars, memberLogin);
        //购买权限确认
        if (!canBuy(request, memberLogin, carModel, null, 1)) {
            throw new Exception("购买数量超过限制或没有购买权限!");
        }
        if(existMinusGoodsNum(carModel)){
            throw new Exception("购买数量不能为负数!");
        }
        for(TfUserGoodsCar car : cars){
            if(!goodsService.checkGoodsIdHasSku(car)){
                throw new RuntimeException("该产品下无此产品规格,请确认后再试!");
            }
        }
        //获取用户默认收货地址
        if (carModel.getMemberAddress() == null) {
            MemberAddress memberAddress = memberAddressService.getDefAddr(memberId);
            processAddress(memberAddress);
            carModel.setMemberAddress(memberAddress);
        }
        //== 确定购买商品的店铺和商品信息
        List<Long> skuIds = Lists.newArrayList();
        long goodsNumCount = 0; //商品数量
        Set<Shop> shopSet = Sets.newHashSet();//获取店铺信息
        for (TfUserGoodsCar car : cars) {
            Shop shop = new Shop();
            shop.setShopId(car.getShopId());
            shop.setShopName(car.getShopName());
            shopSet.add(shop);
            skuIds.add(car.getGoodsSkuId());
            goodsNumCount += car.getGoodsBuyNum();
        }

        Set<Shop> newShopSet = Sets.newHashSet();//将shopSet遍历结果保存到newShopSet
        long paymentAmount = 0; //支付金额
        long rootCategoryId = 0; //商品一级类目
        List<Long> shopIdList = Lists.newArrayList(); //店铺列表信息
        int index = 0;
        boolean is10085 = false;
        for (Shop shopMap : shopSet) {
            if (shopMap.getShopId().equals(100000002099L)) {
                is10085 = true;
            }

            shopIdList.add(shopMap.getShopId());
            long goodsSalePriceTotal = 0;
            //分店铺统计商品购买数量、购买价格
            for (TfUserGoodsCar car : cars) {
                TfCategory category = new TfCategory();
                category.setCategoryId(car.getCategoryId());
                category = goodsQueryService.queryRootCategory(category);
                rootCategoryId = category.getCategoryId();

                //查询商品SKU信息
                if (rootCategoryId == 10000001 || rootCategoryId == 5) {
                    if (rootCategoryId == 10000001) {
                        index++;
                    }
                    car.setMarketId(marketId);
                    GoodsWater goodsWater = goodsService.queryGoodsSkuInfo(car);
                    car.setGoodsName(goodsWater.getGoodsName());
                    car.setGoodsSalePrice(goodsWater.getGoodsSalePrice());
                    /*针对三高客户购买指定终端降价的活动*/
                    boolean isAfterCurrentDate = goodsService.isAfterCurrentDate(compareTime);
                    boolean isCustomizeOrVotelTerminal = goodsService.isCustomizeOrVotelTerminal(car.getGoodsId());
                    boolean is3HighUser = goodsService.is3HighUser(memberLogin);
                    //是移动定制版或支持VOTEL功能的终端 && 是三高客户 && 当前日期小于2016-09-31
                    if (isCustomizeOrVotelTerminal && is3HighUser && isAfterCurrentDate) {
                        Long terminalSalePrice = goodsService.getTerminalSalePrice(goodsWater.getGoodsSalePrice());
                        car.setGoodsSalePrice(terminalSalePrice);
                        goodsSalePriceTotal += terminalSalePrice * car.getGoodsBuyNum();
                    } else {
                        if (marketId != null) {
                            car.setGoodsSalePrice(goodsWater.getGoodsSalePrice());
                            goodsSalePriceTotal += 1 * car.getGoodsBuyNum();
                        } else {
                            car.setGoodsSalePrice(goodsWater.getGoodsSalePrice());
                            goodsSalePriceTotal += goodsWater.getGoodsSalePrice() * car.getGoodsBuyNum();
                        }
                    }
                }
                //商品价格*商品数量
                if (rootCategoryId == 2) {
                    goodsSalePriceTotal += car.getGoodsSalePrice() * car.getGoodsBuyNum() * 100;
                }
                //对砍价活动的商品进行合计减价
                if (!cutBool) {
                    //查询是否存在并减扣
                    Long actNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
                    ActivityCutPrice price = activityTargetNumberService.queryActivityCutByGoodsIdAndActNumber(car.getGoodsId() + "", actNumber + "");
                    //锁定红包
                    if (price != null) {
                        paymentAmount = paymentAmount - ((Double) price.getDiscount()).longValue() * 100;
                        cutBool = true;
                    }
                }
            }
            paymentAmount += goodsSalePriceTotal;
            newShopSet.add(shopMap);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date discountDate = sdf.parse(discountTime);
        Date currentDate = new Date();
        if (marketId != null) {
            String infoStr = (String) JedisClusterUtils.get("ACTIVITY_DATA_" + marketId);
            VisualActivityInfo info = JSON.parseObject(infoStr, VisualActivityInfo.class);
            paymentAmount = info.getMarketPrice();
        } else if (index > 0 && currentDate.before(discountDate) && !is10085) {
            paymentAmount = paymentAmount - 1000;
        }

        //== 合约机判断
        List<ActivityGoodsPara> tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");
        if (null != tempList && tempList.size() > 0) {
            TfOrderDetailContract contract = carModel.getOrderDetailContract();
            //明星机、99购机资格校验
            for (ActivityGoodsPara activityGoodsPara : tempList) {
                if ("99812787".equals(contract.getContractId())) {
                    paymentAmount = paymentAmount - Long.parseLong(activityGoodsPara.getDisMount() == null ? "0" : activityGoodsPara.getDisMount()) * 100;
                }
            }
        }

        //支付方式放carModel
        TfPayMode payMode = new TfPayMode();
        if (rootCategoryId == 2) {
            payMode.setPayModeId(3);
            payMode.setPayModeName("到厅支付");
        } else {
            payMode.setPayModeId(2);
            payMode.setPayModeName("在线支付");
        }
        carModel.setPayMode(payMode);

        carModel.setPaymentAmount(paymentAmount);
        carModel.setRootCategoryId(rootCategoryId);
        carModel.setIsInvoicing(0);
        carModel.setShopIdList(shopIdList);
        carModel.setPayMode(payMode);
        session.setAttribute("shopSet", newShopSet);
        session.setAttribute("carModel", carModel);
        session.setAttribute("marketId", marketId);
        model.addAttribute("skuIds", StringUtils.join(skuIds, ";"));
        model.addAttribute("goodsNumCount", goodsNumCount);
        if (carModel.getOrderDetailSim() != null) {
            // 查询区县、自营厅
            TdEcOrgInfo org = new TdEcOrgInfo();
            String cityCode = carModel.getOrderDetailSim().getCityCode();
            org.setEparchCode(cityCode);
            List<TdEcOrgInfo> orgList = hallService.queryOrgList(org);
            List<TdOrgBusinessHall> orgBusinessHallList = hallService.queryOrgBusinessHallList(org);
            model.addAttribute("cityCode", cityCode);
            model.addAttribute("orgList", orgList);
            model.addAttribute("orgBusinessHallList", orgBusinessHallList);
        }
    }

    /**
     * 分解：经过上面的carList处理后，carlist如果为null的处理逻辑
     * carModel存入session前,保证购买数量不能为负数,产品规格没有被修改
     */
    private void carListNullDeal(HttpServletRequest request, UserGoodsCarModel carModel, Model model, Session session, MemberLogin memberLogin) throws Exception {
        UserGoodsCarModel sessionCarModel = (UserGoodsCarModel) session.getAttribute("carModel");
        //下单之前进行风控判断
        if (!canBuy(request, memberLogin, sessionCarModel, null, 1)) {
            throw new Exception("购买数量超过限制或没有购买权限!");
        }
        if(existMinusGoodsNum(sessionCarModel)){
            throw new Exception("购买数量不能为负数!");
        }
        List<TfUserGoodsCar> carLists = sessionCarModel.getUserGoodsCarList();
        for(TfUserGoodsCar car : carLists){
            if(!goodsService.checkGoodsIdHasSku(car)){
                throw new RuntimeException("该产品下无此产品规格,请确认后再试!");
            }
        }
        //处理理财商品
        processFinancialPackageOrder(sessionCarModel.getUserGoodsCarList(), memberLogin);

        MemberAddress memberAddress = carModel.getMemberAddress();
        Integer isInvoicing = carModel.getIsInvoicing();
        TfOrderRecommendContact recommendContact = carModel.getRecommendContact();
        TfDeliveryMode deliveryMode = carModel.getDeliveryMode();
        TfPayMode payMode = carModel.getPayMode();
        TfOrderReceiptTime orderReceiptTime = carModel.getReceiptTime();
        TfOrderDetailContract detailContract = carModel.getOrderDetailContract();
        TfOrderDetailSim detailSim = carModel.getOrderDetailSim();
        List<CouponInfo> couponInfoList = carModel.getCouponInfoList();

        if (memberAddress != null) {
            sessionCarModel.setMemberAddress(memberAddress);
        }
        if (isInvoicing != null) {
            sessionCarModel.setIsInvoicing(isInvoicing);
            sessionCarModel.setInvoicingTitle(carModel.getInvoicingTitle());
        } else {
            sessionCarModel.setIsInvoicing(0);
            sessionCarModel.setInvoicingTitle(null);
        }
        if (recommendContact != null) {
            sessionCarModel.setRecommendContact(recommendContact);
        }
        if (deliveryMode != null) {
            sessionCarModel.setDeliveryMode(deliveryMode);
            if (deliveryMode.getDeliveryModeId() != 2) {
                payMode = new TfPayMode();
                payMode.setPayModeId(2);
                payMode.setPayModeName("在线支付");
            }
        }

        if (payMode != null) {
            sessionCarModel.setPayMode(payMode);
        }
        if (orderReceiptTime != null) {
            sessionCarModel.setReceiptTime(orderReceiptTime);
        }
        if (detailContract != null) {
            sessionCarModel.setOrderDetailContract(detailContract);
        }
        if (detailSim != null) {
            sessionCarModel.setOrderDetailSim(detailSim);
        }
        if (!Collections3.isEmpty(couponInfoList)) {
            sessionCarModel.setCouponInfoList(couponInfoList);
        }

        //前台合约类型已经写25啦，此处需要根据用户选择的合约ID更新合约类型
        //针对99合约，明星机
        List<ActivityGoodsPara> tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");
        if (null != tempList && tempList.size() > 0) {
            String sureNo = "";
            //补充合约担保号码
            if (null != session.getAttribute("sureNo_99Buy") && !session.getAttribute("sureNo_99Buy").equals(""))
                sureNo = session.getAttribute("sureNo_99Buy").toString();
            //合约担保号码
            logger.info("合约担保号码" + sureNo);
            //明星机、99购机资格校验
            for (ActivityGoodsPara activityGoodsPara : tempList) {
                if (activityGoodsPara.getProCode().equals(detailContract.getContractId())) {
                    detailContract.setProductTypeCode(activityGoodsPara.getCrmCode());
                    if ("99812787".equals(detailContract.getContractId())) {
                        if (null != sureNo && !"".equals(sureNo)) detailContract.setSureno(sureNo);
                    }
                }
            }
            sessionCarModel.setOrderDetailContract(detailContract);
            carModel.setOrderDetailContract(detailContract);
        }

        if (sessionCarModel.getOrderDetailSim() != null) {
            //查询区县、自营厅
            TdEcOrgInfo org = new TdEcOrgInfo();
            String cityCode = sessionCarModel.getOrderDetailSim().getCityCode();
            org.setEparchCode(cityCode);
            List<TdEcOrgInfo> orgList = hallService.queryOrgList(org);
            List<TdOrgBusinessHall> orgBusinessHallList = hallService.queryOrgBusinessHallList(org);
            model.addAttribute("cityCode", cityCode);
            model.addAttribute("orgList", orgList);
            model.addAttribute("orgBusinessHallList", orgBusinessHallList);
        }
        session.setAttribute("carModel", sessionCarModel);
    }


    private Boolean existMinusGoodsNum(UserGoodsCarModel userGoodsCarModel){
        for(TfUserGoodsCar tfUserGoodsCar :userGoodsCarModel.getUserGoodsCarList()){
            if(tfUserGoodsCar.getGoodsBuyNum() <= 0 ){
                return true;
            }
        }
        return false;
    }


    public String processFinancialPackageOrder(TfGoodsInfo tfGoodsInfo, MemberLogin memberLogin) throws Exception {
        String isFinancialPackage = "N";
        if ("Y".equals(tfGoodsInfo.getIsFinancialPackage())) {
            isFinancialPackage = "Y";
        }

        if (isFinancialPackage == "Y") {
            if (memberLogin.getMemberTypeId() != 2) { //手机登录
                throw new RuntimeException("只能使用湖南移动手机号码登陆才能购买");
            }
        }
        return isFinancialPackage;
    }

    public String processFinancialPackageOrder(List<TfUserGoodsCar> carList, MemberLogin memberLogin) throws Exception {
        if (carList == null || carList.isEmpty()) {
            throw new RuntimeException("商品信息不能为空");
        }
        String isFinancialPackage = "N";
        for (TfUserGoodsCar car : carList) {
            Long goodsId = car.getGoodsId();
            if (goodsId != null) {
                TfGoodsInfo tfGoodsInfo = goodsStaticService.selectGoodsInfosforStatic(goodsId);
                if ("Y".equals(tfGoodsInfo.getIsFinancialPackage())) {
                    isFinancialPackage = "Y";
                    break;
                }
            }
        }

        // 如果为理财商品时， 验证订单商品条数以及数量
        if (isFinancialPackage == "Y") {
            if (carList.size() != 1) {
                throw new RuntimeException("理财商品不能加入购物车");
            }

            TfUserGoodsCar carInfo = carList.get(0);
            if (carInfo.getGoodsBuyNum() != 1) {
                throw new RuntimeException("理财商品一次只能购买一件");
            }

            if (memberLogin.getMemberTypeId() != 2) { //手机登录
                throw new RuntimeException("只能使用湖南移动手机号码登陆才能购买");
            }

            carInfo.setIsFinancialPackage("Y");
        }
        return isFinancialPackage;
    }


    /**
     * 跳转到选择收货地址页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/linkToChooseReceiptAddress")
    public String linkToChooseReceiptAddress(HttpServletRequest request, String memberAddressId, Model model) {
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        List<MemberAddress> memberAddressesList= memberAddressService.getAddrs(memberId);
        for(MemberAddress memberAddress:memberAddressesList){
            processAddress(memberAddress);
        }
        model.addAttribute("memberAddressList",memberAddressesList);
        model.addAttribute("memberAddressId", memberAddressId);
        return "web/goods/buy/chooseReceiptAddress";
    }

    private void processAddress(MemberAddress memberAddress) {
        if(null!=memberAddress) {
            if (memberAddress.getMemberRecipientPhone().length() == 11) {
                memberAddress.setMemberRecipientPhone(memberAddress.getMemberRecipientPhone().replace(memberAddress.getMemberRecipientPhone().substring(3, 7), "****"));
            }
            if (null != memberAddress.getMemberRecipientAddress() && !"".equals(memberAddress.getMemberRecipientAddress())) {
                int lenth = memberAddress.getMemberRecipientAddress().length();
                String addString = memberAddress.getMemberRecipientAddress().replace(memberAddress.getMemberRecipientAddress().substring(1, lenth - 1), "****");
                memberAddress.setMemberRecipientAddress(addString);
            }

            if (null != memberAddress.getMemberRecipientName() && !"".equals(memberAddress.getMemberRecipientName())) {
                String name = memberAddress.getMemberRecipientName().replace(memberAddress.getMemberRecipientName().substring(1), "****");
                memberAddress.setMemberRecipientName(name);
            }
        }
    }

    /**
     * 跳转到查询套餐信息页面
     *
     * @return
     */
    @RequestMapping("/linkToContractInfo")
    public String linkToContractInfo() {
        return "web/goods/buy/contractInfo";
    }


    //== 配送方式页面

    /**
     * 跳转到选择配送方式页面
     */
    @RequestMapping("/linkToChooseDeliveryMode")
    public String linkToChooseDeliveryMode(Model model, Long goodsId) {
        try {
            //商品是否支持货到付款、到厅自取处理
            Session session = UserUtils.getSession();
            UserGoodsCarModel carModel = (UserGoodsCarModel) session.getAttribute("carModel");
            List<Long> goodsIds = new ArrayList<>();
            List<TfUserGoodsCar> goodsCars = carModel.getUserGoodsCarList();
            if (CollectionUtils.isNotEmpty(goodsCars)) {
                for (TfUserGoodsCar goodsCar : goodsCars) {
                    goodsIds.add(goodsCar.getGoodsId());
                }
            } else if (goodsId != null) {
                goodsIds.add(goodsId);
            }
            Map param = new HashMap();
            param.put("goodsIds", goodsIds);
            List<TfGoodsInfo> goodsCashBusihall = goodsStaticService.queryGoodsCashBusihall(param);
            String cashOndelivery = "";//是否支持货到付款(Y：是，N：否)
            String busiHallOnTake = "";//是否支持到厅取(Y：是，N：否)
            for (TfGoodsInfo goodsInfo : goodsCashBusihall) {
                cashOndelivery = goodsInfo.getIsCashOndelivery();
                busiHallOnTake = goodsInfo.getIsBusiHallOnTake();
            }
            model.addAttribute("cashOndelivery", cashOndelivery);
            model.addAttribute("busiHallOnTake", busiHallOnTake);
            List<TfDeliveryMode> deliveryModeList = new ArrayList<>();
            //fixme 暂时屏蔽号卡网选厅取
            //if (goodsCars.get(0).getCategoryId() != 2L) {
            deliveryModeList = orderQueryService.queryDeliveryModeList(new TfDeliveryMode());
            //} else {
            //    TfDeliveryMode deliveryMode = new TfDeliveryMode();
            //    deliveryMode.setDeliveryModeId(1);
            //    deliveryMode.setDeliveryModeName("物流配送");
            //    deliveryModeList.add(deliveryMode);
            //}
            model.addAttribute("deliveryModeList", deliveryModeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "web/goods/buy/chooseDeliveryMode";
    }

    /**
     * 跳转到选择支付方式页面
     */
    @RequestMapping("/linkToChoosePayMode")
    public String linkToChoosePayMode(Model model, Long goodsId) {
        List<TfPayMode> payModeList = null;
        //商品是否支持货到付款、到厅自取处理
        Session session = UserUtils.getSession();
        UserGoodsCarModel carModel = (UserGoodsCarModel) session.getAttribute("carModel");
        //== 号卡
        if (carModel.getRootCategoryId() == 2) {
            payModeList = Lists.newArrayList();
            TfPayMode tfPayMode = new TfPayMode();
            int deliveryId = carModel.getDeliveryMode().getDeliveryModeId().intValue();
            //物流配送
            if (OrderConstant.DELIVERY_EXPRESS == deliveryId) {
                tfPayMode.setPayModeId(OrderConstant.PAY_WAY_ONLINE);
                tfPayMode.setPayModeName("在线支付");
            }
            //到厅自提
            if (OrderConstant.DELIVERY_STORE_PICKUP == deliveryId) {
                tfPayMode.setPayModeId(OrderConstant.PAY_WAY_HALL);
                tfPayMode.setPayModeName("到厅支付");
                model.addAttribute("busiHallOnTake", "Y");
            }
            payModeList.add(tfPayMode);
        }
        //== 其它
        else {

            List<TfUserGoodsCar> goodsCars = carModel.getUserGoodsCarList();
            List<Long> goodsIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(goodsCars)) {
                for (TfUserGoodsCar goodsCar : goodsCars) {
                    goodsIds.add(goodsCar.getGoodsId());
                }
            } else if (goodsId != null) {
                goodsIds.add(goodsId);
            }

            Map param = new HashMap();
            param.put("goodsIds", goodsIds);
            List<TfGoodsInfo> goodsCashBusihall = goodsStaticService.queryGoodsCashBusihall(param);
            String cashOndelivery = "";//是否支持货到付款(Y：是，N：否)
            String busiHallOnTake = "";//是否支持到厅取(Y：是，N：否)
            for (TfGoodsInfo goodsInfo : goodsCashBusihall) {
                cashOndelivery = goodsInfo.getIsCashOndelivery();
                busiHallOnTake = goodsInfo.getIsBusiHallOnTake();
            }
            TfDeliveryMode tfDeliveryMode = carModel.getDeliveryMode();
            if (null == tfDeliveryMode || tfDeliveryMode.getDeliveryModeId() != 2) {
                busiHallOnTake = "N";
            }
            model.addAttribute("cashOndelivery", cashOndelivery);
            model.addAttribute("busiHallOnTake", busiHallOnTake);
            payModeList = orderQueryService.queryPayModeList(new TfPayMode());
        }
        model.addAttribute("payModeList", payModeList);
        return "web/goods/buy/choosePayMode";
    }

    /**
     * 跳转到选择发票页面
     *
     * @return
     */
    @RequestMapping("/linkToChooseInvoice")
    public String linkToChooseInvoice() {
        return "web/goods/buy/chooseInvoice";
    }

    /**
     * 跳转到选择收货时间段页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/linkToChooseReceiptTime")
    public String linkToChooseReceiptTime(Model model) {
        List<TfOrderReceiptTime> receiptTimeList = orderQueryService.queryReceiptTimeList(new TfOrderReceiptTime());
        model.addAttribute("receiptTimeList", receiptTimeList);
        return "web/goods/buy/chooseReceiptTime";
    }

    /**
     * 跳转到填写推荐人页面
     *
     * @return
     */
    @RequestMapping("/linkToRecommender")
    public String linkToRecommender() {
        return "web/goods/buy/inputRecommender";
    }

    /**
     * 跳转到填写入网信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/linkToNetworkInfo")
    public String linkToNetworkInfo(Model model) {
        model.addAttribute("imgServerPath", IMG_SERVER_PATH);
        return "web/goods/buy/inputNetworkInfo";
    }

    /**
     * 跳转到选择优惠券页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/linkToChooseCoupon")
    public String linkToChooseCoupon(HttpServletRequest request, Model model) {
        try {
            Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
            Session session = UserUtils.getSession();
            UserGoodsCarModel carModel = (UserGoodsCarModel) session.getAttribute("carModel");
            List<TfUserGoodsCar> userGoodsCarList = carModel.getUserGoodsCarList();
            List<Long> shopIdList = carModel.getShopIdList();

            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setMemberId(memberId);
            List<CouponInfo> couponInfoList = couponService.queryMyCouponInfoList(couponInfo);

            Iterator<CouponInfo> couponIt = couponInfoList.iterator();
            while (couponIt.hasNext()) {
                CouponInfo coupon = couponIt.next();

                if (!shopIdList.contains(coupon.getShopId())) {
                    couponIt.remove();
                }

                Long subTotal = 0L;
                for (TfUserGoodsCar car : userGoodsCarList) {
                    if (car.getShopId().longValue() == coupon.getShopId().longValue()) {
                        subTotal += car.getGoodsBuyNum() * car.getGoodsSalePrice();
                    }
                }
                if (subTotal < coupon.getCouponUseLowestValue()) {
                    couponIt.remove();
                }
            }

            model.addAttribute("couponInfoList", couponInfoList);
            model.addAttribute("shopIdList", shopIdList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "web/goods/buy/chooseCoupon";
    }

    /**
     * 校验库存
     *
     * @param cModel
     * @return
     */
    @RequestMapping("/checkStock")
    @ResponseBody
    public Map<String, Object> checkStock(UserGoodsCarModel cModel) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<TfUserGoodsCar> cars = cModel.getUserGoodsCarList();
        try {
            for (TfUserGoodsCar car : cars) {
                //查询商品SKU信息
                GoodsWater goodsWater = goodsService.queryGoodsSkuInfo(car);
                String goodsName = car.getGoodsName();
                Long goodsBuyNum = car.getGoodsBuyNum();
                if (goodsBuyNum <= 0) {
                    resultMap.put("resultCode", "fail");
                    resultMap.put("resultInfo", "商品【" + goodsName + "】库存为0，不能购买");
                    return resultMap;
                }

                //校验库存
                if (goodsWater.getStockNum() >= car.getGoodsBuyNum()) {
                    resultMap.put("resultCode", "fail");
                    resultMap.put("resultInfo", "商品【" + goodsName + "】的购买数量不能大于库存数量");
                    return resultMap;
                }
            }
            resultMap.put("resultCode", "success");
            resultMap.put("resultInfo", "商品库存正常");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 实名认证校验 v1.0
     *
     * @param cModel
     * @return
     */
    @RequestMapping("/realityVerify")
    @ResponseBody
    public Map<String, Object> realityVerify(UserGoodsCarModel cModel) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            goodsService.realityVerify(cModel.getOrderDetailSim());
            resultMap.put("resultCode", "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultMap.put("resultCode", "fail");
        }
        return resultMap;
    }

    /**
     * 身份证实名制校验 v2.0 + 一证五号校验
     */
    @RequestMapping("/realityVerifyV2")
    @ResponseBody
    public Map<String, Object> realityVerifyV2(UserGoodsCarModel cModel) throws Exception {
        Map<String, Object> resultMap =Maps.newHashMap();
        TfOrderDetailSim sim = cModel.getOrderDetailSim();
        sim.setPsptId(TriDes.getInstance().strDec(sim.getPsptId(), keyStr, null, null));
        if(sim.getRegType()==null || sim.getRegType().length() == 0)
            sim.setRegType("1");//一证五号校验必须使用
        try {
            if(isProEnv()) {
                simBusiService.realityVerifyV2(sim);//实名制校验
                simBusiService.oneIdFiveNoVerify(sim);//一证五号校验
            }
            resultMap.put("resultCode", "success");
        } catch (Exception e) {
            logger.error("校验失败"+sim.getPsptId()+sim.getRegName(),e);
            resultMap.put("resultDesc", e.getMessage().replace("_",""));
            resultMap.put("resultCode", "fail");
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 号卡订单次数校验
     * 校验身份证号码一个月内是否已被提交5次订单
     */
    @RequestMapping("/simOrderCountVerify")
    @ResponseBody
    public Map<String, Object> simOrderCountVerify(String psptId) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            int count = orderService.querySubmittedOrderCountByPsptId(psptId);
            if (count >= 5) {
                resultMap.put("resultCode", "fail");
            } else {
                resultMap.put("resultCode", "success");
            }
        } catch (Exception e) {
            logger.error("实名认证校验异常", e);
            resultMap.put("resultCode", "fail");
        }
        return resultMap;
    }


    /**
     * 提交10085订单
     */
    @RequestMapping("/submit10085Order")
    public String submit10085Order(HttpServletRequest request, UserGoodsCarModel cModel, Model model) throws Exception {
        String logGoodsName = "", logGoodsId = "", goodsPrice = "";
        String startTime = com.ai.ecs.common.utils.DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        Session session = UserUtils.getSession();
        UserGoodsCarModel sessionCarModel = (UserGoodsCarModel) session.getAttribute("carModel");
        List<TfUserGoodsCar> cars = sessionCarModel.getUserGoodsCarList();
        boolean stockIsUpdated = false;
        boolean newOrderFlag = false;
        String streamNo= LogUtils.createStreamNo();
        try {

            TfOrderSub orderSub = new TfOrderSub();
            // 订单会员关联
            TfOrderUserRef orderUserRef = new TfOrderUserRef();
            MemberSsoVo memberSsoVo = UserUtils.getSsoLoginUser(request, null);
            MemberVo memberVo = UserUtils.getLoginUser(request);
            MemberLogin memberLogin = memberVo.getMemberLogin();
            MemberInfo memberInfo = memberVo.getMemberInfo();
            orderUserRef.setMemberId(memberLogin.getMemberId());
            orderUserRef.setMemberLogingName(memberLogin.getMemberLogingName());
            Integer memberTypeId = memberLogin.getMemberTypeId();
            if (memberTypeId == 2 && memberSsoVo != null) {
                orderUserRef.setMemberCreditClass(memberSsoVo.getCreditClass());
                orderUserRef.setEparchyCode(memberSsoVo.getEparchyCode());
                orderUserRef.setCounty(memberSsoVo.getDevelopCityCode());
            }
            writerFlowLogEnterMenthod(streamNo,"",memberLogin.getMemberLogingName(),this.getClass().getName(),
                    "submit10085Order",request.getParameterMap(),"提交10085订单",request);
            Long memberPhone = memberLogin.getMemberPhone();
            if (memberPhone != null) {
                orderUserRef.setContactPhone(String.valueOf(memberPhone));
            }
            if (memberPhone != null) {
                int flag = IdentityJudgUtils.judgement(String.valueOf(memberPhone));
                if (flag == IdentityJudgUtils.SPECIAL) {//特殊客户
                    orderUserRef.setIdentify("0");
                } else if (flag == IdentityJudgUtils.LEVEL) {//升级投诉客户
                    orderUserRef.setIdentify("1");
                } else if (flag == IdentityJudgUtils.RED) {//红名单客户
                    orderUserRef.setIdentify("3");
                }
            }
            orderSub.setOrderUserRef(orderUserRef);

            long orderSubAmount = 0;
            long goodsNumCount = 0;
            List<Long> skuIds = Lists.newArrayList();

            TfOrderDetailSim orderDetailSim = sessionCarModel.getOrderDetailSim();
            for (TfUserGoodsCar car : cars) {
                if(car.getGoodsBuyNum()<=0){
                    throw new Exception("商品数量异常！");
                }
                TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();

                //日志记录用 begin
                logGoodsId += car.getGoodsId() + "$" + car.getGoodsSkuId() + ",";
                logGoodsName += car.getGoodsName() + ",";
                //日志记录用 end

                TfCategory category = new TfCategory();
                category.setCategoryId(car.getCategoryId());
                category = goodsQueryService.queryRootCategory(category);
                long rooCategoryId = category.getCategoryId();
                orderSubDetail.setRootCateId(rooCategoryId);

                //设置订单明细信息
                orderSubDetail.setShopId(car.getShopId());
                orderSubDetail.setShopName(car.getShopName());
                orderSubDetail.setShopTypeId(car.getShopTypeId());
                orderSubDetail.setGoodsName(car.getGoodsName());
                orderSubDetail.setGoodsImgUrl(car.getGoodsSkuImgUrl());
                orderSubDetail.setGoodsRemark(car.getGoodsRemark());
                orderSubDetail.setGoodsSkuGiveIntegral(0L);

                if (orderDetailSim == null && rooCategoryId != 2) {
                    //查询商品SKU信息
                    GoodsWater goodsWater = goodsService.queryGoodsSkuInfo(car);
                    orderSubDetail.setThirdPartygoodsSkuId(goodsWater.getThirdPartygoodsSkuId());
                    //校验商品是否上架状态
                    if (4 != goodsWater.getState()) {
                        logger.error("{} 商品已下架！", car.getGoodsId());
                        throw new Exception(car.getGoodsName() + "-该商品已下架，请重新选购其它商品！");
                    }

                    //校验库存
                    Long goodsSkuId = goodsWater.getGoodsSkuId();
                    Long goodsBuyNum = car.getGoodsBuyNum();
                    Long marketId = goodsWater.getMarketId();
                    Long goodsId = goodsWater.getGoodsId();

                    if (goodsWater.getStockNum() < goodsBuyNum) {
                        throw new Exception(car.getGoodsName() + "-库存不足");
                    }

					/*扣减库存*/
                    Boolean result;
                    if (marketId != null) {//是否活动商品(redis)
                        result = goodsService.updateMarketStock(marketId, car.getGoodsSkuId(), goodsBuyNum);//更新活动商品库存(redis)
                    } else {
                        result = goodsSkuService.updateStock(goodsSkuId, goodsBuyNum.intValue());//更新商品库存

                        //更新商品销量
                        TfGoodsSkuValue skuValue = new TfGoodsSkuValue();
                        skuValue.setChnlCode(CommonParams.CHANNEL_CODE);
                        skuValue.setGoodsSaleNum(goodsBuyNum);
                        skuValue.setGoodsSkuId(goodsSkuId);
                        skuValue.setGoodsType(String.valueOf(car.getGoodsType()));
                        goodsSkuValueService.updateGoodsSkuValue(skuValue);
                        stockIsUpdated = true;
                    }

                    if (!result) {
                        throw new Exception(car.getGoodsName() + "-库存不足");
                    }

					/*设置商品单品页URL*/
                    Map<String, Object> goodsParams = new HashMap<>();
                    goodsParams.put("goodsId", car.getGoodsId());
                    List<TfShopGoodsChannelRef> urls = goodsManageService.queryShopGoodsChannelRefByCds(goodsParams);
                    for (TfShopGoodsChannelRef url : urls) {
                        String channelCode = url.getChannelCode();
                        if (OrderConstant.CHANNEL_SHOP.equals(channelCode)) {
                            orderSubDetail.setGoodsUrl(url.getGoodsUrl());
                        } else if (OrderConstant.CHANNEL_SHOPWAP.equals(channelCode)) {
                            orderSubDetail.setWapGoodsUrl(url.getGoodsUrl());
                        }
                    }

                    /*设置产品SKU ID*/
                    Map<String, Object> goodsParam = new HashMap<>();
                    goodsParam.put("goodsSkuId", car.getGoodsSkuId());
                    List<TfGoodsSku> skuList = goodsManageService.queryGoodsSkuInfoByCds(goodsParam);
                    TfGoodsSku goodsSku = skuList.get(0);
                    Long prodSkuId = goodsSku.getProdSkuId();
                    orderSubDetail.setProdSkuId(prodSkuId);
                    orderSubDetail.setProdBossCode(car.getProdBossCode());
                    orderSubDetail.setMarketId(marketId);
                    orderSubDetail.setGoodsId(car.getGoodsId());
                    orderSubDetail.setGoodsSkuId(goodsSkuId);
                    orderSubDetail.setHallAddress(car.getHallAddress());
                    orderSubDetail.setGoodsSkuNum(goodsBuyNum);
                    orderSubDetail.setGoodsFormat(car.getGoodsStandardAttr().replaceAll("=", "：").replaceAll("&amp;", "，").replaceAll("&quot;", "\""));
                    orderSubDetail.setGoodsSkuPrice(goodsWater.getGoodsSalePrice());

                    orderSubDetail.setGoodsSkuPrice(goodsWater.getGoodsSalePrice());
                    orderSubAmount += goodsWater.getGoodsSalePrice().longValue() * goodsBuyNum;

                    skuIds.add(car.getGoodsSkuId());
                    goodsNumCount += car.getGoodsBuyNum();
                    //日志记录用
                    goodsPrice = goodsWater.getGoodsSalePrice() + ",";

					/*设置供应商*/
                    Map<String, Object> paramMap = Maps.newHashMap();
                    paramMap.put("goodsSkuId", car.getGoodsSkuId());
                    List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoByCds(paramMap);
                    TfShopGoodsRef shopGoodsRef = goodsList.get(0).getShopGoodsRef();
                    if (shopGoodsRef != null) {
                        Long supplierShopId = shopGoodsRef.getSupplierShopId();
                        String supplierShopName = shopGoodsRef.getSupplierShopName();

                        if (supplierShopId != null) {
                            orderSubDetail.setSupplierShopId(supplierShopId);
                            orderSubDetail.setSupplierShopName(supplierShopName);
                        } else {
                            orderSubDetail.setSupplierShopId(car.getShopId());
                            orderSubDetail.setSupplierShopName(car.getShopName());
                        }
                    } else {
                        orderSubDetail.setSupplierShopId(car.getShopId());
                        orderSubDetail.setSupplierShopName(car.getShopName());
                    }

                }

				/*设置订单商品合约订购信息*/
                if (sessionCarModel.getOrderDetailContract() != null && car.getGoodsType() == 1) {
                    orderSubDetail.setOrderDetailContract(sessionCarModel.getOrderDetailContract());
                }

                orderSub.addOrderDetail(orderSubDetail);
            }

/*			 设置推荐工号
            TfOrderRecommendContact recommendContact = cModel.getRecommendContact();
			String referrer = (String)session.getAttribute("referrer");

			if (recommendContact == null) {
				recommendContact = new TfOrderRecommendContact();
			}

			// 设置推荐工号
			if (StringUtils.isNotBlank(referrer)) {
				recommendContact.setRecommendContactNo(referrer);
			}
             设置推荐工号结束 */
            //设置推荐人信息
            orderSub.setRecommendContact(cModel.getRecommendContact());

            //设置订单收件人信息
            MemberAddress ma = cModel.getMemberAddress();
            if (ma != null) {
                TfOrderRecipient receipt = new TfOrderRecipient();
                receipt.setOrderRecipientProvince(ma.getMemberRecipientProvince());
                receipt.setOrderRecipientCity(ma.getMemberRecipientCity());
                receipt.setOrderRecipientCounty(ma.getMemberRecipientCounty());
                receipt.setOrderRecipientAddress(ma.getMemberRecipientAddress());
                receipt.setOrderRecipientPhone(ma.getMemberRecipientPhone());
                receipt.setOrderRecipientName(ma.getMemberRecipientName());
                if (ma.getEmail() == null || ma.getEmail().length() == 0) {
                    receipt.setEmail("123@test.com");
                } else {
                    receipt.setEmail(ma.getEmail());
                }
                receipt.setZip(ma.getZip());
                orderSub.setRecipient(receipt);
            }

            //设置订单信息
            orderSub.setOrderSubRemark(cModel.getOrderSubRemark());//用户备注
            orderSub.setOrderChannelCode(CommonParams.CHANNEL_CODE);//渠道编码，WAP
            orderSub.setPayModeId(cModel.getPayMode().getPayModeId());//支付方式
            orderSub.setDeliveryModeId(cModel.getDeliveryMode().getDeliveryModeId());//配送方式
            orderSub.setHallAddress(cModel.getHallAddress());//自提网点
            orderSub.setIsInvoicing(cModel.getIsInvoicing());//是否开发票
            orderSub.setInvoicingTitle(cModel.getInvoicingTitle());//发票抬头
            if (cModel.getReceiptTime() != null) {
                orderSub.setReceiptTimeId(cModel.getReceiptTime().getReceiptTimeId());//收货时间段ID
            }
		/*	if(!canBuy(request,memberLogin,sessionCarModel,orderSub,2)){
				throw new Exception("购买数量超过限制或没有购买权限!");
			}*/
            //提交生成订单
            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
            newOrderFlag = true;
            List<String> orderNoList = Lists.newArrayList();
            for (TfOrderSub os : orderSubList) {
                orderNoList.add(os.getOrderSubNo());
            }
            String orderNos = StringUtils.join(orderNoList, ",");
            model.addAttribute("orderNos", orderNos);
            model.addAttribute("skuIds", skuIds);
            model.addAttribute("goodsNumCount", goodsNumCount);

            session.setAttribute("orderSubList", orderSubList);
            session.setAttribute("orderSubAmount", orderSubAmount);
            session.setAttribute("orderSubPayAmount", orderSubAmount);

            //删除购物车中已购买商品
            for (TfUserGoodsCar car : cars) {
                String key = "B2C" + memberLogin.getMemberId();
                if (JedisClusterUtils.exists(key)) {
                    userGoodsCarService.delete(key, car.getGoodsSkuId());
                }
            }
            //发送短信
            String sendPhone;
            if (memberPhone != null) {
                sendPhone = memberPhone + "";
            } else {
                sendPhone = ma.getMemberRecipientPhone();
            }
            try {
                order10085UpdateService.send85SMS(sendPhone,
                        DateFormatUtils.format(orderSubList.get(0).getOrderTime(), "yyyy-MM-dd HH:mm:ss"),
                        "E006", orderSubList.get(0).getDetailList().get(0).getGoodsName(),
                        orderSubList.get(0).getThirdPartyId(), 0);
            } catch (Exception e) {
                logger.error("短信失败：" + e.getMessage());
            }
            writerFlowLogExitMenthod(streamNo,orderNos,memberLogin.getMemberLogingName(),getClass().getName(),
                    "submit10085Order",objectToMap(orderSub),null,"提交10085订单"
                    );
            BusiLogUtils.writerLogging(request, "buygoods", logGoodsName, logGoodsId,
                    startTime, goodsPrice, "0", orderNos,
                    request.getRequestURI(), null, "1", sessionCarModel);

        } catch (Exception e) {
            if (stockIsUpdated && newOrderFlag == false) {
                //回库
                goodsService.returnGoodsStock(cars);
            }
            BusiLogUtils.writerLogging(request, "buygoods", logGoodsName, logGoodsId,
                    startTime, goodsPrice, "0", "",
                    request.getRequestURI(), e, "2", sessionCarModel);
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"submit10085Order",
                    null,"订单提交失败:"+processThrowableMessage(e));
            e.printStackTrace();
            logger.error("订单提交失败", e);
            throw new Exception("提交10085订单提交失败:" + e.getMessage());
        }

        return "web/goods/buy/choosePayPlatform";
    }

    //========== 号卡订单提交 start ==========//

    /**
     * 号卡订单 提交订单
     */
    @RequestMapping("/submitOrderSim")
    public String submitOrderSim(HttpServletRequest request, UserGoodsCarModel cModel, Model model) throws Exception {
        Session session = UserUtils.getSession();
        UserGoodsCarModel sessionCarModel = (UserGoodsCarModel) session.getAttribute("carModel");
        List<TfUserGoodsCar> cars = sessionCarModel.getUserGoodsCarList();
        //会员信息
        MemberVo memberVo = UserUtils.getLoginUser(request);//会员信息
        MemberLogin memberLogin = memberVo.getMemberLogin();
        MemberInfo memberInfo = memberVo.getMemberInfo();
        MemberSsoVo memberSsoVo = UserUtils.getSsoLoginUser(request, null);
        Long simOrderPayNumL = Long.parseLong(simOrderPayNum);
        String streamNo= LogUtils.createStreamNo();
        try {
            writerFlowLogEnterMenthod(streamNo,"",memberLogin.getMemberLogingName(),this.getClass().getName(),
                    "submitOrderSim",request.getParameterMap(),"号卡订单提交",request);
            // IP地址购买号码次数校验
            int ipPhoneNumOrders = simBusiService.getIpPhoneNumOrders(Servlets.getRemortIP(request));
            if (ipPhoneNumOrders > 30) {
                throw new Exception(ConstantsBase.MY_EXCEP + "同一IP地址1天内进行网上选号最多不超过30个");
            }

            //== 子订单信息
            TfOrderSub orderSub = new TfOrderSub();
            orderSub = confirmOrderUserRef(request, orderSub, memberVo, memberSsoVo);//设置用户信息
            orderSub = confirmOrderBaseInfo(cModel, orderSub);
            orderSub.setOrderSubPayAmount(simOrderPayNumL);//流程中需要用到，必须设置
            orderSub.setOrderSubAmount(simOrderPayNumL);//号卡订单SubAmount和SubPayAmount相同

            //== 处理TfOrderDetailSim
            TfOrderDetailSim orderDetailSim = sessionCarModel.getOrderDetailSim();
            //处理号码，car中就不应该出现这种形式：15273162604_1,15273162604_1
            long phone = Long.parseLong(orderDetailSim.getPhone().substring(0, 11));
            orderDetailSim.setPhone(orderDetailSim.getPhone().substring(0, 11));
            //处理baseSet和simProductId
            String cityCode = orderDetailSim.getCityCode();
            String cityCodeSuffix = cityCode.substring(2);
            String productId = orderDetailSim.getSimProductId().replace("**", cityCodeSuffix);
            String baseSet = orderDetailSim.getBaseSet().replace("**", cityCodeSuffix);
            orderDetailSim.setSimProductId(productId);
            orderDetailSim.setBaseSet(baseSet);
            orderDetailSim.setPreFee(simOrderPayNumL);//预存金额，不使用页面传过来的
            orderDetailSim.setCardFee(simOrderPayNumL);//卡金额
            //设置供应商
            orderDetailSim.setMemberId(memberLogin.getMemberId());
            if (memberInfo != null && StringUtils.isNotBlank(memberInfo.getMemberRealname())) {
                orderDetailSim.setCustName(memberInfo.getMemberRealname());
            } else {
                orderDetailSim.setCustName(memberLogin.getMemberLogingName());
            }
            orderDetailSim.setRegType("1");  // 设置证件类型
            sessionCarModel.setOrderDetailSim(orderDetailSim);

            //== 子订单详情信息
            TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
            cars.get(0).setGoodsSkuId(phone);//防止confirmOrderSubDetail报null
            cars.get(0).setGoodsId(phone);
            orderSubDetail = confirmOrderSubDetail(cars, orderSubDetail);
            if (orderSubDetail == null)
                throw new Exception(ConstantsBase.MY_EXCEP + "未查询到商品信息！");

            // 设置套餐编码，没必要模糊化，没事找事
            //String cityCode = orderDetailSim.getCityCode();
            //String productId = orderDetailSim.getProductId();
            //String baseSet = orderDetailSim.getBaseSet();
            //String cityCodeSuffix = cityCode.substring(2);
            //logger.info("baseSet:"+baseSet);
            //logger.info("productId:"+productId);
            //baseSet = baseSet.replace("**", cityCodeSuffix);
            //productId = productId.replace("**", cityCodeSuffix);
            //orderDetailSim.setProductId(productId);
            //orderDetailSim.setBaseSet(baseSet);

            //== 子订单详情
            orderSubDetail.setGoodsSkuPrice(simOrderPayNumL);
            orderSubDetail.setGoodsSkuNum(1L);//每个订单只能一个号码
            orderSubDetail.setProdSkuId(phone);
            //orderSubDetail.setProdBossCode(goodsInfo.getProdBossCode());//号卡没有这个属性
            //orderSubDetail.setMarketId(marketId);//无活动
            orderSubDetail.setGoodsSkuId(phone);//号卡订单：号码作为SKUID
            orderSubDetail.setGoodsId(phone);//号卡订单：号码作为GoodsId
            orderSubDetail.setGoodsFormat(orderDetailSim.getPhone());//号卡订单：号码作为商品规格
            orderSubDetail.setOrderDetailSim(orderDetailSim);
            orderSub.addOrderDetail(orderSubDetail);

            //== 校验3：购买资格校验（黑名单/一证五号）v1.0
            //ContractSaleCheckCondition condition = new ContractSaleCheckCondition();
            //condition.setCheckType(1);
            //condition.setIdType("00");
            //condition.setPsptId(orderDetailSim.getPsptId());
            //condition.setSerialNumber(orderDetailSim.getPhone());
            //condition.setCity(orderDetailSim.getCityCode());
            //Map resultMap = contractSaleCheckService.contractSaleCheck(condition);
            //String respCode = String.valueOf(resultMap.get("respCode"));
            //String respDesc = String.valueOf(resultMap.get("respDesc"));
            //if(!("0".equals(respCode) && "ok".equals(respDesc))){
            //   throw new Exception("[用户购买号码资格校验接口]调用失败");
            //}
            //String result = String.valueOf(resultMap.get("result"));
            //JSONObject jsonObject = JSONArray.parseArray(result).getJSONObject(0);
            //String xResultCode = jsonObject.getString("X_RESULTCODE");
            //String xResultInfo = jsonObject.getString("X_RESULTINFO");
            //String isBlackList = jsonObject.getString("ISBLACKLIST");
            //int allowNum = jsonObject.getIntValue("ALLOW_NUM");
            //if(!"0".equals(xResultCode)){
            //   throw new Exception(xResultInfo);
            //}
            //if("1".equals(isBlackList)){
            //   throw new Exception("此用户身份证为黑名单用户，不能购买号码");
            //}
            //if(allowNum == 0){
            //   throw new Exception("此用户身份证购买号码已购买5个号码，不能再继续购买");
            //}

            //== 身份证信息校验
            simBusiService.realityVerifyV2(orderDetailSim);
            logger.info("身份证信息验证成功！");

            //== 一证五号 v2.0 校验
            simBusiService.oneIdFiveNoVerify(orderDetailSim);
            logger.info("一证五号校验成功！");

            //== 号码选占
            simBusiService.selTempOccupyRes(orderDetailSim);
            logger.info("号码选占成功！");

            //== "提交订单"其它操作说明
            //库存：暂时都是移动自有商品接口调用获取号码信息，无需电渠侧库存校验/库存扣减/失败回库：无
            //商品合约订购信息：无
            //商品销量：依据sku来定义销量，号卡规则（一个号码是一个sku）每个号卡的销量都是为1，没必要存：无
            //营销活动信息：无
            //积分：无
            //删除购物车中已购买商品：无
            //物流信息：无

            // 提交生成订单
            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
            TfOrderSub orderSubResult = orderSubList.get(0);
            printKaInfo(JSONObject.toJSONString(orderSubResult,SerializerFeature.WriteNullStringAsEmpty));
            writerFlowLogExitMenthod(streamNo,"",memberLogin.getMemberLogingName(),getClass().getName(),
                    "submitOrderSim",objectToMap(orderSubList),"号卡订单提交");
            //== 生成订单后操作
            // 删除备选号码
            TfPhonenumSelect phonenumSelect = new TfPhonenumSelect();
            phonenumSelect.setMemberId(memberLogin.getMemberId());
            phonenumSelect.setPhoneNum(orderDetailSim.getPhone());
            phonenumSelectService.deletePhonenumSelect(phonenumSelect);
            // 根据用户IP地址记录选号次数
            simBusiService.setIpPhoneNumOrders(Servlets.getRemortIP(request));

            model.addAttribute("orderVo", orderSubResult);
            model.addAttribute("sucMsg", "您的在订单生成成功，感谢您的惠顾！");
        } catch (Exception e) {
            logger.error("订单提交失败", e);
            writerFlowLogThrowable(streamNo,"",memberLogin.getMemberLogingName(),getClass().getName(),
                    "submitOrderSim",null,"号卡订单提交失败"+processThrowableMessage(e));
            e.printStackTrace();
            splitException(e, "订单提交失败：" + e.getMessage());
        }
        //return "redirect:/myOrder/toMyAllOrderList";
        return "web/goods/sim/confirmOrderSimBak";
    }

    /**
     * 确认订单：设置常用订单详情信息
     * rooCategoryId/shop/goods/urls/supplierShop
     * TODO：写成服务
     */
    private TfOrderSubDetail confirmOrderSubDetail(List<TfUserGoodsCar> cars, TfOrderSubDetail orderSubDetail) {
        if (CollectionUtils.isEmpty(cars))
            return null;
        for (TfUserGoodsCar car : cars) {
            //日志记录用
            //logGoodsId += car.getGoodsId() + "$" + car.getGoodsSkuId() + ",";
            //logGoodsName += car.getGoodsName() + ",";
            //== 产品类别
            TfCategory category = new TfCategory();
            category.setCategoryId(car.getCategoryId());
            category = goodsQueryService.queryRootCategory(category);
            orderSubDetail.setRootCateId(category.getCategoryId());
            //== 店铺信息
            orderSubDetail.setShopId(car.getShopId());
            orderSubDetail.setShopName(car.getShopName());
            orderSubDetail.setShopTypeId(car.getShopTypeId());
            //== 商品信息
            orderSubDetail.setGoodsName(car.getGoodsName());
            orderSubDetail.setGoodsImgUrl(car.getGoodsSkuImgUrl());
            orderSubDetail.setGoodsRemark(car.getGoodsRemark());
            orderSubDetail.setGoodsSkuId(car.getGoodsSkuId());
            //== 设置商品单品页URL
            Map<String, Object> goodsParams = new HashMap<>();
            goodsParams.put("goodsId", car.getGoodsId());
            List<TfShopGoodsChannelRef> urls = goodsManageService.queryShopGoodsChannelRefByCds(goodsParams);
            for (TfShopGoodsChannelRef url : urls) {
                String channelCode = url.getChannelCode();
                if (OrderConstant.CHANNEL_SHOP.equals(channelCode)) {
                    orderSubDetail.setGoodsUrl(url.getGoodsUrl());
                } else if (OrderConstant.CHANNEL_SHOPWAP.equals(channelCode)) {
                    orderSubDetail.setWapGoodsUrl(url.getGoodsUrl());
                }
            }
            //== 设置供应商Id和Name：如果通过goodsSkuId查不到，则从car中取
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("goodsSkuId", car.getGoodsSkuId());
            List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoByCds(paramMap);
            if (CollectionUtils.isNotEmpty(goodsList)) {
                TfShopGoodsRef shopGoodsRef = goodsList.get(0).getShopGoodsRef();
                if (shopGoodsRef != null) {
                    Long supplierShopId = shopGoodsRef.getSupplierShopId();
                    String supplierShopName = shopGoodsRef.getSupplierShopName();
                    if (supplierShopId != null) {
                        orderSubDetail.setSupplierShopId(supplierShopId);
                        orderSubDetail.setSupplierShopName(supplierShopName);
                    } else {
                        orderSubDetail.setSupplierShopId(car.getShopId());
                        orderSubDetail.setSupplierShopName(car.getShopName());
                    }
                } else {
                    orderSubDetail.setSupplierShopId(car.getShopId());
                    orderSubDetail.setSupplierShopName(car.getShopName());
                }
            }
        }
        return orderSubDetail;
    }

    /**
     * 确认订单：关联用户
     * TODO 写成服务
     */
    private TfOrderSub confirmOrderUserRef(HttpServletRequest request, TfOrderSub orderSub, MemberVo memberVo, MemberSsoVo memberSsoVo) {
        MemberLogin memberLogin = memberVo.getMemberLogin();
        TfOrderUserRef orderUserRef = new TfOrderUserRef();
        orderUserRef.setMemberId(memberLogin.getMemberId());
        orderUserRef.setMemberLogingName(memberLogin.getMemberLogingName());
        Integer memberTypeId = memberLogin.getMemberTypeId();
        if (memberTypeId == 2 && memberSsoVo != null) {
            orderUserRef.setMemberCreditClass(memberSsoVo.getCreditClass());
            orderUserRef.setEparchyCode(memberSsoVo.getEparchyCode());
            orderUserRef.setCounty(memberSsoVo.getDevelopCityCode());
        }
        Long memberPhone = memberLogin.getMemberPhone();
        if (memberPhone != null) {
            orderUserRef.setContactPhone(String.valueOf(memberPhone));
        }
        if (memberPhone != null) {
            int flag = IdentityJudgUtils.judgement(String.valueOf(memberPhone));
            if (flag == IdentityJudgUtils.SPECIAL) {//特殊客户
                orderUserRef.setIdentify("0");
            } else if (flag == IdentityJudgUtils.LEVEL) {//升级投诉客户
                orderUserRef.setIdentify("1");
            } else if (flag == IdentityJudgUtils.RED) {//红名单客户
                orderUserRef.setIdentify("3");
            }
        }
        orderSub.setOrderUserRef(orderUserRef);
        return orderSub;
    }

    /**
     * 设置订单基本信息：用户地址信息
     */
    public static TfOrderSub confirmOrderBaseInfo(UserGoodsCarModel cModel, TfOrderSub orderSub) {
        //设置推荐人信息
        orderSub.setRecommendContact(cModel.getRecommendContact());
        //设置订单收件人信息
        MemberAddress ma = cModel.getMemberAddress();
        Integer deliveryModeId = cModel.getDeliveryMode().getDeliveryModeId();
        if (deliveryModeId == 1 && ma != null && StringUtils.isNotBlank(ma.getMemberRecipientName())) {
            TfOrderRecipient receipt = new TfOrderRecipient();
            receipt.setOrderRecipientProvince(ma.getMemberRecipientProvince());
            receipt.setOrderRecipientCity(ma.getMemberRecipientCity());
            receipt.setOrderRecipientCounty(ma.getMemberRecipientCounty());
            receipt.setOrderRecipientAddress(ma.getMemberRecipientAddress());
            //如果收货人电话为空，则取号卡订单详情表中的联系电话，用于网选厅取和h5匿名在线售卡
            String endContactPhone = ma.getMemberRecipientPhone()!=null?ma.getMemberRecipientPhone():
                    cModel.getOrderDetailSim().getContactPhone();
            receipt.setOrderRecipientPhone(endContactPhone);
            //如果没有收货人，则用身份证的名字
            String endRecipientName = ma.getMemberRecipientName()!=null?ma.getMemberRecipientName():
                    cModel.getOrderDetailSim().getRegName();
            receipt.setOrderRecipientName(endRecipientName);
            orderSub.setRecipient(receipt);
            if (cModel.getReceiptTime() != null) {
                orderSub.setReceiptTimeId(cModel.getReceiptTime().getReceiptTimeId());// 收货时间段ID
            }
        }
        //设置订单信息
        orderSub.setOrderSubRemark(cModel.getOrderSubRemark());//用户备注
        orderSub.setOrderChannelCode(CommonParams.CHANNEL_CODE);//渠道编码，WAP
        orderSub.setPayModeId(cModel.getPayMode().getPayModeId());//支付方式
        orderSub.setDeliveryModeId(cModel.getDeliveryMode().getDeliveryModeId());//配送方式
        orderSub.setHallAddress(cModel.getHallAddress());//自提网点
        orderSub.setIsInvoicing(cModel.getIsInvoicing());//是否开发票
        orderSub.setInvoicingTitle(cModel.getInvoicingTitle());//发票抬头
        return orderSub;
    }
    //========== 号卡订单提交 end ==========//

    /**
     * 提交订单
     *
     * @param cModel
     * @param model
     * @return
     */
    @RequestMapping("/submitOrder")
    public String submitOrder(HttpServletRequest request, UserGoodsCarModel cModel, Model model, Long marketId, Long goodsId) throws Exception {
        String streamNo=createStreamNo();
        String logGoodsName = "", logGoodsId = "", goodsPrice = "";
        String startTime = com.ai.ecs.common.utils.DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        Session session = UserUtils.getSession();
        UserGoodsCarModel sessionCarModel = (UserGoodsCarModel) session.getAttribute("carModel");
        List<TfUserGoodsCar> cars = sessionCarModel.getUserGoodsCarList();
        boolean stockIsUpdated = false;
        boolean newOrderFlag = false;
        boolean isCampOn = false;

        boolean cutBool = false;//砍价的标识
        ActivityCutPrice price = null;
        VisualActivityInfo marketInfo = null;
        long orderSubPayAmount = 0;
        //updatedCars 记录已经更新好的占用库存商品
        List<TfUserGoodsCar> updatedCars = new ArrayList<TfUserGoodsCar>();

        try {
            TfOrderSub orderSub = new TfOrderSub();
            // 订单会员关联
            TfOrderUserRef orderUserRef = new TfOrderUserRef();
            MemberSsoVo memberSsoVo = UserUtils.getSsoLoginUser(request, null);
            MemberVo memberVo = UserUtils.getLoginUser(request);
            writerFlowLogEnterMenthod(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),
                    "submitOrder",request.getParameterMap(),"订单提交",request);
			/*if (marketId != null) {
				if (memberVo != null) {
					// 判断秒杀前提条件
					//用户六个月内是否购买秒杀商品
					String activityCheckone = JedisClusterShopUtils.get("ACTIVITY_CHECK_ONE" + marketId+"_BY_"+memberVo.getMemberLogin().getMemberPhone());
					if(StringUtils.isNotBlank(activityCheckone) && "true".equals(activityCheckone)) {
						throw new Exception("您六个月内已购买过秒杀商品，只限一次");
					}
					//是否具备秒杀资格
					String activityCheck = JedisClusterShopUtils.hget("ACTIVITY_CHECK_" + marketId, memberVo.getMemberLogin().getMemberPhone() + "");
					if (StringUtils.isEmpty(activityCheck)) {
						logger.error("获取秒杀资格失败");
						throw new Exception("获取秒杀资格失败！");
					}
				} else {
					return "redirect:/login/toLogin";
				}
			}*/

            marketId = sessionCarModel.getMarketId();
            if (marketId != null) {
                Boolean hasQualiFlag = (Boolean) session.getAttribute("hasQualiFlag");
                if (!Boolean.TRUE.equals(hasQualiFlag)) {
                    writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            null,"您没有获取秒杀资格， 请重新抢购");
                    throw new Exception("您没有获取秒杀资格， 请重新抢购");
                }

                String key = "ACTIVITY_DATA_" + marketId;
                String marketInfoStr = JedisClusterUtils.get(key);
                marketInfo = JSONObject.parseObject(marketInfoStr, VisualActivityInfo.class);
                if (marketInfo == null || marketInfo.getActivityStock() <= 0) {
                    writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            objectToMap(marketInfo),"手慢啦~宝贝被抢光啦~");
                    throw new Exception("手慢啦~宝贝被抢光啦~");
                }
            }

            MemberLogin memberLogin = memberVo.getMemberLogin();
            MemberInfo memberInfo = memberVo.getMemberInfo();
            orderUserRef.setMemberId(memberLogin.getMemberId());
            orderUserRef.setMemberLogingName(memberLogin.getMemberLogingName());
            Integer memberTypeId = memberLogin.getMemberTypeId();
            if (memberTypeId == 2 && memberSsoVo != null) {
                orderUserRef.setMemberCreditClass(memberSsoVo.getCreditClass());
                orderUserRef.setEparchyCode(memberSsoVo.getEparchyCode());
                orderUserRef.setCounty(memberSsoVo.getDevelopCityCode());
            }
            Long memberPhone = memberLogin.getMemberPhone();
            if (memberPhone != null) {
                orderUserRef.setContactPhone(String.valueOf(memberPhone));
            }
            if (memberPhone != null) {
                int flag = IdentityJudgUtils.judgement(String.valueOf(memberPhone));
                if (flag == IdentityJudgUtils.SPECIAL) {//特殊客户
                    orderUserRef.setIdentify("0");
                } else if (flag == IdentityJudgUtils.LEVEL) {//升级投诉客户
                    orderUserRef.setIdentify("1");
                } else if (flag == IdentityJudgUtils.RED) {//红名单客户
                    orderUserRef.setIdentify("3");
                }
            }
            orderSub.setOrderUserRef(orderUserRef);

            long orderSubAmount = 0;
            long goodsNumCount = 0;
            List<Long> skuIds = Lists.newArrayList();

            TfOrderDetailSim orderDetailSim = sessionCarModel.getOrderDetailSim();
            if (CollectionUtils.isNotEmpty(cars)) {
                for (TfUserGoodsCar car : cars) {
                    TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
                    //日志记录用 begin
                    logGoodsId += car.getGoodsId() + "$" + car.getGoodsSkuId() + ",";
                    logGoodsName += car.getGoodsName() + ",";
                    //日志记录用 end

                    TfCategory category = new TfCategory();
                    category.setCategoryId(car.getCategoryId());
                    category = goodsQueryService.queryRootCategory(category);
                    long rooCategoryId = category.getCategoryId();
                    orderSubDetail.setRootCateId(rooCategoryId);

                    // 理财商品订购
                    if (rooCategoryId != 2) {
                        String isFinacialPackgeOrder = processFinancialPackageOrder(cars, memberLogin);
                        orderSub.setIsFinancialPackage(isFinacialPackgeOrder);
                    }


                    //设置订单明细信息
                    orderSubDetail.setShopId(car.getShopId());
                    orderSubDetail.setShopName(car.getShopName());
                    orderSubDetail.setShopTypeId(car.getShopTypeId());
                    orderSubDetail.setGoodsName(car.getGoodsName());
                    orderSubDetail.setGoodsImgUrl(car.getGoodsSkuImgUrl());
                    orderSubDetail.setGoodsRemark(car.getGoodsRemark());
                    orderSubDetail.setGoodsSkuGiveIntegral(0L);
                    orderSubDetail.setGoodsSkuId(car.getGoodsSkuId());

                    if (orderDetailSim == null && rooCategoryId != 2) {
                        //查询商品SKU信息
                        car.setMarketId(marketId);
                        GoodsWater goodsWater = goodsService.queryGoodsSkuInfo(car);
                        //校验商品是否上架状态
                        if (4 != goodsWater.getState()) {
                            logger.error("{} 商品已下架！", car.getGoodsId());
                            writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                                    objectToMap(car),"该商品已下架，请重新选购其它商品");
                            throw new Exception(car.getGoodsName() + "-该商品已下架，请重新选购其它商品！");
                        }

                        //校验库存
                        Long goodsSkuId = goodsWater.getGoodsSkuId();
                        Long goodsBuyNum = car.getGoodsBuyNum();
                        //活动限购，
                        if (null != marketId) {
                            //查询加上已下单的订单
                            Map param = new HashMap();
                            param.put("goodsSkuId", goodsSkuId);
                            param.put("marketId", marketId);
                            param.put("memberId", memberLogin.getMemberId());
                            Integer buyGoodsNum = orderQueryService.queryBuyGoodsCount(param);
                            logger.info("活动限购goodsBuyNum:{},buyGoodsNum:{},goodsWater.getGoodsMaxNum():{}", goodsBuyNum, buyGoodsNum, goodsWater.getGoodsMaxNum());
                            if (goodsBuyNum + buyGoodsNum > goodsWater.getGoodsMaxNum()) {
                                logger.error("[{}] 用户超出活动[{}]商品购买限制！", memberLogin.getMemberId(), marketId);
                                writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                                        objectToMap(car),"用户超出活动商品购买限制！"+marketId);
                                throw new Exception(car.getGoodsName() + "-超出活动商品购买限制！");
                            }
                        }
                        logger.error("goodsWater.getStockNum:{}", JSONObject.toJSONString(goodsWater));
                        logger.error("goodsBuyNum:{}", goodsBuyNum);
                        if (goodsWater.getStockNum() < goodsBuyNum) {
                            writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                                    objectToMap(car),"库存不足");
                            throw new Exception(car.getGoodsName() + "-库存不足");
                        }

						/*扣减库存*/
                        Boolean result;
						/*先占用库用 提交订单 如果购物车商品的店铺ID为1 占用实物库存 其他店铺的订单则无需占用库存的概念  */
                        if (marketId != null) {//是否活动商品(redis)
                            //	result = goodsService.updateMarketStock(marketId,goodsSkuId,goodsBuyNum);//更新活动商品库存(redis)
                            //如果是移动自营店， 则店铺Id为1 扣减虚拟库存  到时候活动下架 再统一减实物库存和减占用库存
                            //						if (car.getShopId()!=null&&car.getShopId()==1L){
                            //							result=goodsSkuService.updateOccupyStock(goodsSkuId,goodsBuyNum.intValue());//更新占用库存
                            //						}else{//非移动自营店  活动商品的 实物库存 统一在上下架和确认发货处理
                            result = goodsService.updateMarketStock(marketId, car.getGoodsSkuId(), goodsBuyNum);// 更新活动商品库存
                            //						}

                            stockIsUpdated = true;
                        } else {
                            //result = goodsSkuService.updateStock(goodsSkuId,goodsBuyNum.intValue());//更新商品库存
                            //如果是移动自营店， 则店铺Id为1 扣减虚拟库存
                            if (car.getShopId() != null && car.getShopId() == 1L) {
                                result = goodsSkuService.updateOccupyStock(goodsSkuId, goodsBuyNum.intValue());//更新占用库存
                            } else {//非移动自营店
                                result = goodsSkuService.updateStock(goodsSkuId, goodsBuyNum.intValue());// 更新商品库存
                            }

                            //更新商品销量
                            TfGoodsSkuValue skuValue = new TfGoodsSkuValue();
                            skuValue.setChnlCode(CommonParams.CHANNEL_CODE);
                            skuValue.setGoodsSaleNum(goodsBuyNum);
                            skuValue.setGoodsSkuId(goodsSkuId);
                            skuValue.setGoodsType(String.valueOf(car.getGoodsType()));
                            goodsSkuValueService.updateGoodsSkuValue(skuValue);
                            stockIsUpdated = true;
                        }
                        updatedCars.add(car);
                        /**记录库存变化日志**/
                        TfStockUpdateLog tfStockUpdateLog = new TfStockUpdateLog();
                        tfStockUpdateLog.setUpdateType("5");
                        tfStockUpdateLog.setOperRemark("手机wap，增加占用库存");
                        tfStockUpdateLog.setGoodsId(car.getGoodsId());
                        tfStockUpdateLog.setGoodsNum(car.getGoodsBuyNum());
                        tfStockUpdateLog.setOperUserName(memberLogin.getMemberLogingName());
                        tfStockUpdateLog.setOperTime(new Date());
                        tfStockUpdateLog.setGoodsSkuId(car.getGoodsSkuId());
                        // tfStockUpdateLog.setProdSkuId();
                        //保存库存更新的日志
                        stockInterfaceLogService.saveStockUpdateLog(tfStockUpdateLog);

                        if (!result) {
                            writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                                    objectToMap(car),car.getGoodsName()+"-库存不足");
                            throw new Exception(car.getGoodsName() + "-库存不足");
                        }

                        //对砍价活动的商品进行合计减价
                        if (!cutBool && !StringUtils.isBlank(memberPhone + "")) {
                            //查询是否存在并减扣
                            price = activityTargetNumberService.queryActivityCutByGoodsIdAndActNumber(car.getGoodsId() + "", memberPhone + "");
                        }

						/*设置商品单品页URL*/
                        Map<String, Object> goodsParams = new HashMap<>();
                        goodsParams.put("goodsId", car.getGoodsId());
                        List<TfShopGoodsChannelRef> urls = goodsManageService.queryShopGoodsChannelRefByCds(goodsParams);
                        for (TfShopGoodsChannelRef url : urls) {
                            String channelCode = url.getChannelCode();
                            if (OrderConstant.CHANNEL_SHOP.equals(channelCode)) {
                                orderSubDetail.setGoodsUrl(url.getGoodsUrl());
                            } else if (OrderConstant.CHANNEL_SHOPWAP.equals(channelCode)) {
                                orderSubDetail.setWapGoodsUrl(url.getGoodsUrl());
                            }
                        }

						/*设置产品SKU ID*/
                        Map<String, Object> goodsParam = new HashMap<>();
                        goodsParam.put("goodsSkuId", car.getGoodsSkuId());
                        List<TfGoodsSku> skuList = goodsManageService.queryGoodsSkuInfoByCds(goodsParam);
                        if (CollectionUtils.isNotEmpty(skuList)) {
                            TfGoodsSku goodsSku = skuList.get(0);
                            Long prodSkuId = goodsSku.getProdSkuId();
						/*设置购买商品赠送积分*/
                            TfGoodsSkuValueExt goodsSkuValueExt = goodsSku.getTfGoodsSkuValueExtList().get(0);
                            if (goodsSkuValueExt != null) {
                                if (car.getGoodsType() == 1) {
                                    orderSubDetail.setGoodsSkuGiveIntegral(goodsSkuValueExt.getPrestorGiveIntegral());
                                } else if (car.getGoodsType() == 2) {
                                    orderSubDetail.setGoodsSkuGiveIntegral(goodsSkuValueExt.getBareGiveIntegral());
                                }
                            }

                            orderSubDetail.setProdSkuId(prodSkuId);
                        }
                        orderSubDetail.setProdBossCode(car.getProdBossCode());
                        orderSubDetail.setMarketId(marketId);
                        orderSubDetail.setGoodsId(car.getGoodsId());
                        orderSubDetail.setGoodsSkuId(goodsSkuId);
                        orderSubDetail.setHallAddress(car.getHallAddress());
                        orderSubDetail.setGoodsSkuNum(goodsBuyNum);
                        //活动商品时可能没此值
                        if (null != car.getGoodsStandardAttr()) {
                            orderSubDetail.setGoodsFormat(car.getGoodsStandardAttr().replaceAll("=", "：").replaceAll("&amp;", "，").replaceAll("&quot;", "\""));
                        }
                        if (StringUtils.isEmpty(orderSubDetail.getGoodsFormat())) {
                            orderSubDetail.setGoodsFormat("未知");
                        }
                        orderSubDetail.setGoodsSkuPrice(goodsWater.getGoodsSalePrice());
						/*针对三高客户购买指定终端降价的活动*/
                        boolean isAfterCurrentDate = goodsService.isAfterCurrentDate(compareTime);
                        boolean isCustomizeOrVotelTerminal = goodsService.isCustomizeOrVotelTerminal(car.getGoodsId());
                        boolean is3HighUser = goodsService.is3HighUser(memberLogin);

                        //是移动定制版或支持VOTEL功能的终端 && 是三高客户 && 当前日期小于2016-09-31
                        if (isCustomizeOrVotelTerminal && is3HighUser && isAfterCurrentDate) {
                            //查看类型，只有裸机才可以绑定6个月
                            Long terminalSalePrice = goodsService.getTerminalSalePrice(goodsWater.getGoodsSalePrice());
                            if (car.getGoodsType() == 2) {
                                //进行判断是否可以购买，
                                String phoneNum = "";
                                if (memberLogin.getMemberTypeId() == 2) {//手机登录
                                    phoneNum = memberLogin.getMemberLogingName();
                                } else {
                                    throw new Exception("非手机登录用户不可以购买集团专享商品！");
                                }
                                //查看6个月内是否否买过，是否实名认证，是否办理了合适的套餐
                                String code = RealAndAvailable.isAvailable(memberLogin.getMemberId(),
                                        terminalSalePrice.toString(), phoneNum);
                                if (code.equals("-2")) {
                                    throw new Exception("6个月内已购买过集团专享商品！");
                                }
                                if (code.equals("2")) {
                                    throw new Exception("非实名制用户不可购买集团专享商品");
                                }
                                if (code.equals("3")) {
                                    throw new Exception("用户手机套餐不合理");
                                }
                                logger.info("集团用户购买商品标识：" + code);
                                orderUserRef.setUserType("1");//集团客户购机标示
                            }
                            orderSubDetail.setGoodsSkuPrice(terminalSalePrice);
                            orderSubAmount += terminalSalePrice * goodsBuyNum;
                        } else {
                            if (marketId != null) {
                                orderSubDetail.setGoodsSkuPrice(goodsWater.getGoodsSalePrice());
                                orderSubAmount += 1 * goodsWater.getGoodsSalePrice();
                            } else {
                                orderSubDetail.setGoodsSkuPrice(goodsWater.getGoodsSalePrice());
                                orderSubAmount += goodsWater.getGoodsSalePrice().longValue() * goodsBuyNum;
                            }
                        }

                        skuIds.add(car.getGoodsSkuId());
                        goodsNumCount += car.getGoodsBuyNum();
                        //日志记录用
                        goodsPrice = goodsWater.getGoodsSalePrice() + ",";

						/*设置供应商*/
                        Map<String, Object> paramMap = Maps.newHashMap();
                        paramMap.put("goodsSkuId", car.getGoodsSkuId());
                        List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoByCds(paramMap);
                        if (CollectionUtils.isNotEmpty(goodsList)) {
                            TfShopGoodsRef shopGoodsRef = goodsList.get(0).getShopGoodsRef();
                            if (shopGoodsRef != null) {
                                Long supplierShopId = shopGoodsRef.getSupplierShopId();
                                String supplierShopName = shopGoodsRef.getSupplierShopName();
                                if (supplierShopId != null) {
                                    orderSubDetail.setSupplierShopId(supplierShopId);
                                    orderSubDetail.setSupplierShopName(supplierShopName);
                                } else {
                                    orderSubDetail.setSupplierShopId(car.getShopId());
                                    orderSubDetail.setSupplierShopName(car.getShopName());
                                }
                            } else {
                                orderSubDetail.setSupplierShopId(car.getShopId());
                                orderSubDetail.setSupplierShopName(car.getShopName());
                            }
                        }
                    }

                    //设置订单商品合约订购信息
                    TfOrderDetailContract contract = sessionCarModel.getOrderDetailContract();
                    if (contract != null && car.getGoodsType() == 1) {
                        String sureNo = "";
                        //补充合约担保号码
                        if (null != session.getAttribute("sureNo_99Buy") && !session.getAttribute("sureNo_99Buy").equals(""))
                            sureNo = session.getAttribute("sureNo_99Buy").toString();

                        List<ActivityGoodsPara> tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");

                        if (null != tempList && tempList.size() > 0) {
                            //明星机、99购机资格校验
                            for (ActivityGoodsPara activityGoodsPara : tempList) {
                                if (activityGoodsPara.getProCode().equals(contract.getContractId())) {
                                    //明星机、99购机资格校验
                                    goodsService.checkOtherContract(contract, activityGoodsPara, sureNo);
                                    //如果是老人担保机，需要减免200元
                                    if ("99812787".equals(contract.getContractId())) {
                                        //单位分
                                        //合约担保号码
                                        if (null != sureNo && !"".equals(sureNo)) contract.setSureno(sureNo);
                                        //单位分
                                        //  orderSubAmount=  orderSubAmount-10000;
                                        orderSubAmount = orderSubAmount - Long.parseLong(activityGoodsPara.getDisMount()) * 100;
                                    }
                                }
                            }
                        }
                        writerFlowLogOther(streamNo,"",memberLogin.getMemberLogingName(),getClass().getName(),
                                "submitOrder",null,"合约计划用户资格查询",objectToMap(contract));
                        logger.info("合约计划用户资格查询" + contract.toString());
                        logger.info("合约计划用户资格查询" + contract.getProductTypeCode());
                        // 合约计划用户资格查询
                        if ("25".equals(contract.getProductTypeCode())) {
                            goodsService.contractSaleCheck(contract);
                            TfGoodsInfo goodsInfo = goodsManageService.queryGoodsInfo(Long.valueOf(cars.get(0).getGoodsId()));
                            if (StringUtils.isNotEmpty(goodsInfo.getAgreeGroupNo())
                                    && !contract.getContractId().equals(goodsInfo.getAgreeGroupNo())) {
                                writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                                        objectToMap(contract),"订单提交合约计划与商品配置不一致！"+goodsInfo.getAgreeGroupNo());
                                logger.error("订单提交合约计划与商品配置不一致！{},{}", contract.getContractId(), goodsInfo.getAgreeGroupNo());
                                throw new Exception("合约数据异常!");
                            }
                        }

                        //合约预占
                        TerminalCampOnCondition campOnCondition = new TerminalCampOnCondition();
                        campOnCondition.setSerialNumber(contract.getSerialNumber());
                        campOnCondition.setCampOn("1");//1-预占，0-取消预占
                        Map campOnMap = contractSaleService.terminalCampOn(campOnCondition);
                        JSONObject campOnJSON = JSONObject.parseArray(campOnMap.get("result").toString()).getJSONObject(0);
                        String campOnResultcode = campOnJSON.getString("X_RESULTCODE");
                        String campOnResultInfo = campOnJSON.getString("X_RESULTINFO");
                        if (!"0".equals(campOnResultcode) || !"ok".equalsIgnoreCase(campOnResultInfo)) {
                            writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                                    objectToMap(campOnCondition),"合约预占失败："+campOnResultInfo);
                            throw new Exception(campOnResultInfo);
                        } else {
                            isCampOn = true;
                            orderSubDetail.setOrderDetailContract(contract);
                        }
                    }

					/*设置号卡订单明细*/
                    if (orderDetailSim != null && rooCategoryId == 2) {
                        // IP地址购买号码次数校验
                        int ipPhoneNumOrders = simBusiService.getIpPhoneNumOrders(Servlets.getRemortIP(request));
                        if (ipPhoneNumOrders > 30) {
                            throw new Exception("同一IP地址1天内进行网上选号最多不超过30个");
                        }

                        //身份证信息校验
                        goodsService.realityVerify(cModel.getOrderDetailSim());

                        //设置供应商
                        orderSubDetail.setSupplierShopId(car.getShopId());
                        orderSubDetail.setSupplierShopName(car.getShopName());
                        orderDetailSim.setMemberId(memberLogin.getMemberId());

                        if (memberInfo != null && StringUtils.isNotBlank(memberInfo.getMemberRealname())) {
                            orderDetailSim.setCustName(memberInfo.getMemberRealname());
                            //orderDetailSim.setGender(memberInfo.getMemberSex());
                        } else {
                            orderDetailSim.setCustName(memberLogin.getMemberLogingName());
                        }

						/*if (memberLogin.getMemberPhone() != null) {
							orderDetailSim.setContactPhone(String.valueOf(memberLogin.getMemberPhone()));
						} else {
							orderDetailSim.setContactPhone(null);
						}*/

                        // 设置套餐编码
                        String cityCode = orderDetailSim.getCityCode();
                        String productId = orderDetailSim.getSimProductId();
                        String baseSet = orderDetailSim.getBaseSet();
                        String cityCodeSuffix = cityCode.substring(2);
                        logger.info("baseSet:" + baseSet);
                        logger.info("productId:" + productId);
                        baseSet = baseSet.replace("**", cityCodeSuffix);
                        productId = productId.replace("**", cityCodeSuffix);
                        orderDetailSim.setSimProductId(productId);
                        orderDetailSim.setBaseSet(baseSet);
                        orderDetailSim.setRegType("1");  // 设置证件类型

                        // 查询接口重置金额 开始
                        String phone = orderDetailSim.getPhone();
                        if (StringUtils.isNotBlank(phone)) {
                            Map<String, Object> numParamMap = Maps.newHashMap();
                            numParamMap.put("X_CHOICE_TAG", 3);//分支：1-自助选号，3-网上商城选号
                            numParamMap.put("PARA_VALUE4", 2);//最大数量，默认每次查询出9个号码
                            String[] data = phone.split("_");// 获取号卡类型
                            orderDetailSim.setPhone(data[0]);
                            numParamMap.put("CODE_TYPE_CODE", data[1]);//号码类型：1-普号，2-靓号
                            numParamMap.put("PARA_VALUE5", 3);//1：号头查询，2：号尾查询，3：模糊查询
                            numParamMap.put("START_SERIAL_NUMBER", data[0]);//号码特征1
                            numParamMap.put("TRADE_EPARCHY_CODE", cityCode);//归属地市

                            NetNumQueryCondition condition = new NetNumQueryCondition();
                            condition.setConditionExtendsMap(numParamMap);
                            List<NetNum> numInfos = netNumServerService.netNumQuery(condition);

                            if (CollectionUtils.isNotEmpty(numInfos)) {
                                NetNum info = numInfos.get(0);
                                Long costNoRule = Long.valueOf(info.getCostNoRule());

                                if (costNoRule != null && costNoRule != 0) {
                                    car.setGoodsSalePrice(costNoRule * 100);
                                    orderDetailSim.setPreFee(costNoRule * 100);
                                } else {
                                    car.setGoodsSalePrice(10000L);
                                    orderDetailSim.setPreFee(10000L);
                                }
                            } else {
                                car.setGoodsSalePrice(orderDetailSim.getPreFee() * 100);
                                orderDetailSim.setPreFee(orderDetailSim.getPreFee() * 100);
                            }
                        }
                        // 查询接口重置金额  结束
                        orderSubDetail.setOrderDetailSim(orderDetailSim);
                        orderSubDetail.setGoodsSkuId(Long.valueOf(orderDetailSim.getPhone()));//号卡订单：号码作为SKUID
                        orderSubDetail.setGoodsId(Long.valueOf(orderDetailSim.getPhone()));//号卡订单：号码作为GoodsId
                        orderSubDetail.setGoodsFormat(orderDetailSim.getPhone());//号卡订单：号码作为商品规格
                        orderSubDetail.setGoodsSkuPrice(car.getGoodsSalePrice());
                        orderSubDetail.setGoodsSkuNum(1L);
                        orderSubAmount = car.getGoodsSalePrice();
                        goodsNumCount = car.getGoodsBuyNum();
                        skuIds.add(Long.valueOf(orderDetailSim.getPhone()));
                    }

                    orderSub.addOrderDetail(orderSubDetail);
                }
            } else {
                //秒杀资格校验
                if (marketId != null) {
                    TfMemberMarketSignIn signIn = new TfMemberMarketSignIn();
                    signIn.setMarketId(marketId);
                    signIn.setMemberId(memberLogin.getMemberId());
                    signIn.setPhoneNumber(memberLogin.getMemberLogingName());
                    boolean hasQualified = signInService.verifySecKillQualified(signIn);
                    if (!hasQualified) {
                        throw new Exception("没有活动秒杀资格");
                    }
                }

                TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
                TfGoodsInfo goodsInfo = new TfGoodsInfo();
                goodsInfo.setGoodsId(goodsId);
                goodsInfo = goodsManageService.queryGoodsInfoById(goodsId, CommonParams.CHANNEL_CODE);

                //日志记录用 begin
                logGoodsId += goodsId + "$" + goodsInfo.getGoodsSkuId() + ",";
                logGoodsName += goodsInfo.getGoodsName() + ",";
                //日志记录用 end

                TfCategory category = new TfCategory();
                category.setCategoryId(goodsInfo.getCategoryId());
                category = goodsQueryService.queryRootCategory(category);
                long rooCategoryId = category.getCategoryId();
                orderSubDetail.setRootCateId(rooCategoryId);

                // 不是号卡
                if (rooCategoryId != 2) {
                    String isFinancialPackageOrder = processFinancialPackageOrder(goodsInfo, memberLogin);
                    orderSub.setIsFinancialPackage(isFinancialPackageOrder);
                }

                //设置订单明细信息
                orderSubDetail.setShopId(goodsInfo.getShopId());
                orderSubDetail.setShopName(goodsInfo.getShopName());
                orderSubDetail.setShopTypeId(1);
                orderSubDetail.setGoodsName(goodsInfo.getGoodsName());
                orderSubDetail.setGoodsImgUrl(goodsInfo.getGoodsImgUrl());
                orderSubDetail.setGoodsRemark(goodsInfo.getRemarks());
                orderSubDetail.setGoodsSkuGiveIntegral(0L);
                orderSubDetail.setGoodsSkuId(goodsInfo.getGoodsSkuId());
                orderSubDetail.setGoodsFormat("蓝色：10G");

                if (orderDetailSim == null && rooCategoryId != 2) {

//					//校验库存
                    Long goodsSkuId = goodsInfo.getGoodsSkuId();
                    Long goodsBuyNum = 1L;
                    //活动限购，
                    if (null != marketId) {
                        //查询加上已下单的订单
                        Map param = new HashMap();
                        param.put("marketId", marketId);
                        param.put("goodsSkuId", goodsInfo.getGoodsSkuId());
                        param.put("memberId", memberLogin.getMemberId());
                    }

					/*扣减库存*/
                    Boolean result;
					/*先占用库用 提交订单 如果购物车商品的店铺ID为1 占用实物库存 其他店铺的订单则无需占用库存的概念  */
                    if (marketId != null) {//是否活动商品(redis)
                        result = goodsService.updateMarketStock(marketId, goodsSkuId, goodsBuyNum);//更新活动商品库存(redis)
                        //如果是移动自营店， 则店铺Id为1 扣减虚拟库存  到时候活动下架 再统一减实物库存和减占用库存
                        //result = marketService.updateMarketStock(marketId,
                        //		goodsSkuId, goodsBuyNum.intValue());// 更新活动商品库存
                        stockIsUpdated = true;
                    } else {
                        //result = goodsSkuService.updateStock(goodsSkuId,goodsBuyNum.intValue());//更新商品库存
                        //如果是移动自营店， 则店铺Id为1 扣减虚拟库存
                        if (goodsInfo.getShopId() != null && goodsInfo.getShopId() == 1L) {
                            result = goodsSkuService.updateOccupyStock(goodsSkuId, goodsBuyNum.intValue());//更新占用库存
                        } else {//非移动自营店
                            result = goodsSkuService.updateStock(goodsSkuId, goodsBuyNum.intValue());// 更新商品库存
                        }

                        //更新商品销量
                        TfGoodsSkuValue skuValue = new TfGoodsSkuValue();
                        skuValue.setChnlCode(CommonParams.CHANNEL_CODE);
                        skuValue.setGoodsSaleNum(goodsBuyNum);
                        skuValue.setGoodsSkuId(goodsSkuId);
                        skuValue.setGoodsType(String.valueOf(goodsInfo.getGoodsType()));
                        goodsSkuValueService.updateGoodsSkuValue(skuValue);
                        stockIsUpdated = true;
                    }

                    if (!result) {
                        writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                                objectToMap(goodsInfo),goodsInfo.getGoodsName()+"-库存不足");
                        throw new Exception(goodsInfo.getGoodsName() + "-库存不足");
                    }

					/*设置商品单品页URL*/
                    Map<String, Object> goodsParams = new HashMap<>();
                    goodsParams.put("goodsId", goodsId);
                    List<TfShopGoodsChannelRef> urls = goodsManageService.queryShopGoodsChannelRefByCds(goodsParams);
                    for (TfShopGoodsChannelRef url : urls) {
                        String channelCode = url.getChannelCode();
                        if (OrderConstant.CHANNEL_SHOP.equals(channelCode)) {
                            orderSubDetail.setGoodsUrl(url.getGoodsUrl());
                        } else if (OrderConstant.CHANNEL_SHOPWAP.equals(channelCode)) {
                            orderSubDetail.setWapGoodsUrl(url.getGoodsUrl());
                        }
                    }

                    /*设置产品SKU ID*/
                    Map<String, Object> goodsParam = new HashMap<>();
                    goodsParam.put("goodsSkuId", goodsInfo.getGoodsSkuId());
                    List<TfGoodsSku> skuList = goodsManageService.queryGoodsSkuInfoByCds(goodsParam);
                    TfGoodsSku goodsSku = skuList.get(0);
                    Long prodSkuId = goodsSku.getProdSkuId();


                    orderSubDetail.setProdSkuId(prodSkuId);
                    orderSubDetail.setProdBossCode(goodsInfo.getProdBossCode());
                    orderSubDetail.setMarketId(marketId);
                    orderSubDetail.setGoodsId(goodsId);
                    orderSubDetail.setGoodsSkuId(goodsSkuId);
                    orderSubDetail.setGoodsSkuNum(goodsBuyNum);
                    orderSubDetail.setGoodsSkuPrice(goodsInfo.getGoodsSalePrice());
					/*针对三高客户购买指定终端降价的活动*/
                    boolean isAfterCurrentDate = goodsService.isAfterCurrentDate("2016-10-01 00:00:00");
                    boolean isCustomizeOrVotelTerminal = goodsService.isCustomizeOrVotelTerminal(goodsId);
                    boolean is3HighUser = goodsService.is3HighUser(memberLogin);
                    //是移动定制版或支持VOTEL功能的终端 && 是三高客户 && 当前日期小于2016-09-31
                    if (isCustomizeOrVotelTerminal && is3HighUser && isAfterCurrentDate) {
                        Long terminalSalePrice = goodsService.getTerminalSalePrice(goodsInfo.getGoodsSalePrice());
                        orderSubDetail.setGoodsSkuPrice(terminalSalePrice);
                        orderSubAmount += terminalSalePrice * goodsBuyNum;
                    } else {
                        orderSubDetail.setGoodsSkuPrice(goodsInfo.getGoodsSalePrice());
                        orderSubAmount += goodsInfo.getGoodsSalePrice().longValue() * goodsBuyNum;
                    }

                    skuIds.add(goodsInfo.getGoodsSkuId());
                    goodsNumCount += 1;
                    //日志记录用
                    goodsPrice = goodsInfo.getGoodsSalePrice() + ",";

					/*设置供应商*/
                    Map<String, Object> paramMap = Maps.newHashMap();
                    paramMap.put("goodsSkuId", goodsInfo.getGoodsSkuId());
                    List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoByCds(paramMap);
                    if (CollectionUtils.isNotEmpty(goodsList)) {
                        TfShopGoodsRef shopGoodsRef = goodsList.get(0).getShopGoodsRef();
                        if (shopGoodsRef != null) {
                            Long supplierShopId = shopGoodsRef.getSupplierShopId();
                            String supplierShopName = shopGoodsRef.getSupplierShopName();
                            if (supplierShopId != null) {
                                orderSubDetail.setSupplierShopId(supplierShopId);
                                orderSubDetail.setSupplierShopName(supplierShopName);
                            } else {
                                orderSubDetail.setSupplierShopId(goodsInfo.getShopId());
                                orderSubDetail.setSupplierShopName(goodsInfo.getShopName());
                            }
                        } else {
                            orderSubDetail.setSupplierShopId(goodsInfo.getShopId());
                            orderSubDetail.setSupplierShopName(goodsInfo.getShopName());
                        }

                    }
                }

                //设置订单商品合约订购信息
                TfOrderDetailContract contract = sessionCarModel.getOrderDetailContract();
				/*设置号卡订单明细*/
                orderSub.addOrderDetail(orderSubDetail);
            }

			/* 设置推荐工号
			TfOrderRecommendContact recommendContact = cModel.getRecommendContact();
			String referrer = (String)session.getAttribute("referrer");

			if (recommendContact == null) {
			    recommendContact = new TfOrderRecommendContact();
			}

			// 设置推荐工号
            if (StringUtils.isNotBlank(referrer)) {
                recommendContact.setRecommendContactNo(referrer);
            }
             设置推荐工号结束 */
            //设置推荐人信息
            orderSub.setRecommendContact(cModel.getRecommendContact());

            //设置订单收件人信息
            MemberAddress ma=cModel.getMemberAddress();
            if(null!=cModel.getMemberAddress() && null!=cModel.getMemberAddress().getMemberAddressId()) {
                MemberAddress  maTmp = memberAddressService.getAddr(cModel.getMemberAddress().getMemberAddressId());
                ma.setMemberRecipientName(maTmp.getMemberRecipientName());
                ma.setMemberRecipientPhone(maTmp.getMemberRecipientPhone());
                ma.setMemberRecipientAddress(maTmp.getMemberRecipientAddress());
            }
            Integer deliveryModeId = cModel.getDeliveryMode().getDeliveryModeId();
            if (deliveryModeId == 1 && ma != null && StringUtils.isNotBlank(ma.getMemberRecipientName())) {
                TfOrderRecipient receipt = new TfOrderRecipient();
                receipt.setOrderRecipientProvince(ma.getMemberRecipientProvince());
                receipt.setOrderRecipientCity(ma.getMemberRecipientCity());
                receipt.setOrderRecipientCounty(ma.getMemberRecipientCounty());
                receipt.setOrderRecipientAddress(ma.getMemberRecipientAddress());
                receipt.setOrderRecipientPhone(ma.getMemberRecipientPhone());
                receipt.setOrderRecipientName(ma.getMemberRecipientName());
                orderSub.setRecipient(receipt);
                if (cModel.getReceiptTime() != null) {
                    orderSub.setReceiptTimeId(cModel.getReceiptTime().getReceiptTimeId());// 收货时间段ID
                }
            }

            //设置订单信息
            orderSub.setOrderSubRemark(cModel.getOrderSubRemark());//用户备注
            orderSub.setOrderChannelCode(CommonParams.CHANNEL_CODE);//渠道编码，WAP
            orderSub.setPayModeId(cModel.getPayMode().getPayModeId());//支付方式
            orderSub.setDeliveryModeId(cModel.getDeliveryMode().getDeliveryModeId());//配送方式
            orderSub.setHallAddress(cModel.getHallAddress());//自提网点
            orderSub.setIsInvoicing(cModel.getIsInvoicing());//是否开发票
            orderSub.setInvoicingTitle(cModel.getInvoicingTitle());//发票抬头

            // 设置营销活动信息
            if (marketInfo != null) {
                orderSub.setPromotionId(marketInfo.getActivityId());
                orderSub.setPromotionName(marketInfo.getActivityName());
                OrderConstant.PROMOTION_TYPE type = OrderConstant.PROMOTION_TYPE.getPromotionType(marketInfo.getActivityType() + "");
                if (type != null) {
                    orderSub.setPromotionTypeId(type.getValue());
                    orderSub.setPromotionTypeName(type.getName());
                }
            }

            if (price != null) {
                orderSub.setPromotionId(price.getId());
                orderSub.setPromotionName(price.getActTitle());
                if ("1".equals(price.getRsrv1())) {
                    orderSub.setPromotionTypeId(OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_10.getValue());
                    orderSub.setPromotionTypeName(OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_10.getName());
                } else {
                    orderSub.setPromotionTypeId(OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_11.getValue());
                    orderSub.setPromotionTypeName(OrderConstant.PROMOTION_TYPE.PROMOTION_TYPE_11.getName());
                }
            }

            if (orderDetailSim != null) {
                //购买资格校验
                ContractSaleCheckCondition condition = new ContractSaleCheckCondition();
                condition.setCheckType(1);
                condition.setIdType("00");
                condition.setPsptId(orderDetailSim.getPsptId());
                condition.setSerialNumber(orderDetailSim.getPhone());
                condition.setCity(orderDetailSim.getCityCode());
                Map resultMap = contractSaleCheckService.contractSaleCheck(condition);

                String respCode = String.valueOf(resultMap.get("respCode"));
                String respDesc = String.valueOf(resultMap.get("respDesc"));
                if (!("0".equals(respCode) && "ok".equals(respDesc))) {
                    writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            objectToMap(condition),"-[用户购买号码资格校验接口]调用失败"+respDesc);
                    throw new Exception("[用户购买号码资格校验接口]调用失败");
                }

                String result = String.valueOf(resultMap.get("result"));
                JSONObject jsonObject = JSONArray.parseArray(result).getJSONObject(0);
                String xResultCode = jsonObject.getString("X_RESULTCODE");
                String xResultInfo = jsonObject.getString("X_RESULTINFO");
                String isBlackList = jsonObject.getString("ISBLACKLIST");
                int allowNum = jsonObject.getIntValue("ALLOW_NUM");
                if (!"0".equals(xResultCode)) {
                    writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            objectToMap(jsonObject),result);
                    throw new Exception(xResultInfo);
                }

                if ("1".equals(isBlackList)) {
                    writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            objectToMap(condition),"此用户身份证为黑名单用户，不能购买号码");
                    throw new Exception("此用户身份证为黑名单用户，不能购买号码");
                }

                if (allowNum == 0) {
                    writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            objectToMap(condition),"此用户身份证购买号码已购买5个号码，不能再继续购买");
                    throw new Exception("此用户身份证购买号码已购买5个号码，不能再继续购买");
                }

                //号码临时选占
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("RES_TYPE_CODE", "0");//
                paramMap.put("RES_NO", orderDetailSim.getPhone());//号码
                paramMap.put("OCCUPY_TYPE_CODE", "2");//临时选占30分钟-2
                paramMap.put("X_TAG", "3");//
                paramMap.put("X_CHOICE_TAG", "1");//

                SelTempOccupyResCondition resCondition = new SelTempOccupyResCondition();
                resCondition.setConditionExtendsMap(paramMap);
                Map selTempReulstMap = netNumServerService.selTempOccupyRes(resCondition);
                respCode = String.valueOf(selTempReulstMap.get("respCode"));
                logger.info("号码预占结果:" + selTempReulstMap);
                if (!"0".equals(respCode)) {
                    writerFlowLogConditionAndReturn(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            selTempReulstMap,"您选择的号码已被其他人预占，请选择其它号码试一试！",
                            objectToMap(resCondition),"!\"0\".equals("+respCode);
                    throw new Exception("您选择的号码已被其他人预占，请选择其它号码试一试！");
                }
                writerFlowLogOther(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                        selTempReulstMap,"预占结果",objectToMap(resCondition));
                logger.info("预占结果:" + selTempReulstMap);

                // 删除备选号码
                TfPhonenumSelect phonenumSelect = new TfPhonenumSelect();
                phonenumSelect.setMemberId(memberLogin.getMemberId());
                phonenumSelect.setPhoneNum(orderDetailSim.getPhone());
                phonenumSelectService.deletePhonenumSelect(phonenumSelect);
            }

            // 号卡订单不进行风控
            if (orderDetailSim == null) {
                if (!canBuy(request, memberLogin, sessionCarModel, orderSub, 2)) {
                    writerFlowLogThrowable(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),"submitOrder",
                            objectToMap(orderSub),"购买数量超过限制或没有购买权限");
                    throw new Exception("购买数量超过限制或没有购买权限!");
                }
            }


            // 提交生成订单
            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);

            newOrderFlag = true;
            List<String> orderNoList = Lists.newArrayList();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date discountDate = sdf.parse(discountTime);
            Date currentDate = new Date();
            if (marketId == null && currentDate.before(discountDate)) {
                int index = 0;
                for (TfOrderSub order : orderSubList) {
                    int payModeId = order.getPayModeId() == null ? 0 : order.getPayModeId();
                    int orderType = order.getOrderTypeId() == null ? 0 : order.getOrderTypeId();
                    if (index < 1 && payModeId == 2 && (orderType == 1 || orderType == 2) && order.getOrderSubPayAmount() > 1000L) {
                        order.setOrderSubDiscountAmount(1000L);
                        order.setOrderSubPayAmount(order.getOrderSubPayAmount() - 1000L);
                        order.setOrderSubAmount(order.getOrderSubAmount() - 1000L);
                        TfOrderSub orsub = new TfOrderSub();
                        orsub.setOrderSubId(order.getOrderSubId());
                        orsub.setOrderSubPayAmount(order.getOrderSubPayAmount());
                        orsub.setOrderSubDiscountAmount(1000L);
                        orsub.setOrderSubAmount(order.getOrderSubAmount());
                        orderService.updateOrder(orsub);
                        index++;
                        logger.error("在线购机立减10元" + order.getOrderSubId());

                    }
                    orderNoList.add(order.getOrderSubNo());
                }
            } else {
                for (TfOrderSub order : orderSubList) {
                    //如果是老人担保机，需要减免200元
                    List<ActivityGoodsPara> tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");
                    if (null != tempList && tempList.size() > 0) {
                        if ("99812787".equals(orderSub.getDetailList().get(0).getOrderDetailContract().getContractId())) {
                            if (orderSub.getDetailList().size() > 0 && null != orderSub.getDetailList().get(0).getOrderDetailContract().getContractId()) {
                                for (ActivityGoodsPara activityGoodsPara : tempList) {
                                    if ("99812787".equals(activityGoodsPara.getProCode())) {
                                        order.setOrderSubDiscountAmount(Long.parseLong(activityGoodsPara.getDisMount() == null ? "0" : activityGoodsPara.getDisMount()) * 100);
                                        order.setOrderSubPayAmount(order.getOrderSubPayAmount() - Long.parseLong(activityGoodsPara.getDisMount() == null ? "0" : activityGoodsPara.getDisMount()) * 100);
                                        order.setOrderSubAmount(order.getOrderSubAmount());
                                        TfOrderSub orsub = new TfOrderSub();
                                        orsub.setOrderSubId(order.getOrderSubId());
                                        orsub.setOrderSubPayAmount(order.getOrderSubPayAmount());
                                        orsub.setOrderSubDiscountAmount(Long.parseLong(activityGoodsPara.getDisMount() == null ? "0" : activityGoodsPara.getDisMount()) * 100);
                                        orsub.setOrderSubAmount(order.getOrderSubAmount());
                                        orderService.updateOrder(orsub);
                                    }
                                }
                            }
                        }
                    }
                    //遍历子订单
                    for (TfOrderSubDetail tfOrderSubDetail : order.getDetailList()) {
                        //只能减扣一台
                        //非合约机
                        if (tfOrderSubDetail.getOrderDetailContract() == null || tfOrderSubDetail.getOrderDetailContract().equals("")) {
                            if (!cutBool) {
                                //Long actNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
                                //price= activityTargetNumberService.queryActivityCutByGoodsIdAndActNumber(tfOrderSubDetail.getGoodsId()+"", actNumber+"");
                                //锁定红包
                                if (price != null) {
                                    ActivityCutPrice activityCutPrice = new ActivityCutPrice();
                                    activityCutPrice.setId(price.getId()); //之前取到红包ID
                                    activityCutPrice.setStatus("2");
                                    int row = activityTargetNumberService.updateActivityCutPrice(activityCutPrice);
                                    if (row == 1) {
                                        order.setOrderSubDiscountAmount(((Double) price.getDiscount()).longValue() * 100);
                                        order.setOrderSubPayAmount(order.getOrderSubPayAmount() - ((Double) price.getDiscount()).longValue() * 100);
                                        order.setOrderSubAmount(order.getOrderSubAmount());
                                        TfOrderSub orsub = new TfOrderSub();
                                        orsub.setOrderSubId(order.getOrderSubId());
                                        orsub.setOrderSubPayAmount(order.getOrderSubPayAmount());
                                        orsub.setOrderSubDiscountAmount(((Double) price.getDiscount()).longValue() * 100);
                                        orsub.setOrderSubAmount(order.getOrderSubAmount());
                                        orderService.updateOrder(orsub);
                                        orderSubPayAmount = order.getOrderSubPayAmount();
                                        cutBool = true;
                                    }
                                }
                            }
                        }
                    }
                    orderNoList.add(order.getOrderSubNo());
                }
            }
            String orderNos = StringUtils.join(orderNoList, ",");
            writerFlowLogExitMenthod(streamNo,orderNos,memberLogin.getMemberLogingName(),getClass().getName(),
                    "submitOrder",objectToMap(orderSub),objectToMap(orderSubList),"订单提交");
            model.addAttribute("orderNos", orderNos);
            model.addAttribute("skuIds", skuIds);
            model.addAttribute("goodsNumCount", goodsNumCount);

            if (orderSubPayAmount == 0) {
                orderSubPayAmount = orderSubAmount;
            }

            session.setAttribute("orderSubPayAmount", orderSubPayAmount);
            session.setAttribute("orderSubList", orderSubList);
            session.setAttribute("orderSubAmount", orderSubAmount);

            boolean isSameShop = true;
            Long shopId = null;
            for (TfOrderSub order : orderSubList) {
                if (shopId == null) {
                    shopId = order.getShopId();
                }

                if (shopId != order.getShopId()) {
                    isSameShop = false;
                    break;
                }

            }

            model.addAttribute("isSameShop", isSameShop);

            try {
                // 根据用户IP地址记录选号次数
                Integer orderTypeId = orderSubList.get(0).getOrderTypeId();
                if (orderTypeId.intValue() == 7) {
                    simBusiService.setIpPhoneNumOrders(com.ai.ecs.common.web.Servlets.getRemortIP(request));
                }
            } catch (Exception e) {
                logger.error("", e);
            }


            //砍价下单成功更新状态
            if (price != null) {
                ActivityCutPrice activityCutPrice = new ActivityCutPrice();
                activityCutPrice.setId(price.getId()); //之前取到红包ID
                activityCutPrice.setStatus("3");
                activityCutPrice.setOrderNo(orderNos);
                activityTargetNumberService.updateActivityCutPrice(activityCutPrice);
            }

            //删除购物车中已购买商品
            for (TfUserGoodsCar car : cars) {
                String key = "B2C" + memberLogin.getMemberId();
                if (JedisClusterUtils.exists(key)) {
                    userGoodsCarService.delete(key, car.getGoodsSkuId());
                }
            }

            //减少活动秒杀机会
            //goodsService.decreaseSecKillChance(memberLogin.getMemberId(),cars);

            //更新会员已抢购数量、已抢购数量
            //goodsService.updateMarketBuyNum(cars,memberLogin.getMemberId());

			/*JedisClusterShopUtils.hdel("ACTIVITY_CHECK_"
					+ marketId, memberLogin.getMemberPhone() + "");*/

            if (marketId != null) {
                session.removeAttribute("hasQualiFlag");

                JedisClusterShopUtils.set("ACTIVITY_CHECK_ONE" + memberVo.getMemberLogin().getMemberPhone(), "true", 60 * 60 * 24 * 30 * 6);
                JedisClusterShopUtils.expires("ACTIVITY_CHECK_ONE" + memberVo.getMemberLogin().getMemberPhone(), 60 * 60 * 24 * 30 * 6);

                //  秒杀用户
                jedisCluster.lpush("ACTIVITY_CHECK_ONE_" + marketId, memberPhone + "");
            }


			/*if(marketId!=null){
				JedisClusterShopUtils.set("ACTIVITY_CHECK_ONE"
						+ marketId+"_BY_"+memberVo.getMemberLogin().getMemberPhone(),"true", 60*60*24*30*6);
				JedisClusterShopUtils.expires("ACTIVITY_CHECK_ONE"
						+ marketId+"_BY_"+memberVo.getMemberLogin().getMemberPhone(), 60*60*24*30*6);
			}*/

            BusiLogUtils.writerLogging(request, "buygoods", logGoodsName, logGoodsId,
                    startTime, goodsPrice, "0", orderNos,
                    request.getRequestURI(), null, "1", sessionCarModel);
            writerFlowLogEnterMenthod(streamNo,orderNos,memberLogin.getMemberLogingName(),getClass().getName(),"submitOrder",sessionCarModel,"订单提交",request);
        } catch (Exception e) {
            e.printStackTrace();
            //砍价下单更新状态
            if (price != null) {
                ActivityCutPrice activityCutPrice = new ActivityCutPrice();
                activityCutPrice.setId(price.getId()); //之前取到红包ID
                activityCutPrice.setStatus("1");
                activityCutPrice.setOrderNo("");
                activityTargetNumberService.updateActivityCutPrice(activityCutPrice);
            }
            if (stockIsUpdated && newOrderFlag == false) { //回库
                goodsService.returnGoodsStock(updatedCars);
            }
            if (isCampOn) {
                String serialNumber = sessionCarModel.getOrderDetailContract().getSerialNumber();
                TerminalCampOnCondition campOnCondition = new TerminalCampOnCondition();
                campOnCondition.setSerialNumber(serialNumber);
                campOnCondition.setCampOn("0");//1-预占，0-取消预占
                contractSaleService.terminalCampOn(campOnCondition);
            }

            BusiLogUtils.writerLogging(request, "buygoods", logGoodsName, logGoodsId,
                    startTime, goodsPrice, "0", "",
                    request.getRequestURI(), e, "2", sessionCarModel);
            e.printStackTrace();
            logger.error("订单提交失败", e);
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"submitOrder",
                    null,"订单提交失败:"+processThrowableMessage(e));
            throw new Exception("订单提交失败:" + e.getMessage());
        }

        return "web/goods/buy/choosePayPlatform";
    }

    /**
     * 跳转到选择支付平台页面
     *
     * @param model
     * @param couponInfo
     * @return
     */
    @RequestMapping("/linkToChoosePayPlatform")
    public String linkToChoosePayPlatform(Model model, CouponInfo couponInfo,HttpServletRequest request) {
        String streamNo= LogUtils.createStreamNo();
        try {
            Session session = UserUtils.getSession();
            UserGoodsCarModel sessionCarModel = (UserGoodsCarModel) session.getAttribute("carModel");
            writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                    "linkToChoosePayPlatform",couponInfo,"跳转到选择支付平台页面",request);
            List<CouponInfo> couponInfoList = Lists.newArrayList();
            couponInfoList.addAll(sessionCarModel.getCouponInfoList());
            for (CouponInfo coupon : sessionCarModel.getCouponInfoList()) {
                if (coupon.getCouponId().longValue() != couponInfo.getCouponId().longValue()) {
                    couponInfoList.add(coupon);
                }
            }
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),"linkToChoosePayPlatform",couponInfoList,"跳转到选择支付平台页面");
            model.addAttribute("couponInfoList", couponInfoList);
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"linkToChoosePayPlatform",null,processThrowableMessage(e));
            e.printStackTrace();
        }

        return "web/goods/buy/choosePayPlatform";
    }

    /**
     * 查询店铺是否支持选择的支付平台
     *
     * @param carModel
     * @return
     */
    @RequestMapping("/queryPayAccount")
    @ResponseBody
    public Map<String, Object> queryPayAccount(UserGoodsCarModel carModel,HttpServletRequest request) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String streamNo= LogUtils.createStreamNo();
        try {
            writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),"queryPayAccount",carModel
                    ,"查询店铺是否支持选择的支付平台",request);
            List<Shop> shopList = carModel.getShopList();
            short payAccountTypeId = CommonParams.PAY_PLATFORM.get(carModel.getPayPlatform());
            for (Shop shop : shopList) {
                CompanyAcctInfo companyAcctInfo = companyAcctService.getAcctByShopIdAndType(shop.getShopId(), payAccountTypeId);
                if (companyAcctInfo == null) {
                    resultMap.put("returnCode", "fail");
                    resultMap.put("returnInfo", "店铺【" + shop.getShopName() + "】不支持");
                    return resultMap;
                }
            }

            resultMap.put("returnCode", "success");
        } catch (Exception e) {
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),
                    "queryPayAccount",null,processThrowableMessage(e));
            resultMap.put("returnCode", "fail");
            resultMap.put("returnInfo", e.getMessage());
        }

        return resultMap;
    }

    /**
     * 合包理财
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/payFinancialPackage")
    public String payFinancialPackage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderNos = request.getParameter("orderNos");

        MemberVo memberVo = UserUtils.getLoginUser(request);
        String phone = null;
        if (memberVo != null && memberVo.getMemberLogin() != null) {
            if (memberVo.getMemberLogin().getMemberTypeId() != 2) { //手机登录
                throw new RuntimeException("只能使用湖南移动手机号码登陆才能购买");
            }
            phone = memberVo.getMemberLogin().getMemberLogingName();
        }

        if (StringUtils.isBlank(phone)) {
            throw new RuntimeException("只能使用湖南移动手机号码登陆才能购买");
        }

        if (StringUtils.isEmpty(orderNos)) {
            throw new Exception("订单号不能为空");
        }
        Financial financial = null;
        TfOrder order = null;
        String merchantId = null;


        try {
            Map<String, Object> resultMap = orderService.mergeOrder(orderNos);
            order = (TfOrder) resultMap.get("order");
            List<TfOrderSub> orderSubList = (List<TfOrderSub>) resultMap.get("orderSubList");
            Long orderPayAmount = order.getOrderPayAmount();

            if (!"1".equals(financialTest)) {
                CompanyAcctInfo companyAcctInfo = null;
                // 查询店铺对应和包账户信息
                if ("1".equals(orderSubList.get(0).getShopId().toString())) {
                    companyAcctInfo = companyAcctService.getAcctByShopIdAndType(orderSubList.get(0).getSupplierShopId(), (short) 2);
                } else {
                    companyAcctInfo = companyAcctService.getAcctByShopIdAndType(orderSubList.get(0).getShopId(), (short) 2);
                }

                if (companyAcctInfo == null || companyAcctInfo.getAccountNum() == null) {
                    throw new Exception("店铺的和包账户没有配制");
                }
                logger.info("innerMerchant:" + companyAcctInfo.getAccountNum());
                merchantId = companyAcctInfo.getAccountNum();
            } else {
                merchantId = "888073113010002";
            }

            financial = financialProcess.payOrder(phone, 1000000L, merchantId, orderSubList.get(0).getOrderSubNo(), request.getRemoteAddr());
            if (financial == null) {
                throw new Exception("和聚宝资金处理失败");
            }
        } catch (Exception e) {
            logger.error("和聚宝资金失败", e);
            throw new Exception("和聚宝资金处理失败：" + e.getMessage());
        }

        try {
            // 扣款成功， 调用订单流程流转支付流程
            if (financial != null && "1".equals(financial.getNoticeFalg())) {
                //保存订单支付信息
                TfOrderPay orderPay = new TfOrderPay();
                orderPay.setOrderId(order.getOrderId());
                orderPay.setOrderPayAmount(order.getOrderPayAmount());
                orderPay.setOrgCode("hebaolicai");
                orderPay.setShRule("no");
                orderPay.setHmac("no");
                orderPay.setOrderHarvestExpend("0");
                orderPay.setMerchantId(merchantId);
                MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
                orderPay.setOrderPayPerson(memberLogin.getMemberLogingName());
                orderService.saveOrderPay(orderPay);

                //订单支付信息
                TfOrderPay updateOrderPay = new TfOrderPay();
                updateOrderPay.setMerchantId(merchantId);
                updateOrderPay.setMessage("和包理财");
                updateOrderPay.setOrderHarvestExpend("0");
                updateOrderPay.setOrderId(order.getOrderId());
                updateOrderPay.setOrderPayAmount(order.getOrderPayAmount());
                updateOrderPay.setOrderPayTime(new Date());
                updateOrderPay.setPayLogId(financial.getFreezFlowId());
                updateOrderPay.setPayNo(financial.getFreezFlowId());
                updateOrderPay.setPayState("SUCCESS");
                updateOrderPay.setReturnCode("0000");
                updateOrderPay.setType("test");
                updateOrderPay.setVersion("1.0");

                OrderProcessParam param = new OrderProcessParam();
                param.setBusinessId(String.valueOf(order.getOrderId()));
                param.setOrderStatusId(OrderConstant.STATUS_PAY);
                param.setAct(1);
                param.put(Variables.ORDER_COUPON_LIST, Lists.newArrayList());
                param.put(Variables.ORDER_PAY, updateOrderPay);
                orderService.completeByOrderId(param);//订单流转
            }
        } catch (Exception e) {
            logger.error("订单流程失败", e);
            throw e;
        }

        return "redirect:/myOrder/toMyAllOrderList";
    }

    /**
     * 支付订单
     *
     * @param request
     * @param response
     * @param carModel
     * @param orderNos
     */
    @RequestMapping("/payOrder")
    public void payOrder(HttpServletRequest request, HttpServletResponse response, UserGoodsCarModel carModel, String orderNos) throws Exception {
        String streamNo= LogUtils.createStreamNo();
        try {
            writerFlowLogEnterMenthod(streamNo,orderNos,"",getClass().getName(),
                    "payOrder",carModel,"支付订单",request);
            /*订单合并：子订单关联主订单，用于支付*/
            Map<String, Object> resultMap = orderService.mergeOrder(orderNos);
            TfOrder order = (TfOrder) resultMap.get("order");
            List<TfOrderSub> orderSubList = (List<TfOrderSub>) resultMap.get("orderSubList");
            List<CouponInfo> couponInfoList = carModel.getCouponInfoList();

            //根据优惠券ID获取优惠券信息
            List<String> couponIdList = Lists.newArrayList();
            if (couponInfoList != null) {
                for (CouponInfo couponInfo : couponInfoList) {
                    if (couponInfo.getCouponId() != null) {
                        couponIdList.add(String.valueOf(couponInfo.getCouponId()));
                    }
                }
                couponInfoList = couponService.getCouponInfoList(couponIdList);
            }
            writerFlowLogOther(streamNo,orderNos,"",getClass().getName(),"payOrder",couponInfoList,"优惠券信息",carModel);
            //订单支付金额
            String orderPayAmount = goodsService.getOrderPayAmount(request, couponInfoList, orderSubList, order);

            List<Shop> shopList = carModel.getShopList();
            String payPlatform = carModel.getPayPlatform();
            String shRule = goodsService.getShRule(shopList, orderSubList, couponInfoList, payPlatform);//获取商户分润规则

            Integer orderTypeId = orderSubList.get(0).getOrderTypeId();
            String merchantId = null;
            String key = null;
            String callbackUrl = null;
            String notifyUrl = null;
            String type = null;

            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();

            if (orderTypeId == OrderConstant.TYPE_BROADBAND || orderTypeId == OrderConstant.TYPE_BROADBAND_CONTINUE) {
                merchantId = BroadbandConstants.MERCHANT_ID;
                key = BroadbandConstants.SIGN_KEY;
                callbackUrl = basePath + "/broadband/toPayResult";
                notifyUrl = afterOrderPayUrl + "/broadband/payAfterNotify";
                if ("WAPALIPAY".equals(carModel.getPayPlatform())) {
                    type = "GWDirectToPayOrg";
                } else {
                    type = CommonParams.PAY_INTERFACE_TYPE.get(carModel.getPayPlatform());
                }
            } else {
                JSONObject innerMerchant = goodsService.getInnerMerchant(orderSubList);
                logger.info("innerMerchant:" + innerMerchant);
                merchantId = innerMerchant.getString("merchantId");
                key = innerMerchant.getString("key");
                callbackUrl = basePath + "/goodsBuy/linkToPayResult";
                notifyUrl = afterOrderPayUrl + "/goodsBuy/afterPayOrder";
                type = CommonParams.PAY_INTERFACE_TYPE.get(carModel.getPayPlatform());
            }

            String orderDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
            String productName = goodsService.getProductName(orderSubList);
            String payType = CommonParams.PAY_TYPE.get(orderTypeId);
            String reserved1 = goodsService.getReserved1(orderSubList);

            Map<String, String> paramMap = Maps.newHashMap();
            paramMap.put("payOrg", carModel.getPayPlatform());//支付机构：
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
            orderPay.setOrderPayAmount(Long.parseLong(orderPayAmount));
            orderPay.setOrgCode(carModel.getPayPlatform());
            orderPay.setShRule(shRule);
            orderPay.setHmac(key);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setMerchantId(merchantId);
            MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
            orderPay.setOrderPayPerson(memberLogin.getMemberLogingName());
            orderService.saveOrderPay(orderPay);
            writerFlowLogExitMenthod(streamNo,orderNos,"",getClass().getName(),
                    "payOrder",paramMap,Maps.newHashMap().put("content",content),"支付订单");
            response.setContentType("text/html;charset=" + CommonParams.PAY_CHARSET.get(payPlatform));
            PrintWriter out = response.getWriter();
            out.print(content);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("订单支付异常：", e);
            writerFlowLogThrowable(streamNo,orderNos,"",getClass().getName(),
                    "payOrder",null,processThrowableMessage(e));
            throw new Exception("订单支付异常：" + e.getMessage());
        }

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
    public String linkToAlipayResult(Model model, String returnCode, Long chargeflowId,HttpServletRequest request) {
        String streamNo= LogUtils.createStreamNo();
        try {
            writerFlowLogEnterMenthod(streamNo,chargeflowId+"","",
                    getClass().getName(),"linkToAlipayResult",request.getParameterMap(),"跳转到订单支付结果页面，供支付中心调用（同步）",request);
            TfOrderSub orderSub = new TfOrderSub();
            orderSub.setOrderId(chargeflowId);
            List<TfOrderSub> orderSubList = orderQueryService.queryBaseOrderList(orderSub);
            model.addAttribute("orderSubList", orderSubList);
            model.addAttribute("orderSub", orderSubList.get(0));
            Long orderAmount = 0L;
            Long goodsBuyNum = 0L;
            List<Long> skuIdList = Lists.newArrayList();
            List<String> orderSubNoList = Lists.newArrayList();
            for (TfOrderSub sub : orderSubList) {
                orderAmount += sub.getOrderSubAmount();
                orderSubNoList.add(sub.getOrderSubNo());
                List<TfOrderSubDetail> detailList = sub.getDetailList();
                for (TfOrderSubDetail detail : detailList) {
                    goodsBuyNum += detail.getGoodsSkuNum();
                    skuIdList.add(detail.getGoodsSkuId());
                }
            }
            model.addAttribute("orderAmount", orderAmount);
            model.addAttribute("goodsBuyNum", goodsBuyNum);
            model.addAttribute("skuIds", StringUtils.join(skuIdList, ","));
            model.addAttribute("orderSubNos", StringUtils.join(orderSubNoList, ","));
            writerFlowLogExitMenthod(streamNo,chargeflowId+"","",getClass().getName(),"linkToAlipayResult",skuIdList,"支付回调（同步）");
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo,chargeflowId+"","",getClass().getName(),
                    "linkToAlipayResult",null,"支付同步回调失败，异常信息："+processThrowableMessage(e));
            logger.error("支付同步回调失败，异常信息:" + e);
        }

        if ("0000".equals(returnCode)) {
            return "web/goods/buy/paySuccess";
        } else {
            return "/myOrder/toMyAllOrderList";
        }
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
     * @param orderDate
     * @param payNo
     * @param org_code
     * @param organization_payNo
     * @param hmac
     */
    @RequestMapping("/afterPayOrder")
    public void afterPayOrder(HttpServletRequest request, String merchantId, String returnCode, String message, String type,
                              String version, Long amount, Long orderId, String payDate,
                              String status, String orderDate, String payNo, String org_code,
                              String organization_payNo, String hmac, String shRule) {
        logger.info("支付异步回调参数[{}]", request.getParameterMap());
        String streamNo= LogUtils.createStreamNo();
        try {
            writerFlowLogEnterMenthod(streamNo,payNo,"",getClass().getName(),"afterPayOrder",request.getParameterMap(),
                    "支付中心回调用（异步）",request);
            MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
            Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
            if (!validataService.valHmac(payParamMap, merChantBean)) {
                writerFlowLogThrowable(streamNo,payNo,"",getClass().getName(),"afterPayOrder",merChantBean,"签名验证未通过");
                throw new Exception("签名验证未通过");
            }
			/*查询优惠券信息*/
            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setOrderId(String.valueOf(orderId));
            List<CouponInfo> couponInfoList = couponService.queryMyCouponInfoList(couponInfo);

            //订单支付信息
            TfOrderPay orderPay = new TfOrderPay();
            orderPay.setMerchantId(merchantId);
            orderPay.setMessage(message);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setOrderId(orderId);
            orderPay.setOrderPayAmount(amount);
            orderPay.setOrderPayTime(DateUtils.parseDate(payDate, "yyyyMMddHHmmss"));
            orderPay.setOrgCode(org_code);
            orderPay.setPayLogId(organization_payNo);
            orderPay.setPayNo(payNo);
            orderPay.setPayState(status);
            orderPay.setReturnCode(returnCode);
            orderPay.setType(type);
            orderPay.setVersion(version);

            if ("0000".equals(returnCode) && "SUCCESS".equals(status)) {
                /*设置优惠券信息*/
                List<TfOrderSubCoupon> orderSubCouponList = Lists.newArrayList();
                if (couponInfoList != null) {
                    for (CouponInfo coupon : couponInfoList) {
                        TfOrderSubCoupon orderSubCoupon = new TfOrderSubCoupon();
                        BeanUtils.copyProperties(orderSubCoupon, coupon);
                        orderSubCoupon.setOrderSubId(Long.valueOf(coupon.getOrderId()));
                    }
                }

                OrderProcessParam param = new OrderProcessParam();
                param.setBusinessId(String.valueOf(orderId));
                param.setOrderStatusId(OrderConstant.STATUS_PAY);
                param.setAct(1);
                param.put(Variables.ORDER_COUPON_LIST, orderSubCouponList);
                param.put(Variables.ORDER_PAY, orderPay);
                orderService.completeByOrderId(param);//订单流转

                /*使用优惠券*/
                couponService.useCouponInfoMember(String.valueOf(orderId), 10);
            } else {
                OrderProcessParam param = new OrderProcessParam();
                param.setBusinessId(String.valueOf(orderId));
                param.setOrderStatusId(OrderConstant.STATUS_PAY);
                param.setAct(0);
                param.put(Variables.ORDER_PAY, orderPay);
                orderService.completeByOrderId(param);//订单流转

                /*释放优惠券*/
                couponService.useCouponInfoMember(String.valueOf(orderId), 9);
            }
        } catch (Exception e) {
            writerFlowLogThrowable(streamNo,payNo,"",getClass().getName(),"afterPayOrder",null,"支付异步回调失败，异常信息:"+processThrowableMessage(e));
            logger.error("支付异步回调失败，异常信息:" + e);
        }

    }

    /**
     * 增加关注或者删除商品关注
     */
    @RequestMapping("/addOrDelGoodsAttention")
    @ResponseBody
    public Map<String, Object> addOrDelGoodsAttention(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> resultMap = new HashMap<>();

        if (StringUtils.isBlank(request.getParameter("goodsId"))) {
            resultMap.put("isSuccess", false);
            resultMap.put("msg", "参数错误,商品编号为空,无法关注!");
            return resultMap;
        }
        //默认为添加关注
        int attentionType = 0;
        if (!StringUtils.isBlank(request.getParameter("attentionType"))) {
            attentionType = Integer.valueOf(request.getParameter("attentionType"));
        }
        Long goodsId = Long.parseLong(request.getParameter("goodsId"));
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        List<MemberFollow> memberFollows = memberFavoriteService.getMemberFollows(memberId, "1");
        boolean flag = false;
        for (MemberFollow f : memberFollows) {
            if (!flag && null != f.getMemberFollowBusiId() && f.getMemberFollowBusiId().intValue() == goodsId.intValue()) {
                flag = true;
                break;
            }
        }
        if (attentionType == 0) { //添加关注
            if (flag) {//原来已关注
                resultMap.put("isSuccess", true);
                resultMap.put("msg", "此商品已是关注商品,无需再次关注!");
            } else {
                Map<String, Object> argsMap = new HashMap<>();
                argsMap.put("goodsId", goodsId);
                argsMap.put("containShopGoodsChannelRef", true);
                argsMap.put("containGoodsStaticInfo", true);
                List<TfGoodsInfo> goodsInfos = goodsManageService.queryGoodsInfoByCds(argsMap);
                if (Collections3.isEmpty(goodsInfos)) {
                    resultMap.put("isSuccess", false);
                    resultMap.put("msg", "商品信息不在在!,无法关注");
                } else {
                    TfGoodsInfo tfGoodsInfo = goodsInfos.get(0);
                    MemberFollow memberFollow = new MemberFollow();
                    memberFollow.setMemberFollowBusiId(goodsId);
                    //设置商品默认图片
                    if (!Collections3.isEmpty(tfGoodsInfo.getGoodsStaticList())) {
                        for (TfGoodsStatic gs : tfGoodsInfo.getGoodsStaticList()) {
                            if ("0".equals(gs.getGoodsStaticDefault())) {
                                memberFollow.setMemberFollowBusiImgUrl(gs.getGoodsStaticUrl());
                                break;
                            }
                        }
                    }
                    //设置访问路径
                    if (Collections3.isEmpty(tfGoodsInfo.getTfShopGoodsChannelRefList())) {
                        for (TfShopGoodsChannelRef tsgc : tfGoodsInfo.getTfShopGoodsChannelRefList()) {
                            if ("E007".equals(tsgc.getChannelCode())) {
                                memberFollow.setMemberFollowBusiUrl(tsgc.getGoodsUrl());
                            }
                        }
                    }
                    memberFollow.setMemberFollowBusiName(tfGoodsInfo.getGoodsName());
                    memberFollow.setMemberFollowBusiType("1");
                    memberFollow.setMemberId(memberId);
                    memberFavoriteService.saveMemberFollow(memberFollow);
                    resultMap.put("isSuccess", true);
                    resultMap.put("msg", " 恭喜您,关注成功!");
                }
            }
        } else {//取消关注
            memberFavoriteService.delMemberFollow(memberId, goodsId, 1L);
            resultMap.put("isSuccess", true);
            resultMap.put("msg", " 恭喜您,取消关注成功!");
        }
        return resultMap;
    }

    /**
     * 风控判断
     *
     * @param login
     * @param carModel
     * @param orderSub
     * @param type     1,跳往提交订单之前判断，2提交订单时判断
     * @return
     */
    private boolean canBuy(HttpServletRequest request, MemberLogin login, UserGoodsCarModel carModel, TfOrderSub orderSub, int type) {
        TerminalRiskOrderQueryCond cond = new TerminalRiskOrderQueryCond();
        MemberSsoVo memberSsoVo = UserUtils.getSsoLoginUser(request, null);
        cond.setChannel(CommonParams.CHANNEL_CODE);
        if (login != null) {
            cond.setMemberLogingName(login.getMemberLogingName());
            if (login.getMemberPhone() != null)
                cond.setContactPhone(login.getMemberPhone() + "");
        }
        if (memberSsoVo != null) {
            cond.setEpachyCode(memberSsoVo.getEparchyCode());//地市编号
        }
        if (type == 1) {
            List<TfUserGoodsCar> cars = carModel.getUserGoodsCarList();
            for (TfUserGoodsCar car : cars) {
                cond.setGoodsId(car.getGoodsId());
//                cond.setBrandId(car.getCategoryId());//,brandId在后台进行查询
                cond.setBuyNum(car.getGoodsBuyNum());
                if (car.getGoodsType() == 1l)
                    cond.setOrderTypeId(1);
                else
                    cond.setOrderTypeId(2);
                if (!terminalRiskOrderService.canBuy(cond)) {
                    return false;
                }
            }
        } else if (type == 2) {
            TfOrderRecipient recipient = orderSub.getRecipient();
            List<TfUserGoodsCar> cars = carModel.getUserGoodsCarList();
            for (TfUserGoodsCar car : cars) {
                cond.setGoodsId(car.getGoodsId());
//                cond.setBrandId(car.getCategoryId());//,brandId在后台进行查询
                cond.setBuyNum(car.getGoodsBuyNum());
                if (car.getGoodsType() == 1l)
                    cond.setOrderTypeId(1);
                else
                    cond.setOrderTypeId(2);
                cond.setOrderRecipientName(recipient.getOrderRecipientName());
                cond.setOrderRecipientPhone(recipient.getOrderRecipientPhone());
                cond.setOrderRecipientProvience(recipient.getOrderRecipientProvince());
                cond.setOrderRecipientCity(recipient.getOrderRecipientCity());
                cond.setOrderRecipientCountry(recipient.getOrderRecipientCounty());
                cond.setOrderRecipientAddress(recipient.getOrderRecipientAddress());
                if (!terminalRiskOrderService.canBuy(cond)) {
                    return false;
                }
            }
        }
        return true;
    }
}