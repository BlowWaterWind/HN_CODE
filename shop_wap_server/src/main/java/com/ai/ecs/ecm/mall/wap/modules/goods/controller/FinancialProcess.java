package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.iis.upp.bean.OrderLogBean;
import com.ai.iis.upp.service.IOrderLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2017-1-13.
 */
@Service
public class FinancialProcess {

    @Autowired
    private IOrderLogService logService;

    private Logger logger = LoggerFactory.getLogger(FinancialProcess.class);

    private static final String REQ_STR_HEAD = "`SC`";// 包头

    private static final String REQ_STR_CHECK = "0001:BOS010:";// 固定字符+功能号（前置校验接口）

    private static final String REQ_STR_FREEZ = "0001:BOS004:";// 固定字符+功能号（冻结接口）

    private static final String REQ_STR_NOTICE = "0001:BOS006:";// 固定字符+功能号（结果通知接口）

    public static final String REQ_LIULIANG = "0001:BOS020:";  // 流量券实时

    private static final String REQ_STR_END = "62781B75";// 固定字符+功能号（包尾）
    private static final String SUCCESS = "000000"; //

    @Value("${financialIp}")
    private String financialIp;
    @Value("${financialPort}")
    private int financialPort;


    /**
     * 调用和聚宝接口, 赠送50M流量
     * @param phone
     * @param heBaoActivityId
     * @param heBaoflowId
     * @param orderId
     * @param clientIp
     * `SC`00250001:BOS020:RETN=000000,DESC=处理成功62781B75
     * @throws Exception
     */
    public boolean sendFlowPackage(String phone, String heBaoActivityId, String heBaoflowId, String orderId, String clientIp) throws Exception {
        boolean isInvokeSuccess = false;
        try {
            StringBuilder reqStr = new StringBuilder("[");
            reqStr.append("ATDNTNO=BF731001").append(",");//平台机构编号
            reqStr.append(findDayFlow(14)).append(",");//BOSS流水 1234567890
            reqStr.append(findTime(0)).append(",");//操作请求时间 yyyy-mm-dd hh:MM:ss
            reqStr.append(heBaoActivityId).append(",");//营销活动批次号
            reqStr.append(heBaoflowId).append(",");//流量券别号
            reqStr.append(phone).append(",");//手机号码
            reqStr.append("18210100").append(",");//外部系统渠道号
            reqStr.append("50");//流量值（单位为M）
            reqStr.append("]");
            String returnStr = invoke(reqStr.toString(), REQ_LIULIANG, orderId, clientIp);//调接口

            Map<String, String> returnMap = str2Data(returnStr.substring(20).split("]")[0]);//去掉响应头 和包尾
            if(SUCCESS.equals(returnMap.get("RETN"))) {
                isInvokeSuccess = true;
            }
        } catch (Exception e) {
            logger.error("调用接口失败", e);
            e.printStackTrace();
            isInvokeSuccess = false;
        }
        return isInvokeSuccess;
    }



    public Financial payOrder(String serialNumber, Long tradeMoney, String merchantId, String orderSubId, String clientIp) throws Exception {


        if (StringUtils.isBlank(serialNumber)) {
            logger.error("serialNumber 手机号码不能为空");
            throw new Exception("手机号码不能为空");
        }

        if (StringUtils.isBlank(merchantId)) {
            logger.error("merchantId 商户不能为空");
            throw new Exception("商户不能为空");
        }

        // 冻结资金
        Financial financial = freezMoney(serialNumber, tradeMoney, merchantId, orderSubId, clientIp);
        try {
            // 扣款和聚宝资金
            Map<String, String> noticeMap = resultNotice(financial, "2", orderSubId, clientIp);
            // 如果扣款成功
            if (SUCCESS.equals(noticeMap.get("RETN"))) {
                financial.setNoticeFalg("1");  // 扣款成功
            } else {
                throw new Exception("和包账户资金不足");
            }
        } catch (Exception e) {
            financial.setNoticeFalg("2");
            logger.error("扣款和聚宝资金失败", e);
        }

        // 需要解冻资金
        if ("2".equals(financial.getNoticeFalg())) {
            try {
                Map<String, String> noticeMap1 = resultNotice(financial, "1", orderSubId, clientIp);
                if (SUCCESS.equals(noticeMap1.get("RETN"))) {
                    financial.setNoticeFalg("3");  // 解冻成功
                } else {
                    financial.setNoticeFalg("4");  // 扣款成功，解冻失败
                }
            } catch (Exception e) {
                financial.setNoticeFalg("4");
                logger.error("扣款和聚宝资金扣款成功，解冻失败", e);
            }
        }

        return financial;
    }


