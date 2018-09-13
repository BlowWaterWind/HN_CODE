package com.ai.ecs.ecm.mall.wap.platform.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * XSS过滤工具类
 * @author dyy
 * @date 2015-8-6
 */
public class XssHttpServletRequestWraper extends HttpServletRequestWrapper {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/* private static String[] safeless = {"<script",   //需要拦截的JS字符关键字
             "</script",
             "<iframe",
             "</iframe",
             "<frame",
             "</frame",
             "set-cookie",
             "%3cscript",
             "%3c/script",
             "%3ciframe",
             "%3c/iframe",
             "%3cframe",
             "%3c/frame",
             "src=\"javascript:",
             "<body",
             "</body",
             "%3cbody",
             "%3c/body",
             "onblur"
            };
	 private static String  SQLStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|"
				+ "char|declare|sitename|netuser|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|"
				+ "table|from|grant|use|group_concat|column_name|"
				+ "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|"
				+ "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";
	 */

	public XssHttpServletRequestWraper(HttpServletRequest request) {
		super(request);
		Map<String, String> paramMap = getParamMap(request);
		if (MapUtils.isNotEmpty(paramMap)) {
			for (String key : paramMap.keySet()) {
				if(StringUtils.isNotEmpty(paramMap.get(key))){
					request.removeAttribute(key);
					request.setAttribute(key, clearXss(paramMap.get(key)));
				}
			}
		}

	}

	public Map<String, String> getParamMap(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		Map<String, String[]> requestMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : requestMap.entrySet()) {
			paramMap.put(entry.getKey(), entry.getValue()[0]);
		}
		return paramMap;
	}

	@Override
	public String getParameter(String name) {
		return clearXss(super.getParameter(name));
	}

	@Override
	public String getHeader(String name) {
		return clearXss(super.getHeader(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null) {
			return null;
		}
		String[] newValues = new String[values.length];

		for (int i = 0; i < values.length; i++) {
			//logger.debug("--------------Start-------------------getParameterValues "+values[i]);
			newValues[i] = clearXss(values[i]);
		}

		return newValues;
	}


	/*String badStr = "'|and|exec|execute|insert|create|drop|table|from|grant|use|group_concat|column_name|" +
            "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
            "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可
*/	/**
	 * 处理字符转义
	 *
	 * @param value
	 * @return
	 */
	private String clearXss(String value) {
		//logger.debug("--------------Start-------------------clearXss "+value);
		if (value == null || "".equals(value) || value =="null") {
			return value;
		}
		if(StringUtils.isNotBlank(value)){
			value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			try{
				String requestUrl = getRequestURL().toString();//获取请求url
				//过滤宽带地址查询url,防止小区地址中存在括号的情况下无法正常查询出来．
				if(StringUtils.isNotBlank(requestUrl) && (requestUrl.indexOf("/qryAddressCommunityName") != -1
						|| requestUrl.indexOf("/qryAddressBuildingName") != -1 || requestUrl.indexOf("/qryAddressName") != -1)){
					//不对括号进行过滤
				} else {
					value = value.replaceAll("\\(", "（").replaceAll("\\)", "）");
				}
			}catch (Exception e ){
				logger.error("获取请求url错误...");
			}
			value = value.replaceAll("'", "*");
			// value = value.replaceAll("\"", "&quot;"); json, 或者放开，需要对json中的&quot;转型成"
			value = value.replaceAll("eval\\((.*)\\)", "*");
			value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
			value = value.replaceAll("script", "*");

			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid vbscript:... e­xpressions
			scriptPattern = Pattern.compile("(javascript:|vbscript:|view-source:)+", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("*");

			// Avoid onload= e­xpressions
			scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("*");


			scriptPattern = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("*");

			scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("*");

			scriptPattern = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()+",  Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("*");

			//js sql 关键字 去掉< 防止在标签中拼接
			scriptPattern = Pattern.compile("\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|"
					+ "ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|"
					+ "onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|"
					+ "onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|"
					+ "onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|"
					+ "onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|"
					+ "oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|"
					+ "onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|"
					+ "onsubmit|onunload)+\\s*=+",  Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("*");
            /*

            //----js


            //SQL 替換//开发中注意这几个关键字使用
            value = value.replaceAll("execute", "").replaceAll("insert", "");
            value = value.replaceAll("delete", "").replaceAll("update", "");
            value = value.replaceAll("truncate", "").replaceAll("drop", "");
            value = value.replaceAll("grant", "");*/

		}

		//logger.debug("--------------end-------------------clearXss "+value);
		return value;
	}

}