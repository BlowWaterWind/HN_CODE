package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsBusiParam;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsSku;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderSubDetailBusiParam;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * Created by think on 2017/9/12.
 */
@Controller
@RequestMapping("o2oHeBand")
public class O2oHeBandController extends BaseController {
    @Autowired
    private IGoodsManageService goodsManageService;

    @Autowired
    private DictService dictService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    HeFamilyService heFamilyService;

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private IMemberAddressService memberAddressService;

    @Autowired
    private BroadBandService broadBandServiceImpl;

    @Autowired
    private O2oOrderTempService orderTempService;

    @Autowired
    private O2oOrderParamTemService orderParamTemService;

    @Autowired
    private QryAddressService qryAddressService;

    @Autowired
    private O2oParamUtils o2oParamUtils;

    private Logger logger = Logger.getLogger(O2oHeBandController.class);

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
    public String init(HttpServletRequest request , HttpServletResponse response, Model model)  throws Exception {
        //获取办理用户手机号码
        MemberVo memberVo = UserUtils.getLoginUser(request);
        ChannelInfo channelInfo = memberVo.getChannelInfo();
        ShopInfo shopInfo=memberVo.getShopInfo();
        model.addAttribute("staffId",channelInfo.getTradeStaffId());
        model.addAttribute("shopId",shopInfo.getShopId());
        return "web/broadband/o2o/heFamily/o2oHeInit";
    }
    @RequestMapping("heInstall")
    public String bandInstall(HttpServletRequest request,Model model) throws Exception {
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "bandInstall",request.getParameterMap(),"o2o和家庭宽带办理",request);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String hasAddress = request.getParameter("hasAddress");
        String installPhoneNum = request.getParameter("installPhoneNum");
        if(StringUtils.isBlank(installPhoneNum)){
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
            if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
                resultMap.put("respCode", "-1");
                resultMap.put("respDesc", "请输入湖南移动号码办理!");
                model.addAttribute("resultMap",resultMap);
                return "web/goods/broadband/newInstall/checkError";
            }
            model.addAttribute("eparchy_Code",EPARCHY_CODE);
//            model.addAttribute("eparchy_Code","0731");
        }catch (Exception e) {
            logger.error("手机号码归属地 :"+e.getMessage());
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"bandInstall",null,"o2o手机号码归属地:"+processThrowableMessage(e));
            model.addAttribute("resultMap",resultMap);
        }
        // 有宽带账号
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

        if("0".equals(resultMap.get("isBroadBand"))){
            try{
//                查询办理前宽带信息
                BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
                broadbandDetailInfoCondition.setSerialNumber(String.valueOf(installPhoneNum));
                BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);
                model.addAttribute("broadbandDetailInfoResult",broadbandDetailInfoResult);
            }catch (Exception e) {
                e.printStackTrace();
                writerFlowLogThrowable(streamNo,"","",getClass().getName(),"bandInstall",null,"o2o已有宽带用户信息查询:"+processThrowableMessage(e));
                logger.error("已有宽带用户信息查询："+e.getMessage());
            }
            if(!"Y".equalsIgnoreCase(hasAddress)){
                return"web/broadband/o2o/heFamily/o2oHasHeFamilyBroadband";
            }
        }else {
            // 地市信息
            List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
            // 默认区县信息(长沙市)
            List<ThirdLevelAddress> countyList = null;
            if (!CollectionUtils.isEmpty(cityList)) {
                countyList = memberAddressService.getChildrensByPId(cityList.get(0).getOrgId() + "");
                logger.info("地址编码:" + countyList);
                // 地市信息处理 用于适配Boss接口查询条件
                for (ThirdLevelAddress city : cityList) {
                    city.setOrgName(city.getOrgName().replace("市", ""));
                    if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                        city.setOrgName("湘西州");
                    }
                }
            }
            model.addAttribute("cityList", cityList);
            model.addAttribute("countyList", countyList);
        }
        //宽带新装信息   HE_FAMILY_BROADBAND_CATEGORY_ID 和家庭， BROADBAND_CATEGORY_ID_ADD 裸宽
        Map<String,Object> bbArgs = new HashMap<>();
        bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_HE_FAMILY_BROADBAND_CATEGORY_ID"));
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
            heBroadbandItemList = BroadbandUtils.convertHeBroadbandItemEntityList(goodsInfoList);
        }  catch (Exception e) {
            logger.error("和家庭办理异常:"+e.getMessage());
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"bandInstall",null,"o2o和家庭办理异常:"+processThrowableMessage(e));
            e.printStackTrace();
            throw e;
        }
        model.addAttribute("heBroadbandItemList",heBroadbandItemList);

        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "bandInstall",null,objectToMap(heBroadbandItemList),"o2o和家庭宽带办理");
        return "web/broadband/o2o/heFamily/o2oNewHeInstall";
    }
    /**
     * 订单确认
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("confirmHeOrder")
    public String confirmOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "confirmOrder",request.getParameterMap(),"和家庭宽带订单确认",request);
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
        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "confirmOrder",null,null,"和家庭宽带订单确认");
        return "web/broadband/o2o/heFamily/o2oHeConfirmDetail";
    }

    /**
     * 订单确认提交
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("confirmHeInstall")
    public String confirmHeInstall(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "confirmHeInstall",request.getParameterMap(),"和家庭宽带订单确认提交",request);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        try{
            String skuId = request.getParameter("form1_skuId");
            String houseCode = request.getParameter("form1_houseCode");
            String addressName = request.getParameter("form1_addressName");
            String coverType = request.getParameter("form1_coverType");
            String maxWidth = request.getParameter("form1_maxWidth");
            String freePort = request.getParameter("form1_freePort");
            String chooseCat = request.getParameter("form1_chooseCat");
            String chooseBandWidth = request.getParameter("form1_chooseBandWidth");
            String Mbh = request.getParameter("form1_Mbh");
            String eparchyCode = request.getParameter("form1_eparchyCode");
            String county = request.getParameter("form1_county");
            //memberRecipientCounty
            String productId = request.getParameter("submit_productId");
            String packageId = request.getParameter("submit_packageId");
            String discntCode = request.getParameter("submit_discntCode");
            //0为有,1为无
            String hasBroadband = request.getParameter("form1_hasBroadband");
            String hasMbh = request.getParameter("form1_hasMbh");
            String custName = request.getParameter("form1_custName");
            String psptId = request.getParameter("form1_psptId");


            //获取SKU信息
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("goodsSkuId", skuId);
            map.put("containGoodsSkuIdInfo",true);
            map.put("containGoodsBusiParam",true);
            List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
            if(Collections3.isEmpty(goodsInfoList)){
                throw new Exception("无法查询到商品信息");
            }
            if(goodsInfoList.size() > 1){
                throw new Exception("商品数据错误,商品数据不唯一!");
            }
            TfGoodsInfo tfGoodsInfo = goodsInfoList.get(0);
            if(Collections3.isEmpty(tfGoodsInfo.getTfGoodsSkuList())){
                throw new Exception("商品SKU数据错误,商品SKU数据为空!");
            }
            if(tfGoodsInfo.getTfGoodsSkuList().size() != 1){
                throw new Exception("商品SKU数据错误,商品SKU数据不唯一!");
            }
            BroadbandItemEntity broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));


            String installPhoneNum = request.getParameter("installPhoneNum");
            if(StringUtils.isBlank(installPhoneNum)){
                throw new Exception("请输入手机号!");
            }else{
                model.addAttribute("installPhoneNum", installPhoneNum);
            }
            model.addAttribute("broadbandItem",broadbandItem);
            model.addAttribute("houseCode",houseCode);
            model.addAttribute("addressName",addressName);
            model.addAttribute("coverType",coverType);
            model.addAttribute("maxWidth",maxWidth);
            model.addAttribute("freePort",freePort);
            model.addAttribute("chooseCat",chooseCat);
            model.addAttribute("chooseBandWidth",chooseBandWidth);
            model.addAttribute("Mbh",Mbh);
            model.addAttribute("eparchyCode",eparchyCode);
            model.addAttribute("county",county);
            model.addAttribute("productId",productId);
            model.addAttribute("packageId",packageId);
            model.addAttribute("discntCode",discntCode);
            model.addAttribute("hasBroadband",hasBroadband);
            model.addAttribute("hasMbh",hasMbh);
            model.addAttribute("custName",custName);
            model.addAttribute("psptId",psptId);

            // 判断地址的城乡类型
            QryAddressAttrCondition condition=new QryAddressAttrCondition();
            condition.setADDR_ID(houseCode);
            Map result = qryAddressService.queryAddressAttr(condition);
            List<String> bookInstallDateList = new ArrayList<>();
            if ("0".equals(result.get("respCode")) && result.get("result") != null) {
                JSONArray array = (JSONArray) result.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                    resultMap.put("respCode", "0");
                    // 城区
                    if("02".equals(resultObj.getString("COMMUNITY_TYPE"))||"03".equals(resultObj.getString("COMMUNITY_TYPE"))){
                        bookInstallDateList.add(DateUtils.getDateAfterDays(3));//未来1天
                        bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来2天
                        bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来3天
                    // 乡镇
                    }else{
                        bookInstallDateList.add(DateUtils.getDateAfterDays(4));//未来1天
                        bookInstallDateList.add(DateUtils.getDateAfterDays(5));//未来2天
                        bookInstallDateList.add(DateUtils.getDateAfterDays(6));//未来3天
                    }
                    model.addAttribute("bookInstallDateList",bookInstallDateList);
                    model.addAttribute("COMMUNITY_TYPE",resultObj.get("COMMUNITY_TYPE"));
                }else{
                    throw new Exception(resultObj.getString("X_RESULTINFO"));
                }
            } else {
                throw new Exception("地址城乡类型查询失败!");
            }
        } catch(Exception e){
            logger.error("和家庭办理异常:"+e.getMessage());
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"confirmHeInstall",null,"和家庭办理异常:"+processThrowableMessage(e));
            throw e;
        }
        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "confirmOrder",null,null,"和家庭宽带订单确认提交");
        return "web/broadband/o2o/heFamily/o2oConfirmHeInstall";
    }

    /**
     * 检查手机号是否能办理宽带
     * @param phoneNum
     * @return
     */
    @RequestMapping("checkPhone")
    @ResponseBody
    public Map checkPhone(String phoneNum){
        Map result = new HashMap();
        try {
            BroadbandDetailInfoCondition condition1 = new BroadbandDetailInfoCondition();
            condition1.setSerialNumber(phoneNum);
            BroadbandDetailInfoResult data  = broadBandService.broadbandDetailInfo(condition1);
            if(data.getBroadbandDetailInfo()!=null){
                result.put("flag","success");
                result.put("data",data);
            }else{
                result.put("flag","false");
                result.put("data",null);
            }
        }catch (Exception e){
            result.put("flag","false");
            result.put("data",null);
            e.printStackTrace();
        }
        return result;
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
    @RequestMapping("submitTempOrder")
    public Map<String,Object> submitInstallOrder(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception{
        String skuId = request.getParameter("form1_skuId");
        //年限
        String term = request.getParameter("form1_term");
        //带宽
        String bandWidth = request.getParameter("form1_bandWidth");
        //标准地址编码
        String houseCode = request.getParameter("form1_houseCode");
        //安装地址
        String installAddress = request.getParameter("form1_addressName");
        //接入方式
        String accessType = request.getParameter("form1_coverType");
        //手机
        String phoneNum = request.getParameter("form1_phoneNum");
        //装机人姓名
        String installName = request.getParameter("installName");
        //身份证号码
        String idCard = request.getParameter("idCard");
        //支付方式
        String payMode = request.getParameter("payMode");
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

        //是否购买宽带光猫
        String chooseCat = request.getParameter("form1_chooseCat");
        //MODEM方式 3:客户自备;4:自动配发
        String modemSaleType = StringUtils.isEmpty(chooseCat) ? "3" : "4";
        //光猫TRADE_TYPE_CODE类型
        String tradeTypeCodeGm = "1002";
        //光猫费用
        String gmPrice = "0";
        //费用类型
        String gmPriceType = "400";

        //魔百和类型 0:芒果 1:未来
        String tvType = request.getParameter("form1_Mbh");
        //魔百和TRADE_TYPE_CODE类型
        String tradeTypeCodeMbh = "3709";
        //魔百和费用
        String MbhPrice = "0";
        //魔百和费用类型
        String MbhPriceType = "420";
        //机顶盒方式: 0 客户自备，1 自动配发
        String TVBOX_SALE_TYPE = StringUtils.isEmpty(tvType) ? "0" : "1";

        long Price = 0;

        String cityPre = eparchyCode.substring(2);
        if (StringUtils.isNotEmpty(productId)) {
            productId = productId.replace("XX", cityPre);
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
        if (!"731".equals(((Map) ((List) resMap.get("result")).get(0)).get("PROVINCE_CODE"))){
            resMap.put("code", "-1");
            resMap.put("message", "请输入湖南移动号码!");
            return resMap;
        }
//        if("0".equals(hasBroadband) && "0".equals(hasMbh)){
//            resMap.put("code", "2");
//            resMap.put("message", "宽带升档成功");
//            return resMap;
//        }
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
        long goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(Price+""));
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
//        broadbandHeRegCondition.setxTradePayMoneny("0");
        broadbandHeRegCondition.setTradeDepartPassword(staffPwd);
        String TRADE_TYPE_CODE=null;
        String pay=null;
        String FEE_TYPE_CODE=null;
        String FEE_MODE=null;
        //无宽带，无魔百和
        if("1".equals(hasBroadband) && "1".equals(hasMbh)){
            TRADE_TYPE_CODE=1012+","+tradeTypeCodeGm+","+tradeTypeCodeMbh;
            pay=goodsSkuPrice+","+gmPrice+","+MbhPrice;
            FEE_TYPE_CODE=111+","+gmPriceType+","+MbhPriceType;
            FEE_MODE="2,0,0";

            o2oParamUtils.addParams("ADDR_ID",houseCode,"装机地址编码");
            o2oParamUtils.addParams("ADDR_NAME",installAddress,"宽带安装地址名称");
            o2oParamUtils.addParams("ACCESS_TYPE",accessType,"接入方式");
            o2oParamUtils.addParams("MAX_WIDTH",maxWidth,"最大带宽");
            o2oParamUtils.addParams("FREE_PORT_NUM",freePortNum,"空闲端口数");
            o2oParamUtils.addParams("MODEM_SALE_TYPE",modemSaleType,"MODEM方式");
            o2oParamUtils.addParams("TV_TYPE",tvType,"魔百和类型");
            o2oParamUtils.addParams("TVBOX_SALE_TYPE",TVBOX_SALE_TYPE,"机顶盒方式");
            o2oParamUtils.addParams("IS_EXISTS","0","是否存量宽带用户转入");
            o2oParamUtils.addParams("CONTACT",installName,"联系人");
            o2oParamUtils.addParams("CONTACT_PHONE",phoneNum,"联系电话");
            broadbandHeRegCondition.setAddrId(houseCode);
            broadbandHeRegCondition.setAddrName(installAddress);
            broadbandHeRegCondition.setAccessType(accessType);
            broadbandHeRegCondition.setMaxWidth(maxWidth);
            broadbandHeRegCondition.setFreePortNum(freePortNum);
            broadbandHeRegCondition.setModemSaleType(modemSaleType);
            broadbandHeRegCondition.setTvType(tvType);
            broadbandHeRegCondition.setTvboxSaleType(TVBOX_SALE_TYPE);
            broadbandHeRegCondition.setIsExists("0");
            broadbandHeRegCondition.setContact(installName);
            broadbandHeRegCondition.setContactPhone(phoneNum);

        //有宽带，无魔百和
        }else if("0".equals(hasBroadband) && "1".equals(hasMbh)){
            TRADE_TYPE_CODE=1012+","+tradeTypeCodeMbh;
            pay=goodsSkuPrice+","+MbhPrice;
            FEE_TYPE_CODE=111+","+MbhPriceType;
            FEE_MODE="2,0";
            o2oParamUtils.addParams("TV_TYPE",tvType,"魔百和类型");
            o2oParamUtils.addParams("TVBOX_SALE_TYPE",TVBOX_SALE_TYPE,"机顶盒方式");
            o2oParamUtils.addParams("IS_EXISTS","1","是否存量宽带用户转入");
            broadbandHeRegCondition.setTvType(tvType);
            broadbandHeRegCondition.setTvboxSaleType(TVBOX_SALE_TYPE);
            broadbandHeRegCondition.setIsExists("1");

        }else if("0".equals(hasBroadband) && "0".equals(hasMbh)) {
            TRADE_TYPE_CODE="1012";
            pay=String.valueOf(goodsSkuPrice);
            FEE_TYPE_CODE="111";
            FEE_MODE="2";

            o2oParamUtils.addParams("IS_EXISTS","1","是否存量宽带用户转入");
            broadbandHeRegCondition.setIsExists("1");

        }



        o2oParamUtils.addParams("TRADE_TYPE_CODE",TRADE_TYPE_CODE,"订单类型");
        o2oParamUtils.addParams("FEE",pay,"应缴");
        o2oParamUtils.addParams("PAY",pay,"实缴");
        o2oParamUtils.addParams("FEE_TYPE_CODE",FEE_TYPE_CODE,"费用类型");
        o2oParamUtils.addParams("FEE_MODE", FEE_MODE, "营业费");
        if("0".equals(isBookInstall)){
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
        broadbandHeRegCondition.setFeeList(feeList);
        // 添加渠道信息
        MemberVo memberVo = UserUtils.getLoginUser(request);
        if(memberVo.getChannelInfo()==null){
            resMap.put("code",  "1");
            resMap.put("message", "受理校验失败：【未配置渠道信息，请用店长账户配置渠道信息!】");
            return resMap;
        }
        o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneNum);
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
        orderTempInfo.setGoods_sku_price(goodsSkuPrice);
        orderTempInfo.setShop_id(Long.valueOf(memberVo.getShopInfo().getShopId()));
        orderTempInfo.setShop_name(memberVo.getShopInfo().getShopName());
        orderTempInfo.setChannel_code("E050");
        orderTempInfo.setPay_mode_id(2);
        orderTempInfo.setInsert_time(new Date());
        orderTempInfo.setGoods_pay_price(goodsSkuPrice);
        orderTempInfo.setGoods_format(goodsFormat);
        orderTempInfo.setRoot_cate_id(OrderConstant.CATE_BROADBAND);
        orderTempInfo.setShop_type_id(6L);
        orderTempInfo.setOrder_status(0L);
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
        }catch (Exception e){
            resMap.put("code", "1");
            resMap.put("message", "生成订单失败");
        }


        return resMap;
    }

    @RequestMapping("tempResult")
    public String tempResult(Model model){
        return "web/broadband/o2o/heFamily/tempResult";
    }

    private Date getCurrentDate(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        Date currentTime_2 = null;
        try {
            currentTime_2 = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTime_2;
    }
}
