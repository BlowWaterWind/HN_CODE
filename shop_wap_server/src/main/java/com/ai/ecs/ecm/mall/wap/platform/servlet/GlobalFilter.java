package com.ai.ecs.ecm.mall.wap.platform.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * com.ai.ecs.mall.platform.servlet
 * Author: R.H.Feng
 */
public class GlobalFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(getClass());
    public static final String[] BAD_PARAMS = "＜|＞|<|>|\\|javascript|alert|'|".split("\\|");
    public static final List<String> LOGIN_FILTER_PAGE = new ArrayList<String>();

    // 注册处理类实例。新的逻辑写到新的 Processor 类里面。
   /* private static final List<Processor> PROCESSORS = new ArrayList<Processor>(Arrays.asList(
    		new AutoLoginProcessor()
    ));*/

    public void init(FilterConfig filterConfig) throws ServletException {
    	
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8");
        //是否包含非法字符
        if(isIllegalStr(request)){
            response.sendRedirect(request.getContextPath());
            return;
        }
        /*for (Processor processor : PROCESSORS) {
            if (!processor.execute(request, response)) {
                return;
            }
        }*/
        chain.doFilter(servletRequest, servletResponse);
        response.setHeader("X-Powered-By","");
    }

    //是否含有非法字符
    private boolean isIllegalStr(HttpServletRequest request) {

        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
            //得到参数对应值
            String[] parameterValues = request.getParameterValues(name);
            for (String parameter : parameterValues) {
                if (paramValidate(parameter, name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
  //判断参数值中是否包含非法字符
    private boolean paramValidate(String param, String name) {
        for (String str : BAD_PARAMS) {
            if (param.contains(str)) {
                log.error("当前参数包含非法字符:"+ name +"=" + param);
                return true;
            }
        }
        return false;
    }

    public void destroy() {
    }
}
