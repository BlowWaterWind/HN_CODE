package com.ai.ecs.ecm.mall.wap.platform.utils;

import com.ai.ecs.common.utils.SpringContextHolder;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberSsoVo;
import com.ai.ecs.member.api.login.IMemberLoginLogService;
import com.ai.ecs.member.entity.ChannelLoginLog;
import com.ai.ecs.member.entity.MemberVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 会员登录日志工具类
 * Created by xtf on 2016/9/2.
 */
public class MemberLoginLogUtils {
    private static ExecutorService pool = Executors.newFixedThreadPool(200);
    static Logger log = LoggerFactory.getLogger(MemberLoginLogUtils.class);
    private static IMemberLoginLogService memberLoginLogService= SpringContextHolder.getBean("memberLoginLogService");
    /**
     * 保存登录日志
     * @param request
     * @param operType 登陆方式
     *                 0 密码登录
     *                 1 短信验证码
     *                 2 wap自登录
     *                 3 短厅登录
     *                 4.会员登录
     *                goodsName -业务名称
     *                 resultCode-业务办理结果 0成功,非0失败
     *                 resultInfo 业务办理内容结果
     *                 userName --登录用户名，可能是手机号
     * @return
     */
    public static boolean saveLog(HttpServletRequest request,String operType,String resultCode,String resultInfo,String tKey,String userName, MemberVo memberVo ){
        ChannelLoginLog channelLoginLog = new ChannelLoginLog();
        channelLoginLog.setOperType(operType);//登陆方式
        Date date = new Date();
        String timeId = new SimpleDateFormat("yyyyMMdd").format(date);

        channelLoginLog.setId(timeId);
        channelLoginLog.setSource("");
        channelLoginLog.setRequestIp(StringUtils.getRemoteAddr(request));
        channelLoginLog.setStaffId(PropertiesUtil.getValue("staffId"));
        channelLoginLog.setSourceStaffId("");
        channelLoginLog.setRunningId(timeId);
        channelLoginLog.setChanId("E007");
        channelLoginLog.setStartTime(date);
        channelLoginLog.setEndTime(date);
        channelLoginLog.setxResultCode(resultCode);
        channelLoginLog.setxResultInfo(resultInfo);
        if("0".equals(operType)||"1".equals(operType)){//如果是服务密码登录，或者短信验证码登录，则先存放手机号
            channelLoginLog.setSerialNumber(userName);
        }
        //以上为公共参数
        try{
            if("0".equals(resultCode)){//登录成功
                MemberSsoVo memberSsoVo=null;
                if(null!=tKey) {
                     memberSsoVo = UserUtils.getSsoLoginUser(request, tKey);
                }
                channelLoginLog.setMemberId(userName);
                if(null!=memberSsoVo){//客户不为空
                    channelLoginLog.setRsrv3Varchar(memberSsoVo.getProductId());
                    channelLoginLog.setRsrv2Varchar(memberSsoVo.getCreditClass());
                    channelLoginLog.setRsrv1Varchar(memberSsoVo.getCustId());//备用字段插入CUSTID，用户的USER_ID
                    channelLoginLog.setMemberId(null!=memberVo?memberVo.getMemberLogin().getMemberId()+"":"");
                    channelLoginLog.setSerialNumber(memberSsoVo.getMemberPhone());
                    channelLoginLog.setCustName(memberSsoVo.getMemberRealname()==null?userName:memberSsoVo.getMemberRealname());
                    channelLoginLog.setBrand(memberSsoVo.getBrand());
                    channelLoginLog.setBrandCode(memberSsoVo.getBrandCode());
                    channelLoginLog.setEpachy(memberSsoVo.getEparchyName());
                    channelLoginLog.setEpachyCode(memberSsoVo.getEparchyCode());
                    channelLoginLog.setCity(memberSsoVo.getCityName());
                    channelLoginLog.setCityCode(memberSsoVo.getCityCode());
                }else{
                    channelLoginLog.setCustName(userName);//为空就记录登录名
                }
            }else{
                //登录失败时直接使用用户名
                channelLoginLog.setMemberId(userName);//用户名
                channelLoginLog.setCustName(userName);
            }
            pool.execute(new SaveLogThread(channelLoginLog));
        }catch(Exception e){
            log.error("日志记录异常。", e);
            return false;
        }
        return true;
    }

    public static class SaveLogThread extends Thread{
        private ChannelLoginLog channelLoginLog;
        public SaveLogThread(ChannelLoginLog channelLoginLog){
            this.channelLoginLog = channelLoginLog;
        }
        @Override
        public void run() {
            memberLoginLogService.saveLoginLogger(channelLoginLog);
        }
    }
}
