package com.ai.ecs.ecm.mall.wap.platform.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil
{
    private static final Logger log = Logger.getLogger(PropertiesUtil.class);

    private static Properties properties = new Properties();

    static
    {
        try
        {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("ecsite.properties"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static String getValue(String key, String defaultValue) {
        return properties.getProperty(key,defaultValue);
    }

    public static int getIntValue(String key, String defaultValue) {
        return Integer.valueOf(StringUtils.trimToEmpty(properties.getProperty(key, defaultValue)));
    }

}