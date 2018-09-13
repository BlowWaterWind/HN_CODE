package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.broadband.api.IBroadBandService;
import com.ai.ecs.broadband.entity.BroadbandUpdStaRecord;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.login.service.LoginService;
import com.ai.ecs.ecsite.modules.login.service.entity.UserInfoGetCondition;
import com.ai.ecs.ecsite.modules.myMobile.entity.MonthBillCondition;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.myMobile.service.MonthBillService;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * Created by Administrator on 2017/9/19.
 */
@Controller
@RequestMapping("myBroadband")
public class MyBroadbandController extends BaseController {

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private IMemberAddressService memberAddressService;

    @Autowired
    QryAddressService qryAddressService;

    @Autowired
    private IBroadBandService broadBandGoodsService;

    @Autowired
    private MonthBillService monthBillService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    /**
     * 宽带账单查询
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("billQuery")
    public String billQuery(HttpServletRequest request, HttpServletResponse response, Model model )throws Exception  {
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        if (serialNumber == null || serialNumber == 0L) {
            LoggerFactory.getLogger("webDbLog").info("您的帐号没有绑定手机号码");
            throw new Exception("您的帐号没有绑定手机号码");
        }
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(String.valueOf(serialNumber));
        LoggerFactory.getLogger("webDbLog").info(" myAccount broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
        BroadbandDetailInfoResult result = null;
        try {
            result = broadBandService.broadbandDetailInfo(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统错误，请稍后再试");
        }
        LoggerFactory.getLogger("webDbLog").info(" myAccount broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());

        //无宽带进入宽带推荐页面
        if (result == null || result.getBroadbandDetailInfo() == null) {
            return "web/broadband/account/broadBandWithNoAccount";
        }
        BroadbandDetailInfo info = result.getBroadbandDetailInfo();
        if(StringUtils.isNotBlank(info.getProductsInfo())){
            com.alibaba.fastjson.JSONArray array=com.alibaba.fastjson.JSONArray.parseArray(info.getProductsInfo());
            com.alibaba.fastjson.JSONObject json = array.getJSONObject(0);
            if(StringUtils.isNotBlank(json.getString("START_DATE"))) {
//                info.setEndTime(json.getString("END_DATE"));
                info.setStartTime(json.getString("START_DATE"));
            }
        }
        model.addAttribute("broadband",info);

        Calendar cale = Calendar.getInstance();
        List<Date> queryDateList = new ArrayList<>();

        for(int i=5;i>=0;i--){
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0-i);
            queryDateList.add(cale.getTime());
        }
        model.addAttribute("queryDateList",queryDateList);

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String firstday, lastday;
        // 获取当前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        // 获取当前月的最后一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        model.addAttribute("firstday",firstday);
        model.addAttribute("lastday",lastday);
        model.addAttribute("billMaps",billquery(String.valueOf(serialNumber),buildPreMonth(0)));

        return "web/broadband/account/billQuery";
    }

    /**
     * 帐号查询
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("myAccount")
    public String myAccount(HttpServletRequest request, Model model )throws Exception{
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        LoggerFactory.getLogger("webDbLog").info(" myAccount serialNumber:" + serialNumber);

        if (serialNumber==null) {
            throw new Exception("手机号码不能为空！");
        }
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(String.valueOf(serialNumber));
        LoggerFactory.getLogger("webDbLog").info(" myAccount broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
        BroadbandDetailInfoResult result = null;
        try {
            result = broadBandService.broadbandDetailInfo(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统错误，请稍后再试");
        }
        LoggerFactory.getLogger("webDbLog").info(" myAccount broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());

        //无宽带进入宽带推荐页面
        if (result == null || result.getBroadbandDetailInfo() == null) {
            return "web/broadband/account/broadBandWithNoAccount";
        }
        BroadbandDetailInfo info = result.getBroadbandDetailInfo();
        if(StringUtils.isNotBlank(info.getProductsInfo())){
            com.alibaba.fastjson.JSONArray array=com.alibaba.fastjson.JSONArray.parseArray(info.getProductsInfo());
            com.alibaba.fastjson.JSONObject json = array.getJSONObject(0);
            if(StringUtils.isNotBlank(json.getString("START_DATE"))) {
//                info.setEndTime(json.getString("END_DATE"));
                info.setStartTime(json.getString("START_DATE"));
            }
        }
        List<String> bookInstallDateList = new ArrayList<>();
        bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来1天
        bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来2天
        bookInstallDateList.add(DateUtils.getDateAfterDays(6));//未来3天
        model.addAttribute("bookInstallDateList",bookInstallDateList);
        model.addAttribute("broadband",info);
        model.addAttribute("serialNumber",serialNumber);
        return "web/broadband/account/myAccount";
    }


    /**
     * 宽带停复机
     * @param request
     * @param bandAccount
     * @param status
     * @return
     */
    @RequestMapping(value = "updUserStatus", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updUserStatus(HttpServletRequest request,  String bandAccount, String status) throws Exception {
        String streamNo=createStreamNo();
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "updUserStatus",request.getParameterMap(),"宽带停复机",request);
        UpdUserStatusCondition condition = new UpdUserStatusCondition();
        condition.setAccount(bandAccount);
        //status 0:去停机 1：去复机
        condition.setStatus(status);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "提交失败！");
        if (StringUtils.isBlank(bandAccount)||StringUtils.isBlank(status)) {
            resultMap.put("respDesc", "参数不能为空");
            return resultMap;
        }
        BroadbandUpdStaRecord record=new BroadbandUpdStaRecord();
        record.setSerialNumber(String.valueOf(serialNumber));
        record.setBandAccount(bandAccount);
        record.setChannelId(BroadbandConstants.CHANNEL_ID_WAP);
        record.setRecordTime(new Date());
        record.setRecordType(status);
        try {
            Map map = broadBandService.updUserStatus(condition);
            LoggerFactory.getLogger("webDbLog").info("myBroadband updUserStatus resultMap" + map.toString());
            resultMap.put("respDesc", map.get("respDesc"));
            if("0".equals(map.get("respCode")) && map.get("result") != null){
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if("1".equals(resultObj.getString("retCode"))){
                    resultMap.put("respCode", "0");
                }
                resultMap.put("respDesc", resultObj.getString("retMsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"updUserStatus",null,"宽带停复机错误:"+processThrowableMessage(e));
            resultMap.put("respDesc", "系统错误，请稍后再试！");
        }finally {
            record.setResultInfo(resultMap.get("respDesc").toString());
            record.setResultCode(resultMap.get("respCode").toString());
            broadBandGoodsService.addBroadbandUpdStaRecord(record);
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                    "updUserStatus",null,objectToMap(record),"宽带停复机");
        }
        return resultMap;
    }


    //宽带移机
    @RequestMapping(value="move",method = RequestMethod.POST)
    public String move(HttpServletRequest request, Model model,String bandAccount,String bookSession,String bookDate,String addrId,String addrDesc )throws Exception{
        String streamNo=createStreamNo();
        Long phone = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "move",request.getParameterMap(),"宽带移机",request);
        resultMap.put("respCode", "-1");
        if(StringUtils.isBlank(bandAccount) || StringUtils.isBlank(addrDesc) || StringUtils.isBlank(bookDate) || StringUtils.isBlank(addrId)){
            throw new Exception("参数错误！");
        }
        addrDesc=URLDecoder.decode(addrDesc,"UTF-8");
        LoggerFactory.getLogger("webDbLog").info("myBroadband move addrDesc:" + addrDesc);
        BroadbandMoveCondition condition=new BroadbandMoveCondition();
        condition.setBookDate(bookDate);
        condition.setBookSession(bookSession);
        condition.setSerialNumber(bandAccount);
        condition.setAddrName(addrDesc);
        condition.setAddrDesc(addrDesc);
        condition.setAddrId(addrId);

        BroadbandUpdStaRecord record=new BroadbandUpdStaRecord();
        record.setSerialNumber(String.valueOf(phone));
        record.setBandAccount(bandAccount);
        record.setChannelId(BroadbandConstants.CHANNEL_ID_WAP);
        record.setRecordTime(new Date());
        record.setAddrDesc(addrDesc);
        record.setAddrId(addrId);
        record.setBookSession(bookSession);
        record.setBookDate(DateUtils.StringToDate(bookDate));
        record.setRecordType(BroadbandConstants.BROAD_BAND_TYPE_MOVE);
        try {
            Map map = broadBandService.broadbandMove(condition);
            resultMap.put("respDesc", map.get("respDesc"));
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                    record.setCrmOrderId(resultObj.getString("ORDER_ID"));
                    resultMap.put("respCode", "0");
                }
                resultMap.put("respDesc", resultObj.getString("X_RESULTINFO"));
            }
        }catch (Exception e){
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"move",null,"宽带移机错误:"+processThrowableMessage(e));
            resultMap.put("respDesc", "系统错误，请稍后再试！");
        }finally {
            record.setResultInfo(resultMap.get("respDesc").toString());
            record.setResultCode(resultMap.get("respCode").toString());
            broadBandGoodsService.addBroadbandUpdStaRecord(record);
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                    "move",null,objectToMap(record),"宽带移机");
        }
        model.addAttribute("respCode",resultMap.get("respCode"));
        return "web/broadband/account/result";
    }


    @RequestMapping("result")
    public String result(HttpServletRequest request, Model model,String respCode )throws Exception{
        model.addAttribute("respCode",respCode);
        return "web/broadband/account/result";
    }


    @RequestMapping(value = "feeDet", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> feeDet(HttpServletRequest request) {
        //话费及余额
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        //是否停机
        UserInfoGetCondition condition = new UserInfoGetCondition();
        condition.setSerialNumber(String.valueOf(serialNumber));
        condition.setxGetmode("0");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode","-1");
        Map resultFeeMap = null;
        try {
            resultFeeMap  = loginService.getUserInfo(condition);
            LoggerFactory.getLogger("webDbLog").info("myBroadband  feeDet resultMap" + resultFeeMap.toString());
            resultMap.put("respDesc", resultFeeMap.get("respDesc"));
            if(MapUtils.isNotEmpty(resultFeeMap) && "0".equals(resultFeeMap.get("respCode"))){
                JSONArray array = (JSONArray) resultFeeMap.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if(!"1".equals(resultObj.getString("USER_STATE_CODESET"))){
                    resultMap.put("respCode","0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 宽带拆机
     * @param request
     * @param serialNumber
     * @param bookSession
     * @return
     */
    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> destroy(HttpServletRequest request,  String serialNumber,String bookDate,String bookSession) throws Exception{
        String streamNo=createStreamNo();
        Long phone = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "destroy",request.getParameterMap(),"宽带拆机",request);
        BroadbandDestroyCondition condition =new BroadbandDestroyCondition();
        condition.setBookDate(bookDate);
        condition.setBookSession(bookSession);
        condition.setSerialNumber(serialNumber);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode", "-1");
        if (StringUtils.isBlank(serialNumber) || StringUtils.isBlank(bookDate) || StringUtils.isBlank(bookSession)) {
            throw new Exception("参数不能为空");
        }
        BroadbandUpdStaRecord record=new BroadbandUpdStaRecord();
        record.setSerialNumber(String.valueOf(phone));
        record.setBandAccount(serialNumber);
        record.setChannelId(BroadbandConstants.CHANNEL_ID_WAP);
        record.setRecordTime(new Date());
        record.setBookSession(bookSession);
        record.setBookDate(DateUtils.StringToDate(bookDate));
        record.setRecordType(BroadbandConstants.BRAOD_BAND_TYPE_DESTROY);
        try {
            Map map = broadBandService.broadbandDestroy(condition);
            resultMap.put("respDesc", map.get("respDesc"));
            LoggerFactory.getLogger("webDbLog").info("myBroadband  destroy resultMap" + map.toString());
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                    record.setCrmOrderId(resultObj.getString("ORDER_ID"));
                    resultMap.put("respCode", "0");
                }
                resultMap.put("respDesc", resultObj.getString("X_RESULTINFO"));
            } else {
                resultMap.put("respDesc", map.get("respDesc"));
            }
        }catch (Exception e){
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"destroy",null,"宽带拆机错误:"+processThrowableMessage(e));
            resultMap.put("respDesc", "系统错误，请稍后再试！");
        }finally {
            record.setResultInfo(resultMap.get("respDesc").toString());
            record.setResultCode(resultMap.get("respCode").toString());
            broadBandGoodsService.addBroadbandUpdStaRecord(record);
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                    "destroy",null,objectToMap(record),"宽带拆机");
        }
        return resultMap;
    }

    /**跳转移机页面
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toMove",method = RequestMethod.POST)
    public String toMove(HttpServletRequest request, Model model,BroadbandDetailInfo info )throws Exception{
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        if (serialNumber==null) {
            throw new Exception("手机号码不能为空！");
        }
        LoggerFactory.getLogger("webDbLog").info(" myAccount serialNumber:" + serialNumber);
        List<String> bookInstallDateList = new ArrayList<>();
        bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来4天
        bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来5天
        bookInstallDateList.add(DateUtils.getDateAfterDays(6));//未来6天
        model.addAttribute("bookInstallDateList",bookInstallDateList);

        //地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);

        //获取手机号码的归属地市编码
        String EPARCHY_CODE = this.getEparchyCode(String.valueOf(serialNumber));

        List<ThirdLevelAddress> countyList = null;
        if (!CollectionUtils.isEmpty(cityList)) {
            // 地市信息处理 用于适配Boss接口查询条件
            for (ThirdLevelAddress city : cityList) {
                if(city.getEparchyCode().equals(EPARCHY_CODE)){
                    countyList = memberAddressService.getChildrensByPId(city.getOrgId() + "");
                }
                city.setOrgName(city.getOrgName().replace("市", ""));
                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                    city.setOrgName("湘西州");
                }
            }
        }

        model.addAttribute("eparchy_Code", EPARCHY_CODE);
        model.addAttribute("cityList", cityList);
        model.addAttribute("countyList", countyList);

        model.addAttribute("serialNumber",serialNumber);
        model.addAttribute("broadband",info);
        return "web/broadband/account/broadbandMove";
    }

    @RequestMapping(value = "queryAddr", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryAddr(HttpServletRequest request,  String serialNumber,String addrId) throws Exception{
        List<String> bookInstallDateList = new ArrayList<>();

        QryAddressAttrCondition condition=new QryAddressAttrCondition();
        condition.setADDR_ID(addrId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode", "-1");
        if (StringUtils.isBlank(addrId) ) {
            throw new Exception("参数不能为空");
        }
        Map map = qryAddressService.queryAddressAttr(condition);
        LoggerFactory.getLogger("webDbLog").info("myBroadband  queryAddr resultMap" + map.toString());
        if ("0".equals(map.get("respCode")) && map.get("result") != null) {
            JSONArray array = (JSONArray) map.get("result");
            com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
            if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                resultMap.put("respCode", "0");
                if("01".equals(resultObj.getString("COMMUNITY_TYPE"))){//农村
                    bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来1天
                    bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来2天
                    bookInstallDateList.add(DateUtils.getDateAfterDays(6));//未来3天
                }else{
                    bookInstallDateList.add(DateUtils.getDateAfterDays(3));//未来1天
                    bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来2天
                    bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来3天
                }
                resultMap.put("bookInstallDateList",bookInstallDateList);
            }
            resultMap.put("respDesc", resultObj.getString("X_RESULTINFO"));
        } else {
            resultMap.put("respDesc", map.get("respDesc"));
        }
        return resultMap;
    }

    /**
     * 获取指定月份信息
     * @param month 当前月份的前面或者后面几个月   例：-1表示上一个月   1 表示下一个月
     * @return
     */
    private int buildPreMonth(int month){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        cl.add(Calendar.MONTH, month);
        Date date = cl.getTime();
        String m = sdf.format(date);
        return Integer.parseInt(m);
    }

    private Map<String,String> billquery(String serialNumber,int billDate) {
        MonthBillCondition billCondition = new MonthBillCondition();
        billCondition.setSerialNumber(serialNumber);
        billCondition.setEndMonth(billDate);
        billCondition.setStartMonth(billDate);
        billCondition.setWriteOffMode("1");//销帐方式  1--按帐户   2--按用户 。传1，查询的欠费信息是帐户的信息，但帐单是按用户查询的
        billCondition.setNeedAppendix("1");//该字段为0时，只返回账单信息，为1时，返回使用量和余额等信息。
        try {
            Map<String, Object> resultBillDataMap = monthBillService.queryFeeMonthBill(billCondition);
            LoggerFactory.getLogger("webDbLog").info("myBroadband  billQuery resultMap" + resultBillDataMap.toString());
            if (MapUtils.isNotEmpty(resultBillDataMap) && "0".equals(resultBillDataMap.get("respCode"))) {
                List<Map<String, String>> billDataList = (List<Map<String, String>>) resultBillDataMap.get("result");
                for (int i = billDataList.size() - 1; i >= 0; i--) {
                    Map<String, String> element = billDataList.get(i);
                    if ("0".equals(element.get("INTEGRATE_ITEM_CODE"))) {//宽带费用
                        return element;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 宽带账单查询
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("billQueryAjax")
    @ResponseBody
    public Map<String,Object> billQueryAjax(HttpServletRequest request, HttpServletResponse response, Model model,String billDate )throws Exception  {
        Map<String,Object> resultMap =new HashMap<>();
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        if (serialNumber == null || serialNumber == 0L) {
            logger.info("您的帐号没有绑定手机号码");
            throw new Exception("您的帐号没有绑定手机号码");
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMM");

        Calendar cale = Calendar.getInstance();
        Date orDate =format1.parse(billDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String firstday, lastday;
        // 获取当前月的第一天
        cale = Calendar.getInstance();
        cale.setTime(orDate);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        // 获取当前月的最后一天
        cale = Calendar.getInstance();
        cale.setTime(orDate);
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        resultMap.put("firstday",firstday);
        resultMap.put("lastday",lastday);

        Map<String,String> map =billquery(String.valueOf(serialNumber),Integer.parseInt(billDate));
        if(MapUtils.isNotEmpty(map)){
            if(StringUtils.isNotBlank(map.get("FEE2"))){
                resultMap.put("FEE",map.get("FEE2"));
            }else{
                resultMap.put("FEE",0);
            }
        }
        return resultMap;
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
}
