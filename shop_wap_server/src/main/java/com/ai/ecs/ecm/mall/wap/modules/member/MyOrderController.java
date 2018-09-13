package com.ai.ecs.ecm.mall.wap.modules.member;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.LogisticResult;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.OrderCountResult;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.ProcessResult;
import com.ai.ecs.ecm.mall.wap.platform.constants.OrderStatusConstant;
import com.ai.ecs.ecm.mall.wap.platform.utils.HttpClientUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.o2ogroupsim.entity.GetImsiNumberCondition;
import com.ai.ecs.ecsite.modules.o2ogroupsim.service.O2oSimGroupService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.exception.EcsException;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.api.IPlansService;
import com.ai.ecs.goods.entity.TfH5SimConf;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.integral.entity.UserRating;
import com.ai.ecs.integral.entity.UserRatingScore;
import com.ai.ecs.integral.service.IllegalKeyWordManager;
import com.ai.ecs.integral.service.UserRatingScoreService;
import com.ai.ecs.integral.service.UserRatingService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.entity.O2oPreOrder;
import com.ai.ecs.order.api.IOrder10085UpdateService;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderActLog;
import com.ai.ecs.order.entity.TfOrderRecipient;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.ecs.order.param.OrderProcessResult;
import com.ai.ecs.order.param.TaskAction;
import com.ai.ecs.sales.api.ICouponService;
import com.ai.ecs.sales.entity.CouponInfo;
import com.ai.iis.upp.service.IPayBankService;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("myOrder")
public class MyOrderController extends BaseController {
	@Autowired
	@Qualifier("orderQueryService")
	IOrderQueryService orderQueryService;

	@Autowired
	@Qualifier("orderService")
	IOrderService orderService;

	@Autowired
	IPayBankService payBankService;

	@Autowired
	ICouponService couponService;

	@Autowired
	@Qualifier("goodsManageService")
	IGoodsManageService goodsManageService;

	@Autowired
	@Qualifier("illegalKeyWordManagerImpl")
	IllegalKeyWordManager illegalKeyWordManagerImpl;

	@Autowired
	@Qualifier("userRatingServiceImpl")
	UserRatingService userRatingServiceImpl;

	@Autowired
	@Qualifier("dictService")
	DictService dictService;

	@Autowired
	@Qualifier("userRatingScoreServiceImpl")
	UserRatingScoreService userRatingScoreServiceImpl;
	@Autowired
	private IOrder10085UpdateService order10085UpdateService;
	private Integer pagesizedef = 10;

	private Integer pagenodef = 1;

	@Value("${logisticServerHost}")
	private String logisticServerHost;

	@Autowired
	private IPlansService plansService;

	@Autowired
	private O2oSimGroupService o2oSimGroupService;

	// 裸机配件订单状态节点展示
	private final static Integer[] ORDER_STEPS = new Integer[] {
			OrderConstant.STATUS_CREATE, OrderConstant.STATUS_AUDIT,
			OrderConstant.STATUS_DELIVERY, OrderConstant.STATUS_RATING,
			OrderConstant.STATUS_ARCHIVE };
	private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
			+ "wxyz0123456789+/" + "=";

	private boolean isHeBaoLogin() {
		Session session = UserUtils.getSession();
		String loginBaseKey = "LOGIN_BASEINFO_" + session.getId();
		String json = JedisClusterUtils.get(loginBaseKey);
		JSONObject jsonObject = JSON.parseObject(json);
		return jsonObject.getBoolean("loginFromHeApp") == null ? false : jsonObject.getBoolean("loginFromHeApp").booleanValue();
	}


	@RequestMapping(value = "/toMyAllOrderList", method = RequestMethod.GET)
	public String getMyOrderList(HttpServletRequest request,
			HttpServletResponse response, Integer pageSize, Integer pageNo,
			Integer flag) {
        String returnurl = "";
	    try{
            MemberVo member = UserUtils.getLoginUser(request);
            if (pageSize == null) {
                pageSize = pagesizedef;
            }
            if (pageNo == null) {
                pageNo = pagenodef;
            }
            if (member != null) {
                Page<TfOrderSub> page = new Page<TfOrderSub>();
                TfOrderSub orderSub = new TfOrderSub();
                //增加全部订单查询包括85订单
                orderSub.setThirdPartyId("all");
                page.setPageSize(pageSize);
                page.setPageNo(pageNo);
                orderSub.setPage(page);
                if (flag == null || flag == 0) {
                    returnurl = "web/member/myWaitPayOrder";
                } else if (flag == 1) {
                    orderSub.setOrderStatusId(OrderStatusConstant.WAITSENDGOODS
                            .getValue());
                    returnurl = "web/member/myWaitSendGoodsOrder";
                } else if (flag == 2) {
                    orderSub.setOrderStatusId(OrderStatusConstant.WAITRECEIVEGOODS
                            .getValue());
                    returnurl = "web/member/myWaitReceiveGoodsOrder";
                } else if (flag == 3) {
                    orderSub.setOrderStatusId(OrderStatusConstant.WAITCOMMENT
                            .getValue());
                    returnurl = "web/member/myWaitCommentOrder";
                }

                TfOrderUserRef orf = new TfOrderUserRef();
                orf.setMemberId(member.getMemberLogin().getMemberId());
                orderSub.setOrderUserRef(orf);
                List<String> excludeChannelCodes = new ArrayList<>();
                excludeChannelCodes.add(OrderConstant.CHANNEL_B2B);
                excludeChannelCodes.add(OrderConstant.CHANNEL_BLOC);
                orderSub.setExcludeChannelCodes(excludeChannelCodes); // 排除B2B及集团渠道
                List<Integer> excludeOrderType = new ArrayList<>();  //排除意向商品订单
                excludeOrderType.add(OrderConstant.TYPE_INTENTION_GOODS);
                orderSub.setExcludeOrderTypes(excludeOrderType);
                Page<TfOrderSub> orderPage = orderQueryService
                        .queryComplexOrderPage(orderSub, Variables.ACT_GROUP_MEMBER);
                List<TfOrderSub> orderListNew = new ArrayList<TfOrderSub>();
                if (orderPage != null
                        && !CollectionUtils.isEmpty(orderPage.getList())) {
                    for (TfOrderSub order : orderPage.getList()) {
                        List<TfOrderSubDetail> detaillist = order.getDetailList();
                        List<TfOrderSubDetail> detailListNew = wrapGoodsUrl(
                                request, detaillist);
                        order.setDetailList(detailListNew);
                        orderListNew.add(order);
                    }
                    orderPage.setList(orderListNew);
                }
                if (orderPage != null) {
                    request.setAttribute("orderviewList", orderPage);
                    // request.setAttribute("heBaoLogin", isHeBaoLogin());
                }
                TfOrderSub orderSub1 = new TfOrderSub();
                TfOrderUserRef orderUserRef = new TfOrderUserRef();
                orderUserRef.setMemberId(member.getMemberLogin().getMemberId());
                orderSub1.setOrderUserRef(orderUserRef);
                long waitPayCount = orderQueryService.queryOrderCount(orderSub1);
                orderSub1.setOrderStatusId(OrderStatusConstant.WAITSENDGOODS
                        .getValue());
                long waitSendCount = orderQueryService.queryOrderCount(orderSub1);
                orderSub1.setOrderStatusId(OrderStatusConstant.WAITRECEIVEGOODS
                        .getValue());
                long waitReceiveCount = orderQueryService
                        .queryOrderCount(orderSub1);
                orderSub1.setOrderStatusId(OrderStatusConstant.WAITCOMMENT
                        .getValue());
                long waitCommentCount = orderQueryService
                        .queryOrderCount(orderSub1);
                OrderCountResult orderCount = new OrderCountResult();
                orderCount.setMemberId(member.getMemberLogin().getMemberId());
                orderCount.setWaitPayCount(waitPayCount);
                orderCount.setWaitSendCount(waitSendCount);
                orderCount.setWaitReceiveCount(waitReceiveCount);
                orderCount.setWaitCommentCount(waitCommentCount);
                request.setAttribute("orderCount", orderCount);
                if (flag == null) {
                    request.setAttribute("flag", 0);
                } else {
                    request.setAttribute("flag", flag);
                }
            }
        } catch (Exception e){
            logger.error("",e);
			throw e;
        }
		return returnurl;
	}

