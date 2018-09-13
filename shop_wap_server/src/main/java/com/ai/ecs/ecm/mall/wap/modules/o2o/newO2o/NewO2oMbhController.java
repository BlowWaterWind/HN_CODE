package com.ai.ecs.ecm.mall.wap.modules.o2o.newO2o;

import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.BusiLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfo;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoResult;
import com.ai.ecs.ecsite.modules.broadBand.entity.HeFamilyCheckCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
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
import com.ai.ecs.order.entity.*;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.service.IPayBankService;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * 魔百和在线办理
 * 
 * @author ncZang
 */

@Controller
@RequestMapping("newO2oMbh")
public class NewO2oMbhController {

	private Logger logger = Logger.getLogger(NewO2oMbhController.class);

	@Autowired
	private BroadBandService broadBandServiceImpl;

	@Autowired
	private DictService dictService;

	@Autowired
	private IGoodsManageService goodsManageService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IPayBankService payBankService;

	@Autowired
	private IMemberAddressService memberAddressService;

	@Autowired
	ICompanyAcctService companyAcctService;

	@Autowired
	private PhoneAttributionService phoneAttributionService;

	@Autowired
	IOrderQueryService orderQueryService;

	@Autowired
	private IUppHtmlValidataService validataService;

	@Autowired
	private HeFamilyService heFamilyService;

	@Value("${afterOrderPayUrl}")
	String afterOrderPayUrl;

	@Autowired
	private O2oOrderTempService o2oOrderTempService;

	@Autowired
	private O2oOrderParamTemService o2oOrderParamTempService;

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
	@RequestMapping("newInit")
	public String init(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");

		//获取渠道人员手机号码
        MemberVo memberVo=UserUtils.getLoginUser(request);
		model.addAttribute("installPhoneNum", memberVo.getMemberLogin().getMemberPhone());

		return "web/broadband/newO2o/mbh/init";
	}

	private List<BroadbandItemEntity> getMbhGoodsList() throws Exception {
		List<TfGoodsInfo> goodsList = getGoodsList();
		List<BroadbandItemEntity> mbhItemList = BroadbandUtils.convertMbhItemEntityList(goodsList);
		logger.info("封装商品数据：" + mbhItemList);
		for (int i = 0; i < mbhItemList.size() - 1; i++) {
			for (int j = mbhItemList.size() - 1; j > i; j--) {
				if (mbhItemList.get(j).getHeProductId().equals(mbhItemList.get(i).getHeProductId())) {
					mbhItemList.remove(j);
				}
			}
		}
		for (int k = 0; k < mbhItemList.size(); k++) {
			if (mbhItemList.get(k).getBroadbandItemName().contains("年包")) {
				double priceDouble = mbhItemList.get(k).getPrice();
				mbhItemList.get(k)
						.setBroadbandItemName(String.valueOf((int) priceDouble) + "元/" + mbhItemList.get(k).getTerm());
			} else if (mbhItemList.get(k).getBroadbandItemName().contains("月包")) {
				mbhItemList.get(k).setBroadbandItemName(mbhItemList.get(k).getHeProductId() + "元/月");
			}
		}
		Collections.reverse(mbhItemList);
		return mbhItemList;
	}

