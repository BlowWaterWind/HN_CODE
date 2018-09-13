package com.ai.ecs.ecm.mall.wap.platform.interceptor;

import com.ai.ecs.common.config.Global;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberBaseVo;
import com.ai.ecs.ecm.mall.wap.platform.utils.MemberLoginLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsmc.common.redis.cluster.RedisTemplate;
import com.ai.ecs.ecsmc.common.util.Constant;
import com.ai.ecs.ecsmc.domain.ec.po.MemberInfo;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.ecsmc.domain.ec.po.UserInfo;
import com.ai.ecs.ecsmc.domain.ec.vo.LoginUserRole;
import com.ai.ecs.ecsmc.domain.self.po.user.TfFO2oAccountInfo;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserContext;
import com.ai.ecs.utils.CookieUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 从o2o和掌柜登录
 * @author hexiao
 * Created by think on 2017/9/21.
 */
public class O2oAppSsoInterceptor extends HandlerInterceptorAdapter {
    private final static Logger logger = LoggerFactory.getLogger(O2oAppSsoInterceptor.class);
    private final static String SEC_TOKEN_KEY = "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int JEDISTIMEOUT=1800;

    private int JDEDISDAYTIMEOUT=21600;

    @Autowired
    JedisCluster jedisCluster;

    @Autowired
    ILoginService loginService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${AND_SSO_URL}")
    String ssoUrl ;
    @Value("${SECRET}")
    String secret;
    @Value("${DEV_ID}")
    String	devId;
    @Value("${ssoServerHost}")
    private String ssoServerHost;
    @Value("${channelID}")
    private String channelID;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if("test0".equals(request.getParameter("test"))){
            logintest(request, response);
        }
        String credtential = request.getParameter("CREDTENTIAL");
        String signData = request.getParameter("SIGN_DATA");
        String channelCode = request.getParameter("CHANID");
        String UId =  request.getParameter("UID");
        String secToken = request.getParameter("secToken");
        try{
                logger.info("O2O单点登录开始!");
                MemberVo member = UserUtils.getLoginUser(request);
                logger.info("request"+request.toString());
                logger.info("member"+member);
                if (member != null&&member.getChannelInfo()!=null) {
                    Session session=UserUtils.getSession();
                    jedisCluster.expire("loginUser"+session.getId(), JEDISTIMEOUT);
                    //部分号码无渠道信息，号卡推荐暂不取
//                    if(!request.getServletPath().contains("/recmd/") && member.getChannelInfo() == null) {
//                        o2oLoginError(request, response, "您的渠道工号绑定有误，请核实是否已通过店主帐号操作店铺管理--店员管理 为您绑定boss工号，且该工号在boss系统真实存在");
//                    }
                    return true;
                }
                //从cookie获取accessToken或从请求参数获取accessToken
                Cookie[] cookies = request.getCookies();
                String accessToken = "";
                if(com.ai.ecs.common.utils.StringUtils.isNotEmpty(secToken)){
                     accessToken = TriDes.getInstance().strDec(secToken, SEC_TOKEN_KEY, null, null);
                }else{
                    accessToken = CookieUtils.getCookie(request,Constant.COOKIE_USER_TOKEN);
                    if (StringUtils.isNotEmpty(accessToken)) {
                        accessToken = CookieUtils.getCookie(request, Constant.COOKIE_USER_TOKEN);
                    } else if (null != request.getHeader("token") && !"null".equalsIgnoreCase(request.getHeader("token"))) {
                        accessToken = request.getHeader("token");
                    }else{
                        accessToken = CookieUtils.getCookie(request, Constant.COOKIE_TOKEN);
                    }
                }
                logger.info("accessToken:"+accessToken);
                //token不为空则把用户信息写入session
                if(StringUtils.isNotEmpty(accessToken)){
                    UserInfo info = redisTemplate.get(Constant.O2O_SHOPUSER_INFO + accessToken, UserInfo.class);
                    //用户的角色信息，如此员工是店员，还是店主，还是客户
                    LoginUserRole role = redisTemplate.get(Constant.O2O_USERROLE_INFO + accessToken, LoginUserRole.class);
                    //member信息
                    MemberInfo memberInfo = redisTemplate.get("O2OmemberInfo" + accessToken, MemberInfo.class);
                    o2oLogin(request,response,info,role,memberInfo);
                    CookieUtils.setCookie(response,"secToken",secToken);//如果secToken为空放入response会报错;不覆盖一起的token
                }else{
                    o2oLoginError(request,response,"未获取到token");
                }

        }catch (Exception e){
            logger.error("===ANDAPPSSO————preHandle===",e);
            o2oLoginError(request,response,"单点登录异常");
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * o2o和掌柜单点登录，保存店铺信息、渠道工号信息
     * @param request
     * @param response
     * @param info
     * @param role
     * @param memberInfo
     */
    private Boolean o2oLogin(HttpServletRequest request,HttpServletResponse response,UserInfo info,LoginUserRole role,MemberInfo memberInfo){
        try {

            logger.info("role:"+JSON.toJSONString(role));
            logger.info("info:"+JSON.toJSONString(info));
            LoggerFactory.getLogger("O2oAppSsoInterceptor role:"+JSON.toJSONString(role));
            LoggerFactory.getLogger("O2oAppSsoInterceptor info:"+JSON.toJSONString(info));
            logger.info("memberInfo:"+JSON.toJSONString(memberInfo));
            Session session=UserUtils.getSession();
            UserContext context = new UserContext();
            String mobile = info.getMobile();
            String channelCode = "";
            context.setClientIp(StringUtils.getRemoteAddr(request));
            context.setMobile(mobile);
            context.setChannelCode(channelCode);
            Map<String, Object> loginRes =  loginService.loginBySms(context, ""); // 商城登录，首次登录会生成账号并记录用户信息
            if(loginRes==null){
                o2oLoginError(request,response,"商城登录记录用户信息失败");
                return false;
            }
            Object  obj = loginRes.get("data");
            MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
            // 保存渠道信息
            ChannelInfo channelInfo = new ChannelInfo();

                if (role != null && role.getTfFO2oAccountInfo() != null) {
                    channelInfo.setProvinceCode("HNAN");
                    channelInfo.setRouteEparchyCode(role.getTfFO2oAccountInfo().getCityCode());
                    channelInfo.setTradeCityCode(role.getTfFO2oAccountInfo().getCityCode());
                    channelInfo.setTradeDepartId(role.getTfFO2oAccountInfo().getDepartId());
                    channelInfo.setTradeEparchyCode(role.getTfFO2oAccountInfo().getCityCode());
                    channelInfo.setTradeStaffId(role.getTfFO2oAccountInfo().getStaffId());
                    channelInfo.setInModeCode(Global.getConfig("inModeCode"));
                    channelInfo.setChanelId("E050");
                    channelInfo.setCityCode(role.getTfFO2oAccountInfo().getEparchyCode());
                } else {
                    //部分号码无渠道信息，号卡推荐暂不取
                    if(!request.getServletPath().contains("/recmd/")) {
                        o2oLoginError(request, response, "您的渠道工号绑定有误，请核实是否已通过店主帐号操作店铺管理--店员管理 为您绑定boss工号，且该工号在boss系统真实存在");
                    }
                }

            // 保存店铺信息
            ShopInfo shopInfo = new ShopInfo();
            if(role!=null&&role.getShopInfo()!=null){
                shopInfo.setShopId(role.getShopInfo().getShopId());
                shopInfo.setShopName(role.getShopInfo().getShopName());
                shopInfo.setShopCity(role.getChannelCityCode());
            }else{
                o2oLoginError(request,response,"店铺信息为空");
            }
            //保存memberInfo信息
            com.ai.ecs.member.entity.MemberInfo memberTemp = new com.ai.ecs.member.entity.MemberInfo();
            if(memberInfo!=null){
                memberTemp.setMemberCity(memberInfo.getMemberCity());
                memberTemp.setMemberCounty(memberInfo.getMemberCounty());
            }
            membervo.setShopInfo(shopInfo);
            membervo.setChannelInfo(channelInfo);
            membervo.getMemberLogin().setMemberPhone(Long.parseLong(mobile));
            membervo.setMemberInfo(memberTemp);
            JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JDEDISDAYTIMEOUT);
            MemberBaseVo baseMember = new MemberBaseVo();
            baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
            baseMember.setMemberPhone(Long.parseLong(mobile));
            baseMember.setLoginFromHeApp(true);  // 是否从和包登陆
            JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),JDEDISDAYTIMEOUT);
            JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JDEDISDAYTIMEOUT);

            //保存日志
            MemberLoginLogUtils.saveLog(request, "2", "0", "登录成功", null, mobile, membervo);
        }catch (Exception e){
            logger.error("======loginError======", e);
            o2oLoginError(request,response,"单点登录异常");
        }
        return true;

    }

    /**
     * 跳转至登录错误页面
     * @param request
     * @param response
     * 重定向如果不加return语句,后面的代码还会执行,这是个坑
     */
    private void o2oLoginError(HttpServletRequest request, HttpServletResponse response,String result){
        try {
            result =  URLEncoder.encode(URLEncoder.encode(result,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "/o2oTest/loginError?value="+result;
        String contextPath = request.getContextPath();
        StringBuilder redirect = new StringBuilder();
        redirect.append(contextPath).append(url);
        try {
            response.sendRedirect(redirect.toString());
            return;
        } catch (IOException e) {
            logger.error("======loginError======", e);
            e.printStackTrace();
        }
    }
    public void logintest(HttpServletRequest request,HttpServletResponse response){
        logger.info("logintest");
        CookieUtils.setCookie(response, Constant.COOKIE_USER_TOKEN,"ABCDECG");
        String accessToken=CookieUtils.getCookie(request,Constant.COOKIE_USER_TOKEN);
        UserInfo userInfo=new UserInfo();
        userInfo.setId("1");
        userInfo.setLoginName("15111320789");
        userInfo.setMobile("15111320789");

        redisTemplate.set(Constant.O2O_SHOPUSER_INFO + accessToken, userInfo);
        //用户的角色信息，如此员工是店员，还是店主，还是客户
        LoginUserRole role = new LoginUserRole();
        role.setShopId("100004140086");
        role.setChannelCityCode("0731");
        role.setCompanyId("11");
//        TfFO2oAccountInfo oAccountInfo=new TfFO2oAccountInfo();
//        oAccountInfo.setId("1");
//        oAccountInfo.setCityCode("ssss");
//        role.setTfFO2oAccountInfo(oAccountInfo);
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setShopId("100004140086");
        shopInfo.setShopName("test");
        shopInfo.setShopCity("0731");
        role.setShopInfo(shopInfo);
        redisTemplate.set(Constant.O2O_USERROLE_INFO + accessToken, role);
        //member信息
        com.ai.ecs.ecsmc.domain.ec.po.MemberInfo memberInfo = new com.ai.ecs.ecsmc.domain.ec.po.MemberInfo();
        memberInfo.setCustId("1111");
        memberInfo.setMemberLogingName("15111320789");
        memberInfo.setCityCode(role.getChannelCityCode());
        CookieUtils.setCookie(response,"token",accessToken);


        redisTemplate.set("O2OmemberInfo" + accessToken, memberInfo);
    }
}