	private List<TfOrderSubDetail> wrapGoodsUrl(HttpServletRequest request,
			List<TfOrderSubDetail> detaillist) {
		List<TfOrderSubDetail> detailListNew = new ArrayList<TfOrderSubDetail>();
		if (CollectionUtils.isNotEmpty(detaillist)) {
			for (TfOrderSubDetail detail : detaillist) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("containGoodsStaticInfo", true);
				map.put("containShopGoodsChannelRef", true);
				map.put("goodsSkuId", detail.getGoodsSkuId());
				map.put("chnlCode", "E007");
				List<TfGoodsInfo> goods = goodsManageService
						.queryGoodsInfoByCds(map);
				try {
					if (CollectionUtils.isNotEmpty(goods)) {
						if (CollectionUtils.isNotEmpty(goods.get(0)
								.getGoodsStaticList())
								&& StringUtils.isNotEmpty(goods.get(0)
										.getGoodsStaticList().get(0)
										.getGoodsStaticUrl())) {
							detail.setGoodsImgUrl(goods.get(0)
									.getGoodsStaticList().get(0)
									.getGoodsStaticUrl());
						}
						if (CollectionUtils.isNotEmpty(goods.get(0)
								.getGoodsStaticList())
								&& StringUtils.isNotEmpty(goods.get(0)
										.getGoodsStaticList().get(0)
										.getGoodsStaticUrl())) {
							detail.setGoodsUrl(goods.get(0)
									.getTfShopGoodsChannelRefList().get(0)
									.getGoodsUrl());
						}
					}
					detailListNew.add(detail);
				} catch (Exception e) {
					logger.error("填充商品路径失败：", e);
				}

			}

		}
		return detailListNew;

	}

	@RequestMapping(value = "/searchOrderPage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<TfOrderSub> searchOrderPage(HttpServletRequest request,
			HttpServletResponse response, Integer pageSize, Integer pageNo,
			Integer flag) {
		MemberVo member = UserUtils.getLoginUser(request);
		String returnurl = "";
		if (pageSize == null) {
			pageSize = pagesizedef;
		}
		if (pageNo == null) {
			pageNo = pagenodef;
		}
		if (member != null) {
			Page<TfOrderSub> page = new Page<TfOrderSub>();
			TfOrderSub orderSub = new TfOrderSub();
			page.setPageSize(pageSize);
			page.setPageNo(pageNo);
			orderSub.setPage(page);
			if (flag == null) {
				orderSub.setOrderStatusId(OrderStatusConstant.WAITPAY
						.getValue());
			} else if (flag == 1) {
				orderSub.setOrderStatusId(OrderStatusConstant.WAITSENDGOODS
						.getValue());
			} else if (flag == 2) {
				orderSub.setOrderStatusId(OrderStatusConstant.WAITRECEIVEGOODS
						.getValue());
			} else if (flag == 3) {
				orderSub.setOrderStatusId(OrderStatusConstant.WAITCOMMENT
						.getValue());
			}

			TfOrderUserRef orf = new TfOrderUserRef();
			orf.setMemberId(member.getMemberLogin().getMemberId());
			orderSub.setOrderUserRef(orf);
			List<String> excludeChannelCodes = new ArrayList<>();
			excludeChannelCodes.add(OrderConstant.CHANNEL_B2B);
			excludeChannelCodes.add(OrderConstant.CHANNEL_BLOC);
			orderSub.setExcludeChannelCodes(excludeChannelCodes); // 排除B2B及集团渠道
			Page<TfOrderSub> orderPage = orderQueryService
					.queryComplexOrderPage(orderSub);
			List<TfOrderSub> orderListNew = new ArrayList<TfOrderSub>();
			if (orderPage != null
					&& !CollectionUtils.isEmpty(orderPage.getList())) {
				for (TfOrderSub order : orderPage.getList()) {
					List<TfOrderSubDetail> detaillist = order.getDetailList();
					List<TfOrderSubDetail> detailListNew = wrapGoodsUrl(
							request, detaillist);
					order.setDetailList(detailListNew);
					orderListNew.add(order);
				}
				orderPage.setList(orderListNew);
			}
			if (orderPage != null) {
				return orderPage.getList();
			}
		}
		return null;
	}
	/**
	 * 85物流查询
	 * @param request
	 * @param response
	 * @param orderId
	 * @param dsOrderId
	 * @param model
	 * @return
	 */
	public static final String logisticDetail = new PropertiesLoader(
			"mall.properties").getProperty("logisticDetail");
	@RequestMapping(value = "/to10085LogisticDetail")
	public String to10085LogisticDetail(HttpServletRequest request,
										HttpServletResponse response, Long orderId, String dsOrderId,Model model){
		//查询商品
		MemberVo member = UserUtils.getLoginUser(request);
		TfOrderSub sub = new TfOrderSub();
		sub.setOrderSubId(orderId);
		TfOrderUserRef orf = new TfOrderUserRef();
		orf.setMemberId(member.getMemberLogin().getMemberId());
		sub.setOrderUserRef(orf);
		TfOrderSub orderSub = orderQueryService.queryComplexOrder(sub);
		if (orderSub != null) {
			TfOrderSub orderSubParam = new TfOrderSub();
			orderSubParam.setOrderSubId(orderId);
			orderSubParam.setOrderStatusIds(Arrays.asList(ORDER_STEPS));
			List<TfOrderActLog> logList = orderQueryService
					.queryActLogList(orderSubParam);
			orderSub.setDetailList(wrapGoodsUrl(request,
					orderSub.getDetailList()));
			model.addAttribute("orderSub", orderSub);
			model.addAttribute("logList", logList);
		}


		//首先订单查询,获取子订单号，物流查询需要
		TfOrderSub tf = new TfOrderSub();
		tf.setThirdPartyId(dsOrderId);
		List<TfOrderSub> orderSubs = orderQueryService.query10085Order(tf);
		if(orderSubs.size()==1){
			TfOrderSub tfsub = orderSubs.get(0);
			//获取子订单号
			String dsSubOrderId = tfsub.getThirdSubPartyId();
			Map map = new HashMap();
			map.put("dsOrderNo",new String[]{dsOrderId});
			map.put("subOrderNo",new String[]{dsSubOrderId});
			//发送http请求获取信息
			try{
				String result = HttpClientUtils.doGetAndGetString(logisticDetail,map);
				JSONObject json = JSONObject.parseObject(result);
				//获取returnCode;
				String returnCode = json.getString("returnCode");
				if("1".equals(returnCode)){
					//获取成功，获取数据
					JSONObject logistics = json.getJSONObject("rpsData");
					//运单号
					String trackingNo = json.getString("trackingNo");
					orderSub.setLogisticsNum(trackingNo);
					//物流公司编码
					String expressCmpNo = logistics.getString("expressCmpNo");
					String company = "";
					if("01".equals(expressCmpNo)){
						company = "顺丰";
					}else if("02".equals(expressCmpNo)){
						company = "申通";
					}else if("03".equals(expressCmpNo)){
						company = "圆通";
					}else if("04".equals(expressCmpNo)){
						company = "中通";
					}else if("05".equals(expressCmpNo)){
						company = "宅急送";
					}else if("06".equals(expressCmpNo)){
						company = "韵达";
					}else if("07".equals(expressCmpNo)){
						company = "天天";
					}else if("08".equals(expressCmpNo)){
						company = "EMS";
					}else if("09".equals(expressCmpNo)){
						company = "中诚";
					}else if("10".equals(expressCmpNo)){
						company = "汇通";
					}else if("11".equals(expressCmpNo)){
						company = "DHL";
					}else if("12".equals(expressCmpNo)){
						company = "TNT";
					}else if("13".equals(expressCmpNo)){
						company = "UPS";
					}else if("14".equals(expressCmpNo)){
						company = "联邦";
					}else if("15".equals(expressCmpNo)){
						company = "德邦";
					}
					if(orderSub!=null)
						orderSub.setLogisticsCompany(company);
					//物流状态
					String expressState = logistics.getString("expressState");
					model.addAttribute("expressState",expressState);
					//物流详情，商品流动
					JSONArray logisticsDetail = logistics.getJSONArray("logisticsDetail");
					List<LogisticResult> logisticResults = new ArrayList<>();
					//遍历取出商品流转
					for(int i=0;i<logisticsDetail.size();i++){
						LogisticResult logisticResult = new LogisticResult();
						JSONObject placeAndTime = logisticsDetail.getJSONObject(i);
						String time = placeAndTime.getString("time");//时间
						String context = placeAndTime.getString("context");//内容
						logisticResult.setTime(time);
						logisticResult.setRemark(context);
						logisticResults.add(logisticResult);
					}
					model.addAttribute("logisticList", logisticResults);
					return "web/member/myLogisticDetail";
				}else{
					return "redirect:/myOrder/toMyAllOrderList?flag=2";
				}
			}catch (Exception e){
				logger.error("to10085LogisticDetail",e);
			}

		}
		return "redirect:/myOrder/toMyAllOrderList?flag=2";
	}

	@RequestMapping(value = "/toLogistics", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String toLogistics(HttpServletRequest request,
			HttpServletResponse response, Long orderId, Model model,
			String logisticsNum) {
		MemberVo member = UserUtils.getLoginUser(request);
		TfOrderSub sub = new TfOrderSub();
		sub.setOrderSubId(orderId);
		TfOrderUserRef orf = new TfOrderUserRef();
		orf.setMemberId(member.getMemberLogin().getMemberId());
		sub.setOrderUserRef(orf);
		TfOrderSub orderSub = orderQueryService.queryComplexOrder(sub);
		if (orderSub != null) {
			TfOrderSub orderSubParam = new TfOrderSub();
			orderSubParam.setOrderSubId(orderId);
			orderSubParam.setOrderStatusIds(Arrays.asList(ORDER_STEPS));
			List<TfOrderActLog> logList = orderQueryService
					.queryActLogList(orderSubParam);
			orderSub.setDetailList(wrapGoodsUrl(request,
					orderSub.getDetailList()));
			model.addAttribute("orderSub", orderSub);
			model.addAttribute("logList", logList);
		}

		String getInfoUrl = "";
		Map<String, String[]> paramMap = new HashMap<String, String[]>();
		if ("02".equals(orderSub.getLogisticsCompany()))// 三人行物流数据请求
		{

			getInfoUrl = logisticServerHost
					+ "order_center/QueryExpressInf_Order";
			// orderId = 335730024495L;
			paramMap.put("billcode", new String[] { logisticsNum + "" });

		} else if ("01".equals(orderSub.getLogisticsCompanyCode()))// EMS物流数据请求
		{
			getInfoUrl = logisticServerHost
					+ "order_center/QueryEMSExpressInf_Order";
			// orderId = 5116741128399L;
			paramMap.put("logistics_number", new String[] { logisticsNum + "" });

		}

		String strres = "";
		try {
			strres = HttpClientUtils.doGetAndGetString(getInfoUrl, paramMap);
		} catch (ClientProtocolException e) {
			logger.error("toLogistics",e);
		} catch (IOException e) {
			logger.error("toLogistics",e);
		}
		JSONObject jsonRes = JSONObject.parseObject(strres);

		String code = jsonRes.getString("code");
		String data = jsonRes.getString("data");
		String msg = jsonRes.getString("message");

		List<LogisticResult> res = new ArrayList<LogisticResult>();
		if (StringUtils.isNotEmpty(data)) {
			JSONObject dataObj = JSONObject.parseObject(data);
			String xml = dataObj.getString("xml");
			res = JSONArray.parseArray(xml, LogisticResult.class);
			request.setAttribute("logisticList", res);

			return "web/member/myLogisticDetail";
		} else {
			return "redirect:/myOrder/toMyAllOrderList?flag=2";
		}

	}

	@RequestMapping(value = "/payOrder", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String payOrder(HttpServletRequest request, Model model,
			Long subOrderIds) {
		try {
			TfOrderSub orderSub = new TfOrderSub();
			orderSub.setOrderSubId(subOrderIds);
			orderSub = orderQueryService.queryComplexOrder(orderSub);

			// 获取订单购买数量总、skuId，用于页面嵌码
			Long goodsBuyNumCount = 0L;
			List<Long> skuIdList = Lists.newArrayList();
			List<TfOrderSubDetail> orderSubDetailList = orderSub
					.getDetailList();
			for (TfOrderSubDetail orderSubDetail : orderSubDetailList) {
				goodsBuyNumCount += orderSubDetail.getGoodsSkuNum();
				skuIdList.add(orderSubDetail.getGoodsSkuId());
			}

			// 优惠券
			Long memberId = UserUtils.getLoginUser(request).getMemberLogin()
					.getMemberId();
			CouponInfo couponInfo = new CouponInfo();
			couponInfo.setMemberId(memberId);
			List<CouponInfo> couponInfoList = couponService
					.queryMyCouponInfoList(couponInfo);

			model.addAttribute("goodsBuyNumCount", goodsBuyNumCount);
			model.addAttribute("skuIds", StringUtils.join(skuIdList, ";"));
			model.addAttribute("couponInfoList", couponInfoList);
			model.addAttribute("orderSub", orderSub);
		} catch (Exception e) {
			throw e;
		}
		return "web/member/selectPayWay";
	}

	@RequestMapping(value = "/commentOrder.json", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String commentOrder(HttpServletRequest request,
			HttpServletResponse response, UserRatingScore ratscore,
			UserRating userRating, Long orderSubId, Integer... score) {
		try {
			MemberVo member = UserUtils.getLoginUser(request);
			if (member == null) {
				return "请先登录！";
			}
			TfOrderSubDetail orderDetail = orderQueryService
					.queryOrderDetail(orderSubId);
			if (orderDetail == null) {
				orderDetail = new TfOrderSubDetail();
			}
			TfOrderSub orderSub = new TfOrderSub();
			orderSub.setOrderSubId(orderDetail.getOrderSubId());
			TfOrderUserRef orf = new TfOrderUserRef();
			orf.setMemberId(member.getMemberLogin().getMemberId());
			orderSub.setOrderUserRef(orf);
			orderSub = orderQueryService.queryComplexOrder(orderSub,
					Variables.ACT_GROUP_MEMBER);
			if (score != null) {
				if (score[0] == null) {
					return "请进行商品评分";
				}
				userRating.setGoodsRatingScore(score[0]);
				if (score[1] == null) {
					return "请进行店铺评分";
				}
				userRating.setShopRatingScore(score[1]);
				if (score[2] == null) {
					return "请进行物流评分";
				}
				userRating.setLogisticsRatingScore(score[2]);
			} else {
				return "请进行评分";
			}
			Long memberId = member.getMemberLogin().getMemberId();
			if (userRating != null) {
				userRating.setGoodsId(orderDetail.getGoodsSkuId());
				userRating.setShopId(orderDetail.getShopId());
				userRating.setMemberId(memberId);
				userRating.setRatingType("1");
				userRating.setMemberId(memberId);
				userRating.setMemberLoginName(member.getMemberLogin()
						.getMemberLogingName());
				userRating.setOrderSubId(orderDetail.getOrderSubId());
				userRating.setOrderSubNo(orderDetail.getOrderSubNo());
				userRating.setShopId(orderSub.getShopId());
				userRating.setGoodsName(orderDetail.getGoodsName());
				userRating.setShopName(orderSub.getShopName());
				userRating.setGoods_sku_id(orderDetail.getGoodsSkuId());
				userRating.setGoods_format(orderDetail.getGoodsFormat());
				userRating.setRatingContain(illegalKeyWordManagerImpl
						.verifyReplaceThrough(userRating.getRatingContain()));
			}
			String res = userRatingServiceImpl.insertRatingScore(userRating);

			if ("新增评价成功".equals(res)) {
				OrderProcessParam param = new OrderProcessParam();
				param.setBusinessId(orderSub.getOrderSubNo() + "");
				param.setOrderStatusId(orderSub.getOrderStatusId());
				param.setAct(1);
				orderService.complete(param);
			}
			return res;
		} catch (Exception e) {
			logger.error("新增评价失败",e);
			if (e.getMessage() != null) {
				return e.getMessage();
			}
		}
		return "新增评价失败";
	}

	@RequestMapping(value = "/toCommentOrder", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String toCommentOrder(HttpServletRequest request,
			HttpServletResponse response, Long orderDetailId) {
		TfOrderSubDetail orderDetail = orderQueryService
				.queryOrderDetail(orderDetailId);
		orderDetail.setGoodsImgUrl(orderDetail.getGoodsImgUrl());
		request.setAttribute("orderSubDetail", orderDetail);
		return "web/member/commentOrder";
	}

	@RequestMapping(value = "/sendGoodsWarn", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String sendGoodsWarn(HttpServletRequest request,
			HttpServletResponse response, Long orderSubId) {
		try {
			int i = orderService.remindDelivery(orderSubId);
			if (i > 0) {
				return "success";
			}
		} catch (Exception e) {
			logger.error("发货提醒失败", e);
			if (e.getMessage() != null) {
				return e.getMessage();
			}
		}
		return "发货提醒失败，请重试";
	}

	@RequestMapping(value = "/toOrderDetail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String toOrderDetail(HttpServletRequest request,
			HttpServletResponse response, Long orderId, Integer type)
			throws Exception {
		try {
			MemberVo member = UserUtils.getLoginUser(request);
			TfOrderSub orderSub = new TfOrderSub();
			orderSub.setOrderSubId(orderId);
			TfOrderUserRef orf = new TfOrderUserRef();
			if (type == null) {
				orf.setMemberId(member.getMemberLogin().getMemberId());
			}
			if (type != null && type == 1) {
				orf.setContactPhone(member.getMemberLogin().getMemberPhone()
						+ "");
			}
			orderSub.setOrderUserRef(orf);
			TfOrderSub orderview = orderQueryService.queryComplexOrder(orderSub, Variables.ACT_GROUP_MEMBER);
			if (orderview == null) {
				logger.info("无订单详情，订单号：" + orderId);
				throw new Exception("该订单不存在！");
			}
			orderview.setDetailList(wrapGoodsUrl(request,
					orderview.getDetailList()));


			TfOrderRecipient tfOrderRecipient = orderview.getRecipient();

			//模糊化名字
			String name = tfOrderRecipient.getOrderRecipientName();
			if(name != null){
				StringBuilder blurryName = new StringBuilder("");
				blurryName.append(name.substring(0,1));
				for(int i = 1; i < name.length(); i ++){
					blurryName.append("*");
				}
				request.setAttribute("blurryName",blurryName);
			}

			//模糊化地址
			String addr = tfOrderRecipient.getOrderRecipientAddress();
			if(addr != null ){
				StringBuilder blurryAddr = new StringBuilder("");
				for (int i = 0; i < addr.length(); i ++){
					blurryAddr.append("*");
				}
				request.setAttribute("blurryAddr",blurryAddr);
			}


			//模糊化电话号码
			String phone = tfOrderRecipient.getOrderRecipientPhone();
			if(phone != null){
				StringBuilder blurryPhone = new StringBuilder("");
				if (phone.length() >= 11){
					blurryPhone.append(phone.substring(0,3));
					blurryPhone.append("****");
					blurryPhone.append(phone.substring(7));
				} else {
					blurryPhone.append(phone);
				}
				request.setAttribute("blurryPhone",blurryPhone);
			}

			//判断85
			check85(orderview);
			request.setAttribute("orderview", orderview);
		} catch (Exception e) {
			throw new Exception("获取订单详情异常！");
		}
		return "web/member/orderDetail";
	}

	private void check85(TfOrderSub orderview) {
		if(orderview.getShopId()!=100000002099l)
			return;
		TfOrderSub sub = new TfOrderSub();
		sub.setThirdPartyId(orderview.getThirdPartyId());
		List<TfOrderSub> orderSubs = orderQueryService.query10085Order(sub);
		if(orderSubs==null||orderSubs.size()==0){
			return;
		}
		sub = orderSubs.get(0);
		sub.setOrderSubId(orderview.getOrderSubId());

		long status = orderview.getOrderStatusId();
		int status85 = sub.getOrderStatusId();
		//更新
		if(status85==1005){
			if(status!=6){
				//更新待发货
				sub.setOrderStatusId(6);
				orderview.setOrderStatusId(6);
				orderview.getOrderStatus().setOrderStatusName("待发货");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1024){
			if(status!=12){
				//更新已签收
				sub.setOrderStatusId(12);
				orderview.setOrderStatusId(12);
				orderview.getOrderStatus().setOrderStatusName("已归档");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1042){
			if(status!=1){
				//更新状态是待支付
				sub.setOrderStatusId(1);
				orderview.setOrderStatusId(1);
				orderview.getOrderStatus().setOrderStatusName("待支付");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1043){
			if(status!=6){
				//更新状态是已支付,待发货
				sub.setOrderStatusId(6);
				orderview.setOrderStatusId(6);
				orderview.getOrderStatus().setOrderStatusName("待发货");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1031){
			if(status!=34){
				//待退款
				sub.setOrderStatusId(34);
				orderview.setOrderStatusId(34);
				orderview.getOrderStatus().setOrderStatusName("待退款");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1033){
			if(status!=17){
				//异常
				sub.setOrderStatusId(17);
				orderview.setOrderStatusId(17);
				orderview.getOrderStatus().setOrderStatusName("异常");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1034){
			if(status!=15){
				//拒收
				sub.setOrderStatusId(15);
				orderview.setOrderStatusId(15);
				orderview.getOrderStatus().setOrderStatusName("拒收");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1037){
			if(status!=33){
				//退款驳回
				sub.setOrderStatusId(33);
				orderview.setOrderStatusId(33);
				orderview.getOrderStatus().setOrderStatusName("退货不受理");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1046){
			if(status!=14){
				//退货退款中
				sub.setOrderStatusId(14);
				orderview.setOrderStatusId(14);
				orderview.getOrderStatus().setOrderStatusName("退款中");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1030||status85==1047){
			if(status!=16){
				sub.setOrderStatusId(16);
				orderview.setOrderStatusId(16);
				orderview.getOrderStatus().setOrderStatusName("退款结束");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
		if(status85==1025||status85==1045){
			if(status!=18){
				sub.setOrderStatusId(18);
				orderview.setOrderStatusId(18);
				orderview.getOrderStatus().setOrderStatusName("已取消");
				order10085UpdateService.updateOrderStatus(sub);
			}
		}
	}

	/**
	 *
	 *            pay : 支付 cancel :取消订单 receipt : 确认收货 rating : 评价 refund :退款
	 * @param response
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/process", method = RequestMethod.GET)
	public String process(HttpServletRequest request,
			HttpServletResponse response, ProcessResult process,
			RedirectAttributesModelMap modelMap) throws Exception {
		try {
			TfOrderSub orderSub = new TfOrderSub();
			orderSub.setOrderSubId(process.getOrderSubId());
			MemberVo member = UserUtils.getLoginUser(request);
			TfOrderUserRef orf = new TfOrderUserRef();
			orf.setMemberId(member.getMemberLogin().getMemberId());
			orderSub.setOrderUserRef(orf);
			TfOrderSub orderview = orderQueryService.queryComplexOrder(
					orderSub, Variables.ACT_GROUP_MEMBER);
			if (orderview != null){
				LinkedHashMap<String, TaskAction> taskActionLinkedHashMap = orderview.getActionMap();//订单可执行动作
				//判断要执行的动作是否在可执行动作列表中
				if (taskActionLinkedHashMap != null && taskActionLinkedHashMap.keySet().size() > 0){
					boolean actionFlag = false;
					Set<String> taskKeySet = taskActionLinkedHashMap.keySet();
					Iterator<String> taskIterator = taskKeySet.iterator();
					while (taskIterator.hasNext()){
						String taskKey = taskIterator.next();
						if (process.getAction().equals(taskKey)){
							actionFlag = true;
							break;
						}
					}
					//actionFlag=false没有操作 且操作不是申请售后
					if (!actionFlag && !"aftersale".equals(process.getAction())){
						throw new Exception("无法执行该操作！");
					}
				} else if(!"aftersale".equals(process.getAction())){
					throw new Exception("此订单没有可执行的动作！");
				}
			} else {
				throw new Exception("没有找到对应订单，请稍后再试！");
			}
			OrderProcessParam param = new OrderProcessParam();
			if ("rating".equals(process.getAction())) {
				return "redirect:/userRating/toCommentOrder";
			} else if ("refund".equals(process.getAction())) {
				return "redirect:/afterserviceWap/retMoney/retMoneyUI?subOrderId="
						+ process.getOrderSubId()
						+ "&&subOrderDetailId="
						+ process.getSubOrderDetailId();
			}  else if ("refundGoods".equals(process.getAction())) {
				return "redirect:/afterserviceWap/retAndChangeGood/retAndChangeGoodUI?subOrderId="
						+ process.getOrderSubId()
						+ "&&subOrderDetailId="
						+ process.getSubOrderDetailId();
			} else if ("aftersale".equals(process.getAction())) {
				return "redirect:/afterserviceWap/aftersaleService/toAftersaleServiceUI?subOrderId="
						+ process.getOrderSubId()
						+ "&&subOrderDetailId="
						+ process.getSubOrderDetailId();
			} else if ("pay".equals(process.getAction())) {
				return "redirect:/myOrder/payOrder?subOrderIds="
						+ process.getOrderSubId();
			}else if("cancel".equals(process.getAction())){
				param.setAct(0);
			}else if("receipt".equals(process.getAction())){
               //receipt收货时方向为1
				param.setAct(1);
			}
			param.setBusinessId(orderview.getOrderSubNo() + "");
			param.setOrderStatusId(orderview.getOrderStatusId());

			OrderProcessResult result = orderService.complete(param);
			if (result.getResult() == OrderProcessResult.FAILED) {
				throw new Exception(result.getMessage());
			}
		} catch (Exception e) {

			logger.error("处理订单流程错误：", e);
			throw e;
		}
		return "redirect:/myOrder/toMyAllOrderList";
	}

	/**
	 * 功能简述 我的订单滑动分页
	 * 
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNo
	 * @param flag
	 * @return
	 * @author：yunxy
	 * @create：2016年6月28日 下午5:16:33
	 */
	@ResponseBody
	@RequestMapping("/queryMyOrderByPage")
	public Page<TfOrderSub> queryMyOrderByPage(HttpServletRequest request,
			HttpServletResponse response, Integer pageSize, Integer pageNo,
			Integer flag) {
		Page<TfOrderSub> orderPage = null;
		try {
			MemberVo member = UserUtils.getLoginUser(request);
			if (pageSize == null) {
				pageSize = pagesizedef;
			}
			if (pageNo == null) {
				pageNo = pagenodef;
			}
			if (member != null) {
				Page<TfOrderSub> page = new Page<TfOrderSub>();
				TfOrderSub orderSub = new TfOrderSub();
				//查询所有订单，包括85
				orderSub.setThirdPartyId("all");
				page.setPageSize(pageSize);
				page.setPageNo(pageNo);
				orderSub.setPage(page);

				if (flag == 1) {
					orderSub.setOrderStatusId(OrderStatusConstant.WAITSENDGOODS
							.getValue());
				} else if (flag == 2) {
					orderSub.setOrderStatusId(OrderStatusConstant.WAITRECEIVEGOODS
							.getValue());
				} else if (flag == 3) {
					orderSub.setOrderStatusId(OrderStatusConstant.WAITCOMMENT
							.getValue());
				}

				TfOrderUserRef orf = new TfOrderUserRef();
				orf.setMemberId(member.getMemberLogin().getMemberId());
				orderSub.setOrderUserRef(orf);
				List<String> excludeChannelCodes = new ArrayList<>();
				excludeChannelCodes.add(OrderConstant.CHANNEL_B2B);
				excludeChannelCodes.add(OrderConstant.CHANNEL_BLOC);
				orderSub.setExcludeChannelCodes(excludeChannelCodes); // 排除B2B及集团渠道
				orderPage = orderQueryService.queryComplexOrderPage(orderSub,
						Variables.ACT_GROUP_MEMBER);
				List<TfOrderSub> orderListNew = new ArrayList<TfOrderSub>();
				if (orderPage != null
						&& !CollectionUtils.isEmpty(orderPage.getList())) {
					for (TfOrderSub order : orderPage.getList()) {
						List<TfOrderSubDetail> detaillist = order
								.getDetailList();
						List<TfOrderSubDetail> detailListNew = wrapGoodsUrl(
								request, detaillist);
						order.setDetailList(detailListNew);
						orderListNew.add(order);
					}
					orderPage.setList(orderListNew);
				}
			}
		} catch (Exception e) {
			logger.error("订单滑动分页异常:", e);
		}
		return orderPage;
	}

	@RequestMapping(value = "/toMyGroupOrderList", method = RequestMethod.GET)
	public String myGroupOrderList(HttpServletRequest request,
			HttpServletResponse response, Integer pageSize, Integer pageNo,
			Integer flag) {
		MemberVo member = UserUtils.getLoginUser(request);
		if (pageSize == null) {
			pageSize = pagesizedef;
		}
		if (pageNo == null) {
			pageNo = pagenodef;
		}
		if (member != null) {
			TfOrderSub orderSub = new TfOrderSub();
			Page<TfOrderSub> page = new Page<TfOrderSub>();
			page.setPageSize(pageSize);
			page.setPageNo(pageNo);
			orderSub.setPage(page);
			TfOrderUserRef orf = new TfOrderUserRef();
			orf.setContactPhone(member.getMemberLogin().getMemberPhone() + "");
			orderSub.setOrderUserRef(orf);
			orderSub.setOrderChannelCode(OrderConstant.CHANNEL_BLOC);
			Page<TfOrderSub> orderPage = orderQueryService
					.queryComplexOrderPage(orderSub, Variables.ACT_GROUP_MEMBER);
			List<TfOrderSub> orderListNew = new ArrayList<TfOrderSub>();
			if (orderPage != null
					&& !CollectionUtils.isEmpty(orderPage.getList())) {
				for (TfOrderSub order : orderPage.getList()) {
					List<TfOrderSubDetail> detaillist = order.getDetailList();
					List<TfOrderSubDetail> detailListNew = new ArrayList<TfOrderSubDetail>();
					if (CollectionUtils.isNotEmpty(detaillist)) {
						for (TfOrderSubDetail detail : detaillist) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("containGoodsStaticInfo", true);
							map.put("containShopGoodsChannelRef", true);
							map.put("goodsSkuId", detail.getGoodsSkuId());
							map.put("chnlCode", "E006");
							List<TfGoodsInfo> goods = goodsManageService
									.queryGoodsInfoByCds(map);
							if (CollectionUtils.isNotEmpty(goods)) {
								try {
									if (CollectionUtils.isNotEmpty(goods.get(0)
											.getGoodsStaticList())
											&& StringUtils
													.isNotEmpty(goods
															.get(0)
															.getGoodsStaticList()
															.get(0)
															.getGoodsStaticUrl())) {
										detail.setGoodsImgUrl(goods.get(0)
												.getGoodsStaticList().get(0)
												.getGoodsStaticUrl());
									}
									if (CollectionUtils.isNotEmpty(goods.get(0)
											.getGoodsStaticList())
											&& StringUtils
													.isNotEmpty(goods
															.get(0)
															.getGoodsStaticList()
															.get(0)
															.getGoodsStaticUrl())) {
										detail.setGoodsUrl(goods.get(0)
												.getTfShopGoodsChannelRefList()
												.get(0).getGoodsUrl());
									}
								} catch (Exception e) {
									logger.error("填充商品路径错误：", e);
								}
							}
							detailListNew.add(detail);
						}
						order.setDetailList(detailListNew);
					}
					orderListNew.add(order);
				}
				orderPage.setList(orderListNew);
			}
			if (orderPage != null) {
				request.setAttribute("orderviewList", orderPage);
			}
		}
		return "web/member/myGroupOrderList";
	}
	/**
	 * 10085退货退款
	 * @param request
	 * @param orderSubNo
	 * @param dsOrderId,85方订单编号
	 * @return
	 * @throws Exception
	 */
	public static final String refundApply = new PropertiesLoader(
			"mall.properties").getProperty("refundApply");
	@RequestMapping(value ="/refund10085Apply")
	public String refund10085Apply(HttpServletRequest request,Long orderSubId,Model model)throws Exception{
		//首先根据85方的订单号查询子订单号，推送的订单只包含一个子订单

		TfOrderSub tf = new TfOrderSub();
		tf.setOrderSubId(orderSubId);
		tf = orderQueryService.queryComplexOrder(tf);
		List<TfOrderSub> orderSubs = orderQueryService.query10085Order(tf);
		if(orderSubs.size()==1){
			TfOrderSub sub = orderSubs.get(0);
			//组建map
			Map map = new HashMap();
			map.put("subOrderNo",sub.getThirdSubPartyId());//10085子订单号
			map.put("thirdOrderNo",sub.getOrderSubNo());//协同渠道订单编号
			map.put("refundReason","商品质量问题");
			map.put("hmac", UppCore.getHmac(map,"10085qsjkeyshdkfodu","UTF-8"));
			String result = HttpClientUtils.sentHttp(refundApply,map);
			JSONObject json = JSONObject.parseObject(result);
			//获取返回码
			String returnCode = json.getString("returnCode");
			String message ="";
			if("1".equals(returnCode)){//申请退款成功
				//处理本地订单，只需要做一个标示即可
				sub.setOrderSubId(orderSubId);
				sub.setOrderStatusId(34);
				order10085UpdateService.updateOrderStatus(sub);
				message = "申请退款成功";
			}else{//申请退款失败
				//提示出错
				message = "申请退款失败";
				logger.error("申请退款失败");
			}
		}
		return "redirect:/myOrder/toMyAllOrderList";
	}

	/**
	 * 号卡订单查询数据
	 * @param model
	 * @param psptId
	 * @param statusId
	 * @param request
	 * @param subOrderPage
     * @return
     */
	@RequestMapping(value = "/querySimOrderData")
	@ResponseBody
	public Page queryCardOrderList(Model model,String psptId,Integer statusId, HttpServletRequest request,Page<TfOrderSub> subOrderPage){
		MemberVo member = UserUtils.getLoginUser(request);
		TfOrderSub orderSub=new TfOrderSub();
		orderSub.setOrderStatusId(statusId);
		orderSub.setExcludeOrderStatusIds(Arrays.asList(OrderConstant.STATUS_CREATE));
		orderSub.setOrderTypeIds(String.valueOf(OrderConstant.TYPE_SIM));
		orderSub.setPage(subOrderPage);
		model.addAttribute("statusId",statusId);
		psptId= TriDes.getInstance().strDec(psptId, keyStr, null, null);

		Calendar calendar = Calendar.getInstance();
		orderSub.setEndOrderTime(calendar.getTime());
		calendar.add(Calendar.MONTH, -3);
		orderSub.setStartOrderTime(calendar.getTime());
		if(member!=null && StringUtils.isEmpty(psptId)){
			TfOrderUserRef orf = new TfOrderUserRef();
			orf.setMemberId(member.getMemberLogin()
					.getMemberId());
			orderSub.setOrderUserRef(orf);
			model.addAttribute("memberId",member.getMemberLogin().getMemberId());
			//查询O2O在线售卡需写卡订单(根据orderSub.deliverMode=4区分)
			subOrderPage = orderQueryService.queryComplexOrderPage(orderSub);
		}else if(StringUtils.isNotEmpty(psptId)){
			orderSub.setQueryPsptAndOrderNo(psptId);
			model.addAttribute("psptId",psptId);
			subOrderPage = orderQueryService.queryOrderPageSim(orderSub);
		}
		return subOrderPage;
	}

	/**
	 * o2o渠道预约订单
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryMakeCardOrderData")
	@ResponseBody
	public ResponseBean queryMakeCardOrderData(Model model, String psptId, HttpServletRequest request){
		ResponseBean bean=new ResponseBean();
		psptId= TriDes.getInstance().strDec(psptId, keyStr, null, null);
		List<O2oPreOrder> resultList=Lists.newArrayList();
		if(StringUtils.isNotEmpty(psptId)){
			model.addAttribute("psptId",psptId);
			O2oPreOrder preOrder=new O2oPreOrder();
			preOrder.setCardId(psptId);
			resultList = orderQueryService.queryMakeCardOrder(preOrder);
			bean.addSuccess(resultList);
		}
		return bean;
	}

	/**
	 * 号卡订单查询页面
	 * @param model	 * @param statusId
	 * @param request
	 * @param subOrderPage
     * @return
     */
	@RequestMapping(value = "/querySimOrderList")
	public String queryCardOrderIndex(Model model,String psptId,Integer statusId, HttpServletRequest request,Page<TfOrderSub> subOrderPage){
		MemberVo member = UserUtils.getLoginUser(request);
		model.addAttribute("statusId",statusId);
		if(member!=null){
			model.addAttribute("memberId",member.getMemberLogin().getMemberId());
		}else if(StringUtils.isNotEmpty(psptId)){
			model.addAttribute("psptId",psptId);
		}
		return "web/goods/sim/orderSimList";
	}

	/**
	 * 号卡订单详情
	 * @param model
	 * @param request
	 * @param orderSub
     * @return
     */
	@RequestMapping(value = "/querySimOrderDetail", method = RequestMethod.GET)
	public String queryCardOrderIndex(Model model, HttpServletRequest request,TfOrderSub orderSub){
		MemberVo member = UserUtils.getLoginUser(request);
		orderSub.setOrderTypeIds(String.valueOf(OrderConstant.TYPE_SIM));
		Page<TfOrderSub> results = orderQueryService.queryComplexOrderPage(orderSub);//可以查到busiParam
		TfOrderSub result = results.getList().get(0);
		model.addAttribute("result",result);
		if(result.getDeliveryModeId() == 4){
			//展示到店取或者物流以及订单信息
			TfOrderSubDetail detail = result.getDetailList().get(0);
			TfH5SimConf cond = new TfH5SimConf();
			cond.setConfId(detail.getGoodsUrl());
			TfH5SimConf resultPlan = plansService.queryListCond(cond).get(0);
			model.addAttribute("conf",resultPlan);
			model.addAttribute("orderSub",result);
			return "web/goods/o2osimonline/orderDetailCust";
		}
		if("O2O_GROUP".equals(result.getDetailSim().getTerminalType())){
			GetImsiNumberCondition condition = new GetImsiNumberCondition();
			condition.setSERIAL_NUMBER(result.getDetailSim().getPhone());
			try{
				//
				String imsi = o2oSimGroupService.getImsiNumber(condition);
				model.addAttribute("imsi",imsi);
			}catch (Exception e){
				if(e instanceof EcsException){
					EcsException e1 = (EcsException)e;
					model.addAttribute("imsi",e1.getFriendlyDesc());
				}
				logger.error("查询出错",e);
			}
		}
		return "web/goods/sim/orderSimDetail";
	}

	/**
	 * TODO 获取和掌柜的经纬度
	 * @param shopId
	 * @param model
	 * @return
	 */
	@RequestMapping("shopMap")
	public String shopMap(String shopId,Model model){
		model.addAttribute("LONGITUDE","112");
		model.addAttribute("LATITUDE","28");
		model.addAttribute("SHOPNAME","谢江琼测试测试测试");
		model.addAttribute("SHOPADDR","湖南省长沙市岳麓区尖山路中电软件园7栋");
		model.addAttribute("SHOPID","123456");
		return "web/goods/sim/shopMap";
	}


}
