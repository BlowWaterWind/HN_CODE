package com.ai.ecs.ecm.mall.wap.modules.member;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.integral.entity.IntegrayAccount;
import com.ai.ecs.integral.entity.IntegrayDetail;
import com.ai.ecs.integral.service.IntegrayAccountService;
import com.ai.ecs.integral.service.IntegrayDetailService;
import com.ai.ecs.member.entity.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("integration")
public class IntegrationController  extends BaseController
{
    @Autowired
    @Qualifier("integrayAccountServiceImpl")
    IntegrayAccountService integrayAccountService;
    
    @Autowired
    @Qualifier("integrayDetailServiceImpl")
    IntegrayDetailService integrayDetailService;
    
    
    @RequestMapping(value = "/toIntegrationSelect", method = RequestMethod.GET)
    public String toIntegrationSelect(HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
           IntegrayAccount integrayAccount = integrayAccountService.getByMemberId(member.getMemberLogin().getMemberId());
           IntegrayDetail integrayDetail=new IntegrayDetail();
           integrayDetail.setMemberId(member.getMemberLogin().getMemberId());
           List<IntegrayDetail> list = integrayDetailService.findAll(integrayDetail);

           // 模糊化电话号码
            Long lPhone = member.getMemberLogin().getMemberPhone();
            if(lPhone != null){
                String strPhone = String.valueOf(lPhone);
                StringBuilder blurryPhone = new StringBuilder("");
                if(strPhone.length() >= 11){
                    blurryPhone.append(strPhone.substring(0,3));
                    blurryPhone.append("****");
                    blurryPhone.append(strPhone.substring(7));
                } else {
                    blurryPhone.append(strPhone);
                }

                request.setAttribute("blurryPhone",blurryPhone);
            }

           request.setAttribute("integrayAccount", integrayAccount);
           request.setAttribute("integraylist", list);
           request.setAttribute("member", member.getMemberLogin());
        }
        return "web/member/integrationSelect";
    }
    
    
    
    
    
}
