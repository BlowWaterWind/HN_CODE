/**
 * 项目名称：shop_wap_server 
 * 文件名：WechatUtil.java
 * 版本信息：
 * 日期：2016年11月8日
 * Copyright asiainfo Corporation 2016
 * 版权所有 *
 */
package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.ai.ecs.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.qq.connect.utils.RandomStatusGenerator;

/**
 * WechatUtil
 * @author：huangyi7@asiainfo.com
 * @Sep：2016年11月8日 下午2:56:51
 * @version 1.0
 */
public class WechatUtil {

	// appid和appSecret 是在公众平台上申请的
	// AppId
	public static final String WX_APP_ID = "";

	// AppSecret
	public static final String WX_APP_KEY = "";

	// 回调地址
	public static final String WX_REDIRECT_URI = "";

	/**
	 * getRequestCodeUrl 方法作用：获取code
	 * @param request
	 * @return
	 * @throws Exception
	 * @return String返回说明
	 * @Exception 异常说明
	 * @author：huangyi7@asiainfo.com
	 * @create：2016年11月8日 下午2:57:26
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static String getRequestCodeUrl(ServletRequest request) throws Exception {

		String state = RandomStatusGenerator.getUniqueState();
		((HttpServletRequest) request).getSession().setAttribute("weixin_connect_state", state);
		return String
			.format(
				"https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
				WX_APP_ID, WX_REDIRECT_URI, "snsapi_userinfo", state);
	}

	/**
	 * getUserInfoAccessToken 方法作用：获取请求用户信息的access_token
	 * @param code
	 * @return
	 * @throws Exception
	 * @return Map<String,String>返回说明
	 * @Exception 异常说明
	 * @author：huangyi7@asiainfo.com
	 * @create：2016年11月8日 下午2:58:07
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static Map<String, String> getUserInfoAccessToken(String code) throws Exception {

		JSONObject object = null;
		Map<String, String> data = new HashMap<String, String>();

		String url = String
			.format(
				"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
				WX_APP_ID, WX_APP_KEY, code);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		String tokens = EntityUtils.toString(httpEntity, "utf-8");
		object = JSONObject.parseObject(tokens);
		String errcode = object.getString("errcode");
		if (!StringUtils.isEmpty(errcode)) {
			return null;
		}
		String openId = object.getString("openid");
		if (StringUtils.isEmpty(openId)) {
			return null;
		}
		data.put("openid", openId.replaceAll("\"", ""));
		data.put("access_token", object.getString("access_token").replaceAll("\"", ""));
		data.put("unionid", object.getString("unionid").replaceAll("\"", ""));

		return data;
	}

	/**
	 * getUserInfo 方法作用：获取用户信息
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws Exception
	 * @return Map<String,String>返回说明
	 * @Exception 异常说明
	 * @author：huangyi7@asiainfo.com
	 * @create：2016年11月8日 下午2:58:49
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static Map<String, String> getUserInfo(String accessToken, String openId) throws Exception {

		Map<String, String> data = new HashMap<String, String>();
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid="
			+ openId + "&lang=zh_CN";
		JSONObject userInfo = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		String response = EntityUtils.toString(httpEntity, "utf-8");
		userInfo = JSONObject.parseObject(response);
		String errcode = userInfo.getString("errcode");
		if (!StringUtils.isEmpty(errcode)) {
			return null;
		}
		String openId2 = userInfo.getString("openid");
		if (StringUtils.isEmpty(openId2)) {
			return null;
		}
		data.put("openid", openId2.replaceAll("\"", ""));
		data.put("nickname", userInfo.getString("nickname").replaceAll("\"", ""));
		return data;
	}
}
