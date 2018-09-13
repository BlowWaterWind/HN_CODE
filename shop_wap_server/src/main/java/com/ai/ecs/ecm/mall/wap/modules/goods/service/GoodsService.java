package com.ai.ecs.ecm.mall.wap.modules.goods.service;

import com.ai.ecs.activity.api.IActivityTargetNumberService;
import com.ai.ecs.activity.entity.ActivityGoodsPara;
import com.ai.ecs.activity.entity.ActivityTargetNumber;
import com.ai.ecs.activity.entity.TargetNumberUtil;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.VisualActivityInfo;
import com.ai.ecs.ecm.mall.wap.modules.market.KillRedisManage;
import com.ai.ecs.ecm.mall.wap.platform.utils.BeanCopierUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.HttpClientUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.RealNameMsDesPlus;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.contract.entity.ConRegularCheckIntfCondition;
import com.ai.ecs.ecsite.modules.contract.entity.ConSuperStarCheckIntfCondition;
import com.ai.ecs.ecsite.modules.contract.entity.ContractSaleCheckSureCondition;
import com.ai.ecs.ecsite.modules.contract.entity.HTContractSaleCheckCondition;
import com.ai.ecs.ecsite.modules.contract.entity.TermCheckByModelCondition;
import com.ai.ecs.ecsite.modules.contract.service.ContractSaleService;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.api.IGoodsSkuService;
import com.ai.ecs.goods.api.IGoodsStaticService;
import com.ai.ecs.goods.api.inter.IStockInterfaceLogService;
import com.ai.ecs.goods.entity.goods.*;
import com.ai.ecs.member.api.IMemberMarketSignInService;
import com.ai.ecs.member.api.IWtjkzqUsrDetailService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.TfMemberMarketSignIn;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.merchant.entity.CompanyInfo;
import com.ai.ecs.merchant.shop.pojo.Shop;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.sales.api.ICouponService;
import com.ai.ecs.sales.api.IMarketService;
import com.ai.ecs.sales.entity.CouponInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.ai.ecs.ecm.mall.wap.common.CommonParams.HN_TERMINAL_MERCHANT;
import static com.ai.ecs.ecm.mall.wap.common.CommonParams.ORDER_TYPE_MERCHANT;
import static com.ai.ecs.exception.ExceptionUtils.splitException;

/**
 * Created by wangqiang11 on 2016/4/29.
 */
@Service
public class GoodsService
{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IMarketService marketService;
    @Autowired
    ICouponService couponService;
    @Autowired
    IGoodsStaticService goodsStaticService;
    @Autowired
    ICompanyAcctService companyAcctService;
    @Autowired
    IGoodsManageService goodsManageService;
    @Autowired
    IGoodsSkuService goodsSkuService;
    @Autowired
    IMemberMarketSignInService signInService;
    @Autowired
    IWtjkzqUsrDetailService wtjkzqUsrDetailService;
    @Autowired
    ContractSaleService contractSaleService;
    @Autowired
    IStockInterfaceLogService stockInterfaceLogService;    
    
 	@Autowired
 	IActivityTargetNumberService activityTargetNumberService;
 	
 	@Autowired
 	BasicInfoQryModifyService  basicInfoQryModifyService;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private KillRedisManage killRedisManage;

    @Autowired
    NetNumServerService netNumServerService;

    /**
     * 从购物车中获取不重复的店铺集合
     * @return
     */
    public Set<Shop> getShopSetFromGoodsCar(List<TfUserGoodsCar> userGoodsCarList){
        Set<Shop> shopSet = Sets.newHashSet();
        for (TfUserGoodsCar car : userGoodsCarList) {
            Shop shop = new Shop();
            shop.setShopId(car.getShopId());
            shop.setShopName(car.getShopName());
            shopSet.add(shop);
        }

        return shopSet;
    }

    /**
     * 查询商品sku信息
     * @param goodsCar
     * @return
     */
    public GoodsWater queryGoodsSkuInfo(TfUserGoodsCar goodsCar){
        Long goodsId = goodsCar.getGoodsId();
        Long goodsSkuId = goodsCar.getGoodsSkuId();
        String goodsType = String.valueOf(goodsCar.getGoodsType());
        Map<String,String> params = this.parseParams(goodsCar.getGoodsStandardAttrId());

        GoodsWater goodsWater = goodsStaticService.getGoodsWater(goodsId,goodsSkuId,CommonParams.CHANNEL_CODE, goodsType,params);
        logger.info("goodsWater:"+JSONObject.toJSONString(goodsWater));
        Long marketId = goodsCar.getMarketId();
        if(marketId == null ){
            return goodsWater;
        }


        String key = "ACTIVITY_DATA_" + marketId;
        String marketInfoStr =  jedisCluster.get(key);
        VisualActivityInfo marketInfo = JSONObject.parseObject(marketInfoStr, VisualActivityInfo.class);
        goodsWater.setGoodsSalePrice(marketInfo.getMarketPrice());//设置活动价格
        goodsWater.setStockNum(marketInfo.getActivityStock());
        goodsWater.setMarketId(Long.parseLong(marketInfo.getActivityId()));
        goodsWater.setGoodsMaxNum(marketInfo.getUserBuyLimit());
        goodsWater.setGoodsSkuId(Long.parseLong(marketInfo.getGoodsSkuId()));


        /*String marketInfoJSON = String.valueOf(JedisClusterUtils.getObject("ACTIVITY_DATA_INFO" + marketId));
        MarketInfo marketInfo = JSONObject.parseObject(marketInfoJSON,MarketInfo.class);
        if(marketInfo == null){
            marketInfo = new MarketInfo();
            marketInfo.setMarketId(marketId);
            marketInfo = marketService.queryMarket(marketInfo);
            JedisClusterUtils.setObject("ACTIVITY_DATA_INFO" + marketId,JSONObject.toJSONString(marketInfo),86400);
            JedisClusterUtils.expires("ACTIVITY_DATA_INFO" + marketId,60*60);
        }
        *//*MarketInfo  marketInfo = new MarketInfo();
        marketInfo.setMarketId(marketId);
        marketInfo = marketService.queryMarket(marketInfo);*//*
        logger.info("marketInfo:"+JSONObject.toJSONString(marketInfo));
        for (MarketGoodsSkuPrice marketGoodsSkuPrice :marketInfo.getMarketGoodsSkuPriceList()) {
            if (marketGoodsSkuPrice.getGoodsSkuId().equals(goodsSkuId)) {
                Long goodsSkuMarketPrice = 0L;
                if(marketInfo.getMarketTypeId() == 3){//预售商品
                    goodsSkuMarketPrice = marketGoodsSkuPrice.getFirstPrice();
                    if(goodsSkuMarketPrice == null || goodsSkuMarketPrice == 0L){
                        goodsSkuMarketPrice = marketGoodsSkuPrice.getGoodsSkuMarketPrice();
                    }
                }else{
                    goodsSkuMarketPrice = marketGoodsSkuPrice.getGoodsSkuMarketPrice();
                }

                Long goodsSkuMarketStock = marketGoodsSkuPrice.getGoodsSkuMarketStock();
                goodsWater.setGoodsSalePrice(goodsSkuMarketPrice);//设置活动价格
                goodsWater.setStockNum(goodsSkuMarketStock);
                goodsWater.setMarketId(marketInfo.getMarketId());
                goodsWater.setGoodsMaxNum(marketInfo.getGoodsMaxNum());
                goodsWater.setGoodsSkuId(marketGoodsSkuPrice.getGoodsSkuId());
                break;
            }
        }*/

        return goodsWater;
    }

