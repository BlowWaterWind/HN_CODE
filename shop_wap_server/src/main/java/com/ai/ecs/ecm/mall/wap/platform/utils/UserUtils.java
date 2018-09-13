/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.ai.ecs.ecm.mall.wap.platform.utils;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.common.utils.SpringContextHolder;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.security.SystemAuthorizingRealm.Principal;
import com.ai.ecs.ecm.mall.wap.common.security.shiro.cache.JedisCacheManager;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberBaseVo;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.ecm.mall.wap.platform.config.Global;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.constant.CacheConstants;
import com.ai.ecs.member.entity.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户工具类
 */
public class UserUtils {
	// private static IUserSerice userSerice =
	// SpringContextHolder.getBean(IUserSerice.class);
	// private static UserService userService =
	// SpringContextHolder.getBean(UserService.class);
	public static final String LOCATION_CACHE = "locationCache";
	
	private static ILoginService loginService = SpringContextHolder.getBean(ILoginService.class);

	// public static final String USER_CACHE_ID_ = "id_";
	// public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static String SMS_CODE_SESSION_KEY = "smsCodeKey"; // 短信验证码

	public static String SMS_CODE_SESSION_TIME_KEY = "smsCodeTimeKey";

	public static String MOBILE_CODE_SESSION_KEY = "mobileNumber";

	public static String LOCATION_LATITUDE = "hui_latitude"; // 纬度

	public static String LOCATION_LONGITUDE = "hui_longitude"; // 经度

	public static String USER_FIRST_SHOP = "USER_FIRST_SHOP"; // 首选店铺

	private static int JEDISTIMEOUT = 1800;

	public static String USER_FIRST_SHOP_1 = "1"; // 已进入过首选店铺

	public static final String USER_CACHE_LOGINFAIL = "HUI_USERCACHE_LOGINFAIL";

	private static Long smsCodeTimeout = Long.valueOf(Global
			.getConfig("smsCodeTimeout")); // 验证码过期时间

	private static Long smsCodeSendTime = Long.valueOf(Global
			.getConfig("smsCodeSendTime"));// 短信发送间隔时间
	private final static Logger logger = LoggerFactory
			.getLogger(UserUtils.class);

	public static String sessionKeyPrefix = Global.getConfig("redis.keyPrefix")
			+ "_session_";

	private static JedisCacheManager jedisCacheManager = SpringContextHolder
			.getBean(JedisCacheManager.class);

	private static JedisCacheManager.JedisCache jedisCache = (JedisCacheManager.JedisCache) jedisCacheManager
			.getCache(CacheConstants.USER_CACHE);

	private static JedisCacheManager.JedisCache jedisCacheLocal = (JedisCacheManager.JedisCache) jedisCacheManager
			.getCache(LOCATION_CACHE);

	public static String wantingServerHost = new PropertiesLoader(
			"mall.properties").getProperty("wantingServerHost");

	private static String ssoServerHost = new PropertiesLoader(
			"mall.properties").getProperty("ssoServerHost");

	private static String channelID = new PropertiesLoader(
			"mall.properties").getProperty("channelID");

	/**
	 * 根据ID获取用户
	 * 
	 * @param id
	 * @return 取不到返回null
	 */
	public static UserEntity get(String id) {
		UserEntity userEntity;
		// user = (User) JedisClusterUtils.getObject(USER_CACHE + USER_CACHE_ID_
		// + id);
		userEntity = (UserEntity) jedisCache.get(CacheConstants.USER_CACHE_ID_
				+ id);

		return userEntity;
	}
	
	
	/**
	 * 更新会员信息缓存
	 * @param memberVo
	 * @return
	 */
	public static MemberVo updateMemberVoCache(MemberVo memberVo){
		Session session=getSession();
		JedisClusterUtils.setObject("loginUser" + session.getId(),
				memberVo, JEDISTIMEOUT);
		return memberVo;

	}

