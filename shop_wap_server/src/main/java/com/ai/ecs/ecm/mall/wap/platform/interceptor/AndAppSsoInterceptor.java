package com.ai.ecs.ecm.mall.wap.platform.interceptor;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberBaseVo;
import com.ai.ecs.ecm.mall.wap.platform.utils.*;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.UserContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import org.apache.shiro.session.Session;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.JedisCluster;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.server.UID;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 和掌柜 App登录对接
 * Created by licq on 2017/2/20.
 */

public class AndAppSsoInterceptor extends HandlerInterceptorAdapter {
	private final static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	private int JEDISTIMEOUT=1800;

	@Autowired
	JedisCluster jedisCluster;

	@Autowired
	ILoginService loginService;

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
		String credtential = request.getParameter("CREDTENTIAL");
		String signData = request.getParameter("SIGN_DATA");
		String channelCode = request.getParameter("CHANID");
		String UId =  request.getParameter("UID");


		try{
			if(UId == null) {
				if(StringUtil.isNotBlank(channelCode)){
					if("E052".equals(channelCode)){
						//是否显示头的标识，1隐藏
						request.getSession().setAttribute("isShowHead", "1");
					}else{
						request.getSession().removeAttribute("isShowHead");
					}
				}
				if(StringUtil.isNotBlank(credtential)&&
						StringUtil.isNotBlank(signData)){
					JSONObject telAndisBindCard = getMblNo(credtential, signData, secret, ssoUrl, devId);//根据参数调和包接口获取手机号
					logger.info("==============tel========" + telAndisBindCard + "==============");
					String mobile = telAndisBindCard.getString("mobile");
					boolean isBindCard = telAndisBindCard.getBoolean("isBindCard");
					String ChannelCode1 = "E007";
					if(!StringUtils.isBlank(mobile)){//校验通过
						wapLogin(mobile, isBindCard, request, response,ChannelCode1);
					}
				}
			}else{
				String ChannelCode2="E008";
				String moblie =  JedisClusterUtils.get(UId);
                if(UserUtils.getUser().getMobile() != null  ){
                          if(moblie.equals(UserUtils.getUser().getMobile())){
                          	 return false;
						  }else{

							  wapLogin(moblie, false, request, response,ChannelCode2);
							  JedisClusterShopUtils.del(UId);
						  }
				}else{
					wapLogin(moblie, false, request, response,ChannelCode2);
					JedisClusterShopUtils.del(UId);

				}

			}
		}catch (Exception e){
			logger.error("===ANDAPPSSO————preHandle===",e);
		}
		return super.preHandle(request, response, handler);
	}



	private void wapLogin(String mobile, boolean isBindCard, HttpServletRequest request,HttpServletResponse response ,String ChannelCode){
		try {
			Session session = UserUtils.getSession();
			String userKey="loginUser" + session.getId();
			//当前登录号码是否为传入手机号
			if(JedisClusterUtils.exists(userKey)){
				MemberVo m = (MemberVo) JedisClusterUtils.getObject(userKey);
				if(m.getMemberLogin()!=null && mobile.equals(m.getMemberLogin().getMemberLogingName())){
					String baseUserKey = "LOGIN_BASEINFO_" + session.getId();
					String str = JedisClusterUtils.get(baseUserKey);
					JSONObject jsonObject = JSONObject.parseObject(str);
					Boolean bindCard = jsonObject.getBoolean("bindCard");

					if (bindCard == null) {
						bindCard = Boolean.FALSE;
					}

					// 如果用户已绑卡， 或解除绑卡更新session状态
					if (isBindCard != bindCard.booleanValue()) {
						jsonObject.put("bindCard", isBindCard);
						JedisClusterUtils.set(baseUserKey, jsonObject.toJSONString(), JEDISTIMEOUT);
					}
					return ;
				}else{
					//退出登陆
					loginOut(request,response);
				}
			}
			UserContext context = new UserContext();
			context.setClientIp(StringUtils.getRemoteAddr(request));
			context.setMobile(mobile);
			context.setChannelCode(ChannelCode);
			Map<String, Object> loginRes =  loginService.loginBySms(context, ""); // 商城登录，首次登录会生成账号并记录用户信息
			if(loginRes==null){
				return ;
			}
			Object obj = loginRes.get("data");
			MemberVo membervo = JSONObject.parseObject(JSONObject.toJSONString(obj), MemberVo.class);
			membervo.getMemberLogin().setMemberPhone(Long.parseLong(mobile));
			JedisClusterUtils.setObject("loginUser" + session.getId(), membervo, JEDISTIMEOUT);
			MemberBaseVo baseMember = new MemberBaseVo();
			baseMember.setMemberId(membervo.getMemberLogin().getMemberId());
			baseMember.setMemberPhone(Long.parseLong(mobile));
			baseMember.setLoginFromHeApp(true);  // 是否从和包登陆
			baseMember.setBindCard(isBindCard);
			JedisClusterUtils.set("LOGIN_BASEINFO_" + session.getId(), JSON.toJSONString(baseMember),JEDISTIMEOUT);
			JedisClusterUtils.set("sessionId" + session.getId(), session.getId() + "", JEDISTIMEOUT);
			//保存日志
			MemberLoginLogUtils.saveLog(request, "2", "0", "登录成功", null, mobile, membervo);
		} catch (Exception e) {
			logger.error("======loginError======", e);
		}
	}



	public static JSONObject getMblNo(String credtential, String signData, String secret, String url, String devId){
		//传入的参数为链接中的 CREDTENTIAL参数，SIGN_DATA参数，分配的秘钥，接口 地址，开发者 ID
		String xml=buildConfirmXmlMessage(credtential,signData,secret,devId);
		String respXml=sendRequest(xml,url);
		JSONObject returnValue = new JSONObject();
		try{
			Document document= DocumentHelper.parseText(respXml);
			Element root=document.getRootElement();
			if("000000".equals(root.element("HEAD").element("RSPCD").getText())){
				returnValue.put("mobile", root.element("BODY").element("MBL_NO").getText());
				boolean isBindCard = false;
				if (root.element("BODY").element("USRCRDSTS") != null) {
					String sBindCard = root.element("BODY").element("USRCRDSTS").getText();
					if ("1".equals(sBindCard)) {
						isBindCard = true;
					} else {
						isBindCard = false;
					}
				}
				returnValue.put("isBindCard", isBindCard);
			}
		}catch(DocumentException e){
			logger.error("========getMblNo=====",e);
		}
		return returnValue;
	}


	/**
	 * 发送请求
	 */
	private static String sendRequest(String reqData,String url)   {
		StringWriter writer=new StringWriter();
		OutputStreamWriter osw=null;
		try{
			URL reqURL=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)reqURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept","*/*");
			conn.setRequestProperty("User-Agent","stargate");
			conn.setRequestProperty("Content-Type","application/json");
			osw=new OutputStreamWriter(conn.getOutputStream(),"utf-8");
			osw.write(reqData);
			osw.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			char[] chars=new char[256];
			int count=0;
			while((count=br.read(chars))>0){
				writer.write(chars,0,count);
			}
		}catch(Exception e){
			logger.error("=========sendRequest======",e);
			e.printStackTrace();
		}finally{
			try {
				if (osw != null) {
					osw.close();
				}
			} catch (IOException e) {
				logger.error("=====close file error =========",e);
			}
		}
		return writer.toString();
	}
	/**
	 * 组装 XML 报文 ,credtential 待验签数据 signData 验签密文 secret 接入者秘钥
	 *
	 * @paramcredtential
	 * @paramsignData
	 * @paramsecret
	 * @paramdevId
	 * @return
	 */
	private static String buildConfirmXmlMessage(String credtential, String signData, String secret,String devId){
		// 使用 dom4j 组装 xml
		Document document=DocumentHelper.createDocument();
		Element root=document.addElement("ROOT");
		Element head=root.addElement("HEAD");
		Element body=root.addElement("BODY");
		//HEAD 部分
		head.addElement("TXNCD").setText("2208000");
		head.addElement("MBLNO").setText("");
		head.addElement("SESSIONID").setText("");
		head.addElement("PLAT").setText("99");
		head.addElement("UA").setText("default");
		head.addElement("VERSION").setText("default");
		head.addElement("PLUGINVER").setText("");
		head.addElement("NETTYPE").setText("");
		head.addElement("MCID").setText("default");
		head.addElement("MCA").setText("default");
		head.addElement("IMEI").setText("default");
		head.addElement("IMSI").setText("default");
		head.addElement("SOURCE").setText("default");
		head.addElement("DEVID").setText(devId);
		Date currentTime=new Date();
		SimpleDateFormat df=new SimpleDateFormat("HHmmss");
		String serlno=df.format(currentTime);
		head.addElement("SERLNO").setText(serlno);
		//BODY 部分
		body.addElement("CREDTENTIAL").setText(credtential);
		body.addElement("SIGN_DATA").setText(signData);
		body.addElement("SIGN_TYPE").setText("MD5");
		// 获取没有头声明的 xml
		String reqDate=root.asXML();
		// 去掉 reqDate 中的 root 标签来进行加密签名
		reqDate=reqDate.substring(0,reqDate.length()-7);
		reqDate=reqDate.substring(6,reqDate.length());
		String signature=signature(reqDate,secret);
		// 把签名出来的结果组装到 xml 中
		root.addElement("SIGNATURE").setText(signature);
		String xml=null;
		XMLWriter writer=null;
		ByteArrayOutputStream baos=null;
		try{
				OutputFormat format=OutputFormat.createCompactFormat();
				format.setIndent(false);
				format.setNewlines(false);
				format.setLineSeparator("");
				baos=new ByteArrayOutputStream();
				writer=new XMLWriter(baos,format);
				writer.write(document);
				xml=baos.toString("utf-8");
				return xml;
		}catch(Exception e){
			logger.error("=== buildConfirmXmlMessage==close file error =========",e);
		}finally{
			try {
				writer.close();
				baos.close();
			} catch (IOException e) {
				logger.error("=====buildConfirmXmlMessage =========",e);
			}
		}
		return xml;
	}
	/**
	 * signature 加密 reqDate 加密数据 secret 加密 key
	 * @paramreqDate
	 * @paramsecret
	 * @return
	 */
	private static String signature(String reqDate,String secret){
		String hashAlgorithmName="HmacSHA1";
		String appSecret=secret;
		String requestData=reqDate;
		SecretKeySpec spec=new SecretKeySpec(appSecret.getBytes(),hashAlgorithmName);
		Mac mac;
		String Signature="";
		try{
			mac=Mac.getInstance(hashAlgorithmName);
			mac.init(spec);
			byte[] bytes=mac.doFinal(requestData.getBytes());
			Signature=new String((new BASE64Encoder()).encodeBuffer(bytes));
		}catch(NoSuchAlgorithmException |InvalidKeyException e){
			logger.error("=== signatureerror =========",e);
		}
		return Signature;
	}


	/**
	 * 退出登陆
	 * @param request
	 * @param response
	 */
	public void loginOut(HttpServletRequest request, HttpServletResponse response)   {
		String logoutUrl = ssoServerHost + "ecsuc/remote/userServer/userLogout";
		Session session = UserUtils.getSession();
		String tkey = CookieUtils.getCookie(request, "ticketId"); // 从COOKIE获取用户登录令牌信息
		String uId = CookieUtils.getCookie(request, "uId");
		try {
			if (StringUtil.isNotBlank(tkey) && StringUtil.isNotBlank(uId)) { // 令牌不为空，则调用单点登录中心登出
				Map<String, String[]> paramMap = new HashMap<String, String[]>();
				paramMap.put("ticketId", new String[] { tkey });
				paramMap.put("uId", new String[] { uId });
				paramMap.put("channelID", new String[] { channelID });
				String strres = HttpClientUtils.doPostAndGetString(logoutUrl, paramMap);
//				JSONObject jsonRes = JSONObject.parseObject(strres);
//				Long code = jsonRes.getLong("code");
//				if (code == 200) { // 登出成功
					CookieUtils.setCookie(response, "ticketId", "", -1);
					CookieUtils.setCookie(response, "uid", "", -1);
//				}
			}
			JedisClusterUtils.delObject("loginUser" + session.getId());
			JedisClusterUtils.delObject("LOGIN_BASEINFO_" + session.getId());
			JedisClusterUtils.del("sessionId" + session.getId());
			UserUtils.getSubject().logout();
		} catch (Exception e) {
			logger.error("========loginOut-------====",e);
		}
	}

}
