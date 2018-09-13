package com.ai.ecs.ecm.mall.wap.modules.goods.utils; /**
 */


import jodd.util.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;


public class Utility {

    private static Logger log = Logger.getLogger(Utility.class);

    /**
     * SHA1Encode
     *
     * @param sourceString
     * @return String
     * @todo //sha1加密
     * @Exception
     * @author：caijj
     * @Jan 11, 2013 8:02:10 PM
     * @update:
     * @Jan 11, 2013 8:02:10 PM
     */
    public static String SHA1Encode(String sourceString) {
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
     * getNowDate 取当前时间
     *
     * @return
     * @throws Exception
     */
    public static String getNowDate() throws Exception {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Calendar calendar = new GregorianCalendar();
        String date = sf.format(calendar.getTime());
        return date.replace("-", "");
    }

    /**
     * 获取6位随机数
     *
     * @return
     */
    public static String getSixRandom() {
        long seed = 9999;
        long rand = seed + Math.round(Math.random() * 1000000);
        String pwd = String.valueOf(rand);
        if (pwd.length() < 6) {
            pwd = String.valueOf(999999 - rand);
        } else {
            pwd = pwd.substring(0, 6);
        }
        return pwd;
    }

    /**
     * byte2hexString
     *
     * @param bytes
     * @return String
     * @todo
     * @Exception
     * @author：caijj
     * @Jan 11, 2013 8:01:57 PM
     * @update:
     * @Jan 11, 2013 8:01:57 PM
     */
    public final static String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }

    /**
     * GetDistance ToDo 计算地球两点之间直线距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return int
     * @Exception
     * @author：caijj
     * @Jan 23, 2013 2:57:20 PM
     * @update:
     * @Jan 23, 2013 2:57:20 PM
     */
    public static int GetDistance(String lat1, String lng1, String lat2, String lng2) {
        double EARTH_RADIUS = 6378.137;// 地球半径
        double radLat1 = rad(Float.parseFloat(lat1));
        double radLat2 = rad(Float.parseFloat(lat2));
        double a = radLat1 - radLat2;
        double b = rad(Float.parseFloat(lng1)) - rad(Float.parseFloat(lng2));

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;
        return (int) s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * getCityCodeByCityName ToDo 根据城市名称获取城市编码
     *
     * @param cityName
     * @return
     * @throws Exception String
     * @Exception
     * @author：caijj
     * @Feb 18, 2013 11:56:00 AM
     * @update:
     * @Feb 18, 2013 11:56:00 AM
     */
    public static String getCityCodeByCityName(String cityName) throws Exception {
        return "";// getValue("weather/cityName", "weather/cityCode", "cityCode", "cityName", cityName);
    }

    /**
     * getEpaCodeByEpaName ToDo 根据地州名称获取地州编码
     *
     * @param
     * @return
     * @throws Exception String
     * @Exception
     * @author：caijj
     * @Feb 18, 2013 11:56:00 AM
     * @update:
     * @Feb 18, 2013 11:56:00 AM
     */
    public static String getEpaCodeByEpaName(String eparchyName) throws Exception {
        return "";//getValue("uipBean/epaName", "uipBean/epaCode", "epaCode", "epaName", eparchyName);
    }

    /**
     * 度分秒转化成度，以#隔开
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */
    public static String itudeConvert(String location) throws Exception {
        double location_D = 0;
        if (!"".equals(location) && location.indexOf("#") > 0) {
            String[] local = location.split("#");
            location_D = Double.parseDouble(local[0]);
            if (local.length > 1) {
                location_D = location_D + Double.parseDouble(local[1]) / 60;
            }
            if (local.length > 2) {
                location_D = location_D + Double.parseDouble(local[2]) / 3600;
            }
        }
        return String.valueOf(location_D);
    }

    /**
     * 从配置文件获得交易编码、业务编码
     *
     * @param
     * @return data
     * @throws Exception
     * @author janly
     */
    @SuppressWarnings("unchecked")
    public static String getTradeCode(String tt, String property) throws Exception {
//        IData data = new DataMap();
//        try {
//            data.put("BIZ_CODE", common.getProperty("biztrans.xml", "biztran/" + property + "/BIZ_CODE", ""));
//            data.put("TRANS_CODE", common.getProperty("biztrans.xml", "biztran/" + property + "/TRANS_CODE", ""));
//
//            data.put("OPCODE", common.getProperty("biztrans.xml", "biztran/" + property + "/TRANS_CODE", ""));
//        } catch (Exception e) {
//            e.printStackTrace();
//            // log.error("获取交易编码业务编码出错！"+e,e);
//        }
        return "";
    }

    /**
     * 字典排序
     *
     * @param timestamp
     * @param nonce
     * @param token
     * @return
     * @throws Exception
     */
    public static String getSignature(String timestamp, String nonce, String token) throws Exception {
        String maxString = "";// 排后面
        String minString = "";// 排前面
        if (timestamp.compareTo(nonce) < 0) {
            maxString = nonce;
            minString = timestamp;
        } else {
            maxString = timestamp;
            minString = nonce;
        }

        if (token.compareTo(maxString) > 0) {
            token = minString + maxString + token;
        } else if (token.compareTo(minString) < 0) {
            token = token + minString + maxString;
        } else {
            token = minString + token + maxString;
        }
        return SHA1Encode(token);
    }

    public static String getSysTime()
            throws Exception {
        return decodeTimestamp("yyyy-MM-dd HH:mm:ss", new Timestamp(System.currentTimeMillis()));
    }

    public static String decodeTimestamp(String format, Timestamp time)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }


