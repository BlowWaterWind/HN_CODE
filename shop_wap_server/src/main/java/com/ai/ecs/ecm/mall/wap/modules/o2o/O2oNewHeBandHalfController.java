package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.broadband.entity.BroadbandMarketRecord;
import com.ai.ecs.common.utils.DateUtils;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.InvokeEcop;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.cms.entity.BroadbandPoster;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.HeFamilyCheckCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.BkhtRecvfeeCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.queryPackage.entity.QueryAccountPackagesCondition;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.ai.ecs.order.constant.OrderConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2018/1/8.
 */
@Controller
@RequestMapping("o2oMarketing")
public class O2oNewHeBandHalfController extends BaseController {


    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    public FlowServeService flowServeService;

    @Autowired
    HeFamilyService heFamilyService;

    @Autowired
    private DictService dictService;

    @Autowired
    private IGoodsManageService goodsManageService;

    @Autowired
    private O2oOrderTempService orderTempService;

    @Autowired
    private O2oOrderParamTemService orderParamTemService;

    private static final List<String> disctCodes=new ArrayList<>();

    static {
        disctCodes.add( "99543245");
        disctCodes.add( "99543246");
        disctCodes.add( "99543247");
        disctCodes.add( "99543248");
        disctCodes.add( "99543249");
        disctCodes.add( "99543250");
        disctCodes.add( "99543251");
        disctCodes.add( "99543252");
    }

    @Autowired
    InvokeEcop invokeEcop;

    @Autowired
    private QueryAccountPackagesService queryAccountPackagesService;

    private Logger logger = Logger.getLogger(O2oNewHeBandHalfController.class);

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";

