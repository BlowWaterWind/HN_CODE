package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.common.utils.StringUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.constant.BroadbandConstants;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.AmountUtils;
import com.ai.ecs.ecm.mall.wap.modules.goods.utils.BroadbandUtils;
import com.ai.ecs.ecm.mall.wap.modules.o2o.util.O2oParamUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.modules.broadBand.entity.*;
import com.ai.ecs.ecsite.modules.broadBand.service.BroadBandService;
import com.ai.ecs.ecsite.modules.myMobile.entity.PhoneAttributionModel;
import com.ai.ecs.ecsite.modules.phoneAttribution.service.PhoneAttributionService;
import com.ai.ecs.goods.api.IGoodsCommService;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.BroadbandItemEntity;
import com.ai.ecs.goods.entity.goods.TfGoodsBusiParam;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsSku;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.api.IMemberLoginService;
import com.ai.ecs.member.entity.ChannelInfo;
import com.ai.ecs.member.entity.MemberLogin;
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
import com.ai.ecs.order.entity.TfOrderSubDetailBusiParam;
import com.ai.ecs.order.entity.TfOrderUserRef;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 宽带续费
 * Created by think on 2017/9/16.
 */
@RequestMapping("o2oRenew")
@Controller
public class O2oLinkToRenewController {
    private final static org.slf4j.Logger localLogger = LoggerFactory.getLogger(O2oLinkToRenewController.class);
    @Autowired
    private BroadBandService broadBandService;
    @Autowired
    private IMemberAddressService memberAddressService;
    @Autowired
    private PhoneAttributionService phoneAttributionService;
    @Autowired
    private BroadBandService broadBandServiceImpl;
    @Autowired
    private IGoodsCommService goodsCommService;
    @Autowired
    private IGoodsManageService goodsManageService;
    @Autowired
    private IMemberLoginService memberLoginService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private O2oOrderTempService orderTempService;

    @Autowired
    private O2oOrderParamTemService orderParamTemService;

    @Autowired
    private O2oParamUtils o2oParamUtils;


    private static org.slf4j.Logger logger =  LoggerFactory.getLogger("webDbLog");

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
            + "wxyz0123456789+/" + "=";

    @RequestMapping("renewIndex")
    public String renewIndex(HttpServletRequest request,Model model){
//        try {
            //查询湖南省下面的全部地市
            List<ThirdLevelAddress> cityList = memberAddressService.getChildrensByPId("190000");
            model.addAttribute("cityList",cityList);

                return "web/goods/broadband/renew/queryAccount";
    }

