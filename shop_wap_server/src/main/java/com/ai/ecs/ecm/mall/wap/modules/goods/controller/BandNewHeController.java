package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.framework.exception.ServiceException;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.ParamUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.entity.Dict;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqQueryFeeCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.queryPackage.entity.QueryAccountPackagesCondition;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderSubDetailBusiParam;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.ai.ecs.utils.LogUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * 新和家庭wap办理
 * Created by think on 2017/12/12.
 */
@Controller
@RequestMapping("newHeBand")
public class BandNewHeController extends BaseController {
    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private IMemberAddressService memberAddressService;

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

    @Autowired
    private QryAddressService qryAddressService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    IOrderQueryService orderQueryService;

    @Autowired
    private QueryAccountPackagesService queryAccountPackagesService;

    private Logger logger = Logger.getLogger(BandNewHeController.class);


    @RequestMapping("heInstall")
    public String bandInstall(HttpServletRequest request, Model model) throws Exception {
        String streamNo = createStreamNo();
        String installPhoneNum = "";
        String busType = request.getParameter("busType");

        //是否是已办理宽带，已存在地址信息
        String hasAddress = request.getParameter("hasAddress");
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            installPhoneNum = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
            writerFlowLogEnterMenthod(streamNo, "", memberVo.getMemberLogin().getMemberLogingName(), getClass().getName(),
                    "bandInstall", request.getParameterMap(), "新和家庭宽带办理", request);
        } catch (Exception e) {
            e.printStackTrace();
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(), "bandInstall", null, "新和家庭宽带办理:" + processThrowableMessage(e));
            logger.error(e, e);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isBlank(installPhoneNum)) {
            throw new Exception("请输入手机号!");
        } else {
            model.addAttribute("installPhoneNum", installPhoneNum);
        }
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        model.addAttribute("eparchy_Code", "0731");
        resultMap.put("isBroadBand", 1);
        model.addAttribute("resultMap", resultMap);
        //新和家庭套餐编码对应宽带档次
        Map<String,String> bandCodeMap = getBandCodeMap("HE_BAND_MAIN_CODE");
        //省内和家庭补办宽带
        Map<String,String> countyCodeMap = getBandCodeMap("COUNTY_HE_BAND_MAIN_CODE");
        //国内和家庭补办宽带
        Map<String,String> stateCodeMap = getBandCodeMap("STATE_HE_BAND_MAIN_CODE");
        //校验
        try {
            PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
            phoneAttributionModel1.setSerialNumber(installPhoneNum);
            resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
            if ("0".equals(resultMap.get("respCode")) && resultMap.get("result") != null) {
                JSONArray array = (JSONArray) resultMap.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                    String EPARCHY_CODE = String.valueOf(resultObj.get("AREA_CODE"));
                    model.addAttribute("eparchy_Code", EPARCHY_CODE);
                    if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))) {
                        resultMap.put("respCode", "-1");
                        resultMap.put("respDesc", "请输入湖南移动号码办理!");
                        model.addAttribute("resultMap", resultMap);
                        return "web/goods/broadband/newInstall/checkError";
                    }
                    model.addAttribute("eparchy_Code", EPARCHY_CODE);
                }else{
                    resultMap.put("respCode", "-1");
                    resultMap.put("respDesc",resultObj.get("X_RESULTINFO"));
                    model.addAttribute("resultMap", resultMap);
                    return "web/goods/broadband/newInstall/checkError";
                }
            } else {
                throw new Exception("归属地接口查询异常！");
            }
