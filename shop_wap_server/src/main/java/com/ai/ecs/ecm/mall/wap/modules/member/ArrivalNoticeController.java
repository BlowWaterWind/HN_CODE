package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.api.IArrivalNoticeService;
import com.ai.ecs.member.entity.ArrivalNotice;
import com.ai.ecs.member.entity.MemberVo;

@Controller
@RequestMapping("arrivalNotice")
public class ArrivalNoticeController extends BaseController
{
    @Autowired
    @Qualifier("arrivalNoticeService")
    IArrivalNoticeService arrivalNoticeService;
    
    @RequestMapping(value = "/addNotice.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addNotice(HttpServletRequest request,
            HttpServletResponse response, Long phone, Long skuId)
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
            ArrivalNotice notice = new ArrivalNotice();
            String res = arrivalNoticeService.addNotice(notice);
            if (!"true".equals(res))
            {
                return "到货通知设置异常，请重试！";
            }
            else
            {
                return "success";
            }
        }else{
            return "请先登录！";
        }
       
    }
    
    @RequestMapping(value = "/noticeUser.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String noticeUser(HttpServletRequest request,
            HttpServletResponse response, Long skuId)
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
            Map<String, Object> res = arrivalNoticeService.getNoticeBySkuMember(member.getMemberLogin().getMemberId(), skuId);
            if (res!=null)
            {
            }
            else
            {
                return "该商品未设置到货通知！";
            }
        }else{
            return "请先登录！";
        }
        return "success";
       
    }

}
