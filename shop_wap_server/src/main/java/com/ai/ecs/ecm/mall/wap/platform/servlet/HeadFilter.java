package com.ai.ecs.ecm.mall.wap.platform.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HeadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String myhosts = request.getHeader("host");
        if(null!=myhosts&&!"".equals(myhosts)){
            myhosts = myhosts.trim();
            if(myhosts.indexOf("wap.hn.10086.cn")==-1
                    //&& myhosts.indexOf("111.8.20.252")==-1
                    && myhosts.indexOf("10.159.98")==-1
                    && myhosts.indexOf("10.154.73")==-1
                    && myhosts.indexOf("15.15.20")==-1
                    && myhosts.indexOf("localhost")==-1
                    && myhosts.indexOf("up-shopwap")==-1
                    && myhosts.indexOf("127.0.0.1")==-1
                    && myhosts.indexOf("10.154.77")==-1
                    && myhosts.indexOf("10.13.11")==-1
                    && myhosts.indexOf("192.192.1")==-1
                    && myhosts.indexOf("172.168.20")==-1
                    && myhosts.indexOf("wapshop.hn.cmcc")==-1
                    && myhosts.indexOf("pcshop.hn.cmcc")==-1
                    && myhosts.indexOf("ecop.hn.cmcc")==-1
                    && myhosts.indexOf("upay.hn.cmcc")==-1
                    && myhosts.indexOf("ecop.hn.cmcc")==-1
                    && myhosts.indexOf("10.159.113.121")==-1
                    && myhosts.indexOf("10.159.112.47")==-1
                    && myhosts.indexOf("111.8.20")==-1){
                System.out.println("host值2:"+myhosts);
                response.setStatus(403);
                return ;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
