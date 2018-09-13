package com.ai.ecs.ecm.mall.wap.modules.goods;

import com.ai.ecs.common.framework.exception.ServiceException;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.web.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.ecm.mall.wap.platform.utils.BusiLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqChangeProdAndElemCondition;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqQueryFeeCondition;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqUserAllDiscntAndAvailableProductCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.api.IMemberMarketSignInService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.TfMemberMarketSignIn;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderSubDetailBusiParam;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.ai.ecs.sales.entity.MarketInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 流量套餐办理,除了4G自选套餐
 * @author zhanggj
 *
 */
@Controller
@RequestMapping("flow")
public class ShopFlowServerController extends BaseController {

	private Logger log = Logger.getLogger(ShopFlowServerController.class);
	
	 @Autowired
	 public FlowServeService flowServeService;
	 
	 @Autowired
	 IOrderService orderService;
	 
	 @Autowired
	 IOrderQueryService  orderQueryService;

	 @Autowired
	 GoodsService goodsService;

	 @Autowired
	 IMemberMarketSignInService signInService;

	 /**
     * 产品(元素)变更
     */
    HqChangeProdAndElemCondition hqChangeProdAndElemCondition = new HqChangeProdAndElemCondition();
	    
	 /**
     * 查询用户产品是否开通与有可变更
     */
	 HqUserAllDiscntAndAvailableProductCondition hqUserAndProductCondition = new HqUserAllDiscntAndAvailableProductCondition();
	 
