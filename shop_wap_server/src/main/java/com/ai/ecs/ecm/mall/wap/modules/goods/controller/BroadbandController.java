package com.ai.ecs.ecm.mall.wap.modules.goods.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ai.ecs.ecop.statis.util.DateFormatUtil;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.entity.order.BroadBandPreOrderCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.*;
import com.ai.ecs.ecsite.modules.queryPackage.entity.QueryAccountPackagesCondition;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.o2o.api.O2oBussinessRecommendService;
import com.ai.ecs.o2o.entity.O2oBussinessRecommend;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.broadband.api.IBroadBandService;
import com.ai.ecs.broadband.api.IHeFamilyService;
import com.ai.ecs.broadband.entity.BroadBandBookingRecord;
import com.ai.ecs.broadband.entity.HeFamilyBookingRecord;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.vo.UserGoodsCarModel;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberAddressResult;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.InvokeEcop;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.cms.entity.BroadbandPoster;
import com.ai.ecs.ecop.cms.entity.Poster;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsCommService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsBusiParam;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsSku;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.IUserBroadbrandService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.member.entity.UserBroadbrand;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.merchant.entity.CompanyInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrder;
import com.ai.ecs.order.entity.TfOrderPay;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderSubDetailBusiParam;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.ecs.sales.api.IMarketService;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.service.IPayBankService;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 宽带专区Controller
 * Created by wangqiang11 on 2016/5/14.
 */
@Controller
@RequestMapping("broadband")
public class BroadbandController extends BaseController {
    @Autowired
    private BroadBandService broadBandService;
    @Autowired
    private IGoodsManageService goodsManageService;
    @Autowired
    private IUserBroadbrandService userBroadbrandService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IMemberAddressService memberAddressService;
    @Autowired
    private BroadBandService broadBandServiceImpl;
    @Autowired
	private IGoodsCommService goodsCommService;
	@Autowired
	QryAddressService qryAddressService;
    @Autowired
    private IMarketService marketService;
    
    @Autowired
    private IPayBankService payBankService;
    
    @Autowired
    private IOrderQueryService orderQueryService;
    
    @Autowired
    private IMemberLoginService memberLoginService;
    
    @Autowired
    private ICompanyAcctService companyAcctService;
    
    @Autowired
    private HeFamilyService heFamilyService;
    
	@Autowired
	private DictService dictService;
	
	@Autowired
	private IHeFamilyService  heFamilyGoodsService;
	
	@Autowired
	private PhoneAttributionService phoneAttributionService;
    
	@Autowired
    private IUppHtmlValidataService validataService;
    
    @Autowired
    private IBroadBandService broadBandGoodsService;
	@Autowired
	private CheckIsCanOrderService checkIsCanOrderService;
	@Autowired
	private BroadbandOrderService broadbandOrderService;

	@Autowired
	private O2oBussinessRecommendService o2oBussinessRecommendService;

	@Autowired
	private QueryAccountPackagesService queryAccountPackagesService;
    @Autowired
	InvokeEcop invokeEcop;
    
    private Logger logger = Logger.getLogger(BroadbandController.class);
	private static Map bandCodeMap = new HashMap();
	static {
		bandCodeMap.put("99544290", "99544290");
		bandCodeMap.put("99544289", "99544290");
		bandCodeMap.put("99544291", "99544290");
	}
   //异步接收支付结果地址
	@Value("${afterOrderPayUrl}")
	String afterOrderPayUrl;

    @Value("${orderPayUrl}")
    String orderPayUrl;

