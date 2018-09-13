package com.ai.ecs.ecm.mall.wap.modules.goods.controller;

import com.ai.ecs.common.utils.Collections3;
import com.ai.ecs.ecm.mall.wap.common.CommonParams;
import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecsite.util.BeanMapUtil;
import com.ai.ecs.goods.api.IGoodsManageService;
import com.ai.ecs.member.api.IMemberMarketSignInService;
import com.ai.ecs.member.api.IWtjkzqUsrDetailService;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.TfMemberMarketSignIn;
import com.ai.ecs.sales.api.IMarketService;
import com.ai.ecs.sales.entity.MarketChannelRef;
import com.ai.ecs.sales.entity.MarketInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 会员活动控制器
 * 1、活动：2016奥运会，加油中国
 * Created by wangqiang11 on 2016/8/2
 */
@Controller
@RequestMapping("goodsBuy")
public class GoodsMarketController extends BaseController {
    @Autowired
    IMemberMarketSignInService signInService;
    @Autowired
    IMarketService marketService;
    @Autowired
    IGoodsManageService goodsManageService;
    @Autowired
    IWtjkzqUsrDetailService wtjkzqUsrDetailService;

    /**
     * 校验是否已签到
     * @param request
     * @param signIn
     * @return
     */
    @RequestMapping("/verifyIsSignedIn")
    @ResponseBody
    public String verifyIsSignedIn(HttpServletRequest request , TfMemberMarketSignIn signIn ,String callback){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
            Long memberId = memberLogin.getMemberId();
            String phoneNumber = memberLogin.getMemberLogingName();
            signIn.setMemberId(memberId);
            signIn.setPhoneNumber(phoneNumber);

            boolean isSignedIn = signInService.queryMemberIsSignedIn(signIn);
            if(isSignedIn){
                resultMap.put("isSignedIn","Y");
            }else {
                resultMap.put("isSignedIn","N");
            }
        } catch (Exception e) {
            logger.error("校验是否已签到异常" + e);
        }

        String resultMapJSON = JSONObject.toJSONString(resultMap);
        return callback + "(" + resultMapJSON + ")";
    }

    /**
     * 会员活动签到
     * @param signIn
     * @return
     */
    @RequestMapping("/memberSignIn")
    @ResponseBody
    public String memberSignIn(HttpServletRequest request , TfMemberMarketSignIn signIn ,String callback){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
            Long memberId = memberLogin.getMemberId();
            String phoneNumber = memberLogin.getMemberLogingName();
            signIn.setMemberId(memberId);
            signIn.setPhoneNumber(phoneNumber);
            boolean isSignedIn = signInService.queryMemberIsSignedIn(signIn);
            if(isSignedIn){
                resultMap.put("resultCode","fail");
                resultMap.put("resultInfo","您今天已经签到过了哦");
            }else{
                boolean signInSuccess = signInService.memberSignIn(signIn);
                if(signInSuccess){
                    resultMap.put("resultCode","success");
                    resultMap.put("resultInfo","签到成功");
                }else{
                    resultMap.put("resultCode","fail");
                    resultMap.put("resultInfo","签到失败，请稍后再试");
                }
            }
        } catch (Exception e) {
            logger.error("签到失败" + e);
            resultMap.put("resultCode","fail");
            resultMap.put("resultInfo","签到失败" + e);
        }
        String resultMapJSON = JSONObject.toJSONString(resultMap);
        return callback + "(" + resultMapJSON + ")";
    }

    /**
     * 校验秒杀资格
     * @param request
     * @param signIn
     * @return
     */
    @RequestMapping("/verifySecKillQualified")
    @ResponseBody
    public String verifySecKillQualified(HttpServletRequest request,TfMemberMarketSignIn signIn ,String callback){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            MemberLogin memberLogin = UserUtils.getLoginUser(request).getMemberLogin();
            Long memberId = memberLogin.getMemberId();
            String phoneNumber = memberLogin.getMemberLogingName();
            signIn.setMemberId(memberId);
            signIn.setPhoneNumber(phoneNumber);

            boolean verifyIsSuccess = signInService.verifySecKillQualified(signIn);
            if(verifyIsSuccess){
                resultMap.put("resultCode","success");
                resultMap.put("resultInfo","校验通过");
            }else{
                resultMap.put("resultCode","fail");
                resultMap.put("resultInfo","您没有秒杀资格");
            }
        } catch (Exception e) {
            logger.error("校验秒杀资格异常" + e);
            resultMap.put("resultCode","fail");
            resultMap.put("resultInfo","您没有秒杀资格"+e);
        }
        String resultMapJSON = JSONObject.toJSONString(resultMap);
        return callback + "(" + resultMapJSON + ")";
    }

    /**
     * 查询奥运会活动
     * @return
     */
    @RequestMapping("/queryOlympicMarketList")
    @ResponseBody
    public String queryOlympicMarketList(String callback){
        try {
            List<MarketInfo> marketInfoList = marketService.queryOlympicMarketList();
            if (Collections3.isEmpty(marketInfoList)){
                return null;
            }

            List<Map<String, Object>> resultList = Lists.newArrayList();
            for (MarketInfo marketInfo : marketInfoList) {
                Map<String, Object> beanMap = BeanMapUtil.bean2Map(marketInfo);
                List<MarketChannelRef> marketChannelRefList = marketInfo.getMarketChannelRefList();
                if (Collections3.isEmpty(marketChannelRefList)){
                    continue;
                }

                for (MarketChannelRef marketChannelRef : marketChannelRefList) {
                    if(CommonParams.CHANNEL_CODE.equals(marketChannelRef.getChannelCode())){
                        beanMap.put("pageUrl",marketChannelRef.getPageUrl());
                    }
                }
                resultList.add(beanMap);
            }
            String resultListJSON = JSONArray.toJSONString(resultList);
            return callback + "(" + resultListJSON + ")";
        } catch (Exception e) {
            logger.error("查询奥运会活动异常" + e);
        }
        return null;
    }

    /**
     * 验证是否三高客户
     * @param mobilePhoneNo
     * @return
     */
    @RequestMapping("/verifyIs3HighUser")
    @ResponseBody
    public Map<String,Object> verifyIs3HighUser(String mobilePhoneNo){
        Map<String,Object> resultMap = Maps.newHashMap();
        try {
            boolean is3HighUser = wtjkzqUsrDetailService.checkJkzqUsr(mobilePhoneNo);
            resultMap.put("is3HighUser",is3HighUser);
        } catch (Exception e) {
            resultMap.put("is3HighUser",false);
            e.printStackTrace();
        }
        return resultMap;
    }

}
