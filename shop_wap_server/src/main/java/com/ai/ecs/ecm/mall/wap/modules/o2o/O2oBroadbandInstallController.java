package com.ai.ecs.ecm.mall.wap.modules.o2o;

import static com.ai.ecs.ecm.mall.wap.common.CommonParams.PAY_CHARSET;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.BusiLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
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
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.merchant.entity.CompanyInfo;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
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

@Controller
@RequestMapping("o2obroadbandInstall")
public class O2oBroadbandInstallController extends BaseController{
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
	
	@Autowired
    private O2oOrderTempService o2oOrderTempService;
	
	@Autowired
    private O2oOrderParamTemService o2oOrderParamTempService;
	 
	 @Value("${afterOrderPayUrl}")
	 String afterOrderPayUrl;
	
	 private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
	            + "wxyz0123456789+/" + "=";
	 
	 /**
	 * 号码填写
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("init")
	public String init(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");

		return "web/broadband/o2o/newInstall/init";
	}
	 
	
	@RequestMapping("broadbandInstall")
	public String broadbandInstall(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		
		// 获取登录用户手机号码
		String installPhoneNum = request.getParameter("installPhoneNum");
		model.addAttribute("installPhoneNum", installPhoneNum);
		
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
		return "web/broadband/o2o/newInstall/newInstall";
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
			String installPhoneNum = request.getParameter("installPhoneNum");
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
			 model.addAttribute("installPhoneNum",installPhoneNum);
		}
		catch(Exception e){
			logger.error("单宽带新装异常:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return "web/broadband/o2o/newInstall/confirmInstallInfo";
	}

	@ResponseBody
	@RequestMapping("submitInstallOrder")
	public Map<String,Object> submitInstallOrder(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception{
		Map<String,Object> resMap = new HashMap<String,Object>();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		resMap.put("code", "-1");
		resMap.put("message", "您的信息提交失败!");
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
		
		String staffPwd = request.getParameter("staffPwd");
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
        logger.info("获取渠道部门密码：" + staffPwd);
		
		/*宽带商品信息*/
    	Map<String,Object> map =Maps.newHashMap();
    	map.put("goodsSkuId", skuId);
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
		String goodsFormat = "单宽带新装:"+broadbandItem.getBandWidth()+"M/"+broadbandItem.getTerm();

		/*业务参数*/
        O2oParamUtils o2oParamUtils = new O2oParamUtils();
        o2oParamUtils.addParams("TRADE_DEPART_PASSWD", staffPwd, "渠道交易部门密码");
        o2oParamUtils.addParams("SERIAL_NUMBER", phoneName, "手机号码");
        o2oParamUtils.addParams("PSPT_TYPE_CODE", "0", "证件类型");
        o2oParamUtils.addParams("PSPT_ID",idCard,"证件号码");
        o2oParamUtils.addParams("CUST_NAME",installName,"客户名称");
        o2oParamUtils.addParams("PSPT_ADDR",addressName,"证件地址");
        o2oParamUtils.addParams("CONTACT", installName, "联系人");
        o2oParamUtils.addParams("CONTACT_PHONE", phoneName, "联系人电话");
        o2oParamUtils.addParams("ADDR_ID", houseCode, "标准地址编码");
        o2oParamUtils.addParams("ADDR_NAME", addressName, "标准地址");
        o2oParamUtils.addParams("ADDR_DESC", addressName, "装机详细地址");
        o2oParamUtils.addParams("ACCESS_TYPE", coverType, "接入方式");
        o2oParamUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
        o2oParamUtils.addParams("FREE_PORT_NUM", freePort, "空闲端口数");
        o2oParamUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
        o2oParamUtils.addParams("RES_NO", "-1", "光猫串码");
        o2oParamUtils.addParams("IS_CHECKED", "0", "绑定手机号标志");
        o2oParamUtils.addParams("BIND_SERIAL_NUMBER", phoneName, "绑定手机号码");
        o2oParamUtils.addParams("eparchyCode", eparchyCode, "地市编码");
        o2oParamUtils.addParams("FEE", goodsSkuPrice+",0", "应缴");
        o2oParamUtils.addParams("FEE_TYPE_CODE", "1122,410", "费用类型编码");
        o2oParamUtils.addParams("FEE_MODE", "2,2", "费用模式");
        o2oParamUtils.addParams("PAY", goodsSkuPrice+",0", "实缴");
        o2oParamUtils.addParams("TRADE_TYPE_CODE", "1002,1002", "交易类型");
        o2oParamUtils.addParams("CHANNEL_CODE", "E050", "手机号码");
		