    public Financial freezMoney(String serialNumber, Long tradeMoney, String merchantId, String orderSubId, String clientIp) throws Exception {

        Financial financial = null;
        // 目标地址及端口
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("financialNo", findDayFlow(14));
        paramMap.put("freezFlowId", fingReqFlow());
        paramMap.put("transDate", findYearMonth());// 冻结日期
        paramMap.put("freezTime", (new SimpleDateFormat("HH:mm:ss")).format(new Date()));// 冻结时间
        paramMap.put("serialNumber", serialNumber); // 手机号码
        paramMap.put("tradeMoney", tradeMoney + "");// 冻结金额
        paramMap.put("merchantId", merchantId);     // 商户号
        paramMap.put("acType", "3");
        paramMap.put("acLevel", "6");

        Map<String, String> returnMap = null;
        try {
            String returnStr = invoke(data2Str(paramMap, "DJ"), REQ_STR_FREEZ, orderSubId, clientIp); //调接口
            returnMap = str2Data(returnStr.substring(21).split("]")[0]);//去掉响应头 和包尾
        } catch (Exception e) {
            logger.error("调用冻结资金失败", e);
            throw new Exception("冻结资金服务正忙， 请稍后再试");
        }

        if(SUCCESS.equals(returnMap.get("RETN"))){
            financial = new Financial();
            financial.setFreezFlowId(paramMap.get("freezFlowId"));
            financial.setTransDate(paramMap.get("transDate"));
            financial.setFreezTime(paramMap.get("freezTime"));
            financial.setMrchantId(paramMap.get("merchantId"));
            financial.setSerialNumber(paramMap.get("serialNumber"));
            financial.setSettlAmount(paramMap.get("tradeMoney"));//结算金额
            financial.setTradeMoney(paramMap.get("tradeMoney"));
            financial.setSettlMerchant(paramMap.get("merchantId"));;
            financial.setActivityType("3");
            financial.setActivityLevel("6");
            financial.setNoticeFalg("0");//已冻结
            return financial;
        } else {
            throw new Exception("和聚宝可冻结资金不足");
        }
    }


    public Map<String, String> resultNotice(Financial financial, String operType, String orderSubId, String clientIp) throws Exception {
        // 目标地址及端口
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("financialNo", findDayFlow(14));
        paramMap.put("transDate", findYearMonth());// 交易日期
        paramMap.put("freezTime", (new SimpleDateFormat("HH:mm:ss")).format(new Date())); // 交易时间
        paramMap.put("flowId", fingReqFlow());
        paramMap.put("serialNumber", financial.getSerialNumber());// 手机号码
        paramMap.put("operType", operType);// 处理方式  1：解冻用户和聚宝资产（用户参加活动支付失败）2：登记商户结算（用户参加活动支付成功）
        paramMap.put("tradeMoney", financial.getTradeMoney());// 冻结金额
        paramMap.put("freezFlowId", financial.getFreezFlowId());//冻结时的请求流水号
        paramMap.put("settlMoney", "60000");//结算金额
        paramMap.put("merchantId", financial.getMrchantId());// 商户号
        paramMap.put("acType", "3");// 活动类型
        paramMap.put("acLevel", "6");// 活动档次

        String returnStr = invoke(data2Str(paramMap,"TZ"), REQ_STR_NOTICE, orderSubId, clientIp);//调接口
        Map<String, String>  returnMap = str2Data(returnStr.substring(21).split("]")[0]);//去掉响应头 和包尾
        return returnMap;
    }



