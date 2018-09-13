package com.ai.ecs.ecm.mall.wap.modules.afterservice;

import com.ai.ecs.common.ResponseBean;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.config.Global;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.aftersale.IChangeGoodUserService;
import com.ai.ecs.order.constant.OrderConstant;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.change.TfChangeOrder;
import com.ai.ecs.order.entity.change.TfChangeOrderDetail;
import com.ai.ecs.order.entity.change.TfChangeOrderDetailImei;
import com.ai.ecs.order.entity.change.TfChangeOrderImg;
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
@RequestMapping("afterserviceWap/changeGood")
public class ChangeGoodWapController extends BaseController {
    private final String pagePath = "web/afterservice/";

    @Autowired
    private IChangeGoodUserService cagGoodUserService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @RequestMapping(value = "changeGoodApply", method = RequestMethod.POST)
    public String changeGoodApply(HttpServletRequest request,
            DefaultMultipartHttpServletRequest multipartRequest, Map<String, Object> retMap) {
        long orderSubId = Long.parseLong(request.getParameter("orderSubId"));
        long orderSubDetailId = Long.parseLong(request.getParameter("orderSubDetailId"));
        String changeGoodReason = request.getParameter("changeGoodReason");
        String changeGoodExplain = request.getParameter("changeGoodExplain");
        String proxyApplyPerson = request.getParameter("proxyApplyPerson");
        String proxyApplyPersonName = request.getParameter("proxyApplyPersonName");
        long changeGoodNum = Long.parseLong(request.getParameter("changeGoodNum"));
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        String memberLoginName = UserUtils.getLoginUser(request).getMemberLogin().getMemberLogingName();

        retMap.put("asServerName", "changeGood");
        retMap.put("changeGoodReason", changeGoodReason);
        retMap.put("changeGoodExplain", changeGoodExplain);
        retMap.put("changeGoodNum", changeGoodNum);

        TfChangeOrder changeOrder = new TfChangeOrder();
        List<TfChangeOrderDetail> changeOrderDetailList = new ArrayList<TfChangeOrderDetail>();
        List<TfChangeOrderImg> changeOrderImgList = new ArrayList<TfChangeOrderImg>();

        //== proxyApplyPerson存在时，则存proxyApplyPerson ==//
        if (!"".equals(proxyApplyPerson)&& proxyApplyPerson!= null) {
            changeOrder.setProxyApplyPerson(proxyApplyPerson+"");
            changeOrder.setProxyApplyPersonName(proxyApplyPersonName);
        }
        else {
            changeOrder.setApplyPerson(memberId + "");
            changeOrder.setApplyLoginName(memberLoginName);
        }
        changeOrder.setApplyTime(new Date());
        changeOrder.setChangeReson(changeGoodReason);
        changeOrder.setChangeResonDesc(changeGoodExplain);
        changeOrder.setChangeOrderNo(AfterserviceTool.genOrderNum());
        changeOrder.setOrderStatusId(OrderConstant.CHANGEGOODS_PENDING);
        //渠道编码
        changeOrder.setChannelCode("8006");

        TfOrderSub subOrder = new TfOrderSub();
        subOrder.setOrderSubId(orderSubId);
        try {
            TfChangeOrderDetail changeOrderDetail = new TfChangeOrderDetail();

            // 商城这边第二次换货不走订单流程，上一次订单明细Id和上一次订单Id未处理 TODO
            // changeOrderDetail.setOrderSubId(orderSubId);
            // changeOrderDetail.setOrderSubDetailId(orderSubDetailId);
            // 1.查表：子订单id=上一次子订单id，则是第二次以上的换货
            // 将原始子订单Id和原始订单明细Id取出来，再重新存进去，将本次的子订单id存于上一次子订单id，本次的子订单明细id存于上一次子订单明细id
            // 2.查表：子订单id，没有上一次子订单id与之匹配，则是第一次换货
            // 原始子订单Id和原始订单明细Id、上一次子订单id和上一次子订单明细id存值都为子订单id和子订单明细id
            TfChangeOrderDetail changeDetail0 = cagGoodUserService.queryChangeOrderDetailPreId(orderSubId);
            // 没有退过货，原始子订单Id和原始订单明细Id、上一次子订单id和上一次子订单明细id都存子订单id和子订单明细id
            if (changeDetail0 == null) {
                changeOrderDetail.setStartOrderSubId(orderSubId);
                changeOrderDetail.setStartOrderSubDetailId(orderSubDetailId);
            }
            // 有退过货，原始子订单id和原始子订单明细Id存子订单id和子订单明细id
            // 上一次子订单id和上一次子订单明细id存changeOrderId和changeOrderDetailId
            else {
                changeOrderDetail.setStartOrderSubId(changeDetail0.getStartOrderSubId());
                changeOrderDetail
                        .setStartOrderSubDetailId(changeDetail0.getStartOrderSubDetailId());
                changeOrderDetail.setChangeOrderId(changeDetail0.getChangeOrderId());
                changeOrderDetail.setChangeOrderDetailId(changeDetail0.getChangeOrderDetailId());
            }

            TfOrderSub queriedSubOrder = orderQueryService.queryComplexOrder(subOrder,Variables.ACT_GROUP_MEMBER);
            List<TfOrderSubDetail> orderSubDetailList = new ArrayList<TfOrderSubDetail>();
            if (queriedSubOrder == null) {
                retMap.put("errorMsg", "订单记录异常，请联系客户");
                return pagePath + "retAndChangeGoodBack";
            }
            else {
                //物流信息
                changeOrder.setLogisticsCode(queriedSubOrder.getLogisticsCompanyCode());
                changeOrder.setLogisticsName(queriedSubOrder.getLogisticsCompany());
                changeOrder.setLogisticsNum(queriedSubOrder.getLogisticsNum());
                orderSubDetailList = queriedSubOrder.getDetailList();
                retMap.put("subOrderNo", queriedSubOrder.getOrderSubNo());
            }

            for (TfOrderSubDetail detl : orderSubDetailList) {
                if (orderSubDetailId == detl.getOrderSubDetailId()) {
                    // 换货数量判断
                    if (detl.getGoodsSkuNum() >= changeGoodNum) {
                        changeOrderDetail.setGoodsSkuNum(changeGoodNum);
                        changeOrderDetail.setGoodsSkuId(detl.getGoodsSkuId());
                        changeOrderDetail.setGoodsFormat(detl.getGoodsFormat());
                        changeOrderDetail.setGoodsName(detl.getGoodsName());
                        changeOrderDetail.setGoodsSkuPrice(detl.getGoodsSkuPrice());
                        changeOrderDetailList.add(changeOrderDetail);
                        break;
                    }
                    else {
                        retMap.put("errorMsg", "申请失败，您换货的商品数量高于购买数量");
                        return pagePath + "retAndChangeGoodBack";
                    }
                }
            }
            // 获取、上传图片
            ResponseBean<String> responsebean = AfterserviceTool.upload2tfs(request, multipartRequest);
            // 上传失败
            if (responsebean.getCode().equals("-1")) {
                retMap.put("errorMsg", "上传图片失败");
                return pagePath + "retAndChangeGoodBack";
            }
            String tfsRetDataName = responsebean.getData();
            // 处理图片
            if (tfsRetDataName != null) {
                if (!tfsRetDataName.contains(",")) {
                    TfChangeOrderImg asChangeAppImg = new TfChangeOrderImg();
                    asChangeAppImg.setChangeOrderImgUrl(tfsRetDataName);
                    changeOrderImgList.add(asChangeAppImg);
                }
                else {
                    String[] s = tfsRetDataName.split(",");
                    for (int i = 0; i < s.length; i++) {
                        TfChangeOrderImg asChangeAppImg = new TfChangeOrderImg();
                        asChangeAppImg.setChangeOrderImgUrl(s[i]);
                        changeOrderImgList.add(asChangeAppImg);
                    }
                }
            }
            cagGoodUserService.insertChangeAllRecord(changeOrder, changeOrderImgList,
                    changeOrderDetailList, new TfChangeOrderDetailImei());

            retMap.put("changeOrder", changeOrder);
            retMap.put("changeOrderDetail", changeOrderDetail);
        }
        catch (Exception e) {
            retMap.put("errorMsg", "换货申请失败");
            logger.error("换货申请信息存储异常");
        }
        retMap.put("sucMsg", "换货申请成功，等待管理员审核！");
        return pagePath + "retAndChangeGoodWapBack";
    }