    /**
     * 发起http get请求获取网页源代码
     *
     * @param requestUrl
     * @return
     */
    public static String httpRequest(String requestUrl) {
        StringBuffer buffer = null;

        try {
            // 建立连接
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");

            // 获取输入流
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 读取返回结果
            buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            httpUrlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    /**
     * 获取6位随机密码
     *
     * @return
     */
    public static String getRandomPwd() {
        long seed = 9999;
        long rand = seed + Math.round(Math.random() * 1000000);
        String pwd = String.valueOf(rand);
        if (pwd.length() < 6) {
            pwd = String.valueOf(999999 - rand);
        } else {
            pwd = pwd.substring(0, 6);
        }
        return pwd;
    }

    /**
     * getYearMonth(获取年月)
     *
     * @param
     * @param
     * @return IDataset返回说明
     * @throws Exception
     * @Exception 异常说明
     * @author：zhangkun3
     * @create：Aug 30, 2013 4:31:51 PM
     * @moduser：
     * @moddate：
     * @remark：
     */
    public static String getYearMonth() throws Exception {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        return dateformat.format(cal.getTime());
    }

    /**
     * getMonth(获取月份)
     *
     * @param
     * @param
     * @return IDataset返回说明
     * @throws Exception
     * @Exception 异常说明
     * @author：zhangkun3
     * @create：Aug 30, 2013 4:31:51 PM
     * @moduser：
     * @moddate：
     * @remark：
     */
    public static String getMonth() throws Exception {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM");
        Calendar cal = Calendar.getInstance();
        return dateformat.format(cal.getTime());
    }

    /**
     * getFormatDate(获取时间格式)
     *
     * @param
     * @param
     * @return IDataset返回说明
     * @throws Exception
     * @Exception 异常说明
     * @author：zhangkun3
     * @create：Aug 30, 2013 4:31:51 PM
     * @moduser：
     * @moddate：
     * @remark：
     */
    public static String getFormatDate(String format) throws Exception {
        if (StringUtil.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        return dateformat.format(cal.getTime());
    }

    /**
     * 将秒转换成时分秒
     *
     * @param time
     * @return
     * @throws Exception
     */
    public static String convertTime(long time) throws Exception {
        long second = time % 60;
        long minute = (time - second) % 3600 / 60;
        long hour = (time - second - minute * 60) / 3600;
        return hour + "小时" + minute + "分" + second + "秒";
    }

    /**
     * 转换流量
     *
     * @param flow
     * @return
     * @throws Exception
     */
    public static String convertFlow(long flow) throws Exception {
        long bit = flow % 1024;
        long kByte = (flow - bit) % 1048576 / 1024;
        long mByte = (flow - bit - kByte * 1024) / 1048576;
        return mByte + "MB " + kByte + "KB " + bit + "bit";
    }

    /**
     * 主机获取IP（非127.0.0.1）
     *
     * @author：zhangkun3
     * @2014-5-16 下午12:21:10
     */
    @SuppressWarnings("unchecked")
    public static String getLocalIP() {
        String ip = "";
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = (InetAddress) addresses.nextElement();
                    if (address != null && address instanceof Inet4Address) {
                        ip = address.getHostAddress();
                        if (!"127.0.0.1".equals(ip)) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("Utility.getLocalIP Exception：" + e.getMessage());
        }
        return ip;
    }

    /**
     * 获取客户端IP及浏览器信息 本地调用接口IP为本机IP，无浏览器信息
     *
     * @author：zhangkun3
     * @Jul 23, 2014 3:50:31 PM
     */
    @SuppressWarnings("unchecked")
    public static String getClientInfo(HttpServletRequest request) {
//        IData data = new DataMap();
        String ip = "";
        if (null != request) {// 客户端请求
            ip = request.getHeader("x-forwarded-for");

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            ip = ip.split(",")[0];
            if ("10.238.233.230".equals(ip)) {
                ip = request.getHeader("X-ClientIP");
                if (null == ip) {
                    ip = "127.0.0.1";
                }
                ip = ip.split(",")[0];
            }
//            data.put("TRADE_USERIP", ip);
//            data.put("TRADE_USERAGENT", request.getHeader("user-agent"));
        } else {// 本机请求
            InetAddress addr;
            try {
                addr = InetAddress.getLocalHost();
                ip = addr.getHostAddress();// 获得本机IP
//                data.put("TRADE_USERAGENT", "");
//                data.put("TRADE_USERIP", ip);
            } catch (UnknownHostException e) {
                log.error("获取本机IP失败：：：" + e, e);
            }
        }

        return "";
    }

    /**
     * 功能简述 计算两个时间的差值 单位ms
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return long
     * @throws Exception
     */
    public static String CalculatInteTime(String startDate, String endDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long intervalTime = 0;
        try {
            Date start = format.parse(startDate);
            Date end = format.parse(endDate);
            intervalTime = end.getTime() - start.getTime();
        } catch (Exception e) {
            log.error("日期转换错误,方法名:CalculatInteTime：", e);
        }

        return intervalTime + "";
    }


    /**
     * 功能简述
     * 计算两时间差
     *
     * @param startDate
     * @param end
     * @return
     * @author：qianman@asiainfo.com
     * @create：2016年3月29日 下午9:14:03
     */
    public static long CalculatInteTime(String startDate, Date end) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long intervalTime = 0;
        try {
            Date start = format.parse(startDate);
            intervalTime = end.getTime() - start.getTime();
        } catch (Exception e) {
            log.error("日期转换错误,方法名:CalculatInteTime：", e);
        }
        return intervalTime;
    }

    /**
     * toGetData 判断是否要去取数据
     *
     * @param saveTime
     * @return boolean返回说明
     * @throws Exception
     * @Exception 异常说明
     * @author：zhangkun3
     * @create：2014-1-21 上午11:42:31
     * @moduser：
     * @moddate：
     * @remark：
     */
    public static boolean toGetData(String saveTime, int delay) throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // pm-time存放的是到网站取数据的时间，单位秒
        calendar.add(Calendar.MINUTE, -delay);
        return sf.parse(saveTime).before(sf.parse(sf.format(calendar.getTime())));
    }

    /**
     * 功能简述
     * 国际漫游 加密串
     *
     * @param str
     * @return
     * @author：qianman@asiainfo.com
     * @create：2016年9月25日 下午1:09:50
     */
    public static String getMd5(String str) {
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        charArray = null;
        byteArray = null;
        md5Bytes = null;
        md5 = null;
        return hexValue.toString();
    }
}
