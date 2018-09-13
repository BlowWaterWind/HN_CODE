/**
 * 项目名称：
 * 文件名：
 * 版本信息：
 * 日期：2018年1月29日
 * Copyright asiainfo Corporation 2016
 * 版权所有 *
 */
package com.ai.ecs.ecm.mall.wap.platform.utils;

import com.ai.ecs.common.utils.DateUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 商城日志
 * 将日志输出到elk平台
 * @version 1.0
 */
public class LogUtils {
	
	/**
	 * 日志字段长度
	 */
	private static final int LOG_FIELD_MAX_LENGTH = 5000;
	
	/**
	 * 对象转换为json时，多余的page对象
	 */
	private static final String JSON_PAGE = ",\"page\":{\"count\":0,\"disabled\":true,\"first\":0,\"firstPage\":false,\"firstResult\":0,\"funcName\":\"page\",\"funcParam\":\"\",\"html\":\"<ul>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(0,-1,'');\\\">&#171; 上一页<\\/a><\\/li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(0,-1,'');\\\">1<\\/a><\\/li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(0,-1,'');\\\">下一页 &#187;<\\/a><\\/li>\\n<li class=\\\"disabled controls\\\"><a href=\\\"javascript:\\\">当前 <input type=\\\"text\\\" value=\\\"1\\\" onkeypress=\\\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)page(this.value,-1,'');\\\" onclick=\\\"this.select();\\\"/> / <input type=\\\"text\\\" value=\\\"-1\\\" onkeypress=\\\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)page(1,this.value,'');\\\" onclick=\\\"this.select();\\\"/> 条，共 0 条<\\/a><\\/li>\\n<\\/ul>\\n<div style=\\\"clear:both;\\\"><\\/div>\",\"last\":0,\"lastPage\":false,\"length\":8,\"list\":[],\"map\":null,\"maxResults\":-1,\"message\":\"\",\"next\":2,\"notCount\":false,\"orderBy\":\"\",\"pageNo\":1,\"pageSize\":-1,\"prev\":0,\"slider\":1,\"totalPage\":0}";
	private static final String JSON_PAGE2 =",\"page\":{\"count\":0,\"disabled\":true,\"first\":0,\"firstPage\":false,\"firstResult\":0,\"funcName\":\"page\",\"funcParam\":\"\",\"html\":\"<ul>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(0,-1,'');\\\">&#171; 上一页</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(0,-1,'');\\\">1</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(0,-1,'');\\\">下一页 &#187;</a></li>\\n<li class=\\\"disabled controls\\\"><a href=\\\"javascript:\\\">当前 <input type=\\\"text\\\" value=\\\"1\\\" onkeypress=\\\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)page(this.value,-1,'');\\\" onclick=\\\"this.select();\\\"/> / <input type=\\\"text\\\" value=\\\"-1\\\" onkeypress=\\\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)page(1,this.value,'');\\\" onclick=\\\"this.select();\\\"/> 条，共 0 条</a></li>\\n</ul>\\n<div style=\\\"clear:both;\\\"></div>\",\"last\":0,\"lastPage\":false,\"length\":8,\"list\":[],\"maxResults\":-1,\"message\":\"\",\"next\":2,\"notCount\":false,\"orderBy\":\"\",\"pageNo\":1,\"pageSize\":-1,\"prev\":0,\"slider\":1,\"totalPage\":0}";

	/**
	 * 流程日志记录器
	 */
	private static final Logger FLOW_LOG = LoggerFactory.getLogger("flowLog");
	
	/**
	 * 日期格式
	 */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	/**
	 * 系统名称
	 */
	private static final String SYS_NAME = "WAP商城";
	
