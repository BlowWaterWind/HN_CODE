package com.ai.ecs.ecm.mall.wap.modules.goods.utils;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {
    private static Logger log = Logger.getLogger(HttpRequest.class);

    static int connectionTimeout = 600000;
    static int readTimeout = 600000;

    /**
     * 功能简述
     * 
     * @param json
     * @return 下发请求
     */
    public static JSONObject sendRequest(String url, JSONObject json) {
        JSONObject jsonResult = new JSONObject();
        HttpClient client = new HttpClient();
        PostMethod httpPost = new PostMethod(url);
        // 设置连接超时时间
        client.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
        client.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
        httpPost.setRequestHeader("Content-Encoding", "UTF-8");
        String mapString = null;
        mapString = json.toString();
        String responseData = null;
        InputStream is;
        try {
            is = new java.io.ByteArrayInputStream(mapString.getBytes("UTF-8"));
            InputStreamRequestEntity inEntity = new InputStreamRequestEntity(is);
            httpPost.setRequestEntity(inEntity);
            // 发送数据并获取响应
            Exception exception = null;
            client.executeMethod(httpPost);
            int resStatusCode = httpPost.getStatusCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpPost.getResponseBodyAsStream(), "UTF-8"));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            responseData = sb.toString();
            if (resStatusCode == HttpStatus.SC_OK) {
                jsonResult = JSONObject.fromObject(responseData);
            } else {
                exception = new Exception("[HTTPTest] Http连接返回失败.ErrorCode:" + resStatusCode);
            }
            if (exception != null) {
                throw exception;
            }
        } catch (java.net.ConnectException ex) {
            log.error("[HTTPTest] 连接的主机或端口不通!");
            ex.printStackTrace();
        } catch (IOException ex) {
            String message = ex.getMessage();
            if (message != null && message.toLowerCase().indexOf("read timed") > -1) {
                log.error("[HTTPTest] 等待响应超时!");
                ex.printStackTrace();
            } else {
                log.error("[HTTPTest] 连接主机超时!");
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            log.error("[HTTPTest] 发送http请求未知异常!");
            ex.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return jsonResult;
    }
}
