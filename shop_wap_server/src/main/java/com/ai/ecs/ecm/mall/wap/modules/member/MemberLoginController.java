package com.ai.ecs.ecm.mall.wap.modules.member;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.MD5;
import com.ai.ecs.common.utils.SpringContextHolder;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberBaseVo;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.ecsite.modules.login.service.LoginService;
import com.ai.ecs.ecsite.modules.login.service.entity.UserInfoGetCondition;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.constant.MemberStatusConstant;
import com.ai.ecs.member.constant.MemberTypeConstant;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.createStreamNo;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.processThrowableMessage;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogConditionAndReturn;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogEnterMenthod;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogExitMenthod;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.writerFlowLogThrowable;

@Controller
@RequestMapping("login")
public class MemberLoginController extends BaseController {

	@Autowired
	ILoginService loginService;

	@Autowired
	IMemberLoginService memberLoginService;

	@Value("${smsLight}")
	private String smsLight;

	@Value("${ssoServerHost}")
	private String ssoServerHost;

	@Value("${channelID}")
	private String channelID;

	@Value("${goToGroupCookieUrl}")
	private String goToGroupCookieUrl;
	@Value("${environment}")
	private String environment;
	@Autowired
	private LoginService loginService2;

	private static final Logger logger = LoggerFactory.getLogger(MemberLoginController.class);
	private static JedisCluster jedisCluster = SpringContextHolder
			.getBean(JedisCluster.class);
	private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
		+ "wxyz0123456789+/" + "=";

	private int JEDISTIMEOUT = 1800;

	private int SECONDSSMSOUT = 55;

	private int LOGINJEDISTIMEOUT = 1800;

	private int SMSOUT = 60 * 60 * 24;

	private int LOGINLOCKJEDISTIMEOUT = 60 * 60 * 24;

	private int CAPTCHAJEDISTIMEOUT = 1800;

	private static char[] RAND_CODES = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private String MALL_VCODE="wapMallVCode";
	private String MOBILE="mob";
	private String BRAND="brand";
	private String SEND_SMS_CODE_VCODE_KEY="sendSmsCodeVCodeKey:";
	private String WAP_MALL_AI="wapMallAI_=:";
	private String MEMBER_LOGIN_SEND_SMS_COUNT="MemberLoginSendSmsCount:";
	private String MEMBER_LOGIN_SECONDS_SMS_COUNT_STR="MemberLoginSecondsSmsCountStr:";
	@Autowired
	private SmsSendService smsSendService;

	/**
	 * 跳转到登录页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toLogin", method = RequestMethod.GET)
	@RefreshCSRFToken
	public String login(HttpServletRequest request, HttpServletResponse response, Model model,
		String mobileFlag, String specialRef) {

	/*	// 查看session中的跳转地址为不为空， 不空时保存地址信息
		Session oldSession = UserUtils.getSession();
		String redirectUrlOldValue = null;
		if (oldSession != null) {
			String oldKey = "redirectUrl" + oldSession.getId();
			redirectUrlOldValue = JedisClusterUtils.get(oldKey);
			if (!StringUtils.isBlank(redirectUrlOldValue)) {
				JedisClusterUtils.del(oldKey);
			}
		}
*/
		// 删除了session
		//UserUtils.getSubject().logout();


		Session session = UserUtils.getSession();
		if (StringUtils.isNotEmpty(mobileFlag)) {
			request.setAttribute("mobileFlag", mobileFlag);
		}
		if (StringUtils.isNotEmpty(specialRef)) {
			JedisClusterUtils.set("specialRef" + session.getId(), specialRef, JEDISTIMEOUT);
		}

		// 保存新地址到redis中
		/*if (!StringUtils.isBlank(redirectUrlOldValue)) {
			JedisClusterUtils.set("redirectUrl" + session.getId(), redirectUrlOldValue, JEDISTIMEOUT);
		}*/

