package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.ParamUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfo;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoCondition;
import com.ai.ecs.ecsite.modules.broadBand.entity.BroadbandDetailInfoResult;
import com.ai.ecs.ecsite.modules.broadBand.entity.QrySelectableImsSnCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.ims.entity.ImsCheckCondition;
import com.ai.ecs.ecsite.modules.ims.entity.ImsCreateCondition;
import com.ai.ecs.ecsite.modules.ims.entity.ImsSnCheckCondition;
import com.ai.ecs.ecsite.modules.ims.service.ImsQueryService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * O2o渠道版固话加装
 * Created by hexiao3@asiainfo.com on 2018/3/13.
 */
@RequestMapping("o2oImsOnly")
@Controller
public class O2oImsOnlyController  extends BaseController {
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
    private O2oOrderTempService orderTempService;

    @Autowired
    private O2oOrderParamTemService orderParamTemService;

    @Autowired
    private O2oParamUtils o2oParamUtils;

    private Logger logger = Logger.getLogger(O2oImsOnlyController.class);
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
        return "web/broadband/o2o/imsOnly/imsInit";
    }
    /**
     * 进入固话办理页面
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("queryAccount")
    public String init(HttpServletRequest request, Model model) throws Exception {
        String installPhoneNum = request.getParameter("installPhoneNum");
        if(StringUtils.isBlank(installPhoneNum)){
            throw new Exception("请输入手机号!");
        }else{
            model.addAttribute("installPhoneNum", installPhoneNum);
        }
        /**
         * 手机号固话办理校验
         */
        ImsSnCheckCondition imsSnCheckCondition = new ImsSnCheckCondition();
        imsSnCheckCondition.setSerialNumber(installPhoneNum);
//        imsSnCheckCondition.setStaffId("ITFWC000");
//        imsSnCheckCondition.setTradeDepartPassword("ai1234");
        Map imsResult = imsQueryService.imsSnCheck(imsSnCheckCondition);
        if (!"0".equals(imsResult.get("respCode"))) {
            imsResult.put("respCode", "-1");
            imsResult.put("respDesc",imsResult.get("respDesc"));
            model.addAttribute("resultMap",imsResult);
            return "web/o2oResult";
        }
        BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
        condition.setSerialNumber(installPhoneNum);
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
            return "web/o2oResult";
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
        return "web/broadband/o2o/imsOnly/myAccount";
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
            return "web/o2oResult";
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
            logger.error("和家庭办理异常:"+e.getMessage());
            e.printStackTrace();
            throw e;
        }
        model.addAttribute("heBroadbandItemList",heBroadbandItemList);
        return "web/broadband/o2o/imsOnly/index";
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
//        model.addAttribute("eparchy_Code", "0731");
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
                return "web/o2oResult";
            }
//            model.addAttribute("eparchy_Code","0731");
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
//        model.addAttribute("cityList", cityList);
        return "web/broadband/o2o/imsOnly/imsDetail";
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
    @RequestMapping("comfirmOrder")
    public Map submitInstallOrder(HttpServletRequest request, HttpServletResponse response, Model model)throws Exception{
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
        //工号密码
        String staffPwd = request.getParameter("staffPwd");//预约安装日期段
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
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
            return resMap;
        }
        if(goodsInfoList.size() > 1){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "商品数据错误,商品数据不唯一!");
            return resMap;
        }
        TfGoodsInfo tfGoodsInfo = goodsInfoList.get(0);
        if(Collections3.isEmpty(tfGoodsInfo.getTfGoodsSkuList())){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "商品SKU数据错误,商品SKU数据为空!");
            return resMap;
        }
        if(tfGoodsInfo.getTfGoodsSkuList().size() != 1){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "商品SKU数据错误,商品SKU数据不唯一!");
            return resMap;
        }
        //校验
        PhoneAttributionModel phoneAttributionModel = new PhoneAttributionModel();
        phoneAttributionModel.setSerialNumber(phoneNum);
        resMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        //校验未通过
        if (!"731".equals(((Map) ((List) resMap.get("result")).get(0)).get("PROVINCE_CODE"))){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "请输入湖南移动号码!");
            return resMap;
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
        }

        logger.info("goodsInfoList:"+ JSON.toJSONString(goodsInfoList));
        List<BroadbandItemEntity> broadbandItem = new ArrayList<>();
        broadbandItem = BroadbandUtils.convertNewHeHalfItemEntityList(goodsInfoList);
        logger.info("broadbandItem:"+ JSON.toJSONString(broadbandItem));
        long goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(Price+""));
        int broadbandPrice = broadbandItem.get(0).getPrice().intValue();
		/*订单明细业务参数信息*/