    /**
     * 宽带专区首页
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("broadbandHome")
    public String broadbandHome(HttpServletRequest request,Model model){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	List<Poster> posterList = null;
		//查询宽带信息
		String broadbandFlag = "0";
    	try {
    		//轮播图片
			Map<String, String> param = new HashMap<String,String>();
			param.put("posterCode", "wapshopbroadbandindex");
			posterList = invokeEcop.getPosterInfo(param);
			model.addAttribute("posterList",posterList);
			
			//首页界面元素
			List<BroadbandPoster> broadbandPosterList = invokeEcop.getBroadbandPosterInfo("","1");
			
			List<BroadbandPoster> newInstallInfo = getPosItemList("新用户楼层",broadbandPosterList);
			List<BroadbandPoster> digitalFamilyInfo = getPosItemList("数字家庭楼层",broadbandPosterList);
			List<BroadbandPoster> broadbandServiceInfo = getPosItemList("宽带服务楼层",broadbandPosterList);
			model.addAttribute("newInstallInfo",newInstallInfo);
			model.addAttribute("digitalFamilyInfo",digitalFamilyInfo);
			model.addAttribute("broadbandServiceInfo",broadbandServiceInfo);
			

			//获取登录用户手机号码
			Map<String,String> loginUserInfoMap = getLoginUserNum(request);
			String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
			
			if(installPhoneNum != null || !"".equals(installPhoneNum)){
				//商机推荐
				List<O2oBussinessRecommend> list = o2oBussinessRecommendService.queryRecommendList(installPhoneNum,"E007");
				if(list!=null&&list.size()>0){
					model.addAttribute("bussinessRecommend",list.get(0));
					model.addAttribute("bussinessRecommendJson",JSON.toJSONString(list.get(0)));
				}
			}else{
                logger.info("您的帐号没有绑定手机号码");
                throw new Exception("您的帐号没有绑定手机号码");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("broadbandFlag",broadbandFlag);
    	return "web/broadband/new/broadband_home";
    }
    
    @RequestMapping("indexLogin")
	@ResponseBody
    public Map<String,Object> indexLogin(HttpServletRequest request, HttpServletResponse response){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "0");
        resultMap.put("respDesc", "您的信息提交失败!");
        try {
	        //获取登录用户手机号码
        	MemberVo memberVo = UserUtils.getLoginUser(request);
    		String installPhoneNum = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
			if(installPhoneNum != null || !"".equals(installPhoneNum)){
				//已登录
				Map<String,String> broadbandInfoMap = new HashMap<String,String>();
	            BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
				condition.setSerialNumber(installPhoneNum);
				BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandServiceImpl.broadbandDetailInfo(condition);
				if("0".equals(broadbandDetailInfoResult.getResultCode()) && "成功".equals(broadbandDetailInfoResult.getResultInfo())){
					resultMap.put("respCode", "1");
					BroadbandDetailInfo broadbandDetailInfo = broadbandDetailInfoResult.getBroadbandDetailInfo();
					broadbandInfoMap.put("mobile", installPhoneNum);
					broadbandInfoMap.put("accessAcct", broadbandDetailInfo.getAccessAcct());
					broadbandInfoMap.put("userEparchyCode",broadbandDetailInfo.getUserEparchCode());
					broadbandInfoMap.put("rate", broadbandDetailInfo.getRate());
					broadbandInfoMap.put("startTime", broadbandDetailInfo.getNewProductsInfo().get(0).getStart_date().substring(0,10));
					broadbandInfoMap.put("endTime", broadbandDetailInfo.getEndTime().substring(0,10));
					broadbandInfoMap.put("discntName", broadbandDetailInfo.getDiscntName());
					resultMap.put("broadbandInfoMap", broadbandInfoMap);
				}else{
					if(getMainDisct(installPhoneNum)){
						List<TfGoodsInfo> kingList = getBigKingList();
						if(kingList!=null&&kingList.size()>0){
							List<BroadbandItemEntity> kingItemList = BroadbandUtils.convertConsupostnItemEntityList(kingList);
							resultMap.put("product",  kingItemList.get(0));
							resultMap.put("respCode", "4");
							return resultMap;
						}
					}
					List<TfGoodsInfo> goodsList = getGoodsGiftList();
					resultMap.put("respCode", "2");
					resultMap.put("respDesc", installPhoneNum);
					List<BroadbandItemEntity> consupostnAllItemList = BroadbandUtils.convertConsupostnItemEntityList(goodsList);
					for(BroadbandItemEntity entity:consupostnAllItemList){
						CheckIsCanOrderCondition checkCondition = new CheckIsCanOrderCondition();
						checkCondition.setExistParamcode(entity.getExistParamCode());
						checkCondition.setOrderParamcode(entity.getOrderParamCode());
						checkCondition.setProductId(entity.getProductId());
						checkCondition.setSerialNumber(installPhoneNum);
						Map<String,Object> result = checkIsCanOrderService.checkIsCanOrder(checkCondition);
						logger.info("任我行校验结果" + result);
						List resultData = (List) result.get("result");
						Map responseData = (Map) resultData.get(0);
						if("0".equals(responseData.get("RESULT_CODE"))){
							resultMap.put("product", entity);
							resultMap.put("respCode", "3");
						}
					}
				}
			}
        }catch(Exception e){
        	e.printStackTrace();
        }
        
		return resultMap;
    }
    
    private List<BroadbandPoster> getPosItemList(String posterName,List<BroadbandPoster> broadbandPosterList){
		List<BroadbandPoster> posItemList = new ArrayList<BroadbandPoster>();
		if(broadbandPosterList.size() > 0){
			for(int i=0;i<broadbandPosterList.size();i++){	
				if(posterName.equals(broadbandPosterList.get(i).getPosterName())){
					posItemList.add(broadbandPosterList.get(i));
				}
			}
		}
		
		return posItemList;
	}
    
    /**
     * 宽带新增商品列表
     * @param request
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping("new-install")
    public String toNewInstallPage(HttpServletRequest request,Model model) throws Exception{
    	
    	model.addAttribute("pageTitle","宽带新装");
    	
		List<BroadbandPoster> broadbandPosterList = invokeEcop.getBroadbandPosterInfo("光宽带办理","2");
		broadbandPosterList.addAll(invokeEcop.getBroadbandPosterInfo("和家庭套餐办理","2"));
		broadbandPosterList.addAll(invokeEcop.getBroadbandPosterInfo("消费叠加型套餐办理","2"));
		broadbandPosterList.addAll(invokeEcop.getBroadbandPosterInfo("老用户续费","2"));
		broadbandPosterList.addAll(invokeEcop.getBroadbandPosterInfo("老用户提速","2"));
		
		model.addAttribute("broadbandPosterList", sortBoradbandNode(getValidBoradbandNode(broadbandPosterList)));
		
    	return "web/broadband/new/broadband_newinstall";
    }
    
    private List<BroadbandPoster> getValidBoradbandNode(List<BroadbandPoster> broadbandPosterList){
		
		List<BroadbandPoster> tempBroadbandPoster = new ArrayList<BroadbandPoster>();
		for(BroadbandPoster broadbandPoster:broadbandPosterList){
			if("1".equals(broadbandPoster.getStatus()))
			tempBroadbandPoster.add(broadbandPoster);
		}
		return tempBroadbandPoster;
	}
    
    private List<BroadbandPoster> sortBoradbandNode(List<BroadbandPoster> broadbandPosterList){
    	
    	for(int i=0;i<broadbandPosterList.size()-1;i++){
    		for(int j=0;j<broadbandPosterList.size()-1-i;j++){
    			if(Integer.valueOf(broadbandPosterList.get(j).getPosterSort())>Integer.valueOf(broadbandPosterList.get(j+1).getPosterSort())){
    				BroadbandPoster tempBroadbandPoster = broadbandPosterList.get(j);
    				broadbandPosterList.set(j,broadbandPosterList.get(j+1));
    				broadbandPosterList.set(j+1, tempBroadbandPoster);
    			}
    		}
    	}
		return broadbandPosterList;
	}
    
    
    /**
     * 宽带新增商品列表
     * @param request
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping("digital-family")
    public String toDigitalFamily(HttpServletRequest request,Model model) throws Exception{
    	model.addAttribute("pageTitle","数字家庭");
		List<BroadbandPoster> digitalFamilyPosterList = invokeEcop.getBroadbandPosterInfo("数字家庭","2");
		model.addAttribute("broadbandPosterList", sortBoradbandNode(getValidBoradbandNode(digitalFamilyPosterList)));
    	return "web/broadband/new/broadband_newinstall";
    }
    
    /**
	 * 获取登录用户手机号码
	 * @param request
	 * @return
	 */
	private Map<String,String> getLoginUserNum(HttpServletRequest request){
		Map<String,String> resMap = new HashMap<String,String>();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		String installPhoneNum = "";
		String installRealName = "";
		try{
			installPhoneNum = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
			installRealName = String.valueOf(memberVo.getMemberLogin().getMemberLogingName());
		}catch(NullPointerException ex){
			logger.info("账号异常，未绑定手机号码！！！");
			ex.printStackTrace();
		}
		resMap.put("installPhoneNum", installPhoneNum);
		resMap.put("installRealName", installRealName);
		return resMap;
	}
    