//            model.addAttribute("eparchy_Code","0731");
        } catch (Exception e) {
            logger.error("手机号码归属地 :" + e.getMessage());
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(), "bandInstall", null, "手机号码归属地:" + processThrowableMessage(e));
            model.addAttribute("resultMap", resultMap);
        }

        //查询套餐信息
        String packageName = "";//主套餐名称
        Boolean hasPackage = false;//是否有是和家庭用户
        Long curentPackage = 0L;//当前套餐档次
        String packageCode = "";//当前主套餐编号
        String reissueType = "";//宽带补办类型
        QueryAccountPackagesCondition queryCondition = new QueryAccountPackagesCondition();
        queryCondition.setSerialNumber(installPhoneNum);
        Map res = queryAccountPackagesService.queryPackageInfo(queryCondition);
        if ("0".equals(res.get("respCode")) && res.get("result") != null) {
            JSONArray array = (JSONArray) res.get("result");
            JSONObject resultObj = array.getJSONObject(0);
            if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                Map resMap = (Map) resultObj.get("DISCNT_INFO");
                String resfee = (String) resMap.get("MAIN_DISCNT_FEE");
                String discntCode = (String) resMap.get("MAIN_DISCNT_CODE");
                packageName = (String) resMap.get("MAIN_DISCNT_NAME");
                if (StringUtils.isNotEmpty(resfee)) {
                    curentPackage = Long.parseLong(resfee) / 100;
                }
                //是否是省内补办宽带
                if ("reissue".equals(busType)) {
                    if (countyCodeMap.get(discntCode) != null) {
                        packageCode = (String) countyCodeMap.get(discntCode);
                        model.addAttribute("package_code", countyCodeMap.get(discntCode));
                        model.addAttribute("busType", busType);
                        model.addAttribute("reissueType", "province");
                        reissueType = "province";
                        hasPackage = true;
                    }
                    if (stateCodeMap.get(discntCode) != null) {
                        packageCode = (String) stateCodeMap.get(discntCode);
                        model.addAttribute("package_code", stateCodeMap.get(discntCode));
                        model.addAttribute("busType", busType);
                        model.addAttribute("reissueType", "state");
                        reissueType = "state";
                        hasPackage = true;
                    }
                    if (StringUtils.isEmpty(packageCode)) {
                        throw new Exception("不是和家庭用户，不能办理此业务!");
                    }
                }
                /**
                 * 判断用户是否办理新和家庭套餐
                 */
                else if (bandCodeMap.get(discntCode) != null) {
                    model.addAttribute("package_code", bandCodeMap.get(discntCode));
                    hasPackage = true;
                }
            }else{
                throw new Exception("查询用户套餐失败!");
            }
        } else {
            throw new Exception("查询当前套餐失败!");
        }


        // 有宽带账号
        HeFamilyCheckCondition condition = new HeFamilyCheckCondition();
        condition.setSerial_number(String.valueOf(installPhoneNum));
        resultMap = heFamilyService.heFamilyCheck(condition);
        JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
        JSONObject result = (JSONObject) resultArray.get(0);
        String isBroadBand = (String) result.get("BROADBAND_FLAG");
        String isMbh = (String) result.get("INTERACTIVE_FLAG");
        String resultCode = (String) result.get("X_RESULTCODE");
        String resultInfo = (String) result.get("X_RESULTINFO");
        resultMap.put("isBroadBand", isBroadBand);
        resultMap.put("isMbh", isMbh);
        resultMap.put("resultCode", resultCode);
        resultMap.put("respDesc", resultInfo);
        model.addAttribute("resultMap", resultMap);

        //有宽带未进入地址页面
        if("0".equals(resultMap.get("isBroadBand"))&&!"Y".equals(hasAddress)){
            try{
//                查询办理前宽带信息
                BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
                broadbandDetailInfoCondition.setSerialNumber(String.valueOf(installPhoneNum));
                BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);
                model.addAttribute("broadbandDetailInfoResult",broadbandDetailInfoResult);
                if(broadbandDetailInfoResult!=null&& broadbandDetailInfoResult.getBroadbandDetailInfo()!=null&&"reissue".equals(busType)){
                    resultMap.put("respCode", "-1");
                    resultMap.put("respDesc", "用户已办理宽带!");
                    model.addAttribute("resultMap",resultMap);
                    return "web/goods/broadband/newInstall/checkError";
                }else{
                    return "web/goods/broadband/newHeFamily/newHeHasBroadband";
                }
            }catch (Exception e) {
                e.printStackTrace();
                logger.error("已有宽带用户信息查询："+e.getMessage());
            }
        }

        // 地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
        // 默认区县信息(长沙市)
//        List<ThirdLevelAddress> countyList = null;
        if (!CollectionUtils.isEmpty(cityList)) {
//            countyList = memberAddressService.getChildrensByPId(cityList.get(0).getOrgId() + "");
//            logger.info("地址编码:" + countyList);
            // 地市信息处理 用于适配Boss接口查询条件
            for (ThirdLevelAddress city : cityList) {
                city.setOrgName(city.getOrgName().replace("市", ""));
                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                    city.setOrgName("湘西州");
                }
            }
        }
        model.addAttribute("cityList", cityList);
//        model.addAttribute("countyList", countyList);
        //宽带新装信息   HE_FAMILY_BROADBAND_CATEGORY_ID 和家庭， BROADBAND_CATEGORY_ID_ADD 裸宽
        Map<String, Object> bbArgs = new HashMap<>();
        if("province".equals(reissueType)){
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_BAND_FAMILY"));
        }else if("state".equals(reissueType)){
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_STATE_BAND_FAMILY"));
        }else{
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_NEW_FAMILY"));
        }
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
            heBroadbandItemList = BroadbandUtils.convertNewHeBroadbandItemEntityList(goodsInfoList);

            Collections.sort(heBroadbandItemList, new Comparator<BroadbandItemEntity>() {
                @Override
                public int compare(BroadbandItemEntity arg0, BroadbandItemEntity arg1) {
                    return arg0.getProductLevel().compareTo(arg1.getProductLevel());
                }
            });
        } catch (Exception e) {
            logger.error("和家庭办理异常:" + e.getMessage());
            writerFlowLogThrowable(streamNo, "", "", getClass().getName(), "bandInstall", null, "新和家庭办理异常:" + processThrowableMessage(e));
            e.printStackTrace();
            throw e;
        }
        model.addAttribute("heBroadbandItemList", heBroadbandItemList);
        /**
         * 模拟参数
         */
        String fee = queryFee(installPhoneNum);
        Long balance = Long.parseLong(fee) / 100;
        model.addAttribute("hasPackage", hasPackage);
        model.addAttribute("balance", balance);
        model.addAttribute("packageName", packageName);
        model.addAttribute("curentPackage", curentPackage);

        writerFlowLogExitMenthod(streamNo, "", "", getClass().getName(),
                "bandInstall", null, null, "新和家庭宽带办理");

        return "web/goods/broadband/newHeFamily/newHeChoose";
    }

    @RequestMapping("confirmHeInstall")
    public String confirmHeInstall(HttpServletRequest request, Model model) throws Exception {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(),
                "confirmHeInstall", request.getParameterMap(), "新和家庭宽带下单", request);
        // 地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
        // 默认区县信息(长沙市)