	/**
	 * 是否是手机号登录
	 * 
	 * @return
	 */
	public static boolean isMobileLogin(HttpServletRequest request) {
		MemberVo member = UserUtils.getLoginUser(request
				);
		if (member != null) {
			MemberLogin memberLogin = member.getMemberLogin();
			if (memberLogin.getMemberTypeId() == 1) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据登录名获取用户，手机号码字段
	 * 
	 * @param userType
	 *            用户类型
	 * @param loginName
	 *            手机号码
	 * @return 取不到返回null
	 */
	public static UserEntity getByLoginName(String userType, String loginName) {
		UserEntity userEntity = (UserEntity) jedisCache
				.get(CacheConstants.USER_CACHE_LOGIN_NAME_ + userType + "_"
						+ loginName);
		if (userEntity == null) {
			try {
				userEntity = new UserEntity();
				userEntity.setLoginName(loginName);
				userEntity.setUserType("1");
				// user = userSerice.getByLoginName(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (userEntity == null) {
				return null;
			}
			jedisCache.put(CacheConstants.USER_CACHE_ID_ + userEntity.getId(),
					userEntity);
			jedisCache.put(CacheConstants.USER_CACHE_LOGIN_NAME_ + userType
					+ "_" + userEntity.getLoginName(), userEntity);
			JedisClusterUtils.setObject(CacheConstants.USER_CACHE_LOGIN_NAME_
					+ userType + "_" + userEntity.getLoginName(), userEntity,
					3600 * 24 * 7);
		}
		return userEntity;
	}


	@SuppressWarnings("static-access")
	public static MemberSsoVo getGroupSsoVo(HttpServletRequest request, HttpServletResponse response, String uId) {
		MemberSsoVo user = null;
		try {
			String getInfoUrl = ssoServerHost + "ecsuc/remote/userServer/activateUserSession";
			Map<String, String[]> paramMap = new HashMap<String, String[]>();
			paramMap.put("uId", new String[] { uId });
			paramMap.put("channelId", new String[] { channelID });
			logger.error("activateUserSession: " + paramMap);
			String strres = HttpClientUtils.doPostAndGetString(getInfoUrl, paramMap);
			logger.error("strres: " + strres);

			if (strres  != null) {
				JSONObject obj = JSONObject.parseObject(strres);
				String code = obj.getString("code");
				// 状态为200
				if("200".equals(code)) {
					String loginUrl = ssoServerHost + "ecsuc/remote/userServer/userLogin";
					paramMap = new HashMap<String, String[]>();

					paramMap.put("username", new String[] { uId });
					paramMap.put("channel", new String[] { channelID });
					paramMap.put("isSMS", new String[] { "01" });
					paramMap.put("password", new String[] { uId });
					paramMap.put("isImg", new String[] { "02" });
					paramMap.put("regType", new String[] { "01" });
					paramMap.put("loginType", new String[] { "04" });
					paramMap.put("onlyOne", new String[]{"02"}); //01:只允许一个终端登录02：允许多个终端同时登录（默认）
					paramMap.put("clientIp", new String[] { request.getRemoteHost() });

					logger.error("userLogin params:" + paramMap);
					strres = HttpClientUtils.doPostAndGetString(loginUrl, paramMap);
					logger.error("userLogin strres:" + strres);

					JSONObject jsonRes = JSONObject.parseObject(strres);
					if (jsonRes != null && jsonRes.getJSONObject("simplePrincipal") != null) {
						String tkey = jsonRes.getString("t");
						JSONObject artifactObj = jsonRes.getJSONObject("simplePrincipal").getJSONObject("attributes");
						String mobile = artifactObj.getString("userName");

						Session session = UserUtils.getSession();
						UserContext context = new UserContext();
						context.setClientIp(request.getRemoteAddr());
						context.setChannelCode("E007");
						if (StringUtils.isNotEmpty(tkey)) {
							context.setSessionId(tkey);

							// 查询crm用户信息
							user = UserUtils.getSsoLoginUser(request, tkey);

							Map<String, Object> loginRes = null;// 商城登录，首次登录会生成账号并记录用户信息
							loginRes = loginService.loginByMobile(context, mobile, "123456");
							CookieUtils.setCookie(response, "ticketId", tkey, JEDISTIMEOUT);
							CookieUtils.setCookie(response, "uId", artifactObj.getString("uid"), JEDISTIMEOUT);
							Object data = loginRes.get("data");
							MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(data), MemberVo.class);
							JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);

							JedisClusterUtils.del("specialRef" + session.getId());


							MemberLoginLogUtils.saveLog(request, "0", "0", "登录成功", tkey, mobile, membervo);
							JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
							MemberBaseVo baseMember = new MemberBaseVo();
							baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
							baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
							baseMember.setEparchCode(user.getRemoveEparchyCode());
							JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember), JEDISTIMEOUT);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("登陆出错", e);
			user = null;
		}
		return user;
	}




    /**
     * 获取会员登录信息VO
     * @return MemberVo
     * 
     * @Value("${ssoServerHost ") private static String ssoServerHost;
     */
    @SuppressWarnings("static-access")
	public static MemberVo getLoginUser(HttpServletRequest request)
    {
        String getInfoUrl = ssoServerHost + "ecsuc/remote/userServer/fetchUserInfo";
        Session session = UserUtils.getSession();
        String tKey = CookieUtils.getCookie(request,"ticketId");//为空
        String sessionId=(String)  JedisClusterUtils.get("sessionId"+session.getId());
        MemberVo member=null;
        if(StringUtils.isNotEmpty(sessionId)&&StringUtils.isEmpty(tKey)){
            member =(MemberVo) JedisClusterUtils.getObject("loginUser"+session.getId());
            if (member != null)
            {
                MemberLogin memberLogin=member.getMemberLogin();
                MemberInfo memberInfo=member.getMemberInfo();
                if(memberLogin==null){
                    memberLogin=new MemberLogin();
                }
                if(memberInfo==null){
                    memberInfo=new MemberInfo();
                }
                if(memberInfo.getMemberNickname()==null){
                    memberInfo.setMemberNickname(memberLogin.getMemberLogingName());
                }
                member.setMemberLogin(memberLogin);
                member.setMemberInfo(memberInfo);
            }
            return member; 
        }
			if(tKey==null){
				return null;
			}
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        paramMap.put("ticketId", new String[]
        { tKey });
        paramMap.put("channelID", new String[]
        { channelID });

        String strres = "";
        try
        {
			strres= (String) session.getAttribute("sso_strres");
			if(StringUtils.isEmpty(strres)) {
				logger.info("HttpClientUtils.doPostAndGetString:{}", tKey);
				strres = HttpClientUtils.doPostAndGetString(getInfoUrl, paramMap);
				logger.info("HttpClientUtils.doPostAndGetString:{}", strres);
			}
            JSONObject obj=JSONObject.parseObject(strres);
             String code=obj.getString("code");
            String msg=obj.getString("msg");
            if(msg!=null&&!"200".equals(code)){
            	throw new Exception(msg);
            }

            String tInfo=obj.getString("t");
            MemberSsoVo jsonRes = JSONObject.parseObject(tInfo,MemberSsoVo.class);
            if (jsonRes != null)
            {
				session.setAttribute("sso_strres",strres);
            	JedisClusterUtils.setObject(tKey+"WEB_USERINFO", jsonRes,JEDISTIMEOUT);
            	member = (MemberVo) JedisClusterUtils.getObject("loginUser"+session.getId());
            	if(member==null){
            		member=createSsoUser(jsonRes,request);
            	}
            }
        }
        catch (Exception e)
        {
        	logger.error("单点登录服务异常：",e);
        }
        return member;
    }
    
    public static MemberVo createSsoUser(MemberSsoVo jsonRes ,HttpServletRequest request){
    		UserContext context = new UserContext();
    		context.setClientIp(request.getRemoteAddr());
    		Map<String, Object> loginRes;
			try {
				loginRes = loginService.loginByMobile(
						context, jsonRes.getMemberPhone(), "111");
			// 商城登录，首次登录会生成账号并记录用户信息
    		if (loginRes != null) {
				if (loginRes.get("msg") != null) {
					return null;
				}
				Object obj = loginRes.get("data");
				MemberVo membervo = JSONObject.parseObject(
						JSONObject.toJSONString(obj), MemberVo.class);
				Session session=getSession();
				JedisClusterUtils.setObject("loginUser" + session.getId(),
						membervo, JEDISTIMEOUT);
				MemberSsoVo user = getSsoLoginUser(request,null);
				if(user!=null){
				return membervo;
				}
		}
			} catch (Exception e) {
				logger.error("单点登录新增用户失败：" ,e);
			}
			return null;

    }
    
    public static MemberSsoVo getSsoLoginUser(HttpServletRequest request,String tKey) {
		try {
			String getInfoUrl = ssoServerHost + "ecsuc/remote/userServer/fetchUserInfo";
			Session session = UserUtils.getSession();
			if(StringUtils.isEmpty(tKey)){
			  	tKey = CookieUtils.getCookie(request,"ticketId");

				if (StringUtils.isEmpty(tKey)) {
					return null;
				}
			}

			MemberSsoVo jsonRes = (MemberSsoVo) JedisClusterUtils.getObject(tKey + "WEB_USERINFO");
			if (jsonRes == null) {
				//logger.info("getSsoLoginUser:{}",tKey);
				Map<String, String[]> paramMap = new HashMap<String, String[]>();
				paramMap.put("ticketId", new String[] { tKey });
				paramMap.put("channelID", new String[] { channelID });

				String strres = "";
				logger.error("getSsoLoginUser:" + paramMap);
				strres = HttpClientUtils.doPostAndGetString(getInfoUrl, paramMap);
				logger.error("getSsoLoginUser strres:" + strres);
				JSONObject obj = JSONObject.parseObject(strres);
				String code = obj.getString("code");
				String msg = obj.getString("msg");
				if ("-1".equals(code)) {
					throw new Exception(msg);
				}
				String tInfo = obj.getString("t");
				jsonRes = JSONObject.parseObject(tInfo, MemberSsoVo.class);
			}
			if(null!=jsonRes) {
				JedisClusterUtils.setObject(tKey + "WEB_USERINFO", jsonRes, 3600);
			}
			return jsonRes;
		} catch (Exception e) {
			logger.error("单点登录服务异常：", e);
		}
		return null;
	}


	/**
	 * 从缓存中取用户信息，只能用户免登录时用,CRM用户
	 * 
	 * @param loginName
	 * @return
	 */
	public static UserEntity getCacheByPhoneId(String loginName) {
		// User user =
		// (User)jedisCache.get(CacheConstants.USER_CACHE_LOGIN_NAME_ + 4 + "_"
		// + loginName);
		UserEntity userEntity = (UserEntity) JedisClusterUtils
				.getObject(CacheConstants.USER_CACHE_LOGIN_NAME_ + 4 + "_"
						+ loginName);
		return userEntity;
	}

	/**
	 * 根据登录名获取用户，手机号码字段
	 * 
	 * @return 取不到返回null
	 */
	public static void putUserCache(UserEntity userEntity) {
		jedisCache.put(CacheConstants.USER_CACHE_ID_ + userEntity.getId(),
				userEntity);
		jedisCache.put(
				CacheConstants.USER_CACHE_LOGIN_NAME_
						+ userEntity.getUserType() + "_"
						+ userEntity.getLoginName(), userEntity);
		JedisClusterUtils.setObject(CacheConstants.USER_CACHE_LOGIN_NAME_
				+ userEntity.getUserType() + "_" + userEntity.getLoginName(),
				userEntity, 3600 * 24 * 3);

		userCacheJson(userEntity);

	}

	private static void userCacheJson(UserEntity userEntityEntity) {
		if (StringUtils.isNotEmpty(userEntityEntity.getId())) {
			Map<String, String> userMap = new HashMap<>();
			userMap.put("userId", userEntityEntity.getId());
			userMap.put("phoneId", userEntityEntity.getLoginName());
			JedisClusterUtils.set(sessionKeyPrefix + getSession().getId()
					+ "_json", JSON.toJSONString(userMap), 3600 * 24 * 2);
		}
	}

	/**
	 * 清除指定用户缓存
	 * 
	 * @param userEntity
	 */
	public static void clearCache(UserEntity userEntity) {
		if (userEntity.getId() != null) {
			jedisCache.remove(CacheConstants.USER_CACHE_ID_
					+ userEntity.getId());
			jedisCache.remove(CacheConstants.USER_CACHE_LOGIN_NAME_
					+ userEntity.getUserType() + "_"
					+ userEntity.getLoginName());
			jedisCache.remove(CacheConstants.USER_CACHE_LOGIN_NAME_
					+ userEntity.getOldLoginName());
			JedisClusterUtils.del(sessionKeyPrefix + getSession().getId()
					+ "_json");
		}
	}

	/**
	 * 获取当前用户
	 * 
	 * @return 取不到返回 new User()
	 */
	public static UserEntity getUser(String flag) {
		try {
			Principal principal = getPrincipal();
			if (principal != null) {
				UserEntity userEntity = get(principal.getId());
				if (userEntity != null) {
					return userEntity;
				}
				return new UserEntity();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new UserEntity();
	}

	/**
	 * 获取当前用户
	 * 
	 * @return 取不到返回 new User()
	 */
	public static UserEntity getUser() {
		try {
			Principal principal = getPrincipal();
			if (principal != null) {
				UserEntity userEntity = get(principal.getId());
				if (userEntity != null) {
					// 当免登录时，未获取到用户信息时,重新调用crm接口调用
					/*
					 * if ("4".equals(user.getUserType()) &&
					 * "".equals(user.getCustId()) && !user.isLogin()) {
					 * user.setIsLogin(true);
					 * UserUtils.putUserEntityCache(user); new
					 * UpdateUser(user).start(); }
					 */
					return userEntity;
				}
				return new UserEntity();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new UserEntity();
	}

	/**
	 * 用户注销
	 */
	public static void logout() {
		// 删除用户缓存
		UserUtils.clearCache(UserUtils.getUser());
		getSubject().logout();
	}

	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal() {
		try {

			Subject subject = SecurityUtils.getSubject();
			// subject.logout();
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return principal;
			}

		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		return null;
	}

	public static Session getSession() {
		try {
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null) {
				session = subject.getSession();
			}
			if (session != null) {
				return session;
			}
			// subject.logout();
		} catch (InvalidSessionException e) {

		}
		return null;
	}

	// ============== User Cache ==============

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		// Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		// getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		// getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}

	// public static Map<String, Object> getCacheMap(){
	// Principal principal = getPrincipal();
	// if(principal!=null){
	// return principal.getCacheMap();
	// }
	// return new HashMap<String, Object>();
	// }

	/**
	 * 短信验证码放session
	 * 
	 * @param sms
	 * @throws Exception
	 */
	public static void setSMSCodeKey(String sms) {
		getSession().setAttribute(SMS_CODE_SESSION_TIME_KEY,
				System.currentTimeMillis());
		getSession().setAttribute(SMS_CODE_SESSION_KEY, sms);
	}

	/**
	 * 短信验证码号码
	 * 
	 * @param mobile
	 */
	public static void setMobileCodeKey(String mobile) {
		getSession().setAttribute(MOBILE_CODE_SESSION_KEY, mobile);
	}

	// ----------------------------------发短信验证，存手机号码
	public static String getMobileCodeKey(HttpServletRequest request) {
		return (String) getSession().getAttribute(MOBILE_CODE_SESSION_KEY);

	}

	/**
	 * 取session短信验证码
	 * 
	 * @param request
	 * @return
	 */
	public static String getSMSCodeKey(HttpServletRequest request) {
		Long inputTime = (Long) getSession().getAttribute(
				SMS_CODE_SESSION_TIME_KEY);
		if (inputTime != null && smsCodeTimeout != null) { // 当能获取放入验证时间与配制时间时，
															// 才做验证
			Long now = System.currentTimeMillis();
			if (now - inputTime > smsCodeTimeout) { // 验证过期， 清除session中的值
				getSession().setAttribute(SMS_CODE_SESSION_KEY, null);
				getSession().setAttribute(SMS_CODE_SESSION_TIME_KEY, null);
			}
		}
		return (String) getSession().getAttribute(SMS_CODE_SESSION_KEY);
	}

	/**
	 * 清空验码
	 * 
	 * @param request
	 */
	public static void clearSmsCode(HttpServletRequest request) {
		getSession().setAttribute(SMS_CODE_SESSION_KEY, null);
		getSession().setAttribute(SMS_CODE_SESSION_TIME_KEY, null);
	}

	/**
	 * 验证是否重复发送
	 * 
	 * @param request
	 * @return true 是， false 否
	 */
	public static boolean IsSendSMSCode(HttpServletRequest request) {
		Long inputTime = (Long) getSession().getAttribute(
				SMS_CODE_SESSION_TIME_KEY);
		if (inputTime != null && smsCodeSendTime != null) { // 当能获取放入验证时间与配制时间时，
															// 才做验证
			Long now = System.currentTimeMillis();
			if (now - inputTime > smsCodeSendTime) { // 是否在设置时间内
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取纬度
	 * 
	 * @param request
	 * @return
	 */
	public static String getLatitude(HttpServletRequest request) {
		String latitude = request.getParameter("v_latitude");
		if (StringUtils.isEmpty(latitude)) {
			latitude = CookieUtils.getCookie(request,
					UserUtils.LOCATION_LATITUDE);
		}
		if (StringUtils.isEmpty(latitude)) {
			latitude = (String) UserUtils.getCache(UserUtils.getSession()
					.getId() + "_" + UserUtils.LOCATION_LATITUDE);
		}
		return latitude;
	}

	/**
	 * 获取纬度
	 * 
	 * @param request
	 * @return
	 */
	public static String getLatitudeByShop(HttpServletRequest request) {
		String latitude = request.getParameter("v_latitude");
		if (StringUtils.isEmpty(latitude)) {
			latitude = CookieUtils.getCookie(request,
					UserUtils.LOCATION_LATITUDE);
		}
		if (StringUtils.isEmpty(latitude)) {
			latitude = (String) UserUtils.getCache(UserUtils.getSession()
					.getId() + "_" + UserUtils.LOCATION_LATITUDE);
		}
		return latitude;
	}

	/**
	 * 获取经度
	 * 
	 * @param request
	 * @return
	 */
	public static String getLongitude(HttpServletRequest request) {
		String longitude = request.getParameter("v_longitude");
		if (StringUtils.isEmpty(longitude)) {
			longitude = CookieUtils.getCookie(request,
					UserUtils.LOCATION_LONGITUDE);
		}
		if (StringUtils.isEmpty(longitude)) {
			longitude = (String) UserUtils.getCache(UserUtils.getSession()
					.getId() + "_" + UserUtils.LOCATION_LONGITUDE);
		}

		if (StringUtils.isEmpty(longitude)) {
			longitude = (String) jedisCacheLocal.get(UserUtils.getSession()
					.getId() + "_" + UserUtils.LOCATION_LONGITUDE);
			if (StringUtils.isNotEmpty(longitude)) {
				jedisCacheLocal.remove(UserUtils.getSession().getId() + "_"
						+ UserUtils.LOCATION_LONGITUDE);
			}
		}
		return longitude;
	}

	public static void putLocalCache(String key, String value) {
		jedisCacheLocal.put(key, value);
	}
}
