package com.ai.ecs.ecm.mall.wap.platform.servlet;

import com.google.common.collect.Maps;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	private static Map<String, String> proMap = Maps.newHashMap();

    private static class PropertiesInstance {
        private static final PropertiesUtil props = new PropertiesUtil();
    }

    public static PropertiesUtil getInstance() {
        return PropertiesInstance.props;
    }

    public Map<String, String> getPropMap() {
        return proMap;
    }

    private PropertiesUtil() {
        proMap = readProperties();
    }

    /**
     * 去读properties文件的内容，保存到Map中
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    private static Map<String, String> readProperties() {
        Properties props = new Properties();
        try {
            InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("xss_filter_url.properties");
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = props.getProperty(key);
                proMap.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proMap;
    }
}
