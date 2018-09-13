package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.broadband.api.IBroadBandService;
import com.ai.ecs.broadband.entity.BroadbandSpeedUpRecord;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by tanpei on 2017/3/1.
 * 宽带提速
 */
@Controller
@RequestMapping("broadbandSpeedUp")
public class BroadbandSpeedUpController extends BaseController {
    private static String SPEED_UP_COUNT = "WAP_BROAD_BAND_SPEED_UP_SMS_COUNT_";
    private static String SPEED_UP_CODE = "WAP_BROAD_BAND_SPEED_UP_SMS_CODE_";
    private int SMSOUT = 60 * 60 * 24;
    private static String SPEED_UP_20M = "20M";
    private static String SPEED_UP_50M = "50M";
    private static String SPEED_UP_100M = "100M";
    private int JEDISTIMEOUT = 1800;

    private int SECONDSSMSOUT = 60;

    @Autowired
    GoodsService goodsService;

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private DictService dictService;

    @Autowired
    private IBroadBandService broadBandGoodsService;


    private Logger logger = Logger.getLogger(BroadbandSpeedUpController.class);


    /**
     * 宽带提速页面跳转
     *
     * @param request
     * @return
     */
    @RequestMapping("toSpeedUp")
    public String toBroadbandSpeedUp(HttpServletRequest request, HttpServletResponse response, Model model)throws Exception  {
            Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
//            Long serialNumber = 15207485252L;
            logger.info("speedup serialNumber"+serialNumber);
            if (serialNumber == null || serialNumber == 0L) {
                logger.info("您的帐号没有绑定手机号码");
                throw new Exception("您的帐号没有绑定手机号码");
            }
            BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
            condition.setSerialNumber(String.valueOf(serialNumber));
            logger.info("broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
            BroadbandDetailInfoResult result = null;
            try {
                result = broadBandService.broadbandDetailInfo(condition);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("系统错误，请稍后再试");
            }
            logger.info("broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());

            //有宽带进入宽带帐号选择页面
            if (result == null || result.getBroadbandDetailInfo() == null) {
                return "web/goods/broadband/speedUp/broadBandWithNoAccount";
            }
            BroadbandDetailInfo info = result.getBroadbandDetailInfo();
//                //一个手机号码存在绑定多个帐号的情况，但接口只返回一个帐号，待确认 先做多个的准备
            List<BroadbandSpeedUpRecord> list = new ArrayList<BroadbandSpeedUpRecord>();
            BroadbandSpeedUpRecord record=new BroadbandSpeedUpRecord();
            record.setBandAccount(info.getAccessAcct());
            record.setProductInfo(info.getDiscntName());
            record.setOldBandWidth(info.getRate());
            record.setAddress(info.getAddrDesc());
            record.setCustName(info.getCustName());
            record.setSerialNumber(info.getSerialNumber());
            record.setPsptId(info.getPsptId());
            record.setEndTime(DateUtils.StringToDateTime(info.getEndTime()));
        if(StringUtils.isNotBlank(info.getProductsInfo())){
                com.alibaba.fastjson.JSONArray array=com.alibaba.fastjson.JSONArray.parseArray(info.getProductsInfo());
                com.alibaba.fastjson.JSONObject json = array.getJSONObject(0);
                if(StringUtils.isNotBlank(json.getString("START_DATE"))) {
                    record.setStartTime(DateUtils.StringToDateTime(json.getString("START_DATE")));
                    String surplusDays = DateUtils.differDays(info.getEndTime().replaceAll("-", "/"));
                    record.setSurplusDays(Integer.parseInt(surplusDays));
                }
            }
            list.add(record);
            model.addAttribute("speedUpList", list);
            model.addAttribute("serialNumber",serialNumber);
            return "web/goods/broadband/speedUp/broadBandAccountList";
    }


    /**
     * 宽带提速办理
     *
     * @param request
     * @return
     */
    @RequestMapping("handleSpeedUp")
    public String handleSpeedUp(HttpServletRequest request, HttpServletResponse response, Model model,BroadbandSpeedUpRecord bdrecord) throws Exception{
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("respCode", "-1");
//		办理提速：
//		1.查询选择的宽带帐号，判断是否是登录手机号绑定的
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
//            Long serialNumber = 15207485252L;
            if (serialNumber == null || serialNumber == 0L) {
                resultMap.put("respDesc", "您的登录帐号未绑定手机号码");
                throw new Exception("您的登录帐号未绑定手机号码");
            }

            bdrecord.setCityCode(this.getEparchyCode(bdrecord.getSerialNumber()));
            //校验
            if ("0".equals(bdrecord.getPayAmount()) && SPEED_UP_20M.equals(bdrecord.getNewBandWidth())) {//免费提速
                //先校验
                bdrecord.setIsFree("1");
                checkForFree(resultMap, bdrecord, true);
            } else {//付费提速
                bdrecord.setIsFree("0");
                checkForPay(resultMap, bdrecord, true);
            }
            // 入库（失败的也记录）
            bdrecord.setMemberId(UserUtils.getLoginUser(request).getMemberLogin().getMemberId());
            bdrecord.setChannelCode(BroadbandConstants.CHANNEL_ID_WAP);
            bdrecord.setRecordTime(new Date());
            bdrecord.setRespCode(String.valueOf(resultMap.get("respCode")));
            bdrecord.setRespDesc(String.valueOf(resultMap.get("respDesc")));
            Long id = broadBandGoodsService.saveBroadBandSpeedUpRecord(bdrecord);
            bdrecord.setRecordId(id);
            model.addAttribute("record", bdrecord);
        return "web/goods/broadband/speedUp/payResult";
    }


    /**
	 * 获取手机号码的归属地市编码
	 * @param installPhoneNum
	 * @return
	 * @throws Exception 
	 */
	private String getEparchyCode(String installPhoneNum) throws Exception{
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(installPhoneNum);
        Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        return String.valueOf(((Map)((List)resultMap.get("result")).get(0)).get("AREA_CODE"));
	}
    
    /**
     * 宽带提速选择帐号
     *
     * @param request
     * @return
     */
    @RequestMapping("confirmAccount")
    public String confirmAccount(HttpServletRequest request, HttpServletResponse response, Model model, BroadbandSpeedUpRecord record) throws Exception{
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        LoggerFactory.getLogger("webDbLog").info("broadbandSpeedUp confirmAccount record " + JSONObject.fromObject(record).toString());
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(String.valueOf(serialNumber));
        LoggerFactory.getLogger("webDbLog").info("broadbandSpeedUp confirmAccount broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
        BroadbandDetailInfoResult result = null;
        try {
            result = broadBandService.broadbandDetailInfo(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统错误，请稍后再试");
        }
        LoggerFactory.getLogger("webDbLog").info("broadbandSpeedUp confirmAccount broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());
        BroadbandDetailInfo info = result.getBroadbandDetailInfo();
//                //一个手机号码存在绑定多个帐号的情况，但接口只返回一个帐号，待确认 先做多个的准备
        if (info == null || !info.getAccessAcct().equals(record.getBandAccount())) {
            throw new Exception("宽带帐号异常!");
        }
        record.setProductInfo(info.getDiscntName());
        record.setAddress(info.getAddrDesc());
        record.setCustName(info.getCustName());
        record.setPsptId(info.getPsptId());
        record.setEndTime(DateUtils.StringToDateTime(info.getEndTime()));
        if (StringUtils.isNotBlank(info.getProductsInfo())) {
            JSONArray array = JSONArray.parseArray(info.getProductsInfo());
            com.alibaba.fastjson.JSONObject json = array.getJSONObject(0);
            if (StringUtils.isNotBlank(json.getString("START_DATE"))) {
                record.setStartTime(DateUtils.StringToDateTime(json.getString("START_DATE")));
            }
        }
        List<String> rateList = new ArrayList<String>();
        int rate = Integer.parseInt(record.getOldBandWidth());
        Map<String, Integer> map = new HashMap<String, Integer>();
        int m20 = Integer.parseInt(dictService.getDictValue("BROADBAND_SPEED_UP_PRICE", "BROADBAND_SPEED_UP_20M"));
        int m50 = Integer.parseInt(dictService.getDictValue("BROADBAND_SPEED_UP_PRICE", "BROADBAND_SPEED_UP_50M"));
        int m100 = Integer.parseInt(dictService.getDictValue("BROADBAND_SPEED_UP_PRICE", "BROADBAND_SPEED_UP_100M"));
        if (rate <= 10) {
            rateList.add(SPEED_UP_20M);
            rateList.add(SPEED_UP_50M);
            rateList.add(SPEED_UP_100M);
            map.put(SPEED_UP_20M, m20);
            map.put(SPEED_UP_50M, m20 + m50);
            map.put(SPEED_UP_100M, m20 + m50 + m100);
        }
        if (rate > 10 && rate <= 20) {
            rateList.add(SPEED_UP_50M);
            rateList.add(SPEED_UP_100M);
            map.put(SPEED_UP_50M, m50);
            map.put(SPEED_UP_100M, m50 + m100);
        }
        if (rate > 20 && rate <= 50) {
            rateList.add(SPEED_UP_100M);
            map.put(SPEED_UP_100M, m100);
        }
        record.setRateList(rateList);
        record.setRateMap(map);
        record.setSerialNumber(String.valueOf(serialNumber));
        model.addAttribute("speedupRecord", record);
        return "web/goods/broadband/speedUp/broadBandSpeedUp";
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
        if (JedisClusterUtils.exists(SPEED_UP_CODE + mobile)) {
            return "您的短信密码已发送到手机，1分钟内不重复发送";
        }
        String countstr = JedisClusterUtils.get(SPEED_UP_COUNT + mobile);
        Integer count = 0;
        if (StringUtils.isNotEmpty(countstr)) {
            count = Integer.parseInt(countstr);
        }
        if(count>=10){
            return "短信验证码发送超过限制，请明天再来！";
        }

        if (StringUtils.isNotBlank(mobile)) {
            String vcode = getFixLengthString(6);
            // 发送短信码
            logger.info("随机短信密码：" + vcode);
            sendSms(mobile, "尊敬的用户，您的随机验证码为：" + vcode);
            JedisClusterUtils.set(SPEED_UP_CODE + mobile, vcode,SECONDSSMSOUT);
            count++;
            JedisClusterUtils.set(SPEED_UP_COUNT + mobile, count
                    + "", SMSOUT);
            return "success";
        } else {
            return "系统繁忙，请稍后再试";
        }
    }

    /**
     * 校验短信验证码及校验接口
     *
     * @param request
     * @param response
     * @param mobile
     * @param smsCaptcha
     * @return
     */
    @RequestMapping(value = "checkSpeedUp", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkSpeedUp(HttpServletRequest request,
                           HttpServletResponse response, String mobile, String smsCaptcha,String bandAccount,String newBandWidth,String payAmount) {
        logger.info("checkSpeedUp bandAccount"+bandAccount);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode","-1");
        if(StringUtils.isBlank(mobile)||StringUtils.isBlank(bandAccount)||StringUtils.isBlank(newBandWidth)){
            resultMap.put("respDesc","参数不能为空");
            return resultMap;
        }
        // 校验短信密码
        if (JedisClusterUtils.exists(SPEED_UP_CODE + mobile)) {
            String vcode = JedisClusterUtils.get(SPEED_UP_CODE + mobile);
            if (vcode.equals(smsCaptcha)) {
                JedisClusterUtils.del(SPEED_UP_CODE + mobile);
                logger.info("验证码正确");
                //校验接口
                BroadbandSpeedUpRecord record=new BroadbandSpeedUpRecord();
                record.setSerialNumber(mobile);
                record.setBandAccount(bandAccount);
                record.setNewBandWidth(newBandWidth);
                if("0".equals(payAmount)&&SPEED_UP_20M.equals(newBandWidth)){
                    checkForFree(resultMap,record,false);
                }else{
                    checkForPay(resultMap,record,false);
                }
                logger.info(resultMap);
            } else {
                resultMap.put("respDesc","短信密码错误");
            }
        } else {
            resultMap.put("respDesc","短信密码已失效");
        }
        return resultMap;
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "comfirmLevel")
    public String comfirmLevel(HttpServletRequest request,
                                           HttpServletResponse response,BroadbandSpeedUpRecord record,Model model)throws Exception {
        LoggerFactory.getLogger("webDbLog").info("broadbandSpeedUp comfirmLevel record "+JSONObject.fromObject(record).toString());
        model.addAttribute("speedupRecord", record);
        return "web/goods/broadband/speedUp/broadbandComfirmLevel";
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


    /**
     *收费提速校验 办理
     * @param resultMap 返回结果
     * @param record
     * @param needHandle 是否需要办理提速（值为false只作校验）
     * @return
     */
    private Map<String,Object> checkForPay(Map<String, Object> resultMap, BroadbandSpeedUpRecord record,boolean needHandle) {
        SpeedForPayCheckCondition condition = new SpeedForPayCheckCondition();
        condition.setKdSerialNumber(record.getBandAccount());
        condition.setSerialNumber(record.getSerialNumber());
        condition.setRate(record.getNewBandWidth());
        Map<String, Object> map = null;
        logger.info("=======SpeedForPayCheck param：" + JSONObject.fromObject(condition).toString());
        try {
            map = broadBandService.speedForPayCheck(condition);
            resultMap.put("respDesc", map.get("respDesc"));
        } catch (Exception e) {
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        logger.info("====SpeedForPayCheck result：" + map);
        try {
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("1".equals(resultObj.get("FLAG"))) {//已经合户
                    if(needHandle){
                        SpeedForPayRegCondition conditionReg = new SpeedForPayRegCondition();
                        conditionReg.setSerialNumber(resultObj.getString("SERIAL_NUMBER"));
                        conditionReg.setKdSerialNumber(resultObj.getString("KD_SERIAL_NUMBER"));
                        conditionReg.setFlag("1");
                        conditionReg.setGuaranteeDiscntCode(resultObj.getString("GUARANTEE_DISCNT_CODE"));
                        conditionReg.setOldRate(resultObj.getString("OLD_RATE"));
                        conditionReg.setRate(resultObj.getString("RATE"));
                        conditionReg.setKdUserId(resultObj.getString("KD_USER_ID"));
                        conditionReg.setUserId(resultObj.getString("USER_ID"));
                        conditionReg.setUserEparchyCode(resultObj.getString("USER_EPARCHY_CODE"));
                        conditionReg.setgDiscntEndDate(resultObj.getString("G_DISCNT_END_DATE"));
                        conditionReg.setpDiscntEndDate(resultObj.getString("P_DISCNT_END_DATE"));
                        record.setCityCode(resultObj.getString("USER_EPARCHY_CODE"));
                        Map<String, Object> mapReg = null;
                        logger.info("=======SpeedForPayReg param：" + JSONObject.fromObject(conditionReg).toString());
                        try {
                            mapReg = broadBandService.speedForPayReg(conditionReg);
                        } catch (Exception e) {
                            resultMap.put("respDesc", "接口调用异常");
                            e.printStackTrace();
                        }
                        logger.info("====SpeedForPayReg result：" + mapReg);
                        resultMap.put("respDesc", mapReg.get("respDesc"));
                        if ("0".equals(mapReg.get("respCode")) && mapReg.get("result") != null) {
                            JSONArray jsonArray = (JSONArray) mapReg.get("result");
                            com.alibaba.fastjson.JSONObject result = jsonArray.getJSONObject(0);
                            record.setRsrv1(result.getString("ORDER_ID"));
                            resultMap.put("respCode", "0");
                            StringBuilder sb=new StringBuilder();
                            sb.append("尊敬的移动客户您好,您办理");
                            sb.append(Integer.parseInt(record.getPayAmount())/100);
                            sb.append("元/月提速包，宽带速率已提升至");
                            sb.append(resultObj.getString("RATE"));
                            sb.append("，有效期到期后宽带将自动回落到原有速率，客户可以继续使用宽带，按照原有速率宽带产品标准资费按月从手机账户自动扣费。【中国移动】");
                            logger.info(sb.toString());
                            sendSms(record.getSerialNumber(),sb.toString());
                        }
                    }else{
                        resultMap.put("respCode","0");
                    }
                    return resultMap;
                }else{
                    resultMap.put("respDesc", "宽带帐号和手机号未合户");
                }
            }
        }catch (Exception e){
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     *免费提速校验 办理
     * @param resultMap 返回结果
     * @param record
     * @param needHandle 是否需要办理提速（值为false只作校验）
     * @return
     */
    private Map<String,Object> checkForFree(Map<String, Object> resultMap, BroadbandSpeedUpRecord record,boolean needHandle) {
        SpeedForFreeCheckCondition condition = new SpeedForFreeCheckCondition();
        condition.setKdSerialNumber(record.getBandAccount());
        condition.setSerialNumber(record.getSerialNumber());
        Map<String, Object> map = null;
        logger.info("=======SpeedForFreeCheck param：" + JSONObject.fromObject(condition).toString());
        try {
            map = broadBandService.speedForFreeCheck(condition);
            resultMap.put("respDesc", map.get("respDesc"));
        } catch (Exception e) {
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        logger.info("====SpeedForFreeCheck result：" + map);
        try {
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("1".equals(resultObj.get("FLAG"))) {//已经合户
                    if(needHandle){
                        SpeedForFreeRegCondition conditionReg = new SpeedForFreeRegCondition();
                        conditionReg.setSerialNumber(resultObj.getString("SERIAL_NUMBER"));
                        conditionReg.setKdSerialNumber(resultObj.getString("KD_SERIAL_NUMBER"));
                        conditionReg.setFlag("1");
                        conditionReg.setGuaranteeDiscntCode(resultObj.getString("GUARANTEE_DISCNT_CODE"));
                        conditionReg.setOldRate(resultObj.getString("OLD_RATE"));
                        conditionReg.setRate(resultObj.getString("RATE"));
                        conditionReg.setKdUserId(resultObj.getString("KD_USER_ID"));
                        conditionReg.setUserId(resultObj.getString("USER_ID"));
                        conditionReg.setUserEparchyCode(resultObj.getString("USER_EPARCHY_CODE"));
                        conditionReg.setgDiscntEndDate(resultObj.getString("G_DISCNT_END_DATE"));
                        conditionReg.setpDiscntEndDate(resultObj.getString("P_DISCNT_END_DATE"));
                        record.setCityCode(resultObj.getString("USER_EPARCHY_CODE"));
                        Map<String, Object> mapReg = null;
                        logger.info("=======SpeedForFreeReg param：" + JSONObject.fromObject(conditionReg).toString());
                        try {
                            mapReg = broadBandService.speedForFreeReg(conditionReg);
                        } catch (Exception e) {
                            resultMap.put("respDesc", "接口调用异常");
                            e.printStackTrace();
                        }
                        logger.info("====SpeedForFreeReg result：" + mapReg);
                        resultMap.put("respDesc", mapReg.get("respDesc"));
                        if ("0".equals(mapReg.get("respCode")) && mapReg.get("result") != null) {
                            JSONArray jsonArray = (JSONArray) mapReg.get("result");
                            com.alibaba.fastjson.JSONObject result = jsonArray.getJSONObject(0);
                            record.setRsrv1(result.getString("ORDER_ID"));
                            resultMap.put("respCode", "0");
                            StringBuilder sb=new StringBuilder();
                            sb.append("尊敬的移动客户您好。您办理");
                            sb.append(Integer.parseInt(record.getPayAmount())/100);
                            sb.append("元/月提速包，宽带速率已提升至");
                            sb.append(resultObj.getString("RATE"));
                            sb.append("，有效期到期后宽带将自动回落到原有速率，客户可以继续使用宽带，按照原有速率宽带产品标准资费按月从手机账户自动扣费。【中国移动】");
                            sendSms(record.getSerialNumber(),sb.toString());
                        }
                    }else{
                        resultMap.put("respCode","0");
                    }
                    return resultMap;
                }else{
                    resultMap.put("respDesc", "宽带帐号和手机号未合户");
                }
            }
        }catch (Exception e){
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        return resultMap;
    }

}
