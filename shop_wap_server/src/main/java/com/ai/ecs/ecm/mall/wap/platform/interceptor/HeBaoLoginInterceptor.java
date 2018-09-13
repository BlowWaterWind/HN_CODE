package com.ai.ecs.ecm.mall.wap.platform.interceptor;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.entity.MemberVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.BooleanArraySerializer;
import jodd.util.StringUtil;
import org.apache.shiro.session.Session;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhoujie5 on 2017/5/11.
 * 和包嵌入页面， 当没有登陆上处理拦截器
 * 和包免登陆的拦截器时， AndAppSsoInterceptor
 */
public class HeBaoLoginInterceptor extends HandlerInterceptorAdapter {

    private static final int JEDISTIMEOUT = 1800;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Session session = UserUtils.getSession();
        String loginBaseKey = "LOGIN_BASEINFO_" + session.getId();
        Boolean isHeAppLogin = Boolean.FALSE;

        // 判断redis中是否存在登陆key
        if(JedisClusterUtils.exists(loginBaseKey)){
            String json = JedisClusterUtils.get(loginBaseKey);
            if (!StringUtil.isEmpty(json)) {
                JSONObject jsonObject = JSON.parseObject(json);
                Boolean isLoginFromHeApp = jsonObject.getBoolean("loginFromHeApp");
                if (isLoginFromHeApp != null && isLoginFromHeApp) {
                    JedisClusterUtils.expires(loginBaseKey, JEDISTIMEOUT);
                    JedisClusterUtils.expires("loginUser" + session.getId(), JEDISTIMEOUT);
                    isHeAppLogin = Boolean.TRUE;
                }
            }
        }

        if (!isHeAppLogin) {
            //return true; //throw new Exception("您访问界面只能从和包APP进入。");
            response.sendRedirect("http://p.10086.cn/d");
            return true;
        } else {
            return true;
        }
    }
}
