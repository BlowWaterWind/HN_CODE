package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.broadband.api.IBroadBandService;
import com.ai.ecs.broadband.entity.BroadbandSpeedUpRecord;
import com.ai.ecs.broadband.entity.BroadbandUpdStaRecord;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.login.service.LoginService;
import com.ai.ecs.ecsite.modules.login.service.entity.UserInfoGetCondition;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
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
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * Created by Administrator on 2017/9/19.
 */
@Controller
@RequestMapping("o2oMyBroadband")
public class O2oMyBroadbandController extends BaseController {

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private IBroadBandService broadBandGoodsService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private IMemberAddressService memberAddressService;

    @Autowired
    QryAddressService qryAddressService;

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";

    @RequestMapping("queryAccount")
    public String queryAccount(HttpServletRequest request, HttpServletResponse response, Model model)throws Exception  {
        return "web/broadband/o2o/myAccount/queryAccount";
    }

    /**
     * 宽带账单查询
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("billQuery")
    public String toBroadbandSpeedUp(HttpServletRequest request, HttpServletResponse response, Model model)throws Exception  {
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        LoggerFactory.getLogger("webDbLog").info("speedup serialNumber"+serialNumber);
        if (serialNumber == null || serialNumber == 0L) {
            LoggerFactory.getLogger("webDbLog").info("您的帐号没有绑定手机号码");
            throw new Exception("您的帐号没有绑定手机号码");
        }
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(String.valueOf(serialNumber));
        LoggerFactory.getLogger("webDbLog").info("broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
        BroadbandDetailInfoResult result = null;
        try {
            result = broadBandService.broadbandDetailInfo(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统错误，请稍后再试");
        }
        LoggerFactory.getLogger("webDbLog").info("broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());

        //有宽带进入宽带帐号选择页面
        if (result == null || result.getBroadbandDetailInfo() == null) {
            return "web/goods/broadband/common/broadBandWithNoAccount";
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
            JSONArray array= JSONArray.parseArray(info.getProductsInfo());
            com.alibaba.fastjson.JSONObject json = array.getJSONObject(0);
            if(StringUtils.isNotBlank(json.getString("START_DATE"))) {
                record.setStartTime(DateUtils.StringToDateTime(json.getString("START_DATE")));
                String surplusDays = DateUtils.differDays(info.getEndTime().replaceAll("-", "/"));
                record.setSurplusDays(Integer.parseInt(surplusDays));
            }
        }
        list.add(record);
        model.addAttribute("speedUpList", list);
        return "web/goods/broadband/speedUp/broadBandAccountList";
    }

    /**
     * 帐号查询
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("myAccount")
    public String myAccount(String serialNumber,HttpServletRequest request, Model model )throws Exception{
        if (serialNumber==null) {
            throw new Exception("手机号码不能为空！");
        }

        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(serialNumber);
        LoggerFactory.getLogger("webDbLog").info("broadbandAccount myAccount broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
        BroadbandDetailInfoResult result = null;
        try {
            result = broadBandService.broadbandDetailInfo(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("系统错误，请稍后再试");
        }
        LoggerFactory.getLogger("webDbLog").info("broadbandAccount myAccount broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());

        //无宽带进入宽带推荐页面
        if (result == null || result.getBroadbandDetailInfo() == null) {
            return "web/broadband/o2o/common/broadBandWithNoAccount";
        }
        BroadbandDetailInfo info = result.getBroadbandDetailInfo();
        if(StringUtils.isNotBlank(info.getProductsInfo())){
            JSONArray array= JSONArray.parseArray(info.getProductsInfo());
            com.alibaba.fastjson.JSONObject json = array.getJSONObject(0);
            if(StringUtils.isNotBlank(json.getString("START_DATE"))) {
//                info.setEndTime(json.getString("END_DATE"));
                info.setStartTime(json.getString("START_DATE"));
            }
        }
        model.addAttribute("broadband",info);
        model.addAttribute("serialNumber",serialNumber);
        return "web/broadband/o2o/myAccount/myAccount";
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
    public Map<String,Object> updUserStatus(HttpServletRequest request,  String bandAccount, String status,String serialNumber,String password) throws Exception {
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "updUserStatus",request.getParameterMap(),"o2o宽带停复机",request);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode", "-1");
        BroadbandUpdStaRecord record=new BroadbandUpdStaRecord();
        if (StringUtils.isBlank(bandAccount)||StringUtils.isBlank(status)) {
            resultMap.put("respDesc", "参数不能为空");
            return resultMap;
        }
        try {
            password = TriDes.getInstance().strDec(password, keyStr, null, null);
            MemberVo memberVo=UserUtils.getLoginUser(request);
            ChannelInfo channelInfo=memberVo.getChannelInfo();

            UpdUserStatusCondition condition = new UpdUserStatusCondition();
            condition.setAccount(bandAccount);
            //status 0:去停机 1：去复机
            condition.setStatus(status);
            String cityCode=this.getEparchyCode(serialNumber);
            condition.setCityCode(cityCode);
            condition.setEparchyCodeFCust(cityCode);
            condition.setTradeDepartPassword(password);
            condition.setDepartId(channelInfo.getTradeDepartId());
            condition.setStaffId(channelInfo.getTradeStaffId());
            condition.setInModeCode(channelInfo.getInModeCode());
            condition.setChanId(channelInfo.getChanelId());
            condition.setMystaffId(channelInfo.getTradeStaffId());
            LoggerFactory.getLogger("webDbLog").info("=======broadbandAccount updUserStatus param：" + JSONObject.fromObject(condition).toString());
            record.setSerialNumber(String.valueOf(serialNumber));
            record.setBandAccount(bandAccount);
            record.setChannelId(BroadbandConstants.CHANNEL_ID_O2O_CHANNEL);
            record.setRecordTime(new Date());
            record.setRecordType(status);
            Map map = broadBandService.updUserStatus(condition);
            LoggerFactory.getLogger("webDbLog").info("broadbandAccount updUserStatus resultMap" + map.toString());
            resultMap.put("respDesc", map.get("respDesc"));
            if("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("1".equals(resultObj.getString("retCode"))) {
                    resultMap.put("respCode", "0");
                }
                resultMap.put("respDesc", resultObj.getString("retMsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"updUserStatus",null,"o2o宽带停复机错误:"+processThrowableMessage(e));
            resultMap.put("respDesc", "系统错误，请稍后再试！");
        }finally {
            record.setResultInfo(resultMap.get("respDesc").toString());
            record.setResultCode(resultMap.get("respCode").toString());
            broadBandGoodsService.addBroadbandUpdStaRecord(record);
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                    "updUserStatus",null,objectToMap(record),"o2o宽带停复机");
        }
        return resultMap;
    }

    @RequestMapping("result")
    public String result(HttpServletRequest request, Model model,String respCode )throws Exception{
        model.addAttribute("respCode",respCode);
        return "web/broadband/o2o/common/result";
    }


    @RequestMapping(value = "feeDet", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> feeDet(String serialNumber,HttpServletRequest request) {
        //是否停机
        UserInfoGetCondition condition = new UserInfoGetCondition();
        condition.setSerialNumber(serialNumber);
        condition.setxGetmode("0");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode","-1");
        Map resultFeeMap = null;
        try {
            resultFeeMap  = loginService.getUserInfo(condition);
            resultMap.put("respDesc", resultFeeMap.get("respDesc"));
            LoggerFactory.getLogger("webDbLog").info("myBroadband  feeDet resultMap" + resultFeeMap.toString());
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

    /**跳转移机页面
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "toMove",method = RequestMethod.POST)
    public String toMove(HttpServletRequest request, Model model,BroadbandDetailInfo info )throws Exception{
        if (info==null || StringUtils.isEmpty(info.getSerialNumber())) {
            throw new Exception("手机号码不能为空！");
        }
        LoggerFactory.getLogger("webDbLog").info(" myAccount serialNumber:" + info.getSerialNumber());
        List<String> bookInstallDateList = new ArrayList<>();
        bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来4天
        bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来5天
        bookInstallDateList.add(DateUtils.getDateAfterDays(6));//未来6天
        model.addAttribute("bookInstallDateList",bookInstallDateList);

        //地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);

        //获取手机号码的归属地市编码
        String EPARCHY_CODE = this.getEparchyCode(info.getSerialNumber());

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
        model.addAttribute("broadband",info);
        return "web/broadband/o2o/myAccount/broadbandMove";
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

    //宽带移机
    @RequestMapping(value="move",method = RequestMethod.POST)
    public String move(HttpServletRequest request, Model model,String bandAccount,String bookSession,String bookDate,String addrId,String addrDesc,String serialNumber,String password)throws Exception{
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "move",request.getParameterMap(),"o2o宽带移机",request);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("respCode", "-1");
        if(StringUtils.isBlank(bandAccount) || StringUtils.isBlank(addrDesc) || StringUtils.isBlank(bookDate) || StringUtils.isBlank(addrId)){
            throw new Exception("参数错误！");
        }
        addrDesc= URLDecoder.decode(addrDesc,"UTF-8");
        LoggerFactory.getLogger("webDbLog").info("myBroadband move addrDesc:" + addrDesc);
        password = TriDes.getInstance().strDec(password, keyStr, null, null);
        MemberVo memberVo=UserUtils.getLoginUser(request);
        ChannelInfo channelInfo=memberVo.getChannelInfo();
        BroadbandMoveCondition condition=new BroadbandMoveCondition();
        condition.setBookDate(bookDate);
        condition.setBookSession(bookSession);
        condition.setSerialNumber(bandAccount);
        condition.setAddrName(addrDesc);
        condition.setAddrDesc(addrDesc);
        condition.setAddrId(addrId);
        String cityCode=this.getEparchyCode(serialNumber);
        condition.setCityCode(cityCode);
        condition.setEparchyCodeFCust(cityCode);
        condition.setTradeDepartPassword(password);
        condition.setDepartId(channelInfo.getTradeDepartId());
        condition.setStaffId(channelInfo.getTradeStaffId());
        condition.setInModeCode(channelInfo.getInModeCode());
        condition.setChanId(channelInfo.getChanelId());
        BroadbandUpdStaRecord record=new BroadbandUpdStaRecord();
        record.setSerialNumber(serialNumber);
        record.setBandAccount(bandAccount);
        record.setChannelId(BroadbandConstants.CHANNEL_ID_O2O_CHANNEL);
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
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"move",null,"o2o宽带移机错误:"+processThrowableMessage(e));
            resultMap.put("respDesc", "系统错误，请稍后再试！");
        }finally {
            record.setResultInfo(resultMap.get("respDesc").toString());
            record.setResultCode(resultMap.get("respCode").toString());
            broadBandGoodsService.addBroadbandUpdStaRecord(record);
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                    "move",null,objectToMap(record),"o2o宽带移机");
        }
        model.addAttribute("respCode",resultMap.get("respCode"));
        return "web/broadband/o2o/common/result";
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
}
