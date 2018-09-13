package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.ParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.BusiLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.*;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.queryPackage.entity.QueryAccountPackagesCondition;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.merchant.entity.CompanyInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.*;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.service.IPayBankService;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.common.CommonParams.PAY_CHARSET;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.objectToMap;

@Controller
@RequestMapping("consupostn")
public class ConsupostnController {

	private Logger logger = Logger.getLogger(ConsupostnController.class);

	@Autowired
    private IMemberAddressService memberAddressService;

	@Autowired
    private ConsupostnService consupostnServiceImpl;

	@Autowired
	private HeFamilyService heFamilyService;

	@Autowired
    private IGoodsManageService  goodsManageService;

	@Autowired
    private PhoneAttributionService phoneAttributionService;

	@Autowired
    private DictService dictService;

	@Autowired
    private IOrderService orderService;

	@Autowired
    IOrderQueryService orderQueryService;

	@Autowired
	private IUppHtmlValidataService validataService;

	@Autowired
    ICompanyAcctService companyAcctService;

	@Autowired
    private IPayBankService payBankService;

	@Value("${afterOrderPayUrl}")
    String afterOrderPayUrl;

	@Autowired
    private BroadBandService broadBandService;

	@Autowired
	private CheckIsCanOrderService checkIsCanOrderService;
	@Autowired
	private QryAddressService qryAddressService;
	@Autowired
	private QueryAccountPackagesService queryAccountPackagesService;
	private static Map mainCodeMap = new HashMap();
	static {
		mainCodeMap.put("99544290", "99544290");
		mainCodeMap.put("99544289", "99544290");
		mainCodeMap.put("99544291", "99544290");
	}