	/**
	 * 跳转到办理页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("newIndex")
	public String toMbhIndexPage(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");

		// 获取登录用户手机号码
		String phoneId = request.getParameter("installPhoneNum");
		model.addAttribute("phoneId", phoneId);
		model.addAttribute("mbhItemList",getMbhGoodsList());

		return "web/broadband/newO2o/mbh/index";
	}

	/**
	 * 跳转到办理页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("newDetail")
	public String toMbhDetailPage(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "toMbhDetailPage",request.getParameterMap(),"o2o魔百盒业务下单",request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");
		String phoneId = request.getParameter("phoneId");
		model.addAttribute("phoneId", phoneId);
		model.addAttribute("mbhItemList",getMbhGoodsList());
		// 校验魔百和和宽带安装情况
		Map<String,String> checkResMap = isMbhAndBroadBand(phoneId);
		model.addAttribute("checkResCode",checkResMap.get("checkResCode"));
		String isBroadBand = checkResMap.get("isBroadBand");
		String isMbh = checkResMap.get("isMbh");
		model.addAttribute("isBroadBand", isBroadBand);
		model.addAttribute("isMbh", isMbh);

		if (isMbh.equals("0")) {
			// 已经办理魔百和
			model.addAttribute("errorInfo", "您已经办理了魔百和，不能再次办理！");
			return "web/broadband/newO2o/mbh/error";
		}
		
		if(phoneId != null || !"".equals(phoneId)){
			 BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
			 condition.setSerialNumber(phoneId);
			 BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandServiceImpl.broadbandDetailInfo(condition);
			 if("0".equals(broadbandDetailInfoResult.getResultCode())  && "成功".equals(broadbandDetailInfoResult.getResultInfo())){
				 BroadbandDetailInfo broadbandDetailInfo =
				 broadbandDetailInfoResult.getBroadbandDetailInfo();
				 logger.info(broadbandDetailInfo);
				 model.addAttribute("broadbandDetailInfo",broadbandDetailInfo);
			 }else{
				 //无宽带进入推荐页面
				 model.addAttribute("errorInfo","尊敬的用户，您当前账号未办理宽带业务，请先办理宽带业务！");
				 return "web/broadband/newO2o/mbh/error";
			 }
		}
		List<String> bookInstallDateList = new ArrayList<>();
		bookInstallDateList.add(DateUtils.getDateAfterDays(1));// 未来1天
		bookInstallDateList.add(DateUtils.getDateAfterDays(2));// 未来2天
		bookInstallDateList.add(DateUtils.getDateAfterDays(3));// 未来3天
		model.addAttribute("bookInstallDateList", bookInstallDateList);
        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "toMbhDetailPage",null,objectToMap(bookInstallDateList),"o2o魔百盒业务下单");
		return "web/broadband/newO2o/mbh/detail";
	}

	/**
	 * 判断号码是否办理宽带和魔百和
	 * 
	 * @return resMap
	 * @return resultCode 返回编码
	 * @return isBroadBand 是否办理宽带
	 * @return isMbh 是否办理魔百和
	 * @throws Exception
	 */
	private Map<String, String> isMbhAndBroadBand(String installPhoneNum) throws Exception {
		Map<String, String> resMap = new HashMap<String, String>();
		HeFamilyCheckCondition infoCondition = new HeFamilyCheckCondition();
		infoCondition.setSerial_number(installPhoneNum);
		Map<String, Object> resultMap = heFamilyService.heFamilyCheck(infoCondition);
		JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
		JSONObject result = (JSONObject) resultArray.get(0);
		logger.error("判断号码是否办理宽带和魔百和接口返回串：" + result);
		resMap.put("checkResCode", (String) result.get("X_RESULTCODE"));
		resMap.put("checkResInfo", (String) result.get("X_RESULTINFO"));
		resMap.put("isBroadBand", (String) result.get("BROADBAND_FLAG"));
		resMap.put("isMbh", (String) result.get("INTERACTIVE_FLAG"));
		return resMap;
	}

	@RequestMapping("newGetTvProduct")
	@ResponseBody
	public Map<String, Object> getTvProduct(HttpServletRequest request, HttpServletResponse response,
			String chooseGiftElementId) throws Exception {
		Map<String, Object> resMap = new HashMap<String, Object>();

		List<BroadbandItemEntity> chooseMbhItemList = BroadbandUtils.convertMbhItemEntityList(getGoodsList());
		logger.info("获取可选封装宽带产品列表" + chooseMbhItemList);
		List<BroadbandItemEntity> abledChooseMbhItemList = new ArrayList<BroadbandItemEntity>();// 可选电视产品
		for (int i = 0; i < chooseMbhItemList.size(); i++) {
			if (chooseGiftElementId.equals(chooseMbhItemList.get(i).getHeProductId())) {
				if (chooseMbhItemList.get(i).getBroadbandItemName().contains("芒果TV")) {
					chooseMbhItemList.get(i).setBroadbandItemName("芒果TV");
				} else if (chooseMbhItemList.get(i).getBroadbandItemName().contains("未来电视")) {
					chooseMbhItemList.get(i).setBroadbandItemName("未来电视");
				}
				abledChooseMbhItemList.add(chooseMbhItemList.get(i));
			}
		}
		resMap.put("abledChooseMbhItemList", abledChooseMbhItemList);
		return resMap;
	}

