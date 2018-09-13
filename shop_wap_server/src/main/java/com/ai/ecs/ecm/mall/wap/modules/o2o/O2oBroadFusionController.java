package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.framework.exception.ServiceException;
import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.HNanConstant;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.DictService;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.entity.order.FusionBroadBandRegCondition;
import com.ai.ecs.ecsite.modules.broadBand.service.*;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqQueryFeeCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.queryPackage.entity.QueryAccountPackagesCondition;
import com.ai.ecs.ecsite.modules.queryPackage.service.QueryAccountPackagesService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.broadband.BroadbandFusionBean;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;
import com.ai.ecs.merchant.company.ICompanyAcctService;
import com.ai.ecs.o2o.api.O2oOrderParamTemService;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderParamTemp;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.constant.OrderConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import static com.ai.ecs.ecm.mall.wap.platform.utils.LogUtils.*;

/**
 * Created by Administrator on 2018/7/5.
 */
@Controller
@RequestMapping("o2oBroadFusion")
public class O2oBroadFusionController {
    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";

    private Logger logger = Logger.getLogger(O2oBroadFusionController.class);

    @Autowired
    private IMemberAddressService memberAddressService;

    @Autowired
    private ConsupostnService consupostnServiceImpl;

    @Autowired
    private HeFamilyService heFamilyService;

    @Autowired
    private IGoodsManageService goodsManageService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private DictService dictService;


    @Autowired
    private O2oOrderTempService orderTempService;

    @Autowired
    IOrderQueryService orderQueryService;

    @Autowired
    ICompanyAcctService companyAcctService;

    @Autowired
    private O2oOrderParamTemService orderParamTemService;

    @Autowired
    private O2oParamUtils o2oParamUtils;

    @Value("${afterOrderPayUrl}")
    String afterOrderPayUrl;

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private QryAddressService qryAddressService;

    @Autowired
    private BroadbandOrderService broadbandOrderService;

    @Autowired
    public FlowServeService flowServeService;

    private static Map bandCodeMap = new HashMap();
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

