package com.ai.ecs.ecm.mall.wap.modules.o2o;

import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.api.IMemberInfoService;
import com.ai.ecs.member.entity.MemberInfo;
import com.ai.ecs.member.entity.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 单点测试
 * @author hexiao
 * Created by think on 2017/10/26.
 */
@Controller
@RequestMapping("o2oTest")
public class O2oTest {
    @Autowired
    private IMemberInfoService memberInfoService;

    @RequestMapping("param")
    public String o2oParamTest(Model model, HttpServletRequest request) throws Exception {
        MemberVo memberVo = UserUtils.getLoginUser(request);
        MemberInfo member = memberInfoService.getMemberInfoByKey(memberVo.getMemberLogin().getMemberId());
        model.addAttribute("memberVo",memberVo);
        //商城用户信息
        model.addAttribute("member",member);
        return "web/broadband/o2o/test";
    }
    @RequestMapping("loginError")
    public String loginError(Model model){
        return "/web/broadband/o2o/loginError";
    }
}