	/**
	 * 获取登录用户手机号码
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> getLoginUserNum(HttpServletRequest request) {
		Map<String, String> resMap = new HashMap<String, String>();
		MemberVo memberVo = UserUtils.getLoginUser(request);
		String installPhoneNum = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
		String installRealName = String.valueOf(memberVo.getMemberLogin().getMemberLogingName());
		resMap.put("installPhoneNum", installPhoneNum);
		resMap.put("installRealName", installRealName);
		return resMap;
	}

	/**
	 * 获取魔百和商品列表
	 * 
	 * @param skuId
	 * @return
	 * @throws Exception
	 */
	private List<TfGoodsInfo> getGoodsList() throws Exception {
		Map<String, Object> bbArgs = new HashMap<String, Object>();
		bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID", "O2O_MBH_BROADBAND_CATEGORY_ID"));
		List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
		logger.info("获取魔百和商品列表：" + consupostnGoodsInfoList);
		return consupostnGoodsInfoList;
	}

	/**
	 * 订单确认
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("newConfirm")
	public String toMbhConfirmPage(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		String streamNo=createStreamNo();
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
				"toMbhConfirmPage",request.getParameterMap(),"魔百盒业务下单",request);
		model.addAttribute("phoneId", request.getParameter("phoneId"));
		model.addAttribute("price", Double.valueOf(request.getParameter("price")).intValue());
		model.addAttribute("goodsSkuId", request.getParameter("goodsSkuId"));
		model.addAttribute("tvProductId", request.getParameter("tvProductId"));
		model.addAttribute("reqPackageId", request.getParameter("reqPackageId"));
		model.addAttribute("reqElementId", request.getParameter("reqElementId"));
		model.addAttribute("giftElementId", request.getParameter("giftElementId"));
		model.addAttribute("accessAcct", request.getParameter("accessAcct"));
		model.addAttribute("custName", request.getParameter("custName"));
		model.addAttribute("psptId", request.getParameter("psptId"));
		model.addAttribute("addrName", request.getParameter("addrName"));
		model.addAttribute("startTime", request.getParameter("startTime"));
		model.addAttribute("endTime", request.getParameter("endTime"));
		model.addAttribute("discntName", request.getParameter("discntName"));// 套餐名称
		model.addAttribute("form1_Mbh", request.getParameter("form1_Mbh"));// 电视牌照方
		model.addAttribute("bookInstallDate", request.getParameter("bookInstallDate"));
		model.addAttribute("bookInstallTime", request.getParameter("bookInstallTime"));
		model.addAttribute("term", request.getParameter("term"));
		model.addAttribute("payMode", request.getParameter("payMode"));
		model.addAttribute("payModeName", request.getParameter("payModeName"));
		writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
				"toMbhConfirmPage",null,null,"魔百盒业务下单");
		return "web/broadband/newO2o/mbh/confirm";
	}

	/**
	 * 订单提交
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequestMapping("submit")
//	@ResponseBody
//	public Map<String, Object> submitOrder(HttpServletRequest request, HttpServletResponse response, Model model)
//			throws Exception {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		resultMap.put("respCode", "-1");
//		resultMap.put("respDesc", "您的信息提交失败!");
//
//		String phoneId = request.getParameter("phoneId");
//		String accessAcct = request.getParameter("accessAcct");
//		String tvProductId = request.getParameter("tvProductId");
//		String reqPackageId = request.getParameter("reqPackageId");
//		String reqElementId = request.getParameter("reqElementId");
//		String giftElementId = request.getParameter("giftElementId");
//		String sibSaleType = "1";
//		String goodsSkuId = request.getParameter("goodsSkuId");
//		String eparchyCode = this.getEparchyCode(phoneId);
//		/* 订单明细信息 */
//		List<BroadbandItemEntity> mbhItemList = BroadbandUtils.convertMbhItemEntityList(getGoodsList());
//		BroadbandItemEntity mbhTfGoodsInfo = new BroadbandItemEntity();
//		
//		for (int i = 0; i < mbhItemList.size(); i++) {
//			String mbhGoodsSkuId = String.valueOf(mbhItemList.get(i).getGoodsSkuId());
//			if (mbhGoodsSkuId.equals(goodsSkuId)) {
//				mbhTfGoodsInfo = mbhItemList.get(i);
//				break;
//			}
//		}
//		long orderSubAmount = mbhTfGoodsInfo.getPrice().longValue() * 100;
//
//		/* 业务参数 */
//		O2oParamUtils o2oParamUtils = new O2oParamUtils();
//		o2oParamUtils.addParams("SERIAL_NUMBER", phoneId, "手机号码");
//		o2oParamUtils.addParams("ACCESS_ACCT", accessAcct, "宽带账号");
//		o2oParamUtils.addParams("TV_PRODUCT_ID", tvProductId, "TV产品ID");
//		o2oParamUtils.addParams("REQ_PACKAGE_ID", reqPackageId, "必选服务包ID");
//		o2oParamUtils.addParams("REQ_ELEMENT_ID", reqElementId, "必选服务ID");
//		if (!giftElementId.equals("20")) o2oParamUtils.addParams("GIFT_ELEMENT_ID", giftElementId, "魔百和年包元素ID");
//		o2oParamUtils.addParams("TRADE_TYPE_CODE", "3709", "订单类型");
//		o2oParamUtils.addParams("FEE_TYPE_CODE", "420", "费用类型");
//		o2oParamUtils.addParams("FEE_MODE", "0", "营业费");
//		o2oParamUtils.addParams("FEE", String.valueOf(orderSubAmount / 100), "应缴");
//		o2oParamUtils.addParams("PAY", String.valueOf(orderSubAmount / 100), "实缴");
//		o2oParamUtils.addParams("SIB_SALE_TYPE", sibSaleType, "机顶盒方式");
//
//		MemberVo memberVo = UserUtils.getLoginUser(request);
//		
//        //----增加受理中校验接口结束。。。
//        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
//        orderTempInfo.setOrder_type_id(Long.valueOf(OrderConstant.TYPE_MBH));
//        orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
//        orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
//        orderTempInfo.setContact_phone(phoneId);
//        orderTempInfo.setEparchy_code(eparchyCode);
//        orderTempInfo.setCounty(request.getParameter("install_county"));
//        orderTempInfo.setGoods_id(mbhTfGoodsInfo.getGoodsId());
//        orderTempInfo.setGoods_sku_id(mbhTfGoodsInfo.getGoodsSkuId());
//        orderTempInfo.setGoods_name(mbhTfGoodsInfo.getBroadbandItemName());
//        orderTempInfo.setGoods_sku_price(mbhTfGoodsInfo.getPrice().longValue());
//        orderTempInfo.setShop_id(Long.valueOf(memberVo.getShopInfo().getShopId()));
//        orderTempInfo.setShop_name(memberVo.getShopInfo().getShopName());
//        orderTempInfo.setChannel_code("E050");
//        orderTempInfo.setPay_mode_id(2);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
//        orderTempInfo.setInsert_time(getCurrentDate());
//        orderTempInfo.setGoods_pay_price(mbhTfGoodsInfo.getPrice().longValue());
//        orderTempInfo.setGoods_format(mbhTfGoodsInfo.getBroadbandItemName());
//        orderTempInfo.setRoot_cate_id(OrderConstant.CATE_BROADBAND);
//        orderTempInfo.setShop_type_id(6L);
//        Long orderTempId = o2oOrderTempService.insert(orderTempInfo);
//        
//        o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneId);
//        
//        for(O2oOrderParamTemp paramTemp:o2oParamUtils.getParamsList()){
//            paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
//        }
//        o2oOrderParamTempService.batchInsert(o2oParamUtils.getParamsList());
//        resultMap.put("code", "0");
//        resultMap.put("orderId", orderTempId);
//        resultMap.put("broadbandType", OrderConstant.TYPE_CONSUPOSTN);
//        resultMap.put("serialNumber", phoneId);
//        resultMap.put("goodsName", mbhTfGoodsInfo.getBroadbandItemName());
//
//		return resultMap;
//	}

