package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoResult;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandModifyPwdCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;

/**
 * 宽带专区-宽带账号Controller
 * Created by zangnc on 2016/5/14.
 */
@Controller
@RequestMapping("broadbandAccount")
public class BroadbandAccountController {
	private static String ACCOUNT_QRY_COUNT = "PC_BROAD_BAND_ACCOUNT_SMS_COUNT_";
    private static String ACCOUNT_QRY_CODE = "PC_BROAD_BAND_ACCOUNT_SMS_CODE_";
    private int SMSOUT = 60 * 60 * 24;//设置24小时缓存
    private int SECONDSSMSOUT = 60;
    private Logger logger = Logger.getLogger(BroadbandAccountController.class);

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef"
            + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";
    
    @Autowired
    private SmsSendService smsSendService;
    
    @Autowired
    private BroadBandService broadBandServiceImpl;
    
	@RequestMapping(value = "/accQry")
	public String accQry(HttpServletRequest request,
			HttpServletResponse response,Model model) {
		
		return "web/broadband/account/accQry";
	}
	
	@RequestMapping(value = "/accReset")
	public String accReset(HttpServletRequest request,
			HttpServletResponse response,Model model) {
        return "web/broadband/account/accReset";
	}
	
	/**
     * 发送短信验证码
     *
     * @param request
     * @param response
     * @param mobile   手机号
     * @return
     */
    @RequestMapping(value = "sendSms", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String sendSms(HttpServletRequest request,
                          HttpServletResponse response, String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return "手机号码不能为空！";
        }
        if (JedisClusterUtils.exists(ACCOUNT_QRY_CODE + mobile)) {
            return "您的短信密码已发送到手机，1分钟内不重复发送";
        }
        
        String countstr = JedisClusterUtils.get(ACCOUNT_QRY_COUNT + mobile);
        Integer count = 0;
        if (StringUtils.isNotEmpty(countstr)) {
            count = Integer.parseInt(countstr);
        }
        logger.info(count);
        if(count>=10){
            return "短信验证码发送超过限制，请明天再来！";
        }

        if (StringUtils.isNotBlank(mobile)) {
            String vcode = getFixLengthString(6);
            // 发送短信码
            logger.info("随机短信密码：" + vcode);
            sendSms(mobile, "尊敬的用户，您的短信随机验证码是：" + vcode + "。中国移动不会以任何方式向您索取该密码，请勿告知他人。");
            JedisClusterUtils.set(ACCOUNT_QRY_CODE + mobile, vcode, SECONDSSMSOUT);
            count++;
            JedisClusterUtils.set(ACCOUNT_QRY_COUNT + mobile, count
                    + "", SMSOUT);
            return "success";
        } else {
            return "系统繁忙，请稍后再试";
        }
    }

