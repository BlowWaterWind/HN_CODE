package com.ai.ecs.ecm.mall.wap.modules.afterservice;

import com.ai.ecs.afterservice.api.IAftersaleServerService;
import com.ai.ecs.afterservice.constant.AftersaleConstants;
import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.config.Global;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.aftersale.IReturnGoodUserService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.returns.TfReturnOrder;
import com.ai.ecs.order.entity.returns.TfReturnOrderDetail;
import com.ai.ecs.order.entity.returns.TfReturnOrderDetailImei;
import com.ai.ecs.order.entity.returns.TfReturnOrderImg;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("afterserviceWap/retGood")
public class RetGoodWapController extends BaseController {
    private final String pagePath = "web/afterservice/";

    @Autowired
    private IAftersaleServerService aftersaleService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IReturnGoodUserService retGoodUserService;


    /**
     * 申请退货： 单件商品进行退货，退货单和退货单明细一对一的关系，不支持整个订单退货 支持代理申请人和买家自己两种角色申请
     */
    @RequestMapping(value = "retGoodApply")
    public String retGoodApply(DefaultMultipartHttpServletRequest multipartRequest,
            HttpServletRequest request, Map<String, Object> retMap) {
        // 订单Id
        long orderSubId = Long.parseLong(request.getParameter("orderSubId"));
        // 订单明细Id
        long orderSubDetailId = Long.parseLong(request.getParameter("orderSubDetailId"));
        // 退货数量
        long retGoodNum = Long.parseLong(request.getParameter("retGoodNum"));
        // 退款金额
        long returnPrice = (long) (Float.parseFloat(request.getParameter("returnPrice")) * 100);
        String retGoodReason = request.getParameter("retGoodReason");
        String retGoodExplain = request.getParameter("retGoodExplain");
        String proxyApplyPerson = request.getParameter("proxyApplyPerson");
        String proxyApplyPersonName = request.getParameter("proxyApplyPersonName");

        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        String memberLoginName = UserUtils.getLoginUser(request).getMemberLogin().getMemberLogingName();

        retMap.put("asServerName", "retGood");
        retMap.put("retGoodReason", retGoodReason);
        retMap.put("retGoodExplain", retGoodExplain);
        retMap.put("retGoodNum", retGoodNum);
        retMap.put("returnPrice", (float) returnPrice / 100);

        // 待存储的退货单
        TfReturnOrder retOrder = new TfReturnOrder();
        // 待存储的退货单详情
        TfReturnOrderDetail retOrderDetail = new TfReturnOrderDetail();
        // 退货单详情List
        // List<TfReturnOrderDetail> retOrderDetailList = new
        // ArrayList<TfReturnOrderDetail>();

        /** 代理申请人存在时，存代理人信息 **/
        if (!"".equals(proxyApplyPerson) && proxyApplyPerson != null) {
            retOrder.setProxyApplyPerson(proxyApplyPerson);
            retOrder.setProxyApplyPersonName(proxyApplyPersonName);
        }
        else {
            retOrder.setApplyPerson(memberId.toString());
            retOrder.setApplyLoginName(memberLoginName);
        }
        retOrder.setApplyTime(new Date());
        retOrder.setReturnReson(retGoodReason);
        retOrder.setReturnResonDesc(retGoodExplain);
        retOrder.setReturnOrderNo(AfterserviceTool.genOrderNum());

        //== 得到允许退货的最大数量:填写的数量 < = 子订单详情条目数-已退货的数目 ==//
        // TfReturnOrderDetailExtend retDetl = new TfReturnOrderDetailExtend();
        List<TfReturnOrderDetail> retDetlList = new ArrayList<TfReturnOrderDetail>();
        TfOrderSubDetail subDetl = new TfOrderSubDetail();
        try {
            // 判断是退货申请是否允许，允许的最大值，返回
            retDetlList = retGoodUserService.selectReturnOrderDetailList(orderSubDetailId);
            // retDetl =
            // retGoodUserService.selectByOrderSubDetailId(orderSubDetailId);
            subDetl = orderQueryService.queryOrderDetail(orderSubDetailId);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        long retSkuNum = 0;
        // 如果以前退货过
        if (retDetlList != null) {
            // 得到退货的数目
            for (TfReturnOrderDetail retDetl : retDetlList) {
                retSkuNum = retDetl.getGoodsSkuNum() + retSkuNum;
            }
        }
        // 子订单详情中的sku数量
        long subOrderNum = subDetl.getGoodsSkuNum();
        if (retGoodNum > (subOrderNum - retSkuNum)) {
            retMap.put("errorMsg", "对不起，您退货的数量超过可申请退货的最大数量");
            return pagePath + "retAndChangeGoodWapBack";
        }

        TfOrderSubDetail subOrderDetail = new TfOrderSubDetail();
        // 原始订单明细Id
        subOrderDetail.setOrderSubDetailId(orderSubDetailId);
        // 原始子订单Id
        subOrderDetail.setOrderSubId(orderSubId);

        //== 子订单信息存储到退货单表中 ==//
        TfOrderSub subOrderCond = new TfOrderSub();
        subOrderCond.setOrderSubId(orderSubId);
        try {
            TfOrderSub queriedSubOrder = orderQueryService.queryComplexOrder(subOrderCond);
            // 如果没有该条信息
            if (queriedSubOrder == null) {
                retMap.put("errorMsg", "订单记录异常，请联系客服");
                return pagePath + "retAndChangeGoodWapBack";
            }
            // 返回所属订单号
            retMap.put("subOrderNo", queriedSubOrder.getOrderSubNo());
            retOrder.setShopId(queriedSubOrder.getShopId());
            retOrder.setShopName(queriedSubOrder.getShopName());
            //渠道编码
            retOrder.setChannelCode(AftersaleConstants.CHANNELCODE_WAP);
            retOrder.setLogisticsCode(queriedSubOrder.getLogisticsCompanyCode());
            retOrder.setLogisticsName(queriedSubOrder.getLogisticsCompany());
            retOrder.setLogisticsNum(queriedSubOrder.getLogisticsNum());
            retOrder.setOrderStatusId(OrderConstant.RETURNGOODS_PENDING);
            retOrder.setDeliveryModeId(queriedSubOrder.getDeliveryModeId());
            retOrder.setHallAddress(queriedSubOrder.getHallAddress());
            retOrder.setOldOrderNum(queriedSubOrder.getOldOrderNum());
            retOrder.setOrderId(queriedSubOrder.getOrderId());
            //TODO
            retOrder.setOrderSubNo(queriedSubOrder.getOrderSubNo());
            retOrder.setOrderTime(queriedSubOrder.getOrderTime());
            retOrder.setOrderTypeId(queriedSubOrder.getOrderTypeId());
            retOrder.setPayModeId(queriedSubOrder.getPayModeId());
            retOrder.setPaySerialNumber(queriedSubOrder.getPaySerialNumber());
            retOrder.setReceiptTimeId(queriedSubOrder.getReceiptTimeId());
            retOrder.setSupplierShopId(queriedSubOrder.getSupplierShopId());
            retOrder.setSupplierShopName(queriedSubOrder.getSupplierShopName());
            retOrder.setReceiptTimeId(queriedSubOrder.getReceiptTimeId());
            retOrder.setShopTypeId(queriedSubOrder.getShopTypeId());
            //TODO 活动相关字段因为orderSub中没有，所以无法set值

            //== 相应子订单明细存储到退货单明细中 ==//
            List<TfOrderSubDetail> orderSubDetailList = queriedSubOrder.getDetailList();
            List<TfReturnOrderDetail> retOrderDetailList = new ArrayList<TfReturnOrderDetail>();
            for (int i = 0; i < orderSubDetailList.size(); i++) {
                if (orderSubDetailId == orderSubDetailList.get(i).getOrderSubDetailId()) {
                    // 退货数量判断
                    TfOrderSubDetail dtl = orderSubDetailList.get(i);
                    retOrder.setOrderSubDetailId(subDetl.getOrderSubDetailId());
                    retOrderDetail.setGoodsSkuNum(retGoodNum);
                    retOrderDetail.setGoodsSkuPrice(dtl.getGoodsSkuPrice());
                    retOrderDetail.setReturnGoodsSkuPrice(dtl.getGoodsSkuPrice());
                    retOrderDetail.setGoodsFormat(dtl.getGoodsFormat());
                    retOrderDetail.setGoodsName(dtl.getGoodsName());
                    retOrderDetail.setGoodsSkuId(dtl.getGoodsSkuId());
                    retOrderDetail.setOrderSubId(orderSubId);
                    retOrderDetail.setOrderSubDetailId(orderSubDetailId);

                    //retOrderDetail.setGoodsHtmlUrl();
                    //retOrderDetail.setGoodsSkuGiveIntegralStatus();
                    //retOrderDetail.setGoodsSkuGiveIntegral();
                    //retOrderDetail.setRootCateId();
                    //retOrderDetail.setRuleId();
                    //retOrderDetail.setMarketId();
                    //retOrderDetail.setGoodsHtmlUrl();

                    //== 金额是否符合要求判断，防止攻击 ==//
                    long l1 = queriedSubOrder.getOrderSubPayAmount();
                    // sku价格*退款的sku数目
                    long l2 = dtl.getGoodsSkuPrice() * retGoodNum;
                    long maxretMony = l1 < l2 ? l1 : l2;
                    // 返回最高可退金额
                    retMap.put("maxretMony", maxretMony);
                    if (returnPrice <= maxretMony) {
                        retOrder.setReturnPrice(returnPrice);
                        retOrder.setActualReturnPrice(returnPrice);
                        retOrderDetailList.add(retOrderDetail);
                        break;
                    }
                    else {
                        retMap.put("errorMsg", "申请失败，您填写的退货总金额高于购买总金额");
                        return pagePath + "retAndChangeGoodWapBack";
                    }
                }
            }

            //== 获取、上传图片 ==//
            ResponseBean<String> responsebean = AfterserviceTool.upload2tfs(request,multipartRequest);
            // 上传失败
            if (responsebean.getCode().equals("-1")) {
                retMap.put("errorMsg", "上传图片失败");
                return pagePath + "retAndChangeGoodWapBack";
            }
            String tfsRetDataName = responsebean.getData();
            List<TfReturnOrderImg> retImgL = new ArrayList<TfReturnOrderImg>();
            // 处理图片
            if (tfsRetDataName != null) {
                if (!tfsRetDataName.contains(",")) {
                    TfReturnOrderImg retImg = new TfReturnOrderImg();
                    retImg.setReturnOrderImgUrl(tfsRetDataName);
                    retImgL.add(retImg);
                }
                else {
                    String[] s = tfsRetDataName.split(",");
                    for (int i = 0; i < s.length; i++) {
                        TfReturnOrderImg retImg = new TfReturnOrderImg();
                        retImg.setReturnOrderImgUrl(s[i]);
                        retImgL.add(retImg);
                    }
                }
            }

            //== 插入退货记录 ==//
            retGoodUserService.insertReturnOrderAllRecord(retOrder, retImgL, retOrderDetailList,
                    new TfReturnOrderDetailImei());
            // 放在前面，否则返回页面将可能没有信息
            retMap.put("retOrder", retOrder);
            retMap.put("retOrderDetail", retOrderDetail);
        }
        catch (Exception e) {
            retMap.put("errorMsg", "退货申请失败");
            e.printStackTrace();
            return pagePath + "retAndChangeGoodWapBack";
        }
        retMap.put("sucMsg", "退货申请成功");
        return pagePath + "retAndChangeGoodWapBack";
    }

    /**
     * 退货列表
     */
    @RequestMapping(value = "retGoodList", method = RequestMethod.POST)
    @ResponseBody
    public String reqRetGoodList(HttpServletRequest request, HttpServletResponse response) {
        String tfsUrl = request.getParameter("tfsUrl");
        String projectUrl = request.getContextPath();
        TfReturnOrder cond = new TfReturnOrder();
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        cond.setMemberId(memberId.toString());

        Page<TfReturnOrder> page = cond.getPage();
        page.setPageNo(getPageNo(request, response));
        page.setPageSize((getPageSize(request, response, 3)));
        Page<TfReturnOrder> listPage = new Page<>();
        // 待转json的map
        Map<String, Object> respRetGoodMap = new HashMap<>();
        // 返回Json
        String retGoodLJson = "";
        try {
            listPage = retGoodUserService.queryRetGoodListPage(page,cond);
            //退货单循环赋值订单信息
            for(TfReturnOrder retOrder:listPage.getList()){
                TfOrderSub orderSubTemp = orderQueryService.getSubOrderAndSingleDetl(
                        retOrder.getReturnOrderDetailL().get(0).getOrderSubId(),
                        retOrder.getReturnOrderDetailL().get(0).getOrderSubDetailId()
                );
                retOrder.setOrderSub(orderSubTemp);
            }
            respRetGoodMap.put("serverType", "retGood");
            respRetGoodMap.put("tfsUrl",tfsUrl);
            respRetGoodMap.put("projectUrl",projectUrl);
            // 没有列表信息时，放一个空的值进入，以防模板错误
            if (listPage.getList().size() == 0) {
                respRetGoodMap.put("retMsg", Global.NO);
                Page<TfReturnOrder> pageNull = new Page<TfReturnOrder>();
                List<TfReturnOrder> retOrder = new ArrayList<TfReturnOrder>();
                pageNull.setList(retOrder);
                respRetGoodMap.put("page", pageNull);
            }
            // 存在列表信息时
            else {
                // 标志信息
                respRetGoodMap.put("retMsg", Global.YES);
                // 列表信息
                respRetGoodMap.put("page", listPage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            respRetGoodMap.put("retMsg", Global.NO);
        }
        logger.info("=====reqRetGoodList=====："+JSONObject.toJSONString(respRetGoodMap,SerializerFeature.WriteMapNullValue));
        retGoodLJson = JSONObject.toJSONString(respRetGoodMap, SerializerFeature.WriteMapNullValue);
        return retGoodLJson;
    }

    /**
     * 退货详情
     * @param request
     * @param retMap
     * @return
     */
    @RequestMapping(value = "listDetail",method = RequestMethod.GET)
    public String listDetail(HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== listDetail：wap 退货单详情 ==========");
        long returnOrderId = Long.parseLong(request.getParameter("returnOrderId"));
        long returnOrderDetailId = Long.parseLong(request.getParameter("returnOrderDetailId"));

        TfOrderSub showOrder = new TfOrderSub();
        TfReturnOrderDetail retOrderDetl = new TfReturnOrderDetail();
        try {
            Map<String,Object> condMap = new HashMap<>();
            condMap.put("actLogL",true);
            //退货单信息
            TfReturnOrder retOrder = retGoodUserService.queryRetOrderAndSingleDetl(returnOrderId,returnOrderDetailId,condMap);
            //查询订单
            //TfOrderSub orderSubCond = new TfOrderSub();
            //orderSubCond.setOrderSubId(retOrderDetl.getOrderSubId());
            //TfOrderSub orderSub = orderQueryService.queryComplexOrder(orderSubCond);
            TfOrderSub orderSub = orderQueryService.getSubOrderAndSingleDetl(
                    retOrder.getReturnOrderDetailL().get(0).getOrderSubId(),
                    retOrder.getReturnOrderDetailL().get(0).getOrderSubDetailId());
            retOrder.setOrderSub(orderSub);
            retMap.put("retOrder",retOrder);
            logger.info("===== RetGood-listDetail ====="+JSONObject.toJSONString(retOrder,SerializerFeature.WriteMapNullValue));
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "退货订单详情数据异常");
            return pagePath + "retAndChangeGoodWapBack";
        }
        return pagePath + "retGoodDetailWap";
    }
}
