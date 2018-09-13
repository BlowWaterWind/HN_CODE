package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.o2o.api.O2oBussinessRecommendService;
import com.ai.ecs.o2o.entity.O2oBussinessRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hexiao
 * wap版商机推荐
 * Created by think on 2017/11/28.
 */
@Controller
@RequestMapping("bussinessRecommend")
public class BussinessRecommendConroller  extends BaseController {
    @Autowired
    private O2oBussinessRecommendService o2oBussinessRecommendService;

    @RequestMapping("recommend")
    public String recommend(Model model,HttpServletRequest request){
        MemberVo memberVo = UserUtils.getLoginUser(request);
        String installPhoneNum = memberVo.getMemberLogin().getMemberLogingName();
        List<O2oBussinessRecommend> list = o2oBussinessRecommendService.queryRecommendList(installPhoneNum,"E006");
        model.addAttribute("list",list);
        model.addAttribute("phone",installPhoneNum);
        return "web/broadband/o2o/bussinessRecommend/recommend";
    }
}