/**
 * <p>Copyright: Copyright (c) 2014</p>
 */
package com.ai.ecs.ecm.mall.wap.platform.interceptor;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ai.ecs.ecm.mall.wap.platform.utils.JsonpUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.entity.MemberVo;

/**
 * 检查是否登录
 * @author dyy
 * @date: 2015年4月15日 上午10:19:00
 * @version 1.0
 * @since JDK 1.7
 */
public class CheckoutInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(CheckoutInterceptor.class);

    /** 未登录的状态码 */
    public final static String NOT_LOGIN_CODE = "600";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberVo member=UserUtils.getLoginUser(request);
        if (member != null) {
            return true;
        }

        String url = request.getRequestURL().toString();
        logger.debug("用户未登录，当前请求的url:{}", url);

        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("code", NOT_LOGIN_CODE);
        errorMap.put("msg", "登录失效");
        result.put("_error", errorMap);
        JsonpUtils.writeToResponse(request, response, result);
        return false;
    }

}
