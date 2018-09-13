package com.ai.ecs.ecm.mall.wap.modules.goods.constant;

import com.google.common.collect.ImmutableMap;


public class BroadbandConstants {
	
	/**
	 * 地市父ID
	 */
	public static final String CITY_PID = "190000";
	
	/**
	 * 地州编码
	 */
	public static final String EPARCHY_CODE = "0731";
	
	/**
	 * 渠道编码  PC
	 */
	public static final String   CHANNEL_ID_PC = "E006";
	
	/**
	 * 渠道编码 WAP
	 */
	public static final String   CHANNEL_ID_WAP = "E007";

	/**
	 * 渠道编码 o2o渠道
	 */
	public static final String   CHANNEL_ID_O2O_CHANNEL = "E050";

	/**
	 * 平台级店铺ID
	 */
	public static final Long  SHOP_ID = 1L;
	
	/**
	 * 平台级店铺名称
	 */
	public static final String SHOP_NAME = "湖南省移动公司";
	
	
	/**
	 * 商户编号 用于支付
	 */
	public static final String   MERCHANT_ID="888073157351233";


	/**
	 * 密钥  
	 */
	public static final String  SIGN_KEY = "exHX1SRkF8JTnzEId9bFp5qmF";

	public static final String PRODUCT_TYPE="VIRTUAL";

	public static final String PAY_TYPE="3";
	/**
	 * 支付平台与支付机构代码对应关系
	 */
	public static ImmutableMap<String,Short> PAY_PLATFORM= ImmutableMap.of("WAPALIPAY",(short)4,"WAPIPOS_SHIPOS",(short)5);

	/**
	 * 宽带移机
	 */
	public static final String BROAD_BAND_TYPE_MOVE="2";

	/**
	 * 宽带拆机
	 */
	public static final String BRAOD_BAND_TYPE_DESTROY ="3";

	public static final String RESPONSE_CODE="respCode";
	public static final String RESPONSE_DESC="respDesc";

	public final static String MERCHANTID = "888073157379240";

	public final static String KEY = "ue6s8vnZWWUa09i54SJdl19CY";

	public final static String RETURN_CODE_SUCCESS="0000";

	public final static String BROADBAND_BUSITYPE_HEFAMILY= "0";
	public final static String BROADBAND_BUSITYPE_CONS_PERSONAL= "1";
	public final static String BROADBAND_BUSITYPE_CONS_FAMILY= "2";
	public final static String BROADBAND_BUSITYPE_FAMILYNET = "3";
	public final static String CITY_CODE_OF_LIUYANG="A31H";


















}
