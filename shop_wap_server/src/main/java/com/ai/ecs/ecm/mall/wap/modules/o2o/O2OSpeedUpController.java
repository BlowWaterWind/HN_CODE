package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.broadband.entity.BroadbandSpeedUpRecord;
import com.ai.ecs.common.utils.JedisClusterUtils;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.sms.entity.SmsSendCondition;
import com.ai.ecs.ecsite.modules.sms.service.SmsSendService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("o2oSpeedUp")
public class O2OSpeedUpController extends BaseController {
    private static String SPEED_UP_COUNT = "WAP_BROAD_BAND_SPEED_UP_SMS_COUNT_";
    private static String SPEED_UP_CODE = "WAP_BROAD_BAND_SPEED_UP_SMS_CODE_";
    private int SMSOUT = 60 * 60 * 24;
    private static String SPEED_UP_20M = "20M";
    private static String SPEED_UP_50M = "50M";
    private static String SPEED_UP_100M = "100M";
    private int JEDISTIMEOUT = 1800;

    private int SECONDSSMSOUT = 60;

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";


    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private DictService dictService;

    @Autowired
    private O2oOrderParamTemService o2oOrderParamTemService;

    @Autowired
    private O2oOrderTempService o2oOrderTempService;

    @RequestMapping("queryAccount")
    public String queryAccount(String serialNumber, Model model) throws Exception {

        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp queryAccount serialNumber:" + serialNumber);

        if (StringUtils.isBlank(serialNumber)) {
            throw new Exception("手机号码不能为空！");
        }

        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(serialNumber);
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp queryAccount broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
        BroadbandDetailInfoResult result = null;
        try {
            result = broadBandService.broadbandDetailInfo(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统错误，请稍后再试");
        }
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp queryAccount broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());

        //有宽带进入宽带帐号选择页面
        if (result == null || result.getBroadbandDetailInfo() == null) {
            return "web/broadband/o2o/common/broadBandWithNoAccount";
        }
        BroadbandDetailInfo info = result.getBroadbandDetailInfo();
//                //一个手机号码存在绑定多个帐号的情况，但接口只返回一个帐号，待确认 先做多个的准备
        List<BroadbandSpeedUpRecord> list = new ArrayList<BroadbandSpeedUpRecord>();
        BroadbandSpeedUpRecord record = new BroadbandSpeedUpRecord();
        record.setBandAccount(info.getAccessAcct());
        record.setProductInfo(info.getDiscntName());
        record.setOldBandWidth(info.getRate());
        record.setAddress(info.getAddrDesc());
        record.setCustName(info.getCustName());
        record.setSerialNumber(info.getSerialNumber());
        record.setPsptId(info.getPsptId());
        record.setEndTime(DateUtils.StringToDateTime(info.getEndTime()));
        if (StringUtils.isNotBlank(info.getProductsInfo())) {
            JSONArray array = JSONArray.parseArray(info.getProductsInfo());
            com.alibaba.fastjson.JSONObject json = array.getJSONObject(0);
            if (StringUtils.isNotBlank(json.getString("START_DATE"))) {
                record.setStartTime(DateUtils.StringToDateTime(json.getString("START_DATE")));
                String surplusDays = DateUtils.differDays(info.getEndTime().replaceAll("-", "/"));
                record.setSurplusDays(Integer.parseInt(surplusDays));
            }
        }
        list.add(record);
        model.addAttribute("speedUpList", list);
        model.addAttribute("serialNumber", serialNumber);
        return "web/broadband/o2o/speedUp/broadBandAccountList";
    }

    /**
     * 宽带提速页面跳转
     *
     * @param request
     * @return
     */
    @RequestMapping("toSpeedUp")
    public String toBroadbandSpeedUp(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        return "web/broadband/o2o/speedUp/queryAccount";
    }