    @RequestMapping(value = "changeGoodList", method = RequestMethod.POST)
    @ResponseBody
    public String reqMyChangeGoodList(HttpServletRequest request, HttpServletResponse response) {
        String tfsUrl = request.getParameter("tfsUrl");
        String projectUrl = request.getContextPath();
        TfChangeOrder cond = new TfChangeOrder();
        Long memberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
        cond.setMemberId(memberId.toString());
        Page<TfChangeOrder> page = cond.getPage();
        page.setPageNo(getPageNo(request, response));
        page.setPageSize(getPageSize(request, response, 10));
        Page<TfChangeOrder> listPage = new Page<TfChangeOrder>();

        // 待转json的Map
        Map<String, Object> respchangeGoodMap = new HashMap<String, Object>();
        try {
            listPage = cagGoodUserService.queryChangeGoodListPage(page,cond);
            //退货单循环赋值订单信息
            for(TfChangeOrder changeOrder:listPage.getList()){
                TfOrderSub orderSubTemp = orderQueryService.getSubOrderAndSingleDetl(
                        changeOrder.getOrderSubId(),
                        changeOrder.getOrderSubDetailId()
                );
                changeOrder.setOrderSub(orderSubTemp);
            }
            respchangeGoodMap.put("serverType", "changeGood");
            respchangeGoodMap.put("tfsUrl",tfsUrl);
            respchangeGoodMap.put("projectUrl",projectUrl);
            // 没有列表数据时
            if (listPage.getList().size() == 0) {
                respchangeGoodMap.put("retMsg", Global.NO);
                Page<TfChangeOrder> pageNull = new Page<TfChangeOrder>();
                List<TfChangeOrder> changeOrder = new ArrayList<TfChangeOrder>();
                pageNull.setList(changeOrder);
                respchangeGoodMap.put("page", pageNull);
            }
            else {
                respchangeGoodMap.put("retMsg", Global.YES);
                respchangeGoodMap.put("page", listPage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            respchangeGoodMap.put("retMsg", Global.NO);
        }
        String changeGoodLJson = JSONObject.toJSONString(respchangeGoodMap,
                SerializerFeature.WriteMapNullValue);
        logger.info("=====changeGoodList：=====" + JSONObject.toJSONString(changeGoodLJson, SerializerFeature.WriteMapNullValue));
        return changeGoodLJson;
    }

    /**
     * 列表查看换货详情
     */
    @RequestMapping(value = "listDetail")
    public String listDetail(HttpServletRequest request, Map<String, Object> retMap) {
        logger.info("========== 进入换货详情处理 ==========");
        long changeOrderId = Long.parseLong(request.getParameter("changeOrderId"));
        long changeOrderDetailId = Long.parseLong(request.getParameter("changeOrderDetailId"));
        try {
            Map<String,Object> condMap = new HashMap<>();
            condMap.put("actLogL",true);
            TfChangeOrder changeOrder =cagGoodUserService.queryChangeOrderAndSingleDetl(changeOrderId,changeOrderDetailId,condMap);
            //获取子订单信息
            TfOrderSub orderSub = orderQueryService.getSubOrderAndSingleDetl(
                    changeOrder.getOrderSubId(),
                    changeOrder.getOrderSubDetailId());
            changeOrder.setOrderSub(orderSub);
            retMap.put("changeOrder", changeOrder);
            logger.info("===== changeOrder:listDetail ======" + JSONObject.toJSONString(changeOrder, SerializerFeature.WriteMapNullValue));
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            retMap.put("errorMsg", "订单换货异常，请联系客服");
            return pagePath + "changeGoodBack";
        }
        return pagePath + "changeGoodDetailWap";
    }

}
