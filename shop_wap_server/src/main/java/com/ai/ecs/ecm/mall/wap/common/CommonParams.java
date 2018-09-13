package com.ai.ecs.ecm.mall.wap.common;

import com.google.common.collect.ImmutableMap;

import com.ai.ecs.order.constant.OrderConstant;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 *
 * @author kuangjq
 * @Date 2016-3-26 上午10:41:03 
 *
 */
public class CommonParams {
	/**
	 * 渠道编码：WAP商城
	 */
	public static final String CHANNEL_CODE="E007";

	public static final String CITY_HN_PID = "190000";//查询湖南所有市PID

	/**
	 * kibala
	 */
	public static final String KA_LOG = "webDbLog";



	/**
	 * 支付平台与支付机构代码对应关系
	 */
	public static ImmutableMap<String,Short> PAY_PLATFORM= ImmutableMap.of("WAPIPOS_SHIPOS",(short)2,"WAPALIPAY",(short)3);

	/**
	 * 支付平台与编码对应关系
	 */
	public static ImmutableMap<String,String> PAY_CHARSET= ImmutableMap.of("WAPIPOS_SHIPOS","GBK","WAPALIPAY","UTF-8");


	/**
	 * 支付方式中有CMCC往T_EZF_FLOW的T_EZF_FLOW_INTERID保存为支付流水
	 * CommonServiceImpl.java#savePayFlowOrder
	 */
	public static ImmutableMap<String,String> PAY_ENUM_PAYPLATFORM = ImmutableMap.of(
			"1","ALIPAY-WAP_CMCCPAYH5",
			"2","WEIXIN-WAP_CMCCPAYH5",
			"3","UNIONPAY-WAP_CMCCPAYH5",
			"4","CMPAY-WAP_CMCCPAYH5",
			"5","IPOS-WAP_CMCCPAYH5");
	public static final String KEY = "ue6s8vnZWWUa09i54SJdl19CY";

	/**
	 * 一阶段支付账户888073157379240
	 * 二阶段支付商户10005标识电渠
	 */
	public static ImmutableMap<String,String> PAY_ENUM_MERCHANT = ImmutableMap.of(
			"ALIPAY-WAP_CMCCPAYH5","888073157379240",
			"WEIXIN-WAP_CMCCPAYH5","888073157379240",
			"UNIONPAY-WAP_CMCCPAYH5","888073157379240",
			"CMPAY-WAP_CMCCPAYH5","888073157379240",
			"IPOS-WAP_CMCCPAYH5","10005");