    /**
     * 保存到订单临时表
     *
     * @param request
     * @return
     */
    @RequestMapping("saveOrderTemp")
    @ResponseBody
    public Map<String, Object> saveOrderTemp(HttpServletRequest request, HttpServletResponse response, Model model, BroadbandSpeedUpRecord bdrecord,String staffPwd) throws Exception {
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp handleSpeedUp record " + JSONObject.fromObject(bdrecord).toString());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode", "0");
        //保存到临时表
        try {
            MemberVo memberVo=UserUtils.getLoginUser(request);
            ChannelInfo channelInfo=memberVo.getChannelInfo();
            O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
            orderTempInfo.setChannel_code(BroadbandConstants.CHANNEL_ID_O2O_CHANNEL);
            orderTempInfo.setContact_phone(bdrecord.getSerialNumber());
            orderTempInfo.setEparchy_code(this.getEparchyCode(bdrecord.getSerialNumber()));
            MemberLogin memberLogin=UserUtils.getLoginUser(request).getMemberLogin();
            orderTempInfo.setMember_id(memberLogin.getMemberId());
            orderTempInfo.setMember_loging_name(memberLogin.getMemberLogingName());
            orderTempInfo.setGoods_name("宽带提速"+bdrecord.getNewBandWidth());
            orderTempInfo.setOrder_type_id(19L);
            orderTempInfo.setGoods_pay_price(Long.parseLong(bdrecord.getPayAmount()));
            orderTempInfo.setInsert_time(new Date());
            ShopInfo shopInfo=memberVo.getShopInfo();
            if(shopInfo!=null){
                orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));
                orderTempInfo.setShop_name(shopInfo.getShopName());
            }
            Long orderTempId=o2oOrderTempService.insert(orderTempInfo);
            List<O2oOrderParamTemp> list = new ArrayList<>();
            O2oOrderParamTemp o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("BAND_ACCOUNT");
            o2oOrderParamTemp.setParamValue(bdrecord.getBandAccount());
            o2oOrderParamTemp.setParamDesc("宽带帐号");
            list.add(o2oOrderParamTemp);
            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("SERIAL_NUMBER");
            o2oOrderParamTemp.setParamValue(bdrecord.getSerialNumber());
            o2oOrderParamTemp.setParamDesc("手机号码");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("CUST_NAME");
            o2oOrderParamTemp.setParamValue(bdrecord.getCustName());
            o2oOrderParamTemp.setParamDesc("客户姓名");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("PRODUCT_INFO");
            o2oOrderParamTemp.setParamValue(bdrecord.getProductInfo());
            o2oOrderParamTemp.setParamDesc("套餐名称");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("ADDRESS");
            o2oOrderParamTemp.setParamValue(bdrecord.getAddress());
            o2oOrderParamTemp.setParamDesc("安装地址");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("PSPT_ID");
            o2oOrderParamTemp.setParamValue(bdrecord.getPsptId());
            o2oOrderParamTemp.setParamDesc("身份证号码");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("NEW_BAND_WIDTH");
            o2oOrderParamTemp.setParamValue(bdrecord.getNewBandWidth());
            o2oOrderParamTemp.setParamDesc("提升速率");
            list.add(o2oOrderParamTemp);
            //保存工号信息
            list= putChannelParams(list,channelInfo,orderTempInfo.getEparchy_code());

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("TRADE_DEPART_PASSWD");
            o2oOrderParamTemp.setParamValue(staffPwd);
            o2oOrderParamTemp.setParamDesc("渠道交易部门密码");
            list.add(o2oOrderParamTemp);

            for(O2oOrderParamTemp paramTemp:list){
                paramTemp.setOrderTempId(new BigDecimal(orderTempId));
            }
            o2oOrderParamTemService.batchInsert(list);
            resultMap.put("orderId",orderTempId);
        } catch (Exception e) {
            resultMap.put("respCode", "-1");
            resultMap.put("respDesc", "系统繁忙，稍后再试！");

        }
        return resultMap;
    }


    /**
     * 获取手机号码的归属地市编码
     *
     * @param installPhoneNum
     * @return
     * @throws Exception
     */
    private String getEparchyCode(String installPhoneNum) throws Exception {
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(installPhoneNum);
        Map<String, Object> resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        return String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
    }

    /**
     * 宽带提速选择帐号
     *
     * @param request
     * @return
     */
    @RequestMapping(value="confirmAccount", method = RequestMethod.POST)
    public String confirmAccount(HttpServletRequest request, HttpServletResponse response, Model model, BroadbandSpeedUpRecord record) throws Exception {
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp confirmAccount record " + JSONObject.fromObject(record).toString());
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(record.getSerialNumber());
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp confirmAccount broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
        BroadbandDetailInfoResult result = null;
        try {
            result = broadBandService.broadbandDetailInfo(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统错误，请稍后再试");
        }
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp confirmAccount broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());
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
        model.addAttribute("speedupRecord", record);
        return "web/broadband/o2o/speedUp/broadBandSpeedUp";
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
        if (count >= 10) {
            return "短信验证码发送超过限制，请明天再来！";
        }

        if (StringUtils.isNotBlank(mobile)) {
            String vcode = getFixLengthString(6);
            // 发送短信码
            LoggerFactory.getLogger("webDbLog").info("随机短信密码：" + vcode);
            sendSms(mobile, "尊敬的用户，您的随机验证码为：" + vcode);
            JedisClusterUtils.set(SPEED_UP_CODE + mobile, vcode, SECONDSSMSOUT);
            count++;
            JedisClusterUtils.set(SPEED_UP_COUNT + mobile, count
                    + "", SMSOUT);
            return "success";
        } else {
            return "系统繁忙，请稍后再试";
        }
    }

    /**
     * 校验接口
     *
     * @param request
     * @param response
     * @param
     * @return
     */
    @RequestMapping(value = "checkSpeedUp", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkSpeedUp(HttpServletRequest request, BroadbandSpeedUpRecord record,String staffPwd,
                                            HttpServletResponse response) {

            MemberVo memberVo=UserUtils.getLoginUser(request);
            MemberLogin memberLogin=UserUtils.getLoginUser(request).getMemberLogin();
            ChannelInfo channelInfo=memberVo.getChannelInfo();
            staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
            LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp checkSpeedUp record " + JSONObject.fromObject(record).toString());
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("respCode", "-1");
            resultMap.put("respDesc", "提交失败！");
            if (StringUtils.isBlank(record.getSerialNumber()) || StringUtils.isBlank(record.getBandAccount()) || StringUtils.isBlank(record.getPayAmount())) {
                resultMap.put("respDesc", "参数不能为空");
                return resultMap;
            }
        try {
            //校验接口
            if ("0".equals(record.getPayAmount()) && SPEED_UP_20M.equals(record.getNewBandWidth())) {
                checkForFree(resultMap, record, channelInfo,staffPwd);
            } else {
                checkForPay(resultMap, record, channelInfo,staffPwd);
            }
            if(!"0".equals(resultMap.get("respCode"))){
                return resultMap;
            }
            //保存到临时表

            O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
            orderTempInfo.setChannel_code(BroadbandConstants.CHANNEL_ID_O2O_CHANNEL);
            orderTempInfo.setContact_phone(record.getSerialNumber());
            orderTempInfo.setEparchy_code(this.getEparchyCode(record.getSerialNumber()));

            orderTempInfo.setMember_id(memberLogin.getMemberId());
            orderTempInfo.setMember_loging_name(memberLogin.getMemberLogingName());
            orderTempInfo.setGoods_name("宽带提速"+record.getNewBandWidth());
            orderTempInfo.setOrder_type_id(19L);
            orderTempInfo.setGoods_pay_price(Long.parseLong(record.getPayAmount()));
            orderTempInfo.setInsert_time(new Date());
            ShopInfo shopInfo=memberVo.getShopInfo();
            if(shopInfo!=null){
                orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));
                orderTempInfo.setShop_name(shopInfo.getShopName());
            }
            Long orderTempId=o2oOrderTempService.insert(orderTempInfo);
            List<O2oOrderParamTemp> list = new ArrayList<>();
            O2oOrderParamTemp o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("BAND_ACCOUNT");
            o2oOrderParamTemp.setParamValue(record.getBandAccount());
            o2oOrderParamTemp.setParamDesc("宽带帐号");
            list.add(o2oOrderParamTemp);
            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("SERIAL_NUMBER");
            o2oOrderParamTemp.setParamValue(record.getSerialNumber());
            o2oOrderParamTemp.setParamDesc("手机号码");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("CUST_NAME");
            o2oOrderParamTemp.setParamValue(record.getCustName());
            o2oOrderParamTemp.setParamDesc("客户姓名");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("PRODUCT_INFO");
            o2oOrderParamTemp.setParamValue(record.getProductInfo());
            o2oOrderParamTemp.setParamDesc("套餐名称");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("ADDRESS");
            o2oOrderParamTemp.setParamValue(record.getAddress());
            o2oOrderParamTemp.setParamDesc("安装地址");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("PSPT_ID");
            o2oOrderParamTemp.setParamValue(record.getPsptId());
            o2oOrderParamTemp.setParamDesc("身份证号码");
            list.add(o2oOrderParamTemp);

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("NEW_BAND_WIDTH");
            o2oOrderParamTemp.setParamValue(record.getNewBandWidth());
            o2oOrderParamTemp.setParamDesc("提升速率");
            list.add(o2oOrderParamTemp);
            //保存工号信息
            list= putChannelParams(list,channelInfo,orderTempInfo.getEparchy_code());

            o2oOrderParamTemp = new O2oOrderParamTemp();
            o2oOrderParamTemp.setParamName("TRADE_DEPART_PASSWD");
            o2oOrderParamTemp.setParamValue(staffPwd);
            o2oOrderParamTemp.setParamDesc("渠道交易部门密码");
            list.add(o2oOrderParamTemp);

            for(O2oOrderParamTemp paramTemp:list){
                paramTemp.setOrderTempId(new BigDecimal(orderTempId));
            }
            o2oOrderParamTemService.batchInsert(list);
            resultMap.put("orderId",orderTempId);
        } catch (Exception e) {
            resultMap.put("respCode", "-1");
            resultMap.put("respDesc", "系统繁忙，稍后再试！");
        }
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp checkSpeedUp resultMap" + resultMap.toString());
        return resultMap;
    }

    /**
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "comfirmLevel",method = RequestMethod.POST)
    public String comfirmLevel(BroadbandSpeedUpRecord record, Model model) throws Exception {
        LoggerFactory.getLogger("webDbLog").info("o2oSpeedUp comfirmLevel record " + JSONObject.fromObject(record).toString());
        model.addAttribute("speedupRecord", record);
        return "web/broadband/o2o/speedUp/broadbandComfirmLevel";
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
     * 收费提速校验 办理
     *
     * @param resultMap  返回结果
     * @param record
     * @return
     */
    private Map<String, Object> checkForPay(Map<String, Object> resultMap, BroadbandSpeedUpRecord record, ChannelInfo channelInfo,String password) {
        SpeedForPayCheckCondition condition = new SpeedForPayCheckCondition();
        condition.setKdSerialNumber(record.getBandAccount());
        condition.setSerialNumber(record.getSerialNumber());
        condition.setRate(record.getNewBandWidth());
        Map<String, Object> map = null;
        try {
            String cityCode=this.getEparchyCode(record.getSerialNumber());
            condition.setCityCode(cityCode);
            condition.setEparchyCodeFCust(cityCode);
            condition.setTradeDepartPassword(password);
            condition.setDepartId(channelInfo.getTradeDepartId());
            condition.setStaffId(channelInfo.getTradeStaffId());
            condition.setInModeCode(channelInfo.getInModeCode());
            condition.setChanId(channelInfo.getChanelId());
            LoggerFactory.getLogger("webDbLog").info("=======o2oSpeedUp SpeedForPayCheck param：" + JSONObject.fromObject(condition).toString());
            map = broadBandService.speedForPayCheck(condition);
            resultMap.put("respDesc", map.get("respDesc"));
        } catch (Exception e) {
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        LoggerFactory.getLogger("webDbLog").info("====o2oSpeedUp SpeedForPayCheck result：" + map);
        try {
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("1".equals(resultObj.get("FLAG"))) {//已经合户
                    resultMap.put("respCode", "0");
                } else {
                    resultMap.put("respDesc", "宽带帐号和手机号未合户");
                }
            }
        } catch (Exception e) {
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 免费提速校验 办理
     *
     * @param resultMap  返回结果
     * @param record
     * @return
     */
    private Map<String, Object> checkForFree(Map<String, Object> resultMap, BroadbandSpeedUpRecord record, ChannelInfo channelInfo,String password) {
        SpeedForFreeCheckCondition condition = new SpeedForFreeCheckCondition();
        condition.setKdSerialNumber(record.getBandAccount());
        condition.setSerialNumber(record.getSerialNumber());
        Map<String, Object> map = null;
        try {
            String cityCode=this.getEparchyCode(record.getSerialNumber());
            condition.setCityCode(cityCode);
            condition.setEparchyCodeFCust(cityCode);
            condition.setTradeDepartPassword(password);
            condition.setDepartId(channelInfo.getTradeDepartId());
            condition.setStaffId(channelInfo.getTradeStaffId());
            condition.setInModeCode(channelInfo.getInModeCode());
            condition.setChanId(channelInfo.getChanelId());
            LoggerFactory.getLogger("webDbLog").info("=======o2oSpeedUp SpeedForFreeCheck param：" + JSONObject.fromObject(condition).toString());
            map = broadBandService.speedForFreeCheck(condition);
            resultMap.put("respDesc", map.get("respDesc"));
        } catch (Exception e) {
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        LoggerFactory.getLogger("webDbLog").info("====o2oSpeedUp SpeedForFreeCheck result：" + map);
        try {
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("1".equals(resultObj.get("FLAG"))) {//已经合户
                    resultMap.put("respCode", "0");
                } else {
                    resultMap.put("respDesc", "宽带帐号和手机号未合户");
                }
            }
        } catch (Exception e) {
            resultMap.put("respDesc", "接口调用异常");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 从session中获取o2o传递的交易参数
     * @param paramList
     * @param channelInfo
     * @param
     */
    private List<O2oOrderParamTemp> putChannelParams(List<O2oOrderParamTemp> paramList, ChannelInfo channelInfo,String cityCode){
        O2oOrderParamTemp param1 = new O2oOrderParamTemp();
        param1.setParamName("ROUTE_EPARCHY_CODE");
        param1.setParamValue(cityCode);
        param1.setParamDesc("渠道用户市区编号");
        O2oOrderParamTemp param2 = new O2oOrderParamTemp();
        param2.setParamName("IN_MODE_CODE");
        param2.setParamValue(channelInfo.getInModeCode());
        param2.setParamDesc("渠道交易类型");
        O2oOrderParamTemp param3 = new O2oOrderParamTemp();
        param3.setParamName("TRADE_DEPART_ID");
        param3.setParamValue(channelInfo.getTradeDepartId());
        param3.setParamDesc("渠道交易部门编号");
        O2oOrderParamTemp param4 = new O2oOrderParamTemp();
        param4.setParamName("TRADE_CITY_CODE");
        param4.setParamValue(cityCode);
        param4.setParamDesc("渠道交易市区编号");
        O2oOrderParamTemp param5 = new O2oOrderParamTemp();
        param5.setParamName("TRADE_EPARCHY_CODE");
        param5.setParamValue(cityCode);
        param5.setParamDesc("渠道交易区域编码");
        O2oOrderParamTemp param6 = new O2oOrderParamTemp();
        param6.setParamName("TRADE_STAFF_ID");
        param6.setParamValue(channelInfo.getTradeStaffId());
        param6.setParamDesc("渠道交易工号");
        paramList.add(param1);
        paramList.add(param2);
        paramList.add(param3);
        paramList.add(param4);
        paramList.add(param5);
        paramList.add(param6);
        return paramList;
    }

}
