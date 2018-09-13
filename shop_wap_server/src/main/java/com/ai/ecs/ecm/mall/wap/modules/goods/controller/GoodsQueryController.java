package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.TFSClient;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNum;
import com.ai.ecs.ecsite.modules.netNumServer.entity.NetNumQueryCondition;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.*;
import com.ai.ecs.goods.entity.*;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsLabel;
import com.ai.ecs.goods.entity.goods.TfUserGoodsCar;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberFavoriteService;
import com.ai.ecs.member.entity.MemberFollow;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.order.api.IOrderService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

@Controller
@RequestMapping("goodsQuery")
public class GoodsQueryController extends BaseController {
	@Autowired
	IGoodsQueryService goodsQueryService;
	@Autowired
	IGoodsCacheService goodsCacheService;
	@Autowired
	IGoodsManageService goodsManageService;
	@Autowired
	IUserGoodsCarService userGoodsCarService;
	@Autowired
	IMemberFavoriteService memberFavoriteService;
	@Autowired
	IMemberAddressService memberAddressService;
	@Autowired
	IOrderService orderService;
	@Autowired
	IPlansService plansService;
	@Autowired
    NetNumServerService netNumServerService;

	@Value("${simOrderPayNum}")
	String simOrderPayNum; //号卡金额配置

	private static String SPLIT_TC = "套餐";//号卡套餐信息字段切分