    /**
     * 更新会员已抢购数量、已抢购数量
     * @param cars
     * @param memberId
     */
    public void updateMarketBuyNum(List<TfUserGoodsCar> cars , Long memberId){
        for (TfUserGoodsCar car : cars) {
            Long marketId = car.getMarketId();
            if(marketId == null){
                continue;
            }

            String memberBuykey = "ACTIVITY_GOODS_USER_" + marketId + memberId;
            String marketBuykey = "ACTIVITY_STOCK" + marketId;
            String memberBuyNum = JedisClusterUtils.get(memberBuykey);
            String marketBuyNum = JedisClusterUtils.get(marketBuykey);

            if(StringUtils.isBlank(memberBuyNum)){
                JedisClusterUtils.set(memberBuykey , "1" , 1800);
                JedisClusterUtils.expires(memberBuykey,60*60*24*7);
            }else{
                JedisClusterUtils.set(memberBuykey , String.valueOf(Integer.valueOf(marketBuyNum) + 1) , 1800);
                JedisClusterUtils.expires(memberBuykey,60*60*24*7);
            }

            if(StringUtils.isBlank(marketBuyNum)){
                JedisClusterUtils.set(marketBuykey , "1" , 1800);
                JedisClusterUtils.expires(memberBuykey,60*60*24*7);
            }else{
                JedisClusterUtils.set(marketBuykey , String.valueOf(Integer.valueOf(marketBuyNum) + 1) , 1800);
                JedisClusterUtils.expires(memberBuykey,60*60*24*7);
            }

        }
    }

    /**
     * 获取订单中所有商品名称
     * @param orderSubList
     * @return
     */
    public static String getProductName(List<TfOrderSub> orderSubList){
        List<String> goodsNameList = Lists.newArrayList();
        for (TfOrderSub orderSub : orderSubList) {
            List<TfOrderSubDetail> detailList = orderSub.getDetailList();
            for (TfOrderSubDetail detail : detailList) {
                goodsNameList.add(detail.getGoodsName());
            }
        }

        String productName = StringUtils.join(goodsNameList,",");
        if(productName.length() > 20){
            productName = StringUtils.substring(productName,0,20) + "...";
        }

        return productName;
    }

    /**
     * 转换商品属性格式
     * 字符串转换成Map
     * @param params
     * @return
     */
    public static Map<String, String> parseParams(String params) {
        if (params == null || "".equals(params)) {
            return null;
        }
        Map<String, String> paramsMap = new HashMap<String, String>();
        String[] paramsArr = params.split("&");
        for (String paramItem : paramsArr) {
            String[] item = paramItem.split("=");
            paramsMap.put(item[0], item[1]);
        }
        return paramsMap;
    }