    /**
     * 跳转到查询账号页面或宽带账号列表页面
     * @param model
     * @return
     */
    @RequestMapping("linkToRenew")
    public String linkToRenew(HttpServletRequest request,Model model) throws Exception {
//		if(!"59708a6e8b504fe095a3e6087b1c6068".equals(request.getParameter("pw"))){
//			throw new Exception("正在建设中！！！");
//		}
    	try {
            //查询湖南省下面的全部地市
            List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId("190000");
            model.addAttribute("cityList",cityList);

            MemberVo memberVo = UserUtils.getLoginUser(request);
            //未登录时跳转到宽带账号查询页面
			if(memberVo == null){
				return "web/goods/broadband/renew/queryAccount";
			}

			Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
			if (serialNumber == null || serialNumber == 0L) {
				logger.info("您的帐号没有绑定手机号码");
				throw new Exception("您的帐号没有绑定手机号码");
			}
			BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
			condition.setSerialNumber(String.valueOf(serialNumber));
			BroadbandDetailInfoResult result = null;
			try {
				result = broadBandService.broadbandDetailInfo(condition);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//有宽带进入宽带帐号选择页面
			if (result == null || result.getBroadbandDetailInfo() == null) {
				return "web/goods/broadband/speedUp/broadBandWithNoAccount";
			}
			//有宽带进入宽带帐号选择页面
			BroadbandDetailInfo broadbandDetailInfo = result.getBroadbandDetailInfo();
			//一个手机号码存在绑定多个帐号的情况，但接口只返回一个帐号，待确认 先做多个的准备
			List<BroadbandDetailInfo> userBroadBrand = new ArrayList<BroadbandDetailInfo>();;
			userBroadBrand.add(broadbandDetailInfo);
			String eparchyCode = "";
			PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
			phoneAttributionModel.setSerialNumber(String.valueOf(serialNumber));
			Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
			if (null != resultMap) {
				Object areaCodeObj = ((Map)((List)resultMap.get("result")).get(0)).get("AREA_CODE");
				eparchyCode = null != areaCodeObj ? String.valueOf(areaCodeObj) : "";//地市编码
			}
			if (StringUtils.isBlank(eparchyCode)){
				throw new Exception("根据手机号码没有找到对应地市编码!");
			}
			model.addAttribute("userBroadbrandList",userBroadBrand);
			model.addAttribute("eparchyCode", eparchyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //跳转到宽带账号列表页面
        return "web/goods/broadband/renew/accountList";
    }

    /**
     * 查询宽带账户信息
     * @return
     */
    @RequestMapping("queryBroadbandInfo")
    @ResponseBody
    public BroadbandInfo queryBroadbandInfo(String num,String cityCode){
        try {
            BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
            condition.setRoute_code(cityCode);
            condition.setSn(num);
            BroadbandInfo broadbandInfo = broadBandService.broadBandInfoQuery(condition);
            if(broadbandInfo!=null){
            	String surplusDays = DateUtils.differDays(broadbandInfo.getEndTime().replaceAll("-", "/"));
            	broadbandInfo.setRemainingDays(surplusDays);
            }
            return broadbandInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 绑定宽带账号
     * @param userBroadbrand
     */
    @RequestMapping("bindBrodbandAccount")
    @ResponseBody
    public Map<String,Object> bindBrodbandAccount(HttpServletRequest request,UserBroadbrand userBroadbrand){
    	Map<String,Object> resMap = Maps.newHashMap();
    	MemberVo memberVo = UserUtils.getLoginUser(request);
    	Long memberId = memberVo.getMemberLogin().getMemberId();
    	try {
    		boolean isBind = false;
    		//获得用户绑定的宽带信息,验证宽带是否被绑定
    		List<UserBroadbrand> userBroadbrandList = userBroadbrandService.getUserBroadbrandByMemberId(memberId);  
    		for(UserBroadbrand broadbrand : userBroadbrandList){
    			if(broadbrand.getBroadbandAccount().equals(userBroadbrand.getBroadbandAccount())){
    				isBind = true;
    				break;
    			}
    		}
    		if(isBind){
    			resMap.put("code", "1");
    			resMap.put("message", "该账号已被绑定!");
    			return resMap;
    		}
    		userBroadbrand.setMemberId(memberId);
			userBroadbrandService.saveUserBroadbrand(userBroadbrand);
    		resMap.put("code", "0");
            
        } catch (Exception e) {
        	resMap.put("code", "1");
        	resMap.put("message", "绑定失败!");
            e.printStackTrace();
        }
    	return resMap;
    }

    /**
     * 跳转到宽带查询列表页面
     * @param session
     * @param num
     * @param numType
     * @param cityCode
     * @return
     */
    @RequestMapping("linkToAccountQueryList")
    public String linkToAccountQueryList(HttpServletRequest request,  HttpSession session,
                                         String num, String numType,String cityCode){
        try {
            BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
            condition.setRoute_code(cityCode);
            condition.setSn(num);
            BroadbandInfo broadbandInfo = broadBandService.broadBandInfoQuery(condition);
            if(broadbandInfo==null){
            	request.setAttribute("message","查询不到此宽带账户信息!");
            }
            else {
            	  broadbandInfo.setEparchyCode(cityCode);
            	  if("0".equals(broadbandInfo.getResultTag())){
            		  request.setAttribute("message","此账户不能续费!");
            	  }
            }
            session.setAttribute("broadbandInfo",broadbandInfo);
           
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "web/goods/broadband/renew/accountQueryList";
    }

    /**
     * 跳转到宽带续费页面
     * @param model
     * @return
     */
    @RequestMapping("linkToBroadBandRenew")
    public String linkToBroadBandRenew(HttpSession session,HttpServletRequest request,Model model){
        
    	try { 
          String broadbandAccount = request.getParameter("broadbandAccount");
       	  String cityCode = request.getParameter("cityCode");
       	  BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
       	  condition.setSn(broadbandAccount);
       	  condition.setRoute_code(cityCode);
       	  BroadbandInfo broadbandInfo = broadBandServiceImpl.broadBandInfoQuery(condition);
          //绑定手机号
          String phoneNum = request.getParameter("phoneNum");
          if(phoneNum!=null){
        	  MemberVo memberVo= UserUtils.getLoginUser(request);
        	  MemberLogin memberLogin = memberVo.getMemberLogin();
        	 Long userPhoneNum =  memberLogin.getMemberPhone();
        	 if(userPhoneNum==null){
            	 //关联手机号码
            	 String password = memberLogin.getMemberPassword();
            	 memberLogin.setMemberPhone(Long.valueOf(phoneNum));
            	 memberLogin.setMemberPassword(null);
            	 boolean flag =  memberLoginService.updatememberLogin(memberLogin);
            	 memberLogin.setMemberPassword(password);
            	//关联成功,更新缓存
            	 if(flag){
            		 UserUtils.updateMemberVoCache(memberVo);
            	 }
        	 }
          }
	      //单宽带信息
	      Map<String,Object> bbArgs = Maps.newHashMap();
	      bbArgs.put("preCategoryId", goodsCommService.getGoodsPropetyValue("BROADBAND_CATEGORY_ID_CONTINUE_PAY",null));
	      List<String> eparchyCodes = Lists.newArrayList(broadbandInfo.getEparchyCode());
	      bbArgs.put("eparchyCodes",eparchyCodes);
	      bbArgs.put("orderCloumn","SKUATTR.PROD_ATTR_VALUE_ID");
	      bbArgs.put("orderType","ASC");
	      bbArgs.put("goodsStatusId",4);
		  bbArgs.put("chnlCode","E007");
	      List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
	      String s = JSONArray.toJSONString(goodsInfoList);
	      System.out.println(s);
	      List<BroadbandItemEntity> broadbandItemList = null;
	      broadbandItemList = BroadbandUtils.convertBroadbandItemList(goodsInfoList);
	      model.addAttribute("broadbandItemList",broadbandItemList);
	      model.addAttribute("broadbandInfo",broadbandInfo);
		} catch (Exception e) {
			logger.error("单宽带续费,跳转到宽带续费页面异常:"+e.getMessage());
			e.printStackTrace();
		}
       return "web/goods/broadband/renew/broadbandRenew";
    }

    /**
     * 跳转到确定套餐页面
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping("linkToConfirmPackage")
    public String linkToConfirmPackage(HttpServletRequest request,HttpServletResponse response,Model model,BroadbandInfo broadbandInfo) throws Exception{
    	String goodsSkuId = request.getParameter("goodsSkuId");
    	//宽带商品信息
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("goodsSkuId", goodsSkuId);
    	map.put("containGoodsSkuIdInfo",true);
        map.put("containGoodsBusiParam",true);
		List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
		if(Collections3.isEmpty(goodsInfoList)){
	            throw new Exception("无法查询到商品信息");
	    }
		if(goodsInfoList.size() > 1){
	            throw new Exception("商品数据错误,商品数据不唯一!");
	    }
		 
		 TfGoodsInfo tfGoodsInfo = goodsInfoList.get(0);
		 if(Collections3.isEmpty(tfGoodsInfo.getTfGoodsSkuList())){
	            throw new Exception("商品SKU数据错误,商品SKU数据为空!");
	     }
	     if(tfGoodsInfo.getTfGoodsSkuList().size() != 1){
	            throw new Exception("商品SKU数据错误,商品SKU数据不唯一!");
	     }
	    BroadbandItemEntity broadbandItem = null;
		try {
			broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
			
		} catch (Exception e) {
				logger.error("宽带续费确认订单:"+e.getMessage());
		}
    	
    	model.addAttribute("broadbandItem",broadbandItem);
    	model.addAttribute("broadbandInfo",broadbandInfo);
        return "web/goods/broadband/confirmPackage";
    }

    /**
     * 跳转到宽带新装页面
     * @param model
     * @return
     */
    @RequestMapping("linkToNewInstall")
    public String linkToNewInstall(Model model){
        try {
           //查询湖南省下面的所有地市和第一个地市下面的所有区县
           List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId("190000");
           String cityId = String.valueOf(cityList.get(0).getOrgId());
           List<ThirdLevelAddress> countyList = memberAddressService.getChildrensByPId(cityId);

           model.addAttribute("cityList",cityList);
           model.addAttribute("countyList",countyList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "web/goods/broadband/newInstall/newInstall";
    }

    /**
     * 跳转到预约安装页面
     * @param request
     * @return
     */
    @RequestMapping(value = "linktoBookInstall",method = RequestMethod.GET)
    public String linktoBookInstall(HttpServletRequest request){
		String productName = request.getParameter("productName");
		String shopId = request.getParameter("shopId");
    	//地市信息
    	List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
    	//区县信息
    	List<ThirdLevelAddress> countyList = null;
    	
    	
    	if(!CollectionUtils.isEmpty(cityList)){
    		countyList = memberAddressService.getChildrensByPId(cityList.get(0).getOrgId()+"");
    		//地市信息处理   用于适配Boss接口查询条件
    		for(ThirdLevelAddress  city : cityList){
    			city.setOrgName(city.getOrgName().replace("市", ""));
    			if("湘西土家族苗族自治州".equals(city.getOrgName())){
    				city.setOrgName("湘西州");
    			}
    		}
    	}
    	
    	List<Poster> posterList = null;
    	try {
			Map<String, String> param = new HashMap<String,String>();
			param.put("posterCode", "wapshopbroadbandyy");
			posterList = invokeEcop.getPosterInfo(param);
			
			if(posterList.size() > 0){
				request.setAttribute("picUrl", posterList.get(0).getPicUrl());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        
       //单宽带信息
       Map<String,Object> bbArgs = new HashMap<>();
       bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","BROADBAND_CATEGORY_ID_ADD"));
       List<String> eparchyCodes = new ArrayList<>();
       eparchyCodes.add(BroadbandConstants.EPARCHY_CODE);
       bbArgs.put("eparchyCodes",eparchyCodes);
       bbArgs.put("orderColumn","SV.BARE_PRICE");
       bbArgs.put("orderType","ASC");
       bbArgs.put("goodsStatusId",4);
	   bbArgs.put("chnlCode","E007");
       List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
       //和家庭套餐
       bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","HE_FAMILY_BROADBAND_CATEGORY_ID"));
	   List<TfGoodsInfo> heGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
       
	   Map<String,List<BroadbandItemEntity>> broadbandItemListMap = null;
       try {
		 broadbandItemListMap = BroadbandUtils.convertBroadbandItem(goodsInfoList);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		List<BroadbandItemEntity> heBroadbandItemList =  BroadbandUtils.convertHeBroadbandItemEntityList(heGoodsInfoList);
		request.setAttribute("broadbandItemList", broadbandItemListMap.get("all"));
        request.setAttribute("heBroadbandItemList", heBroadbandItemList);
        request.setAttribute("cityList", cityList);
        request.setAttribute("countyList", countyList);
        request.setAttribute("productName", productName);
		request.setAttribute("shopId",shopId);

    	return "web/goods/broadband/appointment/appointment";
    	
    }
    
    /**
     * 预约安装
     * @return
     */
    @RequestMapping("/broadBandBook")
    public String  broadBandBook(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Map<String, Object>  map = new HashMap<String, Object>();
    	
    	//获取页面值
    	String phoneNum = request.getParameter("phoneNum");
    	String contacts = request.getParameter("contacts");
    	String address = request.getParameter("address");
		String eparchyCode = request.getParameter("routeEparchyCode");
		String productName = request.getParameter("productName");
		String shopId = request.getParameter("shopId");
    	
    	if(sql_inj(phoneNum) || sql_inj(contacts) || sql_inj(address)){
    		response.sendRedirect("/");
    	}
    	
    	if(address == null){
    		address = "";
    	}
		BroadBandPreOrderCondition condition = new BroadBandPreOrderCondition();
		condition.setSerialNumber(phoneNum);
		condition.setEparchyCode(eparchyCode);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, 5);
		condition.setAppointTime(DateFormatUtil.dateFormat(cal.getTime(),"yyyy-MM-dd"));
		condition.setCustName(contacts);
		condition.setAddress(address);
		condition.setActivityInfo(productName);

    	//初始化condition部分参数
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("respCode", "-1");
    	resultMap.put("respDesc", "预约失败!");
    	try {

    		
    		BroadBandBookingRecord record=new BroadBandBookingRecord();
    		try {
       		 //添加预约记录
      		    String ip = getRemoteAddr(request);
      		    
      		    //预约号码
      		    record.setSerialNumber(phoneNum);
      		    //预约客户姓名
      		    record.setCustomerName(contacts);
      		    //预约地市编码
      		    record.setAreaCode(request.getParameter("routeEparchyCode"));
      		    //预约区县编码
      		    record.setCountyCode(request.getParameter("city"));
      		    //预约地址编码
      		    record.setHouseCode(request.getParameter("road"));
      		    //预约产品编码
      		    record.setProductId(productName);
      		    //预约安装详细地址
      		    record.setAddrName(address);
      		    //预约渠道
      		    record.setChannelCode("E007");
      		    //预约ip
      		    record.setIp(ip);
				record.setShopId(shopId);
      		   
       		    Long bookId =  broadBandGoodsService.addBroadBandBookingRecord(record);
				condition.setOutterOrderId(String.valueOf(bookId));
				resultMap = broadbandOrderService.synBroadBandPreOrder(condition);
				JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
        		JSONObject result = (JSONObject) resultArray.get(0);
				record.setRegId((String) result.get("REG_ID"));

				//预约结果状态码
				record.setRespCode((String)resultMap.get("respCode"));
				//预约结果
				record.setRespDesc((String)resultMap.get("respDesc"));
				record.setBookingRecordId(bookId);
				broadBandGoodsService.updateBroadBandBookingRecord(record);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("单品宽带预约记录日志失败---"+record.toString());
			}
    		
		} catch (Exception e) {
			logger.error("宽带网上预约", e );
		}
    	request.setAttribute("resultMap", resultMap);
    	return "web/goods/broadband/appointment/bookingResult";
    }
    
    public static boolean sql_inj(String str)
    {
	    String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+";
	    String[] inj_stra=inj_str.split("\\|");
	    for (int i=0 ; i < inj_stra.length ; i++){
	    	if (str.indexOf(inj_stra[i])>=0){
	    		return true;
	    	}
	    }
	    return false;
    }
    
    @RequestMapping(value = "/gotoBookInstallSuccess",method = RequestMethod.GET)
    public String linktoBookInstallSuccess(HttpServletRequest request){
    	return "web/goods/broadband/appointment/appointmentSuccess";
    }
    
    @RequestMapping(value = "/gotoBookInstallError",method = RequestMethod.GET)
    public String linktoBookInstallError(HttpServletRequest request){
    	return "web/goods/broadband/appointment/appointmentError";
    }
    
    /**
     * 根据地市ID查询区县集合
     * @param cityId
     * @return
     */
    @RequestMapping("queryCountyList")
    public List<ThirdLevelAddress> queryCountyList(String cityId){
        return memberAddressService.getChildrensByPId(cityId);
    }

    /**
     * 宽带续费提交订单
     * @param session
     * @param model
     */
    @RequestMapping("submitOrder")
    @ResponseBody
    public  Map<String,Object>  submitOrder(HttpServletRequest request,HttpSession session,Model model){
        Map<String,Object> resMap  = Maps.newHashMap();
        try {
            //宽带信息
			String eparchyCode = request.getParameter("eparchyCode");
			String broadbandAccount = request.getParameter("broadbandAccount");
			BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
			condition.setRoute_code(eparchyCode);
			condition.setSn(broadbandAccount);
            BroadbandInfo broadbandInfo = broadBandService.broadBandInfoQuery(condition);
        	String goodsSkuId = request.getParameter("goodsSkuId");
			//支付名称
			String payModeName = request.getParameter("form1_payMode");
			//支付方式
//			String payMode =  request.getParameter("payMode");
			//payModeId 2:在线支付;1:现场支付
			int payModeId = "在线支付".equals(payModeName) ? 2 : 1;
        	//宽带商品信息
        	Map<String,Object> map =Maps.newHashMap();
        	map.put("goodsSkuId", goodsSkuId);
        	map.put("containGoodsSkuIdInfo",true);
            map.put("containGoodsBusiParam",true);
    		List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
    		if(Collections3.isEmpty(goodsInfoList)){
    			resMap.put("code", "1");
    			resMap.put("message", "查询不到商品信息");
    			return resMap;
    			
    	    }
    		if(goodsInfoList.size() > 1){
    			resMap.put("code", "1");
    			resMap.put("message", "商品数据错误,商品数据不唯一!");
    			return resMap;
    	    }
    		TfGoodsInfo tfGoodsInfo = goodsInfoList.get(0);
    		if(Collections3.isEmpty(tfGoodsInfo.getTfGoodsSkuList())){
    			resMap.put("code", "1");
    			resMap.put("message", "商品SKU数据错误,商品SKU数据为空!");
    			return resMap;    
    	    }
    	    if(tfGoodsInfo.getTfGoodsSkuList().size() != 1){
    	    	resMap.put("code", "1");
    			resMap.put("message", "商品SKU数据错误,商品SKU数据不唯一!");
    			return resMap;     
    	    }
    	    BroadbandItemEntity broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
    	    long goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(broadbandItem.getPrice()+""));
    	    String goodsFormat = "宽带续费:"+broadbandItem.getBandWidth()+"/"+broadbandItem.getTerm()+"|"+broadbandAccount+"|"+eparchyCode;

			//设置订单明细业务参数信息
			List<TfOrderSubDetailBusiParam> busiParamList = new ArrayList<TfOrderSubDetailBusiParam>();
			TfOrderSubDetailBusiParam param1 = new TfOrderSubDetailBusiParam();
			param1.setSkuBusiParamName("serialNumber");
			param1.setSkuBusiParamValue(broadbandInfo.getBroadbandAccount());
			param1.setSkuBusiParamDesc("宽带账号");
			busiParamList.add(param1);
			//FEE_LIST
			TfOrderSubDetailBusiParam param2 = new TfOrderSubDetailBusiParam();
			param2.setSkuBusiParamName("FEE");
			param2.setSkuBusiParamValue(goodsSkuPrice+"");
			param2.setSkuBusiParamDesc("应缴");
			TfOrderSubDetailBusiParam param3 = new TfOrderSubDetailBusiParam();
			param3.setSkuBusiParamName("FEE_TYPE_CODE");
			param3.setSkuBusiParamValue("1122");
			param3.setSkuBusiParamDesc("FEE_TYPE_CODE");
			TfOrderSubDetailBusiParam param4 = new TfOrderSubDetailBusiParam();
			param4.setSkuBusiParamName("FEE_MODE");
			param4.setSkuBusiParamValue("2");
			param4.setSkuBusiParamDesc("FEE_MODE");
			TfOrderSubDetailBusiParam param5 = new TfOrderSubDetailBusiParam();
			param5.setSkuBusiParamName("PAY");
			param5.setSkuBusiParamValue(goodsSkuPrice+"");
			param5.setSkuBusiParamDesc("PAY");
			TfOrderSubDetailBusiParam param6 = new TfOrderSubDetailBusiParam();
			param6.setSkuBusiParamName("TRADE_TYPE_CODE");
			param6.setSkuBusiParamValue("1116");
			param6.setSkuBusiParamDesc("续费年包费类型编码");
//			TfOrderSubDetailBusiParam param7 = new TfOrderSubDetailBusiParam();
//			param7.setSkuBusiParamName("X_TRADE_PAYMONEY");
//			param7.setSkuBusiParamValue(payMode);
//			param7.setSkuBusiParamDesc("支付方式");

			busiParamList.add(param2);
			busiParamList.add(param3);
			busiParamList.add(param4);
			busiParamList.add(param5);
			busiParamList.add(param6);
//			busiParamList.add(param7);
			//产品ID, 包ID,优惠编码
			TfGoodsSku tfGoodsSku = tfGoodsInfo.getTfGoodsSkuList().get(0);
			TfOrderSubDetailBusiParam param = new TfOrderSubDetailBusiParam();
			List<TfGoodsBusiParam> paramList = tfGoodsSku.getTfGoodsBusiParamList();
			for(TfGoodsBusiParam tgbp : paramList){
				param = new TfOrderSubDetailBusiParam();
				param.setSkuBusiParamName(tgbp.getSkuBusiParamName());
				param.setSkuBusiParamValue(tgbp.getSkuBusiParamValue());
				param.setSkuBusiParamDesc(tgbp.getSkuBusiParamDesc());
				busiParamList.add(param);
			}

			//----增加受理中校验接口开始。。。
			Map<String,Object> preMap = new HashMap<>();
			TfOrderSubDetailBusiParam tempParam = null;
			for(int i  = 0; i < busiParamList.size(); i ++){
				tempParam = busiParamList.get(i);
				preMap.put(tempParam.getSkuBusiParamName(),tempParam.getSkuBusiParamValue());
			}

			//增加受理中校验参数
			preMap.put("PRE_TYPE","1");
			Map result = broadBandService.broadBandContinuePayByMap(preMap, new BroadbandRenewCondition());
			logger.info("宽带续费业务受理中校验接口返回： " + result);

			if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
				//throw new Exception("【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
				resMap.put("code", "1");
				resMap.put("message", "当前宽带账号不能进行续费,宽带续费业务受理中校验失败:【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+ " message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
				return resMap;
			}
			//----增加受理中校验接口结束。。。


			//设置订单会员关联信息
            TfOrderSub orderSub = new TfOrderSub();
            TfOrderUserRef orderUserRef = new TfOrderUserRef();
            MemberVo memberVo = UserUtils.getLoginUser(request);
            if(memberVo == null){//未登录
                orderUserRef.setMemberId(1L);
                orderUserRef.setMemberLogingName("游客");
            }else{//已登录
                MemberLogin memberLogin = memberVo.getMemberLogin();
                orderUserRef.setMemberId(memberLogin.getMemberId());
                orderUserRef.setMemberLogingName(memberLogin.getMemberLogingName());
                orderUserRef.setContactPhone(String.valueOf(memberLogin.getMemberPhone()));
            }
            orderSub.setOrderUserRef(orderUserRef);

            //设置订单明细信息
            TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
            orderSubDetail.setGoodsId(broadbandItem.getGoodsId());
            orderSubDetail.setGoodsSkuId(broadbandItem.getGoodsSkuId());
            orderSubDetail.setGoodsName(goodsInfoList.get(0).getGoodsName());
            orderSubDetail.setGoodsSkuPrice(goodsSkuPrice);  
            orderSubDetail.setGoodsSkuNum(1L);
            orderSubDetail.setRootCateId(OrderConstant.CATE_BROADBAND_CONTINUE);
            orderSubDetail.setGoodsFormat(goodsFormat);
            orderSubDetail.setShopId(BroadbandConstants.SHOP_ID);
            orderSubDetail.setShopName(BroadbandConstants.SHOP_NAME);
            orderSubDetail.setShopTypeId(6);

    		orderSubDetail.setOrderSubDetailBusiParams(busiParamList);
    		orderSub.addOrderDetail(orderSubDetail);
    		
    		/*设置订单信息*/
    		orderSub.setShopId(BroadbandConstants.SHOP_ID);//TODO 平台级店铺，暂时设置1
    		orderSub.setShopName(BroadbandConstants.SHOP_NAME);//
    		orderSub.setOrderTypeId(OrderConstant.TYPE_BROADBAND_CONTINUE);    //---宽带续费订单
    		orderSub.setOrderSubAmount(goodsSkuPrice);//总额
    		orderSub.setOrderSubPayAmount(goodsSkuPrice);//支付金额
    		orderSub.setOrderSubDiscountAmount(0L);//优惠金额
    		//orderSub.setOrderSubPayStatus("Y");//支付状态：已支付
    		//orderSub.setOrderStatusId(12);//订单状态：已归档，完成
    		orderSub.setOrderChannelCode("E007");//渠道编码，WAP商城
    		orderSub.setPayModeId(2);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
    		orderSub.setDeliveryModeId(3);//配送方式，虚拟商品，不需要配送
    		orderSub.setIsInvoicing(0);//是否开发票:0-不开
    		List<TfOrderSub> orderSubList= orderService.newOrder(orderSub);
            session.setAttribute("productName",broadbandInfo.getGoodsName());
            session.setAttribute("productDesc",broadbandInfo.getProdName());
            session.setAttribute("orderNos",orderSubList.get(0).getOrderSubNo());
            resMap.put("code", "0");
			resMap.put("message", "生成订单成功");
			resMap.put("orderSub", orderSubList.get(0));
        } catch (Exception e) {
        	resMap.put("code", "1");
 			resMap.put("message", "生成订单异常");
            e.printStackTrace();
        }
        return resMap;
    }

	/**
	 * 虚拟订单结果页面
	 * @param model
	 * @return
     */
	@RequestMapping("tempResult")
	public String tempResult(Model model){
		return "web/goods/broadband/renew/tempResult";
	}


	/**
     * 支付订单
     * @param carModel
     */
    @RequestMapping("/payOrder")
    public void payOrder(HttpServletRequest request, HttpServletResponse response, UserGoodsCarModel carModel){
        try {
        	String  orderNo = request.getParameter("orderSubNo");
        	String payPlatform = request.getParameter("payPlatform");
        	String eparchyCode = request.getParameter("eparchyCode");
        	Map<String, Object> resultMap = orderService.mergeOrder(orderNo);
        	TfOrder order = (TfOrder)resultMap.get("order");
            //List<TfOrderSub> orderSubList = (List<TfOrderSub>)resultMap.get("orderSubList");
           //分润规则
            //Short typeId = CommonParams.PAY_PLATFORM.get(payPlatform);  //账户类型Id 2:和包支付,3:支付宝支付
            //if(typeId == null){
               //typeId = 2;
            //}
            //支付账户类型ID
            CompanyInfo companyInfo = new CompanyInfo();
            companyInfo.setChannelCityCode(eparchyCode);
            companyInfo.setCompanyTypeId((short)7);
            CompanyAcctInfo eparchyAcctInfo = companyAcctService.getAcctByChannelAndType(companyInfo,BroadbandConstants.PAY_PLATFORM.get(payPlatform));


			String shRule = "";
			if (eparchyAcctInfo != null) {
				shRule = eparchyAcctInfo.getAccountNum()+"^"+order.getOrderAmount()+"^宽带续费";
			}
            
        	//结算价格
            Long orderAmount = Long.valueOf(order.getOrderAmount()+"");
            //同步页面返回地址
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String callbackUrl = basePath+"/broadband/toPayResult";
            // 异步页面回调地址
            String notifyCallbackUrl = afterOrderPayUrl+"/broadband/payAfterNotify";
            
            /*订单支付接口调用*/
            TfOrderPay orderPay = new TfOrderPay();
            orderPay.setOrderId(order.getOrderId());
            orderPay.setOrderPayAmount(orderAmount);
            orderPay.setOrgCode(payPlatform);
            orderPay.setShRule(shRule);
            orderPay.setHmac(BroadbandConstants.SIGN_KEY);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setMerchantId(BroadbandConstants.MERCHANT_ID);
            //回调页面
            String content  = orderService.applyPay(orderPay, callbackUrl, notifyCallbackUrl);
            String encode = CommonParams.PAY_CHARSET.get(payPlatform);
            if(encode == null){
                encode = "GBK";
            }
            response.setContentType("text/html;charset=" + encode);
            PrintWriter out = response.getWriter();
            out.print(content);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    
    
    /**
     * 支付完成回调
     * @param model
     * @param returnCode
     * @param chargeflowId
     * @return
     * @throws Exception 
     */
    @RequestMapping("toPayResult")
    public String toPayResult(HttpServletRequest request,HttpServletResponse response,Model model,String returnCode ,Long chargeflowId,String merchantId) throws Exception{
    	logger.info("单宽带续费,支付同步回调参数:returnCode="+returnCode+",chargeflowId="+chargeflowId);
        
    	TfOrderSub orderSub = new TfOrderSub();
        orderSub.setOrderId(chargeflowId);
        List<TfOrderSub> orderSubList = orderQueryService.queryBaseOrderList(orderSub);
        model.addAttribute("orderSub",orderSubList.get(0));
        model.addAttribute("returnCode",returnCode);
        return "web/goods/broadband/renew/payResult";
    }
    
    
    /**
     * 跳转到订单支付结果页面，供支付中心调用（异步）
     * @param returnCode
     * @return
     * @throws Exception 
     */
    @RequestMapping("/payAfterNotify")
    public void payAfterNotify(HttpServletRequest request,HttpServletResponse response,String merchantId ,String returnCode,String message,String type,
            String version,Integer amount,Long orderId ,String payDate,
            String status,String orderDate,String payNo,String org_code,
            String organization_payNo,String hmac,String shRule,String accountDate) throws Exception{
    	try{
    		logger.info("宽带续费,支付异步回调参数:"+request.getParameterMap());
    		 MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
             Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
             if(validataService.valHmac(payParamMap,merChantBean)){
            	 TfOrderSub orderSubParam = new TfOrderSub();
                 orderSubParam.setOrderId(orderId);
            	 	orderSubParam.setPage(new Page<TfOrderSub>(1,100));
                 Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
                 List<TfOrderSub> orderSubList = orderSubPage.getList();
                 if(orderSubList!=null && orderSubList.size()>0 ){
                	 TfOrderSub orderSub = orderSubList.get(0);
                	 if(orderSub.getDetailList()!=null && orderSub.getDetailList().size()>0){
                		 TfOrderSubDetail orderSubDetail = orderSub.getDetailList().get(0);
                		 TfOrderSubDetailBusiParam orderSubDetailBusiParam = new TfOrderSubDetailBusiParam();
                		 orderSubDetailBusiParam.setSkuBusiParamName("DEAL_TIME");
                		 orderSubDetailBusiParam.setSkuBusiParamValue(accountDate);
                		 orderSubDetailBusiParam.setSkuBusiParamDesc("会计日期");
                		 orderSubDetailBusiParam.setOrderSubDetailId(orderSubDetail.getOrderSubDetailId());
                		 List<TfOrderSubDetailBusiParam> busiParamList  = Lists.newArrayList();
                		 busiParamList.add(orderSubDetailBusiParam);
                		 orderService.saveBusinessParam(busiParamList);
                	  }
                 }
                 //订单支付信息
                 TfOrderPay orderPay = new TfOrderPay();
                 orderPay.setMerchantId(merchantId);
                 orderPay.setMessage(message);
                 orderPay.setOrderHarvestExpend("0");
                 orderPay.setOrderId(orderId);
                 orderPay.setOrderPayAmount((long)amount);
                 orderPay.setOrderPayTime(org.apache.commons.lang3.time.DateUtils.parseDate(payDate, "yyyyMMddHHmmss"));
                 orderPay.setOrgCode(org_code);
                 orderPay.setPayLogId(organization_payNo);
                 orderPay.setPayNo(payNo);
                 orderPay.setPayState(status);
                 orderPay.setReturnCode(returnCode);
                 orderPay.setType(type);
                 orderPay.setVersion(version);
                 OrderProcessParam param = new OrderProcessParam();
                 param.setBusinessId(String.valueOf(orderId));
                 param.setOrderStatusId(OrderConstant.STATUS_PAY);
                 param.setAct(1);
                 param.put(Variables.ORDER_PAY, orderPay);
                 orderService.completeByOrderId(param);//订单流转
                 
                 
                 response.getWriter().print("success");
                 
                 
             }
             else {
            	 logger.info("单宽带续费,支付异步回调验签失败");
            	 response.getWriter().print("fail");
             }
            
    	}
    	catch(Exception e){
    		  logger.error("单宽带续费,支付异步回调失败，异常信息"+e);
    		  response.getWriter().print("fail");
    	}
    	
    }
    
    
    

    /**
     * 三级地址获取
     * @param request
     * @param response
     * @param pId
     * @return
     */
    @RequestMapping(value = "/getChildrenByPid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> getChildrenByPid(HttpServletRequest request,
            HttpServletResponse response, String pId)
    {
    	Map<String, Object> resultmap = new HashMap<String, Object>();
        List<ThirdLevelAddress> addrChildren = memberAddressService
                .getChildrensByPId(pId);
        
        List<MemberAddressResult> addrList = new ArrayList<MemberAddressResult>();
        
        if (!CollectionUtils.isEmpty(addrChildren))
        {
            for (ThirdLevelAddress addr : addrChildren)
            {
            	//过滤掉虚拟地址
				if (!"b2badmin".equals(addr.getOperater())){
					MemberAddressResult res = new MemberAddressResult();
					res.setOrgId(addr.getOrgId());
					res.setOrgName(addr.getOrgName());
					addrList.add(res);
				}
            }
            resultmap.put("flag", "Y");
            resultmap.put("orgList", addrList);
        }
        else
        {
            resultmap.put("flag", "N");
        }
        return resultmap;
    }
    /**
     * 三级地址获取
     * @param request
     * @param response
     * @param cityCode
     * @return
     */
    @RequestMapping(value = "/getCountyFromBoss", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> getCountyFromBoss(HttpServletRequest request,
            HttpServletResponse response, String cityCode)
    {
    	Map<String, Object> resultmap = new HashMap<String, Object>();
		List countyList = new ArrayList();
		QryAddressCityNameCondition condition = new QryAddressCityNameCondition();
		condition.setEPARCHY_CODE(String.valueOf(cityCode));
		try{
			Map addreMap = qryAddressService.qryAddressCityName(condition);
			if("0".equals(addreMap.get("respCode"))){
				List list = (List) addreMap.get("result");
				Map result = (Map) list.get(0);
				countyList = (List) result.get("ADDRESS_INFO");
				if(countyList!=null&&countyList.size()>0){
					resultmap.put("flag", "Y");
					resultmap.put("orgList", countyList);
				}else{
					resultmap.put("flag", "N");
				}
			}else{
				resultmap.put("flag", "N");
			}
		}catch (Exception e){
			resultmap.put("flag", "N");
		}

        return resultmap;
    }

    @RequestMapping("submitApply")
    public String submitApply(HttpServletRequest request, HttpServletResponse response,Model model){
    		Map<String,Object> resultMap = new HashMap<String,Object>();
    		resultMap.put("respCode", "-1");
    		resultMap.put("respDesc", "您的信息提交失败!");
    	try {
    		
    		String phoneNum = request.getParameter("phoneNum");
    		//归属地校验
    		PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
    		phoneAttributionModel.setSerialNumber(phoneNum);
    		resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);

    		if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
    			resultMap.put("respCode", "-1");
    			resultMap.put("respDesc", "请输入湖南移动号码!");
    			model.addAttribute("resultMap",resultMap);
    	    	return "web/goods/broadband/appointment/bookingResult";
    		} 
    		//预约资格校验
        	String productId = request.getParameter("submit_productId");
        	String city = request.getParameter("memberRecipientCity");
        	String cityPre = city.substring(2);
        	if(StringUtils.isNotEmpty(productId)){
        		productId = productId.replace("XX", cityPre);
        	}
        	HeFamilyCheckCondition condition = new HeFamilyCheckCondition();
        	condition.setSerial_number(phoneNum);
        	condition.setProduct_id(productId);
        	resultMap = heFamilyService.heFamilyCheck(condition);
        	if("0".equals(resultMap.get("respCode"))){
				JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
				JSONObject result = (JSONObject) resultArray.get(0);
				String resultCode = (String)result.get("X_RESULTCODE") ;
				String resultInfo = (String)result.get("X_RESULTINFO") ;
				resultMap.put("resultCode", resultCode);
				resultMap.put("respDesc", resultInfo);
			}
        	//校验未通过
        	if(!"0".equals(resultMap.get("respCode"))){
        		model.addAttribute("resultMap",resultMap);
            	return "web/goods/broadband/appointment/bookingResult";
        	}
        	String houseCode = request.getParameter("houseCode");
        	String address = request.getParameter("address");
        	String packageId = request.getParameter("submit_packageId");
        	String discntCode = request.getParameter("submit_discntCode");
        	String hasBroadband = request.getParameter("submit_hasBroadband");
        	String hasMbh = request.getParameter("submit_hasMbh");
        	String contacts = request.getParameter("contacts");
        	
        	
        	if(StringUtils.isNotEmpty(packageId)){
        		packageId = packageId.replace("XX", cityPre);
        	}
        	if(StringUtils.isNotEmpty(discntCode)){
        		discntCode = discntCode.replace("XX", cityPre);
        	}
        	
        	HeFamilyBookingCondition heFamilyBookingCondition = new    HeFamilyBookingCondition();
        	heFamilyBookingCondition.setSerial_number(phoneNum);
        	heFamilyBookingCondition.setProduct_id(productId);
        	heFamilyBookingCondition.setAddr_id(houseCode);
        	heFamilyBookingCondition.setAddr_name(address);
        	heFamilyBookingCondition.setProduct_id(productId);
        	heFamilyBookingCondition.setPackage_id(packageId);
        	heFamilyBookingCondition.setElement_id(discntCode);
        	heFamilyBookingCondition.setInteractive_flag(hasMbh);
        	heFamilyBookingCondition.setBroadband_flag(hasBroadband);
        	
    		
    		
    		 resultMap =  heFamilyService.heFamilyBooking(heFamilyBookingCondition);
    		 //添加预约记录
    		 String ip = getRemoteAddr(request);
    		 HeFamilyBookingRecord heFamilyBookingRecord = new HeFamilyBookingRecord();
    		 heFamilyBookingRecord.setSerialNumber(phoneNum);
    		 heFamilyBookingRecord.setAddrId(houseCode);
    		 heFamilyBookingRecord.setAddrName(address);
    		 heFamilyBookingRecord.setProductId(productId);
    		 heFamilyBookingRecord.setPackageId(packageId);
    		 heFamilyBookingRecord.setElementId(discntCode);
    		 heFamilyBookingRecord.setInteractiveFlag(hasMbh);
    		 heFamilyBookingRecord.setBroadbandFlag(hasBroadband);
    		 heFamilyBookingRecord.setIp(ip);
    		 heFamilyBookingRecord.setRespCode((String)resultMap.get("respCode"));
    		 heFamilyBookingRecord.setRespDesc((String)resultMap.get("respDesc"));
    		 heFamilyBookingRecord.setIsAccount("0");
    		 heFamilyBookingRecord.setChannelCode("E007");
    		 heFamilyBookingRecord.setCustomerName(contacts);
    		 boolean flag = this.heFamilyGoodsService.addHeFamilyBookingRecord(heFamilyBookingRecord);
    		 if(!flag){
    			 logger.error("和宽带预约,调boss接口成功,添加预约记录失败");
    		 }
    	} catch (Exception e) {
			logger.error("和宽带预约:"+e.getMessage());
		}
    	model.addAttribute("resultMap",resultMap);
    	return "web/goods/broadband/appointment/bookingResult";
    }
    
    /**
     * 跳转到宽带服务服务标准
     * @return
     */
    @RequestMapping("serviceStandard")
    public String serviceStandard(HttpServletRequest request){
      
    	return "web/goods/broadband/broadService/serviceStandard";
    }
    
    /**
     * 跳转到宽带服务安装流程
     * @return
     */
    @RequestMapping("installProcedure")
    public String installProcedure(HttpServletRequest request){
      
    	return "web/goods/broadband/broadService/installProcedure";
    }

    /**
     * 跳转到宽带服务故障申告
     * @return
     */
    @RequestMapping("faultReport")
    public String faultReport(HttpServletRequest request){

        return "web/goods/broadband/broadService/faultReport";
    }
    
    /**
     * 跳转到宽带服务自助排障
     * @return
     */
    @RequestMapping("broadSelfHelp")
    public String broadSelfHelp(HttpServletRequest request){

        return "web/goods/broadband/selfHelp/broadSelfHelp";
    }
    
    
    /**
     * 和家庭预约校验 
     * @param request
     * @param response
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("heFamilyCheck")
    public Map<String,Object> heFamilyCheck(HttpServletRequest request,HttpServletResponse response,Model model){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	String phoneNum = request.getParameter("phoneNum");
    	String productId = request.getParameter("productId");
    	String city = request.getParameter("city");
    	if(StringUtils.isEmpty(phoneNum)){
    		resultMap.put("respCode", "-1");
    		resultMap.put("respDesc", "预约手机号码不能为空!");
    		return resultMap;
    	}
    	if(StringUtils.isEmpty(productId)){
    		resultMap.put("respCode", "-1");
    		resultMap.put("respDesc", "产品编码不能为空!");
    		return resultMap;
    	}
    	if(StringUtils.isEmpty(city)){
    		resultMap.put("respCode", "-1");
    		resultMap.put("respDesc", "所属城市不能为空!");
    		return resultMap;
    	}
    	
    	
    	
    	try {
    		
    		PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
    		phoneAttributionModel.setSerialNumber(phoneNum);
    		resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);

    		if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
    			resultMap.put("respCode", "-1");
    			resultMap.put("respDesc", "请输入湖南移动号码!");
    			return resultMap;
    		} 
        	String preStr = city.substring(2);
        	productId = productId.replace("XX",preStr);
        	HeFamilyCheckCondition condition = new HeFamilyCheckCondition();
        	condition.setSerial_number(phoneNum);
        	condition.setProduct_id(productId);
    		
			resultMap = heFamilyService.heFamilyCheck(condition);
			if("0".equals(resultMap.get("respCode"))){
				JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
				JSONObject result = (JSONObject) resultArray.get(0);
				String isBroadBand = (String)result.get("BROADBAND_FLAG") ;
				String isMbh = (String)result.get("INTERACTIVE_FLAG") ;
				String resultCode = (String)result.get("X_RESULTCODE") ;
				String resultInfo = (String)result.get("X_RESULTINFO") ;
				resultMap.put("isBroadBand", isBroadBand);
				resultMap.put("isMbh", isMbh);
				resultMap.put("resultCode", resultCode);
				resultMap.put("respDesc", resultInfo);
			}
			
		} catch (Exception e) {
			logger.error("和家庭预约校验 :"+e.getMessage());
			resultMap.put("respCode", "-1");
    		resultMap.put("respDesc", e.getMessage());
		}
    	
    	return resultMap;
    }
    
    
    
    /**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	/**
   	 * 宽带聚合页跳转-消费叠加型
   	 */
   	@RequestMapping("ap_index")
	public String toAggregatPage_Consupostn(HttpServletRequest request, HttpServletResponse response,Model model){
   		
   		return "web/broadband/agrepage/ap_index";
   	}
   	
   	/**
   	 * 宽带聚合页跳转-宽带续费
   	 */
   	@RequestMapping("ap_lyh") 
	public String toAggregatPage_Renewal(HttpServletRequest request, HttpServletResponse response,Model model){
   		
   		return "web/broadband/agrepage/ap_lyh";
   	}

	/**
	 * 获取消费叠加型商品列表
	 * @return
	 * @throws Exception
	 */
	private List<TfGoodsInfo> getGoodsGiftList() throws Exception{
		Map<String,Object> bbArgs = new HashMap<String,Object>();
		bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","BROADBAND_CATEGORY_ID_SPEED_GIFT"));
		logger.info("bbArgs___"+JSON.toJSONString(bbArgs));
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
		logger.info("consupostnGoodsInfoList___"+JSON.toJSONString(consupostnGoodsInfoList));
		return consupostnGoodsInfoList;
	}
	/**
	 * 获取大王卡商品列表
	 * @return
	 * @throws Exception
	 */
	private List<TfGoodsInfo> getBigKingList() throws Exception{
		Map<String,Object> bbArgs = new HashMap<String,Object>();
		bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","BROADBAND_CATEGORY_ID_BIG_KING"));
		logger.info("bbArgs___"+JSON.toJSONString(bbArgs));
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
		logger.info("consupostnGoodsInfoList___"+JSON.toJSONString(consupostnGoodsInfoList));
		return consupostnGoodsInfoList;
	}


	private Boolean  getMainDisct(String installPhoneNum) throws Exception {
		QueryAccountPackagesCondition queryCondition = new QueryAccountPackagesCondition();
		queryCondition.setSerialNumber(installPhoneNum);
		Boolean isBigKing = false;
		//查询当前套餐
		Map res = queryAccountPackagesService.queryPackageInfo(queryCondition);
		Boolean hasPackage = false;
		if ("0".equals(res.get("respCode")) && res.get("result") != null) {
			JSONArray array = (JSONArray) res.get("result");
			JSONObject resultObj = array.getJSONObject(0);
			Map resMap = (Map) resultObj.get("DISCNT_INFO");
			String discntCode = (String) resMap.get("MAIN_DISCNT_CODE");
			/**
			 * 判断用户是否办理大王卡套餐
			 */
			if(bandCodeMap.get(discntCode)!=null){
				isBigKing = true;
			}
		} else {
			throw new Exception("查询当前套餐失败!");
		}
		return isBigKing;
	}
}