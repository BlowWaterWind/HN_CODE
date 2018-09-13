/**
 */
package com.ai.ecs.ecm.mall.wap.common.security;

import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.Base64;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单验证（包含验证码）过滤类
 * @author ThinkGem
 * @version 2014-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";
	public static final String DEFAULT_USER_TYPE_PARAM = "userType";
	public static final String DEFAULT_LOGIN_MODE_PARAM = "loginMode";
	public static final String DEFAULT_CHECK_TYPE_PARAM = "checkType"; /**验证方式: 0=服务密码,1=登录密码, 2=短信密码 */

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
	private String messageParam = DEFAULT_MESSAGE_PARAM;
    private String userTypeParam = DEFAULT_USER_TYPE_PARAM;
    private String loginModeParam = DEFAULT_LOGIN_MODE_PARAM;

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
        String userType= WebUtils.getCleanParam(request, userTypeParam);
        String loginMode= WebUtils.getCleanParam(request, loginModeParam);
        String phoneId=WebUtils.getCleanParam(request,"phoneId"); //手机号吗
        String servicePwd=WebUtils.getCleanParam(request,"servicePwd"); //服务密码
        String smsPwd=WebUtils.getCleanParam(request,"smsPwd");//短信密码
        String checkType=WebUtils.getCleanParam(request,DEFAULT_CHECK_TYPE_PARAM); /**验证方式: 4=服务密码,1=登录密码, 2=短信密码 */
        Map<String ,String> paramMap=new HashMap<String, String>(); // 登录页面参数集
        paramMap.put("phoneId",phoneId);
        paramMap.put("servicePwd",servicePwd);
        paramMap.put("smsPwd",smsPwd);
        paramMap.put("userType",userType);
        paramMap.put("loginMode",loginMode);
        paramMap.put("checkType",checkType);
        password = new String(Base64.decode(password));//base64 解密
		if (password==null){
			password = "";
		}

		boolean rememberMe = isRememberMe(request);
		String host = getRemoteAddr((HttpServletRequest) request);
		String captcha = getCaptcha(request);
		boolean mobile = isMobileLogin(request);
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile,paramMap);
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (StringUtils.isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	
	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	public String getMobileLoginParam() {
		return mobileLoginParam;
	}
	
	protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }
	
	public String getMessageParam() {
		return messageParam;
	}





    /**
	 * 登录成功之后跳转URL
	 */
	public String getSuccessUrl() {
		return super.getSuccessUrl();
	}
	
	@Override
	protected void issueSuccessRedirect(ServletRequest request,
			ServletResponse response) throws Exception {
//			 WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
//			super.issueSuccessRedirect(request, response);
        request.setAttribute(getMessageParam(), "success");
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request, ServletResponse response) {
		String className = e.getClass().getName(), message = "";
		if (IncorrectCredentialsException.class.getName().equals(className)
				|| UnknownAccountException.class.getName().equals(className)){
			message = "用户或密码错误, 请重试.";
		}
		else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")){
			message = StringUtils.replace(e.getMessage(), "msg:", "");
		}
		else{
			message = "系统出现问题，请稍后再试！";
			e.printStackTrace(); // 输出到控制台
		}
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
	}
	
}