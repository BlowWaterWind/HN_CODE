/**
 * <p>Copyright: Copyright (c) 2014</p>
 */
package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 
 * @author dyy
 * @date: 2015年4月15日 上午10:29:19
 * @version 1.0
 * @since JDK 1.7
 */
public class JsonpUtils {

    private final static Logger logger = LoggerFactory.getLogger(JsonpUtils.class);

    public static void writeToResponse(HttpServletRequest request, HttpServletResponse response, int httpStatus, Object result) {
        response.setStatus(httpStatus);
        writeToResponse(request, response, result);
    }

    public static void writeToResponse(HttpServletRequest request, HttpServletResponse response, Object result) {
        String json = JSONObject.toJSONString(result);
        String callback = request.getParameter("callback");
        if (StringUtils.isBlank(callback)) {
            writeToResponse(response, json, "application/json;charset=UTF-8");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(callback).append("(").append(json).append(");");
            writeToResponse(response, sb.toString(), "application/javascript;charset=UTF-8");
        }
    }

    public static void writeToResponse(HttpServletResponse response, String json, String contentType) {
        try {
            if (response.isCommitted()) {
                return;
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType(contentType);
            response.setContentLength(json.getBytes().length);
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            logger.error("写入response出错", e);
        }
    }

}
