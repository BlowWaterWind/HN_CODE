package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串处理工具类
 * 
 * <p>
 * detailed comment
 * 
 * @author dyy 2016年3月9日
 * @see
 * @since 1.0
 */
public class UtilString {
	/**
	 * 默认字符串处理
	 * 
	 * @param str
	 * @return
	 */
	public static String defaultString(String str) {
		return StringUtils.isEmpty(str) ? "" : str;
	}

	/**
	 * 会员名显示前三位和后三位
	 * 
	 * @param str
	 * @return
	 */
	public static String showMemberName(String name) {
		if (StringUtils.isEmpty(name)||"null".equals(name)) {
			return "";
		}
		return name.substring(0, 3) + "***"
				+ name.substring(name.length() - 4, name.length());
	}

	public static String isMobileNO(String mobiles) {
		if (mobiles == null) {
			return "";
		}
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		if (m.matches()) {
			return showMemberName(mobiles);
		} else {
			return mobiles;
		}

	}

	public static String clearXss(String value){
		{
			if (value == null || "".equals(value) || value =="null") {
				return value;
			}
			if(StringUtils.isNotBlank(value)){
				value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				value = value.replaceAll("'", "");
				// value = value.replaceAll("\"", "&quot;"); json, 或者放开，需要对json中的&quot;转型成"
				value = value.replaceAll("eval\\((.*)\\)", "");
				value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
				value = value.replaceAll("script", "");

				// Avoid anything between script tags
				Pattern scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				value = scriptPattern.matcher(value).replaceAll("");

				// Avoid vbscript:... e­xpressions
				scriptPattern = Pattern.compile("(javascript:|vbscript:|view-source:)*", Pattern.CASE_INSENSITIVE);
				value = scriptPattern.matcher(value).replaceAll("");

				// Avoid onload= e­xpressions
				scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				value = scriptPattern.matcher(value).replaceAll("");


				scriptPattern = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE);
				value = scriptPattern.matcher(value).replaceAll("");

				scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				value = scriptPattern.matcher(value).replaceAll("");

				scriptPattern = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				value = scriptPattern.matcher(value).replaceAll("");

				scriptPattern = Pattern.compile("(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*",  Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				value = scriptPattern.matcher(value).replaceAll("");

				//js sql 关键字
				scriptPattern = Pattern.compile("<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|"
						+ "ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|"
						+ "onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|"
						+ "onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|"
						+ "onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|"
						+ "onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|"
						+ "oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|"
						+ "onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|"
						+ "onsubmit|onunload)+\\s*=+",  Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				value = scriptPattern.matcher(value).replaceAll("");
			}
			return value;
		}

	}
}
