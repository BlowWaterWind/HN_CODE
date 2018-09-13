package com.ai.ecs.ecm.mall.wap.modules.o2o.newO2o;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.entity.order.AcceptFeeCalculateCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.ConsupostnService;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.mobilePayHongBao.service.IMobilePayHongBaoService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.iis.upp.service.IPayBankService;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

@Controller
@RequestMapping("newO2oConsupostn")
public class NewO2oConsupostnController {
	
	private Logger logger = Logger.getLogger(NewO2oConsupostnController.class);
	
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
    private IMobilePayHongBaoService mobilePayHongBaoService;
	
	@Autowired
    private SmsSendService smsSendService;
	
	@Autowired
    private O2oOrderTempService o2oOrderTempService;
	
	 @Autowired
    private O2oOrderParamTemService o2oOrderParamTempService;
    @Autowired
    private QueryAccountPackagesService queryAccountPackagesService;
	
	 private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
	            + "wxyz0123456789+/" + "=";

	/**
	 * 号码填写
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("newInit")
	public String init(HttpServletRequest request , HttpServletResponse response, Model model,String secToken)  throws Exception {
		String streamNo=createStreamNo();
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
				"init",request.getParameterMap(),"o2o组网送宽带办理",request);
	    Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        
		//获取渠道人员手机号码
        MemberVo memberVo=UserUtils.getLoginUser(request);
		model.addAttribute("installPhoneNum", memberVo.getMemberLogin().getMemberPhone());
		ChannelInfo channelInfo = memberVo.getChannelInfo();
		ShopInfo shopInfo=memberVo.getShopInfo();
		model.addAttribute("staffId",channelInfo.getTradeStaffId());
		model.addAttribute("shopId",shopInfo.getShopId());
		String minCost = request.getParameter("minCost")==null?"0":request.getParameter("minCost");
		String busiType = request.getParameter("busiType")==null?"0":request.getParameter("busiType");
        model.addAttribute("minCost",minCost);
        model.addAttribute("busiType",busiType);
		model.addAttribute("secToken",secToken);
		writerFlowLogExitMenthod(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),
				"init",null,objectToMap(busiType),"o2o组网送宽带办理");
    	return "web/broadband/newO2o/consupostn/init";
	}
		 
	 
	/**
	 * 办理页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("newIndex")
	public String index(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "index",request.getParameterMap(),"o2o消费叠加型办理",request);
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        String hasAddress = request.getParameter("hasAddress");
        
		//获取办理用户手机号码
        String installPhoneNum = request.getParameter("installPhoneNum");
		model.addAttribute("installPhoneNum", installPhoneNum);
        
		//校验魔百和和宽带安装情况
		Map<String,String> checkResMap = isMbhAndBroadBand(installPhoneNum);
		String isBroadBand = checkResMap.get("isBroadBand");
		String isMbh = checkResMap.get("isMbh");
		model.addAttribute("isBroadBand",isBroadBand);
		model.addAttribute("isMbh",isMbh);
		if("0".equals(isBroadBand)){
			 //已经办理宽带
			try{
				BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
				broadbandDetailInfoCondition.setSerialNumber(installPhoneNum);
				BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);
				
				model.addAttribute("broadbandDetailInfoResult",broadbandDetailInfoResult);
	        }catch (Exception e) {
	            e.printStackTrace();
				writerFlowLogThrowable(streamNo,"","",getClass().getName(),"index",null,"o2o已有宽带用户信息查询:"+processThrowableMessage(e));
	            logger.error("已有宽带用户信息查询："+e.getMessage());
	        }
			if(!"Y".equalsIgnoreCase(hasAddress)){
				return "web/broadband/newO2o/consupostn/addr";
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
        	if("0".equals(minCost)){
        		if(consupostnAllItemList.get(i).getBroadbandItemName().contains("组家庭网送宽带")){
            		consupostnItemList.add(consupostnAllItemList.get(i));
            	}
        	}else{
        		if(consupostnAllItemList.get(i).getBroadbandItemName().contains("消费叠加型保底消费"+minCost)){
            		consupostnItemList.add(consupostnAllItemList.get(i));
            	}
        	}
        }
        if(consupostnItemList.size() == 0){
        	resultMap.put("respDesc", "参数错误，请确认访问的地址是否正确！");
            model.addAttribute("resultMap",resultMap);
            return "web/broadband/newO2o/newHeFamily/checkError";
        }
        Collections.reverse(consupostnItemList);
        model.addAttribute("consupostnItemList",consupostnItemList);
        
        //获取手机号码的归属地市编码
        String EPARCHY_CODE = this.getEparchyCode(installPhoneNum);
        model.addAttribute("eparchy_Code", EPARCHY_CODE);
        
        String busiType = request.getParameter("busiType");//消费叠加型办理类型：0-组网送宽带 1-个人版   2-家庭版

        if(busiType.equals("0")) {
//            QueryAccountPackagesCondition queryCondition = new QueryAccountPackagesCondition();
//            queryCondition.setSerialNumber(installPhoneNum);
//            //查询当前套餐
//            Long curentPackage = 0L;
//            Map res = queryAccountPackagesService.queryPackageInfo(queryCondition);
//            Boolean hasPackage = false;
//            if ("0".equals(res.get("respCode")) && res.get("result") != null) {
//                JSONArray array = (JSONArray) res.get("result");
//                JSONObject resultObj = array.getJSONObject(0);
//                Map resMap = (Map) resultObj.get("DISCNT_INFO");
//                String resfee = (String) resMap.get("MAIN_DISCNT_FEE");
//                if(StringUtils.isNotEmpty(resfee)){
//                    curentPackage = Long.parseLong(resfee)/100;
//                }
//                if(curentPackage>=38L){
//                    writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
//                            "index",objectToMap(busiType),objectToMap(checkResMap),"o2o组网送宽带办理");
//                    return "web/broadband/o2o/consupostn/familynet";
//                }else{
//					resultMap.put("respCode", "-1");
//					resultMap.put("respDesc", "主套餐小于38元/月，不能办理!");
//					model.addAttribute("resultMap",resultMap);
//					return "web/goods/broadband/newInstall/checkError";
//                }
//            } else {
//                throw new Exception("查询当前套餐失败!");
//            }
			writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
					"index",objectToMap(busiType),objectToMap(checkResMap),"o2o组网送宽带");
			return "web/broadband/newO2o/consupostn/familynet";
		}
        else if(busiType.equals("1")) {
			writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
					"index",objectToMap(busiType),objectToMap(checkResMap),"o2o消费叠加型办理");
			return "web/broadband/newO2o/consupostn/personal";
		}
        else if(busiType.equals("2")) {
			writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
					"index",objectToMap(busiType),objectToMap(checkResMap),"o2o消费叠加型办理");
			return "web/broadband/newO2o/consupostn/family";
		}
        else{
			return "web/broadband/newO2o/newHeFamily/checkError";
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
	public String familynet(HttpServletRequest request , HttpServletResponse response, Model model,String secToken)  throws Exception {
        //System.out.println("访问到这个页面");
		return init(request,response,model,secToken);
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
		bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_CONSUPOSTN_BROADBAND_CATEGORY_ID"));
		logger.info("bbArgs:" + bbArgs);
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
		
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
	
	@RequestMapping("newGetAreaInfo")
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
	
	@RequestMapping("newGetFamilyNetMemberInfo")
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
        	logger.info("memberListParamStr = " + memberListParamStr);
        	if(memberListParamStr != null && !"".equals(memberListParamStr) && !"null".equals(memberListParamStr)){
        		memberListLength = familyMemberList.size();
        		logger.info("memberListLength = 1");
        	}else{
        		memberListLength = 0;
        		logger.info("memberListLength = 0");
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
	@RequestMapping("newConfirmOrder")
	public String confirmOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		String streamNo=createStreamNo();
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
				"confirmOrder",request.getParameterMap(),"宽带确认下单",request);
		model.addAttribute("phoneId", request.getParameter("phoneId"));
		model.addAttribute("contactPhone", request.getParameter("contactPhone"));
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

		String totalMoney = "";
		if("1".equals(request.getParameter("isBroadBand"))){
			AcceptFeeCalculateCondition condition = new AcceptFeeCalculateCondition();
			MemberVo memberVo = UserUtils.getLoginUser(request);
			condition.setRouteEparchyCode(memberVo.getChannelInfo().getTradeEparchyCode());
			condition.setBusiType("0");
			condition.setStandardAddressId(request.getParameter("houseCode"));
			condition.setModemSaleType("4");
			Map res = broadBandService.acceptFeeCalculate(condition);
			Object resultObject = null;
			if(res != null && "ok".equals(res.get("respDesc"))){
				//返回结果中 respDesc":"ok"
				JSONArray resultArray = JSONArray.parseArray(String.valueOf(res.get("result")));
				resultObject = resultArray.get(0);
				if (resultObject.equals(null)){
					//如果为空，那么就要处理
					Map<String,Object> resultMap = new HashMap<String,Object>();
					resultMap.put("respCode", "-1");
					resultMap.put("respDesc", "接口返回异常");
					model.addAttribute("resultMap",resultMap);
					return "web/broadband/newO2o/newHeFamily/checkError";
				}
			}else{
				Map<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("respCode", "-1");
				resultMap.put("respDesc", "接口返回异常");
				model.addAttribute("resultMap",resultMap);
				return "web/broadband/newO2o/newHeFamily/checkError";
			}

			JSONObject result = (JSONObject) resultObject;
			totalMoney = (String)result.get("totalMoney");
		}
		model.addAttribute("totalMoney", totalMoney);

		writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
				"confirmOrder",null,null,"宽带确认下单");
		return "web/broadband/newO2o/consupostn/confirm";
	}
	
    private Date getCurrentDate(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        Date currentTime_2 = null;
        try {
            currentTime_2 = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTime_2;
    }
	
	/**
	 * 订单提交
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("newSubmitOrder")
	@ResponseBody
	public Map<String,Object> submitTempOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		String streamNo=createStreamNo();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		resultMap.put("code", "-1");
        resultMap.put("message", "您的信息提交失败!");
		writerFlowLogEnterMenthod(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),
				"submitOrder",request.getParameterMap(),"宽带确认订单信息",request);
        long goodsSkuPrice = 0;
        try{
        	String installName = request.getParameter("installName");//装机人姓名
        	String phoneNum = request.getParameter("phoneId");//手机
        	String contactPhone = request.getParameter("contactPhone");//联系人手机号码
        	String idCard = request.getParameter("idCard");//身份证号码
        	String productId = request.getParameter("productId");//产品id
            String giftElementId = request.getParameter("packageId");//魔百和年包元素 0元包

        	String isBroadBand = request.getParameter("isBroadBand");
        	String isMbh = request.getParameter("isMbh");
            String goodsSkuId = request.getParameter("goodsSkuId");

            String totalMoney = request.getParameter("totalMoney");//总费用
            
            String houseCode = request.getParameter("houseCode");//标准地址编码
            String installAddress = request.getParameter("address");//安装地址
            String accessType = request.getParameter("form1_coverType");//接入方式
            String maxWidth = request.getParameter("form1_maxWidth");//最大带宽
            String freePortNum = request.getParameter("form1_freePort");//最大端口数
            
            String payMode = request.getParameter("payMode");//payModeId
//            String payMode = "0";//固定为直接支付
			payMode=StringUtils.isEmpty(payMode)?"0":payMode;
			String eparchyCode = request.getParameter("eparchyCode");//地市编码
            String modemSaleType = "4";//MODEM方式 赠送光猫（3:客户自备;4:自动配发）
            
            String tvType =request.getParameter("form1_Mbh");//魔百和类型 0:芒果 1:未来
//            String tvboxSaleType=tvType;
			String tvboxSaleType = StringUtils.isEmpty(tvType) ? "0" : "1";//机顶盒方式
			if(!StringUtils.isNotEmpty(tvType)){
				isMbh = "0";
			}
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
 			}
            
            String memberListStr = request.getParameter("memberListStr");
            String minCost = request.getParameter("minCost");
            String monthCost = request.getParameter("monthCost");
            
            String tradeTypeCode = "";
            String feeTypeCode = "";
            String feeMode = "";
            String fee = "";


            String staffPwd = request.getParameter("staffPwd");
            staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
			staffPwd =  URLEncoder.encode(staffPwd,"utf-8");
            logger.info("获取渠道部门密码：" + staffPwd);
            //归属地校验
            PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
            phoneAttributionModel.setSerialNumber(phoneNum);
            resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
            if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
                resultMap.put("code", "-1");
            	resultMap.put("message", "请输入湖南移动号码办理!");
                return resultMap;
            }
            
            //办理校验
            if(!"0".equals(minCost)){
	            ConsupostnCheckCondition condition = new ConsupostnCheckCondition();
	            condition.setSerial_number(phoneNum);
	            if ("0".equals(isBroadBand.trim())) {//有宽带信息
	            	condition.setIs_exists("1");
	            }else{
	            	condition.setIs_exists("0");
	            }
	            ChannelInfo channelInfo = memberVo.getChannelInfo();
	            condition.setDepartId(channelInfo.getTradeDepartId());
	    		condition.setCityCode(channelInfo.getTradeCityCode());
	    		condition.setStaffId(channelInfo.getTradeStaffId());
	    		condition.setInModeCode(channelInfo.getInModeCode());
	    		condition.setChanId(channelInfo.getChanelId());
	    		condition.setTradeDepartPassword(staffPwd);
	            resultMap = consupostnServiceImpl.consupostnCheck(condition);
	            //校验未通过
	            logger.error("消费叠加型校验调用CRM接口返回结果：" + resultMap);
	            if(!"0".equals(resultMap.get("respCode")) && !"成功".equals(resultMap.get("respCode"))){
	            	logger.error(">>>>>>>>>>消费叠加型接口校验未通过");
	            	resultMap.put("code", "-1");
	            	resultMap.put("message", resultMap.get("respDesc"));
	                return resultMap;
	            }
            }
            logger.error(">>>>>>>>>>消费叠加型接口校验通过");
            //获取消费叠加型商品信息
            TfGoodsInfo tfGoodsInfo = getGoodsInfo(goodsSkuId);
            BroadbandItemEntity broadbandItem = BroadbandUtils.convertConsupostnItemInfo(tfGoodsInfo);

            //重新拿一遍用户的totalMoney
			String totalMoney1 = "";
			if("1".equals(isBroadBand)){
				AcceptFeeCalculateCondition condition = new AcceptFeeCalculateCondition();
				condition.setRouteEparchyCode(memberVo.getChannelInfo().getTradeEparchyCode());
				condition.setBusiType("0");
				condition.setStandardAddressId(houseCode);
				condition.setModemSaleType("4");
				Map res = broadBandService.acceptFeeCalculate(condition);
				Object resultObject = null;
				if(res != null && "ok".equals(res.get("respDesc"))){
					JSONArray resultArray = JSONArray.parseArray(String.valueOf(res.get("result")));
					resultObject = resultArray.get(0);
					if (resultObject.equals(null)){
						resultMap.put("code", "-1");
						resultMap.put("message","接口返回异常");
						return resultMap;
					}
				}else{
					resultMap.put("code", "-1");
					resultMap.put("message","接口返回异常");
					return resultMap;
				}

				JSONObject result = (JSONObject) resultObject;
				totalMoney1 = (String)result.get("totalMoney");
			}
			if(!totalMoney1.equals(totalMoney)){
				resultMap.put("code", "-1");
				resultMap.put("message","费用异常");
				return resultMap;
			}
			if("0".equals(isBroadBand)){
				goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(broadbandItem.getPrice()+""));
			}else{
                goodsSkuPrice = (long)(Double.parseDouble(AmountUtils.changeY2F(broadbandItem.getPrice()+""))+Double.parseDouble(totalMoney));
			}
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
            
            /*业务参数*/
            O2oParamUtils o2oParamUtils = new O2oParamUtils();
            o2oParamUtils.addParams("TRADE_DEPART_PASSWD", staffPwd, "渠道交易部门密码");
            o2oParamUtils.addParams("SERIAL_NUMBER", phoneNum, "手机号码");
            o2oParamUtils.addParams("PRODUCT_ID", productId, "消费叠加型套餐产品ID");
			o2oParamUtils.addParams("MINCOST", minCost, "保底消费金额");

