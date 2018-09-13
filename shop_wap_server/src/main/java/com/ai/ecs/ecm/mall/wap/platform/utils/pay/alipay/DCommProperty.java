package com.ai.ecs.ecm.mall.wap.platform.utils.pay.alipay;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 
 * 只依赖jse
 * hg中的TbUserAction为例子
 * PropertyComm 
 *  
 * @author：邓国明
 * @Aug 22, 2012 9:27:30 AM 
 * @version 1.0
 */
public class DCommProperty {

	private static Map<String, Properties> propertyMap;

	private Properties p = new Properties();

	private DCommProperty() {

	}

	/**
	 * 通过url直接得到propertyComm,这里的用了map来存储，并非单例 后者不会覆盖前者 getPropertyFactory
	 * 
	 * @param propertyUrl
	 * @return PropertyComm
	 * @Exception
	 * @author：denggm@asiainfo-linkage.com
	 * @Aug 10, 2012 5:04:12 PM
	 * @update:
	 * @Aug 10, 2012 5:04:12 PM
	 */
	public static Properties getPropertyInstance(String propertyUrl) {
		if (propertyMap == null) {
			propertyMap = new HashMap<String, Properties>();
		}
		if (propertyMap.containsKey(propertyUrl)) {
			return propertyMap.get(propertyUrl); //说明已存在，直接返回
		}
		// 不包含在map中，要创建
//		DCommProperty comm = new DCommProperty();
		Properties properties = new Properties();

		if (propertyUrl == null) { // 为空，则
			return null;
		}
		InputStream in = null;
		try {
			in = DCommProperty.class.getResourceAsStream(propertyUrl);
			properties.load(in);
			propertyMap.put(propertyUrl, properties);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("得到properties错误,propertyUrl:"+propertyUrl+"请检查propertyUrl");
		//	e.printStackTrace();
		}
		return properties;
	}
	
	
	


}
