package com.ai.ecs.ecm.mall.wap.platform.interceptor;

import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.entity.MemberVo;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2016-11-16.
 */
public class GroupRequestIntercepter extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(GroupRequestIntercepter.class);
    private int JEDISTIMEOUT=1800;

    @Autowired
    JedisCluster jedisCluster;

    @Value("${channelID}")
    private String channelID;

    @Value("${ssoCheckUrl}")
    private String ssoCheckUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String backUrl = request.getParameter("backUrl");
        String url = request.getRequestURL().toString();
        if(backUrl != null && !url.toLowerCase().startsWith(backUrl.toLowerCase())) {
            // 如果用户已登陆
            MemberVo member = UserUtils.getLoginUser(request);
            if (member != null) {
                Session session = UserUtils.getSession();
                jedisCluster.expire("loginUser" + session.getId(), JEDISTIMEOUT);
                response.sendRedirect(backUrl);
                return true;
                // 用户没有登陆
            } else {
                // 获取集团登陆信息
                response.sendRedirect(ssoCheckUrl + "?channelID=" + channelID +"&backUrl="+ backUrl);
                return true;
            }
        }
        return false;
    }
}