	/**
	 * writerFlowLogEnterMenthod 方法作用：写入流程日志进入方法
	 * @param streamNo 流水号
	 * @param loginName 登陆名
	 * @param orderSubNo 订单编号(order_sub_no)
	 * @param className 类名
	 * @param methodName 方法名称,例如：SSOLogin(HttpServletRequest request,HttpServletResponse response)
	 * @param requestParam 方法请求参数:如果请求参数只为 request和response，则可以不记录
	 * @param desc 描述,记录其他说明或者异常堆栈信息
	 * @return void返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static void writerFlowLogEnterMenthod(String streamNo,String orderSubNo,String loginName,String className,String methodName,Object requestParam,String desc,HttpServletRequest request){
		writerFlowLog(streamNo,orderSubNo, loginName, className, methodName, "Enter", requestParam, null, "", desc, request.getRequestURI(), getIpAddress(request), "", "");
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = getClientIp(request, "X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = getClientIp(request, "PROXY-CLIENT-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = getClientIp(request, "WL-PROXY-CLIENT-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = getClientIp(request, "HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = getClientIp(request, "HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}

		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	/**
	 * getClientIp 方法作用:获取客户端IP
	 * @param request
	 * @param upperCaseHeaderName 大写的头字段名
	 * @return
	 * @return String返回说明
	 * @Exception 异常说明
	 * @author：daijun@asiainfo.com
	 * @create：2017年12月28日 上午8:46:57
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	private static String getClientIp(HttpServletRequest request,String upperCaseHeaderName) {
		String clientIp="";
		try {
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				//取客户端IP
				//经过F5网关处理后的客户端IP地址
				if (headerName.toUpperCase().equals(upperCaseHeaderName)) {
					clientIp= request.getHeader(headerName);

					//有多个IP的情况下，取最后一个
					if(clientIp.contains(",") || clientIp.contains("，")){
						clientIp = clientIp.replace("，", ",");
						String[] clientIpItem = clientIp.split(",");
						clientIp=clientIpItem[clientIpItem.length-1].trim();
					}
				}
			}
		} catch (Exception e) {
			// 捕获手机号码获取时可能产生的异常
			e.printStackTrace();
		}

		return clientIp;
	}
	/**
	 * writerFlowLogExitMenthod 方法作用：写入流程日志退出方法
	 * @param streamNo 流水号
	 * @param loginName 登陆名
	 * @param className 类名
	 * @param methodName 方法名称,例如：SSOLogin(HttpServletRequest request,HttpServletResponse response)
	 * @param responseResult 响应结果
	 * @param desc 描述，记录其他说明或者异常堆栈信息
	 * @return void返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static void writerFlowLogExitMenthod(String streamNo,String orderSubNo,String loginName,String className,String methodName,Object responseResult,String desc){
		writerFlowLog(streamNo, orderSubNo,loginName, className, methodName, "Exit", null, responseResult, "", desc, "", "", "", "");
	}

	public static void writerFlowLogExitMenthod(String streamNo,String orderSubNo,String loginName,String className,String methodName,Object requestParam,Object responseResult,String desc){
		writerFlowLog(streamNo, orderSubNo,loginName, className, methodName, "Exit", requestParam, responseResult, "", desc, "", "", "", "");
	}

	/**
	 * writerFlowLogConditionAndReturn 方法作用：写入流程日志条件分支并退出方法
	 * @param streamNo 流水号
	 * @param loginName 登陆名
	 * @param className 类名
	 * @param methodName 方法名称,例如：SSOLogin(HttpServletRequest request,HttpServletResponse response)
	 * @param responseResult 响应结果,如果涉及到条件中，直接结束响应的，将响应进行记录
	 * @param desc 描述，记录其他说明或者异常堆栈信息
	 * @param requestParam 请求参数,当为条件时，请求参数为对比信息
	 * @param condition 条件分支字符串，例如：if(xxxxxx==xxxxx)  如果是  else 的 直接记录 else
	 * @return void返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static void writerFlowLogConditionAndReturn(String streamNo,String orderSubNo,String loginName,String className,String methodName,Object responseResult,String desc,Object requestParam,String condition){
		writerFlowLog(streamNo,orderSubNo, loginName, className, methodName, "ConditionAndReturn", requestParam, responseResult, condition, desc, "", "", "", "");
	}
	
	/**
	 * writerFlowLogCondition 方法作用：写入流程日志条件分支
	 * @param streamNo 流水号
	 * @param loginName 登陆名
	 * @param className 类名
	 * @param methodName 方法名称,例如：SSOLogin(HttpServletRequest request,HttpServletResponse response)
	 * @param desc 描述，记录其他说明或者异常堆栈信息
	 * @param requestParam 请求参数,当为条件时，请求参数为对比信息
	 * @param condition 条件分支字符串，例如：if(xxxxxx==xxxxx)  如果是  else 的 直接记录 else
	 * @return void返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static void writerFlowLogCondition(String streamNo,String orderSubNo,String loginName,String className,String methodName,String desc,Object requestParam,String condition){
		writerFlowLog(streamNo,orderSubNo, loginName, className, methodName, "Condition", requestParam, null, condition, desc, "", "", "", "");
	}
	
	/**
	 * writerFlowLogCondition 方法作用：写入流程日志条件分支
	 * @param streamNo 流水号
	 * @param loginName 登陆名
	 * @param className 类名
	 * @param methodName 方法名称,例如：SSOLogin(HttpServletRequest request,HttpServletResponse response)
	 * @param responseResult 响应结果,如果涉及到条件中，直接结束响应的，将响应进行记录
	 * @param desc 描述，记录其他说明或者异常堆栈信息
	 * @return void返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static void writerFlowLogThrowable(String streamNo,String orderSubNo,String loginName,String className,String methodName,Object responseResult,String desc){
		writerFlowLog(streamNo,orderSubNo, loginName, className, methodName, "Exception", null, responseResult, "", desc, "", "", "", "");
	}
	
	/**
	 * writerFlowLogOther 方法作用：写入流程日志其他流程
	 * @param streamNo 流水号
	 * @param loginName 登陆名
	 * @param className 类名
	 * @param methodName 方法名称,例如：SSOLogin(HttpServletRequest request,HttpServletResponse response)
	 * @param responseResult 响应结果
	 * @param desc 描述，记录其他说明或者异常堆栈信息
	 * @param requestParam 请求参数,当为条件时，请求参数为对比信息
	 * @return void返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static void writerFlowLogOther(String streamNo,String orderSubNo,String loginName,String className,String methodName,Object responseResult,String desc,Object requestParam){
		writerFlowLog(streamNo,orderSubNo, loginName, className, methodName, "Other", requestParam, responseResult, "", desc, "", "", "", "");
	}
	
	/**
	 * writerFlowLogCondition 方法作用：写入流程日志
	 * @param streamNo 流水号
	 * @param loginName 登陆名
	 * @param className 类名
	 * @param methodName 方法名称,例如：SSOLogin(HttpServletRequest request,HttpServletResponse response)
	 * @param flowFlag 流程标记： 进入：Enter，退出：Exit，条件分支：Condition，条件分支并退出方法：ConditionAndReturn，异常或错误：Throwable，其他流程：Other
	 * @param requestParam 请求参数,当为条件时，请求参数为对比信息
	 * @param responseResult 响应结果
	 * @param condition 条件分支字符串，例如：if(xxxxxx==xxxxx)  如果是  else 的 直接记录 else
	 * @param str1 扩展字段
	 * @param str2 扩展字段
	 * @param str3 扩展字段
	 * @param str4 扩展字段
	 * @param desc 描述，记录其他说明或者异常堆栈信息
	 * @return void返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static void writerFlowLog(String streamNo,String orderSubNo,String loginName,String className,String methodName,String flowFlag,
		Object requestParam,Object responseResult,String condition,String desc, String str1,String str2, String str3, String str4){
		if (FLOW_LOG.isInfoEnabled()){
			try{
				String serverIp = InetAddress.getLocalHost().getHostAddress();
				
				Map<String, Object> flowMap = new HashMap<String, Object>();
				flowMap.put("streamNo", streamNo); // 流水号
				flowMap.put("orderSubNo,", orderSubNo); // 订单编号
				flowMap.put("loginName", loginName); // 登陆名
				flowMap.put("className", className); // 类名
				flowMap.put("methodName", methodName); // 方法名称
				flowMap.put("flowFlag", flowFlag); // 流程标记： 进入：Enter，退出：Exit，条件分支：Condition，条件分支并退出方法：ConditionAndReturn，异常或错误：Throwable，其他流程：Other
				flowMap.put("requestParam", convertMapToString(requestParam)); // 请求参数
				flowMap.put("responseResult", convertMapToString(responseResult)); // 响应结果
				flowMap.put("condition", condition); // 分支条件
				flowMap.put("serverIp", serverIp); // 服务器IP
				flowMap.put("desc", desc); // 描述
				
				flowMap.put("sysName", SYS_NAME); // 系统名称  网厅/wap
				DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
				flowMap.put("createDate", DATE_FORMAT.format(new Date())); // 创建时间
				flowMap.put("str1", str1); // 扩展字段1
				flowMap.put("str2", str2); // 扩展字段2
				flowMap.put("str3", str3); // 扩展字段3
				flowMap.put("str4", str4); // 扩展字段4
				
				FLOW_LOG.info(JSONObject.fromObject(flowMap).toString());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String convertMapToString(Object map){
		
		String str = "";
		
		if (null != map){
			try{
				str = com.alibaba.fastjson.JSONObject.toJSONString(map);
				
				if (StringUtils.isNotBlank(str)){
					str = StringUtils.replace(str, JSON_PAGE, "");
					str = StringUtils.replace(str, JSON_PAGE2, "");
					str = StringUtils.length(str) > LOG_FIELD_MAX_LENGTH ? StringUtils.substring(str, 0, LOG_FIELD_MAX_LENGTH) + "..." : str;
				}
			}catch (Exception e){
				str = "Json转换异常:" + processThrowableMessage(e);
			}
		}
		
		return str;
	}
	
	/**
	 * convertParamToMap 方法作用:转换参数为Map， key的数量和values的数量要保持一致，
	 * 如果key和比value的数量不一致，多余的value就会被抛弃
	 * 如果key为空，将返回空map对象
	 * @param keys
	 * @return
	 * @return Map<String,Object>返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static Map<String,Object> convertParamToMap(String[] keys, Object[] values){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (null != keys && keys.length != 0){
			for (int i = 0; i < keys.length; i++ ) {
				resultMap.put(keys[i], null != values && values.length > i ? values[i] : "");
			}
		}
    	
		return resultMap;
	}

	/**
	 * convertParamToMap 方法作用: 转换参数为Map
	 * @param key
	 * @param value
	 * @return
	 * @return Map<String,Object>返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static Map<String,Object> convertParamToMap(String key, Object value){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(key, value);
    	return resultMap;
	}
	
	/**
	 * convertDefaultResponseResultToMap 方法作用: 转换参数为Map， 默认key：responseResult
	 * @param value
	 * @return
	 * @return Map<String,Object>返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static Map<String,Object> convertDefaultResponseResultToMap(Object value){
    	return convertParamToMap("responseResult" ,value);
	}
	
	/**
	 * convertDefaultKeyToMap 方法作用:转换默认参数默认key到map，自动设置key名字，values值顺序依据：condition1，condition2，...
	 * @return
	 * @return Map<String,Object>返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static Map<String,Object> convertDefaultKeyToMap(Object ... values){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (null != values && values.length != 0){
			for (int i = 0; i < values.length; i++ ) {
				resultMap.put("condition" + (i + 1), values[i]);
			}
		}
    	
		return resultMap;
	}
	
	/**
	 * convertDefaultConditionToMap 方法作用:转换默认条件为map，自动设置key名字，values值顺序依据：condition1Left，condition1Right，condition2Left，condition2Right ...
	 * 传入值，必须成对出现
	 * @return
	 * @return Map<String,Object>返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static Map<String,Object> convertDefaultConditionToMap(Object ... values){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String keyPrefix = "condition";
		String keyFlag = "Left";
		int j = 1;
		if (null != values && values.length > 0){
			for (int i = 0; i < values.length; i++ ) {
				
				
				resultMap.put(keyPrefix + j + keyFlag, null != values && values.length > i ? values[i] : "");
				
				if ("Left".equals(keyFlag)){
					keyFlag = "Right";
				}else{
					keyFlag = "Left";
					j ++;
				}
			}
		}
    	
		return resultMap;
	}

	/**
	 * createStreamNo 方法作用: 创建流水号
	 * @return
	 * @return String返回说明
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static String createStreamNo(){
		return DateUtils.getDate("yyyyMMddHHmmss")+(int)((Math.random()*9+1)*100000);
	}
	
	/**
	 * processThrowableMessage 方法作用：处理错误或者异常的堆栈信息为字符串
	 * @param e
	 * @return
	 * @Exception 异常说明
	 * @moduser：
	 * @moddate：
	 * @remark：
	 */
	public static String processThrowableMessage(Throwable e){
		
		String errorMessage = "";
		StringWriter out = new StringWriter();
		try {
			e.printStackTrace(new java.io.PrintWriter(out));
			errorMessage = out.toString();
		}
		finally {
			try {
				out.close();
			} catch (IOException e1) {
			}
		}

		return errorMessage.replace("\t", "   ");
	}

	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if(obj == null){
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
		}catch (Exception e){
			writerFlowLogThrowable(createStreamNo(),"","","LogUtils", "objectToMap",null,processThrowableMessage(e));
		}

		return map;
	}
}
