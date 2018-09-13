package com.ai.ecs.ecm.mall.wap.modules.goods.service;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.bean.PayTypeBean;
import com.ai.iis.upp.bean.PayValResultBean;
import com.ai.iis.upp.service.IMerchantService;
import com.ai.iis.upp.service.IPayTypeService;
import com.ai.iis.upp.util.UppContants;
import com.ai.iis.upp.util.UppCore;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ValidataServiceImpl implements IUppHtmlValidataService {
	private static Logger log = Logger.getLogger(ValidataServiceImpl.class);

	@Autowired
	private IPayTypeService payTypeService;

	@Autowired
	private IMerchantService merchantService;

	@Override
	public boolean valHmac(Map<String, String> payParamMap,MerChantBean merchantBean) {
		// TODO Auto-generated method stub
		log.info("valHmac……start……");
		String signKey = merchantBean.gettEzfMerchantKey();
		if (StringUtils.isEmpty(signKey)) {
			return false;
		}
		String hmac = (String) payParamMap.get("hmac");
		log.info("valHmac……"+hmac);
		String serverHmac = UppCore.getHmac(payParamMap, signKey, "UTF-8");
		return hmac.equals(serverHmac);
	}

	@SuppressWarnings({ "finally", "rawtypes", "unchecked" })
	private PayTypeBean getPayTypeParamInter(String type){

		PayTypeBean payTypeParamBean = new PayTypeBean();
		payTypeParamBean.setPay_type(type);
		PayTypeBean payTypeBean = payTypeService.queryPayTypeByType(payTypeParamBean);
		if(null != payTypeBean){
			try{
				JedisClusterUtils.setObject(UppContants.UPP_CACHE_PAYTYPE_KEY+type, payTypeBean, UppContants.UPP_ORDER_TIME_OUT_SECONDS);
			}catch(Exception e){
				log.info(e);
			}
		}
		return payTypeBean;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PayValResultBean valParam(Map<String, String> payParamMap) {
		// TODO Auto-generated method stub
		log.info("valParam……start……");
		PayValResultBean resultBean;
		String type = (String)payParamMap.get("type");
		PayTypeBean payTypeBean = null;
		try{
			payTypeBean = (PayTypeBean) JedisClusterUtils.getObject(UppContants.UPP_CACHE_PAYTYPE_KEY+type);
			if(null == payTypeBean){
				payTypeBean= getPayTypeParamInter(type);
			}
		}catch(Exception e){
			log.error(e);
			payTypeBean= getPayTypeParamInter(type);
		}
		if(null != payTypeBean){
			String payCheckRules = payTypeBean.getPay_check_rules();
			resultBean = UppCore.validateStrColumnNotNull(payParamMap,payCheckRules.split(","));
			if(resultBean != null && resultBean.getResultCode().equals("0000")){
				resultBean.setTargetUrlTag(payTypeBean.getPay_target());	
				resultBean.setTargetUrlPage(payTypeBean.getPay_target_url());
			}
		}else{
			resultBean = new PayValResultBean();
			resultBean.setResultCode("0096");
			resultBean.setResultMsg("接口类型不合法");
		}
		log.info("valParam……"+ (resultBean != null ? resultBean.toString() : null));
		return resultBean;
	}

	@SuppressWarnings({ "finally" })
	@Override
	public MerChantBean querybyMerchantId(String merchantId) {
		// TODO Auto-genera
		MerChantBean merChantParamBean= null;
		try{
			merChantParamBean = (MerChantBean)JedisClusterUtils.getObject(UppContants.UPP_CACHE_MERCHNAT_KEY+merchantId);
			if(null == merChantParamBean){
				merChantParamBean = querybyMerchantIdInter(merchantId);
			}
		}catch(Exception e){
			log.error(e);
		}
		return merChantParamBean;
	}

	private MerChantBean querybyMerchantIdInter(String merchantId){
		String cacheKey = UppContants.UPP_CACHE_MERCHNAT_KEY + merchantId;
		MerChantBean result = null;
		MerChantBean merChantParamBean = new MerChantBean();
		merChantParamBean.settEzfMerchantId(merchantId);
		result = merchantService.querybyMerchantId(merChantParamBean);
		if(null != result){
			try{
				JedisClusterUtils.del(cacheKey);
				JedisClusterUtils.setObject(cacheKey, result, UppContants.UPP_ORDER_TIME_OUT_SECONDS);
			}catch(Exception e){
				log.error(e);
			}
		}
		return result;
	}
}