	/**
	 * 订单提交
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("newNoPaySubmit")
	@ResponseBody
	public Map<String, Object> noPaySubmitOrder(HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("respCode", "-1");
		resultMap.put("respDesc", "您的信息提交失败!");

		try {
			String phoneId = request.getParameter("phoneId");
			String accessAcct = request.getParameter("accessAcct");
			String tvProductId = request.getParameter("tvProductId");
			String reqPackageId = request.getParameter("reqPackageId");
			String reqElementId = request.getParameter("reqElementId");
			String giftElementId = request.getParameter("giftElementId");
			String sibSaleType = "1";
			String goodsSkuId = request.getParameter("goodsSkuId");
			String eparchyCode = this.getEparchyCode(phoneId);
			String staffPwd = request.getParameter("staffPwd");
			String payMode = request.getParameter("payMode");
			payMode= StringUtils.isEmpty(payMode)?"0":payMode;
	        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
	        logger.info("获取渠道部门密码：" + staffPwd);
			MemberVo memberVo = UserUtils.getLoginUser(request);
			// 通过校验魔百和和宽带安装情况验证密码
			Map<String,String> checkResMap = isPwdCheck(phoneId,memberVo.getChannelInfo(),staffPwd);
			String checkResCode = checkResMap.get("checkResCode");
			if (!"0".equals(checkResCode)) {
				// 已经办理魔百和
				resultMap.put("respDesc", checkResMap.get("checkResInfo"));
				return resultMap;
			}
			/* 订单明细信息 */
			List<BroadbandItemEntity> mbhItemList = BroadbandUtils.convertMbhItemEntityList(getGoodsList());
			BroadbandItemEntity mbhTfGoodsInfo = new BroadbandItemEntity();
			for (int i = 0; i < mbhItemList.size(); i++) {
				String mbhGoodsSkuId = String.valueOf(mbhItemList.get(i).getGoodsSkuId());
				if (mbhGoodsSkuId.equals(goodsSkuId)) {
					mbhTfGoodsInfo = mbhItemList.get(i);
					break;
				}
			}
			long orderSubAmount = mbhTfGoodsInfo.getPrice().longValue() * 100;
			/* 业务参数 */
			O2oParamUtils o2oParamUtils = new O2oParamUtils();
			o2oParamUtils.addParams("SERIAL_NUMBER", phoneId, "手机号码");
			o2oParamUtils.addParams("ACCESS_ACCT", accessAcct, "宽带账号");
			o2oParamUtils.addParams("TV_PRODUCT_ID", tvProductId, "TV产品ID");
			o2oParamUtils.addParams("REQ_PACKAGE_ID", reqPackageId, "必选服务包ID");
			o2oParamUtils.addParams("REQ_ELEMENT_ID", reqElementId, "必选服务ID");
			if (!giftElementId.equals("20")) o2oParamUtils.addParams("GIFT_ELEMENT_ID", giftElementId, "魔百和年包元素ID");
			o2oParamUtils.addParams("TRADE_TYPE_CODE", "3709", "订单类型");
			o2oParamUtils.addParams("FEE_TYPE_CODE", "420", "费用类型");
			o2oParamUtils.addParams("FEE_MODE", "0", "营业费");
			o2oParamUtils.addParams("FEE", String.valueOf(orderSubAmount/100), "应缴");
			o2oParamUtils.addParams("PAY", String.valueOf(orderSubAmount/100), "实缴");
			o2oParamUtils.addParams("SIB_SALE_TYPE", sibSaleType, "机顶盒方式");
			o2oParamUtils.addParams("TRADE_DEPART_PASSWD", staffPwd, "渠道交易部门密码");
			/* 设置订单信息 */
			// ----增加受理中校验接口结束。。。
			O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
			orderTempInfo.setOrder_type_id(Long.valueOf(OrderConstant.TYPE_MBH));
			orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
			orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
			orderTempInfo.setContact_phone(phoneId);
			orderTempInfo.setEparchy_code(eparchyCode);
			orderTempInfo.setCounty(request.getParameter("install_county"));
			orderTempInfo.setGoods_id(mbhTfGoodsInfo.getGoodsId());
			orderTempInfo.setGoods_sku_id(mbhTfGoodsInfo.getGoodsSkuId());
			orderTempInfo.setGoods_name(mbhTfGoodsInfo.getBroadbandItemName());
			orderTempInfo.setGoods_sku_price(orderSubAmount);
			orderTempInfo.setShop_id(Long.valueOf(memberVo.getShopInfo().getShopId()));
			orderTempInfo.setShop_name(memberVo.getShopInfo().getShopName());
			orderTempInfo.setChannel_code("E050");
			orderTempInfo.setPay_mode_id(Integer.parseInt(payMode));// 支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
			orderTempInfo.setInsert_time(getCurrentDate());
			orderTempInfo.setGoods_pay_price(orderSubAmount);
			orderTempInfo.setGoods_format(mbhTfGoodsInfo.getBroadbandItemName());
			orderTempInfo.setRoot_cate_id(OrderConstant.CATE_BROADBAND);
			orderTempInfo.setShop_type_id(6L);