	/**
      * 提交订单
      * @param request
      * @param response
      * @param model
      * @return
      * @throws Exception
      */
	 @RequestMapping("/submitFlowOrder")
     public String orderDealWith(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes)throws Exception{
        
			Map<String,Object> map = new HashMap<String,Object>();
	    	
	    	//前台request的数据
			Long goodsSkuId = Long.parseLong(request.getParameter("goodsSkuId"));
			Long goodsSkuPrice = Long.parseLong(request.getParameter("goodsSkuPrice"));
			String goodsId = request.getParameter("goodsId");
			String goodsUrl = request.getParameter("goodsUrl");
			String goodsName = request.getParameter("goodsName");
			String goodsFormat = request.getParameter("goodsFormat");
			String elementId = request.getParameter("elementId");
			String orderSubmitType = request.getParameter("orderSubmitType");
			String productId = "";
			if(request.getParameter("ProductId") != null){
				productId = request.getParameter("productId");
			}
			String busiType = request.getParameter("busiType");
		    String shopId = request.getParameter("shopId");
		    String shopName = request.getParameter("shopName");
		    String shopTypeId = request.getParameter("shopTypeId");
		    
	    	try {
			 	MemberVo memberVo = UserUtils.getLoginUser(request);
		 	
		 		
				map.put("memberPhone", memberVo.getMemberLogin().getMemberLogingName());
				TfOrderSub orderSub = new TfOrderSub();
				
				 // 订单会员关联
		        TfOrderUserRef orderUserRef = new TfOrderUserRef();
				orderUserRef.setMemberId(memberVo.getMemberLogin().getMemberId());
		        orderUserRef.setMemberLogingName(memberVo.getMemberLogin().getMemberLogingName());
		        orderUserRef.setContactPhone(Long.toString(memberVo.getMemberLogin().getMemberPhone()));
		        orderSub.setOrderUserRef(orderUserRef);
				
		 		/*设置订单明细信息*/
				TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
				
				orderSubDetail.setGoodsId(Long.parseLong(goodsId));
				orderSubDetail.setGoodsSkuId(goodsSkuId);
				orderSubDetail.setGoodsName(goodsName);
				orderSubDetail.setGoodsSkuPrice(goodsSkuPrice);
				orderSubDetail.setGoodsSkuNum(1L);
				orderSubDetail.setRootCateId(OrderConstant.CATE_TRAFFIC);
				orderSubDetail.setGoodsFormat(goodsFormat);
				orderSubDetail.setShopId(Long.parseLong(shopId));
				orderSubDetail.setShopName(shopName);
				orderSubDetail.setShopTypeId(Integer.parseInt(shopTypeId));        
				orderSubDetail.setGoodsImgUrl(goodsUrl);
				
				//业务参数
				List<TfOrderSubDetailBusiParam> busiParamList = new ArrayList<TfOrderSubDetailBusiParam>();
				TfOrderSubDetailBusiParam param = new TfOrderSubDetailBusiParam();
				param.setSkuBusiParamName("elementId");
				param.setSkuBusiParamValue(elementId);
				param.setSkuBusiParamDesc("");
				
				param.setSkuBusiParamName("busiType");
				param.setSkuBusiParamValue(busiType);
				param.setSkuBusiParamDesc("");

				if(StringUtils.isNoneBlank(productId)){
					param.setSkuBusiParamName("productId");
					param.setSkuBusiParamValue(productId);
					param.setSkuBusiParamDesc("");
				}
				
				busiParamList.add(param);
				orderSubDetail.setOrderSubDetailBusiParams(busiParamList);
				orderSub.addOrderDetail(orderSubDetail);
	
				/*设置订单信息*/
				long orderSubAmount = goodsSkuPrice;
				Date upOrderTime = new Date();
				orderSub.setShopId(Long.parseLong(shopId));//TODO 平台级店铺，暂时设置1
				orderSub.setShopName(shopName);//
				if("TYPE_TRAFFIC".equals(orderSubmitType)){
					orderSub.setOrderTypeId(OrderConstant.TYPE_TRAFFIC);    //---流量包订单
				}else if("TYPE_SMS".equals(orderSubmitType)){
					orderSub.setOrderTypeId(OrderConstant.TYPE_SMS);    //---短信包订单
				}
				orderSub.setOrderSubAmount(orderSubAmount);//总额
				orderSub.setOrderSubPayAmount(orderSubAmount);//支付金额
				orderSub.setOrderSubDiscountAmount(0L);//优惠金额
				orderSub.setOrderSubPayStatus("Y");//支付状态：已支付
				orderSub.setOrderStatusId(12);//订单状态：已归档，完成
				orderSub.setOrderChannelCode("E007");//渠道编码，手机商城
				orderSub.setPayModeId(2);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
				orderSub.setDeliveryModeId(3);//配送方式，虚拟商品，不需要配送
				orderSub.setIsInvoicing(0);//是否开发票:0-不开
				orderSub.setOrderTime(upOrderTime);
				
				//生成订单
				try {
					List<TfOrderSub> orderSublist= orderService.newOrder(orderSub);
					for(int i=0;i<orderSublist.size();i++)
					{
						Date orderTime = orderSublist.get(i).getOrderTime();
						map.put("orderTime", orderTime);
						String orderNos = orderSublist.get(i).getOrderSubNo();
						map.put("orderNos", orderNos);
						String GoodsFormat = orderSublist.get(i).getDetailList().get(0).getGoodsFormat();
						map.put("GoodsFormat", GoodsFormat);
						Long payAmount = orderSublist.get(i).getOrderSubPayAmount();
						map.put("payAmount", BigDecimal.valueOf(Long.valueOf(payAmount)).divide(new BigDecimal(100)).toString());
					}
				    } catch (Exception e) {
						log.error("生成流量包订单失败", e);
						//后续需确定的，是否要记录日志
				    }
			    }catch(Exception e)
			    {
			    	log.error(e);
			    	//return "web/goods/flow/orderError";
			    	//后续需确定的，是否要记录日志
			    }
	    	    
	    	    redirectAttributes.addFlashAttribute("memberPhone",map.get("memberPhone"));
	    	    redirectAttributes.addFlashAttribute("orderTime",map.get("orderTime"));
	    	    redirectAttributes.addFlashAttribute("orderNos",map.get("orderNos"));
	    	    redirectAttributes.addFlashAttribute("GoodsFormat",map.get("GoodsFormat"));
	    	    redirectAttributes.addFlashAttribute("goodsName",goodsName);
	    	    redirectAttributes.addFlashAttribute("payAmount", map.get("payAmount"));
	    	    
	    	    redirectAttributes.addAttribute("orderNos", map.get("orderNos"));
	    	    return "redirect:" + "gotoSuccessPage";
	    }
	 