	/**i
	 * 首页11
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("init")
	public String init(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		String streamNo=createStreamNo();
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        String hasAddress = request.getParameter("hasAddress");
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
				"init",request.getParameterMap(),"消费叠加型办理",request);
		//获取登录用户手机号码
		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		String installRealName = loginUserInfoMap.get("installRealName");
		model.addAttribute("installPhoneNum", installPhoneNum);
		model.addAttribute("installRealName", installRealName);

		//校验魔百和和宽带安装情况
		Map<String,String> checkResMap = isMbhAndBroadBand(installPhoneNum);
		model.addAttribute("checkResCode",checkResMap.get("checkResCode"));
        model.addAttribute("isBroadBand",checkResMap.get("isBroadBand"));
        model.addAttribute("isMbh",checkResMap.get("isMbh"));
        String isBroadBand = checkResMap.get("isBroadBand");
		String isMbh = checkResMap.get("isMbh");
		if("0".equals(isBroadBand)){
			 //已经办理宽带
			try{
				BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
				broadbandDetailInfoCondition.setSerialNumber(installPhoneNum);
				BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);

				model.addAttribute("broadbandDetailInfoResult",broadbandDetailInfoResult);
	        }catch (Exception e) {
	            e.printStackTrace();
				writerFlowLogThrowable(streamNo,"","",getClass().getName(),"init",null,"已有宽带用户信息查询:"+processThrowableMessage(e));
	            logger.error("已有宽带用户信息查询："+e.getMessage());
	        }
			if(!"Y".equalsIgnoreCase(hasAddress)){
				return "web/goods/broadband/consup/addr";
			}
		}else{

	        //地市信息
	        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
	        //默认区县信息(长沙市)
	        List<ThirdLevelAddress> countyList = null;
	        if (!CollectionUtils.isEmpty(cityList)) {
	            countyList = memberAddressService.getChildrensByPId(cityList.get(0).getOrgId() + "");
	            // 地市信息处理 用于适配Boss接口查询条件
	            for (ThirdLevelAddress city : cityList) {
	                city.setOrgName(city.getOrgName().replace("市", ""));
	                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
	                    city.setOrgName("湘西州");
	                }
	            }
	        }

	        model.addAttribute("cityList", cityList);
	        model.addAttribute("countyList", countyList);
		}

        String minCost = request.getParameter("minCost");//保底消费
        model.addAttribute("minCost",minCost);

        List<TfGoodsInfo> goodsList = getGoodsList();
        List<BroadbandItemEntity> consupostnAllItemList = BroadbandUtils.convertConsupostnItemEntityList(goodsList);

        List<BroadbandItemEntity> consupostnItemList = new ArrayList<BroadbandItemEntity>();
        for(int i=0;i<consupostnAllItemList.size();i++){
        	if(consupostnAllItemList.get(i).getBroadbandItemName().contains("消费叠加型保底消费"+minCost)){
        		consupostnItemList.add(consupostnAllItemList.get(i));
        	}
        }
        if(consupostnItemList.size() == 0){
        	resultMap.put("respDesc", "参数错误，请确认访问的地址是否正确！");
            model.addAttribute("resultMap",resultMap);
            return "web/goods/broadband/newInstall/checkError";
        }
        Collections.reverse(consupostnItemList);
        model.addAttribute("consupostnItemList",consupostnItemList);

        //获取手机号码的归属地市编码
        String EPARCHY_CODE = this.getEparchyCode(installPhoneNum);
        model.addAttribute("eparchy_Code", EPARCHY_CODE);

        String busiType = request.getParameter("busiType");//消费叠加型办理类型：1-个人版   2-家庭版

        if(busiType.equals("1")) {
			writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
					"init",objectToMap(busiType),objectToMap(checkResMap),"个人版48元档消费叠加型下单");
			return "web/goods/broadband/consup/personal";
		}
        else if(busiType.equals("2")) {
			writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
					"init",objectToMap(busiType),objectToMap(checkResMap),"家庭版158元档消费叠加型下单");
			return "web/goods/broadband/consup/family";
		}
        else{
            return "web/broadband/checkError";
        }
	}

	/**
	 * 跳转到消费叠加型-组网送宽带办理页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("familynet")
	public String familynet(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		String streamNo=createStreamNo();
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
				"familynet",request.getParameterMap(),"组网送宽带办理",request);
        String hasAddress = request.getParameter("hasAddress");
        model.addAttribute("minCost","0");
        //获取登录用户手机号码
		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		String installRealName = loginUserInfoMap.get("installRealName");
		model.addAttribute("installPhoneNum", installPhoneNum);
		model.addAttribute("installRealName", installRealName);

		//获取手机号码的归属地市编码
        String EPARCHY_CODE = this.getEparchyCode(installPhoneNum);
        model.addAttribute("eparchy_Code", EPARCHY_CODE);

        //校验魔百和和宽带安装情况
  		Map<String,String> checkResMap = isMbhAndBroadBand(installPhoneNum);
  		model.addAttribute("checkResCode",checkResMap.get("checkResCode"));
        model.addAttribute("isBroadBand",checkResMap.get("isBroadBand"));
        model.addAttribute("isMbh",checkResMap.get("isMbh"));
        String isBroadBand = checkResMap.get("isBroadBand");
        if("0".equals(isBroadBand)){
			 //已经办理宽带
			try{
				BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
				broadbandDetailInfoCondition.setSerialNumber(installPhoneNum);
				BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);

				model.addAttribute("broadbandDetailInfoResult",broadbandDetailInfoResult);
	        }catch (Exception e) {
	            e.printStackTrace();
				writerFlowLogThrowable(streamNo,"","",getClass().getName(),"familynet",null,"已有宽带用户信息查询:"+processThrowableMessage(e));
	            logger.error("已有宽带用户信息查询："+e.getMessage());
	        }

			if(!"Y".equalsIgnoreCase(hasAddress)){
				return "web/goods/broadband/consup/addr";
			}

		}

		List<TfGoodsInfo> goodsList = getGoodsList();
        List<BroadbandItemEntity> consupostnAllItemList = BroadbandUtils.convertConsupostnItemEntityList(goodsList);
        List<BroadbandItemEntity> consupostnItemList = new ArrayList<BroadbandItemEntity>();
        for(int i=0;i<consupostnAllItemList.size();i++){
        	if(consupostnAllItemList.get(i).getBroadbandItemName().contains("组家庭网送宽带")){
        		consupostnItemList.add(consupostnAllItemList.get(i));
        	}
        }
        Collections.reverse(consupostnItemList);
        model.addAttribute("consupostnItemList",consupostnItemList);

        Session session = UserUtils.getSession();
        if (session.getAttribute("bandAccount") == null) {
        	String bandAccount = null;
            bandAccount = findBandAccountByPhone(installPhoneNum);
            if (StringUtils.isNotBlank(bandAccount)) {
                session.setAttribute("bandAccount", bandAccount);
            }
        }
//		QueryAccountPackagesCondition queryCondition = new QueryAccountPackagesCondition();
//		queryCondition.setSerialNumber(installPhoneNum);
//		//查询当前套餐
//		Long curentPackage = 0L;
//		Map res = queryAccountPackagesService.queryPackageInfo(queryCondition);
//		Boolean hasPackage = false;
//		if ("0".equals(res.get("respCode")) && res.get("result") != null) {
//			JSONArray array = (JSONArray) res.get("result");
//			JSONObject resultObj = array.getJSONObject(0);
//			Map resMap = (Map) resultObj.get("DISCNT_INFO");
//			String resfee = (String) resMap.get("MAIN_DISCNT_FEE");
//			if(StringUtils.isNotEmpty(resfee)){
//				curentPackage = Long.parseLong(resfee)/100;
//			}
//			if(curentPackage>=38L){
//				writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
//						"familynet",null,objectToMap(consupostnItemList),"组家庭网送宽带");
//				return "web/goods/broadband/consup/familynet";
//			}else{
//				resultMap.put("respCode", "-1");
//				resultMap.put("respDesc", "主套餐小于38元/月，不能办理!");
//				model.addAttribute("resultMap",resultMap);
//				return "web/goods/broadband/newInstall/checkError";
//			}
//		} else {
//			throw new Exception("查询当前套餐失败!");
//		}
		writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
				"familynet",null,objectToMap(consupostnItemList),"组家庭网送宽带");
		return "web/goods/broadband/consup/familynet";
	}

	/**
	 * 跳转到消费叠加型-任我用98元送100M
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("freeGift100M")
	public String freeGift100M(HttpServletRequest request , HttpServletResponse response, Model model,String goodsSkuId)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");

		String hasAddress = request.getParameter("hasAddress");
		model.addAttribute("minCost","0");
		//获取登录用户手机号码
		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		String installRealName = loginUserInfoMap.get("installRealName");
		model.addAttribute("installPhoneNum", installPhoneNum);
		model.addAttribute("installRealName", installRealName);

		//获取手机号码的归属地市编码
		String EPARCHY_CODE = this.getEparchyCode(installPhoneNum);
		model.addAttribute("eparchy_Code",EPARCHY_CODE);

		//校验魔百和和宽带安装情况
		Map<String,String> checkResMap = isMbhAndBroadBand(installPhoneNum);
		model.addAttribute("checkResCode",checkResMap.get("checkResCode"));
//		model.addAttribute("isBroadBand",checkResMap.get("isBroadBand"));
//		model.addAttribute("isMbh",checkResMap.get("isMbh"));

		List<TfGoodsInfo> goodsList = getGoodsGiftList();
		List<BroadbandItemEntity> consupostnAllItemList = BroadbandUtils.convertConsupostnItemEntityList(goodsList);
		List<BroadbandItemEntity> consupostnItemList = new ArrayList<BroadbandItemEntity>();
			BroadbandItemEntity goodsTemp = new BroadbandItemEntity();
		for(int i=0;i<consupostnAllItemList.size();i++){
			if(goodsSkuId.equals(String.valueOf(consupostnAllItemList.get(i).getGoodsSkuId()))){
				consupostnItemList.add(consupostnAllItemList.get(i));
				goodsTemp = consupostnAllItemList.get(i);

			}
		}
		CheckIsCanOrderCondition checkCondition = new CheckIsCanOrderCondition();
		checkCondition.setExistParamcode(goodsTemp.getExistParamCode());
		checkCondition.setOrderParamcode(goodsTemp.getOrderParamCode());
		checkCondition.setProductId(goodsTemp.getProductId());
		checkCondition.setSerialNumber(installPhoneNum);
		Map<String,Object> result = checkIsCanOrderService.checkIsCanOrder(checkCondition);
		List resultData = (List) result.get("result");
		Map responseData = (Map) resultData.get(0);
		if(!"0".equals(responseData.get("RESULT_CODE"))){
			resultMap.put("respCode", "-1");
			resultMap.put("respDesc", responseData.get("RESULT_INFO"));
			model.addAttribute("resultMap",resultMap);
			return "web/goods/broadband/newInstall/checkError";
		}

		Collections.reverse(consupostnItemList);
		model.addAttribute("consupostnItemList",consupostnItemList);
		model.addAttribute("goodsTemp",goodsTemp);

		Session session = UserUtils.getSession();
		if (session.getAttribute("bandAccount") == null) {
			String bandAccount = null;
			bandAccount = findBandAccountByPhone(installPhoneNum);
			if (StringUtils.isNotBlank(bandAccount)) {
				session.setAttribute("bandAccount", bandAccount);
			}
		}

		return "web/goods/broadband/consup/freeGift100M";
	}

	/**
	 * 跳转到消费叠加型-大王卡宽带送100M
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("bigKing")
	public String bigKing(HttpServletRequest request , HttpServletResponse response, Model model,String goodsSkuId)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");

		String hasAddress = request.getParameter("hasAddress");
		model.addAttribute("minCost","0");
		//获取登录用户手机号码
		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		String installRealName = loginUserInfoMap.get("installRealName");
		model.addAttribute("installPhoneNum", installPhoneNum);
		model.addAttribute("installRealName", installRealName);

		//获取手机号码的归属地市编码
		String EPARCHY_CODE = this.getEparchyCode(installPhoneNum);
		model.addAttribute("eparchy_Code",EPARCHY_CODE);

		//校验魔百和和宽带安装情况
		Map<String,String> checkResMap = isMbhAndBroadBand(installPhoneNum);
		model.addAttribute("checkResCode",checkResMap.get("checkResCode"));
//		model.addAttribute("isBroadBand",checkResMap.get("isBroadBand"));
//		model.addAttribute("isMbh",checkResMap.get("isMbh"));

		if(getMainDisct(installPhoneNum)){
			List<TfGoodsInfo> kingList = getBigKingList();
			if(kingList!=null&&kingList.size()>0){
				List<BroadbandItemEntity> kingItemList = BroadbandUtils.convertConsupostnItemEntityList(kingList);
				Collections.reverse(kingItemList);
				model.addAttribute("consupostnItemList",kingItemList);
				model.addAttribute("goodsTemp",kingItemList.get(0));
			}
		}else{
			resultMap.put("respCode", "-1");
			resultMap.put("respDesc", "用户无大王卡套餐无法办理");
			model.addAttribute("resultMap",resultMap);
			return "web/goods/broadband/newInstall/checkError";
		}

		Session session = UserUtils.getSession();
		if (session.getAttribute("bandAccount") == null) {
			String bandAccount = null;
			bandAccount = findBandAccountByPhone(installPhoneNum);
			if (StringUtils.isNotBlank(bandAccount)) {
				session.setAttribute("bandAccount", bandAccount);
			}
		}

		return "web/goods/broadband/consup/bigKing";
	}

	/**
	 * 获取登录用户手机号码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Map<String,String> getLoginUserNum(HttpServletRequest request) throws Exception{
		Map<String,String> resMap = new HashMap<String,String>();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		String installPhoneNum = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
		if (installPhoneNum == null || "".equals(installPhoneNum)) {
            logger.info("您的帐号没有绑定手机号码");
            throw new Exception("您的帐号没有绑定手机号码");
        }
		String installRealName = String.valueOf(memberVo.getMemberLogin().getMemberLogingName());
		resMap.put("installPhoneNum", installPhoneNum);
		resMap.put("installRealName", installRealName);
		return resMap;
	}

	/**
	 * 判断号码是否办理宽带和魔百和
	 * @return resMap
	 * @return resultCode 返回编码
	 * @return isBroadBand 是否办理宽带
	 * @return isMbh 是否办理魔百和
	 * @throws Exception
	 */
	private Map<String,String> isMbhAndBroadBand(String installPhoneNum) throws Exception{
		Map<String,String> resMap = new HashMap<String,String>();
        HeFamilyCheckCondition infoCondition = new HeFamilyCheckCondition();
        infoCondition.setSerial_number(installPhoneNum);
        Map<String,Object> resultMap = heFamilyService.heFamilyCheck(infoCondition);
        JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
        JSONObject result = (JSONObject) resultArray.get(0);
        logger.error("判断号码是否办理宽带和魔百和接口返回串：" + result);
        resMap.put("checkResCode", (String)result.get("X_RESULTCODE"));
        resMap.put("isBroadBand", (String)result.get("BROADBAND_FLAG"));
        resMap.put("isMbh", (String)result.get("INTERACTIVE_FLAG"));
        return resMap;
	}