    /**
     * 获取分润规则
     * @param shopList
     * @param orderSubList
     * @param couponInfoList
     * @param payPlatform
     * @return
     * @throws Exception
     */
    public String getShRule(List<Shop> shopList,List<TfOrderSub> orderSubList,List<CouponInfo> couponInfoList,String payPlatform) throws Exception{
        List<String> shRuleList = Lists.newArrayList();
        for(Shop shop : shopList){
            long shopPayAmount = 0;
            String shopId = String.valueOf(shop.getShopId());
            TfOrderDetailContract contract = null;
            Long goodsId = null;
            Integer orderTypeId = null;
            for (TfOrderSub orderSub : orderSubList){
                if(String.valueOf(orderSub.getShopId()).equals(shopId)){
                    orderTypeId = orderSub.getOrderTypeId();

                    if(orderTypeId == OrderConstant.TYPE_BARE || orderTypeId == OrderConstant.TYPE_PARTS || orderTypeId == OrderConstant.TYPE_CONTRACT
                            || orderTypeId == OrderConstant.TYPE_VALUECARD_PHONEFEE || orderTypeId == OrderConstant.TYPE_VALUECARD_FLOWFEE) {
                        //根据goodsSkuId获取goodsId
                        Map<String,Object> skuParamMap = Maps.newHashMap();
                        skuParamMap.put("goodsSkuId",orderSub.getDetailList().get(0).getGoodsSkuId());

                        List<TfGoodsSku> queryGoodsSkuInfoByCds = goodsManageService.queryGoodsSkuInfoByCds(skuParamMap);
                        if (CollectionUtils.isNotEmpty(queryGoodsSkuInfoByCds)) {
                            goodsId = queryGoodsSkuInfoByCds.get(0).getGoodsId();
                        }
                        shopPayAmount += orderSub.getOrderSubPayAmount();
                        contract = orderSub.getDetailList().get(0).getOrderDetailContract();
                    }

                    //号卡订单
                    if(orderTypeId == OrderConstant.TYPE_SIM){
                        shopPayAmount += orderSub.getOrderSubPayAmount();
                    }

                }

            }

            //扣除优惠券金额
            if(couponInfoList != null && couponInfoList.size() > 0){
                for (CouponInfo couponInfo : couponInfoList){
                    if(String.valueOf(couponInfo.getShopId()).equals(shopId)){
                        shopPayAmount -= couponInfo.getCouponValue();
                    }
                }
            }

            //号卡订单
            if (orderTypeId == OrderConstant.TYPE_SIM) {
                String cityCode = orderSubList.get(0).getDetailList().get(0).getOrderDetailSim().getCityCode();
                CompanyInfo companyInfo = new CompanyInfo();
                companyInfo.setChannelCityCode(cityCode);
                companyInfo.setCompanyTypeId((short) 7);
                CompanyAcctInfo eparchyAcctInfo = companyAcctService.getAcctByChannelAndType(companyInfo, CommonParams.PAY_PLATFORM.get(payPlatform));
                if (eparchyAcctInfo == null) {
                    logger.error(String.format("未配置账户，地市编码(%s)", cityCode));
                    throw new RuntimeException(String.format("该店铺暂不支持该支付方式，推荐您使用和包完成支付，地市编码(%s)", cityCode));
                }

                shRuleList.add(eparchyAcctInfo.getAccountNum() + "^" + shopPayAmount + "^备注");
            }

            //合约机订单
            if(orderTypeId == OrderConstant.TYPE_CONTRACT){
                //收入型合约
                if("25".equals(contract.getProductTypeCode())|| "10".equals(contract.getProductTypeCode()) || "24".equals(contract.getProductTypeCode())){
                    //查询供应商账户
                    if(!"1".equals(shopId)){
                        //获取商户分润账户
                        Short type = CommonParams.PAY_PLATFORM.get(payPlatform);
                        CompanyAcctInfo companyAcctInfo = companyAcctService.getAcctByShopIdAndType(shop.getShopId(),type);
                        if(companyAcctInfo == null){
                            logger.error(String.format("%s未配置账户", shop.getShopName()));
                            throw new Exception("该店铺暂不支持该支付方式，推荐您使用和包完成支付。");
                        }
                        shRuleList.add(companyAcctInfo.getAccountNum()+ "^" + shopPayAmount + "^备注");
                    }else {
                        Map<String, Object> paramMap = Maps.newHashMap();
                        paramMap.put("goodsId", goodsId);
                        List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoByCds(paramMap);
                        Long supplierShopId = goodsList.get(0).getShopGoodsRef().getSupplierShopId();
                        Short type = CommonParams.PAY_PLATFORM.get(payPlatform);
                        CompanyAcctInfo supplierAcctInfo = companyAcctService.getAcctByShopIdAndType(supplierShopId, type);
                        if (supplierAcctInfo == null) {
                            logger.error(String.format("供货商%s未配置账户", supplierShopId));
                            throw new Exception("该店铺暂不支持该支付方式，推荐您使用和包完成支付。");
                        }

                        //分给平台供应商
                        String supplierShRule = supplierAcctInfo.getAccountNum() + "^" + shopPayAmount + "^备注";
                        shRuleList.add(supplierShRule);
                    }
                }
            }
            
          //宽带订单
            if(orderTypeId == OrderConstant.TYPE_BROADBAND || orderTypeId == OrderConstant.TYPE_BROADBAND_CONTINUE){
            	List<TfOrderSubDetailBusiParam> busiParamList = orderSubList.get(0).getDetailList().get(0).getOrderSubDetailBusiParams();
            	String eparchyCode = "";
            	for(TfOrderSubDetailBusiParam orderSubDetailBusiParam : busiParamList){
            		if("eparchyCode".equals(orderSubDetailBusiParam.getSkuBusiParamName())){
            			eparchyCode = orderSubDetailBusiParam.getSkuBusiParamValue();
            			break;
            		}
            	}
            	 /*分润规则*/
    	        CompanyInfo companyInfo = new CompanyInfo();
    	        companyInfo.setChannelCityCode(eparchyCode);
    	        companyInfo.setCompanyTypeId((short)7);
                shopPayAmount = orderSubList.get(0).getOrderSubPayAmount();
    	        CompanyAcctInfo eparchyAcctInfo = companyAcctService.getAcctByChannelAndType(companyInfo, CommonParams.PAY_PLATFORM.get(payPlatform));
                if (eparchyAcctInfo != null) {
                    String shRule = eparchyAcctInfo.getAccountNum()+"^"+shopPayAmount+"^宽带新装";
                    logger.info("单宽带新装,分润规则:"+shRule);
                    shRuleList.add(shRule);
                }
            }
            

            //店铺为移动平台店铺且订单类型为裸机或配件
            if("1".equals(shopId) && (orderTypeId == OrderConstant.TYPE_BARE || orderTypeId == OrderConstant.TYPE_PARTS
                    || orderTypeId == OrderConstant.TYPE_VALUECARD_PHONEFEE || orderTypeId == OrderConstant.TYPE_VALUECARD_FLOWFEE)){
                Map<String,Object> paramMap = Maps.newHashMap();
                paramMap.put("goodsId",goodsId);
                List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoByCds(paramMap);
                Long supplierShopId = goodsList.get(0).getShopGoodsRef().getSupplierShopId ();

                shop.setShopId(supplierShopId);

                //获取供货商商户分润账户
                Short type = CommonParams.PAY_PLATFORM.get(payPlatform);
                CompanyAcctInfo companyAcctInfo = companyAcctService.getAcctByShopIdAndType(shop.getShopId(), type);
                if(companyAcctInfo == null){
                    logger.error(String.format("供货商%s未配置账户", supplierShopId));
                    throw new Exception("该店铺暂不支持该支付方式，推荐您使用和包完成支付。");
                }
                shRuleList.add(companyAcctInfo.getAccountNum()+ "^" + shopPayAmount + "^备注");
            }

            if(!"1".equals(shopId) && (orderTypeId == OrderConstant.TYPE_BARE || orderTypeId == OrderConstant.TYPE_PARTS
                    || orderTypeId == OrderConstant.TYPE_VALUECARD_PHONEFEE || orderTypeId == OrderConstant.TYPE_VALUECARD_FLOWFEE)){
                //获取供货商商户分润账户
                Short type = CommonParams.PAY_PLATFORM.get(payPlatform);
                CompanyAcctInfo companyAcctInfo = companyAcctService.getAcctByShopIdAndType(shop.getShopId(),type);
                if(companyAcctInfo == null){
                    logger.error(String.format("%s未配置账户", shop.getShopName()));
                    throw new Exception("该店铺暂不支持该支付方式，推荐您使用和包完成支付。");
                }
                shRuleList.add(companyAcctInfo.getAccountNum()+ "^" + shopPayAmount + "^备注");
            }

        }

        String shRule = StringUtils.join(shRuleList, "|");

        return shRule;
    }

    /**
     * 获取订单支付金额（扣除优惠券金额后）
     * @return
     */
    public String getOrderPayAmount(HttpServletRequest request,List<CouponInfo> couponInfoList,List<TfOrderSub> orderSubList,TfOrder order)throws Exception{
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        Long orderPayAmount = order.getOrderPayAmount();
        if(couponInfoList != null && couponInfoList.size() > 0){
            for (CouponInfo couponInfo : couponInfoList){
                for (TfOrderSub orderSub : orderSubList){
                    if(orderSub.getShopId() == couponInfo.getShopId()){
                        couponInfo.setOrderSubNo(orderSub.getOrderSubNo());
                    }
                }

                couponInfo.setMemberId(memberId);
                couponInfo.setCouponStatusId(5);
                couponInfo.setOrderId(String.valueOf(order.getOrderId()));
            }
            couponService.frozenCouponInfoMember(couponInfoList);

            //扣减优惠券金额
            for (CouponInfo couponInfo : couponInfoList){
                orderPayAmount -= couponInfo.getCouponValue();
            }
        }
        return String.valueOf(orderPayAmount);
    }

