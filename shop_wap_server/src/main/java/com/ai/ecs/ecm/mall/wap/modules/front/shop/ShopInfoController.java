package com.ai.ecs.ecm.mall.wap.modules.front.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.api.login.ILoginService;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.sales.api.ICouponService;
import com.ai.ecs.sales.api.IMarketService;
import com.ai.ecs.sales.entity.CouponBatchDetail;
import com.ai.ecs.sales.entity.MarketInfo;

@Controller
@RequestMapping("shopInfo")
public class ShopInfoController extends BaseController
{
    @Autowired
    ILoginService loginService;
    @Autowired
    IMarketService marketService;
    @Autowired
    ICouponService couponService;
    
   
    private static final Logger logger = LoggerFactory.getLogger(ShopInfoController.class);

    /**
     * 判断是否已经登录
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "isLogin",method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> isLogin(HttpServletRequest request,Map paramMap) throws Exception 
    {
        Map<String, Object> returnMap = new HashMap<String, Object>(); // 存储栏目信息
        
        MemberVo member = UserUtils.getLoginUser(request);
        if(member == null)
        {
            returnMap.put("isLogin", "1");// 未登录
        }
        else
        {
            returnMap.put("isLogin", "0");// 已登录
            returnMap.put("nickName", member.getMemberInfo().getMemberNickname());
            returnMap.put("memberId", member.getMemberInfo().getMemberId());
        }
        return returnMap;
    }
    /**
     * 查询店铺优惠券
     * @param paramMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "queryTicketList",method = RequestMethod.POST)
    public @ResponseBody List<CouponBatchDetail> queryTicketList(Long shopId) throws Exception 
    {
        return couponService.queryCouponBatchDetailByShopId(shopId);
    }
    /**
     * 领取优惠券
     * @param Long
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "receiveTicket",method = RequestMethod.POST)
    public @ResponseBody Map<String,Object>  receiveTicket(HttpServletRequest request,Map params) throws Exception 
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        MemberVo member = UserUtils.getLoginUser(request);
        if(member==null)
        {
            resultMap.put("flag", "0");
            resultMap.put("resultMessage", "您未登录，请登录");
            return resultMap;
        }
        params.put("memberId",member.getMemberLogin().getMemberId().toString());
        params.put("memberLoginName",member.getMemberLogin().getMemberLogingName());
        String message =  couponService.receiveCoupon(params);
        resultMap.put("flag", "1");
        resultMap.put("resultMessage", message);
         
       return resultMap;
    }
    
  
    
}