			if(Integer.valueOf(payMode).intValue()==OrderConstant.PAY_WAY_CONSUME_FIRST){
				List<Map<String,Object>> xTradeList = new ArrayList<Map<String,Object>>();
				Map<String,Object> xTradeMap = new HashMap<String,Object>();
				xTradeMap.put("PAY_MONEY_CODE", "7");
				xTradeMap.put("MONEY", orderSubAmount);
				xTradeList.add(xTradeMap);
				o2oParamUtils.addParams("X_TRADE_PAYMONEY", "7,"+orderSubAmount, "支付方式");
			}

			Long orderTempId = o2oOrderTempService.insert(orderTempInfo);

//			o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneId);
			
            List<O2oOrderParamTemp> o2oOrderParamTempList = putChannelParams(o2oParamUtils.getParamsList(),memberVo.getChannelInfo(),eparchyCode);

			for (O2oOrderParamTemp paramTemp : o2oOrderParamTempList) {
				paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
			}
			o2oOrderParamTempService.batchInsert(o2oOrderParamTempList);
			resultMap.put("code", "0");
			resultMap.put("orderId", orderTempId);
			resultMap.put("broadbandType", OrderConstant.TYPE_MBH);
			resultMap.put("serialNumber", phoneId);
			resultMap.put("goodsName", mbhTfGoodsInfo.getBroadbandItemName());
		} catch (Exception e) {
			logger.error("魔百和提交订单异常:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

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

	private Map<String, String> isPwdCheck(String installPhoneNum,ChannelInfo channelInfo,String tradeDepartPassword) throws Exception {
		Map<String, String> resMap = new HashMap<String, String>();
		HeFamilyCheckCondition infoCondition = new HeFamilyCheckCondition();
		infoCondition.setSerial_number(installPhoneNum);
		infoCondition.setEparchyCodeFCust(channelInfo.getTradeEparchyCode());
		infoCondition.setDepartId(channelInfo.getTradeDepartId());
		infoCondition.setCityCode(channelInfo.getTradeCityCode());
		infoCondition.setStaffId(channelInfo.getTradeStaffId());
		infoCondition.setInModeCode(channelInfo.getInModeCode());
		infoCondition.setChanId(channelInfo.getChanelId());
		infoCondition.setTradeDepartPassword(tradeDepartPassword);
		Map<String, Object> resultMap = heFamilyService.heFamilyCheck(infoCondition);
		JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
		JSONObject result = (JSONObject) resultArray.get(0);
		logger.error("判断号码是否办理宽带和魔百和接口返回串：" + result);
		resMap.put("checkResCode", (String) result.get("X_RESULTCODE"));
		resMap.put("checkResInfo", (String) result.get("X_RESULTINFO"));
		resMap.put("isBroadBand", (String) result.get("BROADBAND_FLAG"));
		resMap.put("isMbh", (String) result.get("INTERACTIVE_FLAG"));
		return resMap;
	}

	private Date getCurrentDate() {
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
	 * 获取手机号码的归属地市编码
	 * 
	 * @param installPhoneNum
	 * @return
	 * @throws Exception
	 */
	private String getEparchyCode(String installPhoneNum) throws Exception {
		PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
		phoneAttributionModel.setSerialNumber(installPhoneNum);
		Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
		return String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
	}

	/**
	 * 订单支付
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("pay")
	public void payOrder(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		String orderSubNo = request.getParameter("orderNo");
		String payPlatform = request.getParameter("payPlatform");
		String eparchyCode = request.getParameter("eparchyCode");

		Map<String, Object> resultMap = orderService.mergeOrder(orderSubNo);
		TfOrder order = (TfOrder) resultMap.get("order");

		/* 分润规则 */
		CompanyInfo companyInfo = new CompanyInfo();
		companyInfo.setChannelCityCode(eparchyCode);
		companyInfo.setCompanyTypeId((short) 7);

		/**
		 * 分润 规则宽带与续费订单支付宝走非分润接口 查询地市的分润账户， 有可能分润账户的信息为空(当为空时为支付宝支付) 需要判断一下
		 */
		CompanyAcctInfo eparchyAcctInfo = companyAcctService.getAcctByChannelAndType(companyInfo,
				BroadbandConstants.PAY_PLATFORM.get(payPlatform));
		logger.debug("eparchyAcctInfo:" + eparchyAcctInfo);
		String shRule = "";
		if (eparchyAcctInfo != null) {
			shRule = eparchyAcctInfo.getAccountNum() + "^" + order.getOrderAmount() + "^宽带新装";
		}
		logger.info("单宽带新装,分润规则:" + shRule);

		// 结算价格
		Long orderAmount = Long.valueOf(order.getOrderAmount() + "");
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		// 同步页面回调地址
		String callbackUrl = basePath + "/broadband/toPayResult";
		// 异步页面回调地址
		String notifyCallbackUrl = afterOrderPayUrl + "/broadband/payAfterNotify";

		TfOrderPay orderPay = new TfOrderPay();
		orderPay.setOrderId(order.getOrderId());
		orderPay.setOrderPayAmount(orderAmount);
		orderPay.setOrgCode(payPlatform);
		orderPay.setShRule(shRule);
		orderPay.setHmac(BroadbandConstants.SIGN_KEY);
		orderPay.setOrderHarvestExpend("0");
		orderPay.setMerchantId(BroadbandConstants.MERCHANT_ID);

		String content = orderService.applyPay(orderPay, callbackUrl, notifyCallbackUrl);

		String encode = CommonParams.PAY_CHARSET.get(payPlatform);
		if (encode == null) {
			encode = "GBK";
		}
		response.setContentType("text/html;charset=" + encode);
		PrintWriter out = response.getWriter();
		out.print(content);
		out.flush();
		out.close();
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
	public String toPayResult(HttpServletRequest request, HttpServletResponse response, Model model, String returnCode,
			Long chargeflowId, String merchantId) throws Exception {
		logger.info("魔百和在线办理,支付同步回调参数:returnCode=" + returnCode + ",chargeflowId=" + chargeflowId);
		TfOrderSub orderSubParam = new TfOrderSub();
		orderSubParam.setOrderId(chargeflowId);
		orderSubParam.setPage(new Page<TfOrderSub>(1, 100));
		Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
		List<TfOrderSub> orderSubList = orderSubPage.getList();
		if (orderSubList != null && orderSubList.size() > 0) {
			TfOrderSub orderSub = orderSubList.get(0);
			if (orderSub.getDetailList() != null && orderSub.getDetailList().size() > 0) {
				TfOrderSubDetail orderSubDetail = orderSub.getDetailList().get(0);
				model.addAttribute("orderSubDetail", orderSubDetail);
			}
		}
		model.addAttribute("returnCode", returnCode);
		return "web/broadband/payResult";
	}

	/**
	 * 跳转到订单支付结果页面，供支付中心调用（异步）
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/payAfterNotify")
	public void payAfterNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			logger.info("魔百和在线办理,支付异步回调参数:" + request.getParameterMap());
			String orderId = request.getParameter("orderId");
			String merchantId = request.getParameter("merchantId");
			String accountDate = request.getParameter("accountDate");

			MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
			Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
			if (!validataService.valHmac(payParamMap, merChantBean)) {
				throw new Exception("魔百和在线办理:签名验证未通过");
			}

			TfOrderSub orderSubParam = new TfOrderSub();
			orderSubParam.setOrderId(Long.parseLong(orderId));
			orderSubParam.setPage(new Page<TfOrderSub>(1, 100));
			Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
			List<TfOrderSub> orderSubList = orderSubPage.getList();
			if (orderSubList != null && orderSubList.size() > 0) {
				TfOrderSub orderSub = orderSubList.get(0);
				if (orderSub.getDetailList() != null && orderSub.getDetailList().size() > 0) {
					TfOrderSubDetail orderSubDetail = orderSub.getDetailList().get(0);
					TfOrderSubDetailBusiParam orderSubDetailBusiParam = new TfOrderSubDetailBusiParam();
					orderSubDetailBusiParam.setSkuBusiParamName("DEAL_TIME");
					orderSubDetailBusiParam.setSkuBusiParamValue(accountDate);
					orderSubDetailBusiParam.setSkuBusiParamDesc("会计日期");
					orderSubDetailBusiParam.setOrderSubDetailId(orderSubDetail.getOrderSubDetailId());
					List<TfOrderSubDetailBusiParam> busiParamList = Lists.newArrayList();
					busiParamList.add(orderSubDetailBusiParam);
					orderService.saveBusinessParam(busiParamList);
				}
			}

		} catch (Exception e) {
			logger.error("魔百和在线办理,支付异步回调失败，异常信息" + e);
			BusiLogUtils.writerLogging(request, "payAfterNotify", "", "", "", "", "", "", "", e, "2", "");
		}

	}

}