	/**
	 * 获取手机号码的归属地市编码
	 * @param installPhoneNum
	 * @return
	 * @throws Exception
	 */
	private String getEparchyCode(String installPhoneNum) throws Exception{
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(installPhoneNum);
        Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        return String.valueOf(((Map)((List)resultMap.get("result")).get(0)).get("AREA_CODE"));
	}

	/**
	 * 获取宽带账户信息
	 * @param request
	 * @return
	 */
    private String findBandAccountByPhone(String phone) throws Exception {
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(phone);
        BroadbandDetailInfoResult result = null;
        result = broadBandService.broadbandDetailInfo(condition);
        logger.info("broadbandDetailInfo result：" + net.sf.json.JSONObject.fromObject(result).toString());
        if (result != null && result.getBroadbandDetailInfo() != null) {
            return result.getBroadbandDetailInfo().getAccessAcct();
        }
        return null;
    }

	/**
	 * 获取消费叠加型商品列表
	 * @return
	 * @throws Exception
	 */
	private List<TfGoodsInfo> getGoodsList() throws Exception{
		Map<String,Object> bbArgs = new HashMap<String,Object>();
		bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","CONSUPOSTN_BROADBAND_CATEGORY_ID"));
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);

		return consupostnGoodsInfoList;
	}
	/**
	 * 获取消费叠加型商品列表
	 * @return
	 * @throws Exception
	 */
	private List<TfGoodsInfo> getGoodsGiftList() throws Exception{
		Map<String,Object> bbArgs = new HashMap<String,Object>();
		bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","BROADBAND_CATEGORY_ID_SPEED_GIFT"));
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
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
		logger.info("bbArgs___"+ JSON.toJSONString(bbArgs));
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
		logger.info("consupostnGoodsInfoList___"+JSON.toJSONString(consupostnGoodsInfoList));
		return consupostnGoodsInfoList;
	}


	/**
	 * 获取消费叠加型商品信息
	 * @param skuId
	 * @return TfGoodsInfo
	 * @throws Exception
	 */
	private TfGoodsInfo getGoodsInfo(String skuId) throws Exception{
		Map<String, Object> map = new HashMap<>();
        map.put("goodsSkuId", skuId);
        map.put("containGoodsSkuIdInfo",true);
        map.put("containGoodsBusiParam",true);
        map.put("containGoodsStaticInfo",true);
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

	    return tfGoodsInfo;
	}

	@RequestMapping("getAreaInfo")
	@ResponseBody
	public Map<String,Object> getAreaInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resMap = new HashMap<String,Object>();
        //地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
        //默认区县信息(长沙市)
        List<ThirdLevelAddress> countyList = null;
        if (!CollectionUtils.isEmpty(cityList)) {
            countyList = memberAddressService.getChildrensByPId(cityList.get(0).getOrgId() + "");
            // 地市信息处理 用于适配Boss接口查询条件
            for (ThirdLevelAddress city : cityList) {
                city.setOrgName(city.getOrgName().replace("市", ""));
                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                    city.setOrgName("湘西州");
                }
            }
        }
        resMap.put("cityList", cityList);
        resMap.put("countyList", countyList);

		return resMap;
	}

	@RequestMapping("getFamilyNetMemberInfo")
	@ResponseBody
	public Map<String,Object> getFamilyNetMemberInfo(HttpServletRequest request, HttpServletResponse response,String phoneId,
			String productId,String discntCode) throws Exception{
		Map<String,Object> resMap = new HashMap<String,Object>();
		Map<String,Object> resultMap = new HashMap<String,Object>();

		String memberListParamStr = "";//成员参数
		int memberListLength = 0;//成员数
		List<String> otherMemberList = new ArrayList<String>();//其他成员号码列表
		String memberFlag = "1";//号码主副卡标记，1为主卡，2为副卡

		FamilyNetMemberQryCondition condition = new FamilyNetMemberQryCondition();
		condition.setSerialNumber(phoneId);
		condition.setProductId(productId);
		condition.setRelationTypeCode("45");
		condition.setDiscntCode(discntCode);
		resultMap = consupostnServiceImpl.qryOtherFmyMembersBySn(condition);

        logger.error("家庭网成员查询结果：" + resultMap);
        if(!"0".equals(resultMap.get("respCode")) && !"成功".equals(resultMap.get("respCode"))){
        	resMap.put("resCode", "-1");
        	resMap.put("respDesc", "很抱歉，家庭网成员查询失败！");
        	logger.info("家庭网成员验证失败");
        }else{
        	List familyMemberList = (List) resultMap.get("result");
			memberListLength = familyMemberList.size();
        	for(int i=0;i<familyMemberList.size();i++){
        		if(i == familyMemberList.size()-1){
        			memberListParamStr += ((Map) (familyMemberList).get(i)).get("SERIAL_NUMBER_B");
        		}else{
        			memberListParamStr += ((Map) (familyMemberList).get(i)).get("SERIAL_NUMBER_B") + "|";
        		}

        		otherMemberList.add((String)((Map) (familyMemberList).get(i)).get("ROLE_CODE_B"));
        	}

        	//根据ROLE_CODE_B参数，如果存在1，则为查询号码为副卡，如果全为2，则为查询号码为主卡
        	for(int j=0;j<otherMemberList.size();j++){
        		if("1".equals(otherMemberList.get(j))){
        			memberFlag = "2";
        			break;
        		}
        	}

        	resMap.put("resCode", "0");
        	resMap.put("memberListParamStr", memberListParamStr);
        	resMap.put("memberListLength", memberListLength);
        	resMap.put("memberFlag", memberFlag);
        }

		return resMap;
	}

	/**
	 * 订单确认
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("confirmOrder")
	public String confirmOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		String streamNo=createStreamNo();
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
				"confirmOrder",request.getParameterMap(),"确认办理",request);
		model.addAttribute("phoneId", request.getParameter("phoneId"));
		model.addAttribute("installRealName", request.getParameter("installRealName"));
		model.addAttribute("minCost", request.getParameter("minCost"));
		model.addAttribute("monthCost", request.getParameter("monthCost"));
		model.addAttribute("bandWidth", request.getParameter("bandWidth"));
		model.addAttribute("price", request.getParameter("price"));
		model.addAttribute("goodsSkuId", request.getParameter("goodsSkuId"));
		model.addAttribute("productId", request.getParameter("productId"));
		model.addAttribute("packageId", request.getParameter("packageId"));
		model.addAttribute("eparchyCode", request.getParameter("eparchyCode"));
		model.addAttribute("isMbh", request.getParameter("isMbh"));
		model.addAttribute("isBroadBand", request.getParameter("isBroadBand"));
		model.addAttribute("form1_maxWidth", request.getParameter("form1_maxWidth"));
		model.addAttribute("form1_freePort", request.getParameter("form1_freePort"));
		model.addAttribute("eparchyCode", request.getParameter("eparchyCode"));
		model.addAttribute("form1_coverType", request.getParameter("form1_coverType"));
		model.addAttribute("install_county", request.getParameter("install_county"));
		model.addAttribute("houseCode", request.getParameter("houseCode"));
		model.addAttribute("address", request.getParameter("address"));
		model.addAttribute("installName", request.getParameter("installName"));
		model.addAttribute("idCard", request.getParameter("idCard"));
		model.addAttribute("isBookInstall", request.getParameter("isBookInstall"));
		model.addAttribute("bookInstallDate", request.getParameter("bookInstallDate"));
		model.addAttribute("bookInstallTime", request.getParameter("bookInstallTime"));
		model.addAttribute("form1_communityType",request.getParameter("form1_communityType"));
		model.addAttribute("form1_Mbh", request.getParameter("form1_Mbh"));
		model.addAttribute("accessAcct", request.getParameter("accessAcct"));
		model.addAttribute("memberListStr", request.getParameter("memberListParamStr"));
		model.addAttribute("payMode", request.getParameter("payMode"));
		model.addAttribute("payModeName", request.getParameter("payModeName"));
		writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
				"confirmOrder",null,null,"确认办理");
		return "web/goods/broadband/consup/confirm";
	}
	/**
	 * 任我行宽带赠送-订单确认
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("confirmGiftOrder")
	public String confirmGiftOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{

		model.addAttribute("phoneId", request.getParameter("phoneId"));
		model.addAttribute("installRealName", request.getParameter("installRealName"));
		model.addAttribute("minCost", request.getParameter("minCost"));
		model.addAttribute("monthCost", request.getParameter("monthCost"));
		model.addAttribute("bandWidth", request.getParameter("bandWidth"));
		model.addAttribute("price", request.getParameter("price"));
		model.addAttribute("goodsSkuId", request.getParameter("goodsSkuId"));
		model.addAttribute("productId", request.getParameter("productId"));
		model.addAttribute("packageId", request.getParameter("packageId"));
		model.addAttribute("eparchyCode", request.getParameter("eparchyCode"));
		model.addAttribute("isMbh", request.getParameter("isMbh"));
		model.addAttribute("isBroadBand", request.getParameter("isBroadBand"));
		model.addAttribute("form1_maxWidth", request.getParameter("form1_maxWidth"));
		model.addAttribute("form1_freePort", request.getParameter("form1_freePort"));
		model.addAttribute("eparchyCode", request.getParameter("eparchyCode"));
		model.addAttribute("form1_coverType", request.getParameter("form1_coverType"));
		model.addAttribute("communityType", request.getParameter("communityType"));
		model.addAttribute("install_county", request.getParameter("install_county"));
		model.addAttribute("houseCode", request.getParameter("houseCode"));
		model.addAttribute("address", request.getParameter("address"));
		model.addAttribute("installName", request.getParameter("installName"));
		model.addAttribute("idCard", request.getParameter("idCard"));
		model.addAttribute("bookInstallDate", request.getParameter("bookInstallDate"));
		model.addAttribute("bookInstallTime", request.getParameter("bookInstallTime"));
		model.addAttribute("form1_Mbh", request.getParameter("form1_Mbh"));
		model.addAttribute("accessAcct", request.getParameter("accessAcct"));
		model.addAttribute("memberListStr", request.getParameter("memberListParamStr"));

		return "web/goods/broadband/consup/giftConfirm";
	}

	/**
	 * 大王卡宽带-订单确认
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("confirmKingOrder")
	public String confirmKingOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{

		model.addAttribute("phoneId", request.getParameter("phoneId"));
		model.addAttribute("installRealName", request.getParameter("installRealName"));
		model.addAttribute("minCost", request.getParameter("minCost"));
		model.addAttribute("monthCost", request.getParameter("monthCost"));
		model.addAttribute("bandWidth", request.getParameter("bandWidth"));
		model.addAttribute("price", request.getParameter("price"));
		model.addAttribute("goodsSkuId", request.getParameter("goodsSkuId"));
		model.addAttribute("productId", request.getParameter("productId"));
		model.addAttribute("packageId", request.getParameter("packageId"));
		model.addAttribute("eparchyCode", request.getParameter("eparchyCode"));
		model.addAttribute("isMbh", request.getParameter("isMbh"));
		model.addAttribute("isBroadBand", request.getParameter("isBroadBand"));
		model.addAttribute("form1_maxWidth", request.getParameter("form1_maxWidth"));
		model.addAttribute("form1_freePort", request.getParameter("form1_freePort"));
		model.addAttribute("eparchyCode", request.getParameter("eparchyCode"));
		model.addAttribute("form1_coverType", request.getParameter("form1_coverType"));
		model.addAttribute("communityType", request.getParameter("communityType"));
		model.addAttribute("install_county", request.getParameter("install_county"));
		model.addAttribute("houseCode", request.getParameter("houseCode"));
		model.addAttribute("address", request.getParameter("address"));
		model.addAttribute("installName", request.getParameter("installName"));
		model.addAttribute("idCard", request.getParameter("idCard"));
		model.addAttribute("bookInstallDate", request.getParameter("bookInstallDate"));
		model.addAttribute("bookInstallTime", request.getParameter("bookInstallTime"));
		model.addAttribute("form1_Mbh", request.getParameter("form1_Mbh"));
		model.addAttribute("accessAcct", request.getParameter("accessAcct"));
		model.addAttribute("memberListStr", request.getParameter("memberListParamStr"));

		return "web/goods/broadband/consup/bigKingConfirm";
	}


	/**
	 * 订单提交
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("submitOrder")
	public String submitOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		String streamNo=createStreamNo();
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
				"submitOrder",request.getParameterMap(),"宽带确认订单信息",request);
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        long goodsSkuPrice = 0;
		long totalPrice=0;
		String payMode = request.getParameter("payMode");//payModeId
		payMode=StringUtils.isEmpty(payMode)?"0":payMode;
		Object tradeId=null;
		try{
        	String installName = request.getParameter("installName");//装机人姓名
//        	String phoneNum = request.getParameter("phoneId");//手机
        	Map<String,String> loginUserInfoMap = getLoginUserNum(request);
    		String phoneNum = loginUserInfoMap.get("installPhoneNum");
			phoneNum = "15074988627";

        	String idCard = request.getParameter("idCard");//身份证号码
        	String productId = request.getParameter("productId");//产品id
            String giftElementId = request.getParameter("packageId");//魔百和年包元素 0元包

        	String isBroadBand = request.getParameter("isBroadBand");
        	String isMbh = request.getParameter("isMbh");
            String goodsSkuId = request.getParameter("goodsSkuId");

            String houseCode = request.getParameter("houseCode");//标准地址编码
            String installAddress = request.getParameter("address");//安装地址
            String accessType = request.getParameter("form1_coverType");//接入方式
            String maxWidth = request.getParameter("form1_maxWidth");//最大带宽
            String freePortNum = request.getParameter("form1_freePort");//最大端口数

            String eparchyCode = request.getParameter("eparchyCode");//地市编码
            String modemSaleType = "4";//MODEM方式 赠送光猫（3:客户自备;4:自动配发）

            String tvType =request.getParameter("form1_Mbh");//魔百和类型 0:芒果 1:未来
            String tvboxSaleType=StringUtils.isEmpty(tvType)?"0":"1";//机顶盒方式


			// 是否预约装机
			String isBookInstall = request.getParameter("isBookInstall");
			// 预约装机日期
			String bookInstallDate = null;
			// 预约装机时间
			String bookInstallTime = null;
			// 小区地址类型
			String communityType = request.getParameter("form1_communityType");
			// 预约装机
			if("1".equals(isBookInstall)){

				String bookInstallDateTemp = request.getParameter("bookInstallDate");//预约安装日期
				String bookInstallTimeTemp = request.getParameter("bookInstallTime");//预约安装日期段
				switch (bookInstallTimeTemp){
					case "上午08:00-12:00":
						bookInstallTime = "0";
						break;
					case "中午12:00-14:00":
						bookInstallTime = "1";
						break;
					case "下午14:00-18:00":
						bookInstallTime = "2";
						break;
					case "晚上18:00-20:00":
						bookInstallTime = "3";
						break;
				}
				// 将YYYY/MM/DD 格式 转为YYYY-MM-DD格式
				bookInstallDate = bookInstallDateTemp.replaceAll("/","-");
				// 2017-12-19 由于发现某些手机浏览器输出时间为MM/DD/YYYY，故这里需要对时间根据年份长度做验证和调整
				String[] verDate = bookInstallDate.split("-");
				if(verDate[0].length() != 4){
					bookInstallDate = verDate[2] + "-" + verDate[1] + "-" + verDate[0];
				}
			}

            String memberListStr = request.getParameter("memberListStr");
            String minCost = request.getParameter("minCost");
            String monthCost = request.getParameter("monthCost");
            String bandWidth = request.getParameter("bandWidth");

            String tradeTypeCode = "";
            String feeTypeCode = "";
            String feeMode = "";
            String fee = "";

            //归属地校验
//            PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
//            phoneAttributionModel.setSerialNumber(phoneNum);
//            resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
//            if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
//                resultMap.put("respCode", "-1");
//                resultMap.put("respDesc", "请输入湖南移动号码办理!");
//                model.addAttribute("resultMap",resultMap);
//                return "web/broadband/checkError";
//            }

            //办理校验
            if(!"0".equals(minCost)){
	            ConsupostnCheckCondition condition = new ConsupostnCheckCondition();
	            condition.setSerial_number(phoneNum);
	            if ("0".equals(isBroadBand.trim())) {//有宽带信息
	            	condition.setIs_exists("1");
	            }else{
	            	condition.setIs_exists("0");
	            }
					resultMap = consupostnServiceImpl.consupostnCheck(condition);
					//校验未通过
					logger.error("消费叠加型校验调用CRM接口返回结果：" + resultMap);
					if(!"0".equals(resultMap.get("respCode")) && !"成功".equals(resultMap.get("respCode"))){
						logger.error(">>>>>>>>>>消费叠加型接口校验未通过");
						resultMap.put("respDesc", resultMap.get("respDesc"));
						model.addAttribute("resultMap",resultMap);
						return "web/goods/broadband/newInstall/checkError";
					}
				}

            logger.error(">>>>>>>>>>消费叠加型接口校验通过");
            //获取消费叠加型商品信息
            TfGoodsInfo tfGoodsInfo = getGoodsInfo(goodsSkuId);
            BroadbandItemEntity broadbandItem = BroadbandUtils.convertConsupostnItemInfo(tfGoodsInfo);
			goodsSkuPrice=Long.parseLong(AmountUtils.changeY2F(broadbandItem.getPrice()+""));
			logger.error("goodsSkuPrice = " + goodsSkuPrice);

			//光猫费用
			long gmPrice=0;
			//机顶盒费用(魔百盒)
			long mbhPrice=0;
			if("4".equals(modemSaleType)&&broadbandItem.getModemPrice()!=null){//光猫自动派发
				gmPrice=broadbandItem.getModemPrice();
			}
			if("1".equals(tvboxSaleType)&&broadbandItem.getStbPrice()!=null){//机顶盒 自动派发
				mbhPrice=broadbandItem.getStbPrice();
			}
            String goodsFormat = broadbandItem.getBroadbandItemName()+"/"+broadbandItem.getTerm();

            /*订单会员关联*/
            TfOrderSub orderSub = new TfOrderSub();
            MemberVo memberVo = UserUtils.getLoginUser(request);
            TfOrderUserRef orderUserRef = new TfOrderUserRef();
            orderUserRef.setMemberId(memberVo.getMemberLogin().getMemberId());
            orderUserRef.setMemberLogingName(memberVo.getMemberLogin().getMemberLogingName());
            orderUserRef.setContactPhone(phoneNum);
            orderUserRef.setEparchyCode(eparchyCode);
            orderUserRef.setCounty(request.getParameter("install_county"));
            orderSub.setOrderUserRef(orderUserRef);

            /*明细信息*/
            TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
            orderSubDetail.setGoodsId(broadbandItem.getGoodsId());
            orderSubDetail.setGoodsSkuId(broadbandItem.getGoodsSkuId());
            orderSubDetail.setGoodsName(tfGoodsInfo.getGoodsName());
            orderSubDetail.setGoodsSkuPrice(goodsSkuPrice);
            orderSubDetail.setGoodsSkuNum(1L);
            orderSubDetail.setRootCateId(OrderConstant.CATE_BROADBAND);
            orderSubDetail.setGoodsFormat(goodsFormat);
            orderSubDetail.setShopId(BroadbandConstants.SHOP_ID);
            orderSubDetail.setShopName(BroadbandConstants.SHOP_NAME);
            orderSubDetail.setShopTypeId(6);

            /*业务参数*/
            ParamUtils paramUtils = new ParamUtils();
            paramUtils.addParams("SERIAL_NUMBER", phoneNum, "手机号码");
            paramUtils.addParams("PRODUCT_ID", productId, "消费叠加型套餐产品ID");
			paramUtils.addParams("MINCOST", minCost, "保底消费金额");
			paramUtils.addParams("BANDWIDTH", bandWidth, "带宽");
			paramUtils.addParams("ROUTE_EPARCHY_CODE", eparchyCode, "渠道用户市区编号");

            //办理校验
            ConsupostnTransactCondition ctcondition = new ConsupostnTransactCondition();
            ctcondition.setSerial_number(phoneNum);
            ctcondition.setProduct_id(productId);
			//预约安装的三个参数都不为空时才能做预约安装
			if (StringUtils.isNotBlank(bookInstallDate) && StringUtils.isNotBlank(bookInstallTime) && StringUtils.isNotBlank(communityType)) {
				paramUtils.addParams("BOOK_INSTALL_DATE",bookInstallDate,"预约装机日期");
				paramUtils.addParams("BOOK_INSTALL_TIME",bookInstallTime,"预约装机时间段");
				paramUtils.addParams("COMMUNITY_TYPE",communityType,"小区地址类型");
				ctcondition.setBook_install_date(bookInstallDate);
				ctcondition.setBook_install_time(bookInstallTime);
				ctcondition.setCommunity_type(communityType);
			}
            if("0".equals(minCost)||"98".equals(minCost)||"158".equals(minCost)){
            	paramUtils.addParams("FRIEND_REQ_DATA",memberListStr,"家庭网成员号码");
            	ctcondition.setFriend_req_data(memberListStr);

            }

            List<Map<String,Object>> feeList = new ArrayList<Map<String,Object>>();

            if ("1".equals(isBroadBand.trim()) && "1".equals(isMbh.trim())){
            	//无宽带，无魔百和
            	tradeTypeCode = "1002,3709,1002";
                feeTypeCode = "400,420,410";
                feeMode = "0,0,2";
				fee = gmPrice+","+mbhPrice+","+goodsSkuPrice;
                paramUtils.addParams("ADDR_ID", houseCode, "装机地址编码");
                paramUtils.addParams("ADDR_NAME", installAddress, "宽带安装地址名称");
                paramUtils.addParams("ADDR_DESC", installAddress, "装机详细地址");
                paramUtils.addParams("ACCESS_TYPE", accessType, "接入方式");
                paramUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
                paramUtils.addParams("FREE_PORT_NUM", freePortNum, "空闲端口数");
                paramUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
                paramUtils.addParams("TV_TYPE", tvType, "魔百和类型");
        		paramUtils.addParams("TVBOX_SALE_TYPE", tvboxSaleType, "机顶盒方式");
        		paramUtils.addParams("IS_EXISTS", "0", "是否存量宽带用户转入");
        		paramUtils.addParams("CONTACT",installName,"联系人");
                paramUtils.addParams("CONTACT_PHONE",phoneNum,"联系方式");
                paramUtils.addParams("TRADE_TYPE_CODE", tradeTypeCode, "订单类型");
                paramUtils.addParams("FEE_TYPE_CODE", feeTypeCode, "费用类型");
                paramUtils.addParams("FEE_MODE", feeMode, "营业费");
                paramUtils.addParams("FEE", fee, "应缴");
                paramUtils.addParams("PAY", fee, "实缴");

                ctcondition.setAddr_id(houseCode);
                ctcondition.setAddr_name(installAddress);
                ctcondition.setAddr_desc(installAddress);
                ctcondition.setAccess_type(accessType);
                ctcondition.setMax_width(maxWidth);
                ctcondition.setFree_port_num(freePortNum);
                ctcondition.setModem_sale_type(modemSaleType);
                ctcondition.setTv_type(tvType);
                ctcondition.setTvbox_sale_type(tvboxSaleType);
                ctcondition.setIs_exists("0");
                ctcondition.setContact(installName);
                ctcondition.setContact_phone(phoneNum);

                Map<String,Object> feeMap = new HashMap<String,Object>();
                feeMap.put("TRADE_TYPE_CODE", tradeTypeCode);
                feeMap.put("FEE_TYPE_CODE", feeTypeCode);
                feeMap.put("FEE_MODE", feeMode);
                feeMap.put("FEE", fee);
                feeMap.put("PAY", fee);
                feeList.add(feeMap);
				totalPrice=gmPrice+mbhPrice+goodsSkuPrice;
			}else if("0".equals(isBroadBand.trim()) && "1".equals(isMbh.trim())){//有宽带，无魔百和
            	tradeTypeCode = "3709";
                feeTypeCode = "420";
                feeMode = "0";
				fee = mbhPrice+"";

            	paramUtils.addParams("TV_TYPE", tvType, "魔百和类型");
        		paramUtils.addParams("TVBOX_SALE_TYPE", tvboxSaleType, "机顶盒方式");
        		paramUtils.addParams("IS_EXISTS", "1", "是否存量宽带用户转入");
        		paramUtils.addParams("TRADE_TYPE_CODE", tradeTypeCode, "订单类型");
                paramUtils.addParams("FEE_TYPE_CODE", feeTypeCode, "费用类型");
                paramUtils.addParams("FEE_MODE", feeMode, "营业费");
                paramUtils.addParams("FEE", fee, "应缴");
                paramUtils.addParams("PAY", fee, "实缴");

        		ctcondition.setTv_type(tvType);
                ctcondition.setTvbox_sale_type(tvboxSaleType);
                ctcondition.setIs_exists("1");
                Map<String,Object> feeMap = new HashMap<String,Object>();
                feeMap.put("TRADE_TYPE_CODE", tradeTypeCode);
                feeMap.put("FEE_TYPE_CODE", feeTypeCode);
                feeMap.put("FEE_MODE", feeMode);
                feeMap.put("FEE", fee);
                feeMap.put("PAY", fee);
                feeList.add(feeMap);
				totalPrice=mbhPrice;
			}else if("0".equals(isBroadBand.trim()) && "0".equals(isMbh.trim())) {
            	paramUtils.addParams("IS_EXISTS", "1", "是否存量宽带用户转入");
            	ctcondition.setIs_exists("1");
            }else if("1".equals(isBroadBand.trim()) && "0".equals(isMbh.trim())){
            	tradeTypeCode = "1002,1002";
                feeTypeCode = "400,410";
                feeMode = "0,2";
				fee = gmPrice+","+goodsSkuPrice;

                paramUtils.addParams("ADDR_ID", houseCode, "装机地址编码");
                paramUtils.addParams("ADDR_NAME", installAddress, "宽带安装地址名称");
                paramUtils.addParams("ADDR_DESC", installAddress, "装机详细地址");
                paramUtils.addParams("ACCESS_TYPE", accessType, "接入方式");
                paramUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
                paramUtils.addParams("FREE_PORT_NUM", freePortNum, "空闲端口数");
                paramUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
        		paramUtils.addParams("IS_EXISTS", "0", "是否存量宽带用户转入");
        		paramUtils.addParams("CONTACT",installName,"联系人");
                paramUtils.addParams("CONTACT_PHONE",phoneNum,"联系方式");
                paramUtils.addParams("TRADE_TYPE_CODE", tradeTypeCode, "订单类型");
                paramUtils.addParams("FEE_TYPE_CODE", feeTypeCode, "费用类型");
                paramUtils.addParams("FEE_MODE", feeMode, "营业费");
                paramUtils.addParams("FEE", fee, "应缴");
                paramUtils.addParams("PAY", fee, "实缴");

                ctcondition.setAddr_id(houseCode);
                ctcondition.setAddr_name(installAddress);
                ctcondition.setAddr_desc(installAddress);
                ctcondition.setAccess_type(accessType);
                ctcondition.setMax_width(maxWidth);
                ctcondition.setFree_port_num(freePortNum);
                ctcondition.setModem_sale_type(modemSaleType);
                ctcondition.setIs_exists("0");
                ctcondition.setContact(installName);
                ctcondition.setContact_phone(phoneNum);

                Map<String,Object> feeMap = new HashMap<String,Object>();
                feeMap.put("TRADE_TYPE_CODE", tradeTypeCode);
                feeMap.put("FEE_TYPE_CODE", feeTypeCode);
                feeMap.put("FEE_MODE", feeMode);
                feeMap.put("FEE", fee);
                feeMap.put("PAY", fee);
                feeList.add(feeMap);
				totalPrice=gmPrice+goodsSkuPrice;
			}

