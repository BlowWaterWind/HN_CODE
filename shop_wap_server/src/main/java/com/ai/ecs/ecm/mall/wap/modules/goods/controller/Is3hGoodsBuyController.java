package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.goods.service.GoodsService;
import com.ai.ecs.ecm.mall.wap.platform.utils.RealAndAvailable;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils.getLoginUser;

/**
 * Created by xtf on 2016/11/4.
 * 判断是否是3高用户购买特定商品（集团专区）
 */
@RequestMapping(value="is3hGoodsBuy")
@Controller
public class Is3hGoodsBuyController extends BaseController {
    private Logger logger = Logger.getLogger(Is3hGoodsBuyController.class);

    // 集团购机比较时间
    private static String compareTime = new PropertiesLoader("mall.properties").getProperty("compareTime");

    @Autowired
    GoodsService goodsService;
    /**
     * 查询是否是集团专区的商品，并且有时间控制
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value="isGroupProduct3hBuy")
    public String isGroupProduct(HttpServletRequest request,Long goodsId,Long goodsSkuId,String goodsPrice){
        try{
            //判断是否是集团购机商品
            boolean isAfterCurrentDate = goodsService.isAfterCurrentDate(compareTime);
            boolean isCustomizeOrVotelTerminal = goodsService.isCustomizeOrVotelTerminal(goodsId);
            if(!isAfterCurrentDate||!isCustomizeOrVotelTerminal){
                return "-1";
            }
            //判断用户是否登录
            MemberVo memberVo = getLoginUser(request);
            if(memberVo==null){
                return "1";
            }
            MemberLogin memberLogin = memberVo.getMemberLogin();
            if(memberLogin==null){
                return "1";
            }
            //用户已经登录
            //判断是否是3高用户
            boolean is3Higher = goodsService.is3HighUser(memberLogin);

            if(isAfterCurrentDate&&isCustomizeOrVotelTerminal&&is3Higher){//符合
                String phoneNum = null;
                if(memberLogin.getMemberTypeId()==2){//手机登录
                    phoneNum = memberLogin.getMemberLogingName();
                }else{
                    return "-1";
                }
                return RealAndAvailable.isAvailable(memberLogin.getMemberId(),goodsPrice, phoneNum);
            }else {
                logger.info("非3高用户或者非集团专区，正常购买");
                return "-1";
            }
        }catch (Exception e){
            logger.error("查询出错："+e.getMessage());
        }
        return "-1";//查询出错
    }
}
