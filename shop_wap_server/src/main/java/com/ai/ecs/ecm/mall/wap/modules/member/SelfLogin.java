package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.ecs.common.config.Global;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.platform.utils.InvokeEcop;
import com.ai.ecs.ecop.wap.entity.CmnetIp;
import com.ai.ecs.ecop.wap.entity.GatewayIp;
import com.ai.ecs.ecsite.modules.myMobile.entity.BasicInfoCondition;
import com.ai.ecs.ecsite.modules.myMobile.service.BasicInfoQryModifyService;
@Component
public class SelfLogin {

	@Autowired
	private BasicInfoQryModifyService basicInfoQryModifyService;
	
	@Autowired
	public InvokeEcop invokeEcop;
	
	private static final org.slf4j.Logger logger = LoggerFactory
	            .getLogger(SelfLogin.class);
	
	protected static Map<String, String> statusMap = new HashMap<String, String>();
	static{
		statusMap.put("1", "主动预约销号");
		statusMap.put("2", "预约销号");
		statusMap.put("3", "欠费预销号");
		statusMap.put("4", "欠费销户");
		statusMap.put("5", "开户返销");
		statusMap.put("6", "过户注销");
		statusMap.put("7", "无状态数据");
	}
	
	/**
	 * 
	 * getSelfLoginFlag  自登录
	 * @param request
	 * @param serialNumber
	 * @return
	 * @return boolean返回说明
	 * @Exception 异常说明
	 * @author：yangxl5@asiainfo.com
	 * @create：2016-3-11 上午10:23:35 
	 * @moduser： 
	 * @moddate：
	 * @remark：
	 */
	public boolean selfLogin(HttpServletRequest request,String serialNumber){
		String startTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		boolean loginFlag = false;
		String loginType = "4";//3-wap自登录，4-其他渠道自登录
    	try {
			String LOGIN_SDATE = DateUtils.getTime("yyyyMMdd HH:mm:ss"); //记录登录开始时间
			if(StringUtils.isBlank(serialNumber)){
				loginType = "3";
				serialNumber = this.getMobile(request); //CMWAP获取手机号码
				logger.info("===============CMWAP获取手机号码" + serialNumber +"==============");
			}
			//获取到手机号码，进行自登录操作
			if (StringUtils.isNotBlank(serialNumber)) {
				//自动登录
				loginFlag = autoLogin(request,serialNumber,LOGIN_SDATE);
				writerLogging(request,startTime,loginFlag,loginType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return loginFlag;
    }
	
	/**
	 * 
	 * getMobile 从wap网关获取手机号
	 * @param request
	 * @return
	 * @return String返回说明
	 * @Exception 异常说明
	 * @author：yangxl5@asiainfo.com
	 * @create：2016-3-11 上午10:23:01 
	 * @moduser： 
	 * @moddate：
	 * @remark：
	 */
    public String getMobile(HttpServletRequest request) {
		String mobile = null;
		boolean matchAllowIp = false; // 获取cmwap登录判断标识
		
		try {
			String temvit = "";
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				logger.info((new StringBuilder("=====headerName:")).append(headerName).toString());
				logger.info((new StringBuilder("=====headerName_INFOMATION:")).append(request.getHeader(headerName)).toString());
				
				// 判断是否存在X-UP-CALLING-LINE-ID字段，并获取其值
				if (headerName.toUpperCase().equals("X-UP-CALLING-LINE-ID")) {
					temvit = request.getHeader(headerName);
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
					request.getSession().setAttribute("temvit", temvit);
				}
				//取客户端IP
				//经过F5网关处理后的客户端IP地址
				if (headerName.toUpperCase().equals("X-FORWARDED-FOR")) {
					String clientIp = request.getHeader(headerName);
					request.getSession().setAttribute("clientIp", clientIp);
					//有多个IP的情况下，取最后一个
					if(clientIp.contains(",") || clientIp.contains("，")){
						clientIp = clientIp.replace("，", ",");
						String[] clientIpItem = clientIp.split(",");
						clientIp=clientIpItem[clientIpItem.length-1].trim();						
					}
					
					try {
						Map data = new HashMap();
						clientIp = clientIp.trim();
						data.put("clientIp", "117.136.88.231");
						
						//判断用户手机获取的X-FORWARDED-FOR地址是否是wap网关地址
						if (isAllowIp(request,data)) {
							//用户经过F5处理的IP在白名单之内,表示未伪造用户信息，符合CMWAP自登录条件   
							matchAllowIp = true;
							logger.info("=========用户经过F5处理的IP在白名单之内,表示未伪造用户信息，符合CMWAP自登录条件=========");
						} else {
							logger.info("Filter client ip failed:client ip not in write list " + clientIp);
						}
						
					} catch (Exception e) {
						logger.info("Filter client ip failed:" + clientIp + ":"+ e);
					}
				}
			}
		} catch (Exception e) {
			// 捕获手机号码获取时可能产生的异常
			logger.info(e.getMessage());
		}
		if (!matchAllowIp) {
			return null;
		}
		return mobile;
	}
    
    
	public boolean isAllowIp(HttpServletRequest request,Map data) throws Exception {
		String dataId = data.get("clientIp").toString(); // ID
		CmnetIp cmnetIpInfo = invokeEcop.getCmnetIpInfo(dataId);
		if(cmnetIpInfo !=null){
			if (StringUtils.isNotBlank(cmnetIpInfo.getPublicIp())){
				return true;
			}
		}
		GatewayIp gatewayIp = invokeEcop.getGatewayIpInfo(dataId);
		if(gatewayIp !=null){
			if (StringUtils.isNotBlank(gatewayIp.getPublicIp())){
				return true;
			}
		}
		return false;
	}
    
    /**
     * 
     * autoLogin 自登录
     * @param request
     * @param serialNumber
     * @param LOGIN_SDATE
     * @return
     * @throws Exception
     * @return boolean返回说明
     * @Exception 异常说明
     * @author：yangxl5@asiainfo.com
     * @create：2016-3-11 上午10:22:15 
     * @moduser： 
     * @moddate：
     * @remark：
     */
    public boolean autoLogin(HttpServletRequest request,String serialNumber,String LOGIN_SDATE) throws Exception{
    	boolean loginFlag = false;
		// 不校验用户密码，提取用户信息
    	BasicInfoCondition condition = new BasicInfoCondition();
    	condition.setSerialNumber(serialNumber);
    	condition.setxGetMode("0");
    	Map userInfo = basicInfoQryModifyService.queryUserBasicInfo(condition);
		if(MapUtils.isNotEmpty(userInfo) && HNanConstant.SUCCESS.equals(userInfo.get("respCode"))){
			Map userBasicInfo = JSONObject.fromObject(userInfo.get("result").toString().replace("[", "").replace("]", ""));
			loginFlag = userInfoAction(request,userBasicInfo,serialNumber,"0");
		}
		return loginFlag;
	}
    
    public void writerLogging(HttpServletRequest request,String startTime,boolean loginFlag,String loginType) throws Exception{
		try{
			String runningId = DateUtils.getDate("yyyyMMddHHmmss")+(int)((Math.random()*9+1)*100000);
			Logger logger = LoggerFactory.getLogger("business_web_log");
			String sessionId = request.getSession().getId();
			Map userInfo = JedisClusterUtils.getMap(sessionId+"WAP_USERINFO");
			String custName = "";
			String brand = "";
			String brandCode = "";
			String epachyCode = "";
			String serialNumber = "";
			if(null != userInfo){
				if(null != userInfo.get("userName")){
					custName = userInfo.get("userName").toString();
				}
				if(null != userInfo.get("brand")){
					brand = userInfo.get("brand").toString();
				}
				if(null != userInfo.get("brandCode")){
					brandCode = userInfo.get("brandCode").toString();
				}
				if(null != userInfo.get("epachyCode")){
					epachyCode = userInfo.get("epachyCode").toString();
				}
				if(null != userInfo.get("serialNumber")){
					serialNumber = userInfo.get("serialNumber").toString();
				}
			}
			String resultCode = "";
			if(loginFlag){
				resultCode = "0";
			}else{
				resultCode = "-1";
			}
			String chanId = "";
			String rsrv2 = "";//rsrv1字段记录自登录方式下请求头的ip、手机号等信息
			if("3".equals(loginType)){
				chanId = Global.getConfig("ChanId");
				rsrv2 = "serialNumber="+request.getSession().getAttribute("temvit")+"&clientIp="+request.getSession().getAttribute("clientIp");
			}else{
				chanId = request.getParameter("CHANID");
			}
			String staffId =  Global.getConfig("staffId");
			String operType = "LOGIN";
			String goodsName = "自登录";
			
			String goodsId = "";
			String goodsPrice = "";
			String endTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
			
			String sourceChanId = "";
			String sourceShopId="";
			String sourceStaffId = "";
			if(StringUtils.isNoneBlank(request.getParameter("sourceChannelId"))){
				sourceChanId = request.getParameter("sourceChannelId");
			}
			if(StringUtils.isNoneBlank(request.getParameter("sourceShopId"))){
				sourceShopId = request.getParameter("sourceShopId");
			}
			if(StringUtils.isNoneBlank(request.getParameter("sourceStaffId"))){
				sourceStaffId = request.getParameter("sourceStaffId");
			}
			String touchId = "";
			String requestIp = request.getSession().getAttribute("clientIp").toString();
			String operResultInfo = "";
//			Map requestMap = request.getParameterMap();
			String reqParam = "";
//			if(null != requestMap){
//				reqParam = requestMap.toString();
//			}
			
			String rsrv1 = sourceShopId;
			String rsrv3 = "";
			String rsrv4 = "";
			logger.info(genLogStr(sourceChanId,touchId,requestIp,staffId,sourceStaffId,reqParam,operResultInfo,
				runningId,serialNumber,custName,chanId,operType,goodsName,brand,brandCode,goodsId,sessionId,
				epachyCode,goodsPrice,startTime,endTime,resultCode,operResultInfo,rsrv1,rsrv2,rsrv3,rsrv4));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    
    private String genLogStr(String ... args){
		StringBuffer sb = new StringBuffer();
		int i=0;
		for(String str : args){
			if(i == 0){
				sb.append(str);
			}else{
				sb.append("@;;@").append(str);
			}
			i++;
		}
		return sb.toString();
	}
    
    public boolean userInfoAction(HttpServletRequest request,Map userInfo,String serialNumber,String loginType)throws Exception{
    	boolean loginFlag = false;
		Map returnInfo = new HashMap();
		if(!statusMap.containsKey(userInfo.get("REMOVE_TAG").toString())){
			Map<String,String> userInfos = new HashMap<String,String>();
			//这里记录:是否登录
			userInfos.put("isValidate","true");
			//这里记录:登录方式
			userInfos.put("loginType", loginType);
			//这里记录：手机号
			userInfos.put("serialNumber",serialNumber);
	    	//这里记录：用户名
			userInfos.put("userName",userInfo.get("CUST_NAME").toString());
			//这里记录：品牌名
			userInfos.put("brand",userInfo.get("BRAND").toString());
			//这里记录：品牌编号
			userInfos.put("brandCode",userInfo.get("BRAND_CODE").toString());
			//这里记录：地市名
			userInfos.put("epachyName",userInfo.get("X_EPARCHY_NAME").toString());
			//这里记录：地市编码
			userInfos.put("epachyCode",userInfo.get("EPARCHY_CODE").toString());
			//用户预付费标志
			userInfos.put("prpayTag",userInfo.get("PREPAY_TAG").toString());
			//用户预付费标志名称
			userInfos.put("prpayTagName",userInfo.get("X_PREPAY_TAG_NAME").toString());
			//这里记录:登录时间
			userInfos.put("loginDate",DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			//用户主套餐id
			userInfos.put("productId",userInfo.get("PRODUCT_ID").toString());
			//这里记录：用户主套餐
			userInfos.put("productName",userInfo.get("PRODUCT_NAME").toString());
			//这里记录用户星级
			userInfos.put("creditClass",userInfo.get("CREDIT_CLASS").toString());
			
			HttpSession session = request.getSession();
			JedisClusterUtils.setMap(session.getId()+"WAP_USERINFO", userInfos,1800);
			loginFlag = true;
		}
		return loginFlag;
	}
    
}