//            ctcondition.setFeeList(feeList);
			ctcondition.setFeeList(paramUtils.parseFeeList(feeList));

			ctcondition.setPre_type("1");
//			ctcondition.setStaffId("SUPERUSR");
//			ctcondition.setTradeDepartPassword("ai1234");
			logger.info("消费叠加型办理入参"+JSONObject.toJSONString(ctcondition, SerializerFeature.WriteMapNullValue));
			Map<String,Object> checkRes = transactBusi(ctcondition);
			logger.error("消费叠加型校验调用CRM接口返回结果：" + checkRes);
			if(!"0".equals(checkRes.get("respCode"))){//校验未通过
				checkRes.put("respDesc", checkRes.get("respDesc"));
				model.addAttribute("resultMap",checkRes);
				return "web/goods/broadband/newInstall/checkError";
			}
			if("2".equals(payMode)&&totalPrice>0){
				paramUtils.addParams("IN_MODE_CODE", "P", "渠道交易类型");
			}
            orderSubDetail.setOrderSubDetailBusiParams(paramUtils.getParamsList());
            orderSub.addOrderDetail(orderSubDetail);
            /*设置订单信息*/
            orderSub.setShopId(BroadbandConstants.SHOP_ID);
            orderSub.setShopName(BroadbandConstants.SHOP_NAME);
            orderSub.setOrderTypeId(OrderConstant.TYPE_CONSUPOSTN);
            orderSub.setOrderSubAmount(totalPrice);	//子订单总额
            orderSub.setOrderSubPayAmount(totalPrice);//支付金额
            orderSub.setOrderSubDiscountAmount(0L);//优惠金额
            orderSub.setOrderChannelCode("E007");//渠道编码，wap
            orderSub.setPayModeId(Integer.parseInt(payMode));//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
            orderSub.setDeliveryModeId(3);//配送方式，虚拟商品
            orderSub.setIsInvoicing(0);//是否开发票:0-不开
			orderSub.setPhoneNumber(phoneNum);
			logger.info("消费叠加型办理提交订单参数orderSub:"+JSONArray.toJSONString(orderSub));

            //生成订单
            List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
            model.addAttribute("orderSub",orderSubList.get(0));
            model.addAttribute("orderSubDetail",orderSubList.get(0).getDetailList().get(0));
            model.addAttribute("installAddress",installAddress);
            model.addAttribute("idCard",idCard);
            model.addAttribute("installName",installName);
            model.addAttribute("phoneNum", phoneNum);
            model.addAttribute("eparchyCode",eparchyCode);
			model.addAttribute("resultMap",resultMap);

			List<TfOrderSubDetailBusiParam> paramsList = orderQueryService.queryBusiParamByOrderSub(orderSubList.get(0).getOrderSubId());
			Map<String, Object> paramMap = new HashMap<>();
			for (TfOrderSubDetailBusiParam tfOrderSubDetailBusiParam : paramsList) {
				paramMap.put(tfOrderSubDetailBusiParam.getSkuBusiParamName(),
						tfOrderSubDetailBusiParam.getSkuBusiParamValue());
			}
			tradeId =paramMap.get("CRM_ORDER_ID");
			if(tradeId==null){
				resultMap.put("respCode", "-1");
				resultMap.put("respDesc", "办理失败！");
				model.addAttribute("resultMap",resultMap);
				return "web/broadband/checkError";
			}

		}catch(Exception e){
			writerFlowLogThrowable(streamNo,"","",getClass().getName(),"submitOrder",null,"消费叠加型提交订单异常:"+processThrowableMessage(e));
            logger.error("消费叠加型提交订单异常:"+e.getMessage());
            e.printStackTrace();
            throw e;
        }


        if("2".equals(payMode)&&totalPrice>0&&tradeId!=null){
			writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
					"submitOrder",null,null,"消费叠加型提交");
			return "web/broadband/order/payChoose";
		}else{
			writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
					"submitOrder",null,null,"消费叠加型提交");
			return "/web/goods/broadband/newInstall/orderResult";
		}
	}


	/**
	 * 任我用送宽带-订单提交
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("submitGiftOrder")
	public String submitGiftOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");
		long goodsSkuPrice = 0;
		try{
			String installName = request.getParameter("installName");//装机人姓名
			String phoneNum = request.getParameter("phoneId");//手机
			String idCard = request.getParameter("idCard");//身份证号码
			String productId = request.getParameter("productId");//产品id
			String giftElementId = request.getParameter("packageId");//魔百和年包元素 0元包

			String isBroadBand = request.getParameter("isBroadBand");
			String isMbh = request.getParameter("isMbh");
			String goodsSkuId = request.getParameter("goodsSkuId");

			String houseCode = request.getParameter("houseCode");//标准地址编码
			String installAddress = request.getParameter("address");//安装地址
			String accessType = request.getParameter("form1_coverType");//接入方式
			String maxWidth = request.getParameter("form1_maxWidth");//最大带宽
			String freePortNum = request.getParameter("form1_freePort");//最大端口数
			String communityType = request.getParameter("communityType");//地址类型
			int payModeId = 2;//payModeId 2:在线支付;1:现场支付

			String eparchyCode = request.getParameter("eparchyCode");//地市编码
			String modemSaleType = "4";//MODEM方式 赠送光猫（3:客户自备;4:自动配发）

			String tvType =request.getParameter("form1_Mbh");//魔百和类型 0:芒果 1:未来
			String tvboxSaleType=StringUtils.isEmpty(tvType)?"0":"1";//机顶盒方式

			String bookInstallDateTmp = request.getParameter("bookInstallDate");//预约安装日期
			String bookInstallTimeTmp = request.getParameter("bookInstallTime");//预约安装日期段
			String bookInstallDate = bookInstallDateTmp;

			String memberListStr = request.getParameter("memberListStr");
			String minCost = request.getParameter("minCost");
			String monthCost = request.getParameter("monthCost");

			String tradeTypeCode = "";
			String feeTypeCode = "";
			String feeMode = "";
			String fee = "";

			//归属地校验
			PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
			phoneAttributionModel.setSerialNumber(phoneNum);
			resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
			if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
				resultMap.put("respCode", "-1");
				resultMap.put("respDesc", "请输入湖南移动号码办理!");
				model.addAttribute("resultMap",resultMap);
				return "web/goods/broadband/newInstall/checkError";
			}

			//办理校验
			ConsupostnCheckCondition condition = new ConsupostnCheckCondition();
			condition.setSerial_number(phoneNum);
				condition.setIs_exists("0");
			resultMap = consupostnServiceImpl.consupostnCheck(condition);
			logger.error("消费叠加型校验调用CRM接口返回结果：" + resultMap);
			if(!"0".equals(resultMap.get("respCode")) && !"成功".equals(resultMap.get("respCode"))){
				logger.error(">>>>>>>>>>消费叠加型接口校验未通过");
				resultMap.put("respDesc", resultMap.get("respDesc"));
				model.addAttribute("resultMap",resultMap);
				return "web/goods/broadband/newInstall/checkError";
			}

			logger.error(">>>>>>>>>>消费叠加型接口校验通过");
			//获取消费叠加型商品信息
			TfGoodsInfo tfGoodsInfo = getGoodsInfo(goodsSkuId);
			BroadbandItemEntity broadbandItem = BroadbandUtils.convertConsupostnItemInfo(tfGoodsInfo);
//			goodsSkuPrice = broadbandItem.getPrice().longValue() * 100;
			logger.error("goodsSkuPrice = " + goodsSkuPrice);

			String goodsFormat = broadbandItem.getBroadbandItemName()+"/"+broadbandItem.getTerm();

            /*订单会员关联*/
			TfOrderSub orderSub = new TfOrderSub();
			MemberVo memberVo = UserUtils.getLoginUser(request);
			TfOrderUserRef orderUserRef = new TfOrderUserRef();
			orderUserRef.setMemberId(memberVo.getMemberLogin().getMemberId());
			orderUserRef.setMemberLogingName(memberVo.getMemberLogin()
					.getMemberLogingName());
			orderUserRef.setContactPhone(phoneNum);
			orderUserRef.setEparchyCode(eparchyCode);
			orderUserRef.setCounty(request.getParameter("install_county"));
			orderSub.setOrderUserRef(orderUserRef);

            /*明细信息*/
			TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
			orderSubDetail.setGoodsId(broadbandItem.getGoodsId());
			orderSubDetail.setGoodsSkuId(broadbandItem.getGoodsSkuId());
			orderSubDetail.setGoodsSkuId(broadbandItem.getGoodsSkuId());
			orderSubDetail.setGoodsName(tfGoodsInfo.getGoodsName());
			orderSubDetail.setGoodsSkuPrice(goodsSkuPrice);
			orderSubDetail.setGoodsSkuNum(1L);
			orderSubDetail.setRootCateId(OrderConstant.CATE_BROADBAND);
			orderSubDetail.setGoodsFormat(goodsFormat);
			orderSubDetail.setShopId(BroadbandConstants.SHOP_ID);
			orderSubDetail.setShopName(BroadbandConstants.SHOP_NAME);
			orderSubDetail.setShopTypeId(6);

            /*业务参数*/
			ParamUtils paramUtils = new ParamUtils();
			paramUtils.addParams("SERIAL_NUMBER", phoneNum, "手机号码");
			paramUtils.addParams("PRODUCT_ID", productId, "消费叠加型套餐产品ID");
			paramUtils.addParams("BOOK_INSTALL_DATE",bookInstallDate,"预约装机时间");
			paramUtils.addParams("BOOK_INSTALL_TIME",bookInstallTimeTmp,"预约装机时间");
			paramUtils.addParams("COMMUNITY_TYPE",communityType,"小区地址类型");
			paramUtils.addParams("MINCOST", minCost, "保底消费金额");

			//办理校验
			ConsupostnTransactCondition ctcondition = new ConsupostnTransactCondition();
			ctcondition.setSerial_number(phoneNum);
			ctcondition.setProduct_id(productId);
			ctcondition.setBook_install_date(bookInstallDate);

			List<Map<String,Object>> feeList = new ArrayList<Map<String,Object>>();