	 /**
	  * 跳转到成功页面
	  * @param request
	  * @param model
	  * @return
	  */
	 @RequestMapping(value="gotoSuccessPage")
	  public String gotoSuccessPage(HttpServletRequest request,Model model){
		 
		   String orderNos = null;
		   TfOrderSub orderSubInfo = null;
		   Map<String,Object> map = (Map<String, Object>) RequestContextUtils.getInputFlashMap(request);
		   if(map == null){   // 重新刷新页面
			   map = new HashMap<String,Object>();
			   orderNos = request.getParameter("orderNos");
			   TfOrderSub orderSub = new TfOrderSub();
			   orderSub.setOrderSubNo(orderNos);
			   orderSubInfo = orderQueryService.queryComplexOrder(orderSub);
			   Date orderTime = orderSubInfo.getOrderTime();
			   Long payAmount = orderSubInfo.getOrderSubPayAmount();
			  
			   map.put("orderTime", orderTime);
			   map.put("orderNos",orderNos);
			   map.put("payAmount", BigDecimal.valueOf(Long.valueOf(payAmount)).divide(new BigDecimal(100)).toString());
			   map.put("GoodsFormat", orderSubInfo.getDetailList().get(0).getGoodsFormat());
			   map.put("goodsName", orderSubInfo.getDetailList().get(0).getGoodsName());
			   MemberVo memberVo = UserUtils.getLoginUser(request);
			   map.put("memberPhone", memberVo.getMemberLogin().getMemberPhone());
			   model.addAttribute("orderSub", map);
			   return  "web/goods/flow/orderSuccess";
		   }
		   model.addAttribute("orderSub", map);
		   return  "web/goods/flow/orderSuccess";
	  }
	 
	 

