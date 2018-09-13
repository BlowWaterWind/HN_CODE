package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.goods.entity.goods.TfGoodsAttr;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 会员活动查询控制器
 * 1、活动：2016奥运会，加油中国
 * Created by wangqiang11 on 2016/8/2
 */
@Controller
@RequestMapping("goodsQuery")
public class GoodsMarketQueryController extends BaseController {

    @Autowired
    IGoodsManageService goodsManageService;

    /**
     * 查询移动定制版或支持VOTEL功能的手机（校园迎新活动）
     * @param model
     * @return
     */
    @RequestMapping("/toBuyPhoneCouponArea")
    public String toBuyPhoneCouponArea(Model model){
        try {
            Map<String,Object> customizeParam = Maps.newHashMap();
            customizeParam.put("goodsAttrName","移动定制版");
            customizeParam.put("goodsAttrValue","是");
            customizeParam.put("goodsStatusId",4);
            List<TfGoodsAttr> customizeList = goodsManageService.queryGoodsAttrByCds(customizeParam);

            Map<String,Object> votelParam = Maps.newHashMap();
            votelParam.put("goodsAttrName","VOTEL功能");
            votelParam.put("goodsAttrValue","支持");
            votelParam.put("goodsStatusId",4);
            List<TfGoodsAttr> votelList = goodsManageService.queryGoodsAttrByCds(votelParam);
            votelList.addAll(customizeList);

            List<Long> goodsIds = Lists.newArrayList();
            for (TfGoodsAttr goodsAttr : votelList) {
                goodsIds.add(goodsAttr.getGoodsId());
            }

            List<TfGoodsInfo> goodsList = goodsManageService.queryGoodsInfoListByIds(goodsIds,CommonParams.CHANNEL_CODE);
            model.addAttribute("goodsList",goodsList);
        } catch (Exception e) {
            logger.error("查询移动定制版或支持VOTEL功能的手机异常：" + e);
        }

        return "web/goods/market/buyPhoneCouponArea";
    }

}
