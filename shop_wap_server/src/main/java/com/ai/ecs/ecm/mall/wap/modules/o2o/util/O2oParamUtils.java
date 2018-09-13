package com.ai.ecs.ecm.mall.wap.modules.o2o.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecsite.modules.base.AbilityAbstractCondition;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 参数组装类
 *
 * 注意：添加完参数之后请务必务必通过getParamsList方法获取list
 * @author 臧弄潮
 */
@Component
@Scope("prototype")
public class O2oParamUtils {

	@Autowired
	private PhoneAttributionService phoneAttributionService;

	private List<O2oOrderParamTemp> busiParamList;
	private O2oOrderParamTemp tempParam;
	private final static String CITY_CODE="0732,0739,0731,0733,0734,0730,0736,0744,0737,0735,0746,0745,0738,0743";

	public O2oParamUtils(){
		busiParamList = Lists.newArrayList();
	}

	public void addChannelInfo(ChannelInfo channelInfo,String installPhoneNum){
		this.addParams("PROVINCE_CODE",channelInfo.getProvinceCode(),"渠道省编号");
		this.addParams("TRADE_DEPART_ID",channelInfo.getTradeDepartId(),"渠道交易部门编号");
//		this.addParams("TRADE_DEPART_PASSWD",channelInfo.getTradeDepartPasswd(),"渠道交易部门密码");

		this.addParams("TRADE_STAFF_ID",channelInfo.getTradeStaffId(),"渠道交易工号");
//		this.addParams("TRADE_TERMINAL_ID",channelInfo.getTradeTerminalId(),"渠道交易终端地址");
		this.addParams("IN_MODE_CODE",channelInfo.getInModeCode(),"渠道交易类型");
		if(StringUtils.isNotEmpty(installPhoneNum)&&!CITY_CODE.contains(channelInfo.getTradeEparchyCode())){
			String cityCode = this.getCityCode(installPhoneNum);
			this.addParams("TRADE_CITY_CODE",cityCode,"渠道交易市区编号");
			this.addParams("ROUTE_EPARCHY_CODE",cityCode,"渠道用户市区编号");
			this.addParams("TRADE_EPARCHY_CODE",cityCode,"渠道交易区域编码");
		}else{
			this.addParams("TRADE_CITY_CODE",channelInfo.getTradeCityCode(),"渠道交易市区编号");
			this.addParams("ROUTE_EPARCHY_CODE",channelInfo.getRouteEparchyCode(),"渠道用户市区编号");
			this.addParams("TRADE_EPARCHY_CODE",channelInfo.getTradeEparchyCode(),"渠道交易区域编码");
		}
	}

	/**
	 * 注入工号信息
	 * @param condition
	 * @param channelInfo
     */
	public void addConditionChannel(AbilityAbstractCondition condition,ChannelInfo channelInfo,String installPhoneNum){
		if(StringUtils.isNotEmpty(installPhoneNum)&&!CITY_CODE.contains(channelInfo.getTradeEparchyCode())){
			String cityCode = this.getCityCode(installPhoneNum);
			condition.setCityCode(cityCode);
			condition.setEparchyCodeFCust(cityCode);
		}else{
			condition.setEparchyCodeFCust(channelInfo.getTradeEparchyCode());
			condition.setCityCode(channelInfo.getTradeCityCode());
		}
		condition.setDepartId(channelInfo.getTradeDepartId());
		condition.setStaffId(channelInfo.getTradeStaffId());
		condition.setInModeCode(channelInfo.getInModeCode());
		condition.setChanId(channelInfo.getChanelId());
	}
	public void removeParams(){
		busiParamList = new ArrayList<>();
	}

	public void addParams(String paramName,String value,String desc){
		tempParam = new O2oOrderParamTemp();
        tempParam.setParamName(paramName);
        tempParam.setParamValue(value);
        tempParam.setParamDesc(desc);
        busiParamList.add(tempParam);
	}



	public List<O2oOrderParamTemp> getParamsList(){
		return busiParamList;
	}

	/**
	 * 费用参数解析
	 * @param feeList
	 * @return
	 * @throws Exception
     */
	public  List<Map<String, Object>> parseFeeList(List<Map<String,Object>> feeList) throws Exception {
		List<Map<String,Object>> feeResult = new ArrayList<>();
		String splitFlag = ",";
		for(Map<String,Object> fee:feeList){
			String TRADE_TYPE_CODE = (String) fee.get("TRADE_TYPE_CODE");
			String FEE = (String) fee.get("FEE");
			String FEE_TYPE_CODE = (String) fee.get("FEE_TYPE_CODE");
			String FEE_MODE = (String) fee.get("FEE_MODE");
			String PAY = (String) fee.get("PAY");
			if (TRADE_TYPE_CODE == null || FEE == null
					|| FEE_TYPE_CODE == null || FEE_MODE == null
					|| PAY == null) {
				throw new Exception("FEE_LIST相关参数部分为空" +
						"[TRADE_TYPE_CODE:" + TRADE_TYPE_CODE + "]" +
						"[FEE:" + FEE + "][FEE_TYPE_CODE:" + FEE_TYPE_CODE + "]" +
						"[FEE_MODE:" + FEE_MODE + "]" +
						"[PAY:" + PAY + "]");
			} else if (TRADE_TYPE_CODE == null && FEE == null
					&& FEE_TYPE_CODE == null && FEE_MODE == null
					&& PAY == null) {
				return null;
			}
			String[] trade_type_code_arr = TRADE_TYPE_CODE.split(splitFlag);
			String[] fee_arr = FEE.split(splitFlag);
			String[] fee_type_code_arr = FEE_TYPE_CODE.split(splitFlag);
			String[] fee_mode_arr = FEE_MODE.split(splitFlag);
			String[] pay_arr = PAY.split(splitFlag);
			if (trade_type_code_arr.length != fee_arr.length ||
					fee_arr.length != fee_type_code_arr.length ||
					fee_type_code_arr.length != fee_mode_arr.length ||
					fee_mode_arr.length != pay_arr.length) {
				throw new Exception("FEE_LIST相关参数分割记录数不相等");
			}
			for (int i = 0; i < trade_type_code_arr.length; i++) {
				Map<String, Object> feeMap = new HashMap<String, Object>();
				feeMap.put("TRADE_TYPE_CODE", trade_type_code_arr[i]);
				feeMap.put("FEE", fee_arr[i]);
				feeMap.put("FEE_TYPE_CODE", fee_type_code_arr[i]);
				feeMap.put("FEE_MODE", fee_mode_arr[i]);
				feeMap.put("PAY", pay_arr[i]);
				feeResult.add(feeMap);
			}
		}
		return feeResult;
	}

	/**
	 * 获取手机号码的归属地市编码
	 * @param installPhoneNum
	 * @return
	 * @throws Exception
	 */
	private String getCityCode(String installPhoneNum) {
		PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
		phoneAttributionModel.setSerialNumber(installPhoneNum);
		Map<String, Object> resultMap = null;
		try {
			resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(((Map)((List)resultMap.get("result")).get(0)).get("AREA_CODE"));
	}
}