    public Map<String, String> str2Data(String returnStr) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String[] strs = returnStr.split(",");
            for(String str : strs){
                String[] tempStrs = str.split("=");
                map.put(tempStrs[0], tempStrs[1]);
            }
        } catch (Exception e) {
            logger.error("\n====和包理财，请求字符串转换异常", e);
        }
        return map;
    }



    public String data2Str(Map<String, String> reqMap,String reqType) {
        StringBuilder reqStr = new StringBuilder("[");
        if("JY".equals(reqType)){
            //前置校验
            reqStr.append(findDayFlow(14)).append(",");// 系统跟踪号 CHAR 14 Y 当日不允许重复，全为数字
            reqStr.append(findYearMonth()).append(",");// 交易日期 CHAR 10 Y YYYY-MM-DD
            reqStr.append((new SimpleDateFormat("HH:mm:ss")).format(new Date())).append(",");// 交易时间 CHAR 8 Y Hh24:mm:ss
            reqStr.append("SF").append(",");// 报文协议标识 CHAR 2 Y 常量"SF"
            reqStr.append("0001").append(",");// 报文版本 CHAR 4 Y 初始版本号0001
            reqStr.append("rq").append(",");// 请求响应标志 CHAR 2 Y 请求（rq）
            reqStr.append("00").append(",");// 报文属性 CHAR 2 Y 业务报文（00）
            reqStr.append("00").append(",");// 报文安全标志 CHAR 2 Y 00
            reqStr.append(reqMap.get("serialNumber")).append(",");// 手机号码 CHAR 11 Y
            reqStr.append(reqMap.get("acType")).append(",");// 活动类型 CHAR 8 N 值从 1 递增
            reqStr.append(reqMap.get("acLevel")); // 活动档次 CHAR 8 N 值从 1 递增
            reqStr.append("]");
        }else if("DJ".equals(reqType)){
            //冻结接口
            reqStr.append(reqMap.get("financialNo")).append(",");// 系统跟踪号 CHAR 14 Y 当日不允许重复，全为数字
            reqStr.append(reqMap.get("transDate")).append(",");// 交易日期 CHAR 10 Y YYYY-MM-DD
            reqStr.append(reqMap.get("freezTime")).append(",");// 交易时间 CHAR 8 Y Hh24:mm:ss
            reqStr.append("SF").append(",");// 报文协议标识 CHAR 2 Y 常量"SF"
            reqStr.append("0001").append(",");// 报文版本 CHAR 4 Y 初始版本号0001
            reqStr.append("rq").append(",");// 请求响应标志 CHAR 2 Y 请求（rq）
            reqStr.append("00").append(",");// 报文属性 CHAR 2 Y 业务报文（00）
            reqStr.append("00").append(",");// 报文安全标志 CHAR 2 Y 00
            reqStr.append(reqMap.get("freezFlowId")).append(",");// 电渠请求流水 CHAR 16 Y
            reqStr.append(reqMap.get("merchantId")).append(",");// 请求商户编号 CHAR 32 Y 两节营销活动传;结算商户号
            reqStr.append(reqMap.get("serialNumber")).append(",");// 手机号码 CHAR 11 Y
            reqStr.append(reqMap.get("tradeMoney")).append(",");// 冻结金额 CHAR 15 Y 金额单位为分
            reqStr.append(findTime(0)).append(",");// 冻结开始时间 CHAR 19 Y 日期格式 ：yyyy-mm-dd hh24:MM:SS
            reqStr.append(findTime(365)).append(",");// 冻结结束时间 CHAR 19 Y 日期格式 ：yyyy-mm-dd hh24:MM:SS
            reqStr.append("两节购机营销活动").append(",");// 冻结原因 CHAR 100 N 固定值：两节营销活动
            reqStr.append("1").append(",");// 自动解冻标识 CHAR 1 Y 0：自动解冻;1：主动解冻;营销活动，自动解冻传值0 营销活动，传值1
            reqStr.append(reqMap.get("acType")).append(",");// 活动类型 CHAR 8 N 值从 1 递增
            reqStr.append(reqMap.get("acLevel")); // 活动档次 CHAR 8 N 值从 1 递增
            reqStr.append("]");
        }else if("TZ".equals(reqType)){
            //通知接口
            reqStr.append(reqMap.get("financialNo")).append(",");// 系统跟踪号 CHAR 14 Y 当日不允许重复，全为数字
            reqStr.append(reqMap.get("transDate")).append(",");// 交易日期 CHAR 10 Y YYYY-MM-DD
            reqStr.append(reqMap.get("freezTime")).append(",");// 交易时间 CHAR 8 Y Hh24:mm:ss
            reqStr.append("SF").append(",");// 报文协议标识 CHAR 2 Y 常量"SF"
            reqStr.append("0001").append(",");// 报文版本 CHAR 4 Y 初始版本号0001
            reqStr.append("rq").append(",");// 请求响应标志 CHAR 2 Y 请求（rq）
            reqStr.append("00").append(",");// 报文属性 CHAR 2 Y 业务报文（00）
            reqStr.append("00").append(",");// 报文安全标志 CHAR 2 Y 00
            reqStr.append(reqMap.get("flowId")).append(",");// 电渠请求流水 CHAR 16 Y
            reqStr.append(reqMap.get("serialNumber")).append(",");// 冻结手机号码 CHAR 11 Y
            reqStr.append(reqMap.get("operType")).append(",");//处理方式 CHAR 1 Y 1：解冻用户和聚宝资产（用户参加活动支付失败）2：登记商户结算（用户参加活动支付成功）
            reqStr.append(reqMap.get("tradeMoney")).append(",");// 冻结金额 CHAR 15 Y 金额单位为分
            reqStr.append(reqMap.get("freezFlowId")).append(",");// 原冻结电渠请求流水号 CHAR 16 Y 冻结时的请求流水号
            reqStr.append(reqMap.get("settlMoney")).append(",");//结算金额 CHAR 15 Y
            reqStr.append(reqMap.get("merchantId")).append(",");//冻结请求商户号	CHAR 32	Y
            reqStr.append(reqMap.get("merchantId")).append(",");//结算商户号	CHAR 32	Y
            reqStr.append(reqMap.get("acType")).append(",");// 活动类型 CHAR 8 N 值从 1 递增
            reqStr.append(reqMap.get("acLevel")); // 活动档次 CHAR 8 N 值从 1 递增
            reqStr.append("]");
        }
        return reqStr.toString();
    }



    /**
     * fingReqFlow 获取请求流水 不重复
     * @return
     * @return String返回说明
     * @Exception 异常说明
     * @author：shubo@asiainfo.com
     * @create：2016年12月28日 下午8:49:05 @moduser： @moddate： @remark：
     */
    public String fingReqFlow() {

        int machineId = 2;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {// 有可能是负数
            hashCodeV = -hashCodeV;
        }
        return machineId + String.format("%015d", hashCodeV);
    }


    /**
     * findDayFlow 系统跟踪号
     * @return
     * @return String返回说明
     * @Exception 异常说明
     * @author：shubo@asiainfo.com
     * @create：2016年12月28日 下午8:36:41 @moduser： @moddate： @remark：
     */
    public String findDayFlow(int len) {
        StringBuilder sb = new StringBuilder(len * 2);
        sb.append((int)(Math.random() * 8) + 1);
        for (int i = 0; i < len - 1; i++) {
            sb.append((int)(Math.random() * 9));
        }
        return sb.toString();
    }



    /**
     * findYearMonth 获取年月日（简：2016-06-18）
     * @return
     * @return String返回说明
     * @Exception 异常说明
     * @author：shubo@asiainfo.com
     * @create：2016年6月17日 下午3:06:54 @moduser： @moddate： @remark：
     */
    public String findYearMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
        return date;
    }

    /**
     * findTime 获取时间
     * @return
     * @return String返回说明
     * @Exception 异常说明
     * @author：shubo@asiainfo.com
     * @create：2016年12月28日 下午7:55:12 @moduser： @moddate： @remark：
     */
    public String findTime(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + day);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        return df.format(date);
    }

    /**
     * invoke socket请求 （省平台联机交易接口）
     * @param reqStr
     * @param funTypeStr
     * @return
     * @throws Exception
     * @return String返回说明
     * @Exception 异常说明
     * @author：shubo@asiainfo.com
     * @create：2017年1月5日 下午2:59:32
     * @moduser：
     * @moddate：
     * @remark：
     */
    public String invoke(String reqStr, String funTypeStr, String orderSubId, String clientIp) throws Exception {
        String respStr = "";
        Socket socket = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        PrintWriter out = null;
        try {
            socket = new Socket(financialIp, financialPort);//测试
//			socket = new Socket("15.15.20.8", 8888);//本地
            // socket = new Socket("211.138.236.210", 33102);//
            // 向服务器端发送数据
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK")));
            // PrintWriter out=new PrintWriter(socket.getOutputStream());
            String dataPcLen = Integer.toHexString((funTypeStr + reqStr).getBytes("GBK").length) + "";// 16进制数据包长度：4字节
            for (int i = 4 - dataPcLen.length(); i > 0; i--) {
                dataPcLen = "0" + dataPcLen;
            }

            String baowen = REQ_STR_HEAD + dataPcLen + funTypeStr + reqStr + REQ_STR_END;// 冻结接口
            baowen = doEncrypt(baowen);

            logger.debug("\n====和聚宝请求：" + baowen);
            //System.out.println(baowen);

            outputStream = socket.getOutputStream();
            outputStream.write(baowen.getBytes("GBK"));

            inputStream = socket.getInputStream();
            byte[] arrayOfByte2 = new byte[8000];
            int i = inputStream.read(arrayOfByte2);
            out.write(new String(arrayOfByte2, 0, i));

            respStr = new String(arrayOfByte2, "GBK");
            logger.debug("\n====和聚宝响应:" + new String(arrayOfByte2, "GBK"));
            //System.out.println(respStr);
            out.flush();
        } finally {

            try {
                OrderLogBean bean = new OrderLogBean();
                bean.setClientIp(clientIp);
                bean.setOrderETime(new Date());
                bean.setOrderSTime(new Date());
                bean.setOrderId(orderSubId);
                bean.setOrderOper("order");
                bean.setOrderReq(reqStr);
                bean.setOrderResp(respStr);

                logService.saveLog(bean);
            } catch (Exception e) {
                logger.error("保存日志失败");
            }


            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    logger.error("inputStream.close", e);
                }
            }

            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    logger.error("outputStream.close", e);
                }
            }

            if(out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    logger.error("out.close", e);
                }
            }

            if(socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    logger.error("socket.close", e);
                }
            }
        }
        return respStr;
    }

    /**
     * doEncrypt 加密
     * @param data
     * @return void返回说明
     * @throws UnsupportedEncodingException
     * @Exception 异常说明
     * @author：shubo@asiainfo.com
     * @create：2016年12月21日 下午5:10:40 @moduser： @moddate： @remark：
     */
    public static String doEncrypt(String data) throws UnsupportedEncodingException {
        try {
            // MD5加密并转BASE64的数据
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes("GBK"));
            BASE64Encoder encoder = new BASE64Encoder();
            String base64Data = encoder.encode(md.digest());

            // 右补空格
            for (int i = base64Data.length(); i < 64; i++) {
                base64Data += " ";
            }
            data = data + base64Data;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return data;
    }
}
