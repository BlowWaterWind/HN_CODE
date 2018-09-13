package com.ai.ecs.ecm.mall.wap.modules.goods.service;

import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.bean.PayValResultBean;

import java.util.Map;

public interface IUppHtmlValidataService {
	/**
	 * 签名验证
	 * @param payParamMap
	 * @return
	 */
	public boolean valHmac(Map<String, String> payParamMap, MerChantBean merchantBean);

	/**
	 * 必要参数校验
	 * @param payParamMap
	 * @return
	 */
	public PayValResultBean valParam(Map<String, String> payParamMap);

	/**
	 * 根据商户号得到商户信息（UPP）
	 * @param merchantId
	 * @return
	 */
	public MerChantBean querybyMerchantId(String merchantId);
}
