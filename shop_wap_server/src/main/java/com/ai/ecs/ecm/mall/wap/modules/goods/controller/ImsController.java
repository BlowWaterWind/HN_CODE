package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfo;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoResult;
import com.ai.ecs.ecsite.modules.broadBand.entity.GetImsElementsCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.HeFamilyCheckCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.QrySelectableImsSnCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.netNumServer.entity.VerifyIdCardCondition;
import com.ai.ecs.ecsite.modules.netNumServer.service.NetNumServerService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.entity.base.ConstantsBase;
import com.ai.ecs.exception.ExceptionUtils;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.order.entity.TfOrderDetailSim;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 固话
 * @author 臧弄潮
 */

@Controller
@RequestMapping("ims")
public class ImsController {
	
	private Logger logger = Logger.getLogger(ImsController.class);
	
	@Autowired
    private PhoneAttributionService phoneAttributionService;
	
	@Autowired
    private BroadBandService broadBandService;
	
	@Autowired
	private HeFamilyService heFamilyService;
	
	@Autowired
    private IMemberAddressService memberAddressService;
	
	@Autowired
    private DictService dictService;
	
	@Autowired
    private IGoodsManageService  goodsManageService;
	
	@Autowired
    NetNumServerService netNumServerService;
	
	/**i
	 * 固话加装
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("imsNew")
	public String imsNew(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        
        //获取登录用户手机号码
  		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
  		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		
        //获取手机号码的归属地市编码
        String eparchyCode = this.getEparchyCode(installPhoneNum);		
        model.addAttribute("eparchyCode", eparchyCode);
        
        //宽带套餐
        List<TfGoodsInfo> consupostnGoodsList = getGoodsList("CONSUPOSTN_BROADBAND_CATEGORY_ID");
        List<BroadbandItemEntity> consupostnAllItemList = BroadbandUtils.convertConsupostnItemEntityList(consupostnGoodsList);
        List<TfGoodsInfo> hefamilyGoodsList = getGoodsList("HE_FAMILY_BROADBAND_CATEGORY_ID");
        List<BroadbandItemEntity> heBroadbandItemList = BroadbandUtils.convertHeBroadbandItemEntityList(hefamilyGoodsList);
        String cityPre = eparchyCode.substring(2);
        String productId = "";
        for(int k=0;k<heBroadbandItemList.size();k++){
        	productId = heBroadbandItemList.get(k).getProductId();
	        if (StringUtils.isNotEmpty(productId)) {
	            productId = productId.replace("XX", cityPre);
	            heBroadbandItemList.get(k).setProductId(productId);
	        }
        }
        List<BroadbandItemEntity> familyItemList = new ArrayList<BroadbandItemEntity>();
        List<BroadbandItemEntity> consupostnItemList = new ArrayList<BroadbandItemEntity>();
        for(int i=0;i<consupostnAllItemList.size();i++){
        	if(consupostnAllItemList.get(i).getBroadbandItemName().contains("组家庭网送宽带")){
        		consupostnAllItemList.get(i).setBroadbandItemName("组家庭网送宽带套餐");
        		familyItemList.add(consupostnAllItemList.get(i));
        	}
        	if(consupostnAllItemList.get(i).getBroadbandItemName().contains("消费叠加型保底消费")){
        		if(consupostnAllItemList.get(i).getBroadbandItemName().contains("48")){
        			consupostnAllItemList.get(i).setBroadbandItemName("48元档消费叠加型套餐");
        			consupostnAllItemList.get(i).setPrice(48D);
            		consupostnItemList.add(consupostnAllItemList.get(i));
        		}else if(consupostnAllItemList.get(i).getBroadbandItemName().contains("158")){
        			consupostnAllItemList.get(i).setBroadbandItemName("158元档消费叠加型套餐");
        			consupostnAllItemList.get(i).setPrice(158D);
            		consupostnItemList.add(consupostnAllItemList.get(i));
        		}
        	}
        }
        for(int j=0;j<heBroadbandItemList.size();j++){
        	heBroadbandItemList.get(j).setBroadbandItemName(heBroadbandItemList.get(j).getPrice() + "元和家庭套餐");
        }
        
        model.addAttribute("familyItemList",familyItemList);
        model.addAttribute("consupostnItemList",consupostnItemList);
        model.addAttribute("heBroadbandItemList",heBroadbandItemList);
        
        return "web/goods/broadband/ims/ims_new_index";
	}
	
	/**
	 * 获取宽带套餐列表
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	private List<TfGoodsInfo> getGoodsList(String categoryId) throws Exception{
		Map<String,Object> bbArgs = new HashMap<String,Object>();
		bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID",categoryId));
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);

		return consupostnGoodsInfoList;
	}
	
	/**
	 * 获取可选固话套餐
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getImsInfo")
	@ResponseBody
	public Map<String,Object> getImsInfo(HttpServletRequest request, HttpServletResponse response, String product_id){
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
		try {
	        GetImsElementsCondition getImsElementsCondition = new GetImsElementsCondition();
	        getImsElementsCondition.setQry_product_id(product_id);
	        Map<String,String> loginUserInfoMap = getLoginUserNum(request);
	        String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
	        String eparchyCode = this.getEparchyCode(installPhoneNum);		
	        getImsElementsCondition.setEparchy_code(eparchyCode);
	  		resultMap =  broadBandService.getImsElements(getImsElementsCondition);
	  		logger.info(resultMap);
	  		if("0".equals((String) resultMap.get("respCode"))){
		  		List resList = (List) resultMap.get("result");
		  		List imsList = new ArrayList();
		  		String imsItemName = "";
		  		Map<String,Object> imsMap = new HashMap<String,Object>();
		  		for(int i=0;i<resList.size();i++){
		  			imsMap = new HashMap<String,Object>();
		  			imsItemName = (String)((Map)resList.get(i)).get("PACKAGE_NAME");
		  			imsMap.put("imsItemName", imsItemName.substring(0, 4));
		  			String feeInfo = imsItemName.substring(4,imsItemName.length());
		  			imsMap.put("imsItemFee", Pattern.compile("[^0-9]").matcher(feeInfo).replaceAll(""));
		  			imsMap.put("imsItemProductId", (String)((Map)resList.get(i)).get("PRODUCT_ID"));
		  			imsMap.put("imsItemPackageId", (String)((Map)resList.get(i)).get("PACKAGE_ID"));
		  			imsMap.put("imsEparchyCode", (String)((Map)resList.get(i)).get("STAFF_EPARCHY_CODE"));
		  			List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
		  			for(int j=0;j<cityList.size();j++){
		  				if(eparchyCode.equals(cityList.get(j).getEparchyCode())){
		  					imsMap.put("cityName", cityList.get(i).getOrgName());
		  				}
		  			}
		  			List elementList =  (List)((Map)resList.get(i)).get("ELEMENTS");
		  			for(int j=0;j<elementList.size();j++){
		  				String explainStr = (String)((Map)elementList.get(j)).get("EXPLAIN");
		  				String[] explains = explainStr.split("，");
		  				explainStr = "";
		  				int k = 1;
		  				explainStr += "<p>";
		  				for(String explain:explains){
		  					explainStr += k + ". " + explain; 
		  					if(k < explains.length) explainStr += "<br/>";
		  					k++;
		  				}
		  				explainStr += "</p>";
		  				imsMap.put("explain", explainStr);
		  				String elementName = (String)((Map)elementList.get(j)).get("ELEMENT_NAME");
		  				elementName = Pattern.compile("[^0-9]").matcher(elementName.substring(0, 8)).replaceAll("");//提取优惠资费
		  				imsMap.put("elementName", elementName);		  				imsMap.put("elementId", (String)((Map)elementList.get(j)).get("ELEMENT_ID"));
		  				imsMap.put("expireDate", (String)((Map)elementList.get(j)).get("EXPIRE_DATE"));
		  				
		  				imsList.add(imsMap);
		  			}
		    	}
		  		resultMap.put("imsList",imsList);
	  		}else{
	  			resultMap.put("errorInfo",(String) resultMap.get("respDesc"));
	  		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 获取宽带套餐列表
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("chooseImsNum")
	@ResponseBody
	public Map<String,Object> chooseImsNum(HttpServletRequest request, HttpServletResponse response, String Ims_sn, String eparchyCode){
		Map<String,Object> resMap = new HashMap<String,Object>();
		try {
			QrySelectableImsSnCondition condition = new QrySelectableImsSnCondition();
	  		condition.setIms_sn(Ims_sn);
	  		condition.setRoute_eparchy_code(eparchyCode);
	  		condition.setMatch_position("1");
	  		resMap = broadBandService.qrySelectableImsSn(condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}
	
	/**i
	 * 固话新装确认
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("imsNewDetail")
	public String imsNewDetail(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        
        Map<String,String> loginUserInfoMap = getLoginUserNum(request);
		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		String eparchyCode = request.getParameter("form1_eparchyCode");
		
		List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
		for(int i=0;i<cityList.size();i++){
			if(eparchyCode.equals(cityList.get(i).getEparchyCode())){
				model.addAttribute("cityName", cityList.get(i).getOrgName());
			}
		}
		
		model.addAttribute("eparchyCode", request.getParameter("eparchyCode"));
		
        model.addAttribute("goodsSkuId", request.getParameter("goodsSkuId"));
        model.addAttribute("monthCost", request.getParameter("monthCost"));
        model.addAttribute("bandWidth", request.getParameter("bandWidth"));
        model.addAttribute("price", request.getParameter("price"));
        model.addAttribute("productId", request.getParameter("productId"));
        model.addAttribute("packageId", request.getParameter("packageId"));
        
        model.addAttribute("imsProductId", request.getParameter("imsProductId"));
        model.addAttribute("imsPackageId", request.getParameter("imsPackageId"));
        model.addAttribute("imsOptDiscnt", request.getParameter("imsOptDiscnt"));
        model.addAttribute("imsOptDiscntEnddate", request.getParameter("imsOptDiscntEnddate"));
        model.addAttribute("imsEparchyCode", request.getParameter("imsEparchyCode"));
        
        model.addAttribute("form1_maxWidth", request.getParameter("form1_maxWidth"));
        model.addAttribute("form1_freePort", request.getParameter("form1_freePort"));
        model.addAttribute("form1_eparchyCode", request.getParameter("form1_eparchyCode"));
        model.addAttribute("form1_coverType", request.getParameter("form1_coverType"));
        model.addAttribute("install_county", request.getParameter("install_county"));
        model.addAttribute("houseCode", request.getParameter("houseCode"));
        model.addAttribute("address", request.getParameter("address"));
        
        model.addAttribute("tvType", request.getParameter("tvType"));
        model.addAttribute("tvboxSaleType", request.getParameter("tvboxSaleType"));
		
        model.addAttribute("installPhoneNum", installPhoneNum);
        model.addAttribute("memberListStr", request.getParameter("memberListParamStr"));
        
        model.addAttribute("chooseKdInfo", request.getParameter("chooseKdInfo"));
        model.addAttribute("chooseImsInfo", request.getParameter("chooseImsInfo"));
        
        return "web/goods/broadband/ims/ims_new_detail";
	}
	
	/**
	 * 获取可选固话套餐
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("searchImsNum")
	@ResponseBody
	public Map<String,Object> searchImsNum(HttpServletRequest request, HttpServletResponse response, String searchImsSn, String eparchyCode){
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
		try {
			QrySelectableImsSnCondition condition = new QrySelectableImsSnCondition();
	  		condition.setIms_sn(searchImsSn);
	  		condition.setRoute_eparchy_code(eparchyCode);
	  		condition.setMatch_position("0");//0:任意位置匹配  1:开始位置 2:结束位置
	  		resultMap =  broadBandService.qrySelectableImsSn(condition);
	  		if("0".equals((String) resultMap.get("respCode"))){
		  		List resList = (List) resultMap.get("result");
	  		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**i
	 * 固话加装
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("imsAdd")
	public String imsAdd(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        
        //获取登录用户手机号码
  		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
  		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
  		installPhoneNum = "15116312543";
		
  		//获取宽带信息
  		Map<String,String> broadbandInfoMap = new HashMap<String,String>();
  		Map<String,String> checkResMap = isMbhAndBroadBand(installPhoneNum);
        String isBroadBand = checkResMap.get("isBroadBand");
        if("0".equals(isBroadBand)){
			BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
			broadbandDetailInfoCondition.setSerialNumber(installPhoneNum);
			BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);
			if("0".equals(broadbandDetailInfoResult.getResultCode()) && "成功".equals(broadbandDetailInfoResult.getResultInfo())){
				BroadbandDetailInfo broadbandDetailInfo = broadbandDetailInfoResult.getBroadbandDetailInfo();
				String coverType = broadbandDetailInfo.getCoverType();
				if("FTTH".equals(coverType)){
					broadbandInfoMap.put("mobile", installPhoneNum);
					broadbandInfoMap.put("accessAcct", broadbandDetailInfo.getAccessAcct());
					broadbandInfoMap.put("custName", broadbandDetailInfo.getCustName());
					broadbandInfoMap.put("addrId", broadbandDetailInfo.getAddrId());
					broadbandInfoMap.put("addrDesc", broadbandDetailInfo.getAddrDesc());
					broadbandInfoMap.put("startTime", broadbandDetailInfo.getNewProductsInfo().get(0).getStart_date().substring(0,10));
					broadbandInfoMap.put("productId", broadbandDetailInfo.getNewProductsInfo().get(0).getProduct_id());
					broadbandInfoMap.put("endTime", broadbandDetailInfo.getEndTime().substring(0,10));
					broadbandInfoMap.put("discntName", broadbandDetailInfo.getDiscntName());
					resultMap.put("broadbandInfoMap", broadbandInfoMap);
				}else{
					model.addAttribute("errorInfo","尊敬的用户，您的宽带接口类型不能安装固话！");
					return "web/broadband/checkError";
				}
			}
		}else{
			model.addAttribute("errorInfo","尊敬的用户，您当前账号未办理宽带业务，请先办理宽带业务！");
			return "web/broadband/checkError";
		}
        
        return "web/goods/broadband/ims/ims_add_index";
	}
	
	
	
	/**i
	 * 固话加装
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("imsAddDetail")
	public String imsAddDetail(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
  		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
  		model.addAttribute("installPhoneNum", request.getParameter("installPhoneNum"));
  		//获取手机号码的归属地市编码
        String eparchyCode = this.getEparchyCode(installPhoneNum);		
        model.addAttribute("eparchyCode", eparchyCode);
  		List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
		for(int i=0;i<cityList.size();i++){
			if(eparchyCode.equals(cityList.get(i).getEparchyCode())){
				model.addAttribute("cityName", cityList.get(i).getOrgName());
			}
		}
		//获取地址编码查询小区类型
		model.addAttribute("addrId", request.getParameter("addrId"));
		model.addAttribute("productId", request.getParameter("productId"));
		model.addAttribute("discntName", request.getParameter("discntName"));
		model.addAttribute("chooseKdInfo", request.getParameter("chooseKdInfo"));
		return "web/goods/broadband/ims/ims_add_detail";
	}
	
	@RequestMapping("getLoginUser")
	@ResponseBody
	public Map<String,Object> realityVerify(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败！");
		
        Map<String,String> loginUserInfoMap = getLoginUserNum(request);
  		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
  		
  		if(installPhoneNum != null || !"".equals(installPhoneNum)){
  			resultMap.put("respCode", "0");
  	        resultMap.put("respDesc", "查询成功！");
  			resultMap.put("installPhoneNum", installPhoneNum);
  		}
        
		return resultMap;
	}
	
	/**
	 * 实名制校验
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("realityVerify")
	@ResponseBody
	public Map<String,Object> realityVerify(HttpServletRequest request, HttpServletResponse response, String cardId, String installName,String installPhoneNum){
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        //实名制认证
  		VerifyIdCardCondition verifyIdCardCondition = new VerifyIdCardCondition();
  		verifyIdCardCondition.setCretId(cardId);
  		verifyIdCardCondition.setCretName(installName);
  		verifyIdCardCondition.setCretAddr(TfOrderDetailSim.PAPT_ADDR_DEFAULT);
  		verifyIdCardCondition.setCretSex("0");
  		try {
        	Map map = netNumServerService.verifyIdCard(verifyIdCardCondition);
        	String respCode = (String) map.get("respCode");
        	if ("0".equals(respCode)) {
            	JSONArray result = (JSONArray) map.get("result");
            	if (result != null && result.size() > 0) {
            		Map map0 = (Map) result.get(0);
            		if (!"0000".equals(map0.get("X_RSPCODE"))) {
            			resultMap.put("respDesc", "用户实名制校验未通过！");
            		}else{
            			resultMap.put("respCode", "0");
            	        resultMap.put("respDesc", "用户实名制校验成功!");
            		}
            	} else {
            		resultMap.put("respDesc", "用户实名制校验未通过！");
            	}
        	} else{
        		resultMap.put("respDesc", "用户实名制校验未通过！");
        	}
  		} catch (Exception e) {
  			e.printStackTrace();
  			logger.info("实名制校验失败");
  		}
  		
  		return resultMap;
	}
	
	/**i
	 * 固话加装办理
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("imsAddSubmit")
	public String imsAddSubmit(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		
		
		return null;
	}
	
	/**i
	 * 固话变更
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("imsUpdate")
	public String imsUpdate(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
  		
        //查询号码固话套餐
        
        
        return "web/goods/broadband/ims/ims_add_index";
	}
	
	/**i
	 * 固话变更办理
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("imsUpdateSubmit")
	public String imsUpdateSubmit(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
  		
        //查询号码固话套餐
        
        
        return "web/goods/broadband/ims/ims_add_index";
	}
	
	/**
	 * 获取登录用户手机号码
	 * @param request
	 * @return
	 */
	private Map<String,String> getLoginUserNum(HttpServletRequest request){
		Map<String,String> resMap = new HashMap<String,String>();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		String installPhoneNum = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
		String installRealName = String.valueOf(memberVo.getMemberLogin().getMemberLogingName());
		resMap.put("installPhoneNum", installPhoneNum);
		resMap.put("installRealName", installRealName);
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
	
	@RequestMapping("detail")
	public String detail(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        
        //获取用户宽带信息
		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		installPhoneNum = "15116312543";
		BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
		broadbandDetailInfoCondition.setSerialNumber(installPhoneNum);
		BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);
		if("0".equals(broadbandDetailInfoResult.getResultCode())){
			model.addAttribute("broadbandDetailInfoResult",broadbandDetailInfoResult);
		}
		
		//获取用户选择的固话套餐
		model.addAttribute("imsItemProductId", request.getParameter("imsItemProductId"));
		model.addAttribute("imsItemPackageId", request.getParameter("imsItemPackageId"));
        model.addAttribute("elementId", request.getParameter("elementId"));
        model.addAttribute("imsItemName", request.getParameter("imsItemName"));
        model.addAttribute("installPhoneNum", request.getParameter("installPhoneNum"));
        
        //地址列表
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
        model.addAttribute("cityList", cityList);
        
        //获取手机号码的归属地市编码
        String eparchyCode = this.getEparchyCode(installPhoneNum);		
        model.addAttribute("eparchyCode", eparchyCode);
        
        //安装日期选择列表
        List<String> bookInstallDateList = new ArrayList<>();
        bookInstallDateList.add(DateUtils.getDateAfterDays(1));//未来1天
        bookInstallDateList.add(DateUtils.getDateAfterDays(2));//未来2天
        bookInstallDateList.add(DateUtils.getDateAfterDays(3));//未来3天
        model.addAttribute("bookInstallDateList",bookInstallDateList);
        
        return "web/goods/broadband/ims/detail";
	}
}	
