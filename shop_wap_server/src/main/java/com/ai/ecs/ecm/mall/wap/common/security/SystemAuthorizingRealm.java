/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.ai.ecs.ecm.mall.wap.common.security;


import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.Encodes;
import com.ai.ecs.common.utils.SpringContextHolder;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.security.shiro.cache.JedisCacheManager;
import com.ai.ecs.ecm.mall.wap.platform.config.Global;
import com.ai.ecs.ecm.mall.wap.platform.service.SystemService;
import com.ai.ecs.ecm.mall.wap.platform.servlet.ValidateCodeServlet;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.entity.UserEntity;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统安全认证实现类
 * 
 * @author ThinkGem
 * @version 2014-7-5
 */
@Service
public class SystemAuthorizingRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SystemService systemService;
	@Autowired
	private JedisCacheManager jedisCacheManager;

	/*@Autowired
	private UserService userService;*/

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		// 是否校验登录验证码
		// if (LoginController.isValidateCodeLogin(token.getUsername(), false,
		// false)){
		Session session = UserUtils.getSession();
		String code = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
		// AALL用于压测
		if (!"AALL".equals(token.getCaptcha())) {
			if (!"4".equals(token.getCheckType())) {
				if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)) {
					logger.error("msg:验证码错误, 请重试.{},{}", code, token.getCaptcha());
					throw new AuthenticationException("msg:验证码错误, 请重试.");
				}
			}
		}
		// }
		UserEntity userEntityEntity = null;
		// 用户手机号登录 // 待调crm接口
		if (token.getUserType().equals("4")) {
			String phoneId = token.getPhoneId();
			String tokenPassword = "";
			// 服务密码登录
			if (token.getCheckType().equals("0")) {
				/*if (!userService.validateJXPhone(phoneId)) {
					throw new AuthenticationException("msg:请用江西移动号码登录！");
				}*/

				// 设置密码
				tokenPassword = SystemService.entryptPassword(token.getServicePwd());
				// crm 接口验证
				/*SVerifyUserPwdOut sVerifyUserPwdOut = userService.crmLogin(phoneId, token.getServicePwd());
				if (null == sVerifyUserPwdOut) {
					logger.error("用户登录：{},crm接口调用失败！！", phoneId);
					throw new AuthenticationException("msg:CRM系统异常，请稍候再试！");
				}*/
				String result = null;
				if (null == result) {
//					logger.error("登录验证失败:{}", sVerifyUserPwdOut);
					throw new AuthenticationException("msg:登录验证失败，请重新输入！");
				} else if (null != result && !result.equals("0")) { // 验证失败
					if (result.equals("-1") || result.equals("-2")) {
						Long loginNum = loginFail(phoneId);
						throw new AuthenticationException("msg:密码错误，请重新输入！密码输错" + (6 - loginNum) + "次之后将被锁定");
					} else if (result.equals("-3")) {
						throw new AuthenticationException("msg:在一天内办理所有业务输错密码次数超过规定次数！");
					} else if (result.equals("-4")) {
						throw new AuthenticationException("msg:密码正确但是密码是弱密码");
					} else {
						throw new AuthenticationException("msg:登录验证失败，请重试");
					}
				} else {
					// 服务密码验证通过，查询用户信息
					// 暂不查用户信息
					/*
					 * userEntity= userService.queryCrmUserInfo(phoneId);
					 * if(null==userEntity){ throw new
					 * AuthenticationException("msg:CRM系统异常，请稍候再试！"); }
					 */
//					userEntity = userService.initUserInfo(phoneId); // 暂未查crm接口
					userEntityEntity.setPassword(tokenPassword);
				}
			} else if (token.getCheckType().equals("2")) {
				/*if (!userService.validateJXPhone(phoneId)) {
					throw new AuthenticationException("msg:请用江西移动号码登录！");
				}*/
				// 短信密码 登录
				// 验证是否江西用户，只有江西用户才能用短信密码登录
				String sessionMobile = (String) session.getAttribute(UserUtils.MOBILE_CODE_SESSION_KEY);
				String sessionSmsCode = (String) session.getAttribute(UserUtils.SMS_CODE_SESSION_KEY);
				if (null == sessionMobile || !sessionMobile.equals(phoneId)) {
					throw new AuthenticationException("msg:发送的手机号码与验证的手机号码不一致！");
				} else if (!sessionSmsCode.equals(token.getSmsPwd())) {
					throw new AuthenticationException("msg:短信密码错误！");
				}
				tokenPassword = SystemService.entryptPassword(token.getSmsPwd());
				// 暂不查用户信息
				/*
				 * userEntity= userService.queryCrmUserInfo(phoneId);
				 * if(null==userEntity){ throw new
				 * AuthenticationException("msg:CRM系统异常，请稍候再试！"); }
				 */
//				userEntity = userService.initUserInfo(phoneId); // 暂未查crm接口
				userEntityEntity.setPassword(tokenPassword);

			} else if (token.getCheckType().equals("4")) {
				// 当从header获取到手机号时的登录,也应该是CRM的用户
				phoneId = token.getUsername();
				logger.debug("自动登录调用 begin-->{}", phoneId);
				tokenPassword = SystemService.entryptPassword(token.getCaptcha());// 与自动登录设置一样
//				userEntity = userService.initUserInfo(phoneId); // 暂未查crm接口
				if (null == userEntityEntity) {
					return null;
				}
				userEntityEntity.setPassword(tokenPassword);
				logger.debug("自动登录调用 end-->{}", phoneId);
			}

			// crm 登录成功后，注册到平台用户,返回平台注册用户信息
			try {
				UserEntity tempUserEntity = UserUtils.getCacheByPhoneId(userEntityEntity.getLoginName());
				if (null != tempUserEntity && StringUtils.isNotEmpty(tempUserEntity.getId())) {
					userEntityEntity = tempUserEntity;
				} else {
					/*User resultUser = userService.autoRegisterInfo(userEntity);
					userEntity.setId(resultUser.getId());
					userEntity.getUserInfo().setUserId(resultUser.getId());*/
				}

				/*userEntity.setPassword(tokenPassword);// shiro登录验证密码
				UserUtils.putUserEntityCache(userEntity); // 放入缓存*/

			} catch (Exception e) {
				logger.error("保存crm注册用户信息出错：[{}]", userEntityEntity);
				e.printStackTrace();
			}
			logger.debug("crm 用户登录token:{}-->userEntity:{}", token, userEntityEntity);

			if (userEntityEntity != null) {
				byte[] salt = Encodes.decodeHex(userEntityEntity.getPassword().substring(0, 16));
				return new SimpleAuthenticationInfo(new Principal(userEntityEntity, token.isMobileLogin()),
						userEntityEntity.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
			} else {
				return null;
			}
		} else {
			// 注册帐号登录
			userEntityEntity = getSystemService().getUserByLoginName("1", token.getUsername());
			logger.debug("注册用户登录token:{}-->userEntity:{}", token, userEntityEntity);
			// 校验用户名密码
			if (userEntityEntity != null) {
				if (Global.NO.equals(userEntityEntity.getLoginFlag())) {
					throw new AuthenticationException("msg:该已帐号禁止登录.");
				}
				UserUtils.putUserCache(userEntityEntity);
				byte[] salt = Encodes.decodeHex(userEntityEntity.getPassword().substring(0, 16));
				return new SimpleAuthenticationInfo(new Principal(userEntityEntity, token.isMobileLogin()),
						userEntityEntity.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
			} else {
				return null;
			}
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
			// Collection<Session> sessions =
			// getSystemService().getSessionDao().getActiveSessions(true,
			// principal, UserUtils.getSession());
			// if (sessions.size() > 0){
			// // 如果是登录进来的，则踢出已在线用户
			// if (UserUtils.getSubject().isAuthenticated()){
			// for (Session session : sessions){
			// getSystemService().getSessionDao().delete(session);
			// }
			// }
			// // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
			// else{
			// UserUtils.getSubject().logout();
			// throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
			// }
			// }
		}
		UserEntity userEntity = getSystemService().getUserByLoginName(principal.getUserType(), principal.getMobile());
		if (userEntity != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 记录登录日志
			// LogUtils.saveLog(Servlets.getRequest(), "系统登录");
			return info;
		} else {
			return null;
		}
	}

	@Override
	protected void checkPermission(Permission permission, AuthorizationInfo info) {
		authorizationValidate(permission);
		super.checkPermission(permission, info);
	}

	@Override
	protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
			for (Permission permission : permissions) {
				authorizationValidate(permission);
			}
		}
		return super.isPermitted(permissions, info);
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		authorizationValidate(permission);
		return super.isPermitted(principals, permission);
	}

	@Override
	protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
			for (Permission permission : permissions) {
				authorizationValidate(permission);
			}
		}
		return super.isPermittedAll(permissions, info);
	}

	/**
	 * 授权验证方法
	 * 
	 * @param permission
	 */
	private void authorizationValidate(Permission permission) {
		// 模块授权预留接口
	}

	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	// /**
	// * 清空用户关联权限认证，待下次使用时重新加载
	// */
	// public void clearCachedAuthorizationInfo(Principal principal) {
	// SimplePrincipalCollection principals = new
	// SimplePrincipalCollection(principal, getName());
	// clearCachedAuthorizationInfo(principals);
	// }

	/**
	 * 清空所有关联认证
	 * 
	 * @Deprecated 不需要清空，授权缓存保存到session中
	 */
	@Deprecated
	public void clearAllCachedAuthorizationInfo() {
		// Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		// if (cache != null) {
		// for (Object key : cache.keys()) {
		// cache.remove(key);
		// }
		// }
	}

	/**
	 * 获取系统业务对象
	 */
	public SystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}

	/**
	 * 判断登陆次数
	 * 
	 * @param loginNum
	 */

	public Long loginFail(String phoneId) {
		JedisCacheManager.JedisCache jedisCache = (JedisCacheManager.JedisCache) jedisCacheManager
				.getCache(UserUtils.USER_CACHE_LOGINFAIL);
		Map<String, Map<String, Long>> loginFailMap = (Map<String, Map<String, Long>>) jedisCache.get("loginFailMap");

		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
		}
		Map<String, Long> loginFail = loginFailMap.get(phoneId);
		Long loginFailNum = null;
		Long loginFailNumTime = null;
		if (loginFail != null) {
			loginFailNum = loginFail.get(phoneId);
			loginFailNumTime = loginFail.get(phoneId + "_time");
		}
		if (null == loginFail) {
			loginFail = Maps.newHashMap();
		} else {
			//格式化时间的工具类
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//时间差，天数之差
			double distanceOfTwoDate = 0;
			try {
				//将毫秒数转换为时间类型
				Date parse = sdf.parse(sdf.format(loginFailNumTime));
				//计算天数之差方法
				distanceOfTwoDate = DateUtils.getDistanceOfTwoDate(parse, new Date());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//当0点之后清0，错误计数器清零
			if (distanceOfTwoDate > 0) {
				cleanValidateCodeLogin(phoneId);
				loginFailNum = 0l;
			}
		}

		if (null == loginFailNum) {
			loginFailNum = 0l;
		}

		loginFail.put(phoneId, ++loginFailNum);
		loginFail.put(phoneId + "_time", System.currentTimeMillis());

		loginFailMap.put(phoneId, loginFail);

		jedisCache.put("loginFailMap", loginFailMap);

		return loginFailNum;
	}

	public void cleanValidateCodeLogin(String phoneId) {
		JedisCacheManager.JedisCache jedisCache = (JedisCacheManager.JedisCache) jedisCacheManager
				.getCache(UserUtils.USER_CACHE_LOGINFAIL);
		Map<String, Map<String, Long>> loginFailMap = (Map<String, Map<String, Long>>) jedisCache.get("loginFailMap");
		if (loginFailMap == null) {
			return;
		}
		loginFailMap.remove(phoneId);
		jedisCache.put("loginFailMap", loginFailMap);
	}

	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;

		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名
		private boolean mobileLogin; // 是否手机登录
		private String mobile; // 手机号码
		private String userType;// 用户类型

		// private Map<String, Object> cacheMap;

		public Principal(UserEntity userEntity, boolean mobileLogin) {
			this.id = userEntity.getId();
			this.loginName = userEntity.getLoginName();
			this.name = userEntity.getName();
			this.mobileLogin = mobileLogin;
			this.mobile = userEntity.getMobile();
			this.userType = userEntity.getUserType();
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public boolean isMobileLogin() {
			return mobileLogin;
		}

		// @JsonIgnore
		// public Map<String, Object> getCacheMap() {
		// if (cacheMap==null){
		// cacheMap = new HashMap<String, Object>();
		// }
		// return cacheMap;
		// }

		/**
		 * 获取SESSIONID
		 */
		public String getSessionid() {
			try {
				return (String) UserUtils.getSession().getId();
			} catch (Exception e) {
				return "";
			}
		}

		public String getMobile() {
			return mobile;
		}

		@Override
		public String toString() {
			return id;
		}

		public String getUserType() {
			return userType;
		}
	}
	//
	// public static void main(String[] args) {
	// double distanceOfTwoDate = DateUtils.getDistanceOfTwoDate(
	// new SimpleDateFormat().format(System.currentTimeMillis()), new
	// Date());
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// try {
	// Date parse = sdf.parse(sdf.format(System.currentTimeMillis()));
	// System.out.println(parse);
	// double d = DateUtils.getDistanceOfTwoDate(parse, new Date());
	// System.out.println(d);
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