    /**
     * 跳转到宽带查询列表页面
     * @param session
     * @param num
     * @param numType
     * @param cityCode
     * @return
     */
    @RequestMapping("linkToAccountQueryList")
    public String linkToAccountQueryList(HttpServletRequest request,  HttpSession session,
                                         String num, String numType,String cityCode,Model model){
        try {
            BroadbandDetailInfoCondition condition1 = new BroadbandDetailInfoCondition();
            condition1.setSerialNumber(String.valueOf(num));
            BroadbandDetailInfoResult result  = broadBandService.broadbandDetailInfo(condition1);
           if (result == null || result.getBroadbandDetailInfo() == null) {
               request.setAttribute("message","查询不到此宽带账户信息!");
               return renewIndex(request,model);
            }else{
               //有宽带进入宽带帐号选择页面
               BroadbandDetailInfo broadbandDetailInfo = result.getBroadbandDetailInfo();
               //有宽带进入宽带帐号选择页面
               BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
               condition.setRoute_code(cityCode);
               condition.setSn(broadbandDetailInfo.getAccessAcct());
               BroadbandInfo broadbandInfo = broadBandService.broadBandInfoQuery(condition);
               if(broadbandInfo==null){
                   request.setAttribute("message","查询不到宽带账户信息!");
                   return renewIndex(request,model);
               }
               else {
                   broadbandInfo.setEparchyCode(cityCode);
                   if("0".equals(broadbandInfo.getResultTag())){
                       request.setAttribute("message","此账户不能续费!");
                       return renewIndex(request,model);
                   }
               }
               session.setAttribute("broadbandInfo",broadbandInfo);
           }
            model.addAttribute("phoneName",num);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "web/broadband/o2o/renew/accountQueryList";
    }

    /**
     * 跳转到宽带续费页面
     * @param model
     * @return
     */
    @RequestMapping("linkToBroadBandRenew")
    public String linkToBroadBandRenew(HttpSession session,HttpServletRequest request,Model model){

        try {
            String broadbandAccount = request.getParameter("broadbandAccount");
            String cityCode = request.getParameter("cityCode");
            BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
            condition.setSn(broadbandAccount);
            condition.setRoute_code(cityCode);
            BroadbandInfo broadbandInfo = broadBandServiceImpl.broadBandInfoQuery(condition);
            //绑定手机号
            String phoneNum = request.getParameter("phoneNum");
            if(phoneNum!=null){
                MemberVo memberVo= UserUtils.getLoginUser(request);
                MemberLogin memberLogin = memberVo.getMemberLogin();
                Long userPhoneNum =  memberLogin.getMemberPhone();
                if(userPhoneNum==null){
                    //关联手机号码
                    String password = memberLogin.getMemberPassword();
                    memberLogin.setMemberPhone(Long.valueOf(phoneNum));
                    memberLogin.setMemberPassword(null);
                    boolean flag =  memberLoginService.updatememberLogin(memberLogin);
                    memberLogin.setMemberPassword(password);
                    //关联成功,更新缓存
                    if(flag){
                        UserUtils.updateMemberVoCache(memberVo);
                    }
                }
            }
            //单宽带信息
            Map<String,Object> bbArgs = Maps.newHashMap();
            bbArgs.put("preCategoryId", goodsCommService.getGoodsPropetyValue("BROADBAND_CATEGORY_ID_CONTINUE_PAY",null));
            List<String> eparchyCodes = Lists.newArrayList(broadbandInfo.getEparchyCode());
            bbArgs.put("eparchyCodes",eparchyCodes);
            bbArgs.put("orderCloumn","SKUATTR.PROD_ATTR_VALUE_ID");
            bbArgs.put("orderType","ASC");
            bbArgs.put("goodsStatusId",4);
            bbArgs.put("chnlCode","E007");
            List<TfGoodsInfo> goodsInfoList = goodsManageService.queryBroadInfos(bbArgs);
            String s = JSONArray.toJSONString(goodsInfoList);
            System.out.println(s);
            List<BroadbandItemEntity> broadbandItemList = null;
            broadbandItemList = BroadbandUtils.convertBroadbandItemList(goodsInfoList);
            model.addAttribute("broadbandItemList",broadbandItemList);
            model.addAttribute("broadbandInfo",broadbandInfo);
            model.addAttribute("phoneNum",phoneNum);
        } catch (Exception e) {
            logger.error("单宽带续费,跳转到宽带续费页面异常:"+e.getMessage());
            e.printStackTrace();
        }
        return "web/broadband/o2o/renew/broadbandRenew";
    }

    /**
     * 跳转到确定套餐页面
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("linkToConfirmPackage")
    public String linkToConfirmPackage(HttpServletRequest request, HttpServletResponse response, Model model, BroadbandInfo broadbandInfo) throws Exception{
        String goodsSkuId = request.getParameter("goodsSkuId");
        String phoneNum = request.getParameter("phoneNum");
        //宽带商品信息
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("goodsSkuId", goodsSkuId);
        map.put("containGoodsSkuIdInfo",true);
        map.put("containGoodsBusiParam",true);
        map.put("phoneNum",phoneNum);
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
        BroadbandItemEntity broadbandItem = null;
        try {
            broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));

        } catch (Exception e) {
            logger.error("宽带续费确认订单:"+e.getMessage());
        }

        model.addAttribute("broadbandItem",broadbandItem);
        model.addAttribute("broadbandInfo",broadbandInfo);
        return "web/broadband/o2o/renew/confirmPackage";
    }

    /**
     * 宽带续费提交订单
     * @param session
     * @param model
     */
    @RequestMapping("submitOrder")
    @ResponseBody
    public  Map<String,Object>  submitOrder(HttpServletRequest request,HttpSession session,Model model){
        Map<String,Object> resMap  = Maps.newHashMap();
        try {
            localLogger.info("提交续费订单!");
            //宽带信息
            String eparchyCode = request.getParameter("eparchyCode");
            String broadbandAccount = request.getParameter("broadbandAccount");
            String phoneName =  request.getParameter("phoneNum");
            //工号密码
            String staffPwd = request.getParameter("staffPwd");//预约安装日期段
            staffPwd = TriDes.getInstance().strDec(staffPwd, keyStr, null, null);
            BroadBandInfoQueryCondition condition = new BroadBandInfoQueryCondition();
            condition.setRoute_code(eparchyCode);
            condition.setSn(broadbandAccount);
            BroadbandInfo broadbandInfo = broadBandService.broadBandInfoQuery(condition);
            localLogger.info("broadbandInfo:"+JSON.toJSONString(broadbandInfo));
            String goodsSkuId = request.getParameter("goodsSkuId");
            //宽带商品信息
            Map<String,Object> map =Maps.newHashMap();
            map.put("goodsSkuId", goodsSkuId);
            map.put("containGoodsSkuIdInfo",true);
            map.put("containGoodsBusiParam",true);
            List<TfGoodsInfo> goodsInfoList = goodsManageService.queryGoodsInfoByCds(map);
            localLogger.info("goodsInfoList:"+JSON.toJSONString(goodsInfoList));
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
            BroadbandItemEntity broadbandItem = BroadbandUtils.convertBroadbandItemInfo(goodsInfoList.get(0));
            localLogger.info("broadbandItem:"+JSON.toJSONString(broadbandItem));
            long goodsSkuPrice = Long.parseLong(AmountUtils.changeY2F(broadbandItem.getPrice()+""));
            String goodsFormat = "宽带续费:"+broadbandItem.getBandWidth()+"/"+broadbandItem.getTerm()+"|"+broadbandAccount+"|"+eparchyCode;

            //设置订单明细业务参数信息
//            O2oParamUtils o2oParamUtils = new O2oParamUtils();
            List<O2oOrderParamTemp> busiParamList = new ArrayList<O2oOrderParamTemp>();
            BroadbandRenewCondition broadbandRenewCondition = new BroadbandRenewCondition();
            o2oParamUtils.removeParams();
            o2oParamUtils.addParams("serialNumber",broadbandInfo.getBroadbandAccount(),"宽带账号");
            o2oParamUtils.addParams("FEE", String.valueOf(goodsSkuPrice),"应缴");
            o2oParamUtils.addParams("FEE_MODE","2","FEE_MODE");
            o2oParamUtils.addParams("FEE_TYPE_CODE","1122","FEE_TYPE_CODE");
            o2oParamUtils.addParams("PAY", String.valueOf(goodsSkuPrice),"实缴");
            o2oParamUtils.addParams("TRADE_TYPE_CODE","1116","续费年包费类型编码");
            o2oParamUtils.addParams("TRADE_DEPART_PASSWD", staffPwd, "渠道交易部门密码");
//            o2oParamUtils.addParams("X_TRADE_PAYMONEY", "0", "支付方式");
            broadbandRenewCondition.setSerialNumber(broadbandInfo.getBroadbandAccount());
            broadbandRenewCondition.setTradeTypeCode("1116");
            broadbandRenewCondition.setFeeMode("2");
            broadbandRenewCondition.setFeeTypeCode("1122");
            broadbandRenewCondition.setFee(String.valueOf(goodsSkuPrice));
            broadbandRenewCondition.setPay(String.valueOf(goodsSkuPrice));
            broadbandRenewCondition.setTradeDepartPassword(staffPwd);
//            broadbandRenewCondition.setxTradePayMoneny("0");
            //产品ID, 包ID,优惠编码
            TfGoodsSku tfGoodsSku = tfGoodsInfo.getTfGoodsSkuList().get(0);
            List<TfGoodsBusiParam> paramList = tfGoodsSku.getTfGoodsBusiParamList();
            for(TfGoodsBusiParam tgbp : paramList){
                o2oParamUtils.addParams(tgbp.getSkuBusiParamName(),tgbp.getSkuBusiParamValue(),tgbp.getSkuBusiParamDesc());
                if("PRODUCT_ID".equals(tgbp.getSkuBusiParamName())){
                    broadbandRenewCondition.setProductId(tgbp.getSkuBusiParamValue());
                }else if("PACKAGE_ID".equals(tgbp.getSkuBusiParamName())){
                    broadbandRenewCondition.setPackageId(tgbp.getSkuBusiParamValue());
                }else if("ELEMENT_ID".equals(tgbp.getSkuBusiParamName())){
                    broadbandRenewCondition.setElementId(tgbp.getSkuBusiParamValue());
                }else if("DEAL_TIME".equals(tgbp.getSkuBusiParamName())){
                    broadbandRenewCondition.setDealTime(tgbp.getSkuBusiParamValue());
                }
            }
            // 添加渠道工号信息
            MemberVo memberVo = UserUtils.getLoginUser(request);
            o2oParamUtils.addChannelInfo(memberVo.getChannelInfo(),phoneName);
            o2oParamUtils.addConditionChannel(broadbandRenewCondition,memberVo.getChannelInfo(),phoneName);

            //增加受理中校验参数
            broadbandRenewCondition.setPreType("1");
            localLogger.info("broadbandRenewCondition:"+ JSON.toJSONString(broadbandRenewCondition));
            Map result = broadBandService.broadBandContinuePay(broadbandRenewCondition);
            logger.info("宽带续费业务受理中校验接口返回： " + result);

            if(null == result || !result.get(OrderConstant.IF_RESULT_CODE).equals(OrderConstant.IF_SUCCESS_CODE)){
                resMap.put("code", "1");
                resMap.put("message", "当前宽带账号不能进行续费,宽带续费业务受理中校验失败:【 code = "+result.get(OrderConstant.IF_RESULT_CODE)+ " message = " + result.get(OrderConstant.IF_RESULT_INFO)+" 】");
                return resMap;
            }


            O2oOrderTempInfo orderTempInfo = new O2oOrderTempInfo();
            orderTempInfo.setOrder_type_id(Long.valueOf(OrderConstant.TYPE_BROADBAND_CONTINUE));
            orderTempInfo.setMember_id(memberVo.getMemberLogin().getMemberId());
            orderTempInfo.setMember_loging_name(memberVo.getMemberLogin().getMemberLogingName());
            orderTempInfo.setContact_phone(phoneName);
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
            Long orderTempId = orderTempService.insert(orderTempInfo);
            //添加交易参数

            busiParamList = o2oParamUtils.getParamsList();
            for(O2oOrderParamTemp paramTemp:busiParamList){
                paramTemp.setOrderTempId(BigDecimal.valueOf(orderTempId));
            }
            orderParamTemService.batchInsert(busiParamList);

            orderTempInfo.setOrder_temp_id(orderTempId);
            resMap.put("orderSub", orderTempInfo);
            resMap.put("code", "0");
            resMap.put("message", "生成订单成功");
        } catch (Exception e) {
            resMap.put("code", "1");
            resMap.put("message", "生成订单异常");
            e.printStackTrace();
        }
        return resMap;
    }
}