		return "web/member/loginForm";
	}

	@RequestMapping(value = "/toLogin2", method = RequestMethod.GET)
	@RefreshCSRFToken
	public String login2(HttpServletRequest request, HttpServletResponse response, Model model,
						String mobileFlag, String specialRef) {



		Session session = UserUtils.getSession();
		if (StringUtils.isNotEmpty(mobileFlag)) {
			request.setAttribute("mobileFlag", mobileFlag);
		}
		if (StringUtils.isNotEmpty(specialRef)) {
			JedisClusterUtils.set("specialRef" + session.getId(), specialRef, JEDISTIMEOUT);
		}

		return "web/member/loginFormNew";
	}

	/**
	 * 登出
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@RefreshCSRFToken
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model)
		throws Exception {

		String logoutUrl = ssoServerHost + "ecsuc/remote/userServer/userLogout";
		Session session = UserUtils.getSession();
		String tkey = CookieUtils.getCookie(request, "ticketId"); // 从COOKIE获取用户登录令牌信息
		String uId = CookieUtils.getCookie(request, "uId");

		if (StringUtils.isNotEmpty(tkey) && StringUtils.isNotEmpty(uId)) { // 令牌不为空，则调用单点登录中心登出
			Map<String, String[]> paramMap = new HashMap<String, String[]>();
			paramMap.put("ticketId", new String[] { tkey });
			paramMap.put("uId", new String[] { uId });
			paramMap.put("channelID", new String[] { channelID });

			String strres = "";
			try {
				strres = HttpClientUtils.doPostAndGetString(logoutUrl, paramMap);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JSONObject jsonRes = JSONObject.parseObject(strres);
			Long code = jsonRes.getLong("code");
			String msg = jsonRes.getString("msg");

			if (code == 200) { // 登出成功
				JedisClusterUtils.del("sessionId" + session.getId());
				JedisClusterUtils.delObject("loginUser" + session.getId());
				CookieUtils.setCookie(response, "ticketId", "", -1);
				CookieUtils.setCookie(response, "uid", "", -1);
			} else {
				/*
				 * BusiLogUtils.writerLogging(request, "logout", null, null,
				 * DateUtils.formatDate(new Date()), null, msg, msg, "", null, "", null);
				 */
				// MemberLoginLogUtils.saveLog(request,"登出","失败","登出失败",null,null);
				throw new Exception(msg);// 登出失败，返回失败信息
			}
		}
		JedisClusterUtils.delObject("loginUser" + session.getId());
		JedisClusterUtils.delObject("LOGIN_BASEINFO_" + session.getId());
		JedisClusterUtils.del("sessionId" + session.getId());
		CookieUtils.setCookie(response,MOBILE, "",-1);
		UserUtils.getSubject().logout();
		return "web/member/loginForm";
	}

	/**
	 * 用户名登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@VerifyCSRFToken
	@RequestMapping(value = "/loginByName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> loginByName(HttpServletRequest request, HttpServletResponse response,
		String loginname, String password, String captcha) throws Exception {

		loginname = TriDes.getInstance().strDec(loginname, keyStr, null, null);
		password = TriDes.getInstance().strDec(password, keyStr, null, null);
		Integer failLoginCount = 0;
		String failStr = JedisClusterUtils.get("failLoginCount" + loginname);
		if (StringUtils.isNotEmpty(failStr)) {
			failLoginCount = Integer.parseInt(failStr);
		}
		Map<String, String> resMap = new HashMap<String, String>();
		Session session = UserUtils.getSession();
		String captchaReal = (String) JedisClusterUtils.get("VC_VALUE_LOGIN" + session.getId());

		if (StringUtils.isEmpty(captchaReal)) {
			resMap.put("msg", "验证码失效，请刷新");
			return resMap;
		}
		if (captchaReal != null && !captchaReal.equalsIgnoreCase(captcha)) {
			JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
			resMap.put("msg", "验证码校验失败，请重新输入");
			return resMap;
		}

		UserContext context = new UserContext();
		context.setClientIp(request.getRemoteHost());
		context.setSessionId(request.getSession().getId());
		context.setChannelCode("E007");
		try {

			if (failLoginCount > 5) {
				resMap.put("msg", "登录失败超过5次，您的账号被冻结30分钟");
				Integer failLoginLockCount = 0;
				String failLoginLockCountStr = JedisClusterUtils.get("failLoginLockCount" + loginname);
				if (StringUtils.isNotEmpty(failLoginLockCountStr)) {
					failLoginLockCount = Integer.parseInt(failLoginLockCountStr);
				}
				if (failLoginLockCount < 5) {
					JedisClusterUtils.set("failLoginLockCount" + loginname, ++failLoginLockCount + "",
						LOGINLOCKJEDISTIMEOUT);
				} else {
					MemberLogin memberLogin = memberLoginService.getByLoginMame(loginname, MemberTypeConstant.REGISTER.getValue());
					if (memberLogin != null) {
						memberLogin.setMemberPassword(null);
						memberLogin.setMemberStatusId(MemberStatusConstant.LOCKUP.getValue());
						memberLoginService.updatememberLogin(memberLogin);

					}
					resMap.put("msg", "冻结超过5次，您的账号被锁定，请联系客服");
				}
				return resMap;
			}
			Map<String, Object> member = null;
			try {
				member = loginService.loginByName(context, loginname, password);
			} catch (NullPointerException e) {
				BusiLogUtils.writerLogging(request, "loginByName", null, null, DateUtils
					.formatDate(new Date()), null, "该账号不存在", "该账号不存在", "", e, "", loginname);
				failLoginCount++;
				resMap.put("msg", "用户名或密码错误!");
				JedisClusterUtils.set("failLoginCount" + loginname, failLoginCount + "", LOGINJEDISTIMEOUT);
				return resMap;
			}
			JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JEDISTIMEOUT);
			if (member != null) {
				if (member.get("msg") != null) {
					resMap.put("msg", (String) member.get("msg"));
					failLoginCount++;
					JedisClusterUtils.set("failLoginCount" + loginname, failLoginCount + "",
						LOGINJEDISTIMEOUT);
					MemberLoginLogUtils.saveLog(request, "4", "1", "登录失败", null, loginname, null);
					return resMap;
				}
				Object obj = member.get("data");
				MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
				JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);
				JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
				resMap.put("username", loginname);
				resMap.put("msg", "success");
				/*
				 * BusiLogUtils.writerLogging(request, "loginByName", null, null,
				 * DateUtils.formatDate(new Date()), null, "用户名登录成功", "用户名登录成功", "", null, "",
				 * loginname);
				 */
				MemberBaseVo baseMember = new MemberBaseVo();
				baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
				baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
				JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),
					JEDISTIMEOUT);
				MemberLoginLogUtils.saveLog(request, "4", "0", "登录成功", null, loginname, membervo);
				request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));//刷新csrfToken的值
			}
		} catch (Exception e) {
			/*
			 * BusiLogUtils.writerLogging(request, "loginByName", null, null,
			 * DateUtils.formatDate(new Date()), null, e.getMessage(), e.getMessage(), "", e, "",
			 * loginname);
			 */
			MemberLoginLogUtils.saveLog(request, "4", "1", "登录失败", null, loginname, null);
			resMap.put("msg", "会员服务异常");
		}

		return resMap;
	}

	/**
	 * 手机号+短信验证码登录
	 * @param request
	 * @param response
	 * @return
	 */
	@VerifyCSRFToken
	@RequestMapping(value = "/loginBySms", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> loginBySms(HttpServletRequest request, HttpServletResponse response,
		String mobile, String smsCaptcha, String imageCaptcha) {
		CookieUtils.setCookie(response,MOBILE, "",60*60*24);
		mobile = TriDes.getInstance().strDec(mobile, keyStr, null, null);
		smsCaptcha = TriDes.getInstance().strDec(smsCaptcha, keyStr, null, null);
		// 登录失败次数
		Integer failLoginCount = 0;
		String failStr = JedisClusterUtils.get("failLoginCount" + mobile);
		if (StringUtils.isNotEmpty(failStr)) {
			failLoginCount = Integer.parseInt(failStr);
		}
		// 登录成功次数
		Integer successLoginCount = 0;
		String successStr = JedisClusterUtils.get("successLoginCount" + mobile);
		if (StringUtils.isNotEmpty(successStr)) {
			successLoginCount = Integer.parseInt(successStr);
		}

		Map<String, String> resMap = new HashMap<String, String>();
		Session session = UserUtils.getSession();
		String checkMobile = JedisClusterUtils.get("MemberLogin" + session.getId() + mobile);
		if (checkMobile == null) {
			resMap.put("msg", "随机码已失效，请重新发送短信随机码！");
			return resMap;
		} else if (!checkMobile.equals(mobile)) {
			JedisClusterUtils.del("MemberLogin" + session.getId() + mobile);
			resMap.put("msg", "短信验证手机与登录手机号不一致！");
			return resMap;
		}
		String specialRef = JedisClusterUtils.get("specialRef" + session.getId());
		resMap.put("ref", specialRef);
		String loginUrl = ssoServerHost + "ecsuc/remote/userServer/userLogin";
		String loginType="dx";
		if(verify(mobile, request,"dx")) {
			String captchaReal = (String) JedisClusterUtils.get("VC_VALUE_LOGIN" + session.getId());
			if (StringUtils.isEmpty(captchaReal)) {
				resMap.put("msg", "验证码失效，请刷新");
				return resMap;
			}
			if (captchaReal != null && !captchaReal.equalsIgnoreCase(imageCaptcha)) {
				resMap.put("msg", "验证码校验失败，请重新输入");
				return resMap;
			}
		}

		if (successLoginCount > 15) {
			resMap.put("msg", "您的账号今日登录次数超过15次，请明天再来！");
			return resMap;
		}
		if (failLoginCount > 3) {
			resMap.put("msg", "登录失败超过3次，您的账号被冻结30分钟");
			Integer failLoginLockCount = 0;
			String failLoginLockCountStr = JedisClusterUtils.get("failLoginLockCount" + mobile);
			if (StringUtils.isNotEmpty(failLoginLockCountStr)) {
				failLoginLockCount = Integer.parseInt(failLoginLockCountStr);
			}
			if (failLoginLockCount < 5) {
				JedisClusterUtils.set("failLoginLockCount" + mobile, ++failLoginLockCount + "",
					LOGINLOCKJEDISTIMEOUT);
			} else {
				try {
					MemberLogin memberLogin = memberLoginService.getByLoginMame(mobile,MemberTypeConstant.MOBILE.getValue());
					if (memberLogin != null) {
						memberLogin.setMemberPassword(null);
						memberLogin.setMemberStatusId(MemberStatusConstant.LOCKUP.getValue());
						memberLoginService.updatememberLogin(memberLogin);
					}
					resMap.put("msg", "冻结超过5次，您的账号被锁定，请联系客服");
				} catch (Exception e) {
					resMap.put("msg", "系统异常");
					return resMap;
				}
			}
			return resMap;
		}

		UserContext context = new UserContext();
		context.setClientIp(request.getRemoteAddr());
		context.setMobile(mobile);
		context.setChannelCode("E007");
		try {
			// ****************************************单点登录*************************
			Map<String, String[]> paramMap = new HashMap<String, String[]>();
			paramMap.put("username", new String[] { mobile });
			paramMap.put("channel", new String[] { channelID });
			paramMap.put("isSMS", new String[] { "01" });
			paramMap.put("password", new String[] { smsCaptcha });
			paramMap.put("isImg", new String[] { "02" });
			paramMap.put("regType", new String[] { "01" });
			paramMap.put("loginType", new String[] { "02" });
			paramMap.put("onlyOne", new String[]{"01"}); //01:只允许一个终端登录02：允许多个终端同时登录（默认）
			paramMap.put("clientIp", new String[]{request.getRemoteAddr()});

			String strres = "";
			try {
				logger.error("loginBySms=>loginUrl" + loginUrl + ",paramMap:" + paramMap);
				strres = HttpClientUtils.doPostAndGetString(loginUrl, paramMap);
				logger.error("loginBySms=>strres" + strres);
			} catch (ClientProtocolException e) {
				logger.error("系统异常", e);
				resMap.put("msg", "系统异常");
				return resMap;
			} catch (IOException e) {
				logger.error("系统异常", e);
				resMap.put("msg", "系统异常");
				return resMap;
			}
			JSONObject jsonRes = JSONObject.parseObject(strres);
			Long rescode = jsonRes.getLong("code");
			String msg = jsonRes.getString("msg");
			String loginKey = jsonRes.getString("t");// 获取单点登录令牌，为空则登录失败
			JSONObject attributes = jsonRes.getJSONObject("simplePrincipal").getJSONObject("attributes");

			if (StringUtils.isNotEmpty(loginKey)) {
				context.setSessionId(loginKey);
				Map<String, Object> loginRes = loginService.loginBySms(context, smsCaptcha); // 商城登录，首次登录会生成账号并记录用户信息
				if (loginRes != null) {
					CookieUtils.setCookie(response, "ticketId", loginKey, JEDISTIMEOUT);
					CookieUtils.setCookie(response, "uId", attributes.getString("uid"), JEDISTIMEOUT);
					Object obj = loginRes.get("data");
					MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
					JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);

					resMap.put("channelID", channelID);
					resMap.put("Artifact", attributes.getString("artifact"));
					resMap.put("goToGroupCookieUrl", goToGroupCookieUrl);
					resMap.put("msg", "success");
					resMap.put("url", "");


					// https://actest.10086.cn/AddUID.htm?
					// channelID=00731&Artifact=153c99188da44cd1b947a8cbc5994e1c&backUrl=http%3A%2F%2Fwww.hn.10086.cn%2Fnewservice%2Fstatic%2FmyMobile%2FmonthBillQuery.html&TransactionID=1479177037251
					/*
					 * BusiLogUtils.writerLogging(request, "loginBySms", null, mobile,
					 * DateUtils.formatDate(new Date()), null, "短信验证码登录成功", "短信验证码登录成功", "", null,
					 * "", mobile);
					 */
					MemberLoginLogUtils.saveLog(request, "1", "0", "登录成功", loginKey, mobile, membervo);
					resMap.put("mobile", mobile);
					MemberSsoVo user = UserUtils.getSsoLoginUser(request, loginKey);
					if (user != null) {
						resMap.put("cityCode", user.getRemoveEparchyCode());
						CookieUtils.setCookie(response,BRAND, brand(user.getBrand()),60*60*24);
					} else {
						resMap.put("cityCode", "");
					}
					JedisClusterUtils.del("specialRef" + session.getId());
					JedisClusterUtils.del("MemberLogin" + session.getId() + mobile);
					JedisClusterUtils.del(MEMBER_LOGIN_SECONDS_SMS_COUNT_STR + mobile);
					JedisClusterUtils.del(MEMBER_LOGIN_SEND_SMS_COUNT + mobile);
					MemberBaseVo baseMember = new MemberBaseVo();
					baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
					baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
					baseMember.setEparchCode(user.getRemoveEparchyCode());
					JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),
						JEDISTIMEOUT);
					successLoginCount++;
					JedisClusterUtils.set("successLoginCount" + mobile, successLoginCount + "", 30 * 60);
					JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
					CookieUtils.setCookie(response,MALL_VCODE+loginType, MD5.encode(WAP_MALL_AI.concat(mobile)),60*60*24*10);
					JedisClusterUtils.set(MALL_VCODE+loginType+mobile, MD5.encode(WAP_MALL_AI.concat(mobile)),60*60*24*10);
					JedisClusterUtils.expires(MALL_VCODE+loginType+mobile, 60*60*24*10);
					CookieUtils.setCookie(response,MOBILE, mobile,60*60*24);
					request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));//登录成功刷新csrfToken的值
				}
			} else if (StringUtils.isNotEmpty(msg)) {
				failLoginCount++;
				JedisClusterUtils.set("failLoginCount" + mobile, failLoginCount + "", LOGINJEDISTIMEOUT);
				MemberLoginLogUtils.saveLog(request, "1", "1", "登录失败", null, mobile, null);
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
				resMap.put("msg", msg);
			}
		} catch (Exception e) {
			try {
				/*
				 * BusiLogUtils.writerLogging(request, "loginBySms", null, mobile,
				 * DateUtils.formatDate(new Date()), null, "短信验证码登录失败", "短信验证码登录失败", "", e, "",
				 * mobile);
				 */
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
				MemberLoginLogUtils.saveLog(request, "1", "1", "登录失败", null, mobile, null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			resMap.put("msg", "短信验证码登录失败！");
		}finally {
			JedisClusterUtils.del("MemberLogin" + session.getId() + mobile);
		}

		return resMap;
	}


	/**
	 * 手机号+短信验证码登录,
	 * @param request
	 * @param response
	 * @return
	 */
	/*@VerifyCSRFToken
	@RequestMapping(value = "/loginBySmsGroup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> loginBySmsGroup(HttpServletRequest request, HttpServletResponse response,
										  String mobile, String smsCaptcha, String imageCaptcha) {
		mobile = TriDes.getInstance().strDec(mobile, keyStr, null, null);
		// 登录失败次数
		Integer failLoginCount = 0;
		String failStr = JedisClusterUtils.get("failLoginCount" + mobile);
		if (StringUtils.isNotEmpty(failStr)) {
			failLoginCount = Integer.parseInt(failStr);
		}
		// 登录成功次数
		Integer successLoginCount = 0;
		String successStr = JedisClusterUtils.get("successLoginCount" + mobile);
		if (StringUtils.isNotEmpty(successStr)) {
			successLoginCount = Integer.parseInt(successStr);
		}

		Map<String, String> resMap = new HashMap<String, String>();
		Session session = UserUtils.getSession();
		String checkMobile = JedisClusterUtils.get("MemberLogin" + session.getId() + mobile);
		if (checkMobile == null) {
			resMap.put("msg", "随机码已失效，请重新发送短信随机码！");
			return resMap;
		} else if (!checkMobile.equals(mobile)) {
			JedisClusterUtils.del("MemberLogin" + session.getId() + mobile);
			resMap.put("msg", "短信验证手机与登录手机号不一致！");
			return resMap;
		}
		String specialRef = JedisClusterUtils.get("specialRef" + session.getId());
		resMap.put("ref", specialRef);
		String loginType="dx";

		if (successLoginCount > 15) {
			resMap.put("msg", "您的账号今日登录次数超过15次，请明天再来！");
			return resMap;
		}
		if (failLoginCount > 3) {
			resMap.put("msg", "登录失败超过3次，您的账号被冻结30分钟");
			Integer failLoginLockCount = 0;
			String failLoginLockCountStr = JedisClusterUtils.get("failLoginLockCount" + mobile);
			if (StringUtils.isNotEmpty(failLoginLockCountStr)) {
				failLoginLockCount = Integer.parseInt(failLoginLockCountStr);
			}
			if (failLoginLockCount < 5) {
				JedisClusterUtils.set("failLoginLockCount" + mobile, ++failLoginLockCount + "",
						LOGINLOCKJEDISTIMEOUT);
			} else {
				try {
					MemberLogin memberLogin = memberLoginService.getByLoginMame(mobile,MemberTypeConstant.MOBILE.getValue());
					if (memberLogin != null) {
						memberLogin.setMemberPassword(null);
						memberLogin.setMemberStatusId(MemberStatusConstant.LOCKUP.getValue());
						memberLoginService.updatememberLogin(memberLogin);
					}
					resMap.put("msg", "冻结超过5次，您的账号被锁定，请联系客服");
				} catch (Exception e) {
					resMap.put("msg", "系统异常");
					return resMap;
				}
			}
			return resMap;
		}

		UserContext context = new UserContext();
		context.setClientIp(request.getRemoteAddr());
		context.setMobile(mobile);
		context.setChannelCode("E007");
		try {
			String loginKey="";
            JSONObject resJSONObj=LoginUtils.loginBySms(mobile,smsCaptcha,request);
            JSONObject headerJSONObj =null;
            if(null!=resJSONObj){
                headerJSONObj = resJSONObj.getJSONObject("header");
            }
			if(null == headerJSONObj || !"104000".equals(headerJSONObj.getString("resultcode"))){
                resMap.put("msg", "验证码错误");
                return resMap;
			}
			if (StringUtils.isNotEmpty(loginKey)) {
				context.setSessionId(loginKey);
				Map<String, Object> loginRes = loginService.loginBySms(context, smsCaptcha); // 商城登录，首次登录会生成账号并记录用户信息
				if (loginRes != null) {
					CookieUtils.setCookie(response, "ticketId", loginKey, JEDISTIMEOUT);
					CookieUtils.setCookie(response, "uId", "", JEDISTIMEOUT);
					Object obj = loginRes.get("data");
					MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
					JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);

					resMap.put("channelID", channelID);
					resMap.put("goToGroupCookieUrl", goToGroupCookieUrl);
					resMap.put("msg", "success");
					resMap.put("url", "");

					MemberLoginLogUtils.saveLog(request, "1", "0", "登录成功", loginKey, mobile, membervo);
					resMap.put("mobile", mobile);
					MemberSsoVo user = UserUtils.getSsoLoginUser(request, loginKey);
					if (user != null) {
						resMap.put("cityCode", user.getRemoveEparchyCode());
					} else {
						resMap.put("cityCode", "");
					}
					JedisClusterUtils.del("specialRef" + session.getId());
					JedisClusterUtils.del("MemberLogin" + session.getId() + mobile);
					JedisClusterUtils.del(MEMBER_LOGIN_SECONDS_SMS_COUNT_STR + mobile);
					JedisClusterUtils.del(MEMBER_LOGIN_SEND_SMS_COUNT + mobile);
					MemberBaseVo baseMember = new MemberBaseVo();
					baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
					baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
					baseMember.setEparchCode(user.getRemoveEparchyCode());
					JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),
							JEDISTIMEOUT);
					successLoginCount++;
					JedisClusterUtils.set("successLoginCount" + mobile, successLoginCount + "", 30 * 60);
					JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
					CookieUtils.setCookie(response,MALL_VCODE+loginType, MD5.encode(WAP_MALL_AI.concat(mobile)),60*60*24*10);
					JedisClusterUtils.set(MALL_VCODE+loginType+mobile, MD5.encode(WAP_MALL_AI.concat(mobile)),60*60*24*10);
					JedisClusterUtils.expires(MALL_VCODE+loginType+mobile, 60*60*24*10);
				}
			} else if (StringUtils.isNotEmpty(msg)) {
				failLoginCount++;
				JedisClusterUtils.set("failLoginCount" + mobile, failLoginCount + "", LOGINJEDISTIMEOUT);
				MemberLoginLogUtils.saveLog(request, "1", "1", "登录失败", null, mobile, null);
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
				resMap.put("msg", msg);
			}
		} catch (Exception e) {
			try {
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
				MemberLoginLogUtils.saveLog(request, "1", "1", "登录失败", null, mobile, null);
			} catch (Exception e1) {
                logger.error("loginBySmsGroup",e1);
			}
			resMap.put("msg", "短信验证码登录失败！");
		}finally {
			JedisClusterUtils.del("MemberLogin" + session.getId() + mobile);
		}

		return resMap;
	}*/
	/**
	 * 手机号+服务密码登录+随机码
	 * @param request
	 * @param response
	 * @return
	 */
	@VerifyCSRFToken
	@RequestMapping(value = "/loginByService", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> loginByService(HttpServletRequest request, HttpServletResponse response,
		String mobile, String servicePass, String verifyCode) {
		CookieUtils.setCookie(response,MOBILE, "",60*60*24);
		mobile = TriDes.getInstance().strDec(mobile, keyStr, null, null);
		servicePass = TriDes.getInstance().strDec(servicePass, keyStr, null, null);
		writerFlowLogEnterMenthod(createStreamNo(),"",mobile,getClass().getName(),"loginByService",request.getParameterMap(),
				"服务密码登录 environment:"+environment,request);
		if(null!=environment && environment.equals("test")){
			return testLogin(mobile,request,response);
		}
		Integer failLoginCount = 0;
		String failStr = JedisClusterUtils.get("failLoginCount" + mobile + servicePass);
		if (StringUtils.isNotEmpty(failStr)) {
			failLoginCount = Integer.parseInt(failStr);
		}
		// 登录成功次数
		Integer successLoginCount = 0;
		String successStr = JedisClusterUtils.get("successLoginCount" + mobile);
		if (StringUtils.isNotEmpty(successStr)) {
			successLoginCount = Integer.parseInt(successStr);
		}

		Map<String, String> resMap = new HashMap<String, String>();
		Session session = UserUtils.getSession();
		String specialRef = JedisClusterUtils.get("specialRef" + session.getId());
		resMap.put("ref", specialRef);
		String loginType="fw";
		if(verify(mobile, request,loginType)) {
			String captchaReal = (String) JedisClusterUtils.get(SEND_SMS_CODE_VCODE_KEY.concat(session.getId()+":").concat(mobile));
			if (StringUtils.isEmpty(captchaReal)) {
				resMap.put("msg", "短信随机码不正确或已过期");
				return resMap;
			}
			if (captchaReal != null && !captchaReal.equalsIgnoreCase(verifyCode)) {
				JedisClusterUtils.del(SEND_SMS_CODE_VCODE_KEY.concat(session.getId()+":").concat(mobile));
				logger.error("captchaReal:" + captchaReal + ", verifyCode:" + verifyCode);
				resMap.put("msg", "短信随机码不正确或已过期");
				return resMap;
			}
		}

		if (successLoginCount > 15) {
			logger.error("successLoginCount: " + successLoginCount);
			resMap.put("msg", "您的账号今日登录次数超过15次，请明天再来！");
			return resMap;
		}
		if (failLoginCount > 3) {
			logger.error("failLoginCount: " + failLoginCount);
			resMap.put("msg", "登录失败超过3次，您的账号被冻结30分钟");
			Integer failLoginLockCount = 0;
			String failLoginLockCountStr = JedisClusterUtils.get("failLoginLockCount" + mobile);
			if (StringUtils.isNotEmpty(failLoginLockCountStr)) {
				failLoginLockCount = Integer.parseInt(failLoginLockCountStr);
			}
			if (failLoginLockCount < 5) {
				JedisClusterUtils.set("failLoginLockCount" + mobile, ++failLoginLockCount + "", LOGINLOCKJEDISTIMEOUT);
			} else {
				try {
					MemberLogin memberLogin = memberLoginService.getByLoginMame(mobile,MemberTypeConstant.MOBILE.getValue());
					if (memberLogin != null) {
						memberLogin.setMemberPassword(null);
						memberLogin.setMemberStatusId(MemberStatusConstant.LOCKUP.getValue());
						memberLoginService.updatememberLogin(memberLogin);
					}
					resMap.put("msg", "冻结超过5次，您的账号被锁定，请联系客服");
				} catch (Exception e) {
					logger.error("系统异常", e);
					resMap.put("msg", "系统异常");
					return resMap;
				}
			}
			return resMap;
		}

		UserContext context = new UserContext();
		context.setClientIp(request.getRemoteAddr());
		context.setChannelCode("E007");
		try {

			// ****************************************单点登录*************************
			//String loginUrl = ssoServerHost + "ecsuc/remote/userServer/userLogin"; 10731,40731,30731,40731
			String loginUrl = ssoServerHost + "ecsuc/remote/userServer/userLogin";
			Map<String, String[]> paramMap = new HashMap<String, String[]>();
			paramMap.put("username", new String[] { mobile });
			paramMap.put("password", new String[] { servicePass });
			paramMap.put("channel", new String[] { channelID });
			paramMap.put("isSMS", new String[] { "02" });
			paramMap.put("isImg", new String[] { "02" });
			paramMap.put("regType", new String[] { "01" });
			paramMap.put("loginType", new String[] { "01" });
			paramMap.put("onlyOne", new String[]{"01"}); //01:只允许一个终端登录02：允许多个终端同时登录（默认）
			paramMap.put("clientIp", new String[]{ request.getRemoteAddr() });
			String strres = "";
			try {
				logger.info("userLogin loginUrl:" +  loginUrl + " paramMap:" + JSON.toJSONString(paramMap));
				strres = HttpClientUtils.doPostAndGetString(loginUrl, paramMap);
				logger.info("userLogin:" +  strres);
			} catch (ClientProtocolException e) {
				logger.error("ClientProtocolException", e);
				resMap.put("msg", "系统异常");
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
				return resMap;
			} catch (IOException e) {
				logger.error("IOException", e);
				resMap.put("msg", "系统异常");
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
				return resMap;
			}

			JSONObject jsonRes = JSONObject.parseObject(strres);
			Long rescode = jsonRes.getLong("code");
			String msg = jsonRes.getString("msg");
			String loginKey = jsonRes.getString("t");// 获取单点登录令牌，为空则登录失败


			if (StringUtils.isNotEmpty(loginKey)) {
				JSONObject attributes = jsonRes.getJSONObject("simplePrincipal").getJSONObject("attributes");
				context.setSessionId(loginKey);
				Map<String, Object> loginRes = loginService.loginByMobile(context, mobile, servicePass);// 商城登录，首次登录会生成账号并记录用户信息
				if (loginRes != null) {
					if (loginRes.get("msg") != null) {
						resMap.put("msg", (String) loginRes.get("msg"));
						CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
						JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
						MemberLoginLogUtils.saveLog(request, "0", "1", "登录失败", null, mobile, null);
						return resMap;
					}
					CookieUtils.setCookie(response, "ticketId", loginKey, JEDISTIMEOUT);
					CookieUtils.setCookie(response, "uId", attributes.getString("uid"), JEDISTIMEOUT);
					Object obj = loginRes.get("data");
					MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
					JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);
					resMap.put("channelID", channelID);
					resMap.put("Artifact", attributes.getString("artifact"));
					resMap.put("goToGroupCookieUrl", goToGroupCookieUrl);
					resMap.put("TransactionID", getTransactionID(channelID));
					resMap.put("msg", "success");
					JedisClusterUtils.del("specialRef" + session.getId());
					resMap.put("mobile", mobile);
					MemberSsoVo user = UserUtils.getSsoLoginUser(request, loginKey);
					if (user != null) {
						resMap.put("cityCode", user.getRemoveEparchyCode());
						CookieUtils.setCookie(response,BRAND, brand(user.getBrand()),60*60*24);
					} else {
						resMap.put("cityCode", "");
					}

					MemberLoginLogUtils.saveLog(request, "0", "0", "登录成功", loginKey, mobile, membervo);
					successLoginCount++;
					JedisClusterUtils.set("successLoginCount" + mobile, successLoginCount + "", 30 * 60);
					JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
					MemberBaseVo baseMember = new MemberBaseVo();
					baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
					baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
					baseMember.setEparchCode(user.getRemoveEparchyCode());
					JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember), JEDISTIMEOUT);
					CookieUtils.setCookie(response,MALL_VCODE+loginType, MD5.encode(WAP_MALL_AI.concat(mobile)),60*60*24*10);
					CookieUtils.setCookie(response,MOBILE, mobile,60*60*24);
					JedisClusterUtils.set(MALL_VCODE+loginType+mobile,MD5.encode(WAP_MALL_AI.concat(mobile)),60*60*24*10);
					request.getSession().setAttribute("CSRFToken", CSRFTokenUtil.generate(request));//登录成功刷新csrfToken的值
					return resMap;
				}
			} else if (StringUtils.isNotEmpty(msg)) {
				failLoginCount++;
				JedisClusterUtils.set("failLoginCount" + mobile + servicePass, failLoginCount + "",
					LOGINJEDISTIMEOUT);
				if ("user and pwd error".equals(msg)) {
					msg = "用户名或密码不正确!";
				}
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
				MemberLoginLogUtils.saveLog(request, "0", "1", "登录失败", null, mobile, null);
				resMap.put("msg", msg);
				return resMap;
			}
		} catch (Exception e) {
			try {
				/*
				 * BusiLogUtils.writerLogging(request, "loginByService", null, mobile,
				 * DateUtils.formatDate(new Date()), null, "服务密码登录失败", "服务密码登录失败", "", e, "",
				 * mobile);
				 */
				MemberLoginLogUtils.saveLog(request, "0", "1", "登录失败", null, mobile, null);
				CookieUtils.setCookie(response,MALL_VCODE+loginType, "",0);
				JedisClusterUtils.del(MALL_VCODE+loginType+mobile);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			logger.error("服务密码登录失败", e);
		}
		resMap.put("msg", "服务密码登录失败");
		return resMap;
	}

	public String getTransactionID(String channelID) {
		SimpleDateFormat sFormat = new SimpleDateFormat("YYYYMMDDHHmmss");
		String realseStr = channelID;
		Date date = new Date();
		realseStr += sFormat.format(date);
		Random random = new Random(10);
		for(int i = 0; i < 10; i++) {
			realseStr += random.nextInt(10);
		}
		return realseStr;
	}

	/**
	 * 发送短信验证码
	 * @param request
	 * @param response
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/sendSms", method = RequestMethod.POST)
	@ResponseBody
	public String sendSms(HttpServletRequest request, HttpServletResponse response, String mobile) {

		String countstr = JedisClusterUtils.get(MEMBER_LOGIN_SEND_SMS_COUNT + mobile);// 短信1天内发送次数
		Integer count = 0;
		if (StringUtils.isNotEmpty(countstr)) {
			count = Integer.parseInt(countstr);
		}
		if (count >= 5) {
			return "短信下发数已达上限，您可以使用服务密码方式登录！";
		}

		String secondsSmsCountStr = JedisClusterUtils.get(MEMBER_LOGIN_SECONDS_SMS_COUNT_STR + mobile);// 短信1分钟内发送次数
		Integer secondsSmsCount = 0;
		if (StringUtils.isNotEmpty(secondsSmsCountStr)) {
			secondsSmsCount = Integer.parseInt(secondsSmsCountStr);
		}
		if (secondsSmsCount >= 1) {
			return "短信发送时间间隔太短，请稍后再试！";
		}

		String smsUrl = ssoServerHost + "ecsuc/remote/userServer/sendSmsCodeByPhone";
		UserContext context = new UserContext();
		context.setClientIp(request.getRemoteAddr());
		context.setSessionId(request.getSession().getId());

		Map<String, String[]> paramMap = new HashMap<String, String[]>();
		paramMap.put("phone", new String[] { mobile });

		String strres = "";
		try {
			logger.info("sendSms=>" + "smsUrl:" + smsUrl +  ",paramMap:" + JSON.toJSONString(paramMap));
			strres = HttpClientUtils.doPostAndGetString(smsUrl, paramMap);
			logger.info("sendSms=>" + "strres:" + strres);
			JSONObject jsonRes = JSONObject.parseObject(strres);
			Long code = jsonRes.getLong("code");
			String msg = jsonRes.getString("msg");
			String t = jsonRes.getString("t");
			if ("on".equals(smsLight)) {
				request.setAttribute("sms", t);
			}
			if (code != 200) {
				if (StringUtils.isNotEmpty(msg)) {
					return msg;
				}
				return "获取短信验证码失败";
			}
			count++;
			secondsSmsCount++;
			JedisClusterUtils.set(MEMBER_LOGIN_SEND_SMS_COUNT + mobile, count + "", SMSOUT);
			JedisClusterUtils.set(MEMBER_LOGIN_SECONDS_SMS_COUNT_STR + mobile, secondsSmsCount + "",
				SECONDSSMSOUT);
			Session session = UserUtils.getSession();
			JedisClusterUtils.set("MemberLogin" + session.getId() + mobile, mobile, JEDISTIMEOUT);
			return "success";

		} catch (Exception e) {
			logger.error("获取登录验证码失败：", e);
			return "获取短信验证码失败";
		}
	}

	/**
	 * 获取图片验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "getCaptchaImage.do", method = RequestMethod.GET)
	public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response, String type)
		throws IOException {

		// 禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的响应是图片
		response.setContentType("image/jpeg");
		int width = 200;
		int height = 60;
		// 创建BufferedImage类的对象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 创建Graphics类的对象
		Graphics g = image.getGraphics();
		// 通过Graphics类的对象创建一个Graphics2D类的对象
		Graphics2D g2d = (Graphics2D) g;
		// 实例化一个Random对象
		Random rand = new Random();
		// 通过Font构造字体
		Font nFont = g.getFont();
		g.setFont(nFont.deriveFont(50F));
		// 改变图形的当前颜色为随机生成的颜色
		g.setColor(getRandColor(200, 250));
		// 绘制一个填色矩形
		g.fillRect(0, 0, width, height);
		// 画一条折线
		BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT,
		// 创建一个供画笔选择线条粗细的对象
			BasicStroke.JOIN_BEVEL);
		// 改变线条的粗细
		g2d.setStroke(bs);
		// 设置当前颜色为预定义颜色中的深灰色
		g.setColor(Color.DARK_GRAY);
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		for (int j = 0; j < 3; j++) {
			xPoints[j] = rand.nextInt(width - 1);
			yPoints[j] = rand.nextInt(height - 1);
		}
		g.drawPolyline(xPoints, yPoints, 3);
		// 生成并输出随机的验证文字
		StringBuilder sRand = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			char ctmp = RAND_CODES[rand.nextInt(RAND_CODES.length)];
			sRand.append(ctmp);
			Color color = new Color(20 + rand.nextInt(110), 20 + rand.nextInt(110), 20 + rand.nextInt(110));
			g.setColor(color);
			/**** 随机缩放文字并将文字旋转指定角度 **/
			// 将文字旋转指定角度
			// Graphics2D g2d_word = (Graphics2D) g;
			AffineTransform trans = new AffineTransform();
			trans.rotate(rand.nextInt(45) * 3.14 / 180, 15 * i + 10, 7);
			// 缩放文字
			float scaleSize = rand.nextFloat() + 0.8f;
			if (scaleSize > 1.1f)
				scaleSize = 1f;
			trans.scale(scaleSize, scaleSize);
			// g2d_word.setTransform(trans);
			/************************/
			g.drawString(String.valueOf(ctmp), 30 * i + 40, 35 + rand.nextInt(17));
		}
		g.dispose();

		// 将生成的验证码保存到Session中
		Session session = UserUtils.getSession();
		request.setAttribute("sessionId", session.getId());
		if (StringUtils.isEmpty(type)) {
			JedisClusterUtils.set("VC_VALUE_LOGIN" + session.getId(), sRand.toString(), CAPTCHAJEDISTIMEOUT);
			JedisClusterUtils.set("VC_CREATE_TIME" + session.getId(), System.currentTimeMillis() + "",
				CAPTCHAJEDISTIMEOUT);
		} else if ("findPass".equals(type)) {
			JedisClusterUtils.set("VC_VALUE_LOGIN_FINDPASS" + session.getId(), sRand.toString(),
				CAPTCHAJEDISTIMEOUT);
			JedisClusterUtils.set("VC_CREATE_TIME_FINDPASS" + session.getId(), System.currentTimeMillis()
				+ "", CAPTCHAJEDISTIMEOUT);
		}
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	private Color getRandColor(int s, int e) {

		Random random = new Random();
		if (s > 255) {
			s = 255;
		}
		if (e > 255) {
			e = 255;
		}
		int r = s + random.nextInt(e - s);
		int g = s + random.nextInt(e - s);
		int b = s + random.nextInt(e - s);
		return new Color(r, g, b);
	}

	@RequestMapping(value = "getArtifact")
	public void getArtifact(HttpServletRequest request, HttpServletResponse response, String artifact, String backUrl) throws IOException {
		String streamNo=createStreamNo();
		writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),"getArtifact",
				request.getParameterMap(),"getArtifact 进入从集团访问商城，集团回调认证方法",request);
		logger.error("getArtifact=>artifact:" +  artifact + ",backUrl:" + backUrl);
		if ("-1".equals(artifact) || StringUtils.isEmpty(artifact)) {
			logger.error("集团没有登陆， 跳转到对应的地址");
			response.sendRedirect(backUrl);
		}

		String loginUrl = ssoServerHost + "ecsuc/remote/userServer/userLogin";
		Map<String, String[]> paramMap = new HashMap<String, String[]>();
		paramMap.put("username", new String[]{artifact+""});
		paramMap.put("password", new String[]{"password"});
		paramMap.put("channel", new String[]{channelID});
		paramMap.put("isSMS", new String[]{"02"});
		paramMap.put("isImg", new String[]{"02"});
		paramMap.put("regType", new String[]{"01"});
		paramMap.put("loginType", new String[]{"03"});
		paramMap.put("onlyOne", new String[]{"01"}); //01:只允许一个终端登录02：允许多个终端同时登录（默认）
		String strres = "";
		try {
			logger.error("userLogin params:" + paramMap);
			strres = HttpClientUtils.doPostAndGetString(loginUrl, paramMap);
			writerFlowLogConditionAndReturn(streamNo,"","",getClass().getName(),"getArtifact",strres,
					"调用ecsuc/remote/userServer/userLogin接口 ",paramMap,"");
			logger.error("userLogin strres:" + strres);
		} catch (ClientProtocolException e) {
			writerFlowLogThrowable(streamNo,"","",getClass().getName(),
					"getArtifact-查询用户出错",strres,processThrowableMessage(e));
			logger.error("查询用户出错", e);
			response.sendRedirect(backUrl);
		} catch (IOException e) {
			writerFlowLogThrowable(streamNo,"","",getClass().getName(),
					"getArtifact-查询用户出错",strres,processThrowableMessage(e));
			logger.error("查询用户出错", e);
			response.sendRedirect(backUrl);
		}

		JSONObject jsonRes = JSONObject.parseObject(strres);
		if (jsonRes != null && jsonRes.getJSONObject("simplePrincipal") != null) {
			String tkey = jsonRes.getString("t");
			JSONObject artifactObj = jsonRes.getJSONObject("simplePrincipal").getJSONObject("attributes");
			String uid = artifactObj.getString("uid");
			String mobile = artifactObj.getString("userName");

			Session session = UserUtils.getSession();
			UserContext context = new UserContext();
			context.setClientIp(request.getRemoteAddr());
			context.setChannelCode("E007");
			if (StringUtils.isNotEmpty(tkey)) {
				context.setSessionId(tkey);
				Map<String, Object> loginRes = null;// 商城登录，首次登录会生成账号并记录用户信息
				try {
					loginRes = loginService.loginByMobile(context, mobile, "123456");
					writerFlowLogConditionAndReturn(streamNo,"","",getClass().getName(),"getArtifact",loginRes,
							"调用loginService.loginByMobile 接口 ",mobile,"");
					CookieUtils.setCookie(response, "ticketId", tkey, JEDISTIMEOUT);
					CookieUtils.setCookie(response, "uId", artifactObj.getString("uid"), JEDISTIMEOUT);
					Object obj = loginRes.get("data");
					MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
					JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);

					JedisClusterUtils.del("specialRef" + session.getId());

					MemberSsoVo user = UserUtils.getSsoLoginUser(request, tkey);
					MemberLoginLogUtils.saveLog(request, "0", "0", "登录成功", tkey, mobile, membervo);
					JedisClusterUtils.del("VC_VALUE_LOGIN" + session.getId());
					MemberBaseVo baseMember = new MemberBaseVo();
					baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
					baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
					baseMember.setEparchCode(user.getRemoveEparchyCode());
					JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember), JEDISTIMEOUT);
					writerFlowLogExitMenthod(streamNo,"",mobile,getClass().getName(),
							"getArtifact",baseMember,"集团登录成功");
				} catch (Exception e) {
					writerFlowLogThrowable(streamNo,"","",getClass().getName(),
							"getArtifact-loginByMobile 登录异常",strres,processThrowableMessage(e));
					logger.error("登陆出错", e);
				}
			}
		}
		response.sendRedirect(backUrl);
	}

	/**
	 * 发送短信验证码,用于服务密码+随机码登录
	 *
	 * @param request
	 * @param response
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/sendSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String sendSmsCode(HttpServletRequest request,
							  HttpServletResponse response, String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return "手机号码不能为空！";
		}
		try {
			Session session = UserUtils.getSession();
			String sendSmsCodeCountKey = "sendSmsCodeCount:".concat(DateUtils.formatDate(new Date())).concat(mobile);
			String sendSmsCodeCountStrKey = "sendSmsCodeCountStr:".concat(mobile);
			String sendSmsCodeVCodeKey = SEND_SMS_CODE_VCODE_KEY.concat(session.getId()+":").concat(mobile);
			String countstr = JedisClusterUtils.get(sendSmsCodeCountKey);
			Integer count = 0;
			if (StringUtils.isNotEmpty(countstr)) {
				count = Integer.parseInt(countstr);
			}
			if (count > 20) {
				return "短信验证码发送超过限制，请明天再来！";
			}

			String secondsSmsCountStr = JedisClusterUtils.get(sendSmsCodeCountStrKey);//短信1分钟内发送次数
			Integer secondsSmsCount = 0;
			if (StringUtils.isNotEmpty(secondsSmsCountStr)) {
				secondsSmsCount = Integer.parseInt(secondsSmsCountStr);
			}
			if (secondsSmsCount >= 1) {
				return "短信发送时间间隔太短，请稍后再试！";
			}

			SmsSendCondition condition = new SmsSendCondition();
			condition.setSerialNumber(mobile);
			String vcode = getFixLengthString(6);
			condition.setNoticeContent("尊敬的用户，您的短信随机验证码是：" + vcode + "。中国移动不会以任何方式向您索取该密码，请勿告知他人。");
			smsSendService.sendSms(condition);
			count++;
			secondsSmsCount++;
			JedisClusterUtils.set(sendSmsCodeVCodeKey, vcode, 300);
			JedisClusterUtils.set(sendSmsCodeCountKey, count + "", SMSOUT);
			JedisClusterUtils.set(sendSmsCodeCountStrKey, secondsSmsCount + "", 60);

			JedisClusterUtils.set("pcMemberInfo" + session.getId() + mobile, mobile, JEDISTIMEOUT);
			return "success";
		} catch (Exception e) {
			logger.error("sendSmsCode:", e);
		}
		return "获取短信验证码失败";
	}

	/*@RequestMapping(value = "/sendSmsCodeGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String sendSmsCodeGroup(HttpServletRequest request,
							  HttpServletResponse response, String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return "手机号码不能为空！";
		}
		try {
			Session session = UserUtils.getSession();
			String sendSmsCodeCountKey = "sendSmsCodeCount:".concat(DateUtils.formatDate(new Date())).concat(mobile);
			String sendSmsCodeCountStrKey = "sendSmsCodeCountStr:".concat(mobile);
			String countstr = JedisClusterUtils.get(sendSmsCodeCountKey);
			Integer count = 0;
			if (StringUtils.isNotEmpty(countstr)) {
				count = Integer.parseInt(countstr);
			}
			if (count > 20) {
				return "短信验证码发送超过限制，请明天再来！";
			}
            //短信1分钟内发送次数
			String secondsSmsCountStr = JedisClusterUtils.get(sendSmsCodeCountStrKey);
			Integer secondsSmsCount = 0;
			if (StringUtils.isNotEmpty(secondsSmsCountStr)) {
				secondsSmsCount = Integer.parseInt(secondsSmsCountStr);
			}
			if (secondsSmsCount >= 1) {
				return "短信发送时间间隔太短，请稍后再试！";
			}

			JSONObject resJSONObj=LoginUtils.sendSms(mobile,request);
            JSONObject headerJSONObj = null;
			if(null!=resJSONObj){
                headerJSONObj = resJSONObj.getJSONObject("header");
            }
            if(null == headerJSONObj || !"104000".equals(headerJSONObj.getString("resultcode"))){
                return "获取短信验证码失败";
            }
			count++;
			secondsSmsCount++;
			JedisClusterUtils.set(sendSmsCodeCountKey, count + "", SMSOUT);
			JedisClusterUtils.set(sendSmsCodeCountStrKey, secondsSmsCount + "", 60);

			JedisClusterUtils.set("MemberLogin" + session.getId() + mobile, mobile, JEDISTIMEOUT);
			return "success";
		} catch (Exception e) {
			logger.error("sendSmsCode:", e);
		}
		return "获取短信验证码失败";
	}*/

	/**
	 * 是否需要输随机码
	 * @param request
	 * @param pwdType
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/needVerifyCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResponseBean needVerifyCode(HttpServletRequest request, String loginType, String mobile){

		ResponseBean responseBean=new ResponseBean();
		if (verify(mobile, request,loginType)){
			responseBean.addError("0");
		}
		return responseBean;
	}

	/**
	 * 验证是否需要随机码\验证码验证，根据是否成功登录过判断
	 * @param mobile
	 * @param request
	 * @return
	 */
	private boolean verify(String mobile,HttpServletRequest request,String loginType) {
		String mallVCode=CookieUtils.getCookie(request,MALL_VCODE+loginType);
		String mallVCodeRedis=JedisClusterUtils.get(MALL_VCODE+loginType+mobile);
		if(StringUtils.isBlank(mallVCode) || StringUtils.isBlank(mobile) || StringUtils.isBlank(mallVCodeRedis)){
			return true;
		}else{
			String verifyMobile= MD5.encode(WAP_MALL_AI.concat(mobile));
			if(!mallVCode.equals(verifyMobile)
					&& !mallVCodeRedis.equals(verifyMobile)){
				return true;
			}
		}
		return false;
	}

	private String getFixLengthString(int strLength) {

		Random rm = new Random();

		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

		String fixLengthString = String.valueOf(pross);

		return fixLengthString.substring(1, strLength + 1);

	}

	private Map testLogin(String mobile,HttpServletRequest request, HttpServletResponse response){
		UserInfoGetCondition condition3 = new UserInfoGetCondition();
		condition3.setSerialNumber(mobile);
		condition3.setxGetmode("0");
		Map returnData = null;
		Map resMap=new HashMap();
		Session session = UserUtils.getSession();
		try {
			returnData = loginService2.getUserInfo(condition3);
			Map userBasicInfo = net.sf.json.JSONObject.fromObject(returnData.get("result").toString().replace("[", "").replace("]", ""));
			UserContext context = new UserContext();
			context.setClientIp("");
			context.setChannelCode("E007");
			Map<String, Object> loginRes = loginService.loginByMobile(context, mobile, "1234");
			if (loginRes != null) {
				if (loginRes.get("msg") != null) {
					resMap.put("msg", (String) loginRes.get("msg"));
				} else {
					if (loginRes.get("data") == null) {
						resMap.put("msg", "登录失败，请重试");
						MemberLoginLogUtils.saveLog(request, "0", "1", "登录失败", null, mobile, null);
						return resMap;
					}
					CookieUtils.setCookie(response, "ticketId", "loginKey"+mobile, JEDISTIMEOUT);
					Object obj = loginRes.get("data");
					MemberVo membervo = JSONObject.parseObject(
							JSONObject.toJSONString(obj), MemberVo.class);
					JedisClusterUtils.setObject(
							"loginUser" + session.getId(), membervo,
							JEDISTIMEOUT);



					resMap.put("msg", "success");
					resMap.put("mobile", mobile + "");
					resMap.put("Artifact", "");
					resMap.put("TransactionID", getTransactionID(channelID));
					resMap.put("channelID",channelID);
					JedisClusterUtils.del("specialRef" + session.getId());
					if (userBasicInfo != null) {
						resMap.put("cityCode", userBasicInfo.get("EPARCHY_CODE").toString());
					} else {
						resMap.put("cityCode", "");
					}
					MemberLoginLogUtils.saveLog(request, "0", "0", "登录成功", "loginKey"+mobile, mobile, membervo);

					MemberBaseVo baseMember = new MemberBaseVo();

					baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
					baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
					baseMember.setEparchCode(userBasicInfo.get("EPARCHY_CODE").toString());
					JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(),
							JSON.toJSONString(baseMember), JEDISTIMEOUT);

					return resMap;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resMap;
	}

	/**
	 * 品牌转换
	 */
	private String brand (String name){
		String gotone="全球通";
		String Mzone="动感地带";
		String easyown="神州行";

		if(gotone.equals(name)){
			return "gotone";
		}else if(Mzone.equals(name)){
			return "Mzone";
		}else if(easyown.equals(name)){
			return "easyown";
		}else{
			return "";
		}

	}
}
