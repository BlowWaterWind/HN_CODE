package com.ai.ecs.ecm.mall.wap.platform.utils;

import com.ai.ecs.common.utils.Exceptions;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.member.entity.MemberVo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 业务日志处理
 *
 * @author fengrh 2016/6/28  17:09
 * @see
 * @since 1.0
 * 修改人： fengrh 修改时间：2016/6/28  17:09 修改备注：
 */
public class BusiLogUtils {
    static Logger logger = LoggerFactory.getLogger(HNanConstant.LOGTYPE);
    private static ExecutorService pool= Executors.newFixedThreadPool(200);

    /**
     *
     * @param request
     * @param goodsName 商品名称
     *                  @param operType 操作类型
     * @param goodsId 商品id
     * @param goodsPrice 商品价格
     *                   @param operResult 返回状态码
     * @param resutlInfo 返回结果
     * @param source 请求path
     * @param logType 1 INFO, 2 ERROR
     * @throws Exception
     */
    public static void writerLogging(HttpServletRequest request,String operType,String goodsName, String goodsId,
                                     String startTime ,String goodsPrice,String operResult, String resutlInfo,
                                     String source,Exception ex,String logType,Object otherParam) throws Exception {
        try {
            String sessionId = UserUtils.getSession().getId().toString();
            MemberVo memberVo =  UserUtils.getLoginUser(request);
            String custName = "";
            String brand = "";
            String brandCode = "";
            String epachyCode = "";
            String serialNumber = "";
            if (null != memberVo) {
                if (null != memberVo.getMemberLogin()) {
                    custName = memberVo.getMemberLogin().getMemberLogingName();
                    serialNumber = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
                }
            }
            String chanId = "E007";
            String staffId = "ITFWPMAL";
            String runningId =  System.currentTimeMillis()+"";
            String REQ_PARAM=getParams(request.getParameterMap()); //请求参数
            String endTime = com.ai.ecs.common.utils.DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            String touch_id = "shop-wap", request_ip = StringUtils.getRemoteAddr(request), SOURCE_STAFF_ID = "";
            String rsrv_str3="";
            MemberSsoVo memberSsoVo=UserUtils.getSsoLoginUser(request,null);
            if(null!=memberSsoVo){
                rsrv_str3=memberSsoVo.getCreditClass();//星级
                epachyCode = memberSsoVo.getEparchyCode();
            }
            if(null != otherParam){
                REQ_PARAM += JSON.toJSONString(otherParam);
            }

            pool.execute(new SaveLogThread( genLogStr(source,touch_id, request_ip, staffId, SOURCE_STAFF_ID, REQ_PARAM,"",runningId, serialNumber,
                    custName, chanId, operType,
                    goodsName, brand, brandCode, goodsId, sessionId, epachyCode, goodsPrice,
                    startTime, endTime, operResult, resutlInfo,"","",rsrv_str3,""),"1"));

        }catch (Exception e){
            logger.error("日志记录异常。",e);
        }
    }
    /**
     *
     * @param request
     * @param source 请求path
     * @throws Exception
     */
    public static void writerLogging(HttpServletRequest request,
                                     String source,Exception ex,Object otherParam) throws Exception {
        try {
            String operType = "";
            String goodsName = "";
            String goodsId = "";
            String startTime = com.ai.ecs.common.utils.DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            String goodsPrice = "";
            String operResult = "";
            String resutlInfo = "";
            String sessionId = UserUtils.getSession().getId().toString();
            MemberVo memberVo =  UserUtils.getLoginUser(request);
            String custName = "";
            String brand = "";
            String brandCode = "";
            String epachyCode = "";
            String serialNumber = "";
            if (null != memberVo) {
                if (null != memberVo.getMemberLogin()) {
                    custName = memberVo.getMemberLogin().getMemberLogingName();
                    serialNumber = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
                }
            }
            String chanId = "E007";
            String staffId = "ITFWPMAL";
            String runningId =  System.currentTimeMillis()+"";
            String REQ_PARAM=getParams(request.getParameterMap()); //请求参数
            String endTime = com.ai.ecs.common.utils.DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
            String touch_id = "shop-wap", request_ip = StringUtils.getRemoteAddr(request), SOURCE_STAFF_ID = "";
            String rsrv_str3="";
            MemberSsoVo memberSsoVo=UserUtils.getSsoLoginUser(request,null);
            if(null!=memberSsoVo){
                rsrv_str3=memberSsoVo.getCreditClass();//星级
                epachyCode = memberSsoVo.getEparchyCode();
            }
            if(null != otherParam){
                REQ_PARAM += JSON.toJSONString(otherParam);
            }

            pool.execute(new SaveLogThread( genLogStr(source,touch_id, request_ip, staffId, SOURCE_STAFF_ID, REQ_PARAM,"",runningId, serialNumber,
                    custName, chanId, operType,
                    goodsName, brand, brandCode, goodsId, sessionId, epachyCode, goodsPrice,
                    startTime, endTime, operResult, resutlInfo,"","",rsrv_str3,""),"1"));

        }catch (Exception e){
            logger.error("日志记录异常。",e);
        }
    }