	/**
	 * 一阶段支付账户888073157379240对应的KEY值
	 * 二阶段支付商户10005标识电渠 测试私钥(对应) T_EZF_MERCHANT的T_EZF_MERCHANT_KEY
	 * 对应T_EZF_MERCHANT_INFO的T_EZF_MD5
	 * 增加注释
	 */
	public static ImmutableMap<String,String> PAY_ENUM_MERCHANT_KEY = ImmutableMap.of(
			"ALIPAY-WAP_CMCCPAYH5","ue6s8vnZWWUa09i54SJdl19CY",
			"WEIXIN-WAP_CMCCPAYH5","ue6s8vnZWWUa09i54SJdl19CY",
			"UNIONPAY-WAP_CMCCPAYH5","ue6s8vnZWWUa09i54SJdl19CY",
			"CMPAY-WAP_CMCCPAYH5","ue6s8vnZWWUa09i54SJdl19CY",
			"IPOS-WAP_CMCCPAYH5","5SgJip6BKHHTM4qNqBH8o2PQWAr3hlbV");

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
		public static final String SYNC_CALLBACK_FIAL = "-1";//银行同步回调结果失败
		public static final String ASYNCHRONOUS_CALLBACK_FIAL = "-2";//银行异步回调结果失败
		public static final String POSTING_FIAL = "-3";//上账失败
	}


	/**
	 * 订单类型与支付类型对应关系
	 * 支付类型：1-充值缴费;2-终端类订单;3-宽带订单支付;4-号卡;5-一体化平台;6-流量
	 */
	public final static Map<Integer, String> PAY_TYPE = new HashMap<Integer, String>() {{
		put(OrderConstant.TYPE_CONTRACT, "2");
		put(OrderConstant.TYPE_BARE, "2");
		put(OrderConstant.TYPE_PARTS, "2");
		put(OrderConstant.TYPE_SIM, "4");
		put(OrderConstant.TYPE_TRAFFIC, "6");
		put(OrderConstant.TYPE_BROADBAND, "3");
		put(OrderConstant.TYPE_BROADBAND_CONTINUE, "3");
		put(OrderConstant.TYPE_SMS, "7");
		put(OrderConstant.TYPE_BANK_DEPOSIT,"5");
	}};


	/**
	 * 银行订单类型
	 */
	public final static Map<String,Boolean> NEED_FREEZE  = new HashMap<String,Boolean>(){{
		put("SPDB",true);
		put("CCB",false);
	}};

	/**
	 * 银行订单类型
	 */
	public final static Map<String,String> RECMD_PHONE  = new HashMap<String,String>(){{
		put("SPDB","99123450001");
		put("CCB","99123450002");
	}};



	/**
	 * 订单类型与支付商户秘钥对应关系
	 */
	public final static Map<Integer, JSONObject> ORDER_TYPE_MERCHANT = new HashMap<Integer, JSONObject>() {{
		JSONObject realAccount = new JSONObject();
		realAccount.put("merchantId","888073157340201");
		realAccount.put("key","PiuyCichiwMavfcVkjCYEnttg");

		JSONObject virtualAccount = new JSONObject();
		virtualAccount.put("merchantId","888073157340203");
		virtualAccount.put("key","EpCm6NlOsKc8n4mNU0844J3iX");

		put(OrderConstant.TYPE_CONTRACT, realAccount);
		put(OrderConstant.TYPE_BARE, realAccount);
		put(OrderConstant.TYPE_PARTS, realAccount);
		put(OrderConstant.TYPE_VALUECARD_PHONEFEE, realAccount);
		put(OrderConstant.TYPE_VALUECARD_FLOWFEE, realAccount);
		put(OrderConstant.TYPE_PARTS, realAccount);
		put(OrderConstant.TYPE_SIM, virtualAccount);
		put(OrderConstant.TYPE_TRAFFIC, virtualAccount);
		put(OrderConstant.TYPE_BROADBAND, virtualAccount);
		put(OrderConstant.TYPE_SMS, virtualAccount);
	}};

	/**
	 * 湖南三季度营销活动支撑商户
	 * 商户名称：中国移动通信集团终端有限公司湖南分公司
	 * 商户编号：888009948145937
	 */
	public static final JSONObject HN_TERMINAL_MERCHANT = JSONObject.parseObject("{\"merchantId\":\"888009948145937\",\"key\":\"MiuyCichiwMDdwy7e29e90acaQ\"}");

	/**
	 * 地市编码、客服工号部门对应关系
	 */
	public final static Map<String, JSONObject> EPARCH_KF = new HashMap<String, JSONObject>() {{
		put("0730", JSONObject.parseObject("{\"inStaffId\":\"FY199027\",\"inDepartId\":\"14BA1\"}"));//岳阳
		put("0731", JSONObject.parseObject("{\"inStaffId\":\"A1KFWS25\",\"inDepartId\":\"1480B\"}"));//长沙
		put("0732", JSONObject.parseObject("{\"inStaffId\":\"CY018018\",\"inDepartId\":\"14BAB\"}"));//湘潭
		put("0733", JSONObject.parseObject("{\"inStaffId\":\"BA051325\",\"inDepartId\":\"14ABE\"}"));//株洲
		put("0734", JSONObject.parseObject("{\"inStaffId\":\"DYWSF026\",\"inDepartId\":\"14A70\"}"));//衡阳
		put("0735", JSONObject.parseObject("{\"inStaffId\":\"LY000025\",\"inDepartId\":\"14B9D\"}"));//郴州
		put("0736", JSONObject.parseObject("{\"inStaffId\":\"J01A0026\",\"inDepartId\":\"1497A\"}"));//常德
		put("0737", JSONObject.parseObject("{\"inStaffId\":\"HYE99027\",\"inDepartId\":\"14A6B\"}"));//益阳
		put("0738", JSONObject.parseObject("{\"inStaffId\":\"KY002326\",\"inDepartId\":\"14B79\"}"));//娄底
		put("0739", JSONObject.parseObject("{\"inStaffId\":\"ESSQ1321\",\"inDepartId\":\"14B90\"}"));//邵阳
		put("0743", JSONObject.parseObject("{\"inStaffId\":\"U0012036\",\"inDepartId\":\"14A71\"}"));//自治州
		put("0744", JSONObject.parseObject("{\"inStaffId\":\"GZ074429\",\"inDepartId\":\"146FC\"}"));//张家界
		put("0745", JSONObject.parseObject("{\"inStaffId\":\"NHWSXH24\",\"inDepartId\":\"21354\"}"));//怀化
		put("0746", JSONObject.parseObject("{\"inStaffId\":\"MY010027\",\"inDepartId\":\"14A50\"}"));//永州
	}};

	/**
	 * 支付平台与支付接口编码对应关系
	 */
	public final static Map<String,String> PAY_INTERFACE_TYPE = new HashMap<String,String>(){{
		put("WAPIPOS_SHIPOS","SHGWDirectPayToPayOrg");//和包,分账
		put("WAPALIPAY","SHGWDirectPayToPayOrg");//支付宝
	}};
}
