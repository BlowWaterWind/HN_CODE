package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.broadband.api.IBroadbandCommentService;
import com.ai.ecs.broadband.entity.BroadbandComment;
import com.ai.ecs.broadband.entity.BroadbandCommentQuestion;
import com.ai.ecs.broadband.entity.BroadbandCommentResult;
import com.ai.ecs.common.utils.IdGen;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.IUppHtmlValidataService;
import com.ai.ecs.ecm.mall.wap.platform.utils.BusiLogUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.DateUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.entity.order.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadbandOrderService;
import com.ai.ecs.ecsite.modules.broadBand.service.QryAddressService;
import com.ai.ecs.ecsite.modules.dobusiness.entity.HqChangeProdAndElemCondition;
import com.ai.ecs.ecsite.modules.dobusiness.service.FlowServeService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.ecsite.modules.recharge.entity.DqBuildSeqCondition;
import com.ai.ecs.ecsite.modules.recharge.entity.DqPaySuccFuncCondition;
import com.ai.ecs.ecsite.modules.recharge.service.RechargeService;
import com.ai.ecs.ecsmc.domain.ec.po.ShopInfo;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oOrderTempService;
import com.ai.ecs.o2o.entity.O2oOrderTempInfo;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.IOrderService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.*;
import com.ai.ecs.order.param.OrderProcessParam;
import com.ai.iis.upp.bean.MerChantBean;
import com.ai.iis.upp.util.UppCore;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Administrator on 2017/9/14.
 */
@Controller
@RequestMapping("o2oOrder")
public class O2oBandOrderController extends BaseController {

    @Autowired
    private O2oOrderTempService o2oOrderTempService;

    @Autowired
    private BroadBandService broadBandService;

    @Autowired
    private IGoodsManageService goodsManageService;

    @Autowired
    private IBroadbandCommentService broadbandCommentService;

    @Autowired
    QryAddressService qryAddressService;

    @Autowired
    BroadbandOrderService broadbandOrderService;

    @Autowired
    private PhoneAttributionService phoneAttributionService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUppHtmlValidataService validataService;

    @Autowired
    IOrderQueryService orderQueryService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private FlowServeService flowServeService;

    @Value("${afterOrderPayUrl}")
    String afterOrderPayUrl;


    private final String ChanId = "E004";                         //
    private final String StaffId  =  "ITFWAPNN";              //
    private final String TradeDepartPassword = "225071";
    private final String ELEMENT_ID_100 = "99541461";   //100M流量编码
    private final String ELEMENT_ID_500 = "99541463";	//500M流量编码

