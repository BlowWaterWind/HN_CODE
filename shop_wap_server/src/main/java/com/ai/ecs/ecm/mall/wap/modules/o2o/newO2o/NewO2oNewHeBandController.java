package com.ai.ecs.ecm.mall.wap.modules.o2o.newO2o;

import com.ai.ecs.common.framework.exception.ServiceException;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.entity.order.AcceptFeeCalculateCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqQueryFeeCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.queryPackage.entity.QueryAccountPackagesCondition;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.ai.ecs.order.constant.OrderConstant;
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
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * copy O2oNewHeBandController
 * 新和家庭办理
 * Created by think on 2017/12/12.
 */
@Controller
@RequestMapping("newO2oNewHeBand")
public class NewO2oNewHeBandController extends BaseController {
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
    private O2oParamUtils o2oParamUtils;

    @Autowired
    private O2oOrderTempService orderTempService;

    @Autowired
    private O2oOrderParamTemService orderParamTemService;

    @Autowired
    private QryAddressService qryAddressService;

    @Autowired
    private QueryAccountPackagesService queryAccountPackagesService;

    @Autowired
    JedisCluster jedisCluster;


    private Logger logger = Logger.getLogger(NewO2oNewHeBandController.class);

    private static final String PRODUCT_DETAIL = "bandProductDetail";

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";
    private static Map bandCodeMap = new HashMap();
    private static Map countyCodeMap = new HashMap();
    private static Map stateCodeMap = new HashMap();
    static {
        bandCodeMap.put("99813092","51281030");
        bandCodeMap.put("99813093","51281031");
        bandCodeMap.put("99813094","51281032");
        bandCodeMap.put("99813095","51281033");
        bandCodeMap.put("99813096","51281034");
        bandCodeMap.put("99813097","51281035");
        bandCodeMap.put("99813098","51281036");
        bandCodeMap.put("99813099","51281037");
        bandCodeMap.put("99813100","51281038");
        bandCodeMap.put("99813123","51081031");//99


        /**
         * 两节省内和家庭补办
         */
        countyCodeMap.put("99543246","51081021");//79
        countyCodeMap.put("99543247","51081022");//99
        countyCodeMap.put("99543248","51081023");//129
        countyCodeMap.put("99543249","51081024");//169
        countyCodeMap.put("99543250","51081025");//199
        countyCodeMap.put("99543266","51081044");//249
        countyCodeMap.put("99543251","51081026");//299
        countyCodeMap.put("99543267","51081045");//349
        countyCodeMap.put("99543252","51081027");//399

        /**
         * 全国流量和家庭补办
         */
        stateCodeMap.put("99813092","51081021");//79
        stateCodeMap.put("99813093","51081022");//99
        stateCodeMap.put("99813094","51081023");//129
        stateCodeMap.put("99813095","51081024");//169
        stateCodeMap.put("99813096","51081025");//199
        stateCodeMap.put("99813097","51081044");//249
        stateCodeMap.put("99813098","51081026");//299
        stateCodeMap.put("99813099","51081045");//349
        stateCodeMap.put("99813100","51081027");//399
        stateCodeMap.put("99813123","51081022");//399


        /**
         * 全国流量和家庭补办(不带宽带版)
         */
        stateCodeMap.put("99543246","51081021");//79
        stateCodeMap.put("99543247","51081022");//99
        stateCodeMap.put("99543248","51081023");//129
        stateCodeMap.put("99543249","51081024");//169
        stateCodeMap.put("99543250","51081025");//199
        stateCodeMap.put("99543266","51081044");//249
        stateCodeMap.put("99543251","51081026");//299
        stateCodeMap.put("99543267","51081045");//349
        stateCodeMap.put("99543252","51081027");//399
    }


