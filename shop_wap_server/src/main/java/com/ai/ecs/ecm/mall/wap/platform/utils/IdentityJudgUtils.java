package com.ai.ecs.ecm.mall.wap.platform.utils;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.PropertiesLoader;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 身份判定是否为相应的名单
 * 特殊客户 BKF0010
 * 升级投诉客户 BKF0011
 * 红名单客户 BKF0009
 * Created by xtf on 2016/11/3.
 */
public class IdentityJudgUtils {
    private static String callEcbUrl = null;
    private static Logger logger = Logger.getLogger(IdentityJudgUtils.class);
    public static final int SPECIAL = 0;//特殊客户
    public static final int LEVEL = 1;//升级客户
    public static final int RED = 2;//红名单客户
    public static final int NORMAL = -1;//普通客户
    public static int judgement(String mobileNum){
        //判断是否是特殊客户
        String identify = JedisClusterUtils.get(mobileNum+"IDENTIFY");
        if(identify!=null&&!identify.equals("")){
            return Integer.parseInt(identify);
        }
        if(callEcb("BKF0010",mobileNum)){
            JedisClusterUtils.set(mobileNum+"IDENTIFY",SPECIAL+"",0);
            return SPECIAL;
        }else if(callEcb("BKF0011",mobileNum)){//升级投诉客户
            JedisClusterUtils.set(mobileNum+"IDENTIFY",LEVEL+"",0);
            return LEVEL;
        }else if(callEcb("BKF0009",mobileNum)){//红名单客户
            JedisClusterUtils.set(mobileNum+"IDENTIFY",RED+"",0);
            return RED;
        }
        JedisClusterUtils.set(mobileNum+"IDENTIFY",NORMAL+"",0);
        return NORMAL;
    }

    /**
     * 调用ecb接口，查询用户信息
     * @param busiCode
     * @param mobileNum
     * @return
     */
    private static boolean callEcb(String busiCode,String mobileNum){
        try
        {
            callEcbUrl = JedisClusterUtils.get("valetCallEcbUrl");
            if(callEcbUrl==null){
                callEcbUrl = new PropertiesLoader("mall.properties").getProperty("ecbCall");
                JedisClusterUtils.set("valetCallEcbUrl",callEcbUrl,300);
            }
            //String p = "http://192.192.1.132:8809/ecb/services/ECB/busiCall";
            //http://192.192.1.132:8809/ecb/services/ECB
            String p = callEcbUrl+"/ecb/services/ECB/busiCall";
            URL url=new URL(p);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String flowNo = sdf.format(new Date());
            String param = "param=<?xml version=\"1.0\" encoding=\"utf-8\"?><R><H>" +
                    "<F n=\"VOUCH_FLOW_NO\">NGCRM"+flowNo+"</F><F n=\"BUSI_CITY_CODE\">*</F>" +
                    "<F n=\"BUSI_CODE\">"+busiCode+"</F><F n=\"ACTIVITY_CODE\">0</F><F n=\"CHANNEL_ACC\">ngcrm</F>" +
                    "<F n=\"CHANNEL_CODE\">NGCRM</F><F n=\"CHANNEL_PWD\">ngcrm</F></H><B><S><F n=\"MOBILENUM\">"+mobileNum+"</F>" +
                    "</S><M/></B></R>";
            printWriter.write(param);
            printWriter.flush();
            BufferedReader in = null;
            StringBuilder sb = new StringBuilder();
            try{
                in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8") );
                String str = null;
                while((str = in.readLine()) != null) {
                    sb.append( str );
                }
            } catch (Exception ex) {
                throw ex;
            } finally{
                try{
                    conn.disconnect();
                    if(in!=null){
                        in.close();
                    }
                    if(printWriter!=null){
                        printWriter.close();
                    }
                }catch(IOException ex) {
                    throw ex;
                }
            }
            String result =  sb.toString();
            result = result.replace("&lt;", "<");
            logger.info("调用ecb接口返回信息："+result);
            result = result.replace("<ns1:busiCallResponse xmlns:ns1=\"http://ws.ecb.chinacreator.com/\"><return>", "");
            result = result.replace("</return></ns1:busiCallResponse>", "");
            result = result.replaceAll("&gt;",">");
            logger.info("调用ecb接口返回信息--处理后："+result);
            StringReader read = new StringReader(result);
            SAXBuilder builder=new SAXBuilder(false);
            //得到XML的文档
            Document doc = builder.build(read,"UTF-8");
            Element root = doc.getRootElement();
            List<Element> list = root.getChild("H").getChildren();
            Map<String,String> map = new HashMap<>();
            for(Element e:list){
                map.put(e.getAttributeValue("n"), e.getTextTrim());
                //System.out.println(e.getAttributeValue("n")+":"+e.getTextTrim());
            }
            return "0".equals(map.get("BUSI_RETURN_CODE"));
        }
        catch (Exception e)
        {
            logger.error("调用ecb接口查询出错",e);
            return false;
        }

    }
    public static void main(String[] args){
        System.out.println( judgement("15211002483"));
    }
}