    /**
     * 查询全部订单
     *
     * @param
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("queryOrderTempList")
    public String queryOrderTempList(HttpServletRequest request, HttpServletResponse response, Model model, String flag,Long unfinishedNumber,Long finishedNumber,String phone) throws Exception {
        model.addAttribute("phone",phone);
        ShopInfo shopInfo = UserUtils.getLoginUser(request).getShopInfo();
        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
//        List<Integer> excludeOrderStatusIds = new ArrayList<Integer>();
//        excludeOrderStatusIds.add(1);
//        orderTempInfo.setExcludeOrderStatusIds(excludeOrderStatusIds);
        orderTempInfo.setOrder_status(0L);
        //过滤店铺
        orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));
        //处理失效订单
        o2oOrderTempService.updateTimeOutOrder(orderTempInfo);
        if(unfinishedNumber==null){
            unfinishedNumber = 0L;
        }
        if(finishedNumber==null){
            finishedNumber = 0L;
        }
        long unstartNumber = o2oOrderTempService.findO2oOrderTempInfoCount(orderTempInfo);
        List<Integer> excludeOrderPayStatusIds = new ArrayList<Integer>();
        excludeOrderPayStatusIds.add(0);
        excludeOrderPayStatusIds.add(2);
        excludeOrderPayStatusIds.add(3);
        excludeOrderPayStatusIds.add(4);
        orderTempInfo.setExcludeOrderStatusIds(excludeOrderPayStatusIds);
        orderTempInfo.setPay_mode_id(2);
        orderTempInfo.setOrder_status(null);
        long payNum = o2oOrderTempService.findO2oPayOrdeInfoCount(orderTempInfo);
        if (StringUtils.isBlank(flag)) {
            model.addAttribute("flag", "all");
        } else {
            model.addAttribute("flag", flag);
        }
        model.addAttribute("unfinishedNumber", unfinishedNumber);
        model.addAttribute("finishedNumber", finishedNumber);
        model.addAttribute("unstartNumber", unstartNumber);
        model.addAttribute("payNum",payNum);
        return "web/broadband/o2o/order/orderTempList2";
    }

    /**
     * 查询全部订单
     *
     * @param
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("queryOrderPayTempList")
    public String queryOrderPayTempList(HttpServletRequest request, HttpServletResponse response, Model model, String flag,Long unfinishedNumber,Long finishedNumber,String phone) throws Exception {
        model.addAttribute("phone",phone);
        ShopInfo shopInfo = UserUtils.getLoginUser(request).getShopInfo();
        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
//        List<Integer> excludeOrderStatusIds = new ArrayList<Integer>();
//        excludeOrderStatusIds.add(1);
//        orderTempInfo.setExcludeOrderStatusIds(excludeOrderStatusIds);
        orderTempInfo.setOrder_status(0L);
        //过滤店铺
        orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));
        //处理失效订单
        o2oOrderTempService.updateTimeOutOrder(orderTempInfo);
        if(unfinishedNumber==null){
            unfinishedNumber = 0L;
        }
        if(finishedNumber==null){
            finishedNumber = 0L;
        }
        long unstartNumber = o2oOrderTempService.findO2oOrderTempInfoCount(orderTempInfo);
        List<Integer> excludeOrderPayStatusIds = new ArrayList<Integer>();
        excludeOrderPayStatusIds.add(0);
        excludeOrderPayStatusIds.add(2);
        excludeOrderPayStatusIds.add(3);
        excludeOrderPayStatusIds.add(4);
        orderTempInfo.setExcludeOrderStatusIds(excludeOrderPayStatusIds);
        orderTempInfo.setPay_mode_id(2);
        orderTempInfo.setOrder_status(null);
        long payNum = o2oOrderTempService.findO2oPayOrdeInfoCount(orderTempInfo);

        if (StringUtils.isBlank(flag)) {
            model.addAttribute("flag", "all");
        } else {
            model.addAttribute("flag", flag);
        }
        model.addAttribute("unfinishedNumber", unfinishedNumber);
        model.addAttribute("finishedNumber", finishedNumber);
        model.addAttribute("unstartNumber", unstartNumber);
        model.addAttribute("payNum",payNum);
        return "web/broadband/o2o/order/orderPayList2";
    }

    @RequestMapping("queryOrderListOld")
    public String queryOrderListOld(HttpServletRequest request, HttpServletResponse response, Model model, String flag, String startTime, String endTime,String phone) throws Exception {
        if (StringUtils.isBlank(flag)) {
            model.addAttribute("flag", "all");
        } else {
            model.addAttribute("flag", flag);
        }

        //初始化时间
        Date curDate = new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(Calendar.MONTH,-1);
        //渠道版默认查当天（个人版默认查前一个月）
        if (StringUtils.isEmpty(startTime)) {
            startTime = DateUtils.formatDate(cal.getTime());
        }
        if (StringUtils.isBlank(endTime)) {
            endTime = DateUtils.formatDate(curDate);
        }
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("phone",phone);
        ShopInfo shopInfo = UserUtils.getLoginUser(request).getShopInfo();
        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
        List<Integer> excludeOrderStatusIds = new ArrayList<Integer>();
        excludeOrderStatusIds.add(1);
        orderTempInfo.setExcludeOrderStatusIds(excludeOrderStatusIds);
        //过滤店铺
        orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));

        long unstartNumber = o2oOrderTempService.findO2oOrderTempInfoCount(orderTempInfo);

        List<Integer> excludeOrderPayStatusIds = new ArrayList<Integer>();
        excludeOrderPayStatusIds.add(0);
        excludeOrderPayStatusIds.add(2);
        excludeOrderPayStatusIds.add(3);
        excludeOrderPayStatusIds.add(4);
        orderTempInfo.setExcludeOrderStatusIds(excludeOrderPayStatusIds);
        long payNum = o2oOrderTempService.findO2oPayOrdeInfoCount(orderTempInfo);
        model.addAttribute("unstartNumber", unstartNumber);
        model.addAttribute("payNum",payNum);
        if(StringUtils.isBlank(phone)){
            return "web/broadband/o2o/order/orderList";
        }
        BroadbandOrderListCondition condition=new BroadbandOrderListCondition();
        condition.setStartDate(startTime);
        condition.setEndDate(endTime);
        condition.setQryType("1");
        condition.setQryNumber(phone);
        condition.setRouteEparchyCode(getEparchyCode(String.valueOf(phone)));
        condition.setEparchyCodeFCust(condition.getRouteEparchyCode());
        LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder queryOrderList param：" + JSONObject.fromObject(condition).toString());
        Map map=broadbandOrderService.getBroadbandOrderList(condition);
        LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder queryOrderList result：" + map);
        List<BroadbandOrderResult> allList=new ArrayList<BroadbandOrderResult>();
        List<BroadbandOrderResult> finishList=new ArrayList<BroadbandOrderResult>();
        List<BroadbandOrderResult> unfinishList=new ArrayList<BroadbandOrderResult>();
        if("0".equals(map.get("respCode")) && map.get("result") != null){
            List<Map<String, String>> orderDataList = (List<Map<String, String>>) map.get("result");
            for (int i = orderDataList.size() - 1; i >= 0; i--) {
                Map<String, String> element = orderDataList.get(i);
                if(element.get("TRADE_ID")==null){
                    break;
                }
                BroadbandOrderResult broadbandOrderResult=new BroadbandOrderResult();
                broadbandOrderResult.setTradeId(element.get("TRADE_ID"));
                broadbandOrderResult.setSerialNumber(element.get("SERIAL_NUMBER"));
                broadbandOrderResult.setAccNbr(element.get("ACC_NBR"));
                broadbandOrderResult.setOfferName(element.get("OFFER_NAME"));
                broadbandOrderResult.setTradeOfferNum(element.get("TRADE_OFFER_NUM"));
                broadbandOrderResult.setTradeFee(element.get("TRADE_FEE"));
                broadbandOrderResult.setTradeStatus(element.get("TRADE_STATUS"));
                broadbandOrderResult.setCreateDate(element.get("CREATE_DATE"));
                broadbandOrderResult.setPreInstallDate(element.get("PRE_INSTALL_DATE"));
                broadbandOrderResult.setEparchyCode(element.get("EPARCHY_CODE"));
                broadbandOrderResult.setTradeTypeCode(element.get("TRADE_TYPE_CODE"));
                broadbandOrderResult.setTradeType(element.get("TRADE_TYPE"));
                allList.add(broadbandOrderResult);
                if("系统完工".equals(element.get("TRADE_STATUS"))){
                    finishList.add(broadbandOrderResult);
                }else{
                    unfinishList.add(broadbandOrderResult);
                }
            }
        }
        if("unfinished".equals(flag)){
            model.addAttribute("broadbandOrderList",unfinishList);
        }else if("finished".equals(flag)){
            model.addAttribute("broadbandOrderList",finishList);
        }else{
            model.addAttribute("broadbandOrderList",allList);
        }
        long unfinishedNumber = unfinishList.size();
        long finishedNumber = finishList.size();
        model.addAttribute("unfinishedNumber", unfinishedNumber);
        model.addAttribute("finishedNumber", finishedNumber);
        return "web/broadband/o2o/order/orderList";
    }


    @RequestMapping("queryOrderList")
    public String queryOrderList(HttpServletRequest request, HttpServletResponse response, Model model, String flag, String startTime, String endTime,String phone) throws Exception {
        if (StringUtils.isBlank(flag)) {
            model.addAttribute("flag", "all");
        } else {
            model.addAttribute("flag", flag);
        }

        //初始化时间
        Date curDate = new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(Calendar.MONTH,-1);
        //渠道版默认查当天（个人版默认查前一个月）
        if (StringUtils.isEmpty(startTime)) {
            startTime = DateUtils.formatDate(cal.getTime());
        }
        if (StringUtils.isBlank(endTime)) {
            endTime = DateUtils.formatDate(curDate);
        }
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("phone",phone);
        ShopInfo shopInfo = UserUtils.getLoginUser(request).getShopInfo();
        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
//        List<Integer> excludeOrderStatusIds = new ArrayList<Integer>();
//        excludeOrderStatusIds.add(1);
//        orderTempInfo.setExcludeOrderStatusIds(excludeOrderStatusIds);
        orderTempInfo.setOrder_status(0L);
        //过滤店铺
        orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));

        long unstartNumber = o2oOrderTempService.findO2oOrderTempInfoCount(orderTempInfo);

        List<Integer> excludeOrderPayStatusIds = new ArrayList<Integer>();
        excludeOrderPayStatusIds.add(0);
        excludeOrderPayStatusIds.add(2);
        excludeOrderPayStatusIds.add(3);
        excludeOrderPayStatusIds.add(4);
        orderTempInfo.setExcludeOrderStatusIds(excludeOrderPayStatusIds);
        orderTempInfo.setOrder_status(null);
        long payNum = o2oOrderTempService.findO2oPayOrdeInfoCount(orderTempInfo);
        model.addAttribute("unstartNumber", unstartNumber);
        model.addAttribute("payNum",payNum);
        if(StringUtils.isBlank(phone)){
            return "web/broadband/o2o/order/orderListNew2";
        }
        BroadbandOrderListCondition condition=new BroadbandOrderListCondition();
        condition.setStartDate(startTime);
        condition.setEndDate(endTime);
        condition.setQryType("1");
        condition.setQryNumber(phone);
        condition.setRouteEparchyCode(getEparchyCode(String.valueOf(phone)));
        condition.setEparchyCodeFCust(condition.getRouteEparchyCode());
        LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder queryOrderList param：" + JSONObject.fromObject(condition).toString());
        Map map=broadbandOrderService.getBroadbandOrderList(condition);
        LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder queryOrderList result：" + map);
        List<BroadbandOrderResult> allList=new ArrayList<BroadbandOrderResult>();
        List<BroadbandOrderResult> finishList=new ArrayList<BroadbandOrderResult>();
        List<BroadbandOrderResult> unfinishList=new ArrayList<BroadbandOrderResult>();
        if("0".equals(map.get("respCode")) && map.get("result") != null){
            List<Map<String, String>> orderDataList = (List<Map<String, String>>) map.get("result");
            for (int i = orderDataList.size() - 1; i >= 0; i--) {
                Map<String, String> element = orderDataList.get(i);
                if(element.get("TRADE_ID")==null){
                    break;
                }
                BroadbandOrderResult broadbandOrderResult=new BroadbandOrderResult();
                broadbandOrderResult.setTradeId(element.get("TRADE_ID"));
                broadbandOrderResult.setSerialNumber(element.get("SERIAL_NUMBER"));
                broadbandOrderResult.setAccNbr(element.get("ACC_NBR"));
                broadbandOrderResult.setOfferName(element.get("OFFER_NAME"));
                broadbandOrderResult.setTradeOfferNum(element.get("TRADE_OFFER_NUM"));
                broadbandOrderResult.setTradeFee(element.get("TRADE_FEE"));
                broadbandOrderResult.setTradeStatus(element.get("TRADE_STATUS"));
                broadbandOrderResult.setCreateDate(element.get("CREATE_DATE"));
                broadbandOrderResult.setPreInstallDate(element.get("PRE_INSTALL_DATE"));
                broadbandOrderResult.setEparchyCode(element.get("EPARCHY_CODE"));
                broadbandOrderResult.setTradeTypeCode(element.get("TRADE_TYPE_CODE"));
                broadbandOrderResult.setTradeType(element.get("TRADE_TYPE"));
                allList.add(broadbandOrderResult);
                if("系统完工".equals(element.get("TRADE_STATUS"))){
                    finishList.add(broadbandOrderResult);
                }else{
                    unfinishList.add(broadbandOrderResult);
                }
            }
        }
        if("unfinished".equals(flag)){
            model.addAttribute("broadbandOrderList",unfinishList);
        }else if("finished".equals(flag)){
            model.addAttribute("broadbandOrderList",finishList);
        }else{
            model.addAttribute("broadbandOrderList",allList);
        }
        long unfinishedNumber = unfinishList.size();
        long finishedNumber = finishList.size();
        model.addAttribute("unfinishedNumber", unfinishedNumber);
        model.addAttribute("finishedNumber", finishedNumber);

        String nowYear = com.ai.ecs.common.utils.DateUtils.getYear();
        String nextYear = String.valueOf((Integer.parseInt(nowYear)+1));
        String defaultValue = com.ai.ecs.common.utils.DateUtils.getDate() + " 8~12点" ;
        model.addAttribute("years",nowYear+","+nextYear);
        model.addAttribute("defaultValue",defaultValue);
        return "web/broadband/o2o/order/orderListNew2";
    }


    /**
     * 查询安装进度
     * @param request
     * @param broadbandOrder
     * @param model
     * @return
     */
    @RequestMapping("/queryWorkSheet")
    public String queryWorkSheet(HttpServletRequest request , BroadbandOrderResult broadbandOrder,Model model) throws Exception {
        if ("1002".equals(broadbandOrder.getTradeTypeCode())) {
            //再查安装进度
            QueryWorkSheetCondition qCodition = new QueryWorkSheetCondition();
            qCodition.setAccNbr(broadbandOrder.getAccNbr());
            qCodition.setRoutEparchyCode(broadbandOrder.getEparchyCode());
            qCodition.setEparchyCodeFCust(broadbandOrder.getEparchyCode());
            qCodition.setCityCode(broadbandOrder.getEparchyCode());
            qCodition.setTradeId(broadbandOrder.getTradeId());
            LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder orderDetail param：" + JSONObject.fromObject(qCodition).toString());
            Map resultMap = null;
//            qCodition.setStaffId("ITFWPMAL");
//            qCodition.setTradeDepartPassword("ai1234");
            resultMap = broadbandOrderService.queryWorkSheet(qCodition);

            LoggerFactory.getLogger("webDbLog").info("====broadbandOrder orderDetail result：" + resultMap);
            if (resultMap!=null && "0".equals(resultMap.get("respCode")) && resultMap.get("result") != null) {
                JSONArray array = (JSONArray) resultMap.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(1);
                JSONArray tradeInfoArray = resultObj.getJSONArray("TRADE");
                if (tradeInfoArray != null) {
                    List<Map<String, Object>> tradeInfoList = new ArrayList<>();
                    for (int i = 0; i < tradeInfoArray.size(); i++) {
                        tradeInfoList.add((Map) tradeInfoArray.getJSONObject(i));
                        if("在线预约".equals(tradeInfoList.get(i).get("name"))||"现场开通".equals(tradeInfoList.get(i).get("name"))){
                            model.addAttribute("handleName",tradeInfoList.get(i).get("handleName"));
                            model.addAttribute("handlePhone",tradeInfoList.get(i).get("phone"));
                        }
                    }
                    Collections.reverse(tradeInfoList);
                    model.addAttribute("tradeInfoList", tradeInfoList);
                }
            }
        }
        model.addAttribute("broadbandOrder",broadbandOrder);

        return "web/broadband/o2o/order/queryWorkSheet";
    }