    /**
     * 号码填写
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("newInit")
    public String init(HttpServletRequest request , HttpServletResponse response, Model model,String secToken)  throws Exception {
        String busType = request.getParameter("busType");
        //获取办理用户手机号码
        MemberVo memberVo = UserUtils.getLoginUser(request);
        ChannelInfo channelInfo = memberVo.getChannelInfo();
        ShopInfo shopInfo=memberVo.getShopInfo();
        model.addAttribute("staffId",channelInfo.getTradeStaffId());
        model.addAttribute("shopId",shopInfo.getShopId());
        model.addAttribute("secToken",secToken);
        model.addAttribute("busType",busType);
        return "web/broadband/newO2o/newHeFamily/o2oNewHeInit";
    }

    @RequestMapping("newHeInstall")
    public String bandInstall(HttpServletRequest request,Model model) throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String streamNo=createStreamNo();
        //是否是省内和家庭补办宽带
        String busType = request.getParameter("busType");
        //是否是已办理宽带，已存在地址信息
        String hasAddress = request.getParameter("hasAddress");
        //办理号码
        String installPhoneNum = request.getParameter("installPhoneNum");
        if(StringUtils.isBlank(installPhoneNum)){
            /**
             *当受检查的值是null时，返回true，当受检查值时""时，返回值时true，当受检查值是空字符串时，返回值是true。
             */
            throw new Exception("请输入手机号!");
        }else{
            model.addAttribute("installPhoneNum", installPhoneNum);
        }
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        model.addAttribute("eparchy_Code","0731");
        resultMap.put("isBroadBand", 1);
        model.addAttribute("resultMap",resultMap);
        //校验
        try {
            PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
            phoneAttributionModel1.setSerialNumber(installPhoneNum);
            resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
            String EPARCHY_CODE= String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
            model.addAttribute("eparchy_Code", EPARCHY_CODE);
            if ("0".equals(resultMap.get("respCode")) && resultMap.get("result") != null) {
                if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
                    resultMap.put("respCode", "-1");
                    resultMap.put("respDesc", "请输入湖南移动号码办理!");
                    model.addAttribute("resultMap",resultMap);
                    return "web/broadband/newO2o/newHeFamily/checkError";
                 }
                model.addAttribute("eparchy_Code",EPARCHY_CODE);
            }else{
                throw new Exception("归属地接口查询异常！");
            }
        }catch (Exception e) {
            logger.error("手机号码归属地 :"+e.getMessage());
            model.addAttribute("resultMap",resultMap);
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
            Map resMap = (Map) resultObj.get("DISCNT_INFO");
            String resfee = (String) resMap.get("MAIN_DISCNT_FEE");
            String discntCode = (String) resMap.get("MAIN_DISCNT_CODE");
            packageName = (String) resMap.get("MAIN_DISCNT_NAME");
            if(StringUtils.isNotEmpty(resfee)){
                curentPackage = Long.parseLong(resfee)/100;
            }
            //是否是省内补办宽带
            if("reissue".equals(busType)){
                if(countyCodeMap.get(discntCode)!=null){
                    packageCode = (String) countyCodeMap.get(discntCode);
                    model.addAttribute("package_code",countyCodeMap.get(discntCode));
                    model.addAttribute("busType",busType);
                    model.addAttribute("reissueType","province");
                    reissueType = "province";
                    hasPackage = true;
                }
                if(stateCodeMap.get(discntCode)!=null){
                    packageCode = (String) stateCodeMap.get(discntCode);
                    model.addAttribute("package_code",stateCodeMap.get(discntCode));
                    model.addAttribute("busType",busType);
                    model.addAttribute("reissueType","state");
                    reissueType = "state";
                    hasPackage = true;
                }
                if(StringUtils.isEmpty(packageCode)){
                    throw new Exception("不是和家庭用户，不能办理此业务!");
                }
            }
            /**
             * 判断用户是否办理新和家庭套餐
             */
           else if(bandCodeMap.get(discntCode)!=null){
                model.addAttribute("package_code",bandCodeMap.get(discntCode));
                hasPackage = true;
            }
        } else {
            throw new Exception("查询当前套餐失败!");
        }

        // 查询用户账号信息
        HeFamilyCheckCondition condition = new HeFamilyCheckCondition();
        condition.setSerial_number(String.valueOf(installPhoneNum));
        resultMap = heFamilyService.heFamilyCheck(condition);
        JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
        JSONObject result = (JSONObject) resultArray.get(0);
        String isBroadBand = (String)result.get("BROADBAND_FLAG") ;
        String isMbh = (String)result.get("INTERACTIVE_FLAG") ;
        String resultCode = (String)result.get("X_RESULTCODE") ;
        String resultInfo = (String)result.get("X_RESULTINFO") ;
        resultMap.put("isBroadBand", isBroadBand);
        resultMap.put("isMbh", isMbh);
        resultMap.put("resultCode", resultCode);
        resultMap.put("respDesc", resultInfo);
        model.addAttribute("resultMap",resultMap);
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
                    return "web/broadband/newO2o/newHeFamily/checkError";
                }else{
                    return "web/broadband/newO2o/newHeFamily/o2oHasBroadband";
                }
            }catch (Exception e) {
                e.printStackTrace();
                logger.error("已有宽带用户信息查询："+e.getMessage());
            }
        }

        // 地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
        // 默认区县信息(长沙市)
        if (!CollectionUtils.isEmpty(cityList)) {
            // 地市信息处理 用于适配Boss接口查询条件
            for (ThirdLevelAddress city : cityList) {
                city.setOrgName(city.getOrgName().replace("市", ""));
                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                    city.setOrgName("湘西州");
                }
            }
        }
        model.addAttribute("cityList", cityList);
        //宽带新装信息   HE_FAMILY_BROADBAND_CATEGORY_ID 和家庭， BROADBAND_CATEGORY_ID_ADD 裸宽
        Map<String,Object> bbArgs = new HashMap<>();
        if("province".equals(reissueType)){
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_BAND_FAMILY"));
        }else if("state".equals(reissueType)){
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_STATE_BAND_FAMILY"));
        }else{
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_NEW_FAMILY"));
        }
        List<String> eparchyCodes = new ArrayList<>();
        eparchyCodes.add(BroadbandConstants.EPARCHY_CODE);
        bbArgs.put("eparchyCodes",eparchyCodes);
        bbArgs.put("orderColumn","SV.BARE_PRICE");
        bbArgs.put("orderType","ASC");
        bbArgs.put("goodsStatusId",4);
        bbArgs.put("chnlCode","E007");
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
        String s = JSONArray.toJSONString(goodsInfoList);
        logger.info("商品数量:"+goodsInfoList.size());
        List<BroadbandItemEntity> heBroadbandItemList = null;
        //根据价格排序
        try {
            heBroadbandItemList = BroadbandUtils.convertNewHeBroadbandItemEntityList(goodsInfoList);
            logger.info("转义数量:"+heBroadbandItemList.size());
            Collections.sort(heBroadbandItemList,new Comparator<BroadbandItemEntity>(){
                @Override
                public int compare(BroadbandItemEntity arg0, BroadbandItemEntity arg1) {
                    return arg0.getProductLevel().compareTo(arg1.getProductLevel());
                }
            });
        }  catch (Exception e) {
            logger.error("和家庭办理异常:"+e.getMessage());
            e.printStackTrace();
            throw e;
        }
        model.addAttribute("heBroadbandItemList",heBroadbandItemList);
        for(BroadbandItemEntity entity:heBroadbandItemList){
            if(jedisCluster.get(PRODUCT_DETAIL+installPhoneNum+entity.getProductId())==null){
                String productDetail = entity.getGoodsSkuId()+"|"+entity.getBroadbandItemName();
                jedisCluster.set(PRODUCT_DETAIL+installPhoneNum+entity.getProductId(),productDetail);
                jedisCluster.expire(PRODUCT_DETAIL+installPhoneNum+entity.getProductId(), 60  * 60);
            }
        }
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "goodsChooseInfo",request.getParameterMap(),"用户手机号:"+installPhoneNum+"商品列表信息:"+JSON.toJSONString(heBroadbandItemList),request);
        /**
         * 模拟参数
         */
        String  fee =queryFee(installPhoneNum);
        Long balance = Long.parseLong(fee)/100;



        //查询当前套餐

        model.addAttribute("hasPackage",hasPackage);
        model.addAttribute("balance",balance);
        model.addAttribute("packageName",packageName);
        model.addAttribute("curentPackage",curentPackage);
        return "web/broadband/newO2o/newHeFamily/o2oNewHeChoose";
    }

    @RequestMapping("newConfirmHeInstall")
    public String confirmHeInstall(HttpServletRequest request,Model model)throws Exception{
        String streamNo=createStreamNo();
            // 地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
        // 默认区县信息(长沙市)
        if (!CollectionUtils.isEmpty(cityList)) {
            // 地市信息处理 用于适配Boss接口查询条件
            for (ThirdLevelAddress city : cityList) {
                city.setOrgName(city.getOrgName().replace("市", ""));
                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                    city.setOrgName("湘西州");
                }
            }
        }
        model.addAttribute("cityList", cityList);
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
        model.addAttribute("form1_phoneNum", request.getParameter("installPhoneNum"));
        model.addAttribute("installName", request.getParameter("installName"));
        model.addAttribute("form1_price", request.getParameter("form1_price"));
        model.addAttribute("idCard", request.getParameter("idCard"));
        model.addAttribute("homeData", request.getParameter("form1_homedata"));
        model.addAttribute("homeVoice", request.getParameter("form1_homevoice"));
        model.addAttribute("giveData", request.getParameter("form1_giveData"));
        model.addAttribute("giveMbh", request.getParameter("form1_giveMbh"));
        model.addAttribute("busType", request.getParameter("busType"));
        model.addAttribute("reissueType", request.getParameter("reissueType"));
