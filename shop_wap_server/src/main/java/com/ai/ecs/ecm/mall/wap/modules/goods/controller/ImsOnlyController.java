package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.ParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.ims.entity.ImsCheckCondition;
import com.ai.ecs.ecsite.modules.ims.entity.ImsCreateCondition;
import com.ai.ecs.ecsite.modules.ims.entity.ImsSnCheckCondition;
import com.ai.ecs.ecsite.modules.ims.service.ImsQueryService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.json.JSONObject;
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
import java.util.*;

/**
 * 已有宽带用户加装固话
 * Created by hexiao3@asiainfo.com on 2018/3/8.
 */
@Controller
@RequestMapping("imsOnly")
public class ImsOnlyController extends BaseController{
    @Autowired
    private DictService dictService;
    @Autowired
    private IGoodsManageService goodsManageService;
    @Autowired
    private IMemberAddressService memberAddressService;
    @Autowired
    private PhoneAttributionService phoneAttributionService;
    @Autowired
    private ImsQueryService imsQueryService;
    @Autowired
    private BroadBandService broadBandService;
    @Autowired
    private IOrderService orderService;

    private Logger logger = Logger.getLogger(ImsOnlyController.class);

    /**
     * 进入固话办理页面
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("init")
    public String init(HttpServletRequest request, Model model) throws Exception {
        String serialNumber ="";
        try {
            MemberVo memberVo = UserUtils.getLoginUser(request);
            serialNumber = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
//            serialNumber = "13808493515";
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e,e);
        }
        if(StringUtils.isBlank(serialNumber)){
            throw new Exception("请登录或登录帐号未绑定手机号!");
        }else{
            model.addAttribute("installPhoneNum", serialNumber);
        }
        /**
         * 手机号固话办理校验
         */
        ImsSnCheckCondition imsSnCheckCondition = new ImsSnCheckCondition();
        imsSnCheckCondition.setSerialNumber(serialNumber);
//        imsSnCheckCondition.setStaffId("ITFWC000");
//        imsSnCheckCondition.setTradeDepartPassword("ai1234");
        Map imsResult = imsQueryService.imsSnCheck(imsSnCheckCondition);
        if (!"0".equals(imsResult.get("respCode"))) {
            imsResult.put("respCode", "-1");
            imsResult.put("respDesc",imsResult.get("respDesc"));
            model.addAttribute("resultMap",imsResult);
            return "web/wapResult";
        }
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(serialNumber);
//        LoggerFactory.getLogger("webDbLog").info("broadbandAccount myAccount broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
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
            imsResult.put("respCode", "-1");
            imsResult.put("respDesc", "用户无宽带信息，办理固话必须先办理宽带!");
            model.addAttribute("resultMap",imsResult);
            return "web/wapResult";
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
        return "web/goods/imsOnly/myAccount";
    }
    /**
     * 套餐选择
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model) throws Exception {
        Map imsResult = new HashMap();
        if(!StringUtils.isNotEmpty(request.getParameter("addrId"))){
            imsResult.put("respCode", "-1");
            imsResult.put("respDesc", "用户无宽带信息，办理固话必须先办理宽带!");
            model.addAttribute("resultMap",imsResult);
            return "web/wapResult";
        }
        model.addAttribute("custName", request.getParameter("custName"));
        model.addAttribute("addrDesc", request.getParameter("addrDesc"));
        model.addAttribute("addrId", request.getParameter("addrId"));
        model.addAttribute("installPhoneNum", request.getParameter("installPhoneNum"));
        Map<String,Object> bbArgs = new HashMap<>();
        bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","IMS_CATEGORY"));
        List<String> eparchyCodes = new ArrayList<>();
        eparchyCodes.add(BroadbandConstants.EPARCHY_CODE);
        bbArgs.put("eparchyCodes",eparchyCodes);
        bbArgs.put("orderColumn","SV.BARE_PRICE");
        bbArgs.put("orderType","ASC");
        bbArgs.put("goodsStatusId",4);
        bbArgs.put("chnlCode","E007");
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
        model.addAttribute("goodsInfoList",goodsInfoList);
        List<BroadbandItemEntity> heBroadbandItemList = null;
        try {
            heBroadbandItemList = BroadbandUtils.convertNewHeHalfItemEntityList(goodsInfoList);
        }  catch (Exception e) {
            logger.error("固话加装办理异常:"+e.getMessage());
            e.printStackTrace();
            throw e;
        }
        model.addAttribute("heBroadbandItemList",heBroadbandItemList);
        return "web/goods/imsOnly/index";
    }

    /**
     * 填写固话参数
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("imsChoose")
    public String imsChoose(HttpServletRequest request, Model model){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        model.addAttribute("goodsSkuId", request.getParameter("form1_skuId"));
        model.addAttribute("form1_productId", request.getParameter("form1_productId"));
        model.addAttribute("form1_packageId", request.getParameter("form1_packageId"));
        model.addAttribute("form1_goodsName", request.getParameter("form1_goodsName"));
        model.addAttribute("form1_level", request.getParameter("form1_level"));
        model.addAttribute("form1_custName", request.getParameter("form1_custName"));
        model.addAttribute("form1_addressName", request.getParameter("form1_addressName"));
        model.addAttribute("form1_houseCode", request.getParameter("form1_houseCode"));
        model.addAttribute("installPhoneNum", request.getParameter("installPhoneNum"));
        String installPhoneNum =   request.getParameter("installPhoneNum");
        String EPARCHY_CODE ="";
        String cityName = "";
        try {
            PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
            phoneAttributionModel1.setSerialNumber(installPhoneNum);
            resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
             EPARCHY_CODE= String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
//            EPARCHY_CODE = "0731";
            model.addAttribute("eparchy_Code", EPARCHY_CODE);
            if (!"731".equals(((Map) ((List) resultMap.get("result")).get(0)).get("PROVINCE_CODE"))){
                resultMap.put("respCode", "-1");
                resultMap.put("respDesc", "请输入湖南移动号码办理!");
                model.addAttribute("resultMap",resultMap);
                return "web/wapResult";
            }
        }catch (Exception e) {
            logger.error("手机号码归属地 :"+e.getMessage());
            model.addAttribute("resultMap",resultMap);
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
                if(city.getEparchyCode().equals(EPARCHY_CODE)){
                    cityName = city.getOrgName();
                }
            }
        }
        model.addAttribute("cityName",cityName);
        return "web/goods/imsOnly/imsDetail";
    }


    /**
     * 提交订单保存临时表
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("comfirmOrder")
    public String submitInstallOrder(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception{
        String skuId = request.getParameter("form1_skuId");
        //标准地址编码
        String houseCode = request.getParameter("form1_houseCode");
        //安装地址
        String installAddress = request.getParameter("form1_addressName");
        //手机
        String phoneNum = request.getParameter("installPhoneNum");
        String imsphone = request.getParameter("imsPhone");
        //装机人姓名
        String installName = request.getParameter("form1_custName");
        //身份证号码
        String idCard = request.getParameter("form1_psptId");
        //支付方式
        String payMode = request.getParameter("payMode");
        //payModeId 2:在线支付;1:现场支付
        int payModeId = "在线支付".equals(payMode) ? 2 : 1;
        //地市编码
        String eparchyCode = request.getParameter("form1_eparchyCode");
        //套餐产品id
        String productId = request.getParameter("form1_productId");
        //套餐包id
        String packageId = request.getParameter("form1_packageId");
        //产品费用
        String productPrice = request.getParameter("form1_price");

        long Price = 0;


		/*宽带商品信息*/
        Map<String,Object> map = Maps.newHashMap();
        map.put("goodsSkuId", skuId);
        map.put("containGoodsSkuIdInfo",true);
        map.put("containGoodsBusiParam",true);
        List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
        Map<String,Object> resMap = Maps.newHashMap();
        if(Collections3.isEmpty(goodsInfoList)){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "查询不到商品信息");
            model.addAttribute("resultMap",resMap);
            return "web/wapResult";
        }
        if(goodsInfoList.size() > 1){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "商品数据错误,商品数据不唯一!");
            model.addAttribute("resultMap",resMap);
            return "web/wapResult";
        }
        TfGoodsInfo tfGoodsInfo = goodsInfoList.get(0);
        if(Collections3.isEmpty(tfGoodsInfo.getTfGoodsSkuList())){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "商品SKU数据错误,商品SKU数据为空!");
            model.addAttribute("resultMap",resMap);
            return "web/wapResult";
        }
        if(tfGoodsInfo.getTfGoodsSkuList().size() != 1){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "商品SKU数据错误,商品SKU数据不唯一!");
            model.addAttribute("resultMap",resMap);
            return "web/wapResult";
        }
        //校验
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(phoneNum);
        resMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        //校验未通过
        if (!"731".equals(((Map) ((List) resMap.get("result")).get(0)).get("PROVINCE_CODE"))){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "请输入湖南移动号码!");
            model.addAttribute("resultMap",resMap);
            return "web/wapResult";
        }
        /**
         * 固话号码校验
         */
        ImsCheckCondition imsCheckCondition = new ImsCheckCondition();
        imsCheckCondition.setSerialNumber(imsphone);
