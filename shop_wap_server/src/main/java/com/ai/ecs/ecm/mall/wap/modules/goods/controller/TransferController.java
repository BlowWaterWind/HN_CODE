package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by admin on 2017/9/18.
 */

/**
 * 集团免登录中间页
 */
@Controller
@RequestMapping("Transfer")
public class TransferController extends BaseController {
    @RequestMapping("transfer")
    public String transferRequest(){
        return "web/goods/broadband/consup/transfer";
    }
}
