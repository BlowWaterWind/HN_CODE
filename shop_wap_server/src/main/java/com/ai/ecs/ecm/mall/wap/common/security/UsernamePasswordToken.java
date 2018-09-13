/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.ai.ecs.ecm.mall.wap.common.security;

import java.util.Map;

/**
 * 用户和密码（包含验证码）令牌类
 * @author ThinkGem
 * @version 2013-5-19
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;
	private boolean mobileLogin;
    private String userType; // 1 注册用户登录  4 手机用户登录
    private String phoneId; //手机号码
    private String servicePwd; //服务密码
    private String smsPwd; //短信验证码
    private String checkType; /**验证方式: 0=服务密码,1=登录密码, 2=短信密码 */
    private String loginMode;//登录模式 0 服务密码 2 短信密码

    public String getLoginMode() {
        return loginMode;
    }


	
	public UsernamePasswordToken() {
		super();
	}

	public UsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha, boolean mobileLogin,Map<String,String> param) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.mobileLogin = mobileLogin;
        this.userType = param.get("userType");
        this.loginMode=param.get("loginMode");
        this.phoneId=param.get("phoneId");
        this.servicePwd=param.get("servicePwd");
        this.smsPwd=param.get("smsPwd");
        this.checkType=param.get("checkType");

	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public boolean isMobileLogin() {
		return mobileLogin;
	}

    public String getUserType() {
        return userType;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public String getServicePwd() {
        return servicePwd;
    }

    public String getSmsPwd() {
        return smsPwd;
    }

    public String getCheckType() {
        return checkType;
    }
}