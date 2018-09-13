package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.activity.api.IActivityTargetNumberService;
import com.ai.ecs.activity.entity.ActivityGoodsPara;
import com.ai.ecs.common.framework.exception.ServiceException;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.platform.utils.PhoneNumberUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.contract.entity.*;
import com.ai.ecs.ecsite.modules.contract.service.ContractSaleService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqGetUserAllDiscntCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.ecsite.modules.offerBalance.entity.OfferBalanceCondition;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSend4AllChanCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.ICommonPropagandaService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.api.IGoodsStaticService;
import com.ai.ecs.goods.api.agreement.IAgreeMentService;
import com.ai.ecs.goods.entity.CommonPropaganda;
import com.ai.ecs.goods.entity.goods.GoodsWater;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.integral.entity.UserRating;
import com.ai.ecs.integral.service.UserRatingService;
import com.ai.ecs.merchant.shop.pojo.ShopDetail;
import com.ai.ecs.merchant.shop.service.IShopInfoService;
import com.ai.ecs.sales.api.IMarketService;
import com.ai.ecs.sales.entity.MarketGoodsSkuPrice;
import com.ai.ecs.sales.entity.MarketInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("goodsInfo")
public class GoodsSkuController extends BaseController {

	@Autowired
	IGoodsStaticService goodsStaticService;
	@Autowired
	IMarketService marketService;
	@Autowired
	UserRatingService userRatingService;
	@Autowired
    IAgreeMentService agreeMentService;
	@Autowired
	ICommonPropagandaService commonPropagandaService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	ContractSaleService contractSaleService;
	@Autowired
	PhoneAttributionService phoneAttributionService;
	
	@Autowired
	IActivityTargetNumberService activityTargetNumberService;
	
	@Autowired
	BasicInfoQryModifyService  basicInfoQryModifyService;
	 
	@Autowired
    private JedisCluster jedisCluster;
	@Autowired
	IShopInfoService shopInfoService;
	@Autowired
    SmsSendService smsSendService;
	@Autowired
	IGoodsManageService goodsManageService;
	@Autowired
	FlowServeService flowServeService;

	// 集团购机比较时间
	private static String compareTime = new PropertiesLoader("mall.properties").getProperty("compareTime");
	/**
	 * 模糊化字符串
	 * @param name
	 * @return
	 */
	public String blurringName(String name)
	{
	    if(name==null)
	    {
	        return null;    
	    }
	    return name.substring(0, 1) +"***"+name.substring(name.length()-1,name.length());
	}

