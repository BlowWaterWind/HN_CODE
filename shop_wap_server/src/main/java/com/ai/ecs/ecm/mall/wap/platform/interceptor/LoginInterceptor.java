/**
 * <p>Copyright: Copyright (c) 2014</p>
 */
package com.ai.ecs.ecm.mall.wap.platform.interceptor;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberBaseVo;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.ecm.mall.wap.platform.utils.CookieUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.MemberLoginLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.wap.entity.CmnetIp;
import com.ai.ecs.ecop.wap.entity.GatewayIp;
import com.ai.ecs.ecop.wap.service.AllowIpService;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @author dyy
 * @date: 2015骞�鏈�5鏃�涓婂崍10:45:33
 * @version 1.0
 * @since JDK 1.7
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	private final static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	private String loginUrl="/login/toLogin";

	private int JEDISTIMEOUT=1800;

	@Autowired
	JedisCluster jedisCluster;

	@Autowired
	public AllowIpService allowIpService;

	@Autowired
	ILoginService loginService;
	@Autowired
	private BasicInfoQryModifyService basicInfoQryModifyService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		MemberVo member = UserUtils.getLoginUser(request);
		if (member != null) {
			Session session=UserUtils.getSession();
			jedisCluster.expire("loginUser"+session.getId(), JEDISTIMEOUT);
			return true;
		} else {
			// 查询UID
			String tKey = CookieUtils.getCookie(request,"cmccssotoken");
			logger.error("cmccssotoken:" + tKey);
			if(StringUtils.isBlank(tKey)) {
				tKey = CookieUtils.getCookie(request,"c");
				logger.error("c:" + tKey);
			}

			if (!StringUtils.isBlank(tKey)) {
				Matcher m = Pattern.compile("(.+)@.10086.cn").matcher(tKey);
				if (m.find()) {
					tKey = m.group(1);
				}
				logger.error("tKey:" + tKey);
			}

			//MemberSsoVo memberSsoVo = UserUtils.getGroupSsoVo(request, response, "6fd57aa78b484b60bcdf7c06ea1afc8b");
			// 如果集团已登陆
			if(!StringUtils.isBlank(tKey)) {
				MemberSsoVo memberSsoVo = UserUtils.getGroupSsoVo(request, response, tKey);
				if (memberSsoVo != null) {
					return true;
				}
			}
		}
		//如果没登陆 查看网关能否登陆
		if(cmWapautoLogin(request, response)){
			return true;
		}
		String url = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String requestType = request.getHeader("x-requested-with");
		Session session=UserUtils.getSession();
		logger.debug("url:{}", url);
		StringBuilder redirect = new StringBuilder();
		redirect.append(contextPath).append(loginUrl);
		StringBuilder refUrl = new StringBuilder();
		if ("XMLHttpRequest".equals(requestType)) {
			url=request.getHeader("Referer");
			if(StringUtils.isNotEmpty(url)){
				JedisClusterUtils.set("redirectUrl"+session.getId(),url,JEDISTIMEOUT);
			}
			response.setStatus(401);
			throw new Exception("登录失效，请先进行登录！");
		}else{
			if (url != null && url.indexOf(contextPath) != -1) {
				refUrl.append(url);
				Map<String, String> paramMap = getParamMap(request);
				if (MapUtils.isNotEmpty(paramMap)) {
					refUrl.append("?");
					redirect.append("?");
					int i=1;
					for (String key : paramMap.keySet()) {
						if(StringUtils.isNotEmpty(paramMap.get(key))){
							refUrl.append(key + "=");
							redirect.append(URLEncoder.encode(paramMap.get(key)
																	  .toString(), "UTF-8"));
							refUrl.append(URLEncoder.encode(paramMap.get(key).toString(), "UTF-8"));
							if(paramMap.size()>i){
								redirect.append("&");
								refUrl.append("&");
							}
						}
						i++;
					}
				}
				JedisClusterUtils.set("redirectUrl"+session.getId(),url,JEDISTIMEOUT);
			}
		}
		if(!url.contains("/broadband/broadbandHome")){
			JedisClusterUtils.set("redirectUrl"+session.getId(),refUrl.toString(),JEDISTIMEOUT);
			response.sendRedirect(redirect.toString());
			return false;
		}else{
			return true;
		}
	}

	public Map<String, String> getParamMap(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		Map<String, String[]> requestMap = request.getParameterMap();
		// if(request instanceof MultipartHttpServletRequest) {
		// MultipartHttpServletRequest multipartRequest =
		// (MultipartHttpServletRequest) request;
		// MultipartFile multipartFile =
		// multipartRequest.getFile(Constants.FILE_PARAM_NAME);
		// if(multipartFile != null) {
		// paramMap.put(Constants.FILE_PARAM_NAME+"_size",
		// Long.toString(multipartFile.getSize()));
		// }
		// }
		for (Map.Entry<String, String[]> entry : requestMap.entrySet()) {
			paramMap.put(entry.getKey(), entry.getValue()[0]);
		}
		return paramMap;
	}
	private String MOBILE="mob";
	private String BRAND="brand";
	/**
	 * 网关自动登录
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean  cmWapautoLogin(HttpServletRequest request, HttpServletResponse response) {
		String mobile =getMobile(request);
		if(StringUtil.isBlank(mobile)){
			return  false;
		}
		try {
			Session session = UserUtils.getSession();
			UserContext context = new UserContext();
			context.setClientIp(request.getRemoteAddr());
			context.setMobile(mobile);
			context.setChannelCode("E007");
			Map<String, Object> loginRes =  loginService.loginBySms(context, ""); // 商城登录，首次登录会生成账号并记录用户信息
			if(loginRes==null){
				return false;
			}
			Object obj = loginRes.get("data");
			MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
			JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);
			MemberBaseVo baseMember = new MemberBaseVo();
			baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
			baseMember.setMemberPhone(membervo.getMemberLogin().getMemberPhone());
			CookieUtils.setCookie(response,MOBILE, mobile,60*60*24);

			JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),JEDISTIMEOUT);
			JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JEDISTIMEOUT);
			getBasicInfo(request, response, mobile);
			//保存日志
			MemberLoginLogUtils.saveLog(request, "2", "0", "登录成功", null, mobile, membervo);
		} catch (Exception e) {
			logger.error("======loginError======", e);
			return false;
		}
		return  true;
	}

	private void getBasicInfo(HttpServletRequest request, HttpServletResponse response, String mobile)  {
		try {
			BasicInfoCondition condition = new BasicInfoCondition();
			condition.setSerialNumber(mobile);
			condition.setxGetMode("0");
			condition.setRunningId(System.currentTimeMillis()+"");
			condition.setStaffId("ITFWAPNN");
			condition.setTradeDepartPassword("225071");
			Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(condition);
			CookieUtils.setCookie(response,BRAND, brand(userInfo.get("BRAND")+""),60*60*24);
		}catch (Exception e){
			logger.error("getBasicInfo",e);
		}

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
	/**
	 *
	 * @todo getMobile 从wap网关获取手机号
	 * @param request
	 * @return
	 */
	private String getMobile(HttpServletRequest request) {
		String mobile = null;
		boolean matchAllowIp = false; // 获取cmwap登录判断标识
		try {
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				// 判断是否存在X-UP-CALLING-LINE-ID字段，并获取其值
				if (headerName.toUpperCase().equals("X-UP-CALLING-LINE-ID")) {
					mobile = getCallingId(request.getHeader(headerName));
				}
				//StringUtil.isNotBlank(mobile)&& 经过F5网关处理后的客户端IP地址 取客户端IP
				if (headerName.toUpperCase().equals("X-FORWARDED-FOR")) {
					String clientIp = request.getHeader(headerName);
					matchAllowIp = isForwardeId(clientIp);
				}
			}
		} catch (Exception e) {
			// 捕获手机号码获取时可能产生的异常
			logger.error("getMobile",e);
		}
		if (!matchAllowIp) {
			return null;
		}
		return mobile;
	}

	/**
	 * 取出头文件中的手机号码
	 * CALLING-LINE-
	 * @param temvit
	 * @return
	 */
	private String  getCallingId(String temvit){
		String mobile = null;
		if (temvit.substring(0, 3).trim().equals("861")){
			mobile = temvit.substring(2, 13);
		}
		if (temvit.substring(0, 4).trim().equals("+861")){
			mobile = temvit.substring(3, 14);
		}
		if (temvit.substring(0, 2).trim().equals("13")){
			mobile = temvit;
		}
		if (temvit.substring(0, 2).trim().equals("14")){
			mobile = temvit;
		}
		if (temvit.substring(0, 2).trim().equals("15")){
			mobile = temvit;
		}
		if (temvit.substring(0, 2).trim().equals("18")){
			mobile = temvit;
		}
		if (temvit.substring(0, 2).trim().equals("17")){
			mobile = temvit;
		}
		logger.info("=====X-UP-CALLING-LINE-ID:"+temvit);
		return mobile;
	}

	/**
	 * 判断用户手机获取的X-FORWARDED-FOR地址是否是wap网关地址
	 * @param clientIp
	 * @return
	 */
	private boolean isForwardeId(String clientIp){
		//有多个IP的情况下，取最后一个
		if(clientIp.contains(",") || clientIp.contains("，")){
			clientIp = clientIp.replace("，", ",");
			String[] clientIpItem = clientIp.split(",");
			clientIp=clientIpItem[clientIpItem.length-1].trim();
		}
		try {
			//判断用户手机获取的X-FORWARDED-FOR地址是否是wap网关地址
			if (isAllowIp(clientIp.trim())) {	//用户经过F5处理的IP在白名单之内,表示未伪造用户信息，符合CMWAP自登录条件
				logger.info("=========用户经过F5处理的IP在白名单之内,表示未伪造用户信息，符合CMWAP自登录条件=========");
				return true;
			} else {
				logger.info("Filter client ip failed:client ip not in write list " + clientIp);
			}
		} catch (Exception e) {
			logger.error("Filter client ip failed:" + clientIp + ":" ,e);
		}
		return false;
	}


	private boolean isAllowIp(String clientIp) throws Exception {
		CmnetIp cmnetIp = new CmnetIp();
		cmnetIp.setPublicIp(clientIp);
		CmnetIp cmnetIpInfo = allowIpService.getCmnetIpInfo(cmnetIp);
		if(cmnetIpInfo !=null){
			if (StringUtils.isNotBlank(cmnetIpInfo.getPublicIp())){
				return true;
			}
		}
		GatewayIp gatewayIp = new GatewayIp();
		gatewayIp.setPublicIp(clientIp);
		gatewayIp  = allowIpService.getGatewayIpInfo(gatewayIp);
		if(gatewayIp !=null){
			if (StringUtils.isNotBlank(gatewayIp.getPublicIp())){
				return true;
			}
		}
		return false;
	}

}
