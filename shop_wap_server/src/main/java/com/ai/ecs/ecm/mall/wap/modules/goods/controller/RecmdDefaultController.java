package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.order.entity.recmd.TfOrderRecmd;
import org.springframework.ui.Model;

/**
 * Created by hewei on 2017/12/20/020.
 */
public class RecmdDefaultController extends RecmdBaseController {
    /**
     * 跳转推荐页面之前处理
     * @param recmd
     */
    @Override
    public TfOrderRecmd beforeToGenRecmdLink(TfOrderRecmd recmd) {
        return null;
    }

    /**
     * 生成二维码之前处理
     * @param recmd
     */
    @Override
    public TfOrderRecmd beforeGenQrcode(TfOrderRecmd recmd) {
        return null;
    }

    /**
     * 处理推荐展示信息：这个必须依据业务来进行展示
     * @param model
     * @param orderRecmd
     */
    @Override
    public Model dealRecmdShowInfo(Model model, TfOrderRecmd orderRecmd) {
        return null;
    }
}
