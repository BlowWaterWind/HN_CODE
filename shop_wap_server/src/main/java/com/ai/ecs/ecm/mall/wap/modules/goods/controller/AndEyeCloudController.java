package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AndEyeUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.AndEyeCloudCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqDreamNetQryCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.BasisServeService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.AndEyeItemEntity;
import com.ai.ecs.member.entity.MemberVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoResult;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * 和目云存储
 * @author hexiao3@asiainfo.com
 */
@Controller
@RequestMapping("andeyecloud")
public class AndEyeCloudController {
	private Logger logger = Logger.getLogger(AndEyeCloudController.class);
	@Autowired
	private IGoodsManageService goodsManageService;

	@Autowired
	private BroadBandService broadBandService;

	@Autowired
	BasisServeService basisServeService;

	/**i
	 * WAP商城和目首页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {

		return "web/broadband/andcloud/index";
	}

	/**
	 * 和目云存储业务办理提交
	 * @param request
	 * @param response
	 * @param model
	 * @return
     * @throws Exception
     */
	@RequestMapping("comfirmOrder")
	public String comfirmOrder(HttpServletRequest request , HttpServletResponse response, Model model) throws Exception {
		Map resultMap = new HashMap();
		resultMap.put("respCode","-1");
		resultMap.put("respDesc", "和目云存储业务办理失败!");
		String serialNumber ="";
		try {
			MemberVo memberVo = UserUtils.getLoginUser(request);
			serialNumber = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
//            serialNumber = "13808493515";
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e,e);
		}
		if(StringUtils.isBlank(serialNumber)){
			throw new Exception("请登录或登录帐号未绑定手机号!");
		}else{
			model.addAttribute("installPhoneNum", serialNumber);
		}

		String duration = request.getParameter("DURATION_VAL");
		String packageName = request.getParameter("PACKAGE_VAL");
		model.addAttribute("packageName",packageName);
		model.addAttribute("duration",duration);
		/**
		 * 商品查询
		 */
		Map<String,Object> params = new HashMap<String,Object>();

		params.put("containGoodsSkuIdInfo",true);
		params.put("containGoodsBusiParam",true);
		params.put("duration","DURATION");
		params.put("durationVal",duration);
		params.put("package","PACKAGE");
		params.put("packageVal",packageName);
		List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByParams(params);
		List<AndEyeItemEntity> andEyeItemEntityList = AndEyeUtils.covertAndEye(goodsInfoList);

		/**
		 * 已办理SP业务查询
		 */
		HqDreamNetQryCondition hqDreamNetQryCondition = new HqDreamNetQryCondition();
//        hqDreamNetQryCondition.setSerialNumber("13974956455");
		hqDreamNetQryCondition.setDealTag("00");
		hqDreamNetQryCondition.setRemoveTag("0");
		hqDreamNetQryCondition.setTagChar("0");
//		hqDreamNetQryCondition.setStaffId("ITFWC000");
//		hqDreamNetQryCondition.setTradeDepartPassword("ai1234");
		hqDreamNetQryCondition.setSerialNumber(serialNumber);;
		Map hasHandled =basisServeService.getDreamBusi(hqDreamNetQryCondition);
		List<Map> handledList = (List) hasHandled.get("result");
		for(Map handle:handledList){
			Iterator iterable = andEyeItemEntityList.iterator();
			while (iterable.hasNext()){
				AndEyeItemEntity  andEyeItemEntity1= (AndEyeItemEntity) iterable.next();
				if(andEyeItemEntity1.getBizCode().equals(handle.get("BIZ_CODE"))){
					iterable.remove();
				}
			}
		}
		/**
		 * 选择办理产品
		 */
		AndEyeItemEntity andEyeItemEntity = new AndEyeItemEntity();
		if(andEyeItemEntityList!=null&&andEyeItemEntityList.size()>0){
			andEyeItemEntity = andEyeItemEntityList.get(0);
			/**
			 * 调用办理接口
			 */
			MemberVo memberVo = UserUtils.getLoginUser(request);
			AndEyeCloudCondition condition = new AndEyeCloudCondition();
			condition.setSerialNumber(serialNumber);
			condition.setBizCode(andEyeItemEntity.getBizCode());
			condition.setBizTypeCode(andEyeItemEntity.getBizTypeCode());
			condition.setOperCode("06");
			condition.setOprSource("08");
			condition.setSpCode(andEyeItemEntity.getSpCode());
//			condition.setStaffId("ITFWC000");
//			condition.setTradeDepartPassword("ai1234");

			resultMap = broadBandService.platInfoRegE(condition);
		}else{
			resultMap.put("respCode","-1");
			resultMap.put("respDesc", "用户已办理和目业务，无可用业务办理！");
		}
		model.addAttribute("resultMap",resultMap);
		return "web/broadband/andcloud/result";
	}
}
