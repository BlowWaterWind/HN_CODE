package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.BusiLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.HeFamilyService;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsCommService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IUserBroadbrandService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.merchant.entity.CompanyAcctInfo;
import com.ai.ecs.merchant.entity.CompanyInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.service.IPayBankService;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.common.CommonParams.PAY_CHARSET;
import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;


/**
 * Created by admin on 2016/12/12.
 */
@Controller
@RequestMapping("BroadbandTrade")
public class BroadbandTradeController extends BaseController {
    @Autowired
    private IMemberAddressService memberAddressService;

    @Autowired
    private IGoodsCommService goodsCommService;

    @Autowired
    private IGoodsManageService goodsManageService;

    @Autowired
    private DictService dictService;

    @Autowired
    private ICompanyAcctService companyAcctService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IPayBankService payBankService;

    @Autowired
    private IUserBroadbrandService userBroadbrandService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    HeFamilyService heFamilyService;

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private IUppHtmlValidataService validataService;

    @Autowired
    private QryAddressService qryAddressService;

    @Value("${afterOrderPayUrl}")
    String afterOrderPayUrl;

    private Logger logger = Logger.getLogger(BroadbandTradeController.class);

    /**
     * 和家庭新装
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("heBroadbandInstall")
    public String heBroadbandInstall(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "heBroadbandInstall",request.getParameterMap(),"和家庭宽带下单",request);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String hasAddress = request.getParameter("hasAddress");
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        String installPhoneNum ="";
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            installPhoneNum = memberVo.getMemberLogin().getMemberLogingName();
        }catch(Exception e){
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"heBroadbandInstall",null,"和家庭宽带下单异常:"+processThrowableMessage(e));
            logger.error(e,e);
        }
        if (StringUtils.isBlank(installPhoneNum)) {
            model.addAttribute("installPhoneNum", "");
        } else {
            model.addAttribute("installPhoneNum", installPhoneNum);
        }
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
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"heBroadbandInstall",null,"和家庭宽带下单异常:"+processThrowableMessage(e));
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
                logger.error("已有宽带用户信息查询："+e.getMessage());
            }
            if(!"Y".equalsIgnoreCase(hasAddress)){
                return"web/goods/broadband/newInstall/hasHeFamilyBroadband";
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
        bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","HE_FAMILY_BROADBAND_CATEGORY_ID"));
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
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"heBroadbandInstall",null,"和家庭宽带下单异常:"+processThrowableMessage(e));
            e.printStackTrace();
            throw e;
        }
        model.addAttribute("heBroadbandItemList",heBroadbandItemList);
        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "heBroadbandInstall",null,null,"和家庭宽带下单");
        return "web/goods/broadband/newInstall/newHeInstall";
    }

    @RequestMapping("confirmHeInstall")
    public String confirmHeInstall(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "confirmHeInstall",request.getParameterMap(),"和家庭宽带确认下单",request);
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


            String installPhoneNum ="";
            try {
                MemberVo memberVo = UserUtils.getLoginUser(request);
                installPhoneNum = memberVo.getMemberLogin().getMemberLogingName();
            }catch(Exception e){
                e.printStackTrace();
                writerFlowLogThrowable(streamNo,"","",getClass().getName(),"confirmHeInstall",null,"和家庭宽带确认下单异常:"+processThrowableMessage(e));
                logger.error(e,e);
            }
            if (installPhoneNum == null || installPhoneNum == "") {
                model.addAttribute("installPhoneNum", "");
            } else {
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

            List<String> bookInstallDateList = new ArrayList<>();
            //预约装机日期（城市在预约办理48小时后，农村在预约办理72小时后）
            QryAddressAttrCondition condition = new QryAddressAttrCondition();
            condition.setADDR_ID(houseCode);
            Map<String, Object> resMap = new HashMap<>();
            try {
                resMap = qryAddressService.queryAddressAttr(condition);
                if(resMap.get("respCode").equals("0")){
                    JSONArray result = (JSONArray) resMap.get("result");
                    Map resMap1 = (Map) result.get(0) ;
                    if("01".equals(resMap1.get("COMMUNITY_TYPE"))){
                        bookInstallDateList.add(DateUtils.getDateAfterDays(3));
                        bookInstallDateList.add(DateUtils.getDateAfterDays(4));
                        bookInstallDateList.add(DateUtils.getDateAfterDays(5));
                        bookInstallDateList.add(DateUtils.getDateAfterDays(6));

                    } else {
                        bookInstallDateList.add(DateUtils.getDateAfterDays(2));
                        bookInstallDateList.add(DateUtils.getDateAfterDays(3));
                        bookInstallDateList.add(DateUtils.getDateAfterDays(4));
                        bookInstallDateList.add(DateUtils.getDateAfterDays(5));
                    }
                    model.addAttribute("communityType",resMap1.get("COMMUNITY_TYPE"));
                } else {
                    logger.error("查询地址类型失败！");
                }
            }catch (Exception e){
                logger.error("查询地址类型异常:"+e.getMessage());
                writerFlowLogThrowable(streamNo,"","",getClass().getName(),"confirmHeInstall",null,"查询地址类型异常:"+processThrowableMessage(e));
                e.printStackTrace();
            }

            model.addAttribute("bookInstallDateList",bookInstallDateList);
        } catch(Exception e){
            logger.error("和家庭办理异常:"+e.getMessage());
            e.printStackTrace();
            writerFlowLogThrowable(streamNo,"","",getClass().getName(),"confirmHeInstall",null,"和家庭宽带确认下单异常:"+processThrowableMessage(e));
            throw e;
        }
        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "confirmHeInstall",null,null,"和家庭宽带确认下单");
        return "web/goods/broadband/newInstall/confirmHeInstall";
    }

    /**
     * 和家庭立即办理提交订单
     *
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("submitInstallOrder")
    @ResponseBody
    public Map<String,Object> submitInstallOrder(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception{
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "submitInstallOrder",request.getParameterMap(),"和家庭宽带提交订单",request);
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
        //支付名称
        String payModeName = request.getParameter("form1_payMode");
        //支付方式
//        String payMode =  request.getParameter("payMode");
        //payModeId 2:在线支付;1:现场支付
        int payModeId = "在线支付".equals(payModeName) ? 2 : 1;
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
        String bookInstallDate = null;
        // 预约装机时间
        String bookInstallTime = null;
        // 小区地址类型
        String communityType = request.getParameter("form1_communityType");
        // 预约装机
        if("1".equals(isBookInstall)){

            String bookInstallDateTemp = request.getParameter("bookInstallDate");//预约安装日期
            String bookInstallTimeTemp = request.getParameter("bookInstallTime");//预约安装日期段
            switch (bookInstallTimeTemp){
                case "上午08:00-12:00":
                    bookInstallTime = "0";
                    break;
                case "中午12:00-14:00":
                    bookInstallTime = "1";
                    break;
                case "下午14:00-18:00":
                    bookInstallTime = "2";
                    break;
                case "晚上18:00-20:00":
                    bookInstallTime = "3";
                    break;
            }
            // 将YYYY/MM/DD 格式 转为YYYY-MM-DD格式
            bookInstallDate = bookInstallDateTemp.replaceAll("/","-");
        }

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


        //宽带商品信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodsSkuId", skuId);
        map.put("containGoodsSkuIdInfo", true);
        map.put("containGoodsBusiParam", true);
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
        Map<String,Object> resMap = Maps.newHashMap();
        model.addAttribute("resMap",resMap);
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
        BroadbandItemEntity broadbandItem = null;
        broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
        long goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(Price+""));
        int broadbandPrice = broadbandItem.getPrice().intValue();
	        /*业务参数*/
        List<TfOrderSubDetailBusiParam> busiParamList = Lists.newArrayList();
        TfOrderSubDetailBusiParam tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("SERIAL_NUMBER");
        tempParam.setSkuBusiParamValue(phoneNum);
        tempParam.setSkuBusiParamDesc("手机号码");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("PRODUCT_ID");
        tempParam.setSkuBusiParamValue(productId);
        tempParam.setSkuBusiParamDesc("和家庭套餐产品id");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("BROADBAND_PRICE");
        tempParam.setSkuBusiParamValue(String.valueOf(broadbandPrice));
        tempParam.setSkuBusiParamDesc("和家庭套餐产品id");
        busiParamList.add(tempParam);