    /**
     * 滑动分页
     *
     * @param request
     * @param response
     * @param flag
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryOrderListByPage")
    public Page<O2oOrderTempInfo> queryMyOrderByPage(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     String flag) {
        ShopInfo shopInfo = UserUtils.getLoginUser(request).getShopInfo();
        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
//        List<Integer> excludeOrderStatusIds = new ArrayList<Integer>();
//        excludeOrderStatusIds.add(1);
//        orderTempInfo.setExcludeOrderStatusIds(excludeOrderStatusIds);
        orderTempInfo.setOrder_status(0L);
        //过滤店铺
        orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));

        LoggerFactory.getLogger("webDbLog").info("shopId:"+orderTempInfo.getShop_id());
        Calendar cal=Calendar.getInstance();
        Date curDate=new Date();
        Page<O2oOrderTempInfo> page = o2oOrderTempService.findO2oOrderTempInfoPage(new Page<O2oOrderTempInfo>(request, response), orderTempInfo);
        LoggerFactory.getLogger("webDbLog").info("========================================O2oOrderTempInfoList"+page.getList());
        if (page.getList() != null && page.getList().size() > 0) {
            for (O2oOrderTempInfo info : page.getList()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("containGoodsStaticInfo", true);
                map.put("containShopGoodsChannelRef", true);
                map.put("goodsSkuId", info.getGoods_sku_id());
                map.put("chnlCode", "E007");
                List<TfGoodsInfo> goods = goodsManageService.queryGoodsInfoByCds(map);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(goods)) {
                    try {
                        if (goods.size() > 0) {
                            info.setDesc(goods.get(0).getGoodsShortDesc());
                            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(goods.get(0).getGoodsStaticList()) && org.apache.commons.lang3.StringUtils.isNotEmpty(goods.get(0).getGoodsStaticList().get(0).getGoodsStaticUrl())) {
                                info.setGoodsImageUrl(goods.get(0).getGoodsStaticList().get(0).getGoodsStaticUrl());
                            }
                        }
                    } catch (Exception e) {
                        logger.error("填充商品路径错误：", e);
                    }
                }
                cal.setTime(info.getInsert_time());
                cal.add(Calendar.DATE,1);
                if(cal.getTime().before(curDate)){
                    info.setDifferTime("-1");
                }else{
                    info.setDifferTime(DateUtils.differTimes(info.getInsert_time()));
                }
            }
        }
        return page;
    }

    /**
     * 待支付订单列表滑动分页
     *
     * @param request
     * @param response
     * @param flag
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryOrderPayListByPage")
    public Page<O2oOrderTempInfo> queryOrderPayListByPage(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     String flag) {
        ShopInfo shopInfo = UserUtils.getLoginUser(request).getShopInfo();
        O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
        List<Integer> excludeOrderStatusIds = new ArrayList<Integer>();
        excludeOrderStatusIds.add(0);
        excludeOrderStatusIds.add(2);
        excludeOrderStatusIds.add(3);
        excludeOrderStatusIds.add(4);
        orderTempInfo.setExcludeOrderStatusIds(excludeOrderStatusIds);
        orderTempInfo.setPay_mode_id(2);
        //过滤店铺
        orderTempInfo.setShop_id(Long.parseLong(shopInfo.getShopId()));

        LoggerFactory.getLogger("webDbLog").info("shopId:"+orderTempInfo.getShop_id());
        Calendar cal=Calendar.getInstance();
        Date curDate=new Date();
        Page<O2oOrderTempInfo> page = o2oOrderTempService.findO2oOrderPayTempInfoPage(new Page<O2oOrderTempInfo>(request, response), orderTempInfo);
        LoggerFactory.getLogger("webDbLog").info("========================================O2oOrderTempInfoList"+page.getList());
        if (page.getList() != null && page.getList().size() > 0) {
            for (O2oOrderTempInfo info : page.getList()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("containGoodsStaticInfo", true);
                map.put("containShopGoodsChannelRef", true);
                map.put("goodsSkuId", info.getGoods_sku_id());
                map.put("chnlCode", "E007");
                List<TfGoodsInfo> goods = goodsManageService.queryGoodsInfoByCds(map);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(goods)) {
                    try {
                        if (goods.size() > 0) {
                            info.setDesc(goods.get(0).getGoodsShortDesc());
                            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(goods.get(0).getGoodsStaticList()) && org.apache.commons.lang3.StringUtils.isNotEmpty(goods.get(0).getGoodsStaticList().get(0).getGoodsStaticUrl())) {
                                info.setGoodsImageUrl(goods.get(0).getGoodsStaticList().get(0).getGoodsStaticUrl());
                            }
                        }
                    } catch (Exception e) {
                        logger.error("填充商品路径错误：", e);
                    }
                }
                cal.setTime(info.getInsert_time());
                cal.add(Calendar.DATE,1);
                if(cal.getTime().before(curDate)){
                    info.setDifferTime("-1");
                }else{
                    info.setDifferTime(DateUtils.differTimes(info.getInsert_time()));
                }
            }
        }
        return page;
    }
    /**
     * 查看订单详情
     *
     * @param broadbandOrder
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="orderDetail",method =RequestMethod.POST)
    public String orderDetail(HttpServletRequest request , BroadbandOrderResult broadbandOrder,Model model) throws Exception {
        ShopInfo shopInfo = UserUtils.getLoginUser(request).getShopInfo();
        model.addAttribute("shopName", shopInfo.getShopName());
        try {
            List<BroadbandComment> broadbandComments = broadbandCommentService.getBroadbandCommentByOrderId(broadbandOrder.getTradeId());
            if(broadbandComments!=null && !broadbandComments.isEmpty() && broadbandComments.size()==1){
                model.addAttribute("broadbandComment",broadbandComments.get(0));
            }
            if ("1002".equals(broadbandOrder.getTradeTypeCode()) && (!"系统完工".equals(broadbandOrder.getTradeStatus()))) {//宽带新装
                NewBroadBandDetailCondition detailCondition = new NewBroadBandDetailCondition();
                detailCondition.setEparchyCode(broadbandOrder.getEparchyCode());
                detailCondition.setTradeId(broadbandOrder.getTradeId());
                LoggerFactory.getLogger("webDbLog").info("=======o2oOrder orderDetail param：" + JSONObject.fromObject(detailCondition).toString());
                Map detailMap = broadbandOrderService.getBroadbandOrderDetail(detailCondition);
                LoggerFactory.getLogger("webDbLog").info("====o2oOrder orderDetail result：" + detailMap);
                if ("0".equals(detailMap.get("respCode")) && detailMap.get("result") != null) {
                    JSONArray arrayDetail = (JSONArray) detailMap.get("result");
                    com.alibaba.fastjson.JSONObject resultObjDetail = arrayDetail.getJSONObject(0);
                    broadbandOrder.setCustName(resultObjDetail.getString("CUST_NAME"));
                    broadbandOrder.setAddrDesc(resultObjDetail.getString("ADDR_DESC"));
                    broadbandOrder.setRecvDate(resultObjDetail.getString("RECV_DATE"));
                    broadbandOrder.setPreInstallDate(resultObjDetail.getString("BOOK_INSTALL_DATE"));
                }
            } else {
                //非新装或新装完工宽带查宽带信息查询接口
                if (StringUtils.isBlank(broadbandOrder.getSerialNumber())) {
                    BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
                    condition.setSn(broadbandOrder.getAccNbr());
                    condition.setRoute_code(broadbandOrder.getEparchyCode());
                    BroadbandInfo broadbandInfo = broadBandService.broadBandInfoQuery(condition);
                    if (broadbandInfo != null) {
                        broadbandOrder.setCustName(broadbandInfo.getCustName());
                        broadbandOrder.setAddrDesc(broadbandInfo.getInstallAddress());
                    }
                } else {
                    BroadbandDetailInfoCondition condition = new BroadbandDetailInfoCondition();
                    condition.setSerialNumber(String.valueOf(broadbandOrder.getSerialNumber()));
                    LoggerFactory.getLogger("webDbLog").info(" o2oOrder broadbandDetailInfo param：" + JSONObject.fromObject(condition).toString());
                    BroadbandDetailInfoResult result = null;
                    try {
                        result = broadBandService.broadbandDetailInfo(condition);
                    } catch (Exception e) {
                        logger.error("宽带订单查看详情异常:" + e.getMessage());
                        throw new Exception("系统错误，请稍后再试");
                    }
                    LoggerFactory.getLogger("webDbLog").info(" o2oOrder broadbandDetailInfo result：" + JSONObject.fromObject(result).toString());
                    if (result != null) {
                        BroadbandDetailInfo info = result.getBroadbandDetailInfo();
                        broadbandOrder.setCustName(info.getCustName());
                        broadbandOrder.setAddrDesc(info.getAddrDesc());
                    }
                }
            }
            if ("1002".equals(broadbandOrder.getTradeTypeCode())) {
                //再查安装进度
                QueryWorkSheetCondition qCodition = new QueryWorkSheetCondition();
                qCodition.setAccNbr(broadbandOrder.getAccNbr());
                qCodition.setRoutEparchyCode(broadbandOrder.getEparchyCode());
                qCodition.setEparchyCodeFCust(broadbandOrder.getEparchyCode());
                qCodition.setCityCode(broadbandOrder.getEparchyCode());
                qCodition.setTradeId(broadbandOrder.getTradeId());
                LoggerFactory.getLogger("webDbLog").info("=======o2oOrder orderDetail param：" + JSONObject.fromObject(qCodition).toString());
                Map resultMap = null;
                resultMap = broadbandOrderService.queryWorkSheet(qCodition);
                LoggerFactory.getLogger("webDbLog").info("====o2oOrder orderDetail result：" + resultMap);
                if ("0".equals(resultMap.get("respCode")) && resultMap.get("result") != null) {
                    JSONArray array = (JSONArray) resultMap.get("result");
                    com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(1);
                    JSONArray tradeInfoArray = resultObj.getJSONArray("TRADE");
                    if (tradeInfoArray != null) {
                        List<Map<String, Object>> tradeInfoList = new ArrayList<>();
                        for (int i = 0; i < tradeInfoArray.size(); i++) {
                            tradeInfoList.add((Map) tradeInfoArray.getJSONObject(i));
                        }
                        Collections.reverse(tradeInfoList);
                        model.addAttribute("tradeInfoList", tradeInfoList);
                    }
                }
            }
            //查询日志
            BroadBandLogCondition condition=new BroadBandLogCondition();
            condition.setSerialNumber(broadbandOrder.getSerialNumber());
            condition.setRouteEparchyCode(broadbandOrder.getEparchyCode());
            Map logMap = broadbandOrderService.getBroadBandLog(condition);
            LoggerFactory.getLogger("webDbLog").info("====o2oOrder BroadBandLog result：" + logMap);
            if ("0".equals(logMap.get("respCode")) && logMap.get("result") != null) {
                JSONArray array = (JSONArray) logMap.get("result");
                List<Map<String, Object>> logList = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    if(StringUtils.isNotBlank(array.getJSONObject(i).getString("ACCEPT_TAG"))){
                        logList.add((Map) array.getJSONObject(i));
                    }
                }
//                Collections.reverse(tradeInfoList);
                model.addAttribute("logList", logList);
            }

            //撤单透明化
            CBroadbandQryOrderCondition cBroadbandQryOrderCondition = new CBroadbandQryOrderCondition();
            cBroadbandQryOrderCondition.setTradeId(broadbandOrder.getTradeId());
            cBroadbandQryOrderCondition.setEparchyCode(broadbandOrder.getEparchyCode());
            cBroadbandQryOrderCondition.setRouteEparchyCode(broadbandOrder.getEparchyCode());
            Map cancelMap =broadbandOrderService.cBroadbandQryOrder(cBroadbandQryOrderCondition);
            LoggerFactory.getLogger("webDbLog").info("====broadbandOrder cBroadbandQryOrder result：" + cancelMap);
            if ("0".equals(cancelMap.get("respCode")) && cancelMap.get("result") != null) {
                JSONArray array = (JSONArray) cancelMap.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                JSONArray retArray = resultObj.getJSONArray("RET");
                if (retArray != null) {
                    List<Map<String, Object>> retList = new ArrayList<>();
                    for (int i = 0; i < retArray.size(); i++) {
                        retList.add((Map) retArray.getJSONObject(i));
                    }
                    model.addAttribute("retList", retList);
                }
            }
        } catch (Exception e) {
            logger.error("宽带订单查看详情异常:" + e.getMessage());
            throw new Exception("系统错误，请稍后再试");
        }
        model.addAttribute("broadbandOrder",broadbandOrder);

        String nowYear = com.ai.ecs.common.utils.DateUtils.getYear();
        String nextYear = String.valueOf((Integer.parseInt(nowYear)+1));
        String defaultValue = com.ai.ecs.common.utils.DateUtils.getDate() + " 8~12点" ;
        model.addAttribute("years",nowYear+","+nextYear);
        model.addAttribute("defaultValue",defaultValue);
        model.addAttribute("broadbandOrder",broadbandOrder);
        LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder orderDetail ===============：" + JSONObject.fromObject(broadbandOrder).toString());
        return "web/broadband/o2o/order/newOrderDetail";
    }


    /**
     * 取消订单
     *
     * @param request
     * @param response
     * @param orderId
     * @return
     */
    @RequestMapping(value = "cancelOrder")
    @ResponseBody
    public Map<String, Object> cancelOrder(HttpServletRequest request,
                                           HttpServletResponse response, String orderId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "取消订单失败！");
        if (StringUtils.isNotBlank(orderId)) {
            O2oOrderTempInfo order = new O2oOrderTempInfo();
            order.setOrder_temp_id(Long.parseLong(orderId));
            order.setOrder_status(3L);
            o2oOrderTempService.updateByPrimaryKeySelective(order);
            resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "取消订单成功！");
        }
        return resultMap;
    }

    /**
     * 再次派单
     *
     * @param request
     * @param response
     * @param orderId
     * @return
     */
    @RequestMapping(value = "dispatchAgain")
    @ResponseBody
    public Map<String, Object> dispatchAgain(HttpServletRequest request,
                                             HttpServletResponse response, String orderId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "派单失败！");
        if (StringUtils.isBlank(orderId)) {

        }
        return resultMap;
    }


    /**
     * 催单
     *
     * @param request
     * @param response
     * @param
     * @return
     */
    @RequestMapping(value = "remind")
    @ResponseBody
    public Map<String, Object> remind(HttpServletRequest request,
                                      HttpServletResponse response, String tradeId, String serialNumber, String cityCode) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "提交失败，请稍后再试！");
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            return resultMap;
        }
        BroadBandRemindCondition condition = new BroadBandRemindCondition();
        condition.setEparchyCode(cityCode);
        condition.setSerialNumber(serialNumber);//宽带帐号
        condition.setTradeId(tradeId);
        Map<String, Object> map = null;
        LoggerFactory.getLogger("webDbLog").info("=======o2oOrder remind param：" + JSONObject.fromObject(condition).toString());
        try {
            map = broadbandOrderService.broadBandRemind(condition);
            LoggerFactory.getLogger("webDbLog").info("=======o2oOrder remind result：" + map);
            resultMap.put(BroadbandConstants.RESPONSE_DESC, map.get("respDesc"));
            resultMap.put(BroadbandConstants.RESPONSE_CODE, map.get("respCode"));
            if ("0".equals(map.get("respCode"))||"-1".equals(map.get("respCode"))) {
//                JSONArray array = (JSONArray) map.get("result");
//                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
//                resultMap.put(BroadbandConstants.RESPONSE_CODE, resultObj.get("X_RESULTCODE"));
//                if ("0".equals(resultObj.get("X_RESULTCODE"))||"-1".equals(resultObj.get("X_RESULTCODE"))) {
                resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
                resultMap.put(BroadbandConstants.RESPONSE_DESC, "催单成功！");
//                } else {
//                    resultMap.put(BroadbandConstants.RESPONSE_DESC, resultObj.get("X_RESULTINFO"));
//                }
            }
        } catch (Exception e) {
            logger.error("宽带订单催单异常:" + e.getMessage());
            resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "系统错误，请稍后再试！");
        }
        return resultMap;
    }

    /**
     * 修改预约时间
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "modifyInstallTime")
    @ResponseBody
    public Map<String, Object> modifyInstallTime(HttpServletRequest request, String tradeId, String bookSession, String bookDate, String serialNumber, String bandAccount) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "提交失败，请稍后再试！");
        String addrId=null;
        String orderId=null;
        MemberVo member = UserUtils.getLoginUser(request);
        if (member == null) {
            return resultMap;
        }
        try {
            if(StringUtils.isBlank(serialNumber)){
                resultMap.put(BroadbandConstants.RESPONSE_DESC, "手机号码为空，改约失败！");
                return resultMap;
            }
            //开户信息查询接口查询addrId
            QryToChgInfoCondition qryToChgInfoCondition=new QryToChgInfoCondition();
            qryToChgInfoCondition.setChgOrderType("1002");
            qryToChgInfoCondition.setConSN(serialNumber);
            LoggerFactory.getLogger("webDbLog").info("=======o2oOrder getOdrToChgInfo param：" + JSONObject.fromObject(qryToChgInfoCondition).toString());
            Map qMap=broadbandOrderService.getOdrToChgInfo(qryToChgInfoCondition);
            LoggerFactory.getLogger("webDbLog").info("=======o2oOrder getOdrToChgInfo result：" + qMap);
            if("0".equals(qMap.get("respCode")) && qMap.get("result") != null) {
                JSONArray qArray = (JSONArray) qMap.get("result");
                com.alibaba.fastjson.JSONObject resultObjQ = qArray.getJSONObject(0);
                addrId=resultObjQ.getString("ADDR_ID");
                orderId=resultObjQ.getString("ORDER_ID");
            }else{
                resultMap.put(BroadbandConstants.RESPONSE_DESC, "开户单查询信息为空");
                return resultMap;
            }
        ChkScheduleChgCondition condition = new ChkScheduleChgCondition();
        condition.setSerialNumber(serialNumber);
        condition.setBookSession(bookSession);
        condition.setBookDate(bookDate);
        condition.setTradeId(orderId);
        condition.setAddrId(addrId);
        Map<String, Object> map = null;
        LoggerFactory.getLogger("webDbLog").info("=======o2oOrder chkPreChgBySn param：" + JSONObject.fromObject(condition).toString());

            map = broadbandOrderService.chkPreChgBySn(condition);
            LoggerFactory.getLogger("webDbLog").info("=======o2oOrder chkPreChgBySn result：" + map);
            resultMap.put(BroadbandConstants.RESPONSE_DESC, map.get("respDesc"));
            logger.info("====modifyInstallTime result：" + map);
            if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                JSONArray array = (JSONArray) map.get("result");
                com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                if ("0".equals(resultObj.get("RES_CODE"))) {
                    //校验通过，调办理接口
                    UpdScheduleBySnCondition updScheduleBySnCondition = new UpdScheduleBySnCondition();
                    updScheduleBySnCondition.setBookDate(bookDate);
                    updScheduleBySnCondition.setBookSession(bookSession);
                    updScheduleBySnCondition.setSerialNumber(serialNumber);
                    updScheduleBySnCondition.setOrderId(orderId);//宽带帐号
                    updScheduleBySnCondition.setTradeId(tradeId);//订单号
                    updScheduleBySnCondition.setAddrId(addrId);
                    updScheduleBySnCondition.setAccessAcct(bandAccount);//宽带帐号
                    LoggerFactory.getLogger("webDbLog").info("=======o2oOrder updScheduleBySn param：" + JSONObject.fromObject(updScheduleBySnCondition).toString());
                    Map<String, Object> mapUp = broadbandOrderService.updScheduleBySn(updScheduleBySnCondition);
                    LoggerFactory.getLogger("webDbLog").info("=======o2oOrder updScheduleBySn result：" + mapUp);
                    resultMap.put(BroadbandConstants.RESPONSE_DESC, mapUp.get("respDesc"));
                    if ("0".equals(mapUp.get("respCode")) && mapUp.get("result") != null) {
                        JSONArray arrayUp = (JSONArray) mapUp.get("result");
                        com.alibaba.fastjson.JSONObject resultObjUp = arrayUp.getJSONObject(0);
                        if ("0".equals(resultObjUp.get("RES_CODE"))) {//修改成功
                            resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
                            resultMap.put(BroadbandConstants.RESPONSE_DESC, resultObjUp.get("RES_DESC"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "接口调用异常");
            logger.error("宽带订单修改预约时间异常:" + e.getMessage());
        }
        return resultMap;
    }

    /**
     * 查看是否已经评价
     * @param request
     * @param response
     * @param orderId
     * @return
     */
    @RequestMapping(value = "toComment")
    @ResponseBody
    public Map<String,Object> toComment(HttpServletRequest request, HttpServletResponse response, String orderId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
//        List<BroadbandCommentResult> result=null;
//        if (StringUtils.isNotBlank(orderId)) {
//            result=broadbandCommentService.getCommentResultByOrderId(orderId);
//            resultMap.put("list",result);
//        }
        List<BroadbandComment> broadbandComments = broadbandCommentService.getBroadbandCommentByOrderId(orderId);
        if(broadbandComments!=null && !broadbandComments.isEmpty() && broadbandComments.size()==1){
            resultMap.put("broadbandComment",broadbandComments.get(0));
        }
        return resultMap;
    }

    /**
     * 提交评价 -- 1 、未进行分享  2、已经进行分享 ，在评价 送100M流量
     * @param request
     * @return
     */
    @RequestMapping("/submitNewComment")
    @ResponseBody
    public Map<String,Object> submitNewComment(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        String orderId = request.getParameter("tradeId");
        String resultScore = request.getParameter("resultScore");
        List<BroadbandComment> broadbandComments = broadbandCommentService.getBroadbandCommentByOrderId(orderId);
        if(broadbandComments!=null && broadbandComments.size()==1){
            BroadbandComment broadbandComment = broadbandComments.get(0);
            broadbandComment.setResultScore(Integer.parseInt(resultScore));
            broadbandCommentService.updateBroadbandCommentByOrderId(broadbandComment);
        }else{
            BroadbandComment broadbandComment = new BroadbandComment();
            broadbandComment.setResultId(IdGen.uuid());
            broadbandComment.setResultScore(Integer.parseInt(resultScore));
            broadbandComment.setShareNum(0);
            broadbandComment.setIsSendFlow(0);
            broadbandComment.setOrderId(orderId);
            broadbandComment.setResultTime(new Date());
            broadbandCommentService.insertBroadbandComment(broadbandComment);
        }
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "恭喜您，评价提交成功！");
        return resultMap;
    }

    /**
     * 提交分享 -- 1、只分享，未进行评价。2 、已进行评价，分享 ，送流量
     * @return
     */
    @RequestMapping("/shareSubmit")
    @ResponseBody
    public Map<String,Object> shareSubmit(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        String orderId = request.getParameter("tradeId");
        Long serialNumber = UserUtils.getLoginUser(request).getMemberLogin().getMemberPhone();
        List<BroadbandComment> broadbandComments = broadbandCommentService.getBroadbandCommentByOrderId(orderId);
        if(broadbandComments!=null && broadbandComments.size()==1){
            BroadbandComment broadbandComment = broadbandComments.get(0);
            int shareNum = broadbandComment.getShareNum();
            broadbandComment.setShareNum(shareNum+1);
            int isSendFlow = broadbandComment.getIsSendFlow();
            String sendFlow = broadbandComment.getSendFlow();
            broadbandComment.setSendFlowTime(new Date());
            int resultScore = broadbandComment.getResultScore();

            if(isSendFlow==0 && StringUtils.isBlank(sendFlow) && resultScore==5){
                HqChangeProdAndElemCondition hqChangeProdAndElemCondition = new HqChangeProdAndElemCondition();
                hqChangeProdAndElemCondition.setStaffId(StaffId);
                hqChangeProdAndElemCondition.setTradeDepartPassword(TradeDepartPassword);
                hqChangeProdAndElemCondition.setInModeCode("L");
                hqChangeProdAndElemCondition.setChanId(ChanId);
                hqChangeProdAndElemCondition.setSERIAL_NUMBER(String.valueOf(serialNumber));
                hqChangeProdAndElemCondition.setNUM(1);
                hqChangeProdAndElemCondition.setELEMENT_TYPE_CODE("D");
                hqChangeProdAndElemCondition.setMODIFY_TAG("0");
                hqChangeProdAndElemCondition.setPRODUCT_ID("");
                hqChangeProdAndElemCondition.setREMOVE_TAG("0");
                hqChangeProdAndElemCondition.setELEMENT_ID(ELEMENT_ID_100);
                hqChangeProdAndElemCondition.setDISCNT(ELEMENT_ID_100);
                Map<String, Object> ajaxData1 = null;
                try {
                    ajaxData1 = flowServeService.changeProdAndElem(hqChangeProdAndElemCondition);
                    if(MapUtils.isEmpty(ajaxData1) || !ajaxData1.get("respCode").equals("0")){
                        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
                        resultMap.put(BroadbandConstants.RESPONSE_DESC, ajaxData1.get("respDesc").toString());
                        return resultMap;
                    }
                } catch (Exception e) {
                    logger.error("宽带订单赠送流量异常:" + e.getMessage());
                    resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
                    resultMap.put(BroadbandConstants.RESPONSE_DESC, "提交失败，请稍后再试！");
                    return resultMap;
                }
                System.out.println("===========赠送流量结果返回=============="+ajaxData1);

                if(!MapUtils.isEmpty(ajaxData1) && ajaxData1.get("respCode").equals("0")){//流量赠送成功
                    broadbandComment.setIsSendFlow(1);
                    broadbandComment.setSendFlow(ELEMENT_ID_100);
                    broadbandCommentService.updateBroadbandCommentByOrderId(broadbandComment);
                }
            }else{
                broadbandCommentService.updateBroadbandCommentByOrderId(broadbandComment);
            }
        }else{
            BroadbandComment broadbandComment = new BroadbandComment();
            broadbandComment.setResultId(IdGen.uuid());
            broadbandComment.setShareNum(1);
            broadbandComment.setIsSendFlow(0);
            broadbandComment.setOrderId(orderId);
            broadbandComment.setResultTime(new Date());
            broadbandComment.setResultScore(0);
            broadbandCommentService.insertBroadbandComment(broadbandComment);
        }

        resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "恭喜您，分享提交成功！");
        return resultMap;
    }

    @RequestMapping(value = "submitComment")
    @ResponseBody
    public Map<String, Object> submitComment(HttpServletRequest request, BroadbandCommentQuestion question, String orderId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "提交失败，请稍后再试！");
        try {
            MemberVo member = UserUtils.getLoginUser(request);
            Long memberId = null;
            if (member != null) {
                memberId = member.getMemberLogin().getMemberId();
            }
            Date date = new Date();
            if (question != null && question.getCommentResults() != null && question.getCommentResults().size() > 0) {
                for (BroadbandCommentResult result : question.getCommentResults()) {
                    result.setMemberId(memberId);
                    result.setResultTime(date);
                    result.setOrderId(orderId);
                }
                broadbandCommentService.saveBatchCommentResult(question.getCommentResults());
                resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
                resultMap.put(BroadbandConstants.RESPONSE_DESC, "恭喜您，评价提交成功！");
            }
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "系统异常！");
            logger.error("宽带订单提交评论异常:" + e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "queryAddr", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryAddr(HttpServletRequest request, String tradeId, String cityCode, String installTime) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
        resultMap.put(BroadbandConstants.RESPONSE_DESC, "该订单不能改约");
        List<String> bookInstallDateList = new ArrayList<>();
        Date installDate=DateUtils.StringToDateTime(installTime);
        Date curDate=new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(installDate);
        try {

            NewBroadBandDetailCondition detailCondition=new NewBroadBandDetailCondition();
            detailCondition.setEparchyCode(cityCode);
            detailCondition.setTradeId(tradeId);
            LoggerFactory.getLogger("webDbLog").info("=======o2oOrder orderDetail param：" + JSONObject.fromObject(detailCondition).toString());
            Map detailMap=broadbandOrderService.getBroadbandOrderDetail(detailCondition);
            resultMap.put(BroadbandConstants.RESPONSE_DESC, detailMap.get("respDesc"));
            LoggerFactory.getLogger("webDbLog").info("====o2oOrder orderDetail result：" + detailMap);
            if("0".equals(detailMap.get("respCode")) && detailMap.get("result") != null){
                JSONArray arrayDetail = (JSONArray) detailMap.get("result");
                com.alibaba.fastjson.JSONObject resultObjDetail = arrayDetail.getJSONObject(0);
                QryAddressAttrCondition condition = new QryAddressAttrCondition();
                condition.setADDR_ID(resultObjDetail.getString("ADDR_ID"));
                Map map = qryAddressService.queryAddressAttr(condition);
                LoggerFactory.getLogger("webDbLog").info("myBroadband  queryAddr resultMap" + map.toString());
                resultMap.put(BroadbandConstants.RESPONSE_DESC, map.get("respDesc"));
                if ("0".equals(map.get("respCode")) && map.get("result") != null) {
                    JSONArray array = (JSONArray) map.get("result");
                    com.alibaba.fastjson.JSONObject resultObj = array.getJSONObject(0);
                    if ("0".equals(resultObj.getString("X_RESULTCODE"))) {
                        resultMap.put(BroadbandConstants.RESPONSE_CODE, "0");
                        if ("01".equals(resultObj.getString("COMMUNITY_TYPE"))) {//农村
                            cal.add(Calendar.HOUR,-72);
                            if(curDate.before(cal.getTime())){//可以改约 改约开始时间为当前时间往后推72小时
                                bookInstallDateList.add(DateUtils.getDateAfterDays(4));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(5));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(6));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(7));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(8));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(9));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(10));
                            }else{
                                resultMap.put(BroadbandConstants.RESPONSE_DESC,"农村预约时间必须提前三天才能改约,您的预约时间为"+installTime);
                            }
                        } else {
                            cal.add(Calendar.HOUR,-48);
                            if(curDate.before(cal.getTime())){//可以改约 改约开始时间为当前时间往后推/48小时
                                bookInstallDateList.add(DateUtils.getDateAfterDays(3));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(4));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(5));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(6));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(7));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(8));
                                bookInstallDateList.add(DateUtils.getDateAfterDays(9));
                            }else{
                                resultMap.put(BroadbandConstants.RESPONSE_DESC,"城市预约时间必须提前两天才能改约,您的预约时间为"+installTime);
                            }
                        }
                        resultMap.put("bookInstallDateList", bookInstallDateList);
                    }
                }
            }
        } catch (Exception e) {
            resultMap.put(BroadbandConstants.RESPONSE_CODE, "-1");
            resultMap.put(BroadbandConstants.RESPONSE_DESC, "系统错误");
        }
        return resultMap;
    }
    @RequestMapping("payList")
    public String payList(Model model,Long orderId){
        O2oOrderTempInfo orderTempInfo =o2oOrderTempService.selectByPrimaryKey(orderId);
//        orderTempInfo.setGoods_pay_price(orderTempInfo.getGoods_pay_price()/100);
        model.addAttribute("orderTempInfo",orderTempInfo);
        return "web/broadband/o2o/order/payList";
    }
    @RequestMapping("payResult")
    public String payResult(Model model){
        return "web/broadband/o2o/order/payResult";
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
     * 订单支付
     * @param request
     * @param response
     * @param
     * @return
     */
    @RequestMapping("payOrder")
    public String payOrder(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,Model model, Long orderTempId, String payPlatform) throws Exception{
        try {
            O2oOrderTempInfo orderTempInfo = o2oOrderTempService.selectByPrimaryKey(orderTempId);
            Map<String, Object> resultMap = orderService.mergeOrder(orderTempInfo.getOrderSubNo());
            TfOrder order = (TfOrder) resultMap.get("order");
            //获取交易流水号 获取关联订单

            List<TfOrderSub> orderSubList = (List<TfOrderSub>) resultMap.get("orderSubList");
            TfOrderSub orderSub = orderSubList.get(0);
            if(orderSub==null){
                throw new Exception("找不到该订单！");
            }
            //查询是否已经支付成功  应该要从统一支付查 接口未提供
            TfOrderPay orderPay = orderService.checkPayFromBoss(orderSub);
            if (orderPay != null && StringUtils.isNotBlank(orderPay.getPayState())) {
                //订单支付信息
                orderPay.setPayLogId(orderPay.getPayNumber());
                orderPay.setPayNo(orderPay.getPayNumber());



                //订单流转
                OrderProcessParam param = new OrderProcessParam();
                param.setBusinessId(String.valueOf(orderSub.getOrderId()));
                param.setOrderStatusId(OrderConstant.STATUS_PAY);
                if (OrderConstant.PAY_STATE_SUCCESS.equals(orderPay.getPayState())) {
                    param.setAct(1);
                    //同步订单状态
                    paySucc(orderPay,orderSub);
                } else {
                    param.setAct(0);
                }
                param.put(Variables.ORDER_PAY, orderPay);
                orderService.completeByOrderId(param);
                throw new Exception("请不要重复支付！");
            }


            // 结算价格
            Long orderAmount = Long.valueOf(order.getOrderAmount() + "");

            // 同步页面回调地址
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
            String callbackUrl = basePath + "/o2oOrder/toPayResult";
            // 异步页面回调地址
            String notifyCallbackUrl = afterOrderPayUrl + "/o2oOrder/payAfterNotify";
//            String callbackUrl="http://15.15.20.154:8080/shop-wap/o2oOrder/toPayResult";
//            String notifyCallbackUrl="http://15.15.20.154:8080/shop-wap/o2oOrder/payAfterNotify";
            orderPay = orderPay==null?new TfOrderPay():orderPay;
            String orderNo = null;
            DqBuildSeqCondition dqBuildSeqCondition = new DqBuildSeqCondition();
            Map resultDataOrderNoMap = rechargeService.getOrderNo(dqBuildSeqCondition);
            logger.info("=======payOrder getOrderNo result：" + resultDataOrderNoMap);
            if (null != resultDataOrderNoMap && "0".equals(resultDataOrderNoMap.get("respCode"))) {
                Map OrderNoData = (Map) ((List) resultDataOrderNoMap.get("result")).get(0);
                orderNo = (String) OrderNoData.get("SEQ");
                if (StringUtils.isNotBlank(orderNo)) {
                    orderPay.setPayNumber(orderNo);
                }
            }
            if(StringUtils.isBlank(orderPay.getPayNumber())){
                throw new Exception("获取支付流水失败！请稍后再试！");
            }
            orderPay.setOrderId(order.getOrderId());
            orderPay.setOrderPayAmount(orderAmount);
            orderPay.setOrgCode(payPlatform);
            orderPay.setHmac(BroadbandConstants.KEY);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setMerchantId(BroadbandConstants.MERCHANTID);
            orderPay.setProductType(BroadbandConstants.PRODUCT_TYPE);
            orderPay.setGift(0L);
            orderPay.setMerActivityID("");
            orderPay.setPaymentLimit("");
            orderPay.setProductURL("http://wap.hn.10086.cn/shop/");
            orderPay.setPayType(BroadbandConstants.PAY_TYPE);
            orderPay.setSerialNumber(orderTempInfo.getContact_phone());
            orderPay.setiDType("01");//中国移动用户标识类型 01：表示11位手机号 02：邮箱 03：固话 04：宽带  05：物联网号码
            orderPay.setIvrMobile(orderTempInfo.getContact_phone());
            orderPay.setProductId(String.valueOf(orderTempInfo.getGoods_sku_id()));
            orderPay.setProductName(orderTempInfo.getGoods_name());
            orderPay.setProductDesc(orderTempInfo.getGoods_name());
            orderPay.setOrderChannelCode(orderTempInfo.getChannel_code());
            orderPay.setPayChannel(orderTempInfo.getChannel_code());
            orderPay.setVersion("2.0.0");
            orderPay.setType(OrderConstant.PayOrgInterfaceType.getInterfaceType(orderPay.getOrgCode().replace("-","_")));
            orderPay.setOrderPayPerson(orderTempInfo.getContact_phone());
            orderPay.setOrderPayTime(new Date());
            orderService.mergeOrderPay(orderPay);
            String orderDate = DateFormatUtils.format(new Date(), "yyyyMMdd");
            modelMap.put("callbackUrl", callbackUrl);//页面返回 URL
            modelMap.put("notifyUrl", notifyCallbackUrl); //后台通知URL
            modelMap.put("merchantId", orderPay.getMerchantId());//商户编号
            modelMap.put("type", OrderConstant.PayOrgInterfaceType.getInterfaceType(orderPay.getOrgCode().replace("-","_")));//接口类型
            modelMap.put("version", "2.0.0"); // 版本号
            modelMap.put("channelId",OrderConstant.PayChannelCode.getPayChannel(orderPay.getOrderChannelCode()));// 渠道标志
            modelMap.put("payType", orderPay.getPayType()); // 支付类型：1-充值缴费;2-终端类订单;3-宽带订单支付;4-号卡;5-一体化平台;6-流量
            modelMap.put("period", "30");// 有效期数量
            modelMap.put("periodUnit", "00");// 有效期单位
            modelMap.put("amount", String.valueOf(orderPay.getOrderPayAmount())); // 支付金额  金额单位为分
            modelMap.put("orderDate", orderDate); //订单提交日期 格式：YYYYMMDD
            modelMap.put("merchantOrderId", String.valueOf(orderPay.getOrderId())); // 商户订单号

            modelMap.put("OrderNo", orderPay.getPayNumber());
            modelMap.put("merAcDate", orderDate);//商户会计日期   按格式 YYYYMMDD 可以与订单提交日期保持一致
            modelMap.put("productDesc", orderPay.getProductDesc());//商品描述
            modelMap.put("productId", String.valueOf(orderPay.getProductId()));//商品编号
            modelMap.put("productName", orderPay.getProductName());//商品名称
            modelMap.put("reserved1", "");//保留字段
            modelMap.put("reserved2", "");//保留字段
            modelMap.put("payOrg", orderPay.getOrgCode()); //支付机构
            modelMap.put("authorizeMode", "WAP"); // 推荐用户进行确认的方式 :推荐用户进行确认的方式  WEB/WAP
            modelMap.put("mobile", orderPay.getSerialNumber());
            modelMap.put("Gift", String.valueOf(orderPay.getGift()));
            modelMap.put("MerActivityID", orderPay.getMerActivityID());
            modelMap.put("PaymentLimit", orderPay.getPaymentLimit());
            modelMap.put("ProductURL", orderPay.getProductURL());
            modelMap.put("IDType", orderPay.getiDType());
            modelMap.put("IvrMobile", orderPay.getIvrMobile());
            modelMap.put("ProductType", orderPay.getProductType());
            modelMap.put("hmac",UppCore.getHmac(modelMap, orderPay.getHmac(), "UTF-8")); //签名数据 Key
//            modelMap.put("requestPayUrl", "http://2g081036f0.imwork.net:54358/upay/payOrder.html");
//            modelMap.put("requestPayUrl", "http://20311v6e20.imwork.net:46323/upay/payOrder.html");
            modelMap.put("requestPayUrl", "http://www.hn.10086.cn/upay/payOrder.html");

            LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder payOrder param：" +modelMap.toString());
        } catch (Exception e) {
            logger.error("宽带支付订单异常:" + e.getMessage());
//            e.printStackTrace();
            throw e;
        }
        return "web/broadband/o2o/order/payForm";
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
    @RequestMapping("toPayResult")
    public String toPayResult(HttpServletRequest request,
                              HttpServletResponse response, Model model, String returnCode,
                              Long chargeflowId) throws Exception {
        logger.info("宽带订单,支付同步回调参数:returnCode=" + returnCode
                + ",chargeflowId=" + chargeflowId);
//        TfOrderSub orderSubParam = new TfOrderSub();
//        orderSubParam.setOrderId(chargeflowId);
//        orderSubParam.setPage(new Page<TfOrderSub>(1, 100));
//        Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
//        List<TfOrderSub> orderSubList = orderSubPage.getList();
//        if (orderSubList != null && orderSubList.size() > 0) {
//            TfOrderSub orderSub = orderSubList.get(0);
//            if (orderSub.getDetailList() != null
//                    && orderSub.getDetailList().size() > 0) {
//                TfOrderSubDetail orderSubDetail = orderSub.getDetailList().get(
//                        0);
//                model.addAttribute("orderSubDetail", orderSubDetail);
//            }
//        }
        LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder toPayResult：" +"宽带订单,支付同步回调参数:returnCode=" + returnCode
                + ",chargeflowId=" + chargeflowId);

        model.addAttribute("returnCode", returnCode);
        model.addAttribute("orderId",chargeflowId);
        return "web/broadband/o2o/order/payResult";
    }

    /**
     * 跳转到订单支付结果页面，供支付中心调用（异步）
     * @return
     * @throws Exception
     */
    @RequestMapping("payAfterNotify")
    public void payAfterNotify(HttpServletRequest request,String merchantId ,String returnCode,String message,String type,Long amount,Long orderId ,String payDate,
                               String status,String payNo,String org_code,
                               String organization_payNo,String accountDate) throws Exception {
        TfOrderPay orderPay=new TfOrderPay();
        orderPay.setOrderId(orderId);
        orderPay=orderQueryService.queryOrderPay(orderPay);
        try{
            logger.info("宽带订单,支付异步回调参数:"+request.getParameterMap());
            LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder payAfterNotify parammap：" +request.getParameterMap());
            MerChantBean merChantBean = validataService.querybyMerchantId(merchantId);
            Map<String, String> payParamMap = UppCore.getHashMapParam(request.getParameterMap());
            if(!validataService.valHmac(payParamMap,merChantBean)){
                throw new Exception("宽带支付回调:签名验证未通过");
            }

            TfOrderSub orderSubParam = new TfOrderSub();
            orderSubParam.setOrderId(orderId);
            orderSubParam.setPage(new Page<TfOrderSub>(1,100));
            Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
            List<TfOrderSub> orderSubList = orderSubPage.getList();
            TfOrderSub orderSub=null;
            if(orderSubList!=null && orderSubList.size()>0 ){
                orderSub = orderSubList.get(0);
                if(StringUtils.isNotBlank(accountDate)&&orderSub.getDetailList()!=null && orderSub.getDetailList().size()>0){
                    TfOrderSubDetail orderSubDetail = orderSub.getDetailList().get(0);
                    TfOrderSubDetailBusiParam orderSubDetailBusiParam = new TfOrderSubDetailBusiParam();
                    orderSubDetailBusiParam.setSkuBusiParamName("DEAL_TIME");
                    orderSubDetailBusiParam.setSkuBusiParamValue(accountDate);
                    orderSubDetailBusiParam.setSkuBusiParamDesc("会计日期");
                    orderSubDetailBusiParam.setOrderSubDetailId(orderSubDetail.getOrderSubDetailId());
                    List<TfOrderSubDetailBusiParam> busiParamList  = Lists.newArrayList();
                    busiParamList.add(orderSubDetailBusiParam);
                    orderService.saveBusinessParam(busiParamList);
                }
            }
            //订单支付信息
            orderPay.setMerchantId(merchantId);
            orderPay.setMessage(message);
            orderPay.setOrderHarvestExpend("0");
            orderPay.setOrderPayAmount((long)amount);
            if(StringUtils.isNotBlank(payDate)){
                if(payDate.length()==8){
                    orderPay.setSettleDate(org.apache.commons.lang3.time.DateUtils.parseDate(payDate, "yyyyMMdd"));
                }else if(payDate.length()==14){
                    orderPay.setSettleDate(org.apache.commons.lang3.time.DateUtils.parseDate(payDate, "yyyyMMddHHmmss"));
                }
            }
            orderPay.setOrgCode(org_code);
            orderPay.setPayLogId(payNo);
            orderPay.setPayNo(payNo);
            orderPay.setPayState(status);
            orderPay.setReturnCode(returnCode);
            orderPay.setType(type);


            //订单流转
            OrderProcessParam param = new OrderProcessParam();
            param.setBusinessId(String.valueOf(orderId));
            param.setOrderStatusId(OrderConstant.STATUS_PAY);
            if (BroadbandConstants.RETURN_CODE_SUCCESS.equals(returnCode) && OrderConstant.PAY_STATE_SUCCESS.equals(status)) {
                param.setAct(1);
                //同步订单状态
                if(orderSub!=null){
                    paySucc(orderPay,orderSub);
                }
            } else {
                param.setAct(0);
            }
            param.put(Variables.ORDER_PAY, orderPay);
            orderService.completeByOrderId(param);
        }
        catch(Exception e){
            logger.error("宽带订单支付异步回调失败，异常信息"+e);
            BusiLogUtils.writerLogging(request,"payAfterNotify","","","","","","","",e,"2","");
        }
    }

    @RequestMapping("bossSyn")
    @ResponseBody
    public Map bossSyn(Model model,HttpServletRequest request,Long orderTempId){
        Map result  = new HashMap();
        result.put("respCode","-1");
        result.put("respMsg","同步失败");
        if(orderTempId==null){
            result.put("respMsg","临时订单编号为空！");
            return result;
        }
        try{
            O2oOrderTempInfo orderTempInfo =  o2oOrderTempService.selectByPrimaryKey(orderTempId);
            TfOrderSub tfOrderSub = new TfOrderSub();
            tfOrderSub.setOrderSubNo(orderTempInfo.getOrderSubNo());
            tfOrderSub =  orderQueryService.queryBaseOrder(tfOrderSub);
             result = o2oOrderTempService.bossSynOrder(tfOrderSub);
        }catch (Exception e){
            result.put("respMsg","BOSS同步失败！");
        }
        return result;

    }

    @RequestMapping("checkPayState")
    @ResponseBody
    public Map checkPayState(Model model,HttpServletRequest request,Long orderId){
        TfOrderSub orderSubParam = new TfOrderSub();
        orderSubParam.setOrderId(orderId);
        orderSubParam.setPage(new Page<TfOrderSub>(1,10));
        Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
        List<TfOrderSub> orderSubList = orderSubPage.getList();
        TfOrderSub orderSub=new TfOrderSub();
        if(orderSubList!=null && orderSubList.size()>0 ) {
             orderSub = orderSubList.get(0);
        }
        Map result  = new HashMap();
        result.put("returnCode","-1");
        result.put("status","未支付");
        try{
            TfOrderPay orderPay=orderService.checkPayFromBoss(orderSub);
            if(orderPay!=null&&OrderConstant.PAY_STATE_SUCCESS.equals(orderPay.getPayState())){
                result.put("returnCode",BroadbandConstants.RETURN_CODE_SUCCESS);
                result.put("status",OrderConstant.PAY_STATE_SUCCESS);
            }else if(orderPay!=null&&OrderConstant.PAY_STATE_FAILED.equals(orderPay.getPayState())){
                result.put("status",OrderConstant.PAY_STATE_FAILED);
            }
        }catch (Exception e){
            result.put("status","查询异常");
        }
        return result;

    }

    private void paySucc(TfOrderPay orderPay,TfOrderSub orderSub){
        String tradeId="";
        String eparchyCode="";
        List<TfOrderSubDetailBusiParam> paramList = orderQueryService.queryBusiParamByOrderSub(orderSub.getOrderSubId());
        Map<String, Object> paramMap = new HashMap<>();
        for (TfOrderSubDetailBusiParam tfOrderSubDetailBusiParam : paramList) {
            paramMap.put(tfOrderSubDetailBusiParam.getSkuBusiParamName(),
                    tfOrderSubDetailBusiParam.getSkuBusiParamValue());
        }
        tradeId = (String) paramMap.get("TRADE_ID");
        eparchyCode=(String)paramMap.get("ROUTE_EPARCHY_CODE");

        //同步订单状态
        DqPaySuccFuncCondition dqPaySuccFuncCondition = new DqPaySuccFuncCondition();
        dqPaySuccFuncCondition.setAMOUNT(String.valueOf(orderPay.getOrderPayAmount()));
        dqPaySuccFuncCondition.setTRADE_ID(tradeId);
        dqPaySuccFuncCondition.setROUTE_EPARCHY_CODE(eparchyCode);
        dqPaySuccFuncCondition.setPEER_ORDER_ID(orderPay.getPayNumber());//支付流水
        dqPaySuccFuncCondition.setSERIAL_NUMBER(orderPay.getSerialNumber());
        String payMoneyCode = "";
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("ALIPAY")!=-1){
            payMoneyCode="A";
        }
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("WEIXIN")!=-1){
            payMoneyCode="W";
        }
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("UNIONPAY")!=-1){
            payMoneyCode="U";
        }
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("CMPAY")!=-1){
            payMoneyCode="C";
        }
        dqPaySuccFuncCondition.setPAY_MONEY_CODE(payMoneyCode);
        LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder payAfterNotify paySuccFunc：" + JSONObject.fromObject(dqPaySuccFuncCondition).toString());
        try {
            for (int i = 0; i <= 2; i++) {
                Map resultDataMap = rechargeService.paySuccFunc(dqPaySuccFuncCondition);
                LoggerFactory.getLogger("webDbLog").info("=======broadbandOrder payAfterNotify paySuccFunc：" + resultDataMap);
                orderPay.setSynCode((String) resultDataMap.get("respCode"));
                orderPay.setSynDesc((String) resultDataMap.get("respDesc"));
                if (null != resultDataMap && "0".equals(resultDataMap.get("respCode"))) {
                    break;
                } else {
                    Thread.sleep(4000);
                }
            }
        }catch (Exception e){
            LoggerFactory.getLogger("webDbLog").error("修改boss订单状态，异常信息"+e);
        }
    }

    @RequestMapping("payDetail")
    public String payDetail(HttpServletRequest request,Model model,Long orderId)throws Exception{
        TfOrderPay orderPay=new TfOrderPay();
        if(orderId==null){
            throw new Exception("订单号不能为空！");
        }
        orderPay.setOrderId(orderId);
        orderPay=orderQueryService.queryOrderPay(orderPay);
        if(orderPay==null){
            throw new Exception("找不到该订单！");
        }
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("ALIPAY")!=-1){
            orderPay.setOrgName("支付宝支付");
        }
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("WEIXIN")!=-1){
            orderPay.setOrgName("微信支付");
        }
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("UNIONPAY")!=-1){
            orderPay.setOrgName("银联支付");
        }
        if(StringUtils.isNotBlank(orderPay.getOrgCode()) && orderPay.getOrgCode().lastIndexOf("CMPAY")!=-1){
            orderPay.setOrgName("和包支付");
        }
        TfOrderSub orderSubParam = new TfOrderSub();
        orderSubParam.setOrderId(orderId);
        orderSubParam.setPage(new Page<TfOrderSub>(1,10));
        Page<TfOrderSub> orderSubPage = orderQueryService.queryComplexOrderPage(orderSubParam);
        List<TfOrderSub> orderSubList = orderSubPage.getList();
        TfOrderSub orderSub=null;
        if(orderSubList!=null && orderSubList.size()>0 ){
            orderSub = orderSubList.get(0);
            List<TfOrderSubDetail> detaillist = orderSub.getDetailList();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(detaillist)) {
                for (TfOrderSubDetail detail : detaillist) {
                    orderSub.setGoodsSkuId(detail.getGoodsSkuId());
                    orderSub.setGoodsName(detail.getGoodsName());
                }
            }
            model.addAttribute("orderSub",orderSub);
        }
        //订单支付信息
        model.addAttribute("orderPay",orderPay);
        return "web/broadband/o2o/order/orderPayDetail";
    }
}