//			if ("1".equals(isBroadBand.trim()) && "1".equals(isMbh.trim())){
				//无宽带，无魔百和
				tradeTypeCode = "1002,3709,1002";
				feeTypeCode = "400,420,410";
				feeMode = "0,0,2";
				fee = "0,0,0";
				paramUtils.addParams("ADDR_ID", houseCode, "装机地址编码");
				paramUtils.addParams("ADDR_NAME", installAddress, "宽带安装地址名称");
				paramUtils.addParams("ADDR_DESC", installAddress, "装机详细地址");
				paramUtils.addParams("ACCESS_TYPE", accessType, "接入方式");
				paramUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
				paramUtils.addParams("FREE_PORT_NUM", freePortNum, "空闲端口数");
				paramUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
				paramUtils.addParams("TV_TYPE", tvType, "魔百和类型");
				paramUtils.addParams("TVBOX_SALE_TYPE", tvboxSaleType, "机顶盒方式");
				paramUtils.addParams("IS_EXISTS", "0", "是否存量宽带用户转入");
				paramUtils.addParams("CONTACT",installName,"联系人");
				paramUtils.addParams("CONTACT_PHONE",phoneNum,"联系方式");
				paramUtils.addParams("TRADE_TYPE_CODE", tradeTypeCode, "订单类型");
				paramUtils.addParams("FEE_TYPE_CODE", feeTypeCode, "费用类型");
				paramUtils.addParams("FEE_MODE", feeMode, "营业费");
				paramUtils.addParams("FEE", fee, "应缴");
				paramUtils.addParams("PAY", fee, "实缴");

				ctcondition.setAddr_id(houseCode);
				ctcondition.setAddr_name(installAddress);
				ctcondition.setAddr_desc(installAddress);
				ctcondition.setAccess_type(accessType);
				ctcondition.setMax_width(maxWidth);
				ctcondition.setFree_port_num(freePortNum);
				ctcondition.setModem_sale_type(modemSaleType);
				ctcondition.setTv_type(tvType);
				ctcondition.setTvbox_sale_type(tvboxSaleType);
				ctcondition.setIs_exists("0");
				ctcondition.setContact(installName);
				ctcondition.setContact_phone(phoneNum);
				ctcondition.setBook_install_time(bookInstallTimeTmp);
				ctcondition.setCommunity_type(communityType);

				Map<String,Object> feeMap = new HashMap<String,Object>();
				feeMap.put("TRADE_TYPE_CODE", tradeTypeCode);
				feeMap.put("FEE_TYPE_CODE", feeTypeCode);
				feeMap.put("FEE_MODE", feeMode);
				feeMap.put("FEE", fee);
				feeMap.put("PAY", fee);
				feeList.add(feeMap);