		/*订单明细业务参数信息*/
        List<O2oOrderParamTemp> busiParamList = putChannelParams(o2oParamUtils.getParamsList(),memberVo.getChannelInfo(),eparchyCode);
		//FEE_LIST
		//产品编码、包编码、元素编码
		TfGoodsSku tfGoodsSku = tfGoodsInfo.getTfGoodsSkuList().get(0);
		TfOrderSubDetailBusiParam param = new TfOrderSubDetailBusiParam();
		List<TfGoodsBusiParam> paramList = tfGoodsSku.getTfGoodsBusiParamList();
		for(TfGoodsBusiParam tgbp : paramList){
			o2oParamUtils.addParams(tgbp.getSkuBusiParamName(), tgbp.getSkuBusiParamValue(), tgbp.getSkuBusiParamDesc());
		}
		
		//----增加受理中校验接口开始。。。
		Map<String,String> preMap = new HashMap<>();
		O2oOrderParamTemp tempParam = null;
		for(int i  = 0; i < busiParamList.size(); i ++){
			tempParam = busiParamList.get(i);
			preMap.put(tempParam.getParamName(),tempParam.getParamValue());
		}

		//增加受理中校验参数
		preMap.put("PRE_TYPE","1");
		Map result = broadBandServiceImpl.broadbandCreateByMap(preMap,new BroadbandCreateCondition());
		logger.info("宽带新装受理中校验接口返回： " + result);

		if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
			resMap.put("code", "-1");
			resMap.put("message", "受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
        	return resMap;
		}
		//----增加受理中校验接口结束。。。
		O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
        orderTempInfo.setOrder_type_id(Long.valueOf(OrderConstant.TYPE_BROADBAND));
        orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
        orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
        orderTempInfo.setContact_phone(phoneName);
        orderTempInfo.setEparchy_code(eparchyCode);
//        orderTempInfo.setCounty(request.getParameter("install_county"));
        orderTempInfo.setGoods_id(broadbandItem.getGoodsId());
        orderTempInfo.setGoods_sku_id(broadbandItem.getGoodsSkuId());
        orderTempInfo.setGoods_name(tfGoodsInfo.getGoodsName());
        orderTempInfo.setGoods_sku_price(goodsSkuPrice);
        orderTempInfo.setShop_id(Long.valueOf(memberVo.getShopInfo().getShopId()));
        orderTempInfo.setShop_name(memberVo.getShopInfo().getShopName());
        orderTempInfo.setChannel_code("E050");
        orderTempInfo.setPay_mode_id(2);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
        orderTempInfo.setInsert_time(getCurrentDate());
        orderTempInfo.setGoods_pay_price(goodsSkuPrice);
        orderTempInfo.setGoods_format(goodsFormat);
        orderTempInfo.setRoot_cate_id(OrderConstant.CATE_BROADBAND);
        orderTempInfo.setShop_type_id(6L);
        Long orderTempId = o2oOrderTempService.insert(orderTempInfo);
        
//        o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneNum);
        
        for(O2oOrderParamTemp paramTemp:busiParamList){
            paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
        }
        o2oOrderParamTempService.batchInsert(busiParamList);
        resMap.put("code", "0");
        resMap.put("orderId", orderTempId);
        resMap.put("broadbandType", OrderConstant.TYPE_BROADBAND);
        resMap.put("serialNumber", phoneName);
        resMap.put("goodsName", tfGoodsInfo.getGoodsName());
		return resMap;
		
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