    private static String genLogStr(String... args) {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (String str : args) {
            if (i == 0) {
                sb.append(str);
            } else {
                sb.append("@;;@").append(str);
            }
            i++;
        }
        return sb.toString();
    }

    public static String getParams(Map paramMap){
        if (paramMap == null){
            return "";
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        return params.toString();
    }

    public static void saveLog(HttpServletRequest request ,Exception ex, Long beginTime, long eTime) {
        try {
            String sessionId = UserUtils.getSession().getId().toString();
            MemberVo memberVo =  UserUtils.getLoginUser(request);
            String custName = "";
            String brand = "";
            String brandCode = "";
            String epachyCode = "";
            String serialNumber = "";
            if (null != memberVo) {
                if (null != memberVo.getMemberLogin()) {
                    custName = memberVo.getMemberLogin().getMemberLogingName();
                    serialNumber = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
                }
            }
            String chanId = "E007";
            String staffId = "ITFWPMAL";
            String runningId = System.currentTimeMillis()+"";
            String REQ_PARAM=getParams(request.getParameterMap()); //请求参数
            String startTime = transferLongToDate(beginTime);
            String endTime = transferLongToDate(eTime);
            String touch_id = "shop-wap", request_ip = StringUtils.getRemoteAddr(request), SOURCE_STAFF_ID = "";
            String resutlInfo= Exceptions.getStackTraceAsString(ex);
            String rsrv_str3="";
            MemberSsoVo memberSsoVo=UserUtils.getSsoLoginUser(request,null);
            if(null!=memberSsoVo){
                rsrv_str3=memberSsoVo.getCreditClass(); //星级
                epachyCode = memberSsoVo.getEparchyCode();
            }

            pool.execute(new SaveLogThread( genLogStr(request.getRequestURI(),touch_id, request_ip, staffId, SOURCE_STAFF_ID, REQ_PARAM,"",runningId, serialNumber,
                    custName, chanId, "INIT",
                    "", brand, brandCode, "", sessionId, epachyCode, "",
                    startTime, endTime, "", resutlInfo,"","",rsrv_str3,""),"1"));

        }catch (Exception e){
            logger.error("日志记录异常。",e);
        }
    }

    /**
     * 保存日志线程
     */
    public static class SaveLogThread extends Thread{

        private String logStr;
        private String logType;

        public SaveLogThread(String logStr,String logType){
            super(SaveLogThread.class.getSimpleName());
            this.logStr = logStr;
            this.logType=logType;
        }

        @Override
        public void run() {
            logger.info(logStr);
        }
    }

    private static String transferLongToDate(Long millSec){
        String dateFormat="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }
}
