package com.ai.ecs.ecm.mall.wap.modules.goods.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.ecs.order.entity.TfOrderSubDetailBusiParam;
import com.google.common.collect.Lists;

/**
 * 参数组装类
 * 
 * 注意：添加完参数之后请务必务必通过getParamsList方法获取list
 * @author 臧弄潮
 */
public class ParamUtils {
	private List<TfOrderSubDetailBusiParam> busiParamList;
	private TfOrderSubDetailBusiParam tempParam;
	
	public ParamUtils(){
		busiParamList = Lists.newArrayList();
	}

	public void addParams(String paramName,String value,String desc){
		tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName(paramName);
        tempParam.setSkuBusiParamValue(value);
        tempParam.setSkuBusiParamDesc(desc);
        busiParamList.add(tempParam);
	}
	
	public List<TfOrderSubDetailBusiParam> getParamsList(){
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
}
