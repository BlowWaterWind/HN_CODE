package com.ai.ecs.ecm.mall.wap.modules.afterservice.service;

import com.ai.ecs.afterservice.api.IAftersaleServerService;
import com.ai.ecs.afterservice.entity.AftersaleApply;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.aftersale.IChangeGoodUserService;
import com.ai.ecs.order.api.aftersale.IReturnGoodUserService;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.change.TfChangeOrder;
import com.ai.ecs.order.entity.returns.TfReturnOrder;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 售后服务封装
 *
 * @author hewei 2016/6/15
 * @since 1.0
 */
@Service
public class AftersaleCommonService {
    //日志对象
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IAftersaleServerService aftersaleService;

    @Autowired
    private IReturnGoodUserService retGoodUserService;

    @Autowired
    private IChangeGoodUserService changeGoodService;

    /**
     * 退款、退换货、售后服务互斥校验
     * 是否还有商品可供退货
     * @param retMap            用来保存查询到的参数：错误信息提示语、标记、查询到的主表实体
     * @param orderSubId        查询条件：子订单id
     * @param orderSubDetlId    查询条件：子订单详情id
     * @param orderSubDetlId    查询条件：用户Id
     * @return 返回的Map：boolean标记、retMap
     */
    public Map applyIsDuringValid(Map<String, Object> retMap, long orderSubId,long orderSubDetlId,long memberId) throws Exception {
        boolean flage = true;
        //订单和订单详情
        TfOrderSub orderSub = orderQueryService.getSubOrderAndSingleDetl(orderSubId,orderSubDetlId);

        //验证订单是否属于当前用户下的，防止登录获取别人订单信息进行攻击
        if(memberId!=orderSub.getOrderUserRef().getMemberId()){
            retMap.put("promptMsg","对不起，您无该订单申请售后的权限!");
            retMap.put("flage","common");
            retMap.put("orderSub",orderSub);
        }

        //是否处于退货流程
        TfReturnOrder retOrder = retGoodUserService.canReturnGood(orderSubId);
        if (retOrder!=null){
            flage = false;
            retOrder.setOrderSub(orderSub);
            retMap.put("promptMsg","对不起，您的该商品正处于退货状态，不能重复申请");
            retMap.put("flage","retGood");
            retMap.put("retOrder",retOrder);
            logger.info("===== applyIsDuringValid：retOrder ====="+ JSONObject.toJSONString(retOrder, SerializerFeature.WriteMapNullValue));
        }

        //== 是否还有商品可退（待放开）：TODO 退货流程中，子订单详情相关退货信息没有修改，已经退货的订单，returnSkuNum为null ==//
        //List<TfReturnOrderDetail> retDetlL = retGoodUserService.selectReturnOrderDetailList(orderSubDetlId);
        //剩余商品
        //long remain = orderSub.getDetailList().get(0).getGoodsSkuNum() - orderSub.getDetailList().get(0).getReturnSkuNum();
        //if (remain <= 0) {
        //    flage = false;
        //    List<TfReturnOrderDetail> retDetlL = retGoodUserService.selectReturnOrderDetailList(orderSubDetlId);
        //    retOrder.setReturnOrderDetailL(retDetlL);
        //    retMap.put("promptMsg", "退货申请失败。（原因：您退货的数量超过可申请退货的最大数量）");
        //    retMap.put("flage", "retGood1");
        //    retMap.put("retOrder", retOrder);
        //    logger.info("===== applyIsDuringValid：retOrder ====="+ JSONObject.toJSONString(retOrder, SerializerFeature.WriteMapNullValue));
        //}

        //是否处于换货流程
        TfChangeOrder changeOrder = changeGoodService.canChange(orderSubDetlId);
        if (flage && changeOrder!=null) {
            flage = false;
            changeOrder.setOrderSub(orderSub);
            retMap.put("promptMsg","对不起，您的该商品正处于换货状态，暂不能申请换货");
            retMap.put("flage","changeGood");
            retMap.put("changeOrder",changeOrder);
            logger.info("===== applyIsDuringValid：changeOrder ====="+JSONObject.toJSONString(changeOrder,SerializerFeature.WriteMapNullValue));
        }

        //是否处于售后流程
        AftersaleApply asApp = aftersaleService.canApplyASServer(orderSubDetlId);
        if (flage&&asApp!=null) {
            flage = false;
            asApp.setOrderSub(orderSub);
            retMap.put("promptMsg","对不起，您的该商品正处于售后状态，暂不能申请售后服务");
            retMap.put("flage","aftersaleService");
            retMap.put("asApp",asApp);
            logger.info("===== applyIsDuringValid：asApp ====="+JSONObject.toJSONString(asApp,SerializerFeature.WriteMapNullValue));
        }

        //商品是否退款或全部退货
        Map<String,Object> map = new HashMap<>();
        map.put("flage",flage);
        map.put("retMap",retMap);
        return map;
    }
}