	/**
	 * 通过商品编号查询商品规格列表信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getGoodsWater")
	public void getGoodsWater(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		try {
			JSONObject result = new JSONObject();
			Long goodsId = Long.parseLong(request.getParameter("goodsId"));
			Long goodsSkuId = Long.parseLong(request.getParameter("goodsSkuId"));
			String goodsType =request.getParameter("goodsType");
			Map<String, String> params = parseParams(request.getParameter("params"));
			GoodsWater goodsWater = goodsStaticService.getGoodsWater(goodsId,goodsSkuId,
					CommonParams.CHANNEL_CODE, goodsType, params);
			Long shopGoodsPrice = goodsWater.getGoodsSalePrice();
			
			String marketId = request.getParameter("marketId");
			MarketInfo marketInfo = null;
			if(StringUtils.isNotEmpty(marketId)){
				// marketInfo=marketService.getMarketInfo(goodsId,CommonParams.CHANNEL_CODE);\
				 String preStr = "ACTIVITY_DATA_";
				 String marketInfoStr =  String.valueOf(JedisClusterUtils.getObject(preStr+marketId));
				 marketInfo = JSONObject.parseObject(marketInfoStr,MarketInfo.class);
				
			}
			
			//根据session判断当前用户选择的商品时候是优惠购机商品
			 Session session=UserUtils.getSession();
			if( null != request.getParameter("goodsId") && null != request.getParameter("goodsType") && "1".equals(request.getParameter("goodsType").toString())){
				 ActivityGoodsPara  activityGoodsPara = new ActivityGoodsPara();
				 activityGoodsPara.setChanId(CommonParams.CHANNEL_CODE);
				 activityGoodsPara.setGoodCode(request.getParameter("goodsId").toString());
				 List<ActivityGoodsPara> listPara=  activityTargetNumberService.queryActivityGoodsParaListForBuy(activityGoodsPara); 
				 if(listPara.size()>0){
					    //选择的商品属于优惠购机就存在缓存
						session.setAttribute("intiActivityGoodsParaList", listPara);					 
				 }		 
			} else{
				 session.removeAttribute("intiActivityGoodsParaList");
				 session.removeAttribute("sureNo_99Buy");
				 //不在的时候要删除，不让后续额判断能在session中取到这些信息
			 }		
			
			if(marketInfo!=null){//正在参与活动的商品，商品详情从营销侧取
				result.put("status", 1);
				List<MarketGoodsSkuPrice> marketGoodsSkuPriceList=marketInfo.getMarketGoodsSkuPriceList();
				//默认取第一种规格的活动价格
				goodsWater.setGoodsSalePrice(marketGoodsSkuPriceList.get(0).getGoodsSkuMarketPrice());
				long stockNum=0;
				long saleNum=0;
				for(MarketGoodsSkuPrice item:marketGoodsSkuPriceList){
					if(goodsWater.getGoodsSkuId()==null){
						stockNum+=item.getGoodsSkuMarketStock();
						saleNum+=item.getSaleNum();
					}else{
						if(goodsWater.getGoodsSkuId().longValue()==item.getGoodsSkuId().longValue()){
							stockNum=item.getGoodsSkuMarketStock();
							saleNum=item.getSaleNum();
							//设置指定skuid商品的活动价格
							goodsWater.setGoodsSkuPrice(item.getGoodsSkuMarketPrice());
							goodsWater.setGoodsDeposit(item.getFirstPrice());
						}
					}
				} 
				goodsWater.setStockNum(stockNum);
				goodsWater.setSaleNum(saleNum);
				goodsWater.setMarketId(marketInfo.getMarketId());
			}else{
				result.put("status", 0);
			}

			boolean isAfterCurrentDate = goodsService.isAfterCurrentDate(compareTime);
			boolean isCustomizeOrVotelTerminal = goodsService.isCustomizeOrVotelTerminal(goodsWater.getGoodsId());
			//是移动定制版或支持VOTEL功能的终端 && 当前日期小于2016-09-31
			if(isCustomizeOrVotelTerminal && isAfterCurrentDate) {
				Long terminalSalePrice = goodsService.getTerminalSalePrice(goodsWater.getGoodsSalePrice());
				goodsWater.setGoodsGroupPrice(terminalSalePrice);
			}

			//查询商品评价总数
			Long userRatingCount = getUserRatings(goodsId);

			result.put("userRatingCount",userRatingCount);
			result.put("goodsWater", goodsWater);
			result.put("marketInfo", marketInfo);
			result.put("shopGoodsPrice", shopGoodsPrice);
			response.getWriter().write(result.toString());
		} catch (Exception e) {
			logger.error("getGoodsWater:",e);
		}
	}

	private Long getUserRatings(Long goodsId) {
		String ratingKey="UserRating_".concat(goodsId+"");
		UserRating userRating = new UserRating();
		userRating.setGoodsId(goodsId);
		Long count = 0L;
		try {
			String rating = JedisClusterUtils.get(ratingKey);
			if (StringUtils.isNotEmpty(rating)) {
				count = Long.valueOf(rating);
			} else {
				count = userRatingService.queryScoreCount(userRating);
				JedisClusterUtils.set(ratingKey,count+"", 900);
			}
		}catch (Exception e){
			logger.error("getUserRatings:",e);
		}
		return count;
	}
	/**
	 * 通过店铺id动态获取店铺信息
	 */
	@RequestMapping("getShopDetail")
	@ResponseBody
	public Map getShopDetail(@RequestParam Long shopId){
		Map map = new HashMap();
		ShopDetail detail = shopInfoService.getShopDetailInfo(shopId,CommonParams.CHANNEL_CODE);
		map.put("shop",detail);
		return map;
	}
	@RequestMapping(value = "/getGoodsMarket")
	public void getGoodsMarket(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		try {
			Long goodsId = Long.parseLong(request.getParameter("goodsId"));
			String marketId = request.getParameter("marketId");
			
			JSONObject result = new JSONObject();
			
//			MarketInfo marketInfo=marketService.getMarketInfo(goodsId,CommonParams.CHANNEL_CODE);
			MarketInfo marketInfo = null;
			if(StringUtils.isNotEmpty(marketId)){
				 //String preStr = "ACTIVITY_DATA_";
				 //String marketInfoStr =  String.valueOf(JedisClusterUtils.getObject(preStr+marketId));
				 //marketInfo = JSONObject.parseObject(marketInfoStr,MarketInfo.class);
				MarketInfo queryMarket = new MarketInfo();
				queryMarket.setMarketId(Long.valueOf(marketId));
				marketInfo = this.marketService.queryMarket(queryMarket);
				logger.error("marketInfoStr:"+JSONArray.toJSONString(marketInfo));
			}
			
			if(marketInfo==null){//未参与活动
				result.put("status", 0);
				result.put("message", "此商品未参与活动");
			}else{
				if(marketInfo.getMarketStatusId()==7){
					result.put("status", 0);
					result.put("message", "活动已下架");
					response.getWriter().write(result.toString());
					return ;
				}
				
				Date now = new Date();
				if(now.getTime()> marketInfo.getMarketEndtime().getTime()){
					result.put("status", 0);
					result.put("message", "活动已结束");
					response.getWriter().write(result.toString());
					return ;
				}
				
				
				if(goodsId.equals(marketInfo.getMarketGoodsSkuRefList().get(0).getGoodsId())){
					result.put("status", 1);
					List<MarketGoodsSkuPrice> marketGoodsSkuPriceList=marketInfo.getMarketGoodsSkuPriceList();
					//默认取第一种规格的活动价格
					result.put("marketPrice", marketGoodsSkuPriceList.get(0).getGoodsSkuMarketPrice());
					//活动类型
					result.put("marketType", marketInfo.getMarketTypeId());
					if(marketInfo.getMarketTypeId()==3){
						//预售活动定金
						result.put("firstPrice", marketGoodsSkuPriceList.get(0).getFirstPrice());
					}
	                if(marketInfo.getMarketTypeId()==2){
	                	long nowDate=(new Date()).getTime();
	                	result.put("to_start", marketInfo.getMarketStarttime().getTime()-nowDate);
	                	result.put("to_end", marketInfo.getMarketEndtime().getTime()-nowDate);
	                }
					long stockNum=0;
					long saleNum=0;
					for(MarketGoodsSkuPrice item:marketGoodsSkuPriceList){
						//活动商品库存为所有sku库存之和
						stockNum+=item.getGoodsSkuMarketStock();
						saleNum+=item.getSaleNum();
						logger.error("item.getGoodsSkuMarketStock==============:"+item.getGoodsSkuMarketStock());
						logger.error("stockNum==============:"+stockNum);
					}
					result.put("stockNum", stockNum);
					result.put("saleNum", saleNum);
					
					
				}
				else {
					result.put("status", 0);
					result.put("message", "活动与商品不匹配");
					
				}
				
				
			}

			response.getWriter().write(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询商品评价
	 * @param goodsId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryUserRating")
	@ResponseBody
	public Page<UserRating> queryUserRating(Long goodsId,int pageNo,int pageSize){
		try{
			Page<UserRating> page = new Page<UserRating>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			UserRating userRating = new UserRating();
			userRating.setGoodsId(goodsId);
			userRating.setRatingStatusId(2);
			userRating.setDoType("reply");
			page = userRatingService.findAllScorePage(page,userRating);
			List<UserRating> list = page.getList();
			if(list!=null && list.size()>0)
			{
			    for(int i=0;i<list.size();i++)
	            {
	                 UserRating ur = list.get(i);
	                 ur.setMemberLoginName(blurringName(ur.getMemberLoginName()));
	            }    
			}
			
			return page;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询合约列表
	 * @param phoneNumber
	 * @return
	 */
	@RequestMapping("/queryContractProducts")
	@ResponseBody
	public Map<String,Object> queryContractProducts(String phoneNumber,String goodsId){
		Map<String,Object> resultMap = Maps.newHashMap();
		try{
			if(StringUtils.isBlank(phoneNumber)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo","手机号码不能为空");
				return resultMap;
			}

			if(!PhoneNumberUtils.isChinaMobilePhoneNum(phoneNumber)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo","请输入正确的移动手机号码");
				return resultMap;
			}

			//查询手机号码归属地市编码
			PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
			phoneAttributionModel.setSerialNumber(phoneNumber);
			Map<String, Object> phoneAttributionMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
			JSONObject phoneAttributionJSON = JSONObject.parseArray(phoneAttributionMap.get("result").toString()).getJSONObject(0);
			String phoneNumberResultcode = phoneAttributionJSON.getString("X_RESULTCODE");
			String phoneNumberResultInfo = phoneAttributionJSON.getString("X_RESULTINFO");
			if(!"0".equals(phoneNumberResultcode) || !"ok".equalsIgnoreCase(phoneNumberResultInfo)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo",phoneNumberResultInfo);
				return resultMap;
			}

			TfGoodsInfo goodsInfo=goodsManageService.queryGoodsInfo(Long.valueOf(goodsId));

			//查询合约列表
			String eparchCode = phoneAttributionJSON.getString("AREA_CODE");
			ContractSaleConListCondition conListCondition = new ContractSaleConListCondition();
			conListCondition.setSerialNumber(phoneNumber);
			
			//根据session判断当前用户选择的商品时候是优惠购机商品
			Session session=UserUtils.getSession();
			 List<ActivityGoodsPara>  tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");
			 if(null !=tempList &&  tempList.size()>0 && tempList.get(0).getGoodCode().equals(goodsId)){				
				conListCondition.setModelCode(tempList.get(0).getRsrv1());
				conListCondition.setProductTypeCode(tempList.get(0).getCrmCode());
 			}else{
				 //取商品配置的合约类型
				 if(null!=goodsInfo && StringUtils.isNotEmpty(goodsInfo.getAgreeTypeId())){
					 conListCondition.setProductTypeCode(goodsInfo.getAgreeTypeId());
				 }else {
					 //没有配置，默认用25（收入型合约）
					 conListCondition.setProductTypeCode("25");
				 }
		   }
			 
			conListCondition.setInEparchyCode(eparchCode);
			JSONObject kfJSON = CommonParams.EPARCH_KF.get(eparchCode);
			conListCondition.setInStaffId(kfJSON.getString("inStaffId"));
			conListCondition.setInDepartId(kfJSON.getString("inDepartId"));
            
			Map contractMap = contractSaleService.contractSaleConList(conListCondition);
			JSONObject contractJSON = JSONObject.parseArray(contractMap.get("result").toString()).getJSONObject(0);

			String contractResultcode = contractJSON.getString("X_RESULTCODE");
			String contractResultInfo = contractJSON.getString("X_RESULTINFO");
			if(!"0".equals(contractResultcode) || !"ok".equalsIgnoreCase(contractResultInfo)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo",contractResultInfo);
				return resultMap;
			}

			JSONArray contractProducts = contractJSON.getJSONArray("CONTRACTLIST");

			String agreeGroupNo="99632509";//电渠收入补贴50%
			//商品配置了合约，取商品配置合约
			if(null!=goodsInfo && StringUtils.isNotEmpty(goodsInfo.getAgreeGroupNo())){
				agreeGroupNo=goodsInfo.getAgreeGroupNo();
			}
			JSONArray contractProductsFirst = new JSONArray();
			for (int i = 0; i < contractProducts.size(); i++) {
				JSONObject contractProduct = contractProducts.getJSONObject(i);
				String contractId = contractProduct.getString("PRODUCT_ID");
				if(agreeGroupNo.equals(contractId)){
					contractProduct.put("PRODUCT_NAME","购机送话费");
					contractProductsFirst.add(contractProduct);
					break;
				}
				
				//合约类型相同，但只展示商品配置的合约
				 if(null !=tempList && tempList.size()>0 && tempList.get(0).getGoodCode().equals(goodsId)){
					 for(ActivityGoodsPara activityGoodsPara : tempList){
						 if(activityGoodsPara.getProCode().equals(contractId))
						 {
							    contractProduct.put("PRODUCT_NAME",activityGoodsPara.getActTitle());
								contractProductsFirst.add(contractProduct);
								continue;
						 }						 
					 } 
				 }
			}
			
			resultMap.put("resultCode","success");
			resultMap.put("resultInfo",contractResultInfo);
			resultMap.put("contractProducts",contractProductsFirst);
			resultMap.put("eparchCode",eparchCode);
			resultMap.put("productTypeCode",conListCondition.getProductTypeCode());
		}catch (Exception e){
			logger.error("查询合约计划异常：",e);
			resultMap.put("resultCode","fail");
			resultMap.put("resultInfo","查询合约计划异常：" + e.getMessage());
		}
		return resultMap;
	}

	/**
	 * 查询合约套餐
	 * @return
	 */
	@RequestMapping("/queryContractPackages")
	@ResponseBody
	public Map<String,Object> queryContractPackages(ContractSaleInfoQueryCondition condition){
		Map<String,Object> resultMap = Maps.newHashMap();
		try {
			if(condition == null){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo","合约、手机号码和手机号码归属地市编码不能为空");
				return resultMap;
			}

			JSONObject kfJSON = CommonParams.EPARCH_KF.get(condition.getInEparchyCode());
			condition.setInStaffId(kfJSON.getString("inStaffId"));
			
			
			Session session=UserUtils.getSession();
			 List<ActivityGoodsPara>  tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");
			if(null !=tempList && tempList.size()>0){
				for(ActivityGoodsPara activityGoodsPara : tempList){
					  if(activityGoodsPara.getProCode().equals(condition.getContractId())){
						  condition.setDeviceModelCode(activityGoodsPara.getRsrv1());
					  }
				}			
			} 

  			Map packageMap = contractSaleService.contractSaleInfoQuery(condition);
			JSONObject packageJSON = JSONObject.parseArray(packageMap.get("result").toString()).getJSONObject(0);
			String packageResultcode = packageJSON.getString("X_RESULTCODE");
			String packageResultInfo = packageJSON.getString("X_RESULTINFO");
			if(!"0".equals(packageResultcode) || !"ok".equalsIgnoreCase(packageResultInfo)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo",packageResultInfo);
				return resultMap;
			}

			resultMap.put("result",packageJSON);
		} catch (Exception e) {
			logger.error("查询合约套餐异常：",e);
			resultMap.put("resultCode","fail");
			resultMap.put("resultInfo","查询合约套餐异常：" + e);
		}

		return resultMap;
	}

	/**
	 * 查询计划消费计算结果
	 * @param condition
	 * @return
	 */
	@RequestMapping("/queryContractSaleConCal")
	@ResponseBody
	public Map<String,Object> queryContractSaleConCal(ContractSaleConCalCondition condition){
		Map<String,Object> resultMap = Maps.newHashMap();
		try {
			if(condition == null){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo","合约ID、手机号码和手机号码归属地市编码不能为空");
				return resultMap;
			}

			Session session=UserUtils.getSession();
			 List<ActivityGoodsPara>  tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");
			if(null !=tempList && tempList.size()>0){
				for(ActivityGoodsPara activityGoodsPara : tempList){
					  if(activityGoodsPara.getProCode().equals(condition.getContractId())){
						  condition.setDeviceModelCode(activityGoodsPara.getRsrv1());
						  condition.setProductTypeCode(activityGoodsPara.getCrmCode());
					  }
				}			
			} 
			
			Map calMap = contractSaleService.contractSaleConCal(condition);
			JSONObject calJSON = JSONObject.parseArray(calMap.get("result").toString()).getJSONObject(0);
			String packageResultcode = calJSON.getString("X_RESULTCODE");
			String packageResultInfo = calJSON.getString("X_RESULTINFO");
			if(!"0".equals(packageResultcode) || !"ok".equalsIgnoreCase(packageResultInfo)){
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo",packageResultInfo);
				return resultMap;
			}
			resultMap.put("result",calJSON);
		} catch (Exception e) {
			logger.error("查询计划消费计算结果异常：",e);
			resultMap.put("resultCode","fail");
			resultMap.put("resultInfo","查询计划消费计算结果异常：" + e);
		}

		return resultMap;
	}

	/**
	 * 合约计划用户资格查询
	 * @param condition
	 * @return
	 */
	@RequestMapping("/contractSaleCheck")
	@ResponseBody
	public Map<String,Object> contractSaleCheck(HTContractSaleCheckCondition condition) throws Exception{
		Map<String,Object> resultMap = Maps.newHashMap();
		Session session=UserUtils.getSession();
		
		//如果是明星机，99购机做预售校验 
		//优惠购机活动判断 		 
		//想用的goodsId有相同的机型编码
		//根据session判断当前用户选择的商品时候是优惠购机商品
	 	//session.setAttribute("sureNo_99Buy","18774967474");
		 List<ActivityGoodsPara>  tempList = (ArrayList<ActivityGoodsPara>) session.getAttribute("intiActivityGoodsParaList");
		if(null !=tempList && tempList.size()>0){
			String inTime = "";
			String inCityCode = "";
			try
			{
				 BasicInfoCondition bCondition = new BasicInfoCondition();
				 bCondition.setSerialNumber(condition.getSerialNumber());
				 bCondition.setxGetMode("0");
					Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(bCondition);		
				//	logger.info(JSON.toJSONString(userInfo,true)); 
					if(MapUtils.isNotEmpty(userInfo) && HNanConstant.SUCCESS.equals(userInfo.get("respCode"))){
						Map userBasicInfo = net.sf.json.JSONObject.fromObject(userInfo.get("result").toString().replace("[", "").replace("]", ""));
						  inTime =userBasicInfo.get("IN_DATE").toString(); // "2016-12-09 07:29:00";//
						  inCityCode =  userBasicInfo.get("CITY_CODE").toString(); //"A31A";//
					}else{
						resultMap.put("resultCode","fail");
						resultMap.put("resultInfo","没有查询到该手机号的用户信息");
						return resultMap;
					} 
			} catch (Exception e) {
				logger.error("三户资料查询异常：",e);
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo","三户资料查询异常：" + e);
				return resultMap;
			}		 
		
			  //判断选中的机型是否满足明星机，99购机的条件
			for(ActivityGoodsPara activityGoodsPara :tempList ){
				//选择的机型属于明星机，99购机做预售校验 
				logger.info("--------activityGoodsPara.getProCode()---------" + condition.getContractId() + "====="+activityGoodsPara.getProCode());
				if(activityGoodsPara.getProCode().equals(condition.getContractId())){
					Date  current = new Date();
					Date start = activityGoodsPara.getStartTime();
					Date end =  activityGoodsPara.getEndTime();
					if(current.before(start)){
						resultMap.put("resultCode","fail");
						resultMap.put("resultInfo","活动尚未开始！");
						return resultMap;
					}
					
					if(current.after(end)){
						resultMap.put("resultCode","fail");
						resultMap.put("resultInfo","活动已经结束！");
						return resultMap;
					}  
					
					 logger.info("-----------------" + condition.getContractId()); 
					
					//99购机特惠
						if("99812781,99812782,99812783,99812784".contains(condition.getContractId())){
							//目标客户校验或和新入网							
						   //合约担保计划校验
						 ConRegularCheckIntfCondition conRegularCheckIntfCondition = new ConRegularCheckIntfCondition();
						   conRegularCheckIntfCondition.setSerialNumber(condition.getSerialNumber());
						   conRegularCheckIntfCondition.setImei("-1");
						   conRegularCheckIntfCondition.setContractId(condition.getContractId());
						   conRegularCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1());
						   conRegularCheckIntfCondition.setProductId(condition.getProductStr().get(0).get("PRODUCT_ID").toString());
						   conRegularCheckIntfCondition.setDiscntCode(condition.getProductStr().get(0).get("ELEMENT_ID").toString());						   
						   resultMap  = goodsService.conRegularCheckIntf(conRegularCheckIntfCondition);
						   logger.info("conRegularCheckIntf" + resultMap.toString());
 						   
							if(resultMap.size()>0)  return resultMap;
								break; 
						}
					
						//明星机
						if("99812790,99812791,99812792,99812793,99812788,99812789".contains(condition.getContractId())){							
							//超级明星机预受理
							ConSuperStarCheckIntfCondition  conSuperStarCheckIntfCondition  = new ConSuperStarCheckIntfCondition();
							conSuperStarCheckIntfCondition.setSerialNumber(condition.getSerialNumber());
							conSuperStarCheckIntfCondition.setImei("-1");
							conSuperStarCheckIntfCondition.setContractId(condition.getContractId());
							conSuperStarCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1()); 
							  Map<String,Object> productStrMap = Maps.newHashMap();
					            productStrMap.put("PRODUCT_ID",condition.getProductStr().get(0).get("PRODUCT_ID").toString());
					            productStrMap.put("DISCNT_CODE",condition.getProductStr().get(0).get("ELEMENT_ID").toString());		    
					        	conSuperStarCheckIntfCondition.setProductStr(productStrMap);
							resultMap  = goodsService.conSuperStarCheckIntf(conSuperStarCheckIntfCondition);
							if(resultMap.size()>0)  return resultMap;
							break; 
						 }  
						
						//老客户担保（目标客户（仅限2/3G客户和新入网客户） + 担保号码+入网一年以上）
						if("99812787".equals(condition.getContractId())){						 
							 //合约担保计划校验			
							//补充合约担保号码
							 String sureNo ="";
							logger.info("--------sureNo--------=====");
						 if(null != session.getAttribute("sureNo_99Buy") && !session.getAttribute("sureNo_99Buy").equals(""))						
								sureNo = session.getAttribute("sureNo_99Buy").toString();						  
							if(null == sureNo || "".equals(sureNo)){
								  resultMap.put("resultCode","nosureno");
								   resultMap.put("resultInfo","老客户孝心机需要有星级客户担保！");
								  return resultMap;
							}  
							
							if(sureNo.equals(condition.getSerialNumber())){
								  session.removeAttribute("sureNo_99Buy");
								  resultMap.put("resultCode","fail");
								   resultMap.put("resultInfo","担保号码不能与购买手机合约的号码相同。");
								  return resultMap;
							}
							   ConRegularCheckIntfCondition conRegularCheckIntfCondition = new ConRegularCheckIntfCondition();
							   conRegularCheckIntfCondition.setSerialNumber(condition.getSerialNumber());
							   conRegularCheckIntfCondition.setImei("-1");
							   conRegularCheckIntfCondition.setContractId(condition.getContractId());
							   conRegularCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1());
							   conRegularCheckIntfCondition.setProductId(condition.getProductStr().get(0).get("PRODUCT_ID").toString());
							   conRegularCheckIntfCondition.setDiscntCode(condition.getProductStr().get(0).get("ELEMENT_ID").toString());		
							   conRegularCheckIntfCondition.setSureNo(sureNo);//session 中的担保号码
							   resultMap  = goodsService.conRegularCheckIntf(conRegularCheckIntfCondition);
								if(resultMap.size()>0)  return resultMap;
									break;
						 }
						
						
						//新入网（目标客户（仅限2/3G客户和新入网客户））
						if("99812786".equals(condition.getContractId())){ 							 
				              //合约担保计划校验
							  ConRegularCheckIntfCondition conRegularCheckIntfCondition = new ConRegularCheckIntfCondition();
							   conRegularCheckIntfCondition.setSerialNumber(condition.getSerialNumber());
							   conRegularCheckIntfCondition.setImei("-1");
							   conRegularCheckIntfCondition.setContractId(condition.getContractId());
							   conRegularCheckIntfCondition.setDeviceModelCode(activityGoodsPara.getRsrv1());
							   conRegularCheckIntfCondition.setProductId(condition.getProductStr().get(0).get("PRODUCT_ID").toString());
							   conRegularCheckIntfCondition.setDiscntCode(condition.getProductStr().get(0).get("ELEMENT_ID").toString());						   
							   resultMap  = goodsService.conRegularCheckIntf(conRegularCheckIntfCondition);
								if(resultMap.size()>0)  return resultMap;
									break; 
						    }   
						
						 //终端机型校验						
						 JSONObject kfJSON = CommonParams.EPARCH_KF.get(condition.getInEparchyCode());
						 TermCheckByModelCondition termCheckByModelCondition  = new TermCheckByModelCondition();
						 	termCheckByModelCondition.setInStaffId(kfJSON.getString("inStaffId"));
							termCheckByModelCondition.setInDepartId(kfJSON.getString("inDepartId")); 
							termCheckByModelCondition.setInEparchyCode(condition.getInEparchyCode());
							termCheckByModelCondition.setInCityCode(inCityCode);		//需要查询三户资料		
							//termCheckByModelCondition.setModelCode(activityGoodsPara.getRsrv1());   
							termCheckByModelCondition.setProductTypeCode("06");				
							resultMap  = goodsService.termCheckByModel(termCheckByModelCondition);
							
							  logger.info("termCheckByModelCondition" + resultMap.toString());
							  logger.info("termCheckByModel  "+termCheckByModelCondition.toString());
							if(resultMap.size()>0) 
								return resultMap;
						    // 终端机型校验
							else {
								resultMap.put("resultCode","success");
								return resultMap;
 						 }  
					} 
				 
			}  
		}else{
			Map checkMap = null;
			try {
				JSONObject kfJSON = CommonParams.EPARCH_KF.get(condition.getInEparchyCode());
				condition.setInStaffId(kfJSON.getString("inStaffId"));
				condition.setInDepartId(kfJSON.getString("inDepartId"));
	
				checkMap = contractSaleService.contractSaleCheck(condition);
				JSONObject checkJSON = JSONObject.parseArray(checkMap.get("result").toString()).getJSONObject(0);
				String checkResultcode = checkJSON.getString("X_RESULTCODE");
				String checkResultInfo = checkJSON.getString("X_RESULTINFO");
				if(!"0".equals(checkResultcode)){
					resultMap.put("resultCode","fail");
					resultMap.put("resultInfo",checkResultInfo);
					return resultMap;
				}

				boolean b = checkPlan(condition.getSerialNumber(),condition.getProductStr().get(0).get("ELEMENT_ID").toString());
				if(b){
					resultMap.put("resultCode","fail");
					resultMap.put("resultInfo","当前套餐已订购，请重新选择！");
					return resultMap;
				}

				resultMap.put("result",checkJSON);
			} catch (Exception e) {
				logger.error("合约计划用户资格查询异常：",e);
				resultMap.put("resultCode","fail");
				resultMap.put("resultInfo","当前未查询到您的合约办理资格情况,请稍后再试!");
				splitException(e, "合约办理资格校验返回："+net.sf.json.JSONObject.fromObject(checkMap).toString());
			}
		}
		return resultMap;
	}

	/**
	 *
	 *  查询所有优惠
	 * 验证所选套餐是否为已订购的
	 * @return
	 */
	private  boolean checkPlan(String SERIAL_NUMBER, String elementId)throws Exception{
		Map<String, Object> ajaxData = new HashMap<String,Object>();
		HqGetUserAllDiscntCondition hqGetUserAllDiscntCondition = new HqGetUserAllDiscntCondition();
		hqGetUserAllDiscntCondition.setSerialNumber(SERIAL_NUMBER);
		hqGetUserAllDiscntCondition.setRemoveTag("0");//销号标志
		hqGetUserAllDiscntCondition.setxGetmode(1);//1查询用户当前正在使用的优惠 2，预约的优惠 3，两种优惠都查询出来

		OfferBalanceCondition condition = new OfferBalanceCondition();
		condition.setSerialNumber(SERIAL_NUMBER);
		Map<String,Object> allDiscntResult =
				flowServeService.queryDiscnt(hqGetUserAllDiscntCondition);
		if(!HNanConstant.SUCCESS.equals(allDiscntResult.get("respCode"))|| MapUtils.isEmpty(allDiscntResult)){
			ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
			throw new ServiceException(HNanConstant.FAIL, allDiscntResult.get("respDesc").toString(), null);
		}
		boolean flag=false;
		JSONArray resultArr = (JSONArray) allDiscntResult.get("result");
		for(int i=0;i<resultArr.size();i++) {
			JSONObject obj = resultArr.getJSONObject(i);
			String code = obj.getString("DISCNT_CODE");//优惠名称
			if(code.equals(elementId)){
				flag=true;
				break;
			}
		}
		return flag;
	}
	/**
	 * 查询购前须知或购机协议
	 * @param commonPropagandaCode
	 * @return
	 */
	@RequestMapping("/queryCommonPropaganda")
	@ResponseBody
	public CommonPropaganda queryCommonPropaganda(String commonPropagandaCode){
		try {
			CommonPropaganda commonPropaganda = commonPropagandaService.getCommonPropagandaByCode(commonPropagandaCode,CommonParams.CHANNEL_CODE);
			return commonPropaganda;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Map<String, String> parseParams(String params) {
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
	
	@ResponseBody
	@RequestMapping("chooseMarketRedis")
	public Map<String,Object> chooseMarketRedis(HttpServletRequest request, HttpServletResponse response, Model model){
		 String ip = request.getHeader("x-forwarded-for");
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("WL-Proxy-Client-IP");
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getRemoteAddr();
		    }
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String  marketIds = request.getParameter("marketIds");
		String oper = request.getParameter("oper");
		logger.info("chooseMarketRedis   ip:"+ip+",oper="+oper+",marketIds="+marketIds);
		if(StringUtils.isEmpty(marketIds)){
			resultMap.put("code", "-1");
			resultMap.put("message", "参数不对");
			return resultMap;
		}
		String   activityIdAllListStr =  (String)JedisClusterUtils.getObject("ACTIVITY_ID_ALL_LIST");
		if(StringUtils.isEmpty(activityIdAllListStr)){
			resultMap.put("code", "-1");
			resultMap.put("message", "redis里面没有值");
		}
		if("del".equals(oper)){
			List<String>  activityIdAllList = (List<String>)JSONObject.parse(activityIdAllListStr);
			for(String marketId : marketIds.split(",")){
				activityIdAllList.remove(marketId);
				String preStr = "ACTIVITY_DATA_"+marketId;
				JedisClusterUtils.delObject(preStr);
			}
			JedisClusterUtils.delObject("ACTIVITY_ID_ALL_LIST");
			JedisClusterUtils.setObject("ACTIVITY_ID_ALL_LIST", JSONArray.toJSONString(activityIdAllList),86400);
			JedisClusterUtils.expires("ACTIVITY_ID_ALL_LIST",86400*7);
			resultMap.put("code", "0");
			resultMap.put("message", "删除成功");
		}
		else if("update".equals(oper)){
			//清空活动对象
			List<String>  activityIdAllList = (List<String>)JSONObject.parse(activityIdAllListStr);
			for(String marketId : activityIdAllList){
				JedisClusterUtils.delObject("ACTIVITY_DATA_"+marketId);
			}
			//清空活动ID
			JedisClusterUtils.delObject("ACTIVITY_ID_ALL_LIST");
			
			//添加活动
			activityIdAllList = Lists.newArrayList();
			for(String marketId : marketIds.split(",")){
				activityIdAllList.add(marketId);
				MarketInfo marketParam = new MarketInfo();
				marketParam.setMarketId(Long.parseLong(marketId));
				MarketInfo marketInfo = marketService.queryMarket(marketParam);
				JedisClusterUtils.setObject("ACTIVITY_DATA_"+marketId, JSONObject.toJSONString(marketInfo), 86400);
				JedisClusterUtils.expires("ACTIVITY_DATA_"+marketId,86400*7);
			}
			JedisClusterUtils.setObject("ACTIVITY_ID_ALL_LIST",JSONObject.toJSONString(activityIdAllList),86400);
			JedisClusterUtils.expires("ACTIVITY_ID_ALL_LIST",86400*7);
			resultMap.put("code", "0");
			resultMap.put("message", "修改成功");
			
		}
		else if("show".equals(oper)){
			 String marketId = marketIds.split(",")[0];
			 String marketInfoStr =  String.valueOf(JedisClusterUtils.getObject("ACTIVITY_DATA_"+marketId));	
			 resultMap.put("code", "0");
			 resultMap.put("activityIdAllListStr", activityIdAllListStr);
			 resultMap.put("marketInfoStr", marketInfoStr);
		}
		return resultMap;
	}
	
	
	@RequestMapping(value = "sendRandomCode")
	@ResponseBody
	public Map<String, Object> sendRandomCode(HttpServletRequest request, String mobile) throws Exception {
		String code = "";
		Map<String, Object> map = new HashMap<String, Object>();		
		 		
		BasicInfoCondition condition = new BasicInfoCondition();
    	condition.setSerialNumber(mobile);
    	condition.setxGetMode("0"); 
    	condition.setRunningId(String.valueOf(request.getAttribute("runningId")));
    	Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(condition);	         	 
    	JSONObject phoneAttributionJSON = JSONObject.parseArray(userInfo.get("result").toString()).getJSONObject(0);
		// logger.info(JSON.toJSONString(userInfo,true));
		 String phoneNumberResultcode = phoneAttributionJSON.getString("X_RESULTCODE"); 
		if(!"0".equals(phoneNumberResultcode)){		 
			map.put("resultCode", "fail");
			map.put("resultInfo", "非湖南移动用户不能做合约担保！");
			return map;
		} 
		
	
		if (jedisCluster.exists("BargainSmsSimMaxBomb_" + mobile)) {//判断5分钟内是否发送过短信			 
			map.put("resultCode", "fail");
			map.put("resultInfo", "您的短信密码已发送到手机，5分钟内不重复发送");
		} else {
			SmsSend4AllChanCondition smsSend4AllChanCondition = new SmsSend4AllChanCondition();
			smsSend4AllChanCondition.setSERIAL_NUMBER(mobile);
			smsSend4AllChanCondition.setCacheKey(mobile + "BargainSms_SMS_SHOP");
			//允许用户输入错误三次
			smsSend4AllChanCondition.setErrorTimes(3);
			smsSend4AllChanCondition.setCodeLength(6);
			smsSend4AllChanCondition.setNOTICE_CONTENT("湖南移动提醒你：您本次参加老客户孝心机活动验证码为：{$}");
			map = smsSendService.sendSms4AllChan(smsSend4AllChanCondition);
			code = JedisClusterUtils.get(mobile + "BargainSms_SMS_SHOP");
			if (MapUtils.isNotEmpty(map) && "0".equals(map.get("X_RESULTCODE"))) {
				jedisCluster.set("BargainSmsSimMaxBomb_" + mobile, "1");
				jedisCluster.expire("BargainSmsSimMaxBomb_" + mobile, 300);//保持5分钟
			}
			map.put("resultCode", "0");
			map.put("resultInfo", "孝心机活动验证码，下发成功请注意查收!");
		}
		logger.error("发送随机短信密码，号码：" + mobile + "，code:" + map.get("X_RESULTCODE") + ",info:" + map.get("X_RESULTINFO") + "随机码：" + code);
		return map;
	}

	/**
	 * 校验验证码
	 * @param smsCode
	 * @return Map<String,Object>
	 * @throws Exception
	 */
	@RequestMapping(value="checkCode")
	@ResponseBody
	public Map<String,Object> checkCode(String phoneNo,String smsCode,HttpServletRequest request)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String flag = request.getParameter("flag");
		if(!(StringUtils.isNotEmpty(flag) && Integer.parseInt(flag)==0)){
			if(StringUtils.isEmpty( JedisClusterUtils.get(phoneNo+"BargainSms_SMS_SHOP"))){				 
				map.put("resultCode", "fail");
				map.put("resultInfo", "请先发送验证码");
				return map;
			}
		}
		SmsSend4AllChanCondition smsSend4AllChanCondition = new SmsSend4AllChanCondition();
		smsSend4AllChanCondition.setSERIAL_NUMBER(phoneNo);
		smsSend4AllChanCondition.setCodeValue(smsCode);
		smsSend4AllChanCondition.setCacheKey(phoneNo+"BargainSms_SMS_SHOP");
		Map<String, Object> smsResult = smsSendService.CheckSms4AllChan(smsSend4AllChanCondition); 
		if(!("0".equals(smsResult.get("X_RESULTCODE")))){
			map.put("runningId", smsResult.get("runningId"));
			map.put("resultCode", "fail");
			map.put("resultInfo", smsResult.get("message"));
			logger.error("校验短信验证码，号码：" + phoneNo + "，code:" + map.get("X_RESULTCODE") + ",info:" + map.get("X_RESULTINFO"));
			return map;
		} 		
		JedisClusterUtils.del(phoneNo + "BargainSms_SMS_SHOP");
	 
		//担保资格校验
		 ContractSaleCheckSureCondition  contractSaleCheckSureCondition = new ContractSaleCheckSureCondition();
		contractSaleCheckSureCondition.setSerialNumber(phoneNo);
		contractSaleCheckSureCondition.setCheckType("01");
		contractSaleCheckSureCondition.setProductTypeCode("10");
		map = goodsService.contractSaleCheckSure(contractSaleCheckSureCondition);
		logger.error("校验短信验证码，号码："+phoneNo);

		if(map.size()>0){		 
			return map;
		}
		else{			
			Session session=UserUtils.getSession();
		 	session.setAttribute("sureNo_99Buy",phoneNo);
			map.put("resultCode","0");
			map.put("resultInfo","该号码可以做老客户孝心机的担保号码！");	
			return map;
		} 
	}
	
}