//        List<ThirdLevelAddress> countyList = null;
        if (!CollectionUtils.isEmpty(cityList)) {
//            countyList = memberAddressService.getChildrensByPId(cityList.get(0).getOrgId() + "");
//            logger.info("地址编码:" + countyList);
            // 地市信息处理 用于适配Boss接口查询条件
            for (ThirdLevelAddress city : cityList) {
                city.setOrgName(city.getOrgName().replace("市", ""));
                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                    city.setOrgName("湘西州");
                }
            }
        }
        model.addAttribute("cityList", cityList);
//        model.addAttribute("countyList", countyList);
        model.addAttribute("goodsSkuId", request.getParameter("form1_skuId"));
        model.addAttribute("goodsName", request.getParameter("form1_goodsName"));
        model.addAttribute("addressName", request.getParameter("form1_addressName"));
        model.addAttribute("houseCode", request.getParameter("form1_houseCode"));
        model.addAttribute("maxWidth", request.getParameter("form1_maxWidth"));
        model.addAttribute("freePort", request.getParameter("form1_freePort"));
        model.addAttribute("eparchyCode", request.getParameter("form1_eparchyCode"));
        model.addAttribute("county", request.getParameter("form1_county"));
        model.addAttribute("coverType", request.getParameter("form1_coverType"));
        model.addAttribute("chooseCat", request.getParameter("form1_chooseCat"));
        model.addAttribute("chooseBandWidth", request.getParameter("form1_chooseBandWidth"));
        model.addAttribute("productId", request.getParameter("submit_productId"));
        model.addAttribute("packageId", request.getParameter("submit_packageId"));
        model.addAttribute("discntCode", request.getParameter("submit_discntCode"));
        model.addAttribute("Mbh", request.getParameter("form1_Mbh"));
        model.addAttribute("hasBroadband", request.getParameter("form1_hasBroadband"));
        model.addAttribute("hasMbh", request.getParameter("form1_hasMbh"));
        model.addAttribute("COMMUNITY_TYPE", request.getParameter("form1_communityType"));
        model.addAttribute("addrDesc", request.getParameter("form1_addrDesc"));
        model.addAttribute("bookInstallDate", request.getParameter("bookInstallDate"));
        model.addAttribute("bookInstallTime", request.getParameter("bookInstallTime"));
        model.addAttribute("form1_phoneNum", request.getParameter("form1_phoneNum"));
        model.addAttribute("installName", request.getParameter("installName"));
        model.addAttribute("form1_price", request.getParameter("form1_price"));
        model.addAttribute("idCard", request.getParameter("idCard"));
        model.addAttribute("homeData", request.getParameter("form1_homedata"));
        model.addAttribute("homeVoice", request.getParameter("form1_homevoice"));
        model.addAttribute("giveData", request.getParameter("form1_giveData"));
        model.addAttribute("giveMbh", request.getParameter("form1_giveMbh"));
        writerFlowLogExitMenthod(streamNo, "", "", getClass().getName(),
                "confirmHeInstall", null, null, "新和家庭宽带下单");
        return "web/goods/broadband/newHeFamily/newHeInstall";
    }

    /**
     * 订单确认
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("confirmHeInstallDetail")
    public String confirmOrder(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String streamNo = createStreamNo();
        writerFlowLogEnterMenthod(streamNo, "", "", getClass().getName(),
                "confirmOrder", request.getParameterMap(), "新和家庭宽带确认订单", request);
        model.addAttribute("goodsSkuId", request.getParameter("form1_skuId"));
        model.addAttribute("goodsName", request.getParameter("form1_goodsName"));
        model.addAttribute("form1_price", request.getParameter("form1_price"));
        model.addAttribute("addressName", request.getParameter("form1_addressName"));
        model.addAttribute("houseCode", request.getParameter("form1_houseCode"));
        model.addAttribute("maxWidth", request.getParameter("form1_maxWidth"));
        model.addAttribute("freePort", request.getParameter("form1_freePort"));
        model.addAttribute("eparchyCode", request.getParameter("form1_eparchyCode"));
        model.addAttribute("county", request.getParameter("form1_county"));
        model.addAttribute("coverType", request.getParameter("form1_coverType"));
        model.addAttribute("chooseCat", request.getParameter("form1_chooseCat"));
        model.addAttribute("chooseBandWidth", request.getParameter("form1_chooseBandWidth"));
        model.addAttribute("productId", request.getParameter("form1_productId"));
        model.addAttribute("packageId", request.getParameter("form1_packageId"));
        model.addAttribute("discntCode", request.getParameter("form1_discntCode"));
        model.addAttribute("Mbh", request.getParameter("form1_Mbh"));
        model.addAttribute("hasBroadband", request.getParameter("form1_hasBroadband"));
        model.addAttribute("hasMbh", request.getParameter("form1_hasMbh"));
        model.addAttribute("COMMUNITY_TYPE", request.getParameter("form1_communityType"));
        model.addAttribute("addrDesc", request.getParameter("form1_addrDesc"));
        model.addAttribute("bookInstallDate", request.getParameter("bookInstallDate"));
        model.addAttribute("bookInstallTime", request.getParameter("bookInstallTime"));
        model.addAttribute("form1_phoneNum", request.getParameter("form1_phoneNum"));
        model.addAttribute("installName", request.getParameter("installName"));
        model.addAttribute("isBookInstall", request.getParameter("isBookInstall"));
        model.addAttribute("idCard", request.getParameter("idCard"));
        model.addAttribute("form1_giveMbh", request.getParameter("form1_giveMbh"));
        model.addAttribute("payMode", request.getParameter("payMode"));
        model.addAttribute("payModeName", request.getParameter("payModeName"));
        model.addAttribute("contactPhone", request.getParameter("contactPhone"));
        writerFlowLogExitMenthod(streamNo, "", "", getClass().getName(),
                "confirmOrder", null, null, "新和家庭宽带确认");
        return "web/goods/broadband/newHeFamily/newHeConfirmDetail";
    }

    /**
     * 提交订单保存临时表
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("submitTempOrder")
    public Map<String, Object> submitInstallOrder(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String skuId = request.getParameter("form1_skuId");
        //年限
        String term = request.getParameter("form1_term");
        //带宽
        String bandWidth = request.getParameter("form1_chooseBandWidth");
        //标准地址编码
        String houseCode = request.getParameter("form1_houseCode");
        //安装地址
        String installAddress = request.getParameter("form1_addressName");
        //接入方式
        String accessType = request.getParameter("form1_coverType");
        //手机
        String phoneNum = request.getParameter("form1_phoneNum");
        //联系人手机号
        String contactPhone = request.getParameter("contactPhone");

        //装机人姓名
        String installName = request.getParameter("installName");
        //身份证号码
        String idCard = request.getParameter("idCard");
        //支付方式
        String payMode = request.getParameter("payMode");
        //payModeId 2:在线支付;1:现场支付
        payMode = StringUtils.isEmpty(payMode) ? "0" : payMode;
        //最大带宽
        String maxWidth = request.getParameter("form1_maxWidth");
        //最大端口数
        String freePortNum = request.getParameter("form1_freePort");
        //地市编码
        String eparchyCode = request.getParameter("form1_eparchyCode");
        //是否有推荐人
        String referrer = request.getParameter("referrer");
        //0：员工推荐,1:渠道推荐
        String recommendPerson = request.getParameter("recommendPerson");
        //员工编号
        String empNO = request.getParameter("form1_EmpNO");
        //和家庭套餐产品id
        String productId = request.getParameter("form1_productId");
        //产品费用
        String productPrice = request.getParameter("form1_price");
        //产品TRADE_TYPE_CODE类型
        String chooseProduct = "0".equals(productPrice) ? "" : "1012";

        String form1_broadbandPrice = request.getParameter("form1_broadbandPrice");
        String hasBroadband = request.getParameter("form1_hasBroadband");
        String hasMbh = request.getParameter("form1_hasMbh");

        // 是否预约装机
        String isBookInstall = request.getParameter("isBookInstall");
        // 预约装机日期
        // 预约装机时间
        // 小区地址类型
        String communityType = request.getParameter("form1_communityType");
        String bookInstallDate = request.getParameter("bookInstallDate");//预约安装日期
        String bookInstallTime = request.getParameter("bookInstallTime");//预约安装日期段
        //工号密码
        String staffPwd = request.getParameter("staffPwd");//预约安装日期段

        //是否购买宽带光猫
        String chooseCat = request.getParameter("form1_chooseCat");
        //MODEM方式 3:客户自备;4:自动配发
        String modemSaleType = StringUtils.isEmpty(chooseCat) ? "3" : "4";
        //光猫TRADE_TYPE_CODE类型
        String tradeTypeCodeGm = "1002";
        //光猫费用
        long gmPrice = 0L;
        //费用类型
        String gmPriceType = "400";

        //魔百和类型 0:芒果 1:未来
        String tvType = request.getParameter("form1_Mbh");
        //魔百和TRADE_TYPE_CODE类型
        String tradeTypeCodeMbh = "3709";
        //魔百和费用
        long MbhPrice = 0L;
        //魔百和费用类型
        String MbhPriceType = "420";
        //机顶盒方式: 0 客户自备，1 自动配发
        String TVBOX_SALE_TYPE = StringUtils.isEmpty(tvType) ? "0" : "1";

        String cityPre = eparchyCode.substring(2);
        if (StringUtils.isNotEmpty(productId)) {
            productId = productId.replace("XX", cityPre);
        }

		/*宽带商品信息*/
        Map<String, Object> map = Maps.newHashMap();
        map.put("goodsSkuId", skuId);
        map.put("containGoodsSkuIdInfo", true);
        map.put("containGoodsBusiParam", true);
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
        Map<String, Object> resMap = Maps.newHashMap();
        if (Collections3.isEmpty(goodsInfoList)) {
            resMap.put("code", "1");
            resMap.put("message", "查询不到商品信息");
            return resMap;
        }
        if (goodsInfoList.size() > 1) {
            resMap.put("code", "1");
            resMap.put("message", "商品数据错误,商品数据不唯一!");
            return resMap;
        }
        TfGoodsInfo tfGoodsInfo = goodsInfoList.get(0);
        if (Collections3.isEmpty(tfGoodsInfo.getTfGoodsSkuList())) {
            resMap.put("code", "1");
            resMap.put("message", "商品SKU数据错误,商品SKU数据为空!");
            return resMap;
        }
        if (tfGoodsInfo.getTfGoodsSkuList().size() != 1) {
            resMap.put("code", "1");
            resMap.put("message", "商品SKU数据错误,商品SKU数据不唯一!");
            return resMap;
        }
        //校验
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(phoneNum);
        resMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        //校验未通过
        //校验未通过
        if ("0".equals(resMap.get("respCode")) && resMap.get("result") != null) {
            JSONArray array = (JSONArray) resMap.get("result");
            com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
            if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                if (!"731".equals(resultObj.get("PROVINCE_CODE"))) {
                    resMap.put("code", "-1");
                    resMap.put("message", "请输入湖南移动号码!");
                    return resMap;
                }
            }else{
                resMap.put("code", "-1");
                resMap.put("message", resultObj.get("X_RESULTINFO"));
                return resMap;
            }
        }else{
            resMap.put("code", "-1");
            resMap.put("message", "归属地接口调用异常!");
            return resMap;
        }
        HeFamilyTradeCheckCondition condition = new HeFamilyTradeCheckCondition();
        condition.setSerial_number(phoneNum);
        resMap = heFamilyService.heFamilyTradeCheck(condition);
        if ("0".equals(resMap.get("respCode")) && resMap.get("result") != null) {
            if (!"0".equals(resMap.get("respCode"))) {
                resMap.put("code", resMap.get("respCode"));
                resMap.put("message", resMap.get("respDesc"));
                return resMap;
            }
        }else{
                resMap.put("code", "-1");
                resMap.put("message", "归属地接口调用异常!");
                return resMap;
        }
        logger.info("checkResult:" + resMap);
        logger.info("goodsInfoList:" + JSON.toJSONString(goodsInfoList));
        BroadbandItemEntity broadbandItem = null;
        broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
        logger.info("broadbandItem:" + JSON.toJSONString(broadbandItem));
        long goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(broadbandItem.getPrice() + ""));
        if ("4".equals(modemSaleType) && broadbandItem.getModemPrice() != null) {//光猫自动派发
            gmPrice = broadbandItem.getModemPrice();
        }
        if ("1".equals(TVBOX_SALE_TYPE) && broadbandItem.getStbPrice() != null) {//机顶盒 自动派发
            MbhPrice = broadbandItem.getStbPrice();
        }
        int broadbandPrice = broadbandItem.getPrice().intValue();
        /*订单明细业务参数信息*/
        List<O2oOrderParamTemp> paramList = Lists.newArrayList();
        BroadbandHeRegCondition broadbandHeRegCondition = new BroadbandHeRegCondition();
        ParamUtils paramUtils = new ParamUtils();
        paramUtils.addParams("SERIAL_NUMBER", phoneNum, "手机号码");
        paramUtils.addParams("PRODUCT_ID", productId, "和家庭套餐产品id");
        paramUtils.addParams("BROADBAND_PRICE", String.valueOf(broadbandPrice), "和家庭套餐产品id");
        paramUtils.addParams("ROUTE_EPARCHY_CODE", eparchyCode, "渠道用户市区编号");
        broadbandHeRegCondition.setSerialNumber(phoneNum);
        broadbandHeRegCondition.setProductId(productId);
        long totalPrice = 0;
        String TRADE_TYPE_CODE = null;
        String pay = null;
        String FEE_TYPE_CODE = null;
        String FEE_MODE = null;
        if (!StringUtils.isNotEmpty(tvType)) {
            hasMbh = "0";
        }
        //无宽带，无魔百和
        if ("1".equals(hasBroadband) && "1".equals(hasMbh)) {
            TRADE_TYPE_CODE = 1012 + "," + tradeTypeCodeGm + "," + tradeTypeCodeMbh + "," + 1002;
            pay = 0 + "," + gmPrice + "," + MbhPrice + "," + goodsSkuPrice;
            FEE_TYPE_CODE = 111 + "," + gmPriceType + "," + MbhPriceType + "," + 410;
            FEE_MODE = "2,0,0,2";

            paramUtils.addParams("ADDR_ID", houseCode, "装机地址编码");
            paramUtils.addParams("ADDR_NAME", installAddress, "宽带安装地址名称");
            paramUtils.addParams("ADDR_DESC", installAddress, "宽带安装地址明细");
            paramUtils.addParams("ACCESS_TYPE", accessType, "接入方式");
            paramUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
            paramUtils.addParams("FREE_PORT_NUM", freePortNum, "空闲端口数");
            paramUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
            paramUtils.addParams("TV_TYPE", tvType, "魔百和类型");
            paramUtils.addParams("TVBOX_SALE_TYPE", TVBOX_SALE_TYPE, "机顶盒方式");
            paramUtils.addParams("IS_EXISTS", "0", "是否存量宽带用户转入");
            paramUtils.addParams("CONTACT", installName, "联系人");
            paramUtils.addParams("CONTACT_PHONE", contactPhone, "联系电话");
            broadbandHeRegCondition.setAddrId(houseCode);
            broadbandHeRegCondition.setAddrName(installAddress);
            broadbandHeRegCondition.setAddrDesc(installAddress);
            broadbandHeRegCondition.setAccessType(accessType);
            broadbandHeRegCondition.setMaxWidth(maxWidth);
            broadbandHeRegCondition.setFreePortNum(freePortNum);
            broadbandHeRegCondition.setModemSaleType(modemSaleType);
            broadbandHeRegCondition.setTvType(tvType);
            broadbandHeRegCondition.setTvboxSaleType(TVBOX_SALE_TYPE);
            broadbandHeRegCondition.setIsExists("0");
            broadbandHeRegCondition.setContact(installName);
            broadbandHeRegCondition.setContactPhone(contactPhone);
            totalPrice = gmPrice + MbhPrice + goodsSkuPrice;

            //有宽带，无魔百和
        } else if ("0".equals(hasBroadband) && "1".equals(hasMbh)) {
            TRADE_TYPE_CODE = 1012 + "," + tradeTypeCodeMbh;
            pay = 0 + "," + MbhPrice;
            FEE_TYPE_CODE = 111 + "," + MbhPriceType;
            FEE_MODE = "2,0";
            paramUtils.addParams("TV_TYPE", tvType, "魔百和类型");
            paramUtils.addParams("TVBOX_SALE_TYPE", TVBOX_SALE_TYPE, "机顶盒方式");
            paramUtils.addParams("IS_EXISTS", "1", "是否存量宽带用户转入");
            broadbandHeRegCondition.setTvType(tvType);
            broadbandHeRegCondition.setTvboxSaleType(TVBOX_SALE_TYPE);
            broadbandHeRegCondition.setIsExists("1");
            totalPrice = MbhPrice;

        } else if ("0".equals(hasBroadband) && "0".equals(hasMbh)) {
            TRADE_TYPE_CODE = "1012";
            pay = 0 + "";
            FEE_TYPE_CODE = "111";
            FEE_MODE = "2";

            paramUtils.addParams("IS_EXISTS", "1", "是否存量宽带用户转入");
            broadbandHeRegCondition.setIsExists("1");
            totalPrice = 0;
            //无宽带有魔百合
        } else if ("1".equals(hasBroadband) && "0".equals(hasMbh)) {
            TRADE_TYPE_CODE = 1012 + "," + tradeTypeCodeGm + "," + 1002;
            pay = 0 + "," + gmPrice + "," + goodsSkuPrice;
            FEE_TYPE_CODE = 111 + "," + gmPriceType + "," + 410;
            FEE_MODE = "2,0,2";

            paramUtils.addParams("ADDR_ID", houseCode, "装机地址编码");
            paramUtils.addParams("ADDR_NAME", installAddress, "宽带安装地址名称");
            paramUtils.addParams("ADDR_DESC", installAddress, "宽带安装地址明细");
            paramUtils.addParams("ACCESS_TYPE", accessType, "接入方式");
            paramUtils.addParams("MAX_WIDTH", maxWidth, "最大带宽");
            paramUtils.addParams("FREE_PORT_NUM", freePortNum, "空闲端口数");
            paramUtils.addParams("MODEM_SALE_TYPE", modemSaleType, "MODEM方式");
//            paramUtils.addParams("TV_TYPE",tvType,"魔百和类型");
//            paramUtils.addParams("TVBOX_SALE_TYPE",TVBOX_SALE_TYPE,"机顶盒方式");
            paramUtils.addParams("IS_EXISTS", "0", "是否存量宽带用户转入");
            paramUtils.addParams("CONTACT", installName, "联系人");
            paramUtils.addParams("CONTACT_PHONE", contactPhone, "联系电话");
            broadbandHeRegCondition.setAddrId(houseCode);
            broadbandHeRegCondition.setAddrName(installAddress);
            broadbandHeRegCondition.setAddrDesc(installAddress);
            broadbandHeRegCondition.setAccessType(accessType);
            broadbandHeRegCondition.setMaxWidth(maxWidth);
            broadbandHeRegCondition.setFreePortNum(freePortNum);
            broadbandHeRegCondition.setModemSaleType(modemSaleType);
//            broadbandHeRegCondition.setTvType(tvType);
//            broadbandHeRegCondition.setTvboxSaleType(TVBOX_SALE_TYPE);
            broadbandHeRegCondition.setIsExists("0");
            broadbandHeRegCondition.setContact(installName);
            broadbandHeRegCondition.setContactPhone(contactPhone);
            totalPrice = gmPrice + MbhPrice + goodsSkuPrice;

        }


        paramUtils.addParams("TRADE_TYPE_CODE", TRADE_TYPE_CODE, "订单类型");
        paramUtils.addParams("FEE", pay, "应缴");
        paramUtils.addParams("PAY", pay, "实缴");
        paramUtils.addParams("FEE_TYPE_CODE", FEE_TYPE_CODE, "费用类型");
        paramUtils.addParams("FEE_MODE", FEE_MODE, "营业费");
        if ("0".equals(isBookInstall) || StringUtils.isAnyEmpty(isBookInstall)) {
            paramUtils.addParams("COMMUNITY_TYPE", "", "小区地址类型");
            paramUtils.addParams("BOOK_INSTALL_DATE", null, "预约装机日期");
            paramUtils.addParams("BOOK_INSTALL_TIME", null, "预约装机时间");
            broadbandHeRegCondition.setCommunityType("");
            broadbandHeRegCondition.setBookInstallDate(null);
            broadbandHeRegCondition.setBookInstallTime(null);
        } else {
            paramUtils.addParams("COMMUNITY_TYPE", communityType, "小区地址类型");
            paramUtils.addParams("BOOK_INSTALL_DATE", bookInstallDate, "预约装机日期");
            paramUtils.addParams("BOOK_INSTALL_TIME", bookInstallTime, "预约装机时间");
            broadbandHeRegCondition.setCommunityType(communityType);
            broadbandHeRegCondition.setBookInstallDate(bookInstallDate);
            broadbandHeRegCondition.setBookInstallTime(bookInstallTime);
        }
        List<Map<String, Object>> feeList = new ArrayList<Map<String, Object>>();
        Map<String, Object> feeMap = new HashMap<String, Object>();
        feeMap.put("TRADE_TYPE_CODE", TRADE_TYPE_CODE);
        feeMap.put("FEE_TYPE_CODE", FEE_TYPE_CODE);
        feeMap.put("FEE_MODE", FEE_MODE);
        feeMap.put("FEE", pay);
        feeMap.put("PAY", pay);
        feeList.add(feeMap);