        String TRADE_TYPE_CODE=null;
        String pay=null;
        String FEE_TYPE_CODE=null;
        String FEE_MODE=null;
        if("1".equals(hasBroadband) && "1".equals(hasMbh)){//无宽带，无魔百和
            TRADE_TYPE_CODE=1012+","+tradeTypeCodeGm+","+tradeTypeCodeMbh;
            pay=goodsSkuPrice+","+gmPrice+","+MbhPrice;
            FEE_TYPE_CODE=111+","+gmPriceType+","+MbhPriceType;
            FEE_MODE="2,0,0";

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("ADDR_ID");
            tempParam.setSkuBusiParamValue(houseCode);
            tempParam.setSkuBusiParamDesc("装机地址编码");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("ADDR_NAME");
            tempParam.setSkuBusiParamValue(installAddress);
            tempParam.setSkuBusiParamDesc("宽带安装地址名称");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("ADDR_DESC");
            tempParam.setSkuBusiParamValue(installAddress);
            tempParam.setSkuBusiParamDesc("装机详细地址");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("ACCESS_TYPE");
            tempParam.setSkuBusiParamValue(accessType);
            tempParam.setSkuBusiParamDesc("接入方式");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("MAX_WIDTH");
            tempParam.setSkuBusiParamValue(maxWidth);
            tempParam.setSkuBusiParamDesc("最大带宽");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("FREE_PORT_NUM");
            tempParam.setSkuBusiParamValue(freePortNum);
            tempParam.setSkuBusiParamDesc("空闲端口数");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("MODEM_SALE_TYPE");
            tempParam.setSkuBusiParamValue(modemSaleType);
            tempParam.setSkuBusiParamDesc("MODEM方式");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("TV_TYPE");
            tempParam.setSkuBusiParamValue(tvType);
            tempParam.setSkuBusiParamDesc("魔百和类型");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("TVBOX_SALE_TYPE");
            tempParam.setSkuBusiParamValue(TVBOX_SALE_TYPE);
            tempParam.setSkuBusiParamDesc("机顶盒方式");
            busiParamList.add(tempParam);

            //是否存量宽带用户转入  0 否 1 是
            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("IS_EXISTS");
            tempParam.setSkuBusiParamValue("0");
            tempParam.setSkuBusiParamDesc("是否存量宽带用户转入");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("CONTACT");
            tempParam.setSkuBusiParamValue(installName);
            tempParam.setSkuBusiParamDesc("联系人");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("CONTACT_PHONE");
            tempParam.setSkuBusiParamValue(phoneNum);
            tempParam.setSkuBusiParamDesc("联系电话");
            busiParamList.add(tempParam);

        }else if("0".equals(hasBroadband) && "1".equals(hasMbh)){//有宽带，无魔百和
            TRADE_TYPE_CODE=1012+","+tradeTypeCodeMbh;
            pay=goodsSkuPrice+","+MbhPrice;
            FEE_TYPE_CODE=111+","+MbhPriceType;
            FEE_MODE="2,0";

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("TV_TYPE");
            tempParam.setSkuBusiParamValue(tvType);
            tempParam.setSkuBusiParamDesc("魔百和类型");
            busiParamList.add(tempParam);

            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("TVBOX_SALE_TYPE");
            tempParam.setSkuBusiParamValue(TVBOX_SALE_TYPE);
            tempParam.setSkuBusiParamDesc("机顶盒方式");
            busiParamList.add(tempParam);

            //是否存量宽带用户转入  0 否 1 是
            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("IS_EXISTS");
            tempParam.setSkuBusiParamValue("1");
            tempParam.setSkuBusiParamDesc("是否存量宽带用户转入");
            busiParamList.add(tempParam);

        }else if("0".equals(hasBroadband) && "0".equals(hasMbh)) {
            TRADE_TYPE_CODE="1012";
            pay=String.valueOf(goodsSkuPrice);
            FEE_TYPE_CODE="111";
            FEE_MODE="2";

            //是否存量宽带用户转入  0 否 1 是
            tempParam = new TfOrderSubDetailBusiParam();
            tempParam.setSkuBusiParamName("IS_EXISTS");
            tempParam.setSkuBusiParamValue("1");
            tempParam.setSkuBusiParamDesc("是否存量宽带用户转入");
            busiParamList.add(tempParam);
        }

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("TRADE_TYPE_CODE");
        tempParam.setSkuBusiParamValue(TRADE_TYPE_CODE);
        tempParam.setSkuBusiParamDesc("订单类型");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("FEE");
        tempParam.setSkuBusiParamValue(pay);
        tempParam.setSkuBusiParamDesc("应缴");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("PAY");
        tempParam.setSkuBusiParamValue(pay);
        tempParam.setSkuBusiParamDesc("实缴");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("FEE_MODE");
        tempParam.setSkuBusiParamValue(FEE_MODE);
        tempParam.setSkuBusiParamDesc("营业费");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("FEE_TYPE_CODE");
        tempParam.setSkuBusiParamValue(FEE_TYPE_CODE);
        tempParam.setSkuBusiParamDesc("费用类型");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("COMMUNITY_TYPE");
        tempParam.setSkuBusiParamValue(communityType);
        tempParam.setSkuBusiParamDesc("小区地址类型");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("BOOK_INSTALL_DATE");
        tempParam.setSkuBusiParamValue(bookInstallDate);
        tempParam.setSkuBusiParamDesc("预约装机日期");
        busiParamList.add(tempParam);

