package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import static com.ai.ecs.ecm.mall.wap.common.CommonParams.PAY_CHARSET;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandCreateCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.BusiLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsCommService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsBusiParam;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsSku;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
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
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 宽带新装Controller
 * Created by tangfan  
 */
@Controller
@RequestMapping("broadbandInstall")
public class BroadbandInstallController extends BaseController{
	
	@Autowired
    private IMemberAddressService memberAddressService;
	
	@Autowired
	private IGoodsCommService goodsCommService;
	
	@Autowired
    private IGoodsManageService goodsManageService;
	
	@Autowired
	private DictService dictService;
	
	 @Autowired
	 private ICompanyAcctService companyAcctService;
	  
	 @Autowired
	 private IOrderService orderService;
	 
	 @Autowired
	 private IOrderQueryService orderQueryService;
	 
	 @Autowired
	 private IUppHtmlValidataService validataService;

	 @Autowired
	 private BroadBandService broadBandServiceImpl;

	@Autowired
	private PhoneAttributionService phoneAttributionService;
	 
	 @Value("${afterOrderPayUrl}")
	 String afterOrderPayUrl;
	
	
	@RequestMapping("broadbandInstall")
	public String broadbandInstall(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		
		//获取登录用户手机号码
		Map<String,String> loginUserInfoMap = getLoginUserNum(request);
		String installPhoneNum = loginUserInfoMap.get("installPhoneNum");
		String installRealName = loginUserInfoMap.get("installRealName");
		model.addAttribute("installPhoneNum", installPhoneNum);
		model.addAttribute("installRealName", installRealName);
		
		//获取手机号码的归属地市编码
        String EPARCHY_CODE = this.getEparchyCode(installPhoneNum);
        model.addAttribute("eparchy_Code", EPARCHY_CODE);
		
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
    	
    	  //宽带新装信息
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
	      String s = JSONArray.toJSONString(goodsInfoList);
	      logger.info(s);
	      List<BroadbandItemEntity> broadbandItemList = null;
	      try {
			broadbandItemList = BroadbandUtils.convertInstallBroadbandItemList(goodsInfoList);
		}  catch (Exception e) {
			logger.error("单宽带新装异常:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
	    model.addAttribute("broadbandItemList",broadbandItemList);
        request.setAttribute("cityList", cityList);
        request.setAttribute("countyList", countyList);
		return "web/goods/broadband/newInstall/newInstall";
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
	
	@RequestMapping("confirmInstall")
	public String confirmInstall(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		try{
			String skuId = request.getParameter("form1_skuId");
			String houseCode = request.getParameter("form1_houseCode");
			String addressName = request.getParameter("form1_addressName");
			String coverType = request.getParameter("form1_coverType");
			String maxWidth = request.getParameter("form1_maxWidth");
			String freePort = request.getParameter("form1_freePort");
			String chooseCat = request.getParameter("form1_chooseCat");
			String eparchyCode = request.getParameter("form1_eparchyCode");
			String county = request.getParameter("form1_county");
			
			//获取SKU信息
			Map<String,Object> map = new HashMap<String,Object>();
	    	map.put("goodsSkuId", skuId);
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
		     BroadbandItemEntity broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
		     
		     model.addAttribute("broadbandItem",broadbandItem);
		     model.addAttribute("houseCode",houseCode);
		     model.addAttribute("addressName",addressName);
		     model.addAttribute("coverType",coverType);
		     model.addAttribute("maxWidth",maxWidth);
		     model.addAttribute("freePort",freePort);
		     model.addAttribute("chooseCat",chooseCat);
		     model.addAttribute("eparchyCode",eparchyCode);
			 model.addAttribute("county",county);
		}
		catch(Exception e){
			logger.error("单宽带新装异常:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return "web/goods/broadband/newInstall/confirmInstallInfo";
	}

	/**
	 * 查询手机号码相关属性信息；
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("queryTelephoneAttribution")
	public Object queryTelephoneAttribution(HttpServletRequest request,HttpServletResponse response,Model model){
		String telephoneNum = request.getParameter("telephoneNum");
		Map<String,Object> resultMap = new HashMap<>();
		if(StringUtils.isBlank(telephoneNum)){
			resultMap.put("respCode", "-1");
			resultMap.put("respDesc", "手机号码为空，查询号码归属地失败!");
		}
		//获取手机号码的归属地市编码
		PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
		phoneAttributionModel.setSerialNumber(telephoneNum);
		try {
			resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
		} catch (Exception e) {
			resultMap.put("respCode", "-1");
			resultMap.put("respDesc", "查询号码归属地失败!");
			e.printStackTrace();
		}
		return  resultMap;
	}
	
	@ResponseBody
	@RequestMapping("submitInstallOrder")
	public Map<String,Object> submitInstallOrder(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception{
		String skuId = request.getParameter("form1_skuId");
		String maxWidth = request.getParameter("form1_maxWidth");
		String freePort = request.getParameter("form1_freePort");
		String eparchyCode = request.getParameter("form1_eparchyCode");
		String houseCode = request.getParameter("form1_houseCode");
		String addressName = request.getParameter("form1_addressName");
		String coverType = request.getParameter("form1_coverType");
		String chooseCat = request.getParameter("form1_chooseCat");
		//MODEM方式 0:前台配发;3:客户自备;4:自动配发
    	String modemSaleType = StringUtils.isEmpty(chooseCat)?"3":"4";  
		String phoneName = request.getParameter("phoneName");
		String installName = request.getParameter("installName");
		String idCard = request.getParameter("idCard");
		String employeNo = request.getParameter("employeNo");
		String channelRecoNo = request.getParameter("channelRecoNo");
		String installTime = request.getParameter("radio_installTime");
		
		/*宽带商品信息*/
    	Map<String,Object> map =Maps.newHashMap();
    	map.put("goodsSkuId", skuId);
    	map.put("containGoodsSkuIdInfo",true);
        map.put("containGoodsBusiParam",true);
		List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
		Map<String,Object> resMap = Maps.newHashMap();
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
		String goodsFormat = "单宽带新装:"+broadbandItem.getBandWidth()+"M/"+broadbandItem.getTerm();

		/*订单明细业务参数信息*/
		List<TfOrderSubDetailBusiParam> busiParamList = Lists.newArrayList();
		TfOrderSubDetailBusiParam param1 = new TfOrderSubDetailBusiParam();
		param1.setSkuBusiParamName("PSPT_TYPE_CODE");
		param1.setSkuBusiParamValue("0");			//0:身份证
		param1.setSkuBusiParamDesc("证件类型");
		TfOrderSubDetailBusiParam param2 = new TfOrderSubDetailBusiParam();
		param2.setSkuBusiParamName("PSPT_ID");
		param2.setSkuBusiParamValue(idCard);
		param2.setSkuBusiParamDesc("证件号码");
		TfOrderSubDetailBusiParam param3 = new TfOrderSubDetailBusiParam();
		param3.setSkuBusiParamName("CUST_NAME");
		param3.setSkuBusiParamValue(installName);
		param3.setSkuBusiParamDesc("客户名称");
		TfOrderSubDetailBusiParam param4 = new TfOrderSubDetailBusiParam();
		param4.setSkuBusiParamName("PSPT_ADDR");
		param4.setSkuBusiParamValue(addressName);
		param4.setSkuBusiParamDesc("证件地址");
		TfOrderSubDetailBusiParam param5 = new TfOrderSubDetailBusiParam();
		param5.setSkuBusiParamName("CONTACT");
		param5.setSkuBusiParamValue(installName);
		param5.setSkuBusiParamDesc("联系人");
		TfOrderSubDetailBusiParam param6 = new TfOrderSubDetailBusiParam();
		param6.setSkuBusiParamName("CONTACT_PHONE");
		param6.setSkuBusiParamValue(phoneName);
		param6.setSkuBusiParamDesc("联系人电话");
		TfOrderSubDetailBusiParam param7 = new TfOrderSubDetailBusiParam();
		param7.setSkuBusiParamName("ADDR_ID");
		param7.setSkuBusiParamValue(houseCode);
		param7.setSkuBusiParamDesc("标准地址CODE");
		TfOrderSubDetailBusiParam param8 = new TfOrderSubDetailBusiParam();
		param8.setSkuBusiParamName("ADDR_NAME");
		param8.setSkuBusiParamValue(addressName);
		param8.setSkuBusiParamDesc("标准地址");
		TfOrderSubDetailBusiParam param9 = new TfOrderSubDetailBusiParam();
		param9.setSkuBusiParamName("ADDR_DESC");
		param9.setSkuBusiParamValue(addressName);
		param9.setSkuBusiParamDesc("装机详细地址");
		TfOrderSubDetailBusiParam param10 = new TfOrderSubDetailBusiParam();
		param10.setSkuBusiParamName("ACCESS_TYPE");
		param10.setSkuBusiParamValue(coverType);
		param10.setSkuBusiParamDesc("接入方式");
		TfOrderSubDetailBusiParam param11 = new TfOrderSubDetailBusiParam();
		param11.setSkuBusiParamName("MAX_WIDTH");
		param11.setSkuBusiParamValue(maxWidth);
		param11.setSkuBusiParamDesc("最大带宽");
		TfOrderSubDetailBusiParam param12 = new TfOrderSubDetailBusiParam();
		param12.setSkuBusiParamName("FREE_PORT_NUM");
		param12.setSkuBusiParamValue(freePort);
		param12.setSkuBusiParamDesc("空闲端口数");
		TfOrderSubDetailBusiParam param13 = new TfOrderSubDetailBusiParam();
		param13.setSkuBusiParamName("MODEM_SALE_TYPE");
		param13.setSkuBusiParamValue(modemSaleType);
		param13.setSkuBusiParamDesc("MODEM方式");
		TfOrderSubDetailBusiParam param14 = new TfOrderSubDetailBusiParam();
		param14.setSkuBusiParamName("RES_NO");
		param14.setSkuBusiParamValue("-1");
		param14.setSkuBusiParamDesc("光猫串码");
		TfOrderSubDetailBusiParam param15 = new TfOrderSubDetailBusiParam();
		param15.setSkuBusiParamName("IS_CHECKED");
		param15.setSkuBusiParamValue("0");				//默认绑定
		param15.setSkuBusiParamDesc("绑定手机号标志");
		TfOrderSubDetailBusiParam param16 = new TfOrderSubDetailBusiParam();
		param16.setSkuBusiParamName("BIND_SERIAL_NUMBER");
		param16.setSkuBusiParamValue(phoneName);
		param16.setSkuBusiParamDesc("绑定手机号码");
		// 地市编码,用于订单中心支付
		TfOrderSubDetailBusiParam param17 = new TfOrderSubDetailBusiParam();
		param17.setSkuBusiParamName("eparchyCode");
		param17.setSkuBusiParamValue(eparchyCode);
		param17.setSkuBusiParamDesc("地市编码");
		//FEE_LIST
		TfOrderSubDetailBusiParam param18 = new TfOrderSubDetailBusiParam();
		param18.setSkuBusiParamName("FEE");
		param18.setSkuBusiParamValue(goodsSkuPrice+",0");
		param18.setSkuBusiParamDesc("应缴");
		TfOrderSubDetailBusiParam param19 = new TfOrderSubDetailBusiParam();
		param19.setSkuBusiParamName("FEE_TYPE_CODE");
		param19.setSkuBusiParamValue("1122,410");
		param19.setSkuBusiParamDesc("FEE_TYPE_CODE");
		TfOrderSubDetailBusiParam param20 = new TfOrderSubDetailBusiParam();
		param20.setSkuBusiParamName("FEE_MODE");
		param20.setSkuBusiParamValue("2,2");
		param20.setSkuBusiParamDesc("FEE_MODE");
		TfOrderSubDetailBusiParam param21 = new TfOrderSubDetailBusiParam();
		param21.setSkuBusiParamName("PAY");
		param21.setSkuBusiParamValue(goodsSkuPrice+",0");
		param21.setSkuBusiParamDesc("PAY");
		TfOrderSubDetailBusiParam param22 = new TfOrderSubDetailBusiParam();
		param22.setSkuBusiParamName("TRADE_TYPE_CODE");
		param22.setSkuBusiParamValue("1002,1002");
		param22.setSkuBusiParamDesc("TRADE_TYPE_CODE");
		//产品编码、包编码、元素编码
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
		busiParamList.add(param1);
		busiParamList.add(param2);
		busiParamList.add(param3);
		busiParamList.add(param4);
		busiParamList.add(param5);
		busiParamList.add(param6);
		busiParamList.add(param7);
		busiParamList.add(param8);
		busiParamList.add(param9);
		busiParamList.add(param10);
		busiParamList.add(param11);
		busiParamList.add(param12);
		busiParamList.add(param13);
		busiParamList.add(param14);
		busiParamList.add(param15);
		busiParamList.add(param16);
		busiParamList.add(param17);
		busiParamList.add(param18);
		busiParamList.add(param19);
		busiParamList.add(param20);
		busiParamList.add(param21);
		busiParamList.add(param22);

		//----增加受理中校验接口开始。。。
		Map<String,String> preMap = new HashMap<>();
		TfOrderSubDetailBusiParam tempParam = null;
		for(int i  = 0; i < busiParamList.size(); i ++){
			tempParam = busiParamList.get(i);
			preMap.put(tempParam.getSkuBusiParamName(),tempParam.getSkuBusiParamValue());
		}

		//增加受理中校验参数
		preMap.put("PRE_TYPE","1");
		Map result = broadBandServiceImpl.broadbandCreateByMap(preMap,new BroadbandCreateCondition());
		logger.info("宽带新装受理中校验接口返回： " + result);

		if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
			throw new Exception("受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
		}
		//----增加受理中校验接口结束。。。

	    TfOrderSub orderSub = new TfOrderSub();
	    /*订单会员关联信息*/
	    MemberVo memberVo = UserUtils.getLoginUser(request);
		TfOrderUserRef orderUserRef = new TfOrderUserRef(); 
		orderUserRef.setMemberId(memberVo.getMemberLogin().getMemberId());
		orderUserRef.setMemberLogingName(memberVo.getMemberLogin().getMemberLogingName());
		orderUserRef.setEparchyCode(eparchyCode);
		orderUserRef.setCounty(request.getParameter("form1_county"));
        orderSub.setOrderUserRef(orderUserRef);
        /*订单明细信息*/
        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        orderSubDetail.setGoodsId(broadbandItem.getGoodsId());
        orderSubDetail.setGoodsSkuId(broadbandItem.getGoodsSkuId());
        orderSubDetail.setGoodsName(goodsInfoList.get(0).getGoodsName());
        orderSubDetail.setGoodsSkuPrice(goodsSkuPrice);  
        orderSubDetail.setGoodsSkuNum(1L);
        orderSubDetail.setRootCateId(OrderConstant.CATE_BROADBAND);
        orderSubDetail.setGoodsFormat(goodsFormat);
        orderSubDetail.setShopId(BroadbandConstants.SHOP_ID);
        orderSubDetail.setShopName(BroadbandConstants.SHOP_NAME);
        orderSubDetail.setShopTypeId(6);

	    orderSubDetail.setOrderSubDetailBusiParams(busiParamList);
		orderSub.addOrderDetail(orderSubDetail);
		/*订单信息*/
		orderSub.setShopId(BroadbandConstants.SHOP_ID);		 
		orderSub.setShopName(BroadbandConstants.SHOP_NAME); 
		orderSub.setOrderTypeId(OrderConstant.TYPE_BROADBAND);
		orderSub.setOrderSubAmount(goodsSkuPrice);	//子订单总额	
		orderSub.setOrderSubPayAmount(goodsSkuPrice);//支付金额
		orderSub.setOrderSubDiscountAmount(0L);//优惠金额
		//orderSub.setOrderSubPayStatus("Y");//支付状态：已支付
		//orderSub.setOrderStatusId(12);//订单状态：已归档，完成
		orderSub.setOrderChannelCode("E007");//渠道编码，网上商城
		orderSub.setPayModeId(2);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
		orderSub.setDeliveryModeId(3);//配送方式，虚拟商品
		orderSub.setIsInvoicing(0);//是否开发票:0-不开
		logger.info("单宽带新装,提交订单参数orderSub:"+JSONArray.toJSONString(orderSub));
		List<TfOrderSub> orderSubList= orderService.newOrder(orderSub);
	    resMap.put("code", "0");
		resMap.put("message", "生成订单成功");
		resMap.put("orderSub", orderSubList.get(0));
		
		return resMap;
		
	}
	
	
	 @RequestMapping("payOrder")
	 public void payOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		try{
			String  orderSubNo = request.getParameter("payForm_orderSubNo");
	    	String payPlatform = request.getParameter("payForm_payPlatform");
	    	String eparchyCode = request.getParameter("payForm_eparchyCode"); 
	     	Map<String, Object> resultMap = orderService.mergeOrder(orderSubNo);
	    	TfOrder order = (TfOrder)resultMap.get("order");
			 
	     	/*支付方式*/
	        /*Short payTypeId = CommonParams.PAY_PLATFORM.get(payPlatform);  //账户类型Id 2:和包支付,3:支付宝支付
	        if(payTypeId == null){
	        	payTypeId = 2;		 
	        }*/
	        /*分润规则  */
	        CompanyInfo companyInfo = new CompanyInfo();
	        short  payPlatForm = BroadbandConstants.PAY_PLATFORM.get(payPlatform);	//支付平台与支付机构代码对应关系
	        companyInfo.setChannelCityCode(eparchyCode);
	        companyInfo.setCompanyTypeId((short)7);
	        CompanyAcctInfo eparchyAcctInfo = companyAcctService.getAcctByChannelAndType(companyInfo,payPlatForm);

			String shRule = "";
			if(eparchyAcctInfo != null) {
				shRule = eparchyAcctInfo.getAccountNum()+"^"+order.getOrderAmount()+"^单宽带新装";
			}

	        logger.info("单宽带新装,分润规则:"+companyInfo);
	        
	        //同步页面返回地址
	        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	        String callbackUrl = basePath+"/broadband/toPayResult";
	        // 异步页面回调地址
	        String notifyCallbackUrl = afterOrderPayUrl+"/broadband/payAfterNotify";
	        
	        TfOrderPay orderPay = new TfOrderPay();
	        orderPay.setOrderId(order.getOrderId());
	        orderPay.setOrderPayAmount(order.getOrderAmount());
	        orderPay.setOrgCode(payPlatform);
	        orderPay.setShRule(shRule);
	        orderPay.setHmac(BroadbandConstants.SIGN_KEY);
	        orderPay.setOrderHarvestExpend("0");
	        orderPay.setMerchantId(BroadbandConstants.MERCHANT_ID);
	        String content  = orderService.applyPay(orderPay, callbackUrl, notifyCallbackUrl);
	        String encode = PAY_CHARSET.get(payPlatform);
	        if(encode == null){
	            encode = "GBK";
	        }
	        response.setContentType("text/html;charset=" + encode);
	        PrintWriter out = response.getWriter();
	        out.print(content);
	        out.flush();
	        out.close();
		}
		catch(Exception e){
			logger.error("单宽带新装异常:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		 
	 }
	 
	 
	 	/**
	     * 跳转到订单支付结果页面，供支付中心调用（同步）
	     * @param model
	     * @param returnCode
	     * @param chargeflowId
	     * @return
	     */
	    @RequestMapping("toPayResult")
	    public String toPayResult(Model model,String returnCode ,Long chargeflowId){
	    	logger.info("单宽带新装,支付同步回调参数:returnCode="+returnCode+",chargeflowId="+chargeflowId);
	    	try{
	    		TfOrderSub orderSub = new TfOrderSub();
			    orderSub.setOrderId(chargeflowId);
			    List<TfOrderSub> orderSubList = orderQueryService.queryBaseOrderList(orderSub);
			    if(orderSubList!=null && orderSubList.size()>0){
			    	model.addAttribute("orderSub",orderSubList.get(0));
			    }
			    model.addAttribute("returnCode",returnCode);
	    	}
	    	catch(Exception e){
	    		logger.error("单宽带新装,支付同步回调失败，异常信息:"+e);
	    		e.printStackTrace();
	    	}
	        return "web/goods/broadband/newInstall/payResult";
	    }
	    
	    /**
		 * 跳转到订单支付结果页面，供支付中心调用（异步）
		 * 
		 * @param model
		 * @param returnCode
		 * @param chargeflowId
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/payAfterNotify")
		public void payAfterNotify(HttpServletRequest request,HttpServletResponse response,String merchantId ,String returnCode,String message,String type,
	            String version,Integer amount,Long orderId ,String payDate,
	            String status,String orderDate,String payNo,String org_code,
	            String organization_payNo,String hmac,String shRule,String accountDate) throws Exception {
			
			try{
				 logger.info("单宽带新装,支付异步回调参数:"+request.getParameterMap());
				 MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
		         Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
		         if(!validataService.valHmac(payParamMap,merChantBean)){
		            throw new Exception("单宽带新装:签名验证未通过");
		         }
		         
		         TfOrderSub orderSubParam = new TfOrderSub();
		         orderSubParam.setOrderId((long)orderId);
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
	             orderPay.setOrderId((long)orderId);
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
			catch(Exception e){
				 logger.error("单宽带新装,支付异步回调失败，异常信息:"+e);
	             BusiLogUtils.writerLogging(request,"payAfterNotify","","","","","","","",e,"2","");
	             response.getWriter().print("fail");
			}
			
		}
	
}