    /**
     * 宽带信息查询
     *
     * @param request
     * @param response
     * @param mobile
     * @param smsCaptcha
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "accountQry", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkSms_accountQry(HttpServletRequest request,
                           HttpServletResponse response, String mobile, String smsCaptcha) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode","-1");
        if(StringUtils.isBlank(mobile)){
            resultMap.put("respDesc","手机号码不能为空！");
            return resultMap;
        }
        // 校验短信密码
        if (JedisClusterUtils.exists(ACCOUNT_QRY_CODE + mobile)) {
            String vcode = JedisClusterUtils.get(ACCOUNT_QRY_CODE + mobile);
            if (vcode.equals(smsCaptcha)) {
                JedisClusterUtils.del(ACCOUNT_QRY_CODE + mobile);
                logger.info("验证码正确");
                
                //查询宽带信息
                BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
    			condition.setSerialNumber(mobile);
    			BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandServiceImpl.broadbandDetailInfo(condition);
    			if(broadbandDetailInfoResult.getBroadbandDetailInfo() == null){
    				resultMap.put("respCode","9999");
    				resultMap.put("respDesc","此号码未开通湖南移动宽带，无法查到宽带信息！");
    			}else{
    				resultMap.put("respCode","0");
    				resultMap.put("accessAcct", broadbandDetailInfoResult.getBroadbandDetailInfo().getAccessAcct());
    				resultMap.put("rate", broadbandDetailInfoResult.getBroadbandDetailInfo().getRate());
    				resultMap.put("mbh", broadbandDetailInfoResult.getBroadbandDetailInfo().getMbhDetailInfos());
    				resultMap.put("startTime", broadbandDetailInfoResult.getBroadbandDetailInfo().getNewProductsInfo().get(0).getStart_date());
    				resultMap.put("endTime", broadbandDetailInfoResult.getBroadbandDetailInfo().getNewProductsInfo().get(0).getEnd_date());
    				logger.info(broadbandDetailInfoResult.getBroadbandDetailInfo());
    			}
            } else {
                resultMap.put("respDesc","短信密码错误");
            }
        } else {
            resultMap.put("respDesc","短信密码已失效");
        }
        return resultMap;
    }

    /**
     * 宽带密码修改
     *
     * @param request
     * @param response
     * @param mobile
     * @param smsCaptcha
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "accountReset", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkSms_accountReset(HttpServletRequest request,
                           HttpServletResponse response, String mobile, String smsCaptcha,
                           String psptId,String accessAcct,String newPwd,String confirm_newPwd) throws Exception {
        psptId =  TriDes.getInstance()
                .strDec(psptId, keyStr, null, null);
        newPwd =  TriDes.getInstance()
                .strDec(newPwd, keyStr, null, null);
        confirm_newPwd = TriDes.getInstance()
                .strDec(confirm_newPwd, keyStr, null, null);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode","-1");
        if(StringUtils.isBlank(mobile)){
            resultMap.put("respDesc","手机号码不能为空！");
            return resultMap;
        }
        if(StringUtils.isBlank(psptId)){
            resultMap.put("respDesc","身份证号码不能为空！");
            return resultMap;
        }
        if(StringUtils.isBlank(accessAcct)){
            resultMap.put("respDesc","宽带账号不能为空！");
            return resultMap;
        }
        if(StringUtils.isBlank(newPwd)){
            resultMap.put("respDesc","新密码不能为空！");
            return resultMap;
        }
        if(StringUtils.isBlank(confirm_newPwd)){
            resultMap.put("respDesc","确认新密码不能为空！");
            return resultMap;
        }
        if(!newPwd.equals(confirm_newPwd)){
            resultMap.put("respDesc","两次输入密码必须一致！");
            return resultMap;
        }
        // 校验短信密码
        if (JedisClusterUtils.exists(ACCOUNT_QRY_CODE + mobile)) {
            String vcode = JedisClusterUtils.get(ACCOUNT_QRY_CODE + mobile);
            if (vcode.equals(smsCaptcha)) {
                JedisClusterUtils.del(ACCOUNT_QRY_CODE + mobile);
                logger.info("验证码正确");
                
                //更改宽带密码
                BroadbandModifyPwdCondition condition = new BroadbandModifyPwdCondition();
    			condition.setSerialNumber(mobile);
    			condition.setPsptId(psptId);
    			condition.setAccessAcct(accessAcct);
    			condition.setNewPwd(confirm_newPwd);
    			Map<String,Object> modifyMap = broadBandServiceImpl.modifyPwd(condition);
    			if(modifyMap.get("respCode").equals("0")){
    				resultMap.put("respCode","0");
    				resultMap.put("respDesc","宽带密码修改成功");
    				logger.info("宽带密码修改成功");
    			}else{
    				resultMap.put("respDesc","宽带密码修改失败");
    				logger.info("宽带密码修改失败");
    			}
            } else {
                resultMap.put("respDesc","短信密码错误");
            }
        } else {
            resultMap.put("respDesc","短信密码已失效");
        }
        return resultMap;
    }

    private String getFixLengthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        String fixLengthString = String.valueOf(pross);
        return fixLengthString.substring(1, strLength + 1);
    }

    /**
     * 发送短信
     *
     * @param serialNumber
     * @param noticeContent
     */
    private void sendSms(String serialNumber, String noticeContent) {
        SmsSendCondition condition = new SmsSendCondition();
        condition.setSerialNumber(serialNumber);
        condition.setNoticeContent(noticeContent);
        try {
            smsSendService.sendSms(condition);
        } catch (Exception e) {
            logger.error("==smsSendService.sendSm   s==:", e);
        }
    }
}