    /**
     * 号码填写
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("init")
    public String init(HttpServletRequest request , HttpServletResponse response, Model model,String posterName,String secToken)  throws Exception {
        List<BroadbandPoster> broadbandPosterList;
        broadbandPosterList = invokeEcop.getO2oBroadbandPosterInfo(posterName,"4");
        if(broadbandPosterList!=null &&broadbandPosterList.size()>0){
            model.addAttribute("broadbandPoster",broadbandPosterList.get(0));
        }
        MemberVo memberVo = UserUtils.getLoginUser(request);
        ChannelInfo channelInfo = memberVo.getChannelInfo();
        ShopInfo shopInfo=memberVo.getShopInfo();
        model.addAttribute("staffId",channelInfo.getTradeStaffId());
        model.addAttribute("shopId",shopInfo.getShopId());
        model.addAttribute("secToken",secToken);
        //获取办理用户手机号码
        return "web/broadband/o2o/marketing/init";
    }

    @RequestMapping("halfHandle")
    public String halfHandle(HttpServletRequest request,Model model,BroadbandPoster broadbandPoster) throws Exception {
        model.addAttribute("broadbandPoster",broadbandPoster);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,Object> resMap = new HashMap<String,Object>();
        String installPhoneNum = request.getParameter("installPhoneNum");
        if(StringUtils.isBlank(installPhoneNum)){
            throw new Exception("请输入手机号!");
        }else{
            model.addAttribute("installPhoneNum", installPhoneNum);
        }
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "您的信息提交失败!");
        //校验
        try {
            PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
            phoneAttributionModel1.setSerialNumber(installPhoneNum);
            resMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
            String EPARCHY_CODE= String.valueOf(((Map) ((List) resMap.get("result")).get(0)).get("AREA_CODE"));
            model.addAttribute("eparchy_Code", EPARCHY_CODE);
            if (!"731".equals(((Map) ((List) resMap.get("result")).get(0)).get("PROVINCE_CODE"))){
                resultMap.put(BroadbandConstants.RESPONSE_DESC, "请输入湖南移动号码办理!");
                model.addAttribute("resultMap",resultMap);
                return "web/broadband/o2o/marketing/init";
            }
            model.addAttribute("eparchy_Code",EPARCHY_CODE);
        }catch (Exception e) {
            logger.error("手机号码归属地 :"+e.getMessage());
            throw new Exception("手机号码归属地查询失败!");
        }
        // 有宽带账号
//        HeFamilyCheckCondition condition = new HeFamilyCheckCondition();
//        condition.setSerial_number(String.valueOf(installPhoneNum));
//        resMap = heFamilyService.heFamilyCheck(condition);
//        logger.info("heFamilyCheck："+resMap);
//        JSONArray resultArray = JSONArray.parseArray(String.valueOf(resMap.get("result")));
//        JSONObject result = (JSONObject) resultArray.get(0);
//        String isBroadBand = (String)result.get("BROADBAND_FLAG") ;
//        String isMbh = (String)result.get("INTERACTIVE_FLAG") ;
//        String resultCode = (String)result.get("X_RESULTCODE") ;
//        String resultInfo = (String)result.get("X_RESULTINFO") ;
//        resultMap.put("isBroadBand", isBroadBand);
//        resultMap.put("isMbh", isMbh);
//        resultMap.put("resultCode", resultCode);
//        resultMap.put(BroadbandConstants.RESPONSE_DESC, resultInfo);
//        if("0".equals(resultMap.get("isBroadBand"))){//用户已办理宽带，如果不是则回到首页提示不能办理
            try{
                //查询当前套餐
                QueryAccountPackagesCondition queryCondition = new QueryAccountPackagesCondition();
                queryCondition.setSerialNumber(installPhoneNum);
                Map res = queryAccountPackagesService.queryPackageInfo(queryCondition);
                logger.info("queryPackageInfo："+res);
                if ("0".equals(res.get("respCode")) && res.get("result") != null) {
                    JSONArray array = (JSONArray) res.get("result");
                    JSONObject resultObj = array.getJSONObject(0);
                    Map dictresMap = (Map) resultObj.get("DISCNT_INFO");
                    String discntCoode = (String) dictresMap.get("MAIN_DISCNT_CODE");
                    if(disctCodes.contains(discntCoode)){//如果是和家庭新装，则进入已选页面
                        Long curentPackage = 0L;
                        String packageName = "";
                        String resfee = (String) dictresMap.get("MAIN_DISCNT_FEE");
                        packageName = (String) dictresMap.get("MAIN_DISCNT_NAME");
                        if(StringUtils.isNotEmpty(resfee)){
                            curentPackage = Long.parseLong(resfee)/100;
                        }
                        model.addAttribute("packageName",packageName);
                        model.addAttribute("curentPackage",curentPackage);
                        Map<String,Object> bbArgs = new HashMap<>();
                        bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_NEW_FAMILY_HALF"));
                        List<String> eparchyCodes = new ArrayList<>();
                        eparchyCodes.add(BroadbandConstants.EPARCHY_CODE);
                        bbArgs.put("eparchyCodes",eparchyCodes);
                        bbArgs.put("orderColumn","SV.BARE_PRICE");
                        bbArgs.put("orderType","ASC");
                        bbArgs.put("goodsStatusId",4);
                        bbArgs.put("chnlCode","E007");
                        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
                        String s = JSONArray.toJSONString(goodsInfoList);
                        logger.info(s);
                        List<BroadbandItemEntity> heBroadbandItemList = null;
                        try {
                            heBroadbandItemList = BroadbandUtils.convertNewHeHalfItemEntityList(goodsInfoList);

                        }  catch (Exception e) {
                            logger.error("和家庭办理异常:"+e.getMessage());
                            e.printStackTrace();
                            throw e;
                        }
                        resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
                        resultMap.put(BroadbandConstants.RESPONSE_DESC, "ok");
                        model.addAttribute("resultMap",resultMap);
                        model.addAttribute("heBroadbandItemList",heBroadbandItemList);
                        model.addAttribute("type", OrderConstant.TYPE_BROADBAND_HALF_HE);
                        return "web/broadband/o2o/marketing/o2oNewHeHalfChoose";
                    }else{
                        resultMap.put(BroadbandConstants.RESPONSE_DESC, "您目前已有的套餐不符合当前套餐的办理条件!");
                    }
                } else {
                    resultMap.put(BroadbandConstants.RESPONSE_DESC, "套餐查询失败!");
                }
            }catch (Exception e) {
                e.printStackTrace();
                logger.error("已有宽带用户信息查询："+e.getMessage());
                throw new Exception("套餐查询失败!");
            }
//        }else{
//            resultMap.put(BroadbandConstants.RESPONSE_DESC, "您目前已有的套餐不符合当前套餐的办理条件!");
//        }
        model.addAttribute("resultMap",resultMap);
        return "web/broadband/o2o/marketing/init";
    }

    @RequestMapping("yearHandle")
    public String yearHandle(HttpServletRequest request,Model model,BroadbandPoster broadbandPoster) throws Exception {
        model.addAttribute("broadbandPoster",broadbandPoster);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,Object> resMap = new HashMap<String,Object>();
        String installPhoneNum = request.getParameter("installPhoneNum");
        if(StringUtils.isBlank(installPhoneNum)){
            throw new Exception("请输入手机号!");
        }else{
            model.addAttribute("installPhoneNum", installPhoneNum);
        }
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "您的信息提交失败!");
        //校验
        try {
            PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
            phoneAttributionModel1.setSerialNumber(installPhoneNum);
            resMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
            String EPARCHY_CODE= String.valueOf(((Map) ((List) resMap.get("result")).get(0)).get("AREA_CODE"));
            model.addAttribute("eparchy_Code", EPARCHY_CODE);
            if (!"731".equals(((Map) ((List) resMap.get("result")).get(0)).get("PROVINCE_CODE"))){
                resultMap.put(BroadbandConstants.RESPONSE_DESC, "请输入湖南移动号码办理!");
                model.addAttribute("resultMap",resultMap);
                return "web/broadband/o2o/marketing/init";
            }
            model.addAttribute("eparchy_Code",EPARCHY_CODE);
        }catch (Exception e) {
            logger.error("手机号码归属地 :"+e.getMessage());
            throw new Exception("手机号码归属地查询失败!");
        }
        // 有宽带账号
        HeFamilyCheckCondition condition = new HeFamilyCheckCondition();
        condition.setSerial_number(String.valueOf(installPhoneNum));
        resMap = heFamilyService.heFamilyCheck(condition);
        logger.info("heFamilyCheck："+resMap);
        JSONArray resultArray = JSONArray.parseArray(String.valueOf(resMap.get("result")));
        JSONObject result = (JSONObject) resultArray.get(0);
        String isBroadBand = (String)result.get("BROADBAND_FLAG") ;
        String isMbh = (String)result.get("INTERACTIVE_FLAG") ;
        String resultCode = (String)result.get("X_RESULTCODE") ;
        String resultInfo = (String)result.get("X_RESULTINFO") ;
        resultMap.put("isBroadBand", isBroadBand);
        resultMap.put("isMbh", isMbh);
        resultMap.put("resultCode", resultCode);
        resultMap.put(BroadbandConstants.RESPONSE_DESC, resultInfo);
        if("0".equals(resultMap.get("isMbh"))){//有魔百盒
            try{
                //查询当前套餐
                QueryAccountPackagesCondition queryCondition = new QueryAccountPackagesCondition();
                queryCondition.setSerialNumber(installPhoneNum);
                Map res = queryAccountPackagesService.queryPackageInfo(queryCondition);
                logger.info("queryPackageInfo："+res);
                if ("0".equals(res.get("respCode")) && res.get("result") != null) {
                    JSONArray array = (JSONArray) res.get("result");
                    JSONObject resultObj = array.getJSONObject(0);
                    Map dictresMap = (Map) resultObj.get("DISCNT_INFO");
                    Long curentPackage = 0L;
                    String packageName = "";
                    String resfee = (String) dictresMap.get("MAIN_DISCNT_FEE");
                    packageName = (String) dictresMap.get("MAIN_DISCNT_NAME");
                    if (StringUtils.isNotEmpty(resfee)) {
                        curentPackage = Long.parseLong(resfee) / 100;
                    }
                    model.addAttribute("packageName", packageName);
                    model.addAttribute("curentPackage", curentPackage);
                    Map<String, Object> bbArgs = new HashMap<>();
                    bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID", "O2O_BROADBAND_CATEGORY_ID_YEAR"));
                    List<String> eparchyCodes = new ArrayList<>();
                    eparchyCodes.add(BroadbandConstants.EPARCHY_CODE);
                    bbArgs.put("eparchyCodes", eparchyCodes);
                    bbArgs.put("orderColumn", "SV.BARE_PRICE");
                    bbArgs.put("orderType", "ASC");
                    bbArgs.put("goodsStatusId", 4);
                    bbArgs.put("chnlCode", "E007");
                    List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
                    String s = JSONArray.toJSONString(goodsInfoList);
                    logger.info(s);
                    List<BroadbandItemEntity> heBroadbandItemList = null;
                    try {
                        heBroadbandItemList = BroadbandUtils.convertNewHeHalfItemEntityList(goodsInfoList);
                    } catch (Exception e) {
                        logger.error("和家庭办理异常:" + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                    resultMap.put("respCode", "0");
                    resultMap.put("respDesc", "ok");
                    model.addAttribute("resultMap", resultMap);
                    model.addAttribute("heBroadbandItemList", heBroadbandItemList);
                    model.addAttribute("type", OrderConstant.TYPE_BROADBAND_YEAR_TV);
                    return "web/broadband/o2o/marketing/o2oTVYearChoose";
                } else {
                    resultMap.put(BroadbandConstants.RESPONSE_DESC, "套餐查询失败!");
                }
            }catch (Exception e) {
                e.printStackTrace();
                logger.error("已有宽带用户信息查询："+e.getMessage());
                throw new Exception("系统错误!");
            }
        }else{
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "您未办理宽带电视!");
        }
        model.addAttribute("resultMap", resultMap);
        return "web/broadband/o2o/marketing/init";
    }

    @RequestMapping(value="submitTempOrder")
    @ResponseBody
    public Map<String, Object> submitTempOrder(HttpServletRequest request, BroadbandMarketRecord record, String staffPwd,
                                               HttpServletResponse response) {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
        ChannelInfo channelInfo = memberVo.getChannelInfo();
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
        LoggerFactory.getLogger("webDbLog").info("o2oMarketing submitTempOrder record " + net.sf.json.JSONObject.fromObject(record).toString());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "提交失败！");
        try {
            check(resultMap, record, channelInfo, staffPwd);
            if (!"0".equals(resultMap.get("respCode"))) {
                return resultMap;
            }
            //保存到临时表
            O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
            orderTempInfo.setChannel_code(BroadbandConstants.CHANNEL_ID_O2O_CHANNEL);
            orderTempInfo.setContact_phone(record.getSerialNumber());
            orderTempInfo.setEparchy_code(record.getCityCode());
            orderTempInfo.setGoods_id(record.getGoodsId());
            orderTempInfo.setGoods_sku_id(record.getGoodsSkuId());
            orderTempInfo.setGoods_name(record.getGoodsName());
            orderTempInfo.setMember_id(memberLogin.getMemberId());
            orderTempInfo.setMember_loging_name(memberLogin.getMemberLogingName());
            orderTempInfo.setOrder_type_id(Long.valueOf(record.getRecordType()));
            orderTempInfo.setGoods_pay_price(record.getGoodsPrice());
            orderTempInfo.setGoods_sku_price(record.getGoodsPrice());
            orderTempInfo.setPay_mode_id(2);
            orderTempInfo.setInsert_time(new Date());
            orderTempInfo.setRoot_cate_id(OrderConstant.CATE_BROADBAND);
            orderTempInfo.setShop_type_id(6L);
            orderTempInfo.setOrder_status(0L);
            ShopInfo shopInfo = memberVo.getShopInfo();
            if (shopInfo != null) {
                orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));
                orderTempInfo.setShop_name(shopInfo.getShopName());
            }
            Long orderTempId = orderTempService.insert(orderTempInfo);
            O2oParamUtils o2oParamUtils = new O2oParamUtils();
            o2oParamUtils.addParams("TRADE_DEPART_PASSWD", staffPwd, "渠道交易部门密码");
            o2oParamUtils.addParams("PRODUCT_ID", record.getProductId(), "产品编码");
            o2oParamUtils.addParams("PACKAGE_ID", record.getPackageId(), "包编码");
            o2oParamUtils.addParams("SERIAL_NUMBER", record.getSerialNumber(), "手机号码");
            if(record.getProductLevel()!=null){
                o2oParamUtils.addParams("PRODUCT_LEVEL", String.valueOf(record.getProductLevel()), "产品档次");
            }
            o2oParamUtils.addParams("MARKETING_TYPE", record.getRecordType(), "营销活动类型");
            List<O2oOrderParamTemp> o2oOrderParamTempList =putChannelParams(o2oParamUtils.getParamsList(), channelInfo, orderTempInfo.getEparchy_code());
            for(O2oOrderParamTemp paramTemp:o2oOrderParamTempList){
                paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
            }
            orderParamTemService.batchInsert(o2oOrderParamTempList);
            resultMap.put("orderId", orderTempId);
            resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "ok");
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "系统繁忙，稍后再试！");
        }
        LoggerFactory.getLogger("webDbLog").info("o2oNewHeBandHalf submitTempOrder resultMap" + resultMap.toString());
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

    /**
     * 校验
     *
     * @param resultMap  返回结果
     * @param record
     * @return
     */
    private Map<String, Object> check(Map<String, Object> resultMap, BroadbandMarketRecord record, ChannelInfo channelInfo, String password) {
        BkhtRecvfeeCondition bkhtRecvfeeCondition = new BkhtRecvfeeCondition();
        bkhtRecvfeeCondition.setACCT_ID("");
        bkhtRecvfeeCondition.setCARRIER_CODE(0);
        bkhtRecvfeeCondition.setCARRIER_ID("");
        bkhtRecvfeeCondition.setCHANNEL_ACCEPT_TIME(DateUtils.getCurrentTime());
        bkhtRecvfeeCondition.setCHANNEL_TRADE_ID(DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS") + genRandom(3));
        bkhtRecvfeeCondition.setCHARGE_ID("");
        bkhtRecvfeeCondition.setCHARGE_SOURCE_CODE(37);
        bkhtRecvfeeCondition.setDEPOSIT_CODE("");
        bkhtRecvfeeCondition.setPACKAGE_ID(record.getPackageId());
        bkhtRecvfeeCondition.setPRODUCT_ID(record.getProductId());
        bkhtRecvfeeCondition.setPARA_CODE1("HF");
        bkhtRecvfeeCondition.setPARA_CODE2("");
        bkhtRecvfeeCondition.setPARA_CODE3("");
        bkhtRecvfeeCondition.setPARA_CODE4("");
        bkhtRecvfeeCondition.setPARA_CODE5("");
        bkhtRecvfeeCondition.setCHANNEL_ID(channelInfo.getTradeStaffId());
        bkhtRecvfeeCondition.setPAY_FEE_MODE_CODE(0);
        bkhtRecvfeeCondition.setRECOVER_TAG("0");
        bkhtRecvfeeCondition.setSERIAL_NUMBER(record.getSerialNumber());
        bkhtRecvfeeCondition.setREMOVE_TAG("0");
        bkhtRecvfeeCondition.setTRADE_TYPE_CODE("240");
        bkhtRecvfeeCondition.setWRITEOFF_MODE("0");
        bkhtRecvfeeCondition.setX_FPAY_FEE("0");
        bkhtRecvfeeCondition.setEPARCHY_CODE(record.getCityCode());
        bkhtRecvfeeCondition.setPRE_TYPE("1");
        Map<String, Object> map = null;
        try {
            bkhtRecvfeeCondition.setCityCode(record.getCityCode());
            bkhtRecvfeeCondition.setEparchyCodeFCust(record.getCityCode());
            bkhtRecvfeeCondition.setTradeDepartPassword(password);
            bkhtRecvfeeCondition.setDepartId(channelInfo.getTradeDepartId());
            bkhtRecvfeeCondition.setStaffId(channelInfo.getTradeStaffId());
            bkhtRecvfeeCondition.setInModeCode(channelInfo.getInModeCode());
            bkhtRecvfeeCondition.setChanId(channelInfo.getChanelId());
            LoggerFactory.getLogger("webDbLog").info("=======o2oMarketing check param：" + net.sf.json.JSONObject.fromObject(bkhtRecvfeeCondition).toString());
            map = flowServeService.MGTJ4Handle(bkhtRecvfeeCondition);
            LoggerFactory.getLogger("webDbLog").info("====o2oMarketing check result：" + map);
            resultMap.put(BroadbandConstants.RESPONSE_DESC, map.get("respDesc"));
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "接口调用异常");
            e.printStackTrace();
        }
        try {
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                    resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
                }
            }
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "接口调用异常");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 返回0到9的字符,n位
     *
     * @return
     */
    private static String genRandom(int n) {
        Random random = new Random();
        String randomStr = "";
        for (int i = 0; i < n; i++) {
            int s1 = random.nextInt(10);
            randomStr += s1;
        }
        return randomStr;
    }
}