	/**
	     * 
	     * handleFlowBusi 流量套餐办理,取消，更改
	     * @param request
	     * @return
	     * @throws Exception
	     * @return Map<String,Object>返回说明
	     * @Exception 异常说明
	     * @author：zhangqd3@asiainfo.com
	     * @create：2016年3月14日 上午11:27:04 
	     * @moduser： 
	     * @moddate：
	     * @remark：
	     */
	    @SuppressWarnings("unchecked")
		@RequestMapping(value="handleFlowBusi")
	    @ResponseBody
	    public Map<String, Object> handleFlowBusi(HttpServletRequest request,Model model,RedirectAttributes redirectAttributes)throws Exception{
	      	
	    	Map<String, Object> ajaxData = new HashMap<String, Object>();
	    	try {
				MemberVo memberVo = UserUtils.getLoginUser(request);
				//String serialNumber = "15802621270";
				String serialNumber;
				if (memberVo == null) {
					ajaxData.put("isLogin", "1");
					return ajaxData;
				} else {
					serialNumber = memberVo.getMemberLogin().getMemberLogingName();
				}
				MemberLogin memberLogin = memberVo.getMemberLogin();
				MemberSsoVo memberSsoVo = UserUtils.getSsoLoginUser(request,null);
				String eparchyCode = memberSsoVo.getEparchyCode();
				String elementId = "";
				String productId = "";
				String busiType = "";
				String marketId = "";
				String goodsId = "";
				//前台request的数据
				if (request.getParameter("elementId") != null && request.getParameter("elementId") != "") {
					elementId = request.getParameter("elementId");
					String prefixEparchyCode = eparchyCode.substring(eparchyCode.length() - 2);
					elementId = elementId.replace("**", prefixEparchyCode);
				}
				if (request.getParameter("productId") != null && request.getParameter("productId") != "") {
					productId = request.getParameter("productId");
				}
				if (request.getParameter("busiType") != null && request.getParameter("busiType") != "") {
					busiType = request.getParameter("busiType");
				}
				if (request.getParameter("marketId") != null && request.getParameter("marketId") != "") {
					marketId = request.getParameter("marketId");
				}
				if (request.getParameter("goodsId") != null && request.getParameter("goodsId") != "") {
					goodsId = request.getParameter("goodsId");
				}
				String goodsSkuId = request.getParameter("goodsSkuId");
				BusiLogUtils.writerLogging(request,"handleFlowBusi",request.getParameter("goodsName"),goodsId,"","",",","","E007",null,"1",null);
				//秒杀资格校验
				if (StringUtils.isNoneBlank(marketId)) {
					TfMemberMarketSignIn signIn = new TfMemberMarketSignIn();
					signIn.setMarketId(Long.valueOf(marketId));
					signIn.setMemberId(memberLogin.getMemberId());
					signIn.setPhoneNumber(memberLogin.getMemberLogingName());
					boolean hasQualified = signInService.verifySecKillQualified(signIn);
					if (!hasQualified) {
						ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
						ajaxData.put(HNanConstant.resultInfo, "没有活动秒杀资格");
						return ajaxData;
					}
				}

				Long memberId = memberVo.getMemberLogin().getMemberId();
				if (StringUtils.isNoneBlank(marketId)) {
					String memberBuyNumStr = JedisClusterUtils.get("ACTIVITY_GOODS_USER_" + marketId + "_" + memberId);
					String marketInfoJSON = String.valueOf(JedisClusterUtils.getObject("ACTIVITY_DATA_" + marketId));
					MarketInfo marketInfo = JSONObject.parseObject(marketInfoJSON, MarketInfo.class);
					if (marketInfo != null) {
						if(null==memberBuyNumStr){
							memberBuyNumStr="0";
						}
						Integer memberBuyNumInt = Integer.valueOf(memberBuyNumStr);
						Integer goodsMaxNum = marketInfo.getGoodsMaxNum();
						if(memberBuyNumInt.intValue() > goodsMaxNum.intValue()) {
							ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
							ajaxData.put(HNanConstant.resultInfo, "超出活动购买限制数量");
							return ajaxData;
						}

						String marketStartTime = DateUtils.formatDateTime(marketInfo.getMarketStarttime());
						if(goodsService.isAfterCurrentDate(marketStartTime)){
							ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
							ajaxData.put(HNanConstant.resultInfo, "活动未开始");
							return ajaxData;
						}

						String marketEndTime = DateUtils.formatDateTime(marketInfo.getMarketEndtime());
						if(!goodsService.isAfterCurrentDate(marketEndTime)){
							ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
							ajaxData.put(HNanConstant.resultInfo, "活动已结束");
							return ajaxData;
						}

					} else {
						ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
						ajaxData.put(HNanConstant.resultInfo, "活动已过期");
						return ajaxData;
					}
				} else {
					ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
					ajaxData.put(HNanConstant.resultInfo, "请从活动页面进入!");
					return ajaxData;
				}
				//扣减活动库存
				boolean b = goodsService.updateMarketStock(Long.valueOf(marketId), Long.parseLong(goodsSkuId), Long.valueOf(1));
				if (!b) {
					ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
					ajaxData.put(HNanConstant.resultInfo, "已被抢完!");
					return ajaxData;
				}

				hqChangeProdAndElemCondition.setMODIFY_TAG("0");//变更标记：0-增加，1-删除，2-修改
				hqChangeProdAndElemCondition.setRSRV_STR1("1");
				hqChangeProdAndElemCondition.setSERIAL_NUMBER(serialNumber);
				hqChangeProdAndElemCondition.setDISCNT(elementId);
				hqChangeProdAndElemCondition.setELEMENT_ID(elementId);
				if (StringUtils.isNotBlank(productId)) {
					hqChangeProdAndElemCondition.setPRODUCT_ID(productId);
				}

				hqChangeProdAndElemCondition.setREMOVE_TAG("0");

				if ("openBusi".equals(busiType)) {
					String str[]=elementId.split(",");
					if(str.length==2) {
						hqChangeProdAndElemCondition.setMODIFY_TAG("0,0");//变更标记：0-增加，1-删除，2-修改
						hqChangeProdAndElemCondition.setRSRV_STR1("0,0");//变更标记：0-增加，1-删除，2-修改
						hqChangeProdAndElemCondition.setNUM(2);
						hqChangeProdAndElemCondition.setELEMENT_TYPE_CODE("D,D");
					}else{
						hqChangeProdAndElemCondition.setMODIFY_TAG("0");//变更标记：0-增加，1-删除，2-修改
						hqChangeProdAndElemCondition.setRSRV_STR1("0");//变更标记：0-增加，1-删除，2-修改
						hqChangeProdAndElemCondition.setNUM(1);
						hqChangeProdAndElemCondition.setELEMENT_TYPE_CODE("D");
					}
				} else if ("cancelBusi".equals(busiType)) {
					hqChangeProdAndElemCondition.setMODIFY_TAG("1");//变更标记：0-增加，1-删除，2-修改
				}
				//因商城工号没有权限，暂用网厅工号办理
				hqChangeProdAndElemCondition.setStaffId("ITFWTNNN");
				hqChangeProdAndElemCondition.setTradeDepartPassword("909880");
				Map resultMap = flowServeService.changeProdAndElem(hqChangeProdAndElemCondition);
				//办理结果
				if (!MapUtils.isEmpty(resultMap) && HNanConstant.SUCCESS.equals(resultMap.get("respCode"))) {
					ajaxData.put(HNanConstant.resultCode, HNanConstant.SUCCESS);

					//减少活动秒杀机会
					List<TfUserGoodsCar> cars = Lists.newArrayList();
					TfUserGoodsCar car = new TfUserGoodsCar();
					car.setMarketId(Long.valueOf(marketId));
					cars.add(car);
					goodsService.decreaseSecKillChance(memberId, cars);
					//更新会员已抢购数量、已抢购数量
					goodsService.updateMarketBuyNum(cars, memberId);
					flowOrder(request, redirectAttributes,ajaxData);
				} else {
					ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
					ajaxData.put(HNanConstant.resultInfo, resultMap.get("respDesc"));
				}
			}catch (Exception e){
				ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
				ajaxData.put(HNanConstant.resultInfo, "系统繁忙，办理失败！");
				logger.error("handleFlowBusi:",e);
			}
	    	return ajaxData;
		}
	private void flowOrder(HttpServletRequest request, RedirectAttributes redirectAttributes,Map ajaxData){
		Map<String,Object> map = new HashMap<String,Object>();

		//前台request的数据
		Long goodsSkuId = Long.parseLong(request.getParameter("goodsSkuId"));
		Long goodsSkuPrice = Long.parseLong(request.getParameter("goodsSkuPrice"))*100;
		String goodsId = request.getParameter("goodsId");
		String goodsUrl = request.getParameter("goodsUrl");
		String goodsName = request.getParameter("goodsName");
		String goodsFormat = request.getParameter("goodsFormat");
		String elementId = request.getParameter("elementId");
		String orderSubmitType = request.getParameter("orderSubmitType");
		String productId = "";
		if(request.getParameter("ProductId") != null){
			productId = request.getParameter("productId");
		}
		String busiType = request.getParameter("busiType");
		String shopId = request.getParameter("shopId");
		String shopName = request.getParameter("shopName");
		String shopTypeId = request.getParameter("shopTypeId");

		try {
			MemberVo memberVo = UserUtils.getLoginUser(request);


			map.put("memberPhone", memberVo.getMemberLogin().getMemberLogingName());
			TfOrderSub orderSub = new TfOrderSub();

			// 订单会员关联
			TfOrderUserRef orderUserRef = new TfOrderUserRef();
			orderUserRef.setMemberId(memberVo.getMemberLogin().getMemberId());
			orderUserRef.setMemberLogingName(memberVo.getMemberLogin().getMemberLogingName());
			orderUserRef.setContactPhone(Long.toString(memberVo.getMemberLogin().getMemberPhone()));
			orderSub.setOrderUserRef(orderUserRef);

		 		/*设置订单明细信息*/
			TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();

			orderSubDetail.setGoodsId(Long.parseLong(goodsId));
			orderSubDetail.setGoodsSkuId(goodsSkuId);
			orderSubDetail.setGoodsName(goodsName);
			orderSubDetail.setGoodsSkuPrice(goodsSkuPrice);
			orderSubDetail.setGoodsSkuNum(1L);
			orderSubDetail.setRootCateId(OrderConstant.CATE_TRAFFIC);
			orderSubDetail.setGoodsFormat(goodsFormat);
			orderSubDetail.setShopId(Long.parseLong(shopId));
			orderSubDetail.setShopName(shopName);
			orderSubDetail.setShopTypeId(Integer.parseInt(shopTypeId));
			orderSubDetail.setGoodsImgUrl(goodsUrl);

			//业务参数
			List<TfOrderSubDetailBusiParam> busiParamList = new ArrayList<TfOrderSubDetailBusiParam>();
			TfOrderSubDetailBusiParam param = new TfOrderSubDetailBusiParam();
			param.setSkuBusiParamName("elementId");
			param.setSkuBusiParamValue(elementId);
			param.setSkuBusiParamDesc("");

			param.setSkuBusiParamName("busiType");
			param.setSkuBusiParamValue(busiType);
			param.setSkuBusiParamDesc("");

			if(StringUtils.isNoneBlank(productId)){
				param.setSkuBusiParamName("productId");
				param.setSkuBusiParamValue(productId);
				param.setSkuBusiParamDesc("");
			}

			busiParamList.add(param);
			orderSubDetail.setOrderSubDetailBusiParams(busiParamList);
			orderSub.addOrderDetail(orderSubDetail);

				/*设置订单信息*/
			long orderSubAmount = goodsSkuPrice;
			Date upOrderTime = new Date();
			orderSub.setShopId(Long.parseLong(shopId));//TODO 平台级店铺，暂时设置1
			orderSub.setShopName(shopName);//
			if("TYPE_TRAFFIC".equals(orderSubmitType)){
				orderSub.setOrderTypeId(OrderConstant.TYPE_TRAFFIC);    //---流量包订单
			}else if("TYPE_SMS".equals(orderSubmitType)){
				orderSub.setOrderTypeId(OrderConstant.TYPE_SMS);    //---短信包订单
			}
			orderSub.setOrderSubAmount(orderSubAmount);//总额
			orderSub.setOrderSubPayAmount(orderSubAmount);//支付金额
			orderSub.setOrderSubDiscountAmount(0L);//优惠金额
			orderSub.setOrderSubPayStatus("Y");//支付状态：已支付
			orderSub.setOrderStatusId(12);//订单状态：已归档，完成
			orderSub.setOrderChannelCode("E007");//渠道编码，手机商城
			orderSub.setPayModeId(2);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
			orderSub.setDeliveryModeId(3);//配送方式，虚拟商品，不需要配送
			orderSub.setIsInvoicing(0);//是否开发票:0-不开
			orderSub.setOrderTime(upOrderTime);

			//生成订单
			try {
				List<TfOrderSub> orderSublist= orderService.newOrder(orderSub);
				for(int i=0;i<orderSublist.size();i++)
				{
					Date orderTime = orderSublist.get(i).getOrderTime();
					map.put("orderTime", orderTime);
					String orderNos = orderSublist.get(i).getOrderSubNo();
					map.put("orderNos", orderNos);
					String GoodsFormat = orderSublist.get(i).getDetailList().get(0).getGoodsFormat();
					map.put("GoodsFormat", GoodsFormat);
					Long payAmount = orderSublist.get(i).getOrderSubPayAmount();
					map.put("payAmount", BigDecimal.valueOf(Long.valueOf(payAmount)).divide(new BigDecimal(100)).toString());
				}
			} catch (Exception e) {
				log.error("生成流量包订单失败", e);
				//后续需确定的，是否要记录日志
			}
		}catch(Exception e)
		{
			log.error(e);
			//return "web/goods/flow/orderError";
			//后续需确定的，是否要记录日志
		}

		ajaxData.put("memberPhone",map.get("memberPhone"));
		ajaxData.put("orderTime",map.get("orderTime"));
		ajaxData.put("orderNos",map.get("orderNos"));
		ajaxData.put("GoodsFormat",map.get("GoodsFormat"));
		ajaxData.put("goodsName",goodsName);
		ajaxData.put("payAmount", map.get("payAmount"));

		ajaxData.put("orderNos", map.get("orderNos"));
	}
	 /**
	  * 查询余额和查询用户产品是否开通与有可变更
	  * @param request
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value="queryFlowInfos")
	 @ResponseBody
	 public Map<String, Object> initFlowInfos(HttpServletRequest request)throws Exception{
		 Map<String, Object> ajaxData = new HashMap<String, Object>();
		 
		 String elementId = request.getParameter("elementId");
		 //String elementId = "30517924";
		 MemberVo memberVo = UserUtils.getLoginUser(request);
		/** MemberVo memberVo = new MemberVo();
		 MemberLogin login = new MemberLogin();
		 login.setMemberPhone(15802621270L); //13617486389L 
		 memberVo.setMemberLogin(login);*/
		 String serialNumber = null;
		 if(memberVo != null){
			 serialNumber = memberVo.getMemberLogin().getMemberLogingName();
		 }else if(memberVo == null){
			 ajaxData.put("isLogin", "1");  //未登录
			 return ajaxData;
		 }
    	 try {
    		 //查询余额
	    	 //queryFee(ajaxData, serialNumber);
    		 //查询用户产品是否开通与有可变更
    		 queryUserAndProduct(request,serialNumber, elementId,ajaxData);
    	  } catch (ServiceException e) {
			 return ajaxData;
		 
	    }
		 return ajaxData;
	   }
	   
	   /**
	    * 跳转到订购失败页面
	    * @param request
	    * @return
	    */
	   @RequestMapping(value="gotoFailPage")
	   public String gotoFailPage(HttpServletRequest request,Model model){
		   String respDesc = request.getParameter("respDesc");
		   String goodsName = request.getParameter("goodsName");
		   model.addAttribute("respDesc", respDesc);
		   model.addAttribute("goodsName", goodsName);
		   return  "web/goods/flow/orderError";
	   }
	   /**
		 * 
		 * queryUserAndProduct 查询用户产品是否开通与有可变更
		 * @return
		 * @return Map返回说明
		 * @Exception 异常说明
		 * @author：zhangqd3@asiainfo.com
		 * @create：2016年4月1日 下午3:28:48 
		 * @moduser： 
		 * @moddate：
		 * @remark：
		 */
		@SuppressWarnings("unchecked")
		public void queryUserAndProduct(HttpServletRequest request , String serialNumber,String elementId ,Map ajaxData)throws Exception{
			MemberSsoVo memberSsoVo = UserUtils.getSsoLoginUser(request,null);
			String eparchyCode = memberSsoVo.getEparchyCode();
			String prefixEparchyCode = eparchyCode.substring(eparchyCode.length() - 2);
			elementId = elementId.replace("**", prefixEparchyCode);

	    	hqUserAndProductCondition.setSERIAL_NUMBER(serialNumber);     //电话号码
	    	hqUserAndProductCondition.setPRODUCT_MODE("00");              //产品模式: 00基本产品
	    	hqUserAndProductCondition.setREMOVE_TAG("0");                 //销号标记：  0正常用户
	    	hqUserAndProductCondition.setELEMENT_ID(elementId);
	    	//hqUserAndProductCondition.setX_ACCEPT_MODE("0");             //查询标志  查询所有为订购元素
	    	hqUserAndProductCondition.setX_GETMODE("1");                 //获取方式  
	    	hqUserAndProductCondition.setPRESENT_TIME(DateUtils.getCurrentTime());
	    	Map userAndProduct = flowServeService.checkProductStatus(hqUserAndProductCondition);
	    	if(!HNanConstant.SUCCESS.equals(userAndProduct.get("respCode"))
	    			|| MapUtils.isEmpty(userAndProduct)){
	        	ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
	        	ajaxData.put(HNanConstant.resultInfo,userAndProduct.get("respDesc")); 
	        	throw new ServiceException(HNanConstant.FAIL, userAndProduct.get("respDesc").toString(), null);
	 	    }
	    	JSONArray serveState =  JSONArray.fromObject(userAndProduct.get("result"));
	    	Map flagMap = (Map) serveState.get(0); 
	    	ajaxData.put("BookFlag", flagMap.get("BookFlag"));
	    	ajaxData.put("OpenFlag", flagMap.get("OpenFlag"));
	    	ajaxData.put("ProductFlag", flagMap.get("ProductFlag"));
	    	ajaxData.put("ProductList", flagMap.get("ProductList"));
		}
		
		/**
		 * 
		 * queryFee 查询余额
		 * @return
		 * @return String返回说明
		 * @Exception 异常说明
		 * @author：zhangqd3@asiainfo.com
		 * @create：2016年4月1日 上午11:05:09 
		 * @moduser： 
		 * @moddate：
		 * @remark：
		 */
		@SuppressWarnings("unchecked")
		public void queryFee(Map ajaxData,String SERIAL_NUMBER)throws Exception{
			
			HqQueryFeeCondition hqQueryFeeCondition = new HqQueryFeeCondition();
			hqQueryFeeCondition.setSERIAL_NUMBER(SERIAL_NUMBER);
	 	    Map userFee = flowServeService.queryFee(hqQueryFeeCondition);
	 	    if(!HNanConstant.SUCCESS.equals(userFee.get("respCode"))|| MapUtils.isEmpty(userFee)){
	 	    	ajaxData.put(HNanConstant.resultCode, HNanConstant.FAIL);
	 	     	ajaxData.put(HNanConstant.resultInfo,userFee.get("respDesc")); 
	 	     	throw new ServiceException(HNanConstant.FAIL, userFee.get("respDesc").toString(), null);
	 	    }
	 	   ajaxData.put("userFee", ((Map)(JSONArray.fromObject(userFee.get("result"))).get(0)).get("NEW_ALLLEAVE_FEE"));//当前金额
		}
}