//        broadbandHeRegCondition.setFeeList(feeList);
        broadbandHeRegCondition.setFeeList(paramUtils.parseFeeList(feeList));

        // 添加渠道信息
        MemberVo memberVo = UserUtils.getLoginUser(request);
//        o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneNum);
//        o2oParamUtils.addConditionChannel(broadbandHeRegCondition,memberVo.getChannelInfo(),phoneNum);
        //----增加受理中校验接口开始。。。
        broadbandHeRegCondition.setPreType("1");
        //增加受理中校验参数
//        preMap.put("PRE_TYPE","1");
        logger.info("broadbandHeRegCondition:" + JSON.toJSONString(broadbandHeRegCondition));
        Map result = heFamilyService.broadbandHeReg(broadbandHeRegCondition);
        logger.info("和家庭宽带业务受理中校验接口返回： " + result);

        if (null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)) {
            //throw new Exception(" 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            resMap.put("code", "1");
            resMap.put("message", " 受理中校验失败：【 code = " + result.get(OrderConstant.IF_RESULT_CODE) + " message = " + result.get(OrderConstant.IF_RESULT_INFO) + " 】");
            return resMap;
        }
        //----增加受理中校验接口结束。。。
        if ("2".equals(payMode) && totalPrice > 0) {
            paramUtils.addParams("IN_MODE_CODE", "P", "渠道交易类型");
        }
        String goodsFormat = broadbandItem.getBroadbandItemName() + "/" + broadbandItem.getTerm();

        TfOrderSub orderSub = new TfOrderSub();
    	/*订单会员关联*/
        TfOrderUserRef orderUserRef = new TfOrderUserRef();
        orderUserRef.setMemberId(memberVo.getMemberLogin().getMemberId());
        orderUserRef.setMemberLogingName(memberVo.getMemberLogin().getMemberLogingName());
        orderUserRef.setContactPhone(phoneNum);
        // orderUserRef.setMemberId(1L);
        // orderUserRef.setMemberLogingName("游客测试");
        orderUserRef.setCounty(request.getParameter("form1_county"));  //区县
        orderUserRef.setEparchyCode(eparchyCode);  //地市编码
        orderSub.setOrderUserRef(orderUserRef);
    			/*明细信息*/
        TfOrderSubDetail orderSubDetail = new TfOrderSubDetail();
        orderSubDetail.setGoodsId(broadbandItem.getGoodsId());
        orderSubDetail.setGoodsSkuId(broadbandItem.getGoodsSkuId());
        orderSubDetail.setGoodsName(goodsInfoList.get(0).getGoodsName());
        orderSubDetail.setGoodsSkuPrice(goodsSkuPrice);
        orderSubDetail.setGoodsSkuNum(1L);
        orderSubDetail.setRootCateId(OrderConstant.CATE_BROADBAND);
        orderSubDetail.setGoodsFormat(goodsFormat);
        orderSubDetail.setShopId(BroadbandConstants.SHOP_ID);
        orderSubDetail.setShopName(BroadbandConstants.SHOP_NAME);
        orderSubDetail.setShopTypeId(6);
        orderSubDetail.setOrderSubDetailBusiParams(paramUtils.getParamsList());
        orderSub.addOrderDetail(orderSubDetail);
          /*设置订单信息*/
        //店铺信息
        orderSub.setShopId(BroadbandConstants.SHOP_ID);
        orderSub.setShopName(BroadbandConstants.SHOP_NAME);
        orderSub.setOrderTypeId(OrderConstant.TYPE_HE_BROADBAND);
        orderSub.setOrderSubAmount(totalPrice);    //子订单总额
        orderSub.setOrderSubPayAmount(totalPrice);//支付金额
        orderSub.setOrderSubDiscountAmount(0L);//优惠金额
        orderSub.setOrderChannelCode("E007");//渠道编码，网上商城
        orderSub.setPayModeId(Integer.parseInt(payMode));//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
        orderSub.setDeliveryModeId(3);//配送方式，虚拟商品
        orderSub.setIsInvoicing(0);//是否开发票:0-不开
        orderSub.setPhoneNumber(phoneNum);
        logger.info("和家庭立即办理,提交订单参数orderSub:" + JSONArray.toJSONString(orderSub));
        List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);

        model.addAttribute("orderSub", orderSubList.get(0));
        model.addAttribute("orderSubDetail", orderSubList.get(0).getDetailList().get(0));
        resMap.put("code", "0");
        resMap.put("message", "生成订单成功");
        resMap.put("orderSub", orderSubList.get(0));
        resMap.put("orderSubDetail", orderSubList.get(0).getDetailList().get(0));
        resMap.put("orderSubId", String.valueOf(orderSubList.get(0).getOrderSubId()));
        return resMap;
    }


    @RequestMapping("queryCommunityType")
    @ResponseBody
    public Map<String, Object> queryCommunityType(HttpServletRequest request, String houseCode) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        QryAddressAttrCondition condition = new QryAddressAttrCondition();
        condition.setADDR_ID(houseCode);
        Map result = qryAddressService.queryAddressAttr(condition);
        if ("0".equals(result.get("respCode")) && result.get("result") != null) {
            JSONArray array = (JSONArray) result.get("result");
            JSONObject resultObj = array.getJSONObject(0);
            if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                resultMap.put("respCode", "0");
                resultMap.put("data", resultObj.get("COMMUNITY_TYPE"));
                // 城区
                if ("02".equals(resultObj.getString("COMMUNITY_TYPE")) || "03".equals(resultObj.getString("COMMUNITY_TYPE"))) {
                    // 乡镇
                } else {
                }
            } else {
                resultMap.put("respCode", "-1");
                resultMap.put("data", resultObj.getString("X_RESULTINFO"));
            }
        } else {
            throw new Exception("地址城乡类型查询失败!");
        }
        return resultMap;
    }

    public String queryFee(String SERIAL_NUMBER) throws Exception {
        HqQueryFeeCondition hqQueryFeeCondition = new HqQueryFeeCondition();
        hqQueryFeeCondition.setSERIAL_NUMBER(SERIAL_NUMBER);
        Map userFee = flowServeService.queryFee(hqQueryFeeCondition);
        logger.info("fee:" + JSON.toJSONString(userFee));
        if (!HNanConstant.SUCCESS.equals(userFee.get("respCode")) || MapUtils.isEmpty(userFee)) {
            throw new ServiceException(HNanConstant.FAIL, userFee.get("respDesc").toString(), null);
        }
        return String.valueOf(((Map) (net.sf.json.JSONArray.fromObject(userFee.get("result"))).get(0)).get("NEW_ALLLEAVE_FEE"));
    }

    /**
     * 查询补办宽带信息
     * @param type
     * @return
     */
    private Map<String,String> getBandCodeMap(String type){
        Map<String,String> result = new HashMap<>();
        List<Dict> dictList = dictService.getDictList(type);
        for(Dict dict:dictList){
            result.put(dict.getLabel(),dict.getValue());
        }
        return result;
    }
}