        /**
         * 两节
         */
        bandCodeMap.put("99543246","51281030");//79
        bandCodeMap.put("99543247","51281031");//99
        bandCodeMap.put("99543248","51281032");//129
        bandCodeMap.put("99543249","51281033");//169
        bandCodeMap.put("99543250","51281034");//199
        bandCodeMap.put("99543266","51281035");//249
        bandCodeMap.put("99543251","51281036");//299
        bandCodeMap.put("99543267","51281037");//349
        bandCodeMap.put("99543252","51281038");//399
    }


    @Autowired
    private QueryAccountPackagesService queryAccountPackagesService;


    /**
     * 号码填写
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("init")
    public String init(HttpServletRequest request , HttpServletResponse response, Model model, String secToken,String busiType,String minCost)  throws Exception {
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "init",request.getParameterMap(),"o2o一单清",request);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");

        //获取渠道人员手机号码
        MemberVo memberVo= UserUtils.getLoginUser(request);
        model.addAttribute("installPhoneNum", memberVo.getMemberLogin().getMemberPhone());
        ChannelInfo channelInfo = memberVo.getChannelInfo();
        ShopInfo shopInfo=memberVo.getShopInfo();
        model.addAttribute("staffId",channelInfo.getTradeStaffId());
        model.addAttribute("shopId",shopInfo.getShopId());
        model.addAttribute("minCost",minCost);
        model.addAttribute("busiType",busiType);
        model.addAttribute("secToken",secToken);
        writerFlowLogExitMenthod(streamNo,"",memberVo.getMemberLogin().getMemberLogingName(),getClass().getName(),
                "init",null,objectToMap(busiType),"o2o一单清");
        return "web/broadband/o2o/fusion/init";
    }


    /**
     *
     * @param request
     * @param response
     * @param model
     * @param busiType 0和家庭 1消费叠加型个人版 2 消费叠加型家庭版 3 组网送宽带
     * @return
     * @throws Exception
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request , HttpServletResponse response, Model model,String busiType,String minCost)  throws Exception {
        String streamNo=createStreamNo();
        model.addAttribute("minCost",minCost);
        model.addAttribute("busiType",busiType);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("respCode", "-1");
        resultMap.put("respDesc", "您的信息提交失败!");
        String hasAddress = request.getParameter("hasAddress");
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "init",request.getParameterMap(),"一单清办理",request);
        //获取办理用户手机号码
        String installPhoneNum = request.getParameter("installPhoneNum");
        //校验魔百和和宽带安装情况
        Map<String,String> checkResMap = isMbhAndBroadBand(installPhoneNum);
        model.addAttribute("checkResCode",checkResMap.get("checkResCode"));
        model.addAttribute("isBroadBand",checkResMap.get("isBroadBand"));
        model.addAttribute("isMbh",checkResMap.get("isMbh"));
        model.addAttribute("installPhoneNum", installPhoneNum);
        String isBroadBand = checkResMap.get("isBroadBand");
        if("0".equals(isBroadBand)){
            //已经办理宽带
            try{
                BroadbandDetailInfoCondition broadbandDetailInfoCondition=new BroadbandDetailInfoCondition();
                broadbandDetailInfoCondition.setSerialNumber(installPhoneNum);
                BroadbandDetailInfoResult broadbandDetailInfoResult = broadBandService.broadbandDetailInfo(broadbandDetailInfoCondition);
                model.addAttribute("broadbandDetailInfoResult",broadbandDetailInfoResult);
            }catch (Exception e) {
                e.printStackTrace();
                writerFlowLogThrowable(streamNo,"","",getClass().getName(),"init",null,"已有宽带用户信息查询:"+processThrowableMessage(e));
                logger.error("已有宽带用户信息查询："+e.getMessage());
            }
            if(!"Y".equalsIgnoreCase(hasAddress)){
                return "web/broadband/o2o/fusion/addr";
            }
        }

        if("0".equals(checkResMap.get("isMbh"))){
            throw new Exception("用户已办理魔百和!");
        }
        List<BroadbandItemEntity> itemList = getGoodsList(busiType,minCost);

        if(itemList.size() == 0){
            resultMap.put("respDesc", "参数错误，请确认访问的地址是否正确！");
            model.addAttribute("resultMap",resultMap);
            return "web/goods/broadband/newInstall/checkError";
        }
        model.addAttribute("broadItemList",itemList);

        if(BroadbandConstants.BROADBAND_BUSITYPE_HEFAMILY.equals(busiType)) {
            /**
             * 模拟参数
             */
            String  fee =queryFee(installPhoneNum);
            Long balance = Long.parseLong(fee)/100;
            Long curentPackage = 0L;
            String packageName = "";
            QueryAccountPackagesCondition queryCondition = new QueryAccountPackagesCondition();
            queryCondition.setSerialNumber(installPhoneNum);
            //查询当前套餐
            Map res = queryAccountPackagesService.queryPackageInfo(queryCondition);
            Boolean hasPackage = false;
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
                /**
                 * 判断用户是否办理新和家庭套餐
                 */
                if(bandCodeMap.get(discntCode)!=null){
                    model.addAttribute("package_code",bandCodeMap.get(discntCode));
                    hasPackage = true;
                }
            } else {
                throw new Exception("查询当前套餐失败!");
            }
            model.addAttribute("hasPackage",hasPackage);
            model.addAttribute("balance",balance);
            model.addAttribute("packageName",packageName);
            model.addAttribute("curentPackage",curentPackage);
            writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                    "init",objectToMap(busiType),objectToMap(checkResMap),"新和家庭");
            return "web/broadband/o2o/fusion/heFamily";
        }
        else if(BroadbandConstants.BROADBAND_BUSITYPE_CONS_PERSONAL.equals(busiType)) {
            return "web/broadband/o2o/fusion/personal";
        }else if(BroadbandConstants.BROADBAND_BUSITYPE_CONS_FAMILY.equals(busiType)){
            return "web/broadband/o2o/fusion/family";
        }else if(BroadbandConstants.BROADBAND_BUSITYPE_FAMILYNET.equals(busiType)){
            return "web/broadband/o2o/fusion/familyNet";
        }else{
            return "web/goods/broadband/newInstall/checkError";
        }
    }


    @RequestMapping("toInstall")
    public String confirmHeInstall(HttpServletRequest request, Model model, BroadbandFusionBean broadbandFusionBean)throws Exception{
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "confirmHeInstall",request.getParameterMap(),"一单清办理",request);
        // 地市信息
        List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId(BroadbandConstants.CITY_PID);
        if (!CollectionUtils.isEmpty(cityList)) {
//
            // 地市信息处理 用于适配Boss接口查询条件
            for (ThirdLevelAddress city : cityList) {
                city.setOrgName(city.getOrgName().replace("市", ""));
                if ("湘西土家族苗族自治州".equals(city.getOrgName())) {
                    city.setOrgName("湘西州");
                }
            }
        }

        String installPhoneNum = broadbandFusionBean.getSerialNumber();
        model.addAttribute("serialNumber", installPhoneNum);
        //获取手机号码的归属地市编码
        String eparchyCode = this.getEparchyCode(installPhoneNum);
        model.addAttribute("eparchyCode",eparchyCode);
        broadbandFusionBean.setEparchyCode(eparchyCode);
        model.addAttribute("cityList", cityList);
        model.addAttribute("broadbandFusionBean",broadbandFusionBean);
        writerFlowLogExitMenthod(streamNo,"","",getClass().getName(),
                "confirmHeInstall",null,null,"一单清办理");
        return "web/broadband/o2o/fusion/install";
    }

    /**
     * 订单确认
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("confirmInstall")
    public String confirmOrder(HttpServletRequest request, HttpServletResponse response,Model model,BroadbandFusionBean broadbandFusionBean) throws Exception{
        String streamNo=createStreamNo();
        writerFlowLogEnterMenthod(streamNo,"","",getClass().getName(),
                "confirmOrder",request.getParameterMap(),"一单清办理确认订单",request);
        if(StringUtils.isBlank(broadbandFusionBean.getContactPhone())){
            broadbandFusionBean.setContactPhone(broadbandFusionBean.getSerialNumber());
        }
        model.addAttribute("broadbandFusionBean",broadbandFusionBean);
        return "web/broadband/o2o/fusion/confirm";
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
    @RequestMapping("submitOrder")
    public Map<String,Object> submitInstallOrder(HttpServletRequest request,HttpServletResponse response,Model model,BroadbandFusionBean broadbandFusionBean)throws Exception{
        String streamNo=createStreamNo();
        String staffPwd = request.getParameter("staffPwd");
        staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
        logger.info("获取渠道部门密码：" + staffPwd);
        MemberVo memberVo = UserUtils.getLoginUser(request);
        String serialNumber = broadbandFusionBean.getSerialNumber();

        String skuId = broadbandFusionBean.getGoodsSkuId();

//        //联系人手机号
        String contactPhone = StringUtils.isBlank(broadbandFusionBean.getContactPhone())?serialNumber:broadbandFusionBean.getContactPhone();

//        //支付方式
        String payMode = broadbandFusionBean.getPayMode();
//        //payModeId 2:在线支付;1:现场支付
        payMode=StringUtils.isEmpty(payMode)?"0":payMode;

        String eparchyCode = broadbandFusionBean.getEparchyCode();

        String productId = broadbandFusionBean.getProductId();

        String hasBroadband = broadbandFusionBean.getIsBroadband();
        String hasMbh = broadbandFusionBean.getIsMbh();
        String busiType=broadbandFusionBean.getBusiType();
        String imsTabShow=StringUtils.isEmpty(broadbandFusionBean.getImsTabShow())?"false":broadbandFusionBean.getImsTabShow();
        String moreMagicTabShow=StringUtils.isEmpty(broadbandFusionBean.getMoreMagicTabShow())?"false":broadbandFusionBean.getMoreMagicTabShow();

        //是否购买宽带光猫
        //MODEM方式 3:客户自备;4:自动配发
        String modemSaleType = "4";

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
        phoneAttributionModel.setSerialNumber(serialNumber);
        resMap = phoneAttributionService.queryPhoneAttribution(phoneAttributionModel);
        //校验未通过
        if (!"731".equals(((Map) ((List) resMap.get("result")).get(0)).get("PROVINCE_CODE"))){
            resMap.put("code", "-1");
            resMap.put("message", "请输入湖南移动号码!");
            return resMap;
        }
        ChannelInfo channelInfo = memberVo.getChannelInfo();

        //办理校验
        if(BroadbandConstants.BROADBAND_BUSITYPE_CONS_PERSONAL.equals(busiType) ||BroadbandConstants.BROADBAND_BUSITYPE_CONS_FAMILY.equals(busiType)){
            ConsupostnCheckCondition consupostnCheckCondition = new ConsupostnCheckCondition();
            consupostnCheckCondition.setSerial_number(serialNumber);
            consupostnCheckCondition.setDepartId(channelInfo.getTradeDepartId());
            consupostnCheckCondition.setCityCode(channelInfo.getTradeCityCode());
            consupostnCheckCondition.setStaffId(channelInfo.getTradeStaffId());
            consupostnCheckCondition.setInModeCode(channelInfo.getInModeCode());
            consupostnCheckCondition.setChanId(channelInfo.getChanelId());
            consupostnCheckCondition.setTradeDepartPassword(staffPwd);
            if ("0".equals(hasBroadband.trim())) {//有宽带信息
                consupostnCheckCondition.setIs_exists("1");
            }else{
                consupostnCheckCondition.setIs_exists("0");
            }
            resMap = consupostnServiceImpl.consupostnCheck(consupostnCheckCondition);
            //校验未通过
            logger.error("消费叠加型校验调用CRM接口返回结果：" + resMap);
            if(!"0".equals(resMap.get("respCode")) && !"成功".equals(resMap.get("respCode"))){
                logger.error(">>>>>>>>>>消费叠加型接口校验未通过");
                resMap.put("code", resMap.get("respCode"));
                resMap.put("message", resMap.get("respDesc"));
                model.addAttribute("resultMap",resMap);
                return resMap;
            }
        }else if(BroadbandConstants.BROADBAND_BUSITYPE_HEFAMILY.equals(busiType)){
            HeFamilyTradeCheckCondition condition = new HeFamilyTradeCheckCondition();
            condition.setSerial_number(serialNumber);
            resMap = heFamilyService.heFamilyTradeCheck(condition);
            logger.error("和家庭校验接口返回结果：" + resMap);
            if (!"0".equals(resMap.get("respCode"))) {
                resMap.put("code",  resMap.get("respCode"));
                resMap.put("message", resMap.get("respDesc"));
                return resMap;
            }
        }
        logger.info("checkResult:"+resMap);
        logger.info("goodsInfoList:"+ JSON.toJSONString(goodsInfoList));
        BroadbandItemEntity broadbandItem = null;
        broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
        logger.info("broadbandItem:"+ JSON.toJSONString(broadbandItem));
        long gmPrice=0;
        if("4".equals(modemSaleType)&&broadbandItem.getModemPrice()!=null){//光猫自动派发
            gmPrice=broadbandItem.getModemPrice();
        }
        long mbhPrice=0;
        if(broadbandItem.getStbPrice()!=null){//机顶盒 自动派发
            mbhPrice=broadbandItem.getStbPrice();
        }
        FusionBroadBandRegCondition condition = new FusionBroadBandRegCondition();
        condition.setPreType("1");
        condition.setSerialNumber(serialNumber);
        condition.setProductId(productId);
        Integer orderType=null;
        if(BroadbandConstants.BROADBAND_BUSITYPE_HEFAMILY.equals(busiType)){
            orderType=OrderConstant.TYPE_HE_BROADBAND;
        }else{
            orderType=OrderConstant.TYPE_CONSUPOSTN;
        }

        List<O2oOrderParamTemp> paramList = Lists.newArrayList();
        o2oParamUtils.removeParams();
        o2oParamUtils.addParams("TRADE_DEPART_PASSWD", staffPwd, "渠道交易部门密码");
        o2oParamUtils.addParams("SERIAL_NUMBER",serialNumber,"手机号码");
        o2oParamUtils.addParams("PRODUCT_ID",productId,"产品ID");
        o2oParamUtils.addParams("ROUTE_EPARCHY_CODE", eparchyCode, "渠道用户市区编号");
        o2oParamUtils.addParams("BUSITYPE", "FUSION", "一单清业务类型");

        long totalPrice=0;

        //费用
        List<Map<String,Object>> feeList=new ArrayList<>();
        //无宽带，无魔百和
        if("1".equals(hasBroadband)){
            condition.setIptvTabShow("false");
            condition.setImsTabShow(imsTabShow);
            condition.setMoreMagicTabShow(moreMagicTabShow);
            condition.setIsExists("0");
            condition.setContact(broadbandFusionBean.getContact());
            condition.setContactPhone(contactPhone);
            o2oParamUtils.addParams("IMSTABSHOW", imsTabShow, "是否办理固话");
            o2oParamUtils.addParams("MOREMAGICTABSHOW", moreMagicTabShow, "是否办理魔百盒");
            o2oParamUtils.addParams("IPTVTABSHOW", "false", "是否办理IPTV");
            o2oParamUtils.addParams("CONTACT",broadbandFusionBean.getContact(),"联系人");
            o2oParamUtils.addParams("CONTACT_PHONE",contactPhone,"联系电话");
            o2oParamUtils.addParams("IS_EXISTS","0","是否存量宽带用户转入");

            //宽带安装信息
            Map<String,Object> BROADBAND = new HashMap<String,Object>();
            BROADBAND.put("ADDR_DESC", broadbandFusionBean.getAddressName());
            BROADBAND.put("MAX_WIDTH", broadbandFusionBean.getMaxWidth());
            BROADBAND.put("FREE_PORT_NUM", broadbandFusionBean.getFreePort());
            BROADBAND.put("ACCESS_TYPE", broadbandFusionBean.getCoverType());
            BROADBAND.put("MODEM_SALE_TYPE", modemSaleType);
            BROADBAND.put("ADDR_NAME", broadbandFusionBean.getAddressName());
            BROADBAND.put("ADDR_ID", broadbandFusionBean.getHouseCode());
            BROADBAND.put("BOOK_YES", broadbandFusionBean.getBookYes());
            BROADBAND.put("PASS_WORD",broadbandFusionBean.getSerialNumber().substring(5,11));
            if("1".equals(broadbandFusionBean.getBookYes())){
                BROADBAND.put("BOOK_INSTALL_DATE", broadbandFusionBean.getBookInstallDate());
                BROADBAND.put("BOOK_INSTALL_TIME", broadbandFusionBean.getBookInstallTime());
//                paramUtils.addParams("BOOK_INSTALL_DATE",broadbandFusionBean.getBookInstallDate(),"安装预约日期");
//                paramUtils.addParams("BOOK_INSTALL_TIME",broadbandFusionBean.getBookInstallTime(),"安装预约时间段");
            }
            condition.setBroadbandReqData(BROADBAND);
            o2oParamUtils.addParams("BROADBAND_REQ_DATA", JSON.toJSONString(BROADBAND).toString(), "宽带安装信息");

            //宽带安装费 （光猫）
            Map<String,Object> installFee=new HashMap<>();
            installFee.put("FEE_MODE", "0");
            installFee.put("OLDFEE", gmPrice);
            installFee.put("FEE", gmPrice);
            installFee.put("FEE_TYPE_CODE", "500");
            installFee.put("TRADE_TYPE_CODE", "1002");
            feeList.add(installFee);
            totalPrice+=gmPrice;
            if("true".equals(broadbandFusionBean.getMoreMagicTabShow())&& StringUtils.isNotEmpty(broadbandFusionBean.getMbh())){
                List<Map<String,Object>> MAGIC_BASE_INFO_LIST=new ArrayList<>();
                //魔百盒机顶盒
                Map<String,Object> magicFee=new HashMap<>();
                magicFee.put("TRADE_TYPE_CODE","3709");
                magicFee.put("FEE_TYPE_CODE","510");
                magicFee.put("FEE",mbhPrice);
                magicFee.put("OLDFEE",mbhPrice);
                magicFee.put("FEE_MODE","0");
                feeList.add(magicFee);
                totalPrice+=mbhPrice;
                //如果魔百盒是预存礼包 需增加预存费用
                String[] strs =broadbandFusionBean.getMbh().split(",");
                List<BroadbandItemEntity> mbhItemList = BroadbandUtils.convertMbhItemEntityList(getGoodsList());
                for(int i=0;i<strs.length;i++){
                    if(StringUtils.isNotBlank(strs[i])){
                        String[] magicStrs = strs[i].split("\\|");
                        if(magicStrs.length==3){
                            BroadbandItemEntity mbhTfGoodsInfo = new BroadbandItemEntity();
                            for(int j=0;j<mbhItemList.size();j++){
                                String mbhGoodsSkuId = String.valueOf(mbhItemList.get(j).getGoodsSkuId());
                                if(mbhGoodsSkuId.equals(magicStrs[2])){
                                    mbhTfGoodsInfo = mbhItemList.get(j);
                                    break;
                                }
                            }
                            Map<String,Object> magic=new HashMap<>();
                            magic.put("TV_PRODUCT_ID",magicStrs[0]);
                            magic.put("REQ_ELEMENT_ID",magicStrs[1]);
                            magic.put("GIFT_ELEMENT_ID",mbhTfGoodsInfo.getHeProductId());
                            magic.put("MAGIC_ID","D00"+(i+1));
                            MAGIC_BASE_INFO_LIST.add(magic);

                            Map<String,Object> magicGiftFee=new HashMap<>();
                            magicGiftFee.put("TRADE_TYPE_CODE","3709");
                            magicGiftFee.put("FEE_TYPE_CODE","1110");
                            magicGiftFee.put("FEE",mbhTfGoodsInfo.getPrice()*100);
                            magicGiftFee.put("OLDFEE",mbhTfGoodsInfo.getPrice()*100);
                            magicGiftFee.put("FEE_MODE","2");
                            magicGiftFee.put("ID","D00"+(i+1));

                            feeList.add(magicGiftFee);
                            totalPrice+=mbhTfGoodsInfo.getPrice()*100;
                        }else if(magicStrs.length==2){
                            Map<String,Object> magic=new HashMap<>();
                            magic.put("TV_PRODUCT_ID",magicStrs[0]);
                            magic.put("REQ_ELEMENT_ID",magicStrs[1]);
                            magic.put("MAGIC_ID","D00"+(i+1));
                            MAGIC_BASE_INFO_LIST.add(magic);
                        }
                    }
                }
                condition.setMagicBaseInfoList(MAGIC_BASE_INFO_LIST);
                o2oParamUtils.addParams("MAGIC_BASE_INFO_LIST", JSON.toJSONString(MAGIC_BASE_INFO_LIST).toString(), "魔百盒信息");
            }
            if("true".equals(broadbandFusionBean.getImsTabShow())){
                Map<String,Object> ims=new HashMap<>();
                ims.put("IMS_PHONE",broadbandFusionBean.getImsPhone());
                ims.put("IMSUSER_TYPE","1");
                List<Map<String,Object>> eleList=new ArrayList<>();
                Map<String,Object> elements=new HashMap<>();
                elements.put("ELEMENT_ID",broadbandFusionBean.getImsElementId());
                elements.put("ELEMENT_TYPE_CODE","D");
                elements.put("MODIFY_TAG","0");
                String startDate= DateUtils.formatDate(new Date());
                elements.put("START_DATE", startDate);
                elements.put("END_DATE","2050-12-31");
                elements.put("PRODUCT_ID",broadbandFusionBean.getImsProductId());
                elements.put("PACKAGE_ID",broadbandFusionBean.getImsPackageId());
                eleList.add(elements);
                ims.put("IMS_SELECTED_ELEMENTS",eleList);
                condition.setImsReqData(ims);
                o2oParamUtils.addParams("IMS_PHONE",broadbandFusionBean.getImsPhone(),"固话号码");
                o2oParamUtils.addParams("IMSUSER_TYPE","1","固话类型");
                o2oParamUtils.addParams("IMS_SELECTED_ELEMENTS",JSON.toJSONString(eleList).toString(),"固话信息");
            }
            o2oParamUtils.addParams("X_TRADE_FEESUB", JSON.toJSONString(feeList).toString(), "费用");
            condition.setXtradeFeesub(feeList);
            //支付方式
            List<Map<String,Object>> payList=new ArrayList<>();
            Map<String,Object> pay1=new HashMap<>();
            pay1.put("PAY_MONEY_CODE","0");
            pay1.put("MONEY",totalPrice);
            payList.add(pay1);
            condition.setXtradePaymoney(payList);
            o2oParamUtils.addParams("X_TRADE_PAYMONEY", JSON.toJSONString(payList).toString(), "支付方式");

            //有宽带 存量转入（存量不支持固话）
        }else if("0".equals(hasBroadband)) {
            condition.setIsExists("1");
            o2oParamUtils.addParams("IS_EXISTS", "1", "是否存量宽带用户转入");
            totalPrice = 0;
        }
        if(StringUtils.isNotEmpty(broadbandFusionBean.getMemberListStr())&&("2".equals(busiType)||"3".equals(busiType))){
            //成员信息
            List<Map<String,Object>> tabDataList=new ArrayList<>();
            for(String memberStr:broadbandFusionBean.getMemberListStr().split("\\|")){
                Map<String,Object> tabData=new HashMap<>();
                tabData.put("SERIAL_NUMBER_B", memberStr);
                tabData.put("SHARE_FLOW", "0");
                tabData.put("SHARE_VOICE", "0");
                tabData.put("tag", "0");
                tabDataList.add(tabData);
            }
            condition.setTabData(tabDataList);
            o2oParamUtils.addParams("TAB_DATA",JSON.toJSONString(tabDataList).toString(),"成员信息");
        }


        o2oParamUtils.addConditionChannel(condition,memberVo.getChannelInfo(),serialNumber);

        logger.info("broadbandFusionRegCondition:"+ JSON.toJSONString(condition));
        Map result = broadbandOrderService.fusionBroadbandReg(condition);
        logger.info("一单清校验接口返回结果： " + result);
        if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
            resMap.put("code", "1");
            resMap.put("message", " 受理中校验失败：【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+" message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
            return resMap;
        }
        //----增加受理中校验接口结束。。。
        if("2".equals(payMode)&&totalPrice>0){
            o2oParamUtils.addParams("IN_MODE_CODE", "P", "渠道交易类型");
        }
        String goodsFormat = broadbandItem.getBroadbandItemName() + "/" + broadbandItem.getTerm();

        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
        orderTempInfo.setOrder_type_id(Long.valueOf(orderType));
        orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
        orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
        orderTempInfo.setContact_phone(serialNumber);
        orderTempInfo.setEparchy_code(eparchyCode);
        orderTempInfo.setCounty(broadbandFusionBean.getCounty());
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
        o2oParamUtils.addChannelInfo( memberVo.getChannelInfo(),serialNumber);

        try{
            Long orderTempId = orderTempService.insert(orderTempInfo);
            logger.info("临时订单id： " + orderTempId);
            paramList = o2oParamUtils.getParamsList();
            for(O2oOrderParamTemp paramTemp:paramList){
                paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
            }
            orderParamTemService.batchInsert(paramList);
            orderTempInfo.setOrder_temp_id(orderTempId);
            resMap.put("orderTemp", orderTempInfo);
            resMap.put("code", "0");
            resMap.put("message", "生成订单成功");

            String name = URLEncoder.encode(orderTempInfo.getGoods_name(),"utf-8");
            name = URLEncoder.encode(name,"utf-8");
            String sucUrl ="/o2oBusiness/invokeSmsInterface?goodsName="+name+"&broadbandType="+orderType+"&serialNumber="+serialNumber+"&orderId="+orderTempId;
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

    /**
     * 判断号码是否办理宽带和魔百和
     * @return resMap
     * @return resultCode 返回编码
     * @return isBroadBand 是否办理宽带
     * @return isMbh 是否办理魔百和
     * @throws Exception
     */
    private Map<String,String> isMbhAndBroadBand(String installPhoneNum) throws Exception{
        Map<String,String> resMap = new HashMap<String,String>();
        HeFamilyCheckCondition infoCondition = new HeFamilyCheckCondition();
        infoCondition.setSerial_number(installPhoneNum);
        Map<String,Object> resultMap = heFamilyService.heFamilyCheck(infoCondition);
        JSONArray resultArray = JSONArray.parseArray(String.valueOf(resultMap.get("result")));
        JSONObject result = (JSONObject) resultArray.get(0);
        logger.error("判断号码是否办理宽带和魔百和接口返回串：" + result);
        resMap.put("checkResCode", (String)result.get("X_RESULTCODE"));
        resMap.put("isBroadBand", (String)result.get("BROADBAND_FLAG"));
        resMap.put("isMbh", (String)result.get("INTERACTIVE_FLAG"));
        return resMap;
    }

    private String queryFee(String SERIAL_NUMBER)throws Exception{
        HqQueryFeeCondition hqQueryFeeCondition = new HqQueryFeeCondition();
        hqQueryFeeCondition.setSERIAL_NUMBER(SERIAL_NUMBER);
        Map userFee = flowServeService.queryFee(hqQueryFeeCondition);
        logger.info("fee:"+ JSON.toJSONString(userFee));
        if(!HNanConstant.SUCCESS.equals(userFee.get("respCode"))|| MapUtils.isEmpty(userFee)){
            throw new ServiceException(HNanConstant.FAIL, userFee.get("respDesc").toString(), null);
        }
        return String.valueOf(((Map)(net.sf.json.JSONArray.fromObject(userFee.get("result"))).get(0)).get("NEW_ALLLEAVE_FEE"));
    }

    /**
     * 根据业务类型获取商品列表
     * @param busiType
     * @return
     * @throws Exception
     */
    private List<BroadbandItemEntity> getGoodsList(String busiType,String minCost) throws Exception{
        List<BroadbandItemEntity> itemList = null;
        Map<String,Object> bbArgs = new HashMap<>();
        if(BroadbandConstants.BROADBAND_BUSITYPE_HEFAMILY.equals(busiType)){
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_BROADBAND_CATEGORY_ID_NEW_FAMILY"));
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
            itemList = BroadbandUtils.convertNewHeBroadbandItemEntityList(goodsInfoList);
        }else {
            bbArgs = new HashMap<String,Object>();
            bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_CONSUPOSTN_BROADBAND_CATEGORY_ID"));
            List<TfGoodsInfo> goodsList = goodsManageService.queryBroadInfos(bbArgs);
            List<BroadbandItemEntity> consupostnAllItemList = BroadbandUtils.convertConsupostnItemEntityList(goodsList);
            String queryString="";
            if(BroadbandConstants.BROADBAND_BUSITYPE_CONS_PERSONAL.equals(busiType) || BroadbandConstants.BROADBAND_BUSITYPE_CONS_FAMILY.equals(busiType)){
                queryString="消费叠加型保底消费"+minCost;
            }else if(BroadbandConstants.BROADBAND_BUSITYPE_FAMILYNET.equals(busiType)){
                queryString="组家庭网送宽带";
            }
            itemList = new ArrayList<BroadbandItemEntity>();
            for (int i = 0; i < consupostnAllItemList.size(); i++) {
                if (consupostnAllItemList.get(i).getBroadbandItemName().contains(queryString)) {
                    itemList.add(consupostnAllItemList.get(i));
                }
            }
        }
        Collections.sort(itemList,new Comparator<BroadbandItemEntity>(){
            @Override
            public int compare(BroadbandItemEntity arg0, BroadbandItemEntity arg1) {
                return arg0.getBandWidth().compareTo(arg1.getBandWidth());
            }
        });
        return itemList;
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

    /**
     * 获取魔百和商品列表
     * @param
     * @return
     * @throws Exception
     */
    private List<TfGoodsInfo> getGoodsList() throws Exception{
        Map<String,Object> bbArgs = new HashMap<String,Object>();
        bbArgs.put("preCategoryId", dictService.getDictValue("BROADBAND_CATEGORY_ID","O2O_MBH_BROADBAND_CATEGORY_ID"));
        List<TfGoodsInfo> consupostnGoodsInfoList = goodsManageService.queryBroadInfos(bbArgs);

        return consupostnGoodsInfoList;
    }

}