        tempParam = new TfOrderSubDetailBusiParam();
        tempParam.setSkuBusiParamName("BOOK_INSTALL_TIME");
        tempParam.setSkuBusiParamValue(bookInstallTime);
        tempParam.setSkuBusiParamDesc("预约装机时间");
        busiParamList.add(tempParam);

//        tempParam = new TfOrderSubDetailBusiParam();
//        tempParam.setSkuBusiParamName("X_TRADE_PAYMONEY");
//        tempParam.setSkuBusiParamValue(payMode);
//        tempParam.setSkuBusiParamDesc("支付方式");
//        busiParamList.add(tempParam);


        //----增加受理中校验接口开始。。。
        Map<String,Object> preMap = new HashMap<>();
        for(int i  = 0; i < busiParamList.size(); i ++){
            tempParam = busiParamList.get(i);
            preMap.put(tempParam.getSkuBusiParamName(),tempParam.getSkuBusiParamValue());
        }

        //增加受理中校验参数
        preMap.put("PRE_TYPE","1");
        Map result = heFamilyService.broadbandHeRegByMap(preMap,new BroadbandHeRegCondition());
        logger.info("和家庭宽带业务受理中校验接口返回： " + result);

        if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
            //throw new Exception(" 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            resMap.put("code", "1");
            resMap.put("message", " 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            return resMap;
        }
        //----增加受理中校验接口结束。。。