    /**
     * 商品回库
     * @param cars
     */
    public void returnGoodsStock(List<TfUserGoodsCar> cars){
        if(cars == null){
            return;
        }

        try {
            for (TfUserGoodsCar car : cars) {
                GoodsWater goodsWater = this.queryGoodsSkuInfo(car);
                Long goodsSkuId = goodsWater.getGoodsSkuId();
                int goodsBuyNum = car.getGoodsBuyNum().intValue();

               // goodsSkuService.updateStock(goodsSkuId,-goodsBuyNum);//更新商品库存
                //集体回库 根据店铺不一样 回库存方式不一样 如果对于营销活动 也不一样
                if (car.getMarketId()!=null){
                    marketService.updateMarketStock(car.getMarketId(),
                            goodsSkuId, -goodsBuyNum);// 更新活动商品库存
                    /**记录日志*/
                    TfStockUpdateLog tfStockUpdateLog=  new TfStockUpdateLog();
                    tfStockUpdateLog.setOperTime(new Date());
                    tfStockUpdateLog.setGoodsId(car.getGoodsId());
                    tfStockUpdateLog.setOperRemark("提交订单失败，活动商品库存回库");
                    tfStockUpdateLog.setOperUserName("系统事务回滚");
                    tfStockUpdateLog.setGoodsSkuId(car.getGoodsSkuId());
                    tfStockUpdateLog.setGoodsNum(-car.getGoodsBuyNum());
                    tfStockUpdateLog.setUpdateType("6");
                    stockInterfaceLogService.saveStockUpdateLog(tfStockUpdateLog);
                }else{
                    if (car.getShopId()!=null&&car.getShopId()==1L){//移动自营店
                        goodsSkuService.updateOccupyStock(goodsSkuId, -goodsBuyNum);//更新商品的占用库存
                    }else{
                        goodsSkuService.updateStock(goodsSkuId, -goodsBuyNum);//更新商品库存
                    }
                    /**记录日志*/
                    TfStockUpdateLog tfStockUpdateLog=  new TfStockUpdateLog();
                    tfStockUpdateLog.setOperTime(new Date());
                    tfStockUpdateLog.setGoodsId(car.getGoodsId());
                    tfStockUpdateLog.setOperRemark("提交订单失败，普通商品库存回库");
                    tfStockUpdateLog.setOperUserName("系统事务回滚");
                    tfStockUpdateLog.setGoodsSkuId(car.getGoodsSkuId());
                    tfStockUpdateLog.setGoodsNum(-car.getGoodsBuyNum());
                    tfStockUpdateLog.setUpdateType("6");
                    stockInterfaceLogService.saveStockUpdateLog(tfStockUpdateLog);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品占用数量，批量回库
     *
     * @param cars
     */
    public void returnGoodsOccupyStock(List<TfUserGoodsCar> cars,String loginName) {
        if (cars == null) {
            return;
        }

        try {
            for (TfUserGoodsCar car : cars) {
                GoodsWater goodsWater = this.queryGoodsSkuInfo(car);
                Long goodsSkuId = goodsWater.getGoodsSkuId();
                int goodsBuyNum = car.getGoodsBuyNum().intValue();

                goodsSkuService.updateOccupyStock(goodsSkuId, -goodsBuyNum);//更新商品的占用库存
                /**记录日志*/
                TfStockUpdateLog tfStockUpdateLog=  new TfStockUpdateLog();
                tfStockUpdateLog.setOperTime(new Date());
                tfStockUpdateLog.setGoodsId(car.getGoodsId());
                tfStockUpdateLog.setOperRemark("提交订单失败虚拟库存回库");
                tfStockUpdateLog.setOperUserName(loginName);
                tfStockUpdateLog.setGoodsSkuId(car.getGoodsSkuId());
                tfStockUpdateLog.setGoodsNum(-car.getGoodsBuyNum());
                tfStockUpdateLog.setUpdateType("6");
                stockInterfaceLogService.saveStockUpdateLog(tfStockUpdateLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取选中的购物车商品
     * @param cars
     * @return
     */
    public List<TfUserGoodsCar> getCheckedCars(List<TfUserGoodsCar> cars){
        Iterator<TfUserGoodsCar> iter = cars.iterator();
        while (iter.hasNext()){
            TfUserGoodsCar car = iter.next();
            if(!"Y".equals(car.getIsChecked())){
                iter.remove();
            }
        }
        return cars;
    }

    /**
     * 实名认证 v1.0
     */
    public boolean realityVerify(TfOrderDetailSim detailSim)throws Exception{
        JSONObject reqParamJSON = new JSONObject();
        reqParamJSON.put("busiCode","REALITY_VERIFY");
        reqParamJSON.put("sourceCode","731001");//请求省编码，默认湖南省
        reqParamJSON.put("targetCode","1085");//落地省编码
        reqParamJSON.put("version","1.0");//报文版本
        reqParamJSON.put("transactionID",this.getTransactionID());//全网唯一操作流水号

        JSONObject plaintextJSON = new JSONObject();
        //测试
        /*plaintextJSON.put("userName","huNwangT");//用户名
        plaintextJSON.put("password","2w3QA9!");//密码*/
        //生产
        plaintextJSON.put("userName","wcIL$y");//用户名
        plaintextJSON.put("password","mBA%VvsJq");//密码
        plaintextJSON.put("billId",detailSim.getPhone());//服务号码
        plaintextJSON.put("channelId",CommonParams.CHANNEL_CODE);//渠道
        plaintextJSON.put("busiType","1");//业务类型，1：新客户入网
        plaintextJSON.put("custName",detailSim.getRegName());//客户名称
        plaintextJSON.put("custCertNo",detailSim.getPsptId());//客户身份证号码
        plaintextJSON.put("gender","男".equals(detailSim.getGender())?"1":"0");//性别，传1：代表男，传0：代表女
        plaintextJSON.put("sourceType","2");//开户对应的身份证信息来源类型，1：二代证读卡器； 2：实名制系统对接的工单；3：其它
        logger.info(plaintextJSON.toJSONString());

        RealNameMsDesPlus desPlus = new RealNameMsDesPlus();
        String plaintext = desPlus.encrypt(plaintextJSON.toJSONString());
        reqParamJSON.put("reqInfo",plaintext);
        logger.info(reqParamJSON.toJSONString());

        String reqUrl = "http://172.168.20.82:30209/crmEncryptEngine/servlet/ServletForHttp";

        Map<String,String[]> reqParam = Maps.newHashMap();
        reqParam.put("reqParam",new String[]{reqParamJSON.toJSONString()});
        String content = HttpClientUtils.doPostAndGetString(reqUrl,reqParam);
        logger.info(content);

        JSONObject contentJSON = JSONObject.parseObject(content);
        String returnCode = contentJSON.getString("returnCode");

        if("0000".equals(returnCode)){
            String verifyResult = contentJSON.getString("verifyResult");
            String returnMessage = contentJSON.getString("returnMessage");
            if("0".equals(verifyResult)){
                return true;
            }else if("1".equals(verifyResult)){
                throw new Exception("实名认证结果：身份证信息不一致；错误信息：" + returnMessage);
            }else if("2".equals(verifyResult)){
                throw new Exception("实名认证结果：身份证信息不存在；错误信息：" + returnMessage);
            }
        }else if("1001".equals(returnCode)){
            throw new Exception("调用实名认证接口异常：操作失败");
        }else if("2999".equals(returnCode)){
            throw new Exception("调用实名认证接口异常：其它异常");
        }

        return false;
    }

    /**
     * 获取流水号
     * @return
     */
    public String getTransactionID(){
        String time = DateUtils.getDate("yyyyMMddhh24mmss");
        String transactionID = "731" + time;

        String second = time.substring(12);
        String secondCache = JedisClusterUtils.get("realityVerify-second");
        if(StringUtils.isBlank(secondCache)){
            JedisClusterUtils.set("realityVerify-second",second,86400);
            JedisClusterUtils.expires("realityVerify-second",60*60*24*7);
        }

        if(second.equals(secondCache)){
            String serialNo = "1";
            String serialNoCache = JedisClusterUtils.get("realityVerify-serialNo");

            if(StringUtils.isNotBlank(serialNoCache)){
                serialNo = String.valueOf(Integer.parseInt(serialNoCache) + 1);
            }

            String number = String.valueOf(Integer.parseInt(serialNo));
            transactionID = transactionID + String.format("%06d", Integer.valueOf(number));

            JedisClusterUtils.set("realityVerify-serialNo",number,86400);
            JedisClusterUtils.expires("realityVerify-serialNo",60*60*24*7);
        }else{
            transactionID = transactionID + "000001";
            JedisClusterUtils.set("realityVerify-serialNo","1",86400);
            JedisClusterUtils.expires("realityVerify-serialNo",60*60*24*7);
        }

        return transactionID;
    }

    /**
     * 减少秒杀机会
     * @param memberId
     */
    public void decreaseSecKillChance(Long memberId , List<TfUserGoodsCar> cars){
        for (TfUserGoodsCar car : cars) {
            Long marketId = car.getMarketId();
            if(marketId == null){
                continue;
            }

            TfMemberMarketSignIn signIn = new TfMemberMarketSignIn();
            signIn.setMemberId(memberId);
            signIn.setMarketId(marketId);
            signInService.decreaseSecKillChance(signIn);
        }
    }

    /**
     * 更新活动库存
     * @param marketId
     * @param goodsSkuId
     * @param goodsBuyNum
     */
    public boolean updateMarketStock(Long marketId,Long goodsSkuId,Long goodsBuyNum){
        //更新活动商品库存(数据库)
        boolean result=false;
        logger.info("updateMarketStock:[{}],[{}],[{}]",marketId,goodsSkuId,goodsBuyNum.intValue());
        if(goodsSkuId != null){
            result= marketService.updateMarketStock(marketId,goodsSkuId,goodsBuyNum.intValue());
            logger.info("result:[{}]",result);
        }

        if (result) {  // 如果库存修改成功， 同时修改redis中的库存数量
            String keyLock = "updateMarketStock_" + marketId;
            // 锁定修改redis库存， 15秒内尝试锁定
            boolean stockLockFlag = killRedisManage.tryLock(keyLock, 15, TimeUnit.SECONDS);
            try {
                if (stockLockFlag) {
                    String key = "ACTIVITY_DATA_" + marketId;
                    String marketInfoStr =  jedisCluster.get(key);
                    VisualActivityInfo marketInfo = JSONObject.parseObject(marketInfoStr, VisualActivityInfo.class);
                    marketInfo.setActivityStock(marketInfo.getActivityStock() - 1);
                    Long leftExpire = jedisCluster.ttl(key);  // 获取还有多少秒过期
                    // 修改库存后， 重新放回到redis中
                    jedisCluster.setex(key, leftExpire.intValue(), JSONObject.toJSONString(marketInfo));
                } else {
                    logger.error("不能获取redis锁， 导致redis中的库存与数据库中库存不一致。");
                }
            } catch (Exception e) {
                logger.error("修改redis中的库存失败。", e);
            } finally {
                if(stockLockFlag) {
                    killRedisManage.unLock(keyLock);
                }
            }
        }
        //更新活动商品库存(redis)


        /*if(marketInfo!=null){
            List<MarketGoodsSkuPrice> marketGoodsSkuPriceList = marketInfo.getMarketGoodsSkuPriceList();
            for(MarketGoodsSkuPrice marketGoodsSkuPrice : marketGoodsSkuPriceList){
                if(goodsSkuId.equals(marketGoodsSkuPrice.getGoodsSkuId())){
                    if(marketGoodsSkuPrice.getGoodsSkuMarketStock()!=null){
                        //判断是否还有存库
                        if(marketGoodsSkuPrice.getGoodsSkuMarketStock()>0) {
                            marketGoodsSkuPrice.setGoodsSkuMarketStock(marketGoodsSkuPrice.getGoodsSkuMarketStock() - goodsBuyNum);
                            marketGoodsSkuPrice.setSaleNum(marketGoodsSkuPrice.getSaleNum() + goodsBuyNum);
                        }
                    }
                    if(marketInfo.getMarketStock()!=null){
                        marketInfo.setMarketStock(marketInfo.getMarketStock() - goodsBuyNum);
                    }

                }
            }
            JedisClusterUtils.del(preStr+marketId);
            JedisClusterUtils.setObject(preStr+marketId, JSONObject.toJSONString(marketInfo), 86400);
            JedisClusterUtils.expires(preStr+marketId,86400*7);
        }*/
        return result;
    }

    /**
     * 判断是否三高客户
     * @param memberLogin
     * @return
     */
    public boolean is3HighUser(MemberLogin memberLogin){
        Integer memberTypeId = memberLogin.getMemberTypeId();
        String memberLoginName = memberLogin.getMemberLogingName();
        logger.info("memberTypeId:"+memberTypeId);
        logger.info("checkJkzqUsr:"+wtjkzqUsrDetailService.checkJkzqUsr(memberLoginName));
        return wtjkzqUsrDetailService.checkJkzqUsr(memberLoginName) && memberTypeId ==2;
    }

    /**
     * 获取终端价格
     * 活动时间持续到2016年9月底
     * @param terminalSalePrice
     * @throws Exception
     */
    public Long getTerminalSalePrice(Long terminalSalePrice)throws Exception{
        if(terminalSalePrice >= 10000 && terminalSalePrice <= 100000){
            terminalSalePrice -= 10000;
        }else if(terminalSalePrice > 100000 && terminalSalePrice <= 200000){
            terminalSalePrice -= 15000;
        }else if(terminalSalePrice > 200000 && terminalSalePrice <= 300000){
            terminalSalePrice -= 20000;
        }else if(terminalSalePrice > 300000){
            terminalSalePrice -= 30000;
        }

        logger.info("terminalSalePrice:"+terminalSalePrice);

        return terminalSalePrice;
    }

    /**
     * 判断是否移动定制版或支持VOTEL功能的终端
     * @param goodsId
     * @return
     */
    public boolean isCustomizeOrVotelTerminal(Long goodsId){
        //判断是否移动定制版的终端
        Map<String,Object> mobileCustomizeParam = Maps.newHashMap();
        mobileCustomizeParam.put("goodsId",goodsId);
        mobileCustomizeParam.put("goodsAttrName","移动定制版");
        mobileCustomizeParam.put("goodsAttrValue","是");
        List<TfGoodsAttr> mobileCustomizeList = goodsManageService.queryGoodsAttrByCds(mobileCustomizeParam);
        boolean isMobileCustomize = !Collections3.isEmpty(mobileCustomizeList);

        //判断是否支持VOTEL功能的终端
        Map<String,Object> votelParam = Maps.newHashMap();
        votelParam.put("goodsId",goodsId);
        votelParam.put("goodsAttrName","VOTEL功能");
        votelParam.put("goodsAttrValue","支持");
        List<TfGoodsAttr> votelList = goodsManageService.queryGoodsAttrByCds(votelParam);
        boolean isVotel = !Collections3.isEmpty(votelList);

        logger.info("isMobileCustomize:" + isMobileCustomize);
        logger.info("isVotel:" + isVotel);

        return isMobileCustomize || isVotel;
    }

    /**
     * 判断某个日期是否在系统当前日期之后
     * @param date
     * @return
     */
    public boolean isAfterCurrentDate(String date)throws Exception{
        String currentDate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        int result = DateUtils.dataCompare(date,currentDate);
        logger.info("isBeforeCurrentDate:"+(result == 1));
        return result == 1;
    }

    /**
     * 根据订单信息获取内部商户号
     * @param orderSubList
     * @return
     */
    public JSONObject getInnerMerchant(List<TfOrderSub> orderSubList)throws Exception{
        for (TfOrderSub orderSub : orderSubList) {
            List<TfOrderSubDetail> detailList = orderSub.getDetailList();
            for (TfOrderSubDetail orderSubDetail : detailList) {
                Long goodsId = orderSubDetail.getGoodsId();
                boolean isCustomizeOrVotelTerminal = this.isCustomizeOrVotelTerminal(goodsId);
                boolean isAfterCurrentDate = this.isAfterCurrentDate("2016-10-31 00:00:00");

                if(isCustomizeOrVotelTerminal && isAfterCurrentDate){
                    Long goodsSkuPrice = orderSubDetail.getGoodsSkuPrice();
                    if(goodsSkuPrice > 80000){
                        return HN_TERMINAL_MERCHANT;
                    }
                }
            }
        }

        Integer orderTypeId = orderSubList.get(0).getOrderTypeId();
        return ORDER_TYPE_MERCHANT.get(orderTypeId);
    }

    /**
     * 获取支付接口需要的备注信息
     *
     * @param orderSubList
     * @return
     */
    public String getReserved1(List<TfOrderSub> orderSubList) {
        Long orderSubPayAmount = 0L;
        for (TfOrderSub orderSub : orderSubList) {
            orderSubPayAmount += (orderSub.getOrderSubPayAmount() / 100);
        }

        String reserved1 = orderSubPayAmount + "元";

        Integer orderTypeId = orderSubList.get(0).getOrderTypeId();
        switch (orderTypeId.intValue()) {
            case OrderConstant.TYPE_SIM:
                reserved1 = "号码预存".concat(reserved1);
                break;

            case OrderConstant.TYPE_BARE:
            case OrderConstant.TYPE_CONTRACT:
                reserved1 = "终端销售".concat(reserved1);
                break;

            case OrderConstant.TYPE_PARTS:
                reserved1 = "配件销售".concat(reserved1);
                break;
        }

        return reserved1;
    }

    /**
     * 合约计划用户资格查询
     * @param contract
     * @return
     */
    public void contractSaleCheck(TfOrderDetailContract contract)throws Exception{
        Map checkMap = null;
        try {
            //被WAP登录拦截后此参数值后会多出一个半角冒号，原因不明，暂时做去除处理
            String contractId = contract.getContractId().replaceAll(":","");
            contract.setContractId(contractId);

            HTContractSaleCheckCondition condition = new HTContractSaleCheckCondition();
            BeanCopierUtils.copyProperties(contract,condition);

            List<Map<String,Object>> productStrList = Lists.newArrayList();
            Map<String,Object> productStrMap = Maps.newHashMap();
            productStrMap.put("PRODUCT_ID",contract.getProductId());
            productStrMap.put("ELEMENT_ID",contract.getElementId());
            productStrList.add(productStrMap);
            condition.setProductStr(productStrList);
            condition.setInEparchyCode(contract.getEparchyCode());

            JSONObject kfJSON = CommonParams.EPARCH_KF.get(condition.getInEparchyCode());
            condition.setInStaffId(kfJSON.getString("inStaffId"));
            condition.setInDepartId(kfJSON.getString("inDepartId"));

            checkMap = contractSaleService.contractSaleCheck(condition);
            JSONObject checkJSON = JSONObject.parseArray(checkMap.get("result").toString()).getJSONObject(0);

            logger.info(checkJSON.toJSONString());

            String checkResultcode = checkJSON.getString("X_RESULTCODE");
            String checkResultInfo = checkJSON.getString("X_RESULTINFO");
            if(!"0".equals(checkResultcode)){
                throw new Exception( checkResultInfo);
            }
        } catch (Exception e) {
            logger.error("合约计划用户资格查询异常：",e);
            splitException(e, "合约办理资格校验返回："+net.sf.json.JSONObject.fromObject(checkMap).toString());
            throw new Exception("当前未查询到您的合约办理资格情况,请稍后再试!");
        }
    }
    
    
    /**
     * 明星机，99购机，特惠机校验
     * @param contract
     * @param activityGoodsPara 
     * @param sureNo 
     * @return
     */
    public void checkOtherContract(TfOrderDetailContract contract, ActivityGoodsPara activityGoodsPara, String sureNo)throws Exception{       
    	
    	//三户资料查询 
		String inTime = "";
		String inCityCode = "";
		try
		{
			 BasicInfoCondition bCondition = new BasicInfoCondition();
			 bCondition.setSerialNumber(contract.getSerialNumber());
			 bCondition.setxGetMode("0");
				Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(bCondition);		
				logger.info(JSON.toJSONString(userInfo,true)); 
				if(MapUtils.isNotEmpty(userInfo) && HNanConstant.SUCCESS.equals(userInfo.get("respCode"))){
					Map userBasicInfo = net.sf.json.JSONObject.fromObject(userInfo.get("result").toString().replace("[", "").replace("]", ""));
					  inTime =userBasicInfo.get("IN_DATE").toString(); // "2016-12-09 07:29:00";//
					  inCityCode =  userBasicInfo.get("CITY_CODE").toString(); //"A31A";//
				}else{ 
					  throw new Exception("没有查询到该手机号的用户信息!"+userInfo.get("respDesc")); 
				} 
		} catch (Exception e) {
			  throw new Exception("三户资料查询失败：" +e ); 
		} 
		
        	//99购机特惠
			if("99812781,99812782,99812783,99812784".contains(contract.getContractId())){
			  
				 //合约担保计划校验
				   ConRegularCheckIntfCondition conRegularCheckIntfCondition = new ConRegularCheckIntfCondition();
				   conRegularCheckIntfCondition.setSerialNumber(contract.getSerialNumber());
				   conRegularCheckIntfCondition.setImei("-1");
				   conRegularCheckIntfCondition.setContractId(contract.getContractId());
				   conRegularCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1());
				   conRegularCheckIntfCondition.setProductId(contract.getProductId());
				   conRegularCheckIntfCondition.setDiscntCode(contract.getElementId());		
				   try { 
				   Map checkMap = conRegularCheckIntf(conRegularCheckIntfCondition);
					if(checkMap.size()>0)  
					      throw new Exception("99购机用户资格查询失败：" + checkMap.get("resultInfo")); 					 	 
				} catch (Exception e) {
					  logger.error("99购机用户资格查询接口异常：",e);
			            throw new Exception("99购机用户资格查询接口异常：" + e);
				} 
			}
        	
			//明星机
			if("99812790,99812791,99812792,99812793,99812788,99812789".contains(contract.getContractId())){
				 //合约担保计划校验
				ConSuperStarCheckIntfCondition  conSuperStarCheckIntfCondition  = new ConSuperStarCheckIntfCondition();
				conSuperStarCheckIntfCondition.setSerialNumber(contract.getSerialNumber());
				conSuperStarCheckIntfCondition.setImei("-1");
				conSuperStarCheckIntfCondition.setContractId(contract.getContractId());
				conSuperStarCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1());				 
		            Map<String,Object> productStrMap = Maps.newHashMap();
		            productStrMap.put("PRODUCT_ID",contract.getProductId());
		            productStrMap.put("DISCNT_CODE",contract.getElementId());		       
		            conSuperStarCheckIntfCondition.setProductStr(productStrMap); 
				   try { 
				   Map checkMap = conSuperStarCheckIntf(conSuperStarCheckIntfCondition);
 					if(checkMap.size()>0)  
					      throw new Exception("超级明星机预受理接口查询失败：" + checkMap.get("resultInfo")); 		 
				} catch (Exception e) {
					  logger.error("超级明星机预受理接口异常：",e);
			            throw new Exception("超级明星机预受理接口异常：" + e);
				} 
			}
			
			//老客户担保（目标客户（仅限2/3G客户和新入网客户） + 担保号码+入网一年以上）
			if("99812787".equals(contract.getContractId())){			
				 //合约担保计划校验
				 ConRegularCheckIntfCondition conRegularCheckIntfCondition = new ConRegularCheckIntfCondition();
				   conRegularCheckIntfCondition.setSerialNumber(contract.getSerialNumber());
				   conRegularCheckIntfCondition.setImei("-1");
				   conRegularCheckIntfCondition.setContractId(contract.getContractId());
				   conRegularCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1());
				   conRegularCheckIntfCondition.setProductId(contract.getProductId());
				   conRegularCheckIntfCondition.setDiscntCode(contract.getElementId());		
				   conRegularCheckIntfCondition.setSureNo(sureNo);//session 中的担保号码
				   try { 
				   Map checkMap = conRegularCheckIntf(conRegularCheckIntfCondition);
					if(checkMap.size()>0)  
					      throw new Exception("99购机用户资格查询接口查询失败：" + checkMap.get("resultInfo")); 	
				} catch (Exception e) {
					  logger.error("99购机用户资格查询接口异常：",e);
			            throw new Exception("99购机用户资格查询接口异常：" + e);
				} 
			}
			
			//新入网（目标客户（仅限2/3G客户和新入网客户））
			if("99812786".equals(contract.getContractId())){
				 //合约担保计划校验				
				   ConRegularCheckIntfCondition conRegularCheckIntfCondition = new ConRegularCheckIntfCondition();
				   conRegularCheckIntfCondition.setSerialNumber(contract.getSerialNumber());
				   conRegularCheckIntfCondition.setImei("-1");
				   conRegularCheckIntfCondition.setContractId(contract.getContractId());
				   conRegularCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1());
				   conRegularCheckIntfCondition.setProductId(contract.getProductId());
				   conRegularCheckIntfCondition.setDiscntCode(contract.getElementId());		
				   try { 
				   Map checkMap = contractSaleService.conRegularCheckIntf(conRegularCheckIntfCondition);
				   if(checkMap.size()>0)  
					      throw new Exception("99购机用户资格查询接口查询失败：" + checkMap.get("resultInfo")); 	
				} catch (Exception e) {
					  logger.error("99购机用户资格查询接口异常：",e);
			            throw new Exception("99购机用户资格查询接口异常：" + e);
				} 
			} 
			
			 //合约担保计划校验				
			//终端机型校验						
			 JSONObject kfJSON = CommonParams.EPARCH_KF.get(contract.getEparchyCode());
			 TermCheckByModelCondition termCheckByModelCondition  = new TermCheckByModelCondition();
			 	termCheckByModelCondition.setInStaffId(kfJSON.getString("inStaffId"));
				termCheckByModelCondition.setInDepartId(kfJSON.getString("inDepartId")); 
				termCheckByModelCondition.setInEparchyCode(contract.getEparchyCode());
				termCheckByModelCondition.setInCityCode(inCityCode);		//需要查询三户资料		
				termCheckByModelCondition.setModelCode(activityGoodsPara.getRsrv1());   
				termCheckByModelCondition.setProductTypeCode(activityGoodsPara.getCrmCode());				
 			   try { 
			   Map checkMap = contractSaleService.termCheckByModel(termCheckByModelCondition);
			   if(checkMap.size()>0)  
				      throw new Exception("99购机用户终端机型校验接口查询失败：" + checkMap.get("resultInfo")); 	
			} catch (Exception e) {
				  logger.error("99购机终端机型校验接口异常：",e);
		            throw new Exception("99购机终端机型校验接口异常：" + e);
			} 
			
			
    }

    /**
	 * //目标客户资格查询
	 * @param serialNumber
	 * @return
	 */
    public boolean judgeTargetNumber(String serialNumber) {			
			ActivityTargetNumber  activityTargetNumber = new ActivityTargetNumber();
			 activityTargetNumber.setActNumber(serialNumber);
			 activityTargetNumber.setActCode(TargetNumberUtil.ACT_TYPE_2017_DOUBLE);		 
			 List<ActivityTargetNumber>  listTargetNumber=  activityTargetNumberService.queryTagetNumberStatus(activityTargetNumber);
			 
				if(listTargetNumber.size()>0){
					 return true;
				  } 
			return false ;
		}
	 
	   
	 /**
	  * //判断手机是否入网一年或者新用户
	  * @param serialNumber
	  * @param type
	  * @return
	  * @throws Exception
	  */
	 public boolean judgeNumberLife(String serialNumber,String type,String inTime)  {		 			
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");			
			try {
				 Date nowDate= new Date();
				 Date indate = format.parse(inTime);
				 if(type.equals("0")){
					 if(31 > getDiscrepantDays(indate,nowDate)) 
						 return true;  
				 }				 
				 if(type.equals("1")){
					 if(365< getDiscrepantDays(indate,nowDate))
						 return true; 
				 } 
			} catch (ParseException e) {				
				 e.printStackTrace();
					logger.error("合约计划用户用户开户时间判断异常：",e);
				 return false; 
			} 
			return false;
		}
    
	 public  int getDiscrepantDays(Date dateStart, Date dateEnd) {
	        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
	    }
	 
    
    /**
	 * 终端机型校验
	 * @param termCheckByModelCondition
	 * @return
	 */
	public Map<String,Object> termCheckByModel(TermCheckByModelCondition  termCheckByModelCondition){
		Map<String,Object> resultMap = Maps.newHashMap();
		 try {				
				logger.info(" termCheckByModelCondition " + termCheckByModelCondition.toString());
				Map checkMap = contractSaleService.termCheckByModel(termCheckByModelCondition);
				 logger.info("termCheckByModelCondition" + JSON.toJSONString(checkMap,true));
				
				JSONObject checkJSON = JSONObject.parseArray(checkMap.get("result").toString()).getJSONObject(0);
				String checkResultcode = checkJSON.getString("X_RESULTCODE");
				String checkResultInfo = checkJSON.getString("X_RESULTINFO");
				if(!"0".equals(checkResultcode)){
					resultMap.put("resultCode","fail");
					resultMap.put("resultInfo",checkResultInfo);
					return resultMap;
				}
			} catch (Exception e) {
				logger.error("机型校验查询异常：",e);
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo","机型校验资格查询异常：" + e);
				return resultMap;
			}  
		 return resultMap;
	}
	
	 
	
	/**
	 * 超级明星机预受理接口
	 * @param condition
	 * @return
	 */	 
	public Map<String,Object> conSuperStarCheckIntf(ConSuperStarCheckIntfCondition condition ){
		Map<String,Object> resultMap = Maps.newHashMap();
		try {		 
		  	Map checkMap = contractSaleService.conSuperStarCheckIntf(condition);
			JSONObject checkJSON = JSONObject.parseArray(checkMap.get("result").toString()).getJSONObject(0);
			String checkResultcode = checkJSON.getString("X_RESULTCODE");
			String checkResultInfo = checkJSON.getString("X_RESULTINFO");			
			if(!"0".equals(checkResultcode)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo",checkResultInfo);
				return resultMap;
			}			 
		} catch (Exception e) {
			logger.error("超级明星机预受理接口查询接口异常：",e);
			resultMap.put("resultCode","fail");
			resultMap.put("resultInfo","超级明星机预受理接口查询接口异常：" + e);
			return resultMap;
		}
		return resultMap;
	} 
	
	/**
	 * 99购机用户资格查询接口
	 * @param condition
	 * @return
	 */	 
	public Map<String,Object> conRegularCheckIntf(ConRegularCheckIntfCondition condition ){
		Map<String,Object> resultMap = Maps.newHashMap();
		try {			 
		 
		  	Map checkMap = contractSaleService.conRegularCheckIntf(condition);
			JSONObject checkJSON = JSONObject.parseArray(checkMap.get("result").toString()).getJSONObject(0);
			String checkResultcode = checkJSON.getString("X_RESULTCODE");
			String checkResultInfo = checkJSON.getString("X_RESULTINFO");			
			if(!"0".equals(checkResultcode)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo",checkResultInfo);
				return resultMap;
			}			 
		} catch (Exception e) {
			logger.error("99购机用户资格查询接口异常：",e);
			resultMap.put("resultCode","fail");
			resultMap.put("resultInfo","99购机用户资格查询接口异常：" + e);
			return resultMap;
		}
		return resultMap;
	} 
	
	/**
	 * 合约计划担保客户校验
	 * @param condition
	 * @return
	 */
	public Map<String,Object> contractSaleCheckSure(ContractSaleCheckSureCondition condition){
		Map<String,Object> resultMap = Maps.newHashMap();
		try {  
			logger.info("checkMap.get(\"result\") " +condition.getCheckType());
			logger.info("checkMap.get(\"result\") " + condition.getProductTypeCode());
			logger.info("checkMap.get(\"result\") " + condition.getSerialNumber());
			Map checkMap = contractSaleService.contractSaleCheckSure(condition);
			logger.info("checkMap.get(\"result\") ");
			 logger.info(JSON.toJSONString(checkMap,true));
			JSONObject checkJSON = JSONObject.parseArray(checkMap.get("result").toString()).getJSONObject(0);
			String checkResultcode = checkJSON.getString("X_RESULTCODE");
			String checkResultInfo = checkJSON.getString("X_RESULTINFO");
			if(!"0".equals(checkResultcode)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo",checkResultInfo);
				return resultMap;
			}		 
		} catch (Exception e) {
			logger.error("合约计划担保客户校验：",e);
			resultMap.put("resultCode","fail");
			resultMap.put("resultInfo","合约计划担保客户校验：" + e);
			return resultMap;
		}
		return resultMap;
	}


	public Boolean checkGoodsIdHasSku(TfUserGoodsCar goodsCar){
        List<TfGoodsSku> skus = goodsStaticService.getGoodsSkuByGoodsIdAndChnl(goodsCar.getGoodsId(),CommonParams.CHANNEL_CODE);
        for(TfGoodsSku sku :skus){
            if(sku.getGoodsSkuId().equals(goodsCar.getGoodsSkuId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据用户所选的goodsId和goodsSkuId从数据库中取出数据,防止用户自行修改商品的店铺IdD等信息
     * @param userGoodsCarModel
     */
    private void updateCartList(UserGoodsCarModel userGoodsCarModel){
        for(TfUserGoodsCar tfUserGoodsCar :userGoodsCarModel.getUserGoodsCarList()){

        }
    }

    ///**
    // * 根据用户IP地址记录选号次数
    // *
    // * @param request
    // */
    //public void setIpPhoneNumOrders(HttpServletRequest request) {
    //    String ip = this.getIpAddress(request);
    //    String date = DateUtils.getDate("yyyyMMdd");
    //
    //    String ipPhoneNumOrders = JedisClusterUtils.get(date + "ip-phoneNum-orders-" + ip);
    //    if (StringUtils.isBlank(ipPhoneNumOrders)) {
    //        ipPhoneNumOrders = "1";
    //    } else {
    //        ipPhoneNumOrders = String.valueOf(Integer.valueOf(ipPhoneNumOrders) + 1);
    //    }
    //    JedisClusterUtils.set(date + "ip-phoneNum-orders-" + ip, ipPhoneNumOrders, 86400);//有效期24小时
    //}
    //
    ///**
    // * 根据用户IP地址记录选号次数
    // *
    // * @param request
    // * @return
    // */
    //public int getIpPhoneNumOrders(HttpServletRequest request) throws Exception {
    //    String ip = this.getIpAddress(request);
    //    logger.info("ip:" + ip);
    //    String date = DateUtils.getDate("yyyyMMdd");
    //
    //    String ipPhoneNumOrders = JedisClusterUtils.get(date + "ip-phoneNum-orders-" + ip);
    //    if (StringUtils.isBlank(ipPhoneNumOrders)) {
    //        return 0;
    //    }
    //
    //    return Integer.parseInt(ipPhoneNumOrders);
    //}

    ///**
    // * 获取用户IP地址
    // *
    // * @param request
    // * @return
    // */
    //public String getIpAddress(HttpServletRequest request) {
    //    String ip = request.getHeader("x-forwarded-for");
    //    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //        ip = request.getHeader("Proxy-Client-IP");
    //    }
    //    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //        ip = request.getHeader("WL-Proxy-Client-IP");
    //    }
    //    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //        ip = request.getHeader("HTTP_CLIENT_IP");
    //    }
    //    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    //    }
    //    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //        ip = request.getRemoteAddr();
    //    }
    //    return ip;
    //}

}