//        imsCheckCondition.setStaffId("ITFWC000");
//        imsCheckCondition.setTradeDepartPassword("ai1234");
        Map imsRs = imsQueryService.imsCheck(imsCheckCondition);
        if (!"0".equals(imsRs.get("respCode"))) {
            resMap.put("respCode", "1");
            resMap.put("respDesc", "固话号码选占失败，请更换号码!");
            model.addAttribute("resultMap",resMap);
            return "web/wapResult";
        }

        logger.info("goodsInfoList:"+ JSON.toJSONString(goodsInfoList));
        List<BroadbandItemEntity> broadbandItem = new ArrayList<>();
        broadbandItem = BroadbandUtils.convertNewHeHalfItemEntityList(goodsInfoList);
        logger.info("broadbandItem:"+ JSON.toJSONString(broadbandItem));
        long goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(Price+""));
        int broadbandPrice = broadbandItem.get(0).getPrice().intValue();
		/*订单明细业务参数信息*/
        List<O2oOrderParamTemp> paramList = Lists.newArrayList();
        ImsCreateCondition imsCreateCondition = new ImsCreateCondition();
        ParamUtils paramUtils = new ParamUtils();
        paramUtils.addParams("SERIAL_NUMBER",phoneNum,"手机号码");
        paramUtils.addParams("ADDR_DESC",installAddress,"详细地址");
        paramUtils.addParams("IMSUSER_TYPE","1","固话类型");
        paramUtils.addParams("EPARCHY_CODE",eparchyCode,"地州");
        paramUtils.addParams("ADDR_ID",houseCode,"宽带安装地址编码");
        paramUtils.addParams("IMS_OPT_DISCNT",broadbandItem.get(0).getGiveData(),"固话可选元素");
        paramUtils.addParams("IMS_OPT_DISCNT_ENDDATE","2050-12-31","固话可选元素结束日期");
        paramUtils.addParams("AUTH_SERIAL_NUMBER",phoneNum,"手机号");
        paramUtils.addParams("HSS_AUTH_TYPE","1","固定参数");
        paramUtils.addParams("REPLACEMODEL_FLAG","0","是否更换光猫");
        paramUtils.addParams("HSS_CAPS_TYPE","1","可选能力");
        paramUtils.addParams("PRODUCT_ID","16100001","产品ID");
        paramUtils.addParams("IMS_PRODUCT_ID",productId,"固话产品ID");
        paramUtils.addParams("IMS_PACKAGE_ID",packageId,"固话包ID");
        paramUtils.addParams("IMS_PHONE",imsphone,"固话号码");
        paramUtils.addParams("TRADE_TYPE_CODE","161","业务类型");
        paramUtils.addParams("MODEM_SALE_TYPE","3","MODEM方式");
        paramUtils.addParams("HSS_CAPS_ID","4","参数");
        imsCreateCondition.setSerialNumber(phoneNum);
        imsCreateCondition.setImsProductId(productId);
        imsCreateCondition.setAddrDesc(installAddress);
        imsCreateCondition.setAddrId(houseCode);
        imsCreateCondition.setAuthSerialNumber(phoneNum);
        imsCreateCondition.setImsOptDiscnt(broadbandItem.get(0).getGiveData());
        imsCreateCondition.setImsOptDiscntEndTime("2050-12-31");
        imsCreateCondition.setEparchyCode(eparchyCode);
        imsCreateCondition.setImsPackageId(packageId);
        imsCreateCondition.setImsPhone(imsphone);