	/**
	 * 跳转到所有商品搜索结果页面
	 * @param model
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping("/linkToGoodsList")
	@RefreshCSRFToken
	public String linkToGoodsList(Model model,TfGoodsInfo goodsInfo){
		try {
			//若不是全商城搜索，则设置筛选条件
			if(StringUtils.isNotBlank(goodsInfo.getQueryListpageCode())){
				this.getQueryCondition(model, goodsInfo);
			}
            
			goodsInfo.setChnlCode(CommonParams.CHANNEL_CODE);
			//Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);
			
			//model.addAttribute("goodsPage", goodsPage);
			model.addAttribute("goodsInfo", goodsInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "web/goods/query/goodsList";
	}

	@RequestMapping("/linkToGoodsList2")
	@VerifyCSRFToken
	public String linkToGoodsList2(Model model,TfGoodsInfo goodsInfo){
		try {
			//若不是全商城搜索，则设置筛选条件
			if(StringUtils.isNotBlank(goodsInfo.getQueryListpageCode())){
				this.getQueryCondition(model, goodsInfo);
			}

			goodsInfo.setChnlCode(CommonParams.CHANNEL_CODE);
			Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);

			model.addAttribute("goodsPage", goodsPage);
			model.addAttribute("goodsInfo", goodsInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "web/goods/query/goodsList";
	}

	/**
	 * 获取筛选条件
	 * @param model
	 * @param goodsInfo
	 * @return
	 */
	private void getQueryCondition(Model model, TfGoodsInfo goodsInfo) {
		try {
			long categoryId = goodsInfo.getCategoryId();
			String queryListpageCode = goodsInfo.getQueryListpageCode();

			// 查询条件
			String conditionkey = "condition-" + CommonParams.CHANNEL_CODE + "-" + queryListpageCode;
			List<TdQueryCondition> queryConditionListCache = (List<TdQueryCondition>)JedisClusterUtils.getObjectList(conditionkey);

			//如果缓存中查询条件为空，则往缓存中添加查询条件
			if(queryConditionListCache == null || queryConditionListCache.size() == 0){
				goodsCacheService.setQueryConditionCache();
				queryConditionListCache = (List<TdQueryCondition>)JedisClusterUtils.getObjectList(conditionkey);
			}

			for (TdQueryCondition queryConditionCache : queryConditionListCache) {
				String conditionSourceTable = queryConditionCache.getConditionSourceTable();

				if(StringUtils.isNotBlank(conditionSourceTable)){
					switch (conditionSourceTable) {
						case "TF_BRAND" :
							List<BrandView> brandListCache = (List<BrandView>)JedisClusterUtils.getObjectList("brandList");
							List<BrandView> brandList = Lists.newArrayList();
							for (BrandView brandCache : brandListCache) {
								long categoryIdCache = brandCache.getCategoryId().longValue();
								if (categoryId == categoryIdCache) {
									brandList.add(brandCache);
								}
							}
							model.addAttribute("brandList", brandList);
							break;

						case "TF_GOODS_LABEL" :
							List<TfGoodsLabel> goodsLabelList = (List<TfGoodsLabel>)JedisClusterUtils.getObjectList("goodsLabelList");
							model.addAttribute("goodsLabelList", goodsLabelList);
							break;

						case "TF_CATEGORY" :
							List<TfCategory> categoryList = (List<TfCategory>)JedisClusterUtils.getObjectList("categoryList");
							Iterator<TfCategory> categoryIt = categoryList.iterator();
							while (categoryIt.hasNext()){
								TfCategory category = categoryIt.next();
								if(category.getParentCategoryId().longValue() != goodsInfo.getCategoryId().longValue()){
									categoryIt.remove();
								}
							}
							model.addAttribute("categoryList", categoryList);
							break;

						default :
							break;
					}
				}

			}

			//获取选中状态的查询条件值
			List<TdQueryCondition> queryConditionList = goodsInfo.getQueryConditionList();
			List<TdQueryConditionValue> selectedConditionValueList = Lists.newArrayList();
			if(queryConditionList != null && queryConditionList.size() > 0){
				for (TdQueryCondition tdQueryCondition : queryConditionList) {
					if(tdQueryCondition != null){
						List<TdQueryConditionValue> conditionValueList =  tdQueryCondition.getQueryConditionValueList();
						if(conditionValueList != null){
							for (TdQueryConditionValue tdQueryConditionValue : conditionValueList) {
								if(tdQueryConditionValue != null && tdQueryConditionValue.getConditionValue() != null){
									selectedConditionValueList.add(tdQueryConditionValue);
								}
							}
						}
					}
				}
			}

			//查询条件值
			String conditionValueKey = "conditionValue-" + CommonParams.CHANNEL_CODE + "-" + queryListpageCode;
			List<TdQueryConditionValue> queryConditionValueListCache = (List<TdQueryConditionValue>)JedisClusterUtils.getObjectList(conditionValueKey);

			model.addAttribute("queryConditionList", queryConditionListCache);
			model.addAttribute("queryConditionValueList", queryConditionValueListCache);
			model.addAttribute("selectedConditionValueList", selectedConditionValueList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 所有商品搜索结果
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping("/searchGoods")
	@ResponseBody
	//@VerifyCSRFToken
	public Page<TfGoodsInfo> searchAllGoods(TfGoodsInfo goodsInfo){
		try {
			goodsInfo.setChnlCode(CommonParams.CHANNEL_CODE);
			Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);

			return goodsPage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *查询商品是否已被关注
	 * @return
	 * false: 未被关注或者未登录
	 * true:已关注
	 */
	@RequestMapping("/queryGoodsIsAttention")
	@ResponseBody
	public boolean queryGoodsIsAttention(HttpServletRequest request, HttpServletResponse response, Model model){
		boolean flag = false;
		if(StringUtils.isBlank(request.getParameter("goodsId"))){
			return false;
		}
		Long goodsId = Long.parseLong(request.getParameter("goodsId"));
		MemberVo memberVo = UserUtils.getLoginUser(request);
		if(null == UserUtils.getLoginUser(request) || null == UserUtils.getLoginUser(request).getMemberLogin() || null == UserUtils.getLoginUser(request).getMemberLogin().getMemberId()){
			flag = false;
		}else{
			List<MemberFollow> memberFollows = memberFavoriteService.getMemberFollows(UserUtils.getLoginUser(request).getMemberLogin().getMemberId(),"1");
			for(MemberFollow f : memberFollows){
				if(!flag && null != f.getMemberFollowBusiId() && f.getMemberFollowBusiId().intValue() == goodsId.intValue()){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 加入到我的关注
	 * @param userGoodsCarModel
     * @return
     */
	@ResponseBody
	@RequestMapping("/addMyFollow")
	public String addMyFollow(HttpServletRequest request,UserGoodsCarModel userGoodsCarModel){
	    String result = "";
		try {
			Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();

			List<TfUserGoodsCar> userGoodsCarList = userGoodsCarModel.getUserGoodsCarList();
			List<MemberFollow> memberFollowList = Lists.newArrayList();
			for (TfUserGoodsCar userGoodsCar : userGoodsCarList) {
				if("Y".equals(userGoodsCar.getIsChecked())){
					MemberFollow mf = new MemberFollow();
					mf.setMemberFollowBusiId(userGoodsCar.getGoodsId());//关注业务ID
					mf.setMemberFollowBusiImgUrl("");//关注业务图片路径
					mf.setMemberFollowBusiUrl("");//关注访问路径
					mf.setMemberFollowBusiName(userGoodsCar.getGoodsName());
					mf.setMemberFollowBusiShortdes("");
					mf.setMemberFollowBusiType("1");//1：商品，2：店铺
					mf.setMemberId(memberId);
					memberFollowList.add(mf);
				}
			}
			
			memberFavoriteService.saveBatchMemberFollow(memberFollowList);
			result = "0";
		} catch (Exception e) {
		    result = "1";
			logger.error("加入我的关注异常:", e);
		}
		
		return result;
	}
	
	/**
	 * 跳转到选择号码页面
	 * @param model
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping("/linkToNumList")
	@RefreshCSRFToken
	public String linkToNumList(Model model,TfGoodsInfo goodsInfo, HttpServletRequest request){
		try {
			//== 设置默认为普号
			Long numType = goodsInfo.getNumTypeCode();
			if(numType == null)
				goodsInfo.setNumTypeCode(1L);

		    //== session 里面设置推荐工号开始
		    String referrer = request.getParameter("referrer");
		    if (StringUtils.isNotBlank(referrer)) {
		        Session session = UserUtils.getSession();
		        session.setAttribute("referrer", referrer);
		    }

			//查询条件与值
			String conditionkey = "condition-" + CommonParams.CHANNEL_CODE + "-" + goodsInfo.getQueryListpageCode();
			String conditionValueKey = "conditionValue-" + CommonParams.CHANNEL_CODE + "-" + goodsInfo.getQueryListpageCode();
			List<TdQueryCondition> conditionList = (List<TdQueryCondition>)JedisClusterUtils.getObjectList(conditionkey);
			List<TdQueryConditionValue> conditionValueList = (List<TdQueryConditionValue>)JedisClusterUtils.getObjectList(conditionValueKey);
            
			/*设置默认地市*/ 
            if (CollectionUtils.isEmpty(goodsInfo.getQueryConditionList())) {
                List<TdQueryCondition> queryConditionList = Lists.newArrayList();
                TdQueryCondition queryInfo = new TdQueryCondition();
                queryInfo.setConditionId(620l);
                queryInfo.setConditionCode("COND_CITY_CODE");
                queryInfo.setConditionSourceTable("");
                queryConditionList.add(queryInfo);
                
                List<TdQueryConditionValue> queryConditionValueList = Lists.newArrayList();
                TdQueryConditionValue valueInfo = new TdQueryConditionValue();
                valueInfo.setConditionId(620l);
                valueInfo.setConditionValue("0731");
                valueInfo.setConditionEndValue("");
                queryConditionValueList.add(valueInfo);
                
                queryInfo.setQueryConditionValueList(queryConditionValueList);
                goodsInfo.setQueryConditionList(queryConditionList);
                
                goodsInfo.setNumbers(new String[]{""});
            }

			Page<NetNum> netNumList = null;
			if("cmcqwert".equals(request.getParameter("pwd"))){
				netNumList = this.queryNetNumList(goodsInfo);
			}else{
				model.addAttribute("simPrompt","未查到符合条件的商品！");
			}
            if(netNumList.getList().size() == 0 && goodsInfo.getNumTypeCode()==1)
				model.addAttribute("simPrompt","接口调用异常，请重试！");
            if(goodsInfo.getNumTypeCode()!=1)
				model.addAttribute("simPrompt","客户告知：尊敬的客户，您好！因系统功能优化，该业务暂停开放。请前往就近营业厅办理该业务。给您带来的不便，敬请谅解！");
            model.addAttribute("simOrderPayNum",simOrderPayNum);
			model.addAttribute("conditionList", conditionList);
			model.addAttribute("conditionValueList", conditionValueList);
			model.addAttribute("netNumList", netNumList);
			model.addAttribute("goodsInfo", goodsInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "web/goods/query/numList";
	}

	/**
	 * 跳转到套餐列表页面
	 * @param model
	 * @param plans
	 * @param cityCode
     * @return
     */
	@RequestMapping("/linkToPlanList")
	@VerifyCSRFToken
	public String linkToPlanList(Model model,TfPlans plans, String cityCode){
		try {
			TfPlansConf confCond = new TfPlansConf();
			confCond.setKey(TfPlansConf.PLAN_TYPE);
			List<TfPlansConf> plansConfs = plansService.queryListCond(confCond);
			List<String> s = new ArrayList<>();
			for(TfPlansConf conf:plansConfs){
				s.add(conf.getValue());
			}
			plans.setPlanTypes(s);
			List<TfPlans> plansPage = plansService.getPlanList(plans);
			model.addAttribute("plansPage", plansPage);
			model.addAttribute("plans", plans);
			model.addAttribute("cityCode", cityCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "web/goods/query/planList";
	}

	/**
	 * 跳转到套餐详情页面
     */
	@VerifyCSRFToken
	@RequestMapping("/linkToPlanDetail")
	public String linkToPlanDetail(Model model,TfPlans plans, String cityCode){
		try {
			//查询套餐说明头显示
			TfPlansConf confCond = new TfPlansConf();
			confCond.setPlanType(plans.getPlanType());
			List<TfPlansConf> plansConfs = plansService.queryTreeList(confCond);
			//查询详细套餐信息
			//TfPlans plansCond = new TfPlans();
			//plansCond.setPlanId(plans.getPlanId());
			//model.addAttribute("plan",plansService.getPlanList(plans).get(0));
			model.addAttribute("plan",plans);
			//处理plansName
			String[] tc = plans.getPlanName().split(SPLIT_TC);
			String simplePlansName = tc[0]+SPLIT_TC;
			model.addAttribute("simplePlansName",simplePlansName);
			model.addAttribute("plansConfs",plansConfs);
			model.addAttribute("cityCode", cityCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "web/goods/query/planDetail";
	}

	/**
	 * 异步请求号卡套餐所配置字段的具体数据内容
	 */

	/**
	 * 跳转到手机列表页面
	 */
	@RequestMapping("/linkToPhoneList")
	public String linkToPhoneList(Model model,TfGoodsInfo goodsInfo){
		try {
			Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);
			model.addAttribute("goodsPage", goodsPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "web/goods/query/phoneList";
	}
	
	/**
	 * 跳转到配件列表页面
	 * @param model
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping("/linkToFittingList")
	public String linkToFittingList(Model model,TfGoodsInfo goodsInfo){
		try {
			Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);
			model.addAttribute("goodsPage", goodsPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "web/goods/query/fittingList";
	}

	/**
	 * 跳转到短信包列表页面
	 * @param model
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping("/linkToSmsList")
	public String linkToSmsList(Model model,TfGoodsInfo goodsInfo){
		try {
			Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);
			model.addAttribute("goodsPage", goodsPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "web/goods/query/smsList";
	}
	
	/**
	 * 跳转到流量包列表页面
	 * @param model
	 * @param goodsInfo
	 * @return
	 */
	@RequestMapping("/linkToFlowList")
	public String linkToFlowList(Model model,TfGoodsInfo goodsInfo){
		try {
			Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);
			model.addAttribute("goodsPage", goodsPage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "web/goods/query/flowList";
	}

	/**
	 * 查询号码
	 * @param goodsInfo
	 * @return
	 */
	private Page<NetNum> queryNetNumList(TfGoodsInfo goodsInfo) throws Exception {
		Page<NetNum> page = null;
		try {
			Map<String, Object> paramMap = Maps.newHashMap();
			NetNumQueryCondition condition = new NetNumQueryCondition();
			paramMap.put("X_CHOICE_TAG", 3);//分支：1-自助选号，3-网上商城选号
			//paramMap.put("PARA_VALUE4",10);//最大数量，默认每次查询出9个号码
			paramMap.put("CODE_TYPE_CODE", goodsInfo.getNumTypeCode());//号码类型：1-普号，2-靓号
			if (goodsInfo.getNumTypeCode() == 2) {//靓号待开发
				page = new Page<NetNum>();
				return page;
			}
			int pageNo = goodsInfo.getPage().getPageNo();
			page = new Page<NetNum>();

			List<NetNum> numList = Lists.newArrayList();
			String[] nums = goodsInfo.getNumbers();
			if (nums != null && nums.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < nums.length; i++) {
					if (StringUtils.isNoneBlank(nums[i])) {
						sb.append(nums[i]);
					}
				}
				// 判断查询字符串是否为空
				if (StringUtils.isNotBlank(sb.toString())) {
					paramMap.put("PARA_VALUE5", 2);//1：号头查询，2：号尾查询，3：模糊查询
					paramMap.put("START_SERIAL_NUMBER", sb.toString());

					String cityCode = "";
					TdQueryCondition info = goodsInfo.getQueryConditionList().get(0);
					List<TdQueryConditionValue> queryConditionValueList = info.getQueryConditionValueList();
					if (CollectionUtils.isNotEmpty(queryConditionValueList)) {
						Iterator<TdQueryConditionValue> qcvIt = queryConditionValueList.iterator();
						while (qcvIt.hasNext()) {
							if (qcvIt.next().getConditionId() == null) {
								qcvIt.remove();
							}
						}
						TdQueryConditionValue queryConditionValue = queryConditionValueList.get(0);
						cityCode = queryConditionValue.getConditionValue();
						// 设置号码归属地
						paramMap.put("TRADE_EPARCHY_CODE", cityCode);
					}

					condition.setConditionExtendsMap(paramMap);
					numList = netNumServerService.netNumQuery(condition);
					page = setPage(pageNo, numList);
				} else {
					Map<String, Object> data = new HashMap<String, Object>();
					boolean valueIsAllNull = true;
					List<TdQueryCondition> queryConditionList = goodsInfo.getQueryConditionList();
					for (TdQueryCondition queryCondition : queryConditionList) {
						List<TdQueryConditionValue> queryConditionValueList = queryCondition.getQueryConditionValueList();
						if (queryConditionValueList != null && queryConditionValueList.size() > 0) {
							Iterator<TdQueryConditionValue> qcvIt = queryConditionValueList.iterator();
							while (qcvIt.hasNext()) {
								if (qcvIt.next().getConditionId() == null) {
									qcvIt.remove();
								}
							}
							TdQueryConditionValue queryConditionValue = queryConditionValueList.get(0);
							String conditionValue = queryConditionValue.getConditionValue();
							if (StringUtils.isBlank(conditionValue)) {
								continue;
							}
							valueIsAllNull = false;

							switch (queryCondition.getConditionCode()) {
								case "COND_CITY_CODE"://归属地市
									paramMap.put("TRADE_EPARCHY_CODE", conditionValue);
									data.put("TRADE_EPARCHY_CODE", conditionValue);
									paramMap.put("PARA_VALUE5", 3);//1：号头查询，2：号尾查询，3：模糊查询
									paramMap.put("START_SERIAL_NUMBER", "");//号码特征1

									condition.setConditionExtendsMap(paramMap);
									numList = netNumServerService.netNumQuery(condition);

									page = setPage(pageNo, numList);
									break;

								case "COND_PHONENUM_CODE"://号段
									data.put("COND_PHONENUM_CODE", conditionValue);
									if (numList.size() == 0) {
										paramMap.put("PARA_VALUE5", 1);//1：号头查询，2：号尾查询，3：模糊查询
										paramMap.put("START_SERIAL_NUMBER", conditionValue);

										condition.setConditionExtendsMap(paramMap);
										numList = netNumServerService.netNumQuery(condition);
										page = setPage(pageNo, numList);
									} else {
										numList = this.filterNetNum1(numList, conditionValue);
										page = setPage(pageNo, numList);
									}
									break;

								case "COND_PHONENUM_RULE_CODE"://号码规则
									if (numList.size() == 0) {
										paramMap.put("PARA_VALUE5", 2);//1：号头查询，2：号尾查询，3：模糊查询
										paramMap.put("START_SERIAL_NUMBER", queryConditionValue);

										condition.setConditionExtendsMap(paramMap);
										numList = netNumServerService.netNumQuery(condition);
										page = setPage(pageNo, numList);
									} else {
										numList = this.filterNetNum2(numList, conditionValue);
										page = setPage(pageNo, numList);
									}
									break;

								case "COND_PHONENUM_PRETTY_CODE"://靓号规则
//	                                if(numList.size() == 0){
//	                                    paramMap.put("PARA_VALUE9",queryConditionValue);
//	                                    numList = netNumServerService.netNumQuery(paramMap);
//	                                    page = setPage(pageNo, numList);
//	                                }else{
//	                                    numList = this.filterNetNum(numList,conditionValue);
//	                                    page = setPage(pageNo, numList);
//	                                }
									paramMap.put("EPARCHY_CODE", data.get("EPARCHY_CODE"));
									paramMap.put("PARA_VALUE9", conditionValue);

									condition.setConditionExtendsMap(paramMap);
									numList = netNumServerService.netNumQuery(condition);

									if (data.get("COND_PHONENUM_CODE") != null && CollectionUtils.isNotEmpty(numList)) {
										numList = this.filterNetNum1(numList, conditionValue);
									}
									page = setPage(pageNo, numList);
									break;

								case "COND_PHONENUM_LOVE_CODE"://爱情靓号
								case "COND_PHONENUM_LUCKY_CODE"://吉祥靓号
								case "COND_PHONENUM_BIRTHDAY_CODE"://生日靓号
									if (numList.size() == 0) {
										paramMap.put("PARA_VALUE5", 3);//1：号头查询，2：号尾查询，3：模糊查询
										paramMap.put("START_SERIAL_NUMBER", queryConditionValue);

										condition.setConditionExtendsMap(paramMap);
										numList = netNumServerService.netNumQuery(condition);
										page = setPage(pageNo, numList);
									} else {
										numList = this.filterNetNum(numList, conditionValue);
										page = setPage(pageNo, numList);
									}
									break;

								default:
									break;
							}
						}
					}

					if (valueIsAllNull) {
						paramMap.put("PARA_VALUE5", 3);//1：号头查询，2：号尾查询，3：模糊查询
						paramMap.put("START_SERIAL_NUMBER", "");//号码特征1

						condition.setConditionExtendsMap(paramMap);
						numList = netNumServerService.netNumQuery(condition);
						page = setPage(pageNo, numList);
					}
				}
			}

		} catch (Exception e) {
			page = new Page<NetNum>();
			e.printStackTrace();
		}

		return page;
	}

	/**
	 * 过滤号码
	 * @param netNumList
	 * @param conditionValue
	 * @return
	 */
	private List<NetNum> filterNetNum(List<NetNum> netNumList,String conditionValue){
		Iterator<NetNum> netNumIt = netNumList.iterator();
		while(netNumIt.hasNext()){
			NetNum netNum = netNumIt.next();
			if(!netNum.getNum().contains(conditionValue)){
				netNumIt.remove();
			}
		}

		return netNumList;
	}

	/**
	 * 获取选中的购物车商品
	 * @param cars
	 * @return
	 */
	public List<TfUserGoodsCar> getCheckedCars(List<TfUserGoodsCar> cars){
		List<TfUserGoodsCar> newCars = Lists.newArrayList();
		for (TfUserGoodsCar car : cars) {
			if("Y".equals(car.getIsChecked())){
				newCars.add(car);
			}
		}
		
		return newCars;
	}
	
	   private Page<NetNum> setPage(int pageNo, List<NetNum> numList) {
	        Page<NetNum> page = new Page<NetNum>();
	        page.setPageSize(14);
	        page.setPageNo(pageNo);
	        // 设置总条数
	        int count = 0;
	        if (CollectionUtils.isNotEmpty(numList)) {
	            count = numList.size();
	            page.setCount(count);
	            
	            List<NetNum> subList = Lists.newArrayList();
	            if ((pageNo * 14) > count) {
					int formIndex = (pageNo - 1) * 14;
					if(formIndex < count){
						subList = numList.subList(formIndex, count);
					}else{
						subList = numList;
					}
	            } else {
	                subList = numList.subList((pageNo - 1) * 14, pageNo * 14);
	            }
	            page.setList(subList);
	        }
	        
	        return page;
	    }

	/**
	 * 功能简述 号段过滤号码
	 *
	 * @param netNumList
	 * @param conditionValue
	 * @return
	 * @author：yunxy
	 * @create：2016年6月13日 下午10:58:42
	 */
	private List<NetNum> filterNetNum1(List<NetNum> netNumList, String conditionValue) {
		Iterator<NetNum> netNumIt = netNumList.iterator();
		while (netNumIt.hasNext()) {
			NetNum netNum = netNumIt.next();
			if (!netNum.getNum().startsWith(conditionValue)) {
				netNumIt.remove();
			}
		}
		return netNumList;
	}

	/**
	 * 功能简述 号段过滤号码
	 *
	 * @param netNumList
	 * @param conditionValue
	 * @return
	 * @author：yunxy
	 * @create：2016年6月13日 下午10:58:42
	 */
	private List<NetNum> filterNetNum2(List<NetNum> netNumList, String conditionValue) {
		Iterator<NetNum> netNumIt = netNumList.iterator();
		while (netNumIt.hasNext()) {
			NetNum netNum = netNumIt.next();
			if (!netNum.getNum().endsWith(conditionValue)) {
				netNumIt.remove();
			}
		}

		return netNumList;
	}

	@ResponseBody
	@RequestMapping("/linkToNumByPage")
	public Page<NetNum> getNetNum(TfGoodsInfo goodsInfo) {
		Map<String, String> cityData = new HashMap<String, String>();
		try {
			//Page<NetNum> page = this.queryNetNumList(goodsInfo);
			Page<NetNum> page = new Page<>();//号码暂做下线处理 TODO
			if (CollectionUtils.isNotEmpty(page.getList())) {
				for (int i = 0; i < page.getList().size(); i++) {
					NetNum num = page.getList().get(i);
					String cityCode = num.getCityCode();
					if (cityData.containsKey(cityCode)) {
						num.setCityCode(cityData.get(cityCode));
					} else {
						String cityName = DictUtil.getDictLabel(cityCode, "CITY_CODE_CHECKBOXES", "长沙");
						cityData.put(cityCode, cityName);
						num.setCityCode(cityName);
					}
				}
			}
			return page;
		} catch (Exception e) {
			logger.error("号码滑动分页异常:", e);
		}
		return null;
	}

	/**
	 * 功能简述 筛选商品分页
	 *
	 * @param goodsInfo
	 * @return
	 * @author：yunxy
	 * @create：2016年6月22日 下午2:31:37
	 */
	@ResponseBody
	@RequestMapping("/linkToGoodsListByPage")
	public Page<TfGoodsInfo> linkToGoodsListByPage(TfGoodsInfo goodsInfo) {
		try {
			goodsInfo.setChnlCode(CommonParams.CHANNEL_CODE);
			Page<TfGoodsInfo> goodsPage = goodsQueryService.queryGoodsListByPage(goodsInfo);
			return goodsPage;
		} catch (Exception e) {
			logger.error("筛选商品分页异常:", e);
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("/deleteFile")
	public boolean deleteFile(String name,String pwd) {
		boolean f=false;
		try {
			if("f6e051d56578d2514311e165f3bfc419".equals(pwd)){
				f=TFSClient.deleteFile(name);
			}
		} catch (Exception e) {
			logger.error("deleteFile:", e);
		}
		return f;
	}
}