        String goodsFormat = broadbandItem.getBroadbandItemName() + "/" + broadbandItem.getTerm();
        TfOrderSub orderSub = new TfOrderSub();
    	/*订单会员关联*/
        MemberVo memberVo = UserUtils.getLoginUser(request);
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
        orderSubDetail.setOrderSubDetailBusiParams(busiParamList);
        orderSub.addOrderDetail(orderSubDetail);
          /*设置订单信息*/
        //店铺信息
        orderSub.setShopId(BroadbandConstants.SHOP_ID);
        orderSub.setShopName(BroadbandConstants.SHOP_NAME);
        orderSub.setOrderTypeId(OrderConstant.TYPE_HE_BROADBAND);
        orderSub.setOrderSubAmount(goodsSkuPrice);    //子订单总额
        orderSub.setOrderSubPayAmount(goodsSkuPrice);//支付金额
        orderSub.setOrderSubDiscountAmount(0L);//优惠金额
        orderSub.setOrderChannelCode("E007");//渠道编码，网上商城
        orderSub.setPayModeId(payModeId);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
        orderSub.setDeliveryModeId(3);//配送方式，虚拟商品
        orderSub.setIsInvoicing(0);//是否开发票:0-不开

        logger.info("和家庭立即办理,提交订单参数orderSub:" + JSONArray.toJSONString(orderSub));
        List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);

        model.addAttribute("orderSub",orderSubList.get(0));
        model.addAttribute("orderSubDetail",orderSubList.get(0).getDetailList().get(0));
        resMap.put("code", "0");
        resMap.put("message", "生成订单成功");
        resMap.put("orderSub", orderSubList.get(0));
        resMap.put("orderSubDetail",orderSubList.get(0).getDetailList().get(0));
        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "submitInstallOrder",objectToMap(orderSub),objectToMap(orderSubList),"和家庭宽带订单生成");
        return resMap;
    }



    @RequestMapping("orderResult")
    public String orderResult(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
        String form1_orderNo =request.getParameter("form1_orderNo");
        String form1_goodsName =request.getParameter("form1_goodsName");
        String form1_installAddress =request.getParameter("form1_installAddress");
        model.addAttribute("orderNo",form1_orderNo);
        model.addAttribute("goodsName",form1_goodsName);
        model.addAttribute("installAddress",form1_installAddress);

        return "web/goods/broadband/newInstall/orderResult";
    }
    /**
     * 和家庭办理校验
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("heFamilyTradeCheck")
    public Map<String, Object> heFamilyTradeCheck(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String phoneNum = request.getParameter("form1_phoneNum");
        if (StringUtils.isEmpty(phoneNum)) {
            resultMap.put("respCode", "-1");
            resultMap.put("respDesc", "手机号码不能为空!");
            return resultMap;
        }
        try {
            PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
            phoneAttributionModel.setSerialNumber(phoneNum);
            resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);

            if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))) {
                resultMap.put("respCode", "-1");
                resultMap.put("respDesc", "请输入湖南移动号码!");
                return resultMap;
            }

//             HeFamilyTradeCheckCondition condition = new HeFamilyTradeCheckCondition();
//             condition.setSerial_number(phoneNum);
//             resultMap = heFamilyService.heFamilyTradeCheck(condition);
//             if("0".equals(resultMap.get("respCode"))){
//                 JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
//                 JSONObject result = (JSONObject) resultArray.get(0);
//                 String resultCode = (String)result.get("X_RESULTCODE") ;
//                 String resultInfo = (String)result.get("X_RESULTINFO") ;
//
//                 resultMap.put("resultCode", resultCode);
//                 resultMap.put("respDesc", resultInfo);
//             }

        } catch (Exception e) {
            logger.error("和家庭办理校验 :" + e.getMessage());
            resultMap.put("respCode", "-1");
            resultMap.put("respDesc", "和家庭办理校验失败:" + e.getMessage());
        }

        return resultMap;
    }

    /**
     * 支付订单
     *
     * @param ；
     * @param response
     * @param model
     * @throws
     */
    @RequestMapping("payOrder")
    public void payOrder(HttpServletRequest request,
                         HttpServletResponse response, Model model) throws Exception {
        try {
            String  orderSubNo = request.getParameter("payForm_orderSubNo");
            String payPlatform = request.getParameter("payForm_payPlatform");
            String eparchyCode = request.getParameter("payForm_eparchyCode");
            Map<String, Object> resultMap = orderService.mergeOrder(orderSubNo);
            TfOrder order = (TfOrder)resultMap.get("order");

	     	/*支付方式*/
            Short payTypeId = CommonParams.PAY_PLATFORM.get(payPlatform);  //账户类型Id 2:和包支付,3:支付宝支付
            if(payTypeId == null){
                payTypeId = 2;
            }

			/* 分润规则 */
            CompanyInfo companyInfo = new CompanyInfo();
            short payPlatForm = BroadbandConstants.PAY_PLATFORM.get(payPlatform);    //支付平台与支付机构代码对应关系
            companyInfo.setChannelCityCode(eparchyCode);
            companyInfo.setCompanyTypeId((short) 7);
            CompanyAcctInfo eparchyAcctInfo = companyAcctService.getAcctByChannelAndType(companyInfo, payPlatForm);
            String shRule = eparchyAcctInfo.getAccountNum() + "^"
                    + order.getOrderAmount() + "^和家庭立即办理";
            logger.info("和家庭立即办理,分润规则:" + shRule);

            // 结算价格
            Long orderAmount = Long.valueOf(order.getOrderAmount() + "");
            // 同步页面回调地址
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
            String callbackUrl = basePath + "/BroadbandTrade/toPayResult";
            // 异步页面回调地址
            String notifyCallbackUrl = afterOrderPayUrl + "/BroadbandTrade/payAfterNotify";

            TfOrderPay orderPay = new TfOrderPay();
            orderPay.setOrderId(order.getOrderId());
            orderPay.setOrderPayAmount(orderAmount);
            orderPay.setOrgCode(payPlatform);
            orderPay.setShRule(shRule);
            orderPay.setHmac(BroadbandConstants.SIGN_KEY);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setMerchantId(BroadbandConstants.MERCHANT_ID);

            String content = orderService.applyPay(orderPay, callbackUrl,
                    notifyCallbackUrl);

            String encode = PAY_CHARSET.get(payPlatform);
            if (encode == null) {
                encode = "GBK";
            }
            response.setContentType("text/html;charset=" + encode);
            PrintWriter out = response.getWriter();
            out.print(content);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("和家庭立即办理,支付订单异常:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 跳转到订单支付结果页面，供支付中心调用（同步）
     *
     * @param model
     * @param returnCode
     * @param chargeflowId
     * @return
     * @throws Exception
     */
    @RequestMapping("/toPayResult")
    public String toPayResult(HttpServletRequest request,
                              HttpServletResponse response, Model model, String returnCode,
                              Long chargeflowId, String merchantId) throws Exception {

        logger.info("和家庭立即办理,支付同步回调参数:returnCode=" + returnCode
                + ",chargeflowId=" + chargeflowId);
        try {
            TfOrderSub orderSub = new TfOrderSub();
            orderSub.setOrderId(chargeflowId);
            List<TfOrderSub> orderSubList = orderQueryService.queryBaseOrderList(orderSub);
            if (orderSubList != null && orderSubList.size() > 0) {
                model.addAttribute("orderSub", orderSubList.get(0));
            }
            model.addAttribute("returnCode", returnCode);
        } catch (Exception e) {
            logger.error("单宽带新装,支付同步回调失败，异常信息:" + e);
            e.printStackTrace();
        }
        return "web/goods/broadband/newInstall/payResult";
    }


    /**
     * 跳转到订单支付结果页面，供支付中心调用（异步）
     *
     * @param
     * @param returnCode
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("/payAfterNotify")
    public void payAfterNotify(HttpServletRequest request, HttpServletResponse response, String merchantId, String returnCode, String message, String type,
                               String version, Integer amount, Long orderId, String payDate,
                               String status, String orderDate, String payNo, String org_code,
                               String organization_payNo, String hmac, String shRule, String accountDate) throws Exception {

        try {
            logger.info("和家庭立即办理,支付异步回调参数:" + request.getParameterMap());
            MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
            Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
            if (!validataService.valHmac(payParamMap, merChantBean)) {
                throw new Exception("和家庭立即办理:签名验证未通过");
            }

            TfOrderSub orderSubParam = new TfOrderSub();
            orderSubParam.setOrderId((long) orderId);
            orderSubParam.setPage(new Page<TfOrderSub>(1, 100));
            Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
            List<TfOrderSub> orderSubList = orderSubPage.getList();
            if (orderSubList != null && orderSubList.size() > 0) {
                TfOrderSub orderSub = orderSubList.get(0);
                if (orderSub.getDetailList() != null && orderSub.getDetailList().size() > 0) {
                    TfOrderSubDetail orderSubDetail = orderSub.getDetailList().get(0);
                    TfOrderSubDetailBusiParam orderSubDetailBusiParam = new TfOrderSubDetailBusiParam();
                    orderSubDetailBusiParam.setSkuBusiParamName("DEAL_TIME");
                    orderSubDetailBusiParam.setSkuBusiParamValue(accountDate);
                    orderSubDetailBusiParam.setSkuBusiParamDesc("会计日期");
                    orderSubDetailBusiParam.setOrderSubDetailId(orderSubDetail.getOrderSubDetailId());
                    List<TfOrderSubDetailBusiParam> busiParamList = Lists.newArrayList();
                    busiParamList.add(orderSubDetailBusiParam);
                    orderService.saveBusinessParam(busiParamList);
                }
            }
            //订单支付信息
            TfOrderPay orderPay = new TfOrderPay();
            orderPay.setMerchantId(merchantId);
            orderPay.setMessage(message);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setOrderId((long) orderId);
            orderPay.setOrderPayAmount((long) amount);
            orderPay.setOrderPayTime(org.apache.commons.lang3.time.DateUtils.parseDate(payDate, "yyyyMMddHHmmss"));
            orderPay.setOrgCode(org_code);
            orderPay.setPayLogId(organization_payNo);
            orderPay.setPayNo(payNo);
            orderPay.setPayState(status);
            orderPay.setReturnCode(returnCode);
            orderPay.setType(type);
            orderPay.setVersion(version);
            OrderProcessParam param = new OrderProcessParam();
            param.setBusinessId(String.valueOf(orderId));
            param.setOrderStatusId(OrderConstant.STATUS_PAY);
            param.setAct(1);
            param.put(Variables.ORDER_PAY, orderPay);
            orderService.completeByOrderId(param);//订单流转
            response.getWriter().print("success");

        } catch (Exception e) {
            logger.error("和家庭立即办理,支付异步回调失败，异常信息" + e);
            BusiLogUtils.writerLogging(request, "payAfterNotify", "", "", "", "", "", "", "", e, "2", "");
            response.getWriter().print("fail");
        }

    }


}

