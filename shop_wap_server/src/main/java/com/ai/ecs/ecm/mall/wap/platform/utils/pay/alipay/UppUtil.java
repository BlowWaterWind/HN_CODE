package com.ai.ecs.ecm.mall.wap.platform.utils.pay.alipay;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;



public class UppUtil {

	/**
	 * 
	 * @param busiStr a=b&b=c&c=d
	 * @return
	 * @throws Exception 
	 */
	public static Map getMapByStr(String busiStr) throws Exception{
		if(busiStr==null||busiStr.length()<1){
			throw new NullPointerException("busiStr不可为空");
		}
		System.out.println("入参:"+busiStr);
		String[] strArray = busiStr.split("&");
		Map map = new HashMap();
		for (int i = 0; i < strArray.length; i++) {
			//形如a=b
			String s = strArray[i];
			String[] tmparray = s.split("=");
			if(tmparray.length<2){
				map.put(tmparray[0],"");
			}else{
				map.put(tmparray[0],URLDecoder.decode(tmparray[1],"utf8"));
			}
		}
		return map;
	}
	
}
