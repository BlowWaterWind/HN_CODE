package com.ai.ecs.ecm.mall.wap.modules.afterservice;

import com.ai.ecs.afterservice.api.IAftersaleServerService;
import com.ai.ecs.afterservice.entity.AftersaleReplyReason;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.afterservice.service.AftersaleCommonService;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.order.api.IOrderQueryService;
import com.ai.ecs.order.api.aftersale.IReturnGoodUserService;
import com.ai.ecs.order.constant.Variables;
import com.ai.ecs.order.entity.TfOrderSub;
import com.ai.ecs.order.entity.TfOrderSubDetail;
import com.ai.ecs.order.entity.returns.TfReturnOrderDetail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("afterserviceWap/retAndChangeGood")
public class RetAndChangeForwardController extends BaseController {
    private final String pagePath = "web/afterservice/";

    @Autowired
    private IAftersaleServerService aftersaleService;

    @Autowired
    private IOrderQueryService orderQueryService;

    @Autowired
    private IReturnGoodUserService retGoodService;

    @Autowired
    private AftersaleCommonService commonService;

    /**
     * 申请退换货中间跳转页面
     */
    @RequestMapping(value = "retAndChangeGoodUI")
    public String retAndChangeGoodUI(HttpServletRequest request, Map<String, Object> retMap) {
        long subOrderId = Long.parseLong(request.getParameter("subOrderId"));
        long subOrderDetailId = Long.parseLong(request.getParameter("subOrderDetailId"));
        TfOrderSub showOrder = new TfOrderSub();
        try {
            //== 检查是否可以申请退换货 ==//
            long currentMemberId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
            Map resultMap = commonService.applyIsDuringValid(retMap, subOrderId, subOrderDetailId,currentMemberId);
            boolean flage = (boolean) resultMap.get("flage");
            retMap = (Map<String, Object>) resultMap.get("retMap");
            //统一返回结果页面
            if (!flage) {
                retMap.put("msgCode", "-1");
                //因为不同服务，页面el表达式得到子订单id的方式不同，所以统一处理子订单id
                return pagePath + "aftersalePromptWap";
            }

            // 实体查询条件
            TfOrderSub subOrderCond = new TfOrderSub();
            subOrderCond.setOrderSubId(subOrderId);
            //// 图片前缀路径
            //String preImgUrl = AfterserviceTool.properties.getProperty("tfs.req.url");
            //retMap.put("preImgUrl", preImgUrl);
            //标识服务类型
            retMap.put("asServerName", "retAndChangeGood");
            showOrder  = orderQueryService.queryComplexOrder(subOrderCond, Variables.ACT_GROUP_MEMBER);

            //== 得到当前子订单明细信息，重新存储于子订单中，使子订单中只有该条明细 ==//
            TfOrderSubDetail detlTemp = new TfOrderSubDetail();
            for (TfOrderSubDetail detl : showOrder.getDetailList()) {
                if (detl.getOrderSubDetailId() == subOrderDetailId) {
                    detlTemp = detl;
                }
            }
            showOrder.getDetailList().removeAll(showOrder.getDetailList());
            showOrder.getDetailList().add(detlTemp);

            //== 跳转后退款金额最初显示的值 ==//
            long skuPrice = showOrder.getDetailList().get(0).getGoodsSkuPrice();
            long payAmout = showOrder.getOrderSubPayAmount();
            long s = skuPrice > payAmout ? payAmout : skuPrice;
            retMap.put("initPrice", s);

            //== 可以退货的商品个数 ==//
            List<TfReturnOrderDetail> retDetlList = retGoodService.selectReturnOrderDetailList(subOrderDetailId);
            TfOrderSubDetail subDetl = orderQueryService.queryOrderDetail(subOrderDetailId);
            long retedNum = 0;
            for (TfReturnOrderDetail detl : retDetlList) {
                retedNum = retedNum + detl.getGoodsSkuNum();
            }
            // 可退货数量
            long allowRetNum = subDetl.getGoodsSkuNum() - retedNum;
            if (allowRetNum <= 0) {
                retMap.put("errorMsg", "对不起，您没有可以退换货的商品");
                return pagePath + "retAndChangeGoodWapBack";
            }
            else {
                retMap.put("allowRetNum", allowRetNum);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorMsg", "订单状态异常");
            return pagePath + "retAndChangeGoodWapBack";
        }
        retMap.put("showOrder", showOrder);
        return pagePath + "retAndChangeGoodWap";
    }

    /**
     * 初始化原因
     */
    @RequestMapping(value = "initData", method = RequestMethod.POST)
    @ResponseBody
    public List<AftersaleReplyReason> ajaxDatas(HttpServletRequest request) throws Exception {
        int typeId = Integer.parseInt(request.getParameter("asServerTypeId"));
        List<AftersaleReplyReason> asReasonL = aftersaleService.selectAllListByTypeId(typeId);
        String testJson = JSON.toJSONString(asReasonL, true);
        logger.info(JSONObject.toJSONString(testJson, SerializerFeature.WriteMapNullValue));
        return asReasonL;
    }
}
