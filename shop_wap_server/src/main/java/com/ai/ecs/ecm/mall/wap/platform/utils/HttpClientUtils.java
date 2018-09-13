/**
 * @authorDeng.yy
 * @createDate 2014年1月13日
 */
package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP客户端操作工具类。
 * 
 * @authorDeng.yy
 */
public class HttpClientUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	/**
	 * 发送HTTP请求过程中使用的默认编码(UTF-8)。
	 */
	public static final String DEFAULT_REQUEST_CHARSET = "utf-8";

	/**
	 * 提交一个GET请求。默认使用UTF-8编码。
	 * 
	 * @param uri 请求的地址
	 * @param params 请求的参数
	 * @return 请求返回的结果内容。
	 * @throws IOException
	 * 
	 * @authorDeng.yy
	 * @throws ClientProtocolException
	 * @createDate 2013年11月21日
	 */
	public static String doGetAndGetString(String uri, Map<String, String[]> params) throws ClientProtocolException,
			IOException {
		return doGetAndGetString(uri, params, DEFAULT_REQUEST_CHARSET);
	}

	/**
	 * 提交一个GET请求。
	 * 
	 * @param uri 请求的地址
	 * @param params 请求的参数
	 * @param charset 提交和解析结果所使用的字符编码
	 * @return 请求返回的结果内容。
	 * @throws IOException
	 * 
	 * @authorDeng.yy
	 * @throws ClientProtocolException
	 * @createDate 2013年11月21日
	 */
	public static String doGetAndGetString(String uri, Map<String, String[]> params, String charset)
			throws ClientProtocolException, IOException {
		String paramStr = formatRequestParams(params, charset);
		if (paramStr != null) {
			StringBuilder newUri = new StringBuilder();
			newUri.append(uri);
			if (uri.indexOf('?') == -1) {
				newUri.append('?');
			} else if (uri.charAt(uri.length() - 1) != '&') {
				newUri.append('&');
			}
			newUri.append(paramStr);

			uri = newUri.toString();
		}

		CloseableHttpClient httpclient = createClient();
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		return getContentAndCloseResponse(response, charset);
	}

	/**
	 * 提交一个文件下载GET请求。
	 * 
	 * @param uri 请求的地址
	 * @param params 请求的参数
	 * @param charset 提交和解析结果所使用的字符编码
	 * @return 请求返回的结果内容。
	 * @throws IOException
	 * 
	 * @authorDeng.yy
	 * @throws IllegalStateException
	 * @createDate 2013年11月21日
	 */
	public static InputStream doGetImage(String uri, Map<String, String[]> params) throws IllegalStateException,
			IOException {
		String paramStr = formatRequestParams(params, DEFAULT_REQUEST_CHARSET);
		InputStream inputStream = null;
		if (paramStr != null) {
			StringBuilder newUri = new StringBuilder();
			newUri.append(uri);
			if (uri.indexOf('?') == -1) {
				newUri.append('?');
			} else if (uri.charAt(uri.length() - 1) != '&') {
				newUri.append('&');
			}
			newUri.append(paramStr);

			uri = newUri.toString();
		}

		CloseableHttpClient httpclient = createClient();
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == 200) {
			inputStream = response.getEntity().getContent();
		}
		return inputStream;
	}

	private static CloseableHttpClient createClient() {
		// RequestConfig config = new RequestConfig(false, null, null, false,
		// null, true, true, false, 50, true, null, null, );
		// HttpClientBuilder builder = HttpClientBuilder.create().setde;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// httpclient
		return httpclient;
	}

	private static String formatRequestParams(Map<String, String[]> params, String charset) {
		if (MapUtils.isNotEmpty(params)) {
			StringBuilder paramStr = new StringBuilder();
			Set<Entry<String, String[]>> paramEntries = params.entrySet();
			String[] values = null;
			for (Entry<String, String[]> param : paramEntries) {
				values = param.getValue();
				if (ArrayUtils.isNotEmpty(values)) {
					for (String value : values) {
						paramStr.append(param.getKey()).append("=").append(value).append("&");
					}
				}
			}
			if (paramStr.length() > 0) {
				paramStr.deleteCharAt(paramStr.length() - 1);
			}

			// return URLEncoder.encode(paramStr.toString(), charset);
			return paramStr.toString();
		} else {
			return null;
		}
	}

	private static String getContentAndCloseResponse(CloseableHttpResponse response, String defaultContentEncoding)
			throws IOException {
		try {
			HttpEntity entity = response.getEntity();
			String contentEncoding = defaultContentEncoding;
			Header encodingHeader = entity.getContentEncoding();
			if (encodingHeader != null) {
				String encodingValue = encodingHeader.getValue();
				if (StringUtils.isNotEmpty(encodingValue)) {
					contentEncoding = encodingValue;
				}
			}

			String content = IOUtils.toString(entity.getContent(), contentEncoding);
			EntityUtils.consume(entity);

			return content;
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				logger.error("Close response failed.", e);
			}
		}
	}

	/**
	 * 提交一个POST请求。默认使用UTF-8编码。
	 * 
	 * @param uri 请求的地址
	 * @param params 请求的参数
	 * @return 请求返回的结果内容。
	 * @throws IOException
	 * 
	 * @authorDeng.yy
	 * @throws ClientProtocolException
	 * @createDate 2013年11月21日
	 */
	public static String doPostAndGetString(String uri, Map<String, String[]> params) throws ClientProtocolException,
			IOException {
		return doPostAndGetString(uri, params, DEFAULT_REQUEST_CHARSET);
	}

	/**
	 * 提交一个POST请求。
	 * 
	 * @param uri 请求的地址
	 * @param params 请求的参数
	 * @param charset 提交和解析结果所使用的字符编码
	 * @return 请求返回的结果内容。
	 * @throws IOException
	 * 
	 * @authorDeng.yy
	 * @throws ClientProtocolException
	 * @createDate 2013年11月21日
	 */
	public static String doPostAndGetString(String uri, Map<String, String[]> params, String charset)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = createClient();
		HttpPost httpPost = new HttpPost(uri);
		if (MapUtils.isNotEmpty(params)) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<Entry<String, String[]>> paramEntries = params.entrySet();
			String[] values = null;
			for (Entry<String, String[]> param : paramEntries) {
				values = param.getValue();
				if (ArrayUtils.isNotEmpty(values)) {
					for (String value : values) {
						nvps.add(new BasicNameValuePair(param.getKey(), value));
					}
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
		}

		RequestConfig.Builder builder = RequestConfig.custom();
		builder.setConnectionRequestTimeout(30000);
		builder.setSocketTimeout(30000);
		builder.setConnectTimeout(30000);
		httpPost.setConfig(builder.build());
		CloseableHttpResponse response = httpclient.execute(httpPost);
		return getContentAndCloseResponse(response, charset);
	}

	/**
	 * 提交一个POST请求。默认使用UTF-8编码。
	 * 
	 * @param uri 请求的地址
	 * @param params 请求的参数
	 * @return 请求返回的结果内容。
	 * @throws IOException
	 * 
	 * @authorDeng.yy
	 * @throws ClientProtocolException
	 * @createDate 2013年11月21日
	 */
	public static void doPost(String uri, Map<String, String[]> params) throws ClientProtocolException, IOException {
		doPost(uri, params, DEFAULT_REQUEST_CHARSET);
	}

	/**
	 * 提交一个POST请求。
	 * 
	 * @param uri 请求的地址
	 * @param params 请求的参数
	 * @param charset 提交和解析结果所使用的字符编码
	 * @return 请求返回的结果内容。
	 * @throws IOException
	 * 
	 * @authorDeng.yy
	 * @throws ClientProtocolException
	 * @createDate 2013年11月21日
	 */
	public static void doPost(String uri, Map<String, String[]> params, String charset) throws ClientProtocolException,
			IOException {
		CloseableHttpClient httpclient = createClient();
		HttpPost httpPost = new HttpPost(uri);
		if (MapUtils.isNotEmpty(params)) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<Entry<String, String[]>> paramEntries = params.entrySet();
			String[] values = null;
			for (Entry<String, String[]> param : paramEntries) {
				values = param.getValue();
				if (ArrayUtils.isNotEmpty(values)) {
					for (String value : values) {
						nvps.add(new BasicNameValuePair(param.getKey(), value));
					}
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
		}

		RequestConfig.Builder builder = RequestConfig.custom();
		builder.setConnectionRequestTimeout(3000);
		builder.setSocketTimeout(3000);
		builder.setConnectTimeout(3000);
		httpPost.setConfig(builder.build());
		httpclient.execute(httpPost);
	}

	/**
	 * 提交http請求上傳附件
	 * 
	 * @author：deng.youyi
	 * @createTime：2014年6月3日
	 * 
	 * @param url
	 * @param filepath
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String sendFile(String url, String fileName, InputStream inputStream) throws IOException {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		String content = "";
		try {
			HttpPost httppost = new HttpPost(url);

			HttpEntity req = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.setCharset(Charset.forName("UTF-8"))
					.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA, fileName).build();
			httppost.setEntity(req);
			response = httpclient.execute(httppost);
			HttpEntity entity1 = response.getEntity();
			content = IOUtils.toString(entity1.getContent(), "UTF-8");
			EntityUtils.consume(entity1);
			int statusCode = response.getStatusLine().getStatusCode();
			if (200 != statusCode) {
				content = "向该地址送消息错误[url={" + url + "}, params={" + content + "}, statusCode={" + statusCode + "}]";
			}
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}
	/**
	 * 发送http
	 * @param notifyURL
	 * @param uppParam
	 * @return
	 * @throws Exception
	 */
	public static String sentHttp(String notifyURL, Map uppParam)
			throws Exception {
		if (org.apache.commons.lang.StringUtils.isEmpty(notifyURL)) {
			throw new Exception("notifyUrl不可为空!!!其param：" + uppParam);
		}
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(notifyURL);
		postMethod.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");

		Set keyset = uppParam.keySet();
		for (Object object : keyset) {
			String keysetStr = (String) object;
			// log.info("加入"+keysetStr+",");
			postMethod.addParameter(keysetStr, (String) uppParam.get(keysetStr));
		}
		client.executeMethod(postMethod);

		int resStatusCode = postMethod.getStatusCode();
		// 判断响应代码
		String responseStr = "";
		if (resStatusCode == HttpStatus.SC_OK) {
			responseStr = postMethod.getResponseBodyAsString();
		}
		responseStr = responseStr.trim();
		return responseStr;
	}
}