//        O2oParamUtils o2oParamUtils = new O2oParamUtils();
        List<O2oOrderParamTemp> paramList = Lists.newArrayList();
        ImsCreateCondition imsCreateCondition = new ImsCreateCondition();
        o2oParamUtils.removeParams();
        o2oParamUtils.addParams("SERIAL_NUMBER",phoneNum,"手机号码");
        o2oParamUtils.addParams("ADDR_DESC",installAddress,"详细地址");
        o2oParamUtils.addParams("IMSUSER_TYPE","1","固话类型");
        o2oParamUtils.addParams("EPARCHY_CODE",eparchyCode,"地州");
        o2oParamUtils.addParams("ADDR_ID",houseCode,"宽带安装地址编码");
        o2oParamUtils.addParams("IMS_OPT_DISCNT",broadbandItem.get(0).getGiveData(),"固话可选元素");
        o2oParamUtils.addParams("IMS_OPT_DISCNT_ENDDATE","2050-12-31","固话可选元素结束日期");
        o2oParamUtils.addParams("AUTH_SERIAL_NUMBER",phoneNum,"手机号");
        o2oParamUtils.addParams("HSS_AUTH_TYPE","1","固定参数");
        o2oParamUtils.addParams("REPLACEMODEL_FLAG","0","是否更换光猫");
        o2oParamUtils.addParams("HSS_CAPS_TYPE","1","可选能力");
        o2oParamUtils.addParams("PRODUCT_ID","16100001","产品ID");
        o2oParamUtils.addParams("IMS_PRODUCT_ID",productId,"固话产品ID");
        o2oParamUtils.addParams("IMS_PACKAGE_ID",packageId,"固话包ID");
        o2oParamUtils.addParams("IMS_PHONE",imsphone,"固话号码");
        o2oParamUtils.addParams("TRADE_TYPE_CODE","161","业务类型");
        o2oParamUtils.addParams("MODEM_SALE_TYPE","3","MODEM方式");
        o2oParamUtils.addParams("HSS_CAPS_ID","4","参数");
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
        if(memberVo.getChannelInfo()==null){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "受理校验失败：【未配置渠道信息，请用店长账户配置渠道信息!】");
            return resMap;
        }
        o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneNum);
        o2oParamUtils.addConditionChannel(imsCreateCondition,memberVo.getChannelInfo(),phoneNum);
        imsCreateCondition.setPreType("1");
        Map result =  imsQueryService.imsCreate(imsCreateCondition);
        //----增加受理中校验接口开始。。。
        //增加受理中校验参数
//
        if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
            //throw new Exception(" 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            resMap.put("respCode", "1");
            resMap.put("respDesc", " 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            return resMap;
        }
//        //----增加受理中校验接口结束。。。
//
        String goodsFormat = broadbandItem.get(0).getBroadbandItemName() + "/" + broadbandItem.get(0).getTerm();

        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
//        orderTempInfo.setOrder_temp_id(Long.valueOf(orderTempService.getId()));
        orderTempInfo.setOrder_type_id(Long.valueOf(OrderConstant.TYPE_IMS));
        orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
        orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
        orderTempInfo.setContact_phone(phoneNum);
        orderTempInfo.setEparchy_code(eparchyCode);
        orderTempInfo.setCounty(request.getParameter("form1_county"));
        orderTempInfo.setGoods_id(broadbandItem.get(0).getGoodsId());
        orderTempInfo.setGoods_sku_id(broadbandItem.get(0).getGoodsSkuId());
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
            resMap.put("respCode", "0");
            resMap.put("respDesc", "生成订单成功");
        }catch (Exception e){
            resMap.put("respCode", "1");
            resMap.put("respDesc", "生成订单失败");
        }

        return resMap;
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
}
