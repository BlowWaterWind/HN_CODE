package com.ai.ecs.ecm.mall.wap.modules.Bargain;
import com.ai.ecs.common.utils.JedisClusterUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by tanglong on 2016/12/14.
 */


@Controller
@RequestMapping(value="/share")
public class BargainShareSign {
    /**
     * 分享控制器
     * @param request
     * @return
     */
    @RequestMapping(value = "ticketSignature")
    @ResponseBody
    public Map<String, String> ticketSignature(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String url = request.getParameter("url");// 获取分享的url
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        SortedMap<String, String> signData = new TreeMap<String, String>();
        signData.put("noncestr", nonceStr);
        signData.put("url", url);
        signData.put("timestamp", timeStamp);
        signData.put("jsapi_ticket", JedisClusterUtils.get("dtb_jsapi_ticket"));
        String signature = createLinkString(signData);
        map.put("nonceStr", nonceStr);
        map.put("url", url);
        map.put("timeStamp", timeStamp);
        map.put("signature", signature.toLowerCase());// 转换为小写
        map.put("appId", "wxf6e74a02d659d984");// 公众号Id
        return map;
    }

    /**
     * 功能简述 获取加密串
     *
     * @param params
     * @return
     * @throws Exception
     * @author：qianman@asiainfo.com
     * @create：2016年8月9日 下午2:30:22
     */
    private String createLinkString(Map<String, String> params) {
        StringBuffer strBuff = new StringBuffer();
        String signatureStr = "";
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = params.get(key);
            strBuff.append(key + "=" + value + "&");
        }
        signatureStr = strBuff.toString();
        signatureStr = signatureStr.substring(0, signatureStr.length() - 1);
        return SHA1Encode(signatureStr).toLowerCase();// 返回是大写
    }
    /**
     * SHA1Encode
     *
     * @param sourceString
     * @return String
     * @Exception
     * @author：caijj
     * @Jan 11, 2013 8:02:10 PM
     * @update:
     * @Jan 11, 2013 8:02:10 PM
     */
    private String SHA1Encode(String sourceString) {
        String resultString = null;
        try {
            resultString = new String(sourceString);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = byte2hexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }
    /**
     * byte2hexString
     *
     * @param bytes
     * @return String
     * @Exception
     * @author：caijj
     * @Jan 11, 2013 8:01:57 PM
     * @update:
     * @Jan 11, 2013 8:01:57 PM
     */
    private String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }
}