//        imsCreateCondition.setStaffId("ITFWC000");
//        imsCreateCondition.setTradeDepartPassword("ai1234");
        // 添加渠道信息
        MemberVo memberVo = UserUtils.getLoginUser(request);
        Map result =  imsQueryService.imsCreate(imsCreateCondition);
        //----增加受理中校验接口开始。。。
        //增加受理中校验参数
        if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
            //throw new Exception(" 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            resMap.put("respCode", "1");
            resMap.put("respDesc", " 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            model.addAttribute("resultMap",resMap);
            return "web/wapResult";
        }
//        //----增加受理中校验接口结束。。。
//
        String goodsFormat = broadbandItem.get(0).getBroadbandItemName() + "/" + broadbandItem.get(0).getTerm();

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
        orderSubDetail.setGoodsId(broadbandItem.get(0).getGoodsId());
        orderSubDetail.setGoodsSkuId(broadbandItem.get(0).getGoodsSkuId());
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
        orderSub.setOrderTypeId(OrderConstant.TYPE_IMS);
        orderSub.setOrderSubAmount(goodsSkuPrice);    //子订单总额
        orderSub.setOrderSubPayAmount(goodsSkuPrice);//支付金额
        orderSub.setOrderSubDiscountAmount(0L);//优惠金额
        orderSub.setOrderChannelCode("E007");//渠道编码，网上商城
        orderSub.setPayModeId(payModeId);//支付方式：1、货到付款；2、在线支付；3、到厅自提；4、不需支付
        orderSub.setDeliveryModeId(3);//配送方式，虚拟商品
        orderSub.setIsInvoicing(0);//是否开发票:0-不开

        logger.info("固话业务立即办理,提交订单参数orderSub:" + JSONArray.toJSONString(orderSub));
        List<TfOrderSub> orderSubList = orderService.newOrder(orderSub);

        model.addAttribute("orderSub",orderSubList.get(0));
        model.addAttribute("orderSubDetail",orderSubList.get(0).getDetailList().get(0));
        resMap.put("respCode", "0");
        resMap.put("respDesc", "生成订单成功");
        resMap.put("orderSub", orderSubList.get(0));
        resMap.put("orderSubDetail",orderSubList.get(0).getDetailList().get(0));
        model.addAttribute("resultMap",resMap);
        return "web/wapResult";
    }

    /**
     * 查询号码池
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping("queryImsNumbers")
    public List queryImsNumbers(String key,String eparchCode) throws Exception {
        if(!StringUtils.isNotEmpty(key)){
            key = "0";
        }
        QrySelectableImsSnCondition qrySelectableImsSnCondition = new QrySelectableImsSnCondition();
        qrySelectableImsSnCondition.setRoute_eparchy_code(eparchCode);
        qrySelectableImsSnCondition.setIms_sn(key);
        qrySelectableImsSnCondition.setMatch_position("0");
//        qrySelectableImsSnCondition.setStaffId("SUPERUSR");
//        qrySelectableImsSnCondition.setDepartId("23061");
//        qrySelectableImsSnCondition.setTradeDepartPassword("ai1234");
        Map result = broadBandService.qrySelectableImsSn(qrySelectableImsSnCondition);
        List<Map> rs =new ArrayList<>();
        if ("0".equals(result.get("respCode"))) {
            List<Map> list = (List) result.get("result");

            if(list!=null&&list.size()>0){
                int size = list.size();
                int index = 0;
                int endIndex = size;
                if(size>10){
                    Random rand=new Random();
                    index=rand.nextInt(size-10);
                    endIndex = index+10;
                }else{
                    endIndex = size;
                }
                rs.addAll(list.subList(index,endIndex));
            }
        }
        return rs;
    }


    @RequestMapping("getProduct")
    @ResponseBody
    public Map getProduct(HttpServletRequest request, Model model) throws Exception {
        Map imsResult = new HashMap();
        List<BroadbandItemEntity> heBroadbandItemList = null;
        try {
            Map<String,Object> bbArgs = new HashMap<>();
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","IMS_CATEGORY"));
            List<String> eparchyCodes = new ArrayList<>();
            eparchyCodes.add(BroadbandConstants.EPARCHY_CODE);
            bbArgs.put("eparchyCodes",eparchyCodes);
            bbArgs.put("orderColumn","SV.BARE_PRICE");
            bbArgs.put("orderType","ASC");
            bbArgs.put("goodsStatusId",4);
            bbArgs.put("chnlCode","E007");
            List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
            heBroadbandItemList = BroadbandUtils.convertNewHeHalfItemEntityList(goodsInfoList);
            MemberVo memberVo = UserUtils.getLoginUser(request);
            String serialNumber = String.valueOf(memberVo.getMemberLogin().getMemberPhone());
            PhoneAttributionModel phoneAttributionModel1 = new PhoneAttributionModel();
            phoneAttributionModel1.setSerialNumber(serialNumber);
            Map resultMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel1);
            String earchyCode= String.valueOf(((Map) ((List) resultMap.get("result")).get(0)).get("AREA_CODE"));
            String cityName="";
            // 地市信息
            List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
            // 默认区县信息(长沙市)
            if (!CollectionUtils.isEmpty(cityList)) {
                for (ThirdLevelAddress city : cityList) {
                    city.setOrgName(city.getOrgName().replace("市", ""));
                    if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                        city.setOrgName("湘西州");
                    }
                    if(city.getEparchyCode().equals(earchyCode)){
                        cityName = city.getOrgName();
                    }
                }
            }
            imsResult.put("cityName",cityName);
            imsResult.put("earchyCode",earchyCode);
        }  catch (Exception e) {
            logger.error("固话加装办理异常:"+e.getMessage());
            e.printStackTrace();
            imsResult.put(BroadbandConstants.RESPONSE_DESC,"系统异常！");
            imsResult.put(BroadbandConstants.RESPONSE_CODE,"-1");
        }
        imsResult.put("list",heBroadbandItemList);
        imsResult.put(BroadbandConstants.RESPONSE_CODE,"0");
        imsResult.put(BroadbandConstants.RESPONSE_DESC,"ok");
        return imsResult;
    }
}