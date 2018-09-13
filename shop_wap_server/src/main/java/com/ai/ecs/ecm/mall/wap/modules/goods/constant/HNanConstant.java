package com.ai.ecs.ecm.mall.wap.modules.goods.constant;

public class HNanConstant {
	
	public static final String ECOPSUCCESS = "0000";//ECOP调用成功
	public static final String SUCCESS = "0";//返回正确结果（接口调用成功）
	public static final String FAIL = "-1";//返回错误结果（接口调用失败）
	public static final String NOLOGIN = "1";//返回错误结果（接口调用失败）
	
	public static final String resultCode = "X_RESULTCODE"; //返回结果编码字段
	public static final String resultInfo = "X_RESULTINFO"; //返回结果信息字段
	public static final String recordNum = "X_RECORDNUM"; //返回结果记录数字段
	public static final String LEFTMENUTYPE = "WDYDCDQE003";//我的移动左侧菜单简拼
	public static final String ALIPAY_WEB_ORG_CODE = "alipay"; // 支付宝支付机构编号
	public static final String LOGTYPE = "business_web_log";
	
	/**
	 * FeeLogState：缴费日志状态 
	 *  
	 * @author：daijun@asiainfo.com 
	 * @Sep：2016年4月8日 上午9:17:23 
	 * @version 1.0
	 */
	public class FeeLogState{
		public static final String PAY_SUCCESS = "0"; //支付完成
		public static final String WAIT_SYNC_CALLBACK = "1";//等待银行同步回调
		public static final String WAIT_ASYNCHRONOUS_CALLBACK = "2";//等待银行异步回调
		public static final String POSTING = "3";//上账中
		public static final String ASYNCHRONOUS_CALLBACK_FIAL = "-2";//银行异步回调结果失败
		public static final String POSTING_FIAL = "-3";//上账失败
	}
	
	
	public static final String LOGINWITHPASS = "2";//服务密码登录方式标志
	public static final String LOGINWITHSMS = "0";//短信验证码登录方式标志
	
}    
	
	