            //办理校验
            ConsupostnTransactCondition ctcondition = new ConsupostnTransactCondition();
            ctcondition.setSerial_number(phoneNum);
            ctcondition.setProduct_id(productId);
			//预约安装的三个参数都不为空时才能做预约安装
			if (StringUtils.isNotBlank(bookInstallDate) && StringUtils.isNotBlank(bookInstallTime) && StringUtils.isNotBlank(communityType)) {
				o2oParamUtils.addParams("BOOK_INSTALL_DATE",bookInstallDate,"预约装机日期");
				o2oParamUtils.addParams("BOOK_INSTALL_TIME",bookInstallTime,"预约装机时间段");
				o2oParamUtils.addParams("COMMUNITY_TYPE",communityType,"小区地址类型");
				ctcondition.setBook_install_date(bookInstallDate);
				ctcondition.setBook_install_time(bookInstallTime);
				ctcondition.setCommunity_type(communityType);
			}

			if("0".equals(minCost)||"98".equals(minCost)||"158".equals(minCost)){
            	o2oParamUtils.addParams("FRIEND_REQ_DATA",memberListStr,"家庭网成员号码");
            	ctcondition.setFriend_req_data(memberListStr);
            }

            List<Map<String, Object>> feeList = new ArrayList<Map<String, Object>>();
            long totalPrice=0;
            if ("1".equals(isBroadBand.trim()) && "1".equals(isMbh.trim())){
            	//无宽带，无魔百和
            	tradeTypeCode = "1002,3709,1002";
                feeTypeCode = "400,420,410";
                feeMode = "0,0,2";
                fee = gmPrice+","+mbhPrice+","+goodsSkuPrice;
                o2oParamUtils.addParams("ADDR_ID", houseCode, "装机地址编码");
                o2oParamUtils.addParams("ADDR_NAME", installAddress, "宽带安装地址名称");
                o2oParamUtils.addParams("ADDR_DESC", installAddress, "装机详细地址");
                o2oParamUtils.addParams("ACCESS_TYPE", accessType, "接入方式");
                o2oParamUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
                o2oParamUtils.addParams("FREE_PORT_NUM", freePortNum, "空闲端口数");
                o2oParamUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
                o2oParamUtils.addParams("TV_TYPE", tvType, "魔百和类型");
        		o2oParamUtils.addParams("TVBOX_SALE_TYPE", tvboxSaleType, "机顶盒方式");
        		o2oParamUtils.addParams("IS_EXISTS", "0", "是否存量宽带用户转入");
        		o2oParamUtils.addParams("CONTACT",installName,"联系人");
                o2oParamUtils.addParams("CONTACT_PHONE",contactPhone,"联系方式");
                o2oParamUtils.addParams("TRADE_TYPE_CODE", tradeTypeCode, "订单类型");
                o2oParamUtils.addParams("FEE_TYPE_CODE", feeTypeCode, "费用类型");
                o2oParamUtils.addParams("FEE_MODE", feeMode, "营业费");
                o2oParamUtils.addParams("FEE", fee, "应缴");
                o2oParamUtils.addParams("PAY", fee, "实缴");
                
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
                ctcondition.setContact_phone(contactPhone);

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
                
            	o2oParamUtils.addParams("TV_TYPE", tvType, "魔百和类型");
        		o2oParamUtils.addParams("TVBOX_SALE_TYPE", tvboxSaleType, "机顶盒方式");
        		o2oParamUtils.addParams("IS_EXISTS", "1", "是否存量宽带用户转入");
        		o2oParamUtils.addParams("TRADE_TYPE_CODE", tradeTypeCode, "订单类型");
                o2oParamUtils.addParams("FEE_TYPE_CODE", feeTypeCode, "费用类型");
                o2oParamUtils.addParams("FEE_MODE", feeMode, "营业费");
                o2oParamUtils.addParams("FEE", fee, "应缴");
                o2oParamUtils.addParams("PAY", fee, "实缴");
        		
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
            	o2oParamUtils.addParams("IS_EXISTS", "1", "是否存量宽带用户转入");
            	ctcondition.setIs_exists("1");
            }else if("1".equals(isBroadBand.trim()) && "0".equals(isMbh.trim())){//无宽带 有魔百盒
            	tradeTypeCode = "1002,1002";
                feeTypeCode = "400,410";
                feeMode = "0,2";
                fee = gmPrice+","+goodsSkuPrice;
                
                o2oParamUtils.addParams("ADDR_ID", houseCode, "装机地址编码");
                o2oParamUtils.addParams("ADDR_NAME", installAddress, "宽带安装地址名称");
                o2oParamUtils.addParams("ADDR_DESC", installAddress, "装机详细地址");
                o2oParamUtils.addParams("ACCESS_TYPE", accessType, "接入方式");
                o2oParamUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
                o2oParamUtils.addParams("FREE_PORT_NUM", freePortNum, "空闲端口数");
                o2oParamUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
        		o2oParamUtils.addParams("IS_EXISTS", "0", "是否存量宽带用户转入");
        		o2oParamUtils.addParams("CONTACT",installName,"联系人");
                o2oParamUtils.addParams("CONTACT_PHONE",phoneNum,"联系方式");
                o2oParamUtils.addParams("TRADE_TYPE_CODE", tradeTypeCode, "订单类型");
                o2oParamUtils.addParams("FEE_TYPE_CODE", feeTypeCode, "费用类型");
                o2oParamUtils.addParams("FEE_MODE", feeMode, "营业费");
                o2oParamUtils.addParams("FEE", fee, "应缴");
                o2oParamUtils.addParams("PAY", fee, "实缴");
                
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
            
            ctcondition.setFeeList(o2oParamUtils.parseFeeList(feeList));
            ctcondition.setPre_type("1");

			if(Integer.valueOf(payMode).intValue()==OrderConstant.PAY_WAY_CONSUME_FIRST){
				List<Map<String,Object>> xTradeList = new ArrayList<Map<String,Object>>();
				Map<String,Object> xTradeMap = new HashMap<String,Object>();
				xTradeMap.put("PAY_MONEY_CODE", "7");
				xTradeMap.put("MONEY", totalPrice);
				xTradeList.add(xTradeMap);
				ctcondition.setX_trade_paymoney(xTradeList);//现金0 先装后付7 余额支付6
				o2oParamUtils.addParams("X_TRADE_PAYMONEY", "7,"+totalPrice, "支付方式");
			}
            
            Map<String,Object> checkRes = transactBusi(ctcondition,memberVo.getChannelInfo(),staffPwd);
            logger.error("消费叠加型校验调用CRM接口返回结果：" + checkRes);
            if(!"0".equals(checkRes.get("respCode"))){//校验未通过
            	resultMap.put("code", "-1");
            	resultMap.put("message", checkRes.get("respDesc"));
            	return resultMap;
            }
            
            
            logger.info("获取memberVo" + memberVo);
            logger.info("memberId:" + memberVo.getMemberLogin().getMemberId());
            //----增加受理中校验接口结束。。。
            O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
            orderTempInfo.setOrder_type_id(Long.valueOf(OrderConstant.TYPE_CONSUPOSTN));
            orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
            orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
            orderTempInfo.setContact_phone(phoneNum);
            orderTempInfo.setEparchy_code(eparchyCode);
            orderTempInfo.setCounty(request.getParameter("install_county"));
            orderTempInfo.setGoods_id(broadbandItem.getGoodsId());
            orderTempInfo.setGoods_sku_id(broadbandItem.getGoodsSkuId());
            orderTempInfo.setGoods_name(tfGoodsInfo.getGoodsName());
            orderTempInfo.setGoods_sku_price(totalPrice);
            orderTempInfo.setShop_id(Long.valueOf(memberVo.getShopInfo().getShopId()));
            orderTempInfo.setShop_name(memberVo.getShopInfo().getShopName());
            orderTempInfo.setChannel_code("E050");
            orderTempInfo.setPay_mode_id(Integer.parseInt(payMode));//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
            orderTempInfo.setInsert_time(getCurrentDate());
            orderTempInfo.setGoods_pay_price(totalPrice);
            orderTempInfo.setGoods_format(goodsFormat);
            orderTempInfo.setRoot_cate_id(OrderConstant.CATE_BROADBAND);
            orderTempInfo.setShop_type_id(6L);
            Long orderTempId = o2oOrderTempService.insert(orderTempInfo);
            
//            o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneNum);
            ChannelInfo channelInfo=memberVo.getChannelInfo();
			if(orderTempInfo.getPay_mode_id().intValue()==2&&orderTempInfo.getGoods_pay_price().longValue()>0){
				channelInfo.setInModeCode("P");
			}
            List<O2oOrderParamTemp> o2oOrderParamTempList = putChannelParams(o2oParamUtils.getParamsList(),channelInfo,eparchyCode);
            
            for(O2oOrderParamTemp paramTemp:o2oOrderParamTempList){
                paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
            }
            o2oOrderParamTempService.batchInsert(o2oOrderParamTempList);
            resultMap.put("code", "0");
            resultMap.put("orderId", orderTempId);
            resultMap.put("broadbandType", OrderConstant.TYPE_CONSUPOSTN);
            resultMap.put("serialNumber", phoneNum);
            resultMap.put("goodsName", tfGoodsInfo.getGoodsName());
			writerFlowLogExitMenthod(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),
					"submitOrder",null,null,"宽带确认订单信息");
        }catch(Exception e){
            logger.error("消费叠加型提交订单异常:"+e.getMessage());
            e.printStackTrace();
			writerFlowLogThrowable(streamNo,"","",getClass().getName(),"submitOrder",null,"消费叠加型提交订单异常:"+processThrowableMessage(e));
            throw e;
        }
		
    	return resultMap;
	}
	
	private Map<String,Object> transactBusi(ConsupostnTransactCondition condition,ChannelInfo channelInfo,String tradeDepartPassword) throws Exception{
		Map<String,Object> resultMap = new HashMap<String,Object>();
		condition.setEparchyCodeFCust(channelInfo.getTradeEparchyCode());
		condition.setDepartId(channelInfo.getTradeDepartId());
		condition.setCityCode(channelInfo.getTradeCityCode());
		condition.setStaffId(channelInfo.getTradeStaffId());
		condition.setInModeCode(channelInfo.getInModeCode());
		condition.setChanId(channelInfo.getChanelId());
		condition.setTradeDepartPassword(tradeDepartPassword);
		resultMap = consupostnServiceImpl.consupostnTransact(condition);
		return resultMap;
	}
	
	/**
     * 从session中获取o2o传递的交易参数
     * @param paramList
     * @param channelInfo
     * @param
     */
    private List<O2oOrderParamTemp> putChannelParams(List<O2oOrderParamTemp> paramList, ChannelInfo channelInfo,String cityCode){
        O2oOrderParamTemp param1 = new O2oOrderParamTemp();
        param1.setParamName("ROUTE_EPARCHY_CODE");
        param1.setParamValue(cityCode);
        param1.setParamDesc("渠道用户市区编号");
        O2oOrderParamTemp param2 = new O2oOrderParamTemp();
        param2.setParamName("IN_MODE_CODE");
        param2.setParamValue(channelInfo.getInModeCode());
        param2.setParamDesc("渠道交易类型");
        O2oOrderParamTemp param3 = new O2oOrderParamTemp();
        param3.setParamName("TRADE_DEPART_ID");
        param3.setParamValue(channelInfo.getTradeDepartId());
        param3.setParamDesc("渠道交易部门编号");
        O2oOrderParamTemp param4 = new O2oOrderParamTemp();
        param4.setParamName("TRADE_CITY_CODE");
        param4.setParamValue(cityCode);
        param4.setParamDesc("渠道交易市区编号");
        O2oOrderParamTemp param5 = new O2oOrderParamTemp();
        param5.setParamName("TRADE_EPARCHY_CODE");
        param5.setParamValue(cityCode);
        param5.setParamDesc("渠道交易区域编码");
        O2oOrderParamTemp param6 = new O2oOrderParamTemp();
        param6.setParamName("TRADE_STAFF_ID");
        param6.setParamValue(channelInfo.getTradeStaffId());
        param6.setParamDesc("渠道交易工号");
        paramList.add(param1);
        paramList.add(param2);
        paramList.add(param3);
        paramList.add(param4);
        paramList.add(param5);
        paramList.add(param6);
        return paramList;
    }
}