//			}

			ctcondition.setFeeList(feeList);
			ctcondition.setPre_type("1");
			Map<String,Object> checkRes = transactBusi(ctcondition);
			logger.error("消费叠加型校验调用CRM接口返回结果：" + checkRes);
			if(!"0".equals(checkRes.get("respCode"))){//校验未通过
				checkRes.put("respDesc", checkRes.get("respDesc"));
				model.addAttribute("resultMap",checkRes);
				return "web/goods/broadband/newInstall/checkError";
			}

			orderSubDetail.setOrderSubDetailBusiParams(paramUtils.getParamsList());
			orderSub.addOrderDetail(orderSubDetail);
            /*设置订单信息*/
			orderSub.setShopId(BroadbandConstants.SHOP_ID);
			orderSub.setShopName(BroadbandConstants.SHOP_NAME);
			orderSub.setOrderTypeId(OrderConstant.TYPE_CONSUPOSTN);
			orderSub.setOrderSubAmount(goodsSkuPrice);	//子订单总额
			orderSub.setOrderSubPayAmount(goodsSkuPrice);//支付金额
			orderSub.setOrderSubDiscountAmount(0L);//优惠金额
			orderSub.setOrderChannelCode("E007");//渠道编码，wap
			orderSub.setPayModeId(payModeId);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
			orderSub.setDeliveryModeId(3);//配送方式，虚拟商品
			orderSub.setIsInvoicing(0);//是否开发票:0-不开
			logger.info("消费叠加型办理提交订单参数orderSub:"+JSONArray.toJSONString(orderSub));

			//生成订单
			List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);
			logger.info("消费叠加型办理提交订单参数orderSubList:"+JSONArray.toJSONString(orderSubList));
			model.addAttribute("orderSub",orderSubList.get(0));
			model.addAttribute("orderSubDetail",orderSubList.get(0).getDetailList().get(0));
			model.addAttribute("installAddress",installAddress);
			model.addAttribute("idCard",idCard);
			model.addAttribute("installName",installName);
			model.addAttribute("phoneNum",phoneNum);
			model.addAttribute("eparchyCode",eparchyCode);
			model.addAttribute("resultMap",resultMap);
			logger.info("消费叠加型返回结果______________:"+JSONArray.toJSONString(resultMap));
		}catch(Exception e){
			logger.error("消费叠加型提交订单异常:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}

		if(goodsSkuPrice == 0){
			return "/web/goods/broadband/newInstall/orderResult";
		}else{
			return "web/goods/buy/choosePayPlatform";
		}
	}

	private Map<String,Object> transactBusi(ConsupostnTransactCondition condition) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap = consupostnServiceImpl.consupostnTransact(condition);
		return resultMap;
	}

	/**
	 * 订单支付
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("payOrder")
	public void payOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		try {
            String orderSubNo = request.getParameter("payForm_orderSubNo");
            String payPlatform = request.getParameter("payForm_payPlatform");
            String eparchyCode = request.getParameter("payForm_eparchyCode");

            Map<String, Object> resultMap = orderService.mergeOrder(orderSubNo);
            TfOrder order = (TfOrder) resultMap.get("order");
            List<TfOrderSub> orderSubList = (List<TfOrderSub>) resultMap.get("orderSubList");
            Short type = CommonParams.PAY_PLATFORM.get(payPlatform);
            if (type == null) {
                type = 2;
            }
			/* 分润规则 */
            CompanyInfo companyInfo = new CompanyInfo();
            companyInfo.setChannelCityCode(eparchyCode);
            companyInfo.setCompanyTypeId((short) 7);
            CompanyAcctInfo eparchyAcctInfo = companyAcctService.getAcctByChannelAndType(companyInfo,
            		BroadbandConstants.PAY_PLATFORM.get(payPlatform));
            String shRule = eparchyAcctInfo.getAccountNum() + "^"
                    + order.getOrderAmount() + "^消费叠加型立即办理";
            logger.info("消费叠加型立即办理,分润规则:" + shRule);
            // 结算价格
            Long orderAmount = Long.valueOf(order.getOrderAmount() + "");

            // 同步页面回调地址
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
            String callbackUrl = basePath + "/consupostn/toPayResult";
            // 异步页面回调地址
            String notifyCallbackUrl = afterOrderPayUrl + "/consupostn/payAfterNotify";

            TfOrderPay orderPay = new TfOrderPay();
            orderPay.setOrderId(order.getOrderId());
            orderPay.setOrderPayAmount(orderAmount);
            orderPay.setOrgCode(payPlatform);
            orderPay.setShRule(shRule);
            orderPay.setHmac(BroadbandConstants.SIGN_KEY);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setMerchantId(BroadbandConstants.MERCHANT_ID);
            String content = orderService.applyPay(orderPay, callbackUrl,
                    notifyCallbackUrl);
            String encode = PAY_CHARSET.get(payPlatform);
            if (encode == null) {
                encode = "GBK";
            }
            response.setContentType("text/html;charset=" + encode);
            PrintWriter out = response.getWriter();
            out.print(content);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("消费叠加型支付订单异常:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
	}

	/**
     * 跳转到订单支付结果页面，供支付中心调用（同步）
     *
     * @param model
     * @param returnCode
     * @param chargeflowId
     * @return
     * @throws Exception
     */
    @RequestMapping("/toPayResult")
    public String toPayResult(HttpServletRequest request,
                              HttpServletResponse response, Model model, String returnCode,
                              Long chargeflowId, String merchantId) throws Exception {
        logger.info("消费叠加型办理,支付同步回调参数:returnCode=" + returnCode
                + ",chargeflowId=" + chargeflowId);
        TfOrderSub orderSubParam = new TfOrderSub();
        orderSubParam.setOrderId(chargeflowId);
        orderSubParam.setPage(new Page<TfOrderSub>(1, 100));
        Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
        List<TfOrderSub> orderSubList = orderSubPage.getList();
        if (orderSubList != null && orderSubList.size() > 0) {
            TfOrderSub orderSub = orderSubList.get(0);
            if (orderSub.getDetailList() != null
                    && orderSub.getDetailList().size() > 0) {
                TfOrderSubDetail orderSubDetail = orderSub.getDetailList().get(
                        0);
                model.addAttribute("orderSubDetail", orderSubDetail);
            }
        }
        model.addAttribute("returnCode", returnCode);
        return "web/broadband/payResult";
    }

    /**
     * 跳转到订单支付结果页面，供支付中心调用（异步）
     * @return
     * @throws Exception
     */
    @RequestMapping("/payAfterNotify")
    public void payAfterNotify(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        try{
            logger.info("消费叠加型立即办理,支付异步回调参数:"+request.getParameterMap());
            String orderId = request.getParameter("orderId");
            String merchantId = request.getParameter("merchantId");
            String accountDate = request.getParameter("accountDate");

            MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
            Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
            if(!validataService.valHmac(payParamMap,merChantBean)){
                throw new Exception("消费叠加型立即办理:签名验证未通过");
            }

            TfOrderSub orderSubParam = new TfOrderSub();
            orderSubParam.setOrderId(Long.parseLong(orderId));
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

        }
        catch(Exception e){
            logger.error("消费叠加型立即办理,支付异步回调失败，异常信息"+e);
            BusiLogUtils.writerLogging(request,"payAfterNotify","","","","","","","",e,"2","");
        }

    }

	/**
	 * 查询地址类型，城市或者农村
	 * @param addrId
	 * @return
	 * @throws Exception
     */
	@ResponseBody
	@RequestMapping("queryAddress")
	public Map queryAddress(String addrId) throws Exception{
		List<String> bookInstallDateList = new ArrayList<>();

		QryAddressAttrCondition condition=new QryAddressAttrCondition();
		condition.setADDR_ID(addrId);
		condition.setStaffId("ITFWPMAL");
		condition.setTradeDepartPassword("lc9195");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("respCode", "-1");
		if (StringUtils.isBlank(addrId) ) {
			throw new Exception("参数不能为空");
		}
		Map map = qryAddressService.queryAddressAttr(condition);
        if ("0".equals(map.get("respCode")) && map.get("result") != null) {
            JSONArray array = (JSONArray) map.get("result");
            com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
            if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
			resultMap.put("respCode", "0");
                if("02".equals(resultObj.getString("COMMUNITY_TYPE"))||"03".equals(resultObj.getString("COMMUNITY_TYPE"))){//城区
                    bookInstallDateList.add(DateUtils.getDateAfterDays(3));//未来1天
                    bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来2天
                    bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来3天
                }else{
					bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来1天
					bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来2天
					bookInstallDateList.add(DateUtils.getDateAfterDays(6));//未来3天
                }
			resultMap.put("bookInstallDateList",bookInstallDateList);
			resultMap.put("COMMUNITY_TYPE",resultObj.get("COMMUNITY_TYPE"));
            }
            resultMap.put("respDesc", resultObj.getString("X_RESULTINFO"));
        } else {
            resultMap.put("respDesc", map.get("respCode"));
        }
		return resultMap;
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
			if(mainCodeMap.get(discntCode)!=null){
				isBigKing = true;
			}
		} else {
			throw new Exception("查询当前套餐失败!");
		}
		return isBigKing;
	}


}