//        插码相关取值
        model.addAttribute("goodsId", request.getParameter("form1_goodsId"));
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "goodsInstallInfo",request.getParameterMap(),"用户手机号:"+request.getParameter("installPhoneNum")+"商品SKU:"+request.getParameter("form1_skuId"),request);
        return "web/broadband/newO2o/newHeFamily/o2oNewHeInstall";
    }

    /**
     * 订单确认
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("newConfirmHeInstallDetail")
    public String confirmOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
        String streamNo=createStreamNo();
        model.addAttribute("goodsSkuId", request.getParameter("form1_skuId"));
        model.addAttribute("goodsName", request.getParameter("form1_goodsName"));
        model.addAttribute("form1_price", request.getParameter("form1_price"));
        model.addAttribute("addressName", request.getParameter("form1_addressName"));//装机地址
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
        model.addAttribute("idCard", request.getParameter("idCard"));
        model.addAttribute("form1_giveMbh", request.getParameter("form1_giveMbh"));
        model.addAttribute("contactPhone", request.getParameter("contactPhone"));
        model.addAttribute("isBookInstall", request.getParameter("isBookInstall"));
        model.addAttribute("payMode", request.getParameter("payMode"));
        model.addAttribute("payModeName", request.getParameter("payModeName"));

//        插码相关参数传递form1_goodsId
        model.addAttribute("goodsId", request.getParameter("form1_goodsId"));

        String totalMoney = "";
        if("1".equals(request.getParameter("form1_hasBroadband"))){
            AcceptFeeCalculateCondition condition = new AcceptFeeCalculateCondition();
            MemberVo memberVo = UserUtils.getLoginUser(request);
            condition.setRouteEparchyCode(memberVo.getChannelInfo().getTradeEparchyCode());
            condition.setBusiType("0");
            condition.setStandardAddressId(request.getParameter("form1_houseCode"));
            condition.setModemSaleType("4");
            Map res = broadBandService.acceptFeeCalculate(condition);
            Object resultObject = null;
            if(res != null && "ok".equals(res.get("respDesc"))){
                JSONArray resultArray = JSONArray.parseArray(String.valueOf(res.get("result")));
                resultObject = resultArray.get(0);
                if (resultObject.equals(null)){
                    //返回的boss接口的json数据拿不到result内容的话
                    Map<String,Object> resultMap = new HashMap<String,Object>();
                    resultMap.put("respCode", "-1");
                    resultMap.put("respDesc", "接口返回异常");
                    model.addAttribute("resultMap",resultMap);
                    return "web/broadband/newO2o/newHeFamily/checkError";
                }
            }else{
                Map<String,Object> resultMap = new HashMap<String,Object>();
                resultMap.put("respCode", "-1");
                resultMap.put("respDesc", "接口返回异常");
                model.addAttribute("resultMap",resultMap);
                return "web/broadband/newO2o/newHeFamily/checkError";
            }

            JSONObject result = (JSONObject) resultObject;
            totalMoney = (String)result.get("totalMoney");
        }
        model.addAttribute("totalMoney", totalMoney);
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "goodsDetailInfo",request.getParameterMap(),"用户手机号:"+request.getParameter("form1_phoneNum")+"商品SKU:"+request.getParameter("form1_skuId"),request);
        return "web/broadband/newO2o/newHeFamily/o2oNewHeConfirmDetail";
    }

    /**
     * 提交订单保存临时表
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("newSubmitTempOrder")
    public Map<String,Object> submitInstallOrder(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception{
        String streamNo=createStreamNo();
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
        //联系人号码
        String contactPhone = request.getParameter("contactPhone");
        //装机人姓名
        String installName = request.getParameter("installName");
        //身份证号码
        String idCard = request.getParameter("idCard");
        //支付方式
        String payMode = request.getParameter("payMode");
        payMode=StringUtils.isEmpty(payMode)?"0":payMode;
        //payModeId 2:在线支付;1:现场支付
//        int payModeId = "在线支付".equals(payMode) ? 2 : 1;
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

        //新增宽带费用
        String totalMoney = request.getParameter("totalMoney");

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
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
        staffPwd =  URLEncoder.encode(staffPwd,"utf-8");
        //是否购买宽带光猫
        String chooseCat = request.getParameter("form1_chooseCat");

        //MODEM方式 3:客户自备;4:自动配发
        String modemSaleType = StringUtils.isEmpty(chooseCat) ? "3" : "4";
        //光猫TRADE_TYPE_CODE类型
        String tradeTypeCodeGm = "1002";
        //光猫费用
        Long gmPrice = 0L;
        //费用类型
        String gmPriceType = "400";

        //魔百和类型 0:芒果 1:未来
        String tvType = request.getParameter("form1_Mbh");
        //魔百和TRADE_TYPE_CODE类型
        String tradeTypeCodeMbh = "3709";
        //魔百和费用
        Long MbhPrice = 0L;
        //魔百和费用类型
        String MbhPriceType = "420";
        //机顶盒方式: 0 客户自备，1 自动配发
        String TVBOX_SALE_TYPE = StringUtils.isEmpty(tvType) ? "0" : "1";


        String cityPre = eparchyCode.substring(2);
        if (StringUtils.isNotEmpty(productId)) {
            productId = productId.replace("XX", cityPre);
        }
        if(StringUtils.isEmpty(skuId)){
            String productDetail = jedisCluster.get(PRODUCT_DETAIL+phoneNum+productId);
            String[] goodsDetail = productDetail.split("\\|");
            skuId = goodsDetail[0];
        }
		/*宽带商品信息*/
        Map<String,Object> map = Maps.newHashMap();
        map.put("goodsSkuId", skuId);
        map.put("containGoodsSkuIdInfo",true);
        map.put("containGoodsBusiParam",true);
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
        Map<String,Object> resMap = Maps.newHashMap();
        if(Collections3.isEmpty(goodsInfoList)){
            resMap.put("code", "1");
            resMap.put("message", "查询不到商品信息");
            writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                    "canNotFindGoods",request.getParameterMap(),"查询不到商品信息:"+skuId+"phone:"+phoneNum,request);
            return resMap;
        }
        if(goodsInfoList.size() > 1){
            resMap.put("code", "1");
            resMap.put("message", "商品数据错误,商品数据不唯一!");
            return resMap;
        }
        TfGoodsInfo tfGoodsInfo = goodsInfoList.get(0);
        if(Collections3.isEmpty(tfGoodsInfo.getTfGoodsSkuList())){
            resMap.put("code", "1");
            resMap.put("message", "商品SKU数据错误,商品SKU数据为空!");
            return resMap;
        }
        if(tfGoodsInfo.getTfGoodsSkuList().size() != 1){
            resMap.put("code", "1");
            resMap.put("message", "商品SKU数据错误,商品SKU数据不唯一!");
            return resMap;
        }
        //校验
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(phoneNum);
        resMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        //校验未通过
        if ("0".equals(resMap.get("respCode")) && resMap.get("result") != null) {
            if (!"731".equals(((Map) ((List) resMap.get("result")).get(0)).get("PROVINCE_CODE"))){
                resMap.put("code", "-1");
                resMap.put("message", "请输入湖南移动号码!");
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
        if (!"0".equals(resMap.get("respCode"))) {
            resMap.put("code",  resMap.get("respCode"));
            resMap.put("message", resMap.get("respDesc"));
            return resMap;
        }
        logger.info("checkResult:"+resMap);
        logger.info("goodsInfoList:"+ JSON.toJSONString(goodsInfoList));
        BroadbandItemEntity broadbandItem = null;
        broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
        logger.info("broadbandItem:"+ JSON.toJSONString(broadbandItem));
        //宽带安装费用              新增办理费用

        String totalMoney1 = "";
        if("1".equals(hasBroadband)){
            AcceptFeeCalculateCondition acceptFeeCalculatecondition = new AcceptFeeCalculateCondition();
            MemberVo memberVo = UserUtils.getLoginUser(request);
            acceptFeeCalculatecondition.setRouteEparchyCode(memberVo.getChannelInfo().getTradeEparchyCode());
            acceptFeeCalculatecondition.setBusiType("0");
            acceptFeeCalculatecondition.setStandardAddressId(houseCode);
            acceptFeeCalculatecondition.setModemSaleType("4");
            Map res = broadBandService.acceptFeeCalculate(acceptFeeCalculatecondition);
            Object resutlObject = null;
            if(res != null && "ok".equals(res.get("respDesc"))){
                JSONArray resultArray = JSONArray.parseArray(String.valueOf(res.get("result")));
                resutlObject = resultArray.get(0);
                if (resutlObject.equals(null)){
                    resMap.put("code", "-1");
                    resMap.put("message", "接口返回异常");
                    return resMap;
                }
            }else{
                resMap.put("code", "-1");
                resMap.put("message", "接口返回异常");
                return resMap;
            }

            JSONObject result = (JSONObject) resutlObject;
            totalMoney1 = (String)result.get("totalMoney");
        }
        if (!totalMoney1.equals(totalMoney)){
            //如果是不相等那么可以进行以下操作
            resMap.put("code", "-1");
            resMap.put("message", "费用异常");
            return resMap;
        }
        long goodsSkuPrice = 0L;
        if("1".equals(hasBroadband)){
            goodsSkuPrice = (long)(Double.parseDouble(AmountUtils.changeY2F(broadbandItem.getPrice()+""))+Double.parseDouble(totalMoney));
            //goodsSkuPrice = Long.parseLong(String.valueOf(Double.parseDouble(AmountUtils.changeY2F(broadbandItem.getPrice()+""))+Double.parseDouble(totalMoney)));
        }else{
            goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(broadbandItem.getPrice()+""));
        }

        if("4".equals(modemSaleType)&&broadbandItem.getModemPrice()!=null){//光猫自动派发
            gmPrice=broadbandItem.getModemPrice();
        }
        if("1".equals(TVBOX_SALE_TYPE)&&broadbandItem.getStbPrice()!=null){//机顶盒 自动派发
            MbhPrice=broadbandItem.getStbPrice();
        }
        int broadbandPrice = broadbandItem.getPrice().intValue();
		/*订单明细业务参数信息*/
//        O2oParamUtils o2oParamUtils = new O2oParamUtils();
        List<O2oOrderParamTemp> paramList = Lists.newArrayList();
        BroadbandHeRegCondition broadbandHeRegCondition =new BroadbandHeRegCondition();
        o2oParamUtils.removeParams();
        o2oParamUtils.addParams("SERIAL_NUMBER",phoneNum,"手机号码");
        o2oParamUtils.addParams("PRODUCT_ID",productId,"和家庭套餐产品id");
        o2oParamUtils.addParams("BROADBAND_PRICE",String.valueOf(broadbandPrice),"和家庭套餐产品id");
//        o2oParamUtils.addParams("X_TRADE_PAYMONEY", "0", "支付方式");
        o2oParamUtils.addParams("TRADE_DEPART_PASSWD", staffPwd, "渠道交易部门密码");
        broadbandHeRegCondition.setSerialNumber(phoneNum);
        broadbandHeRegCondition.setProductId(productId);
        long totalPrice=0;
        broadbandHeRegCondition.setTradeDepartPassword(staffPwd);
        String TRADE_TYPE_CODE=null;
        String pay=null;
        String FEE_TYPE_CODE=null;
        String FEE_MODE=null;
        if(!StringUtils.isNotEmpty(tvType)){
            hasMbh = "0";
        }
        //无宽带，无魔百和
        if("1".equals(hasBroadband) && "1".equals(hasMbh)){
            TRADE_TYPE_CODE=1012+","+tradeTypeCodeGm+","+tradeTypeCodeMbh+","+1002;
            pay=0+","+gmPrice+","+MbhPrice+","+goodsSkuPrice;
            FEE_TYPE_CODE=111+","+gmPriceType+","+MbhPriceType+","+410;
            FEE_MODE="2,0,0,2";

            o2oParamUtils.addParams("ADDR_ID",houseCode,"装机地址编码");
            o2oParamUtils.addParams("ADDR_NAME",installAddress,"宽带安装地址名称");
            o2oParamUtils.addParams("ADDR_DESC",installAddress,"宽带安装地址明细");
            o2oParamUtils.addParams("ACCESS_TYPE",accessType,"接入方式");
            o2oParamUtils.addParams("MAX_WIDTH",maxWidth,"最大带宽");
            o2oParamUtils.addParams("FREE_PORT_NUM",freePortNum,"空闲端口数");
            o2oParamUtils.addParams("MODEM_SALE_TYPE",modemSaleType,"MODEM方式");
            o2oParamUtils.addParams("TV_TYPE",tvType,"魔百和类型");
            o2oParamUtils.addParams("TVBOX_SALE_TYPE",TVBOX_SALE_TYPE,"机顶盒方式");
            o2oParamUtils.addParams("IS_EXISTS","0","是否存量宽带用户转入");
            o2oParamUtils.addParams("CONTACT",installName,"联系人");
            o2oParamUtils.addParams("CONTACT_PHONE",contactPhone,"联系电话");
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
            totalPrice=gmPrice+MbhPrice+goodsSkuPrice;
            //有宽带，无魔百和
        }else if("0".equals(hasBroadband) && "1".equals(hasMbh)){
            TRADE_TYPE_CODE=1012+","+tradeTypeCodeMbh;
            pay=0+","+MbhPrice;
            FEE_TYPE_CODE=111+","+MbhPriceType;
            FEE_MODE="2,0";
            o2oParamUtils.addParams("TV_TYPE",tvType,"魔百和类型");
            o2oParamUtils.addParams("TVBOX_SALE_TYPE",TVBOX_SALE_TYPE,"机顶盒方式");
            o2oParamUtils.addParams("IS_EXISTS","1","是否存量宽带用户转入");
            broadbandHeRegCondition.setTvType(tvType);
            broadbandHeRegCondition.setTvboxSaleType(TVBOX_SALE_TYPE);
            broadbandHeRegCondition.setIsExists("1");
            totalPrice=MbhPrice;
        }else if("0".equals(hasBroadband) && "0".equals(hasMbh)) {
            TRADE_TYPE_CODE="1012";
            pay=0+"";
            FEE_TYPE_CODE="111";
            FEE_MODE="2";

            o2oParamUtils.addParams("IS_EXISTS","1","是否存量宽带用户转入");
            broadbandHeRegCondition.setIsExists("1");
            totalPrice=0;
        //有宽带无魔百合
        }else if("1".equals(hasBroadband) && "0".equals(hasMbh)){
            TRADE_TYPE_CODE=1012+","+tradeTypeCodeGm+","+1002;
            pay=0+","+gmPrice+","+goodsSkuPrice;
            FEE_TYPE_CODE=111+","+gmPriceType+","+410;
            FEE_MODE="2,0,2";

            o2oParamUtils.addParams("ADDR_ID",houseCode,"装机地址编码");
            o2oParamUtils.addParams("ADDR_NAME",installAddress,"宽带安装地址名称");
            o2oParamUtils.addParams("ADDR_DESC",installAddress,"宽带安装地址明细");
            o2oParamUtils.addParams("ACCESS_TYPE",accessType,"接入方式");
            o2oParamUtils.addParams("MAX_WIDTH",maxWidth,"最大带宽");
            o2oParamUtils.addParams("FREE_PORT_NUM",freePortNum,"空闲端口数");
            o2oParamUtils.addParams("MODEM_SALE_TYPE",modemSaleType,"MODEM方式");
//            o2oParamUtils.addParams("TV_TYPE",tvType,"魔百和类型");
//            o2oParamUtils.addParams("TVBOX_SALE_TYPE",TVBOX_SALE_TYPE,"机顶盒方式");
            o2oParamUtils.addParams("IS_EXISTS","0","是否存量宽带用户转入");
            o2oParamUtils.addParams("CONTACT",installName,"联系人");
            o2oParamUtils.addParams("CONTACT_PHONE",contactPhone,"联系电话");
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
            totalPrice=gmPrice+MbhPrice+goodsSkuPrice;
            //有宽带，无魔百和
        }



        o2oParamUtils.addParams("TRADE_TYPE_CODE",TRADE_TYPE_CODE,"订单类型");
        o2oParamUtils.addParams("FEE",pay,"应缴");
        o2oParamUtils.addParams("PAY",pay,"实缴");
        o2oParamUtils.addParams("FEE_TYPE_CODE",FEE_TYPE_CODE,"费用类型");
        o2oParamUtils.addParams("FEE_MODE", FEE_MODE, "营业费");
        if("0".equals(isBookInstall)||StringUtils.isEmpty(isBookInstall)){
            o2oParamUtils.addParams("COMMUNITY_TYPE","","小区地址类型");
            o2oParamUtils.addParams("BOOK_INSTALL_DATE",null,"预约装机日期");
            o2oParamUtils.addParams("BOOK_INSTALL_TIME",null,"预约装机时间");
            broadbandHeRegCondition.setCommunityType("");
            broadbandHeRegCondition.setBookInstallDate(null);
            broadbandHeRegCondition.setBookInstallTime(null);
        }else{
            o2oParamUtils.addParams("COMMUNITY_TYPE",communityType,"小区地址类型");
            o2oParamUtils.addParams("BOOK_INSTALL_DATE",bookInstallDate,"预约装机日期");
            o2oParamUtils.addParams("BOOK_INSTALL_TIME",bookInstallTime,"预约装机时间");
            broadbandHeRegCondition.setCommunityType(communityType);
            broadbandHeRegCondition.setBookInstallDate(bookInstallDate);
            broadbandHeRegCondition.setBookInstallTime(bookInstallTime);
        }
        List<Map<String,Object>> feeList = new ArrayList<Map<String,Object>>();
        Map<String,Object> feeMap = new HashMap<String,Object>();
        feeMap.put("TRADE_TYPE_CODE", TRADE_TYPE_CODE);
        feeMap.put("FEE_TYPE_CODE", FEE_TYPE_CODE);
        feeMap.put("FEE_MODE", FEE_MODE);
        feeMap.put("FEE", pay);
        feeMap.put("PAY", pay);
        feeList.add(feeMap);
        broadbandHeRegCondition.setFeeList(o2oParamUtils.parseFeeList(feeList));
        if(Integer.valueOf(payMode).intValue()==OrderConstant.PAY_WAY_CONSUME_FIRST){
            List<Map<String,Object>> xTradeList = new ArrayList<Map<String,Object>>();
            Map<String,Object> xTradeMap = new HashMap<String,Object>();
            xTradeMap.put("PAY_MONEY_CODE", "7");
            xTradeMap.put("MONEY", totalPrice);
            xTradeList.add(xTradeMap);
            broadbandHeRegCondition.setxTradePayMoneny(xTradeList);//现金0 先装后付7 余额支付6
            o2oParamUtils.addParams("X_TRADE_PAYMONEY", "7,"+totalPrice, "支付方式");
        }
        // 添加渠道信息
        MemberVo memberVo = UserUtils.getLoginUser(request);
        if(memberVo.getChannelInfo()==null){
            resMap.put("code",  "1");
            resMap.put("message", "受理校验失败：【未配置渠道信息，请用店长账户配置渠道信息!】");
            return resMap;
        }
//        o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneNum);
        o2oParamUtils.addConditionChannel(broadbandHeRegCondition,memberVo.getChannelInfo(),phoneNum);
        //----增加受理中校验接口开始。。。
        broadbandHeRegCondition.setPreType("1");
        //增加受理中校验参数
//        preMap.put("PRE_TYPE","1");
        logger.info("broadbandHeRegCondition:"+ JSON.toJSONString(broadbandHeRegCondition));
        Map result = heFamilyService.broadbandHeReg(broadbandHeRegCondition);
        logger.info("和家庭宽带业务受理中校验接口返回： " + result);

        if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
            //throw new Exception(" 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            resMap.put("code", "1");
            resMap.put("message", " 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            return resMap;
        }
        //----增加受理中校验接口结束。。。

        String goodsFormat = broadbandItem.getBroadbandItemName() + "/" + broadbandItem.getTerm();

        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
//        orderTempInfo.setOrder_temp_id(Long.valueOf(orderTempService.getId()));
        orderTempInfo.setOrder_type_id(Long.valueOf(OrderConstant.TYPE_HE_BROADBAND));
        orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
        orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
        orderTempInfo.setContact_phone(phoneNum);
        orderTempInfo.setEparchy_code(eparchyCode);
        orderTempInfo.setCounty(request.getParameter("form1_county"));
        orderTempInfo.setGoods_id(broadbandItem.getGoodsId());
        orderTempInfo.setGoods_sku_id(broadbandItem.getGoodsSkuId());
        orderTempInfo.setGoods_name(goodsInfoList.get(0).getGoodsName());
        orderTempInfo.setGoods_sku_price(totalPrice);
        orderTempInfo.setShop_id(Long.valueOf(memberVo.getShopInfo().getShopId()));
        orderTempInfo.setShop_name(memberVo.getShopInfo().getShopName());
        orderTempInfo.setChannel_code("E050");
        orderTempInfo.setPay_mode_id(Integer.parseInt(payMode));
        orderTempInfo.setInsert_time(new Date());
        orderTempInfo.setGoods_pay_price(totalPrice);
        orderTempInfo.setGoods_format(goodsFormat);
        orderTempInfo.setRoot_cate_id(OrderConstant.CATE_BROADBAND);
        orderTempInfo.setShop_type_id(6L);
        orderTempInfo.setOrder_status(0L);
        if(orderTempInfo.getPay_mode_id().intValue()==2&&orderTempInfo.getGoods_pay_price().longValue()>0){
            memberVo.getChannelInfo().setInModeCode("P");
        }
        o2oParamUtils.addChannelInfo( memberVo.getChannelInfo(),phoneNum);

        try{
            Long orderTempId = orderTempService.insert(orderTempInfo);
            paramList = o2oParamUtils.getParamsList();
            for(O2oOrderParamTemp paramTemp:paramList){
                paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
            }
            orderParamTemService.batchInsert(paramList);
            orderTempInfo.setOrder_temp_id(orderTempId);
            resMap.put("orderSub", orderTempInfo);
            resMap.put("code", "0");
            resMap.put("message", "生成订单成功");

            String name = URLEncoder.encode("和家庭宽带安装办理","utf-8");
            name = URLEncoder.encode(name,"utf-8");
            String sucUrl ="/o2oBusiness/invokeSmsInterface?goodsName="+name+"&broadbandType=13&serialNumber="+phoneNum+"&orderId="+orderTempId;
            String contextPath = request.getContextPath();
            StringBuilder redirect = new StringBuilder();
            redirect.append(contextPath).append(sucUrl);
            response.sendRedirect(redirect.toString());
        }catch (Exception e){

            writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                    "submitTempOrder",request.getParameterMap(),"临时订单生产失败:"+processThrowableMessage(e),request);
            resMap.put("code", "1");
            resMap.put("message", "生成订单失败");
            e.printStackTrace();
        }


        return resMap;
    }


    @RequestMapping("queryCommunityType")
    @ResponseBody
    public Map<String,Object>  queryCommunityType(HttpServletRequest request,String houseCode) throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        QryAddressAttrCondition condition=new QryAddressAttrCondition();
        condition.setADDR_ID(houseCode);
        Map result = qryAddressService.queryAddressAttr(condition);
        if ("0".equals(result.get("respCode")) && result.get("result") != null) {
            JSONArray array = (JSONArray) result.get("result");
            JSONObject resultObj = array.getJSONObject(0);
            if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                resultMap.put("respCode", "0");
                resultMap.put("data",resultObj.get("COMMUNITY_TYPE"));
                // 城区
                if("02".equals(resultObj.getString("COMMUNITY_TYPE"))||"03".equals(resultObj.getString("COMMUNITY_TYPE"))){
                    // 乡镇
                }else{
                }
            }else{
                resultMap.put("respCode", "-1");
                resultMap.put("data", resultObj.getString("X_RESULTINFO"));
            }
        } else {
            throw new Exception("地址城乡类型查询失败!");
        }
        return resultMap;
    }
    public String queryFee(String SERIAL_NUMBER)throws Exception{
        HqQueryFeeCondition hqQueryFeeCondition = new HqQueryFeeCondition();
        hqQueryFeeCondition.setSERIAL_NUMBER(SERIAL_NUMBER);
        Map userFee = flowServeService.queryFee(hqQueryFeeCondition);
        logger.info("fee:"+JSON.toJSONString(userFee));
        if(!HNanConstant.SUCCESS.equals(userFee.get("respCode"))|| MapUtils.isEmpty(userFee)){
            throw new ServiceException(HNanConstant.FAIL, userFee.get("respDesc").toString(), null);
        }
       return String.valueOf(((Map)(net.sf.json.JSONArray.fromObject(userFee.get("result"))).get(0)).get("NEW_ALLLEAVE_FEE"));
    }
}
