package com.ai.ecs.ecm.mall.wap.platform.servlet;

import com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.XssHttpServletRequestWraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class XssFilter implements Filter {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	public static final Map<String,String> URL_MAP = PropertiesUtil.getInstance().getPropMap();
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if(checkRefer((HttpServletRequest)request)){
			throw new ServletException("referer error!");
		}
		chain.doFilter(new XssHttpServletRequestWraper(
                (HttpServletRequest)request), response);//对request和response进行过滤
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	/**
	 * 判断refer头，在判断token之后进一步做判断
	 */
	public boolean checkRefer(HttpServletRequest request){
		boolean result = false;
		try {
			String method = ((HttpServletRequest) request).getMethod();
			String requestType = request.getHeader("X-Requested-With");
			String url = request.getRequestURI();
			String referer="";

			if(validPropertiesUrl(url)){
				logger.info("特殊路径放开过滤,访问url:"+request.getRequestURL());
				return false;
			}else if(method.equalsIgnoreCase("POST") ){//只处理post请求
				referer = request.getHeader("Referer");
				if((referer==null) ||
						(!referer.trim().startsWith("http://111.8.20"))
						&&(!referer.trim().startsWith("http://10.154.73"))
						&&(!referer.trim().startsWith("http://10.159.98"))
						&&(!referer.trim().startsWith("http://wap.hn.10086.cn/"))
						&&(!referer.trim().startsWith("https://wap.hn.10086.cn/"))
						&&(!referer.trim().startsWith("http://www.10086.cn/"))
						&&(!referer.trim().startsWith("https://www.10086.cn/"))
						&&(!referer.trim().contains("172.168.20"))
						&&(!referer.trim().contains("192.192.1"))
						&&(!referer.trim().contains("cyou100.com/"))
						&&(!referer.trim().contains("http://localhost"))
						&&(!referer.trim().contains("http://10.13.10.2"))
						&&(!referer.trim().contains("http://15.15.20.42"))
						&&(!referer.trim().contains("10.159.113.121"))
						&&(!referer.trim().contains("10.159.112.47"))
						&&(!referer.trim().startsWith("http://10.13.11"))
						&&(!referer.trim().contains("http://localhost")))
				{
					LogUtils.writerFlowLogThrowable(LogUtils.createStreamNo(),"","",getClass().getName(),
							"checkRefer ", request.getParameterMap(),referer+"-refer filter："+request.getRequestURL());
					return false;
				}
				return false;

			}
		} catch (Exception e) {
			result = true;
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 过滤配置文件中的特殊URL
	 * @Title validPropertiesUrl
	 * @Description:
	 * @author ruanrf
	 * @param url
	 * @return
	 */
	private boolean validPropertiesUrl(String url){
		String key = null;
		String value = null;
		for (Map.Entry<String, String> entry:URL_MAP.entrySet()) {
			key = entry.getKey();
			value = URL_MAP.get("context.root")+entry.getValue();
			if(url.contains(value) && !"context.root".equals(key)){
				return true;
			}
		}
		return false;
	}
}