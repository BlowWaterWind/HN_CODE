package com.ai.ecs.ecm.mall.wap.platform.utils.pay.alipay;

import com.ai.ecs.common.utils.MD5;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* *
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 */

public class UppCore
{
	private static Logger log = Logger.getLogger(UppCore.class);

	/**
	 * 
	 * @param Params
	 *            业务参数
	 * @param signKey
	 *            密钥
	 * @param input_charset
	 *            编码
	 * @return
	 */
	public static String getHmac(Map Params, String signKey,
			String input_charset) {
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = UppCore.paraFilter(Params);
		// 由于notify有两个参数...,这里进行处理
		String notifyURL = sParaNew.get("notifyURL");
		if (notifyURL != null && notifyURL.contains("?listener")) {
			notifyURL = notifyURL.replace("?listener", "&listener");
			sParaNew.put("notifyURL", notifyURL);
		}

		// 获取待签名字符串
		String preSignStr = UppCore.createLinkString(sParaNew);
		System.out.println("signKey:" + signKey);
		System.out.println("input_charset:" + input_charset);
		String hmac = MD5.sign(preSignStr, signKey, input_charset);
		return hmac;
	}

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("")
					|| key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")
					|| key.equalsIgnoreCase("action")
					|| key.equalsIgnoreCase("hmac")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		System.out.println("'''''''''prestr:" + prestr);
		return prestr;
	}

	public static String putMapNotNull(String value) {
		if (value == null) {
			value = "";
		}
		return value;
	}

	public static String sentHttp2(String url,Map<String,String> uppParam) throws Exception{
		// 判断响应代码
		String responseStr = "";

		if(StringUtils.isEmpty(url)){
			throw new Exception("req_url不可为空!!!其param："+uppParam);
		}
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		if(null != uppParam && !uppParam.isEmpty()){
			log.info("------------sentHttp2----"+uppParam.toString());
			Set keyset = uppParam.keySet();
			for (Object object : keyset) {
				String keysetStr = (String)object;
				//log.info("加入"+keysetStr+",");
				postMethod.addParameter(keysetStr,uppParam.get(keysetStr)==null?"":uppParam.get(keysetStr));
			}
			log.info("notify_remote:"+uppParam);
			log.info("sendparam===>"+postMethod.getParameters());
		}
		client.executeMethod(postMethod);
		int resStatusCode = postMethod.getStatusCode();
		log.info("notify_remote_response:"+resStatusCode+"..."+uppParam);
		log.info("notify_remote_response:"+resStatusCode+"..."+uppParam);

		if (resStatusCode == HttpStatus.SC_OK) {
			responseStr = postMethod.getResponseBodyAsString();
		}
		responseStr = responseStr.trim();
		log.info("responseStr:"+responseStr.trim());

		return responseStr;
	}

	//
	// /**
	// * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	// * @param sWord 要写入日志里的文本内容
	// */
	// public static void logResult(String sWord) {
	// FileWriter writer = null;
	// try {
	// writer = new FileWriter(AlipayConfig.log_path + "alipay_log_" +
	// System.currentTimeMillis()+".txt");
	// writer.write(sWord);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (writer != null) {
	// try {
	// writer.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	//
	// /**
	// * 生成文件摘要
	// * @param strFilePath 文件路径
	// * @param file_digest_type 摘要算法
	// * @return 文件摘要结果
	// */
	// public static String getAbstract(String strFilePath, String
	// file_digest_type) throws IOException {
	// PartSource file = new FilePartSource(new File(strFilePath));
	// if(file_digest_type.equals("MD5")){
	// return DigestUtils.md5Hex(file.createInputStream());
	// }
	// else if(file_digest_type.equals("SHA")) {
	// return DigestUtils.sha256Hex(file.createInputStream());
	// }
	// else {
	// return "";
	// }
	// }
}
