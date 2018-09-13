package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.o2o.api.O2oBussinessRecommendService;
import com.ai.ecs.o2o.entity.O2oBussinessRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author hexiao
 * 商机业务推荐
 * Created by think on 2017/11/28.
 */
@Controller
@RequestMapping("o2oBussinessRecommend")
public class O2oBussinessRecommendController extends BaseController {
    @Autowired
    private O2oBussinessRecommendService o2oBussinessRecommendService;

    /**
     * 商机推荐输入手机号
     * @param model
     * @return
     */
    @RequestMapping("init")
    public String init(Model model){
        return "web/broadband/o2o/bussinessRecommend/o2oBusInit";
    }
    @RequestMapping("recommend")
    public String recommend(Model model,String phone){
        List<O2oBussinessRecommend> list = o2oBussinessRecommendService.queryRecommendList(phone,"E050");
        model.addAttribute("list",list);
        model.addAttribute("phone",phone);
        return "web/broadband/o2o/bussinessRecommend/recommend";
    }
}
