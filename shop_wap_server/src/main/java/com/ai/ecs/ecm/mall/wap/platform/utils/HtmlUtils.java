package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * simple introduction
 *
 * <p>detailed comment
 * @author dyy 2016年3月14日
 * @see
 * @since 1.0
 */
public class HtmlUtils {

    private static NumberFormat defaultPriceFormat = new DecimalFormat("###,###,###,###,###,###.00");

    private static NumberFormat defaultNumberFormat = new DecimalFormat("##################.##");

    public static String defaultString(Object text) {
        return defaultString(text, "");
    }

    public static String defaultString(Object text, String defaultValue) {
        if (text == null) {
            return defaultValue;
        } else {
            return encode(text.toString());
        }
    }
    
    /**
     * html转义
     * 
     * @param html
     * @return
     */
    public static String encode(String html) {
        if (html != null) {
            html = html.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(" ", "&nbsp;")
                    .replaceAll("\'", "&#39;").replaceAll("\"", "&quot;");
        }
        return html;
    }

    public static String defaultDate(Date date) {
        return defaultDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String defaultDate(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            return DateFormatUtils.format(date, pattern);
        }
    }

    public static String defaultDate(Long date) {
        return defaultDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String defaultDate(Long date, String pattern) {
        if (date == null || date == 0) {
            return "";
        } else {
            return DateFormatUtils.format(new Date(date), pattern);
        }
    }

    public static String defaultPrice(Number num) {
        return formatNumber(num, defaultPriceFormat);
    }

    public static String defaultNumber(Number num) {
        return formatNumber(num, defaultNumberFormat);
    }

    public static String defaultNumber(Number num, String pattern) {
        if (num == null) {
            return "";
        } else {
            NumberFormat format = new DecimalFormat(pattern);
            return format.format(num);
        }
    }

    private static String formatNumber(Number num, NumberFormat format) {
        if (num == null) {
            return "";
        } else {
            return format.format(num);
        }
    }

    public static String fixLengthString(String text, int maxLength) {
        if (text == null) {
            return "";
        } else {
            int len = text.length();
            if (len <= maxLength) {
                return text;
            } else {
                StringBuilder s = new StringBuilder();
                s.append(text.substring(0, maxLength - 3)).append("...");
                return s.toString();
            }
        }
    }

}
